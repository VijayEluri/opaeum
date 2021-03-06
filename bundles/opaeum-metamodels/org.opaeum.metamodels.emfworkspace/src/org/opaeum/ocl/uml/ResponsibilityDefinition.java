package org.opaeum.ocl.uml;

import java.util.Collection;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.TimeEvent;
import org.opaeum.runtime.domain.TaskDelegation;

public interface ResponsibilityDefinition{
	Element getDefiningElement();
	/**
	 * After creating a task, an assignment will be created for each AbstractUser in this expression 
	 */
	OpaqueExpression getPotentialOwners();
	/**
	 * When creating a task this expression will populate the lookup for the business administrator
	 * @return
	 */
	OpaqueExpression getBusinessAdministrators();
	/**
	 * When creating a task this expression will populate the lookup for the stakeholders
	 * @return
	 */
	OpaqueExpression getStakeholders();
	
	Collection<TimeEvent> getDeadlines();
	TaskDelegation getDelegation();
	Collection<Constraint> getConditionEscalations();
	Collection<Constraint> getTimeEscalations(TimeEvent deadline);
	Classifier getExpressionContext();
}
