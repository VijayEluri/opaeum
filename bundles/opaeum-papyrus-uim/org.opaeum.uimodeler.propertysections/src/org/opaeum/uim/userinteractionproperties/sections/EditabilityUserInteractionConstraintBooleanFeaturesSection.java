package org.opaeum.uim.userinteractionproperties.sections;

import org.eclipse.emf.ecore.EReference;
import org.opaeum.uim.constraint.ConstraintPackage;
import org.opaeum.uim.userinteractionproperties.common.AbstractUserInteractionConstraintBooleanFeaturesSection;

public class EditabilityUserInteractionConstraintBooleanFeaturesSection extends AbstractUserInteractionConstraintBooleanFeaturesSection{
	@Override
	protected EReference getConstraintContainingFeature(){
		EReference v = ConstraintPackage.eINSTANCE.getEditableConstrainedObject_Editability();
		return v;
	}

}
