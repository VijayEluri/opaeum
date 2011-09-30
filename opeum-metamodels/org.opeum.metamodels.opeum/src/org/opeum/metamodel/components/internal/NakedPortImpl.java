package org.opeum.metamodel.components.internal;

import org.opeum.metamodel.bpm.INakedBusinessRole;
import org.opeum.metamodel.bpm.INakedBusinessService;
import org.opeum.metamodel.components.INakedPort;
import org.opeum.metamodel.core.internal.NakedPropertyImpl;

public class NakedPortImpl extends NakedPropertyImpl implements INakedPort{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1593895201835920222L;

	@Override
	public boolean isBusinessService(){
		if(getNakedBaseType() instanceof INakedBusinessService){
			return true;
		}else if(getNakedBaseType() instanceof INakedBusinessRole){
			return true;
		}
		return false;
	}
}
