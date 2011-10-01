package org.opeum.metamodel.core.internal.emulated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.klasse.octopus.expressions.internal.types.PathName;
import nl.klasse.octopus.model.IAssociationClass;
import nl.klasse.octopus.model.IAssociationEnd;
import nl.klasse.octopus.model.IAttribute;
import nl.klasse.octopus.model.IClassifier;
import nl.klasse.octopus.model.IImportedElement;
import nl.klasse.octopus.model.IInterface;
import nl.klasse.octopus.model.IModelElement;
import nl.klasse.octopus.model.IOperation;
import nl.klasse.octopus.model.IPackage;
import nl.klasse.octopus.model.IState;
import nl.klasse.octopus.model.VisibilityKind;
import nl.klasse.octopus.oclengine.IOclContext;

import org.opeum.metamodel.commonbehaviors.INakedBehavior;
import org.opeum.metamodel.commonbehaviors.INakedBehavioredClassifier;
import org.opeum.metamodel.commonbehaviors.INakedReception;
import org.opeum.metamodel.commonbehaviors.INakedSignal;
import org.opeum.metamodel.core.CodeGenerationStrategy;
import org.opeum.metamodel.core.INakedClassifier;
import org.opeum.metamodel.core.INakedConstraint;
import org.opeum.metamodel.core.INakedElement;
import org.opeum.metamodel.core.INakedGeneralization;
import org.opeum.metamodel.core.INakedInterface;
import org.opeum.metamodel.core.INakedInterfaceRealization;
import org.opeum.metamodel.core.INakedMessageStructure;
import org.opeum.metamodel.core.INakedNameSpace;
import org.opeum.metamodel.core.INakedOperation;
import org.opeum.metamodel.core.INakedPackage;
import org.opeum.metamodel.core.INakedPowerType;
import org.opeum.metamodel.core.INakedProperty;
import org.opeum.metamodel.core.internal.NakedClassifierImpl;
import org.opeum.metamodel.core.internal.NakedGeneralizationImpl;
import org.opeum.metamodel.core.internal.NakedInterfaceRealizationImpl;

