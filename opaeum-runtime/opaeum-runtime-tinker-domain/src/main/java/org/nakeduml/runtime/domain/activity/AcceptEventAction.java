package org.nakeduml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.nakeduml.runtime.domain.TinkerNode;
import org.nakeduml.runtime.domain.activity.interf.IAcceptEventAction;
import org.nakeduml.runtime.domain.activity.interf.IEvent;
import org.nakeduml.runtime.domain.activity.interf.IInputPin;
import org.nakeduml.runtime.domain.activity.interf.IOutputPin;
import org.nakeduml.runtime.domain.activity.interf.ISignalEvent;
import org.nakeduml.runtime.domain.activity.interf.ITrigger;
import org.nakeduml.tinker.runtime.GraphDb;
import org.opaeum.runtime.domain.ISignal;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class AcceptEventAction extends Action implements IAcceptEventAction {

	private static final long serialVersionUID = 8562661610043434431L;
	private IEvent event;

	public AcceptEventAction() {
		super();
	}

	public AcceptEventAction(boolean persist, String name) {
		super(persist, name);
	}

	public AcceptEventAction(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public boolean execute() {
		return true;
	}
	
	@Override
	public Boolean processNextStart() throws NoSuchElementException {
		Boolean result = super.processNextStart();
		if (doAllIncomingFlowsHaveTokens() && hasPreConditionPassed() && hasPostConditionPassed()) {
			setNodeStatus(NodeStatus.ENABLED);
		}
		return result;
	}
	
	@Override
	public List<ControlToken> getInTokens() {
		List<ControlToken> result = new ArrayList<ControlToken>();
		Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + getName());
		for (Edge edge : iter) {
			result.add(new ControlToken(edge.getInVertex()));
		}
		return result;
	}
	
	@Override
	public List<? extends InputPin<?, ?>> getInput() {
		return Arrays.asList();
	}

	public void trigger(IEvent signal) {
		this.vertex.setProperty("triggered", true);
		this.event = signal;
	}
	
	@Override
	protected void addToInputPinVariable(IInputPin<?, ?> inputPin, Collection<?> elements) {
		//Will not be called
	}
	
	@Override
	protected void transferObjectTokensToAction() {
		copyEventToOutputPin(this.event);
		removeEvent(this.event);
	}	

	public abstract void copyEventToOutputPin(IEvent event);

	protected void removeEvent(IEvent event) {
		if (event instanceof ISignalEvent) {
			ISignal signal = ((ISignalEvent)event).getSignal();
			if (signal instanceof TinkerNode) {
				GraphDb.getDb().removeVertex(((TinkerNode) signal).getVertex());
			}
		}
		GraphDb.getDb().removeVertex(((TinkerNode)event).getVertex());
	}	
	
	@Override
	protected boolean isTriggered() {
		if (this.vertex.getProperty("triggered")==null) {
			return false;
		} else {
			return (Boolean) this.vertex.getProperty("triggered");
		}
	}

	@Override
	public abstract List<? extends ITrigger> getTrigger();
	
	public boolean containsTriggerForEvent(IEvent event) {
		for (ITrigger trigger : getTrigger()) {
			if (trigger.accepts(event)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<? extends IOutputPin<?,?>> getResult() {
		return getOutput();
	}

}
