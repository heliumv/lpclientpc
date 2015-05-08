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
package com.lp.editor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPanel;

public class LpEditorSheetPanel extends JPanel{
		private static final long serialVersionUID = 1L;

		private int topSpace = 10;

		private LpDecoratedTextPane textPane;
		
		public LpEditorSheetPanel(LpDecoratedTextPane textPane) {
			this.textPane = textPane;
			this.add(textPane);
		}
		
		@Override
		public Dimension getPreferredSize() {
			Dimension d = new Dimension(textPane.getPageFormatWithZoom());
//			d.height = getParent().getHeight();
//			if(super.getPreferredSize().getHeight() > d.height) {
				d.height = super.getPreferredSize().height;
//			}
			
			return d;
		}
		
		@Override
		public Dimension getMinimumSize() {
			Dimension size = getPreferredSize();
//			if(size.height < minHeight) {
//				size.height = minHeight;
//			}
			return size;
		}
		
		@Override
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}
		
		@Override
		protected void paintChildren(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(getParent().getBackground());
			g.drawRect(0, 0, getSize().width - 1, topSpace);
			g.fillRect(0, 0, getSize().width - 1, topSpace);
			g.setColor(Color.black);
			g.drawRect(0, topSpace, getSize().width - 2, getSize().height);
			g.drawLine(0, topSpace, 0, getSize().height);
			g.fillRect(getSize().width - 2, topSpace + 1, 3, getSize().height);
			super.paintChildren(g);
		}
		
		@Override
		protected void paintBorder(Graphics g) {
//			g.setColor(textPane.getParent().getBackground());
//			g.drawRect(0, 0, getSize().width - 1, topSpace);
//			g.fillRect(0, 0, getSize().width - 1, topSpace);
//			g.setColor(Color.black);
//			g.drawRect(0, topSpace, getSize().width - 2, getSize().height);
//			g.drawLine(0, topSpace, 0, getSize().height);
//			g.fillRect(getSize().width - 2, topSpace + 1, 3, getSize().height);
		}

		@Override
		public Insets getInsets() {
			Insets i = (Insets) textPane.getPageMarginWithZoom().clone();
			i.top += topSpace;
			i.bottom += 10;
			return i;
		}
		
		@Override
		public boolean isOpaque() {
			return true;
		}
}
