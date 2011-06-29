package org.nakeduml.tinker.basicjava.tinker;

import java.util.Collections;

import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.feature.TransformationContext;
import net.sf.nakeduml.feature.visit.VisitAfter;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.AbstractJavaProducingVisitor;
import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.composition.AbstractCompositionNodeStrategy;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.metamodel.core.INakedAssociationClass;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedEntity;
import net.sf.nakeduml.metamodel.core.INakedInterface;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.core.INakedSimpleType;
import net.sf.nakeduml.metamodel.core.internal.StereotypeNames;
import net.sf.nakeduml.textmetamodel.TextWorkspace;

import org.nakeduml.java.metamodel.OJClass;
import org.nakeduml.java.metamodel.OJField;
import org.nakeduml.java.metamodel.OJForStatement;
import org.nakeduml.java.metamodel.OJIfStatement;
import org.nakeduml.java.metamodel.OJOperation;
import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.OJTryStatement;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedClass;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedInterface;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedPackage;

public class TinkerSoftDeleteTransformation extends AbstractJavaProducingVisitor {

	public static final String IF_EDGE_NOT_DELETED = "if_edge_not_deleted";
	public static final String FOR_MANY_IF_NOT_DELETED = "forManyIfNotDeleted";

	public void initialize(OJAnnotatedPackage javaModel, NakedUmlConfig config, TextWorkspace textWorkspace, TransformationContext context) {
		super.initialize(javaModel, config, textWorkspace, context);
	}

	@VisitBefore(matchSubclasses = true)
	public void visitClass(INakedEntity c) {
		if (OJUtil.hasOJClass(c) && !(c instanceof INakedSimpleType)) {
			OJAnnotatedClass ojClass = findJavaClass(c);
			if (!c.hasSupertype()) {
				extendsBaseTinkerSoftDelete(ojClass);
			}
			transformMarkDeletedToSoft(c, ojClass);
		}
	}

	@VisitAfter(matchSubclasses = true)
	public void visitFeature(INakedEntity c) {
		for (INakedProperty p : c.getEffectiveAttributes()) {
			if (p.getOwner() instanceof INakedInterface && OJUtil.hasOJClass(c)) {
				if (p.getAssociation() instanceof INakedAssociationClass) {
				} else {
					visitProperty(c, OJUtil.buildStructuralFeatureMap(p));
				}
			}
		}
	}
	
	@VisitAfter(matchSubclasses = true)
	public void visitFeature(INakedProperty p){
		if(OJUtil.hasOJClass(p.getOwner())){
			if(p.getAssociation() instanceof INakedAssociationClass){
			}else{
				visitProperty(p.getOwner(), OJUtil.buildStructuralFeatureMap(p));
			}
		}
	}	

	private void visitProperty(INakedClassifier umlOwner, NakedStructuralFeatureMap map) {
		INakedProperty p = map.getProperty();
		if (!OJUtil.isBuiltIn(p)) {
			if (p.getNakedBaseType().hasStereotype(StereotypeNames.HELPER)) {
			} else if (p.isDerived() || p.isReadOnly()) {
				implementAttributeFully(umlOwner, map);
			} else {
				implementAttributeFully(umlOwner, map);
			}
		}
	}

	private void implementAttributeFully(INakedClassifier umlOwner, NakedStructuralFeatureMap map) {
		OJAnnotatedClass owner = findJavaClass(umlOwner);
		buildGetter(owner, map, false);
	}

	private OJOperation buildGetter(OJAnnotatedClass owner, NakedStructuralFeatureMap map, boolean returnDefault) {
		OJOperation getter = owner.findOperation(map.getter(), Collections.EMPTY_LIST);
		if (owner instanceof OJAnnotatedInterface) {
		} else if (returnDefault) {
		} else {
			owner.addToImports(TinkerUtil.tinkerFormatter);
			owner.addToImports(new OJPathName("java.util.Date"));
			INakedProperty prop = map.getProperty();
			if (prop.getOtherEnd() != null && prop.getOtherEnd().isNavigable() && !(prop.getOtherEnd().isDerived() || prop.getOtherEnd().isReadOnly())) {
				if (map.isManyToOne()) {
					addDeletedOnFilterToPolymorphicGetterForToOne(map, getter);
				} else if (map.isOneToMany()) {
					addDeletedOnFilterTobuildPolymorphicGetterForMany(map, getter);
				} else if (map.isManyToMany()) {
					addDeletedOnFilterTobuildPolymorphicGetterForMany(map, getter);
				} else if (map.isOneToOne()) {
					addDeletedOnFilterToPolymorphicGetterForToOne(map, getter);
				}
			} else {
				if (!prop.isDerived() && prop.getBaseType() instanceof INakedEntity) {
					if (map.isOne()) {
						addDeletedOnFilterToPolymorphicGetterForToOne(map, getter);
					} else if (map.isMany()) {
						addDeletedOnFilterTobuildPolymorphicGetterForMany(map, getter);
					} else {
					}
				}				
			}
		}
		return getter;
	}

