package org.opaeum.uim.cube;

import org.opaeum.ecore.EObject;
import org.opaeum.uim.Labels;

public class LevelPropertyImpl implements LevelProperty {
	private Labels labelOverride;
	private String name;
	private String umlElementUid;
	private boolean underUserControl;


	public EObject eContainer() {
		EObject result = null;
		
		return result;
	}
	
	public Labels getLabelOverride() {
		return this.labelOverride;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getUmlElementUid() {
		return this.umlElementUid;
	}
	
	public boolean getUnderUserControl() {
		return this.underUserControl;
	}
	
	public void isUnderUserControl(boolean underUserControl) {
		this.underUserControl=underUserControl;
	}
	
	public void setLabelOverride(Labels labelOverride) {
		this.labelOverride=labelOverride;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setUmlElementUid(String umlElementUid) {
		this.umlElementUid=umlElementUid;
	}

}