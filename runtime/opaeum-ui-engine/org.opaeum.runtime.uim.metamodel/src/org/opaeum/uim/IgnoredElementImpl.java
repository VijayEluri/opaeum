package org.opaeum.uim;

import org.opaeum.ecore.EObjectImpl;
import org.opaeum.org.opaeum.runtime.uim.metamodel.UimInstantiator;
import org.opaeum.runtime.domain.EcoreDataTypeParser;
import org.opaeum.runtime.environment.Environment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IgnoredElementImpl extends EObjectImpl implements IgnoredElement {
	private String umlElementUid;
	private UserInterfaceRoot userInterfaceRoot;


	public void buildTreeFromXml(Element xml) {
		if ( xml.getAttribute("umlElementUid").length()>0 ) {
			setUmlElementUid(EcoreDataTypeParser.getInstance().parseEString(xml.getAttribute("umlElementUid")));
		}
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
		
		}
	}
	
	public String getUmlElementUid() {
		return this.umlElementUid;
	}
	
	public UserInterfaceRoot getUserInterfaceRoot() {
		return this.userInterfaceRoot;
	}
	
	public void populateReferencesFromXml(Element xml) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
		
		}
	}
	
	public void setUmlElementUid(String umlElementUid) {
		this.umlElementUid=umlElementUid;
	}
	
	public void setUserInterfaceRoot(UserInterfaceRoot userInterfaceRoot) {
		this.userInterfaceRoot=userInterfaceRoot;
	}

}