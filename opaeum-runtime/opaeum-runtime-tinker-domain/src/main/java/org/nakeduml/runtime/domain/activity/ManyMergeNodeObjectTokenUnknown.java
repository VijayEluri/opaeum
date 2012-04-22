package org.nakeduml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;


public abstract class ManyMergeNodeObjectTokenUnknown extends MergeNodeObjectTokenUnknown<CollectionObjectToken<?>, CollectionObjectToken<?>> {

	public ManyMergeNodeObjectTokenUnknown() {
		super();
	}

	public ManyMergeNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public ManyMergeNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ManyObjectFlowUnknown getOutFlow();

	@Override
	protected abstract List<? extends ManyObjectFlowUnknown> getInFlows();

}
