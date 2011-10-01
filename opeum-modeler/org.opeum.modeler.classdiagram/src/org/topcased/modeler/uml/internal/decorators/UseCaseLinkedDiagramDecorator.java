/*****************************************************************************
 * Copyright (c) 2008 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin) vincent.hemery@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/

package org.topcased.modeler.uml.internal.decorators;

import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.UseCase;
import org.topcased.modeler.diagrams.model.util.DiagramsUtils;
import org.topcased.modeler.editor.ModelerGraphicalViewer;
import org.topcased.modeler.extensions.AbstractLinkedDiagramDecorator;
import org.topcased.modeler.uml.usecasediagram.edit.UseCaseEditPart;

/**
 * This class decorates the Call Behavior Actions with an arrow ticks if there is some available linked diagrams.
 * 
 */
public class UseCaseLinkedDiagramDecorator extends AbstractLinkedDiagramDecorator
{

    /**
     * Instantiates a new use case linked diagram decorator.
     * 
     * @param pTarget the target
     */
    public UseCaseLinkedDiagramDecorator(IDecoratorTarget pTarget)
    {
        super(pTarget);
    }

    /**
     * Method to determine if the decoratorTarget is a supported type for this decorator and return the associated
     * Classifier element.
     * 
     * @param pDecoratorTarget IDecoratorTarget to check and return valid Classifier target.
     * 
     * @return node Node if IDecoratorTarget can be supported, null otherwise.
     */
    public static UseCase getUseCaseDecoratorTarget(IDecoratorTarget pDecoratorTarget)
    {
        return (UseCase) pDecoratorTarget.getAdapter(UseCase.class);
    }

    /**
     * Checks for diagram.
     * 
     * @param pTarget the target
     * 
     * @return true, if checks for diagram
     * 
     * @see org.topcased.modeler.extensions.AbstractLinkedDiagramDecorator#hasDiagram(org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget)
     */
    protected boolean hasDiagram(IDecoratorTarget pTarget)
    {

        UseCase lModelObject = getUseCaseDecoratorTarget(pTarget);
        UseCaseEditPart lEp = (UseCaseEditPart) pTarget.getAdapter(UseCaseEditPart.class);
        if (lModelObject != null && lEp != null)
        {
            Behavior lUseCaseClassifierBehavior = lModelObject.getClassifierBehavior();
            if (lUseCaseClassifierBehavior != null)
            {
                if (lEp.getViewer() instanceof ModelerGraphicalViewer)
                {
                    return !(DiagramsUtils.findAllExistingDiagram(((ModelerGraphicalViewer) lEp.getViewer()).getModelerEditor().getDiagrams(), lUseCaseClassifierBehavior).isEmpty());
                }
            }
        }
        return false;
    }
}