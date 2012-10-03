package org.opaeum.eclipse.uml.filters.core;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.OpaqueExpression;

public class OpaqueExpressionFilter extends AbstractFilter{
	@Override
	public boolean select(Element e){
		return e instanceof OpaqueExpression;
	}
}