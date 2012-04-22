package org.nakeduml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ManyForkNodeObjectTokenUnknown extends ForkNodeObjectTokenUnknown<CollectionObjectToken<?>> {

	public ManyForkNodeObjectTokenUnknown() {
		super();
	}

	public ManyForkNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public ManyForkNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}

}
