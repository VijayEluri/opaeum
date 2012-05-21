package org.nakeduml.runtime.domain.activity;

import org.nakeduml.runtime.domain.activity.interf.IActivityParameterNode;
import org.opaeum.runtime.domain.CompositionNode;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ActivityParameterNode<O,OUT extends ObjectToken<O>> extends ObjectNode<O,OUT,OUT> implements IActivityParameterNode<O,OUT> {

	private static final long serialVersionUID = 2412762156421899453L;

	public ActivityParameterNode() {
		super();
	}

	public ActivityParameterNode(boolean persist, String name) {
		super(persist, name);
	}

	public ActivityParameterNode(Vertex vertex) {
		super(vertex);
	}

	@Override
	public boolean mayContinue() {
		return true;
	}
	
	@Override
	public CompositionNode getOwningObject() {
		return getActivity();
	}
	
}
