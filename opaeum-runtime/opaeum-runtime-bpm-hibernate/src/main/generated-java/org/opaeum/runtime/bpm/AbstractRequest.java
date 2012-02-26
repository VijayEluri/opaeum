package org.opaeum.runtime.bpm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessContext;
import org.hibernate.Session;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.jbpm.workflow.core.NodeContainer;
import org.jbpm.workflow.core.WorkflowProcess;
import org.jbpm.workflow.core.impl.NodeImpl;
import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
import org.jbpm.workflow.instance.NodeInstanceContainer;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.opaeum.annotation.NumlMetaInfo;
import org.opaeum.runtime.bpm.util.Stdlib;
import org.opaeum.runtime.domain.CancelledEvent;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.runtime.domain.HibernateEntity;
import org.opaeum.runtime.domain.IEventGenerator;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.domain.IProcessObject;
import org.opaeum.runtime.domain.IProcessStep;
import org.opaeum.runtime.domain.IntrospectionUtil;
import org.opaeum.runtime.domain.OutgoingEvent;
import org.opaeum.runtime.environment.Environment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Proxy(lazy=false)
@org.hibernate.annotations.Entity(dynamicUpdate=true)
@Filter(name="noDeletedObjects")
@Entity(name="AbstractRequest")
@DiscriminatorColumn(name="type_descriminator",discriminatorType=javax.persistence.DiscriminatorType.STRING)
@Inheritance(strategy=javax.persistence.InheritanceType.JOINED)
@Table(schema="opaeum_bpm",name="abstract_request")
@NumlMetaInfo(uuid="252060@_6MA8UI2-EeCrtavWRHwoHg")
@AccessType("field")
abstract public class AbstractRequest implements IEventGenerator, CompositionNode, HibernateEntity, Serializable, IPersistentObject, IProcessObject {
	private String uid;
	@Column(name="executed_on")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date executedOn;
	@Transient
	private Set<CancelledEvent> cancelledEvents = new HashSet<CancelledEvent>();
	@Column(name="object_version")
	@Version
	private int objectVersion;
	protected Object currentException;
	@Column(name="calling_process_instance_id")
	private Long callingProcessInstanceId;
	@Filter(condition="deleted_on > current_timestamp",name="noDeletedObjects")
	@OneToMany(fetch=javax.persistence.FetchType.LAZY,cascade=javax.persistence.CascadeType.ALL,mappedBy="_request",targetEntity=ParticipationInRequest.class)
	@LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private Set<ParticipationInRequest> _participationsInRequest = new HashSet<ParticipationInRequest>();
	@Transient
	private Set<OutgoingEvent> outgoingEvents = new HashSet<OutgoingEvent>();
	@Transient
	transient private WorkflowProcessInstance processInstance;
	private String callingNodeInstanceUniqueId;
	@Transient
	transient private WorkflowProcessInstance callingProcessInstance;
		// Initialise to 1000 from 1970
	@Column(name="deleted_on")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date deletedOn = Stdlib.FUTURE;
	@GeneratedValue(strategy=javax.persistence.GenerationType.AUTO)
	@Id
	private Long id;
	@Transient
	private boolean processDirty;
	@Column(name="process_instance_id")
	private Long processInstanceId;
	@Type(type="org.opaeum.runtime.bpm.AbstractRequestStateResolver")
	private AbstractRequestState endNodeInRegion1;
	static final private long serialVersionUID = 22;
	static private Set<AbstractRequest> mockedAllInstances;
	@Index(name="idx_abstract_request_parent_task_id",columnNames="parent_task_id")
	@ManyToOne(fetch=javax.persistence.FetchType.LAZY)
	@JoinColumn(name="parent_task_id",nullable=true)
	private TaskRequest _parentTask;

	/** Default constructor for AbstractRequest
	 */
	public AbstractRequest() {
	}

	@NumlMetaInfo(uuid="252060@_3USwcKDGEeCv9IRqC7lfYw")
	public void activate() {
		generateActivateEvent();
	}
	
	public void addAllToParticipationsInRequest(Set<ParticipationInRequest> _participationsInRequest) {
		for ( ParticipationInRequest o : _participationsInRequest ) {
			addToParticipationsInRequest(o);
		}
	}
	
