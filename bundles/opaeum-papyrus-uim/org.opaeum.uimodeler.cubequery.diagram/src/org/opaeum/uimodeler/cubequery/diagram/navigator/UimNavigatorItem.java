package org.opaeum.uimodeler.cubequery.diagram.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class UimNavigatorItem extends UimAbstractNavigatorItem{
	/**
	 * @generated
	 */
	static{
		final Class[] supportedTypes = new Class[]{View.class,EObject.class};
		Platform.getAdapterManager().registerAdapters(new IAdapterFactory(){
			public Object getAdapter(Object adaptableObject,Class adapterType){
				if(adaptableObject instanceof org.opaeum.uimodeler.cubequery.diagram.navigator.UimNavigatorItem
						&& (adapterType == View.class || adapterType == EObject.class)){
					return ((org.opaeum.uimodeler.cubequery.diagram.navigator.UimNavigatorItem) adaptableObject).getView();
				}
				return null;
			}
			public Class[] getAdapterList(){
				return supportedTypes;
			}
		}, org.opaeum.uimodeler.cubequery.diagram.navigator.UimNavigatorItem.class);
	}
	/**
	 * @generated
	 */
	private View myView;
	/**
	 * @generated
	 */
	private boolean myLeaf = false;
	/**
	 * @generated
	 */
	public UimNavigatorItem(View view,Object parent,boolean isLeaf){
		super(parent);
		myView = view;
		myLeaf = isLeaf;
	}
	/**
	 * @generated
	 */
	public View getView(){
		return myView;
	}
	/**
	 * @generated
	 */
	public boolean isLeaf(){
		return myLeaf;
	}
	/**
	 * @generated
	 */
	public boolean equals(Object obj){
		if(obj instanceof org.opaeum.uimodeler.cubequery.diagram.navigator.UimNavigatorItem){
			return EcoreUtil.getURI(getView()).equals(
					EcoreUtil.getURI(((org.opaeum.uimodeler.cubequery.diagram.navigator.UimNavigatorItem) obj).getView()));
		}
		return super.equals(obj);
	}
	/**
	 * @generated
	 */
	public int hashCode(){
		return EcoreUtil.getURI(getView()).hashCode();
	}
}
