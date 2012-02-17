package org.opaeum.topcased.activitydiagram.propertysections;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.InstanceValue;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pin;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.ValueSpecification;
import org.opaeum.eclipse.uml.propertysections.common.UIAdapterImpl;
import org.opaeum.topcased.propertysections.ocl.OclBodyComposite;

public class PinTable extends Composite{
	private static final String[] COLUMN_NAMES = new String[]{"Pin Name","Type","Value"};
	private Action callAction;
	private CheckboxTableViewer parametersTableViewer;
	private Table parametersTable;
	private Adapter modelListener = new UIAdapterImpl(){
		@Override
		protected void safeNotifyChanged(Notification msg){
			// TODO Auto-generated method stub
		}
	};
	public PinTable(Composite parent,int style){
		super(parent, style);
		setBackground(parent.getBackground());
		setLayout(new GridLayout(2, false));
		createContents(this);
	}
	public void setAction(Action callAction){
		if(this.callAction != null){
			this.callAction.eAdapters().remove(modelListener);
		}
		this.callAction = callAction;
		if(this.callAction != null){
			this.callAction.eAdapters().add(modelListener);
		}
		parametersTableViewer.setInput(callAction.getInputs());
		//
		// // Let's checking the valuepin
		// parametersTableViewer.setCheckedElements(valuePins.toArray());
		refresh();
	}
	public void refreshPin(Pin a){
		parametersTableViewer.refresh(a);
	}
	protected void createContents(Composite parent){
		parametersTable = new Table(parent, SWT.FULL_SELECTION | SWT.SINGLE | SWT.V_SCROLL | SWT.CHECK | SWT.BORDER);
		parametersTableViewer = new CheckboxTableViewer(parametersTable);
		parametersTableViewer.setColumnProperties(COLUMN_NAMES);
		parametersTableViewer.setContentProvider(new PinContentProvider());
		parametersTableViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event){
				if(event.getSelection() instanceof IStructuredSelection){
					IStructuredSelection selec = (IStructuredSelection) event.getSelection();
					setSelectedPin((Pin) selec.getFirstElement());
				}
			}
		});
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 60;
		gridData.verticalSpan = 2;
		parametersTable.setLayoutData(gridData);
		parametersTable.setHeaderVisible(true);
		parametersTable.setLinesVisible(true);
		TableViewerColumn column;
		column = createTableViewerColumn(parametersTableViewer, COLUMN_NAMES[0], new ParameterNameLabelProvider(), 0);
		column.getColumn().setWidth(150);
		column = createTableViewerColumn(parametersTableViewer, COLUMN_NAMES[1], new ParameterTypeLabelProvider(), 1);
		column.getColumn().setWidth(150);
		column = createTableViewerColumn(parametersTableViewer, COLUMN_NAMES[2], new ParameterValueLabelProvider(), 2);
		column.getColumn().setWidth(150);
		parametersTableViewer.addCheckStateListener(new ICheckStateListener(){
			public void checkStateChanged(CheckStateChangedEvent event){
			}
		});
	}
	protected void setSelectedPin(Pin firstElement){
	}
	protected void handleModelChanged(Notification msg){
		Object notifier = msg.getNotifier();
		Object newObject = msg.getNewValue();
		Object oldObject = msg.getOldValue();
		if(notifier instanceof ValuePin){
			switch(msg.getFeatureID(ValuePin.class)){
			case UMLPackage.VALUE_PIN__VALUE:
				updateModelListening(oldObject, newObject);
				break;
			case UMLPackage.VALUE_PIN__INCOMING:
				// In that case, the pin is transformed into an input pin (so should be checked)
				parametersTableViewer.setChecked(notifier, true);
				break;
			default:
				break;
			}
		}else if(notifier instanceof InputPin){
			switch(msg.getFeatureID(InputPin.class)){
			case UMLPackage.INPUT_PIN__INCOMING:
				parametersTableViewer.setChecked(notifier, false);
				break;
			default:
				break;
			}
		}else if(notifier instanceof ValueSpecification){
			Element inputPin = ((ValueSpecification) notifier).getOwner();
			if(inputPin != null){
				parametersTableViewer.update(inputPin, new String[]{COLUMN_NAMES[2]});
			}
		}
	}
	protected void updateModelListening(Object oldValue,Object newValue){
		if(oldValue != newValue){
			if(oldValue != null && oldValue instanceof EObject){
				((EObject) oldValue).eAdapters().remove(modelListener);
			}
			if(newValue != null && newValue instanceof EObject){
				if(!((EObject) newValue).eAdapters().contains(modelListener)){
					((EObject) newValue).eAdapters().add(modelListener);
				}
			}
		}
	}
	protected Adapter getModelListener(){
		return modelListener;
	}
	private TableViewerColumn createTableViewerColumn(final TableViewer viewer,final String columnTitle,
			final ColumnLabelProvider labelProvider,final int index){
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.CENTER, index);
		column.getColumn().setText(columnTitle);
		column.setLabelProvider(labelProvider);
		return column;
	}
	protected Action getCallAction(){
		return callAction;
	}
	protected void refresh(){
		layout(true);
	}
	protected EStructuralFeature getFeature(){
		return UMLPackage.eINSTANCE.getBehavioralFeature_OwnedParameter();
	}
	class PinContentProvider implements IStructuredContentProvider{
		public Object[] getElements(Object inputElement){
			if(inputElement instanceof List<?>){
				return ((List<?>) inputElement).toArray();
			}
			return new Object[0];
		}
		public void dispose(){
		}
		public void inputChanged(Viewer viewer,Object oldInput,Object newInput){
		}
	}
	protected class ParameterNameLabelProvider extends ColumnLabelProvider{
		@Override
		public String getText(Object element){
			Pin pin = (Pin) element;
			if(!pin.eContainingFeature().isMany()){
				return pin.eContainingFeature().getName();
			}
			return pin.getName();
		}
	}
	protected class ParameterTypeLabelProvider extends ColumnLabelProvider{
		@Override
		public String getText(Object element){
			Pin param = (Pin) element;
			if(param.getType() != null){
				return param.getType().getName();
			}else{
				return "null";
			}
		}
	}
	protected class ParameterValueLabelProvider extends ColumnLabelProvider{
		@Override
		public String getText(Object element){
			InputPin inputPin = (InputPin) element;
			if(!inputPin.getIncomings().isEmpty()){
				return "N/A";
			}
			if(inputPin instanceof ValuePin){
				ValuePin param = (ValuePin) inputPin;
				ValueSpecification valSpec = param.getValue();
				if(valSpec != null){
					if(valSpec instanceof InstanceValue){
						InstanceSpecification inst = ((InstanceValue) valSpec).getInstance();
						return inst == null ? "" : inst.getName();
					}else if(valSpec instanceof OpaqueExpression){
						return OclBodyComposite.getOclText(((OpaqueExpression) valSpec).getBodies(), ((OpaqueExpression) valSpec).getLanguages());
					}
				}
			}
			return "Not set";
		}
	}
}
