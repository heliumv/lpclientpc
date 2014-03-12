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
package com.lp.client.frame.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Map;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.type.OrientationEnum;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dialog.WrapperTimedOptionPane;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.StandarddruckerDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class PanelReportKriterienZweiDrucker extends PanelDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelStandardDrucker panelStandardDruckerTop = null;
	private PanelStandardDrucker panelStandardDruckerBottom = null;
	private JPanel jpaTop = null;
	private JPanel jpaBottom = null;
	private PanelReport panelReportTop = null;
	private PanelReport panelReportBottom = null;
	private PanelReportIfJRDSZweiDrucker jpaPanelReportIf = null;
	private JSplitPane jSplitPane1 = new JSplitPane();
	private JSplitPane jSplitPane2 = new JSplitPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayoutTop = new GridBagLayout();
	private GridBagLayout gridBagLayoutBottom = new GridBagLayout();

	boolean bVariantenInitialisiert = false;

	private boolean bMitEmailFax = false;
	private boolean bNurVorschau = false;
	private boolean bDruckbestaetigung = false;
	private StandarddruckerDto standarddruckerTopDto = null;
	private StandarddruckerDto standarddruckerBottomDto = null;
	private int iKopien = 0;

	private static final String ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_preview";
	private static final String ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_fullscreen";
	private static final String ACTION_SPECIAL_DRUCKEN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_drucken";
	private static final String ACTION_SPECIAL_SAVE = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_save";
	private static final String ACTION_SPECIAL_DRUCKER_TOP_SPEICHERN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_drucker_top_speichern";
	private static final String ACTION_SPECIAL_DRUCKER_TOP_LOESCHEN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_drucker_top_loeschen";

	private static final String ACTION_SPECIAL_DRUCKER_BOTTOM_SPEICHERN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_drucker_bottom_speichern";
	private static final String ACTION_SPECIAL_DRUCKER_BOTTOM_LOESCHEN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_drucker_bottom_loeschen";

	public PanelReportKriterienZweiDrucker(InternalFrame internalFrame,
			PanelReportIfJRDSZweiDrucker panelReportIfTop, String addTitleI,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId,
			boolean bDirekt, boolean bMitEmailFax, boolean bNurVorschau,
			boolean bDruckbestaetigung) throws Throwable {
		super(internalFrame, addTitleI);
		this.jpaPanelReportIf = panelReportIfTop;
		this.bMitEmailFax = bMitEmailFax;
		this.bNurVorschau = bNurVorschau;
		this.bDruckbestaetigung = bDruckbestaetigung;
		jbInitPanel();
		initComponents();
		LockStateValue lockstateValue = new LockStateValue(null, null,
				LOCK_NO_LOCKING);
		this.updateButtons(lockstateValue);
		// Alle Controls aktivieren
		if (panelReportIfTop instanceof PanelBasis) {
			enableAllComponents(this, true);
		}
		// Wenn der Report sofort erstellt und angezeigt werden soll, dann mach
		// das halt
		if (panelReportIfTop.getBErstelleReportSofort()) {
			preview();
		}
		initStandarddrucker();
		// Erweiterte Druckereinstellungen: Dieses Feature ist zzt nicht
		// sichtbar

	}

	private void jbInitPanel() throws Throwable {
		// Button Refresh
		createAndSaveButton("/com/lp/client/res/refresh.png", LPMain
				.getTextRespectUISPr("lp.drucken.reportaktualisieren"),
				ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW, KeyStroke.getKeyStroke(
						KeyEvent.VK_F5, 0), null);
		if (!bNurVorschau) {
			// Button Druckvorschau
			createAndSaveButton("/com/lp/client/res/printer_view.png", LPMain
					.getTextRespectUISPr("lp.drucken.vorschau"),
					ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN, KeyStroke
							.getKeyStroke('R',
									java.awt.event.InputEvent.CTRL_MASK), null);
			// Button Drucken
			createAndSaveButton("/com/lp/client/res/printer.png", LPMain
					.getTextRespectUISPr("lp.drucken.drucken"),
					ACTION_SPECIAL_DRUCKEN, KeyStroke.getKeyStroke('P',
							java.awt.event.InputEvent.CTRL_MASK), null);
		}
		// Button Speichern
		createAndSaveButton("/com/lp/client/res/disk_blue.png", LPMain
				.getTextRespectUISPr("lp.drucken.speichern"),
				ACTION_SPECIAL_SAVE, KeyStroke.getKeyStroke('S',
						java.awt.event.InputEvent.CTRL_MASK), null);
		// Das Action-Array fuer das ToolsPanel erstellen
		String[] aWhichButtonIUse;

		if (bNurVorschau) {
			aWhichButtonIUse = new String[] { ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW };
		}

		else {
			if (bMitEmailFax) {
				aWhichButtonIUse = new String[] {
						ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW,
						ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN,
						ACTION_SPECIAL_DRUCKEN, ACTION_SPECIAL_SAVE };
			} else {
				aWhichButtonIUse = new String[] {
						ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW,
						ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN,
						ACTION_SPECIAL_DRUCKEN, ACTION_SPECIAL_SAVE };
			}

		}

		enableToolsPanelButtons(aWhichButtonIUse);

		// Splitpane ausrichten
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane1.setAutoscrolls(true);
		jSplitPane1.setBorder(BorderFactory.createLineBorder(Color.black));
		jpaWorkingOn.setLayout(gridBagLayout1);

		// Splitpane ausrichten
		jSplitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane2.setAutoscrolls(true);
		jSplitPane2.setBorder(BorderFactory.createLineBorder(Color.black));
		jpaWorkingOn.setLayout(gridBagLayout2);
		// Das Druckerpanel
		panelStandardDruckerTop = new PanelStandardDrucker();
		panelStandardDruckerBottom = new PanelStandardDrucker();
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(jSplitPane2, new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(jSplitPane1, new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		if (jpaPanelReportIf instanceof JPanel) {
			jpaTop = new JPanel(gridBagLayoutTop);
			jpaTop.add(panelStandardDruckerTop, new GridBagConstraints(0,
					iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			iZeile++;
			jpaTop.add((JPanel) jpaPanelReportIf, new GridBagConstraints(0,
					iZeile, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			jpaBottom = new JPanel(gridBagLayoutBottom);
			jpaBottom.add(panelStandardDruckerBottom, new GridBagConstraints(0,
					0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jpaBottom.add((JPanel) jpaPanelReportIf, new GridBagConstraints(0,
					1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			iZeile++;
			jSplitPane1.add(jpaTop, JSplitPane.TOP);
			jSplitPane1.add(new JPanel(), JSplitPane.BOTTOM);
			jSplitPane2.add(jpaBottom, JSplitPane.TOP);
			jSplitPane2.add(new JPanel(), JSplitPane.BOTTOM);
		} else {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	protected final void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		// Refresh ... erzeugt den Report neu und zeigt ihn an
		if (e.getActionCommand().equals(ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW)) {
			preview();
		}
		// grosser Vorschaudialog
		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN)) {
			// print erzeugen, falls das noch nicht passiert ist.
			if (panelReportTop == null || panelReportTop.getPrint() == null) {
				preview();
			}
			// der im unteren panel angezeigte Print wird in die Vorschau
			// uebernommen
			if (panelReportTop != null
					&& panelReportTop.getPrint() != null
					&& panelReportTop.getPrint().getPrint().getPages().size() > 0) {
				// den Timer im Hintergrund abdrehen
				getInternalFrame().getFrameProgress().pause();
				// und jetzt den Dialog zeigen
				JDialog dialog = new JDialogDruckVorschau(LPMain.getInstance()
						.getDesktop(), LPMain
						.getTextRespectUISPr("lp.drucken.vorschau")
						+ ": " + this.getAdd2Title(), panelReportTop.getPrint()
						.getPrint());
				dialog.setModal(true);
			}
		}
		// Report abspeichern
		else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			// Den Report erstellen, falls dies noch nicht geschehen ist.
			if (panelReportTop == null) {
				preview();
			}
			// Report speichern
			if (panelReportTop != null) {
				JasperPrintLP printTop = createPrintIfPrintWasNotCreatedOrUpdateIfNecessaryTop(true);
				new PanelReport(getInternalFrame(), "", printTop)
						.doClickOnSave();
			}
			// Report speichern
			if (panelReportBottom != null) {
				JasperPrintLP printBottom = createPrintIfPrintWasNotCreatedOrUpdateIfNecessaryBottom(true);
				new PanelReport(getInternalFrame(), "", printBottom)
						.doClickOnSave();
			}

		}
		// Ausdrucken
		else if (e.getActionCommand().equals(ACTION_SPECIAL_DRUCKEN)) {
			print();
			if (bDruckbestaetigung) {
				Long lDauer = new Long(2000);
				ArbeitsplatzparameterDto parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_ANZEIGEDAUER_DRUCKBESTAETIGUNG);
				if (parameter != null && parameter.getCWert() != null) {
					lDauer = Long.parseLong(parameter.getCWert()) * 1000;
				}
				if (!lDauer.equals(new Long(0))) {
					WrapperTimedOptionPane.showTimedOptionPane(null, LPMain
							.getTextRespectUISPr("lp.dialog.dokumentgedruckt"),
							"", "", lDauer, JOptionPane.INFORMATION_MESSAGE,
							JOptionPane.DEFAULT_OPTION, new ImageIcon(
									getClass().getResource(
											"/com/lp/client/res/printer.png")),
							null, null);
				}
			}
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_DRUCKER_TOP_SPEICHERN)) {
			// Druckereinstellung speichern
			saveStandarddruckertop();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_DRUCKER_BOTTOM_SPEICHERN)) {
			// Druckereinstellung speichern
			saveStandarddruckerbottom();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_DRUCKER_TOP_LOESCHEN)) {
			// Druckereinstellung speichern
			removeStandarddruckertop();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_DRUCKER_BOTTOM_LOESCHEN)) {
			// Druckereinstellung speichern
			removeStandarddruckerbottom();
		}

	}

	private void preview() throws Throwable {
		myLogger.debug("Erzeuge Report: " + jpaPanelReportIf.getModul() + "|"
				+ jpaPanelReportIf.getReportnameTop());
		long lTime = System.currentTimeMillis();
		boolean bAllowed = true;
		// Pflichtfelder pruefen
		if (jpaPanelReportIf instanceof PanelBasis) {
			if (!((PanelBasis) jpaPanelReportIf).allMandatoryFieldsSetDlg()) {
				bAllowed = false;
			}
		}
		if (bAllowed) {
			// Print erzeugen
			JasperPrintLP printTop = null;
			JasperPrintLP printBottom = null;
			try {
				printTop = createPrintTop();
				printBottom = createPrintBottom();
				if (printTop == null || printBottom == null) {
					// nicht jede Option gibt auch wirklich einen print retour
					myLogger.debug("Report=null nach: "
							+ (System.currentTimeMillis() - lTime) + " ms.");
					return;
				}
				// Erstellungszeit loggen
				myLogger.debug("Report erzeugt nach: "
						+ (System.currentTimeMillis() - lTime) + " ms.");
			} catch (ExceptionLP ex) {
				boolean bHandled = false;
				// individuelle Fehlerbehandlung in PanelBasis
				if (jpaPanelReportIf instanceof PanelBasis) {
					bHandled = ((PanelBasis) jpaPanelReportIf)
							.handleOwnException(ex);
				}
				if (!bHandled) {
					// ansonsten Fehlerdialog anzeigen
					String msg = LPMain.getInstance().getMsg(ex);
					// CK 03.05.2006:WH moechte keine leeren Fehlermeldungen
					// sehen
					if (msg == null) {
						msg = "Fehlercode " + ex.getICode();
						msg = msg + "\n" + ex.getSMsg();
					}
					new DialogError(LPMain.getInstance().getDesktop(), ex,
							DialogError.TYPE_ERROR);
				}
				// Fehler loggen
				myLogger.info("getModul():                  "
						+ jpaPanelReportIf.getModul());
				myLogger.info("getReportname():             "
						+ jpaPanelReportIf.getReportnameTop());
				myLogger.info("Report-Fehler " + ex.getICode() + " nach: "
						+ (System.currentTimeMillis() - lTime) + " ms.");
				return;
			}
			if (printTop.getPrint().getPages().size() == 0) {
				showDialogKeineDatenZuDrucken();
			} else {

				panelReportTop = new PanelReport(getInternalFrame(), this
						.getAdd2Title(), printTop, false);
				jSplitPane1.add(panelReportTop, JSplitPane.BOTTOM);

				panelReportBottom = new PanelReport(getInternalFrame(), this
						.getAdd2Title(), printBottom, false);
				jSplitPane2.add(panelReportBottom, JSplitPane.BOTTOM);

			}
			// Titel setzen
			String sTitle = "(" + LPMain.getTextRespectUISPr("lp.report") + "="
					+ printTop.getSReportName() + ")";
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					sTitle);
		}
	}

	/**
	 * showDialogKeineDatenZuDrucken
	 */
	private void showDialogKeineDatenZuDrucken() {
		// das untere panel weg
		JPanel jpaPanel = new JPanel();
		jpaPanel.setLayout(new BorderLayout());
		JLabel jlaNoPages = new JLabel(LPMain
				.getTextRespectUISPr("lp.hint.nopages"));
		jlaNoPages.setHorizontalAlignment(SwingConstants.CENTER);
		jpaPanel.add(jlaNoPages, BorderLayout.CENTER);
		jSplitPane1.add(jpaPanel, JSplitPane.BOTTOM);
		jSplitPane2.add(jpaPanel, JSplitPane.BOTTOM);
	}

	private JasperPrintLP createPrintIfPrintWasNotCreatedOrUpdateIfNecessaryTop(
			boolean bMitLogo) throws Throwable {
		int iKopienAktuell = 0;
		if (jpaPanelReportIf instanceof ReportBeleg) {
			Integer iiKopienAktuell = ((ReportBeleg) jpaPanelReportIf).wnfKopien
					.getInteger();
			if (iiKopienAktuell != null) {
				iKopienAktuell = iiKopienAktuell.intValue();
			}
		}
		JasperPrintLP printTop = null;
		// wenn er noch nicht erzeugt wurde, muss er jetzt auf jeden fall
		// erzeugt werden
		if (panelReportTop == null || panelReportTop.getPrint() == null) {
			printTop = createPrintTop();
		} else {
			// er ist schon da
			printTop = panelReportTop.getPrint();
			// jetzt muss noch geprueft werden, ob das logo fuer Belege drauf
			// sein soll oder nicht
			if (jpaPanelReportIf instanceof ReportBeleg) {
				((ReportBeleg) jpaPanelReportIf).setBPrintLogo(bMitLogo);
				// wenn der in der falschen form da ist ...
				if (bMitLogo != printTop.getBMitLogo()
						|| iKopien != iKopienAktuell) {
					// ... muss er nochmal erzeugt werden
					printTop = createPrintTop();
				}
			}
		}
		// Kopien merken
		iKopien = iKopienAktuell;
		return printTop;
	}

	private JasperPrintLP createPrintIfPrintWasNotCreatedOrUpdateIfNecessaryBottom(
			boolean bMitLogo) throws Throwable {
		int iKopienAktuell = 0;
		if (jpaPanelReportIf instanceof ReportBeleg) {
			Integer iiKopienAktuell = ((ReportBeleg) jpaPanelReportIf).wnfKopien
					.getInteger();
			if (iiKopienAktuell != null) {
				iKopienAktuell = iiKopienAktuell.intValue();
			}
		}
		JasperPrintLP printBottom = null;
		// wenn er noch nicht erzeugt wurde, muss er jetzt auf jeden fall
		// erzeugt werden
		if (panelReportBottom == null || panelReportBottom.getPrint() == null) {
			printBottom = createPrintTop();
		} else {
			// er ist schon da
			printBottom = panelReportBottom.getPrint();
			// jetzt muss noch geprueft werden, ob das logo fuer Belege drauf
			// sein soll oder nicht
			if (jpaPanelReportIf instanceof ReportBeleg) {
				((ReportBeleg) jpaPanelReportIf).setBPrintLogo(bMitLogo);
				// wenn der in der falschen form da ist ...
				if (bMitLogo != printBottom.getBMitLogo()
						|| iKopien != iKopienAktuell) {
					// ... muss er nochmal erzeugt werden
					printBottom = createPrintBottom();
				}
			}
		}
		// Kopien merken
		iKopien = iKopienAktuell;
		return printBottom;
	}

	private JasperPrintLP createPrintTop() throws Throwable {
		JasperPrintLP print = null;
		try {
			LPMain.getTheClient().setReportvarianteIId(
					panelStandardDruckerTop.getVariante());
			print = jpaPanelReportIf.getReportTop();
			LPMain.getTheClient().setReportvarianteIId(null);
		} catch (ExceptionLP ex) {
			// Reportexceptions hier speziell behandeln
			if (!handleOwnException(ex)) {
				// weiter werfen, fass sie nicht behandelt werden konnte
				throw ex;
			}
		}
		return print;
	}

	private JasperPrintLP createPrintBottom() throws Throwable {
		JasperPrintLP print = null;
		try {
			LPMain.getTheClient().setReportvarianteIId(
					panelStandardDruckerBottom.getVariante());
			
			print = jpaPanelReportIf.getReportBottom();
			LPMain.getTheClient().setReportvarianteIId(null);
		} catch (ExceptionLP ex) {
			// Reportexceptions hier speziell behandeln
			if (!handleOwnException(ex)) {
				// weiter werfen, fass sie nicht behandelt werden konnte
				throw ex;
			}
		}
		return print;
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		if (exfc.getICode() == EJBExceptionLP.FEHLER_DRUCKEN_FILE_NOT_FOUND) {
			String message = LPMain
					.getTextRespectUISPr("lp.error.reportfilenotfound");
			if (exfc.getAlInfoForTheClient() != null
					&& !exfc.getAlInfoForTheClient().isEmpty()) {
				for (Iterator<?> iter = exfc.getAlInfoForTheClient().iterator(); iter
						.hasNext();) {
					String item = (String) iter.next();
					message = message + "\n" + item;
				}
			}
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), message);
			return true;
		} else if (exfc.getICode() == EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DATEN_ZU_DRUCKEN) {
			showDialogKeineDatenZuDrucken();
			return true;
		} else {
			return false;
		}
	}

	public static MailtextDto getDefaultMailtextDto(
			PanelReportIfJRDS panelReportIfJRDS) throws Throwable {
		// report_email: 1 hier wird ein standard-MailtextDto erstellt
		MailtextDto mailtextDto = new MailtextDto();
		mailtextDto.setParamLocale(LPMain.getTheClient().getLocUi());
		mailtextDto.setParamMandantCNr(LPMain.getTheClient().getMandant());
		mailtextDto.setParamModul(panelReportIfJRDS.getModul());
		mailtextDto.setParamXslFile(panelReportIfJRDS.getReportname());
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

	public void print() throws Throwable {
		boolean bCloseDialog = true;
		JasperPrint printTop = createPrintIfPrintWasNotCreatedOrUpdateIfNecessaryTop(
				false).getPrint();
		JasperPrint printBottom = createPrintIfPrintWasNotCreatedOrUpdateIfNecessaryBottom(
				false).getPrint();
		if (printTop.getPages().size() > 0 && printBottom.getPages().size() > 0) {
			try {
				JRPrintServiceExporter exporterTop = new JRPrintServiceExporter();
				JRPrintServiceExporter exporterBottom = new JRPrintServiceExporter();
				// set the report to print
				exporterTop.setParameter(JRExporterParameter.JASPER_PRINT,
						printTop);
				exporterBottom.setParameter(JRExporterParameter.JASPER_PRINT,
						printBottom);
				PrintRequestAttributeSet asetTop = new HashPrintRequestAttributeSet();
				PrintRequestAttributeSet asetBottom = new HashPrintRequestAttributeSet();
				PrintService printerTop = panelStandardDruckerTop
						.getSelectedPrintService();
				PrintService printerBottom = panelStandardDruckerBottom
						.getSelectedPrintService();
				if (printerTop != null) {
					// Hochformat
					if (printTop.getOrientationValue() == OrientationEnum.PORTRAIT) {
						asetTop.add(OrientationRequested.PORTRAIT);
					}
					// Querformat
					else if (printTop.getOrientationValue() == OrientationEnum.LANDSCAPE) {
						asetTop.add(OrientationRequested.LANDSCAPE);
					}

					PrintServiceAttributeSet serviceAttributeSetTop = printerTop
							.getAttributes();

					exporterTop
							.setParameter(
									JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
									asetTop);
					exporterTop
							.setParameter(
									JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
									serviceAttributeSetTop);
					// Erweiterte Druckdialoge zzt nicht anzeigen
					exporterTop
							.setParameter(
									JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
									Boolean.FALSE);
					exporterTop
							.setParameter(
									JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
									Boolean.FALSE);
					exporterTop.exportReport();

				}
				if (printerBottom != null) {
					// Hochformat
					if (printBottom.getOrientationValue() == OrientationEnum.PORTRAIT) {
						asetBottom.add(OrientationRequested.PORTRAIT);
					}
					// Querformat
					else if (printBottom.getOrientationValue() == OrientationEnum.LANDSCAPE) {
						asetBottom.add(OrientationRequested.LANDSCAPE);
					}
					PrintServiceAttributeSet serviceAttributeSetBottom = printerBottom
							.getAttributes();
					exporterBottom
							.setParameter(
									JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
									asetBottom);
					exporterBottom
							.setParameter(
									JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
									serviceAttributeSetBottom);
					// Erweiterte Druckdialoge zzt nicht anzeigen
					exporterBottom
							.setParameter(
									JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
									Boolean.FALSE);
					exporterBottom
							.setParameter(
									JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
									Boolean.FALSE);
					exporterBottom.exportReport();
				}

			} catch (Exception ex) {
				if (ex instanceof ExceptionLP
						&& ((ExceptionLP) ex).getICode() == EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DRUCKER_INSTALLIERT) {
					bCloseDialog = false;
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain
											.getTextRespectUISPr("lp.drucken.keindruckerinstalliert"));
				}

				else if (ex instanceof JRException) {
					throw new JRException(ex);
				} else {
					new DialogError(LPMain.getInstance().getDesktop(), ex,
							DialogError.TYPE_ERROR);
				}
			}
			if (bCloseDialog) {
				getInternalFrame().closePanelDialog();
			}
		} else {
			showDialogKeineDatenZuDrucken();
		}
	}

	private void initStandarddrucker() throws Throwable {
		if (standarddruckerTopDto == null) {
			standarddruckerTopDto = new StandarddruckerDto();
		}
		if (standarddruckerBottomDto == null) {
			standarddruckerBottomDto = new StandarddruckerDto();
		}

		standarddruckerTopDto.setCPc(Helper.getPCName());
		standarddruckerBottomDto.setCPc(Helper.getPCName());
		if (jpaPanelReportIf instanceof ReportBeleg) {
			Integer kstIId = ((ReportBeleg) jpaPanelReportIf)
					.getKostenstelleIId();
			standarddruckerTopDto.setKostenstelleIId_notInDB(kstIId);
			standarddruckerBottomDto.setKostenstelleIId_notInDB(kstIId);
		}
		standarddruckerTopDto.setLocale_notInDB(LPMain.getTheClient()
				.getLocUi());
		standarddruckerTopDto.setMandantCNr(LPMain.getTheClient().getMandant());
		standarddruckerTopDto.setSFilename_notInDB(jpaPanelReportIf
				.getReportnameTop());
		standarddruckerTopDto.setSModul_notInDB(jpaPanelReportIf.getModul());

		standarddruckerBottomDto.setLocale_notInDB(LPMain.getTheClient()
				.getLocUi());
		standarddruckerBottomDto.setMandantCNr(LPMain.getTheClient()
				.getMandant());
		standarddruckerBottomDto.setSFilename_notInDB(jpaPanelReportIf
				.getReportnameBottom());
		standarddruckerBottomDto.setSModul_notInDB(jpaPanelReportIf.getModul());

		if (bVariantenInitialisiert == true
				&& panelStandardDruckerTop.getVariante() == null) {

			standarddruckerTopDto.setReportvarianteIId(null);
			// Drucker holen
			standarddruckerTopDto = DelegateFactory.getInstance()
					.getDruckerDelegate()
					.standarddruckerFindByPcReportnameOhneVariante(
							standarddruckerTopDto);

		} else {

			standarddruckerTopDto.setReportvarianteIId(panelStandardDruckerTop
					.getVariante());
			// Drucker holen
			standarddruckerTopDto = DelegateFactory.getInstance()
					.getDruckerDelegate()
					.standarddruckerFindByPcReportnameOhneExc(
							standarddruckerTopDto);

		}

		if (bVariantenInitialisiert == true
				&& panelStandardDruckerBottom.getVariante() == null) {

			standarddruckerBottomDto.setReportvarianteIId(null);
			// Drucker holen
			standarddruckerBottomDto = DelegateFactory.getInstance()
					.getDruckerDelegate()
					.standarddruckerFindByPcReportnameOhneVariante(
							standarddruckerBottomDto);

		} else {

			standarddruckerBottomDto
					.setReportvarianteIId(panelStandardDruckerBottom
							.getVariante());
			// Drucker holen
			standarddruckerBottomDto = DelegateFactory.getInstance()
					.getDruckerDelegate()
					.standarddruckerFindByPcReportnameOhneExc(
							standarddruckerBottomDto);

		}

		if (bVariantenInitialisiert == false) {

			Map m = DelegateFactory.getInstance().getDruckerDelegate()
					.holeAlleVarianten(jpaPanelReportIf.getReportnameTop());
			panelStandardDruckerTop.getWcoVariante().setMap(m);

			Map m2 = DelegateFactory.getInstance().getDruckerDelegate()
					.holeAlleVarianten(jpaPanelReportIf.getReportnameBottom());

			panelStandardDruckerBottom.getWcoVariante().setMap(m2);
			panelStandardDruckerTop
					.getWcoVariante()
					.addActionListener(
							new PanelReportKriterienZweiDrucker_wcoVariante_actionAdapter(
									this));

			panelStandardDruckerBottom
					.getWcoVariante()
					.addActionListener(
							new PanelReportKriterienZweiDrucker_wcoVariante_actionAdapter(
									this));
			bVariantenInitialisiert = true;
			
		}

		// Drucker vorselektieren
		if (standarddruckerTopDto != null) {
			panelStandardDruckerTop.setSelectedPrinter(standarddruckerTopDto
					.getCDrucker());

			if (standarddruckerTopDto.getReportvarianteIId() != null) {
				panelStandardDruckerTop.getWcoVariante().setKeyOfSelectedItem(
						standarddruckerTopDto.getReportvarianteIId());
			}

		}
		// Drucker vorselektieren
		if (standarddruckerBottomDto != null) {
			panelStandardDruckerBottom
					.setSelectedPrinter(standarddruckerBottomDto.getCDrucker());

			if (standarddruckerBottomDto.getReportvarianteIId() != null) {
				panelStandardDruckerBottom
						.getWcoVariante()
						.setKeyOfSelectedItem(
								standarddruckerBottomDto.getReportvarianteIId());
			}
		}

		panelStandardDruckerTop.getWbuSpeichern().addActionListener(this);
		panelStandardDruckerTop.getWbuSpeichern().setActionCommand(
				ACTION_SPECIAL_DRUCKER_TOP_SPEICHERN);
		panelStandardDruckerTop.getWbuLoeschen().addActionListener(this);
		panelStandardDruckerTop.getWbuLoeschen().setActionCommand(
				ACTION_SPECIAL_DRUCKER_TOP_LOESCHEN);

		panelStandardDruckerBottom.getWbuSpeichern().addActionListener(this);
		panelStandardDruckerBottom.getWbuSpeichern().setActionCommand(
				ACTION_SPECIAL_DRUCKER_BOTTOM_SPEICHERN);
		panelStandardDruckerBottom.getWbuLoeschen().addActionListener(this);
		panelStandardDruckerBottom.getWbuLoeschen().setActionCommand(
				ACTION_SPECIAL_DRUCKER_BOTTOM_LOESCHEN);

	}

	private void saveStandarddruckertop() throws Throwable {
		try {
			if (standarddruckerTopDto == null) {
				standarddruckerTopDto = new StandarddruckerDto();
			}
			if (Helper.getPCName() != null
					&& jpaPanelReportIf.getModul() != null
					&& jpaPanelReportIf.getReportnameTop() != null) {
				// nur, wenn auch ein Drucker ausgewaehlt ist
				if (panelStandardDruckerTop.getSelectedPrinter() != null) {
					standarddruckerTopDto.setCPc(Helper.getPCName());
					if (jpaPanelReportIf instanceof ReportBeleg) {
						Integer kstIId = ((ReportBeleg) jpaPanelReportIf)
								.getKostenstelleIId();
						standarddruckerTopDto
								.setKostenstelleIId_notInDB(kstIId);
					}
					standarddruckerTopDto.setLocale_notInDB(LPMain
							.getTheClient().getLocUi());
					standarddruckerTopDto.setMandantCNr(LPMain.getTheClient()
							.getMandant());
					standarddruckerTopDto.setSFilename_notInDB(jpaPanelReportIf
							.getReportnameTop());
					standarddruckerTopDto.setSModul_notInDB(jpaPanelReportIf
							.getModul());
					standarddruckerTopDto.setCDrucker(panelStandardDruckerTop
							.getSelectedPrinter());

					standarddruckerTopDto.setBStandard(Helper
							.boolean2Short(true));
					standarddruckerTopDto
					.setReportvarianteIId(panelStandardDruckerTop
							.getVariante());
					if (jpaPanelReportIf instanceof ReportBeleg) {
						standarddruckerTopDto
								.setKostenstelleIId_notInDB(((ReportBeleg) jpaPanelReportIf)
										.getKostenstelleIId());
					}
					Integer iId = DelegateFactory.getInstance()
							.getDruckerDelegate().updateStandarddrucker(
									standarddruckerTopDto);
					standarddruckerTopDto.setIId(iId);
				}
			}
		} catch (ExceptionLP ex) {
			if (ex.getICode() == EJBExceptionLP.FEHLER_BEIM_ANLEGEN) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain
										.getTextRespectUISPr("lp.drucken.druckereinstellungkonntenichtgespeichertwerden"));
			} else {
				throw ex;
			}
		}
	}

	private void saveStandarddruckerbottom() throws Throwable {
		try {
			if (standarddruckerBottomDto == null) {
				standarddruckerBottomDto = new StandarddruckerDto();
			}
			if (Helper.getPCName() != null
					&& jpaPanelReportIf.getModul() != null
					&& jpaPanelReportIf.getReportnameTop() != null) {
				if (panelStandardDruckerBottom.getSelectedPrinter() != null) {
					standarddruckerBottomDto.setCPc(Helper.getPCName());
					if (jpaPanelReportIf instanceof ReportBeleg) {
						Integer kstIId = ((ReportBeleg) jpaPanelReportIf)
								.getKostenstelleIId();
						standarddruckerBottomDto
								.setKostenstelleIId_notInDB(kstIId);
					}
					standarddruckerBottomDto.setLocale_notInDB(LPMain
							.getTheClient().getLocUi());
					standarddruckerBottomDto.setMandantCNr(LPMain
							.getTheClient().getMandant());
					standarddruckerBottomDto
							.setSFilename_notInDB(jpaPanelReportIf
									.getReportnameBottom());
					standarddruckerBottomDto.setSModul_notInDB(jpaPanelReportIf
							.getModul());
					standarddruckerBottomDto
							.setCDrucker(panelStandardDruckerBottom
									.getSelectedPrinter());
					standarddruckerBottomDto.setBStandard(Helper
							.boolean2Short(true));
					standarddruckerBottomDto
					.setReportvarianteIId(panelStandardDruckerBottom.getVariante());
					if (jpaPanelReportIf instanceof ReportBeleg) {
						standarddruckerBottomDto
								.setKostenstelleIId_notInDB(((ReportBeleg) jpaPanelReportIf)
										.getKostenstelleIId());
					}
					Integer iId = DelegateFactory.getInstance()
							.getDruckerDelegate().updateStandarddrucker(
									standarddruckerBottomDto);
					standarddruckerBottomDto.setIId(iId);
				}
			}
		} catch (ExceptionLP ex) {
			if (ex.getICode() == EJBExceptionLP.FEHLER_BEIM_ANLEGEN) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain
										.getTextRespectUISPr("lp.drucken.druckereinstellungkonntenichtgespeichertwerden"));
			} else {
				throw ex;
			}
		}
	}

	public void wcoVariante_actionPerformed(ActionEvent e) {
		try {
			initStandarddrucker();
		} catch (Throwable ex) {
			ex.printStackTrace();

		}
	}

	private void removeStandarddruckertop() throws Throwable {
		if (standarddruckerTopDto != null) {
			DelegateFactory.getInstance().getDruckerDelegate()
					.removeStandarddrucker(standarddruckerTopDto);
			standarddruckerTopDto = null;
		}
	}

	private void removeStandarddruckerbottom() throws Throwable {
		if (standarddruckerBottomDto != null) {
			DelegateFactory.getInstance().getDruckerDelegate()
					.removeStandarddrucker(standarddruckerBottomDto);
			standarddruckerBottomDto = null;
		}
	}
}

class PanelReportKriterienZweiDrucker_wcoVariante_actionAdapter implements
		ActionListener {
	private PanelReportKriterienZweiDrucker adaptee;

	PanelReportKriterienZweiDrucker_wcoVariante_actionAdapter(
			PanelReportKriterienZweiDrucker adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoVariante_actionPerformed(e);
	}
}
