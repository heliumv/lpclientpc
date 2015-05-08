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

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;

import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportMitarbeiteruebersicht extends ReportZeiterfassung implements
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

	public ReportMitarbeiteruebersicht(
			InternalFrameZeiterfassung internalFrame, String add2Title)
			throws Throwable {
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
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, month + 1);
		month = cal.get(Calendar.MONTH);
		wcoMonat.setMap(m);
		wcoMonatBis.setMap(m);
		wcoMonat.setSelectedIndex(month);
		wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));

		cal.set(Calendar.MONTH, month + 1);

		wcoMonatBis.setSelectedIndex(cal.get(Calendar.MONTH));
		wspJahrBis.setValue(new Integer(cal.get(Calendar.YEAR)));

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wspJahr;
	}

	private void jbInit() throws Throwable {
		wlaJahr.setText(LPMain.getTextRespectUISPr("lp.von")
				+ " " + LPMain.getTextRespectUISPr("lp.jahr"));
		wlaMonat.setText(LPMain.getTextRespectUISPr("lp.monat1"));

		wlaJahrBis.setText(LPMain.getTextRespectUISPr("lp.bis")
				+ " " + LPMain.getTextRespectUISPr("lp.jahr"));
		wlaMonatBis.setText(LPMain.getTextRespectUISPr(
				"lp.monat1"));

		wcoMonat.setMandatoryField(true);
		wcoMonatBis.setMandatoryField(true);

		jpaWorkingOn.add(wlaJahr, "cell 0 5, span 3, split 4, growx 50");
		jpaWorkingOn.add(wspJahr, "growx");
		jpaWorkingOn.add(wlaMonat, "growx 50");
		jpaWorkingOn.add(wcoMonat, "growx");
		
		jpaWorkingOn.add(wlaJahrBis, "newline, span 3, split 4, growx 50");
		jpaWorkingOn.add(wspJahrBis, "growx");
		jpaWorkingOn.add(wlaMonatBis, "growx 50");
		jpaWorkingOn.add(wcoMonatBis, "growx");
	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungFac.REPORT_MONATSABRECHNUNG;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

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

		return DelegateFactory
				.getInstance()
				.getZeiterfassungReportDelegate()
				.printMitarbeiteruebersicht(getPersonalIId(),
						(Integer) wspJahr.getValue(),
						(Integer) wcoMonat.getKeyOfSelectedItem(),
						(Integer) wspJahrBis.getValue(),
						(Integer) wcoMonatBis.getKeyOfSelectedItem(),
						getPersonAuswahl(), iSortierung, mitVersteckten(), nurAnwesende());

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
