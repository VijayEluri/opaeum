package net.sf.nakeduml.metamodel.actions.internal;

import java.util.Collection;

import net.sf.nakeduml.metamodel.actions.INakedAcceptCallAction;
import net.sf.nakeduml.metamodel.activities.INakedOutputPin;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedOperation;

public class NakedAcceptCallActionImpl extends NakedAcceptEventActionImpl implements INakedAcceptCallAction{
	@Override
	public Collection<INakedElement> getOwnedElements(){
		// TODO Auto-generated method stub
		return super.getOwnedElements();
	}
	@Override
	public Collection<INakedOutputPin> getOutput(){
		Collection<INakedOutputPin> result = super.getOutput();
		result.add(getReturnInfo());
		return result;
	}
	INakedOutputPin returnInfo;
	@Override
	public INakedOutputPin getReturnInfo(){
		return returnInfo;
	}
	public void setReturnInfo(INakedOutputPin r){
		this.returnInfo = r;
	}
	@Override
	public INakedOperation getOperation(){
		return (INakedOperation) super.getTrigger().getEvent();
	}
}
