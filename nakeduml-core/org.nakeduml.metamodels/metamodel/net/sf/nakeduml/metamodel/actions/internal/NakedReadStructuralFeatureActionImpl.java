package net.sf.nakeduml.metamodel.actions.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.sf.nakeduml.metamodel.actions.INakedReadStructuralFeatureAction;
import net.sf.nakeduml.metamodel.activities.INakedOutputPin;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedProperty;

public class NakedReadStructuralFeatureActionImpl extends NakedStructuralFeatureActionImpl implements INakedReadStructuralFeatureAction{
	private static final long serialVersionUID = 1730941677240408016L;
	public INakedOutputPin result;
	@Override
	public void setFeature(INakedProperty feature){
		super.setFeature(feature);
		linkResultToFeature();
	}
	private void linkResultToFeature(){
		if(this.result != null && this.getFeature() != null){
			this.result.setLinkedTypedElement(this.feature);
		}
	}
	public INakedOutputPin getResult(){
		return this.result;
	}
	public Collection<INakedOutputPin> getOutput(){
		if(getResult() != null){
			return Arrays.asList(this.getResult());
		}else{
			return Collections.emptySet();
		}
	}
	public void setResult(INakedOutputPin result){
		if(this.result != result){
			removeOwnedElement(this.result, true);
			this.result = result;
			linkResultToFeature();
		}
	}
	@Override
	public Collection<INakedElement> getOwnedElements(){
		Collection<INakedElement> retValue = super.getOwnedElements();
		if(this.result != null){
			retValue.add(this.result);
		}
		return retValue;
	}
}