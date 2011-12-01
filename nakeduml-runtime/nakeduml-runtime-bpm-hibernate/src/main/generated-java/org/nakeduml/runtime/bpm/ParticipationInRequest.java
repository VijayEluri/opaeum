package org.nakeduml.runtime.bpm;

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
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Index;
import org.nakeduml.annotation.NumlMetaInfo;
import org.nakeduml.runtime.bpm.util.OpiumLibraryForBPMFormatter;
import org.nakeduml.runtime.bpm.util.Stdlib;
import org.nakeduml.runtime.domain.CancelledEvent;
import org.nakeduml.runtime.domain.CompositionNode;
import org.nakeduml.runtime.domain.HibernateEntity;
import org.nakeduml.runtime.domain.IEventGenerator;
import org.nakeduml.runtime.domain.IPersistentObject;
import org.nakeduml.runtime.domain.IntrospectionUtil;
import org.nakeduml.runtime.domain.OutgoingEvent;
import org.nakeduml.runtime.environment.Environment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@org.hibernate.annotations.Entity(dynamicUpdate=true)
@Filter(name="noDeletedObjects")
@Entity(name="ParticipationInRequest")
@DiscriminatorColumn(name="type_descriminator",discriminatorType=javax.persistence.DiscriminatorType.STRING)
@Inheritance(strategy=javax.persistence.InheritanceType.JOINED)
@Table(schema="opium_bpm",name="participation_in_request")
@NumlMetaInfo(uuid="252060@_GVhgMI6NEeCrtavWRHwoHg")
@AccessType("field")
@DiscriminatorValue("participation_in_request")
public class ParticipationInRequest extends Participation implements IEventGenerator, CompositionNode, HibernateEntity, Serializable, IPersistentObject {
	@Transient
	private Set<CancelledEvent> cancelledEvents = new HashSet<CancelledEvent>();
		// Initialise to 1000 from 1970
	@Column(name="deleted_on")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date deletedOn = Stdlib.FUTURE;
	@Index(name="idx_participation_in_request_request_id",columnNames="request_id")
	@ManyToOne(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="request_id",nullable=true)
	private AbstractRequest request;
	@Transient
	private Set<OutgoingEvent> outgoingEvents = new HashSet<OutgoingEvent>();
	static final private long serialVersionUID = 11;
	static private Set<ParticipationInRequest> mockedAllInstances;
	@Enumerated(javax.persistence.EnumType.STRING)
	@Column(name="kind",nullable=true)
	private RequestParticipationKind kind;

	/** Default constructor for ParticipationInRequest
	 */
	public ParticipationInRequest() {
	}
	
