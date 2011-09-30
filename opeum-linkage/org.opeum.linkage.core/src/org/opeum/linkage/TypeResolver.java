package org.opeum.linkage;

import nl.klasse.octopus.model.CollectionMetaType;
import nl.klasse.octopus.model.IClassifier;
import nl.klasse.octopus.stdlib.IOclLibrary;

import org.opeum.feature.StepDependency;
import org.opeum.feature.visit.VisitBefore;
import org.opeum.metamodel.activities.INakedValuePin;
import org.opeum.metamodel.core.INakedClassifier;
import org.opeum.metamodel.core.INakedInstanceSpecification;
import org.opeum.metamodel.core.INakedPrimitiveType;
import org.opeum.metamodel.core.INakedProperty;
import org.opeum.metamodel.core.INakedTypedElement;

/**
 */
@StepDependency(phase = LinkagePhase.class, requires = { PinLinker.class ,MappedTypeLinker.class }, after = { PinLinker.class ,MappedTypeLinker.class})
public class TypeResolver extends AbstractModelElementLinker {
	@VisitBefore
	public void resolveClassifier(INakedInstanceSpecification is) {
		if (is.getClassifier() == null) {
			// For Keywords only
			is.setClassifier(getBuiltInTypes().getStringType());
		}
	}

	@VisitBefore(matchSubclasses = true)
	public void resolveType(INakedTypedElement aw) {
		if (!(aw instanceof INakedValuePin && aw.getNakedBaseType() == null)) {
			//VAlue pins will have their basetype calculated from the ocl
			INakedClassifier baseType = aw.getNakedBaseType();
			if (baseType == null) {
				(aw).setBaseType(this.workspace.getOpeumLibrary().getDefaultType());
				baseType = this.workspace.getOpeumLibrary().getDefaultType();
			}
			IClassifier type = baseType;
			if (baseType instanceof INakedPrimitiveType) {
				INakedPrimitiveType pt = ((INakedPrimitiveType) baseType);
				if (pt.getOclType() != null) {
					// typically from non-uml metamodels
					type = pt.getOclType();
				}
			}
			boolean singleObject = aw.getNakedMultiplicity().isSingleObject();
			if (aw instanceof INakedProperty) {
				singleObject = singleObject && ((INakedProperty) aw).getQualifierNames().length == 0;
			}
			if (singleObject) {
				aw.setType(type);
			} else {
				IOclLibrary lib = this.workspace.getOclEngine().getOclLibrary();
				resolveCollection(aw, type, lib);
			}
		}
	}

	//TODO move to utility class
	public static void resolveCollection(INakedTypedElement aw, IClassifier type, IOclLibrary lib) {
		if (!aw.isOrdered() && aw.isUnique()) {
			aw.setType(lib.lookupCollectionType(CollectionMetaType.SET, type));
		} else if (aw.isOrdered() && aw.isUnique()) {
			aw.setType(lib.lookupCollectionType(CollectionMetaType.SEQUENCE, type));
		} else if (!aw.isOrdered() && !aw.isUnique()) {
			aw.setType(lib.lookupCollectionType(CollectionMetaType.BAG, type));
		} else if (aw.isOrdered() && !aw.isUnique()) {
			aw.setType(lib.lookupCollectionType(CollectionMetaType.SEQUENCE, type));
		}
	}
}
