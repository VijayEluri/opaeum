package org.opaeum.uim.userinteractionproperties.uml;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.uml2.uml.Element;
import org.opaeum.uim.constraint.ConstraintFactory;
import org.opaeum.uim.constraint.UserInteractionConstraint;
import org.opaeum.uim.perspective.MultiplicityElementNavigationConstraint;
import org.opaeum.uim.perspective.PerspectiveFactory;
import org.opaeum.uim.perspective.PerspectivePackage;
import org.opaeum.uim.userinteractionproperties.common.AbstractRequiredRolesSection;
import org.opaeum.uimodeler.common.IUIElementMapMap;

public class SecurityOnAddRequiredRolesSection extends AbstractRequiredRolesSection{
	@Override
	protected EReference getConstraintContainingFeature(){
		return PerspectivePackage.eINSTANCE.getMultiplicityElementNavigationConstraint_AddConstraint();
	}
	
	@Override
	public EObject getFeatureOwner(EObject e){
		MultiplicityElementNavigationConstraint c = getConstraintContainer(e);
		return c.getAddConstraint();
	}
	protected UserInteractionConstraint createConstraint(){
		return PerspectiveFactory.eINSTANCE.createNavigationConstraint();
	}

	public MultiplicityElementNavigationConstraint getConstraintContainer(EObject e){
		IUIElementMapMap explorerMap = (IUIElementMapMap)e.eResource().getResourceSet();
		MultiplicityElementNavigationConstraint c = (MultiplicityElementNavigationConstraint) explorerMap.getElementFor((Element)e);
		return c;
	}
}
