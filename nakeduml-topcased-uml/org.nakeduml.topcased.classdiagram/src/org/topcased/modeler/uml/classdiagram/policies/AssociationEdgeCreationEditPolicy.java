/*******************************************************************************
 * Copyright (c) 2006 AIRBUS FRANCE. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.topcased.modeler.uml.classdiagram.policies;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.uml2.uml.Association;
import org.topcased.modeler.commands.CreateTypedEdgeCommand;
import org.topcased.modeler.di.model.GraphEdge;
import org.topcased.modeler.di.model.GraphElement;
import org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy;
import org.topcased.modeler.uml.alldiagram.commands.ReconnectAssociationEdgeCommand;
import org.topcased.modeler.uml.classdiagram.commands.AssociationEdgeCreationCommand;
import org.topcased.modeler.uml.classdiagram.edit.AssociationEditPart;
import org.topcased.modeler.utils.SourceTargetData;
import org.topcased.modeler.utils.Utils;

/**
 * Association edge creation <br>
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class AssociationEdgeCreationEditPolicy extends AbstractEdgeCreationEditPolicy
{
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy#createCommand(org.eclipse.gef.EditDomain,
     *      org.topcased.modeler.di.model.GraphEdge, org.topcased.modeler.di.model.GraphElement)
     * @generated
     */
    protected CreateTypedEdgeCommand createCommand(EditDomain domain, GraphEdge edge, GraphElement source)
    {
        return new AssociationEdgeCreationCommand(domain, edge, source);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy#checkEdge(org.topcased.modeler.di.model.GraphEdge)
     * @generated
     */
    protected boolean checkEdge(GraphEdge edge)
    {
        return Utils.getElement(edge) instanceof Association;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy#checkSource(org.topcased.modeler.di.model.GraphElement)
     * @generated
     */
    protected boolean checkSource(GraphElement source)
    {
        EObject object = Utils.getElement(source);
        if (object instanceof org.eclipse.uml2.uml.Type)
        {
            return true;
        }
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy#checkTargetForSource(org.topcased.modeler.di.model.GraphElement,
     *      org.topcased.modeler.di.model.GraphElement)
     * @generated
     */
    protected boolean checkTargetForSource(GraphElement source, GraphElement target)
    {
        EObject sourceObject = Utils.getElement(source);
        EObject targetObject = Utils.getElement(target);

        if (sourceObject instanceof org.eclipse.uml2.uml.Type && targetObject instanceof org.eclipse.uml2.uml.Type)
        {
            return true;
        }
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy#checkCommand(org.eclipse.gef.commands.Command)
     * @generated
     */
    protected boolean checkCommand(Command command)
    {
        return command instanceof AssociationEdgeCreationCommand;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy#getSourceTargetData(org.topcased.modeler.di.model.GraphElement,
     *      org.topcased.modeler.di.model.GraphElement)
     * @generated
     */
    protected SourceTargetData getSourceTargetData(GraphElement source, GraphElement target)
    {
        EObject sourceObject = Utils.getElement(source);
        EObject targetObject = Utils.getElement(target);

        if (sourceObject instanceof org.eclipse.uml2.uml.Type && targetObject instanceof org.eclipse.uml2.uml.Type)
        {
            return new SourceTargetData(false, true, SourceTargetData.DIAGRAM, "org.eclipse.uml2.uml.Package", "packagedElement", null, null, null, null, null, null);
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
     * @generated NOT
     */
    protected Command getReconnectSourceCommand(ReconnectRequest request)
    {
        ConnectionEditPart connection = request.getConnectionEditPart();
        if (connection instanceof AssociationEditPart)
        {
            final AssociationEditPart edit = (AssociationEditPart) connection;

            final GraphElement newSource = (GraphElement) getHost().getModel();

            if (checkSource(newSource))
            {
                final GraphElement graphElt = (GraphElement) edit.getSource().getModel();
                ReconnectAssociationEdgeCommand command = new ReconnectAssociationEdgeCommand(edit, ReconnectAssociationEdgeCommand.SOURCE)
                {
                    protected GraphElement getOldGraphElement(ConnectionEditPart conn)
                    {
                        return graphElt;
                    }
                };
                command.setNewElement(newSource);
                return command;
            }
        }

        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.modeler.edit.policies.AbstractEdgeCreationEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
     * 
     * @generated NOT
     */
    protected Command getReconnectTargetCommand(ReconnectRequest request)
    {
        ConnectionEditPart connection = request.getConnectionEditPart();
        if (connection instanceof AssociationEditPart)
        {
            final AssociationEditPart edit = (AssociationEditPart) connection;

            final GraphElement targetNode = (GraphElement) getHost().getModel();
            final GraphElement sourceNode = (GraphElement) edit.getSource().getModel();

            if (checkTargetForSource(sourceNode, targetNode))
            {
                final GraphElement graphElt = (GraphElement) edit.getTarget().getModel();
                ReconnectAssociationEdgeCommand command = new ReconnectAssociationEdgeCommand(edit, ReconnectAssociationEdgeCommand.TARGET)
                {
                    protected GraphElement getOldGraphElement(ConnectionEditPart conn)
                    {
                        return graphElt;
                    }
                };
                command.setNewElement(targetNode);
                return command;
            }
        }

        return null;
    }
}