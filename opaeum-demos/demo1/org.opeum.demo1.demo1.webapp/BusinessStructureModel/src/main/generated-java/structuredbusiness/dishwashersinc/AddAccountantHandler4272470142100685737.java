package structuredbusiness.dishwashersinc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.opaeum.annotation.PropertyMetaInfo;
import org.opaeum.runtime.environment.SimpleTypeRuntimeStrategyFactory;
import org.opaeum.runtime.environment.marshall.PropertyValue;
import org.opaeum.runtime.environment.marshall.Value;
import org.opaeum.runtime.event.ICallEventHandler;
import org.opaeum.runtime.persistence.AbstractPersistence;

import structuredbusiness.DishwashersInc;
import structuredbusiness.Manager;

public class AddAccountantHandler4272470142100685737 implements ICallEventHandler {
	private Date firstOccurrenceScheduledFor;
	private Boolean isChartered;
	private boolean isEvent;
	private Manager manager;
	private String name;

	/** Constructor for AddAccountantHandler4272470142100685737
	 * 
	 * @param name 
	 * @param isChartered 
	 * @param manager 
	 * @param isEvent 
	 */
	public AddAccountantHandler4272470142100685737(String name, Boolean isChartered, Manager manager, boolean isEvent) {
		this.firstOccurrenceScheduledFor=new Date(System.currentTimeMillis()+1000);
		setName(name);
		setChartered(isChartered);
		setManager(manager);
		this.isEvent=isEvent;
	}
	
	/** Default constructor for AddAccountantHandler4272470142100685737
	 */
	public AddAccountantHandler4272470142100685737() {
	}

	public int getConsumerPoolSize() {
		return 5;
	}
	
	public Date getFirstOccurrenceScheduledFor() {
		return this.firstOccurrenceScheduledFor;
	}
	
	public String getHandlerUuid() {
		return "914890@_FGOJ8H4bEeGW5bASaRr7SQ";
	}
	
	public boolean getIsEvent() {
		return this.isEvent;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,lookupMethod="getSourcePopulationForAddAccountantManager",opaeumId=3326746136356078816l,uuid="914890@_RA5zQH4bEeGW5bASaRr7SQ")
	public Manager getManager() {
		return this.manager;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=1698496834697340522l,strategyFactory=SimpleTypeRuntimeStrategyFactory.class,uuid="914890@_HmRE0H4bEeGW5bASaRr7SQ")
	public String getName() {
		return this.name;
	}
	
	public String getQueueName() {
		return "structuredbusiness::DishwashersInc::addAccountant";
	}
	
	public boolean handleOn(Object t) {
		DishwashersInc target = (DishwashersInc)t;
		if ( isEvent ) {
			return target.consumeAddAccountantOccurrence(getName(),isChartered(),getManager());
		} else {
			target.addAccountant(getName(),isChartered(),getManager());
			return true;
		}
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=7742455353317600026l,strategyFactory=SimpleTypeRuntimeStrategyFactory.class,uuid="914890@_MWWvsH4bEeGW5bASaRr7SQ")
	public Boolean isChartered() {
		return this.isChartered;
	}
	
	public Collection<PropertyValue> marshall() {
		Collection<PropertyValue> result = new ArrayList<PropertyValue>();
		result.add(new PropertyValue(341190338248797855l, Value.valueOf(this.getName())));
		result.add(new PropertyValue(9099761849766142693l, Value.valueOf(this.isChartered())));
		result.add(new PropertyValue(-6l, Value.valueOf(isEvent)));
		result.add(new PropertyValue(4684052632804621483l, Value.valueOf(this.getManager())));
		return result;
	}
	
	public Date scheduleNextOccurrence() {
		return new Date(System.currentTimeMillis() + 1000*60*60*24*10);
	}
	
	public void setChartered(Boolean isChartered) {
		this.isChartered=isChartered;
	}
	
	public void setIsEvent(boolean isEvent) {
		this.isEvent=isEvent;
	}
	
	public void setManager(Manager manager) {
		this.manager=manager;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void unmarshall(Collection<PropertyValue> ps, AbstractPersistence persistence) {
		for ( PropertyValue p : ps ) {
			if ( p.getId()==341190338248797855l ) {
				this.setName((String)Value.valueOf(p.getValue(),persistence));
			} else {
				if ( p.getId()==9099761849766142693l ) {
					this.setChartered((Boolean)Value.valueOf(p.getValue(),persistence));
				} else {
					if ( p.getId()==4684052632804621483l ) {
						this.setManager((Manager)Value.valueOf(p.getValue(),persistence));
					} else {
					
					}
				}
			}
			if ( p.getId()==-6l ) {
				this.isEvent=(Boolean)Value.valueOf(p.getValue(),persistence);
			}
		}
	}

}