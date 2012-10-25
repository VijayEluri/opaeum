package org.nakeuml.tinker.collectiontest;

import junit.framework.Assert;

import org.junit.Test;
import org.opaeum.test.tinker.BaseLocalDbTest;
import org.tinker.collectiontest.Fantasy;
import org.tinker.concretetest.God;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class QualifiedOrderedSetTest extends BaseLocalDbTest {

	@Test(expected=IllegalStateException.class)
	public void testOrderedSetIsUnique() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Fantasy fantasy1 = new Fantasy(true);
		fantasy1.setName("fantasy1");
		fantasy1.init(god);
		fantasy1.addToOwningObject();
		god.getFantasy().add(fantasy1);
		db.stopTransaction(Conclusion.SUCCESS);
		God godTest = new God(god.getVertex());
		Assert.assertEquals(1, godTest.getFantasy().size());
	}
	
	@Test
	public void testOrderedSetIsOrdered() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Fantasy fantasy1 = new Fantasy(true);
		fantasy1.setName("fantasy1");
		god.getFantasy().add(fantasy1, god.getQualifierForFantasy(fantasy1));
		Fantasy fantasy2 = new Fantasy(true);
		fantasy2.setName("fantasy2");
		god.getFantasy().add(fantasy2, god.getQualifierForFantasy(fantasy2));
		Fantasy fantasy3 = new Fantasy(true);
		fantasy3.setName("fantasy3");
		god.getFantasy().add(fantasy3, god.getQualifierForFantasy(fantasy3));
		Fantasy fantasy4 = new Fantasy(true);
		fantasy4.setName("fantasy4");
		god.getFantasy().add(fantasy4, god.getQualifierForFantasy(fantasy4));
		db.stopTransaction(Conclusion.SUCCESS);
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getFantasy().size());
		Assert.assertEquals("fantasy1", godTest.getFantasy().get(0).getName());
		Assert.assertEquals("fantasy2", godTest.getFantasy().get(1).getName());
		Assert.assertEquals("fantasy3", godTest.getFantasy().get(2).getName());
		Assert.assertEquals("fantasy4", godTest.getFantasy().get(3).getName());
		db.startTransaction();
		God godTest2 = new God(god.getVertex());
		Fantasy fantasy5 = new Fantasy(true);
		fantasy5.setName("fantasy5");
		godTest2.getFantasy().add(2, fantasy5, god.getQualifierForFantasy(fantasy5));
		db.stopTransaction(Conclusion.SUCCESS);
		God godTest3 = new God(god.getVertex());
		Assert.assertEquals(5, godTest3.getFantasy().size());
		Assert.assertEquals("fantasy1", godTest3.getFantasy().get(0).getName());
		Assert.assertEquals("fantasy2", godTest3.getFantasy().get(1).getName());
		Assert.assertEquals("fantasy5", godTest3.getFantasy().get(2).getName());
		Assert.assertEquals("fantasy3", godTest3.getFantasy().get(3).getName());
		Assert.assertEquals("fantasy4", godTest3.getFantasy().get(4).getName());
	}
	
	
}