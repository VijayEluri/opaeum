package org.nakeduml.environment;

import org.hibernate.Session;
import org.nakeduml.annotation.NumlMetaInfo;
import org.nakeduml.runtime.domain.IPersistentObject;
import org.nakeduml.runtime.domain.IntrospectionUtil;

public class EntityValue extends Value{
	private static final long serialVersionUID = 1907963640694864841L;
	Long id;
	Integer classId;
	public EntityValue(){
	}
	public EntityValue(IPersistentObject e){
		this.classId=IntrospectionUtil.getOriginalClass(e).getAnnotation(NumlMetaInfo.class).nakedUmlId(); 
		this.id=e.getId();
	}
	public Long getId(){
		return id;
	}
	public void setId(Long id){
		this.id = id;
	}
	@Override
	public Object getValue(Session session){
		Class<?> asdf = getValueClass();
		Object result = session.get(asdf, id);
		if(result == null){
			throw new IllegalStateException(asdf.getSimpleName() + ":" + id + " could not be found!");
		}
		return result;
	}
	public Class<?> getValueClass(){
		return Environment.getMetaInfoMap().getClass(classId);
	}
}