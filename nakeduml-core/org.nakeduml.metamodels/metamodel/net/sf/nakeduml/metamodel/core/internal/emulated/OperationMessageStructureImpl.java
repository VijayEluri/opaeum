package net.sf.nakeduml.metamodel.core.internal.emulated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.nakeduml.metamodel.core.ICompositionParticipant;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedConstraint;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedOperation;
import net.sf.nakeduml.metamodel.core.INakedParameter;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.core.INakedTypedElement;
import net.sf.nakeduml.metamodel.core.internal.ArtificialProperty;
import nl.klasse.octopus.model.IClass;
import nl.klasse.octopus.oclengine.IOclContext;
import nl.klasse.octopus.stdlib.IOclLibrary;

public class OperationMessageStructureImpl extends EmulatedCompositionMessageStructure implements IClass{
	List<INakedProperty> attributes;
	private INakedOperation oper;
	public OperationMessageStructureImpl(INakedClassifier owner,INakedOperation oper,IOclLibrary lib){
		super(owner, oper,lib);
		this.oper = oper;
	}
	public OperationMessageStructureImpl(INakedOperation nop,IOclLibrary lib){
		this(nop.getOwner(), nop, lib);
	}
	@Override
	public List<INakedProperty> getOwnedAttributes(){
		if(attributes == null){
			attributes = new ArrayList<INakedProperty>();
			for(INakedElement p:oper.getOwnedElements()){
				if(p instanceof INakedParameter){
					attributes.add(new TypedElementPropertyBridge(this, (INakedTypedElement) p));
				}
			}
		}
		return attributes;
	}
	public List<IOclContext> getDefinitions(){
		return Collections.emptyList();
	}
	public List<INakedConstraint> getOwnedRules(){
		return Collections.emptyList();
	}
	public boolean isPersistent(){
		return oper.isLongRunning();
	}
	public INakedOperation getOperation(){
		return oper;
	}

}
