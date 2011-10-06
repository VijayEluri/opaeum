package org.opeum.runtime.bpm.businesscalendar.businesscalendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.opeum.runtime.bpm.businesscalendar.BusinessCalendar;
import org.opeum.runtime.bpm.businesscalendar.BusinessTimeUnit;
import org.opeum.runtime.environment.marshall.PropertyValue;
import org.opeum.runtime.environment.marshall.Value;
import org.opeum.runtime.event.ICallEventHandler;
import org.opeum.runtime.persistence.AbstractPersistence;

public class AddTimeToHandler155 implements ICallEventHandler {
	private Date result;
	private boolean isEvent;
	private BusinessTimeUnit timeUnit;
	private Date firstOccurrenceScheduledFor;
	private Date fromDateTime;
	private Double numberOfUnits;

	/** Default constructor for AddTimeToHandler155
	 */
	public AddTimeToHandler155() {
	}
	
	/** Constructor for AddTimeToHandler155
	 * 
	 * @param fromDateTime 
	 * @param timeUnit 
	 * @param numberOfUnits 
	 * @param result 
	 * @param isEvent 
	 */
	public AddTimeToHandler155(Date fromDateTime, BusinessTimeUnit timeUnit, Double numberOfUnits, Date result, boolean isEvent) {
		this.firstOccurrenceScheduledFor=new Date(System.currentTimeMillis()+1000);
		setFromDateTime(fromDateTime);
		setTimeUnit(timeUnit);
		setNumberOfUnits(numberOfUnits);
		setResult(result);
		this.isEvent=isEvent;
	}

	public int getConsumerPoolSize() {
		return 5;
	}
	
	public Date getFirstOccurrenceScheduledFor() {
		return this.firstOccurrenceScheduledFor;
	}
	
	public Date getFromDateTime() {
		return this.fromDateTime;
	}
	
	public String getHandlerUuid() {
		return "252060@_NTccANcEEeCJ0dmaHEVVnw";
	}
	
	public boolean getIsEvent() {
		return this.isEvent;
	}
	
	public Double getNumberOfUnits() {
		return this.numberOfUnits;
	}
	
	public String getQueueName() {
		return "OpeumLibraryForBPM::businesscalendar::BusinessCalendar::addTimeTo";
	}
	
	public Date getResult() {
		return this.result;
	}
	
	public BusinessTimeUnit getTimeUnit() {
		return this.timeUnit;
	}
	
	public boolean handleOn(Object t) {
		BusinessCalendar target = (BusinessCalendar)t;
		if ( isEvent ) {
			return target.consumeAddTimeToOccurrence(getFromDateTime(),getTimeUnit(),getNumberOfUnits(),getResult());
		} else {
			target.addTimeTo(getFromDateTime(),getTimeUnit(),getNumberOfUnits(),getResult());
			return true;
		}
	}
	
	public Collection<PropertyValue> marshall() {
		Collection<PropertyValue> result = new ArrayList<PropertyValue>();
		result.add(new PropertyValue(157, Value.valueOf(this.getFromDateTime())));
		result.add(new PropertyValue(159, Value.valueOf(this.getTimeUnit())));
		result.add(new PropertyValue(156, Value.valueOf(this.getNumberOfUnits())));
		result.add(new PropertyValue(158, Value.valueOf(this.getResult())));
		result.add(new PropertyValue(-6, Value.valueOf(isEvent)));
		return result;
	}
	
	public Date scheduleNextOccurrence() {
		return new Date(System.currentTimeMillis() + 1000*60*60*24*10);
	}
	
	public void setFromDateTime(Date fromDateTime) {
		this.fromDateTime=fromDateTime;
	}
	
	public void setIsEvent(boolean isEvent) {
		this.isEvent=isEvent;
	}
	
	public void setNumberOfUnits(Double numberOfUnits) {
		this.numberOfUnits=numberOfUnits;
	}
	
	public void setResult(Date result) {
		this.result=result;
	}
	
	public void setTimeUnit(BusinessTimeUnit timeUnit) {
		this.timeUnit=timeUnit;
	}
	
	public void unmarshall(Collection<PropertyValue> ps, AbstractPersistence persistence) {
		for ( PropertyValue p : ps ) {
			switch ( p.getId() ) {
				case -6:
					this.isEvent=(Boolean)Value.valueOf(p.getValue(),persistence);
				break;
			
				case 158:
					this.setResult((Date)Value.valueOf(p.getValue(),persistence));
				break;
			
				case 156:
					this.setNumberOfUnits((Double)Value.valueOf(p.getValue(),persistence));
				break;
			
				case 159:
					this.setTimeUnit((BusinessTimeUnit)Value.valueOf(p.getValue(),persistence));
				break;
			
				case 157:
					this.setFromDateTime((Date)Value.valueOf(p.getValue(),persistence));
				break;
			
			}
		
		}
	}

}