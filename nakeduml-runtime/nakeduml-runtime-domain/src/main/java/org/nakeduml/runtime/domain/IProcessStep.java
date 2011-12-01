package org.nakeduml.runtime.domain;


public interface IProcessStep extends IEnum {
	public long getId();
	public String getHumanName();
	public TriggerMethod[] getTriggerMethods();
}