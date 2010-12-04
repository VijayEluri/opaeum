package net.sf.nakeduml.javageneration.jbpm5.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.basicjava.simpleactions.ActionMap;
import net.sf.nakeduml.javageneration.jbpm5.BpmUtil;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.javametamodel.OJBlock;
import net.sf.nakeduml.javametamodel.OJClass;
import net.sf.nakeduml.javametamodel.OJClassifier;
import net.sf.nakeduml.javametamodel.OJIfStatement;
import net.sf.nakeduml.javametamodel.OJOperation;
import net.sf.nakeduml.javametamodel.OJPathName;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedField;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedOperation;
import net.sf.nakeduml.linkage.BehaviorUtil;
import net.sf.nakeduml.metamodel.actions.INakedCallAction;
import net.sf.nakeduml.metamodel.actions.INakedInvocationAction;
import net.sf.nakeduml.metamodel.activities.INakedInputPin;
import net.sf.nakeduml.metamodel.activities.INakedObjectNode;
import net.sf.nakeduml.metamodel.activities.INakedPin;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedOperation;
import net.sf.nakeduml.metamodel.core.INakedTypedElement;
import net.sf.nakeduml.metamodel.name.NameWrapper;
import nl.klasse.octopus.oclengine.IOclEngine;

/**
 * Base class for all action builders that could potentially build a task
 * representing a usertask.
 * 
 * @param <A>
 */
public abstract class PotentialTaskActionBuilder<A extends INakedInvocationAction> extends Jbpm5ActionBuilder<A> {
	protected NakedStructuralFeatureMap callMap;

	protected PotentialTaskActionBuilder(IOclEngine oclEngine, A node) {
		super(oclEngine, node);
		if (node instanceof INakedCallAction && BehaviorUtil.hasMessageStructure((INakedCallAction) node)) {
			callMap = OJUtil.buildStructuralFeatureMap((INakedCallAction) node, getOclEngine().getOclLibrary());
		}
	}

	@Override
	public void implementSupportingTaskMethods(OJClass activityClass) {
		implementJbpmAssignmentsIfNecessary(activityClass);
		implementCompleteMethod(activityClass);
	}


	private void implementCompleteMethod(OJClass activityClass) {
		activityClass.addToImports(BpmUtil.getNodeInstance());
		activityClass.addToImports(BpmUtil.getJbpmKnowledgeSession());
		OJOperation complete = new OJAnnotatedOperation();
		complete.setName("on" + node.getMappingInfo().getJavaName().getCapped() + "Completed");
		activityClass.addToOperations(complete);
		implementPostConditions(complete);
		String literalExpression = activityClass.getName() + "State." + BpmUtil.stepLiteralName(node);
		complete.getBody().addToStatements("NodeInstance waitingToken=findWaitingNode(" + literalExpression + ".getQualifiedName())");
		complete.getBody().addToStatements(
				"List<TaskInstance> tasks=(List<TaskInstance>)getProcessInstance().getTaskMgmtInstance().getUnfinishedTasks(waitingToken)");
		OJIfStatement ifFound = new OJIfStatement();
		ifFound.setCondition("tasks.size()==1");
		OJBlock thenPart = ifFound.getThenPart();
		thenPart.addToStatements("tasks.get(0).end()");
		implementConditionalFlows(complete, thenPart, false);
		complete.getBody().addToStatements(ifFound);
	}

	@Override
	public boolean isTask() {
		return node.isTask();
	}

	/**
	 * Implements assignment methods. These methods return an array of strings
	 * holding the userNames of the set of users that could possbly complete the
	 * task in question or
	 */
	private void implementJbpmAssignmentsIfNecessary(OJClassifier c) {
		// for targets as well as swimlanes
		if (node.isTask() && node.getTargetElement() != null) {
			INakedTypedElement targetElement = node.getTargetElement();
			NameWrapper cappedJavaName = node.getInPartition() == null ? node.getMappingInfo().getJavaName() : node.getInPartition()
					.getMappingInfo().getJavaName();
			cappedJavaName = cappedJavaName.getCapped();
			ActionMap actionMap = new ActionMap(node);
			if (targetElement.getNakedMultiplicity().getUpper() > 1) {
				String getAcorIds = "getActorIdsFor" + cappedJavaName;
				if (c.findOperation(getAcorIds, Collections.EMPTY_LIST) == null) {
					// do not duplicate these methods for swimlane references.
					// One per
					// swimlane per process
					OJOperation actorIds = new OJAnnotatedOperation();
					actorIds.setName(getAcorIds);
					c.addToOperations(actorIds);
					actorIds.setReturnType(new OJPathName("String[]"));
					c.addToImports("java.util.ArrayList");
					OJAnnotatedField results = new OJAnnotatedField();
					results.setName("results");
					results.setType(new OJPathName("Collection<String>"));
					results.setInitExp("new ArrayList<String>()");
					actorIds.getBody().addToLocals(results);
					OJBlock forEach = buildLoopThroughTarget(actorIds, actorIds.getBody(), actionMap);
					forEach.addToStatements("results.add(" + actionMap.targetName() + ".getUserName())");
					actorIds.getBody().addToStatements("return results.toArray(new String[results.size()])");
				}
			} else {
				String getActorId = "getActorIdFor" + cappedJavaName;
				if (c.findOperation(getActorId, Collections.EMPTY_LIST) == null) {
					// do not duplicate these methods for swimlane references.
					// One per
					// swimlane per process
					OJOperation actorId = new OJAnnotatedOperation();
					actorId.setName(getActorId);
					c.addToOperations(actorId);
					actorId.setReturnType(new OJPathName("String"));
					// Build if statement if necessary
					OJBlock forEach = buildLoopThroughTarget(actorId, actorId.getBody(), actionMap);
					forEach.addToStatements("return " + actionMap.targetName() + ".getUserName()");
					if (targetElement.getNakedMultiplicity().getLower() == 0) {
						actorId.getBody().addToStatements("return null");
					}
				}
			}
		}
	}
}