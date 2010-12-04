package net.sf.nakeduml.javageneration.jbpm5.activity;

import java.util.List;

import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.jbpm5.actions.Jbpm5ActionBuilder;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.javametamodel.OJBlock;
import net.sf.nakeduml.javametamodel.OJPathName;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedClass;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedOperation;
import net.sf.nakeduml.metamodel.activities.INakedExpansionNode;
import net.sf.nakeduml.metamodel.activities.INakedExpansionRegion;
import nl.klasse.octopus.oclengine.IOclEngine;

public class ExpansionRegionBuilder extends Jbpm5ActionBuilder<INakedExpansionRegion> {
	public ExpansionRegionBuilder(IOclEngine oclEngine, INakedExpansionRegion node) {
		super(oclEngine, node);
	}

	@Override
	public void implementActionOn(OJAnnotatedOperation oper) {
		List<INakedExpansionNode> outputElements = node.getOutputElement();
		OJAnnotatedClass owner = (OJAnnotatedClass) oper.getOwner();
		INakedExpansionNode inputElement = node.getInputElement().get(0);
		OJAnnotatedOperation collectionExpression=new OJAnnotatedOperation(ActivityUtil.getCollectionExpression(inputElement));
		collectionExpression.addParam("context", ActivityUtil.PROCESS_CONTEXT);
		owner.addToOperations(collectionExpression);
		collectionExpression.setReturnType(new OJPathName("java.util.Collection"));
		OJBlock collectionBody = collectionExpression.getBody();
		for (INakedExpansionNode expansionNode : outputElements) {
			NakedStructuralFeatureMap map=OJUtil.buildStructuralFeatureMap(expansionNode.getActivity(), expansionNode);
			collectionExpression.getBody().addToStatements("context.setVariable(\"" + map.umlName() + "\","+map.javaDefaultValue() + ")");
			owner.addToImports(map.javaDefaultTypePath());
			owner.addToImports(map.javaTypePath());
		}
		collectionBody.addToStatements("return " + expressor.expressInputPinOrOutParamOrExpansionNode(collectionBody, inputElement));
	}
}