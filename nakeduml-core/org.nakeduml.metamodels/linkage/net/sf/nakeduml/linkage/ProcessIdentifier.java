package net.sf.nakeduml.linkage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.metamodel.actions.INakedAcceptCallAction;
import net.sf.nakeduml.metamodel.activities.ActivityKind;
import net.sf.nakeduml.metamodel.activities.INakedActivity;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedOperation;
import net.sf.nakeduml.metamodel.statemachines.INakedStateMachine;
import net.sf.nakeduml.metamodel.statemachines.StateMachineKind;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
@StepDependency(phase = LinkagePhase.class, after = {}, requires = {})
public class ProcessIdentifier extends AbstractModelElementLinker{
	private BehaviorUtil behaviorUtil;
	@VisitBefore
	public void visitWorkspace(INakedModelWorkspace workspace){
		Map<INakedOperation,Collection<INakedAcceptCallAction>> callActions = new HashMap<INakedOperation,Collection<INakedAcceptCallAction>>();
		for(INakedElement e:workspace.getAllElements()){
			if(e instanceof INakedAcceptCallAction){
				INakedAcceptCallAction action = (INakedAcceptCallAction) e;
				Collection<INakedAcceptCallAction> actions = callActions.get(action.getOperation());
				if(actions==null){
					actions = new ArrayList<INakedAcceptCallAction>();
					callActions.put(action.getOperation(), actions);
				}
				actions.add(action);
			}
		}
		this.behaviorUtil=new BehaviorUtil(callActions);
	}
	@VisitBefore
	public void visitBehavior(INakedStateMachine sm){
		if(sm.isClassifierBehavior()){
			sm.setStateMachineKind(StateMachineKind.LONG_LIVED);
		}
	}
	@VisitBefore
	public void visitBehavior(INakedActivity a){
		if(behaviorUtil==null){
			behaviorUtil=new BehaviorUtil(new HashMap<INakedOperation,Collection<INakedAcceptCallAction>>());
		}
		if(behaviorUtil.requiresExternalInput(a)){
			a.setActivityKind(ActivityKind.PROCESS);
		}else if(a.hasMultipleConcurrentResults() || BehaviorUtil.hasParallelFlows(a) || BehaviorUtil.getNearestActualClass(a)==null||BehaviorUtil.hasLoopBack(a)){
			a.setActivityKind(ActivityKind.COMPLEX_SYNCHRONOUS_METHOD);
		}else{
			a.setActivityKind(ActivityKind.SIMPLE_SYNCHRONOUS_METHOD);
		}
	}
}
