package structuredbusiness;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import org.hibernate.annotations.Where;
import org.opaeum.annotation.BusinessRole;
import org.opaeum.annotation.NumlMetaInfo;
import org.opaeum.annotation.PropertyMetaInfo;
import org.opaeum.audit.AuditMe;
import org.opaeum.runtime.bpm.organization.IBusinessRole;
import org.opaeum.runtime.bpm.organization.PersonNode;
import org.opaeum.runtime.bpm.organization.Person_iBusinessRole_1;
import org.opaeum.runtime.bpm.request.AbstractRequest;
import org.opaeum.runtime.bpm.request.Participation;
import org.opaeum.runtime.bpm.request.ParticipationInRequest;
import org.opaeum.runtime.bpm.request.ParticipationInTask;
import org.opaeum.runtime.bpm.request.ParticipationParticipant;
import org.opaeum.runtime.bpm.request.RequestParticipationKind;
import org.opaeum.runtime.bpm.request.TaskRequest;
import org.opaeum.runtime.domain.CancelledEvent;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.runtime.domain.HibernateEntity;
import org.opaeum.runtime.domain.IEventGenerator;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.domain.IntrospectionUtil;
import org.opaeum.runtime.domain.OutgoingEvent;
import org.opaeum.runtime.environment.Environment;
import org.opaeum.runtime.organization.IPersonNode;
import org.opaeum.runtime.persistence.AbstractPersistence;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import structuredbusiness.util.Stdlib;
import structuredbusiness.util.StructuredbusinessFormatter;

