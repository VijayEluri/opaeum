package org.opaeum.uim.userinteractionproperties.sections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.opaeum.eclipse.EmfElementFinder;
import org.opaeum.eclipse.uml.propertysections.base.AbstractChooserPropertySection;
import org.opaeum.emf.workspace.EmfWorkspace;
import org.opaeum.uim.UimPackage;
import org.opaeum.uim.cube.CubeQuery;
import org.opaeum.uim.model.ClassUserInteractionModel;
import org.opaeum.uim.provider.UimItemProviderAdapterFactory;
import org.opaeum.uim.util.UmlUimLinks;

public class CubeQueryCubeSourceSection extends AbstractChooserPropertySection{
	public String getLabelText(){
		return "Cube Source:";
	}
	protected EStructuralFeature getFeature(){
		return UimPackage.eINSTANCE.getUmlReference_UmlElementUid();
	}
	protected Object getFeatureValue(){
		return UmlUimLinks.getCurrentUmlLinks(getEObject()).getUmlElement(getCubeQuery());
	}
	private CubeQuery getCubeQuery(){
		return (CubeQuery) getEObject();
	}
	protected Object[] getComboFeatureValues(){
		Collection<Element> results = new ArrayList<Element>();
		ClassUserInteractionModel cuim = (ClassUserInteractionModel)getCubeQuery().eContainer().eContainer();
		Class clzz = (Class) UmlUimLinks.getCurrentUmlLinks(getEObject()).getUmlElement(cuim);
		results.addAll(EmfElementFinder.getCubes(clzz));
		return results.toArray();
	}
	protected ILabelProvider getLabelProvider(){
		List f = new ArrayList();
		f.add(new UimItemProviderAdapterFactory());
		f.addAll(getPrincipalAdapterFactories());
		return new AdapterFactoryLabelProvider(new ComposedAdapterFactory(f));
	}
	protected void createCommand(Object oldValue,Object newValue){
		boolean equals = oldValue == null ? false : oldValue.equals(newValue);
		if(!equals){
			EditingDomain editingDomain = getEditingDomain();
			Element value = (Element) newValue;
			String uuid = EmfWorkspace.getId(value);
			CompoundCommand compoundCommand = new CompoundCommand(COMMAND_NAME);
			// apply the property change to all selected elements
			for(EObject nextObject:getEObjectList()){
				compoundCommand.append(SetCommand.create(editingDomain, nextObject, getFeature(), uuid));
			}
			editingDomain.getCommandStack().execute(compoundCommand);
		}
	}
}
