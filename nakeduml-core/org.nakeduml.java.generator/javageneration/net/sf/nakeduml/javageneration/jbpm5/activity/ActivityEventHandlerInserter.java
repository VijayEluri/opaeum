package net.sf.nakeduml.javageneration.jbpm5.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.jbpm5.AbstractEventHandlerInserter;
import net.sf.nakeduml.javageneration.jbpm5.FromNode;
import net.sf.nakeduml.javageneration.jbpm5.WaitForEventElements;
import net.sf.nakeduml.javageneration.jbpm5.actions.Jbpm5ActionBuilder;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.metamodel.actions.INakedAcceptEventAction;
import net.sf.nakeduml.metamodel.activities.INakedActivity;
import net.sf.nakeduml.metamodel.activities.INakedActivityEdge;
import net.sf.nakeduml.metamodel.activities.INakedActivityNode;
import net.sf.nakeduml.metamodel.activities.INakedOutputPin;
import net.sf.nakeduml.metamodel.commonbehaviors.GuardedFlow;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedTypedElement;

import org.nakeduml.java.metamodel.OJBlock;
import org.nakeduml.java.metamodel.OJIfStatement;
import org.nakeduml.java.metamodel.OJOperation;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedClass;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;

public class ActivityEventHandlerInserter extends AbstractEventHandlerInserter {
	private Jbpm5ActionBuilder<INakedActivityNode> actionBuilder;



	@VisitBefore(matchSubclasses = true)
	public void visitActivity(INakedActivity activity) {
		if (activity.isProcess()) {
			OJAnnotatedClass activityClass = findJavaClass(activity);
			super.implementEventHandling(activityClass, activity, getEventActions(activity));
		}
	}

	/**
	 * Overrides the default guard logic and extends it with weight evaluation
	 * logic
	 */
	@Override
	protected void maybeContinueFlow(OJOperation operationContext, OJBlock block, GuardedFlow flow) {
		getActionBuilder().maybeContinueFlow(operationContext, block, (INakedActivityEdge) flow);
	}

	@Override
	protected void implementEventConsumption(FromNode node, OJIfStatement ifNotNull) {
		INakedAcceptEventAction aea = (INakedAcceptEventAction) node.getWaitingElement();
		for (INakedOutputPin argument : aea.getResult()) {
			NakedStructuralFeatureMap pinMap = OJUtil.buildStructuralFeatureMap(argument.getActivity(), argument);
			INakedTypedElement parm = argument.getLinkedTypedElement();
			if (parm == null) {
				ifNotNull.getThenPart().addToStatements(pinMap.setter() + "(unknown)");
			} else {
				if (pinMap.isOne()) {
					ifNotNull.getThenPart().addToStatements(pinMap.setter() + "(" + parm.getMappingInfo().getJavaName().toString() + ")");
				} else {
					if (parm.getNakedMultiplicity().isMany()) {
						ifNotNull.getThenPart()
						.addToStatements(pinMap.allAdder() + "(" + parm.getMappingInfo().getJavaName().toString() + ")");
					} else {
						ifNotNull.getThenPart()
								.addToStatements(pinMap.adder() + "(" + parm.getMappingInfo().getJavaName().toString() + ")");
					}
				}
			}
		}
	}

	private Collection<WaitForEventElements> getEventActions(INakedActivity activity) {
		Map<INakedElement, WaitForEventElements> results = new HashMap<INakedElement, WaitForEventElements>();
		for (INakedActivityNode node : activity.getActivityNodesRecursively()) {
			if (node instanceof INakedAcceptEventAction) {
				INakedAcceptEventAction action = (INakedAcceptEventAction) node;
				if (action.getTrigger() != null && action.getTrigger().getEvent() != null) {
					WaitForEventElements eventActions = results.get(action.getTrigger().getEvent());
					if (eventActions == null) {
						eventActions = new WaitForEventElements(action.getTrigger().getEvent());
						results.put(action.getTrigger().getEvent(), eventActions);
					}
					for (INakedActivityEdge flow : action.getAllEffectiveOutgoing()) {
						eventActions.addWaitingNode(action, flow, true);
					}
				}
			}
		}
		return results.values();
	}

	private Jbpm5ActionBuilder<INakedActivityNode> getActionBuilder() {
		if(actionBuilder==null){
			actionBuilder=new Jbpm5ActionBuilder<INakedActivityNode>(workspace.getOclEngine(), null) {
				@Override
				public void implementActionOn(OJAnnotatedOperation oper) {
				}
			};
		}
		return actionBuilder;
	}
}