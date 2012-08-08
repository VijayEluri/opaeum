package org.opaeum.javageneration.composition;

import java.util.List;

import nl.klasse.octopus.codegen.umlToJava.maps.StructuralFeatureMap;

import org.eclipse.ocl.uml.MessageType;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.opaeum.eclipse.EmfAssociationUtil;
import org.opaeum.eclipse.EmfClassifierUtil;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitAfter;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJClassifier;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJForStatement;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.javageneration.AbstractJavaProducingVisitor;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.javageneration.basicjava.AbstractStructureVisitor;
import org.opaeum.javageneration.basicjava.OperationAnnotator;
import org.opaeum.javageneration.maps.AssociationClassEndMap;
import org.opaeum.javageneration.oclexpressions.AttributeExpressionGenerator;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.runtime.domain.CompositionNode;

/**
 * This class implements the CompositionNode semantics which enriches the Java model with ideas on how compositions should ideally be
 * implemented.
 */
@StepDependency(phase = JavaTransformationPhase.class,requires = {OperationAnnotator.class},after = {OperationAnnotator.class,
		AttributeExpressionGenerator.class})
public class CompositionNodeImplementor extends AbstractStructureVisitor{
	protected static OJPathName COMPOSITION_NODE = new OJPathName(CompositionNode.class.getName());
	public static final String GET_OWNING_OBJECT = "getOwningObject";
	protected void visitClass(Classifier c){
		if(OJUtil.hasOJClass(c)){
			OJPathName path = ojUtil.classifierPathname(c);
			OJClassifier ojClassifier = this.javaModel.findClass(path);
			if(ojClassifier instanceof OJAnnotatedClass){
				OJAnnotatedClass ojClass = (OJAnnotatedClass) ojClassifier;
				boolean isTransientMessageStructure = c instanceof MessageType && !(EmfClassifierUtil.isPersistent(c));
				if(!isTransientMessageStructure){
					ojClass.addToImplementedInterfaces(COMPOSITION_NODE);
					addGetOwningObject(c, ojClass);
					addRemoveFromOwner(ojClass);
					addMarkDeleted(ojClass, c);
				}
				addAddToOwningObject(ojClass, c);
				addInit(c, ojClass);
				addConstructorForTests(c, ojClass);
			}
		}
	}
	public void addConstructorForTests(Classifier entity,OJAnnotatedClass ojClass){
		if(!isInterfaceOrAssociationClass(entity)){
			Property endToComposite = getLibrary().getEndToComposite(entity);
			if(endToComposite != null){
				Classifier owningType = (Classifier) endToComposite.getType();
				OJPathName paramPath = ojUtil.classifierPathname(owningType);
				OJConstructor testConstructor = ojClass.findConstructor(paramPath);
				if(testConstructor == null){
					testConstructor = new OJConstructor();
					ojClass.addToConstructors(testConstructor);
					testConstructor.addParam("owningObject", ojUtil.classifierPathname(owningType));
					if(isMap(endToComposite.getOtherEnd())){
						for(Property p:endToComposite.getOtherEnd().getQualifiers()){
							StructuralFeatureMap qMap = ojUtil.buildStructuralFeatureMap(p);
							testConstructor.addParam(qMap.fieldname(), qMap.javaTypePath());
							testConstructor.getBody().addToStatements(qMap.setter() + "(" + qMap.fieldname() + ")");
						}
					}
					testConstructor.getBody().addToStatements("init(owningObject)");
				}else{
				}
				testConstructor.setComment("This constructor is intended for easy initialization in unit tests");
				testConstructor.getBody().addToStatements("addToOwningObject()");
			}
		}
	}
	public void addAddToOwningObject(OJAnnotatedClass ojClass,Classifier entity){
		OJOperation addToOwningObject = new OJAnnotatedOperation("addToOwningObject");
		addToOwningObject
				.setComment("Call this method when you want to attach this object to the containment tree. Useful with transitive persistence");
		if(!isInterfaceOrAssociationClass(entity)){
			Property endToComposite = getLibrary().getEndToComposite(entity);
			if(endToComposite != null && !endToComposite.isDerived()){
				if(endToComposite.getAssociation() != null && EmfAssociationUtil.isClass(endToComposite.getAssociation())){
					AssociationClassEndMap aMap = new AssociationClassEndMap(ojUtil, endToComposite);
					addToOwningObject.getBody().addToStatements(
							aMap.getMap().getter() + "()." + aMap.getOtherEndToAssocationClassMap().internalAdder() + "("
									+ aMap.getEndToAssocationClassMap().getter() + "())");
				}else{
					StructuralFeatureMap featureMap = ojUtil.buildStructuralFeatureMap(endToComposite);
					StructuralFeatureMap otherFeatureMap = ojUtil.buildStructuralFeatureMap(endToComposite.getOtherEnd());
					if(isMap(otherFeatureMap.getProperty())){
						String qArgs = ojUtil.addQualifierArguments(otherFeatureMap.getProperty().getQualifiers(), "this");
						addToOwningObject.getBody().addToStatements(
								featureMap.getter() + "()." + otherFeatureMap.internalAdder() + "(" + qArgs + "(" + ojClass.getName() + ")this)");
					}else{
						addToOwningObject.getBody().addToStatements(
								featureMap.getter() + "()." + otherFeatureMap.internalAdder() + "((" + ojClass.getName() + ")this)");
					}
				}
			}
		}
		if(entity instanceof BehavioredClassifier && ((BehavioredClassifier) entity).getClassifierBehavior() != null){
			addToOwningObject.getBody().addToStatements("startClassifierBehavior()");
		}
		ojClass.addToOperations(addToOwningObject);
	}
	public boolean isInterfaceOrAssociationClass(Classifier c){
		return c instanceof Interface || c instanceof Association;
	}
	public void addMarkDeleted(OJAnnotatedClass ojClass,Classifier sc){
		OJAnnotatedOperation markDeleted = new OJAnnotatedOperation("markDeleted");
		ojClass.addToOperations(markDeleted);
		if(sc.getGenerals().size() >= 1){
			markDeleted.getBody().addToStatements("super.markDeleted()");
		}else if(AbstractJavaProducingVisitor.isPersistent(sc)){
			if(ojClass.findField("deletedOn") != null){
				ojClass.addToImports("java.util.Date");
				markDeleted.getBody().addToStatements("setDeletedOn(new Date(System.currentTimeMillis()))");
			}else{
				// is deletion relevant?
			}
		}
		markChildrenForDeletion(sc, ojClass, markDeleted);
		invokeOperationRecursively(sc, markDeleted, "markDeleted()");
	}
	protected void markChildrenForDeletion(Classifier sc,OJClass ojClass,OJAnnotatedOperation markDeleted){
		for(Property np:getLibrary().getEffectiveAttributes(sc)){
			if(!np.isComposite() && np.getOtherEnd() != null && np.getOtherEnd().isNavigable() && !np.isDerived()
					&& !np.getOtherEnd().isDerived() && (isPersistent(np.getType()) || np.getType() instanceof Interface)){
				StructuralFeatureMap map = ojUtil.buildStructuralFeatureMap(np);
				StructuralFeatureMap otherMap = ojUtil.buildStructuralFeatureMap(np.getOtherEnd());
				if(map.isManyToMany()){
					markDeleted.getBody().addToStatements(map.removeAll() + "(" + map.getter() + "())");
				}else if(map.isManyToOne()){
					OJIfStatement ifNotNull;
					if(isMap(np.getOtherEnd())){
						ifNotNull = new OJIfStatement(map.getter() + "()!=null", map.getter() + "()." + otherMap.internalRemover() + "("
								+ ojUtil.addQualifierArguments(np.getOtherEnd().getQualifiers(), "this") + "this)");
					}else{
						ifNotNull = new OJIfStatement(map.getter() + "()!=null", map.getter() + "()." + otherMap.internalRemover() + "(this)");
					}
					markDeleted.getBody().addToStatements(ifNotNull);
				}else if(map.isOneToOne()){
					// TODO this may have unwanted results such as removing the
					// owner from "this" too
					OJIfStatement ifNotNull = new OJIfStatement(map.getter() + "()!=null", map.getter() + "()." + otherMap.internalRemover()
							+ "(this)");
					markDeleted.getBody().addToStatements(ifNotNull);
				}
			}
		}
	}
	@VisitAfter(matchSubclasses = true)
	public void visitInterface(Interface i){
		if(!EmfClassifierUtil.isHelper(i) && OJUtil.hasOJClass(i)){
			OJPathName path = ojUtil.classifierPathname(i);
			OJClassifier ojClassifier = this.javaModel.findClass(path);
			((OJAnnotatedInterface) ojClassifier).addToSuperInterfaces(COMPOSITION_NODE);
			ojClassifier.addToImports(COMPOSITION_NODE);
		}
	}
	protected void addRemoveFromOwner(OJClass ojClass){
		OJAnnotatedOperation remove = new OJAnnotatedOperation("removeFromOwningObject");
		remove.getBody().addToStatements("this.markDeleted()");
		ojClass.addToOperations(remove);
	}
	/**
	 * Removes initialization logic from the default constructor and adds it to the init method which takes the
	 */
	protected void addInit(Classifier c,OJClass ojClass){
		OJOperation init = new OJAnnotatedOperation("init");
		if(isPersistent(c)){
			init.addParam("owner", COMPOSITION_NODE);
		}else{
			Property etc = getLibrary().getEndToComposite(c);
			if(etc != null){
				init.addParam("owner", ojUtil.classifierPathname(etc.getType()));
			}
		}
		init.setBody(ojClass.getDefaultConstructor().getBody());
		ojClass.getDefaultConstructor().setBody(new OJBlock());
		int start = 0;
		if(c.getGenerals().size() >= 1){
			OJSimpleStatement simpleStatement = new OJSimpleStatement("super.init(owner)");
			if(init.getBody().getStatements().isEmpty()){
				init.getBody().getStatements().add(simpleStatement);
			}else{
				init.getBody().getStatements().set(0, simpleStatement);
			}
			start++;
		}
		Property etc = getLibrary().getEndToComposite(c);
		if(etc != null && !etc.isDerived()){
			StructuralFeatureMap compositeFeatureMap = ojUtil.buildStructuralFeatureMap(etc);
			ojClass.addToImports(compositeFeatureMap.javaBaseTypePath());
			init.getBody()
					.getStatements()
					.add(start,
							new OJSimpleStatement("this." + compositeFeatureMap.internalAdder() + "((" + compositeFeatureMap.javaBaseType() + ")owner)"));
		}
		ojClass.addToOperations(init);
	}
	protected void addGetOwningObject(Classifier c,OJClass ojClass){
		OJOperation getOwner = new OJAnnotatedOperation(GET_OWNING_OBJECT);
		if(isPersistent(c)){
			getOwner.setReturnType(COMPOSITION_NODE);
		}else{
			Property endToComposite = getLibrary().getEndToComposite(c);
			if(endToComposite != null){
				getOwner.setReturnType(ojUtil.classifierPathname(endToComposite.getType()));
			}
		}
		getOwner.setBody(new OJBlock());
		Property endToComposite = getLibrary().getEndToComposite(c);
		if(endToComposite != null){
			StructuralFeatureMap map = ojUtil.buildStructuralFeatureMap(endToComposite);
			getOwner.getBody().addToStatements("return " + map.getter() + "()");
		}else{
			getOwner.getBody().addToStatements("return null");
		}
		ojClass.addToOperations(getOwner);
	}
	@Override
	protected void visitProperty(Classifier owner,StructuralFeatureMap buildStructuralFeatureMap){
		// TODO Auto-generated method stub
	}
	@Override
	protected void visitComplexStructure(Classifier umlOwner){
		if(EmfClassifierUtil.isCompositionParticipant(umlOwner)){
			visitClass((Classifier) umlOwner);
		}else if(EmfClassifierUtil.isStructuredDataType(umlOwner)){
			addMarkDeleted(findJavaClass(umlOwner), umlOwner);
		}
	}
	private void invokeOperationRecursively(Classifier ew,OJOperation markDeleted,String operationName){
		for(Property np:getLibrary().getEffectiveAttributes(ew)){
			StructuralFeatureMap map = ojUtil.buildStructuralFeatureMap(np);
			if(np.isComposite() && (isPersistent(np.getType()) || np.getType() instanceof Interface) && !np.isDerived()){
				Classifier type = (Classifier) np.getType();
				if(map.isMany()){
					markDeleted.getOwner().addToImports("java.util.ArrayList");
					OJForStatement forEach = new OJForStatement();
					forEach.setCollection("new ArrayList<" + map.javaBaseDefaultType() + ">(" + map.getter() + "())");
					forEach.setElemType(ojUtil.classifierPathname(type));
					forEach.setElemName("child");
					forEach.setBody(new OJBlock());
					forEach.getBody().addToStatements("child." + operationName);
					markDeleted.getBody().addToStatements(forEach);
				}else if(map.isOne()){
					OJIfStatement ifNotNull = new OJIfStatement(map.getter() + "()!=null", map.getter() + "()." + operationName);
					markDeleted.getBody().addToStatements(ifNotNull);
				}
			}
		}
	}
}