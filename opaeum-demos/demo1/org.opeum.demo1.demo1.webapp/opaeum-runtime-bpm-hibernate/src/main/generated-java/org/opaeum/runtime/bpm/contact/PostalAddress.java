package org.opaeum.runtime.bpm.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Filter;
import org.opaeum.annotation.NumlMetaInfo;
import org.opaeum.runtime.bpm.util.OpaeumLibraryForBPMFormatter;
import org.opaeum.runtime.bpm.util.Stdlib;
import org.opaeum.runtime.domain.CancelledEvent;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.runtime.domain.HibernateEntity;
import org.opaeum.runtime.domain.IEventGenerator;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.domain.IntrospectionUtil;
import org.opaeum.runtime.domain.OutgoingEvent;
import org.opaeum.runtime.environment.Environment;
import org.opaeum.runtime.persistence.AbstractPersistence;
import org.opaeum.runtime.persistence.CmtPersistence;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@NumlMetaInfo(uuid="252060@_anU2QF-jEeGSPaWW9iQb9Q")
@Filter(name="noDeletedObjects")
@org.hibernate.annotations.Entity(dynamicUpdate=true)
@AccessType(	"field")
@Table(name="postal_address",schema="opaeum_bpm")
@Inheritance(strategy=javax.persistence.InheritanceType.JOINED)
@Entity(name="PostalAddress")
@DiscriminatorValue(	"postal_address")
@DiscriminatorColumn(discriminatorType=javax.persistence.DiscriminatorType.STRING,name="type_descriminator")
public class PostalAddress extends Address implements IPersistentObject, IEventGenerator, HibernateEntity, CompositionNode, Serializable {
	@Transient
	private Set<CancelledEvent> cancelledEvents = new HashSet<CancelledEvent>();
		// Initialise to 1000 from 1970
	@Temporal(	javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="deleted_on")
	private Date deletedOn = Stdlib.FUTURE;
	static private Set<PostalAddress> mockedAllInstances;
	@Transient
	private Set<OutgoingEvent> outgoingEvents = new HashSet<OutgoingEvent>();
	@Transient
	private AbstractPersistence persistence;
	static final private long serialVersionUID = 1723129922807887446l;

	/** Default constructor for PostalAddress
	 */
	public PostalAddress() {
	}

	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
	}
	
	static public Set<? extends PostalAddress> allInstances() {
		if ( mockedAllInstances==null ) {
			CmtPersistence session =org.opaeum.runtime.environment.Environment.getInstance().getComponent(CmtPersistence.class);
			return new HashSet(session.readAll(org.opaeum.runtime.bpm.contact.PostalAddress.class));
		} else {
			return mockedAllInstances;
		}
	}
	
	public void buildTreeFromXml(Element xml, Map<String, Object> map) {
		setUid(xml.getAttribute("uid"));
		if ( xml.getAttribute("unitNumber").length()>0 ) {
			setUnitNumber(OpaeumLibraryForBPMFormatter.getInstance().parseString(xml.getAttribute("unitNumber")));
		}
		if ( xml.getAttribute("complexName").length()>0 ) {
			setComplexName(OpaeumLibraryForBPMFormatter.getInstance().parseString(xml.getAttribute("complexName")));
		}
		if ( xml.getAttribute("streetName").length()>0 ) {
			setStreetName(OpaeumLibraryForBPMFormatter.getInstance().parseString(xml.getAttribute("streetName")));
		}
		if ( xml.getAttribute("streetNumber").length()>0 ) {
			setStreetNumber(OpaeumLibraryForBPMFormatter.getInstance().parseString(xml.getAttribute("streetNumber")));
		}
		if ( xml.getAttribute("property1").length()>0 ) {
			setProperty1(OpaeumLibraryForBPMFormatter.getInstance().parseString(xml.getAttribute("property1")));
		}
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
		
		}
	}
	
	public void copyShallowState(PostalAddress from, PostalAddress to) {
		to.setUnitNumber(from.getUnitNumber());
		to.setComplexName(from.getComplexName());
		to.setStreetName(from.getStreetName());
		to.setStreetNumber(from.getStreetNumber());
		to.setProperty1(from.getProperty1());
	}
	
	public void copyState(PostalAddress from, PostalAddress to) {
		to.setUnitNumber(from.getUnitNumber());
		to.setComplexName(from.getComplexName());
		to.setStreetName(from.getStreetName());
		to.setStreetNumber(from.getStreetNumber());
		to.setProperty1(from.getProperty1());
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	public boolean equals(Object other) {
		if ( other instanceof PostalAddress ) {
			return other==this || ((PostalAddress)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	public Set<CancelledEvent> getCancelledEvents() {
		return this.cancelledEvents;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	public String getName() {
		return "PostalAddress["+getId()+"]";
	}
	
	public Set<OutgoingEvent> getOutgoingEvents() {
		return this.outgoingEvents;
	}
	
	public CompositionNode getOwningObject() {
		return null;
	}
	
	public int hashCode() {
		return getUid().hashCode();
	}
	
	public void init(CompositionNode owner) {
		super.init(owner);
		createComponents();
	}
	
	public PostalAddress makeCopy() {
		PostalAddress result = new PostalAddress();
		copyState((PostalAddress)this,result);
		return result;
	}
	
	public PostalAddress makeShallowCopy() {
		PostalAddress result = new PostalAddress();
		copyShallowState((PostalAddress)this,result);
		result.setId(this.getId());
		return result;
	}
	
	public void markDeleted() {
		super.markDeleted();
		setDeletedOn(new Date());
	}
	
	static public void mockAllInstances(Set<PostalAddress> newMocks) {
		mockedAllInstances=newMocks;
	}
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
		
		}
	}
	
	public void removeFromOwningObject() {
		this.markDeleted();
	}
	
	public void setCancelledEvents(Set<CancelledEvent> cancelledEvents) {
		this.cancelledEvents=cancelledEvents;
	}
	
	public void setDeletedOn(Date deletedOn) {
		this.deletedOn=deletedOn;
		super.setDeletedOn(deletedOn);
	}
	
	public void setOutgoingEvents(Set<OutgoingEvent> outgoingEvents) {
		this.outgoingEvents=outgoingEvents;
	}
	
	public String toXmlReferenceString() {
		return "<PostalAddress uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<PostalAddress ");
		sb.append("classUuid=\"252060@_anU2QF-jEeGSPaWW9iQb9Q\" ");
		sb.append("className=\"org.opaeum.runtime.bpm.contact.PostalAddress\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		if ( getUnitNumber()!=null ) {
			sb.append("unitNumber=\""+ OpaeumLibraryForBPMFormatter.getInstance().formatString(getUnitNumber())+"\" ");
		}
		if ( getComplexName()!=null ) {
			sb.append("complexName=\""+ OpaeumLibraryForBPMFormatter.getInstance().formatString(getComplexName())+"\" ");
		}
		if ( getStreetName()!=null ) {
			sb.append("streetName=\""+ OpaeumLibraryForBPMFormatter.getInstance().formatString(getStreetName())+"\" ");
		}
		if ( getStreetNumber()!=null ) {
			sb.append("streetNumber=\""+ OpaeumLibraryForBPMFormatter.getInstance().formatString(getStreetNumber())+"\" ");
		}
		if ( getProperty1()!=null ) {
			sb.append("property1=\""+ OpaeumLibraryForBPMFormatter.getInstance().formatString(getProperty1())+"\" ");
		}
		sb.append(">");
		sb.append("\n</PostalAddress>");
		return sb.toString();
	}

}