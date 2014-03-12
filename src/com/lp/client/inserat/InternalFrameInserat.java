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
package com.lp.client.inserat;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.auftrag.TabbedPaneAuftrag;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragartDto;
import com.lp.server.auftrag.service.AuftragpositionArtDto;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.auftrag.service.AuftragtextDto;
import com.lp.server.benutzer.service.RechteFac;

@SuppressWarnings("static-access")
public class InternalFrameInserat extends InternalFrame {

	private static final long serialVersionUID = 1L;
	private TabbedPaneInserat tpInserat = null;

	public static final int IDX_TABBED_PANE_INSERAT = 0;

	public InternalFrameInserat(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {

		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		tabbedPaneRoot.insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("iv.inserat.modulname"), null,
				null,
				LPMain.getInstance()
						.getTextRespectUISPr("iv.inserat.modulname"),
				IDX_TABBED_PANE_INSERAT);

		// Default TabbedPane setzen
		refreshTabbedPaneInserat();
		tpInserat.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tpInserat);

		// dem frame das icon setzen
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/news16x16.png"));
		setFrameIcon(iicon);

		// ich selbst moechte informiert werden.
		addItemChangedListener(this);
		// listener bin auch ich
		registerChangeListeners();
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();

		int selectedCur = 0;

		try {
			selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();
		} catch (Exception ex) {

			selectedCur = ((com.lp.client.pc.Desktop) e.getSource())
					.getSelectedIndex();
		}

		if (selectedCur == IDX_TABBED_PANE_INSERAT) {
			refreshTabbedPaneInserat();

			// Info an Tabbedpane, bist selektiert worden.
			tpInserat.lPEventObjectChanged(null);
		}

	}

	private void refreshTabbedPaneInserat() throws Throwable {
		if (tpInserat == null) {
			tpInserat = new TabbedPaneInserat(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_INSERAT, tpInserat);
			initComponents();
		}
	}

	public TabbedPaneInserat getTabbedPaneInserat() throws Throwable {
		return tpInserat;
	}

}
