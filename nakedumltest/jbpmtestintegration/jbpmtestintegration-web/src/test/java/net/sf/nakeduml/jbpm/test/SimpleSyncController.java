package net.sf.nakeduml.jbpm.test;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import jbpm.jbpm.Application;
import jbpm.jbpm.application.SimpleSync1;
import jbpm.jbpm.application.SimpleSync1State;

import org.hibernate.Session;
import org.junit.Assert;

public class SimpleSyncController {

	@Inject
	private Session session;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void testProcess() {
		List<Application> roots = session.createQuery("select h from Application h").list();
		Assert.assertTrue(roots.size()>0);
		Application app = roots.get(0);
		SimpleSync1 p = app.SimpleSync1(app.getCustomer().iterator().next());
		p.execute();
		Assert.assertTrue( p.isStepActive(SimpleSync1State.JOINNODE1) );
		Assert.assertTrue( p.isStepActive(SimpleSync1State.SHIP) );
		session.flush();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doOperationTest() {
		List<Application> roots = session.createQuery("select h from Application h").list();
		Assert.assertTrue(roots.size()>0);
		Application app = roots.get(0);
		SimpleSync1 p = app.getSimpleSync1().get(0);
		p.Operation1();
		assert p.isStepActive(SimpleSync1State.ACTIVITYFINALNODE1);			
		session.flush();
	}
	
}
