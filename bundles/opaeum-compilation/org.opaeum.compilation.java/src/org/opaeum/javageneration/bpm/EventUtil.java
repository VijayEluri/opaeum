package org.opaeum.javageneration.bpm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.uml2.uml.AcceptEventAction;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.ChangeEvent;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.TimeEvent;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.Vertex;
import org.opaeum.eclipse.EmfActivityUtil;
import org.opaeum.eclipse.EmfElementFinder;
import org.opaeum.eclipse.EmfEventUtil;
import org.opaeum.emf.workspace.EmfWorkspace;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.opaeum.javageneration.oclexpressions.ValueSpecificationUtil;
import org.opaeum.javageneration.util.JavaNameGenerator;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.metamodel.core.internal.TagNames;
import org.opaeum.metamodel.workspace.OpaeumLibrary;
import org.opaeum.name.NameConverter;
import org.opaeum.ocl.uml.OpaqueExpressionContext;
import org.opaeum.runtime.domain.BusinessTimeUnit;
import org.opaeum.runtime.domain.CancelledEvent;
import org.opaeum.runtime.domain.IEventGenerator;
import org.opaeum.runtime.domain.OutgoingEvent;

//TODO refactor into an EventMap
public class EventUtil{
	OpaeumLibrary library;
	private ValueSpecificationUtil valueSpecificationUtil;
	private OJUtil ojUtil;
	public EventUtil(OJUtil ojUtil){
		super();
		this.ojUtil = ojUtil;
		this.library = ojUtil.getLibrary();
		this.valueSpecificationUtil = new ValueSpecificationUtil(ojUtil);
	}
	public String getEventConsumerName(NamedElement e){
		if(e instanceof SignalEvent){
			return ojUtil.buildSignalMap(((SignalEvent) e).getSignal()).eventConsumerMethodName();
		}else if(e instanceof CallEvent){
			return ojUtil.buildOperationMap(((CallEvent) e).getOperation()).eventConsumerMethodName();
		}else if(e instanceof Vertex){
			return JavaNameGenerator.toJavaName("on" + NameConverter.capitalize(e.getName()) + "Completed");
		}else if(e instanceof Event){
			return JavaNameGenerator.toJavaName("onOccurrenceOf" + NameConverter.capitalize(e.getName()));
		}else{
			return JavaNameGenerator.toJavaName("on" + NameConverter.capitalize(e.getName()));
		}
	}
	public OJPathName handlerPathName(Operation s){
		return ojUtil.buildOperationMap(s).handlerPath();
	}
	public OJPathName handlerPathName(NamedElement ne){
		Namespace ctx;
		if(ne instanceof Event){
			Event e = (Event) ne;
			if(EmfEventUtil.isDeadline(e)){
				ctx = (Namespace) EmfElementFinder.getContainer(e);
			}else{
				ctx = EmfEventUtil.getBehavioralNamespaceContext(e);
			}
			if(ctx == null){
				ctx = EmfElementFinder.getNearestNamespace(e);
			}
		}else{
			ctx = EmfElementFinder.getNearestNamespace(ne);
		}
		return ojUtil.packagePathname(ctx).getCopy().append(NameConverter.capitalize(ne.getName()) + "Handler");
	}
	public void implementChangeEventRequest(OJOperation operation,NamedElement ne,OpaqueExpression when){
		OJAnnotatedClass owner = (OJAnnotatedClass) operation.getOwner();
		if(when != null){
			String changeExpr = valueSpecificationUtil.expressOcl(library.getOclExpressionContext(when), operation, library.getBooleanType());
			OJAnnotatedOperation evaluationMethod = new OJAnnotatedOperation(evaluatorName(ne), new OJPathName("boolean"));
			evaluationMethod.getBody().addToStatements("return " + changeExpr);
			owner.addToOperations(evaluationMethod);
			OJPathName handler = handlerPathName(ne);
			owner.addToImports(handler);
			operation.getBody().addToStatements("getOutgoingEvents().add(new OutgoingEvent(this, new " + handler.getLast() + "(token))");
		}else{
			operation.getBody().addToStatements("NO_CHANGE_EXPRESSION_SPECIFIED");
		}
	}
	public static String evaluatorName(NamedElement event){
		return "evaluate" + NameConverter.capitalize(event.getName());
	}
	public static void addOutgoingEventManagement(OJClass ojClass){
		OJPathName pn = new OJPathName(IEventGenerator.class.getName());
		if(!ojClass.getImplementedInterfaces().contains(pn)){
			ojClass.addToImplementedInterfaces(pn);
			ojClass.addToImports(Set.class.getName());
			ojClass.addToImports(HashSet.class.getName());
			ojClass.addToImports(OutgoingEvent.class.getName());
			ojClass.addToImports(CancelledEvent.class.getName());
			OJPathName eventCancellationPath = new OJPathName(Set.class.getName());
			eventCancellationPath.addToElementTypes(new OJPathName(CancelledEvent.class.getName()));
			OJAnnotatedField field1 = OJUtil.addTransientProperty(ojClass, "cancelledEvents", eventCancellationPath, true);
			field1.setInitExp("new HashSet<CancelledEvent>()");
			ojClass.addToImports(HashMap.class.getName());
			field1.putAnnotation(new OJAnnotationValue(new OJPathName("javax.persistence.Transient")));
			OJPathName eventSetPath = new OJPathName(Set.class.getName());
			eventSetPath.addToElementTypes(new OJPathName(OutgoingEvent.class.getName()));
			OJAnnotatedField field2 = OJUtil.addTransientProperty(ojClass, "outgoingEvents", eventSetPath, true);
			field2.setInitExp("new HashSet<OutgoingEvent>()");
			field2.putAnnotation(new OJAnnotationValue(new OJPathName("javax.persistence.Transient")));
		}
	}
	public void implementTimeEventRequest(OJOperation operation,OJBlock block,TimeEvent event,boolean businessTime){
		implementTimerRequest(operation, block, event, "this", businessTime);
	}
	public void implementDeadlineRequest(OJOperation operation,OJBlock block,TimeEvent event,String taskName){
		implementTimerRequest(operation, block, event, taskName, true);
	}
	private void implementTimerRequest(OJOperation operation,OJBlock block,TimeEvent event,String targetExpression,boolean businessTime){
		if(event.getWhen() != null && event.getWhen().getExpr() != null){
			OJAnnotatedClass owner = (OJAnnotatedClass) operation.getOwner();
			ValueSpecification when = event.getWhen().getExpr();
			OJPathName eventHandler = handlerPathName(event);
			owner.addToImports(eventHandler);
			String whenExpr = valueSpecificationUtil.expressValue(operation, when, library.getEventGeneratingClassifier(event), null);
			if(event.isRelative()){
				EnumerationLiteral timeUnit = EmfEventUtil.getTimeUnit(event);
				OpaqueExpressionContext businessCalendarToUse = library.getArtificationExpression(event, TagNames.BUSINESS_CALENDAR_TO_USE);
				if(businessCalendarToUse == null || businessCalendarToUse.hasErrors()){
					block.addToStatements("this.firstOccurrenceScheduledFor=BusinessCalendar.getInstance().addTimeTo(new Date(), timeUnit,delay)");
				}else{
					block.addToStatements("this.firstOccurrenceScheduledFor="
							+ valueSpecificationUtil.expressOcl(businessCalendarToUse, operation, null) + ".addTimeTo(new Date(), timeUnit,delay)");
				}
				String timeUnitConstant = timeUnit == null ? "BUSINESSDAY" : OJUtil.toJavaLiteral(timeUnit);
				owner.addToImports(BusinessTimeUnit.class.getName());
				block.addToStatements("getOutgoingEvents().add(new OutgoingEvent(" + targetExpression + ",new " + eventHandler.getLast() + "("
						+ whenExpr + ",BusinessTimeUnit." + timeUnitConstant + ",token)))");
			}else{
				// TODO add the timeSpecification INstanceSpecification values
				block.addToStatements("getOutgoingEvents().add(new OutgoingEvent(" + targetExpression + ",new " + eventHandler.getLast() + "("
						+ whenExpr + ",token)))");
			}
		}
	}
	public static void cancelTimer(OJBlock block,TimeEvent event,String targetExpression){
		block.addToStatements("getCancelledEvents().add(new CancelledEvent(" + targetExpression + ",\"" + EmfWorkspace.getId(event) + "\"))");
	}
	public static void cancelChangeEvent(OJBlock block,ChangeEvent event){
		block.addToStatements("getCancelledEvents().add(new CancelledEvent(this,\"" + EmfWorkspace.getId(event) + "\"))");
	}
	public static String getInvokerName(Operation o){
		return ((NamedElement) o.getOwner()).getName() + NameConverter.capitalize(o.getName()) + EmfWorkspace.getOpaeumId(o) + "Invoker";
	}
	public void requestTokenCreatingEvents(OJAnnotatedOperation operation,Collection<ActivityNode> activityNodes,boolean businessCalendarAvailable){
		for(ActivityNode node:activityNodes){
			if(node instanceof AcceptEventAction && EmfActivityUtil.getAllEffectiveIncoming(node).isEmpty()){
				AcceptEventAction acceptEventAction = (AcceptEventAction) node;
				for(Trigger t:acceptEventAction.getTriggers()){
					if(t.getEvent() instanceof TimeEvent){
						implementTimeEventRequest(operation, operation.getBody(), (TimeEvent) t.getEvent(), businessCalendarAvailable);
					}else if(t.getEvent() instanceof ChangeEvent){
						ChangeEvent ce = (ChangeEvent) t.getEvent();
						if(ce.getChangeExpression() instanceof OpaqueExpression){
							implementChangeEventRequest(operation, ce, (OpaqueExpression) ce.getChangeExpression());
						}
					}
				}
			}
		}
	}
	public static void cancelEvents(OJBlock block,Collection<ActivityNode> activityNodes){
		for(ActivityNode node:activityNodes){
			if(node instanceof AcceptEventAction && EmfActivityUtil.getAllEffectiveIncoming(node).isEmpty()){
				AcceptEventAction acceptEventAction = (AcceptEventAction) node;
				Collection<Trigger> triggers = acceptEventAction.getTriggers();
				for(Trigger t:triggers){
					if(t.getEvent() instanceof TimeEvent){
						cancelTimer(block, (TimeEvent) t.getEvent(), "this");
					}else if(t.getEvent() instanceof ChangeEvent){
						cancelChangeEvent(block, (ChangeEvent) t.getEvent());
					}
				}
			}
		}
	}
	public static String intervalCalculatorName(NamedElement e){
		return "calculateNextOccurrenceOf" + NameConverter.capitalize(e.getName());
	}
}