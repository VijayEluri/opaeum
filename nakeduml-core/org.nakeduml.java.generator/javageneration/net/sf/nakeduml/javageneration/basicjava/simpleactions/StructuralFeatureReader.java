package net.sf.nakeduml.javageneration.basicjava.simpleactions;

import net.sf.nakeduml.javageneration.basicjava.AbstractObjectNodeExpressor;
import net.sf.nakeduml.javageneration.maps.ActionMap;
import net.sf.nakeduml.javageneration.maps.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.metamodel.actions.INakedReadStructuralFeatureAction;
import net.sf.nakeduml.metamodel.activities.INakedOutputPin;
import net.sf.nakeduml.metamodel.workspace.NakedUmlLibrary;

import org.nakeduml.java.metamodel.OJBlock;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;

public class StructuralFeatureReader extends SimpleNodeBuilder<INakedReadStructuralFeatureAction> {
	public StructuralFeatureReader(NakedUmlLibrary oclEngine, INakedReadStructuralFeatureAction action, AbstractObjectNodeExpressor expressor) {
		super(oclEngine, action, expressor);
	}

	@Override
	public void implementActionOn(OJAnnotatedOperation operation,OJBlock block){
		INakedOutputPin result = node.getResult();
		NakedStructuralFeatureMap resultMap = OJUtil.buildStructuralFeatureMap(result.getActivity(), result);
		expressor.buildResultVariable(operation, block, resultMap);
		ActionMap actionMap = new ActionMap(node);
		OJBlock fs = buildLoopThroughTarget(operation, block, actionMap);
		String call = actionMap.targetName() + "." + new NakedStructuralFeatureMap(node.getFeature()).getter() + "()";
		NakedStructuralFeatureMap featureMap = OJUtil.buildStructuralFeatureMap(node.getFeature());
		call=expressor.storeResults(resultMap, call, featureMap.isMany());
		fs.addToStatements(call);
	}
}