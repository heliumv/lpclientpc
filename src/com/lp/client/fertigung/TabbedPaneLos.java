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
package com.lp.client.fertigung;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ReportArtikelbestellt;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogArtikelbilder;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.DialogSerienChargenauswahl;
import com.lp.client.frame.component.DialogSnrauswahl;
import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.ReportAufgeloestefehlmengen;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LostechnikerDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Panels der Lose
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.94 $
 */

public class TabbedPaneLos extends TabbedPane implements ICopyPaste {

	private PanelQuery panelQueryAuswahl = null;
	private PanelLosKopfdaten panelKopfdaten = null;

	private PanelSplit panelSplitMaterial = null;
	private PanelQuery panelQueryMaterial = null;
	private PanelLosMaterial panelDetailMaterial = null;

	private PanelSplit panelSplitZeitdaten = null;
	private PanelQuery panelQueryZeitdaten = null;
	private PanelLosZeitdaten panelDetailZeitdaten = null;

	private PanelSplit panelSplitLagerentnahme = null;
	private PanelQuery panelQueryLagerentnahme = null;
	private PanelLosLagerentnahme panelDetailLagerentnahme = null;

	private PanelSplit panelSplitAblieferung = null;
	private PanelQuery panelQueryAblieferung = null;
	private PanelLosAblieferung panelDetailAblieferung = null;

	private PanelSplit panelSplitGutschlecht = null;
	private PanelQuery panelQueryGutschlecht = null;
	private PanelLosGutSchlecht panelDetailGutschlecht = null;

	private PanelSplit panelSplitLosklasse = null;
	private PanelQuery panelQueryLosklasse = null;
	private PanelLoslosklasse panelDetailLosklasse = null;

	private PanelSplit panelSplitLostechniker = null;
	private PanelQuery panelQueryLostechniker = null;
	private PanelLostechniker panelDetailLostechniker = null;

	private PanelQuery panelQueryFehlmengen = null;
	private PanelSplit panelSplitFehlmengen = null;
	private PanelLosFehlmengen panelDetailFehlmengen = null;

	private PanelBasis panelDetailLoseigenschaft = null;

	private PanelTabelleLosnachkalkulation panelUebersicht = null;

	private PanelSplit panelSplitLoszusatzstatus = null;
	private PanelQuery panelQueryLoszusatzstatus = null;
	private PanelLoszusatzstatus panelDetailLoszusatzstatus = null;

	private PanelDialogKriterienLoszeiten panelDialogKriterienLoszeiten = null;
	private boolean pdKriterienAuftragzeitenUeberMenueAufgerufen = false;
	private PanelTabelleLoszeiten panelTabelleLoszeiten = null;

	private Map mEingeschraenkteFertigungsgruppen = DelegateFactory
			.getInstance().getStuecklisteDelegate()
			.getEingeschraenkteFertigungsgruppen();

	private JMenuBar menuBar = null;

	public static int IDX_AUSWAHL = -1;
	public int IDX_KOPFDATEN = -1;
	public int IDX_MATERIAL = -1;
	public int IDX_FEHLMENGEN = -1;
	public int IDX_ARBEITSPLAN = -1;
	public int IDX_ISTZEITDATEN = -1;
	public int IDX_LAGERENTNAHME = -1;
	public int IDX_ABLIEFERUNG = -1;
	public int IDX_GUTSCHLECHT = -1;
	public int IDX_LOSLOSKLASSE = -1;
	public int IDX_NACHKALKULATION = -1;
	public int IDX_LOSZUSATZSTATUS = -1;
	public int IDX_LOSTECHNIKER = -1;
	public int IDX_PANEL_LOSEIGENSCHAFTEN = -1;

	private LosDto losDto = null;
	private StuecklisteDto stuecklisteDto = null;

	private final static String MENUE_ACTION_BEARBEITEN_AKTUALISIERE_STUECKLISTE = "menu_action_aktualisiere_stueckliste";
	private final static String MENUE_ACTION_BEARBEITEN_AKTUALISIERE_ARBEITSPLAN = "menu_action_aktualisiere_arbeitsplan";
	private final static String MENUE_ACTION_MODUL_DRUCKEN_FERTIGUNGSBEGLEITSCHEIN = "menu_action_fertigungsbegleitschein";
	private final static String MENUE_ACTION_MODUL_DRUCKEN_AUSGABELISTE = "menu_action_ausgabeliste";
	private final static String MENUE_ACTION_MODUL_DRUCKEN_THEORETISCHE_FEHLMENGENLISTE = "menu_action_theoretische_fehlmengenliste";
	private final static String MENUE_ACTION_MODUL_DRUCKEN_PRODUKTIONSINFORMATION = "MENUE_ACTION_MODUL_DRUCKEN_PRODUKTIONSINFORMATION";
	private final static String MENUE_ACTION_BEARBEITEN_PRODUKTIONSSTOP = "menu_action_produktionsstop";
	private final static String MENUE_ACTION_BEARBEITEN_LOSGROESSEAENDERN = "MENUE_ACTION_BEARBEITEN_LOSGROESSEAENDERN";
	private final static String MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN = "MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN";
	private final static String MENUE_ACTION_JOURNAL_ABLIEFERUNGSSTATISTIK = "menu_action_ablieferungsstatistik";
	private final static String MENUE_ACTION_JOURNAL_HALBFERTIGFABRIKATSINVENTUR = "menu_action_halbfertigfabrikatsinventur";
	private final static String MENUE_ACTION_JOURNAL_OFFENE_LOSE = "menu_action_offene_lose";
	private final static String MENUE_ACTION_JOURNAL_OFFENE_AG = "menu_action_offene_ag";
	private final static String MENUE_ACTION_JOURNAL_ALLE_LOSE = "menu_action_alle_lose";
	private final static String MENUE_ACTION_JOURNAL_AUFLOESBARE_FEHLMENGEN = "menu_action_aufloesbare_fehlmengen";
	private final static String MENUE_ACTION_JOURNAL_FEHLMENGEN_ALLER_LOSE = "menu_action_fehlmengen_aller_lose";
	private final static String MENUE_ACTION_JOURNAL_RANKINGLISTE = "MENUE_ACTION_JOURNAL_RANKINGLISTE";
	private final static String MENUE_ACTION_JOURNAL_ZEITENTWICKLUNG = "MENUE_ACTION_JOURNAL_ZEITENTWICKLUNG";
	private final static String MENUE_ACTION_JOURNAL_AUSLASTUNGSVORSCHAU = "MENUE_ACTION_JOURNAL_AUSLASTUNGSVORSCHAU";
	private final static String MENUE_ACTION_JOURNAL_AUSLASTUNGSVORSCHAU_DETAILIERT = "MENUE_ACTION_JOURNAL_AUSLASTUNGSVORSCHAU_DETAILIERT";
	private final static String MENUE_ACTION_JOURNAL_AUSLIEFERLISTE = "MENUE_ACTION_JOURNAL_AUSLIEFERLISTE";
	private final static String MENUE_ACTION_JOURNAL_FEHLERSTATISTIK = "MENUE_ACTION_JOURNAL_FEHLERSTATISTIK";
	private final static String MENUE_ACTION_BEARBEITEN_ALLE_FEHLMENGEN_AUFLOSEN = "MENUE_ACTION_BEARBEITEN_ALLE_FEHLMENGEN_AUFLOSEN";

	static final private String ACTION_SPECIAL_GERAETESNR_KOPIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_geraetesnr_kopieren";

	private final static String ACTION_SPECIAL_MATERIALENTNAHME = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_materialentnahme";
	private final static String ACTION_SPECIAL_MATERIALENTNAHME_OHNE_SOLLPOSITION = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_materialentnahme_ohne_sollposition";
	private final static String ACTION_SPECIAL_ISTMENGE_AENDERN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_istmengeaendern";
	private final static String MENUE_ACTION_JOURNAL_STUECKRUECKMELDUNG = "menu_action_stueckrueckmeldung";
	private static final String ACTION_SPECIAL_ABLIEFERUNGEN_NEU_KALKULIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_ablieferungen_neu_kalkulieren";
	private static final String ACTION_SPECIAL_SOLLPREISE_MATERIAL_NEU_KALKULIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_solpreise_material_neu_kalkulieren";
	private final static String ACTION_SPECIAL_TECHNIKER = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_TECHNIKER_SPECIAL_BEARBEITEN";

	private final String MENU_ACTION_JOURNAL_STATISTIK = "MENU_ACTION_JOURNAL_STATISTIK";
	private final String MENU_ACTION_JOURNAL_MONATSAUSWERTUNG = "MENU_ACTION_JOURNAL_MONATSAUSWERTUNG";
	private final String MENU_BEARBEITEN_KOMMENTAR = "MENU_BEARBEITEN_KOMMENTAR";
	private final String MENU_BEARBEITEN_PRODUKTIONSINFORMATION = "MENU_BEARBEITEN_PRODUKTIONSINFORMATION";
	private final String MENU_ACTION_BEARBEITEN_BEWERTUNG = "MENU_ACTION_BEARBEITEN_BEWERTUNG";
	private final String MENUE_ACTION_MEHRERE_LOSE_AUSGEBEN_FTG_GRUPPE = "MENUE_ACTION_MEHRERE_LOSE_AUSGEBEN_FTG_GRUPPE";
	private final String MENUE_ACTION_MEHRERE_LOSE_AUSGEBEN_ENTHALTENE_STKL = "MENUE_ACTION_MEHRERE_LOSE_AUSGEBEN_ENTHALTENE_STKL";

	private final String MENUE_ACTION_ZUSATZSTATUS = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_ZUSATZSTATUS";

	private final String MENUE_ACTION_AUFTRAGERLEDIGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_AUFTRAGERLEDIGEN";
	private final String MENUE_ACTION_TOPSABLIEFERN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_TOPSABLIEFERN";
	private static final String ACTION_SPECIAL_AUSGEBEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_los_ausgeben";
	private static final String ACTION_SPECIAL_NEU_ANHAND_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_NEU_ANHAND_AUFTRAG";

	private static final String ACTION_SPECIAL_FEHLMENGEN_DRUCKEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_fehlmengen_drucken";

	private final static String MENUE_ACTION_MODUL_DRUCKEN_ETIKETT = "menue_action_etikett";
	private final static String MENUE_ACTION_MODUL_DRUCKEN_ETIKETT_A4 = "menue_action_etikett_a4";

	private PanelDialogLosKommentar pdStuecklisteKommentar = null;
	private PanelDialogLosProduktionsinformation pdProduktionsinformation = null;

	private final static String MENU_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN";
	private final static String MENU_BEARBEITEN_ZUORDNUNG = "MENU_BEARBEITEN_ZUORDNUNG";

	private final static String EXTRA_ARTIKELBILD = PanelBasis.ACTION_MY_OWN_NEW
			+ "EXTRA_ARTIKELBILD";

	private final static String MENUE_ACTION_INFO_GESAMTKALKULATION = "menu_action_gesamtkalkulation";

	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;
	private PanelQueryFLR panelQueryFLRAuftragauswahl = null;

	public String sRechtModulweit = null;

	private PanelQueryFLR panelQueryFLRTechniker = null;

