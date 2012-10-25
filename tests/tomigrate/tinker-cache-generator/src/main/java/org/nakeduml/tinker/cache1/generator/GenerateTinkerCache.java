package org.opaeum.tinker.cache1.generator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.opaeum.emf.load.EmfWorkspaceLoader;
import net.sf.opaeum.emf.workspace.EmfWorkspace;
import net.sf.opaeum.feature.NakedUmlConfig;
import net.sf.opaeum.feature.TransformationProcess;
import net.sf.opaeum.feature.TransformationStep;
import net.sf.opaeum.javageneration.JavaTextSource;

import org.opaeum.tinker.auditing.TinkerImplementAttributeCacheStep;
import org.opaeum.tinker.composition.tinker.TinkerExtendedCompositionSemanticsJavaStep;

public class GenerateTinkerCache {

	protected File outputRoot;
	protected File modelFile;
	protected TransformationProcess process = new TransformationProcess();

	public static void main(String[] args) throws Exception {
		File model = new File("../TestModels/Models/tinker/tinker.uml");
		File outputRoot = new File("../tinker-cache/");
		System.out.println(outputRoot.getAbsolutePath());
		GenerateTinkerCache g = new GenerateTinkerCache(outputRoot, model);
		g.generate();
	}

	public GenerateTinkerCache(File outputRoot, File modelFile) {
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
		return toSet(net.sf.opaeum.javageneration.basicjava.BasicJavaModelStep.class,
				net.sf.opaeum.javageneration.composition.ExtendedCompositionSemanticsJavaStep.class,
				net.sf.opaeum.emf.extraction.StereotypeApplicationExtractor.class, TinkerExtendedCompositionSemanticsJavaStep.class,
				TinkerImplementAttributeCacheStep.class);
	}

	protected NakedUmlConfig buildConfig(EmfWorkspace workspace) throws IOException {
		NakedUmlConfig cfg = new NakedUmlConfig();
		cfg.setOutputRoot(outputRoot);
		cfg.load(new File(modelFile.getParent(), workspace.getDirectoryName() + "-opaeum.properties"), workspace.getName());
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