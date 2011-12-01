package net.sf.nakeduml.metamodel.actions.internal;

import net.sf.nakeduml.metamodel.actions.INakedAddStructuralFeatureValueAction;
import net.sf.nakeduml.metamodel.activities.INakedInputPin;

public class NakedAddStructuralFeatureValueActionImpl extends NakedWriteStructuralFeatureActionImpl implements INakedAddStructuralFeatureValueAction{
	private static final long serialVersionUID = -6520786921966729943L;
	private boolean replaceAll;
	private INakedInputPin insertAt;
	public INakedInputPin getInsertAt(){
		return insertAt;
	}
	public void setInsertAt(INakedInputPin insertAt){
		this.insertAt = insertAt;
	}
	public boolean isReplaceAll(){
		return replaceAll;
	}
	public void setReplaceAll(boolean f){
		this.replaceAll = f;
	}
}