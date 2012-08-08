package org.opaeum.eclipse.context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.opaeum.eclipse.EclipseUriToFileConverter;
import org.opaeum.eclipse.EmfToOpaeumSynchronizer;
import org.opaeum.eclipse.OclUpdater;
import org.opaeum.eclipse.OpaeumConfigDialog;
import org.opaeum.eclipse.OpaeumEclipsePlugin;
import org.opaeum.eclipse.OpaeumErrorMarker;
import org.opaeum.eclipse.ProgressMonitorTransformationLog;
import org.opaeum.emf.workspace.EmfWorkspace;
import org.opaeum.emf.workspace.UriToFileConverter;
import org.opaeum.feature.OpaeumConfig;
import org.opaeum.runtime.domain.ExceptionAnalyser;
import org.opaeum.runtime.domain.IntrospectionUtil;

public class OpaeumEclipseContext{
	public static boolean shouldBeCm1Compatible(){
		if(getCurrentContext() != null){
			return getCurrentContext().getConfig().shouldBeCm1Compatible();
		}else{
			return true;
		}
	}

	private static Map<IContainer,OpaeumEclipseContext> contexts = new WeakHashMap<IContainer,OpaeumEclipseContext>();
	private static OpaeumEclipseContext currentContext;
	private Map<IFile,OpenUmlFile> openUmlFiles = new HashMap<IFile,OpenUmlFile>();
	private boolean isOpen = false;
	private IContainer umlDirectory;
	private IFile currentOpenFile;
	protected UriToFileConverter resourceHelper = new EclipseUriToFileConverter();
	private OpaeumErrorMarker errorMarker;
	private boolean isLoading;
	private boolean newlyCreated;
	private EObjectSelectorUI eObjectSelectorUI;
	private TransactionalEditingDomain directoryEditingDomain;
	private EmfWorkspace dew;
	private OpaeumConfig config;
	public OpaeumEclipseContext(OpaeumConfig cfg,IContainer umlDirectory,boolean newlyCreated){
		super();
		this.config=cfg;
		isOpen = true;
		this.newlyCreated = newlyCreated;
		this.umlDirectory = umlDirectory;
		if(cfg.getErrorMarker() == null){
			this.errorMarker = new OpaeumErrorMarker();
		}else{
			this.errorMarker = (OpaeumErrorMarker) IntrospectionUtil.newInstance(cfg.getErrorMarker());
		}
	}
	public boolean isNewlyCreated(){
		return newlyCreated;
	}
	public void reinitialize(){
		this.dew = null;
		this.directoryEditingDomain = null;
		ArrayList<OpenUmlFile> arrayList = new ArrayList<OpenUmlFile>(openUmlFiles.values());
		this.openUmlFiles.clear();
		for(OpenUmlFile editingContext:arrayList){
			editingContext.reinitializeProcess();
		}
		for(OpenUmlFile editingContext:arrayList){
			startSynch(editingContext.getEditingDomain(), editingContext.getFile());
		}
	}
	public String getId(Element umlElement){
		return EmfWorkspace.getId(umlElement);
	}
	public Collection<EmfWorkspace> getEmfWorkspaces(){
		Collection<EmfWorkspace> result = new HashSet<EmfWorkspace>();
		for(OpenUmlFile editingContext:this.openUmlFiles.values()){
			result.add(editingContext.getEmfWorkspace());
		}
		return result;
	}
	public boolean startSynch(final EditingDomain domain,final IFile file){
		OpenUmlFile openUmlFile = new OpenUmlFile(domain,  file,config);
		openUmlFile.addSynchronizationListener(new OclUpdater(this.openUmlFiles));
		openUmlFile.addSynchronizationListener(errorMarker);
		if(dew != null){
			dew.getResourceSet().getResource(URI.createPlatformResourceURI((file).getFullPath().toString(), true), true);
		}
		openUmlFiles.put(file, openUmlFile);
		errorMarker.maybeSchedule(openUmlFile);

		return true;
	}
	public boolean isOpen(){
		return isOpen;
	}
	public void setCurrentEditContext(EditingDomain rs,IFile file,EObjectSelectorUI eObjectSelectorUI){
		seteObjectSelectorUI(eObjectSelectorUI);
		setCurrentContext(this);
		this.currentOpenFile = file;
	}
	public void onSave(IProgressMonitor monitor,IFile f){
		try{
			OpenUmlFile openUmlFile = this.openUmlFiles.get(f);
			monitor.beginTask("Saving UML Models",  100);
			if(openUmlFile != null){
				openUmlFile.setDirty(false);
				openUmlFile.onSave(monitor);
			}
			getUmlDirectory().refreshLocal(1, null);
			if(dew != null){
				List<Resource> resources = new ArrayList<Resource>(dew.getResourceSet().getResources());
				for(Resource resource:resources){
					String lastSegment = resource.getURI().lastSegment();
					String lastSegment2 = f.getLocation().lastSegment();
					if(lastSegment.equals(lastSegment2)){
						resource.unload();
						resource.load(null);
						EcoreUtil.resolveAll(resource);
					}
				}
			}
		}catch(CoreException e){
			e.printStackTrace();
		}catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			monitor.done();
		}
	}
	public void onClose(IFile umlFile){
		OpenUmlFile openUmlFile = this.openUmlFiles.get(umlFile);

		this.openUmlFiles.remove(umlFile);
		if(openUmlFile != null){
			// May not have loaded successfully
			if(openUmlFile.isDirty()){
				reinitialize();
			}else if(openUmlFile != null){
				openUmlFile.release();
				currentOpenFile = null;
			}
		}
	}
	public IContainer getUmlDirectory(){
		return umlDirectory;
	}

	public EmfWorkspace getCurrentEmfWorkspace(){
		if(currentOpenFile == null || !openUmlFiles.containsKey(currentOpenFile) || openUmlFiles.isEmpty()){
			if(dew != null){
				return dew;
			}else{
				return null;
			}
		}else{
			OpenUmlFile openUmlFile = this.openUmlFiles.get(currentOpenFile);
			return openUmlFile.getEmfWorkspace();
		}
	}
	public EmfWorkspace loadDirectory(IProgressMonitor monitor){
		this.isLoading = true;
		try{
			monitor.beginTask("Loading EMF resources", 300);
			ResourceSet rst;
			rst = new ResourceSetImpl();
			URI uri = URI.createPlatformResourceURI(getUmlDirectory().getFullPath().toString(), true);
			if(dew == null){
				dew = new EmfWorkspace(uri, rst, getConfig().getWorkspaceMappingInfo(), getConfig().getWorkspaceIdentifier(),getConfig().getMavenGroupId());
				dew.setUriToFileConverter(new EclipseUriToFileConverter());
				dew.setName(getConfig().getWorkspaceName());
				for(IResource r:umlDirectory.members()){
					monitor.subTask("Loading " + r.getName());
					if(r instanceof IFile && r.getFileExtension().equals("uml")){
						final Resource resource = dew.getResourceSet().getResource(
								URI.createPlatformResourceURI(((IFile) r).getFullPath().toString(), true), true);
						EcoreUtil.resolveAll(resource);
					}
					monitor.worked(100 / umlDirectory.members().length);
				}
			}
			dew.guessGeneratingModelsAndProfiles(URI.createPlatformResourceURI(umlDirectory.getFullPath().toString(), true));
			return dew;
		}catch(CoreException e){
			throw new RuntimeException(e);
		}finally{
			this.isLoading = false;
			monitor.done();
		}
	}
	public boolean isOpen(IFile f){
		for(OpenUmlFile openUmlFile:openUmlFiles.values()){
			for(Resource resource:openUmlFile.getEmfWorkspace().getResourceSet().getResources()){
				if(resource.getURI() != null && resource.getURI().lastSegment().equals(f.getLocation().lastSegment())){
					return true;
				}
			}
		}
		return false;
	}
	public boolean isSyncronizingWith(IFile file){
		return openUmlFiles.containsKey(file);
	}
	public boolean getAutoSync(){
		return this.getConfig().synchronizeAutomatically();
	}
	public boolean isLoading(){
		return this.isLoading;
	}
	public EditingDomain getEditingDomain(){
		if(currentOpenFile != null && openUmlFiles.get(currentOpenFile) != null){
			OpenUmlFile editingContext = openUmlFiles.get(currentOpenFile);
			return editingContext.getEditingDomain();
		}else if(directoryEditingDomain != null){
			return directoryEditingDomain;
		}else{
			return null;
		}
	}
	public OpaeumConfig getConfig(){
		return this.config;
	}
	public static OpaeumEclipseContext getCurrentContext(){
		return currentContext;
	}
	public static void setCurrentContext(OpaeumEclipseContext currentContext){
		OpaeumEclipseContext.currentContext = currentContext;
	}
	public static OpaeumEclipseContext findOrCreateContextFor(IContainer umlDir){
		OpaeumEclipseContext result = getContextFor(umlDir);
		if(result == null){
			OpaeumConfig cfg = null;
			final IFile propsFile = (IFile) umlDir.findMember("opaeum.properties");
			boolean newContext = propsFile == null;
			if(newContext){
				cfg = new OpaeumConfig(new File(umlDir.getLocation().toFile(), "opaeum.properties"));
				OpaeumConfigDialog dlg = new OpaeumConfigDialog(Display.getDefault().getActiveShell(), cfg);
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
				OpaeumEclipsePlugin.getDefault();
				cfg = new OpaeumConfig(propsFile.getLocation().toFile());
			}
			cfg.calculateOutputRoot(umlDir.getProject().getLocation().toFile());
			final OpaeumEclipseContext newOne = new OpaeumEclipseContext(cfg, umlDir, newContext);
			contexts.put(umlDir, newOne);
			result = newOne;
		}
		setCurrentContext(result);
		return result;
	}
	public OpenUmlFile getEditingContextFor(IFile f){
		return this.openUmlFiles.get(f);
	}
	public static OpaeumEclipseContext getContextFor(IContainer umlDir){
		OpaeumEclipseContext result = contexts.get(umlDir);
		return result;
	}
	public static void selectContext(Element e){
		setCurrentContext(getContextFor(e));
		getCurrentContext().selectOpenFileFor(e);
	}
	private void selectOpenFileFor(Element e){
		// TODO consider using the element's resource's filename
		Set<Entry<IFile,OpenUmlFile>> entrySet = openUmlFiles.entrySet();
		for(Entry<IFile,OpenUmlFile> entry:entrySet){
			if(entry.getValue().getEmfWorkspace().getResourceSet().equals(e.eResource().getResourceSet())){
				currentOpenFile = entry.getKey();
				break;
			}
		}
	}
	public static OpaeumEclipseContext getContextFor(Element element){
		String platformString = element.eResource().getURI().toPlatformString(true);
		if(platformString == null){
			return null;
		}else{
			return getContextFor(ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString)).getParent());
		}
	}
	public EObjectSelectorUI geteObjectSelectorUI(){
		return eObjectSelectorUI;
	}
	public void seteObjectSelectorUI(EObjectSelectorUI eObjectSelectorUI){
		this.eObjectSelectorUI = eObjectSelectorUI;
	}
	public void executeAndForget(final Command command){
		performExecute(command, null);
	}
	public void executeAndWait(final Command command){
		if(Display.getCurrent() != null){
			throw new IllegalStateException("ExecuteAndWait can only be called from background threads");
		}
		if(getEditingDomain() instanceof TransactionalEditingDomain){
			try{
				Object lock = new Object();
				synchronized(lock){
					executeInOwnTransaction(command, lock);
					lock.wait(3 * 60 * 1000);
				}
			}catch(InterruptedException e){
				throw new RuntimeException(e);
			}
		}else if(getEditingDomain() == null){
			command.execute();
		}else{
			getEditingDomain().getCommandStack().execute(command);
		}
	}
	private void performExecute(final Command command,final Object lock){
		if(getEditingDomain() instanceof TransactionalEditingDomain){
			executeInOwnTransaction(command, lock);
		}else if(getEditingDomain() == null){
			command.execute();
		}else{
			getEditingDomain().getCommandStack().execute(command);
		}
	}
	private void executeInOwnTransaction(final Command command,final Object lock){
		// !!!!! Papyrus requires all this shit
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				try{
					((TransactionalEditingDomain) getEditingDomain()).runExclusive(new Runnable(){
						public void run(){
							Display.getCurrent().asyncExec(new Runnable(){
								public void run(){
									try{
										getEditingDomain().getCommandStack().execute(command);
									}finally{
										if(lock != null){
											synchronized(lock){
												lock.notifyAll();
											}
										}
									}
								}
							});
						}
					});
				}catch(Exception e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	public OpaeumErrorMarker getErrorMarker(){
		return errorMarker;
	}
	public OpenUmlFile getEditingContextFor(EObject eObject){
		Collection<OpenUmlFile> values = this.openUmlFiles.values();
		for(OpenUmlFile openUmlFile:values){
			if(openUmlFile.getEmfWorkspace().getResourceSet()==eObject.eResource().getResourceSet()){
				return openUmlFile;
			}
		}
		return null;
	}
	public Collection<OpenUmlFile> getEditingContexts(){
		return this.openUmlFiles.values();
	}
}
