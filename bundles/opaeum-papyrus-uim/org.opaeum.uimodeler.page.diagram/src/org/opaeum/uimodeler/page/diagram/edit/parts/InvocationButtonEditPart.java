package org.opaeum.uimodeler.page.diagram.edit.parts;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
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
import org.opaeum.uimodeler.common.UimFigureUtil;
import org.opaeum.uimodeler.common.figures.ActionButtonEventAdapter;
import org.opaeum.uimodeler.common.figures.CustomOperationActionFigure;
import org.opaeum.uimodeler.common.figures.HackedDefaultSizeNodeFigure;
import org.opaeum.uimodeler.page.diagram.edit.policies.InvocationButtonItemSemanticEditPolicy;
import org.opaeum.uimodeler.page.diagram.part.UimDiagramEditorPlugin;
import org.opaeum.uimodeler.page.diagram.part.UimVisualIDRegistry;

/**
 * @generated
 */
public class InvocationButtonEditPart extends ShapeNodeEditPart {
	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3033;
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
	public InvocationButtonEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new InvocationButtonItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {

		FlowLayoutEditPolicy lep = new FlowLayoutEditPolicy() {

			protected Command createAddCommand(EditPart child, EditPart after) {
				return null;
			}

			protected Command createMoveChildCommand(EditPart child,
					EditPart after) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		};
		return lep;
	}

	/**
	 * @generated NOT
	 */
	protected IFigure createNodeShape() {
		return primaryShape = new CustomOperationActionFigure(
				UimFigureUtil.getNearestComposite(getParent()));
	}

	/**
	 * @generated NOT
	 */
	public CustomOperationActionFigure getPrimaryShape() {
		return (CustomOperationActionFigure) primaryShape;
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof InvocationButtonNameEditPart) {
			((InvocationButtonNameEditPart) childEditPart)
					.setLabel(getPrimaryShape()
							.getFigureInvocationButtonNameFigure());
			return true;
		}

		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof InvocationButtonNameEditPart) {
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	/**
	 * @generated
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	/**
	 * @generated
	 */
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		return getContentPane();
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		String prefElementId = "InvocationButton";
		IPreferenceStore store = UimDiagramEditorPlugin.getInstance()
				.getPreferenceStore();
		String preferenceConstantWitdh = PreferenceInitializerForElementHelper
				.getpreferenceKey(getNotationView(), prefElementId,
						PreferenceConstantHelper.WIDTH);
		String preferenceConstantHeight = PreferenceInitializerForElementHelper
				.getpreferenceKey(getNotationView(), prefElementId,
						PreferenceConstantHelper.HEIGHT);
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(
				store.getInt(preferenceConstantWitdh),
				store.getInt(preferenceConstantHeight));

		return result;
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated NOT
	 */
	protected NodeFigure createNodeFigure() {
		IFigure shape = createNodeShape();
		NodeFigure figure = new HackedDefaultSizeNodeFigure(getPrimaryShape());
		figure.setLayoutManager(new StackLayout());
		figure.add(shape);
		new ActionButtonEventAdapter(this, getPrimaryShape());
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		if (nodeShape.getLayoutManager() == null) {
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setSpacing(5);
			nodeShape.setLayoutManager(layout);
		}
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	public IFigure getContentPane() {
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	/**
	 * @generated
	 */
	protected void setForegroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setForegroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineWidth(width);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineType(int style) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineStyle(style);
		}
	}

	/**
	 * @generated
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(UimVisualIDRegistry
				.getType(InvocationButtonNameEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	public class InvocationButtonFigure extends RectangleFigure {
		/**
		 * @generated
		 */
		private WrappingLabel fFigureInvocationButtonNameFigure;

		/**
		 * @generated
		 */
		public InvocationButtonFigure() {

			FlowLayout layoutThis = new FlowLayout();
			layoutThis.setStretchMinorAxis(false);
			layoutThis.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);

			layoutThis.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
			layoutThis.setMajorSpacing(5);
			layoutThis.setMinorSpacing(5);
			layoutThis.setHorizontal(true);

			this.setLayoutManager(layoutThis);

			createContents();
		}

		/**
		 * @generated
		 */
		private void createContents() {

			fFigureInvocationButtonNameFigure = new WrappingLabel();

			fFigureInvocationButtonNameFigure.setText("<...>");

			this.add(fFigureInvocationButtonNameFigure);

		}

		/**
		 * @generated
		 */
		public WrappingLabel getFigureInvocationButtonNameFigure() {
			return fFigureInvocationButtonNameFigure;
		}
	}

	/**
	 * @generated
	 */
	@Override
	public Object getPreferredValue(EStructuralFeature feature) {
		IPreferenceStore preferenceStore = (IPreferenceStore) getDiagramPreferencesHint()
				.getPreferenceStore();
		Object result = null;

		if (feature == NotationPackage.eINSTANCE.getLineStyle_LineColor()
				|| feature == NotationPackage.eINSTANCE
						.getFontStyle_FontColor()
				|| feature == NotationPackage.eINSTANCE
						.getFillStyle_FillColor()) {
			String prefColor = null;
			if (feature == NotationPackage.eINSTANCE.getLineStyle_LineColor()) {
				prefColor = PreferenceConstantHelper
						.getElementConstant("InvocationButton",
								PreferenceConstantHelper.COLOR_LINE);
			} else if (feature == NotationPackage.eINSTANCE
					.getFontStyle_FontColor()) {
				prefColor = PreferenceConstantHelper
						.getElementConstant("InvocationButton",
								PreferenceConstantHelper.COLOR_FONT);
			} else if (feature == NotationPackage.eINSTANCE
					.getFillStyle_FillColor()) {
				prefColor = PreferenceConstantHelper
						.getElementConstant("InvocationButton",
								PreferenceConstantHelper.COLOR_FILL);
			}
			result = FigureUtilities.RGBToInteger(PreferenceConverter.getColor(
					(IPreferenceStore) preferenceStore, prefColor));
		} else if (feature == NotationPackage.eINSTANCE
				.getFillStyle_Transparency()
				|| feature == NotationPackage.eINSTANCE.getFillStyle_Gradient()) {
			String prefGradient = PreferenceConstantHelper
					.getElementConstant("InvocationButton",
							PreferenceConstantHelper.COLOR_GRADIENT);
			GradientPreferenceConverter gradientPreferenceConverter = new GradientPreferenceConverter(
					preferenceStore.getString(prefGradient));
			if (feature == NotationPackage.eINSTANCE
					.getFillStyle_Transparency()) {
				result = new Integer(
						gradientPreferenceConverter.getTransparency());
			} else if (feature == NotationPackage.eINSTANCE
					.getFillStyle_Gradient()) {
				result = gradientPreferenceConverter.getGradientData();
			}
		}

		if (result == null) {
			result = getStructuralFeatureValue(feature);
		}
		return result;
	}
}
