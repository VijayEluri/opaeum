package org.nakeduml.environment;

import org.hibernate.Session;
import org.nakeduml.annotation.NumlMetaInfo;
import org.nakeduml.runtime.domain.ActiveObject;

public class HelperValue extends Value{
	private static final long serialVersionUID = 9038651829406737134L;
	Integer classOrInterfaceId;
	private Class<ActiveObject> helperClass;
	public HelperValue(){
	}
	public HelperValue(ActiveObject s){
		Environment instance = Environment.getInstance();
		Class<ActiveObject> implementationClass = instance.getImplementationClass(s);
		NumlMetaInfo metaInfo = implementationClass.getAnnotation(NumlMetaInfo.class);
		if(metaInfo != null){
			classOrInterfaceId = metaInfo.nakedUmlId();
		}else{
			helperClass = implementationClass;
		}
	}
	@Override
	public Object getValue(Session session){
		if(classOrInterfaceId == null){
			return Environment.getInstance().getComponent(helperClass);
		}else{
			Class<?> class1 = getValueClass();
			return Environment.getInstance().getComponent(class1);
		}
	}
	public Class<?> getValueClass(){
		if(helperClass == null){
			return Environment.getMetaInfoMap().getClass(classOrInterfaceId);
		}else{
			return helperClass;
		}
	}
}