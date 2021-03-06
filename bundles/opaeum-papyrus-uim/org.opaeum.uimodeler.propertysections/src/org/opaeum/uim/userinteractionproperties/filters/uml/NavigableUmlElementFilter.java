package org.opaeum.uim.userinteractionproperties.filters.uml;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.opaeum.eclipse.EmfClassifierUtil;
import org.opaeum.eclipse.uml.filters.core.AbstractEObjectFilter;
import org.opaeum.uimodeler.common.IUIElementMapMap;

public class NavigableUmlElementFilter extends AbstractEObjectFilter<Element>{
	@Override
	public boolean select(Element e){
		boolean isMapped = e.eResource().getResourceSet() instanceof IUIElementMapMap && ((IUIElementMapMap)e.eResource().getResourceSet()).getElementFor(e)!=null;
		if(isMapped){
			if(e instanceof Classifier){
				return EmfClassifierUtil.isPersistentComplexStructure((Type) e);
			}else if(e instanceof TypedElement){
				return EmfClassifierUtil.isPersistentComplexStructure(((TypedElement) e).getType());
			}
		}
		return isMapped;
	}
}
