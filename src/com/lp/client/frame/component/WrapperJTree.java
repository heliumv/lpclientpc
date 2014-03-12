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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.RepaintManager;
import javax.swing.Scrollable;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class WrapperJTree extends JTree implements Printable, Scrollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6110129760436011601L;

	public WrapperJTree(DefaultTreeModel m){
		super(m);
	}

	public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.black);
		RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);
		Dimension d = this.getSize();
		double panelWidth  = d.width;
		double panelHeight = d.height;
		double pageHeight = pf.getImageableHeight();
		double pageWidth  = pf.getImageableWidth();
		double scale = pageWidth/panelWidth;
		scale = scale > 0.7 ? 0.7:scale;
		int totalNumPages = (int)Math.ceil(scale * panelHeight / pageHeight);
		//System.out.println(panelHeight);
		//System.out.println(pageHeight);
		//System.out.println(totalNumPages);
		if(pageIndex >= totalNumPages) {
			//sec.util.Disp.printError("No such page(s)!");
			return Printable.NO_SUCH_PAGE;
		}
		// Shift Graphic to line up with beginning of print-imageable region
		g2.translate(pf.getImageableX(), pf.getImageableY());
		// Shift Graphic to line up with beginning of next page to print
		g2.translate(0f, -pageIndex * pageHeight);
		// Scale the page so the width fits...
		g2.scale(scale, scale);
		this.paint(g2);   //repaint the page for printing
		return Printable.PAGE_EXISTS;
	}

	public void printContent(){
		PrinterJob pj=PrinterJob.getPrinterJob();
		pj.setPrintable(this);
		if (pj.printDialog())
			try{
				pj.print();
			} catch (Exception PrintException) {}
	}

	public void expandAll(TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(path, expand);
            }
        }
    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            this.expandPath(parent);
        } else {
            this.collapsePath(parent);
        }
    }
	
//	@Override
//	public boolean getScrollableTracksViewportWidth() {
//		return false;
//	}
}
