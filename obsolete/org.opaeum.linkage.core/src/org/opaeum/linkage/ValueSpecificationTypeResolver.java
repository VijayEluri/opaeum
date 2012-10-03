package org.opaeum.linkage;

import org.eclipse.uml2.uml.INakedOperation;
import org.eclipse.uml2.uml.INakedProperty;
import org.eclipse.uml2.uml.INakedSlot;
import org.eclipse.uml2.uml.INakedTypedElement;
import org.eclipse.uml2.uml.INakedValuePin;
import org.eclipse.uml2.uml.INakedValueSpecification;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitAfter;
import org.opaeum.feature.visit.VisitBefore;

@StepDependency(phase = LinkagePhase.class, requires = { TypeResolver.class }, after = { TypeResolver.class })
public class ValueSpecificationTypeResolver extends AbstractModelElementLinker {
	@VisitBefore(matchSubclasses = true)
	public void visitProperty(INakedProperty p) {
		processValue(p.getInitialValue(), p);
	}
	@VisitBefore(matchSubclasses=true)
	public void visitOperation(INakedOperation o){
		if(o.getBodyCondition()!=null && o.getReturnParameter()!=null){
			processValue(o.getBodyCondition().getSpecification(), o.getReturnParameter());
		}
	}

	private void processValue(INakedValueSpecification value, INakedTypedElement te) {
		if (value != null) {
			value.setType(te.getType());
		}
	}

	@VisitAfter(matchSubclasses = true)
	public void visitValuePin(INakedValuePin p) {
		processValue(p.getValue(), p);
	}

	@VisitBefore(matchSubclasses = true)
	public void visitSlot(INakedSlot p) {
		for (INakedValueSpecification s : p.getValues()) {
			if (p.getValues().size() == 1) {
				s.setType(p.getDefiningFeature().getType());
			} else {
				s.setType(p.getDefiningFeature().getNakedBaseType());
			}
		}
	}
}