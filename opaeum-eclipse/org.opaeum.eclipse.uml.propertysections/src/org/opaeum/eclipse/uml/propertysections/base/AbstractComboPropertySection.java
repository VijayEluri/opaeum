package org.opaeum.eclipse.uml.propertysections.base;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.opaeum.topcased.uml.editor.OpaeumItemProviderAdapterFactory;

public abstract class AbstractComboPropertySection extends AbstractOpaeumPropertySection{
	protected ComboViewer combo;
	private List<? extends EObject> comboValues;
	protected abstract List<? extends EObject> getComboValues();
	protected abstract Object getOldFeatureValue();
	@Override
	public Control getPrimaryInput(){
		return combo.getCombo();
	}
	@Override
	protected void createWidgets(Composite composite){
		combo = new ComboViewer(new Combo(composite, SWT.FLAT | SWT.READ_ONLY | SWT.BORDER));
		if(getFeature() != null){
			boolean isChangeable = getFeature().isChangeable();
			combo.getCombo().setEnabled(isChangeable);
		}
		combo.setContentProvider(new ArrayContentProvider());
		combo.setLabelProvider(getLabelProvider());
	}
	@Override
	protected void setSectionData(Composite composite){
		FormData data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(composite, new String[]{getLabelText()}));
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		combo.getCombo().setLayoutData(data);
	}
	@Override
	protected void hookListeners(){
		combo.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event){
				handleComboModified();
			}
		});
	}
	protected ILabelProvider getLabelProvider(){
		return new AdapterFactoryLabelProvider(new OpaeumItemProviderAdapterFactory());
	}
	protected void handleComboModified(){
		createCommand(getOldFeatureValue(), getSelectedItem());
	}
	protected Object getSelectedItem(){
		return ((IStructuredSelection) combo.getSelection()).getFirstElement();
	}
	@Override
	public void refresh(){
		super.refresh();
		ILabelProvider lp = getLabelProvider();
		for(EObject eObject:comboValues){
			String text = lp.getText(eObject);
			combo.add(text);
		}
		combo.setSelection(new StructuredSelection(getOldFeatureValue()));
	}
	@Override
	protected void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		if(combo != null){
			combo.getCombo().setEnabled(enabled);
		}
	}
	protected Combo getCombo(){
		return combo.getCombo();
	}
	public void setInput(IWorkbenchPart part,ISelection selection){
		super.setInput(part, selection);
		this.comboValues = getComboValues();
	}
}