package org.opaeum.validation;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.TypedElement;
import org.opaeum.eclipse.EmfPropertyUtil;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.metamodel.validation.IValidationRule;

@StepDependency(phase = ValidationPhase.class)
public class PropertyValidation extends AbstractValidator{
	enum PropertyValidationRule implements IValidationRule{
		INTERFACE_PROPERTIES_CANNOT_BE_STATIC("Interface properties cannot be static","{0} is marked as static but belongs to an interface", pkg.getFeature_IsStatic()),
		REDEFINED_ASSOCIATION_END_ON_BIDIRECTIONAL_ASSOCATION(
				"One end of a bidirectional cannot be redefined as it breaks to two-way semantics.",
				"{0} redefines {1} which participates in a bidirectional association. Redefine both ends of the association.",pkg.getProperty_RedefinedProperty()),
				REDEFINED_ASSOCIATION_END_SAME_NO_OF_QUALIFIERS(
						"Qualifiers of an association end must correspond with qualifiers of the redefined association end.",
						"{0} redefines {1} but their qualifiers do not correspond in number and/or type.",pkg.getProperty_RedefinedProperty()),
		DERIVED_UNION_NOT_BIDIRECTIONAL(
				"When an association end on a bidirectional association is marked as a derived union, the opposite end should be marked as a derived union too.",
				"{0} is marked as a derived union, but its opposite end {1} is not.",pkg.getProperty_IsDerivedUnion()),
		SUBSETTED_PROPERTY_NO_UNION("Subsetted properties must be marked as derived unions",
				"{0} subsets {1} which is not marked as a derived union",pkg.getProperty_SubsettedProperty()),
		SUBSETTED_PROPERTY_NOT_IN_CONTEXT("Subsetted properties must be in the redefinition context of the subseting property",
				"{0} subset {1}  which is not accessible from {2}",pkg.getProperty_SubsettedProperty()),
				REDEFINED_PROPERTY_NOT_IN_CONTEXT("Redefined properties must be in the redefinition context of the subseting property",
						"{0} redefines {1} which is not accessible from {2}",pkg.getProperty_RedefinedProperty()),
		TYPED_ELEMENT_NO_TYPE("Typed elements must be typed",
				"{0}'s type is not specified",pkg.getTypedElement_Type()), QUALIFIER_WITHOUT_BACKING_ATTRIBUTE_NOT_SUPPORTED("Qualifiers need properties on the owning classifier with the same name and type",
						"Qualifier {0} on {1} does not have a backing property on the owning classifier {2}",pkg.getProperty_Qualifier());
		private String description;
		private String messagePattern;
		private EStructuralFeature feature[];
		private PropertyValidationRule(String description,String messagePattern, EStructuralFeature ... feature){
			this.description = description;
			this.messagePattern = messagePattern;
			this.feature=feature;
		}
		public EStructuralFeature[] getFeatures(){
			return feature;
		}
		public String getDescription(){
			return this.description;
		}
		public String getMessagePattern(){
			return messagePattern;
		}
	}
	@VisitBefore(match={Parameter.class,Property.class})
	public void visitTypedElement(TypedElement te){
		if(te.getType()==null){
			getErrorMap().putError(te, PropertyValidationRule.TYPED_ELEMENT_NO_TYPE);
		}
		if(te instanceof Property){
			visitProperty((Property) te);
		}
	}
	public void visitProperty(Property p){
		// TODO property cannot be derivedUnion AND have defaultVAlue
		if(p.getAssociationEnd() == null){
			Classifier owner = (Classifier) EmfPropertyUtil.getOwningClassifier(p);
			if(p.isStatic() && owner instanceof Interface){
				getErrorMap().putError(p, PropertyValidationRule.INTERFACE_PROPERTIES_CANNOT_BE_STATIC);
			}
			if(p.isNavigable() && p.isDerivedUnion() && p.getAssociation() != null && p.getOtherEnd() != null && p.getOtherEnd().isNavigable()
					&& !p.getOtherEnd().isDerivedUnion()){
				getErrorMap().putError(p, PropertyValidationRule.DERIVED_UNION_NOT_BIDIRECTIONAL, p.getOtherEnd());
			}
			if(p.getSubsettedProperties().size() > 0){
				for(Property sp:p.getSubsettedProperties()){
					if(!sp.isDerivedUnion()){
						getErrorMap().putError(p, PropertyValidationRule.SUBSETTED_PROPERTY_NO_UNION, sp);
					}
					if(workspace.getOpaeumLibrary().findEffectiveAttribute(owner, sp.getName()) == null){
						getErrorMap().putError(p, PropertyValidationRule.SUBSETTED_PROPERTY_NOT_IN_CONTEXT, sp, owner);
					}
				}
			}
			if(p.getRedefinedProperties().size() > 0){
				for(Property rp:p.getRedefinedProperties()){
					if(workspace.getOpaeumLibrary().findEffectiveAttribute(owner, rp.getName()) == null){
						getErrorMap().putError(p, PropertyValidationRule.REDEFINED_PROPERTY_NOT_IN_CONTEXT, rp, owner);
					}
					if(rp.getQualifiers().size()!=p.getQualifiers().size()){
						//TODO check for type of qualifiers
						getErrorMap().putError(p, PropertyValidationRule.REDEFINED_ASSOCIATION_END_SAME_NO_OF_QUALIFIERS, rp);
					}
					if(rp.isNavigable() && rp.getAssociation() != null && rp.getOtherEnd().isNavigable()){
						if(p.getAssociation() == null || !p.getOtherEnd().getRedefinedProperties().contains(rp.getOtherEnd())){
							getErrorMap().putError(p, PropertyValidationRule.REDEFINED_ASSOCIATION_END_ON_BIDIRECTIONAL_ASSOCATION, rp);
						}
					}
				}
			}
		}else{
			if(EmfPropertyUtil.getBackingPropertyForQualifier(p)==null){
				getErrorMap().putError(p, PropertyValidationRule.QUALIFIER_WITHOUT_BACKING_ATTRIBUTE_NOT_SUPPORTED, p.getAssociationEnd(), EmfPropertyUtil.getOwningClassifier(p.getAssociationEnd().getOtherEnd()));
			}
		}
	}
}
