package structuredbusiness;

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
import org.opaeum.annotation.Property;
import org.opaeum.runtime.bpm.organization.IBusinessRole;
import org.opaeum.runtime.bpm.organization.PersonNode;
import org.opaeum.runtime.bpm.organization.Person_iBusinessRole_1;
import org.opaeum.runtime.bpm.request.AbstractRequest;
import org.opaeum.runtime.bpm.request.Participation;
import org.opaeum.runtime.bpm.request.ParticipationInRequest;
import org.opaeum.runtime.bpm.request.ParticipationInTask;
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
import org.opaeum.runtime.persistence.CmtPersistence;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import structuredbusiness.util.Stdlib;
import structuredbusiness.util.StructuredbusinessFormatter;

@NumlMetaInfo(uuid="914890@_mec6wGCfEeG6xvYqJACneg")
@BusinessRole
@Filter(name="noDeletedObjects")
@org.hibernate.annotations.Entity(dynamicUpdate=true)
@AccessType(	"field")
@Table(name="accountant")
@Inheritance(strategy=javax.persistence.InheritanceType.JOINED)
@Entity(name="Accountant")
@DiscriminatorColumn(discriminatorType=javax.persistence.DiscriminatorType.STRING,name="type_descriminator")
public class Accountant implements IPersistentObject, IEventGenerator, HibernateEntity, CompositionNode, IBusinessRole, Serializable {
	@Transient
	private Set<CancelledEvent> cancelledEvents = new HashSet<CancelledEvent>();
		// Initialise to 1000 from 1970
	@Temporal(	javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="deleted_on")
	private Date deletedOn = Stdlib.FUTURE;
	@Index(columnNames="dishwashers_inc_id",name="idx_accountant_dishwashers_inc_id")
	@ManyToOne(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="dishwashers_inc_id",nullable=true)
	private DishwashersInc dishwashersInc;
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.TABLE)
	private Long id;
	static private Set<Accountant> mockedAllInstances;
	@Version
	@Column(name="object_version")
	private int objectVersion;
	@Transient
	private Set<OutgoingEvent> outgoingEvents = new HashSet<OutgoingEvent>();
	@Where(clause="participant_type='914890@_mec6wGCfEeG6xvYqJACneg'")
	@LazyCollection(	org.hibernate.annotations.LazyCollectionOption.TRUE)
	@Filter(condition="deleted_on > current_timestamp",name="noDeletedObjects")
	@OneToMany(fetch=javax.persistence.FetchType.LAZY,targetEntity=Participation.class)
	@JoinColumn(name="participation_id",nullable=true)
	private Set<Participation> participation = new HashSet<Participation>();
	@Transient
	private AbstractPersistence persistence;
	@OneToOne(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="person_i_business_role_1_represented_person_id",nullable=true)
	private Person_iBusinessRole_1 person_iBusinessRole_1_representedPerson;
	static final private long serialVersionUID = 3718403143352034683l;
	private String uid;

	/** This constructor is intended for easy initialization in unit tests
	 * 
	 * @param owningObject 
	 */
	public Accountant(DishwashersInc owningObject) {
		init(owningObject);
		addToOwningObject();
	}
	
	/** Default constructor for Accountant
	 */
	public Accountant() {
	}

	public void addAllToParticipation(Set<Participation> participation) {
		for ( Participation o : participation ) {
			addToParticipation(o);
		}
	}
	
	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
		getDishwashersInc().z_internalAddToAccountant((Accountant)this);
	}
	
	public void addToParticipation(Participation participation) {
		if ( participation!=null ) {
			participation.z_internalRemoveFromParticipant(participation.getParticipant());
			participation.z_internalAddToParticipant(this);
			z_internalAddToParticipation(participation);
		}
	}
	
	static public Set<? extends Accountant> allInstances() {
		if ( mockedAllInstances==null ) {
			CmtPersistence session =org.opaeum.runtime.environment.Environment.getInstance().getComponent(CmtPersistence.class);
			return new HashSet(session.readAll(structuredbusiness.Accountant.class));
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
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("person_iBusinessRole_1_representedPerson") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("742593574795479974")) ) {
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
		}
	}
	
	public void clearParticipation() {
		removeAllFromParticipation(getParticipation());
	}
	
	public void copyShallowState(Accountant from, Accountant to) {
	}
	
	public void copyState(Accountant from, Accountant to) {
	}
	
	public void createComponents() {
	}
	
	public Person_iBusinessRole_1 createPerson_iBusinessRole_1_representedPerson() {
		Person_iBusinessRole_1 newInstance= new Person_iBusinessRole_1();
		newInstance.init(this);
		return newInstance;
	}
	
	public boolean equals(Object other) {
		if ( other instanceof Accountant ) {
			return other==this || ((Accountant)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	public Set<CancelledEvent> getCancelledEvents() {
		return this.cancelledEvents;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	@Property(isComposite=false,opposite="accountant")
	@NumlMetaInfo(uuid="914890@_nIu3gWO4EeGodsKwWzy5aw")
	public DishwashersInc getDishwashersInc() {
		DishwashersInc result = this.dishwashersInc;
		
		return result;
	}
	
	public Long getId() {
		return this.id;
	}
	
	@NumlMetaInfo(uuid="252060@_rz7zsI6TEeCne5ArYLDbiA")
	public Collection<AbstractRequest> getInitiatedRequests() {
		Collection<AbstractRequest> result = collect9();
		
		return result;
	}
	
	@NumlMetaInfo(uuid="252060@_7MraII6lEeCFsPOcAnk69Q")
	public Collection<AbstractRequest> getInterestingRequests() {
		Collection<AbstractRequest> result = collect2();
		
		return result;
	}
	
	@NumlMetaInfo(uuid="252060@_jSstQI6lEeCFsPOcAnk69Q")
	public Collection<AbstractRequest> getManagedRequests() {
		Collection<AbstractRequest> result = collect4();
		
		return result;
	}
	
	public String getName() {
		return "Accountant["+getId()+"]";
	}
	
	public int getObjectVersion() {
		return this.objectVersion;
	}
	
	public Set<OutgoingEvent> getOutgoingEvents() {
		return this.outgoingEvents;
	}
	
	@NumlMetaInfo(uuid="252060@_NYHP0I6mEeCFsPOcAnk69Q")
	public Collection<TaskRequest> getOwnedTaskRequests() {
		Collection<TaskRequest> result = collect7();
		
		return result;
	}
	
	public CompositionNode getOwningObject() {
		return getDishwashersInc();
	}
	
	@Property(isComposite=false,opposite="participant")
	@NumlMetaInfo(uuid="252060@_3YyGkYoXEeCPduia_-NbFw")
	public Set<Participation> getParticipation() {
		Set<Participation> result = this.participation;
		
		return result;
	}
	
	@NumlMetaInfo(uuid="252060@_TfLFAJBkEeCWM9wKKqKWag")
	public Collection<ParticipationInRequest> getParticipationsInRequests() {
		Collection<ParticipationInRequest> result = collect6();
		
		return result;
	}
	
	@NumlMetaInfo(uuid="252060@_DIGv8JBkEeCWM9wKKqKWag")
	public Collection<ParticipationInTask> getParticipationsInTasks() {
		Collection<ParticipationInTask> result = collect11();
		
		return result;
	}
	
	@Property(isComposite=true,opposite="businessRole")
	@NumlMetaInfo(uuid="252060@_3lcZgVYuEeGj5_I7bIwNoA252060@_3lcZgFYuEeGj5_I7bIwNoA")
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
		this.z_internalAddToDishwashersInc((DishwashersInc)owner);
		createComponents();
	}
	
	public Accountant makeCopy() {
		Accountant result = new Accountant();
		copyState((Accountant)this,result);
		return result;
	}
	
	public Accountant makeShallowCopy() {
		Accountant result = new Accountant();
		copyShallowState((Accountant)this,result);
		result.setId(this.getId());
		return result;
	}
	
	public void markDeleted() {
		if ( getRepresentedPerson()!=null ) {
			getRepresentedPerson().z_internalRemoveFromBusinessRole(this);
		}
		if ( getDishwashersInc()!=null ) {
			getDishwashersInc().z_internalRemoveFromAccountant(this);
		}
		setDeletedOn(new Date());
	}
	
	static public void mockAllInstances(Set<Accountant> newMocks) {
		mockedAllInstances=newMocks;
	}
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("person_iBusinessRole_1_representedPerson") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("742593574795479974")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((Person_iBusinessRole_1)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
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
	
	public void removeFromOwningObject() {
		this.markDeleted();
	}
	
	public void removeFromParticipation(Participation participation) {
		if ( participation!=null ) {
			participation.z_internalRemoveFromParticipant(this);
			z_internalRemoveFromParticipation(participation);
		}
	}
	
	public void setCancelledEvents(Set<CancelledEvent> cancelledEvents) {
		this.cancelledEvents=cancelledEvents;
	}
	
	public void setDeletedOn(Date deletedOn) {
		this.deletedOn=deletedOn;
	}
	
	public void setDishwashersInc(DishwashersInc dishwashersInc) {
		if ( this.getDishwashersInc()!=null ) {
			this.getDishwashersInc().z_internalRemoveFromAccountant(this);
		}
		if ( dishwashersInc!=null ) {
			dishwashersInc.z_internalAddToAccountant(this);
			this.z_internalAddToDishwashersInc(dishwashersInc);
			setDeletedOn(Stdlib.FUTURE);
		} else {
			markDeleted();
		}
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
		this.clearParticipation();
		this.addAllToParticipation(participation);
	}
	
	public void setPerson_iBusinessRole_1_representedPerson(Person_iBusinessRole_1 person_iBusinessRole_1_representedPerson) {
		Person_iBusinessRole_1 oldValue = this.getPerson_iBusinessRole_1_representedPerson();
		if ( oldValue==null ) {
			if ( person_iBusinessRole_1_representedPerson!=null ) {
				Accountant oldOther = (Accountant)person_iBusinessRole_1_representedPerson.getBusinessRole();
				person_iBusinessRole_1_representedPerson.z_internalRemoveFromBusinessRole(oldOther);
				if ( oldOther != null ) {
					oldOther.z_internalRemoveFromPerson_iBusinessRole_1_representedPerson(person_iBusinessRole_1_representedPerson);
				}
				person_iBusinessRole_1_representedPerson.z_internalAddToBusinessRole((Accountant)this);
			}
			this.z_internalAddToPerson_iBusinessRole_1_representedPerson(person_iBusinessRole_1_representedPerson);
		} else {
			if ( !oldValue.equals(person_iBusinessRole_1_representedPerson) ) {
				oldValue.z_internalRemoveFromBusinessRole(this);
				z_internalRemoveFromPerson_iBusinessRole_1_representedPerson(oldValue);
				if ( person_iBusinessRole_1_representedPerson!=null ) {
					Accountant oldOther = (Accountant)person_iBusinessRole_1_representedPerson.getBusinessRole();
					person_iBusinessRole_1_representedPerson.z_internalRemoveFromBusinessRole(oldOther);
					if ( oldOther != null ) {
						oldOther.z_internalRemoveFromPerson_iBusinessRole_1_representedPerson(person_iBusinessRole_1_representedPerson);
					}
					person_iBusinessRole_1_representedPerson.z_internalAddToBusinessRole((Accountant)this);
				}
				this.z_internalAddToPerson_iBusinessRole_1_representedPerson(person_iBusinessRole_1_representedPerson);
			}
		}
	}
	
	public void setRepresentedPerson(IPersonNode p) {
		setRepresentedPerson((PersonNode)p);
	}
	
	public void setRepresentedPerson(PersonNode representedPerson) {
		if ( this.getRepresentedPerson()!=null ) {
			this.getRepresentedPerson().z_internalRemoveFromBusinessRole(this);
		}
		if ( representedPerson!=null ) {
			representedPerson.z_internalAddToBusinessRole(this);
			this.z_internalAddToRepresentedPerson(representedPerson);
		}
	}
	
	public void setUid(String newUid) {
		this.uid=newUid;
	}
	
	public String toXmlReferenceString() {
		return "<Accountant uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<Accountant ");
		sb.append("classUuid=\"914890@_mec6wGCfEeG6xvYqJACneg\" ");
		sb.append("className=\"structuredbusiness.Accountant\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		sb.append(">");
		if ( getPerson_iBusinessRole_1_representedPerson()==null ) {
			sb.append("\n<person_iBusinessRole_1_representedPerson/>");
		} else {
			sb.append("\n<person_iBusinessRole_1_representedPerson propertyId=\"742593574795479974\">");
			sb.append("\n" + getPerson_iBusinessRole_1_representedPerson().toXmlString());
			sb.append("\n</person_iBusinessRole_1_representedPerson>");
		}
		sb.append("\n</Accountant>");
		return sb.toString();
	}
	
	public void z_internalAddToDishwashersInc(DishwashersInc val) {
		this.dishwashersInc=val;
	}
	
	public void z_internalAddToParticipation(Participation val) {
		this.participation.add(val);
	}
	
	public void z_internalAddToPerson_iBusinessRole_1_representedPerson(Person_iBusinessRole_1 val) {
		this.person_iBusinessRole_1_representedPerson=val;
	}
	
	public void z_internalAddToRepresentedPerson(PersonNode representedPerson) {
		Person_iBusinessRole_1 newOne = new Person_iBusinessRole_1(this,representedPerson);
		this.z_internalAddToPerson_iBusinessRole_1_representedPerson(newOne);
		newOne.getRepresentedPerson().z_internalAddToPerson_iBusinessRole_1_businessRole(newOne);
	}
	
	public void z_internalRemoveFromDishwashersInc(DishwashersInc val) {
		if ( getDishwashersInc()!=null && val!=null && val.equals(getDishwashersInc()) ) {
			this.dishwashersInc=null;
			this.dishwashersInc=null;
		}
	}
	
	public void z_internalRemoveFromParticipation(Participation val) {
		this.participation.remove(val);
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
	
	/** Implements ->collect( i_Participation : Participation | i_Participation.oclAsType(ParticipationInTask) )
	 */
	private Collection<ParticipationInTask> collect11() {
		Collection<ParticipationInTask> result = new ArrayList<ParticipationInTask>();
		for ( Participation i_Participation : select10() ) {
			ParticipationInTask bodyExpResult = ((ParticipationInTask) i_Participation);
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements ->collect( i_ParticipationInRequest : ParticipationInRequest | i_ParticipationInRequest.request )
	 */
	private Collection<AbstractRequest> collect2() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>();
		for ( ParticipationInRequest i_ParticipationInRequest : select1() ) {
			AbstractRequest bodyExpResult = i_ParticipationInRequest.getRequest();
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements ->collect( i_ParticipationInRequest : ParticipationInRequest | i_ParticipationInRequest.request )
	 */
	private Collection<AbstractRequest> collect4() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>();
		for ( ParticipationInRequest i_ParticipationInRequest : select3() ) {
			AbstractRequest bodyExpResult = i_ParticipationInRequest.getRequest();
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements ->collect( i_Participation : Participation | i_Participation.oclAsType(ParticipationInRequest) )
	 */
	private Collection<ParticipationInRequest> collect6() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>();
		for ( Participation i_Participation : select5() ) {
			ParticipationInRequest bodyExpResult = ((ParticipationInRequest) i_Participation);
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements ->collect( i_ParticipationInTask : ParticipationInTask | i_ParticipationInTask.taskRequest )
	 */
	private Collection<TaskRequest> collect7() {
		Collection<TaskRequest> result = new ArrayList<TaskRequest>();
		for ( ParticipationInTask i_ParticipationInTask : this.getParticipationsInTasks() ) {
			TaskRequest bodyExpResult = i_ParticipationInTask.getTaskRequest();
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements ->collect( i_ParticipationInRequest : ParticipationInRequest | i_ParticipationInRequest.request )
	 */
	private Collection<AbstractRequest> collect9() {
		Collection<AbstractRequest> result = new ArrayList<AbstractRequest>();
		for ( ParticipationInRequest i_ParticipationInRequest : select8() ) {
			AbstractRequest bodyExpResult = i_ParticipationInRequest.getRequest();
			if ( bodyExpResult != null ) result.add( bodyExpResult );
		}
		return result;
	}
	
	/** Implements ->select( i_ParticipationInRequest : ParticipationInRequest | i_ParticipationInRequest.kind = RequestParticipationKind::stakeholder )
	 */
	private Collection<ParticipationInRequest> select1() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>();
		for ( ParticipationInRequest i_ParticipationInRequest : this.getParticipationsInRequests() ) {
			if ( (i_ParticipationInRequest.getKind().equals( RequestParticipationKind.STAKEHOLDER)) ) {
				result.add( i_ParticipationInRequest );
			}
		}
		return result;
	}
	
	/** Implements ->select( i_Participation : Participation | i_Participation.oclIsKindOf(ParticipationInTask) )
	 */
	private Set<Participation> select10() {
		Set<Participation> result = new HashSet<Participation>();
		for ( Participation i_Participation : this.getParticipation() ) {
			if ( (i_Participation instanceof ParticipationInTask) ) {
				result.add( i_Participation );
			}
		}
		return result;
	}
	
	/** Implements ->select( i_ParticipationInRequest : ParticipationInRequest | i_ParticipationInRequest.kind = RequestParticipationKind::businessOwner )
	 */
	private Collection<ParticipationInRequest> select3() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>();
		for ( ParticipationInRequest i_ParticipationInRequest : this.getParticipationsInRequests() ) {
			if ( (i_ParticipationInRequest.getKind().equals( RequestParticipationKind.BUSINESSOWNER)) ) {
				result.add( i_ParticipationInRequest );
			}
		}
		return result;
	}
	
	/** Implements ->select( i_Participation : Participation | i_Participation.oclIsKindOf(ParticipationInRequest) )
	 */
	private Set<Participation> select5() {
		Set<Participation> result = new HashSet<Participation>();
		for ( Participation i_Participation : this.getParticipation() ) {
			if ( (i_Participation instanceof ParticipationInRequest) ) {
				result.add( i_Participation );
			}
		}
		return result;
	}
	
	/** Implements ->select( i_ParticipationInRequest : ParticipationInRequest | i_ParticipationInRequest.kind = RequestParticipationKind::initiator )
	 */
	private Collection<ParticipationInRequest> select8() {
		Collection<ParticipationInRequest> result = new ArrayList<ParticipationInRequest>();
		for ( ParticipationInRequest i_ParticipationInRequest : this.getParticipationsInRequests() ) {
			if ( (i_ParticipationInRequest.getKind().equals( RequestParticipationKind.INITIATOR)) ) {
				result.add( i_ParticipationInRequest );
			}
		}
		return result;
	}

}