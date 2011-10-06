package org.opeum.metamodel.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opeum.feature.TransformationContext;
import org.opeum.feature.visit.VisitSpec;
import org.opeum.feature.visit.VisitorAdapter;
import org.opeum.metamodel.core.INakedClassifier;
import org.opeum.metamodel.core.INakedElement;
import org.opeum.metamodel.core.INakedElementOwner;
import org.opeum.metamodel.core.INakedNameSpace;
import org.opeum.metamodel.core.INakedRootObject;
import org.opeum.metamodel.workspace.INakedModelWorkspace;
import nl.klasse.octopus.expressions.internal.types.PathName;

public abstract class NakedElementOwnerVisitor extends VisitorAdapter<INakedElementOwner,INakedModelWorkspace>{
	private ThreadLocal<INakedRootObject> currentRootObject=new ThreadLocal<INakedRootObject>();
	protected TransformationContext transformationContext;
	@Override
	public Collection<? extends INakedElementOwner> getChildren(INakedElementOwner root){
		return root.getOwnedElements();
	}
	// TODO find a better place for this method
	protected PathName getPathNameInModel(INakedClassifier stereotype){
		List<String> names = new ArrayList<String>();
		names.add(stereotype.getName());
		INakedNameSpace p = stereotype.getNameSpace();
		while(p != null){
			names.add(0, p.getName());
			p = p.getNameSpace();
		}
		PathName pn = new PathName();
		for(String s:names){
			pn.addString(s);
		}
		return pn;
	}
	protected void visitParentsRecursively(INakedElement parent){
		if(parent.getOwnerElement() instanceof INakedElement){
			visitParentsRecursively((INakedElement) parent.getOwnerElement());
			for(VisitSpec v:methodInvokers.beforeMethods){
				maybeVisit(parent, v);
			}
		}
	}
	public void visitUpThenDown(INakedElement e){
		visitParentsRecursively(e);
		visitRecursively(e);
	}
	@Override
	public void visitRecursively(INakedElementOwner o){
		setCurrent(o);
		super.visitRecursively(o);
	}
	protected void setCurrent(INakedElementOwner o){
		if(o instanceof INakedRootObject){
			INakedRootObject pkg = (INakedRootObject) o;
			this.setCurrentRootObject(pkg);
		}
	}
	@Override
	public void visitOnly(INakedElementOwner o){
		INakedElementOwner parent = o;
		while(parent instanceof INakedElement){
			if(parent instanceof INakedRootObject){
				setCurrentRootObject((INakedRootObject) parent);
				break;
			}else{
				parent = ((INakedElement) parent).getOwnerElement();
			}
		}
		super.visitOnly(o);
	}
	protected Collection<INakedRootObject> getModelInScope(){
		Set<INakedRootObject> result = new HashSet<INakedRootObject>(getCurrentRootObject().getAllDependencies());
		result.add(getCurrentRootObject());
		return result;
	}
	public final void setTransformationContext(TransformationContext c){
		this.transformationContext = c;
	}
	protected INakedRootObject getCurrentRootObject(){
		return currentRootObject.get();
	}
	protected void setCurrentRootObject(INakedRootObject currentRootObject){
		this.currentRootObject.set(currentRootObject);
	}
}