package org.opaeum.uim.cube;

import java.util.List;
import java.util.Map;

import org.opaeum.ecore.EObject;
import org.w3c.dom.Element;

public interface AxisEntry extends EObject {
	public void buildTreeFromXml(Element xml, Map<String, Object> map);
	
	public DimensionBinding getDimensionBinding();
	
	public List<LevelProperty> getLevelProperty();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map);
	
	public void setDimensionBinding(DimensionBinding dimensionBinding);
	
	public void setLevelProperty(List<LevelProperty> levelProperty);
	
	public void setUid(String uid);

}