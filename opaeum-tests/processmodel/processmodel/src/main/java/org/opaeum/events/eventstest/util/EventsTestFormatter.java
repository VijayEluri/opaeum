package org.opaeum.events.eventstest.util;

import org.opeum.runtime.domain.AbstractFormatter;

public class EventsTestFormatter extends AbstractFormatter implements IEventsTestFormatter {
	static final private ThreadLocal<EventsTestFormatter> INSTANCE = new ThreadLocal<EventsTestFormatter>();


	static public EventsTestFormatter getInstance() {
		EventsTestFormatter result = INSTANCE.get();
		if ( result==null ) {
			INSTANCE.set(result=new EventsTestFormatter());
		}
		return result;
	}

}