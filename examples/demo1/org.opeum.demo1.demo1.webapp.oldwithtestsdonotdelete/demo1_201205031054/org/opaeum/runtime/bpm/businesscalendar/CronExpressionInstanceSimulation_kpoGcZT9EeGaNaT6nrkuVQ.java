package org.opaeum.runtime.bpm.businesscalendar;

import java.text.ParseException;

import org.opaeum.simulation.SimulationMetaData;
import org.opaeum.simulation.StructInstanceSimulation;

public class CronExpressionInstanceSimulation_kpoGcZT9EeGaNaT6nrkuVQ extends StructInstanceSimulation {



	public CronExpression createNewObject() throws ParseException {
		CronExpression result = new CronExpression();
		int minutesCount = 0;
		while ( minutesCount<1 ) {
			minutesCount++;
			result.setMinutes((String)SimulationMetaData.getInstance().getNextValueForProperty("demo1_201205031054::CronExpressionInstanceSimulation","minutes"));
		}
		int hoursCount = 0;
		while ( hoursCount<1 ) {
			hoursCount++;
			result.setHours((String)SimulationMetaData.getInstance().getNextValueForProperty("demo1_201205031054::CronExpressionInstanceSimulation","hours"));
		}
		int dayOfMonthCount = 0;
		while ( dayOfMonthCount<1 ) {
			dayOfMonthCount++;
			result.setDayOfMonth((String)SimulationMetaData.getInstance().getNextValueForProperty("demo1_201205031054::CronExpressionInstanceSimulation","dayOfMonth"));
		}
		int monthCount = 0;
		while ( monthCount<1 ) {
			monthCount++;
			result.setMonth((String)SimulationMetaData.getInstance().getNextValueForProperty("demo1_201205031054::CronExpressionInstanceSimulation","month"));
		}
		int dayOfWeekCount = 0;
		while ( dayOfWeekCount<1 ) {
			dayOfWeekCount++;
			result.setDayOfWeek((String)SimulationMetaData.getInstance().getNextValueForProperty("demo1_201205031054::CronExpressionInstanceSimulation","dayOfWeek"));
		}
		return result;
	}
	
	public void populateReferences(Object in) throws ParseException {
		CronExpression instance = (CronExpression)in;
	}

}