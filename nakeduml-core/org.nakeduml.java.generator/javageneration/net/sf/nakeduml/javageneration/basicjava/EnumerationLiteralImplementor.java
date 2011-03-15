package net.sf.nakeduml.javageneration.basicjava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.oclexpressions.ValueSpecificationUtil;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.metamodel.core.INakedEnumeration;
import net.sf.nakeduml.metamodel.core.INakedEnumerationLiteral;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.core.INakedSlot;
import net.sf.nakeduml.metamodel.core.INakedValueSpecification;
import net.sf.nakeduml.metamodel.core.internal.NakedEnumerationLiteralImpl;
import nl.klasse.octopus.model.IAttribute;
import nl.klasse.octopus.model.IEnumLiteral;

import org.nakeduml.java.metamodel.OJClass;
import org.nakeduml.java.metamodel.OJConstructor;
import org.nakeduml.java.metamodel.OJField;
import org.nakeduml.java.metamodel.OJIfStatement;
import org.nakeduml.java.metamodel.OJOperation;
import org.nakeduml.java.metamodel.OJParameter;
import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;
import org.nakeduml.java.metamodel.annotation.OJEnum;
import org.nakeduml.java.metamodel.annotation.OJEnumLiteral;
import org.nakeduml.java.metamodel.generated.OJVisibilityKindGEN;

public class EnumerationLiteralImplementor extends AttributeImplementor {
	@VisitBefore(matchSubclasses = true)
	public void generateExtraConstructor(INakedEnumeration c) {
		OJEnum myClass = (OJEnum) findJavaClass(c);
		if(myClass==null){
			System.out.println();
		}
		OJOperation values = OJUtil.findOperation(myClass, "getValues");
		if (values != null) {
			OJPathName results = new OJPathName("java.util.Set");
			results.addToElementTypes(OJUtil.classifierPathname(c));
			values.setReturnType(results);
			values.getBody().removeAllFromStatements();
			myClass.addToImports("java.util.HashSet");
			values.getBody().addToStatements("return new HashSet<"+c.getName()+">(java.util.Arrays.asList(values()))");
		}
		OJAnnotatedOperation getName = new OJAnnotatedOperation();
		getName.setName("getName");
		getName.setReturnType(new OJPathName("String"));
		getName.getBody().addToStatements("return name()");
		myClass.addToOperations(getName);
		OJConstructor constr = new OJConstructor();
		myClass.addToConstructors(constr);
		constr.setVisibility(OJVisibilityKindGEN.PRIVATE);
		List<? extends INakedProperty> allAttributes = c.getEffectiveAttributes();
		boolean hasDuplicates = hasDuplicates(allAttributes);
		if (!hasDuplicates) {
			for (INakedProperty attr : allAttributes) {
				if (!(attr.isDerived() || attr.isOclDef())) {
					addToConstructor(constr, myClass, attr, c);
				}
			}
			if (!constr.getParameters().isEmpty()) {
				myClass.addToConstructors(constr);
			}
		}
	}

	// @VisitBefore(matchSubclasses = true)
	// public void generateGettersAndSetters(INakedEnumeration c) {
	// OJAnnotatedClass owner = findJavaClass(c);
	// List<? extends INakedProperty> allAttributes =
	// c.getEffectiveAttributes();
	// for (INakedProperty iNakedProperty : allAttributes) {
	// NakedStructuralFeatureMap map = new
	// NakedStructuralFeatureMap(iNakedProperty);
	// OJPathName javaTypePath = map.javaTypePath();
	// buildGetter(owner, map, javaTypePath);
	// }
	// }
	@VisitBefore(matchSubclasses = true)
	public void generateStaticMethods(INakedEnumeration c) {
		OJEnum myClass = (OJEnum) findJavaClass(c);
		// Does lookups on arbitrary string properties
		List<? extends INakedProperty> allAttributes = c.getEffectiveAttributes();
		for (INakedProperty iNakedProperty : allAttributes) {
			if (iNakedProperty.getType().getName().equals("String") && iNakedProperty.getNakedMultiplicity().isOne()) {
				// TODO support for other types??
				OJAnnotatedOperation staticOp = new OJAnnotatedOperation();
				staticOp.setStatic(true);
				staticOp.setName("from"+iNakedProperty.getMappingInfo().getJavaName().getCapped());
				OJPathName path = OJUtil.classifierPathname(c);
				staticOp.setReturnType(path);
				OJParameter ojParameter = new OJParameter();
				ojParameter.setName(iNakedProperty.getName());
				ojParameter.setType(OJUtil.classifierPathname(iNakedProperty.getNakedBaseType()));
				staticOp.addToParameters(ojParameter);
				List<IEnumLiteral> literals = c.getLiterals();
				for (IEnumLiteral iEnumLiteral : literals) {
					OJIfStatement ifSPS = new OJIfStatement();
					NakedEnumerationLiteralImpl nakedLiteral = (NakedEnumerationLiteralImpl) iEnumLiteral;
					List<INakedSlot> slots = nakedLiteral.getSlots();
					for (INakedSlot iNakedSlot : slots) {
						if (iNakedSlot.getDefiningFeature().equals(iNakedProperty)) {
							ifSPS.setCondition(iNakedProperty.getName() + ".equals(" + ValueSpecificationUtil.expressValue(myClass, iNakedSlot.getFirstValue(),true) + ")");
							ifSPS.addToThenPart("return " + iEnumLiteral.getName());
							break;
						}
					}
					staticOp.getBody().addToStatements(ifSPS);
				}
				staticOp.getBody().addToStatements("return null");
				myClass.addToOperations(staticOp);
			}
		}
	}

	private boolean hasDuplicates(List allAttributes) {
		boolean result = false;
		List<String> names = new ArrayList<String>();
		Iterator it = allAttributes.iterator();
		while (it.hasNext()) {
			IAttribute attr = (IAttribute) it.next();
			if (names.contains(attr.getName())) {
				return true;
			} else {
				names.add(attr.getName());
			}
		}
		return result;
	}

	private void addToConstructor(OJConstructor constr, OJClass myClass, INakedProperty feat, INakedEnumeration c) {
		NakedStructuralFeatureMap mapper = OJUtil.buildStructuralFeatureMap(feat);
		OJPathName type = mapper.javaTypePath();
		myClass.addToImports(type);
		String parname = feat.getName();
		constr.addParam(parname, type);
		String setter = mapper.setter();
		// TODO Why is this being set to private???
		// OJUtil.findOperation(myClass,
		// mapper.getter()).setVisibility(OJVisibilityKindGEN.PRIVATE);
		constr.getBody().addToStatements("this." + setter + "(" + parname + ")");
		OJEnum oje = (OJEnum) myClass;
		for (IEnumLiteral l : c.getLiterals()) {
			OJEnumLiteral ojl = oje.findLiteral(l.getName().toUpperCase());
			OJField f = ojl.findAttributeValue(mapper.umlName());
			INakedValueSpecification value = ((INakedEnumerationLiteral) l).getFirstValueFor(feat.getName());
			f.setInitExp(ValueSpecificationUtil.expressValue(constr, value, c, feat.getType()));
		}
	}
}