package org.nakeduml.runtime.domain.activity;

import org.nakeduml.runtime.domain.activity.interf.ICallOperationAction;
import org.nakeduml.runtime.domain.activity.interf.IInputPin;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class CallOperationAction extends CallAction implements ICallOperationAction {

	private static final long serialVersionUID = 5673405797104866039L;

	public CallOperationAction() {
		super();
	}

	public CallOperationAction(boolean persist, String name) {
		super(persist, name);
	}

	public CallOperationAction(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public abstract IInputPin<?, ?> getTarget();
	
	//TODO implements this
	@Override
	public boolean isSynchronous() {
		return true;
	}
	
}
