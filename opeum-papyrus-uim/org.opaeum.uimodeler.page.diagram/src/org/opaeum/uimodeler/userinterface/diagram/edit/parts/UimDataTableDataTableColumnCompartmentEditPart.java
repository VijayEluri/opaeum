package org.opaeum.uimodeler.userinterface.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.ShapeCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.common.editpolicies.DuplicatePasteEditPolicy;
import org.opaeum.uimodeler.userinterface.diagram.edit.policies.UimDataTableDataTableColumnCompartmentItemSemanticEditPolicy;
import org.opaeum.uimodeler.userinterface.diagram.part.Messages;

/**
 * @generated
 */
public class UimDataTableDataTableColumnCompartmentEditPart extends ShapeCompartmentEditPart{
	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 7002;
	/**
	 * @generated
	 */
	public UimDataTableDataTableColumnCompartmentEditPart(View view){
		super(view);
	}
	/**
	 * @generated
	 */
	public String getCompartmentName(){
		return Messages.UimDataTableDataTableColumnCompartmentEditPart_title;
	}
	/**
	 * @generated NOT
	 */
	public IFigure createFigure(){
		ResizableCompartmentFigure scf = new ShapeCompartmentFigure(getCompartmentName(), getMapMode()){
			{
				remove(getTextPane());
				remove(scrollPane);
				setLayoutManager(new StackLayout());
				add(scrollPane);
				setBorder(null);
			}
		};
		scf.getContentPane().setLayoutManager(getLayoutManager());
		ResizableCompartmentFigure result = (ResizableCompartmentFigure) scf;
		result.setTitleVisibility(false);
		return result;
	}
	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies(){
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new UimDataTableDataTableColumnCompartmentItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
		installEditPolicy(DuplicatePasteEditPolicy.PASTE_ROLE, new DuplicatePasteEditPolicy());
		//in Papyrus diagrams are not strongly synchronised
		//installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CANONICAL_ROLE, new org.opaeum.uimodeler.userinterface.diagram.edit.policies.UimDataTableDataTableColumnCompartmentCanonicalEditPolicy());
	}
	/**
	 * @generated
	 */
	protected void setRatio(Double ratio){
		if(getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout){
			super.setRatio(ratio);
		}
	}
	/**
	 * @generated
	 */
	protected void handleNotificationEvent(Notification notification){
		Object feature = notification.getFeature();
		if(NotationPackage.eINSTANCE.getSize_Width().equals(feature) || NotationPackage.eINSTANCE.getSize_Height().equals(feature)
				|| NotationPackage.eINSTANCE.getLocation_X().equals(feature) || NotationPackage.eINSTANCE.getLocation_Y().equals(feature)){
			refreshBounds();
		}
		super.handleNotificationEvent(notification);
	}
	/**
	 * @generated
	 */
	protected void refreshBounds(){
		int width = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getSize_Width())).intValue();
		int height = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getSize_Height())).intValue();
		Dimension size = new Dimension(width, height);
		int x = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getLocation_X())).intValue();
		int y = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getLocation_Y())).intValue();
		Point loc = new Point(x, y);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), new Rectangle(loc, size));
	}
	/**
	 * @generated
	 */
	protected void refreshVisuals(){
		super.refreshVisuals();
		refreshBounds();
	}
}
