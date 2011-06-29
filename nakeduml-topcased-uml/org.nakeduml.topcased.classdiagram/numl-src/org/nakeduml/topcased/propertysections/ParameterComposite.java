package org.nakeduml.topcased.propertysections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.ParameterEffectKind;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.topcased.facilities.widgets.ComboViewer;
import org.topcased.modeler.editor.MixedEditDomain;
import org.topcased.modeler.editor.properties.TextChangeHelper;
import org.topcased.tabbedproperties.sections.widgets.CSingleObjectChooser;
import org.topcased.tabbedproperties.utils.TypeCacheAdapter;

public class ParameterComposite extends Composite{
	private Parameter parameter;
	private MixedEditDomain mixedEditDomain;
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Text parameterNameTxt;
	private CSingleObjectChooser parameterType;
	private CCombo parameterDirectionCb;
	private ComboViewer parameterDirectionCbViewer;
	public ParameterComposite(Composite parent,int style,TabbedPropertySheetWidgetFactory widgetFactory){
		super(parent, style);
		this.widgetFactory = widgetFactory;
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		widgetFactory.adapt(this);
		createContents(this);
		hookListeners();
	}
	public void setParameter(Parameter parameter){
		this.parameter = parameter;
		loadData();
	}
	public void setMixedEditDomain(MixedEditDomain mixedEditDomain){
		this.mixedEditDomain = mixedEditDomain;
	}
	protected void createContents(Composite parent){
		widgetFactory.createLabel(parent, "Name : ");
		parameterNameTxt = widgetFactory.createText(parent, "", SWT.BORDER);
		parameterNameTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		widgetFactory.createLabel(parent, "Type : ");
		parameterType = new CSingleObjectChooser(parent, widgetFactory, SWT.NONE);
		parameterType.setLabelProvider(new AdapterFactoryLabelProvider(new UMLItemProviderAdapterFactory()));
		parameterType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		widgetFactory.createLabel(parent, "Direction : ");
		parameterDirectionCb = widgetFactory.createCCombo(parent, SWT.BORDER|SWT.FLAT );
		parameterDirectionCb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		parameterDirectionCbViewer = new ComboViewer(parameterDirectionCb,SWT.FLAT | SWT.BORDER);
		parameterDirectionCbViewer.setContentProvider(new ArrayContentProvider());
		parameterDirectionCbViewer.setLabelProvider(new AdapterFactoryLabelProvider(new UMLItemProviderAdapterFactory()));
	}
	private void hookListeners(){
		TextChangeHelper parameterNameListener = new TextChangeHelper(){
			public void textChanged(Control control){
				String newText = parameterNameTxt.getText();
				if(parameter != null && !newText.equals(parameter.getName())){
					mixedEditDomain.getEMFEditingDomain().getCommandStack()
							.execute(SetCommand.create(mixedEditDomain.getEMFEditingDomain(), parameter, UMLPackage.eINSTANCE.getNamedElement_Name(), newText));
				}
			}
		};
		parameterNameListener.startListeningTo(parameterNameTxt);
		parameterNameListener.startListeningForEnter(parameterNameTxt);
		parameterType.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event){
				Object selectedElement = parameterType.getSelection();
				if(selectedElement != null && !selectedElement.equals(parameter.getType())){
					mixedEditDomain.getEMFEditingDomain().getCommandStack()
							.execute(SetCommand.create(mixedEditDomain.getEMFEditingDomain(), parameter, UMLPackage.eINSTANCE.getTypedElement_Type(), selectedElement));
				}
			}
		});
		parameterDirectionCbViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event){
				Object selectedElement = ((StructuredSelection) parameterDirectionCbViewer.getSelection()).getFirstElement();
				if(!selectedElement.equals(parameter.getDirection())){
					mixedEditDomain.getEMFEditingDomain().getCommandStack()
							.execute(SetCommand.create(mixedEditDomain.getEMFEditingDomain(), parameter, UMLPackage.eINSTANCE.getParameter_Direction(), selectedElement));
				}
			}
		});
	}
	private void loadData(){
		if(parameter != null){
			String nameToDisplay = parameter.getName() != null ? parameter.getName() : "";
			parameterNameTxt.setText(nameToDisplay);
			parameterType.setChoices(getChoices());
			if(parameter.getType() != null){
				parameterType.setSelection(parameter.getType());
			}
			parameterDirectionCbViewer.setInput(ParameterDirectionKind.VALUES);
			if(parameter.getDirection() != null){
				parameterDirectionCbViewer.setSelection(new StructuredSelection(parameter.getDirection()), true);
			}
		}else{
			parameterNameTxt.setText("");
			parameterType.setSelection(null);
			parameterDirectionCbViewer.setInput(null);
		}
	}
	private Object[] getChoices(){
		List<Object> choices = new ArrayList<Object>();
		choices.add("");
		Collection<EObject> types = TypeCacheAdapter.getExistingTypeCacheAdapter(parameter.getOperation()).getReachableObjectsOfType(parameter.getOperation(),
				UMLPackage.eINSTANCE.getType());
		
		choices.addAll(UmlMetaTypeRemover.removeAll(types));
		return choices.toArray();
	}
}