@AuditMe
@NumlMetaInfo(uuid="914890@_loLrkJHrEeGtApeO0lzlHQ")
@BusinessRole
@Filter(name="noDeletedObjects")
@org.hibernate.annotations.Entity(dynamicUpdate=true)
@AccessType(	"field")
@Table(name="technician")
@Inheritance(strategy=javax.persistence.InheritanceType.JOINED)
@Entity(name="Technician")
@DiscriminatorColumn(discriminatorType=javax.persistence.DiscriminatorType.STRING,name="type_descriminator")
public class Technician implements IPersistentObject, IEventGenerator, HibernateEntity, CompositionNode, IBusinessRole, Serializable {
	@Index(columnNames="branch_id",name="idx_technician_branch_id")
	@ManyToOne(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="branch_id",nullable=true)
	private Branch branch;
	@Transient
	private Set<CancelledEvent> cancelledEvents = new HashSet<CancelledEvent>();
		// Initialise to 1000 from 1970
	@Temporal(	javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="deleted_on")
	private Date deletedOn = Stdlib.FUTURE;
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.TABLE)
	private Long id;
	static private Set<Technician> mockedAllInstances;
	@Version
	@Column(name="object_version")
	private int objectVersion;
	@Transient
	private Set<OutgoingEvent> outgoingEvents = new HashSet<OutgoingEvent>();
	@Where(clause="participant_type='914890@_loLrkJHrEeGtApeO0lzlHQ'")
	@LazyCollection(	org.hibernate.annotations.LazyCollectionOption.TRUE)
	@Filter(condition="deleted_on > current_timestamp",name="noDeletedObjects")
	@OneToMany(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY,targetEntity=ParticipationParticipant.class)
	@JoinColumn(name="participation_participant_participation_id",nullable=true)
	private Set<ParticipationParticipant> participationParticipant_participation = new HashSet<ParticipationParticipant>();
	@Transient
	private AbstractPersistence persistence;
	@OneToOne(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="person_i_business_role_1_represented_person_id",nullable=true)
	private Person_iBusinessRole_1 person_iBusinessRole_1_representedPerson;
	@Transient
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	static final private long serialVersionUID = 8797403277162081281l;
	private String uid;

	/** This constructor is intended for easy initialization in unit tests
	 * 
	 * @param owningObject 
	 */
	public Technician(Branch owningObject) {
		init(owningObject);
		addToOwningObject();
	}
	
	/** Default constructor for Technician
	 */
	public Technician() {
	}

	public void addAllToParticipation(Set<Participation> participation) {
		for ( Participation o : participation ) {
			addToParticipation(o);
		}
	}
	
	public void addAllToParticipationParticipant_participation(Set<ParticipationParticipant> participationParticipant_participation) {
		for ( ParticipationParticipant o : participationParticipant_participation ) {
			addToParticipationParticipant_participation(o);
		}
	}
	
	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(property,listener);
	}
	
	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
		getBranch().z_internalAddToTechnician((Technician)this);
	}
	
	public void addToParticipation(Participation participation) {
		if ( participation!=null ) {
			participation.z_internalRemoveFromParticipant(participation.getParticipant());
			z_internalAddToParticipation(participation);
		}
	}
	
	public void addToParticipationParticipant_participation(ParticipationParticipant participationParticipant_participation) {
		if ( participationParticipant_participation!=null ) {
			participationParticipant_participation.z_internalRemoveFromParticipant(participationParticipant_participation.getParticipant());
			participationParticipant_participation.z_internalAddToParticipant(this);
			z_internalAddToParticipationParticipant_participation(participationParticipant_participation);
		}
	}
	
	static public Set<? extends Technician> allInstances(AbstractPersistence persistence) {
		if ( mockedAllInstances==null ) {
			return new HashSet(persistence.readAll(structuredbusiness.Technician.class));
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
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("person_iBusinessRole_1_representedPerson") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("6594897030343926470")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						Person_iBusinessRole_1 curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=Environment.getInstance().getMetaInfoMap().newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.setPerson_iBusinessRole_1_representedPerson(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("participationParticipant_participation") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("5579540379306504838")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						ParticipationParticipant curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=Environment.getInstance().getMetaInfoMap().newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.addToParticipationParticipant_participation(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
		}
	}
	
	public void clearParticipation() {
		removeAllFromParticipation(getParticipation());
	}
	
	public void clearParticipationParticipant_participation() {
		removeAllFromParticipationParticipant_participation(getParticipationParticipant_participation());
	}
	
	public void copyShallowState(Technician from, Technician to) {
	}
	
	public void copyState(Technician from, Technician to) {
	}
	
	public void createComponents() {
	}
	
	public ParticipationParticipant createParticipationParticipant_participation() {
		ParticipationParticipant newInstance= new ParticipationParticipant();
		newInstance.init(this);
		return newInstance;
	}
	
	public Person_iBusinessRole_1 createPerson_iBusinessRole_1_representedPerson() {
		Person_iBusinessRole_1 newInstance= new Person_iBusinessRole_1();
		newInstance.init(this);
		return newInstance;
	}
	
	public boolean equals(Object other) {
		if ( other instanceof Technician ) {
			return other==this || ((Technician)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=3879850637956564961l,opposite="technician",uuid="914890@_JaKw4ZKfEeGiJMBDeZRymA")
	@NumlMetaInfo(uuid="914890@_JaKw4ZKfEeGiJMBDeZRymA")
	public Branch getBranch() {
		Branch result = this.branch;
		
		return result;
	}
	
	public Set<CancelledEvent> getCancelledEvents() {
		return this.cancelledEvents;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	public Long getId() {
		return this.id;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=6185666218388591493l,uuid="252060@_rz7zsI6TEeCne5ArYLDbiA")
	@NumlMetaInfo(uuid="252060@_rz7zsI6TEeCne5ArYLDbiA")
	public Collection<AbstractRequest> getInitiatedRequests() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>(collect11());
		
		return result;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=5635486542671558270l,uuid="252060@_7MraII6lEeCFsPOcAnk69Q")
	@NumlMetaInfo(uuid="252060@_7MraII6lEeCFsPOcAnk69Q")
	public Collection<AbstractRequest> getInterestingRequests() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>(collect2());
		
		return result;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=5447021495172291044l,uuid="252060@_jSstQI6lEeCFsPOcAnk69Q")
	@NumlMetaInfo(uuid="252060@_jSstQI6lEeCFsPOcAnk69Q")
	public Collection<AbstractRequest> getManagedRequests() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>(collect9());
		
		return result;
	}
	
	public String getName() {
		return "Technician["+getId()+"]";
	}
	
	public int getObjectVersion() {
		return this.objectVersion;
	}
	
	public Set<OutgoingEvent> getOutgoingEvents() {
		return this.outgoingEvents;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=6404162095298970578l,uuid="252060@_NYHP0I6mEeCFsPOcAnk69Q")
	@NumlMetaInfo(uuid="252060@_NYHP0I6mEeCFsPOcAnk69Q")
	public Collection<TaskRequest> getOwnedTaskRequests() {
		Collection<TaskRequest> result = new ArrayList<TaskRequest>(collect5());
		
		return result;
	}
	
	public CompositionNode getOwningObject() {
		return getBranch();
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=4480510548106225415l,opposite="participant",uuid="252060@_3YyGkYoXEeCPduia_-NbFw")
	public Set<Participation> getParticipation() {
		Set<Participation> result = new HashSet<Participation>();
		for ( ParticipationParticipant cur : this.getParticipationParticipant_participation() ) {
			result.add(cur.getParticipation());
		}
		return result;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=true,opaeumId=5579540379306504838l,opposite="participant",uuid="252060@_3YyGkIoXEeCPduia_-NbFw")
	@NumlMetaInfo(uuid="252060@_YgstsI29EeCrtavWRHwoHg@252060@_3YyGkIoXEeCPduia_-NbFw")
	public Set<ParticipationParticipant> getParticipationParticipant_participation() {
		Set<ParticipationParticipant> result = this.participationParticipant_participation;
		
		return result;
	}
	
	public ParticipationParticipant getParticipationParticipant_participationFor(Participation match) {
		for ( ParticipationParticipant var : getParticipationParticipant_participation() ) {
			if ( var.getParticipation().equals(match) ) {
				return var;
			}
		}
		return null;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=2234431193389771664l,uuid="252060@_TfLFAJBkEeCWM9wKKqKWag")
	@NumlMetaInfo(uuid="252060@_TfLFAJBkEeCWM9wKKqKWag")
	public Collection<ParticipationInRequest> getParticipationsInRequests() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>(collect7());
		
		return result;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=6858863738991536174l,uuid="252060@_DIGv8JBkEeCWM9wKKqKWag")
	@NumlMetaInfo(uuid="252060@_DIGv8JBkEeCWM9wKKqKWag")
	public Collection<ParticipationInTask> getParticipationsInTasks() {
		Collection<ParticipationInTask> result = new ArrayList<ParticipationInTask>(collect4());
		
		return result;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=true,opaeumId=6594897030343926470l,opposite="businessRole",uuid="252060@_3lcZgFYuEeGj5_I7bIwNoA")
	@NumlMetaInfo(uuid="252060@_tH0fAIoVEeCLqpffVZYAlw@252060@_3lcZgFYuEeGj5_I7bIwNoA")
	public Person_iBusinessRole_1 getPerson_iBusinessRole_1_representedPerson() {
		Person_iBusinessRole_1 result = this.person_iBusinessRole_1_representedPerson;
		
		return result;
	}
	
	public Person_iBusinessRole_1 getPerson_iBusinessRole_1_representedPersonFor(PersonNode match) {
		if ( person_iBusinessRole_1_representedPerson.getRepresentedPerson().equals(match) ) {
			return person_iBusinessRole_1_representedPerson;
		} else {
			return null;
		}
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=8923586012099856841l,opposite="businessRole",uuid="252060@_3lcZgVYuEeGj5_I7bIwNoA")
	public PersonNode getRepresentedPerson() {
		PersonNode result = null;
		if ( this.person_iBusinessRole_1_representedPerson!=null ) {
			result = this.person_iBusinessRole_1_representedPerson.getRepresentedPerson();
		}
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
		this.z_internalAddToBranch((Branch)owner);
		createComponents();
	}
	
	public Technician makeCopy() {
		Technician result = new Technician();
		copyState((Technician)this,result);
		return result;
	}
	
	public Technician makeShallowCopy() {
		Technician result = new Technician();
		copyShallowState((Technician)this,result);
		result.setId(this.getId());
		return result;
	}
	
	public void markDeleted() {
		if ( getBranch()!=null ) {
			getBranch().z_internalRemoveFromTechnician(this);
		}
		if ( getRepresentedPerson()!=null ) {
			getRepresentedPerson().z_internalRemoveFromBusinessRole(this);
		}
		if ( getPerson_iBusinessRole_1_representedPerson()!=null ) {
			getPerson_iBusinessRole_1_representedPerson().markDeleted();
		}
		for ( ParticipationParticipant child : new ArrayList<ParticipationParticipant>(getParticipationParticipant_participation()) ) {
			child.markDeleted();
		}
		setDeletedOn(new Date());
	}
	
	static public void mockAllInstances(Set<Technician> newMocks) {
		mockedAllInstances=newMocks;
	}
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("person_iBusinessRole_1_representedPerson") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("6594897030343926470")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((Person_iBusinessRole_1)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("participationParticipant_participation") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("5579540379306504838")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((ParticipationParticipant)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
		}
	}
	
	public void removeAllFromParticipation(Set<Participation> participation) {
		Set<Participation> tmp = new HashSet<Participation>(participation);
		for ( Participation o : tmp ) {
			removeFromParticipation(o);
		}
	}
	
	public void removeAllFromParticipationParticipant_participation(Set<ParticipationParticipant> participationParticipant_participation) {
		Set<ParticipationParticipant> tmp = new HashSet<ParticipationParticipant>(participationParticipant_participation);
		for ( ParticipationParticipant o : tmp ) {
			removeFromParticipationParticipant_participation(o);
		}
	}
	
	public void removeFromOwningObject() {
		this.markDeleted();
	}
	
	public void removeFromParticipation(Participation participation) {
		if ( participation!=null ) {
			z_internalRemoveFromParticipation(participation);
		}
	}
	
	public void removeFromParticipationParticipant_participation(ParticipationParticipant participationParticipant_participation) {
		if ( participationParticipant_participation!=null ) {
			participationParticipant_participation.z_internalRemoveFromParticipant(this);
			z_internalRemoveFromParticipationParticipant_participation(participationParticipant_participation);
		}
	}
	
	public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(property,listener);
	}
	
	public void setBranch(Branch branch) {
		Branch oldValue = this.getBranch();
		propertyChangeSupport.firePropertyChange("branch",getBranch(),branch);
		if ( oldValue==null ) {
			if ( branch!=null ) {
				Technician oldOther = (Technician)branch.getTechnician();
				branch.z_internalRemoveFromTechnician(oldOther);
				if ( oldOther != null ) {
					oldOther.z_internalRemoveFromBranch(branch);
				}
				branch.z_internalAddToTechnician((Technician)this);
			}
			this.z_internalAddToBranch(branch);
		} else {
			if ( !oldValue.equals(branch) ) {
				oldValue.z_internalRemoveFromTechnician(this);
				z_internalRemoveFromBranch(oldValue);
				if ( branch!=null ) {
					Technician oldOther = (Technician)branch.getTechnician();
					branch.z_internalRemoveFromTechnician(oldOther);
					if ( oldOther != null ) {
						oldOther.z_internalRemoveFromBranch(branch);
					}
					branch.z_internalAddToTechnician((Technician)this);
				}
				this.z_internalAddToBranch(branch);
			}
		}
	}
	
	public void setCancelledEvents(Set<CancelledEvent> cancelledEvents) {
		this.cancelledEvents=cancelledEvents;
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
	
	public void setOutgoingEvents(Set<OutgoingEvent> outgoingEvents) {
		this.outgoingEvents=outgoingEvents;
	}
	
	public void setParticipation(Set<Participation> participation) {
		propertyChangeSupport.firePropertyChange("participation",getParticipation(),participation);
		this.clearParticipation();
		this.addAllToParticipation(participation);
	}
	
	public void setParticipationParticipant_participation(Set<ParticipationParticipant> participationParticipant_participation) {
		propertyChangeSupport.firePropertyChange("participationParticipant_participation",getParticipationParticipant_participation(),participationParticipant_participation);
		this.clearParticipationParticipant_participation();
		this.addAllToParticipationParticipant_participation(participationParticipant_participation);
	}
	
	public void setPerson_iBusinessRole_1_representedPerson(Person_iBusinessRole_1 person_iBusinessRole_1_representedPerson) {
		Person_iBusinessRole_1 oldValue = this.getPerson_iBusinessRole_1_representedPerson();
		propertyChangeSupport.firePropertyChange("person_iBusinessRole_1_representedPerson",getPerson_iBusinessRole_1_representedPerson(),person_iBusinessRole_1_representedPerson);
		if ( oldValue==null ) {
			if ( person_iBusinessRole_1_representedPerson!=null ) {
				Technician oldOther = (Technician)person_iBusinessRole_1_representedPerson.getBusinessRole();
				person_iBusinessRole_1_representedPerson.z_internalRemoveFromBusinessRole(oldOther);
				if ( oldOther != null ) {
					oldOther.z_internalRemoveFromPerson_iBusinessRole_1_representedPerson(person_iBusinessRole_1_representedPerson);
				}
				person_iBusinessRole_1_representedPerson.z_internalAddToBusinessRole((Technician)this);
			}
			this.z_internalAddToPerson_iBusinessRole_1_representedPerson(person_iBusinessRole_1_representedPerson);
		} else {
			if ( !oldValue.equals(person_iBusinessRole_1_representedPerson) ) {
				oldValue.z_internalRemoveFromBusinessRole(this);
				z_internalRemoveFromPerson_iBusinessRole_1_representedPerson(oldValue);
				if ( person_iBusinessRole_1_representedPerson!=null ) {
					Technician oldOther = (Technician)person_iBusinessRole_1_representedPerson.getBusinessRole();
					person_iBusinessRole_1_representedPerson.z_internalRemoveFromBusinessRole(oldOther);
					if ( oldOther != null ) {
						oldOther.z_internalRemoveFromPerson_iBusinessRole_1_representedPerson(person_iBusinessRole_1_representedPerson);
					}
					person_iBusinessRole_1_representedPerson.z_internalAddToBusinessRole((Technician)this);
				}
				this.z_internalAddToPerson_iBusinessRole_1_representedPerson(person_iBusinessRole_1_representedPerson);
			}
		}
	}
	
	public void setRepresentedPerson(IPersonNode p) {
		setRepresentedPerson((PersonNode)p);
	}
	
	public void setRepresentedPerson(PersonNode representedPerson) {
		propertyChangeSupport.firePropertyChange("representedPerson",getRepresentedPerson(),representedPerson);
		if ( this.getRepresentedPerson()!=null ) {
			this.getRepresentedPerson().z_internalRemoveFromBusinessRole(this);
		}
		if ( representedPerson!=null ) {
			this.z_internalAddToRepresentedPerson(representedPerson);
		}
	}
	
	public void setUid(String newUid) {
		this.uid=newUid;
	}
	
	public String toXmlReferenceString() {
		return "<Technician uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<Technician ");
		sb.append("classUuid=\"914890@_loLrkJHrEeGtApeO0lzlHQ\" ");
		sb.append("className=\"structuredbusiness.Technician\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		sb.append(">");
		if ( getPerson_iBusinessRole_1_representedPerson()==null ) {
			sb.append("\n<person_iBusinessRole_1_representedPerson/>");
		} else {
			sb.append("\n<person_iBusinessRole_1_representedPerson propertyId=\"6594897030343926470\">");
			sb.append("\n" + getPerson_iBusinessRole_1_representedPerson().toXmlString());
			sb.append("\n</person_iBusinessRole_1_representedPerson>");
		}
		sb.append("\n<participationParticipant_participation propertyId=\"5579540379306504838\">");
		for ( ParticipationParticipant participationParticipant_participation : getParticipationParticipant_participation() ) {
			sb.append("\n" + participationParticipant_participation.toXmlString());
		}
		sb.append("\n</participationParticipant_participation>");
		sb.append("\n</Technician>");
		return sb.toString();
	}
	
	public void z_internalAddToBranch(Branch val) {
		this.branch=val;
	}
	
	public void z_internalAddToParticipation(Participation participation) {
		ParticipationParticipant newOne = new ParticipationParticipant(this,participation);
		this.z_internalAddToParticipationParticipant_participation(newOne);
		newOne.getParticipation().z_internalAddToParticipationParticipant_participant(newOne);
	}
	
	public void z_internalAddToParticipationParticipant_participation(ParticipationParticipant val) {
		this.participationParticipant_participation.add(val);
	}
	
	public void z_internalAddToPerson_iBusinessRole_1_representedPerson(Person_iBusinessRole_1 val) {
		this.person_iBusinessRole_1_representedPerson=val;
	}
	
	public void z_internalAddToRepresentedPerson(PersonNode representedPerson) {
		Person_iBusinessRole_1 newOne = new Person_iBusinessRole_1(this,representedPerson);
		this.z_internalAddToPerson_iBusinessRole_1_representedPerson(newOne);
		newOne.getRepresentedPerson().z_internalAddToPerson_iBusinessRole_1_businessRole(newOne);
	}
	
	public void z_internalRemoveFromBranch(Branch val) {
		if ( getBranch()!=null && val!=null && val.equals(getBranch()) ) {
			this.branch=null;
			this.branch=null;
		}
	}
	
	public void z_internalRemoveFromParticipation(Participation participation) {
		for ( ParticipationParticipant cur : new HashSet<ParticipationParticipant>(this.participationParticipant_participation) ) {
			if ( cur.getParticipation().equals(participation) ) {
				cur.clear();
				break;
			}
		}
	}
	
	public void z_internalRemoveFromParticipationParticipant_participation(ParticipationParticipant val) {
		this.participationParticipant_participation.remove(val);
	}
	
	public void z_internalRemoveFromPerson_iBusinessRole_1_representedPerson(Person_iBusinessRole_1 val) {
		if ( getPerson_iBusinessRole_1_representedPerson()!=null && val!=null && val.equals(getPerson_iBusinessRole_1_representedPerson()) ) {
			this.person_iBusinessRole_1_representedPerson=null;
			this.person_iBusinessRole_1_representedPerson=null;
		}
	}
	
	public void z_internalRemoveFromRepresentedPerson(PersonNode representedPerson) {
		if ( this.person_iBusinessRole_1_representedPerson!=null ) {
			this.person_iBusinessRole_1_representedPerson.clear();
		}
	}
	
	/** Implements self.participationsInRequests->select(temp1 : ParticipationInRequest | temp1.kind.=(OpaeumLibraryForBPM::request::RequestParticipationKind::initiator))->collect(temp2 : ParticipationInRequest | temp2.request)
	 */
	private Collection<AbstractRequest> collect11() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>();
		for ( ParticipationInRequest temp2 : select10() ) {
			AbstractRequest bodyExpResult = temp2.getRequest();
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements self.participationsInRequests->select(temp1 : ParticipationInRequest | temp1.kind.=(OpaeumLibraryForBPM::request::RequestParticipationKind::stakeholder))->collect(temp2 : ParticipationInRequest | temp2.request)
	 */
	private Collection<AbstractRequest> collect2() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>();
		for ( ParticipationInRequest temp2 : select1() ) {
			AbstractRequest bodyExpResult = temp2.getRequest();
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements self.participation->select(temp1 : Participation | temp1.oclIsKindOf(OpaeumLibraryForBPM::request::ParticipationInTask))->collect(temp2 : Participation | temp2.oclAsType(OpaeumLibraryForBPM::request::ParticipationInTask))
	 */
	private Collection<ParticipationInTask> collect4() {
		Collection<ParticipationInTask> result = new ArrayList<ParticipationInTask>();
		for ( Participation temp2 : select3() ) {
			ParticipationInTask bodyExpResult = ((ParticipationInTask) temp2);
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements self.participationsInTasks->collect(temp1 : ParticipationInTask | temp1.taskRequest)
	 */
	private Collection<TaskRequest> collect5() {
		Collection<TaskRequest> result = new ArrayList<TaskRequest>();
		for ( ParticipationInTask temp1 : this.getParticipationsInTasks() ) {
			TaskRequest bodyExpResult = temp1.getTaskRequest();
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements self.participation->select(temp1 : Participation | temp1.oclIsKindOf(OpaeumLibraryForBPM::request::ParticipationInRequest))->collect(temp2 : Participation | temp2.oclAsType(OpaeumLibraryForBPM::request::ParticipationInRequest))
	 */
	private Collection<ParticipationInRequest> collect7() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>();
		for ( Participation temp2 : select6() ) {
			ParticipationInRequest bodyExpResult = ((ParticipationInRequest) temp2);
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements self.participationsInRequests->select(temp1 : ParticipationInRequest | temp1.kind.=(OpaeumLibraryForBPM::request::RequestParticipationKind::businessOwner))->collect(temp2 : ParticipationInRequest | temp2.request)
	 */
	private Collection<AbstractRequest> collect9() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>();
		for ( ParticipationInRequest temp2 : select8() ) {
			AbstractRequest bodyExpResult = temp2.getRequest();
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements self.participationsInRequests->select(temp1 : ParticipationInRequest | temp1.kind.=(OpaeumLibraryForBPM::request::RequestParticipationKind::stakeholder))
	 */
	private Collection<ParticipationInRequest> select1() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>();
		for ( ParticipationInRequest temp1 : this.getParticipationsInRequests() ) {
			if ( (temp1.getKind().equals( RequestParticipationKind.STAKEHOLDER)) ) {
				result.add( temp1 );
			}
		}
		return result;
	}
	
	/** Implements self.participationsInRequests->select(temp1 : ParticipationInRequest | temp1.kind.=(OpaeumLibraryForBPM::request::RequestParticipationKind::initiator))
	 */
	private Collection<ParticipationInRequest> select10() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>();
		for ( ParticipationInRequest temp1 : this.getParticipationsInRequests() ) {
			if ( (temp1.getKind().equals( RequestParticipationKind.INITIATOR)) ) {
				result.add( temp1 );
			}
		}
		return result;
	}
	
	/** Implements self.participation->select(temp1 : Participation | temp1.oclIsKindOf(OpaeumLibraryForBPM::request::ParticipationInTask))
	 */
	private Set<Participation> select3() {
		Set<Participation> result = new HashSet<Participation>();
		for ( Participation temp1 : this.getParticipation() ) {
			if ( (temp1 instanceof ParticipationInTask) ) {
				result.add( temp1 );
			}
		}
		return result;
	}
	
	/** Implements self.participation->select(temp1 : Participation | temp1.oclIsKindOf(OpaeumLibraryForBPM::request::ParticipationInRequest))
	 */
	private Set<Participation> select6() {
		Set<Participation> result = new HashSet<Participation>();
		for ( Participation temp1 : this.getParticipation() ) {
			if ( (temp1 instanceof ParticipationInRequest) ) {
				result.add( temp1 );
			}
		}
		return result;
	}
	
	/** Implements self.participationsInRequests->select(temp1 : ParticipationInRequest | temp1.kind.=(OpaeumLibraryForBPM::request::RequestParticipationKind::businessOwner))
	 */
	private Collection<ParticipationInRequest> select8() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>();
		for ( ParticipationInRequest temp1 : this.getParticipationsInRequests() ) {
			if ( (temp1.getKind().equals( RequestParticipationKind.BUSINESSOWNER)) ) {
				result.add( temp1 );
			}
		}
		return result;
	}

}