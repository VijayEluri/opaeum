package org.nakeduml.projectgeneration;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.filegeneration.TextFileGenerator;
import net.sf.nakeduml.javageneration.CharArrayTextSource;
import net.sf.nakeduml.metamodel.models.INakedModel;
import net.sf.nakeduml.pomgeneration.BasicJavaAdaptorPomStep;
import net.sf.nakeduml.pomgeneration.BasicJavaIntegratedAdaptorPomStep;
import net.sf.nakeduml.pomgeneration.WarPomStep;

@StepDependency(phase = DefaultConfigGenerationPhase.class, requires = { BasicJavaIntegratedAdaptorPomStep.class, TextFileGenerator.class }, before = { TextFileGenerator.class })
public class IntegratedArquillianConfigStep extends AbstractProjectGenerationStep {
	@VisitBefore
	public void visitModel(INakedModel model) {
		createConfig("beans.xml", CharArrayTextSource.OutputRootId.INTEGRATED_ADAPTOR_RESOURCE, "META-INF");
		createConfig("arquillian.xml", CharArrayTextSource.OutputRootId.INTEGRATED_ADAPTOR_TEST_RESOURCE);
		createConfig("log4j.properties", CharArrayTextSource.OutputRootId.INTEGRATED_ADAPTOR_TEST_RESOURCE);
		createConfig("hornetq-jms.xml", CharArrayTextSource.OutputRootId.INTEGRATED_ADAPTOR_TEST_RESOURCE_JBOSSAS);
		createConfig("jndi.properties", CharArrayTextSource.OutputRootId.INTEGRATED_ADAPTOR_TEST_RESOURCE_JBOSSAS);
	}
}
