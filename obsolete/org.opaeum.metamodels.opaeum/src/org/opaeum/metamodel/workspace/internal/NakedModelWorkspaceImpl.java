package org.opaeum.metamodel.workspace.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nl.klasse.octopus.oclengine.IOclEngine;
import nl.klasse.octopus.oclengine.internal.OclEngine;

import org.eclipse.uml2.uml.DefaultOpaeumComparator;
import org.eclipse.uml2.uml.INakedClassifier;
import org.eclipse.uml2.uml.INakedElement;
import org.eclipse.uml2.uml.INakedInterface;
import org.eclipse.uml2.uml.INakedModel;
import org.eclipse.uml2.uml.INakedRootObject;
import org.opaeum.feature.MappingInfo;
import org.opaeum.feature.WorkspaceMappingInfo;
import org.opaeum.metamodel.core.internal.NakedElementOwnerImpl;
import org.opaeum.metamodel.validation.ErrorMap;
import org.opaeum.metamodel.workspace.ModelWorkspace;
import org.opaeum.metamodel.workspace.OpaeumLibrary;

public class NakedModelWorkspaceImpl extends NakedElementOwnerImpl implements ModelWorkspace{
	public static final String META_CLASS = "nakedWorkspace";
	private OpaeumLibrary builtInTypes;
	private Map<String,INakedElement> allElementsByModelId = new HashMap<String,INakedElement>();
	private INakedInterface businessRole;
	private WorkspaceMappingInfo modelMappingInfo;
	private Set<INakedRootObject> children = new HashSet<INakedRootObject>();
	private Set<INakedClassifier> rootClassifiers = new HashSet<INakedClassifier>();
	private String name;
	private IOclEngine oclEngine = new OclEngine();
	private ErrorMap validator = new ErrorMap();
	private List<INakedRootObject> generatingRootObjects = new ArrayList<INakedRootObject>();
	private Set<INakedRootObject> primaryRootObjects = new HashSet<INakedRootObject>();
	private String identifier;
	private Map<INakedElement,Set<INakedElement>> dependencies = new HashMap<INakedElement,Set<INakedElement>>();
	private INakedClassifier applicationRoot;
	public NakedModelWorkspaceImpl(){
	}
	public synchronized void markDependency(INakedElement from,INakedElement to){
		Set<INakedElement> set = getDependentElements(to);
		set.add(from);
	}
	@Override
	public Collection<INakedModel> getPrimaryModels(){
		Collection<INakedModel> result=new TreeSet<INakedModel>(new DefaultOpaeumComparator());
		for(INakedRootObject ro:getPrimaryRootObjects()){
			if(ro instanceof INakedModel){
				result.add((INakedModel) ro);
			}
		}
		return result;
	}
	public synchronized Set<INakedElement> getDependentElements(INakedElement to){
		Set<INakedElement> set = this.dependencies.get(to);
		if(set == null){
			set = new HashSet<INakedElement>();
			this.dependencies.put(to, set);
		}
		return set;
	}
	@Override
	public IOclEngine getOclEngine(){
		return this.oclEngine;
	}
	@Override
	public void setWorkspaceMappingInfo(WorkspaceMappingInfo modelMappingInfo){
		this.modelMappingInfo = modelMappingInfo;
	}
	@Override
	public synchronized void putModelElement(INakedElement mw){
		if(this.allElementsByModelId.containsKey(mw.getId())){
			System.out.println("Element " + mw.toString() + " already in workspace");
			// TODO investigate why this happens
			// throw new IllegalStateException("Element " + mw.getName() + " is already in the workspace");
		}
		this.allElementsByModelId.put(mw.getId(), mw);
		MappingInfo vi = this.modelMappingInfo.getMappingInfo(mw.getId(), mw.isStoreMappingInfo());
		mw.setMappingInfo(vi);
		if(mw instanceof INakedRootObject){
			addOwnedElement((INakedRootObject) mw);
		}
	}
	@Override
	public synchronized void removeModelElement(INakedElement mw){
		this.allElementsByModelId.remove(mw.getId());
		for(INakedElement child:mw.getOwnedElements()){
			removeModelElement(child);
		}
		this.modelMappingInfo.removeMappingInfo(mw.getId());
	}
	@Override
	public INakedElement getModelElement(Object id){
		if(id == null){
			return null;
		}else{
			return this.allElementsByModelId.get(id);
		}
	}
	public Collection<INakedElement> getAllElements(){
		return this.allElementsByModelId.values();
	}
	public INakedInterface getBusinessRole(){
		return this.businessRole;
	}
	public void setBusinessRole(INakedInterface rootUserEntity){
		this.businessRole = rootUserEntity;
	}
	public WorkspaceMappingInfo getWorkspaceMappingInfo(){
		return this.modelMappingInfo;
	}
	public void setModelMappingInfo(WorkspaceMappingInfo modelMappingInfo){
		this.modelMappingInfo = modelMappingInfo;
	}
	public MappingInfo getMappingInfo(){
		return this.getWorkspaceMappingInfo().getMappingInfo("replace with name identifying the transformation", false);
	}
	@SuppressWarnings({
			"rawtypes","unchecked"
	})
	public Collection<INakedElement> getOwnedElements(){
		return (Collection) this.children;
	}
	public void setName(String string){
		this.name = string;
		if(name == null)
			throw new IllegalStateException();
	}
	public String getName(){
		return this.name;
	}
	public synchronized void addOwnedElement(INakedElement element){
		if(this.children.contains(element)){
			this.children.remove(element);
		}
		this.children.add((INakedRootObject) element);
	}
	public OpaeumLibrary getOpaeumLibrary(){
		if(this.builtInTypes == null){
			this.builtInTypes = new OpaeumLibrary(this.getOclEngine().getOclLibrary());
		}
		return this.builtInTypes;
	}
	public void setBuiltInTypes(OpaeumLibrary builtInTypes){
		this.builtInTypes = builtInTypes;
	}
	public ErrorMap getErrorMap(){
		return validator;
	}
	@Override
	public Collection<INakedRootObject> getRootObjects(){
		return children;
	}
	@Override
	public Collection<INakedElement> removeOwnedElement(INakedElement element,boolean recursively){
		Collection<INakedElement> result = super.removeOwnedElement(element, recursively);
		this.children.remove(element);
		this.generatingRootObjects.remove(element);
		this.primaryRootObjects.remove(element);
		super.removeOwnedElement(element, recursively);
		removeModelElement(element);
		return result;
	}
	@Override
	public List<INakedRootObject> getGeneratingModelsOrProfiles(){
		return generatingRootObjects;
	}
	@Override
	public void addGeneratingRootObject(INakedRootObject p){
		generatingRootObjects.add(p);
	}
	@Override
	public void clearGeneratingModelOrProfiles(){
		generatingRootObjects.clear();
	}
	@Override
	public String getMetaClass(){
		return "nakedWorkspace";
	}
	@Override
	public boolean isPrimaryModel(INakedRootObject rootObject){
		return primaryRootObjects.contains(rootObject);
	}
	@Override
	public void addPrimaryModel(INakedRootObject rootObject){
		if(rootObject==null){
			throw new IllegalArgumentException();
		}
		primaryRootObjects.add(rootObject);
	}
	public void setIdentifier(String directoryName){
		this.identifier = directoryName;
	}
	public String getIdentifier(){
		return identifier;
	}
	@Override
	public Collection<INakedRootObject> getPrimaryRootObjects(){
		return primaryRootObjects;
	}
	public Set<INakedClassifier> getRootClassifiers(){
		return rootClassifiers;
	}
	public void setRootClassifiers(Set<INakedClassifier> rootClassifiers){
		this.rootClassifiers = rootClassifiers;
	}
	@Override
	public void clearRootClassifiers(){
		rootClassifiers.clear();
	}
	@Override
	public void addRootClassifier(INakedClassifier cp){
		rootClassifiers.add(cp);
	}
	public void release(){
		for(INakedElement e:new HashSet<INakedElement>(getOwnedElements())){
			this.removeOwnedElement(e, true);
		}
		this.allElementsByModelId.clear();
		this.builtInTypes=null;
		this.businessRole=null;
		this.children.clear();
		this.dependencies.clear();
		this.generatingRootObjects.clear();
		this.ownedElements.clear();
		this.rootClassifiers.clear();
		this.validator.clear();
		this.primaryRootObjects.clear();
		

	}
	@Override
	public INakedClassifier getApplicationRoot(){
		return this.applicationRoot;
	}
	@Override
	public void setApplicationRoot(INakedClassifier root){
		this.applicationRoot=root;
		
	}
}