package net.sf.nakeduml.emf.extraction;

import java.util.Iterator;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitAfter;
import net.sf.nakeduml.metamodel.core.INakedComment;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedElementOwner;
import net.sf.nakeduml.metamodel.core.INakedEnumeration;
import net.sf.nakeduml.metamodel.core.INakedInstanceSpecification;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.core.INakedSlot;
import net.sf.nakeduml.metamodel.core.internal.NakedCommentImpl;
import net.sf.nakeduml.metamodel.core.internal.NakedInstanceSpecificationImpl;
import net.sf.nakeduml.metamodel.core.internal.NakedSlotImpl;
import net.sf.nakeduml.metamodel.core.internal.NakedValueSpecificationImpl;
import net.sf.nakeduml.metamodel.profiles.INakedStereotype;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EEnumLiteralImpl;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.TimeEvent;
import org.eclipse.uml2.uml.Trigger;

/**
 * Applies stereotypes to elements. Uses InstanceSpecifications to represent
 * stereotype applications Should be done last. pre: All Stereotype classifiers,
 * their attributes and their attributes types must be available
 * 
 * @author ampie
 * 
 */
@StepDependency(phase = EmfExtractionPhase.class, requires = { ConstraintExtractor.class }, after = { ConstraintExtractor.class })
public class StereotypeApplicationExtractor extends AbstractExtractorFromEmf {
	@VisitAfter
	public void visitTrigger(Trigger tr){

	}
	@VisitAfter(matchSubclasses = true)
	public void visit(Element element) {
		INakedElement nakedPeer = getNakedPeer(element);
		if (element instanceof Comment) {
			visitComment((Comment) element);
		}else if(element instanceof Trigger){
			//TimeEvent are stored with the Trigger
			Trigger trigger = (Trigger)element;
			if(trigger.getEvent() instanceof TimeEvent){
				element=trigger.getEvent();
			}
		}
		if (nakedPeer != null) {
			addStereotypes(nakedPeer, element);
			addKeywords(nakedPeer, element);
		}
	}

	private void addKeywords(INakedElement nakedPeer, Element e) {
		for (String s : e.getKeywords()) {
			INakedInstanceSpecification is = new NakedInstanceSpecificationImpl();
			// No classifier - string classifier will be assigned during linkage
			is.initialize(nakedPeer.getId() + s, s,false);
			nakedPeer.addStereotype(is);
			workspace.putModelElement(is);
		}
	}

	public void visitComment(Comment a) {
		INakedElement e = super.getNakedPeer(a.getOwner());
		if (e != null) {
			INakedComment nakedComment = new NakedCommentImpl();
			initialize(nakedComment, a, a.getOwner());
			nakedComment.setBody(a.getBody());
			e.getComments().add(nakedComment);
		}
	}

	private void addStereotypes(INakedElement nakedElement, Element modelElement) {
		Iterator<Stereotype> stereotypes = modelElement.getAppliedStereotypes().iterator();
		while (stereotypes.hasNext()) {
			Stereotype stereotype = (Stereotype) stereotypes.next();
			INakedStereotype nakedStereotype = (INakedStereotype) getNakedPeer(stereotype);
			if (nakedStereotype != null) {
				INakedInstanceSpecification instanceSpec = buildStereotypeApplication(modelElement, stereotype, nakedStereotype);
				if (nakedElement instanceof INakedElementOwner) {
					instanceSpec.setOwnerElement(nakedElement);
				}
				nakedElement.addStereotype(instanceSpec);
			}
		}
	}

	private INakedInstanceSpecification buildStereotypeApplication(Element modelElement, Stereotype stereotype,
			INakedStereotype nakedStereotype) {
		INakedInstanceSpecification instanceSpec = new NakedInstanceSpecificationImpl();
		instanceSpec.setClassifier(nakedStereotype);
		instanceSpec.setName(nakedStereotype.getName());
		String stereotypeApplicationId = getId(modelElement) +"#"+ stereotype.getName();
		instanceSpec.initialize(stereotypeApplicationId, stereotype.getName(),false);
		workspace.putModelElement(instanceSpec);
		EObject application = modelElement.getStereotypeApplication(stereotype);
		Iterator<? extends INakedProperty> attributes = nakedStereotype.getEffectiveAttributes().iterator();
		while (attributes.hasNext()) {
			INakedProperty attribute = (INakedProperty) attributes.next();
			EStructuralFeature structuralFeature = application.eClass().getEStructuralFeature(attribute.getName());
			if (structuralFeature != null) {
				// might be an "artificial" feature introduced by NakedUml
				Object value = application.eGet(structuralFeature);
				INakedSlot slot = new NakedSlotImpl();
				slot.setDefiningFeature(attribute);
				slot.initialize(attribute.getId() +"#"+ stereotypeApplicationId, attribute.getName(),false);
				if (value instanceof EList) {
					Iterator<? extends EObject> iter = ((EList<? extends EObject>) value).iterator();
					int i = 0;
					while (iter.hasNext()) {
						putValue(i, iter.next(), slot);
						i++;
					}
				} else {
					putValue(0, value, slot);
				}
				slot.setOwnerElement(instanceSpec);
				instanceSpec.addOwnedElement(slot);
				workspace.putModelElement(slot);
			}
		}
		return instanceSpec;
	}

	private void putValue(int index, Object value, INakedSlot slot) {
		if (value != null) {
			NakedValueSpecificationImpl valueSpec = new NakedValueSpecificationImpl();
			if (value instanceof EObject) {
				EObject eObjectValue = (EObject) value;
				String valueId = getId(eObjectValue);
				INakedElement nakedElementValue = workspace.getModelElement(valueId);
				if (nakedElementValue == null) {
					if (value instanceof EEnumLiteralImpl) {
						if (slot.getDefiningFeature().getNakedBaseType() instanceof INakedEnumeration) {
							INakedEnumeration ne = (INakedEnumeration) slot.getDefiningFeature().getNakedBaseType();
							valueSpec.setValue(ne.lookupLiteral(((EEnumLiteral) value).getLiteral()));
							// Note that it is NOT an element reference. These enum literals
							// come from the profile, not
							// the model
						} else {
							valueSpec.setValue(((EEnumLiteral) value).getLiteral());
						}
					} else {
						// resolve later to the actual element in the model that this refers
						// to
						valueSpec.setValueId(valueId);
					}
				} else {
					valueSpec.setValue(nakedElementValue);
				}
			} else {
				valueSpec.setValue(value);
			}
			valueSpec.initialize(slot.getId() + index, slot.getName(),false);
			valueSpec.setOwnerElement(slot);
			slot.addOwnedElement(valueSpec);
			workspace.putModelElement(valueSpec);
		}
	}
}