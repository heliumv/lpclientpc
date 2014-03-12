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
package com.lp.client.frame.component;

import java.awt.Graphics;

import javax.swing.JLabel;

public class AutoSizeLabel extends JLabel {
	
	private static final long serialVersionUID = -4431141056670925313L;

	protected void paintComponent(Graphics g) {
//		if (true) {
			super.paintComponent(g);
//		} else {
//			ImageIcon icon = (ImageIcon) getIcon();  
//			if (icon == null) {
//				super.paintComponent(g);
//			} else {
//				int iconWidth = icon.getIconWidth();  
//				int iconHeight = icon.getIconHeight();  
//				double iconAspect = (double) iconHeight / iconWidth;  
//				int w = ((JViewport)getParent()).getWidth();  
//				int h = ((JViewport)getParent()).getHeight();  
//				double canvasAspect = (double) h / w;  
//				int x = 0, y = 0;    // Maintain aspect ratio.  
//				if(iconAspect < canvasAspect)  {   
//					// Drawing space is taller than image.   
//					y = h;   
//					h = (int) (w * iconAspect);   
//					y = (y - h) / 2; // center it along vertical 
//				}  else  {   
//					// Drawing space is wider than image.   
//					x = w;   
//					w = (int) (h / iconAspect);   
//					x = (x - w) / 2; // center it along horizontal  
//				}        
//				Image img = icon.getImage();  
//				g.drawImage(img, x, y, w + x, h + y, 0, 0, iconWidth, iconHeight, null); 
//			}
//		}
	}
}
