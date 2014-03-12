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
package com.lp.client.artikel;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.anfrage.ReportAnfragestatistik;
import com.lp.client.angebot.ReportAngebotsstatistik;
import com.lp.client.auftrag.ReportRahmenauftragReservierungsliste;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
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
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelImportDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.csv.LPCSVReader;

@SuppressWarnings("static-access")
/*
 * <p>Ueberschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.70 $
 */
public class TabbedPaneArtikel extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryArtikel = null;
	private PanelQuery panelQueryArtikellieferant = null;
	private PanelQuery panelQueryStaffelpreise = null;

	private PanelQuery panelQueryArtikellager = null;
	private PanelBasis panelSplitArtikellager = null;
	private PanelBasis panelDetailArtikellager = null;

	private PanelQuery panelQueryLagerplatz = null;
	private PanelBasis panelDetailArtikel = null;
	private PanelBasis panelDetailArtikelbestelldaten = null;
	private PanelBasis panelDetailArtikeltechnik = null;
	private PanelBasis panelDetailArtikelsonstiges = null;
	private PanelBasis panelDetailExterneDokumente = null;
	private PanelBasis panelDetailArtikellieferant = null;
	private PanelBasis panelDetailLagerplatz = null;
	private PanelArtikellieferantstaffelpreise panelDetailStaffelpeise = null;
	private PanelBasis panelSplitArtikellieferant = null;

	private PanelQuery panelQueryKatalog = null;
	private PanelBasis panelDetailKatalog = null;
	private PanelBasis panelSplitKatalog = null;

	private PanelBasis panelSplitStaffelpreise = null;
	private PanelBasis panelSplitLagerplatz = null;
	private PanelBasis panelDetailArtikelpreise = null;

	private PanelBasis panelSplitVkpfStaffelmenge = null;
	private PanelQuery panelQueryVkpfStaffelmenge = null;
	private PanelVkpfStaffelmenge panelDetailVkpfStaffelmenge = null;

	private PanelQuery panelQueryArtikelkommentar = null;
	private PanelBasis panelSplitArtikelkommentar = null;
	private PanelBasis panelDetailArtikelkommentar = null;

	private PanelQuery panelQuerySperren = null;
	private PanelBasis panelDetailSperren = null;
	private PanelBasis panelSplitSperren = null;

	private PanelQuery panelQueryShopgruppe = null;
	private PanelBasis panelDetailShopgruppe = null;
	private PanelBasis panelSplitShopgruppe = null;

	private PanelQuery panelQueryZugehoerige = null;
	private PanelBasis panelDetailZugehoerige = null;
	private PanelBasis panelSplitZugehoerige = null;

	private PanelQuery panelQueryEinkaufsean = null;
	private PanelBasis panelDetailEinkaufsean = null;
	private PanelBasis panelSplitEinkaufsean = null;

	private PanelBasis panelDetailArtikeleigenschaft = null;

	private PanelQueryFLR panelArtikel = null;

	private PanelQueryFLR panelQueryFLRPaternoster = null;

	private final String MENUE_ACTION_STATISTIK = "MENUE_ACTION_STATISTIK";
	private final String MENUE_ACTION_RESERVIERUNGEN = "MENUE_ACTION_RESERVIERUNGEN";

	private final String MENUE_ACTION_FEHLMENGEN = "MENUE_ACTION_FEHLMENGEN";
	private final String MENUE_ACTION_VKPREISENTWICKLUNG = "MENUE_ACTION_VKPREISENTWICKLUNG";

	private final String MENUE_ACTION_BESTELLTLISTE = "MENUE_ACTION_BESTELLTLISTE";
	private final String MENUE_ACTION_ANFRAGESTATISTIK = "MENUE_ACTION_ANFRAGESTATISTIK";
	private final String MENUE_ACTION_ANGEBOTSSTATISTIK = "NENUE_ACTION_ANGEBOTSSTATISTIK";
	private final String MENUE_ACTION_RAHMENRESERVIERUNGEN = "MENUE_ACTION_RAHMENRESERVIERUNGEN";
	private final String MENUE_ACTION_DETAILBEDARFE = "MENUE_ACTION_DETAILBEDARFE";
	private final String MENUE_ACTION_VERWENDUNGSNACHWEIS = "MENUE_ACTION_VERWENDUNGSNACHWEIS";

	private final String MENUE_ACTION_ETIKETT = "MENUE_ACTION_ETIKETT";
	private final String MENUE_ACTION_STAMMBLATT = "MENUE_ACTION_STAMMBLATT";
	private final String MENUE_ACTION_LOSSTATUS = "MENUE_ACTION_LOSSTATUS";
	private final String MENUE_ACTION_FREIINFERTIGUNG = "MENUE_ACTION_FREIINFERTIGUNG";
	private final String MENUE_ACTION_BEWEGUNSVORSCHAU = "MENUE_ACTION_BEWEGUNSVORSCHAU";
	private final String MENUE_ACTION_AUFTRAGSSERIENNUMMERN = "MENUE_ACTION_AUFTRAGSSERIENNUMMERN";
	private final String MENUE_ACTION_KUNDENSOKOS = "MENUE_ACTION_KUNDENSOKOS";

	private final String MENUE_JOURNAL_ACTION_LAGERSTANDSLISTE = "MENUE_JOURNAL_ACTION_LAGERSTANDSLISTE";
	private final String MENUE_JOURNAL_ACTION_SERIENNUMMERN = "MENUE_JOURNAL_ACTION_SERIENNUMMERN";
	private final String MENUE_JOURNAL_ACTION_LADENHUETER = "MENUE_JOURNAL_ACTION_LADENHUETER";
	private final String MENUE_JOURNAL_ACTION_HITLISTE = "MENUE_JOURNAL_ACTION_HITLISTE";
	private final String MENUE_JOURNAL_ACTION_MINDESTHALTBARKEIT = "MENUE_JOURNAL_ACTION_MINDESTHALTBARKEIT";
	private final String MENUE_JOURNAL_ACTION_PREISLISTE = "MENUE_JOURNAL_ACTION_PREISLISTE";
	private final String MENUE_JOURNAL_ACTION_WARENBENEGUNSJOURNAL = "MENUE_JOURNAL_ACTION_WARENBENEGUNSJOURNAL";
	private final String MENUE_JOURNAL_ACTION_REKLAMATIONEN = "MENUE_JOURNAL_ACTION_REKLAMATIONEN";
	private final String MENUE_JOURNAL_ACTION_GESTPREISUEBERMINVK = "MENUE_JOURNAL_ACTION_GESTPREISUEBERMINVK";
	private final String MENUE_JOURNAL_ACTION_WARENENTNAHMESTATISTIK = "MENUE_JOURNAL_ACTION_WARENENTNAHMESTATISTIK";
	private final String MENUE_JOURNAL_ACTION_MINDESTLAGERSTAENDE = "MENUE_JOURNAL_ACTION_MINDESTLAGERSTAENDE";
	private final String MENUE_JOURNAL_ACTION_ARTIKELGRUPPEN = "MENUE_JOURNAL_ACTION_ARTIKELGRUPPEN";
	private final String MENUE_JOURNAL_ACTION_SHOPGRUPPEN = "MENUE_JOURNAL_ACTION_SHOPGRUPPEN";
	private final String MENUE_JOURNAL_ACTION_NAECHSTE_WARTUNGEN = "MENUE_JOURNAL_ACTION_NAECHSTE_WARTUNGEN";

	private final String MENUE_ACTION_RAHMENBESTELLTLISTE = "MENUE_JOURNAL_ACTION_RAHMENBESTELLTLISTE";
	private final String MENUE_ACTION_AENDERUNGEN = "MENUE_ACTION_AENDERUNGEN";

	private final String MENUE_ACTION_CSVIMPORT = "MENUE_ACTION_CSVIMPORT";
	private final String MENUE_ACTION_PREISPFLEGE_EXPORT = "MENUE_ACTION_PREISPFLEGE_EXPORT";
	private final String MENUE_ACTION_PREISPFLEGE_IMPORT = "MENUE_ACTION_PREISPFLEGE_IMPORT";

	private final String MENUE_JOURNAL_ACTION_OFFENERAHMENBEDARFE = "MENUE_JOURNAL_ACTION_OFFENERAHMENBEDARFE";

	private final String MENUE_PFLEGE_VKPREISE = "MENUE_PFLEGE_VKPREISE";
	private final String MENUE_PFLEGE_PATERNOSTER = "MENUE_PFLEGE_PATERNOSTER";
	private final String MENUE_PFLEGE_AENDERE_SNRCHNR = "MENUE_PFLEGE_AENDERE_SNRCHNR";

	private final String EXTRA_NEU_AUS_ARTIKEL = "neu_aus_artikel";
	private final String EXTRA_LAGERORT_BEARBEITEN = "EXTRA_LAGERORT_BEARBEITEN";

	public static int IDX_PANEL_AUSWAHL = -1;
	public int IDX_PANEL_DETAIL = -1;
	public int IDX_PANEL_PREISE = -1;
	public int IDX_PANEL_VKPFSTAFFELMENGE = -1;
	public int IDX_PANEL_LIEFERANT = -1;
	public int IDX_PANEL_EK_STAFFELPREISE = -1;
	public int IDX_PANEL_LAGER = -1;
	public int IDX_PANEL_LAGERPLATZ = -1;
	public int IDX_PANEL_TECHNIK = -1;
	public int IDX_PANEL_BESTELLDATEN = -1;
	public int IDX_PANEL_SONSTIGES = -1;
	public int IDX_PANEL_ARTIKELKOMMENTAR = -1;
	public int IDX_PANEL_KATALOG = -1;
	public int IDX_PANEL_SPERREN = -1;
	public int IDX_PANEL_SHOPGRUPPE = -1;
	public int IDX_PANEL_ZUGEHOERIGE = -1;
	public int IDX_PANEL_ARTIKELEIGENSCHAFTEN = -1;
	public int IDX_PANEL_EINKAUFSEAN = -1;
	public int IDX_PANEL_EXTERNEDOKUMENTE = -1;
	// usemenubar 2: Variable deklarieren, wegen lazy loading
	private WrapperMenuBar wrapperManuBar = null;
	boolean bKurzbezeichnungStattVerpackungsart = false;

	public PanelQuery getPanelQueryArtikel() {
		return panelQueryArtikel;
	}

	public TabbedPaneArtikel(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.artikel"));
		jbInit();

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_STATUSZEILE_KBEZ_STATT_VERPACKUNGSART);
		bKurzbezeichnungStattVerpackungsart = (java.lang.Boolean) parameter
				.getCWertAsObject();

		initComponents();
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailArtikel == null) {
			panelDetailArtikel = new PanelArtikel(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailArtikel);
		}
	}

	private void createPreise(Integer key) throws Throwable {
		if (panelDetailArtikelpreise == null) {
			panelDetailArtikelpreise = new PanelVkpfPreise(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.panel.preise"), key);
			setComponentAt(IDX_PANEL_PREISE, panelDetailArtikelpreise);
		}
	}

	private void createBestelldaten(Integer key) throws Throwable {
		if (panelDetailArtikelbestelldaten == null) {
			panelDetailArtikelbestelldaten = new PanelArtikelbestelldaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.panel.bestelldaten"), key);
			setComponentAt(IDX_PANEL_BESTELLDATEN,
					panelDetailArtikelbestelldaten);
		}
	}

	private void createSonstiges(Integer key) throws Throwable {
		if (panelDetailArtikelsonstiges == null) {
			panelDetailArtikelsonstiges = new PanelArtikelsonstiges(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.sonstiges"), key);
			setComponentAt(IDX_PANEL_SONSTIGES, panelDetailArtikelsonstiges);
		}
	}

	private void createExterneDokumente(Integer key) throws Throwable {

		panelDetailExterneDokumente = new PanelExterneDokumente(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.externedokumente"), key);
		setComponentAt(IDX_PANEL_EXTERNEDOKUMENTE, panelDetailExterneDokumente);

	}

	private void createTechnik(Integer key) throws Throwable {
		if (panelDetailArtikeltechnik == null) {
			panelDetailArtikeltechnik = new PanelArtikeltechnik(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.technik"), key);
			setComponentAt(IDX_PANEL_TECHNIK, panelDetailArtikeltechnik);
		}
	}

	private void createEigenschaften(Integer key) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

		panelDetailArtikeleigenschaft = new PanelDynamisch(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.eigenschaften"),
				panelQueryArtikel, PanelFac.PANEL_ARTIKELEIGENSCHAFTEN,
				HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse);
		setComponentAt(IDX_PANEL_ARTIKELEIGENSCHAFTEN,
				panelDetailArtikeleigenschaft);

	}

	private void createAuswahl() throws Throwable {
		if (panelQueryArtikel == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = null;

			QueryType[] qtAuswahl = ArtikelFilterFactory.getInstance()
					.createQTArtikelauswahl();

			boolean bBenutzerIstInMandantensprechAngemeldet = false;
			if (LPMain
					.getInstance()
					.getTheClient()
					.getLocMandantAsString()
					.equals(LPMain.getInstance().getTheClient()
							.getLocUiAsString())) {
				bBenutzerIstInMandantensprechAngemeldet = true;
			}

			if (bBenutzerIstInMandantensprechAngemeldet) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW,
						PanelBasis.ACTION_FILTER };
			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
			}

			panelQueryArtikel = new PanelQuery(qtAuswahl, ArtikelFilterFactory
					.getInstance().createFKArtikelliste(),
					QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true,
					ArtikelFilterFactory.getInstance().createFKVArtikel());
			if (bBenutzerIstInMandantensprechAngemeldet) {
				// Hier den zusaetzlichen Button aufs Panel bringen
				panelQueryArtikel.createAndSaveAndShowButton(
						"/com/lp/client/res/nut_and_bolt16x16.png",
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.artikel_kopieren"),
						PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_ARTIKEL,
						null);
			}

			ArbeitsplatzparameterDto aparameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_LAGERPLATZ_DIREKT_ERFASSEN);

			if (aparameter != null) {

				panelQueryArtikel.createAndSaveAndShowButton(
						"/com/lp/client/res/table_sql.png",
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.lagerortbearbeiten")
								+ " F12", PanelBasis.ACTION_MY_OWN_NEW
								+ EXTRA_LAGERORT_BEARBEITEN,
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12,
								0), null);

			}

			panelQueryArtikel.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDArtikelnummer(
							getInternalFrame()), ArtikelFilterFactory
							.getInstance().createFKDVolltextsuche());

			panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDLieferantennrBezeichnung());

			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER);
			boolean bDirektfilterAGAKStattReferenznummer = (java.lang.Boolean) parameter
					.getCWertAsObject();

			if (bDirektfilterAGAKStattReferenznummer) {
				panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
						.getInstance().createFKDAKAG());
			} else {
				panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
						.getInstance().createFKDReferenznr());
			}

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_SI_WERT)) {

				SortierKriterium krit = new SortierKriterium("aspr.c_siwert",
						true, "ASC");

				panelQueryArtikel
						.setzeErstesUebersteuerbaresSortierkriterium(
								LPMain.getTextRespectUISPr("artikel.auswahl.sortbysiwert"),
								krit);
			}
			panelQueryArtikel.setFilterComboBox(DelegateFactory.getInstance()
					.getArtikelDelegate().getAllSprArtgru(),
					new FilterKriterium("ag.i_id", true, "" + "",
							FilterKriterium.OPERATOR_EQUAL, false), false,
					LPMain.getTextRespectUISPr("lp.alle"));

			panelQueryArtikel.addStatusBar();

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryArtikel);
		}
	}

	private void createArtikellieferant(Integer key) throws Throwable {

		if (panelQueryArtikellieferant == null) {
			String[] aWhichButtonIUse = null;

			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF)) {

				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW,
						PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
						PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
						PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
						PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT };

			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_PREVIOUS,
						PanelBasis.ACTION_NEXT };
			}

			QueryType[] querytypes = null;
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKArtikellieferant(key);

			panelQueryArtikellieferant = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_ARTIKELLIEFERANT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.panel.artikellieferant"),
					true);

			panelDetailArtikellieferant = new PanelArtikellieferant(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.panel.artikellieferant"),
					null);

			panelSplitArtikellieferant = new PanelSplit(getInternalFrame(),
					panelDetailArtikellieferant, panelQueryArtikellieferant,
					160);
			setComponentAt(IDX_PANEL_LIEFERANT, panelSplitArtikellieferant);
		} else {
			// filter refreshen.
			panelQueryArtikellieferant.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKArtikellieferant(key));
		}
	}

	private void createVkpfStaffelmenge(Integer key) throws Throwable {
		if (panelQueryVkpfStaffelmenge == null) {

			String[] aWhichButtonIUse = null;
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF)) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW };

			} else {
				aWhichButtonIUse = new String[] {};
			}

			QueryType[] querytypes = null;
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKVkpfStaffelmenge(key);

			panelQueryVkpfStaffelmenge = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_VKPFSTAFFELMENGE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.panel.vkpfstaffelmenge"),
					true);

			panelDetailVkpfStaffelmenge = new PanelVkpfStaffelmenge(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.panel.vkpfstaffelmenge"),
					null);

			panelSplitVkpfStaffelmenge = new PanelSplit(getInternalFrame(),
					panelDetailVkpfStaffelmenge, panelQueryVkpfStaffelmenge,
					180);

			setComponentAt(IDX_PANEL_VKPFSTAFFELMENGE,
					panelSplitVkpfStaffelmenge);
		} else {
			// filter refreshen.
			panelQueryVkpfStaffelmenge.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKVkpfStaffelmenge(key));
		}
	}

	private void createArtikelkommentar(Integer key) throws Throwable {

		if (panelQueryArtikelkommentar == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKArtikelkommentar(key);

			panelQueryArtikelkommentar = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_ARTIKELKOMMENTAR, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kommentar"), true);

			panelDetailArtikelkommentar = new PanelArtikelkommentar(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kommentar"), null);

			panelSplitArtikelkommentar = new PanelSplit(getInternalFrame(),
					panelDetailArtikelkommentar, panelQueryArtikelkommentar,
					180);
			setComponentAt(IDX_PANEL_ARTIKELKOMMENTAR,
					panelSplitArtikelkommentar);
		} else {
			// filter refreshen.
			panelQueryArtikelkommentar.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKArtikelkommentar(key));
		}
	}

	private void createStaffelpreise(Integer key) throws Throwable {
		if (panelQueryStaffelpreise == null) {
			String[] aWhichStandardButtonIUse = null;

			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF)) {

				aWhichStandardButtonIUse = new String[] {
						PanelBasis.ACTION_NEW,
						PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
						PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
						PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN };

			} else {
				aWhichStandardButtonIUse = new String[] {};
			}

			panelQueryStaffelpreise = new PanelQuery(null, ArtikelFilterFactory
					.getInstance().createFKArtikellieferantstaffel(key),
					QueryParameters.UC_ID_ARTIKELLIEFERANTSTAFFEL,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"artikel.title.panel.staffelpreise"), true);

			panelDetailStaffelpeise = new PanelArtikellieferantstaffelpreise(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.panel.staffelpreise"), null);
			if (panelQueryArtikellieferant == null) {
				createArtikellieferant(key);
				panelQueryArtikellieferant.eventYouAreSelected(false);
			}

			if (panelQueryArtikellieferant.getSelectedId() != null) {
				panelDetailStaffelpeise.artikellieferantDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikellieferantFindByPrimaryKey(
								(Integer) panelQueryArtikellieferant
										.getSelectedId());
			} else {
				panelDetailStaffelpeise.artikellieferantDto = null;
			}

			panelSplitStaffelpreise = new PanelSplit(getInternalFrame(),
					panelDetailStaffelpeise, panelQueryStaffelpreise, 200);

			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
				setComponentAt(IDX_PANEL_EK_STAFFELPREISE,
						panelSplitStaffelpreise);
			}
		} else {
			// filter refreshen.
			if (key != null) {
				panelQueryStaffelpreise.setDefaultFilter(ArtikelFilterFactory
						.getInstance().createFKArtikellieferantstaffel(key));
			} else {
				panelQueryStaffelpreise.setDefaultFilter(ArtikelFilterFactory
						.getInstance()
						.createFKArtikellieferantstaffelWennNULL());
			}

		}
	}

	private void createLagerplatz(Integer key) throws Throwable {

		if (panelQueryLagerplatz == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, };

			panelQueryLagerplatz = new PanelQuery(null, ArtikelFilterFactory
					.getInstance().createFKArtikellagerplaetze(key),
					QueryParameters.UC_ID_ARTIKELLAGERPLAETZE,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"artikel.title.panel.lagerplatz"), true);

			panelDetailLagerplatz = new PanelArtikellagerplaetze(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.panel.lagerplatz"), null);

			panelSplitLagerplatz = new PanelSplit(getInternalFrame(),
					panelDetailLagerplatz, panelQueryLagerplatz, 320);
			setComponentAt(IDX_PANEL_LAGERPLATZ, panelSplitLagerplatz);
		} else {
			// filter refreshen.
			panelQueryLagerplatz.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKArtikellagerplaetze(key));
		}
	}

	private void createKatalog(Integer key) throws Throwable {

		if (panelQueryKatalog == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKKatalog(key);

			panelQueryKatalog = new PanelQuery(null, filters,
					QueryParameters.UC_ID_KATALOG, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.katalog"), true);

			panelDetailKatalog = new PanelKatalogseite(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.katalog"),
					null);

			panelSplitKatalog = new PanelSplit(getInternalFrame(),
					panelDetailKatalog, panelQueryKatalog, 400);
			setComponentAt(IDX_PANEL_KATALOG, panelSplitKatalog);
		} else {
			// filter refreshen.
			panelQueryKatalog.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKKatalog(key));
		}
	}

	private void createEinkaufsean(Integer key) throws Throwable {

		if (panelQueryEinkaufsean == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKEinkaufsean(key);

			panelQueryEinkaufsean = new PanelQuery(null, filters,
					QueryParameters.UC_ID_EINKAUFSEAN,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"artikel.einkaufsean"), true);

			panelDetailEinkaufsean = new PanelEinkaufsean(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.einkaufsean"), null);

			panelSplitEinkaufsean = new PanelSplit(getInternalFrame(),
					panelDetailEinkaufsean, panelQueryEinkaufsean, 350);
			setComponentAt(IDX_PANEL_EINKAUFSEAN, panelSplitEinkaufsean);
		} else {
			// filter refreshen.
			panelQueryEinkaufsean.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKEinkaufsean(key));
		}
	}

	private void createSperren(Integer key) throws Throwable {

		if (panelQuerySperren == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKArtikelsperren(key);

			panelQuerySperren = new PanelQuery(null, filters,
					QueryParameters.UC_ID_ARTIKELSPERREN,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr("lp.sperren"),
					true);

			panelDetailSperren = new PanelArtikelsperren(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.sperren"),
					null);

			panelSplitSperren = new PanelSplit(getInternalFrame(),
					panelDetailSperren, panelQuerySperren, 360);
			setComponentAt(IDX_PANEL_SPERREN, panelSplitSperren);
		} else {
			// filter refreshen.
			panelQuerySperren.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKArtikelsperren(key));
		}
	}

	private void createShopgruppe(Integer key) throws Throwable {

		if (panelQueryShopgruppe == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKArtikelshopgruppen(key);

			panelQueryShopgruppe = new PanelQuery(null, filters,
					QueryParameters.UC_ID_ARTIEKLSHOPGRUPPE,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"artikel.tab.shopgruppen"), true);

			panelDetailShopgruppe = new PanelArtikelshopgruppe(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.tab.shopgruppen"),
					null);

			panelSplitShopgruppe = new PanelSplit(getInternalFrame(),
					panelDetailShopgruppe, panelQueryShopgruppe, 360);
			setComponentAt(IDX_PANEL_SHOPGRUPPE, panelSplitShopgruppe);
		} else {
			// filter refreshen.
			panelQueryShopgruppe.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKArtikelshopgruppen(key));
		}
	}

	private void createZugehoerige(Integer key) throws Throwable {

		if (panelQueryZugehoerige == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKZugehoerige(key);

			panelQueryZugehoerige = new PanelQuery(null, filters,
					QueryParameters.UC_ID_ZUGEHOERIGE,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("lp.zugehoerige"), true);

			panelDetailZugehoerige = new PanelZugehoerige(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.zugehoerige"),
					null);

			panelSplitZugehoerige = new PanelSplit(getInternalFrame(),
					panelDetailZugehoerige, panelQueryZugehoerige, 380);
			setComponentAt(IDX_PANEL_ZUGEHOERIGE, panelSplitZugehoerige);
		} else {
			// filter refreshen.
			panelQueryZugehoerige.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKZugehoerige(key));
		}
	}

	private void createArtikellager(Integer key) throws Throwable {

		if (panelQueryArtikellager == null) {

			boolean bDarfGestpreiseaendern = false;
			try {
				bDarfGestpreiseaendern = DelegateFactory.getInstance()
						.getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_WW_ARTIKEL_GESTPREISE_CU);
			} catch (Throwable ex) {
				handleException(ex, true);
			}
			String[] aWhichButtonIUse = null;
			if (bDarfGestpreiseaendern == true) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW };
			} else {
				aWhichButtonIUse = new String[] {};
			}

			panelQueryArtikellager = new PanelQuery(null, ArtikelFilterFactory
					.getInstance().createFKArtikellager(key),
					QueryParameters.UC_ID_ARTIKELLAGER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.lager"), true);

			panelQueryArtikellager
					.befuellePanelFilterkriterienDirektUndVersteckte(null,
							null, ArtikelFilterFactory.getInstance()
									.createFKVLager());

			panelDetailArtikellager = new PanelArtikellager(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("label.lager"),
					null);

			panelSplitArtikellager = new PanelSplit(getInternalFrame(),
					panelDetailArtikellager, panelQueryArtikellager, 200);
			setComponentAt(IDX_PANEL_LAGER, panelSplitArtikellager);

		} else {
			// filter refreshen.
			panelQueryArtikellager.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKArtikellager(key));
		}
	}

	private void jbInit() throws Throwable {
		int tabIndex = 0;
		IDX_PANEL_AUSWAHL = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		tabIndex++;
		IDX_PANEL_DETAIL = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_DETAIL);

		tabIndex++;

		// Darf Preise sehen: Tab nicht anzeigen, wenn kein Benutzerrecht:
		// LP_DARF_PREISE_SEHEN_VERKAUF
		if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
			IDX_PANEL_PREISE = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.panel.preise"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tooltip.preise"), IDX_PANEL_PREISE);

			tabIndex++;
		}

		// Darf Preise sehen: Tab nicht anzeigen, wenn kein Benutzerrecht:
		// LP_DARF_PREISE_SEHEN_VERKAUF
		if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
			IDX_PANEL_VKPFSTAFFELMENGE = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.panel.vkpfstaffelmenge"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.panel.vkpfstaffelmenge"),
					IDX_PANEL_VKPFSTAFFELMENGE);

			tabIndex++;
		}

		IDX_PANEL_LIEFERANT = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.artikellieferant"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.artikellieferant"),
				IDX_PANEL_LIEFERANT);

		tabIndex++;

		// Darf Preise sehen: Tab nicht anzeigen, wenn kein Benutzerrecht:
		// LP_DARF_PREISE_SEHEN_EINKAUF
		if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			IDX_PANEL_EK_STAFFELPREISE = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.panel.staffelpreise"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.panel.staffelpreise"),
					IDX_PANEL_EK_STAFFELPREISE);
			tabIndex++;
		}
		IDX_PANEL_LAGER = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("label.lager"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("label.lager"),
				IDX_PANEL_LAGER);

		tabIndex++;
		IDX_PANEL_LAGERPLATZ = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.lagerplatz"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.lagerplatz"), IDX_PANEL_LAGERPLATZ);

		tabIndex++;
		IDX_PANEL_TECHNIK = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.technik"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.technik"),
				IDX_PANEL_TECHNIK);

		tabIndex++;
		IDX_PANEL_BESTELLDATEN = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.bestelldaten"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.bestelldaten"),
				IDX_PANEL_BESTELLDATEN);

		tabIndex++;
		IDX_PANEL_SONSTIGES = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.sonstiges"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.sonstiges"),
				IDX_PANEL_SONSTIGES);

		tabIndex++;
		IDX_PANEL_ARTIKELKOMMENTAR = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kommentar"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kommentar"),
				IDX_PANEL_ARTIKELKOMMENTAR);

		tabIndex++;
		IDX_PANEL_KATALOG = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.katalog"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.katalog"),
				IDX_PANEL_KATALOG);

		tabIndex++;
		IDX_PANEL_SPERREN = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.sperren"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.sperren"),
				IDX_PANEL_SPERREN);
		tabIndex++;
		IDX_PANEL_SHOPGRUPPE = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.tab.webshopgruppen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.tab.webshopgruppen"), IDX_PANEL_SHOPGRUPPE);
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZUSAMMENGEHOERIGE_ARTIKEL)) {
			tabIndex++;
			IDX_PANEL_ZUGEHOERIGE = tabIndex;
			insertTab(LPMain.getInstance()
					.getTextRespectUISPr("lp.zugehoerige"), null, null, LPMain
					.getInstance().getTextRespectUISPr("lp.zugehoerige"),
					IDX_PANEL_ZUGEHOERIGE);
		}

		// Wenn keine Panelbeschriebung vorhanden, dann ausblenden
		if (DelegateFactory
				.getInstance()
				.getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN)) {
			tabIndex++;
			IDX_PANEL_ARTIKELEIGENSCHAFTEN = tabIndex;
			insertTab(
					LPMain.getInstance()
							.getTextRespectUISPr("lp.eigenschaften"), null,
					null,
					LPMain.getInstance()
							.getTextRespectUISPr("lp.eigenschaften"),
					IDX_PANEL_ARTIKELEIGENSCHAFTEN);
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUECHE)) {

			tabIndex++;
			IDX_PANEL_EINKAUFSEAN = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.einkaufsean"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.einkaufsean"), IDX_PANEL_EINKAUFSEAN);
		}
		DokumentenlinkDto[] dtosDoku = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(
						LocaleFac.BELEGART_ARTIKEL, true);
		if (dtosDoku != null && dtosDoku.length > 0) {

			tabIndex++;
			IDX_PANEL_EXTERNEDOKUMENTE = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"lp.externedokumente"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"lp.externedokumente"), IDX_PANEL_EXTERNEDOKUMENTE);
		}

		// QP1 ist default.
		createAuswahl();

		if ((Integer) panelQueryArtikel.getSelectedId() != null) {
			getInternalFrameArtikel()
					.setArtikelDto(
							DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikelFindByPrimaryKey(
											(Integer) panelQueryArtikel
													.getSelectedId()));
		}
		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren

		if (panelQueryArtikel.getSelectedId() == null) {
			getInternalFrame().enableAllMyKidPanelsExceptMe(0, false);
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryArtikel,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);
	}

	public InternalFrameArtikel getInternalFrameArtikel() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryArtikel.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		// AD: System.out.println(e.toString() + ", ID:" + e.getID() + ", " +
		// e.getSource().toString());
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryArtikel) {
				Integer angebotIId = (Integer) panelQueryArtikel
						.getSelectedId();
				if (angebotIId != null) {
					setSelectedComponent(panelDetailArtikel);
				}

			} else if (e.getSource() == panelQueryFLRPaternoster) {
				Integer paternosterIId = (Integer) panelQueryFLRPaternoster
						.getSelectedId();

				DelegateFactory.getInstance().getAutoPaternosterDelegate()
						.paternosterAddArtikelAll(paternosterIId);

			} else if (e.getSource() == panelArtikel) {
				// Neu aus Artikel.
				Integer iIdArtikel = (Integer) panelArtikel.getSelectedId();

				DialogArtikelkopieren d = new DialogArtikelkopieren(
						DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(iIdArtikel),
						getInternalFrameArtikel());
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
				if (d.getArtikelnummerNeu() != null) {
					Object[] o = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.kopiereArtikel(iIdArtikel,
									d.getArtikelnummerNeu(),
									d.getZuKopierendeFelder(),
									d.getHerstellerIIdNeu());

					Integer artikelId_Neu = (Integer) o[0];

					panelQueryArtikel.setSelectedId(artikelId_Neu);
					panelQueryArtikel.eventYouAreSelected(false);

					boolean bAndereSprachenKopiert = (Boolean) o[1];
					if (bAndereSprachenKopiert == true) {

						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.info"),
								LPMain.getInstance().getTextRespectUISPr(
										"artikel.info.mehreresprachenkopiert"));
					}

				}
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD
				|| e.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (e.getSource() == panelDetailArtikellieferant) {
				panelSplitArtikellieferant.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKatalog) {
				panelSplitKatalog.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSperren) {
				panelSplitSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailZugehoerige) {
				panelSplitZugehoerige.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailStaffelpeise) {
				panelSplitStaffelpreise.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailLagerplatz) {
				panelSplitLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikelpreise) {
				panelDetailArtikelpreise.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikelkommentar) {
				panelDetailArtikelkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailVkpfStaffelmenge) {
				panelSplitVkpfStaffelmenge.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikellager) {
				panelSplitArtikellager.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailEinkaufsean) {
				panelSplitEinkaufsean.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailShopgruppe) {
				panelSplitShopgruppe.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailArtikellieferant) {
				panelQueryArtikellieferant.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailKatalog) {
				panelQueryKatalog.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailSperren) {
				panelQuerySperren.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailZugehoerige) {
				panelQueryZugehoerige.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailLagerplatz) {
				panelQueryLagerplatz.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailStaffelpeise) {
				panelQueryStaffelpreise.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailArtikelkommentar) {
				panelQueryArtikelkommentar.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailVkpfStaffelmenge) {
				panelQueryVkpfStaffelmenge.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailArtikellager) {
				panelQueryArtikellager.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailEinkaufsean) {
				panelQueryEinkaufsean.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailShopgruppe) {
				panelQueryShopgruppe.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailArtikel) {
				panelQueryArtikel.clearDirektFilter();
				Object oKey = panelDetailArtikel.getKeyWhenDetailPanel();

				panelQueryArtikel.setSelectedId(oKey);
			}

			if (e.getSource() == panelDetailArtikellieferant) {
				Object oKey = panelDetailArtikellieferant
						.getKeyWhenDetailPanel();
				panelQueryArtikellieferant.eventYouAreSelected(false);
				panelQueryArtikellieferant.setSelectedId(oKey);
				panelSplitArtikellieferant.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKatalog) {
				Object oKey = panelDetailKatalog.getKeyWhenDetailPanel();
				panelQueryKatalog.eventYouAreSelected(false);
				panelQueryKatalog.setSelectedId(oKey);
				panelSplitKatalog.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailEinkaufsean) {
				Object oKey = panelDetailEinkaufsean.getKeyWhenDetailPanel();
				panelQueryEinkaufsean.eventYouAreSelected(false);
				panelQueryEinkaufsean.setSelectedId(oKey);
				panelSplitEinkaufsean.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailSperren) {
				Object oKey = panelDetailSperren.getKeyWhenDetailPanel();
				panelQuerySperren.eventYouAreSelected(false);
				panelQuerySperren.setSelectedId(oKey);
				panelSplitSperren.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailShopgruppe) {
				Object oKey = panelDetailShopgruppe.getKeyWhenDetailPanel();
				panelQueryShopgruppe.eventYouAreSelected(false);
				panelQueryShopgruppe.setSelectedId(oKey);
				panelSplitShopgruppe.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailZugehoerige) {
				Object oKey = panelDetailZugehoerige.getKeyWhenDetailPanel();
				panelQueryZugehoerige.eventYouAreSelected(false);
				panelQueryZugehoerige.setSelectedId(oKey);
				panelSplitZugehoerige.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailStaffelpeise) {
				Object oKey = panelDetailStaffelpeise.getKeyWhenDetailPanel();
				panelQueryStaffelpreise.eventYouAreSelected(false);
				panelQueryStaffelpreise.setSelectedId(oKey);
				panelSplitStaffelpreise.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailLagerplatz) {
				Object oKey = panelDetailLagerplatz.getKeyWhenDetailPanel();
				panelQueryLagerplatz.eventYouAreSelected(false);
				panelQueryLagerplatz.setSelectedId(oKey);
				panelSplitLagerplatz.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailArtikelkommentar) {
				Object oKey = panelDetailArtikelkommentar
						.getKeyWhenDetailPanel();
				panelQueryArtikelkommentar.eventYouAreSelected(false);
				panelQueryArtikelkommentar.setSelectedId(oKey);
				panelSplitArtikelkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikellager) {
				Object oKey = panelDetailArtikellager.getKeyWhenDetailPanel();
				panelQueryArtikellager.eventYouAreSelected(false);
				panelQueryArtikellager.setSelectedId(oKey);
				panelSplitArtikellager.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailVkpfStaffelmenge) {
				Object oKey = panelDetailVkpfStaffelmenge
						.getKeyWhenDetailPanel();
				panelQueryVkpfStaffelmenge.eventYouAreSelected(false);
				panelQueryVkpfStaffelmenge.setSelectedId(oKey);
				panelSplitVkpfStaffelmenge.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryArtikel) {
				if (panelQueryArtikel.getSelectedId() != null) {
					getInternalFrameArtikel().setKeyWasForLockMe(
							panelQueryArtikel.getSelectedId() + "");
					createDetail((Integer) panelQueryArtikel.getSelectedId());
					panelDetailArtikel.setKeyWhenDetailPanel(panelQueryArtikel
							.getSelectedId());

					// Dto-setzen
					getInternalFrameArtikel().setArtikelDto(
							DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikelFindByPrimaryKey(
											(Integer) panelQueryArtikel
													.getSelectedId()));

					// createArtikellieferant((Integer) panelQueryArtikel
					// .getSelectedId());
					// TODO: ghp, sollte doch eigentlich panelQueryArtikel sein?
					// panelQueryArtikellieferant.eventYouAreSelected(false);
					// panelQueryArtikel.eventYouAreSelected(false);
					panelQueryArtikel.updateLpTitle();

					String sBezeichnung = "";
					if (getInternalFrameArtikel().getArtikelDto()
							.getArtikelsprDto() != null) {
						sBezeichnung = getInternalFrameArtikel()
								.getArtikelDto().getArtikelsprDto().getCBez();
					}
					if (sBezeichnung == null) {
						sBezeichnung = "";
					}
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							getInternalFrameArtikel().getArtikelDto().getCNr()
									+ ", " + sBezeichnung);
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, true);

					if (bKurzbezeichnungStattVerpackungsart) {
						// Statusbar setzen
						if (getInternalFrameArtikel().getArtikelDto() != null
								&& getInternalFrameArtikel().getArtikelDto()
										.getArtikelsprDto() != null
								&& getInternalFrameArtikel().getArtikelDto()
										.getArtikelsprDto().getCKbez() != null) {

							panelQueryArtikel.setStatusbarSpalte4(LPMain
									.getInstance().getTextRespectUISPr(
											"artikel.kurzbez")
									+ ": "
									+ getInternalFrameArtikel().getArtikelDto()
											.getArtikelsprDto().getCKbez());
						} else {
							panelQueryArtikel.setStatusbarSpalte4("");

						}

						if (getInternalFrameArtikel().getArtikelDto() != null
								&& getInternalFrameArtikel().getArtikelDto()
										.getCIndex() != null) {
							panelQueryArtikel.setStatusbarSpalte5(
									LPMain.getInstance().getTextRespectUISPr(
											"artikel.index")
											+ ": "
											+ getInternalFrameArtikel()
													.getArtikelDto()
													.getCIndex(), true);
						} else {
							panelQueryArtikel.setStatusbarSpalte5("", true);

						}
					} else {

						// Statusbar setzen
						Integer materialIId = getInternalFrameArtikel()
								.getArtikelDto().getMaterialIId();
						if (materialIId != null) {
							MaterialDto materialDto = DelegateFactory
									.getInstance().getMaterialDelegate()
									.materialFindByPrimaryKey(materialIId);

							panelQueryArtikel.setStatusbarSpalte4(LPMain
									.getInstance().getTextRespectUISPr(
											"fert.tab.oben.material.title")
									+ ": " + materialDto.getBezeichnung());
						} else {
							panelQueryArtikel.setStatusbarSpalte4("");

						}

						if (getInternalFrameArtikel().getArtikelDto()
								.getVerpackungDto() != null
								&& getInternalFrameArtikel().getArtikelDto()
										.getVerpackungDto()
										.getCVerpackungsart() != null) {
							panelQueryArtikel
									.setStatusbarSpalte5(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"artikel.technik.verpackungsart")
													+ ": "
													+ getInternalFrameArtikel()
															.getArtikelDto()
															.getVerpackungDto()
															.getCVerpackungsart(),
											true);
						} else {
							panelQueryArtikel.setStatusbarSpalte5("", true);

						}

						if (getInternalFrameArtikel().getArtikelDto()
								.getVerpackungDto() != null
								&& getInternalFrameArtikel().getArtikelDto()
										.getVerpackungDto().getCBauform() != null) {
							panelQueryArtikel.setStatusbarSpalte6(
									LPMain.getInstance().getTextRespectUISPr(
											"artikel.technik.bauform")
											+ ": "
											+ getInternalFrameArtikel()
													.getArtikelDto()
													.getVerpackungDto()
													.getCBauform(), true);
						} else {
							panelQueryArtikel.setStatusbarSpalte6("", true);

						}
					}
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}
			} else if (e.getSource() == panelQueryArtikellieferant) {

				Integer iId = (Integer) panelQueryArtikellieferant
						.getSelectedId();

				createStaffelpreise(iId);
				if (panelQueryArtikellieferant.getSelectedId() != null) {
					panelDetailStaffelpeise.artikellieferantDto = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikellieferantFindByPrimaryKey(
									(Integer) panelQueryArtikellieferant
											.getSelectedId());
				} else {
					panelDetailStaffelpeise.artikellieferantDto = null;
				}
				if (getInternalFrameArtikel().getArtikelDto() != null
						&& getInternalFrameArtikel().getArtikelDto().getIId() != null) {
					panelDetailArtikellieferant.setKeyWhenDetailPanel(iId);
					panelDetailArtikellieferant.eventYouAreSelected(false);
					panelQueryArtikellieferant.updateButtons();

				}
			} else if (e.getSource() == panelQueryKatalog) {

				Integer iId = (Integer) panelQueryKatalog.getSelectedId();
				panelDetailKatalog.setKeyWhenDetailPanel(iId);
				panelDetailKatalog.eventYouAreSelected(false);
				panelQueryKatalog.updateButtons();
			} else if (e.getSource() == panelQueryEinkaufsean) {

				Integer iId = (Integer) panelQueryEinkaufsean.getSelectedId();
				panelDetailEinkaufsean.setKeyWhenDetailPanel(iId);
				panelDetailEinkaufsean.eventYouAreSelected(false);
				panelQueryEinkaufsean.updateButtons();
			} else if (e.getSource() == panelQuerySperren) {

				Integer iId = (Integer) panelQuerySperren.getSelectedId();
				panelDetailSperren.setKeyWhenDetailPanel(iId);
				panelDetailSperren.eventYouAreSelected(false);
				panelQuerySperren.updateButtons();
			} else if (e.getSource() == panelQueryShopgruppe) {

				Integer iId = (Integer) panelQueryShopgruppe.getSelectedId();
				panelDetailShopgruppe.setKeyWhenDetailPanel(iId);
				panelDetailShopgruppe.eventYouAreSelected(false);
				panelQueryShopgruppe.updateButtons();
			} else if (e.getSource() == panelQueryZugehoerige) {

				Integer iId = (Integer) panelQueryZugehoerige.getSelectedId();
				panelDetailZugehoerige.setKeyWhenDetailPanel(iId);
				panelDetailZugehoerige.eventYouAreSelected(false);
				panelQueryZugehoerige.updateButtons();
			} else if (e.getSource() == panelQueryArtikellager) {

				WwArtikellagerPK iId = (WwArtikellagerPK) panelQueryArtikellager
						.getSelectedId();
				panelDetailArtikellager.setKeyWhenDetailPanel(iId);
				panelDetailArtikellager.eventYouAreSelected(false);
				panelQueryArtikellager.updateButtons();

			} else if (e.getSource() == panelQueryStaffelpreise) {

				Integer iId = (Integer) panelQueryStaffelpreise.getSelectedId();
				panelDetailStaffelpeise.setKeyWhenDetailPanel(iId);
				panelDetailStaffelpeise.eventYouAreSelected(false);
				panelQueryStaffelpreise.updateButtons();
			} else if (e.getSource() == panelQueryLagerplatz) {
				Integer iId = (Integer) panelQueryLagerplatz.getSelectedId();
				panelDetailLagerplatz.setKeyWhenDetailPanel(iId);
				panelDetailLagerplatz.eventYouAreSelected(false);
				panelQueryLagerplatz.updateButtons();

			} else if (e.getSource() == panelQueryArtikelkommentar) {
				Integer iId = (Integer) panelQueryArtikelkommentar
						.getSelectedId();
				panelDetailArtikelkommentar.setKeyWhenDetailPanel(iId);
				panelDetailArtikelkommentar.eventYouAreSelected(false);
				panelQueryArtikelkommentar.updateButtons();
			} else if (e.getSource() == panelQueryVkpfStaffelmenge) {
				Integer iId = (Integer) panelQueryVkpfStaffelmenge
						.getSelectedId();
				panelDetailVkpfStaffelmenge.setKeyWhenDetailPanel(iId);
				panelDetailVkpfStaffelmenge.eventYouAreSelected(false);
				panelQueryVkpfStaffelmenge.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailArtikel) {
				this.setSelectedComponent(panelQueryArtikel);
				setKeyWasForLockMe();
				panelQueryArtikel.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikellieferant) {
				setKeyWasForLockMe();
				if (panelDetailArtikellieferant.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikellieferant
							.getId2SelectAfterDelete();
					panelQueryArtikellieferant.setSelectedId(oNaechster);
				}
				panelSplitArtikellieferant.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKatalog) {
				setKeyWasForLockMe();
				if (panelDetailKatalog.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKatalog
							.getId2SelectAfterDelete();
					panelQueryKatalog.setSelectedId(oNaechster);
				}
				panelSplitKatalog.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSperren) {
				setKeyWasForLockMe();
				if (panelDetailSperren.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySperren
							.getId2SelectAfterDelete();
					panelQuerySperren.setSelectedId(oNaechster);
				}
				panelSplitSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailShopgruppe) {
				setKeyWasForLockMe();
				if (panelDetailShopgruppe.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryShopgruppe
							.getId2SelectAfterDelete();
					panelQueryShopgruppe.setSelectedId(oNaechster);
				}
				panelSplitShopgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailZugehoerige) {
				setKeyWasForLockMe();
				if (panelDetailZugehoerige.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZugehoerige
							.getId2SelectAfterDelete();
					panelQueryZugehoerige.setSelectedId(oNaechster);
				}
				panelSplitZugehoerige.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailStaffelpeise) {
				setKeyWasForLockMe();
				if (panelDetailStaffelpeise.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryStaffelpreise
							.getId2SelectAfterDelete();
					panelQueryStaffelpreise.setSelectedId(oNaechster);
				}
				panelSplitStaffelpreise.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailEinkaufsean) {
				setKeyWasForLockMe();
				if (panelDetailEinkaufsean.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryEinkaufsean
							.getId2SelectAfterDelete();
					panelQueryEinkaufsean.setSelectedId(oNaechster);
				}
				panelSplitEinkaufsean.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailLagerplatz) {
				setKeyWasForLockMe();
				if (panelDetailLagerplatz.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLagerplatz
							.getId2SelectAfterDelete();
					panelQueryLagerplatz.setSelectedId(oNaechster);
				}
				panelSplitLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikelkommentar) {
				setKeyWasForLockMe();
				if (panelDetailArtikelkommentar.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikelkommentar
							.getId2SelectAfterDelete();
					panelQueryArtikelkommentar.setSelectedId(oNaechster);
				}
				panelSplitArtikelkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikellager) {
				setKeyWasForLockMe();
				if (panelDetailArtikellager.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikellager
							.getId2SelectAfterDelete();
					panelQueryArtikellager.setSelectedId(oNaechster);
				}
				panelSplitArtikellager.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailVkpfStaffelmenge) {
				setKeyWasForLockMe();
				if (panelDetailVkpfStaffelmenge.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVkpfStaffelmenge
							.getId2SelectAfterDelete();
					panelQueryVkpfStaffelmenge.setSelectedId(oNaechster);
				}
				panelQueryVkpfStaffelmenge.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			// usemenuebar: Hier super.eventItemchanged(e); aufrufen, damit die
			// Menuebar, wenn auf ein "unteres Ohrwaschl"
			// geklickt wird, angezeigt wird.
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == panelQueryArtikel
					&& sAspectInfo.endsWith(EXTRA_NEU_AUS_ARTIKEL)) {
				createDetail((Integer) panelQueryArtikel.getSelectedId());

				if (panelQueryArtikel.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				dialogArtikel();

			} else if (e.getSource() == panelQueryArtikel
					&& sAspectInfo.endsWith(EXTRA_LAGERORT_BEARBEITEN)) {

				ArbeitsplatzparameterDto aparameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_LAGERPLATZ_DIREKT_ERFASSEN);

				DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								(Integer) panelQueryArtikel.getSelectedId());

				DialogLagerortBearbeiten d = new DialogLagerortBearbeiten(
						new Integer(aparameter.getCWert()), panelQueryArtikel);

				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryArtikel) {
				createDetail((Integer) panelQueryArtikel.getSelectedId());

				if (panelQueryArtikel.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailArtikel.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailArtikel);
			} else if (e.getSource() == panelQueryArtikellieferant) {
				panelDetailArtikellieferant.eventActionNew(e, true, false);
				panelDetailArtikellieferant.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitArtikellieferant);
			} else if (e.getSource() == panelQueryArtikellager) {
				panelDetailArtikellager.eventActionNew(e, true, false);
				panelDetailArtikellager.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitArtikellager);
			} else if (e.getSource() == panelQueryKatalog) {
				panelDetailKatalog.eventActionNew(e, true, false);
				panelDetailKatalog.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitKatalog);
			} else if (e.getSource() == panelQueryEinkaufsean) {
				panelDetailEinkaufsean.eventActionNew(e, true, false);
				panelDetailEinkaufsean.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitEinkaufsean);
			} else if (e.getSource() == panelQuerySperren) {
				panelDetailSperren.eventActionNew(e, true, false);
				panelDetailSperren.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitSperren);
			} else if (e.getSource() == panelQueryShopgruppe) {
				panelDetailShopgruppe.eventActionNew(e, true, false);
				panelDetailShopgruppe.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitShopgruppe);
			} else if (e.getSource() == panelQueryZugehoerige) {
				panelDetailZugehoerige.eventActionNew(e, true, false);
				panelDetailZugehoerige.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitZugehoerige);
			} else if (e.getSource() == panelQueryLagerplatz) {
				panelDetailLagerplatz.eventActionNew(e, true, false);
				panelDetailLagerplatz.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitLagerplatz);
			} else if (e.getSource() == panelQueryStaffelpreise) {
				panelDetailStaffelpeise.eventActionNew(e, true, false);
				panelDetailStaffelpeise.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitStaffelpreise);
			} else if (e.getSource() == panelQueryArtikelkommentar) {
				panelDetailArtikelkommentar.eventActionNew(e, true, false);
				panelDetailArtikelkommentar.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitArtikelkommentar);
			} else if (e.getSource() == panelQueryVkpfStaffelmenge) {
				panelDetailVkpfStaffelmenge.eventActionNew(e, true, false);
				panelDetailVkpfStaffelmenge.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitVkpfStaffelmenge);
			}
		}
		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryArtikellieferant) {
				int iPos = panelQueryArtikellieferant.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryArtikellieferant
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryArtikellieferant
							.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.vertauscheArtikellieferanten(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryArtikellieferant.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryArtikelkommentar) {
				int iPos = panelQueryArtikelkommentar.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryArtikelkommentar
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryArtikelkommentar
							.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory
							.getInstance()
							.getArtikelkommentarDelegate()
							.vertauscheArtikelkommentar(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryArtikelkommentar.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryLagerplatz) {
				int iPos = panelQueryLagerplatz.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryLagerplatz
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryLagerplatz
							.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.vertauscheArtikellagerplaetze(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryLagerplatz.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQuerySperren) {
				int iPos = panelQuerySperren.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQuerySperren
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQuerySperren
							.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.vertauscheArtikelsperren(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQuerySperren.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryArtikellieferant) {
				int iPos = panelQueryArtikellieferant.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryArtikellieferant.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryArtikellieferant
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryArtikellieferant
							.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.vertauscheArtikellieferanten(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryArtikellieferant.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryArtikelkommentar) {
				int iPos = panelQueryArtikelkommentar.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryArtikelkommentar.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryArtikelkommentar
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryArtikelkommentar
							.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory
							.getInstance()
							.getArtikelkommentarDelegate()
							.vertauscheArtikelkommentar(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryArtikelkommentar.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryLagerplatz) {
				int iPos = panelQueryLagerplatz.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryLagerplatz.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryLagerplatz
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryLagerplatz
							.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.vertauscheArtikellagerplaetze(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryLagerplatz.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQuerySperren) {
				int iPos = panelQuerySperren.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQuerySperren.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQuerySperren
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQuerySperren
							.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.vertauscheArtikelsperren(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQuerySperren.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == panelQueryArtikellieferant) {
				panelDetailArtikellieferant.eventActionNew(e, true, false);
				panelDetailArtikellieferant.eventYouAreSelected(false); // Buttons
				// schalten
			}
		}
	}

	private void dialogArtikel() throws Throwable {
		panelArtikel = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), false);
		panelArtikel.setSelectedId(panelQueryArtikel.getSelectedId());
		new DialogQuery(panelArtikel);
	}

	private void dialogPaternoster() throws Throwable {
		panelQueryFLRPaternoster = ArtikelFilterFactory.getInstance()
				.createPanelFLRPaternoster(getInternalFrame(), null, null);
		new DialogQuery(panelQueryFLRPaternoster);
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		// usemenubar 1: super-Methode muss in jedem TabbedPane vorhanden sein
		/*
		 * AD: if (e != null) System.out.println(e.toString() + ", " +
		 * e.getSource().toString());
		 */
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryArtikel.eventYouAreSelected(false);
			if (panelQueryArtikel.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			panelQueryArtikel.updateButtons();

		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			createDetail((Integer) panelQueryArtikel.getSelectedId());
			panelDetailArtikel.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_PREISE) {

			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();
			myLogger.error(
					"ART>VK-Preise 1 : "
							+ (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART),
					null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();

			if (DelegateFactory.getInstance().getVkPreisfindungDelegate()
					.vkpfartikelpreislisteFindByMandantCNr() == null
					|| DelegateFactory.getInstance()
							.getVkPreisfindungDelegate()
							.vkpfartikelpreislisteFindByMandantCNr().length == 0) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
						.getTextRespectUISPr("vkpf.hint.keinepreisliste"));
				setSelectedComponent(panelQueryArtikel);
			} else {
				DelegateFactory.getInstance().getLagerDelegate()
						.getHauptlagerDesMandanten();

				createPreise(getInternalFrameArtikel().getArtikelDto().getIId());
				panelDetailArtikelpreise.eventYouAreSelected(false);
			}

			myLogger.error(
					"ART>VK-Preise 2 : "
							+ (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART),
					null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();
		} else if (selectedIndex == IDX_PANEL_VKPFSTAFFELMENGE) {
			createVkpfStaffelmenge(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelSplitVkpfStaffelmenge.eventYouAreSelected(false);
			panelQueryVkpfStaffelmenge.updateButtons();
		} else if (selectedIndex == IDX_PANEL_BESTELLDATEN) {
			createBestelldaten(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelDetailArtikelbestelldaten.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_LIEFERANT) {
			createArtikellieferant(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelSplitArtikellieferant.eventYouAreSelected(false);
			panelQueryArtikellieferant.updateButtons();
		} else if (selectedIndex == IDX_PANEL_EK_STAFFELPREISE) {
			createArtikellieferant(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelQueryArtikellieferant.eventYouAreSelected(false);
			panelDetailArtikellieferant
					.setKeyWhenDetailPanel(panelQueryArtikellieferant
							.getSelectedId());
			panelDetailArtikellieferant.eventYouAreSelected(false);

			if (panelQueryArtikellieferant.getSelectedId() != null) {

				createStaffelpreise((Integer) panelQueryArtikellieferant
						.getSelectedId());
				panelSplitStaffelpreise.eventYouAreSelected(false);
				panelQueryStaffelpreise.updateButtons();
			} else {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.info"),
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.keinlieferantausgewaehlt"));

				setSelectedIndex(IDX_PANEL_LIEFERANT);
				lPEventObjectChanged(e);
			}
		} else if (selectedIndex == IDX_PANEL_TECHNIK) {
			createTechnik(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailArtikeltechnik.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_SONSTIGES) {
			createSonstiges(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailArtikelsonstiges.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_EXTERNEDOKUMENTE) {
			createExterneDokumente(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelDetailExterneDokumente.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_LAGER) {
			createArtikellager(getInternalFrameArtikel().getArtikelDto()
					.getIId());

			panelSplitArtikellager.eventYouAreSelected(false);
			panelQueryArtikellager.updateButtons();

		} else if (selectedIndex == IDX_PANEL_KATALOG) {
			createKatalog(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitKatalog.eventYouAreSelected(false);
			panelQueryKatalog.updateButtons();
		} else if (selectedIndex == IDX_PANEL_EINKAUFSEAN) {
			createEinkaufsean(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelSplitEinkaufsean.eventYouAreSelected(false);
			panelQueryEinkaufsean.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SPERREN) {
			createSperren(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitSperren.eventYouAreSelected(false);
			panelQuerySperren.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SHOPGRUPPE) {
			createShopgruppe(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitShopgruppe.eventYouAreSelected(false);
			panelQueryShopgruppe.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZUGEHOERIGE) {
			createZugehoerige(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelSplitZugehoerige.eventYouAreSelected(false);
			panelQueryZugehoerige.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LAGERPLATZ) {
			createLagerplatz(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitLagerplatz.eventYouAreSelected(false);
			panelQueryLagerplatz.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ARTIKELKOMMENTAR) {
			createArtikelkommentar(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelSplitArtikelkommentar.eventYouAreSelected(false);
			panelQueryArtikelkommentar.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ARTIKELEIGENSCHAFTEN) {
			createEigenschaften(getInternalFrameArtikel().getArtikelDto()
					.getIId());
			panelDetailArtikeleigenschaft.eventYouAreSelected(false);
		}
		refreshTitle();
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.artikel"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		String sBezeichnung = "";
		if (getInternalFrameArtikel().getArtikelDto() != null) {
			if (getInternalFrameArtikel().getArtikelDto().getArtikelsprDto() != null) {
				sBezeichnung = getInternalFrameArtikel().getArtikelDto()
						.getArtikelsprDto().getCBez();
			}
			if (sBezeichnung == null) {
				sBezeichnung = "";
			}
			if (getInternalFrameArtikel().getArtikelDto().getCNr() == null) {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE, "");

			} else {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFrameArtikel().getArtikelDto().getCNr()
								+ ", " + sBezeichnung);
			}
		}
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		// usemenuebar: Vergleichen, welcher Button gedrueckt wurde
		if (e.getActionCommand().equals(MENUE_ACTION_STATISTIK)) {

			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.artikelstatistik");
			if (getInternalFrameArtikel().getArtikelDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportArtikelstatistik(getInternalFrameArtikel(),
								getInternalFrameArtikel().getArtikelDto()
										.getIId(), false, add2Title));
			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_RESERVIERUNGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.artikelreservierung");
			getInternalFrame().showReportKriterien(
					new ReportArtikelreservierungen(getInternalFrameArtikel(),
							add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_BESTELLTLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.artikelbestellt");
			getInternalFrame().showReportKriterien(
					new ReportArtikelbestellt(getInternalFrameArtikel(),
							getInternalFrameArtikel().getArtikelDto(),
							add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_FEHLMENGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.fehlmengen");
			getInternalFrame().showReportKriterien(
					new ReportArtikelfehlmengen(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_VKPREISENTWICKLUNG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.vkpreisentwicklung");
			getInternalFrame().showReportKriterien(
					new ReportVkpreisentwicklung(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_ANFRAGESTATISTIK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"anf.anfragestatistik");
			getInternalFrame().showReportKriterien(
					new ReportAnfragestatistik(getInternalFrameArtikel(),
							add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_ANGEBOTSSTATISTIK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"angb.angebotsstatistik");
			getInternalFrame().showReportKriterien(
					new ReportAngebotsstatistik(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_RAHMENRESERVIERUNGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"auft.rahmenreservierungen");
			getInternalFrame().showReportKriterien(
					new ReportRahmenauftragReservierungsliste(
							getInternalFrameArtikel(), add2Title));

		} else if (e.getActionCommand()
				.equals(MENUE_ACTION_VERWENDUNGSNACHWEIS)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.verwendungsnachweis");
			getInternalFrame().showReportKriterien(
					new ReportVerwendungsnachweis(getInternalFrameArtikel(),
							add2Title));

		} else if (e.getActionCommand().equals(MENUE_PFLEGE_VKPREISE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.title.panel.preise");
			getInternalFrame().showPanelDialog(
					new PanelDialogPreispflege(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_PFLEGE_PATERNOSTER)) {
			dialogPaternoster();
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_LAGERSTANDSLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.lagerstandsliste");
			getInternalFrame().showReportKriterien(
					new ReportLagerstandliste(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_ARTIKELGRUPPEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.artikelgruppen");
			getInternalFrame().showReportKriterien(
					new ReportArtikelgruppen(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand()
				.equals(MENUE_JOURNAL_ACTION_SHOPGRUPPEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.shopgruppen");
			getInternalFrame()
					.showReportKriterien(
							new ReportShopgruppen(getInternalFrameArtikel(),
									add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_NAECHSTE_WARTUNGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.naechstewartungen");
			getInternalFrame().showReportKriterien(
					new ReportNaechsteWartungen(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_SERIENNUMMERN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.seriennummern");
			getInternalFrame().showReportKriterien(
					new ReportSeriennummern(getInternalFrameArtikel(),
							add2Title, (Integer) panelQueryArtikel
									.getSelectedId()));
		} else if (e.getActionCommand().equals(MENUE_ACTION_ETIKETT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.etikett");
			getInternalFrame().showReportKriterien(
					new ReportArtikeletikett(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_STAMMBLATT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.stammblatt");
			getInternalFrame().showReportKriterien(
					new ReportArtikelstammblatt(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_LOSSTATUS)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.losstatus");
			getInternalFrame().showReportKriterien(
					new ReportLosstatus(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_DETAILBEDARFE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.detailbedarf");
			getInternalFrame().showReportKriterien(
					new ReportRahmenbedarfe(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_FREIINFERTIGUNG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.freiinfertigung");
			getInternalFrame().showReportKriterien(
					new ReportFreiInFertigung(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_KUNDENSOKOS)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.kundesokos");
			getInternalFrame()
					.showReportKriterien(
							new ReportKundensokos(getInternalFrameArtikel(),
									add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_BEWEGUNSVORSCHAU)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"bes.bewegungsvorschau");
			getInternalFrame().showReportKriterien(
					new ReportBewegungsvorschau(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_AUFTRAGSSERIENNUMMERN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.auftragsseriennummern");
			getInternalFrame().showReportKriterien(
					new ReportAuftragsseriennummern(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_WARENBENEGUNSJOURNAL)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.warenbewegungsjournal");
			getInternalFrame().showReportKriterien(
					new ReportWarenbewegung(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_REKLAMATIONEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikl.report.reklamationen");
			getInternalFrame().showReportKriterien(
					new ReportOffeneReklamationenEinesArtikels(
							getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_GESTPREISUEBERMINVK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.gestpreisueberminvk");
			getInternalFrame().showReportKriterien(
					new ReportGestpreisUeberMinVK(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_MINDESTLAGERSTAENDE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.mindestlagerstaende");
			getInternalFrame().showReportKriterien(
					new ReportMindestlagerstaende(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_WARENENTNAHMESTATISTIK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.warenentnahmestatistik");
			getInternalFrame().showReportKriterien(
					new ReportWarenentnahmestatistik(getInternalFrameArtikel(),
							add2Title));
		} else if (e.getActionCommand()
				.equals(MENUE_JOURNAL_ACTION_LADENHUETER)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.ladenhueter");
			getInternalFrame()
					.showReportKriterien(
							new ReportLadenhueter(getInternalFrameArtikel(),
									add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_HITLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.hitliste");
			getInternalFrame().showReportKriterien(
					new ReportHitliste(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_MINDESTHALTBARKEIT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.journal.mindesthaltbarkeit");
			getInternalFrame().showReportKriterien(
					new ReportMindesthalbarkeit(getInternalFrameArtikel(),
							add2Title));
		}

		else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_PREISLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.preisliste");
			getInternalFrame().showReportKriterien(
					new ReportPreisliste(getInternalFrameArtikel(), add2Title));
		}

		else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_OFFENERAHMENBEDARFE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.offenerahmendetailbedarfe");
			getInternalFrame().showReportKriterien(
					new ReportAlleRahmenbedarfe(getInternalFrameArtikel(),
							add2Title));
		}

		else if (e.getActionCommand().equals(MENUE_ACTION_RAHMENBESTELLTLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.artikelrahmenbestelltliste");
			getInternalFrame().showReportKriterien(
					new ReportArtikelRahmenbestellungsliste(
							getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_AENDERUNGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.info.report.aenderungen");
			getInternalFrame()
					.showReportKriterien(
							new ReportAenderungen(getInternalFrameArtikel(),
									add2Title));
		} else if (e.getActionCommand().equals(MENUE_PFLEGE_AENDERE_SNRCHNR)) {
			getInternalFrame().showPanelDialog(
					new PanelDialogSnrChnrAendern(getInternalFrameArtikel(),
							LPMain.getInstance().getTextRespectUISPr(
									"artikel.pflege.snrchnraendern")));
		} else if (e.getActionCommand().equals(MENUE_ACTION_CSVIMPORT)) {

			// Dateiauswahldialog
			// PJ 14984
			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_CSV, false);
			File f = null;
			if (files != null && files.length > 0) {
				f = files[0];
			}
			if (f != null) {
				LPCSVReader reader = new LPCSVReader(new FileReader(f), ';');

				String[] sLine;
				// Erste Zeile Auslassen (Ueberschrift)
				sLine = reader.readNext();
				// zeilenweise einlesen.
				sLine = reader.readNext();
				ArrayList<ArtikelImportDto> alDaten = new ArrayList<ArtikelImportDto>();

				do {

					ArtikelImportDto dtoTemp = new ArtikelImportDto();
					try {
						dtoTemp.setArtikelnummer(sLine[0]);
						dtoTemp.setBezeichnung(sLine[1]);
						dtoTemp.setKurzbezeichnung(sLine[2]);
						dtoTemp.setZusatzbezeichnung(sLine[3]);
						dtoTemp.setZusatzbezeichnung2(sLine[4]);
						dtoTemp.setArtikelart(sLine[5]);
						dtoTemp.setEinheit(sLine[6]);
						dtoTemp.setArtikelgruppe(sLine[7]);
						dtoTemp.setArtikelklasse(sLine[8]);
						dtoTemp.setReferenznummer(sLine[9]);
						dtoTemp.setMwstsatz(sLine[10]);

						// 14520
						dtoTemp.setVkpreisbasis(sLine[11]);
						dtoTemp.setVkpreisbasisgueltigab(sLine[12]);

						dtoTemp.setFixpreispreisliste1(sLine[13]);
						dtoTemp.setRabattsatzpreisliste1(sLine[14]);
						dtoTemp.setGueltigabpreisliste1(sLine[15]);

						dtoTemp.setFixpreispreisliste2(sLine[16]);
						dtoTemp.setRabattsatzpreisliste2(sLine[17]);
						dtoTemp.setGueltigabpreisliste2(sLine[18]);

						dtoTemp.setFixpreispreisliste3(sLine[19]);
						dtoTemp.setRabattsatzpreisliste3(sLine[20]);
						dtoTemp.setGueltigabpreisliste3(sLine[21]);

						// PJ17708
						// otional REVISION / INDEX / CHARGENGEFUEHRT
						// //SNRBEHAFTET
						if (sLine.length > 22) {
							dtoTemp.setRevision(sLine[22]);
						}
						if (sLine.length > 23) {
							dtoTemp.setIndex(sLine[23]);
						}
						if (sLine.length > 24) {
							dtoTemp.setChargenbehaftet(sLine[24]);
						}
						if (sLine.length > 25) {
							dtoTemp.setSnrbehaftet(sLine[25]);
						}

						alDaten.add(dtoTemp);
					} catch (java.lang.ArrayIndexOutOfBoundsException e1) {
						// dann wurde der Rest mir Leer aufgefuellt
						alDaten.add(dtoTemp);
					}

					sLine = reader.readNext();
				} while (sLine != null);

				ArtikelImportDto[] dtos = new ArtikelImportDto[alDaten.size()];

				dtos = (ArtikelImportDto[]) alDaten.toArray(dtos);

				DialogArtikelImport d = new DialogArtikelImport(dtos, this);
				d.setSize(500, 500);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_PREISPFLEGE_EXPORT)) {
			DialogArtikelExport d = new DialogArtikelExport(
					getInternalFrameArtikel());
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);
		} else if (e.getActionCommand().equals(MENUE_ACTION_PREISPFLEGE_IMPORT)) {

			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_XLS, false);
			if (files == null || files.length < 1 || files[0] == null) {
				return;
			}
			File f = files[0];

			ByteArrayOutputStream ous = null;
			InputStream ios = null;
			try {
				byte[] buffer = new byte[4096];
				ous = new ByteArrayOutputStream();
				ios = new FileInputStream(f);
				int read = 0;
				while ((read = ios.read(buffer)) != -1) {
					ous.write(buffer, 0, read);
				}
			} finally {
				try {
					if (ous != null)
						ous.close();
				} catch (IOException ex) {
				}

				try {
					if (ios != null)
						ios.close();
				} catch (IOException ex) {
				}
			}

			DelegateFactory.getInstance().getArtikelDelegate()
					.preiseXLSForPreispflege(ous.toByteArray());

			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hinweis"),
							LPMain.getTextRespectUISPr("artikel.preispflege.import.erfolgreich"));

		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {

			// usemenubar 3: Nachfolgende Zeile erzeugt Standard- Symbolleiste
			wrapperManuBar = new WrapperMenuBar(this);
			JMenu menuDatei = (JMenu) wrapperManuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);
			menuDatei.add(new JSeparator(), 0);
			// CSV-Import
			JMenuItem menuItemImport = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("part.csvimport"));
			menuItemImport.addActionListener(this);
			menuItemImport.setActionCommand(MENUE_ACTION_CSVIMPORT);
			menuDatei.add(menuItemImport, 0);

			// usemenubar 4: Hier Artikelspezifische Menues hinzufuegen
			JMenu menuPflege = new WrapperMenu("lp.pflege", this);
			JMenu menuInfo = new WrapperMenu("lp.info", this);

			JMenuItem menuItemArtikelstatistik = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("lp.statistik"));

			JMenuItem menuItemReservierungen = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.report.artikelreservierung"));
			JMenuItem menuItemFehlmenge = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("artikel.report.fehlmengen"));
			JMenuItem menuItemBestelltliste = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.report.artikelbestellt"));
			// Rahmenbestelltliste
			JMenuItem menuItemRahmenbestelltliste = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.report.artikelrahmenbestelltliste"));
			JMenuItem menuItemAnfragestatistik = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("anf.anfragestatistik"));
			JMenuItem menuItemAngebotsstatistik = new JMenuItem(LPMain
					.getInstance()
					.getTextRespectUISPr("angb.angebotsstatistik"));
			JMenuItem menuItemVerwendungsnachweis = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.verwendungsnachweis"));

			// usemenubar 5: Actionlistener setzen
			menuItemArtikelstatistik.addActionListener(this);
			// usemenubar 6: und ActionCommand setzten
			// Druck der Statistik nur wenn DarfPreiseSehen Rechte gesetzt
			// sind.
			menuItemArtikelstatistik.setActionCommand(MENUE_ACTION_STATISTIK);
			menuInfo.add(menuItemArtikelstatistik);

			menuItemReservierungen.addActionListener(this);
			menuItemReservierungen
					.setActionCommand(MENUE_ACTION_RESERVIERUNGEN);
			menuInfo.add(menuItemReservierungen);

			menuItemFehlmenge.addActionListener(this);
			menuItemFehlmenge.setActionCommand(MENUE_ACTION_FEHLMENGEN);
			menuInfo.add(menuItemFehlmenge);

			menuItemBestelltliste.addActionListener(this);
			menuItemBestelltliste.setActionCommand(MENUE_ACTION_BESTELLTLISTE);
			menuInfo.add(menuItemBestelltliste);

			menuItemRahmenbestelltliste.addActionListener(this);
			menuItemRahmenbestelltliste
					.setActionCommand(MENUE_ACTION_RAHMENBESTELLTLISTE);
			menuInfo.add(menuItemRahmenbestelltliste);

			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
				menuItemAnfragestatistik.addActionListener(this);
				menuItemAnfragestatistik
						.setActionCommand(MENUE_ACTION_ANFRAGESTATISTIK);
				menuInfo.add(menuItemAnfragestatistik);
			}

			if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
				menuItemAngebotsstatistik.addActionListener(this);
				menuItemAngebotsstatistik
						.setActionCommand(MENUE_ACTION_ANGEBOTSSTATISTIK);
				menuInfo.add(menuItemAngebotsstatistik);
			}

			JMenuItem menuItemRahmenreservierungen = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"auft.rahmenreservierungen"));

			menuItemRahmenreservierungen.addActionListener(this);
			menuItemRahmenreservierungen
					.setActionCommand(MENUE_ACTION_RAHMENRESERVIERUNGEN);
			menuInfo.add(menuItemRahmenreservierungen);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_RAHMENDETAILBEDARFE)) {

				JMenuItem menuItemDetailbedarfe = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.detailbedarf"));

				menuItemDetailbedarfe.addActionListener(this);
				menuItemDetailbedarfe
						.setActionCommand(MENUE_ACTION_DETAILBEDARFE);
				menuInfo.add(menuItemDetailbedarfe);
			}

			menuItemVerwendungsnachweis.addActionListener(this);
			menuItemVerwendungsnachweis
					.setActionCommand(MENUE_ACTION_VERWENDUNGSNACHWEIS);
			menuInfo.add(menuItemVerwendungsnachweis);

			JMenuItem menuItemEtikett = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("artikel.report.etikett"));
			menuItemEtikett.addActionListener(this);
			menuItemEtikett.setActionCommand(MENUE_ACTION_ETIKETT);
			menuInfo.add(menuItemEtikett);

			JMenuItem menuItemStammblatt = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("artikel.report.stammblatt"));
			menuItemStammblatt.addActionListener(this);
			menuItemStammblatt.setActionCommand(MENUE_ACTION_STAMMBLATT);
			menuInfo.add(menuItemStammblatt);

			JMenuItem menuItemLosstatus = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("artikel.report.losstatus"));
			menuItemLosstatus.addActionListener(this);
			menuItemLosstatus.setActionCommand(MENUE_ACTION_LOSSTATUS);
			menuInfo.add(menuItemLosstatus);

			JMenuItem menuItemFreiinfertigung = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.report.freiinfertigung"));
			menuItemFreiinfertigung.addActionListener(this);
			menuItemFreiinfertigung
					.setActionCommand(MENUE_ACTION_FREIINFERTIGUNG);
			menuInfo.add(menuItemFreiinfertigung);
			JMenuItem menuItemBewegungsvorschau = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("bes.bewegungsvorschau"));
			menuItemBewegungsvorschau.addActionListener(this);
			menuItemBewegungsvorschau
					.setActionCommand(MENUE_ACTION_BEWEGUNSVORSCHAU);
			menuInfo.add(menuItemBewegungsvorschau);

			JMenuItem menuItemWarenbewegungsjournal = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.report.warenbewegungsjournal"));
			menuItemWarenbewegungsjournal.addActionListener(this);
			menuItemWarenbewegungsjournal
					.setActionCommand(MENUE_JOURNAL_ACTION_WARENBENEGUNSJOURNAL);
			menuInfo.add(menuItemWarenbewegungsjournal);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_REKLAMATION)) {
				JMenuItem menuItemReklamationen = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikl.report.reklamationen"));
				menuItemReklamationen.addActionListener(this);
				menuItemReklamationen
						.setActionCommand(MENUE_JOURNAL_ACTION_REKLAMATIONEN);
				menuInfo.add(menuItemReklamationen);
			}

			// PJ14139
			if (getInternalFrame().bRechtDarfPreiseSehenVerkauf == true) {
				JMenuItem menuItemVkpreisentwicklung = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.vkpreisentwicklung"));
				menuItemVkpreisentwicklung.addActionListener(this);
				menuItemVkpreisentwicklung
						.setActionCommand(MENUE_ACTION_VKPREISENTWICKLUNG);
				menuInfo.add(menuItemVkpreisentwicklung);
			}

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_AUFTRAG_SERIENNUMMERN)) {
				JMenuItem menuItemAuftragsseriennummern = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.auftragsseriennummern"));
				menuItemAuftragsseriennummern.addActionListener(this);
				menuItemAuftragsseriennummern
						.setActionCommand(MENUE_ACTION_AUFTRAGSSERIENNUMMERN);
				menuInfo.add(menuItemAuftragsseriennummern);
			}

			JMenuItem menuItemAenderungen = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("artikel.info.report.aenderungen"));
			menuItemAenderungen.addActionListener(this);
			menuItemAenderungen.setActionCommand(MENUE_ACTION_AENDERUNGEN);
			menuInfo.add(menuItemAenderungen);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {

				JMenuItem menuItemKundensoksos = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.kundesokos"));
				menuItemKundensoksos.addActionListener(this);
				menuItemKundensoksos.setActionCommand(MENUE_ACTION_KUNDENSOKOS);
				menuInfo.add(menuItemKundensoksos);
			}

			// usemenubar 7: Unbedingt diese Methode verwenden, um Menues dem
			// MenueBar
			// hinzuzufuegen:
			wrapperManuBar.addJMenuItem(menuInfo);

			// PJ14139
			// Mit WH besprochen: wenn eins der beiden Rechte felht, dann ist
			// der
			// Punkt Journal weg!
			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf == true
					&& getInternalFrame().bRechtDarfPreiseSehenVerkauf == true) {

				// usemenubar 8: Wenn man in ein bestehendes System-Menue einen
				// Eintrag hinzufuegen
				// will, muss man diese Methode verwenden:
				JMenu journal = (JMenu) wrapperManuBar
						.getComponent(WrapperMenuBar.MENU_JOURNAL);
				JMenuItem menuItemLagerstandsListe = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.lagerstandsliste"));
				menuItemLagerstandsListe.addActionListener(this);
				menuItemLagerstandsListe
						.setActionCommand(MENUE_JOURNAL_ACTION_LAGERSTANDSLISTE);

				JMenuItem menuItemSeriennummernListe = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.seriennummern"));
				menuItemSeriennummernListe.addActionListener(this);
				menuItemSeriennummernListe
						.setActionCommand(MENUE_JOURNAL_ACTION_SERIENNUMMERN);

				journal.add(menuItemLagerstandsListe);

				WrapperMenuItem menuItemArtikelgruppen = new WrapperMenuItem(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.report.artikelgruppen"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menuItemArtikelgruppen.addActionListener(this);
				menuItemArtikelgruppen
						.setActionCommand(MENUE_JOURNAL_ACTION_ARTIKELGRUPPEN);
				journal.add(menuItemArtikelgruppen);

				WrapperMenuItem menuItemShopgruppen = new WrapperMenuItem(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.report.shopgruppen"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menuItemShopgruppen.addActionListener(this);
				menuItemShopgruppen
						.setActionCommand(MENUE_JOURNAL_ACTION_SHOPGRUPPEN);
				journal.add(menuItemShopgruppen);

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_CHARGENNUMMERN)
						|| LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufZusatzfunktionZugreifen(
										MandantFac.ZUSATZFUNKTION_SERIENNUMMERN)) {

					journal.add(menuItemSeriennummernListe);
				}
				WrapperMenuItem menuItemHitliste = new WrapperMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.hitliste"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menuItemHitliste.addActionListener(this);
				menuItemHitliste
						.setActionCommand(MENUE_JOURNAL_ACTION_HITLISTE);

				journal.add(menuItemHitliste);

				ParametermandantDto parameter = null;
				try {
					parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_CHARGENNUMMER_BEINHALTET_MINDESTHALTBARKEITSDATUM,
									ParameterFac.KATEGORIE_ARTIKEL,
									LPMain.getInstance().getTheClient()
											.getMandant());
				} catch (Throwable ex1) {
					handleException(ex1, true);
				}

				if (parameter.getCWert() != null
						&& !parameter.getCWert().equals("0")) {
					JMenuItem menuItemMindesthaltbarkeit = new JMenuItem(LPMain
							.getInstance().getTextRespectUISPr(
									"artikel.journal.mindesthaltbarkeit"));
					menuItemMindesthaltbarkeit.addActionListener(this);
					menuItemMindesthaltbarkeit
							.setActionCommand(MENUE_JOURNAL_ACTION_MINDESTHALTBARKEIT);

					journal.add(menuItemMindesthaltbarkeit);

				}

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_RAHMENDETAILBEDARFE)) {

					// Rahmenbedarfe
					JMenuItem menuItemRahmendetailbedarfe = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr(
									"artikel.report.offenerahmendetailbedarfe"));
					menuItemRahmendetailbedarfe.addActionListener(this);
					menuItemRahmendetailbedarfe
							.setActionCommand(MENUE_JOURNAL_ACTION_OFFENERAHMENBEDARFE);
					journal.add(menuItemRahmendetailbedarfe);
				}

				// Preisliste
				JMenuItem menuItemPreisliste = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.preisliste"));
				menuItemPreisliste.addActionListener(this);
				menuItemPreisliste
						.setActionCommand(MENUE_JOURNAL_ACTION_PREISLISTE);
				journal.add(menuItemPreisliste);

				JMenuItem menuItemLadenhueter = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.ladenhueter"));
				menuItemLadenhueter.addActionListener(this);
				menuItemLadenhueter
						.setActionCommand(MENUE_JOURNAL_ACTION_LADENHUETER);
				journal.add(menuItemLadenhueter);

				WrapperMenuItem menuItemGestpreisUnterMinVK = new WrapperMenuItem(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.report.gestpreisueberminvk"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menuItemGestpreisUnterMinVK.addActionListener(this);
				menuItemGestpreisUnterMinVK
						.setActionCommand(MENUE_JOURNAL_ACTION_GESTPREISUEBERMINVK);
				journal.add(menuItemGestpreisUnterMinVK);

				JMenuItem menuItemWarenentnahmestatistik = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.warenentnahmestatistik"));
				menuItemWarenentnahmestatistik.addActionListener(this);
				menuItemWarenentnahmestatistik
						.setActionCommand(MENUE_JOURNAL_ACTION_WARENENTNAHMESTATISTIK);
				journal.add(menuItemWarenentnahmestatistik);

				JMenuItem menuItemMindestlagerstaende = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.mindestlagerstaende"));
				menuItemMindestlagerstaende.addActionListener(this);
				menuItemMindestlagerstaende
						.setActionCommand(MENUE_JOURNAL_ACTION_MINDESTLAGERSTAENDE);
				journal.add(menuItemMindestlagerstaende);

				JMenuItem menuItemNaechsteWartungen = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.report.naechstewartungen"));
				menuItemNaechsteWartungen.addActionListener(this);
				menuItemNaechsteWartungen
						.setActionCommand(MENUE_JOURNAL_ACTION_NAECHSTE_WARTUNGEN);
				journal.add(menuItemNaechsteWartungen);

			}
			boolean bDarfLagerprueffunktionensehen = false;

			try {
				bDarfLagerprueffunktionensehen = DelegateFactory
						.getInstance()
						.getTheJudgeDelegate()
						.hatRecht(
								RechteFac.RECHT_WW_DARF_LAGERPRUEFFUNKTIONEN_SEHEN);
			} catch (Throwable ex) {
				handleException(ex, true);
			}

			if (bDarfLagerprueffunktionensehen) {

				JMenuItem menuItemPflegeVkpreise = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.title.panel.preise"));
				menuItemPflegeVkpreise.addActionListener(this);
				menuItemPflegeVkpreise.setActionCommand(MENUE_PFLEGE_VKPREISE);
				menuPflege.add(menuItemPflegeVkpreise);

				JMenuItem menuItemPruefeAendereSnrChnr = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"artikel.pflege.snrchnraendern"));
				menuItemPruefeAendereSnrChnr.addActionListener(this);
				menuItemPruefeAendereSnrChnr
						.setActionCommand(MENUE_PFLEGE_AENDERE_SNRCHNR);
				menuPflege.add(menuItemPruefeAendereSnrChnr);

				JMenuItem menuItemPaternoster = new JMenuItem(
						"Paternoster hinzuf\u00FCgen");
				menuItemPaternoster.addActionListener(this);
				menuItemPaternoster.setActionCommand(MENUE_PFLEGE_PATERNOSTER);
				menuPflege.add(menuItemPaternoster);

				if (getInternalFrame().bRechtDarfPreiseSehenVerkauf == true) {
					JMenuItem menuItemExport = new JMenuItem(LPMain
							.getInstance().getTextRespectUISPr(
									"artikel.preispflege.export"));
					menuItemExport.addActionListener(this);
					menuItemExport
							.setActionCommand(MENUE_ACTION_PREISPFLEGE_EXPORT);
					menuPflege.add(menuItemExport);
				}

				if (getInternalFrame().bRechtDarfPreiseAendernVerkauf == true) {
					JMenuItem menuItemExport = new JMenuItem(LPMain
							.getInstance().getTextRespectUISPr(
									"artikel.preispflege.import"));
					menuItemExport.addActionListener(this);
					menuItemExport
							.setActionCommand(MENUE_ACTION_PREISPFLEGE_IMPORT);
					menuPflege.add(menuItemExport);
				}

				wrapperManuBar.addJMenuItem(menuPflege);

			}

			// dann bekommt man das gewuenschte Menue zurueck und kann diesem
			// einen
			// Eintrag hinzufuegen. Um das Menueitem nicht an letzter Stelle
			// hunzuzufuegen, kann man bei
			// journal.add(..) auch die Stelle mitangeben
		}
		return wrapperManuBar;
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}
}
