package org.opaeum.eclipse.uml.propertysections.activitydiagram;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.uml2.uml.Action;
import org.opaeum.eclipse.uml.propertysections.base.AbstractOpaeumPropertySection;

public class DisplayPinsSection extends AbstractOpaeumPropertySection{
	private Button button;
	public Action getAction(){
		return (Action) getEObject();
	}
	protected void createWidgets(Composite composite){
		button = getWidgetFactory().createButton(composite, "Display Pins", SWT.PUSH);
		button.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e){
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e){
			}
		});
	}
	@Override
	protected void setSectionData(Composite composite){
		FormData fData = new FormData();
		fData.top = new FormAttachment(0);
		fData.left = new FormAttachment(0);
		fData.right = new FormAttachment(100);
		button.setLayoutData(fData);
	}
	@Override
	protected EStructuralFeature getFeature(){
		return null;
	}
	@Override
	public String getLabelText(){
		return "Display Pins";
	}
	@Override
	public Control getPrimaryInput(){
		return button;
	}
}
