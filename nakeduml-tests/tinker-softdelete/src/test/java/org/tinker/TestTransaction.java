package org.tinker;

import static org.junit.Assert.*;

import org.drools.command.assertion.AssertEquals;
import org.junit.Test;
import org.tinker.God;

import com.orientechnologies.orient.core.id.ORecordId;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.pgm.TransactionalGraph.Mode;
import com.tinkerpop.blueprints.pgm.Vertex;

public class TestTransaction extends BaseTest {

//	@Test
//	public void testTransactionPass() {
//		db.setTransactionMode(Mode.MANUAL);
//		db.startTransaction();
//		God god = new God();
//		ORecordId id = (ORecordId) god.getVertex().getId();
//		db.stopTransaction(Conclusion.SUCCESS);
//		db.startTransaction();
//		Vertex v = db.getVertex(id);
//		assertNotNull(v);
//		God retrievedGod = new God(v);
//		assertNotNull(retrievedGod);
//		db.stopTransaction(Conclusion.SUCCESS);
//		assertEquals(1, countVertices());
//	}
//	
//	@Test
//	public void testTransactionFail() {
//		db.setTransactionMode(Mode.MANUAL);
//		db.startTransaction();
//		God god = new God();
//		ORecordId id = (ORecordId) god.getVertex().getId();
//		db.stopTransaction(Conclusion.FAILURE);
//		db.startTransaction();
//		Vertex v = db.getVertex(id);
//		assertNull(v);
//		db.stopTransaction(Conclusion.SUCCESS);
//		assertEquals(0, countVertices());
//	}
	
}