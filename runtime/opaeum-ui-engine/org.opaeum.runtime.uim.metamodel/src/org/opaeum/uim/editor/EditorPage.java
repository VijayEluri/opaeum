package org.opaeum.uim.editor;

import java.util.Map;

import org.opaeum.ecore.EObject;
import org.opaeum.uim.Page;
import org.w3c.dom.Element;

public interface EditorPage extends EObject, Page {
	public void buildTreeFromXml(Element xml, Map<String, Object> map);
	
	public AbstractEditor getEditor();
	
	public String getUid();
	
	public void populateReferencesFromXml(Element xml, Map<String, Object> map);
	
	public void setEditor(AbstractEditor editor);
	
	public void setUid(String uid);

}