/*
 * File generated by Grasland Grammar Generator on Dec 23, 2006 7:43:22 PM
 */
package org.opaeum.java.metamodel;

import java.util.Set;

import org.opaeum.java.metamodel.generated.OJForStatementGEN;
import org.opaeum.java.metamodel.utilities.JavaStringHelpers;

public class OJForStatement extends OJForStatementGEN{
	/**
	 * Constructor for OJForStatement
	 * 
	 * @param name
	 * @param comment
	 * @param elemName
	 * @param collection
	 */
	public OJForStatement(String name,String comment,String elemName,String collection){
		super(name, comment, elemName, collection);
		setBody(new OJBlock());
	}
	/**
	 * Constructor for OJForStatement
	 */
	public OJForStatement(){
		super();
		setBody(new OJBlock());
	}
	public OJForStatement(String elementName,OJPathName type,String collection){
		this(null, null, elementName, collection);
		setElemType(type);
		setBody(new OJBlock());
	}
	public String toJavaString(){
		String result = "for ( " + getElemType().getCollectionTypeName() + " " + getElemName() + " : " + getCollection() + " ) {\n";
		result = result + JavaStringHelpers.indent(getBody().toJavaString(), 1) + "\n}";
		return result;
	}
	public OJForStatement getDeepCopy(){
		OJForStatement copy = new OJForStatement();
		copyDeepInfoInto(copy);
		return copy;
	}
	public void copyDeepInfoInto(OJForStatement copy){
		super.copyDeepInfoInto(copy);
		copy.setElemName(getElemName());
		copy.setCollection(getCollection());
		if(getElemType() != null){
			copy.setElemType(getElemType().getDeepCopy());
		}
		if(getBody() != null){
			copy.setBody(getBody().getDeepCopy());
		}
	}
	public void renameAll(Set<OJPathName> renamePathNames,String suffix){
		if(getElemType() != null){
			getElemType().renameAll(renamePathNames, suffix);
		}
		setCollection(replaceAll(getCollection(), renamePathNames, suffix));
		if(getBody() != null){
			getBody().renameAll(renamePathNames, suffix);
		}
	}
}