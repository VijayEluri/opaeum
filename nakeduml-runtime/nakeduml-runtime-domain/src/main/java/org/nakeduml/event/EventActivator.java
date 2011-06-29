package org.nakeduml.event;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.nakeduml.environment.IMessageSender;
import org.nakeduml.runtime.domain.IPersistentObject;

public class EventActivator{
	public void activateTimeEvents(Session session,IMessageSender sender){
		Criteria cri = session.createCriteria(TimeEvent.class);
		cri.add(Restrictions.gt("when", new Date()));
		List<TimeEvent> list = cri.list();
		for(TimeEvent timeEvent:list){
			session.delete(timeEvent);
			sender.sendObjectToQueue(timeEvent, "queue/EventDeliveryQueue");
		}
	}
	public void activateChangeEvents(Session session,IMessageSender sender){
		Criteria cri = session.createCriteria(ChangeEvent.class);
		List<ChangeEvent> list = cri.list();
		for(ChangeEvent changeEvent:list){
			IPersistentObject source = (IPersistentObject) session.get(changeEvent.getEventSourceClass(), changeEvent.getEventSourceId());
			changeEvent.evaluateConditionOn(source);
			if(changeEvent.isTrue()){
				session.delete(changeEvent);
				sender.sendObjectToQueue(changeEvent, "queue/EventDeliveryQueue");
			}
		}
	}
}
