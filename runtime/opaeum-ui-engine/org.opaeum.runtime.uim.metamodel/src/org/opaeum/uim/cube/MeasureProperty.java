package org.opaeum.uim.cube;

import org.opaeum.ecore.EObject;
import org.opaeum.runtime.domain.EcoreDataTypeParser;
import org.opaeum.runtime.environment.Environment;
import org.opaeum.uim.UmlReference;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface MeasureProperty extends EObject, UmlReference {
	public void buildTreeFromXml(Element xml);
	
	public AggregationFormula getAggregationFormula();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml);
	
	public void setAggregationFormula(AggregationFormula aggregationFormula);
	
	public void setUid(String uid);
	
	public String toString();

}