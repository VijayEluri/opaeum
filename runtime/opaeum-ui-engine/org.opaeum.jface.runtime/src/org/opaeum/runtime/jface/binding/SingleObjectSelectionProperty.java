
package org.opaeum.runtime.jface.binding;

import org.eclipse.jface.databinding.swt.WidgetValueProperty;
import org.eclipse.swt.SWT;
import org.opaeum.runtime.jface.widgets.CSingleObjectChooser;

public class SingleObjectSelectionProperty extends WidgetValueProperty {
	public SingleObjectSelectionProperty() {
		super(SWT.Selection);
	}

	public Object getValueType() {
		return Object.class;
	}

	protected Object doGetValue(Object source) {
		CSingleObjectChooser c=(CSingleObjectChooser) source;
		return c.getSelection();
	}

	protected void doSetValue(Object source, Object value) {
		CSingleObjectChooser c=(CSingleObjectChooser) source;
		c.setSelection(value);
	}
}