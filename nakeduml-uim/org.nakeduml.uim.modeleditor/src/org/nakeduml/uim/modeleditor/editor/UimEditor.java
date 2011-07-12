/*******************************************************************************
 * No CopyrightText Defined in the configurator file.
 ******************************************************************************/
package org.nakeduml.uim.modeleditor.editor;

import java.util.ArrayList;
import java.util.List;

import net.sf.nakeduml.emf.workspace.UmlElementMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.nakeduml.topcased.uml.editor.NakedUmlEditor;
import org.nakeduml.uim.modeleditor.UimPlugin;
import org.nakeduml.uim.util.UmlUimLinks;
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
	@Override
	protected EObject openFile(IFile file,boolean b){
		EObject openFile = super.openFile(file, b);
		IWorkbenchPage activePage = UimPlugin.getActivePage();
		if(activePage != null){
			for(IEditorReference r:activePage.getEditorReferences()){
				IEditorPart editor = r.getEditor(false);
				if(editor instanceof NakedUmlEditor){
					UmlElementMap umlElementMap = ((NakedUmlEditor) editor).getUmlElementMap();
					UmlUimLinks.associate(getResourceSet(),umlElementMap);
				}
			}
		}
		return openFile;
	}
	public static final String EDITOR_ID = "org.nakeduml.uim.modeleditor.editor.UimEditor";
	/**
	 * @see org.topcased.modeler.editor.Modeler#getAdapterFactories()
	 * @generated
	 */
	protected List getAdapterFactories(){
		List factories = new ArrayList();
		factories.add(new org.nakeduml.uim.provider.UimItemProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.modeleditor.providers.UimModelerProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.layout.provider.LayoutItemProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.modeleditor.providers.LayoutModelerProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.control.provider.ControlItemProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.modeleditor.providers.ControlModelerProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.folder.provider.FolderItemProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.modeleditor.providers.FolderModelerProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.form.provider.FormItemProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.modeleditor.providers.FormModelerProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.binding.provider.BindingItemProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.modeleditor.providers.BindingModelerProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.security.provider.SecurityItemProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.modeleditor.providers.SecurityModelerProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.action.provider.ActionItemProviderAdapterFactory());
		factories.add(new org.nakeduml.uim.modeleditor.providers.ActionModelerProviderAdapterFactory());
		factories.addAll(super.getAdapterFactories());
		return factories;
	}
	/**
	 * @see org.topcased.modeler.editor.Modeler#getId()
	 * @generated NOT
	 */
	public String getId(){
		return super.getContributorId();
	}
	/**
	 * @see org.topcased.modeler.editor.Modeler#getAdapter(java.lang.Class)
	 * @generated
	 */
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
