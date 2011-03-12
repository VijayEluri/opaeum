package net.sf.nakeduml.pomgeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sf.nakeduml.feature.OutputRoot;
import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.javageneration.CharArrayTextSource;

import org.apache.maven.pom.Dependency;
import org.apache.maven.pom.POMFactory;

@StepDependency(phase = PomGenerationPhase.class, requires = { BasicJavaIntegratedAdaptorPomStep.class })
public class IntegratedSeam3PomStep extends PomGenerationStep {
	@Override
	public Dependency[] getDependencies() {
		List<Dependency> dependencies = new ArrayList<Dependency>();
		addJbossJeeSpec(dependencies);
		addCdi(dependencies);
		Dependency slf4j = POMFactory.eINSTANCE.createDependency();
		slf4j.setGroupId("org.slf4j");
		slf4j.setArtifactId("slf4j-log4j12");
		slf4j.setVersion("1.6.1");
		dependencies.add(slf4j);
		Dependency seamPersistenceApi = POMFactory.eINSTANCE.createDependency();
		seamPersistenceApi.setGroupId("org.jboss.seam.persistence");
		seamPersistenceApi.setArtifactId("seam-persistence-api");
		seamPersistenceApi.setVersion("${seam.persistence.version}");
		seamPersistenceApi.setScope("compile");
		dependencies.add(seamPersistenceApi);
		Dependency seamPersistenceImpl = POMFactory.eINSTANCE.createDependency();
		seamPersistenceImpl.setGroupId("org.jboss.seam.persistence");
		seamPersistenceImpl.setArtifactId("seam-persistence-impl");
		seamPersistenceImpl.setVersion("${seam.persistence.version}");
		seamPersistenceImpl.setScope("runtime");
		seamPersistenceImpl.setType("jar");
		dependencies.add(seamPersistenceImpl);
		addSeamSolderApi(dependencies);
		addSeamSolderImpl(dependencies);
		Dependency nakedUmlUtil = POMFactory.eINSTANCE.createDependency();
		nakedUmlUtil.setGroupId("org.nakeduml");
		nakedUmlUtil.setArtifactId("nakeduml-runtime-adaptor");
		nakedUmlUtil.setVersion("${numl.version}");
		nakedUmlUtil.setType("jar");
		nakedUmlUtil.setExclusions(POMFactory.eINSTANCE.createExclusionsType());
		super.addArquillian(dependencies);
		return dependencies.toArray(new Dependency[dependencies.size()]);
	}

	@Override
	public Properties getParentPomProperties() {
		Properties p = super.getParentPomProperties();
		p.put("jboss.home", "${env.JBOSS_HOME}");
		p.put("jboss.domain", "default");
		p.put("seam.persistence.version", "3.0.0-SNAPSHOT");
		p.put("seam.solder.version", "3.0.0.Beta1");
		p.put("seam.servlet.version", "3.0.0.Alpha3");
		p.put("numl.version",PomGenerationPhase.NUML_VERSION );
		return p;
	}

	@Override
	public boolean hasFinalName() {
		return true;
	}

	@Override
	public String getPackaging() {
		return "jar";
	}

	@Override
	public OutputRoot getExampleTargetDir() {
		return config.getOutputRoot(CharArrayTextSource.OutputRootId.INTEGRATED_ADAPTOR_GEN_RESOURCE);
	}




}