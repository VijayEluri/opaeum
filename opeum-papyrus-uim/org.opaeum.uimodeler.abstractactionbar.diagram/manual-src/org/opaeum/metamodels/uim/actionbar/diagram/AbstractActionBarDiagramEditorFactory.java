package org.opaeum.metamodels.uim.actionbar.diagram;

import org.eclipse.papyrus.infra.gmfdiag.common.GmfEditorFactory;
import org.opaeum.uimodeler.abstractactionbar.diagram.edit.parts.AbstractEditorEditPart;

public class AbstractActionBarDiagramEditorFactory extends GmfEditorFactory {

	public AbstractActionBarDiagramEditorFactory() {
		super(AbstractActionBarDiagramForMultiEditor.class, AbstractEditorEditPart.MODEL_ID);

	}

}