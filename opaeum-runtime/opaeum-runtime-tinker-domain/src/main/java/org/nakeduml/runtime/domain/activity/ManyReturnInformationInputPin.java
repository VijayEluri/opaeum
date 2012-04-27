package org.nakeduml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ManyReturnInformationInputPin<O> extends ReturnInformationInputPin<O,CollectionObjectToken<O>> {

	public ManyReturnInformationInputPin() {
		super();
	}

	public ManyReturnInformationInputPin(boolean persist, String name) {
		super(persist, name);
	}

	public ManyReturnInformationInputPin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract ReplyAction getAction();
	
	@Override
	protected int countNumberOfElementsOnTokens() {
		int size = 0;
		List<CollectionObjectToken<O>> tokens = getInTokens();
		for (CollectionObjectToken<O> collectionObjectToken : tokens) {
			size += collectionObjectToken.getElements().size();
		}
		return size;
	}	
	
}
