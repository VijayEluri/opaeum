package net.sf.nakeduml.linkage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.nakeduml.feature.InputModel;
import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.feature.PhaseDependency;
import net.sf.nakeduml.feature.TransformationContext;
import net.sf.nakeduml.feature.TransformationPhase;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedElementOwner;
import net.sf.nakeduml.metamodel.visitor.NakedElementOwnerVisitor;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import nl.klasse.octopus.stdlib.internal.types.OclLibraryImpl;

@PhaseDependency()
public class LinkagePhase implements TransformationPhase<AbstractModelElementLinker,INakedElement>{
	public final class ErrorRemover extends NakedElementOwnerVisitor{
		@VisitBefore(matchSubclasses = true)
		public void visitElement(INakedElement e){
			modelWorkspace.getErrorMap().getErrors().remove(e.getId());
		}
	}
	private NakedUmlConfig config;
	@InputModel
	private INakedModelWorkspace modelWorkspace;
	private List<AbstractModelElementLinker> linkers;
	@Override
	public Collection<?> processElements(TransformationContext context,Collection<INakedElement> elements){
		for(INakedElement element:elements){
			new ErrorRemover().visitRecursively(element);
		}
		Collection<INakedElement> affectedElements = new HashSet<INakedElement>(elements);
		for(AbstractModelElementLinker d:linkers){
			for(INakedElement element:filterChildrenOut(affectedElements)){
				modelWorkspace.getOclEngine().setOclLibrary(new OclLibraryImpl());
				d.visitRecursively((INakedElementOwner) element);
			}
			affectedElements.addAll(d.getAffectedElements());
		}
		return affectedElements;
	}
	@Override
	public void execute(TransformationContext context){
		for(AbstractModelElementLinker d:linkers){
			d.startVisiting(modelWorkspace);
		}
	}
	@Override
	public void initialize(NakedUmlConfig config,List<AbstractModelElementLinker> features){
		this.config = config;
		this.linkers = features;
	}
	public void initializeSteps(){
		for(AbstractModelElementLinker d:linkers){
			d.initialize(modelWorkspace, this.config);
		}
	}
	@Override
	public Collection<AbstractModelElementLinker> getSteps(){
		return this.linkers;
	}
	public static Set<INakedElement> filterChildrenOut(Collection<INakedElement> elements){
		Set<INakedElement> elementsToProcess = new HashSet<INakedElement>();
		outer:for(INakedElement element1:elements){
			for(INakedElement element2:elements){
				if(contains(element2, element1)){
					// skip - parent will be processed
					continue outer;
				}
			}
			elementsToProcess.add((INakedElement) element1);
		}
		return elementsToProcess;
	}
	static boolean contains(INakedElement arg0,INakedElement arg1){
		if(!(arg1.getOwnerElement() instanceof INakedElement)){
			return false;
		}else if(arg0.equals(arg1.getOwnerElement())){
			return true;
		}else{
			return contains(arg0, (INakedElement) arg1.getOwnerElement());
		}
	}
}
