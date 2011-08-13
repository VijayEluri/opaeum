package org.nakeduml.eclipse.starter;

import java.io.File;
import java.util.Iterator;

import net.sf.nakeduml.feature.TransformationProcess;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import net.sf.nakeduml.textmetamodel.TextWorkspace;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.uml2.uml.Model;
import org.nakeduml.eclipse.NakedUmlEclipsePlugin;
import org.nakeduml.eclipse.ProgressMonitorTransformationLog;
import org.nakeduml.topcased.uml.editor.NakedUmlEditor;

public class RecalculateDependenciesAction implements IObjectActionDelegate{
	private IStructuredSelection selection;
	private IWorkbenchPart workbenchPart;
	static Model model;
	@Override
	public void run(IAction action){
		for(Iterator<?> it = selection.iterator();it.hasNext();){
			Object element = it.next();
			if(element instanceof Model){
				model = (Model) element;
				new Job("Recalculating Dependencies"){
					@Override
					protected IStatus run(final IProgressMonitor monitor){
						try{
							TransformationProcess p = JavaTransformationProcessManager.getCurrentTransformationProcess();
							if(p == null){
								Shell shell = workbenchPart.getSite().getShell();
								MessageDialog.openError(shell, "NakedUML still initializing","The NakedUML tooling is still initializing. Please try again shortly.");
							}else{
								monitor.beginTask("Generating Java Code", p.getPhases().size());
								p.setLog(new ProgressMonitorTransformationLog(monitor));
								p.execute();
								p.integrate();
								new JavaProjectGenerator(NakedUmlEditor.getCurrentContext().getUmlElementCache().getConfig(), p, ResourcesPlugin.getWorkspace()
										.getRoot(), true).schedule();
								ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IProject.DEPTH_INFINITE, null);
							}
						}catch(Exception e){
							NakedUmlEclipsePlugin.getDefault().getLog().log(new Status(Status.INFO, NakedUmlEclipsePlugin.getPluginId(), Status.OK, e.getMessage(), e));
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							monitor.done();
						}
						return new Status(IStatus.OK, Activator.PLUGIN_ID, "Model compiled successfully");
					}
				}.schedule();
				break;
			}
		}
	}
	public File getModeFile(Model model){
		String uriPAth = model.eResource().getURI().toPlatformString(true);
		IFile modelIFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(uriPAth));
		File modelFile = modelIFile.getLocation().toFile();
		return modelFile;
	}
	@Override
	public void selectionChanged(IAction action,ISelection selection){
		this.selection = (IStructuredSelection) selection;
		action.setEnabled(EditNakedUmlConfigAction.hasConfigFile((IStructuredSelection) selection));
	}
	@Override
	public void setActivePart(IAction arg0,IWorkbenchPart workbenchPart){
		this.workbenchPart = workbenchPart;
	}
}