public abstract class MessageStructureImpl extends EmulatingElement implements INakedMessageStructure,INakedBehavioredClassifier{
	private static final long serialVersionUID = 1445741729804979053L;
	protected INakedElement element;
	INakedClassifier owner;
	private Collection<INakedInterfaceRealization> interfaceRealizations = new HashSet<INakedInterfaceRealization>();
	private List<INakedGeneralization> generalizations = new ArrayList<INakedGeneralization>();
	protected MessageStructureImpl(INakedClassifier owner,INakedElement element){
		super(element);
		this.element = element;
		this.owner = owner;
	}
	public abstract List<INakedProperty> getOwnedAttributes();
	public void addInterface(INakedInterface in){
		NakedInterfaceRealizationImpl t = new NakedInterfaceRealizationImpl();
		t.setOwnerElement(this);
		t.setContract(in);
		t.setMappingInfo(in.getMappingInfo().getCopy());
		t.getMappingInfo().setIdInModel(getId() + in.getId());
		t.initialize(getId() + in.getId(), "artificalRealization", false);
		addInterfaceRealization(t);
	}
	protected void addSuperclass(INakedClassifier in){
		NakedGeneralizationImpl t = new NakedGeneralizationImpl();
		t.setOwnerElement(this);
		t.setGeneral(in);
		t.setMappingInfo(in.getMappingInfo().getCopy());
		t.getMappingInfo().setIdInModel(getId() + in.getId());
		t.initialize(getId() + in.getId(), "artificalGeneralization", false);
		generalizations.add(t);
	}
	public List<IClassifier> getGeneralizations(){
		List<IClassifier> results = new ArrayList<IClassifier>();
		for(INakedGeneralization g:generalizations){
			results.add(g.getGeneral());
		}
		return results;
	}
	public INakedClassifier getSupertype(){
		return this.generalizations.isEmpty() ? null : this.generalizations.get(0).getGeneral();
	}
	@Override
	public INakedClassifier getNestingClassifier(){
		return owner;
	}
	// TODO make configurable
	public CodeGenerationStrategy getCodeGenerationStrategy(){
		return CodeGenerationStrategy.ALL;
	}
	@Override
	public String getName(){
		return element.getName();// Don't capp unless we override the
									// qualifiedJavaName too
	}
	public void addOclDefAttribute(IAttribute attr){
	}
	public void addOclDefOperation(IOperation oper){
	}
	public boolean conformsTo(IClassifier c){
		return c.equals(this);
	}
	public IAssociationClass findAssociationClass(String name){
		return owner.findAssociationClass(name);
	}
	public IAssociationEnd findAssociationEnd(String assName){
		return owner.findAssociationEnd(assName);
	}
	public IAttribute findAttribute(String attName){
		for(INakedProperty a:getOwnedAttributes()){
			if(a.getName().equals(attName)){
				return a;
			}
		}
		return owner.findAttribute(attName);
	}
	public IAttribute findClassAttribute(String attName){
		return owner.findClassAttribute(attName);
	}
	public IOperation findClassOperation(String opName,List<IClassifier> parameters){
		return owner.findClassOperation(opName, parameters);
	}
	public IClassifier findCommonSuperType(IClassifier otherType){
		return null;
	}
	public IOperation findOperation(String opName,List<IClassifier> parameters){
		return owner.findOperation(opName, parameters);
	}
	public IState findState(PathName path){
		return owner.findState(path);
	}
	public List<IAttribute> getAllAttributes(){
		return getAttributes();
	}
	public List<IAssociationEnd> getAllNavigations(){
		return Collections.emptyList();
	}
	public List<IAttribute> getAttributes(){
		return new ArrayList<IAttribute>(getOwnedAttributes());
	}
	public List<IAttribute> getClassAttributes(){
		return Collections.emptyList();
	}
	public List<IOperation> getClassOperations(){
		return Collections.emptyList();
	}
	public List<IInterface> getInterfaces(){
		List<IInterface> result = new ArrayList<IInterface>();
		for(INakedInterfaceRealization r:this.interfaceRealizations){
			result.add(r.getContract());
		}
		return result;
	}
	public boolean getIsAbstract(){
		return false;
	}
	public List<IAssociationEnd> getNavigations(){
		return Collections.emptyList();
	}
	public List<IOperation> getOperations(){
		return Collections.emptyList();
	}
	public List<IState> getStates(){
		return owner.getStates();
	}
	public String getStereotype(){
		return null;
	}
	public Collection<IClassifier> getSubClasses(){
		return Collections.emptySet();
	}
	public boolean isCollectionKind(){
		return false;
	}
	public void removeOclDefAttribute(IAttribute attr){
	}
	public void removeOclDefOperation(IOperation oper){
	}
	public void setIsAbstract(boolean isAbstract){
	}
	public void setVisibility(VisibilityKind public1){
	}
	public VisibilityKind getVisibility(){
		return VisibilityKind.PUBLIC;
	}
	public IModelElement lookup(PathName path){
		return owner.lookup(path);
	}
	public IOperation lookupOperation(PathName path,List<IClassifier> types){
		return owner.lookupOperation(path, types);
	}
	@SuppressWarnings("deprecation")
	@Override
	public PathName getPathName(){
		PathName p = owner.getPathName();
		p.addString(getName());
		return p;
	}
	public void addInterfaceRealization(INakedInterfaceRealization in){
		interfaceRealizations.add(in);
	}
	public void addInvariant(IOclContext string){
	}
	public void addSubClass(INakedClassifier c){
	}
	public INakedProperty findEffectiveAttribute(String name){
		return null;
	}
	public List<INakedOperation> getEffectiveOperations(){
		ArrayList<INakedOperation> result = new ArrayList<INakedOperation>();
		for(INakedInterfaceRealization ir:getInterfaceRealizations()){
			result.addAll(ir.getContract().getEffectiveOperations());
		}
		return result;
	}
	public INakedPackage getAsPackage(){
		return null;
	}
	public List<INakedProperty> getEffectiveAttributes(){
		ArrayList<INakedProperty> result = new ArrayList<INakedProperty>();
		for(INakedInterfaceRealization ir:getInterfaceRealizations()){
			NakedClassifierImpl.addEffectiveAttributes(result, ir.getContract().getEffectiveAttributes());
		}
		for(INakedGeneralization g:getNakedGeneralizations()){
			NakedClassifierImpl.addEffectiveAttributes(result, g.getGeneral().getEffectiveAttributes());
		}
		NakedClassifierImpl.addEffectiveAttributes(result, getOwnedAttributes());
		return result;
	}
	public Collection<IImportedElement> getImports(){
		return Collections.emptySet();
	}
	public String getMappedImplementationType(){
		return null;
	}
	public Collection<INakedClassifier> getNestedClassifiers(){
		return Collections.emptySet();
	}
	public Collection<INakedBehavior> getOwnedBehaviors(){
		return Collections.emptySet();
	}
	public INakedPowerType getPowerType(){
		return null;
	}
	public boolean hasPowerType(){
		return false;
	}
	public boolean hasSubtypes(){
		return false;
	}
	public boolean hasSupertype(){
		return false;
	}
	public boolean isPowerTypeInstance(){
		return false;
	}
	public void removeSubClass(INakedClassifier specific){
	}
	public void setCodeGenerationStrategy(CodeGenerationStrategy none){
	}
	public Collection<INakedGeneralization> getNakedGeneralizations(){
		return this.generalizations;
	}
	public void setMappedImplementationType(String class1){
	}
	public void setPowerType(INakedPowerType impl){
	}
	@Override
	public INakedNameSpace getOwner(){
		return (INakedNameSpace) owner;
	}
	@Override
	public Collection<INakedInterfaceRealization> getInterfaceRealizations(){
		return this.interfaceRealizations;
	}
	@Override
	public Collection<IClassifier> getClassifiers(){
		return Collections.emptySet();
	}
	@Override
	public Collection<IPackage> getSubpackages(){
		return Collections.emptySet();
	}
	@Override
	public void setOwnedRules(List<INakedConstraint> loopResults){
	}
	public INakedNameSpace getParent(){
		return getNameSpace();
	}
	@Override
	public INakedBehavior getClassifierBehavior(){
		return null;
	}
	@Override
	public void setClassifierBehavior(INakedBehavior behavior){
	}
	@Override
	public Collection<INakedReception> getOwnedReceptions(){
		return Collections.emptySet();
	}
	@Override
	public Collection<? extends INakedReception> getEffectiveReceptions(){
		ArrayList<INakedReception> result = new ArrayList<INakedReception>();
		for(INakedInterfaceRealization ir:getInterfaceRealizations()){
			result.addAll(ir.getContract().getEffectiveReceptions());
		}
		return result;
	}
	@Override
	public Collection<? extends INakedBehavior> getEffectiveBehaviors(){
		return Collections.emptySet();
	}
	@Override
	public boolean hasReceptionFor(INakedSignal signal){
		return false;
	}
	@Override
	public Set<INakedProperty> getDirectlyImplementedAttributes(){
		return new HashSet<INakedProperty>( getEffectiveAttributes());
	}
	@Override
	public Set<INakedOperation> getDirectlyImplementedOperations(){
		return new HashSet<INakedOperation>(getEffectiveOperations());
	}
	@Override
	public Collection<? extends INakedReception> getDirectlyImplementedReceptions(){
		return Collections.emptySet();
	}
}