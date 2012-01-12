package org.opaeum.structuretests.test2.util;

import org.opaeum.runtime.domain.AbstractFormatter;

public class StructuretestsFormatter extends AbstractFormatter implements IStructuretestsFormatter {
	static final private ThreadLocal<StructuretestsFormatter> INSTANCE = new ThreadLocal<StructuretestsFormatter>();


	static public StructuretestsFormatter getInstance() {
		StructuretestsFormatter result = INSTANCE.get();
		if ( result==null ) {
			INSTANCE.set(result=new StructuretestsFormatter());
		}
		return result;
	}

}