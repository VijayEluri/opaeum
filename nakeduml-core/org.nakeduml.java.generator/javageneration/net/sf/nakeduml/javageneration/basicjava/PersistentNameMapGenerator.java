package net.sf.nakeduml.javageneration.basicjava;

import org.nakeduml.environment.PersistentNameClassMap;
import org.nakeduml.java.metamodel.OJBlock;
import org.nakeduml.java.metamodel.OJClass;
import org.nakeduml.java.metamodel.OJConstructor;
import org.nakeduml.java.metamodel.OJParameter;
import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.OJVisibilityKind;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedClass;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedField;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;
import org.nakeduml.runtime.domain.AbstractEntity;

import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.AbstractJavaProducingVisitor;
import net.sf.nakeduml.javageneration.JavaTextSource.OutputRootId;
import net.sf.nakeduml.javageneration.composition.tinker.TinkerExtendedCompositionSemanticsJavaStep;
import net.sf.nakeduml.javageneration.oclexpressions.OclExpressionExecution.UtilCreator;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.models.INakedModel;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import nl.klasse.octopus.codegen.umlToJava.modelgenerators.visitors.UtilityCreator;

public class PersistentNameMapGenerator extends AbstractJavaProducingVisitor{
	private boolean isIntegrationPhase;
	OJBlock initBlock;
	private boolean isTinker = false;
	public PersistentNameMapGenerator(boolean isIntegrationPhase, boolean isTinker){
		super();
		this.isIntegrationPhase = isIntegrationPhase;
		this.isTinker = isTinker;
	}
	public class EntityCollector extends AbstractJavaProducingVisitor{
		@VisitBefore(matchSubclasses = true)
		public void visitClassifier(INakedClassifier c){
			if(isPersistent(c)){
				initBlock.addToStatements("map.put(\"" + c.getMappingInfo().getQualifiedPersistentName() + "\"," + c.getMappingInfo().getQualifiedJavaName() + ".class)");
			}
		}
	}
	@VisitBefore
	public void visitWorkspace(INakedModelWorkspace ws){
		if(isIntegrationPhase){
			createMapClass(OutputRootId.INTEGRATED_ADAPTOR_GEN_SRC);
			new EntityCollector().startVisiting(ws);
		}
	}
	@VisitBefore
	public void visitModel(INakedModel m){
		if(!isIntegrationPhase){
			createMapClass(OutputRootId.DOMAIN_GEN_SRC);
			EntityCollector collector = new EntityCollector();
			for(INakedElement e:getModelInScope()){
				if(e instanceof INakedModel){
					INakedModel model = (INakedModel) e;
					collector.visitRecursively(model);
				}
			}
		}
	}
	protected void createMapClass(OutputRootId output){
		OJClass mapClass = new OJAnnotatedClass(this.isTinker?"TinkerPersistentNameClassMapImpl":"PersistentNameClassMapImpl");
		mapClass.addToImplementedInterfaces(new OJPathName(this.isTinker?"org.nakeduml.environment.TinkerPersistentNameClassMap":PersistentNameClassMap.class.getName()));
		UtilityCreator.getUtilPack().addToClasses(mapClass);
		OJPathName classExtendsAbstractEntity = new OJPathName("Class<? extends " +(this.isTinker?"BaseTinker":"AbstractEntity")+ ">");
		super.createTextPath(mapClass, output);
		OJAnnotatedField map = new OJAnnotatedField("map", new OJPathName("java.util.Map"));
		map.getType().addToElementTypes(new OJPathName("String"));
		map.getType().addToElementTypes(classExtendsAbstractEntity);
		mapClass.addToImports("java.util.Map");
		mapClass.addToImports("java.util.HashMap");
		mapClass.addToImports(AbstractEntity.class.getName());
		map.setInitExp("new HashMap<String, Class<? extends "+(this.isTinker?"BaseTinker":"AbstractEntity")+">>()");
		mapClass.addToFields(map);
		map.setFinal(true);
		OJConstructor constr = new OJConstructor();
		mapClass.addToConstructors(constr);
		initBlock = constr.getBody();
		mapClass.addToImports(this.isTinker?AbstractEntity.class.getName():"org.util.BaseTinker");
		OJAnnotatedOperation getClass = new OJAnnotatedOperation("getClass", classExtendsAbstractEntity);
		getClass.addToParameters(new OJParameter("name", new OJPathName("String")));
		mapClass.addToOperations(getClass);
		getClass.getBody().addToStatements("return map.get(name)");
	}
}