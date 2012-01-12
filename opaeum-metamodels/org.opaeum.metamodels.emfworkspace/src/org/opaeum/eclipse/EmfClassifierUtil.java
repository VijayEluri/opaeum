package org.opaeum.eclipse;

import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Type;

public class EmfClassifierUtil{
	public static boolean conformsTo(Classifier from,Classifier to){
		if(from.equals(to)){
			return true;
		}else if(from.allParents().contains(to)){
			return true;
		}else if(from instanceof BehavioredClassifier){
			for(InterfaceRealization i:((BehavioredClassifier) from).getInterfaceRealizations()){
				if(i.getContract().equals(to)|| i.getContract().allParents().contains(to)){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean comformsToLibraryType(Type type,String string){
		if(type.getName().equalsIgnoreCase(string)){
			return true;
		}else if(type instanceof Classifier){
			for(Classifier g:((Classifier) type).getGenerals()){
				if(comformsToLibraryType(g, string)){
					return true;
				}
			}
		}
		return false;
	}
}