package org.nakeduml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ManyMergeNodeObjectTokenKnown<O> extends MergeNodeObjectTokenKnown<O, CollectionObjectToken<O>, CollectionObjectToken<O>> {

	public ManyMergeNodeObjectTokenKnown() {
		super();
	}

	public ManyMergeNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public ManyMergeNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract ManyObjectFlowKnown<O> getOutFlow();

	@Override
	protected abstract List<? extends ManyObjectFlowKnown<O>> getInFlows();

}
