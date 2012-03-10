package org.nakeduml.runtime.domain.activity;

import org.nakeduml.runtime.domain.TinkerNode;
import org.nakeduml.tinker.runtime.GraphDb;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class Event implements TinkerNode {

	private static final long serialVersionUID = 546409593170432165L;
	protected Vertex vertex;

	public Event(Vertex vertex) {
		super();
		this.vertex = vertex;
	}

	public Event(String name) {
		super();
		this.vertex = GraphDb.getDb().addVertex(null);
		this.vertex.setProperty("name", name);
	}

	public Vertex getVertex() {
		return vertex;
	}
	
	public String getName() {
		return (String) this.vertex.getProperty("name");
	}

	@Override
	public boolean isTinkerRoot() {
		return false;
	}

	@Override
	public Long getId() {
		return null;
	}

	@Override
	public void setId(Long id) {
	}

	@Override
	public String getUid() {
		return null;
	}

	@Override
	public int getObjectVersion() {
		return 0;
	}
	
}
