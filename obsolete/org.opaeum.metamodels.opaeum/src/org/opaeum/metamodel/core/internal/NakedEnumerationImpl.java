package org.opaeum.metamodel.core.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import nl.klasse.octopus.model.IEnumLiteral;
import nl.klasse.octopus.model.IModelElement;

import org.eclipse.uml2.uml.INakedElement;
import org.eclipse.uml2.uml.INakedEnumeration;
import org.eclipse.uml2.uml.INakedEnumerationLiteral;

public class NakedEnumerationImpl extends NakedClassifierImpl implements INakedEnumeration{
	private static final long serialVersionUID = 2719597562476111234L;
	public static final String META_CLASS = "enumeration";
	List<INakedEnumerationLiteral> literals = new ArrayList<INakedEnumerationLiteral>();
	public NakedEnumerationImpl(){
	}
	@Override
	public void addOwnedElement(INakedElement element){
		super.addOwnedElement(element);
		if(element instanceof NakedEnumerationLiteralImpl){
			this.literals.add((INakedEnumerationLiteral) element);
		}
	}
	@Override
	public Collection<INakedElement> removeOwnedElement(INakedElement element,boolean recursively){
		Collection<INakedElement> result = super.removeOwnedElement(element, recursively);
		if(element instanceof INakedEnumerationLiteral){
			this.literals.remove((INakedEnumerationLiteral) element);
		}
		return result;
	}
	@Override
	protected boolean isNamedMember(INakedElement e){
		return super.isNamedMember(e) || e instanceof INakedEnumerationLiteral;
	};
	@Override
	public String getMetaClass(){
		return META_CLASS;
	}
	public IModelElement lookupLiteral(String name){
		Iterator<INakedEnumerationLiteral> iter = this.literals.iterator();
		while(iter.hasNext()){
			INakedEnumerationLiteral l = iter.next();
			if(l.getName().equals(name)){
				return l;
			}
		}
		return null;
	}
	@SuppressWarnings({
			"unchecked","rawtypes"
	})
	public List<IEnumLiteral> getLiterals(){
		return (List) getOwnedLiterals();
	}
	@Override
	public List<INakedEnumerationLiteral> getOwnedLiterals(){
		Collections.sort(literals, new Comparator<INakedEnumerationLiteral>(){
			@Override
			public int compare(INakedEnumerationLiteral o1,INakedEnumerationLiteral o2){
				return o1.getOrdinal() - o2.getOrdinal();
			}
		});
		return literals;
	}
}