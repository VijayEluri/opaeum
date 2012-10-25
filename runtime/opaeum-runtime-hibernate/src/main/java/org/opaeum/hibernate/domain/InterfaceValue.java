package org.opaeum.hibernate.domain;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.opaeum.runtime.costing.IQuantifiedResourceBase;
import org.opaeum.runtime.domain.IPersistentObject;

@Embeddable
public class InterfaceValue extends AbstractInterfaceValue{
	private Long identifier;
	private String classIdentifier;
	@Transient
	private IPersistentObject value;
	public InterfaceValue(IPersistentObject resource){
		setValue(resource);
	}
	public InterfaceValue(){
	}
	public void setIdentifier(Long identifier){
		this.identifier = identifier;
	}
	public Long getIdentifier(){
		return identifier;
	}
	protected IPersistentObject getValue(){
		return value;
	}
	protected String getClassIdentifier(){
		return classIdentifier;
	}
	protected void setClassIdentifier(String classIdentifier){
		this.classIdentifier = classIdentifier;
	}
	protected void setValueImpl(IPersistentObject value){
		this.value = value;
	}
}