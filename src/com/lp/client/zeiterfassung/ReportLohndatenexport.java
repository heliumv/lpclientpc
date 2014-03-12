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
package com.lp.client.zeiterfassung;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PersonalDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class ReportLohndatenexport extends ReportZeiterfassung implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaJahr = new WrapperLabel();
	private WrapperSpinner wspJahr = new WrapperSpinner(new Integer(0),
			new Integer(0), new Integer(9999), new Integer(1));
	private WrapperLabel wlaMonat = new WrapperLabel();
	private WrapperComboBox wcoMonat = new WrapperComboBox();
	JasperPrintLP jasperPrint = null;

	private String ACTION_EXPORT = "ACTION_EXPORT";

	private WrapperButton wbuExport = new WrapperButton();

	private WrapperCheckBox wcbBisHeute = new WrapperCheckBox();

	private String exportprogram = null;

	public ReportLohndatenexport(InternalFrameZeiterfassung internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, internalFrame.getPersonalDto().getIId(),
				add2Title);
		jbInit();
		initComponents();

		DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getInstance()
				.getUISprLocale());
		String[] defaultMonths = symbols.getMonths();
		Map<Integer, String> m = new TreeMap<Integer, String>();
		for (int i = 0; i < defaultMonths.length - 1; i++) {
			m.put(new Integer(i), defaultMonths[i]);

		}
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		cal.set(Calendar.MONTH, month - 1);
		month = cal.get(Calendar.MONTH);
		wcoMonat.setMap(m);
		wcoMonat.setSelectedIndex(month);
		wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wspJahr;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource() != null && e.getSource().equals(wbuExport)) {

			exportiereReportNachZielprogramm();

		} else {
			if (wcbBisHeute.isSelected()) {
				wcoMonat.setEnabled(false);
				wcoMonat.setKeyOfSelectedItem(new Integer(Calendar
						.getInstance().get(Calendar.MONTH)));
				wspJahr.setValue(new Integer(Calendar.getInstance().get(
						Calendar.YEAR)));
			} else {
				wcoMonat.setEnabled(true);
			}
		}
	}

	private void jbInit() throws Throwable {
		wlaJahr.setText(LPMain.getTextRespectUISPr("lp.jahr"));
		wlaMonat.setText(LPMain.getTextRespectUISPr("lp.monat1"));
		wcoMonat.setMandatoryField(true);

		wcbBisHeute.setText(LPMain
				.getTextRespectUISPr("pers.monatsabrechnung.nurbisheute"));
		wcbBisHeute.addActionListener(this);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_ZIELPROGRAMM,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		exportprogram = parameter.getCWert();

		wbuExport.setText(parameter.getCWert() + "-"
				+ LPMain.getTextRespectUISPr("pers.zeitdaten.export"));

		wbuExport.setActionCommand(ACTION_EXPORT);
		wbuExport.addActionListener(this);
		wbuExport.setEnabled(false);

		wrbSortKostenstelleNameVorname.setVisible(false);
		wrbSortAbteilungKostenstelleNameVorname.setVisible(false);

		jpaWorkingOn.add(wlaJahr, "span, split, growx 50");
		jpaWorkingOn.add(wspJahr, "growx");
		jpaWorkingOn.add(wlaMonat, "growx 50");
		jpaWorkingOn.add(wcoMonat, "growx");
		jpaWorkingOn.add(wcbBisHeute, "growx, wrap");

		jpaWorkingOn.add(wbuExport, "cell 3 4");

	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungFac.REPORT_MONATSABRECHNUNG;
	}

	private void exportiereReportNachZielprogramm() throws Throwable {
		byte[] CRLFAscii = { 13, 10 };
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_FIRMENNUMMER,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());
		String firmennummer = parameter.getCWert();

		String s = "";
		Object[][] daten = jasperPrint.getDatenMitUeberschrift();

		if (daten.length > 0) {

			int iJahr = (Integer) wspJahr.getValue();
			int iMonat = (Integer) wcoMonat.getKeyOfSelectedItem();

			int iVersatz = 0;

			try {
				parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_PERIODENVERSATZ,
								ParameterFac.KATEGORIE_PERSONAL,
								LPMain.getTheClient().getMandant());

				iVersatz = ((Integer) parameter.getCWertAsObject()).intValue();

			} catch (RemoteException ex5) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
			}
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, (Integer) wspJahr.getValue());
			c.set(Calendar.MONTH, (Integer) wcoMonat.getKeyOfSelectedItem());

			if (iVersatz > 0) {

				c.set(Calendar.MONTH, (Integer) wcoMonat.getKeyOfSelectedItem()
						+ iVersatz);

			}

			iJahr = c.get(Calendar.YEAR);
			iMonat = c.get(Calendar.MONTH);

			int spaltePersonalNummer = 0;
			int spalteLohnart = 0;
			int spalteStunden = 0;

			for (int i = 0; i < daten[0].length; i++) {

				String ueberschrift = (String) daten[0][i];

				if (ueberschrift.equals("Personalnummer")) {
					spaltePersonalNummer = i;
				}
				if (ueberschrift.equals("Lohnart_Nr")) {
					spalteLohnart = i;
				}
				if (ueberschrift.equals("Stunden")) {
					spalteStunden = i;
				}

			}
			if (exportprogram.equals("VARIAL")) {
				String sLetztePersonalNummerLohnart = "";

				BigDecimal stunden = new BigDecimal(0);

				boolean immer = true;
				int i = 1;
				while (immer == true && daten.length > 1) {

					String aktuellePeronalLohnart = daten[i][spaltePersonalNummer]
							+ " " + daten[i][spalteLohnart];

					if (!aktuellePeronalLohnart
							.equals(sLetztePersonalNummerLohnart)
							|| i >= daten.length - 1) {
						if (i > 1) {

							String sZeile = "";
							sZeile += Helper.fitString2Length(firmennummer, 6,
									' ');

							sZeile += Helper.fitString2Length(iJahr + "", 4,
									' ');
							sZeile += Helper.fitString2LengthAlignRight(
									(iMonat + 1) + "", 2, '0');
							sZeile += Helper.fitString2LengthAlignRight(
									daten[i - 1][spaltePersonalNummer] + "", 7,
									'0');
							sZeile += Helper.fitString2LengthAlignRight("", 6,
									'0');
							sZeile += Helper.fitString2LengthAlignRight(
									daten[i - 1][spalteLohnart] + "", 3, '0');

							// Leezeichen
							sZeile += "                                                                                                       ";

							stunden = Helper.rundeKaufmaennisch(stunden, 2)
									.multiply(new BigDecimal(100));

							sZeile += Helper.fitString2LengthAlignRight(
									stunden.intValue() + "", 9, '0');
							sZeile += "+";

							sZeile = Helper.fitString2Length(sZeile, 170, ' ');

							sZeile += new String(CRLFAscii);

							if (stunden.doubleValue() != 0) {

								s += sZeile;
							}
						}
						stunden = new BigDecimal(0);
						if (i >= daten.length - 1) {
							break;
						}

					}
					stunden = stunden.add((BigDecimal) daten[i][spalteStunden]);
					sLetztePersonalNummerLohnart = aktuellePeronalLohnart;
					i++;
				}
			} else if (exportprogram.equals("EGECKO")) {

				c.set(Calendar.DAY_OF_MONTH,
						c.getActualMaximum(Calendar.DAY_OF_MONTH));

				DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy");
				String datum = dfmt.format(c.getTime());

				// Zuerst nach Person und Lohnart verdichten
				TreeMap<String, TreeMap<Integer, BigDecimal>> tmPersonen = new TreeMap<String, TreeMap<Integer, BigDecimal>>();

				for (int i = 1; i < daten.length; i++) {
					String persnr = (String) daten[i][spaltePersonalNummer];
					Integer lohnart = (Integer) daten[i][spalteLohnart];

					TreeMap<Integer, BigDecimal> tmLohnarten = new TreeMap<Integer, BigDecimal>();

					if (tmPersonen.containsKey(persnr)) {
						tmLohnarten = tmPersonen.get(persnr);
					}

					BigDecimal stundenLohnart = new BigDecimal(0);
					if (tmLohnarten.containsKey(lohnart)) {
						stundenLohnart = tmLohnarten.get(lohnart);
					}

					stundenLohnart = stundenLohnart
							.add((BigDecimal) daten[i][spalteStunden]);
					tmLohnarten.put(lohnart, stundenLohnart);
					tmPersonen.put(persnr, tmLohnarten);

				}

				// Nun exportieren

				Iterator<String> it = tmPersonen.keySet().iterator();
				while (it.hasNext()) {
					String persNr = it.next();

					TreeMap<Integer, BigDecimal> tmLohnarten = tmPersonen
							.get(persNr);
					Iterator<Integer> itLohn = tmLohnarten.keySet().iterator();
					while (itLohn.hasNext()) {
						Integer lohnart = itLohn.next();

						String sZeile = firmennummer + "^";// Firmennummer
						sZeile += persNr + "^";// Personalnummer
						sZeile += datum + "^";// Abrechnungsperiode
						sZeile += lohnart + "^";// Lohnart
						sZeile += datum + "^";// Datum
						sZeile += "^^^^^^^";// PLATZHALTER

						BigDecimal bdStunden = tmLohnarten.get(lohnart);
						DecimalFormat df = new DecimalFormat("0.00");
						df.format(bdStunden.doubleValue());

						sZeile += "+"
								+ df.format(bdStunden.doubleValue())
										.replaceAll(",", ".") + "^";// Stunden
						sZeile += "^^^";// Platzhalter
						sZeile += new String(CRLFAscii);

						s += sZeile;

					}

				}

			}

			parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_ZIEL,
							ParameterFac.KATEGORIE_PERSONAL,
							LPMain.getTheClient().getMandant());
			String dateiname = parameter.getCWert();

			java.io.File ausgabedatei = new java.io.File(dateiname);

			boolean b = true;

			if (ausgabedatei.exists()) {
				// Hinweis
				b = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("pers.lohndatenexport.fileexists"));
			}

			if (b == true) {

				try {
					java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
					java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
					bw.write(s);
					bw.close();

					// SP1604
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("pers.lohndatenexport.datenexportiert")
											+ " " + ausgabedatei);
				} catch (Exception e) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							e.getMessage());
				}
			}

		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		try {

			Integer iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER;
			if (wrbSortAbteilungNameVorname.isSelected()) {
				iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_NAME_VORNAME;
			} else if (wrbSortNameVorname.isSelected()) {
				iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_NAME_VORNAME;
			}

			if (wcbBisHeute.isSelected()) {
				jasperPrint = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.printLohndatenexport(getPersonalIId(),
								(Integer) wspJahr.getValue(),
								(Integer) wcoMonat.getKeyOfSelectedItem(),
								false,
								new java.sql.Date(System.currentTimeMillis()),
								getPersonAuswahl(), iSortierung,
								mitVersteckten());
			} else {
				jasperPrint = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.printLohndatenexport(getPersonalIId(),
								(Integer) wspJahr.getValue(),
								(Integer) wcoMonat.getKeyOfSelectedItem(),
								true, null, getPersonAuswahl(), iSortierung,
								mitVersteckten());
			}

			wbuExport.setEnabled(true);

		} catch (ExceptionLP ex) {

			if (ex.getICode() == com.lp.util.EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM) {
				ArrayList<?> al = ex.getAlInfoForTheClient();
				String sZusatz = null;
				if (al.get(0) instanceof Integer) {

					Integer personalIId = (Integer) al.get(0);

					try {
						sZusatz = new PersonalDelegate()
								.personalFindByPrimaryKey(personalIId)
								.formatAnrede();
					} catch (ExceptionLP ex1) {
						handleException(ex1, false);
					}
				}

				DialogFactory.showModalDialog("Fehler", sZusatz
						+ " hat kein g\u00FCltiges Eintrittsdatum");
			} else {
				// brauche ich
				handleException(ex, false);
			}
		}
		return jasperPrint;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