	@NumlMetaInfo(uuid="252060@_Qo338I6QEeCrtavWRHwoHg")
	public void addRequestParticipant(Participant _newParticipant, RequestParticipationKind _kind) {
		ParticipationInRequest _participation = null;
		ParticipationInRequest _resultOnCreatePartipation = null;
		_resultOnCreatePartipation=new ParticipationInRequest();
		_participation=_resultOnCreatePartipation;
		AbstractRequest tgtAddParticipation=this;
		tgtAddParticipation.addToParticipationsInRequest(_participation);
		ParticipationInRequest tgtSetNewParticipant=_participation;
		tgtSetNewParticipant.setParticipant(_newParticipant);
		ParticipationInRequest tgtSetKind=_participation;
		tgtSetKind.setKind(_kind);
	}
	
	/** Call this method when you want to attach this object to the containment tree. Useful with transitive persistence
	 */
	public void addToOwningObject() {
	}
	
	public void addToParticipationsInRequest(ParticipationInRequest _participationsInRequest) {
		if ( _participationsInRequest!=null ) {
			_participationsInRequest.z_internalRemoveFromRequest(_participationsInRequest.getRequest());
			_participationsInRequest.z_internalAddToRequest(this);
			z_internalAddToParticipationsInRequest(_participationsInRequest);
		}
	}
	
