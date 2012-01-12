package org.opaeum.linkage;

import nl.klasse.octopus.model.OclUsageType;
import nl.klasse.octopus.model.internal.parser.parsetree.ParsedOclString;

import org.opaeum.feature.MappingInfo;
import org.opaeum.metamodel.core.INakedClassifier;
import org.opaeum.metamodel.core.INakedProperty;
import org.opaeum.metamodel.core.internal.NakedConstraintImpl;
import org.opaeum.metamodel.core.internal.NakedValueSpecificationImpl;

public class ConstraintUtil{
	public static NakedConstraintImpl buildArtificialConstraint(INakedProperty p,String ocl,String constraintName){
		NakedConstraintImpl constraint = new NakedConstraintImpl();
		MappingInfo mi = p.getMappingInfo().getCopy();
		constraint.setMappingInfo(mi);
		mi.setIdInModel(mi.getIdInModel() + constraintName);
		constraint.initialize(mi.getIdInModel(), "art", false);
		NakedValueSpecificationImpl vs = new NakedValueSpecificationImpl();
		vs.setOwnerElement(constraint);
		mi = mi.getCopy();
		vs.setMappingInfo(mi);
		mi.setIdInModel(mi.getIdInModel() + constraintName);
		vs.initialize(mi.getIdInModel(), "art", false);
		ParsedOclString parsedOclContext = new ParsedOclString(p.getName() + constraintName, OclUsageType.INV);
		parsedOclContext.setContext(p.getOwner(), p);
		vs.setValue(parsedOclContext);
		constraint.setSpecification(vs);
		vs.setOwnerElement(constraint);
		constraint.setName(constraintName);
		p.getOwner().addOwnedElement(constraint);
		parsedOclContext.setExpressionString(ocl);
		return constraint;
	}
	
	public static NakedConstraintImpl buildArtificialConstraint(INakedClassifier owner, INakedProperty p,String ocl,String constraintName){
		NakedConstraintImpl constraint = new NakedConstraintImpl();
		MappingInfo constraintMappingInfo = p.getMappingInfo().getCopy();
		constraint.setMappingInfo(constraintMappingInfo);
		constraintMappingInfo.setIdInModel(constraintMappingInfo.getIdInModel() + constraintName);
		constraint.initialize(constraintMappingInfo.getIdInModel(), constraintName, false);
		NakedValueSpecificationImpl vs = new NakedValueSpecificationImpl();
		vs.setOwnerElement(constraint);
		MappingInfo vsMappingInfo = constraintMappingInfo.getCopy();
		vs.setMappingInfo(vsMappingInfo);
		vsMappingInfo.setIdInModel(constraintMappingInfo.getIdInModel()+"VS");
		vs.initialize(vsMappingInfo.getIdInModel(), constraintName, false);
		ParsedOclString parsedOclContext = new ParsedOclString(p.getName() + constraintName, OclUsageType.INV);
		parsedOclContext.setContext(owner, p);
		vs.setValue(parsedOclContext);
		constraint.setSpecification(vs);
		vs.setOwnerElement(constraint);
		constraint.setName(constraintName);
		owner.addOwnedElement(constraint);
		parsedOclContext.setExpressionString(ocl);
		return constraint;
	}	
}