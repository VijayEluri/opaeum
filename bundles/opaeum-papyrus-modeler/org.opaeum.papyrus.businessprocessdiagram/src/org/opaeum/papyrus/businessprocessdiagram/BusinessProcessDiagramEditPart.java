package org.opaeum.papyrus.businessprocessdiagram;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableLabelEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.gmfdiag.common.editpart.PapyrusDiagramEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.policies.ActivityDiagramItemSemanticEditPolicy;
import org.eclipse.papyrus.uml.diagram.activity.edit.policies.RemoveOrphanViewPolicy;
import org.eclipse.papyrus.uml.diagram.common.editpolicies.DuplicatePasteEditPolicy;
import org.eclipse.papyrus.uml.diagram.common.editpolicies.PapyrusCreationEditPolicy;

public class BusinessProcessDiagramEditPart extends PapyrusDiagramEditPart{
	public final static String MODEL_ID = "PapyrusUMLBusinessProcessDiagram"; //$NON-NLS-1$
	public static final int VISUAL_ID = 1000;
	public BusinessProcessDiagramEditPart(View view){
		super(view);
	}
	protected void createDefaultEditPolicies(){
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CREATION_ROLE, new PapyrusCreationEditPolicy());
		installEditPolicy(DuplicatePasteEditPolicy.PASTE_ROLE, new DuplicatePasteEditPolicy());
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ActivityDiagramItemSemanticEditPolicy());
		installEditPolicy("RemoveOrphanView", new RemoveOrphanViewPolicy()); //$NON-NLS-1$
	}
	/* package-local */static class NodeLabelDragPolicy extends NonResizableEditPolicy{
		@SuppressWarnings("rawtypes")
		protected List createSelectionHandles(){
			MoveHandle h = new MoveHandle((GraphicalEditPart) getHost());
			h.setBorder(null);
			return Collections.singletonList(h);
		}
		/**
		 * @generated
		 */
		public Command getCommand(Request request){
			return null;
		}
		/**
		 * @generated
		 */
		public boolean understandsRequest(Request request){
			return false;
		}
	}
	/**
	 * @generated
	 */
	/* package-local */static class LinkLabelDragPolicy extends NonResizableLabelEditPolicy{
		/**
		 * @generated
		 */
		@SuppressWarnings("rawtypes")
		protected List createSelectionHandles(){
			MoveHandle mh = new MoveHandle((GraphicalEditPart) getHost());
			mh.setBorder(null);
			return Collections.singletonList(mh);
		}
	}
}
