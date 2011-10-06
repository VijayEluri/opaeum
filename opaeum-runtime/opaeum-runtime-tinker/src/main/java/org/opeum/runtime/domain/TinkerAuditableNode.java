package org.opeum.runtime.domain;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

public interface TinkerAuditableNode extends TinkerNode {
	void createAuditVertex(boolean createParentVertex);
	Vertex getAuditVertex();
	List<? extends TinkerAuditNode> getAudits();
}
