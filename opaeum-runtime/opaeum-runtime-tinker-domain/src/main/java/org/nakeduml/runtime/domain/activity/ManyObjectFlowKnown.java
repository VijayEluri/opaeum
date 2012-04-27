package org.nakeduml.runtime.domain.activity;

import java.util.Collection;

import com.tinkerpop.blueprints.pgm.Edge;

public abstract class ManyObjectFlowKnown<O> extends ObjectFlowKnown<O, CollectionObjectToken<O>> {

	public ManyObjectFlowKnown(Edge edge) {
		super(edge);
	}
	
	protected Edge getEdge() {
		return this.edge;
	}
	
	protected abstract boolean evaluateGuardConditions(Collection<O> tokenValue);
	
	@Override
	protected boolean evaluateGuardConditions(CollectionObjectToken<O> token) {
		return evaluateGuardConditions(token.getElements());	
	}
	
	public ManyObjectFlowUnknown convertToUnknownObjectFlow() {
		return new ManyObjectFlowUnknown(ManyObjectFlowKnown.this.edge) {

			@SuppressWarnings("unchecked")
			@Override
			protected boolean evaluateGuardConditions(Collection<?> tokenValue) {
				return ManyObjectFlowKnown.this.evaluateGuardConditions((Collection<O>) tokenValue);
			}

			@Override
			protected <IN extends Token, OUT extends Token> ActivityNode<IN, OUT> getTarget() {
				return ManyObjectFlowKnown.this.getTarget();
			}

			@Override
			protected <IN extends Token, OUT extends Token> ActivityNode<IN, OUT> getSource() {
				return ManyObjectFlowKnown.this.getSource();
			}

			@Override
			public String getName() {
				return  ManyObjectFlowKnown.this.getName();
			}

			@Override
			protected int getWeigth() {
				return ManyObjectFlowKnown.this.getWeigth();
			}
			
		};
	}

}
