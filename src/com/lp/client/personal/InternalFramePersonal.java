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
package com.lp.client.personal;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.BereitschaftartDto;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.personal.service.KollektivDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeitmodellDto;
import com.lp.server.system.service.LocaleFac;
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

public class InternalFramePersonal extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPanePersonal tabbedPanePersonal = null;
	private TabbedPanePersonalgrunddaten tabbedPaneGrunddaten = null;
	private TabbedPaneZeitmodell tabbedPaneZeitmodell = null;
	private TabbedPaneBetriebskalender tabbedPaneBetriebskalender = null;
	private TabbedPaneArtikelzulage tabbedPaneArtikelzulage = null;
	private TabbedPaneKollektiv tabbedPaneKollektiv = null;
	private TabbedPanePersonalgruppe tabbedPanePersonalgruppe = null;
	private TabbedPaneBereitschaft tabbedPaneBereitschaft = null;
	private TabbedPaneFahrzeug tabbedPaneFahrzeug = null;
	public SchichtzeitEinstellungen schichtzeitEinstellungen = null;

	private int IDX_TABBED_PANE_PERSONAL = -1;
	private int IDX_TABBED_PANE_ZEITMODELL = -1;
	private int IDX_TABBED_PANE_BEREITSCHAFT = -1;
	private int IDX_TABBED_PANE_BETRIEBSKALENDER = -1;
	private int IDX_TABBED_PANE_ARTIKELZULAGE = -1;
	private int IDX_TABBED_PANE_KOLLEKTIV = -1;
	private int IDX_TABBED_PANE_PERSONALGRUPPEN = -1;
	private int IDX_TABBED_PANE_GRUNDDATEN = -1;
	private int IDX_TABBED_PANE_FAHRZEUG = -1;

	private PersonalDto personalDto = null;
	private ZeitmodellDto zeitmodellDto = null;
	private KollektivDto kollektivDto = null;
	private FahrzeugDto fahrzeugDto = null;

	public FahrzeugDto getFahrzeugDto() {
		return fahrzeugDto;
	}

	public void setFahrzeugDto(FahrzeugDto fahrzeugDto) {
		this.fahrzeugDto = fahrzeugDto;
	}

	private BereitschaftartDto bereitschaftartDto = null;

	public BereitschaftartDto getBereitschaftartDto() {
		return bereitschaftartDto;
	}

	public void setBereitschaftartDto(BereitschaftartDto bereitschaftartDto) {
		this.bereitschaftartDto = bereitschaftartDto;
	}

	public void setPersonalDto(PersonalDto personalDto) {
		this.personalDto = personalDto;
	}

	public void setZeitmodellDto(ZeitmodellDto zeitmodellDto) {
		this.zeitmodellDto = zeitmodellDto;
	}

	public void setKollektivDto(KollektivDto kollektivDto) {
		this.kollektivDto = kollektivDto;
	}

	public void setSchichtzeitEinstellungen(
			SchichtzeitEinstellungen schichtzeitEinstellungen) {
		this.schichtzeitEinstellungen = schichtzeitEinstellungen;
	}

	public TabbedPanePersonal getTabbedPanePersonal() {
		return tabbedPanePersonal;
	}

	public TabbedPanePersonalgruppe getTabbedPanePersonalgruppe() {
		return tabbedPanePersonalgruppe;
	}

	public PersonalDto getPersonalDto() {
		return personalDto;
	}

	public ZeitmodellDto getZeitmodellDto() {
		return zeitmodellDto;
	}

	public KollektivDto getKollektivDto() {
		return kollektivDto;
	}

	public SchichtzeitEinstellungen getSchichtzeitEinstellungen() {
		return schichtzeitEinstellungen;
	}

	public InternalFramePersonal(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {
		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// TabbedPane Personal

		int tabIndex = 0;
		IDX_TABBED_PANE_PERSONAL = tabIndex;

		tabbedPaneRoot.insertTab(
				LPMain.getTextRespectUISPr("menueentry.personal"), null, null,
				LPMain.getTextRespectUISPr("menueentry.personal"),
				IDX_TABBED_PANE_PERSONAL);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PERSONAL)) {
			tabIndex++;
			IDX_TABBED_PANE_ZEITMODELL = tabIndex;

			tabbedPaneRoot.insertTab(
					LPMain.getTextRespectUISPr("pers.title.tab.zeitmodell"),
					null, null,
					LPMain.getTextRespectUISPr("pers.title.tab.zeitmodell"),
					IDX_TABBED_PANE_ZEITMODELL);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_BEREITSCHAFT)) {
				tabIndex++;
				IDX_TABBED_PANE_BEREITSCHAFT = tabIndex;

				tabbedPaneRoot.insertTab(
						LPMain.getTextRespectUISPr("pers.bereitschaft"), null,
						null, LPMain.getTextRespectUISPr("pers.bereitschaft"),
						IDX_TABBED_PANE_BEREITSCHAFT);
			}

			tabIndex++;
			IDX_TABBED_PANE_BETRIEBSKALENDER = tabIndex;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("pers.title.tab.betriebskalender"),
							null,
							null,
							LPMain.getTextRespectUISPr("pers.title.tab.betriebskalender"),
							IDX_TABBED_PANE_BETRIEBSKALENDER);

			tabIndex++;
			IDX_TABBED_PANE_ARTIKELZULAGE = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getTextRespectUISPr("pers.artikelzulage"), null,
					null, LPMain.getTextRespectUISPr("pers.artikelzulage"),
					IDX_TABBED_PANE_ARTIKELZULAGE);

			tabIndex++;
			IDX_TABBED_PANE_KOLLEKTIV = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getTextRespectUISPr("pers.personaldaten.kollektiv"),
					null, null,
					LPMain.getTextRespectUISPr("pers.personaldaten.kollektiv"),
					IDX_TABBED_PANE_KOLLEKTIV);
			tabIndex++;
			IDX_TABBED_PANE_PERSONALGRUPPEN = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getTextRespectUISPr("pers.personalgruppe"), null,
					null, LPMain.getTextRespectUISPr("pers.personalgruppe"),
					IDX_TABBED_PANE_PERSONALGRUPPEN);
			tabIndex++;
			IDX_TABBED_PANE_FAHRZEUG = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getTextRespectUISPr("pers.fahrzeug"), null, null,
					LPMain.getTextRespectUISPr("pers.fahrzeug"),
					IDX_TABBED_PANE_FAHRZEUG);

			// nur anzeigen wenn Benutzer Recht dazu hat
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
				tabIndex++;
				IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
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
		createTabbedPanePersonal(null);
		tabbedPanePersonal.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tabbedPanePersonal);
		// iicon: hier das li/on icon gemacht
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/worker16x16.png"));
		setFrameIcon(iicon);

	}

	private void createTabbedPanePersonal(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPanePersonal = new TabbedPanePersonal(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_PERSONAL,
					tabbedPanePersonal);
			if (tabbedPanePersonal.getPanelQueryPersonal().getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPanePersonal, 0, false);
			}
			initComponents();
		}

	}

	private void createTabbedPaneGrunddaten(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneGrunddaten = new TabbedPanePersonalgrunddaten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneGrunddaten);
			initComponents();
		}
	}

	private void createTabbedPanePersonalgruppe(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPanePersonalgruppe = new TabbedPanePersonalgruppe(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_PERSONALGRUPPEN,
					tabbedPanePersonalgruppe);
			initComponents();
		}
	}

	private void createTabbedPaneFahrzeug(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneFahrzeug = new TabbedPaneFahrzeug(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_FAHRZEUG,
					tabbedPaneFahrzeug);
			initComponents();
		}
	}

	private void createTabbedPaneArtikelzulage(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneArtikelzulage = new TabbedPaneArtikelzulage(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ARTIKELZULAGE,
					tabbedPaneArtikelzulage);
			initComponents();
		}
	}

	private void createTabbedPaneKollektiv(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneKollektiv = new TabbedPaneKollektiv(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_KOLLEKTIV,
					tabbedPaneKollektiv);
			initComponents();
		}
	}

	private void createTabbedPaneBetriebskalender(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneBetriebskalender = new TabbedPaneBetriebskalender(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_BETRIEBSKALENDER,
					tabbedPaneBetriebskalender);
			initComponents();
		}
	}

	private void createTabbedPaneZeitmodell(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneZeitmodell = new TabbedPaneZeitmodell(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ZEITMODELL,
					tabbedPaneZeitmodell);
			if (tabbedPaneZeitmodell.getPanelQueryZeitmodell().getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPaneZeitmodell, 0, false);
			}
			initComponents();
		}

	}

	private void createTabbedPaneBereitschaft(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneBereitschaft = new TabbedPaneBereitschaft(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_BEREITSCHAFT,
					tabbedPaneBereitschaft);
			if (tabbedPaneBereitschaft.getPanelQueryBereitschaft()
					.getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPaneBereitschaft, 0, false);
			}
			initComponents();
		}

	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_TABBED_PANE_PERSONAL) {
			createTabbedPanePersonal(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPanePersonal.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_ZEITMODELL) {
			createTabbedPaneZeitmodell(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneZeitmodell.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_BEREITSCHAFT) {
			createTabbedPaneBereitschaft(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneBereitschaft.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_BETRIEBSKALENDER) {
			createTabbedPaneBetriebskalender(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneBetriebskalender.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			createTabbedPaneGrunddaten(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneGrunddaten.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_ARTIKELZULAGE) {
			createTabbedPaneArtikelzulage(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneArtikelzulage.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_KOLLEKTIV) {
			createTabbedPaneKollektiv(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneKollektiv.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_PERSONALGRUPPEN) {
			createTabbedPanePersonalgruppe(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPanePersonalgruppe.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_FAHRZEUG) {
			createTabbedPaneFahrzeug(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneFahrzeug.lPEventObjectChanged(null);
		}

	}

}
