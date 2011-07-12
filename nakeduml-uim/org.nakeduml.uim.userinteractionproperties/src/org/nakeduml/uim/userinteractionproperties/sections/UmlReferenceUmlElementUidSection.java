package org.nakeduml.uim.userinteractionproperties.sections;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.TypedElement;
import org.nakeduml.uim.UimPackage;
import org.nakeduml.uim.binding.PropertyRef;
import org.nakeduml.uim.util.UmlUimLinks;
import org.topcased.tabbedproperties.sections.AbstractChooserPropertySection;

/**
 * A section display a text field to edit/see String features
 *
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 *
 * @generated NOT
 */
public class UmlReferenceUmlElementUidSection extends AbstractChooserPropertySection{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
	 * @generated
	 */
	protected String getLabelText(){
		return "UmlElementUid:";
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
	 * @generated
	 */
	protected EStructuralFeature getFeature(){
		return UimPackage.eINSTANCE.getUmlReference_UmlElementUid();
	}
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.topcased.tabbedproperties.sections.AbstractChooserPropertySection#getComboFeatureValues()
	 * @generated NOT
	 */
	protected Object[] getComboFeatureValues(){
		PropertyRef pr = (PropertyRef) getEObject();
		if(pr.getBinding() != null && UmlUimLinks.getInstance(pr).getTypedElement(pr.getBinding()) != null){
			TypedElement typedElement = UmlUimLinks.getInstance(pr).getTypedElement(pr.getBinding());
			Classifier classifier = (Classifier) typedElement.getType();
			EList<Property> attrs = classifier.getAllAttributes();
			return (Property[]) attrs.toArray(new Property[attrs.size()]);
		}else if(pr.getPrevious() != null && UmlUimLinks.getInstance(pr).getProperty(pr.getPrevious()) != null){
			TypedElement typedElement = UmlUimLinks.getInstance(pr).getProperty(pr.getPrevious());
			Classifier classifier = (Classifier) typedElement.getType();
			EList<Property> attrs = classifier.getAllAttributes();
			return (Property[]) attrs.toArray(new Property[attrs.size()]);
		}
		return new Property[0];
	}
	@Override
	protected Object getFeatureValue(){
		// TODO Auto-generated method stub
		return null;
	}

}