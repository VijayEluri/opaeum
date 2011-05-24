/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.nakeduml.uim.impl;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.nakeduml.uim.AbstractFolder;
import org.nakeduml.uim.AbstractFormFolder;
import org.nakeduml.uim.EditableSecureObject;
import org.nakeduml.uim.SecureObject;
import org.nakeduml.uim.SecurityConstraint;
import org.nakeduml.uim.UimPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Form Folder</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.nakeduml.uim.impl.AbstractFormFolderImpl#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link org.nakeduml.uim.impl.AbstractFormFolderImpl#getEditability <em>Editability</em>}</li>
 *   <li>{@link org.nakeduml.uim.impl.AbstractFormFolderImpl#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractFormFolderImpl extends AbstractFolderImpl implements AbstractFormFolder {
	/**
	 * The cached value of the '{@link #getVisibility() <em>Visibility</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVisibility()
	 * @generated
	 * @ordered
	 */
	protected SecurityConstraint visibility;
	/**
	 * The cached value of the '{@link #getEditability() <em>Editability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEditability()
	 * @generated
	 * @ordered
	 */
	protected SecurityConstraint editability;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractFormFolderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UimPackage.Literals.ABSTRACT_FORM_FOLDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SecurityConstraint getVisibility() {
		return visibility;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetVisibility(SecurityConstraint newVisibility, NotificationChain msgs) {
		SecurityConstraint oldVisibility = visibility;
		visibility = newVisibility;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY, oldVisibility, newVisibility);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVisibility(SecurityConstraint newVisibility) {
		if (newVisibility != visibility) {
			NotificationChain msgs = null;
			if (visibility != null)
				msgs = ((InternalEObject)visibility).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY, null, msgs);
			if (newVisibility != null)
				msgs = ((InternalEObject)newVisibility).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY, null, msgs);
			msgs = basicSetVisibility(newVisibility, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY, newVisibility, newVisibility));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SecurityConstraint getEditability() {
		return editability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEditability(SecurityConstraint newEditability, NotificationChain msgs) {
		SecurityConstraint oldEditability = editability;
		editability = newEditability;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY, oldEditability, newEditability);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEditability(SecurityConstraint newEditability) {
		if (newEditability != editability) {
			NotificationChain msgs = null;
			if (editability != null)
				msgs = ((InternalEObject)editability).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY, null, msgs);
			if (newEditability != null)
				msgs = ((InternalEObject)newEditability).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY, null, msgs);
			msgs = basicSetEditability(newEditability, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY, newEditability, newEditability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractFolder getParent() {
		if (eContainerFeatureID() != UimPackage.ABSTRACT_FORM_FOLDER__PARENT) return null;
		return (AbstractFolder)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParent(AbstractFolder newParent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newParent, UimPackage.ABSTRACT_FORM_FOLDER__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParent(AbstractFolder newParent) {
		if (newParent != eInternalContainer() || (eContainerFeatureID() != UimPackage.ABSTRACT_FORM_FOLDER__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, newParent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseAdd(this, UimPackage.ABSTRACT_FOLDER__CHILDREN, AbstractFolder.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UimPackage.ABSTRACT_FORM_FOLDER__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case UimPackage.ABSTRACT_FORM_FOLDER__PARENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetParent((AbstractFolder)otherEnd, msgs);
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
			case UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY:
				return basicSetVisibility(null, msgs);
			case UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY:
				return basicSetEditability(null, msgs);
			case UimPackage.ABSTRACT_FORM_FOLDER__PARENT:
				return basicSetParent(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case UimPackage.ABSTRACT_FORM_FOLDER__PARENT:
				return eInternalContainer().eInverseRemove(this, UimPackage.ABSTRACT_FOLDER__CHILDREN, AbstractFolder.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY:
				return getVisibility();
			case UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY:
				return getEditability();
			case UimPackage.ABSTRACT_FORM_FOLDER__PARENT:
				return getParent();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY:
				setVisibility((SecurityConstraint)newValue);
				return;
			case UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY:
				setEditability((SecurityConstraint)newValue);
				return;
			case UimPackage.ABSTRACT_FORM_FOLDER__PARENT:
				setParent((AbstractFolder)newValue);
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
			case UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY:
				setVisibility((SecurityConstraint)null);
				return;
			case UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY:
				setEditability((SecurityConstraint)null);
				return;
			case UimPackage.ABSTRACT_FORM_FOLDER__PARENT:
				setParent((AbstractFolder)null);
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
			case UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY:
				return visibility != null;
			case UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY:
				return editability != null;
			case UimPackage.ABSTRACT_FORM_FOLDER__PARENT:
				return getParent() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == SecureObject.class) {
			switch (derivedFeatureID) {
				case UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY: return UimPackage.SECURE_OBJECT__VISIBILITY;
				default: return -1;
			}
		}
		if (baseClass == EditableSecureObject.class) {
			switch (derivedFeatureID) {
				case UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY: return UimPackage.EDITABLE_SECURE_OBJECT__EDITABILITY;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == SecureObject.class) {
			switch (baseFeatureID) {
				case UimPackage.SECURE_OBJECT__VISIBILITY: return UimPackage.ABSTRACT_FORM_FOLDER__VISIBILITY;
				default: return -1;
			}
		}
		if (baseClass == EditableSecureObject.class) {
			switch (baseFeatureID) {
				case UimPackage.EDITABLE_SECURE_OBJECT__EDITABILITY: return UimPackage.ABSTRACT_FORM_FOLDER__EDITABILITY;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //AbstractFormFolderImpl