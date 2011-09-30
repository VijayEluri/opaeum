/*******************************************************************************
 * Copyright (c) 2006 AIRBUS FRANCE. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.topcased.modeler.uml.classdiagram.figures;

import org.eclipse.draw2d.PositionConstants;
import org.topcased.draw2d.figures.ComposedLabel;
import org.topcased.draw2d.figures.EditableLabel;
import org.topcased.draw2d.figures.ILabel;
import org.topcased.draw2d.figures.Label;

/**
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ClassFigure extends org.topcased.draw2d.figures.ClassFigure
{
    /**
     * Constructor <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ClassFigure()
    {
        super();
    }

    /**
     * @see org.topcased.draw2d.figures.ClassFigure#createLabel()
     */
    protected ILabel createLabel()
    {
        EditableLabel nameLbl = new EditableLabel(true);
        nameLbl.setTextJustification(PositionConstants.CENTER);
        return new ComposedLabel(new Label(), nameLbl, new Label(), false);
    }

}