package org.opaeum.uim.binding;

import org.opaeum.ecore.EObjectImpl;
import org.opaeum.org.opaeum.runtime.uim.metamodel.UimInstantiator;
import org.opaeum.runtime.domain.EcoreDataTypeParser;
import org.opaeum.runtime.environment.Environment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertyRefImpl extends EObjectImpl implements PropertyRef {
	private UimBinding binding;
	private PropertyRef next;
	private PropertyRef previous;
	private String umlElementUid;


	public void buildTreeFromXml(Element xml) {
		if ( xml.getAttribute("umlElementUid").length()>0 ) {
			setUmlElementUid(EcoreDataTypeParser.getInstance().parseEString(xml.getAttribute("umlElementUid")));
		}
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("next") ) {
				String typeString = ((Element)currentPropertyNode).getAttribute("xsi:type");
				PropertyRef curVal;
				if ( typeString==null ||typeString.trim().length()==0 ) {
					typeString="bind:PropertyRef";
				}
				curVal=UimInstantiator.INSTANCE.newInstance(typeString);
				this.setNext(curVal);
				curVal.init(this,eResource(),(Element)currentPropertyNode);
				curVal.buildTreeFromXml((Element)currentPropertyNode);
				curVal.setPrevious(this);
			}
		}
	}
	
	public UimBinding getBinding() {
		return this.binding;
	}
	
	public PropertyRef getNext() {
		return this.next;
	}
	
	public PropertyRef getPrevious() {
		return this.previous;
	}
	
	public String getUmlElementUid() {
		return this.umlElementUid;
	}
	
	public void populateReferencesFromXml(Element xml) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("next") ) {
				((org.opaeum.uim.binding.PropertyRef)this.eResource().getElement((Element)currentPropertyNode)).populateReferencesFromXml((Element)currentPropertyNode);
			}
		}
	}
	
	public void setBinding(UimBinding binding) {
		this.binding=binding;
	}
	
	public void setNext(PropertyRef next) {
		this.next=next;
	}
	
	public void setPrevious(PropertyRef previous) {
		this.previous=previous;
	}
	
	public void setUmlElementUid(String umlElementUid) {
		this.umlElementUid=umlElementUid;
	}

}