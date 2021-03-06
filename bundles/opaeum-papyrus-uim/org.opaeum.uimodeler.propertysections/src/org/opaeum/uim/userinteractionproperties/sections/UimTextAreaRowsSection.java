package org.opaeum.uim.userinteractionproperties.sections;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.opaeum.eclipse.uml.propertysections.base.AbstractIntegerPropertySection;
import org.opaeum.uim.control.ControlPackage;

/**
 * A section display a text field to edit/see Integer Features
 *
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class UimTextAreaRowsSection extends AbstractIntegerPropertySection{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.topcased.tabbedproperties.sections.AbstractOpaeumPropertySection#getLabelText()
	 * @generated
	 */
	public String getLabelText(){
		return "Rows:";
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.topcased.tabbedproperties.sections.AbstractOpaeumPropertySection#getFeature()
	 * @generated
	 */
	protected EStructuralFeature getFeature(){
		return ControlPackage.eINSTANCE.getUimTextArea_Rows();
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.topcased.tabbedproperties.sections.AbstractIntegerPropertySection#getFeatureInteger()
	 * @generated
	 */
	protected Integer getFeatureInteger(){
		Object Int = getEObject().eGet(getFeature());
		if(Int == null){
			return new Integer(0);
		}
		return (Integer) Int;
	}
}