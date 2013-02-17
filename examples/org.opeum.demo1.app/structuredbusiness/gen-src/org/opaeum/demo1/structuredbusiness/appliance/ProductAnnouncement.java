package org.opaeum.demo1.structuredbusiness.appliance;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Transient;

import org.opaeum.annotation.NumlMetaInfo;
import org.opaeum.demo1.structuredbusiness.util.StructuredbusinessFormatter;
import org.opaeum.runtime.domain.ISignal;
import org.opaeum.runtime.domain.IntrospectionUtil;
import org.opaeum.runtime.environment.Environment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@NumlMetaInfo(applicationIdentifier="demo1",uuid="914890@_o9aQgGCfEeG6xvYqJACneg")
public class ProductAnnouncement implements ISignal, Serializable {
	@Transient
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	static final private long serialVersionUID = 4518636647403222403l;
	private String uid;

	/** Default constructor for ProductAnnouncement
	 */
	public ProductAnnouncement() {
	}

	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(property,listener);
	}
	
	public void buildTreeFromXml(Element xml, Map<String, Object> map) {
		setUid(xml.getAttribute("uid"));
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
		
		}
	}
	
	public boolean equals(Object other) {
		if ( other instanceof ProductAnnouncement ) {
			return other==this || ((ProductAnnouncement)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	public String getUid() {
		if ( this.uid==null || this.uid.trim().length()==0 ) {
			uid=UUID.randomUUID().toString();
		}
		return this.uid;
	}
	
	public int hashCode() {
		return getUid().hashCode();
	}
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
		
		}
	}
	
	public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(property,listener);
	}
	
	public void setUid(String newUid) {
		this.uid=newUid;
	}
	
	public String toXmlReferenceString() {
		return "<ProductAnnouncement uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<ProductAnnouncement ");
		sb.append("classUuid=\"914890@_o9aQgGCfEeG6xvYqJACneg\" ");
		sb.append("className=\"org.opaeum.demo1.structuredbusiness.appliance.ProductAnnouncement\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		sb.append(">");
		sb.append("\n</ProductAnnouncement>");
		return sb.toString();
	}

}