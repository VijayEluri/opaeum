package org.opaeum.javageneration.jbpm5.activity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import nl.klasse.octopus.model.IClassifier;
import nl.klasse.octopus.stdlib.IOclLibrary;

import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJForStatement;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.javageneration.basicjava.AbstractObjectNodeExpressor;
import org.opaeum.javageneration.basicjava.OperationAnnotator;
import org.opaeum.javageneration.basicjava.SimpleActivityMethodImplementor;
import org.opaeum.javageneration.jbpm5.AbstractJavaProcessVisitor;
import org.opaeum.javageneration.jbpm5.EventUtil;
import org.opaeum.javageneration.jbpm5.Jbpm5Util;
import org.opaeum.javageneration.jbpm5.actions.AcceptEventActionBuilder;
import org.opaeum.javageneration.jbpm5.actions.CallBehaviorActionBuilder;
import org.opaeum.javageneration.jbpm5.actions.CallOperationActionBuilder;
import org.opaeum.javageneration.jbpm5.actions.EmbeddedScreenFlowTaskBuilder;
import org.opaeum.javageneration.jbpm5.actions.EmbeddedSingleScreenTaskBuilder;
import org.opaeum.javageneration.jbpm5.actions.Jbpm5ActionBuilder;
import org.opaeum.javageneration.jbpm5.actions.Jbpm5ObjectNodeExpressor;
import org.opaeum.javageneration.jbpm5.actions.ParameterNodeBuilder;
import org.opaeum.javageneration.jbpm5.actions.ReplyActionBuilder;
import org.opaeum.javageneration.jbpm5.actions.SimpleActionBridge;
import org.opaeum.javageneration.maps.NakedStructuralFeatureMap;
import org.opaeum.javageneration.maps.SignalMap;
import org.opaeum.javageneration.maps.StructuredActivityNodeMap;
import org.opaeum.javageneration.oclexpressions.CodeCleanup;
import org.opaeum.javageneration.oclexpressions.PreAndPostConditionGenerator;
import org.opaeum.javageneration.oclexpressions.ValueSpecificationUtil;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.linkage.BehaviorUtil;
import org.opaeum.linkage.CompositionEmulator;
import org.opaeum.linkage.NakedParsedOclStringResolver;
import org.opaeum.linkage.PinLinker;
import org.opaeum.linkage.ProcessIdentifier;
import org.opaeum.metamodel.actions.INakedAcceptCallAction;
import org.opaeum.metamodel.actions.INakedAcceptEventAction;
import org.opaeum.metamodel.actions.INakedCallBehaviorAction;
import org.opaeum.metamodel.actions.INakedCallOperationAction;
import org.opaeum.metamodel.actions.INakedReplyAction;
import org.opaeum.metamodel.actions.INakedSendSignalAction;
import org.opaeum.metamodel.activities.ActivityKind;
import org.opaeum.metamodel.activities.ActivityNodeContainer;
import org.opaeum.metamodel.activities.ControlNodeType;
import org.opaeum.metamodel.activities.INakedAction;
import org.opaeum.metamodel.activities.INakedActivity;
import org.opaeum.metamodel.activities.INakedActivityEdge;
import org.opaeum.metamodel.activities.INakedActivityNode;
import org.opaeum.metamodel.activities.INakedActivityVariable;
import org.opaeum.metamodel.activities.INakedControlNode;
import org.opaeum.metamodel.activities.INakedExpansionNode;
import org.opaeum.metamodel.activities.INakedExpansionRegion;
import org.opaeum.metamodel.activities.INakedObjectFlow;
import org.opaeum.metamodel.activities.INakedObjectNode;
import org.opaeum.metamodel.activities.INakedOutputPin;
import org.opaeum.metamodel.activities.INakedParameterNode;
import org.opaeum.metamodel.activities.INakedStructuredActivityNode;
import org.opaeum.metamodel.bpm.INakedEmbeddedScreenFlowTask;
import org.opaeum.metamodel.bpm.INakedEmbeddedSingleScreenTask;
import org.opaeum.metamodel.commonbehaviors.INakedBehavioredClassifier;
import org.opaeum.metamodel.core.INakedClassifier;
import org.opaeum.metamodel.core.INakedElement;
import org.opaeum.metamodel.core.INakedMessageStructure;
import org.opaeum.metamodel.core.INakedParameter;
import org.opaeum.runtime.domain.ExceptionHolder;

