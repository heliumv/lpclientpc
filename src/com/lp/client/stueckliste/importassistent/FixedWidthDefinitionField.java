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
 package com.lp.client.stueckliste.importassistent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.lp.service.StklImportSpezifikation;

public class FixedWidthDefinitionField extends JTextArea implements MouseMotionListener, MouseListener, IColumnTypeDefiner{
	
	private static final long serialVersionUID = -8633799663695034260L;
	
	private Color ADD_COLOR = Color.blue.brighter();
	private Color REMOVE_COLOR = Color.red.darker();
	private Color SEPARATOR_COLOR = Color.black;
	private Color LINE_COLOR = Color.lightGray;
	private Color HEADER_COLOR = new Color(1f, 0.5f, 0.5f, 0.5f);
	
	private int charWidth;
	private int charHeight;
	private int correction = 1;
	private Integer highlightColumnIndex = null;
	
	private StklImportSpezifikation spez = null;
	
	private List<IDefinitionListener> listeners = new ArrayList<IDefinitionListener>();
	
	public FixedWidthDefinitionField() {
		Font f = Font.decode(Font.MONOSPACED);
		setFont(f);
		charWidth = getFontMetrics(f).charWidth('a');
		charHeight = getFontMetrics(f).getHeight();
		setEditable(false);
		setFocusable(false);
		DefaultCaret caret = (DefaultCaret)getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		setBackground(Color.white);
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	
	public void addDefinitionListener(IDefinitionListener listener) {
		if(listeners.contains(listener))return;
		listeners.add(listener);
	}
	
	public void removeDefinitionListener(IDefinitionListener listener) {
		listeners.remove(listener);
	}

	protected void fireFixedWidthAddedEvent(Integer column) {
		for(IDefinitionListener listener : listeners) {
			listener.addedColumnSeparator(column);
		}
	}
	
	protected void fireFixedWidthRemovedEvent(Integer column) {
		for(IDefinitionListener listener : listeners) {
			listener.removedColumnSeparator(column);
		}
	}
	
	protected void fireColumnTypeSetEvent(Integer index, String type) {
		for(IDefinitionListener listener : listeners) {
			listener.updateColumnType(index, type);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Integer mouseOver = null;
		if(getMousePosition() != null) {
			mouseOver = getColByX(getMousePosition().x);
		}
		
		//Spaltentypen
		g.setColor(new Color(0.9f, 0.9f, 0.9f, 0.5f));
		g.fillRect(0, 0, getWidth(), charHeight);

		//Header im File
		g.setColor(HEADER_COLOR);
		g.fillRect(0, charHeight, getWidth(), (getSpezifikation().getFromRow()-1)*charHeight);
		
		if(highlightColumnIndex != null && highlightColumnIndex >= 0) {
			int colStart = highlightColumnIndex == 0 ? 0 : getSpezifikation().getWidths().get(highlightColumnIndex-1);
			int colEnd = getSpezifikation().getWidths().get(highlightColumnIndex);
			g.setXORMode(Color.BLACK);
			g.setColor(Color.white);
			g.fillRect(getXByCol(colStart), 0, getXByCol(colEnd - colStart), getHeight());
			g.setPaintMode();
		}

		//Spalten
		g.setColor(LINE_COLOR);
		for(int i = charHeight; i < getHeight(); i+= charHeight) {
			g.drawLine(0, i, getWidth(), i);
		}
		
		for (Integer width : getSpezifikation().getWidths()) {
			if(mouseOver != null && mouseOver.equals(width)) {
				g.setColor(REMOVE_COLOR);
				mouseOver = null;
			} else {
				g.setColor(SEPARATOR_COLOR);
			}
			g.drawLine(getXByCol(width), 0, getXByCol(width), getHeight());
		}
		
		if(mouseOver != null) {
			g.setColor(ADD_COLOR);
			g.drawLine(getXByCol(mouseOver), 0, getXByCol(mouseOver), getHeight());
		}
		
	}
	
	protected String createHeadline() {
		StringBuffer sb = new StringBuffer();
		List<Integer> widths = getSpezifikation().getWidths();
		List<String> types = getSpezifikation().getColumnTypes();
		int lastCol = 0;
		for(int i = 0; i < widths.size(); i++) {
			int w = widths.get(i);
			int length = (w - lastCol);
			sb.append(String.format("%-" + length  + "." + length + "s", types.get(i)));
			lastCol = w;
		}
		return sb.toString();
	}
	
	public void setLines(List<String> lines) {
		StringBuffer sb = new StringBuffer();
		sb.append(createHeadline());
		for(String line : lines) {
			sb.append("\n");
			sb.append(line);
		}
		setText(sb.toString());
	}
	
	protected int getColByX(int x) {
		return (x + correction) / charWidth;
	}
	
	protected int getXByCol(int col) {
		return col*charWidth + correction;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Integer col = new Integer(getColByX(e.getPoint().x));
		if(col == 0) return;
		if(getSpezifikation().getWidths().contains(col))
			fireFixedWidthRemovedEvent(col);
		else
			fireFixedWidthAddedEvent(col);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	protected int getColumnIndexContaining(Integer col) {
		List<Integer> widths = getSpezifikation().getWidths();
		for(int i = 0; i < widths.size(); i++) {
			if(widths.get(i) > col) {
				return i;
			}
		}
		return -1;
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Integer col = getColByX(e.getPoint().x);
		int index = getColumnIndexContaining(col);
		
		String type = null;
		if(index >= 0) {
			 type = getSpezifikation().getColumnTypes().get(index);
		}
		setToolTipText(type);
		repaint();
	}
	
	public void highlightColumnAt(Point p) {
		if(p == null || !getBounds().contains(p))
			highlightColumnIndex = null;
		else
			highlightColumnIndex = getColumnIndexContaining(getColByX(p.x));
		repaint();		
	}
	
	public void setColumnTypeAt(Point p, String type) {
		if(!getBounds().contains(p))
			return;
		int index = getColumnIndexContaining(getColByX(p.x));
		if(index >= 0)
			fireColumnTypeSetEvent(index, type);
	}

	protected StklImportSpezifikation getSpezifikation() {
		return spez;
	}

	public void setSpezifikation(StklImportSpezifikation spezifikation) {
		this.spez = spezifikation;
		repaint();
	}
}
