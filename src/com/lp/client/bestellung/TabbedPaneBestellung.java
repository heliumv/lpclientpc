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
package com.lp.client.bestellung;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.anfrage.AnfrageFilterFactory;
import com.lp.client.anfrage.PanelAnfrageKopfdaten;
import com.lp.client.artikel.DialogLagercockpitUmbuchen;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ArtikelsetViewController;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BSPOSDocument2BSPOSDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellungsartDto;
import com.lp.server.bestellung.service.BestellungtextDto;
import com.lp.server.bestellung.service.ImportMonatsbestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.service.POSDocument2POSDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um die Bestellung.</I> </p> <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>01.02.05</I></p> <p> </p>
 * 
 * @author Josef Erlinger
 * 
 * @version $Revision: 1.65 $
 */
public class TabbedPaneBestellung
// copypaste: 0 Interface Implementieren
		extends TabbedPane implements ICopyPaste {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelBestellungAuswahlQP1 = null;
	private PanelBestellungKopfdaten panelBestellungKopfdatenD = null;

	private PanelQuery panelBestellungPositionenTopQP3 = null;
	private PanelBestellungPositionen panelBestellungPositionenBottomD3 = null;
	private PanelSplit panelBestellungPositionenSP3 = null; // FLR 1:n Liste

	private PanelBestellungKonditionen panelBestellungKonditionenD4 = null;

	private PanelQuery panelBestellungWareneingangTopQP4 = null;
	private PanelBasis panelBestellungWareneingangBottomD4 = null;
	private PanelSplit panelBestellungWareineingangSP4 = null;

	private PanelQuery panelBestellungWEPTopQP5 = null;
	private PanelBasis panelBestellungWEPBottomD5 = null;
	private PanelSplit panelBestellungWEPSP5 = null;

	public String sRechtModulweit = null;

	private static final String MY_OWN_NEW_ALLE_ANGELEGTEN_BESTELLUNGEN_VERSENDEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "_ANGELEGTEN_BESTELLUNGEN_VERSENDEN";

	private static final String ACTION_SPECIAL_SORTIERE_NACH_IDENT = "ACTION_SPECIAL_SORTIERE_NACH_IDENT";
	private final String BUTTON_SORTIERE_NACH_IDENT = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SORTIERE_NACH_IDENT;

	private static final String ACTION_SPECIAL_NEU_AUS_PROJEKT = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_NEU_AUS_PROJEKT";

	private PanelQuery panelBestellungPositionSichtRahmenTopQP6 = null; // FLR
	// 1:n
	// Liste
	private PanelBestellungPositionSichtRahmen panelBestellungPositionSichtRahmenBottomD6 = null;
	private PanelSplit panelBestellungPositionSichtRahmenSP6 = null;

	private PanelQuery panelBestellungSichtLieferTermineTopQP7 = null;
	private PanelBasis panelBestellungSichtLieferTermineBottomD7 = null;
	private PanelSplit panelBestellungSichtLieferTermineSP7 = null;

	private PanelQueryFLR panelQueryFLRBestellungauswahl = null;

	private PanelDialogKriteriensetABTermin pdABTerminABKommentar = null;

	// Je ein Panel fuer internen und externen Kommentar
	private PanelDialogBestellungInternExternKommentar pdBestellungInternerKommentar = null;
	private PanelDialogBestellungInternExternKommentar pdBestellungExternerKommentar = null;

	private PanelDialogBestellungWEPInternKommentar pdBestellungWEPInternerKommentar = null;

	private PanelQueryFLR panelQueryFLRAnfrageAuswahl = null;
	private PanelQueryFLR panelBestellungAuswahlNeuAus = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;

	private PanelQueryFLR panelQueryFLRLieferant = null;
	private PanelQueryFLR panelQueryFLRProjekt = null;

	private final static String MY_OWN_NEW_NEU_AUS_ANFRAGE = PanelBasis.ACTION_MY_OWN_NEW
			+ "extra_neu_aus_anfrage";
	private final static String MY_OWN_NEW_ABRUF_ZU_RAHMEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "extra_abruf_zu_rahmen";
	private final static String MY_OWN_NEW_ALLE_ABTERMINE_VORBELEGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "extra_termin_zu_bestellung";
	private final static String MY_OWN_NEW_BESTELLUNG_AUS_BESTELLUNG = PanelBasis.ACTION_MY_OWN_NEW
			+ "extra_bestellung_aus_bestellung";

	public final static String MY_OWN_NEW_NEUER_WARENEINGANG_AUS_WEP = PanelBasis.ACTION_MY_OWN_NEW
			+ "neuer_wareneingang_aus_wep";

	// Je ein Menueeintrag fuer internen und externen Kommentar
	private final static String MENU_BEARBEITEN_INTERNER_KOMMENTAR = "MENU_BEARBEITEN_INTERNER_KOMMENTAR";
	private final static String MENU_BEARBEITEN_EXTERNER_KOMMENTAR = "MENU_BEARBEITEN_EXTERNER_KOMMENTAR";
	private final static String MENU_BEARBEITEN_WEP_INTERNER_KOMMENTAR = "MENU_BEARBEITEN_WEP_INTERNER_KOMMENTAR";
	private final static String MENU_ACTION_BEARBEITEN_BSMAHNSPERRE_SETZEN = "MENUE_ACTION_BEARBEITEN_BSMAHNSPERRE_SETZEN";
	private final static String MENU_ACTION_ALLE = "menu_action_bes_alle";
	private final static String MENU_ACTION_JOURNAL_OFFENE = "MENU_ACTION_JOURNAL_OFFENE";
	private final static String MENU_ACTION_JOURNAL_WARENEINGANG = "MENU_ACTION_JOURNAL_WARENEINGANG";
	private final static String MENU_ACTION_BEARBEITEN_AUFTRAGSZUORDNUNG = "MENU_ACTION_BEARBEITEN_AUFTRAGSZUORDNUNG";
	private final static String MENU_ACTION_BEARBEITEN_OFFENE_WEPOS = "MENU_ACTION_BEARBEITEN_OFFENE_WEPOS";

	public final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_KOPFDATEN = 1;
	public final static int IDX_PANEL_BESTELLPOSITION = 2;
	private final static int IDX_PANEL_KONDITIONEN = 3;
	private final static int IDX_PANEL_WARENEINGANG = 4;
	public final static int IDX_PANEL_WARENEINGANGSPOSITIONEN = 5;
	public final static int IDX_PANEL_BESTELLPOSITIONSICHTRAHMEN = 6;
	private final static int IDX_PANEL_SICHTLIEFERTERMINE = 7;

	public final static String EXTRA_NEU_NEUE_ZEILE_VOR_BESTEHENDER = "neue_zeile_vor_bestehender";
	public final static String EXTRA_NEU_ALLE_POSITIONEN_AUS_WEP_UEBERNEHMEN = "extra_neu_alle_positionen_aus_wep_uebernehmen";
	public final static String EXTRA_NEU_ALLE_PREISE_ERFASSEN = "exra_neu_alle_preise_erfassen";

	private final static String MY_OWN_NEW_EXTRA_NEU_ALLE_POSITIONEN_AUS_WEP_UEBERNEHMEN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_ALLE_POSITIONEN_AUS_WEP_UEBERNEHMEN;
	private final static String MY_OWN_NEW_EXTRA_NEU_ALLE_PREISE_ERFASSEN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_ALLE_PREISE_ERFASSEN;

	private final static String FUER_MEHRERE_BESTELLUNGEN_ALLE_PREISE_ERFASSEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "FUER_MEHRER_BESTELLUNGEN_ALLE_PREISE_ERFASSEN";

	// dtos, die in mehr als einem panel benoetigt werden
	private BestellungDto besDto = null;
	private BestellungDto rahmBesDto = null;
	private LieferantDto lieferantDto = null;
	private PartnerDto partnerDto = null;
	private WareneingangDto wareneingangDto = null;
	private WareneingangspositionDto wepDto = null;

	private static final String MENU_ACTION_DATEI_BESTELLUNG = "MENU_ACTION_DATEI_BESTELLUNG";
	private static final String MENU_ACTION_DATEI_ABHOLAUFTRAG = "MENU_ACTION_DATEI_ABHOLAUFTRAG";
	private static final String MENU_ACTION_DATEI_MONATSBESTELLUNG = "MENU_ACTION_DATEI_MONATSBESTELLUNG";
	private static final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN";
	private static final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN";
	private static final String MENU_BEARBEITEN_STORNO_AUFHEBEN = "MENU_BEARBEITEN_STORNO_AUFHEBEN";

	private boolean lieferantenTermin = true;

	private boolean bHatRahmenBestellungen = false;

	// private static final String

	/**
	 * @todo MB wieso die 2 hier speichern?
	 */
	private Date abDate = null;
	private String abNummer = null;

	private boolean bWechselkurseOK = true;

	private BestellungtextDto kopftextDto = null;
	private BestellungtextDto fusstextDto = null;

	protected void setBooleanVonSichtLieferantenTermin(
			boolean lieferantenTerminI) {
		lieferantenTermin = lieferantenTerminI;
	}

	protected boolean getBooleanVonSichtLieferantenTermin() {
		return lieferantenTermin;
	}

	public PanelQuery getBestellungPositionenTop() {
		return panelBestellungPositionenTopQP3;
	}

	public PanelBasis getBestellungPositionenBottom() {
		return panelBestellungPositionenBottomD3;
	}

	public PanelQuery getWareneingangTop() {
		return panelBestellungWareneingangTopQP4;
	}

	protected void setBestellungDto(BestellungDto oBestellungDtoI)
			throws Throwable {
		besDto = oBestellungDtoI;

		// Lieferant der Bestellung holen
		if (getBesDto() != null
				&& getBesDto().getLieferantIIdBestelladresse() != null) {
			// nur dann, wenn ich den noch nicht hab
			if (getLieferantDto() == null
					|| getLieferantDto().getIId() == null
					|| !getLieferantDto().getIId().equals(
							getBesDto().getLieferantIIdBestelladresse())) {
				this.setLieferantDto(DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								getBesDto().getLieferantIIdBestelladresse()));
			}
		} else {
			this.setLieferantDto(null);
		}
	}

	public BestellungDto getBesDto() {
		return besDto;
	}

	public WareneingangDto getWareneingangDto() {
		return wareneingangDto;
	}

	public void setWareneingangDto(WareneingangDto wareneingangDto) {
		this.wareneingangDto = wareneingangDto;
	}

	public WareneingangspositionDto getWareneingangspositionenDto() {
		return wepDto;
	}

	public void setWareneingangspositionenDto(
			WareneingangspositionDto wareneingangspositionenDto) {
		this.wepDto = wareneingangspositionenDto;
	}

	private void enableSichtRahmen(boolean enableI) {
		setEnabledAt(IDX_PANEL_BESTELLPOSITIONSICHTRAHMEN, enableI);
	}

	private void enableWE(boolean enableI) {
		setEnabledAt(IDX_PANEL_WARENEINGANG, enableI);
	}

	private void enableBSPOS(boolean enableI) {
		setEnabledAt(IDX_PANEL_BESTELLPOSITION, enableI);
	}

	private void enableKonditionen(boolean enableI) {
		setEnabledAt(IDX_PANEL_KONDITIONEN, enableI);
	}

	private void enableSichLieferantenTermine(boolean enableI) {
		setEnabledAt(IDX_PANEL_SICHTLIEFERTERMINE, enableI);
	}

	public PanelBestellungPositionen getPanelBestellungPositionenBottomD3() {
		return this.panelBestellungPositionenBottomD3;
	}

	public TabbedPaneBestellung(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("best.title.bestellung"));
		sRechtModulweit = getInternalFrame().getRechtModulweit();
		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {

		BestellungsartDto[] arten = DelegateFactory.getInstance()
				.getBestellungServiceDelegate().getAllBestellungsArt();
		for (int i = 0; i < arten.length; i++) {
			if (BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR
					.equals(arten[i].getCNr())) {
				bHatRahmenBestellungen = true;
			}
		}

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"), IDX_PANEL_AUSWAHL);
		// 2 tab oben: D2 die restlichen Panels erst bei Bedarf laden
		insertTab(LPMain.getTextRespectUISPr("bes.title.panel.kopfdaten"),
				null, null,
				LPMain.getTextRespectUISPr("bes.title.tooltip.kopfdaten"),
				IDX_PANEL_KOPFDATEN);
		// 3 tab oben: D2 die restlichen Panels erst bei Bedarf laden
		insertTab(LPMain.getTextRespectUISPr("bes.title.panel.positionsdaten"),
				null, null,
				LPMain.getTextRespectUISPr("bes.title.panel.positionsdaten"),
				IDX_PANEL_BESTELLPOSITION);
		insertTab(LPMain.getTextRespectUISPr("bes.title.panel.konditionen"),
				null, null,
				LPMain.getTextRespectUISPr("bes.title.panel.konditionen"),
				IDX_PANEL_KONDITIONEN);
		insertTab(LPMain.getTextRespectUISPr("bes.title.panel.wareneingang"),
				null, null,
				LPMain.getTextRespectUISPr("bes.title.panel.wareneingang"),
				IDX_PANEL_WARENEINGANG);
		insertTab(
				LPMain.getTextRespectUISPr("bes.title.panel.wareneingangspositionen"),
				null,
				null,
				LPMain.getTextRespectUISPr("bes.title.panel.wareneingangspositionen"),
				IDX_PANEL_WARENEINGANGSPOSITIONEN);
		insertTab(LPMain.getTextRespectUISPr("lp.sichtrahmen"), null, null,
				LPMain.getTextRespectUISPr("lp.sichtrahmen"),
				IDX_PANEL_BESTELLPOSITIONSICHTRAHMEN);
		insertTab(
				LPMain.getTextRespectUISPr("bes.title.panel.sichtliefertermine"),
				null,
				null,
				LPMain.getTextRespectUISPr("bes.title.panel.sichtliefertermine"),
				IDX_PANEL_SICHTLIEFERTERMINE);
		setSelectedComponent(panelBestellungAuswahlQP1);

		// panel anlegen
		refreshBestellungAuswahl();
		panelBestellungAuswahlQP1.eventYouAreSelected(false);

		// QP1 ist default.
		setSelectedComponent(panelBestellungAuswahlQP1);

		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelBestellungAuswahlQP1.getSelectedId() == null) {
			getInternalFrame().enableAllPanelsExcept(false);
		}

		// damit die Kopfdaten einen aktuellen Datenssatz haben.
		ItemChangedEvent it = new ItemChangedEvent(panelBestellungAuswahlQP1,
				ItemChangedEvent.ITEM_CHANGED);

		lPEventItemChanged(it);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void setLieferantDto(LieferantDto dto) {
		lieferantDto = dto;
	}

	public LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	public void setRechnungPartnerDto(PartnerDto dto) {
		partnerDto = dto;
	}

	public PartnerDto getRechnungPartnerDto() {
		return partnerDto;
	}

	public String getABNummer() {
		return abNummer;
	}

	public void setABNummer(String abNummer) {
		this.abNummer = abNummer;
	}

	public void setABDate(Date abDate) {
		this.abDate = abDate;
	}

	public java.sql.Date getABDate() {
		return abDate;
	}

	private void dialogQueryBestellungFromListe() throws Throwable {
		Integer rahmbesIId = null;
		if (getInternalFrameBestellung().getTabbedPaneBestellung()
				.getRahmBesDto() != null) {
			rahmbesIId = getInternalFrameBestellung().getTabbedPaneBestellung()
					.getRahmBesDto().getIId();
		}
		FilterKriterium[] fk = BestellungFilterFactory
				.getInstance()
				.createFKOffenBestellungenOhneRahmenMitGelifertenEinesMandanten();
		panelQueryFLRBestellungauswahl = BestellungFilterFactory.getInstance()
				.createPanelFLRBestellung(getInternalFrame(), false, false, fk,
						rahmbesIId);
		panelQueryFLRBestellungauswahl.setMultipleRowSelectionEnabled(true);
		new DialogQuery(panelQueryFLRBestellungauswahl);
	}

	public void refreshBestellungAuswahl() throws Throwable {

		QueryType[] qtAuswahl = BestellungFilterFactory.getInstance()
				.buildQueryTypesBestellungAuswahl();

		if (panelBestellungAuswahlQP1 == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			FilterKriterium[] filterAuswahl = BestellungFilterFactory
					.getInstance().getDefaultFilterBestellungAuswahl();

			panelBestellungAuswahlQP1 = new PanelQuery(qtAuswahl,
					filterAuswahl, QueryParameters.UC_ID_BESTELLUNG,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true);

			// Hier den zusaetzlichen Button aufs Panel bringen
			panelBestellungAuswahlQP1.createAndSaveAndShowButton(
					"/com/lp/client/res/note_find16x16.png",
					LPMain.getTextRespectUISPr("bes.bestellungausanfrage"),
					MY_OWN_NEW_NEU_AUS_ANFRAGE,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
			if (bHatRahmenBestellungen) {
				panelBestellungAuswahlQP1.createAndSaveAndShowButton(
						"/com/lp/client/res/abruf.png",
						LPMain.getTextRespectUISPr("bes.abrufzurahmen"),
						MY_OWN_NEW_ABRUF_ZU_RAHMEN,
						RechteFac.RECHT_BES_BESTELLUNG_CUD);
			}
			panelBestellungAuswahlQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/shoppingcart_full16x16.png",
							LPMain.getTextRespectUISPr("bes.bestellung_aus_bestellung"),
							MY_OWN_NEW_BESTELLUNG_AUS_BESTELLUNG,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);
			setComponentAt(IDX_PANEL_AUSWAHL, panelBestellungAuswahlQP1);
			panelBestellungAuswahlQP1.createAndSaveAndShowButton(
					"/com/lp/client/res/text_ok16x16.png",
					LPMain.getTextRespectUISPr("bes.allepreiseerfassen"),
					FUER_MEHRERE_BESTELLUNGEN_ALLE_PREISE_ERFASSEN,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelBestellungAuswahlQP1.befuellePanelFilterkriterienDirekt(
					BestellungFilterFactory.getInstance()
							.getFilterkriteriumDirekt1(),
					BestellungFilterFactory.getInstance()
							.getFilterkriteriumDirekt2());
			panelBestellungAuswahlQP1.addDirektFilter(BestellungFilterFactory
					.getInstance().createFKDProjekt());
			panelBestellungAuswahlQP1.addDirektFilter(BestellungFilterFactory
					.getInstance().createFKDTextSuchen());

			panelBestellungAuswahlQP1
					.befuelleFilterkriteriumSchnellansicht(BestellungFilterFactory
							.getInstance().createFKBestellungSchnellansicht());

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
				panelBestellungAuswahlQP1.createAndSaveAndShowButton(
						"/com/lp/client/res/briefcase2_document16x16.png",
						LPMain.getTextRespectUISPr("angb.neuausprojekt"),
						ACTION_SPECIAL_NEU_AUS_PROJEKT,
						RechteFac.RECHT_BES_BESTELLUNG_CUD);
			}

		}
	}

	private void erstelleBestellungAusProjekt(Integer projektIId)
			throws Throwable {
		PanelBestellungKopfdaten panelKopfdaten = refreshBestellungKopfdaten();
		panelKopfdaten.eventActionNew(new ItemChangedEvent(this,
				ItemChangedEvent.ACTION_NEW), true, false);
		setSelectedComponent(panelKopfdaten);
		panelKopfdaten.setDefaults();

		// Nun noch Projekt/ProjektBezeichnung
		// setzen
		panelKopfdaten.setDefaultsAusProjekt(projektIId);
	}

	private void updateISortButtons() {
		if (panelBestellungPositionenTopQP3.getTable().getSelectedRow() == 0)
			panelBestellungPositionenTopQP3.enableToolsPanelButtons(false,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
		else if (panelBestellungPositionenTopQP3.getTable().getSelectedRow() == panelBestellungPositionenTopQP3
				.getTable().getRowCount() - 1)
			panelBestellungPositionenTopQP3.enableToolsPanelButtons(false,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
	}

	private void handleItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getSource() == panelBestellungAuswahlQP1) {
			// hole key
			Object iId = panelBestellungAuswahlQP1.getSelectedId();

			if (iId != null) {
				initializeDtos((Integer) iId);

				getInternalFrame().setKeyWasForLockMe(iId + "");

				getPanelBestellungKonditionenD4().setKeyWhenDetailPanel(iId);

				refreshBestellungKopfdaten();

				panelBestellungKopfdatenD.setKeyWhenDetailPanel(iId);

				String sWaehrungfBS = getBesDto().getWaehrungCNr();

				((PanelBestellungKopfdaten) panelBestellungKopfdatenD)
						.wechselkurseOKDlg(LPMain.getTheClient()
								.getSMandantenwaehrung(), sWaehrungfBS);

				if (getBesDto() != null) {
					enablePanels(getBesDto(), false);

					setTitle();
				}
			} else {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			panelBestellungAuswahlQP1.updateButtons();
		} else if (eI.getSource() == panelBestellungPositionenTopQP3) {
			Object iId = panelBestellungPositionenTopQP3.getSelectedId();
			panelBestellungPositionenBottomD3.setKeyWhenDetailPanel(iId);
			panelBestellungPositionenBottomD3.eventYouAreSelected(false);
			panelBestellungPositionenTopQP3.updateButtons();
			updateISortButtons();
		}

		else if (eI.getSource() == panelBestellungWareneingangTopQP4) {
			// hole id von Bestellung QP1 und setze es in wareneingangDto
			Integer iIdBestellung = (Integer) panelBestellungAuswahlQP1
					.getSelectedId();
			refreshBestellungWareneingang(getBesDto().getIId());
			Object wareneingangIId = ((ISourceEvent) eI.getSource())
					.getIdSelected();

			panelBestellungWareneingangBottomD4
					.setKeyWhenDetailPanel(wareneingangIId);
			panelBestellungWareneingangBottomD4.eventYouAreSelected(false);

			if (getBesDto().getStatusCNr().equals(
					BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
				panelBestellungWareneingangTopQP4
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_DISABLE_ALL));

				if (panelBestellungWareneingangTopQP4.getHmOfButtons()
						.containsKey(PanelQuery.LEAVEALONE_DOKUMENTE)) {
					LPButtonAction bu = (LPButtonAction) panelBestellungWareneingangTopQP4
							.getHmOfButtons().get(
									PanelQuery.LEAVEALONE_DOKUMENTE);
					bu.setEnable(true);
					bu.getButton().setEnabled(true);
				}

				if (panelBestellungWareneingangTopQP4.getHmOfButtons()
						.containsKey(PanelQuery.LEAVEALONE_PRINTPANELQUERY)) {

					LPButtonAction bu = (LPButtonAction) panelBestellungWareneingangTopQP4
							.getHmOfButtons().get(
									PanelQuery.LEAVEALONE_PRINTPANELQUERY);
					bu.setEnable(true);
					bu.getButton().setEnabled(true);
				}

			} else {
				panelBestellungWareneingangTopQP4
						.updateButtons(panelBestellungWareneingangBottomD4
								.getLockedstateDetailMainKey());
			}

		} else if (eI.getSource() == panelBestellungWEPTopQP5) {
			panelBestellungAuswahlQP1.setUebersteuerteId(null);

			Integer iIdBS = (Integer) panelBestellungAuswahlQP1.getSelectedId();

			refreshBestellungWEP(iIdBS);

			Object iIdBSPOS = ((ISourceEvent) eI.getSource()).getIdSelected();

			// BS refresh wegen moeglicher Statusaednerung
			setBestellungDto(DelegateFactory.getInstance()
					.getBestellungDelegate().bestellungFindByPrimaryKey(iIdBS));

			panelBestellungWEPBottomD5.setKeyWhenDetailPanel(iIdBSPOS);
			panelBestellungWEPBottomD5.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			panelBestellungWEPTopQP5.updateButtons(panelBestellungWEPBottomD5
					.getLockedstateDetailMainKey());

			if (iIdBSPOS != null) {
				// BSPOS holen
				BestellpositionDto bsPos = DelegateFactory.getInstance()
						.getBestellungDelegate()
						.bestellpositionFindByPrimaryKey((Integer) iIdBSPOS);
				if (bsPos.getArtikelIId() == null) {
					Iterator<?> it = panelBestellungWEPBottomD5
							.getHmOfButtons().keySet().iterator();
					while (it.hasNext()) {
						LPButtonAction lpb = panelBestellungWEPBottomD5
								.getHmOfButtons().get(it.next());
						lpb.getButton().setEnabled(false);
					}
					return;
				}
			}

			// Neu Button nur anzeigen, wenn WE noch keine ER hinterlegt
			if (getWareneingangDto().getEingangsrechnungIId() != null) {
				LPButtonAction lpb = panelBestellungWEPTopQP5.getHmOfButtons()
						.get(PanelBasis.ACTION_NEW);
				if (lpb != null) {
					lpb.getButton().setEnabled(false);
				}

			}

		} else if (eI.getSource() == panelBestellungPositionSichtRahmenTopQP6) {
			Integer iIdBesPos = (Integer) panelBestellungPositionSichtRahmenTopQP6
					.getSelectedId();
			panelBestellungPositionSichtRahmenBottomD6
					.setKeyWhenDetailPanel(iIdBesPos);
			panelBestellungPositionSichtRahmenBottomD6
					.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			panelBestellungPositionSichtRahmenTopQP6
					.updateButtons(panelBestellungPositionSichtRahmenBottomD6
							.getLockedstateDetailMainKey());

		} else if (eI.getSource() == this.panelBestellungSichtLieferTermineTopQP7) {
			Integer iIdBestellposition = (Integer) panelBestellungSichtLieferTermineTopQP7
					.getSelectedId();

			panelBestellungSichtLieferTermineBottomD7
					.setKeyWhenDetailPanel(iIdBestellposition);
			panelBestellungSichtLieferTermineBottomD7
					.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			this.panelBestellungSichtLieferTermineTopQP7
					.updateButtons(this.panelBestellungSichtLieferTermineBottomD7
							.getLockedstateDetailMainKey());
		}
	}

	private LockStateValue getLockStateForBSPOSPanel() throws ExceptionLP,
			Throwable {

		LockStateValue lsv = null;
		// je nach Status
		if (getBesDto().getStatusCNr().equals(
				BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
			lsv = new LockStateValue(PanelBasis.LOCK_DISABLE_ALL);
		}

		else if (getBesDto().getStatusCNr().equals(
				BestellungFac.BESTELLSTATUS_OFFEN)) {
			boolean bEnable = getWareneingangDto() == null
					|| getWareneingangDto() != null
					&& getWareneingangDto().getIId() == null
					&& (DelegateFactory.getInstance().getWareneingangDelegate()
							.getAnzahlWE(getBesDto().getIId())
							.compareTo(new Integer(0)) == 0);
			if (!bEnable) {
				lsv = new LockStateValue(PanelBasis.LOCK_DISABLE_ALL);
			} else {
				lsv = panelBestellungPositionenBottomD3
						.getLockedstateDetailMainKey();
			}
		}

		else {
			lsv = panelBestellungPositionenBottomD3
					.getLockedstateDetailMainKey();
		}
		return lsv;
	}

	private void handleActionSave(ItemChangedEvent eI) throws Throwable {

		if (eI.getSource() == panelBestellungPositionenBottomD3) {
			// 1 den Key des Datensatzes merken
			Object oKey = panelBestellungPositionenBottomD3
					.getKeyWhenDetailPanel();

			// wenn der neue Datensatz wirklich angelegt wurde (Abbruch moeglich
			// durch Pruefung auf Unterpreisigkeit)
			if (!oKey.equals(LPMain.getLockMeForNew())) {
				// 2 die Liste neu aufbauen
				panelBestellungPositionenTopQP3.eventYouAreSelected(false);

				// 3 den Datensatz in der Liste selektieren
				panelBestellungPositionenTopQP3.setSelectedId(oKey);
			}
			// 4 im Detail den selektierten anzeigen
			panelBestellungPositionenSP3.eventYouAreSelected(false);
		} else if (eI.getSource() == panelBestellungWEPBottomD5) {
			// refresh auf die liste

			Object oKey = panelBestellungWEPBottomD5.getKeyWhenDetailPanel();
			panelBestellungWEPTopQP5.eventYouAreSelected(false);
			panelBestellungWEPTopQP5.setSelectedId(oKey);
			panelBestellungWEPSP5.eventYouAreSelected(false);

		} else if (eI.getSource() == panelBestellungKopfdatenD) {
			// 2 den Filter im BestellungHandler aktualisieren,
			// wurde ev. bei der Auswahl Rahmenbestellung ueberschrieben
			panelBestellungAuswahlQP1.clearDirektFilter();
			Object oKeyQP1 = panelBestellungKopfdatenD.getKeyWhenDetailPanel();
			panelBestellungAuswahlQP1.setSelectedId(oKeyQP1);
		} else if (eI.getSource() == panelBestellungWareneingangBottomD4) {
			// 1 den Key des Datensatzes merken
			Object oKey = panelBestellungWareneingangBottomD4
					.getKeyWhenDetailPanel();

			// 2 die Liste neu aufbauen
			panelBestellungWareneingangTopQP4.eventYouAreSelected(false);

			// 3 den Datensatz in der Liste selektieren
			panelBestellungWareneingangTopQP4.setSelectedId(oKey);

			// 4 im Detail den selektierten anzeigen
			panelBestellungWareineingangSP4.eventYouAreSelected(false);
		} else if (eI.getSource() == panelBestellungPositionSichtRahmenBottomD6) {
			panelBestellungPositionSichtRahmenSP6.eventYouAreSelected(false);

			// wenn die letzte Position uebernommen wurde, Wechsel auf
			// Positionen
			if (panelBestellungPositionSichtRahmenBottomD6
					.getKeyWhenDetailPanel() == null) {
				getInternalFrame().enableAllPanelsExcept(true);
				refreshBestellungPositionen(getBesDto().getIId());
				setSelectedComponent(panelBestellungPositionenSP3);
			}
		} else if (eI.getSource() == panelBestellungSichtLieferTermineBottomD7) {

			// 1 den Key des Datensatzes merken
			Object oKey = panelBestellungSichtLieferTermineBottomD7
					.getKeyWhenDetailPanel();

			// 2 die Liste neu aufbauen
			panelBestellungSichtLieferTermineTopQP7.eventYouAreSelected(false);

			// 3 den Datensatz in der Liste selektieren
			panelBestellungSichtLieferTermineTopQP7.setSelectedId(oKey);

			// 4 im Detail den selektierten anzeigen
			panelBestellungSichtLieferTermineSP7.eventYouAreSelected(false);
		}
	}

	private void handleActionNew(ItemChangedEvent eI) throws Throwable {

		// if (eI.getSource() == panelBestellungAuswahlQP1) {
		// im QP1 auf new gedrueckt.

		if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			// --NEW
			if (eI.getSource() == panelBestellungAuswahlQP1) {
				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels aktivieren
				if (panelBestellungAuswahlQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBestellungKopfdatenD = refreshBestellungKopfdaten();
				// setBinNew(true);
				panelBestellungKopfdatenD.eventActionNew(eI, true, false);
				setSelectedComponent(panelBestellungKopfdatenD);
			}

			else if (eI.getSource() == panelBestellungPositionenTopQP3) {
				// pqnewnotallowed: 0 das Anlegen einer neuen Position nur
				// ausloesen, wenn es erlaubt is
				// eI.setAcceptFromThisSrc(panelBestellungPositionenTopQP3);
				panelBestellungPositionenBottomD3.eventActionNew(eI, true,
						false);
				panelBestellungPositionenBottomD3.eventYouAreSelected(false);
				setSelectedComponent(panelBestellungPositionenSP3); // ui
				// eI.setAcceptFromThisSrc(null);
			} else if (eI.getSource() == panelBestellungWEPTopQP5) {
				// eI.setAcceptFromThisSrc(panelBestellungWEPTopQP5);
				panelBestellungWEPBottomD5.eventActionNew(eI, true, false);
				panelBestellungWEPBottomD5.eventYouAreSelected(false);
				setSelectedComponent(panelBestellungWEPSP5); // ui
				// eI.setAcceptFromThisSrc(null);
			}

			else if (eI.getSource() == panelBestellungWareneingangTopQP4) {
				// hier wird das alte wareneingangdto ausgeleert und neu
				// angelegt
				Integer iIdBestellung = (Integer) panelBestellungAuswahlQP1
						.getSelectedId();

				setWareneingangDto(new WareneingangDto());
				getWareneingangDto().setBestellungIId(iIdBestellung);
				// eI.setAcceptFromThisSrc(panelBestellungWareneingangTopQP4);
				panelBestellungWareneingangBottomD4.eventActionNew(eI, true,
						false);
				panelBestellungWareneingangBottomD4.eventYouAreSelected(false);
				setSelectedComponent(panelBestellungWareineingangSP4);
				// eI.setAcceptFromThisSrc(null);
			}

			else if (eI.getSource() == panelBestellungSichtLieferTermineTopQP7) {
				if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
					String sAspectInfo = ((ISourceEvent) eI.getSource())
							.getAspect();

					if (sAspectInfo
							.equals(MY_OWN_NEW_ALLE_ABTERMINE_VORBELEGEN)) {
						refreshPanelDialogKriteriensetABTermin();
						getInternalFrame().showPanelDialog(
								pdABTerminABKommentar);
					}
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			// --OWNNEW
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.equals(MY_OWN_NEW_ABRUF_ZU_RAHMEN)) {
				if (eI.getSource() == panelBestellungAuswahlQP1) {

					// eine Abrufbestellung kann nur aus einer Rahmenbestellung
					// generiert werden
					if (isBSangelegtDlg()) {
						if (getBesDto()
								.getBestellungartCNr()
								.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
							if (getBesDto().getStatusCNr().equals(
									BestellungFac.BESTELLSTATUS_OFFEN)
									|| getBesDto()
											.getStatusCNr()
											.equals(BestellungFac.BESTELLSTATUS_BESTAETIGT)) {
								if (DelegateFactory
										.getInstance()
										.getBestellungDelegate()
										.berechneOffeneMenge(
												getBesDto().getIId()) == 0.0) {
									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.hint"),
													LPMain.getTextRespectUISPr("bes.rahmenbestellunggeliefert"));
								} else {
									refreshBestellungKopfdaten();

									// jetzt dafuer sorgen, dass die Buttons
									// richtig geschalten werden
									getInternalFrame().setKeyWasForLockMe(
											LPMain.getLockMeForNew());

									// ZUERST wechseln, denn hier wird die
									// aktuelle Bestellung neu festgelegt
									setSelectedComponent(panelBestellungKopfdatenD);

									panelBestellungKopfdatenD.eventActionNew(
											eI, true, false);
								}
							} else {
								showStatusMessage("lp.hint",
										"bes.rahmenbestellungnichtoffen");
							}
						} else {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("bes.rahmenbestellungwaehlen"));
						}
					}
				}
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_NEU_AUS_PROJEKT)) {
				if (eI.getSource() == panelBestellungAuswahlQP1) {
					dialogQueryProjektFromListe(null);
				}
			}

			else if (sAspectInfo.equals(MY_OWN_NEW_NEU_AUS_ANFRAGE)) {
				if (eI.getSource() == panelBestellungAuswahlQP1) {
					// der Benutzer muss eine Anfrage auswaehlen
					dialogQueryAnfrageFromListe(null);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_BESTELLUNG_AUS_BESTELLUNG)) {
				// Neu aus Bestellung.
				if (eI.getSource() == panelBestellungAuswahlQP1) {
					panelBestellungAuswahlNeuAus = BestellungFilterFactory
							.getInstance()
							.createPanelFLRBestellung(getInternalFrame(), true,
									false, null, null);
					new DialogQuery(panelBestellungAuswahlNeuAus);
				}
			} else if (sAspectInfo
					.equals(MY_OWN_NEW_NEUER_WARENEINGANG_AUS_WEP)) {
				// Neu aus Bestellung.
				if (eI.getSource() == panelBestellungWEPTopQP5) {

					// Neue Wareneingang aus Dialog anlegen
					DialogNeuerWareneingang dialog = new DialogNeuerWareneingang(
							this);
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(dialog);
					dialog.setVisible(true);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_ALLE_ABTERMINE_VORBELEGEN)) {
				refreshPanelDialogKriteriensetABTermin();
				getInternalFrame().showPanelDialog(pdABTerminABKommentar);
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				if (eI.getSource() == panelBestellungPositionenTopQP3) {
					// copypaste: 2
					einfuegenHV();
				}
			} else if (sAspectInfo
					.equals(MY_OWN_NEW_EXTRA_NEU_ALLE_POSITIONEN_AUS_WEP_UEBERNEHMEN)) {
				if (panelBestellungWEPTopQP5.getTable().getRowCount() > 0) {
					if (getWareneingangDto().getIId() != null
							&& getBesDto().getIId() != null) {
						ArrayList<Object[]> alData = DelegateFactory
								.getInstance()
								.getWareneingangDelegate()
								.uebernimmAlleWepsOhneBenutzerinteraktion(
										getWareneingangDto().getIId(),
										getBesDto().getIId());
						// Fehlmengen aufloesen
						if (alData != null) {
							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
									.getInstance()
									.getParameterDelegate()
									.getParametermandant(
											ParameterFac.PARAMETER_FEHLMENGEN_LOSBEZOGEN_SOFORT_AUFLOESEN,
											ParameterFac.KATEGORIE_FERTIGUNG,
											LPMain.getTheClient().getMandant());
							boolean bFehlmengenLosbezogenSofortAufloesen = ((Boolean) parameter
									.getCWertAsObject());

							for (int i = 0; i < alData.size(); i++) {
								Object[] oData = alData.get(i);

								if (bFehlmengenLosbezogenSofortAufloesen == true
										&& oData[3] != null) {
									FehlmengenAufloesen
											.fehlmengenAufloesenMitLosBezug(
													getInternalFrameBestellung(),
													(Integer) oData[0],
													(Integer) oData[1], null,
													(BigDecimal) oData[2],
													(Integer) oData[3]);
								} else {
									FehlmengenAufloesen.fehlmengenAufloesen(
											getInternalFrameBestellung(),
											(Integer) oData[0],
											(Integer) oData[1],
											(String[]) null,
											(BigDecimal) oData[2]);
								}

							}
						}
					}
					panelBestellungWEPSP5.eventYouAreSelected(false);
				}
			} else if (sAspectInfo
					.equals(MY_OWN_NEW_EXTRA_NEU_ALLE_PREISE_ERFASSEN)) {
				if (panelBestellungWEPTopQP5.getTable().getRowCount() > 0) {
					if (getWareneingangDto().getIId() != null
							&& getBesDto().getIId() != null) {
						Integer[] iArtikelIId = DelegateFactory
								.getInstance()
								.getWareneingangDelegate()
								.erfasseAllePreiseOhneBenutzerinteraktion(
										getWareneingangDto().getIId());

						zeigeArtikelhinweiseEingangsrechnung(iArtikelIId, true);
					}
					panelBestellungWEPSP5.eventYouAreSelected(false);
				}
			} else if (sAspectInfo
					.equals(FUER_MEHRERE_BESTELLUNGEN_ALLE_PREISE_ERFASSEN)) {
				dialogQueryBestellungFromListe();
			} else if (sAspectInfo.equals(BUTTON_SORTIERE_NACH_IDENT)) {
				if (istAktualisierenBestellungErlaubt()) {
					DelegateFactory.getInstance().getBestellungDelegate()
							.sortiereNachArtikelnummer(getBesDto().getIId());
					getPanelBestellungPositionenTopQP3().eventYouAreSelected(
							false);
				}
			}
		}
	}

	public void zeigeArtikelhinweiseEingangsrechnung(Integer[] iArtikelIId,
			boolean bMitArtikelCNr) throws ExceptionLP, Throwable {
		for (int y = 0; y < iArtikelIId.length; y++) {
			String[] hinweise = DelegateFactory
					.getInstance()
					.getArtikelkommentarDelegate()
					.getArtikelhinweise(iArtikelIId[y],
							LocaleFac.BELEGART_EINGANGSRECHNUNG);
			if (hinweise != null) {
				ArtikelDto artikelDto = null;
				if (bMitArtikelCNr) {
					artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(iArtikelIId[y]);
				}
				for (int i = 0; i < hinweise.length; i++) {
					String sMessage = Helper.strippHTML(hinweise[i]);
					if (artikelDto != null) {
						sMessage = artikelDto.getCNr() + ": " + sMessage;
					}
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.hinweis"), sMessage);
				}
			}
		}
	}

	private void handleActionGotoMyDefaultQp(ItemChangedEvent eI)
			throws Throwable {
		if (eI.getSource() == panelBestellungKopfdatenD) {
			// 3 die Liste neu laden, falls sich etwas geaendert hat
			panelBestellungAuswahlQP1.eventYouAreSelected(false);

			Integer iId = (Integer) panelBestellungAuswahlQP1.getSelectedId();
			getBesDto().setIId(iId);
			// 4 nach einem Discard ist der aktuelle Key nicht mehr gesetzt
			setKeyWasForLockMe();
			// 5 auf die Auswahl schalten
			setSelectedComponent(panelBestellungAuswahlQP1);
		} else if (eI.getSource() == panelBestellungPositionenBottomD3) {
			// 2 bei einem Neu im 1:n Panel wurde der KeyForLockMe
			// ueberschrieben
			setKeyWasForLockMe();
			if (panelBestellungPositionenBottomD3.getKeyWhenDetailPanel() == null) {
				Object oNaechster = panelBestellungPositionenTopQP3
						.getId2SelectAfterDelete();
				panelBestellungPositionenTopQP3.setSelectedId(oNaechster);
			}
			panelBestellungPositionenSP3.eventYouAreSelected(false); // refresh
			// auf
			// das
			// gesamte
			// 1:n
			// panel

			// if
			// (getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac
			// .
			// BESTELLSTATUS_OFFEN) ||
			// getBestellungDto().getBestellungsstatusCNr().
			// equals(BestellungFac.BESTELLSTATUS_BESTAETIGT)) {
			// enableWareneingangPanels();
			// // this.enablePanels(null);
			// }
			// else {
			// enableWE(false);
			// enableWEP(false);
			// // this.enablePanels(null);
			// }
			// this.enableSichtLieferantTermine();
		}
		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		else if (eI.getSource() == panelBestellungWEPBottomD5) {
			Object oKey = panelBestellungWEPTopQP5.getSelectedId();
			// holt sich alten key wieder
			getInternalFrame().setKeyWasForLockMe(
					(oKey == null) ? null : oKey + "");
			panelBestellungWEPSP5.eventYouAreSelected(false); // refresh auf das
			// gesamte 1:n
			// panel

			panelBestellungWEPSP5.eventYouAreSelected(false); // das 1:n Panel
			// neu aufbauen
		}
		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		else if (eI.getSource() == panelBestellungWareneingangBottomD4) {
			Object oKey = panelBestellungWareneingangTopQP4.getSelectedId();
			// holt sich alten key wieder
			getInternalFrame().setKeyWasForLockMe(
					(oKey == null) ? null : oKey + "");
			panelBestellungWareineingangSP4.eventYouAreSelected(false); // refresh
			// auf
			// das
			// gesamte
			// 1:n
			// panel
		} else if (eI.getSource() == panelBestellungPositionSichtRahmenBottomD6) {
			Object oKey = panelBestellungPositionSichtRahmenTopQP6
					.getSelectedId();
			getInternalFrame().setKeyWasForLockMe(
					(oKey == null) ? null : oKey + "");
			panelBestellungPositionSichtRahmenSP6.eventYouAreSelected(false);
		} else if (eI.getSource() == panelBestellungSichtLieferTermineBottomD7) {
			Object oKey = panelBestellungSichtLieferTermineTopQP7
					.getSelectedId();
			getInternalFrame().setKeyWasForLockMe(
					(oKey == null) ? null : oKey + "");
			panelBestellungSichtLieferTermineSP7.eventYouAreSelected(false);
		}
	}

	public void alleAngelegtenBestellungenDrucken(Integer lieferantIId)
			throws ExceptionLP, Throwable, InterruptedException {

		ArrayList<Integer> a = DelegateFactory.getInstance()
				.getBestellungDelegate()
				.getAngelegtenBestellungen(lieferantIId);

		for (Iterator<Integer> iter = a.iterator(); iter.hasNext();) {
			Integer item = (Integer) iter.next();
			BestellungDto besDto = DelegateFactory.getInstance()
					.getBestellungDelegate().bestellungFindByPrimaryKey(item);

			// Lieferant holen
			LieferantDto lfDto = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							besDto.getLieferantIIdBestelladresse());

			// Die Bestellung muss Positionen haben
			if (DelegateFactory.getInstance().getBestellungDelegate()
					.getAnzahlMengenbehaftetBSPOS(besDto.getIId()) > 0) {
				// DruckPanel instantiieren
				PanelReportKriterien krit = new PanelReportKriterien(
						getInternalFrame(), new ReportBestellung(
								getInternalFrame(), getAktuellesPanel(),
								besDto, lfDto), "", lfDto.getPartnerDto(),
						besDto.getAnsprechpartnerIId(), true, true, false);

				getInternalFrame().showPanelDialog(krit);

			} else {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("bes.keinepositionen")
								+ "\n" + besDto.getCNr());
			}

		}
	}

	public void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame());

		new DialogQuery(panelQueryFLRProjekt);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		// Just for tests to be removed BEGIN
		// Status setzen von Bestellungen getestet
		// if (eI.getSource() == panelBestellungAuswahlQP1) {
		// Object iId = panelBestellungAuswahlQP1.getSelectedId();
		// System.out.println(iId);
		// System.out.println(DelegateFactory.getInstance().getBestellungDelegate
		// ().getRichtigenBestellStatus((Integer) iId,false));
		// }
		// END
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			handleItemChanged(eI);
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelBestellungPositionenBottomD3) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBestellungPositionenTopQP3
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBestellungWareneingangBottomD4) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBestellungWareneingangTopQP4
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBestellungWEPBottomD5) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBestellungWEPTopQP5.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBestellungSichtLieferTermineBottomD7) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBestellungSichtLieferTermineTopQP7
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelBestellungAuswahlQP1) {
				Object iId = panelBestellungAuswahlQP1.getSelectedId();
				if (iId != null) {
					refreshBestellungPositionen(getBesDto().getIId());
					setSelectedComponent(panelBestellungPositionenSP3);
				}
			} else if (eI.getSource() == panelQueryFLRLieferant) {
				Integer lieferantIId = (Integer) panelQueryFLRLieferant
						.getSelectedId();
				alleAngelegtenBestellungenDrucken(lieferantIId);
			} else if (eI.getSource() == panelQueryFLRProjekt) {
				Integer projektIId = (Integer) ((ISourceEvent) eI.getSource())
						.getIdSelected();

				if (projektIId != null) {
					erstelleBestellungAusProjekt(projektIId);
				}
			} else if (eI.getSource() == panelQueryFLRAnfrageAuswahl) {
				// Eine Bestellung aus der gewaehlten Anfrage erzeugen.
				Integer iIdAnfrage = (Integer) ((ISourceEvent) eI.getSource())
						.getIdSelected();

				if (iIdAnfrage != null) {
					resetDtos();
					Integer iIdBestellung = DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.erzeugeBestellungAusAnfrage(iIdAnfrage,
									LPMain.getInstance().getTheClient());
					panelBestellungAuswahlQP1.eventYouAreSelected(false);
					initializeDtos(iIdBestellung);

					// wenn es bisher keine Bestellung gegeben hat
					if (panelBestellungAuswahlQP1.getSelectedId() == null) {
						getInternalFrame().enableAllPanelsExcept(true);
					}
					panelBestellungAuswahlQP1.setSelectedId(iIdBestellung);
					getInternalFrame().setKeyWasForLockMe(iIdBestellung + "");
					setSelectedComponent(refreshBestellungKopfdaten());
				}
			} else if (eI.getSource() == panelQueryFLRAuftrag) {
				Integer iAuftragIid = (Integer) ((ISourceEvent) eI.getSource())
						.getIdSelected();
				getBesDto().setAuftragIId(iAuftragIid);
				DelegateFactory.getInstance().getBestellungDelegate()
						.setAuftragIIdInBestellung(getBesDto());

			} else if (eI.getSource() == panelQueryFLRBestellungauswahl) {
				Object[] ids = panelQueryFLRBestellungauswahl.getSelectedIds();

				BigDecimal gesamtwert = new BigDecimal(0);
				ArrayList<Integer> weIds = new ArrayList<Integer>();
				for (int i = 0; i < ids.length; i++) {

					WareneingangDto[] weDtos = DelegateFactory.getInstance()
							.getWareneingangDelegate()
							.wareneingangFindByBestellungIId((Integer) ids[i]);

					for (int j = 0; j < weDtos.length; j++) {
						BigDecimal einzelwert = getWareneingangWertsumme(weDtos[j]);

						if (einzelwert != null) {
							gesamtwert = gesamtwert.add(einzelwert);
						}
						weIds.add(weDtos[j].getIId());
					}
				}

				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("bes.mehrere.preise.erfasst"));
				mf.setLocale(LPMain.getTheClient().getLocUi());
				Object pattern[] = { Helper.formatZahl(gesamtwert, LPMain
						.getTheClient().getLocUi())
						+ " "
						+ LPMain.getInstance().getTheClient()
								.getSMandantenwaehrung() };
				String sMsg = mf.format(pattern);

				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(), sMsg,
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.QUESTION_MESSAGE, JOptionPane.NO_OPTION);
				if (b == true) {
					Iterator<Integer> it = weIds.iterator();
					while (it.hasNext()) {
						DelegateFactory
								.getInstance()
								.getWareneingangDelegate()
								.erfasseAllePreiseOhneBenutzerinteraktion(
										it.next());
					}
				}
			} else if (eI.getSource() == panelBestellungAuswahlNeuAus) {
				// Neu aus Bestellung

				Integer iIdBestellungBasis = (Integer) ((ISourceEvent) eI
						.getSource()).getIdSelected();

				if (iIdBestellungBasis != null) {

					Integer bestellungIId = DelegateFactory.getInstance()
							.getBestellungDelegate()
							.erzeugeBestellungAusBestellung(iIdBestellungBasis);

					initializeDtos(bestellungIId); // befuellt ...
					getInternalFrame().setKeyWasForLockMe(bestellungIId + "");
					panelBestellungAuswahlQP1.setSelectedId(bestellungIId);

					// wenn es bisher keinen Auftrag gegeben hat
					if (panelBestellungAuswahlQP1.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, false);
					}

					refreshBestellungPositionen(getBesDto().getIId());
					setSelectedComponent(panelBestellungKopfdatenD);

				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (eI.getSource() == panelQueryFLRAuftrag) {
				getBesDto().setAuftragIId(null);
				DelegateFactory.getInstance().getBestellungDelegate()
						.setAuftragIIdInBestellung(getBesDto());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			handleActionGotoMyDefaultQp(eI);
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelBestellungPositionenBottomD3) {
				refreshBestellungPositionen(getBesDto().getIId());

				panelBestellungPositionenTopQP3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBestellungWareneingangBottomD4) {
				Integer iIdBestellungI = getBesDto().getIId();
				refreshBestellungWareneingang(iIdBestellungI);
				panelBestellungWareneingangTopQP4.eventYouAreSelected(false);
			}

			else if (eI.getSource() == panelBestellungWEPBottomD5) {
				Integer iIdBestellungI = getBesDto().getIId();
				refreshBestellungWEP(iIdBestellungI);
				panelBestellungWEPTopQP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBestellungSichtLieferTermineBottomD7) {
				refreshBestellungSichtLieferTermine();
				panelBestellungSichtLieferTermineTopQP7
						.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			handleActionSave(eI);
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW
				|| eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			handleActionNew(eI);
		}

		// Einer der Knoepfe zur Reihung der Positionen auf einem PanelQuery
		// wurde gedrueckt
		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelBestellungPositionenTopQP3) {
				if (getBesDto().getStatusCNr().equals(
						BestellungFac.BESTELLSTATUS_ANGELEGT)) {
					int iPos = panelBestellungPositionenTopQP3.getTable()
							.getSelectedRow();

					// wenn die Position nicht die erste ist
					if (iPos > 0) {
						Integer iIdPosition = (Integer) panelBestellungPositionenTopQP3
								.getSelectedId();

						// Integer iIdPositionMinus1 = (Integer)
						// panelBestellungPositionenTopQP3
						// .getTable().getValueAt(iPos - 1, 0);
						//
						// DelegateFactory
						// .getInstance()
						// .getBestellungDelegate()
						// .vertauscheBestellpositionen(iIdPosition,
						// iIdPositionMinus1);
						//
						TableModel tm = panelBestellungPositionenTopQP3
								.getTable().getModel();
						DelegateFactory.getInstance().getBestellungDelegate()
								.vertauscheBestellpositionMinus(iPos, tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						panelBestellungPositionenTopQP3
								.setSelectedId(iIdPosition);
						updateISortButtons();
					}
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("anf.error.positionenverschieben"));
				}
			} else if (eI.getSource() == panelBestellungWareneingangTopQP4) {
				int iPos = panelBestellungWareneingangTopQP4.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelBestellungWareneingangTopQP4
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelBestellungWareneingangTopQP4
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.vertauscheWareneingang(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelBestellungWareneingangTopQP4
							.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelBestellungPositionenTopQP3) {
				if (getBesDto().getStatusCNr().equals(
						BestellungFac.BESTELLSTATUS_ANGELEGT)) {
					int iPos = panelBestellungPositionenTopQP3.getTable()
							.getSelectedRow();

					// wenn die Position nicht die letzte ist
					if (iPos < panelBestellungPositionenTopQP3.getTable()
							.getRowCount() - 1) {
						Integer iIdPosition = (Integer) panelBestellungPositionenTopQP3
								.getSelectedId();

						// Integer iIdPositionPlus1 = (Integer)
						// panelBestellungPositionenTopQP3
						// .getTable().getValueAt(iPos + 1, 0);

						// DelegateFactory
						// .getInstance()
						// .getBestellungDelegate()
						// .vertauscheBestellpositionen(iIdPosition,
						// iIdPositionPlus1);

						TableModel tm = panelBestellungPositionenTopQP3
								.getTable().getModel();
						DelegateFactory.getInstance().getBestellungDelegate()
								.vertauscheBestellpositionPlus(iPos, tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						panelBestellungPositionenTopQP3
								.setSelectedId(iIdPosition);
						updateISortButtons();
					}
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("anf.error.positionenverschieben"));
				}
			} else if (eI.getSource() == panelBestellungWareneingangTopQP4) {
				int iPos = panelBestellungWareneingangTopQP4.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelBestellungWareneingangTopQP4.getTable()
						.getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelBestellungWareneingangTopQP4
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelBestellungWareneingangTopQP4
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.vertauscheWareneingang(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelBestellungWareneingangTopQP4
							.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelBestellungPositionenTopQP3) {
				panelBestellungPositionenBottomD3
						.setArtikeSetIIdForNewPosition(panelBestellungPositionenBottomD3
								.getBestellpositionDto()
								.getPositioniIdArtikelset());

				panelBestellungPositionenBottomD3.eventActionNew(eI, true,
						false);
				panelBestellungPositionenBottomD3.eventYouAreSelected(false); // Buttons
				// schalten
			} else if (eI.getSource() == panelBestellungWareneingangTopQP4) {
				panelBestellungWareneingangBottomD4.eventActionNew(eI, true,
						false);
				panelBestellungWareneingangBottomD4.eventYouAreSelected(false); // Buttons
				// schalten
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			// der OK Button in einem PanelDialog wurde gedrueckt
			/*
			 * MR: 06.12.07: Auskommentiert. Wird in
			 * TabbedPaneBestellvorschlag.lpEventItemChanged verarbeitet. if
			 * (true) { throw new Exception(
			 * "eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED"
			 * ); } else if (eI.getSource() == pdABTerminABKommentar) {
			 */
			if (eI.getSource() == pdABTerminABKommentar) {
				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.setForAllPositionenABTermin(getBesDto().getIId(),
								getABDate(), getABNummer(),
								getBooleanVonSichtLieferantenTermin());

				panelBestellungSichtLieferTermineSP7.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			// copypaste: 6
			if (eI.getSource() == panelBestellungPositionenTopQP3) {
				copyHV();
			}
		}
	}

	/**
	 * copypaste: 3 kopiere die selektierten positionen in den pastebuffer.
	 * 
	 * @throws ExceptionLP
	 * @throws Throwable
	 */

	public void copyHV() throws ExceptionLP, Throwable {

		Object aoIIdPosition[] = panelBestellungPositionenTopQP3
				.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			BestellpositionDto[] dtos = new BestellpositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellpositionFindByPrimaryKey(
								(Integer) aoIIdPosition[i]);
			}

			if (panelBestellungPositionenBottomD3.getArtikelsetViewController()
					.validateCopyConstraintsUI(dtos)) {
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void einfuegenHV() throws IOException, ParserConfigurationException,
			SAXException, Throwable {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		if (!panelBestellungPositionenBottomD3.getArtikelsetViewController()
				.validatePasteConstraintsUI(o)) {
			return;
		}

		if (o instanceof BelegpositionDto[]) {
			BestellpositionDto[] positionDtos = DelegateFactory
					.getInstance()
					.getBelegpostionkonvertierungDelegate()
					.konvertiereNachBestellpositionDto(getBesDto(),
							(BelegpositionDto[]) o);

			if (positionDtos != null) {
				Integer iId = null;
				Boolean b = positionAmEndeEinfuegen();
				if (b != null) {
					for (int i = 0; i < positionDtos.length; i++) {
						BestellpositionDto positionDto = positionDtos[i];
						try {
							positionDto.setIId(null);
							// damits hinten angehaengt wird.

							if (b == false) {
								Integer iIdAktuellePosition = (Integer) getBestellungPositionenTop()
										.getSelectedId();

								// die erste Position steht auf 1
								Integer iSortAktuellePosition = new Integer(1);

								if (iIdAktuellePosition != null) {
									BestellpositionDto aktuellePositionDto = DelegateFactory
											.getInstance()
											.getBestellungDelegate()
											.bestellpositionFindByPrimaryKey(
													iIdAktuellePosition);

									iSortAktuellePosition = aktuellePositionDto
											.getISort();

									panelBestellungPositionenBottomD3
											.getArtikelsetViewController()
											.setArtikelSetIIdFuerNeuePosition(
													aktuellePositionDto
															.getPositioniIdArtikelset());

									// Die bestehenden Positionen muessen Platz
									// fuer die
									// neue schaffen
									DelegateFactory
											.getInstance()
											.getBestellungDelegate()
											.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
													getBesDto().getIId(),
													iSortAktuellePosition
															.intValue());
								} else {
									iSortAktuellePosition = null;
								}

								// Die neue Position wird an frei gemachte
								// Position
								// gesetzt
								positionDto.setISort(iSortAktuellePosition);
							} else {
								positionDto.setISort(null);
							}

							positionDto.setBelegIId(getBesDto().getIId());

							ArtikelsetViewController viewController = panelBestellungPositionenBottomD3
									.getArtikelsetViewController();

							boolean bDiePositionSpeichern = viewController
									.validateArtikelsetConstraints(positionDto);
							if (bDiePositionSpeichern) {
								positionDto
										.setPositioniIdArtikelset(viewController
												.getArtikelSetIIdFuerNeuePosition());

								// wir legen eine neue position an
								Integer newIId = DelegateFactory
										.getInstance()
										.getBestellungDelegate()
										.createBestellposition(
												positionDto,
												BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
												null);

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

				// die Liste neu aufbauen
				panelBestellungPositionenTopQP3.eventYouAreSelected(false);
				// den Datensatz in der Liste selektieren
				panelBestellungPositionenTopQP3.setSelectedId(iId);

				// im Detail den selektierten anzeigen
				panelBestellungPositionenSP3.eventYouAreSelected(false);
			}

		}

	}

	/**
	 * save bestellpositionDto von xalOfBelegPosI als textpos.
	 * 
	 * @param pOSDocument2POSDtoI
	 *            BestellpositionDto
	 * @param xalOfBelegPosI
	 *            int
	 * @return Integer
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public Integer saveBelegPosAsTextpos(
			POSDocument2POSDto pOSDocument2POSDtoI, int xalOfBelegPosI)
			throws ExceptionLP, Throwable {

		BSPOSDocument2BSPOSDto bSPOSDocument2BSPOSDto = (BSPOSDocument2BSPOSDto) pOSDocument2POSDtoI;
		BestellpositionDto bestellpositionTextDto = new BestellpositionDto();
		bestellpositionTextDto.setBelegIId(getBesDto().getIId());

		/**
		 * @todo JO klaeren PJ 5190
		 */
		bestellpositionTextDto.setISort(xalOfBelegPosI + 1000);
		bestellpositionTextDto
				.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE);
		String sMsg = LPMain.getTextRespectUISPr("lp.copy.error") + " "
				+ bSPOSDocument2BSPOSDto.getAlPOS().get(xalOfBelegPosI);
		bestellpositionTextDto.setXTextinhalt(sMsg);

		Integer iId = DelegateFactory
				.getInstance()
				.getBestellungDelegate()
				.createBestellposition(
						bestellpositionTextDto,
						BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
						null);

		return iId;
	}

	/**
	 * fuelle bestellpositionDtoI von xalOfBSPOSI mit den mussfeldern.
	 * 
	 * @param belegposDtoI
	 *            BestellpositionDto
	 * @param xalOfBSPosI
	 *            int
	 */
	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBSPosI) {

		BestellpositionDto bsPosDto = (BestellpositionDto) belegposDtoI;

		bsPosDto.setBelegIId(getBesDto().getIId());
		bsPosDto.setISort(xalOfBSPosI + 1000);

		String sPosArt = bsPosDto.getPositionsartCNr();
		if (LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt)
				|| LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)) {

			if (bsPosDto.getNMenge() == null) {
				bsPosDto.setNMenge(new BigDecimal(0));
			}
			if (bsPosDto.getNNettoeinzelpreis() == null) {
				bsPosDto.setNNettoeinzelpreis(new BigDecimal(0));
			}
			if (bsPosDto.getDRabattsatz() == null) {
				bsPosDto.setDRabattsatz(0.0);
			}
			if (bsPosDto.getNNettogesamtpreis() == null) {
				bsPosDto.setNNettogesamtpreis(bsPosDto.getNNettoeinzelpreis()
						.add(bsPosDto.getNNettoeinzelpreis().multiply(
								new BigDecimal(bsPosDto.getDRabattsatz())
										.movePointLeft(2))));
			}
		}
	}

	/**
	 * Fuer lazy loading.
	 * 
	 * @throws Throwable
	 * @return PanelBasis
	 */
	private PanelBestellungKopfdaten refreshBestellungKopfdaten()
			throws Throwable {

		if (panelBestellungKopfdatenD == null) {
			Integer iIdBestellung = null;
			// cast von String zu Integer da bestellung Integer hat
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdBestellung = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			panelBestellungKopfdatenD = new PanelBestellungKopfdaten(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.kopfdaten"),
					iIdBestellung); // empty bei leerer auftragsliste

			setComponentAt(IDX_PANEL_KOPFDATEN, panelBestellungKopfdatenD);
		}
		return panelBestellungKopfdatenD;
	}

	// TODO-AGILCHANGES
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

		Integer iIdBestellung = null;
		if (getBesDto() != null) {
			iIdBestellung = getBesDto().getIId();
		}

		// dtos hinterlegen
		initializeDtos(iIdBestellung);
		int selectedCur = this.getSelectedIndex();

		getInternalFrame().setRechtModulweit(sRechtModulweit);

		if (selectedCur == IDX_PANEL_AUSWAHL) {
			refreshBestellungAuswahl();

			panelBestellungAuswahlQP1.eventYouAreSelected(false);
			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelBestellungAuswahlQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
		}

		else if (selectedCur == IDX_PANEL_KOPFDATEN) {
			refreshBestellungKopfdaten();
			panelBestellungKopfdatenD.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANEL_BESTELLPOSITION) {
			if (iIdBestellung != null) {
				// filter aktualisieren

				refreshBestellungPositionen(getBesDto().getIId());

				// if (getBestellungDto() != null) {
				// FilterKriterium[] filtersPositionen =
				// BestellungFilterFactory.getInstance().
				// getDefaultFilterBestellung(iIdBestellung);
				// panelBestellungPositionenTopQP3.setDefaultFilter(
				// filtersPositionen);
				// }

				panelBestellungPositionenSP3.eventYouAreSelected(false);
				// btngeliefert: 0
				LockStateValue lsv = getLockStateForBSPOSPanel();
				panelBestellungPositionenTopQP3.updateButtons(lsv);
			}
		}

		else if (selectedCur == IDX_PANEL_KONDITIONEN) {

			// TODO ghp
			getPanelBestellungKonditionenD4().setKeyWhenDetailPanel(
					iIdBestellung);
			getPanelBestellungKonditionenD4().eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANEL_WARENEINGANG) {

			boolean hatRecht = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_BES_WARENEINGANG_CUD);
			if (hatRecht == true) {
				getInternalFrame().setRechtModulweit(
						RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			// hole id von bestellung
			refreshBestellungWareneingang(getBesDto().getIId());

			panelBestellungWareineingangSP4.eventYouAreSelected(false);

			if (getBesDto().getStatusCNr().equals(
					BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
				panelBestellungWareneingangTopQP4
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_DISABLE_ALL));

				if (panelBestellungWareneingangTopQP4.getHmOfButtons()
						.containsKey(PanelQuery.LEAVEALONE_DOKUMENTE)) {
					LPButtonAction bu = (LPButtonAction) panelBestellungWareneingangTopQP4
							.getHmOfButtons().get(
									PanelQuery.LEAVEALONE_DOKUMENTE);
					bu.setEnable(true);
					bu.getButton().setEnabled(true);
				}

				if (panelBestellungWareneingangTopQP4.getHmOfButtons()
						.containsKey(PanelQuery.LEAVEALONE_PRINTPANELQUERY)) {

					LPButtonAction bu = (LPButtonAction) panelBestellungWareneingangTopQP4
							.getHmOfButtons().get(
									PanelQuery.LEAVEALONE_PRINTPANELQUERY);
					bu.setEnable(true);
					bu.getButton().setEnabled(true);
				}
			} else {
				panelBestellungWareneingangTopQP4
						.updateButtons(panelBestellungWareneingangBottomD4
								.getLockedstateDetailMainKey());
			}
		}

		else if (selectedCur == IDX_PANEL_WARENEINGANGSPOSITIONEN) {
			boolean hatRecht = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_BES_WARENEINGANG_CUD);
			if (hatRecht == true) {
				getInternalFrame().setRechtModulweit(
						RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			// refreshen von WE damit key auch bei direktem click gesetzt werden
			// kann in handleItemChanged()
			if (getWareneingangDto() == null
					|| getWareneingangDto().getIId() == null) {
				// warnung dasS kein we selektiert ist
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						LPMain.getTextRespectUISPr("bes.wareneingangwaehlen"));
				Object bestellId = panelBestellungAuswahlQP1.getSelectedId();
				refreshBestellungWareneingang((Integer) bestellId);
				setSelectedComponent(panelBestellungWareineingangSP4);
			}
			refreshBestellungWareneingang(iIdBestellung);
			refreshBestellungWEP(iIdBestellung);

			// flrextradata 3: Extra Daten, die man im FLR auswerten kann,
			// setzen
			if (panelBestellungWareneingangTopQP4.getSelectedId() != null) {

				ArrayList<Object> listOfExtraDataForFLR = new ArrayList<Object>();
				listOfExtraDataForFLR.add(
						BestellpositionFac.FLR_EXTRA_DATA_WARENEINGANGIID,
						panelBestellungWareneingangTopQP4.getSelectedId());
				panelBestellungWEPTopQP5
						.setListOfExtraData(listOfExtraDataForFLR);

				panelBestellungWEPTopQP5.eventYouAreSelected(false);

				panelBestellungWEPTopQP5
						.updateButtons(panelBestellungWEPBottomD5
								.getLockedstateDetailMainKey());
			}
		}

		else if (selectedCur == IDX_PANEL_BESTELLPOSITIONSICHTRAHMEN) {
			refreshBestellungPositionSichtRahmen();

			panelBestellungPositionSichtRahmenTopQP6
					.updateButtons(panelBestellungPositionSichtRahmenBottomD6
							.getLockedstateDetailMainKey());

			panelBestellungPositionSichtRahmenSP6.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANEL_SICHTLIEFERTERMINE) {

			// DialogFactory.showModalDialog("Hinweis", "Panel in Arbeit");
			// return;

			if (iIdBestellung != null) {
				refreshBestellungSichtLieferTermine();
				if (getBesDto() != null) {
					FilterKriterium[] filtersPositionen = BestellungFilterFactory
							.getInstance().getDefaultFilterBestellung(
									iIdBestellung);
					panelBestellungSichtLieferTermineTopQP7
							.setDefaultFilter(filtersPositionen);
				}

				this.panelBestellungSichtLieferTermineSP7
						.eventYouAreSelected(false);

				panelBestellungSichtLieferTermineTopQP7
						.updateButtons(panelBestellungSichtLieferTermineBottomD7
								.getLockedstateDetailMainKey());
			}
		}

		if (iIdBestellung != null) {
			// if
			// (getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac
			// .
			// BESTELLSTATUS_OFFEN) ||
			// getBestellungDto().getBestellungsstatusCNr().
			// equals(BestellungFac.BESTELLSTATUS_BESTAETIGT)) {
			// enableWareneingangPanels();
			// // this.enablePanels(null);
			// }
			// else {
			// if
			// (getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac
			// .
			// BESTELLSTATUS_GELIEFERT)) {
			// enableWareneingangPanels();
			// // this.enablePanels(null);
			// }
			// else {
			// enableWE(false);
			// enableWEP(false);
			// // this.enablePanels(null);
			// }
			// }
			// this.enableSichtLieferantTermine();
		}
	}

	private PanelBestellungKonditionen getPanelBestellungKonditionenD4()
			throws Throwable {

		if (panelBestellungKonditionenD4 == null) {
			panelBestellungKonditionenD4 = new PanelBestellungKonditionen(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.konditionen"),
					null, this);
			this.setComponentAt(IDX_PANEL_KONDITIONEN,
					panelBestellungKonditionenD4);
		}
		return panelBestellungKonditionenD4;
	}

	public PanelSplit refreshBestellungWEP(Integer iIdBestellung)
			throws Throwable {
		if (panelBestellungWEPSP5 == null) {
			panelBestellungWEPBottomD5 = new PanelBestellungWEP(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.wareneingangspositionen"),
					null); // eventuell gibt es noch keine position
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelBestellungWEPTopQP5 = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_BESTELLUNGWAREINEINGANGSPOSITIONEN,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.wareneingangspositionen"),
					true);
			panelBestellungWEPTopQP5.setBBenutzeUebersteuerteId(true);
			panelBestellungWEPTopQP5.addDirektFilter(BestellungFilterFactory
					.getInstance().createFKDWepText());

			panelBestellungWEPTopQP5.createAndSaveAndShowButton(
					"/com/lp/client/res/document_check16x16.png",
					LPMain.getTextRespectUISPr("bes.allewepsuebernehmen"),
					MY_OWN_NEW_EXTRA_NEU_ALLE_POSITIONEN_AUS_WEP_UEBERNEHMEN,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);

			panelBestellungWEPTopQP5.createAndSaveAndShowButton(
					"/com/lp/client/res/text_ok16x16.png",
					LPMain.getTextRespectUISPr("bes.allepreiseerfassen"),
					MY_OWN_NEW_EXTRA_NEU_ALLE_PREISE_ERFASSEN,
					RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);

			panelBestellungWEPTopQP5
					.createAndSaveAndShowButton(
							"/com/lp/client/res/shoppingcart.png",
							LPMain.getTextRespectUISPr("bes.bestellung.neuerwareneingang.auswep"),
							MY_OWN_NEW_NEUER_WARENEINGANG_AUS_WEP,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);

			panelBestellungWEPSP5 = new PanelSplit(getInternalFrame(),
					panelBestellungWEPBottomD5, panelBestellungWEPTopQP5, 170);

			setComponentAt(IDX_PANEL_WARENEINGANGSPOSITIONEN,
					panelBestellungWEPSP5);

		}
		panelBestellungAuswahlQP1.setUebersteuerteId(null);
		panelBestellungWEPTopQP5.setDefaultFilter(BestellungFilterFactory
				.getInstance().getDefaultFilterWEPQP5(iIdBestellung));
		return panelBestellungWEPSP5;
	}

	public PanelSplit refreshBestellungWareneingang(Integer iIdBestellung)
			throws Throwable {
		if (panelBestellungWareineingangSP4 == null) {
			panelBestellungWareneingangBottomD4 = new PanelBestellungWareneingang(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.wareneingang"),
					null); // eventuell gibt es noch keine position

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN };

			panelBestellungWareneingangTopQP4 = new PanelQuery(null, null,
					QueryParameters.UC_ID_BESTELLUNGWARENEINGANG,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.wareneingang"),
					true); // flag, damit flr erst beim aufruf des panels
			// loslaeuft

			panelBestellungWareineingangSP4 = new PanelSplit(
					getInternalFrame(), panelBestellungWareneingangBottomD4,
					panelBestellungWareneingangTopQP4, 200);

			setComponentAt(IDX_PANEL_WARENEINGANG,
					panelBestellungWareineingangSP4);
		}
		// filter refreshen.
		panelBestellungWareneingangTopQP4
				.setDefaultFilter(BestellungFilterFactory.getInstance()
						.getDefaultFilterWareneingangQP4(iIdBestellung));

		return panelBestellungWareineingangSP4;
	}

	private PanelSplit refreshBestellungSichtLieferTermine() throws Throwable {
		if (this.panelBestellungSichtLieferTermineSP7 == null) {
			panelBestellungSichtLieferTermineBottomD7 = new PanelBestellungSichtLieferantenTermine(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.sichtliefertermine"),
					null); // eventuell gibt es noch keine position
			panelBestellungSichtLieferTermineTopQP7 = new PanelQuery(null,
					null, QueryParameters.UC_ID_SICHTLIEFERTERMINE, null,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.wareneingang"),
					true); // flag, damit flr erst beim aufruf des panels
			// loslaeuft

			// Hier den zusaetzlichen Button aufs Panel bringen
			panelBestellungSichtLieferTermineTopQP7.createAndSaveAndShowButton(
					"/com/lp/client/res/alarmclock.png",
					LPMain.getTextRespectUISPr("bes.allelieferterminesetzen"),
					MY_OWN_NEW_ALLE_ABTERMINE_VORBELEGEN,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);

			panelBestellungSichtLieferTermineSP7 = new PanelSplit(
					getInternalFrame(),
					panelBestellungSichtLieferTermineBottomD7,
					panelBestellungSichtLieferTermineTopQP7, 200);

			setComponentAt(IDX_PANEL_SICHTLIEFERTERMINE,
					panelBestellungSichtLieferTermineSP7);
		}
		return panelBestellungSichtLieferTermineSP7;
	}

	public void refreshBestellungPositionen(Integer iIdBesI) throws Throwable {
		if (panelBestellungPositionenSP3 == null) {
			panelBestellungPositionenBottomD3 = new PanelBestellungPositionen(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.positionsdaten"),
					null); // eventuell gibt es noch keine position

			if (getBesDto() != null) { // fuer leere Bestellungsliste
				BestellungFilterFactory.getInstance()
						.getDefaultFilterBestellung(iIdBesI);
			}

			// copypaste: 5 die zusaetzlichen Buttons am PanelQuery anbringen
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW, };

			panelBestellungPositionenTopQP3 = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_BESTELLPOSITION,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.positionsdaten"),
					true); // flag, damit flr erst beim aufruf des panels
			// loslaeuft

			panelBestellungPositionenSP3 = new PanelSplit(getInternalFrame(),
					panelBestellungPositionenBottomD3,
					panelBestellungPositionenTopQP3, 110);

			panelBestellungPositionenTopQP3
					.createAndSaveAndShowButton(
							"/com/lp/client/res/navigate_close.png",
							LPMain.getTextRespectUISPr("bes.positionen.sortierenachartikel"),
							BUTTON_SORTIERE_NACH_IDENT, null);

			// mehrfachselekt: fuer dieses QP aktivieren
			// copypaste: 4
			panelBestellungPositionenTopQP3
					.setMultipleRowSelectionEnabled(true);

			setComponentAt(IDX_PANEL_BESTELLPOSITION,
					panelBestellungPositionenSP3);
		}

		// filter refreshen.
		panelBestellungPositionenTopQP3
				.setDefaultFilter(BestellungFilterFactory.getInstance()
						.getDefaultFilterBestellung(iIdBesI));

		// return panelBestellungPositionenSP3;
	}

	public void setPanelBestellungPositionenTopQP3(
			PanelQuery panelBestellungPositionenTopQP3I) {

		panelBestellungPositionenTopQP3 = panelBestellungPositionenTopQP3I;
	}

	public PanelQuery getPanelBestellungPositionenTopQP3() {
		return panelBestellungPositionenTopQP3;
	}

	public void dialogQueryAnfrageFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAnfrageAuswahl = AnfrageFilterFactory.getInstance()
				.createPanelFLRAnfrage(getInternalFrame(), false, false);
		new DialogQuery(panelQueryFLRAnfrageAuswahl);
	}

	/**
	 * Dtos initialisieren.
	 * 
	 * @param iIdBesI
	 *            PK der Bestellung
	 * @throws Throwable
	 */
	public void initializeDtos(Integer iIdBesI) throws Throwable {

		if (getBesDto() != null && iIdBesI != null
				&& !iIdBesI.equals(getBesDto().getIId())) {
			setWareneingangDto(null);
		}
		setRahmBesDto(new BestellungDto());
		setBestellungDto(new BestellungDto());
		setLieferantDto(new LieferantDto());

		if (iIdBesI != null) {
			// Bes (rahmen, abruf, ...) lesen
			setBestellungDto(DelegateFactory.getInstance()
					.getBestellungDelegate()
					.bestellungFindByPrimaryKey(iIdBesI));

			if (getBesDto().getBestellungartCNr().equals(
					BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
				// --ist AbrufBes
				setRahmBesDto(DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellungFindByPrimaryKey(
								getBesDto().getIBestellungIIdRahmenbestellung()));
			}

			if (getBesDto().getLieferantIIdBestelladresse() != null) {
				setLieferantDto(DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								getBesDto().getLieferantIIdBestelladresse()));
			}

			// belegartkonditionen: 2a Kopftext und Fusstext hinterlegen
			if (getBesDto().getBestelltextIIdFusstext() == null
					&& getBesDto().getBestelltextIIdKopftext() == null) {
				initTexte();
			}
			if (getBesDto().getBestelltextIIdKopftext() != null) {
				kopftextDto = DelegateFactory
						.getInstance()
						.getBestellungServiceDelegate()
						.bestellungtextFindByPrimaryKey(
								getBesDto().getBestelltextIIdKopftext());
			}

			if (getBesDto().getBestelltextIIdFusstext() != null) {
				fusstextDto = DelegateFactory
						.getInstance()
						.getBestellungServiceDelegate()
						.bestellungtextFindByPrimaryKey(
								getBesDto().getBestelltextIIdFusstext());
			}
		}
	}

	private void initTexte() throws ExceptionLP, Throwable {

		// Texte vorbelegen
		if (kopftextDto == null || kopftextDto.getIId() == null) {
			try {
				kopftextDto = DelegateFactory
						.getInstance()
						.getBestellungServiceDelegate()
						.getBestellungkopfDefault(
								getLieferantDto().getPartnerDto()
										.getLocaleCNrKommunikation());
			} catch (Throwable t) {
				// wenn es keinen Kopftext gibt
				kopftextDto = DelegateFactory
						.getInstance()
						.getBestellungServiceDelegate()
						.createDefaultBestellungtext(
								MediaFac.MEDIAART_KOPFTEXT,
								BestellungServiceFac.BESTELLUNG_DEFAULT_KOPFTEXT);
			}
		}
		fusstextDto = null;
		if (fusstextDto == null || fusstextDto.getIId() == null) {
			try {
				fusstextDto = DelegateFactory
						.getInstance()
						.getBestellungServiceDelegate()
						.getBestellungfussDefault(
								getLieferantDto().getPartnerDto()
										.getLocaleCNrKommunikation());
			} catch (Throwable t) {
				// wenn es keinen Fusstext gibt
				fusstextDto = DelegateFactory
						.getInstance()
						.getBestellungServiceDelegate()
						.createDefaultBestellungtext(
								MediaFac.MEDIAART_FUSSTEXT,
								BestellungServiceFac.BESTELLUNG_DEFAULT_FUSSTEXT);
			}
		}
	}

	public void resetDtos() {
		besDto = new BestellungDto();
		lieferantDto = new LieferantDto();
		rahmBesDto = new BestellungDto();
		partnerDto = new PartnerDto();
		wareneingangDto = new WareneingangDto();
		wepDto = new WareneingangspositionDto();
	}

	private void dialogQueryAuftrag() throws Throwable {
		FilterKriterium[] fk = SystemFilterFactory.getInstance()
				.createFKMandantCNr();
		Integer auftragIId = null;
		if (getBesDto() != null) {
			auftragIId = getBesDto().getAuftragIId();
		}
		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), true, true, fk,
						auftragIId);
		new DialogQuery(panelQueryFLRAuftrag);
	}

	/**
	 * Diese Methode setzt die aktuelle Bestellung aus der Auswahlliste als der
	 * zu lockende Bestellvorschlag.
	 * 
	 * @throws Throwable
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = panelBestellungAuswahlQP1.getSelectedId();
		if (oKey != null) {
			initializeDtos((Integer) oKey);
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void printAlleAngelegtenBestellungen() throws Throwable {

		String[] optionen = new String[] {
				LPMain.getInstance().getTextRespectUISPr(
						"bes.angelegtedrucken.alle"),
				LPMain.getInstance().getTextRespectUISPr(
						"bes.angelegtedrucken.auswaehlen") };

		int iOption = DialogFactory
				.showModalDialog(getInternalFrame(), LPMain.getInstance()
						.getTextRespectUISPr("bes.angelegtedrucken.welche"),
						LPMain.getInstance().getTextRespectUISPr("lp.frage"),
						optionen, optionen[0]);

		if (iOption > -1) {

			if (iOption == 0) {

				alleAngelegtenBestellungenDrucken(null);
			} else if (iOption == 1) {

				panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
						.createPanelFLRLieferantGoto(getInternalFrame(), null,
								true, false);
				new DialogQuery(panelQueryFLRLieferant);
			}
		}
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENU_ACTION_DATEI_BESTELLUNG)) {
			printBestellung();
		} else if (e.getActionCommand().equals(
				MY_OWN_NEW_ALLE_ANGELEGTEN_BESTELLUNGEN_VERSENDEN)) {
			printAlleAngelegtenBestellungen();
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_ABHOLAUFTRAG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"bes.menu.datei.abholauftrag");
			getInternalFrame().showReportKriterien(
					new ReportAbholauftrag(getInternalFrame(), add2Title,
							getBesDto().getIId()),
					getLieferantDto().getPartnerDto(),
					getBesDto().getAnsprechpartnerIId(), false);

		} else if (e.getActionCommand().equals(
				MENU_ACTION_DATEI_MONATSBESTELLUNG)) {
			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_CSV, false);
			if (files == null || files.length < 1 || files[0] == null) {
				return;
			}
			File f = files[0];

			ArrayList<String[]> al = null;
			LPCSVReader reader = null;
			reader = new LPCSVReader(new FileReader(f), ';');
			al = (ArrayList<String[]>) reader.readAll();
			reader.close();
			StringBuffer err = new StringBuffer();
			if (al.size() > 0) {
				ArrayList<ImportMonatsbestellungDto> zeilen = new ArrayList<ImportMonatsbestellungDto>();

				for (int i = 1; i < al.size(); i++) {
					try {
						String[] as = al.get(i);

						ImportMonatsbestellungDto m = new ImportMonatsbestellungDto();

						m.setLieferscheinnr(as[3]);

						m.setMenge(new BigDecimal(new Double(as[7])));

						LieferantDto lDto = DelegateFactory
								.getInstance()
								.getLieferantDelegate()
								.lieferantFindByCKundennrcNrMandant(as[0],
										LPMain.getTheClient().getMandant());

						if (lDto != null) {
							m.setLieferantIId(lDto.getIId());

							PartnerDto pDto = DelegateFactory
									.getInstance()
									.getPartnerDelegate()
									.partnerFindByPrimaryKey(
											lDto.getPartnerIId());

							if (lDto.getIIdKostenstelle() == null) {
								err.append("Zeile "
										+ i
										+ ": "
										+ pDto.formatAnrede()
										+ " hat keine Kostenstelle hinterlegt\n\r");
							}

						} else {
							err.append("Zeile " + i
									+ ": Kein Lieferant zu Kdnr. " + as[0]
									+ " gefunden \n\r");
						}

						if (as[9].length() > 7) {

							Calendar c = Calendar.getInstance();

							c.set(Calendar.YEAR,
									new Integer(as[9].substring(0, 4)));
							c.set(Calendar.MONTH,
									new Integer(as[9].substring(4, 6)) - 1);
							c.set(Calendar.DAY_OF_MONTH,
									new Integer(as[9].substring(6, 8)));

							m.setWeDatum(Helper.cutDate(new java.sql.Date(c
									.getTimeInMillis())));

						} else {
							err.append("Zeile " + i
									+ ": Kein g\u00FCltiges Datum / " + as[8]
									+ "\n\r");
						}

						ArtikelDto artikelDto = null;
						try {
							artikelDto = DelegateFactory.getInstance()
									.getArtikelDelegate()
									.artikelFindByCNr(as[5]);
							m.setArtikelIId(artikelDto.getIId());

							zeilen.add(m);

						} catch (ExceptionLP ex) {
							err.append("Zeile " + i + ": " + "Artikel " + as[5]
									+ " nicht gefunden.\r\n");
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
					DelegateFactory.getInstance().getBestellungDelegate()
							.importiereMonatsbestellung(zeilen);
					JOptionPane.showMessageDialog(this,
							"Daten erfolgreich importiert",
							"Monatsbestellung-Import",
							JOptionPane.INFORMATION_MESSAGE);
				}

			}

		} else if (e.getActionCommand().equals(
				MENU_ACTION_BEARBEITEN_AUFTRAGSZUORDNUNG)) {
			boolean doIt = true;
			if (getBesDto() != null && getBesDto().getIId() != null) {
				if (LocaleFac.STATUS_ERLEDIGT
						.equals(getBesDto().getStatusCNr())
						|| LocaleFac.STATUS_GELIEFERT.equals(getBesDto()
								.getStatusCNr())) {
					doIt = DialogFactory.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getInstance().getTextRespectUISPr(
									"bes.frage.auftragzuordnung"));
				}
				if (doIt) {
					dialogQueryAuftrag();
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_ACTION_BEARBEITEN_OFFENE_WEPOS)) {
			DialogOffeneWEPos d = new DialogOffeneWEPos(getInternalFrame());
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);
		} else if (e.getActionCommand()
				.equals(MENU_ACTION_JOURNAL_WARENEINGANG)) {
			getInternalFrame().showReportKriterien(
					new ReportBestellungWareneingang(getInternalFrame(), LPMain
							.getTextRespectUISPr("bes.menu.wareneingang")));
		} else if (e.getActionCommand().equals(MENU_ACTION_ALLE)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportBestellungAlleBestellungen(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("bes.menu.allebestellungen")));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_OFFENE)) {
			getInternalFrame().showReportKriterien(
					new ReportBestellungOffeneBestellungen(getInternalFrame(),
							LPMain.getTextRespectUISPr("bes.menu.offene")));
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			// nur, wenn auch eine Bestellung ausgewaehlt ist.
			if (getBesDto() != null && getBesDto().getIId() != null) {

				// Statuswechsel 'Offen' -> 'Geliefert' : Ausgeloest durch Menue
				if (getBesDto().getStatusCNr().equals(
						BestellungFac.BESTELLSTATUS_OFFEN)
						|| getBesDto().getStatusCNr().equals(
								BestellungFac.BESTELLSTATUS_BESTAETIGT)
						|| getBesDto().getStatusCNr().equals(
								BestellungFac.BESTELLSTATUS_GELIEFERT)) {
					if (DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("bes.status.auferledigtsetzen"),
									LPMain.getTextRespectUISPr("lp.hint"))) {

						DelegateFactory.getInstance().getBestellungDelegate()
								.manuellErledigen(getBesDto().getIId());

						// den geaenderten Status anzeigen
						setBestellungDto(DelegateFactory
								.getInstance()
								.getBestellungDelegate()
								.bestellungFindByPrimaryKey(
										getBesDto().getIId()));

						getBestellungKopfdaten().eventYouAreSelected(false);
						panelBestellungAuswahlQP1.eventYouAreSelected(false);
					}
				} else {
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("bes.warning.kannnichtgeliefertwerden"));

					mf.setLocale(LPMain.getTheClient().getLocUi());

					Object pattern[] = { getBesDto().getStatusCNr() };

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.warning"),
							mf.format(pattern));
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN)) {
			// nur, wenn auch eine Bestellung ausgewaehlt ist.
			if (getBesDto() != null && getBesDto().getIId() != null) {

				// Statuswechsel 'Geliefert' -> 'Offen' : Ausgeloest durch Menue
				if (getBesDto().getStatusCNr().equals(
						BestellungFac.BESTELLSTATUS_GELIEFERT)
						|| getBesDto().getStatusCNr().equals(
								BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
					if (DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("bes.status.aufoffensetzen"),
									LPMain.getTextRespectUISPr("lp.hint"))) {

						DelegateFactory.getInstance().getBestellungDelegate()
								.erledigungAufheben(getBesDto().getIId());

						// den geaenderten Status anzeigen
						setBestellungDto(DelegateFactory
								.getInstance()
								.getBestellungDelegate()
								.bestellungFindByPrimaryKey(
										getBesDto().getIId()));

						getBestellungKopfdaten().eventYouAreSelected(false);
						panelBestellungAuswahlQP1.eventYouAreSelected(false);
					}
				} else {
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("bes.warning.kannnichtunerledigtwerden"));

					mf.setLocale(LPMain.getTheClient().getLocUi());

					Object pattern[] = { getBesDto().getStatusCNr() };

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.warning"),
							mf.format(pattern));
				}
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_STORNO_AUFHEBEN)) {
			// nur, wenn auch eine Bestellung ausgewaehlt ist.
			if (getBesDto() != null && getBesDto().getIId() != null) {

				// Statuswechsel 'Geliefert' -> 'Offen' : Ausgeloest durch Menue
				if (getBesDto().getStatusCNr().equals(
						BestellungFac.BESTELLSTATUS_STORNIERT)) {
					if (DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("bes.status.aufanlegensetzen"),
									LPMain.getTextRespectUISPr("lp.hint"))) {

						DelegateFactory.getInstance().getBestellungDelegate()
								.stornoAufheben(getBesDto().getIId());

						// den geaenderten Status anzeigen
						setBestellungDto(DelegateFactory
								.getInstance()
								.getBestellungDelegate()
								.bestellungFindByPrimaryKey(
										getBesDto().getIId()));

						getBestellungKopfdaten().eventYouAreSelected(false);
						panelBestellungAuswahlQP1.eventYouAreSelected(false);
					}
				} else {
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("bes.warning.kannnichtunerledigtwerden"));

					mf.setLocale(LPMain.getTheClient().getLocUi());

					Object pattern[] = { getBesDto().getStatusCNr() };

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.warning"),
							mf.format(pattern));
				}
			}
		}

		else if (e.getActionCommand()
				.equals(MENU_BEARBEITEN_INTERNER_KOMMENTAR)) {
			if (isBSangelegtDlg()) {
				if (!refreshBestellungKopfdaten().isLockedDlg()) {
					refreshPdBestellungInternerKommentar();
					getInternalFrame().showPanelDialog(
							pdBestellungInternerKommentar);
				}
			}
		}

		else if (e.getActionCommand()
				.equals(MENU_BEARBEITEN_EXTERNER_KOMMENTAR)) {
			if (isBSangelegtDlg()) {
				if (!refreshBestellungKopfdaten().isLockedDlg()) {
					refreshPdBestellungExternerKommentar();
					getInternalFrame().showPanelDialog(
							pdBestellungExternerKommentar);
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_WEP_INTERNER_KOMMENTAR)) {
			if (isBSangelegtDlg()) {
				if (!refreshBestellungKopfdaten().isLockedDlg()) {
					if (getWareneingangspositionenDto() == null) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("bes.wepkommentar"));
						return;
					} else {
						if (getWareneingangspositionenDto() != null
								&& getWareneingangspositionenDto().getIId() != null) {
							refreshPdBestellungWEPInternerKommentar();
							getInternalFrame().showPanelDialog(
									pdBestellungWEPInternerKommentar);
						} else {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("bes.warning.keinewareneingangsbuchung"));
						}
					}
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_ACTION_BEARBEITEN_BSMAHNSPERRE_SETZEN)) {
			if (getBesDto() != null && getBesDto().getIId() != null) {
				getInternalFrame().showPanelDialog(
						new PanelDialogBSMahnsperreBis(getInternalFrame(),
								LPMain.getTextRespectUISPr("lp.mahnsperrebis"),
								getBesDto()));
			}
		}
	}

	// das Panel fuer den internen Kommentar
	private void refreshPdBestellungInternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken nicht
		// richtig
		pdBestellungInternerKommentar = new PanelDialogBestellungInternExternKommentar(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.internerkommentar"), true);
	}

	// das Panel fuer den internen Kommentar
	private PanelDialogKriterien refreshPanelDialogKriteriensetABTermin()
			throws HeadlessException, Throwable {
		pdABTerminABKommentar = new PanelDialogKriteriensetABTermin(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.setabterminundabnummer"), this);
		return pdABTerminABKommentar;
	}

	// das Panel fuer den externen Kommentar
	private void refreshPdBestellungExternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken nicht
		// richtig
		pdBestellungExternerKommentar = new PanelDialogBestellungInternExternKommentar(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.externerkommentar"), false);
	}

	// das Panel fuer den internen Kommentar
	private void refreshPdBestellungWEPInternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken nicht
		// richtig
		pdBestellungWEPInternerKommentar = new PanelDialogBestellungWEPInternKommentar(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.internerkommentar"), true);
	}

	private InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	/**
	 * Bestellung drucken.
	 * 
	 * @throws Throwable
	 */
	public void printBestellung() throws Throwable {
		// nur drucken, wenn auch eine ausgewaehlt wurde.
		if (getBesDto() != null && getBesDto().getIId() != null) {

			if (pruefeKonditionen()) {
				// --Anwender war in Konditionen
				if (aktuelleBestellungHatPositionenDlg()) {
					// die Bestellung aktivieren; Status: offen
					setBestellungDto(DelegateFactory.getInstance()
							.getBestellungDelegate()
							.bestellungFindByPrimaryKey(getBesDto().getIId()));
					BigDecimal bdBestellwertInBestellwaehrung = getBesDto()
							.getNBestellwert();
					// MB 30.05.06 Warnung be Unterschreitung des
					// Mindestbestellwerts (sofern dieser definiert ist)
					if (bdBestellwertInBestellwaehrung != null
							&& getLieferantDto() != null
							&& getLieferantDto().getNMindestbestellwert() != null) {
						// Dazu den Bestellwert in Mandantenwaehrung umrechnen
						BigDecimal bdBestellwertInMandantenWaehrung = DelegateFactory
								.getInstance()
								.getLocaleDelegate()
								.rechneUmInAndereWaehrung(
										bdBestellwertInBestellwaehrung,
										getBesDto().getWaehrungCNr(),
										LPMain.getTheClient()
												.getSMandantenwaehrung());
						if (bdBestellwertInMandantenWaehrung
								.compareTo(getLieferantDto()
										.getNMindestbestellwert()) < 0) {
							boolean bAnswer = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr("bes.warning.mindestbestellwertunterschritten"),
											LPMain.getTextRespectUISPr("lp.hint"));
							if (!bAnswer) {
								return;
							}
						}
					}

					// if (getBesDto().getStatusCNr().equals(
					// BestellungFac.BESTELLSTATUS_ANGELEGT)) {
					//
					// BestellungDto bestellungDto = getBesDto();
					// bestellungDto
					// .setStatusCNr(BestellungFac.BESTELLSTATUS_OFFEN);
					//
					// DelegateFactory.getInstance().getBestellungDelegate()
					// .updateBestellung(bestellungDto);
					// }

					if (getBesDto().getBestellungartCNr().equals(
							BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
						// abfragen ob abruf und pruefen ob abruf rahmen
						// zugeordnet ist
						if (getBesDto().getIBestellungIIdRahmenbestellung() == null) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("bes.abrufbestellungistkeinenrahemenzugeordnet"));
							return;
						} else {
							// Wenn Abruf dann Status von Rahmen neu errechnen
							BestellungDto abestDto = DelegateFactory
									.getInstance()
									.getBestellungDelegate()
									.bestellungFindByPrimaryKey(
											getBesDto()
													.getIBestellungIIdRahmenbestellung());
							String sStatus = DelegateFactory
									.getInstance()
									.getBestellungDelegate()
									.getRichtigenBestellStatus(
											abestDto.getIId(), false);
							abestDto.setStatusCNr(sStatus);
							DelegateFactory.getInstance()
									.getBestellungDelegate()
									.updateBestellung(abestDto);
						}
					}

					getInternalFrame().showReportKriterien(
							new ReportBestellung(getInternalFrame(),
									getAktuellesPanel(), getBesDto(),
									getLieferantDto()),
							getLieferantDto().getPartnerDto(),
							getBesDto().getAnsprechpartnerIId(), false);
				}
			}
		}
	}

	/**
	 * Pruefen, ob eine Bestellung Positionen hat. Warnung anzeigen, wenn sie
	 * keine Positionen hat.
	 * 
	 * @return boolean true, wenn die Bestellung Positionen hat
	 * @throws Throwable
	 */
	private boolean aktuelleBestellungHatPositionenDlg() throws Throwable {
		boolean bHatPositionen = true;

		if (DelegateFactory.getInstance().getBestellungDelegate()
				.bestellpositionFindByBestellung(getBesDto().getIId()).length == 0) {
			bHatPositionen = false;

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("bes.warning.keinepositionen"));
		}
		return bHatPositionen;
	}

	/**
	 * Ist eine BS angelegt?
	 * 
	 * @return boolean
	 */
	public boolean isBSangelegtDlg() {

		boolean bIsBSang = true;

		if (getBesDto() == null || getBesDto().getIId() == null) {
			bIsBSang = false;
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("bes.warning.keinebestellung"));
		}
		return bIsBSang;
	}

	/**
	 * Der Status der Bestellung kann in einigen Faellen ueber den Update Button
	 * geaendert werden.
	 * 
	 * @return boolean true, wenn die aktuelle Bestellung geaendert werden darf
	 * @throws Throwable
	 *             Ausnahme
	 */
	public boolean checkAndSetBestellungstatusDlg() throws Throwable {

		boolean bStatusChangeOK = false;
		String cNrStatusNext = null;

		if (isBSangelegtDlg()) {

			String cNrStatus = getBesDto().getStatusCNr();

			if (cNrStatus.equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
				// Statuswechsel 'Angelegt' -> 'Offen'
				// Eine Bestellung kann im Augenblick nur durch Drucken in den
				// Zustand 'Offen' gelangen
				bStatusChangeOK = true;
			}

			else if (cNrStatus.equals(BestellungFac.BESTELLSTATUS_STORNIERT)) {
				// Statuswechsel 'Storniert' -> 'Angelegt' : Ausgeloest durch
				// Button Update
				if (DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("bes.status.aufanlegensetzen"),
								LPMain.getTextRespectUISPr("lp.hint"))) {

					DelegateFactory.getInstance().getBestellungDelegate()
							.stornoAufheben(getBesDto().getIId());

					/** @todo JO so kompliziert PJ 5199 */
					// den geaenderten Status anzeigen
					setBestellungDto(DelegateFactory.getInstance()
							.getBestellungDelegate()
							.bestellungFindByPrimaryKey(getBesDto().getIId()));
					getBestellungKopfdaten().eventYouAreSelected(false);
				}
			}

			else if (cNrStatus.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
				// Statuswechsel 'Erledigt' -> 'Geliefert' : Ausgeloest durch
				// Button Update
				if (DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("bes.status.auf_geliefert_setzen"),
								LPMain.getTextRespectUISPr("lp.hint"))) {

					getBesDto().setStatusCNr(
							BestellungFac.BESTELLSTATUS_GELIEFERT);
					DelegateFactory.getInstance().getBestellungDelegate()
							.updateBestellung(getBesDto());

					/** @todo JO so kompliziert PJ 5199 */
					// den geaenderten Status anzeigen
					setBestellungDto(DelegateFactory.getInstance()
							.getBestellungDelegate()
							.bestellungFindByPrimaryKey(getBesDto().getIId()));
					getBestellungKopfdaten().eventYouAreSelected(false);
				}
			}

			else if (cNrStatus.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)) {
				// Statuswechsel 'Geliefert' -> 'Offen'
				try {
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("bes.bestellunggeliefert"));

					/** @todo JO ??? PJ 5199 */
					String sStatus = HelperBestellung
							.showBestellstatusWennVonGeliefertAufOffenOderBestaetigt(
									getBesDto(), getInternalFrameBestellung());

					mf.setLocale(LPMain.getTheClient().getLocUi());

					Object pattern[] = { " " + sStatus };
					String sMsg = mf.format(pattern);

					boolean bAnswer = DialogFactory.showModalJaNeinDialog(
							getInternalFrame(), sMsg,
							LPMain.getTextRespectUISPr("lp.hint"));
					if (bAnswer) {
						DelegateFactory.getInstance().getBestellungDelegate()
								.erledigungAufheben(getBesDto().getIId());
						refreshAktuellesPanel();
					}
				} catch (ExceptionLP t) {
					// hier wird exception vom server gefangen betreff
					// manuellerledigen
					if (t.getICode() == EJBExceptionLP.FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("bes.offensetzengehtnichtweilnichtmanuellerledigt"));
					} else {
						handleException(t, true);
					}
				} catch (Throwable t) {
					handleException(t, true);
				}
			}

			else if (cNrStatus.equals(BestellungFac.BESTELLSTATUS_OFFEN)) {
				// Statuswechsel 'Offen' -> 'Angelegt'
				cNrStatusNext = BestellungFac.BESTELLSTATUS_ANGELEGT;
				if (DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.berechneAnzahlMengenbehafteteBestellpositionen(
								getBesDto().getIId()) == 0) {
					// keine mengenbehafteten BSPOS
					bStatusChangeOK = true;
				} else {
					if (DelegateFactory.getInstance().getWareneingangDelegate()
							.getAnzahlWE(getBesDto().getIId())
							.equals(new Integer(0))) {
						// keine WEs
						boolean bAnswer = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("bes.bestellungoffen"),
										LPMain.getTextRespectUISPr("lp.warning"));
						if (bAnswer) {
							bStatusChangeOK = true;
						}
					} else {
						// hat WEs
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.warning"),
										LPMain.getTextRespectUISPr("bes.warning.hatbereitswareneingaenge"));
					}
				}
			}

			else {
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("bes.warning.bestellungkannnichtgeaendertwerden"));

				mf.setLocale(LPMain.getTheClient().getLocUi());
				// hier werden parameter uebergeben (siehe auch Resourcebundle)
				Object pattern[] = { getBesDto().getStatusCNr() };

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						mf.format(pattern));
			}
		}

		if (bStatusChangeOK) {

			// auch im Zwischenpuffer setzen
			getBesDto().setStatusCNr(cNrStatusNext);

			// in db sofort setzen
			DelegateFactory.getInstance().getBestellungDelegate()
					.updateBestellung(getBesDto());

		}
		return bStatusChangeOK;
	}

	@Override
	protected void initDtos() throws Throwable {
		initializeDtos(getBesDto().getIId());
	}

	/**
	 * Diese Methode prueft den Status des aktuellen Bestellung und legt fest,
	 * ob eine Aenderung in den Kopfdaten bzw. Konditionen erlaubt ist.
	 * 
	 * @return boolean true, wenn ein update erlaubt ist
	 * @throws Throwable
	 */
	public boolean istAktualisierenBestellungErlaubt() throws Throwable {
		boolean bIstAenderungErlaubtO = true;

		if (isBSangelegtDlg()) {
			if (getBesDto().getStatusCNr().equals(
					BestellungFac.BESTELLSTATUS_STORNIERT)
					|| getBesDto().getStatusCNr().equals(
							BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
				bIstAenderungErlaubtO = false;
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("bes.warning.bestellungkannnichtgeaendertwerden"));

				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { getBesDto().getStatusCNr() };

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						mf.format(pattern));
			} else {
				if (BestellungFac.BESTELLSTATUS_OFFEN.equals(getBesDto()
						.getStatusCNr())
						|| BestellungFac.BESTELLSTATUS_BESTAETIGT
								.equals(getBesDto().getStatusCNr())) {
					Object[] options = {
							LPMain.getInstance().getTextRespectUISPr("lp.ja"),
							LPMain.getInstance().getTextRespectUISPr("lp.nein"),
							LPMain.getInstance().getTextRespectUISPr(
									"bes.aenderungsbestellung") };
					int iOption = DialogFactory.showModalDialog(
							getInternalFrame(),
							LPMain.getInstance().getTextRespectUISPr(
									"bes.bestellungoffen"), "", options,
							options[1]);
					if (iOption == JOptionPane.YES_OPTION) {
						bIstAenderungErlaubtO = true;
					} else if (iOption == JOptionPane.NO_OPTION) {
						bIstAenderungErlaubtO = false;
					} else if (iOption == JOptionPane.CANCEL_OPTION) {
						// TODO tAenderungsbestellung setzen
						bIstAenderungErlaubtO = true;
						getBesDto().setTAenderungsbestellung(
								new Timestamp(System.currentTimeMillis()));
						DelegateFactory.getInstance().getBestellungDelegate()
								.updateBestellung(getBesDto());
					}
				} else {
					bIstAenderungErlaubtO = true;
				}
			}
		}
		return bIstAenderungErlaubtO;
	}

	/**
	 * Fuer lazy loading.
	 * 
	 * @return PanelBasis
	 * @throws Throwable
	 */
	private PanelBasis getBestellungKopfdaten() throws Throwable {
		if (panelBestellungKopfdatenD == null) {
			panelBestellungKopfdatenD = new PanelBestellungKopfdaten(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.kopfdaten"),
					getInternalFrame().getKeyWasForLockMe()); // empty bei
			// leerer liste
			setComponentAt(IDX_PANEL_KOPFDATEN, panelBestellungKopfdatenD);
		}
		return panelBestellungKopfdatenD;
	}

	// private void enableSichtLieferantTermine() {
	// if (getBestellungDto() != null && getBestellungDto().getIId() != null) {
	// if (getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac.
	// BESTELLSTATUS_ANGELEGT) ||
	// getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac.
	// BESTELLSTATUS_STORNIERT)) {
	// enableSichLieferantenTermine(false);
	// }
	// else {
	// enableSichLieferantenTermine(true);
	// }
	// }
	// }

	// /**
	// * ausgrauen von WE und WEP wenn bedingung erfuellt
	// * @throws Throwable
	// */
	// private void enableWareneingangPanels()
	// throws Throwable {
	// /**
	// * @todo MB->JE hier crashts andauernd, deswegen if-block PJ 5200
	// */
	// if (getBestellungDto() != null) {
	// Integer iIdBestellungI = getBestellungDto().getIId();
	// //hier wird WEP und WE ausgegraut
	// if (getBestellungDto().getIId() != null) {
	// if (getBestellungDto().getBestellungartCNr().equals(BestellungFac.
	// BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
	// enableWE(false);
	// enableWEPOS(false);
	// }
	// else if ( (DelegateFactory.getInstance().getBestellungDelegate().
	// getAnzahlBestellpositionen( (Integer) iIdBestellungI)).equals(new
	// Integer(0))) {
	// // WE-Panel ausgrauen.
	// enableWE(false);
	// }
	// if (
	// (DelegateFactory.getInstance().getWareneingangDelegate().getAnzahlWE( (
	// Integer) iIdBestellungI)).equals(new Integer(0))) {
	// // WEP-Panel ausgrauen.
	// enableWEPOS(false);
	// }
	// }
	// }
	// }

	/**
	 * Diese Methode prueft, ob zur aktuellen Bestellung Konditionen erfasst
	 * wurden.
	 * 
	 * @return boolean
	 * @throws Throwable
	 */
	private boolean pruefeKonditionen() throws Throwable {

		boolean bHatKF = true;
		// parameter holen und dann ein if rund um das was schon da war
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN);
		Short sValue = new Short(parameter.getCWert());
		if (Helper.short2boolean(sValue)) {
			if (isBSangelegtDlg()) {
				if (getBesDto().getBestelltextIIdKopftext() != null
						&& getBesDto().getBestelltextIIdFusstext() != null) {
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("lp.hint.konditionenerfassen"));
					bHatKF = false;
				}
			}
		} else {
			initKonditionen();
		}
		return bHatKF;
	}

	public boolean pruefeAktuellenBestellung() {
		boolean bIstGueltig = true;

		if (getBesDto() == null || getBesDto().getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("bes.warning.keinebestellung"));
		}

		return bIstGueltig;
	}

	/**
	 * initKonditionen
	 * 
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private void initKonditionen() throws ExceptionLP, Throwable {
		initTexte(); // Kopf- und Fusstext werden initialisiert

		getBesDto().setBestelltextIIdKopftext(kopftextDto.getIId());
		getBesDto().setBestelltextIIdFusstext(fusstextDto.getIId());
		// Wird nur von printBestellung aufgerufen welches selber danach das
		// Update aufruft
		// DelegateFactory.getInstance().getBestellungDelegate()
		// .updateBestellung(getBesDto());
	}

	public JMenuBar getJMenuBar() throws Throwable {
		// usemenubar 3: Nachfolgende Zeile erzeugt Standard- Symbolleiste
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);

		// Menue Datei; ein neuer Eintrag wird immer oben im Menue eingetragen
		JMenu jmModul = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_MONATSBESTELLUNGSART,
						ParameterFac.KATEGORIE_BESTELLUNG,
						LPMain.getTheClient().getMandant());
		int iMonatsbestellungsart = ((Integer) parameter.getCWertAsObject());

		String textMonatsbestellung = LPMain
				.getTextRespectUISPr("bes.monatsbestellung");
		if (iMonatsbestellungsart == 0) {
			textMonatsbestellung = LPMain
					.getTextRespectUISPr("bes.monatsbestellung.mitwe");
		} else if (iMonatsbestellungsart == 1) {
			textMonatsbestellung = LPMain
					.getTextRespectUISPr("bes.monatsbestellung.ohnewe");
		} else if (iMonatsbestellungsart == 2) {
			textMonatsbestellung = LPMain
					.getTextRespectUISPr("bes.monatsbestellung.bestellungsimport");
		}

		JMenuItem menuItemDateiAlleAngelegtenVersenden = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr(
						"bes.bestellung.alleangelegtendrucken"));
		menuItemDateiAlleAngelegtenVersenden.addActionListener(this);

		menuItemDateiAlleAngelegtenVersenden
				.setActionCommand(MY_OWN_NEW_ALLE_ANGELEGTEN_BESTELLUNGEN_VERSENDEN);
		jmModul.add(menuItemDateiAlleAngelegtenVersenden, 0);

		JMenuItem menuItemDateiMonatsbestellung = new JMenuItem(
				textMonatsbestellung);
		menuItemDateiMonatsbestellung.addActionListener(this);
		menuItemDateiMonatsbestellung
				.setActionCommand(MENU_ACTION_DATEI_MONATSBESTELLUNG);
		jmModul.add(menuItemDateiMonatsbestellung, 0);

		JMenuItem menuItemDateiAbholauftrag = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.datei.abholauftrag"));
		menuItemDateiAbholauftrag.addActionListener(this);
		menuItemDateiAbholauftrag
				.setActionCommand(MENU_ACTION_DATEI_ABHOLAUFTRAG);
		jmModul.add(menuItemDateiAbholauftrag, 0);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF)) {
			JMenuItem menuItemDateiBestellung = new JMenuItem(
					LPMain.getTextRespectUISPr("bes.menu.datei.bestellung"));
			menuItemDateiBestellung.addActionListener(this);
			menuItemDateiBestellung
					.setActionCommand(MENU_ACTION_DATEI_BESTELLUNG);
			jmModul.add(menuItemDateiBestellung, 0);
		}

		// Menue Bearbeiten
		JMenu jmBearbeiten = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		JMenuItem menuItemOffeneWEPos = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.offenewepos.uebersichtoffene"));
		menuItemOffeneWEPos.addActionListener(this);
		menuItemOffeneWEPos
				.setActionCommand(MENU_ACTION_BEARBEITEN_OFFENE_WEPOS);
		jmBearbeiten.add(menuItemOffeneWEPos, 0);

		JMenuItem menuItemBearbeitenAuftragzuordnung = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.auftragzuordnungaendern"));
		menuItemBearbeitenAuftragzuordnung.addActionListener(this);
		menuItemBearbeitenAuftragzuordnung
				.setActionCommand(MENU_ACTION_BEARBEITEN_AUFTRAGSZUORDNUNG);
		jmBearbeiten.add(menuItemBearbeitenAuftragzuordnung, 0);

		JMenuItem menuItemBearbeitenMahnsperreBis = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.mahnsperrebis"));
		menuItemBearbeitenMahnsperreBis.addActionListener(this);
		menuItemBearbeitenMahnsperreBis
				.setActionCommand(MENU_ACTION_BEARBEITEN_BSMAHNSPERRE_SETZEN);
		jmBearbeiten.add(menuItemBearbeitenMahnsperreBis, 0);

		JMenuItem menuItemBearbeitenWEPKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.wepkommentar"));
		menuItemBearbeitenWEPKommentar.addActionListener(this);
		menuItemBearbeitenWEPKommentar
				.setActionCommand(MENU_BEARBEITEN_WEP_INTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenWEPKommentar, 0);

		// 7 Menueeintrag fuer externen Kommentar
		JMenuItem menuItemBearbeitenExternerKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.externerkommentar"));
		menuItemBearbeitenExternerKommentar.addActionListener(this);
		menuItemBearbeitenExternerKommentar
				.setActionCommand(MENU_BEARBEITEN_EXTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenExternerKommentar, 0);

		// 6 Menueeintrag fuer internen Kommentar
		JMenuItem menuItemBearbeitenInternerKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.internerkommentar"));
		menuItemBearbeitenInternerKommentar.addActionListener(this);
		menuItemBearbeitenInternerKommentar
				.setActionCommand(MENU_BEARBEITEN_INTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenInternerKommentar, 0);

		jmBearbeiten.add(new JSeparator(), 0);

		JMenuItem menuItemBearbeitenManuellErledigen = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.bearbeiten.manuellerledigen"));
		menuItemBearbeitenManuellErledigen.addActionListener(this);
		menuItemBearbeitenManuellErledigen
				.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN);
		jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);

		JMenuItem menuItemBearbeitenManuellStornoAufheben = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.bearbeiten.stornoaufheben"));
		menuItemBearbeitenManuellStornoAufheben.addActionListener(this);
		menuItemBearbeitenManuellStornoAufheben
				.setActionCommand(MENU_BEARBEITEN_STORNO_AUFHEBEN);
		jmBearbeiten.add(menuItemBearbeitenManuellStornoAufheben, 0);

		JMenuItem menuItemBearbeitenManuellErledigenAufheben = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.bearbeiten.erledigungaufheben"));
		menuItemBearbeitenManuellErledigenAufheben.addActionListener(this);
		menuItemBearbeitenManuellErledigenAufheben
				.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN);
		jmBearbeiten.add(menuItemBearbeitenManuellErledigenAufheben, 0);

		JMenuItem menuItemAlle = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.allebestellungen"));
		menuItemAlle.addActionListener(this);
		menuItemAlle.setActionCommand(MENU_ACTION_ALLE);
		JMenu journal = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_JOURNAL);
		journal.add(menuItemAlle);

		JMenuItem menuItemOffene = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.offene"));
		menuItemOffene.addActionListener(this);
		menuItemOffene.setActionCommand(MENU_ACTION_JOURNAL_OFFENE);
		journal.add(menuItemOffene);

		JMenuItem menuItemWareneingang = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.wareneingang"));
		menuItemWareneingang.addActionListener(this);
		menuItemWareneingang.setActionCommand(MENU_ACTION_JOURNAL_WARENEINGANG);
		journal.add(menuItemWareneingang);

		return wrapperMenuBar;
	}

	public void refreshBestellungPositionSichtRahmen() throws Throwable {
		FilterKriterium[] filtersPos = null;

		if (getBesDto() != null) {
			String besArt = getBesDto().getBestellungartCNr();

			if (besArt != null
					&& besArt
							.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {

				filtersPos = BestellungFilterFactory.getInstance()
						.createFKBestellpositionSichtRahmen(
								getBesDto().getIId());
			}
			if (besArt != null
					&& besArt
							.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

				filtersPos = BestellungFilterFactory
						.getInstance()
						.createFKBestellpositionSichtRahmenAusAbruf(
								getBesDto().getIBestellungIIdRahmenbestellung());
			}
		}

		if (panelBestellungPositionSichtRahmenSP6 == null) {
			panelBestellungPositionSichtRahmenBottomD6 = new PanelBestellungPositionSichtRahmen(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.sichtrahmen"), null);

			panelBestellungPositionSichtRahmenTopQP6 = new PanelQuery(null,
					filtersPos,
					QueryParameters.UC_ID_BESTELLPOSITIONSICHTRAHMEN, null,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.sichtrahmen"), true);

			panelBestellungPositionSichtRahmenSP6 = new PanelSplit(
					getInternalFrame(),
					panelBestellungPositionSichtRahmenBottomD6,
					panelBestellungPositionSichtRahmenTopQP6, 200);

			setComponentAt(IDX_PANEL_BESTELLPOSITIONSICHTRAHMEN,
					panelBestellungPositionSichtRahmenSP6);
		}

		panelBestellungPositionSichtRahmenTopQP6.setDefaultFilter(filtersPos);
	}

	// public void showPanelDialogDivisor()
	// throws Throwable {
	// if (pdDivisor == null) {
	// pdDivisor = new PanelDialogBestellungDivisor(
	// getInternalFrame(),
	// LPMain.getInstance().getTextRespectUISPr("bes.divisor"));
	// }
	// getInternalFrame().showPanelDialog(pdDivisor);
	// }

	// public void gotoSichtRahmen()
	// throws Throwable {
	// setSelectedComponent(refreshBestellungPositionSichtRahmen());
	// setEnabledAt(IDX_PANEL_BESTELLPOSITIONSICHTRAHMEN, true);
	//
	// // alles richtig schalten, auch wenn die Liste der Abrufpositionen vorher
	// leer war
	// panelBestellungPositionSichtRahmenSP6.eventYouAreSelected(false); //
	// refresh auf das gesamte 1:n panel
	//
	// if (getBestellungDto().getBestellungartCNr().equals(BestellungFac.
	// BESTELLUNGART_ABRUFBESTELLUNG_C_NR) &&
	// getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac.
	// BESTELLSTATUS_OFFEN) ||
	// getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac.
	// BESTELLSTATUS_BESTAETIGT)) {
	// enableWE(true);
	// enableWEPOS(true);
	// }
	// else {
	// enableWE(false);
	// enableWEPOS(false);
	// }
	// }

	public void enablePanels(BestellungDto bsDtoI, boolean bDisableOnlyI)
			throws Throwable {

		String cNrStatusTemp = (bsDtoI.getStatusCNr() == null) ? BestellungFac.BESTELLSTATUS_ANGELEGT
				: bsDtoI.getStatusCNr();
		String cBSArtTemp = (bsDtoI.getBestellungartCNr() == null) ? "novalue"
				: bsDtoI.getBestellungartCNr();

		// SK Hierher verschoben damit nur 1x zur DB gegangen wird
		boolean bEsGIbtWareneingaenge = false;
		if (getBesDto().getIId() != null) {
			bEsGIbtWareneingaenge = DelegateFactory.getInstance()
					.getWareneingangDelegate()
					.getAnzahlWE(getBesDto().getIId()).intValue() > 0;
		}

		boolean bWE = isbWechselkurseOK()
				&& !(cBSArtTemp
						.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR))
				&& !(cNrStatusTemp.equals(BestellungFac.BESTELLSTATUS_ANGELEGT) || cNrStatusTemp
						.equals(BestellungFac.BESTELLSTATUS_STORNIERT))
				&& (DelegateFactory.getInstance().getBestellungDelegate()
						.getAnzahlBestellpositionen(getBesDto().getIId())
						.intValue() > 0)
				// SK Wenn es schon WEs zur BEstellung gibt muessen diese auch
				// angezeigt werden (Kann durch Statusaenderungen auftreten)
				|| bEsGIbtWareneingaenge;

		boolean bWEPOS = false;

		if (isbWechselkurseOK()
				&& !(cBSArtTemp
						.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR))
				&& !(cNrStatusTemp.equals(BestellungFac.BESTELLSTATUS_ANGELEGT) || cNrStatusTemp
						.equals(BestellungFac.BESTELLSTATUS_STORNIERT))
				&& bEsGIbtWareneingaenge) {
			bWEPOS = true;
		}

		boolean bBSPOS = isbWechselkurseOK()
		/** @todo JO->WH PJ 5208 */
		// && ! (cNrStatusTemp.equals(BestellungFac.BESTELLSTATUS_ANGELEGT)
		// ||
		// cNrStatusTemp.equals(BestellungFac.BESTELLSTATUS_STORNIERT))
		;

		boolean bSichLieferantenTermine = isbWechselkurseOK()
				&& !(cNrStatusTemp.equals(BestellungFac.BESTELLSTATUS_ANGELEGT) || cNrStatusTemp
						.equals(BestellungFac.BESTELLSTATUS_STORNIERT));

		boolean bKonditionen = isbWechselkurseOK();

		boolean bRahmen = isbWechselkurseOK()
				// && !
				// cNrStatusTemp.equals(BestellungFac.BESTELLSTATUS_ANGELEGT)
				&& (cBSArtTemp
						.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR) || (cBSArtTemp
						.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)));

		if (bDisableOnlyI) {
			// disable mode: dh. nur die false sind "nachfalsen" -> noch mehr
			// abdrehen.
			if (!bBSPOS) {
				enableBSPOS(bBSPOS);
			}
			if (!bKonditionen) {
				enableKonditionen(bKonditionen);
			}

			if (!bSichLieferantenTermine) {
				enableSichLieferantenTermine(bSichLieferantenTermine);
			}
			if (!bRahmen) {
				enableSichtRahmen(bRahmen);
			}
		} else {
			enableBSPOS(bBSPOS);
			enableKonditionen(bKonditionen);

			enableSichLieferantenTermine(bSichLieferantenTermine);
			enableSichtRahmen(bRahmen);
		}
	}

	public void showStatusMessage(String lpTitle, String lpMessage)
			throws Throwable {
		MessageFormat mf = new MessageFormat(
				LPMain.getTextRespectUISPr(lpMessage));
		mf.setLocale(LPMain.getTheClient().getLocUi());
		Object pattern[] = { getBesDto().getStatusCNr() };
		DialogFactory.showModalDialog(LPMain.getTextRespectUISPr(lpTitle),
				mf.format(pattern));
	}

	public void setbWechselkurseOK(boolean bWechselkurseOKI) {
		this.bWechselkurseOK = bWechselkurseOKI;
	}

	public void setFusstextDto(BestellungtextDto fusstextDto) {
		this.fusstextDto = fusstextDto;
	}

	public void setKopftextDto(BestellungtextDto kopftextDto) {
		this.kopftextDto = kopftextDto;
	}

	public void setRahmBesDto(BestellungDto rahmBesDtoI) {
		this.rahmBesDto = rahmBesDtoI;
	}

	public boolean isbWechselkurseOK() {
		return bWechselkurseOK;
	}

	public BestellungtextDto getFusstextDto() {
		return fusstextDto;
	}

	public BestellungtextDto getKopftextDto() {
		return kopftextDto;
	}

	public BestellungDto getRahmBesDto() {
		return rahmBesDto;
	}

	public PanelQuery getPanelBestellungAuswahlQP1() {
		return panelBestellungAuswahlQP1;
	}

	public PanelSplit getPanelBestellungPositionSichtRahmenSP6() {
		return panelBestellungPositionSichtRahmenSP6;
	}

	public PanelQuery getPanelBestellungWepQP5() {
		return panelBestellungWEPTopQP5;
	}

	public PanelQuery getPanelAuswahl() {
		setSelectedIndex(IDX_PANEL_AUSWAHL);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return panelBestellungAuswahlQP1;
	}

	public PanelSplit getPanelPositionen() {
		setSelectedIndex(IDX_PANEL_BESTELLPOSITION);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return panelBestellungPositionenSP3;
	}

	public PanelBestellungKonditionen getPanelKonditionen() {
		setSelectedIndex(IDX_PANEL_KONDITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return panelBestellungKonditionenD4;
	}

	public PanelSplit getPanelWareneingang() {
		setSelectedIndex(IDX_PANEL_WARENEINGANG);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return panelBestellungWareineingangSP4;
	}

	public PanelSplit getPanelWareneingangPositionen() {
		setSelectedIndex(IDX_PANEL_WARENEINGANGSPOSITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return panelBestellungWEPSP5;
	}

	public PanelSplit getPanelLieferrantentermine() {
		setSelectedIndex(IDX_PANEL_SICHTLIEFERTERMINE);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return panelBestellungSichtLieferTermineSP7;
	}

	public Object getInseratDto() {
		return getBesDto();
	}

	public void setTitle() {
		String sTitle;
		if (getBesDto() == null || getBesDto().getIId() == null) {
			sTitle = LPMain.getTextRespectUISPr("best.title.neuebestellung");
		} else {
			// die zugehoerige Rahmenbestellung auch anzeigen.
			if (rahmBesDto.getIId() != null) {
				sTitle = LPMain.getTextRespectUISPr("bes.rahmenbestellung")
						+ ": " + rahmBesDto.getCNr() + " /  *";
			} else {
				sTitle = "";
			}
			// Bestellnummer und Namen des Lieferanten dranhaengen.
			sTitle += getBesDto().getCNr();
			if (getLieferantDto() != null) {
				sTitle += " ";
				sTitle += getLieferantDto().getPartnerDto()
						.formatFixTitelName1Name2();
			}
		}
		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	public boolean aktualisiereBestellungsstatus(BestellungDto bestellungDto)
			throws Throwable {
		if (bestellungDto != null) {
			if (bestellungDto.getStatusCNr().equals(
					BestellungFac.BESTELLSTATUS_ANGELEGT)) {
				return true;
			} else if (bestellungDto.getStatusCNr().equals(
					BestellungFac.BESTELLSTATUS_STORNIERT)) {
				boolean bStornieren = (DialogFactory.showMeldung(
						LPMain.getInstance().getTextRespectUISPr(
								"fert.frage.stornoaufheben"), LPMain
								.getInstance().getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bStornieren == true) {
					DelegateFactory.getInstance().getBestellungDelegate()
							.stornoAufheben(bestellungDto.getIId());
				}
				return bStornieren;
			} else if (bestellungDto.getStatusCNr().equals(
					BestellungFac.BESTELLSTATUS_OFFEN)) {
				boolean bZuruecknehmen = (DialogFactory.showMeldung("", LPMain
						.getInstance().getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bZuruecknehmen == true) {

				}
				return bZuruecknehmen;
			}

			else if (bestellungDto.getStatusCNr().equals(
					BestellungFac.BESTELLSTATUS_BESTAETIGT)
					|| bestellungDto.getStatusCNr().equals(
							BestellungFac.BESTELLSTATUS_ERLEDIGT)
					|| bestellungDto.getStatusCNr().equals(
							BestellungFac.BESTELLSTATUS_GELIEFERT)) {
				if (DialogFactory.showMeldung(
						LPMain.getInstance().getTextRespectUISPr(
								"rech.rechnungistbereitserledigt"), LPMain
								.getInstance().getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

				}
				return false;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public BigDecimal getWareneingangWertsumme(WareneingangDto wareneingangDto) {
		BigDecimal bdReturn = new BigDecimal(0);
		if (wareneingangDto != null && wareneingangDto.getIId() != null) {
			try {
				Integer iWareneingangIId = wareneingangDto.getIId();
				WareneingangspositionDto[] wePos = DelegateFactory
						.getInstance()
						.getWareneingangDelegate()
						.wareneingangspositionFindByWareneingangIId(
								iWareneingangIId);
				if (wePos.length > 0) {

					for (int i = 0; i < wePos.length; i++) {

						BigDecimal bdEinstandspreisUngerundet = DelegateFactory
								.getInstance()
								.getWareneingangDelegate()
								.getBerechnetenEinstandspreisEinerWareneingangsposition(
										wePos[i].getIId());

						if (bdEinstandspreisUngerundet != null) {
							if (wePos[i].getNGeliefertemenge() != null) {
								BigDecimal bdPosSumme = wePos[i]
										.getNGeliefertemenge().multiply(
												bdEinstandspreisUngerundet);
								bdReturn = bdReturn.add(bdPosSumme);
							}
						}
					}
				}

			} catch (Throwable e) {
				// Nothing we just return null
			}
		}
		if (bdReturn != null) {
			bdReturn = bdReturn.setScale(4, BigDecimal.ROUND_HALF_EVEN);
		}
		return bdReturn;
	}

}
