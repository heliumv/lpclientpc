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
package com.lp.client.rechnung;

import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * Diese Klasse kuemmert sich um Panels des Rechnungsmoduls; um Rechnungen des
 * Typs Gutschrift
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 20.11.2004
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.9 $
 */
public class TabbedPaneGutschrift extends TabbedPaneRechnungAll {

	private static final long serialVersionUID = 1L;
	private final static String MENUE_ACTION_ALLE_GUTSCHRIFTEN = "MENUE_ACTION_ALLE_GUTSCHRIFTEN";
	private final static String MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME = "MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME";
	public int iDX_UMSATZ;
	private int iIDX_KONDITIONEN = -1;
	private PanelDialogKriterienGutschriftUmsatz pdKriterienGutschriftUmsatz = null;
	private PanelTabelleGutschriftUmsatz ptGutschriftUmsatz = null;
	private PanelRechnungKonditionen panelDetailKonditionen = null;

	public TabbedPaneGutschrift(InternalFrame internalFrame) throws Throwable {
		super(internalFrame, RechnungFac.RECHNUNGTYP_GUTSCHRIFT, LPMain
				.getTextRespectUISPr("rechnung.tab.unten.gutschrift.title"));
		jbInitTP();
		initComponents();
	}

	private void jbInitTP() throws Throwable {

		// 3 Konditionen
		iIDX_KONDITIONEN = this.getTabCount();
		insertTab(
				LPMain.getTextRespectUISPr("rechnung.tab.oben.konditionen.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("rechnung.tab.oben.konditionen.tooltip"),
				iIDX_KONDITIONEN);

		// 4 Kontierung
		iDX_KONTIERUNG = this.getTabCount();
		insertTab(
				LPMain.getTextRespectUISPr("rechnung.tab.oben.kontierung.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("rechnung.tab.oben.kontierung.tooltip"),
				iDX_KONTIERUNG);
		// 5 Umsatzuebersicht
		iDX_UMSATZ = this.getTabCount();
		insertTab(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), null,
				null, LPMain.getTextRespectUISPr("lp.umsatzuebersicht"),
				iDX_UMSATZ);
		iIDX_ZAHLUNGEN = this.getTabCount();
		insertTab(
				LPMain.getTextRespectUISPr("rechnung.tab.oben.zahlungen.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("rechnung.tab.oben.zahlungen.tooltip"),
				iIDX_ZAHLUNGEN);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		super.lPEventItemChanged(e);
		if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getPanelDialogGutschriftUmsatz(false)) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getPanelDialogGutschriftUmsatz(
						true).getAlleFilterKriterien();
				// die Kriterien dem PanelTabelle setzen
				setComponentAt(iDX_UMSATZ,
						getPanelTabelleGutschriftUmsatz(true));
				getPanelTabelleGutschriftUmsatz(true).setDefaultFilter(
						aAlleKriterien);
				// die Uebersicht aktualisieren
				getPanelTabelleGutschriftUmsatz(true)
						.eventYouAreSelected(false);
				// man steht auf alle Faelle auf der Uebersicht
				setSelectedComponent(getPanelTabelleGutschriftUmsatz(true));
				getPanelTabelleGutschriftUmsatz(true).updateButtons(
						new LockStateValue(null, null,
								PanelBasis.LOCK_IS_NOT_LOCKED));
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int index = this.getSelectedIndex();
		if (index == iDX_UMSATZ) {
			getPanelTabelleGutschriftUmsatz(true);
			getInternalFrame().showPanelDialog(
					getPanelDialogGutschriftUmsatz(true));
		}
		if (index == iIDX_ZAHLUNGEN) {
			getPanelSplitZahlungen(true).eventYouAreSelected(false);
			// wenn ich aus dem umsatz komme, verlier ich den titel
			this.setRechnungDto(this.getRechnungDto());
		}
		if (index == iIDX_KONDITIONEN) {
			getPanelDetailKonditionen(true).setKeyWhenDetailPanel(
					getRechnungDto().getIId());
			getPanelDetailKonditionen(true).eventYouAreSelected(false);
			// wenn ich aus dem umsatz komme, verlier ich den titel
			this.setRechnungDto(this.getRechnungDto());
		}

	}

	protected void print() throws Throwable {
		if (getRechnungDto() != null) {
			if (DelegateFactory.getInstance().getRechnungDelegate()
					.hatRechnungPositionen(getRechnungDto().getIId())
					.booleanValue()) {
				reloadRechnungDto();
				getInternalFrame().showReportKriterien(
						new ReportGutschrift(getInternalFrame(),
								getAktuellesPanel(), getRechnungDto(),
								getKundeDto(), super.getTitle()),
						super.getKundeDto().getPartnerDto(),
						super.getRechnungDto().getAnsprechpartnerIId(), false);
			} else {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hint"),
						"Die Gutschrift hat noch keine Positionen");
			}
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);
		JMenu modul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);
		modul.add(new JSeparator(), 0);

