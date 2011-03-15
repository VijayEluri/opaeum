package net.sf.nakeduml.metamodel.commonbehaviors.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.nakeduml.metamodel.commonbehaviors.INakedBehavior;
import net.sf.nakeduml.metamodel.commonbehaviors.INakedBehavioredClassifier;
import net.sf.nakeduml.metamodel.commonbehaviors.INakedReception;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedGeneralization;
import net.sf.nakeduml.metamodel.core.internal.NakedClassifierImpl;
import nl.klasse.octopus.model.IClassifier;

public class NakedBehavioredClassifierImpl extends NakedClassifierImpl implements INakedBehavioredClassifier{
	protected List<INakedBehavior> ownedBehaviors = new ArrayList<INakedBehavior>();
	private Set<INakedReception> ownedReception = new HashSet<INakedReception>();
	@Override
	public Set<INakedReception> getOwnedReception(){
		return ownedReception;
	}
	@Override
	public Collection<IClassifier> getClassifiers() {
		Collection<IClassifier> classifiers = super.getClassifiers();
		classifiers.addAll(getOwnedBehaviors());
		return classifiers;
	}
	@Override
	public void addOwnedElement(INakedElement element){
		super.addOwnedElement(element);
		if(element instanceof INakedBehavior){
			this.ownedBehaviors.add((INakedBehavior) element);
		}
		if(element instanceof INakedReception){
			this.ownedReception.add((INakedReception) element);
		}
	}
	public Collection<INakedBehavior> getOwnedBehaviors(){
		return this.ownedBehaviors;
	}
	public void removeOwnedElement(INakedElement element){
		super.removeOwnedElement(element);
		if(element instanceof INakedBehavior){
			this.ownedBehaviors.remove(element);
		}
	}
	public Collection<INakedBehavior> getEffectiveBehaviors(){
		Collection<INakedBehavior> results = new ArrayList<INakedBehavior>(ownedBehaviors);
		for(INakedGeneralization g:getNakedGeneralizations()){
			if(g.getGeneral() instanceof INakedBehavioredClassifier){
				results.addAll(((INakedBehavioredClassifier) g.getGeneral()).getEffectiveBehaviors());
			}
		}
		return results;
	}
	@Override
	public Collection<? extends INakedReception> getEffectiveReceptions(){
		List<INakedReception> results = new ArrayList<INakedReception>(ownedReception);
		for(INakedGeneralization g:getNakedGeneralizations()){
			if(g.getGeneral() instanceof INakedBehavioredClassifier){
				results.addAll(((INakedBehavioredClassifier) g.getGeneral()).getEffectiveReceptions());
			}
		}
		return results;
	}
}