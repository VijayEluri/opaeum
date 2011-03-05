package org.nakeduml.projectgeneration;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitAfter;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.filegeneration.TextFileGenerator;
import net.sf.nakeduml.javageneration.JavaTextSource;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.javametamodel.OJBlock;
import net.sf.nakeduml.javametamodel.OJField;
import net.sf.nakeduml.javametamodel.OJForStatement;
import net.sf.nakeduml.javametamodel.OJIfStatement;
import net.sf.nakeduml.javametamodel.OJParameter;
import net.sf.nakeduml.javametamodel.OJPathName;
import net.sf.nakeduml.javametamodel.OJSimpleStatement;
import net.sf.nakeduml.javametamodel.OJTryStatement;
import net.sf.nakeduml.javametamodel.OJVisibilityKind;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedClass;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedField;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedOperation;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedPackage;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedParameter;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotationValue;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedEntity;
import net.sf.nakeduml.metamodel.core.INakedPackage;
import net.sf.nakeduml.metamodel.models.INakedModel;
import net.sf.nakeduml.seam3.persistence.DependentScopedSession;
import nl.klasse.octopus.model.IClassifier;

@StepDependency(phase = ProjectGenerationPhase.class, requires = { TextFileGenerator.class }, before = { TextFileGenerator.class })
public class ProjectTestGenerationStep extends AbstractProjectGenerationStep {

	private Set<INakedPackage> packages = new HashSet<INakedPackage>();

	@VisitBefore(matchSubclasses = true)
	public void visitEntities(INakedEntity entity) {
		packages.add((INakedPackage) entity.getNameSpace());
	}
	
	@VisitAfter(matchSubclasses = true)
	public void visitModel(INakedModel model) {
		// Fetch root entity
		INakedEntity root = findRootEntity();
		createStartOperation(root);
		OJAnnotatedClass baseTest = createBaseTestClass();
		createDummyTestClass(baseTest, root);
	}

