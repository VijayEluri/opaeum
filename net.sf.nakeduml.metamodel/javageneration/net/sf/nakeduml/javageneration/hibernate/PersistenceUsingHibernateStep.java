package net.sf.nakeduml.javageneration.hibernate;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.TransformationContext;
import net.sf.nakeduml.javageneration.AbstractJavaTransformationStep;
import net.sf.nakeduml.javageneration.JavaTransformationPhase;
import net.sf.nakeduml.javageneration.oclexpressions.OclExpressionExecution;
import net.sf.nakeduml.javageneration.persistence.PersistenceStep;
import net.sf.nakeduml.linkage.InverseCalculator;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import net.sf.nakeduml.validation.namegeneration.PersistentNameGenerator;

@StepDependency(phase = JavaTransformationPhase.class,requires = {PersistenceStep.class,InverseCalculator.class,
		PersistentNameGenerator.class},after = {OclExpressionExecution.class/* to ensure hibernate configurator is done afterwards*/,PersistenceStep.class}, before = {})
public class PersistenceUsingHibernateStep extends AbstractJavaTransformationStep{
	@Override
	public void generate(INakedModelWorkspace workspace,TransformationContext context){
		HibernateAnnotator ha = new HibernateAnnotator();
		ha.initialize(workspace, javaModel, config, textWorkspace);
		ha.startVisiting(workspace);
		

//		
//		OneToOneOptimiser otoo=new OneToOneOptimiser();
//		otoo.initialize(workspace, javaModel, config, textWorkspace);
//		otoo.startVisiting(workspace);
		// EnumerationLiteralNameAdder asdf = new
		// EnumerationLiteralNameAdder(javaModel, config);
		// asdf.startVisiting(workspace);
		
//		TooManyNavigationSupport tns=new TooManyNavigationSupport();
//		tns.initialize(workspace, javaModel, config, textWorkspace);
//		tns.startVisiting(workspace);

	}
}
