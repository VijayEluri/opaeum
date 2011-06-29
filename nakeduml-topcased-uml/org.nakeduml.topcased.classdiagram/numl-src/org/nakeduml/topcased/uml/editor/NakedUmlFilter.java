package org.nakeduml.topcased.uml.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.UMLPackage;
import org.topcased.modeler.editor.outline.AdditionalResources;

public class NakedUmlFilter extends ViewerFilter{
	public static Set<EClassifier> ALLOWED_ELEMENTS = new HashSet<EClassifier>();
	static{
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getClass_());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getActivity());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getStateMachine());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getPackage());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getComponent());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getProperty());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getOperation());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getParameter());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getState());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getAction());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getPin());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getPort());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getRegion());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getActivityPartition());
		ALLOWED_ELEMENTS.add(UMLPackage.eINSTANCE.getClass_());
	}
	public boolean select(Viewer viewer,Object parentElement,Object element){
		if(element instanceof AdditionalResources){
			return true;
		}
		if(element instanceof EObject){
			return hasElements(element);
		}
		return false;
	}
	private boolean hasElements(Object element){
		if(isNode(element)){
			return true;
		}
		return false;
	}
	private boolean isNode(Object element){
		if(element instanceof EAnnotation){
			return false;
		}else if(element instanceof Element){
			return isAllowedElement(element);
		}else{
			return true;
		}
	}
	public static boolean isAllowedElement(Object element){
		for(EClassifier e:ALLOWED_ELEMENTS){
			if(e.isInstance(element)){
				return true;
			}
		}
		return false;
	}
}