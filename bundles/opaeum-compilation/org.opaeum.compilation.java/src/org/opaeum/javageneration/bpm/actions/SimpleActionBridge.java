package org.opaeum.javageneration.bpm.actions;

import org.eclipse.uml2.uml.ActivityNode;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.javageneration.basicjava.simpleactions.SimpleNodeBuilder;
import org.opaeum.javageneration.util.OJUtil;

public class SimpleActionBridge extends Jbpm5ActionBuilder<ActivityNode> {
	private SimpleNodeBuilder<?> delegate;

	public SimpleActionBridge(OJUtil ojUtil, ActivityNode node, SimpleNodeBuilder<?> delegate) {
		super(ojUtil, node);
		this.delegate = delegate;
	}

	@Override
	public void implementActionOn(OJAnnotatedOperation oper) {
		if (delegate != null) {
			delegate.implementActionOn(oper, oper.getBody());
		}
	}
}
