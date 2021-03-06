package org.opaeum.runtime.environment;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.opaeum.runtime.domain.ISignal;
import org.opaeum.runtime.domain.IntrospectionUtil;
import org.opaeum.runtime.environment.marshall.PropertyValue;
import org.opaeum.runtime.environment.marshall.SerializableValue;
import org.opaeum.runtime.environment.marshall.Value;
import org.opaeum.runtime.event.IEventHandler;
import org.opaeum.runtime.persistence.AbstractPersistence;

public class GenericSignalHandler implements IEventHandler{
	ISignal signal;
	public GenericSignalHandler(){
	}
	public GenericSignalHandler(ISignal signal){
		super();
		this.signal = signal;
	}
	@Override
	public String getHandlerUuid(){
		return GenericSignalHandler.class.getName();
	}
	@Override
	public void unmarshall(Collection<PropertyValue> propertyValues,AbstractPersistence session){
		ArrayList<PropertyValue> l=new ArrayList<PropertyValue>(propertyValues);
		PropertyValue propertyValue = l.get(0);
		Serializable value = ((SerializableValue) propertyValue.getValue()).getValue();
		@SuppressWarnings("unchecked")
		Class<? extends ISignal> clss = (Class<? extends ISignal>) value;
		this.signal = IntrospectionUtil.newInstance(clss);
		PropertyDescriptor[] pds = IntrospectionUtil.getProperties(signal.getClass());
		for(int i = 1; i < l.size();i++){
			PropertyValue pv = l.get(i);
			IntrospectionUtil.set(pds[pv.getId().intValue()], signal, Value.valueOf(pv.getValue(), session));
		}
	}
	@Override
	public Collection<PropertyValue> marshall(Environment env){
		Collection<PropertyValue> result=new ArrayList<PropertyValue>();
		result.add(new PropertyValue(10101l, new SerializableValue("1234", signal.getClass())));
		int i=0;
		for(PropertyDescriptor p:IntrospectionUtil.getProperties(signal.getClass())){
			result.add(new PropertyValue((long)i++,Value.valueOf(IntrospectionUtil.get(p,signal),env)));
		}
		return null;
	}
	@Override
	public boolean handleOn(Object target, AbstractPersistence p){
		for(Method method:target.getClass().getMethods()){
			if(method.getName().startsWith("receive") && method.getParameterTypes().length==1 && method.getParameterTypes()[0].isInstance(signal)){
				try{
					method.invoke(target, signal);
				}catch(Exception e){
					throw new RuntimeException(e);
				}
				return true;
			}
		}
		return false;
	}
	@Override
	public String getQueueName(){
		return "GenericSignals";
	}
	@Override
	public int getConsumerPoolSize(){
		return 0;
	}
	@Override
	public Date scheduleNextOccurrence(Object b){
		return null;
	}
	@Override
	public Date getFirstOccurrenceScheduledFor(){
		return null;
	}
}
