package org.opaeum.runtime.bpm.organization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyCollection;
import org.opaeum.annotation.NumlMetaInfo;
import org.opaeum.runtime.bpm.businesscalendar.BusinessCalendar;
import org.opaeum.runtime.bpm.contact.OrganizationEMailAddress;
import org.opaeum.runtime.bpm.contact.OrganizationPhoneNumber;
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
import org.opaeum.runtime.organization.IOrganizationNode;
import org.opaeum.runtime.persistence.CmtPersistence;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@NumlMetaInfo(uuid="252060@_pZdQEEtmEeGd4cpyhpib9Q")
@Filter(name="noDeletedObjects")
@org.hibernate.annotations.Entity(dynamicUpdate=true)
@AccessType(	"field")
@Table(name="organizational_node",schema="opaeum_bpm")
@Inheritance(strategy=javax.persistence.InheritanceType.JOINED)
@Entity(name="OrganizationalNode")
@DiscriminatorColumn(discriminatorType=javax.persistence.DiscriminatorType.STRING,name="type_descriminator")
public class OrganizationalNode implements IOrganizationNode, IPersistentObject, IEventGenerator, HibernateEntity, CompositionNode, Serializable {
	@OneToOne(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY,mappedBy="organization")
	private BusinessCalendar businessCalendar;
	@Index(columnNames="business_network_id",name="idx_organizational_node_business_network_id")
	@ManyToOne(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="business_network_id",nullable=true)
	private BusinessNetwork businessNetwork;
	@Transient
	private Set<CancelledEvent> cancelledEvents = new HashSet<CancelledEvent>();
		// Initialise to 1000 from 1970
	@Temporal(	javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="deleted_on")
	private Date deletedOn = Stdlib.FUTURE;
	@LazyCollection(	org.hibernate.annotations.LazyCollectionOption.TRUE)
	@Filter(condition="deleted_on > current_timestamp",name="noDeletedObjects")
	@OneToMany(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY,mappedBy="organization",targetEntity=OrganizationEMailAddress.class)
	private Set<OrganizationEMailAddress> eMailAddress = new HashSet<OrganizationEMailAddress>();
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.AUTO)
	private Long id;
	static private Set<OrganizationalNode> mockedAllInstances;
	@Column(name="name")
	private String name;
	@Version
	@Column(name="object_version")
	private int objectVersion;
	@LazyCollection(	org.hibernate.annotations.LazyCollectionOption.TRUE)
	@Filter(condition="deleted_on > current_timestamp",name="noDeletedObjects")
	@OneToMany(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY,mappedBy="organization",targetEntity=OrganizationFullfillsActorRole.class)
	private Set<OrganizationFullfillsActorRole> organizationFullfillsActorRole_businessActor = new HashSet<OrganizationFullfillsActorRole>();
	@LazyCollection(	org.hibernate.annotations.LazyCollectionOption.TRUE)
	@Filter(condition="deleted_on > current_timestamp",name="noDeletedObjects")
	@OneToMany(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY,mappedBy="representedOrganization",targetEntity=Organization_iBusinessComponent_1.class)
	private Set<Organization_iBusinessComponent_1> organization_iBusinessComponent_1_businessComponent = new HashSet<Organization_iBusinessComponent_1>();
	@Transient
	private Set<OutgoingEvent> outgoingEvents = new HashSet<OutgoingEvent>();
	@LazyCollection(	org.hibernate.annotations.LazyCollectionOption.TRUE)
	@Filter(condition="deleted_on > current_timestamp",name="noDeletedObjects")
	@OneToMany(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY,mappedBy="organization",targetEntity=OrganizationPhoneNumber.class)
	private Set<OrganizationPhoneNumber> phoneNumber = new HashSet<OrganizationPhoneNumber>();
	static final private long serialVersionUID = 9636702410571466l;
	private String uid;

	/** This constructor is intended for easy initialization in unit tests
	 * 
	 * @param owningObject 
	 */
	public OrganizationalNode(BusinessNetwork owningObject) {
		init(owningObject);
		addToOwningObject();
	}
	
	/** Default constructor for OrganizationalNode
	 */
	public OrganizationalNode() {
	}

	public void addAllToBusinessActor(Set<IBusinessActor> businessActor) {
		for ( IBusinessActor o : businessActor ) {
			addToBusinessActor(o);
		}
	}
	
	public void addAllToBusinessComponent(Set<IBusinessComponent> businessComponent) {
		for ( IBusinessComponent o : businessComponent ) {
			addToBusinessComponent(o);
		}
	}
	
	public void addAllToEMailAddress(Set<OrganizationEMailAddress> eMailAddress) {
		for ( OrganizationEMailAddress o : eMailAddress ) {
			addToEMailAddress(o);
		}
	}
	
	public void addAllToOrganizationFullfillsActorRole_businessActor(Set<OrganizationFullfillsActorRole> organizationFullfillsActorRole_businessActor) {
		for ( OrganizationFullfillsActorRole o : organizationFullfillsActorRole_businessActor ) {
			addToOrganizationFullfillsActorRole_businessActor(o);
		}
	}
	
	public void addAllToOrganization_iBusinessComponent_1_businessComponent(Set<Organization_iBusinessComponent_1> organization_iBusinessComponent_1_businessComponent) {
		for ( Organization_iBusinessComponent_1 o : organization_iBusinessComponent_1_businessComponent ) {
			addToOrganization_iBusinessComponent_1_businessComponent(o);
		}
	}
	
	public void addAllToPhoneNumber(Set<OrganizationPhoneNumber> phoneNumber) {
		for ( OrganizationPhoneNumber o : phoneNumber ) {
			addToPhoneNumber(o);
		}
	}
	
	public void addToBusinessActor(IBusinessActor businessActor) {
		if ( businessActor!=null ) {
			businessActor.z_internalRemoveFromOrganization(businessActor.getOrganization());
			businessActor.z_internalAddToOrganization(this);
			z_internalAddToBusinessActor(businessActor);
		}
	}
	
	public void addToBusinessComponent(IBusinessComponent businessComponent) {
		if ( businessComponent!=null ) {
			businessComponent.z_internalRemoveFromRepresentedOrganization(businessComponent.getRepresentedOrganization());
			businessComponent.z_internalAddToRepresentedOrganization(this);
			z_internalAddToBusinessComponent(businessComponent);
		}
	}
	
	public void addToEMailAddress(OrganizationEMailAddress eMailAddress) {
		if ( eMailAddress!=null ) {
			eMailAddress.z_internalRemoveFromOrganization(eMailAddress.getOrganization());
			eMailAddress.z_internalAddToOrganization(this);
			z_internalAddToEMailAddress(eMailAddress);
		}
	}
	
	public void addToOrganizationFullfillsActorRole_businessActor(OrganizationFullfillsActorRole organizationFullfillsActorRole_businessActor) {
		if ( organizationFullfillsActorRole_businessActor!=null ) {
			organizationFullfillsActorRole_businessActor.z_internalRemoveFromOrganization(organizationFullfillsActorRole_businessActor.getOrganization());
			organizationFullfillsActorRole_businessActor.z_internalAddToOrganization(this);
			z_internalAddToOrganizationFullfillsActorRole_businessActor(organizationFullfillsActorRole_businessActor);
		}
	}
	
	public void addToOrganization_iBusinessComponent_1_businessComponent(Organization_iBusinessComponent_1 organization_iBusinessComponent_1_businessComponent) {
		if ( organization_iBusinessComponent_1_businessComponent!=null ) {
			organization_iBusinessComponent_1_businessComponent.z_internalRemoveFromRepresentedOrganization(organization_iBusinessComponent_1_businessComponent.getRepresentedOrganization());
			organization_iBusinessComponent_1_businessComponent.z_internalAddToRepresentedOrganization(this);
			z_internalAddToOrganization_iBusinessComponent_1_businessComponent(organization_iBusinessComponent_1_businessComponent);
		}
	}
	
	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
		getBusinessNetwork().z_internalAddToOrganization((OrganizationalNode)this);
	}
	
	public void addToPhoneNumber(OrganizationPhoneNumber phoneNumber) {
		if ( phoneNumber!=null ) {
			phoneNumber.z_internalRemoveFromOrganization(phoneNumber.getOrganization());
			phoneNumber.z_internalAddToOrganization(this);
			z_internalAddToPhoneNumber(phoneNumber);
		}
	}
	
	static public Set<? extends OrganizationalNode> allInstances() {
		if ( mockedAllInstances==null ) {
			CmtPersistence session =org.opaeum.runtime.environment.Environment.getInstance().getComponent(CmtPersistence.class);
			return new HashSet(session.readAll(org.opaeum.runtime.bpm.organization.OrganizationalNode.class));
		} else {
			return mockedAllInstances;
		}
	}
	
	public void buildTreeFromXml(Element xml, Map<String, Object> map) {
		setUid(xml.getAttribute("uid"));
		if ( xml.getAttribute("name").length()>0 ) {
			setName(OpaeumLibraryForBPMFormatter.getInstance().parseString(xml.getAttribute("name")));
		}
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("phoneNumber") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("1861213202254517122")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						OrganizationPhoneNumber curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=Environment.getMetaInfoMap().newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.addToPhoneNumber(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("eMailAddress") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("6276678134555712740")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						OrganizationEMailAddress curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=Environment.getMetaInfoMap().newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.addToEMailAddress(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("businessCalendar") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("2759918346397932051")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						BusinessCalendar curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=Environment.getMetaInfoMap().newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.setBusinessCalendar(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("organizationFullfillsActorRole_businessActor") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("5544220265950373323")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						OrganizationFullfillsActorRole curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=Environment.getMetaInfoMap().newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.addToOrganizationFullfillsActorRole_businessActor(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
		}
	}
	
	public void clearBusinessActor() {
		removeAllFromBusinessActor(getBusinessActor());
	}
	
	public void clearBusinessComponent() {
		removeAllFromBusinessComponent(getBusinessComponent());
	}
	
	public void clearEMailAddress() {
		removeAllFromEMailAddress(getEMailAddress());
	}
	
	public void clearOrganizationFullfillsActorRole_businessActor() {
		removeAllFromOrganizationFullfillsActorRole_businessActor(getOrganizationFullfillsActorRole_businessActor());
	}
	
	public void clearOrganization_iBusinessComponent_1_businessComponent() {
		removeAllFromOrganization_iBusinessComponent_1_businessComponent(getOrganization_iBusinessComponent_1_businessComponent());
	}
	
	public void clearPhoneNumber() {
		removeAllFromPhoneNumber(getPhoneNumber());
	}
	
	public void copyShallowState(OrganizationalNode from, OrganizationalNode to) {
		to.setName(from.getName());
		if ( from.getBusinessCalendar()!=null ) {
			to.setBusinessCalendar(from.getBusinessCalendar().makeShallowCopy());
		}
	}
	
	public void copyState(OrganizationalNode from, OrganizationalNode to) {
		to.setName(from.getName());
		for ( OrganizationPhoneNumber child : from.getPhoneNumber() ) {
			to.addToPhoneNumber(child.makeCopy());
		}
		for ( OrganizationEMailAddress child : from.getEMailAddress() ) {
			to.addToEMailAddress(child.makeCopy());
		}
		if ( from.getBusinessCalendar()!=null ) {
			to.setBusinessCalendar(from.getBusinessCalendar().makeCopy());
		}
	}
	
	public void createComponents() {
	}
	
	public boolean equals(Object other) {
		if ( other instanceof OrganizationalNode ) {
			return other==this || ((OrganizationalNode)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	public Set<IBusinessActor> getBusinessActor() {
		Set<IBusinessActor> result = new HashSet<IBusinessActor>();
		for ( OrganizationFullfillsActorRole cur : this.getOrganizationFullfillsActorRole_businessActor() ) {
			result.add(cur.getBusinessActor());
		}
		return result;
	}
	
	@NumlMetaInfo(uuid="252060@_8YsOoFZFEeGj5_I7bIwNoA")
	public BusinessCalendar getBusinessCalendar() {
		BusinessCalendar result = this.businessCalendar;
		
		return result;
	}
	
	public Set<IBusinessComponent> getBusinessComponent() {
		Set<IBusinessComponent> result = new HashSet<IBusinessComponent>();
		for ( Organization_iBusinessComponent_1 cur : this.getOrganization_iBusinessComponent_1_businessComponent() ) {
			result.add(cur.getBusinessComponent());
		}
		return result;
	}
	
	@NumlMetaInfo(uuid="252060@_4uxKkUvREeGmqIr8YsFD4g")
	public BusinessNetwork getBusinessNetwork() {
		BusinessNetwork result = this.businessNetwork;
		
		return result;
	}
	
	public Set<CancelledEvent> getCancelledEvents() {
		return this.cancelledEvents;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	@NumlMetaInfo(uuid="252060@_JF99wEtqEeGd4cpyhpib9Q")
	public Set<OrganizationEMailAddress> getEMailAddress() {
		Set<OrganizationEMailAddress> result = this.eMailAddress;
		
		return result;
	}
	
	public Long getId() {
		return this.id;
	}
	
	@NumlMetaInfo(uuid="252060@_OorfwEtnEeGd4cpyhpib9Q")
	public String getName() {
		String result = this.name;
		
		return result;
	}
	
	public int getObjectVersion() {
		return this.objectVersion;
	}
	
	@NumlMetaInfo(uuid="252060@_WjvQ0UtyEeGElKTCe2jfDw252060@_WjvQ0EtyEeGElKTCe2jfDw")
	public Set<OrganizationFullfillsActorRole> getOrganizationFullfillsActorRole_businessActor() {
		Set<OrganizationFullfillsActorRole> result = this.organizationFullfillsActorRole_businessActor;
		
		return result;
	}
	
	public OrganizationFullfillsActorRole getOrganizationFullfillsActorRole_businessActorFor(IBusinessActor match) {
		for ( OrganizationFullfillsActorRole var : getOrganizationFullfillsActorRole_businessActor() ) {
			if ( var.getBusinessActor().equals(match) ) {
				return var;
			}
		}
		return null;
	}
	
	@NumlMetaInfo(uuid="252060@_vf2LYFYuEeGj5_I7bIwNoA252060@_vf4noFYuEeGj5_I7bIwNoA")
	public Set<Organization_iBusinessComponent_1> getOrganization_iBusinessComponent_1_businessComponent() {
		Set<Organization_iBusinessComponent_1> result = this.organization_iBusinessComponent_1_businessComponent;
		
		return result;
	}
	
	public Organization_iBusinessComponent_1 getOrganization_iBusinessComponent_1_businessComponentFor(IBusinessComponent match) {
		for ( Organization_iBusinessComponent_1 var : getOrganization_iBusinessComponent_1_businessComponent() ) {
			if ( var.getBusinessComponent().equals(match) ) {
				return var;
			}
		}
		return null;
	}
	
	public Set<OutgoingEvent> getOutgoingEvents() {
		return this.outgoingEvents;
	}
	
	public CompositionNode getOwningObject() {
		return getBusinessNetwork();
	}
	
	@NumlMetaInfo(uuid="252060@_HF7DgEtoEeGd4cpyhpib9Q")
	public Set<OrganizationPhoneNumber> getPhoneNumber() {
		Set<OrganizationPhoneNumber> result = this.phoneNumber;
		
		return result;
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
		createComponents();
	}
	
	public OrganizationalNode makeCopy() {
		OrganizationalNode result = new OrganizationalNode();
		copyState((OrganizationalNode)this,result);
		return result;
	}
	
	public OrganizationalNode makeShallowCopy() {
		OrganizationalNode result = new OrganizationalNode();
		copyShallowState((OrganizationalNode)this,result);
		result.setId(this.getId());
		return result;
	}
	
	public void markDeleted() {
		if ( getBusinessNetwork()!=null ) {
			getBusinessNetwork().z_internalRemoveFromOrganization(this);
		}
		for ( OrganizationPhoneNumber child : new ArrayList<OrganizationPhoneNumber>(getPhoneNumber()) ) {
			child.markDeleted();
		}
		for ( OrganizationEMailAddress child : new ArrayList<OrganizationEMailAddress>(getEMailAddress()) ) {
			child.markDeleted();
		}
		if ( getBusinessCalendar()!=null ) {
			getBusinessCalendar().markDeleted();
		}
		for ( OrganizationFullfillsActorRole child : new ArrayList<OrganizationFullfillsActorRole>(getOrganizationFullfillsActorRole_businessActor()) ) {
			child.markDeleted();
		}
		for ( Organization_iBusinessComponent_1 child : new ArrayList<Organization_iBusinessComponent_1>(getOrganization_iBusinessComponent_1_businessComponent()) ) {
			child.markDeleted();
		}
		setDeletedOn(new Date());
	}
	
	static public void mockAllInstances(Set<OrganizationalNode> newMocks) {
		mockedAllInstances=newMocks;
	}
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("phoneNumber") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("1861213202254517122")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((OrganizationPhoneNumber)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("eMailAddress") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("6276678134555712740")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((OrganizationEMailAddress)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("businessCalendar") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("2759918346397932051")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((BusinessCalendar)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("organizationFullfillsActorRole_businessActor") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("5544220265950373323")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((OrganizationFullfillsActorRole)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("organization_iBusinessComponent_1_businessComponent") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("6254493747225779734")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						addToOrganization_iBusinessComponent_1_businessComponent((Organization_iBusinessComponent_1)map.get(((Element)currentPropertyValueNode).getAttribute("uid")));
					}
				}
			}
		}
	}
	
	public void removeAllFromBusinessActor(Set<IBusinessActor> businessActor) {
		Set<IBusinessActor> tmp = new HashSet<IBusinessActor>(businessActor);
		for ( IBusinessActor o : tmp ) {
			removeFromBusinessActor(o);
		}
	}
	
	public void removeAllFromBusinessComponent(Set<IBusinessComponent> businessComponent) {
		Set<IBusinessComponent> tmp = new HashSet<IBusinessComponent>(businessComponent);
		for ( IBusinessComponent o : tmp ) {
			removeFromBusinessComponent(o);
		}
	}
	
	public void removeAllFromEMailAddress(Set<OrganizationEMailAddress> eMailAddress) {
		Set<OrganizationEMailAddress> tmp = new HashSet<OrganizationEMailAddress>(eMailAddress);
		for ( OrganizationEMailAddress o : tmp ) {
			removeFromEMailAddress(o);
		}
	}
	
	public void removeAllFromOrganizationFullfillsActorRole_businessActor(Set<OrganizationFullfillsActorRole> organizationFullfillsActorRole_businessActor) {
		Set<OrganizationFullfillsActorRole> tmp = new HashSet<OrganizationFullfillsActorRole>(organizationFullfillsActorRole_businessActor);
		for ( OrganizationFullfillsActorRole o : tmp ) {
			removeFromOrganizationFullfillsActorRole_businessActor(o);
		}
	}
	
	public void removeAllFromOrganization_iBusinessComponent_1_businessComponent(Set<Organization_iBusinessComponent_1> organization_iBusinessComponent_1_businessComponent) {
		Set<Organization_iBusinessComponent_1> tmp = new HashSet<Organization_iBusinessComponent_1>(organization_iBusinessComponent_1_businessComponent);
		for ( Organization_iBusinessComponent_1 o : tmp ) {
			removeFromOrganization_iBusinessComponent_1_businessComponent(o);
		}
	}
	
	public void removeAllFromPhoneNumber(Set<OrganizationPhoneNumber> phoneNumber) {
		Set<OrganizationPhoneNumber> tmp = new HashSet<OrganizationPhoneNumber>(phoneNumber);
		for ( OrganizationPhoneNumber o : tmp ) {
			removeFromPhoneNumber(o);
		}
	}
	
	public void removeFromBusinessActor(IBusinessActor businessActor) {
		if ( businessActor!=null ) {
			z_internalRemoveFromBusinessActor(businessActor);
		}
	}
	
	public void removeFromBusinessComponent(IBusinessComponent businessComponent) {
		if ( businessComponent!=null ) {
			z_internalRemoveFromBusinessComponent(businessComponent);
		}
	}
	
	public void removeFromEMailAddress(OrganizationEMailAddress eMailAddress) {
		if ( eMailAddress!=null ) {
			eMailAddress.z_internalRemoveFromOrganization(this);
			z_internalRemoveFromEMailAddress(eMailAddress);
		}
	}
	
	public void removeFromOrganizationFullfillsActorRole_businessActor(OrganizationFullfillsActorRole organizationFullfillsActorRole_businessActor) {
		if ( organizationFullfillsActorRole_businessActor!=null ) {
			organizationFullfillsActorRole_businessActor.z_internalRemoveFromOrganization(this);
			z_internalRemoveFromOrganizationFullfillsActorRole_businessActor(organizationFullfillsActorRole_businessActor);
		}
	}
	
	public void removeFromOrganization_iBusinessComponent_1_businessComponent(Organization_iBusinessComponent_1 organization_iBusinessComponent_1_businessComponent) {
		if ( organization_iBusinessComponent_1_businessComponent!=null ) {
			organization_iBusinessComponent_1_businessComponent.z_internalRemoveFromRepresentedOrganization(this);
			z_internalRemoveFromOrganization_iBusinessComponent_1_businessComponent(organization_iBusinessComponent_1_businessComponent);
		}
	}
	
	public void removeFromOwningObject() {
		this.markDeleted();
	}
	
	public void removeFromPhoneNumber(OrganizationPhoneNumber phoneNumber) {
		if ( phoneNumber!=null ) {
			phoneNumber.z_internalRemoveFromOrganization(this);
			z_internalRemoveFromPhoneNumber(phoneNumber);
		}
	}
	
	public void setBusinessActor(Set<IBusinessActor> businessActor) {
		this.clearBusinessActor();
		this.addAllToBusinessActor(businessActor);
	}
	
	public void setBusinessCalendar(BusinessCalendar businessCalendar) {
		BusinessCalendar oldValue = this.getBusinessCalendar();
		if ( oldValue==null ) {
			if ( businessCalendar!=null ) {
				OrganizationalNode oldOther = (OrganizationalNode)businessCalendar.getOrganization();
				businessCalendar.z_internalRemoveFromOrganization(oldOther);
				if ( oldOther != null ) {
					oldOther.z_internalRemoveFromBusinessCalendar(businessCalendar);
				}
				businessCalendar.z_internalAddToOrganization((OrganizationalNode)this);
			}
			this.z_internalAddToBusinessCalendar(businessCalendar);
		} else {
			if ( !oldValue.equals(businessCalendar) ) {
				oldValue.z_internalRemoveFromOrganization(this);
				z_internalRemoveFromBusinessCalendar(oldValue);
				if ( businessCalendar!=null ) {
					OrganizationalNode oldOther = (OrganizationalNode)businessCalendar.getOrganization();
					businessCalendar.z_internalRemoveFromOrganization(oldOther);
					if ( oldOther != null ) {
						oldOther.z_internalRemoveFromBusinessCalendar(businessCalendar);
					}
					businessCalendar.z_internalAddToOrganization((OrganizationalNode)this);
				}
				this.z_internalAddToBusinessCalendar(businessCalendar);
			}
		}
	}
	
	public void setBusinessComponent(Set<IBusinessComponent> businessComponent) {
		this.clearBusinessComponent();
		this.addAllToBusinessComponent(businessComponent);
	}
	
	public void setBusinessNetwork(BusinessNetwork businessNetwork) {
		if ( this.getBusinessNetwork()!=null ) {
			this.getBusinessNetwork().z_internalRemoveFromOrganization(this);
		}
		if ( businessNetwork!=null ) {
			businessNetwork.z_internalAddToOrganization(this);
			this.z_internalAddToBusinessNetwork(businessNetwork);
			setDeletedOn(Stdlib.FUTURE);
		} else {
			markDeleted();
		}
	}
	
	public void setCancelledEvents(Set<CancelledEvent> cancelledEvents) {
		this.cancelledEvents=cancelledEvents;
	}
	
	public void setDeletedOn(Date deletedOn) {
		this.deletedOn=deletedOn;
	}
	
	public void setEMailAddress(Set<OrganizationEMailAddress> eMailAddress) {
		this.clearEMailAddress();
		this.addAllToEMailAddress(eMailAddress);
	}
	
	public void setId(Long id) {
		this.id=id;
	}
	
	public void setName(String name) {
		this.z_internalAddToName(name);
	}
	
	public void setObjectVersion(int objectVersion) {
		this.objectVersion=objectVersion;
	}
	
	public void setOrganizationFullfillsActorRole_businessActor(Set<OrganizationFullfillsActorRole> organizationFullfillsActorRole_businessActor) {
		this.clearOrganizationFullfillsActorRole_businessActor();
		this.addAllToOrganizationFullfillsActorRole_businessActor(organizationFullfillsActorRole_businessActor);
	}
	
	public void setOrganization_iBusinessComponent_1_businessComponent(Set<Organization_iBusinessComponent_1> organization_iBusinessComponent_1_businessComponent) {
		this.clearOrganization_iBusinessComponent_1_businessComponent();
		this.addAllToOrganization_iBusinessComponent_1_businessComponent(organization_iBusinessComponent_1_businessComponent);
	}
	
	public void setOutgoingEvents(Set<OutgoingEvent> outgoingEvents) {
		this.outgoingEvents=outgoingEvents;
	}
	
	public void setPhoneNumber(Set<OrganizationPhoneNumber> phoneNumber) {
		this.clearPhoneNumber();
		this.addAllToPhoneNumber(phoneNumber);
	}
	
	public void setUid(String newUid) {
		this.uid=newUid;
	}
	
	public String toXmlReferenceString() {
		return "<OrganizationalNode uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<OrganizationalNode ");
		sb.append("classUuid=\"252060@_pZdQEEtmEeGd4cpyhpib9Q\" ");
		sb.append("className=\"org.opaeum.runtime.bpm.organization.OrganizationalNode\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		if ( getName()!=null ) {
			sb.append("name=\""+ OpaeumLibraryForBPMFormatter.getInstance().formatString(getName())+"\" ");
		}
		sb.append(">");
		sb.append("\n<phoneNumber propertyId=\"1861213202254517122\">");
		for ( OrganizationPhoneNumber phoneNumber : getPhoneNumber() ) {
			sb.append("\n" + phoneNumber.toXmlString());
		}
		sb.append("\n</phoneNumber>");
		sb.append("\n<eMailAddress propertyId=\"6276678134555712740\">");
		for ( OrganizationEMailAddress eMailAddress : getEMailAddress() ) {
			sb.append("\n" + eMailAddress.toXmlString());
		}
		sb.append("\n</eMailAddress>");
		if ( getBusinessCalendar()==null ) {
			sb.append("\n<businessCalendar/>");
		} else {
			sb.append("\n<businessCalendar propertyId=\"2759918346397932051\">");
			sb.append("\n" + getBusinessCalendar().toXmlString());
			sb.append("\n</businessCalendar>");
		}
		sb.append("\n<organizationFullfillsActorRole_businessActor propertyId=\"5544220265950373323\">");
		for ( OrganizationFullfillsActorRole organizationFullfillsActorRole_businessActor : getOrganizationFullfillsActorRole_businessActor() ) {
			sb.append("\n" + organizationFullfillsActorRole_businessActor.toXmlString());
		}
		sb.append("\n</organizationFullfillsActorRole_businessActor>");
		sb.append("\n<organization_iBusinessComponent_1_businessComponent propertyId=\"6254493747225779734\">");
		for ( Organization_iBusinessComponent_1 organization_iBusinessComponent_1_businessComponent : getOrganization_iBusinessComponent_1_businessComponent() ) {
			sb.append("\n" + organization_iBusinessComponent_1_businessComponent.toXmlReferenceString());
		}
		sb.append("\n</organization_iBusinessComponent_1_businessComponent>");
		sb.append("\n</OrganizationalNode>");
		return sb.toString();
	}
	
	public void z_internalAddToBusinessActor(IBusinessActor businessActor) {
		OrganizationFullfillsActorRole newOne = new OrganizationFullfillsActorRole(this,businessActor);
		this.z_internalAddToOrganizationFullfillsActorRole_businessActor(newOne);
		newOne.getBusinessActor().z_internalAddToOrganizationFullfillsActorRole_organization(newOne);
	}
	
	public void z_internalAddToBusinessCalendar(BusinessCalendar val) {
		this.businessCalendar=val;
	}
	
	public void z_internalAddToBusinessComponent(IBusinessComponent businessComponent) {
		Organization_iBusinessComponent_1 newOne = new Organization_iBusinessComponent_1(this,businessComponent);
		this.z_internalAddToOrganization_iBusinessComponent_1_businessComponent(newOne);
		newOne.getBusinessComponent().z_internalAddToOrganization_iBusinessComponent_1_representedOrganization(newOne);
	}
	
	public void z_internalAddToBusinessNetwork(BusinessNetwork val) {
		this.businessNetwork=val;
	}
	
	public void z_internalAddToEMailAddress(OrganizationEMailAddress val) {
		this.eMailAddress.add(val);
	}
	
	public void z_internalAddToName(String val) {
		this.name=val;
	}
	
	public void z_internalAddToOrganizationFullfillsActorRole_businessActor(OrganizationFullfillsActorRole val) {
		this.organizationFullfillsActorRole_businessActor.add(val);
	}
	
	public void z_internalAddToOrganization_iBusinessComponent_1_businessComponent(Organization_iBusinessComponent_1 val) {
		this.organization_iBusinessComponent_1_businessComponent.add(val);
	}
	
	public void z_internalAddToPhoneNumber(OrganizationPhoneNumber val) {
		this.phoneNumber.add(val);
	}
	
	public void z_internalRemoveFromBusinessActor(IBusinessActor businessActor) {
		for ( OrganizationFullfillsActorRole cur : new HashSet<OrganizationFullfillsActorRole>(this.organizationFullfillsActorRole_businessActor) ) {
			if ( cur.getBusinessActor().equals(businessActor) ) {
				cur.clear();
				break;
			}
		}
	}
	
	public void z_internalRemoveFromBusinessCalendar(BusinessCalendar val) {
		if ( getBusinessCalendar()!=null && val!=null && val.equals(getBusinessCalendar()) ) {
			this.businessCalendar=null;
		}
	}
	
	public void z_internalRemoveFromBusinessComponent(IBusinessComponent businessComponent) {
		for ( Organization_iBusinessComponent_1 cur : new HashSet<Organization_iBusinessComponent_1>(this.organization_iBusinessComponent_1_businessComponent) ) {
			if ( cur.getBusinessComponent().equals(businessComponent) ) {
				cur.clear();
				break;
			}
		}
	}
	
	public void z_internalRemoveFromBusinessNetwork(BusinessNetwork val) {
		if ( getBusinessNetwork()!=null && val!=null && val.equals(getBusinessNetwork()) ) {
			this.businessNetwork=null;
		}
	}
	
	public void z_internalRemoveFromEMailAddress(OrganizationEMailAddress val) {
		this.eMailAddress.remove(val);
	}
	
	public void z_internalRemoveFromName(String val) {
		if ( getName()!=null && val!=null && val.equals(getName()) ) {
			this.name=null;
		}
	}
	
	public void z_internalRemoveFromOrganizationFullfillsActorRole_businessActor(OrganizationFullfillsActorRole val) {
		this.organizationFullfillsActorRole_businessActor.remove(val);
	}
	
	public void z_internalRemoveFromOrganization_iBusinessComponent_1_businessComponent(Organization_iBusinessComponent_1 val) {
		this.organization_iBusinessComponent_1_businessComponent.remove(val);
	}
	
	public void z_internalRemoveFromPhoneNumber(OrganizationPhoneNumber val) {
		this.phoneNumber.remove(val);
	}

}