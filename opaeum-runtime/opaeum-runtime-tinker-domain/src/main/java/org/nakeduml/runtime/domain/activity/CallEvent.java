package org.nakeduml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public class CallEvent extends Event {

	private static final long serialVersionUID = -467486969342220483L;

	public CallEvent(String name) {
		super(name);
	}

	public CallEvent(Vertex vertex) {
		super(vertex);
	}

}
