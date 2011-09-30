package org.opeum.tinker.withcache.test.generator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.opeum.emf.load.EmfWorkspaceLoader;
import net.sf.opeum.emf.workspace.EmfWorkspace;
import net.sf.opeum.feature.NakedUmlConfig;
import net.sf.opeum.feature.TransformationProcess;
import net.sf.opeum.feature.TransformationStep;
import net.sf.opeum.javageneration.JavaTextSource;

import org.opeum.tinker.auditing.TinkerAuditImplementationStep;
import org.opeum.tinker.auditing.TinkerImplementAttributeCacheStep;

public class GenerateTinkerAuditWithCache {

	protected File outputRoot;  
	protected File modelFile;
	protected TransformationProcess process = new TransformationProcess();

	public static void main(String[] args) throws Exception {
		File model = new File("../TestModels/Models/tinker/tinker.uml");
		File outputRoot = new File("../tinker-audit2-cache/");
		System.out.println(outputRoot.getAbsolutePath());
		GenerateTinkerAuditWithCache g = new GenerateTinkerAuditWithCache(outputRoot, model);
		g.generate();
	}

	public GenerateTinkerAuditWithCache(File outputRoot, File modelFile) {
		super();
		this.outputRoot = outputRoot;
		this.modelFile = modelFile;
	}

	private void generate() throws Exception {
		long start = System.currentTimeMillis();
		EmfWorkspace workspace = EmfWorkspaceLoader.loadSingleModelWorkspace(modelFile, outputRoot.getName());
		workspace.setDirectoryName(outputRoot.getName());
		NakedUmlConfig cfg = buildConfig(workspace);
		cfg.store();
		process.execute(cfg, workspace, getSteps());
		workspace.getMappingInfo().store();
		System.out.println("Generating code for model '" + modelFile.getName() + "' took " + (System.currentTimeMillis() - start) + " ms");
	}

	@SuppressWarnings("unchecked")
	private Set<Class<? extends TransformationStep>> getSteps() {
		return toSet(net.sf.opeum.javageneration.basicjava.BasicJavaModelStep.class, 
				net.sf.opeum.javageneration.composition.ExtendedCompositionSemanticsJavaStep.class,
				net.sf.opeum.emf.extraction.StereotypeApplicationExtractor.class,
				TinkerAuditImplementationStep.class,
				TinkerImplementAttributeCacheStep.class);
	}

	protected NakedUmlConfig buildConfig(EmfWorkspace workspace) throws IOException {
		NakedUmlConfig cfg = new NakedUmlConfig();
		cfg.setOutputRoot(outputRoot);
		cfg.load(new File(modelFile.getParent(), workspace.getDirectoryName() + "-opeum.properties"), workspace.getName());
		cfg.store();
		mapOutputRoots(cfg);
		return cfg;
	}

	protected void mapOutputRoots(NakedUmlConfig cfg) {
		mapDomainProjects(cfg);
	}

	private void mapDomainProjects(NakedUmlConfig cfg) {
		cfg.mapOutputRoot(JavaTextSource.OutputRootId.DOMAIN_GEN_SRC, true, "", "../src/main/generated-java");
	}

	protected static Set<Class<? extends TransformationStep>> toSet(Class<? extends TransformationStep>... classes) {
		return new HashSet<Class<? extends TransformationStep>>(Arrays.asList(classes));
	}
}
