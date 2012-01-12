package org.opaeum.javageneration.jbpm5.statemachine;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.klasse.octopus.model.IClassifier;
import nl.klasse.octopus.stdlib.IOclLibrary;

import org.hibernate.annotations.Type;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.javageneration.NakedStateMap;
import org.opaeum.javageneration.basicjava.OperationAnnotator;
import org.opaeum.javageneration.basicjava.SimpleActivityMethodImplementor;
import org.opaeum.javageneration.jbpm5.AbstractJavaProcessVisitor;
import org.opaeum.javageneration.jbpm5.EventUtil;
import org.opaeum.javageneration.jbpm5.Jbpm5Util;
import org.opaeum.javageneration.jbpm5.actions.Jbpm5ObjectNodeExpressor;
import org.opaeum.javageneration.jbpm5.activity.ActivityProcessImplementor;
import org.opaeum.javageneration.oclexpressions.CodeCleanup;
import org.opaeum.javageneration.oclexpressions.PreAndPostConditionGenerator;
import org.opaeum.javageneration.oclexpressions.ValueSpecificationUtil;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.linkage.CompositionEmulator;
import org.opaeum.linkage.NakedParsedOclStringResolver;
import org.opaeum.linkage.ProcessIdentifier;
import org.opaeum.linkage.StateMachineUtil;
import org.opaeum.metamodel.activities.INakedActivity;
import org.opaeum.metamodel.commonbehaviors.INakedBehavior;
import org.opaeum.metamodel.commonbehaviors.INakedChangeEvent;
import org.opaeum.metamodel.commonbehaviors.INakedOpaqueBehavior;
import org.opaeum.metamodel.commonbehaviors.INakedTimeEvent;
import org.opaeum.metamodel.commonbehaviors.INakedTrigger;
import org.opaeum.metamodel.core.INakedClassifier;
import org.opaeum.metamodel.core.INakedElement;
import org.opaeum.metamodel.statemachines.INakedRegion;
import org.opaeum.metamodel.statemachines.INakedState;
import org.opaeum.metamodel.statemachines.INakedStateMachine;
import org.opaeum.metamodel.statemachines.INakedTransition;
import org.opaeum.runtime.domain.IProcessObject;
import org.opaeum.runtime.domain.IProcessStep;

