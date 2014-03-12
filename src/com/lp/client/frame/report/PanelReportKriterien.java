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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterName;
import javax.swing.AbstractButton;
import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.save.JRPdfSaveContributor;
import net.sf.jasperreports.view.save.JRPrintSaveContributor;

import com.lp.client.bestellung.ReportWepEtikett;
import com.lp.client.eingangsrechnung.ReportEingangsrechnung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.IControl;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.service.DokumentnichtarchiviertDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.DruckerFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ReportkonfDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.StandarddruckerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

public class PanelReportKriterien extends PanelDialog implements
		VetoableChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelStandardDrucker panelStandardDrucker = null;
	private PanelVersandEmail panelVersandEmail = null;
	private PanelVersandFax panelVersandFax = null;
	private JPanel jpaTop = null;
	private PanelReport panelReport = null;
	private PanelReportIfJRDS jpaPanelReportIf = null;
	private String LINE_BREAK = "\n";
	private String CELL_BREAK = "\t";
	private List<ReportkonfDto> reportkonfCache;
	private Timestamp berechnungsZeitpunkt = null;

	private boolean bZoomfaktorGesetzt = false;
	private Object letzterZoomfaktor = null;

	public PanelStandardDrucker getPanelStandardDrucker() {
		return panelStandardDrucker;
	}

	private JSplitPane jSplitPane1 = new JSplitPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayoutTop = new GridBagLayout();
	private WrapperButton wbuErweitert = null;
	// private String sBelegnummer = null;

	private ReportkonfDto[] konfDtos = null;
	boolean bVariantenInitialisiert = false;

	private boolean bMitEmailFax = false;
	private boolean bNurVorschau = false;
	private PartnerDto partnerDtoEmpfaenger = null;
	private Integer ansprechpartnerIId = null;
	private boolean bDirekt = false;
	private StandarddruckerDto standarddruckerDto = null;
	protected int iKopien = 0;

	private static final String ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_preview";
	private static final String ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_fullscreen";
	private static final String ACTION_SPECIAL_DRUCKEN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_drucken";
	private static final String ACTION_SPECIAL_EMAIL = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_email";
	private static final String ACTION_SPECIAL_FAX = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_fax";
	private static final String ACTION_SPECIAL_CSV = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_csv";
	private static final String ACTION_SPECIAL_SAVE = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_save";
	private static final String ACTION_SPECIAL_ERWEITERT = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_erweitert";
	private static final String ACTION_SPECIAL_DRUCKER_SPEICHERN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_drucker_speichern";
	private static final String ACTION_SPECIAL_DRUCKER_LOESCHEN = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_drucker_loeschen";

	private static final String ACTION_COPY_TO_CLIPBOARD = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_copy_to_clipboard";

	public PanelReportKriterien(InternalFrame internalFrame,
			PanelReportIfJRDS panelReportIf, String addTitleI,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId,
			boolean bDirekt, boolean bMitEmailFax, boolean bNurVorschau)
			throws Throwable {
		super(internalFrame, addTitleI);
		this.jpaPanelReportIf = panelReportIf;
		this.partnerDtoEmpfaenger = partnerDtoEmpfaenger;
		this.ansprechpartnerIId = ansprechpartnerIId;
		this.bDirekt = bDirekt;
		this.bMitEmailFax = bMitEmailFax;
		this.bNurVorschau = bNurVorschau;
		jbInitPanel();
		initComponents();
		LockStateValue lockstateValue = new LockStateValue(null, null,
				LOCK_NO_LOCKING);
		this.updateButtons(lockstateValue);
		// Alle Controls aktivieren
		if (panelReportIf instanceof PanelBasis) {
			enableAllComponents(this, true);
		}

		initStandarddrucker();
		// Wenn der Report sofort erstellt und angezeigt werden soll, dann mach
		// das halt
		if (panelReportIf.getBErstelleReportSofort()) {
			preview();
		}

		// Erweiterte Druckereinstellungen: Dieses Feature ist zzt nicht
		// sichtbar
		wbuErweitert.setVisible(false);
		// eventYouAreSelected(false) ;
		updateLpTitle();
	}

	private void jbInitPanel() throws Throwable {
		// Button Refresh
		createAndSaveButton("/com/lp/client/res/refresh.png",
				LPMain.getTextRespectUISPr("lp.drucken.reportaktualisieren"),
				ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW,
				KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), null);
		if (!bNurVorschau) {
			// Button Druckvorschau
			createAndSaveButton("/com/lp/client/res/printer_view.png",
					LPMain.getTextRespectUISPr("lp.drucken.vorschau"),
					ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN,
					KeyStroke.getKeyStroke('R',
							java.awt.event.InputEvent.CTRL_MASK), null);
			// Button Drucken
			createAndSaveButton("/com/lp/client/res/printer.png",
					LPMain.getTextRespectUISPr("lp.drucken.drucken"),
					ACTION_SPECIAL_DRUCKEN, KeyStroke.getKeyStroke('P',
							java.awt.event.InputEvent.CTRL_MASK), null);
			// Button CSV-Export
			createAndSaveButton("/com/lp/client/res/document_out.png",
					LPMain.getTextRespectUISPr("lp.report.csvexport"),
					ACTION_SPECIAL_CSV, null, null);

			// Button Zwischenablage
			createAndSaveButton("/com/lp/client/res/copy.png",
					LPMain.getTextRespectUISPr("lp.inzwischenablagekopieren"),
					ACTION_COPY_TO_CLIPBOARD, null, null);

			// Buttons fuer Email und Fax, falls erwuenscht
			if (bMitEmailFax) {

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_EMAILVERSAND)) {
					createAndSaveButton(
							"/com/lp/client/res/mail.png",
							LPMain.getTextRespectUISPr("lp.drucken.alsemailversenden"),
							ACTION_SPECIAL_EMAIL, KeyStroke.getKeyStroke('E',
									java.awt.event.InputEvent.CTRL_MASK), null);
				}
				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_FAXVERSAND)) {
					createAndSaveButton(
							"/com/lp/client/res/fax.png",
							LPMain.getTextRespectUISPr("lp.drucken.alsfaxversenden"),
							ACTION_SPECIAL_FAX, KeyStroke.getKeyStroke('F',
									java.awt.event.InputEvent.CTRL_MASK), null);
				}
			}
		}
		// Button Speichern
		createAndSaveButton("/com/lp/client/res/disk_blue.png",
				LPMain.getTextRespectUISPr("lp.drucken.speichern"),
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
						ACTION_SPECIAL_DRUCKEN, ACTION_SPECIAL_EMAIL,
						ACTION_SPECIAL_FAX, ACTION_SPECIAL_SAVE,
						ACTION_SPECIAL_CSV, ACTION_COPY_TO_CLIPBOARD };
			} else {
				aWhichButtonIUse = new String[] {
						ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW,
						ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN,
						ACTION_SPECIAL_DRUCKEN, ACTION_SPECIAL_SAVE,
						ACTION_SPECIAL_CSV, ACTION_COPY_TO_CLIPBOARD };
			}

		}

		enableToolsPanelButtons(aWhichButtonIUse);

		// Splitpane ausrichten
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);

		jSplitPane1.setAutoscrolls(true);

		jpaWorkingOn.setLayout(gridBagLayout1);

		// Das Druckerpanel
		panelStandardDrucker = new PanelStandardDrucker();
		wbuErweitert = new WrapperButton();
		wbuErweitert.setHeavyOperation(true);

		wbuErweitert.setActionCommand(ACTION_SPECIAL_ERWEITERT);

		wbuErweitert.addActionListener(this);

		wbuErweitert.setText(LPMain.getTextRespectUISPr("button.erweitert"));

		wbuErweitert.setToolTipText(LPMain
				.getTextRespectUISPr("button.erweitert.tooltip"));

		wbuErweitert.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));

		wbuErweitert.setPreferredSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(jSplitPane1, new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		if (jpaPanelReportIf instanceof JPanel) {
			jpaTop = new JPanel(gridBagLayoutTop);
			jpaTop.add(panelStandardDrucker, new GridBagConstraints(0, iZeile,
					1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jpaTop.add(wbuErweitert, new GridBagConstraints(1, iZeile, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaTop.add((JPanel) jpaPanelReportIf, new GridBagConstraints(0,
					iZeile, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			iZeile++;
			jSplitPane1.add(jpaTop, JSplitPane.TOP);
			jSplitPane1.add(new JPanel(), JSplitPane.BOTTOM);
			ActionListener buttonListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					updateRefreshButton();
				}
			};

			for (AbstractButton btn : getAllButtons((JPanel) jpaPanelReportIf)) {
				btn.addActionListener(buttonListener);
			}
		} else {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		FocusManager.getCurrentManager().addVetoableChangeListener(this);
	}

	private List<AbstractButton> getAllButtons(JPanel panel) {
		ArrayList<AbstractButton> btns = new ArrayList<AbstractButton>();
		for (Component c : panel.getComponents()) {
			if (c instanceof JPanel) {
				btns.addAll(getAllButtons((JPanel) c));
			} else if (c instanceof AbstractButton) {
				btns.add((AbstractButton) c);
			}
		}
		return btns;
	}

	protected final void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(PanelBasis.ESC)
				|| e.getActionCommand()
						.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			FocusManager.getCurrentManager().removeVetoableChangeListener(this);
		}
		super.eventActionSpecial(e);
		// Refresh ... erzeugt den Report neu und zeigt ihn an
		if (e.getActionCommand().equals(ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW)) {
			preview();
			// initStandarddrucker();
		}
		// grosser Vorschaudialog
		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN)) {
			// print erzeugen, falls das noch nicht passiert ist.
			if (panelReport == null || panelReport.getPrint() == null) {
				preview();
			}
			// der im unteren panel angezeigte Print wird in die Vorschau
			// uebernommen
			if (panelReport != null && panelReport.getPrint() != null
					&& panelReport.getPrint().getPrint().getPages().size() > 0) {
				aktiviereBeleg();
				// den Timer im Hintergrund abdrehen
				getInternalFrame().getFrameProgress().pause();
				// und jetzt den Dialog zeigen
				JDialog dialog = new JDialogDruckVorschau(LPMain.getInstance()
						.getDesktop(),
						LPMain.getTextRespectUISPr("lp.drucken.vorschau")
								+ ": " + this.getAdd2Title(), panelReport
								.getPrint().getPrint());
				dialog.setModal(true);
				archiviereBeleg();
				// TODO: soll hier nicht besser Drucktyp print verwendet werden
				setzeVersandstatus(JasperPrintLP.DRUCKTYP_PREVIEW);
			}
		}
		// Email
		else if (e.getActionCommand().equals(ACTION_SPECIAL_EMAIL)) {
			// Email - Betreff setzen
			if (jpaPanelReportIf.getMailtextDto().getMailBetreff() != null) {
				getPanelVersandEmail(true).setBetreff(
						jpaPanelReportIf.getMailtextDto().getMailBetreff());

			}
			// Email-Panel anzeigen
			if (!getPanelVersandEmail(true).isVisible()) {
				getPanelVersandEmail(true).setVisible(true);
			}
			// Wenn es das Fax-Panel schon gibt, muss ich das wegschalten
			if (getPanelVersandFax(false) != null
					&& getPanelVersandFax(true).isVisible()) {
				getPanelVersandFax(true).setVisible(false);
			}
			// SplitPane anpassen (moeglichst platzsparend)
			jSplitPane1.setDividerLocation(jpaTop.getMinimumSize().height);
		}

		// Fax
		else if (e.getActionCommand().equals(ACTION_SPECIAL_FAX)) {
			// Fax-Panel anzeigen
			if (!getPanelVersandFax(true).isVisible()) {
				getPanelVersandFax(true).setVisible(true);
			}
			// Wenn es das Email-Panel schon gibt, muss ich das wegschalten
			if (getPanelVersandEmail(false) != null
					&& getPanelVersandEmail(true).isVisible()) {
				getPanelVersandEmail(true).setVisible(false);
			}
			// SplitPane anpassen (moeglichst platzsparend)
			jSplitPane1.setDividerLocation(jpaTop.getMinimumSize().height);
		}
		// Versandauftrag senden
		else if (e.getActionCommand()
				.equals(PanelVersand.ACTION_SPECIAL_SENDEN)) {
			aktiviereBeleg();
			String sDrucktyp = null;
			// Fax ...
			if (getPanelVersandFax(true).isVisible()) {
				createVersandauftrag(getPanelVersandFax(true)
						.getVersandauftragDto(), JasperPrintLP.DRUCKTYP_FAX);
				sDrucktyp = JasperPrintLP.DRUCKTYP_FAX;
			}
			// ... oder Email
			else if (getPanelVersandEmail(true).isVisible()) {
				createVersandauftrag(getPanelVersandEmail(true)
						.getVersandauftragDto(), JasperPrintLP.DRUCKTYP_MAIL);
				sDrucktyp = JasperPrintLP.DRUCKTYP_MAIL;
			}
			archiviereBeleg();
			setzeVersandstatus(sDrucktyp);
		}
		// Hinzufugen von Anhaengen an eine Mail.
		else if (e.getActionCommand().equals(
				PanelVersandEmail.ACTION_SPECIAL_ATTACHMENT)) {
			String sAttachments = panelVersandEmail.getjtfAnhaengeText();
			File[] files = HelperClient.chooseFile(this, "*.*", true);
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					sAttachments += files[i].getAbsolutePath() + ";";
				}
				panelVersandEmail.setjtfAnhaengeText(sAttachments);
			}
		}

		// Report abspeichern oder CSV Export
		else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)
				|| e.getActionCommand().equals(ACTION_SPECIAL_CSV)
				|| e.getActionCommand().equals(ACTION_COPY_TO_CLIPBOARD)) {
			// Den Report erstellen, falls dies noch nicht geschehen ist.
			if (panelReport == null) {
				preview();
			}

			if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {

				// Report speichern
				if (panelReport != null) {
					JasperPrintLP print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(
							true, JasperPrintLP.DRUCKTYP_SAVE);
					aktiviereBeleg();

					List saveContributors = new ArrayList();
					File lastFolder;
					JRSaveContributor lastSaveContributor = null;
					final String[] DEFAULT_CONTRIBUTORS = {
							"net.sf.jasperreports.view.save.JRPrintSaveContributor",
							"net.sf.jasperreports.view.save.JRPdfSaveContributor",
							"net.sf.jasperreports.view.save.JRRtfSaveContributor",
							"net.sf.jasperreports.view.save.JROdtSaveContributor",
							"net.sf.jasperreports.view.save.JRDocxSaveContributor",
							"net.sf.jasperreports.view.save.JRHtmlSaveContributor",
							"net.sf.jasperreports.view.save.JRSingleSheetXlsSaveContributor",
							"net.sf.jasperreports.view.save.JRMultipleSheetsXlsSaveContributor",
							"net.sf.jasperreports.view.save.JRCsvSaveContributor",
							"net.sf.jasperreports.view.save.JRXmlSaveContributor",
							"net.sf.jasperreports.view.save.JREmbeddedImagesXmlSaveContributor" };

					for (int i = 0; i < DEFAULT_CONTRIBUTORS.length; i++) {
						try {
							Class saveContribClass = JRClassLoader
									.loadClassForName(DEFAULT_CONTRIBUTORS[i]);
							Constructor constructor = saveContribClass
									.getConstructor(new Class[] { Locale.class,
											ResourceBundle.class });
							JRSaveContributor saveContrib = (JRSaveContributor) constructor
									.newInstance(new Object[] { getLocale(),
											null });
							saveContributors.add(saveContrib);
						} catch (Exception et) {
						}
					}

					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setLocale(this.getLocale());
					fileChooser.updateUI();
					for (int i = 0; i < saveContributors.size(); i++) {
						fileChooser
								.addChoosableFileFilter((JRSaveContributor) saveContributors
										.get(i));
					}

					if (saveContributors.contains(lastSaveContributor)) {
						fileChooser.setFileFilter(lastSaveContributor);
					} else if (saveContributors.size() > 0) {
						fileChooser
								.setFileFilter((JRSaveContributor) saveContributors
										.get(0));
					}

					int retValue = fileChooser.showSaveDialog(this);
					if (retValue == JFileChooser.APPROVE_OPTION) {
						FileFilter fileFilter = fileChooser.getFileFilter();
						File file = fileChooser.getSelectedFile();

						lastFolder = file.getParentFile();

						JRSaveContributor contributor = null;

						if (fileFilter instanceof JRSaveContributor) {
							contributor = (JRSaveContributor) fileFilter;
						} else {
							int i = 0;
							while (contributor == null
									&& i < saveContributors.size()) {
								contributor = (JRSaveContributor) saveContributors
										.get(i++);
								if (!contributor.accept(file)) {
									contributor = null;
								}
							}

							if (contributor == null) {
								contributor = new JRPrintSaveContributor(
										getLocale(), null);
							}
						}

						lastSaveContributor = contributor;

						try {

							if (contributor instanceof JRPdfSaveContributor) {
								if (!file.getName().toLowerCase()
										.endsWith(".pdf")) {
									file = new File(file.getAbsolutePath()
											+ ".pdf");
								}

								if (!file.exists()
										|| JOptionPane.OK_OPTION == JOptionPane
												.showConfirmDialog(
														null,
														MessageFormat
																.format("Die Datei existiert bereits. \u00DCberschreiben?",
																		new Object[] { file
																				.getName() }),
														"Speichern",
														JOptionPane.OK_CANCEL_OPTION)) {
									JRPdfExporter exporter = new JRPdfExporter();
									exporter.setParameter(
											JRExporterParameter.JASPER_PRINT,
											print.getPrint());
									exporter.setParameter(
											JRExporterParameter.OUTPUT_FILE,
											file);

									// PJ15464

									Map<net.sf.jasperreports.engine.export.FontKey, PdfFont> fontMap = getPDFFontMap();

									exporter.setParameter(
											JRExporterParameter.FONT_MAP,
											fontMap);

									exporter.exportReport();
								}
							} else {
								contributor.save(print.getPrint(), file);
							}

						} catch (JRException ex) {

							JOptionPane.showMessageDialog(this,
									"Fehler beim Speichern");
						}
					}

					if (jpaPanelReportIf instanceof IPanelReportAction) {
						((IPanelReportAction) jpaPanelReportIf)
								.eventActionSave(e);
					}
					archiviereBeleg();
					setzeVersandstatus(JasperPrintLP.DRUCKTYP_SAVE);
				}
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_CSV)) {

				JasperPrintLP print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(
						true, JasperPrintLP.DRUCKTYP_CSV);
				java.util.List list = print.getPrint().getPages();

				Object[][] csvDaten = print.getDatenMitUeberschrift();

				// Derzeit einfach auf String umwandeln

				if (csvDaten != null && csvDaten.length > 1) {

					int iZeile = 0;
					String[][] csv = new String[csvDaten.length][csvDaten[0].length];

					for (int i = 0; i < csvDaten.length; i++) {
						for (int z = 0; z < csvDaten[0].length; z++) {
							try {
								if (csvDaten[i][z] != null) {

									if (csvDaten[i][z] instanceof Number) {
										csv[i][z] = Helper.formatZahl(
												(Number) csvDaten[i][z], LPMain
														.getTheClient()
														.getLocUi());
									} else if (csvDaten[i][z] instanceof LPDatenSubreport) {
										csv[i][z] = "SUBREPORT";
									} else {
										csv[i][z] = csvDaten[i][z].toString();
									}

								}
							} catch (Exception e1) {
								int u = 0;
							}
						}
						iZeile++;
					}

					DialogFactory.showCSVExportDialog(
							new String("\t").charAt(0), csv);

				}

			} else if (e.getActionCommand().equals(ACTION_COPY_TO_CLIPBOARD)) {

				JasperPrintLP print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(
						true, JasperPrintLP.DRUCKTYP_CSV);

				Object[][] csvDaten = print.getDatenMitUeberschrift();
				StringBuffer excelStr = new StringBuffer();

				if (csvDaten != null && csvDaten.length > 1) {

					// Daten
					for (int i = 0; i < csvDaten.length; i++) {
						Object[] zeile = csvDaten[i];

						if (zeile != null) {
							for (int j = 0; j < zeile.length; j++) {

								if (zeile[j] instanceof Number) {
									try {
										excelStr.append(Helper.formatZahl(
												(Number) zeile[j], LPMain
														.getTheClient()
														.getLocUi()));
									} catch (Throwable e1) {
										//
									}
								} else if (zeile[j] instanceof String) {
									String s = (String) zeile[j];
									excelStr.append("\""
											+ escape(s.replaceAll("\"", "\"\""))
											+ "\"");
								} else {
									excelStr.append(escape(zeile[j]));
								}

								if (j < zeile.length - 1) {
									excelStr.append(CELL_BREAK);
								}
							}
						}
						excelStr.append(LINE_BREAK);
					}
				}

				Toolkit.getDefaultToolkit()
						.getSystemClipboard()
						.setContents(new StringSelection(excelStr.toString()),
								null);

			}

		}
		// Ausdrucken
		else if (e.getActionCommand().equals(ACTION_SPECIAL_DRUCKEN)) {
			aktiviereBeleg();
			print();
			// if (jpaPanelReportIf instanceof com.lp.client.finanz.ReportUva)
			// ((PanelBasis)jpaPanelReportIf).eventActionSave(e, true);
			if (jpaPanelReportIf instanceof IPanelReportAction) {
				((IPanelReportAction) jpaPanelReportIf).eventActionPrint(e);
			}
			archiviereBeleg();
			setzeVersandstatus(JasperPrintLP.DRUCKTYP_DRUCKER);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ERWEITERT)) {
			if (panelReport == null) {
				preview();
			}
			if (panelReport != null) {
				panelReport.doClickOnPrint();
			}
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_DRUCKER_SPEICHERN)) {
			// Druckereinstellung speichern
			saveStandarddrucker();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_DRUCKER_LOESCHEN)) {
			// Druckereinstellung speichern
			removeStandarddrucker();
		}
	}

	private void aktiviereBeleg() throws Throwable {
		if(jpaPanelReportIf instanceof IAktiviereBelegReport) {
			IAktiviereBelegReport rep = (IAktiviereBelegReport)jpaPanelReportIf;
			rep.aktiviereBeleg(berechnungsZeitpunkt);
			rep.refreshPanelInBackground();
		}
	}
	
	private void berecheneBeleg() throws Throwable {
		if(jpaPanelReportIf instanceof IAktiviereBelegReport)
			berechnungsZeitpunkt = ((IAktiviereBelegReport)jpaPanelReportIf).berechneBeleg();
	}

	public void druckeArchiviereUndSetzeVersandstatusEinesBelegs()
			throws Throwable {
		if(berechnungsZeitpunkt == null)
			berecheneBeleg();
		aktiviereBeleg();
		print();
		archiviereBeleg();
		setzeVersandstatus(JasperPrintLP.DRUCKTYP_DRUCKER);
	}

	private String escape(Object cell) {
		if (cell == null) {
			return "";
		} else {
			return cell.toString().replace(LINE_BREAK, " ")
					.replace(CELL_BREAK, " ");
		}

	}

	/**
	 * createVersandauftrag
	 * 
	 * @param versandauftragDto
	 *            VersandauftragDto
	 * @throws Throwable
	 */
	private void createVersandauftrag(VersandauftragDto versandauftragDto,
			String sDruckType) throws Throwable {

		int iKopienAktuell = 0;
		if (jpaPanelReportIf instanceof ReportBeleg) {

			if (((ReportBeleg) jpaPanelReportIf).wnfKopien.getInteger() == null) {
				iKopien = 0;
			} else {

				iKopien = ((ReportBeleg) jpaPanelReportIf).wnfKopien
						.getInteger();
			}
			versandauftragDto.setBelegartCNr(((ReportBeleg) jpaPanelReportIf)
					.getBelegartCNr());
			versandauftragDto.setIIdBeleg(((ReportBeleg) jpaPanelReportIf)
					.getIIdBeleg());
			String sBelegnummer = DelegateFactory
					.getInstance()
					.getVersandDelegate()
					.getDefaultDateinameForBelegEmail(
							getPanelVersandEmail(true).getbelegartCNr(),
							getPanelVersandEmail(true).getbelegIId());
			versandauftragDto.setCDateiname(sBelegnummer.replace(" ", "")
					.replace("/", "_"));
		}
		if (versandauftragDto.getCEmpfaenger() == null
				|| (getPanelVersandEmail(true).isVisible() && versandauftragDto
						.getCAbsenderadresse() == null)) {
			showDialogPflichtfelderAusfuellen();
		} else {
			boolean sizeIsOk = true;
			JasperPrint print = null;
			if (iKopien > 0) {
				// Merken wie viele Kopien eingestellt waren
				iKopienAktuell = iKopien;
				((ReportBeleg) jpaPanelReportIf).wnfKopien.setInteger(0);
				iKopien = 0;
				((ReportBeleg) jpaPanelReportIf).setBPrintLogo(true);
				print = createPrint(sDruckType).getPrint();
			} else {
				print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(
						true, sDruckType).getPrint();
			}
			// Kopieanzahl zuruecksetzen
			iKopien = iKopienAktuell;
			if (print.getPages().size() > 0) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

				exporter.setParameter(JRExporterParameter.FONT_MAP, getPDFFontMap());

				exporter.exportReport();

				versandauftragDto.setOInhalt(baos.toByteArray());

				boolean bDokumenteAnhaengen = false;

				if (getPanelVersandEmail(true).isVisible()) {

					if (getPanelVersandEmail(true).getWcbDokumenteAnhaengen()
							.isSelected()) {
						bDokumenteAnhaengen = true;
					}
				}

				versandauftragDto = DelegateFactory
						.getInstance()
						.getVersandDelegate()
						.updateVersandauftrag(versandauftragDto,
								bDokumenteAnhaengen);
				if (getPanelVersandEmail(true).isVisible()) {
					// Anhaenge verarbeiten
					String[] sAttachments = null;
					if (!panelVersandEmail.getjtfAnhaengeText().equals("")) {
						sAttachments = panelVersandEmail.getjtfAnhaengeText()
								.split(";");
					}
					if (sAttachments != null) {
						// check wie grosz die anhaenge sind:
						int size = 0;
						for (int y = 0; y < sAttachments.length; y++) {
							File file = new File(sAttachments[y]);
							size += file.length() / 1024; // umrechnung in KB
						}
						// holen der maximalgroesze
						int maxsize = Integer
								.parseInt(DelegateFactory
										.getInstance()
										.getParameterDelegate()
										.getMandantparameter(
												LPMain.getTheClient()
														.getMandant(),
												"ALLGEMEIN",
												ParameterFac.PARAMETER_SIZE_E_MAILANHANG)
										.getCWert());

						if (size > maxsize) {
							sizeIsOk = false;
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("lp.drucken.anhangzugros")
													+ " (" + maxsize + "KB)");
							panelVersandEmail.setjtfAnhaengeText("");
						}
						if (sizeIsOk) {
							VersandanhangDto versandanhangDto = new VersandanhangDto();
							for (int i = 0; i < sAttachments.length; i++) {
								File f = new File(sAttachments[i]);
								versandanhangDto
										.setVersandauftragIId(versandauftragDto
												.getIId());
								versandanhangDto.setCDateiname(f.getName());
								FileInputStream fiStream = new FileInputStream(
										f);
								byte[] fileData = new byte[(int) f.length()];
								fiStream.read(fileData);
								fiStream.close();
								versandanhangDto.setOInhalt(fileData);
								DelegateFactory.getInstance()
										.getVersandDelegate()
										.createVersandanhang(versandanhangDto);
							}
						}
					}

				}
				if (sizeIsOk) {
					getInternalFrame().closePanelDialog();
				}
			} else {
				showDialogKeineDatenZuDrucken();
			}
		}
	}

	private Map<net.sf.jasperreports.engine.export.FontKey, PdfFont> getPDFFontMap() {
		Map<net.sf.jasperreports.engine.export.FontKey, PdfFont> fontMap = new HashMap<net.sf.jasperreports.engine.export.FontKey, PdfFont>();
		// Arial is Helvetica
		fontMap.put(new FontKey("Arial", true, false), new PdfFont(
				"Helvetica-Bold", "Cp1252", false));
		fontMap.put(new FontKey("Arial", false, true), new PdfFont(
				"Helvetica-Oblique", "Cp1252", false));
		fontMap.put(new FontKey("Arial", true, true), new PdfFont(
				"Helvetica-BoldOblique", "Cp1252", false));
		// Courier New wird Courier

		fontMap.put(new FontKey("Courier New", false, false),
				new PdfFont("Courier", "Cp1252", false));
		fontMap.put(new FontKey("Courier New", true, false),
				new PdfFont("Courier-Bold", "Cp1252", false));
		fontMap.put(new FontKey("Courier New", false, true),
				new PdfFont("Courier-Oblique", "Cp1252", false));
		fontMap.put(new FontKey("Courier New", true, true),
				new PdfFont("Courier-BoldOblique", "Cp1252", false));
		// Times bleibt Times
		fontMap.put(new FontKey("Times New Roman", true, false),
				new PdfFont("Times-Bold", "Cp1252", false));
		fontMap.put(new FontKey("Times New Roman", false, true),
				new PdfFont("Times-Italic", "Cp1252", false));
		fontMap.put(new FontKey("Times New Roman", true, true),
				new PdfFont("Times-BoldItalic", "Cp1252", false));
		return fontMap;
	}

	private void preview() throws Throwable {
		myLogger.debug("Erzeuge Report: " + jpaPanelReportIf.getModul() + "|"
				+ jpaPanelReportIf.getReportname());
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
			berecheneBeleg();
			JasperPrintLP print = null;
			try {
				print = createPrint(JasperPrintLP.DRUCKTYP_PREVIEW);
				if (print == null) {
					// nicht jede Option gibt auch wirklich einen print retour
					myLogger.debug("Report=null nach: "
							+ (System.currentTimeMillis() - lTime) + " ms.");
					return;
				}
				// Erstellungszeit loggen
				myLogger.debug("Report erzeugt nach: "
						+ (System.currentTimeMillis() - lTime) + " ms.");
				saveValuesToCache();
				setRefreshNecessary(false);
			} catch (ExceptionLP ex) {
				if (ex.getICode() == EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT) {
					if (ex.getCause() != null) {
						if (ex.getCause().getCause() != null) {
							if (ex.getCause().getCause() instanceof EJBExceptionLP) {
								EJBExceptionLP ejbex = (EJBExceptionLP) ex
										.getCause().getCause();
								if (ejbex.getCode() == EJBExceptionLP.FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT) {
									LPMain.getInstance();
									String sMsg = LPMain
											.getTextRespectUISPr("rech.warning.hvwertreportwert");
									ArrayList<?> al = ejbex
											.getAlInfoForTheClient();
									if (al != null && al.size() > 0) {
										sMsg += " (" + al.get(0) + ") ";
									}
									DialogFactory.showModalDialog(LPMain
											.getTextRespectUISPr("lp.warning"),
											sMsg);
									return;
								}
							} else {
								if (ex.getCause().getCause().getCause() != null) {
									if (ex.getCause().getCause().getCause() instanceof EJBExceptionLP) {
										EJBExceptionLP ejbex = (EJBExceptionLP) ex
												.getCause().getCause()
												.getCause();
										if (ejbex.getCode() == EJBExceptionLP.FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT) {
											LPMain.getInstance();
											String sMsg = LPMain
													.getTextRespectUISPr("rech.warning.hvwertreportwert");
											ArrayList<?> al = ejbex
													.getAlInfoForTheClient();
											if (al != null && al.size() > 0) {
												sMsg += " (" + al.get(0) + ") ";
											}
											DialogFactory
													.showModalDialog(
															LPMain.getTextRespectUISPr("lp.warning"),
															sMsg);
											return;
										}
									}
								}
							}
						}
					}

				}

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
					if (msg == null || ex.getICode()==EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT) {
						msg = "Fehlercode " + ex.getICode();
						msg = msg + "\n" + ex.getSMsg();
						new DialogError(LPMain.getInstance().getDesktop(), ex,
								DialogError.TYPE_ERROR);
					} else {
						new DialogError(LPMain.getInstance().getDesktop(), ex,
								DialogError.TYPE_INFORMATION);
					}

				}
				// Fehler loggen
				myLogger.info("getModul():                  "
						+ jpaPanelReportIf.getModul());
				myLogger.info("getReportname():             "
						+ jpaPanelReportIf.getReportname());
				myLogger.info("Report-Fehler " + ex.getICode() + " nach: "
						+ (System.currentTimeMillis() - lTime) + " ms.");
				return;
			}
			if (print.getPrint().getPages().size() == 0) {
				showDialogKeineDatenZuDrucken();
			} else {

				if (panelReport != null) {
					letzterZoomfaktor = panelReport.getJpaViewer().getCmbZoom()
							.getSelectedItem();
				}

				panelReport = new PanelReport(getInternalFrame(),
						this.getAdd2Title(), print, false);

				if (letzterZoomfaktor != null && bZoomfaktorGesetzt == true) {
					panelReport.getJpaViewer().getCmbZoom()
							.setSelectedItem(letzterZoomfaktor);
				}

				jSplitPane1.add(panelReport, JSplitPane.BOTTOM);

				if (bZoomfaktorGesetzt == false) {

					if (konfDtos != null) {
						for (int i = 0; i < konfDtos.length; i++) {
							ReportkonfDto dto = konfDtos[i];

							if (dto.getCKomponentenname().equals("ZOOMFAKTOR")) {
								panelReport.getJpaViewer().getCmbZoom()
										.setSelectedItem(dto.getCKey());
								bZoomfaktorGesetzt = true;
								letzterZoomfaktor = panelReport.getJpaViewer()
										.getCmbZoom().getSelectedItem();
							}

						}
					}
				}

			}
			// Titel setzen
			String sTitle = "(" + LPMain.getTextRespectUISPr("lp.report") + "="
					+ print.getSReportName() + ")";
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
		JLabel jlaNoPages = new JLabel(
				LPMain.getTextRespectUISPr("lp.hint.nopages"));
		jlaNoPages.setHorizontalAlignment(SwingConstants.CENTER);
		jpaPanel.add(jlaNoPages, BorderLayout.CENTER);
		jSplitPane1.add(jpaPanel, JSplitPane.BOTTOM);
	}

	private PanelVersandEmail getPanelVersandEmail(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelVersandEmail == null && bNeedInstantiationIfNull) {
			String belegartCNr = null;
			Integer belegIId = null;
			if (jpaPanelReportIf instanceof ReportBeleg) {
				belegartCNr = ((ReportBeleg) jpaPanelReportIf).getBelegartCNr();
				belegIId = ((ReportBeleg) jpaPanelReportIf).getIIdBeleg();
			}

			MailtextDto mtDto = jpaPanelReportIf.getMailtextDto();
			if (mtDto != null) {

				if (panelStandardDrucker.getVariante() != null) {
					ReportvarianteDto varDto = DelegateFactory
							.getInstance()
							.getDruckerDelegate()
							.reportvarianteFindByPrimaryKey(
									panelStandardDrucker.getVariante());
					mtDto.setParamXslFile(varDto.getCReportnamevariante());
				}

			}

			panelVersandEmail = new PanelVersandEmail(getInternalFrame(),
					mtDto, belegartCNr, belegIId, jpaPanelReportIf, this,
					partnerDtoEmpfaenger);
			panelVersandEmail.setDefaultAbsender(partnerDtoEmpfaenger,
					ansprechpartnerIId);
			// Direktversand
			if (bDirekt) {
				panelVersandEmail.setEditorFieldVisible(false);
			}
			panelVersandEmail.getWbuSenden().addActionListener(this);
			panelVersandEmail.getwbuAnhangWaehlen().addActionListener(this);

			jpaTop.add(panelVersandEmail, new GridBagConstraints(0, iZeile, 1,
					1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			panelVersandEmail.setVisible(false);
			iZeile++;
		}
		return panelVersandEmail;
	}

	private PanelVersandFax getPanelVersandFax(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelVersandFax == null && bNeedInstantiationIfNull) {
			String belegartCNr = null;
			Integer belegIId = null;
			if (jpaPanelReportIf instanceof ReportBeleg) {
				belegartCNr = ((ReportBeleg) jpaPanelReportIf).getBelegartCNr();
				belegIId = ((ReportBeleg) jpaPanelReportIf).getIIdBeleg();
			}
			panelVersandFax = new PanelVersandFax(getInternalFrame(),
					belegartCNr, belegIId, jpaPanelReportIf, this,
					partnerDtoEmpfaenger);
			panelVersandFax.setDefaultAbsender(partnerDtoEmpfaenger,
					ansprechpartnerIId);
			panelVersandFax.getWbuSenden().addActionListener(this);
			jpaTop.add(panelVersandFax, new GridBagConstraints(0, iZeile, 1, 1,
					1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			panelVersandFax.setVisible(false);
			iZeile++;
		}
		return panelVersandFax;
	}

	private JasperPrintLP createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(
			boolean bMitLogo, String sDrucktype) throws Throwable {
		int iKopienAktuell = 0;
		if (jpaPanelReportIf instanceof ReportBeleg) {
			Integer iiKopienAktuell = ((ReportBeleg) jpaPanelReportIf).wnfKopien
					.getInteger();
			if (iiKopienAktuell != null) {
				iKopienAktuell = iiKopienAktuell.intValue();
			}
		}

		// PJ 16106
		if (jpaPanelReportIf instanceof ReportEingangsrechnung) {
			((ReportEingangsrechnung) jpaPanelReportIf)
					.schecknummerZurueckschreiben();
		}

		JasperPrintLP print = null;
		// wenn er noch nicht erzeugt wurde, muss er jetzt auf jeden fall
		// erzeugt werden
		if (panelReport == null || panelReport.getPrint() == null) {
			print = createPrint(sDrucktype);
			panelReport = new PanelReport(getInternalFrame(),
					this.getAdd2Title(), print, false);
		} else {
			// er ist schon da
			print = panelReport.getPrint();
			// jetzt muss noch geprueft werden, ob das logo fuer Belege drauf
			// sein soll oder nicht
			if (jpaPanelReportIf instanceof ReportBeleg) {
				((ReportBeleg) jpaPanelReportIf).setBPrintLogo(bMitLogo);
				// wenn der in der falschen form da ist ...
				if (bMitLogo != print.getBMitLogo()
						|| iKopien != iKopienAktuell) {
					// ... muss er nochmal erzeugt werden
					print = createPrint(sDrucktype);
				}
			}

			/**
			 * @todo MR->MR: Achtung von etwas anderem ableiten !! z.B Kurzbrief
			 *       mit Logo das kein Reportbeleg ist hier abfangen
			 * 
			 */
			else if (jpaPanelReportIf instanceof com.lp.client.partner.ReportSerienbrief) {
				((com.lp.client.partner.ReportSerienbrief) jpaPanelReportIf)
						.setBPrintLogo(bMitLogo);
				// wenn der in der falschen form da ist ...
				if (bMitLogo != print.getBMitLogo()
						|| iKopien != iKopienAktuell) {
					// ... muss er nochmal erzeugt werden
					print = createPrint(sDrucktype);
				}
			} else if (jpaPanelReportIf instanceof com.lp.client.partner.ReportKurzbrief) {
				((com.lp.client.partner.ReportKurzbrief) jpaPanelReportIf)
						.setBPrintLogo(bMitLogo);
				// wenn der in der falschen form da ist ...
				if (bMitLogo != print.getBMitLogo()
						|| iKopien != iKopienAktuell) {
					// ... muss er nochmal erzeugt werden
					print = createPrint(sDrucktype);
				}
			}

		}
		// Kopien merken
		iKopien = iKopienAktuell;
		return print;
	}

	private JasperPrintLP createPrint(String sDrucktype) throws Throwable {
		JasperPrintLP print = null;
		try {
			LPMain.getTheClient().setReportvarianteIId(
					panelStandardDrucker.getVariante());
			print = jpaPanelReportIf.getReport(sDrucktype);
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
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"), message);
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
		JasperPrint print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(
				false, JasperPrintLP.DRUCKTYP_DRUCKER).getPrint();
		if (print.getPages().size() > 0) {
			PrintService printer = panelStandardDrucker
					.getSelectedPrintService();
			String sUbersteuerterDrucker = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.getPrinterNameForReport(jpaPanelReportIf.getModul(),
							jpaPanelReportIf.getReportname(),
							LPMain.getInstance().getUISprLocale(), null);
			if (sUbersteuerterDrucker != null) {
				PrintService[] printService = PrintServiceLookup
						.lookupPrintServices(null, null);
				for (int i = 0; i < printService.length; i++) {
					if (printService[i].getName().equals(sUbersteuerterDrucker)) {
						printer = printService[i];
					}
				}
			}
			if (printer != null) {
				try {
					PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
					int printableWidth = 0;
					int printableHeight = 0;
					
					if (print.getOrientationValue() == OrientationEnum.LANDSCAPE) {
						printableWidth = print.getPageHeight();
						printableHeight = print.getPageWidth();
					} else {
						printableWidth = print.getPageWidth();
						printableHeight = print.getPageHeight();
					}
				

					if ((printableWidth != 0) && (printableHeight != 0)) {
						printRequestAttributeSet.add(new MediaPrintableArea(
								0.0F, 0.0F, printableWidth / 72.0F,
								printableHeight / 72.0F,
								MediaPrintableArea.INCH));
					}

					PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
					printServiceAttributeSet.add(new PrinterName(printer
							.getName(), null));

					// AD JR LPJRPrintServiceExporter exporter = new
					// LPJRPrintServiceExporter();
					JRPrintServiceExporter exporter = new JRPrintServiceExporter();

					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							print);
					exporter.setParameter(
							JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
							printRequestAttributeSet);
					exporter.setParameter(
							JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
							printServiceAttributeSet);
					exporter.setParameter(
							JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
							Boolean.FALSE);
					exporter.setParameter(
							JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
							Boolean.FALSE);

					// print it
					int iAnzahl = 1;
					// Etiketten koennen mehrfach gedruckt werden
					if (jpaPanelReportIf instanceof ReportEtikett) {
						Integer iExemplare = ((ReportEtikett) jpaPanelReportIf)
								.getAnzahlExemplare();
						if (iExemplare != null && iExemplare.intValue() >= 1) {
							iAnzahl = iExemplare.intValue();
						}
					}

					if (jpaPanelReportIf instanceof ReportWepEtikett) {
						Integer iExemplare = ((ReportWepEtikett) jpaPanelReportIf)
								.getAnzahlExemplare();
						if (iExemplare != null && iExemplare.intValue() >= 1) {
							iAnzahl = iExemplare.intValue();
						}
					}

					PrintService psOverride = overrideAcceptingJobs(printer);
					if (psOverride != null) {
						exporter.setParameter(
								JRPrintServiceExporterParameter.PRINT_SERVICE,
								psOverride);

						// mehrere Exemplare geht auch
						/**
						 * @todo MR->VF exporter.exportReport() als Thread
						 *       laufen lassen.
						 */
						for (int i = 0; i < iAnzahl; i++) {
							exporter.exportReport();
						}
					}
				} catch (Exception ex) {
					if (ex instanceof ExceptionLP
							&& ((ExceptionLP) ex).getICode() == EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DRUCKER_INSTALLIERT) {
						bCloseDialog = false;
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.drucken.keindruckerinstalliert"));
					}

					else if (ex instanceof JRException) {
						// throw new JRException(ex);
						if (ex.getCause() != null) {
							if (ex.getCause() instanceof PrinterException) {
								// z.B Fehler wie "Printer is not Accepting Job"
								// oder "Invalid Name of PrinterService".
								throw new ExceptionLP(
										EJBExceptionLP.FEHLER_FEHLER_BEIM_DRUCKEN,
										ex);
							}
						}
					} else {
						new DialogError(LPMain.getInstance().getDesktop(), ex,
								DialogError.TYPE_ERROR);
					}
				}
				// PJ 17341
				boolean bAutomatischVerlassen = (Boolean) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								"ALLGEMEIN",
								ParameterFac.PARAMETER_DRUCKVORSCHAU_AUTOMATISCH_BEENDEN)
						.getCWertAsObject();

				if (bCloseDialog && bAutomatischVerlassen == true) {
					getInternalFrame().closePanelDialog();
				}
			} else {
				showDialogKeineDatenZuDrucken();
			}
		}
	}

	private boolean isAcceptingPrintJobs(PrintService printService) {
		// PrintService[] printers = PrinterJob.lookupPrintServices();
		// for(int i = 0 ; i < printers.length; i++) {
		// PrintServiceAttributeSet attributes = printers[i].getAttributes() ;
		// Attribute as[] = attributes.toArray() ;
		// for(int j = 0 ; j < as.length; j++) {
		// System.out.println("Attribute " + as[j].getName() + " " +
		// as[j].toString()) ;
		// }
		//
		// PrinterIsAcceptingJobs acceptingJob =
		// printers[i].getAttribute(PrinterIsAcceptingJobs.class) ;
		// System.out.println("actual state is " + acceptingJob.getValue() +
		// " for " + printers[i].getName()) ;
		// }
		//
		// PrintService tempPrinter =
		// PrintServiceLookup.lookupDefaultPrintService();
		// PrinterIsAcceptingJobs acceptingJob =
		// tempPrinter.getAttribute(PrinterIsAcceptingJobs.class) ;
		// if(null != acceptingJob) {
		// System.out.println("actual state is " + acceptingJob.getValue()) ;
		// }

		PrinterIsAcceptingJobs attribute = printService
				.getAttribute(PrinterIsAcceptingJobs.class);
		if (null == attribute)
			return true;
		return 1 == attribute.getValue();
		// return false ;
	}

	private PrintService overrideAcceptingJobs(PrintService printService) {
		if (!isAcceptingPrintJobs(printService)) {
			boolean shouldPrint = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("lp.drucken.hinweisprinterisnotacceptingjob"),
							LPMain.getTextRespectUISPr("lp.achtung"),
							JOptionPane.QUESTION_MESSAGE, JOptionPane.NO_OPTION);
			if (shouldPrint) {
				PrintService psOverride = new ForcedPrintServiceAcceptingJob(
						printService, true);
				printService = psOverride;
			} else {
				printService = null;
			}
		}

		return printService;
	}

	private void setDruckeinstellungenGeladen(boolean b) {
		panelStandardDrucker.getWbuLoeschen().setEnabled(b);
		panelStandardDrucker.getWbuSpeichern().setBackground(
				b ? new Color(0, 200, 0) : null);
		panelStandardDrucker.getWbuSpeichern().setOpaque(true);
	}

	private void initStandarddrucker() throws Throwable {
		if (standarddruckerDto == null) {
			standarddruckerDto = new StandarddruckerDto();
		}
		standarddruckerDto.setCPc(Helper.getPCName());
		if (jpaPanelReportIf instanceof ReportBeleg) {
			Integer kstIId = ((ReportBeleg) jpaPanelReportIf)
					.getKostenstelleIId();
			standarddruckerDto.setKostenstelleIId_notInDB(kstIId);
		}
		/**
		 * @todo das Locale des Reports
		 */
		standarddruckerDto.setLocale_notInDB(LPMain.getTheClient().getLocUi());
		standarddruckerDto.setMandantCNr(LPMain.getTheClient().getMandant());
		standarddruckerDto.setSFilename_notInDB(jpaPanelReportIf
				.getReportname());
		standarddruckerDto.setSModul_notInDB(jpaPanelReportIf.getModul());

		if (bVariantenInitialisiert == true
				&& panelStandardDrucker.getVariante() == null) {
			standarddruckerDto.setReportvarianteIId(null);
			// Drucker holen
			standarddruckerDto = DelegateFactory
					.getInstance()
					.getDruckerDelegate()
					.standarddruckerFindByPcReportnameOhneVariante(
							standarddruckerDto);
		} else {
			standarddruckerDto.setReportvarianteIId(panelStandardDrucker
					.getVariante());
			// Drucker holen
			standarddruckerDto = DelegateFactory
					.getInstance()
					.getDruckerDelegate()
					.standarddruckerFindByPcReportnameOhneExc(
							standarddruckerDto);
		}

		if (bVariantenInitialisiert == false) {

			Map m = DelegateFactory.getInstance().getDruckerDelegate()
					.holeAlleVarianten(jpaPanelReportIf.getReportname());
			panelStandardDrucker.getWcoVariante().setMap(m);

			panelStandardDrucker.getWcoVariante().addActionListener(
					new PanelReportKriterien_wcoVariante_actionAdapter(this));

			bVariantenInitialisiert = true;
		}

		// Drucker vorselektieren
		boolean druckerEinstellungenGeladen = false;
		if (standarddruckerDto != null) {

			panelStandardDrucker.setSelectedPrinter(standarddruckerDto
					.getCDrucker());

			if (standarddruckerDto.getReportvarianteIId() != null) {
				panelStandardDrucker.getWcoVariante().setKeyOfSelectedItem(
						standarddruckerDto.getReportvarianteIId());
			}

			konfDtos = DelegateFactory
					.getInstance()
					.getDruckerDelegate()
					.reportkonfFindByStandarddruckerIId(
							standarddruckerDto.getIId());
			druckerEinstellungenGeladen = (konfDtos != null && konfDtos.length > 0);
			// Nun Einstellungen wieder in due Komponenenten schreiben
			schreibeReportKonfInDieKomponentenZurueck(konfDtos);

		}

		// Buttons anpassen
		setDruckeinstellungenGeladen(druckerEinstellungenGeladen);

		panelStandardDrucker.getWbuSpeichern().addActionListener(this);
		panelStandardDrucker.getWbuSpeichern().setActionCommand(
				ACTION_SPECIAL_DRUCKER_SPEICHERN);
		panelStandardDrucker.getWbuLoeschen().addActionListener(this);
		panelStandardDrucker.getWbuLoeschen().setActionCommand(
				ACTION_SPECIAL_DRUCKER_LOESCHEN);

		if (panelVersandEmail != null) {
			panelVersandEmail.bUebersteuerterEmpaengerVorschlagGesetzt = false;
			panelVersandEmail.bUebersteuerterEmpaengerVorschlagCCGesetzt = false;
			panelVersandEmail.setVorschlag();
		}

	}

	private void schreibeReportKonfInDieKomponentenZurueck(ReportkonfDto[] dtos)
			throws Throwable {

		// Zuerst alle Check-Boxen und Radio-Buttons

		for (int i = 0; i < dtos.length; i++) {
			ReportkonfDto dto = dtos[i];

			if (dto.getCKomponententyp().equals(
					DruckerFac.REPORTKONF_KOMPONENTENTYP_RADIOBUTTON)
					|| dto.getCKomponententyp().equals(
							DruckerFac.REPORTKONF_KOMPONENTENTYP_CHECKBOX)) {

				sucheKomponente(dto,
						((PanelBasis) jpaPanelReportIf).getComponents());
			}

		}

		for (int i = 0; i < dtos.length; i++) {
			ReportkonfDto dto = dtos[i];

			sucheKomponente(dto,
					((PanelBasis) jpaPanelReportIf).getComponents());

		}

	}

	private void sucheKomponente(ReportkonfDto dto, Component[] components)
			throws Throwable {

		try {
			for (int i = 0; i < components.length; ++i) {

				if (components[i].getName() != null
						&& components[i].getName().equals(
								dto.getCKomponentenname())) {
					if (components[i] instanceof WrapperSelectField) {
						WrapperSelectField wsf = (WrapperSelectField) components[i];

						wsf.setSTyp(dto.getCTyp());

						try {
							Integer iKey = new Integer(dto.getCKey());
							wsf.setKey(iKey);
						} catch (Exception e) {
							wsf.setKey(dto.getCKey());
						}

					} else if (components[i] instanceof WrapperTextField) {

						WrapperTextField wtf = (WrapperTextField) components[i];

						if (wtf.isSaveReportInformation() == true) {
							wtf.setText(dto.getCKey());
						}

					} else if (components[i] instanceof WrapperCheckBox) {

						WrapperCheckBox wcb = (WrapperCheckBox) components[i];

						wcb.setShort(new Short(dto.getCKey()));
					} else if (components[i] instanceof WrapperRadioButton
							|| components[i] instanceof JRadioButton) {

						JRadioButton wcb = (JRadioButton) components[i];

						wcb.doClick();

					} else if (components[i] instanceof WrapperComboBox) {

						WrapperComboBox wcb = (WrapperComboBox) components[i];

						try {
							Integer iKey = new Integer(dto.getCKey());
							wcb.setKeyOfSelectedItem(iKey);
						} catch (Exception e) {
							wcb.setKeyOfSelectedItem(dto.getCKey());
						}

					} else if (components[i] instanceof WrapperDateField) {

						WrapperDateField wcb = (WrapperDateField) components[i];
						Long iKey = new Long(dto.getCKey());
						wcb.setDate(new java.sql.Date(iKey));
					} else if (components[i] instanceof WrapperNumberField) {
						WrapperNumberField wcb = (WrapperNumberField) components[i];
						wcb.setBigDecimal(new BigDecimal(dto.getCKey()));
					}

				}
				if ((components[i] instanceof java.awt.Container)
						&& !(components[i] instanceof IControl)) {

					sucheKomponente(dto,
							((java.awt.Container) components[i])
									.getComponents());
				}
			}
		} catch (Exception e) {
			// WENN FEHLER, dann wird der Eintrag geloescht
			DelegateFactory.getInstance().getDruckerDelegate()
					.removeReportkonf(dto.getIId());
		}

	}

	private ReportkonfDto[] getKomponentenEinesReports() throws Throwable {
		ArrayList<?> al = getReportkonfDtos(new ArrayList(),
				((PanelBasis) jpaPanelReportIf).getComponents());

		ReportkonfDto[] a = new ReportkonfDto[al.size()];
		return al.toArray(a);

	}

	private ArrayList getReportkonfDtos(ArrayList alKomponenten,
			Component[] components) throws Throwable {
		for (int i = 0; i < components.length; ++i) {

			if ((components[i] instanceof java.awt.Container)
					&& !(components[i] instanceof IControl)
					&& !(components[i] instanceof JRadioButton)) {
				if (!(components[i] instanceof WrapperLabel)) {
					alKomponenten = getReportkonfDtos(alKomponenten,
							((java.awt.Container) components[i])
									.getComponents());
				}

			} else {
				ReportkonfDto reportkonfDto = new ReportkonfDto();
				reportkonfDto.setCKomponentenname(components[i].getName());
				if (components[i].getName() != null) {
					if (components[i] instanceof WrapperSelectField) {
						WrapperSelectField wsf = (WrapperSelectField) components[i];

						if (wsf.getOKey() != null) {

							reportkonfDto.setCKey(wsf.getOKey().toString());
							reportkonfDto
									.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_SELECTFIELD);
							reportkonfDto.setCTyp(wsf.getSTyp());

						}

					} else if (components[i] instanceof WrapperTextField) {

						WrapperTextField wtf = (WrapperTextField) components[i];
						if (wtf.isSaveReportInformation() == true
								&& wtf.getText() != null) {
							reportkonfDto.setCKey(wtf.getText());
							reportkonfDto
									.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_TEXTFIELD);
						}
					} else if (components[i] instanceof WrapperCheckBox) {

						WrapperCheckBox wcb = (WrapperCheckBox) components[i];

						reportkonfDto.setCKey(wcb.getShort().toString());
						reportkonfDto
								.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_CHECKBOX);

					} else if (components[i] instanceof WrapperRadioButton
							|| components[i] instanceof JRadioButton) {

						JRadioButton wcb = (JRadioButton) components[i];

						if (wcb.isSelected() == true) {
							reportkonfDto.setCKey(Helper.boolean2Short(
									wcb.isSelected()).toString());
							reportkonfDto
									.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_RADIOBUTTON);
						}

					} else if (components[i] instanceof WrapperComboBox) {

						WrapperComboBox wcb = (WrapperComboBox) components[i];
						if (wcb.getKeyOfSelectedItem() != null) {
							reportkonfDto.setCKey(wcb.getKeyOfSelectedItem()
									.toString());
							reportkonfDto
									.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_COMBOBOX);
						}
					} else if (components[i] instanceof WrapperDateField) {

						WrapperDateField wcb = (WrapperDateField) components[i];
						if (wcb.getDate() != null) {
							reportkonfDto.setCKey(wcb.getDate().getTime() + "");
							reportkonfDto
									.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_DATEFIELD);
						}
					} else if (components[i] instanceof WrapperNumberField) {

						WrapperNumberField wcb = (WrapperNumberField) components[i];
						if (wcb.getBigDecimal() != null) {
							reportkonfDto.setCKey(wcb.getBigDecimal()
									.toString());
							reportkonfDto
									.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_NUMBERFIELD);
						}
					}

					if (reportkonfDto.getCKey() != null) {
						alKomponenten.add(reportkonfDto);
					}
				}
			}

		}
		if (panelReport != null
				&& panelReport.getJpaViewer().getCmbZoom().getSelectedItem() != null) {
			ReportkonfDto reportkonfDto = new ReportkonfDto();
			reportkonfDto.setCKomponentenname("ZOOMFAKTOR");

			reportkonfDto.setCKey(panelReport.getJpaViewer().getCmbZoom()
					.getSelectedItem()
					+ "");
			reportkonfDto
					.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_COMBOBOX);

			alKomponenten.add(reportkonfDto);
		}

		return alKomponenten;
	}

	private void saveStandarddrucker() throws Throwable {
		try {
			if (standarddruckerDto == null) {
				standarddruckerDto = new StandarddruckerDto();
			}
			if (Helper.getPCName() != null
					&& jpaPanelReportIf.getModul() != null
					&& jpaPanelReportIf.getReportname() != null) {
				// nur, wenn auch ein Drucker ausgewaehlt ist
				if (panelStandardDrucker.getSelectedPrinter() != null) {
					standarddruckerDto.setCPc(Helper.getPCName());
					if (jpaPanelReportIf instanceof ReportBeleg) {
						Integer kstIId = ((ReportBeleg) jpaPanelReportIf)
								.getKostenstelleIId();
						standarddruckerDto.setKostenstelleIId_notInDB(kstIId);
					}
					/**
					 * @todo das Locale des Reports
					 */
					standarddruckerDto.setLocale_notInDB(LPMain.getTheClient()
							.getLocUi());
					standarddruckerDto.setMandantCNr(LPMain.getTheClient()
							.getMandant());
					standarddruckerDto.setSFilename_notInDB(jpaPanelReportIf
							.getReportname());
					standarddruckerDto.setSModul_notInDB(jpaPanelReportIf
							.getModul());
					standarddruckerDto.setCDrucker(panelStandardDrucker
							.getSelectedPrinter());
					standarddruckerDto.setBStandard(Helper.boolean2Short(true));

					standarddruckerDto
							.setReportvarianteIId(panelStandardDrucker
									.getVariante());
					if (jpaPanelReportIf instanceof ReportBeleg) {
						standarddruckerDto
								.setKostenstelleIId_notInDB(((ReportBeleg) jpaPanelReportIf)
										.getKostenstelleIId());
					}
					Integer iId = DelegateFactory.getInstance()
							.getDruckerDelegate()
							.updateStandarddrucker(standarddruckerDto);
					standarddruckerDto.setIId(iId);

					// PJ 14057 Nun auch Einstellung mitspeichern
					ReportkonfDto[] dtos = getKomponentenEinesReports();

					if (dtos != null && dtos.length > 0) {
						DelegateFactory
								.getInstance()
								.getDruckerDelegate()
								.saveReportKonf(standarddruckerDto.getIId(),
										dtos);
						setDruckeinstellungenGeladen(true);
					}

				}
			}
		} catch (ExceptionLP ex) {
			if (ex.getICode() == EJBExceptionLP.FEHLER_BEIM_ANLEGEN) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.drucken.druckereinstellungkonntenichtgespeichertwerden"));
			} else {
				throw ex;
			}
		}
	}

	private void removeStandarddrucker() throws Throwable {
		if (standarddruckerDto != null) {
			DelegateFactory.getInstance().getDruckerDelegate()
					.deleteReportKonf(standarddruckerDto.getIId());
			DelegateFactory.getInstance().getDruckerDelegate()
					.removeStandarddrucker(standarddruckerDto);
			standarddruckerDto = null;

			setDruckeinstellungenGeladen(false);

		}
	}

	private void archiviereBeleg() throws ExceptionLP, Throwable {
		String sBelegnummer = null;
		String sRow = null;
		LPMain.getInstance();
		TheClientDto theClientDto = LPMain.getTheClient();
		JasperPrintLP print;
		if (panelReport != null) {
			print = panelReport.getPrint();
		} else {
			print = createPrint(null);
		}
		// Path Seperator vom server holen damit der Name richtig angezeigt
		// werden kann
		String sPathSeperator = DelegateFactory.getInstance()
				.getSystemDelegate().getServerPathSeperator();
		String sName = print.getSReportName().substring(
				print.getSReportName().lastIndexOf(sPathSeperator) + 1);
		// Wennn ein FLR-Druck dann is der Reportname ""
		if (!sName.equals("")) {
			DokumentnichtarchiviertDto docNichtArchivDto = DelegateFactory
					.getInstance()
					.getJCRDocDelegate()
					.dokumentnichtarchiviertfindbyMandantCReportname(
							theClientDto.getMandant(), sName);
			if (docNichtArchivDto == null) {
				PrintInfoDto oInfo = print.getOInfoForArchive();
				DocPath docPath = null;
				if (oInfo != null) {
					docPath = oInfo.getDocPath();
				}
				if (docPath != null) {
					// Nur archivieren wenn nicht in dieser Tabelle vorhanden
					JCRDocDto jcrDocDto = new JCRDocDto();
					sBelegnummer = docPath.getLastDocNode().asVisualPath();
					;
					if (sBelegnummer == null) {
						sBelegnummer = "0000000";
					}
					sRow = docPath.getLastDocNode().asPath();
					sRow = sRow == null ? " " : sRow;
					Integer iPartnerIId;
					if (oInfo.getiId() != null) {
						iPartnerIId = oInfo.getiId();
					} else {
						if (partnerDtoEmpfaenger != null) {
							iPartnerIId = partnerDtoEmpfaenger.getIId();
						} else {
							// Wenn kein Partner uebergeben dann Default
							MandantDto mandantDto = DelegateFactory
									.getInstance()
									.getMandantDelegate()
									.mandantFindByPrimaryKey(
											theClientDto.getMandant());
							iPartnerIId = mandantDto.getPartnerIId();
						}
					}
					// File file = File.createTempFile(sName, ".jrprint");
					// file.delete();
					// JRSaver.saveObject(print.getPrint(), file);

					Helper.setJcrDocBinaryData(jcrDocDto, print.getPrint());

					// AD JR JRPrintSaveContributor saver = new
					// JRPrintSaveContributor(getLocale(),));
					// AD JR saver.save(print.getPrint(), file);
					// jcrDocDto.setbData(Helper.getBytesFromFile(file));
					jcrDocDto.setJasperPrint(print.getPrint());
					// file.delete();
					jcrDocDto.setDocPath(docPath.add(new DocNodeFile(sName)));
					jcrDocDto.setlPartner(iPartnerIId);
					jcrDocDto.setsBelegnummer(sBelegnummer);
					jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
					jcrDocDto
							.setlAnleger(LPMain.getTheClient().getIDPersonal());
					jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
					jcrDocDto.setsSchlagworte(" ");
					jcrDocDto.setsName(sName);
					jcrDocDto.setsFilename(sName + ".jrprint");
					if (oInfo.getTable() != null) {
						jcrDocDto.setsTable(oInfo.getTable());
					} else {
						jcrDocDto.setsTable(" ");
					}
					jcrDocDto.setsRow(sRow);
					jcrDocDto.setsMIME(".jrprint");
					jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
					jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
					jcrDocDto.setbVersteckt(false);
					jcrDocDto
							.setlVersion(DelegateFactory.getInstance()
									.getJCRDocDelegate()
									.getNextVersionNumer(jcrDocDto));
					DelegateFactory.getInstance().getJCRDocDelegate()
							.addNewDocumentOrNewVersionOfDocument(jcrDocDto);
				} else {

					// PJ17937
					if (LPMain.getInstance().isLPAdmin()) {

						String[] optionen = new String[] {
								LPMain.getTextRespectUISPr("button.ok"),
								LPMain.getTextRespectUISPr("lp.jcr.keinbelegpfad.button.lpadmin") };

						int iOption = DialogFactory
								.showModalDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("lp.jcr.keinbelegpfad"),
										LPMain.getTextRespectUISPr("lp.hint"),
										optionen, optionen[0]);

						if (iOption == 1) {
							// Archivierung deaktivieren
							DelegateFactory
									.getInstance()
									.getJCRDocDelegate()
									.deaktiviereArchivierung(
											theClientDto.getMandant(), sName);
						}

					} else {
						DialogFactory.showModalDialog(LPMain
								.getTextRespectUISPr("lp.hint"), LPMain
								.getTextRespectUISPr("lp.jcr.keinbelegpfad"));
					}

				}
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

	private void setzeVersandstatus(String sDruckart) throws Throwable {
		JasperPrintLP print;
		if (panelReport != null) {
			print = panelReport.getPrint();
		} else {
			print = createPrint(null);
		}
		String sBelegart = (String) print
				.getAdditionalInformation(JasperPrintLP.KEY_BELEGART);

		Integer iIId = (Integer) print
				.getAdditionalInformation(JasperPrintLP.KEY_BELEGIID);

		if (LocaleFac.BELEGART_BESTELLUNG.equals(sBelegart)) {
			DelegateFactory.getInstance().getBestellungDelegate()
					.setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_ANFRAGE.equals(sBelegart)) {
			DelegateFactory.getInstance().getAnfrageDelegate()
					.setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_ANGEBOT.equals(sBelegart)) {
			DelegateFactory.getInstance().getAngebotDelegate()
					.setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_AUFTRAG.equals(sBelegart)) {
			DelegateFactory.getInstance().getAuftragDelegate()
					.setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_LIEFERSCHEIN.equals(sBelegart)) {
			DelegateFactory.getInstance().getLsDelegate()
					.setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_RECHNUNG.equals(sBelegart)) {
			DelegateFactory.getInstance().getRechnungDelegate()
					.setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}
		if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(sBelegart)) {
			DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.updateEingangsrechnungGedruckt(iIId);
		}
		if(jpaPanelReportIf instanceof IAktiviereBelegReport) {
			IAktiviereBelegReport rep = (IAktiviereBelegReport)jpaPanelReportIf;
			rep.refreshPanelInBackground();
		}

	}

	private boolean checkValuesForChanges() {
		if (reportkonfCache == null)
			return true;
		ReportkonfDto[] actualKonf = null;
		try {
			actualKonf = getKomponentenEinesReports();
		} catch (Throwable e) {
			return true; // Vorsichtshalber true zurueckgeben
		}
		if (actualKonf.length != reportkonfCache.size())
			return true;
		for (int i = 0; i < actualKonf.length; i++) {
			ReportkonfDto actual = actualKonf[i];
			ReportkonfDto cache = reportkonfCache.get(i);
			if ((actual.getCKey() == null) != (cache.getCKey() == null)) {
				return true;
			}
			if (actual.getCKey() != null
					&& !actual.getCKey().equals(cache.getCKey())) {
				return true;
			}
		}
		return false;
	}

	private void saveValuesToCache() {
		try {
			reportkonfCache = Arrays.asList(getKomponentenEinesReports());
		} catch (Throwable e) {
			handleException(e, false);
		}
	}

	private void setRefreshNecessary(boolean necessary) {
		AbstractButton refreshBtn = getHmOfButtons().get(
				ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW).getButton();
		refreshBtn.setOpaque(true);
		// refreshBtn.setBorderPainted(necessary);
		refreshBtn.setBackground(necessary ? Color.blue : UIManager
				.getColor("Button.background"));
	}

	@Override
	public void vetoableChange(PropertyChangeEvent evt)
			throws PropertyVetoException {
		if ("focusOwner".equals(evt.getPropertyName())
				&& evt.getNewValue() instanceof Component) {
			if (SwingUtilities.isDescendingFrom((Component) evt.getNewValue(),
					(Component) jpaPanelReportIf)) {
				updateRefreshButton();
			}
		}
	}

	private void updateRefreshButton() {
		boolean changed = checkValuesForChanges();
		setRefreshNecessary(changed);
	}
}

class PanelReportKriterien_wcoVariante_actionAdapter implements ActionListener {
	private PanelReportKriterien adaptee;

	PanelReportKriterien_wcoVariante_actionAdapter(PanelReportKriterien adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoVariante_actionPerformed(e);
	}
}
