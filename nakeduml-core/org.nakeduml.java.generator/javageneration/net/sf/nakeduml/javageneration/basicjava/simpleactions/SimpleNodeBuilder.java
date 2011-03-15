package net.sf.nakeduml.javageneration.basicjava.simpleactions;

import net.sf.nakeduml.javageneration.basicjava.AbstractNodeBuilder;
import net.sf.nakeduml.javageneration.basicjava.AbstractObjectNodeExpressor;
import net.sf.nakeduml.metamodel.activities.INakedActivityNode;
import nl.klasse.octopus.oclengine.IOclEngine;

import org.nakeduml.java.metamodel.OJBlock;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;

public abstract class SimpleNodeBuilder<E extends INakedActivityNode> extends AbstractNodeBuilder {
	protected E node;
	public SimpleNodeBuilder(IOclEngine oclEngine, E action, AbstractObjectNodeExpressor objectNodeExpressor) {
		super(oclEngine, objectNodeExpressor);
		this.node = action;
	}

	public abstract void implementActionOn(OJAnnotatedOperation operation, OJBlock block);
}