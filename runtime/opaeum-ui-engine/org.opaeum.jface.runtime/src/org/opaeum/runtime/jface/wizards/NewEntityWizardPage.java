package org.opaeum.runtime.jface.wizards;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.jface.builder.ComponentTreeBuilder;
import org.opaeum.runtime.rwt.OpaeumRapSession;
import org.opaeum.uim.Page;
import org.opaeum.uim.component.UimComponent;
import org.opaeum.uim.panel.GridPanel;

public class NewEntityWizardPage extends WizardPage{
	private static final long serialVersionUID = 1231231321L;
	private Page opaeumPage;
	private OpaeumRapSession session;
	public NewEntityWizardPage(Page opaeumPage,DataBindingContext dataBindingContext,IPersistentObject selectedObject,OpaeumRapSession session){
		super(opaeumPage.getName());
		this.session = session;
		this.opaeumPage = opaeumPage;
		this.dataBindingContext = dataBindingContext;
		this.newObject = selectedObject;
	}
	private DataBindingContext dataBindingContext;
	private IPersistentObject newObject;
	public void createControl(Composite parent){
		Composite body = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		body.setLayout(gl);
		if(opaeumPage.getPanel() instanceof GridPanel){
			gl.numColumns = ((GridPanel) opaeumPage.getPanel()).getNumberOfColumns();
		}
		
		ComponentTreeBuilder builder = new ComponentTreeBuilder(newObject, newObject, session);
		List<UimComponent> children = opaeumPage.getPanel().getChildren();
		for(UimComponent child:children){
			builder.addComponent(body, child, dataBindingContext);
		}
		setControl(body);
		body.layout();
	}
}
