/*******************************************************************************
 * No CopyrightText Defined in the configurator file.
 ******************************************************************************/
package org.opaeum.uim.modeleditor.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.opaeum.eclipse.context.OpaeumEclipseContext;
import org.opaeum.uim.modeleditor.UimPlugin;
import org.opaeum.uim.util.UmlUimLinks;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.topcased.modeler.commands.GEFtoEMFCommandStackWrapper;
import org.topcased.modeler.documentation.EAnnotationDocPage;
import org.topcased.modeler.documentation.IDocPage;
import org.topcased.modeler.editor.Modeler;

/**
 * Generated Model editor
 * 
 * @generated
 */
public class UimEditor extends Modeler{
	public static UmlUimLinks getCurrentUmlLinks(){
		return currentUmlLinks;
	}
	private static UmlUimLinks currentUmlLinks;
	@Override
	protected void setInput(IEditorInput input){
		super.setInput(input);
		if(OpaeumEclipseContext.getCurrentContext() != null){
			currentUmlLinks=new UmlUimLinks(OpaeumEclipseContext.getCurrentContext().getCurrentEmfWorkspace());
		}
	}
	@Override
	protected EObject openFile(IFile file,boolean b){
		EObject openFile = super.openFile(file, b);
		return openFile;
	}
	public static final String EDITOR_ID = "org.opaeum.uim.modeleditor.editor.UimEditor";
	/**
	 * @see org.topcased.modeler.editor.Modeler#getAdapterFactories()
	 * @generated
	 */
	@SuppressWarnings({
			"unchecked","rawtypes"
	})
	protected List getAdapterFactories(){
		List factories = new ArrayList();
		factories.add(new org.opaeum.uim.provider.UimItemProviderAdapterFactory());
		factories.add(new org.opaeum.uim.modeleditor.providers.UimModelerProviderAdapterFactory());
		factories.add(new org.opaeum.uim.layout.provider.LayoutItemProviderAdapterFactory());
		factories.add(new org.opaeum.uim.modeleditor.providers.LayoutModelerProviderAdapterFactory());
		factories.add(new org.opaeum.uim.control.provider.ControlItemProviderAdapterFactory());
		factories.add(new org.opaeum.uim.modeleditor.providers.ControlModelerProviderAdapterFactory());
		factories.add(new org.opaeum.uim.folder.provider.FolderItemProviderAdapterFactory());
		factories.add(new org.opaeum.uim.modeleditor.providers.FolderModelerProviderAdapterFactory());
		factories.add(new org.opaeum.uim.form.provider.FormItemProviderAdapterFactory());
		factories.add(new org.opaeum.uim.modeleditor.providers.FormModelerProviderAdapterFactory());
		factories.add(new org.opaeum.uim.binding.provider.BindingItemProviderAdapterFactory());
		factories.add(new org.opaeum.uim.modeleditor.providers.BindingModelerProviderAdapterFactory());
		factories.add(new org.opaeum.uim.security.provider.SecurityItemProviderAdapterFactory());
		factories.add(new org.opaeum.uim.modeleditor.providers.SecurityModelerProviderAdapterFactory());
		factories.add(new org.opaeum.uim.action.provider.ActionItemProviderAdapterFactory());
		factories.add(new org.opaeum.uim.modeleditor.providers.ActionModelerProviderAdapterFactory());
		factories.addAll(super.getAdapterFactories());
		return factories;
	}
	/**
	 * @see org.topcased.modeler.editor.Modeler#getId()
	 * @generated NOT
	 */
	public String getId(){
		return "org.opaeum.uim.modeleditor.editor.UimEditor";
	}
	/**
	 * @see org.topcased.modeler.editor.Modeler#getAdapter(java.lang.Class)
	 * @generated
	 */
	@SuppressWarnings("deprecation")
	public Object getAdapter(Class type){
		if(type == IDocPage.class){
			GEFtoEMFCommandStackWrapper stack = new GEFtoEMFCommandStackWrapper(getCommandStack());
			return new EAnnotationDocPage(stack);
		}
		return super.getAdapter(type);
	}
	/**
	 * @see org.topcased.modeler.editor.Modeler#getPreferenceStore()
	 * 
	 * @generated
	 */
	public IPreferenceStore getPreferenceStore(){
		IProject project = (((IFileEditorInput) getEditorInput()).getFile()).getProject();
		Preferences root = Platform.getPreferencesService().getRootNode();
		try{
			if(root.node(ProjectScope.SCOPE).node(project.getName()).nodeExists(UimPlugin.getId())){
				return new ScopedPreferenceStore(new ProjectScope(project), UimPlugin.getId());
			}
		}catch(BackingStoreException e){
			e.printStackTrace();
		}
		return UimPlugin.getDefault().getPreferenceStore();
	}
}
