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
package com.lp.client.anfrage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.StatusDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>TabbedPane fuer Modul Anfrage.</p> <p>Copyright Logistik Pur Software GmbH
 * (c) 2004-2008</p> <p>Erstellungsdatum 07.06.05</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.25 $
 */
public class TabbedPaneAnfrage extends TabbedPane implements ICopyPaste {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery anfrageAuswahl = null;
	private PanelAnfrageKopfdaten anfrageKopfdaten = null;
	private PanelBasis anfrageKonditionen = null;

	private PanelQuery anfragePositionenTop = null;
	private PanelAnfragePositionen anfragePositionenBottom = null;
	private PanelSplit anfragePositionen = null; // FLR 1:n Liste

	private PanelQuery anfragePositionenlieferdatenTop = null;
	private PanelAnfragepositionlieferdaten anfragePositionenlieferdatenBottom = null;
	private PanelSplit anfragePositionenlieferdaten = null; // FLR 1:n Liste

	private PanelQueryFLR panelQueryFLRLiefergruppeAuswahl = null;

	public static final int IDX_PANEL_AUSWAHL = 0;
	private static final int IDX_PANEL_KOPFDATEN = 1;
	private static final int IDX_PANEL_POSITIONEN = 2;
	private static final int IDX_PANEL_POSITIONENLIEFERDATEN = 3;
	private static final int IDX_PANEL_KONDITIONEN = 4;

	// dtos, die in mehr als einem panel benoetigt werden
	private AnfrageDto anfrageDto = null;
	private LieferantDto lieferantDto = null;
	private LfliefergruppeDto liefergruppeDto = null;

	private static final String MENU_ACTION_DATEI_ANFRAGE = "MENU_ACTION_DATEI_ANFRAGE";

	private static final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN";

	private static final String MENU_JOURNAL_ANFRAGE_LIEFERDATENUEBERSICHT = "MENU_JOURNAL_ANFRAGE_LIEFERDATENUEBERSICHT";

	private static final String EXTRA_NEU_LIEFERGRUPPENANFRAGE = "extra_neu_liefergruppenanfrage";
	private static final String EXTRA_ANFRAGEN_ZU_LIEFERGRUPPENANFRAGE = "extra_anfragen_zu_liefergruppenanfrage";

	private static final String MY_OWN_NEW_ALLE_ANGELEGTEN_LIEFERGRUPPENANFRAGEN_VERSENDEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "_ALLE_ANGELEGTEN_LIEFERGRUPPENANFRAGEN_VERSENDEN";

	private static final String MY_OWN_NEW_LIEFERGRUPPENANFRAGE = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_LIEFERGRUPPENANFRAGE;
	private static final String MY_OWN_NEW_ANFRAGEN_ZU_LIEFERGRUPPENANFRAGE = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_ANFRAGEN_ZU_LIEFERGRUPPENANFRAGE;

	private final static String ACTION_SPECIAL_RUECKPFLEGE = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_RUECKPFLEGE";

	private static final String ACTION_SPECIAL_NEU_AUS_PROJEKT = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_NEU_AUS_PROJEKT";

	private final static String ACTION_SPECIAL_RUECKPFLEGE_ALS_ERSTER = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_RUECKPFLEGE_ALS_ERSTER";

	private PanelQueryFLR panelQueryFLRProjekt = null;

