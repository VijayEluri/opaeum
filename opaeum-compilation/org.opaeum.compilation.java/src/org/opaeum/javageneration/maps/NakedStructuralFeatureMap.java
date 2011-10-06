package org.opeum.javageneration.maps;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nl.klasse.octopus.codegen.umlToJava.maps.StructuralFeatureMap;
import nl.klasse.tools.common.StringHelpers;

import org.opeum.java.metamodel.OJPathName;
import org.opeum.metamodel.core.INakedProperty;


public class NakedStructuralFeatureMap extends StructuralFeatureMap{
	private static Set<String> reservedWords=new HashSet<String>(Arrays.asList("return","class","interface", "void", "int", "short","boolean","long","double", "float", "synchronized") );

	
	@Override
	public String umlName(){
		return super.umlName();
	}
	@Override
	public String fieldname(){
		if(reservedWords.contains(feature.getName())){
			return "_" + umlName();
		}else{
			return umlName();
		}
	}
	public NakedStructuralFeatureMap(INakedProperty feature){
		super(feature);
		baseTypeMap = new NakedClassifierMap( feature.getNakedBaseType());
		featureTypeMap = new NakedClassifierMap(feature.getType());
	}
	@Override
	public OJPathName javaTypePath(){
		if(isMany()){
			OJPathName copy = super.javaTypePath().getCopy();
			copy.addToElementTypes(javaBaseTypePath());
			return copy;
		}else if(isJavaPrimitive()){
			return featureTypeMap.javaObjectTypePath();
		}else{
			return super.javaTypePath();
		}
	}
	
	@Override
	public OJPathName javaDefaultTypePath(){
		if(isMany()){
			OJPathName baseType = super.javaBaseDefaultTypePath();
			OJPathName copy = super.javaDefaultTypePath().getCopy();
			copy.addToElementTypes(baseType);
			return copy;
		}else{
			return super.javaDefaultTypePath();
		}
	}
	
	
	@Override
	public OJPathName javaBaseTypePath(){
		if(baseTypeMap.isJavaPrimitive()){
			return baseTypeMap.javaObjectTypePath();
		}else{
			return baseTypeMap.javaTypePath();
		}
	}
	public String allAdder(){
		return "addAllTo" + StringHelpers.firstCharToUpper(feature.getName());
	}
	public String clearer(){
		return "clear" + StringHelpers.firstCharToUpper(feature.getName());
	}
	public boolean isMany(){
		if(feature instanceof INakedProperty){
			INakedProperty property = (INakedProperty) feature;
			int qualifierCount = property.getQualifiers().size();
			return property.getNakedMultiplicity().isMany() || qualifierCount > 0;
		}else{
			return feature.getMultiplicity().getUpper() > 1;
		}
	}
	public boolean isOne(){
		return !isMany();
	}
	public boolean isOneToMany(){
		return otherEndIsOne() && isMany();
	}
	public boolean isManyToMany(){
		return !otherEndIsOne() && isMany();
	}
	public boolean isManyToOne(){
		return !otherEndIsOne() && isOne();
	}
	public boolean isOneToOne(){
		return otherEndIsOne() && isOne();
	}
	/**
	 * IF the other end is not navigable or there is no other end, an assumption of otherEnd=Many is made
	 * 
	 * @return
	 */
	protected boolean otherEndIsOne(){
		if(getProperty().getOtherEnd() != null){
			INakedProperty otherEnd = getProperty().getOtherEnd();
			return otherEnd.isNavigable() && otherEnd.getNakedMultiplicity().isOne() && otherEnd.getQualifiers().size() == 0;
		}else{
			return false;
		}
	}
	public INakedProperty getProperty(){
		return((INakedProperty) feature);
	}
}