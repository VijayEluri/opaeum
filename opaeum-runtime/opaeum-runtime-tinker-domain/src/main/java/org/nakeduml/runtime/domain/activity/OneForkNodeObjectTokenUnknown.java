package org.nakeduml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class OneForkNodeObjectTokenUnknown extends ForkNodeObjectTokenUnknown<SingleObjectToken<?>> {

	public OneForkNodeObjectTokenUnknown() {
		super();
	}

	public OneForkNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public OneForkNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}

}
