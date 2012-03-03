package org.nakeduml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Edge;

public abstract class ObjectFlowUnknown extends ActivityEdge<ObjectToken<?>> {

	public ObjectFlowUnknown(Edge edge) {
		super(edge);
	}

	public <T> ObjectFlowKnown<T> convertToKnown() {
		return new ObjectFlowKnown<T>(ObjectFlowUnknown.this.edge) {

			@SuppressWarnings("unchecked")
			@Override
			protected ActivityNode<ObjectToken<T>> getTarget() {
				return ObjectFlowUnknown.this.getTarget();
			}

			@SuppressWarnings("unchecked")
			@Override
			protected ActivityNode<ObjectToken<T>> getSource() {
				return ObjectFlowUnknown.this.getSource();
			}

			@Override
			public String getName() {
				return ObjectFlowUnknown.this.getName();
			}

			@Override
			protected boolean evaluateGuardConditions(ObjectToken<T> token) {
				return ObjectFlowUnknown.this.evaluateGuardConditions(token);
			}

			@Override
			protected int getWeigth() {
				return ObjectFlowUnknown.this.getWeigth();
			}
		};
	}

}