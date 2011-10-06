package org.opeum.metamodel.components.internal;

import org.opeum.metamodel.components.INakedConnectorEnd;
import org.opeum.metamodel.core.INakedProperty;
import org.opeum.metamodel.core.internal.NakedMultiplicityElement;

public class NakedConnectorEndImpl extends NakedMultiplicityElement implements INakedConnectorEnd{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6692848840616896126L;
	private INakedProperty role;
	private INakedProperty partWitPort;
	@Override
	public INakedProperty getPartWithPort(){
		return this.partWitPort;
	}
	public INakedProperty getPartWitPort(){
		return partWitPort;
	}
	public void setPartWitPort(INakedProperty partWitPort){
		this.partWitPort = partWitPort;
	}
	@Override
	public INakedProperty getRole(){
		return this.role;
	}
	@Override
	public String getMetaClass(){
		return "connectorEnd";
	}
	public void setRole(INakedProperty role){
		this.role = role;
	}
}