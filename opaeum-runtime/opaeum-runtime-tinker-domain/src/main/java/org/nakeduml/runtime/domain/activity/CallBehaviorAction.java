package org.nakeduml.runtime.domain.activity;

import org.nakeduml.runtime.domain.activity.interf.ICallBehaviorAction;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class CallBehaviorAction extends CallAction implements ICallBehaviorAction {

	private static final long serialVersionUID = 8168069329356552298L;

	public CallBehaviorAction() {
		super();
	}

	public CallBehaviorAction(boolean persist, String name) {
		super(persist, name);
	}

	public CallBehaviorAction(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public abstract AbstractActivity getBehavior();

}
