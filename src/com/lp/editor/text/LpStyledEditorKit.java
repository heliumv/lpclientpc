/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.editor.text;

import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.GlyphView;
import javax.swing.text.GlyphView.GlyphPainter;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TabExpander;
import javax.swing.text.TabSet;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import com.lp.editor.ui.LpDecoratedTextPane;

/**
 * <p>
 * Ueberschrift: Logistik Pur Editor
 * </p>
 * <p>
 * Beschreibung: RTF - Editor die LogP - Version 5
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Logistik Pur Software GmbH
 * </p>
 * 
 * @author Sascha Zelzer
 * @author Kajetan Fuchsberger
 * @version 0.0
 */

public class LpStyledEditorKit extends StyledEditorKit {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Hashtable<Object, Action> actions;
	private int tabsize ;
	
	public LpStyledEditorKit() {
		super();
		tabsize = 72 ; // Default Size Java
		createActionsHashTable();
	}

	public int getTabsize() {
		return tabsize ;
	}
	
	public void setTabsize(int newTabsize) {
		tabsize = newTabsize ;
	}
	
	/**
	 * Erzeugen einer Hashtable, um auf die Actions spaeter ueber den Namen
	 * zugreifen zu koennen. Es werden aus dem uebergebenen
	 * <code>EditorKit</code> alle vorhanden Actions ausgelesen und in eine
	 * Hashtable geschrieben, mit dem Namen als Key. Diese koennen dann mithilfe
	 * von {@link #getActionByName} ausgelesen werden.
	 * 
	 * @see #getActionByName
	 * 
	 */
	private synchronized void createActionsHashTable() {
		if (actions == null) {
			actions = new Hashtable<Object, Action>();

			Action[] actionsArray = this.getActions();
			for (int i = 0; i < actionsArray.length; i++) {
				Action a = actionsArray[i];
				actions.put(a.getValue(Action.NAME), a);
				// System.out.println(a.getValue(Action.NAME));
			}
		}
	}

	/**
	 * Zugriff auf die Actions in der Hashtable ueber den Namen.
	 * 
	 * @param sActionName
	 *            Der Name der Action, die aus der Hastable gelesen werden soll.
	 * @return Die Action aus der Hashtable, die ueber den Namen spezifiziert
	 *         wurde.
	 */
	public Action getActionByName(String sActionName) {
		return (Action) (actions.get(sActionName));
	}

	/**
	 * Ueberschreibt createDefaultDocument um Default-Properties fuer das neue
	 * Dokument zu setzen.
	 * 
	 * @return Document
	 */
	public Document createDefaultDocument() {
		Document doc = super.createDefaultDocument();
		doc.putProperty(LpDecoratedTextPane.DimensionProperty,
				LpDecoratedTextPane.FORMAT_A4);
		doc.putProperty(LpDecoratedTextPane.MarginProperty, new Insets(5, 5, 5,
				5));
		doc.putProperty(Document.TitleProperty, "Untitled");
		return doc;
	}

	public ViewFactory getViewFactory() {
		return new StyledViewFactory();
	}

	class StyledViewFactory implements ViewFactory {

		public View create(Element elem) {
			String kind = elem.getName();
			if (kind != null) {
				if (kind.equals(AbstractDocument.ContentElementName)){
					return new ScaledLableView(elem);
				} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
//					return new ParagraphView(elem);
					return new LpParagraphView(elem) ;
				} else if (kind.equals(AbstractDocument.SectionElementName)) {
					return new ScaledView(elem, View.Y_AXIS);
				} else if (kind.equals(StyleConstants.ComponentElementName)) {
					return new ComponentView(elem);
				} else if (kind.equals(StyleConstants.IconElementName)) {
					return new IconView(elem);
				}
			}

			// default to text display
			return new LabelView(elem);
		}

	}
	

	class LpParagraphView extends ParagraphView	{
		public LpParagraphView(Element element) {
			super(element) ;
		}
		
		@Override
		public float nextTabStop(float x, int tabOffset) {
	        TabSet tabs = getTabSet();
	        if(tabs == null) {
	            return (float)(getTabBase() + (((int)x / getTabsize() + 1) * getTabsize()));
	        }

	        return super.nextTabStop(x, tabOffset);
		}
	}
}

