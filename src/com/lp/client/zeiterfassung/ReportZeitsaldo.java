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
package com.lp.client.zeiterfassung;

import java.awt.event.ActionEvent;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;

import com.lp.client.frame.ExceptionLP;
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
import com.lp.server.util.report.JasperPrintLP;

public class ReportZeitsaldo extends ReportZeiterfassung implements
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

	private WrapperLabel wlaJahrBis = new WrapperLabel();
	private WrapperSpinner wspJahrBis = new WrapperSpinner(new Integer(0),
			new Integer(0), new Integer(9999), new Integer(1));
	private WrapperLabel wlaMonatBis = new WrapperLabel();
	private WrapperComboBox wcoMonatBis = new WrapperComboBox();

	private WrapperCheckBox wcbBisHeute = new WrapperCheckBox();
	private WrapperCheckBox wcbBisGestern = new WrapperCheckBox();

	public ReportZeitsaldo(InternalFrameZeiterfassung internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, internalFrame.getPersonalDto().getIId(), add2Title);
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
		wcoMonatBis.setMap(m);
		wspJahrBis.setValue(new Integer(cal.get(Calendar.YEAR)));

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wspJahr;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (wcbBisHeute.isSelected() && e.getSource().equals(wcbBisHeute)) {
			wcbBisGestern.setSelected(false);
			wcoMonat.setEnabled(false);
			wcoMonatBis.setEnabled(false);
			wspJahrBis.setEnabled(false);
			wcoMonatBis.setKeyOfSelectedItem(null);

			wcoMonat.setKeyOfSelectedItem(new Integer(Calendar.getInstance()
					.get(Calendar.MONTH)));
			wspJahr.setValue(new Integer(Calendar.getInstance().get(
					Calendar.YEAR)));
		} else if (wcbBisGestern.isSelected()
				&& e.getSource().equals(wcbBisGestern)) {
			wcbBisHeute.setSelected(false);
			wcoMonat.setEnabled(false);
			wcoMonatBis.setEnabled(false);
			wspJahrBis.setEnabled(false);
			wcoMonatBis.setKeyOfSelectedItem(null);

			wcoMonat.setKeyOfSelectedItem(new Integer(Calendar.getInstance()
					.get(Calendar.MONTH)));
			wspJahr.setValue(new Integer(Calendar.getInstance().get(
					Calendar.YEAR)));
		} else {
			wcoMonat.setEnabled(true);
			wcoMonatBis.setEnabled(true);
			wspJahrBis.setEnabled(true);
		}
	}

	private void jbInit() throws Throwable {
		wlaJahr.setText(LPMain.getTextRespectUISPr("lp.jahr"));
		wlaMonat.setText(LPMain.getTextRespectUISPr("lp.monat1"));
		wcoMonat.setMandatoryField(true);

		wlaJahrBis.setText(LPMain.getTextRespectUISPr("lp.bis") + " "
				+ LPMain.getTextRespectUISPr("lp.jahr"));
		wlaMonatBis.setText(LPMain.getTextRespectUISPr("lp.monat1"));

		wcbBisHeute.setText(LPMain
				.getTextRespectUISPr("pers.monatsabrechnung.nurbisheute"));
		wcbBisHeute.addActionListener(this);

		wcbBisGestern.setText(LPMain
				.getTextRespectUISPr("pers.monatsabrechnung.nurbisgestern"));
		wcbBisGestern.addActionListener(this);

		wrbSortPersonalnummer.setSelected(true);

		jpaWorkingOn.add(wlaJahr, "span, split, growx 30");
		jpaWorkingOn.add(wspJahr, "growx");
		jpaWorkingOn.add(wlaMonat, "growx 30");
		jpaWorkingOn.add(wcoMonat, "growx");
		jpaWorkingOn.add(wcbBisHeute, "growx, wrap");

		jpaWorkingOn.add(wlaJahrBis, "span, split, growx 30");
		jpaWorkingOn.add(wspJahrBis, "growx");
		jpaWorkingOn.add(wlaMonatBis, "growx 30");
		jpaWorkingOn.add(wcoMonatBis, "growx");
		jpaWorkingOn.add(wcbBisGestern, "growx");

	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungFac.REPORT_ZEITSALDO;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		Integer iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER;
		if (wrbSortAbteilungNameVorname.isSelected()) {
			iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_NAME_VORNAME;
		} else if (wrbSortNameVorname.isSelected()) {
			iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_NAME_VORNAME;
		} else if (wrbSortKostenstelleNameVorname.isSelected()) {
			iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME;
		} else if (wrbSortAbteilungKostenstelleNameVorname.isSelected()) {
			iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME;
		}

		// PersonalDto[] dtos = null;
		try {

			if (wcbBisGestern.isSelected()) {
				java.sql.Timestamp ts = new java.sql.Timestamp(
						System.currentTimeMillis() - 3600000 * 24);
				ts = com.lp.util.Helper.cutTimestamp(ts);

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(ts.getTime());
				jasperPrint = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.printZeitsaldo(getPersonalIId(),
								(Integer) wspJahr.getValue(),
								(Integer) wcoMonat.getKeyOfSelectedItem(),
								(Integer) wspJahrBis.getValue(),
								(Integer) wcoMonatBis.getKeyOfSelectedItem(),
								getPersonAuswahl(), iSortierung,
								mitVersteckten(), false,
								new java.sql.Date(ts.getTime()), nurAnwesende());
			} else {
				jasperPrint = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.printZeitsaldo(getPersonalIId(),
								(Integer) wspJahr.getValue(),
								(Integer) wcoMonat.getKeyOfSelectedItem(),
								(Integer) wspJahrBis.getValue(),
								(Integer) wcoMonatBis.getKeyOfSelectedItem(),
								getPersonAuswahl(), iSortierung,
								mitVersteckten(), !wcbBisHeute.isSelected(),
								new java.sql.Date(System.currentTimeMillis()),
								nurAnwesende());
			}

			// if (dtos != null && dtos.length > 0) {
			// String s =
			// "Folgende Personen haben kein Eintrittsdatum eingetragen:\n";
			// for (int i = 0; i < dtos.length; i++) {
			// s = s + dtos[i].formatAnrede() + "\n";
			// }
			// DialogFactory.showModalDialog(LPMain
			// .getTextRespectUISPr("lp.error"), s);
			// }
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
