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
package com.lp.client.auftrag;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.artikel.DialogNeueArtikelnummer;
import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
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
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragartDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragtextDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;

/*
 * <p>Tabbed pane fuer Komponente Auftrag.</p> <p>Copyright Logistik Pur
 * Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum 2004-10-28</p> <p> </p>
 *
 * @author Uli Walch
 *
 * @version $Revision: 1.61 $
 */
public class TabbedPaneAuftrag extends TabbedPane implements ICopyPaste {

	private static final long serialVersionUID = 1L;

	private JLabel bestellungenSumme = null;
	private JLabel splittbetragSumme = null;
	private PanelQuery auftragAuswahl = null; // FLR Liste
	private PanelAuftragKopfdaten auftragKopfdaten = null;
	private PanelAuftragKonditionen2 auftragKonditionen = null;
	private PanelQuery auftragSichtLieferstatus = null;

	private PanelQuery auftragPositionenTop = null;
	private PanelAuftragPositionen2 auftragPositionenBottom = null;
	private PanelSplit auftragPositionen = null; // FLR 1:n Liste

	private PanelQuery auftragTeilnehmerTop = null;
	private PanelBasis auftragTeilnehmerBottom = null;
	private PanelSplit auftragTeilnehmer = null; // FLR 1:n Liste

	private PanelQuery auftragZeitplanTop = null;
	private PanelBasis auftragZeitplanBottom = null;
	private PanelSplit auftragZeitplan = null; // FLR 1:n Liste

	private PanelQuery auftragZahlungsplanTop = null;

	public PanelQuery getAuftragZahlungsplanTop() {
		return auftragZahlungsplanTop;
	}

	private PanelBasis auftragZahlungsplanBottom = null;
	private PanelSplit auftragZahlungsplan = null; // FLR 1:n Liste

	private PanelQuery auftragZahlungsplanmeilensteinTop = null;

	public PanelQuery getAuftragZahlungsplanmeilensteinTop() {
		return auftragZahlungsplanmeilensteinTop;
	}

	private PanelBasis auftragZahlungsplanmeilensteinBottom = null;
	private PanelSplit auftragZahlungsplanmeilenstein = null; // FLR 1:n Liste

	private PanelQuery sichtAuftragAufAndereBelegartenTop = null;
	private PanelBasis sichtAuftragAufAndereBelegartenBottom = null;
	private PanelSplit sichtAuftragAufAndereBelegarten = null; // FLR 1:n Liste

	private PanelQuery auftragSichtRahmenTop = null;
	private PanelBasis auftragSichtRahmenBottom = null;
	private PanelSplit auftragSichtRahmen = null; // FLR 1:n Liste

	private PanelQuery panelAuftragsnrnrnTopQP;
	private PanelSplit panelAuftragsnrnrnSP;
	private PanelStammdatenCRUD panelAuftragsnrnrnBottomD;

	private PanelDialogKriterienAuftragUebersicht pdKriterienAuftragUebersicht = null;
	private boolean bKriterienAuftragUebersichtUeberMenueAufgerufen = false;
	private PanelTabelleAuftragUebersicht ptAuftragUebersicht = null;

	private PanelDialogKriterienAuftragzeiten pdKriterienAuftragzeiten = null;
	private boolean pdKriterienAuftragzeitenUeberMenueAufgerufen = false;
	private PanelTabelleAuftragzeiten ptAuftragzeiten = null;

	private PanelBasis panelDetailAuftragseigenschaft = null;

	private PanelDialogAuftragArtikelSchnellanlage panelDialogAuftragArtikelSchnellanlageArtikelAendern = null;

	// private PanelQuery auftragRechnungen = null;
	// private PanelQuery auftragLieferschein = null;
	private PanelTabelleSichtLSRE auftragLSRE = null;

	private PanelQuery auftragLose = null;
	private PanelQuery auftragBestellungen = null;
	private PanelQuery auftragEingangsrechnung = null;
	private PanelQueryFLR panelQueryFLRBegruendung = null;

	private Integer iIdAuftrag;

	public static final int IDX_PANEL_AUFTRAGAUSWAHL = 0;
	public static final int IDX_PANEL_AUFTRAGKOPFDATEN = 1;
	private final int IDX_PANEL_AUFTRAGPOSITIONEN = 2;
	private final int IDX_PANEL_AUFTRAGKONDITIONEN = 3;
	private final int IDX_PANEL_AUFTRAGTEILNEHMER = 4;
	private final int IDX_PANEL_SICHTLIEFERSTATUS = 5;
	private final int IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN = 6;
	private final int IDX_PANEL_SICHTRAHMEN = 7;
	private final int IDX_PANEL_AUFTRAGUEBERSICHT = 8;
	private final int IDX_PANEL_AUFTRAGZEITEN = 9;
	private final int IDX_PANEL_LSRE = 10;
	private final int IDX_PANEL_LOSE = 11;
	private final int IDX_PANEL_BESTELLUNGEN = 12;
	private final int IDX_PANEL_EINGANGSRECHNUNGEN = 13;
	private int IDX_PANEL_AUFTRAGEIGENSCHAFTEN = -1;
	private int IDX_PANEL_ZEITPLAN = -1;
	private int IDX_PANEL_ZAHLUNGSPLAN = -1;
	private int IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN = -1;

	// dtos, die in mehr als einem panel benoetigt werden
	private AuftragDto auftragDto = null;
	private KundeDto kundeAuftragDto = null;
	private AuftragtextDto kopftextDto = null; // belegartkonditionen: 1
	// Kopftext unabhaengig vom
	// PanelKontionen hinterlegen
	private AuftragtextDto fusstextDto = null; // belegartkonditionen: 2
	// Fusstext unabhaengig vom
	// PanelKontionen hinterlegen

	private final String MENU_ACTION_DATEI_NEU_AUS_AUFTRAG = "MENU_ACTION_DATEI_NEU_AUS_AUFTRAG";
	private final String MENU_ACTION_DATEI_AUFTRAGBESTAETIGUNG = "MENU_ACTION_DATEI_AUFTRAGBESTAETIGUNG";
	private final String MENU_ACTION_DATEI_VERSANDWEGBESTAETIGUNG = "MENU_ACTION_DATEI_VERSANDWEGBESTAETIGUNG";
	private final String MENU_ACTION_DATEI_PACKLISTE = "MENU_ACTION_DATEI_PACKLISTE";
	private final String MENU_ACTION_DATEI_ETIKETT = "MENU_ACTION_DATEI_ETIKETT";

	private final String MENU_ACTION_JOURNAL_OFFEN = "MENU_ACTION_JOURNAL_OFFEN";
	private final String MENU_ACTION_JOURNAL_OFFEN_DETAILS = "MENU_ACTION_JOURNAL_OFFEN_DETAILS";
	private final String MENU_ACTION_JOURNAL_OFFEN_POSITIONEN = "MENU_ACTION_JOURNAL_OFFEN_POSITIONEN";
	private final String MENU_ACTION_JOURNAL_UEBERSICHT = "MENU_ACTION_JOURNAL_UEBERSICHT";
	private final String MENU_ACTION_JOURNAL_STATISTIK = "MENU_ACTION_JOURNAL_STATISTIK";
	private final String MENU_ACTION_JOURNAL_ERFUELLUNGSGRAD = "MENU_ACTION_JOURNAL_ERFUELLUNGSGRAD";
	private final String MENU_ACTION_JOURNAL_WARTUNGSAUSWERTUNG = "MENU_ACTION_JOURNAL_WARTUNGSAUSWERTUNG";
	private final String MENU_ACTION_JOURNAL_ALLE_AUFTRAEGE = "MENU_ACTION_JOURNAL_ALLE_AUFTRAEGE";
	private final String MENU_ACTION_JOURNAL_ERLEDIGTE_AUFTRAEGE = "MENU_ACTION_JOURNAL_ERLEDIGTE_AUFTRAEGE";
	private final String MENU_ACTION_JOURNAL_TAETIGKEITSSTATISTIK = "MENU_ACTION_JOURNAL_TAETIGKEITSSTATISTIK";

	private final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN";
	private final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN";
	private final String MENU_BEARBEITEN_INTERNER_KOMMENTAR = "MENU_BEARBEITEN_INTERNER_KOMMENTAR";
	private final String MENU_BEARBEITEN_EXTERNER_KOMMENTAR = "MENU_BEARBEITEN_EXTERNER_KOMMENTAR";
	private final String MENU_BEARBEITEN_ANGEBOT_ZEITDATEN_UEBERNEHMEN = "MENU_BEARBEITEN_ANGEBOT_ZEITDATEN_UEBERNEHMEN";
	private final String MENU_BEARBEITEN_ERFUELLUNGSGRAD = "MENU_BEARBEITEN_ERFUELLUNGSGRAD";
	private final String MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN = "MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN";

	private final String MENU_ACTION_INFO_AUFTRAGZEITEN = "MENU_ACTION_INFO_AUFTRAGZEITEN";
	private final String MENU_ACTION_INFO_NACHKALKULATION = "MENU_ACTION_INFO_NACHKALKULATION";
	private final String MENUE_ACTION_INFO_WIEDERBESCHAFFUNG = "MENU_ACTION_INFO_WIEDERBESCHAFFUNG";
	private final String MENUE_ACTION_INFO_VERFUEGBARKEITSPRUEFUNG = "MENUE_ACTION_INFO_VERFUEGBARKEITSPRUEFUNG";
	private final String MENUE_ACTION_INFO_ROLLIERENDEPLANUNG = "MENUE_ACTION_INFO_ROLLIERENDEPLANUNG";
	private final String MENUE_ACTION_INFO_RAHMENERFUELLUNG = "MENUE_ACTION_INFO_RAHMENERFUELLUNG";
	private final String MENUE_ACTION_INFO_RAHMENUEBERSICHT = "MENUE_ACTION_INFO_RAHMENUEBERSICHT";
	private final String MENUE_ACTION_INFO_AUFTRAGSUEBERSICHT = "MENU_ACTION_INFO_AUFTRAGSUEBERSICHT";
	private final String MENUE_ACTION_INFO_PROJEKTBLATT = "MENU_ACTION_INFO_PROJEKTBLATT";

	private static final String ACTION_SPECIAL_CSVIMPORT_POSITIONEN = "ACTION_SPECIAL_CSVIMPORT_POSITIONEN";
	private static final String ACTION_SPECIAL_SCHNELLERFASSUNG_POSITIONEN = "ACTION_SPECIAL_SCHNELLERFASSUNG_POSITIONEN";

	private final String MENU_BEARBEITEN_HANDARTIKEL_UMANDELN = "MENU_BEARBEITEN_HANDARTIKEL_UMANDELN";

	private static final String ACTION_SPECIAL_NEU_AUS_PROJEKT = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_NEU_AUS_PROJEKT";

	private static final String ACTION_SPECIAL_PREISE_NEU_KALKULIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_PREISE_NEU_KALKULIEREN";

	public final static String EXTRA_NEU_EXTERNER_TEILNEHMER = "externer_teilnehmer";
	public final static String EXTRA_NEU_AUS_ANGEBOT = "aus_angebot";
	public final static String EXTRA_RAHMENAUFTRAG_NEU_AUS_ANGEBOT = "rahmenauftrag_aus_angebot";
	public final static String EXTRA_NEU_AUS_AUFTRAG = "aus_auftrag";
	public final static String EXTRA_ABRUF_ZU_RAHMEN = "extra_abruf_zu_rahmen";
	public final static String EXTRA_NEU_AUS_SCHNELLANLAGE = "aus_schnellanlage";
	public final static String EXTRA_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN = "aus_schnellanlage_bearbeiten";

	private final String MENU_BEARBEITEN_BEGRUENDUNG = "MENU_BEARBEITEN_BEGRUENDUNG";

	private String MY_OWN_NEW_NEU_EXTERNER_TEILNEHMER = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_EXTERNER_TEILNEHMER;
	private String MY_OWN_NEW_RAHMENAUFTRAG_NEU_AUS_ANGEBOT = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_RAHMENAUFTRAG_NEU_AUS_ANGEBOT;

	private String MY_OWN_NEW_NEU_AUS_ANGEBOT = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_AUS_ANGEBOT;

	private String MY_OWN_NEW_NEU_AUS_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_AUS_AUFTRAG;

	private String MY_OWN_NEW_NEU_AUS_SCHNELLANLAGE = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_AUS_SCHNELLANLAGE;

	private String MY_OWN_NEW_NEU_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN;

	private String MY_OWN_NEW_TOGGLE_VERRECHENBAR = PanelBasis.ACTION_MY_OWN_NEW
			+ "TOGGLE_VERRECHENBAR";

	private String MY_OWN_NEW_ABRUF_ZU_RAHMEN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_ABRUF_ZU_RAHMEN;
	private String MENUE_ACTION_ETIKETT_DRUCKEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_ETIKETT_DRUCKEN";
	private final String BUTTON_IMPORTCSV_AUFTRAGPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_CSVIMPORT_POSITIONEN;
	private final String BUTTON_SCHNELLERFASSUNG_AUFTRAGPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SCHNELLERFASSUNG_POSITIONEN;

	public final static String LIEFERSCHEIN_PREISE_UPDATEN = "lp_preise_updaten";

	private String MY_OWN_NEW_LIEFERSCHEIN_PREISE_UPDATEN = PanelBasis.ACTION_MY_OWN_NEW
			+ LIEFERSCHEIN_PREISE_UPDATEN;

	private PanelQueryFLR panelQueryFLRAuftragauswahl = null;
	private PanelQueryFLR panelQueryFLRAngebotauswahl = null;
	private PanelQueryFLR panelQueryFLRAngebotauswahlFuerRahmenauftrag = null;

	private PanelQueryFLR panelQueryFLRProjekt = null;

	private PanelDialogAuftragKommentar pdAuftragInternerKommentar = null;
	private PanelDialogAuftragKommentar pdAuftragExternerKommentar = null;

	// Bitmuster fuer das Enable der Panels
	boolean[] aPanelsEnabled = new boolean[16];

	private boolean bAuftragTerminStundenMinuten = false;

	boolean bEsGibtRahmenauftraege = false;
	private WrapperMenuBar menuBar = null;

	public TabbedPaneAuftrag(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("auft.auftrag"));

