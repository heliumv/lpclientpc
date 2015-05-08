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
package com.lp.client.angebot;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.artikel.DialogNeueArtikelnummer;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ArtikelsetViewController;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebottextDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.StatusDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>TabbedPane fuer Modul Angebot.</p> <p>Copyright Logistik Pur Software GmbH
 * (c) 2004-2008</p> <p>Erstellungsdatum 04.07.05</p> <p> </p>
 *
 * @author Uli Walch
 *
 * @version $Revision: 1.30 $
 */
public class TabbedPaneAngebot extends TabbedPane implements ICopyPaste {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery angebotAuswahl = null;
	private PanelAngebotKopfdaten angebotKopfdaten = null;

	private PanelQuery angebotPositionenTop = null;
	private PanelAngebotPositionen angebotPositionenBottom = null;
	private PanelSplit angebotPositionen = null; // FLR 1:n Liste

	private PanelBasis angebotKonditionen = null;

	private PanelDialogKriterienAngebotUebersicht pdKriterienAngebotUebersicht = null;
	private boolean bKriterienAngebotUebersichtUeberMenueAufgerufen = false;
	private PanelTabelleAngebotUebersicht ptAngebotUebersicht = null;

	private PanelDialogKriterienAngebotzeiten panelDialogKriterienAngebotzeiten = null;
	private boolean pdKriterienAngebotzeitenUeberMenueAufgerufen = false;
	private PanelTabelleAngebotzeiten panelTabelleAngebotzeiten = null;

	public static final int IDX_PANEL_AUSWAHL = 0;
	private final int IDX_PANEL_KOPFDATEN = 1;
	private final int IDX_PANEL_POSITIONEN = 2;
	public static final int IDX_PANEL_KONDITIONEN = 3;
	private final int IDX_PANEL_ANGEBOTUEBERSICHT = 4;
	private final int IDX_PANEL_ZEITDATEN = 5;

	// dtos, die in mehr als einem panel benoetigt werden
	private AngebotDto angebotDto = null;
	private KundeDto kundeDto = null;
	private AngebottextDto kopftextDto = null; // Kopftext unabhaengig vom
	// PanelKontionen hinterlegen
	private AngebottextDto fusstextDto = null; // Fusstext unabhaengig vom
	// PanelKontionen hinterlegen

	private final String MENU_DATEI_DRUCKEN = "MENU_ACTION_DRUCKEN";
	private final String MENU_DATEI_DRUCKEN_VORKALKULATION = "MENU_ACTION_DRUCKEN_VORKALKULATION";
	private final String MENU_DATEI_LIEFERSCHEINETIKETT = "MENU_DATEI_LIEFERSCHEINETIKETT";
	private final String MENU_BEARBEITEN_AKQUISEDATEN = "MENU_BEARBEITEN_AKQUISEDATEN";
	private final String MENU_BEARBEITEN_HANDARTIKEL_UMANDELN = "MENU_BEARBEITEN_HANDARTIKEL_UMANDELN";
	private final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN";
	// kommentar: 2 Je ein Menueeintrag fuer internen und externen Kommentar
	private final String MENU_BEARBEITEN_INTERNER_KOMMENTAR = "MENU_BEARBEITEN_INTERNER_KOMMENTAR";
	private final String MENU_BEARBEITEN_EXTERNER_KOMMENTAR = "MENU_BEARBEITEN_EXTERNER_KOMMENTAR";

	private final String MENU_JOURNAL_ANGEBOT_ALLE = "MENU_JOURNAL_ANGEBOT_ALLE";
	private final String MENU_JOURNAL_ANGEBOT_OFFENE = "MENU_JOURNAL_ANGEBOT_OFFENE";
	private final String MENU_JOURNAL_ANGEBOT_ABGELEHNTE = "MENU_JOURNAL_ANGEBOT_ABGELEHNTE";
	private final String MENU_ACTION_JOURNAL_UEBERSICHT = "MENU_JOURNAL_ANGEBOT_UEBERSICHT";
	private final String MENU_ACTION_JOURNAL_POTENTIAL = "MENU_ACTION_JOURNAL_POTENTIAL";

	private final String MENU_ANSICHT_ANGEBOT_ALLE = "MENU_ANSICHT_ANGEBOT_ALLE";
	private final String MENU_ANSICHT_ANGEBOT_OFFENE = "MENU_ANSICHT_ANGEBOT_OFFENE";
	private final String MENU_ANSICHT_ANGEBOT_ANGELEGTE = "MENU_ANSICHT_ANGEBOT_ANGELEGTE";
	private final String MENU_ANSICHT_ANGEBOT_OFFENE_MEINE = "MENU_ANSICHT_ANGEBOT_OFFENE_MEINE";
	private final String MENU_ANSICHT_ANGEBOT_ANGELEGTE_MEINE = "MENU_ANSICHT_ANGEBOT_ANGELEGTE_MEINE";

	private static final String ACTION_SPECIAL_PREISE_NEU_KALKULIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_PREISE_NEU_KALKULIEREN";

	private static final String ACTION_SPECIAL_NEU_AUS_PROJEKT = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_NEU_AUS_PROJEKT";

	private PanelDialogAngebotAkquisedaten pdAngebotAkquisedaten = null;

	// kommentar: 1 Je ein Panel fuer internen und externen Kommentar
	private PanelDialogAngebotKommentar pdAngebotInternerKommentar = null;
	private PanelDialogAngebotKommentar pdAngebotExternerKommentar = null;

	// Action fuer den Button "Neu aus bestehendem Angebot"
	public final static String EXTRA_NEU_AUS_ANGEBOT = "aus_angebot";

	private PanelQueryFLR panelQueryFLRAngebot = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRProjekt = null;
	private PanelQueryFLR panelQueryFLRAngeboterledigungsgrund = null;

	private JMenu menuAnsicht = null;
	private JCheckBoxMenuItem menuItemAlleAngelegten = null;
	private JCheckBoxMenuItem menuItemAlle = null;
	private JCheckBoxMenuItem menuItemAlleOffenen = null;
	private JCheckBoxMenuItem menuItemMeineOffenen = null;
	private JCheckBoxMenuItem menuItemMeineAngelegten = null;

