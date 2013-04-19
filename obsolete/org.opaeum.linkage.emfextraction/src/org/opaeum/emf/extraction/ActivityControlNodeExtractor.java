package org.opaeum.emf.extraction;

import org.eclipse.uml2.uml.ActivityFinalNode;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ControlNodeType;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.FlowFinalNode;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.INakedActivityNode;
import org.eclipse.uml2.uml.INakedActivityPartition;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.JoinNode;
import org.eclipse.uml2.uml.MergeNode;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.metamodel.activities.internal.NakedControlNodeImpl;

@StepDependency(phase = EmfExtractionPhase.class,requires = {FeatureExtractor.class,ActivityStructureExtractor.class},after = {
		FeatureExtractor.class,ActivityStructureExtractor.class})
public class ActivityControlNodeExtractor extends CommonBehaviorExtractor{
	@VisitBefore
	public void visitInitialNode(InitialNode emfNode,NakedControlNodeImpl nakedNode){
		nakedNode.setControlNodeType(ControlNodeType.INITIAL_NODE);
		assignPartition(nakedNode, emfNode);
	}
	@VisitBefore
	public void visitActivityFinalNode(ActivityFinalNode emfNode,NakedControlNodeImpl nakedNode){
		nakedNode.setControlNodeType(ControlNodeType.ACTIVITY_FINAL_NODE);
		assignPartition(nakedNode, emfNode);
	}
	@VisitBefore
	public void visitFlowFinalNode(FlowFinalNode emfNode,NakedControlNodeImpl nakedNode){
		nakedNode.setControlNodeType(ControlNodeType.FLOW_FINAL_NODE);
		assignPartition(nakedNode, emfNode);
	}
	@VisitBefore
	public void visitJoinNode(JoinNode emfNode,NakedControlNodeImpl nakedNode){
		nakedNode.setControlNodeType(ControlNodeType.JOIN_NODE);
		assignPartition(nakedNode, emfNode);
	}
	@VisitBefore
	public void visitForkNode(ForkNode emfNode,NakedControlNodeImpl nakedNode){
		nakedNode.setControlNodeType(ControlNodeType.FORK_NODE);
		assignPartition(nakedNode, emfNode);
	}
	@VisitBefore
	public void visitDecisionNode(DecisionNode emfNode,NakedControlNodeImpl nakedNode){
		nakedNode.setControlNodeType(ControlNodeType.DECISION_NODE);
		assignPartition(nakedNode, emfNode);
	}
	@VisitBefore
	public void visitMergeNode(MergeNode emfNode,NakedControlNodeImpl nakedNode){
		nakedNode.setControlNodeType(ControlNodeType.MERGE_NODE);
		assignPartition(nakedNode, emfNode);
	}
	private void assignPartition(INakedActivityNode node,ActivityNode emfNode){
		if(emfNode.getInPartitions().size() == 1){
			node.setInPartition(((INakedActivityPartition) getNakedPeer((emfNode.getInPartitions().get(0)))));
		}
	}
}
