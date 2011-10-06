package org.opeum.uml2uim;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.StateMachine;
import org.opeum.eclipse.context.OpeumEclipseContext;

public class OpenFormAction extends AbstractUimGenerationAction implements IObjectActionDelegate{
	@Override
	protected void runActionRecursively(NamedElement eObject,IAction action){
		URI uri = getFileUri(eObject, getFileName(eObject, action));
		if(!getFile(uri).exists()){
			SynchronizeAction.doSynchronize(eObject);
		}
	}

	@Override
	public String getFileName(NamedElement namedElement, IAction action){
		// TODO Auto-generated method stub
		String suffix = "";
		if(namedElement instanceof StateMachine || namedElement instanceof Activity){
			suffix = "Editor";
		}else if(namedElement instanceof org.eclipse.uml2.uml.Class){
			if(action.getId().endsWith("Edit")){
				// Check action id;
				suffix = "Editor";
			}else{
				suffix = "Creator";
			}
		}else if(namedElement instanceof Operation){
			if(action.getId().endsWith("Invoke")){
				// Check action id;
				suffix = "Invoker";
			}else{
				suffix = "Task";
			}
		}
		return OpeumEclipseContext.getCurrentContext().getId(namedElement) + suffix;
	}
}