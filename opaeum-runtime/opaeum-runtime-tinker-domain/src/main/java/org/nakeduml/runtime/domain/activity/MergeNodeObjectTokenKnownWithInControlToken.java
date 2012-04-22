package org.nakeduml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;


public abstract class MergeNodeObjectTokenKnownWithInControlToken<O, OUT extends ObjectToken<O>> extends MergeNode<Token, OUT> {

	public MergeNodeObjectTokenKnownWithInControlToken() {
		super();
	}

	public MergeNodeObjectTokenKnownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public MergeNodeObjectTokenKnownWithInControlToken(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ObjectFlowKnown<O,OUT> getOutFlow();
	
	@Override
	protected abstract List<? extends ActivityEdge<Token>> getInFlows();

}
