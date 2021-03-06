package org.opaeum.runtime.bpm.request;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Index;
import org.opaeum.annotation.NumlMetaInfo;
import org.opaeum.annotation.PropertyMetaInfo;
import org.opaeum.hibernate.domain.InternalHibernatePersistence;
import org.opaeum.hibernate.domain.UiidBasedInterfaceValue;
import org.opaeum.runtime.bpm.organization.IParticipant;
import org.opaeum.runtime.bpm.util.Stdlib;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.runtime.domain.HibernateEntity;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.domain.InterfaceValueOwner;
import org.opaeum.runtime.persistence.AbstractPersistence;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@NumlMetaInfo(applicationIdentifier="opaeum_hibernate_tests",uuid="252060@_3YyGkIoXEeCPduia_-NbFw")
@Filter(name="noDeletedObjects")
@org.hibernate.annotations.Entity(dynamicUpdate=true)
@AccessType(	"field")
@Table(name="participation_participant",schema="bpm",uniqueConstraints=
	@UniqueConstraint(columnNames={"participation_id","deleted_on"}))
@Entity(name="ParticipationParticipant")
public class ParticipationParticipant implements InterfaceValueOwner, IPersistentObject, HibernateEntity, CompositionNode, Serializable {
	static private Map<String, Class> INTERFACE_FIELDS = new HashMap<String,Class>();
	static{
	INTERFACE_FIELDS.put("participant",IParticipant.class);
	}
	
		// Initialise to 1000 from 1970
	@Temporal(	javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="deleted_on")
	private Date deletedOn = Stdlib.FUTURE;
	@TableGenerator(allocationSize=20,name="id_generator",pkColumnName="type",pkColumnValue="participation_participant",table="hi_value")
	@Id
	@GeneratedValue(generator="id_generator",strategy=javax.persistence.GenerationType.TABLE)
	private Long id;
	static private Set<? extends ParticipationParticipant> mockedAllInstances;
	@Version
	@Column(name="object_version")
	private int objectVersion;
	@Embedded
	@AttributeOverrides(	{
		@AttributeOverride(column=
			@Column(name="participant"),name="identifier"),
		@AttributeOverride(column=
			@Column(name="participant_type"),name="classIdentifier")})
	protected UiidBasedInterfaceValue participant;
	@Index(columnNames="participation_id",name="idx_participation_participant_participation_id")
	@ManyToOne(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="participation_id",nullable=true)
	protected Participation participation;
	@Transient
	private InternalHibernatePersistence persistence;
	static final private long serialVersionUID = 6189386099840902425l;
	private String uid;

	/** Constructor for ParticipationParticipant
	 * 
	 * @param end2 
	 * @param end1 
	 */
	public ParticipationParticipant(IParticipant end2, Participation end1) {
		this.z_internalAddToParticipation(end1);
		this.z_internalAddToParticipant(end2);
	}
	
	/** Constructor for ParticipationParticipant
	 * 
	 * @param end1 
	 * @param end2 
	 */
	public ParticipationParticipant(Participation end1, IParticipant end2) {
		this.z_internalAddToParticipation(end1);
		this.z_internalAddToParticipant(end2);
	}
	
	/** Default constructor for ParticipationParticipant
	 */
	public ParticipationParticipant() {
	}

	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
	}
	
	static public Set<? extends ParticipationParticipant> allInstances(AbstractPersistence persistence) {
		if ( mockedAllInstances==null ) {
			return new HashSet(persistence.readAll(org.opaeum.runtime.bpm.request.ParticipationParticipant.class));
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
		
		}
	}
	
	public void clear() {
		this.z_internalRemoveFromParticipant(getParticipant());
		this.z_internalRemoveFromParticipation(getParticipation());
		markDeleted();
	}
	
	public void createComponents() {
	}
	
	public boolean equals(Object other) {
		if ( other instanceof ParticipationParticipant ) {
			return other==this || ((ParticipationParticipant)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	public Class getFieldType(String fieldName) {
		Class result = INTERFACE_FIELDS.get(fieldName);
		
		return result;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getName() {
		return "ParticipationParticipant["+getId()+"]";
	}
	
	public int getObjectVersion() {
		return this.objectVersion;
	}
	
	public CompositionNode getOwningObject() {
		return getParticipant();
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=1067114618754283926l,opposite="participationParticipant_participation",uuid="252060@_3YyGlIoXEeCPduia_-NbFw")
	@NumlMetaInfo(uuid="252060@_3YyGkIoXEeCPduia_-NbFw@252060@_3YyGlIoXEeCPduia_-NbFw")
	public IParticipant getParticipant() {
		IParticipant result = null;
		if ( this.participant==null ) {
			this.participant=new UiidBasedInterfaceValue();
		}
		result=(IParticipant)this.participant.getValue(persistence);
		return result;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=3746223061979168312l,opposite="participationParticipant_participant",uuid="252060@_3YyGkYoXEeCPduia_-NbFw")
	@NumlMetaInfo(uuid="252060@_3YyGkIoXEeCPduia_-NbFw@252060@_3YyGkYoXEeCPduia_-NbFw")
	public Participation getParticipation() {
		Participation result = this.participation;
		
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
		this.z_internalAddToParticipant((IParticipant)owner);
	}
	
	public void markDeleted() {
		setDeletedOn(new Date(System.currentTimeMillis()));
		setDeletedOn(new Date());
	}
	
	static public void mockAllInstances(Set newMocks) {
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
		return "<ParticipationParticipant uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<ParticipationParticipant ");
		sb.append("classUuid=\"252060@_3YyGkIoXEeCPduia_-NbFw\" ");
		sb.append("className=\"org.opaeum.runtime.bpm.request.ParticipationParticipant\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		sb.append(">");
		sb.append("\n</ParticipationParticipant>");
		return sb.toString();
	}
	
	public void z_internalAddToParticipant(IParticipant participant) {
		if ( participant.equals(getParticipant()) ) {
			return;
		}
		if ( this.participant==null ) {
			this.participant=new UiidBasedInterfaceValue();
		}
		this.participant.setValue(participant);
	}
	
	public void z_internalAddToParticipation(Participation participation) {
		if ( participation.equals(getParticipation()) ) {
			return;
		}
		this.participation=participation;
	}
	
	public void z_internalRemoveFromParticipant(IParticipant participant) {
		if ( getParticipant()!=null && participant!=null && participant.equals(getParticipant()) ) {
			this.participant.setValue(null);
		}
	}
	
	public void z_internalRemoveFromParticipation(Participation participation) {
		if ( getParticipation()!=null && participation!=null && participation.equals(getParticipation()) ) {
			this.participation=null;
			this.participation=null;
		}
	}

}