package org.opeum.javageneration;

import java.util.Collection;

import nl.klasse.octopus.codegen.umlToJava.maps.ClassifierMap;
import nl.klasse.octopus.expressions.internal.types.PathName;

import org.opeum.java.metamodel.OJElement;
import org.opeum.java.metamodel.OJPathName;
import org.opeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opeum.java.metamodel.annotation.OJAnnotatedElement;
import org.opeum.java.metamodel.annotation.OJAnnotationAttributeValue;
import org.opeum.java.metamodel.annotation.OJAnnotationValue;
import org.opeum.java.metamodel.annotation.OJEnumValue;
import org.opeum.javageneration.maps.NakedClassifierMap;
import org.opeum.javageneration.maps.NakedStructuralFeatureMap;
import org.opeum.metamodel.core.INakedClassifier;
import org.opeum.metamodel.core.INakedElement;
import org.opeum.metamodel.core.INakedEnumeration;
import org.opeum.metamodel.core.INakedEnumerationLiteral;
import org.opeum.metamodel.core.INakedInstanceSpecification;
import org.opeum.metamodel.core.INakedPrimitiveType;
import org.opeum.metamodel.core.INakedSlot;
import org.opeum.metamodel.core.INakedValueSpecification;

public class StereotypeAnnotator extends AbstractJavaProducingVisitor{
	protected void annotate(INakedElement umlElement,OJAnnotatedClass ojClass,String stereotypeName,OJElement javaElement){
		if(umlElement.hasStereotype(stereotypeName)){
			INakedInstanceSpecification is = umlElement.getStereotype(stereotypeName);
			if(javaElement instanceof OJAnnotatedElement){
				applyStereotypeAsAnnotation((OJAnnotatedElement) javaElement, is);
			}
		}
	}
	protected void applyStereotypesAsAnnotations(INakedElement umlElement,OJAnnotatedElement javaElement){
		applyStereotypesAsAnnotations(javaElement, umlElement.getStereotypes());
	}
	protected void applyStereotypesAsAnnotations(OJAnnotatedElement javaElement,Collection<? extends INakedInstanceSpecification> stereotypes){
		for(INakedInstanceSpecification is:stereotypes){
			if(isBuiltIn(is.getClassifier())){
				applyStereotypeAsAnnotation(javaElement, is);
			}
		}
	}
	private void applyStereotypeAsAnnotation(OJAnnotatedElement javaElement,INakedInstanceSpecification stereotypeApplication){
		OJAnnotationValue an = buildAnnotation(stereotypeApplication);
		putAnnotation(javaElement, an);
	}
	private static OJAnnotationValue buildAnnotation(INakedInstanceSpecification stereotypeApplication){
		ClassifierMap map = new NakedClassifierMap(stereotypeApplication.getClassifier());
		OJAnnotationValue an = new OJAnnotationValue(map.javaTypePath());
		for(INakedSlot slot:stereotypeApplication.getSlots()){
			NakedStructuralFeatureMap sfm = new NakedStructuralFeatureMap(slot.getDefiningFeature());
			OJAnnotationAttributeValue aa = new OJAnnotationAttributeValue(sfm.umlName());
			an.putAttribute(aa);
			if(slot.getValues().isEmpty() && sfm.isOne()){
				// TODO rethink this
				// 1. initialValue caused duplicate elements ????
				// 2. THere might not be a slot for the feature
				if(slot.getDefiningFeature().getInitialValue() != null){
					addValue(stereotypeApplication, aa, slot, slot.getDefiningFeature().getInitialValue());
				}else{
					if(sfm.javaType().endsWith("int")){
						aa.addNumberValue(new Integer(0));
					}else if(sfm.javaType().endsWith("float")){
						aa.addNumberValue(new Float(0));
					}else if(sfm.javaType().endsWith("boolean")){
						aa.addBooleanValue(Boolean.FALSE);
					}else{
						aa.addStringValue("");
					}
				}
			}else{
				for(INakedValueSpecification vs:slot.getValues()){
					addValue(stereotypeApplication, aa, slot, vs);
				}
			}
		}
		return an;
	}
	private void putAnnotation(OJAnnotatedElement javaElement,OJAnnotationValue an){
		javaElement.putAnnotation(an);
	}
	private static void addValue(INakedInstanceSpecification is,OJAnnotationAttributeValue aa,INakedSlot slot,INakedValueSpecification vs){
		if(vs.getValue() instanceof Boolean){
			aa.addBooleanValue(vs.booleanValue());
		}else if(vs.getValue() instanceof Number){
			aa.addNumberValue((Number) vs.getValue());
		}else if(vs.getValue() instanceof String){
			aa.addStringValue((String) vs.getValue());
		}else if(vs.getValue() instanceof INakedEnumerationLiteral){
			INakedEnumerationLiteral l = (INakedEnumerationLiteral) vs.getValue();
			INakedEnumeration en = (INakedEnumeration) l.getEnumeration();
			if(slot.getDefiningFeature().getBaseType() instanceof INakedPrimitiveType){
				// String
				aa.addStringValue(l.getName());
			}else{
				ClassifierMap ecm = new NakedClassifierMap(en);
				OJEnumValue ev = new OJEnumValue(ecm.javaTypePath(), l.getName().toUpperCase());
				aa.addEnumValue(ev);
			}
		}else if(vs.getValue() instanceof INakedClassifier){
			OJPathName path = new OJPathName(((INakedClassifier) vs.getValue()).getMappingInfo().getQualifiedJavaName());
			aa.addClassValue(path);
		}else if(vs.getValue() instanceof INakedInstanceSpecification){
			aa.addAnnotationValue(buildAnnotation((INakedInstanceSpecification) vs.getValue()));
		}else if(vs.getValue() instanceof INakedElement){
			aa.addStringValue(((INakedElement) vs.getValue()).getName());
		}else{
			System.out.println("unknow value type:" + vs.getValue());
		}
	}
	private boolean isBuiltIn(INakedClassifier stereotype){
		PathName pn = getPathNameInModel(stereotype);
		boolean result = workspace.getOpeumLibrary().getTypeMap().containsKey(pn.toString());
		return result;
	}
}