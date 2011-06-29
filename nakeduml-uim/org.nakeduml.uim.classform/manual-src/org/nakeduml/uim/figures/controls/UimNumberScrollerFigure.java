package org.nakeduml.uim.figures.controls;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JSpinner;
import javax.swing.border.BevelBorder;

import org.eclipse.draw2d.ArrowButton;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.draw2d.ui.figures.RectangularDropShadowLineBorder;
import org.topcased.draw2d.figures.ILabel;
import org.topcased.draw2d.figures.Label;

public class UimNumberScrollerFigure extends Figure implements IControlFigure{
	private Label textField;
	private ArrowButton upAarrowButton;
	private ArrowButton downAarrowButton;
	private JSpinner spinner = new JSpinner();
	public UimNumberScrollerFigure(){
		super();
		minSize = new Dimension(10, 10);
		final GridLayout gl = new GridLayout(2, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayoutManager(gl);
		textField = new Label();
		add(textField, new GridData(GridData.CENTER, GridData.CENTER, true, false));
		Figure scroller = new Figure();
		GridLayout scrollerLayout = new GridLayout(1, false);
		scrollerLayout.marginHeight = 0;
		scrollerLayout.marginWidth = 0;
		scrollerLayout.verticalSpacing = 0;
		scrollerLayout.horizontalSpacing = 0;
		scroller.setLayoutManager(scrollerLayout);
		scroller.setPreferredSize(new Dimension(20, 20));
		upAarrowButton = new ArrowButton(ArrowButton.NORTH);
		upAarrowButton.setPreferredSize(new Dimension(20, 10));
		scroller.add(upAarrowButton);
		downAarrowButton = new ArrowButton(ArrowButton.SOUTH);
		downAarrowButton.setPreferredSize(new Dimension(20, 10));
		scroller.add(downAarrowButton);
		add(scroller, new GridData(GridData.END, GridData.END, true, true));
		setBorder(new RectangularDropShadowLineBorder(1));
	}
	@Override
	public void paint(Graphics graphics){
		spinner.setBounds(new Rectangle(getBounds().x + 2, getBounds().y + 2, getBounds().width - 4, getBounds().height - 4));
		GraphicsBridge g2 = GraphicsBridge.buildBridge(graphics, this, spinner);
		spinner.getEditor().setBackground(new Color(getBackgroundColor().getRed(), getBackgroundColor().getGreen(), getBackgroundColor().getBlue()));
		spinner.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(155, 155, 155), new Color(200, 200, 200)));
		spinner.doLayout();
		spinner.paint(g2);
		graphics.popState();
	}
	@Override
	public ILabel getBindingLabel(){
		return textField;
	}
}