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
 *******************************************************************************/
package com.lp.client.media;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.pc.LPMain;

public class InternalFrameMedia extends InternalFrame {
	private static final long serialVersionUID = -2466508157898155832L;

	public static final int IDX_TABBED_PANE_MEDIAINBOX = 0;

	private TabbedPaneMedia tpMedia = null;

	public InternalFrameMedia(String title, String belegartCnr, String rechtModulweit) throws Throwable {
		super(title, belegartCnr, rechtModulweit) ;
		jbInit() ;
		initComponents() ;
	}
	
	
	private void jbInit() throws Throwable {
		tabbedPaneRoot.insertTab(
				LPMain.getTextRespectUISPr("media.inbox.modulname"), null,
				null,
				LPMain.getTextRespectUISPr("media.inbox.modulname"),
				IDX_TABBED_PANE_MEDIAINBOX);

		// Default TabbedPane setzen
		refreshTabbedPaneMedia();
		tpMedia.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tpMedia);

		// dem frame das icon setzen
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/inbox_into16x16.png"));
		setFrameIcon(iicon);

		// ich selbst moechte informiert werden.
//		addItemChangedListener(this);
		// listener bin auch ich
		registerChangeListeners();		
	}
	
	@Override
	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();

		int selectedCur = 0;

		try {
			selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();
			if(selectedCur == IDX_TABBED_PANE_MEDIAINBOX) {
				getTabbedPaneMedia().lPEventObjectChanged(null);
			}
		} catch (Exception ex) {

			selectedCur = ((com.lp.client.pc.Desktop) e.getSource())
					.getSelectedIndex();
		}

		if (selectedCur == IDX_TABBED_PANE_MEDIAINBOX) {
			refreshTabbedPaneMedia(); 

			// Info an Tabbedpane, bist selektiert worden.
			tpMedia.lPEventObjectChanged(null);
		}
	}

	private void refreshTabbedPaneMedia() throws Throwable {
		if (tpMedia == null) {
			tpMedia = new TabbedPaneMedia(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_MEDIAINBOX, tpMedia);
			initComponents();
		}
	}

	public TabbedPaneMedia getTabbedPaneMedia() throws Throwable {
		return tpMedia;
	}
	
	protected void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {	
	}
	
}
