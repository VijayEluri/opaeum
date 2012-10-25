package org.opaeum.topcased.activitydiagram.bpm.edit;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.uml2.uml.NamedElement;
import org.opaeum.topcased.activitydiagram.OpaeumObjectFlowEdgeCreationEditPolicy;
import org.opaeum.topcased.uml.editor.OpaeumEditor;
import org.topcased.draw2d.figures.ComposedLabel;
import org.topcased.modeler.di.model.GraphNode;
import org.topcased.modeler.uml.activitydiagram.ActivityEditPolicyConstants;
import org.topcased.modeler.uml.activitydiagram.edit.OpaqueActionEditPart;

public class SimpleTaskEditPart extends OpaqueActionEditPart{
	public SimpleTaskEditPart(GraphNode obj){
		super(obj);
	}
	protected void createEditPolicies(){
		super.createEditPolicies();
		installEditPolicy(ActivityEditPolicyConstants.OBJECTFLOW_EDITPOLICY, new OpaeumObjectFlowEdgeCreationEditPolicy());
	}

	@Override
	public void performRequest(Request request){
		if(request.getType() == RequestConstants.REQ_OPEN){
			NamedElement e = (NamedElement) getEObject();
			String uuid = OpaeumEditor.getCurrentContext().getId(e);
//			URI uri = AbstractUimGenerationAction.getFileUri(e, uuid);
//			if(!AbstractUimGenerationAction.getFile(uri).exists()){
//				SynchronizeAction.doSynchronize(e);
//			}
//			AbstractUimGenerationAction.openEditor(e, uuid);
		}else{
			super.performRequest(request);
		}
	}
	protected void refreshHeaderLabel(){
		super.refreshHeaderLabel();
		ComposedLabel composedLabel = (ComposedLabel) getLabel();
		composedLabel.setPrefix("<<simpleTask>>");
	}
}