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
package com.lp.client.stueckliste;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.xml.sax.SAXException;

import com.lp.client.angebotstkl.AngebotstklFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.assistent.view.AssistentView;
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
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.importassistent.StklImportController;
import com.lp.client.system.ReportEntitylog;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.stueckliste.service.StrukturierterImportSiemensNXDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.service.POSDocument2POSDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;

@SuppressWarnings("static-access")
public class TabbedPaneStueckliste extends TabbedPane implements ICopyPaste {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryStueckliste = null;
	private PanelBasis panelDetailStueckliste = null;

	private PanelQuery panelQueryPositionen = null;
	private PanelStuecklistepositionen panelBottomPositionen = null;
	private PanelSplit panelSplitPositionen = null;

	private PanelQuery panelQueryPositionenErsatz = null;
	private PanelStuecklistepositionenErsatz panelBottomPositionenErsatz = null;
	private PanelSplit panelSplitPositionenErsatz = null;

	private PanelQuery panelQueryEigenschaft = null;
	private PanelBasis panelBottomEigenschaft = null;
	private PanelSplit panelSplitEigenschaft = null;

	private PanelQuery panelQueryArbeitsplan = null;
	private PanelBasis panelBottomArbeitsplan = null;
	private PanelSplit panelSplitArbeitsplan = null;

	private PanelQuery panelQueryAbbuchungslager = null;
	private PanelBasis panelBottomAbbuchungslager = null;
	private PanelSplit panelSplitAbbuchungslager = null;

	public static int IDX_PANEL_AUSWAHL = -1;
	public static int IDX_PANEL_DETAIL = -1;
	public static int IDX_PANEL_POSITIONEN = -1;
	public static int IDX_PANEL_POSITIONENERSATZ = -1;
	public static int IDX_PANEL_ARBEITSPLAN = -1;
	public static int IDX_LAGERENTNAHME = -1;
	public static int IDX_PANEL_EIGENSCHAFTEN = -1;

	public boolean bStuecklistenfreigabe = false;

	private boolean bPositionen = true;

	private PanelDialogStuecklisteKommentar pdStuecklisteKommentar = null;

	private WrapperMenuBar wrapperManuBar = null;

	private final String MENU_INFO_ARBEITSPLAN = "MENU_INFO_ARBEITSPLAN";
	private final String MENU_INFO_GESAMTKALKULATION = "MENU_INFO_GESAMTKALKULATION";
	private final String MENU_INFO_AENDERUNGEN = "MENU_INFO_AENDERUNGEN";

	private final String MENU_XLSIMPORT_ARBEITSPLAN = "MENU_XLSIMPORT_ARBEITSPLAN";
	private final String MENU_XLSIMPORT_MATERIAL = "MENU_XLSIMPORT_MATERIAL";

	private final String MENUE_INVENTUR_ACTION_STUECKLISTE = "MENUE_INVENTUR_ACTION_STUECKLISTE";
	private final String MENU_BEARBEITEN_KOMMENTAR = "MENU_BEARBEITEN_KOMMENTAR";
	private final String MENU_BEARBEITEN_LOSE_AKTUALISIEREN = "MENU_BEARBEITEN_LOSE_AKTUALISIEREN";

	private final String MENU_BEARBEITEN_ARTIKEL_ERSETZEN = "MENU_BEARBEITEN_ARTIKEL_ERSETZEN";
	private final String ACTION_SPECIAL_BAUMSICHT = "ACTION_SPECIAL_BAUMSICHT";
	private final String MENU_STKL_BAUMSICHT = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_BAUMSICHT;

	private static final String ACTION_SPECIAL_IMPORT = "action_special_import";
	private static final String ACTION_SPECIAL_ARBEITSPLAN = "action_special_import_arbeitsplan";
	private static final String ACTION_SPECIAL_IMPORT_AGSTKL = "action_special_import_agstkl";

	private static final String ACTION_SPECIAL_CSVIMPORT_POSITIONEN = "ACTION_SPECIAL_CSVIMPORT_POSITIONEN";
	private static final String ACTION_SPECIAL_CSVIMPORT_ARBEITSPLAN = "ACTION_SPECIAL_CSVIMPORT_ARBEITSPLAN";
	private final String MENUE_ACTION_CSVIMPORT = "MENUE_ACTION_CSVIMPORT";

	private final String BUTTON_IMPORTSTUECKLISTEPOSITIONEN_XLS = PanelBasis.ACTION_MY_OWN_NEW
			+ "IMPORTSTUECKLISTEPOSITIONEN_XLS";

	private final String BUTTON_IMPORTSTUECKLISTEPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_IMPORT;
	private final String BUTTON_IMPORTAGSTKLPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_IMPORT_AGSTKL;
	private final String BUTTON_IMPORTSTUECKLISTEARBEITSPLAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_ARBEITSPLAN;
	private final String BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_CSVIMPORT_POSITIONEN;
	private final String BUTTON_IMPORTCSV_STUECKLISTEARBEISTPLAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_CSVIMPORT_ARBEITSPLAN;
	private PanelQueryFLR panelAgstkl = null;
	private PanelQueryFLR panelStueckliste = null;

	private ISheetImportController sheetImportController = null;

	public int iStrukturierterStklImport = 0;

