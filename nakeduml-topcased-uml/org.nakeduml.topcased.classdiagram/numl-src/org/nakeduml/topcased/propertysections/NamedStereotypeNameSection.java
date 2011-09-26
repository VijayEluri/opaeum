package org.nakeduml.topcased.propertysections;

import net.sf.nakeduml.emf.extraction.StereotypesHelper;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.UMLPackage;
import org.topcased.tabbedproperties.sections.AbstractStringPropertySection;

public class NamedStereotypeNameSection extends AbstractStringPropertySection
{
    @Override
	protected void handleTextModified(){
		// TOPCASED expects all features to have a realizing runtime type, and dynamic classes don't have that
    	getEObject().eSet(getFeature(), getText().getText());
	}

	protected String getLabelText()
    {
        return "Name:";
    }

    protected EStructuralFeature getFeature()
    {
        return StereotypesHelper.findAttribute(getEObject(), "name");
    }
}