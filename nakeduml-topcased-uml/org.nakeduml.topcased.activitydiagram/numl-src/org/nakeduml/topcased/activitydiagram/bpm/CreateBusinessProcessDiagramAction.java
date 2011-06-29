package org.nakeduml.topcased.activitydiagram.bpm;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import org.topcased.modeler.extensions.ICreationDiagram;

public class CreateBusinessProcessDiagramAction extends Command implements ICreationDiagram{
	private Namespace owner;
	private Operation specification;
	private Activity activity;
	public EObject getLinkedElementWithDiagram(EObject initialObject){
		activity = (Activity) UMLFactory.eINSTANCE.createActivity();
		if(initialObject instanceof Operation){
			specification = (Operation) initialObject;
			this.owner = (Namespace) specification.getOwner();
			activity.setName(specification.getName() + "Process");
		}else{
			this.owner = (Namespace) initialObject;
			activity.setName("BusinessProcess1");
		}
		return activity;
	}
	@Override
	public void execute(){
		redo();
	}
	@Override
	public void redo(){
		if(owner instanceof BehavioredClassifier){
			((BehavioredClassifier) owner).getOwnedBehaviors().add(activity);
		}else{
			((Package) owner).getOwnedTypes().add(activity);
		}
		if(specification != null){
			activity.setSpecification(specification);
		}
	}
	@Override
	public void undo(){
		if(owner instanceof BehavioredClassifier){
			((BehavioredClassifier) owner).getOwnedBehaviors().remove(activity);
		}else{
			((Package) owner).getOwnedTypes().remove(activity);
		}
	}
}