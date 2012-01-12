package org.opaeum.metamodel.bpm;

import java.util.Collection;

import org.opaeum.metamodel.core.INakedClassifier;
import org.opaeum.metamodel.core.INakedElement;
import org.opaeum.metamodel.core.INakedValueSpecification;
import org.opaeum.runtime.domain.TaskDelegation;

public interface INakedResponsibilityDefinition extends INakedElement{
	/**
	 * After creating a task, an assignment will be created for each AbstractUser in this expression 
	 */
	INakedValueSpecification getPotentialOwners();
	/**
	 * When creating a task this expression will populate the lookup for the business administrator
	 * @return
	 */
	INakedValueSpecification getPotentialBusinessAdministrators();
	/**
	 * When creating a task this expression will populate the lookup for the stakeholders
	 * @return
	 */
	INakedValueSpecification getPotentialStakeholders();
	
	Collection<INakedDeadline> getDeadlines();
	TaskDelegation getDelegation();
	INakedClassifier getExpressionContext();
}