package org.nakeduml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;


public abstract class ManyMergeNodeObjectTokenUnknownWithInControlToken extends MergeNodeObjectTokenUnknownWithInControlToken<CollectionObjectToken<?>> {

	public ManyMergeNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public ManyMergeNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public ManyMergeNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ManyObjectFlowUnknown getOutFlow();
	
	@Override
	protected abstract List<? extends ActivityEdge<Token>> getInFlows();

}
