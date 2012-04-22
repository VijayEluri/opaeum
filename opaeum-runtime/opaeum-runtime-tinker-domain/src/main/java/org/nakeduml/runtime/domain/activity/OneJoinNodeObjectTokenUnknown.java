package org.nakeduml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class OneJoinNodeObjectTokenUnknown extends JoinNodeObjectTokenUnknown<SingleObjectToken<?>> {

	public OneJoinNodeObjectTokenUnknown() {
		super();
	}
	
	public OneJoinNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}	

	public OneJoinNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}
	
	@Override
	protected abstract OneObjectFlowUnknown getOutFlow();

	@Override
	protected abstract List<OneObjectFlowUnknown> getInFlows();
	
}
