/*******************************************************************************
 * Copyright (c) 2006, 2008 AIRBUS FRANCE. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.topcased.modeler.uml.classdiagram.edit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.topcased.draw2d.figures.ComposedLabel;
import org.topcased.modeler.ModelerEditPolicyConstants;
import org.topcased.modeler.di.model.GraphElement;
import org.topcased.modeler.di.model.GraphNode;
import org.topcased.modeler.edit.policies.LabelDirectEditPolicy;
import org.topcased.modeler.edit.policies.ResizableEditPolicy;
import org.topcased.modeler.edit.policies.RestoreEditPolicy;
import org.topcased.modeler.requests.RestoreConnectionsRequest;
import org.topcased.modeler.uml.UMLLabel;
import org.topcased.modeler.uml.UMLTools;
import org.topcased.modeler.uml.classdiagram.ClassElementsVisibilityConstants;
import org.topcased.modeler.uml.classdiagram.annotation.UseElementsVisibility;
import org.topcased.modeler.uml.classdiagram.commands.DataTypeRestoreConnectionCommand;
import org.topcased.modeler.uml.classdiagram.figures.DataTypeFigure;
import org.topcased.modeler.uml.classdiagram.policies.DataTypeLayoutEditPolicy;
import org.topcased.modeler.uml.classdiagram.preferences.ClassDiagramPreferenceConstants;
import org.topcased.modeler.utils.Utils;

/**
 * The DataType object controller <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@UseElementsVisibility(preferencePrefix = ClassElementsVisibilityConstants.DATATYPE_PREFERENCE_PREFIX)
public class DataTypeEditPart extends ClassifierEditPart
{
    /**
     * Constructor <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param obj the graph node
     * @generated
     */
    public DataTypeEditPart(GraphNode obj)
    {
        super(obj);
    }

    /**
     * Creates edit policies and associates these with roles <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected void createEditPolicies()
    {
        super.createEditPolicies();

        installEditPolicy(ModelerEditPolicyConstants.RESTORE_EDITPOLICY, new RestoreEditPolicy()
        {
            @Override
            protected Command getRestoreConnectionsCommand(RestoreConnectionsRequest request)
            {
                return new DataTypeRestoreConnectionCommand(getHost());
            }
        });

        ResizableEditPolicy resizableEditPolicy = new ResizableEditPolicy();
        resizableEditPolicy.setResizeDirections(PositionConstants.EAST_WEST);
        installEditPolicy(ModelerEditPolicyConstants.RESIZABLE_EDITPOLICY, resizableEditPolicy);

        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DataTypeLayoutEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new LabelDirectEditPolicy());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     * @generated
     */
    @Override
    protected IFigure createFigure()
    {

        return new DataTypeFigure();
    }

    /**
     * Set the name of the datatype, with the stereotype and the "from"
     * 
     * @see org.topcased.modeler.edit.EMFGraphNodeEditPart#refreshHeaderLabel()
     */
    @Override
    protected void refreshHeaderLabel()
    {
        DataTypeFigure fig = (DataTypeFigure) getFigure();
        ComposedLabel lbl = (ComposedLabel) fig.getLabel();
        DataType dataType = (DataType) Utils.getElement(getGraphNode());

        String stereotype = UMLLabel.getStereotypesNotation(dataType, getPreferenceStore());
        if (stereotype.length() != 0)
        {
            stereotype += " ";
        }
        lbl.setPrefix(stereotype + "<<dataType>>");

        if (dataType.getName() != null)
        {
            lbl.setMain(dataType.getName());
        }

        lbl.setSuffix(UMLTools.getFromPackageNotation(dataType, (Element) Utils.getElement((GraphElement) getParent().getModel())));
    }

    /**
     * @see org.topcased.modeler.edit.GraphNodeEditPart#refreshTextAndFont()
     */
    @Override
    protected void refreshTextAndFont()
    {
        super.refreshTextAndFont();

        // The name of a DataType is displayed using a bold Font. When the DataType is abstract, it is Italic too.
        if (((Classifier) getEObject()).isAbstract())
        {
            ((ComposedLabel) getLabel()).getMain().setFont(getStyledFont(SWT.BOLD | SWT.ITALIC));
        }
        else
        {
            ((ComposedLabel) getLabel()).getMain().setFont(getStyledFont(SWT.BOLD));
        }
    }

    /**
     * @see org.topcased.modeler.edit.GraphNodeEditPart#getPreferenceDefaultBackgroundColor()
     * 
     * @generated
     */
    @Override
    protected Color getPreferenceDefaultBackgroundColor()
    {
        String backgroundColor = getPreferenceStore().getString(ClassDiagramPreferenceConstants.DATATYPE_DEFAULT_BACKGROUND_COLOR);
        if (backgroundColor.length() != 0)
        {
            return Utils.getColor(backgroundColor);
        }
        return null;
    }

    /**
     * @see org.topcased.modeler.edit.GraphNodeEditPart#getPreferenceDefaultForegroundColor()
     * 
     * @generated
     */
    @Override
    protected Color getPreferenceDefaultForegroundColor()
    {
        String foregroundColor = getPreferenceStore().getString(ClassDiagramPreferenceConstants.DATATYPE_DEFAULT_FOREGROUND_COLOR);
        if (foregroundColor.length() != 0)
        {
            return Utils.getColor(foregroundColor);
        }
        return null;
    }

    /**
     * @see org.topcased.modeler.edit.GraphNodeEditPart#getPreferenceDefaultFont()
     * 
     * @generated
     */
    @Override
    protected Font getPreferenceDefaultFont()
    {
        String preferenceFont = getPreferenceStore().getString(ClassDiagramPreferenceConstants.DATATYPE_DEFAULT_FONT);
        if (preferenceFont.length() != 0)
        {
            return Utils.getFont(new FontData(preferenceFont));
        }
        return null;

    }

}