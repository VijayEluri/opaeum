/**
 */
package org.opaeum.uim.perspective;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getExplorer <em>Explorer</em>}</li>
 *   <li>{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getEditor <em>Editor</em>}</li>
 *   <li>{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getInbox <em>Inbox</em>}</li>
 *   <li>{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getOutbox <em>Outbox</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.opaeum.uim.perspective.PerspectivePackage#getPerspectiveConfiguration()
 * @model
 * @generated
 */
public interface PerspectiveConfiguration extends EObject {
	/**
	 * Returns the value of the '<em><b>Explorer</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Explorer</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Explorer</em>' containment reference.
	 * @see #setExplorer(ExplorerConfiguration)
	 * @see org.opaeum.uim.perspective.PerspectivePackage#getPerspectiveConfiguration_Explorer()
	 * @model containment="true"
	 * @generated
	 */
	ExplorerConfiguration getExplorer();

	/**
	 * Sets the value of the '{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getExplorer <em>Explorer</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Explorer</em>' containment reference.
	 * @see #getExplorer()
	 * @generated
	 */
	void setExplorer(ExplorerConfiguration value);

	/**
	 * Returns the value of the '<em><b>Editor</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Editor</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Editor</em>' containment reference.
	 * @see #setEditor(EditorConfiguration)
	 * @see org.opaeum.uim.perspective.PerspectivePackage#getPerspectiveConfiguration_Editor()
	 * @model containment="true"
	 * @generated
	 */
	EditorConfiguration getEditor();

	/**
	 * Sets the value of the '{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getEditor <em>Editor</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Editor</em>' containment reference.
	 * @see #getEditor()
	 * @generated
	 */
	void setEditor(EditorConfiguration value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference.
	 * @see #setProperties(PropertiesConfiguration)
	 * @see org.opaeum.uim.perspective.PerspectivePackage#getPerspectiveConfiguration_Properties()
	 * @model containment="true"
	 * @generated
	 */
	PropertiesConfiguration getProperties();

	/**
	 * Sets the value of the '{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getProperties <em>Properties</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Properties</em>' containment reference.
	 * @see #getProperties()
	 * @generated
	 */
	void setProperties(PropertiesConfiguration value);

	/**
	 * Returns the value of the '<em><b>Inbox</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inbox</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inbox</em>' containment reference.
	 * @see #setInbox(InboxConfiguration)
	 * @see org.opaeum.uim.perspective.PerspectivePackage#getPerspectiveConfiguration_Inbox()
	 * @model containment="true"
	 * @generated
	 */
	InboxConfiguration getInbox();

	/**
	 * Sets the value of the '{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getInbox <em>Inbox</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Inbox</em>' containment reference.
	 * @see #getInbox()
	 * @generated
	 */
	void setInbox(InboxConfiguration value);

	/**
	 * Returns the value of the '<em><b>Outbox</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Outbox</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Outbox</em>' reference.
	 * @see #setOutbox(OutboxConfiguration)
	 * @see org.opaeum.uim.perspective.PerspectivePackage#getPerspectiveConfiguration_Outbox()
	 * @model
	 * @generated
	 */
	OutboxConfiguration getOutbox();

	/**
	 * Sets the value of the '{@link org.opaeum.uim.perspective.PerspectiveConfiguration#getOutbox <em>Outbox</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Outbox</em>' reference.
	 * @see #getOutbox()
	 * @generated
	 */
	void setOutbox(OutboxConfiguration value);

} // PerspectiveConfiguration