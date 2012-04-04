package org.opaeum.rap.runtime.internal.wizards;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.opaeum.rap.runtime.OpaeumRapSession;
import org.opaeum.rap.runtime.internal.editors.ComponentTreeBuilder;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.event.IEventHandler;
import org.opaeum.uim.Page;
import org.opaeum.uim.UimComponent;
import org.opaeum.uim.panel.GridPanel;

public class OperationInvocationWizardPage extends WizardPage{
	private static final long serialVersionUID = 1231231321L;
	private Page opaeumPage;
	private OpaeumRapSession session;
	public OperationInvocationWizardPage(Page opaeumPage,DataBindingContext dataBindingContext,IPersistentObject selectedObject,
			IEventHandler objectBeingEdited,OpaeumRapSession session){
		super(opaeumPage.getName());
		this.session = session;
		this.opaeumPage = opaeumPage;
		this.dataBindingContext = dataBindingContext;
		this.selectedObject = selectedObject;
		this.objectBeingEdited = objectBeingEdited;
	}
	private DataBindingContext dataBindingContext;
	private IPersistentObject selectedObject;
	private IEventHandler objectBeingEdited;
	public void createControl(Composite parent){
		Composite body = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		body.setLayout(gl);
		if(opaeumPage.getPanel() instanceof GridPanel){
			gl.numColumns = ((GridPanel) opaeumPage.getPanel()).getNumberOfColumns();
		}
		
		ComponentTreeBuilder builder = new ComponentTreeBuilder(selectedObject, objectBeingEdited, session);
		EList<UimComponent> children = opaeumPage.getPanel().getChildren();
		for(UimComponent child:children){
			builder.addComponent(body, child, dataBindingContext);
		}
		setControl(body);
		body.layout();
	}
}
