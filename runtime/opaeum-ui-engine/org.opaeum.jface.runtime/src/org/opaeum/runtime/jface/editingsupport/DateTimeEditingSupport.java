/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.opaeum.runtime.jface.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.opaeum.runtime.jface.binding.BindingUtil;
import org.opaeum.runtime.jface.entityeditor.EntityEditorInputJface;
import org.opaeum.uim.component.UimField;

public class DateTimeEditingSupport extends AbstractEditingSupport{
	private DateCellEditor cellEditor;
	public DateTimeEditingSupport(Class<?>rowClass, CheckboxTableViewer tableViewer,UimField uimField,BindingUtil bindingUtil,EntityEditorInputJface input){
		super(rowClass, tableViewer, input, bindingUtil, uimField);
	}
	@Override
	protected CellEditor getCellEditor(Object element){
		if(cellEditor == null){
			int style = SWT.None;
			switch(uimField.getControlKind()){
			case DATE_POPUP:
				style = SWT.DATE | SWT.CALENDAR | SWT.DROP_DOWN;
				break;
			case DATE_SCROLLER:
				style = SWT.DATE | SWT.CALENDAR;
				break;
			case DATE_TIME_POPUP:
				style = SWT.DATE | SWT.TIME | SWT.CALENDAR | SWT.DROP_DOWN;
				break;
			}
			cellEditor = new DateCellEditor((Composite) tableViewer.getControl(), style);
			cellEditor.setValidator(getDefaultValidator());
		}
		return cellEditor;
	}
}