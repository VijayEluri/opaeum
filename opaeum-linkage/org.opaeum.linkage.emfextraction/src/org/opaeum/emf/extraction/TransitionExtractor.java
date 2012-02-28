package org.opaeum.emf.extraction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.metamodel.core.INakedClassifier;
import org.opaeum.metamodel.core.INakedTypedElement;
import org.opaeum.metamodel.statemachines.INakedState;
import org.opaeum.metamodel.statemachines.internal.NakedTransitionImpl;

@StepDependency(phase = EmfExtractionPhase.class,requires = {StateExtractor.class,InstanceExtractor.class, ActivityExtractor.class},after = {ActivityExtractor.class, StateExtractor.class,InstanceExtractor.class})
public class TransitionExtractor extends CommonBehaviorExtractor{
	protected List<INakedTypedElement> getEnvironment(){
		List<INakedTypedElement> result = new ArrayList<INakedTypedElement>();
		return result;
	}
	@VisitBefore
	public void visitTransition(Transition emfTransition, NakedTransitionImpl nakedTransition){
		INakedState source = (INakedState) getNakedPeer(emfTransition.getSource());
		INakedState target = (INakedState) getNakedPeer(emfTransition.getTarget());
		nakedTransition.setSource(source);
		nakedTransition.setTarget(target);
		nakedTransition.setEffect(getOwnedBehavior(nakedTransition, emfTransition.getEffect()));
	}
	protected INakedClassifier getNearestContext(Transition node){
		StateMachine sm = getStateMachine(node);
		return super.getNearestContext(sm);
	}
	private StateMachine getStateMachine(Transition node){
		Element e = node;
		while(!(e instanceof StateMachine) && e != null){
			e = e.getOwner();
		}
		StateMachine sm = (StateMachine) e;
		return sm;
	}
}
