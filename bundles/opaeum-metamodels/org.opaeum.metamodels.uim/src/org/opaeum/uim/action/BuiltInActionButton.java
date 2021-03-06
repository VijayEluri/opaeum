/**
 */
package org.opaeum.uim.action;

import org.opaeum.uim.Labels;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Built In Action Button</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.opaeum.uim.action.BuiltInActionButton#getKind <em>Kind</em>}</li>
 *   <li>{@link org.opaeum.uim.action.BuiltInActionButton#getLabels <em>Labels</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.opaeum.uim.action.ActionPackage#getBuiltInActionButton()
 * @model
 * @generated
 */
public interface BuiltInActionButton extends AbstractActionButton {
	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The default value is <code>"null"</code>.
	 * The literals are from the enumeration {@link org.opaeum.uim.action.ActionKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.opaeum.uim.action.ActionKind
	 * @see #setKind(ActionKind)
	 * @see org.opaeum.uim.action.ActionPackage#getBuiltInActionButton_Kind()
	 * @model default="null"
	 * @generated
	 */
	ActionKind getKind();

	/**
	 * Sets the value of the '{@link org.opaeum.uim.action.BuiltInActionButton#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.opaeum.uim.action.ActionKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(ActionKind value);

	/**
	 * Returns the value of the '<em><b>Labels</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Labels</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Labels</em>' reference.
	 * @see #setLabels(Labels)
	 * @see org.opaeum.uim.action.ActionPackage#getBuiltInActionButton_Labels()
	 * @model
	 * @generated
	 */
	Labels getLabels();

	/**
	 * Sets the value of the '{@link org.opaeum.uim.action.BuiltInActionButton#getLabels <em>Labels</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Labels</em>' reference.
	 * @see #getLabels()
	 * @generated
	 */
	void setLabels(Labels value);

} // BuiltInActionButton
