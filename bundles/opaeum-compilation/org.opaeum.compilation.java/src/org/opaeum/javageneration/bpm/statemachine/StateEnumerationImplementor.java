package org.opaeum.javageneration.bpm.statemachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Vertex;
import org.opaeum.eclipse.EmfStateMachineUtil;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.javageneration.bpm.BpmUtil;
import org.opaeum.javageneration.bpm.ProcessStepEnumerationImplementor;

@StepDependency(phase = JavaTransformationPhase.class,requires = StateMachineImplementor.class,after = StateMachineImplementor.class)
public class StateEnumerationImplementor extends ProcessStepEnumerationImplementor{
	@Override
	protected NamedElement getEnclosingElement(NamedElement s){
		Vertex state = (Vertex) s;
		if(state.getContainer().getState() != null){
			return state.getContainer().getState();
		}else{
			return null;
		}
	}
	@VisitBefore(matchSubclasses = true)
	public void visitClass(StateMachine c){
		boolean hasStateComposition = hasStateComposition(c);
		OJEnum e = buildOJEnum(c, hasStateComposition);
		OJAnnotatedOperation getOpaeumId = new OJAnnotatedOperation("getOpaeumId", new OJPathName("long"));
		e.addToOperations(getOpaeumId);
		getOpaeumId.initializeResultVariable("getId()");
		Collection<Region> regions = c.getRegions();
		regions(regions);
	}
	private void regions(Collection<Region> regions){
		for(Region r:regions){
			for(Vertex s:r.getSubvertices()){
				state(s);
				if(s instanceof State){
				regions(((State) s).getRegions());}
			}
		}
	}
	private boolean hasStateComposition(StateMachine sm){
		for(State s:EmfStateMachineUtil.getStatesRecursively(sm)){
			if(s.getContainer().getState() != null){
				return true;
			}
		}
		return false;
	}
	private void state(Vertex state){
		StateMachine sm = EmfStateMachineUtil.getNearestApplicableStateMachine(state);
		OJPackage p = findOrCreatePackage(ojUtil.packagePathname((Namespace) sm.getOwner()));
		OJEnum e = (OJEnum) p.findClass(new OJPathName(sm.getName() + "State"));
		State enclosingElement = state.getContainer().getState();
		buildLiteral(state, e, enclosingElement == null ? "null" : BpmUtil.stepLiteralName(enclosingElement));
	}
	@Override
	protected Collection<Trigger> getOperationTriggers(Element step){
		Vertex state = (Vertex) step;
		Collection<Trigger> result = new ArrayList<Trigger>();
		List<Transition> outgoing = state.getOutgoings();
		for(Transition t:outgoing){
			Collection<Trigger> triggers = t.getTriggers();
			for(Trigger trigger:triggers){
				if(trigger.getEvent() instanceof CallEvent){
					result.add(trigger);
				}
			}
		}
		return result;
	}
}
