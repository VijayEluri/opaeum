package org.opaeum.eclipse.uml.propertysections.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

public class FirstEndDefaultValueSection extends PropertyDefaultValueSection{
	@Override
	protected EObject getFeatureOwner(EObject e){
		if(e == null){
			return null;
		}else{
			return ((Association) e).getMemberEnds().get(0);
		}
	}
}