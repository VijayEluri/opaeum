package org.opaeum.javageneration.basicjava.simpleactions;

import nl.klasse.octopus.codegen.umlToJava.maps.PropertyMap;

import org.eclipse.uml2.uml.CallBehaviorAction;
import org.opaeum.eclipse.EmfActionUtil;
import org.opaeum.eclipse.EmfBehaviorUtil;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.javageneration.basicjava.AbstractObjectNodeExpressor;

public class BehaviorCaller extends AbstractBehaviorCaller<CallBehaviorAction>{
	public BehaviorCaller(CallBehaviorAction action,AbstractObjectNodeExpressor expressor){
		super(action, expressor);
	}
	protected void maybeStartBehavior(OJAnnotatedOperation oper,OJBlock block,PropertyMap resultMap){
		if(EmfBehaviorUtil.isProcess(node.getBehavior())){
			block.addToStatements(resultMap.fieldname() + ".execute()");
		}
	}
	protected PropertyMap getResultMap(){
		PropertyMap resultMap = null;
		if(EmfBehaviorUtil.hasExecutionInstance(node.getBehavior())){
			resultMap = ojUtil.buildStructuralFeatureMap(node);
		}else{
			resultMap = ojUtil.buildStructuralFeatureMap(EmfActionUtil.getReturnPin( node));
		}
		return resultMap;
	}
	@Override
	protected boolean shouldStoreMessageStructureOnProcess(){
		return node.getBehavior().getContext() == null && EmfBehaviorUtil.hasExecutionInstance(node.getBehavior());
	}
}
