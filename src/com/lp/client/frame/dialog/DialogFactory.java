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
package com.lp.client.frame.dialog;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.filechooser.FileFilter;

import com.lp.client.finanz.DialogBelegKurspruefung;
import com.lp.client.finanz.DialogFibuFehlerResult;
import com.lp.client.finanz.DialogPeriodeAuswahl;
import com.lp.client.frame.component.DialogMitScollbar;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.client.system.DialogArtikelhinweis;
import com.lp.client.system.DialogDatumseingabe;
import com.lp.client.system.DialogDatumseingabeVonBis;
import com.lp.client.system.DialogEingabeJahr;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.finanz.service.FibuFehlerDto;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVWriter;

/**
 * <p>
 * Diese Klasse kuemmert sich um das Anzeigen von Dialogen aller Art
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 04.09.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/02/04 08:52:16 $
 */
public class DialogFactory {
	public static int showMeldung(String sMeldung, String sTitleI, int iOptionI) {
		return showMeldung(sMeldung, sTitleI, JOptionPane.QUESTION_MESSAGE,
				iOptionI);
		/*
		 * JOptionPane pane = InternalFrame
		 * .getNarrowOptionPane(Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
		 * pane.setMessage(sMeldung);
		 * pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
		 * pane.setOptionType(iOptionI); JDialog dialog =
		 * pane.createDialog(LPMain.getInstance().getDesktop(), sTitleI);
		 * dialog.setVisible(true);
		 * 
		 * if (pane.getValue() != null) { return ((Integer)
		 * pane.getValue()).intValue(); } else { return
		 * JOptionPane.CLOSED_OPTION; }
		 */
	}

	public static int showMeldung(String sMeldung, String sTitel,
			int messageType, int optionType) {
		JOptionPane pane = InternalFrame
				.getNarrowOptionPane(Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
		pane.setMessage(sMeldung);
		pane.setMessageType(messageType);
		pane.setOptionType(optionType);
		JDialog dialog = pane.createDialog(LPMain.getInstance().getDesktop(),
				sTitel);
		dialog.setVisible(true);

		if (pane.getValue() != null) {
			return ((Integer) pane.getValue()).intValue();
		} else {
			return JOptionPane.CLOSED_OPTION;
		}
	}

	// public static int showImportantMeldung(String sMeldung, String sTitleI,
	// int iOptionI) {
	//
	// JOptionPane pane = InternalFrame
	// .getNarrowOptionPane(Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
	// pane.setMessage(sMeldung);
	// pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
	// pane.setOptionType(iOptionI);
	// pane.setBackground(Color.red) ;
	// Component[] components = pane.getComponents() ;
	// for (Component component : components) {
	// component.setBackground(Color.red) ;
	// }
	// JDialog dialog = pane.createDialog(LPMain.getInstance().getDesktop(),
	// sTitleI);
	// dialog.setBackground(Color.red) ;
	// dialog.setVisible(true);
	//
	// if (pane.getValue() != null) {
	// return ((Integer) pane.getValue()).intValue();
	// } else {
	// return JOptionPane.CLOSED_OPTION;
	// }
	// }

	public static int showModalJaNeinAbbrechenDialog(
			InternalFrame internalFrame, Object message, String title) {
		Object[] options = { LPMain.getTextRespectUISPr("lp.ja"),
				LPMain.getTextRespectUISPr("lp.nein"),
				LPMain.getTextRespectUISPr("lp.abbrechen"), };
		return JOptionPane.showOptionDialog(internalFrame, message, title,
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, // don't use a custom Icon
				options, // the titles of buttons
				options[0]); // default button title
	}

	/**
	 * Zeige einen modalen Warnungsdialog.
	 * 
	 * @param title
	 *            String
	 * @param msg
	 *            String
	 */
	public static void showModalDialog(String title, String msg) {
		LPMain.getInstance().getDesktop()
				.showModalDialog(title, msg, JOptionPane.WARNING_MESSAGE);
	}

	public static void showMessageMitScrollbar(String title, String message,
			boolean bEnableScrollPane) throws Throwable {
		DialogMitScollbar d = new DialogMitScollbar(title, message,
				bEnableScrollPane);
		d.setSize(500, 500);
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);

	}

