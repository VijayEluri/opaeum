package org.nakeduml.runtime.domain;

import java.io.Serializable;
import java.util.List;

import org.nakeduml.tinker.collection.TinkerSequence;
import org.nakeduml.tinker.collection.TinkerSequenceImpl;
import org.nakeduml.tinker.runtime.GraphDb;
import org.opaeum.runtime.domain.ISignal;
import org.opaeum.runtime.domain.IntrospectionUtil;

import com.tinkerpop.blueprints.pgm.Edge;

public abstract class BaseTinkerBehavioredClassifier extends BaseTinkerSoftDelete implements Serializable, TinkerCompositionNode {

	private static final long serialVersionUID = 228929853082097254L;
	private TinkerSequence<ISignal> events;

	public BaseTinkerBehavioredClassifier() {
		super();
	}

	public abstract void receiveSignal(ISignal signal);
	
	protected void attachToRoot() {
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "classifierBehavior");
		edge.setProperty("inClass", IntrospectionUtil.getOriginalClass(this.getClass()).getName());
		this.events = new TinkerSequenceImpl<ISignal>(this, "eventPool", "uid", true, false, true);
	}

	public List<ISignal> getEventPool() {
		List<ISignal> result = this.events;
		return result;
	}
	
	public void addToEventPool(ISignal signal) {
		if ( signal!=null ) {
			this.events.add(signal);
		}
	}
	
	public void removeFromEventPool(ISignal signal) {
		if ( signal!=null ) {
			this.events.remove(signal);
		}
	}
	
}