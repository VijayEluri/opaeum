package net.sf.nakeduml.seamgeneration.jsf.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.html.HtmlOutputText;

import net.sf.nakeduml.name.NameConverter;
import net.sf.nakeduml.userinteractionmetamodel.ClassifierUserInteraction;
import net.sf.nakeduml.userinteractionmetamodel.PropertyField;

import org.jboss.seam.el.SeamExpressionFactory;
import org.jboss.seam.ui.component.html.HtmlFragment;
import org.richfaces.component.html.HtmlColumn;

import com.sun.faces.el.ELContextImpl;

public class JsfListObjectColumnBuilder implements IJsfPropertyFieldColumnBuilder {

	private static final String ATTRIBUTES_THAT_ARE_SET_KEY = UIComponentBase.class.getName() + ".attributesThatAreSet";
	private PropertyField pf;
	private ClassifierUserInteraction ui;

	public JsfListObjectColumnBuilder(ClassifierUserInteraction ui, PropertyField pf) {
		super();
		this.pf = pf;
		this.ui = ui;
	}

	@Override
	public HtmlColumn createColumn() {
		HtmlColumn column = new HtmlColumn();
		
		column.setLabel(generateComponentLabel(NameConverter.decapitalize(pf.getProperty().getName())));
		column.setWidth("180px");
		
		HtmlFragment headerFragment = new HtmlFragment();
		HtmlOutputText columnHeading = new HtmlOutputText();
		ValueExpression ve = SeamExpressionFactory.INSTANCE.createValueExpression(new ELContextImpl(null), generateComponentLabel(NameConverter.decapitalize(pf
				.getProperty().getName())), Object.class);
		columnHeading.setValue(ve);
		setSettedAttributes(columnHeading, "value");
		headerFragment.getChildren().add(columnHeading);
		column.getFacets().put("header", headerFragment);
		setSettedAttributes(column, "label", "width");
		return column;
	}

	@SuppressWarnings("unchecked")
	protected void setSettedAttributes(UIComponent list, String... s) {
		list.getAttributes().put(ATTRIBUTES_THAT_ARE_SET_KEY, new ArrayList<String>());
		List<String> attributes = (List<String>) list.getAttributes().get(ATTRIBUTES_THAT_ARE_SET_KEY);
		attributes.addAll(Arrays.asList(s));
	}

	protected String generateComponentLabel(String name) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("#{messages['");
		stringBuilder.append(name);
		stringBuilder.append("']}");
		return stringBuilder.toString();
	}

	//TODO duplicated
	protected String generateJsfId(ClassifierUserInteraction e, String componentClassName) {
		return e.getName() + componentClassName + "JsfId";
	}	
}