package org.opaeum.uim.userinteractionproperties.filters;

import org.opaeum.uim.UserInteractionElement;
import org.opaeum.uim.action.OperationButton;

public class OperationActionFilter extends AbstractFilter{
	@Override
	public boolean select(UserInteractionElement e){
		return e instanceof OperationButton;
	}
}
