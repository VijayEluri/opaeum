package org.opaeum.javageneration.basicjava.simpleactions;

import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.javageneration.basicjava.AbstractObjectNodeExpressor;
import org.opaeum.javageneration.jbpm5.EventUtil;
import org.opaeum.javageneration.maps.NakedStructuralFeatureMap;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.metamodel.activities.ActivityKind;
import org.opaeum.metamodel.activities.INakedExpansionNode;
import org.opaeum.metamodel.activities.INakedObjectNode;
import org.opaeum.metamodel.workspace.OpaeumLibrary;

public class ExpansionNodeImplementor extends SimpleNodeBuilder<INakedExpansionNode>{
	public ExpansionNodeImplementor(OpaeumLibrary oclEngine,INakedExpansionNode action,AbstractObjectNodeExpressor objectNodeExpressor){
		super(oclEngine, action, objectNodeExpressor);
	}
	
	@Override
	public void implementActionOn(OJAnnotatedOperation operation,OJBlock block){
		if(node.isOutputElement()){
			NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(node.getActivity(), node,true);
			// expressor.maybeBuildResultVariable(operation, block, map);
			if(node.getFeedingNode() instanceof INakedObjectNode){
				String call = expressor.expressInputPinOrOutParamOrExpansionNode(block, node);
				String pref="";
				if(node.getActivity().getActivityKind()!=ActivityKind.SIMPLE_SYNCHRONOUS_METHOD){
					pref="getNodeContainer().";
				}
				block.addToStatements(pref+expressor.storeResults(map, call, node.getFeedingNode().getNakedMultiplicity().isMany()));
			}
			EventUtil.cancelEvents(block, this.node.getExpansionRegion().getActivityNodes());
		}else{
			EventUtil.requestEvents(operation, this.node.getExpansionRegion().getActivityNodes(), getLibrary().getBusinessRole() != null);
		}
	}
}