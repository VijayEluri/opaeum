package org.opeum.emf.extraction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Pin;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValuePin;
import org.opeum.metamodel.activities.INakedActivityNode;
import org.opeum.metamodel.activities.INakedActivityPartition;
import org.opeum.metamodel.activities.INakedPin;
import org.opeum.metamodel.activities.internal.NakedActionImpl;
import org.opeum.metamodel.activities.internal.NakedInputPinImpl;
import org.opeum.metamodel.activities.internal.NakedObjectNodeImpl;
import org.opeum.metamodel.activities.internal.NakedOutputPinImpl;
import org.opeum.metamodel.activities.internal.NakedPinImpl;
import org.opeum.metamodel.activities.internal.NakedValuePinImpl;
import org.opeum.metamodel.core.INakedClassifier;
import org.opeum.metamodel.core.internal.NakedMultiplicityImpl;

public abstract class AbstractActionExtractor extends CommonBehaviorExtractor{
	@SuppressWarnings("unchecked")
	protected <T extends INakedPin>List<T> populatePins(Activity emfActivity,List<? extends Pin> emfArguments){
		List<T> nakedArguments = new ArrayList<T>();
		for(int i = 0;i < emfArguments.size();i++){
			Pin arg = (Pin) emfArguments.get(i);
			INakedPin nakedPin = initializePin(emfActivity, arg);
			nakedPin.setIndex(i);
			nakedArguments.add((T) nakedPin);
		}
		return nakedArguments;
	}
	protected void initAction(Action emfAction,NakedActionImpl nakedAction){
		assignPartition(nakedAction, emfAction);
	}
	protected INakedPin initializePin(Activity activity,Pin emfPin){
		// Pins belong to actions and should always be initialized as part of
		// the
		// initialization of the action
		NakedPinImpl resultingPin = null;
		if(emfPin instanceof ValuePin){
			NakedValuePinImpl nakedValuePin = (NakedValuePinImpl) getNakedPeer(emfPin);
			if(nakedValuePin == null){
				nakedValuePin = new NakedValuePinImpl();
				initialize(nakedValuePin, emfPin, emfPin.getOwner());
			}
			INakedClassifier nakedType = (INakedClassifier) getNakedPeer((emfPin.getType()));
			nakedValuePin.setBaseType(nakedType);
			resolveMultiplicityAndActualType(nakedValuePin, emfPin);
			resultingPin = nakedValuePin;
		}else if(emfPin instanceof InputPin){
			resultingPin = (NakedPinImpl) getNakedPeer(emfPin);
			if(resultingPin == null){
				resultingPin = new NakedInputPinImpl();
				initialize(resultingPin, emfPin, emfPin.getOwner());
			}
			INakedClassifier nakedType = (INakedClassifier) getNakedPeer((emfPin.getType()));
			resultingPin.setBaseType(nakedType);
			resolveMultiplicityAndActualType(resultingPin, emfPin);
		}else if(emfPin instanceof OutputPin){
			resultingPin = (NakedPinImpl) getNakedPeer(emfPin);
			if(resultingPin == null){
				resultingPin = new NakedOutputPinImpl();
				initialize(resultingPin, emfPin, emfPin.getOwner());
			}
			INakedClassifier nakedType = (INakedClassifier) getNakedPeer((emfPin.getType()));
			resultingPin.setBaseType(nakedType);
			resolveMultiplicityAndActualType(resultingPin, emfPin);
		}
		if(resultingPin != null){
			resultingPin.setName(emfPin.getName());
		}
		return resultingPin;
	}
	protected void resolveMultiplicityAndActualType(NakedPinImpl ae,Pin te){
		populateMultiplicityAndBaseType(te, te.getType(), ae);
	}
	protected void populateMultiplicityAndBaseType(MultiplicityElement emfNode,Type type,NakedObjectNodeImpl ae){
		ae.setMultiplicity(new NakedMultiplicityImpl(emfNode.getLower(), emfNode.getUpper() == -1 ? Integer.MAX_VALUE : emfNode.getUpper()));
		ae.setIsOrdered(emfNode.isOrdered());
		ae.setIsUnique(emfNode.isUnique());
		ae.setBaseType((INakedClassifier) getNakedPeer(type));
	}
	protected void assignPartition(INakedActivityNode node,ActivityNode emfNode){
		if(emfNode.getInPartitions().size() == 1){
			node.setInPartition(((INakedActivityPartition) getNakedPeer((emfNode.getInPartitions().get(0)))));
		}
	}
}