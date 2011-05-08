package org.nakeduml.uim.figures.controls;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JComponent;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gmf.runtime.common.ui.services.action.global.GetGlobalActionHandlerOperation;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.internal.image.JPEGDecoder;
import org.eclipse.ui.internal.Workbench;

import sun.awt.X11GraphicsConfig;
import sun.awt.X11GraphicsEnvironment;
import sun.awt.image.ByteArrayImageSource;
import sun.awt.image.GifImageDecoder;
import sun.awt.image.ToolkitImage;
import sun.awt.image.codec.JPEGImageEncoderImpl;
import sun.awt.resources.awt;
import sun.swing.BakedArrayList;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGHuffmanTable;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGQTable;
import com.sun.imageio.plugins.gif.GIFImageWriter;
import com.sun.imageio.plugins.gif.GIFImageWriterSpi;

public class GraphicsBridge extends java.awt.Graphics{
	private final Graphics g;
	private Color color;
	private Rectangle clipBounds;
	private Shape clip;
	private Font font;
	boolean isPopped;
	public GraphicsBridge(Graphics g){
		this.g = g;
	}
	@Override
	public void translate(int x,int y){
		System.out.println("GraphicsBridge.translate()");
		g.translate(x, y);
		getClipBounds().x = x;
		getClipBounds().y = y;
	}
	@Override
	public void setXORMode(Color c){
		System.out.println("GraphicsBridge.setXORMode()");
		g.setXORMode(true);
		g.setForegroundColor(new org.eclipse.swt.graphics.Color(Workbench.getInstance().getDisplay(), c.getRed(), c.getGreen(), c.getBlue()));
	}
	@Override
	public void setPaintMode(){
		System.out.println("GraphicsBridge.setPaintMode()");
		g.setXORMode(false);
	}
	@Override
	public void setFont(Font font){
		System.out.println("GraphicsBridge.setFont()");
		this.font = font;
		g.setFont(new org.eclipse.swt.graphics.Font(Workbench.getInstance().getDisplay(), font.getFontName(), font.getSize(), font.getStyle()));
	}
	@Override
	public void setColor(Color c){
		System.out.println("GraphicsBridge.setColor()");
		this.color = c;
		if(c != null){
			g.setForegroundColor(new org.eclipse.swt.graphics.Color(Workbench.getInstance().getDisplay(), c.getRed(), c.getGreen(), c.getBlue()));
			g.setBackgroundColor(new org.eclipse.swt.graphics.Color(Workbench.getInstance().getDisplay(), c.getRed(), c.getGreen(), c.getBlue()));
		}else{
			System.out.println();
		}
	}
	@Override
	public void setClip(int x,int y,int width,int height){
		System.out.println("GraphicsBridge.setClip()");
		this.clipBounds = new Rectangle(x, y, width, height);
		g.setClip(new org.eclipse.draw2d.geometry.Rectangle(x, y, width, height));
	}
	@Override
	public void setClip(Shape clip){
		System.out.println("GraphicsBridge.setClip()");
		this.clip = clip;
	}
	@Override
	public FontMetrics getFontMetrics(Font f){
		final org.eclipse.swt.graphics.FontMetrics fontMetrics = g.getFontMetrics();
		System.out.println("GraphicsBridge.getFontMetrics()");
		return new FontMetrics(f){
			@Override
			public int getLeading(){
				return fontMetrics.getLeading();
			}
			@Override
			public int getAscent(){
				return fontMetrics.getAscent();
			}
			@Override
			public int getDescent(){
				return fontMetrics.getDescent();
			}
			@Override
			public int stringWidth(String str){
				return fontMetrics.getAverageCharWidth() * str.length();
			}
			@Override
			public int getHeight(){
				return fontMetrics.getHeight();
			}
		};
	}
	@Override
	public Font getFont(){
		if(this.font == null){
			FontData fontData = g.getFont().getFontData()[0];
			this.font = new Font(fontData.getName(), fontData.getStyle(), fontData.getHeight());
		}
		System.out.println("GraphicsBridge.getFont()");
		return this.font;
	}
	@Override
	public Color getColor(){
		if(color == null){
			this.color = new Color(g.getBackgroundColor().getRed(), g.getBackgroundColor().getGreen(), g.getBackgroundColor().getBlue());
		}
		System.out.println("GraphicsBridge.getColor()");
		return color;
	}
	@Override
	public Rectangle getClipBounds(){
		System.out.println("GraphicsBridge.getClipBounds()");
		if(clipBounds == null){
			org.eclipse.draw2d.geometry.Rectangle rectangle = g.getClip(new org.eclipse.draw2d.geometry.Rectangle(0, 0, 10000, 10000));
			clipBounds = new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		}
		return this.clipBounds;
	}
	@Override
	public Shape getClip(){
		System.out.println("GraphicsBridge.getClip()");
		return clip;
	}
	@Override
	public void fillRoundRect(int x,int y,int width,int height,int arcWidth,int arcHeight){
		System.out.println("GraphicsBridge.fillRoundRect()");
		g.fillRoundRectangle(new org.eclipse.draw2d.geometry.Rectangle(x, y, arcWidth, arcHeight), arcWidth, arcHeight);
	}
	@Override
	public void fillRect(int x,int y,int width,int height){
		System.out.println("GraphicsBridge.fillRect()");
		g.fillRectangle(x, y, width, height);
	}
	@Override
	public void fillPolygon(int[] xPoints,int[] yPoints,int nPoints){
		System.out.println("GraphicsBridge.fillPolygon()");
		PointList pl = buildpointList(xPoints, yPoints, nPoints);
		g.fillPolygon(pl);
	}
	private PointList buildpointList(int[] xPoints,int[] yPoints,int nPoints){
		System.out.println("GraphicsBridge.buildpointList()");
		PointList pl = new PointList();
		for(int i = 0;i < nPoints;i++){
			pl.addPoint(xPoints[i], yPoints[i]);
		}
		return pl;
	}
	@Override
	public void fillOval(int x,int y,int width,int height){
		System.out.println("GraphicsBridge.fillOval()");
		g.fillOval(x, y, width, height);
	}
	@Override
	public void fillArc(int x,int y,int width,int height,int startAngle,int arcAngle){
		System.out.println("GraphicsBridge.fillArc()");
		g.fillArc(x, y, width, height, startAngle, arcAngle);
	}
	@Override
	public void drawString(AttributedCharacterIterator iterator,int x,int y){
		System.out.println("GraphicsBridge.drawString()");
		StringBuffer sb = new StringBuffer();
		for(int i = iterator.getBeginIndex();i <= iterator.getEndIndex();i++){
			sb.append(iterator.next());
		}
		drawString(sb.toString(), x, y);
	}
	@Override
	public void drawString(String str,int x,int y){
		System.out.println("GraphicsBridge.drawString()");
		FontData fontData = g.getFont().getFontData()[0];
		int potentialY = y - Math.round(fontData.height);
		fontData.setHeight(fontData.getHeight() - 2);
		g.setFont(new org.eclipse.swt.graphics.Font(Workbench.getInstance().getDisplay(), fontData));
		g.drawString(str, new Point(x, Math.abs(potentialY)));
	}
	@Override
	public void drawRoundRect(int x,int y,int width,int height,int arcWidth,int arcHeight){
		System.out.println("GraphicsBridge.drawRoundRect()");
		g.drawRoundRectangle(new org.eclipse.draw2d.geometry.Rectangle(x, y, width, height), arcWidth, arcHeight);
	}
	@Override
	public void drawPolyline(int[] xPoints,int[] yPoints,int nPoints){
		System.out.println("GraphicsBridge.drawPolyline()");
		g.drawPolyline(buildpointList(xPoints, yPoints, nPoints));
	}
	@Override
	public void drawPolygon(int[] xPoints,int[] yPoints,int nPoints){
		System.out.println("GraphicsBridge.drawPolygon()");
		g.drawPolygon(buildpointList(xPoints, yPoints, nPoints));
	}
	@Override
	public void drawOval(int x,int y,int width,int height){
		System.out.println("GraphicsBridge.drawOval()");
		g.drawOval(x, y, width, height);
	}
	@Override
	public void drawLine(int x1,int y1,int x2,int y2){
		System.out.println("GraphicsBridge.drawLine()");
		g.drawLine(x1, y1, x2, y2);
	}
	@Override
	public boolean drawImage(Image img,int dx1,int dy1,int dx2,int dy2,int sx1,int sy1,int sx2,int sy2,Color bgcolor,ImageObserver observer){
		// TODO Auto-generated method stub
		System.out.println("GraphicsBridge.drawImage()");
		return false;
	}
	@Override
	public boolean drawImage(Image img,int dx1,int dy1,int dx2,int dy2,int sx1,int sy1,int sx2,int sy2,ImageObserver observer){
		// TODO Auto-generated method stub
		System.out.println("GraphicsBridge.drawImage()");
		return false;
	}
	@Override
	public boolean drawImage(Image img,int x,int y,int width,int height,Color bgcolor,ImageObserver observer){
		// TODO Auto-generated method stub
		System.out.println("GraphicsBridge.drawImage()");
		return false;
	}
	@Override
	public boolean drawImage(Image img,int x,int y,int width,int height,ImageObserver observer){
		// TODO Auto-generated method stub
		System.out.println("GraphicsBridge.drawImage()");
		return false;
	}
	@Override
	public boolean drawImage(Image img,int x,int y,Color bgcolor,ImageObserver observer){
		// TODO Auto-generated method stub
		System.out.println("GraphicsBridge.drawImage()");
		return false;
	}
	@Override
	public boolean drawImage(Image img,final int x,final int y,ImageObserver observer){
		// TODO Auto-generated method stub
		JComponent c = (JComponent) observer;
		Container container = c.getParent();
		Color bgColor = c.getBackground();
		if(!c.isOpaque()){
			while(!container.isOpaque()){
				container = container.getParent();
				bgColor = container.getBackground();
			}
		}
		System.out.println("GraphicsBridge.drawImage()");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedImage bi = new BufferedImage(img.getWidth(observer), img.getHeight(observer), BufferedImage.TYPE_INT_RGB);
		Graphics2D big = bi.createGraphics();
		big.drawImage(img, 0, 0, observer);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
		try{
			for(int curX = 0;curX < bi.getWidth();curX++){
				for(int curY = 0;curY < bi.getHeight();curY++){
					if(Color.black.getRGB() == bi.getRGB(curX, curY)){
						bi.setRGB(curX, curY, bgColor.getRGB());
					}
				}
			}
			bi.getColorModel().coerceData(bi.getRaster(), true);
			encoder.encode(bi);
			org.eclipse.swt.graphics.Image swtImage = new org.eclipse.swt.graphics.Image(Workbench.getInstance().getDisplay(), new ByteArrayInputStream(os.toByteArray()));
			g.drawImage(swtImage, x, y);
		}catch(ImageFormatException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	@Override
	public void drawArc(int x,int y,int width,int height,int startAngle,int arcAngle){
		g.drawArc(x, y, width, height, startAngle, arcAngle);
		System.out.println("GraphicsBridge.drawArc()");
	}
	@Override
	public void dispose(){
		
		popState();
	}
	@Override
	public java.awt.Graphics create(){
		System.out.println("GraphicsBridge.create()");
//		MapModeGraphics mapModeGraphics = (MapModeGraphics) g;
//		GraphicsBridge result = new GraphicsBridge(new MapModeGraphics(mapModeGraphics, mapModeGraphics.getMapMode()));
		GraphicsBridge result = new GraphicsBridge(g);
		result.pushState();
		return result;
	}
	@Override
	public void copyArea(int x,int y,int width,int height,int dx,int dy){
		// TODO Auto-generated method stub
		System.out.println("GraphicsBridge.copyArea()");
	}
	@Override
	public void clipRect(int x,int y,int width,int height){
		System.out.println("GraphicsBridge.clipRect()");
		org.eclipse.draw2d.geometry.Rectangle newClipBounds = g.getClip(new org.eclipse.draw2d.geometry.Rectangle(x, y, width, height));
		g.clipRect(new org.eclipse.draw2d.geometry.Rectangle(x, y, width, height));
		this.clipBounds = new Rectangle(newClipBounds.x, newClipBounds.y, newClipBounds.width, newClipBounds.height);
	}
	@Override
	public java.awt.Graphics create(int x,int y,int width,int height){
		java.awt.Graphics ng = super.create(x, y, width, height);
		return ng;
	}
	@Override
	public Rectangle getClipRect(){
		// TODO Auto-generated method stub
		return super.getClipRect();
	}
	@Override
	public FontMetrics getFontMetrics(){
		return super.getFontMetrics();
	}
	@Override
	public void drawRect(int x,int y,int width,int height){
		super.drawRect(x, y, width, height);
	}
	@Override
	public void draw3DRect(int x,int y,int width,int height,boolean raised){
		super.draw3DRect(x, y, width, height, raised);
	}
	@Override
	public void fill3DRect(int x,int y,int width,int height,boolean raised){
		super.fill3DRect(x, y, width, height, raised);
	}
	@Override
	public void drawPolygon(Polygon p){
		super.drawPolygon(p);
	}
	@Override
	public void fillPolygon(Polygon p){
		super.fillPolygon(p);
	}
	@Override
	public void drawChars(char[] data,int offset,int length,int x,int y){
		super.drawChars(data, offset, length, x, y);
	}
	@Override
	public void drawBytes(byte[] data,int offset,int length,int x,int y){
		super.drawBytes(data, offset, length, x, y);
	}
	@Override
	public boolean hitClip(int x,int y,int width,int height){
		return super.hitClip(x, y, width, height);
	}
	@Override
	public Rectangle getClipBounds(Rectangle r){
		return super.getClipBounds(r);
	}
	@Override
	public void clearRect(int x,int y,int width,int height){
		System.out.println("GraphicsBridge.clearRect()");
		// TODO Auto-generated method stub
	}
	public void pushState(){
		g.pushState();
	}
	public void popState(){
		if(!isPopped){
			g.popState();
			isPopped=true;
		}
	}
}