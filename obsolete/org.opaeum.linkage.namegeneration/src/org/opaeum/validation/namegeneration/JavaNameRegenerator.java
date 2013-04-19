package org.opaeum.validation.namegeneration;

import org.eclipse.uml2.uml.Element;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.metamodel.name.SingularNameWrapper;
import org.opaeum.metamodel.workspace.ModelWorkspace;

@StepDependency(phase = NameGenerationPhase.class,requires = {
	UmlNameRegenerator.class
},after = {
	UmlNameRegenerator.class
})
public class JavaNameRegenerator extends AbstractJavaNameGenerator{
	@VisitBefore(matchSubclasses = true)
	public void updateJavaName(ModelWorkspace nakedElement){
		nakedElement.getMappingInfo().setJavaName(new SingularNameWrapper(nakedElement.getName(),null).getCapped());
		nakedElement.getMappingInfo().setQualifiedJavaName(super.config.getMavenGroupId());
	}
	@VisitBefore(matchSubclasses = true)
	public void updateJavaName(Element nakedElement){

		nakedElement.getMappingInfo().setJavaName(generateJavaName(nakedElement));
		nakedElement.getMappingInfo().setQualifiedJavaName(generateQualifiedJavaName(nakedElement));
		if(nakedElement.getMappingInfo().requiresJavaRename()){
			getAffectedElements().add(nakedElement);
		}
	}
}
