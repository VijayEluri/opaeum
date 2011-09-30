package org.opeum.topcased.propertysections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.uml2.common.edit.provider.IItemQualifiedTextProvider;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.opeum.eclipse.EmfElementFinder;
import org.opeum.topcased.uml.editor.OpeumItemProviderAdapterFactory;
import org.opeum.topcased.uml.editor.OpeumQualifiedNameLabelProvider;
import org.topcased.tabbedproperties.sections.AbstractChooserPropertySection;
import org.topcased.tabbedproperties.utils.ITypeCacheAdapter;
import org.topcased.tabbedproperties.utils.TypeCacheAdapter;

public class TypedElementTypeSection extends AbstractChooserPropertySection{
	protected EStructuralFeature getFeature(){
		return UMLPackage.eINSTANCE.getTypedElement_Type();
	}
	protected String getLabelText(){
		return "Type:";
	}
	protected Object[] getComboFeatureValues(){
		List<Object> choices = new ArrayList<Object>();
		choices.add("");
		ITypeCacheAdapter typeCacheAdapter = TypeCacheAdapter.getExistingTypeCacheAdapter(getEObject());
		Collection<EObject> types = typeCacheAdapter.getReachableObjectsOfType(getEObject(), UMLPackage.eINSTANCE.getType());
		if(((TypedElement) getEObject()).getModel() != null){
			choices.addAll(UmlMetaTypeRemover.removeAll(types));
		}else{
			choices.addAll(types);

		}
		return choices.toArray();
	}
	protected ILabelProvider getLabelProvider(){
		return new AdapterFactoryLabelProvider(new UMLItemProviderAdapterFactory());
	}
	protected ILabelProvider getAdvancedLabeProvider(){
		return new OpeumQualifiedNameLabelProvider(new OpeumItemProviderAdapterFactory());
	}
	protected Object getFeatureValue(){
		return ((TypedElement) getEObject()).getType();
	}
}