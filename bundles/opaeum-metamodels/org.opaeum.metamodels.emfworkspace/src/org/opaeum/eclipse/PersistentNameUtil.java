package org.opaeum.eclipse;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.TypedElement;
import org.opaeum.emf.workspace.EmfWorkspace;
import org.opaeum.metamodel.name.NameWrapper;
import org.opaeum.metamodel.name.SingularNameWrapper;
import org.opaeum.name.NameConverter;

public class PersistentNameUtil{
	public static NameWrapper getPersistentName(Element nme){
		String generatedName = null;
		String existingSqlName=null;
		for(EObject eObject:nme.getStereotypeApplications()){
			EStructuralFeature sf = eObject.eClass().getEStructuralFeature("persistentName");
			if(sf!=null){
				if(sf.getName().equals("persistentName")){
					existingSqlName=(String)eObject.eGet(sf);
				}
			}
		}
		if(existingSqlName != null && existingSqlName.trim().length()>0){
			generatedName = existingSqlName;
		}else if(nme instanceof Association){
			// Name is generated by UmlNameGenerator
			Association ass = (Association) nme;
			generatedName = NameConverter.toUnderscoreStyle(NameConverter.toJavaVariableName(ass.getName()));
		}else if(nme instanceof MultiplicityElement){
			TypedElement tew = (TypedElement) nme;
			String name = tew.getName();
			if(tew instanceof OutputPin){
				name = name + "On" + NameConverter.capitalize( ((NamedElement) tew.getOwner()).getName());// TO ensure uniqueness of name
			}
			// TODO check if this was necessary
			boolean isCmCompatible = true;
			boolean isPersistentInterface = tew.getType() instanceof Interface && !EmfClassifierUtil.isHelper(tew.getType());
			if(tew.getType()!=null&& EmfClassifierUtil.isPersistentComplexStructure(tew.getType()) && !(isCmCompatible && isPersistentInterface)){
				// foreign key
				// TODO re-evaluate the _id thing
				generatedName = NameConverter.toUnderscoreStyle(name) + "_id";
			}else{
				generatedName = NameConverter.toUnderscoreStyle(name);
			}
		}else if(nme instanceof EnumerationLiteral){
			EnumerationLiteral nakedLiteral = ((EnumerationLiteral) nme);
			generatedName = nakedLiteral.getName();
		}else if(nme instanceof NamedElement){
			// TODO actions within StructuredACtivityNodes
			generatedName = NameConverter.toUnderscoreStyle(((NamedElement) nme).getName());
		}
		if(generatedName == null){
			generatedName = nme.getClass().getSimpleName() +EmfWorkspace.getOpaeumId(  nme);
		}
		return new SingularNameWrapper(generatedName.toLowerCase(), null);
	}
}
