package org.opaeum.uim.panel;

import java.util.Map;

import org.opaeum.ecore.EObject;
import org.w3c.dom.Element;

public interface GridPanel extends EObject, CollapsiblePanel {
	public void buildTreeFromXml(Element xml, Map<String, Object> map);
	
	public Integer getNumberOfColumns();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map);
	
	public void setNumberOfColumns(Integer numberOfColumns);
	
	public void setUid(String uid);

}