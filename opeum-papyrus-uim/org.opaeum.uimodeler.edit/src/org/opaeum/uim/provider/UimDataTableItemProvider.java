/**
 */
package org.opaeum.uim.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.opaeum.uim.UimDataTable;
import org.opaeum.uim.UimFactory;
import org.opaeum.uim.UimPackage;
import org.opaeum.uim.action.ActionFactory;
import org.opaeum.uim.binding.BindingFactory;
import org.opaeum.uim.constraint.ConstraintFactory;
import org.opaeum.uim.constraint.ConstraintPackage;
import org.opaeum.uim.editor.EditorFactory;
import org.opaeum.uim.panel.PanelFactory;
import org.opaeum.uim.panel.PanelPackage;

/**
 * This is the item provider adapter for a {@link org.opaeum.uim.UimDataTable} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UimDataTableItemProvider
	extends MasterComponentItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UimDataTableItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addNamePropertyDescriptor(object);
			addUnderUserControlPropertyDescriptor(object);
			addPreferredWidthPropertyDescriptor(object);
			addPreferredHeightPropertyDescriptor(object);
			addFillHorizontallyPropertyDescriptor(object);
			addFillVerticallyPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserInteractionElement_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserInteractionElement_name_feature", "_UI_UserInteractionElement_type"),
				 UimPackage.Literals.USER_INTERACTION_ELEMENT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Under User Control feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUnderUserControlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserInteractionElement_underUserControl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserInteractionElement_underUserControl_feature", "_UI_UserInteractionElement_type"),
				 UimPackage.Literals.USER_INTERACTION_ELEMENT__UNDER_USER_CONTROL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Preferred Width feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPreferredWidthPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Outlayable_preferredWidth_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Outlayable_preferredWidth_feature", "_UI_Outlayable_type"),
				 PanelPackage.Literals.OUTLAYABLE__PREFERRED_WIDTH,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Preferred Height feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPreferredHeightPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Outlayable_preferredHeight_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Outlayable_preferredHeight_feature", "_UI_Outlayable_type"),
				 PanelPackage.Literals.OUTLAYABLE__PREFERRED_HEIGHT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Fill Horizontally feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFillHorizontallyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Outlayable_fillHorizontally_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Outlayable_fillHorizontally_feature", "_UI_Outlayable_type"),
				 PanelPackage.Literals.OUTLAYABLE__FILL_HORIZONTALLY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Fill Vertically feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFillVerticallyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Outlayable_fillVertically_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Outlayable_fillVertically_feature", "_UI_Outlayable_type"),
				 PanelPackage.Literals.OUTLAYABLE__FILL_VERTICALLY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ConstraintPackage.Literals.CONSTRAINED_OBJECT__VISIBILITY);
			childrenFeatures.add(ConstraintPackage.Literals.EDITABLE_CONSTRAINED_OBJECT__EDITABILITY);
			childrenFeatures.add(UimPackage.Literals.UIM_CONTAINER__CHILDREN);
			childrenFeatures.add(UimPackage.Literals.UIM_DATA_TABLE__BINDING);
			childrenFeatures.add(UimPackage.Literals.UIM_DATA_TABLE__ACTIONS_ON_MULTIPLE_SELECTION);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns UimDataTable.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/UimDataTable"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((UimDataTable)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_UimDataTable_type") :
			getString("_UI_UimDataTable_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(UimDataTable.class)) {
			case UimPackage.UIM_DATA_TABLE__NAME:
			case UimPackage.UIM_DATA_TABLE__UNDER_USER_CONTROL:
			case UimPackage.UIM_DATA_TABLE__PREFERRED_WIDTH:
			case UimPackage.UIM_DATA_TABLE__PREFERRED_HEIGHT:
			case UimPackage.UIM_DATA_TABLE__FILL_HORIZONTALLY:
			case UimPackage.UIM_DATA_TABLE__FILL_VERTICALLY:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case UimPackage.UIM_DATA_TABLE__VISIBILITY:
			case UimPackage.UIM_DATA_TABLE__EDITABILITY:
			case UimPackage.UIM_DATA_TABLE__CHILDREN:
			case UimPackage.UIM_DATA_TABLE__BINDING:
			case UimPackage.UIM_DATA_TABLE__ACTIONS_ON_MULTIPLE_SELECTION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ConstraintPackage.Literals.CONSTRAINED_OBJECT__VISIBILITY,
				 ConstraintFactory.eINSTANCE.createUserInteractionConstraint()));

		newChildDescriptors.add
			(createChildParameter
				(ConstraintPackage.Literals.EDITABLE_CONSTRAINED_OBJECT__EDITABILITY,
				 ConstraintFactory.eINSTANCE.createUserInteractionConstraint()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 UimFactory.eINSTANCE.createUimField()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 UimFactory.eINSTANCE.createUimDataTable()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 UimFactory.eINSTANCE.createDetailComponent()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 UimFactory.eINSTANCE.createAbstractActionBar()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 EditorFactory.eINSTANCE.createEditorActionBar()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 ActionFactory.eINSTANCE.createBuiltInActionButton()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 ActionFactory.eINSTANCE.createTransitionButton()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 ActionFactory.eINSTANCE.createLinkToQuery()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 ActionFactory.eINSTANCE.createOperationButton()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 ActionFactory.eINSTANCE.createBuiltInLink()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 PanelFactory.eINSTANCE.createGridPanel()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 PanelFactory.eINSTANCE.createVerticalPanel()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_CONTAINER__CHILDREN,
				 PanelFactory.eINSTANCE.createHorizontalPanel()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_DATA_TABLE__BINDING,
				 BindingFactory.eINSTANCE.createTableBinding()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_DATA_TABLE__ACTIONS_ON_MULTIPLE_SELECTION,
				 ActionFactory.eINSTANCE.createBuiltInActionButton()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_DATA_TABLE__ACTIONS_ON_MULTIPLE_SELECTION,
				 ActionFactory.eINSTANCE.createTransitionButton()));

		newChildDescriptors.add
			(createChildParameter
				(UimPackage.Literals.UIM_DATA_TABLE__ACTIONS_ON_MULTIPLE_SELECTION,
				 ActionFactory.eINSTANCE.createOperationButton()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == ConstraintPackage.Literals.CONSTRAINED_OBJECT__VISIBILITY ||
			childFeature == ConstraintPackage.Literals.EDITABLE_CONSTRAINED_OBJECT__EDITABILITY ||
			childFeature == UimPackage.Literals.UIM_CONTAINER__CHILDREN ||
			childFeature == UimPackage.Literals.UIM_DATA_TABLE__ACTIONS_ON_MULTIPLE_SELECTION;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
