package org.opaeum.uimodeler.perspective.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.providers.IViewProvider;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateDiagramViewOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateEdgeViewOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateNodeViewOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateViewForKindOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateViewOperation;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.papyrus.infra.gmfdiag.preferences.utils.GradientPreferenceConverter;
import org.eclipse.papyrus.infra.gmfdiag.preferences.utils.PreferenceConstantHelper;
import org.eclipse.papyrus.uml.diagram.common.helper.PreferenceInitializerForElementHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.opaeum.uimodeler.perspective.diagram.edit.parts.EditorConfigurationEditPart;
import org.opaeum.uimodeler.perspective.diagram.edit.parts.ExplorerConfigurationEditPart;
import org.opaeum.uimodeler.perspective.diagram.edit.parts.PerspectiveConfigurationEditPart;
import org.opaeum.uimodeler.perspective.diagram.edit.parts.PropertiesConfigurationEditPart;
import org.opaeum.uimodeler.perspective.diagram.part.UimVisualIDRegistry;

/**
 * @generated
 */
public class UimViewProvider extends AbstractProvider implements IViewProvider{
	/**
	 * @generated
	 */
	public final boolean provides(IOperation operation){
		if(operation instanceof CreateViewForKindOperation){
			return provides((CreateViewForKindOperation) operation);
		}
		assert operation instanceof CreateViewOperation;
		if(operation instanceof CreateDiagramViewOperation){
			return provides((CreateDiagramViewOperation) operation);
		}else if(operation instanceof CreateEdgeViewOperation){
			return provides((CreateEdgeViewOperation) operation);
		}else if(operation instanceof CreateNodeViewOperation){
			return provides((CreateNodeViewOperation) operation);
		}
		return false;
	}
	/**
	 * @generated
	 */
	protected boolean provides(CreateViewForKindOperation op){
		/*
		 if (op.getViewKind() == Node.class)
		 return getNodeViewClass(op.getSemanticAdapter(), op.getContainerView(), op.getSemanticHint()) != null;
		 if (op.getViewKind() == Edge.class)
		 return getEdgeViewClass(op.getSemanticAdapter(), op.getContainerView(), op.getSemanticHint()) != null;
		 */
		// check Diagram Type should be the class diagram
		String modelID = UimVisualIDRegistry.getModelID(op.getContainerView());
		if(!getDiagramProvidedId().equals(modelID)){
			return false;
		}
		int visualID = UimVisualIDRegistry.getVisualID(op.getSemanticHint());
		if(Node.class.isAssignableFrom(op.getViewKind())){
			return UimVisualIDRegistry.canCreateNode(op.getContainerView(), visualID);
		}
		return true;
	}
	/**
	 * @generated
	 */
	protected String getDiagramProvidedId(){
		/*
		 * Indicates for which diagram this provider works for.
		 * <p>
		 * This method can be overloaded when diagram editor inherits from another one, but should never be <code>null</code>
		 * </p>
		 * 
		 * @return the unique identifier of the diagram for which views are provided.
		 */
		return PerspectiveConfigurationEditPart.MODEL_ID;
	}
	/**
	 * @generated
	 */
	protected boolean provides(CreateDiagramViewOperation op){
		return PerspectiveConfigurationEditPart.MODEL_ID.equals(op.getSemanticHint())
				&& UimVisualIDRegistry.getDiagramVisualID(getSemanticElement(op.getSemanticAdapter())) != -1;
	}
	/**
	 * @generated
	 */
	protected boolean provides(CreateNodeViewOperation op){
		if(op.getContainerView() == null){
			return false;
		}
		IElementType elementType = getSemanticElementType(op.getSemanticAdapter());
		EObject domainElement = getSemanticElement(op.getSemanticAdapter());
		int visualID;
		if(op.getSemanticHint() == null){
			// Semantic hint is not specified. Can be a result of call from CanonicalEditPolicy.
			// In this situation there should be NO elementType, visualID will be determined
			// by VisualIDRegistry.getNodeVisualID() for domainElement.
			if(elementType != null || domainElement == null){
				return false;
			}
			visualID = UimVisualIDRegistry.getNodeVisualID(op.getContainerView(), domainElement);
		}else{
			visualID = UimVisualIDRegistry.getVisualID(op.getSemanticHint());
			if(elementType != null){
				if(!UimElementTypes.isKnownElementType(elementType) || (!(elementType instanceof IHintedType))){
					return false; // foreign element type
				}
				String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
				if(!op.getSemanticHint().equals(elementTypeHint)){
					return false; // if semantic hint is specified it should be the same as in element type
				}
				if(domainElement != null && visualID != UimVisualIDRegistry.getNodeVisualID(op.getContainerView(), domainElement)){
					return false; // visual id for node EClass should match visual id from element type
				}
			}else{
				if(!PerspectiveConfigurationEditPart.MODEL_ID.equals(UimVisualIDRegistry.getModelID(op.getContainerView()))){
					return false; // foreign diagram
				}
				switch(visualID){
				case EditorConfigurationEditPart.VISUAL_ID:
				case PropertiesConfigurationEditPart.VISUAL_ID:
				case ExplorerConfigurationEditPart.VISUAL_ID:
					if(domainElement == null || visualID != UimVisualIDRegistry.getNodeVisualID(op.getContainerView(), domainElement)){
						return false; // visual id in semantic hint should match visual id for domain element
					}
					break;
				default:
					return false;
				}
			}
		}
		return EditorConfigurationEditPart.VISUAL_ID == visualID || PropertiesConfigurationEditPart.VISUAL_ID == visualID
				|| ExplorerConfigurationEditPart.VISUAL_ID == visualID;
	}
	/**
	 * @generated
	 */
	protected boolean provides(CreateEdgeViewOperation op){
		IElementType elementType = getSemanticElementType(op.getSemanticAdapter());
		if(!UimElementTypes.isKnownElementType(elementType) || (!(elementType instanceof IHintedType))){
			return false; // foreign element type
		}
		String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
		if(elementTypeHint == null || (op.getSemanticHint() != null && !elementTypeHint.equals(op.getSemanticHint()))){
			return false; // our hint is visual id and must be specified, and it should be the same as in element type
		}
		int visualID = UimVisualIDRegistry.getVisualID(elementTypeHint);
		EObject domainElement = getSemanticElement(op.getSemanticAdapter());
		if(domainElement != null && visualID != UimVisualIDRegistry.getLinkWithClassVisualID(domainElement)){
			return false; // visual id for link EClass should match visual id from element type
		}
		return true;
	}
	/**
	 * @generated
	 */
	public Diagram createDiagram(IAdaptable semanticAdapter,String diagramKind,PreferencesHint preferencesHint){
		Diagram diagram = NotationFactory.eINSTANCE.createDiagram();
		diagram.getStyles().add(NotationFactory.eINSTANCE.createDiagramStyle());
		diagram.setType(PerspectiveConfigurationEditPart.MODEL_ID);
		diagram.setElement(getSemanticElement(semanticAdapter));
		diagram.setMeasurementUnit(MeasurementUnit.PIXEL_LITERAL);
		return diagram;
	}
	/**
	 * @generated
	 */
	public Node createNode(IAdaptable semanticAdapter,View containerView,String semanticHint,int index,boolean persisted,
			PreferencesHint preferencesHint){
		final EObject domainElement = getSemanticElement(semanticAdapter);
		final int visualID;
		if(semanticHint == null){
			visualID = UimVisualIDRegistry.getNodeVisualID(containerView, domainElement);
		}else{
			visualID = UimVisualIDRegistry.getVisualID(semanticHint);
		}
		switch(visualID){
		case EditorConfigurationEditPart.VISUAL_ID:
			return createEditorConfiguration_2001(domainElement, containerView, index, persisted, preferencesHint);
		case PropertiesConfigurationEditPart.VISUAL_ID:
			return createPropertiesConfiguration_2002(domainElement, containerView, index, persisted, preferencesHint);
		case ExplorerConfigurationEditPart.VISUAL_ID:
			return createExplorerConfiguration_2003(domainElement, containerView, index, persisted, preferencesHint);
		}
		// can't happen, provided #provides(CreateNodeViewOperation) is correct
		return null;
	}
	/**
	 * @generated
	 */
	public Edge createEdge(IAdaptable semanticAdapter,View containerView,String semanticHint,int index,boolean persisted,
			PreferencesHint preferencesHint){
		IElementType elementType = getSemanticElementType(semanticAdapter);
		String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
		switch(UimVisualIDRegistry.getVisualID(elementTypeHint)){
		}
		// can never happen, provided #provides(CreateEdgeViewOperation) is correct
		return null;
	}
	/**
	 * @generated
	 */
	public Node createEditorConfiguration_2001(EObject domainElement,View containerView,int index,boolean persisted,
			PreferencesHint preferencesHint){
		Shape node = NotationFactory.eINSTANCE.createShape();
		node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
		node.setType(UimVisualIDRegistry.getType(EditorConfigurationEditPart.VISUAL_ID));
		ViewUtil.insertChildView(containerView, node, index, persisted);
		node.setElement(domainElement);
		stampShortcut(containerView, node);
		// initializeFromPreferences 
		final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();
		PreferenceInitializerForElementHelper.initForegroundFromPrefs(node, prefStore, "EditorConfiguration");
		PreferenceInitializerForElementHelper.initFontStyleFromPrefs(node, prefStore, "EditorConfiguration");
		PreferenceInitializerForElementHelper.initBackgroundFromPrefs(node, prefStore, "EditorConfiguration");
		return node;
	}
	/**
	 * @generated
	 */
	public Node createPropertiesConfiguration_2002(EObject domainElement,View containerView,int index,boolean persisted,
			PreferencesHint preferencesHint){
		Shape node = NotationFactory.eINSTANCE.createShape();
		node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
		node.setType(UimVisualIDRegistry.getType(PropertiesConfigurationEditPart.VISUAL_ID));
		ViewUtil.insertChildView(containerView, node, index, persisted);
		node.setElement(domainElement);
		stampShortcut(containerView, node);
		// initializeFromPreferences 
		final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();
		PreferenceInitializerForElementHelper.initForegroundFromPrefs(node, prefStore, "PropertiesConfiguration");
		PreferenceInitializerForElementHelper.initFontStyleFromPrefs(node, prefStore, "PropertiesConfiguration");
		PreferenceInitializerForElementHelper.initBackgroundFromPrefs(node, prefStore, "PropertiesConfiguration");
		return node;
	}
	/**
	 * @generated
	 */
	public Node createExplorerConfiguration_2003(EObject domainElement,View containerView,int index,boolean persisted,
			PreferencesHint preferencesHint){
		Shape node = NotationFactory.eINSTANCE.createShape();
		node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
		node.setType(UimVisualIDRegistry.getType(ExplorerConfigurationEditPart.VISUAL_ID));
		ViewUtil.insertChildView(containerView, node, index, persisted);
		node.setElement(domainElement);
		stampShortcut(containerView, node);
		// initializeFromPreferences 
		final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();
		PreferenceInitializerForElementHelper.initForegroundFromPrefs(node, prefStore, "ExplorerConfiguration");
		PreferenceInitializerForElementHelper.initFontStyleFromPrefs(node, prefStore, "ExplorerConfiguration");
		PreferenceInitializerForElementHelper.initBackgroundFromPrefs(node, prefStore, "ExplorerConfiguration");
		return node;
	}
	/**
	 * @generated
	 */
	protected void stampShortcut(View containerView,Node target){
		if(!PerspectiveConfigurationEditPart.MODEL_ID.equals(UimVisualIDRegistry.getModelID(containerView))){
			EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
			shortcutAnnotation.setSource("Shortcut"); //$NON-NLS-1$
			shortcutAnnotation.getDetails().put("modelID", PerspectiveConfigurationEditPart.MODEL_ID); //$NON-NLS-1$
			target.getEAnnotations().add(shortcutAnnotation);
		}
	}
	/**
	 * @generated
	 */
	protected EObject getSemanticElement(IAdaptable semanticAdapter){
		if(semanticAdapter == null){
			return null;
		}
		EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
		if(eObject != null){
			return EMFCoreUtil.resolve(TransactionUtil.getEditingDomain(eObject), eObject);
		}
		return null;
	}
	/**
	 * @generated
	 */
	protected IElementType getSemanticElementType(IAdaptable semanticAdapter){
		if(semanticAdapter == null){
			return null;
		}
		return (IElementType) semanticAdapter.getAdapter(IElementType.class);
	}
	/**
	 * @generated
	 */
	private void initFontStyleFromPrefs(View view,final IPreferenceStore store,String elementName){
		String fontConstant = PreferenceConstantHelper.getElementConstant(elementName, PreferenceConstantHelper.FONT);
		String fontColorConstant = PreferenceConstantHelper.getElementConstant(elementName, PreferenceConstantHelper.COLOR_FONT);
		FontStyle viewFontStyle = (FontStyle) view.getStyle(NotationPackage.Literals.FONT_STYLE);
		if(viewFontStyle != null){
			FontData fontData = PreferenceConverter.getFontData(store, fontConstant);
			viewFontStyle.setFontName(fontData.getName());
			viewFontStyle.setFontHeight(fontData.getHeight());
			viewFontStyle.setBold((fontData.getStyle() & SWT.BOLD) != 0);
			viewFontStyle.setItalic((fontData.getStyle() & SWT.ITALIC) != 0);
			org.eclipse.swt.graphics.RGB fontRGB = PreferenceConverter.getColor(store, fontColorConstant);
			viewFontStyle.setFontColor(FigureUtilities.RGBToInteger(fontRGB).intValue());
		}
	}
	/**
	 * @generated
	 */
	private void initForegroundFromPrefs(View view,final IPreferenceStore store,String elementName){
		String lineColorConstant = PreferenceConstantHelper.getElementConstant(elementName, PreferenceConstantHelper.COLOR_LINE);
		org.eclipse.swt.graphics.RGB lineRGB = PreferenceConverter.getColor(store, lineColorConstant);
		ViewUtil.setStructuralFeatureValue(view, NotationPackage.eINSTANCE.getLineStyle_LineColor(), FigureUtilities.RGBToInteger(lineRGB));
	}
	/**
	 * @generated
	 */
	private void initBackgroundFromPrefs(View view,final IPreferenceStore store,String elementName){
		String fillColorConstant = PreferenceConstantHelper.getElementConstant(elementName, PreferenceConstantHelper.COLOR_FILL);
		String gradientColorConstant = PreferenceConstantHelper.getElementConstant(elementName, PreferenceConstantHelper.COLOR_GRADIENT);
		String gradientPolicyConstant = PreferenceConstantHelper.getElementConstant(elementName, PreferenceConstantHelper.GRADIENT_POLICY);
		org.eclipse.swt.graphics.RGB fillRGB = PreferenceConverter.getColor(store, fillColorConstant);
		ViewUtil.setStructuralFeatureValue(view, NotationPackage.eINSTANCE.getFillStyle_FillColor(), FigureUtilities.RGBToInteger(fillRGB));
		FillStyle fillStyle = (FillStyle) view.getStyle(NotationPackage.Literals.FILL_STYLE);
		fillStyle.setFillColor(FigureUtilities.RGBToInteger(fillRGB).intValue());
		;
		if(store.getBoolean(gradientPolicyConstant)){
			GradientPreferenceConverter gradientPreferenceConverter = new GradientPreferenceConverter(store.getString(gradientColorConstant));
			fillStyle.setGradient(gradientPreferenceConverter.getGradientData());
			fillStyle.setTransparency(gradientPreferenceConverter.getTransparency());
		}
	}
}