	protected void setLosDto(LosDto losDto) throws Throwable {
		this.losDto = losDto;
		refreshFilterMaterial();
		refreshFilterZeitdaten();
		refreshFilterLagerentnahme();
		refreshFilterLoszusatzstatus();
		refreshFilterLostechniker();
		refreshFilterLoslosklasse();
		refreshFilterFehlmengen();
		refreshFilterLosablieferung();
		refreshFilterLosgutschlecht();
		// Titel
		String sTitle;
		if (getLosDto() != null) {
			// lock key setzen
			if (losDto.getIId() != null) {
				// getInternalFrame().setKeyWasForLockMe(losDto.getIId().toString
				// ());
			}
			sTitle = getLosDto().getCNr();

			if (getLosDto().getStuecklisteIId() != null) {
				setStuecklisteDto(DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(
								getLosDto().getStuecklisteIId()));

				sTitle += " " + getStuecklisteDto().getArtikelDto().getCNr();

			} else {
				setStuecklisteDto(null);
			}

			if (getLosDto().getCProjekt() != null) {
				sTitle = sTitle + " " + getLosDto().getCProjekt();
			}

		} else {
			sTitle = "";
			setStuecklisteDto(null);
		}
		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	public LosDto getLosDto() {
		return losDto;
	}

	public void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {

		boolean bRahmenauftraegeVerwendbar = false;
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_RAHMENAUFTRAEGE_IN_FERTIGUNG_VERWENDBAR);

		if ((Boolean) parameter.getCWertAsObject()) {
			bRahmenauftraegeVerwendbar = true;
		}

		FilterKriterium[] fk = LieferscheinFilterFactory
				.getInstance()
				.createFKPanelQueryFLRAuftragAuswahl(bRahmenauftraegeVerwendbar);
		panelQueryFLRAuftragauswahl = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), true, false, fk);
		new DialogQuery(panelQueryFLRAuftragauswahl);
	}

	protected void setStuecklisteDto(StuecklisteDto stuecklisteDto) {
		this.stuecklisteDto = stuecklisteDto;
	}

	private void refreshPdKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdStuecklisteKommentar = new PanelDialogLosKommentar(
				getInternalFrame(), LPMain.getTextRespectUISPr("lp.kommentar"));
	}

	private void refreshPdProduktionsinformation() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdProduktionsinformation = new PanelDialogLosProduktionsinformation(
				getInternalFrame(), LPMain.getTextRespectUISPr("lp.kommentar"));
	}

	protected StuecklisteDto getStuecklisteDto() {
		return stuecklisteDto;
	}

	public TabbedPaneLos(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("fert.tab.unten.los.title"));
		sRechtModulweit = getInternalFrame().getRechtModulweit();
		jbInit();
		initComponents();
	}

	public String getRechtModulweit() {
		return sRechtModulweit;
	}

	private void jbInit() throws Throwable {
		int tabIndex = 0;
		IDX_AUSWAHL = tabIndex;
		// Tab 1: Kassenbuecher
		insertTab(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"), IDX_AUSWAHL);
		tabIndex++;
		IDX_KOPFDATEN = tabIndex;
		// Tab 2: Kopfdaten
		insertTab(LPMain.getTextRespectUISPr("lp.kopfdaten"), null, null,
				LPMain.getTextRespectUISPr("lp.kopfdaten"), IDX_KOPFDATEN);
		tabIndex++;
		IDX_MATERIAL = tabIndex;
		// Tab 3: Material
		insertTab(LPMain.getTextRespectUISPr("fert.tab.oben.material.title"),
				null, null,
				LPMain.getTextRespectUISPr("fert.tab.oben.material.tooltip"),
				IDX_MATERIAL);

		// PJ17777
		if (mEingeschraenkteFertigungsgruppen == null) {

			tabIndex++;
			IDX_FEHLMENGEN = tabIndex;
			// Tab 4: Fehlmengen
			insertTab(
					LPMain.getTextRespectUISPr("fert.tab.oben.fehlmengen.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("fert.tab.oben.fehlmengen.tooltip"),
					IDX_FEHLMENGEN);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_ZEITERFASSUNG)) {
				// Tab 5: Zeitdaten
				tabIndex++;
				IDX_ARBEITSPLAN = tabIndex;
				insertTab(
						LPMain.getTextRespectUISPr("fert.tab.oben.zeitdaten.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("fert.tab.oben.zeitdaten.tooltip"),
						IDX_ARBEITSPLAN);

				// Tab 9: GutSchlecht

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_STUECKRUECKMELDUNG)) {
					tabIndex++;
					IDX_GUTSCHLECHT = tabIndex;
					insertTab(LPMain.getTextRespectUISPr("fert.gutschlecht"),
							null, null,
							LPMain.getTextRespectUISPr("fert.gutschlecht"),
							IDX_GUTSCHLECHT);

				}

				// Tab 6: Ist-Zeitdaten
				tabIndex++;
				IDX_ISTZEITDATEN = tabIndex;
				insertTab(
						LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.tooltip"),
						IDX_ISTZEITDATEN);
			}
			// Tab 7: Lagerentnahmen
			tabIndex++;
			IDX_LAGERENTNAHME = tabIndex;
			insertTab(
					LPMain.getTextRespectUISPr("fert.tab.oben.lagerentnahme.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("fert.tab.oben.lagerentnahme.tooltip"),
					IDX_LAGERENTNAHME);
		}
		// Tab 8: Ablieferungen
		tabIndex++;
		IDX_ABLIEFERUNG = tabIndex;
		insertTab(
				LPMain.getTextRespectUISPr("fert.tab.oben.ablieferung.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("fert.tab.oben.ablieferung.tooltip"),
				IDX_ABLIEFERUNG);

		// PJ17777
		if (mEingeschraenkteFertigungsgruppen == null) {
			// Tab 9: Losklassen
			tabIndex++;
			IDX_LOSLOSKLASSE = tabIndex;
			insertTab(
					LPMain.getTextRespectUISPr("fert.tab.oben.losklassen.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("fert.tab.oben.losklassen.tooltip"),
					IDX_LOSLOSKLASSE);
			// Tab 10: Nachkalkulation
			tabIndex++;
			IDX_NACHKALKULATION = tabIndex;
			insertTab(
					LPMain.getTextRespectUISPr("fert.tab.oben.nachkalkulation.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("fert.tab.oben.nachkalkulation.tooltip"),
					IDX_NACHKALKULATION);
			tabIndex++;
			IDX_LOSZUSATZSTATUS = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("fert.zusatzstatus"), null,
					null, LPMain.getTextRespectUISPr("fert.zusatzstatus"),
					IDX_LOSZUSATZSTATUS);
			tabIndex++;
			IDX_LOSTECHNIKER = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("fert.lostechniker"), null,
					null, LPMain.getTextRespectUISPr("fert.lostechniker"),
					IDX_LOSTECHNIKER);
		}

		// Wenn keine Panelbeschriebung vorhanden, dann ausblenden
		PanelbeschreibungDto[] dtos = DelegateFactory
				.getInstance()
				.getPanelDelegate()
				.panelbeschreibungFindByPanelCNrMandantCNr(
						PanelFac.PANEL_LOSEIGENSCHAFTEN, null);
		if (dtos != null && dtos.length > 0) {
			tabIndex++;
			IDX_PANEL_LOSEIGENSCHAFTEN = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("lp.eigenschaften"), null,
					null, LPMain.getTextRespectUISPr("lp.eigenschaften"),
					IDX_PANEL_LOSEIGENSCHAFTEN);
		}

		setSelectedComponent(getPanelQueryAuswahl(true));
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * getPanelQueryAuswahl mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	public PanelQuery getPanelQueryAuswahl(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryAuswahl == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseEingangsrechnung = { PanelBasis.ACTION_NEW };

			ArbeitsplatzparameterDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_FERTIGUNG_ANSICHT_OFFENE_LOSE);

			if (parameter != null && parameter.getCWert() != null
					&& parameter.getCWert().equals("1")) {

				panelQueryAuswahl = new PanelQuery(null, FertigungFilterFactory
						.getInstance().createFKLosAuswahlOffene(),
						QueryParameters.UC_ID_LOS,
						aWhichButtonIUseEingangsrechnung, getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.auswahl"), true);
			} else {
				panelQueryAuswahl = new PanelQuery(null, FertigungFilterFactory
						.getInstance().createFKLosAuswahl(),
						QueryParameters.UC_ID_LOS,
						aWhichButtonIUseEingangsrechnung, getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.auswahl"), true);
			}

			FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory
					.getInstance().createFKDLosnummer();

			FilterKriteriumDirekt fkDirekt2 = null;

			ParametermandantDto parameterAuftragStattbezeichnung = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE,
							ParameterFac.KATEGORIE_FERTIGUNG,
							LPMain.getTheClient().getMandant());
			if ((Boolean) parameterAuftragStattbezeichnung.getCWertAsObject()) {
				fkDirekt2 = FertigungFilterFactory.getInstance()
						.createFKDProjektDesLoses();
			} else {
				fkDirekt2 = FertigungFilterFactory.getInstance()
						.createFKDArtikelnummer();
			}

			panelQueryAuswahl.befuellePanelFilterkriterienDirekt(fkDirekt1,
					fkDirekt2);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
				panelQueryAuswahl.addDirektFilter(FertigungFilterFactory
						.getInstance().createFKDLosProjektnummerAusAuftrag());
			} else {

				ParametermandantDto parameterLo = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);

				int iLosnummerAuftragsbezogen = (Integer) parameterLo
						.getCWertAsObject();

				if (iLosnummerAuftragsbezogen >= 1) {
					panelQueryAuswahl.addDirektFilter(FertigungFilterFactory
							.getInstance().createFKDVolltextsucheArtikel());
				} else {
					panelQueryAuswahl.addDirektFilter(FertigungFilterFactory
							.getInstance().createFKDLosAuftagsnummer());
				}

			}

			panelQueryAuswahl.addDirektFilter(FertigungFilterFactory
					.getInstance().createFKDLosKunde());

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

				panelQueryAuswahl.addDirektFilter(FertigungFilterFactory
						.getInstance().createFKDLosProjektbezeichnung());

			}

			// PJ17681
			if (mEingeschraenkteFertigungsgruppen != null) {
				panelQueryAuswahl.setFilterComboBox(
						mEingeschraenkteFertigungsgruppen, new FilterKriterium(
								"flrlos.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false), true);
			} else {
				panelQueryAuswahl.setFilterComboBox(DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.getAllFertigungsgrupe(), new FilterKriterium(
						"flrlos.fertigungsgruppe_i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false, LPMain
						.getTextRespectUISPr("lp.alle"));
			}
			panelQueryAuswahl
					.befuelleFilterkriteriumSchnellansicht(FertigungFilterFactory
							.getInstance().createFKLosAuswahlOffene());

			// Hier den zusaetzlichen Button aufs Panel bringen
			panelQueryAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/graphics-tablet.png",
					LPMain.getTextRespectUISPr("fert.artikelbild"),
					EXTRA_ARTIKELBILD, null);

			panelQueryAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/data_next.png",
					LPMain.getTextRespectUISPr("fert.tooltip.losausgeben"),
					ACTION_SPECIAL_AUSGEBEN, RechteFac.RECHT_FERT_LOS_CUD);

			parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_FERTIGUNG_ZUSATZSTATUS);

			if (parameter != null && parameter.getCWert() != null
					&& parameter.getCWert().equals("1")) {
				panelQueryAuswahl.createAndSaveAndShowButton(
						"/com/lp/client/res/scanner16x16.png",
						LPMain.getTextRespectUISPr("fert.zusatzstatuserfassen")
								+ " F12", MENUE_ACTION_ZUSATZSTATUS,
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12,
								0), null);

			}

			parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_AUFTRAGSABLIEFERUNG_IM_LOS);
			if (parameter != null) {
				panelQueryAuswahl.createAndSaveAndShowButton(
						"/com/lp/client/res/document_check16x16.png",
						LPMain.getTextRespectUISPr("fert.auftragabliefern")
								+ " F11", MENUE_ACTION_AUFTRAGERLEDIGEN,
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11,
								0), null);
			}

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_TOPS_ANBINDUNG)) {

				panelQueryAuswahl
						.createAndSaveAndShowButton(
								"/com/lp/client/res/laserpointer.png",
								LPMain.getTextRespectUISPr("fert.topsabliefern")
										+ " F9", MENUE_ACTION_TOPSABLIEFERN,
								KeyStroke.getKeyStroke(
										java.awt.event.KeyEvent.VK_F9, 0), null);
			}

			panelQueryAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/auftrag16x16.png",
					LPMain.getTextRespectUISPr("fert.neuanhandauftrag"),
					ACTION_SPECIAL_NEU_ANHAND_AUFTRAG, null);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_ZEITERFASSUNG)) {

				// PJ 17816
				boolean hatRecht = DelegateFactory.getInstance()
						.getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);
				if (hatRecht) {

					ParametermandantDto parameterVB = DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getMandantparameter(
									LPMain.getTheClient().getMandant(),
									ParameterFac.KATEGORIE_PERSONAL,
									ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
					boolean bVonBisErfassung = (java.lang.Boolean) parameterVB
							.getCWertAsObject();

					// SP2352
					if (bVonBisErfassung == false) {

						panelQueryAuswahl.getToolBar().addButtonLeft(
								"/com/lp/client/res/gear_run.png",
								LPMain.getTextRespectUISPr("proj.startzeit"),
								Desktop.MY_OWN_NEW_ZEIT_START, null, null);
						panelQueryAuswahl.getToolBar().addButtonLeft(
								"/com/lp/client/res/gear_stop.png",
								LPMain.getTextRespectUISPr("proj.stopzeit"),
								Desktop.MY_OWN_NEW_ZEIT_STOP, null, null);

						((LPButtonAction) panelQueryAuswahl.getHmOfButtons()
								.get(Desktop.MY_OWN_NEW_ZEIT_START))
								.getButton().setEnabled(true);
						((LPButtonAction) panelQueryAuswahl.getHmOfButtons()
								.get(Desktop.MY_OWN_NEW_ZEIT_STOP)).getButton()
								.setEnabled(true);
					}

				}
			}
			setComponentAt(IDX_AUSWAHL, panelQueryAuswahl);
		}
		return panelQueryAuswahl;
	}

	/**
	 * getPanelQueryAuswahl mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	private PanelSplit getPanelSplitFehlmengen(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitFehlmengen == null && bNeedInstantiationIfNull) {
			panelSplitFehlmengen = new PanelSplit(getInternalFrame(),
					getPanelDetailFehlmengen(true),
					getPanelQueryFehlmengen(true), 370);
			setComponentAt(IDX_FEHLMENGEN, panelSplitFehlmengen);

		}
		return panelSplitFehlmengen;
	}

	private PanelQuery getPanelQueryFehlmengen(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryFehlmengen == null && bNeedInstantiationIfNull) {
			panelQueryFehlmengen = FertigungFilterFactory.getInstance()
					.createPanelFehlmengen(getInternalFrame(),
							getLosDto().getIId());

			panelQueryFehlmengen.createAndSaveAndShowButton(
					"/com/lp/client/res/printer.png", LPMain
							.getTextRespectUISPr("lp.printer"),
					ACTION_SPECIAL_FEHLMENGEN_DRUCKEN, KeyStroke.getKeyStroke(
							'P', java.awt.event.InputEvent.CTRL_MASK), null);

		}
		return panelQueryFehlmengen;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// da will jemand die Buchungen ansehen
			if (e.getSource() == getPanelQueryAuswahl(false)) {
				Object key = getPanelQueryAuswahl(true).getSelectedId();
				holeLosDto((Integer) key);
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					setSelectedComponent(getPanelSplitMaterial(true));
				}
			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				// Neu aus Partner.
				Integer fertigungsgruppeIId = (Integer) panelQueryFLRFertigungsgruppe
						.getSelectedId();
				DelegateFactory.getInstance().getFertigungDelegate()
						.gebeMehrereLoseAus(fertigungsgruppeIId);
			} else if (e.getSource() == panelQueryFLRTechniker) {
				// Neu aus Partner.
				Object[] ids = panelQueryFLRTechniker.getSelectedIds();
				for (int i = 0; i < ids.length; i++) {

					LostechnikerDto lostechnikerDto = new LostechnikerDto();
					lostechnikerDto.setPersonalIId((Integer) ids[i]);
					lostechnikerDto.setLosIId(getLosDto().getIId());

					try {
						DelegateFactory.getInstance().getFertigungDelegate()
								.createLostechniker(lostechnikerDto);
					} catch (Exception e1) {
						// Bei Fehler auslassen
					}
				}
				getPanelQueryLostechniker(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryFLRAuftragauswahl) {
				Integer iIdAuftrag = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AuftragDto auftragDto = DelegateFactory.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftrag);

				getInternalFrame()
						.showPanelDialog(
								new PanelDialogLoseAusAuftrag(
										getInternalFrame(),
										iIdAuftrag,
										LPMain.getTextRespectUISPr("fert.neuanhandauftrag")
												+ " " + auftragDto.getCNr()));

			} else if (e.getSource() == panelQueryFehlmengen) {
				// PJ 16017
				Integer key = (Integer) panelQueryFehlmengen.getSelectedId();
				if (key != null) {

					ArtikelfehlmengeDto artikelfehlmengeDto = DelegateFactory
							.getInstance().getFehlmengeDelegate()
							.artikelfehlmengeFindByPrimaryKey(key);
					ArtikelDto artikelDto = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									artikelfehlmengeDto.getArtikelIId());
					String add2Title = LPMain
							.getTextRespectUISPr("artikel.report.artikelbestellt");
					getInternalFrame().showReportKriterien(
							new ReportArtikelbestellt(getInternalFrame(),
									artikelDto, add2Title));
				}
			}

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryAuswahl(false)) {
				Object key = getPanelQueryAuswahl(true).getSelectedId();
				holeLosDto((Integer) key);
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_AUSWAHL, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_AUSWAHL, true);
				}
				getPanelQueryAuswahl(true).updateButtons();
			} else if (e.getSource() == getPanelQueryLagerentnahme(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailLagerentnahme(true).setKeyWhenDetailPanel(key);
				getPanelDetailLagerentnahme(true).eventYouAreSelected(false);
				getPanelQueryLagerentnahme(true).updateButtons();
			} else if (e.getSource() == getPanelQueryLoszusatzstatus(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailLoszusatzstatus(true).setKeyWhenDetailPanel(key);
				getPanelDetailLoszusatzstatus(true).eventYouAreSelected(false);
				getPanelQueryLoszusatzstatus(true).updateButtons();
			} else if (e.getSource() == getPanelQueryMaterial(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailMaterial(true).setKeyWhenDetailPanel(key);
				getPanelDetailMaterial(true).eventYouAreSelected(false);
				getPanelQueryMaterial(true).updateButtons();
			} else if (e.getSource() == getPanelQueryZeitdaten(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailZeitdaten(true).setKeyWhenDetailPanel(key);
				getPanelDetailZeitdaten(true).eventYouAreSelected(false);
				getPanelQueryZeitdaten(true).updateButtons();
			} else if (e.getSource() == getPanelQueryLoslosklasse(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailLoslosklasse(true).setKeyWhenDetailPanel(key);
				getPanelDetailLoslosklasse(true).eventYouAreSelected(false);
				getPanelQueryLoslosklasse(true).updateButtons();
			} else if (e.getSource() == getPanelQueryLostechniker(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailLostechniker(true).setKeyWhenDetailPanel(key);
				getPanelDetailLostechniker(true).eventYouAreSelected(false);
				getPanelQueryLostechniker(true).updateButtons();
			} else if (e.getSource() == getPanelQueryAblieferung(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailAblieferung(true).setKeyWhenDetailPanel(key);
				getPanelDetailAblieferung(true).eventYouAreSelected(false);
				getPanelQueryAblieferung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryLosgutschlecht(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailLosGutSchlecht(true).setKeyWhenDetailPanel(key);
				getPanelDetailLosGutSchlecht(true).eventYouAreSelected(false);
				getPanelQueryLosgutschlecht(true).updateButtons();
			} else if (e.getSource() == getPanelQueryFehlmengen(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailFehlmengen(true).setKeyWhenDetailPanel(key);
				getPanelDetailFehlmengen(true).eventYouAreSelected(false);
				getPanelQueryFehlmengen(true).updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryAuswahl(false)) {
				if (getPanelQueryAuswahl(true).getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				getPanelKopfdaten(true).eventActionNew(null, true, false);
				setSelectedComponent(getPanelKopfdaten(true));
			} else if (e.getSource() == getPanelQueryMaterial(false)) {
				getPanelDetailMaterial(true).eventActionNew(e, true, false);
				getPanelDetailMaterial(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryZeitdaten(false)) {
				getPanelDetailZeitdaten(true).eventActionNew(e, true, false);
				getPanelDetailZeitdaten(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryLagerentnahme(false)) {
				getPanelDetailLagerentnahme(true)
						.eventActionNew(e, true, false);
				getPanelDetailLagerentnahme(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryLoszusatzstatus(false)) {
				getPanelDetailLoszusatzstatus(true).eventActionNew(e, true,
						false);
				getPanelDetailLoszusatzstatus(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryLostechniker(false)) {
				getPanelDetailLostechniker(true).eventActionNew(e, true, false);
				getPanelDetailLostechniker(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryLoslosklasse(false)) {
				getPanelDetailLoslosklasse(true).eventActionNew(e, true, false);
				getPanelDetailLoslosklasse(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryLosgutschlecht(false)) {
				getPanelDetailLosGutSchlecht(true).eventActionNew(e, true,
						false);
				getPanelDetailLosGutSchlecht(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryAblieferung(false)) {
				// vorher Status pruefen
				if (getLosDto() == null) {
					throw new ExceptionLP(EJBExceptionLP.FEHLER, new Exception(
							"losDto = null"));
				} else {
					if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_STORNIERT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
								new Exception(getLosDto().getCNr()
										+ " ist storniert"));
					}
					if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_ANGELEGT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
								new Exception(getLosDto().getCNr()
										+ " ist noch nicht ausgegeben"));
					}
					if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_GESTOPPT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT,
								new Exception(getLosDto().getCNr()
										+ " ist gestoppt"));
					}
				}
				// alles ok -> weiter gehts
				getPanelDetailAblieferung(true).eventActionNew(e, true, false);
				getPanelDetailAblieferung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelKopfdaten(false)) {
				// nix
			} else if (e.getSource() == getPanelDetailMaterial(false)) {
				getPanelQueryMaterial(true).updateButtons();
				getPanelDetailMaterial(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailZeitdaten(false)) {
				getPanelQueryZeitdaten(true).updateButtons();
				getPanelDetailZeitdaten(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLagerentnahme(false)) {
				getPanelQueryLagerentnahme(true).updateButtons();
				getPanelDetailLagerentnahme(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLoszusatzstatus(false)) {
				getPanelQueryLoszusatzstatus(true).updateButtons();
				getPanelDetailLoszusatzstatus(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLostechniker(false)) {
				getPanelQueryLostechniker(true).updateButtons();
				getPanelDetailLostechniker(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLoslosklasse(false)) {
				getPanelQueryLoslosklasse(true).updateButtons();
				getPanelDetailLoslosklasse(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailAblieferung(false)) {
				getPanelQueryAblieferung(true).updateButtons();
				getPanelDetailAblieferung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailAblieferung(false)) {
				getPanelQueryLosgutschlecht(true).updateButtons();
				getPanelQueryLosgutschlecht(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelKopfdaten(false)) {
				getPanelQueryAuswahl(true).clearDirektFilter();
				Object key = getPanelKopfdaten(true).getKeyWhenDetailPanel();
				getPanelQueryAuswahl(true).eventYouAreSelected(false);
				getPanelQueryAuswahl(true).setSelectedId(key);
				getPanelQueryAuswahl(true).eventYouAreSelected(false);
			}
			if (e.getSource() == getPanelDetailMaterial(false)) {
				Object key = getPanelDetailMaterial(true)
						.getKeyWhenDetailPanel();
				getPanelQueryMaterial(true).eventYouAreSelected(false);
				getPanelQueryMaterial(true).setSelectedId(key);
				getPanelQueryMaterial(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailZeitdaten(false)) {
				Object key = getPanelDetailZeitdaten(true)
						.getKeyWhenDetailPanel();
				getPanelQueryZeitdaten(true).eventYouAreSelected(false);
				getPanelQueryZeitdaten(true).setSelectedId(key);
				getPanelQueryZeitdaten(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLagerentnahme(false)) {
				Object key = getPanelDetailLagerentnahme(true)
						.getKeyWhenDetailPanel();
				getPanelQueryLagerentnahme(true).eventYouAreSelected(false);
				getPanelQueryLagerentnahme(true).setSelectedId(key);
				getPanelQueryLagerentnahme(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLoszusatzstatus(false)) {
				Object key = getPanelDetailLoszusatzstatus(true)
						.getKeyWhenDetailPanel();
				getPanelQueryLoszusatzstatus(true).eventYouAreSelected(false);
				getPanelQueryLoszusatzstatus(true).setSelectedId(key);
				getPanelQueryLoszusatzstatus(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLoslosklasse(false)) {
				Object key = getPanelDetailLoslosklasse(true)
						.getKeyWhenDetailPanel();
				getPanelQueryLoslosklasse(true).eventYouAreSelected(false);
				getPanelQueryLoslosklasse(true).setSelectedId(key);
				getPanelQueryLoslosklasse(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLostechniker(false)) {
				Object key = getPanelDetailLostechniker(true)
						.getKeyWhenDetailPanel();
				getPanelQueryLostechniker(true).eventYouAreSelected(false);
				getPanelQueryLostechniker(true).setSelectedId(key);
				getPanelQueryLostechniker(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailAblieferung(false)) {
				Object key = getPanelDetailAblieferung(true)
						.getKeyWhenDetailPanel();
				getPanelQueryAblieferung(true).eventYouAreSelected(false);
				getPanelQueryAblieferung(true).setSelectedId(key);
				getPanelQueryAblieferung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLosGutSchlecht(false)) {
				Object key = getPanelDetailLosGutSchlecht(true)
						.getKeyWhenDetailPanel();
				getPanelQueryLosgutschlecht(true).eventYouAreSelected(false);
				getPanelQueryLosgutschlecht(true).setSelectedId(key);
				getPanelQueryLosgutschlecht(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelKopfdaten(false)) {
				setLosDto(null);
				getPanelQueryAuswahl(true).eventYouAreSelected(false);
				this.setSelectedComponent(getPanelQueryAuswahl(true));
			} else if (e.getSource() == getPanelDetailMaterial(false)) {
				if (getPanelDetailMaterial(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryMaterial(true)
							.getId2SelectAfterDelete();
					getPanelQueryMaterial(true).setSelectedId(oNaechster);
				}
				setKeyWasForLockMe();
				getPanelSplitMaterial(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailZeitdaten(false)) {
				if (getPanelDetailZeitdaten(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryZeitdaten(true)
							.getId2SelectAfterDelete();
					getPanelQueryZeitdaten(true).setSelectedId(oNaechster);
				}
				setKeyWasForLockMe();
				getPanelSplitZeitdaten(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLagerentnahme(false)) {
				if (getPanelDetailLagerentnahme(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryLagerentnahme(true)
							.getId2SelectAfterDelete();
					getPanelQueryLagerentnahme(true).setSelectedId(oNaechster);
				}
				setKeyWasForLockMe();
				getPanelSplitLagerentnahme(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLoszusatzstatus(false)) {
				if (getPanelDetailLoszusatzstatus(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryLoszusatzstatus(true)
							.getId2SelectAfterDelete();
					getPanelQueryLoszusatzstatus(true)
							.setSelectedId(oNaechster);
				}
				setKeyWasForLockMe();
				getPanelSplitLoszusatzstatus(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLoslosklasse(false)) {
				setKeyWasForLockMe();
				getPanelSplitLoslosklasse(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailAblieferung(false)) {
				setKeyWasForLockMe();
				getPanelSplitAblieferung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLosGutSchlecht(false)) {
				setKeyWasForLockMe();
				getPanelSplitLosgutschlecht(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailLostechniker(false)) {
				setKeyWasForLockMe();
				getPanelSplitLostechniker(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == getPanelDetailAblieferung(true)) {
				getPanelQueryAblieferung(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelDetailLagerentnahme(true)) {
				getPanelQueryLagerentnahme(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelDetailLoszusatzstatus(true)) {
				getPanelQueryLoszusatzstatus(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelDetailLoslosklasse(true)) {
				getPanelQueryLoslosklasse(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelDetailMaterial(true)) {
				getPanelQueryMaterial(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelDetailZeitdaten(true)) {
				getPanelQueryZeitdaten(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelDetailAblieferung(true)) {
				getPanelQueryAblieferung(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelDetailLosGutSchlecht(true)) {
				getPanelQueryLosgutschlecht(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelDetailLostechniker(true)) {
				getPanelQueryLostechniker(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		}
		// der OK Button in einem PanelDialog wurde gedrueckt
		else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getKriterienLoszeiten(false)) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getKriterienLoszeiten(true)
						.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				getPanelTabelleLoszeiten().setDefaultFilter(aAlleKriterien);

				// die Auftragzeiten aktualisieren
				getPanelTabelleLoszeiten().eventYouAreSelected(false);

				// man steht auf alle Faelle in den Auftragzeiten
				setSelectedComponent(getPanelTabelleLoszeiten());
				pdKriterienAuftragzeitenUeberMenueAufgerufen = false;
				getPanelTabelleLoszeiten().updateButtons(
						new LockStateValue(null, null,
								PanelBasis.LOCK_IS_NOT_LOCKED));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.equals(EXTRA_ARTIKELBILD)) {
				// Dialog mit Bilder anzeigen
				if (getLosDto() != null) {
					if (getLosDto().getStuecklisteIId() != null) {
						Integer artikeklIId = DelegateFactory
								.getInstance()
								.getStuecklisteDelegate()
								.stuecklisteFindByPrimaryKey(
										getLosDto().getStuecklisteIId())
								.getArtikelIId();
						new DialogArtikelbilder(getInternalFrame().getSize(),
								artikeklIId);
					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("fert.artikelbild.materialliste.error"));
					}
				}
			} else if (sAspectInfo.equals(Desktop.MY_OWN_NEW_ZEIT_START)
					|| sAspectInfo.equals(Desktop.MY_OWN_NEW_ZEIT_STOP)) {

				LPMain.getInstance()
						.getDesktop()
						.zeitbuchungAufBeleg(sAspectInfo,
								LocaleFac.BELEGART_LOS, getLosDto().getIId());

			} else if (sAspectInfo.equals(ACTION_SPECIAL_AUSGEBEN)) {
				// los aktualisieren und Stuecklistenaenderungen pruefen
				// getTabbedPaneLos().reloadLosDto();
				int iAnswer = pruefeStuecklisteAktuellerAlsLosDlg(getLosDto()
						.getIId());
				if (iAnswer != JOptionPane.CANCEL_OPTION) {
					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.gebeLosAus(
									getLosDto().getIId(),
									false,
									getAbzubuchendeSeriennrChargen(getLosDto()
											.getIId(), getLosDto()
											.getNLosgroesse(), false));
					// refresh aufs panel
					printAusgabelisteUndFertigungsbegleitscheinDlg(getLosDto()
							.getIId(), true);
				}

			} else if (sAspectInfo.equals(ACTION_SPECIAL_GERAETESNR_KOPIEREN)) {
				LossollmaterialDto lossollmaterialDtoArtikel = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.getArtikelIIdOffenenSeriennummernBeiGeraeteseriennummer(
								getLosDto().getIId());

				if (lossollmaterialDtoArtikel != null) {
					List<SeriennrChargennrMitMengeDto> l = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.getOffenenSeriennummernBeiGeraeteseriennummer(
									getLosDto().getIId());

					DialogSnrauswahl d = new DialogSnrauswahl(
							SeriennrChargennrMitMengeDto
									.erstelleStringArrayAusMehrerenSeriennummern(l));

					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);

					String[] cSelectedSnrs = d.sSeriennrArray;
					d.dispose();

					if (cSelectedSnrs != null && cSelectedSnrs.length > 0) {
						BigDecimal bdErledigt = DelegateFactory.getInstance()
								.getFertigungDelegate()
								.getErledigteMenge(getLosDto().getIId());

						LosablieferungDto losablieferungDto = new LosablieferungDto();
						losablieferungDto.setLosIId(getLosDto().getIId());
						losablieferungDto.setNMenge(new BigDecimal(
								cSelectedSnrs.length));

						boolean bErledigt = false;
						List<SeriennrChargennrMitMengeDto> lTemp = new ArrayList<SeriennrChargennrMitMengeDto>();

						for (int i = 0; i < cSelectedSnrs.length; i++) {
							String gsnrs = cSelectedSnrs[i];

							SeriennrChargennrMitMengeDto snr = new SeriennrChargennrMitMengeDto();
							snr.setCSeriennrChargennr(gsnrs);

							// SP2841 -> Version hinzufuegen

							for (int x = 0; x < l.size(); x++) {
								if (l.get(x).getCSeriennrChargennr()
										.equals(gsnrs)) {
									snr.setCVersion(l.get(x).getCVersion());
									break;
								}
							}

							snr.setNMenge(new BigDecimal(1));

							ArrayList<GeraetesnrDto> lGs = new ArrayList<GeraetesnrDto>();
							GeraetesnrDto gsnrDto = new GeraetesnrDto();
							gsnrDto.setCSnr(gsnrs);
							gsnrDto.setArtikelIId(lossollmaterialDtoArtikel
									.getArtikelIId());
							lGs.add(gsnrDto);

							snr.setAlGeraetesnr(lGs);
							lTemp.add(snr);

							bdErledigt = bdErledigt.add(new BigDecimal(1));

							BigDecimal offen = getLosDto().getNLosgroesse()
									.subtract(bdErledigt);

							if (offen.doubleValue() <= 0
									&& i == cSelectedSnrs.length - 1) {
								bErledigt = true;

							}

						}
						losablieferungDto.setSeriennrChargennrMitMenge(lTemp);
						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.createLosablieferung(losablieferungDto,
										bErledigt);

						panelSplitAblieferung.eventYouAreSelected(false);

					}
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hinweis"),
									LPMain.getTextRespectUISPr("fert.geraetesnruebernahme.nichtmoeglich"));
				}
			} else if (sAspectInfo.equals(ACTION_SPECIAL_FEHLMENGEN_DRUCKEN)) {

				if (getLosDto() != null) {
					getInternalFrame()
							.showReportKriterien(
									new ReportFehlteilliste(
											getInternalFrame(),
											getLosDto().getIId(),
											LPMain.getTextRespectUISPr("fert.report.fehlteilliste")));
				}

			} else if (sAspectInfo.equals(ACTION_SPECIAL_TECHNIKER)) {

				panelQueryFLRTechniker = PersonalFilterFactory.getInstance()
						.createPanelFLRPersonalMitKostenstelle(
								getInternalFrame(), true, true, null);
				panelQueryFLRTechniker.setMultipleRowSelectionEnabled(true);
				new DialogQuery(panelQueryFLRTechniker);

			} else if (sAspectInfo.equals(MENUE_ACTION_TOPSABLIEFERN)) {
				String losnummer = (String) JOptionPane.showInputDialog(this,
						LPMain.getTextRespectUISPr("label.losnummer"),
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.QUESTION_MESSAGE);
				if (losnummer != null && losnummer.length() > 1
						&& losnummer.startsWith("$L")) {
					losnummer = losnummer.substring(2);

					try {
						LosDto losDto = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.losFindByCNrMandantCNr(losnummer,
										LPMain.getTheClient().getMandant());
						if (losDto.getStatusCNr().equals(
								LocaleFac.STATUS_ERLEDIGT)) {
							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									"Das Los " + losnummer
											+ " ist bereits erledigt.");
						} else {
							// CK:13872 Nach Menge fragen

							BigDecimal offeneMenge = null;

							BigDecimal bereitsAbgeliefert = DelegateFactory
									.getInstance()
									.getFertigungDelegate()
									.wievileTOPSArtikelWurdenBereitsZugebucht(
											losDto.getIId());

							if (bereitsAbgeliefert == null) {
								offeneMenge = losDto.getNLosgroesse();
							} else {
								offeneMenge = losDto.getNLosgroesse().subtract(
										bereitsAbgeliefert);

								if (offeneMenge.doubleValue() < 0) {
									offeneMenge = new BigDecimal(0);
								}
							}

							DialogMengeTopsabliefern d = new DialogMengeTopsabliefern(
									this, offeneMenge);
							LPMain.getInstance().getDesktop()
									.platziereDialogInDerMitteDesFensters(d);
							d.setVisible(true);

							BigDecimal menge = d.getMenge();

							if (menge != null) {

								if (menge.doubleValue() > offeneMenge
										.doubleValue()) {

									BigDecimal diff = bereitsAbgeliefert.add(
											menge).subtract(
											losDto.getNLosgroesse());

									boolean b = DialogFactory
											.showModalJaNeinDialog(
													getInternalFrame(),
													"Wollen Sie wirklich um insgesamt "
															+ diff
															+ " Satz mehr als die Losgr\u00F6\u00DFe "
															+ losDto.getNLosgroesse()
															+ " zubuchen?");
									if (b == false) {
										return;
									}
								}

								DelegateFactory
										.getInstance()
										.getFertigungDelegate()
										.bucheTOPSArtikelAufHauptLager(
												losDto.getIId(), menge);
							}
						}
					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									"Los " + losnummer
											+ " konnte nicht gefunden werden.");
						} else {
							handleException(ex, true);
						}
					}
				}
			} else if (sAspectInfo.equals(MENUE_ACTION_AUFTRAGERLEDIGEN)) {

				String auftragsnummer = (String) JOptionPane.showInputDialog(
						this,
						LPMain.getTextRespectUISPr("label.auftragnummer"),
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.QUESTION_MESSAGE);

				if (auftragsnummer != null && auftragsnummer.length() > 1
						&& auftragsnummer.startsWith("$A")) {
					auftragsnummer = auftragsnummer.substring(2);
					AuftragDto auftragDto = null;
					try {
						auftragDto = DelegateFactory
								.getInstance()
								.getAuftragDelegate()
								.auftragFindByMandantCNrCNr(
										LPMain.getTheClient().getMandant(),
										auftragsnummer);
						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.perAuftragsnummerLoseAbliefern(
										auftragDto.getIId());

					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									"Auftrag " + auftragsnummer
											+ " konnte nicht gefunden werden.");
						} else if (ex.getICode() == EJBExceptionLP.FEHLER_ABLIEFERN_PER_AUFTRAGSNUMMER_NICHT_MOEGLICH) {

							ArrayList al = ex.getAlInfoForTheClient();

							String s = "";

							for (int i = 0; i < al.size(); i++) {
								s += al.get(i) + "\n";
							}

							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("fert.auftragabliefern.fehler")
													+ "\n" + s);
							return;
						} else if (ex.getICode() == EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN) {
							StringBuffer sText = (StringBuffer) ex
									.getAlInfoForTheClient().get(0);

							sText.append("\n\n")
									.append(LPMain
											.getTextRespectUISPr("lp.error.vorgang.abgebrochen"));

							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									sText.toString());
						} else {
							handleException(ex, true);
						}

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
			} else if (sAspectInfo.equals(MENUE_ACTION_ZUSATZSTATUS)) {

				ArbeitsplatzparameterDto parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_FERTIGUNG_ZUSATZSTATUS);

				if (parameter != null && parameter.getCWert() != null) {
					Integer zusatzstatusIId = new Integer(parameter.getCWert());

					ZusatzstatusDto zusatzstatusDto = DelegateFactory
							.getInstance().getFertigungDelegate()
							.zusatzstatusFindByPrimaryKey(zusatzstatusIId);

					DialogLoszusatzstatus status = new DialogLoszusatzstatus(
							zusatzstatusDto);
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(status);
					status.setVisible(true);

				}
			} else if (sAspectInfo.equals(ACTION_SPECIAL_MATERIALENTNAHME)) {
				reloadLosDto();
				if (getLosDto() != null) {
					if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_STORNIERT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
								null);
					} else if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_ANGELEGT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
								null);
					} else if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_ERLEDIGT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
								null);
					}

					getInternalFrame()
							.showPanelDialog(
									new PanelDialogNachtraeglicheEntnahme(
											getInternalFrame(),
											getPanelQueryMaterial(true),
											LPMain.getTextRespectUISPr("fert.tooltip.nachtraeglichematerialentnahme"),
											getLosDto().getIId(),
											getPanelDetailMaterial(true)
													.getLossollmaterialDto()));
				}
			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_MATERIALENTNAHME_OHNE_SOLLPOSITION)) {
				reloadLosDto();
				if (getLosDto() != null) {
					if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_STORNIERT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
								null);
					} else if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_ANGELEGT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
								null);
					} else if (getLosDto().getStatusCNr().equals(
							FertigungFac.STATUS_ERLEDIGT)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
								null);
					}

					getInternalFrame()
							.showPanelDialog(
									new PanelDialogNachtraeglicheEntnahme(
											getInternalFrame(),
											getPanelQueryMaterial(true),
											LPMain.getTextRespectUISPr("fert.tooltip.nachtraeglichematerialentnahme"),
											getLosDto().getIId(), null));
				}
			} else if (sAspectInfo.equals(ACTION_SPECIAL_ISTMENGE_AENDERN)) {

				if (getLosDto().getStatusCNr().equals(
						FertigungFac.STATUS_STORNIERT)) {
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
							new Exception(getLosDto().getCNr()
									+ " ist storniert"));
				}
				if (getLosDto().getStatusCNr().equals(
						FertigungFac.STATUS_ANGELEGT)) {
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
							new Exception(getLosDto().getCNr()
									+ " ist noch nicht ausgegeben"));
				}
				if (getLosDto().getStatusCNr().equals(
						FertigungFac.STATUS_GESTOPPT)) {
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT,
							new Exception(getLosDto().getCNr()
									+ " ist gestoppt"));
				}
				if (getLosDto().getStatusCNr().equals(
						FertigungFac.STATUS_ERLEDIGT)) {
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
							new Exception(getLosDto().getCNr()
									+ " ist erledigt"));
				}

				if (panelQueryMaterial.getSelectedId() != null) {
					DialogIstmaterialAendern d = new DialogIstmaterialAendern(
							(Integer) panelQueryMaterial.getSelectedId(),
							getInternalFrame());
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				}

			} else if (sAspectInfo != null
					&& sAspectInfo
							.endsWith(ACTION_SPECIAL_ABLIEFERUNGEN_NEU_KALKULIEREN)) {
				if (getLosDto() != null) {
					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.aktualisiereNachtraeglichPreiseAllerLosablieferungen(
									getLosDto().getIId());
					getPanelQueryAblieferung(true).eventYouAreSelected(false);
				}
			} else if (sAspectInfo != null
					&& sAspectInfo
							.endsWith(ACTION_SPECIAL_SOLLPREISE_MATERIAL_NEU_KALKULIEREN)) {
				if (getLosDto() != null) {
					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.sollpreiseAllerSollmaterialpositionenNeuKalkulieren(
									getLosDto().getIId());
					getPanelQueryMaterial(true).eventYouAreSelected(false);
				}
			} else if (sAspectInfo.equals(ACTION_SPECIAL_NEU_ANHAND_AUFTRAG)) {
				dialogQueryAuftragFromListe(null);
			}

			else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				einfuegenHV();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryLagerentnahme) {
				int iPos = panelQueryLagerentnahme.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryLagerentnahme
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryLagerentnahme
							.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.vertauscheLoslagerentnahme(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryLagerentnahme.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryLagerentnahme) {
				int iPos = panelQueryLagerentnahme.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryLagerentnahme.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryLagerentnahme
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryLagerentnahme
							.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.vertauscheLoslagerentnahme(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryLagerentnahme.setSelectedId(iIdPosition);
				}
			}
		}

	}

	private void holeLosDto(Integer key) throws Throwable {
		if (key != null) {
			setLosDto(DelegateFactory.getInstance().getFertigungDelegate()
					.losFindByPrimaryKey(key));
			if (losDto.getIId() != null) {
				getInternalFrame().setKeyWasForLockMe(
						getLosDto().getIId().toString());
			}
			if (getPanelKopfdaten(false) != null) {
				getPanelKopfdaten(true).setKeyWhenDetailPanel(key);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		getInternalFrame().setRechtModulweit(sRechtModulweit);

		if (selectedIndex == IDX_AUSWAHL) {
			getPanelQueryAuswahl(true).eventYouAreSelected(false);
		} else if (selectedIndex == IDX_KOPFDATEN) {
			if (this.getLosDto() != null) {
				getPanelKopfdaten(true).setKeyWhenDetailPanel(
						this.getLosDto().getIId());
			}
			getPanelKopfdaten(true).eventYouAreSelected(false);
		} else if (selectedIndex == IDX_MATERIAL) {

			boolean hatRecht = DelegateFactory
					.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(
							RechteFac.RECHT_FERT_LOS_DARF_ISTMATERIAL_MANUELL_NACHBUCHEN);
			if (hatRecht == true) {
				getInternalFrame().setRechtModulweit(
						RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			getPanelSplitMaterial(true).eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_ARBEITSPLAN) {
			getPanelSplitZeitdaten(true).eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_LAGERENTNAHME) {
			getPanelSplitLagerentnahme(true).eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_ABLIEFERUNG) {

			boolean hatRecht = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_FERT_LOS_DARF_ABLIEFERN);
			if (hatRecht == true) {
				getInternalFrame().setRechtModulweit(
						RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			getPanelSplitAblieferung(true).eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_GUTSCHLECHT) {
			getPanelSplitLosgutschlecht(true).eventYouAreSelected(false);
			if (getPanelQueryZeitdaten(true).getSelectedId() == null) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("fert.error.arbeitsgangauswaehlen"));
				setSelectedIndex(IDX_ARBEITSPLAN);
				lPEventObjectChanged(e);
				return;
			}

			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_LOSLOSKLASSE) {
			getPanelSplitLoslosklasse(true).eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_FEHLMENGEN) {
			getPanelSplitFehlmengen(true).eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_NACHKALKULATION) {
			getPanelNachkalkulation().setDefaultFilter(
					FertigungFilterFactory.getInstance()
							.createFKKriteriumNachkalkulation(
									getLosDto().getIId()));
			getPanelNachkalkulation().eventYouAreSelected(false);
			getPanelNachkalkulation().updateButtons(
					new LockStateValue(null, null,
							PanelBasis.LOCK_IS_NOT_LOCKED));
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_ISTZEITDATEN) {
			if (!pdKriterienAuftragzeitenUeberMenueAufgerufen) {
				// this.setLosDto(this.getLosDto());
				getInternalFrame().showPanelDialog(getKriterienLoszeiten(true));
			}
		} else if (selectedIndex == IDX_LOSZUSATZSTATUS) {
			getPanelSplitLoszusatzstatus(true).eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_LOSTECHNIKER) {
			getPanelSplitLostechniker(true).eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		} else if (selectedIndex == IDX_PANEL_LOSEIGENSCHAFTEN) {
			refreshEigenschaften(getLosDto().getIId());
			panelDetailLoseigenschaft.eventYouAreSelected(false);
			this.setLosDto(this.getLosDto());
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_AKTUALISIERE_ARBEITSPLAN)) {
			if (getLosDto() != null) {
				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.aktualisiereSollArbeitsplanAusStueckliste(
								getLosDto().getIId());
				if (getSelectedIndex() == IDX_ARBEITSPLAN) {
					getPanelQueryZeitdaten(true).eventYouAreSelected(false);
				} else if (getSelectedIndex() == IDX_KOPFDATEN) {
					getPanelKopfdaten(true).eventYouAreSelected(false);
				}
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_AKTUALISIERE_STUECKLISTE)) {
			if (getLosDto() != null) {
				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.aktualisiereSollMaterialAusStueckliste(
								getLosDto().getIId());
				if (getSelectedIndex() == IDX_MATERIAL) {
					getPanelQueryMaterial(true).eventYouAreSelected(false);
				} else if (getSelectedIndex() == IDX_KOPFDATEN) {
					getPanelKopfdaten(true).eventYouAreSelected(false);
				}
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_KOMMENTAR)) {
			if (!getPanelKopfdaten(true).isLockedDlg()) {
				if (getLosDto() != null) {

					if (getLosDto().getStatusCNr() != null
							&& getLosDto().getStatusCNr().equals(
									FertigungFac.STATUS_ERLEDIGT)) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("los.error.losbereitserledigt"));
					}

					refreshPdKommentar();
					getInternalFrame().showPanelDialog(pdStuecklisteKommentar);
				}
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_PRODUKTIONSINFORMATION)) {
			if (!getPanelKopfdaten(true).isLockedDlg()) {
				if (getLosDto() != null) {
					refreshPdProduktionsinformation();
					getInternalFrame()
							.showPanelDialog(pdProduktionsinformation);
				}
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_STATISTIK)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fert.menu.losstatistik");
			getInternalFrame().showReportKriterien(
					new ReportLosstatistik(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_RANKINGLISTE)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fert.menu.rankingliste");
			getInternalFrame().showReportKriterien(
					new ReportRankingliste(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_ZEITENTWICKLUNG)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fert.menu.zeitentwicklung");
			getInternalFrame().showReportKriterien(
					new ReportZeitentwicklung(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_AUSLASTUNGSVORSCHAU)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fert.menu.auslastungsvorschau");
			getInternalFrame()
					.showReportKriterien(
							new ReportAuslastungsvorschau(getInternalFrame(),
									add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_AUSLASTUNGSVORSCHAU_DETAILIERT)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fert.menu.auslastungsvorschaudetailliert");
			getInternalFrame().showReportKriterien(
					new ReportAuslastungsvorschauDetailiert(getInternalFrame(),
							add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_AUSLIEFERLISTE)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fert.menu.auslieferliste");
			getInternalFrame().showReportKriterien(
					new ReportAuslieferliste(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_FEHLERSTATISTIK)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fert.los.report.fehlerstatistik");
			getInternalFrame().showReportKriterien(
					new ReportFehlerstatistik(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENU_ACTION_JOURNAL_MONATSAUSWERTUNG)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fert.menu.monatsauswertung");
			getInternalFrame().showReportKriterien(
					new ReportMonatsauswertung(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_AUFLOESBARE_FEHLMENGEN)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportAufloesbareFehlmengen(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("fert.report.aufloesbarefehlmengen")));

		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_FEHLMENGEN_ALLER_LOSE)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportFehlmengenAllerLose(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("fert.report.fehlmengenallerlose")));

		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MODUL_DRUCKEN_AUSGABELISTE)) {
			printAusgabeliste(getLosDto(), false);
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MODUL_DRUCKEN_PRODUKTIONSINFORMATION)) {
			printProduktionsinformation();
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MODUL_DRUCKEN_FERTIGUNGSBEGLEITSCHEIN)) {
			printFertigungsbegleitschein(getLosDto());
		} else if (e.getActionCommand()
				.equals(MENU_ACTION_BEARBEITEN_BEWERTUNG)) {
			if (getLosDto() != null) {
				Double fErfuellungsgrad = null;
				if (getLosDto().getFBewertung() != null) {
					fErfuellungsgrad = getLosDto().getFBewertung();
				}
				DialogBewertung d = new DialogBewertung(this, fErfuellungsgrad);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
			}

		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MODUL_DRUCKEN_THEORETISCHE_FEHLMENGENLISTE)) {
			reloadLosDto();
			printTheoretischeFehlmengen();
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_PRODUKTIONSSTOP)) {
			reloadLosDto();
			if (getLosDto() != null) {
				boolean bAnswer;
				if (getLosDto().getStatusCNr().equals(
						FertigungFac.STATUS_GESTOPPT)) {
					bAnswer = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("fert.question.produktionsstopaufheben"));
					if (bAnswer) {
						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.stoppeProduktionRueckgaengig(
										getLosDto().getIId());
					}
				} else {
					bAnswer = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("fert.question.produktionstoppen"));
					if (bAnswer) {
						DelegateFactory.getInstance().getFertigungDelegate()
								.stoppeProduktion(getLosDto().getIId());
					}
				}
				if (bAnswer) {
					if (getSelectedIndex() == IDX_AUSWAHL) {
						getPanelQueryAuswahl(true).eventYouAreSelected(false);
					} else if (getSelectedIndex() == IDX_KOPFDATEN) {
						getPanelKopfdaten(true).eventYouAreSelected(false);
					}
				}
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_ABLIEFERUNGSSTATISTIK)) {
			if (getLosDto() != null) {
				getInternalFrame()
						.showReportKriterien(
								new ReportAblieferungsstatistik(
										getInternalFrame(),
										getStuecklisteDto(),
										LPMain.getTextRespectUISPr("fert.menu.ablieferungsstatistik")));
			}
		}

		else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_LOSGROESSEAENDERN)) {

			if (getLosDto() != null) {

				DialogLosgroesseaendern d = new DialogLosgroesseaendern(this,
						getLosDto().getNLosgroesse().intValue());
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

				getPanelSplitMaterial(true).eventYouAreSelected(false);
			}

		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN)) {

			if (getLosDto() != null) {

				DialogTerminverschieben d = new DialogTerminverschieben(this);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

			}

		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_ALLE_FEHLMENGEN_AUFLOSEN)) {

			boolean bOption = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("fert.los.allefelhmengenaufloesen.warning"),
							LPMain.getTextRespectUISPr("lp.frage"));
			if (bOption) {

				TreeMap<?, ?> aufgeloest = DelegateFactory.getInstance()
						.getFehlmengeDelegate()
						.alleFehlmengenDesMandantenAufloesen();

				getInternalFrame().showReportKriterien(
						new ReportAufgeloestefehlmengen(getInternalFrame(),
								aufgeloest));

			}

		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_HALBFERTIGFABRIKATSINVENTUR)) {
			if (getLosDto() != null) {
				getInternalFrame()
						.showReportKriterien(
								new ReportHalbfertigfabrikatsinventur(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("fert.menu.halbfertigfabrikatinventur")));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_JOURNAL_ALLE_LOSE)) {
			if (getLosDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportAlleLose(getInternalFrame(), LPMain
								.getTextRespectUISPr("fert.menu.allelose")));
			}
		} else if (e.getActionCommand()
				.equals(MENUE_ACTION_JOURNAL_OFFENE_LOSE)) {
			if (getLosDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportOffeneLose(getInternalFrame(), LPMain
								.getTextRespectUISPr("fert.menu.offenelose")));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_JOURNAL_OFFENE_AG)) {
			if (getLosDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportOffeneAg(getInternalFrame(), LPMain
								.getTextRespectUISPr("fert.menu.offeneag")));
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_INFO_GESAMTKALKULATION)) {
			if (getLosDto() != null) {
				getInternalFrame()
						.showReportKriterien(
								new ReportLosGesamtkalkulation(
										(InternalFrameFertigung) getInternalFrame(),
										LPMain.getTextRespectUISPr("fert.report.gesamtkalkulation")));
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_JOURNAL_STUECKRUECKMELDUNG)) {
			if (getLosDto() != null) {
				getInternalFrame()
						.showReportKriterien(
								new ReportStueckrueckmeldung(
										getInternalFrame(),
										getLosDto().getIId(),
										LPMain.getTextRespectUISPr("fert.menu.stueckrueckmeldung")
												+ " " + getLosDto().getCNr()));
			}
		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (getLosDto() != null) {

				String s = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.istBelegGeradeInBearbeitung(LocaleFac.BELEGART_LOS,
								getLosDto().getIId());

				if (s != null) {
					boolean b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("fert.los.erledigen.losgeradeinbearbeitung")
											+ s
											+ LPMain.getTextRespectUISPr("fert.los.erledigen.losgeradeinbearbeitung1"));

					if (b == false) {
						return;
					}

				}

				int indexJa = 0;
				int indexNein = 1;
				int indexDatumLetzterAblieferung = 2;
				int iAnzahlOptionen = 3;

				Object[] aOptionen = new Object[iAnzahlOptionen];
				aOptionen[indexJa] = LPMain.getTextRespectUISPr("lp.ja");
				aOptionen[indexNein] = LPMain.getTextRespectUISPr("lp.nein");
				aOptionen[indexDatumLetzterAblieferung] = LPMain
						.getTextRespectUISPr("fert.losereledigen.datumderletztenablieferung");

				int iAuswahl = DialogFactory
						.showModalDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("fert.status.auferledigtsetzen"),
								LPMain.getTextRespectUISPr("lp.frage"),
								aOptionen, aOptionen[0]);

				if (iAuswahl == indexJa
						|| iAuswahl == indexDatumLetzterAblieferung) {

					boolean bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden = false;
					if (iAuswahl == indexDatumLetzterAblieferung) {
						bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden = true;
					}

					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.manuellErledigen(getLosDto().getIId(),
									bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden);
					panelQueryAuswahl.eventYouAreSelected(false);

				}
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MEHRERE_LOSE_AUSGEBEN_FTG_GRUPPE)) {
			dialogQueryFertigungsgruppeFromListe();

		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_ZUORDNUNG)) {
			if (getLosDto() != null && getLosDto().getIId() != null) {
				/**
				 * @todo
				 */
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MODUL_DRUCKEN_ETIKETT)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fertigung.report.etikett");
			getInternalFrame().showReportKriterien(
					new ReportLosetikett(getInternalFrame(), add2Title,
							getLosDto().getIId()));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MODUL_DRUCKEN_ETIKETT_A4)) {
			String add2Title = LPMain
					.getTextRespectUISPr("fertigung.report.etikett.a4");
			getInternalFrame().showReportKriterien(
					new ReportLosetikettA4(getInternalFrame(), add2Title,
							getLosDto().getIId()));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MEHRERE_LOSE_AUSGEBEN_ENTHALTENE_STKL)) {

			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("fert.los.stlbaumausgeben"));
			mf.setLocale(LPMain.getTheClient().getLocUi());

			Object pattern[] = { getLosDto().getCNr() };
			String sMsg = mf.format(pattern);

			boolean bAntwort = DialogFactory.showModalJaNeinDialog(
					getInternalFrame(), sMsg);

			if (bAntwort == true) {
				TreeSet ts = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.getLoseEinesStuecklistenbaums(getLosDto().getIId());
				Iterator it = ts.iterator();

				while (it.hasNext()) {

					Integer losIId = (Integer) it.next();

					LosDto losDto = DelegateFactory.getInstance()
							.getFertigungDelegate().losFindByPrimaryKey(losIId);

					int iAnswer = pruefeStuecklisteAktuellerAlsLosDlg(losIId);
					if (iAnswer != JOptionPane.CANCEL_OPTION) {
						boolean bAusgabeErfolgreich = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.gebeLosAus(
										losIId,
										false,
										getAbzubuchendeSeriennrChargen(
												losDto.getIId(),
												losDto.getNLosgroesse(), false));

						if (bAusgabeErfolgreich == false) {

							mf = new MessageFormat(
									LPMain.getTextRespectUISPr("fert.los.stlbaumausgeben.fehler"));
							mf.setLocale(LPMain.getTheClient().getLocUi());

							pattern = new Object[] { losDto.getCNr() };
							sMsg = mf.format(pattern);

							boolean b = DialogFactory.showModalJaNeinDialog(
									getInternalFrame(), sMsg);

							if (b == true) {
								return;
							}
						}

						// refresh aufs panel
						printAusgabelisteUndFertigungsbegleitscheinDlg(losIId,
								false);
					} else {
						return;
					}
				}
			}
		}
	}

	public void printAusgabelisteUndFertigungsbegleitscheinDlg(Integer losIId,
			boolean bFrageAnzeigen) throws Throwable {

		LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
				.losFindByPrimaryKey(losIId);

		FertigungsgruppeDto ftgDto = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.fertigungsgruppeFindByPrimaryKey(
						losDto.getFertigungsgruppeIId());

		// PJ 17672
		if (ftgDto.getIFormularnummer() != null
				&& ftgDto.getIFormularnummer() == -1) {
			return;
		}

		boolean bFertigungspapiereDrucken = true;

		if (bFrageAnzeigen == true) {

			bFertigungspapiereDrucken = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("fert.frage.fertigungspapieredrucken"),
							LPMain.getTextRespectUISPr("lp.frage"));
		}
		if (bFertigungspapiereDrucken) {
			printAusgabeliste(losDto, false);

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_LOSAUSGABELISTE2_AUTOMATISCH_DRUCKEN,
							ParameterFac.KATEGORIE_FERTIGUNG,
							LPMain.getTheClient().getMandant());
			if (((Boolean) parameter.getCWertAsObject()) == true) {
				printAusgabeliste(losDto, true);
			}

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_ZEITERFASSUNG)) {
				printFertigungsbegleitschein(losDto);
			}
		}

	}

	public int pruefeStuecklisteAktuellerAlsLosDlg(Integer losIId)
			throws Throwable {
		// Default ist NO, damits fuer materiallisten auch geht
		int iAnswer = JOptionPane.NO_OPTION;
		// nur wenn das Los sich auf eine Stueckliste bezieht
		LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
				.losFindByPrimaryKey(losIId);

		if (losDto.getStuecklisteIId() != null) {
			// ist der Arbeitsplan der Stueckliste aktueller als der des Loses?

			StuecklisteDto stklDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());

			if (losDto.getTAktualisierungarbeitszeit() != null
					&& stklDto.getTAendernarbeitsplan().after(
							losDto.getTAktualisierungarbeitszeit())) {

				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("fert.frage.stklarbeitsplanaktualisieren"));
				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { losDto.getCNr() };
				String sMsg = mf.format(pattern);

				iAnswer = DialogFactory.showModalJaNeinAbbrechenDialog(
						getInternalFrame(), sMsg,
						LPMain.getTextRespectUISPr("lp.frage"));
				if (iAnswer == JOptionPane.YES_OPTION) {
					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.aktualisiereSollArbeitsplanAusStueckliste(
									losDto.getIId());
				}
			}
			// ist das Material der Stueckliste aktueller als der des Loses?
			if (losDto.getTAktualisierungstueckliste() != null
					&& stklDto.getTAendernposition().after(
							losDto.getTAktualisierungstueckliste())) {

				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("fert.frage.stklmaterialaktualisieren"));
				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { losDto.getCNr() };
				String sMsg = mf.format(pattern);

				iAnswer = DialogFactory.showModalJaNeinAbbrechenDialog(
						getInternalFrame(), sMsg,
						LPMain.getTextRespectUISPr("lp.frage"));
				if (iAnswer == JOptionPane.YES_OPTION) {
					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.aktualisiereSollMaterialAusStueckliste(
									losDto.getIId());
				}
			}
		}
		return iAnswer;
	}

	private void printTheoretischeFehlmengen() throws Throwable {
		if (getLosDto() != null) {
			if (getLosDto().getStatusCNr()
					.equals(FertigungFac.STATUS_STORNIERT)) {
				throw new ExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						new Exception("Los " + getLosDto().getCNr() + " ist "
								+ getLosDto().getStatusCNr()));
			} else if (getLosDto().getStatusCNr().equals(
					FertigungFac.STATUS_ERLEDIGT)) {
				throw new ExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						new Exception("Los " + getLosDto().getCNr() + " ist "
								+ getLosDto().getStatusCNr()));
			} else {
				getInternalFrame()
						.showReportKriterien(
								new ReportTheoretischeFehlmengenliste(
										getInternalFrame(),
										getLosDto().getIId(),
										LPMain.getTextRespectUISPr("fert.report.theoretischefehlmengenliste")
												+ " " + losDto.getCNr()));
			}
		}
	}

	protected void printAusgabeliste(LosDto losDto, boolean bAlternativerReport)
			throws Throwable {

		if (losDto != null) {

			getInternalFrame()
					.showReportKriterien(
							new ReportAusgabeliste(
									getInternalFrame(),
									losDto.getIId(),
									LPMain.getTextRespectUISPr("fert.report.ausgabeliste")
											+ " " + losDto.getCNr(),
									bAlternativerReport));

		}
	}

	protected void printProduktionsinformation() throws Throwable {
		reloadLosDto();
		if (getLosDto() != null) {
			getInternalFrame()
					.showReportKriterien(
							new ReportProduktionsinformation(
									getInternalFrame(),
									getLosDto().getIId(),
									LPMain.getTextRespectUISPr("fert.produktionsinformation")
											+ " " + losDto.getCNr()));
		}
	}

	protected void printFertigungsbegleitschein(LosDto losDto) throws Throwable {

		if (losDto != null) {
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
				throw new ExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						new Exception("Los " + losDto.getCNr() + " ist "
								+ losDto.getStatusCNr()));
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ANGELEGT)) {
				throw new ExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
						new Exception("Los " + losDto.getCNr() + " ist "
								+ losDto.getStatusCNr()));
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ERLEDIGT)) {
				throw new ExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						new Exception("Los " + losDto.getCNr() + " ist "
								+ losDto.getStatusCNr()));
			} else {
				getInternalFrame()
						.showReportKriterien(
								new ReportFertigungsbegleitschein(
										getInternalFrame(),
										losDto.getIId(),
										LPMain.getTextRespectUISPr("fert.report.fertigungsbegleitschein")
												+ " " + losDto.getCNr()));
			}
		}
	}

	protected void reloadLosDto() throws Throwable {
		if (getLosDto() != null) {
			setLosDto(DelegateFactory.getInstance().getFertigungDelegate()
					.losFindByPrimaryKey(getLosDto().getIId()));
		}
	}

	/**
	 * 
	 * @return Integer
	 * @throws Throwable
	 */
	protected Integer getSelectedIIdLos() throws Throwable {
		return (Integer) getPanelQueryAuswahl(true).getSelectedId();
	}

	/**
	 * Diese Methode setzt der aktuellen ER aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws Throwable
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryAuswahl(true).getSelectedId();

		if (oKey != null) {
			holeLosDto((Integer) oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void dialogQueryFertigungsgruppeFromListe() throws Throwable {
		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory
				.getInstance()
				.createPanelFLRFertigungsgruppe(getInternalFrame(), null, false);
		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (null != menuBar)
			return menuBar;

		WrapperMenuBar wmb = new WrapperMenuBar(this);
		JMenu modul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);
		modul.add(new JSeparator(), 0);
		JMenu jMenuDrucken = new WrapperMenu("lp.drucken", this);
		modul.add(jMenuDrucken, 0);
		WrapperMenuItem menuItemModulDruckenFertigungsbegleitschein = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.menu.fertigungsbegleitschein"),
				null);
		WrapperMenuItem menuItemModulDruckenAusgabeliste = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.menu.ausgabeliste"), null);
		WrapperMenuItem menuItemModulDruckenTheoretischeFehlmengen = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.menu.theoretischefehlmengenliste"),
				null);
		WrapperMenuItem menuItemModulDruckenEtikett = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.menu.etikett"), null);
		WrapperMenuItem menuItemModulDruckenEtikettA4 = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fertigung.report.etikett.a4"), null);
		jMenuDrucken.add(menuItemModulDruckenFertigungsbegleitschein);
		jMenuDrucken.add(menuItemModulDruckenAusgabeliste);
		jMenuDrucken.add(menuItemModulDruckenTheoretischeFehlmengen);
		jMenuDrucken.add(menuItemModulDruckenEtikett);
		jMenuDrucken.add(menuItemModulDruckenEtikettA4);

		WrapperMenuItem menuItemModulProduktionsinformation = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.produktionsinformation"), null);
		menuItemModulProduktionsinformation
				.setActionCommand(MENUE_ACTION_MODUL_DRUCKEN_PRODUKTIONSINFORMATION);
		menuItemModulProduktionsinformation.addActionListener(this);
		jMenuDrucken.add(menuItemModulProduktionsinformation);

		menuItemModulDruckenFertigungsbegleitschein
				.setActionCommand(MENUE_ACTION_MODUL_DRUCKEN_FERTIGUNGSBEGLEITSCHEIN);
		menuItemModulDruckenAusgabeliste
				.setActionCommand(MENUE_ACTION_MODUL_DRUCKEN_AUSGABELISTE);
		menuItemModulDruckenTheoretischeFehlmengen
				.setActionCommand(MENUE_ACTION_MODUL_DRUCKEN_THEORETISCHE_FEHLMENGENLISTE);
		menuItemModulDruckenEtikett
				.setActionCommand(MENUE_ACTION_MODUL_DRUCKEN_ETIKETT);
		menuItemModulDruckenEtikett.addActionListener(this);
		menuItemModulDruckenEtikettA4
				.setActionCommand(MENUE_ACTION_MODUL_DRUCKEN_ETIKETT_A4);
		menuItemModulDruckenEtikettA4.addActionListener(this);
		menuItemModulDruckenFertigungsbegleitschein.addActionListener(this);
		menuItemModulDruckenAusgabeliste.addActionListener(this);
		menuItemModulDruckenTheoretischeFehlmengen.addActionListener(this);
		// Menu Bearbeiten
		JMenu jmBearbeiten = (JMenu) wmb
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);
		WrapperMenuItem menuItemBearbeitenStuecklisteAktualisieren = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.menu.stuecklisteaktualisieren"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemBearbeitenStuecklisteAktualisieren.addActionListener(this);
		menuItemBearbeitenStuecklisteAktualisieren
				.setActionCommand(MENUE_ACTION_BEARBEITEN_AKTUALISIERE_STUECKLISTE);

		WrapperMenuItem menuItemBearbeitenArbeitsplanAktualisieren = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.menu.arbeitsplanaktualisieren"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemBearbeitenArbeitsplanAktualisieren.addActionListener(this);
		menuItemBearbeitenArbeitsplanAktualisieren
				.setActionCommand(MENUE_ACTION_BEARBEITEN_AKTUALISIERE_ARBEITSPLAN);

		WrapperMenuItem menuItemBearbeitenProduktionsstop = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.menu.produktionsstop"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemBearbeitenProduktionsstop.addActionListener(this);
		menuItemBearbeitenProduktionsstop
				.setActionCommand(MENUE_ACTION_BEARBEITEN_PRODUKTIONSSTOP);

		WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemBearbeitenManuellErledigen.addActionListener(this);
		menuItemBearbeitenManuellErledigen
				.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN);

		WrapperMenuItem menuItemBearbeitenZuordnung = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fert.menu.zuordnung"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemBearbeitenZuordnung.addActionListener(this);
		menuItemBearbeitenZuordnung.setActionCommand(MENU_BEARBEITEN_ZUORDNUNG);

		jmBearbeiten.add(menuItemBearbeitenStuecklisteAktualisieren, 0);
		jmBearbeiten.add(menuItemBearbeitenArbeitsplanAktualisieren, 1);

		WrapperMenuItem menuItemBearbeitenBewertung = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.bewertungkommentar"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemBearbeitenBewertung.addActionListener(this);
		menuItemBearbeitenBewertung
				.setActionCommand(MENU_ACTION_BEARBEITEN_BEWERTUNG);
		jmBearbeiten.add(menuItemBearbeitenBewertung, 2);

		jmBearbeiten.add(new JSeparator(), 3);
		jmBearbeiten.add(menuItemBearbeitenProduktionsstop, 4);
		jmBearbeiten.add(new JSeparator(), 5);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FERT_DARF_LOS_ERLEDIGEN)) {

			jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 6);
			jmBearbeiten.add(new JSeparator(), 7);

		}

		JMenuItem menuItemBearbeitenInternerKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.kommentar"));
		menuItemBearbeitenInternerKommentar.addActionListener(this);
		menuItemBearbeitenInternerKommentar
				.setActionCommand(MENU_BEARBEITEN_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenInternerKommentar);

		JMenuItem menuItemBearbeitenProduktionsinformation = new JMenuItem(
				LPMain.getTextRespectUISPr("fert.produktionsinformation"));
		menuItemBearbeitenProduktionsinformation.addActionListener(this);
		menuItemBearbeitenProduktionsinformation
				.setActionCommand(MENU_BEARBEITEN_PRODUKTIONSINFORMATION);
		jmBearbeiten.add(menuItemBearbeitenProduktionsinformation);

		JMenu menuItemMehrereLoseAugeben = new JMenu(
				LPMain.getTextRespectUISPr("lp.menu.mehrereausgeben"));

		WrapperMenuItem menuItemMehrereLoseAugebenFTG = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.mehrereausgeben.anhandftg"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemMehrereLoseAugebenFTG.addActionListener(this);
		menuItemMehrereLoseAugebenFTG
				.setActionCommand(MENUE_ACTION_MEHRERE_LOSE_AUSGEBEN_FTG_GRUPPE);
		menuItemMehrereLoseAugeben.add(menuItemMehrereLoseAugebenFTG);

		WrapperMenuItem menuItemMehrereLoseAugebenEnthalteneStkl = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.mehrereausgeben.enthaltenestkl"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemMehrereLoseAugebenEnthalteneStkl.addActionListener(this);
		menuItemMehrereLoseAugebenEnthalteneStkl
				.setActionCommand(MENUE_ACTION_MEHRERE_LOSE_AUSGEBEN_ENTHALTENE_STKL);
		menuItemMehrereLoseAugeben
				.add(menuItemMehrereLoseAugebenEnthalteneStkl);

		jmBearbeiten.add(new JSeparator());
		jmBearbeiten.add(menuItemMehrereLoseAugeben);

		WrapperMenuItem menuItemLosgroesseAendern = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.losgroesseaendern"),
				RechteFac.RECHT_FERT_LOS_CUD);
		menuItemLosgroesseAendern.addActionListener(this);
		menuItemLosgroesseAendern
				.setActionCommand(MENUE_ACTION_BEARBEITEN_LOSGROESSEAENDERN);
		jmBearbeiten.add(new JSeparator());
		jmBearbeiten.add(menuItemLosgroesseAendern);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FERT_TERMINE_VERSCHIEBEN)) {
			WrapperMenuItem menuItemTerminverschieben = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.terminverschieben"),
					RechteFac.RECHT_FERT_LOS_CUD);
			menuItemTerminverschieben.addActionListener(this);
			menuItemTerminverschieben
					.setActionCommand(MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN);
			jmBearbeiten.add(menuItemTerminverschieben);
		}

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						RechteFac.RECHT_FERT_DARF_FEHLMENGEN_PER_DIALOG_AUFLOESEN)) {
			jmBearbeiten.add(new JSeparator());
			WrapperMenuItem menuItemFehlmengrnAufloesen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.los.allefelhmengenaufloesen"),
					RechteFac.RECHT_FERT_LOS_CUD);
			menuItemFehlmengrnAufloesen.addActionListener(this);
			menuItemFehlmengrnAufloesen
					.setActionCommand(MENUE_ACTION_BEARBEITEN_ALLE_FEHLMENGEN_AUFLOSEN);
			jmBearbeiten.add(menuItemFehlmengrnAufloesen);
		}

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FERT_LOS_CUD)) {

			// journal
			JMenu jmJournal = (JMenu) wmb
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			WrapperMenuItem menuItemJournalAlleLose = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.allelose"), null);
			menuItemJournalAlleLose.addActionListener(this);
			menuItemJournalAlleLose
					.setActionCommand(MENUE_ACTION_JOURNAL_ALLE_LOSE);
			WrapperMenuItem menuItemJournalOffeneLose = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.offenelose"), null);
			menuItemJournalOffeneLose.addActionListener(this);
			menuItemJournalOffeneLose
					.setActionCommand(MENUE_ACTION_JOURNAL_OFFENE_LOSE);
			WrapperMenuItem menuItemJournalAblieferungsstatistik = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.ablieferungsstatistik"),
					null);
			menuItemJournalAblieferungsstatistik.addActionListener(this);
			menuItemJournalAblieferungsstatistik
					.setActionCommand(MENUE_ACTION_JOURNAL_ABLIEFERUNGSSTATISTIK);
			WrapperMenuItem menuItemJournalHalbfertigfabrikatsinventur = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.halbfertigfabrikatinventur"),
					null);
			menuItemJournalHalbfertigfabrikatsinventur.addActionListener(this);
			menuItemJournalHalbfertigfabrikatsinventur
					.setActionCommand(MENUE_ACTION_JOURNAL_HALBFERTIGFABRIKATSINVENTUR);
			WrapperMenuItem menuItemJournalAufloesbareFehlmengen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.report.aufloesbarefehlmengen"),
					null);
			menuItemJournalAufloesbareFehlmengen.addActionListener(this);
			menuItemJournalAufloesbareFehlmengen
					.setActionCommand(MENUE_ACTION_JOURNAL_AUFLOESBARE_FEHLMENGEN);

			WrapperMenuItem menuItemJournalFehlmengenAllerLose = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.report.fehlmengenallerlose"),
					null);
			menuItemJournalFehlmengenAllerLose.addActionListener(this);
			menuItemJournalFehlmengenAllerLose
					.setActionCommand(MENUE_ACTION_JOURNAL_FEHLMENGEN_ALLER_LOSE);

			WrapperMenuItem menuItemJournalStueckrueckmeldung = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.stueckrueckmeldung"),
					null);
			menuItemJournalStueckrueckmeldung.addActionListener(this);
			menuItemJournalStueckrueckmeldung
					.setActionCommand(MENUE_ACTION_JOURNAL_STUECKRUECKMELDUNG);
			WrapperMenuItem menuItemJournalStatistik = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.losstatistik"),
					RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemJournalStatistik.addActionListener(this);
			menuItemJournalStatistik
					.setActionCommand(MENU_ACTION_JOURNAL_STATISTIK);

			JMenuItem menuItemJournalMonatsauswertung = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.monatsauswertung"));
			menuItemJournalMonatsauswertung.addActionListener(this);
			menuItemJournalMonatsauswertung
					.setActionCommand(MENU_ACTION_JOURNAL_MONATSAUSWERTUNG);

			JMenuItem menuItemJournalOffeneAg = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.offeneag"));
			menuItemJournalOffeneAg.addActionListener(this);
			menuItemJournalOffeneAg
					.setActionCommand(MENUE_ACTION_JOURNAL_OFFENE_AG);

			jmJournal.add(menuItemJournalAlleLose, 0);
			jmJournal.add(menuItemJournalOffeneLose, 1);
			jmJournal.add(menuItemJournalOffeneAg, 2);
			jmJournal.add(menuItemJournalAblieferungsstatistik, 3);
			jmJournal.add(new JSeparator(), 4);
			jmJournal.add(menuItemJournalHalbfertigfabrikatsinventur, 5);
			jmJournal.add(menuItemJournalAufloesbareFehlmengen, 6);
			jmJournal.add(menuItemJournalFehlmengenAllerLose, 7);
			jmJournal.add(new JSeparator(), 8);
			jmJournal.add(menuItemJournalStueckrueckmeldung, 9);
			jmJournal.add(new JSeparator(), 10);
			jmJournal.add(menuItemJournalStatistik, 11);
			jmJournal.add(menuItemJournalMonatsauswertung, 12);
			jmJournal.add(new JSeparator(), 13);

			JMenuItem menuItemJournalZeitentwicklung = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.zeitentwicklung"));
			menuItemJournalZeitentwicklung.addActionListener(this);
			menuItemJournalZeitentwicklung
					.setActionCommand(MENUE_ACTION_JOURNAL_ZEITENTWICKLUNG);

			jmJournal.add(menuItemJournalZeitentwicklung, 13);

			JMenuItem menuItemJournalAuslastungsvorschau = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.auslastungsvorschau"));
			menuItemJournalAuslastungsvorschau.addActionListener(this);
			menuItemJournalAuslastungsvorschau
					.setActionCommand(MENUE_ACTION_JOURNAL_AUSLASTUNGSVORSCHAU);

			jmJournal.add(menuItemJournalAuslastungsvorschau, 14);

			JMenuItem menuItemJournalAuslastungsvorschauDetailiert = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.auslastungsvorschaudetailliert"));
			menuItemJournalAuslastungsvorschauDetailiert
					.addActionListener(this);
			menuItemJournalAuslastungsvorschauDetailiert
					.setActionCommand(MENUE_ACTION_JOURNAL_AUSLASTUNGSVORSCHAU_DETAILIERT);

			jmJournal.add(menuItemJournalAuslastungsvorschauDetailiert, 15);

			JMenuItem menuItemJournalAuslieferliste = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.menu.auslieferliste"));
			menuItemJournalAuslieferliste.addActionListener(this);
			menuItemJournalAuslieferliste
					.setActionCommand(MENUE_ACTION_JOURNAL_AUSLIEFERLISTE);

			jmJournal.add(menuItemJournalAuslieferliste, 16);

			JMenuItem menuItemJournalFehlerstatistik = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.los.report.fehlerstatistik"));
			menuItemJournalFehlerstatistik.addActionListener(this);
			menuItemJournalFehlerstatistik
					.setActionCommand(MENUE_ACTION_JOURNAL_FEHLERSTATISTIK);
			jmJournal.add(menuItemJournalFehlerstatistik, 17);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_RANKINGLISTE)) {
				JMenuItem menuItemJournalRankingliste = new JMenuItem(
						LPMain.getTextRespectUISPr("fert.menu.rankingliste"));
				menuItemJournalRankingliste.addActionListener(this);
				menuItemJournalRankingliste
						.setActionCommand(MENUE_ACTION_JOURNAL_RANKINGLISTE);

				jmJournal.add(menuItemJournalRankingliste, 18);
			}

			JMenu menuInfo = new WrapperMenu("lp.info", this);
			JMenuItem menuItemInfoGesamtkalkulation = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.report.gesamtkalkulation"));
			menuItemInfoGesamtkalkulation.addActionListener(this);
			menuItemInfoGesamtkalkulation
					.setActionCommand(MENUE_ACTION_INFO_GESAMTKALKULATION);
			menuInfo.add(menuItemInfoGesamtkalkulation);
			wmb.addJMenuItem(menuInfo);
		}

		// return wmb;
		menuBar = wmb;
		return menuBar;
	}

	protected PanelLosKopfdaten getPanelKopfdaten(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelKopfdaten == null && bNeedInstantiationIfNull) {
			panelKopfdaten = new PanelLosKopfdaten(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kopfdaten"), null, this);
			this.setComponentAt(IDX_KOPFDATEN, panelKopfdaten);
		}
		return panelKopfdaten;
	}

	private void refreshFilterMaterial() throws Throwable {
		if (getLosDto() != null) {
			if (getPanelQueryMaterial(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKSollmaterial(getLosDto().getIId());
				getPanelQueryMaterial(true).setDefaultFilter(krit);
			}
		}
	}

	private PanelQuery getPanelQueryMaterial(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryMaterial == null && bNeedInstantiationIfNull) {

			boolean hatRecht = DelegateFactory
					.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(
							RechteFac.RECHT_FERT_LOS_DARF_ISTMATERIAL_MANUELL_NACHBUCHEN);

			String[] aWhichButtonIUsePositionen = new String[] {
					PanelBasis.ACTION_NEW, PanelBasis.ACTION_KOPIEREN };

			if (sRechtModulweit.equals(RechteFac.RECHT_MODULWEIT_READ)) {

				if (hatRecht == true) {
					aWhichButtonIUsePositionen = null;
				}

			}

			panelQueryMaterial = new PanelQuery(null, null,
					QueryParameters.UC_ID_LOSSOLLMATERIAL,
					aWhichButtonIUsePositionen, getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.material.title"),
					true);
			// mehrfachselekt: fuer dieses QP aktivieren
			panelQueryMaterial.setMultipleRowSelectionEnabled(true);

			panelQueryMaterial
					.createAndSaveAndShowButton(
							"/com/lp/client/res/data_next.png",
							LPMain.getTextRespectUISPr("fert.tooltip.nachtraeglichematerialentnahme"),
							ACTION_SPECIAL_MATERIALENTNAHME,
							RechteFac.RECHT_FERT_LOS_CUD);

			if (sRechtModulweit.equals(RechteFac.RECHT_MODULWEIT_READ)) {

				if (hatRecht == false) {
					panelQueryMaterial
							.createAndSaveAndShowButton(
									"/com/lp/client/res/data_add.png",
									LPMain.getTextRespectUISPr("fert.tooltip.nachtraeglichematerialentnahme.ohnesoll"),
									ACTION_SPECIAL_MATERIALENTNAHME_OHNE_SOLLPOSITION,
									RechteFac.RECHT_FERT_LOS_CUD);
				}

			} else {
				panelQueryMaterial
						.createAndSaveAndShowButton(
								"/com/lp/client/res/data_add.png",
								LPMain.getTextRespectUISPr("fert.tooltip.nachtraeglichematerialentnahme.ohnesoll"),
								ACTION_SPECIAL_MATERIALENTNAHME_OHNE_SOLLPOSITION,
								RechteFac.RECHT_FERT_LOS_CUD);
			}

			panelQueryMaterial
					.createAndSaveAndShowButton(
							"/com/lp/client/res/document_exchange.png",
							LPMain.getTextRespectUISPr("fert.tooltip.istmaterialaendern"),
							ACTION_SPECIAL_ISTMENGE_AENDERN,
							RechteFac.RECHT_FERT_LOS_CUD);

			panelQueryMaterial
					.createAndSaveAndShowButton(
							"/com/lp/client/res/calculator16x16.png",
							LPMain.getTextRespectUISPr("fert.tooltip.sollpreiseneukalkulieren"),
							ACTION_SPECIAL_SOLLPREISE_MATERIAL_NEU_KALKULIEREN,
							RechteFac.RECHT_FERT_LOS_CUD);

			refreshFilterMaterial();
		}
		return panelQueryMaterial;
	}

	private PanelSplit getPanelSplitMaterial(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitMaterial == null && bNeedInstantiationIfNull) {
			panelSplitMaterial = new PanelSplit(getInternalFrame(),
					getPanelDetailMaterial(true), getPanelQueryMaterial(true),
					200);
			setComponentAt(IDX_MATERIAL, panelSplitMaterial);
		}
		return panelSplitMaterial;
	}

	private PanelLosMaterial getPanelDetailMaterial(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailMaterial == null && bNeedInstantiationIfNull) {
			panelDetailMaterial = new PanelLosMaterial(getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.material.title"),
					null, // leer
					this);
		}
		return panelDetailMaterial;
	}

	private void refreshFilterZeitdaten() throws Throwable {
		if (getLosDto() != null && getLosDto().getIId() != null) {
			if (getPanelQueryZeitdaten(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKZeitdaten(getLosDto().getIId());
				getPanelQueryZeitdaten(true).setDefaultFilter(krit);
				getPanelQueryZeitdaten(true).eventYouAreSelected(false);
				getPanelSplitZeitdaten(true);

				FilterKriterium[] kritGutSchlecht = FertigungFilterFactory
						.getInstance().createFKLosgutschlecht(
								(Integer) getPanelQueryZeitdaten(true)
										.getSelectedId());
				getPanelQueryLosgutschlecht(true).setDefaultFilter(
						kritGutSchlecht);
				getPanelQueryLosgutschlecht(true).eventYouAreSelected(false);

			}
		}
	}

	public PanelQuery getPanelQueryZeitdaten(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryZeitdaten == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };
			panelQueryZeitdaten = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_LOSSOLLARBEITSPLAN,
					aWhichButtonIUsePositionen,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.zeitdaten.title"),
					true);

			panelQueryZeitdaten.setMultipleRowSelectionEnabled(true);
			refreshFilterZeitdaten();
		}
		return panelQueryZeitdaten;
	}

	private PanelSplit getPanelSplitZeitdaten(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitZeitdaten == null && bNeedInstantiationIfNull) {
			panelSplitZeitdaten = new PanelSplit(getInternalFrame(),
					getPanelDetailZeitdaten(true),
					getPanelQueryZeitdaten(true), 180);
			setComponentAt(IDX_ARBEITSPLAN, panelSplitZeitdaten);
		}
		return panelSplitZeitdaten;
	}

	private PanelLosZeitdaten getPanelDetailZeitdaten(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailZeitdaten == null && bNeedInstantiationIfNull) {
			panelDetailZeitdaten = new PanelLosZeitdaten(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.zeitdaten.title"),
					null, // leer
					this);
		}
		return panelDetailZeitdaten;
	}

	private void refreshFilterLagerentnahme() throws Throwable {
		if (getLosDto() != null) {
			if (getPanelQueryLagerentnahme(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKLagerentnahme(getLosDto().getIId());
				getPanelQueryLagerentnahme(true).setDefaultFilter(krit);
			}
		}
	}

	private void refreshFilterLoszusatzstatus() throws Throwable {
		if (getLosDto() != null) {
			if (getPanelQueryLoszusatzstatus(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKLoszusazustatus(getLosDto().getIId());
				getPanelQueryLoszusatzstatus(true).setDefaultFilter(krit);
			}
		}
	}

	public void refreshFilterLostechniker() throws Throwable {
		if (getLosDto() != null) {
			if (getPanelQueryLostechniker(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKLostechniker(getLosDto().getIId());
				getPanelQueryLostechniker(true).setDefaultFilter(krit);
			}
		}
	}

	private void refreshFilterLosgutschlecht() throws Throwable {
		if (getLosDto() != null) {
			if (getPanelQueryLosgutschlecht(false) != null) {
				/*
				 * FilterKriterium[] krit = FertigungFilterFactory.getInstance()
				 * .createFKLosgutschlecht(getLosDto().getIId());
				 * getPanelQueryLosgutschlecht(true).setDefaultFilter(krit);
				 */
			}

			getPanelDetailLosGutSchlecht(true);

		}
	}

	private void refreshFilterLoslosklasse() throws Throwable {
		if (getLosDto() != null) {
			if (getPanelQueryLoslosklasse(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKLoslosklasse(getLosDto().getIId());
				getPanelQueryLoslosklasse(true).setDefaultFilter(krit);
			}
		}
	}

	private void refreshFilterLosablieferung() throws Throwable {
		if (getLosDto() != null) {
			if (getPanelQueryAblieferung(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKLosablieferung(getLosDto().getIId());
				getPanelQueryAblieferung(true).setDefaultFilter(krit);
			}
		}
	}

	private void refreshEigenschaften(Integer key) throws Throwable {
		if (panelDetailLoseigenschaft == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
					PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

			panelDetailLoseigenschaft = new PanelDynamisch(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.eigenschaften"),
					panelQueryAuswahl, PanelFac.PANEL_LOSEIGENSCHAFTEN,
					HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse);
			setComponentAt(IDX_PANEL_LOSEIGENSCHAFTEN,
					panelDetailLoseigenschaft);
		}
	}

	private void refreshFilterFehlmengen() throws Throwable {
		if (getLosDto() != null) {
			if (getPanelQueryFehlmengen(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKFehlmengen(getLosDto().getIId());
				getPanelQueryFehlmengen(true).setDefaultFilter(krit);
			}
		}
	}

	private PanelQuery getPanelQueryLagerentnahme(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryLagerentnahme == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };
			panelQueryLagerentnahme = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_LOSLAGERENTNAHME,
					aWhichButtonIUsePositionen,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.lagerentnahme.title"),
					true);
			refreshFilterLagerentnahme();
		}
		return panelQueryLagerentnahme;
	}

	private PanelQuery getPanelQueryLoszusatzstatus(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryLoszusatzstatus == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW };
			panelQueryLoszusatzstatus = new PanelQuery(null, null,
					QueryParameters.UC_ID_LOSZUSATZSTATUS,
					aWhichButtonIUsePositionen, getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.zusatzstatus"), true);
			refreshFilterLoszusatzstatus();
		}
		return panelQueryLoszusatzstatus;
	}

	public PanelQuery getPanelQueryLostechniker(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryLostechniker == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW };
			panelQueryLostechniker = new PanelQuery(null, null,
					QueryParameters.UC_ID_LOSTECHNIKER,
					aWhichButtonIUsePositionen, getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.lostechniker"), true);

			panelQueryLostechniker
					.createAndSaveAndShowButton(
							"/com/lp/client/res/worker16x16.png",
							LPMain.getTextRespectUISPr("fert.los.mehreretechnikerauswaehlen"),
							ACTION_SPECIAL_TECHNIKER,
							RechteFac.RECHT_FERT_TECHNIKER_BEARBEITEN);

			refreshFilterLostechniker();
		}
		return panelQueryLostechniker;
	}

	private PanelQuery getPanelQueryLosgutschlecht(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryGutschlecht == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW };
			panelQueryGutschlecht = new PanelQuery(null, null,
					QueryParameters.UC_ID_LOSGUTSCHLECHT,
					aWhichButtonIUsePositionen, getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.gutschlecht"), true);
			refreshFilterLosgutschlecht();
		}
		return panelQueryGutschlecht;
	}

	private PanelQuery getPanelQueryLoslosklasse(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryLosklasse == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW };
			panelQueryLosklasse = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_LOSLOSKLASSE,
					aWhichButtonIUsePositionen,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.losklassen.title"),
					true);
			refreshFilterLoslosklasse();
		}
		return panelQueryLosklasse;
	}

	public PanelQuery getPanelQueryAblieferung(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryAblieferung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW };
			panelQueryAblieferung = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_LOSABLIEFERUNG,
					aWhichButtonIUsePositionen,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.ablieferung.title"),
					true);
			refreshFilterLosablieferung();

			panelQueryAblieferung
					.createAndSaveAndShowButton(
							"/com/lp/client/res/calculator16x16.png",
							LPMain.getTextRespectUISPr("fert.tooltip.gestehungspreiseneukalkulieren"),
							ACTION_SPECIAL_ABLIEFERUNGEN_NEU_KALKULIEREN,
							RechteFac.RECHT_FERT_LOS_CUD);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN)) {
				panelQueryAblieferung
						.createAndSaveAndShowButton(
								"/com/lp/client/res/cube_yellow_add.png",
								LPMain.getTextRespectUISPr("fert.los.offenegeraetesnrs.tooltip"),
								ACTION_SPECIAL_GERAETESNR_KOPIEREN,
								RechteFac.RECHT_FERT_LOS_CUD);
			}
			panelQueryAblieferung.setMultipleRowSelectionEnabled(true);
		}
		return panelQueryAblieferung;
	}

	private PanelSplit getPanelSplitLagerentnahme(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitLagerentnahme == null && bNeedInstantiationIfNull) {
			panelSplitLagerentnahme = new PanelSplit(getInternalFrame(),
					getPanelDetailLagerentnahme(true),
					getPanelQueryLagerentnahme(true), 200);
			setComponentAt(IDX_LAGERENTNAHME, panelSplitLagerentnahme);
		}
		return panelSplitLagerentnahme;
	}

	private PanelSplit getPanelSplitLoszusatzstatus(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitLoszusatzstatus == null && bNeedInstantiationIfNull) {
			panelSplitLoszusatzstatus = new PanelSplit(getInternalFrame(),
					getPanelDetailLoszusatzstatus(true),
					getPanelQueryLoszusatzstatus(true), 350);
			setComponentAt(IDX_LOSZUSATZSTATUS, panelSplitLoszusatzstatus);
		}
		return panelSplitLoszusatzstatus;
	}

	private PanelSplit getPanelSplitLoslosklasse(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitLosklasse == null && bNeedInstantiationIfNull) {
			panelSplitLosklasse = new PanelSplit(getInternalFrame(),
					getPanelDetailLoslosklasse(true),
					getPanelQueryLoslosklasse(true), 200);
			setComponentAt(IDX_LOSLOSKLASSE, panelSplitLosklasse);
		}
		return panelSplitLosklasse;
	}

	private PanelSplit getPanelSplitAblieferung(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitAblieferung == null && bNeedInstantiationIfNull) {
			panelSplitAblieferung = new PanelSplit(getInternalFrame(),
					getPanelDetailAblieferung(true),
					getPanelQueryAblieferung(true), 180);
			setComponentAt(IDX_ABLIEFERUNG, panelSplitAblieferung);
		}
		return panelSplitAblieferung;
	}

	private PanelSplit getPanelSplitLosgutschlecht(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitGutschlecht == null && bNeedInstantiationIfNull) {
			panelSplitGutschlecht = new PanelSplit(getInternalFrame(),
					getPanelDetailLosGutSchlecht(true),
					getPanelQueryLosgutschlecht(true), 200);
			setComponentAt(IDX_GUTSCHLECHT, panelSplitGutschlecht);
		}
		return panelSplitGutschlecht;
	}

	private PanelSplit getPanelSplitLostechniker(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitLostechniker == null && bNeedInstantiationIfNull) {
			panelSplitLostechniker = new PanelSplit(getInternalFrame(),
					getPanelDetailLostechniker(true),
					getPanelQueryLostechniker(true), 200);
			setComponentAt(IDX_LOSTECHNIKER, panelSplitLostechniker);
		}
		return panelSplitLostechniker;
	}

	private PanelLosLagerentnahme getPanelDetailLagerentnahme(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailLagerentnahme == null && bNeedInstantiationIfNull) {
			panelDetailLagerentnahme = new PanelLosLagerentnahme(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.lagerentnahme.title"),
					null, // leer
					this);
		}
		return panelDetailLagerentnahme;
	}

	private PanelLosFehlmengen getPanelDetailFehlmengen(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailFehlmengen == null && bNeedInstantiationIfNull) {
			panelDetailFehlmengen = new PanelLosFehlmengen(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.fehlmengen.title"),
					null, // leer
					this);
		}
		return panelDetailFehlmengen;
	}

	private PanelLoszusatzstatus getPanelDetailLoszusatzstatus(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailLoszusatzstatus == null && bNeedInstantiationIfNull) {
			panelDetailLoszusatzstatus = new PanelLoszusatzstatus(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.zusatzstatus"), null);
		}
		return panelDetailLoszusatzstatus;
	}

	private PanelLostechniker getPanelDetailLostechniker(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailLostechniker == null && bNeedInstantiationIfNull) {
			panelDetailLostechniker = new PanelLostechniker(getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.lostechniker"), null, this);
		}
		return panelDetailLostechniker;
	}

	private PanelLoslosklasse getPanelDetailLoslosklasse(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailLosklasse == null && bNeedInstantiationIfNull) {
			panelDetailLosklasse = new PanelLoslosklasse(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.losklassen.title"),
					null, // leer
					this);
		}
		return panelDetailLosklasse;
	}

	private PanelLosAblieferung getPanelDetailAblieferung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailAblieferung == null && bNeedInstantiationIfNull) {
			panelDetailAblieferung = new PanelLosAblieferung(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.ablieferung.title"),
					null, // leer
					this);
		}
		return panelDetailAblieferung;
	}

	private PanelLosGutSchlecht getPanelDetailLosGutSchlecht(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailGutschlecht == null && bNeedInstantiationIfNull) {
			panelDetailGutschlecht = new PanelLosGutSchlecht(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.gutschlecht"), null, // leer
					this);
		}
		return panelDetailGutschlecht;
	}

	private PanelTabelle getPanelNachkalkulation() throws Throwable {
		if (panelUebersicht == null) {
			panelUebersicht = new PanelTabelleLosnachkalkulation(
					QueryParameters.UC_ID_LOSNACHKALKULATION,
					LPMain.getTextRespectUISPr("fert.tab.oben.nachkalkulation.title"),
					(InternalFrameFertigung) getInternalFrame());
			setComponentAt(IDX_NACHKALKULATION, panelUebersicht);
		}
		return panelUebersicht;
	}

	protected void gotoAuswahl() {
		this.setSelectedIndex(IDX_AUSWAHL);
	}

	private PanelDialogKriterienLoszeiten getKriterienLoszeiten(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDialogKriterienLoszeiten == null && bNeedInstantiationIfNull) {
			panelDialogKriterienLoszeiten = new PanelDialogKriterienLoszeiten(
					getInternalFrame(), this,
					LPMain.getTextRespectUISPr("fert.title.kriterienloszeiten")
							+ " " + getLosDto().getCNr());
		}
		return panelDialogKriterienLoszeiten;
	}

	private PanelTabelleLoszeiten getPanelTabelleLoszeiten() throws Throwable {
		if (panelTabelleLoszeiten == null) {
			panelTabelleLoszeiten = new PanelTabelleLoszeiten(
					QueryParameters.UC_ID_LOSZEITEN,
					LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.title")
							+ " " + getLosDto().getCNr(), getInternalFrame());

			// default Kriterium vorbelegen
			FilterKriterium[] aFilterKrit = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(
					FertigungFac.KRIT_PERSONAL, true, "true",
					FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium(
					FertigungFac.KRIT_LOS_I_ID, true, losDto.getIId()
							.toString(), FilterKriterium.OPERATOR_EQUAL, false);
			aFilterKrit[0] = krit1;
			aFilterKrit[1] = krit2;

			panelTabelleLoszeiten.setDefaultFilter(aFilterKrit);

			setComponentAt(IDX_ISTZEITDATEN, panelTabelleLoszeiten);
		} else {
			getInternalFrame()
					.setLpTitle(
							InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
							LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.title")
									+ " " + getLosDto().getCNr());

		}
		return panelTabelleLoszeiten;
	}

	public SeriennrChargennrAufLagerDto[] bereinigeBereitsVerbrauchteSnrChnr(
			ArrayList al, SeriennrChargennrAufLagerDto[] snrchnrauflagerDtos,
			Integer artikelIId) {

		ArrayList<SeriennrChargennrAufLagerDto> alVorhandene = new ArrayList<SeriennrChargennrAufLagerDto>();

		for (int i = 0; i < snrchnrauflagerDtos.length; i++) {
			alVorhandene.add(snrchnrauflagerDtos[i]);
		}

		for (int i = 0; i < al.size(); i++) {
			BucheSerienChnrAufLosDto bucheSerienChnrAufLosDto = (BucheSerienChnrAufLosDto) al
					.get(i);
			for (int j = 0; j < alVorhandene.size(); j++) {
				SeriennrChargennrAufLagerDto dto = (SeriennrChargennrAufLagerDto) alVorhandene
						.get(j);
				if (bucheSerienChnrAufLosDto.getArtikelIId().equals(artikelIId)) {
					if (dto.getCSeriennrChargennr().equals(
							bucheSerienChnrAufLosDto.getCSeriennrChargennr())) {
						// dto entfernen, bzw. verringern
						if (bucheSerienChnrAufLosDto.getNMenge().doubleValue() >= dto
								.getNMenge().doubleValue()) {
							alVorhandene.remove(j);
						} else {
							dto.setNMenge(dto.getNMenge().subtract(
									bucheSerienChnrAufLosDto.getNMenge()));
							alVorhandene.set(j, dto);
						}

					}
				}
			}
		}
		SeriennrChargennrAufLagerDto[] temp = new SeriennrChargennrAufLagerDto[alVorhandene
				.size()];
		return (SeriennrChargennrAufLagerDto[]) alVorhandene.toArray(temp);
	}

	public ArrayList<BucheSerienChnrAufLosDto> getAbzubuchendeSeriennrChargen(
			Integer losIId, BigDecimal nMenge, boolean bEsWirdAbgeliefert)
			throws Throwable {

		LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
				.losFindByPrimaryKey(losIId);
		StuecklisteDto stklDto = null;
		if (losDto.getStuecklisteIId() != null) {
			stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());
		}

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());

		boolean bKeineAutomatischeMateriabuchung = ((Boolean) parameter
				.getCWertAsObject());

		// 18630 Kann von Stkl uebersteuert werden
		if (stklDto != null) {
			bKeineAutomatischeMateriabuchung = Helper.short2boolean(stklDto
					.getBKeineAutomatischeMaterialbuchung());
		}

		if (bKeineAutomatischeMateriabuchung == false) {

			boolean bLosausgabeAutomatisch = false;
			boolean bGanzeChargenVerwenden = false;
			boolean bUnterstuecklistenAusgeben = true;
			if (stklDto != null) {

				bUnterstuecklistenAusgeben = Helper.short2boolean(stklDto
						.getBAusgabeunterstueckliste());

				// 18616 Wenn Materialbuchung bei Ablieferung, dann brauch ich
				// bei der Ausgabe nicht weitermachen
				if (Helper.short2boolean(stklDto
						.getBMaterialbuchungbeiablieferung())
						&& bEsWirdAbgeliefert == false) {
					return null;
				}

			}

			parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_LOSAUSGABE_AUTOMATISCH);

			if ((Boolean) parameter.getCWertAsObject()) {
				bLosausgabeAutomatisch = true;
			}

			parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_GANZE_CHARGEN_VERWENDEN);

			if ((Boolean) parameter.getCWertAsObject()) {
				bGanzeChargenVerwenden = true;
			}

			ArrayList al = new ArrayList();
			LoslagerentnahmeDto[] laeger = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.loslagerentnahmeFindByLosIId(losIId);
			HashMap<Integer, List<SeriennrChargennrMitMengeDto>> hmSelSeriennummern = new HashMap<Integer, List<SeriennrChargennrMitMengeDto>>();

			LossollmaterialDto[] dtos = DelegateFactory.getInstance()
					.getFertigungDelegate().lossollmaterialFindByLosIId(losIId);
			for (int i = 0; i < dtos.length; i++) {

				if (dtos[i].getArtikelIId() != null) {
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(dtos[i].getArtikelIId());

					StuecklisteDto stuecklisteDto = DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									dtos[i].getArtikelIId());
					if (stuecklisteDto != null
							&& bUnterstuecklistenAusgeben == false) {
						continue;
					}

					BigDecimal bdAusgegeben = DelegateFactory.getInstance()
							.getFertigungDelegate()
							.getAusgegebeneMenge(dtos[i].getIId());

					BigDecimal sollsatzgroesse = dtos[i].getNMenge().divide(
							losDto.getNLosgroesse(), 10,
							BigDecimal.ROUND_HALF_EVEN);

					BigDecimal erledigteMenge = DelegateFactory.getInstance()
							.getFertigungDelegate()
							.getErledigteMenge(losDto.getIId());

					// Wenn bereits Material ausgegeben ist, muss dieses
					// beruecksichtigt werden

					BigDecimal abzubuchendeMenge = nMenge.add(erledigteMenge)
							.multiply(sollsatzgroesse).subtract(bdAusgegeben);

					abzubuchendeMenge = Helper.rundeKaufmaennisch(
							abzubuchendeMenge, 3);

					if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {

						SeriennrChargennrAufLagerDto[] snrchnrauflagerDtos = DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.getAllSerienChargennrAufLagerInfoDtos(
										artikelDto.getIId(),
										laeger[0].getLagerIId());

						snrchnrauflagerDtos = DialogBucheSerienChargennrAufLos
								.bereinigeBereitsVerbrauchteSnrChnr(al,
										snrchnrauflagerDtos,
										dtos[i].getArtikelIId(),
										laeger[0].getLagerIId());

						if (abzubuchendeMenge.doubleValue() > 0) {

							if (snrchnrauflagerDtos.length == 1
									&& snrchnrauflagerDtos[0].getNMenge()
											.doubleValue() >= abzubuchendeMenge
											.doubleValue()) {

								BucheSerienChnrAufLosDto dtoTemp = new BucheSerienChnrAufLosDto();
								dtoTemp.setArtikelIId(artikelDto.getIId());
								dtoTemp.setLagerIId(laeger[0].getLagerIId());
								dtoTemp.setCSeriennrChargennr(snrchnrauflagerDtos[0]
										.getCSeriennrChargennr());
								dtoTemp.setNMenge(abzubuchendeMenge);
								dtoTemp.setLossollmaterialIId(dtos[i].getIId());
								al.add(dtoTemp);

							} else {

								// PJ 14637

								DialogBucheSerienChargennrAufLos dialog = new DialogBucheSerienChargennrAufLos(
										losDto.getIId(), losDto.getCNr(),
										artikelDto, abzubuchendeMenge, al);
								LPMain.getInstance()
										.getDesktop()
										.platziereDialogInDerMitteDesFensters(
												dialog);

								BigDecimal vorhandeneMenge = new BigDecimal(0);

								for (int u = 0; u < snrchnrauflagerDtos.length; u++) {
									vorhandeneMenge = vorhandeneMenge
											.add(snrchnrauflagerDtos[u]
													.getNMenge());
								}

								if (dialog.bEsIstNichtsAufLager == false) {

									if (bLosausgabeAutomatisch
											&& vorhandeneMenge.doubleValue() >= abzubuchendeMenge
													.doubleValue()) {

										if (bGanzeChargenVerwenden == true) {
											for (int u = 0; u < snrchnrauflagerDtos.length; u++) {

												BucheSerienChnrAufLosDto bucheSerienChnrAufLosDto = new BucheSerienChnrAufLosDto();
												bucheSerienChnrAufLosDto
														.setArtikelIId(artikelDto
																.getIId());
												bucheSerienChnrAufLosDto
														.setCSeriennrChargennr(snrchnrauflagerDtos[u]
																.getCSeriennrChargennr());
												bucheSerienChnrAufLosDto
														.setLossollmaterialIId(dtos[i]
																.getIId());
												bucheSerienChnrAufLosDto
														.setLagerIId(laeger[0]
																.getLagerIId());

												if (abzubuchendeMenge
														.doubleValue() > 0
														&& snrchnrauflagerDtos[u]
																.getNMenge()
																.doubleValue() >= abzubuchendeMenge
																.doubleValue()) {

													bucheSerienChnrAufLosDto
															.setNMenge(abzubuchendeMenge);
													abzubuchendeMenge = new BigDecimal(
															0);
													al.add(bucheSerienChnrAufLosDto);
												}

											}
										}

										for (int u = 0; u < snrchnrauflagerDtos.length; u++) {

											BucheSerienChnrAufLosDto bucheSerienChnrAufLosDto = new BucheSerienChnrAufLosDto();
											bucheSerienChnrAufLosDto
													.setArtikelIId(artikelDto
															.getIId());
											bucheSerienChnrAufLosDto
													.setCSeriennrChargennr(snrchnrauflagerDtos[u]
															.getCSeriennrChargennr());
											bucheSerienChnrAufLosDto
													.setLossollmaterialIId(dtos[i]
															.getIId());
											bucheSerienChnrAufLosDto
													.setLagerIId(laeger[0]
															.getLagerIId());

											if (abzubuchendeMenge.doubleValue() > 0) {
												if (snrchnrauflagerDtos[u]
														.getNMenge()
														.doubleValue() >= abzubuchendeMenge
														.doubleValue()) {
													bucheSerienChnrAufLosDto
															.setNMenge(abzubuchendeMenge);
													abzubuchendeMenge = new BigDecimal(
															0);
												} else {
													bucheSerienChnrAufLosDto
															.setNMenge(snrchnrauflagerDtos[u]
																	.getNMenge());

													abzubuchendeMenge = abzubuchendeMenge
															.subtract(snrchnrauflagerDtos[u]
																	.getNMenge());

												}

												al.add(bucheSerienChnrAufLosDto);
											}

										}

									} else {

										dialog.setVisible(true);

										if (dialog.bAbbruch == false) {

											ArrayList alTemp = dialog
													.getDaten(dtos[i].getIId());
											for (int k = 0; k < alTemp.size(); k++) {
												al.add(alTemp.get(k));
											}
										} else {
											throw new ExceptionLP(
													EJBExceptionLP.FEHLER_ABBUCHUNG_SNRCHNR_ABGEBROCHEN,
													new Exception("ABBRUCH"));
										}
									}
								}
							}
						}
					} else if (Helper.short2boolean(artikelDto
							.getBSeriennrtragend())) {

						List<SeriennrChargennrMitMengeDto> alSeriennummernBereitsSelektiert = hmSelSeriennummern
								.get(dtos[i].getArtikelIId());

						SeriennrChargennrAufLagerDto[] snrsAufLagerdtos = DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.getAllSerienChargennrAufLager(
										dtos[i].getArtikelIId(),
										laeger[0].getLagerIId());

						ArrayList<SeriennrChargennrAufLagerDto> alListeMitSelektiertenBereinigt = new ArrayList<SeriennrChargennrAufLagerDto>();

						for (int u = 0; u < snrsAufLagerdtos.length; u++) {

							SeriennrChargennrAufLagerDto dtoZeile = snrsAufLagerdtos[u];

							boolean bGefunden = false;
							// Bereits selektierte rausfiltern
							if (alSeriennummernBereitsSelektiert != null) {
								for (int j = 0; j < alSeriennummernBereitsSelektiert
										.size(); j++) {

									if (dtoZeile
											.getCSeriennrChargennr()
											.equals(alSeriennummernBereitsSelektiert
													.get(j)
													.getCSeriennrChargennr())) {
										bGefunden = true;
										BigDecimal mengeNeu = dtoZeile
												.getNMenge().subtract(
														alSeriennummernBereitsSelektiert
																.get(j)
																.getNMenge());
										dtoZeile.setNMenge(mengeNeu);
										if (mengeNeu.doubleValue() > 0) {
											alListeMitSelektiertenBereinigt
													.add(dtoZeile);
										}
									}

								}
							}
							if (bGefunden == false) {
								alListeMitSelektiertenBereinigt.add(dtoZeile);
							}
						}

						if (alListeMitSelektiertenBereinigt.size() > 0) {

							DialogSerienChargenauswahl d = new DialogSerienChargenauswahl(
									dtos[i].getArtikelIId(),
									laeger[0].getLagerIId(),
									hmSelSeriennummern.get(dtos[i]
											.getArtikelIId()), true, true,
									getInternalFrame(), null, false);
							LPMain.getInstance().getDesktop()
									.platziereDialogInDerMitteDesFensters(d);

							d.setBdBenoetigteMenge(abzubuchendeMenge);

							d.setVisible(true);

							if (d.alSeriennummern == null
									|| d.alSeriennummern.size() == 0) {
								throw new ExceptionLP(
										EJBExceptionLP.FEHLER_ABBUCHUNG_SNRCHNR_ABGEBROCHEN,
										new Exception("ABBRUCH"));
							}

							List<SeriennrChargennrMitMengeDto> selektierteSnrs = d.alSeriennummern;

							int iAnzahlSelektiert = selektierteSnrs.size();

							for (int k = 0; k < selektierteSnrs.size(); k++) {
								selektierteSnrs.get(k).lossollmaterialIId = dtos[i]
										.getIId();
							}

							if (hmSelSeriennummern.containsKey(dtos[i]
									.getArtikelIId())) {
								List<SeriennrChargennrMitMengeDto> selektierteSnrsVorhanden = hmSelSeriennummern
										.get(dtos[i].getArtikelIId());
								for (int k = 0; k < selektierteSnrsVorhanden
										.size(); k++) {
									selektierteSnrs
											.add(selektierteSnrsVorhanden
													.get(k));
								}
							}

							if (iAnzahlSelektiert != abzubuchendeMenge
									.intValue()) {

								boolean b = DialogFactory
										.showModalJaNeinDialog(
												null,
												LPMain.getTextRespectUISPr("fert.error.zuwenigsnrchnrausgewaehlt"),
												LPMain.getTextRespectUISPr("lp.frage"));
								if (b == true) {
									hmSelSeriennummern.put(
											dtos[i].getArtikelIId(),
											selektierteSnrs);
								} else {
									d.setVisible(true);
								}
							} else {
								hmSelSeriennummern.put(dtos[i].getArtikelIId(),
										selektierteSnrs);
							}

						}
					}
				}
			}

			// SNRS hinzufuegen
			Iterator it = hmSelSeriennummern.keySet().iterator();
			while (it.hasNext()) {

				Integer artikelIId = (Integer) it.next();

				List<SeriennrChargennrMitMengeDto> snrs = hmSelSeriennummern
						.get(artikelIId);

				for (int k = 0; k < snrs.size(); k++) {
					SeriennrChargennrMitMengeDto dto = snrs.get(k);

					BucheSerienChnrAufLosDto bucheSerienChnrAufLosDto = new BucheSerienChnrAufLosDto();
					bucheSerienChnrAufLosDto.setArtikelIId(artikelIId);
					bucheSerienChnrAufLosDto.setCSeriennrChargennr(dto
							.getCSeriennrChargennr());
					bucheSerienChnrAufLosDto
							.setLossollmaterialIId(dto.lossollmaterialIId);
					bucheSerienChnrAufLosDto.setLagerIId(laeger[0]
							.getLagerIId());
					bucheSerienChnrAufLosDto.setNMenge(new BigDecimal(1));

					al.add(bucheSerienChnrAufLosDto);
				}
			}

			return al;

		} else {
			return null;
		}
	}

	public Object getDto() {
		return losDto;
	}

	public void copyHV() throws Throwable {
		Object aoIIdPosition[] = null;

		// Kopiere ich aus Panel Material oder Zeitdaten
		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_MATERIAL) {
			// Kopieren aus Material
			aoIIdPosition = this.panelQueryMaterial.getSelectedIds();
			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {

				// Dialogabfrage ob Soll oder IstMaterial kopiert werden soll.
				Object[] aOptionen = new Object[2];
				final int indexSollMaterial = 0;
				final int indexIstMaterial = 1;
				aOptionen[indexSollMaterial] = LPMain
						.getTextRespectUISPr("fert.frage.sollmaterial");
				aOptionen[indexIstMaterial] = LPMain
						.getTextRespectUISPr("fert.frage.istmaterial");

				int iAuswahl = DialogFactory
						.showModalDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("fert.frage.solloderistmaterialkopieren"),
								LPMain.getTextRespectUISPr("lp.frage"),
								aOptionen, aOptionen[0]);

				LossollmaterialDto[] dtos = new LossollmaterialDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];

				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.lossollmaterialFindByPrimaryKey(
									(Integer) aoIIdPosition[i]);
					if (dtos[i] != null) {
						dtos[i].setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						dtos[i].setBArtikelbezeichnunguebersteuert(Helper
								.boolean2Short(false));

						if (iAuswahl == indexSollMaterial) {
							// Sollpreise kopieren
							dtos[i].setiCopyPasteModus(BelegpositionDto.COPY_PASTE_MODUS_NORMAL);
						} else if (iAuswahl == indexIstMaterial) {
							// Istpreise kopieren
							dtos[i].setiCopyPasteModus(BelegpositionDto.COPY_PASTE_MODUS_IST_PREIS_AUS_LOS);
						}
					}
				}
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}

		} else if (selectedPanelIndex == IDX_ARBEITSPLAN) {
			// Kopieren aus Zeitdaten
			aoIIdPosition = this.panelQueryZeitdaten.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {

				LossollarbeitsplanDto[] dtos = new LossollarbeitsplanDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];

				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];

					dtos[i] = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.lossollarbeitsplanFindByPrimaryKey(
									(Integer) aoIIdPosition[i]);
					if (dtos[i] != null) {
						dtos[i].setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						dtos[i].setBArtikelbezeichnunguebersteuert(Helper
								.boolean2Short(false));
					}
				}
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}

	}

	public void einfuegenHV() throws Throwable {
		// laut WH:070814 gibt es in Los kein einfuegen
		// MR 20080109: Es koennen Zeitdaten eingefuegt werden

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		if (o instanceof BelegpositionDto[]) {
			LossollarbeitsplanDto[] positionDtos = DelegateFactory
					.getInstance()
					.getBelegpostionkonvertierungDelegate()
					.konvertiereNachLossollarbeitsplanDto(
							(BelegpositionDto[]) o);

			int iInserted = 0;
			Integer iId = null;
			if (positionDtos != null) {

				for (int i = 0; i < positionDtos.length; i++) {
					LossollarbeitsplanDto positionDto = positionDtos[i];
					try {
						positionDto.setIId(null);
						// damits hinten angehaengt wird.
						positionDto.setISort(null);
						positionDto.setBelegIId(this.getLosDto().getIId());
						positionDto.setLosIId(this.getLosDto().getIId());

						// naechsthoeheren Arbeitsgang setzen
						// PJ 16108 AG und UAG muss erhalten bleiben
						/*
						 * positionDto.setIArbeitsgangnummer(DelegateFactory
						 * .getInstance().getFertigungDelegate()
						 * .getNextArbeitsgang(this.getLosDto().getIId()));
						 */

						LossollarbeitsplanDto lossollarbeitsplanDto = DelegateFactory
								.getInstance().getFertigungDelegate()
								.updateLossollarbeitsplan(positionDto);
						if (lossollarbeitsplanDto != null) {
							iId = lossollarbeitsplanDto.getIId();
							iInserted++;
						}

					} catch (Throwable t) {
						// nur loggen!
						myLogger.error(t.getMessage(), t);
					}
				}

				// die Liste neu aufbauen
				panelQueryZeitdaten.eventYouAreSelected(false);
				// den Datensatz in der Liste selektieren
				panelQueryZeitdaten.setSelectedId(iId);

				// im Detail den selektierten anzeigen
				panelDetailZeitdaten.eventYouAreSelected(false);
			}

		}
	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI)
			throws Throwable {
	}
}
