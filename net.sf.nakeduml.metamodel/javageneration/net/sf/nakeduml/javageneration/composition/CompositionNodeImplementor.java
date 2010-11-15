package net.sf.nakeduml.javageneration.composition;

import java.util.List;

import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.feature.visit.VisitAfter;
import net.sf.nakeduml.javageneration.AbstractJavaProducingVisitor;
import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.javametamodel.OJBlock;
import net.sf.nakeduml.javametamodel.OJClass;
import net.sf.nakeduml.javametamodel.OJClassifier;
import net.sf.nakeduml.javametamodel.OJConstructor;
import net.sf.nakeduml.javametamodel.OJForStatement;
import net.sf.nakeduml.javametamodel.OJIfStatement;
import net.sf.nakeduml.javametamodel.OJOperation;
import net.sf.nakeduml.javametamodel.OJPackage;
import net.sf.nakeduml.javametamodel.OJPathName;
import net.sf.nakeduml.javametamodel.OJSimpleStatement;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedClass;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedInterface;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedOperation;
import net.sf.nakeduml.javametamodel.generated.OJVisibilityKindGEN;
import net.sf.nakeduml.metamodel.core.INakedEntity;
import net.sf.nakeduml.metamodel.core.INakedInterface;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.core.INakedStructuredDataType;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import net.sf.nakeduml.textmetamodel.TextWorkspace;
import net.sf.nakeduml.util.CompositionNode;
import nl.klasse.octopus.codegen.umlToJava.maps.StructuralFeatureMap;
import nl.klasse.octopus.model.IModelElement;

/**
 * This class implements the CompositionNode semantics which enriches the Java
 * model with ideas on how compositions should ideally be implemented.
 * 
 * @author ampie
 * 
 */
public class CompositionNodeImplementor extends AbstractJavaProducingVisitor {
	private static final OJPathName COMPOSITION_NODE = new OJPathName(CompositionNode.class.getName());
	public static final String GET_OWNING_OBJECT = "getOwningObject";

	@Override
	public void initialize(INakedModelWorkspace workspace, OJPackage javaModel, NakedUmlConfig config, TextWorkspace textWorkspace) {
		// TODO Auto-generated method stub
		super.initialize(workspace, javaModel, config, textWorkspace);
	}

	@VisitAfter(matchSubclasses = true)
	public void visitClass(INakedEntity c) {
		if (isPersistent(c)) {
			OJPathName path = OJUtil.classifierPathname(c);
			OJClassifier ojClassifier = this.javaModel.findIntfOrCls(path);
			if (ojClassifier instanceof OJAnnotatedClass) {
				OJAnnotatedClass ojClass = (OJAnnotatedClass) ojClassifier;
				if (c instanceof INakedStructuredDataType) {
					// TODO implement this as "correct" as possible
				} else {
					INakedEntity entity = c;
					ojClass.addToImplementedInterfaces(COMPOSITION_NODE);
					addGetOwningObject(entity, ojClass);
					addRemoveFromOwner(entity, ojClass);
					addMarkDeleted(entity, ojClass);
					addAddToOwningObject(entity, ojClass);
					addInit(entity, ojClass);
					addConstructorForTests(ojClass, entity);
					addInternalSetOwner(entity, ojClass);
				}
			}
		}
	}

	@VisitAfter
	public void visitInterface(INakedInterface i) {
		if (hasEntityImplementationsOnly(i) && OJUtil.hasOJClass(i)) {
			OJPathName path = OJUtil.classifierPathname(i);
			OJClassifier ojClassifier = this.javaModel.findIntfOrCls(path);
			((OJAnnotatedInterface) ojClassifier).addToSuperInterfaces(COMPOSITION_NODE);
			ojClassifier.addToImports(COMPOSITION_NODE);
		}
	}

