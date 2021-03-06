package org.opaeum.eclipse.uml.propertysections.property;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;
import org.opaeum.eclipse.EmfElementFinder;
import org.opaeum.eclipse.uml.propertysections.base.AbstractMultiFeaturePropertySection;
import org.opaeum.eclipse.uml.propertysections.subsections.BooleanSubsection;

public class PropertyBooleanFeaturesSection extends AbstractMultiFeaturePropertySection{
	private BooleanSubsection isReadOnly;
	private BooleanSubsection isStatic;
	private BooleanSubsection isDerivedUnion;
	private BooleanSubsection isDerived;
	private BooleanSubsection isComposition;
	public PropertyBooleanFeaturesSection(){
		isReadOnly = createBoolean(UMLPackage.eINSTANCE.getStructuralFeature_IsReadOnly(), "Is Read Only", 140);
		isStatic = createBoolean(UMLPackage.eINSTANCE.getFeature_IsStatic(), "Is Static", 140);
		isDerivedUnion = createBoolean(UMLPackage.eINSTANCE.getProperty_IsDerivedUnion(), "Is Derived Union", 140);
		isDerived = createBoolean(UMLPackage.eINSTANCE.getProperty_IsDerived(), "Is Derived", 140);
	}

	@Override
	public void populateControls(){
		super.populateControls();
	}
	private boolean isInProfile(){
		if(getEObject()!=null &&  getFeatureOwner(getEObject())!=null){
			return EmfElementFinder.getRootObject((Element) getFeatureOwner(getEObject())) instanceof Profile;
		}else{
			return false;
		}
	}

	@Override
	public String getLabelText(){
		return "Config";
	}
}