package org.opaeum.eclipse.javasync;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.opaeum.eclipse.EmfPackageUtil;
import org.opaeum.eclipse.OpaeumEclipsePlugin;
import org.opaeum.eclipse.ProgressMonitorTransformationLog;
import org.opaeum.eclipse.context.OpaeumEclipseContext;
import org.opaeum.eclipse.starter.Activator;
import org.opaeum.eclipse.starter.MemoryUtil;
import org.opaeum.emf.workspace.EmfWorkspace;
import org.opaeum.feature.TransformationProcess;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.rap.RapCapabilities;

public class CreateApplicationProjectAction extends AbstractDirectoryReadingAction{
	public CreateApplicationProjectAction(IStructuredSelection selection2){
		super(selection2, "Create Application Project");
	}
	protected CreateApplicationProjectAction(IStructuredSelection selection2,String string){
		super(selection2, string);
	}
	@Override
	public void run(){
		final IContainer folder = (IContainer) selection.getFirstElement();
		final OpaeumEclipseContext currentContext = OpaeumEclipseContext.findOrCreateContextFor(folder);
		final BasicNewProjectResourceWizard wizard = showWizard(currentContext);
		if(wizard!=null){
			currentContext.getConfig().setProjectNameOverride(wizard.getNewProject().getName());
			currentContext.getConfig().store();
			reinitialiseConfig(currentContext);
			new Job("Recompiling model directory"){
				@Override
				protected IStatus run(final IProgressMonitor monitor){
					TransformationProcess p = null;
					try{
						currentContext.getConfig().addAdditionalTransformationStep(RapCapabilities.class);
						// Create wizard selection wizard.
						monitor.beginTask("Loading All Models", 1000);
						p = prepareDirectoryForTransformation(folder, monitor);
						EmfWorkspace ws = p.findModel(EmfWorkspace.class);
						Collection<Package> rootObjects = ws.getRootObjects();
						for(Package package1:rootObjects){
							if(package1 instanceof Model &&  EmfPackageUtil.isRegeneratingLibrary((Model) package1)){
								ws.addGeneratingModelOrProfile(package1);
							}
							
						}
						monitor.subTask("Generating Java Code");
						p.executeFrom(JavaTransformationPhase.class, new ProgressMonitorTransformationLog(monitor, 400), false);
						if(!(monitor.isCanceled())){
							p.integrate(new ProgressMonitorTransformationLog(monitor, 100));
						}
						monitor.subTask("Generating text files");
//						RapProjectBuilder rpb = new RapProjectBuilder();
//						rpb.initialize(currentContext.getConfig(), p.findModel(TextWorkspace.class), ws,
//								p.findModel(OJUtil.class));
//						rpb.setTransformationContext(new TransformationContext());
//						rpb.beforeWorkspace(ws);
//						OpaeumApplicationGenerator oag = new OpaeumApplicationGenerator();
//						oag.initialize(p.findModel(OJWorkspace.class), currentContext.getConfig(), p.findModel(TextWorkspace.class),
//								ws, p.findModel(OJUtil.class));
//						oag.beforeWorkspace(ws);
						JavaProjectGenerator.writeTextFilesAndRefresh(new SubProgressMonitor(monitor, 400), p, true);
						wizard.getNewProject().refreshLocal(IProject.DEPTH_INFINITE, null);
						currentContext.getUmlDirectory().refreshLocal(IProject.DEPTH_INFINITE, null);
					}catch(Exception e){
						return OpaeumEclipsePlugin.logError("Model directory could not be built", e);
					}finally{
						if(p != null){
							p.release();
						}
						monitor.done();
						MemoryUtil.printMemoryUsage();
					}
					return new Status(IStatus.OK, Activator.PLUGIN_ID, "Model compiled successfully");
				}
			}.schedule();
		}
	}
	public BasicNewProjectResourceWizard showWizard(final OpaeumEclipseContext currentContext){
		IWorkbench workbench = PlatformUI.getWorkbench();
		BasicNewProjectResourceWizard wizard = new BasicNewProjectResourceWizard(){
			@Override
			public void addPages(){
				super.addPages();
				WizardNewProjectCreationPage p = (WizardNewProjectCreationPage) getPages()[0];
				p.setInitialProjectName(currentContext.getConfig().getMavenGroupId() + ".app");
			}
			@Override
			public void createPageControls(Composite pageContainer){
				super.createPageControls(pageContainer);
				try{
					WizardNewProjectCreationPage p = (WizardNewProjectCreationPage) getPages()[0];
					Field f = WizardNewProjectCreationPage.class.getDeclaredField("locationArea");
					f.setAccessible(true);
					ProjectContentsLocationArea pla = (ProjectContentsLocationArea) f.get(p);
					Field bf = ProjectContentsLocationArea.class.getDeclaredField("useDefaultsButton");
					bf.setAccessible(true);
					Button b=(Button) bf.get(pla);
					b.setSelection(false);
					Method m = Widget.class.getDeclaredMethod("sendSelectionEvent", int.class);
					m.setAccessible(true);
					m.invoke(b, SWT.Selection);
				
				}catch(Exception e){
					System.out.println(e);
				}
			}
		};
		wizard.init(workbench, selection);
		wizard.setForcePreviousAndNextButtons(true);
		// Create wizard dialog.
		WizardDialog dialog = new WizardDialog(null, wizard);
		dialog.create();
		dialog.getShell().setSize(Math.max(500, dialog.getShell().getSize().x), 500);
		// Open wizard.
		int open = dialog.open();
		if(open != Window.OK){
			wizard=null;
		}
		return wizard;
	}
}
