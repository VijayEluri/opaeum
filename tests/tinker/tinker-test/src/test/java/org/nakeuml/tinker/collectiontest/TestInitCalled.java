package org.nakeuml.tinker.collectiontest;

import junit.framework.Assert;

import org.junit.Test;
import org.opaeum.test.tinker.BaseLocalDbTest;
import org.tinker.concretetest.God;
import org.tinker.inheritencetest.Biped;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestInitCalled extends BaseLocalDbTest {

	@Test
	public void testInitCalled() {
		db.startTransaction();
		God god = new God(true);
		god.setName("God1");
		Biped biped = new Biped(true);
		god.getAbstractSpecies().add(biped);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countEdges());
		Assert.assertEquals("thisisdodge", biped.getName());
	}
	
}
