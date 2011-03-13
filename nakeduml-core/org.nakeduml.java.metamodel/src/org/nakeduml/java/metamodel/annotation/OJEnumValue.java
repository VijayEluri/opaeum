package org.nakeduml.java.metamodel.annotation;

import org.nakeduml.java.metamodel.OJElement;
import org.nakeduml.java.metamodel.OJPathName;
/**
 * A class representing the invocation of a Enum literal, typically from an annotation attribute value.
 * 
 * @author ampie
 *
 */
public class OJEnumValue extends OJElement {
	private OJPathName type;
	private String literalName;
	public OJEnumValue(OJPathName type, String literalName) {
		super();
		this.type = type;
		this.literalName = literalName;
	}
	public String getLiteralName() {
		return this.literalName;
	}
	public void setLiteralName(String literalName) {
		this.literalName = literalName;
	}
	public OJPathName getType() {
		return this.type;
	}
	public void setType(OJPathName type) {
		this.type = type;
	}
	@Override
	public String toJavaString() {
		//Outing the fully qualified name here to avoid potential name clashes
		return this.type.toJavaString() + "." + this.literalName; 
	}
	
	
	
}