	private void addInternalSetOwner(INakedEntity nc, OJClass ojClass) {
		INakedProperty endToComposite = nc.getEndToComposite();
		// Composition is declared in this type, not supertype
		if (endToComposite != null) {
			OJOperation oper = new OJOperation();
			oper.setVisibility(OJVisibilityKindGEN.PROTECTED);
			oper.setComment("Used to set the owner internally in extended composition semantics");
			oper.setName("internalSetOwner");
			NakedStructuralFeatureMap toOwnerMap = new NakedStructuralFeatureMap(endToComposite);
			oper.addParam("newOwner", toOwnerMap.javaBaseTypePath());
			// Leave body empty for derived feature
			if (!endToComposite.isDerived()) {
				oper.getBody().addToStatements("this." + toOwnerMap.umlName() + "=newOwner");
			}
			ojClass.addToOperations(oper);
		}
	}

	protected void addConstructorForTests(OJAnnotatedClass ojClass, INakedEntity entity) {
		if (entity.hasComposite()) {
			INakedEntity owningType = (INakedEntity) entity.getEndToComposite().getNakedBaseType();
			OJPathName paramPath = OJUtil.classifierPathname(owningType);
			OJConstructor testConstructor = findConstructor(ojClass, paramPath);
			if (testConstructor == null) {
				testConstructor = new OJConstructor();
				ojClass.addToConstructors(testConstructor);
				testConstructor.addParam("owningObject", new OJPathName(owningType.getMappingInfo().getQualifiedJavaName()));
				testConstructor.getBody().addToStatements("init(owningObject)");
			} else {
			}
			testConstructor.setComment("This constructor is intended for easy initialization in unit tests");
			testConstructor.getBody().addToStatements("addToOwningObject()");
		}
	}

	protected void addRemoveFromOwner(INakedEntity sc, OJClass ojClass) {
		OJAnnotatedOperation remove = new OJAnnotatedOperation();
		remove.setName("removeFromOwningObject");
		remove.getBody().addToStatements("this.markDeleted()");
		ojClass.addToOperations(remove);
	}

	public void addMarkDeleted(INakedEntity sc, OJClass ojClass) {
		OJAnnotatedOperation markDeleted = new OJAnnotatedOperation();
		markDeleted.setName("markDeleted");
		ojClass.addToOperations(markDeleted);
		if (sc.hasSupertype()) {
			markDeleted.getBody().addToStatements("super.markDeleted()");
		} else if (isPersistent(sc)) {
			if (ojClass.findField("deletedOn") != null) {
				ojClass.addToImports("java.util.Date");
				markDeleted.getBody().addToStatements("setDeletedOn(new Date(System.currentTimeMillis()))");
			} else {
				// is deletion relevant?
			}
		}
		for (INakedProperty np : sc.getEffectiveAttributes()) {
			if (np.getOtherEnd() != null) {
				NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(np);
				NakedStructuralFeatureMap otherMap = new NakedStructuralFeatureMap(np.getOtherEnd());
				if (map.isManyToMany()) {
					markDeleted.getBody().addToStatements(map.removeAll() + "(" + map.getter() + "())");
				} else if (map.isManyToOne() && np.getOtherEnd().isNavigable()) {
					OJIfStatement ifNotNull = new OJIfStatement(map.getter() + "()!=null", map.getter() + "()." + otherMap.getter()
							+ "().remove((" + ojClass.getName() + ")this)");
					markDeleted.getBody().addToStatements(ifNotNull);
				} else if (map.isOneToOne() && !np.isInverse() && np.getOtherEnd().isNavigable() && !np.isDerived() && !np.isDerivedUnion()) {
					// TODO this may have unwanted results such as removing the
					// owner from "this" too
					OJIfStatement ifNotNull = new OJIfStatement(map.getter() + "()!=null", map.getter() + "()." + otherMap.setter()
							+ "(null)");
					markDeleted.getBody().addToStatements(ifNotNull);
				}
			}
		}
		invokeOperationRecursively(sc, markDeleted, "markDeleted()");
	}