	private void addDeletedOnFilterTobuildPolymorphicGetterForMany(NakedStructuralFeatureMap map, OJOperation getter) {
		OJForStatement ojForStatement = (OJForStatement) getter.getBody().findStatement(TinkerAttributeImplementorStrategy.POLYMORPHIC_GETTER_FOR_TO_MANY_FOR);
		OJTryStatement ojTryStatement = (OJTryStatement) ojForStatement.getBody().findStatement(
				TinkerAttributeImplementorStrategy.POLYMORPHIC_GETTER_FOR_TO_MANY_TRY);
		OJIfStatement ifNotDeleted = new OJIfStatement(
				"edge.getProperty(\"deletedOn\")==null");
		ifNotDeleted.setName(FOR_MANY_IF_NOT_DELETED);
		ifNotDeleted.addToThenPart(ojTryStatement);
		ojForStatement.getBody().getStatements().remove(ojTryStatement);
		ojForStatement.getBody().addToStatements(ifNotDeleted);
	}

	private void addDeletedOnFilterToPolymorphicGetterForToOne(NakedStructuralFeatureMap map, OJOperation getter) {
		OJIfStatement ojIfStatement = (OJIfStatement) getter.getBody().findStatementRecursive(TinkerAttributeImplementorStrategy.POLYMORPHIC_GETTER_FOR_TO_ONE_IF);
		OJTryStatement ojTryStatement = (OJTryStatement) ojIfStatement.getThenPart().findStatementRecursive(
				TinkerAttributeImplementorStrategy.POLYMORPHIC_GETTER_FOR_TO_ONE_TRY);
		OJIfStatement ifNotDeleted = new OJIfStatement(
				"edge.getProperty(\"deletedOn\")==null");
		ifNotDeleted.setName(IF_EDGE_NOT_DELETED);
		ifNotDeleted.addToThenPart(ojTryStatement);
		ojIfStatement.getThenPart().getStatements().remove(ojTryStatement);
		ojIfStatement.getThenPart().addToStatements(ifNotDeleted);
	}

	private void transformMarkDeletedToSoft(INakedEntity c, OJAnnotatedClass ojClass) {
		OJAnnotatedOperation markDeleted = (OJAnnotatedOperation) ojClass.findOperation("markDeleted", Collections.EMPTY_LIST);
		markDeleted.getBody().removeAllFromStatements();
		if (c.hasSupertype()) {
			markDeleted.getBody().addToStatements("super.markDeleted()");
		}
		AbstractCompositionNodeStrategy.invokeOperationRecursively(c, markDeleted, "markDeleted()");
		if (!c.hasSupertype()) {
			removeVertex(c, ojClass, markDeleted);
		}
	}
	
	private void removeVertex(INakedEntity sc, OJClass ojClass, OJOperation markDeleted) {
		OJPathName datePath = new OJPathName("java.util.Date");
		OJField deletedOn = new OJField();
		deletedOn.setName("deletedOn");
		deletedOn.setType(datePath);
		deletedOn.setInitExp("new Date(System.currentTimeMillis())");
		markDeleted.getBody().addToLocals(deletedOn);
		markDeleted.getBody().addToStatements("setDeletedOn(deletedOn)");
		markDeleted.getBody().addToStatements("Iterable<Edge> inEdges = this.vertex.getInEdges()");

		OJForStatement forInEdgesStatement = new OJForStatement("inEdge", TinkerUtil.edgePathName, "inEdges");
		OJIfStatement ifNotAudit = new OJIfStatement("!inEdge.getLabel().equals(\"audit\")");
		ifNotAudit.addToThenPart("inEdge.setProperty(\"deletedOn\", TinkerFormatter.format(deletedOn))");
		forInEdgesStatement.getBody().addToStatements(ifNotAudit);
		ojClass.addToImports(TinkerUtil.tinkerFormatter);
		markDeleted.getBody().addToStatements(forInEdgesStatement);

		markDeleted.getBody().addToStatements("Iterable<Edge> outEdges = this.vertex.getOutEdges()");
		OJIfStatement ifNotAudit2 = new OJIfStatement("!outEdge.getLabel().equals(\"audit\")");
		ifNotAudit2.addToThenPart("outEdge.setProperty(\"deletedOn\", TinkerFormatter.format(deletedOn))");
		OJForStatement forOutEdgesStatement = new OJForStatement("outEdge", TinkerUtil.edgePathName, "outEdges");
		forOutEdgesStatement.getBody().addToStatements(ifNotAudit2);
		ojClass.addToImports(TinkerUtil.tinkerFormatter);
		markDeleted.getBody().addToStatements(forOutEdgesStatement);

		ojClass.addToImports(datePath);
		ojClass.addToImports(TinkerUtil.edgePathName);
	}

	private void extendsBaseTinkerSoftDelete(OJAnnotatedClass ojClass) {
		ojClass.setSuperclass(TinkerUtil.BASE_AUDIT_SOFT_DELETE_TINKER);
	}

}