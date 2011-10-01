package org.opeum.topcased.uml.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.opeum.feature.OpeumConfig;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.opeum.eclipse.OpeumConfigDialog;
import org.opeum.eclipse.OpeumEclipsePlugin;
import org.opeum.eclipse.context.OpeumEclipseContext;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.editor.outline.ModelNavigator;
import org.topcased.modeler.preferences.ModelerPreferenceConstants;
import org.topcased.modeler.uml.editor.outline.UMLOutlinePage;

public class OpeumEditor extends org.topcased.modeler.uml.editor.UMLEditor{
	private static Map<IContainer,OpeumEclipseContext> contexts = new WeakHashMap<IContainer,OpeumEclipseContext>();
	public static OpeumEclipseContext getCurrentContext(){
		return OpeumEclipseContext.getCurrentContext();
	}
	@Override
	public void close(boolean save){
		if(getCurrentContext() != null && getCurrentContext().isLoading()){
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Opium is still initializing",
					"Cannot close the model while the Opium tooling is still initializing. Please try again shortly.");
		}else{
			super.close(save);
			if(save == false && getCurrentContext() != null){
				getCurrentContext().removeNakedModel(getResourceSet());
			}
			if(getCurrentContext() != null){
				getCurrentContext().onClose(save, getEditingDomain().getResourceSet());
			}
		}
	}
	@Override
	public void doSave(IProgressMonitor monitor){
		try{
			monitor.beginTask("Saving UML Models", 1000);
			super.doSave(new SubProgressMonitor(monitor, 500));
			if(getCurrentContext() != null){
				getCurrentContext().onSave(new SubProgressMonitor(monitor, 500), getEditingDomain().getResourceSet());
			}
		}finally{
			monitor.done();
		}
	}
	@Override
	protected IContentOutlinePage createOutlinePage(){
		return new UMLOutlinePage(this){
			@Override
			protected ModelNavigator createNavigator(Composite parent,Modeler editor,IPageSite pageSite){
				return new OpeumNavigator(parent, editor, pageSite);
			}
		};
	}
	public void dispose(){
		try{
			if(getCurrentContext() != null){
				getCurrentContext().onClose(true, getEditingDomain().getResourceSet());
			}
			contexts.remove(getEditingDomain().getResourceSet());
			super.dispose();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			OpeumEclipseContext.setCurrentContext(null);
		}
	}
	@Override
	protected void setInput(IEditorInput input){
		super.setInput(input);
	}
	public void refreshOutline(){
		// Called when this editor's tab has been selected
		super.refreshOutline();
		IFileEditorInput f = getFileEditorInput(getEditorInput());
		OpeumEclipseContext.setCurrentContext(getContext(f));
	}
	private OpeumEclipseContext getContext(final IFileEditorInput fe){
		IContainer umlDir = fe.getFile().getParent();
		IFile umlFile = getUmlFile(fe);
		OpeumEclipseContext result = findOrCreateContextFor(umlDir);
		if(result != null){
			if(result.isSyncronizingWith(getEditingDomain().getResourceSet())){
				result.setCurrentEditContext(getEditingDomain(), umlFile);
			}else{
				result.startSynch(getEditingDomain(), umlFile);
			}
		}
		return result;
	}
	public static OpeumEclipseContext findOrCreateContextFor(IContainer umlDir){
		OpeumEclipseContext result = getContextFor(umlDir);
		if(result == null){
			OpeumConfig cfg = null;
			final IFile propsFile = (IFile) umlDir.getFile(new Path("opeum.properties"));
			boolean newContext = !propsFile.exists();
			if(newContext){
				cfg = new OpeumConfig(new File(umlDir.getLocation().toFile(), "opeum.properties"));
				OpeumConfigDialog dlg = new OpeumConfigDialog(Display.getDefault().getActiveShell(), cfg);
				final int dlgResult = dlg.open();
				if(dlgResult != Window.OK){
					return null;
				}
				try{
					umlDir.refreshLocal(IResource.DEPTH_INFINITE, null);
				}catch(CoreException e){
					e.printStackTrace();
				}
			}else{
				// Load classes
				OpeumEclipsePlugin.getDefault();
				cfg = new OpeumConfig(propsFile.getLocation().toFile());
			}
			cfg.calculateOutputRoot(umlDir.getProject().getLocation().toFile());
			final OpeumEclipseContext newOne = new OpeumEclipseContext(cfg, umlDir, newContext);
			contexts.put(umlDir, newOne);
			result = newOne;
		}
		OpeumEclipseContext.setCurrentContext(result);
		return result;
	}
	public static OpeumEclipseContext getContextFor(IContainer umlDir){
		OpeumEclipseContext result = contexts.get(umlDir);
		return result;
	}
	private IFile getUmlFile(final IFileEditorInput fe){
		return fe.getFile().getProject().getFile(fe.getFile().getProjectRelativePath().removeFileExtension().addFileExtension("uml"));
	}
	@Override
	protected EObject openFile(final IFile file,boolean resolve){
		EObject openFile = super.openFile(file, resolve);
		IPreferenceStore ps = getPreferenceStore(file);
		if(ps != null){
			String filters = ps.getString(ModelerPreferenceConstants.FILTERS_PREF);
			if(filters == null || filters.length() == 0){
				ps.setValue(ModelerPreferenceConstants.FILTERS_PREF, OpeumFilter.class.getName());
			}
			ps.setValue(ModelerPreferenceConstants.CREATE_CHILD_MENU_PREF, OpeumEditorMenu.class.getName());
		}
		return openFile;
	}
	@Override
	protected List<AdapterFactory> getAdapterFactories(){
		List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new OpeumItemProviderAdapterFactory());
		factories.add(new org.topcased.modeler.uml.providers.UMLModelerProviderAdapterFactory());
		factories.addAll(super.getAdapterFactories());
		return factories;
	}
	@Override
	public DefaultEditDomain getEditDomain(){
		return super.getEditDomain();
	}
}