package org.opeum.metamodel.actions.internal;

import org.opeum.metamodel.actions.INakedCallOperationAction;
import org.opeum.metamodel.core.INakedClassifier;
import org.opeum.metamodel.core.INakedMessageStructure;
import org.opeum.metamodel.core.INakedOperation;
import org.opeum.metamodel.core.IParameterOwner;

public class NakedCallOperationActionImpl extends NakedCallActionImpl implements INakedCallOperationAction{
	private static final long serialVersionUID = -236758123440467617L;
	private INakedOperation operation;
	private INakedMessageStructure messageStructure;
	public INakedOperation getOperation(){
		return this.operation;
	}
	public void setOperation(INakedOperation operation){
		this.operation = operation;
	}
	public IParameterOwner getCalledElement(){
		return getOperation();
	}
	@Override
	public INakedMessageStructure getMessageStructure(){
		return messageStructure;
	}
	public void initMessageStructure(){
		if(messageStructure == null && getOperation() != null){
			if(getOperation().getMessageStructure()==null){
				getOperation().initMessageStructure();
			}
			messageStructure = getOperation().getMessageStructure();
		}
	}
	@Override
	public INakedClassifier getExpectedTargetType(){
		if(getOperation() != null){
			return getOperation().getOwner();
		}else{
			return null;
		}
	}
	@Override
	public boolean isLongRunning(){
		return getOperation() != null && getOperation().isLongRunning();
	}
}