package org.nakeduml.topcased.propertysections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.nakeduml.topcased.commands.SetOclExpressionCommand;

public class OperationBodyConditionSection extends AbstractOpaqueExpressionSection{
	@Override
	protected EStructuralFeature getFeature(){
		return UMLPackage.eINSTANCE.getOperation_BodyCondition();
	}
	@Override
	protected String getLabelText(){
		return "Query expression";
	}
	@Override
	protected OpaqueExpression getExpression(EObject e){
		Operation oper = (Operation) e;
		if(oper.getBodyCondition() == null){
			return null;
		}
		return (OpaqueExpression) oper.getBodyCondition().getSpecification();
	}
	@Override
	public void setInput(IWorkbenchPart part,ISelection selection){
		super.setInput(part, selection);
	}
	@Override
	public void refresh(){
		super.refresh();
		super.oclComposite.getTextControl().setEnabled(getOperation().isQuery());
	}
	@Override
	protected NamedElement getOwner(){
		return getOperation().getBodyCondition();
	}
	@Override
	protected ValueSpecification getValueSpecification(){
		return getExpression(getEObject());
	}
	private Operation getOperation(){
		return (Operation) getEObject();
	}
	protected void handleModelChanged(Notification msg){
		super.handleModelChanged(msg);
		Object notifier = msg.getNotifier();
		if(notifier.equals(getEObject())){
			switch(msg.getFeatureID(getEObject().getClass())){
			case UMLPackage.OPERATION__IS_QUERY:
				if(msg.getNewBooleanValue()){
					oclComposite.getTextControl().setEnabled(true && !getOperation().isAbstract());
					createBodyCondition();
				}else{
					oclComposite.getTextControl().setEnabled(false);
					removeBodyCondition();
				}
				break;
			case UMLPackage.OPERATION__IS_ABSTRACT:
				if(msg.getNewBooleanValue()){
					oclComposite.getTextControl().setEnabled(false);
					createBodyCondition();
				}else{
					oclComposite.getTextControl().setEnabled(true && getOperation().isQuery());
					removeBodyCondition();
				}
				break;
			}
		}
	}
	private void removeBodyCondition(){
		getEditingDomain().getCommandStack().execute(
				RemoveCommand.create(getEditingDomain(), getEObject(), UMLPackage.eINSTANCE.getNamespace_OwnedRule(), getOwner()));
		oclComposite.setValueElement(null);
	}
	private void createBodyCondition(){
		if(getOperation().getBodyCondition() == null){
			getEditingDomain().getCommandStack().execute(
					SetCommand.create(getEditingDomain(), getEObject(), UMLPackage.eINSTANCE.getOperation_BodyCondition(),
							UMLFactory.eINSTANCE.createConstraint()));
			oclComposite.setValueElement(getOwner());
		}
	}
	@Override
	protected EReference getValueSpecificationFeature(){
		return UMLPackage.eINSTANCE.getConstraint_Specification();
	}
}
