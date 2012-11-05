package org.opaeum.java.metamodel;

import java.util.List;

import org.opaeum.java.metamodel.generated.OJConstructorGEN;
import org.opaeum.java.metamodel.utilities.JavaStringHelpers;
import org.opaeum.java.metamodel.utilities.JavaUtil;

public class OJConstructor extends OJConstructorGEN{
	private boolean isDefault;
	/******************************************************
	 * The constructor for this classifier.
	 *******************************************************/
	public OJConstructor(){
		super();
	}
	public OJConstructor(boolean b){
		this.isDefault=b;
	}
	public OJClassifier getOwner(){
		return this.getOwningClass();
	}
	@Override
	public void addToParameters(OJParameter element){
		if(isDefault){
			throw new IllegalStateException("Default parameter may not have parameters");
		}
		super.addToParameters(element);
	}
	public OJConstructor getDeepCopy(){
		OJConstructor result = new OJConstructor();
		copyValuesDeep(result);
		return result;
	}
	public void setBody(OJBlock element){
		if(getBody() != null && getBody().getStatements().size()>0){
			List<OJStatement> statements = getBody().getStatements();
			for(OJStatement ojStatement:statements){
				if(ojStatement.toJavaString().contains("setJira(")){
					System.out.println();
				}
			}
		}
		super.setBody(element);
	}
	public String toJavaString(){
		StringBuilder result = new StringBuilder();
		if(getComment().equals("")){
			setComment("constructor for " + getOwner().getName());
		}
		addJavaDocComment(result);
		result.append(visToJava(this) + " " + getOwner().getName());
		// params
		result.append("(" + paramsToJava(this) + ") {\n");
		// body
		StringBuilder bodyStr = new StringBuilder();
		bodyStr.append(JavaUtil.collectionToJavaString(getBody().getStatements(), "\n"));
		result.append(JavaStringHelpers.indent(bodyStr, 1));
		if(result.charAt(result.length() - 1) == '\n'){
			result.deleteCharAt(result.length() - 1);
		}
		// closing bracket
		result.append("\n}\n");
		return result.toString();
	}
	public OJConstructor getConstructorCopy(){
		OJConstructor result = new OJConstructor();
		super.copyValues(result);
		return result;
	}
	public OJConstructor getDeepConstructorCopy(){
		OJConstructor result = new OJConstructor();
		super.copyValuesDeep(result);
		return result;
	}
}