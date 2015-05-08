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
package com.lp.client.instandhaltung;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.kueche.TabbedPaneKueche;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.1 $
 */
public class InternalFrameInstandhaltung extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneInstandhaltung tabbedPaneInstandhaltung = null;
	private TabbedPaneInstandhaltungGrunddaten tabbedPaneGrunddaten = null;

	private int IDX_TABBED_PANE_INSTANDHALTUNG = -1;
	private int IDX_TABBED_PANE_GRUNDDATEN = -1;



	public TabbedPaneInstandhaltung getTabbedPaneInstandhaltung() {
		return tabbedPaneInstandhaltung;
	}

	public InternalFrameInstandhaltung(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {
		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// TabbedPane Personal
		int tabIndex = 0;
		IDX_TABBED_PANE_INSTANDHALTUNG = tabIndex;

		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"is.instandhaltung"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("is.instandhaltung"), IDX_TABBED_PANE_INSTANDHALTUNG);

		
		// nur anzeigen wenn Benutzer Recht dazu hat
		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(
				RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
			tabIndex++;
			IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
			tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
					"pers.title.tab.grunddaten"), null, null, LPMain
					.getInstance().getTextRespectUISPr(
							"pers.title.tab.grunddaten"),
					IDX_TABBED_PANE_GRUNDDATEN);
		}
	

		registerChangeListeners();
		createTabbedPaneReklamation(null);
		tabbedPaneInstandhaltung.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tabbedPaneInstandhaltung);
		// iicon: hier das li/on icon gemacht
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/houses.png"));
		setFrameIcon(iicon);

	}

	private void createTabbedPaneReklamation(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneInstandhaltung = new TabbedPaneInstandhaltung(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_INSTANDHALTUNG,
					tabbedPaneInstandhaltung);
			initComponents();

		}
	}

	private void createTabbedPaneGrunddaten(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneGrunddaten = new TabbedPaneInstandhaltungGrunddaten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneGrunddaten);
			initComponents();
		}
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_TABBED_PANE_INSTANDHALTUNG) {
			createTabbedPaneReklamation(tabbedPane);
			tabbedPaneInstandhaltung.lPEventObjectChanged(null);
		}

		else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			createTabbedPaneGrunddaten(tabbedPane);
			tabbedPaneGrunddaten.lPEventObjectChanged(null);
		} 
	}

}
