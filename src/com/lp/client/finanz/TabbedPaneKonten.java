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

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import com.lowagie.text.Font;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FibuFehlerDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Panels der Konten in der FiBu
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.58 $
 */

public abstract class TabbedPaneKonten extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryKonten = null;
	private PanelFinanzKontoKopfdaten panelDetailKontoKopfdaten = null;
	private PanelSplit panelSplit3 = null;
	private PanelQuery panelQueryBuchungen = null;
	private PanelFinanzBuchungDetails panelDetailBuchung = null;
	private PanelQuery panelKontolaenderartQP1 = null;
	private PanelFinanzKontolaenderart panelKontolaenderart = null;
	private PanelSplit panelSplitKontolaenderart = null;

	private PanelQuery panelKontoLandQP1 = null;
	private PanelFinanzKontoLand panelKontoLand = null;
	private PanelSplit panelSplitKontoLand = null;

	private PanelFinanzUmbuchung panelUmbuchung = null;
	private PanelFinanzSplittbuchung panelSplittbuchung = null;

	protected final static String MENU_ACTION_SALDENLISTE = "menu_action_saldenliste";
	protected final static String MENU_ACTION_ERFOLGSRECHNUNG = "menu_action_erfolgsrechnung";
	protected final static String MENU_ACTION_BILANZ = "menu_action_bilanz";
	protected final static String MENU_ACTION_UMBUCHUNG = "menu_action_umbuchung";
	protected final static String MENU_ACTION_SPLITTBUCHUNG = "menu_action_splittbuchung";
	protected final static String MENU_ACTION_MANUELLE_BUCHUNG = "menu_action_manuelle_buchung";
	protected final static String MENU_ACTION_KONTOBLAETTER = "menu_action_kontoblaetter";
	// protected final static String MENU_ACTION_DATEI_DRUCKEN_KONTOBLATT =
	// "MENU_ACTION_DATEI_DRUCKEN_KONTOBLATT";
	protected final static String MENU_ACTION_JOURNAL_KONTEN = "MENU_ACTION_JOURNAL_KONTEN";
	protected final static String MENU_ACTION_JOURNAL_KONTEN_DEBI = "MENU_ACTION_JOURNAL_KONTEN_DEBI";
	protected final static String MENU_ACTION_JOURNAL_KONTEN_KREDI = "MENU_ACTION_JOURNAL_KONTEN_KREDI";

	protected final static String MENU_ACTION_FINANZAMTSBUCHUNGEN = "MENU_ACTION_FINANZAMTSBUCHUNGEN";
	protected final static String MENU_ACTION_UVA = "MENU_ACTION_UVA";
	protected final static String MENU_ACTION_UVA_ZURUECKNEHMEN = "MENU_ACTION_UVA_ZURUECKNEHMEN";
	protected final static String MENU_ACTION_BEARBEITEN_STORNIEREN = "MENU_ACTION_BEARBEITEN_STORNIEREN";
	protected final static String MENU_ACTION_BEARBEITEN_BELEGUEBERNAHME = "MENU_ACTION_BEARBEITEN_BELEGUEBERNAHME";
	protected final static String MENU_ACTION_BEARBEITEN_IMPORTKONTEN = "MENU_ACTION_BEARBEITEN_IMPORTKONTEN";
	protected final static String MENU_ACTION_BEARBEITEN_PERIODENUEBERNAHME = "MENU_ACTION_BEARBEITEN_PERIODENUEBERNAHME";
	protected final static String MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG = "MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG";
	protected final static String MENU_ACTION_LIQUIDITAETSVORSCHAU = "MENU_ACTION_LIQUIDITAETSVORSCHAU";
	protected final static String MENU_ACTION_GESCHAEFTSJAHR_SPERRE = "MENU_ACTION_GESCHAEFTSJAHR_SPERRE";
	protected final static String MENU_ACTION_OFFENPOSTEN = "menu_action_offeneposten";
	private static final String ACTION_SPECIAL_AUSZIFFERN_NEU = "action_special_ausziffern_neu";
	private static final String ACTION_SPECIAL_AUSZIFFERN_LOESCHEN = "action_special_ausziffern_loeschen";
	private static final String ACTION_SPECIAL_NEUE_SPLITTBUCHUNG = "action_special_neue_splittbuchung";
	protected static final String ACTION_SPECIAL_UMBUCHUNG_AENDERN = "action_special_umbuchung_aendern";

	WrapperMenuItem menueItemStorno = null;

	private String kontotyp = null;
	// private String kontotyprichtig = null;
	private int usecaseId;

	public static int iDX_KONTEN = -1;
	public static int iDX_KOPFDATEN = -1;
	public static int iDX_BUCHUNGEN = -1;
	public static int iDX_KONTOLAENDERART = -1;
	public static int iDX_KONTOLAND = -1;

	// Eine gemeinsame Menubar fuer die Konten-Tabs

	private KontoDto kontoDto = null;

	private boolean bVollversion = false;

	private FinanzamtDto[] finanzamtDtos = null;

	private IGeschaeftsjahrViewController geschaeftsjahrViewController = null;
	private ICsvKontoImportController csvKontoImportController = null;


	private WrapperMenuItem[] menuItemUVAZuruecknehmenFa;

	private WrapperLabel kontoSaldo;
	private WrapperLabel wlbGeschaeftsjahrGesperrt = null;
	private Boolean geschaeftsjahrGesperrt;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param kontotyp
	 *            String
	 * @param sTitle
	 *            String
	 * @throws Throwable
	 */
	public TabbedPaneKonten(InternalFrame internalFrameI, String kontotyp,
			String sTitle, IGeschaeftsjahrViewController viewController)
			throws Throwable {
		super(internalFrameI, sTitle);
		this.kontotyp = kontotyp;
		geschaeftsjahrViewController = viewController;
		csvKontoImportController = new CsvKontoImportController();

		if (kontotyp.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			usecaseId = QueryParameters.UC_ID_FINANZKONTEN_DEBITOREN;
		} else if (kontotyp.equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
			usecaseId = QueryParameters.UC_ID_FINANZKONTEN_KREDITOREN;
		} else if (kontotyp.equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			usecaseId = QueryParameters.UC_ID_FINANZKONTEN_SACHKONTEN;
		}
		jbInit();
		initComponents();
		// kontotyprichtig = kontotyp;
	}

	protected KontoDto getKontoDto() {
		return kontoDto;
	}

	protected void setKontoDto(KontoDto kontoDto) throws Throwable {
		this.kontoDto = kontoDto;
		if (getKontoDto() != null) {
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getKontoDto().getCNr() + " " + getKontoDto().getCBez()
							+ " " + getKontoDto().getPartnerKurzbezeichnung());
			if (panelKontolaenderartQP1 != null
					&& getKontoDto().getIId() != null) {
				panelKontolaenderartQP1.setDefaultFilter(FinanzFilterFactory
						.getInstance().createFKKontolaenderart(
								getKontoDto().getIId()));
			}
			if (panelKontoLandQP1 != null && getKontoDto().getIId() != null) {
				panelKontoLandQP1.setDefaultFilter(FinanzFilterFactory
						.getInstance()
						.createFKKontoland(getKontoDto().getIId()));
			}
		} else {
			getInternalFrame()
					.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		}
	}

	/**
	 * Den Kontotyp holen.
	 * 
	 * @return String
	 */
	public String getKontotyp() {
		return this.kontotyp;
	}

	private void jbInit() throws Throwable {
		int index = 0;
		// Berechtigungen
		bVollversion = ((InternalFrameFinanz) getInternalFrame())
				.getBVollversion();

		// Tab 1: Liste der Konten
		iDX_KONTEN = index;
		insertTab(LPMain.getTextRespectUISPr("finanz.konten"), null, null,
				LPMain.getTextRespectUISPr("finanz.konten"), iDX_KONTEN);
		index++;
		// Tab 2: Kopfdaten
		iDX_KOPFDATEN = index;
		insertTab(LPMain.getTextRespectUISPr("lp.kopfdaten"), null, null,
				LPMain.getTextRespectUISPr("lp.kopfdaten"), iDX_KOPFDATEN);
		index++;
		if (bVollversion) {
			// Tab 3: Liste der Buchungen am Konto
			iDX_BUCHUNGEN = index;
			insertTab(LPMain.getTextRespectUISPr("finanz.buchungen"), null,
					null, LPMain.getTextRespectUISPr("finanz.buchungen"),
					iDX_BUCHUNGEN);
			index++;
		}
		if (kontotyp.equals(FinanzServiceFac.KONTOTYP_KREDITOR)
				|| kontotyp.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			// skip
		} else {
			// Tab 4: KontoLaenderart
			iDX_KONTOLAENDERART = index;
			insertTab(
					LPMain.getTextRespectUISPr("finanz.tab.oben.kontolaenderart.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("finanz.tab.oben.kontolaenderart.tooltip"),
					iDX_KONTOLAENDERART);
			index++;
			// Tab 5: KontoLand
			iDX_KONTOLAND = index;
			insertTab(
					LPMain.getTextRespectUISPr("finanz.tab.oben.kontoland.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("finanz.tab.oben.kontoland.tooltip"),
					iDX_KONTOLAND);
			index++;
		}
		// Default
		this.setSelectedComponent(getPanelQueryKonten());
		// refresh
		// getPanelQueryKonten().eventYouAreSelected(false);
		// damit gleich eine selektiert ist
		// ItemChangedEvent it = new ItemChangedEvent(getPanelQueryKonten(),
		// ItemChangedEvent.ITEM_CHANGED);
		// lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * initPanelTop1QueryKonten.
	 * 
	 * @return PanelQuery
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryKonten() throws Throwable {
		if (panelQueryKonten == null) {
			String[] aWhichButtonIUseKonten = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filtersKonten = buildFiltersKonten();

			panelQueryKonten = new PanelQuery(null, filtersKonten, usecaseId,
					aWhichButtonIUseKonten, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true);
			FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
					.createFKDKontonummer();
			FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
					.createFKDKontobezeichnung();
			FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
					.createFKVKonto();
			panelQueryKonten.befuellePanelFilterkriterienDirektUndVersteckte(
					fkDirekt1, fkDirekt2, fkVersteckt);
			this.setComponentAt(iDX_KONTEN, panelQueryKonten);
			revalidate(); // todo: ghp workaround?
		}
		return panelQueryKonten;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// da will jemand die Buchungen ansehen
			if (e.getSource() == getPanelQueryKonten()) {
				Object key = getPanelQueryKonten().getSelectedId();
				holeKontoDto(key);
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					if (bVollversion) {
						setSelectedComponent(getPanelSplit3());
						getPanelSplit3().eventYouAreSelected(false);
					} else {
						setSelectedComponent(getPanelDetailKontoKopfdaten());
						getPanelDetailKontoKopfdaten().eventYouAreSelected(
								false);
					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (e.getSource().equals(panelQueryBuchungen)
					&& panelQueryBuchungen.getSelectedId() != null) {
				BuchungdetailDto detail = DelegateFactory
						.getInstance()
						.getBuchenDelegate()
						.buchungdetailFindByPrimaryKey(
								(Integer) panelQueryBuchungen.getSelectedId());
				getInternalFrame()
						.showReportKriterien(
								new ReportBuchungsbeleg(
										getInternalFrameFinanz(),
										LPMain.getTextRespectUISPr("fb.report.buchungsbeleg"),
										detail.getBuchungIId()));
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryKonten()) {
				Object key = getPanelQueryKonten().getSelectedId();
				holeKontoDto(key);
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						iDX_KONTEN, key != null);
				// if (key == null) {
				// getInternalFrame().enableAllOberePanelsExceptMe(this,
				// iDX_KONTEN, false);
				// } else {
				// getInternalFrame().enableAllOberePanelsExceptMe(this,
				// iDX_KONTEN, true);
				// }
				getPanelQueryKonten().updateButtons();
			} else if (e.getSource() == panelQueryBuchungen) {
				Integer iId = (Integer) panelQueryBuchungen.getSelectedId();
				// ghp holeKontoDto(iId) ;
				panelDetailBuchung.setKeyWhenDetailPanel(iId);
				panelDetailBuchung.eventYouAreSelected(false);

				// ghp panelQueryBuchungen.setKeyWhenDetailPanel(iId) ;
				panelQueryBuchungen.updateButtons();
				berechneSaldo();

				// ghp panelDetailBuchung.invalidate();
				// ghp panelQueryBuchungen.invalidate() ;
			} else if (e.getSource() == panelKontolaenderartQP1) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelKontolaenderart.setKeyWhenDetailPanel(key);
				panelKontolaenderart.eventYouAreSelected(false);
				panelKontolaenderartQP1.updateButtons();
			} else if (e.getSource() == panelKontoLandQP1) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelKontoLand.setKeyWhenDetailPanel(key);
				panelKontoLand.eventYouAreSelected(false);
				panelKontoLandQP1.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryKonten()) {
				if (getPanelQueryKonten().getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				getPanelDetailKontoKopfdaten()
						.eventActionNew(null, true, false);
				setSelectedComponent(getPanelDetailKontoKopfdaten());
			} else if (e.getSource() == panelKontolaenderartQP1) {
				panelKontolaenderart.eventActionNew(e, true, false);
				panelKontolaenderart.eventYouAreSelected(false);
				/*
				 * // locknew: 2 den Panels den richtigen lockstatus geben
				 * 
				 * LockStateValue lockstateValue = new LockStateValue(null,
				 * null, PanelBasis.LOCK_FOR_NEW);
				 * panelKontolaenderart.updateButtons(lockstateValue);
				 * panelKontolaenderartQP1.updateButtons();
				 */
			} else if (e.getSource() == panelKontoLandQP1) {
				panelKontoLand.eventActionNew(e, true, false);
				panelKontoLand.eventYouAreSelected(false);

				/*
				 * LockStateValue lockstateValue = new LockStateValue(null,
				 * null, PanelBasis.LOCK_FOR_NEW);
				 * panelKontoLand.updateButtons(lockstateValue);
				 * panelKontoLandQP1.updateButtons();
				 */
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelDetailKontoKopfdaten()) {
				this.setSelectedComponent(getPanelQueryKonten());
				getPanelQueryKonten().eventYouAreSelected(false);
			} else if (e.getSource() == panelKontolaenderart) {
				panelSplitKontolaenderart.eventYouAreSelected(false);
			} else if (e.getSource() == panelKontoLand) {
				panelSplitKontoLand.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelDetailKontoKopfdaten()) {
				// nix
			} else if (e.getSource() == panelKontolaenderart) {
				panelSplitKontolaenderart.eventYouAreSelected(false);
			} else if (e.getSource() == panelKontoLand) {
				panelSplitKontoLand.eventYouAreSelected(false);
			} else if (e.getSource() == panelUmbuchung
					|| e.getSource() == panelSplittbuchung) {
				if (this.panelSplit3 != null) {
					// Object key = getPanelQueryKonten().getSelectedId();
					// holeKontoDto(key);
					if (getInternalFrame().getSelectedTabbedPane().equals(this))
						panelSplit3.eventYouAreSelected(false);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelKontolaenderart) {
				Object key = panelKontolaenderart.getKeyWhenDetailPanel();
				panelSplitKontolaenderart.eventYouAreSelected(false);
				panelKontolaenderartQP1.setSelectedId(key);
				panelSplitKontolaenderart.eventYouAreSelected(false);
			} else if (e.getSource() == panelKontoLand) {
				Object key = panelKontoLand.getKeyWhenDetailPanel();
				panelSplitKontoLand.eventYouAreSelected(false);
				panelKontoLandQP1.setSelectedId(key);
				panelSplitKontoLand.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKontoKopfdaten) {
				getPanelQueryKonten().clearDirektFilter();
				Object key = getPanelDetailKontoKopfdaten()
						.getKeyWhenDetailPanel();
				getPanelQueryKonten().eventYouAreSelected(false);
				getPanelQueryKonten().setSelectedId(key);
				getPanelQueryKonten().eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			if (e.getSource() == panelQueryBuchungen) {
				String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
				if (sAspectInfo.contains(ACTION_SPECIAL_UMBUCHUNG_AENDERN)) {
					buchungAendern();
				} else if (sAspectInfo.contains(ACTION_SPECIAL_AUSZIFFERN_NEU)) { 
					Object[] ids = panelQueryBuchungen.getSelectedIds();
					if (ids != null && ids.length > 0) {
						Integer[] iIds = new Integer[ids.length];
						System.arraycopy(ids, 0, iIds, 0, ids.length);
						DelegateFactory.getInstance()
								.getFinanzServiceDelegate()
								.createAuszifferung(iIds);
						panelQueryBuchungen.eventActionRefresh(null, false);
					}
					
				} else if (sAspectInfo
						.contains(ACTION_SPECIAL_AUSZIFFERN_LOESCHEN)) {
					Object[] ids = panelQueryBuchungen.getSelectedIds();
					if (ids != null && ids.length > 0) {
						Integer[] iIds = new Integer[ids.length];
						System.arraycopy(ids, 0, iIds, 0, ids.length);
						DelegateFactory.getInstance()
								.getFinanzServiceDelegate()
								.removeAuszifferung(iIds);
						panelQueryBuchungen.eventActionRefresh(null, false);
					}
				} else if (sAspectInfo
						.contains(ACTION_SPECIAL_NEUE_SPLITTBUCHUNG)) {
					Integer id = (Integer) panelQueryBuchungen.getSelectedId();

					PanelFinanzSplittbuchung sb = getPanelSplittbuchung();
					BuchungdetailDto detail = DelegateFactory.getInstance()
							.getBuchenDelegate()
							.buchungdetailFindByPrimaryKey(id);
					sb.setBuchungDto(
							DelegateFactory
									.getInstance()
									.getBuchenDelegate()
									.buchungFindByPrimaryKey(
											detail.getBuchungIId()), this);
					getInternalFrame().showPanelDialog(sb);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			super.lPEventItemChanged(e);
		}
	}

	private void berechneSaldo() throws ExceptionLP, Throwable {
		kontoSaldo.setText(LPMain.getTextRespectUISPr("finanz.saldo")
				+ " = "
				+ Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getBuchenDelegate()
								.getSaldoVonKontoMitEB(
										kontoDto.getIId(),
										getInternalFrameFinanz()
												.getIAktuellesGeschaeftsjahr(),
										-1), LPMain.getTheClient().getLocUi())
				+ " " + LPMain.getTheClient().getSMandantenwaehrung());
	}

	private void buchungAendern() throws Throwable {
		Integer iId = (Integer) panelQueryBuchungen.getSelectedId();
		panelDetailBuchung.setKeyWhenDetailPanel(iId);
		panelDetailBuchung.eventYouAreSelected(false);
		BuchungdetailDto buchungdetailDto = DelegateFactory.getInstance()
				.getBuchenDelegate().buchungdetailFindByPrimaryKey(iId);
		BuchungdetailDto[] bDetails = DelegateFactory
				.getInstance()
				.getBuchenDelegate()
				.buchungdetailsFindByBuchungIIdOhneMitlaufende(
						buchungdetailDto.getBuchungIId());
		BuchungDto buchungDto = DelegateFactory.getInstance()
				.getBuchenDelegate()
				.buchungFindByPrimaryKey(buchungdetailDto.getBuchungIId());
		if (bDetails.length > 3
				|| (bDetails[0].getNUst().signum() == 0 && bDetails.length > 2)) {
			// ist eine Splittbuchung, derzeit nicht editierbar
			PanelFinanzSplittbuchung sb = getPanelSplittbuchung();
			sb.setBuchungDto(buchungDto, this, true);

			getInternalFrame().showPanelDialog(sb);
		} else {
			PanelFinanzUmbuchung ub = getPanelUmbuchung();
			ub.setGeschaeftsjahr(getInternalFrameFinanz()
					.getIAktuellesGeschaeftsjahr());
			ub.setBuchungDto(buchungDto, this);
			getInternalFrame().showPanelDialog(ub);
		}
	}

	/**
	 * hole KassenbuchDto.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeKontoDto(Object key) throws Throwable {
		if (key != null) {
			setKontoDto(DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey((Integer) key));
			getInternalFrame().setKeyWasForLockMe(key.toString());
			getPanelDetailKontoKopfdaten().setKeyWhenDetailPanel(key);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		// lazy loading und titel setzen
		int index = this.getSelectedIndex();
		if (menueItemStorno != null) {
			menueItemStorno.setEnabled(false);
		}
		if (index == iDX_KONTEN) {
			invalidate();
			getPanelQueryKonten().eventYouAreSelected(false);
		} else if (index == iDX_KOPFDATEN) {
			getPanelDetailKontoKopfdaten().eventYouAreSelected(false);
		} else if (index == iDX_BUCHUNGEN) {
			resetPanelSplit3();

			if (menueItemStorno != null) {
				menueItemStorno.setEnabled(true);
			}

			invalidate();
			panelQueryBuchungen.setDefaultFilter(buildFiltersBuchungen());
			panelQueryBuchungen.eventYouAreSelected(false);
		} else if (index == iDX_KONTOLAENDERART) {
			refreshKontolaenderart();
			panelSplitKontolaenderart.eventYouAreSelected(false);
		} else if (index == iDX_KONTOLAND) {
			refreshKontoLand();
			panelSplitKontoLand.eventYouAreSelected(false);
		}

		if (getKontoDto() != null) {
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getKontoDto().getCNr() + " " + getKontoDto().getCBez()
							+ " " + getKontoDto().getPartnerKurzbezeichnung());

		} else {
			getInternalFrame()
					.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		}

	}

	private FilterKriterium[] buildFiltersBuchungen() throws Throwable {
		FilterKriterium[] filtersAll = null;

		if (getKontoDto() != null) {
			filtersAll = Helper.copyFilterKriterium(FinanzFilterFactory
					.getInstance()
					.createFKBuchungDetail(getKontoDto().getIId()), 1);

			filtersAll[filtersAll.length - 1] = ((InternalFrameFinanz) getInternalFrame())
					.getFKforAktuellesGeschaeftsjahrInDetails();
			// filtersAll[filtersAll.length - 1] = new FilterKriterium(
			// FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
			// + FinanzFac.FLR_BUCHUNG_GESCHAEFTSJAHR_I_GESCHAEFTSJAHR,
			// true,
			// "'"
			// + ((InternalFrameFinanz)
			// getInternalFrame()).getAktuellesGeschaeftsjahr()
			// + "'", FilterKriterium.OPERATOR_EQUAL, false);
		}

		return filtersAll;
	}

	private FilterKriterium[] buildFiltersKonten() throws Throwable {
		FilterKriterium[] filter = null;
		if (this.kontotyp.equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			filter = FinanzFilterFactory.getInstance()
					.createFKSachkontenInklMitlaufende();
		} else if (this.kontotyp.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			filter = FinanzFilterFactory.getInstance()
					.createFKDebitorenkonten();
		} else {
			filter = FinanzFilterFactory.getInstance()
					.createFKKreditorenkonten();
		}
		return filter;
	}

	private void initPanelTop3QueryBuchungen() throws Throwable {
		if (panelQueryBuchungen == null) {
			QueryType[] qtBuchungen = FinanzFilterFactory.getInstance()
					.createQTBuchungDetail();
			String[] aWhichButtonIUseBuchungen = { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER,
					// PanelBasis.ALWAYSENABLED + "ausziffern_neu",
					// PanelBasis.ACTION_LEEREN + "ausziffern",
					// PanelBasis.ALWAYSENABLED + "ausziffern_loeschen",
					PanelBasis.ACTION_PRINT };
			FilterKriterium[] filtersBuchungen = buildFiltersBuchungen();

			panelQueryBuchungen = new PanelQuery(qtBuchungen, filtersBuchungen,
					QueryParameters.UC_ID_BUCHUNGDETAIL,
					aWhichButtonIUseBuchungen, getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.buchungen"), true);

			panelQueryBuchungen.createAndSaveAndShowButton(
					"/com/lp/client/res/edit.png",
					LPMain.getTextRespectUISPr("finanz.buchung.aendern"),
					PanelBasis.LEAVEALONE + ACTION_SPECIAL_UMBUCHUNG_AENDERN,
					null, null);

			// if (!kontotyp.equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			panelQueryBuchungen.createAndSaveAndShowButton(
					"/com/lp/client/res/link_add.png",
					LPMain.getTextRespectUISPr("fb.ausziffern.neu"),
					PanelBasis.ALWAYSENABLED
							+ ACTION_SPECIAL_AUSZIFFERN_NEU, null, null);

//			panelQueryBuchungen.createAndSaveAndShowButton(
//					"/com/lp/client/res/link.png",
//					LPMain.getTextRespectUISPr("fb.ausziffern.hinzu"),
//					PanelBasis.ALWAYSENABLED + ACTION_SPECIAL_AUSZIFFERN_HINZU,
//					null, null);

			panelQueryBuchungen.createAndSaveAndShowButton(
					"/com/lp/client/res/link_delete.png",
					LPMain.getTextRespectUISPr("fb.ausziffern.entfernen"),
					PanelBasis.ALWAYSENABLED
							+ ACTION_SPECIAL_AUSZIFFERN_LOESCHEN, null, null);
			panelQueryBuchungen
					.createAndSaveAndShowButton(
							"/com/lp/client/res/note_new.png",
							LPMain.getTextRespectUISPr("finanz.neueSplittbuchungAusBuchung"),
							PanelBasis.ALWAYSENABLED
									+ ACTION_SPECIAL_NEUE_SPLITTBUCHUNG, null,
							null);
			panelQueryBuchungen.updateButtons();
			// }

			panelQueryBuchungen
					.befuellePanelFilterkriterienDirektUndVersteckte(
							FinanzFilterFactory.getInstance()
									.createFKDBelegnummer(), null,
							FinanzFilterFactory.getInstance()
									.createFKVBuchungStorno(), LPMain
									.getTextRespectUISPr("lp.plusstornierte"));
			if (showNurOffeneInBuchungen())
				panelQueryBuchungen
						.befuelleFilterkriteriumSchnellansicht(FinanzFilterFactory
								.getInstance().createFKSchnellansicht());

			kontoSaldo = new WrapperLabel();
			wlbGeschaeftsjahrGesperrt = new WrapperLabel();

			kontoSaldo.setPreferredSize(new Dimension(Defaults.getInstance()
					.bySizeFactor(150), 0));
			wlbGeschaeftsjahrGesperrt.setPreferredSize(new Dimension(Defaults
					.getInstance().bySizeFactor(250), 0));
			wlbGeschaeftsjahrGesperrt.setForeground(Color.RED);
			wlbGeschaeftsjahrGesperrt.setFont(wlbGeschaeftsjahrGesperrt
					.getFont().deriveFont(Font.BOLD));
			isGeschaeftsjahrGesperrt();

			panelQueryBuchungen.getToolBar().getToolsPanelCenter()
					.add(wlbGeschaeftsjahrGesperrt);
			panelQueryBuchungen.getToolBar().getToolsPanelCenter()
					.add(kontoSaldo);

		}
		// Filter updaten
		panelQueryBuchungen.setDefaultFilter(buildFiltersBuchungen());
	}

	protected boolean isGeschaeftsjahrGesperrt() throws ExceptionLP, Throwable {
		if (geschaeftsjahrGesperrt == null)
			geschaeftsjahrGesperrt = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.isGeschaeftsjahrGesperrt(
							((InternalFrameFinanz) getInternalFrame())
									.getIAktuellesGeschaeftsjahr());
		wlbGeschaeftsjahrGesperrt.setVisible(geschaeftsjahrGesperrt);
		wlbGeschaeftsjahrGesperrt.setText(LPMain.getMessageTextRespectUISPr(
				"finanz.error.geschaeftsjahrgesperrt",
				((InternalFrameFinanz) getInternalFrame())
						.getAktuellesGeschaeftsjahr()));
		return geschaeftsjahrGesperrt;
	}

	protected boolean showNurOffeneInBuchungen() {
		return true;
	}

	/**
	 * Drucken des Kontoblattes
	 * 
	 * @throws Throwable
	 */
	protected void printKontoblatt() throws Throwable {
		if (getKontoDto() != null) {
			Integer kontoIId = getKontoDto().getIId();
			BuchungdetailDto[] buchungen = DelegateFactory.getInstance()
					.getBuchenDelegate().buchungdetailFindByKontoIId(kontoIId);
			if (buchungen.length == 0) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						"Keine Buchungen auf diesem Konto");
			} else {
				String sTitle = kontoDto.getCNr() + " "
						+ LPMain.getTextRespectUISPr("finanz.buchungen");
				getInternalFrame().showReportKriterien(
						new ReportBuchungenAufKonto(getInternalFrame(),
								kontoDto, sTitle));
			}
		} else {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					"Keine Daten zu drucken");
		}
	}

	private PanelFinanzKontoKopfdaten getPanelDetailKontoKopfdaten()
			throws Throwable {
		if (panelDetailKontoKopfdaten == null) {
			panelDetailKontoKopfdaten = new PanelFinanzKontoKopfdaten(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kopfdaten"), null, this);
			if (usecaseId == QueryParameters.UC_ID_FINANZKONTEN_SACHKONTEN) {
				panelDetailKontoKopfdaten.setPrintKontoart(true);
			} else {
				panelDetailKontoKopfdaten.setPrintKontoart(false);
			}
			this.setComponentAt(iDX_KOPFDATEN, panelDetailKontoKopfdaten);
		}
		return panelDetailKontoKopfdaten;
	}

	/*
	 * Ein Neuladen des Splitpanels f&uuml;r Buchungen&Details erzwingen
	 */
	private PanelSplit resetPanelSplit3() throws Throwable {
		panelSplit3 = null;
		panelQueryBuchungen = null;
		return getPanelSplit3();
	}

	private PanelSplit getPanelSplit3() throws Throwable {
		if (panelSplit3 == null) {
			panelDetailBuchung = new PanelFinanzBuchungDetails(
					getInternalFrame(), "Buchungen", this);
			initPanelTop3QueryBuchungen();
			panelSplit3 = new PanelSplit(getInternalFrame(),
					panelDetailBuchung, panelQueryBuchungen, 280);
			this.setComponentAt(iDX_BUCHUNGEN, panelSplit3);
			panelQueryBuchungen.setMultipleRowSelectionEnabled(true);

		}
		return panelSplit3;
	}

	protected PanelQuery getPanelQueryBuchungen() {
		return panelQueryBuchungen;
	}

	private void refreshKontolaenderart() throws Throwable {

		if (panelSplitKontolaenderart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelKontolaenderartQP1 = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_KONTOLAENDERART,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.tab.oben.kontolaenderart.title"),
					true);

			panelKontolaenderart = new PanelFinanzKontolaenderart(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.tab.oben.kontolaenderart.title"),
					null, this);

			panelSplitKontolaenderart = new PanelSplit(getInternalFrame(),
					panelKontolaenderart, panelKontolaenderartQP1, 200);
			setComponentAt(iDX_KONTOLAENDERART, panelSplitKontolaenderart);
			panelKontolaenderartQP1.setDefaultFilter(FinanzFilterFactory
					.getInstance().createFKKontolaenderart(
							getKontoDto().getIId()));

			// liste soll sofort angezeigt werden
			panelKontolaenderartQP1.eventYouAreSelected(true);
		}
	}

	private void refreshKontoLand() throws Throwable {

		if (panelSplitKontoLand == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelKontoLandQP1 = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_KONTOLAND,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.tab.oben.kontolaenderart.title"),
					true);

			panelKontoLand = new PanelFinanzKontoLand(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.tab.oben.kontoland.title"),
					null, this);

			panelSplitKontoLand = new PanelSplit(getInternalFrame(),
					panelKontoLand, panelKontoLandQP1, 200);
			setComponentAt(iDX_KONTOLAND, panelSplitKontoLand);
			panelKontoLandQP1.setDefaultFilter(FinanzFilterFactory
					.getInstance().createFKKontoland(getKontoDto().getIId()));

			// liste soll sofort angezeigt werden
			panelKontoLandQP1.eventYouAreSelected(true);
		}
	}

	private void addImportMenuItem(JMenu menu) throws Throwable {
		WrapperMenuItem menueItemImportKonten = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("finanz.importkonten"),
				RechteFac.RECHT_FB_CHEFBUCHHALTER);
		menueItemImportKonten.addActionListener(this);
		menueItemImportKonten
				.setActionCommand(MENU_ACTION_BEARBEITEN_IMPORTKONTEN);
		menu.add(menueItemImportKonten, 0);
	}

	protected final javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmbKonten = ((InternalFrameFinanz) getInternalFrame()).wmbKonten;

		if (wmbKonten == null) {
			wmbKonten = new WrapperMenuBar(this);
			JMenu jmDatei = (JMenu) wmbKonten
					.getComponent(WrapperMenuBar.MENU_MODUL);
			JMenu jmBearbeiten = (JMenu) wmbKonten
					.getComponent(WrapperMenuBar.MENU_BEARBEITEN);
			JMenu jmJournal = (JMenu) wmbKonten
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			if (bVollversion) {
				// Menu Datei
				/*
				 * JMenu menuDrucken = new WrapperMenu("lp.menu.drucken", this);
				 * jmDatei.add(new JSeparator(), 0); jmDatei.add(menuDrucken,
				 * 0); // Kontoblatt WrapperMenuItem menueItemKontoblatt = null;
				 * menueItemKontoblatt = new WrapperMenuItem("Kontoblatt",
				 * null); menueItemKontoblatt.addActionListener(this);
				 * menueItemKontoblatt
				 * .setActionCommand(MENU_ACTION_DATEI_DRUCKEN_KONTOBLATT);
				 * menuDrucken.add(menueItemKontoblatt);
				 */
				// Menu Bearbeiten
				// JMenu menuePeriode = new
				// WrapperMenu("fb.menu.geschaeftsjahr",
				// this);
				// Map<?, ?> mapGJ = DelegateFactory.getInstance()
				// .getSystemDelegate().getAllGeschaeftsjahr();
				// ButtonGroup bgGJ = new ButtonGroup();
				// for (Iterator<?> iter = mapGJ.keySet().iterator(); iter
				// .hasNext();) {
				// Integer item = (Integer) iter.next();
				// JRadioButtonMenuItem menueItem = new JRadioButtonMenuItem(
				// item.toString());
				// menueItem.addActionListener(this);
				// menueItem
				// .setActionCommand(TabbedPaneKonten.MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG
				// + item.toString());
				// if (iter.hasNext() == false) {
				// menueItem.setSelected(true);
				// ((InternalFrameFinanz)
				// getInternalFrame()).setAktuellesGeschaeftsjahr(item
				// .toString());
				// }
				// menuePeriode.add(menueItem, 0);
				// bgGJ.add(menueItem);
				// }

				addImportMenuItem(jmDatei);

				JMenu menuePeriode = geschaeftsjahrViewController
						.getGeschaeftsJahreMenue(this, this);
				jmBearbeiten.add(menuePeriode);

				WrapperMenuItem menueItemManuelleBuchung = null;
				menueItemManuelleBuchung = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.menu.manuellebuchung"),
						RechteFac.RECHT_FB_FINANZ_CUD);
				menueItemManuelleBuchung.addActionListener(this);
				menueItemManuelleBuchung
						.setActionCommand(MENU_ACTION_MANUELLE_BUCHUNG);
				jmBearbeiten.add(menueItemManuelleBuchung);

				WrapperMenuItem menueItemSplittbuchung = null;
				menueItemSplittbuchung = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.menu.splittbuchung"),
						RechteFac.RECHT_FB_FINANZ_CUD);
				menueItemSplittbuchung.addActionListener(this);
				menueItemSplittbuchung
						.setActionCommand(MENU_ACTION_SPLITTBUCHUNG);
				jmBearbeiten.add(menueItemSplittbuchung);

				menueItemStorno = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("finanz.buchungstornieren"),
						RechteFac.RECHT_FB_FINANZ_CUD);
				menueItemStorno.addActionListener(this);
				menueItemStorno
						.setActionCommand(MENU_ACTION_BEARBEITEN_STORNIEREN);
				jmBearbeiten.add(menueItemStorno);

				WrapperMenuItem menueItemBeleguebernahme = null;
				menueItemBeleguebernahme = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("finanz.beleguebernahme"),
						RechteFac.RECHT_FB_CHEFBUCHHALTER);
				menueItemBeleguebernahme.addActionListener(this);
				menueItemBeleguebernahme
						.setActionCommand(MENU_ACTION_BEARBEITEN_BELEGUEBERNAHME);
				jmBearbeiten.add(menueItemBeleguebernahme);

				finanzamtDtos = DelegateFactory.getInstance()
						.getFinanzDelegate()
						.finanzamtFindAllByMandantCNr(LPMain.getTheClient());
				JMenu menuItemUVAZuruecknehmen = null;
				menuItemUVAZuruecknehmen = new JMenu(
						LPMain.getTextRespectUISPr("fb.menu.uvazuruecknehmen"));

				menuItemUVAZuruecknehmenFa = new WrapperMenuItem[finanzamtDtos.length];
				for (int i = 0; i < finanzamtDtos.length; i++) {
					menuItemUVAZuruecknehmenFa[i] = new WrapperMenuItem(
							finanzamtDtos[i].getPartnerDto().formatName(),
							RechteFac.RECHT_FB_CHEFBUCHHALTER);
					menuItemUVAZuruecknehmenFa[i].addActionListener(this);
					menuItemUVAZuruecknehmenFa[i]
							.setActionCommand(MENU_ACTION_UVA_ZURUECKNEHMEN
									+ "|" + i);
					menuItemUVAZuruecknehmen.add(menuItemUVAZuruecknehmenFa[i],
							0);
				}
				updateUvaMenus(); // letzte Verprobung im Menutext eintragen
				jmBearbeiten.add(menuItemUVAZuruecknehmen);

				WrapperMenuItem menuItemGeschaeftsjahrSperre = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.geschaftsjahrsperre"),
						RechteFac.RECHT_FB_CHEFBUCHHALTER);
				menuItemGeschaeftsjahrSperre.addActionListener(this);
				menuItemGeschaeftsjahrSperre
						.setActionCommand(MENU_ACTION_GESCHAEFTSJAHR_SPERRE);
				jmBearbeiten.add(menuItemGeschaeftsjahrSperre);

				WrapperMenuItem menuItemPeriodenuebernahme = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("finanz.periodenuebernahme"),
						RechteFac.RECHT_FB_CHEFBUCHHALTER);
				menuItemPeriodenuebernahme.addActionListener(this);
				menuItemPeriodenuebernahme
						.setActionCommand(MENU_ACTION_BEARBEITEN_PERIODENUEBERNAHME);
				jmBearbeiten.add(menuItemPeriodenuebernahme);

				// Finananzamtsbuchungen
				WrapperMenuItem menuItemTagesabschluss = null;
				menuItemTagesabschluss = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.menu.finanzamtsbuchungen"),
						RechteFac.RECHT_FB_FINANZ_CUD);
				menuItemTagesabschluss.addActionListener(this);
				menuItemTagesabschluss
						.setActionCommand(MENU_ACTION_FINANZAMTSBUCHUNGEN);
				jmBearbeiten.add(menuItemTagesabschluss);

				// Menu Journal
				WrapperMenuItem menueItemKonten = null;
				menueItemKonten = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("finanz.konten"), null);
				menueItemKonten.addActionListener(this);
				if (kontotyp.equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
					menueItemKonten
							.setActionCommand(MENU_ACTION_JOURNAL_KONTEN);
				} else if (kontotyp.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
					menueItemKonten
							.setActionCommand(MENU_ACTION_JOURNAL_KONTEN_DEBI);
				} else {
					menueItemKonten
							.setActionCommand(MENU_ACTION_JOURNAL_KONTEN_KREDI);
				}
				jmJournal.add(menueItemKonten);

				WrapperMenuItem menueItemSaldenliste = null;
				menueItemSaldenliste = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.menu.saldenliste"), null);
				menueItemSaldenliste.addActionListener(this);
				menueItemSaldenliste.setActionCommand(MENU_ACTION_SALDENLISTE);
				jmJournal.add(menueItemSaldenliste);

				WrapperMenuItem menueItemBilanz = null;
				menueItemBilanz = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.menu.bilanz"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menueItemBilanz.addActionListener(this);
				menueItemBilanz.setActionCommand(MENU_ACTION_BILANZ);
				jmJournal.add(menueItemBilanz);
				WrapperMenuItem menueItemErfolgsrechnung = null;
				menueItemErfolgsrechnung = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.menu.erfolgsrechnung"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menueItemErfolgsrechnung.addActionListener(this);
				menueItemErfolgsrechnung
						.setActionCommand(MENU_ACTION_ERFOLGSRECHNUNG);
				jmJournal.add(menueItemErfolgsrechnung);

				WrapperMenuItem menuItemUVA = null;
				menuItemUVA = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.menu.uva"),
						RechteFac.RECHT_FB_FINANZ_CUD);
				menuItemUVA.addActionListener(this);
				menuItemUVA.setActionCommand(MENU_ACTION_UVA);
				jmJournal.add(menuItemUVA);

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_LIQUIDITAETSVORSCHAU)) {
					// PJ16999
					WrapperMenuItem menuItemLiqu = null;
					menuItemLiqu = new WrapperMenuItem(
							LPMain.getTextRespectUISPr("fb.report.liquiditaetsvorschau"),
							RechteFac.RECHT_FB_FINANZ_CUD);
					menuItemLiqu.addActionListener(this);
					menuItemLiqu
							.setActionCommand(MENU_ACTION_LIQUIDITAETSVORSCHAU);
					jmJournal.add(menuItemLiqu);
				}

				JMenu menuInfo = new WrapperMenu("lp.info", this);
				WrapperMenuItem menueItemKontoblaetter = null;
				menueItemKontoblaetter = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.report.kontoblaetter"),
						null);
				menueItemKontoblaetter.addActionListener(this);
				menueItemKontoblaetter
						.setActionCommand(MENU_ACTION_KONTOBLAETTER);
				menuInfo.add(menueItemKontoblaetter);
				wmbKonten.addJMenuItem(menuInfo);

				WrapperMenuItem menuItemOffenePosten = null;
				menuItemOffenePosten = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.report.offeneposten"),
						null);
				menuItemOffenePosten.addActionListener(this);
				menuItemOffenePosten.setActionCommand(MENU_ACTION_OFFENPOSTEN);
				menuInfo.add(menuItemOffenePosten);

			} else {
				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_LIQUIDITAETSVORSCHAU)) {
					// PJ16999
					WrapperMenuItem menuItemLiqu = null;
					menuItemLiqu = new WrapperMenuItem(
							LPMain.getTextRespectUISPr("fb.report.liquiditaetsvorschau"),
							RechteFac.RECHT_FB_FINANZ_CUD);
					menuItemLiqu.addActionListener(this);
					menuItemLiqu
							.setActionCommand(MENU_ACTION_LIQUIDITAETSVORSCHAU);
					jmJournal.add(menuItemLiqu);
				}

			}
			((InternalFrameFinanz) getInternalFrame()).wmbKonten = wmbKonten;
		}
		return wmbKonten;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		// if
		// (e.getActionCommand().equals(MENU_ACTION_DATEI_DRUCKEN_KONTOBLATT)) {
		// printKontoblatt();
		// } else
		String selectedGJMenuItem = geschaeftsjahrViewController
				.getSelectedGeschaeftsjahr(e.getActionCommand());
		if (null != selectedGJMenuItem) {
			if (panelQueryBuchungen != null) {
				panelQueryBuchungen.updateButtons();
				initPanelTop3QueryBuchungen();
				panelQueryBuchungen.setDefaultFilter(buildFiltersBuchungen());
				if (getSelectedComponent().equals(getPanelSplit3())) {
					setSelectedComponent(getPanelSplit3());
					getPanelSplit3().eventYouAreSelected(false);
				}
				geschaeftsjahrGesperrt = null;
				isGeschaeftsjahrGesperrt();
			}
		}

		// Map<?, ?> mapGJ = DelegateFactory.getInstance().getSystemDelegate()
		// .getAllGeschaeftsjahr();
		// for (Iterator<?> iter = mapGJ.keySet().iterator(); iter.hasNext();) {
		// Integer item = (Integer) iter.next();
		// if (e.getActionCommand().equals(
		// TabbedPaneKonten.MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG
		// + item.toString())) {
		// ((InternalFrameFinanz)
		// getInternalFrame()).setAktuellesGeschaeftsjahr(item.toString());
		// LPMain.getInstance().getTheClient().setGeschaeftsJahr(item) ;
		// if (panelQueryBuchungen != null) {
		// panelQueryBuchungen.updateButtons();
		// initPanelTop3QueryBuchungen();
		// panelQueryBuchungen
		// .setDefaultFilter(buildFiltersBuchungen());
		// if (getSelectedComponent().equals(getPanelSplit3())) {
		// setSelectedComponent(getPanelSplit3());
		// getPanelSplit3().eventYouAreSelected(false);
		// }
		// }
		// }
		// }

		if (e.getActionCommand().equals(MENU_ACTION_BEARBEITEN_IMPORTKONTEN)) {
			importKonten();
		}
		if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_KONTEN)
				|| e.getActionCommand().equals(MENU_ACTION_JOURNAL_KONTEN_DEBI)
				|| e.getActionCommand()
						.equals(MENU_ACTION_JOURNAL_KONTEN_KREDI)) {
			printKonten();
		} else if (e.getActionCommand().equals(MENU_ACTION_SALDENLISTE)) {
			printSaldenliste();
		} else if (e.getActionCommand().equals(MENU_ACTION_ERFOLGSRECHNUNG)) {
			printErfolgsrechnung();
		} else if (e.getActionCommand().equals(MENU_ACTION_BILANZ)) {
			printBilanz();
		} else if (e.getActionCommand()
				.equals(MENU_ACTION_LIQUIDITAETSVORSCHAU)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportLiquiditaetsvorschau(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("fb.report.liquiditaetsvorschau")));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_BEARBEITEN_STORNIEREN)) {
			TabbedPaneKonten tbk = (TabbedPaneKonten) getInternalFrameFinanz()
					.getSelectedTabbedPane();
			if (tbk != null) {
				PanelQuery pq = tbk.getPanelQueryBuchungen();
				if (pq != null && pq.getSelectedId() != null) {
					BuchungdetailDto bDto = DelegateFactory
							.getInstance()
							.getBuchenDelegate()
							.buchungdetailFindByPrimaryKey(
									(Integer) pq.getSelectedId());

					DelegateFactory.getInstance().getBuchenDelegate()
							.storniereBuchung(bDto.getBuchungIId());
				}
				tbk.getPanelSplit3().eventYouAreSelected(false);
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_KONTOBLAETTER)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportKontoblaetter(
									getInternalFrameFinanz(),
									kontoDto,
									LPMain.getTextRespectUISPr("fb.report.kontoblaetter")));
		} else if (e.getActionCommand().equals(MENU_ACTION_OFFENPOSTEN)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportOffenePosten(
									getInternalFrameFinanz(),
									kontoDto,
									LPMain.getTextRespectUISPr("fb.report.offeneposten")));
		} else if (e.getActionCommand().equals(MENU_ACTION_MANUELLE_BUCHUNG)) {
			// modmod: 1 hier wird das Panel fuer den Dialog an das IF
			// uebergeben
			PanelFinanzUmbuchung ub = getPanelUmbuchung();
			ub.setGeschaeftsjahr(getInternalFrameFinanz()
					.getIAktuellesGeschaeftsjahr());
			getInternalFrame().showPanelDialog(ub);
		} else if (e.getActionCommand().equals(MENU_ACTION_FINANZAMTSBUCHUNGEN)) {
			finanzamtsbuchungen();
		} else if (e.getActionCommand().equals(MENU_ACTION_SPLITTBUCHUNG)) {
			PanelFinanzSplittbuchung sb = getPanelSplittbuchung();
			sb.setGeschaeftsjahr(getInternalFrameFinanz()
					.getIAktuellesGeschaeftsjahr());
			getInternalFrame().showPanelDialog(sb);
		} else if (e.getActionCommand().equals(MENU_ACTION_UVA)) {
			printUva();
			// alle uva-menus updaten
			// updateUvaMenus(); wirkt hier nicht, da Dialog nicht modal!
		} else if (e.getActionCommand().startsWith(
				MENU_ACTION_UVA_ZURUECKNEHMEN)) {
			int i = e.getActionCommand().indexOf("|");
			i = Integer.parseInt(e.getActionCommand().substring(i + 1));
			FinanzamtDto finanzamtDto = finanzamtDtos[i];
			letzteUvaZuruecknehmen(finanzamtDto.getPartnerIId());
			// update Menutext auf aktuelle letzte Verprobung
			updateUvaMenu(i);
		} else if (e.getActionCommand().equals(
				MENU_ACTION_BEARBEITEN_BELEGUEBERNAHME)) {
			belegUebernahme();
			// TODO: 2012-06-02 Adi auskommentiert damit Version kompiliert
		} else if (e.getActionCommand().equals(
				MENU_ACTION_BEARBEITEN_PERIODENUEBERNAHME)) {
			periodenUebernahme();
		} else if (e.getActionCommand().equals(
				MENU_ACTION_GESCHAEFTSJAHR_SPERRE)) {
			String sMeldung = LPMain
					.getTextRespectUISPr("fb.frage.geschaeftsjahrsperre1");
			if (DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					sMeldung)) {
				sMeldung = LPMain
						.getTextRespectUISPr("fb.frage.geschaeftsjahrsperre2");
				if (DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						sMeldung))
					DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.sperreGeschaeftsjahr(
									getInternalFrameFinanz()
											.getIAktuellesGeschaeftsjahr());
			}
		}

	}

	protected void updateUvaMenus() throws ExceptionLP, Throwable {
		for (int i = 0; i < finanzamtDtos.length; i++) {
			updateUvaMenu(i);
		}
	}

	private void updateUvaMenu(int index) throws ExceptionLP, Throwable {
		UvaverprobungDto uvavp = DelegateFactory.getInstance()
				.getFinanzServiceDelegate()
				.letzteVerprobung(finanzamtDtos[index].getPartnerIId());
		if (uvavp == null) {
			menuItemUVAZuruecknehmenFa[index].setText(finanzamtDtos[index]
					.getPartnerDto().formatName() + " (KEINE)");
			menuItemUVAZuruecknehmenFa[index].setEnabled(false);
		} else {
			menuItemUVAZuruecknehmenFa[index]
					.setText(finanzamtDtos[index].getPartnerDto().formatName()
							+ " (" + uvavp.toInfo() + ")");
			menuItemUVAZuruecknehmenFa[index].setEnabled(true);
		}
	}

	private InternalFrameFinanz getInternalFrameFinanz() {
		return (InternalFrameFinanz) getInternalFrame();
	}

	private void importKonten() {
		File[] files = HelperClient.chooseFile(this,
				HelperClient.FILE_FILTER_CSV, false);
		if (files == null || files.length < 1)
			return;

		try {
			csvKontoImportController.setImportFile(files[0]);
			// List<ExceptionLP> errors =
			// csvKontoImportController.importCsvFile(files[0]) ;

			DialogCsvResult dlg = new DialogCsvResult(LPMain.getInstance()
					.getDesktop(), "Sachkontenimport Ergebnis", true,
					csvKontoImportController);
			dlg.setVisible(true);
		} catch (IOException ioE) {
			handleException(ioE, false);
		} catch (Throwable t) {
			handleException(t, false);
		}
	}

	private void letzteUvaZuruecknehmen(Integer finanzamtIId)
			throws ExceptionLP, Throwable {
		if (finanzamtIId == null) {
			DialogFactory.showModalDialog("UVA Verprobung",
					"Kein Finanzamt f\u00FCr Mandant definiert");
		} else {
			UvaverprobungDto uvapDto = DelegateFactory.getInstance()
					.getFinanzServiceDelegate().letzteVerprobung(finanzamtIId);
			if (uvapDto != null) {
				String sMeldung = "UVa Verprobung f\u00FCr Gesch\u00E4ftsjahr "
						+ uvapDto.getIGeschaeftsjahr();
				if (uvapDto.getIAbrechnungszeitraum() == UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_JAHR)
					sMeldung += " Monat " + uvapDto.getIMonat()
							+ " zur\u00FCcknehmen";
				else if (uvapDto.getIAbrechnungszeitraum() == UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_QUARTAL)
					sMeldung += " Quartal " + uvapDto.getIMonat()
							+ " zur\u00FCcknehmen";
				else
					sMeldung += " zur\u00FCcknehmen";

				if (DialogFactory.showModalJaNeinDialog(
						this.getInternalFrame(), sMeldung)) {
					DelegateFactory.getInstance().getFinanzServiceDelegate()
							.removeUvaverprobung(uvapDto);
				}
			} else {
				DialogFactory
						.showModalDialog("Uva Verprobung",
								"Es ist keine Verprobung f\u00FCr das gew\u00E4hlte Finanzamt vorhanden.");
			}

		}
	}

	protected void belegUebernahme() throws Throwable {
		Integer geschaeftsjahr = ((InternalFrameFinanz) getInternalFrame())
				.getIAktuellesGeschaeftsjahr();
		GeschaeftsjahrMandantDto gjDto = DelegateFactory.getInstance()
				.getSystemDelegate()
				.geschaeftsjahrFindByPrimaryKey(geschaeftsjahr);
		int[] ret = DialogFactory.showPeriodeAuswahl(gjDto);
		int periode = ret[0];
		boolean alleneu = (ret[1] == 1 ? true : false);
		if (periode > 0) {
			ArrayList<FibuFehlerDto> fehler = DelegateFactory.getInstance()
					.getFinanzServiceDelegate()
					.pruefeBelege(geschaeftsjahr, periode, false);
			if (fehler.size() == 0) {
				// nur verbuchen wenn keine Fehler festgestellt!
				DelegateFactory.getInstance().getFinanzServiceDelegate()
						.verbucheBelege(geschaeftsjahr, periode, alleneu);
				JOptionPane.showMessageDialog(this, "Belege f\u00FCr Periode "
						+ periode + " wurden verbucht.", "Info",
						JOptionPane.OK_OPTION);
			} else {
				DialogFactory
						.showBelegPruefergebnis(getInternalFrame(), fehler);
			}
		}
	}

	protected void periodenUebernahme() throws Throwable {
		TabbedPaneKonten selectedPane = (TabbedPaneKonten) getInternalFrameFinanz()
				.getSelectedTabbedPane();
		if (selectedPane == null)
			return;

		Integer gj = getInternalFrameFinanz().getIAktuellesGeschaeftsjahr();
		String gjFrom = gj == null ? "" : (new Integer(gj.intValue() - 1))
				.toString();
		String gjTo = gj == null ? "" : gj.toString();

		DialogPeriodenuebernahme d = new DialogPeriodenuebernahme(LPMain
				.getInstance().getDesktop(), LPMain.getMessageTextRespectUISPr(
				"finanz.periodenuebernahme.dialog",
				new Object[] { gjFrom, gjTo }), selectedPane.getKontoDto(), gj);
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
	}

	protected void finanzamtsbuchungen() throws Throwable {
		// TODO: periode setzen
		int periode = 1;
		Integer geschaeftsjahr = ((InternalFrameFinanz) getInternalFrame())
				.getIAktuellesGeschaeftsjahr();
		DelegateFactory.getInstance().getFinanzServiceDelegate()
				.createFinanzamtsbuchungen(geschaeftsjahr, periode);
	}

	protected void printKonten() throws Throwable {
		String sTitle = DelegateFactory.getInstance()
				.getFinanzServiceDelegate()
				.uebersetzeKontotypOptimal(this.kontotyp);
		getInternalFrame().showReportKriterien(
				new ReportKonten(getInternalFrame(), "Kontos", sTitle));
	}

	private void printSaldenliste() throws Throwable {
		getInternalFrame().showReportKriterien(
				new ReportSaldenliste(getInternalFrame(), LPMain
						.getTextRespectUISPr("fb.menu.saldenliste"),
						((InternalFrameFinanz) getInternalFrame())
								.getAktuellesGeschaeftsjahr()));
	}

	private void printErfolgsrechnung() throws Throwable {
		getInternalFrame().showReportKriterien(
				new ReportErfolgsrechnung(getInternalFrame(), LPMain
						.getTextRespectUISPr("fb.menu.erfolgsrechnung"),
						((InternalFrameFinanz) getInternalFrame())
								.getAktuellesGeschaeftsjahr()));
	}

	private void printBilanz() throws Throwable {
		getInternalFrame().showReportKriterien(
				new ReportBilanz(getInternalFrame(), LPMain
						.getTextRespectUISPr("fb.menu.bilanz"),
						((InternalFrameFinanz) getInternalFrame())
								.getAktuellesGeschaeftsjahr()));
	}

	private void printUva() throws Throwable {
		getInternalFrame().showReportKriterien(
				new ReportUva(getInternalFrame(), LPMain
						.getTextRespectUISPr("fb.menu.uva"),
						((InternalFrameFinanz) getInternalFrame())
								.getAktuellesGeschaeftsjahr(), this));
	}

	protected PanelFinanzUmbuchung getPanelUmbuchung() throws Throwable {
		if (panelUmbuchung == null) {
			panelUmbuchung = new PanelFinanzUmbuchung(getInternalFrame());
		}

		panelUmbuchung.reset();
		return panelUmbuchung;
	}

	protected PanelFinanzSplittbuchung getPanelSplittbuchung() throws Throwable {
		if (panelSplittbuchung == null) {
			panelSplittbuchung = new PanelFinanzSplittbuchung(
					getInternalFrame());
		}

		panelSplittbuchung.reset();
		return panelSplittbuchung;
	}

	public Object getInseratDto() {
		return kontoDto;
	}

}
