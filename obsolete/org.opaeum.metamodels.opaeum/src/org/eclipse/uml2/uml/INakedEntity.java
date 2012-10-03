package org.eclipse.uml2.uml;

import java.util.Collection;
import java.util.List;

import nl.klasse.octopus.model.IClass;


public interface INakedEntity extends IClass, INakedBehavioredClassifier, INakedComplexStructure, ICompositionParticipant {

	/**
	 * Returns a list of properties that define the uniqueness constraints on
	 * this structured type. A property is considered to be a uniqueness
	 * constraint when its other end is navigable and has qualifiers and a
	 * qualified multiplicity of one.
	 * 
	 */
	List<INakedProperty> getUniquenessConstraints();
	INakedProperty getPrimaryKeyProperty();
	Collection<INakedProperty> getPrimaryKeyProperties();

}