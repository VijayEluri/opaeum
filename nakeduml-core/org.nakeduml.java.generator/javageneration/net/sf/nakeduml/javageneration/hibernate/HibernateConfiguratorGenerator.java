package net.sf.nakeduml.javageneration.hibernate;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.AbstractJavaProducingVisitor;
import net.sf.nakeduml.javageneration.JavaSourceFolderIdentifier;
import net.sf.nakeduml.javageneration.JavaTransformationPhase;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import nl.klasse.octopus.codegen.umlToJava.modelgenerators.visitors.UtilityCreator;

import org.nakeduml.java.metamodel.OJBlock;
import org.nakeduml.java.metamodel.OJIfStatement;
import org.nakeduml.java.metamodel.OJOperation;
import org.nakeduml.java.metamodel.OJPackage;
import org.nakeduml.java.metamodel.OJParameter;
import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.OJTryStatement;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedClass;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedField;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;
import org.nakeduml.java.metamodel.generated.OJVisibilityKindGEN;

@StepDependency(phase = JavaTransformationPhase.class, requires = {HibernateAnnotator.class}, after = { HibernateAnnotator.class })
public class HibernateConfiguratorGenerator extends AbstractJavaProducingVisitor {
	@VisitBefore
	public void visit(INakedModelWorkspace ws) {
		OJPackage util = javaModel.findPackage(UtilityCreator.getUtilPathName());
		OJAnnotatedClass hibernateConfigurator = new OJAnnotatedClass("HibernateConfigurator");
		util.addToClasses(hibernateConfigurator);
		super.createTextPath(hibernateConfigurator, JavaSourceFolderIdentifier.DOMAIN_GEN_TEST_SRC);
		OJAnnotatedField instance = new OJAnnotatedField("INSTANCE",hibernateConfigurator.getPathName());
		instance.setStatic(true);
		instance.setVisibility(OJVisibilityKindGEN.PUBLIC);
		instance.setInitExp("new HibernateConfigurator()");
		hibernateConfigurator.addToFields(instance);
		addGetConfiguration(hibernateConfigurator);
		addGetEntityManagerFactory(hibernateConfigurator);
		addGetEntityManager(hibernateConfigurator);
		addCloseEntityManager(hibernateConfigurator);
		OJOperation getInstance = new OJAnnotatedOperation("getInstance");
		getInstance.setStatic(true);
		getInstance.setReturnType(hibernateConfigurator.getPathName());
		getInstance.getBody().addToStatements("return INSTANCE");
		hibernateConfigurator.addToOperations(getInstance);
		OJOperation main = new OJAnnotatedOperation("main");
		main.addParam("args", new OJPathName("String[]"));
		OJAnnotatedField schemaGen = new OJAnnotatedField("schemaGen",new OJPathName("SchemaExport"));
		hibernateConfigurator.addToImports(new OJPathName("org.hibernate.tool.hbm2ddl.SchemaExport"));
		schemaGen.setInitExp("new SchemaExport(getInstance().getConfiguration().getHibernateConfiguration())");
		main.getBody().addToLocals(schemaGen);
		main.getBody().addToStatements("schemaGen.setDelimiter(\";\")");
		main.getBody().addToStatements("schemaGen.create(true,true)");
		main.setStatic(true);
		hibernateConfigurator.addToOperations(main);
	}

	private void addCloseEntityManager(OJAnnotatedClass hibernateConfigurator) {
		OJOperation closeEntityManager = new OJAnnotatedOperation("closeEntityManager");
		hibernateConfigurator.addToOperations(closeEntityManager);
		OJIfStatement ifNull = new OJIfStatement("this.entityManager!=null && this.entityManager.isOpen()", "entityManager.close()");
		OJTryStatement tryIt = new OJTryStatement();
		tryIt.setTryPart(new OJBlock());
		tryIt.setCatchPart(new OJBlock());
		tryIt.getTryPart().addToStatements(ifNull);
		tryIt.getTryPart().addToStatements("this.entityManager=null");
		OJParameter exception = new OJParameter();
		exception.setName("e");
		exception.setType(new OJPathName("Exception"));
		tryIt.setCatchParam(exception);
		closeEntityManager.getBody().addToStatements(tryIt);
	}

	private void addGetEntityManager(OJAnnotatedClass hibernateConfigurator) {
		OJAnnotatedField entityManagerFactory = new OJAnnotatedField("entityManagerFactory", new OJPathName("javax.persistence.EntityManagerFactory"));
		hibernateConfigurator.addToFields(entityManagerFactory);
		OJOperation getEntityManagerFactory = new OJAnnotatedOperation("getEntityManagerFactory");
		getEntityManagerFactory.setReturnType(entityManagerFactory.getType());
		hibernateConfigurator.addToOperations(getEntityManagerFactory);
		OJIfStatement ifNull = new OJIfStatement("this.entityManagerFactory==null", "entityManagerFactory=getConfiguration().buildEntityManagerFactory()");
		getEntityManagerFactory.getBody().addToStatements(ifNull);
		getEntityManagerFactory.getBody().addToStatements("return this.entityManagerFactory");
	}

	private void addGetEntityManagerFactory(OJAnnotatedClass hibernateConfigurator) {
		OJAnnotatedField entityManager = new OJAnnotatedField("entityManager", new OJPathName("javax.persistence.EntityManager"));
		hibernateConfigurator.addToFields(entityManager);
		OJOperation getEntityManager = new OJAnnotatedOperation("getEntityManager");
		getEntityManager.setReturnType(entityManager.getType());
		hibernateConfigurator.addToOperations(getEntityManager);
		OJIfStatement ifNull = new OJIfStatement("this.entityManager==null", "entityManager=getEntityManagerFactory().createEntityManager()");
		getEntityManager.getBody().addToStatements(ifNull);
		getEntityManager.getBody().addToStatements("return this.entityManager");
	}

	private void addGetConfiguration(OJAnnotatedClass hibernateConfigurator) {
		OJAnnotatedField configuration = new OJAnnotatedField("configuration",new OJPathName("org.hibernate.ejb.Ejb3Configuration"));
		hibernateConfigurator.addToFields(configuration);
		OJOperation getConfiguration = new OJAnnotatedOperation("getConfiguration");
		getConfiguration.setReturnType(configuration.getType());
		hibernateConfigurator.addToOperations(getConfiguration);
		OJIfStatement ifNull = new OJIfStatement("this.configuration==null", "configuration=new Ejb3Configuration()");
		ifNull.getThenPart().addToStatements("this.configuration.configure(\"" + workspace.getIdentifier() + ".hibernate.config.xml\")");
		ifNull.getThenPart().addToStatements("this.configuration.setProperty(\"hibernate.validator.autoregister_listeners\", \"false\")");
		ifNull.getThenPart().addToStatements("this.entityManagerFactory=this.configuration.buildEntityManagerFactory()");
		getConfiguration.getBody().addToStatements(ifNull);
		getConfiguration.getBody().addToStatements("return this.configuration");
	}

	public static OJPathName getConfiguratorPathName() {
		return UtilityCreator.getUtilPathName().append("HibernateConfigurator");
	}


}