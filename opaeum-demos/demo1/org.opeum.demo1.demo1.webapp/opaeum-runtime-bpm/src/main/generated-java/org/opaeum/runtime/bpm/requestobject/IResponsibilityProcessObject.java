package org.opaeum.runtime.bpm.requestobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.opaeum.annotation.NumlMetaInfo;
import org.opaeum.annotation.ParameterMetaInfo;
import org.opaeum.runtime.bpm.util.OpaeumLibraryForBPMFormatter;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.runtime.domain.HibernateEntity;
import org.opaeum.runtime.domain.IPersistentObject;
import org.w3c.dom.Element;

@NumlMetaInfo(uuid="252060@_w0UdIEt8EeGElKTCe2jfDw")
public interface IResponsibilityProcessObject extends HibernateEntity, CompositionNode, IResponsibilityInvocation, Serializable, IPersistentObject, IProcessObject {
	public void buildTreeFromXml(Element xml, Map<String, Object> map);
	
	public boolean consumeOnCompletedOccurrence(@ParameterMetaInfo(name="completedBy",opaeumId=4095523919410592856l,uuid="252060@_yDSYZ0uBEeGElKTCe2jfDw") String completedBy);
	
	public boolean consumeOnFailureOccurrence();
	
	public boolean consumeOnResumedOccurrence(@ParameterMetaInfo(name="resumedBy",opaeumId=787801760248251908l,uuid="252060@_xd0650uBEeGElKTCe2jfDw") String resumedBy);
	
	public boolean consumeOnStartedOccurrence(@ParameterMetaInfo(name="startedBy",opaeumId=8825299842246312l,uuid="252060@_v7N4p0uBEeGElKTCe2jfDw") String startedBy);
	
	public boolean consumeOnSuspendedOccurrence(@ParameterMetaInfo(name="suspendedBy",opaeumId=3377686963449892476l,uuid="252060@_xCSOZ0uBEeGElKTCe2jfDw") String suspendedBy);
	
	public void generateOnCompletedEvent(@ParameterMetaInfo(name="completedBy",opaeumId=4095523919410592856l,uuid="252060@_yDSYZ0uBEeGElKTCe2jfDw") String completedBy);
	
	public void generateOnFailureEvent();
	
	public void generateOnResumedEvent(@ParameterMetaInfo(name="resumedBy",opaeumId=787801760248251908l,uuid="252060@_xd0650uBEeGElKTCe2jfDw") String resumedBy);
	
	public void generateOnStartedEvent(@ParameterMetaInfo(name="startedBy",opaeumId=8825299842246312l,uuid="252060@_v7N4p0uBEeGElKTCe2jfDw") String startedBy);
	
	public void generateOnSuspendedEvent(@ParameterMetaInfo(name="suspendedBy",opaeumId=3377686963449892476l,uuid="252060@_xCSOZ0uBEeGElKTCe2jfDw") String suspendedBy);
	
	public String getUid();
	
	@NumlMetaInfo(uuid="252060@_yDSYYEuBEeGElKTCe2jfDw")
	public void onCompleted(@ParameterMetaInfo(name="completedBy",opaeumId=4095523919410592856l,uuid="252060@_yDSYZ0uBEeGElKTCe2jfDw") String completedBy);
	
	@NumlMetaInfo(uuid="252060@_CDJHUEuCEeGElKTCe2jfDw")
	public void onFailure();
	
	@NumlMetaInfo(uuid="252060@_xd064EuBEeGElKTCe2jfDw")
	public void onResumed(@ParameterMetaInfo(name="resumedBy",opaeumId=787801760248251908l,uuid="252060@_xd0650uBEeGElKTCe2jfDw") String resumedBy);
	
	@NumlMetaInfo(uuid="252060@_v7N4oEuBEeGElKTCe2jfDw")
	public void onStarted(@ParameterMetaInfo(name="startedBy",opaeumId=8825299842246312l,uuid="252060@_v7N4p0uBEeGElKTCe2jfDw") String startedBy);
	
	@NumlMetaInfo(uuid="252060@_xCSOYEuBEeGElKTCe2jfDw")
	public void onSuspended(@ParameterMetaInfo(name="suspendedBy",opaeumId=3377686963449892476l,uuid="252060@_xCSOZ0uBEeGElKTCe2jfDw") String suspendedBy);
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map);
	
	public String toXmlReferenceString();
	
	public String toXmlString();

}