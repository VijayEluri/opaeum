/**
 */
package org.opaeum.uim.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.opaeum.uim.editor.BehaviorExecutionEditor;
import org.opaeum.uim.editor.EditorPackage;
import org.opaeum.uim.model.BehaviorUserInteractionModel;
import org.opaeum.uim.model.ModelPackage;
import org.opaeum.uim.wizard.BehaviorInvocationWizard;
import org.opaeum.uim.wizard.WizardPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Behavior User Interaction Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.opaeum.uim.model.impl.BehaviorUserInteractionModelImpl#getLinkedUmlResource <em>Linked Uml Resource</em>}</li>
 *   <li>{@link org.opaeum.uim.model.impl.BehaviorUserInteractionModelImpl#getInvocationWizard <em>Invocation Wizard</em>}</li>
 *   <li>{@link org.opaeum.uim.model.impl.BehaviorUserInteractionModelImpl#getEditor <em>Editor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BehaviorUserInteractionModelImpl extends EObjectImpl implements BehaviorUserInteractionModel {
	/**
	 * The default value of the '{@link #getLinkedUmlResource() <em>Linked Uml Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkedUmlResource()
	 * @generated
	 * @ordered
	 */
	protected static final String LINKED_UML_RESOURCE_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getLinkedUmlResource() <em>Linked Uml Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkedUmlResource()
	 * @generated
	 * @ordered
	 */
	protected String linkedUmlResource = LINKED_UML_RESOURCE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInvocationWizard() <em>Invocation Wizard</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInvocationWizard()
	 * @generated
	 * @ordered
	 */
	protected BehaviorInvocationWizard invocationWizard;

	/**
	 * The cached value of the '{@link #getEditor() <em>Editor</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEditor()
	 * @generated
	 * @ordered
	 */
	protected BehaviorExecutionEditor editor;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BehaviorUserInteractionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.BEHAVIOR_USER_INTERACTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLinkedUmlResource() {
		return linkedUmlResource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLinkedUmlResource(String newLinkedUmlResource) {
		String oldLinkedUmlResource = linkedUmlResource;
		linkedUmlResource = newLinkedUmlResource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__LINKED_UML_RESOURCE, oldLinkedUmlResource, linkedUmlResource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BehaviorInvocationWizard getInvocationWizard() {
		return invocationWizard;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInvocationWizard(BehaviorInvocationWizard newInvocationWizard, NotificationChain msgs) {
		BehaviorInvocationWizard oldInvocationWizard = invocationWizard;
		invocationWizard = newInvocationWizard;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD, oldInvocationWizard, newInvocationWizard);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInvocationWizard(BehaviorInvocationWizard newInvocationWizard) {
		if (newInvocationWizard != invocationWizard) {
			NotificationChain msgs = null;
			if (invocationWizard != null)
				msgs = ((InternalEObject)invocationWizard).eInverseRemove(this, WizardPackage.BEHAVIOR_INVOCATION_WIZARD__MODEL, BehaviorInvocationWizard.class, msgs);
			if (newInvocationWizard != null)
				msgs = ((InternalEObject)newInvocationWizard).eInverseAdd(this, WizardPackage.BEHAVIOR_INVOCATION_WIZARD__MODEL, BehaviorInvocationWizard.class, msgs);
			msgs = basicSetInvocationWizard(newInvocationWizard, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD, newInvocationWizard, newInvocationWizard));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BehaviorExecutionEditor getEditor() {
		return editor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEditor(BehaviorExecutionEditor newEditor, NotificationChain msgs) {
		BehaviorExecutionEditor oldEditor = editor;
		editor = newEditor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR, oldEditor, newEditor);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEditor(BehaviorExecutionEditor newEditor) {
		if (newEditor != editor) {
			NotificationChain msgs = null;
			if (editor != null)
				msgs = ((InternalEObject)editor).eInverseRemove(this, EditorPackage.BEHAVIOR_EXECUTION_EDITOR__MODEL, BehaviorExecutionEditor.class, msgs);
			if (newEditor != null)
				msgs = ((InternalEObject)newEditor).eInverseAdd(this, EditorPackage.BEHAVIOR_EXECUTION_EDITOR__MODEL, BehaviorExecutionEditor.class, msgs);
			msgs = basicSetEditor(newEditor, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR, newEditor, newEditor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD:
				if (invocationWizard != null)
					msgs = ((InternalEObject)invocationWizard).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD, null, msgs);
				return basicSetInvocationWizard((BehaviorInvocationWizard)otherEnd, msgs);
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR:
				if (editor != null)
					msgs = ((InternalEObject)editor).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR, null, msgs);
				return basicSetEditor((BehaviorExecutionEditor)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD:
				return basicSetInvocationWizard(null, msgs);
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR:
				return basicSetEditor(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__LINKED_UML_RESOURCE:
				return getLinkedUmlResource();
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD:
				return getInvocationWizard();
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR:
				return getEditor();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__LINKED_UML_RESOURCE:
				setLinkedUmlResource((String)newValue);
				return;
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD:
				setInvocationWizard((BehaviorInvocationWizard)newValue);
				return;
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR:
				setEditor((BehaviorExecutionEditor)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__LINKED_UML_RESOURCE:
				setLinkedUmlResource(LINKED_UML_RESOURCE_EDEFAULT);
				return;
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD:
				setInvocationWizard((BehaviorInvocationWizard)null);
				return;
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR:
				setEditor((BehaviorExecutionEditor)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__LINKED_UML_RESOURCE:
				return LINKED_UML_RESOURCE_EDEFAULT == null ? linkedUmlResource != null : !LINKED_UML_RESOURCE_EDEFAULT.equals(linkedUmlResource);
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__INVOCATION_WIZARD:
				return invocationWizard != null;
			case ModelPackage.BEHAVIOR_USER_INTERACTION_MODEL__EDITOR:
				return editor != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (linkedUmlResource: ");
		result.append(linkedUmlResource);
		result.append(')');
		return result.toString();
	}

} //BehaviorUserInteractionModelImpl