	public static void invokeOperationRecursively(INakedEntity ew, OJOperation markDeleted, String operationName) {
		List<? extends INakedProperty> awss = ew.getOwnedAttributes();
		for (int i = 0; i < awss.size(); i++) {
			IModelElement a = (IModelElement) awss.get(i);
			if (a instanceof INakedProperty) {
				INakedProperty np = (INakedProperty) a;
				NakedStructuralFeatureMap map = OJUtil.buildStructuralFeatureMap(np);
				if (np.isComposite() && np.getNakedBaseType() instanceof INakedEntity && !np.isDerived()) {
					INakedEntity type = (INakedEntity) np.getNakedBaseType();
					if (map.isMany()) {
						markDeleted.getOwner().addToImports("java.util.ArrayList");
						OJForStatement forEach = new OJForStatement();
						forEach.setCollection("new ArrayList<" + map.javaBaseDefaultType() + ">(" + map.getter() + "())");
						forEach.setElemType(OJUtil.classifierPathname(type));
						forEach.setElemName("child");
						forEach.setBody(new OJBlock());
						forEach.getBody().addToStatements("child." + operationName);
						markDeleted.getBody().addToStatements(forEach);
					} else if (map.isOne()) {
						OJIfStatement ifNotNull = new OJIfStatement(map.getter() + "()!=null", map.getter() + "()." + operationName);
						markDeleted.getBody().addToStatements(ifNotNull);
					}
				}
			}
		}
	}

	protected void addAddToOwningObject(INakedEntity entity, OJClass ojClass) {
		OJOperation addToOwningObject = new OJAnnotatedOperation();
		addToOwningObject
				.setComment("Call this method when you want to attach this object to the containment tree. Useful with transitive persistence");
		addToOwningObject.setName("addToOwningObject");
		if (entity.hasComposite()) {
			INakedProperty endToComposite = entity.getEndToComposite();
			StructuralFeatureMap featureMap = new NakedStructuralFeatureMap(endToComposite);
			StructuralFeatureMap otherFeatureMap = new NakedStructuralFeatureMap(endToComposite.getOtherEnd());
			if (otherFeatureMap.isCollection()) {
				addToOwningObject.getBody().addToStatements(
						featureMap.getter() + "()." + otherFeatureMap.getter() + "().add((" + ojClass.getName() + ")this)");
			} else {
				addToOwningObject.getBody().addToStatements(
						featureMap.getter() + "()." + otherFeatureMap.setter() + "((" + ojClass.getName() + ")this)");
			}
		}
		ojClass.addToOperations(addToOwningObject);
	}

	/**
	 * Removes initialization logic from the default constructor and adds it to
	 * the init method which takes the
	 */
	protected void addInit(INakedEntity ew, OJClass ojClass) {
		OJOperation init = new OJAnnotatedOperation();
		init.addParam("owner", COMPOSITION_NODE);
		init.setName("init");
		init.setBody(ojClass.getDefaultConstructor().getBody());
		ojClass.getDefaultConstructor().setBody(new OJBlock());
		int start = 0;
		if (ew.getSupertype() != null) {
			OJSimpleStatement simpleStatement = new OJSimpleStatement("super.init(owner)");
			if (init.getBody().getStatements().isEmpty()) {
				init.getBody().getStatements().add(simpleStatement);
			} else {
				init.getBody().getStatements().set(0, simpleStatement);
			}
			start++;
		}
		if (ew.hasComposite()) {
			StructuralFeatureMap compositeFeatureMap = new NakedStructuralFeatureMap(ew.getEndToComposite());
			ojClass.addToImports(compositeFeatureMap.javaBaseTypePath());
			init.getBody().getStatements()
					.add(start, new OJSimpleStatement("internalSetOwner((" + compositeFeatureMap.javaBaseType() + ")owner)"));
		}
		ojClass.addToOperations(init);
	}

	protected void addGetOwningObject(INakedEntity entity, OJClass ojClass) {
		OJOperation getOwner = new OJAnnotatedOperation();
		getOwner.setName(GET_OWNING_OBJECT);
		// TODO this needs to become a uml library
		// getOwner.setReturnType(ReflectionUtil.getUtilInterface(CompositionNode.class));
		getOwner.setReturnType(this.COMPOSITION_NODE);
		getOwner.setBody(new OJBlock());
		if (entity.hasComposite()) {
			INakedProperty ce = entity.getEndToComposite();
			getOwner.getBody().addToStatements("return get" + ce.getMappingInfo().getJavaName().getCapped() + "()");
		} else {
			getOwner.getBody().addToStatements("return null");
		}
		ojClass.addToOperations(getOwner);
	}
}