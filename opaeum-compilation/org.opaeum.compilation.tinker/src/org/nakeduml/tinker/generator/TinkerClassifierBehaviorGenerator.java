package org.nakeduml.tinker.generator;

import java.util.Collection;

import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitAfter;
import org.opaeum.java.metamodel.OJAnnonymousInnerClass;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJTryStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.javageneration.StereotypeAnnotator;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.metamodel.commonbehaviors.INakedBehavioredClassifier;
import org.opaeum.metamodel.commonbehaviors.INakedReception;
import org.opaeum.metamodel.commonbehaviors.INakedSignal;
import org.opaeum.metamodel.core.INakedSimpleType;
import org.opaeum.name.NameConverter;

@StepDependency(phase = JavaTransformationPhase.class, requires = { TinkerImplementNodeStep.class }, after = { TinkerImplementNodeStep.class })
public class TinkerClassifierBehaviorGenerator extends StereotypeAnnotator {

	@VisitAfter(matchSubclasses = true)
	public void visitSignal(INakedBehavioredClassifier c) {
		if (OJUtil.hasOJClass(c) && !(c instanceof INakedSimpleType) && c.getClassifierBehavior() != null) {
			OJAnnotatedClass ojClass = findJavaClass(c);
			implementReceiveSignal(ojClass, c);
			implementReception(ojClass, c);
		}
	}

	private void implementReception(OJAnnotatedClass ojClass, INakedBehavioredClassifier c) {
		Collection<INakedReception> receptions = c.getOwnedReceptions();
		for (INakedReception reception : receptions) {
			OJAnnotatedOperation receptionOper = new OJAnnotatedOperation(NameConverter.decapitalize(reception.getName()));
			OJParameter parameter = new OJParameter("signal", TinkerBehaviorUtil.signalPathName);
			parameter.setFinal(true);
			receptionOper.addToParameters(parameter);
			OJAnnonymousInnerClass classifierSignalEvent = new OJAnnonymousInnerClass(ojClass.getPathName(), "classifierSignalEvent",
					TinkerBehaviorUtil.tinkerClassifierSignalEvent);
			receptionOper.getBody().addToLocals(classifierSignalEvent);

			OJAnnotatedOperation call = new OJAnnotatedOperation("call");
			call.setReturnType(new OJPathName("java.lang.Boolean"));
			call.addToThrows(new OJPathName("java.lang.Exception"));
			call.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
			call.getBody().addToStatements("GraphDb.getDb().startTransaction()");
			OJTryStatement tryS = new OJTryStatement();
			tryS.getTryPart().addToStatements("removeFromEventPool(signal)");
			
			tryS.getTryPart().addToStatements("Set<AbstractNode> nodesToTrigger = getClassifierBehavior().getEnabledNodesWithMatchingTrigger(signal)");
			OJIfStatement ifNodesToTrigger = new OJIfStatement("!nodesToTrigger.isEmpty()");
			ifNodesToTrigger.addToThenPart("AbstractNode acceptEventAction = nodesToTrigger.iterator().next()");
			ifNodesToTrigger.addToThenPart("acceptEventAction.setStarts(new SingleIterator<ControlToken>(new ControlToken(acceptEventAction.getName())))");
			ifNodesToTrigger.addToThenPart("acceptEventAction.next()");
			tryS.getTryPart().addToStatements(ifNodesToTrigger);
			ojClass.addToImports(TinkerBehaviorUtil.tinkerAbstractNodePathName);
			ojClass.addToImports(new OJPathName("java.util.Set"));
			ojClass.addToImports(TinkerBehaviorUtil.tinkerSingleIteratorPathName);
			ojClass.addToImports(TinkerBehaviorUtil.tinkerControlTokenPathName);
			
			tryS.getTryPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.SUCCESS)");
			ojClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);
			tryS.setCatchParam(new OJParameter("e", new OJPathName("java.loang.Exception")));
			tryS.getCatchPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.FAILURE)");
			tryS.getCatchPart().addToStatements("throw e");
			call.getBody().addToStatements(tryS);
			call.getBody().addToStatements("return false; //TODO");
			classifierSignalEvent.getClassDeclaration().addToOperations(call);

			ojClass.addToOperations(receptionOper);
			
			receptionOper.getBody().addToStatements("addToEventPool(signal)");
			receptionOper.getBody().addToStatements(TinkerBehaviorUtil.tinkerClassifierBehaviorExecutorService.getLast() + ".INSTANCE.submit(classifierSignalEvent)");
			ojClass.addToImports(TinkerBehaviorUtil.tinkerClassifierBehaviorExecutorService);
		}
	}

	private void implementReceiveSignal(OJAnnotatedClass ojClass, INakedBehavioredClassifier c) {
		OJAnnotatedOperation receiveSignal = new OJAnnotatedOperation("receiveSignal");
		receiveSignal.addParam("signal", TinkerBehaviorUtil.signalPathName);
		Collection<INakedReception> receptions = c.getOwnedReceptions();
		for (INakedReception reception : receptions) {
			INakedSignal signal = reception.getSignal();
			OJIfStatement ifHasReception = new OJIfStatement("signal instanceof " + signal.getMappingInfo().getJavaName());
			ifHasReception.addToThenPart(NameConverter.decapitalize(reception.getName()) + "((" + signal.getMappingInfo().getJavaName() + ")signal)");
			receiveSignal.getBody().addToStatements(ifHasReception);
		}
		ojClass.addToOperations(receiveSignal);
	}

}