	private void createDummyTestClass(OJAnnotatedClass baseTest, INakedEntity root) {
		OJAnnotatedClass dummyTest = new OJAnnotatedClass();
		dummyTest.setSuperclass(baseTest.getPathName());
		dummyTest.setName("DummyTest");
		dummyTest.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("org.junit.runner.RunWith"), new OJPathName("org.jboss.arquillian.junit.Arquillian")));
		OJAnnotatedPackage owner = (OJAnnotatedPackage) javaModel.findPackage(OJUtil.packagePathname(currentModel));
		owner.addToClasses(dummyTest);
		super.createTextPath(dummyTest, JavaTextSource.OutputRootId.WEBAPP_GEN_TEST_SRC);

		OJAnnotatedField session = new OJAnnotatedField("session", new OJPathName("org.hibernate.Session"));
		session.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("javax.inject.Inject")));
		dummyTest.addToFields(session);
		
		OJAnnotatedOperation createTestArchive = new OJAnnotatedOperation("createTestArchive");
		dummyTest.addToOperations(createTestArchive);
		createTestArchive.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("org.jboss.arquillian.api.Deployment")));
		createTestArchive.setStatic(true);
		OJPathName archive = new OJPathName("org.jboss.shrinkwrap.api.Archive");
		archive.addToElementTypes(new OJPathName("?"));
		createTestArchive.setReturnType(archive);
		OJPathName ioExceptionPath = new OJPathName(IOException.class.getName());
		dummyTest.addToImports(ioExceptionPath);
		OJPathName illegalArgumentException = new OJPathName(IllegalArgumentException.class.getName());
		createTestArchive.setThrows(new HashSet<OJPathName>(Arrays.asList(new OJPathName(ClassNotFoundException.class.getName()), ioExceptionPath, illegalArgumentException)));
		
		dummyTest.addToImports(new OJPathName("org.jboss.shrinkwrap.api.spec.WebArchive"));
		dummyTest.addToImports(new OJPathName("net.sf.nakeduml.arquillian.ArquillianUtils"));
		dummyTest.addToImports(new OJPathName("net.sf.nakeduml.test.NakedUtilTestClasses"));
		
		createTestArchive.getBody().addToStatements("WebArchive war = ArquillianUtils.createWarArchive(false)");
		createTestArchive.getBody().addToStatements("war.addWebResource(\"WEB-INF/beans.xml\", \"beans.xml\")");
		createTestArchive.getBody().addToStatements("war.addWebResource(\"hibernate.cfg.xml\", \"classes/hibernate.cfg.xml\")");
		createTestArchive.getBody().addToStatements("war.addWebResource(\"data.generation.properties\", \"data.generation.properties\")");
		createTestArchive.getBody().addToStatements("war.addPackages(true, NakedUtilTestClasses.getTestPackages())");
		createTestArchive.getBody().addToStatements("war.addPackages(true, getTestPackages())");
		createTestArchive.getBody().addToStatements("return war");
		
		OJAnnotatedOperation test = new OJAnnotatedOperation("test");
		test.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("org.junit.Test")));
		test.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.SuppressWarnings"),"unchecked"));
		
		
		test.getBody().addToStatements("List<" + root.getMappingInfo().getJavaName() + "> roots = session.createQuery(\"select h from "+ root.getMappingInfo().getJavaName() +" h\").list()");
		dummyTest.addToOperations(test);
		dummyTest.addToImports(new OJPathName("java.util.List"));
		dummyTest.addToImports(OJUtil.classifierPathname(root));
		dummyTest.addToImports(new OJPathName("org.junit.Assert"));
		
		test.getBody().addToStatements("Assert.assertTrue(roots.size()>0)");
	}

	private OJAnnotatedClass createBaseTestClass() {
		OJAnnotatedClass baseTest = new OJAnnotatedClass();
		baseTest.setName("BaseTest");
		OJAnnotatedPackage owner = (OJAnnotatedPackage) javaModel.findPackage(OJUtil.packagePathname(currentModel));
		owner.addToClasses(baseTest);
		super.createTextPath(baseTest, JavaTextSource.OutputRootId.WEBAPP_GEN_TEST_SRC);
		createGetTestPackagesOper(baseTest);
		return baseTest;
	}
	
	private void createGetTestPackagesOper(OJAnnotatedClass baseTest) {
		baseTest.addToImports(new OJPathName("java.lang.Package"));
		OJPathName list = new OJPathName("java.util.List");
		baseTest.addToImports(list);
		OJAnnotatedOperation getTestPackages = new OJAnnotatedOperation("getTestPackages");
		getTestPackages.setReturnType(new OJPathName("java.lang.Package[]"));
		baseTest.addToOperations(getTestPackages);
		getTestPackages.setVisibility(OJVisibilityKind.PUBLIC);
		getTestPackages.setStatic(true);
		OJPathName ioExceptionPath = new OJPathName(IOException.class.getName());
		getTestPackages.setThrows(new HashSet<OJPathName>(Arrays.asList(new OJPathName(ClassNotFoundException.class.getName()), ioExceptionPath)));
		baseTest.addToImports(ioExceptionPath);
		
		OJField classes = new OJField();
		classes.setName("packages");
		OJPathName classPath = new OJPathName("java.lang.Package");
		list.addToElementTypes(classPath);
		classes.setType(list);
		classes.setInitExp("new ArrayList<Package>()");
		baseTest.addToImports(new OJPathName("java.util.ArrayList"));
		getTestPackages.getBody().addToLocals(classes);
		
		for (INakedPackage p : packages) {
			String entityName = "";
			for (IClassifier c : p.getClassifiers()) {
				if (c instanceof INakedEntity) {
					entityName = c.getName();
					baseTest.addToImports(OJUtil.classifierPathname((INakedEntity)c));
					break;
				}
			}
			OJSimpleStatement addPackage = new OJSimpleStatement("packages.add(" + entityName + ".class.getPackage())");
			getTestPackages.getBody().addToStatements(addPackage);
		}
		
		baseTest.addToImports(new OJPathName(currentModel.getName() + ".util.Stdlib"));
		OJSimpleStatement addPackage = new OJSimpleStatement("packages.add(Stdlib.class.getPackage())");
		getTestPackages.getBody().addToStatements(addPackage);
		addPackage = new OJSimpleStatement("packages.add(BaseTest.class.getPackage())");
		getTestPackages.getBody().addToStatements(addPackage);
		
		getTestPackages.getBody().addToStatements("Package[] result = new Package[packages.size()]");
		getTestPackages.getBody().addToStatements("packages.toArray(result)");
		getTestPackages.getBody().addToStatements("return result");
		
	}

	private void createStartOperation(INakedEntity root) {
		OJAnnotatedClass startUp = new OJAnnotatedClass();
		startUp.setName("StartUp");
		OJAnnotatedPackage owner = (OJAnnotatedPackage) javaModel.findPackage(OJUtil.packagePathname(currentModel));
		owner.addToClasses(startUp);
		super.createTextPath(startUp, JavaTextSource.OutputRootId.WEBAPP_GEN_TEST_SRC);

		OJPathName rootPathname = OJUtil.classifierPathname(root);
		startUp.addToImports(rootPathname);
		addClassFields(startUp, root);
		addStartOperation(startUp, root);
	}

	private void addClassFields(OJAnnotatedClass startUp, INakedEntity root) {
		OJAnnotatedField session = new OJAnnotatedField("session", new OJPathName("org.hibernate.Session"));
		session.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("javax.inject.Inject")));
		OJPathName dependent = new OJPathName(DependentScopedSession.class.getName());
		session.addAnnotationIfNew(new OJAnnotationValue(dependent));
		startUp.addToImports(dependent);
		startUp.addToFields(session);
		OJAnnotatedField transaction = new OJAnnotatedField("transaction", new OJPathName("org.jboss.seam.persistence.transaction.SeamTransaction"));
		transaction.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("javax.inject.Inject")));
		transaction.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("org.jboss.seam.persistence.transaction.DefaultTransaction")));
		startUp.addToFields(transaction);
		OJAnnotatedField dataGeneratorProperty = new OJAnnotatedField("dataGeneratorProperty", new OJPathName("net.sf.nakeduml.util.DataGeneratorProperty"));
		dataGeneratorProperty.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("javax.inject.Inject")));
		startUp.addToFields(dataGeneratorProperty);

		OJAnnotatedField rootGeneratorProperty = new OJAnnotatedField("rootDataGenerator", new OJPathName(root.getMappingInfo().getQualifiedJavaName()
				+ "DataGenerator"));
		rootGeneratorProperty.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("javax.inject.Inject")));
		startUp.addToFields(rootGeneratorProperty);

	}

	private INakedEntity findRootEntity() {
		INakedEntity firstEntity = findFirstEntity(currentModel);
		return findRootEntity(firstEntity);
	}

	private INakedEntity findRootEntity(INakedEntity firstEntity) {
		if (firstEntity.getEndToComposite() == null) {
			return firstEntity;
		} else {
			return findRootEntity((INakedEntity) firstEntity.getEndToComposite().getBaseType());
		}
	}

	private INakedEntity findFirstEntity(INakedPackage p) {
		for (INakedElement c : p.getOwnedElements()) {
			if (c instanceof INakedEntity) {
				return (INakedEntity) c;
			} else if (c instanceof INakedPackage) {
				return findFirstEntity((INakedPackage) c);
			}
		}
		return null;
	}

	private OJAnnotatedOperation addStartOperation(OJAnnotatedClass startUp, INakedEntity root) {
		OJPathName rootPathname = OJUtil.classifierPathname(root);
		OJAnnotatedOperation start = new OJAnnotatedOperation("start");
		OJAnnotatedParameter param = new OJAnnotatedParameter("webapp", new OJPathName("org.jboss.seam.servlet.WebApplication"));
		OJPathName observes = new OJPathName("javax.enterprise.event.Observes");
		startUp.addToImports(observes);
		param.addAnnotationIfNew(new OJAnnotationValue(observes));
		OJPathName started = new OJPathName("org.jboss.seam.servlet.event.Started");
		startUp.addToImports(started);
		param.addAnnotationIfNew(new OJAnnotationValue(started));
		start.addToParameters(param);
		startUp.addToOperations(start);

		OJTryStatement ojTryStatement = new OJTryStatement();
		start.getBody().addToStatements(ojTryStatement);
		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));

		OJBlock tryBlock = new OJBlock();
		ojTryStatement.setTryPart(tryBlock);
		OJSimpleStatement ojSimpleStatement = new OJSimpleStatement("transaction.begin()");
		tryBlock.addToStatements(ojSimpleStatement);

		OJField rootField = new OJField();
		rootField.setName("the" + root.getMappingInfo().getJavaName().toString());
		rootField.setType(rootPathname);
		String rootName = root.getMappingInfo().getJavaName().toString();
		rootField.setInitExp("(" + rootName + ")session.createQuery(\"from " + rootName
				+ " a where a.name = :name\").setText(\"name\", dataGeneratorProperty.getProperty(\"" + rootName.toLowerCase() + ".name_0\")).uniqueResult()");

		OJIfStatement ifStatement = new OJIfStatement();
		ifStatement.setCondition(rootField.getName() + " == null");
		OJBlock ifBlock = new OJBlock();
		OJField generatedRoots = new OJField();
		generatedRoots.setName(root.getMappingInfo().getJavaName().getPlural().getDecapped().toString());
		OJPathName list = new OJPathName("java.util.List");
		startUp.addToImports(list);
		list.addToElementTypes(rootPathname);
		generatedRoots.setType(list);
		generatedRoots.setInitExp("rootDataGenerator.create" + rootName + "()");
		ifBlock.addToLocals(generatedRoots);
		ifStatement.setThenPart(ifBlock);
		tryBlock.addToLocals(rootField);

		tryBlock.addToStatements(ifStatement);

		OJForStatement forRoots = new OJForStatement(rootName.toLowerCase(), rootPathname, generatedRoots.getName());
		ifBlock.addToStatements(forRoots);
		OJSimpleStatement persist = new OJSimpleStatement("session.persist(" + rootName.toLowerCase() + ")");
		forRoots.getBody().addToStatements(persist);

		OJSimpleStatement populate = new OJSimpleStatement("rootDataGenerator.populate" + rootName + "(" + generatedRoots.getName() + ")");
		ifBlock.addToStatements(populate);
		OJSimpleStatement flush = new OJSimpleStatement("session.flush()");
		ifBlock.addToStatements(flush);
		OJSimpleStatement commit = new OJSimpleStatement("transaction.commit()");
		ifBlock.addToStatements(commit);

		OJBlock catchBlock = new OJBlock();
		ojTryStatement.setCatchPart(catchBlock);

		OJTryStatement catchTry = new OJTryStatement();
		catchBlock.addToStatements(catchTry);

		OJBlock catchTryBlock = new OJBlock();
		catchTry.setTryPart(catchTryBlock);
		catchTryBlock.addToStatements("transaction.rollback()");
		catchTry.setCatchParam(new OJParameter("e1", new OJPathName("java.lang.Exception")));
		OJBlock tryCatchCatch = new OJBlock();
		tryCatchCatch.addToStatements("throw new RuntimeException(e1)");
		catchTry.setCatchPart(tryCatchCatch);

		catchBlock.addToStatements("throw new RuntimeException(e)");
		return start;
	}

}
