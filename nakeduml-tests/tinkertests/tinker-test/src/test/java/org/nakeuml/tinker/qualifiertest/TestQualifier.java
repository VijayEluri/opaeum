package org.nakeuml.tinker.qualifiertest;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.nakeduml.test.tinker.BaseLocalDbTest;
import org.tinker.concretetest.God;
import org.tinker.qualifiertest.Nature;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestQualifier extends BaseLocalDbTest {

	@Test(expected=IllegalStateException.class)
	public void testQualifierEnsuresUniqueness() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		
		Nature nature = new Nature(true);
		nature.setName1("nature1");
		nature.setName2("nature2");
		nature.init(god);
		nature.addToOwningObject();
		
		Nature nature2 = new Nature(true);
		nature2.setName1("nature1");
		nature2.setName2("nature2");
		nature2.init(god);
		nature2.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testQualifiedGetter() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		
		Nature nature = new Nature(true);
		nature.setName1("nature1");
		nature.setName2("nature2");
		nature.init(god);
		nature.addToOwningObject();
		
		db.stopTransaction(Conclusion.SUCCESS);
		
		db.startTransaction();
		
		God godTest = new God(god.getVertex());
		Nature natureForQualifier1 = godTest.getNatureForQualifier1("nature1");
		Assert.assertNotNull(natureForQualifier1);
		Assert.assertEquals("nature1", natureForQualifier1.getName1());

		Nature natureForQualifier2 = godTest.getNatureForQualifier1("nature2");
		Assert.assertNull(natureForQualifier2);

		db.stopTransaction(Conclusion.SUCCESS);
	}
	
	@Test
	public void testQualifiedWithNull() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		
		Nature nature = new Nature(true);
		nature.init(god);
		nature.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);
		
		db.startTransaction();
		God godTest = new God(god.getVertex());
		Nature natureForQualifier1 = godTest.getNatureForQualifier1(null);
		Assert.assertNotNull(natureForQualifier1);
		Assert.assertEquals(null, natureForQualifier1.getName1());
		Nature natureForQualifier2 = godTest.getNatureForQualifier1("nature2");
		Assert.assertNull(natureForQualifier2);
		db.stopTransaction(Conclusion.SUCCESS);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testQualifiedWithNullException() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Nature nature = new Nature(true);
		nature.init(god);
		nature.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.init(god);
		nature.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
	}	
	
	@Test
	public void testQualifiedMany() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		
		Nature nature = new Nature(true);
		nature.setName1("name1_0");
		nature.setName2("xxx");
		nature.init(god);
		nature.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.setName1("name1_1");
		nature.setName2("xxx");
		nature.init(god);
		nature.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.setName1("name1_2");
		nature.setName2("xxx");
		nature.init(god);
		nature.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.setName1("name1_3");
		nature.setName2("xxx");
		nature.init(god);
		nature.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.setName1("name1_4");
		nature.setName2("yyy");
		nature.init(god);
		nature.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		God godTest = new God(god.getVertex());
		Set<Nature> natureForQualifier2 = godTest.getNatureForQualifier2("xxx");
		Assert.assertEquals(4, natureForQualifier2.size());
		natureForQualifier2 = godTest.getNatureForQualifier2("yyy");
		Assert.assertEquals(1, natureForQualifier2.size());
		db.stopTransaction(Conclusion.SUCCESS);
	}	

}
