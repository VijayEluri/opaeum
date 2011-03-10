package org.nakeduml.util;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import jbpm.jbpm.Application;
import jbpm.jbpm.ApplicationDataGenerator;

import org.hibernate.Session;
import org.jboss.seam.persistence.transaction.DefaultTransaction;
import org.jboss.seam.persistence.transaction.SeamTransaction;
import org.jboss.seam.servlet.WebApplication;
import org.jboss.seam.servlet.event.Started;
import org.nakeduml.runtime.adaptor.DataGeneratorProperty;
import org.nakeduml.seam3.persistence.DependentScopedSession;

public class ExampleStartUp {
	@Inject
	@DependentScopedSession
	private Session session;
	@Inject
	@DefaultTransaction
	private SeamTransaction transaction;
	@Inject
	private DataGeneratorProperty dataGeneratorProperty;
	@Inject
	private ApplicationDataGenerator rootDataGenerator;


	public void start(@Observes @Started WebApplication webapp) {
		try {
			Application theApplication = (Application)session.createQuery("from Application a where a.name = :name").setText("name", dataGeneratorProperty.getProperty("application.name_0")).uniqueResult();
			transaction.begin();
			if ( theApplication == null ) {
				List<Application> applications = rootDataGenerator.createApplication();
				for ( Application application : applications ) {
					session.persist(application);
				}
				rootDataGenerator.populateApplication(applications);
				session.flush();
				transaction.commit();
			}
		} catch (Exception e) {
			try {
				transaction.rollback();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		}
	}

}