package org.opaeum.uimodeler.userinterface.diagram.edit.policies;

import java.util.Iterator;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.MoveElementsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.extendedtypes.types.IExtendedHintedElementType;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;
import org.opaeum.uimodeler.userinterface.diagram.edit.helpers.UimBaseEditHelper;
import org.opaeum.uimodeler.userinterface.diagram.part.UimVisualIDRegistry;
import org.opaeum.uimodeler.userinterface.diagram.providers.UimElementTypes;

/**
 * @generated
 */
public class UimBaseItemSemanticEditPolicy extends SemanticEditPolicy{
	/**
	 * Extended request data key to hold editpart visual id.
	 * @generated
	 */
	public static final String VISUAL_ID_KEY = "visual_id"; //$NON-NLS-1$
	/**
	 * Extended request data key to hold the edge view during a reconnect request.
	 * @generated
	 */
	public static final String GRAPHICAL_RECONNECTED_EDGE = "graphical_edge"; //$NON-NLS-1$
	/**
	 * @generated
	 */
	private final IElementType myElementType;
	/**
	 * @generated
	 */
	protected UimBaseItemSemanticEditPolicy(IElementType elementType){
		myElementType = elementType;
	}
	/**
	 * Extended request data key to hold editpart visual id.
	 * Add visual id of edited editpart to extended data of the request
	 * so command switch can decide what kind of diagram element is being edited.
	 * It is done in those cases when it's not possible to deduce diagram
	 * element kind from domain element.
	 * Add the reoriented view to the request extended data so that the view
	 *  currently edited can be distinguished from other views of the same element
	 *  and these latter possibly removed if they become inconsistent after reconnect
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Command getCommand(Request request){
		if(request instanceof ReconnectRequest){
			Object view = ((ReconnectRequest) request).getConnectionEditPart().getModel();
			if(view instanceof View){
				Integer id = new Integer(UimVisualIDRegistry.getVisualID((View) view));
				request.getExtendedData().put(VISUAL_ID_KEY, id);
				request.getExtendedData().put(GRAPHICAL_RECONNECTED_EDGE, (View) view);
			}
		}
		return super.getCommand(request);
	}
	/**
	 * Returns visual id from request parameters.
	 * @generated
	 */
	protected int getVisualID(IEditCommandRequest request){
		Object id = request.getParameter(VISUAL_ID_KEY);
		return id instanceof Integer ? ((Integer) id).intValue() : -1;
	}
	/**
	 * @generated
	 */
	protected Command getSemanticCommand(IEditCommandRequest request){
		IEditCommandRequest completedRequest = completeRequest(request);
		Command semanticCommand = getSemanticCommandSwitch(completedRequest);
		semanticCommand = getEditHelperCommand(completedRequest, semanticCommand);
		if(completedRequest instanceof DestroyRequest){
			DestroyRequest destroyRequest = (DestroyRequest) completedRequest;
			return shouldProceed(destroyRequest) ? addDeleteViewCommand(semanticCommand, destroyRequest) : null;
		}
		return semanticCommand;
	}
	/**
	 * @generated
	 */
	protected Command addDeleteViewCommand(Command mainCommand,DestroyRequest completedRequest){
		Command deleteViewCommand = getGEFWrapper(new DeleteCommand(getEditingDomain(), (View) getHost().getModel()));
		return mainCommand == null ? deleteViewCommand : mainCommand.chain(deleteViewCommand);
	}
	/**
	 * @generated
	 */
	private Command getEditHelperCommand(IEditCommandRequest request,Command editPolicyCommand){
		if(editPolicyCommand != null){
			ICommand command = editPolicyCommand instanceof ICommandProxy ? ((ICommandProxy) editPolicyCommand).getICommand() : new CommandProxy(
					editPolicyCommand);
			request.setParameter(UimBaseEditHelper.EDIT_POLICY_COMMAND, command);
		}
		IElementType requestContextElementType = getContextElementType(request);
		request.setParameter(UimBaseEditHelper.CONTEXT_ELEMENT_TYPE, requestContextElementType);
		ICommand command = requestContextElementType.getEditCommand(request);
		request.setParameter(UimBaseEditHelper.EDIT_POLICY_COMMAND, null);
		request.setParameter(UimBaseEditHelper.CONTEXT_ELEMENT_TYPE, null);
		if(command != null){
			if(!(command instanceof CompositeTransactionalCommand)){
				command = new CompositeTransactionalCommand(getEditingDomain(), command.getLabel()).compose(command);
			}
			return new ICommandProxy(command);
		}
		return editPolicyCommand;
	}
	/**
	 * @generated
	 */
	protected IElementType getContextElementType(IEditCommandRequest request){
		IElementType requestContextElementType = UimElementTypes.getElementType(getVisualID(request));
		return requestContextElementType != null ? requestContextElementType : myElementType;
	}
	/**
	 * @generated
	 */
	protected Command getSemanticCommandSwitch(IEditCommandRequest req){
		if(req instanceof CreateRelationshipRequest){
			return getCreateRelationshipCommand((CreateRelationshipRequest) req);
		}else if(req instanceof CreateElementRequest){
			return getCreateCommand((CreateElementRequest) req);
		}else if(req instanceof ConfigureRequest){
			return getConfigureCommand((ConfigureRequest) req);
		}else if(req instanceof DestroyElementRequest){
			return getDestroyElementCommand((DestroyElementRequest) req);
		}else if(req instanceof DestroyReferenceRequest){
			return getDestroyReferenceCommand((DestroyReferenceRequest) req);
		}else if(req instanceof DuplicateElementsRequest){
			return getDuplicateCommand((DuplicateElementsRequest) req);
		}else if(req instanceof GetEditContextRequest){
			return getEditContextCommand((GetEditContextRequest) req);
		}else if(req instanceof MoveRequest){
			return getMoveCommand((MoveRequest) req);
		}else if(req instanceof ReorientReferenceRelationshipRequest){
			return getReorientReferenceRelationshipCommand((ReorientReferenceRelationshipRequest) req);
		}else if(req instanceof ReorientRelationshipRequest){
			return getReorientRelationshipCommand((ReorientRelationshipRequest) req);
		}else if(req instanceof SetRequest){
			return getSetCommand((SetRequest) req);
		}
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getConfigureCommand(ConfigureRequest req){
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req){
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req){
		// check if the type is an extended type, and then create directly the element...
		IElementType type = req.getElementType();
		if(type instanceof IExtendedHintedElementType){
			IElementEditService provider = ElementEditServiceUtils.getCommandProvider(req.getContainer());
			if(provider == null){
				return UnexecutableCommand.INSTANCE;
			}
			// Retrieve create command from the Element Edit service
			ICommand createGMFCommand = provider.getEditCommand(req);
			return getGEFWrapper(createGMFCommand);
		}
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getSetCommand(SetRequest req){
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getEditContextCommand(GetEditContextRequest req){
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req){
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getDestroyReferenceCommand(DestroyReferenceRequest req){
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getDuplicateCommand(DuplicateElementsRequest req){
		return null;
	}
	/**
	 * @generated
	 */
	protected Command getMoveCommand(MoveRequest req){
		return getGEFWrapper(new MoveElementsCommand(req));
	}
	/**
	 * @generated
	 */
	protected Command getReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest req){
		return UnexecutableCommand.INSTANCE;
	}
	/**
	 * @generated
	 */
	protected Command getReorientRelationshipCommand(ReorientRelationshipRequest req){
		return UnexecutableCommand.INSTANCE;
	}
	/**
	 * @generated
	 */
	protected final Command getGEFWrapper(ICommand cmd){
		return new ICommandProxy(cmd);
	}
	/**
	 * Returns editing domain from the host edit part.
	 * @generated
	 */
	protected TransactionalEditingDomain getEditingDomain(){
		return ((IGraphicalEditPart) getHost()).getEditingDomain();
	}
	/**
	 * Clean all shortcuts to the host element from the same diagram
	 * @generated
	 */
	protected void addDestroyShortcutsCommand(ICompositeCommand cmd,View view){
		assert view.getEAnnotation("Shortcut") == null; //$NON-NLS-1$
		for(Iterator it = view.getDiagram().getChildren().iterator();it.hasNext();){
			View nextView = (View) it.next();
			if(nextView.getEAnnotation("Shortcut") == null || !nextView.isSetElement() || nextView.getElement() != view.getElement()){ //$NON-NLS-1$
				continue;
			}
			cmd.add(new DeleteCommand(getEditingDomain(), nextView));
		}
	}
}
