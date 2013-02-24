package org.opaeum.uim.binding;

import java.util.Map;

import org.opaeum.ecore.EObject;
import org.opaeum.uim.UmlReference;
import org.w3c.dom.Element;

public interface UimBinding extends EObject, UmlReference {
	public void buildTreeFromXml(Element xml, Map<String, Object> map);
	
	public String getLastPropertyUuid();
	
	public PropertyRef getNext();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map);
	
	public void setNext(PropertyRef next);
	
	public void setUid(String uid);

}