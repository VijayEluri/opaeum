/*******************************************************************************
 * No CopyrightText Defined in the configurator file.
 ******************************************************************************/
package org.opeum.uim.modeleditor.providers;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.opeum.uim.layout.util.LayoutAdapterFactory;
import org.topcased.modeler.providers.ILabelFeatureProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers. The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}. The adapters also support Eclipse property sheets. Note that most of the adapters are shared among multiple instances.
 * 
 * @generated
 */
public class LayoutModelerProviderAdapterFactory extends LayoutAdapterFactory implements ComposeableAdapterFactory,IChangeNotifier,IDisposable{
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * 
	 * @generated
	 */
	private ComposedAdapterFactory parentAdapterFactory;
	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * 
	 * @generated
	 */
	private IChangeNotifier changeNotifier = new ChangeNotifier();
	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * 
	 * @generated
	 */
	private Collection supportedTypes = new ArrayList();
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.UimColumnLayout} instances.
	 * 
	 * @generated
	 */
	private UimColumnLayoutModelerProvider uimcolumnlayoutModelerProvider;
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.UimFullLayout} instances.
	 * 
	 * @generated
	 */
	private UimFullLayoutModelerProvider uimfulllayoutModelerProvider;
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.UimXYLayout} instances.
	 * 
	 * @generated
	 */
	private UimXYLayoutModelerProvider uimxylayoutModelerProvider;
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.UimBorderLayout} instances.
	 * 
	 * @generated
	 */
	private UimBorderLayoutModelerProvider uimborderlayoutModelerProvider;
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.UimToolbarLayout} instances.
	 * 
	 * @generated
	 */
	private UimToolbarLayoutModelerProvider uimtoolbarlayoutModelerProvider;
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.OutlayableComponent} instances.
	 * 
	 * @generated
	 */
	private OutlayableComponentModelerProvider outlayablecomponentModelerProvider;
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.UimLayout} instances.
	 * 
	 * @generated
	 */
	private UimLayoutModelerProvider uimlayoutModelerProvider;
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.LayoutContainer} instances.
	 * 
	 * @generated
	 */
	private LayoutContainerModelerProvider layoutcontainerModelerProvider;
	/**
	 * This keeps track of the one adapter used for all {@link org.opeum.uim.layout.UimGridLayout} instances.
	 * 
	 * @generated
	 */
	private UimGridLayoutModelerProvider uimgridlayoutModelerProvider;
	/**
	 * This constructs an instance.
	 * 
	 * @generated
	 */
	public LayoutModelerProviderAdapterFactory(){
		supportedTypes.add(ILabelFeatureProvider.class);
	}
	/**
	 * This returns the root adapter factory that contains this factory.
	 *
	 * @return the root AdapterFactory
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory(){
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}
	/**
	 * This sets the composed adapter factory that contains this factory.
	 *
	 * @param parentAdapterFactory the new parent adapter factory
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory){
		this.parentAdapterFactory = parentAdapterFactory;
	}
	/**
	 * @param type the type to test
	 * @return true if supported
	 * 
	 * @generated
	 */
	public boolean isFactoryForType(Object type){
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}
	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 *
	 * @param notifier the notifier
	 * @param type the object to adapt
	 * @return the Adapter the created adatper
	 * @generated
	 */
	public Adapter adapt(Notifier notifier,Object type){
		return super.adapt(notifier, this);
	}
	/**
	 * @param object the object to adapt
	 * @param type the type to adapt
	 * @return the adapted Object
	 * @generated
	 */
	public Object adapt(Object object,Object type){
		if(isFactoryForType(type)){
			Object adapter = super.adapt(object, type);
			if(!(type instanceof Class) || (((Class) type).isInstance(adapter))){
				return adapter;
			}
		}
		return null;
	}
	/**
	 * This adds a listener.
	 *
	 * @param notifyChangedListener the listener to add
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener){
		changeNotifier.addListener(notifyChangedListener);
	}
	/**
	 * This removes a listener.
	 *
	 * @param notifyChangedListener the listener to remove
	 * 
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener){
		changeNotifier.removeListener(notifyChangedListener);
	}
	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 *
	 * @param notification the notification to fire
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification){
		changeNotifier.fireNotifyChanged(notification);
		if(parentAdapterFactory != null){
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.UimColumnLayout}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createUimColumnLayoutAdapter(){
		if(uimcolumnlayoutModelerProvider == null){
			uimcolumnlayoutModelerProvider = new UimColumnLayoutModelerProvider(this);
		}
		return uimcolumnlayoutModelerProvider;
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.UimFullLayout}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createUimFullLayoutAdapter(){
		if(uimfulllayoutModelerProvider == null){
			uimfulllayoutModelerProvider = new UimFullLayoutModelerProvider(this);
		}
		return uimfulllayoutModelerProvider;
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.UimXYLayout}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createUimXYLayoutAdapter(){
		if(uimxylayoutModelerProvider == null){
			uimxylayoutModelerProvider = new UimXYLayoutModelerProvider(this);
		}
		return uimxylayoutModelerProvider;
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.UimBorderLayout}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createUimBorderLayoutAdapter(){
		if(uimborderlayoutModelerProvider == null){
			uimborderlayoutModelerProvider = new UimBorderLayoutModelerProvider(this);
		}
		return uimborderlayoutModelerProvider;
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.UimToolbarLayout}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createUimToolbarLayoutAdapter(){
		if(uimtoolbarlayoutModelerProvider == null){
			uimtoolbarlayoutModelerProvider = new UimToolbarLayoutModelerProvider(this);
		}
		return uimtoolbarlayoutModelerProvider;
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.OutlayableComponent}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createOutlayableComponentAdapter(){
		if(outlayablecomponentModelerProvider == null){
			outlayablecomponentModelerProvider = new OutlayableComponentModelerProvider(this);
		}
		return outlayablecomponentModelerProvider;
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.UimLayout}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createUimLayoutAdapter(){
		if(uimlayoutModelerProvider == null){
			uimlayoutModelerProvider = new UimLayoutModelerProvider(this);
		}
		return uimlayoutModelerProvider;
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.LayoutContainer}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createLayoutContainerAdapter(){
		if(layoutcontainerModelerProvider == null){
			layoutcontainerModelerProvider = new LayoutContainerModelerProvider(this);
		}
		return layoutcontainerModelerProvider;
	}
	/**
	 * This creates an adapter for a {@link org.opeum.uim.layout.UimGridLayout}.
	 *
	 * @return the Adapter
	 * @generated
	 */
	public Adapter createUimGridLayoutAdapter(){
		if(uimgridlayoutModelerProvider == null){
			uimgridlayoutModelerProvider = new UimGridLayoutModelerProvider(this);
		}
		return uimgridlayoutModelerProvider;
	}
	/**
	 * This disposes all of the item providers created by this factory.
	 * 
	 * @generated
	 */
	public void dispose(){
		if(uimcolumnlayoutModelerProvider != null){
			uimcolumnlayoutModelerProvider.dispose();
		}
		if(uimfulllayoutModelerProvider != null){
			uimfulllayoutModelerProvider.dispose();
		}
		if(uimxylayoutModelerProvider != null){
			uimxylayoutModelerProvider.dispose();
		}
		if(uimborderlayoutModelerProvider != null){
			uimborderlayoutModelerProvider.dispose();
		}
		if(uimtoolbarlayoutModelerProvider != null){
			uimtoolbarlayoutModelerProvider.dispose();
		}
		if(outlayablecomponentModelerProvider != null){
			outlayablecomponentModelerProvider.dispose();
		}
		if(uimlayoutModelerProvider != null){
			uimlayoutModelerProvider.dispose();
		}
		if(layoutcontainerModelerProvider != null){
			layoutcontainerModelerProvider.dispose();
		}
		if(uimgridlayoutModelerProvider != null){
			uimgridlayoutModelerProvider.dispose();
		}
	}
}
