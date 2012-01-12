package org.opaeum.metamodel.core.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import nl.klasse.octopus.expressions.internal.types.PathName;
import nl.klasse.octopus.model.IAssociation;
import nl.klasse.octopus.model.IClassifier;
import nl.klasse.octopus.model.IImportedElement;
import nl.klasse.octopus.model.IInterface;
import nl.klasse.octopus.model.IModelElement;
import nl.klasse.octopus.model.INameSpace;
import nl.klasse.octopus.model.IOperation;
import nl.klasse.octopus.model.IPackage;
import nl.klasse.octopus.model.IPackageableElement;
import nl.klasse.octopus.modelVisitors.IPackageVisitor;

import org.opaeum.metamodel.activities.INakedActivity;
import org.opaeum.metamodel.commonbehaviors.INakedBehavioredClassifier;
import org.opaeum.metamodel.core.INakedAssociation;
import org.opaeum.metamodel.core.INakedClassifier;
import org.opaeum.metamodel.core.INakedElement;
import org.opaeum.metamodel.core.INakedGeneralization;
import org.opaeum.metamodel.core.INakedInterface;
import org.opaeum.metamodel.core.INakedInterfaceRealization;
import org.opaeum.metamodel.core.INakedNameSpace;
import org.opaeum.metamodel.core.INakedPackage;

public class NakedNameSpaceImpl extends NakedPackageableElementImpl implements INakedNameSpace{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2968609750473700819L;
	private Collection<IImportedElement> imports = new ArrayList<IImportedElement>();
	protected Collection<INakedClassifier> nestedClassifiers = new HashSet<INakedClassifier>();
	private Collection<INakedAssociation> nestedAssociations = new HashSet<INakedAssociation>();
	private Collection<INakedInterface> ownedInterfaces = new HashSet<INakedInterface>();
	public final void accept(IPackageVisitor v){
		throw new RuntimeException("Octopus style visiting not supported!");
	}
	@Override
	public void addOwnedElement(INakedElement element){
		super.addOwnedElement(element);
		if(element instanceof INakedAssociation){
			INakedAssociation b = (INakedAssociation) element;
			this.nestedAssociations.add(b);
		}else if(element instanceof INakedInterface){
			INakedInterface intf = (INakedInterface) element;
			this.ownedInterfaces.add(intf);
		}else if(element instanceof IImportedElement){
			synchronized(imports){// Weird concurrency issue
				this.imports.add((IImportedElement) element);
			}
		}
		if(element instanceof INakedClassifier){
			this.nestedClassifiers.add((INakedClassifier) element);
			if(getRootObject() != null){
				// may be deleting
				getRootObject().addDirectlyAccessibleElement(element);
			}
		}
	}
	public void removeOwnedElement(INakedElement element,boolean recursively){
		super.removeOwnedElement(element, recursively);
		if(element instanceof INakedAssociation){
			INakedAssociation b = (INakedAssociation) element;
			this.nestedAssociations.remove(b);
		}else if(element instanceof INakedInterface){
			INakedInterface intf = (INakedInterface) element;
			this.ownedInterfaces.remove(intf);
		}else if(element instanceof IImportedElement){
			this.imports.remove(element);
		}
		if(element instanceof INakedBehavioredClassifier){
			this.nestedClassifiers.remove(element);
			INakedBehavioredClassifier cls = (INakedBehavioredClassifier) element;
			for(INakedInterfaceRealization ir:cls.getInterfaceRealizations()){
				ir.getContract().removeImplementingClassifier(cls);
			}
			for(INakedGeneralization g:cls.getNakedGeneralizations()){
				g.getGeneral().removeSubClass(cls);
			}
		}
	}
	public Collection<INakedClassifier> getNestedClassifiers(){
		return nestedClassifiers;
	}
	public Collection<INakedInterface> getOwnedInterfaces(){
		return ownedInterfaces;
	}
	public Collection<INakedAssociation> getNestedAssociations(){
		return nestedAssociations;
	}
	public INakedNameSpace getParent(){
		if(getOwnerElement() instanceof INakedNameSpace){
			return (INakedNameSpace) getOwnerElement();
		}else{
			return null;
		}
	}
	public Collection<IAssociation> getAssociations(){
		return new ArrayList<IAssociation>(getNestedAssociations());
	}
	public Collection<IClassifier> getClassifiers(){
		return new ArrayList<IClassifier>(getNestedClassifiers());
	}
	@Deprecated
	public Collection<IInterface> getInterfaces(){
		return new ArrayList<IInterface>(getOwnedInterfaces());
	}
	public IPackage getRoot(){
		return getParent().getNakedRoot();
	}
	public Collection<IPackage> getSubpackages(){
		return Collections.emptySet();
	}
	public String getMetaClass(){
		return "NameSpace";
	}
	public final IModelElement lookup(PathName path){
		if(path == null)
			return null;
		if(path.isSingleName()){
			return lookupLocal(path.getLast());
		}else{
			IModelElement first = lookupLocal(path.getFirst());
			if(first != null){
				if(first instanceof INameSpace){
					return ((INameSpace) first).lookup(path.getTail());
				}
			}
		}
		return null;
	}
	public Collection<IImportedElement> getImports(){
		return imports;
	}
	public final IOperation lookupOperation(PathName path,List<IClassifier> types){
		if(path == null || path.isSingleName()){
			return null;
		}else{
			IModelElement first = lookupLocal(path.getFirst());
			if(first != null){
				if(first instanceof INameSpace){
					return ((INameSpace) first).lookupOperation(path.getTail(), types);
				}
			}
		}
		return null;
	}
	private IModelElement lookupLocal(String first){
		if(first == null){
			return null;
		}else{
			for(INakedElement e:getOwnedElements()){
				if(isNamedMember(e) && first.equals(e.getName())){
					return e;
				}
			}
			return null;
		}
	}
	/**
	 * Utility method to determine if a specific originalElement participates in the ownedMember relationship with this NameSpace
	 * 
	 * @param e
	 * @return
	 */
	protected boolean isNamedMember(INakedElement e){
		if(e instanceof INakedActivity && ((INakedActivity) e).getActivityKind().isSimpleSynchronousMethod()){
			return false;
		}
		return e instanceof IClassifier || e instanceof INakedPackage || e instanceof IImportedElement || e instanceof IAssociation || e instanceof IPackageableElement;
	}
	@Override
	public synchronized boolean isImported(IClassifier cls){
		// We get weird concurrency issues here
		synchronized(imports){
			for(IImportedElement i:getImports()){
				if(i.getElement() == cls){
					return true;
				}
			}
		}
		return false;
	}
}