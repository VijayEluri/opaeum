package net.sf.nakeduml.validation.namegeneration;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.name.NameWrapper;

/**
 * Regenerates the persistent names of elements irrespective of version or
 * revision. Use with care. Discards previous persistent names
 * 
 * @author abarnard
 * 
 */
@StepDependency(phase = NameGenerationPhase.class, requires = { UmlNameRegenerator.class }, after = { UmlNameRegenerator.class })
public class PersistentNameRegenerator extends AbstractPersistentNameGenerator {
	@VisitBefore(matchSubclasses = true)
	public void regenerateName(INakedElement nakedElement) {
		NameWrapper pname = generateSqlName(nakedElement);
		nakedElement.getMappingInfo().setPersistentName(pname);
		if(nakedElement.getMappingInfo().requiresPersistentRename()){
			getAffectedElements().add(nakedElement);
		}
	}
}
