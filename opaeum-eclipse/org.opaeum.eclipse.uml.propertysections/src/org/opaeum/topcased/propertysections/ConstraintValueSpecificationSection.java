package org.opaeum.topcased.propertysections;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLPackage;
import org.opaeum.topcased.propertysections.base.AbstractOpaqueExpressionSection;

public class ConstraintValueSpecificationSection extends AbstractOpaqueExpressionSection{
	@Override
	protected OpaqueExpression beforeOclChanged(String text){
		//Specification created by OpaeumElementLinker
		return (OpaqueExpression) ((Constraint)getEObject()).getSpecification();
	}
	@Override
	protected EReference getValueSpecificationFeature(){
		return UMLPackage.eINSTANCE.getConstraint_Specification();
	}
	@Override
	protected String getLabelText(){
		return "Boolean value";
	}
	@Override
	protected NamedElement getValueSpecificationOwner(){
		return (Constraint)getEObject();
	}
}
