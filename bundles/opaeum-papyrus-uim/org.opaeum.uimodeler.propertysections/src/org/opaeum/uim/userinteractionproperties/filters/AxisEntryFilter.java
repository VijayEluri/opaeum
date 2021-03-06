package org.opaeum.uim.userinteractionproperties.filters;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.uml2.uml.Element;
import org.opaeum.eclipse.EmfElementFinder;
import org.opaeum.uim.cube.AxisEntry;

public class AxisEntryFilter implements IFilter{
	public boolean select(Object toTest){
		if(!(toTest instanceof Element)){
			EObject o = EmfElementFinder.adaptObject(toTest);
			return o instanceof AxisEntry;
		}
		return false;
	}
}
