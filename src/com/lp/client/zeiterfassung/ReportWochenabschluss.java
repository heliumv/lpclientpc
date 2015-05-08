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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.WochenabschlussReportDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportWochenabschluss extends PanelBasis implements
		PanelReportIfJRDS, PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaDatumKW = new WrapperLabel();

	private WrapperDateField wdfKW = new WrapperDateField();

	private WrapperLabel wlaKW = new WrapperLabel();

	protected JPanel jpaWorkingOn = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JButton wbuKWZurueck = new JButton();
	private JButton wbuNaechsteKW = new JButton();

	private JButton wbuZeitenAbschliessen = new JButton();
	public WrapperCheckBox wcoInVorschauBleiben = new WrapperCheckBox();

	InternalFrameZeiterfassung internalFrameZeiterfassung = null;

	public ReportWochenabschluss(InternalFrameZeiterfassung internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		internalFrameZeiterfassung = internalFrame;
		jbInit();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfKW;
	}

	public void propertyChange(PropertyChangeEvent e) {
		// System.out.println(e.getPropertyName());
		if (e.getSource() == wdfKW.getDisplay()
				&& e.getNewValue() instanceof Date
				&& e.getPropertyName().equals("date")) {
			wbuZeitenAbschliessen.setEnabled(false);
			Date d = (Date) e.getNewValue();

			Timestamp[] tVonBis = Helper
					.getTimestampVonBisEinerKW(new Timestamp(d.getTime()));

			wdfKW.setDate(d);
			//

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(wdfKW.getDate().getTime());
			try {
				wlaKW.setText("KW: "
						+ c.get(Calendar.WEEK_OF_YEAR)
						+ "/"
						+ Helper.berechneJahrDerKW(c)
						+ " ("
						+ Helper.formatDatum(tVonBis[0], LPMain.getTheClient()
								.getLocUi())
						+ "-"
						+ Helper.formatDatum(tVonBis[1], LPMain.getTheClient()
								.getLocUi()) + ")");
			} catch (Throwable e1) {

				e1.printStackTrace();
			}
		}
	}

	private void jbInit() throws Throwable {
		wlaDatumKW.setText(LPMain.getTextRespectUISPr("lp.datum") + ":");

		wbuZeitenAbschliessen
				.setText(LPMain
						.getTextRespectUISPr("pers.report.wochenabschluss.zeitenabschliessen"));
		wbuZeitenAbschliessen.setEnabled(false);
		wbuZeitenAbschliessen.addActionListener(this);

		wbuKWZurueck.setText("<");
		wbuKWZurueck.addActionListener(this);

		wbuNaechsteKW.setText(">");
		wbuNaechsteKW.addActionListener(this);

		wcoInVorschauBleiben
				.setText(LPMain
						.getTextRespectUISPr("pers.wochenabschluss.report.invorschaubleiben"));

		wdfKW.setMandatoryField(true);
		wdfKW.getDisplay().addPropertyChangeListener(this);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -7);
		wdfKW.setDate(c.getTime());

		jpaWorkingOn.setLayout(gridBagLayout1);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaDatumKW, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wdfKW, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuKWZurueck, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 10, 0));
		jpaWorkingOn.add(wlaKW, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wbuNaechsteKW, new GridBagConstraints(4, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 20, 2, 2), 10, 0));
		iZeile++;
		jpaWorkingOn.add(wbuZeitenAbschliessen, new GridBagConstraints(4,
				iZeile, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 20, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoInVorschauBleiben, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wbuKWZurueck)) {
			Calendar c = Calendar.getInstance();
			c.setTime(wdfKW.getDate());
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 7);
			wdfKW.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
		} else if (e.getSource().equals(wbuNaechsteKW)) {
			Calendar c = Calendar.getInstance();
			c.setTime(wdfKW.getDate());
			c.set(Calendar.DATE, c.get(Calendar.DATE) + 7);
			wdfKW.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
		} else if (e.getSource().equals(wbuZeitenAbschliessen)) {
			DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.zeitenAbschliessen(
							internalFrameZeiterfassung.getPersonalDto()
									.getIId(), wdfKW.getTimestamp());

			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("pers.report.wochenabschluss.zeitenabgeschlossen")
									+ wlaKW.getText());
			wbuZeitenAbschliessen.setEnabled(false);
		}

	}

	public String getModul() {
		return ZeiterfassungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungFac.REPORT_WOCHENABSCHLUSS;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		WochenabschlussReportDto wochenabschlussReportDto = null;
		wochenabschlussReportDto = DelegateFactory
				.getInstance()
				.getZeiterfassungDelegate()
				.printWochenabschluss(
						internalFrameZeiterfassung.getPersonalDto().getIId(),
						wdfKW.getTimestamp());
		wbuZeitenAbschliessen.setToolTipText("");
		if (wochenabschlussReportDto.isBFehlerVorhanden() == false) {

			java.sql.Timestamp tLetzterAbschluss = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.gibtEsBereitseinenZeitabschlussBisZurKW(
							internalFrameZeiterfassung.getPersonalDto()
									.getIId(), wdfKW.getTimestamp());

			if (tLetzterAbschluss != null) {
				wbuZeitenAbschliessen.setEnabled(false);

				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("pers.wochenabschluss.fehler.zeitabschlussvorhanden"));
				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { Helper.formatDatum(tLetzterAbschluss,
						LPMain.getTheClient().getLocUi()) };
				String sMsg = mf.format(pattern);

				wbuZeitenAbschliessen.setToolTipText(sMsg);
			} else {
				wbuZeitenAbschliessen.setEnabled(true);
			}

		} else {
			wbuZeitenAbschliessen.setEnabled(false);

			wbuZeitenAbschliessen
					.setToolTipText(LPMain
							.getTextRespectUISPr("pers.wochenabschluss.fehler.vorhanden"));

		}

		return wochenabschlussReportDto.getJasperPrintLP();

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