	static public Set<? extends AbstractRequest> allInstances() {
		if ( mockedAllInstances==null ) {
			Session session =org.opaeum.runtime.environment.Environment.getInstance().getComponent(Session.class);
			return new HashSet(session.createQuery("from AbstractRequest").list());
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
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("_participationsInRequest") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("206")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						ParticipationInRequest curVal;
						try {
							curVal=IntrospectionUtil.newInstance(((Element)currentPropertyValueNode).getAttribute("className"));
						} catch (Exception e) {
							curVal=Environment.getMetaInfoMap().newInstance(((Element)currentPropertyValueNode).getAttribute("classUuid"));
						}
						curVal.buildTreeFromXml((Element)currentPropertyValueNode,map);
						this.addToParticipationsInRequest(curVal);
						map.put(curVal.getUid(), curVal);
					}
				}
			}
		}
	}
	
	public void clearParticipationsInRequest() {
		removeAllFromParticipationsInRequest(getParticipationsInRequest());
	}
	
	@NumlMetaInfo(uuid="252060@_4RNcAIoaEeCPduia_-NbFw")
	public void complete() {
		generateCompleteEvent();
	}
	
	public void completed() {
		AbstractRequestListener callbackListener = getCallingProcessObject();
		getProcessInstance().setState(WorkflowProcessInstance.STATE_COMPLETED);
		if ( callbackListener!=null ) {
			callbackListener.onAbstractRequestComplete(getCallingNodeInstanceUniqueId(),this);
		}
	}
	
	public boolean consumeActivateOccurrence() {
		boolean consumed = false;
		return consumed;
	}
	
	public boolean consumeAddRequestParticipantOccurrence(Participant _newParticipant, RequestParticipationKind _kind) {
		boolean consumed = false;
		return consumed;
	}
	
	public boolean consumeCompleteOccurrence() {
		boolean consumed = false;
		return consumed;
	}
	
	public boolean consumeRemoveRequestParticipantOccurrence(Participant _participant, RequestParticipationKind _kind) {
		boolean consumed = false;
		return consumed;
	}
	
	public boolean consumeResumeOccurrence() {
		boolean consumed = false;
		return consumed;
	}
	
	public boolean consumeStartOccurrence() {
		boolean consumed = false;
		return consumed;
	}
	
	public boolean consumeSuspendOccurrence() {
		boolean consumed = false;
		return consumed;
	}
	
	public boolean equals(Object other) {
		if ( other instanceof AbstractRequest ) {
			return other==this || ((AbstractRequest)other).getUid().equals(this.getUid());
		}
		return false;
	}
	
	public void execute() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		WorkflowProcessInstance processInstance;
		setExecutedOn(new Date());
		params.put("processObject", this);
		processInstance = (WorkflowProcessInstance)org.opaeum.runtime.environment.Environment.getInstance().getComponent(StatefulKnowledgeSession.class).startProcess("opaeum_bpm_abstract_request",params);
		((WorkflowProcessImpl)processInstance.getProcess()).setAutoComplete(true);
		this.processInstance=processInstance;
		this.setProcessInstanceId(processInstance.getId());
	}
	
	public NodeInstanceImpl findNodeInstanceByUniqueId(String uniqueId) {
		for ( NodeInstanceImpl nodeInstance : getNodeInstancesRecursively() ) {
			if ( nodeInstance.getUniqueId().equals(uniqueId) ) {
				return nodeInstance;
			}
		}
		return null;
	}
	
	public NodeInstanceImpl findWaitingNodeByNodeId(long step) {
		for ( NodeInstanceImpl nodeInstance : getNodeInstancesRecursively() ) {
			if ( ((NodeImpl)nodeInstance.getNode()).getId()==step ) {
				return nodeInstance;
			}
		}
		return null;
	}
	
	public void forceToStep(IProcessStep step) {
		AbstractRequestState nextStep = (AbstractRequestState)step;
		NodeImpl newNode = getNodeForStep(nextStep);
		for ( NodeInstanceImpl curNodeInstance : getNodeInstancesRecursively() ) {
			if ( curNodeInstance.getNode().getNodeContainer()==newNode.getNodeContainer() ) {
				transition(curNodeInstance,newNode);
				return;
			}
		}
	}
	
	public void generateActivateEvent() {
	}
	
	public void generateAddRequestParticipantEvent(Participant _newParticipant, RequestParticipationKind _kind) {
	}
	
	public void generateCompleteEvent() {
	}
	
	public void generateRemoveRequestParticipantEvent(Participant _participant, RequestParticipationKind _kind) {
	}
	
	public void generateResumeEvent() {
	}
	
	public void generateStartEvent() {
	}
	
	public void generateSuspendEvent() {
	}
	
	public Set<IProcessStep> getActiveLeafSteps() {
		Set results = new HashSet<IProcessStep>();
		return results;
	}
	
	public String getCallingNodeInstanceUniqueId() {
		return this.callingNodeInstanceUniqueId;
	}
	
	public WorkflowProcess getCallingProcessDefinition() {
		return (WorkflowProcess) getCallingProcessInstance().getProcess();
	}
	
	public WorkflowProcessInstance getCallingProcessInstance() {
		if ( this.callingProcessInstance==null ) {
			this.callingProcessInstance=(WorkflowProcessInstance)org.opaeum.runtime.environment.Environment.getInstance().getComponent(StatefulKnowledgeSession.class).getProcessInstance(getCallingProcessInstanceId());
			if ( this.callingProcessInstance!=null ) {
				((WorkflowProcessImpl)this.callingProcessInstance.getProcess()).setAutoComplete(true);
			}
		}
		return this.callingProcessInstance;
	}
	
	public Long getCallingProcessInstanceId() {
		return this.callingProcessInstanceId;
	}
	
	public AbstractRequestListener getCallingProcessObject() {
		if ( getCallingProcessInstance()!=null  ) {
			AbstractRequestListener processObject = (AbstractRequestListener)getCallingProcessInstance().getVariable("processObject");
			return processObject;
		}
		return null;
	}
	
	public Set<CancelledEvent> getCancelledEvents() {
		return this.cancelledEvents;
	}
	
	public Date getDeletedOn() {
		return this.deletedOn;
	}
	
	public Date getExecutedOn() {
		return this.executedOn;
	}
	
	public Long getId() {
		return this.id;
	}
	
	@NumlMetaInfo(uuid="252060@_VJyB4I6REeCrtavWRHwoHg")
	public Participant getInitiator() {
		Participant result = any1().getParticipant();
		
		return result;
	}
	
	public IProcessStep getInnermostNonParallelStep() {
		if ( this.endNodeInRegion1!=null ) {
			return this.endNodeInRegion1;
		} else {
			NodeInstanceImpl nodeInstance = (NodeInstanceImpl)getProcessInstance().getNodeInstances().iterator().next();
			if ( getProcessInstance().getNodeInstances().size()>1 ) {
				return null;
			}
			while ( nodeInstance instanceof NodeInstanceContainer && ((NodeInstanceContainer)nodeInstance).getNodeInstances().size()==1 ) {
				nodeInstance=(NodeInstanceImpl)((NodeInstanceContainer)nodeInstance).getNodeInstances().iterator().next();
			}
			return AbstractRequestState.resolveById(nodeInstance.getNodeId());
		}
	}
	
	public String getName() {
		return "AbstractRequest["+getId()+"]";
	}
	
	public Collection<NodeInstanceImpl> getNodeInstancesRecursively() {
		return (Collection<NodeInstanceImpl>)(Collection)((NodeInstanceContainer)getProcessInstance()).getNodeInstances(true);
	}
	
	public int getObjectVersion() {
		return this.objectVersion;
	}
	
	public Set<OutgoingEvent> getOutgoingEvents() {
		return this.outgoingEvents;
	}
	
	public CompositionNode getOwningObject() {
		return null;
	}
	
	@NumlMetaInfo(uuid="252060@_towFgY29EeCrtavWRHwoHg")
	public TaskRequest getParentTask() {
		TaskRequest result = this._parentTask;
		
		return result;
	}
	
	@NumlMetaInfo(uuid="252060@_XLHkUI6NEeCrtavWRHwoHg")
	public Set<ParticipationInRequest> getParticipationsInRequest() {
		Set<ParticipationInRequest> result = this._participationsInRequest;
		
		return result;
	}
	
	public WorkflowProcess getProcessDefinition() {
		return (WorkflowProcess) getProcessInstance().getProcess();
	}
	
	public WorkflowProcessInstance getProcessInstance() {
		if ( this.processInstance==null ) {
			this.processInstance=(WorkflowProcessInstance)org.opaeum.runtime.environment.Environment.getInstance().getComponent(StatefulKnowledgeSession.class).getProcessInstance(getProcessInstanceId());
			if ( this.processInstance!=null ) {
				((WorkflowProcessImpl)this.processInstance.getProcess()).setAutoComplete(true);
			}
		}
		return this.processInstance;
	}
	
	public Long getProcessInstanceId() {
		return this.processInstanceId;
	}
	
	@NumlMetaInfo(uuid="252060@_lEGvZI53EeCfQedkc0TCdA")
	public RequestObject getResponsibilityObject() {
		RequestObject result = null;
		
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
	}
	
	public void init(ProcessContext context) {
		this.setProcessInstanceId(context.getProcessInstance().getId());
		((WorkflowProcessImpl)context.getProcessInstance().getProcess()).setAutoComplete(true);
	}
	
	public boolean isComplete() {
		boolean result = endNodeInRegion1!=null ||currentException!=null;
		
		return result;
	}
	
	public boolean isProcessDirty() {
		return this.processDirty;
	}
	
	public boolean isStepActive(IProcessStep step) {
		if ( step==this.endNodeInRegion1 ) {
			return true;
		}
		if ( getProcessInstance()==null ) {
			return false;
		} else {
			for ( NodeInstanceImpl nodeInstance : getNodeInstancesRecursively() ) {
				if ( step.getId()==nodeInstance.getNodeId() ) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void markDeleted() {
		setDeletedOn(new Date(System.currentTimeMillis()));
		if ( getParentTask()!=null ) {
			getParentTask().z_internalRemoveFromSubRequests((AbstractRequest)this);
		}
		for ( ParticipationInRequest child : new ArrayList<ParticipationInRequest>(getParticipationsInRequest()) ) {
			child.markDeleted();
		}
	}
	
	static public void mockAllInstances(Set<AbstractRequest> newMocks) {
		mockedAllInstances=newMocks;
	}
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("_participationsInRequest") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("206")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						((ParticipationInRequest)map.get(((Element)currentPropertyValueNode).getAttribute("uid"))).populateReferencesFromXml((Element)currentPropertyValueNode, map);
					}
				}
			}
			if ( currentPropertyNode instanceof Element && (currentPropertyNode.getNodeName().equals("_parentTask") || ((Element)currentPropertyNode).getAttribute("propertyId").equals("186")) ) {
				NodeList propertyValueNodes = currentPropertyNode.getChildNodes();
				int j = 0;
				while ( j<propertyValueNodes.getLength() ) {
					Node currentPropertyValueNode = propertyValueNodes.item(j++);
					if ( currentPropertyValueNode instanceof Element ) {
						setParentTask((TaskRequest)map.get(((Element)currentPropertyValueNode).getAttribute("uid")));
					}
				}
			}
		}
	}
	
	public void propagateException(Object exception) {
		AbstractRequestListener callbackListener = getCallingProcessObject();
		if ( callbackListener==null ) {
		
		} else {
			callbackListener.onAbstractRequestUnhandledException(getCallingNodeInstanceUniqueId(),exception, this);
		}
	}
	
	public NodeImpl recursivelyFindNode(AbstractRequestState step, Collection<NodeImpl> collection) {
		for ( NodeImpl curNode : collection ) {
			if ( curNode.getId()==step.getId() ) {
				return curNode;
			} else {
				if ( curNode instanceof NodeContainer ) {
					NodeImpl childNode = recursivelyFindNode(step, (Collection)Arrays.asList(((NodeContainer)curNode).getNodes()));
					if ( childNode!=null ) {
						return childNode;
					}
				}
			}
		}
		return null;
	}
	
	public void removeAllFromParticipationsInRequest(Set<ParticipationInRequest> _participationsInRequest) {
		Set<ParticipationInRequest> tmp = new HashSet<ParticipationInRequest>(_participationsInRequest);
		for ( ParticipationInRequest o : tmp ) {
			removeFromParticipationsInRequest(o);
		}
	}
	
	public void removeFromOwningObject() {
		this.markDeleted();
	}
	
	public void removeFromParticipationsInRequest(ParticipationInRequest _participationsInRequest) {
		if ( _participationsInRequest!=null ) {
			_participationsInRequest.z_internalRemoveFromRequest(this);
			z_internalRemoveFromParticipationsInRequest(_participationsInRequest);
		}
	}
	
	@NumlMetaInfo(uuid="252060@_Nl5kQI6SEeCrtavWRHwoHg")
	public void removeRequestParticipant(Participant _participant, RequestParticipationKind _kind) {
		AbstractRequest tgtRemoveParticipation=this;
		tgtRemoveParticipation.removeAllFromParticipationsInRequest((select2(_participant, _kind)));
	}
	
	@NumlMetaInfo(uuid="252060@_qwWfEIoaEeCPduia_-NbFw")
	public void resume() {
		generateResumeEvent();
	}
	
	public void setCallingNodeInstanceUniqueId(String callingNodeInstanceUniqueId) {
		this.callingNodeInstanceUniqueId=callingNodeInstanceUniqueId;
	}
	
	public void setCallingProcessInstance(WorkflowProcessInstance callingProcessInstance) {
		this.callingProcessInstance=callingProcessInstance;
	}
	
	public void setCallingProcessInstanceId(Long callingProcessInstanceId) {
		this.callingProcessInstanceId=callingProcessInstanceId;
	}
	
	public void setCancelledEvents(Set<CancelledEvent> cancelledEvents) {
		this.cancelledEvents=cancelledEvents;
	}
	
	public void setDeletedOn(Date deletedOn) {
		this.deletedOn=deletedOn;
	}
	
	public void setExecutedOn(Date executedOn) {
		this.executedOn=executedOn;
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
	
	public void setParentTask(TaskRequest _parentTask) {
		if ( this.getParentTask()!=null ) {
			this.getParentTask().z_internalRemoveFromSubRequests((AbstractRequest)this);
		}
		if ( _parentTask!=null ) {
			_parentTask.z_internalAddToSubRequests((AbstractRequest)this);
			this.z_internalAddToParentTask(_parentTask);
		}
	}
	
	public void setParticipationsInRequest(Set<ParticipationInRequest> _participationsInRequest) {
		this.clearParticipationsInRequest();
		this.addAllToParticipationsInRequest(_participationsInRequest);
	}
	
	public void setProcessInstance(WorkflowProcessInstance processInstance) {
		this.processInstance=processInstance;
	}
	
	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId=processInstanceId;
	}
	
	public void setReturnInfo(ProcessContext context) {
		this.callingProcessInstanceId=context.getProcessInstance().getId();
		this.callingNodeInstanceUniqueId=((NodeInstanceImpl)context.getNodeInstance()).getUniqueId();
	}
	
	public void setUid(String newUid) {
		this.uid=newUid;
	}
	
	@NumlMetaInfo(uuid="252060@_-PsMoIoaEeCPduia_-NbFw")
	public void start() {
		generateStartEvent();
	}
	
	@NumlMetaInfo(uuid="252060@_ov5DMIoaEeCPduia_-NbFw")
	public void suspend() {
		generateSuspendEvent();
	}
	
	public String toXmlReferenceString() {
		return "<AbstractRequest uid=\""+getUid() + "\"/>";
	}
	
	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<AbstractRequest ");
		sb.append("classUuid=\"252060@_6MA8UI2-EeCrtavWRHwoHg\" ");
		sb.append("className=\"org.opaeum.runtime.bpm.AbstractRequest\" ");
		sb.append("uid=\"" + this.getUid() + "\" ");
		sb.append(">");
		sb.append("\n<_participationsInRequest propertyId=\"206\">");
		for ( ParticipationInRequest _participationsInRequest : getParticipationsInRequest() ) {
			sb.append("\n" + _participationsInRequest.toXmlString());
		}
		sb.append("\n</_participationsInRequest>");
		if ( getParentTask()==null ) {
			sb.append("\n<_parentTask/>");
		} else {
			sb.append("\n<_parentTask propertyId=\"186\">");
			sb.append("\n" + getParentTask().toXmlReferenceString());
			sb.append("\n</_parentTask>");
		}
		sb.append("\n</AbstractRequest>");
		return sb.toString();
	}
	
	public void transition(NodeInstanceImpl curNodeInstance, NodeImpl newNode) {
		NodeInstanceContainer container = (NodeInstanceContainer)curNodeInstance.getNodeInstanceContainer();
		NodeInstanceImpl newNodeInstance = (NodeInstanceImpl)container.getNodeInstance(newNode);
		container.removeNodeInstance(curNodeInstance);
		curNodeInstance.trigger(newNodeInstance, "asdf");
	}
	
	public void z_internalAddToParentTask(TaskRequest val) {
		this._parentTask=val;
	}
	
	public void z_internalAddToParticipationsInRequest(ParticipationInRequest val) {
		this._participationsInRequest.add(val);
	}
	
	public void z_internalRemoveFromParentTask(TaskRequest val) {
		if ( getParentTask()!=null && val!=null && val.equals(getParentTask()) ) {
			this._parentTask=null;
		}
	}
	
	public void z_internalRemoveFromParticipationsInRequest(ParticipationInRequest val) {
		this._participationsInRequest.remove(val);
	}
	
	/** Implements ->any( p : ParticipationInRequest | p.kind = RequestParticipationKind::initiator )
	 */
	private ParticipationInRequest any1() {
		ParticipationInRequest result = null;
		for ( ParticipationInRequest _p : this.getParticipationsInRequest() ) {
			if ( (_p.getKind().equals( RequestParticipationKind.INITIATOR)) ) {
				return _p;
			}
		}
		return result;
	}
	
	private NodeImpl getNodeForStep(AbstractRequestState step) {
		return recursivelyFindNode(step, (Collection)Arrays.asList(getProcessDefinition().getNodes()));
	}
	
	/** Implements ->select( p : ParticipationInRequest | (p.participant = participant) and p.kind = kind )
	 * 
	 * @param _participant 
	 * @param _kind 
	 */
	private Set<ParticipationInRequest> select2(Participant _participant, RequestParticipationKind _kind) {
		Set<ParticipationInRequest> result = new HashSet<ParticipationInRequest>();
		for ( ParticipationInRequest _p : this.getParticipationsInRequest() ) {
			if ( (_p.getParticipant().equals(_participant) && (_p.getKind().equals( _kind))) ) {
				result.add( _p );
			}
		}
		return result;
	}

}