package org.opaeum.uim.component;

import java.util.List;

import org.opaeum.ecore.EObject;
import org.opaeum.runtime.domain.EcoreDataTypeParser;
import org.opaeum.runtime.environment.Environment;
import org.opaeum.uim.constraint.EditableConstrainedObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface UimContainer extends EObject, UimComponent, EditableConstrainedObject {
	public void buildTreeFromXml(Element xml);
	
	public List<UimComponent> getChildren();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml);
	
	public void setChildren(List<UimComponent> children);
	
	public void setUid(String uid);

}