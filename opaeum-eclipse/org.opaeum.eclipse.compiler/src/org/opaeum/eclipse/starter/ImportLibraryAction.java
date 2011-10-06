package org.opaeum.eclipse.starter;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.opaeum.eclipse.ModelLibrary;
import org.opaeum.eclipse.context.OpaeumEclipseContext;

public class ImportLibraryAction extends AbstractOpaeumAction{
	private ModelLibrary library;
	public ImportLibraryAction(IStructuredSelection selection,ModelLibrary lib){
		super(selection, "Import " + lib.getName());
		this.library=lib;
	}
	@Override
	public void run(){
		final Model model = (Model) selection.getFirstElement();
		final OpaeumEclipseContext currentContext = OpaeumEclipseContext.getCurrentContext();
		EditingDomain ed = currentContext.getEditingDomain();
		PackageImport pi = UMLFactory.eINSTANCE.createPackageImport();
		Resource resource = model.eResource().getResourceSet().getResource(library.getUri(), true);
		Model library = (Model) resource.getContents().get(0);
		pi.setImportedPackage(library);
		if(!model.getImportedPackages().contains(library)){
			ed.getCommandStack().execute(AddCommand.create(ed, model, UMLPackage.eINSTANCE.getPackageImport(), pi));
		}
	}
}