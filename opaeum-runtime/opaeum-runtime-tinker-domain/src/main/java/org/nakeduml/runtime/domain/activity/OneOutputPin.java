package org.nakeduml.runtime.domain.activity;

import java.util.Collections;
import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class OneOutputPin<O> extends OutputPin<O, SingleObjectToken<O>> {

	public OneOutputPin() {
		super();
	}

	public OneOutputPin(boolean persist, String name) {
		super(persist, name);
	}

	public OneOutputPin(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract List<OneObjectFlowKnown<O>> getInFlows();

	@Override
	protected List<OneObjectFlowKnown<O>> getOutFlows() {
		return Collections.emptyList();
	}		
	
	@Override
	protected int countNumberOfElementsOnTokens() {
		return getOutTokens().size();
	}	

}
