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
import com.lp.client.frame.delegate.ZeiterfassungDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.report.JasperPrintLP;

public class ReportMonatsabrechnung extends ReportZeiterfassung implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	private WrapperLabel wlaJahr = new WrapperLabel();
	private WrapperSpinner wspJahr = new WrapperSpinner(new Integer(0),
			new Integer(0), new Integer(9999), new Integer(1));
	private WrapperLabel wlaMonat = new WrapperLabel();
	private WrapperComboBox wcoMonat = new WrapperComboBox();

	private WrapperCheckBox wcbBisHeute = new WrapperCheckBox();
	private WrapperCheckBox wcbBisGestern = new WrapperCheckBox();

	public ReportMonatsabrechnung(InternalFrameZeiterfassung internalFrame,
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

		// SP1703

		ArbeitsplatzparameterDto zoomDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.holeArbeitsplatzparameter(
						ParameterFac.ARBEITSPLATZPARAMETER_MONATSABRECHNUNG_NEUES_MONAT_AB);

		if (zoomDto != null) {

			Integer iTag = new Integer(zoomDto.getCWert());

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			if (c.get(Calendar.DAY_OF_MONTH) < iTag.intValue()) {
				cal.set(Calendar.MONTH, month - 1);
			}

		} else {
			cal.set(Calendar.MONTH, month - 1);
		}

		month = cal.get(Calendar.MONTH);
		wcoMonat.setMap(m);
		wcoMonat.setSelectedIndex(month);
		wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));

		myLogger.error("indexMonth: '" + month + "' of '" + m.size()
				+ "' entries.");
		myLogger.error("Calendar: " + cal.toString() + "'");
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wspJahr;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (wcbBisHeute.isSelected() && e.getSource().equals(wcbBisHeute)) {
			wcbBisGestern.setSelected(false);
			wcoMonat.setEnabled(false);
			wcoMonat.setKeyOfSelectedItem(new Integer(Calendar.getInstance()
					.get(Calendar.MONTH)));
			wspJahr.setValue(new Integer(Calendar.getInstance().get(
					Calendar.YEAR)));
		} else if (wcbBisGestern.isSelected()
				&& e.getSource().equals(wcbBisGestern)) {
			wcbBisHeute.setSelected(false);
			wcoMonat.setEnabled(false);
			wcoMonat.setKeyOfSelectedItem(new Integer(Calendar.getInstance()
					.get(Calendar.MONTH)));
			wspJahr.setValue(new Integer(Calendar.getInstance().get(
					Calendar.YEAR)));
		} else {
			wcoMonat.setEnabled(true);
		}
	}

	private void jbInit() throws Throwable {
		wlaJahr.setText(LPMain.getTextRespectUISPr("lp.jahr"));
		wlaMonat.setText(LPMain.getTextRespectUISPr("lp.monat1"));
		wcoMonat.setMandatoryField(true);

		wcbBisHeute.setText(LPMain
				.getTextRespectUISPr("pers.monatsabrechnung.nurbisheute"));
		wcbBisHeute.addActionListener(this);
		wcbBisGestern.setText(LPMain
				.getTextRespectUISPr("pers.monatsabrechnung.nurbisgestern"));
		wcbBisGestern.addActionListener(this);

		jpaWorkingOn.add(wlaJahr, "span, split, growx 50");
		jpaWorkingOn.add(wspJahr, "growx");
		jpaWorkingOn.add(wlaMonat, "growx 50");
		jpaWorkingOn.add(wcoMonat, "growx");
		jpaWorkingOn.add(wcbBisHeute, "growx");
		jpaWorkingOn.add(wcbBisGestern, "growx, wrap");
	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungFac.REPORT_MONATSABRECHNUNG;
	}

	public JasperPrintLP getReport(String sDrucktype) {
		JasperPrintLP jasperPrint = null;
		try {

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

			if (wcbBisHeute.isSelected() || wcbBisGestern.isSelected()) {

				if (wcbBisGestern.isSelected()) {
					java.sql.Timestamp ts = new java.sql.Timestamp(
							System.currentTimeMillis() - 3600000 * 24);
					ts = com.lp.util.Helper.cutTimestamp(ts);

					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(ts.getTime());

					jasperPrint = new ZeiterfassungDelegate()
							.printMonatsabrechnung(getPersonalIId(),
									new Integer(c.get(Calendar.YEAR)),
									new Integer(c.get(Calendar.MONTH)),
									getPersonAuswahl(), iSortierung,
									mitVersteckten(), nurAnwesende(), false, new java.sql.Date(
											ts.getTime()));

				} else {

					jasperPrint = new ZeiterfassungDelegate()
							.printMonatsabrechnung(getPersonalIId(),
									(Integer) wspJahr.getValue(),
									(Integer) wcoMonat.getKeyOfSelectedItem(),
									getPersonAuswahl(), iSortierung,
									mitVersteckten(), nurAnwesende(), false, new java.sql.Date(
											System.currentTimeMillis()));
				}
			} else {

				jasperPrint = new ZeiterfassungDelegate()
						.printMonatsabrechnung(getPersonalIId(),
								(Integer) wspJahr.getValue(),
								(Integer) wcoMonat.getKeyOfSelectedItem(),
								getPersonAuswahl(), iSortierung,
								mitVersteckten(), nurAnwesende(), true, null);

			}

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
