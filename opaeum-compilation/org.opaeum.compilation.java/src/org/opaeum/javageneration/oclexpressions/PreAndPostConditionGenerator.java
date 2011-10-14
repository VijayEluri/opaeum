package org.opaeum.javageneration.oclexpressions;

import java.util.Collection;

import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.javageneration.AbstractJavaProducingVisitor;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.javageneration.basicjava.OperationAnnotator;
import org.opaeum.javageneration.basicjava.SpecificationImplementor;
import org.opaeum.javageneration.maps.NakedOperationMap;
import org.opaeum.javageneration.maps.NakedStructuralFeatureMap;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.linkage.BehaviorUtil;
import org.opaeum.linkage.NakedParsedOclStringResolver;
import org.opaeum.metamodel.commonbehaviors.INakedBehavior;
import org.opaeum.metamodel.commonbehaviors.INakedBehavioredClassifier;
import org.opaeum.metamodel.commonbehaviors.INakedOpaqueBehavior;
import org.opaeum.metamodel.core.INakedClassifier;
import org.opaeum.metamodel.core.INakedConstraint;
import org.opaeum.metamodel.core.INakedInterface;
import org.opaeum.metamodel.core.INakedMessageStructure;
import org.opaeum.metamodel.core.INakedOperation;
import org.opaeum.metamodel.core.INakedValueSpecification;
import org.opaeum.metamodel.core.IParameterOwner;

