package org.nakeduml.runtime.domain.activity;

import org.nakeduml.runtime.domain.activity.interf.IExecutableNode;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ExecutableNode extends ActivityNode<ControlToken, ControlToken> implements IExecutableNode {

	private static final long serialVersionUID = 4587351057188861144L;

	public ExecutableNode() {
		super();
	}

	public ExecutableNode(boolean persist, String name) {
		super(persist, name);
	}

	public ExecutableNode(Vertex vertex) {
		super(vertex);
	}

}
