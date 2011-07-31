package net.sf.nakeduml.javageneration.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.feature.SortedProperties;
import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.TransformationContext;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.AbstractJavaProducingVisitor;
import net.sf.nakeduml.javageneration.JavaTransformationStep;
import net.sf.nakeduml.javageneration.CharArrayTextSource.OutputRootId;
import net.sf.nakeduml.javageneration.JavaTransformationPhase;
import net.sf.nakeduml.javageneration.auditing.AuditImplementationStep;
import net.sf.nakeduml.javageneration.basicjava.JavaMetaInfoMapGenerator;
import net.sf.nakeduml.javageneration.jbpm5.Jbpm5JavaStep;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.metamodel.bpm.INakedEmbeddedTask;
import net.sf.nakeduml.metamodel.commonbehaviors.INakedSignal;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedElementOwner;
import net.sf.nakeduml.metamodel.core.INakedOperation;
import net.sf.nakeduml.metamodel.core.INakedRootObject;
import net.sf.nakeduml.metamodel.models.INakedModel;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import net.sf.nakeduml.textmetamodel.TextWorkspace;
import nl.klasse.octopus.codegen.umlToJava.modelgenerators.visitors.UtilityCreator;

import org.nakedum.velocity.AbstractTextProducingVisitor;
import org.nakeduml.environment.Environment;
import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedPackage;

