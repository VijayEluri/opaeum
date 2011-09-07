package net.sf.nakeduml.javageneration.basicjava.simpleactions;

import java.util.Iterator;

import net.sf.nakeduml.javageneration.basicjava.AbstractObjectNodeExpressor;
import net.sf.nakeduml.javageneration.maps.NakedClassifierMap;
import net.sf.nakeduml.javageneration.maps.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.maps.SignalMap;
import net.sf.nakeduml.metamodel.actions.INakedSendSignalAction;
import net.sf.nakeduml.metamodel.activities.INakedInputPin;
import net.sf.nakeduml.metamodel.activities.INakedPin;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.workspace.NakedUmlLibrary;
import nl.klasse.octopus.codegen.umlToJava.maps.ClassifierMap;

import org.nakeduml.java.metamodel.OJBlock;
import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedField;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;

public class SignalSender extends SimpleNodeBuilder<INakedSendSignalAction>{
	public SignalSender(NakedUmlLibrary oclEngine,INakedSendSignalAction action,AbstractObjectNodeExpressor expressor){
		super(oclEngine, action, expressor);
	}
	@Override
	public void implementActionOn(OJAnnotatedOperation operation,OJBlock block){
		SignalMap signalMap = new SignalMap(node.getSignal());
		Iterator<INakedInputPin> args = node.getArguments().iterator();
		String signalName = "_signal" + node.getMappingInfo().getJavaName();
		ClassifierMap cm = new NakedClassifierMap(node.getSignal());
		operation.getOwner().addToImports(cm.javaTypePath());
		while(args.hasNext()){
			INakedPin pin = args.next();
			if(pin.getLinkedTypedElement() == null){
				block.addToStatements(signalName + "couldNotLinkPinToProperty!!!");
			}else{
				NakedStructuralFeatureMap map = new NakedStructuralFeatureMap((INakedProperty) pin.getLinkedTypedElement());
				block.addToStatements(signalName + "." + map.setter() + "(" + readPin(operation, block, pin) + ")");
			}
		}
		OJAnnotatedField signal = new OJAnnotatedField(signalName, cm.javaTypePath());
		block.addToLocals(signal);
		String source = "this";
		String targetExpression;
		if(node.getTarget() != null){
			targetExpression = readPin(operation, block, node.getTarget());
		}else{
			targetExpression = source;
		}
		OJPathName handlerPathName = signalMap.handlerTypePath();
		operation.getOwner().addToImports(handlerPathName);
		block.addToStatements("getOutgoingEvents().add(new OutgoingEvent(" + targetExpression + ",new " +handlerPathName.getLast() + "("+ signalName + ",false)))");
		signal.setType(cm.javaTypePath());
		signal.setInitExp("new " + node.getSignal().getMappingInfo().getJavaName() + "()");
	}
}
