package org.opaeum.uim.model;

import java.util.ArrayList;
import java.util.List;

import org.opaeum.ecore.EObjectImpl;
import org.opaeum.org.opaeum.runtime.uim.metamodel.UimInstantiator;
import org.opaeum.runtime.domain.EcoreDataTypeParser;
import org.opaeum.runtime.environment.Environment;
import org.opaeum.uim.cube.CubeQueryEditor;
import org.opaeum.uim.editor.ObjectEditor;
import org.opaeum.uim.wizard.NewObjectWizard;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ClassUserInteractionModelImpl extends EObjectImpl implements ClassUserInteractionModel {
	private CubeQueryEditor cubeQueryEditor;
	private String linkedUmlResource;
	private String name;
	private NewObjectWizard newObjectWizard;
	private ObjectEditor primaryEditor;
	private List<ObjectEditor> secondaryEditors = new ArrayList<ObjectEditor>();
	private String umlElementUid;
	private boolean underUserControl;


	public void buildTreeFromXml(Element xml) {
		if ( xml.getAttribute("umlElementUid").length()>0 ) {
			setUmlElementUid(EcoreDataTypeParser.getInstance().parseEString(xml.getAttribute("umlElementUid")));
		}
		if ( xml.getAttribute("name").length()>0 ) {
			setName(EcoreDataTypeParser.getInstance().parseEString(xml.getAttribute("name")));
		}
		if ( xml.getAttribute("underUserControl").length()>0 ) {
			setUnderUserControl(EcoreDataTypeParser.getInstance().parseEBoolean(xml.getAttribute("underUserControl")));
		}
		if ( xml.getAttribute("linkedUmlResource").length()>0 ) {
			setLinkedUmlResource(EcoreDataTypeParser.getInstance().parseEString(xml.getAttribute("linkedUmlResource")));
		}
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("primaryEditor") ) {
				String typeString = ((Element)currentPropertyNode).getAttribute("xsi:type");
				ObjectEditor curVal;
				if ( typeString==null ||typeString.trim().length()==0 ) {
					typeString="editor:ObjectEditor";
				}
				curVal=UimInstantiator.INSTANCE.newInstance(typeString);
				this.setPrimaryEditor(curVal);
				curVal.init(this,eResource(),(Element)currentPropertyNode);
				curVal.buildTreeFromXml((Element)currentPropertyNode);
				curVal.setModel(this);
			}
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("secondaryEditors") ) {
				String typeString = ((Element)currentPropertyNode).getAttribute("xsi:type");
				ObjectEditor curVal;
				if ( typeString==null ||typeString.trim().length()==0 ) {
					typeString="editor:ObjectEditor";
				}
				curVal=UimInstantiator.INSTANCE.newInstance(typeString);
				this.getSecondaryEditors().add(curVal);
				curVal.init(this,eResource(),(Element)currentPropertyNode);
				curVal.buildTreeFromXml((Element)currentPropertyNode);
			}
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("newObjectWizard") ) {
				String typeString = ((Element)currentPropertyNode).getAttribute("xsi:type");
				NewObjectWizard curVal;
				if ( typeString==null ||typeString.trim().length()==0 ) {
					typeString="wizard:NewObjectWizard";
				}
				curVal=UimInstantiator.INSTANCE.newInstance(typeString);
				this.setNewObjectWizard(curVal);
				curVal.init(this,eResource(),(Element)currentPropertyNode);
				curVal.buildTreeFromXml((Element)currentPropertyNode);
				curVal.setModel(this);
			}
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("cubeQueryEditor") ) {
				String typeString = ((Element)currentPropertyNode).getAttribute("xsi:type");
				CubeQueryEditor curVal;
				if ( typeString==null ||typeString.trim().length()==0 ) {
					typeString="cube:CubeQueryEditor";
				}
				curVal=UimInstantiator.INSTANCE.newInstance(typeString);
				this.setCubeQueryEditor(curVal);
				curVal.init(this,eResource(),(Element)currentPropertyNode);
				curVal.buildTreeFromXml((Element)currentPropertyNode);
			}
		}
	}
	
	public CubeQueryEditor getCubeQueryEditor() {
		return this.cubeQueryEditor;
	}
	
	public String getLinkedUmlResource() {
		return this.linkedUmlResource;
	}
	
	public String getName() {
		return this.name;
	}
	
	public NewObjectWizard getNewObjectWizard() {
		return this.newObjectWizard;
	}
	
	public ObjectEditor getPrimaryEditor() {
		return this.primaryEditor;
	}
	
	public List<ObjectEditor> getSecondaryEditors() {
		return this.secondaryEditors;
	}
	
	public String getUmlElementUid() {
		return this.umlElementUid;
	}
	
	public boolean isUnderUserControl() {
		return this.underUserControl;
	}
	
	public void populateReferencesFromXml(Element xml) {
		NodeList propertyNodes = xml.getChildNodes();
		int i = 0;
		while ( i<propertyNodes.getLength() ) {
			Node currentPropertyNode = propertyNodes.item(i++);
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("primaryEditor") ) {
				((org.opaeum.uim.editor.ObjectEditor)this.eResource().getElement((Element)currentPropertyNode)).populateReferencesFromXml((Element)currentPropertyNode);
			}
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("secondaryEditors") ) {
				((org.opaeum.uim.editor.ObjectEditor)this.eResource().getElement((Element)currentPropertyNode)).populateReferencesFromXml((Element)currentPropertyNode);
			}
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("newObjectWizard") ) {
				((org.opaeum.uim.wizard.NewObjectWizard)this.eResource().getElement((Element)currentPropertyNode)).populateReferencesFromXml((Element)currentPropertyNode);
			}
			if ( currentPropertyNode instanceof Element && currentPropertyNode.getNodeName().equals("cubeQueryEditor") ) {
				((org.opaeum.uim.cube.CubeQueryEditor)this.eResource().getElement((Element)currentPropertyNode)).populateReferencesFromXml((Element)currentPropertyNode);
			}
		}
	}
	
	public void setCubeQueryEditor(CubeQueryEditor cubeQueryEditor) {
		this.cubeQueryEditor=cubeQueryEditor;
	}
	
	public void setLinkedUmlResource(String linkedUmlResource) {
		this.linkedUmlResource=linkedUmlResource;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setNewObjectWizard(NewObjectWizard newObjectWizard) {
		this.newObjectWizard=newObjectWizard;
	}
	
	public void setPrimaryEditor(ObjectEditor primaryEditor) {
		this.primaryEditor=primaryEditor;
	}
	
	public void setSecondaryEditors(List<ObjectEditor> secondaryEditors) {
		this.secondaryEditors=secondaryEditors;
	}
	
	public void setUmlElementUid(String umlElementUid) {
		this.umlElementUid=umlElementUid;
	}
	
	public void setUnderUserControl(boolean underUserControl) {
		this.underUserControl=underUserControl;
	}

}