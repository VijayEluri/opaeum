package org.opaeum.javageneration.basicjava.simpleactions;

import nl.klasse.octopus.codegen.umlToJava.maps.PropertyMap;

import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.javageneration.basicjava.AbstractObjectNodeExpressor;

public class VariableReader extends SimpleNodeBuilder<ReadVariableAction> {
	public VariableReader(ReadVariableAction action, AbstractObjectNodeExpressor expressor) {
		super( action, expressor);
	}

	@Override
	public void implementActionOn(OJAnnotatedOperation operation, OJBlock block) {
		OutputPin result = node.getResult();
		PropertyMap resultMap = ojUtil.buildStructuralFeatureMap(result);
		expressor.buildResultVariable(operation, block, resultMap);
		PropertyMap variableMap = ojUtil.buildStructuralFeatureMap(node.getVariable());
		String call=expressor.storeResults(resultMap, variableMap.fieldname(), variableMap.isMany());
		block.addToStatements(call);
	}
}