	public TabbedPaneStueckliste(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"stkl.stueckliste"));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_STRUKTURIERTER_STKLIMPORT,
						ParameterFac.KATEGORIE_STUECKLISTE,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			iStrukturierterStklImport = (Integer) parameter.getCWertAsObject();
		}

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_STUECKLISTENFREIGABE)) {
			bStuecklistenfreigabe = true;
		}

		jbInit();
		initComponents();

		// TODO: Sollte besser uebergeben werden
		setSheetImportController(new SheetImportController());
	}

	public PanelQuery getPanelQueryPersonal() {
		return panelQueryStueckliste;
	}

	public PanelQuery getPanelQueryPositionen() {
		return panelQueryPositionen;
	}

	public ISheetImportController getSheetImportController() {
		return sheetImportController;
	}

	public void setSheetImportController(
			ISheetImportController sheetImportController) {
		this.sheetImportController = sheetImportController;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryStueckliste == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryStueckliste = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance()
							.createStuecklisteMandantCNr(),
					QueryParameters.UC_ID_STUECKLISTE, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.auswahl"),
					true);
			panelQueryStueckliste
					.befuellePanelFilterkriterienDirektUndVersteckte(
							StuecklisteFilterFactory.getInstance()
									.createFKDArtikelnummer(),
							ArtikelFilterFactory.getInstance()
									.createFKDVolltextsuche(),
							StuecklisteFilterFactory.getInstance()
									.createFKVStuecklisteArtikel());
			panelQueryStueckliste.addDirektFilter(StuecklisteFilterFactory
					.getInstance().createFKDErweiterteTextsuche());

			panelQueryStueckliste.addDirektFilter(StuecklisteFilterFactory
					.getInstance().createFKDKundeName());

			panelQueryStueckliste.createAndSaveAndShowButton(
					"/com/lp/client/res/branch_view.png", LPMain.getInstance()
							.getTextRespectUISPr("stkl.baumansicht"),
					MENU_STKL_BAUMSICHT, null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryStueckliste);
		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailStueckliste == null) {
			panelDetailStueckliste = new PanelStueckliste(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
					key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailStueckliste);
		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {
			wrapperManuBar = new WrapperMenuBar(this);

			JMenuItem menuItemModul = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("lp.drucken") + "...");

			JMenuItem menuItemArbeitsplan = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("stkl.arbeitsplan"));
			menuItemArbeitsplan.addActionListener(this);
			menuItemArbeitsplan.setActionCommand(MENU_INFO_ARBEITSPLAN);

			menuItemModul.addActionListener(this);
			menuItemModul.setActionCommand(MENUE_INVENTUR_ACTION_STUECKLISTE);

			JMenu stkimport = new JMenu(LPMain.getInstance()
					.getTextRespectUISPr("lp.import"));

			JMenu modul = (JMenu) wrapperManuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);
			modul.add(new JSeparator(), 0);
			modul.add(menuItemModul, 0);
			modul.add(menuItemArbeitsplan, 1);
			modul.add(stkimport, 2);

			JMenuItem menuItemImportMaterial = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"stkl.title.panel.positionen"));
			menuItemImportMaterial.addActionListener(this);
			menuItemImportMaterial.setActionCommand(MENU_XLSIMPORT_MATERIAL);
			stkimport.add(menuItemImportMaterial);

			JMenuItem menuItemImportArbeitsplan = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("stkl.arbeitsplan"));
			menuItemImportArbeitsplan.addActionListener(this);
			menuItemImportArbeitsplan
					.setActionCommand(MENU_XLSIMPORT_ARBEITSPLAN);
			stkimport.add(menuItemImportArbeitsplan);

			if (iStrukturierterStklImport != 0) {
				// CSV-Import

				String text = LPMain.getInstance().getTextRespectUISPr(
						"lp.import");

				if (iStrukturierterStklImport == 1) {
					text = "Solid-Works- " + text;
				}
				if (iStrukturierterStklImport == 2) {
					text = "Siemens NX- " + text;
				}
				if (iStrukturierterStklImport == 3) {
					text = "INFRA- " + text;
				}

				JMenuItem menuItemCsvImport = new JMenuItem(text);
				menuItemCsvImport.addActionListener(this);
				menuItemCsvImport.setActionCommand(MENUE_ACTION_CSVIMPORT);
				stkimport.add(menuItemCsvImport);
			}
			JMenu jmBearbeiten = (JMenu) wrapperManuBar
					.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

			JMenuItem menuItemBearbeitenLoseAktualisieren = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.bearbeiten.loseaktualisieren"));
			menuItemBearbeitenLoseAktualisieren.addActionListener(this);
			menuItemBearbeitenLoseAktualisieren
					.setActionCommand(MENU_BEARBEITEN_LOSE_AKTUALISIEREN);
			jmBearbeiten.add(menuItemBearbeitenLoseAktualisieren, 0);

			JMenuItem menuItemBearbeitenArtikelErsetzen = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"stkl.artikel.ersetztendurch"));
			menuItemBearbeitenArtikelErsetzen.addActionListener(this);
			menuItemBearbeitenArtikelErsetzen
					.setActionCommand(MENU_BEARBEITEN_ARTIKEL_ERSETZEN);
			jmBearbeiten.add(menuItemBearbeitenArtikelErsetzen, 0);

			JMenuItem menuItemBearbeitenInternerKommentar = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));
			menuItemBearbeitenInternerKommentar.addActionListener(this);
			menuItemBearbeitenInternerKommentar
					.setActionCommand(MENU_BEARBEITEN_KOMMENTAR);
			jmBearbeiten.add(menuItemBearbeitenInternerKommentar, 0);

			jmBearbeiten.add(new JSeparator(), 0);

			JMenuItem menuItemGesamtkalkulation = new JMenuItem(LPMain
					.getInstance()
					.getTextRespectUISPr("stkl.gesamtkalkulation"));
			menuItemGesamtkalkulation.addActionListener(this);
			menuItemGesamtkalkulation
					.setActionCommand(MENU_INFO_GESAMTKALKULATION);

			JMenu menuInfo = new WrapperMenu("lp.info", this);

			menuInfo.add(menuItemGesamtkalkulation);

			JMenuItem menuItemAenderungen = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("lp.report.aenderungen"));
			menuItemAenderungen.addActionListener(this);
			menuItemAenderungen.setActionCommand(MENU_INFO_AENDERUNGEN);
			menuInfo.add(menuItemAenderungen);

			wrapperManuBar.addJMenuItem(menuInfo);

		}
		return wrapperManuBar;

	}

	private void refreshPositionen(Integer iIdI) throws Throwable {

		if (panelQueryPositionen == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			panelQueryPositionen = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStueckliste(
							iIdI), QueryParameters.UC_ID_STUECKLISTEPOSITION,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"stkl.title.panel.positionen"), true);
			panelBottomPositionen = new PanelStuecklistepositionen(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.title.panel.positionen"), null);

			panelQueryPositionen.createAndSaveAndShowButton(
					"/com/lp/client/res/text_code_colored16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.positionen.importausandererstueckliste"),
					BUTTON_IMPORTSTUECKLISTEPOSITIONEN, null);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_AGSTUECKLISTE)) {

				panelQueryPositionen.createAndSaveAndShowButton(
						"/com/lp/client/res/note_add16x16.png",
						LPMain.getInstance().getTextRespectUISPr(
								"as.positionen.importausandererstueckliste"),
						BUTTON_IMPORTAGSTKLPOSITIONEN, null);
			}

			boolean intelligenterStklImport = LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT);

			panelQueryPositionen
					.createAndSaveAndShowButton(
							"/com/lp/client/res/document_into.png",
							LPMain.getInstance()
									.getTextRespectUISPr(
											intelligenterStklImport ? "stkl.intelligenterstklimport"
													: "stkl.positionen.cvsimport"),
							BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN, null);

			ArbeitsplatzparameterDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_DATEI_AUTOMATISCHE_STUECKLISTENERZEUGUNG);

			if (parameter != null && parameter.getCWert() != null
					&& parameter.getCWert().trim().length() > 0) {

				panelQueryPositionen.createAndSaveAndShowButton(
						"/com/lp/client/res/document_gear.png", "XLS_IMPORT",
						BUTTON_IMPORTSTUECKLISTEPOSITIONEN_XLS, null);
			}

			panelQueryPositionen.setMultipleRowSelectionEnabled(true);

			panelSplitPositionen = new PanelSplit(getInternalFrame(),
					panelBottomPositionen, panelQueryPositionen, 240);

			setComponentAt(IDX_PANEL_POSITIONEN, panelSplitPositionen);
		} else {
			// filter refreshen.
			panelQueryPositionen.setDefaultFilter(StuecklisteFilterFactory
					.getInstance().createFKStueckliste(iIdI));
		}
	}

	private void refreshPositionenErsatz(Integer iIdI) throws Throwable {

		if (panelQueryPositionenErsatz == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN };

			panelQueryPositionenErsatz = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance()
							.createFKStuecklistePosersatz(iIdI),
					QueryParameters.UC_ID_POSERSATZ, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.positionen.ersatz"),
					true);
			panelBottomPositionenErsatz = new PanelStuecklistepositionenErsatz(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.positionen.ersatz"),
					null);

			panelSplitPositionenErsatz = new PanelSplit(getInternalFrame(),
					panelBottomPositionenErsatz, panelQueryPositionenErsatz,
					240);

			setComponentAt(IDX_PANEL_POSITIONENERSATZ,
					panelSplitPositionenErsatz);
		} else {
			// filter refreshen.
			panelQueryPositionenErsatz
					.setDefaultFilter(StuecklisteFilterFactory.getInstance()
							.createFKStuecklistePosersatz(iIdI));
		}
	}

	private void refreshArbeitsplan(Integer key) throws Throwable {

		if (panelQueryArbeitsplan == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };
			panelQueryArbeitsplan = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStueckliste(
							key), QueryParameters.UC_ID_STUECKLISTEARBEITSPLAN,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"stkl.arbeitsplan"), true);

			panelQueryArbeitsplan
					.createAndSaveAndShowButton(
							"/com/lp/client/res/text_code_colored16x16.png",
							LPMain.getInstance()
									.getTextRespectUISPr(
											"stkl.positionen.arbeitsplanimportausandererstueckliste"),
							BUTTON_IMPORTSTUECKLISTEARBEITSPLAN, null);

			panelQueryArbeitsplan.createAndSaveAndShowButton(
					"/com/lp/client/res/document_into.png",
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.positionen.cvsimport"),
					BUTTON_IMPORTCSV_STUECKLISTEARBEISTPLAN, null);

			panelBottomArbeitsplan = new PanelStuecklistearbeitsplan(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.arbeitsplan"), null);
			panelQueryArbeitsplan.setMultipleRowSelectionEnabled(true);
			panelSplitArbeitsplan = new PanelSplit(getInternalFrame(),
					panelBottomArbeitsplan, panelQueryArbeitsplan, 180);

			setComponentAt(IDX_PANEL_ARBEITSPLAN, panelSplitArbeitsplan);
		} else {
			// filter refreshen.
			panelQueryArbeitsplan.setDefaultFilter(StuecklisteFilterFactory
					.getInstance().createFKStueckliste(key));
		}
	}

	private void refreshAbbuchungslager(Integer key) throws Throwable {

		if (panelQueryAbbuchungslager == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };
			panelQueryAbbuchungslager = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance()
							.createFKStuecklisteAbbuchungslager(key),
					QueryParameters.UC_ID_STKLAGERENTNAHME,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"stk.tab.oben.abbuchungslager"), true);

			panelBottomAbbuchungslager = new PanelStklagerentnahme(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"stk.tab.oben.abbuchungslager"), null, this);

			panelSplitAbbuchungslager = new PanelSplit(getInternalFrame(),
					panelBottomAbbuchungslager, panelQueryAbbuchungslager, 180);

			setComponentAt(IDX_LAGERENTNAHME, panelSplitAbbuchungslager);
		} else {
			// filter refreshen.
			panelQueryAbbuchungslager.setDefaultFilter(StuecklisteFilterFactory
					.getInstance().createFKStuecklisteAbbuchungslager(key));
		}
	}

	private void dialogAgstkl(ItemChangedEvent e) throws Throwable {
		panelAgstkl = AngebotstklFilterFactory.getInstance()
				.createPanelFLRAgstkl(getInternalFrame(), true, false);
		new DialogQuery(panelAgstkl);
	}

	private void createEigenschaft(Integer key) throws Throwable {

		if (panelQueryEigenschaft == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryEigenschaft = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStueckliste(
							key), QueryParameters.UC_ID_STUECKLISTEEIGENSCHAFT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("lp.eigenschaft"), true);
			panelBottomEigenschaft = new PanelStuecklisteeigenschaft(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.eigenschaft"), null);

			panelSplitEigenschaft = new PanelSplit(getInternalFrame(),
					panelBottomEigenschaft, panelQueryEigenschaft, 350);

			setComponentAt(IDX_PANEL_EIGENSCHAFTEN, panelSplitEigenschaft);
		} else {
			// filter refreshen.
			panelQueryEigenschaft.setDefaultFilter(StuecklisteFilterFactory
					.getInstance().createFKStueckliste(key));
		}
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		int tabIndex = 0;
		IDX_PANEL_AUSWAHL = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		tabIndex++;
		IDX_PANEL_DETAIL = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				IDX_PANEL_DETAIL);
		tabIndex++;
		IDX_PANEL_POSITIONEN = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"stkl.title.panel.positionen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"stkl.title.panel.positionen"), IDX_PANEL_POSITIONEN);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG)) {
			tabIndex++;
			IDX_PANEL_POSITIONENERSATZ = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.positionen.ersatz"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.positionen.ersatz"),
					IDX_PANEL_POSITIONENERSATZ);
		}

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_STUECKLISTE_ARBEITSPLAN)) {
			tabIndex++;
			IDX_PANEL_ARBEITSPLAN = tabIndex;
			insertTab(
					LPMain.getInstance()
							.getTextRespectUISPr("stkl.arbeitsplan"), null,
					null,
					LPMain.getInstance()
							.getTextRespectUISPr("stkl.arbeitsplan"),
					IDX_PANEL_ARBEITSPLAN);
		}

		tabIndex++;
		IDX_LAGERENTNAHME = tabIndex;
		insertTab(LPMain.getTextRespectUISPr("stk.tab.oben.abbuchungslager"),
				null, null,
				LPMain.getTextRespectUISPr("stk.tab.oben.abbuchungslager"),
				IDX_LAGERENTNAHME);

		tabIndex++;
		IDX_PANEL_EIGENSCHAFTEN = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.eigenschaften"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.eigenschaften"),
				IDX_PANEL_EIGENSCHAFTEN);

		createAuswahl();
		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelQueryStueckliste.getSelectedId() == null) {
			getInternalFrame().enableAllMyKidPanelsExceptMe(0, false);
		}

		if ((Integer) panelQueryStueckliste.getSelectedId() != null) {
			getInternalFrameStueckliste().setStuecklisteDto(
					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.stuecklisteFindByPrimaryKey(
									(Integer) panelQueryStueckliste
											.getSelectedId()));
		}

		if ((Integer) panelQueryStueckliste.getSelectedId() != null) {
			getInternalFrameStueckliste().setStuecklisteDto(
					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.stuecklisteFindByPrimaryKey(
									(Integer) panelQueryStueckliste
											.getSelectedId()));
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryStueckliste,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryStueckliste.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("stkl.stueckliste"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameStueckliste().getStuecklisteDto() != null) {
			String sLosgroesse = "0";
			if (getInternalFrameStueckliste().getStuecklisteDto()
					.getNLosgroesse() != null) {
				sLosgroesse = getInternalFrameStueckliste().getStuecklisteDto()
						.getNLosgroesse().intValue()
						+ "";
			}
			if (getInternalFrameStueckliste().getStuecklisteDto()
					.getArtikelDto() != null) {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFrameStueckliste().getStuecklisteDto()
								.getArtikelDto().formatArtikelbezeichnungMitZusatzbezeichnung()
								+ ", LGR: " + sLosgroesse);
			}
		}
	}

	public InternalFrameStueckliste getInternalFrameStueckliste() {
		return (InternalFrameStueckliste) getInternalFrame();
	}

	private PanelBasis getStuecklisteKopfdaten() throws Throwable {
		Integer iIdAngebot = null;

		if (panelDetailStueckliste == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAngebot = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			panelDetailStueckliste = new PanelStueckliste(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
					iIdAngebot); // empty bei leerer angebotsliste

			setComponentAt(IDX_PANEL_DETAIL, panelDetailStueckliste);
		}

		return panelDetailStueckliste;
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENU_XLSIMPORT_ARBEITSPLAN)) {

			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_XLS, false);
			if (files == null || files.length < 1 || files[0] == null) {
				return;
			}

			File pfad = files[0];

			File test = new File(pfad.getAbsolutePath());

			if (test.getAbsolutePath().toLowerCase().endsWith(".xls")) {

				ByteArrayOutputStream ous = null;
				InputStream ios = null;
				try {
					byte[] buffer = new byte[4096];
					ous = new ByteArrayOutputStream();
					ios = new FileInputStream(pfad.getAbsolutePath());
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

				DialogArbeitsplanXLSImport d = new DialogArbeitsplanXLSImport(
						ous.toByteArray(), this);
				d.setSize(500, 500);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

			}

		} else if (e.getActionCommand().equals(MENU_XLSIMPORT_MATERIAL)) {

			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_XLS, false);
			if (files == null || files.length < 1 || files[0] == null) {
				return;
			}

			File pfad = files[0];

			File test = new File(pfad.getAbsolutePath());

			if (test.getAbsolutePath().toLowerCase().endsWith(".xls")) {

				ByteArrayOutputStream ous = null;
				InputStream ios = null;
				try {
					byte[] buffer = new byte[4096];
					ous = new ByteArrayOutputStream();
					ios = new FileInputStream(pfad.getAbsolutePath());
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

				DialogStuecklisteMaterialXLSImport d = new DialogStuecklisteMaterialXLSImport(
						ous.toByteArray(), this);
				d.setSize(500, 500);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_CSVIMPORT)) {

			if (iStrukturierterStklImport == 1) {
				// Dateiauswahldialog
				File[] files = HelperClient.chooseFile(this,
						HelperClient.FILE_FILTER_XLS, false);
				if (files == null || files.length < 1 || files[0] == null) {
					return;
				}
				File f = files[0];
				File[] fileArray = new File(f.getParent()).listFiles();

				Workbook workbook = Workbook.getWorkbook(f);
				Sheet sheet = workbook.getSheet(0);

				try {
					ArrayList<StrukturierterImportDto> gesamtliste = getSheetImportController()
							.importSheet(sheet, fileArray);
					ArrayList alAngelegteArtikel = DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.importiereStuecklistenstruktur(gesamtliste, false,
									null);

					if (alAngelegteArtikel.size() > 0) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < alAngelegteArtikel.size(); i++) {
							sb.append("\n");
							sb.append(alAngelegteArtikel.get(i));
						}

						DialogFactory
								.showMessageMitScrollbar(
										LPMain.getInstance()
												.getTextRespectUISPr(
														"stkl.import.typ1.artikel.neuangelegt"),
										sb.toString());

					}

					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.info"),
									LPMain.getInstance().getTextRespectUISPr(
											"stkl.import.erfolgreich.info"));

				} catch (IOException ex) {
					handleException(ex, true);
				} finally {
					workbook.close();
				}
			} else if (iStrukturierterStklImport == 2) {
				// Die Einheiten mm und mm?? muessen vorhanden sein, sonst
				// return

				try {
					DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.einheitFindByPrimaryKey(
									SystemFac.EINHEIT_MILLIMETER);
				} catch (Exception e1) {
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.error"),
							"Einheit 'mm' im System nicht vorhanden");
					return;
				}
				try {
					DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.einheitFindByPrimaryKey(
									SystemFac.EINHEIT_QUADRATMILLIMETER);
				} catch (Exception e1) {
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.error"),
							"Einheit 'mm??' im System nicht vorhanden");
					return;
				}

				// Dateiauswahldialog
				File[] files = HelperClient.chooseFile(this,
						HelperClient.FILE_FILTER_CSV, false);
				if (files == null || files.length < 1 || files[0] == null) {
					return;
				}
				File f = files[0];

				LPCSVReader reader = new LPCSVReader(new FileReader(f), ';');
				ArrayList<String[]> al = (ArrayList<String[]>) reader.readAll();
				StrukturierterImportSiemensXS xs = new StrukturierterImportSiemensXS();
				ArrayList<StrukturierterImportSiemensNXDto> alTemp = new ArrayList<StrukturierterImportSiemensNXDto>();

				boolean bErsterGueltigerArtikelGefunden = false;
				for (int i = 1; i < al.size(); i++) {

					// Kopfstueckliste muss vorhanden sein

					StrukturierterImportSiemensNXDto dto = xs.setupRow(al
							.get(i));

					if (bErsterGueltigerArtikelGefunden == false
							&& dto.getArtikelIId() != null) {
						bErsterGueltigerArtikelGefunden = true;

						if (dto.getIEbene() != 1) {
							// Erster Artikel muss in Ebene 1 sein
							DialogFactory
									.showModalDialog(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.error"),
											"Der erste g\u00FCltige Artikel ("
													+ dto.getArtikelnummer()
													+ ") muss sich in Ebene 1 befinden.");
							return;

						}

					}

					if (bErsterGueltigerArtikelGefunden == true) {

						if (al.get(i).length >= 10) {

							// Nur Artikel mit 'm' in Spalte 10 importieren
							if (al.get(i)[9].trim().equals("m")) {

								if (dto.getArtikelIId() != null) {
									alTemp.add(dto);

									// Wenn in Spalte 6 (Material) anhand der
									// ersten Stellen bis zum '_' ein Artikel
									// gefunden
									// werden kann, dann wird eine Stueckliste +
									// Position angelegt

									StrukturierterImportSiemensNXDto dtoHS = xs
											.setupRowRohmaterial(al.get(i));

									if (dtoHS.getArtikelIId() != null) {

										// Rohmaterial muss in Helium in m oder
										// m?? definiert sein, sonst
										// Fehlermeldung
										// SystemFac.EINHEIT_METER oder
										// SystemFac.EINHEIT_QUADRATMETER

										ArtikelDto artikelDtoRohmaterial = DelegateFactory
												.getInstance()
												.getArtikelDelegate()
												.artikelFindByPrimaryKey(
														dtoHS.getArtikelIId());

										if (artikelDtoRohmaterial
												.getEinheitCNr()
												.equals(SystemFac.EINHEIT_METER)
												|| artikelDtoRohmaterial
														.getEinheitCNr()
														.equals(SystemFac.EINHEIT_QUADRATMETER)) {
											alTemp.add(dtoHS);
										} else {

											DialogFactory
													.showModalDialog(
															LPMain.getInstance()
																	.getTextRespectUISPr(
																			"lp.error"),
															"Der Artikel '"
																	+ dtoHS.getArtikelnummer()
																	+ "' muss in HeliumV die Einheit m oder m?? hinterlegt haben. Der Import wird abgebrochen.");

											return;
										}

									}
								}
							}
						}
					}
				}

				ArrayList<StrukturierterImportSiemensNXDto> gesamtliste = xs
						.importSheet(alTemp);

				if (gesamtliste != null && gesamtliste.size() > 0) {
					try {

						ArrayList<ArtikelDto> alNichtimportiert = DelegateFactory
								.getInstance()
								.getStuecklisteDelegate()
								.importiereStuecklistenstrukturSiemensNX(
										gesamtliste, alTemp, null);

						if (alNichtimportiert != null
								&& alNichtimportiert.size() > 0) {

							String msg = "Folgende St\u00FCcklisten sind bereits in HeliumV vorhanden und wurden daher nicht importiert:\n\n";
							for (int i = 0; i < alNichtimportiert.size(); i++) {
								msg += alNichtimportiert.get(i)
										.formatArtikelbezeichnung() + "\n";
							}

							DialogFactory.showModalDialog(LPMain.getInstance()
									.getTextRespectUISPr("lp.error"), msg);
						}
					} catch (IOException ex) {
						handleException(ex, true);
					}
				}

			} else if (iStrukturierterStklImport == 3) {
				// PJ18568
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File pfad = chooser.getSelectedFile();

					File test = new File(pfad.getAbsolutePath());

					// Alle Verzeichnis im "test" auflisten...

					String[] DIR = test.list();

					HashMap<String, HashMap<String, byte[]>> hmDateien = new HashMap<String, HashMap<String, byte[]>>();

					for (int i = 0; i < DIR.length; i++) {

						File einzlneDatei = new File(DIR[i]);

						if (einzlneDatei.getAbsolutePath().toLowerCase()
								.endsWith(".xls")) {

							ByteArrayOutputStream ous = null;
							InputStream ios = null;
							try {
								byte[] buffer = new byte[4096];
								ous = new ByteArrayOutputStream();
								ios = new FileInputStream(
										pfad.getAbsolutePath()
												+ System.getProperty("file.separator")
												+ einzlneDatei.getName());
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

							String stueklistenr = einzlneDatei.getName();

							stueklistenr = stueklistenr.substring(0,
									stueklistenr.length() - 4);

							HashMap<String, byte[]> hmDateienTemp = new HashMap<String, byte[]>();
							hmDateienTemp.put("XLS", ous.toByteArray());

							hmDateien.put(stueklistenr, hmDateienTemp);
						}

						System.out.println(DIR[i]);

					}
					for (int i = 0; i < DIR.length; i++) {

						File einzlneDatei = new File(DIR[i]);

						if (einzlneDatei.getAbsolutePath().toLowerCase()
								.endsWith(".pdf")) {

							ByteArrayOutputStream ous = null;
							InputStream ios = null;
							try {
								byte[] buffer = new byte[4096];
								ous = new ByteArrayOutputStream();
								ios = new FileInputStream(
										pfad.getAbsolutePath()
												+ System.getProperty("file.separator")
												+ einzlneDatei.getName());
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

							String stueklistenr = einzlneDatei.getName();

							stueklistenr = stueklistenr.substring(0,
									stueklistenr.length() - 4);

							if (hmDateien.containsKey(stueklistenr)) {

								HashMap<String, byte[]> hmDateienTemp = hmDateien
										.get(stueklistenr);

								hmDateienTemp.put(einzlneDatei.getName(),
										ous.toByteArray());

								hmDateien.put(stueklistenr, hmDateienTemp);

							}

						}

					}

					DelegateFactory.getInstance().getStuecklisteDelegate()
							.importiereStuecklistenINFRA(hmDateien);

				}

			}
		} else {
			if (getInternalFrameStueckliste().getStuecklisteDto() != null) {
				if (e.getActionCommand().equals(
						MENUE_INVENTUR_ACTION_STUECKLISTE)) {
					String add2Title = LPMain.getInstance()
							.getTextRespectUISPr("stkl.stueckliste");
					getInternalFrame().showReportKriterien(
							new ReportStueckliste(
									getInternalFrameStueckliste(), add2Title,
									getInternalFrameStueckliste()
											.getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(
						MENU_BEARBEITEN_LOSE_AKTUALISIEREN)) {

					// PJ18094
					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_LOSE_AKTUALISIEREN,
									ParameterFac.KATEGORIE_STUECKLISTE,
									LPMain.getInstance().getTheClient()
											.getMandant());

					int iOption = (Integer) parameter.getCWertAsObject();

					Object[] options = null;
					int n = -1;
					TreeMap<String, Object[]> tm = null;
					if (iOption == 0) {
						options = new Object[] {
								LPMain.getInstance()
										.getTextRespectUISPr(
												"stkl.loseaktualisieren.selektiertestueckliste"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.abbrechen") };
						n = JOptionPane.showOptionDialog(
								getInternalFrame(),
								LPMain.getInstance().getTextRespectUISPr(
										"stkl.lose.aktualisieren.frage"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.frage"),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);

						if (n == JOptionPane.YES_OPTION) {
							tm = DelegateFactory
									.getInstance()
									.getFertigungDelegate()
									.aktualisiereLoseAusStueckliste(
											getInternalFrameStueckliste()
													.getStuecklisteDto()
													.getIId());
						} else {
							return;
						}

					} else {
						options = new Object[] {
								LPMain.getInstance()
										.getTextRespectUISPr(
												"stkl.loseaktualisieren.selektiertestueckliste"),
								LPMain.getInstance()
										.getTextRespectUISPr(
												"stkl.loseaktualisieren.allestuecklisten"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.abbrechen") };
						n = JOptionPane.showOptionDialog(
								getInternalFrame(),
								LPMain.getInstance().getTextRespectUISPr(
										"stkl.lose.aktualisieren.frage"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.frage"),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == JOptionPane.YES_OPTION) {
							tm = DelegateFactory
									.getInstance()
									.getFertigungDelegate()
									.aktualisiereLoseAusStueckliste(
											getInternalFrameStueckliste()
													.getStuecklisteDto()
													.getIId());
						} else if (n == JOptionPane.NO_OPTION) {
							tm = DelegateFactory.getInstance()
									.getFertigungDelegate()
									.aktualisiereLoseAusStueckliste(null);
						} else {
							return;
						}
					}

					getInternalFrame().showReportKriterien(
							new ReportLoseAktualisiert(getInternalFrame(), tm));

				} else if (e.getActionCommand().equals(
						MENU_BEARBEITEN_KOMMENTAR)) {

					if (!getStuecklisteKopfdaten().isLockedDlg()) {
						refreshPdKommentar();
						getInternalFrame().showPanelDialog(
								pdStuecklisteKommentar);

					}
				} else if (e.getActionCommand().equals(
						MENU_BEARBEITEN_ARTIKEL_ERSETZEN)) {

					DialogArtikelErsetzen d = new DialogArtikelErsetzen(
							getInternalFrameStueckliste());
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);

				} else if (e.getActionCommand().equals(MENU_INFO_ARBEITSPLAN)) {

					String add2Title = LPMain.getInstance()
							.getTextRespectUISPr("stkl.arbeitsplan");
					getInternalFrame().showReportKriterien(
							new ReportArbeitsplan(
									getInternalFrameStueckliste(), add2Title,
									getInternalFrameStueckliste()
											.getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(
						MENU_INFO_GESAMTKALKULATION)) {

					String add2Title = LPMain.getInstance()
							.getTextRespectUISPr("stkl.gesamtkalkulation");
					getInternalFrame().showReportKriterien(
							new ReportStuecklistegesamtkalkulation(
									getInternalFrameStueckliste(), add2Title,
									getInternalFrameStueckliste()
											.getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(MENU_INFO_AENDERUNGEN)) {

					String add2Title = LPMain.getInstance()
							.getTextRespectUISPr("lp.report.aenderungen");
					getInternalFrame().showReportKriterien(
							new ReportEntitylog(HvDtoLogClass.STUECKLISTE,
									getInternalFrameStueckliste()
											.getStuecklisteDto().getIId() + "",
									getInternalFrameStueckliste(), add2Title,
									getInternalFrameStueckliste()
											.getStuecklisteDto()
											.getArtikelDto()
											.formatArtikelbezeichnung()));
				}

			} else {

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"),
						"Bitte zuerst eine St\u00FCckliste ausw\u00E4hlen");

			}
		}
	}

	private void refreshPdKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdStuecklisteKommentar = new PanelDialogStuecklisteKommentar(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.kommentar"), true);
	}

	private void dialogStueckliste(ItemChangedEvent e) throws Throwable {
		panelStueckliste = StuecklisteFilterFactory.getInstance()
				.createPanelFLRStueckliste(getInternalFrame(), null, false);
		new DialogQuery(panelStueckliste);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelQueryStueckliste) {

				Integer iId = (Integer) panelQueryStueckliste.getSelectedId();
				if (iId != null) {
					refreshPositionen(iId);
					setSelectedComponent(panelSplitPositionen);
				}
			} else if (eI.getSource() == panelAgstkl) {
				DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.kopiereAusAgstkl(
								(Integer) panelAgstkl.getSelectedId(),
								getInternalFrameStueckliste()
										.getStuecklisteDto().getIId());
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelStueckliste) {

				if (bPositionen == true) {

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.kopiereStuecklistenPositionen(
									(Integer) panelStueckliste.getSelectedId(),
									getInternalFrameStueckliste()
											.getStuecklisteDto().getIId());
					panelSplitPositionen.eventYouAreSelected(false);
				} else {
					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.kopiereStuecklisteArbeitsplan(
									(Integer) panelStueckliste.getSelectedId(),
									getInternalFrameStueckliste()
											.getStuecklisteDto().getIId());
					panelSplitArbeitsplan.eventYouAreSelected(false);

				}

			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT_AGSTKL)) {
				dialogAgstkl(eI);
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT)) {
				bPositionen = true;
				dialogStueckliste(eI);
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_BAUMSICHT)) {
				DialogStuecklisteStruktur d = new DialogStuecklisteStruktur(
						getInternalFrameStueckliste());
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
			} else if (sAspectInfo
					.endsWith(BUTTON_IMPORTSTUECKLISTEPOSITIONEN_XLS)) {
				bPositionen = true;

				ArbeitsplatzparameterDto parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_DATEI_AUTOMATISCHE_STUECKLISTENERZEUGUNG);

				if (parameter != null && parameter.getCWert() != null
						&& parameter.getCWert().trim().length() > 0) {

					String sPfad = parameter.getCWert();

					File f = new File(sPfad);

					if (f.exists() == false) {
						DialogFactory.showModalDialog("Fehler",
								"Die angegebene Datei '" + sPfad
										+ "' exisitert nicht.");
						return;
					}

					Workbook workbook = Workbook.getWorkbook(f);
					Sheet sheet = workbook.getSheet("HV_Materialpositionen");

					if (sheet == null) {
						DialogFactory
								.showModalDialog(
										"Info",
										"Die Datei enth\u00E4lt kein Tabellenblatt mit dem Namen 'HV_Materialpositionen'");
						return;
					}

					MontageartDto[] stklDto = DelegateFactory.getInstance()
							.getStuecklisteDelegate()
							.montageartFindByMandantCNr();

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] cells = sheet.getRow(i);

						if (cells.length < 6) {
							continue;
						}
						String artikelnummer = cells[0].getContents();
						ArtikelDto aDto = null;
						try {
							aDto = DelegateFactory.getInstance()
									.getArtikelDelegate()
									.artikelFindByCNr(artikelnummer);
						} catch (Exception e) {
						}

						String einheitCnr = cells[6].getContents();
						EinheitDto einheitDto = DelegateFactory
								.getInstance()
								.getSystemDelegate()
								.einheitFindByPrimaryKey(cells[6].getContents());
						BigDecimal menge = null;

						Float fDimension1 = null;
						Float fDimension2 = null;
						Float fDimension3 = null;

						if (cells[1].getType() == CellType.NUMBER
								|| cells[1].getType() == CellType.NUMBER_FORMULA) {
							menge = new BigDecimal(
									((NumberCell) cells[1]).getValue());
						} else {
							continue;
						}

						if (cells[2].getType() == CellType.NUMBER
								|| cells[2].getType() == CellType.NUMBER_FORMULA) {
							fDimension1 = new Float(
									((NumberCell) cells[2]).getValue());
						}
						if (cells[3].getType() == CellType.NUMBER
								|| cells[3].getType() == CellType.NUMBER_FORMULA) {
							fDimension2 = new Float(
									((NumberCell) cells[3]).getValue());
						}
						if (cells[4].getType() == CellType.NUMBER
								|| cells[4].getType() == CellType.NUMBER_FORMULA) {
							fDimension3 = new Float(
									((NumberCell) cells[4]).getValue());
						}

						StuecklistepositionDto posDto = new StuecklistepositionDto();
						if (aDto != null) {
							posDto.setArtikelIId(aDto.getIId());
							posDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						} else {
							posDto.setSHandeingabe(cells[5].getContents());
							posDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
						}

						posDto.setEinheitCNr(einheitCnr);
						posDto.setNMenge(menge);
						posDto.setMontageartIId(stklDto[0].getIId());
						posDto.setFDimension1(fDimension2);
						posDto.setFDimension2(fDimension3);
						posDto.setFDimension3(fDimension1);
						posDto.setStuecklisteIId(getInternalFrameStueckliste()
								.getStuecklisteDto().getIId());

						if (cells.length > 7 && cells[7].getContents() != null) {

							if (cells[7].getContents().length() > 39) {
								posDto.setCPosition(cells[7].getContents()
										.substring(0, 38));
							} else {
								posDto.setCPosition(cells[7].getContents());
							}
						}

						if (cells.length > 8 && cells[8].getContents() != null) {

							if (cells[8].getContents().length() > 79) {
								posDto.setCKommentar(cells[8].getContents()
										.substring(0, 78));
							} else {
								posDto.setCKommentar(cells[8].getContents());
							}
						}

						if (cells.length > 9 && cells[9].getContents() != null
								&& cells[9].getContents().equals("1")) {
							posDto.setBMitdrucken(new Short((short) 1));
						} else {
							posDto.setBMitdrucken(new Short((short) 0));
						}

						DelegateFactory.getInstance().getStuecklisteDelegate()
								.createStuecklisteposition(posDto);

					}
					sheet = workbook.getSheet("HV_Arbeitsplan");

					if (sheet == null) {
						DialogFactory
								.showModalDialog("Info",
										"Die Datei enth\u00E4lt kein Tabellenblatt mit dem Namen 'HV_Arbeitsplan'");
						return;
					}

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] cells = sheet.getRow(i);

						if (cells.length < 5) {
							continue;
						}

						String artikelnummer = cells[0].getContents();

						ArtikelDto aDto = null;
						try {
							aDto = DelegateFactory.getInstance()
									.getArtikelDelegate()
									.artikelFindByCNr(artikelnummer);
						} catch (Exception e) {
							continue;
						}

						Integer iArbeitsgang = null;
						Integer iUnterArbeitsgang = null;

						Long stueckzeit = null;
						Long ruestzeit = null;

						if (cells[1].getType() == CellType.NUMBER
								|| cells[1].getType() == CellType.NUMBER_FORMULA) {
							iArbeitsgang = new Integer(
									(int) ((NumberCell) cells[1]).getValue());
						}

						if (iArbeitsgang == null) {
							iArbeitsgang = DelegateFactory
									.getInstance()
									.getStuecklisteDelegate()
									.getNextArbeitsgang(
											getInternalFrameStueckliste()
													.getStuecklisteDto()
													.getIId());

						}

						if (cells[2].getType() == CellType.NUMBER
								|| cells[2].getType() == CellType.NUMBER_FORMULA) {
							iUnterArbeitsgang = new Integer(
									(int) ((NumberCell) cells[2]).getValue());
						}
						if (cells[3].getType() == CellType.NUMBER
								|| cells[3].getType() == CellType.NUMBER_FORMULA) {
							stueckzeit = new Long(
									(long) ((NumberCell) cells[3]).getValue());
						}
						if (cells[4].getType() == CellType.NUMBER
								|| cells[4].getType() == CellType.NUMBER_FORMULA) {
							ruestzeit = new Long(
									(long) ((NumberCell) cells[4]).getValue());
						}

						StuecklistearbeitsplanDto posDto = new StuecklistearbeitsplanDto();
						posDto.setArtikelIId(aDto.getIId());

						posDto.setIArbeitsgang(iArbeitsgang);
						posDto.setIUnterarbeitsgang(iUnterArbeitsgang);
						posDto.setLRuestzeit(ruestzeit);
						posDto.setLStueckzeit(stueckzeit);
						posDto.setStuecklisteIId(getInternalFrameStueckliste()
								.getStuecklisteDto().getIId());
						posDto.setBNurmaschinenzeit(new Short((short) 0));

						if (cells.length > 5 && cells[5].getContents() != null) {
							MaschineDto mDto;
							try {
								mDto = DelegateFactory
										.getInstance()
										.getZeiterfassungDelegate()
										.maschineFindByCIdentifikationsnr(
												cells[5].getContents());
								posDto.setMaschineIId(mDto.getIId());
							} catch (Exception e) {
							}
						}

						if (cells.length > 6 && cells[6].getContents() != null) {

							if (cells[6].getContents().length() > 79) {
								posDto.setCKommentar(cells[6].getContents()
										.substring(0, 78));
							} else {
								posDto.setCKommentar(cells[6].getContents());
							}
						}

						if (cells.length > 7) {
							posDto.setXLangtext(cells[6].getContents());
						}

						DelegateFactory.getInstance().getStuecklisteDelegate()
								.createStuecklistearbeitsplan(posDto);

					}

					panelQueryPositionen.eventYouAreSelected(false);
				}
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_ARBEITSPLAN)) {
				bPositionen = false;
				dialogStueckliste(eI);
			} else if (sAspectInfo
					.equals(BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN)
					|| sAspectInfo
							.equals(BUTTON_IMPORTCSV_STUECKLISTEARBEISTPLAN)) {
				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT)
						&& sAspectInfo
								.equals(BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN)) {
					if(LPMain.getInstance().getDesktop()
							.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {
						AssistentView av = new AssistentView(
								getInternalFrameStueckliste(),
								LPMain.getTextRespectUISPr("stkl.intelligenterstklimport"),
								new StklImportController(
										getInternalFrameStueckliste().getStuecklisteDto().getIId(),
										StklImportSpezifikation.SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ,
										getInternalFrame()));
						getInternalFrame().showPanelDialog(av);
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), 
								LPMain.getTextRespectUISPr("stkl.error.keinekundesokoberechtigung"));
					}
					// refreshTitle();
					// DialogStuecklisteimportDefinition dialogStk = new
					// DialogStuecklisteimportDefinition(
					// (Integer) panelQueryStueckliste.getSelectedId());
					// //
					// dialogStk.setDefaultCloseOperation(DialogStuecklisteimportDefinition.EXIT_ON_CLOSE);
					// // dialogStk.setTitle("test");
					// dialogStk.getContentPane().setPreferredSize(
					// dialogStk.getSize());
					// dialogStk.pack();
					// dialogStk.setLocationRelativeTo(LPMain.getInstance()
					// .getDesktop());
					// dialogStk.setVisible(true);
					// panelSplitPositionen.eventYouAreSelected(false);
				} else {
					// PJ 14984
					File[] files = HelperClient.chooseFile(this,
							HelperClient.FILE_FILTER_CSV, false);
					File file = null;
					if (files != null && files.length > 0) {
						file = files[0];
					}
					if (file != null) {
						// AD: Smooks Test
						/*
						 * String messageIn =
						 * SmooksHelper.readInputMessage(file.
						 * getAbsolutePath()); try { List<?> smlist =
						 * SmooksHelper
						 * .runSmooksTransform("c:/stueckliste-smooks-config.xml"
						 * , messageIn); System.out.println(smlist.toString());
						 * } catch (Exception e1) { // TODO Auto-generated catch
						 * block e1.printStackTrace(); }
						 */

						// Tab-getrenntes File einlesen.
						LPCSVReader reader = new LPCSVReader(new FileReader(
								file), ';');

						if (sAspectInfo
								.equals(BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN)) {
							// CSV-Format fuer Arbeitsplan:
							// Spalte1: ARTIKELNUMMER
							// Spalte2: MENGE
							// PJ 13479: Damit man Handartikel importieren kann

							// Spalte3: BEZEICHNUNG-WENN HANDARTIKEL
							// Spalte4: EINHEIT-WENN HANDARTIKEL
							// Spalte5: C_POSITION
							// Spalte6: C_KOMMENTAR
							// Spalte7: B_MITDRUCKEN (PJ 14580)
							if (panelBottomPositionen.defaultMontageartDto == null) {
								// DefaultMontageart==null
								return;
							}

							// PJ18091 Abfrage, ob Positionen vorher geloescht
							// werden sollen

							boolean b = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getInstance()
													.getTextRespectUISPr(
															"stk.positionen.csvimport.positionenvorherloeschen"));
							if (b == true) {
								DelegateFactory
										.getInstance()
										.getStuecklisteDelegate()
										.removeAlleStuecklistenpositionen(
												(Integer) panelQueryStueckliste
														.getSelectedId());
							}

							String[] sLine;
							int iZeile = 0;
							ArrayList<StuecklistepositionDto> list = new ArrayList<StuecklistepositionDto>();
							do {
								// zeilenweise einlesen.
								sLine = reader.readNext();
								iZeile++;
								if (sLine != null) {
									if (sLine.length < 7) {
										DialogFactory
												.showModalDialog(
														LPMain.getInstance()
																.getTextRespectUISPr(
																		"lp.error"),
														"CSV-Datei muss genau 7 Spalten beinhalten.");
										return;
									}

									StuecklistepositionDto dto = new StuecklistepositionDto();
									dto.setBMitdrucken(Helper
											.boolean2Short(true));
									dto.setMontageartIId(panelBottomPositionen.defaultMontageartDto
											.getIId());
									dto.setStuecklisteIId((Integer) panelQueryStueckliste
											.getSelectedId());

									ArtikelDto artikelDto = null;
									try {
										artikelDto = DelegateFactory
												.getInstance()
												.getArtikelDelegate()
												.artikelFindByCNr(sLine[0]);
										dto.setArtikelIId(artikelDto.getIId());
										dto.setEinheitCNr(artikelDto
												.getEinheitCNr());
										dto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
										dto.setCPosition(sLine[4]);
										dto.setCKommentar(sLine[5]);
										try {
											dto.setNMenge(new BigDecimal(
													sLine[1]));

											try {

												Short s = new Short(sLine[6]);

												if (s != 0 && s != 1) {
													throw new NumberFormatException();
												}

												dto.setBMitdrucken(s);
												list.add(dto);
											} catch (NumberFormatException ex) {
												DialogFactory
														.showModalDialog(
																LPMain.getInstance()
																		.getTextRespectUISPr(
																				"lp.error"),
																"Keine g\u00FCltiger Wert f\u00FCr 'Mitdrucken' (entweder 0 oder 1) in Zeile "
																		+ iZeile
																		+ ", Spalte 7. Zeile wird \u00FCbersprungen.");
											}
										} catch (NumberFormatException ex) {
											DialogFactory
													.showModalDialog(
															LPMain.getInstance()
																	.getTextRespectUISPr(
																			"lp.error"),
															"Keine g\u00FCltige Zahl in Zeile "
																	+ iZeile
																	+ ", Spalte 2. Zeile wird \u00FCbersprungen.");
										}

									} catch (ExceptionLP ex1) {
										if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {

											if (sLine[2] != null
													&& sLine[2].length() > 0) {
												// Handartikel anlegen
												dto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
												dto.setSHandeingabe(sLine[2]);
												try {
													dto.setNMenge(new BigDecimal(
															sLine[1]));
													dto.setCPosition(sLine[4]);
													dto.setCKommentar(sLine[5]);

													try {
														DelegateFactory
																.getInstance()
																.getSystemDelegate()
																.einheitFindByPrimaryKey(
																		sLine[3]);
														dto.setEinheitCNr(sLine[3]);

														try {

															Short s = new Short(
																	sLine[6]);

															if (s != 0
																	&& s != 1) {
																throw new NumberFormatException();
															}

															dto.setBMitdrucken(s);
															list.add(dto);
														} catch (NumberFormatException ex) {
															DialogFactory
																	.showModalDialog(
																			LPMain.getInstance()
																					.getTextRespectUISPr(
																							"lp.error"),
																			"Keine g\u00FCltiger Wert f\u00FCr 'Mitdrucken' (entweder 0 oder 1) in Zeile "
																					+ iZeile
																					+ ", Spalte 7. Zeile wird \u00FCbersprungen.");
														}

													} catch (ExceptionLP e) {
														if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
															DialogFactory
																	.showModalDialog(
																			LPMain.getInstance()
																					.getTextRespectUISPr(
																							"lp.error"),
																			"Keine g\u00FCltige Einheit "
																					+ sLine[3]
																					+ " in Zeile "
																					+ iZeile
																					+ ", Spalte 4. Zeile wird \u00FCbersprungen.");
														} else {
															handleException(e,
																	true);
														}
													}

												} catch (NumberFormatException ex) {
													DialogFactory
															.showModalDialog(
																	LPMain.getInstance()
																			.getTextRespectUISPr(
																					"lp.error"),
																	"Keine g\u00FCltige Zahl in Zeile "
																			+ iZeile
																			+ ", Spalte 2. Zeile wird \u00FCbersprungen.");
												}
											}

										} else {
											handleException(ex1, true);
										}
									}
								}
							} while (sLine != null);

							if (list.size() > 0) {
								StuecklistepositionDto[] returnArray = new StuecklistepositionDto[list
										.size()];
								returnArray = (StuecklistepositionDto[]) list
										.toArray(returnArray);
								DelegateFactory
										.getInstance()
										.getStuecklisteDelegate()
										.createStuecklistepositions(returnArray);
								panelSplitPositionen.eventYouAreSelected(false);
							}
						} else if (sAspectInfo
								.equals(BUTTON_IMPORTCSV_STUECKLISTEARBEISTPLAN)) {
							// CSV-Format fuer Arbeitsplan:
							// Spalte1: ARTIKELNUMMER
							// Spalte2: STUECKZEIT IN MILLISEKUNDEN
							// Spalte3: RUESTZEIT IN MILLISEKUNDEN

							String[] sLine;
							int iZeile = 0;
							ArrayList<StuecklistearbeitsplanDto> list = new ArrayList<StuecklistearbeitsplanDto>();
							do {
								// zeilenweise einlesen.
								sLine = reader.readNext();
								iZeile++;
								if (sLine != null) {
									if (sLine.length < 3) {
										DialogFactory
												.showModalDialog(
														LPMain.getInstance()
																.getTextRespectUISPr(
																		"lp.error"),
														"CSV-Datei muss genau 3 Spalten beinhalten.");
										return;

									}
									StuecklistearbeitsplanDto dto = new StuecklistearbeitsplanDto();
									dto.setBNurmaschinenzeit(Helper
											.boolean2Short(false));
									dto.setStuecklisteIId((Integer) panelQueryStueckliste
											.getSelectedId());

									Integer i = DelegateFactory
											.getInstance()
											.getStuecklisteDelegate()
											.getNextArbeitsgang(
													(Integer) panelQueryStueckliste
															.getSelectedId());

									if (i != null) {
										dto.setIArbeitsgang(i);
									} else {
										dto.setIArbeitsgang(new Integer(10));
									}

									ArtikelDto artikelDto = null;
									try {
										artikelDto = DelegateFactory
												.getInstance()
												.getArtikelDelegate()
												.artikelFindByCNr(sLine[0]);
										dto.setArtikelIId(artikelDto.getIId());
										try {
											dto.setLStueckzeit(new Long(
													sLine[1]));
											dto.setLRuestzeit(new Long(sLine[2]));

											if (artikelDto
													.getArtikelartCNr()
													.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {

												list.add(dto);
											} else {
												DialogFactory
														.showModalDialog(
																LPMain.getInstance()
																		.getTextRespectUISPr(
																				"lp.error"),
																"Artikel '"
																		+ sLine[0]
																		+ "' in Zeile "
																		+ iZeile
																		+ ", Spalte 1 ist kein Arbeitszeitartikel. Zeile wird \u00FCbersprungen.");

											}
										} catch (NumberFormatException ex) {
											DialogFactory
													.showModalDialog(
															LPMain.getInstance()
																	.getTextRespectUISPr(
																			"lp.error"),
															"Keine g\u00FCltige Zahl in Zeile "
																	+ iZeile
																	+ ", Spalte 2/3. Zeile wird \u00FCbersprungen.");
										}
									} catch (ExceptionLP ex1) {
										if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
											DialogFactory
													.showModalDialog(
															LPMain.getInstance()
																	.getTextRespectUISPr(
																			"lp.error"),
															"Artikel '"
																	+ sLine[0]
																	+ "' in Zeile "
																	+ iZeile
																	+ ", Spalte 1 nicht gefunden. Zeile wird \u00FCbersprungen.");
										} else {
											handleException(ex1, true);
										}
									}
								}
							} while (sLine != null);

							if (list.size() > 0) {
								StuecklistearbeitsplanDto[] returnArray = new StuecklistearbeitsplanDto[list
										.size()];
								returnArray = (StuecklistearbeitsplanDto[]) list
										.toArray(returnArray);
								DelegateFactory
										.getInstance()
										.getStuecklisteDelegate()
										.createStuecklistearbeitsplans(
												returnArray);
								panelSplitArbeitsplan
										.eventYouAreSelected(false);
							}
						}
					} else {
						// keine auswahl
					}
				}
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				einfuegenHV();
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailStueckliste) {
				panelQueryStueckliste.clearDirektFilter();
				Object oKey = panelDetailStueckliste.getKeyWhenDetailPanel();
				panelQueryStueckliste.setSelectedId(oKey);
			} else if (eI.getSource() == panelBottomPositionen) {
				Object oKey = panelBottomPositionen.getKeyWhenDetailPanel();
				panelQueryPositionen.eventYouAreSelected(false);
				panelQueryPositionen.setSelectedId(oKey);
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionenErsatz) {
				Object oKey = panelBottomPositionenErsatz
						.getKeyWhenDetailPanel();
				panelQueryPositionenErsatz.eventYouAreSelected(false);
				panelQueryPositionenErsatz.setSelectedId(oKey);
				panelSplitPositionenErsatz.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomEigenschaft) {
				Object oKey = panelBottomEigenschaft.getKeyWhenDetailPanel();
				panelQueryEigenschaft.eventYouAreSelected(false);
				panelQueryEigenschaft.setSelectedId(oKey);
				panelSplitEigenschaft.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomArbeitsplan) {
				Object oKey = panelBottomArbeitsplan.getKeyWhenDetailPanel();
				panelQueryArbeitsplan.eventYouAreSelected(false);
				panelQueryArbeitsplan.setSelectedId(oKey);
				panelSplitArbeitsplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAbbuchungslager) {
				Object oKey = panelBottomAbbuchungslager
						.getKeyWhenDetailPanel();
				panelQueryAbbuchungslager.eventYouAreSelected(false);
				panelQueryAbbuchungslager.setSelectedId(oKey);
				panelSplitAbbuchungslager.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryStueckliste) {
				if (panelQueryStueckliste.getSelectedId() != null) {
					getInternalFrameStueckliste().setKeyWasForLockMe(
							panelQueryStueckliste.getSelectedId() + "");

					// Dto-setzen
					createDetail((Integer) panelQueryStueckliste
							.getSelectedId());
					panelDetailStueckliste
							.setKeyWhenDetailPanel(panelQueryStueckliste
									.getSelectedId());
					getInternalFrameStueckliste().setStuecklisteDto(
							DelegateFactory
									.getInstance()
									.getStuecklisteDelegate()
									.stuecklisteFindByPrimaryKey(
											(Integer) panelQueryStueckliste
													.getSelectedId()));

					if (getInternalFrameStueckliste().getStuecklisteDto() != null) {

						String sLosgroesse = "0";
						if (getInternalFrameStueckliste().getStuecklisteDto()
								.getNLosgroesse() != null) {
							sLosgroesse = getInternalFrameStueckliste()
									.getStuecklisteDto().getNLosgroesse()
									.intValue()
									+ "";
						}

						getInternalFrame().setLpTitle(
								InternalFrame.TITLE_IDX_AS_I_LIKE,
								getInternalFrameStueckliste()
										.getStuecklisteDto().getArtikelDto()
										.formatArtikelbezeichnungMitZusatzbezeichnung()
										+ ", LGR: " + sLosgroesse);
					}

					getInternalFrame()
							.enableAllOberePanelsExceptMe(
									this,
									IDX_PANEL_AUSWAHL,
									((ISourceEvent) eI.getSource())
											.getIdSelected() != null);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}
			} else if (eI.getSource() == panelQueryPositionen) {
				Integer iId = (Integer) panelQueryPositionen.getSelectedId();
				panelBottomPositionen.setKeyWhenDetailPanel(iId);
				panelBottomPositionen.eventYouAreSelected(false);
				panelQueryPositionen.updateButtons();
			} else if (eI.getSource() == panelQueryPositionenErsatz) {
				Integer iId = (Integer) panelQueryPositionenErsatz
						.getSelectedId();
				panelBottomPositionenErsatz.setKeyWhenDetailPanel(iId);
				panelBottomPositionenErsatz.eventYouAreSelected(false);
				panelQueryPositionenErsatz.updateButtons();
			} else if (eI.getSource() == panelQueryEigenschaft) {
				Integer iId = (Integer) panelQueryEigenschaft.getSelectedId();
				panelBottomEigenschaft.setKeyWhenDetailPanel(iId);
				panelBottomEigenschaft.eventYouAreSelected(false);
				panelQueryEigenschaft.updateButtons();
			} else if (eI.getSource() == panelQueryArbeitsplan) {
				Integer iId = (Integer) panelQueryArbeitsplan.getSelectedId();
				panelBottomArbeitsplan.setKeyWhenDetailPanel(iId);
				panelBottomArbeitsplan.eventYouAreSelected(false);
				panelQueryArbeitsplan.updateButtons();
			} else if (eI.getSource() == panelQueryAbbuchungslager) {
				Integer iId = (Integer) panelQueryAbbuchungslager
						.getSelectedId();
				panelBottomAbbuchungslager.setKeyWhenDetailPanel(iId);
				panelBottomAbbuchungslager.eventYouAreSelected(false);
				panelQueryAbbuchungslager.updateButtons();
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (eI.getSource() == panelBottomPositionen) {
				panelQueryPositionen.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomArbeitsplan) {
				panelQueryArbeitsplan.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomAbbuchungslager) {
				panelQueryAbbuchungslager.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomEigenschaft) {
				panelQueryEigenschaft.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomPositionenErsatz) {
				panelQueryPositionenErsatz.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelQueryPositionen) {
				int iPos = panelQueryPositionen.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryPositionen
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryPositionen
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheStuecklisteposition(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPositionen.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryPositionenErsatz) {
				int iPos = panelQueryPositionenErsatz.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryPositionenErsatz
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryPositionenErsatz
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauschePosersatz(iIdPosition, iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPositionenErsatz.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryAbbuchungslager) {
				int iPos = panelQueryAbbuchungslager.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryAbbuchungslager
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryAbbuchungslager
							.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheStklagerentnahme(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryAbbuchungslager.setSelectedId(iIdPosition);
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelQueryPositionen) {
				int iPos = panelQueryPositionen.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryPositionen.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryPositionen
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryPositionen
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheStuecklisteposition(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPositionen.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryPositionenErsatz) {
				int iPos = panelQueryPositionenErsatz.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryPositionenErsatz.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryPositionenErsatz
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryPositionenErsatz
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate()
							.vertauschePosersatz(iIdPosition, iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPositionenErsatz.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryAbbuchungslager) {
				int iPos = panelQueryAbbuchungslager.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryAbbuchungslager.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryAbbuchungslager
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryAbbuchungslager
							.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheStklagerentnahme(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAbbuchungslager.setSelectedId(iIdPosition);
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelQueryPositionen) {
				panelBottomPositionen.eventActionNew(eI, true, false);
				panelBottomPositionen.eventYouAreSelected(false); // Buttons
				// schalten
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelDetailStueckliste) {
				this.setSelectedComponent(panelQueryStueckliste);
				setKeyWasForLockMe();
				panelQueryStueckliste.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionen) {
				setKeyWasForLockMe();
				if (panelBottomPositionen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPositionen
							.getId2SelectAfterDelete();
					panelQueryPositionen.setSelectedId(oNaechster);
				}
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionenErsatz) {
				setKeyWasForLockMe();
				if (panelBottomPositionenErsatz.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPositionenErsatz
							.getId2SelectAfterDelete();
					panelQueryPositionenErsatz.setSelectedId(oNaechster);
				}
				panelSplitPositionenErsatz.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomEigenschaft) {
				setKeyWasForLockMe();
				if (panelBottomEigenschaft.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryEigenschaft
							.getId2SelectAfterDelete();
					panelQueryEigenschaft.setSelectedId(oNaechster);
				}
				panelSplitEigenschaft.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomArbeitsplan) {
				setKeyWasForLockMe();
				if (panelBottomArbeitsplan.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArbeitsplan
							.getId2SelectAfterDelete();
					panelQueryArbeitsplan.setSelectedId(oNaechster);
				}
				panelSplitArbeitsplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAbbuchungslager) {
				setKeyWasForLockMe();
				if (panelBottomAbbuchungslager.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAbbuchungslager
							.getId2SelectAfterDelete();
					panelQueryAbbuchungslager.setSelectedId(oNaechster);
				}
				panelSplitAbbuchungslager.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			if (eI.getSource() instanceof AssistentView)
				getAktuellesPanel().eventYouAreSelected(false);
			refreshTitle();
			super.lPEventItemChanged(eI);
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelBottomPositionen) {
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomEigenschaft) {
				panelSplitEigenschaft.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomArbeitsplan) {
				panelSplitArbeitsplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAbbuchungslager) {
				panelSplitAbbuchungslager.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionenErsatz) {
				panelSplitPositionenErsatz.eventYouAreSelected(false);
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryStueckliste) {
				createDetail(null);
				if (panelQueryStueckliste.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailStueckliste.eventActionNew(eI, true, false);
				setSelectedComponent(panelDetailStueckliste);

			} else if (eI.getSource() == panelQueryPositionen) {

				panelBottomPositionen.eventActionNew(eI, true, false);
				panelBottomPositionen.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPositionen);

			} else if (eI.getSource() == panelQueryPositionenErsatz) {

				panelBottomPositionenErsatz.eventActionNew(eI, true, false);
				panelBottomPositionenErsatz.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPositionenErsatz);

			} else if (eI.getSource() == panelQueryEigenschaft) {

				panelBottomEigenschaft.eventActionNew(eI, true, false);
				panelBottomEigenschaft.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitEigenschaft);

			} else if (eI.getSource() == panelQueryArbeitsplan) {

				panelBottomArbeitsplan.eventActionNew(eI, true, false);
				panelBottomArbeitsplan.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitArbeitsplan);

			} else if (eI.getSource() == panelQueryAbbuchungslager) {

				panelBottomAbbuchungslager.eventActionNew(eI, true, false);
				panelBottomAbbuchungslager.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitAbbuchungslager);

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}
	}

	public void copyHV() throws ExceptionLP, Throwable {
		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_PANEL_POSITIONEN) {
			Object aoIIdPosition[] = panelQueryPositionen.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				StuecklistepositionDto[] dtos = new StuecklistepositionDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.stuecklistepositionFindByPrimaryKey(
									(Integer) aoIIdPosition[i]);
				}
				LPMain.getInstance().getPasteBuffer()
						.writeObjectToPasteBuffer(dtos);
			}
		} else if (selectedPanelIndex == IDX_PANEL_ARBEITSPLAN) {
			Object aoIIdPosition[] = panelQueryArbeitsplan.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				StuecklistearbeitsplanDto[] dtos = new StuecklistearbeitsplanDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.stuecklistearbeitsplanFindByPrimaryKey(
									(Integer) aoIIdPosition[i]);
				}
				LPMain.getInstance().getPasteBuffer()
						.writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void einfuegenHV() throws IOException, ParserConfigurationException,
			SAXException, Throwable {

		Object o = LPMain.getInstance().getPasteBuffer()
				.readObjectFromPasteBuffer();

		int selectedPanelIndex = this.getSelectedIndex();
		if (o instanceof BelegpositionDto[]) {
			if (selectedPanelIndex == IDX_PANEL_POSITIONEN) {

				StuecklistepositionDto[] positionDtos = DelegateFactory
						.getInstance().getBelegpostionkonvertierungDelegate()
						.konvertiereNachStklpositionDto((BelegpositionDto[]) o);

				int iInserted = 0;
				if (positionDtos != null) {
					Integer iId = null;
					Boolean b = positionAmEndeEinfuegen();
					if (b != null) {

						for (int i = 0; i < positionDtos.length; i++) {
							StuecklistepositionDto positionDto = positionDtos[i];
							try {
								positionDto.setIId(null);
								positionDto
										.setBelegIId(getInternalFrameStueckliste()
												.getStuecklisteDto().getIId());

								if (b == false) {
									Integer iIdAktuellePosition = (Integer) getPanelQueryPositionen()
											.getSelectedId();

									// erstepos: 0 die erste Position steht an
									// der Stelle 1
									Integer iSortAktuellePosition = new Integer(
											1);

									// erstepos: 1 die erste Position steht an
									// der Stelle 1
									if (iIdAktuellePosition != null) {
										iSortAktuellePosition = DelegateFactory
												.getInstance()
												.getStuecklisteDelegate()
												.stuecklistepositionFindByPrimaryKey(
														iIdAktuellePosition)
												.getISort();

										// Die bestehenden Positionen muessen
										// Platz fuer die neue schaffen
										DelegateFactory
												.getInstance()
												.getStuecklisteDelegate()
												.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
														getInternalFrameStueckliste()
																.getStuecklisteDto()
																.getIId(),
														iSortAktuellePosition
																.intValue());
									}
									// Die neue Position wird an frei gemachte
									// Position gesetzt
									positionDto.setISort(iSortAktuellePosition);
								} else {
									positionDto.setISort(null);
								}

								// Wenn Handeingabe ArtikelID nicht mitkopieren
								if (positionDto.getPositionsartCNr().equals(
										LocaleFac.POSITIONSART_HANDEINGABE)) {
									if (positionDto.getArtikelDto() != null) {
										positionDto.setSHandeingabe(positionDto
												.getArtikelDto()
												.formatBezeichnung());
										positionDto.setArtikelDto(null);
										positionDto.setArtikelIId(null);
									}
								}

								// wir legen eine neue position an
								iId = DelegateFactory.getInstance()
										.getStuecklisteDelegate()
										.createStuecklisteposition(positionDto);
								iInserted++;
							} catch (Throwable t) {
								// nur loggen!
								myLogger.error(t.getMessage(), t);
							}
						}
					}
					// den Datensatz in der Liste selektieren
					panelQueryPositionen.setSelectedId(iId);

					// die Liste neu aufbauen
					panelQueryPositionen.eventYouAreSelected(false);

					// im Detail den selektierten anzeigen
					panelSplitPositionen.eventYouAreSelected(false);
				}

			} else if (selectedPanelIndex == IDX_PANEL_ARBEITSPLAN) {

				StuecklistearbeitsplanDto[] positionDtos = DelegateFactory
						.getInstance()
						.getBelegpostionkonvertierungDelegate()
						.konvertiereNachStklarbeitsplanDto(
								(BelegpositionDto[]) o);

				int iInserted = 0;
				if (positionDtos != null) {
					Integer iId = null;
					for (int i = 0; i < positionDtos.length; i++) {
						StuecklistearbeitsplanDto positionDto = positionDtos[i];
						try {
							positionDto.setIId(null);
							positionDto
									.setStuecklisteIId(getInternalFrameStueckliste()
											.getStuecklisteDto().getIId());
							positionDto.setISort(null);

							// wir legen eine neue position an
							iId = DelegateFactory.getInstance()
									.getStuecklisteDelegate()
									.createStuecklistearbeitsplan(positionDto);
							iInserted++;
						} catch (Throwable t) {
							// nur loggen!
							myLogger.error(t.getMessage(), t);
						}
					}

					// den Datensatz in der Liste selektieren
					panelQueryArbeitsplan.setSelectedId(iId);

					// die Liste neu aufbauen
					panelQueryArbeitsplan.eventYouAreSelected(false);

					// im Detail den selektierten anzeigen
					panelSplitArbeitsplan.eventYouAreSelected(false);
				}
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

		if (true) {
			throw new Exception("saveBSPOSAsTextpos not implemented yet!");
		}

		return null;
	}

	/**
	 * fuelle stuecklistenpositionDtoI von xalOfStuecklistenPOSI mit den
	 * mussfeldern.
	 * 
	 * @param belegposDtoI
	 *            StuecklistepositionDto
	 * @param xalOfStklPosI
	 *            int
	 * @throws Throwable
	 */
	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfStklPosI)
			throws Throwable {

		StuecklistepositionDto stklPosDtoI = (StuecklistepositionDto) belegposDtoI;

		String sPosArt = stklPosDtoI.getPositionsartCNr();
		if (!LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt)
				&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)) {
			String sMsg = "LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt) "
					+ "&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)";
			throw new IllegalArgumentException(sMsg);
		}

		stklPosDtoI.setBelegIId(getInternalFrameStueckliste()
				.getStuecklisteDto().getIId());

		stklPosDtoI.setISort(xalOfStklPosI + 1000);

		if (stklPosDtoI.getNMenge() == null) {
			stklPosDtoI.setNMenge(new BigDecimal(0));
		}

		if (stklPosDtoI.getBMitdrucken() == null) {
			stklPosDtoI.setBMitdrucken(Helper.boolean2Short(false));
		}

		if (stklPosDtoI.getMontageartIId() == null) {
			MontageartDto[] stklDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate().montageartFindByMandantCNr();
			// es muss mindestens 1 art geben!
			stklPosDtoI.setMontageartIId(stklDto[0].getIId());
		}
	}

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		String rechtVorher = getInternalFrame().getRechtModulweit();

		// PJ18550
		if (bStuecklistenfreigabe == true
				&& getInternalFrameStueckliste().getStuecklisteDto() != null
				&& getInternalFrameStueckliste().getStuecklisteDto()
						.getTFreigabe() != null) {
			getInternalFrame()
					.setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
		}

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			getInternalFrame().setRechtModulweit(rechtVorher);
			createAuswahl();
			panelQueryStueckliste.eventYouAreSelected(false);
			if (panelQueryStueckliste.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			panelQueryStueckliste.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFrameStueckliste().getStuecklisteDto() != null) {
				key = getInternalFrameStueckliste().getStuecklisteDto()
						.getIId();
			}
			createDetail(key);
			panelDetailStueckliste.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_POSITIONEN) {
			refreshPositionen(getInternalFrameStueckliste().getStuecklisteDto()
					.getIId());
			panelSplitPositionen.eventYouAreSelected(false);
			panelQueryPositionen.updateButtons();
		} else if (selectedIndex == IDX_PANEL_POSITIONENERSATZ) {

			refreshPositionen(getInternalFrameStueckliste().getStuecklisteDto()
					.getIId());

			if (getPanelQueryPositionen().getSelectedId() != null) {
				refreshPositionenErsatz((Integer) getPanelQueryPositionen()
						.getSelectedId());
				panelSplitPositionenErsatz.eventYouAreSelected(false);
				panelQueryPositionenErsatz.updateButtons();
			} else {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.hinweis"),
						LPMain.getInstance().getTextRespectUISPr(
								"stkl.positionen.ersatz.error"));
				setSelectedComponent(panelSplitPositionen);
			}

		} else if (selectedIndex == IDX_PANEL_ARBEITSPLAN) {
			refreshArbeitsplan(getInternalFrameStueckliste()
					.getStuecklisteDto().getIId());
			panelSplitArbeitsplan.eventYouAreSelected(false);
			panelQueryArbeitsplan.updateButtons();

		} else if (selectedIndex == IDX_LAGERENTNAHME) {
			refreshAbbuchungslager(getInternalFrameStueckliste()
					.getStuecklisteDto().getIId());
			panelSplitAbbuchungslager.eventYouAreSelected(false);
			panelQueryAbbuchungslager.updateButtons();

		} else if (selectedIndex == IDX_PANEL_EIGENSCHAFTEN) {
			createEigenschaft(getInternalFrameStueckliste().getStuecklisteDto()
					.getIId());
			panelSplitEigenschaft.eventYouAreSelected(false);
			panelQueryEigenschaft.updateButtons();

		}
		refreshTitle();

		getInternalFrame().setRechtModulweit(rechtVorher);

	}

}
