package net.sf.nakeduml.javageneration.basicjava.simpleactions;

import net.sf.nakeduml.javageneration.basicjava.AbstractObjectNodeExpressor;
import net.sf.nakeduml.javageneration.jbpm5.TaskUtil;
import net.sf.nakeduml.javageneration.maps.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.metamodel.bpm.INakedEmbeddedScreenFlowTask;
import net.sf.nakeduml.metamodel.workspace.NakedUmlLibrary;

import org.nakeduml.java.metamodel.OJBlock;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;

public class EmbeddedScreenFlowTaskCaller extends AbstractBehaviorCaller<INakedEmbeddedScreenFlowTask>{
	public EmbeddedScreenFlowTaskCaller(NakedUmlLibrary oclEngine,INakedEmbeddedScreenFlowTask action,AbstractObjectNodeExpressor expressor){
		super(oclEngine, action, expressor);
	}
	@Override
	protected void maybeStartBehavior(OJAnnotatedOperation operation,OJBlock block,NakedStructuralFeatureMap resultMap){
		String taskName = resultMap.umlName();
		TaskUtil.implementAssignmentsAndDeadlines(operation, block, node.getTaskDefinition(), taskName);
		block.addToStatements(taskName + ".setReturnInfo(context)");
		block.addToStatements(taskName + ".execute()");
	}
	protected NakedStructuralFeatureMap getResultMap(){
		return OJUtil.buildStructuralFeatureMap(node, getLibrary());
	}
	@Override
	protected boolean shouldStoreMessageStructureOnProcess(){
		return true;
	}
}