	public static void showMessageMitScrollbar(String title, String message)
			throws Throwable {
		showMessageMitScrollbar(title, message, false);

	}

	public static int[] showPeriodeAuswahl(GeschaeftsjahrMandantDto gjDto)
			throws Throwable {
		DialogPeriodeAuswahl d = new DialogPeriodeAuswahl(gjDto);
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
		int[] ret = { d.periode, (d.alleneu ? 1 : 0) };
		return ret;
	}

	public static ArrayList<Object> showBelegKurspruefungOptionen(
			Map<?, ?> geschaeftsjahre) throws Throwable {
		DialogBelegKurspruefung d = new DialogBelegKurspruefung(geschaeftsjahre);
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
		ArrayList<Object> al = new ArrayList<Object>();
		al.add(d.getGeschaeftsjahr());
		al.add(d.isNurPruefen());
		return al;
	}

	public static java.sql.Date showDatumseingabe() throws Throwable {
		DialogDatumseingabe d = new DialogDatumseingabe();
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
		return d.datum;
	}

	public static Integer showJahreseingabe() throws Throwable {
		DialogEingabeJahr d = new DialogEingabeJahr();
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
		return d.iJahr;
	}

	public static java.sql.Date[] showDatumseingabeVonBis(String sTitle)
			throws Throwable {
		DialogDatumseingabeVonBis d = new DialogDatumseingabeVonBis();
		d.setTitle(sTitle);
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);

