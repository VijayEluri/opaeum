package org.nakeduml.runtime.domain.activity;

import java.util.Collections;
import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class OutputPin<O> extends ObjectNode<O> {

	public OutputPin() {
		super();
	}

	public OutputPin(boolean persist, String name) {
		super(persist, name);
	}

	public OutputPin(Vertex vertex) {
		super(vertex);
	}

	protected abstract int getLowerMultiplicity();

	protected abstract int getUpperMultiplicity();

	protected abstract Action getAction();

	protected Boolean executeNode() {
		Action action = this.getAction();
		if (action.mayContinue()) {
			return action.executeNode();
		} else {
			return false;
		}
	}

	protected boolean isLowerMultiplicityReached() {
		return getInTokens().size() >= getLowerMultiplicity();
	}

	protected boolean isUpperMultiplicityReached() {
		return getInTokens().size() >= getUpperMultiplicity();
	}

	// TODO think about upper
	@Override
	protected boolean mayContinue() {
		return isLowerMultiplicityReached();
	}
	
	@Override
	protected List<? extends ActivityEdge<ObjectToken<O>>> getInFlows() {
		return Collections.emptyList();
	}
	
}
