package org.nakeduml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;


public abstract class StructuralFeatureAction<O> extends Action {

	private static final long serialVersionUID = -3001102004461875759L;

	public StructuralFeatureAction() {
		super();
	}

	public StructuralFeatureAction(boolean persist, String name) {
		super(persist, name);
	}

	public StructuralFeatureAction(Vertex vertex) {
		super(vertex);
	}
	
	public abstract O getObject();
	
}
