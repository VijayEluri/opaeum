package net.sf.nakeduml.linkage;

import java.util.List;

import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.metamodel.actions.INakedAcceptEventAction;
import net.sf.nakeduml.metamodel.actions.INakedCallAction;
import net.sf.nakeduml.metamodel.actions.INakedCallOperationAction;
import net.sf.nakeduml.metamodel.actions.INakedOpaqueAction;
import net.sf.nakeduml.metamodel.actions.INakedSendObjectAction;
import net.sf.nakeduml.metamodel.activities.ActivityKind;
import net.sf.nakeduml.metamodel.activities.ControlNodeType;
import net.sf.nakeduml.metamodel.activities.INakedAction;
import net.sf.nakeduml.metamodel.activities.INakedActivity;
import net.sf.nakeduml.metamodel.activities.INakedActivityNode;
import net.sf.nakeduml.metamodel.activities.INakedControlNode;
import net.sf.nakeduml.metamodel.activities.INakedObjectNode;
import net.sf.nakeduml.metamodel.commonbehaviors.INakedBehavior;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedElementOwner;
import net.sf.nakeduml.metamodel.core.INakedEntity;
import net.sf.nakeduml.metamodel.core.INakedInterface;
import net.sf.nakeduml.metamodel.core.INakedOperation;
import net.sf.nakeduml.metamodel.core.IParameterOwner;
import net.sf.nakeduml.metamodel.statemachines.INakedStateMachine;

public class BehaviorUtil {
	public static boolean isUserTask(INakedAction a) {
		if (a instanceof INakedSendObjectAction || a instanceof INakedOpaqueAction) {
			// TODO check if target type represents users?
			return true;
		}
		if (a instanceof INakedCallOperationAction) {
			INakedCallOperationAction ca = (INakedCallOperationAction) a;
			return isUserResponsibility(ca.getOperation());
		}
		return false;
	}

	public static boolean isUserResponsibility(INakedOperation oper) {
		if (oper.getOwner() instanceof INakedInterface) {
			return ((INakedInterface) oper.getOwner()).representsUser();
		} else if (oper.getOwner() instanceof INakedEntity) {
			return ((INakedEntity) oper.getOwner()).representsUser() && oper.isUserResponsibility();
		} else {
			return false;
		}
	}

	public static boolean requiresExternalInput(INakedActivity a) {
		return requiresExternalInput(a, a);
	}

	private static boolean requiresExternalInput(INakedActivity a, INakedActivity origin) {
		List<INakedActivityNode> nodes = a.getActivityNodesRecursively();
		for (INakedActivityNode node : nodes) {
			if (requiresExternalInput(node)) {
				return true;
			} else if (node instanceof INakedCallAction) {
				INakedCallAction ca = (INakedCallAction) node;
				if (ca.getCalledElement() instanceof INakedStateMachine) {
					return true;
				} else if (caledActivityRequiresExternalInput(ca, origin)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean requiresExternalInput(INakedActivityNode node) {
		return node instanceof INakedAcceptEventAction || (node instanceof INakedAction && isUserTask((INakedAction) node));
	}

	private static boolean caledActivityRequiresExternalInput(INakedCallAction ca, INakedActivity origin) {
		if (ca.getCalledElement() instanceof INakedActivity) {
			return requiresExternalInput((INakedActivity) ca.getCalledElement(), origin);
		} else if (ca.getCalledElement() instanceof INakedOperation) {
			INakedOperation o = (INakedOperation) ca.getCalledElement();
			for (INakedBehavior method : o.getMethods()) {
				// polymorphic - counts as a process call if ANY of the possible
				// implementations are processes
				if (method instanceof INakedActivity) {
					// Break recursion here
					if (!origin.equals(method) && requiresExternalInput((INakedActivity) method, origin)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean hasParallelFlows(INakedActivity a) {
		return hasParallelFlows(a, a);
	}

	private static boolean hasParallelFlows(INakedActivity a, INakedActivity origin) {
		List<INakedActivityNode> nodes = a.getActivityNodesRecursively();
		for (INakedActivityNode node : nodes) {
			if (node instanceof INakedControlNode && isForkOrJoin((INakedControlNode) node)) {
				// TODO implicit joins or forks on Actions
				return true;
			} else if (node instanceof INakedCallAction) {
				INakedCallAction ca = (INakedCallAction) node;
				if (ca.getCalledElement() instanceof INakedStateMachine) {
					return true;
				} else if (caledActivityHasParallelFlows(ca, origin)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isForkOrJoin(INakedControlNode node) {
		return node.getControlNodeType() == ControlNodeType.JOIN_NODE || node.getControlNodeType() == ControlNodeType.FORK_NODE;
	}

	private static boolean caledActivityHasParallelFlows(INakedCallAction ca, INakedActivity origin) {
		if (ca.getCalledElement() instanceof INakedActivity) {
			return hasParallelFlows((INakedActivity) ca.getCalledElement(), origin);
		} else if (ca.getCalledElement() instanceof INakedOperation) {
			INakedOperation o = (INakedOperation) ca.getCalledElement();
			for (INakedBehavior method : o.getMethods()) {
				// polymorphic - counts as a process call if ANY of the possible
				// implementations are processes
				if (method instanceof INakedActivity) {
					// Break recursion here
					if (!origin.equals(method) && hasParallelFlows((INakedActivity) method, origin)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Precondition: {@link ProcessIdentifier} has been applied
	 * 
	 * @param owner
	 * @return
	 */
	public static boolean returnsImmediately(INakedCallAction nakedCall) {
		return !(isUserTask(nakedCall) || nakedCall.getCalledElement().isProcess());
	}

	/**
	 * Precondition: {@link ProcessIdentifier} has been applied
	 * 
	 * @param owner
	 * @return
	 */
	public static boolean hasExecutionInstance(IParameterOwner owner) {
		return owner.isProcess()
				|| owner.hasMultipleConcurrentResults()
				|| (owner instanceof INakedActivity && ((INakedActivity) owner).getActivityKind() == ActivityKind.COMPLEX_SYNCHRONOUS_METHOD)
				|| (owner instanceof INakedOperation && isUserResponsibility((INakedOperation) owner));
	}

	public static boolean isTaskOrProcess(INakedCallAction ca) {
		return isUserTask(ca) || ca.isProcessCall();
	}

	public static boolean mustBeStored(INakedObjectNode node) {
		if (hasExecutionInstance(node.getActivity())) {
			if (node.getOwnerElement() instanceof INakedCallAction) {
				INakedCallAction callAction = (INakedCallAction) node.getOwnerElement();
				if (callAction instanceof INakedOpaqueAction
						|| (callAction.getCalledElement() != null && BehaviorUtil.hasExecutionInstance(callAction.getCalledElement()))) {
					// Results stored on the entity representing the message,
					// don't implement this outputpin
					return false;
				} else {
					return true;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static INakedClassifier getNearestActualClass(INakedElementOwner ownerElement) {
		// Returns the first ownerElement that has an OJClass
		while (ownerElement instanceof INakedElement
				&& !(ownerElement instanceof INakedClassifier && OJUtil.hasOJClass((INakedClassifier) ownerElement))) {
			ownerElement = ((INakedElement) ownerElement).getOwnerElement();
		}
		if (ownerElement instanceof INakedClassifier) {
			return (INakedClassifier) ownerElement;
		} else {
			return null;
		}
	}
}