		WrapperMenuItem menuItemDateiDrucken = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.drucken"), null);
		menuItemDateiDrucken.addActionListener(this);
		menuItemDateiDrucken.setActionCommand(MENUE_ACTION_DATEI_DRUCKEN);
		modul.add(menuItemDateiDrucken, 0);

		// Menue Bearbeiten
		JMenu jmBearbeiten = (JMenu) wmb
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		WrapperMenuItem menueItemBeleguebernahme = null;
		menueItemBeleguebernahme = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("finanz.beleguebernahme"),
				RechteFac.RECHT_FB_CHEFBUCHHALTER);
		menueItemBeleguebernahme.addActionListener(this);
		menueItemBeleguebernahme
				.setActionCommand(MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME);
		jmBearbeiten.add(menueItemBeleguebernahme, 0);
		if (!LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

			WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"),
					RechteFac.RECHT_RECH_RECHNUNG_CUD);
			menuItemBearbeitenManuellErledigen.addActionListener(this);
			menuItemBearbeitenManuellErledigen
					.setActionCommand(MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);
		}

		JMenu journal = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_JOURNAL);
		WrapperMenuItem menuItemAlleGS = new WrapperMenuItem(
				"Alle Gutschriften", null);
		menuItemAlleGS.addActionListener(this);
		menuItemAlleGS.setActionCommand(MENUE_ACTION_ALLE_GUTSCHRIFTEN);
		journal.add(menuItemAlleGS);

		JMenu extras = new JMenu(
				LPMain.getTextRespectUISPr("rechnung.menu.extras"));
		WrapperMenuItem menuItemNeuDatum = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rechnung.menu.extras.neudatum"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menuItemNeuDatum.addActionListener(this);
		menuItemNeuDatum.setActionCommand(MENUE_ACTION_NEU_DATUM);
		extras.add(menuItemNeuDatum);

		return wmb;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		super.lPActionEvent(e);
		if (e.getActionCommand().equals(MENUE_ACTION_ALLE_GUTSCHRIFTEN)) {
			getInternalFrame().showReportKriterien(
					new ReportRechnungAlleGutschriften(getInternalFrame(),
							"Alle Gutschriften"));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME)) {

			DelegateFactory.getInstance().getRechnungDelegate()
					.verbucheGutschriftNeu(this.getRechnungDto().getIId());

		}
	}

	private PanelDialogKriterienGutschriftUmsatz getPanelDialogGutschriftUmsatz(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (pdKriterienGutschriftUmsatz == null && bNeedInstantiationIfNull) {
			pdKriterienGutschriftUmsatz = new PanelDialogKriterienGutschriftUmsatz(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht"),
					this);
		}
		return pdKriterienGutschriftUmsatz;
	}

	private PanelTabelle getPanelTabelleGutschriftUmsatz(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (ptGutschriftUmsatz == null && bNeedInstantiationIfNull) {
			ptGutschriftUmsatz = new PanelTabelleGutschriftUmsatz(
					QueryParameters.UC_ID_GUTSCHRIFT_UMSATZ,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"),
					getInternalFrame());
		}
		return ptGutschriftUmsatz;
	}

	private PanelRechnungKonditionen getPanelDetailKonditionen(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailKonditionen == null && bNeedInstantiationIfNull) {
			panelDetailKonditionen = new PanelRechnungKonditionen(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("rechnung.tab.oben.konditionen.title"),
					null, this);
			this.setComponentAt(iIDX_KONDITIONEN, panelDetailKonditionen);
		}
		return panelDetailKonditionen;
	}

	@Override
	protected void printZahlschein() throws Throwable {
	}
	
	protected PanelRechnungZahlung getPanelDetailZahlung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailZahlung == null && bNeedInstantiationIfNull) {
			panelDetailZahlung = new PanelGutschriftZahlung(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("rechnung.title.panel.zahlungen"),
					this);

		}
		return panelDetailZahlung;
	}	
}
