package org.nakeduml.runtime.domain.activity.interf;


public interface ICallOperationAction extends ICallAction {
	IInputPin<?, ?> getTarget();
}
