package org.nakeduml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class DecisionObjectTokenUnknown<IN extends ObjectToken<?>> extends DecisionNode<IN> {

	public DecisionObjectTokenUnknown() {
		super();
	}

	public DecisionObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public DecisionObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract ObjectFlowUnknown<IN> getInFlow();

	@Override
	protected abstract List<? extends ObjectFlowUnknown<IN>> getOutFlows();

	@Override
	protected List<ObjectFlowUnknown<IN>> getInFlows() {
		List<ObjectFlowUnknown<IN>> result = new ArrayList<ObjectFlowUnknown<IN>>();
		result.add(getInFlow());
		return result;
	}

}