class ScaledView extends BoxView {
	static GlyphPainter defaultPainter;

	public ScaledView(Element elem, int axis) {
		super(elem, axis);
//		super(elem);
//		getDocument().putProperty("i18n", Boolean.TRUE);
	}

	public double getZoomFactor() {
		Double scale = (Double) getDocument().getProperty(
				LpDecoratedTextPane.ZoomfactorProperty);
		if (scale != null) {
			return scale.doubleValue();
		}

		return 1;
	}	

	public void paint(Graphics g, Shape allocation) {
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(hints);
		double zoomFactor = getZoomFactor();
		AffineTransform old = g2d.getTransform();
		g2d.scale(zoomFactor, zoomFactor);
		super.paint(g2d, allocation);
		g2d.setTransform(old);
	}

	public float getMinimumSpan(int axis) {
		float f = super.getMinimumSpan(axis);
		f *= getZoomFactor();
		return f;
	}

	public float getMaximumSpan(int axis) {
		float f = super.getMaximumSpan(axis);
		f *= getZoomFactor();
		return f;
	}

	public float getPreferredSpan(int axis) {
		float f = super.getPreferredSpan(axis);
		f *= getZoomFactor();
		return f;
	}
	
	protected void layout(int width, int height) {
		super.layout(new Double(width / getZoomFactor()).intValue(),
				new Double(height / getZoomFactor()).intValue());
	}

	public Shape modelToView(int pos, Shape a, Position.Bias b)
			throws BadLocationException {
		Rectangle alloc;
		alloc = a.getBounds();
		Shape s = super.modelToView(pos, alloc, b);
		alloc = s.getBounds();
		alloc.x = multiplyByZoomfactor(alloc.x);
		alloc.y = multiplyByZoomfactor(alloc.y);
		alloc.width = multiplyByZoomfactor(alloc.width);
		alloc.height = multiplyByZoomfactor(alloc.height);

		return alloc;
	}

	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
		double zoomFactor = getZoomFactor();
		Rectangle alloc = a.getBounds();
		x /= zoomFactor;
		y /= zoomFactor;
		alloc.x = divideByZoomfactor(alloc.x);
		alloc.y = divideByZoomfactor(alloc.y);
		alloc.width = divideByZoomfactor(alloc.width);
		alloc.height = divideByZoomfactor(alloc.height);
		return super.viewToModel(x, y, alloc, bias);
	}

	private int divideByZoomfactor(int x) {
		return (int)Math.round(x / getZoomFactor());
	}
	
	private int multiplyByZoomfactor(int x) {
		return (int)Math.round(x * getZoomFactor());
	}

}

class ScaledGlyphPainter extends GlyphView.GlyphPainter {
    static ScaledGlyphPainter instance=new ScaledGlyphPainter();
    public static ScaledGlyphPainter getInstance() {
        return instance;
    }

    static Graphics2D painterGr;

