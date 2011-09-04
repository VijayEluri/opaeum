package org.nakeduml.runtime.bpm.abstractrequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.nakeduml.runtime.bpm.AbstractRequest;
import org.nakeduml.runtime.bpm.Participant;
import org.nakeduml.runtime.bpm.RequestParticipationKind;
import org.nakeduml.runtime.environment.marshall.PropertyValue;
import org.nakeduml.runtime.environment.marshall.Value;
import org.nakeduml.runtime.event.IEventHandler;
import org.nakeduml.runtime.persistence.AbstractPersistence;

public class RemoveRequestParticipantHandler87 implements IEventHandler {
	private Participant participant;
	private RequestParticipationKind kind;
	private boolean isEvent;
	private Date firstOccurrenceScheduledFor;

	/** Default constructor for RemoveRequestParticipantHandler87
	 */
	public RemoveRequestParticipantHandler87() {
	}
	
	/** Constructor for RemoveRequestParticipantHandler87
	 * 
	 * @param participant 
	 * @param kind 
	 * @param isEvent 
	 */
	public RemoveRequestParticipantHandler87(Participant participant, RequestParticipationKind kind, boolean isEvent) {
		this.firstOccurrenceScheduledFor=new Date(System.currentTimeMillis()+1000);
		setParticipant(participant);
		setKind(kind);
		this.isEvent=isEvent;
	}

	public int getConsumerPoolSize() {
		return 5;
	}
	
	public Date getFirstOccurrenceScheduledFor() {
		return this.firstOccurrenceScheduledFor;
	}
	
	public String getHandlerUuid() {
		return "048f13d7_c73d_43ff_a6de_f05423518ae1";
	}
	
	public boolean getIsEvent() {
		return this.isEvent;
	}
	
	public RequestParticipationKind getKind() {
		return this.kind;
	}
	
	public Participant getParticipant() {
		return this.participant;
	}
	
	public String getQueueName() {
		return "OpiumLibraryForBPM::AbstractRequest::removeRequestParticipant";
	}
	
	public boolean handleOn(Object t) {
		AbstractRequest target = (AbstractRequest)t;
		if ( isEvent ) {
			return target.consumeRemoveRequestParticipantOccurrence(getParticipant(),getKind());
		} else {
			target.removeRequestParticipant(getParticipant(),getKind());
			return true;
		}
	}
	
	public Collection<PropertyValue> marshall() {
		Collection<PropertyValue> result = new ArrayList<PropertyValue>();
		result.add(new PropertyValue(89, Value.valueOf(this.getParticipant())));
		result.add(new PropertyValue(88, Value.valueOf(this.getKind())));
		result.add(new PropertyValue(-6, Value.valueOf(isEvent)));
		return result;
	}
	
	public Date scheduleNextOccurrence() {
		return new Date(System.currentTimeMillis() + 1000*60*60*24*10);
	}
	
	public void setIsEvent(boolean isEvent) {
		this.isEvent=isEvent;
	}
	
	public void setKind(RequestParticipationKind kind) {
		this.kind=kind;
	}
	
	public void setParticipant(Participant participant) {
		this.participant=participant;
	}
	
	public void unmarshall(Collection<PropertyValue> ps, AbstractPersistence persistence) {
		for ( PropertyValue p : ps ) {
			switch ( p.getId() ) {
				case -6:
					this.isEvent=(Boolean)Value.valueOf(p.getValue(),persistence);
				break;
			
				case 88:
					this.setKind((RequestParticipationKind)Value.valueOf(p.getValue(),persistence));
				break;
			
				case 89:
					this.setParticipant((Participant)Value.valueOf(p.getValue(),persistence));
				break;
			
			}
		
		}
	}

}