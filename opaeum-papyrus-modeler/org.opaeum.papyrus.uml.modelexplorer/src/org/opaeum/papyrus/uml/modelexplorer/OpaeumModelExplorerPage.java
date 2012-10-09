package org.opaeum.papyrus.uml.modelexplorer;

import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.utils.EditorUtils;
import org.eclipse.papyrus.views.modelexplorer.ModelExplorerPage;
import org.eclipse.papyrus.views.modelexplorer.ModelExplorerView;
import org.eclipse.papyrus.views.modelexplorer.core.ui.pagebookview.ViewPartPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.opaeum.papyrus.editor.OpaeumMultiDiagramEditor;

public class OpaeumModelExplorerPage extends ModelExplorerPage{
	public OpaeumModelExplorerPage(){
	}
	protected IViewPart createViewer(IWorkbenchPart part){
		return new ModelExplorerView((IMultiDiagramEditor) part){
			private IPropertySheetPage propertySheetPage;
			public Object getAdapter(Class adapter){
				if(IPropertySheetPage.class.equals(adapter)){
					return getPropertySheetPage();
				}else{
					return super.getAdapter(adapter);
				}
			}
			private IPropertySheetPage getPropertySheetPage(){
				final IMultiDiagramEditor multiDiagramEditor = EditorUtils.getMultiDiagramEditor();
				if(multiDiagramEditor != null){
					if(propertySheetPage == null){
						if(multiDiagramEditor instanceof ITabbedPropertySheetPageContributor){
							ITabbedPropertySheetPageContributor contributor = (ITabbedPropertySheetPageContributor) multiDiagramEditor;
							this.propertySheetPage = new OpaeumMultiDiagramEditor.OpaeumTabbedPropertySheet(contributor);
						}
					}
					return propertySheetPage;
				}
				return null;
			}
		};
	}
}
