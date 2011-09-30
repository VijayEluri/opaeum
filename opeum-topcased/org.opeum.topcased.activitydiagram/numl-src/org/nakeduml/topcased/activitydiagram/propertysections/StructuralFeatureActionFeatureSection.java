package org.opeum.topcased.activitydiagram.propertysections;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.uml2.common.edit.provider.IItemQualifiedTextProvider;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.StructuralFeatureAction;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.topcased.tabbedproperties.sections.AbstractChooserPropertySection;

public class StructuralFeatureActionFeatureSection extends AbstractChooserPropertySection{
	protected EStructuralFeature getFeature(){
		return UMLPackage.eINSTANCE.getStructuralFeatureAction_StructuralFeature();
	}
	protected String getLabelText(){
		return "Feature :";
	}
	protected Object[] getComboFeatureValues(){
		List<Object> result = new ArrayList<Object>();
		result.add("");
		
		for(Resource r:getEditingDomain().getResourceSet().getResources()){
			if(!r.getURI().toString().contains("UML.metamodel.uml") && r.getURI().fileExtension().equals("uml")){
				TreeIterator<EObject> allContents = r.getAllContents();
				while(allContents.hasNext()){
					EObject eObject = (EObject) allContents.next();
					if(eObject instanceof StructuralFeature){
						result.add(eObject);
					}
				}
			}
		}
		return result.toArray();
	}
	protected ILabelProvider getLabelProvider(){
		return new AdapterFactoryLabelProvider(new UMLItemProviderAdapterFactory());
	}
	protected ILabelProvider getAdvancedLabeProvider(){
		return new AdapterFactoryLabelProvider(new UMLItemProviderAdapterFactory()){
			public String getText(Object object){
				IItemQualifiedTextProvider itemQualifiedTextProvider = (IItemQualifiedTextProvider) adapterFactory.adapt(object, IItemQualifiedTextProvider.class);
				return itemQualifiedTextProvider != null ? itemQualifiedTextProvider.getQualifiedText(object) : super.getText(object);
			}
		};
	}
	protected Object getFeatureValue(){
		return ((StructuralFeatureAction) getEObject()).getStructuralFeature();
	}
}
