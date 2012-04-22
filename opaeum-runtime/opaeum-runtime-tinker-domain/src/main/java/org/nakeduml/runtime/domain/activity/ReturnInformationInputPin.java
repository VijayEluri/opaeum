package org.nakeduml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ReturnInformationInputPin<O,OUT extends ObjectToken<O>> extends InputPin<O,OUT> {

	public ReturnInformationInputPin() {
		super();
	}

	public ReturnInformationInputPin(boolean persist, String name) {
		super(persist, name);
	}

	public ReturnInformationInputPin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract ReplyAction<?> getAction();
	
}
