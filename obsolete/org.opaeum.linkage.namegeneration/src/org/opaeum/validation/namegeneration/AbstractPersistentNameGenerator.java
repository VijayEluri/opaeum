package org.opaeum.validation.namegeneration;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.ComplexStructure;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Helper;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.ValueSpecification;
import org.opaeum.feature.MappingInfo;
import org.opaeum.metamodel.name.NameWrapper;
import org.opaeum.metamodel.name.SingularNameWrapper;
import org.opaeum.name.NameConverter;
public abstract class AbstractPersistentNameGenerator extends AbstractNameGenerator {
	@Override
	protected boolean hasName(Element p){
		return p.getMappingInfo().hasPersistentName();
	}
	protected final String generateQualifiedPersistentName(Element nme) {
		if (nme instanceof State) {
			State state = (State) nme;
			if (state.hasEnclosingState()) {
				return generateQualifiedPersistentName(state.getEnclosingState()) + "/"
						+ state.getMappingInfo().getPersistentName();
			} else {
				String asIs = state.getMappingInfo().getPersistentName().getAsIs();
				return asIs;
			}
		} else if (nme instanceof ActivityNode) {
			ActivityNode node = (ActivityNode) nme;
			if (node.getInStructuredNode() != null) {
				return generateQualifiedPersistentName(node.getInStructuredNode()) + "/"
						+ node.getMappingInfo().getPersistentName();
			} else {
				return node.getMappingInfo().getPersistentName().getAsIs();
			}
		}else{
			MappingInfo mappingInfo2 = nme.getMappingInfo();
			if (nme.getOwnerElement() instanceof Element) {
				Element owner = (Element) nme.getOwnerElement();
				MappingInfo mappingInfo = owner.getMappingInfo();
				NameWrapper persistentName = mappingInfo.getPersistentName();
				return persistentName.getAsIs() + "."
						+ mappingInfo2.getPersistentName().getAsIs();
			} else {
				return mappingInfo2.getPersistentName().getAsIs();
			}
		}
	}
	protected final NameWrapper generateSqlName(Element nme) {

		String generatedName = null;
		ValueSpecification existingSqlName = getTaggedValue(nme, "persistentName", "persistenceType");
		if (existingSqlName != null) {
			generatedName = existingSqlName.stringValue();
		} else if (nme instanceof Association) {
			// Name is generated by UmlNameGenerator
			Association ass = (Association) nme;
			generatedName = NameConverter.toUnderscoreStyle(ass.getName());
		} else if (nme instanceof MultiplicityElement) {
			TypedElement tew = (TypedElement) nme;
			String name = tew.getName();
			if(tew instanceof OutputPin){
				name=name+"On" + NameConverter.capitalize(tew.getOwnerElement().getName());//TO ensure uniqueness of name
			}
			//TODO check if this was necessary
			boolean isCmCompatible=true;
			boolean isPersistentInterface = tew.getNakedBaseType() instanceof Interface && !(tew.getNakedBaseType() instanceof Helper);
			if (tew.getNakedBaseType() instanceof ComplexStructure && !(isCmCompatible && isPersistentInterface)) {
				// foreign key
				// TODO re-evaluate the _id thing
				generatedName = NameConverter.toUnderscoreStyle(name) + "_id";
			} else {
				generatedName = NameConverter.toUnderscoreStyle(name);
			}
		} else if (nme instanceof EnumerationLiteral) {
			EnumerationLiteral nakedLiteral = ((EnumerationLiteral) nme);
			generatedName = nakedLiteral.getName();
		} else {
			// TODO actions within StructuredACtivityNodes
			generatedName = NameConverter.toUnderscoreStyle(nme.getName());
		}
		if(generatedName==null){
			generatedName=nme.getClass().getSimpleName() + nme.getMappingInfo().getOpaeumId();
		}
		return new SingularNameWrapper(generatedName.toLowerCase(), null);
	}
}
