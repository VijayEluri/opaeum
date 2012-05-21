package org.nakeduml.runtime.domain.activity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.nakeduml.runtime.domain.activity.interf.IInputPin;
import org.nakeduml.runtime.domain.activity.interf.IOutputPin;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class OpaqueAction<R, OUT extends ObjectToken<R>> extends Action {

	private static final long serialVersionUID = -518632734483685424L;

	public OpaqueAction() {
		super();
	}

	public OpaqueAction(boolean persist, String name) {
		super(persist, name);
	}

	public OpaqueAction(Vertex vertex) {
		super(vertex);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends IOutputPin<R,OUT>> getOutput() {
		OutputPin<R,OUT> resultPin = getResultPin();
		if (resultPin != null) {
			return Arrays.<OutputPin<R,OUT>> asList(resultPin);
		} else {
			return Collections.<OutputPin<R,OUT>> emptyList();
		}
	}
	
	@Override
	public abstract List<? extends IInputPin<?,?>> getInput();
	
	protected abstract OutputPin<R,OUT> getResultPin();

}