	/** This constructor is intended for easy initialization in unit tests
	 * 
	 * @param owningObject 
	 */
	public ParticipationInRequest(AbstractRequest owningObject) {
		init(owningObject);
		addToOwningObject();
	}

	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
		getRequest().z_internalAddToParticipationsInRequest((ParticipationInRequest)this);
	}
	
	static public Set<? extends ParticipationInRequest> allInstances() {
		if ( mockedAllInstances==null ) {
			Session session =org.nakeduml.runtime.environment.Environment.getInstance().getComponent(Session.class);
			return new HashSet(session.createQuery("from ParticipationInRequest").list());
		} else {
			return mockedAllInstances;
		}
	}
	
	public void buildTreeFromXml(Element xml, Map<String, Object> map) {
		setUid(xml.getAttribute("uid"));
		if ( xml.getAttribute("kind").length()>0 ) {
			setKind(RequestParticipationKind.valueOf(xml.getAttribute("kind")));
		}
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
		
		}
	}
	
	public void copyShallowState(ParticipationInRequest from, ParticipationInRequest to) {
		to.setKind(from.getKind());
	}
	
	public void copyState(ParticipationInRequest from, ParticipationInRequest to) {
		to.setKind(from.getKind());
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	public boolean equals(Object other) {
		if ( other instanceof ParticipationInRequest ) {
			return other==this || ((ParticipationInRequest)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	public Set<CancelledEvent> getCancelledEvents() {
		return this.cancelledEvents;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	@NumlMetaInfo(uuid="252060@_cATKlI6NEeCrtavWRHwoHg")
	public RequestParticipationKind getKind() {
		RequestParticipationKind result = this.kind;
		
		return result;
	}
	
	public String getName() {
		return "ParticipationInRequest["+getId()+"]";
	}
	
	public Set<OutgoingEvent> getOutgoingEvents() {
		return this.outgoingEvents;
	}
	
	public CompositionNode getOwningObject() {
		return getRequest();
	}
	
	@NumlMetaInfo(uuid="252060@_XLVmwY6NEeCrtavWRHwoHg")
	public AbstractRequest getRequest() {
		AbstractRequest result = this.request;
		
		return result;
	}
	
	public int hashCode() {
		return getUid().hashCode();
	}
	
	public void init(CompositionNode owner) {
		super.init(owner);
		this.z_internalAddToRequest((AbstractRequest)owner);
		createComponents();
	}
	
	public ParticipationInRequest makeCopy() {
		ParticipationInRequest result = new ParticipationInRequest();
		copyState((ParticipationInRequest)this,result);
		return result;
	}
	
	public ParticipationInRequest makeShallowCopy() {
		ParticipationInRequest result = new ParticipationInRequest();
		copyShallowState((ParticipationInRequest)this,result);
		result.setId(this.getId());
		return result;
	}
	
	public void markDeleted() {
		super.markDeleted();
		if ( getParticipant()!=null ) {
			getParticipant().z_internalRemoveFromParticipation((ParticipationInRequest)this);
		}
		if ( getRequest()!=null ) {
			getRequest().z_internalRemoveFromParticipationsInRequest((ParticipationInRequest)this);
		}
	}
	
	static public void mockAllInstances(Set<ParticipationInRequest> newMocks) {
		mockedAllInstances=newMocks;
	}
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("participant") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("165")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						setParticipant((Participant)map.get(((Element)currentPropertyValueNode).getAttribute("uid")));
					}
				}
			}
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
	
	public void setKind(RequestParticipationKind kind) {
		this.z_internalAddToKind(kind);
	}
	
	public void setOutgoingEvents(Set<OutgoingEvent> outgoingEvents) {
		this.outgoingEvents=outgoingEvents;
	}
	
	public void setRequest(AbstractRequest request) {
		if ( this.getRequest()!=null ) {
			this.getRequest().z_internalRemoveFromParticipationsInRequest((ParticipationInRequest)this);
		}
		if ( request!=null ) {
			request.z_internalAddToParticipationsInRequest((ParticipationInRequest)this);
			this.z_internalAddToRequest(request);
			setDeletedOn(Stdlib.FUTURE);
		} else {
			setDeletedOn(new Date());
		}
	}
	
	public String toXmlReferenceString() {
		return "<ParticipationInRequest uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<ParticipationInRequest ");
		sb.append("classUuid=\"252060@_GVhgMI6NEeCrtavWRHwoHg\" ");
		sb.append("className=\"org.nakeduml.runtime.bpm.ParticipationInRequest\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		if ( getKind()!=null ) {
			sb.append("kind=\""+ getKind().name() + "\" ");
		}
		sb.append(">");
		if ( getParticipant()==null ) {
			sb.append("\n<participant/>");
		} else {
			sb.append("\n<participant propertyId=\"165\">");
			sb.append("\n" + getParticipant().toXmlReferenceString());
			sb.append("\n</participant>");
		}
		sb.append("\n</ParticipationInRequest>");
		return sb.toString();
	}
	
	public void z_internalAddToKind(RequestParticipationKind val) {
		this.kind=val;
	}
	
	public void z_internalAddToRequest(AbstractRequest val) {
		this.request=val;
	}
	
	public void z_internalRemoveFromKind(RequestParticipationKind val) {
		if ( getKind()!=null && val!=null && val.equals(getKind()) ) {
			this.kind=null;
		}
	}
	
	public void z_internalRemoveFromRequest(AbstractRequest val) {
		if ( getRequest()!=null && val!=null && val.equals(getRequest()) ) {
			this.request=null;
		}
	}

}