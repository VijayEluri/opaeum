package org.opaeum.eclipse;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Slot;

public class EmfValueSpecificationUtil{

	public static String getOclBody(EList<String> bodies,EList<String> languages){
		String result = null;
		for(int i = 0;i < languages.size();i++){
			if(languages.get(i).equalsIgnoreCase("ocl")){
				if(bodies.size() > i){
					result = bodies.get(i);
					break;
				}
			}
		}
		if(result == null && bodies.size() == 1){
			result = bodies.get(0);
		}
		return result;
	}

	public static Element getContext(OpaqueExpression valueSpec){
		//TODO may have  to refine a bit
		return (Element) EmfElementFinder.getContainer(valueSpec);
	}

	public static String getOclBody(OpaqueExpression oe){
		return getOclBody(oe.getBodies(),oe.getLanguages());
	}
	public static String getOclBody(OpaqueBehavior oe){
		return getOclBody(oe.getBodies(),oe.getLanguages());
	}
	public static String getOclBody(OpaqueAction oe){
		return getOclBody(oe.getBodies(),oe.getLanguages());
	}

	public static Slot getSlotForFeature(InstanceSpecification is,Property a){
		Slot found = null;
		for(Slot slot:new ArrayList<Slot>(is.getSlots())){
			if(slot.getDefiningFeature() != null && slot.getDefiningFeature().equals(a)){
				found = slot;
			}
		}
		return found;
	}
}