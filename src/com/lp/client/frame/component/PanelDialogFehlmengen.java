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
package com.lp.client.frame.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class PanelDialogFehlmengen extends JDialog {

	private static final long serialVersionUID = 8827358190838366999L;
	
	public PanelDialogFehlmengen(Frame owner, String title, JPanel jpanel) {
		this(owner, title, jpanel, null) ;
	}
	
	public PanelDialogFehlmengen(Frame owner, String title, JPanel jpanel, JComponent placeOnWindow) {
		super(owner, title, true) ;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE) ;

		jbInit(jpanel, placeOnWindow) ;
		pack() ;
	}
	
	public void jbInit(JPanel jpanel, JComponent placeOnWindow) {
		getContentPane().add(jpanel, BorderLayout.CENTER) ;		
		relocateTo(placeOnWindow) ;
	}
	
	protected void relocateTo(JComponent placeOnWindow) {
		if(placeOnWindow != null) {
			Point location = placeOnWindow.getLocationOnScreen() ;
			setLocation(location);
			Dimension d = placeOnWindow.getSize() ;
			setMinimumSize(d); 
			setMaximumSize(d);
			setPreferredSize(d);	
		}		
	}
}
