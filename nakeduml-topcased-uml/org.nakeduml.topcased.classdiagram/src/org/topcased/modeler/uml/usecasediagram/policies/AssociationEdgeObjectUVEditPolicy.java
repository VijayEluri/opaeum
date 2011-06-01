/*******************************************************************************
 * Copyright (c) 2005 AIRBUS FRANCE. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   David Sciamma (Anyware Technologies), Mathieu Garcia (Anyware Technologies),
 *   Jacques Lescot (Anyware Technologies), Thomas Friol (Anyware Technologies),
 *   Nicolas Lalev�e (Anyware Technologies) - initial API and implementation 
 ******************************************************************************/

package org.topcased.modeler.uml.usecasediagram.policies;

import org.topcased.modeler.edit.policies.EdgeObjectUVEditPolicy;
import org.topcased.modeler.figures.IEdgeObjectFigure;
import org.topcased.modeler.uml.usecasediagram.figures.AssociationFigure;

/**
 * An edit policy to select and move the name and the cardinality label relative to the EReference connection .<br>
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class AssociationEdgeObjectUVEditPolicy extends EdgeObjectUVEditPolicy
{
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.EdgeObjectUVEditPolicy#isEnd(org.topcased.modeler.figures.IEdgeObjectFigure)
     * @generated
     */
    protected boolean isEnd(IEdgeObjectFigure figure)
    {
        AssociationFigure associationFigure = (AssociationFigure) getHostFigure();

        if (figure == associationFigure.getSrcCountEdgeObjectFigure())
        {
            return false;
        }

        if (figure == associationFigure.getTargetCountEdgeObjectFigure())
        {
            return true;
        }

        return true;
    }

}
