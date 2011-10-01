package org.opeum.uim.editparts;

import org.eclipse.draw2d.IFigure;
import org.opeum.uim.action.NavigationToEntity;
import org.opeum.uim.figures.NavigationToEntityFigure;
import org.topcased.modeler.di.model.GraphNode;

public class AbstractNavigationToEntityEditPart extends BoundEditPart{
	public AbstractNavigationToEntityEditPart(GraphNode obj){
		super(obj);
	}
	protected IFigure createFigure(){
		return new NavigationToEntityFigure();
	}
	public void refreshVisuals(){
		super.refreshVisuals();
		NavigationToEntityFigure ntef = (NavigationToEntityFigure) getFigure();
		NavigationToEntity nte = (NavigationToEntity) getEObject();
		String formName = nte.getToForm() == null ? "Select Form" : nte.getToForm().getName();
		ntef.getToFormFigure().setText(formName);
	}
}