@StepDependency(phase = JavaTransformationPhase.class,requires = {
		OperationAnnotator.class,PinLinker.class,ProcessIdentifier.class,CompositionEmulator.class,NakedParsedOclStringResolver.class,CodeCleanup.class
},after = {
	OperationAnnotator.class,SimpleActivityMethodImplementor.class /*Needs repeatable sequence in the ocl generating steps*/
},before = CodeCleanup.class)
public class ActivityProcessImplementor extends AbstractJavaProcessVisitor{
	private void activityEdge(INakedActivityEdge edge){
		OJAnnotatedClass c = findJavaClass(edge.getActivity());
		if(edge instanceof INakedObjectFlow && ((INakedObjectFlow) edge).getTransformation() != null){
			SimpleActivityMethodImplementor.generateTransformationMultiplier(c, ((INakedObjectFlow) edge));
		}
		if(edge.hasGuard() && BehaviorUtil.hasExecutionInstance(edge.getActivity())){
			INakedActivityNode node = edge.getEffectiveSource();
			OJAnnotatedOperation oper = new OJAnnotatedOperation(Jbpm5Util.getGuardMethod(edge));
			c.addToOperations(oper);
			oper.setReturnType(new OJPathName("boolean"));
			ActivityUtil.setupVariables(oper, node);
			INakedActivityNode source = edge.getEffectiveSource();
			if(edge instanceof INakedObjectFlow){
				addObjectFlowVariable(edge, oper, (INakedObjectFlow) edge);
			}else if((source instanceof INakedControlNode && ((INakedControlNode) source).getControlNodeType() == ControlNodeType.DECISION_NODE)){
				// NB!! we are doing it here for both controlflows and
				// objectflows which is not entirely to uml spec but what the
				// heck,
				// looks like a good idea
				if(source.getIncoming().size() == 1 && source.getIncoming().iterator().next() instanceof INakedObjectFlow){
					INakedObjectFlow objectFlow = (INakedObjectFlow) source.getIncoming().iterator().next();
					addObjectFlowVariable(edge, oper, objectFlow);
				}
			}
			IClassifier booleanType = getOclEngine().getOclLibrary().lookupStandardType(IOclLibrary.BooleanTypeName);
			oper.getBody().addToStatements("return " + ValueSpecificationUtil.expressValue(oper, edge.getGuard(), node.getActivity(), booleanType));
			oper.addParam("context", Jbpm5Util.getProcessContext());
		}
	}
	private void addObjectFlowVariable(INakedActivityEdge edge,OJAnnotatedOperation oper,INakedObjectFlow objectFlow){
		INakedObjectNode origin = objectFlow.getOriginatingObjectNode();
		// TODO the originatingObjectNode may not have the correct type after
		// transformations and selections
		NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(edge.getActivity(), origin, false);
		OJAnnotatedField sourceField = new OJAnnotatedField(map.fieldname(), map.javaTypePath());
		oper.getBody().addToLocals(sourceField);
		AbstractObjectNodeExpressor expressor = new Jbpm5ObjectNodeExpressor(getLibrary());
		sourceField.setInitExp(expressor.expressFeedingNodeForObjectFlowGuard(oper.getBody(), objectFlow));
	}
	@VisitBefore(matchSubclasses = true)
	public void implementActivity(INakedActivity activity){
		if(activity.getActivityKind() != ActivityKind.SIMPLE_SYNCHRONOUS_METHOD){
			OJAnnotatedClass activityClasss = findJavaClass(activity);
			addParameterDelegation(activityClasss, activity);
			OJPathName stateClass = OJUtil.statePathname(activity);
			OJAnnotatedOperation getSelf = new OJAnnotatedOperation("getSelf", activityClasss.getPathName());
			getSelf.initializeResultVariable("this");
			activityClasss.addToOperations(getSelf);
			implementContainer(activity.getActivityKind() == ActivityKind.PROCESS, stateClass, activity, activity);
		}
	}
	private void implementContainer(boolean isProcess,OJPathName stateClass,ActivityNodeContainer container,INakedClassifier msg){
		OJAnnotatedClass activityClass = findJavaClass(msg);
		implementNodeMethods(container);
		doExecute(msg, activityClass, isProcess);
		if(isProcess){
			implementProcessInterfaceOperations(activityClass, stateClass, msg);
			OJOperation init = activityClass.findOperation("init", Arrays.asList(Jbpm5Util.getProcessContext()));
			EventUtil.requestEvents((OJAnnotatedOperation) init, container.getActivityNodes(), getLibrary().getBusinessRole() != null);
		}else{
			Jbpm5Util.implementRelationshipWithProcess(activityClass, false, "process");
			doIsStepActive(activityClass, msg);
			super.addGetNodeInstancesRecursively(activityClass);
			doIsComplete(activityClass, msg);
			doFindNodeInstanceByUniqueId(activityClass);
			addDirtyLogic(activityClass);
		}
		OJUtil.addTransientProperty(activityClass, Jbpm5ObjectNodeExpressor.EXCEPTION_FIELD, new OJPathName("Object"), true).setVisibility(OJVisibilityKind.PROTECTED);
		OJAnnotatedOperation complete = new OJAnnotatedOperation("completed");
		activityClass.addToOperations(complete);
		if(container.getPostConditions().size() > 0){
			complete.getBody().addToStatements("evaluatePostConditions()");
			OJUtil.addFailedConstraints(complete);
		}
		for(INakedActivityNode n:container.getActivityNodes()){
			if(n instanceof INakedStructuredActivityNode){
				INakedStructuredActivityNode san = (INakedStructuredActivityNode) n;
				INakedMessageStructure childMsg = san.getMessageStructure();
				OJAnnotatedClass c = findJavaClass(childMsg);
				OJAnnotatedOperation getter = (OJAnnotatedOperation) c.getUniqueOperation("getSelf");
				if(container instanceof INakedActivity){
					getter.initializeResultVariable("getNodeContainer()");
				}else{
					getter.initializeResultVariable("getNodeContainer().getSelf()");
				}
				OJAnnotatedOperation getActivity = new OJAnnotatedOperation("getContainingActivity", OJUtil.classifierPathname(container.getActivity()));
				c.addToOperations(getActivity);
				if(container instanceof INakedActivity){
					getActivity.initializeResultVariable("getNodeContainer()");
				}else{
					getActivity.initializeResultVariable("getNodeContainer().getContainingActivity()");
				}
				if(container.getActivity().getContext() != null){
					OJAnnotatedOperation contextGetter = (OJAnnotatedOperation) c.getUniqueOperation("getContextObject");
					contextGetter.initializeResultVariable("getSelf().getContextObject()");
				}
				implementVariableDelegation(container, msg, c);
				StructuredActivityNodeMap map = new StructuredActivityNodeMap(san);
				OJUtil.addPersistentProperty(c, "callingNodeInstanceUniqueId", new OJPathName("String"), true);
				implementContainer(isProcess, stateClass, san, childMsg);
				if(isProcess){
					propagateExceptions(map, c);
					OJOperation completed = c.getUniqueOperation("completed");
					completed.getBody().addToStatements("getNodeContainer()." + map.completeMethodName() + "(getCallingNodeInstanceUniqueId(),this)");
				}
			}
		}
	}
	private void propagateExceptions(StructuredActivityNodeMap map,OJAnnotatedClass ojOperationClass){
		OJAnnotatedOperation propagateException = new OJAnnotatedOperation("propagateException");
		ojOperationClass.addToOperations(propagateException);
		propagateException.addParam("exception", new OJPathName("Object"));
		propagateException.getBody().addToStatements("getNodeContainer()." + map.unhandledExceptionOperName() + "(getCallingNodeInstanceUniqueId(),exception, this)");
	}
	public void implementVariableDelegation(ActivityNodeContainer container,INakedClassifier msg,OJAnnotatedClass c){
		// NB!!! remember this is only for OCL, not for actions. We only implement getters
		for(INakedActivityVariable var:container.getVariables()){
			NakedStructuralFeatureMap varMap = OJUtil.buildStructuralFeatureMap(msg, var);
			OJAnnotatedOperation delegate = new OJAnnotatedOperation(varMap.getter(), varMap.javaTypePath());
			c.addToOperations(delegate);
			delegate.initializeResultVariable("getNodeContainer()." + varMap.getter() + "()");
		}
		if(container instanceof INakedActivity){
			for(INakedParameter var:((INakedActivity) container).getOwnedParameters()){
				NakedStructuralFeatureMap varMap = OJUtil.buildStructuralFeatureMap(msg, var);
				OJAnnotatedOperation delegate = new OJAnnotatedOperation(varMap.getter(), varMap.javaTypePath());
				c.addToOperations(delegate);
				delegate.initializeResultVariable("getNodeContainer()." + varMap.getter() + "()");
			}
		}else if(container.getOwnerElement() instanceof ActivityNodeContainer){
			implementVariableDelegation((ActivityNodeContainer) container.getOwnerElement(), msg, c);
		}
	}
	private void doExecute(INakedClassifier activity,OJAnnotatedClass activityClass,boolean isProcess){
		OJOperation execute = implementExecute(activityClass, activity);
		if(isProcess){
			execute.getBody().addToStatements("this.setProcessInstanceId(processInstance.getId())");
		}else if(activity instanceof INakedActivity){
			activityClass.addToImports(new OJPathName(ExceptionHolder.class.getName()));
			INakedActivity a = (INakedActivity) activity;
			for(INakedParameter p:a.getOwnedParameters()){
				if(p.isException()){
					NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(activity, p);
					execute.getBody().addToStatements(
							new OJIfStatement("this." + map.getter() + "()!=null", "throw new ExceptionHolder(this,\"" + p.getName() + "\"," + map.getter() + "())"));
				}
			}
			execute.getBody().addToStatements(
					new OJIfStatement("this." + Jbpm5ObjectNodeExpressor.EXCEPTION_FIELD + "!=null", "throw new ExceptionHolder(this,\"_raised\","
							+ Jbpm5ObjectNodeExpressor.EXCEPTION_FIELD + ")"));
		}
	}
	private void implementNodeMethods(ActivityNodeContainer activity){
		OJAnnotatedClass activityClass;
		if(activity instanceof INakedStructuredActivityNode){
			activityClass = findJavaClass(((INakedStructuredActivityNode) activity).getMessageStructure());
		}else{
			activityClass = findJavaClass((INakedActivity) activity);
		}
		activityClass.addToImports(Jbpm5Util.getProcessContext());
		for(INakedActivityNode node:activity.getActivityNodes()){
			if(node instanceof INakedAction || node instanceof INakedParameterNode || node instanceof INakedControlNode || node instanceof INakedExpansionRegion
					|| node instanceof INakedExpansionNode){
				this.implementNodeMethod(activityClass, node);
				if(node instanceof ActivityNodeContainer){
					visitEdges(((ActivityNodeContainer) node).getActivityEdges());
				}
				if(node instanceof INakedAction && BehaviorUtil.hasMessageStructure((INakedAction) node)){
					for(INakedOutputPin op:((INakedAction) node).getOutput()){
						if(!(node instanceof INakedAcceptCallAction && ((INakedAcceptCallAction) node).getReturnInfo() == op)){
							implementDerivedGetter(activityClass, op);
						}
					}
				}
				if(node instanceof INakedExpansionRegion){
					INakedExpansionRegion region = (INakedExpansionRegion) node;
					INakedMessageStructure msg = region.getMessageStructure();
					OJAnnotatedClass msgClass = findJavaClass(msg);
					for(INakedExpansionNode ip:region.getOutputElement()){
						NakedStructuralFeatureMap propertyMap = OJUtil.buildStructuralFeatureMap(msg, ip, true);
						NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(msg, ip, false);
						OJAnnotatedOperation getter = new OJAnnotatedOperation(map.getter(), map.javaTypePath());
						msgClass.addToOperations(getter);
						getter.initializeResultVariable("getNodeContainer()." + propertyMap.getter() + "()");
					}
				}
			}
		}
		visitEdges(activity.getActivityEdges());
	}
	private void implementDerivedGetter(OJAnnotatedClass activityClass,INakedObjectNode node2){
		NakedStructuralFeatureMap actionMap = OJUtil.buildStructuralFeatureMap((INakedAction) node2.getOwnerElement(), getLibrary());
		NakedStructuralFeatureMap pinMap = OJUtil.buildStructuralFeatureMap(node2.getNearestStructuredElementAsClassifier(), node2, true);
		NakedStructuralFeatureMap propertyMap = OJUtil.buildStructuralFeatureMap(node2.getNearestStructuredElementAsClassifier(), node2, false);
		List<OJPathName> emptyList = Collections.emptyList();
		OJAnnotatedOperation oper = (OJAnnotatedOperation) activityClass.findOperation(pinMap.getter(), emptyList);
		oper.setBody(new OJBlock());
		if(actionMap.isMany()){
			if(pinMap.isMany()){
				oper.initializeResultVariable(pinMap.javaDefaultValue());
				OJForStatement forEach = new OJForStatement("tmp", actionMap.javaBaseTypePath(), actionMap.getter() + "()");
				oper.getBody().addToStatements(forEach);
				if(propertyMap.isMany()){
					forEach.getBody().addToStatements("result.addAll(tmp." + propertyMap.getter() + "())");
				}else{
					forEach.getBody().addToStatements("result.add(tmp." + propertyMap.getter() + "())");
				}
			}else{
				if(propertyMap.isMany()){
					oper.initializeResultVariable(actionMap.getter() + "().iterator().next()." + propertyMap.getter() + "().iterator().next()");
				}else{
					oper.initializeResultVariable(actionMap.getter() + "().iterator().next()." + propertyMap.getter() + "()");
				}
			}
		}else{
			if(pinMap.isMany()){
				oper.initializeResultVariable(pinMap.javaDefaultValue());
				if(propertyMap.isMany()){
					oper.getBody().addToStatements("result.addAll(" + actionMap.getter() + "()." + propertyMap.getter() + "())");
				}else{
					oper.getBody().addToStatements("result.add(" + actionMap.getter() + "()." + propertyMap.getter() + "())");
				}
			}else{
				if(propertyMap.isMany()){
					oper.initializeResultVariable(actionMap.getter() + "()." + propertyMap.getter() + "().iterator().next()");
				}else{
					oper.initializeResultVariable(actionMap.getter() + "()." + propertyMap.getter() + "()");
				}
			}
		}
	}
	protected void visitEdges(Collection<INakedActivityEdge> activityEdges){
		for(INakedActivityEdge edge:activityEdges){
			activityEdge(edge);
		}
	}
	private void implementNodeMethod(OJClass activityClass,INakedActivityNode node){
		Jbpm5ActionBuilder<?> implementor = null;
		if(node instanceof INakedExpansionRegion){
			implementor = new ExpansionRegionBuilder(getLibrary(), (INakedExpansionRegion) node);
		}else if(node instanceof INakedEmbeddedSingleScreenTask){
			implementor = new EmbeddedSingleScreenTaskBuilder(getLibrary(), (INakedEmbeddedSingleScreenTask) node);
		}else if(node instanceof INakedEmbeddedScreenFlowTask){
			implementor = new EmbeddedScreenFlowTaskBuilder(getLibrary(), (INakedEmbeddedScreenFlowTask) node);
		}else if(node instanceof INakedCallBehaviorAction){
			implementor = new CallBehaviorActionBuilder(getLibrary(), (INakedCallBehaviorAction) node);
		}else if(node instanceof INakedCallOperationAction){
			implementor = new CallOperationActionBuilder(getLibrary(), (INakedCallOperationAction) node);
		}else if(node instanceof INakedAcceptEventAction){
			implementor = new AcceptEventActionBuilder(getLibrary(), (INakedAcceptEventAction) node);
		}else if(node instanceof INakedParameterNode){
			INakedParameterNode parameterNode = (INakedParameterNode) node;
			implementor = new ParameterNodeBuilder(getLibrary(), parameterNode);
		}else if(node instanceof INakedReplyAction){
			INakedReplyAction action = (INakedReplyAction) node;
			implementor = new ReplyActionBuilder(getLibrary(), action);
		}else{
			implementor = new SimpleActionBridge(getLibrary(), node, SimpleActivityMethodImplementor.resolveBuilder(node, getLibrary(), new Jbpm5ObjectNodeExpressor(
					getLibrary())));
		}
		if(implementor.hasNodeMethod()){
			OJAnnotatedOperation operation = new OJAnnotatedOperation(implementor.getMap().doActionMethod());
			OJUtil.addMetaInfo(operation, node);
			activityClass.addToOperations(operation);
			operation.addParam("context", Jbpm5Util.getProcessContext());
			if(implementor.isEffectiveFinalNode()){
				implementor.implementFinalStep(operation.getBody());
			}
			
			implementor.setupArgumentPins(operation);
			implementor.implementPreConditions(operation);
			implementor.implementObersvations(operation);
			implementor.implementActionOn(operation);
			if(implementor.isLongRunning()){
				implementor.implementCallbackMethods(activityClass);
			}else if(!(implementor.waitsForEvent() || node instanceof INakedControlNode)){
				implementor.implementPostConditions(operation);
				// implementor.implementConditionalFlows(operation,
				// operation.getBody(), true);
			}
		}
	}
	@Override
	protected Collection<? extends INakedElement> getTopLevelFlows(INakedClassifier umlBehavior){
		return Arrays.asList(umlBehavior);
	}
}