package org.nakeduml.runtime.domain.activity.interf;

import java.util.List;

public interface IInvocationAction extends IAction {
	List<? extends IInputPin<?, ?>> getArgument();
}
