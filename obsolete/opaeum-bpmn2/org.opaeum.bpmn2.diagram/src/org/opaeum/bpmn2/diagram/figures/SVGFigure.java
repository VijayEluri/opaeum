package org.opaeum.bpmn2.diagram.figures;

import java.io.InputStream;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.SVGImageConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.opaeum.bpmn2.modeleditor.Bpmn2Plugin;

public class SVGFigure extends Figure{
	private String imagePath;
	public SVGFigure(String string){
		imagePath = string;
	}
	@Override
	public void paint(Graphics graphics){
		SVGImageConverter ic = new SVGImageConverter();
		RenderedImageKey inf = new RenderedImageKey();
		inf.setValues(getBounds().width, getBounds().height, false, true, getBackgroundColor().getRGB(), null);
		InputStream str;
		try{
			str = Bpmn2Plugin.getDefault().getBundle().getEntry(imagePath).openStream();
			Image img = ic.renderSVGtoSWTImage(str, inf);
			int transparencyType = img.getImageData().getTransparencyType() - SWT.TRANSPARENCY_PIXEL;
			PaletteData palette = new PaletteData(new RGB[]{
					getBackgroundColor().getRGB(),ColorConstants.black.getRGB()
			});
			img.getImageData().palette = palette;
			img.getImageData().transparentPixel = 0;
			graphics.drawImage(img, getBounds().x, getBounds().y);
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