@StepDependency(phase = JavaTransformationPhase.class,requires = {},after = {})
public class HibernateConfigGenerator extends AbstractTextProducingVisitor implements JavaTransformationStep{
	private static final String CDI_ENVIRONMENT = "org.nakeduml.environment.adaptor.CdiEnvironment";
	private static final String DOMAIN_ENVIRONMENT = "org.nakeduml.environment.domain.DomainEnvironment";
	@Override
	public void initialize(OJAnnotatedPackage pac,NakedUmlConfig config,TextWorkspace textWorkspace,TransformationContext context){
		super.initialize(config, textWorkspace, context);
		
	}
	public HibernateConfigGenerator(){
		super();
	}
	public  final class MappingCollector extends AbstractJavaProducingVisitor{
		private final HashSet<OJPathName> classes = new HashSet<OJPathName>();
		private final HashSet<OJPathName> signals = new HashSet<OJPathName>();
		public MappingCollector(INakedModelWorkspace workspace){
			this.workspace=workspace;
		}
		@VisitBefore
		public void signal(INakedSignal s){
			signals.add(OJUtil.classifierPathname(s));
		}
		@VisitBefore(matchSubclasses = true)
		public void visitClassifier(INakedClassifier c){
			if(isPersistent(c)){
				classes.add(OJUtil.classifierPathname(c));
			}
		}
		@VisitBefore(matchSubclasses = true)
		public void visitOpaqueAction(INakedEmbeddedTask a){
			classes.add(OJUtil.classifierPathname(a.getMessageStructure()));
		}
		@VisitBefore(matchSubclasses = true)
		public void visitOperation(INakedOperation o){
			if(o.isLongRunning()){
				classes.add(OJUtil.classifierPathname(o.getMessageStructure()));
			}
		}
		@Override
		public Collection<? extends INakedElementOwner> getChildren(INakedElementOwner root){
			return super.getChildren(root);
		}
	}
	@VisitBefore
	public void visitWorkspace(INakedModelWorkspace workspace){
		if(transformationContext.isIntegrationPhase()){
			Collection<INakedRootObject> rootObjects = (Collection<INakedRootObject>) workspace.getOwnedElements();
			String hibernateConfigName = workspace.getIdentifier() + "-hibernate.cfg.xml";
			generateConfigAndEnvironment(rootObjects, hibernateConfigName, OutputRootId.INTEGRATED_ADAPTOR_GEN_RESOURCE, true);
			HashMap<String,Object> vars = buildVars(rootObjects, false);
			vars.put("pkg", HibernateUtil.getHibernatePackage(true));
			processTemplate(workspace, "templates/Model/Jbpm5HibernateConfig.vsl", "standalone-" + hibernateConfigName,
					OutputRootId.INTEGRATED_ADAPTOR_TEST_GEN_RESOURCE, vars);
		}
	}
	@VisitBefore
	public void visitModel(INakedModel model){
		if(!transformationContext.isIntegrationPhase()){
			String hibernateConfigName = model.getIdentifier() + "-hibernate.cfg.xml";
			Collection<INakedRootObject> selfAndDependencies = new ArrayList<INakedRootObject>(model.getDependencies());
			selfAndDependencies.add(model);
			generateConfigAndEnvironment(selfAndDependencies, hibernateConfigName, OutputRootId.DOMAIN_GEN_TEST_RESOURCE, false);
			generateConfigAndEnvironment(selfAndDependencies, hibernateConfigName, OutputRootId.ADAPTOR_GEN_TEST_RESOURCE, true);
			HashMap<String,Object> vars = buildVars(selfAndDependencies, false);
			vars.put("pkg", HibernateUtil.getHibernatePackage(true));
			processTemplate(workspace, "templates/Model/Jbpm5HibernateConfig.vsl", "standalone-" + hibernateConfigName,
					OutputRootId.ADAPTOR_GEN_TEST_RESOURCE, vars);
		}
	}
	private void generateConfigAndEnvironment(Collection<INakedRootObject> models,String hibernateConfigName,OutputRootId outputRootId,
			boolean isAdaptorEnvironment){
		SortedProperties properties = new SortedProperties();
		HashMap<String,Object> vars = buildVars(models, isAdaptorEnvironment);
		if(isAdaptorEnvironment){
			properties.setProperty(Environment.JBPM_KNOWLEDGE_BASE_IMPLEMENTATION, UtilityCreator.getUtilPathName()
					+ ".jbpm.adaptor.JbpmKnowledgeBase");
			properties.setProperty(Environment.DBMS, "POSTGRESQL");
			properties.setProperty(Environment.PERSISTENT_NAME_CLASS_MAP, UtilityCreator.getUtilPathName() + ".metainfo.adaptor."
					+ JavaMetaInfoMapGenerator.javaMetaInfoMapName());
			processTemplate(workspace, "templates/Model/HornetQJms.vsl", "hornetq-jms.xml", outputRootId, vars);
			processTemplate(workspace, "templates/Model/HornetQConfig.vsl", "hornetq-configuration.xml", outputRootId, vars);
		}else{
			properties.setProperty(Environment.DBMS, "HSQL");
			properties.setProperty(Environment.JBPM_KNOWLEDGE_BASE_IMPLEMENTATION, UtilityCreator.getUtilPathName()
					+ ".jbpm.domain.JbpmKnowledgeBase");
			properties.setProperty(Environment.PERSISTENT_NAME_CLASS_MAP, UtilityCreator.getUtilPathName() + ".metainfo.domain."
					+ JavaMetaInfoMapGenerator.javaMetaInfoMapName());
		}
		properties.setProperty(Environment.ENVIRONMENT_IMPLEMENTATION, isAdaptorEnvironment ? CDI_ENVIRONMENT : DOMAIN_ENVIRONMENT);
		properties.setProperty(Environment.HIBERNATE_CONFIG_NAME, hibernateConfigName);
		properties.setProperty(Environment.DB_USER, config.getDbUser());
		findOrCreateTextFile(properties, outputRootId, Environment.PROPERTIES_FILE_NAME);
		processTemplate(workspace, "templates/Model/Jbpm5HibernateConfig.vsl", hibernateConfigName, outputRootId, vars);
	}
	private HashMap<String,Object> buildVars(Collection<? extends INakedElement> models,boolean isAdaptorEnvironment){
		HashMap<String,Object> vars = new HashMap<String,Object>();
		boolean requiresAudit = transformationContext.isAnyOfFeaturesSelected(AuditImplementationStep.class);
		vars.put("requiresAuditing", requiresAudit);
		vars.put("config", this.config);
		vars.put("isAdaptorEnvironment", isAdaptorEnvironment);
		vars.put("requiresJbpm", transformationContext.isAnyOfFeaturesSelected(Jbpm5JavaStep.class));
		MappingCollector collector = new MappingCollector(workspace);
		// do all models
		for(INakedElement element:models){
			if(element instanceof INakedModel){
				collector.visitRecursively(element);
			}
		}
		vars.put("persistentClasses", collector.classes);
		vars.put("signals", collector.signals);
		vars.put("pkg", HibernateUtil.getHibernatePackage(isAdaptorEnvironment));
		return vars;
	}
}
