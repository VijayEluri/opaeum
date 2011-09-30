package org.opeum.eclipse.javasync;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.opeum.feature.TransformationProcess;
import net.sf.opeum.javageneration.JavaTransformationPhase;
import net.sf.opeum.metamodel.core.INakedElement;
import net.sf.opeum.metamodel.workspace.INakedModelWorkspace;
import net.sf.opeum.pomgeneration.PomGenerationPhase;
import net.sf.opeum.textmetamodel.SourceFolder;
import net.sf.opeum.textmetamodel.TextOutputNode;
import net.sf.opeum.textmetamodel.TextProject;
import net.sf.opeum.textmetamodel.TextWorkspace;
import net.sf.opeum.validation.namegeneration.PersistentNameGenerator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.swt.widgets.Shell;
import org.opeum.eclipse.ProgressMonitorTransformationLog;
import org.opeum.eclipse.context.NakedUmlContextListener;
import org.opeum.eclipse.context.NakedUmlEclipseContext;
import org.opeum.eclipse.starter.Activator;
import org.opeum.eclipse.starter.EclipseProjectGenerationStep;
import org.opeum.java.metamodel.OJPackage;
import org.opeum.topcased.uml.NakedUmlPlugin;

public final class JavaSourceSynchronizer implements NakedUmlContextListener{
	private final IWorkspaceRoot workspace;
	//TODO remove this dependency on the context
	NakedUmlEclipseContext context;
	EclipseProjectGenerationStep eclipseGenerator = new EclipseProjectGenerationStep();
	private TransformationProcess process;
	private IJavaModel javaWorkspace;
	private Set<INakedElement> nakedUmlChanges = new HashSet<INakedElement>();
	private NamespaceRenameRequests namespaceRenameRequests=new NamespaceRenameRequests();
	public JavaSourceSynchronizer(NakedUmlEclipseContext ne,TransformationProcess process){
		this.process = process;
		ne.addContextListener(this);
		ne.addContextListener(this.namespaceRenameRequests);
		this.workspace = ResourcesPlugin.getWorkspace().getRoot();
		this.eclipseGenerator.initialize(workspace, ne.getConfig());
		javaWorkspace = JavaCore.create(workspace);
		this.context = ne;
	}
	@Override
	public void onSave(IProgressMonitor monit){
		new Job("Synchronizing Java sources"){
			public IStatus run(IProgressMonitor monitor){
				try{
					monitor.beginTask("Synchronizing Java sources", 1000);
					if(context.isOpen() && context.getAutoSync()){
						process.replaceModel(context.getCurrentEmfWorkspace());
						renamePackages(new SubProgressMonitor(monitor, 500));
						synchronizeClasses(new SubProgressMonitor(monitor, 500));
					}
					return new Status(IStatus.OK, NakedUmlPlugin.getId(), "Sources Synchronized");
				}catch(Exception e){
					return new Status(IStatus.ERROR, NakedUmlPlugin.getId(), "Sources NOT Synchronized", e);
				}finally{
					monitor.done();
				}
			}
		}.schedule();
	}
	public static boolean hasNewJavaSourceFolders(IWorkspaceRoot workspace,TextWorkspace tws){
		try{
			boolean result = false;
			for(TextProject textProject:tws.getTextProjects()){
				IProject project = workspace.getProject(textProject.getName());
				if(!project.exists() || !project.hasNature(JavaCore.NATURE_ID)){
					result = true;
					break;
				}else{
					if(hasNewSourceFolder(workspace, textProject)){
						result = true;
						break;
					}
				}
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public static boolean hasNewSourceFolder(IWorkspaceRoot workspace,TextProject textProject) throws JavaModelException{
		boolean result = false;
		IJavaModel javaModel = JavaCore.create(workspace);
		IJavaProject javaProject = javaModel.getJavaProject(textProject.getName());
		for(SourceFolder sourceFolder:textProject.getSourceFolders()){
			if(!sourceFolderExistsIn(javaProject, sourceFolder)){
				result = true;
				break;
			}
		}
		return result;
	}
	private static boolean sourceFolderExistsIn(IJavaProject javaProject,SourceFolder sourceFolder) throws JavaModelException{
		boolean hasSourceFolder = false;
		for(IPackageFragmentRoot pfr:javaProject.getPackageFragmentRoots()){
			IResource resource = pfr.getResource();
			if(resource != null){
				IPath projectRelativePath = resource.getProjectRelativePath();
				String path1 = projectRelativePath.toString();
				String path2 = sourceFolder.getRelativePath();
				if(!pfr.isArchive() && path1.equals(path2)){
					hasSourceFolder = true;
				}
			}
		}
		return hasSourceFolder;
	}
	private void renamePackages(IProgressMonitor monitor){
		try{
			Set<NamespaceRenameRequest> renamedNamespaces2 = namespaceRenameRequests.getRenamedNamespaces();
			monitor.beginTask("Renaming Packages", renamedNamespaces2.size());
			for(NamespaceRenameRequest rn:renamedNamespaces2){
				monitor.subTask("Renaming " + rn.getOldName());
				renamePackages(rn);
				monitor.worked(1);
			}
			namespaceRenameRequests.clearRenamedNamespaces();
		}finally{
			monitor.done();
		}
	}
	private void synchronizeClasses(IProgressMonitor monitor){
		try{
			monitor.beginTask("Generating Java Code", 1000);
			Set<INakedElement> clss;
			synchronized(nakedUmlChanges){
				clss = new HashSet<INakedElement>(this.nakedUmlChanges);
				nakedUmlChanges.clear();
			}
			if(clss.size() > 0){
				process.replaceModel(new OJPackage());
				process.replaceModel(new TextWorkspace());
				PersistentNameGenerator png = new PersistentNameGenerator();
				for(INakedElement ne:clss){
					png.visitOnly(ne);
				}
				Collection<?> processElements = process.processElements(clss, JavaTransformationPhase.class, new ProgressMonitorTransformationLog(monitor, 400));
				TextWorkspace tws = process.findModel(TextWorkspace.class);
				if(hasNewJavaSourceFolders(workspace, tws)){
					process.executePhase(PomGenerationPhase.class, false, new ProgressMonitorTransformationLog(monitor, 100));
					new JavaProjectGenerator(process.getConfig(), process, workspace).schedule();
				}
				for(Object object:processElements){
					if(object instanceof TextOutputNode){
						TextOutputNode txt = (TextOutputNode) object;
						monitor.subTask("Emitting " + txt.getName());
						eclipseGenerator.visitUpFirst(txt);
					}
					monitor.worked(500 / processElements.size());
				}
			}
		}finally{
			monitor.done();
		}
	}
	@Override
	public void synchronizationComplete(INakedModelWorkspace workspace,Set<INakedElement> affectedElements){
		this.nakedUmlChanges.addAll(affectedElements);
	}
	private void renamePackages(NamespaceRenameRequest rn){
		try{
			for(IJavaProject jp:javaWorkspace.getJavaProjects()){
				for(IPackageFragmentRoot pfr:jp.getPackageFragmentRoots()){
					for(IJavaElement iJavaElement:pfr.getChildren()){
						if(iJavaElement instanceof IPackageFragment){
							IPackageFragment childPf = (IPackageFragment) iJavaElement;
							if(childPf.getElementName().contains(rn.getOldName())){
								String newName = childPf.getElementName().replaceAll(rn.getOldName(), rn.getNewName());
								rename(childPf, newName);
							}
						}
					}
				}
			}
		}catch(JavaModelException e){
			Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}
	private void rename(IPackageFragment childPf,String newName){
		try{
			boolean isValid = Character.isJavaIdentifierStart(newName.charAt(0));
			if(isValid){
				for(int i = 0;i < newName.length();i++){
					if(!Character.isJavaIdentifierPart(newName.charAt(i))){
						isValid = false;
					}
				}
			}
			if(isValid){
				RenameSupport rs = RenameSupport.create(childPf, newName, RenameSupport.UPDATE_REFERENCES);
				Shell shell = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
				rs.perform(shell, Activator.getDefault().getWorkbench().getActiveWorkbenchWindow());
			}
			// childPf.rename(newName, true, null);
		}catch(Exception e){
			Activator.getDefault().getLog().log(new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}
	@Override
	public void onClose(boolean save){
	}
}