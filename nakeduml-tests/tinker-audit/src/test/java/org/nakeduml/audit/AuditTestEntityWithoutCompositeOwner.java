package org.nakeduml.audit;

import org.audittest.Finger;
import org.audittest.Hand;
import static org.junit.Assert.*;
import org.junit.Test;
import org.tinker.BaseLocalDbTest;

import com.orientechnologies.orient.core.exception.OTransactionException;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class AuditTestEntityWithoutCompositeOwner extends BaseLocalDbTest {
	
	@Test(expected=OTransactionException.class)
	public void test() {
		db.startTransaction();
		Hand hand1 = new Hand();
		hand1.setName("hand1");
		Finger finger1 = new Finger(hand1);
		finger1.setName("finger1");
		Finger finger2 = new Finger(hand1);
		finger2.setName("finger2");
		finger2.setHand(null);
		db.stopTransaction(Conclusion.SUCCESS);
	}
	
	@Test(expected=OTransactionException.class)
	public void test2() {
		db.startTransaction();
		Hand hand1 = new Hand();
		hand1.setName("hand1");
		Finger finger1 = new Finger(hand1);
		finger1.setName("finger1");
		Finger finger2 = new Finger(hand1);
		finger2.setName("finger2");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(6, countVertices());
		assertEquals(7, countEdges());
		db.startTransaction();
		finger2.setHand(null);
		db.stopTransaction(Conclusion.SUCCESS);
	}	

}