		jbInit();
		initComponents();
	}

	private void initializePanelsEnabled() {
		for (int i = 0; i < aPanelsEnabled.length; i++) {
			aPanelsEnabled[i] = true;
		}
	}

	public InternalFrameAuftrag getInternalFrameAuftrag() throws Throwable {
		return (InternalFrameAuftrag) getInternalFrame();
	}

	public PanelQuery getAuftragAuswahl() {
		return auftragAuswahl;
	}

	public PanelBasis getAuftragKopfdaten() {
		return auftragKopfdaten;
	}

	public PanelQuery getAuftragPositionenTop() { // isort: 0
		return auftragPositionenTop;
	}

	public PanelAuftragPositionen2 getAuftragPositionenBottom() {
		return auftragPositionenBottom;
	}

	public PanelQuery getAuftragTeilnehmerTop() {
		return auftragTeilnehmerTop;
	}

	public PanelBasis getAuftragTeilnehmerBottom() {
		return auftragTeilnehmerBottom;
	}

	public PanelQuery getSichtAuftragAufAndereBelegartenTop() {
		return sichtAuftragAufAndereBelegartenTop;
	}

	public PanelQuery getAuftragSichtRahmenTop() {
		return auftragSichtRahmenTop;
	}

	public PanelQuery getAuftragSichtLieferstatus() {
		return auftragSichtLieferstatus;
	}

	public AuftragDto getAuftragDto() {
		return auftragDto;
	}

	public void setAuftragDto(AuftragDto dto) {
		auftragDto = dto;
	}

	public KundeDto getKundeAuftragDto() {
		return kundeAuftragDto;
	}

	public void setKundeAuftragDto(KundeDto dto) {
		kundeAuftragDto = dto;
	}

	public AuftragtextDto getKopftextDto() {
		return kopftextDto;
	}

	public void setKopftextDto(AuftragtextDto kopftextDtoI) {
		kopftextDto = kopftextDtoI;
	}

	public AuftragtextDto getFusstextDto() {
		return fusstextDto;
	}

	public void setFusstextDto(AuftragtextDto fusstextDtoI) {
		fusstextDto = fusstextDtoI;
	}

	public boolean getBAuftragterminstudenminuten() {
		return bAuftragTerminStundenMinuten;
	}

	protected Integer getSelectedIdPositionen() throws Throwable {
		return (Integer) getAuftragPositionenTop().getSelectedId();
	}

	private void jbInit() throws Throwable {
		// dafuer sorgen, dass die Dtos != null sind
		resetDtos();
		// SK: Wenn es die Auftragart Rahmenauftrag nicht gibt soll der Button
		// nicht angezeigt werden
		AuftragartDto[] auftragArt = DelegateFactory.getInstance()
				.getAuftragServiceDelegate().auftragartFindAll();

		for (int i = 0; i < auftragArt.length; i++) {
			if (AuftragServiceFac.AUFTRAGART_RAHMEN.equals(auftragArt[i]
					.getCNr())) {
				bEsGibtRahmenauftraege = true;
			}
		}
		initializePanelsEnabled();

		// die Liste der Auftraege aufbauen
		refreshAuftragAuswahl();

		// die Liste der Auftraege anzeigen
		// auftragAuswahl.eventYouAreSelected(false);

		// den aktuell gewaehlten Auftrag hinterlegen und den Lock setzen
		setKeyWasForLockMe();

		insertTab(LPMain.getTextRespectUISPr("auft.title.panel.auswahl"), null,
				auftragAuswahl,
				LPMain.getTextRespectUISPr("auft.title.tooltip.auswahl"),
				IDX_PANEL_AUFTRAGAUSWAHL);

		// die restlichen Panels erst bei Bedarf laden
		insertTab(LPMain.getTextRespectUISPr("auft.title.panel.kopfdaten"),
				null, null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.kopfdaten"),
				IDX_PANEL_AUFTRAGKOPFDATEN);

		insertTab(LPMain.getTextRespectUISPr("auft.title.panel.positionen"),
				null, null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.positionen"),
				IDX_PANEL_AUFTRAGPOSITIONEN);

		insertTab(LPMain.getTextRespectUISPr("auft.title.panel.konditionen"),
				null, null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.konditionen"),
				IDX_PANEL_AUFTRAGKONDITIONEN);

		insertTab(LPMain.getTextRespectUISPr("auft.title.panel.teilnehmer"),
				null, null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.teilnehmer"),
				IDX_PANEL_AUFTRAGTEILNEHMER);

		insertTab(
				LPMain.getTextRespectUISPr("auft.title.panel.sichtlieferstatus"),
				null,
				null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.sichtlieferstatus"),
				IDX_PANEL_SICHTLIEFERSTATUS);

		insertTab(
				LPMain.getTextRespectUISPr("auft.title.panel.auftragpositioninlieferschein"),
				null,
				null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.auftragpositioninlieferschein"),
				IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN);
		// if(bEsGibtRahmenauftraege){
		insertTab(LPMain.getTextRespectUISPr("lp.sichtrahmen"), null, null,
				LPMain.getTextRespectUISPr("lp.sichtrahmen"),
				IDX_PANEL_SICHTRAHMEN);
		// }
		insertTab(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), null,
				null, LPMain.getTextRespectUISPr("lp.umsatzuebersicht"),
				IDX_PANEL_AUFTRAGUEBERSICHT);

		insertTab(LPMain.getTextRespectUISPr("auft.title.panel.auftragzeiten"),
				null, null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.auftragzeiten"),
				IDX_PANEL_AUFTRAGZEITEN);

		insertTab(LPMain.getTextRespectUISPr("auft.title.panel.lsre"), null,
				null, "LSRE", IDX_PANEL_LSRE);

		insertTab(LPMain.getTextRespectUISPr("auft.title.panel.lose"), null,
				null, LPMain.getTextRespectUISPr("auft.title.tooltip.lose"),
				IDX_PANEL_LOSE);
		insertTab(LPMain.getTextRespectUISPr("bes.bestellungen"), null, null,
				LPMain.getTextRespectUISPr("bes.bestellungen"),
				IDX_PANEL_BESTELLUNGEN);

		insertTab(LPMain.getTextRespectUISPr("er.modulname.tooltip"), null,
				null, LPMain.getTextRespectUISPr("er.modulname.tooltip"),
				IDX_PANEL_EINGANGSRECHNUNGEN);

		int tabIndexAbEingangsrechnung = IDX_PANEL_EINGANGSRECHNUNGEN;

		// Wenn keine Panelbeschriebung vorhanden, dann ausblenden
		PanelbeschreibungDto[] dtos = DelegateFactory
				.getInstance()
				.getPanelDelegate()
				.panelbeschreibungFindByPanelCNrMandantCNr(
						PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN, null);
		if (dtos != null && dtos.length > 0) {
			tabIndexAbEingangsrechnung++;
			IDX_PANEL_AUFTRAGEIGENSCHAFTEN = tabIndexAbEingangsrechnung;

			insertTab(LPMain.getTextRespectUISPr("lp.eigenschaften"), null,
					null, LPMain.getTextRespectUISPr("lp.eigenschaften"),
					IDX_PANEL_AUFTRAGEIGENSCHAFTEN);
		}

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG)) {
			tabIndexAbEingangsrechnung++;
			IDX_PANEL_ZEITPLAN = tabIndexAbEingangsrechnung;
			insertTab(LPMain.getTextRespectUISPr("auft.zeitplan"), null, null,
					LPMain.getTextRespectUISPr("auft.zeitplan"),
					IDX_PANEL_ZEITPLAN);
			tabIndexAbEingangsrechnung++;
			IDX_PANEL_ZAHLUNGSPLAN = tabIndexAbEingangsrechnung;
			insertTab(LPMain.getTextRespectUISPr("auft.zahlungsplan"), null,
					null, LPMain.getTextRespectUISPr("auft.zahlungsplan"),
					IDX_PANEL_ZAHLUNGSPLAN);
			tabIndexAbEingangsrechnung++;
			IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN = tabIndexAbEingangsrechnung;
			insertTab(
					LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein"),
					null, null,
					LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein"),
					IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN);
		}

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_AUFTRAGTERMIN_STUNDEN_MINUTEN)) {
			bAuftragTerminStundenMinuten = true;
		}

		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	private void refreshAuftragAuswahl() throws Throwable {
		if (auftragAuswahl == null) {
			QueryType[] qtAuswahl = AuftragFilterFactory.getInstance()
					.createQTPanelAuftragAuswahl();
			FilterKriterium[] filterAuswahl = SystemFilterFactory.getInstance()
					.createFKMandantCNr(); // die Filterkriterien aendern sich
			// nicht

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			auftragAuswahl = new PanelQuery(qtAuswahl, filterAuswahl,
					QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.auswahl"),
					true,
					AuftragFilterFactory.getInstance().createFKVAuftrag(), null); // liste
																					// refresh
																					// wenn
																					// lasche
																					// geklickt
																					// wurde

			// fkdirekt: 0 dem PanelQuery die direkten FilterKriterien setzen
			FilterKriteriumDirekt fkDirekt1 = AuftragFilterFactory
					.getInstance().createFKDAuftragnummer();

			FilterKriteriumDirekt fkDirekt2 = AuftragFilterFactory
					.getInstance().createFKDKundenname();

			auftragAuswahl.befuellePanelFilterkriterienDirekt(fkDirekt1,
					fkDirekt2);
			auftragAuswahl.addDirektFilter(AuftragFilterFactory.getInstance()
					.createFKDProjekt());
			auftragAuswahl.addDirektFilter(AuftragFilterFactory.getInstance()
					.createFKDTextSuchen());

			auftragAuswahl
					.befuelleFilterkriteriumSchnellansicht(AuftragFilterFactory
							.getInstance().createFKSchnellansicht());
			auftragAuswahl
					.createAndSaveAndShowButton(
							"/com/lp/client/res/presentation_chart16x16.png",
							LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemangebot"),
							MY_OWN_NEW_NEU_AUS_ANGEBOT,
							RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragAuswahl
					.createAndSaveAndShowButton(
							"/com/lp/client/res/presentation.png",
							LPMain.getTextRespectUISPr("lp.tooltip.rahmenauftragausbestehendemangebot"),
							MY_OWN_NEW_RAHMENAUFTRAG_NEU_AUS_ANGEBOT,
							RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragAuswahl
					.createAndSaveAndShowButton(
							"/com/lp/client/res/auftrag16x16.png",
							LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemauftrag"),
							MY_OWN_NEW_NEU_AUS_AUFTRAG,
							RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			ParametermandantDto parameterMandant = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR);

			boolean b = (Boolean) parameterMandant.getCWertAsObject();
			if (b == true) {
				auftragAuswahl
						.createAndSaveAndShowButton(
								"/com/lp/client/res/calculator16x16.png",
								LPMain.getTextRespectUISPr("auftrag.toggle.verrechenbar"),
								MY_OWN_NEW_TOGGLE_VERRECHENBAR,
								RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

			if (bEsGibtRahmenauftraege) {
				auftragAuswahl.createAndSaveAndShowButton(
						"/com/lp/client/res/abruf.png",
						LPMain.getTextRespectUISPr("auft.abrufzurahmen"),
						MY_OWN_NEW_ABRUF_ZU_RAHMEN,
						RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

			ArbeitsplatzparameterDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_AUFT_SERIENNUMMERNETIKETTENDRUCK);
			if (parameter != null && parameter.getCWert() != null
					&& parameter.getCWert().equals("1")) {
				auftragAuswahl.createAndSaveAndShowButton(
						"/com/lp/client/res/printer216x16.png",
						LPMain.getTextRespectUISPr("auft.seriennummerdrucken")
								+ " F11", MENUE_ACTION_ETIKETT_DRUCKEN,
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11,
								0), null);
			}

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
				auftragAuswahl.createAndSaveAndShowButton(
						"/com/lp/client/res/briefcase2_document16x16.png",
						LPMain.getTextRespectUISPr("angb.neuausprojekt"),
						ACTION_SPECIAL_NEU_AUS_PROJEKT,
						RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_AUFTRAG_SCHNELLANLAGE)) {
				auftragAuswahl.createAndSaveAndShowButton(
						"/com/lp/client/res/flash.png",
						LPMain.getTextRespectUISPr("auft.schnellanlage"),
						MY_OWN_NEW_NEU_AUS_SCHNELLANLAGE,
						RechteFac.RECHT_AUFT_AUFTRAG_CUD);
				auftragAuswahl
						.createAndSaveAndShowButton(
								"/com/lp/client/res/nut_and_bolt24x24.png",
								LPMain.getTextRespectUISPr("auftrag.schnellanlage.artikel.bearbeiten"),
								MY_OWN_NEW_NEU_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN,
								RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

		}
	}

	private PanelTabelleSichtLSRE getPanelTabelleLoszeiten() throws Throwable {

		auftragLSRE = new PanelTabelleSichtLSRE(
				QueryParameters.UC_ID_AUFTRAGSICHTLSRE,
				LPMain.getTextRespectUISPr("auft.title.panel.lsre"),
				getInternalFrame());

		// default Kriterium vorbelegen
		FilterKriterium[] aFilterKrit = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium("auftrag_i_id", true,
				auftragDto.getIId().toString(), FilterKriterium.OPERATOR_EQUAL,
				false);

		aFilterKrit[0] = krit1;

		auftragLSRE.setDefaultFilter(aFilterKrit);

		setComponentAt(IDX_PANEL_LSRE, auftragLSRE);

		return auftragLSRE;
	}

	private void refreshLose() throws Throwable {
		FilterKriterium[] filtersRechnungen = FertigungFilterFactory
				.getInstance().createFKAuftrag(auftragDto.getIId());
		if (auftragLose == null) {
			FilterKriterium[] filterAuswahl = new FilterKriterium[1];
			filterAuswahl[0] = new FilterKriterium("flrlos.mandant_c_nr", true,
					"'" + LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);

			// nicht

			String[] aWhichButtonIUse = {};

			auftragLose = new PanelQuery(null, filterAuswahl,
					QueryParameters.UC_ID_LOS, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.lose"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde
			FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory
					.getInstance().createFKDLosnummer();
			FilterKriteriumDirekt fkDirekt2 = FertigungFilterFactory
					.getInstance().createFKDArtikelnummer();
			auftragLose
					.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
			FilterKriteriumDirekt fkDirektStatistikadresse = RechnungFilterFactory
					.getInstance().createFKDKundestatistikadresse();
			auftragLose.addDirektFilter(fkDirektStatistikadresse);
			this.setComponentAt(IDX_PANEL_LOSE, auftragLose);
		}
		auftragLose.setDefaultFilter(filtersRechnungen);
	}

	private void refreshBestellungen() throws Throwable {
		FilterKriterium[] filtersRechnungen = BestellungFilterFactory
				.getInstance().createFKAuftrag(auftragDto.getIId());
		if (auftragBestellungen == null) {

			// nicht

			String[] aWhichButtonIUse = {};

			auftragBestellungen = new PanelQuery(null, filtersRechnungen,
					QueryParameters.UC_ID_BESTELLUNG, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.bestellungen"), true); // liste
			bestellungenSumme = new JLabel();
			auftragBestellungen.getToolBar().getToolsPanelCenter()
					.add(bestellungenSumme);
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde
			FilterKriteriumDirekt fkDirekt1 = BestellungFilterFactory
					.getInstance().getFilterkriteriumDirekt1();
			FilterKriteriumDirekt fkDirekt2 = BestellungFilterFactory
					.getInstance().createFKDProjekt();
			auftragBestellungen.befuellePanelFilterkriterienDirekt(fkDirekt1,
					fkDirekt2);

			this.setComponentAt(IDX_PANEL_BESTELLUNGEN, auftragBestellungen);
		}

		BigDecimal bestellwert = DelegateFactory.getInstance()
				.getAuftragDelegate().berechneBestellwertAuftrag(iIdAuftrag);

		bestellungenSumme.setText(LPMain
				.getTextRespectUISPr("auft.bestellungen.summe")
				+ " = "
				+ Helper.formatZahl(bestellwert, 2, LPMain.getTheClient()
						.getLocUi()));

		auftragBestellungen.setDefaultFilter(filtersRechnungen);
	}

	private void refreshEingangsrechnungen() throws Throwable {
		FilterKriterium[] filtersRechnungen = AuftragFilterFactory
				.getInstance().createFKAuftragEingansrechnungen(
						auftragDto.getIId());
		if (auftragEingangsrechnung == null) {

			String[] aWhichButtonIUse = {};

			auftragEingangsrechnung = new PanelQuery(null, filtersRechnungen,
					QueryParameters.UC_ID_AUFTRAGEINGANGSRECHNUNGEN,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("er.modulname.tooltip"), true);

			splittbetragSumme = new JLabel();
			auftragEingangsrechnung.getToolBar().getToolsPanelCenter()
					.add(splittbetragSumme);
			this.setComponentAt(IDX_PANEL_EINGANGSRECHNUNGEN,
					auftragEingangsrechnung);
		}

		BigDecimal splittbetrag = DelegateFactory.getInstance()
				.getAuftragDelegate()
				.berechneSummeSplittbetrag(auftragDto.getIId());

		splittbetragSumme.setText(LPMain
				.getTextRespectUISPr("auft.eingangsrechnungen.summe")
				+ " = "
				+ Helper.formatZahl(splittbetrag, 2, LPMain.getTheClient()
						.getLocUi()));

		auftragEingangsrechnung.setDefaultFilter(filtersRechnungen);
	}

	public void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAuftragauswahl = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), true, false, null);

		new DialogQuery(panelQueryFLRAuftragauswahl);
	}

	public void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAngebotauswahl = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebotErledigteVersteckt(getInternalFrame(),
						true, false); // pro Angebot 1 Auftrag -> FK offene
		// Angebote eines Mandanten

		new DialogQuery(panelQueryFLRAngebotauswahl);
	}

	public void dialogQueryAngebotFuerRahmenauftragFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRAngebotauswahlFuerRahmenauftrag = AngebotFilterFactory
				.getInstance().createPanelFLRAngebotErledigteVersteckt(
						getInternalFrame(), true, false); // pro Angebot 1
															// Auftrag -> FK
															// offene
		// Angebote eines Mandanten

		new DialogQuery(panelQueryFLRAngebotauswahlFuerRahmenauftrag);
	}

	/**
	 * AGILPRO CHANGES Changed visiblity from protected to public
	 * 
	 * @author Lukas Lisowski
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		iIdAuftrag = auftragDto.getIId();
		// dtos hinterlegen
		initializeDtos(iIdAuftrag);

		int selectedIndex = getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUFTRAGAUSWAHL) {
			setTitleAuftrag(LPMain
					.getTextRespectUISPr("auft.title.panel.auswahl"));
			refreshAuftragAuswahl();
			auftragAuswahl.eventYouAreSelected(false);
			auftragAuswahl.updateButtons();
			// SK Damit nach Auswahl der Auftragliste die Meldung des Sicht
			// Lieferstatus wieder kommt
			if (auftragSichtLieferstatus != null) {
				auftragSichtLieferstatus = null;
			}

			// emptytable: 0 wenn es fuer das Default TabbedPane noch keinen
			// Eintrag gibt, die
			// restlichen Panels deaktivieren, die Grunddaten bleiben erreichbar
			if (getAuftragAuswahl().getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUFTRAGAUSWAHL, false);
			}
		} else if (selectedIndex == IDX_PANEL_AUFTRAGKOPFDATEN) {

			refreshAuftragKopfdaten();
			auftragKopfdaten.eventYouAreSelected(false); // sonst werden die
			// buttons nicht
			// richtig gesetzt!

		} else if (selectedIndex == IDX_PANEL_AUFTRAGPOSITIONEN) {

			refreshAuftragPositionen();
			auftragPositionen.eventYouAreSelected(false);
			auftragPositionenTop.updateButtons(auftragPositionenBottom
					.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_AUFTRAGKONDITIONEN) {

			refreshAuftragKonditionen();
			auftragKonditionen.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_AUFTRAGTEILNEHMER) {

			refreshAuftragTeilnehmer();

			// pqaddbutton: 2 Nachdem das PanelSplit erzeugt wurde, muss
			// eventYouAreSelected
			// auf jeden Fall noch einmal aufgerufen werden, wegen dem Status
			// der zusaetzlichen Buttons
			auftragTeilnehmer.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			auftragTeilnehmerTop.updateButtons(auftragTeilnehmerBottom
					.getLockedstateDetailMainKey());

		} else if (selectedIndex == IDX_PANEL_ZEITPLAN) {

			refreshZeitplan();
			auftragZeitplan.eventYouAreSelected(false);
			auftragZeitplanTop.updateButtons(auftragZeitplanBottom
					.getLockedstateDetailMainKey());

		} else if (selectedIndex == IDX_PANEL_ZAHLUNGSPLAN) {

			refreshZahlungsplan();
			auftragZahlungsplan.eventYouAreSelected(false);
			auftragZahlungsplanTop.updateButtons(auftragZahlungsplanBottom
					.getLockedstateDetailMainKey());

		} else if (selectedIndex == IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN) {
			refreshZahlungsplan();

			auftragZahlungsplanTop.eventYouAreSelected(false);
			auftragZahlungsplanBottom
					.setKeyWhenDetailPanel(auftragZahlungsplanTop
							.getSelectedId());
			auftragZahlungsplanBottom.eventYouAreSelected(false);

			if (auftragZahlungsplanTop.getSelectedId() != null) {
				refreshZahlungsplanmeilenstein();
				auftragZahlungsplanmeilenstein.eventYouAreSelected(false);
				auftragZahlungsplanmeilensteinTop
						.updateButtons(auftragZahlungsplanmeilensteinBottom
								.getLockedstateDetailMainKey());
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein.keinzahlungsplanausgewaehlt"));

				setSelectedIndex(IDX_PANEL_ZAHLUNGSPLAN);
				lPEventObjectChanged(e);
			}

		} else if (selectedIndex == IDX_PANEL_SICHTLIEFERSTATUS) {

			setTitleAuftrag(LPMain
					.getTextRespectUISPr("auft.title.panel.sichtlieferstatus"));
			refreshSichtLieferstatus();
			auftragSichtLieferstatus.eventYouAreSelected(false);
			auftragSichtLieferstatus.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN) {

			// Nur wenn in Sicht Lieferstatus bereits ein Lieferschein
			// selektiert wurde
			if (auftragSichtLieferstatus != null
					&& auftragSichtLieferstatus.getSelectedId() != null) {
				// die anzeigte Liste in diesem Panel ist abhaengig von der
				// momentan
				// selektierten Zeile im Panel auftragSichtLieferstatus
				refreshSichtLieferstatus(); // falls das Panel noch nicht
				// exitistiert

				// kein auftragSichtLieferstatus.eventYouAreSelected(false)!

				int iSelektierteZeile = auftragSichtLieferstatus.getTable()
						.getSelectedRow();

				if (iSelektierteZeile > -1) {
					// in der selektierten Zeile ist column 0 die iIdPosition
					// und column 1
					// die Belegart @todo etwas weniger hardcoded ... PJ 5170
					Integer iIdPosition = (Integer) auftragSichtLieferstatus
							.getTable().getValueAt(iSelektierteZeile, 0);
					String sBelegartkuerzel = (String) auftragSichtLieferstatus
							.getTable().getValueAt(iSelektierteZeile, 1);

					if (sBelegartkuerzel.equals(DelegateFactory.getInstance()
							.getLocaleDelegate()
							.belegartFindByCNr(LocaleFac.BELEGART_AUFTRAG)
							.getCKurzbezeichnung())) {
						refreshSichtAuftragAufAndereBelegarten(iIdPosition);
						sichtAuftragAufAndereBelegarten
								.eventYouAreSelected(false);

						// im QP die Buttons setzen.
						sichtAuftragAufAndereBelegartenTop
								.updateButtons(sichtAuftragAufAndereBelegartenBottom
										.getLockedstateDetailMainKey());
					} else {
						// bei Lieferscheinposition kann dieses Panel nicht
						// angewaehlt werden
						// -> Default ist eine entsprechende Meldung und der
						// Wechsel auf das logische
						// Vorgaenger Panel SichtLieferstatus
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hint"),
										LPMain.getTextRespectUISPr("auft.hint.auftragpositionwaehlen"));

						refreshSichtLieferstatus();
						setSelectedComponent(auftragSichtLieferstatus);
					}
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("auft.hint.keinegeliefertenpositionen"));
					setSelectedComponent(auftragSichtLieferstatus);
				}
			} else {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.hint"), LPMain
						.getTextRespectUISPr("auft.hint.sichtlieferschein"));
				refreshSichtLieferstatus();
				setSelectedComponent(auftragSichtLieferstatus);
			}

		} else if (selectedIndex == IDX_PANEL_SICHTRAHMEN) {

			setTitleAuftrag(LPMain.getTextRespectUISPr("lp.sichtrahmen"));
			refreshAuftragSichtRahmen();
			auftragSichtRahmen.eventYouAreSelected(false);
			auftragSichtRahmenTop.updateButtons(auftragSichtRahmenBottom
					.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_AUFTRAGUEBERSICHT) {

			if (!bKriterienAuftragUebersichtUeberMenueAufgerufen) {
				getKriterienAuftragUebersicht();
				getInternalFrame()
						.showPanelDialog(pdKriterienAuftragUebersicht);
			}
		} else if (selectedIndex == IDX_PANEL_AUFTRAGZEITEN) {

			if (!pdKriterienAuftragzeitenUeberMenueAufgerufen) {
				getKriterienAuftragzeiten();
				getInternalFrame().showPanelDialog(pdKriterienAuftragzeiten);
			}

		} else if (selectedIndex == IDX_PANEL_LSRE) {

			getPanelTabelleLoszeiten().eventYouAreSelected(false);

			setSelectedComponent(auftragLSRE);

			auftragLSRE.updateButtons(new LockStateValue(null, null,
					PanelBasis.LOCK_IS_NOT_LOCKED));
		} else if (selectedIndex == IDX_PANEL_LOSE) {

			refreshLose();
			auftragLose.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_BESTELLUNGEN) {

			refreshBestellungen();
			auftragBestellungen.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_EINGANGSRECHNUNGEN) {

			refreshEingangsrechnungen();
			auftragEingangsrechnung.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_AUFTRAGEIGENSCHAFTEN) {

			refreshEigenschaften(getAuftragDto().getIId());
			panelDetailAuftragseigenschaft.eventYouAreSelected(false);
			if (getAuftragDto() != null) {
				LPButtonAction item = null;
				if (getAuftragDto().getStatusCNr() != null) {
					if (getAuftragDto().getStatusCNr().equals(
							LocaleFac.STATUS_ERLEDIGT)) {
						item = (LPButtonAction) panelDetailAuftragseigenschaft
								.getHmOfButtons().get(PanelBasis.ACTION_UPDATE);
						item.getButton().setEnabled(false);
					}
				}
			}

		}

		enablePanelsNachBitmuster();
		setTitleAuftrag("");
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRBegruendung) {

				DelegateFactory
						.getInstance()
						.getAuftragDelegate()
						.updateAuftragBegruendung(getAuftragDto().getIId(),
								null);
				if (auftragKonditionen != null) {
					auftragKonditionen.eventYouAreSelected(false);
				}

			}

		}

		else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			handleItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_ESCAPE) {
			// goto PanelQuery
			// if (e.getSource() == auftragPositionen) {
			// auftragPositionenBottom.isl
			setSelectedComponent(getAuftragAuswahl());
			// }
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == auftragPositionenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragPositionenTop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragTeilnehmerBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragTeilnehmerTop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragZeitplanBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragZeitplanTop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragZahlungsplanBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragZahlungsplanTop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragZahlungsplanmeilensteinBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragZahlungsplanmeilensteinTop
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == sichtAuftragAufAndereBelegartenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				sichtAuftragAufAndereBelegartenTop
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == auftragSichtRahmenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragSichtRahmenTop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelAuftragsnrnrnBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAuftragsnrnrnTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else
		// eine Zeile in einer Tabelle, bei der ich Listener bin, wurde
		// doppelgeklickt
		if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			setTitleAuftrag("");
			// usemenuebar: Hier super.eventItemchanged(e); aufrufen, damit die
			// Menuebar, wenn auf ein "unteres Ohrwaschl"
			// geklickt wird, angezeigt wird.
			super.lPEventItemChanged(e);
		}

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			handleGotoDetailPanel(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			// pqaddbutton: 3 Es wurde der spezielle Neu Button gedrueckt
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.equals(MY_OWN_NEW_NEU_EXTERNER_TEILNEHMER)) {
				if (e.getSource() == auftragTeilnehmerTop) {
					auftragTeilnehmerBottom.eventActionNew(e, true, false);
					auftragTeilnehmerBottom.eventYouAreSelected(false);
					setSelectedComponent(auftragTeilnehmer); // ui
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_NEU_AUS_ANGEBOT)) {
				if (e.getSource() == auftragAuswahl) {
					dialogQueryAngebotFromListe(null);
				}
			} else if (sAspectInfo
					.equals(MY_OWN_NEW_RAHMENAUFTRAG_NEU_AUS_ANGEBOT)) {
				if (e.getSource() == auftragAuswahl) {
					dialogQueryAngebotFuerRahmenauftragFromListe(null);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_NEU_AUS_AUFTRAG)) {
				if (e.getSource() == auftragAuswahl) {
					dialogQueryAuftragFromListe(null);
				}
			} else if (sAspectInfo
					.equals(MY_OWN_NEW_NEU_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN)) {
				if (getAuftragDto() != null && getAuftragDto().getIId() != null) {
					AuftragpositionDto[] aufposDtos = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.auftragpositionFindByAuftrag(
									getAuftragDto().getIId());

					if (aufposDtos != null && aufposDtos.length > 0
							&& aufposDtos[0].getArtikelIId() != null) {
						panelDialogAuftragArtikelSchnellanlageArtikelAendern = new PanelDialogAuftragArtikelSchnellanlage(
								getInternalFrame(),
								aufposDtos[0].getArtikelIId());
						getInternalFrame()
								.showPanelDialog(
										panelDialogAuftragArtikelSchnellanlageArtikelAendern);
					}

				}

			} else if (sAspectInfo.equals(MY_OWN_NEW_NEU_AUS_SCHNELLANLAGE)) {
				// In KopfdatenWechseln
				// um mit Auftraegen arbeiten zu koennen, muss das Hauptlager
				// des Mandanten definiert sein
				DelegateFactory.getInstance().getLagerDelegate()
						.getHauptlagerDesMandanten();

				// emptytable: 1 wenn es bisher keinen eintrag gibt, die
				// restlichen
				// panels aktivieren
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUFTRAGAUSWAHL, true);
				}

				refreshAuftragKopfdaten().eventActionNew(e, true, false);
				auftragKopfdaten.setSchnellanlage(true);
				setSelectedComponent(auftragKopfdaten);
			} else if (sAspectInfo.equals(MY_OWN_NEW_TOGGLE_VERRECHENBAR)) {
				if (e.getSource() == auftragAuswahl && getAuftragDto() != null
						&& getAuftragDto().getIId() != null) {
					DelegateFactory.getInstance().getAuftragDelegate()
							.toggleVerrechenbar(getAuftragDto().getIId());
					auftragAuswahl.eventYouAreSelected(false);
				}
			} else if (sAspectInfo
					.equals(MY_OWN_NEW_LIEFERSCHEIN_PREISE_UPDATEN)) {
				if (e.getSource() == auftragPositionenTop) {
					DelegateFactory
							.getInstance()
							.getLieferscheinpositionDelegate()
							.preiseAusAuftragspositionenUebernehmen(
									getAuftragDto().getIId());
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_ABRUF_ZU_RAHMEN)) {
				if (e.getSource() == auftragAuswahl) {
					// ein Abrufauftrag kann nur aus einem Rahmenauftrag
					// generiert werden
					if (pruefeAktuellenAuftrag()) {
						if (getAuftragDto().getAuftragartCNr().equals(
								AuftragServiceFac.AUFTRAGART_RAHMEN)) {
							if (getAuftragDto().getStatusCNr().equals(
									AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
									|| getAuftragDto()
											.getStatusCNr()
											.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
								refreshAuftragKopfdaten();

								// jetzt dafuer sorgen, dass die Buttons richtig
								// geschalten werden
								getInternalFrame().setKeyWasForLockMe(
										LPMain.getLockMeForNew());

								// ZUERST wechseln, denn hier wird die aktuelle
								// Bestellung neu festgelegt
								setSelectedComponent(auftragKopfdaten);

								auftragKopfdaten.eventActionNew(e, true, false);

								// an dieser Stelle sind die Panels nach
								// Bitmuster geschalten
								getInternalFrame()
										.enableAllOberePanelsExceptMe(this,
												IDX_PANEL_AUFTRAGKOPFDATEN,
												false);
							} else {
								showStatusMessage("lp.hint",
										"auft.keinabrufzudiesemrahmen");
							}
						} else {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("auft.rahmenauftragwaehlen"));
						}
					}
				}
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				if (istAktualisierenAuftragErlaubt()) {
					// copypaste
					einfuegenHV();
				}
			}else if (sAspectInfo
					.endsWith(ACTION_SPECIAL_PREISE_NEU_KALKULIEREN)) {

				if (getAuftragDto().getStatusCNr().equals(
						AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

					//
					PanelPositionenArtikelVerkauf panelBottom = ((PanelPositionenArtikelVerkauf) auftragPositionenBottom.panelArtikel);

					AuftragpositionDto[] dtos = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.auftragpositionFindByAuftrag(
									getAuftragDto().getIId());

					for (int i = 0; i < dtos.length; i++) {
						if (dtos[i].getPositionsartCNr().equals(
								AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
							auftragPositionenTop
									.setSelectedId(dtos[i].getIId());
							auftragPositionenTop.eventYouAreSelected(false);
							panelBottom.setKeyWhenDetailPanel(dtos[i].getIId());
							panelBottom.update();
							panelBottom.berechneVerkaufspreis(false);
							auftragPositionenBottom
									.eventActionSave(null, false);
						}
					}
				} else {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr(
									"auft.error.neuberechnen"));
				}

			} else if (sAspectInfo.equals(BUTTON_IMPORTCSV_AUFTRAGPOSITIONEN)) {
				importCSVAuftragPositionen();
				auftragPositionen.eventYouAreSelected(false); // refresh
			} else if (sAspectInfo
					.equals(BUTTON_SCHNELLERFASSUNG_AUFTRAGPOSITIONEN)) {

				if (istAktualisierenAuftragErlaubt()) {

					DialogAuftragspositionenSchnellerfassung d = new DialogAuftragspositionenSchnellerfassung(
							auftragPositionen, getAuftragDto());
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				}
				auftragPositionen.eventYouAreSelected(false); // refresh
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_NEU_AUS_PROJEKT)) {
				dialogQueryProjektFromListe(null);
			} else if (sAspectInfo.equals(MENUE_ACTION_ETIKETT_DRUCKEN)) {
				String auftragsnummer = (String) JOptionPane.showInputDialog(
						this,
						LPMain.getTextRespectUISPr("label.auftragnummer"),
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.QUESTION_MESSAGE);

				if (auftragsnummer != null && auftragsnummer.length() > 1
						&& auftragsnummer.startsWith("$A")) {
					auftragsnummer = auftragsnummer.substring(2);
					AuftragpositionDto auftragposDto = null;
					// Zuerst auftrag auswaehlen und erste Position holen
					AuftragDto auftragDto = null;
					try {
						auftragDto = DelegateFactory
								.getInstance()
								.getAuftragDelegate()
								.auftragFindByMandantCNrCNr(
										LPMain.getTheClient().getMandant(),
										auftragsnummer);
					} catch (ExceptionLP ex) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hint"),
										LPMain.getTextRespectUISPr("auft.auftragnichtvorhanden"));
						return;
					}
					try {
						Integer iMinISort = DelegateFactory.getInstance()
								.getAuftragpositionDelegate()
								.getMinISort(auftragDto.getIId());
						auftragposDto = DelegateFactory
								.getInstance()
								.getAuftragpositionDelegate()
								.auftragpositionFindByAuftragISort(
										auftragDto.getIId(), iMinISort);
					} catch (ExceptionLP ex) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hint"),
										LPMain.getTextRespectUISPr("auft.hatkeinepositionen"));
						return;
					}
					// Die erste Position muss von der Art Position sein
					if (AuftragServiceFac.AUFTRAGPOSITIONART_POSITION
							.equals(auftragposDto.getPositionsartCNr())) {
						// Wenn die Position korrekt war die naechste Pos holen
						// und drucken
						try {
							auftragposDto = DelegateFactory
									.getInstance()
									.getAuftragpositionDelegate()
									.auftragpositionFindByAuftragISort(
											auftragposDto.getBelegIId(),
											auftragposDto.getISort() + 1);
						} catch (ExceptionLP ex) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("auft.keinepositionzudrucken"));
							return;
						}
						if (auftragposDto != null) {
							ReportAuftragSrnEtikett reportEtikett = new ReportAuftragSrnEtikett(
									getInternalFrame(),
									auftragposDto.getBelegIId(),
									auftragposDto.getIId(),
									auftragposDto.getArtikelIId(), "");
							getInternalFrame().showReportKriterienZweiDrucker(
									reportEtikett, null, null, false, false,
									false, true);
							reportEtikett.eventYouAreSelected(false);
						}
					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hint"),
										LPMain.getTextRespectUISPr("auft.falschepositionsart"));
						return;
					}
				} else {
					if (auftragsnummer != null) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								"Keine g\u00FCltige Autragsnummer "
										+ auftragsnummer);
						return;
					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == auftragAuswahl) {
				// um mit Auftraegen arbeiten zu koennen, muss das Hauptlager
				// des Mandanten definiert sein
				DelegateFactory.getInstance().getLagerDelegate()
						.getHauptlagerDesMandanten();

				// emptytable: 1 wenn es bisher keinen eintrag gibt, die
				// restlichen
				// panels aktivieren
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUFTRAGAUSWAHL, true);
				}

				refreshAuftragKopfdaten().eventActionNew(e, true, false);
				auftragKopfdaten.setSchnellanlage(false);

				setSelectedComponent(auftragKopfdaten);
			} else if (e.getSource() == auftragPositionenTop) {
				// pqnewnotallowed: 0 das Anlegen einer neuen Position nur
				// ausloesen, wenn es erlaubt ist
				if (istAktualisierenAuftragErlaubt()) {
					auftragPositionenBottom.eventActionNew(e, true, false);
					auftragPositionenBottom.eventYouAreSelected(false);
					setSelectedComponent(auftragPositionen); // ui
					// VF IMS 2180
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUFTRAGPOSITIONEN, false);
				} else {
					auftragPositionen.eventYouAreSelected(false);
				}
			} else if (e.getSource() == auftragTeilnehmerTop) {
				auftragTeilnehmerBottom.eventActionNew(e, true, false);
				auftragTeilnehmerBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragTeilnehmer); // ui
			} else if (e.getSource() == auftragZeitplanTop) {
				auftragZeitplanBottom.eventActionNew(e, true, false);
				auftragZeitplanBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragZeitplan); // ui
			} else if (e.getSource() == auftragZahlungsplanTop) {
				auftragZahlungsplanBottom.eventActionNew(e, true, false);
				auftragZahlungsplanBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragZahlungsplan); // ui
			} else if (e.getSource() == auftragZahlungsplanmeilensteinTop) {
				auftragZahlungsplanmeilensteinBottom.eventActionNew(e, true,
						false);
				auftragZahlungsplanmeilensteinBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragZahlungsplanmeilenstein); // ui
			} else if (e.getSource() == panelAuftragsnrnrnTopQP) {
				panelAuftragsnrnrnBottomD.eventActionNew(e, true, false);
				panelAuftragsnrnrnBottomD.eventYouAreSelected(false);
			}
		} else

		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == auftragKopfdaten) {
				// aenderewaehrung: 6 die Kopfdaten muessen mit den
				// urspruenglichen Daten befuellt werden
				auftragKopfdaten.eventYouAreSelected(false);
				auftragKopfdaten.updateButtons(auftragKopfdaten
						.getLockedstateDetailMainKey());
			} else if (e.getSource() == auftragPositionenBottom) {
				auftragPositionen.eventYouAreSelected(false); // das 1:n Panel
				// neu aufbauen
			} else if (e.getSource() == auftragTeilnehmerBottom) {
				auftragTeilnehmer.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZeitplanBottom) {
				auftragZeitplan.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZahlungsplanBottom) {
				auftragZahlungsplan.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZahlungsplanmeilensteinBottom) {
				auftragZahlungsplanmeilenstein.eventYouAreSelected(false);
			} else if (e.getSource() == auftragSichtRahmenBottom) {
				auftragSichtRahmen.eventYouAreSelected(false);
			} else if (e.getSource() == panelAuftragsnrnrnBottomD) {
				panelAuftragsnrnrnSP.eventYouAreSelected(false);
			} else
				return;
			enablePanelsNachBitmuster();
		} else
		// Wir landen hier nach Button Save
		if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			handleActionSave(e);
		} else
		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == auftragPositionenBottom) {
				// btndiscard: 1 bei einem Neu im 1:n Panel wurde der
				// KeyForLockMe ueberschrieben
				setKeyWasForLockMe();
				if (auftragPositionenBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragPositionenTop
							.getId2SelectAfterDelete();
					auftragPositionenTop.setSelectedId(oNaechster);
				}
				auftragPositionen.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n
				// panel
			} else if (e.getSource() == auftragTeilnehmerBottom) {
				setKeyWasForLockMe();
				if (auftragTeilnehmerBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragTeilnehmerTop
							.getId2SelectAfterDelete();
					auftragTeilnehmerTop.setSelectedId(oNaechster);
				}
				auftragTeilnehmer.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZeitplanBottom) {
				setKeyWasForLockMe();
				if (auftragZeitplanBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragZeitplanTop
							.getId2SelectAfterDelete();
					auftragZeitplanTop.setSelectedId(oNaechster);
				}
				auftragZeitplan.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZahlungsplanBottom) {
				setKeyWasForLockMe();
				if (auftragZahlungsplanBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragZahlungsplanTop
							.getId2SelectAfterDelete();
					auftragZahlungsplanTop.setSelectedId(oNaechster);
				}
				auftragZahlungsplan.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZahlungsplanmeilensteinBottom) {
				setKeyWasForLockMe();
				if (auftragZahlungsplanmeilensteinBottom
						.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragZahlungsplanmeilensteinTop
							.getId2SelectAfterDelete();
					auftragZahlungsplanmeilensteinTop.setSelectedId(oNaechster);
				}
				auftragZahlungsplanmeilenstein.eventYouAreSelected(false);
			} else if (e.getSource() == auftragSichtRahmenBottom) {
				setKeyWasForLockMe();
				auftragSichtRahmen.eventYouAreSelected(false);
			} else if (e.getSource() == auftragKopfdaten) {
				// btndiscard: 2 die Liste neu laden, falls sich etwas geaendert
				// hat
				auftragAuswahl.eventYouAreSelected(false);

				// btndiscard: 3 nach einem Discard ist der aktuelle Key nicht
				// mehr gesetzt
				setKeyWasForLockMe();

				// btndiscard: 4 der Key der Kopfdaten steht noch auf new|...
				auftragKopfdaten.setKeyWhenDetailPanel(auftragAuswahl
						.getSelectedId());

				// btndiscard: 5 auf die Auswahl schalten
				setSelectedComponent(auftragAuswahl);

				// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
				// restlichen panel deaktivieren
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUFTRAGAUSWAHL, false);
				}

				auftragKopfdaten.updateButtons(auftragKopfdaten
						.getLockedstateDetailMainKey());
				enablePanelsNachBitmuster();
			}
		} else
		// der OK Button in einem PanelDialog wurde gedrueckt
		if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == pdKriterienAuftragUebersicht) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = pdKriterienAuftragUebersicht
						.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				refreshAuftragUebersicht();
				ptAuftragUebersicht.setDefaultFilter(aAlleKriterien);

				// die Uebersicht aktualisieren
				ptAuftragUebersicht.eventYouAreSelected(false);

				// man steht auf alle Faelle auf der Uebersicht
				setSelectedComponent(ptAuftragUebersicht);
				setTitleAuftragOhneAuftragnummer(LPMain
						.getTextRespectUISPr("lp.umsatzuebersicht"));
				bKriterienAuftragUebersichtUeberMenueAufgerufen = false;
				ptAuftragUebersicht.updateButtons(new LockStateValue(null,
						null, PanelBasis.LOCK_IS_NOT_LOCKED));
				enablePanelsNachBitmuster();
			} else if (e.getSource() == pdKriterienAuftragzeiten) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = pdKriterienAuftragzeiten
						.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				refreshAuftragzeiten();
				ptAuftragzeiten.setDefaultFilter(aAlleKriterien);

				// die Auftragzeiten aktualisieren
				ptAuftragzeiten.eventYouAreSelected(false);

				// man steht auf alle Faelle in den Auftragzeiten
				setSelectedComponent(ptAuftragzeiten);
				setTitleAuftrag(LPMain
						.getTextRespectUISPr("auft.title.panel.auftragzeiten"));
				pdKriterienAuftragzeitenUeberMenueAufgerufen = false;
				ptAuftragzeiten.updateButtons(new LockStateValue(null, null,
						PanelBasis.LOCK_IS_NOT_LOCKED));
				enablePanelsNachBitmuster();
			} else if (e.getSource() == panelDialogAuftragArtikelSchnellanlageArtikelAendern) {
				// Artikel updaten
				ArtikelDto aDto = panelDialogAuftragArtikelSchnellanlageArtikelAendern
						.getArtikelDtoVorhandenMitNeuenBezeichnungen();

				DelegateFactory.getInstance().getArtikelDelegate()
						.updateArtikel(aDto);

				PaneldatenDto[] paneldatenDtos = panelDialogAuftragArtikelSchnellanlageArtikelAendern
						.getPaneldatenDtos();

				for (int i = 0; i < paneldatenDtos.length; i++) {
					paneldatenDtos[i].setCKey(aDto.getIId() + "");
				}

				DelegateFactory.getInstance().getPanelDelegate()
						.createPaneldaten(paneldatenDtos);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == auftragPositionenTop) {
				if (getAuftragDto().getStatusCNr().equals(
						AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
					int iPos = auftragPositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die erste ist
					if (iPos > 0) {
						Integer iIdPosition = (Integer) auftragPositionenTop
								.getSelectedId();

						// Integer iIdPositionMinus1 = (Integer)
						// auftragPositionenTop
						// .getTable().getValueAt(iPos - 1, 0);
						//
						// DelegateFactory
						// .getInstance()
						// .getAuftragpositionDelegate()
						// .vertauscheAuftragpositionen(iIdPosition,
						// iIdPositionMinus1);

						TableModel tm = auftragPositionenTop.getTable()
								.getModel();
						DelegateFactory.getInstance()
								.getAuftragpositionDelegate()
								.vertauscheAuftragpositionMinus(iPos, tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						auftragPositionenTop.setSelectedId(iIdPosition);
					}
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("anf.error.positionenverschieben"));
				}
			} else

			if (e.getSource() == auftragTeilnehmerTop) {
				int iPos = auftragTeilnehmerTop.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) auftragTeilnehmerTop
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) auftragTeilnehmerTop
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getAuftragteilnehmerDelegate()
							.vertauscheAuftragteilnehmer(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					auftragTeilnehmerTop.setSelectedId(iIdPosition);
				}
			}
		} else

		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == auftragPositionenTop) {
				if (getAuftragDto().getStatusCNr().equals(
						AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
					int iPos = auftragPositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die letzte ist
					if (iPos < auftragPositionenTop.getTable().getRowCount() - 1) {
						Integer iIdPosition = (Integer) auftragPositionenTop
								.getSelectedId();

						// Integer iIdPositionPlus1 = (Integer)
						// auftragPositionenTop
						// .getTable().getValueAt(iPos + 1, 0);
						//
						// DelegateFactory
						// .getInstance()
						// .getAuftragpositionDelegate()
						// .vertauscheAuftragpositionen(iIdPosition,
						// iIdPositionPlus1);

						TableModel tm = auftragPositionenTop.getTable()
								.getModel();
						DelegateFactory.getInstance()
								.getAuftragpositionDelegate()
								.vertauscheAuftragpositionPlus(iPos, tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						auftragPositionenTop.setSelectedId(iIdPosition);
					}
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("anf.error.positionenverschieben"));
				}
			} else

			if (e.getSource() == auftragTeilnehmerTop) {
				int iPos = auftragTeilnehmerTop.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < auftragTeilnehmerTop.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) auftragTeilnehmerTop
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) auftragTeilnehmerTop
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getAuftragteilnehmerDelegate()
							.vertauscheAuftragteilnehmer(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					auftragTeilnehmerTop.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == auftragPositionenTop) {

				auftragPositionenBottom
						.setArtikeSetIIdForNewPosition(auftragPositionenBottom
								.getAuftragpositionDto()
								.getPositioniIdArtikelset());

				auftragPositionenBottom.eventActionNew(e, true, false);
				auftragPositionenBottom.eventYouAreSelected(false); // Buttons
				// schalten
			} else if (e.getSource() == auftragTeilnehmerTop) {
				auftragTeilnehmerBottom.eventActionNew(e, true, false);
				auftragTeilnehmerBottom.eventYouAreSelected(false); // Buttons
				// schalten
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}

	}

	public void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame());

		new DialogQuery(panelQueryFLRProjekt);
	}

	private void handleActionSave(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == auftragKopfdaten) {
			Object pkAuftrag = auftragKopfdaten.getKeyWhenDetailPanel();
			initializeDtos((Integer) pkAuftrag);
			getInternalFrame().setKeyWasForLockMe(pkAuftrag.toString());

			auftragAuswahl.clearDirektFilter();
			auftragAuswahl.eventYouAreSelected(false);
			auftragAuswahl.setSelectedId(pkAuftrag);
			auftragAuswahl.eventYouAreSelected(false);

			// wenn ein Abrufauftrag angelegt wurde und es noch keine Positionen
			// dazu gibt...
			if (getAuftragDto().getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_ABRUF)
					&& DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.getAnzahlMengenbehafteteAuftragpositionen(
									getAuftragDto().getIId()) == 0) {
				// showPanelDialogDivisor();
			}
		} else if (e.getSource() == auftragPositionenBottom) {
			// den Key des Datensatzes merken
			Object oKey = auftragPositionenBottom.getKeyWhenDetailPanel();

			// wenn der neue Datensatz wirklich angelegt wurde (Abbruch moeglich
			// durch Pruefung auf Unterpreisigkeit)
			if (!oKey.equals(LPMain.getLockMeForNew())) {
				// die Liste neu aufbauen
				auftragPositionenTop.eventYouAreSelected(false);

				// den Datensatz in der Liste selektieren
				auftragPositionenTop.setSelectedId(oKey);
			}

			// im Detail den selektierten anzeigen
			auftragPositionen.eventYouAreSelected(false);
		} else if (e.getSource() == auftragTeilnehmerBottom) {
			Object oKey = auftragTeilnehmerBottom.getKeyWhenDetailPanel();
			auftragTeilnehmerTop.eventYouAreSelected(false);
			auftragTeilnehmerTop.setSelectedId(oKey);
			auftragTeilnehmer.eventYouAreSelected(false);
		} else if (e.getSource() == auftragZeitplanBottom) {
			Object oKey = auftragZeitplanBottom.getKeyWhenDetailPanel();
			auftragZeitplanTop.eventYouAreSelected(false);
			auftragZeitplanTop.setSelectedId(oKey);
			auftragZeitplan.eventYouAreSelected(false);
		} else if (e.getSource() == auftragZahlungsplanBottom) {
			Object oKey = auftragZahlungsplanBottom.getKeyWhenDetailPanel();
			auftragZahlungsplanTop.eventYouAreSelected(false);
			auftragZahlungsplanTop.setSelectedId(oKey);
			auftragZahlungsplan.eventYouAreSelected(false);
		} else if (e.getSource() == auftragZahlungsplanmeilensteinBottom) {
			Object oKey = auftragZahlungsplanmeilensteinBottom
					.getKeyWhenDetailPanel();
			auftragZahlungsplanmeilensteinTop.eventYouAreSelected(false);
			auftragZahlungsplanmeilensteinTop.setSelectedId(oKey);
			auftragZahlungsplanmeilenstein.eventYouAreSelected(false);
		} else if (e.getSource() == auftragSichtRahmenBottom) {
			auftragSichtRahmen.eventYouAreSelected(false);

			// wenn die letzte Position uebernommen wurde, Wechsel auf
			// Positionen
			if (auftragSichtRahmenBottom.getKeyWhenDetailPanel() == null) {
				getInternalFrame().enableAllPanelsExcept(true);
				refreshAuftragPositionen();
				setSelectedComponent(auftragPositionen);
			}
		} else if (e.getSource() == panelAuftragsnrnrnBottomD) {
			Object oKey = panelAuftragsnrnrnBottomD.getKeyWhenDetailPanel();
			panelAuftragsnrnrnTopQP.eventYouAreSelected(false);
			panelAuftragsnrnrnTopQP.setSelectedId(oKey);
			panelAuftragsnrnrnSP.eventYouAreSelected(false);
		}

	}

	public void erstelleAuftragAusProjekt(Integer projektIId) throws Throwable {

		ItemChangedEvent e = new ItemChangedEvent(this, -99);

		getAuftragAuswahl().eventActionNew(e, true, false);
		auftragKopfdaten.eventYouAreSelected(false);
		// Nun noch Kunde/Ansprechpartner/Vertreter/Projekt/ProjektBezeichnung
		// setzen
		refreshAuftragKopfdaten().setDefaultsAusProjekt(projektIId);
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
		if (e.getSource() == auftragAuswahl) {
			Integer auftragIId = (Integer) auftragAuswahl.getSelectedId();
			initializeDtos(auftragIId); // befuellt den Auftrag und den Kunden
			getInternalFrame().setKeyWasForLockMe(auftragIId + "");

			if (auftragIId != null) {
				setSelectedComponent(refreshAuftragPositionen()); // auf die
				// Positionen
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRProjekt) {
			Integer projektIId = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();

			if (projektIId != null) {
				erstelleAuftragAusProjekt(projektIId);
			}
		} else if (e.getSource() == panelQueryFLRAuftragauswahl) {
			Integer iIdAuftragBasis = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();

			if (iIdAuftragBasis != null) {
				Integer auftragIId = DelegateFactory
						.getInstance()
						.getAuftragDelegate()
						.erzeugeAuftragAusAuftrag(iIdAuftragBasis,
								getInternalFrame());

				initializeDtos(auftragIId); // befuellt den Auftrag und den
				// Kunden
				getInternalFrame().setKeyWasForLockMe(auftragIId + "");
				auftragAuswahl.setSelectedId(auftragIId);

				// wenn es bisher keinen Auftrag gegeben hat
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUFTRAGAUSWAHL, false);
				}

				setSelectedComponent(refreshAuftragPositionen()); // auf die
				// Positionen
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRAngebotauswahl) {
			Integer iIdAngebotBasis = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();

			if (iIdAngebotBasis != null) {

				boolean bMitZeitDaten = false;

				boolean bZeitdatenVorhanden = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.sindBelegzeitenVorhanden(LocaleFac.BELEGART_ANGEBOT,
								iIdAngebotBasis);

				if (bZeitdatenVorhanden == true) {
					bMitZeitDaten = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("auft.hint.zeitdatenvonangebot")
											+ "?", LPMain
											.getTextRespectUISPr("lp.frage"));
				}

				Integer auftragIId = DelegateFactory
						.getInstance()
						.getAngebotDelegate()
						.erzeugeAuftragAusAngebot(iIdAngebotBasis,
								bMitZeitDaten, false, getInternalFrame());

				// MB 080.05.06 IMS 1964
				auftragKopfdaten.setKeyWhenDetailPanel(auftragIId);

				initializeDtos(auftragIId); // befuellt den Auftrag und den
				// Kunden
				getInternalFrame().setKeyWasForLockMe(auftragIId + "");
				auftragAuswahl.setSelectedId(auftragIId);

				// wenn es bisher keinen Auftrag gegeben hat
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUFTRAGAUSWAHL, false);
				}

				setSelectedComponent(refreshAuftragKopfdaten()); // auf die
				// Kopfdaten
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRAngebotauswahlFuerRahmenauftrag) {
			Integer iIdAngebotBasis = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();

			if (iIdAngebotBasis != null) {

				boolean bMitZeitDaten = false;

				boolean bZeitdatenVorhanden = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.sindBelegzeitenVorhanden(LocaleFac.BELEGART_ANGEBOT,
								iIdAngebotBasis);

				if (bZeitdatenVorhanden == true) {
					bMitZeitDaten = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("auft.hint.zeitdatenvonangebot")
											+ "?", LPMain
											.getTextRespectUISPr("lp.frage"));
				}

				Integer auftragIId = DelegateFactory
						.getInstance()
						.getAngebotDelegate()
						.erzeugeAuftragAusAngebot(iIdAngebotBasis,
								bMitZeitDaten, true, getInternalFrame());
				auftragKopfdaten.setKeyWhenDetailPanel(auftragIId);

				initializeDtos(auftragIId); // befuellt den Auftrag und den
				// Kunden
				getInternalFrame().setKeyWasForLockMe(auftragIId + "");
				auftragAuswahl.setSelectedId(auftragIId);

				// wenn es bisher keinen Auftrag gegeben hat
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUFTRAGAUSWAHL, false);
				}

				setSelectedComponent(refreshAuftragKopfdaten()); // auf die
				// Kopfdaten
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRBegruendung) {

			if (e.getSource() == panelQueryFLRBegruendung) {
				Integer key = (Integer) panelQueryFLRBegruendung
						.getSelectedId();
				DelegateFactory
						.getInstance()
						.getAuftragDelegate()
						.updateAuftragBegruendung(getAuftragDto().getIId(), key);
				if (auftragKonditionen != null) {
					auftragKonditionen.eventYouAreSelected(false);
				}
			}
		}
	}

	/**
	 * Ein ItemChangedEvent mit code ITEM_CHANGED ist eingelangt.
	 * 
	 * @param e
	 *            das ItemChangedEvent
	 * @throws Throwable
	 */
	private void handleItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == auftragAuswahl) {
			Integer iIdAuftrag = (Integer) auftragAuswahl.getSelectedId();
			initializeDtos(iIdAuftrag);
			// updateMenuItemDateiAuftragVersand() ;

			setKeyWasForLockMe();
			refreshAuftragKopfdaten().setKeyWhenDetailPanel(iIdAuftrag);

			setTitleAuftrag(LPMain
					.getTextRespectUISPr("auft.title.panel.auswahl"));

			enablePanelsNachBitmuster();

			// fuer die leere Tabelle
			if (iIdAuftrag == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUFTRAGAUSWAHL, false);
			}
		} else if (e.getSource() == auftragPositionenTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragPositionenBottom.setKeyWhenDetailPanel(key);
			auftragPositionenBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragPositionenTop.updateButtons(auftragPositionenBottom
					.getLockedstateDetailMainKey());
		} else if (e.getSource() == auftragTeilnehmerTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragTeilnehmerBottom.setKeyWhenDetailPanel(key);
			auftragTeilnehmerBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragTeilnehmerTop.updateButtons();
		} else if (e.getSource() == auftragZeitplanTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragZeitplanBottom.setKeyWhenDetailPanel(key);
			auftragZeitplanBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragZeitplanTop.updateButtons();
		} else if (e.getSource() == auftragZahlungsplanTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragZahlungsplanBottom.setKeyWhenDetailPanel(key);
			auftragZahlungsplanBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragZahlungsplanTop.updateButtons();
		} else if (e.getSource() == auftragZahlungsplanmeilensteinTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragZahlungsplanmeilensteinBottom.setKeyWhenDetailPanel(key);
			auftragZahlungsplanmeilensteinBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragZahlungsplanmeilensteinTop.updateButtons();
		} else if (e.getSource() == sichtAuftragAufAndereBelegartenTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected(); // key
			// der
			// Lieferscheinposition
			// !
			sichtAuftragAufAndereBelegartenBottom.setKeyWhenDetailPanel(key);
			sichtAuftragAufAndereBelegartenBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			sichtAuftragAufAndereBelegartenTop.updateButtons();
		} else if (e.getSource() == auftragSichtRahmenTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragSichtRahmenBottom.setKeyWhenDetailPanel(key);
			auftragSichtRahmenBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragSichtRahmenTop.updateButtons();
		} else if (e.getSource() == panelAuftragsnrnrnTopQP) {
			Integer iId = (Integer) panelAuftragsnrnrnTopQP.getSelectedId();
			// getInternalFrame().setKeyWasForLockMe(iId + "");
			panelAuftragsnrnrnBottomD.setKeyWhenDetailPanel(iId);
			panelAuftragsnrnrnBottomD.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			panelAuftragsnrnrnTopQP.updateButtons(panelAuftragsnrnrnBottomD
					.getLockedstateDetailMainKey());
		}

	}

	@Override
	protected void initDtos() throws Throwable {
		initializeDtos(auftragDto.getIId());
	}

	public void initializeDtos(Integer iIdAuftragI) throws Throwable {
		if (iIdAuftragI != null) {
			auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(iIdAuftragI);

			kundeAuftragDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							auftragDto.getKundeIIdAuftragsadresse());

			// belegartkonditionen: 2a Kopftext und Fusstext hinterlegen
			if (auftragDto.getAuftragIIdKopftext() == null
					&& auftragDto.getAuftragIIdFusstext() == null) {
				initAuftragtexte();
			}

			if (auftragDto.getAuftragIIdKopftext() != null) {
				kopftextDto = DelegateFactory
						.getInstance()
						.getAuftragServiceDelegate()
						.auftragtextFindByPrimaryKey(
								auftragDto.getAuftragIIdKopftext());
			}

			if (auftragDto.getAuftragIIdFusstext() != null) {
				fusstextDto = DelegateFactory
						.getInstance()
						.getAuftragServiceDelegate()
						.auftragtextFindByPrimaryKey(
								auftragDto.getAuftragIIdFusstext());
			}
		}
	}

	private PanelSplit refreshAuftragSichtRahmen() throws Throwable {
		QueryType[] qtPos = null;
		FilterKriterium[] filtersPos = null;

		if (getAuftragDto() != null) {
			if (getAuftragDto().getAuftragartCNr() != null
					&& getAuftragDto().getAuftragartCNr().equals(
							AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				filtersPos = AuftragFilterFactory.getInstance()
						.createFKAuftragpositionSichtRahmen(
								getAuftragDto().getIId());
			} else if (getAuftragDto().getAuftragartCNr() != null
					&& getAuftragDto().getAuftragartCNr().equals(
							AuftragServiceFac.AUFTRAGART_ABRUF)) {
				filtersPos = AuftragFilterFactory.getInstance()
						.createFKAuftragpositionSichtRahmenAusAbruf(
								getAuftragDto().getAuftragIIdRahmenauftrag());
			}
		}

		if (auftragSichtRahmen == null) {
			auftragSichtRahmenBottom = new PanelAuftragpositionSichtRahmen(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.sichtrahmen"), null);

			auftragSichtRahmenTop = new PanelQuery(qtPos, filtersPos,
					QueryParameters.UC_ID_AUFTRAGPOSITIONSICHTRAHMEN, null,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.sichtrahmen"), true);

			auftragSichtRahmen = new PanelSplit(getInternalFrame(),
					auftragSichtRahmenBottom, auftragSichtRahmenTop, 200);

			setComponentAt(IDX_PANEL_SICHTRAHMEN, auftragSichtRahmen);
		}

		auftragSichtRahmenTop.setDefaultFilter(filtersPos);

		return auftragSichtRahmen;
	}

	private PanelSplit refreshAuftragPositionen() throws Throwable {
		QueryType[] qtPositionen = null;
		FilterKriterium[] filtersPositionen = AuftragFilterFactory
				.getInstance().createFKFlrauftragiid(auftragDto.getIId());

		if (auftragPositionen == null) {

			auftragPositionenBottom = new PanelAuftragPositionen2(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.positionen"),
					null); // emptytable:
			// 2
			// eventuell
			// gibt es
			// noch
			// keine
			// position

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			auftragPositionenTop = new PanelQuery(qtPositionen,
					filtersPositionen, QueryParameters.UC_ID_AUFTRAGPOSITION,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.positionen"),
					true); // flag,
			// damit flr
			// erst beim
			// aufruf
			// des
			// panels
			// loslaeuft

			auftragPositionenTop
					.createAndSaveAndShowButton(
							"/com/lp/client/res/selection_replace.png",
							LPMain.getTextRespectUISPr("auft.lieferscheinpreise.update"),
							MY_OWN_NEW_LIEFERSCHEIN_PREISE_UPDATEN,
							RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragPositionenTop.createAndSaveAndShowButton(
					"/com/lp/client/res/document_into.png",
					LPMain.getTextRespectUISPr("stkl.positionen.cvsimport"),
					BUTTON_IMPORTCSV_AUFTRAGPOSITIONEN, null);

			auftragPositionenTop
					.createAndSaveAndShowButton(
							"/com/lp/client/res/scanner16x16.png",
							LPMain.getTextRespectUISPr("auftrag.positionen.schnelleingabe"),
							BUTTON_SCHNELLERFASSUNG_AUFTRAGPOSITIONEN,
							RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragPositionenTop.createAndSaveAndShowButton(
					"/com/lp/client/res/calculator16x16.png",
					LPMain.getTextRespectUISPr("auft.preise.neuberechnen"),
					ACTION_SPECIAL_PREISE_NEU_KALKULIEREN,
					RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragPositionen = new PanelSplit(getInternalFrame(),
					auftragPositionenBottom, auftragPositionenTop, 160);

			auftragPositionenTop.setMultipleRowSelectionEnabled(true);

			setComponentAt(IDX_PANEL_AUFTRAGPOSITIONEN, auftragPositionen);
		}

		auftragPositionenTop.setDefaultFilter(filtersPositionen);

		return auftragPositionen;
	}

	private PanelAuftragKopfdaten refreshAuftragKopfdaten() throws Throwable {
		Integer iIdAuftrag = null;

		if (auftragKopfdaten == null) {
			// typkeyfordetail: Der Auftrag hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAuftrag = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			auftragKopfdaten = new PanelAuftragKopfdaten(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.kopfdaten"),
					iIdAuftrag); // empty
			// bei
			// leerer
			// Liste

			setComponentAt(IDX_PANEL_AUFTRAGKOPFDATEN, auftragKopfdaten);
		}

		return auftragKopfdaten;
	}

	private PanelAuftragKonditionen2 refreshAuftragKonditionen()
			throws Throwable {
		if (auftragKonditionen == null) {

			auftragKonditionen = new PanelAuftragKonditionen2(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.konditionen"),
					auftragDto.getIId());

			setComponentAt(IDX_PANEL_AUFTRAGKONDITIONEN, auftragKonditionen);
		}

		return auftragKonditionen;
	}

	private void refreshSichtLieferstatus() throws Throwable {
		QueryType[] qtPositionen = null;
		FilterKriterium[] filtersPositionen = AuftragFilterFactory
				.getInstance().createFKFlrauftragiid(auftragDto.getIId());

		if (auftragSichtLieferstatus == null) {

			auftragSichtLieferstatus = new PanelQuery(
					qtPositionen,
					filtersPositionen,
					QueryParameters.UC_ID_SICHTLIEFERSTATUS,
					null,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.sichtlieferstatus"),
					true);

			setComponentAt(IDX_PANEL_SICHTLIEFERSTATUS,
					auftragSichtLieferstatus);

			auftragSichtLieferstatus.eventYouAreSelected(false);
		}

		auftragSichtLieferstatus.setDefaultFilter(filtersPositionen);
	}

	private void refreshAuftragUebersicht() throws Throwable {
		if (ptAuftragUebersicht == null) {

			ptAuftragUebersicht = new PanelTabelleAuftragUebersicht(
					QueryParameters.UC_ID_AUFTRAGUEBERSICHT,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"),
					getInternalFrame());
			setComponentAt(IDX_PANEL_AUFTRAGUEBERSICHT, ptAuftragUebersicht);
		}
	}

	private void refreshAuftragzeiten() throws Throwable {
		if (ptAuftragzeiten == null) {

			ptAuftragzeiten = new PanelTabelleAuftragzeiten(
					QueryParameters.UC_ID_AUFTRAGZEITEN,
					LPMain.getTextRespectUISPr("auft.title.panel.auftragzeiten"),
					getInternalFrame());

			// default Kriterium vorbelegen
			FilterKriterium[] aFilterKrit = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(
					AuftragFac.KRIT_PERSONAL, true, "true",
					FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium(
					AuftragFac.KRIT_AUFTRAG_I_ID, true, auftragDto.getIId()
							.toString(), FilterKriterium.OPERATOR_EQUAL, false);

			aFilterKrit[0] = krit1;
			aFilterKrit[1] = krit2;

			ptAuftragzeiten.setDefaultFilter(aFilterKrit);

			setComponentAt(IDX_PANEL_AUFTRAGZEITEN, ptAuftragzeiten);
		}
	}

	public PanelSplit refreshPanelAuftragsrnnrn(Integer iIdPosition)
			throws Throwable {
		String[] aWhichStandardButtonIUse = null;
		FilterKriterium[] filtersPositionen = AuftragFilterFactory
				.getInstance().createFKAuftragpositioniid(iIdPosition);

		panelAuftragsnrnrnTopQP = new PanelQuery(null, filtersPositionen,
				QueryParameters.UC_ID_AUFTRAGSERIENNRN,
				aWhichStandardButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("artikel.report.seriennummern"),
				true);

		panelAuftragsnrnrnBottomD = new PanelStammdatenCRUD(getInternalFrame(),
				LPMain.getTextRespectUISPr("artikel.report.seriennummern"),
				null, HelperClient.SCRUD_AUFTRAGSERIENNUMMER_FILE,
				getInternalFrameAuftrag(),
				HelperClient.LOCKME_AUFTRAGSERIENNUMMERN);

		panelAuftragsnrnrnSP = new PanelSplit(getInternalFrame(),
				panelAuftragsnrnrnBottomD, panelAuftragsnrnrnTopQP,
				400 - ((PanelStammdatenCRUD) panelAuftragsnrnrnBottomD)
						.getAnzahlControls() * 30);

		// }
		panelAuftragsnrnrnTopQP.eventYouAreSelected(false);
		return panelAuftragsnrnrnSP;
	}

	private PanelSplit refreshAuftragTeilnehmer() throws Throwable {
		QueryType[] qtTeilnehmer = null;
		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory
				.getInstance().createFKFlrauftragiid(auftragDto.getIId());

		if (auftragTeilnehmer == null) {

			auftragTeilnehmerBottom = new PanelAuftragTeilnehmer(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.teilnehmer"),
					null); // eventuell
			// gibt es
			// noch
			// keinen
			// Teilnehmer

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN, };

			auftragTeilnehmerTop = new PanelQuery(qtTeilnehmer,
					filtersTeilnehmer, QueryParameters.UC_ID_AUFTRAGTEILNEHMER,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.teilnehmer"),
					true);

			// pqaddbutton: 1 Hier den zusaetzlichen Button aufs Panel bringen
			auftragTeilnehmerTop
					.createAndSaveAndShowButton(
							"/com/lp/client/res/new_from_externer_teilnehmer.png",
							LPMain.getTextRespectUISPr("auft.tooltip.externerteilnehmer"),
							MY_OWN_NEW_NEU_EXTERNER_TEILNEHMER,
							RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragTeilnehmer = new PanelSplit(getInternalFrame(),
					auftragTeilnehmerBottom, auftragTeilnehmerTop, 200);

			setComponentAt(IDX_PANEL_AUFTRAGTEILNEHMER, auftragTeilnehmer);
		}

		auftragTeilnehmerTop.setDefaultFilter(filtersTeilnehmer);

		return auftragTeilnehmer;
	}

	private PanelSplit refreshZeitplan() throws Throwable {

		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory
				.getInstance().createFKFlrauftragiid(auftragDto.getIId());

		if (auftragZeitplan == null) {

			auftragZeitplanBottom = new PanelZeitplan(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zeitplan"), null); // eventuell

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			auftragZeitplanTop = new PanelQuery(null, filtersTeilnehmer,
					QueryParameters.UC_ID_ZEITPLAN, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zeitplan"), true);

			auftragZeitplan = new PanelSplit(getInternalFrame(),
					auftragZeitplanBottom, auftragZeitplanTop, 200);

			setComponentAt(IDX_PANEL_ZEITPLAN, auftragZeitplan);
		}

		auftragZeitplanTop.setDefaultFilter(filtersTeilnehmer);

		return auftragZeitplan;
	}

	private PanelSplit refreshZahlungsplan() throws Throwable {
		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory
				.getInstance().createFKFlrauftragiid(auftragDto.getIId());

		if (auftragZahlungsplan == null) {

			auftragZahlungsplanBottom = new PanelZahlungsplan(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zahlungsplan"), null); // eventuell

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			auftragZahlungsplanTop = new PanelQuery(null, filtersTeilnehmer,
					QueryParameters.UC_ID_ZAHLUNGSPLAN, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zahlungsplan"), true);

			auftragZahlungsplan = new PanelSplit(getInternalFrame(),
					auftragZahlungsplanBottom, auftragZahlungsplanTop, 200);

			setComponentAt(IDX_PANEL_ZAHLUNGSPLAN, auftragZahlungsplan);
		}

		auftragZahlungsplanTop.setDefaultFilter(filtersTeilnehmer);

		return auftragZahlungsplan;
	}

	private PanelSplit refreshZahlungsplanmeilenstein() throws Throwable {
		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory
				.getInstance().createFKZahlungsplanmeilenstein(
						(Integer) auftragZahlungsplanTop.getSelectedId());

		if (auftragZahlungsplanmeilenstein == null) {

			auftragZahlungsplanmeilensteinBottom = new PanelZahlungsplanmeilenstein(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein"),
					null); // eventuell

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			auftragZahlungsplanmeilensteinTop = new PanelQuery(null,
					filtersTeilnehmer,
					QueryParameters.UC_ID_ZAHLUNGSPLANMEILENSTEIN,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein"),
					true);

			auftragZahlungsplanmeilenstein = new PanelSplit(getInternalFrame(),
					auftragZahlungsplanmeilensteinBottom,
					auftragZahlungsplanmeilensteinTop, 200);

			setComponentAt(IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN,
					auftragZahlungsplanmeilenstein);
		}

		auftragZahlungsplanmeilensteinTop.setDefaultFilter(filtersTeilnehmer);

		return auftragZahlungsplanmeilenstein;
	}

	private void refreshEigenschaften(Integer key) throws Throwable {
		if (panelDetailAuftragseigenschaft == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
					PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

			panelDetailAuftragseigenschaft = new PanelDynamisch(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.eigenschaften"),
					auftragAuswahl, PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN,
					HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse);
			setComponentAt(IDX_PANEL_AUFTRAGEIGENSCHAFTEN,
					panelDetailAuftragseigenschaft);
		}
	}

	private void refreshSichtAuftragAufAndereBelegarten(Integer iIdPositionI)
			throws Throwable {
		QueryType[] qt = null;
		FilterKriterium[] fk = AuftragFilterFactory.getInstance()
				.createFKSichtAuftragpositionAufLieferscheine(iIdPositionI);

		if (sichtAuftragAufAndereBelegarten == null) {

			sichtAuftragAufAndereBelegartenBottom = new PanelAuftragpositionInLieferschein(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.auftragpositioninlieferschein"),
					null);

			sichtAuftragAufAndereBelegartenTop = new PanelQuery(
					qt,
					fk,
					QueryParameters.UC_ID_AUFTRAGPOSITIONINLIEFERSCHEIN,
					null,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.auftragpositioninlieferschein"),
					true);

			sichtAuftragAufAndereBelegarten = new PanelSplit(
					getInternalFrame(), sichtAuftragAufAndereBelegartenBottom,
					sichtAuftragAufAndereBelegartenTop, 300);

			setComponentAt(IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN,
					sichtAuftragAufAndereBelegarten);
		}

		sichtAuftragAufAndereBelegartenTop.setDefaultFilter(fk);
	}

	/**
	 * Den Text der Titelleiste ueberschreiben.
	 * 
	 * @param panelTitle
	 *            der Title des aktuellen panel
	 * @throws Exception
	 */
	public void setTitleAuftrag(String panelTitle) throws Throwable {
		// aktuelle auftragnummer bestimmen
		StringBuffer auftragnummer = new StringBuffer("");
		if (auftragDto == null || auftragDto.getIId() == null) {
			auftragnummer.append(LPMain
					.getTextRespectUISPr("auft.title.neuerauftrag"));
		} else {

			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							auftragDto.getKundeIIdAuftragsadresse());

			auftragnummer
					.append(LPMain.getTextRespectUISPr("auft.auftrag"))
					.append(" ")
					.append(auftragDto.getCNr())
					.append(" ")
					.append(kundeDto.getPartnerDto().formatFixTitelName1Name2());

		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				auftragnummer.toString());
	}

	public void setTitleAuftragOhneAuftragnummer(String panelTitle)
			throws Exception {
		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	public void resetDtos() {
		auftragDto = new AuftragDto();
		kundeAuftragDto = new KundeDto();
		kopftextDto = new AuftragtextDto();
		fusstextDto = new AuftragtextDto();
	}

	public boolean pruefeAktuellenAuftrag() {
		boolean bIstGueltig = true;

		if (auftragDto == null || auftragDto.getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("auft.warning.keinauftrag"));
		}

		return bIstGueltig;
	}

	public boolean aktuellerAuftragHatPositionen() throws Throwable {
		boolean bHatPositionen = true;

		if (DelegateFactory.getInstance().getAuftragpositionDelegate()
				.getAnzahlMengenbehafteteAuftragpositionen(auftragDto.getIId()) <= 0) {
			bHatPositionen = false;
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("auft.warning.keinepositionen"));
		}

		return bHatPositionen;
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT:
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}

	/**
	 * Pruefen, ob zu dem aktuellen Auftrag Artikelpositionen erfasst sind.
	 * 
	 * @return boolean true, wenn es Artikelpositionen zu diesem Auftrag gibt
	 * @throws java.lang.Throwable
	 */
	public boolean aktuellerAuftragHatArtikelpositionen() throws Throwable {
		boolean bHatArtikelpositionen = true;

		if (DelegateFactory
				.getInstance()
				.getAuftragpositionDelegate()
				.berechneAnzahlArtikelpositionenMitStatus(auftragDto.getIId(),
						null) <= 0) {
			bHatArtikelpositionen = false;
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.warning"),
							LPMain.getTextRespectUISPr("auft.warning.keineartikelpositionen"));
		}

		return bHatArtikelpositionen;
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_DATEI_NEU_AUS_AUFTRAG)) {
			// der Benutzer muss einen Auftrag auswaehlen
			dialogQueryAuftragFromListe(null);
		} else if (e.getActionCommand().equals(
				MENU_ACTION_DATEI_AUFTRAGBESTAETIGUNG)) {
			printAuftragbestaetigung();
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_PACKLISTE)) {
			printPackliste();
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_ETIKETT)) {
			printEtikett();
		} else if (e.getActionCommand().equals(MENU_ACTION_INFO_AUFTRAGZEITEN)) {
			if (pruefeAktuellenAuftrag()) {
				getKriterienAuftragzeiten();
				getInternalFrame().showPanelDialog(pdKriterienAuftragzeiten);
				pdKriterienAuftragzeitenUeberMenueAufgerufen = true;
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_BEGRUENDUNG)) {

			panelQueryFLRBegruendung = AuftragFilterFactory.getInstance()
					.createPanelFLRBegruendung(getInternalFrame(), null, true);
			new DialogQuery(panelQueryFLRBegruendung);

		} else if (e.getActionCommand()
				.equals(MENU_ACTION_INFO_NACHKALKULATION)) {
			if (auftragDto != null && auftragDto.getIId() != null) {
				if (auftragDto.getStatusCNr().equals(
						AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
					showStatusMessage("lp.warning",
							"auft.storno.wurdestorniert");
				} else {
					printNachkalkulation();
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (pruefeAktuellenAuftrag()) {
				if (!refreshAuftragKopfdaten().isLockedDlg()) {

					// Statuswechsel 'Offen' -> 'Erledigt' : Ausgeloest durch
					// Menue
					if (auftragDto.getStatusCNr().equals(
							AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
							|| auftragDto
									.getStatusCNr()
									.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
						if (DialogFactory
								.showMeldung(
										LPMain.getTextRespectUISPr("auft.auftragstatus.erledigtsetzen"),
										LPMain.getTextRespectUISPr("lp.hint"),
										javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

							DelegateFactory.getInstance().getAuftragDelegate()
									.manuellErledigen(auftragDto.getIId());
							refreshAktuellesPanel();
						}
					} else {
						showStatusMessage("lp.warning",
								"auft.warning.auftragkannnichterledigtwerden");
					}
				}
			}
		}

		else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN)) {
			if (auftragDto != null && auftragDto.getIId() != null) {
				refreshAuftragKopfdaten();
				getPanelKopfdaten().eventActionLock(null);
				DialogTerminverschiebenAuftrag d = new DialogTerminverschiebenAuftrag(
						this);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

			}

		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN)) {
			// nur, wenn auch eine Bestellung ausgewaehlt ist.
			if (auftragDto != null && auftragDto.getIId() != null) {
				// Statuswechsel 'Erledigt' -> 'Offen' : Ausgeloest durch Menue
				if (auftragDto.getStatusCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
					if (DialogFactory
							.showMeldung(
									LPMain.getTextRespectUISPr("auft.auftragstatus.offensetzen"),
									LPMain.getTextRespectUISPr("lp.hint"),
									javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
						DelegateFactory.getInstance().getAuftragDelegate()
								.erledigungAufheben(auftragDto.getIId());
						getAuftragKopfdaten().eventYouAreSelected(false);
					}
				} else {

					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("bes.warning.kannnichtunerledigtwerden"));

					mf.setLocale(LPMain.getTheClient().getLocUi());

					Object pattern[] = { auftragDto.getStatusCNr().trim() };

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.warning"),
									mf.format(pattern)
											+ ". "
											+ LPMain.getTextRespectUISPr("auf.erledigung.aufheben.hinweis"));
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_ANGEBOT_ZEITDATEN_UEBERNEHMEN)) {
			if (auftragDto.getAngebotIId() != null) {
				DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.konvertiereAngebotszeitenNachAuftragzeiten(
								auftragDto.getAngebotIId(), auftragDto.getIId());
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("auft.error.angebotnichtdefiniert"));
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_ERFUELLUNGSGRAD)) {
			if (auftragDto.getIId() != null) {
				Double fErfuellungsgrad = null;
				if (auftragDto.getFErfuellungsgrad() != null) {
					fErfuellungsgrad = auftragDto.getFErfuellungsgrad();
				}
				DialogErfuellungsgrad d = new DialogErfuellungsgrad(auftragDto,
						fErfuellungsgrad);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
				auftragKopfdaten.eventYouAreSelected(false);
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_INTERNER_KOMMENTAR)) {
			if (pruefeAktuellenAuftrag()) {
				if (!getAuftragKopfdaten().isLockedDlg()) {
					refreshPdAuftragInternerKommentar();
					getInternalFrame().showPanelDialog(
							pdAuftragInternerKommentar);
					setTitleAuftrag(LPMain
							.getTextRespectUISPr("lp.internerkommentar"));
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_EXTERNER_KOMMENTAR)) {
			if (pruefeAktuellenAuftrag()) {
				if (!getAuftragKopfdaten().isLockedDlg()) {
					refreshPdAuftragExternerKommentar();
					getInternalFrame().showPanelDialog(
							pdAuftragExternerKommentar);
					setTitleAuftrag(LPMain
							.getTextRespectUISPr("lp.externerkommentar"));
				}
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_OFFEN)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportAuftragOffene2(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("auft.print.listeoffeneauftraege")),
							getKundeAuftragDto().getPartnerDto(),
							getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_OFFEN_DETAILS)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportAuftragOffeneDetails(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("auft.print.listeoffenedetails")),
							getKundeAuftragDto().getPartnerDto(),
							getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_OFFEN_POSITIONEN)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportAuftragOffenePositionen(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("auft.print.listeoffenedetails")),
							getKundeAuftragDto().getPartnerDto(),
							getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_UEBERSICHT)) {
			// wenn man diese Auswahl zuliesse, waeren danach die Panels
			// verfuegbar
			if (pruefeAktuellenAuftrag()) {
				getKriterienAuftragUebersicht();
				getInternalFrame()
						.showPanelDialog(pdKriterienAuftragUebersicht);
				bKriterienAuftragUebersichtUeberMenueAufgerufen = true;
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_STATISTIK)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auft.menu.journal.statistik");
			getInternalFrame().showReportKriterien(
					new ReportAuftragstatistik(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_ERFUELLUNGSGRAD)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auft.menu.journal.erfuellungsgrad");
			getInternalFrame().showReportKriterien(
					new ReportErfuellungsgrad(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_WARTUNGSAUSWERTUNG)) {

			String add2Title = LPMain
					.getTextRespectUISPr("kunde.report.wartungsauswertung");
			getInternalFrame()
					.showReportKriterien(
							new ReportWartungsauswertung(getInternalFrame(),
									add2Title));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_ALLE_AUFTRAEGE)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auft.menu.journal.alle");
			getInternalFrame().showReportKriterien(
					new ReportAuftragAlle(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_ERLEDIGTE_AUFTRAEGE)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auft.menu.journal.erledigte");
			getInternalFrame().showReportKriterien(
					new ReportAuftragErledigt(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_TAETIGKEITSSTATISTIK)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auftrag.report.taetigkeitsstatistik");
			getInternalFrame().showReportKriterien(
					new ReportTaetigkeitsstatistik(getInternalFrame(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_INFO_RAHMENERFUELLUNG)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auftrag.report.erfuellungsjournal");
			getInternalFrame().showReportKriterien(
					new ReportErfuellungsjournal(getInternalFrameAuftrag(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_INFO_AUFTRAGSUEBERSICHT)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auft.auftrag.info.auftragsuebersicht");
			getInternalFrame().showReportKriterien(
					new ReportAuftragsuebersicht(getInternalFrameAuftrag(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_PROJEKTBLATT)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auft.report.projektblatt");
			getInternalFrame().showReportKriterien(
					new ReportProjektblatt(getInternalFrameAuftrag(),
							getAuftragDto().getIId(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_INFO_RAHMENUEBERSICHT)) {

			String add2Title = LPMain
					.getTextRespectUISPr("auftrag.report.rahmenuebersicht");
			if (getAuftragDto().getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)) {

				getInternalFrame().showReportKriterien(
						new ReportRahmenuebersicht(getInternalFrameAuftrag(),
								getAuftragDto().getIId(), add2Title));

			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("auft.rahmenauftragwaehlen"));
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_INFO_WIEDERBESCHAFFUNG)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportAuftragWiederbeschaffung(
									getInternalFrame(),
									getAuftragDto().getIId(),
									LPMain.getTextRespectUISPr("auft.menu.info.wiederbeschaffung")),
							getKundeAuftragDto().getPartnerDto(),
							getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_INFO_ROLLIERENDEPLANUNG)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportRollierendePlanung(
									getInternalFrame(),
									getAuftragDto().getIId(),
									LPMain.getTextRespectUISPr("auft.rollierendeplanung")),
							getKundeAuftragDto().getPartnerDto(),
							getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_INFO_VERFUEGBARKEITSPRUEFUNG)) {
			if (auftragDto != null && auftragDto.getIId() != null) {
				if (pruefeAktuellenAuftrag()) {
					if (this.aktuellerAuftragHatArtikelpositionen()) {
						getInternalFrame().showReportKriterien(
								new ReportVerfuegbarkeitspruefung(
										getInternalFrame(), getAuftragDto(),
										getTitleDruck()),
								getKundeAuftragDto().getPartnerDto(),
								getAuftragDto().getAnsprechparnterIId(), false);
					}
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_ACTION_DATEI_VERSANDWEGBESTAETIGUNG)) {
			printAuftragbestaetigungClevercure();
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_HANDARTIKEL_UMANDELN)) {
			if (auftragPositionenTop != null
					&& auftragPositionenTop.getSelectedId() != null) {

				AuftragpositionDto posDto = DelegateFactory
						.getInstance()
						.getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey(
								(Integer) auftragPositionenTop.getSelectedId());

				if (posDto.getArtikelIId() != null
						&& auftragDto != null
						&& posDto.getAuftragpositionstatusCNr().equals(
								AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN)) {

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
											ArtikelFac.HANDARTIKEL_UMWANDELN_AUFTRAG,
											d.getArtikelnummerNeu());
						}

					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr("lp.info"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"auftrag.bearbeiten.handartikelumwandeln.error"));
					}

				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.info"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"auftrag.bearbeiten.handartikelumwandeln.error"));
				}
				auftragPositionen.eventYouAreSelected(false);
				auftragPositionenTop.setSelectedId(posDto.getIId());
			}

		}
	}

	public void eventActionUnlock() throws Throwable {
		getPanelKopfdaten().eventActionUnlock(null);
		refreshAuftragKopfdaten().eventActionRefresh(null, false);
	}

	private void printAuftragbestaetigungClevercure() throws Throwable {
		JTextArea msgLabel;
		JProgressBar progressBar;
		final int MAXIMUM = 100;
		JPanel panel;

		progressBar = new JProgressBar(0, MAXIMUM);
		progressBar.setIndeterminate(true);
		msgLabel = new JTextArea("An Clevercure \u00FCbermitteln");
		msgLabel.setEditable(false);

		panel = new JPanel(new BorderLayout(5, 5));
		panel.add(msgLabel, BorderLayout.PAGE_START);
		panel.add(progressBar, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

		final JDialog dialog = new JDialog();
		dialog.getContentPane().add(panel);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setSize(500, dialog.getHeight());
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(false);
		dialog.setVisible(true);
		msgLabel.setBackground(panel.getBackground());

		SwingWorker<String, Object> worker = new SwingWorker<String, Object>() {
			@Override
			protected void done() {
				dialog.dispose();
			}

			@Override
			protected String doInBackground() throws Exception {
				String result = "";
				try {
					result = DelegateFactory.getInstance().getAuftragDelegate()
							.createOrderResponsePost(getAuftragDto());
					publish(result);
				} catch (Throwable t) {
					handleException(t, false);
				}

				return result;
			}
		};

		worker.execute();
	}

	private void refreshPdAuftragInternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Auftrags nicht richtig
		pdAuftragInternerKommentar = new PanelDialogAuftragKommentar(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.internerkommentar"), true);
	}

	private void refreshPdAuftragExternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Auftrags nicht richtig
		pdAuftragExternerKommentar = new PanelDialogAuftragKommentar(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.externerkommentar"), false);
	}

	public boolean aktualisiereAuftragstatus() throws Throwable {
		boolean bIstAktualisierenErlaubt = false;

		if (pruefeAktuellenAuftrag()) {

			// im Status 'Angelegt' duerfen die Kopfdaten geaendert werden
			if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				bIstAktualisierenErlaubt = true;
			} else if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
				// im Status 'Offen' duerfen die Kopfdaten geaendert werden
				bIstAktualisierenErlaubt = true;
			} else if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
				if (DialogFactory.showMeldung(LPMain
						.getTextRespectUISPr("auft.auftragstatus.offensetzen"),
						LPMain.getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

					DelegateFactory
							.getInstance()
							.getAuftragDelegate()
							.aendereAuftragstatus(auftragDto.getIId(),
									AuftragServiceFac.AUFTRAGSTATUS_OFFEN);

					// DialogFactory.showModalDialog(
					// LPMain.getTextRespectUISPr("lp.hint"),
					// LPMain.getTextRespectUISPr(
					// "auft.auftragstatus.offen"));

					// den geaenderten Status anzeigen
					setAuftragDto(DelegateFactory.getInstance()
							.getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragDto.getIId()));
					auftragKopfdaten.eventYouAreSelected(false);
				}
			} else if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
				if (DialogFactory.showMeldung(LPMain
						.getTextRespectUISPr("auft.auftragstatus.offensetzen"),
						LPMain.getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

					DelegateFactory
							.getInstance()
							.getAuftragDelegate()
							.aendereAuftragstatus(auftragDto.getIId(),
									AuftragServiceFac.AUFTRAGSTATUS_OFFEN);

					DialogFactory.showModalDialog(LPMain
							.getTextRespectUISPr("lp.hint"), LPMain
							.getTextRespectUISPr("auft.auftragstatus.offen"));

					// den geaenderten Status anzeigen
					setAuftragDto(DelegateFactory.getInstance()
							.getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragDto.getIId()));
					auftragKopfdaten.eventYouAreSelected(false);
				}
			} else if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				if (DialogFactory
						.showMeldung(
								LPMain.getTextRespectUISPr("auft.hint.auftragerledigt"),
								LPMain.getTextRespectUISPr("lp.hint"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
					DelegateFactory.getInstance().getAuftragDelegate()
							.erledigungAufheben(auftragDto.getIId());
					initializeDtos(auftragDto.getIId());
					refreshAktuellesPanel();
				}
			}
		}

		return bIstAktualisierenErlaubt;
	}

	/**
	 * Diese Methode prueft den Status des aktuellen Auftrags und legt fest, ob
	 * eine Aenderung in den Kopfdaten bzw. Konditionen erlaubt ist.
	 * 
	 * @return boolean true, wenn ein update erlaubt ist
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public boolean istAktualisierenAuftragErlaubt() throws Throwable {
		boolean bIstAenderungErlaubtO = false;

		if (pruefeAktuellenAuftrag()) {
			if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				bIstAenderungErlaubtO = true;
			} else if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
				boolean bZuruecknehmen = (DialogFactory.showMeldung(LPMain
						.getTextRespectUISPr("auft.hint.offennachangelegt"),
						LPMain.getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bZuruecknehmen == true) {
					DelegateFactory
							.getInstance()
							.getAuftragDelegate()
							.setAuftragstatus(auftragDto,
									AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
				}
				return bZuruecknehmen;
			} else if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
				/*
				 * SP2553 if
				 * (AuftragServiceFac.AUFTRAGART_RAHMEN.equals(auftragDto
				 * .getAuftragartCNr())) { bIstAenderungErlaubtO = true; } else
				 * {
				 */
				showStatusMessage("lp.warning",
						"auft.warning.auftragkannnichtgeaendertwerden");
				// }
			} else {
				showStatusMessage("lp.warning",
						"auft.warning.auftragkannnichtgeaendertwerden");
			}
		}

		return bIstAenderungErlaubtO;
	}

	/*
	 * private void printListeOffenerAuftraege(ReportJournalKriterienDto
	 * reportJournalKriterienDtoI) throws Throwable { try { JasperPrint print =
	 * DelegateFactory.getInstance().getAuftragReportDelegate().
	 * printAuftragOffene(reportJournalKriterienDtoI);
	 * 
	 * if (print == null) { throw new ExceptionLP(EJBExceptionLP.FEHLER, new
	 * Exception("print == null")); }
	 * 
	 * getInternalFrame().showReport( print, LPMain.getTextRespectUISPr
	 * ("auft.print.listeoffeneauftraege")); } catch (ExceptionLP ex) {
	 * 
	 * int code = ex.getICode();
	 * 
	 * switch (code) { case EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN:
	 * DialogFactory.showModalDialog( LPMain.getTextRespectUISPr("lp.error"),
	 * LPMain.getTextRespectUISPr( "auft.warning.keineoffenenauftraege")); } } }
	 */

	private void printNachkalkulation() throws Throwable {
		if (pruefeAktuellenAuftrag()) {
			if (DelegateFactory.getInstance().getAuftragDelegate()
					.berechneAnzahlBelegeZuAuftrag(auftragDto.getIId()) == 0) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("auft.hint.keinebelege"));
			} else {
				getInternalFrame().showReportKriterien(
						new ReportAuftragnachkalkulation(getInternalFrame(),
								getAuftragDto().getIId(), getTitleDruck()),
						getKundeAuftragDto().getPartnerDto(),
						getAuftragDto().getAnsprechparnterIId(), false);
			}
		}
	}

	/**
	 * Packliste zum aktuellen Auftrag drucken.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void printPackliste() throws Throwable {
		if (auftragDto != null && auftragDto.getIId() != null) {
			if (pruefeAktuellenAuftrag()) {
				if (this.aktuellerAuftragHatArtikelpositionen()) {
					getInternalFrame().showReportKriterien(
							new ReportAuftragPackliste(getInternalFrame(),
									getAuftragDto().getIId(), getTitleDruck()),
							getKundeAuftragDto().getPartnerDto(),
							getAuftragDto().getAnsprechparnterIId(), false);
				}
			}
		}
	}

	private void printEtikett() throws Throwable {

	}

	/**
	 * Auftragbestaetigung + Vorkalkulation zum aktuellen Auftrag drucken.
	 * 
	 * @throws Throwable
	 */
	public void printAuftragbestaetigung() throws Throwable {
		if (auftragDto != null && auftragDto.getIId() != null) {
			// belegartkonditionen: 7 Pruefen, ob die Konitionen erfasst wurden
			if (pruefeKonditionen()) {
				if (aktuellerAuftragHatPositionen()) {
					if (!refreshAuftragKopfdaten().isLockedDlg()) {
						DelegateFactory.getInstance().getLagerDelegate()
								.getHauptlagerDesMandanten();
						// pruefen, beginn und ende
						boolean ok = DelegateFactory.getInstance()
								.getAuftragDelegate()
								.checkPositionFormat(auftragDto.getIId());
						if (ok) {
							getInternalFrame().showReportKriterien(
									new ReportAuftrag(getInternalFrame(),
											getAktuellesPanel(),
											getAuftragDto(), getTitleDruck()),
									getKundeAuftragDto().getPartnerDto(),
									getAuftragDto().getAnsprechparnterIId(),
									false);
						} else {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("auft.hint.beginnohneende"));
						}
					}
				}
			}
		}
	}

	/**
	 * belegartkonditionen: 3 Diese Methode prueft, ob zum aktuellen Auftrag
	 * Konditionen erfasst wurden. <br>
	 * Wenn der Benutzer aufgrund von KONDITIONEN_BESTAETIGEN die Konditionen
	 * nicht bestaetigen muss, muessen die Default Texte vorbelegt werden.
	 * 
	 * @return boolean true, wenn die Konditionen gueltig erfasst wurden
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected boolean pruefeKonditionen() throws Throwable {
		boolean bErfasst = true;

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN);

		Short sValue = new Short(parameter.getCWert());

		if (pruefeAktuellenAuftrag()) {
			if (Helper.short2boolean(sValue)) {
				if (auftragDto.getAuftragIIdKopftext() == null
						&& auftragDto.getAuftragIIdFusstext() == null) {
					bErfasst = false;

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("lp.hint.konditionenerfassen"));
				}
			} else {
				// belegartkonditionen: 4 die Auftragkonditionen initialisieren
				initAuftragKonditionen();
			}
		}

		return bErfasst;
	}

	/**
	 * belegartkonditionen: 5 Je nach Mandantenparameter muss der Benutzer die
	 * Konditionen nicht erfassen. Damit der Auftrag gedruckt werden kann,
	 * muessen die Konditionen aber initialisiert worden sein.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void initAuftragKonditionen() throws Throwable {
		initAuftragtexte(); // Kopf- und Fusstext werden initialisiert

		getAuftragDto().setAuftragtextIIdKopftext(kopftextDto.getIId());
		getAuftragDto().setAuftragtextIIdFusstext(fusstextDto.getIId());

		DelegateFactory.getInstance().getAuftragDelegate()
				.updateAuftragOhneWeitereAktion(getAuftragDto());
	}

	/**
	 * Diese Methode setzt des aktuellen Auftrag aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = auftragAuswahl.getSelectedId();

		if (oKey != null) {
			initializeDtos((Integer) oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void getKriterienAuftragUebersicht() throws Throwable {
		if (pdKriterienAuftragUebersicht == null) {

			pdKriterienAuftragUebersicht = new PanelDialogKriterienAuftragUebersicht(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht"));
		}
	}

	private void getKriterienAuftragzeiten() throws Throwable {
		if (pdKriterienAuftragzeiten == null) {

			pdKriterienAuftragzeiten = new PanelDialogKriterienAuftragzeiten(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.kriterienauftragzeiten"));
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		// if(menuBar == null) {
		// menuBar = new WrapperMenuBar(this) ;
		// }
		menuBar = new WrapperMenuBar(this);
		// Menue Datei; ein neuer Eintrag wird immer oben im Menue eingetragen
		JMenu jmModul = (JMenu) menuBar.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);

		JMenuItem menuItemDateiEtikett = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.datei.auftragetikett"));
		menuItemDateiEtikett.addActionListener(this);
		menuItemDateiEtikett.setActionCommand(MENU_ACTION_DATEI_ETIKETT);
		// jmModul.add(menuItemDateiEtikett, 0);

		JMenuItem menuItemDateiPackliste = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.datei.packliste"));
		menuItemDateiPackliste.addActionListener(this);
		menuItemDateiPackliste.setActionCommand(MENU_ACTION_DATEI_PACKLISTE);
		jmModul.add(menuItemDateiPackliste, 0);
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {

			JMenuItem menuItemDateiAuftragbestaetigung = new JMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.drucken"));
			menuItemDateiAuftragbestaetigung.addActionListener(this);
			menuItemDateiAuftragbestaetigung
					.setActionCommand(MENU_ACTION_DATEI_AUFTRAGBESTAETIGUNG);
			jmModul.add(menuItemDateiAuftragbestaetigung, 0);

			// updateMenuItemDateiAuftragVersand();
		}

		// Menue Bearbeiten

		boolean bSchreibrecht = false;

		if (getInternalFrame().getRechtModulweit().equals(
				RechteFac.RECHT_MODULWEIT_UPDATE)) {
			bSchreibrecht = true;
		}

		JMenu jmBearbeiten = (JMenu) menuBar
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FERT_TERMINE_VERSCHIEBEN)) {
			JMenuItem menuItemTerminverschieben = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.terminverschieben"));
			menuItemTerminverschieben.addActionListener(this);
			menuItemTerminverschieben
					.setActionCommand(MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN);
			jmBearbeiten.add(menuItemTerminverschieben);
		}

		JMenuItem menuItemHandartikelUmwandeln = new JMenuItem(
				LPMain.getTextRespectUISPr("angebot.bearbeiten.handartikelumwandeln"));
		menuItemHandartikelUmwandeln.addActionListener(this);
		menuItemHandartikelUmwandeln
				.setActionCommand(MENU_BEARBEITEN_HANDARTIKEL_UMANDELN);
		jmBearbeiten.add(menuItemHandartikelUmwandeln);

		jmBearbeiten.add(new JSeparator(), 0);

		JMenuItem menuItemBearbeitenExternerKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.externerkommentar"));
		menuItemBearbeitenExternerKommentar.addActionListener(this);
		menuItemBearbeitenExternerKommentar
				.setActionCommand(MENU_BEARBEITEN_EXTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenExternerKommentar, 0);

		JMenuItem menuItemBearbeitenInternerKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.internerkommentar"));
		menuItemBearbeitenInternerKommentar.addActionListener(this);
		menuItemBearbeitenInternerKommentar
				.setActionCommand(MENU_BEARBEITEN_INTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenInternerKommentar, 0);

		jmBearbeiten.add(new JSeparator(), 0);

		JMenuItem menuItemBearbeitenBegruendung = new JMenuItem(
				LPMain.getTextRespectUISPr("ls.begruendung"));
		menuItemBearbeitenBegruendung.addActionListener(this);
		menuItemBearbeitenBegruendung
				.setActionCommand(MENU_BEARBEITEN_BEGRUENDUNG);
		jmBearbeiten.add(menuItemBearbeitenBegruendung, 0);

		if (bSchreibrecht) {
			// PJ18288
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_AUFTRAG_AUTOMATISCH_VOLLSTAENDIG_ERLEDIGEN);
			boolean bAutomatischErledigen = (Boolean) parameter
					.getCWertAsObject();

			String recht = null;
			if (bAutomatischErledigen == false) {
				recht = RechteFac.RECHT_AUFT_DARF_AUFTRAG_ERLEDIGEN;
			}

			WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"),
					recht);
			menuItemBearbeitenManuellErledigen.addActionListener(this);
			menuItemBearbeitenManuellErledigen
					.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);

			WrapperMenuItem menuItemBearbeitenManuellErledigenAufheben = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("bes.menu.bearbeiten.erledigungaufheben"),
					recht);

			menuItemBearbeitenManuellErledigenAufheben.addActionListener(this);
			menuItemBearbeitenManuellErledigenAufheben
					.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigenAufheben, 0);

			JMenuItem menuItemBearbeitenAngebotZeitDaten = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.hint.zeitdatenvonangebot"));

			menuItemBearbeitenAngebotZeitDaten.addActionListener(this);
			menuItemBearbeitenAngebotZeitDaten
					.setActionCommand(MENU_BEARBEITEN_ANGEBOT_ZEITDATEN_UEBERNEHMEN);
			jmBearbeiten.add(menuItemBearbeitenAngebotZeitDaten, 0);

			JMenuItem menuItemBearbeitenErfuellungsgrad = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.bearbeiten.erfuellungsgrad"));

			menuItemBearbeitenErfuellungsgrad.addActionListener(this);
			menuItemBearbeitenErfuellungsgrad
					.setActionCommand(MENU_BEARBEITEN_ERFUELLUNGSGRAD);
			jmBearbeiten.add(menuItemBearbeitenErfuellungsgrad, 0);
		}
		// Menue Journal
		JMenu jmJournal = (JMenu) menuBar
				.getComponent(WrapperMenuBar.MENU_JOURNAL);

		JMenuItem menuItemAlleAuftraege = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.alle"));
		menuItemAlleAuftraege.addActionListener(this);
		menuItemAlleAuftraege
				.setActionCommand(MENU_ACTION_JOURNAL_ALLE_AUFTRAEGE);
		jmJournal.add(menuItemAlleAuftraege);

		JMenuItem menuItemErledigteAuftraege = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.erledigte"));
		menuItemErledigteAuftraege.addActionListener(this);
		menuItemErledigteAuftraege
				.setActionCommand(MENU_ACTION_JOURNAL_ERLEDIGTE_AUFTRAEGE);
		jmJournal.add(menuItemErledigteAuftraege);

		JMenuItem menuItemJournalOffene = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.offen"));
		menuItemJournalOffene.addActionListener(this);
		menuItemJournalOffene.setActionCommand(MENU_ACTION_JOURNAL_OFFEN);
		jmJournal.add(menuItemJournalOffene);

		JMenuItem menuItemJournalOffeneDetails = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.offendetails"));
		menuItemJournalOffeneDetails.addActionListener(this);
		menuItemJournalOffeneDetails
				.setActionCommand(MENU_ACTION_JOURNAL_OFFEN_DETAILS);
		jmJournal.add(menuItemJournalOffeneDetails);

		JMenuItem menuItemJournalOffenePositionen = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.offenpositionen"));
		menuItemJournalOffenePositionen.addActionListener(this);
		menuItemJournalOffenePositionen
				.setActionCommand(MENU_ACTION_JOURNAL_OFFEN_POSITIONEN);
		jmJournal.add(menuItemJournalOffenePositionen);

		jmJournal.add(new JSeparator());

		if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {

			JMenuItem menuItemJournalUebersicht = new JMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.umsatzuebersicht"));
			menuItemJournalUebersicht.addActionListener(this);
			menuItemJournalUebersicht
					.setActionCommand(MENU_ACTION_JOURNAL_UEBERSICHT);
			jmJournal.add(menuItemJournalUebersicht);

			WrapperMenuItem menuItemJournalStatistik = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.journal.statistik"),
					RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemJournalStatistik.addActionListener(this);
			menuItemJournalStatistik
					.setActionCommand(MENU_ACTION_JOURNAL_STATISTIK);
			jmJournal.add(menuItemJournalStatistik);
		}

		WrapperMenuItem menuItemerfuellungsgrad = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.erfuellungsgrad"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemerfuellungsgrad.addActionListener(this);
		menuItemerfuellungsgrad
				.setActionCommand(MENU_ACTION_JOURNAL_ERFUELLUNGSGRAD);
		jmJournal.add(menuItemerfuellungsgrad);

		JMenuItem menuItemWartungsauswertung = new JMenuItem(
				LPMain.getTextRespectUISPr("kunde.report.wartungsauswertung"));
		menuItemWartungsauswertung.addActionListener(this);
		menuItemWartungsauswertung
				.setActionCommand(MENU_ACTION_JOURNAL_WARTUNGSAUSWERTUNG);
		jmJournal.add(menuItemWartungsauswertung);

		JMenuItem menuItemTaetigkeitsjournal = new JMenuItem(
				LPMain.getTextRespectUISPr("auftrag.report.taetigkeitsstatistik"));
		menuItemTaetigkeitsjournal.addActionListener(this);
		menuItemTaetigkeitsjournal
				.setActionCommand(MENU_ACTION_JOURNAL_TAETIGKEITSSTATISTIK);
		jmJournal.add(menuItemTaetigkeitsjournal);

		// Menue Info
		JMenu menuInfo = new WrapperMenu("lp.info", this);

		JMenuItem menuItemAuftragzeiten = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.datei.auftragzeiten"));
		menuItemAuftragzeiten.addActionListener(this);
		menuItemAuftragzeiten.setActionCommand(MENU_ACTION_INFO_AUFTRAGZEITEN);
		menuInfo.add(menuItemAuftragzeiten);

		if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {

			WrapperMenuItem menuItemNachkalkulation = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.datei.nachkalkulation"),
					RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemNachkalkulation.addActionListener(this);
			menuItemNachkalkulation
					.setActionCommand(MENU_ACTION_INFO_NACHKALKULATION);
			menuInfo.add(menuItemNachkalkulation);
		}

		JMenuItem menuItemWiederbeschaffung = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.info.wiederbeschaffung"));
		menuItemWiederbeschaffung.addActionListener(this);
		menuItemWiederbeschaffung
				.setActionCommand(MENUE_ACTION_INFO_WIEDERBESCHAFFUNG);
		menuInfo.add(menuItemWiederbeschaffung);

		JMenuItem menuItemRollierendePlanung = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.rollierendeplanung"));
		menuItemRollierendePlanung.addActionListener(this);
		menuItemRollierendePlanung
				.setActionCommand(MENUE_ACTION_INFO_ROLLIERENDEPLANUNG);
		menuInfo.add(menuItemRollierendePlanung);

		JMenuItem menuItemVerfuegbarkeitspruefung = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.info.verfuegbarkeitspruefung"));
		menuItemVerfuegbarkeitspruefung.addActionListener(this);
		menuItemVerfuegbarkeitspruefung
				.setActionCommand(MENUE_ACTION_INFO_VERFUEGBARKEITSPRUEFUNG);
		menuInfo.add(menuItemVerfuegbarkeitspruefung);

		JMenuItem menuItemRahmenerfuelleung = new JMenuItem(
				LPMain.getTextRespectUISPr("auftrag.report.erfuellungsjournal"));
		menuItemRahmenerfuelleung.addActionListener(this);
		menuItemRahmenerfuelleung
				.setActionCommand(MENUE_ACTION_INFO_RAHMENERFUELLUNG);
		menuInfo.add(menuItemRahmenerfuelleung);

		JMenuItem menuItemRahmenuebersicht = new JMenuItem(
				LPMain.getTextRespectUISPr("auftrag.report.rahmenuebersicht"));
		menuItemRahmenuebersicht.addActionListener(this);
		menuItemRahmenuebersicht
				.setActionCommand(MENUE_ACTION_INFO_RAHMENUEBERSICHT);
		menuInfo.add(menuItemRahmenuebersicht);
		// 18766
		JMenuItem menuItemAuftragsubersicht = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.auftrag.info.auftragsuebersicht"));
		menuItemAuftragsubersicht.addActionListener(this);
		menuItemAuftragsubersicht
				.setActionCommand(MENUE_ACTION_INFO_AUFTRAGSUEBERSICHT);
		menuInfo.add(menuItemAuftragsubersicht);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG)) {
			JMenuItem menuItemProjektblatt = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.report.projektblatt"));
			menuItemProjektblatt.addActionListener(this);
			menuItemProjektblatt
					.setActionCommand(MENUE_ACTION_INFO_PROJEKTBLATT);
			menuInfo.add(menuItemProjektblatt);
		}

		menuBar.addJMenuItem(menuInfo);

		return menuBar;
	}

	public void showStatusMessage(String lpTitle, String lpMessage)
			throws Throwable {

		MessageFormat mf = new MessageFormat(
				LPMain.getTextRespectUISPr(lpMessage));

		mf.setLocale(LPMain.getTheClient().getLocUi());

		Object pattern[] = { getAuftragStatus() };

		DialogFactory.showModalDialog(LPMain.getTextRespectUISPr(lpTitle),
				mf.format(pattern));
	}

	public void gotoAuswahl() throws Throwable {
		setSelectedComponent(auftragAuswahl);
		setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.auswahl"));
	}

	public String getAuftragStatus() throws Throwable {
		return DelegateFactory.getInstance().getLocaleDelegate()
				.getStatusCBez(getAuftragDto().getStatusCNr());
	}

	public boolean getLockStateEnableRefreshOnly() {
		boolean bLockStateEnableRefreshOnly = false;

		if (getAuftragDto().getStatusCNr().equals(
				AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
				|| getAuftragDto().getStatusCNr().equals(
						AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)
				|| getAuftragDto().getStatusCNr().equals(
						AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
			bLockStateEnableRefreshOnly = true;
		}

		return bLockStateEnableRefreshOnly;
	}

	private String getTitleDruck() {
		StringBuffer buff = new StringBuffer();

		buff.append(getAuftragDto().getCNr());
		buff.append(" ");
		buff.append(getKundeAuftragDto().getPartnerDto()
				.getCName1nachnamefirmazeile1());

		return buff.toString();
	}

	public void setBKriterienAuftragUebersichtUeberMenueAufgerufen(
			boolean bAufgerufenI) {
		bKriterienAuftragUebersichtUeberMenueAufgerufen = bAufgerufenI;
	}

	/**
	 * Das Schalten der Panels kann in Abhaengigkeit vom LockStateValue eines
	 * bestimmten Panels erfolgen.
	 * 
	 * @param lsvI
	 *            der aktuelle LockState
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void enablePanelsNachBitmuster(LockStateValue lsvI) throws Throwable {
		if (lsvI.getIState() != PanelBasis.LOCK_FOR_NEW) {
			enablePanelsNachBitmuster();
		}
	}

	public void enablePanelsNachBitmuster() throws Throwable {
		if (auftragDto.getIId() != null) {
			if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_FREI)) {
				aPanelsEnabled[IDX_PANEL_SICHTLIEFERSTATUS] = true;
				aPanelsEnabled[IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN] = true;
				aPanelsEnabled[IDX_PANEL_SICHTRAHMEN] = false;
			} else if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				aPanelsEnabled[IDX_PANEL_SICHTLIEFERSTATUS] = false;
				aPanelsEnabled[IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN] = false;
				aPanelsEnabled[IDX_PANEL_SICHTRAHMEN] = true;
			} else if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_ABRUF)) {
				aPanelsEnabled[IDX_PANEL_SICHTLIEFERSTATUS] = true;
				aPanelsEnabled[IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN] = true;
				aPanelsEnabled[IDX_PANEL_SICHTRAHMEN] = true;
			}
			if (!bDarfPreiseSehen)
				aPanelsEnabled[IDX_PANEL_AUFTRAGUEBERSICHT] = false;

			enablePanels();
		}
	}

	private void enablePanels() throws Throwable {
		// wenn es noch keinen Auftrag gibt, werden die Panels from Framework
		// geschalten
		if (auftragAuswahl.getSelectedId() != null) {
			for (int i = 0; i < aPanelsEnabled.length; i++) {

				if (i == IDX_PANEL_AUFTRAGAUSWAHL) {
					setEnabledAt(IDX_PANEL_AUFTRAGAUSWAHL,
							aPanelsEnabled[IDX_PANEL_AUFTRAGAUSWAHL]);
				} else if (i == IDX_PANEL_AUFTRAGKOPFDATEN) {
					setEnabledAt(IDX_PANEL_AUFTRAGKOPFDATEN,
							aPanelsEnabled[IDX_PANEL_AUFTRAGKOPFDATEN]);
				} else if (i == IDX_PANEL_AUFTRAGPOSITIONEN) {
					setEnabledAt(IDX_PANEL_AUFTRAGPOSITIONEN,
							aPanelsEnabled[IDX_PANEL_AUFTRAGPOSITIONEN]);
				} else if (i == IDX_PANEL_AUFTRAGKONDITIONEN) {
					setEnabledAt(IDX_PANEL_AUFTRAGKONDITIONEN,
							aPanelsEnabled[IDX_PANEL_AUFTRAGKONDITIONEN]);
				} else if (i == IDX_PANEL_AUFTRAGTEILNEHMER) {
					setEnabledAt(IDX_PANEL_AUFTRAGTEILNEHMER,
							aPanelsEnabled[IDX_PANEL_AUFTRAGTEILNEHMER]);
				} else if (i == IDX_PANEL_SICHTLIEFERSTATUS) {
					setEnabledAt(IDX_PANEL_SICHTLIEFERSTATUS,
							aPanelsEnabled[IDX_PANEL_SICHTLIEFERSTATUS]);
				} else if (i == IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN) {
					setEnabledAt(
							IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN,
							aPanelsEnabled[IDX_PANEL_SICHTAUFTRAGAUFANDEREBELEGARTEN]);
				} else if (i == IDX_PANEL_SICHTRAHMEN) {
					setEnabledAt(IDX_PANEL_SICHTRAHMEN,
							aPanelsEnabled[IDX_PANEL_SICHTRAHMEN]);
				} else if (i == IDX_PANEL_AUFTRAGUEBERSICHT) {
					setEnabledAt(IDX_PANEL_AUFTRAGUEBERSICHT,
							aPanelsEnabled[IDX_PANEL_AUFTRAGUEBERSICHT]);
				} else if (i == IDX_PANEL_AUFTRAGZEITEN) {
					setEnabledAt(IDX_PANEL_AUFTRAGZEITEN,
							aPanelsEnabled[IDX_PANEL_AUFTRAGZEITEN]);
				} else if (i == IDX_PANEL_LSRE) {
					setEnabledAt(IDX_PANEL_LSRE, aPanelsEnabled[IDX_PANEL_LSRE]);
				} else if (i == IDX_PANEL_LOSE) {
					setEnabledAt(IDX_PANEL_LOSE, aPanelsEnabled[IDX_PANEL_LOSE]);
				} else if (i == IDX_PANEL_AUFTRAGEIGENSCHAFTEN) {
					setEnabledAt(IDX_PANEL_AUFTRAGEIGENSCHAFTEN,
							aPanelsEnabled[IDX_PANEL_AUFTRAGEIGENSCHAFTEN]);
				} else if (i == IDX_PANEL_ZEITPLAN) {
					setEnabledAt(IDX_PANEL_ZEITPLAN,
							aPanelsEnabled[IDX_PANEL_ZEITPLAN]);
				} else if (i == IDX_PANEL_ZAHLUNGSPLAN) {
					setEnabledAt(IDX_PANEL_ZAHLUNGSPLAN,
							aPanelsEnabled[IDX_PANEL_ZAHLUNGSPLAN]);
				} else if (i == IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN) {
					setEnabledAt(IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN,
							aPanelsEnabled[IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN]);
				}

			}
		}
	}

	/*
	 * public void showPanelDialogDivisor() throws Throwable { if (pdDivisor ==
	 * null) { pdDivisor = new PanelDialogAuftragDivisor( getInternalFrame(),
	 * LPMain.getTextRespectUISPr("bes.divisor")); }
	 * 
	 * this.setTitleAuftrag("bes.divisor");
	 * getInternalFrame().showPanelDialog(pdDivisor); }
	 */
	public void gotoSichtRahmen() throws Throwable {
		setSelectedComponent(refreshAuftragSichtRahmen());
		setEnabledAt(IDX_PANEL_SICHTRAHMEN, true);
		setTitleAuftrag(LPMain.getTextRespectUISPr("lp.sichtrahmen"));

		// alles richtig schalten, auch wenn die Liste der Abrufpositionen
		// vorher leer war
		auftragSichtRahmen.eventYouAreSelected(false); // refresh auf das
		// gesamte 1:n panel
	}

	public PanelAuftragKonditionen2 getAuftragKonditionen() {
		return auftragKonditionen;
	}

	/**
	 * belegartkonditionen: 6 Kopf- und Fusstext vorbelegen.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void initAuftragtexte() throws Throwable {
		if (kopftextDto == null || kopftextDto.getIId() == null) {
			kopftextDto = DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.getAufragkopfDefault(
							getKundeAuftragDto().getPartnerDto()
									.getLocaleCNrKommunikation());
		}

		if (fusstextDto == null || fusstextDto.getIId() == null) {
			fusstextDto = DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.getAufragfussDefault(
							getKundeAuftragDto().getPartnerDto()
									.getLocaleCNrKommunikation());
		}
	}

	// TODO-AGILCHANGES8
	/**
	 * AGILPRO CHANGES BEGIN
	 * 
	 * @author Lukas Lisowski
	 */

	/**
	 * @return the auftragPositionen
	 */
	public PanelSplit getPanelPositionen() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGPOSITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.auftragPositionen;
	}

	/**
	 * @return the auftragSichtRahmen
	 */
	public PanelSplit getPanelSichtRahmen() {
		this.setSelectedIndex(IDX_PANEL_SICHTRAHMEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.auftragSichtRahmen;
	}

	/**
	 * @return the auftragTeilnehmer
	 */
	public PanelSplit getPanelTeilnehmer() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGTEILNEHMER);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.auftragTeilnehmer;
	}

	/**
	 * 
	 * @return com.lp.client.frame.component.PanelQuery
	 */
	public PanelQuery getPanelAuswahl() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGAUSWAHL);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return auftragAuswahl;
	}

	/**
	 * 
	 * @return com.lp.client.frame.component.PanelBasis
	 */
	public PanelAuftragKopfdaten getPanelKopfdaten() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGKOPFDATEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return auftragKopfdaten;
	}

	/**
	 * 
	 * @return com.lp.client.frame.component.PanelBasis
	 */
	public PanelBasis getPanelKonditionen() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGKONDITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return auftragKonditionen;
	}

	/**
	 * AGILPRO CHANGES END
	 * 
	 * @return Object
	 */
	public Object getDto() {
		return auftragDto;
	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object aoIIdPosition[] = this.auftragPositionenTop.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			AuftragpositionDto[] dtos = new AuftragpositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory
						.getInstance()
						.getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey(
								(Integer) aoIIdPosition[i]);
			}

			if (getAuftragPositionenBottom().getArtikelsetViewController()
					.validateCopyConstraintsUI(dtos)) {
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}

	}

	public boolean warnungAuftragspositionOhneRahmenbezug(
			AuftragpositionDto auftragpositionDto) throws Throwable {
		if (auftragpositionDto.getIId() == null) {
			if (auftragpositionDto.getPositionsartCNr().equals(
					LocaleFac.POSITIONSART_IDENT)
					|| auftragpositionDto.getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_HANDEINGABE)) {
				if (getAuftragDto().getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)) {
					// einmalige Warnung aussprechen
					if (!getInternalFrameAuftrag().bWarnungAusgesprochen) {
						getInternalFrameAuftrag().bWarnungAusgesprochen = true;
						if (DialogFactory
								.showMeldung(
										LPMain.getTextRespectUISPr("lp.warning.reduziertnichtdieoffenemengeimrahmen"),
										LPMain.getTextRespectUISPr("lp.warning"),
										javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {

							return false;
						}

					}
				}
			}

		}
		return true;
	}

	public void einfuegenHV() throws IOException, ExceptionLP,
			ParserConfigurationException, Throwable, SAXException {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		if (!getAuftragPositionenBottom().getArtikelsetViewController()
				.validatePasteConstraintsUI(o)) {
			return;
		}

		if (o instanceof BelegpositionDto[]) {
			AuftragpositionDto[] positionDtos = DelegateFactory.getInstance()
					.getBelegpostionkonvertierungDelegate()
					.konvertiereNachAuftragpositionDto((BelegpositionDto[]) o);

			if (positionDtos != null) {
				Integer iId = null;
				Boolean b = positionAmEndeEinfuegen();
				if (b != null) {
					for (int i = 0; i < positionDtos.length; i++) {
						AuftragpositionDto positionDto = positionDtos[i];
						try {
							positionDto.setIId(null);

							boolean bSpeichern = warnungAuftragspositionOhneRahmenbezug(positionDto);
							if (bSpeichern == false) {
								break;
							}

							// damits hinten angehaengt wird.
							if (b == false) {
								Integer iIdAktuellePosition = (Integer) getAuftragPositionenTop()
										.getSelectedId();

								// die erste Position steht an der Stelle 1
								Integer iSortAktuellePosition = new Integer(1);

								if (iIdAktuellePosition != null) {
									AuftragpositionDto aktuellePositionDto = DelegateFactory
											.getInstance()
											.getAuftragpositionDelegate()
											.auftragpositionFindByPrimaryKey(
													iIdAktuellePosition);
									iSortAktuellePosition = aktuellePositionDto
											.getISort();

									// iSortAktuellePosition = DelegateFactory
									// .getInstance()
									// .getAuftragpositionDelegate()
									// .auftragpositionFindByPrimaryKey(
									// iIdAktuellePosition)
									// .getISort();

									// poseinfuegen: 4 Die bestehenden
									// Positionen
									// muessen Platz fuer die neue schaffen
									DelegateFactory
											.getInstance()
											.getAuftragpositionDelegate()
											.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
													getAuftragDto().getIId(),
													iSortAktuellePosition
															.intValue());

									getAuftragPositionenBottom()
											.getArtikelsetViewController()
											.setArtikelSetIIdFuerNeuePosition(
													aktuellePositionDto
															.getPositioniIdArtikelset());
									// poseinfuegen: 5 Die neue Position wird an
									// frei
									// gemachte Position gesetzt
									positionDto.setISort(iSortAktuellePosition);
								} else {
									positionDto.setISort(null);
								}

							} else {
								positionDto.setISort(null);
							}

							positionDto.setBelegIId(this.getAuftragDto()
									.getIId());

							// Positionstermin auf Liefertermin setzen, wenn sie
							// nicht gleich sind.
							if (positionDto.getTUebersteuerbarerLiefertermin() == null) {
								positionDto
										.setTUebersteuerbarerLiefertermin(this
												.getAuftragDto()
												.getDLiefertermin());
							}

							// wir legen eine neue position an
							ArtikelsetViewController viewController = getAuftragPositionenBottom()
									.getArtikelsetViewController();

							boolean bDiePositionSpeichern = viewController
									.validateArtikelsetConstraints(positionDto);

							if (bDiePositionSpeichern) {
								positionDto
										.setPositioniIdArtikelset(viewController
												.getArtikelSetIIdFuerNeuePosition());
								Integer newIId = DelegateFactory.getInstance()
										.getAuftragpositionDelegate()
										.createAuftragposition(positionDto);
								positionDto.setIId(newIId);
								if (iId == null) {
									iId = newIId;
								}
							}
						} catch (Throwable t) {
							// nur loggen!
							myLogger.error(t.getMessage(), t);
						}
					}
				}

				ZwsEinfuegenHVAuftragposition cpp = new ZwsEinfuegenHVAuftragposition();
				cpp.processZwsPositions(positionDtos, (BelegpositionDto[]) o);
				// processZwsPositions(positionDtos, (BelegpositionDto[]) o) ;

				// die Liste neu aufbauen
				auftragPositionenTop.eventYouAreSelected(false);
				// den Datensatz in der Liste selektieren
				auftragPositionenTop.setSelectedId(iId);
				// im Detail den selektierten anzeigen
				auftragPositionenBottom.eventYouAreSelected(false);
			}

		}
	}

	// private void processZwsPositions(AuftragpositionDto[] positionsDto,
	// BelegpositionDto[] source) throws ExceptionLP {
	// for(int i = 0; i < source.length; i++) {
	// if(!AuftragServiceFac
	// .AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME.equals(source[i].getPositionsartCNr()))
	// continue ;
	// Integer vonId = ((BelegpositionVerkaufDto)source[i]).getZwsVonPosition()
	// ;
	// if(vonId == null) continue ;
	// Integer vonIndex = findArrayIndexFor(vonId, source) ;
	// if(vonIndex == null) continue ;
	//
	// positionsDto[i].setZwsVonPosition(positionsDto[vonIndex].getIId()) ;
	//
	// Integer bisId = ((BelegpositionVerkaufDto)source[i]).getZwsBisPosition()
	// ;
	// if(bisId == null) continue ;
	// Integer bisIndex = findArrayIndexFor(bisId, source) ;
	// if(bisIndex == null) continue ;
	//
	// positionsDto[i].setZwsBisPosition(positionsDto[bisIndex].getIId()) ;
	//
	// DelegateFactory.getInstance().getAuftragpositionDelegate().updateAuftragpositionOhneWeitereAktion(positionsDto[i])
	// ;
	// }
	// }
	//
	// private Integer findArrayIndexFor(Integer iid, BelegpositionDto[] source)
	// {
	// for(int i = 0; i < source.length; i++) {
	// if(source[i].getIId().equals(iid)) return i ;
	// }
	// return null ;
	// }

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI)
			throws Throwable {
		AuftragpositionDto abPosDto = (AuftragpositionDto) belegposDtoI;

		abPosDto.setBelegIId(auftragDto.getIId());
		abPosDto.setISort(xalOfBelegPosI + 1000);
	}

	private void importCSVAuftragPositionen() throws Throwable {

		File[] files = HelperClient.chooseFile(this,
				HelperClient.FILE_FILTER_CSV, false);
		File f = null;
		if (files != null && files.length > 0) {
			f = files[0];
		}
		if (f != null) {
			ArrayList<String[]> al = null;
			LPCSVReader reader = null;
			reader = new LPCSVReader(new FileReader(f), ';');
			al = (ArrayList<String[]>) reader.readAll();
			reader.close();
			StringBuffer err = new StringBuffer();
			if (al.size() > 0) {
				for (int i = 1; i < al.size(); i++) {
					try {
						AuftragpositionDto apo = new AuftragpositionDto();
						apo.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
						apo.setBelegIId(auftragDto.getIId());
						String[] as = al.get(i);
						ArtikelDto artikelDto = null;
						try {
							artikelDto = DelegateFactory.getInstance()
									.getArtikelDelegate()
									.artikelFindByCNr(as[0]);
						} catch (ExceptionLP e) {
							err.append("Zeile " + i + ": " + "Artikel " + as[0]
									+ " nicht gefunden.\r\n");
						}
						if (artikelDto != null) {
							apo.setArtikelIId(artikelDto.getIId());
							apo.setEinheitCNr(artikelDto.getEinheitCNr());
							// apo.setMwstsatzIId(artikelDto.getM)
							MwstsatzDto mwstsatzDto = DelegateFactory
									.getInstance()
									.getMandantDelegate()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeAuftragDto.getMwstsatzbezIId());
							apo.setMwstsatzIId(mwstsatzDto.getIId());
							apo.setNMenge(new BigDecimal(as[1]));
							apo.setNOffeneMenge(new BigDecimal(as[1]));
							apo.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);

							apo.setTUebersteuerbarerLiefertermin(auftragDto
									.getDLiefertermin());

							PanelPositionenArtikelVerkauf ppav = (PanelPositionenArtikelVerkauf) this.auftragPositionenBottom.panelArtikel;
							ppav.setArtikelDto(artikelDto);
							ppav.berechneVerkaufspreis(mwstsatzDto.getIId(),
									new Double(1), false);
							// VkpreisfindungDto vkpfDto =
							// ppav.getVkpreisfindungDto();
							VerkaufspreisDto vkDto = ppav.verkaufspreisDtoInZielwaehrung;
							apo.setNNettoeinzelpreis(vkDto.nettopreis);
							apo.setNBruttoeinzelpreis(vkDto.bruttopreis);
							apo.setNEinzelpreis(vkDto.einzelpreis);
							apo.setFRabattsatz(vkDto.rabattsatz);
							apo.setNMaterialzuschlag(vkDto.bdMaterialzuschlag);

							apo.setNMwstbetrag(vkDto.mwstsumme);
							apo.setNRabattbetrag(vkDto.rabattsumme);

							apo.setFZusatzrabattsatz(vkDto
									.getDdZusatzrabattsatz());
							apo.setBNettopreisuebersteuert(Helper
									.boolean2Short(false));
							apo.setBMwstsatzuebersteuert(Helper
									.boolean2Short(false));
							apo.setBArtikelbezeichnunguebersteuert(Helper
									.boolean2Short(false));

							DelegateFactory.getInstance()
									.getAuftragpositionDelegate()
									.createAuftragposition(apo);
						}
					} catch (ExceptionLP e1) {
						err.append("Zeile " + i + ": " + e1.getSMsg() + " / "
								+ e1.getMessage() + "\n\r");
					} catch (Exception e2) {
						err.append("Zeile " + i + ": " + e2.getMessage()
								+ "\n\r");
					}
				}
				if (err.length() > 0) {
					JOptionPane.showMessageDialog(this, err,
							"Fehler beim Import", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this,
							"Daten erfolgreich importiert",
							"Auftragsposition-Import",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

}
