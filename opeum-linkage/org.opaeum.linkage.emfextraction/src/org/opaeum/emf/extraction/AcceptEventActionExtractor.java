package org.opeum.emf.extraction;

import java.util.List;

import org.eclipse.uml2.uml.AcceptCallAction;
import org.eclipse.uml2.uml.AcceptEventAction;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Element;
import org.opeum.feature.StepDependency;
import org.opeum.feature.visit.VisitBefore;
import org.opeum.metamodel.actions.internal.NakedAcceptCallActionImpl;
import org.opeum.metamodel.actions.internal.NakedAcceptEventActionImpl;
import org.opeum.metamodel.activities.INakedOutputPin;
import org.opeum.metamodel.bpm.internal.NakedAcceptDeadlineActionImpl;
import org.opeum.metamodel.bpm.internal.NakedAcceptTaskEventActionImpl;
import org.opeum.metamodel.core.internal.NakedElementImpl;
import org.opeum.metamodel.core.internal.StereotypeNames;

@StepDependency(phase = EmfExtractionPhase.class,requires = ActionExtractor.class,after = {
	ActionExtractor.class
})
public class AcceptEventActionExtractor extends AbstractActionExtractor{
	@VisitBefore
	public void visitAcceptEventAction(AcceptEventAction emfAction, NakedAcceptEventActionImpl nakedAction){
		initAction(emfAction, nakedAction);
		Activity emfActivity = getActivity(emfAction);
		List<INakedOutputPin> result = populatePins(emfActivity, emfAction.getResults());
		nakedAction.setResult(result);
	}
	@Override
	protected NakedElementImpl createElementFor(Element e,Class<?> peerClass){
		if(e instanceof AcceptCallAction){
			return new NakedAcceptCallActionImpl();
		}else if(e instanceof AcceptEventAction){
			AcceptEventAction emfAction=(AcceptEventAction) e;
			if(StereotypesHelper.hasKeyword(emfAction, StereotypeNames.ACCEPT_DEADLINE_ACTION)){
				return new NakedAcceptDeadlineActionImpl();
			}else if(StereotypesHelper.hasKeyword(emfAction, StereotypeNames.ACCEPT_TASK_EVENT_ACTION)){
				return new NakedAcceptTaskEventActionImpl();
			}else{
				return new NakedAcceptEventActionImpl();
			}
		}
		return null;
	}
	@VisitBefore
	public void visitAcceptCallAction(AcceptCallAction emfAction,NakedAcceptCallActionImpl nakedAction){
		initAction(emfAction, nakedAction);
		Activity emfActivity = getActivity(emfAction);
		nakedAction.setReturnInfo((INakedOutputPin) initializePin(emfActivity, emfAction.getReturnInformation()));
		List<INakedOutputPin> result = populatePins(emfActivity, emfAction.getResults());
		nakedAction.setResult(result);
	}
}