	public TabbedPaneAnfrage(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("anf.anfrage"));
		jbInit();
		initComponents();
	}

	public InternalFrameAnfrage getInternalFrameAnfrage() throws Throwable {
		return (InternalFrameAnfrage) getInternalFrame();
	}

	public AnfrageDto getAnfrageDto() {
		return anfrageDto;
	}

	public void setAnfrageDto(AnfrageDto anfrageDtoI) {
		anfrageDto = anfrageDtoI;
	}

	public LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	public void setLieferantDto(LieferantDto lieferantDtoI) {
		lieferantDto = lieferantDtoI;
	}

	public LfliefergruppeDto getLiefergruppeDto() {
		return liefergruppeDto;
	}

	public void setLiefergruppeDto(LfliefergruppeDto liefergruppeDtoI) {
		liefergruppeDto = liefergruppeDtoI;
	}

	public PanelQuery getAnfrageAuswahl() {
		return anfrageAuswahl;
	}

	public PanelAnfrageKopfdaten getAnfrageKopfdaten() {
		return anfrageKopfdaten;
	}

	public PanelBasis getAnfrageKonditionen() {
		return anfrageKonditionen;
	}

	public PanelQuery getAnfragePositionenTop() {
		return anfragePositionenTop;
	}

	public PanelBasis getAnfragePositionenBottom() {
		return anfragePositionenBottom;
	}

	public PanelQuery getAnfragePositionenlieferdatenTop() {
		return anfragePositionenlieferdatenTop;
	}

	public PanelBasis getAnfragePositionenlieferdatenBottom() {
		return anfragePositionenlieferdatenBottom;
	}

	private void jbInit() throws Throwable {
		// dafuer sorgen, dass die Dtos != null sind
		resetDtos();

		insertTab(
				LPMain.getInstance().getTextRespectUISPr("anf.panel.auswahl"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"anf.panel.auswahl.tooltip"), IDX_PANEL_AUSWAHL);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr("anf.panel.kopfdaten"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"anf.panel.kopfdaten.tooltip"), IDX_PANEL_KOPFDATEN);

		insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("anf.panel.positionen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"anf.panel.positionen.tooltip"), IDX_PANEL_POSITIONEN);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"anf.panel.positionlieferdaten"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"anf.panel.positionlieferdaten.tooltip"),
				IDX_PANEL_POSITIONENLIEFERDATEN);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"anf.panel.konditionen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"anf.panel.konditionen.tooltip"), IDX_PANEL_KONDITIONEN);

		// die Liste der Anfragen aufbauen und sofort laden
		refreshAnfrageAuswahl();

		// die aktuell gewaehlten Anfrage hinterlegen
		setKeyWasForLockMe();

		// die aktuell gewaehlte Anfrage im Titel anzeigen
		setTitleAnfrage(LPMain.getInstance().getTextRespectUISPr(
				"anf.panel.auswahl"));

		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	private void refreshAnfrageAuswahl() throws Throwable {
		if (anfrageAuswahl == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			anfrageAuswahl = new PanelQuery(AnfrageFilterFactory.getInstance()
					.createQTPanelAnfrageauswahl(), SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_ANFRAGE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.panel.auswahl"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde

			anfrageAuswahl
					.befuellePanelFilterkriterienDirekt(AnfrageFilterFactory
							.getInstance().createFKDAnfragenummer(),
							AnfrageFilterFactory.getInstance()
									.createFKDLieferantName());

			anfrageAuswahl.addDirektFilter(AnfrageFilterFactory.getInstance()
					.createFKDProjekt());

			// New Button fuer Liefergruppenanfrage
			anfrageAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/index_new.png",
					LPMain.getInstance().getTextRespectUISPr(
							"anf.tooltip.liefergruppenanfrage"),
					MY_OWN_NEW_LIEFERGRUPPENANFRAGE, null);

			anfrageAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/index_add.png",
					LPMain.getInstance().getTextRespectUISPr(
							"anf.tooltip.anfragenzuliefergruppenanfrage"),
					MY_OWN_NEW_ANFRAGEN_ZU_LIEFERGRUPPENANFRAGE, null);

			anfrageAuswahl
					.befuelleFilterkriteriumSchnellansicht(AnfrageFilterFactory
							.getInstance().createFKAnfrageSchnellansicht());

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
				anfrageAuswahl.createAndSaveAndShowButton(
						"/com/lp/client/res/briefcase2_document16x16.png",
						LPMain.getTextRespectUISPr("angb.neuausprojekt"),
						ACTION_SPECIAL_NEU_AUS_PROJEKT,
						RechteFac.RECHT_ANF_ANFRAGE_CUD);
			}

			setComponentAt(IDX_PANEL_AUSWAHL, anfrageAuswahl);
		}

		anfrageAuswahl.eventYouAreSelected(false);
	}

	private PanelAnfrageKopfdaten refreshAnfrageKopfdaten() throws Throwable {
		Integer iIdAnfrage = null;

		if (anfrageKopfdaten == null) {

			// Die Anfrage hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAnfrage = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			anfrageKopfdaten = new PanelAnfrageKopfdaten(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"anf.panel.kopfdaten"), iIdAnfrage); // empty bei
			// leerer
			// auftragsliste
			setComponentAt(IDX_PANEL_KOPFDATEN, anfrageKopfdaten);
		}

		return anfrageKopfdaten;
	}

	private PanelSplit refreshAnfragePositionen() throws Throwable {
		if (anfragePositionen == null) {
			anfragePositionenBottom = new PanelAnfragePositionen(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.panel.positionen"), null); // eventuell
			// gibt
			// es
			// noch
			// keine
			// position

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AnfrageFilterFactory
					.getInstance().createFKFlranfrageiid(anfrageDto.getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			anfragePositionenTop = new PanelQuery(qtPositionen,
					filtersPositionen, QueryParameters.UC_ID_ANFRAGEPOSITION,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.panel.positionen"), true); // flag
			// ,
			// damit
			// flr
			// erst
			// beim
			// aufruf
			// des
			// panels
			// loslaeuft

			anfragePositionen = new PanelSplit(getInternalFrame(),
					anfragePositionenBottom, anfragePositionenTop, 200);

			setComponentAt(IDX_PANEL_POSITIONEN, anfragePositionen);
		}

		anfragePositionenTop.setMultipleRowSelectionEnabled(true);

		return anfragePositionen;
	}

	private PanelSplit getAnfragePositionenlieferdaten() throws Throwable {
		if (anfragePositionenlieferdaten == null) {
			anfragePositionenlieferdatenBottom = new PanelAnfragepositionlieferdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"anf.panel.positionlieferdaten"), null);

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = null;

			if (anfrageDto != null && anfrageDto.getIId() != null) { // fuer
				// leere
				// Anfrageliste
				AnfrageFilterFactory.getInstance()
						.createFKFlranfragepositionflranfrageiid(
								anfrageDto.getIId());
			}

			anfragePositionenlieferdatenTop = new PanelQuery(qtPositionen,
					filtersPositionen,
					QueryParameters.UC_ID_ANFRAGEPOSITIONLIEFERDATEN, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"anf.panel.positionlieferdaten"), true); // flag
			// ,
			// damit
			// flr
			// erst
			// beim
			// aufruf
			// des
			// panels
			// loslaeuft
			anfragePositionenlieferdatenTop.createAndSaveAndShowButton(
					"/com/lp/client/res/cube_green_preferences.png",
					LPMain.getInstance().getTextRespectUISPr(
							"anfr.artikellieferantzurueckpflegen.bevorzugt"),
					ACTION_SPECIAL_RUECKPFLEGE_ALS_ERSTER, KeyStroke
							.getKeyStroke('A',
									java.awt.event.InputEvent.CTRL_MASK),
					RechteFac.RECHT_ANF_ANFRAGE_CUD);

			anfragePositionenlieferdatenTop.createAndSaveAndShowButton(
					"/com/lp/client/res/cube_green_add.png",
					LPMain.getInstance().getTextRespectUISPr(
							"anfr.artikellieferantzurueckpflegen"),
					ACTION_SPECIAL_RUECKPFLEGE, KeyStroke.getKeyStroke('B',
							java.awt.event.InputEvent.CTRL_MASK),
					RechteFac.RECHT_ANF_ANFRAGE_CUD);

			anfragePositionenlieferdaten = new PanelSplit(getInternalFrame(),
					anfragePositionenlieferdatenBottom,
					anfragePositionenlieferdatenTop, 200);

			setComponentAt(IDX_PANEL_POSITIONENLIEFERDATEN,
					anfragePositionenlieferdaten);
		}

		return anfragePositionenlieferdaten;
	}

	private PanelBasis refreshAnfrageKonditionen() throws Throwable {
		Integer iIdAnfrage = null;

		if (anfrageKonditionen == null) {

			// Die Anfrage hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAnfrage = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			anfrageKonditionen = new PanelAnfrageKonditionen(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.panel.konditionen"),
					iIdAnfrage); // empty bei leerer auftragsliste
			setComponentAt(IDX_PANEL_KONDITIONEN, anfrageKonditionen);
		}

		return anfrageKonditionen;
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

		Integer iIdAnfrage = anfrageDto.getIId();

		// dtos hinterlegen
		initializeDtos(iIdAnfrage);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_AUSWAHL:
			getAnfrageAuswahl().eventYouAreSelected(false);
			anfrageAuswahl.updateButtons();

			// wenn es fuer das Default TabbedPane noch keinen eintrag gibt, die
			// restlichen Panels deaktivieren, die Grunddaten bleiben erreichbar
			if (getAnfrageAuswahl().getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			break;

		case IDX_PANEL_KOPFDATEN:
			refreshAnfrageKopfdaten().eventYouAreSelected(false); // buttons
			// schalten
			break;

		case IDX_PANEL_POSITIONEN:
			refreshAnfragePositionen();

			if (iIdAnfrage != null) {
				// filter aktualisieren
				FilterKriterium[] filtersPositionen = AnfrageFilterFactory
						.getInstance().createFKFlranfrageiid(
								anfrageDto.getIId());
				anfragePositionenTop.setDefaultFilter(filtersPositionen);
			}
			anfragePositionen.eventYouAreSelected(false);

			// statuspositionen: 1 das ueberschriebene
			// getLockedstateDetailMainKey() aufrufen
			anfragePositionenTop.updateButtons(anfragePositionenBottom
					.getLockedstateDetailMainKey());
			break;

		case IDX_PANEL_POSITIONENLIEFERDATEN:
			getAnfragePositionenlieferdaten();

			if (iIdAnfrage != null) {
				// filter aktualisieren
				FilterKriterium[] filtersPositionen = AnfrageFilterFactory
						.getInstance().createFKFlranfragepositionflranfrageiid(
								anfrageDto.getIId());
				anfragePositionenlieferdatenTop
						.setDefaultFilter(filtersPositionen);
			}

			anfragePositionenlieferdaten.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			anfragePositionenlieferdatenTop
					.updateButtons(anfragePositionenlieferdatenBottom
							.getLockedstateDetailMainKey());
			break;

		case IDX_PANEL_KONDITIONEN:
			refreshAnfrageKonditionen().eventYouAreSelected(false); // sonst
			// werden
			// die
			// buttons
			// nicht
			// richtig
			// gesetzt!
			break;
		}

		enableLieferdaten();
		setTitleAnfrage("");
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		// eine Zeile in einer Tabelle, bei der ich Listener bin, wurde
		// ausgewaehlt
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			handleItemChanged(eI);
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == anfragePositionenBottom) {
				// im QP die Buttons in den Zustand neu setzen.

				anfragePositionenTop.updateButtons(new LockStateValue(
						PanelQuery.LOCK_FOR_NEW));
			} else if (eI.getSource() == anfragePositionenlieferdatenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				anfragePositionenlieferdatenTop
						.updateButtons(new LockStateValue(
								PanelQuery.LOCK_FOR_NEW));
			}
		}

		else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// eine Zeile in einer Tabelle, bei der ich Listener bin, wurde
			// doppelgeklickt
			handleGotoDetailPanel(eI);
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();

			if (eI.getSource() == anfrageAuswahl) {
				if (sAspectInfo.equals(MY_OWN_NEW_LIEFERGRUPPENANFRAGE)) {
					if (anfrageAuswahl.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, true);
					}

					// eine Liefergruppenanfrage anlegen
					((PanelAnfrageKopfdaten) refreshAnfrageKopfdaten())
							.eventActionNewLiefergruppenanfrage(eI, true, false);

					setSelectedComponent(anfrageKopfdaten);
				} else if (sAspectInfo.endsWith(ACTION_SPECIAL_NEU_AUS_PROJEKT)) {
					dialogQueryProjektFromListe(null);
				} else if (sAspectInfo
						.equals(MY_OWN_NEW_ANFRAGEN_ZU_LIEFERGRUPPENANFRAGE)) {
					// der Benutzer muss auf einen Liefergruppenanfrage stehen
					if (pruefeAktuelleAnfrage()) {
						if (getAnfrageDto().getArtCNr().equals(
								AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {

							ArrayList<Integer> iIdLetzteErzeugteAnfrage = DelegateFactory
									.getInstance()
									.getAnfrageDelegate()
									.erzeugeAnfragenAusLiefergruppenanfrage(
											anfrageDto.getIId());

							if (iIdLetzteErzeugteAnfrage != null
									&& iIdLetzteErzeugteAnfrage.size() > 0) {

								Integer anfrageIId = iIdLetzteErzeugteAnfrage
										.get(iIdLetzteErzeugteAnfrage.size() - 1);

								initializeDtos(anfrageIId);
								getInternalFrame().setKeyWasForLockMe(
										anfrageIId.toString());
								refreshAnfrageKopfdaten()
										.setKeyWhenDetailPanel(anfrageIId);

								// wenn es bisher keine Anfrage gegeben hat
								if (anfrageAuswahl.getSelectedId() == null) {
									getInternalFrame()
											.enableAllOberePanelsExceptMe(this,
													IDX_PANEL_AUSWAHL, false);
								}

								anfrageAuswahl.setSelectedId(anfrageIId);
								setTitleAnfrage(LPMain.getInstance()
										.getTextRespectUISPr(
												"anf.panel.auswahl"));
								refreshAktuellesPanel();
								enableLieferdaten();
							}

						} else {
							DialogFactory.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hint"),
									LPMain.getInstance().getTextRespectUISPr(
											"anf.liefergruppenanfragewaehlen"));
						}
					}
				}
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				einfuegenHV();
			} else if (eI.getSource() == anfragePositionenlieferdatenTop) {
				if (sAspectInfo.equals(ACTION_SPECIAL_RUECKPFLEGE)
						|| sAspectInfo
								.equals(ACTION_SPECIAL_RUECKPFLEGE_ALS_ERSTER)) {

					AnfragepositionlieferdatenDto anfLiefPosDto = DelegateFactory
							.getInstance()
							.getAnfragepositionDelegate()
							.anfragepositionlieferdatenFindByPrimaryKey(
									(Integer) anfragePositionenlieferdatenTop
											.getSelectedId());

					AnfragepositionDto anfposDto = DelegateFactory
							.getInstance()
							.getAnfragepositionDelegate()
							.anfragepositionFindByPrimaryKey(
									anfLiefPosDto.getAnfragepositionIId());

					if (anfposDto.getArtikelIId() != null) {

						ArtikellieferantDto alDto = DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
										anfposDto.getArtikelIId(),
										getAnfrageDto()
												.getLieferantIIdAnfrageadresse(),
										new java.sql.Date(getAnfrageDto()
												.getTBelegdatum().getTime()));

						boolean bLoeschen = false;

						if (alDto != null) {

							ArtikellieferantstaffelDto[] stafDto = DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikellieferantstaffelFindByArtikellieferantIId(
											alDto.getIId());

							if (stafDto != null && stafDto.length > 0) {
								int i = DialogFactory
										.showModalJaNeinAbbrechenDialog(
												getInternalFrame(),
												"Es gibt bereits Staffel-Preise, wollen Sie diese l\u00F6schen?",
												LPMain.getInstance()
														.getTextRespectUISPr(
																"lp.warning"));
								if (i == JOptionPane.YES_OPTION) {
									bLoeschen = true;
								} else if (i == JOptionPane.CANCEL_OPTION) {
									return;
								}
							}
						}

						if (sAspectInfo
								.equals(ACTION_SPECIAL_RUECKPFLEGE_ALS_ERSTER)) {
							DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.preiseAusAnfrageRueckpflegen(
											getAnfrageDto().getIId(),
											anfLiefPosDto.getIId(), bLoeschen,
											true);
						} else {
							DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.preiseAusAnfrageRueckpflegen(
											getAnfrageDto().getIId(),
											anfLiefPosDto.getIId(), bLoeschen,
											false);
						}

					}

				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == anfrageAuswahl) {
				// wenn es bisher keine Anfrage gibt, die restlichen Panels
				// aktivieren
				if (anfrageAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, true);
				}

				refreshAnfrageKopfdaten().eventActionNew(eI, true, false);
				setSelectedComponent(anfrageKopfdaten);
			} else if (eI.getSource() == anfragePositionenTop) {
				if (istAktualisierenAnfrageErlaubt()) {
					anfragePositionenBottom.eventActionNew(eI, true, false);
					anfragePositionenBottom.eventYouAreSelected(false);
					setSelectedComponent(anfragePositionen); // ui
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
			// Discard
			if (eI.getSource() == anfrageKopfdaten) {
				// statuskopfdaten: 2
				anfrageKopfdaten.updateButtons(anfrageKopfdaten
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == anfragePositionenBottom) {
				anfragePositionen.eventYouAreSelected(false); // das 1:n Panel
				// neu aufbauen
			} else if (eI.getSource() == anfragePositionenlieferdatenBottom) {
				anfragePositionenlieferdaten.eventYouAreSelected(false); // das
				// 1
				// :
				// n
				// Panel
				// neu
				// aufbauen
			}

			enableLieferdaten();
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			// Wir landen hier nach Button Save
			handleActionSave(eI);
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			// nothing here
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// wir landen hier nach der Abfolge Button Neu -> xx -> Button
			// Discard
			if (eI.getSource() == anfrageKopfdaten) {
				// die Liste neu laden, falls sich etwas geaendert hat
				anfrageAuswahl.eventYouAreSelected(false);

				// nach einem Discard ist der aktuelle Key nicht mehr gesetzt
				setKeyWasForLockMe();

				// der Key der Kopfdaten steht noch auf new|...
				anfrageKopfdaten.setKeyWhenDetailPanel(anfrageAuswahl
						.getSelectedId());

				// auf die Auswahl schalten
				setSelectedComponent(anfrageAuswahl);

				// wenn es fuer das TabbedPane noch keinen eintrag gibt, die
				// restlichen panel deaktivieren
				if (anfrageAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

				// statuskopfdaten: 3
				anfrageKopfdaten.updateButtons(anfrageKopfdaten
						.getLockedstateDetailMainKey());
				enableLieferdaten();
			} else if (eI.getSource() == anfragePositionenBottom) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();
				if (anfragePositionenBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = anfragePositionenTop
							.getId2SelectAfterDelete();
					anfragePositionenTop.setSelectedId(oNaechster);
				}
				anfragePositionen.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n
				// panel
			} else if (eI.getSource() == anfragePositionenlieferdatenBottom) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();

				anfragePositionenlieferdaten.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte
				// 1
				// :
				// n
				// panel
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			// der OK Button in einem PanelDialog wurde gedrueckt
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == anfragePositionenTop) {
				if (getAnfrageDto().getStatusCNr().equals(
						AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)) {
					int iPos = anfragePositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die erste ist
					if (iPos > 0) {
						Integer iIdPosition = (Integer) anfragePositionenTop
								.getSelectedId();

						Integer iIdPositionMinus1 = (Integer) anfragePositionenTop
								.getTable().getValueAt(iPos - 1, 0);

						DelegateFactory
								.getInstance()
								.getAnfragepositionDelegate()
								.vertauscheAnfragepositionen(iIdPosition,
										iIdPositionMinus1);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						anfragePositionenTop.setSelectedId(iIdPosition);
					}
				} else {
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"anf.error.positionenverschieben"));
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == anfragePositionenTop) {
				int iPos = anfragePositionenTop.getTable().getSelectedRow();
				if (getAnfrageDto().getStatusCNr().equals(
						AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)) {
					// wenn die Position nicht die letzte ist
					if (iPos < anfragePositionenTop.getTable().getRowCount() - 1) {
						Integer iIdPosition = (Integer) anfragePositionenTop
								.getSelectedId();

						Integer iIdPositionPlus1 = (Integer) anfragePositionenTop
								.getTable().getValueAt(iPos + 1, 0);

						DelegateFactory
								.getInstance()
								.getAnfragepositionDelegate()
								.vertauscheAnfragepositionen(iIdPosition,
										iIdPositionPlus1);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						anfragePositionenTop.setSelectedId(iIdPosition);
					}
				} else {
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"anf.error.positionenverschieben"));
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == anfragePositionenTop) {
				anfragePositionenBottom.eventActionNew(eI, true, false);
				anfragePositionenBottom.eventYouAreSelected(false); // Buttons
				// schalten
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}
	}

	public void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame());

		new DialogQuery(panelQueryFLRProjekt);
	}

	public void copyHV() throws ExceptionLP, Throwable {

		Object aoIIdPosition[] = anfragePositionenTop.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			AnfragepositionDto[] dtos = new AnfragepositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory
						.getInstance()
						.getAnfragepositionDelegate()
						.anfragepositionFindByPrimaryKey(
								(Integer) aoIIdPosition[i]);
			}
			LPMain.getInstance().getPasteBuffer()
					.writeObjectToPasteBuffer(dtos);
		}

	}

	public void einfuegenHV() throws IOException, ParserConfigurationException,
			SAXException, Throwable {

		Object o = LPMain.getInstance().getPasteBuffer()
				.readObjectFromPasteBuffer();

		if (o instanceof BelegpositionDto[]) {
			AnfragepositionDto[] anfragepositionDtos = DelegateFactory
					.getInstance()
					.getBelegpostionkonvertierungDelegate()
					.konvertiereNachAnfragepositionDto(getAnfrageDto(),
							(BelegpositionDto[]) o);

			if (anfragepositionDtos != null) {
				Integer iId = null;
				Boolean b = positionAmEndeEinfuegen();
				if (b != null) {
					for (int i = 0; i < anfragepositionDtos.length; i++) {
						AnfragepositionDto positionDto = anfragepositionDtos[i];
						try {
							positionDto.setIId(null);
							// damits hinten angehaengt wird.
							if (b == false) {
								Integer iIdAktuellePosition = (Integer) getAnfragePositionenTop()
										.getSelectedId();

								// die erste Position steht an der Stelle 1
								Integer iSortAktuellePosition = new Integer(1);

								if (iIdAktuellePosition != null) {
									iSortAktuellePosition = DelegateFactory
											.getInstance()
											.getAnfragepositionDelegate()
											.anfragepositionFindByPrimaryKey(
													iIdAktuellePosition)
											.getISort();

									// Die bestehenden Positionen muessen Platz
									// fuer die neue schaffen
									DelegateFactory
											.getInstance()
											.getAnfragepositionDelegate()
											.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
													getAnfrageDto().getIId(),
													iSortAktuellePosition
															.intValue());
								} else {
									iSortAktuellePosition = null;
								}

								// Die neue Position wird an frei gemachte
								// Position gesetzt
								positionDto.setISort(iSortAktuellePosition);
							} else {
								positionDto.setISort(null);
							}
							positionDto.setBelegIId(getAnfrageDto().getIId());
							// wir legen eine neue position an
							if (iId == null) {
								iId = DelegateFactory.getInstance()
										.getAnfragepositionDelegate()
										.createAnfrageposition(positionDto);
							} else {
								DelegateFactory.getInstance()
										.getAnfragepositionDelegate()
										.createAnfrageposition(positionDto);
							}
						} catch (Throwable t) {
							// nur loggen!
							myLogger.error(t.getMessage(), t);
						}
					}
				}
				// die Liste neu aufbauen
				anfragePositionenTop.eventYouAreSelected(false);
				// den Datensatz in der Liste selektieren
				anfragePositionenTop.setSelectedId(iId);
				// im Detail den selektierten anzeigen
				anfragePositionen.eventYouAreSelected(false);
			}

		}

	}

	/**
	 * fuelle fehlende muss-felder der anfrage.
	 * 
	 * @param belegposDtoI
	 *            AnfrageDto
	 * @param xalOfBSPOS
	 *            int
	 */
	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBSPOS) {

		AnfragepositionDto anfragepositionDtoI = (AnfragepositionDto) belegposDtoI;
		anfragepositionDtoI.setBelegIId(anfrageDto.getIId());
		anfragepositionDtoI.setISort(xalOfBSPOS + 1000);
		if (anfragepositionDtoI.getBArtikelbezeichnunguebersteuert() == null) {
			anfragepositionDtoI.setBArtikelbezeichnunguebersteuert(Helper
					.boolean2Short(false));
		}
	}

	private void handleActionSave(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == anfrageKopfdaten) {
			Object pkAnfrage = anfrageKopfdaten.getKeyWhenDetailPanel();
			initializeDtos((Integer) pkAnfrage);
			getInternalFrame().setKeyWasForLockMe(pkAnfrage.toString());

			anfrageAuswahl.clearDirektFilter();
			anfrageAuswahl.eventYouAreSelected(false);
			anfrageAuswahl.setSelectedId(pkAnfrage);
			anfrageAuswahl.eventYouAreSelected(false);
		} else if (e.getSource() == anfragePositionenBottom) {
			// den Key des Datensatzes merken
			Object oKey = anfragePositionenBottom.getKeyWhenDetailPanel();

			// wenn der neue Datensatz wirklich angelegt wurde (Abbruch moeglich
			// durch Pruefung auf Unterpreisigkeit)
			if (!oKey.equals(LPMain.getLockMeForNew())) {
				// die Liste neu aufbauen
				anfragePositionenTop.eventYouAreSelected(false);

				// den Datensatz in der Liste selektieren
				anfragePositionenTop.setSelectedId(oKey);
			}

			// im Detail den selektierten anzeigen
			anfragePositionen.eventYouAreSelected(false);
		} else if (e.getSource() == anfragePositionenlieferdatenBottom) {
			// den Key des Datensatzes merken
			Object oKey = anfragePositionenlieferdatenBottom
					.getKeyWhenDetailPanel();

			// wenn der neue Datensatz wirklich angelegt wurde (Abbruch moeglich
			// durch Pruefung auf Unterpreisigkeit)
			if (!oKey.equals(LPMain.getLockMeForNew())) {
				// die Liste neu aufbauen
				anfragePositionenlieferdatenTop.eventYouAreSelected(false);

				// den Datensatz in der Liste selektieren
				anfragePositionenlieferdatenTop.setSelectedId(oKey);
			}

			// im Detail den selektierten anzeigen
			anfragePositionenlieferdaten.eventYouAreSelected(false);
		}
	}

	private void handleGotoDetailPanel(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == anfrageAuswahl) {
			Integer iIdAnfrageI = (Integer) anfrageAuswahl.getSelectedId();
			initializeDtos(iIdAnfrageI);
			refreshAnfrageKopfdaten().setKeyWhenDetailPanel(iIdAnfrageI); // braucht
			// man
			// fuer
			// lock

			// bei Doppelklick direkt in die Positionen wechseln
			if (iIdAnfrageI != null) {
				setSelectedComponent(anfragePositionen);
			}
		} else if (e.getSource() == panelQueryFLRLiefergruppeAuswahl) {
			Integer liefergruppeIId = (Integer) panelQueryFLRLiefergruppeAuswahl
					.getSelectedId();
			liefergruppenanfragenErstellenUndDrucken(liefergruppeIId);
		} else if (e.getSource() == panelQueryFLRProjekt) {
			Integer projektIId = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();

			if (projektIId != null) {
				erstelleAanfrageAusProjekt(projektIId);
			}
		}
	}

	private void erstelleAanfrageAusProjekt(Integer projektIId)
			throws Throwable {
		PanelAnfrageKopfdaten panelKopfdaten = refreshAnfrageKopfdaten();
		panelKopfdaten.eventActionNew(null, true, false);
		setSelectedComponent(panelKopfdaten);
		panelKopfdaten.setDefaults();

		// Nun noch Projekt/ProjektBezeichnung
		// setzen
		panelKopfdaten.setDefaultsAusProjekt(projektIId);
	}

	private void handleItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == anfrageAuswahl) {
			Integer iIdAnfrage = (Integer) anfrageAuswahl.getSelectedId();
			initializeDtos(iIdAnfrage);
			setKeyWasForLockMe();

			setTitleAnfrage(LPMain.getInstance().getTextRespectUISPr(
					"anf.panel.auswahl"));

			refreshAnfragePositionen();
			getAnfragePositionenlieferdaten();

			// pk den flr 1:n panels als default filter setzen
			anfragePositionenTop.setDefaultFilter(AnfrageFilterFactory
					.getInstance().createFKFlranfrageiid(iIdAnfrage));
			anfragePositionenlieferdatenTop
					.setDefaultFilter(AnfrageFilterFactory
							.getInstance()
							.createFKFlranfragepositionflranfrageiid(iIdAnfrage));

			// pk den flr detail ansicht den key fuer die erste ansicht
			// vorbelegen
			anfragePositionen.setKeyWhenDetailPanel(iIdAnfrage);
			anfragePositionenlieferdaten.setKeyWhenDetailPanel(iIdAnfrage);

			enableLieferdaten();

			getInternalFrame().enableAllOberePanelsExceptMe(this,
					IDX_PANEL_AUSWAHL, (iIdAnfrage != null));
		} else

		if (e.getSource() == anfragePositionenTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			anfragePositionenBottom.setKeyWhenDetailPanel(key);
			anfragePositionenBottom.eventYouAreSelected(false);
			// statuspositionen: 2 das ueberschriebene
			// getLockedstateDetailMainKey() aufrufen
			anfragePositionenTop.updateButtons(anfragePositionenBottom
					.getLockedstateDetailMainKey());
		} else

		if (e.getSource() == anfragePositionenlieferdatenTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			anfragePositionenlieferdatenBottom.setKeyWhenDetailPanel(key);
			anfragePositionenlieferdatenBottom.eventYouAreSelected(false);

			anfragePositionenlieferdatenTop
					.updateButtons(anfragePositionenlieferdatenBottom
							.getLockedstateDetailMainKey());
		}
	}

	private void initializeDtos(Integer iIdAnfrage) throws Throwable {
		if (iIdAnfrage != null) {
			anfrageDto = DelegateFactory.getInstance().getAnfrageDelegate()
					.anfrageFindByPrimaryKey(iIdAnfrage);
			enableLieferdaten();

			if (anfrageDto.getLieferantIIdAnfrageadresse() != null) {
				lieferantDto = DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								anfrageDto.getLieferantIIdAnfrageadresse());
				liefergruppeDto = new LfliefergruppeDto();
			} else if (anfrageDto.getLiefergruppeIId() != null) {
				liefergruppeDto = DelegateFactory
						.getInstance()
						.getLieferantServicesDelegate()
						.lfliefergruppeFindByPrimaryKey(
								anfrageDto.getLiefergruppeIId());
				lieferantDto = new LieferantDto();
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
	public void setTitleAnfrage(String panelTitle) throws Throwable {
		// aktuelle anfragenummer bestimmen
		StringBuffer anfragenummer = new StringBuffer("");
		if (anfrageDto == null || anfrageDto.getIId() == null) {
			anfragenummer.append(LPMain.getInstance().getTextRespectUISPr(
					"anf.title.neueanfrage"));
		} else {

			anfragenummer
					.append(LPMain.getInstance().getTextRespectUISPr(
							"anf.anfrage")).append(" ")
					.append(anfrageDto.getCNr());

			if (anfrageDto.getLieferantIIdAnfrageadresse() != null) {
				LieferantDto lfDto = DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								anfrageDto.getLieferantIIdAnfrageadresse());
				anfragenummer.append(" ").append(
						lfDto.getPartnerDto().formatFixTitelName1Name2());
			}
		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				anfragenummer.toString());
	}

	public void resetDtos() {
		anfrageDto = new AnfrageDto();
		lieferantDto = new LieferantDto();
		liefergruppeDto = new LfliefergruppeDto();
	}

	public boolean pruefeAktuelleAnfrage() {
		boolean bIstGueltig = true;

		if (anfrageDto == null || anfrageDto.getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("anf.warning.keineanfrage"));
		}

		return bIstGueltig;
	}

	public boolean aktuelleAnfrageHatMengenbehaftetePositionen()
			throws Throwable {
		boolean bHatMengenbehaftetePositionen = true;

		if (DelegateFactory.getInstance().getAnfragepositionDelegate()
				.getAnzahlMengenbehafteteAnfragepositionen(anfrageDto.getIId()) <= 0) {
			bHatMengenbehaftetePositionen = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("anf.warning.keinepositionen"));
		}

		return bHatMengenbehaftetePositionen;
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
	 * Diese Methode setzt die aktuelle Anfrage aus der Auswahlliste als die zu
	 * lockende Anfrage.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = anfrageAuswahl.getSelectedId();

		if (oKey != null) {
			initializeDtos((Integer) oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void printAlleAngelegtenAnfragen() throws Throwable {
		// PJ17370

		String[] optionen = new String[] {
				LPMain.getInstance().getTextRespectUISPr(
						"anf.angelegtedrucken.alle"),
				LPMain.getInstance().getTextRespectUISPr(
						"anf.angelegtedrucken.auswaehlen") };

		int iOption = DialogFactory.showModalDialog(
				getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr(
						"anf.angelegtedrucken.welche"), LPMain.getInstance()
						.getTextRespectUISPr("part.liefergruppe"), optionen,
				optionen[0]);

		if (iOption > -1) {

			if (iOption == 0) {

				liefergruppenanfragenErstellenUndDrucken(null);
			} else if (iOption == 1) {
				String[] aWhichButtonIUse = null;

				FilterKriterium[] f = SystemFilterFactory.getInstance()
						.createFKMandantCNr();
				QueryType[] querytypes = null;
				panelQueryFLRLiefergruppeAuswahl = new PanelQueryFLR(
						querytypes, f, QueryParameters.UC_ID_LIEFERGRUPPEN,
						aWhichButtonIUse, getInternalFrame(), LPMain
								.getInstance().getTextRespectUISPr(
										"part.liefergruppe"));
				new DialogQuery(panelQueryFLRLiefergruppeAuswahl);
			}
		}
	}

	public void liefergruppenanfragenErstellenUndDrucken(
			Integer lfliefergruppeIId) throws ExceptionLP, Throwable,
			InterruptedException {

		try {

			ArrayList<Integer> a = null;
			try {
				a = DelegateFactory
						.getInstance()
						.getAnfrageDelegate()
						.getAngelegteAnfragenNachUmwandlungDerLiefergruppenanfragen(
								lfliefergruppeIId);
			} catch (ExceptionLP ex) {
				if (ex.getICode() == EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN) {

					if (ex.getAlInfoForTheClient() != null
							&& ex.getAlInfoForTheClient().size() > 0) {
						Integer i = (Integer) ex.getAlInfoForTheClient().get(0);
						AnfrageDto afDto = DelegateFactory.getInstance()
								.getAnfrageDelegate()
								.anfrageFindByPrimaryKey(i);
						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.warning"),
								LPMain.getInstance().getTextRespectUISPr(
										"anf.warning.keinepositionen")
										+ " (" + afDto.getCNr() + ") ");
					}

					return;
				} else {
					handleException(ex, false);
					return;
				}
			}

			for (Iterator<Integer> iter = a.iterator(); iter.hasNext();) {
				Integer item = (Integer) iter.next();
				AnfrageDto afDto = DelegateFactory.getInstance()
						.getAnfrageDelegate().anfrageFindByPrimaryKey(item);

				// Lieferant holen
				LieferantDto lfDto = DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								afDto.getLieferantIIdAnfrageadresse());

				// Die Anfrage muss Positionen haben
				if (DelegateFactory
						.getInstance()
						.getAnfragepositionDelegate()
						.getAnzahlMengenbehafteteAnfragepositionen(
								afDto.getIId()) > 0) {
					// Aktivieren
					Timestamp t = DelegateFactory
							.getInstance()
							.getAnfrageDelegate()
							.berechneBelegControlled(afDto.getIId());
					DelegateFactory.getInstance().getAnfrageDelegate().aktiviereBelegControlled(afDto.getIId(), t);
					// DruckPanel instantiieren
					PanelReportKriterien krit = new PanelReportKriterien(
							getInternalFrame(), new ReportAnfrage(
									getInternalFrame(), getAktuellesPanel(), afDto, ""), "",
							lfDto.getPartnerDto(),
							afDto.getAnsprechpartnerIIdLieferant(), true, true,
							false);

					getInternalFrame().showPanelDialog(krit);

					// jetzt das tatsaechliche Drucken
					/*
					 * try { krit.print(); } catch (JRException ex) { int
					 * iChoice = JOptionPane.YES_OPTION;
					 * myLogger.error(ex.getLocalizedMessage(), ex); String sMsg
					 * = LPMain .getTextRespectUISPr(
					 * "lp.drucken.reportkonntenichtgedrucktwerden") + " " +
					 * afDto.getCNr(); Object[] options = {
					 * LPMain.getTextRespectUISPr("lp.druckerror.wiederholen"),
					 * LPMain.getTextRespectUISPr("lp.druckerror.&uuml;berspringen"),
					 * LPMain.getTextRespectUISPr("lp.abbrechen"), }; iChoice =
					 * DialogFactory.showModalDialog( this.getInternalFrame(),
					 * sMsg, LPMain.getTextRespectUISPr("lp.error"), options,
					 * options[0]); if (iChoice == 0) { Thread.sleep(5000);
					 * krit.print(); } else if (iChoice == 1) { DelegateFactory
					 * .getInstance() .getAnfrageDelegate() .setzeAnfragestatus(
					 * AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT,
					 * afDto.getIId()); } else if (iChoice == 2) {
					 * DelegateFactory .getInstance() .getAnfrageDelegate()
					 * .setzeAnfragestatus(
					 * AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT,
					 * afDto.getIId()); break; } } catch (Throwable ex) { //
					 * Falls es beim Drucken zu einem Fehler kommt // ->
					 * Aktivierung zuruecknehmen DelegateFactory .getInstance()
					 * .getAnfrageDelegate() .setzeAnfragestatus(
					 * AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT,
					 * afDto.getIId()); }
					 */
				} else {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("anf.keinepositionen")
									+ "\n" + afDto.getCNr());
				}

			}
		}
		// Danach immer ein refresh
		finally {
			getPanelAuswahl().eventYouAreSelected(false);
		}
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_DATEI_ANFRAGE)) {
			printAnfrage();
		} else if (e.getActionCommand().equals(
				MY_OWN_NEW_ALLE_ANGELEGTEN_LIEFERGRUPPENANFRAGEN_VERSENDEN)) {
			printAlleAngelegtenAnfragen();
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (pruefeAktuelleAnfrage()) {
				if (getAnfrageDto().getArtCNr().equals(
						AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hint"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"anf.liefergruppenanfragenichtmanuellerledigen"));
				} else {
					if (!refreshAnfrageKopfdaten().isLockedDlg()) {
						// Statuswechsel 'Erfasst' -> 'Erledigt' : Ausgeloest
						// durch Menue
						if (anfrageDto.getStatusCNr().equals(
								AnfrageServiceFac.ANFRAGESTATUS_ERFASST)
								|| anfrageDto.getStatusCNr().equals(
										AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
							if (DialogFactory.showMeldung(
									LPMain.getInstance().getTextRespectUISPr(
											"anf.auferledigtsetzen"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hint"),
									javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

								DelegateFactory.getInstance()
										.getAnfrageDelegate()
										.manuellErledigen(anfrageDto.getIId());
								initializeDtos(anfrageDto.getIId());
								refreshAktuellesPanel();
							}
						} else {
							if (anfrageDto.getStatusCNr().equals(
									AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
								if (DialogFactory
										.showMeldung(
												LPMain.getInstance()
														.getTextRespectUISPr(
																"anf.erledigungaufheben"),
												LPMain.getInstance()
														.getTextRespectUISPr(
																"lp.hint"),
												javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
									DelegateFactory
											.getInstance()
											.getAnfrageDelegate()
											.erledigungAufheben(
													anfrageDto.getIId());
									initializeDtos(anfrageDto.getIId());
									refreshAktuellesPanel();
								}
							} else {
								showStatusMessage("lp.warning",
										"anf.warning.anfragekannnichterledigtwerden");
							}
						}
					}
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_JOURNAL_ANFRAGE_LIEFERDATENUEBERSICHT)) {
			getInternalFrame().showReportKriterien(
					new ReportAnfrageLieferdatenuebersicht(getInternalFrame(),
							LPMain.getInstance().getTextRespectUISPr(
									"anf.print.lieferdatenuebersicht"),
							getAnfrageDto()),
					getLieferantDto().getPartnerDto(),
					getAnfrageDto().getAnsprechpartnerIIdLieferant(), false);
		}
	}

	/**
	 * Diese Methode prueft den Status der aktuellen Anfrage und legt fest, ob
	 * eine Aenderung in den Kopfdaten bzw. Konditionen erlaubt ist.
	 * 
	 * @return boolean true, wenn ein update erlaubt ist
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public boolean istAktualisierenAnfrageErlaubt() throws Throwable {
		boolean bIstAenderungErlaubtO = false;

		if (pruefeAktuelleAnfrage()) {
			if (anfrageDto.getStatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)
					|| anfrageDto.getStatusCNr().equals(
							AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
				bIstAenderungErlaubtO = true;
			} else {
				MessageFormat mf = new MessageFormat(LPMain.getInstance()
						.getTextRespectUISPr(
								"anf.warning.anfragekannnichtgeaendertwerden"));

				mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

				Object pattern[] = { getAnfrageStatus() };

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.warning"), mf.format(pattern));
			}
		}

		return bIstAenderungErlaubtO;
	}

	public boolean istAktualisierenLieferdatenErlaubt() throws Throwable {
		boolean bIstAenderungErlaubtO = false;

		if (pruefeAktuelleAnfrage()) {
			if (anfrageDto.getStatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_ERFASST)
					|| anfrageDto.getStatusCNr().equals(
							AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
				bIstAenderungErlaubtO = true;
			} else {
				MessageFormat mf = new MessageFormat(
						LPMain.getInstance()
								.getTextRespectUISPr(
										"anf.warning.lieferdatenkoennennichtgeaendertwerden"));

				mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

				Object pattern[] = { getAnfrageStatus() };

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.warning"), mf.format(pattern));
			}
		}

		return bIstAenderungErlaubtO;
	}

	public void printAnfrage() throws Throwable {
		if (pruefeAktuelleAnfrage()) {
			if (aktuelleAnfrageHatMengenbehaftetePositionen()) {
				if (!refreshAnfrageKopfdaten().isLockedDlg()) {

					getInternalFrame().showReportKriterien(
							new ReportAnfrage(getInternalFrame(),
									getAktuellesPanel(), getAnfrageDto(),
									getTitleDruck()),
							getLieferantDto().getPartnerDto(),
							getAnfrageDto().getAnsprechpartnerIIdLieferant(),
							false);
				}
			}
		}
	}

	/**
	 * Der Status der Anfrage kann in einigen Faellen ueber den Update Button
	 * geaendert werden.
	 * 
	 * @return boolean true, wenn die aktuelle Anfrage geaendert werden darf
	 * @throws Throwable
	 *             Ausnahme
	 */
	public boolean aktualisiereAnfragestatusDurchButtonUpdate()
			throws Throwable {
		boolean bIstAktualisierenErlaubt = false;

		if (pruefeAktuelleAnfrage()) {

			// im Status 'Offen' duerfen die Kopfdaten nach Rueckfrage geaendert
			// werden
			if (getAnfrageDto().getStatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
				bIstAktualisierenErlaubt = true;
			} else

			// im Status 'Angelegt' duerfen die Kopfdaten geaendert werden
			if (getAnfrageDto().getStatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)) {
				bIstAktualisierenErlaubt = true;
			} else

			// Statuswechsel 'Storniert' -> 'Angelegt' : Ausgeloest durch Button
			// Update
			if (getAnfrageDto().getStatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)) {
				if (DialogFactory.showMeldung(LPMain.getInstance()
						.getTextRespectUISPr("anf.aufoffensetzen"), LPMain
						.getInstance().getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

					DelegateFactory.getInstance().getAnfrageDelegate()
							.stornoAufheben(anfrageDto.getIId());

					// den geaenderten Status anzeigen
					anfrageDto = DelegateFactory.getInstance()
							.getAnfrageDelegate()
							.anfrageFindByPrimaryKey(anfrageDto.getIId());

					anfrageKopfdaten.eventYouAreSelected(false);
				}
			} else

			// Statuswechsel 'Erledigt' -> 'Erfasst': Ausgeloest durch Button
			// Update
			if (getAnfrageDto().getStatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
				if (DialogFactory.showMeldung(LPMain.getInstance()
						.getTextRespectUISPr("anf.hint.anfrageerledigt"),
						LPMain.getInstance().getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
					DelegateFactory.getInstance().getAnfrageDelegate()
							.erledigungAufheben(anfrageDto.getIId());
					initializeDtos(anfrageDto.getIId());
					refreshAktuellesPanel();
				}
			} else {
				MessageFormat mf = new MessageFormat(LPMain.getInstance()
						.getTextRespectUISPr(
								"anf.warning.anfragekannnichtgeaendertwerden"));

				mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

				Object pattern[] = { getAnfrageStatus() };

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.warning"), mf.format(pattern));
			}
		}

		return bIstAktualisierenErlaubt;
	}
	
	@Override
	protected void initDtos() throws Throwable {
		initializeDtos(anfrageDto.getIId());
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);

		// Menue Datei; ein neuer Eintrag wird immer oben im Menue eingetragen
		JMenu jmModul = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);
		JMenuItem menuItemDateiAlleAngelegtenVersenden = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr(
						"anf.anfrage.alleangelegtendrucken"));
		menuItemDateiAlleAngelegtenVersenden.addActionListener(this);

		menuItemDateiAlleAngelegtenVersenden
				.setActionCommand(MY_OWN_NEW_ALLE_ANGELEGTEN_LIEFERGRUPPENANFRAGEN_VERSENDEN);
		jmModul.add(menuItemDateiAlleAngelegtenVersenden, 0);
		JMenuItem menuItemDateiAnfrage = new JMenuItem(LPMain.getInstance()
				.getTextRespectUISPr("lp.menu.drucken"));
		menuItemDateiAnfrage.addActionListener(this);

		menuItemDateiAnfrage.setActionCommand(MENU_ACTION_DATEI_ANFRAGE);
		jmModul.add(menuItemDateiAnfrage, 0);

		// Menue Bearbeiten
		JMenu jmBearbeiten = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		JMenuItem menuItemBearbeitenManuellErledigen = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr("lp.menu.menuellerledigen"));
		menuItemBearbeitenManuellErledigen.addActionListener(this);
		menuItemBearbeitenManuellErledigen
				.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN);
		jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);

		// Menue Journal
		JMenu jmJournal = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_JOURNAL);

		if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			JMenuItem menuItemJournalAnfrageLieferdatenuebersicht = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr(
							"anf.menu.lieferdatenuebersicht"));
			menuItemJournalAnfrageLieferdatenuebersicht.addActionListener(this);
			menuItemJournalAnfrageLieferdatenuebersicht
					.setActionCommand(MENU_JOURNAL_ANFRAGE_LIEFERDATENUEBERSICHT);
			jmJournal.add(menuItemJournalAnfrageLieferdatenuebersicht, 0);
		}

		return wrapperMenuBar;
	}

	public void gotoAuswahl() {
		setSelectedComponent(anfrageAuswahl);
	}

	public String getAnfrageStatus() throws Throwable {
		StatusDto statusDto = DelegateFactory.getInstance().getLocaleDelegate()
				.statusFindByPrimaryKey(getAnfrageDto().getStatusCNr());

		String cNrStatus = statusDto.getCNr();

		if (statusDto.getStatussprDto() != null) {
			cNrStatus = statusDto.getStatussprDto().getCBez();
		}

		return cNrStatus;
	}

	private String getTitleDruck() {
		StringBuffer buff = new StringBuffer();

		buff.append(getAnfrageDto().getCNr());
		buff.append(" ");
		buff.append(getLieferantDto().getIId() == null ? "" : getLieferantDto()
				.getPartnerDto().getCName1nachnamefirmazeile1());

		return buff.toString();
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
	public void enableLieferdaten(LockStateValue lsvI) throws Throwable {
		if (lsvI.getIState() != PanelBasis.LOCK_FOR_NEW) {
			enableLieferdaten();
		}
	}

	public void enableLieferdaten() throws Throwable {
		if (getAnfrageDto().getIId() != null) {
			if (getAnfrageDto().getArtCNr().equals(
					AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
				setEnabledAt(IDX_PANEL_KONDITIONEN, false);
				setEnabledAt(IDX_PANEL_POSITIONENLIEFERDATEN, false);
			} else {
				setEnabledAt(IDX_PANEL_KONDITIONEN, true);
				setEnabledAt(IDX_PANEL_POSITIONENLIEFERDATEN, true);
			}
		}
	}

	public void showStatusMessage(String lpTitle, String lpMessage)
			throws Throwable {
		MessageFormat mf = new MessageFormat(LPMain.getInstance()
				.getTextRespectUISPr(lpMessage));
		mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

		Object pattern[] = { getAnfrageStatus() };

		DialogFactory.showModalDialog(
				LPMain.getInstance().getTextRespectUISPr(lpTitle),
				mf.format(pattern));
	}

	// TODO-AGILCHANGES
	/**
	 * AGILPRO CHANGES BEGIN new methods for getting panels to agilpro
	 * 
	 * @author Lukas Lisowski
	 * @return PanelQuery
	 */
	public PanelQuery getPanelAuswahl() {
		setSelectedIndex(IDX_PANEL_AUSWAHL);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return anfrageAuswahl;
	}

	public PanelBasis getPanelKopfdaten() {
		setSelectedIndex(IDX_PANEL_KOPFDATEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return anfrageKopfdaten;
	}

	public PanelBasis getPanelKonditionen() {
		setSelectedIndex(IDX_PANEL_KONDITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return anfrageKonditionen;
	}

	public PanelSplit getPanelPositionen() {
		setSelectedIndex(IDX_PANEL_POSITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return anfragePositionen;
	}

	public PanelSplit getPanelLieferdaten() {
		setSelectedIndex(IDX_PANEL_POSITIONENLIEFERDATEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return anfragePositionenlieferdaten;
	}

	/**
	 * AGILPRO CHANGES END
	 * 
	 * @return Object
	 */
	public Object getInseratDto() {
		return anfrageDto;
	}

}