		java.sql.Date[] datum = new java.sql.Date[2];
		datum[0] = d.datumVon;
		datum[1] = d.datumBis;
		return datum;
	}

	public static void showArtikelHinweisBild(ArrayList<byte[]> bilder,
			InternalFrame internalFrame) throws Throwable {
		DialogArtikelhinweis d = new DialogArtikelhinweis(bilder, internalFrame);
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
	}

	public static java.sql.Date showDatumseingabe(String sTitle)
			throws Throwable {
		DialogDatumseingabe d = new DialogDatumseingabe();
		d.setTitle(sTitle);
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);

		return d.datum;
	}

	public static java.sql.Date showDatumseingabeDefaultHeute(String sTitle)
			throws Throwable {
		DialogDatumseingabe d = new DialogDatumseingabe();
		d.setTitle(sTitle);
		d.getWnfDatum().setDate(new java.sql.Date(System.currentTimeMillis()));
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);

		return d.datum;
	}

	public static void showCSVExportDialog(char trennzeichen, String[][] daten)
			throws IOException {
		JFileChooser fc = new JFileChooser();
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		fc.setDialogTitle(LPMain.getTextRespectUISPr("lp.report.save"));

		fc.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".txt")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "CSV-Dateien";
			}
		});

		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			if (file.exists()) {
				int erg = JOptionPane
						.showConfirmDialog(LPMain.getInstance().getDesktop(),
								"Die Datei " + file.getName()
										+ " existiert bereits.\n"
										+ "Wollen Sie sie \u00FCberschreiben?",
								"Editor: Warnung", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (erg == JOptionPane.NO_OPTION)
					return;
			}

			LPCSVWriter writer;
			try {
				writer = new LPCSVWriter(new FileWriter(file), trennzeichen,
						LPCSVWriter.DEFAULT_QUOTE_CHARACTER);
				for (int i = 0; i < daten.length; i++) {
					writer.writeNext(daten[i]);
				}

				writer.close();

				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("system.extraliste.gespeichert")
										+ " (" + file.getAbsolutePath() + ") ");

			} catch (java.io.FileNotFoundException e) {
				// ev. Datei gerade verwendet?
				showModalDialog(
						"Fehler",
						"Die angegeben Datei '"
								+ file.getAbsolutePath()
								+ "' konnte nicht erstellt werden. M\u00F6glicherweise wird sie durch ein anderes Programm verwendet.");

			}

		}
	}

	public static int showModalDialog(InternalFrame internalFrame,
			Object message, String title, Object[] options, Object initialValue) {
		return JOptionPane.showOptionDialog(internalFrame, message, title,
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, // Icon
				options, initialValue);
	}

	/**
	 * Eine Verkaufsbelegposition auf Null- bzw. Unterpreise pruefen.
	 * 
	 * @param internmalFrameI
	 *            InternalFrame
	 * @param artikelIId
	 *            Integer
	 * @param lagerIId
	 *            Integer
	 * @param bdNettopreis
	 *            BigDecimal
	 * @param dWechselkurs
	 *            Double
	 * @return boolean
	 * @throws Throwable
	 */
	public static final boolean pruefeUnterpreisigkeitDlg(
			InternalFrame internmalFrameI, Integer artikelIId,
			Integer lagerIId, BigDecimal bdNettopreis, Double dWechselkurs,
			BigDecimal nMenge) throws Throwable {
		boolean bSpeichern = true;
		if (internmalFrameI.bRechtDarfPreiseSehenVerkauf) {
			// Schritt 1: auf Nullpreise pruefen.

			ArtikelDto artikeldDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);

			if (!Helper.short2boolean(artikeldDto.getBKalkulatorisch())) {

				if (bdNettopreis != null
						&& bdNettopreis.compareTo(new BigDecimal(0)) == 0) {

					if (internmalFrameI.isBNullpreiswarnungAnzeigen() == true) {
						JCheckBox checkbox = new JCheckBox(
								LPMain.getTextRespectUISPr("lp.warning.nullpreis.nichtmehrfragen"));
						String message = LPMain
								.getTextRespectUISPr("lp.warning.nullpreis");
						Object[] params = { message, checkbox };
						int n = JOptionPane.showConfirmDialog(internmalFrameI,
								params,
								LPMain.getTextRespectUISPr("lp.warning"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
						if (checkbox.isSelected()) {
							internmalFrameI.setBNullpreiswarnungAnzeigen(false);
						}

						if (n == JOptionPane.YES_OPTION) {
							bSpeichern = true;
						} else {
							bSpeichern = false;
						}
					}

				}
				// Schritt 2: Auf Unterpreise pruefen.
				if (bSpeichern) {
					// auf Mindest-VK-Preis pruefen.
					boolean bIstUnterpreisig;
					if (lagerIId != null) { // Pruefung auf ein bestimmtes Lager
						bIstUnterpreisig = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.liegtVerkaufspreisUnterMinVerkaufspreis(
										artikelIId, lagerIId, bdNettopreis,
										dWechselkurs, nMenge);
					} else {
						bIstUnterpreisig = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.liegtVerkaufpreisUnterMinVerkaufspreis(
										artikelIId, bdNettopreis, dWechselkurs,
										nMenge);
					}
					if (bIstUnterpreisig) {
						bSpeichern = showModalJaNeinDialog(
								internmalFrameI,
								LPMain.getTextRespectUISPr("lp.warning.unterpreisigkeit"),
								LPMain.getTextRespectUISPr("lp.warning"),
								JOptionPane.WARNING_MESSAGE,
								JOptionPane.NO_OPTION);
					}
				}
			}
		}
		return bSpeichern;
	}

	/**
	 * Soll eine Position mit einem eventuellen zugehoerigen Artikel angelegt
	 * werden?
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @throws Throwable
	 * @return boolean
	 */
	public static final boolean pruefeZugehoerigenArtikelAnlegenDlg(
			ArtikelDto artikelDto) throws Throwable {
		boolean bPositionFuerZugehoerigenArtikelAnlegen = false;

		// PJ17804
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_DAZUGEHOERT_BERUECKSICHTIGEN,
						ParameterFac.KATEGORIE_ALLGEMEIN,
						LPMain.getTheClient().getMandant());
		boolean b = (Boolean) parameter.getCWertAsObject();

		if (b == true) {
			if (artikelDto.getArtikelIIdZugehoerig() != null) {
				if (DialogFactory
						.showMeldung(
								LPMain.getTextRespectUISPr("lp.positionfuerzugehoerigenartikel"),
								LPMain.getTextRespectUISPr("lp.frage"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
					bPositionFuerZugehoerigenArtikelAnlegen = true;
				}
			}
		}
		return bPositionFuerZugehoerigenArtikelAnlegen;
	}

	public static boolean showModalJaNeinDialog(InternalFrame internalFrame,
			Object sMeldung) {
		return showModalJaNeinDialog(internalFrame, sMeldung,
				LPMain.getTextRespectUISPr("lp.frage"));
	}

	/**
	 * 
	 * @param internalFrame
	 *            InternalFrame
	 * @param sMeldung
	 *            Object
	 * @param sTitleI
	 *            String
	 * @return boolean
	 */
	public static boolean showModalJaNeinDialog(InternalFrame internalFrame,
			Object sMeldung, String sTitleI) {
		return showModalJaNeinDialog(internalFrame, sMeldung, sTitleI,
				JOptionPane.QUESTION_MESSAGE, JOptionPane.NO_OPTION);
	}

	/**
	 * 
	 * @param internalFrame
	 *            InternalFrame
	 * @param sMeldung
	 *            Object
	 * @param sTitleI
	 *            String
	 * @param iMessageType
	 *            int
	 * @param iInitialOption
	 *            JOptionPane.YES_OPTION oder JOptionPane.NO_OPTION
	 * @return boolean
	 */
	public static boolean showModalJaNeinDialog(InternalFrame internalFrame,
			Object sMeldung, String sTitleI, int iMessageType,
			int iInitialOption) {
		final String sYesOption = LPMain.getTextRespectUISPr("lp.ja");
		final String sNoOption = LPMain.getTextRespectUISPr("lp.nein");
		Object[] options = { sYesOption, sNoOption, };
		JOptionPane pane = InternalFrame
				.getNarrowOptionPane(Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
		pane.setMessage(sMeldung);
		pane.setMessageType(iMessageType);
		pane.setOptions(options);
		if (iInitialOption == JOptionPane.YES_OPTION) {
			pane.setInitialValue(sYesOption);
		} else {
			pane.setInitialValue(sNoOption);
		}
		JDialog dialog = pane.createDialog(internalFrame, sTitleI);

		// PJ15280 -> Mnemonics auf JA/NEIN
		Component[] c = dialog.getComponents();
		if (c.length > 0 && c[0] instanceof JRootPane) {
			JRootPane rp = (JRootPane) c[0];
			if (rp.getComponents().length > 1
					&& rp.getComponents()[1] instanceof JLayeredPane) {
				JLayeredPane lp = (JLayeredPane) rp.getComponents()[1];
				if (lp.getComponents().length > 0
						&& lp.getComponents()[0] instanceof JPanel) {
					JPanel panel = (JPanel) lp.getComponents()[0];
					if (panel.getComponents().length > 0
							&& panel.getComponents()[0] instanceof JOptionPane) {
						JOptionPane panel2 = (JOptionPane) panel
								.getComponents()[0];
						if (panel2.getComponents().length > 1
								&& panel2.getComponents()[1] instanceof JPanel) {
							JPanel panelButtons = (JPanel) panel2
									.getComponents()[1];
							if (panelButtons.getComponents().length > 1) {
								if (panelButtons.getComponents()[0] instanceof JButton) {
									JButton button = (JButton) panelButtons
											.getComponents()[0];
									button.setMnemonic('J');
								}
								if (panelButtons.getComponents()[1] instanceof JButton) {
									JButton button = (JButton) panelButtons
											.getComponents()[1];
									button.setMnemonic('N');
								}
							}
						}
					}
				}
			}
		}

		pane.selectInitialValue();
		dialog.setVisible(true);
		return (pane.getValue() != null && pane.getValue().equals(sYesOption));
	}

	public static void showBelegPruefergebnis(InternalFrame internalFrame,
			ArrayList<FibuFehlerDto> fehler) {
		showBelegPruefergebnis(internalFrame, fehler, "Info");
	}

	public static void showBelegPruefergebnis(InternalFrame internalFrame,
			ArrayList<FibuFehlerDto> fehler, String titel) {
		try {
			DialogFibuFehlerResult dlg = new DialogFibuFehlerResult(LPMain
					.getInstance().getDesktop(), titel, false, fehler);
			dlg.setVisible(true);
		} catch (Throwable t) {
			System.out.println("");
		}
	}

	public static void showBelegPruefergebnisOrig(InternalFrame internalFrame,
			ArrayList<FibuFehlerDto> fehler, String titel) {
		StringBuffer msg = new StringBuffer();
		String s = getBelegPruefungBelege(LocaleFac.BELEGART_RECHNUNG,
				FibuFehlerDto.FEHLER_STATUS, fehler);
		if (s != null && s.length() > 0) {
			msg.append(LPMain.getTextRespectUISPr("fb.fehler.rechnungstatus"));
			msg.append("\nRE: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_RECHNUNG,
				FibuFehlerDto.FEHLER_NICHT_IN_FIBU, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain.getTextRespectUISPr("fb.fehler.rechnungfibu"));
			msg.append("\n AR: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_RECHNUNG,
				FibuFehlerDto.FEHLER_KURS, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain.getTextRespectUISPr("fb.fehler.rechnungkurs"));
			msg.append("\n AR: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_REZAHLUNG,
				FibuFehlerDto.FEHLER_NICHT_IN_FIBU, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain.getTextRespectUISPr("fb.fehler.zahlungfibu"));
			msg.append("\n AR: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_REZAHLUNG,
				FibuFehlerDto.FEHLER_KURS, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain.getTextRespectUISPr("fb.fehler.zahlungkurs"));
			msg.append("\n AR: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_EINGANGSRECHNUNG,
				FibuFehlerDto.FEHLER_NICHT_VOLLSTAENDIG_KONTIERT, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain
					.getTextRespectUISPr("fb.fehler.eingangsrechnungkontiert"));
			msg.append("\n ER: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_EINGANGSRECHNUNG,
				FibuFehlerDto.FEHLER_NICHT_IN_FIBU, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain
					.getTextRespectUISPr("fb.fehler.eingangsrechnungfibu"));
			msg.append("\n ER: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_EINGANGSRECHNUNG,
				FibuFehlerDto.FEHLER_KURS, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain.getTextRespectUISPr("fb.fehler.rechnungkurs"));
			msg.append("\n ER: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_ERZAHLUNG,
				FibuFehlerDto.FEHLER_NICHT_IN_FIBU, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain
					.getTextRespectUISPr("fb.fehler.eingangsrechnungzahlungfibu"));
			msg.append("\n ER: ");
			msg.append(s);
		}

		s = getBelegPruefungBelege(LocaleFac.BELEGART_ERZAHLUNG,
				FibuFehlerDto.FEHLER_KURS, fehler);
		if (s != null && s.length() > 0) {
			if (msg.length() > 0)
				msg.append("\n");
			msg.append(LPMain.getTextRespectUISPr("fb.fehler.zahlungkurs"));
			msg.append("\n ER: ");
			msg.append(s);
		}

		if (msg.length() > 0)
			JOptionPane.showMessageDialog(internalFrame, msg, titel,
					JOptionPane.OK_OPTION);
	}

	private static String getBelegPruefungBelege(String belegartCNr,
			int fehlercode, ArrayList<FibuFehlerDto> fehler) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fehler.size(); i++) {
			if (fehler.get(i).getBelegartCNr().equals(belegartCNr)) {
				if (fehler.get(i).getFehlercode() == fehlercode) {
					if ((i + 1) % 12 == 0)
						sb.append("\n");
					else if (sb.length() > 0)
						sb.append(", ");
					sb.append(fehler.get(i).getBelegCNr());
				}
			}
		}
		return new String(sb);
	}
}
