package org.opaeum.runtime.bpm.businesscalendar;

import org.junit.Test;

public class WorkDayTest implements WorkDayTestContract {



	@Test
	public void testminutesPerDayInitialValue() {
		BusinessCalendar parent = new BusinessCalendar();
		WorkDay object = new WorkDay(parent);
	}

}