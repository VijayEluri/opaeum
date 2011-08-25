package org.nakeduml.eclipse.starter;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

public class OpiumContributionFactory extends ExtensionContributionFactory{
	public OpiumContributionFactory(){
	}
	@Override
	public void createContributionItems(IServiceLocator serviceLocator,IContributionRoot additions){
		MenuManager menuManager = new MenuManager("Opium");
		additions.addContributionItem(menuManager, new Expression(){
			@Override
			public EvaluationResult evaluate(IEvaluationContext context) throws CoreException{
				ISelectionService s = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
				if(s.getSelection() instanceof IStructuredSelection){
					IStructuredSelection selection = (IStructuredSelection) s.getSelection();
					if(selection.getFirstElement() instanceof IContainer){
						if(DynamicOpiumMenu.hasUmlModels(selection) || DynamicOpiumMenu.hasConfigFile(selection)){
							return EvaluationResult.TRUE;
						}
					}
				}
				return EvaluationResult.FALSE;
			}
		});
		menuManager.add(new DynamicOpiumMenu());
	}
}