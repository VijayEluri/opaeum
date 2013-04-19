/**
 *  Copyright (c) 2011 Atos.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos - Initial API and implementation
 * 
 */
package org.opaeum.uimodeler.modelexplorer.queries;

import java.util.Collection;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.facet.infra.query.core.exception.ModelQueryExecutionException;
import org.eclipse.emf.facet.infra.query.core.java.IJavaModelQuery;
import org.eclipse.emf.facet.infra.query.core.java.ParameterValueList;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.views.modelexplorer.NavigatorUtils;
import org.eclipse.papyrus.views.modelexplorer.queries.AbstractEditorContainerQuery;
import org.opaeum.uim.UserInteractionElement;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Get the collection of all contained diagrams
 * FIXME : delete this class when the bug EMF-Facet 365744 will be corrected!
 * 
 * @Deprecated : use oep.infra.gmfdiag.modelexplorer#queries.GetContainedDiagrams
 */
@Deprecated
public class GetContainedUimDiagrams extends AbstractEditorContainerQuery implements IJavaModelQuery<UserInteractionElement, Collection<org.eclipse.gmf.runtime.notation.Diagram>> {

	public Collection<org.eclipse.gmf.runtime.notation.Diagram> evaluate(final UserInteractionElement context, final ParameterValueList parameterValues) throws ModelQueryExecutionException {
		Predicate<EStructuralFeature.Setting> p = new Predicate<EStructuralFeature.Setting>() {
			public boolean apply(EStructuralFeature.Setting arg0) {
				return arg0.getEObject() instanceof Diagram ;
			}
		};


		Function<EStructuralFeature.Setting, Diagram> f = new Function<EStructuralFeature.Setting, Diagram>() {

			public Diagram apply(EStructuralFeature.Setting arg0) {
				return (Diagram)arg0.getEObject();
			}
			
		};
		return NavigatorUtils.findFilterAndApply(context, p, f);
	}
}
