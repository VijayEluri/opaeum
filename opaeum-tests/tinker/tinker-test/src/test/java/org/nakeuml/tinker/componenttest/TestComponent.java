package org.nakeuml.tinker.componenttest;

import junit.framework.Assert;

import org.junit.Test;
import org.opaeum.test.tinker.BaseLocalDbTest;
import org.tinker.concretetest.God;
import org.tinker.concretetest.Universe;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestComponent extends BaseLocalDbTest {

	@Test
	public void testComponent() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countEdges());
		Assert.assertEquals(5, countVertices());
	}
	
}
