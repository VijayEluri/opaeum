package org.opaeum.runtime.jpa;

import javax.persistence.EntityManager;

import org.opaeum.runtime.persistence.ConversationalPersistence;

public class StandaloneJpaConversationalPersistence extends AbstractJpaConversationalPersistence implements ConversationalPersistence{
	EntityManager entityManager;
	public StandaloneJpaConversationalPersistence(EntityManager entityManager){
		super();
		this.entityManager = entityManager;
	}
	@Override
	protected EntityManager getEntityManager(){
		return entityManager;
	}
}
