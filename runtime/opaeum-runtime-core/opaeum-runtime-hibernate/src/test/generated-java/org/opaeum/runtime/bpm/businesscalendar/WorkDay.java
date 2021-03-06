package org.opaeum.runtime.bpm.businesscalendar;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
import org.opaeum.runtime.bpm.util.Stdlib;
import org.opaeum.runtime.domain.CancelledEvent;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.runtime.domain.HibernateEntity;
import org.opaeum.runtime.domain.IEventGenerator;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.domain.IntrospectionUtil;
import org.opaeum.runtime.domain.OutgoingEvent;
import org.opaeum.runtime.persistence.AbstractPersistence;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@NumlMetaInfo(applicationIdentifier="opaeum_hibernate_tests",uuid="252060@_Jn9QcNb-EeCJ0dmaHEVVnw")
@Filter(name="noDeletedObjects")
@org.hibernate.annotations.Entity(dynamicUpdate=true)
@AccessType(	"field")
@Table(name="work_day",schema="bpm",uniqueConstraints={
	@UniqueConstraint(columnNames={"business_calendar_id","kind","deleted_on"}),
	@UniqueConstraint(columnNames={"start_time_id","deleted_on"})})
@NamedQueries(value=
	@NamedQuery(name="QueryWorkDayWithKindForBusinessCalendar",query="from WorkDay a where a.businessCalendar = :businessCalendar and a.kind = :kind"))
@Entity(name="WorkDay")
public class WorkDay implements IPersistentObject, IEventGenerator, HibernateEntity, CompositionNode, Serializable {
	@Index(columnNames="business_calendar_id",name="idx_work_day_business_calendar_id")
	@ManyToOne(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="business_calendar_id",nullable=true)
	protected BusinessCalendar businessCalendar;
	@Transient
	private Set<CancelledEvent> cancelledEvents = new HashSet<CancelledEvent>();
		// Initialise to 1000 from 1970
	@Temporal(	javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="deleted_on")
	private Date deletedOn = Stdlib.FUTURE;
	@OneToOne(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY,mappedBy="workDay")
	protected TimeOfDay endTime;
	@TableGenerator(allocationSize=20,name="id_generator",pkColumnName="type",pkColumnValue="work_day",table="hi_value")
	@Id
	@GeneratedValue(generator="id_generator",strategy=javax.persistence.GenerationType.TABLE)
	private Long id;
	@Enumerated(	javax.persistence.EnumType.STRING)
	@Column(name="kind",nullable=true)
	protected WorkDayKind kind;
	static private Set<? extends WorkDay> mockedAllInstances;
	@Version
	@Column(name="object_version")
	private int objectVersion;
	@Transient
	private Set<OutgoingEvent> outgoingEvents = new HashSet<OutgoingEvent>();
	@Transient
	private InternalHibernatePersistence persistence;
	static final private long serialVersionUID = 5790311637175134369l;
	@ManyToOne(cascade=javax.persistence.CascadeType.ALL,fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="start_time_id",nullable=true)
	protected TimeOfDay startTime;
	private String uid;
	@Column(name="key_in_wor_day_on_bus_cal")
	private String z_keyOfWorkDayOnBusinessCalendar;

	/** This constructor is intended for easy initialization in unit tests
	 * 
	 * @param owningObject 
	 * @param kind 
	 */
	public WorkDay(BusinessCalendar owningObject, WorkDayKind kind) {
		setKind(kind);
		init(owningObject);
		addToOwningObject();
	}
	
