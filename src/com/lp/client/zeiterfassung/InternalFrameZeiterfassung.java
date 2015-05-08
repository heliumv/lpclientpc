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
package com.lp.client.zeiterfassung;

import java.beans.PropertyVetoException;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenzmDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.MandantFac;

/**
 * <p>
 * &UUml;berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author Christian Kollmann
 * @version $Revision: 1.6 $
 */

public class InternalFrameZeiterfassung extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneZeiterfassung tabbedPaneZeiterfassung = null;
	private TabbedPaneSondertaetigkeiten tabbedPaneSondertaetigkeiten = null;
	private TabbedPaneMaschinen tabbedPaneMaschinen = null;
	private TabbedPaneZeitstifte tabbedPaneZeitstifte = null;
	private TabbedPaneMaschinenzeitmodell tabbedPaneMaschinenzeitmodell = null;
	private TabbedPaneDiaeten tabbedPaneDiaeten = null;
	private TabbedPaneZeiterfassunggrunddaten tabbedPaneZeiterfassunggrunddaten = null;

	public static int IDX_TABBED_PANE_ZEITERFASSUNG = -1;
	public static int IDX_TABBED_PANE_SONDERTAETIGKEITEN = -1;
	public int IDX_TABBED_PANE_MASCHINEN = -1;
	public int IDX_TABBED_PANE_MASCHINENZEITMODELL = -1;
	public int IDX_TABBED_PANE_ZEITSTIFTE = -1;
	public int IDX_TABBED_PANE_DIAETEN = -1;
	public int IDX_TABBED_PANE_GRUNDDATEN = -1;

	public boolean bRechtNurBuchen = true;

	private PersonalDto personalDto = null;

	private MaschineDto maschineDto = null;

	private DiaetenDto diaetenDto = null;
	
	public MaschinenzmDto getMaschinenzmDto() {
		return maschinenzmDto;
	}

	public void setMaschinenzmDto(MaschinenzmDto maschinenzmDto) {
		this.maschinenzmDto = maschinenzmDto;
	}

	private MaschinenzmDto maschinenzmDto=null;

	public DialogLoseEinesTechnikers dialogLoseEinesTechnikers = null;

	public void setPersonalDto(PersonalDto personalDto) {
		this.personalDto = personalDto;
	}

	public PersonalDto getPersonalDto() {
		return personalDto;
	}

	public void setMaschineDto(MaschineDto maschineDto) {
		this.maschineDto = maschineDto;
	}

	public void setDiaetenDto(DiaetenDto diaetenDto) {
		this.diaetenDto = diaetenDto;
	}

	public MaschineDto getMaschineDto() {
		return maschineDto;
	}

	public PropertyVetoException vetoableChangeLP() throws Throwable {
		if (dialogLoseEinesTechnikers != null) {
			dialogLoseEinesTechnikers.dispose();
			dialogLoseEinesTechnikers = null;
		}

		return super.vetoableChangeLP();
	}

	public TabbedPaneZeiterfassung getTabbedPaneZeiterfassung() {
		return tabbedPaneZeiterfassung;
	}

	public TabbedPaneMaschinen getTabbedPaneMaschinen() {
		return tabbedPaneMaschinen;
	}

	public TabbedPaneDiaeten getTabbedPaneDiaeten() {
		return tabbedPaneDiaeten;
	}

	public DiaetenDto getDiaetenDto() {
		return diaetenDto;
	}

	public InternalFrameZeiterfassung(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {
		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// TabbedPane Personal
		bRechtNurBuchen = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_ZEITEINGABE_NUR_BUCHEN);

		int tabIndex = 0;
		IDX_TABBED_PANE_ZEITERFASSUNG = tabIndex;
		tabbedPaneRoot.insertTab(
				LPMain.getTextRespectUISPr("zeiterfassung.modulname"), null,
				null, LPMain.getTextRespectUISPr("zeiterfassung.modulname"),
				IDX_TABBED_PANE_ZEITERFASSUNG);

		if (bRechtNurBuchen == false) {

			tabIndex++;
			IDX_TABBED_PANE_SONDERTAETIGKEITEN = tabIndex;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("zeiterfassung.title.tab.sondertaetigkeiten"),
							null,
							null,
							LPMain.getTextRespectUISPr("zeiterfassung.title.tab.sondertaetigkeiten"),
							IDX_TABBED_PANE_SONDERTAETIGKEITEN);
		}
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {
			tabIndex++;
			IDX_TABBED_PANE_MASCHINEN = tabIndex;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("zeiterfassung.title.tab.maschinen"),
							null,
							null,
							LPMain.getTextRespectUISPr("zeiterfassung.title.tab.maschinen"),
							IDX_TABBED_PANE_MASCHINEN);
			
			tabIndex++;
			IDX_TABBED_PANE_MASCHINENZEITMODELL = tabIndex;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("pers.maschinenzeitmodell"),
							null,
							null,
							LPMain.getTextRespectUISPr("pers.maschinenzeitmodell"),
							IDX_TABBED_PANE_MASCHINENZEITMODELL);
			

		}
		// zusatzberecht:3 So wir auf die neue Berechtigung abgefragt
		if (bRechtNurBuchen == false) {
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_STIFTZEITERFASSUNG)) {
				tabIndex++;
				IDX_TABBED_PANE_ZEITSTIFTE = tabIndex;

				tabbedPaneRoot
						.insertTab(
								LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitstifte"),
								null,
								null,
								LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitstifte"),
								IDX_TABBED_PANE_ZEITSTIFTE);
			}
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_REISEZEITEN)) {
				tabIndex++;
				IDX_TABBED_PANE_DIAETEN = tabIndex;

				tabbedPaneRoot
						.insertTab(
								LPMain.getTextRespectUISPr("pers.zeiterfassung.diaeten"),
								null,
								null,
								LPMain.getTextRespectUISPr("pers.zeiterfassung.diaeten"),
								IDX_TABBED_PANE_DIAETEN);

			}

			tabIndex++;
			IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
			// nur anzeigen wenn Benutzer Recht dazu hat
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
				tabbedPaneRoot
						.insertTab(
								LPMain.getTextRespectUISPr("pers.title.tab.grunddaten"),
								null,
								null,
								LPMain.getTextRespectUISPr("pers.title.tab.grunddaten"),
								IDX_TABBED_PANE_GRUNDDATEN);
			}
		}
		registerChangeListeners();
		createTabbedPaneZeiterfassung(null);
		tabbedPaneZeiterfassung.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tabbedPaneZeiterfassung);
		// iicon: hier das li/on icon gemacht
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/clock16x16.png"));
		setFrameIcon(iicon);
	}

	private void createTabbedPaneZeiterfassung(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneZeiterfassung = new TabbedPaneZeiterfassung(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ZEITERFASSUNG,
					tabbedPaneZeiterfassung);

			if (tabbedPaneZeiterfassung.getPanelQueryPersonal().getSelectedId() == null) {
				enableAllPanelsExcept(false);
			}
			initComponents();
		}
	}

	private void createTabbedPaneSonderttaetigkeiten(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneSondertaetigkeiten = new TabbedPaneSondertaetigkeiten(
					this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_SONDERTAETIGKEITEN,
					tabbedPaneSondertaetigkeiten);
			initComponents();
		}
	}

	private void createTabbedPaneDiaeten(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneDiaeten = new TabbedPaneDiaeten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_DIAETEN,
					tabbedPaneDiaeten);
			initComponents();
		}
	}

	private void createTabbedPaneMaschinen(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneMaschinen = new TabbedPaneMaschinen(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_MASCHINEN,
					tabbedPaneMaschinen);
			initComponents();
		}
	}
	private void createTabbedPaneMaschinenzeitmodell(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneMaschinenzeitmodell = new TabbedPaneMaschinenzeitmodell(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_MASCHINENZEITMODELL,
					tabbedPaneMaschinenzeitmodell);
			initComponents();
		}
	}

	private void createTabbedPaneZeitstifte(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneZeitstifte = new TabbedPaneZeitstifte(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ZEITSTIFTE,
					tabbedPaneZeitstifte);
			initComponents();
		}
	}

	private void createTabbedPaneGrunddaten(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneZeiterfassunggrunddaten = new TabbedPaneZeiterfassunggrunddaten(
					this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneZeiterfassunggrunddaten);
			initComponents();
		}
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_TABBED_PANE_ZEITERFASSUNG) {
			createTabbedPaneZeiterfassung(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneZeiterfassung.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_SONDERTAETIGKEITEN) {
			createTabbedPaneSonderttaetigkeiten(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneSondertaetigkeiten.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_MASCHINEN) {
			createTabbedPaneMaschinen(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneMaschinen.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_MASCHINENZEITMODELL) {
			createTabbedPaneMaschinenzeitmodell(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneMaschinenzeitmodell.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_ZEITSTIFTE) {
			createTabbedPaneZeitstifte(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneZeitstifte.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_DIAETEN) {
			createTabbedPaneDiaeten(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneDiaeten.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			createTabbedPaneGrunddaten(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneZeiterfassunggrunddaten.lPEventObjectChanged(null);
		}
	}

}
