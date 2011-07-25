package net.sf.nakeduml.metamodel.activities.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.nakeduml.metamodel.actions.INakedAcceptEventAction;
import net.sf.nakeduml.metamodel.activities.ActivityKind;
import net.sf.nakeduml.metamodel.activities.INakedAction;
import net.sf.nakeduml.metamodel.activities.INakedActivity;
import net.sf.nakeduml.metamodel.activities.INakedActivityEdge;
import net.sf.nakeduml.metamodel.activities.INakedActivityNode;
import net.sf.nakeduml.metamodel.activities.INakedActivityPartition;
import net.sf.nakeduml.metamodel.activities.INakedActivityVariable;
import net.sf.nakeduml.metamodel.activities.INakedParameterNode;
import net.sf.nakeduml.metamodel.bpm.INakedEmbeddedSingleScreenTask;
import net.sf.nakeduml.metamodel.commonbehaviors.INakedTrigger;
import net.sf.nakeduml.metamodel.commonbehaviors.internal.NakedBehaviorImpl;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedElementOwner;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.core.INakedTypedElement;
import net.sf.nakeduml.metamodel.core.internal.ArtificialProperty;
import net.sf.nakeduml.metamodel.core.internal.emulated.TypedElementPropertyBridge;
import nl.klasse.octopus.model.IAttribute;

public class NakedActivityImpl extends NakedBehaviorImpl implements INakedActivity {
	private static final long serialVersionUID = -8111895180462880035L;
	public static final String META_CLASS = "activity";
	private ActivityKind activityKind;
	private Set<INakedActivityEdge> activityEdges = new HashSet<INakedActivityEdge>();
	private Set<INakedActivityNode> activityNodes = new HashSet<INakedActivityNode>();
	private Set<INakedActivityPartition> partitions = new HashSet<INakedActivityPartition>();
	private Set<INakedActivityVariable> variables = new HashSet<INakedActivityVariable>();

	public Set<INakedActivityPartition> getPartitions() {
		return this.partitions;
	}

	public Collection<INakedActivityNode> getStartNodes() {
		Collection<INakedActivityNode> results = new ArrayList<INakedActivityNode>();
		for (INakedActivityNode node : getActivityNodes()) {
			if (node instanceof INakedParameterNode) {
				INakedParameterNode parmNode = (INakedParameterNode) node;
				// Ignore parameter nodes that have no outgoing edges, e.g.
				// out-parameters
				if (parmNode.getParameter().isArgument() && parmNode.getAllEffectiveIncoming().isEmpty()) {
					results.add(node);
				}
			} else if (node.getAllEffectiveIncoming().isEmpty()) {
				if (!(node instanceof INakedAction && ((INakedAction) node).handlesException())) {
					results.add(node);
				}
			}
		}
		return results;
	}

	@Override
	public String getMetaClass() {
		return META_CLASS;
	}

	public boolean isProcess() {
		return getActivityKind() == ActivityKind.PROCESS;
	}

	@Override
	public void addOwnedElement(INakedElement element) {
		super.addOwnedElement(element);
		if (element instanceof INakedActivityPartition) {
			this.partitions.add((INakedActivityPartition) element);
		}
		if (element instanceof INakedActivityNode) {
			this.activityNodes.add((INakedActivityNode) element);
		}
		if (element instanceof INakedActivityEdge) {
			this.activityEdges.add((INakedActivityEdge) element);
		}
		if (element instanceof INakedActivityVariable) {
			this.variables.add((INakedActivityVariable) element);
		}
	}
	public void removeOwnedElement(INakedElement element){
		super.removeOwnedElement(element);
		if (element instanceof INakedActivityPartition) {
			this.partitions.remove((INakedActivityPartition) element);
		}
		if (element instanceof INakedActivityNode) {
			this.activityNodes.remove((INakedActivityNode) element);
		}
		if (element instanceof INakedActivityEdge) {
			this.activityEdges.remove((INakedActivityEdge) element);
		}
		if (element instanceof INakedActivityVariable) {
			this.variables.remove((INakedActivityVariable) element);
		}
	}

	@Override
	protected List<IAttribute> getAllAttributesForOcl(boolean classScope) {
		List<IAttribute> results = super.getAllAttributesForOcl(classScope);
		if (!classScope) {
			for (INakedActivityNode node : getActivityNodesRecursively()) {
				if (node instanceof INakedParameterNode) {
					results.add(new TypedElementPropertyBridge(this, (INakedTypedElement) node));
				}
			}
		}
		return results;
	}

	public List<INakedActivityNode> getActivityNodesRecursively() {
		List<INakedElement> children = new ArrayList<INakedElement>(this.activityNodes);
		List<INakedActivityNode> result = new ArrayList<INakedActivityNode>();
		addNodesRecursively(children, result);
		return result;
	}

	private static void addNodesRecursively(Collection<? extends INakedElement> children, List<? super INakedActivityNode> result) {
		for (INakedElement node : children) {
			if (node instanceof INakedActivityNode) {
				result.add((INakedActivityNode) node);
			}
			if (node instanceof INakedElementOwner) {
				addNodesRecursively(((INakedElementOwner) node).getOwnedElements(), result);
			}
		}
	}

	public Set<INakedActivityEdge> getActivityEdges() {
		return this.activityEdges;
	}

	public Set<INakedActivityNode> getActivityNodes() {
		return this.activityNodes;
	}

	public List<INakedElement> getAllMessageTriggers() {
		List<INakedElement> results = new ArrayList<INakedElement>();
		Iterator iter = getActivityNodesRecursively().iterator();
		while (iter.hasNext()) {
			INakedActivityNode element = (INakedActivityNode) iter.next();
			if (element instanceof INakedAcceptEventAction) {
				INakedAcceptEventAction acceptEventAction = (INakedAcceptEventAction) element;
				if (acceptEventAction.getTrigger() != null && acceptEventAction.getTrigger().getEvent() != null) {
					results.add(acceptEventAction.getTrigger().getEvent());
				}
			}
		}
		return results;
	}

	public Collection<INakedActivityVariable> getVariables() {
		return this.variables;
	}

	public ActivityKind getActivityKind() {
		return this.activityKind;
	}

	public void setActivityKind(ActivityKind activityKind) {
		this.activityKind = activityKind;
	}

	@Override
	public INakedProperty findEmulatedAttribute(INakedAction node){
		for(INakedProperty p:this.ownedAttributes){
			if(p instanceof ArtificialProperty && p.getName().equalsIgnoreCase(node.getName())){
				return p;
			}
		}
		return null;
		
	}

}