	/** Default constructor for WorkDay
	 */
	public WorkDay() {
	}

	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
		if ( getKind()==null ) {
			throw new IllegalStateException("The qualifying property 'kind' must be set before adding a value to 'businessCalendar'");
		}
		getBusinessCalendar().z_internalAddToWorkDay(this.getKind(),(WorkDay)this);
	}
	
	static public Set<? extends WorkDay> allInstances(AbstractPersistence persistence) {
		if ( mockedAllInstances==null ) {
			return new HashSet(persistence.readAll(org.opaeum.runtime.bpm.businesscalendar.WorkDay.class));
		} else {
			return mockedAllInstances;
		}
	}
	
	public void buildTreeFromXml(Element xml, Map<String, Object> map) {
		setUid(xml.getAttribute("uid"));
		if ( xml.getAttribute("kind").length()>0 ) {
			setKind(WorkDayKind.valueOf(xml.getAttribute("kind")));
		}
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("startTime") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("3879474800558390783")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						TimeOfDay curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=org.opaeum.runtime.bpm.util.OpaeumLibraryForBPMJavaMetaInfoMap.INSTANCE.newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.z_internalAddToStartTime(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("endTime") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("1916778051938121831")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						TimeOfDay curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=org.opaeum.runtime.bpm.util.OpaeumLibraryForBPMJavaMetaInfoMap.INSTANCE.newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.z_internalAddToEndTime(curVal);
						curVal.z_internalAddToWorkDay(this);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
		}
	}
	
	public void copyShallowState(WorkDay from, WorkDay to) {
		if ( from.getStartTime()!=null ) {
			to.z_internalAddToStartTime(from.getStartTime().makeShallowCopy());
		}
		if ( from.getEndTime()!=null ) {
			to.z_internalAddToEndTime(from.getEndTime().makeShallowCopy());
		}
		to.setKind(from.getKind());
	}
	
	public void copyState(WorkDay from, WorkDay to) {
		if ( from.getStartTime()!=null ) {
			to.z_internalAddToStartTime(from.getStartTime().makeCopy());
		}
		if ( from.getEndTime()!=null ) {
			to.z_internalAddToEndTime(from.getEndTime().makeCopy());
		}
		to.setKind(from.getKind());
	}
	
	public void createComponents() {
		if ( getEndTime()==null ) {
			setEndTime(new TimeOfDay());
		}
		if ( getStartTime()==null ) {
			setStartTime(new TimeOfDay());
		}
	}
	
	public TimeOfDay createEndTime() {
		TimeOfDay newInstance= new TimeOfDay();
		return newInstance;
	}
	
	public TimeOfDay createStartTime() {
		TimeOfDay newInstance= new TimeOfDay();
		return newInstance;
	}
	
	public boolean equals(Object other) {
		if ( other instanceof WorkDay ) {
			return other==this || ((WorkDay)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=1754968245033077063l,opposite="workDay",uuid="252060@_LAOD4db-EeCJ0dmaHEVVnw")
	@NumlMetaInfo(uuid="252060@_LAOD4db-EeCJ0dmaHEVVnw")
	public BusinessCalendar getBusinessCalendar() {
		BusinessCalendar result = this.businessCalendar;
		
		return result;
	}
	
	public Set<CancelledEvent> getCancelledEvents() {
		return this.cancelledEvents;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=true,opaeumId=1916778051938121831l,opposite="workDay",uuid="252060@_5xvo4NcBEeCJ0dmaHEVVnw")
	@NumlMetaInfo(uuid="252060@_5xvo4NcBEeCJ0dmaHEVVnw")
	public TimeOfDay getEndTime() {
		TimeOfDay result = this.endTime;
		
		return result;
	}
	
	public Long getId() {
		return this.id;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=378853448039264835l,opposite="workDay",uuid="252060@_LrAGRNb-EeCJ0dmaHEVVnw")
	@NumlMetaInfo(uuid="252060@_LrAGRNb-EeCJ0dmaHEVVnw")
	public WorkDayKind getKind() {
		WorkDayKind result = this.kind;
		
		return result;
	}
	
	@PropertyMetaInfo(constraints={},isComposite=false,opaeumId=4204288269954036939l,uuid="252060@_vEgCENcMEeCnccVVb6bGDQ")
	@NumlMetaInfo(uuid="252060@_vEgCENcMEeCnccVVb6bGDQ")
	public Integer getMinutesPerDay() {
		Integer result = (this.getEndTime().getMinuteOfDay() - this.getStartTime().getMinuteOfDay());
		
		return result;
	}
	
	public String getName() {
		return "WorkDay["+getId()+"]";
	}
	
	public int getObjectVersion() {
		return this.objectVersion;
	}
	
	public Set<OutgoingEvent> getOutgoingEvents() {
		return this.outgoingEvents;
	}
	
	public CompositionNode getOwningObject() {
		return getBusinessCalendar();
	}
	
	@PropertyMetaInfo(constraints={},isComposite=true,opaeumId=3879474800558390783l,opposite="workDay",uuid="252060@_xyUUMNcBEeCJ0dmaHEVVnw")
	@NumlMetaInfo(uuid="252060@_xyUUMNcBEeCJ0dmaHEVVnw")
	public TimeOfDay getStartTime() {
		TimeOfDay result = this.startTime;
		
		return result;
	}
	
	public String getUid() {
		if ( this.uid==null || this.uid.trim().length()==0 ) {
			uid=UUID.randomUUID().toString();
		}
		return this.uid;
	}
	
	public String getZ_keyOfWorkDayOnBusinessCalendar() {
		return this.z_keyOfWorkDayOnBusinessCalendar;
	}
	
	public int hashCode() {
		return getUid().hashCode();
	}
	
	public void init(CompositionNode owner) {
		this.z_internalAddToBusinessCalendar((BusinessCalendar)owner);
	}
	
	public WorkDay makeCopy() {
		WorkDay result = new WorkDay();
		copyState((WorkDay)this,result);
		return result;
	}
	
	public WorkDay makeShallowCopy() {
		WorkDay result = new WorkDay();
		copyShallowState((WorkDay)this,result);
		result.setId(this.getId());
		return result;
	}
	
	public void markDeleted() {
		setDeletedOn(new Date(System.currentTimeMillis()));
		if ( getBusinessCalendar()!=null ) {
			getBusinessCalendar().z_internalRemoveFromWorkDay(this.getKind(),this);
		}
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
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("startTime") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("3879474800558390783")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((TimeOfDay)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("endTime") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("1916778051938121831")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((TimeOfDay)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
		}
	}
	
	public void removeFromOwningObject() {
		this.markDeleted();
	}
	
	public void setBusinessCalendar(BusinessCalendar businessCalendar) {
		if ( this.getBusinessCalendar()!=null ) {
			this.getBusinessCalendar().z_internalRemoveFromWorkDay(this.getKind(),this);
		}
		if ( businessCalendar == null ) {
			this.z_internalRemoveFromBusinessCalendar(this.getBusinessCalendar());
		} else {
			if ( getKind()==null ) {
				throw new IllegalStateException("The qualifying property 'kind' must be set before adding a value to 'businessCalendar'");
			}
			this.z_internalAddToBusinessCalendar(businessCalendar);
		}
		if ( businessCalendar!=null ) {
			businessCalendar.z_internalAddToWorkDay(this.getKind(),this);
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
	
	public void setEndTime(TimeOfDay endTime) {
		TimeOfDay oldValue = this.getEndTime();
		if ( oldValue==null ) {
			if ( endTime!=null ) {
				WorkDay oldOther = (WorkDay)endTime.getWorkDay();
				endTime.z_internalRemoveFromWorkDay(oldOther);
				if ( oldOther != null ) {
					oldOther.z_internalRemoveFromEndTime(endTime);
				}
				endTime.z_internalAddToWorkDay((WorkDay)this);
			}
			this.z_internalAddToEndTime(endTime);
		} else {
			if ( !oldValue.equals(endTime) ) {
				oldValue.z_internalRemoveFromWorkDay(this);
				z_internalRemoveFromEndTime(oldValue);
				if ( endTime!=null ) {
					WorkDay oldOther = (WorkDay)endTime.getWorkDay();
					endTime.z_internalRemoveFromWorkDay(oldOther);
					if ( oldOther != null ) {
						oldOther.z_internalRemoveFromEndTime(endTime);
					}
					endTime.z_internalAddToWorkDay((WorkDay)this);
				}
				this.z_internalAddToEndTime(endTime);
			}
		}
	}
	
	public void setId(Long id) {
		this.id=id;
	}
	
	public void setKind(WorkDayKind kind) {
		if ( getBusinessCalendar()!=null && getKind()!=null ) {
			getBusinessCalendar().z_internalRemoveFromWorkDay(this.getKind(),this);
		}
		if ( kind == null ) {
			this.z_internalRemoveFromKind(getKind());
		} else {
			this.z_internalAddToKind(kind);
		}
		if ( getBusinessCalendar()!=null && getKind()!=null ) {
			getBusinessCalendar().z_internalAddToWorkDay(this.getKind(),this);
		}
	}
	
	public void setObjectVersion(int objectVersion) {
		this.objectVersion=objectVersion;
	}
	
	public void setOutgoingEvents(Set<OutgoingEvent> outgoingEvents) {
		this.outgoingEvents=outgoingEvents;
	}
	
	public void setStartTime(TimeOfDay startTime) {
		if ( startTime == null ) {
			this.z_internalRemoveFromStartTime(getStartTime());
		} else {
			this.z_internalAddToStartTime(startTime);
		}
	}
	
	public void setUid(String newUid) {
		this.uid=newUid;
	}
	
	public void setZ_keyOfWorkDayOnBusinessCalendar(String z_keyOfWorkDayOnBusinessCalendar) {
		this.z_keyOfWorkDayOnBusinessCalendar=z_keyOfWorkDayOnBusinessCalendar;
	}
	
	public String toXmlReferenceString() {
		return "<WorkDay uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<WorkDay ");
		sb.append("classUuid=\"252060@_Jn9QcNb-EeCJ0dmaHEVVnw\" ");
		sb.append("className=\"org.opaeum.runtime.bpm.businesscalendar.WorkDay\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		if ( getKind()!=null ) {
			sb.append("kind=\""+ getKind().name() + "\" ");
		}
		sb.append(">");
		if ( getStartTime()==null ) {
			sb.append("\n<startTime/>");
		} else {
			sb.append("\n<startTime propertyId=\"3879474800558390783\">");
			sb.append("\n" + getStartTime().toXmlString());
			sb.append("\n</startTime>");
		}
		if ( getEndTime()==null ) {
			sb.append("\n<endTime/>");
		} else {
			sb.append("\n<endTime propertyId=\"1916778051938121831\">");
			sb.append("\n" + getEndTime().toXmlString());
			sb.append("\n</endTime>");
		}
		sb.append("\n</WorkDay>");
		return sb.toString();
	}
	
	public void z_internalAddToBusinessCalendar(BusinessCalendar businessCalendar) {
		if ( businessCalendar.equals(this.businessCalendar) ) {
			return;
		}
		this.businessCalendar=businessCalendar;
	}
	
	public void z_internalAddToEndTime(TimeOfDay endTime) {
		if ( endTime.equals(this.endTime) ) {
			return;
		}
		this.endTime=endTime;
	}
	
	public void z_internalAddToKind(WorkDayKind kind) {
		if ( kind.equals(this.kind) ) {
			return;
		}
		this.kind=kind;
	}
	
	public void z_internalAddToStartTime(TimeOfDay startTime) {
		if ( startTime.equals(this.startTime) ) {
			return;
		}
		this.startTime=startTime;
	}
	
	public void z_internalRemoveFromBusinessCalendar(BusinessCalendar businessCalendar) {
		if ( getBusinessCalendar()!=null && businessCalendar!=null && businessCalendar.equals(getBusinessCalendar()) ) {
			this.businessCalendar=null;
			this.businessCalendar=null;
		}
	}
	
	public void z_internalRemoveFromEndTime(TimeOfDay endTime) {
		if ( getEndTime()!=null && endTime!=null && endTime.equals(getEndTime()) ) {
			this.endTime=null;
			this.endTime=null;
		}
	}
	
	public void z_internalRemoveFromKind(WorkDayKind kind) {
		if ( getKind()!=null && kind!=null && kind.equals(getKind()) ) {
			this.kind=null;
			this.kind=null;
		}
	}
	
	public void z_internalRemoveFromStartTime(TimeOfDay startTime) {
		if ( getStartTime()!=null && startTime!=null && startTime.equals(getStartTime()) ) {
			this.startTime=null;
			this.startTime=null;
		}
	}

}