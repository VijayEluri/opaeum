package org.nakeduml.runtime.bpm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.nakeduml.annotation.NumlMetaInfo;
import org.nakeduml.runtime.bpm.util.OpiumLibraryForBPMFormatter;
import org.nakeduml.runtime.domain.CompositionNode;
import org.nakeduml.runtime.domain.HibernateEntity;
import org.nakeduml.runtime.domain.IPersistentObject;
import org.w3c.dom.Element;

@NumlMetaInfo(qualifiedPersistentName="opium_library_for_bpm.request_object",uuid="252060@_Wd2QoI53EeCfQedkc0TCdA")
public interface RequestObject extends HibernateEntity, CompositionNode, Serializable, IPersistentObject {
	public void buildTreeFromXml(Element xml, Map<String, Object> map);
	
	@NumlMetaInfo(qualifiedPersistentName="request_object.request_id",uuid="252060@_lEGvYY53EeCfQedkc0TCdA")
	public AbstractRequest getRequest();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map);
	
	public String toXmlReferenceString();
	
	public String toXmlString();

}