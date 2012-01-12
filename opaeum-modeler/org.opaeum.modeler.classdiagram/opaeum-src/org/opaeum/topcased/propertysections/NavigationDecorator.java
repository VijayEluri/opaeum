package org.opaeum.topcased.propertysections;

import java.lang.reflect.Field;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.opaeum.topcased.uml.editor.OpaeumEditor;

public class NavigationDecorator{
	private MouseListener mouseListener;
	private PaintListener paintListener;
	private EObjectNavigationSource source;
	public NavigationDecorator(EObjectNavigationSource source){
		super();
		this.source = source;
	}
	public void refresh(){
		Font font = source.getLabelCombo().getFont();
		if(source.getEObjectToGoTo() != null){
			setFontStyle(font, SWT.UNDERLINE_SINGLE);
			source.getLabelCombo().addPaintListener(getPaintListener());
			source.getLabelCombo().setForeground(ColorConstants.blue);
			source.getLabelCombo().addMouseListener(getMouseListener());
			source.getLabelCombo().setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_HAND));
		}else{
			setFontStyle(font, SWT.NORMAL);
			source.getLabelCombo().setForeground(ColorConstants.black);
			source.getLabelCombo().removeMouseListener(getMouseListener());
			source.getLabelCombo().removePaintListener(getPaintListener());
		}
		source.getLabelCombo().redraw();
	}
	protected void setFontStyle(Font font,int normal){
		FontData fontData = font.getFontData()[0];
		font.getFontData()[0]=new FontData(fontData.getName(), fontData.getHeight(), normal);
	}
	protected PaintListener getPaintListener(){
		if(this.paintListener == null){
			this.paintListener = new PaintListener(){
				@Override
				public void paintControl(PaintEvent e){
					Rectangle bounds = source.getLabelCombo().getBounds();
					e.gc.setForeground(ColorConstants.blue);
					FontData fd = source.getLabelCombo().getFont().getFontData()[0];
					int y1 = bounds.y + Math.round(fd.getHeight());
					e.gc.drawLine(bounds.x - 3, y1, bounds.x + source.getLabelCombo().getText().length() * 5, y1);
				}
			};
		}
		return this.paintListener;
	}
	protected MouseListener getMouseListener(){
		if(this.mouseListener == null){
			this.mouseListener = new MouseAdapter(){
				public void mouseDown(MouseEvent e){
					EObject featureValue = (EObject) source.getEObjectToGoTo();
					IWorkbenchPage activePage = source.getActivePage();
					goToEObject(featureValue, activePage);
				}
			};
		}
		return this.mouseListener;
	}
	public static void goToPreviousEObject(IWorkbenchPage activePage){
		OpaeumEditor nakedUmlEditor = (OpaeumEditor) activePage.getActiveEditor();
		EObject featureValue = nakedUmlEditor.popSelection();
		goToEObject(featureValue, activePage, nakedUmlEditor);
	}
	public static void goToEObject(EObject featureValue,IWorkbenchPage activePage){
		if(featureValue != null){
			OpaeumEditor nakedUmlEditor = (OpaeumEditor) activePage.getActiveEditor();
			nakedUmlEditor.pushSelection(featureValue);
			nakedUmlEditor.gotoEObject(featureValue);
			goToEObject(featureValue, activePage, nakedUmlEditor);
		}
	}
	private static void goToEObject(EObject featureValue,IWorkbenchPage activePage,OpaeumEditor nakedUmlEditor){
		IViewReference[] viewReferences = activePage.getViewReferences();
		for(IViewReference iViewReference:viewReferences){
			IViewPart view = iViewReference.getView(true);
			if(view instanceof PropertySheet){
				PropertySheet sheet = (PropertySheet) view;
				IPage currentPage = sheet.getCurrentPage();
				if(currentPage instanceof TabbedPropertySheetPage){
					((TabbedPropertySheetPage) currentPage).selectionChanged(nakedUmlEditor, new StructuredSelection(featureValue));
					break;
				}
			}
		}
	}
}