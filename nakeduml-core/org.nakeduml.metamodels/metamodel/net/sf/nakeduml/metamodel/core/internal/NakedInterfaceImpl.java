package net.sf.nakeduml.metamodel.core.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.nakeduml.metamodel.commonbehaviors.INakedReception;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedInterface;
import net.sf.nakeduml.metamodel.core.INakedProperty;

public class NakedInterfaceImpl extends NakedClassifierImpl implements INakedInterface {
	private Collection<INakedClassifier> implementingClassifiers = new ArrayList<INakedClassifier>();
	private static final long serialVersionUID = 1406494153933781228L;
	public static final String META_CLASS = "interface";
	private Set<INakedReception> ownedReception = new HashSet<INakedReception>();




	@Override
	public void addOwnedElement(INakedElement element) {
		super.addOwnedElement(element);
		if (element instanceof INakedReception) {
			this.ownedReception.add((INakedReception) element);
		}
	}

	public Set<INakedReception> getOwnedReception() {
		return ownedReception;
	}

	@Override
	public String getMetaClass() {
		return META_CLASS;
	}

	@Override
	public boolean getIsAbstract() {
		return true;
	}

	public void addImplementingClassifier(INakedClassifier c) {
		implementingClassifiers.add(c);
	}

	public Collection<INakedClassifier> getImplementingClassifiers() {
		return implementingClassifiers;
	}

	public void removeImplementingClassifier(INakedClassifier implementingClassifier) {
		this.implementingClassifiers.remove(implementingClassifier);
	}

}
