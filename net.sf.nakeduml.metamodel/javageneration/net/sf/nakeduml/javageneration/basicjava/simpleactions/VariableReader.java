package net.sf.nakeduml.javageneration.basicjava.simpleactions;

import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.basicjava.AbstractObjectNodeExpressor;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.javametamodel.OJBlock;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedOperation;
import net.sf.nakeduml.metamodel.actions.INakedReadVariableAction;
import net.sf.nakeduml.metamodel.activities.INakedOutputPin;
import nl.klasse.octopus.oclengine.IOclEngine;

public class VariableReader extends SimpleActionBuilder<INakedReadVariableAction> {
	public VariableReader(IOclEngine oclEngine, INakedReadVariableAction action, AbstractObjectNodeExpressor expressor) {
		super(oclEngine, action, expressor);
	}

	@Override
	public void implementActionOn(OJAnnotatedOperation operation, OJBlock block) {
		INakedOutputPin result = node.getResult();
		NakedStructuralFeatureMap resultMap = OJUtil.buildStructuralFeatureMap(result.getActivity(), result);
		expressor.maybeBuildResultVariable(operation, block, resultMap);
		NakedStructuralFeatureMap variableMap = OJUtil.buildStructuralFeatureMap(node.getActivity(), node.getVariable());
		String call=expressor.storeResults(resultMap, variableMap.umlName(), variableMap.isMany());
		block.addToStatements(call);
	}
}