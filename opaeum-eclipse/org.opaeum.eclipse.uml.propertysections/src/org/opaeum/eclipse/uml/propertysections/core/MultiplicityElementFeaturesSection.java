package org.opaeum.eclipse.uml.propertysections.core;

import org.eclipse.uml2.uml.UMLPackage;
import org.opaeum.eclipse.uml.propertysections.base.AbstractMultiFeaturePropertySection;
import org.opaeum.eclipse.uml.propertysections.base.BooleanSubSection;
import org.opaeum.eclipse.uml.propertysections.base.LiteralIntegerSubsection;

public class MultiplicityElementFeaturesSection extends AbstractMultiFeaturePropertySection{
	private LiteralIntegerSubsection from;
	private LiteralIntegerSubsection to;
	private BooleanSubSection isUnique;
	private BooleanSubSection isOrdered;
	public MultiplicityElementFeaturesSection(){
		from = createLiteralInteger(UMLPackage.eINSTANCE.getMultiplicityElement_LowerValue(), "Number of values:", 143, 40);
		from.setDefaultValue(0);
		to = createLiteralInteger(UMLPackage.eINSTANCE.getMultiplicityElement_UpperValue(), "to", 10, 40);
		to.setDefaultValue(1);
		isUnique = createBoolean(UMLPackage.eINSTANCE.getMultiplicityElement_IsUnique(), "Every value is unique", 140);
		isOrdered = createBoolean(UMLPackage.eINSTANCE.getMultiplicityElement_IsOrdered(), "Values are ordered", 140);
	}
}
