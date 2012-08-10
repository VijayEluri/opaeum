package org.opaeum.eclipse.javasync;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.opaeum.eclipse.OpaeumEclipsePlugin;
import org.opaeum.eclipse.ProgressMonitorTransformationLog;
import org.opaeum.eclipse.context.OpaeumEclipseContext;
import org.opaeum.eclipse.starter.AbstractOpaeumAction;
import org.opaeum.eclipse.starter.Activator;
import org.opaeum.eclipse.starter.MemoryUtil;
import org.opaeum.emf.workspace.EmfWorkspace;
import org.opaeum.feature.OpaeumConfig;
import org.opaeum.feature.TransformationProcess;
import org.opaeum.java.metamodel.OJWorkspace;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.textmetamodel.TextWorkspace;

public class RecompileModelDirectoryAction extends AbstractOpaeumAction{
	public RecompileModelDirectoryAction(IStructuredSelection selection2){
		super(selection2, "Recompile Model Directory");
	}
	protected RecompileModelDirectoryAction(IStructuredSelection selection2,String string){
		super(selection2, string);
	}
	@Override
	public void run(){
		final IContainer folder = (IContainer) selection.getFirstElement();
		final OpaeumEclipseContext currentContext = OpaeumEclipseContext.findOrCreateContextFor(folder);
		new Job("Recompiling model directory"){
			@Override
			protected IStatus run(final IProgressMonitor monitor){
				try{
					monitor.beginTask("Loading All Models", 1000);
					TransformationProcess p = prepareDirectoryForTransformation(folder, monitor);
					monitor.subTask("Generating Java Code");
					p.executeFrom(JavaTransformationPhase.class, new ProgressMonitorTransformationLog(monitor, 400), false);
					if(!(monitor.isCanceled())){
						p.integrate(new ProgressMonitorTransformationLog(monitor, 100));
					}
					monitor.subTask("Generating text files");
					JavaProjectGenerator.writeTextFilesAndRefresh(new SubProgressMonitor(monitor, 400), p, true);
					currentContext.getUmlDirectory().refreshLocal(IProject.DEPTH_INFINITE, null);
					EmfWorkspace mw = p.findModel(EmfWorkspace.class);
					TreeIterator<Notifier> iter = mw.getResourceSet().getAllContents();
					Map<Long,Element> ids = new HashMap<Long,Element>();
					int duplicates = 0;
					while(iter.hasNext()){
						Notifier notifier = (Notifier) iter.next();
						if(notifier instanceof Element){
							Element e = (Element) notifier;
							if(!ids.containsKey(EmfWorkspace.getOpaeumId(e))){
								ids.put(EmfWorkspace.getOpaeumId(e), e);
							}else{
								duplicates++;
								Element other = ids.get(EmfWorkspace.getOpaeumId(e));
								if(other instanceof NamedElement){
									System.out.println(((NamedElement) e).getQualifiedName() + " collides with " + ((NamedElement) other).getQualifiedName());
								}
								System.out.println(e.getClass().getName() + " collides with " + other.getClass().getName());
								System.out.println(EmfWorkspace.getId(e) + " collides with " + EmfWorkspace.getId(other));
							}
						}
					}
					System.out.println("Number of duplicates: " + duplicates + " from " + ids.size());
				}catch(Exception e){
					e.printStackTrace();
					return new Status(Status.ERROR, OpaeumEclipsePlugin.getPluginId(), Status.ERROR, e.getMessage(), e);
				}finally{
					monitor.done();
					MemoryUtil.printMemoryUsage();
				}
				return new Status(IStatus.OK, Activator.PLUGIN_ID, "Model compiled successfully");
			}
		}.schedule();
	}
	protected TransformationProcess prepareDirectoryForTransformation(final IContainer folder,final IProgressMonitor monitor)
			throws CoreException{
		monitor.subTask("Saving Open Models");
		final OpaeumEclipseContext ctx = OpaeumEclipseContext.findOrCreateContextFor(folder);
		monitor.worked(5);
		monitor.subTask("Loading Opaeum Metadata");
		final EmfWorkspace ws=ctx.loadDirectory(new SubProgressMonitor(monitor, 200));
		ws.getOpaeumLibrary().reset();
		TransformationProcess p = JavaTransformationProcessManager.getTransformationProcessFor(folder);
		p.replaceModel(ws);
		p.replaceModel(new OJUtil());
		p.removeModel(OJWorkspace.class);
		p.removeModel(TextWorkspace.class);
		OpaeumConfig config = ctx.getConfig();
		config.getSourceFolderStrategy().defineSourceFolders(config);
		return p;
	}
}
