package net.sf.nakeduml.linkage;

import java.util.Collection;
import java.util.HashSet;

import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.feature.ITransformationStep;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedElementOwner;
import net.sf.nakeduml.metamodel.core.INakedRootObject;
import net.sf.nakeduml.metamodel.validation.ErrorMap;
import net.sf.nakeduml.metamodel.visitor.NakedElementOwnerVisitor;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import net.sf.nakeduml.metamodel.workspace.NakedUmlLibrary;

public abstract class AbstractModelElementLinker extends NakedElementOwnerVisitor implements ITransformationStep{
	@Override
	public void visitRecursively(INakedElementOwner o){
		if(o instanceof INakedRootObject && workspace.getGeneratingModelsOrProfiles().contains(o)){
			getErrorMap().enterCodeGeneratingModel();
		}
		super.visitRecursively(o);
		getErrorMap().exitCodeGeneratingModel();
	}
	protected INakedModelWorkspace workspace;
	protected NakedUmlConfig config;
	private Collection<INakedElement> affectedElements;
	public void initialize(INakedModelWorkspace workspace,NakedUmlConfig config){
		affectedElements = new HashSet<INakedElement>();
		this.workspace = workspace;
		this.config = config;
	}
	protected NakedUmlLibrary getBuiltInTypes(){
		return workspace.getNakedUmlLibrary();
	}
	protected ErrorMap getErrorMap(){
		return workspace.getErrorMap();
	}
	public Collection<INakedElement> getAffectedElements(){
		return this.affectedElements;
	}
}
