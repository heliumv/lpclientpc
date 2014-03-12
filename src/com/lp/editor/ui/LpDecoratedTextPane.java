/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.editor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;

import com.lp.client.util.logger.LpLogger;
import com.lp.editor.util.ClipboardParser;

/**
 * <p>
 * <I>Ein LpTextPane mit Seitenraendern, 3D-Border, ...</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>Oktober 2003</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Sascha Zelzer
 * @version $Revision: 1.3 $
 */
public class LpDecoratedTextPane extends LpTextPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension pageFormatPixel;
	private Dimension pageFormatMM;
	private LpRuler ruler;
	private Insets margin;
	private boolean bShowPageBreak;
	private Double cachedZoom;

	private Vector<Position> vecStartOverflow = new Vector<Position>();
	private Vector<Position> vecEndOverflow = new Vector<Position>();

	public static final String DimensionProperty = "Dimension";
	public static final String MarginProperty = "Margin";
	public static final String ZoomfactorProperty = "Zoomfactor";

	public static final Dimension FORMAT_A4 = new Dimension(210, 297);
	public static final Dimension FORMAT_A5 = new Dimension(148, 210);
	public static final int UNIT_PIXEL = 0;
	public static final int UNIT_MM = 1;

	public static final int JASPER_DPI = 72;

	protected final float INCH = 25.4f;

	public LpDecoratedTextPane() {
		super();
		pageFormatPixel = new Dimension(0, 0);
		pageFormatMM = new Dimension(0, 0);
		bShowPageBreak = false;
		ruler = null;
		margin = new Insets(5, 5, 5, 5);
		setBorder(null);
	}

	public Dimension getPreferredSize() {
		Dimension preferredSize = getPageFormatNoBordersWithZoom();
//		preferredSize.height = getParent().getHeight();
		if(super.getPreferredSize().height > preferredSize.height)
			preferredSize.height = super.getPreferredSize().height;
		return preferredSize;
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	private int mmToPixel(int mm, int dpi) {
		return (int) (mm * dpi / INCH);
	}

	private int pixelToMM(int px, int dpi) {
		return (int) ((float) px / (float) dpi * INCH);
	}

	private void setPageHeight(int iPageHeight, int unit, int dpi) {
		switch (unit) {
		case UNIT_MM:
			pageFormatMM.height = iPageHeight;
			pageFormatPixel.height = mmToPixel(iPageHeight, dpi);
			break;
		case UNIT_PIXEL:
			pageFormatPixel.height = iPageHeight;
			pageFormatMM.height = pixelToMM(iPageHeight, dpi);
			break;
		}
	}

	/**
	 * Setzt die Breite der Seite (72 dpi!)
	 */
	public void setPageWidth(int iPageWidth) {
		setPageWidth(iPageWidth, UNIT_PIXEL, JASPER_DPI);
		applyPageFormat();
	}

	private void setPageWidth(int iPageWidth, int unit, int dpi) {
		switch (unit) {
		case UNIT_MM:
			pageFormatMM.width = iPageWidth;
			pageFormatPixel.width = mmToPixel(iPageWidth, dpi);
			break;
		case UNIT_PIXEL:
			pageFormatPixel.width = iPageWidth;
			pageFormatMM.width = pixelToMM(iPageWidth, dpi);
			break;
		}
	}

	/**
	 * Setzt die Gr&ouml;&szlig;e der Seite (72 dpi!)
	 */
	public void setPageFormat(Dimension dim, int unit) {
		setPageWidth(dim.width, unit, JASPER_DPI);
		setPageHeight(dim.height, unit, JASPER_DPI);
		applyPageFormat();
	}

	/**
	 * Aktiviert das PageFormat
	 */
	private void applyPageFormat() {
		getDocument().putProperty(LpDecoratedTextPane.DimensionProperty, getPageFormat());
		if (ruler != null) {
			ruler.invalidate();
			ruler.getParent().validate();
		}
		revalidate();
	}

	/**
	 * Setzt die Gr&ouml;&szlig;e der Seite in Pixel (72 dpi!)
	 */
	public void setPageFormat(Dimension dim) {
		setPageFormat(dim, UNIT_PIXEL);
	}

	private Dimension multiplyDimension(Dimension d, double factor) {
		return new Dimension((int)Math.round(d.width * factor),
				(int) Math.round(d.height * factor));
	}
	
	private Dimension addPageBorders(Dimension pageFormatPixel) {
		Dimension dim = new Dimension();
		Insets margin = getPageMargin();
		dim.width = pageFormatPixel.width + margin.left + margin.right;
		dim.height = pageFormatPixel.height;
		return dim;
	}

	/**
	 * Gibt die dargestellte, zoomabh&auml;ngige Aufl&ouml;sung des Textbereiches
	 * (= Seitengr&ouml;&szlig;e - R&auml;nder) in Pixel zur&uuml;ck.
	 * @return
	 */
	public Dimension getPageFormatNoBordersWithZoom() {
		return multiplyDimension(pageFormatPixel, getZoomFactor());
	}


	/**
	 * Gibt die dargestellte, zoomabh&auml;ngige Aufl&ouml;sung der Seite inklusive R&auml;nder
	 * in Pixel zur&uuml;ck.
	 * @return
	 */
	public Dimension getPageFormatWithZoom() {
		return multiplyDimension(addPageBorders(pageFormatPixel), getZoomFactor());
	}

	/**
	 * Gibt die wirkliche, zoomunabh&auml;ngige Aufl&ouml;sung der Seite zur&uuml;ck
	 * (72 dpi!)
	 * @param unit
	 * @return
	 */
	public Dimension getPageFormat(int unit) {
		switch (unit) {
		case UNIT_MM:
			return pageFormatMM;
		case UNIT_PIXEL:
			return pageFormatPixel;
		default:
			return pageFormatMM;
		}
	}
	
	@Override
	public void paste() {
		String parsed = ClipboardParser.parseToLpEditorFormat(getToolkit().getSystemClipboard());
		if(parsed != null) {
			replaceSelection("");
			try {
				getEditorKit().read(new StringReader(parsed), getDocument(), getCaretPosition());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			super.paste();
		}
	}

	/**
	 * Gibt die wirkliche, zoomunabh&auml;ngige Aufl&ouml;sung der Seite in Pixel zur&uuml;ck
	 * (72 dpi!)
	 * @return
	 */
	public Dimension getPageFormat() {
		return getPageFormat(UNIT_PIXEL);
	}

	public void showPageBreak(boolean state) {
		bShowPageBreak = state;
		repaint();
	}

	public void setRuler(LpRuler ruler) {
		this.ruler = ruler;
	}

	public void setPageMargin(Insets m) {
		margin = m;
		ruler.updateRuler();
		repaint();
		getDocument().putProperty(LpDecoratedTextPane.MarginProperty, m);
	}

	public Insets getPageMarginWithZoom() {
		int top = (int) Math.round(margin.top*getZoomFactor());
		int left = (int) Math.round(margin.left*getZoomFactor());
		int bottom = (int) Math.round(margin.bottom*getZoomFactor());
		int right = (int) Math.round(margin.right*getZoomFactor());
		return new Insets(top, left, bottom, right);
	}

	public Insets getPageMargin() {
		return margin;
	}
	
	@Override
	public Insets getInsets() {
		return new Insets(0,0,0,0);
	}

	public void addOverflowRange(Position start, Position end) {
		vecStartOverflow.add(start);
		vecEndOverflow.add(end);
	}

	public void removeOverflowRange(Position start, Position end) {
		vecStartOverflow.remove(start);
		vecEndOverflow.remove(end);
	}
	
	@Override
	public void repaint(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.repaint(0, 0, getWidth(), getHeight());
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (bShowPageBreak) {
			Dimension pageFormatWithZoom = getPageFormatNoBordersWithZoom();
			g.setColor(Color.lightGray);
			g.setXORMode(Color.white);
			Insets insets = getInsets();
			int numBreaks = (getSize().height - insets.top - insets.bottom)
					/ getPageFormatNoBordersWithZoom().height;
			for (int i = 0; i < numBreaks; i++) {
				g.drawLine(insets.left, pageFormatWithZoom.height * (i + 1)
						+ insets.top, pageFormatWithZoom.width,
						pageFormatWithZoom.height * (i + 1) + insets.top);
			}
		}
	}

	protected void paintChildren(Graphics g) {
		super.paintChildren(g);

		if (vecStartOverflow.size() == vecEndOverflow.size()) {
			g.setColor(Color.RED);
			for (int i = 0; i < vecStartOverflow.size(); i++) {
				Position posStart = (Position) vecStartOverflow.get(i);
				Position posEnd = (Position) vecEndOverflow.get(i);
				int ps = 0;
				try {
					ps = posStart.getOffset();
					if (ps > 0) {
						ps++;
					}
					int start = modelToView(ps).y;
					int pe = posEnd.getOffset();
					int add = 0;
					if (pe >= getDocument().getLength()) {
						pe--;
						add = 14;
					}
					int end = modelToView(pe).y + add;

					g.drawLine(2, start, 2, end);
					g.drawLine(2, start, 4, start);
					g.drawLine(2, end, 4, end);
				} catch (BadLocationException exc) {
					LpLogger.getInstance(LpDecoratedTextPane.class).error(
							exc.getLocalizedMessage());
				}
			}
		}
	}

//	protected class PageBorder extends AbstractBorder {
//		/**
//	 * 
//	 */
//		private static final long serialVersionUID = 1L;
//
//		public void paintBorder(Component c, Graphics g, int x, int y,
//				int width, int height) {
//			g.setColor(c.getParent().getBackground());
//			g.drawRect(0, 0, getSize().width - 1, space);
//			g.fillRect(0, 0, getSize().width - 1, space);
//			g.setColor(Color.black);
//			g.drawRect(0, space, getSize().width - 2, getSize().height);
//			g.drawLine(0, space, 0, getSize().height);
//			g.fillRect(getSize().width - 2, space + 1, 3, getSize().height);
//		}
//
//		public Insets getBorderInsets(Component c) {
//			return new Insets(space + 1, 0, 0,0);
////			 top, left, bottom, right
////			return new Insets(space + 1 + margin.top, 1 + margin.left,
////					margin.bottom, 2 + margin.right);
////			return new Insets(0,0,0,0);
//		}
//
//		public boolean isBorderOpaque() {
//			return true;
//		}
//
//	}
	
	public void setZoomFactor(double factor) {
		cachedZoom = factor;
		factor *= new Double(Toolkit.getDefaultToolkit()
			.getScreenResolution()) / JASPER_DPI;
		getDocument().putProperty(LpDecoratedTextPane.ZoomfactorProperty, factor);
		applyPageFormat();
	}

	private Double getZoomFactor() {
		if(getDocument().getProperty(
				LpDecoratedTextPane.ZoomfactorProperty) == null)
			setZoomFactor(new Double(cachedZoom));
		Double fZoom = (Double) getDocument().getProperty(
				LpDecoratedTextPane.ZoomfactorProperty);
		return fZoom == null ? cachedZoom : fZoom;
	}
}
