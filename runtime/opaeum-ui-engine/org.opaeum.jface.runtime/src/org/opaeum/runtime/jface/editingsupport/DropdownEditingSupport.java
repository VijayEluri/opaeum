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

import javax.persistence.Entity;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.environment.JavaTypedElement;
import org.opaeum.runtime.jface.binding.BindingUtil;
import org.opaeum.runtime.jface.entityeditor.EntityEditorInputJface;
import org.opaeum.uim.component.UimField;
import org.opaeum.uim.control.UimLookup;

public class DropdownEditingSupport extends EditingSupport{
	private final CheckboxTableViewer tableViewer;
	private final UimField uimField;
	private BindingUtil bindingUtil;
	private EntityEditorInputJface input;
	private Class<?> rowClass;
	public DropdownEditingSupport(Class<?> rowClass, CheckboxTableViewer tableViewer,UimField uimField, BindingUtil bindingUtil, EntityEditorInputJface input){
		super(tableViewer);
		this.tableViewer = tableViewer;
		this.uimField = uimField;
		this.bindingUtil=bindingUtil;
		this.input=input;
		this.rowClass=rowClass;
	}
	@Override
	protected void setValue(Object element,Object value){
		Object target = bindingUtil.resolveTarget(element, uimField.getBinding());
		if(target != null){
			JavaTypedElement typedElement = bindingUtil.resolveLastTypedElement(rowClass, uimField.getBinding());
			typedElement.invokeSetter(target, value);
			tableViewer.refresh(element);
			if(input != null){
				input.setDirty(true);
			}
		}
	}
	@Override
	protected Object getValue(Object element){
		return bindingUtil.invoke(element, uimField.getBinding());
	}
	@Override
	protected CellEditor getCellEditor(final Object element){
		JavaTypedElement typedElement = bindingUtil.resolveLastTypedElement(rowClass, uimField.getBinding());
		ComboBoxViewerCellEditor result = new ComboBoxViewerCellEditor(tableViewer.getTable());
		result.setLabelProvider(new DefaultLabelProvider());
		result.setContentProvider(new ArrayContentProvider());
		if(typedElement.getBaseType().isEnum()){
			result.setInput(typedElement.getBaseType().getEnumConstants());
		}else if(typedElement.getBaseType().isAnnotationPresent(Entity.class)){
			UimLookup lookup = (UimLookup) uimField.getControl();
			if(lookup.getLookupSource() == null){
				if(typedElement.isReadOnly()){
					result.setInput(bindingUtil.invoke(element, uimField.getBinding()));
				}else{
					result.setInput(typedElement.invokeLookupMethod((IPersistentObject) element));
				}
			}else{
				result.setInput(bindingUtil.invoke(element, lookup.getLookupSource()));
			}
		}
		return result;
	}
	@Override
	protected boolean canEdit(Object element){
		return true;
	}
}