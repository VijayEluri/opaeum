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

import java.util.Collection;

import javax.persistence.Entity;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.environment.JavaTypedElement;
import org.opaeum.runtime.jface.binding.BindingUtil;
import org.opaeum.runtime.jface.entityeditor.EntityEditorInputJface;
import org.opaeum.uim.component.UimField;
import org.opaeum.uim.control.UimLookup;

public class PopupSearchEditingSupport extends EditingSupport{
	private final CheckboxTableViewer tableViewer;
	private final EntityEditorInputJface input;
	private final BindingUtil bindingUtil;
	private final UimField uimField;
	private Class<?> rowClass;
	public PopupSearchEditingSupport(Class<?> rowClass, CheckboxTableViewer tableViewer,EntityEditorInputJface input,BindingUtil bindingUtil,
			UimField uimField){
		super(tableViewer);
		this.tableViewer = tableViewer;
		this.input = input;
		this.bindingUtil = bindingUtil;
		this.uimField = uimField;
		this.rowClass=rowClass;
	}
	@Override
	protected CellEditor getCellEditor(final Object element){
		return new PopupSearchCellEditor(tableViewer.getTable()){
			@Override
			protected Object[] getChoices(){
				JavaTypedElement typedElement = bindingUtil.resolveLastTypedElement(rowClass,uimField.getBinding());
				if(typedElement.getBaseType().isEnum()){
					return typedElement.getBaseType().getEnumConstants();
				}else if(typedElement.getBaseType().isAnnotationPresent(Entity.class)){
					UimLookup lookup = (UimLookup) uimField.getControl();
					if(lookup.getLookupSource() == null){
						return ((Collection<?>) typedElement.invokeLookupMethod((IPersistentObject) element)).toArray();
					}else{
						return ((Collection<?>) bindingUtil.invoke(element, lookup.getLookupSource())).toArray();
					}
				}
				return new Object[0];
			}
		};
	}
	@Override
	protected boolean canEdit(Object element){
		return true;
	}
	@Override
	protected Object getValue(Object element){
		return bindingUtil.invoke(element, uimField.getBinding());
	}
	@Override
	protected void setValue(Object element,Object value){
		bindingUtil.invokeSetter(element, uimField.getBinding(), value);
		tableViewer.refresh(element);
		if(input != null){
			input.setDirty(true);
		}
	}
}