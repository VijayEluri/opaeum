/*******************************************************************************
 * No CopyrightText Defined in the configurator file.
 ******************************************************************************/
package org.nakeduml.uim.modeleditor.providers;


import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.nakeduml.uim.UIMPackage;
import org.topcased.modeler.providers.ILabelFeatureProvider;

/**
 * This is the item provider adpater for a {@link org.nakeduml.uim.UserInteractionElement} object.
 *
 * @generated
 */
public class UserInteractionElementModelerProvider extends ItemProviderAdapter implements ILabelFeatureProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 *
	 * @param adapterFactory the adapter factory
	 * @generated
	 */
	public UserInteractionElementModelerProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * @see org.topcased.modeler.providers.ILabelFeatureProvider#getLabelFeature(java.lang.Object)
	 * @generated
	 */
	public EAttribute getLabelFeature(Object object) {
		return UIMPackage.eINSTANCE.getUserInteractionElement_Name();
	}
}