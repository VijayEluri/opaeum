package org.nakeduml.runtime.bpm.taskobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.nakeduml.runtime.bpm.BusinessRole;
import org.nakeduml.runtime.bpm.TaskObject;
import org.nakeduml.runtime.environment.marshall.PropertyValue;
import org.nakeduml.runtime.environment.marshall.Value;
import org.nakeduml.runtime.event.ICallEventHandler;
import org.nakeduml.runtime.persistence.AbstractPersistence;

public class OnForwardedHandler947 implements ICallEventHandler {
	private boolean isEvent;
	private BusinessRole forwardedTo;
	private BusinessRole forwardedBy;
	private Date firstOccurrenceScheduledFor;

	/** Default constructor for OnForwardedHandler947
	 */
	public OnForwardedHandler947() {
	}
	
	/** Constructor for OnForwardedHandler947
	 * 
	 * @param forwardedBy 
	 * @param forwardedTo 
	 * @param isEvent 
	 */
	public OnForwardedHandler947(BusinessRole forwardedBy, BusinessRole forwardedTo, boolean isEvent) {
		this.firstOccurrenceScheduledFor=new Date(System.currentTimeMillis()+1000);
		setForwardedBy(forwardedBy);
		setForwardedTo(forwardedTo);
		this.isEvent=isEvent;
	}

	public int getConsumerPoolSize() {
		return 5;
	}
	
	public Date getFirstOccurrenceScheduledFor() {
		return this.firstOccurrenceScheduledFor;
	}
	
	public BusinessRole getForwardedBy() {
		return this.forwardedBy;
	}
	
	public BusinessRole getForwardedTo() {
		return this.forwardedTo;
	}
	
	public String getHandlerUuid() {
		return "252060@_NdLN8K0OEeCK48ywUpk_rg";
	}
	
	public boolean getIsEvent() {
		return this.isEvent;
	}
	
	public String getQueueName() {
		return "OpiumLibraryForBPM::TaskObject::onForwarded";
	}
	
	public boolean handleOn(Object t) {
		TaskObject target = (TaskObject)t;
		if ( isEvent ) {
			return target.consumeOnForwardedOccurrence(getForwardedBy(),getForwardedTo());
		} else {
			target.onForwarded(getForwardedBy(),getForwardedTo());
			return true;
		}
	}
	
	public Collection<PropertyValue> marshall() {
		Collection<PropertyValue> result = new ArrayList<PropertyValue>();
		result.add(new PropertyValue(949, Value.valueOf(this.getForwardedBy())));
		result.add(new PropertyValue(948, Value.valueOf(this.getForwardedTo())));
		result.add(new PropertyValue(-6, Value.valueOf(isEvent)));
		return result;
	}
	
	public Date scheduleNextOccurrence() {
		return new Date(System.currentTimeMillis() + 1000*60*60*24*10);
	}
	
	public void setForwardedBy(BusinessRole forwardedBy) {
		this.forwardedBy=forwardedBy;
	}
	
	public void setForwardedTo(BusinessRole forwardedTo) {
		this.forwardedTo=forwardedTo;
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
			
				case 948:
					this.setForwardedTo((BusinessRole)Value.valueOf(p.getValue(),persistence));
				break;
			
				case 949:
					this.setForwardedBy((BusinessRole)Value.valueOf(p.getValue(),persistence));
				break;
			
			}
		
		}
	}

}