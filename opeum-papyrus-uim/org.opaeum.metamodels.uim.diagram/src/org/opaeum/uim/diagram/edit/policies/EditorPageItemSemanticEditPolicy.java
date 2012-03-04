package org.opaeum.uim.diagram.edit.policies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.uml.diagram.common.commands.DuplicateNamedElementCommand;
import org.opaeum.uim.diagram.edit.commands.GridPanelCreateCommand;
import org.opaeum.uim.diagram.edit.commands.HorizontalPanelCreateCommand;
import org.opaeum.uim.diagram.edit.commands.VerticalPanelCreateCommand;
import org.opaeum.uim.diagram.providers.UimElementTypes;

/**
 * @generated
 */
public class EditorPageItemSemanticEditPolicy extends UimBaseItemSemanticEditPolicy{
	/**
	 * @generated
	 */
	public EditorPageItemSemanticEditPolicy(){
		super(UimElementTypes.EditorPage_1000);
	}
	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req){
		if(UimElementTypes.GridPanel_2001 == req.getElementType()){
			return getGEFWrapper(new GridPanelCreateCommand(req));
		}
		if(UimElementTypes.HorizontalPanel_2002 == req.getElementType()){
			return getGEFWrapper(new HorizontalPanelCreateCommand(req));
		}
		if(UimElementTypes.VerticalPanel_2003 == req.getElementType()){
			return getGEFWrapper(new VerticalPanelCreateCommand(req));
		}
		return super.getCreateCommand(req);
	}
	/**
	 * @generated
	 */
	protected Command getDuplicateCommand(DuplicateElementsRequest req){
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		Diagram currentDiagram = null;
		if(getHost() instanceof IGraphicalEditPart){
			currentDiagram = ((IGraphicalEditPart) getHost()).getNotationView().getDiagram();
		}
		return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req, currentDiagram));
	}
	/**
	 * @generated
	 */
	private static class DuplicateAnythingCommand extends DuplicateNamedElementCommand{
		/**
		 * @generated
		 */
		private Diagram diagram;
		/**
		 * @generated
		 */
		public DuplicateAnythingCommand(TransactionalEditingDomain editingDomain,DuplicateElementsRequest req,Diagram currentDiagram){
			super(editingDomain, req.getLabel(), req.getElementsToBeDuplicated(), req.getAllDuplicatedElementsMap(), currentDiagram);
			this.diagram = currentDiagram;
		}
	}
}
