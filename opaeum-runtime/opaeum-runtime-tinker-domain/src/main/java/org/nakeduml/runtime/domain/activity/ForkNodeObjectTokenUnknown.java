package org.nakeduml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ForkNodeObjectTokenUnknown<IN extends ObjectToken<?>> extends ForkNode<IN> {

	public ForkNodeObjectTokenUnknown() {
		super();
	}

	public ForkNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public ForkNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}

}
