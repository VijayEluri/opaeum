/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.nakeduml.uim.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.nakeduml.uim.LayoutContainer;
import org.nakeduml.uim.OutlayableComponent;
import org.nakeduml.uim.UimLayout;
import org.nakeduml.uim.UimPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Layout</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.nakeduml.uim.impl.UimLayoutImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link org.nakeduml.uim.impl.UimLayoutImpl#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UimLayoutImpl extends UimContainerImpl implements UimLayout {
	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<OutlayableComponent> children;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UimLayoutImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UimPackage.Literals.UIM_LAYOUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LayoutContainer getParent() {
		if (eContainerFeatureID() != UimPackage.UIM_LAYOUT__PARENT) return null;
		return (LayoutContainer)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParent(LayoutContainer newParent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newParent, UimPackage.UIM_LAYOUT__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParent(LayoutContainer newParent) {
		if (newParent != eInternalContainer() || (eContainerFeatureID() != UimPackage.UIM_LAYOUT__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, newParent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseAdd(this, UimPackage.LAYOUT_CONTAINER__LAYOUT, LayoutContainer.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UimPackage.UIM_LAYOUT__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OutlayableComponent> getChildren() {
		if (children == null) {
			children = new EObjectContainmentWithInverseEList<OutlayableComponent>(OutlayableComponent.class, this, UimPackage.UIM_LAYOUT__CHILDREN, UimPackage.OUTLAYABLE_COMPONENT__PARENT);
		}
		return children;
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
			case UimPackage.UIM_LAYOUT__PARENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetParent((LayoutContainer)otherEnd, msgs);
			case UimPackage.UIM_LAYOUT__CHILDREN:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
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
			case UimPackage.UIM_LAYOUT__PARENT:
				return basicSetParent(null, msgs);
			case UimPackage.UIM_LAYOUT__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
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
			case UimPackage.UIM_LAYOUT__PARENT:
				return eInternalContainer().eInverseRemove(this, UimPackage.LAYOUT_CONTAINER__LAYOUT, LayoutContainer.class, msgs);
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
			case UimPackage.UIM_LAYOUT__PARENT:
				return getParent();
			case UimPackage.UIM_LAYOUT__CHILDREN:
				return getChildren();
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
			case UimPackage.UIM_LAYOUT__PARENT:
				setParent((LayoutContainer)newValue);
				return;
			case UimPackage.UIM_LAYOUT__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends OutlayableComponent>)newValue);
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
			case UimPackage.UIM_LAYOUT__PARENT:
				setParent((LayoutContainer)null);
				return;
			case UimPackage.UIM_LAYOUT__CHILDREN:
				getChildren().clear();
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
			case UimPackage.UIM_LAYOUT__PARENT:
				return getParent() != null;
			case UimPackage.UIM_LAYOUT__CHILDREN:
				return children != null && !children.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //UimLayoutImpl