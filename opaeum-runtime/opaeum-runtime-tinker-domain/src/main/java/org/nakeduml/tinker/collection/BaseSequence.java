package org.nakeduml.tinker.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.nakeduml.runtime.domain.TinkerAuditableNode;
import org.nakeduml.runtime.domain.TinkerCompositionNode;
import org.nakeduml.runtime.domain.TinkerNode;
import org.nakeduml.tinker.runtime.GraphDb;
import org.nakeduml.tinker.runtime.NakedTinkerIndex;

import com.tinkerpop.blueprints.pgm.CloseableSequence;
import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class BaseSequence<E> extends BaseCollection<E> implements TinkerSequence<E> {

	// protected List<E> internalList = new ArrayList<E>();
	protected NakedTinkerIndex<Edge> index;

	protected List<E> getInternalList() {
		return (List<E>) this.internalCollection;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void loadFromVertex() {
		CloseableSequence<Edge> edges = this.index.queryList(0F, true, false);
		for (Edge edge : edges) {
			E node = null;
			try {
				Class<?> c = this.getClassToInstantiate(edge);
				Object value = this.getVertexForDirection(edge).getProperty("value");
				if (c.isEnum()) {
					node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
					this.internalVertexMap.put(value, this.getVertexForDirection(edge));
				} else if (TinkerNode.class.isAssignableFrom(c)) {
					node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
				} else {
					node = (E) value;
					this.internalVertexMap.put(value, this.getVertexForDirection(edge));
				}
				this.getInternalList().add(node);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		this.loaded = true;
	}

	protected Edge addToListAndListIndex(int indexOf, E e) {
		E previous = this.getInternalList().get(indexOf - 1);
		E current = this.getInternalList().get(indexOf);
		this.getInternalList().add(indexOf, e);
		Edge edge = addInternal(e);

		float min;
		float max;
		if (e instanceof TinkerCompositionNode) {
			min = (Float) ((TinkerCompositionNode) previous).getVertex().getProperty("tinkerIndex");
			max = (Float) ((TinkerCompositionNode) current).getVertex().getProperty("tinkerIndex");
		} else if (e.getClass().isEnum()) {
			min = (Float) this.internalVertexMap.get(((Enum<?>) previous).name()).getProperty("tinkerIndex");
			max = (Float) this.internalVertexMap.get(((Enum<?>) current).name()).getProperty("tinkerIndex");
		} else {
			min = (Float) this.internalVertexMap.get(previous).getProperty("tinkerIndex");
			max = (Float) this.internalVertexMap.get(current).getProperty("tinkerIndex");
		}
		float tinkerIndex = (min + max) / 2;
		this.index.put("index", tinkerIndex, edge);
		getVertexForDirection(edge).setProperty("tinkerIndex", tinkerIndex);
		return edge;
	}

	@Override
	public boolean remove(Object o) {
		maybeLoad();
		int indexOf = this.getInternalList().indexOf(o);
		boolean result = this.getInternalList().remove(o);
		if (result) {
			@SuppressWarnings("unchecked")
			E e = (E)o;
			Vertex v;
			if (o instanceof TinkerCompositionNode) {
				TinkerCompositionNode node = (TinkerCompositionNode) o;
				v = node.getVertex();
				Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.label);
				for (Edge edge : edges) {
					removeEdgefromIndex(v, edge, indexOf);
					GraphDb.getDb().removeEdge(edge);
					if (o instanceof TinkerAuditableNode) {
						createAudit(e, v, true);
					}
					break;
				}
			} else if (o.getClass().isEnum()) {
				v = this.internalVertexMap.get(((Enum<?>) o).name());
				Edge edge = v.getInEdges(this.label).iterator().next();
				removeEdgefromIndex(v, edge, indexOf);
				GraphDb.getDb().removeVertex(v);
			} else {
				v = this.internalVertexMap.get(o);
				Edge edge = v.getInEdges(this.label).iterator().next();
				removeEdgefromIndex(v, edge, indexOf);
				if (o instanceof TinkerAuditableNode) {
					createAudit(e, v, true);
				}
				GraphDb.getDb().removeVertex(v);
			}
		}
		return result;
	}

	@Override
	public E get(int index) {
		if (!this.loaded) {
			loadFromVertex();
		}
		return this.getInternalList().get(index);
	}

	@Override
	public E remove(int index) {
		E e = this.get(index);
		this.remove(e);
		return e;
	}

	@Override
	public void clear() {
		maybeLoad();
		for (E e : new ArrayList<E>(this.getInternalList())) {
			this.remove(e);
		}
	}

	@Override
	public int indexOf(Object o) {
		maybeLoad();
		return this.getInternalList().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		maybeLoad();
		return this.getInternalList().lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		maybeLoad();
		return this.getInternalList().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		maybeLoad();
		return this.getInternalList().listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		maybeLoad();
		return this.getInternalList().subList(fromIndex, toIndex);
	}

	protected abstract void removeEdgefromIndex(Vertex v, Edge edge, int indexOf);
}
