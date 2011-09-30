package org.opeum.javageneration.jbpm5;

import org.opeum.javageneration.AbstractJavaProducingVisitor;
import org.opeum.javageneration.util.OJUtil;

import org.opeum.java.metamodel.OJIfStatement;
import org.opeum.java.metamodel.OJPathName;
import org.opeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opeum.java.metamodel.annotation.OJAnnotatedField;
import org.opeum.java.metamodel.annotation.OJAnnotatedOperation;

/**
 * Provides the behavior related logic common to statemachines and activities:
 * 
 * 
 */
public abstract class AbstractBehaviorVisitor extends AbstractJavaProducingVisitor{
	protected OJAnnotatedOperation addGetCallingProcessObject(OJAnnotatedClass ojOperationClass,OJPathName type){
		// getCAllbackLister
		OJAnnotatedOperation getCallbackListener = new OJAnnotatedOperation("getCallingProcessObject", type);
		ojOperationClass.addToOperations(getCallbackListener);
		OJIfStatement processInstanceNotNull = new OJIfStatement("getCallingProcessInstance()!=null ");
		getCallbackListener.getBody().addToStatements(processInstanceNotNull);
		OJAnnotatedField processObject = new OJAnnotatedField("processObject", type);
		processInstanceNotNull.getThenPart().addToLocals(processObject);
		processObject.setInitExp("(" + type.getLast() + ")getCallingProcessInstance().getVariable(\"processObject\")");
		processInstanceNotNull.getThenPart().addToStatements("return processObject");
		getCallbackListener.getBody().addToStatements("return null");
		return getCallbackListener;
	}

}
