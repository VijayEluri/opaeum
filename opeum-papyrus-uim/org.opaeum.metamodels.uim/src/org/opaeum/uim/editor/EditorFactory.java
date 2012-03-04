/**
 */
package org.opaeum.uim.editor;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.opaeum.uim.editor.EditorPackage
 * @generated
 */
public interface EditorFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EditorFactory eINSTANCE = org.opaeum.uim.editor.impl.EditorFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Abstract Editor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Abstract Editor</em>'.
	 * @generated
	 */
	AbstractEditor createAbstractEditor();

	/**
	 * Returns a new object of class '<em>Action Task Editor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Action Task Editor</em>'.
	 * @generated
	 */
	ActionTaskEditor createActionTaskEditor();

	/**
	 * Returns a new object of class '<em>Class Editor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Class Editor</em>'.
	 * @generated
	 */
	ClassEditor createClassEditor();

	/**
	 * Returns a new object of class '<em>Operation Task Editor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Task Editor</em>'.
	 * @generated
	 */
	OperationTaskEditor createOperationTaskEditor();

	/**
	 * Returns a new object of class '<em>Operation Invocation Editor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Invocation Editor</em>'.
	 * @generated
	 */
	OperationInvocationEditor createOperationInvocationEditor();

	/**
	 * Returns a new object of class '<em>Page</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Page</em>'.
	 * @generated
	 */
	EditorPage createEditorPage();

	/**
	 * Returns a new object of class '<em>Action Bar</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Action Bar</em>'.
	 * @generated
	 */
	ActionBar createActionBar();

	/**
	 * Returns a new object of class '<em>Menu Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Menu Configuration</em>'.
	 * @generated
	 */
	MenuConfiguration createMenuConfiguration();

	/**
	 * Returns a new object of class '<em>Visible Operation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Visible Operation</em>'.
	 * @generated
	 */
	VisibleOperation createVisibleOperation();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EditorPackage getEditorPackage();

} //EditorFactory
