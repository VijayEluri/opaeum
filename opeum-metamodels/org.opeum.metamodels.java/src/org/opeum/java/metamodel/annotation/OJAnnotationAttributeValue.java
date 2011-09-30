package org.opeum.java.metamodel.annotation;

import org.opeum.java.metamodel.OJPathName;

public class OJAnnotationAttributeValue extends OJMetaValue {
	public OJAnnotationAttributeValue() {
		super();
	}
	public OJAnnotationAttributeValue(String name, Boolean value) {
		super(value);
		super.setName(name);
	}
	public OJAnnotationAttributeValue(String name, Number value) {
		super(value);
		super.setName(name);
	}
	public OJAnnotationAttributeValue(String name, OJAnnotationValue value) {
		super(value);
		super.setName(name);
	}
	public OJAnnotationAttributeValue(String name, OJPathName value) {
		super(value);
		super.setName(name);
	}
	public OJAnnotationAttributeValue(String name, OJEnumValue value) {
		super(value);
		super.setName(name);
	}
	public OJAnnotationAttributeValue(String name, String value) {
		super(value);
		super.setName(name);
	}
	public OJAnnotationAttributeValue(String name) {
		super.setName(name);
	}
	
	@Override
	public String toJavaString() {
		return getName() + "=" + toJavaValueExpression();
	}
	public OJAnnotationAttributeValue getCopy(){
		OJAnnotationAttributeValue result = new OJAnnotationAttributeValue();
		copyInfoInto(result);		
		return result;
	}
	public OJAnnotationAttributeValue getDeepCopy(){
		OJAnnotationAttributeValue result = new OJAnnotationAttributeValue();
		copyDeepInfoInto(result);		
		return result;
	}
	
	protected void copyInfoInto(OJAnnotationAttributeValue v){
		super.copyInfoInto(v);
	}
	protected void copyDeepInfoInto(OJAnnotationAttributeValue v){
		super.copyDeepInfoInto(v);
	}
	
}
