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
package com.lp.client.finanz;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich FB-Grundaten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 18.07.05</p>
 *
 * <p>@author $Author: christoph $</p>
 *
 * @version $Revision: 1.12 $ Date $Date: 2011/10/18 09:07:04 $
 */
public class TabbedPaneGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static int IDX_KONTOART = 0;
	private final static int IDX_UVAART = 1;
	private final static int IDX_LAENDERART = 2;
	private final static int IDX_STEUERKATEGORIE = 3;
	private final static int IDX_STEUERKATEGORIEKONTO = 4;

	private PanelQuery panelKontoartQP1 = null;
	private PanelStammdatenCRUD panelKontoartBottomD1 = null;
	private PanelSplit panelKontoartSP1 = null;

	private PanelQuery panelUvaartQP1 = null;
	private PanelStammdatenCRUD panelUvaartBottomD1 = null;
	private PanelSplit panelUvaartSP1 = null;

	private PanelQuery panelLaenderartQP1 = null;
	private PanelStammdatenCRUD panelLaenderartBottomD1 = null;
	private PanelSplit panelLaenderartSP1 = null;


	public TabbedPaneGrunddaten(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"finanz.tab.unten.grunddaten.title"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		// Tab 1: Kontoart
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.oben.kontoart.title"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.oben.kontoart.tooltip"), IDX_KONTOART);
		// Tab 2: Uvaart
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.oben.uvaart.title"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.oben.uvaart.tooltip"), IDX_UVAART);
		// Tab 3: Laenderart
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.oben.laenderart.title"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.oben.laenderart.tooltip"), IDX_LAENDERART);
		// Default
		refreshKontoart();

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * Behandle ActionEvent; zB Menue-Klick oben.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void lPActionEvent(ActionEvent e) throws Throwable {
		// nothing here
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelKontoartQP1) {
				String cNr = (String) panelKontoartQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelKontoartBottomD1.setKeyWhenDetailPanel(cNr);
				panelKontoartBottomD1.eventYouAreSelected(false);
				panelKontoartQP1.updateButtons();
			} else if (e.getSource() == panelUvaartQP1) {
				Integer iId = (Integer) panelUvaartQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelUvaartBottomD1.setKeyWhenDetailPanel(iId);
				panelUvaartBottomD1.eventYouAreSelected(false);
				panelUvaartQP1.updateButtons();
			} else if (e.getSource() == panelLaenderartQP1) {
				String cNr = (String) panelLaenderartQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelLaenderartBottomD1.setKeyWhenDetailPanel(cNr);
				panelLaenderartBottomD1.eventYouAreSelected(false);
				panelLaenderartQP1.updateButtons();
			} 
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelKontoartBottomD1) {
				panelKontoartBottomD1.eventYouAreSelected(false);
			} else if (e.getSource() == panelUvaartBottomD1) {
				panelUvaartBottomD1.eventYouAreSelected(false);
			} else if (e.getSource() == panelLaenderartBottomD1) {
				panelLaenderartBottomD1.eventYouAreSelected(false);
			} 
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelKontoartBottomD1) {
				Object oKey = panelKontoartBottomD1.getKeyWhenDetailPanel();
				panelKontoartQP1.eventYouAreSelected(false);
				panelKontoartQP1.setSelectedId(oKey);
				panelKontoartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelUvaartBottomD1) {
				Object oKey = panelUvaartBottomD1.getKeyWhenDetailPanel();
				panelUvaartQP1.eventYouAreSelected(false);
				panelUvaartQP1.setSelectedId(oKey);
				panelUvaartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelLaenderartBottomD1) {
				Object oKey = panelLaenderartBottomD1.getKeyWhenDetailPanel();
				panelLaenderartQP1.eventYouAreSelected(false);
				panelLaenderartQP1.setSelectedId(oKey);
				panelLaenderartSP1.eventYouAreSelected(false);
			} 
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelKontoartBottomD1) {
				panelKontoartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelUvaartBottomD1) {
				panelUvaartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelLaenderartBottomD1) {
				panelLaenderartSP1.eventYouAreSelected(false);
			}  
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelKontoartQP1) {
				// kontoarten duerfen nicht angelegt werden
			}
			if (eI.getSource() == panelUvaartQP1) {
				// UVAarten duerfen nicht angelegt werden
			}
			if (eI.getSource() == panelLaenderartQP1) {
				// Laenderarten duerfen nicht angelegt werden
			}
			
		}
	}

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_KONTOART:
			refreshKontoart();
			panelKontoartSP1.eventYouAreSelected(false);
			break;
		case IDX_UVAART:
			refreshUvaart();
			panelUvaartSP1.eventYouAreSelected(false);
			break;
		case IDX_LAENDERART:
			refreshLaenderart();
			panelLaenderartSP1.eventYouAreSelected(false);
			break;
		
		
		
		} 
		
	}

	private void refreshKontoart() throws Throwable {

		if (panelKontoartSP1 == null) {

			panelKontoartQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_KONTOART, null, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"finanz.tab.oben.kontoart.title"), true); // liste
																		// refresh
																		// wenn
																		// lasche
																		// geklickt
																		// wurde

			panelKontoartBottomD1 = new PanelStammdatenCRUD(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.detail"),
					null, HelperClient.SCRUD_KONTOART_FILE, getInternalFrame(),
					HelperClient.LOCKME_KONTOART);

			panelKontoartSP1 = new PanelSplit(getInternalFrame(),
					panelKontoartBottomD1, panelKontoartQP1, 200);
			setComponentAt(IDX_KONTOART, panelKontoartSP1);

			// liste soll sofort angezeigt werden
			panelKontoartQP1.eventYouAreSelected(true);
		}
	}

	private void refreshUvaart() throws Throwable {

		if (panelUvaartSP1 == null) {

			FilterKriterium[] fkneu = new FilterKriterium[1];
			
			fkneu[0] = new FilterKriterium("mandantCNr", true, 
					"'" + LPMain.getInstance().getTheClient().getMandant() + "'", 
					FilterKriterium.OPERATOR_EQUAL, false);
			
			panelUvaartQP1 = new PanelQuery(null, fkneu,
					QueryParameters.UC_ID_UVAART, null, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"finanz.tab.oben.uvaart.title"), true);

			panelUvaartBottomD1 = new PanelStammdatenCRUD(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.detail"),
					null, HelperClient.SCRUD_UVAART_FILE, getInternalFrame(),
					HelperClient.LOCKME_UVAART);

			panelUvaartSP1 = new PanelSplit(getInternalFrame(),
					panelUvaartBottomD1, panelUvaartQP1, 200);
			setComponentAt(IDX_UVAART, panelUvaartSP1);

			// liste soll sofort angezeigt werden
			panelUvaartQP1.eventYouAreSelected(true);
		}
	}

	private void refreshLaenderart() throws Throwable {

		if (panelLaenderartSP1 == null) {

			panelLaenderartQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_LAENDERART, null, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"finanz.tab.oben.laenderart.title"), true);

			panelLaenderartBottomD1 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), null,
					HelperClient.SCRUD_LAENDERART_FILE, getInternalFrame(),
					HelperClient.LOCKME_LAENDERART);

			panelLaenderartSP1 = new PanelSplit(getInternalFrame(),
					panelLaenderartBottomD1, panelLaenderartQP1, 200);
			setComponentAt(IDX_LAENDERART, panelLaenderartSP1);

			// liste soll sofort angezeigt werden
			panelLaenderartQP1.eventYouAreSelected(true);
		}
	}

	
	
	

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

}
