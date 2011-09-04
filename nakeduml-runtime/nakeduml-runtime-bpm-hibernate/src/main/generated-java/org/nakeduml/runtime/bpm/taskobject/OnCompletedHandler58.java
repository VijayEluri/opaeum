package org.nakeduml.runtime.bpm.taskobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.nakeduml.runtime.bpm.BusinessRole;
import org.nakeduml.runtime.bpm.TaskObject;
import org.nakeduml.runtime.environment.marshall.PropertyValue;
import org.nakeduml.runtime.environment.marshall.Value;
import org.nakeduml.runtime.event.IEventHandler;
import org.nakeduml.runtime.persistence.AbstractPersistence;

public class OnCompletedHandler58 implements IEventHandler {
	private BusinessRole completedBy;
	private boolean isEvent;
	private Date firstOccurrenceScheduledFor;

	/** Default constructor for OnCompletedHandler58
	 */
	public OnCompletedHandler58() {
	}
	
	/** Constructor for OnCompletedHandler58
	 * 
	 * @param completedBy 
	 * @param isEvent 
	 */
	public OnCompletedHandler58(BusinessRole completedBy, boolean isEvent) {
		this.firstOccurrenceScheduledFor=new Date(System.currentTimeMillis()+1000);
		setCompletedBy(completedBy);
		this.isEvent=isEvent;
	}

	public BusinessRole getCompletedBy() {
		return this.completedBy;
	}
	
	public int getConsumerPoolSize() {
		return 5;
	}
	
	public Date getFirstOccurrenceScheduledFor() {
		return this.firstOccurrenceScheduledFor;
	}
	
	public String getHandlerUuid() {
		return "55da2d9a_27df_446d_8575_19bea4648d54";
	}
	
	public boolean getIsEvent() {
		return this.isEvent;
	}
	
	public String getQueueName() {
		return "OpiumLibraryForBPM::TaskObject::onCompleted";
	}
	
	public boolean handleOn(Object t) {
		TaskObject target = (TaskObject)t;
		if ( isEvent ) {
			return target.consumeOnCompletedOccurrence(getCompletedBy());
		} else {
			target.onCompleted(getCompletedBy());
			return true;
		}
	}
	
	public Collection<PropertyValue> marshall() {
		Collection<PropertyValue> result = new ArrayList<PropertyValue>();
		result.add(new PropertyValue(59, Value.valueOf(this.getCompletedBy())));
		result.add(new PropertyValue(-6, Value.valueOf(isEvent)));
		return result;
	}
	
	public Date scheduleNextOccurrence() {
		return new Date(System.currentTimeMillis() + 1000*60*60*24*10);
	}
	
	public void setCompletedBy(BusinessRole completedBy) {
		this.completedBy=completedBy;
	}
	
	public void setIsEvent(boolean isEvent) {
		this.isEvent=isEvent;
	}
	
	public void unmarshall(Collection<PropertyValue> ps, AbstractPersistence persistence) {
		for ( PropertyValue p : ps ) {
			switch ( p.getId() ) {
				case -6:
					this.isEvent=(Boolean)Value.valueOf(p.getValue(),persistence);
				break;
			
				case 59:
					this.setCompletedBy((BusinessRole)Value.valueOf(p.getValue(),persistence));
				break;
			
			}
		
		}
	}

}