package org.opaeum.validation.core;

import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.linkage.CoreValidationRule;
import org.opaeum.metamodel.commonbehaviors.INakedBehavior;
import org.opaeum.metamodel.commonbehaviors.INakedBehavioredClassifier;
import org.opaeum.metamodel.commonbehaviors.INakedReception;
import org.opaeum.metamodel.core.ICompositionParticipant;
import org.opaeum.metamodel.core.INakedGeneralization;
import org.opaeum.validation.AbstractValidator;
import org.opaeum.validation.ValidationPhase;

@StepDependency(phase = ValidationPhase.class)
public class GeneralizationValidator extends AbstractValidator{
	// TODO find better place
	@VisitBefore
	public void visitReception(INakedReception r){
		if(r.getSignal() == null){
			getErrorMap().putError(r, CoreValidationRule.RECEPTION_REQUIRES_SIGNAL);
		}
	}
	@VisitBefore
	public void visitGeneralization(INakedGeneralization p){
		if(p.getSpecific() != null && p.getGeneral() != null){
			if(!p.getSpecific().getClass().equals(p.getGeneral().getClass())){
				getErrorMap().putError(p.getSpecific(), CoreValidationRule.GENERALIZATION_ONLY_OF_SAME_METATYPE, p.getSpecific().getMetaClass(), p.getGeneral(),
						p.getGeneral().getMetaClass());
			}else if(p.getSpecific() instanceof INakedBehavior){
				INakedBehavior s = (INakedBehavior) p.getSpecific();
				INakedBehavior g = (INakedBehavior) p.getGeneral();
				INakedBehavioredClassifier sContext = s.getContext();
				INakedBehavioredClassifier gContext = g.getContext();
				if(sContext != null && gContext != null && !sContext.conformsTo(gContext)){
					getErrorMap().putError(s, CoreValidationRule.GENERALIZATION_CONTEXTS_CONFORMANCE, g, sContext, gContext);
				}else if(sContext == null && gContext != null){
					getErrorMap().putError(s, CoreValidationRule.GENERALIZATION_CONTEXTS_CONFORMANCE, g, "undefined", gContext);
				}
			}else if(p.getSpecific() instanceof ICompositionParticipant){
				ICompositionParticipant s = (ICompositionParticipant) p.getSpecific();
				ICompositionParticipant g = (ICompositionParticipant) p.getGeneral();
				if(s.getEndToComposite() != null && g.getEndToComposite() != null){
					ICompositionParticipant sContext = (ICompositionParticipant) s.getEndToComposite().getNakedBaseType();
					ICompositionParticipant gContext = (ICompositionParticipant) g.getEndToComposite().getNakedBaseType();
					if(sContext != null && gContext != null && !sContext.conformsTo(gContext)){
						getErrorMap().putError(s, CoreValidationRule.GENERALIZATION_COMPOSITION_CONFORMANCE, g, sContext, gContext);
					}
				}
			}
		}
	}
}