	public TabbedPaneAngebot(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"angb.angebot"));

		jbInit();
		initComponents();
	}

	public PanelQuery getAngebotAuswahl() {
		return angebotAuswahl;
	}

	public PanelQuery getAngebotPositionenTop() {
		return angebotPositionenTop;
	}

	public PanelAngebotPositionen getAngebotPositionenBottom() {
		return angebotPositionenBottom;
	}

	public AngebotDto getAngebotDto() {
		return angebotDto;
	}

	public void setAngebotDto(AngebotDto angebotDtoI) {
		angebotDto = angebotDtoI;
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDtoI) {
		kundeDto = kundeDtoI;
	}

	public AngebottextDto getKopftextDto() {
		return kopftextDto;
	}

	public void setKopftextDto(AngebottextDto kopftextDtoI) {
		kopftextDto = kopftextDtoI;
	}

	public AngebottextDto getFusstextDto() {
		return fusstextDto;
	}

	public void setFuesstextDto(AngebottextDto fusstextDtoI) {
		fusstextDto = fusstextDtoI;
	}

	private void jbInit() throws Throwable {
		// dafuer sorgen, dass die Dtos != null sind
		resetDtos();

		// die Liste der Angebote aufbauen und sofort laden
		createAngebotAuswahl();

		// das aktuell gewaehlten Angebot hinterlegen
		setKeyWasForLockMe();

		// das aktuell gewaehlte Angebot im Titel anzeigen
		setTitleAngebot(LPMain.getInstance().getTextRespectUISPr(
				"angb.panel.auswahl"));

		insertTab(
				LPMain.getInstance().getTextRespectUISPr("angb.panel.auswahl"),
				null,
				angebotAuswahl,
				LPMain.getInstance().getTextRespectUISPr(
						"angb.panel.auswahl.tooltip"), IDX_PANEL_AUSWAHL);

		// die restlichen Panels bei Bedarf laden
		insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("angb.panel.kopfdaten"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"angb.panel.kopfdaten.tooltip"), IDX_PANEL_KOPFDATEN);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"angb.panel.positionen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"angb.panel.positionen.tooltip"), IDX_PANEL_POSITIONEN);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"angb.panel.konditionen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"angb.panel.konditionen.tooltip"),
				IDX_PANEL_KONDITIONEN);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.umsatzuebersicht"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr("lp.umsatzuebersicht"),
				IDX_PANEL_ANGEBOTUEBERSICHT);
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG)) {
			insertTab(
					LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.tooltip"),
					IDX_PANEL_ZEITDATEN);
		}
		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	private void createAngebotAuswahl() throws Throwable {
		QueryType[] qtAuswahl = AngebotFilterFactory.getInstance()
				.createQTPanelAngebotauswahl();
		FilterKriterium[] filterAuswahl = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
				PanelBasis.ACTION_FILTER };

		angebotAuswahl = new PanelQuery(qtAuswahl, filterAuswahl,
				QueryParameters.UC_ID_ANGEBOT, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"angb.panel.auswahl"), true); // liste refresh wenn
		// lasche geklickt wurde

		// dem PanelQuery die direkten FilterKriterien setzen
		FilterKriteriumDirekt fkDirekt1 = AngebotFilterFactory.getInstance()
				.createFKDAngebotnummer();

		FilterKriteriumDirekt fkDirekt2 = AngebotFilterFactory.getInstance()
				.createFKDKundeName();

		angebotAuswahl.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
		angebotAuswahl.addDirektFilter(AngebotFilterFactory.getInstance()
				.createFKDProjekt());
		angebotAuswahl.addDirektFilter(AngebotFilterFactory.getInstance()
				.createFKDTextSuchen());
		angebotAuswahl
				.befuelleFilterkriteriumSchnellansicht(AngebotFilterFactory
						.getInstance().createFKSchnellansicht());
		// der zusaetzliche Button fuer neu aus bestehendem Auftrag
		angebotAuswahl
				.createAndSaveAndShowButton(
						"/com/lp/client/res/presentation_chart16x16.png",
						LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemangebot"),
						PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_ANGEBOT,
						RechteFac.RECHT_ANGB_ANGEBOT_CUD);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			angebotAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/briefcase2_document16x16.png",
					LPMain.getTextRespectUISPr("angb.neuausprojekt"),
					ACTION_SPECIAL_NEU_AUS_PROJEKT,
					RechteFac.RECHT_ANGB_ANGEBOT_CUD);
		}

		// liste soll sofort angezeigt werden
		angebotAuswahl.eventYouAreSelected(false);
	}

	public void erstelleAngebotAusProjekt(Integer projektIId) throws Throwable {

		ItemChangedEvent e = new ItemChangedEvent(this, -99);
		getAngebotAuswahl().eventActionNew(e, true, false);
		getAngebotKopfdaten().eventYouAreSelected(false);

		// Nun noch Kunde/Ansprechpartner/Vertreter/Projekt/ProjektBezeichnung
		// setzen
		getAngebotKopfdaten().setDefaultsAusProjekt(projektIId);
	}

	private PanelAngebotKopfdaten getAngebotKopfdaten() throws Throwable {
		Integer iIdAngebot = null;

		if (angebotKopfdaten == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAngebot = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			angebotKopfdaten = new PanelAngebotKopfdaten(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"angb.panel.kopfdaten"), iIdAngebot); // empty bei
			// leerer
			// angebotsliste

			setComponentAt(IDX_PANEL_KOPFDATEN, angebotKopfdaten);
		}

		return angebotKopfdaten;
	}

	private PanelSplit refreshAngebotPositionen() throws Throwable {
		if (angebotPositionen == null) {
			angebotPositionenBottom = new PanelAngebotPositionen(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.positionen"), null); // eventuell
			// gibt
			// es
			// noch
			// keine
			// position

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotFilterFactory
					.getInstance().createFKFlrangebotiid(angebotDto.getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			angebotPositionenTop = new PanelQuery(qtPositionen,
					filtersPositionen, QueryParameters.UC_ID_ANGEBOTPOSITION,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.positionen"), true); // flag
			// ,
			// damit
			// flr
			// erst
			// beim
			// aufruf
			// des
			// panels
			// loslaeuft
			angebotPositionenTop.createAndSaveAndShowButton(
					"/com/lp/client/res/calculator16x16.png",
					LPMain.getTextRespectUISPr("ang.preise.neuberechnen"),
					ACTION_SPECIAL_PREISE_NEU_KALKULIEREN,
					RechteFac.RECHT_ANGB_ANGEBOT_CUD);
			angebotPositionen = new PanelSplit(getInternalFrame(),
					angebotPositionenBottom, angebotPositionenTop, 150);

			// mehrfachselekt: fuer dieses QP aktivieren
			angebotPositionenTop.setMultipleRowSelectionEnabled(true);

			setComponentAt(IDX_PANEL_POSITIONEN, angebotPositionen);
		}

		return angebotPositionen;
	}

	private PanelBasis getAngebotKonditionen() throws Throwable {
		Integer iIdAngebot = null;

		if (angebotKonditionen == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAngebot = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			angebotKonditionen = new PanelAngebotKonditionen(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.konditionen"),
					iIdAngebot); // empty bei leerer angebotsliste

			setComponentAt(IDX_PANEL_KONDITIONEN, angebotKonditionen);
		}

		return angebotKonditionen;
	}

	public JCheckBoxMenuItem getMenuItemAlleAngelegten() {
		return menuItemAlleAngelegten;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		Integer iIdAngebot = angebotDto.getIId();
		initializeDtos(iIdAngebot);
		int selectedIndex = this.getSelectedIndex();
		switch (selectedIndex) {
		case IDX_PANEL_AUSWAHL:
			getAngebotAuswahl().eventYouAreSelected(false);
			angebotAuswahl.updateButtons();

			// wenn es fuer das Default TabbedPane noch keinen eintrag gibt, die
			// restlichen Panels deaktivieren, die Grunddaten bleiben erreichbar
			if (getAngebotAuswahl().getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			break;

		case IDX_PANEL_KOPFDATEN:
			getAngebotKopfdaten().eventYouAreSelected(false); // sonst werden
			// die buttons
			// nicht richtig
			// gesetzt!
			break;

		case IDX_PANEL_POSITIONEN:
			refreshAngebotPositionen();

			if (iIdAngebot != null) {
				// filter aktualisieren
				FilterKriterium[] filtersPositionen = AngebotFilterFactory
						.getInstance().createFKFlrangebotiid(iIdAngebot);
				angebotPositionenTop.setDefaultFilter(filtersPositionen);
			}
			angebotPositionen.eventYouAreSelected(false);

			angebotPositionenTop.updateButtons(angebotPositionenBottom
					.getLockedstateDetailMainKey());
			break;

		case IDX_PANEL_KONDITIONEN:
			getAngebotKonditionen().eventYouAreSelected(false);
			break;

		case IDX_PANEL_ANGEBOTUEBERSICHT:
			if (!bKriterienAngebotUebersichtUeberMenueAufgerufen) {
				getKriterienAngebotUebersicht();
				getInternalFrame()
						.showPanelDialog(pdKriterienAngebotUebersicht);
			}
			break;
		case IDX_PANEL_ZEITDATEN:
			if (!pdKriterienAngebotzeitenUeberMenueAufgerufen) {
				getInternalFrame().showPanelDialog(
						getKriterienAngebotzeiten(true));
			}
			break;

		}
		setTitleAngebot("");
		setEnabledAt(IDX_PANEL_ANGEBOTUEBERSICHT, bDarfPreiseSehen);
	}

	private PanelDialogKriterienAngebotzeiten getKriterienAngebotzeiten(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDialogKriterienAngebotzeiten == null
				&& bNeedInstantiationIfNull) {
			panelDialogKriterienAngebotzeiten = new PanelDialogKriterienAngebotzeiten(
					getInternalFrame(), this,
					LPMain.getTextRespectUISPr("fert.title.kriterienloszeiten")
							+ " " + getAngebotDto().getCNr());
		}
		return panelDialogKriterienAngebotzeiten;
	}

	private void getKriterienAngebotUebersicht() throws Throwable {
		pdKriterienAngebotUebersicht = new PanelDialogKriterienAngebotUebersicht(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.kriterienumsatzuebersicht"));
	}

	private AngebotpositionDto getSelectedAngebotpositionDto()
			throws Throwable, ExceptionLP {
		if (angebotPositionenTop.getSelectedId() == null)
			return null;

		return DelegateFactory
				.getInstance()
				.getAngebotpositionDelegate()
				.angebotpositionFindByPrimaryKey(
						(Integer) angebotPositionenTop.getSelectedId());
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			handleItemChanged(e);
		}

		else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == angebotPositionenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				angebotPositionenTop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			handleGotoDetailPanel(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {

			if (e.getSource() == angebotAuswahl) {
				if (e.getID() == ItemChangedEvent.ACTION_NEW) {
					// wenn es bisher keinen eintrag gibt, die restlichen
					// panels aktivieren
					if (angebotAuswahl.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, true);
					}

					getAngebotKopfdaten().eventActionNew(e, true, false);
					setSelectedComponent(angebotKopfdaten);
				}
			} else if (e.getSource() == angebotPositionenTop) {
				if (istAktualisierenAngebotErlaubt()) {
					angebotPositionenBottom.eventActionNew(e, true, false);
					angebotPositionenBottom.eventYouAreSelected(false);
					setSelectedComponent(angebotPositionen); // ui
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				// copypaste
				einfuegenHV();
			} else if (sAspectInfo
					.endsWith(ACTION_SPECIAL_PREISE_NEU_KALKULIEREN)) {

				if (getAngebotDto().getStatusCNr().equals(
						AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {

					//
					PanelPositionenArtikelVerkauf panelBottom = ((PanelPositionenArtikelVerkauf) angebotPositionenBottom.panelArtikel);

					AngebotpositionDto[] dtos = DelegateFactory
							.getInstance()
							.getAngebotpositionDelegate()
							.angebotpositionFindByAngebotIId(
									getAngebotDto().getIId());

					for (int i = 0; i < dtos.length; i++) {
						if (dtos[i].getPositionsartCNr().equals(
								AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
							angebotPositionenTop
									.setSelectedId(dtos[i].getIId());
							angebotPositionenTop.eventYouAreSelected(false);
							panelBottom.setKeyWhenDetailPanel(dtos[i].getIId());
							panelBottom.update();
							panelBottom.berechneVerkaufspreis(false);
							angebotPositionenBottom
									.eventActionSave(null, false);
						}
					}
				} else {
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"ang.error.neuberechnen"));
				}

			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_NEU_AUS_PROJEKT)) {
				dialogQueryProjektFromListe(null);
			} else {
				dialogQueryAngebotFromListe(null);
			}

		}
		// der OK Button in einem PanelDialog wurde gedrueckt
		else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getKriterienAngebotzeiten(false)) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getKriterienAngebotzeiten(
						true).getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				getPanelTabelleAngebotzeiten().setDefaultFilter(aAlleKriterien);

				// die Auftragzeiten aktualisieren
				getPanelTabelleAngebotzeiten().eventYouAreSelected(false);

				// man steht auf alle Faelle in den Auftragzeiten
				setSelectedComponent(getPanelTabelleAngebotzeiten());
				pdKriterienAngebotzeitenUeberMenueAufgerufen = false;
				getPanelTabelleAngebotzeiten().updateButtons(
						new LockStateValue(null, null,
								PanelBasis.LOCK_IS_NOT_LOCKED));
			}
		}
		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == angebotPositionenBottom) {
				angebotPositionen.eventYouAreSelected(false); // das 1:n Panel
				// neu aufbauen
			}
		} else

		// Wir landen hier nach Button Save
		if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			handleActionSave(e);
		} else

		if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {

		} else

		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == angebotKopfdaten) {
				// die Liste neu laden, falls sich etwas geaendert hat
				angebotAuswahl.eventYouAreSelected(false);

				// nach einem Discard ist der aktuelle Key nicht mehr gesetzt
				setKeyWasForLockMe();

				// der Key der Kopfdaten steht noch auf new|...
				angebotKopfdaten.setKeyWhenDetailPanel(angebotAuswahl
						.getSelectedId());

				// auf die Auswahl schalten
				setSelectedComponent(angebotAuswahl);

				// wenn es fuer das TabbedPane noch keinen eintrag gibt, die
				// restlichen panel deaktivieren
				if (angebotAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}
			} else if (e.getSource() == angebotPositionenBottom) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();
				if (angebotPositionenBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = angebotPositionenTop
							.getId2SelectAfterDelete();
					angebotPositionenTop.setSelectedId(oNaechster);
				}
				angebotPositionen.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n
				// panel
			}
		} else

		// der OK Button in einem PanelDialog wurde gedrueckt
		if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == pdKriterienAngebotUebersicht) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = pdKriterienAngebotUebersicht
						.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				refreshAngebotUebersicht();
				ptAngebotUebersicht.setDefaultFilter(aAlleKriterien);

				// die Uebersicht aktualisieren
				ptAngebotUebersicht.eventYouAreSelected(false);

				// man steht auf alle Faelle auf der Uebersicht
				setSelectedComponent(ptAngebotUebersicht);
				setTitleAngebotOhneAngebotnummer(LPMain.getInstance()
						.getTextRespectUISPr("lp.umsatzuebersicht"));
				bKriterienAngebotUebersichtUeberMenueAufgerufen = false;
				ptAngebotUebersicht.updateButtons(new LockStateValue(null,
						null, PanelBasis.LOCK_IS_NOT_LOCKED));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == angebotPositionenTop) {
				if (getAngebotDto().getStatusCNr().equals(
						AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
					int iPos = angebotPositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die erste ist
					if (iPos > 0) {
						Integer iIdPosition = (Integer) angebotPositionenTop
								.getSelectedId();

						TableModel tm = angebotPositionenTop.getTable()
								.getModel();

						// AngebotpositionDto posDto =
						// getSelectedAngebotpositionDto();
						// if
						// (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME
						// .equals(posDto.getPositionsartCNr())) {
						// Integer myPosNumber = DelegateFactory.getInstance()
						// .getAngebotpositionDelegate()
						// .getPositionNummer(posDto.getIId());
						// Integer previousIId = DelegateFactory
						// .getInstance()
						// .getAngebotpositionDelegate()
						// .getPositionIIdFromPositionNummer(
						// getAngebotDto().getIId(),
						// myPosNumber - 1);
						// if (previousIId.equals(posDto.getZwsBisPosition())) {
						// return;
						// }
						// }

						DelegateFactory.getInstance()
								.getAngebotpositionDelegate()
								.vertauscheAngebotpositionMinus(iPos, tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						angebotPositionenTop.setSelectedId(iIdPosition);
						updateISortButtons();
					}
				} else {
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"anf.error.positionenverschieben"));
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == angebotPositionenTop) {
				if (getAngebotDto().getStatusCNr().equals(
						AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
					int iPos = angebotPositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die letzte ist
					if (iPos < angebotPositionenTop.getTable().getRowCount() - 1) {
						Integer iIdPosition = (Integer) angebotPositionenTop
								.getSelectedId();

						// Integer iIdPositionPlus1 = (Integer)
						// angebotPositionenTop
						// .getTable().getValueAt(iPos + 1, 0);
						//
						// DelegateFactory
						// .getInstance()
						// .getAngebotpositionDelegate()
						// .vertauscheAngebotpositionen(iIdPosition,
						// iIdPositionPlus1);

						TableModel tm = angebotPositionenTop.getTable()
								.getModel();
						// AngebotpositionDto posDto =
						// getSelectedAngebotpositionDto();
						// Integer myPosNumber = DelegateFactory.getInstance()
						// .getAngebotpositionDelegate()
						// .getPositionNummer(posDto.getIId());
						// if (myPosNumber != null) {
						// Integer nextIId = DelegateFactory
						// .getInstance()
						// .getAngebotpositionDelegate()
						// .getPositionIIdFromPositionNummer(
						// getAngebotDto().getIId(),
						// myPosNumber + 1);
						// if (nextIId != null) {
						// AngebotpositionDto nextDto = DelegateFactory
						// .getInstance()
						// .getAngebotpositionDelegate()
						// .angebotpositionFindByPrimaryKey(
						// nextIId);
						// if
						// (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME
						// .equals(nextDto.getPositionsartCNr())) {
						// if (posDto.getIId().equals(
						// nextDto.getZwsBisPosition())) {
						// return;
						// }
						// }
						// }
						// }
						DelegateFactory.getInstance()
								.getAngebotpositionDelegate()
								.vertauscheAngebotpositionPlus(iPos, tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						angebotPositionenTop.setSelectedId(iIdPosition);
						updateISortButtons();
					}
				} else {
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"anf.error.positionenverschieben"));
				}

			}
		} else

		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == angebotPositionenTop) {

				angebotPositionenBottom
						.setArtikeSetIIdForNewPosition(angebotPositionenBottom
								.getAngebotpositionDto()
								.getPositioniIdArtikelset());
				angebotPositionenBottom.eventActionNew(e, true, false);
				angebotPositionenBottom.eventYouAreSelected(false); // Buttons
				// schalten
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}

	}

	private void refreshAngebotUebersicht() throws Throwable {
		if (ptAngebotUebersicht == null) {
			ptAngebotUebersicht = new PanelTabelleAngebotUebersicht(
					QueryParameters.UC_ID_ANGEBOTUEBERSICHT, LPMain
							.getInstance().getTextRespectUISPr(
									"lp.umsatzuebersicht"), getInternalFrame());
			setComponentAt(IDX_PANEL_ANGEBOTUEBERSICHT, ptAngebotUebersicht);
		}
	}

	public void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAngebot = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebot(getInternalFrame(), true, false, null);

		new DialogQuery(panelQueryFLRAngebot);
	}

	public void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame());

		new DialogQuery(panelQueryFLRProjekt);
	}

	private PanelTabelleAngebotzeiten getPanelTabelleAngebotzeiten()
			throws Throwable {
		if (panelTabelleAngebotzeiten == null) {
			panelTabelleAngebotzeiten = new PanelTabelleAngebotzeiten(
					QueryParameters.UC_ID_ANGEBOTZEITEN,
					LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.title")
							+ " " + getAngebotDto().getCNr(),
					(InternalFrameAngebot) getInternalFrame());

			// default Kriterium vorbelegen
			FilterKriterium[] aFilterKrit = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(
					AngebotFac.KRIT_PERSONAL, true, "true",
					FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium(
					AngebotFac.KRIT_ANGEBOT_I_ID, true, angebotDto.getIId()
							.toString(), FilterKriterium.OPERATOR_EQUAL, false);
			aFilterKrit[0] = krit1;
			aFilterKrit[1] = krit2;

			panelTabelleAngebotzeiten.setDefaultFilter(aFilterKrit);

			setComponentAt(IDX_PANEL_ZEITDATEN, panelTabelleAngebotzeiten);
		}
		return panelTabelleAngebotzeiten;
	}

	public void dialogQueryAngeboterledigungsgrundFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRAngeboterledigungsgrund = AngebotFilterFactory
				.getInstance().createPanelFLRAngeboterledigungsgrund(
						getInternalFrame(), false, false);
		new DialogQuery(panelQueryFLRAngeboterledigungsgrund);
	}

	public void dialogQueryAngeboterledigendurchAuftragFromListe(ActionEvent e)
			throws Throwable {
		String[] aWhichButtonIUse = null;
		QueryType[] querytypes = null;
		FilterKriterium[] fk = AuftragFilterFactory.getInstance()
				.createFKOhneStornierten(getAngebotDto().getProjektIId());
		panelQueryFLRAuftrag = new PanelQueryFLR(querytypes, fk,
				QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"auft.auftrag"));
		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirekt(
				AuftragFilterFactory.getInstance().createFKDAuftragnummer(),
				AuftragFilterFactory.getInstance().createFKDKundenname());
		new DialogQuery(panelQueryFLRAuftrag);
	}

	private void handleActionSave(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == angebotKopfdaten) {
			Object pkAngebot = angebotKopfdaten.getKeyWhenDetailPanel();
			initializeDtos((Integer) pkAngebot);
			getInternalFrame().setKeyWasForLockMe(pkAngebot.toString());

			angebotAuswahl.clearDirektFilter();
			angebotAuswahl.eventYouAreSelected(false);
			angebotAuswahl.setSelectedId(pkAngebot);
			angebotAuswahl.eventYouAreSelected(false);
		} else if (e.getSource() == angebotPositionenBottom) {
			// den Key des Datensatzes merken
			Object oKey = angebotPositionenBottom.getKeyWhenDetailPanel();

			// wenn der neue Datensatz wirklich angelegt wurde (Abbruch moeglich
			// durch Pruefung auf Unterpreisigkeit)
			if (!oKey.equals(LPMain.getLockMeForNew())) {
				// die Liste neu aufbauen
				angebotPositionenTop.eventYouAreSelected(false);

				// den Datensatz in der Liste selektieren
				angebotPositionenTop.setSelectedId(oKey);
			}

			// im Detail den selektierten anzeigen
			angebotPositionen.eventYouAreSelected(false);
		}
	}

	/**
	 * Verarbeitung von ItemChangedEvent.GOTO_DETAIL_PANEL.
	 *
	 * @param e
	 *            ItemChangedEvent
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void handleGotoDetailPanel(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == angebotAuswahl) {
			Integer angebotIId = (Integer) angebotAuswahl.getSelectedId();
			initializeDtos(angebotIId);
			getInternalFrame().setKeyWasForLockMe(angebotIId + "");

			// IMS 1750 nur auf die Positionen wechseln, wenn es ein aktuelles
			// Angebot gibt
			if (angebotIId != null) {
				setSelectedComponent(refreshAngebotPositionen()); // auf die
				// Positionen
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRAngebot) {
			Integer iIdAngebotBasis = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();

			if (iIdAngebotBasis != null) {
				Integer angebotIId = DelegateFactory.getInstance()
						.getAngebotDelegate()
						.erzeugeAngebotAusAngebot(iIdAngebotBasis,getInternalFrame());
				// PJ09/0014616
				angebotKopfdaten.setKeyWhenDetailPanel(angebotIId);
				initializeDtos(angebotIId); // befuellt das Angebot und den
				// Kunden
				getInternalFrame().setKeyWasForLockMe(angebotIId + "");
				angebotAuswahl.setSelectedId(angebotIId);

				// wenn es bisher kein Angebot gegeben hat
				if (angebotAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

				setSelectedComponent(refreshAngebotPositionen());
			}
		} else if (e.getSource() == panelQueryFLRProjekt) {
			Integer projektIId = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();

			if (projektIId != null) {
				erstelleAngebotAusProjekt(projektIId);
			}
		} else if (e.getSource() == panelQueryFLRAngeboterledigungsgrund) {
			String erledigungsgrundCNr = (String) ((ISourceEvent) e.getSource())
					.getIdSelected();

			DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.angebotManuellErledigen(angebotDto.getIId(),
							erledigungsgrundCNr);

			initializeDtos(angebotDto.getIId()); // den Status am UI
			// aktualisieren
			refreshAktuellesPanel();
		}

		else if (e.getSource() == panelQueryFLRAuftrag) {
			Integer iIdAuftrag = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();
			boolean bKonditionenStimmenNichtZusammen = DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.angebotManuellErledigendurchAuftrag(angebotDto.getIId(),
							iIdAuftrag);
			if (bKonditionenStimmenNichtZusammen == true) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.hinweis"),
						LPMain.getInstance().getTextRespectUISPr(
								"angb.auftragerledigen.error"));
			}
			refreshAktuellesPanel();
		}

	}

	private void handleItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == angebotAuswahl) {
			Integer iIdAngebot = (Integer) angebotAuswahl.getSelectedId();
			setKeyWasForLockMe(); // beinhaltet inizializeDtos()
			getAngebotKopfdaten().setKeyWhenDetailPanel(iIdAngebot);

			setTitleAngebot(LPMain.getInstance().getTextRespectUISPr(
					"angb.panel.auswahl"));

			refreshAngebotPositionen();
			angebotPositionenTop.setDefaultFilter(AngebotFilterFactory
					.getInstance().createFKAngebotiid(iIdAngebot));
			angebotPositionen.setKeyWhenDetailPanel(iIdAngebot);

			// IMS 1750 wenn alle Angebote weggefiltert wurden, die anderen
			// Panels deaktivieren
			getInternalFrame().enableAllOberePanelsExceptMe(this,
					IDX_PANEL_AUSWAHL, (iIdAngebot != null));
		} else if (e.getSource() == angebotPositionenTop) {
			Object key = angebotPositionenTop.getIdSelected();
			angebotPositionenBottom.setKeyWhenDetailPanel(key);
			angebotPositionenBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			angebotPositionenTop.updateButtons(angebotPositionenBottom
					.getLockedstateDetailMainKey());
			updateISortButtons();

		}
	}

	private void updateISortButtons() {
		angebotPositionenTop.enableToolsPanelButtons(true,
				PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
		angebotPositionenTop.enableToolsPanelButtons(true,
				PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);

		if (angebotPositionenTop.getTable().getSelectedRow() == 0)
			angebotPositionenTop.enableToolsPanelButtons(false,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
		else if (angebotPositionenTop.getTable().getSelectedRow() == angebotPositionenTop
				.getTable().getRowCount() - 1)
			angebotPositionenTop.enableToolsPanelButtons(false,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
	}

	private void initializeDtos(Integer iIdAngebot) throws Throwable {
		if (iIdAngebot != null) {
			angebotDto = DelegateFactory.getInstance().getAngebotDelegate()
					.angebotFindByPrimaryKey(iIdAngebot);

			kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							angebotDto.getKundeIIdAngebotsadresse());

			// Kopftext und Fusstext hinterlegen
			if (angebotDto.getBelegtextIIdKopftext() == null
					&& angebotDto.getBelegtextIIdFusstext() == null) {
				initAngebottexte();
			}

			if (angebotDto.getBelegtextIIdKopftext() != null) {
				kopftextDto = DelegateFactory
						.getInstance()
						.getAngebotServiceDelegate()
						.angebottextFindByPrimaryKey(
								angebotDto.getBelegtextIIdKopftext());
			}

			if (angebotDto.getBelegtextIIdFusstext() != null) {
				fusstextDto = DelegateFactory
						.getInstance()
						.getAngebotServiceDelegate()
						.angebottextFindByPrimaryKey(
								angebotDto.getBelegtextIIdFusstext());
			}
		}
	}

	/**
	 * Den Text der Titelleiste ueberschreiben.
	 *
	 * @param panelTitle
	 *            der Title des aktuellen panel
	 * @throws Exception
	 */
	public void setTitleAngebot(String panelTitle) throws Throwable {
		// aktuelle angebotnummer bestimmen
		StringBuffer angebotnummer = new StringBuffer("");
		if (angebotDto == null || angebotDto.getIId() == null) {
			angebotnummer.append(LPMain.getInstance().getTextRespectUISPr(
					"angb.neuesangebot"));
		} else {

			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							angebotDto.getKundeIIdAngebotsadresse());

			angebotnummer
					.append(LPMain.getInstance().getTextRespectUISPr(
							"angb.angebot"))
					.append(" ")
					.append(angebotDto.getCNr())
					.append(" ")
					.append(kundeDto.getPartnerDto().formatFixTitelName1Name2());

		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				angebotnummer.toString());
	}

	public void resetDtos() {
		angebotDto = new AngebotDto();
		kundeDto = new KundeDto();
		kopftextDto = new AngebottextDto();
		fusstextDto = new AngebottextDto();
	}

	/**
	 * Je nach Mandantenparameter muss der Benutzer die Konditionen nicht
	 * erfassen. Damit das Angebot gedruckt werden kann, muessen die Konditionen
	 * aber initialisiert worden sein.
	 *
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void initAngebotKonditionen() throws Throwable {
		initAngebottexte(); // Kopf- und Fusstext werden initialisiert

		getAngebotDto().setBelegtextIIdKopftext(kopftextDto.getIId());
		getAngebotDto().setBelegtextIIdFusstext(fusstextDto.getIId());

		DelegateFactory.getInstance().getAngebotDelegate()
				.updateAngebotOhneWeitereAktion(getAngebotDto());
	}

	/**
	 * Kopf- und Fusstext vorbelegen.
	 *
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void initAngebottexte() throws Throwable {
		if (kopftextDto == null || kopftextDto.getIId() == null) {
			try {
				kopftextDto = DelegateFactory
						.getInstance()
						.getAngebotServiceDelegate()
						.getAngebotkopfDefault(
								getKundeDto().getPartnerDto()
										.getLocaleCNrKommunikation());
			} catch (Throwable t) {
				// wenn es keinen Kopftext gibt
				kopftextDto = DelegateFactory
						.getInstance()
						.getAngebotServiceDelegate()
						.createDefaultAngebottext(
								MediaFac.MEDIAART_KOPFTEXT,
								getKundeDto().getPartnerDto()
										.getLocaleCNrKommunikation());
			}
		}

		if (fusstextDto == null || fusstextDto.getIId() == null) {
			try {
				fusstextDto = DelegateFactory
						.getInstance()
						.getAngebotServiceDelegate()
						.getAngebotfussDefault(
								getKundeDto().getPartnerDto()
										.getLocaleCNrKommunikation());
			} catch (Throwable t) {
				// wenn es keinen Fusstext gibt

				fusstextDto = DelegateFactory
						.getInstance()
						.getAngebotServiceDelegate()
						.createDefaultAngebottext(
								MediaFac.MEDIAART_FUSSTEXT,
								getKundeDto().getPartnerDto()
										.getLocaleCNrKommunikation());
			}
		}
	}

	public boolean pruefeAktuellesAngebot() {
		boolean bIstGueltig = true;

		if (angebotDto == null || angebotDto.getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("angb.warning.keinangebot"));
		}

		return bIstGueltig;
	}

	/**
	 * Diese Methode prueft, ob zur aktuellen Angebot Konditionen erfasst
	 * wurden.
	 *
	 * @return boolean
	 * @throws Throwable
	 */
	protected boolean pruefeKonditionen() throws Throwable {
		boolean bErfasst = true;

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ANGEBOT,
						ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN);

		Short sValue = new Short(parameter.getCWert());

		if (pruefeAktuellesAngebot()) {
			if (Helper.short2boolean(sValue)) {
				if (angebotDto.getBelegtextIIdKopftext() == null
						&& angebotDto.getBelegtextIIdFusstext() == null) {
					bErfasst = false;

					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hint"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hint.konditionenerfassen"));
				}
			} else {
				// die Konditionen initialisieren
				initAngebotKonditionen();
			}
		}

		return bErfasst;
	}

	public boolean aktuellesAngebotHatPositionen() throws Throwable {
		boolean bHatPositionen = true;

		if (DelegateFactory.getInstance().getAngebotpositionDelegate()
				.getAnzahlMengenbehafteteAngebotpositionen(angebotDto.getIId()) <= 0) {
			bHatPositionen = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("angb.warning.keinepositionen"));
		}

		return bHatPositionen;
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_FORMAT_NUMBER:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.belegwerte"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}

	/**
	 * Diese Methode setzt die aktuelle Angebot aus der Auswahlliste als die zu
	 * lockende Angebot.
	 *
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = angebotAuswahl.getSelectedId();

		if (oKey != null) {
			initializeDtos((Integer) oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void printAngebotetikett() throws Throwable {

		if (pruefeAktuellesAngebot()) {
			if (aktuellesAngebotHatPositionen()) {

				// man kann unabhaengig vom Status beliebig oft drucken
				getInternalFrame().showReportKriterien(
						new ReportAngebotAdressetikett(getInternalFrame(),
								getKundeDto().getPartnerDto(), getAngebotDto()
										.getAnsprechpartnerIIdKunde(), ""));
			}
		}
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		angebotAuswahl.getCbSchnellansicht().setEnabled(true);
		if (e.getActionCommand().equals(MENU_DATEI_DRUCKEN)) {
			printAngebot();
		} else if (e.getActionCommand().equals(
				MENU_DATEI_DRUCKEN_VORKALKULATION)) {
			printAngebotVorkalkulation();
		} else if (e.getActionCommand().equals(MENU_DATEI_LIEFERSCHEINETIKETT)) {
			printAngebotetikett();
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_AKQUISEDATEN)) {
			if (pruefeAktuellesAngebot()) {
				if (!getAngebotKopfdaten().isLockedDlg()) {
					// Aksquisedaten koennen gewartet werden, wenn der Status
					// des Angebots != 'Angelegt' und != 'Storniert'
					if (getAngebotDto().getStatusCNr().equals(
							AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)
							|| getAngebotDto().getStatusCNr().equals(
									AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
						showStatusMessage("lp.hint",
								"angb.hint.akquisedatenkoennennichterfasstwerden");
					} else {
						refreshPdAngebotAkquisedaten();
						getInternalFrame().showPanelDialog(
								pdAngebotAkquisedaten);
						setTitleAngebot(LPMain.getInstance()
								.getTextRespectUISPr("angb.akquisedaten"));
					}
				}
			}
		}
		// kommentar: 3 die beiden Menuevents behandeln
		else if (e.getActionCommand()
				.equals(MENU_BEARBEITEN_INTERNER_KOMMENTAR)) {
			if (pruefeAktuellesAngebot()) {
				if (!getAngebotKopfdaten().isLockedDlg()) {
					refreshPdAngebotInternerKommentar();
					getInternalFrame().showPanelDialog(
							pdAngebotInternerKommentar);
					setTitleAngebot(LPMain.getInstance().getTextRespectUISPr(
							"lp.internerkommentar"));
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_EXTERNER_KOMMENTAR)) {
			if (pruefeAktuellesAngebot()) {
				if (!getAngebotKopfdaten().isLockedDlg()) {
					refreshPdAngebotExternerKommentar();
					getInternalFrame().showPanelDialog(
							pdAngebotExternerKommentar);
					setTitleAngebot(LPMain.getInstance().getTextRespectUISPr(
							"lp.externerkommentar"));
				}
			}
		} else if (e.getActionCommand().equals(MENU_JOURNAL_ANGEBOT_ALLE)) {
			getInternalFrame().showReportKriterien(
					new ReportAngebotAlle(getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"angb.print.alle")),
					getKundeDto().getPartnerDto(),
					getAngebotDto().getAnsprechpartnerIIdKunde(), false);
		} else if (e.getActionCommand().equals(MENU_JOURNAL_ANGEBOT_OFFENE)) {
			getInternalFrame().showReportKriterien(
					new ReportAngebotOffene(getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"angb.print.offene")),
					getKundeDto().getPartnerDto(),
					getAngebotDto().getAnsprechpartnerIIdKunde(), false);
		}
		if (e.getActionCommand().equals(MENU_JOURNAL_ANGEBOT_ABGELEHNTE)) {
			getInternalFrame().showReportKriterien(
					new ReportAngebotAbgelehnte(getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"angb.print.abgelehnte")),
					getKundeDto().getPartnerDto(),
					getAngebotDto().getAnsprechpartnerIIdKunde(), false);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_UEBERSICHT)) {
			if (pruefeAktuellesAngebot()) {
				getKriterienAngebotUebersicht();
				getInternalFrame()
						.showPanelDialog(pdKriterienAngebotUebersicht);
				bKriterienAngebotUebersichtUeberMenueAufgerufen = true;
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_POTENTIAL)) {
			getInternalFrame().showReportKriterien(
					new ReportAngebotspotential(getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"angb.angebotspotential")));

		}

		// manuellerledigen: 4 Das Menueevent verarbeiten
		if (e.getActionCommand().equals(MENU_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (pruefeAktuellesAngebot()) {
				if (!getAngebotKopfdaten().isLockedDlg()) {
					// Statuswechsel 'Offen' -> 'Erledigt' : Ausgeloest durch
					// Menue
					if (angebotDto.getStatusCNr().equals(
							AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {

						Object[] options = {
								LPMain.getInstance().getTextRespectUISPr(
										"lp.ja"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.nein"),
								LPMain.getInstance().getTextRespectUISPr(
										"angb.erledigendurchauftrag") };
						int iAnswer = (JOptionPane.showOptionDialog(
								null,
								LPMain.getInstance().getTextRespectUISPr(
										"angb.auferledigtsetzen"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.hint"), JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]));

						switch (iAnswer) {
						case JOptionPane.YES_OPTION: {
							dialogQueryAngeboterledigungsgrundFromListe(e);
						}
							break;
						case JOptionPane.NO_OPTION: {
							// nothing here
						}
							break;
						case 2: {
							dialogQueryAngeboterledigendurchAuftragFromListe(e);
						}
							break;
						default: {
							return;
						}
						}

						/*
						 * if (DialogFactory.showMeldung(
						 * LPMain.getInstance().getTextRespectUISPr
						 * ("angb.auferledigtsetzen"),
						 * LPMain.getInstance().getTextRespectUISPr("lp.hint"),
						 * javax.swing.JOptionPane.YES_NO_OPTION) ==
						 * javax.swing.JOptionPane.YES_OPTION) {
						 *
						 * // der Erledigungsgrund muss aus der Liste
						 * ausgewaehlt werden
						 * dialogQueryAngeboterledigungsgrundFromListe(e); }
						 */
					} else {
						showStatusMessage("lp.warning",
								"angb.warning.angebotkannnichterledigtwerden");
					}
				}
			}
		} else if (e.getActionCommand().equals(MENU_ANSICHT_ANGEBOT_ALLE)) {
			FilterKriterium[] fk = SystemFilterFactory.getInstance()
					.createFKMandantCNr();
			angebotAuswahl.setDefaultFilter(fk);
			angebotAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemAlle.setSelected(true);

		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_HANDARTIKEL_UMANDELN)) {
			if (angebotPositionenTop != null
					&& angebotPositionenTop.getSelectedId() != null) {

				AngebotpositionDto posDto = DelegateFactory
						.getInstance()
						.getAngebotpositionDelegate()
						.angebotpositionFindByPrimaryKey(
								(Integer) angebotPositionenTop.getSelectedId());

				if (posDto.getArtikelIId() != null
						&& getAngebotDto() != null
						&& getAngebotDto().getStatusCNr().equals(
								AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {

					ArtikelDto aDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(posDto.getArtikelIId());
					if (aDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						DialogNeueArtikelnummer d = new DialogNeueArtikelnummer(
								getInternalFrame());
						LPMain.getInstance().getDesktop()
								.platziereDialogInDerMitteDesFensters(d);
						d.setVisible(true);
						if (d.getArtikelnummerNeu() != null) {

							DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.wandleHandeingabeInArtikelUm(
											posDto.getIId(),
											ArtikelFac.HANDARTIKEL_UMWANDELN_ANGEBOT,
											d.getArtikelnummerNeu());
						}

					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr("lp.info"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"angebot.bearbeiten.handartikelumwandeln.error"));
					}

				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.info"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"angebot.bearbeiten.handartikelumwandeln.error1"));
				}
				angebotPositionen.eventYouAreSelected(false);
				angebotPositionenTop.setSelectedId(posDto.getIId());
			}

		} else if (e.getActionCommand().equals(MENU_ANSICHT_ANGEBOT_OFFENE)) {
			FilterKriterium[] fk = AngebotFilterFactory.getInstance()
					.createFKAngebotOffene();
			angebotAuswahl.getCbSchnellansicht().setEnabled(false);
			angebotAuswahl.setDefaultFilter(fk);
			angebotAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemAlleOffenen.setSelected(true);
		} else if (e.getActionCommand().equals(MENU_ANSICHT_ANGEBOT_ANGELEGTE)) {
			FilterKriterium[] fk = AngebotFilterFactory.getInstance()
					.createFKAngebotAngelegte();
			angebotAuswahl.getCbSchnellansicht().setEnabled(false);
			angebotAuswahl.setDefaultFilter(fk);
			angebotAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemAlleAngelegten.setSelected(true);
		} else if (e.getActionCommand().equals(
				MENU_ANSICHT_ANGEBOT_OFFENE_MEINE)) {

			angebotAuswahl.getCbSchnellansicht().setEnabled(false);
			Integer iIdPersonal = LPMain.getInstance().getTheClient()
					.getIDPersonal();
			FilterKriterium[] fk = AngebotFilterFactory.getInstance()
					.createFKAngebotOffeneVertreter(iIdPersonal);
			angebotAuswahl.setDefaultFilter(fk);
			angebotAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemMeineOffenen.setSelected(true);
		} else if (e.getActionCommand().equals(
				MENU_ANSICHT_ANGEBOT_ANGELEGTE_MEINE)) {
			angebotAuswahl.getCbSchnellansicht().setEnabled(false);
			Integer iIdPersonal = LPMain.getInstance().getTheClient()
					.getIDPersonal();
			FilterKriterium[] fk = AngebotFilterFactory.getInstance()
					.createFKAngebotAngelegteVertreter(iIdPersonal);
			angebotAuswahl.setDefaultFilter(fk);
			angebotAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemMeineAngelegten.setSelected(true);
		}
	}

	private void deselektAllMenueBoxes() {
		menuItemAlleAngelegten.setSelected(false);
		menuItemAlle.setSelected(false);
		menuItemAlleOffenen.setSelected(false);
		menuItemMeineOffenen.setSelected(false);
		menuItemMeineAngelegten.setSelected(false);
	}

	public void showStatusMessage(String lpTitle, String lpMessage)
			throws Throwable {
		MessageFormat mf = new MessageFormat(LPMain.getInstance()
				.getTextRespectUISPr(lpMessage));

		mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

		Object pattern[] = { getAngebotStatus() };

		DialogFactory.showModalDialog(
				LPMain.getInstance().getTextRespectUISPr(lpTitle),
				mf.format(pattern));
	}

	public void printAngebot() throws Throwable {
		if (angebotDto.getIId() != null && pruefeKonditionen()) {
			if (aktuellesAngebotHatPositionen()) {
				if (!getAngebotKopfdaten().isLockedDlg()) {
					// pruefen, ob das Hauptlager des Mandanten angelegt ist
					// wegen Werteberechnungen
					DelegateFactory.getInstance().getLagerDelegate()
							.getHauptlagerDesMandanten();

					// das aktuelle Panel wegen Statusanzeige aktualisieren
					refreshAktuellesPanel();

					// das Angebot drucken
					getInternalFrame()
							.showReportKriterien(
									new ReportAngebot(getInternalFrame(),
											getAktuellesPanel(), angebotDto,
											getTitleDruck()),
									getKundeDto().getPartnerDto(),
									getAngebotDto()
											.getAnsprechpartnerIIdKunde(),
									false);
				}
			}
		}
	}

	public void printAngebotVorkalkulation() throws Throwable {
		if (angebotDto.getIId() != null && pruefeKonditionen()) {
			if (aktuellesAngebotHatPositionen()) {
				if (!getAngebotKopfdaten().isLockedDlg()) {
					// pruefen, ob das Hauptlager des Mandanten angelegt ist
					// wegen Werteberechnungen
					DelegateFactory.getInstance().getLagerDelegate()
							.getHauptlagerDesMandanten();

					// das aktuelle Panel wegen Statusanzeige aktualisieren
					refreshAktuellesPanel();

					// das Angebot drucken
					getInternalFrame()
							.showReportKriterien(
									new ReportAngebotVorkalkulation(
											getInternalFrame(),
											angebotDto.getIId(),
											getTitleDruck()),
									getKundeDto().getPartnerDto(),
									getAngebotDto()
											.getAnsprechpartnerIIdKunde(),
									false);
				}
			}
		}
	}

	/**
	 * Der Status des Angebots kann in einigen Faellen ueber den Update Button
	 * geaendert werden.
	 *
	 * @return boolean true, wenn das aktuelle Angebot geaendert werden darf
	 * @throws Throwable
	 *             Ausnahme
	 */
	public boolean aktualisiereAngebotstatusDurchButtonUpdate()
			throws Throwable {
		boolean bIstAktualisierenErlaubt = false;

		if (pruefeAktuellesAngebot()) {
			if (angebotDto.getStatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)
					|| angebotDto.getStatusCNr().equals(
							AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
				// waehrung darf nur geaendert werden, wenn keine
				// mengenbehafteten Positionen
				// oder zuerst Positionen loeschen
				bIstAktualisierenErlaubt = true;
			} else

			// manuellerledigen: 5 Statuswechsel 'Erledigt' -> 'Offen' :
			// Ausgeloest durch Button Update
			if (angebotDto.getStatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
				if (DialogFactory.showMeldung(LPMain.getInstance()
						.getTextRespectUISPr("angb.hint.angeboterledigt"),
						LPMain.getInstance().getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
					DelegateFactory.getInstance().getAngebotDelegate()
							.manuelleErledigungAufheben(angebotDto.getIId());
					initializeDtos(angebotDto.getIId());
					refreshAktuellesPanel();
				}
			} else

			// Statuswechsel 'Storniert' -> 'Angelegt' : Ausgeloest durch Button
			// Update
			if (angebotDto.getStatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
				if (DialogFactory.showMeldung(LPMain.getInstance()
						.getTextRespectUISPr("angb.aufoffensetzen"), LPMain
						.getInstance().getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

					DelegateFactory.getInstance().getAngebotDelegate()
							.stornoAufheben(angebotDto.getIId());
					initializeDtos(angebotDto.getIId());
					refreshAktuellesPanel();
				}
			} else {
				showStatusMessage("lp.warning",
						"angb.warning.angebotkannnichtgeaendertwerden");
			}
		}

		return bIstAktualisierenErlaubt;
	}

	/**
	 * Diese Methode prueft den Status des aktuellen Angebots und legt fest, ob
	 * eine Aenderung in den Kopfdaten bzw. Konditionen erlaubt ist.
	 *
	 * @return boolean true, wenn ein update erlaubt ist
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public boolean istAktualisierenAngebotErlaubt() throws Throwable {
		boolean bIstAenderungErlaubtO = false;

		if (pruefeAktuellesAngebot()) {
			if (angebotDto.getStatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
				bIstAenderungErlaubtO = true;
			} else if (angebotDto.getStatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
				boolean bZuruecknehmen = (DialogFactory.showMeldung(
						LPMain.getInstance().getTextRespectUISPr(
								"angb.hint.offennachangelegt"), LPMain
								.getInstance().getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bZuruecknehmen == true) {
					DelegateFactory
							.getInstance()
							.getAngebotDelegate()
							.setAngebotstatus(angebotDto,
									AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
				}
				return bZuruecknehmen;

			}

			else {
				showStatusMessage("lp.warning",
						"angb.warning.angebotkannnichtgeaendertwerden");
			}
		}

		return bIstAenderungErlaubtO;
	}

	private void refreshPdAngebotAkquisedaten() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdAngebotAkquisedaten = new PanelDialogAngebotAkquisedaten(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"angb.akquisedaten"));
	}

	// kommentar: 4 das Panel fuer den internen Kommentar
	private void refreshPdAngebotInternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdAngebotInternerKommentar = new PanelDialogAngebotKommentar(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.internerkommentar"), true);
	}

	// kommentar: 5 das Panel fuer den externen Kommentar
	private void refreshPdAngebotExternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdAngebotExternerKommentar = new PanelDialogAngebotKommentar(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.externerkommentar"), false);
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);
		// Menue Datei; ein neuer Eintrag wird immer oben im Menue eingetragen
		JMenu jmModul = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);
		JMenuItem menuItemDateiAngebotetikett = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr(
						"ls.menu.datei.lieferscheinetikett"));
		menuItemDateiAngebotetikett.addActionListener(this);
		menuItemDateiAngebotetikett
				.setActionCommand(MENU_DATEI_LIEFERSCHEINETIKETT);
		jmModul.add(menuItemDateiAngebotetikett, 0);
		JMenuItem menuItemDateiDruckenVorkalkulation = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("angb.vorkalkulation")
				+ "...");
		menuItemDateiDruckenVorkalkulation.addActionListener(this);
		menuItemDateiDruckenVorkalkulation
				.setActionCommand(MENU_DATEI_DRUCKEN_VORKALKULATION);
		jmModul.add(menuItemDateiDruckenVorkalkulation, 0);
		JMenuItem menuItemDateiDrucken = new JMenuItem(LPMain.getInstance()
				.getTextRespectUISPr("lp.menu.drucken"));
		menuItemDateiDrucken.addActionListener(this);
		menuItemDateiDrucken.setActionCommand(MENU_DATEI_DRUCKEN);
		jmModul.add(menuItemDateiDrucken, 0);

		// Menue Bearbeiten
		JMenu jmBearbeiten = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		// kommentar: 7 Menueeintrag fuer externen Kommentar
		JMenuItem menuItemBearbeitenExternerKommentar = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("lp.menu.externerkommentar"));
		menuItemBearbeitenExternerKommentar.addActionListener(this);
		menuItemBearbeitenExternerKommentar
				.setActionCommand(MENU_BEARBEITEN_EXTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenExternerKommentar, 0);

		// kommentar: 6 Menueeintrag fuer internen Kommentar
		JMenuItem menuItemBearbeitenInternerKommentar = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("lp.menu.internerkommentar"));
		menuItemBearbeitenInternerKommentar.addActionListener(this);
		menuItemBearbeitenInternerKommentar
				.setActionCommand(MENU_BEARBEITEN_INTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenInternerKommentar, 0);

		jmBearbeiten.add(new JSeparator(), 0);

		// manuellerledigen: 3 Menueeintrag erzeugen
		JMenuItem menuItemHandartikelUmwandeln = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr(
						"angebot.bearbeiten.handartikelumwandeln"));
		menuItemHandartikelUmwandeln.addActionListener(this);
		menuItemHandartikelUmwandeln
				.setActionCommand(MENU_BEARBEITEN_HANDARTIKEL_UMANDELN);
		jmBearbeiten.add(menuItemHandartikelUmwandeln, 0);

		JMenuItem menuItemBearbeitenManuellErledigen = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("ang.menu.menuellerledigen"));
		menuItemBearbeitenManuellErledigen.addActionListener(this);
		menuItemBearbeitenManuellErledigen
				.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN);
		jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);

		JMenuItem menuItemBearbeitenAkquisedaten = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("angb.menu.akquisedaten"));
		menuItemBearbeitenAkquisedaten.addActionListener(this);
		menuItemBearbeitenAkquisedaten
				.setActionCommand(MENU_BEARBEITEN_AKQUISEDATEN);
		jmBearbeiten.add(menuItemBearbeitenAkquisedaten, 0);

		// Menue Journal
		JMenu jmJournal = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_JOURNAL);

		WrapperMenuItem menuItemJournalPotential = new WrapperMenuItem(LPMain
				.getInstance().getTextRespectUISPr("angb.angebotspotential"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemJournalPotential.addActionListener(this);
		menuItemJournalPotential
				.setActionCommand(MENU_ACTION_JOURNAL_POTENTIAL);
		jmJournal.add(menuItemJournalPotential, 0);

		JMenuItem menuItemJournalUebersicht = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("lp.menu.umsatzuebersicht"));
		menuItemJournalUebersicht.addActionListener(this);
		menuItemJournalUebersicht
				.setActionCommand(MENU_ACTION_JOURNAL_UEBERSICHT);
		jmJournal.add(menuItemJournalUebersicht, 0);
		menuItemJournalUebersicht.setEnabled(bDarfPreiseSehen);

		jmJournal.add(new JSeparator(), 0);

		JMenuItem menuItemJournalAbgelehnte = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr(
						"angb.menu.angebotabgelehnte"));
		menuItemJournalAbgelehnte.addActionListener(this);
		menuItemJournalAbgelehnte
				.setActionCommand(MENU_JOURNAL_ANGEBOT_ABGELEHNTE);
		jmJournal.add(menuItemJournalAbgelehnte, 0);

		JMenuItem menuItemJournalAngebotOffene = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("angb.menu.angebotoffene"));
		menuItemJournalAngebotOffene.addActionListener(this);
		menuItemJournalAngebotOffene
				.setActionCommand(MENU_JOURNAL_ANGEBOT_OFFENE);
		jmJournal.add(menuItemJournalAngebotOffene, 0);

		JMenuItem menuItemJournalAngebotAlle = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("angb.menu.angebotalle"));
		menuItemJournalAngebotAlle.addActionListener(this);
		menuItemJournalAngebotAlle.setActionCommand(MENU_JOURNAL_ANGEBOT_ALLE);
		jmJournal.add(menuItemJournalAngebotAlle, 0);

		// zusaetzliches Menue Ansicht, das nur sichtbar ist, wenn man auf dem
		// Panel Auswahl steht
		if (menuAnsicht == null) {
			menuAnsicht = new WrapperMenu("lp.ansicht", this);

			menuItemAlle = new JCheckBoxMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("angb.alle"));
			menuItemAlle.addActionListener(this);
			menuItemAlle.setActionCommand(MENU_ANSICHT_ANGEBOT_ALLE);
			menuAnsicht.add(menuItemAlle);

			menuItemAlleOffenen = new JCheckBoxMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("angb.alleoffenen"));
			menuItemAlleOffenen.addActionListener(this);
			menuItemAlleOffenen.setActionCommand(MENU_ANSICHT_ANGEBOT_OFFENE);
			menuAnsicht.add(menuItemAlleOffenen);

			menuItemAlleAngelegten = new JCheckBoxMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("angb.alleangelegten"));
			menuItemAlleAngelegten.addActionListener(this);
			menuItemAlleAngelegten
					.setActionCommand(MENU_ANSICHT_ANGEBOT_ANGELEGTE);
			menuAnsicht.add(menuItemAlleAngelegten);

			menuItemMeineOffenen = new JCheckBoxMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("angb.meineoffenen"));
			menuItemMeineOffenen.addActionListener(this);
			menuItemMeineOffenen
					.setActionCommand(MENU_ANSICHT_ANGEBOT_OFFENE_MEINE);
			menuAnsicht.add(menuItemMeineOffenen);

			menuItemMeineAngelegten = new JCheckBoxMenuItem(LPMain
					.getInstance().getTextRespectUISPr("angb.meineangelegten"));
			menuItemMeineAngelegten.addActionListener(this);
			menuItemMeineAngelegten
					.setActionCommand(MENU_ANSICHT_ANGEBOT_ANGELEGTE_MEINE);
			menuAnsicht.add(menuItemMeineAngelegten);
		}
		wrapperMenuBar.addJMenuItem(menuAnsicht);

		return wrapperMenuBar;
	}

	public void gotoAuswahl() {
		setSelectedComponent(angebotAuswahl);
	}

	public String getAngebotStatus() throws Throwable {
		return DelegateFactory.getInstance().getLocaleDelegate()
				.getStatusCBez(getAngebotDto().getStatusCNr());
	}

	private String getTitleDruck() {
		StringBuffer buff = new StringBuffer();

		buff.append(getAngebotDto().getCNr());
		buff.append(" ");
		buff.append(getKundeDto().getPartnerDto()
				.getCName1nachnamefirmazeile1());

		return buff.toString();
	}

	public void setTitleAngebotOhneAngebotnummer(String panelTitle)
			throws Exception {
		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	public void setBKriterienAngebotUebersichtUeberMenueAufgerufen(
			boolean bAufgerufenI) {
		bKriterienAngebotUebersichtUeberMenueAufgerufen = bAufgerufenI;
	}

	public PanelQuery getPanelAuswahl() {
		this.setSelectedIndex(IDX_PANEL_AUSWAHL);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.angebotAuswahl;
	}

	public PanelBasis getPanelKopfdaten() {
		this.setSelectedIndex(IDX_PANEL_KOPFDATEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.angebotKopfdaten;
	}

	public PanelSplit getPanelPositionen() {
		this.setSelectedIndex(IDX_PANEL_POSITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.angebotPositionen;
	}

	public PanelBasis getPanelKonditionen() {
		this.setSelectedIndex(IDX_PANEL_KONDITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.angebotKonditionen;
	}

	public Object getDto() {
		return angebotDto;
	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object aoIIdPosition[] = this.angebotPositionenTop.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			AngebotpositionDto[] dtos = new AngebotpositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory
						.getInstance()
						.getAngebotpositionDelegate()
						.angebotpositionFindByPrimaryKey(
								(Integer) aoIIdPosition[i]);
			}

			if (getAngebotPositionenBottom().getArtikelsetViewController()
					.validateCopyConstraintsUI(dtos)) {
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}

	}

	public void einfuegenHV() throws IOException, ExceptionLP,
			ParserConfigurationException, Throwable, SAXException {

		Object o = LPMain.getInstance().getPasteBuffer()
				.readObjectFromPasteBuffer();

		if (!getAngebotPositionenBottom().getArtikelsetViewController()
				.validatePasteConstraintsUI(o)) {
			return;
		}

		if (o instanceof BelegpositionDto[]) {
			AngebotpositionDto[] anfragepositionDtos = DelegateFactory
					.getInstance().getBelegpostionkonvertierungDelegate()
					.konvertiereNachAngebotpositionDto((BelegpositionDto[]) o);

			if (anfragepositionDtos != null) {

				// Nur Position anlegen wenn aktualiseren erlaubt ist
				boolean bDarfPosAnlegen = istAktualisierenAngebotErlaubt();
				if (bDarfPosAnlegen) {

					Integer iId = null;

					Boolean b = positionAmEndeEinfuegen();
					if (b != null) {

						for (int i = 0; i < anfragepositionDtos.length; i++) {
							AngebotpositionDto positionDto = anfragepositionDtos[i];
							try {
								positionDto.setIId(null);
								// damits hinten angehaengt wird.

								if (b == false) {
									Integer iIdAktuellePosition = (Integer) getAngebotPositionenTop()
											.getSelectedId();

									// erstepos: 0 die erste Position steht an
									// der Stelle 1
									Integer iSortAktuellePosition = new Integer(
											1);

									// erstepos: 1 die erste Position steht an
									// der Stelle 1
									if (iIdAktuellePosition != null) {
										AngebotpositionDto aktuellePositionDto = DelegateFactory
												.getInstance()
												.getAngebotpositionDelegate()
												.angebotpositionFindByPrimaryKey(
														iIdAktuellePosition);

										iSortAktuellePosition = aktuellePositionDto
												.getISort();
										// Die bestehenden Positionen muessen
										// Platz fuer die neue schaffen
										DelegateFactory
												.getInstance()
												.getAngebotpositionDelegate()
												.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
														getAngebotDto()
																.getIId(),
														iSortAktuellePosition
																.intValue());

										getAngebotPositionenBottom()
												.getArtikelsetViewController()
												.setArtikelSetIIdFuerNeuePosition(
														aktuellePositionDto
																.getPositioniIdArtikelset());

									} else {
										iSortAktuellePosition = null;
									}

									// Die neue Position wird an frei gemachte
									// Position gesetzt
									positionDto.setISort(iSortAktuellePosition);
								} else {
									positionDto.setISort(null);
								}

								positionDto.setBelegIId(this.getAngebotDto()
										.getIId());

								// wir legen eine neue position an
								ArtikelsetViewController viewController = getAngebotPositionenBottom()
										.getArtikelsetViewController();

								boolean bDiePositionSpeichern = viewController
										.validateArtikelsetConstraints(positionDto);

								if (bDiePositionSpeichern) {
									positionDto
											.setPositioniIdArtikelset(viewController
													.getArtikelSetIIdFuerNeuePosition());
									Integer newIId = DelegateFactory
											.getInstance()
											.getAngebotpositionDelegate()
											.createAngebotposition(positionDto);
									if (iId == null) {
										iId = newIId;
									}
									anfragepositionDtos[i] = DelegateFactory
											.getInstance()
											.getAngebotpositionDelegate()
											.angebotpositionFindByPrimaryKey(
													newIId);
								}
							} catch (Throwable t) {
								// nur loggen!
								myLogger.error(t.getMessage(), t);
							}
						}
					}

					ZwsEinfuegenHVAngebotposition cpp = new ZwsEinfuegenHVAngebotposition();
					cpp.processZwsPositions(anfragepositionDtos,
							(BelegpositionDto[]) o);

					// die Liste neu aufbauen
					angebotPositionenTop.eventYouAreSelected(false);
					// den Datensatz in der Liste selektieren
					angebotPositionenTop.setSelectedId(iId);
					// im Detail den selektierten anzeigen
					angebotPositionen.eventYouAreSelected(false);
				}

			}

		}

	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI)
			throws Throwable {

		AngebotpositionDto angebotpositionDtoI = (AngebotpositionDto) belegposDtoI;
		angebotpositionDtoI.setBelegIId(angebotDto.getIId());
		angebotpositionDtoI.setISort(xalOfBelegPosI + 1000);
		if (angebotpositionDtoI.getBArtikelbezeichnunguebersteuert() == null) {
			angebotpositionDtoI.setBArtikelbezeichnunguebersteuert(Helper
					.boolean2Short(false));
		}

	}

	protected Integer getSelectedIdPositionen() throws Throwable {
		return (Integer) getAngebotPositionenTop().getSelectedId();
	}

	@Override
	protected void initDtos() throws Throwable {
		initializeDtos(angebotDto.getIId());
	}

}
