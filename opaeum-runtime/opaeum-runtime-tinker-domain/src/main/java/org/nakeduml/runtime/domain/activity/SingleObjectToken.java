package org.nakeduml.runtime.domain.activity;

import java.util.Arrays;
import java.util.Collection;

import org.nakeduml.runtime.domain.TinkerCompositionNode;
import org.nakeduml.runtime.domain.TinkerNode;
import org.nakeduml.tinker.runtime.GraphDb;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public class SingleObjectToken<O> extends ObjectToken<O> {

	public SingleObjectToken(String edgeName, O object) {
		super(edgeName, object);
		addEdgeToObject(object);
	}

	public SingleObjectToken(String edgeName) {
		super(edgeName);
	}

	public SingleObjectToken(Vertex vertex) {
		super(vertex);
	}

	private void addEdgeToObject(O object) {
		Vertex v;
		if (object instanceof TinkerNode) {
			TinkerNode node = (TinkerNode) object;
			v = node.getVertex();
		} else if (object.getClass().isEnum()) {
			v = GraphDb.getDb().addVertex(null);
			v.setProperty("value", ((Enum<?>) object).name());
		} else {
			v = GraphDb.getDb().addVertex(null);
			v.setProperty("value", object);
		}
		Edge edge = GraphDb.getDb().addEdge(null, this.vertex, v, TOKEN + "toObject");
		edge.setProperty("inClass", object.getClass().getName());
	}

	protected void removeEdgeToObject() {
		O object = getObject();
		Edge edge = this.vertex.getOutEdges(TOKEN + "toObject").iterator().next();
		if (object instanceof TinkerNode) {
			GraphDb.getDb().removeEdge(edge);
		} else if (object.getClass().isEnum()) {
			GraphDb.getDb().removeVertex(edge.getInVertex());
		} else {
			GraphDb.getDb().removeVertex(edge.getInVertex());
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public O getObject() {
		Edge edge = this.vertex.getOutEdges(TOKEN + "toObject").iterator().next();
		Class<?> c = getClassToInstantiate(edge);
		Vertex v = edge.getInVertex();
		O node = null;
		try {
			if (c.isEnum()) {
				Object value = v.getProperty("value");
				node = (O) Enum.valueOf((Class<? extends Enum>) c, (String) value);
			} else if (TinkerCompositionNode.class.isAssignableFrom(c)) {
				node = (O) c.getConstructor(Vertex.class).newInstance(v);
			} else {
				Object value = v.getProperty("value");
				node = (O) value;
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return node;
	}

	private Class<?> getClassToInstantiate(Edge edge) {
		try {
			return Class.forName((String) edge.getProperty("inClass"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public SingleObjectToken<O> duplicate(String flowName) {
		SingleObjectToken<O> objectToken = new SingleObjectToken<O>(flowName, getObject());
		return objectToken;
	}

	@Override
	public void remove() {
		removeEdgeToObject();
		GraphDb.getDb().removeVertex(getVertex());
	}
	
	//TODO think about null token and object tokens that are control tokens
	@Override
	public int getNumberOfElements() {
		return 1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<O> getElements() {
		return Arrays.<O>asList(getObject());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Object Token value = ");
		sb.append(getObject());
		return sb.toString();
	}


}
