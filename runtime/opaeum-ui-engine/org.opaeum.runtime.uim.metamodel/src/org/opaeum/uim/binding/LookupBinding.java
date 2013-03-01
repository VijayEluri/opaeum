package org.opaeum.uim.binding;

import org.opaeum.ecore.EObject;
import org.opaeum.runtime.domain.EcoreDataTypeParser;
import org.opaeum.runtime.environment.Environment;
import org.opaeum.uim.control.UimLookup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface LookupBinding extends EObject, UimBinding {
	public void buildTreeFromXml(Element xml);
	
	public UimLookup getLookup();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml);
	
	public void setLookup(UimLookup lookup);
	
	public void setUid(String uid);

}