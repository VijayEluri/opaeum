package org.nakeduml.runtime.domain.activity;

import org.nakeduml.runtime.domain.activity.interf.IPin;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class Pin<O,IN extends ObjectToken<O>,OUT extends ObjectToken<O>> extends ObjectNode<O, IN, OUT> implements IPin<O,IN,OUT> {
	
	public Pin() {
		super();
	}

	public Pin(boolean persist, String name) {
		super(persist, name);
	}

	public Pin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract int getLowerMultiplicity();

	@Override
	public abstract int getUpperMultiplicity();

	protected boolean isLowerMultiplicityReached() {
		int size = countNumberOfElementsOnTokens();
		return size >= getLowerMultiplicity();
	}

	protected abstract int countNumberOfElementsOnTokens();

	@Override
	public boolean mayContinue() {
		return isLowerMultiplicityReached();
	}

	protected boolean isUpperMultiplicityReached() {
		int size = countNumberOfElementsOnTokens();
		return size >= getUpperMultiplicity();
	}

}
