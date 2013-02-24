package org.opaeum.uim.perspective;

import java.util.Map;

import org.opaeum.ecore.EObject;
import org.w3c.dom.Element;

public interface PropertyNavigationConstraint extends EObject, MultiplicityElementNavigationConstraint, NavigationConstraint {
	public void buildTreeFromXml(Element xml, Map<String, Object> map);
	
	public ClassNavigationConstraint getOwner();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map);
	
	public void setOwner(ClassNavigationConstraint owner);
	
	public void setUid(String uid);

}