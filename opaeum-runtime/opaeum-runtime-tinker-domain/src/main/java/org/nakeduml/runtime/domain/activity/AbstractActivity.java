package org.nakeduml.runtime.domain.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nakeduml.runtime.domain.BaseTinkerSoftDelete;
import org.nakeduml.runtime.domain.TinkerCompositionNode;
import org.nakeduml.runtime.domain.activity.interf.IActivityEdge;
import org.nakeduml.runtime.domain.activity.interf.IActivityNode;
import org.nakeduml.runtime.domain.activity.interf.IEvent;
import org.nakeduml.runtime.domain.activity.interf.IInputPin;
import org.nakeduml.runtime.domain.activity.interf.IOutputPin;
import org.nakeduml.tinker.runtime.GraphDb;
import org.opaeum.runtime.domain.IntrospectionUtil;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class AbstractActivity extends BaseTinkerSoftDelete implements TinkerCompositionNode {

	private static final long serialVersionUID = 7647066355373095288L;

	public void setCallAction(CallAction callAction) {
		Edge edge = GraphDb.getDb().addEdge(null, this.vertex, callAction.getVertex(),"callAction");
		edge.setProperty("inClass", callAction.getClass().getName());
		edge.setProperty("outClass", IntrospectionUtil.getOriginalClass(this.getClass()).getName());
	}
	
	public CallAction getCallAction() {
		Iterable<Edge> iter1 = this.vertex.getOutEdges("callAction");
		if ( iter1.iterator().hasNext() ) {
			Edge edge = iter1.iterator().next();
			try {
				Class<?> c = Class.forName((String) edge.getProperty("inClass"));
				return (CallAction) c.getConstructor(Vertex.class).newInstance(edge.getInVertex());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
	
	protected abstract List<? extends ActivityNode<?, ?>> getInitialNodes();

	public Set<IActivityNode<? extends Token, ? extends Token>> getEnabledNodesWithMatchingTrigger(IEvent event) {
		Set<IActivityNode<? extends Token, ? extends Token>> result = new HashSet<IActivityNode<? extends Token, ? extends Token>>();
		Set<IActivityNode<? extends Token, ? extends Token>> visited = new HashSet<IActivityNode<? extends Token, ? extends Token>>();
		for (IActivityNode<? extends Token, ? extends Token> initNode : getInitialNodes()) {
			walkActivity(result, visited, initNode, event);
		}
		return result;
	}

	public Set<IActivityNode<? extends Token, ? extends Token>> getNodesForStatus(NodeStatus... nodeStatuses) {
		Set<IActivityNode<? extends Token, ? extends Token>> result = new HashSet<IActivityNode<? extends Token, ? extends Token>>();
		Set<IActivityNode<? extends Token, ? extends Token>> visited = new HashSet<IActivityNode<? extends Token, ? extends Token>>();
		for (ActivityNode<? extends Token, ? extends Token> initNode : getInitialNodes()) {
			walkActivity(result, visited, initNode, nodeStatuses);
		}
		return result;
	}

	public IActivityNode<? extends Token, ? extends Token> getNodeForName(String name) {
		Set<IActivityNode<? extends Token, ? extends Token>> result = new HashSet<IActivityNode<? extends Token, ? extends Token>>();
		Set<IActivityNode<? extends Token, ? extends Token>> visited = new HashSet<IActivityNode<? extends Token, ? extends Token>>();
		for (IActivityNode<? extends Token, ? extends Token> initNode : getInitialNodes()) {
			walkActivity(result, visited, initNode, name);
			if (!result.isEmpty()) {
				break;
			}
		}
		if (result.isEmpty()) {
			return null;
		} else {
			return result.iterator().next();
		}
	}

	public Set<IActivityNode<? extends Token, ? extends Token>> getActiveNodes() {
		return getNodesForStatus(NodeStatus.ACTIVE);
	}

	public Set<IActivityNode<? extends Token, ? extends Token>> getEnabledNodes() {
		return getNodesForStatus(NodeStatus.ENABLED);
	}

	public Set<IActivityNode<? extends Token, ? extends Token>> getInactiveNodes() {
		return getNodesForStatus(NodeStatus.INACTIVE);
	}

	public Set<IActivityNode<? extends Token, ? extends Token>> getCompletedNodes() {
		return getNodesForStatus(NodeStatus.COMPLETE);
	}

	private void walkActivity(Set<IActivityNode<? extends Token, ? extends Token>> result, Set<IActivityNode<? extends Token, ? extends Token>> visited,
			IActivityNode<? extends Token, ? extends Token> currentNode, IEvent event) {
		if (currentNode.isEnabled() && currentNode instanceof AcceptEventAction && ((AcceptEventAction) currentNode).containsTriggerForEvent(event)) {
			result.add(currentNode);
		}
		List<? extends IActivityEdge<? extends Token>> outgoing = currentNode.getOutgoing();
		for (IActivityEdge<? extends Token> outFlow : outgoing) {
			IActivityNode<? extends Token, ? extends Token> target = outFlow.getTarget();
			if (!visited.contains(target)) {
				walkActivity(result, visited, target, event);
			} else {
				continue;
			}
		}
	}

	private void walkActivity(Set<IActivityNode<? extends Token, ? extends Token>> result, Set<IActivityNode<? extends Token, ? extends Token>> visited,
			IActivityNode<? extends Token, ? extends Token> currentNode, NodeStatus... nodeStatuses) {
		for (NodeStatus nodeStatus : nodeStatuses) {
			if (currentNode.getNodeStatus() == nodeStatus) {
				result.add(currentNode);
				break;
			}
		}
		List<? extends IActivityEdge<? extends Token>> outgoing = currentNode.getOutgoing();
		for (IActivityEdge<? extends Token> outFlow : outgoing) {
			IActivityNode<? extends Token, ? extends Token> target = outFlow.getTarget();
			if (!visited.contains(target)) {
				walkActivity(result, visited, target, nodeStatuses);
			} else {
				continue;
			}
		}
	}

	private void walkActivity(Set<IActivityNode<? extends Token, ? extends Token>> result, Set<IActivityNode<? extends Token, ? extends Token>> visited,
			IActivityNode<? extends Token, ? extends Token> currentNode, String name) {
		if (currentNode.getName().equals(name)) {
			result.add(currentNode);
			return;
		}
		List<? extends IActivityEdge<? extends Token>> outgoing = currentNode.getOutgoing();
		for (IActivityEdge<? extends Token> outFlow : outgoing) {
			IActivityNode<? extends Token, ? extends Token> target = outFlow.getTarget();
			if (!visited.contains(target)) {
				walkActivity(result, visited, target, name);
			} else {
				continue;
			}
		}
		if (currentNode instanceof Action) {
			for (IOutputPin<?,?> outputPin : ((Action) currentNode).getOutput()) {
				walkActivity(result, visited, outputPin, name);
			}
		} else if (currentNode instanceof InputPin) {
			IInputPin<?,?> inputPin = (IInputPin<?,?>) currentNode;
			walkActivity(result, visited, inputPin.getAction(), name);
		}
	}

}
