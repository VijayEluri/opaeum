package net.sf.nakeduml.emf.extraction;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.metamodel.actions.internal.NakedAddStructuralFeatureValueActionImpl;
import net.sf.nakeduml.metamodel.actions.internal.NakedClearStructuralFeatureActionImpl;
import net.sf.nakeduml.metamodel.actions.internal.NakedReadStructuralFeatureActionImpl;
import net.sf.nakeduml.metamodel.actions.internal.NakedRemoveStructuralFeatureValueActionImpl;
import net.sf.nakeduml.metamodel.activities.INakedInputPin;
import net.sf.nakeduml.metamodel.activities.INakedOutputPin;
import net.sf.nakeduml.metamodel.core.INakedProperty;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.ClearStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.RemoveStructuralFeatureValueAction;

@StepDependency(phase = EmfExtractionPhase.class,requires = {TypedElementExtractor.class,ActivityStructureExtractor.class},after = {
		TypedElementExtractor.class,ActivityStructureExtractor.class})
public class StructuralFeatureActionExtractor extends AbstractActionExtractor{
	@VisitBefore
	public void visitAddStructuralFeatureValueAction(AddStructuralFeatureValueAction emfAction,
			NakedAddStructuralFeatureValueActionImpl nakedAction){
		Activity emfActivity = getActivity(emfAction);
		nakedAction.setFeature((INakedProperty) getNakedPeer(emfAction.getStructuralFeature()));
		nakedAction.setObject((INakedInputPin) initializePin(emfActivity, emfAction.getObject()));
		nakedAction.setValue((INakedInputPin) initializePin(emfActivity, emfAction.getValue()));
		this.addLocalPreAndPostConditions(nakedAction, emfAction);
		assignPartition(nakedAction, emfAction);
		nakedAction.setReplaceAll(emfAction.isReplaceAll());
	}
	@VisitBefore
	public void visitClearStructuralFeatureValueAction(ClearStructuralFeatureAction emfAction,
			NakedClearStructuralFeatureActionImpl nakedAction){
		Activity emfActivity = getActivity(emfAction);
		nakedAction.setFeature((INakedProperty) getNakedPeer(emfAction.getStructuralFeature()));
		nakedAction.setObject((INakedInputPin) initializePin(emfActivity, emfAction.getObject()));
		this.addLocalPreAndPostConditions(nakedAction, emfAction);
		assignPartition(nakedAction, emfAction);
	}
	@VisitBefore
	public void visitRemoveStructuralFeatureValueAction(RemoveStructuralFeatureValueAction emfAction,
			NakedRemoveStructuralFeatureValueActionImpl nakedAction){
		Activity emfActivity = getActivity(emfAction);
		nakedAction.setFeature((INakedProperty) getNakedPeer(emfAction.getStructuralFeature()));
		nakedAction.setObject((INakedInputPin) initializePin(emfActivity, emfAction.getObject()));
		nakedAction.setValue((INakedInputPin) initializePin(emfActivity, emfAction.getValue()));
		this.addLocalPreAndPostConditions(nakedAction, emfAction);
		assignPartition(nakedAction, emfAction);
	}
	@VisitBefore
	public void visitReadStructuralFeatureAction(ReadStructuralFeatureAction emfAction,NakedReadStructuralFeatureActionImpl nakedAction){
		Activity emfActivity = getActivity(emfAction);
		nakedAction.setFeature((INakedProperty) getNakedPeer(emfAction.getStructuralFeature()));
		nakedAction.setObject((INakedInputPin) initializePin(emfActivity, emfAction.getObject()));
		nakedAction.setResult((INakedOutputPin) initializePin(emfActivity, emfAction.getResult()));
		this.addLocalPreAndPostConditions(nakedAction, emfAction);
		assignPartition(nakedAction, emfAction);
	}
}
