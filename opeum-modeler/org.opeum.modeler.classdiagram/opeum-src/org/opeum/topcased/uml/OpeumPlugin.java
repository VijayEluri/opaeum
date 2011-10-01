package org.opeum.topcased.uml;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.uml2.uml.NamedElement;
import org.opeum.eclipse.context.OpeumEclipseContext;
import org.opeum.topcased.uml.editor.OpeumEditor;
import org.topcased.modeler.uml.UMLPlugin;

public class OpeumPlugin extends UMLPlugin{
	@Override
	protected void initializeImageRegistry(ImageRegistry reg){
ImageDescriptor img;
		//		Image img = new Image(getWorkbench().getDisplay(), 32, 32);
//		GC gc = new GC(img);
//		SVGFigure svgFigure = new SVGFigure();
//		svgFigure.setOpaque(false);
//		svgFigure.setSize(32, 32);
//		svgFigure.setURI(getBundle().getEntry("icons/objsvg/Process.svg").toString());
//		SWTGraphics graphics = new SWTGraphics(gc);
//		svgFigure.paint(graphics);
//		ImageData data = img.getImageData();
//		data.transparentPixel = 16777215;
//		ImageData maskData = data.getTransparencyMask();
//		img = new Image(getWorkbench().getDisplay(), data, maskData);
//		reg.put("Actor", img);
	}
	public static void saveAllOpenFilesIn(final OpeumEclipseContext currentContext,final IProgressMonitor monitor){
		if(OpeumPlugin.getActivePage() != null){
			IEditorReference[] editorReferences = OpeumPlugin.getActivePage().getEditorReferences();
			for(IEditorReference r:editorReferences){
				IEditorPart ed = r.getEditor(false);
				if(ed instanceof OpeumEditor){
					if(((OpeumEditor) ed).getCurrentIFile().getParent().equals(currentContext.getUmlDirectory())){
						ed.doSave(new SubProgressMonitor(monitor, 20 / editorReferences.length));
					}
				}
			}
		}
	}
	public static OpeumEditor findOpeumEditor(NamedElement modelElement){
		OpeumEditor e = null;
		IWorkbench workbench = getDefault().getWorkbench();
		IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
		outer:for(IWorkbenchWindow w:workbenchWindows){
			IWorkbenchPage[] pages = w.getPages();
			for(IWorkbenchPage activePage:pages){
				for(IEditorReference er:activePage.getEditorReferences()){
					IEditorPart curEditor = er.getEditor(false);
					if(curEditor instanceof OpeumEditor){
						IFileEditorInput input = (IFileEditorInput) curEditor.getEditorInput();
						String lastSegment = input.getFile().getLocation().removeFileExtension().lastSegment();
						e = (OpeumEditor) curEditor;
						if(lastSegment.equals(modelElement.eResource().getURI().trimFileExtension().lastSegment())){
							break outer;
						}
					}
				}
			}
		}
		return e;
	}
}
