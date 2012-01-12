package org.opaeum.topcased.propertysections.property;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.opaeum.topcased.propertysections.AssociationEndNavigabilityAndCompositionSection;

public class SecondEndNavigabilityAndCompositionSection extends AssociationEndNavigabilityAndCompositionSection{

	@Override
	protected Property getProperty(){
		Association ass=(Association) getEObject();
		return ass.getMemberEnds().get(1);
	}
}