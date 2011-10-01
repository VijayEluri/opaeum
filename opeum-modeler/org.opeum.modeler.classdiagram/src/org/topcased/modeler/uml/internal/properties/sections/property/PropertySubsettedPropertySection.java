package org.topcased.modeler.uml.internal.properties.sections.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.topcased.tabbedproperties.AbstractTabbedPropertySheetPage;
import org.topcased.tabbedproperties.providers.TabbedPropertiesLabelProvider;
import org.topcased.tabbedproperties.sections.AbstractReferencePropertySection;

/**
 * A section used to change the subsettedProperty property of a Property element.
 * 
 * Creation 16 févr. 07
 * 
 * @author <a href="mailto:jacques.lescot@anyware-tech.com">Jacques LESCOT</a>
 */
public class PropertySubsettedPropertySection extends AbstractReferencePropertySection
{
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
     * @generated
     */
    protected String getLabelText()
    {
        return "Subsetted Properties:";
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
     * @generated
     */
    protected EStructuralFeature getFeature()
    {
        return UMLPackage.eINSTANCE.getProperty_SubsettedProperty();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractListPropertySection#getListValues()
     * @generated
     */
    protected Object getListValues()
    {
        return ((Property) getEObject()).getSubsettedProperties();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractListPropertySection#getLabelProvider()
     * @generated
     */
    protected IBaseLabelProvider getLabelProvider()
    {
        if (getFeature() instanceof EReference)
        {
            List<AdapterFactory> f = new ArrayList<AdapterFactory>();
            f.add(new UMLItemProviderAdapterFactory());
            f.addAll(AbstractTabbedPropertySheetPage.getPrincipalAdapterFactories());
            return new TabbedPropertiesLabelProvider(new ComposedAdapterFactory(f));
        }
        return null;
    }

    /**
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
     * @custom
     */
    public boolean shouldUseExtraSpace()
    {
        return true;
    }
}