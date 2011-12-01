package org.nakeduml.runtime.jbpm;


import java.util.Set;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.ProcessBuilderFactory;
import org.drools.io.ResourceFactory;
import org.drools.marshalling.impl.ProcessMarshallerFactory;
import org.drools.runtime.process.ProcessRuntimeFactory;
import org.jbpm.marshalling.impl.ProcessMarshallerFactoryServiceImpl;
import org.jbpm.marshalling.impl.ProcessMarshallerRegistry;
import org.jbpm.process.builder.ProcessBuilderFactoryServiceImpl;
import org.jbpm.process.instance.ProcessRuntimeFactoryServiceImpl;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.node.CompositeContextNode;
import org.jbpm.workflow.core.node.CompositeNode;
import org.jbpm.workflow.core.node.DynamicNode;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.StateNode;
import org.jbpm.workflow.instance.impl.NodeInstanceFactoryRegistry;
import org.jbpm.workflow.instance.impl.factory.CreateNewNodeFactory;

public abstract class AbstractJbpmKnowledgeBase {
	private KnowledgeBase knowledgeBase;
	
	public abstract Set<String> getProcessLocations();

	public KnowledgeBase getKnowledgeBase() {
		if (this.knowledgeBase == null) {
			this.knowledgeBase = readKnowledgeBase();
		}
		return this.knowledgeBase;
	}

	private KnowledgeBase readKnowledgeBase() {
		ProcessBuilderFactory.setProcessBuilderFactoryService(new ProcessBuilderFactoryServiceImpl());
		ProcessMarshallerFactory.setProcessMarshallerFactoryService(new ProcessMarshallerFactoryServiceImpl());
		ProcessRuntimeFactory.setProcessRuntimeFactoryService(new ProcessRuntimeFactoryServiceImpl());
		ProcessMarshallerRegistry.INSTANCE.register(RuleFlowProcess.RULEFLOW_TYPE, new UmlProcessMarshaller());
		NodeInstanceFactoryRegistry.INSTANCE.register(StateNode.class, new CreateNewNodeFactory(Uml2StateInstance.class));
		NodeInstanceFactoryRegistry.INSTANCE.register(CompositeNode.class, new CreateNewNodeFactory(Uml2CompositeNodeInstance.class));
		NodeInstanceFactoryRegistry.INSTANCE.register(CompositeContextNode.class, new CreateNewNodeFactory(Uml2CompositeContextNodeInstance.class));
		NodeInstanceFactoryRegistry.INSTANCE.register(EndNode.class, new CreateNewNodeFactory(Uml2EndStateInstance.class));
		NodeInstanceFactoryRegistry.INSTANCE.register(DynamicNode.class, new CreateNewNodeFactory(Uml2DynamicNodeInstance.class));
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		for(String string:getProcessLocations()){
			kbuilder.add(ResourceFactory.newClassPathResource(string), ResourceType.DRF);
		}
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		return kb;
	}
}