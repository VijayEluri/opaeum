package org.opaeum.metamodel.core.internal.emulated;

import java.util.Collection;
import java.util.Collections;

import nl.klasse.octopus.model.IClassifier;

import org.eclipse.uml2.uml.INakedAction;
import org.eclipse.uml2.uml.INakedCallAction;
import org.eclipse.uml2.uml.INakedClassifier;
import org.eclipse.uml2.uml.INakedConnectorEnd;
import org.eclipse.uml2.uml.INakedExpansionNode;
import org.eclipse.uml2.uml.INakedMultiplicity;
import org.eclipse.uml2.uml.INakedMultiplicityElement;
import org.eclipse.uml2.uml.INakedObjectNode;
import org.eclipse.uml2.uml.INakedOutputPin;
import org.eclipse.uml2.uml.INakedPin;
import org.eclipse.uml2.uml.INakedProperty;
import org.eclipse.uml2.uml.INakedTypedElement;
import org.opaeum.metamodel.bpm.INakedEmbeddedTask;

/**
 * This class is need to emulate attibutes in namespaces where other typed elements should also function appear as attributes to Octopus
 * 
 * @author abarnard
 * 
 */
public class TypedElementPropertyBridge extends AbstractEmulatedProperty implements INakedProperty{
	/**
	 * 
	 */
	private static final long serialVersionUID = 211415204864858873L;
	protected INakedTypedElement parameter;
	boolean ensureLocallyUniqueName = true;
	public TypedElementPropertyBridge(INakedClassifier owner,INakedTypedElement parameter){
		super(owner, parameter);
		this.parameter = parameter;
		ensureLocallyUniqueName=false;
	}
	@Override
	public boolean isDerived(){
		if(ensureLocallyUniqueName && parameter instanceof INakedOutputPin){
			INakedAction action = ((INakedOutputPin) parameter).getAction();
			if(action instanceof INakedCallAction && ((INakedCallAction) action).getCalledElement().isLongRunning()){
				return true;
			}else {
				return action instanceof INakedEmbeddedTask;
			}
		}
		return super.isDerived();
	}
	public TypedElementPropertyBridge(INakedClassifier owner,INakedObjectNode pin,boolean ensureLocallyUniqueName){
		super(owner, pin);
		this.parameter = pin;
		this.ensureLocallyUniqueName = ensureLocallyUniqueName;
		
	}
	public boolean shouldEnsureLocallyUniqueName(){
		return this.ensureLocallyUniqueName;
	}
	public INakedClassifier getNakedBaseType(){
		return parameter.getNakedBaseType();
	}
	public boolean isOrdered(){
		return parameter.isOrdered();
	}
	public boolean isUnique(){
		return parameter.isUnique();
	}
	public INakedMultiplicityElement getOriginal(){
		return parameter;
	}
	public IClassifier getType(){
		return parameter.getType();
	}
	@Override
	public String getName(){
		if((parameter instanceof INakedPin || parameter instanceof INakedExpansionNode) && ensureLocallyUniqueName){
			return locallyUniqueName((INakedObjectNode) parameter);
		}
		return super.getName();
	}
	public INakedMultiplicity getNakedMultiplicity(){
		return parameter.getNakedMultiplicity();
	}
	@Override
	public Collection<INakedConnectorEnd> getConnectorEnd(){
		return Collections.emptySet();
	}
	@Override
	public boolean equals(Object other){
		if(other instanceof TypedElementPropertyBridge){
			TypedElementPropertyBridge o = (TypedElementPropertyBridge) other;
			return o == this || (o.getId().equals(getId()) && o.shouldEnsureLocallyUniqueName() == shouldEnsureLocallyUniqueName());
		}else{
			return false;
		}
	}
	public static String locallyUniqueName(INakedObjectNode pin){
		return pin.getName() + "On" + pin.getOwnerElement().getMappingInfo().getJavaName().getCapped();
	}
	@Override
	public boolean isMeasure(){
		return this.parameter.isMeasure();
	}
	@Override
	public boolean isDimension(){
		return this.parameter.isDimension();
	}
}
