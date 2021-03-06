package org.opaeum.eclipse.uml.propertysections.base;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

public abstract class AbstractEnumerationPropertySection extends AbstractOpaeumPropertySection{
	private Group combo;
	@Override
	protected void createWidgets(Composite composite){
		combo = getWidgetFactory().createGroup(composite, "");
		if(getFeature() != null){
			boolean isChangeable = getFeature().isChangeable();
			combo.setEnabled(isChangeable);
		}
	}
	@Override
	protected void setSectionData(Composite composite){
		FormData data = new FormData();
		data.left = new FormAttachment(labelCombo);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom= new FormAttachment(100, 0);
		combo.setLayoutData(data);
		GridLayout gd = new GridLayout(20,false);
		combo.setLayout(gd);
		gd.horizontalSpacing=0;
		gd.verticalSpacing=0;
		gd.marginWidth=0;
		gd.marginHeight=0;
	}
	@Override
	public Control getPrimaryInput(){
		return combo;
	}
	@Override
	protected void hookListeners(){
	}
	protected void handleComboModified(String name){
		updateModel(getFeatureValue(name));
	}
	@Override
	public void populateControls(){
		for(Control control:combo.getChildren()){
			control.dispose();
		}
		String[] items = getEnumerationFeatureValues();
		for(final String string:items){
			Button createButton = getWidgetFactory().createButton(combo, string, SWT.RADIO);
			if(string.equals(getFeatureAsText(getFeatureOwner(getSelectedObject())))){
				createButton.setSelection(true);
			}
			createButton.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent event){
					handleComboModified(string);
				}
			});
			createButton.setLayoutData(new GridData(GridData.BEGINNING,GridData.BEGINNING,false,false));
		}
		combo.layout();
	}
	@Override
	protected void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		if(combo != null){
			combo.setEnabled(enabled);
		}
	}
	protected abstract String[] getEnumerationFeatureValues();
	protected abstract String getFeatureAsText(EObject featureOwner);
	protected abstract Object getFeatureValue(String name);
}