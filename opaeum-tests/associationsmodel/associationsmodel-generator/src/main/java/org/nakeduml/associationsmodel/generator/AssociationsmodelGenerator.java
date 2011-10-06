package org.opeum.associationsmodel.generator;

import java.io.File;
import java.util.Set;

import net.sf.opeum.feature.TransformationStep;
import net.sf.opeum.pomgeneration.MavenProjectCodeGenerator;

public class AssociationsmodelGenerator extends MavenProjectCodeGenerator {


	/** Constructor for AssociationsmodelGenerator
	 * 
	 * @param outputRoot 
	 * @param modelFile 
	 */
	public AssociationsmodelGenerator(String outputRoot, String modelFile) {
		super(outputRoot,modelFile);
	}

	public Set<Class<? extends TransformationStep>> getIntegrationSteps() {
		return toSet(org.opeum.bootstrap.WarBootstrapStep.class,org.opeum.generation.features.IntegrationTestsAcrossMultipleModels.class,org.opeum.generation.features.HibernateIntegratedAcrossMultipleProjects.class,org.opeum.generation.features.Jbpm5IntegratedAcrossMultipleProjects.class);
	}
	
	public Set<Class<? extends TransformationStep>> getSteps() {
		return toSet(net.sf.opeum.javageneration.oclexpressions.OclExpressionExecution.class,org.opeum.generation.features.BpmUsingJbpm5.class,org.opeum.generation.features.IntegrationTests.class,net.sf.opeum.emf.extraction.StereotypeApplicationExtractor.class,org.opeum.generation.features.PersistenceUsingHibernate.class,org.opeum.generation.features.ExtendedCompositionSemantics.class,net.sf.opeum.javageneration.hibernate.PersistenceUsingHibernateStep.class);
	}
	
	static public void main(String[] args) throws Exception {
		File workspaceFile = null;
		if ( args.length>0 ) {
			workspaceFile=new File(args[0]);
		} else {
			workspaceFile=new File(".").getAbsoluteFile().getParentFile().getParentFile().getParentFile();
		}
		AssociationsmodelGenerator instance = new AssociationsmodelGenerator(workspaceFile.getAbsolutePath() +"/associationsmodel",workspaceFile.getAbsolutePath()+"/TestModels/Models/AssocationClassModel");
		
		instance.transformDirectory();
		instance.generateIntegrationCode();
	}

}