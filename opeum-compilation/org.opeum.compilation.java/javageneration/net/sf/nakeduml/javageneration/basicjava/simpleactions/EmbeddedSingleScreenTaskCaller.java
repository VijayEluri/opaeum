package org.opeum.javageneration.basicjava.simpleactions;

import java.util.List;

import org.opeum.javageneration.basicjava.AbstractObjectNodeExpressor;
import org.opeum.javageneration.jbpm5.TaskUtil;
import org.opeum.javageneration.maps.NakedStructuralFeatureMap;
import org.opeum.javageneration.util.OJUtil;
import org.opeum.metamodel.activities.INakedActivity;
import org.opeum.metamodel.activities.INakedInputPin;
import org.opeum.metamodel.bpm.INakedEmbeddedSingleScreenTask;
import org.opeum.metamodel.core.INakedProperty;
import org.opeum.metamodel.workspace.OpeumLibrary;

import org.opeum.java.metamodel.OJBlock;
import org.opeum.java.metamodel.annotation.OJAnnotatedField;
import org.opeum.java.metamodel.annotation.OJAnnotatedOperation;

public class EmbeddedSingleScreenTaskCaller extends SimpleNodeBuilder<INakedEmbeddedSingleScreenTask>{
	public EmbeddedSingleScreenTaskCaller(OpeumLibrary oclEngine,INakedEmbeddedSingleScreenTask action,AbstractObjectNodeExpressor expressor){
		super(oclEngine, action, expressor);
	}
	@Override
	public void implementActionOn(OJAnnotatedOperation operation,OJBlock block){
		NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(node, getLibrary());
		OJAnnotatedField taskVar = expressor.buildResultVariable(operation, block, map);
		taskVar.setInitExp("new " + map.javaType() + "()");
		block.addToLocals(taskVar);
		List<INakedInputPin> inputValues = node.getInputValues();
		INakedActivity activity = node.getActivity();
		for(INakedInputPin input:inputValues){
			NakedStructuralFeatureMap propertyMap = OJUtil.buildStructuralFeatureMap(activity, input, false);
			operation.getBody().addToStatements(taskVar.getName()+"." + propertyMap.setter() + "(" + readPin(operation, block, input) + ")");
		}
		block.addToStatements(taskVar.getName()+".setReturnInfo(context)");
		TaskUtil.implementAssignmentsAndDeadlines(operation, block, node.getTaskDefinition(), taskVar.getName());
		//Add to containment tree
		INakedProperty attr = activity.findEmulatedAttribute(node);
		block.addToStatements(OJUtil.buildStructuralFeatureMap(attr).adder()+"("+ taskVar.getName() + ")");
		//Store invocation in process
		block.addToStatements(expressor.setterForSingleResult(map, taskVar.getName()));
		block.addToStatements(taskVar.getName()+".execute()");
	}
}
