package org.nakeduml.uml2uim;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.nakeduml.emf.workspace.EmfWorkspace;
import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitBefore;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.TypedElement;
import org.nakeduml.eclipse.EmfStateMachineUtil;
import org.nakeduml.name.NameConverter;
import org.nakeduml.uim.UimFactory;
import org.nakeduml.uim.UmlReference;
import org.nakeduml.uim.action.ActionKind;
import org.nakeduml.uim.folder.ActivityFolder;
import org.nakeduml.uim.folder.EntityFolder;
import org.nakeduml.uim.folder.StateMachineFolder;
import org.nakeduml.uim.form.ActionTaskForm;
import org.nakeduml.uim.form.ClassForm;
import org.nakeduml.uim.form.FormFactory;
import org.nakeduml.uim.form.FormPanel;
import org.nakeduml.uim.form.OperationInvocationForm;
import org.nakeduml.uim.form.OperationTaskForm;
import org.nakeduml.uim.form.StateForm;
import org.nakeduml.uim.form.UimForm;
import org.nakeduml.uim.util.SafeUmlUimLinks;
import org.nakeduml.uim.util.UimUtil;
import org.nakeduml.uim.util.UmlUimLinks;

@StepDependency(phase = UserInteractionSynchronizationPhase.class,requires = FormFolderSynchronizer.class,after = FormFolderSynchronizer.class)
public class FormSynchronizer extends AbstractUimSynchronizer{
	public FormSynchronizer(){
	}
	public FormSynchronizer(EmfWorkspace workspace, ResourceSet resourceSet,boolean regenerate){
		super(workspace,resourceSet, regenerate);
	}
	@VisitBefore(matchSubclasses = false)
	public void beforeAction(OpaqueAction a){
		String resourceUri = UmlUimLinks.getInstance(a).getId(a);
		UimForm form = getFormFor(resourceUri, "uim");
		ActionTaskForm atf = null;
		if(regenerate || form.getPanel() == null){
			atf = FormFactory.eINSTANCE.createActionTaskForm();
			initForm(form, a, atf);
			// TODO make input entities editable through inputs tab per entity
			FormCreator fc = new FormCreator(atf);
			ArrayList<TypedElement> pins = new ArrayList<TypedElement>(a.getInputs());
			pins.addAll(a.getOutputs());
			fc.prepareFormPanel("Task: " + NameConverter.separateWords(a.getName()), pins);
			fc.addButtonBar(ActionKind.CLAIM_TASK, ActionKind.DELEGATE_TASK, ActionKind.FORWARD_TASK,ActionKind.SUSPEND_TASK);
		}else{
			atf = (ActionTaskForm) form.getPanel();
		}
		putFormElements(atf);
		atf.setFolder((ActivityFolder) UmlUimLinks.getInstance(a).getFolderFor(a.getActivity()));
	}
	@VisitBefore(matchSubclasses = true)
	public void beforeClass(Class c){
		EntityFolder folder = (EntityFolder) UmlUimLinks.getInstance(c).getFolderFor(c.getNamespace());
		createClassForm(c, folder, ActionKind.UPDATE, ActionKind.DELETE, ActionKind.BACK);
		createClassForm(c, folder, ActionKind.CREATE, ActionKind.BACK);
	}
	@VisitBefore(matchSubclasses = true)
	public void beforeComponent(Component c){
		//TODO create external view;
	}
	private void createClassForm(Class c,EntityFolder folder,ActionKind...actionKinds){
		String suffix = actionKinds[0] == ActionKind.UPDATE ? "Editor" : "Creator";
		String resourceUri = UmlUimLinks.getId(c) + suffix;
		UimForm form = getFormFor(resourceUri, "uim");
		ClassForm panel;
		if(regenerate || form.getPanel() == null){
			panel = FormFactory.eINSTANCE.createClassForm();
			initForm(form, c, panel);
			panel.setName(c.getName() + suffix);
			FormCreator fc = new FormCreator(panel);
			Collection<Property> allAttributes = SafeUmlUimLinks.getInstance(c).getOwnedAttributes(c);
			fc.prepareFormPanel((actionKinds[0] == ActionKind.UPDATE ? "Edit " : "Create") + NameConverter.separateWords(c.getName()), allAttributes);
			fc.addButtonBar(actionKinds);
		}else{
			panel= (ClassForm) form.getPanel();
		}
		putFormElements(panel);
		panel.setFolder(folder);
	}
	@VisitBefore(matchSubclasses = false)
	public void beforeOperation(Operation o){
		EntityFolder ef = (EntityFolder) UmlUimLinks.getInstance(o).getFolderFor(o.getClass_());
		if(UimUtil.isTask(o)){
			String resourceUri = UmlUimLinks.getInstance(o).getId(o) + "Task";
			UimForm form = getFormFor(resourceUri, "uim");
			OperationTaskForm otf;
			if(regenerate || form.getPanel() == null){
				otf = FormFactory.eINSTANCE.createOperationTaskForm();
				initForm(form, o, otf);
				// TODO make input entities editable through inputs tab per entity
				FormCreator fc = new FormCreator(otf);
				fc.prepareFormPanel("Task: " + NameConverter.separateWords(o.getName()), o.getOwnedParameters());
				fc.addButtonBar(ActionKind.COMPLETE_TASK, ActionKind.SUSPEND_TASK);
				otf.setFolder(ef);
			}else{
				otf = (OperationTaskForm) form.getPanel();
				otf.setFolder(ef);
			}
			putFormElements(otf);
		}
		// TODO generate table Panels for multi output parameters and detail panels for single output parameters
		String resourceUri = UmlUimLinks.getInstance(o).getId(o) + "Invoker";
		UimForm form = getFormFor(resourceUri, "uim");
		OperationInvocationForm oif;
		if(regenerate || form.getPanel() == null){
			oif = FormFactory.eINSTANCE.createOperationInvocationForm();
			initForm(form, o, oif);
			// TODO make input entities editable through inputs tab per entity
			FormCreator fc = new FormCreator(oif);
			fc.prepareFormPanel("Invoke: " + NameConverter.separateWords(o.getName()), o.getOwnedParameters());
			fc.addButtonBar(ActionKind.EXECUTE_OPERATION, ActionKind.BACK);
			oif.setFolder(ef);
		}else{
			oif = (OperationInvocationForm) form.getPanel();
			oif.setFolder(ef);
		}
		putFormElements(oif);
	}
	@VisitBefore(matchSubclasses = false)
	public void beforeState(State s){
		String resourceUri = UmlUimLinks.getId(s);
		StateMachine sm = EmfStateMachineUtil.getStateMachine(s);
		StateMachineFolder smf = (StateMachineFolder) UmlUimLinks.getInstance(s).getFolderFor(sm);
		UimForm form = getFormFor(resourceUri, "uim");
		StateForm sf;
		if(regenerate || form.getPanel() == null){
			sf = FormFactory.eINSTANCE.createStateForm();
			initForm(form, s, sf);
			// TODO make input entities editable through inputs tab per entity
			FormCreator fc = new FormCreator(sf);
			EList<Property> allAttributes;
			if(sm.getContext() != null && sm.getContext().getClassifierBehavior() == sm){
				allAttributes = sm.getContext().getAllAttributes();
			}else{
				allAttributes = sm.getAllAttributes();
			}
			fc.prepareFormPanel(NameConverter.separateWords(s.getName()), allAttributes);
			fc.addButtonBar(ActionKind.UPDATE, ActionKind.DELETE, ActionKind.BACK);
			sf.setFolder(smf);
		}else{
			sf = (StateForm) form.getPanel();
			sf.setFolder(smf);
		}
		putFormElements(sf);
	}
	private void initForm(UimForm form,NamedElement a,FormPanel sf){
		form.setPanel(sf);
		sf.setUmlElementUid(UmlUimLinks.getId(a));
		sf.setName(a.getName());
	}
	private UimForm getFormFor(String id,String extenstion){
		Resource resource = getResource(id, extenstion);
		if(resource.getContents().isEmpty()){
			resource.getContents().add(FormFactory.eINSTANCE.createUimForm());
		}
		return (UimForm) resource.getContents().get(0);
	}

	private void putFormElements(FormPanel form){
		UmlUimLinks.getInstance(form).link(form);
		// Optimization
		TreeIterator<EObject> eAllContents = form.eAllContents();
		while(eAllContents.hasNext()){
			EObject eObject = (EObject) eAllContents.next();
			if(eObject instanceof UmlReference){
				UmlUimLinks.getInstance(form).link((UmlReference) eObject);
			}
		}
	}
}
