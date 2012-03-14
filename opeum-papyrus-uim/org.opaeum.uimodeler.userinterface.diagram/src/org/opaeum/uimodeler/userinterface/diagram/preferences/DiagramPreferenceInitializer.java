package org.opaeum.uimodeler.userinterface.diagram.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.opaeum.uimodeler.userinterface.diagram.part.UimDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramPreferenceInitializer extends AbstractPreferenceInitializer{
	/**
	 * @generated
	 */
	public void initializeDefaultPreferences(){
		IPreferenceStore store = getPreferenceStore();
		LinkToQueryPreferencePage.initDefaults(store);
		GridPanelPreferencePage.initDefaults(store);
		BuiltInActionButtonPreferencePage.initDefaults(store);
		UimDataTablePreferencePage.initDefaults(store);
		OperationButtonPreferencePage.initDefaults(store);
		UimFieldPreferencePage.initDefaults(store);
		BuiltInLinkPreferencePage.initDefaults(store);
		VerticalPanelPreferencePage.initDefaults(store);
		TransitionButtonPreferencePage.initDefaults(store);
		HorizontalPanelPreferencePage.initDefaults(store);
	}
	/**
	 * @generated
	 */
	protected IPreferenceStore getPreferenceStore(){
		return UimDiagramEditorPlugin.getInstance().getPreferenceStore();
	}
}
