package org.nakeduml.seam3.persistence;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.nakeduml.environment.Environment;
import org.nakeduml.hibernate.domain.PostgresDialect;

@Startup
@ApplicationScoped
public class ManagedHibernateSessionFactoryProvider {

	private SessionFactory sessionFactory;

	@PostConstruct
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void init() {
		Configuration config = new Configuration();
		config.configure(Environment.loadProperties().getProperty(Environment.HIBERNATE_CONFIG_NAME));
		try {
			//TODO test if the dialect is indeed postgress
			config.getTypeResolver().registerTypeOverride(PostgresDialect.PostgresqlMateralizedBlobType.INSTANCE);
			this.sessionFactory = config.buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	@PreDestroy
	public void close() {
		this.sessionFactory.close();
	}

}