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
package com.lp.client.angebotstkl;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class InternalFrameAngebotstkl extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneAngebotstkl tabbedPaneAngebotstkl = null;
	private TabbedPaneEinkaufsangebot tabbedPaneEinkaufsangebot = null;
	private TabbedPaneAngebotstklGrunddaten tabbedPaneAngebotstklGrunddaten = null;

	public int iKalkulationsart = 1;

	private int IDX_TABBED_PANE_ANGEBOTSTKL = -1;
	private int IDX_TABBED_PANE_EINKAUFSANGEBOT = -1;
	private int IDX_TABBED_PANE_GRUNDDATEN = -1;

	private AgstklDto agstklDto = null;
	private EinkaufsangebotDto einkaufsangebotDto = null;

	public void setAgstklDto(AgstklDto agstklDto) {
		this.agstklDto = agstklDto;
	}

	public void setEinkaufsangebotDto(EinkaufsangebotDto einkaufsangebotDto) {
		this.einkaufsangebotDto = einkaufsangebotDto;
	}

	public TabbedPaneAngebotstkl getTabbedPaneAngebotstkl() {
		return tabbedPaneAngebotstkl;
	}

	public TabbedPaneEinkaufsangebot getTabbedPaneEinkaufsangebot() {
		return tabbedPaneEinkaufsangebot;
	}

	public AgstklDto getAgstklDto() {
		return agstklDto;
	}

	public EinkaufsangebotDto getEinkaufsangebotDto() {
		return einkaufsangebotDto;
	}

	public InternalFrameAngebotstkl(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {
		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// TabbedPane Personal

		int tabIndex = 0;
		IDX_TABBED_PANE_ANGEBOTSTKL = tabIndex;

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
						ParameterFac.PARAMETER_KALKULATIONSART);
		iKalkulationsart = (java.lang.Integer) parameter.getCWertAsObject();

		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"lp.angebotsstueckliste"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"lp.angebotsstueckliste"), IDX_TABBED_PANE_ANGEBOTSTKL);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_EINKAUFSANGEBOT)) {
			tabIndex++;
			IDX_TABBED_PANE_EINKAUFSANGEBOT = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"lp.einkaufsangebot"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"lp.einkaufsangebot"),
					IDX_TABBED_PANE_EINKAUFSANGEBOT);
		}

		if (iKalkulationsart == 2) {
			tabIndex++;
			IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
					IDX_TABBED_PANE_GRUNDDATEN);
		}

		registerChangeListeners();
		createTabbedAngebotstkl(null);
		tabbedPaneAngebotstkl.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tabbedPaneAngebotstkl);
		// iicon: hier das li/on icon gemacht
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/note_add16x16.png"));
		setFrameIcon(iicon);

	}

	private void createTabbedAngebotstkl(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneAngebotstkl = new TabbedPaneAngebotstkl(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ANGEBOTSTKL,
					tabbedPaneAngebotstkl);
			if (tabbedPaneAngebotstkl.getPanelQueryPersonal().getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPaneAngebotstkl, 0, false);
			}
			initComponents();
		}
	}

	private void createTabbedEinkaufsangebot(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneEinkaufsangebot = new TabbedPaneEinkaufsangebot(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_EINKAUFSANGEBOT,
					tabbedPaneEinkaufsangebot);
			if (tabbedPaneEinkaufsangebot.getPanelQueryEinkaufsangebot()
					.getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPaneEinkaufsangebot, 0,
						false);
			}
			initComponents();
		}
	}

	private void createTabbedGrunddaten(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneAngebotstklGrunddaten = new TabbedPaneAngebotstklGrunddaten(
					this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneAngebotstklGrunddaten);

			initComponents();
		}
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_TABBED_PANE_ANGEBOTSTKL) {
			createTabbedAngebotstkl(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneAngebotstkl.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_EINKAUFSANGEBOT) {
			createTabbedEinkaufsangebot(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneEinkaufsangebot.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			createTabbedGrunddaten(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneAngebotstklGrunddaten.lPEventObjectChanged(null);
		}

	}
}
