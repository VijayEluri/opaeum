package org.opaeum.linkage;

import org.eclipse.uml2.uml.INakedProperty;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitAfter;
import org.opaeum.metamodel.core.internal.NakedOperationImpl;
import org.opaeum.name.NameConverter;

@StepDependency(phase = LinkagePhase.class,after = {MappedTypeLinker.class,PinLinker.class,ReferenceResolver.class,TypeResolver.class},requires = {
		MappedTypeLinker.class,PinLinker.class,ReferenceResolver.class,TypeResolver.class},before = NakedParsedOclStringResolver.class)
public class QualifierLogicCalculator extends AbstractModelElementLinker{
	@VisitAfter(matchSubclasses = true)
	public void visitClass(INakedProperty p){
		if(p.getOtherEnd() != null && p.getOtherEnd().getQualifierNames().length > 0){
			StringBuilder ocl = new StringBuilder("self.").append(p.getName()).append(".").append(p.getOtherEnd().getName())
					.append("->forAll(p|(");
			boolean first = true;
			for(INakedProperty q:p.getOtherEnd().getQualifiers()){
				if(!first){
					ocl.append(" and ");
				}
				first = false;
				ocl.append("p.");
				ocl.append(q.getName());
				ocl.append("=self.");
				ocl.append(q.getName());
			}
			ocl.append(") implies p=self)");
			String constraintName = "uniqueIn" + NameConverter.capitalize(p.getName());
			ConstraintUtil.buildArtificialConstraint(p, ocl.toString(), constraintName);
		}
	}
}