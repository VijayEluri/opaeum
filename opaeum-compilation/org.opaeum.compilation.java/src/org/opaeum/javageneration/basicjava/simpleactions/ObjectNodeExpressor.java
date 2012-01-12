package org.opaeum.javageneration.basicjava.simpleactions;

import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.javageneration.basicjava.AbstractObjectNodeExpressor;
import org.opaeum.javageneration.maps.NakedStructuralFeatureMap;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.metamodel.actions.INakedVariableAction;
import org.opaeum.metamodel.activities.INakedObjectFlow;
import org.opaeum.metamodel.activities.INakedObjectNode;
import org.opaeum.metamodel.commonbehaviors.INakedBehavior;
import org.opaeum.metamodel.workspace.OpaeumLibrary;

public class ObjectNodeExpressor extends AbstractObjectNodeExpressor{
	public ObjectNodeExpressor(OpaeumLibrary oclLibrary){
		super(oclLibrary);
	}
	public boolean pinsAvailableAsVariables(){
		return false;
	}
	public String expressFeedingNodeForObjectFlowGuard(OJBlock block,INakedObjectFlow flow){
		INakedObjectNode feedingNode = (INakedObjectNode) flow.getOriginatingObjectNode();
		NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(flow.getActivity(), feedingNode, shouldEnsureUniquenes(feedingNode));
		String call = map.fieldname();// ParameterNode or top level output
		return surroundWithSelectionAndTransformation(call, flow);
	}
	public final String expressInputPinOrOutParamOrExpansionNode(OJBlock block,INakedObjectNode pin){
		// Either an outputpin or parameterNode
		INakedObjectFlow edge = (INakedObjectFlow) pin.getIncoming().iterator().next();
		INakedObjectNode feedingNode = pin.getFeedingNode();
		NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(pin.getActivity(), feedingNode, shouldEnsureUniquenes(feedingNode));
		String call = map.fieldname();// ParameterNode or top level output
										// pin or expansion node
		return surroundWithSelectionAndTransformation(call, edge);
	}
	public OJAnnotatedField buildResultVariable(OJAnnotatedOperation operation,OJBlock block,NakedStructuralFeatureMap map){
		OJAnnotatedField field = new OJAnnotatedField(map.fieldname(), map.javaTypePath());
		field.setInitExp(map.javaDefaultValue());
		block.addToLocals(field);
		operation.getOwner().addToImports(map.javaBaseTypePath());
		operation.getOwner().addToImports(map.javaDefaultTypePath());
		return field;
	}
	public String storeResults(NakedStructuralFeatureMap resultMap,String call,boolean isMany){
		if(resultMap.isCollection()){
			if(isMany){
				call = resultMap.fieldname() + ".addAll(" + call + ")";
			}else{
				call = resultMap.fieldname() + ".add(" + call + ")";
			}
		}else{
			call = resultMap.fieldname() + "=" + call;
		}
		return call;
	}
	@Override
	public String clear(NakedStructuralFeatureMap map){
		return map.fieldname() + ".clear()";
	}
	@Override
	protected String surroundWithBehaviorCall(String expression,INakedBehavior b,INakedObjectFlow flow){
		// TODO Auto-generated method stub
		return OJUtil.buildOperationMap(b).javaOperName() + "(" + expression + ")";
	}
	@Override
	public String pathToVariableContext(INakedVariableAction action){
		return "";
	}
}