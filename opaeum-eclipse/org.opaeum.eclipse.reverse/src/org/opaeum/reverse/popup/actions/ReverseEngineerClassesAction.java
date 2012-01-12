package org.opaeum.reverse.popup.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.opaeum.eclipse.context.OpaeumEclipseContext;
import org.opaeum.eclipse.context.OpaeumEditingContext;

public class ReverseEngineerClassesAction extends AbstractHandler implements IObjectActionDelegate{
	/**
	 * Constructor for Action1.
	 */
	IStructuredSelection selection;
	private Shell shell;
	public ReverseEngineerClassesAction(){
		super();
	}
	private static CompilationUnit parse(ICompilationUnit unit){
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
	private static CompilationUnit parse(IClassFile unit){
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException{
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveMenuSelection(event);
		Shell shell = HandlerUtil.getActiveShell(event);
		try{
			execute(selection, shell);
		}catch(JavaModelException e){
			e.printStackTrace();
		}
		return null;
	}
	protected void execute(IStructuredSelection selection,Shell shell) throws JavaModelException{
		List<ITypeBinding> types = selectTypeDeclarations(selection);
		this.selection = (IStructuredSelection) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		Collection<IFile> files = new HashSet<IFile>();
		for(IWorkbenchWindow w:PlatformUI.getWorkbench().getWorkbenchWindows()){
			for(IWorkbenchPage p:w.getPages()){
				for(IEditorReference e:p.getEditorReferences()){
					try{
						if(e.getEditorInput() instanceof FileEditorInput){
							FileEditorInput editorInput = (FileEditorInput) e.getEditorInput();
							if(editorInput.getFile().getParent().findMember("opaeum.properties") != null){
								OpaeumEclipseContext ctx = OpaeumEclipseContext.getContextFor(editorInput.getFile().getParent());
								for(IResource r:editorInput.getFile().getParent().members()){
									if(r.getName().endsWith(".uml")){
										if(ctx.getEditingContextFor((IFile) r) != null){
											files.add((IFile) r);
										}
									}
								}
							}
						}
					}catch(PartInitException e1){
						e1.printStackTrace();
					}catch(CoreException e2){
						e2.printStackTrace();
					}
				}
			}
		}
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, new LabelProvider(){
			@Override
			public String getText(Object element){
				IFile file = (IFile) element;
				return file.getProject().getName() + "/" + file.getProjectRelativePath();
			}
		});
		dialog.setElements(files.toArray());
		dialog.setTitle("Models in Workspace");
		dialog.setMessage("Select the targetmodel:");
		dialog.open();
		if(dialog.getFirstResult() != null){
			IFile file = (IFile) dialog.getFirstResult();
			OpaeumEclipseContext ctx = OpaeumEclipseContext.getContextFor(file.getParent());
			try{
				if(ctx != null){
					OpaeumEditingContext eCtx = ctx.getEditingContextFor(file);
					if(eCtx != null){
						ctx.getEmfToOpaeumSynchronizer().suspend();
						new UmlGenerator().generateUml(types, eCtx.getModel());
						ctx.getEmfToOpaeumSynchronizer().resumeAndCatchUp();
					}
				}
				file.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private List<ITypeBinding> selectTypeDeclarations(IStructuredSelection selection) throws JavaModelException{
		Object[] selections = selection.toArray();
		List<ITypeBinding> types = new ArrayList<ITypeBinding>();
		for(Object object:selections){
			addTypesIn(types, object);
		}
		return types;
	}
	protected void addTypesIn(List<ITypeBinding> types,Object object) throws JavaModelException{
		CompilationUnit cu = null;
		if(object instanceof ICompilationUnit){
			cu = parse(((ICompilationUnit) object));
		}else if(object instanceof IClassFile){
			cu = parse((IClassFile) object);
		}else if(object instanceof IPackageFragment){
			IPackageFragment pf = (IPackageFragment) object;
			for(IJavaElement c:pf.getChildren()){
				addTypesIn(types, c);
			}
		}
		if(cu != null){
			@SuppressWarnings("unchecked")
			List<AbstractTypeDeclaration> typeDeclarations = cu.types();
			for(AbstractTypeDeclaration type:typeDeclarations){
				types.add(type.resolveBinding());
			}
		}
	}
	@Override
	public void run(IAction action){
		try{
			execute(selection, shell);
		}catch(JavaModelException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void selectionChanged(IAction action,ISelection selection){
		if(selection instanceof IStructuredSelection){
			this.selection = (IStructuredSelection) selection;
		}
	}
	@Override
	public void setActivePart(IAction action,IWorkbenchPart targetPart){
		this.shell = targetPart.getSite().getShell();
	}
}