package org.nakeduml.uim.userinteractionproperties.sections;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.TypedElement;
import org.nakeduml.uim.PropertyRef;
import org.nakeduml.uim.UIMPackage;
import org.nakeduml.uim.provider.UIMItemProviderAdapterFactory;
import org.topcased.tabbedproperties.AbstractTabbedPropertySheetPage;
import org.topcased.tabbedproperties.providers.TabbedPropertiesLabelProvider;
import org.topcased.tabbedproperties.sections.AbstractChooserPropertySection;

/**
 * A section featuring a combo box with a seach button. This section<br>
 * displays single references.
 * 
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated NOT
 */
public class PropertyRefPropertySection extends AbstractChooserPropertySection {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
	 * @generated
	 */
	protected String getLabelText() {
		return "Property:";
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
	 * @generated
	 */
	protected EStructuralFeature getFeature() {
		return UIMPackage.eINSTANCE.getPropertyRef_Property();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.topcased.tabbedproperties.sections.AbstractChooserPropertySection#getFeatureValue()
	 * @generated
	 */
	protected Object getFeatureValue() {
		return ((PropertyRef) getEObject()).getProperty();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.topcased.tabbedproperties.sections.AbstractChooserPropertySection#getComboFeatureValues()
	 * @generated NOT
	 */
	protected Object[] getComboFeatureValues() {
		PropertyRef pr = (PropertyRef) getEObject();
		if (pr.getBinding() != null && pr.getBinding().getElement() != null) {
			TypedElement typedElement = pr.getBinding().getElement();
			Classifier classifier = (Classifier) typedElement.getType();
			EList<Property> attrs = classifier.getAllAttributes();
			return (Property[]) attrs.toArray(new Property[attrs.size()]);
		} else if (pr.getPrevious() != null
				&& pr.getPrevious().getProperty() != null) {
			TypedElement typedElement = pr.getPrevious().getProperty();
			Classifier classifier = (Classifier) typedElement.getType();
			EList<Property> attrs = classifier.getAllAttributes();
			return (Property[]) attrs.toArray(new Property[attrs.size()]);
		}
		return new Property[0];
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.topcased.tabbedproperties.sections.AbstractChooserPropertySection#getLabelProvider()
	 * @generated
	 */
	protected ILabelProvider getLabelProvider() {
		List f = new ArrayList();
		f.add(new UIMItemProviderAdapterFactory());
		f
				.addAll(AbstractTabbedPropertySheetPage
						.getPrincipalAdapterFactories());
		return new TabbedPropertiesLabelProvider(new ComposedAdapterFactory(f));
	}

}