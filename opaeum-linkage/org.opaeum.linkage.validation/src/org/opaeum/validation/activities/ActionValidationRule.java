package org.opaeum.validation.activities;

import org.opaeum.metamodel.validation.IValidationRule;

public enum ActionValidationRule implements IValidationRule{
	REQUIRED_PIN("Certain actions require input and output pins","{0} needs to have an {1} specified as its {2}"),
	SEND_SIGNAL_ACTION_REQUIRES_SIGNAL("Send Signal actions must have their Signal specified","No Signal or Notification specified for {0}"),
	CALL_BEHAVIOR_ACTION_REQUIRES_BEHAVIOR("Call Behavior actions must have their Behavior specified","No Method, Process or Screenflow  specified for {0}"),
	CALL_BEHAVIOR_ACTION_BEHAVIOR_IN_CONTEXT("Call Behavior actions must call a Behavior available locally or in the context",
			"Called Behavior {1} of {0} is not avaible locally in {2} or in the context {3}"),
	CALL_OPERATION_ACTION_REQUIRES_OPERATION("Call Operation actions must have their Behavior specified","No Operation or Reponsibility specified for {0}"),
	FEATURE_ACTION_REQUIRES_FEATURE("Structural Feature actions must have their Structural Feature specified","No Structural Feature specified for {0}"),
	VARIABLE_ACTION_REQUIRES_FEATURE("Variable actions must have their Variable specified","No Variable specified for {0}"),
	CREATE_OBJECT_ACTION_REQUIRES_CLASSIFIER("Create Object actions must have their Classifier specified","No Classifier specified for {0}"),
	EXPANSION_REGION_REQUIRES_INPUT_ELEMENT("Expansion regions must have exactly on Expansion Node as input element",
			"{0} does not have exactly one Expansion Node as input element"),
	EXPANSION_NODE_REQUIRES_EXPANSION_REGION("Expansion nodes must be specified as either input elements or output elements of an Expansion Region",
			"{0} is not associated with an Expansion Region as either input element or output element"),
	ACCEPT_CALL_RETURN_INFO_MUST_LINK("AcceptCallActions must have a returnInfo OutputPin that links up to the returnInfo InputPin of a ReplyAction",
			"{0} does not have a returnInfo output pin that links up with the returnInfo input pin of an subsequent ReplyAction"),
	REPLY_ACTION_RETURN_INFO_MUST_LINK("Reply actions must have a returnInfo InputPin that links up to the returnInfo OutputPin of a preceding AcceptCallAction",
			"{0} does not have a returnInfo InputPin that links up with the returnInfo OutputPin of an preceding AcceptCallAction"),
	ACCEPT_CALL_REQUIRES_SINGLE_CALL_EVENT("","{0} must have a single Trigger defined with a CallEvent specified as event "),
	REQUIRED_MULTIPLICITY("asdf","{0} must have a multiplicity compatible with that of {1} {2}"),
	SEND_SIGNAL_REQUIRES_BEHAVIORED_CLASSIFIER_TARGET(
			"Signals can only be sent to classes or interfaces",
			"Target elemet {0} of {1} evaluates to a type that is not a class or interface"),
	SEND_SIGNAL_TARGET_MUST_RECEIVE_SIGNAL("Signals cannot be sent to objects that do not declare receptions or triggers for he signal",
			"The type {2} of target element {0} of {1} does not declare a reception or trigger for signal {3}");
	private String description;
	private String messagePattern;
	private ActionValidationRule(String description,String messagePattern){
		this.description = description;
		this.messagePattern = messagePattern;
	}
	public String getDescription(){
		return this.description;
	}
	public String getMessagePattern(){
		return messagePattern;
	}
}
