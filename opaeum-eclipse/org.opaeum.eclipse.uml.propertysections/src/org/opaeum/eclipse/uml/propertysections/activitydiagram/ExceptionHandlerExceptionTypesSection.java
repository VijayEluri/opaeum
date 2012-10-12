package org.opaeum.eclipse.uml.propertysections.activitydiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.ExceptionHandler;
import org.eclipse.uml2.uml.UMLPackage;
import org.opaeum.eclipse.context.OpaeumEclipseContext;
import org.opaeum.eclipse.uml.propertysections.base.AbstractReferenceLookupSection;



public class ExceptionHandlerExceptionTypesSection extends AbstractReferenceLookupSection{
	protected EStructuralFeature getFeature(){
		return UMLPackage.eINSTANCE.getExceptionHandler_ExceptionType();
	}
	public String getLabelText(){
		return "Exceptions:";
	}
	protected Object getFeatureValue(){
		return getExceptionHandler().getExceptionTypes();
	}
	@Override
	protected List<? extends EObject> getAvailableChoices(){
		List<EObject> choices = new ArrayList<EObject>();
		Collection<EObject> types = OpaeumEclipseContext.getReachableObjectsOfType(getEObject(), UMLPackage.eINSTANCE.getClassifier());
		choices.addAll(types);
		return choices;
	}
	private ExceptionHandler getExceptionHandler(){
		return (ExceptionHandler) getEObject();
	}
	@Override
	protected EObject getFeatureOwner(EObject e){
		return e;
	}
}
