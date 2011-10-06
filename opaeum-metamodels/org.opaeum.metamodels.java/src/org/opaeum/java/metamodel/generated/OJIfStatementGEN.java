/*
 * File generated by Grasland Grammar Generator on Dec 23, 2006 7:26:03 PM
 */
package org.opeum.java.metamodel.generated;

import java.util.ArrayList;
import java.util.List;

import org.opeum.java.metamodel.OJBlock;
import org.opeum.java.metamodel.OJElement;
import org.opeum.java.metamodel.OJIfStatement;
import org.opeum.java.metamodel.OJStatement;
import org.opeum.java.metamodel.utilities.InvariantError;


/** Class ...
 */
abstract public class OJIfStatementGEN extends OJStatement {
	private String f_condition = "";
	private OJBlock f_thenPart = null;
	private OJBlock f_elsePart = null;
	static protected boolean usesAllInstances = false;
	static protected List<OJIfStatement> allInstances = new ArrayList<OJIfStatement>();

	/** Constructor for OJIfStatementGEN
	 * 
	 * @param name 
	 * @param comment 
	 * @param condition 
	 */
	protected OJIfStatementGEN(String name, String comment, String condition) {
		super();
		super.setName(name);
		super.setComment(comment);
		this.setCondition(condition);
		if ( usesAllInstances ) {
			allInstances.add(((OJIfStatement)this));
		}
	}
	
	/** Default constructor for OJIfStatement
	 */
	protected OJIfStatementGEN() {
		super();
		if ( usesAllInstances ) {
			allInstances.add(((OJIfStatement)this));
		}
	}

	/** Implements the getter for feature '+ condition : String'
	 */
	public String getCondition() {
		return f_condition;
	}
	
	/** Implements the setter for feature '+ condition : String'
	 * 
	 * @param element 
	 */
	public void setCondition(String element) {
		if ( f_condition != element ) {
			f_condition = element;
		}
	}
	
	/** Implements the getter for feature '+ thenPart : OJBlock'
	 */
	public OJBlock getThenPart() {
		return f_thenPart;
	}
	
	/** Implements the setter for feature '+ thenPart : OJBlock'
	 * 
	 * @param element 
	 */
	public void setThenPart(OJBlock element) {
		if ( f_thenPart != element ) {
			f_thenPart = element;
		}
	}
	
	/** Implements the getter for feature '+ elsePart : OJBlock'
	 */
	public OJBlock getElsePart() {
		return f_elsePart;
	}
	
	/** Implements the setter for feature '+ elsePart : OJBlock'
	 * 
	 * @param element 
	 */
	public void setElsePart(OJBlock element) {
		if ( f_elsePart != element ) {
			f_elsePart = element;
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
		if ( getThenPart() == null ) {
			String message = "Mandatory feature 'thenPart' in object '";
			message = message + this.getIdString();
			message = message + "' of type '" + this.getClass().getName() + "' has no value.";
			result.add(new InvariantError(((OJIfStatement)this), message));
		}
		return result;
	}
	
	/** Default toString implementation for OJIfStatement
	 */
	public String toString() {
		String result = "";
		result = super.toString();
		if ( this.getCondition() != null ) {
			result = result + " condition:" + this.getCondition();
		}
		return result;
	}
	
	/** Returns the default identifier for OJIfStatement
	 */
	public String getIdString() {
		String result = "";
		if ( this.getCondition() != null ) {
			result = result + this.getCondition();
		}
		return result;
	}
	
	/** Implements the OCL allInstances operation
	 */
	static public List allInstances() {
		if ( !usesAllInstances ) {
			throw new RuntimeException("allInstances is not implemented for ((OJIfStatement)this) class. Set usesAllInstances to true, if you want allInstances() implemented.");
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
		OJIfStatement result = new OJIfStatement();
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
	public void copyInfoInto(OJIfStatement copy) {
		super.copyInfoInto(copy);
		copy.setCondition(getCondition());
		if ( getThenPart() != null ) {
			copy.setThenPart(getThenPart());
		}
		if ( getElsePart() != null ) {
			copy.setElsePart(getElsePart());
		}
	}

}