package org.opeum.metamodel.bpm.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.klasse.octopus.model.IPackage;
import nl.klasse.octopus.oclengine.IOclContext;

import org.opeum.metamodel.activities.INakedObjectNode;
import org.opeum.metamodel.bpm.INakedEmbeddedSingleScreenTask;
import org.opeum.metamodel.core.INakedConstraint;
import org.opeum.metamodel.core.INakedProperty;
import org.opeum.metamodel.core.internal.emulated.EmulatedCompositionMessageStructure;
import org.opeum.metamodel.core.internal.emulated.TypedElementPropertyBridge;

public class EmbeddedSingleScreenTaskMessageStructureImpl extends EmulatedCompositionMessageStructure {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1536992747207231274L;
	INakedEmbeddedSingleScreenTask action;

	public INakedEmbeddedSingleScreenTask getAction(){
		return action;
	}

	public EmbeddedSingleScreenTaskMessageStructureImpl(INakedEmbeddedSingleScreenTask action) {
		super(action.getActivity(), action);
		this.action = action;
		
	}

	@Override
	public List<INakedProperty> getOwnedAttributes() {
		if (attributes == null) {
			attributes = new ArrayList<INakedProperty>();
			for (INakedObjectNode p : action.getPins()) {
				attributes.add(new TypedElementPropertyBridge(this, p,false));
			}
			attributes.add(getEndToComposite());
		}
		return attributes;
	}

	public List<IOclContext> getDefinitions() {
		return Collections.emptyList();
	}

	public List<INakedConstraint> getOwnedRules() {
		return Collections.emptyList();
	}


	public boolean isPersistent() {
		return true;
	}

	public INakedEmbeddedSingleScreenTask getOpaqueAction() {
		return action;
	}

	@Override
	public IPackage getRoot() {
		return getOpaqueAction().getActivity().getNakedRoot();
	}



}