@StepDependency(phase = JavaTransformationPhase.class,requires = {
		OperationAnnotator.class,ProcessIdentifier.class,CompositionEmulator.class,NakedParsedOclStringResolver.class,CodeCleanup.class
},after = {
		OperationAnnotator.class,ActivityProcessImplementor.class
/* Needs repeatable sequence in the ocl generating steps */
},before = CodeCleanup.class)
public class StateMachineImplementor extends AbstractJavaProcessVisitor{
	@VisitBefore(matchSubclasses = true)
	public void visitStateMachine(INakedStateMachine umlStateMachine){
		OJAnnotatedClass ojStateMachine = findJavaClass(umlStateMachine);
		addImports(ojStateMachine);
		OJUtil.addTransientProperty(ojStateMachine, Jbpm5ObjectNodeExpressor.EXCEPTION_FIELD, new OJPathName("Object"), true).setVisibility(OJVisibilityKind.PROTECTED);
		addParameterDelegation(ojStateMachine, umlStateMachine);
		implementProcessInterfaceOperations(ojStateMachine, new OJPathName(umlStateMachine.getMappingInfo().getQualifiedJavaName() + "State"), umlStateMachine);
		OJOperation execute = implementExecute(ojStateMachine, umlStateMachine);
		execute.getBody().addToStatements("this.setProcessInstanceId(processInstance.getId())");
		visitRegions(ojStateMachine, umlStateMachine.getRegions());
	}
	private void state(INakedState state){
		NakedStateMap map = new NakedStateMap(state);
		OJAnnotatedClass ojStateMachine = findJavaClass(state.getStateMachine());
		if(state.getKind().isRestingState()){
			OJOperation getter = new OJAnnotatedOperation("get" + state.getStatePath().toString().replace("::", "_"));
			getter.setReturnType(new OJPathName("boolean"));
			ojStateMachine.addToOperations(getter);
			if(state.getStateMachine().isClassifierBehavior()){
				OJAnnotatedClass context = findJavaClass(state.getStateMachine().getContext());
				OJOperation copy = getter.getCopy();
				copy.getBody().addToStatements("return getClassifierBehavior()!=null && getClassifierBehavior()." + copy.getName() + "()");
				context.addToOperations(copy);
			}
			getter.getBody().addToStatements("return isStepActive(" + ojStateMachine.getName() + "State." + Jbpm5Util.stepLiteralName(state) + ")");
		}
		implementOnEntryIfRequired(ojStateMachine, state, map);
		implementOnExitIfRequired(ojStateMachine, state, map);
		if(state.getKind().isShallowHistory() || state.getKind().isDeepHistory()){
			String fieldName = state.getMappingInfo().getJavaName().getAsIs();
			OJPathName statePat = new OJPathName(ojStateMachine.getPathName().toJavaString() + "State");
			OJAnnotatedField historyField = OJUtil.addPersistentProperty(ojStateMachine, fieldName, statePat, true);
			OJAnnotationValue enumeratd = new OJAnnotationValue(new OJPathName(Type.class.getName()));
			enumeratd.putAttribute("type", ojStateMachine.getName() + "StateResolver");
			historyField.putAnnotation(enumeratd);
		}
	}
	private void implementOnExitIfRequired(OJAnnotatedClass ojStateMachine,INakedState state,NakedStateMap map){
		if(StateMachineUtil.doesWorkOnExit(state)){
			OJAnnotatedOperation onExit = new OJAnnotatedOperation(map.getOnExitMethod());
			ojStateMachine.addToOperations(onExit);
			onExit.addParam("context", Jbpm5Util.getProcessContext());
			if(state.getExit() != null){
				implementBehaviorOn(state, state.getExit(), onExit);
			}
			for(INakedTransition t:state.getOutgoing()){
				Collection<INakedTrigger> triggers = t.getTriggers();
				for(INakedTrigger trigger:triggers){
					if(trigger != null){
						if(trigger.getEvent() instanceof INakedTimeEvent){
							EventUtil.cancelTimer(onExit.getBody(), (INakedTimeEvent) trigger.getEvent(), "this");
						}else if(trigger.getEvent() instanceof INakedChangeEvent){
							EventUtil.cancelChangeEvent(onExit.getBody(), (INakedChangeEvent) trigger.getEvent());
						}
					}
				}
			}
		}
	}
	private void implementOnEntryIfRequired(OJAnnotatedClass ojStateMachine,INakedState state,NakedStateMap map){
		if(StateMachineUtil.doesWorkOnEntry(state)){
			OJAnnotatedOperation onEntry = new OJAnnotatedOperation(map.getOnEntryMethod());
			ojStateMachine.addToOperations(onEntry);
			onEntry.addParam("context", Jbpm5Util.getProcessContext());
			if(state.getEntry() != null){
				implementBehaviorOn(state, state.getEntry(), onEntry);
			}
			INakedState historyPeer = StateMachineUtil.getHistoryPeer(state);
			if(historyPeer != null && state.getKind().isRestingState()){
				String setter = "set" + historyPeer.getMappingInfo().getJavaName().getCapped().getAsIs();
				if(state.getKind().isFinal()){
					onEntry.getBody().addToStatements(setter + "(null)");
				}else{
					onEntry.getBody().addToStatements(setter + "(" + ojStateMachine.getName() + "State." + Jbpm5Util.stepLiteralName(state) + ")");
				}
			}
			if(state.getDoActivity() != null){
				implementBehaviorOn(state, state.getDoActivity(), onEntry);
			}
			OJBlock defaultTransitions = onEntry.getBody();
			if(state.getKind().isShallowHistory()){
				String stateGetter = "get" + state.getMappingInfo().getJavaName().getCapped().getAsIs();
				OJAnnotatedField umlState = new OJAnnotatedField("umlState", Jbpm5Util.UML_NODE_INSTANCE);
				ojStateMachine.addToImports(Jbpm5Util.UML_NODE_INSTANCE);
				umlState.setInitExp("(UmlNodeInstance)context.getNodeInstance()");
				onEntry.getBody().addToLocals(umlState);
				OJIfStatement ifNotNul = new OJIfStatement(stateGetter + "()!=null");
				onEntry.getBody().addToStatements(ifNotNul);
				ifNotNul.getThenPart().addToStatements("umlState.transitionToNode(" + stateGetter + "().getId(),null)");
				defaultTransitions = new OJBlock();
				ifNotNul.setElsePart(defaultTransitions);
			}
			Collection<INakedTransition> completionTransitions = state.getCompletionTransitions();
			if(completionTransitions.size() > 0 && !(state.getKind().isComposite() || state.getKind().isOrthogonal())){
				defaultTransitions.addToStatements(EventUtil.getEventConsumerName(state.getCompletionEvent()) + "()");
			}
			if(state.getKind().isFinal() && state.getContainer().getRegionOwner() instanceof INakedState){
				ojStateMachine.addToImports("org.jbpm.workflow.instance.NodeInstanceContainer");
				onEntry.getBody().addToStatements(
						"((NodeInstanceContainer)context.getNodeInstance().getNodeInstanceContainer()).removeNodeInstance((NodeInstanceImpl)context.getNodeInstance())");
				onEntry.getBody()
						.addToStatements(
								"((NodeInstanceContainer) context.getNodeInstance().getNodeInstanceContainer()).nodeInstanceCompleted((NodeInstanceImpl)context.getNodeInstance(), null)");
			}
		}
	}
	private void implementBehaviorOn(INakedState state,INakedBehavior behavior,OJAnnotatedOperation onEntry){
		if(behavior instanceof INakedActivity){
			SimpleActivityMethodImplementor impl = new SimpleActivityMethodImplementor();
			impl.initialize(javaModel, config, textWorkspace, workspace);
			impl.setWorkspace(workspace);
			impl.implementActivityOn((INakedActivity) behavior, onEntry);
		}else if(behavior instanceof INakedOpaqueBehavior){
			INakedOpaqueBehavior b = (INakedOpaqueBehavior) behavior;
			IClassifier voidType = getOclEngine().getOclLibrary().lookupStandardType(IOclLibrary.OclVoidTypeName);
			onEntry.getBody().addToStatements(ValueSpecificationUtil.expressValue(onEntry, b.getBody(), state.getStateMachine(), voidType));
		}
	};
	private void transition(OJAnnotatedClass ojStateMachine,INakedTransition transition){
		if(transition.hasGuard() && transition.getGuard().isValidOclValue() && transition.getSource().getKind().isChoice()){
			OJOperation getter = new OJAnnotatedOperation(Jbpm5Util.getGuardMethod(transition));
			getter.setReturnType(new OJPathName("boolean"));
			ojStateMachine.addToOperations(getter);
			IClassifier booleanType = getOclEngine().getOclLibrary().lookupStandardType(IOclLibrary.BooleanTypeName);
			String expression = ValueSpecificationUtil.expressValue(getter, transition.getGuard(), transition.getStateMachine(), booleanType);
			getter.getBody().addToStatements("return " + expression);
		}
	};
	private void visitRegions(OJAnnotatedClass ojStateMachine,Collection<INakedRegion> regions){
		for(INakedRegion r:regions){
			for(INakedState s:r.getStates()){
				this.state(s);
				visitRegions(ojStateMachine, s.getRegions());
			}
			for(INakedTransition t:r.getTransitions()){
				this.transition(ojStateMachine, t);
			}
		}
	}
	private void addImports(OJClass javaStateMachine){
		javaStateMachine.addToImports(new OJPathName(Set.class.getName()));
		javaStateMachine.addToImports(new OJPathName(HashSet.class.getName()));
		javaStateMachine.addToImports(new OJPathName(List.class.getName()));
		javaStateMachine.addToImports(new OJPathName(ArrayList.class.getName()));
		javaStateMachine.addToImports(new OJPathName(Timestamp.class.getName()));
		javaStateMachine.addToImports(IProcessObject.class.getName());
		javaStateMachine.addToImports(IProcessStep.class.getName());
	}
	@Override
	protected Collection<? extends INakedElement> getTopLevelFlows(INakedClassifier umlBehavior){
		return ((INakedStateMachine) umlBehavior).getRegions();
	}
}