    static {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        painterGr = (Graphics2D) img.getGraphics();
        painterGr.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    /**
     * Determine the span the glyphs given a start location
     * (for tab expansion).
     */
    public float getSpan(GlyphView v, int p0, int p1,
                         TabExpander e, float x) {
        sync(v);
        Segment text = getText(v, p0,p1);
        int[] justificationData = getJustificationData(v);
        float width = getTabbedTextWidth(v, text, metrics, x, e, p0, justificationData);
        return width;
    }

    public float getHeight(GlyphView v) {
        sync(v);
        return metrics.getHeight();
    }

    /**
     * Fetches the ascent above the baseline for the glyphs
     * corresponding to the given range in the model.
     */
    public float getAscent(GlyphView v) {
        sync(v);
        return metrics.getAscent();
    }

    /**
     * Fetches the descent below the baseline for the glyphs
     * corresponding to the given range in the model.
     */
    public float getDescent(GlyphView v) {
        sync(v);
        return metrics.getDescent();
    }

    /**
     * Paints the glyphs representing the given range.
     */
    public void paint(GlyphView v, Graphics g, Shape a, int p0, int p1) {
        sync(v);
        Segment text;
        TabExpander expander = v.getTabExpander();
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();

        // determine the x coordinate to render the glyphs
        int x = alloc.x;
        int p = v.getStartOffset();
        int[] justificationData = getJustificationData(v);
        if (p != p0) {
            text = getText(v, p, p0);
            float width = getTabbedTextWidth(v, text, metrics, x, expander, p, justificationData);
            x += width;
        }

        // determine the y coordinate to render the glyphs
        int y = alloc.y + metrics.getHeight() - metrics.getDescent();

        // render the glyphs
        text = getText(v, p0, p1);
        g.setFont(metrics.getFont());

        drawTabbedText(v, text, x, y, g, expander,p0, justificationData);
    }

    public Shape modelToView(GlyphView v, int pos, Position.Bias bias,
                             Shape a) throws BadLocationException {
        sync(v);
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
        int p0 = v.getStartOffset();
        int p1 = v.getEndOffset();
        TabExpander expander = v.getTabExpander();
        Segment text;

        if(pos == p1) {
            // The caller of this is left to right and borders a right to
            // left view, return our end location.
            return new Rectangle(alloc.x + alloc.width, alloc.y, 0, metrics.getHeight());
        }
        if ((pos >= p0) && (pos <= p1)) {
            // determine range to the left of the position
            text = getText(v, p0, pos);
            int[] justificationData = getJustificationData(v);
            int width = (int)getTabbedTextWidth(v, text, metrics, alloc.x, expander, p0, justificationData);
            return new Rectangle(alloc.x + width, alloc.y, 0, metrics.getHeight());
        }
        throw new BadLocationException("modelToView - can't convert", p1);
    }

    /**
     * Provides a mapping from the view coordinate space to the logical
     * coordinate space of the model.
     *
     * @param v the view containing the view coordinates
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param a the allocated region to render into
     * @param biasReturn always returns <code>Position.Bias.Forward</code>
     *   as the zero-th element of this array
     * @return the location within the model that best represents the
     *  given point in the view
     * @see View#viewToModel
     */
    public int viewToModel(GlyphView v, float x, float y, Shape a,
                           Position.Bias[] biasReturn) {
        sync(v);
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
        int p0 = v.getStartOffset();
        int p1 = v.getEndOffset();
        TabExpander expander = v.getTabExpander();
        Segment text = getText(v, p0, p1);
        int[] justificationData = getJustificationData(v);
        int offs = getTabbedTextOffset(v, text, metrics, alloc.x, (int) x, expander, p0, true,justificationData);
        int retValue = p0 + offs;
        if(retValue == p1) {
            // No need to return backward bias as GlyphPainter1 is used for
            // ltr text only.
            retValue--;
        }
        biasReturn[0] = Position.Bias.Forward;
        return retValue;
    }

    /**
     * Determines the best location (in the model) to break
     * the given view.
     * This method attempts to break on a whitespace
     * location.  If a whitespace location can't be found, the
     * nearest character location is returned.
     *
     * @param v the view
     * @param p0 the location in the model where the
     *  fragment should start its representation >= 0
     * @param x the graphic location along the axis that the
     *  broken view would occupy >= 0; this may be useful for
     *  things like tab calculations
     * @param len specifies the distance into the view
     *  where a potential break is desired >= 0
     * @return the model location desired for a break
     * @see View#breakView
     */
    public int getBoundedPosition(GlyphView v, int p0, float x, float len) {
        sync(v);
        TabExpander expander = v.getTabExpander();
        Segment s = getText(v, p0, v.getEndOffset());
        int[] justificationData = getJustificationData(v);
        int index = getTabbedTextOffset(v,s, metrics, (int)x, (int)(x+len), expander, p0, false, justificationData);
        int p1 = p0 + index;
        return p1;
    }

    void sync(GlyphView v) {
        Font f = v.getFont();
        if ((metrics == null) || (! f.equals(metrics.getFont()))) {
            // fetch a new FontMetrics
            Container c = v.getContainer();
            metrics = (c != null) ? c.getFontMetrics(f) : Toolkit.getDefaultToolkit().getFontMetrics(f);
        }
    }

    private int[] getJustificationData(GlyphView v) {
        View parent = v.getParent();
        int [] ret = null;

        //use reflection to get the data
        Class pClass=parent.getClass();
        if (pClass.isAssignableFrom(ParagraphView.class.getDeclaredClasses()[0])) { //if (parent instanceof ParagraphView.Row) {
            try {
                Field f=pClass.getDeclaredField("justificationData");
                if (f!=null) {
                    f.setAccessible(true);
                    ret=(int[])f.get(parent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    // --- variables ---------------------------------------------

    FontMetrics metrics;
    static char[] SPACE_CHAR=new char[] {' '};

    static Segment getText(View v, int start, int end){
        Segment s=new Segment();
        try {
            s.array=v.getDocument().getText(start, end-start).toCharArray();
            s.offset=0;
            s.count=end-start;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return s;
    }

    float getTabbedTextWidth(View view, Segment s, FontMetrics metrics, float x,
                             TabExpander e, int startOffset,
                             int[] justificationData) {
        float nextX = x;
        char[] txt = s.array;
        String txtStr=new String(txt);
        int txtOffset = s.offset;
        int n = s.offset + s.count;
        int charCount = 0;
        int spaceAddon = 0;
        int spaceAddonLeftoverEnd = -1;
        int startJustifiableContent = 0;
        int endJustifiableContent = 0;
        if (justificationData != null) {
            int offset = - startOffset + txtOffset;
            View parent = null;
            if (view != null && (parent = view.getParent()) != null) {
                offset += parent.getStartOffset();
            }
            spaceAddon =justificationData[0];
            spaceAddonLeftoverEnd =justificationData[1] + offset;
            startJustifiableContent =justificationData[2] + offset;
            endJustifiableContent =justificationData[3] + offset;
        }

        for (int i = txtOffset; i < n; i++) {
            if (txt[i] == '\t'
                    || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd)
                    && (txt[i] == ' ')
                    && startJustifiableContent <= i
                    && i <= endJustifiableContent
            )) {
                nextX += metrics.getStringBounds(txtStr,i-charCount, i, painterGr).getWidth();
                charCount = 0;
                if (txt[i] == '\t') {
                    if (e != null) {
                        nextX = e.nextTabStop((float) nextX, startOffset + i - txtOffset);
                    } else {
                        nextX += metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth();
                        nextX =(int)nextX;
                    }
                } else if (txt[i] == ' ') {
                    nextX += metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth() + spaceAddon;
                    nextX =(int)nextX;
                    if (i <= spaceAddonLeftoverEnd) {
                        nextX++;
                    }
                }
            } else if(txt[i] == '\n') {
                // Ignore newlines, they take up space and we shouldn't be
                // counting them.
                nextX += (float) metrics.getStringBounds(txtStr,i-charCount, i, painterGr).getWidth();
                charCount = 0;
            } else {
                charCount++;
            }
        }

        nextX += metrics.getStringBounds(txtStr,n-charCount, n, painterGr).getWidth();
        return nextX - x;
    }

    float drawTabbedText(View view, Segment s, float x, float y, Graphics g,
                         TabExpander e, int startOffset, int [] justificationData) {
        float nextX = x;
        char[] txt = s.array;
        String txtStr=new String(txt);
        int txtOffset = s.offset;
        int flushLen = 0;
        int flushIndex = s.offset;
        int spaceAddon = 0;
        int spaceAddonLeftoverEnd = -1;
        int startJustifiableContent = 0;
        int endJustifiableContent = 0;
        if (justificationData != null) {
            int offset = - startOffset + txtOffset;
            View parent = null;
            if (view != null && (parent = view.getParent()) != null) {
                offset += parent.getStartOffset();
            }
            spaceAddon =justificationData[0];
            spaceAddonLeftoverEnd =justificationData[1] + offset;
            startJustifiableContent =justificationData[2] + offset;
            endJustifiableContent =justificationData[3] + offset;
        }
        int n = s.offset + s.count;
        for (int i = txtOffset; i < n; i++) {
            if (txt[i] == '\t'
                    || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd)
                    && (txt[i] == ' ')
                    && startJustifiableContent <= i
                    && i <= endJustifiableContent
            )) {
                if (flushLen > 0) {
                    ((Graphics2D)g).drawString(txtStr.substring(flushIndex, flushIndex+flushLen), x, y);
                    //corrected position
                    nextX += metrics.getStringBounds(txtStr,flushIndex, flushIndex+flushLen, painterGr).getWidth();
                    flushLen = 0;
                }
                flushIndex = i + 1;
                if (txt[i] == '\t') {
                    if (e != null) {
                        nextX = e.nextTabStop((float) nextX, startOffset + i - txtOffset);
                    } else {
                        nextX += (float) metrics.getStringBounds(SPACE_CHAR, 0,1, painterGr).getWidth();
                        nextX =(int)nextX;
                    }
                } else if (txt[i] == ' ') {
                    nextX += (float) metrics.getStringBounds(SPACE_CHAR, 0,1, painterGr).getWidth()+spaceAddon;
                    if (i <= spaceAddonLeftoverEnd) {
                        nextX++;
                    }
                }
                x = nextX;
            } else if ((txt[i] == '\n') || (txt[i] == '\r')) {
                if (flushLen > 0) {
                    ((Graphics2D)g).drawString(txtStr.substring(flushIndex, flushIndex+flushLen), x, y);
                    //corrected
                    nextX += metrics.getStringBounds(txtStr,flushIndex, flushIndex+flushLen, painterGr).getWidth();
                    flushLen = 0;
                }
                flushIndex = i + 1;
                x = nextX;
            } else {
                flushLen += 1;
            }
        }
        if (flushLen > 0) {
            ((Graphics2D)g).drawString(txtStr.substring(flushIndex, flushIndex+flushLen), x, y);
            //corrected
            nextX += metrics.getStringBounds(txtStr,flushIndex, flushIndex+flushLen, painterGr).getWidth();
        }
        return nextX;
    }

    int getTabbedTextOffset(View view,
                             Segment s,
                             FontMetrics metrics,
                             int x0, int x, TabExpander e,
                             int startOffset,
                             boolean round,
                             int[] justificationData) {
        if (x0 >= x) {
            // x before x0, return.
            return 0;
        }
        float currX = x0;
        float nextX = currX;
        // s may be a shared segment, so it is copied prior to calling
        // the tab expander
        char[] txt = s.array;
        int txtOffset = s.offset;
        int txtCount = s.count;
        int spaceAddon = 0 ;
        int spaceAddonLeftoverEnd = -1;
        int startJustifiableContent = 0 ;
        int endJustifiableContent = 0;
        if (justificationData != null) {
            int offset = - startOffset + txtOffset;
            View parent = null;
            if (view != null && (parent = view.getParent()) != null) {
                offset += parent.getStartOffset();
            }
            spaceAddon =justificationData[0];
            spaceAddonLeftoverEnd =justificationData[1] + offset;
            startJustifiableContent =justificationData[2] + offset;
            endJustifiableContent =justificationData[3] + offset;
        }
        int n = s.offset + s.count;
        for (int i = s.offset; i < n; i++) {
            if (txt[i] == '\t'
                    || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd)
                    && (txt[i] == ' ')
                    && startJustifiableContent <= i
                    && i <= endJustifiableContent
            )){
                if (txt[i] == '\t') {
                    if (e != null) {
                        nextX = (int) e.nextTabStop((float) nextX,
                                startOffset + i - txtOffset);
                    } else {
                        nextX += metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth();
                    }
                } else if (txt[i] == ' ') {
                    nextX += metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth()+spaceAddon;
                    nextX =(int)nextX;

                    if (i <= spaceAddonLeftoverEnd) {
                        nextX++;
                    }
                }
            } else {
                nextX += metrics.getStringBounds(txt, i, i+1,painterGr).getWidth();
            }
            if ((x >= currX) && (x < nextX)) {
                // found the hit position... return the appropriate side
                if ((round == false) || ((x - currX) < (nextX - x))) {
                    return i - txtOffset;
                } else {
                    return i + 1 - txtOffset;
                }
            }
            currX = nextX;
        }

        // didn't find, return end offset
        return txtCount;
    }
}
class ScaledLableView extends LabelView {
    static GlyphPainter defaultPainter;
    public ScaledLableView(Element elem) {
        super(elem);
    }

    protected void checkPainter() {
        if (getGlyphPainter() == null) {
            if (defaultPainter == null) {
                defaultPainter = new ScaledGlyphPainter();
            }
            setGlyphPainter(defaultPainter.getPainter(this, getStartOffset(), getEndOffset()));
        }
    }
}
