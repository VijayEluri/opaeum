package org.opeum.javageneration.jbpm5.actions;

import java.util.Collection;
import java.util.HashSet;

import org.opeum.javageneration.basicjava.AbstractNodeBuilder;
import org.opeum.javageneration.basicjava.AbstractObjectNodeExpressor;
import org.opeum.javageneration.basicjava.simpleactions.ActivityNodeMap;
import org.opeum.javageneration.jbpm5.Jbpm5Util;
import org.opeum.javageneration.jbpm5.activity.ActivityUtil;
import org.opeum.javageneration.maps.ActionMap;
import org.opeum.javageneration.maps.NakedOperationMap;
import org.opeum.javageneration.maps.NakedStructuralFeatureMap;
import org.opeum.javageneration.oclexpressions.ConstraintGenerator;
import org.opeum.javageneration.util.OJUtil;
import org.opeum.linkage.BehaviorUtil;
import org.opeum.metamodel.actions.INakedCallAction;
import org.opeum.metamodel.actions.INakedReplyAction;
import org.opeum.metamodel.activities.ControlNodeType;
import org.opeum.metamodel.activities.INakedAction;
import org.opeum.metamodel.activities.INakedActivityEdge;
import org.opeum.metamodel.activities.INakedActivityNode;
import org.opeum.metamodel.activities.INakedControlNode;
import org.opeum.metamodel.activities.INakedExpansionRegion;
import org.opeum.metamodel.activities.INakedObjectNode;
import org.opeum.metamodel.activities.INakedOutputPin;
import org.opeum.metamodel.activities.INakedPin;
import org.opeum.metamodel.commonbehaviors.GuardedFlow;
import org.opeum.metamodel.core.INakedConstraint;
import org.opeum.metamodel.core.PreAndPostConstrained;
import org.opeum.metamodel.workspace.OpeumLibrary;

import org.opeum.java.metamodel.OJBlock;
import org.opeum.java.metamodel.OJClass;
import org.opeum.java.metamodel.OJIfStatement;
import org.opeum.java.metamodel.OJOperation;
import org.opeum.java.metamodel.annotation.OJAnnotatedField;
import org.opeum.java.metamodel.annotation.OJAnnotatedOperation;

