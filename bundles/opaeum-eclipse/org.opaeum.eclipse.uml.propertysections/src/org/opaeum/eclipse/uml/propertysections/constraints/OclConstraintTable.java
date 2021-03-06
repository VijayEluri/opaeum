package org.opaeum.eclipse.uml.propertysections.constraints;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.opaeum.eclipse.uml.editingsupport.AbstractCellEditorListener;
import org.opaeum.eclipse.uml.editingsupport.NamedElementNameEditingSupport;
import org.opaeum.eclipse.uml.editingsupport.OpaqueExpressionPropertyEditingSupport;
import org.opaeum.eclipse.uml.propertysections.core.AbstractTableComposite;

public class OclConstraintTable extends AbstractTableComposite<Constraint>{
	public OclConstraintTable(TabbedPropertySheetWidgetFactory factory,Composite parent,EStructuralFeature feature){
		super(parent, SWT.NONE, factory, feature);
	}
	@Override
	protected Constraint getNewChild(){
		Constraint newConstraint = UMLFactory.eINSTANCE.createConstraint();
		newConstraint.setName("newConstraint");
		OpaqueExpression oclExpression = createExpression(newConstraint);
		newConstraint.setSpecification(oclExpression);
		return newConstraint;
	}
	private OpaqueExpression createExpression(Constraint newConstraint){
		OpaqueExpression oclExpression = UMLFactory.eINSTANCE.createOpaqueExpression();
		if(newConstraint != null){
			oclExpression.setName(newConstraint.getName() + "_body");
		}else{
			oclExpression.setName("newConstraint_body");
		}
		oclExpression.getLanguages().add("OCL");
		String body = "";
		oclExpression.getBodies().add(body);
		return oclExpression;
	}
	@Override
	protected void createColumns(){
		createTableViewerColumn(new NamedElementNameEditingSupport(tableViewer));
		OpaqueExpressionPropertyEditingSupport oe = new OpaqueExpressionPropertyEditingSupport(tableViewer, widgetFactory,
				UMLPackage.eINSTANCE.getConstraint_Specification());
		createTableViewerColumn(oe).getColumn();
		oe.addCellEditorListener(new AbstractCellEditorListener(){
			@Override
			public void deactivated(){
				tableViewer.refresh();
			}
		});
	}
}