//TODO implement post conditions 
//as a method similar to "checkInvariants" on operations that are represented as classifiers/ tuples
//or in the finally block
@StepDependency(phase = JavaTransformationPhase.class,requires = {
		OperationAnnotator.class,SpecificationImplementor.class,NakedParsedOclStringResolver.class
},after = {
		OperationAnnotator.class,SpecificationImplementor.class
})
public class PreAndPostConditionGenerator extends AbstractJavaProducingVisitor{
	@VisitBefore(matchSubclasses = true)
	public void visitBehavioredClassifier(INakedBehavioredClassifier bc){
		for(INakedBehavior b:bc.getOwnedBehaviors()){
			visitBehavior(b);
			if(b instanceof INakedOpaqueBehavior){
				visitOpaqueBehavior((INakedOpaqueBehavior) b);
			}
		}
	}
	private void visitBehavior(INakedBehavior behavior){
		if(OJUtil.hasOJClass(behavior.getContext()) && behavior.getOwnerElement() instanceof INakedClassifier){
			// Ignore transition effects and state actions for now
			if(BehaviorUtil.hasExecutionInstance(behavior)){
				addEvaluationMethod(behavior.getPreConditions(), "evaluatePreConditions", behavior, "this");
				addEvaluationMethod(behavior.getPostConditions(), "evaluatePostConditions", behavior, "this");
			}else{
				NakedOperationMap mapper = new NakedOperationMap(behavior);
				addLocalConditions(behavior.getContext(), mapper, behavior.getPreConditions(), true);
				addLocalConditions(behavior.getContext(), mapper, behavior.getPostConditions(), false);
			}
		}
	}
	private void visitOpaqueBehavior(INakedOpaqueBehavior behavior){
		if(BehaviorUtil.hasExecutionInstance(behavior)){
			OJAnnotatedClass javaContext = findJavaClass(behavior);
			OJAnnotatedOperation execute = (OJAnnotatedOperation) OJUtil.findOperation(javaContext, "execute");
			if(execute == null){
				execute = new OJAnnotatedOperation("execute");
				javaContext.addToOperations(execute);
			}
			if(behavior.getBodyExpression() != null){
				NakedOperationMap map = new NakedOperationMap(behavior);
				INakedClassifier owner = behavior.getContext();
				INakedValueSpecification specification = behavior.getBody();
				if(map.getParameterOwner().getReturnParameter() == null){
					execute.getBody().addToStatements(ValueSpecificationUtil.expressValue(execute, specification, owner, specification.getType()));
				}else{
					OJField result = new OJField();
					result.setName("result");
					result.setType(map.javaReturnTypePath());
					result.setInitExp(map.javaReturnDefaultValue());
					IParameterOwner oper = map.getParameterOwner();
					execute.getBody().addToLocals(result);
					NakedStructuralFeatureMap resultMap = OJUtil.buildStructuralFeatureMap(behavior, behavior.getReturnParameter());
					execute.getBody()
							.addToStatements("result=" + ValueSpecificationUtil.expressValue(execute, specification, owner, oper.getReturnParameter().getType()));
					execute.getBody().addToStatements(resultMap.setter() + "(result)");
				}
			}
		}else if(OJUtil.hasOJClass(behavior.getContext()) && behavior.getOwnerElement() instanceof INakedClassifier && behavior.getBodyExpression() != null){
			if(behavior.getName().equals("transformationForObjectFlow1")){
				System.out.println();
			}
			OJAnnotatedClass javaContext = findJavaClass(behavior.getContext());
			NakedOperationMap map = new NakedOperationMap(behavior);
			OJAnnotatedOperation oper = (OJAnnotatedOperation) javaContext.findOperation(map.javaOperName(), map.javaParamTypePaths());
			this.addBody(oper, behavior.getContext(), map, behavior.getBody());
		}
	}
	@VisitBefore(matchSubclasses = true)
	public void visitClassifier(INakedClassifier owner){
		if(OJUtil.hasOJClass(owner) && !(owner instanceof INakedInterface)){
			for(INakedOperation oper:owner.getEffectiveOperations()){
				if(oper.getOwner() instanceof INakedInterface || oper.getOwner() == owner){
					processOperation(oper, owner);
				}
			}
		}
	}
	private void processOperation(INakedOperation oper,INakedClassifier owner){
		NakedOperationMap mapper = new NakedOperationMap(oper);
		if(oper.getBodyCondition() != null && oper.getBodyCondition().getSpecification() != null){
			OJPathName path = OJUtil.classifierPathname(owner);
			OJClass myOwner = javaModel.findClass(path);
			if(myOwner != null){
				OJAnnotatedOperation myOper = (OJAnnotatedOperation) myOwner.findOperation(mapper.javaOperName(), mapper.javaParamTypePaths());
				INakedValueSpecification specification = oper.getBodyCondition().getSpecification();
				addBody(myOper, owner, mapper, specification);
			}
		}
		//
		if(BehaviorUtil.hasExecutionInstance(oper) && oper.getMethods().isEmpty()){
			INakedMessageStructure messageClass = oper.getMessageStructure();
			addEvaluationMethod(oper.getPreConditions(), "evaluatePreConditions", messageClass, "getContextObject()");
			addEvaluationMethod(oper.getPostConditions(), "evaluatePostConditions", messageClass, "getContextObject()");
		}else{
			addLocalConditions(owner, mapper, oper.getPreConditions(), true);
			if(!oper.isLongRunning()){
				// implement on Operation Message Structure instead
				addLocalConditions(owner, mapper, oper.getPostConditions(), false);
			}
		}
	}
	public void addLocalConditions(INakedClassifier owner,NakedOperationMap mapper,Collection<INakedConstraint> conditions,boolean pre){
		OJClass myOwner = findJavaClass(owner);
		OJOperation myOper1 = myOwner.findOperation(mapper.javaOperName(), mapper.javaParamTypePaths());
		ConstraintGenerator cg = new ConstraintGenerator(myOwner, mapper.getParameterOwner());
		if(conditions.size() > 0){
			cg.addConstraintChecks(myOper1, conditions, pre,"this");
		}
	}
	public void addEvaluationMethod(Collection<INakedConstraint> conditions,String evaluationMethodName,INakedClassifier messageClass,String selfExpression){
		if(conditions.size() > 0){
			OJClass myOwner = findJavaClass(messageClass);
			OJOperation myOper1 = new OJAnnotatedOperation(evaluationMethodName);
			myOwner.addToOperations(myOper1);
			ConstraintGenerator cg = new ConstraintGenerator(myOwner, messageClass);
			if(conditions.size() > 0){
				cg.addConstraintChecks(myOper1, conditions, true, selfExpression);
				// true because they can sit anywhere in the method
			}
		}
	}
	private void addBody(OJAnnotatedOperation ojOper,INakedClassifier owner,NakedOperationMap map,INakedValueSpecification specification){
		if(map.getParameterOwner().getReturnParameter() == null){
			ojOper.getBody().addToStatements(ValueSpecificationUtil.expressValue(ojOper, specification, owner, specification.getType()));
		}else{
			IParameterOwner oper = map.getParameterOwner();
			ojOper.initializeResultVariable(ValueSpecificationUtil.expressValue(ojOper, specification, owner, oper.getReturnParameter().getType()));
		}
	}
}