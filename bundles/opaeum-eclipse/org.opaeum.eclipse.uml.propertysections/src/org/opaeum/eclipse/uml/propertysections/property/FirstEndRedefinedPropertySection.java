package org.opaeum.eclipse.uml.propertysections.property;

import org.eclipse.emf.ecore.EObject;
import org.opaeum.eclipse.EmfAssociationUtil;

public class FirstEndRedefinedPropertySection extends PropertyRedefinedPropertySection{
	@Override
	protected EObject getFeatureOwner(EObject e){
		return EmfAssociationUtil.getFirstEnd(e);
	}

}
