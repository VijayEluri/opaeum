package org.opaeum.uim.userinteractionproperties.sections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.opaeum.topcased.propertysections.base.AbstractMultiFeaturePropertySection;
import org.opaeum.uim.UimPackage;
import org.opaeum.uim.panel.Outlayable;
import org.opaeum.uim.panel.PanelPackage;

public class UserInteractionConstraintBooleanFeaturesSection extends AbstractMultiFeaturePropertySection{
	private Text preferredWidth;
	private Text preferredHeight;
	private Button fillHorizontally;
	private Button fillVertically;
	private Label label;
	private Label toLabel;
	protected void setSectionData(Composite composite){
		layout(null, label, 143);
		layout(label, preferredWidth, 30);
		layout(preferredWidth, toLabel, 10);
		layout(toLabel, preferredHeight, 30);
		layout(preferredHeight, fillHorizontally, 140);
		layout(fillHorizontally, fillVertically, 140);
	}
	@Override
	protected Outlayable getFeatureOwner(){
		return this.getProperty();
	}
	protected Outlayable getProperty(){
		return (Outlayable) getEObject();
	}
	@Override
	protected void createWidgets(Composite composite){
		super.createWidgets(composite);
		label = getWidgetFactory().createLabel(composite, "Preferred Width:");
		toLabel = getWidgetFactory().createLabel(composite, "Preferred Height");
		preferredWidth = getWidgetFactory().createText(composite, "", SWT.SINGLE);
		preferredHeight = getWidgetFactory().createText(composite, "", SWT.SINGLE);
		fillHorizontally = getWidgetFactory().createButton(composite, "Fill Horizontally", SWT.CHECK);
		fillVertically = getWidgetFactory().createButton(composite, "Fill Vertically", SWT.CHECK);
	}
	protected void hookListeners(){
		new LiteralIntegerTextChangeListener(PanelPackage.eINSTANCE.getOutlayable_PreferredWidth()).startListeningTo(preferredWidth);
		new LiteralIntegerTextChangeListener(PanelPackage.eINSTANCE.getOutlayable_PreferredHeight()).startListeningTo(preferredHeight);
		fillHorizontally.addSelectionListener(new BooleanSelectionListener(PanelPackage.eINSTANCE.getOutlayable_FillHorizontally()));
		fillVertically.addSelectionListener(new BooleanSelectionListener(PanelPackage.eINSTANCE.getOutlayable_FillVertically()));
	}
	@Override
	protected String getLabelText(){
		return "";
	}
	@Override
	public void refresh(){
		preferredWidth.setText(getProperty().getPreferredWidth()+ "");
		preferredHeight.setText(getProperty().getPreferredHeight()+ "");
		fillHorizontally.setSelection(getProperty().getFillHorizontally());
		fillVertically.setSelection(getProperty().getFillVertically());
	}
}
