package org.opaeum.runtime.bpm.organization;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Index;
import org.opaeum.annotation.NumlMetaInfo;
import org.opaeum.annotation.PropertyMetaInfo;
import org.opaeum.audit.AuditMe;
import org.opaeum.hibernate.domain.CascadingInterfaceValue;
import org.opaeum.runtime.bpm.util.OpaeumLibraryForBPMFormatter;
import org.opaeum.runtime.bpm.util.Stdlib;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.runtime.domain.HibernateEntity;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.domain.IntrospectionUtil;
import org.opaeum.runtime.environment.Environment;
import org.opaeum.runtime.persistence.AbstractPersistence;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@AuditMe
@NumlMetaInfo(uuid="252060@_YJGvcFYjEeGJUqEGX7bKSg")
@Filter(name="noDeletedObjects")
@org.hibernate.annotations.Entity(dynamicUpdate=true)
@AccessType(	"field")
@Table(name="business_network_facilatates_collaboration",uniqueConstraints=
	@UniqueConstraint(columnNames={"business_collaboration","business_collaboration_type","deleted_on"}))
@Inheritance(strategy=javax.persistence.InheritanceType.JOINED)
@Entity(name="BusinessNetworkFacilatatesCollaboration")
@DiscriminatorColumn(discriminatorType=javax.persistence.DiscriminatorType.STRING,name="type_descriminator")
public class BusinessNetworkFacilatatesCollaboration implements IPersistentObject, HibernateEntity, CompositionNode, Serializable {
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	@Embedded
	@AttributeOverrides(	{
		@AttributeOverride(column=
			@Column(name="business_collaboration"),name="identifier"),
		@AttributeOverride(column=
			@Column(name="business_collaboration_type"),name="classIdentifier")})
	private CascadingInterfaceValue businessCollaboration = new CascadingInterfaceValue();
	@Index(columnNames="business_network_id",name="idx_business_network_facilatates_collaboration_business_network_id")
	@ManyToOne(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="business_network_id",nullable=true)
	private BusinessNetwork businessNetwork;
		// Initialise to 1000 from 1970
	@Temporal(	javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="deleted_on")
	private Date deletedOn = Stdlib.FUTURE;
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.TABLE)
	private Long id;
	static private Set<BusinessNetworkFacilatatesCollaboration> mockedAllInstances;
	@Version
	@Column(name="object_version")
	private int objectVersion;
	@Transient
	private AbstractPersistence persistence;
	@Transient
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	static final private long serialVersionUID = 2393652207927689043l;
	private String uid;

	/** Constructor for BusinessNetworkFacilatatesCollaboration
	 * 
	 * @param end1 
	 * @param end2 
	 */
	public BusinessNetworkFacilatatesCollaboration(BusinessNetwork end1, IBusinessCollaboration end2) {
		this.z_internalAddToBusinessNetwork(end1);
		this.z_internalAddToBusinessCollaboration(end2);
	}
	
	/** Constructor for BusinessNetworkFacilatatesCollaboration
	 * 
	 * @param end2 
	 * @param end1 
	 */
	public BusinessNetworkFacilatatesCollaboration(IBusinessCollaboration end2, BusinessNetwork end1) {
		this.z_internalAddToBusinessNetwork(end1);
		this.z_internalAddToBusinessCollaboration(end2);
	}
	
	/** Default constructor for BusinessNetworkFacilatatesCollaboration
	 */
	public BusinessNetworkFacilatatesCollaboration() {
	}

	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(property,listener);
	}
	
	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
	}
	
	static public Set<? extends BusinessNetworkFacilatatesCollaboration> allInstances(AbstractPersistence persistence) {
		if ( mockedAllInstances==null ) {
			return new HashSet(persistence.readAll(org.opaeum.runtime.bpm.organization.BusinessNetworkFacilatatesCollaboration.class));
		} else {
			return mockedAllInstances;
		}
	}
	
	public void buildTreeFromXml(Element xml, Map<String, Object> map) {
		setUid(xml.getAttribute("uid"));
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("businessCollaboration") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("2754791200820683043")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						IBusinessCollaboration curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=Environment.getInstance().getMetaInfoMap().newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.setBusinessCollaboration(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
		}
	}
	
	public void clear() {
		getBusinessCollaboration().z_internalRemoveFromBusinessNetworkFacilatatesCollaboration_businessNetwork(this);
		this.z_internalRemoveFromBusinessCollaboration(getBusinessCollaboration());
		getBusinessNetwork().z_internalRemoveFromBusinessNetworkFacilatatesCollaboration_businessCollaboration(this);
		this.z_internalRemoveFromBusinessNetwork(getBusinessNetwork());
		markDeleted();
	}
	
	public boolean equals(Object other) {
		if ( other instanceof BusinessNetworkFacilatatesCollaboration ) {
			return other==this || ((BusinessNetworkFacilatatesCollaboration)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=true,opaeumId=2754791200820683043l,opposite="businessNetworkFacilatatesCollaboration_businessNetwork",uuid="252060@_YJETMFYjEeGJUqEGX7bKSg")
	@NumlMetaInfo(uuid="252060@_YJGvcFYjEeGJUqEGX7bKSg@252060@_YJETMFYjEeGJUqEGX7bKSg")
	public IBusinessCollaboration getBusinessCollaboration() {
		IBusinessCollaboration result = (IBusinessCollaboration)this.businessCollaboration.getValue(persistence);
		
		return result;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=1977591455643758737l,opposite="businessNetworkFacilatatesCollaboration_businessCollaboration",uuid="252060@_YJGvcVYjEeGJUqEGX7bKSg")
	@NumlMetaInfo(uuid="252060@_YJGvcFYjEeGJUqEGX7bKSg@252060@_YJGvcVYjEeGJUqEGX7bKSg")
	public BusinessNetwork getBusinessNetwork() {
		BusinessNetwork result = this.businessNetwork;
		
		return result;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getName() {
		return "BusinessNetworkFacilatatesCollaboration["+getId()+"]";
	}
	
	public int getObjectVersion() {
		return this.objectVersion;
	}
	
	public CompositionNode getOwningObject() {
		return getBusinessNetwork();
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
	
	public void init(CompositionNode owner) {
		this.z_internalAddToBusinessNetwork((BusinessNetwork)owner);
	}
	
	public void markDeleted() {
		if ( getBusinessNetwork()!=null ) {
			getBusinessNetwork().z_internalRemoveFromBusinessNetworkFacilatatesCollaboration_businessCollaboration(this);
		}
		if ( getBusinessCollaboration()!=null ) {
			getBusinessCollaboration().markDeleted();
		}
		setDeletedOn(new Date());
	}
	
	static public void mockAllInstances(Set<BusinessNetworkFacilatatesCollaboration> newMocks) {
		mockedAllInstances=newMocks;
	}
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("businessNetwork") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("1977591455643758737")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						setBusinessNetwork((BusinessNetwork)map.get(((Element)currentPropertyValueNode).getAttribute("uid")));
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("businessCollaboration") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("2754791200820683043")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((IBusinessCollaboration)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
		}
	}
	
	public void removeFromOwningObject() {
		this.markDeleted();
	}
	
	public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(property,listener);
	}
	
	public void setBusinessCollaboration(IBusinessCollaboration businessCollaboration) {
		IBusinessCollaboration oldValue = this.getBusinessCollaboration();
		propertyChangeSupport.firePropertyChange("businessCollaboration",getBusinessCollaboration(),businessCollaboration);
		if ( oldValue==null ) {
			if ( businessCollaboration!=null ) {
				BusinessNetworkFacilatatesCollaboration oldOther = (BusinessNetworkFacilatatesCollaboration)businessCollaboration.getBusinessNetworkFacilatatesCollaboration_businessNetwork();
				businessCollaboration.z_internalRemoveFromBusinessNetworkFacilatatesCollaboration_businessNetwork(oldOther);
				if ( oldOther != null ) {
					oldOther.z_internalRemoveFromBusinessCollaboration(businessCollaboration);
				}
				businessCollaboration.z_internalAddToBusinessNetworkFacilatatesCollaboration_businessNetwork((BusinessNetworkFacilatatesCollaboration)this);
			}
			this.z_internalAddToBusinessCollaboration(businessCollaboration);
		} else {
			if ( !oldValue.equals(businessCollaboration) ) {
				oldValue.z_internalRemoveFromBusinessNetworkFacilatatesCollaboration_businessNetwork(this);
				z_internalRemoveFromBusinessCollaboration(oldValue);
				if ( businessCollaboration!=null ) {
					BusinessNetworkFacilatatesCollaboration oldOther = (BusinessNetworkFacilatatesCollaboration)businessCollaboration.getBusinessNetworkFacilatatesCollaboration_businessNetwork();
					businessCollaboration.z_internalRemoveFromBusinessNetworkFacilatatesCollaboration_businessNetwork(oldOther);
					if ( oldOther != null ) {
						oldOther.z_internalRemoveFromBusinessCollaboration(businessCollaboration);
					}
					businessCollaboration.z_internalAddToBusinessNetworkFacilatatesCollaboration_businessNetwork((BusinessNetworkFacilatatesCollaboration)this);
				}
				this.z_internalAddToBusinessCollaboration(businessCollaboration);
			}
		}
	}
	
	public void setBusinessNetwork(BusinessNetwork businessNetwork) {
		propertyChangeSupport.firePropertyChange("businessNetwork",getBusinessNetwork(),businessNetwork);
		if ( this.getBusinessNetwork()!=null ) {
			this.getBusinessNetwork().z_internalRemoveFromBusinessNetworkFacilatatesCollaboration_businessCollaboration(this);
		}
		if ( businessNetwork!=null ) {
			businessNetwork.z_internalAddToBusinessNetworkFacilatatesCollaboration_businessCollaboration(this);
			this.z_internalAddToBusinessNetwork(businessNetwork);
			setDeletedOn(Stdlib.FUTURE);
		} else {
			markDeleted();
		}
	}
	
	public void setDeletedOn(Date deletedOn) {
		this.deletedOn=deletedOn;
	}
	
	public void setId(Long id) {
		this.id=id;
	}
	
	public void setObjectVersion(int objectVersion) {
		this.objectVersion=objectVersion;
	}
	
	public void setUid(String newUid) {
		this.uid=newUid;
	}
	
	public String toXmlReferenceString() {
		return "<BusinessNetworkFacilatatesCollaboration uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<BusinessNetworkFacilatatesCollaboration ");
		sb.append("classUuid=\"252060@_YJGvcFYjEeGJUqEGX7bKSg\" ");
		sb.append("className=\"org.opaeum.runtime.bpm.organization.BusinessNetworkFacilatatesCollaboration\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		sb.append(">");
		if ( getBusinessNetwork()==null ) {
			sb.append("\n<businessNetwork/>");
		} else {
			sb.append("\n<businessNetwork propertyId=\"1977591455643758737\">");
			sb.append("\n" + getBusinessNetwork().toXmlReferenceString());
			sb.append("\n</businessNetwork>");
		}
		if ( getBusinessCollaboration()==null ) {
			sb.append("\n<businessCollaboration/>");
		} else {
			sb.append("\n<businessCollaboration propertyId=\"2754791200820683043\">");
			sb.append("\n" + getBusinessCollaboration().toXmlString());
			sb.append("\n</businessCollaboration>");
		}
		sb.append("\n</BusinessNetworkFacilatatesCollaboration>");
		return sb.toString();
	}
	
	public void z_internalAddToBusinessCollaboration(IBusinessCollaboration val) {
		this.businessCollaboration.setValue(val);
	}
	
	public void z_internalAddToBusinessNetwork(BusinessNetwork val) {
		this.businessNetwork=val;
	}
	
	public void z_internalRemoveFromBusinessCollaboration(IBusinessCollaboration val) {
		if ( getBusinessCollaboration()!=null && val!=null && val.equals(getBusinessCollaboration()) ) {
			this.businessCollaboration.setValue(null);
		}
	}
	
	public void z_internalRemoveFromBusinessNetwork(BusinessNetwork val) {
		if ( getBusinessNetwork()!=null && val!=null && val.equals(getBusinessNetwork()) ) {
			this.businessNetwork=null;
			this.businessNetwork=null;
		}
	}

}