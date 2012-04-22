package org.nakeduml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ManyJoinNodeObjectTokenUnknownWithInControlToken extends JoinNodeObjectTokenUnknownWithInControlToken<CollectionObjectToken<?>> {

	public ManyJoinNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public ManyJoinNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}

	public ManyJoinNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	protected abstract ManyObjectFlowUnknown getOutFlow();


	@Override
	protected abstract List<ActivityEdge<Token>> getInFlows();

}
