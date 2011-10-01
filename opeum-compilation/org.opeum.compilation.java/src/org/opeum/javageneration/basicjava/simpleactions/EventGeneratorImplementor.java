package org.opeum.javageneration.basicjava.simpleactions;

import org.opeum.feature.StepDependency;
import org.opeum.feature.visit.VisitBefore;
import org.opeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opeum.javageneration.AbstractJavaProducingVisitor;
import org.opeum.javageneration.JavaTransformationPhase;
import org.opeum.javageneration.jbpm5.EventUtil;
import org.opeum.javageneration.jbpm5.activity.ActivityEventConsumptionImplementor;
import org.opeum.javageneration.jbpm5.statemachine.StateMachineEventConsumptionImplementor;
import org.opeum.javageneration.util.OJUtil;
import org.opeum.metamodel.commonbehaviors.INakedBehavioredClassifier;

@StepDependency(phase = JavaTransformationPhase.class,requires = {
		StateMachineEventConsumptionImplementor.class,ActivityEventConsumptionImplementor.class
},after = {
		StateMachineEventConsumptionImplementor.class,ActivityEventConsumptionImplementor.class
})
public class EventGeneratorImplementor extends AbstractJavaProducingVisitor{
	@VisitBefore(matchSubclasses = true)
	public void visitBehavioredClassifier(INakedBehavioredClassifier s){
		if(OJUtil.hasOJClass(s)){
			OJAnnotatedClass ojClass = findJavaClass(s);
			EventUtil.addOutgoingEventManagement(ojClass);
		}
	}
}