package org.opaeum.runtime.bpm.request;

import org.opaeum.runtime.domain.IProcessStep;
import org.opaeum.runtime.domain.TriggerMethod;

public enum AbstractRequestState implements IProcessStep {
;
	private String humanName;
	private long id;
	private IProcessStep parentState;
	private TriggerMethod[] triggerMethods;
	private String uuid;
	/** Constructor for AbstractRequestState
	 * 
	 * @param parentState 
	 * @param uuid 
	 * @param id 
	 * @param humanName 
	 * @param triggerMethods 
	 */
	private AbstractRequestState(IProcessStep parentState, String uuid, long id, String humanName, TriggerMethod[] triggerMethods) {
		this.parentState=parentState;
		this.uuid=uuid;
		this.id=id;
		this.humanName=humanName;
		this.triggerMethods=triggerMethods;
	}

	public String getHumanName() {
		return this.humanName;
	}
	
	public long getId() {
		return this.id;
	}
	
	public IProcessStep getParentState() {
		return this.parentState;
	}
	
	public TriggerMethod[] getTriggerMethods() {
		return this.triggerMethods;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	static public AbstractRequestState resolveById(long id) {
		for ( AbstractRequestState s : values() ) {
			if ( s.getId()==id ) {
				return s;
			}
		}
		return null;
	}

}