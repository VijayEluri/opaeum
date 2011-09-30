package org.opeum.audit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.audittest.FatNail;
import org.audittest.Finger;
import org.audittest.Hand;
import org.audittest.HandAudit;
import org.audittest.Nail;
import org.junit.Test;
import org.opeum.test.tinker.BaseLocalDbTest;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class AuditTestGeneration extends BaseLocalDbTest {

	@Test
	public void testAuditGod() {
		db.startTransaction();
		Hand hand = new Hand();
		hand.setName("THEHAND");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(1,hand.getAudits().size());
		assertTrue(hand.getAudits().iterator().next() instanceof HandAudit);
		assertEquals("THEHAND",hand.getAudits().iterator().next().getName());
		assertEquals(2, countVertices());
		assertEquals(1, countEdges());
	}
	
	@Test
	public void testAuditCompositeGodAndUniversesInOneTransaction() {
		db.startTransaction();
		Hand hand = new Hand();
		hand.setName("THEGOD");
		Finger universe1 = new Finger(hand);
		universe1.setName("universe1");
		Finger universe2 = new Finger(hand);
		universe2.setName("universe2");
		Finger universe3 = new Finger(hand);
		universe3.setName("universe3");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(8, countVertices());
		assertEquals(10, countEdges());
	}
	
	@Test
	public void testAuditCompositeGodAndUniversesInTwoTransaction() {
		db.startTransaction();
		Hand hand = new Hand();
		hand.setName("THEGOD");
		db.stopTransaction(Conclusion.SUCCESS);
		db.startTransaction();
		Finger finger1 = new Finger(hand);
		finger1.setName("universe1");
		Finger finger2 = new Finger(hand);
		finger2.setName("universe2");
		Finger finger3 = new Finger(hand);
		finger3.setName("universe3");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(9, countVertices());
		assertEquals(11, countEdges());
	}
	
	@Test
	public void testAuditCompositeGodAndUniversesInThreeTransaction() {
		db.startTransaction();
		Hand hand = new Hand();
		hand.setName("THEHAND");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(2, countVertices());
		assertEquals(1, countEdges());		
		db.startTransaction();
		Finger finger1 = new Finger(hand);
		finger1.setName("finger1");
		Finger finger2 = new Finger(hand);
		finger2.setName("finger2");
		Finger finger3 = new Finger(hand);
		finger3.setName("finger3");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(9, countVertices());
		assertEquals(11, countEdges());
		db.startTransaction();
		Finger finger4 = new Finger(hand);
		finger4.setName("finger4");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(12, countVertices());
		assertEquals(15, countEdges());
		db.startTransaction();
		finger4.setName("finger41");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(13, countVertices());
		assertEquals(16, countEdges());
		db.startTransaction();
		assertEquals("finger4", finger4.getAudits().get(1).getPreviousAuditEntry().getName());
		assertEquals("THEHAND", finger4.getAudits().get(1).getPreviousAuditEntry().getHand().getName());
		db.stopTransaction(Conclusion.SUCCESS);
	}
	
	@Test
	public void testAuditCompositeHandFingerAndNail() {
		db.startTransaction();
		Hand hand = new Hand();
		hand.setName("THEHAND");
		Finger finger = new Finger(hand);
		finger.setName("finger");
		Nail nail = new FatNail(finger);
		nail.setName("fatnail");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(6, countVertices());
		assertEquals(7, countEdges());		
	}
	
	@Test
	public void testMovingFinger() {
		db.startTransaction();
		Hand hand1 = new Hand();
		hand1.setName("hand1");
		Finger finger1 = new Finger(hand1);
		finger1.setName("finger1");
		Hand hand2 = new Hand();
		hand2.setName("hand2");
		Finger finger2 = new Finger(hand2);
		finger2.setName("finger2");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(8, countVertices());
		assertEquals(8, countEdges());		
		db.startTransaction();
		finger1.setHand(hand2);
		finger2.setHand(hand1);
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(12, countVertices());
		assertEquals(16, countEdges());		
	}
	
	@Test
	public void testMovingFingerTwice() {
		db.startTransaction();
		Hand hand1 = new Hand();
		hand1.setName("hand1");
		Finger finger1 = new Finger(hand1);
		finger1.setName("finger1");
		Hand hand2 = new Hand();
		hand2.setName("hand2");
		Finger finger2 = new Finger(hand2);
		finger2.setName("finger2");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(8, countVertices());
		assertEquals(8, countEdges());		
		db.startTransaction();
		finger1.setHand(hand2);
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(11, countVertices());
		assertEquals(13, countEdges());
		assertEquals(2, hand2.getFinger().size());
		assertEquals(0, hand1.getFinger().size());
	}
	
}
