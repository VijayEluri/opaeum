package org.opaeum.uimodeler.perspective.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.papyrus.infra.gmfdiag.preferences.utils.GradientPreferenceConverter;
import org.eclipse.papyrus.infra.gmfdiag.preferences.utils.PreferenceConstantHelper;
import org.eclipse.papyrus.uml.diagram.common.helper.PreferenceInitializerForElementHelper;
import org.eclipse.swt.graphics.Color;
import org.opaeum.uimodeler.perspective.diagram.edit.policies.ExplorerConfigurationItemSemanticEditPolicy;
import org.opaeum.uimodeler.perspective.diagram.part.UimPerspectiveDiagramEditorPlugin;

/**
 * @generated
 */
public class ExplorerConfigurationEditPart extends ShapeNodeEditPart{
	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 2003;
	/**
	 * @generated
	 */
	protected IFigure contentPane;
	/**
	 * @generated
	 */
	protected IFigure primaryShape;
	/**
	 * @generated
	 */
	public ExplorerConfigurationEditPart(View view){
		super(view);
	}
	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies(){
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ExplorerConfigurationItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
	}
	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy(){
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy(){
			protected EditPolicy createChildEditPolicy(EditPart child){
				EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
				if(result == null){
					result = new NonResizableEditPolicy();
				}
				return result;
			}
			protected Command getMoveChildrenCommand(Request request){
				return null;
			}
			protected Command getCreateCommand(CreateRequest request){
				return null;
			}
		};
		return lep;
	}
	/**
	 * @generated
	 */
	protected IFigure createNodeShape(){
		return primaryShape = new RectangleFigure();
	}
	/**
	 * @generated
	 */
	public RectangleFigure getPrimaryShape(){
		return (RectangleFigure) primaryShape;
	}
	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate(){
		String prefElementId = "ExplorerConfiguration";
		IPreferenceStore store = UimPerspectiveDiagramEditorPlugin.getInstance().getPreferenceStore();
		String preferenceConstantWitdh = PreferenceInitializerForElementHelper.getpreferenceKey(getNotationView(), prefElementId,
				PreferenceConstantHelper.WIDTH);
		String preferenceConstantHeight = PreferenceInitializerForElementHelper.getpreferenceKey(getNotationView(), prefElementId,
				PreferenceConstantHelper.HEIGHT);
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(store.getInt(preferenceConstantWitdh), store.getInt(preferenceConstantHeight));
		return result;
	}
	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	protected NodeFigure createNodeFigure(){
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}
	/**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape){
		return nodeShape; // use nodeShape itself as contentPane
	}
	/**
	 * @generated
	 */
	public IFigure getContentPane(){
		if(contentPane != null){
			return contentPane;
		}
		return super.getContentPane();
	}
	/**
	 * @generated
	 */
	protected void setForegroundColor(Color color){
		if(primaryShape != null){
			primaryShape.setForegroundColor(color);
		}
	}
	/**
	 * @generated
	 */
	protected void setLineWidth(int width){
		if(primaryShape instanceof Shape){
			((Shape) primaryShape).setLineWidth(width);
		}
	}
	/**
	 * @generated
	 */
	protected void setLineType(int style){
		if(primaryShape instanceof Shape){
			((Shape) primaryShape).setLineStyle(style);
		}
	}
	/**
	 * @generated
	 */
	@Override
	public Object getPreferredValue(EStructuralFeature feature){
		IPreferenceStore preferenceStore = (IPreferenceStore) getDiagramPreferencesHint().getPreferenceStore();
		Object result = null;
		if(feature == NotationPackage.eINSTANCE.getLineStyle_LineColor() || feature == NotationPackage.eINSTANCE.getFontStyle_FontColor()
				|| feature == NotationPackage.eINSTANCE.getFillStyle_FillColor()){
			String prefColor = null;
			if(feature == NotationPackage.eINSTANCE.getLineStyle_LineColor()){
				prefColor = PreferenceConstantHelper.getElementConstant("ExplorerConfiguration", PreferenceConstantHelper.COLOR_LINE);
			}else if(feature == NotationPackage.eINSTANCE.getFontStyle_FontColor()){
				prefColor = PreferenceConstantHelper.getElementConstant("ExplorerConfiguration", PreferenceConstantHelper.COLOR_FONT);
			}else if(feature == NotationPackage.eINSTANCE.getFillStyle_FillColor()){
				prefColor = PreferenceConstantHelper.getElementConstant("ExplorerConfiguration", PreferenceConstantHelper.COLOR_FILL);
			}
			result = FigureUtilities.RGBToInteger(PreferenceConverter.getColor((IPreferenceStore) preferenceStore, prefColor));
		}else if(feature == NotationPackage.eINSTANCE.getFillStyle_Transparency()
				|| feature == NotationPackage.eINSTANCE.getFillStyle_Gradient()){
			String prefGradient = PreferenceConstantHelper.getElementConstant("ExplorerConfiguration", PreferenceConstantHelper.COLOR_GRADIENT);
			GradientPreferenceConverter gradientPreferenceConverter = new GradientPreferenceConverter(preferenceStore.getString(prefGradient));
			if(feature == NotationPackage.eINSTANCE.getFillStyle_Transparency()){
				result = new Integer(gradientPreferenceConverter.getTransparency());
			}else if(feature == NotationPackage.eINSTANCE.getFillStyle_Gradient()){
				result = gradientPreferenceConverter.getGradientData();
			}
		}
		if(result == null){
			result = getStructuralFeatureValue(feature);
		}
		return result;
	}
}
