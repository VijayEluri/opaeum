package org.opaeum.eclipse;

import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.opaeum.feature.ISourceFolderStrategy;
import org.opaeum.feature.ITransformationStep;
import org.opaeum.feature.OpaeumConfig;
import org.opaeum.metamodel.workspace.AbstractStrategyFactory;
import org.opaeum.strategies.DateTimeStrategyFactory;
import org.opaeum.strategies.TextStrategyFactory;

public class OpaeumEclipsePlugin extends Plugin implements IRegistryChangeListener{
	public static final String PLUGIN_ID = "org.opaeum.eclipse";
	public static final String TRANSFORMATION_STEP_EXTENSION_POINT_ID = "transformationStep";
	public static final String SOURCE_FOLDER_DEFINITION_STRATEGY_EXTENSION_POINT_ID = "sourceFolderStrategy";
	public static final String MODEL_LIBRARY_EXTENSION_POINT_ID = "modelLibrary";
	private static final String PROFILE_EXTENSION_POINT_ID = "profile";
	private static final String STRATEGY_FACTORY_EXTENSION_POINT_ID = "strategyFactory";
	private static OpaeumEclipsePlugin plugin;
	private ResourceBundle resourceBundle;
	private Set<Class<? extends ITransformationStep>> transformationSteps = new HashSet<Class<? extends ITransformationStep>>();
	private Set<Class<? extends ISourceFolderStrategy>> sourceFolderStrategies = new HashSet<Class<? extends ISourceFolderStrategy>>();
	private Set<Class<? extends AbstractStrategyFactory>> strategyFactories = new HashSet<Class<? extends AbstractStrategyFactory>>();
	private Set<ModelLibrary> modelLibraries = new HashSet<ModelLibrary>();
	private Set<ModelLibrary> profiles = new HashSet<ModelLibrary>();
	public OpaeumEclipsePlugin(){
		super();
		plugin = this;
		try{
			resourceBundle = ResourceBundle.getBundle("org.opaeum.eclipse.plugin.messages");//$NON-NLS-1$
		}catch(MissingResourceException x){
			resourceBundle = null;
		}
		OpaeumConfig.registerClass(DateTimeStrategyFactory.class);
		OpaeumConfig.registerClass(TextStrategyFactory.class);
		IExtensionRegistry r = Platform.getExtensionRegistry();
		registerExtensions(r.getConfigurationElementsFor("org.opaeum.eclipse", TRANSFORMATION_STEP_EXTENSION_POINT_ID), transformationSteps);
		registerExtensions(r.getConfigurationElementsFor("org.opaeum.eclipse", SOURCE_FOLDER_DEFINITION_STRATEGY_EXTENSION_POINT_ID), sourceFolderStrategies);
		registerExtensions(r.getConfigurationElementsFor("org.opaeum.eclipse",STRATEGY_FACTORY_EXTENSION_POINT_ID), strategyFactories);
		addModelLibraries(r.getConfigurationElementsFor("org.opaeum.eclipse", MODEL_LIBRARY_EXTENSION_POINT_ID));
		addProfiles(r.getConfigurationElementsFor("org.opaeum.eclipse", PROFILE_EXTENSION_POINT_ID));
		r.addRegistryChangeListener(this);
	}
	public static void logError(String message,Throwable t){
		getDefault().getLog().log(new Status(IStatus.ERROR, getPluginId(), message, t));
	}
	public static OpaeumEclipsePlugin getDefault(){
		return plugin;
	}
	public static String getResourceString(String key){
		ResourceBundle bundle = OpaeumEclipsePlugin.getDefault().getResourceBundle();
		try{
			return (bundle != null) ? bundle.getString(key) : key;
		}catch(MissingResourceException e){
			return key;
		}
	}
	public ResourceBundle getResourceBundle(){
		return resourceBundle;
	}
	public static String getPluginId(){
		return getDefault().getBundle().getSymbolicName();
	}
	@Override
	public void registryChanged(IRegistryChangeEvent event){
		registerExtensionDeltas(event.getExtensionDeltas("org.opaeum.eclipse", TRANSFORMATION_STEP_EXTENSION_POINT_ID), transformationSteps);
		registerExtensionDeltas(event.getExtensionDeltas("org.opaeum.eclipse", SOURCE_FOLDER_DEFINITION_STRATEGY_EXTENSION_POINT_ID), sourceFolderStrategies);
		registerExtensionDeltas(event.getExtensionDeltas("org.opaeum.eclipse", STRATEGY_FACTORY_EXTENSION_POINT_ID), strategyFactories);
		IExtensionDelta[] extensionDeltas = event.getExtensionDeltas("org.opaeum.eclipse", MODEL_LIBRARY_EXTENSION_POINT_ID);
		for(IExtensionDelta delta:extensionDeltas){
			if(delta.getKind() == IExtensionDelta.ADDED){
				addModelLibraries(delta.getExtension().getConfigurationElements());
			}
		}
		extensionDeltas = event.getExtensionDeltas("org.opaeum.eclipse", PROFILE_EXTENSION_POINT_ID);
		for(IExtensionDelta delta:extensionDeltas){
			if(delta.getKind() == IExtensionDelta.ADDED){
				addProfiles(delta.getExtension().getConfigurationElements());
			}
		}
	}
	public void registerExtensionDeltas(IExtensionDelta[] extensionDeltas,@SuppressWarnings("rawtypes") Set transformationSteps2){
		for(IExtensionDelta delta:extensionDeltas){
			if(delta.getKind() == IExtensionDelta.ADDED){
				registerExtensions(delta.getExtension().getConfigurationElements(), transformationSteps2);
			}
		}
	}
	private void addModelLibraries(IConfigurationElement[] configurationElements){
		for(IConfigurationElement ce:configurationElements){
			this.modelLibraries.add(new ModelLibrary(URI.createURI(ce.getAttribute("uri")), ce.getAttribute("name")));
		}
	}
	private void addProfiles(IConfigurationElement[] configurationElements){
		for(IConfigurationElement ce:configurationElements){
			this.profiles.add(new ModelLibrary(URI.createURI(ce.getAttribute("uri")), ce.getAttribute("name")));
		}
	}
	public Set<Class<? extends ISourceFolderStrategy>> getSourceFolderStrategies(){
		return sourceFolderStrategies;
	}
	@SuppressWarnings({
			"unchecked","rawtypes"
	})
	private void registerExtensions(IConfigurationElement[] configurationElements,Set classes){
		try{
			for(IConfigurationElement ce:configurationElements){
				Class<? extends Object> clz = ce.createExecutableExtension("className").getClass();
				classes.add(clz);
				OpaeumConfig.registerClass(clz);
			}
		}catch(CoreException e){
			e.printStackTrace();
		}
	}
	public Set<Class<? extends ITransformationStep>> getTransformationSteps(){
		return transformationSteps;
	}
	public Set<ModelLibrary> getModelLibraries(){
		return modelLibraries;
	}
	public Set<ModelLibrary> getProfiles(){
		return profiles;
	}
	public static String getId(){
		return PLUGIN_ID;
	}
}