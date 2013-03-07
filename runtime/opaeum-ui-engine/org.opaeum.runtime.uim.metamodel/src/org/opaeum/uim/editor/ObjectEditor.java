package org.opaeum.uim.editor;

import org.opaeum.ecore.EObject;
import org.opaeum.runtime.domain.EcoreDataTypeParser;
import org.opaeum.runtime.environment.Environment;
import org.opaeum.uim.model.ClassUserInteractionModel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface ObjectEditor extends EObject, InstanceEditor {
	public void buildTreeFromXml(Element xml);
	
	public ClassUserInteractionModel getModel();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml);
	
	public void setModel(ClassUserInteractionModel model);
	
	public void setUid(String uid);

}