package org.nakeduml.eclipse.starter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import net.sf.nakeduml.emf.extraction.StereotypeApplicationExtractor;
import net.sf.nakeduml.emf.workspace.EmfWorkspace;
import net.sf.nakeduml.emf.workspace.UriResolver;
import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.feature.TransformationStep;
import net.sf.nakeduml.javageneration.MavenProjectCodeGenerator;
import net.sf.nakeduml.javageneration.hibernate.HibernateConfigGenerator;
import net.sf.nakeduml.javageneration.hibernate.HibernatePackageAnnotator;
import net.sf.nakeduml.javageneration.jbpm5.Jbpm5EnvironmentBuilder;
import net.sf.nakeduml.javageneration.jbpm5.ProcessStepResolverImplementor;
import net.sf.nakeduml.javageneration.testgeneration.ArquillianTestJavaGenerator;
import net.sf.nakeduml.metamodel.core.internal.StereotypeNames;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.nakeduml.bootstrap.IntegratedArquillianBootstrapStep;
import org.nakeduml.bootstrap.WarBootstrapStep;
import org.nakeduml.eclipse.ApplyProfileAction;
import org.nakeduml.generation.features.BpmUsingJbpm5;
import org.nakeduml.generation.features.ExtendedCompositionSemantics;
import org.nakeduml.generation.features.OclExpressionExecution;
import org.nakeduml.generation.features.PersistenceUsingHibernate;
import org.nakeduml.uml2uim.FormSynchronizer;
import org.nakeduml.uml2uim.ModelCopyStep;

public class StarterCodeGenerator extends MavenProjectCodeGenerator{
	public enum OutputRootId{
		GENERATOR_ROOT,
		GENERATOR_SRC
	}
	public StarterCodeGenerator(ResourceSet resourceSet,NakedUmlConfig cfg,File modelDirectory){
		super(resourceSet, cfg, modelDirectory);
	}
	@Override
	protected NakedUmlConfig prepareConfig() throws IOException{
		NakedUmlConfig cfg = super.prepareConfig();
		cfg.mapOutputRoot(OutputRootId.GENERATOR_SRC, true, "-generator", "src/main/java");
		if(cfg.getOutputRoot().exists()){
			for(File file:cfg.getOutputRoot().listFiles()){
				if(file.getName().equals(".project") || file.getName().equals(".classpath")){
					file.delete();
				}
			}
		}
		cfg.mapOutputRoot(OutputRootId.GENERATOR_SRC, true, "-generator", "src/main/java");
		return cfg;
	}
	@Override
	protected EmfWorkspace loadSingleModel(File modelFile) throws Exception{
		EmfWorkspace result = super.loadSingleModel(modelFile);
		setMappedImplementationPackage(result);
		result.setUriResolver(getUriResolver());
		return result;
	}
	private UriResolver getUriResolver(){
		return new UriResolver(){
			@Override
			public File resolve(URI uri){
				String platformString2 = uri.toPlatformString(true);
				try{
					IFile diFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString2));
					return diFile.getLocation().toFile();
				}catch(IllegalArgumentException a){
					try{
						IFolder diFile = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(platformString2));
						return diFile.getLocation().toFile();
					}catch(IllegalArgumentException a2){
						IProject diFile = ResourcesPlugin.getWorkspace().getRoot().getProject(platformString2);
						return diFile.getLocation().toFile();
					}
				}
			}
		};
	}
	protected void setMappedImplementationPackage(EmfWorkspace result){
		for(Package pkg:result.getPrimaryModels()){
			if(pkg instanceof Model){
				Model model = (Model) pkg;
				Profile pf = ApplyProfileAction.applyNakedUmlProfile(model);
				Stereotype st = pf.getOwnedStereotype(StereotypeNames.PACKAGE);
				if(!model.isStereotypeApplied(st)){
					model.applyStereotype(st);
				}
				model.setValue(st, "mappedImplementationPackage", super.cfg.getMavenGroupId() + "." + model.getName().toLowerCase());
				try{
					model.eResource().save(new HashMap());
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	protected EmfWorkspace loadDirectory() throws IOException{
		EmfWorkspace result = super.loadDirectory();
		result.setUriResolver(getUriResolver());
		setMappedImplementationPackage(result);
		return result;
	}
	@Override
	protected Set<Class<? extends TransformationStep>> getSteps(){
		Set<Class<? extends TransformationStep>> basicSteps = getBasicSteps();
		basicSteps.add(EclipseProjectGenerationStep.class);
		return basicSteps;
	}
	public static Set<Class<? extends TransformationStep>> getBasicSteps(){
		return toSet(PersistenceUsingHibernate.class, ExtendedCompositionSemantics.class, OclExpressionExecution.class, StereotypeApplicationExtractor.class,
				BpmUsingJbpm5.class, ArquillianTestJavaGenerator.class, FormSynchronizer.class, ProcessStepResolverImplementor.class);
	}
	@Override
	protected Set<Class<? extends TransformationStep>> getIntegrationSteps(){
		Set<Class<? extends TransformationStep>> basicIntegrationSteps = getBasicIntegrationSteps();
		basicIntegrationSteps.add(EclipseProjectGenerationStep.class);
		basicIntegrationSteps.add(GeneratorGenerator.class);
		basicIntegrationSteps.add(GeneratorPomStep.class);
		return basicIntegrationSteps;
	}
	public static Set<Class<? extends TransformationStep>> getBasicIntegrationSteps(){
		return toSet(ModelCopyStep.class, HibernateConfigGenerator.class, HibernatePackageAnnotator.class, Jbpm5EnvironmentBuilder.class,
				ArquillianTestJavaGenerator.class, IntegratedArquillianBootstrapStep.class, WarBootstrapStep.class);
	}
	public File getOutputRoot(){
		return cfg.getOutputRoot();
	}
}