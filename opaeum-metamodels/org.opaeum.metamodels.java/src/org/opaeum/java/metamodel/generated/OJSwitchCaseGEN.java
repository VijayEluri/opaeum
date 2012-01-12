/*
 * File generated by Grasland Grammar Generator on Dec 23, 2006 7:26:03 PM
 */
package org.opaeum.java.metamodel.generated;

import java.util.ArrayList;
import java.util.List;

import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJElement;
import org.opaeum.java.metamodel.OJSwitchCase;
import org.opaeum.java.metamodel.utilities.InvariantError;


/** Class ...
 */
abstract public class OJSwitchCaseGEN extends OJElement {
	private String f_label = "";
	private OJBlock f_body = null;
	static protected boolean usesAllInstances = false;
	static protected List<OJSwitchCase> allInstances = new ArrayList<OJSwitchCase>();

	/** Constructor for OJSwitchCaseGEN
	 * 
	 * @param name 
	 * @param comment 
	 * @param label 
	 */
	protected OJSwitchCaseGEN(String name, String comment, String label) {
		super();
		super.setName(name);
		super.setComment(comment);
		this.setLabel(label);
		if ( usesAllInstances ) {
			allInstances.add(((OJSwitchCase)this));
		}
	}
	
	/** Default constructor for OJSwitchCase
	 */
	protected OJSwitchCaseGEN() {
		super();
		if ( usesAllInstances ) {
			allInstances.add(((OJSwitchCase)this));
		}
	}

	/** Implements the getter for feature '+ label : String'
	 */
	public String getLabel() {
		return f_label;
	}
	
	/** Implements the setter for feature '+ label : String'
	 * 
	 * @param element 
	 */
	public void setLabel(String element) {
		if ( f_label != element ) {
			f_label = element;
		}
	}
	
	/** Implements the getter for feature '+ body : OJBlock'
	 */
	public OJBlock getBody() {
		return f_body;
	}
	
	/** Implements the setter for feature '+ body : OJBlock'
	 * 
	 * @param element 
	 */
	public void setBody(OJBlock element) {
		if ( f_body != element ) {
			f_body = element;
		}
	}
	
	/** Checks all invariants of this object and returns a list of messages about broken invariants
	 */
	public List<InvariantError> checkAllInvariants() {
		List<InvariantError> result = new ArrayList<InvariantError>();
		return result;
	}
	
	/** Implements a check on the multiplicities of all attributes and association ends
	 */
	public List<InvariantError> checkMultiplicities() {
		List<InvariantError> result = new ArrayList<InvariantError>();
		if ( getBody() == null ) {
			String message = "Mandatory feature 'body' in object '";
			message = message + this.getIdString();
			message = message + "' of type '" + this.getClass().getName() + "' has no value.";
			result.add(new InvariantError(((OJSwitchCase)this), message));
		}
		return result;
	}
	
	/** Default toString implementation for OJSwitchCase
	 */
	public String toString() {
		String result = "";
		result = super.toString();
		if ( this.getLabel() != null ) {
			result = result + " label:" + this.getLabel();
		}
		return result;
	}
	
	/** Returns the default identifier for OJSwitchCase
	 */
	public String getIdString() {
		String result = "";
		if ( this.getLabel() != null ) {
			result = result + this.getLabel();
		}
		return result;
	}
	
	/** Implements the OCL allInstances operation
	 */
	static public List allInstances() {
		if ( !usesAllInstances ) {
			throw new RuntimeException("allInstances is not implemented for ((OJSwitchCase)this) class. Set usesAllInstances to true, if you want allInstances() implemented.");
		}
		return allInstances;
	}
	
	/** Returns a copy of this instance. True parts, i.e. associations marked
			'aggregate' or 'composite', and attributes, are copied as well. References to
			other objects, i.e. associations not marked 'aggregate' or 'composite', will not
			be copied. The returned copy will refer to the same objects as the original (this)
			instance.
	 */
	public OJElement getCopy() {
		OJSwitchCase result = new OJSwitchCase();
		this.copyInfoInto(result);
		return result;
	}
	
	/** Copies all attributes and associations of this instance into 'copy'.
			True parts, i.e. associations marked 'aggregate' or 'composite', and attributes, 
			are copied as well. References to other objects, i.e. associations not marked 
			'aggregate' or 'composite', will not be copied. The 'copy' will refer 
			to the same objects as the original (this) instance.
	 * 
	 * @param copy 
	 */
	public void copyInfoInto(OJSwitchCase copy) {
		super.copyInfoInto(copy);
		copy.setLabel(getLabel());
		if ( getBody() != null ) {
			copy.setBody(getBody());
		}
	}

}