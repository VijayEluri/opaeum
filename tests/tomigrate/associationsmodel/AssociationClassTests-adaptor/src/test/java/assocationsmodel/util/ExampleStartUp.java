package assocationsmodel.util;

import assocationsmodel.root.Root;
import assocationsmodel.root.RootDataGenerator;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.jboss.seam.persistence.transaction.DefaultTransaction;
import org.jboss.seam.persistence.transaction.SeamTransaction;
import org.opaeum.runtime.adaptor.DataGeneratorProperty;
import org.opaeum.seam3.persistence.DependentScopedSession;

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
	private RootDataGenerator rootDataGenerator;


	public void start() {
		try {
			Root theRoot = (Root)session.createQuery("from Root a where a.name = :name").setText("name", dataGeneratorProperty.getProperty("root.name_0")).uniqueResult();
			transaction.begin();
			if ( theRoot == null ) {
				List<Root> roots = rootDataGenerator.createRoot();
				for ( Root root : roots ) {
					session.persist(root);
				}
				rootDataGenerator.populateRoot(roots);
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