public abstract class Jbpm5ActionBuilder<A extends INakedActivityNode> extends AbstractNodeBuilder{
	protected A node;
	protected AbstractObjectNodeExpressor expressor;
	public ActivityNodeMap getMap(){
		return map;
	}
	protected ActivityNodeMap map;
	protected Jbpm5ActionBuilder(final OpeumLibrary l,A node){
		super(l, new Jbpm5ObjectNodeExpressor(l));
		this.node = node;
		if(node instanceof INakedAction){
			this.map = new ActionMap((INakedAction) node);
		}else{
			this.map = new ActivityNodeMap(node);
		}
		this.expressor = (AbstractObjectNodeExpressor) super.expressor;
	}
	public void setupVariablesAndArgumentPins(OJAnnotatedOperation oper){
		ActivityUtil.setupVariables(oper, node);
		if(node instanceof INakedAction){
			for(INakedPin pin:((INakedAction) node).getInput()){
				boolean ignore = node instanceof INakedReplyAction && pin.equals(((INakedReplyAction) node).getReturnInfo());
				if(!ignore){
					OJBlock block = oper.getBody();
					NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(pin.getActivity(), pin, true);
					oper.getOwner().addToImports(map.javaTypePath());
					OJAnnotatedField field = new OJAnnotatedField(map.umlName(), map.javaTypePath());
					field.setInitExp(expressPin(oper, block, pin));
					block.addToLocals(field);
				}
			}
		}
	}
	public void implementFinalStep(OJBlock block){
		if(node.getActivity().isLongRunning() && node instanceof INakedControlNode
				&& ((INakedControlNode) node).getControlNodeType() == ControlNodeType.ACTIVITY_FINAL_NODE){
			block.addToStatements(new OJIfStatement("getProcessInstance().getNodeInstances().size()==1", "completed()"));
		}
		block.addToStatements(Jbpm5Util.endNodeFieldNameFor(node.getActivity()) + "=" + node.getActivity().getMappingInfo().getJavaName() + "State."
				+ Jbpm5Util.stepLiteralName(node));
	}
	public abstract void implementActionOn(OJAnnotatedOperation oper);
	public void implementPreConditions(OJOperation oper){
		if(node instanceof PreAndPostConstrained){
			implementConditions(oper, oper.getBody(), (PreAndPostConstrained) node, true);
		}
	}
	public void implementConditions(OJOperation oper,OJBlock block,PreAndPostConstrained constrained,boolean pre){
		Collection<INakedConstraint> conditions = pre ? constrained.getPreConditions() : constrained.getPostConditions();
		if(conditions.size() > 0){
			if(node instanceof INakedAction){
				if(!pre && ((INakedAction) node).isLongRunning()){
					// Most commonly used for Tasks where there would be a
					// message structure T
					// TODO support other output pins
					final INakedAction action = (INakedAction) node;
					Collection<INakedPin> pins = new HashSet<INakedPin>(action.getOutput());
					pins.addAll(action.getInput());
					for(INakedPin pin:pins){
						NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(pin.getActivity(), pin, false);
						oper.getOwner().addToImports(map.javaTypePath());
						OJAnnotatedField field = new OJAnnotatedField(map.umlName(), map.javaTypePath());
						field.setInitExp("completedWorkObject." + map.getter() + "()");
						block.addToLocals(field);
					}
				}
			}
			ConstraintGenerator cg = new ConstraintGenerator((OJClass) oper.getOwner(), constrained);
			block.addToStatements(cg.buildConstraintsBlock(oper, block, conditions, pre));
		}
	}
	public void implementPostConditions(OJOperation oper){
		if(node instanceof PreAndPostConstrained){
			implementConditions(oper, oper.getBody(), (PreAndPostConstrained) node, false);
		}
	}
	public boolean isLongRunning(){
		return false;
	}
	public void implementCallbackMethods(OJClass activityClass){
	}
	public void flowTo(OJBlock block,INakedActivityNode target){
		if(target.isImplicitJoin()){
			block.addToStatements("waitingNode.flowToNode(\"" + Jbpm5Util.getArtificialJoinName(target) + "\")");
		}else{
			block.addToStatements("waitingNode.flowToNode(\"" + target.getMappingInfo().getPersistentName() + "\")");
		}
	}
	public void implementConditionalFlows(OJOperation operationContext,OJBlock block){
		// TODO implement cases where there are conditions and forks
		block.addToStatements("this.processDirty=true");
		if(node.isImplicitFork()){
			block.addToStatements("waitingNode.flowToNode(\"" + Jbpm5Util.getArtificialForkName(node) + "\")");
		}else if(node.isImplicitDecision()){
			block.addToStatements("waitingNode.flowToNode(\"" + Jbpm5Util.getArtificialChoiceName(node) + "\")");
		}else if(node.getAllEffectiveOutgoing().size() > 0){
			GuardedFlow flow = node.getAllEffectiveOutgoing().iterator().next();
			flowTo(block, ((INakedActivityEdge) flow).getEffectiveTarget());
		}
	}
	public boolean waitsForEvent(){
		return false;
	}
	protected final void buildPinField(OJOperation operationContext,OJBlock block,INakedObjectNode pin){
		NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(pin.getActivity(), pin, true);
		operationContext.getOwner().addToImports(map.javaTypePath());
		OJAnnotatedField field = new OJAnnotatedField(pin.getMappingInfo().getJavaName().getAsIs(), map.javaTypePath());
		field.setInitExp(expressPin(operationContext, block, pin));
		block.addToLocals(field);
	}
	public boolean hasNodeMethod(){
		// TODO refine this
		return node instanceof INakedAction || node instanceof INakedObjectNode || node instanceof INakedExpansionRegion
				|| (node instanceof INakedControlNode && ((INakedControlNode) node).getControlNodeType().isFinalNode());
	}
	public boolean isEffectiveFinalNode(){
		return BehaviorUtil.isEffectiveFinalNode(node);
	}
}
