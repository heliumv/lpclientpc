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
package com.lp.client.partner;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedEventDrop;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelQueryFLRGoto;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.media.DropPanelSplit;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.partner.fastlanereader.generated.service.FLRLFLiefergruppePK;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.csv.LPCSVReader;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich ...</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2012/12/18 07:56:27 $
 */
public class TabbedPaneLieferant extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperMenuBar wrapperManuBar = null;

	public static int IDX_PANE_LIEFERANT = -1;
	public static int IDX_PANE_KOPFDATEN = -1;
	public static int IDX_PANE_KONDITIONEN = -1;
	public static int IDX_PANE_RECHNUNGSADRESSE = -1;
	public static int IDX_PANE_ANSPRECHPARTNER = -1;
	public static int IDX_PANE_BANK = -1;
	public static int IDX_PANE_LIEFERGRUPPEN = -1;
	public static int IDX_PANE_KURZBRIEF = -1;
	public static int IDX_PANE_LIEFERANTBEURTEILUNG = -1;
	// public static final int IDX_PANE_UMSATZSTATISTIK_QP6 = 5;
	// ich bin immer der letzte!
	public static int IDX_PANE_MONATSATATISTIK_PT_LAST = -1;
	public static int IDX_PANE_KOMMENTAR = -1;

	private String rechtModulweit = null;

	private LieferantDto lieferantDto = new LieferantDto();

	private final String MENUE_ACTION_LIEFERSTATISTIK = "MENUE_ACTION_LIEFERSTATISTIK";
	private final String MENUE_ACTION_MONATSSTATISTIK = "MENUE_ACTION_MONATSSTATISTIK";
	private final String MENUE_ACTION_UMSATZSTATISTIK = "MENUE_ACTION_UMSATZSTATISTIK";
	private final String MENUE_ACTION_ADRESSETIKETT = "MENUE_ACTION_ADRESSETIKETT";
	private final String MENUE_ACTION_LIEFERANTENZUSAMMENFUEHREN = "MENUE_ACTION_LIEFERANTENZUSAMMENFUEHREN";
	private final String MENUE_ACTION_ARTIKELDESLIEFERANTEN = "MENUE_ACTION_ARTIkeLDESLIEFERANTEN";
	private final String MENUE_ACTION_LIEFERANTENLISTE = "MENUE_ACTION_LIEFERANTENLISTE";
	private final String MENUE_ACTION_CSVIMPORT_ARTIKELLIEFERANT = "MENUE_ACTION_CSVIMPORT_ARTIKELLIEFERANT";
	private final String MENUE_ACTION_STAMMBLATT = "MENUE_ACTION_STAMMBLATT";

	private PanelBasis panelLieferantenKopfdatenD2 = null;
	private PanelLieferantKonditionen panelLieferantKonditionenD3 = null;
	private PanelLieferantRechnungsadresse panelLieferantRechnungsadresseD4 = null;

	private PanelBasis panelLieferantAnsprechpartnerSP5 = null;
	private PanelQuery panelLieferantAnsprechpartnerTopQP5 = null;
	private PanelLieferantAnsprechpartner panelLieferantAnsprechpartnerBottomD5 = null;
	private PanelQuery panelLieferantenQP1 = null;

	// das sind die extra Neu Buttons auf den QueryPanels dieses TabbedPane
	private final static String EXTRA_NEU_AUS_PARTNER = "neu_aus_partner";
	private PanelQueryFLR panelPartnerQPFLR = null;

	private PanelDialogKriterienUmsatz panelDKUebersicht;
	private PanelTabelleUmsatzstatistik pTUmsatzstatistik = null;

	private PanelDialogKriterienMonatsstatistik panelDKMonatsstatistik;
	private PanelTabelleMonatsstatistik pTMonatsstatistik = null;

	private PanelQuery panelLiefergruppeTopQP6 = null;
	private PanelSplit panelLiefergruppeSP6 = null;
	private PanelLieferantLiefergruppe panelLiefergruppeBottomD6 = null;

	private PanelQuery panelQueryKurzbrief = null;
	private PanelPartnerKurzbrief panelDetailKurzbrief = null;
	private PanelSplit panelSplitKurzbrief = null;

	private PanelQuery panelBankTopQP = null;
	private PanelBasis panelBankBottomD = null;
	private PanelSplit panelBankSP = null;

	private PanelQuery panelQueryLieferantbeurteilung = null;
	private PanelBasis panelDetailLieferantbeurteilung = null;
	private PanelSplit panelSplitLieferantbeurteilung = null;

	private PanelQuery panelKommentarTopQP = null;
	private PanelKundeLieferantKommentar panelKommentarBottomD = null;
	private PanelSplit panelKommentarSP = null;

	private static final String ACTION_SPECIAL_SCAN = "action_special_scan";
	private final String BUTTON_SCAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SCAN;
	private static final String ACTION_SPECIAL_NEW_EMAIL = "action_special_"
			+ PanelBasis.ALWAYSENABLED + "new_email_entry";

	public TabbedPaneLieferant(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"label.lieferant"));
		rechtModulweit = getInternalFrame().getRechtModulweit();
		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// hier kommt alles drauf

		// tab oben: QP1 LieferantenFLR; lazy loading
		int tabIndex = 0;
		IDX_PANE_LIEFERANT = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANE_LIEFERANT);

		// tab oben; Kopfdaten ; D2; lazy loading
		tabIndex++;
		IDX_PANE_KOPFDATEN = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				IDX_PANE_KOPFDATEN);

		// tab oben; Konditionen ; D3; lazy loading
		tabIndex++;
		IDX_PANE_KONDITIONEN = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"auft.title.panel.konditionen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"auft.title.panel.konditionen"), IDX_PANE_KONDITIONEN);

		// tab oben; Rechnungsadresse ; D4; lazy loading
		tabIndex++;
		IDX_PANE_RECHNUNGSADRESSE = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.rechnungsadresse"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr("lp.rechnungsadresse"),
				IDX_PANE_RECHNUNGSADRESSE);

		// tab oben; Splitpane Ansprechpartner
		tabIndex++;
		IDX_PANE_ANSPRECHPARTNER = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"label.ansprechpartner"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"label.ansprechpartner"), IDX_PANE_ANSPRECHPARTNER);

		// Bankverbindung
		tabIndex++;
		IDX_PANE_BANK = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"part.kund.bankverbindung"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"part.kund.bankverbindung"), IDX_PANE_BANK);

		// tab oben; Splitpane Liefergruppen; lazy loading
		tabIndex++;
		IDX_PANE_LIEFERGRUPPEN = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("part.liefergruppe"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("part.liefergruppe"),
				IDX_PANE_LIEFERGRUPPEN);

		tabIndex++;
		IDX_PANE_KURZBRIEF = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kurzbrief"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kurzbrief"),
				IDX_PANE_KURZBRIEF);
		tabIndex++;
		IDX_PANE_LIEFERANTBEURTEILUNG = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.beurteilung"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.beurteilung"),
				IDX_PANE_LIEFERANTBEURTEILUNG);
		tabIndex++;
		IDX_PANE_KOMMENTAR = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("part.kommentar"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("part.kommentar"),
				IDX_PANE_KOMMENTAR);
		// 6 tab oben; Statistik
		/** kommt bald ;-) */
		// insertTab(
		// LPMain.getInstance().getTextRespectUISPr("lp.umsatzuebersicht"),
		// null,
		// null,
		// LPMain.getInstance().getTextRespectUISPr("lp.umsatzuebersicht"),
		// IDX_PANE_UMSATZSTATISTIK_QP6);
		// defaults
		// QP1 ist default.
		setSelectedComponent(panelLieferantenQP1);
		this.refreshLieferantenQP1();

		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelLieferantenQP1.getSelectedId() == null) {
			getInternalFrame().enableAllPanelsExcept(false);
		}

		getInternalFrameLieferant().getLieferantDto().setIId(
				(Integer) panelLieferantenQP1.getSelectedId());

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelLieferantenQP1,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("label.lieferant"));
	}

	private void refreshKommentar(Integer iIdPartnerI) throws Throwable {

		if (panelKommentarTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKPartnerkommentar(iIdPartnerI, false);

			panelKommentarTopQP = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_PARTNERKOMMENTAR,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("part.kommentar"), true);

			panelKommentarBottomD = new PanelKundeLieferantKommentar(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("part.kommentar"), null, false);

			panelKommentarSP = new PanelSplit(getInternalFrame(),
					panelKommentarBottomD, panelKommentarTopQP, 160);
			setComponentAt(IDX_PANE_KOMMENTAR, panelKommentarSP);
		} else {
			// filter refreshen.
			panelKommentarTopQP
					.setDefaultFilter(PartnerFilterFactory.getInstance()
							.createFKPartnerkommentar(iIdPartnerI, false));
		}
	}

	private void refreshKurzbrief(Integer iIdPartnerI) throws Throwable {

		if (panelSplitKurzbrief == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKKurzbriefpartner(iIdPartnerI,
							LocaleFac.BELEGART_LIEFERANT);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT)) {
				aWhichStandardButtonIUse = new String[] {
						PanelBasis.ACTION_NEW, ACTION_SPECIAL_NEW_EMAIL };
			}

			panelQueryKurzbrief = new PanelQuery(null, filters,
					QueryParameters.UC_ID_PARTNERKURZBRIEF,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr("lp.kurzbrief"),
					true);

			if (aWhichStandardButtonIUse.length > 1) {
				panelQueryKurzbrief.createAndSaveAndShowButton(
						"/com/lp/client/res/documentHtml.png", LPMain
								.getInstance()
								.getTextRespectUISPr("lp.newHtml"),
						ACTION_SPECIAL_NEW_EMAIL, KeyStroke.getKeyStroke('N',
								InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK),
						null);
			}

			panelDetailKurzbrief = new PanelPartnerKurzbrief(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kurzbrief"), null);

			panelSplitKurzbrief = new DropPanelSplit(getInternalFrame(),
					panelDetailKurzbrief, panelQueryKurzbrief, 150);

			setComponentAt(IDX_PANE_KURZBRIEF, panelSplitKurzbrief);
		} else {
			// filter refreshen.
			panelQueryKurzbrief.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKKurzbriefpartner(iIdPartnerI,
							LocaleFac.BELEGART_LIEFERANT));
		}
	}

	private void refreshLieferantbeurteilung(Integer iIdLieferantI)
			throws Throwable {

		if (panelSplitLieferantbeurteilung == null) {
			String[] aWhichStandardButtonIUse = {};

			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKLieferantbeurteilung(iIdLieferantI);

			panelQueryLieferantbeurteilung = new PanelQuery(null, filters,
					QueryParameters.UC_ID_LIEFERANTBEURTEILUNG,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("lp.beurteilung"), true);

			panelDetailLieferantbeurteilung = new PanelLieferantbeurteilung(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.beurteilung"), null);

			panelSplitLieferantbeurteilung = new PanelSplit(getInternalFrame(),
					panelDetailLieferantbeurteilung,
					panelQueryLieferantbeurteilung, 150);

			setComponentAt(IDX_PANE_LIEFERANTBEURTEILUNG,
					panelSplitLieferantbeurteilung);
		} else {
			// filter refreshen.
			panelQueryLieferantbeurteilung
					.setDefaultFilter(PartnerFilterFactory.getInstance()
							.createFKLieferantbeurteilung(iIdLieferantI));
		}
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_LIEFERSTATISTIK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"lp.statistik");
			getInternalFrame().showReportKriterien(
					new ReportLieferantenstatistik(
							((InternalFrameLieferant) getInternalFrame()),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_MONATSSTATISTIK)) {
			getInternalFrame().showPanelDialog(getPanelDKMonatsstatistik());
		} else if (e.getActionCommand().equals(MENUE_ACTION_UMSATZSTATISTIK)) {
			if (getInternalFrameLieferant().getLieferantDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportLieferantumsatzstatistik(getInternalFrame(),
								getInternalFrameLieferant().getLieferantDto()
										.getPartnerDto()
										.formatFixTitelName1Name2()));
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_ARTIKELDESLIEFERANTEN)) {
			if (getInternalFrameLieferant().getLieferantDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportArtikeldesLieferanten(
								getInternalFrameLieferant(),
								getInternalFrameLieferant().getLieferantDto()
										.getPartnerDto()
										.formatFixTitelName1Name2()));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_STAMMBLATT)) {
			if (getInternalFrameLieferant().getLieferantDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportLieferantenstammblatt(
								getInternalFrameLieferant(),
								getInternalFrameLieferant().getLieferantDto()
										.getPartnerDto()
										.formatFixTitelName1Name2()));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_LIEFERANTENLISTE)) {
			if (getInternalFrameLieferant().getLieferantDto() != null) {
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"part.kunde.lieferantenliste");
				getInternalFrame().showReportKriterien(
						new ReportLieferantenliste(getInternalFrameLieferant(),
								add2Title));
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_LIEFERANTENZUSAMMENFUEHREN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"part.lieferantenzusammenfuehren");

			PanelDialogLieferantenZusammenfuehren d = new PanelDialogLieferantenZusammenfuehren(
					getLieferantDto(), getInternalFrame(), add2Title);
			getInternalFrame().showPanelDialog(d);
			d.setVisible(true);
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_CSVIMPORT_ARTIKELLIEFERANT)) {
			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_CSV, false);
			File f = null;
			if (files != null && files.length > 0) {
				f = files[0];
			}
			if (f != null) {
				ArrayList<String[]> al;
				LPCSVReader reader = new LPCSVReader(new FileReader(f), ';');
				al = (ArrayList<String[]>) reader.readAll();
				reader.close();
				if (al.size() > 0) {
					String err = DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.importiereArtikellieferant(
									getLieferantDto().getIId(), al, false,
									LPMain.getInstance().getTheClient());
					if (err.length() > 0) {
						JOptionPane
								.showMessageDialog(this, err,
										"Fehler beim Import",
										JOptionPane.ERROR_MESSAGE);
					} else {
						boolean b = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										"Keine Fehler in der Import-Datei vorhanden. Wollen Sie die Daten nun importieren?");

						if (b == true) {
							DelegateFactory
									.getInstance()
									.getLieferantDelegate()
									.importiereArtikellieferant(
											getLieferantDto().getIId(), al,
											true,
											LPMain.getInstance().getTheClient());
						}
					}
				}
			}
		}

		if (e.getActionCommand().equals(MENUE_ACTION_ADRESSETIKETT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"part.report.adressetikett");
			getInternalFrame().showReportKriterien(
					new ReportAdressetikett(getInternalFrame(),
							getInternalFrameLieferant().getLieferantDto()
									.getPartnerDto(), add2Title));
		}
	}

	public LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	public void setLieferantDto(LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}

	private void refreshBankSP4(Integer iIdPartnerI) throws Throwable {

		if (panelBankTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKPartnerbank(iIdPartnerI);

			panelBankTopQP = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_PARTNERBANK,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"finanz.bankverbindung"), true);

			panelBankBottomD = new PanelLieferantpartnerbank(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("finanz.bankverbindung"), null);

			panelBankSP = new PanelSplit(getInternalFrame(), panelBankBottomD,
					panelBankTopQP, 200);
			setComponentAt(IDX_PANE_BANK, panelBankSP);
		} else {
			// filter refreshen.
			panelBankTopQP.setDefaultFilter(PartnerFilterFactory.getInstance()
					.createFKPartnerbank(iIdPartnerI));
		}
	}

	private void refreshLieferantenQP1() throws Exception, Throwable {
		if (panelLieferantenQP1 == null) {
			// welche buttons brauchen wir?
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelLieferantenQP1 = new PanelQuery(PartnerFilterFactory
					.getInstance().createQTPLZ(), PartnerFilterFactory
					.getInstance().createFKLieferantMandantPartner(),
					QueryParameters.UC_ID_LIEFERANTEN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true,
					PartnerFilterFactory.getInstance().createFKVLieferant(),
					null); // refqp:
							// 1
							// hier
							// auf
							// refresh
							// when
							// you
							// are
							// selected
							// stellen

			// Hier den zusaetzlichen Button aufs Panel bringen
			panelLieferantenQP1.createAndSaveAndShowButton(
					"/com/lp/client/res/businessmen16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"part.tooltip.new_lieferant_from_partner"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_PARTNER, null);

			panelLieferantenQP1.befuellePanelFilterkriterienDirekt(
					PartnerFilterFactory.getInstance()
							.createFKDLieferantPartnerName(),
					PartnerFilterFactory.getInstance()
							.createFKDLieferantPartnerOrt());

			panelLieferantenQP1.addDirektFilter(PartnerFilterFactory
					.getInstance().createFKDPartnerErweiterteSuche());

			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
			boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
					.getCWertAsObject();

			if (!bSuchenInklusiveKbez) {
				panelLieferantenQP1.addDirektFilter(PartnerFilterFactory
						.getInstance()
						.createFKDLieferantPartnerKurzbezeichnung());
			}
			setComponentAt(IDX_PANE_LIEFERANT, panelLieferantenQP1);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);
		getInternalFrame().setRechtModulweit(rechtModulweit);
		int selectedCur = this.getSelectedIndex();

		if (selectedCur == IDX_PANE_LIEFERANT) {
			// gehe zu QP1.
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");
			getInternalFrame()
					.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
			refreshLieferantenQP1();
			panelLieferantenQP1.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelLieferantenQP1.updateButtons();
		}

		else if (selectedCur == IDX_PANE_KOPFDATEN) {
			// gehe zu D2.
			Integer iId = (Integer) getInternalFrameLieferant()
					.getLieferantDto().getIId();
			try {
				refreshLieferantenKopfdatenD2(iId);
				panelLieferantenKopfdatenD2.eventYouAreSelected(false);
			} catch (ExceptionLP efc) {
				if (efc != null
						&& efc.getICode() == EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING) {
					panelLieferantenQP1.eventYouAreSelected(false);
					setEnabledAt(IDX_PANE_LIEFERANT, true);
					setSelectedComponent(panelLieferantenQP1);
				}
			}
		}

		else if (selectedCur == IDX_PANE_KONDITIONEN) {
			// gehe zu D3.
			Integer iId = (Integer) getInternalFrameLieferant()
					.getLieferantDto().getIId();
			refreshLieferantenKonditionenD3(iId);
			// Info an Panel: bist selektiert worden.
			panelLieferantKonditionenD3.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANE_RECHNUNGSADRESSE) {
			// gehe zu D4.
			Integer iId = (Integer) getInternalFrameLieferant()
					.getLieferantDto().getIId();
			refreshLieferantRechnungsadresseD4(iId);
			// Info an Panel: bist selektiert worden.
			panelLieferantRechnungsadresseD4.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANE_ANSPRECHPARTNER) {
			// gehe zu SP5
			Integer iIdPartner = (Integer) DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							(Integer) getInternalFrameLieferant()
									.getLieferantDto().getIId())
					.getPartnerIId();

			refreshAnsprechpartnerSP5(iIdPartner);
			panelLieferantAnsprechpartnerSP5.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelLieferantAnsprechpartnerTopQP5
					.updateButtons(panelLieferantAnsprechpartnerBottomD5
							.getLockedstateDetailMainKey());
		} else if (selectedCur == IDX_PANE_BANK) {
			// gehe zu SP5
			Integer iIdPartner = (Integer) DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							(Integer) getInternalFrameLieferant()
									.getLieferantDto().getIId())
					.getPartnerIId();
			refreshBankSP4(iIdPartner);
			panelBankSP.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelBankTopQP.updateButtons(panelBankBottomD
					.getLockedstateDetailMainKey());
		}

		else if (selectedCur == IDX_PANE_LIEFERGRUPPEN) {
			// gehe zu SP6

			Integer iIdLieferant = getInternalFrameLieferant()
					.getLieferantDto().getIId();
			refreshLiefergruppeSP5(iIdLieferant);
			panelLiefergruppeSP6.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelLiefergruppeTopQP6.updateButtons(panelLiefergruppeBottomD6
					.getLockedstateDetailMainKey());
		} else if (selectedCur == IDX_PANE_KOMMENTAR) {

			refreshKommentar(getInternalFrameLieferant().getLieferantDto()
					.getPartnerIId());
			panelKommentarSP.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelKommentarTopQP.updateButtons(panelKommentarBottomD
					.getLockedstateDetailMainKey());
		} else if (selectedCur == IDX_PANE_KURZBRIEF) {

			boolean hatRechtKurzbriefCUD = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PART_KURZBRIEF_CUD);
			if (hatRechtKurzbriefCUD == true) {
				getInternalFrame().setRechtModulweit(
						RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			refreshKurzbrief(getInternalFrameLieferant().getLieferantDto()
					.getPartnerIId());
			panelSplitKurzbrief.eventYouAreSelected(false);
			panelQueryKurzbrief.updateButtons();
		} else if (selectedCur == IDX_PANE_LIEFERANTBEURTEILUNG) {
			refreshLieferantbeurteilung(getInternalFrameLieferant()
					.getLieferantDto().getIId());
			panelSplitLieferantbeurteilung.eventYouAreSelected(false);
			panelQueryLieferantbeurteilung.updateButtons();
		}
		/** kommt bald */
		// else if (selectedCur == IDX_PANE_UMSATZSTATISTIK_QP6) {
		// refreshUmsatzPT7();
		// // bevor man an die Uebersicht kommt, muss man die Kriterien waehlen
		// showPanelDialog(getPanelKriterienUmsatz());
		// }
		else if (selectedCur == IDX_PANE_MONATSATATISTIK_PT_LAST) {
			// gehe zu DK6 dann PT6

			getPTMonatsstatistik();

			// bevor man an die Umsaetze kommt, muss man die Kriterien waehlen
			getPanelDKMonatsstatistik();
			getInternalFrame().showPanelDialog(panelDKMonatsstatistik);
		}
	}

	private InternalFrameLieferant getInternalFrameLieferant() {
		return ((InternalFrameLieferant) getInternalFrame());
	}

	private PanelDialogKriterien getPanelDKUmsatz() throws HeadlessException,
			Throwable {

		if (panelDKUebersicht == null) {
			FilterKriterium fkKD = PartnerFilterFactory.getInstance()
					.createFKKriteriumIId(
							getInternalFrameLieferant().getLieferantDto()
									.getIId());

			panelDKUebersicht = new PanelDialogKriterienUmsatzLF(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.kriterienumsatzuebersicht"), fkKD);
		}
		return panelDKUebersicht;
	}

	private PanelDialogKriterien getPanelDKMonatsstatistik() throws Throwable {

		if (panelDKMonatsstatistik == null) {
			panelDKMonatsstatistik = new PanelDialogKriterienMonatsstatistik(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.kriterienumsatzuebersicht"));
		}
		FilterKriterium fkKD = PartnerFilterFactory.getInstance()
				.createFKKriteriumIId(
						getInternalFrameLieferant().getLieferantDto().getIId());
		panelDKMonatsstatistik.setFkKDoderLF(fkKD);

		return panelDKMonatsstatistik;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelLieferantenQP1) {
				if (panelLieferantenQP1.getSelectedId() != null) {
					Object iId = ((ISourceEvent) eI.getSource())
							.getIdSelected();
					getInternalFrameLieferant().getLieferantDto().setIId(
							(Integer) iId);
					getInternalFrame().setKeyWasForLockMe(iId + "");
					if (iId == null) {
						getInternalFrame().enableAllMyKidPanelsExceptMe(
								IDX_PANE_LIEFERANT, false);
					} else {
						getInternalFrameLieferant().setLieferantDto(
								DelegateFactory
										.getInstance()
										.getLieferantDelegate()
										.lieferantFindByPrimaryKey(
												(Integer) iId));
						getInternalFrame().enableAllMyKidPanelsExceptMe(
								IDX_PANE_LIEFERANT, true);
						setLieferantDto(DelegateFactory.getInstance()
								.getLieferantDelegate()
								.lieferantFindByPrimaryKey((Integer) iId));
					}

					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANE_LIEFERANT, true);

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANE_LIEFERANT, false);
				}
			} else if (eI.getSource() == panelLieferantAnsprechpartnerTopQP5) {
				// hole key
				Integer iIdAnsprechpartner = (Integer) panelLieferantAnsprechpartnerTopQP5
						.getSelectedId();
				panelLieferantAnsprechpartnerBottomD5
						.setKeyWhenDetailPanel(iIdAnsprechpartner);
				panelLieferantAnsprechpartnerBottomD5
						.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelLieferantAnsprechpartnerTopQP5.updateButtons();

				getInternalFrameLieferant().setLieferantDto(
						DelegateFactory
								.getInstance()
								.getLieferantDelegate()
								.lieferantFindByPrimaryKey(
										getInternalFrameLieferant()
												.getLieferantDto().getIId()));
			} else if (eI.getSource() == panelBankTopQP) {
				// hole key
				Integer iIdAnsprechpartner = (Integer) panelBankTopQP
						.getSelectedId();
				panelBankBottomD.setKeyWhenDetailPanel(iIdAnsprechpartner);
				panelBankBottomD.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelBankTopQP.updateButtons();

				getInternalFrameLieferant().setLieferantDto(
						DelegateFactory
								.getInstance()
								.getLieferantDelegate()
								.lieferantFindByPrimaryKey(
										getInternalFrameLieferant()
												.getLieferantDto().getIId()));
			} else if (eI.getSource() == panelLiefergruppeTopQP6) {
				FLRLFLiefergruppePK pk = (FLRLFLiefergruppePK) panelLiefergruppeTopQP6
						.getSelectedId();
				panelLiefergruppeBottomD6.setKeyWhenDetailPanel(pk);
				panelLiefergruppeBottomD6.eventYouAreSelected(false);

				// btnstate: 1 im QP die Buttons in den Zustand nolocking/save
				// setzen.
				panelLiefergruppeTopQP6.updateButtons();
			} else if (eI.getSource() == panelQueryKurzbrief) {

				Integer iId = (Integer) panelQueryKurzbrief.getSelectedId();
				panelDetailKurzbrief.setKeyWhenDetailPanel(iId);
				panelDetailKurzbrief.eventYouAreSelected(false);
				panelQueryKurzbrief.updateButtons();
			} else if (eI.getSource() == panelQueryLieferantbeurteilung) {

				Integer iId = (Integer) panelQueryLieferantbeurteilung
						.getSelectedId();
				panelDetailLieferantbeurteilung.setKeyWhenDetailPanel(iId);
				panelDetailLieferantbeurteilung.eventYouAreSelected(false);
				panelQueryLieferantbeurteilung.updateButtons();
			} else if (eI.getSource() == panelKommentarTopQP) {
				Object key = panelKommentarTopQP.getSelectedId();
				panelKommentarBottomD.setKeyWhenDetailPanel(key);
				panelKommentarBottomD.eventYouAreSelected(false);
				panelKommentarTopQP.updateButtons();
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelLieferantAnsprechpartnerBottomD5) {
				// im QP die Buttons in den Zustand neu setzen.
				panelLieferantAnsprechpartnerTopQP5
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelLiefergruppeBottomD6) {
				// btnstate: 2 im QP die Buttons in den Zustand neu setzen.
				panelLiefergruppeTopQP6.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBankBottomD) {
				// btnstate: 2 im QP die Buttons in den Zustand neu setzen.
				panelBankTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelDetailKurzbrief) {
				panelQueryKurzbrief.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				panelDetailKurzbrief.beEditMode(true);
			} else if (eI.getSource() == panelDetailLieferantbeurteilung) {
				panelQueryLieferantbeurteilung
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));

			} else if (eI.getSource() instanceof PanelQueryFLRGoto) {
				Integer iIdLieferant = (Integer) ((ISourceEvent) eI.getSource())
						.getIdSelected();
				this.setSelectedIndex(1);
				panelLieferantenQP1.setSelectedId(iIdLieferant);
				refreshLieferantenKopfdatenD2(iIdLieferant);
				panelLieferantenKopfdatenD2.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKommentarBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelKommentarTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelLieferantenKopfdatenD2) {
				panelLieferantenKopfdatenD2.eventYouAreSelected(false);
			} else if (eI.getSource() == panelLieferantKonditionenD3) {
				panelLieferantKonditionenD3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelLieferantRechnungsadresseD4) {
				panelLieferantRechnungsadresseD4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelLieferantAnsprechpartnerBottomD5) {
				panelLieferantAnsprechpartnerSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelLiefergruppeBottomD6) {
				panelLiefergruppeSP6.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKurzbrief) {
				panelSplitKurzbrief.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailLieferantbeurteilung) {
				panelSplitLieferantbeurteilung.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBankBottomD) {
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKommentarBottomD) {
				panelKommentarSP.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelLieferantAnsprechpartnerBottomD5) {
				Object oKey = panelLieferantAnsprechpartnerBottomD5
						.getKeyWhenDetailPanel();
				panelLieferantAnsprechpartnerTopQP5.eventYouAreSelected(false);
				panelLieferantAnsprechpartnerTopQP5.setSelectedId(oKey);
				panelLieferantAnsprechpartnerSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelLiefergruppeBottomD6) {
				Object oKey = panelLiefergruppeBottomD6.getKeyWhenDetailPanel();
				panelLiefergruppeTopQP6.eventYouAreSelected(false);
				panelLiefergruppeTopQP6.setSelectedId(oKey);
				panelLiefergruppeSP6.eventYouAreSelected(false);
			} else if (eI.getSource() == panelLiefergruppeBottomD6) {
				Object oKey = panelBankBottomD.getKeyWhenDetailPanel();
				panelBankTopQP.eventYouAreSelected(false);
				panelBankTopQP.setSelectedId(oKey);
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelLieferantenKopfdatenD2) {
				// MB 04.05.06 IMS 1676
				panelLieferantenQP1.clearDirektFilter();
				if (panelLieferantenQP1.getSelectedId() == null
						|| // wenns der
							// erste ist
							// ... oder:
						(panelLieferantenQP1.getSelectedId() != null && !(panelLieferantenQP1
								.getSelectedId(). // wenn ein falscher
													// selektiert ist
								equals(panelLieferantenKopfdatenD2
										.getKeyWhenDetailPanel())))) {
					Object key = panelLieferantenKopfdatenD2
							.getKeyWhenDetailPanel();
					panelLieferantenQP1.eventYouAreSelected(false);
					panelLieferantenQP1.setSelectedId(key);
					panelLieferantenQP1.eventYouAreSelected(false);
				}
			} else if (eI.getSource() == panelDetailKurzbrief) {
				Object oKey = panelDetailKurzbrief.getKeyWhenDetailPanel();
				panelQueryKurzbrief.eventYouAreSelected(false);
				panelQueryKurzbrief.setSelectedId(oKey);
				panelSplitKurzbrief.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailLieferantbeurteilung) {
				Object oKey = panelDetailLieferantbeurteilung
						.getKeyWhenDetailPanel();
				panelQueryLieferantbeurteilung.eventYouAreSelected(false);
				panelQueryLieferantbeurteilung.setSelectedId(oKey);
				panelSplitLieferantbeurteilung.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBankBottomD) {
				Object oKey = panelBankBottomD.getKeyWhenDetailPanel();
				panelBankTopQP.eventYouAreSelected(false);
				panelBankTopQP.setSelectedId(oKey);
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKommentarBottomD) {
				Object oKey = panelKommentarBottomD.getKeyWhenDetailPanel();
				panelKommentarTopQP.eventYouAreSelected(false);
				panelKommentarTopQP.setSelectedId(oKey);
				panelKommentarSP.eventYouAreSelected(false);
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelLieferantenKopfdatenD2) {
				// ... QP1 nach selektieren.
				panelLieferantenQP1.eventYouAreSelected(false);
				getInternalFrameLieferant().getLieferantDto().setIId(
						(Integer) panelLieferantenQP1.getSelectedId());
				getInternalFrame().setKeyWasForLockMe(
						panelLieferantenQP1.getSelectedId() + "");
				if (panelLieferantenQP1.getSelectedId() == null) {
					// leer: die restlichen panels deaktivieren
					// damit enableAllPanelsExcept den richtigen enabled!
					setSelectedComponent(panelLieferantenQP1);
					getInternalFrame().enableAllPanelsExcept(false);
				}
				setSelectedComponent(panelLieferantenQP1);
			} else if (eI.getSource() == panelLieferantRechnungsadresseD4) {
				refreshLieferantRechnungsadresseD4((Integer) panelLieferantenQP1
						.getSelectedId());
				panelLieferantRechnungsadresseD4
						.setKeyWhenDetailPanel(panelLieferantenQP1
								.getSelectedId());
				getInternalFrameLieferant().getLieferantDto().setIId(
						(Integer) panelLieferantenQP1.getSelectedId());
				panelLieferantRechnungsadresseD4.eventYouAreSelected(false);
				setSelectedComponent(panelLieferantRechnungsadresseD4);
			} else if (eI.getSource() == panelLieferantAnsprechpartnerBottomD5) {
				Integer iIdPartner = (Integer) DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								(Integer) getInternalFrameLieferant()
										.getLieferantDto().getIId())
						.getPartnerIId();
				if (panelLieferantAnsprechpartnerBottomD5
						.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelLieferantAnsprechpartnerTopQP5
							.getId2SelectAfterDelete();
					panelLieferantAnsprechpartnerTopQP5
							.setSelectedId(oNaechster);
				}
				refreshAnsprechpartnerSP5(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				panelLieferantAnsprechpartnerSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBankBottomD) {
				Integer iIdPartner = (Integer) DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								(Integer) getInternalFrameLieferant()
										.getLieferantDto().getIId())
						.getPartnerIId();
				if (panelBankBottomD.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelBankTopQP
							.getId2SelectAfterDelete();
					panelBankTopQP.setSelectedId(oNaechster);
				}
				refreshAnsprechpartnerSP5(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelLiefergruppeBottomD6) {
				if (panelLiefergruppeBottomD6.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelLiefergruppeTopQP6
							.getId2SelectAfterDelete();
					panelLiefergruppeTopQP6.setSelectedId(oNaechster);
				}
				Integer iIdLieferant = (Integer) panelLieferantenQP1
						.getSelectedId();
				refreshLiefergruppeSP5(iIdLieferant);
				getInternalFrame().setKeyWasForLockMe(iIdLieferant + "");
				panelLiefergruppeSP6.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKurzbrief) {
				if (panelDetailKurzbrief.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKurzbrief
							.getId2SelectAfterDelete();
					panelQueryKurzbrief.setSelectedId(oNaechster);
				}
				Integer iIdLieferant = (Integer) panelLieferantenQP1
						.getSelectedId();
				LieferantDto liefDto = DelegateFactory.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(iIdLieferant);
				refreshKurzbrief(liefDto.getPartnerIId());
				getInternalFrame().setKeyWasForLockMe(iIdLieferant + "");
				panelSplitKurzbrief.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailLieferantbeurteilung) {
				if (panelDetailLieferantbeurteilung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLieferantbeurteilung
							.getId2SelectAfterDelete();
					panelQueryLieferantbeurteilung.setSelectedId(oNaechster);
				}
				Integer iIdLieferant = (Integer) panelLieferantenQP1
						.getSelectedId();

				refreshLieferantbeurteilung(iIdLieferant);
				getInternalFrame().setKeyWasForLockMe(iIdLieferant + "");
				panelSplitLieferantbeurteilung.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKommentarBottomD) {
				// ...SP4 refreshen; zb. nach save, loeschen.
				Object key = panelKommentarTopQP.getSelectedId();
				refreshKommentar(getInternalFrameLieferant().getLieferantDto()
						.getPartnerIId());
				getInternalFrame().setKeyWasForLockMe(key + "");
				panelKommentarSP.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelLieferantenQP1) {
				Integer key = (Integer) getInternalFrameLieferant()
						.getLieferantDto().getIId();
				// wenn noch nicht erzeugt, dann jetzt erzeugen; wegen is lazy.
				refreshLieferantenKopfdatenD2(key);
				// jetzt ab zu D2.
				setSelectedComponent(panelLieferantenKopfdatenD2);
			} else if (eI.getSource() == panelPartnerQPFLR) {
				// Neu aus Partner.
				Integer iIdPartner = (Integer) panelPartnerQPFLR
						.getSelectedId();
				refreshLieferantenKopfdatenD2(null);
				getInternalFrameLieferant().setLieferantDto(new LieferantDto());
				getInternalFrameLieferant().getLieferantDto().setPartnerIId(
						iIdPartner);
				panelLieferantenKopfdatenD2.eventActionNew(eI, true, false);

				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey(iIdPartner);

				setSelectedComponent(panelLieferantenKopfdatenD2);
				if (partnerDto.getLandplzortDto() != null) {
					PanelLieferantkopfdaten panellieferantkopfdaten = (PanelLieferantkopfdaten) panelLieferantenKopfdatenD2;
					panellieferantkopfdaten.setDefaultMWSTforLand(partnerDto
							.getLandplzortDto().getLandDto());
				}
			}
		}

		else if ((eI.getID() == ItemChangedEvent.ACTION_NEW)
				|| (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW)) {

			if (eI.getSource() == panelLieferantenQP1) {
				// im QP1 auf new gedrueckt.

				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels aktivieren
				if (panelLieferantenQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}

				if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
					refreshLieferantenKopfdatenD2(null);
					// jetzt in's richtige panel mit new.
					getInternalFrameLieferant().setLieferantDto(
							new LieferantDto());
					panelLieferantenKopfdatenD2.eventActionNew(eI, true, false);
					// auch ui maessig, fuehrt zu eventYouAreSelected
					setSelectedComponent(panelLieferantenKopfdatenD2);
				} else {
					// Neu aus Partner.
					dialogPartner(eI);
				}
			} else if (eI.getSource() == panelLieferantAnsprechpartnerTopQP5) {
				// im QP5 auf new gedrueckt.
				// jetzt in's richtige panel mit new.
				panelLieferantAnsprechpartnerBottomD5.eventActionNew(eI, true,
						false);
				// jetzt eventYouAreSelected ausloesen wegen naechster zeile.
				panelLieferantAnsprechpartnerBottomD5
						.eventYouAreSelected(false);
				// auch ui maessig, fuehrt NICHT zu eventYouAreSelected
				setSelectedComponent(panelLieferantAnsprechpartnerSP5);
			} else if (eI.getSource() == panelBankTopQP) {
				// im QP5 auf new gedrueckt.
				// jetzt in's richtige panel mit new.
				panelBankBottomD.eventActionNew(eI, true, false);
				// jetzt eventYouAreSelected ausloesen wegen naechster zeile.
				panelBankBottomD.eventYouAreSelected(false);
				// auch ui maessig, fuehrt NICHT zu eventYouAreSelected
				setSelectedComponent(panelBankSP);
			} else if (eI.getSource() == panelLiefergruppeTopQP6) {
				panelLiefergruppeBottomD6.eventActionNew(eI, true, false);
				panelLiefergruppeBottomD6.eventYouAreSelected(false);
				setSelectedComponent(panelLiefergruppeSP6);
			} else if (eI.getSource() == panelQueryKurzbrief) {
				panelDetailKurzbrief.eventActionNew(eI, true, false);
				panelDetailKurzbrief.eventYouAreSelected(false);
				setSelectedComponent(panelSplitKurzbrief);
			} else if (eI.getSource() == panelQueryLieferantbeurteilung) {
				panelDetailLieferantbeurteilung.eventActionNew(eI, true, false);
				panelDetailLieferantbeurteilung.eventYouAreSelected(false);
				setSelectedComponent(panelSplitLieferantbeurteilung);
			} else if (eI.getSource() == panelKommentarTopQP) {
				panelKommentarBottomD.eventActionNew(eI, true, false);
				panelKommentarBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelKommentarSP);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (eI.getSource() == getPanelDKUmsatz()) {
				/** kommt bald */
				// // die Kriterien fuer PanelTabelle abholen
				// FilterKriterium[] aAlleKriterien = getPanelKriterienUmsatz().
				// getAlleFilterKriterien();
				//
				// // die Kriterien dem PanelTabelle setzen
				// refreshUmsatzPT7().setDefaultFilter(aAlleKriterien);
				//
				// // die Uebersicht aktualisieren redundant, wenn man dann
				// ohnehin wechselt
				// setComponentAt(IDX_PANE_UMSATZSTATISTIK_QP6,
				// panelMonatsstatistik);
				// refreshUmsatzPT7().eventYouAreSelected(false);
				//
				// // man steht auf alle Faelle auf der Uebersicht
				// //setSelectedComponent(getPanelUebersicht());
			} else if (eI.getSource() == getPanelDKMonatsstatistik()) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getPanelDKMonatsstatistik()
						.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				getPTMonatsstatistik().setDefaultFilter(aAlleKriterien);

				try {
					getComponentAt(IDX_PANE_MONATSATATISTIK_PT_LAST);
				} catch (Exception ex) {

					IDX_PANE_MONATSATATISTIK_PT_LAST = getTabCount();

					insertTab(
							LPMain.getInstance().getTextRespectUISPr(
									"lp.statistik.monate"),
							null,
							getPTMonatsstatistik(),
							LPMain.getInstance().getTextRespectUISPr(
									"lp.statistik.monate"),
							IDX_PANE_MONATSATATISTIK_PT_LAST);
				}
				setSelectedComponent(getPTMonatsstatistik());
				getPTMonatsstatistik().eventYouAreSelected(false);
				getPTMonatsstatistik().updateButtons(
						new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));
				getInternalFrame().closePanelDialog();
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			// Einer der Knoepfe zur Reihung der Positionen auf einem PanelQuery
			// wurde gedrueckt
			if (eI.getSource() == panelLieferantAnsprechpartnerTopQP5) {
				int iPos = panelLieferantAnsprechpartnerTopQP5.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelLieferantAnsprechpartnerTopQP5
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelLieferantAnsprechpartnerTopQP5
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getAnsprechpartnerDelegate()
							.vertauscheAnsprechpartner(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelLieferantAnsprechpartnerTopQP5
							.setSelectedId(iIdPosition);
					panelLieferantAnsprechpartnerSP5.eventYouAreSelected(true);
				}
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelLieferantAnsprechpartnerTopQP5) {
				int iPos = panelLieferantAnsprechpartnerTopQP5.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelLieferantAnsprechpartnerTopQP5.getTable()
						.getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelLieferantAnsprechpartnerTopQP5
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelLieferantAnsprechpartnerTopQP5
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getAnsprechpartnerDelegate()
							.vertauscheAnsprechpartner(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelLieferantAnsprechpartnerTopQP5
							.setSelectedId(iIdPosition);
					panelLieferantAnsprechpartnerSP5.eventYouAreSelected(true);
				}
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelLieferantAnsprechpartnerTopQP5) {
				panelLieferantAnsprechpartnerBottomD5.eventActionNew(eI, true,
						false);
				panelLieferantAnsprechpartnerBottomD5
						.eventYouAreSelected(false); // Buttons schalten
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			if (eI.getSource() instanceof PanelQuery) {
				PanelQuery pq = (PanelQuery) eI.getSource();
				if (pq.getAspect().equals(ACTION_SPECIAL_NEW_EMAIL)) {
					// panelDetailKurzbrief.beHtml();
					panelDetailKurzbrief.eventActionNew(eI, true, false);
					panelDetailKurzbrief.eventYouAreSelected(false);
					setSelectedComponent(panelSplitKurzbrief);
					panelDetailKurzbrief.beHtml();
					panelDetailKurzbrief.beEditMode(true);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DROP) {
			myLogger.info("Drop Changed on " + eI.getSource());
			if (eI.getSource() == panelDetailKurzbrief) {
				if (eI instanceof ItemChangedEventDrop) {
					ItemChangedEventDrop dropEvent = (ItemChangedEventDrop) eI;
					Integer kbId = DelegateFactory
							.getInstance()
							.getEmailMediaDelegate()
							.createKurzbriefFromEmail(
									getLieferantDto().getPartnerDto().getIId(),
									LocaleFac.BELEGART_LIEFERANT,
									(MediaEmailMetaDto) dropEvent.getDropData());
					panelQueryKurzbrief.setSelectedId(kbId);
					panelSplitKurzbrief.eventYouAreSelected(false);
				}
			}
		}
	}

	private void refreshLieferantenKopfdatenD2(Object key) throws Throwable {
		if (panelLieferantenKopfdatenD2 == null) {
			panelLieferantenKopfdatenD2 = new PanelLieferantkopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kopfdaten"), key);
			setComponentAt(IDX_PANE_KOPFDATEN, panelLieferantenKopfdatenD2);
		}
	}

	private void refreshLieferantenKonditionenD3(Object key) throws Throwable {
		if (panelLieferantKonditionenD3 == null) {
			panelLieferantKonditionenD3 = new PanelLieferantKonditionen(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"auft.title.panel.konditionen"), key);
			setComponentAt(IDX_PANE_KONDITIONEN, panelLieferantKonditionenD3);
		}
	}

	private void refreshLieferantRechnungsadresseD4(Object key)
			throws Throwable {
		if (panelLieferantRechnungsadresseD4 == null) {
			panelLieferantRechnungsadresseD4 = new PanelLieferantRechnungsadresse(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.rechnungsadresse"), key);
			setComponentAt(IDX_PANE_RECHNUNGSADRESSE,
					panelLieferantRechnungsadresseD4);
		}
	}

	private void refreshAnsprechpartnerSP5(Integer iIdPartnerI)
			throws Throwable {

		QueryType[] querytypes = null;

		if (panelLieferantAnsprechpartnerSP5 == null) {
			// die zusaetzlichen Buttons am PanelQuery anbringen
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN };

			panelLieferantAnsprechpartnerTopQP5 = new PanelQuery(querytypes,
					PartnerFilterFactory.getInstance().createFKAnsprechpartner(
							iIdPartnerI),
					QueryParameters.UC_ID_ANSPRECHPARTNER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.ansprechpartner"), true);

			panelLieferantAnsprechpartnerBottomD5 = new PanelLieferantAnsprechpartner(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.ansprechpartner"), null);
			panelLieferantAnsprechpartnerTopQP5
					.befuellePanelFilterkriterienDirektUndVersteckte(
							PartnerFilterFactory.getInstance()
									.createFKDAnsprechpartnerPartnerName(),
							null, PartnerFilterFactory.getInstance()
									.createFKVAnsprechpartner());
			panelLieferantAnsprechpartnerSP5 = new PanelSplit(
					getInternalFrame(), panelLieferantAnsprechpartnerBottomD5,
					panelLieferantAnsprechpartnerTopQP5, 150);
			setComponentAt(IDX_PANE_ANSPRECHPARTNER,
					panelLieferantAnsprechpartnerSP5);
		} else {
			// filter refreshen.
			panelLieferantAnsprechpartnerTopQP5
					.setDefaultFilter(PartnerFilterFactory.getInstance()
							.createFKAnsprechpartner(iIdPartnerI));
		}
	}

	private void dialogPartner(ItemChangedEvent e) throws Throwable {

		panelPartnerQPFLR = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame());
		DialogQuery dialogQueryPartner = new DialogQuery(panelPartnerQPFLR);
	}

	private PanelTabelleMonatsstatistik getPTMonatsstatistik() throws Throwable {

		if (pTMonatsstatistik == null) {
			pTMonatsstatistik = new PanelTabelleMonatsstatistik(
					QueryParameters.UC_ID_MONATSSTATISTIK_LIEFERANT, LPMain
							.getInstance().getTextRespectUISPr(
									"lp.statistik.monate"), getInternalFrame());
		}
		LieferantDto lDto = DelegateFactory
				.getInstance()
				.getLieferantDelegate()
				.lieferantFindByPrimaryKey(
						(Integer) panelLieferantenQP1.getSelectedId());
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				lDto.getPartnerDto().formatTitelAnrede());
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		return pTMonatsstatistik;
	}

	public PanelBasis getPanelLieferantenKopfdatenD2() {
		return panelLieferantenKopfdatenD2;
	}

	public PanelQuery getPanelLieferantenQP1() {
		return panelLieferantenQP1;
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {
			wrapperManuBar = new WrapperMenuBar(this);
			JMenu menuDatei = (JMenu) wrapperManuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);
			menuDatei.add(new JSeparator(), 0);
			JMenu menuInfo = new WrapperMenu("lp.info", this);
			wrapperManuBar.addJMenuItem(menuInfo);

			JMenuItem menuItemLieferstatistik = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("lp.statistik"));
			menuItemLieferstatistik.addActionListener(this);
			menuItemLieferstatistik
					.setActionCommand(MENUE_ACTION_LIEFERSTATISTIK);
			menuInfo.add(menuItemLieferstatistik);

			boolean bHatGFs = false;
			try {
				bHatGFs = (DelegateFactory.getInstance().getSystemDelegate()
						.getAllGeschaeftsjahr() != null && !DelegateFactory
						.getInstance().getSystemDelegate()
						.getAllGeschaeftsjahr().isEmpty());
				int i = 0;
			} catch (Throwable t) {
				// nothing here
			}

			JMenuItem menuItemMonatestatistik = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("lp.statistik.monate"));
			menuItemMonatestatistik.setEnabled(bHatGFs);
			menuItemMonatestatistik.addActionListener(this);
			menuItemMonatestatistik
					.setActionCommand(MENUE_ACTION_MONATSSTATISTIK);
			menuInfo.add(menuItemMonatestatistik);

			JMenuItem menuItemUmsatzstatistik = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"part.kund.umsatzstatistik"));
			menuItemUmsatzstatistik.addActionListener(this);
			menuItemUmsatzstatistik
					.setActionCommand(MENUE_ACTION_UMSATZSTATISTIK);
			JMenu journal = (JMenu) wrapperManuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			journal.add(menuItemUmsatzstatistik);

			JMenuItem menuItemLieferantenliste = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"part.kunde.lieferantenliste"));
			menuItemLieferantenliste.addActionListener(this);
			menuItemLieferantenliste
					.setActionCommand(MENUE_ACTION_LIEFERANTENLISTE);
			journal.add(menuItemLieferantenliste);

			JMenuItem menuItemadressetikett = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"part.report.adressetikett"));
			menuItemadressetikett.addActionListener(this);
			menuItemadressetikett.setActionCommand(MENUE_ACTION_ADRESSETIKETT);
			menuInfo.add(menuItemadressetikett);

			JMenuItem menuItemaArtikeldeslieferanten = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"lieferant.report.artikeldeslieferanten"));
			menuItemaArtikeldeslieferanten.addActionListener(this);
			menuItemaArtikeldeslieferanten
					.setActionCommand(MENUE_ACTION_ARTIKELDESLIEFERANTEN);
			menuInfo.add(menuItemaArtikeldeslieferanten);

			JMenuItem menuItemStammblatt = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("lp.stammblatt"));
			menuItemStammblatt.addActionListener(this);
			menuItemStammblatt.setActionCommand(MENUE_ACTION_STAMMBLATT);
			menuInfo.add(menuItemStammblatt);

			/* Lieferanten zusammenfuehren */
			boolean bDarfLieferantenZusammenfuehren = false;
			try {
				bDarfLieferantenZusammenfuehren = DelegateFactory
						.getInstance()
						.getTheJudgeDelegate()
						.hatRecht(
								RechteFac.RECHT_PART_LIEFERANT_ZUSAMMENFUEHREN_ERLAUBT);
			} catch (Throwable ex) {
				handleException(ex, true);
			}
			if (bDarfLieferantenZusammenfuehren) {
				JMenuItem menuItemLieferantenZusammenfuehren = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr(
								"part.lieferantenzusammenfuehren"));
				menuItemLieferantenZusammenfuehren.addActionListener(this);
				menuItemLieferantenZusammenfuehren
						.setActionCommand(MENUE_ACTION_LIEFERANTENZUSAMMENFUEHREN);
				HelperClient.setToolTipTextMitRechtToComponent(
						menuItemLieferantenZusammenfuehren,
						RechteFac.RECHT_PART_LIEFERANT_ZUSAMMENFUEHREN_ERLAUBT);
				menuDatei.add(menuItemLieferantenZusammenfuehren, 0);
			}

			/* CSV Import Artikellieferanten */
			JMenuItem menuItemCsvImportArtikellieferant = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"part.csvimportartikellieferant"));
			menuItemCsvImportArtikellieferant.addActionListener(this);
			menuItemCsvImportArtikellieferant
					.setActionCommand(MENUE_ACTION_CSVIMPORT_ARTIKELLIEFERANT);
			/*
			 * HelperClient.setToolTipTextMitRechtToComponent(
			 * menuItemCsvImportArtikellieferant,
			 * RechteFac.RECHT_PART_LIEFERANT_ZUSAMMENFUEHREN_ERLAUBT);
			 */
			menuDatei.add(menuItemCsvImportArtikellieferant, 0);

		}
		return wrapperManuBar;
	}

	private PanelTabelle refreshUmsatzPT7() throws Throwable {

		if (pTUmsatzstatistik == null) {

			LieferantDto lieferantDto = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							(Integer) panelLieferantenQP1.getSelectedId());

			pTUmsatzstatistik = new PanelTabelleUmsatzstatistik(
					QueryParameters.UC_ID_KUNDE_UMSATZSTATISTIK, LPMain
							.getInstance().getTextRespectUISPr(
									"lp.umsatzuebersicht"), getInternalFrame(),
					lieferantDto.getPartnerDto().formatAnrede());
		}

		return pTUmsatzstatistik;
	}

	/**
	 * gotoAuswahl
	 */
	protected void gotoAuswahl() {
		setSelectedIndex(IDX_PANE_LIEFERANT);

		getInternalFrame().closePanelDialog();
	}

	private void refreshLiefergruppeSP5(Integer iIdLieferantI) throws Throwable {

		if (panelLiefergruppeTopQP6 == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = null;

			panelLiefergruppeTopQP6 = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_LFLIEFERGRUPPEN,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"part.liefergruppe"), true);

			panelLiefergruppeBottomD6 = new PanelLieferantLiefergruppe(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("part.liefergruppe"), null);

			panelLiefergruppeSP6 = new PanelSplit(getInternalFrame(),
					panelLiefergruppeBottomD6, panelLiefergruppeTopQP6, 200);
			setComponentAt(IDX_PANE_LIEFERGRUPPEN, panelLiefergruppeSP6);
		}
		// filter refreshen.
		panelLiefergruppeTopQP6.setDefaultFilter(PartnerFilterFactory
				.getInstance().createFKLieferant(iIdLieferantI));
	}

	public void setPanelLieferantenKopfdatenD2(
			PanelBasis panelLieferantenKopfdatenD2) {
		this.panelLieferantenKopfdatenD2 = panelLieferantenKopfdatenD2;
	}
}
