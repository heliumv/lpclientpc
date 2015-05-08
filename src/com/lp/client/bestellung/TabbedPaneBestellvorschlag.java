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
package com.lp.client.bestellung;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.artikel.ReportArtikelstatistik;
import com.lp.client.artikel.TabbedPaneArtikel;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.fertigung.PanelTabelleBewegungsvorschau;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlag2;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagUeberleitung2;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagverdichtung;
import com.lp.client.frame.component.PanelPositionenBestellvorschlag;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich ...</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>10.03.05</I>
 * </p>
 * 
 * @todo MB. das ist eigentlich das gleiche wie TPAnfragevorschlag -> eine
 *       Klasse daraus machen
 *       <p>
 *       </p>
 * 
 * @author Josef Erlinger
 * @version $Revision: 1.19 $
 */
public class TabbedPaneBestellvorschlag extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelBestellungVorschlagTopQP1 = null;
	private PanelSplit panelBestellungVorschlagSP1 = null;
	private PanelPositionenBestellvorschlag panelBestellungVorschlagBottomD1 = null;

	private PanelTabelleBewegungsvorschau panelTabelleBestellungBewegungsvorschau2 = null;

	private PanelQuery panelQueryLieferantenoptimieren = null;

	// Kriterienauswahl fuer die Erstellung des Bestellvorschlags
	private PanelDialogKriterienBestellvorschlag2 pdKritBestellvorschlag = null;
	private PanelDialogKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand pdKritBestellvorschlagAnhandStuecklistenmindestlagerstand = null;

	// Kriterienauswahl fuer die Bestellvorschlagverdichtung
	private PanelDialogKriterienBestellvorschlagverdichtung pdKritBestellvorschlagverdichtung = null;

	// Kriterienauswahl fuer die Ueberleitung des Bestellvorschlags in
	// Bestellungen
	private PanelDialogKriterienBestellvorschlagUeberleitung2 pdKritUeberleitung = null;

	// Kriterienauswahl fuer das Loeschen von spaeter bestellbaren Positionen.
	private PanelDialogKriterienSpaeterBestellbareLoeschen pdKritSpaeterBestellbare = null;

	// Kriterien fuer loeschen bis
	private PanelDialogKriterienBestellvorschlagLoeschenBisZu pdLoeschenBis = null;

	private String ACTION_SPECIAL_MONATSSTATISTIK = PanelBasis.ALWAYSENABLED
			+ "action_special_monatsstatikstik";

	private String ACTION_SPECIAL_GOTO_ARTIKEL = PanelBasis.ALWAYSENABLED
			+ "action_special_goto_artikel";

	private static final int IDX_PANEL_BESTELLUNGVORSCHLAG = 0;
	private static final int IDX_PANEL_BEWEGUNGSVORSCHAU = 1;
	private static final int IDX_PANEL_LIEFERANTEN_OPTIMIEREN = 2;

	private static final String ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_neuer_bestellvorschlag";

	private static final String ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_STUECKLISTENMINDESTLAGERSTAND = PanelBasis.ALWAYSENABLED
			+ "action_special_neuer_bestellvorschlag_anhand_stuecklistenmindestlagerstand";

	private static final String ACTION_SPECIAL_LIEFERANT_UEBERNEHMEN = PanelBasis.ALWAYSENABLED
			+ "action_special_lieferant_uebernehmen";

	private static final String ACTION_SPECIAL_BESTELLUNGEN_AUS_BESTELLVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_bestellungen_aus_bestellvorschlag";
	private static final String ACTION_SPECIAL_SPAETER_BESTELLBARE_LOESCHEN = PanelBasis.ALWAYSENABLED
			+ "action_special_spaeter_bestellbare_loeschen";
	private static final String ACTION_SPECIAL_VERDICHTEN = PanelBasis.ALWAYSENABLED
			+ "action_special_verdichten";
	private static final String MENU_ACTION_JOURNAL_BESTELLVORSCHLAG = "MENU_ACTION_JOURNAL_BESTELLVORSCHLAG";
	private static final String MENU_ACTION_JOURNAL_GEAENDERTE_ARTIKEL = "MENU_ACTION_JOURNAL_GEAENDERTE_ARTIKEL";
	private static final String MENU_ACTION_VORSCHLAG_LOESCHEN_BIS_DATUM = "MENU_ACTION_VORSCHLAG_LOESCHEN_BIS_DATUM";

	private static final String ACTION_SPECIAL_LIEFERANT_ZURUECKSETZEN_BESTELLVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_lieferant_zuruecksetzen_bestellvorschlag";

	private static final String ACTION_SPECIAL_LIEFERANT_ZURUECKSETZEN_OPTIMIEREN = PanelBasis.ALWAYSENABLED
			+ "action_special_lieferant_zuruecksetzen_optimieren";

	private JLabel lieferantInfos = null;

	private BestellvorschlagDto bestellvorschlagDto = null;

	public BestellvorschlagDto getBestellvorschlagDto() {
		return bestellvorschlagDto;
	}

	public void setBestellvorschlagDto(BestellvorschlagDto bestellvorschlagDto)
			throws Throwable {
		this.bestellvorschlagDto = bestellvorschlagDto;
		if (bestellvorschlagDto != null) {
			refreshFilterBewegungsvorschau();
		}

	}

	public TabbedPaneBestellvorschlag(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("bes.title.panel.bestellvorschlag"));
		jbInit();
		initComponents();
	}

	public InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	public PanelSplit getPanelBestellungVorschlagSP1() {
		return panelBestellungVorschlagSP1;
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("lp.bestellvorschlag"), null,
				null,
				LPMain.getTextRespectUISPr("lp.tooltip.bestellvorschlag"),
				IDX_PANEL_BESTELLUNGVORSCHLAG);

		insertTab(LPMain.getTextRespectUISPr("bes.bewegungsvorschau"), null,
				null, LPMain.getTextRespectUISPr("bes.bewegungsvorschau"),
				IDX_PANEL_BEWEGUNGSVORSCHAU);

		insertTab(
				LPMain.getTextRespectUISPr("bes.bestellvorschlag.lieferantenoptimieren"),
				null,
				null,
				LPMain.getTextRespectUISPr("bes.bestellvorschlag.lieferantenoptimieren"),
				IDX_PANEL_LIEFERANTEN_OPTIMIEREN);

		refreshPanelBestellungVorschlag();

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * changed
	 * 
	 * @param eI
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (pdKritBestellvorschlag != null) {
			pdKritBestellvorschlag.eventItemchanged(eI);
		}
		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelBestellungVorschlagTopQP1) {
				// bei Doppelklick auf die Bewegungsvorschau wechseln
				setSelectedComponent(getPanelTabelleBewegungsvorschau(true));
			}
		} else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelBestellungVorschlagTopQP1) {

				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();

				if (key != null) {
					getInternalFrameBestellung()
							.setKeyWasForLockMe(
									panelBestellungVorschlagTopQP1
											.getSelectedId() + "");
				}

				panelBestellungVorschlagBottomD1.setKeyWhenDetailPanel(key);
				panelBestellungVorschlagBottomD1.eventYouAreSelected(false);
				panelBestellungVorschlagTopQP1.updateButtons();

				if (key != null) {
					BestellvorschlagDto dto = DelegateFactory.getInstance()
							.getBestellvorschlagDelegate()
							.bestellvorschlagFindByPrimaryKey((Integer) key);
					setBestellvorschlagDto(dto);

				}

				if (DelegateFactory.getInstance().getBestellvorschlagDelegate()
						.getAnzahlBestellvorschlagDesMandanten() > 0) {
					LPButtonAction item = (LPButtonAction) panelBestellungVorschlagTopQP1
							.getHmOfButtons()
							.get(ACTION_SPECIAL_BESTELLUNGEN_AUS_BESTELLVORSCHLAG);
					item.getButton().setEnabled(true);
				} else {
					LPButtonAction item = (LPButtonAction) panelBestellungVorschlagTopQP1
							.getHmOfButtons()
							.get(ACTION_SPECIAL_BESTELLUNGEN_AUS_BESTELLVORSCHLAG);
					item.getButton().setEnabled(false);
				}
				panelBestellungVorschlagTopQP1.enableToolsPanelButtons(
						panelBestellungVorschlagTopQP1.getSelectedIds() != null
								&& panelBestellungVorschlagTopQP1
										.getSelectedIds().length > 0,
						PanelBasis.ACTION_DELETE);
			} else if (eI.getSource() == panelQueryLieferantenoptimieren) {

				String text = new String("");

				Integer lieferantIId = (Integer) panelQueryLieferantenoptimieren
						.getKeyOfFilterComboBox();
				if (lieferantIId != null) {

					LieferantDto lfDto = DelegateFactory.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(lieferantIId);

					text += LPMain.getTextRespectUISPr("label.lieferart")
							+ ": "
							+ DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.lieferartFindByPrimaryKey(
											lfDto.getLieferartIId())
									.formatBez() + ", ";

					text = text.trim()
							+ LPMain.getTextRespectUISPr("label.zahlungsziel")
							+ ": ";
					text += DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.zahlungszielFindByPrimaryKey(
									lfDto.getZahlungszielIId()).getCBez();

					text = text.trim()
							+ ", "
							+ LPMain.getTextRespectUISPr("lp.mindestbestellwert")
							+ ": ";
					if (lfDto.getNMindestbestellwert() != null) {
						text += Helper.formatZahl(lfDto
								.getNMindestbestellwert(), 2, LPMain
								.getTheClient().getLocUi());
						text += " " + lfDto.getWaehrungCNr();
					}

				}

				lieferantInfos.setText(text);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelBestellungVorschlagBottomD1) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBestellungVorschlagTopQP1
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelTabelleBestellungBewegungsvorschau2) {
				// im QP die Buttons in den Zustand neu setzen.
				panelTabelleBestellungBewegungsvorschau2
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelBestellungVorschlagBottomD1) {
				panelBestellungVorschlagSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelBestellungVorschlagBottomD1) {
				Object oKey = panelBestellungVorschlagBottomD1
						.getKeyWhenDetailPanel();
				panelBestellungVorschlagTopQP1.eventYouAreSelected(false);
				panelBestellungVorschlagTopQP1.setSelectedId(oKey);
				panelBestellungVorschlagSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelBestellungVorschlagBottomD1) {
				Object oKey = panelBestellungVorschlagTopQP1.getSelectedId();

				// holt sich alten key wieder
				getInternalFrame().setKeyWasForLockMe(
						(oKey == null) ? null : oKey + "");

				if (panelBestellungVorschlagBottomD1.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelBestellungVorschlagTopQP1
							.getId2SelectAfterDelete();
					panelBestellungVorschlagTopQP1.setSelectedId(oNaechster);
				}

				panelBestellungVorschlagSP1.eventYouAreSelected(false); // refresh
																		// auf
																		// das
																		// gesamte
																		// 1:n
																		// panel

				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels deaktivieren
				if (panelBestellungVorschlagTopQP1.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_BESTELLUNGVORSCHLAG, false);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelBestellungVorschlagTopQP1) {

				/** @todo JO->JE wenn beans nach CW-FAQ dann test! PJ 5219 */
				panelBestellungVorschlagBottomD1
						.eventActionNew(eI, true, false);
				panelBestellungVorschlagBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelBestellungVorschlagSP1);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (eI.getSource() == pdKritBestellvorschlag) {
				try {
					// uhrp: 0 hier starten wir die Messung.
					myLogger.debug("BS>BV 1: " + 0);
					Defaults.lUhrQuickDirtyBS = System.currentTimeMillis();

					// einen neuen Bestellvorschlag erstellen
					if (pdKritBestellvorschlag
							.getVorhandeneBestellvorschlagEintrageLoeschen()) {
						DelegateFactory
								.getInstance()
								.getBestellvorschlagDelegate()
								.erstelleBestellvorschlag(
										pdKritBestellvorschlag
												.getAuftragsvorlaufzeit(),
										pdKritBestellvorschlag.getToleranz(),
										pdKritBestellvorschlag
												.getLieferterminFuerArtikelOhneReservierung(),
										pdKritBestellvorschlag.getLosIId(),
										pdKritBestellvorschlag.getAuftragIId(),
										pdKritBestellvorschlag
												.isbMitNichtlagerbeweirtschaftetenArtikeln(),
										pdKritBestellvorschlag
												.isbNurBetroffeneLospositionen(),
										pdKritBestellvorschlag
												.isbVormerklisteLoeschen());
					}
					panelBestellungVorschlagSP1.eventYouAreSelected(false);
					pdKritBestellvorschlag.eventYouAreSelected(false);
				} catch (Throwable t) {
					ExceptionLP efc = (ExceptionLP) t;

					if (efc != null
							&& efc.getICode() == EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.warning"),
								LPMain.getInstance().getMsg(efc));
					} else {
						throw new ExceptionLP(EJBExceptionLP.FEHLER,
								t.toString(), t);
					}
				}
			} else if (eI.getSource() == pdKritBestellvorschlagAnhandStuecklistenmindestlagerstand) {
				try {
					DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(
									pdKritBestellvorschlagAnhandStuecklistenmindestlagerstand
											.getLiefertermin(),
									pdKritBestellvorschlagAnhandStuecklistenmindestlagerstand
											.isbVormerklisteLoeschen());

					panelBestellungVorschlagSP1.eventYouAreSelected(false);
					pdKritBestellvorschlagAnhandStuecklistenmindestlagerstand
							.eventYouAreSelected(false);
				} catch (Throwable t) {
					ExceptionLP efc = (ExceptionLP) t;

					if (efc != null
							&& efc.getICode() == EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.warning"),
								LPMain.getInstance().getMsg(efc));
					} else {
						throw new ExceptionLP(EJBExceptionLP.FEHLER,
								t.toString(), t);
					}
				}
			} else if (eI.getSource() == pdKritUeberleitung) {
				// Kriterien
				BestellvorschlagUeberleitungKriterienDto kritDto = pdKritUeberleitung
						.getBestellvorschlagUeberleitungKriterienDto();

				// boolean der ueberprueft ob die ueberleitung fehlgeschlagen
				// oder funktioniert hat
				Boolean ueberleitungErfolgreich = false;
				if (kritDto.getBBelegprolieferantprotermin()) {
					ueberleitungErfolgreich = DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.createBESausBVfuerAlleLieferantenMitGleichenTermin(
									null,
									BestellungFilterFactory.getInstance()
											.sortBestellvorschlag(),
									kritDto.getKostenstelleIId(),
									kritDto.isBBeruecksichtigeProjektklammer());
				} else if (kritDto.getBBelegprolieferant()) {
					ueberleitungErfolgreich = DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.createBESausBVjeLieferant(
									null,
									BestellungFilterFactory.getInstance()
											.sortBestellvorschlag(),
									kritDto.getKostenstelleIId(),
									kritDto.isBBeruecksichtigeProjektklammer());
				} else if (kritDto.getBBelegeinlieferanteintermin()) {
					ueberleitungErfolgreich = DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.createBESausBVfuerBestimmtenLieferantUndTermin(
									BestellungFilterFactory
											.getInstance()
											.getFKBestellvorschlagFilter3(
													kritDto.getBelegeinlieferanteinterminLieferantIId(),
													Helper.formatDateWithSlashes(
															kritDto.getTBelegeinlieferanteinterminTermin())
															.toString()),
									BestellungFilterFactory.getInstance()
											.sortBestellvorschlag(),
									kritDto.getKostenstelleIId(),
									kritDto.isBBeruecksichtigeProjektklammer());
				} else if (kritDto.getBBelegeinlieferant()) {
					ueberleitungErfolgreich = DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.createBESausBVfueBestimmtenLieferant(
									BestellungFilterFactory
											.getInstance()
											.getFKBestellvorschlagFilter4(
													kritDto.getBelegeinlieferantLieferantIId()),
									BestellungFilterFactory.getInstance()
											.sortBestellvorschlag(),
									kritDto.getKostenstelleIId(),
									kritDto.isBBeruecksichtigeProjektklammer());
				} else if (kritDto.getBAbrufeZuRahmen()) {
					SortierKriterium[] ski = BestellungFilterFactory
							.getInstance().sortBestellvorschlag();
					FilterKriterium[] fk = null;
					BestellungDto[] bestellungDto = DelegateFactory
							.getInstance().getBestellvorschlagDelegate()
							.createBESausBVzuRahmen(fk, ski);
					String sBestellnummern = LPMain
							.getTextRespectUISPr("bes.rahmenueberliefert");
					boolean bUeberBestellt = false;
					if (bestellungDto.length > 0) {
						bUeberBestellt = true;
					}
					for (int i = 0; i < bestellungDto.length; i++) {
						sBestellnummern += bestellungDto[i].getCNr() + "\n";
					}
					if (bUeberBestellt) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								sBestellnummern);
					}
					// Wenn ohne Exception durch dann ok
					ueberleitungErfolgreich = true;
				}

				if (ueberleitungErfolgreich.booleanValue()) {
					// refresh aufs gesamte Panel
					panelBestellungVorschlagSP1.eventYouAreSelected(false);
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("bes.bestellvorschlagueberleitungfehlgeschlagen"));
				}
			} else if (eI.getSource() == pdKritSpaeterBestellbare) {
				// einen neuen Bestellvorschlag erstellen
				if (pdKritSpaeterBestellbare.getDate() != null) {
					DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.loescheSpaeterWiederbeschaffbarePositionen(
									pdKritSpaeterBestellbare.getDate());
				}
				panelBestellungVorschlagSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == pdLoeschenBis) {
				if (pdLoeschenBis.getDate() != null) {
					DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.loescheBestellvorschlaegeAbTermin(
									pdLoeschenBis.getDate());
					panelBestellungVorschlagSP1.eventYouAreSelected(false);
				}
			} else if (eI.getSource() == pdKritBestellvorschlagverdichtung) {
				// Bestellvorschlag verdichten
				DelegateFactory
						.getInstance()
						.getBestellvorschlagDelegate()
						.verdichteBestellvorschlag(
								pdKritBestellvorschlagverdichtung
										.getVerdichtungszeitraum(),
								pdKritBestellvorschlagverdichtung
										.getMindestbestellmengeBeruecksichtigen(),
								pdKritBestellvorschlagverdichtung
										.getProjektklammerBeruecksichtigen());
				panelBestellungVorschlagSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.equals(ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG)) {
				refreshPdKriterienBestellvorschlag();
				getInternalFrame().showPanelDialog(pdKritBestellvorschlag);
			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_STUECKLISTENMINDESTLAGERSTAND)) {
				refreshPdKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand();
				getInternalFrame()
						.showPanelDialog(
								pdKritBestellvorschlagAnhandStuecklistenmindestlagerstand);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_LIEFERANT_UEBERNEHMEN)) {
				// implement

				if (panelQueryLieferantenoptimieren.getSelectedId() != null
						&& panelQueryLieferantenoptimieren
								.getKeyOfFilterComboBox() != null) {
					Object[] selectedIds = panelQueryLieferantenoptimieren
							.getSelectedIds();
					Integer keyVorher = (Integer) panelQueryLieferantenoptimieren
							.getKeyOfFilterComboBox();

					for (int i = 0; i < selectedIds.length; i++) {

						DelegateFactory
								.getInstance()
								.getBestellvorschlagDelegate()
								.uebernimmLieferantAusLieferantOptimieren(
										(Integer) selectedIds[i],
										(Integer) panelQueryLieferantenoptimieren
												.getKeyOfFilterComboBox());
					}

					refreshLieferantenOptimieren();

					panelQueryLieferantenoptimieren
							.setKeyOfFilterComboBox(keyVorher);

					panelQueryLieferantenoptimieren.eventYouAreSelected(false);
					panelQueryLieferantenoptimieren
							.setSelectedId(selectedIds[0]);
					panelQueryLieferantenoptimieren.updateButtons();
				}

			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_LIEFERANT_ZURUECKSETZEN_OPTIMIEREN)) {
				// implement

				if (panelQueryLieferantenoptimieren.getSelectedId() != null) {

					Integer selectedId = (Integer) panelQueryLieferantenoptimieren
							.getSelectedId();
					ArrayList<Integer> al = new ArrayList<Integer>();
					al.add((Integer) panelQueryLieferantenoptimieren
							.getSelectedId());
					DelegateFactory.getInstance().getBestellvorschlagDelegate()
							.artikellieferantZuruecksetzen(al);

					refreshLieferantenOptimieren();

					panelQueryLieferantenoptimieren.eventYouAreSelected(false);
					panelQueryLieferantenoptimieren.setSelectedId(selectedId);
					panelQueryLieferantenoptimieren.updateButtons();
				}

			} else if (sAspectInfo.equals(ACTION_SPECIAL_MONATSSTATISTIK)) {
				// implement

				if (panelQueryLieferantenoptimieren.getSelectedId() != null) {

					BestellvorschlagDto bvDto = DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.bestellvorschlagFindByPrimaryKey(
									(Integer) panelQueryLieferantenoptimieren
											.getSelectedId());

					if (bvDto.getIArtikelId() != null) {
						ReportArtikelstatistik reportEtikett = new ReportArtikelstatistik(
								getInternalFrame(), bvDto.getIArtikelId(),
								true, "");
						getInternalFrame().showReportKriterien(reportEtikett,
								false);
					}

				}

			} else if (sAspectInfo.equals(ACTION_SPECIAL_GOTO_ARTIKEL)) {
				// implement

				if (panelQueryLieferantenoptimieren.getSelectedId() != null) {

					BestellvorschlagDto bvDto = DelegateFactory
							.getInstance()
							.getBestellvorschlagDelegate()
							.bestellvorschlagFindByPrimaryKey(
									(Integer) panelQueryLieferantenoptimieren
											.getSelectedId());

					if (bvDto.getIArtikelId() != null) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_ARTIKEL)) {
							InternalFrameArtikel ifArtikel = (InternalFrameArtikel) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_ARTIKEL);
							ifArtikel
									.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL,
											TabbedPaneArtikel.IDX_PANEL_AUSWAHL,
											bvDto.getIArtikelId(),
											null,
											ArtikelFilterFactory
													.getInstance()
													.createFKArtikellisteKey(
															(Integer) bvDto
																	.getIArtikelId()));
						}
					}

				}

			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_LIEFERANT_ZURUECKSETZEN_BESTELLVORSCHLAG)) {
				// implement

				if (panelBestellungVorschlagTopQP1.getSelectedIds() != null) {

					ArrayList alZeilen = new ArrayList();

					Collections.addAll(alZeilen,
							panelBestellungVorschlagTopQP1.getSelectedIds());

					DelegateFactory.getInstance().getBestellvorschlagDelegate()
							.artikellieferantZuruecksetzen(alZeilen);

					refreshPanelBestellungVorschlag();

					panelBestellungVorschlagTopQP1.eventYouAreSelected(false);
				}

			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_BESTELLUNGEN_AUS_BESTELLVORSCHLAG)) {
				refreshPdKriterienBestellvorschlagUeberleitung();
				getInternalFrame().showPanelDialog(pdKritUeberleitung);
			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_SPAETER_BESTELLBARE_LOESCHEN)) {
				refreshPdKriterienSpaeterBestellbare();
				getInternalFrame().showPanelDialog(pdKritSpaeterBestellbare);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_VERDICHTEN)) {
				// DelegateFactory.getInstance().getBestellvorschlagDelegate().
				// verdichteBestellvorschlag();
				// panelBestellungVorschlagSP1.eventYouAreSelected(false);
				refreshPdKriterienBestellvorschlagverdichtung();
				getInternalFrame().showPanelDialog(
						pdKritBestellvorschlagverdichtung);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (eI.getSource() == panelBestellungVorschlagTopQP1) {
				copyHV();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			if (eI.getSource() == panelBestellungVorschlagTopQP1) {
				einfuegenHV();
			}
		}
	}

	public void deleteAuswahl() throws ExceptionLP, Throwable {
		if (panelBestellungVorschlagTopQP1.getSelectedIds() == null)
			return;
		for (Object id : panelBestellungVorschlagTopQP1.getSelectedIds()) {
			DelegateFactory.getInstance().getBestellvorschlagDelegate()
					.removeBestellvorschlag((Integer) id);
		}
		panelBestellungVorschlagTopQP1.eventYouAreSelected(false);
	}

	public void einfuegenHV() throws IOException, ParserConfigurationException,
			SAXException, Throwable {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();
		if (o instanceof BelegpositionDto[]) {
			BestellvorschlagDto[] bestVorDto = DelegateFactory.getInstance()
					.getBelegpostionkonvertierungDelegate()
					.konvertiereNachBestellvorschlagDto((BelegpositionDto[]) o);
			Integer iId = null;
			for (int i = 0; i < bestVorDto.length; i++) {

				iId = DelegateFactory.getInstance()
						.getBestellvorschlagDelegate()
						.createBestellvorschlag(bestVorDto[i]);

			}

			panelBestellungVorschlagTopQP1.eventYouAreSelected(false);
			panelBestellungVorschlagTopQP1.setSelectedId(iId);
			panelBestellungVorschlagSP1.eventYouAreSelected(false);

		}
	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object[] iId = panelBestellungVorschlagTopQP1.getSelectedIds();
		BestellpositionDto[] bestellpositionDto = new BestellpositionDto[iId.length];
		for (int i = 0; i < iId.length; i++) {
			BestellvorschlagDto bestvorDto = DelegateFactory.getInstance()
					.getBestellvorschlagDelegate()
					.bestellvorschlagFindByPrimaryKey((Integer) iId[i]);
			if (bestvorDto != null) {
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(bestvorDto.getIArtikelId());
				bestellpositionDto[i] = new BestellpositionDto();
				bestellpositionDto[i].setArtikelIId(bestvorDto.getIArtikelId());
				bestellpositionDto[i].setBDrucken(Helper.boolean2Short(false));
				bestellpositionDto[i]
						.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
				bestellpositionDto[i]
						.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
				bestellpositionDto[i].setCBez(artikelDto.getArtikelsprDto()
						.getCBez());
				bestellpositionDto[i]
						.setLieferantIIdWennCopyInBestellvorschlag(bestvorDto
								.getILieferantId());
				bestellpositionDto[i].setEinheitCNr(artikelDto.getEinheitCNr());
				bestellpositionDto[i].setNMenge(bestvorDto
						.getNZubestellendeMenge());

				bestellpositionDto[i].setTUebersteuerterLiefertermin(bestvorDto
						.getTLiefertermin());
				if (bestvorDto.getNNettoeinzelpreis() != null) {
					bestellpositionDto[i].setNNettoeinzelpreis(bestvorDto
							.getNNettoeinzelpreis());
				} else {
					bestellpositionDto[i]
							.setNNettoeinzelpreis(new BigDecimal(0));
				}
				if (bestvorDto.getNNettogesamtpreis() != null) {
					bestellpositionDto[i].setNNettogesamtpreis(bestvorDto
							.getNNettogesamtpreis());
				} else {
					bestellpositionDto[i]
							.setNNettogesamtpreis(new BigDecimal(0));
				}
				if (bestvorDto.getNRabattbetrag() != null) {
					bestellpositionDto[i].setNRabattbetrag(bestvorDto
							.getNRabattbetrag());
				} else {
					bestellpositionDto[i].setNRabattbetrag(new BigDecimal(0));
				}
				if (bestvorDto.getDRabattsatz() != null) {
					bestellpositionDto[i].setDRabattsatz(bestvorDto
							.getDRabattsatz());
				} else {
					bestellpositionDto[i].setDRabattsatz(new Double(0));
				}
				bestellpositionDto[i].setNMaterialzuschlag(new BigDecimal(0));
				bestellpositionDto[i].setBNettopreisuebersteuert(Helper
						.boolean2Short(false));

				LPMain.getPasteBuffer().writeObjectToPasteBuffer(
						bestellpositionDto);
			}
		}

	}

	/**
	 * eventStateChanged
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_BESTELLUNGVORSCHLAG:
			refreshPanelBestellungVorschlag();
			this.panelBestellungVorschlagSP1.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			this.panelBestellungVorschlagTopQP1.updateButtons();

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelBestellungVorschlagTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_BESTELLUNGVORSCHLAG, false);
			}
			panelBestellungVorschlagTopQP1
					.updateButtons(panelBestellungVorschlagBottomD1
							.getLockedstateDetailMainKey());
			break;
		}
		switch (selectedIndex) {
		case IDX_PANEL_BEWEGUNGSVORSCHAU:
			getPanelTabelleBewegungsvorschau(true).eventYouAreSelected(false);
			getPanelTabelleBewegungsvorschau(true).updateButtons(
					new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));
		}
		switch (selectedIndex) {
		case IDX_PANEL_LIEFERANTEN_OPTIMIEREN:
			refreshLieferantenOptimieren();
			panelQueryLieferantenoptimieren.eventYouAreSelected(false);
			panelQueryLieferantenoptimieren.updateButtons();
		}
	}

	/**
	 * Diese Methode setzt den aktuellen Bestellvorschlag aus der Auswahlliste
	 * als die zu lockende Bestellung.
	 * 
	 * @throws Throwable
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = panelBestellungVorschlagTopQP1.getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_BESTELLVORSCHLAG)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportBestellVorschlag(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("bes.title.panel.bestellvorschlag")));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_GEAENDERTE_ARTIKEL)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportGeaenderteArtikel(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("best.jornal.geaenderteartikel")));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_VORSCHLAG_LOESCHEN_BIS_DATUM)) {
			refreshPdLoeschenBis();
			getInternalFrame().showPanelDialog(pdLoeschenBis);
		}
	}

	private PanelSplit refreshPanelBestellungVorschlag() throws Throwable {
		FilterKriterium[] fkBestellvorschlag = null;

		if (!LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
			SystemFilterFactory.getInstance().createFKMandantCNr();
		}

		if (panelBestellungVorschlagSP1 == null) {
			panelBestellungVorschlagBottomD1 = new PanelPositionenBestellvorschlag(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.bestellvorschlag"),
					null, // eventuell gibt es noch keine Position
					100);

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			panelBestellungVorschlagTopQP1 = new PanelQuery(
					null,
					fkBestellvorschlag,
					QueryParameters.UC_ID_BESTELLVORSCHLAG,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.title.panel.bestellvorschlag"),
					true); // flag, damit flr erst beim aufruf des panels
							// loslaeuft
			panelBestellungVorschlagTopQP1.setMultipleRowSelectionEnabled(true);

			panelBestellungVorschlagTopQP1.createAndSaveAndShowButton(
					"/com/lp/client/res/clipboard.png",
					LPMain.getTextRespectUISPr("bes.tooltip.bestellvorschlag"),
					ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelBestellungVorschlagTopQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/clipboard_next_down.png",
							LPMain.getTextRespectUISPr("bes.tooltip.bestellvorschlag.anhandmindestlagerstand"),
							ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_STUECKLISTENMINDESTLAGERSTAND,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelBestellungVorschlagTopQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/history_delete.png",
							LPMain.getTextRespectUISPr("bes.spaeterbestellbareloeschen"),
							ACTION_SPECIAL_SPAETER_BESTELLBARE_LOESCHEN,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelBestellungVorschlagTopQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/branch.png",
							LPMain.getTextRespectUISPr("bes.bestellvorschlagverdichten"),
							ACTION_SPECIAL_VERDICHTEN,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelBestellungVorschlagTopQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/clipboard_next.png",
							LPMain.getTextRespectUISPr("bes.tooltip.bestellungausbestellvorschlag"),
							ACTION_SPECIAL_BESTELLUNGEN_AUS_BESTELLVORSCHLAG,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);

			panelBestellungVorschlagTopQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/address_book216x16.png",
							LPMain.getTextRespectUISPr("bes.bestellvorschlag.lieferantzuruecksetzen"),
							ACTION_SPECIAL_LIEFERANT_ZURUECKSETZEN_BESTELLVORSCHLAG,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);

			panelBestellungVorschlagTopQP1.befuellePanelFilterkriterienDirekt(
					BestellungFilterFactory.getInstance()
							.createFKDLieferantPartnerName(),
					BestellungFilterFactory.getInstance()
							.createFKDBestellvorschlagArtikelnummer());
			panelBestellungVorschlagTopQP1
					.addDirektFilter(BestellungFilterFactory.getInstance()
							.createFKDBestellvorschlagTextsuche());

			panelBestellungVorschlagSP1 = new PanelSplit(getInternalFrame(),
					panelBestellungVorschlagBottomD1,
					panelBestellungVorschlagTopQP1, 190);

			setComponentAt(IDX_PANEL_BESTELLUNGVORSCHLAG,
					panelBestellungVorschlagSP1);
		}
		return panelBestellungVorschlagSP1;
	}

	/**
	 * @todo MB fuer was das??? mit WH klaeren
	 * 
	 * @throws Throwable
	 */
	// private void refreshPanelNichtMehrBenoetigteBestellungen() throws
	// Throwable {
	//
	// if (panelSplitNichtMehrBenoetigeBestellungenSP3 == null) {
	// panelQueryNichtMehrBenoetigeBestellungenQP3 = new PanelQuery(null,
	// null, QueryParameters.UC_ID_BESTELLUNGSTATUS, null,
	// getInternalFrame(),
	// LPMain.getTextRespectUISPr("lp.bestellungstatus"), true);
	//
	// panelNichtMehrBenoetigeBestellungenBottomD3 = new PanelStammdatenCRUD(
	// getInternalFrame(),
	// LPMain.getTextRespectUISPr("lp.bestellungstatus"), null,
	// HelperClient.SCRUD_BESTELLUNGSTATUS_FILE,
	// getInternalFrameBestellung(),
	// HelperClient.LOCKME_BESTELLUNGSTATUS);
	//
	// /** @todo JO QS PJ 5220 */
	// panelSplitNichtMehrBenoetigeBestellungenSP3 = new PanelSplit(
	// getInternalFrame(),
	// panelNichtMehrBenoetigeBestellungenBottomD3,
	// panelQueryNichtMehrBenoetigeBestellungenQP3, 200);
	//
	// setComponentAt(IDX_PANEL_NICHT_MEHR_BENOETIGTE_BESTELLUNGEN,
	// panelSplitNichtMehrBenoetigeBestellungenSP3);
	// }
	// }

	private void refreshFilterBewegungsvorschau() throws Throwable {
		if (getBestellvorschlagDto() != null) {
			if (getPanelTabelleBewegungsvorschau(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory
						.getInstance()
						.createFKBewegungsvorschau(
								getBestellvorschlagDto().getIArtikelId(), false);
				getPanelTabelleBewegungsvorschau(true).setDefaultFilter(krit);
			}
		}
	}

	private PanelTabelle getPanelTabelleBewegungsvorschau(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelTabelleBestellungBewegungsvorschau2 == null
				&& bNeedInstantiationIfNull) {

			panelTabelleBestellungBewegungsvorschau2 = new PanelTabelleBewegungsvorschau(
					QueryParameters.UC_ID_BEWEGUNGSVORSCHAU2,
					LPMain.getTextRespectUISPr("fert.tab.oben.bewegungsvorschau.title"),
					getInternalFrame());

			setComponentAt(IDX_PANEL_BEWEGUNGSVORSCHAU,
					panelTabelleBestellungBewegungsvorschau2);
			if (getBestellvorschlagDto() != null) {
				panelTabelleBestellungBewegungsvorschau2
						.setDefaultFilter(FertigungFilterFactory.getInstance()
								.createFKBewegungsvorschau(
										getBestellvorschlagDto()
												.getIArtikelId(), false));
			}
		}
		return panelTabelleBestellungBewegungsvorschau2;
	}

	private void refreshLieferantenOptimieren() throws Throwable {

		panelQueryLieferantenoptimieren = new PanelQuery(null,
				BestellungFilterFactory.getInstance()
						.createFKLieferantenoptimierenMandantCNr(),
				QueryParameters.UC_ID_LIEFERANTENOPTIMIEREN, null,
				getInternalFrame(), LPMain.getTextRespectUISPr("lp.auswahl"),
				true);
		panelQueryLieferantenoptimieren.addStatusBar();

		panelQueryLieferantenoptimieren.befuellePanelFilterkriterienDirekt(
				BestellungFilterFactory.getInstance()
						.createFKDLieferantenoptimierenArtikelnummer(),
				BestellungFilterFactory.getInstance()
						.createFKDLieferantenoptimierenTextsuche());

		panelQueryLieferantenoptimieren.setFilterComboBox(DelegateFactory
				.getInstance().getBestellvorschlagDelegate()
				.getAllLieferantenDesBestellvorschlages(), new FilterKriterium(
				"flrLieferantenoptimieren.lieferant_i_id_artikellieferant", true, "" + "",
				FilterKriterium.OPERATOR_EQUAL, false), true);

		panelQueryLieferantenoptimieren.setMultipleRowSelectionEnabled(true);

		panelQueryLieferantenoptimieren
				.createAndSaveAndShowButton(
						"/com/lp/client/res/check2.png",
						LPMain.getTextRespectUISPr("bes.bestellvorschlag.lieferantuebernehmen"),
						ACTION_SPECIAL_LIEFERANT_UEBERNEHMEN, KeyStroke
								.getKeyStroke('L',
										java.awt.event.InputEvent.CTRL_MASK),
						null);

		panelQueryLieferantenoptimieren
				.createAndSaveAndShowButton(
						"/com/lp/client/res/address_book216x16.png",
						LPMain.getTextRespectUISPr("bes.bestellvorschlag.lieferantzuruecksetzen"),
						ACTION_SPECIAL_LIEFERANT_ZURUECKSETZEN_OPTIMIEREN,
						RechteFac.RECHT_BES_BESTELLUNG_CUD);

		panelQueryLieferantenoptimieren.createAndSaveAndShowButton(
				"/com/lp/client/res/chart.png",
				LPMain.getTextRespectUISPr("lp.statistik.monate"),
				ACTION_SPECIAL_MONATSSTATISTIK,
				RechteFac.RECHT_BES_BESTELLUNG_R);

		panelQueryLieferantenoptimieren
				.createAndSaveAndShowButton(
						"/com/lp/client/res/data_into.png",
						LPMain.getTextRespectUISPr("bes.bestellvorschlag.gehezuartikel"),
						ACTION_SPECIAL_GOTO_ARTIKEL,
						RechteFac.RECHT_BES_BESTELLUNG_R);

		lieferantInfos = new JLabel();
		panelQueryLieferantenoptimieren.getToolBar().getToolsPanelCenter()
				.add(lieferantInfos);

		setComponentAt(IDX_PANEL_LIEFERANTEN_OPTIMIEREN,
				panelQueryLieferantenoptimieren);

	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {

		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);
		JMenu journal = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_JOURNAL);
		JMenuItem menuItemBestellVorschlag = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.bestellvorschlag"));
		menuItemBestellVorschlag.addActionListener(this);
		menuItemBestellVorschlag
				.setActionCommand(MENU_ACTION_JOURNAL_BESTELLVORSCHLAG);
		journal.add(menuItemBestellVorschlag);

		// PJ 17369
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_STRUKTURIERTER_STKLIMPORT,
						ParameterFac.KATEGORIE_STUECKLISTE,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null
				&& ((Integer) parameter.getCWertAsObject()) == 1) {
			JMenuItem menuItemGeaenderteArtikel = new JMenuItem(
					LPMain.getTextRespectUISPr("best.jornal.geaenderteartikel"));
			menuItemGeaenderteArtikel.addActionListener(this);
			menuItemGeaenderteArtikel
					.setActionCommand(MENU_ACTION_JOURNAL_GEAENDERTE_ARTIKEL);
			journal.add(menuItemGeaenderteArtikel);
		}

		JMenu jmModul = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);

		JMenuItem menuItemDateiAbDatumLoeschen = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.datei.bisdatumloeschen"));
		menuItemDateiAbDatumLoeschen.addActionListener(this);
		menuItemDateiAbDatumLoeschen
				.setActionCommand(MENU_ACTION_VORSCHLAG_LOESCHEN_BIS_DATUM);
		jmModul.add(menuItemDateiAbDatumLoeschen, 0);
		return wrapperMenuBar;
	}

	private PanelDialogKriterien refreshPdKriterienBestellvorschlag()
			throws Throwable {
		pdKritBestellvorschlag = new PanelDialogKriterienBestellvorschlag2(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.title.panelbestellvorschlagerzeugen"));
		return pdKritBestellvorschlag;
	}

	private PanelDialogKriterien refreshPdKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand()
			throws Throwable {
		pdKritBestellvorschlagAnhandStuecklistenmindestlagerstand = new PanelDialogKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.tooltip.bestellvorschlag.anhandmindestlagerstand"));
		return pdKritBestellvorschlagAnhandStuecklistenmindestlagerstand;
	}

	private PanelDialogKriterien refreshPdKriterienBestellvorschlagverdichtung()
			throws Throwable {
		pdKritBestellvorschlagverdichtung = new PanelDialogKriterienBestellvorschlagverdichtung(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.title.panelbestellvorschlagverdichten"));
		return pdKritBestellvorschlagverdichtung;
	}

	private PanelDialogKriterien refreshPdKriterienBestellvorschlagUeberleitung()
			throws Throwable {
		pdKritUeberleitung = new PanelDialogKriterienBestellvorschlagUeberleitung2(
				false,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.label.bestellvorschlagueberleitung"));
		return pdKritUeberleitung;
	}

	private PanelDialogKriterienSpaeterBestellbareLoeschen refreshPdKriterienSpaeterBestellbare()
			throws Throwable {
		pdKritSpaeterBestellbare = new PanelDialogKriterienSpaeterBestellbareLoeschen(
				getInternalFrame());
		return pdKritSpaeterBestellbare;
	}

	private PanelDialogKriterienBestellvorschlagLoeschenBisZu refreshPdLoeschenBis()
			throws Throwable {
		pdLoeschenBis = new PanelDialogKriterienBestellvorschlagLoeschenBisZu(
				getInternalFrame());
		return pdLoeschenBis;
	}

	public Object getDto() {
		return bestellvorschlagDto;
	}
}
