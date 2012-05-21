package org.nakeduml.runtime.domain.activity.interf;

import org.nakeduml.runtime.domain.activity.ObjectToken;
import org.nakeduml.runtime.domain.activity.ObjectTokenInterator;

public interface IObjectNode<O,IN extends ObjectToken<O>,OUT extends ObjectToken<O>> extends IActivityNode<IN,OUT>, ITypedElement<O> {
	int getUpperBound();
	void addIncomingToken(IN token);
	void addIncomingToken(ObjectTokenInterator<O, IN> iter);
}
