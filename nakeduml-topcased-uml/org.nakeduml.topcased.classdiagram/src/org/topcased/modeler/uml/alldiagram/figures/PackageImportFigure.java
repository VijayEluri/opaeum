/*******************************************************************************
 * Copyright (c) 2006 AIRBUS FRANCE. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.topcased.modeler.uml.alldiagram.figures;

import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.swt.SWT;
import org.topcased.modeler.edit.locators.EdgeObjectOffsetLocator;
import org.topcased.modeler.figures.EdgeObjectOffsetEditableLabel;
import org.topcased.modeler.figures.IEdgeObjectFigure;
import org.topcased.modeler.figures.IEdgeObjectOffsetFigure;
import org.topcased.modeler.figures.IGraphEdgeFigure;

/**
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class PackageImportFigure extends PolylineConnectionEx implements IGraphEdgeFigure, HandleBounds
{
    /**
     * @generated
     */
    private static final int LINE_ON = 8;

    /**
     * @generated
     */
    private static final int LINE_OFF = 7;

    /**
     * @generated
     */
    private IEdgeObjectFigure visibilityEdgeObject;

    /**
     * @generated
     */
    private Locator visibilityLocator;

    /**
     * The constructor <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public PackageImportFigure()
    {
        super();

        visibilityEdgeObject = new EdgeObjectOffsetEditableLabel(this);
        visibilityLocator = new EdgeObjectOffsetLocator((IEdgeObjectOffsetFigure) visibilityEdgeObject);
        add(visibilityEdgeObject, visibilityLocator);

        setLineStyle(SWT.LINE_CUSTOM);
        setLineDash(new int[] {LINE_ON, LINE_OFF});
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the object figure
     * @generated
     */
    public IEdgeObjectFigure getVisibilityEdgeObjectFigure()
    {
        return visibilityEdgeObject;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.figures.IGraphEdgeFigure#getLocator(org.topcased.modeler.figures.IEdgeObjectFigure)
     * @generated
     */
    public Locator getLocator(IEdgeObjectFigure edgeObjectfigure)
    {
        if (edgeObjectfigure == visibilityEdgeObject)
        {
            return visibilityLocator;
        }

        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
     * @generated
     */
    public Rectangle getHandleBounds()
    {
        return getPoints().getBounds();
    }
}