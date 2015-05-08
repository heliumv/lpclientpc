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
import java.sql.Timestamp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportZeiterfassungZeitdaten extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperLabel wlaZeitraum = new WrapperLabel();
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();
	private WrapperLabel wbuPersonal = new WrapperLabel();
	private JButton wbuGestern = null;
	private WrapperLabel wlaAuswahl = new WrapperLabel();
	private WrapperTextField wtfPersonal = new WrapperTextField();
	private WrapperCheckBox wcbPersonalAlle = new WrapperCheckBox();
	private Integer personalIId = null;
	private WrapperDateRangeController wdrBereich = null;

	public ReportZeiterfassungZeitdaten(
			InternalFrameZeiterfassung internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr(
				"zeiterfassung.report.zeitdatenjournal");

		jbInit();
		initComponents();
		if (internalFrame.getPersonalDto() != null) {
			wtfPersonal.setText(internalFrame.getPersonalDto().formatAnrede());
			personalIId = internalFrame.getPersonalDto().getIId();
		}
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Integer iPersonal = personalIId;
		if (wcbPersonalAlle.isSelected()) {
			iPersonal = null;
		}

		return DelegateFactory
				.getInstance()
				.getZeiterfassungReportDelegate()
				.printZeitdatenjournal(iPersonal, wdfVon.getTimestamp(),
						wdfBis.getTimestamp());
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaZeitraum.setText(LPMain.getTextRespectUISPr("lp.zeitraum"));
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		
		

		wbuGestern = new JButton(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/table_selection_cell.png")));
		wbuGestern
				.setToolTipText(LPMain
						.getTextRespectUISPr("pers.zeiterfassung.produktivitaetsstatistik.datummitgesternvorbesetzen"));
		wbuGestern.addActionListener(this);

		
		wbuPersonal
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.selektierteperson")
						+ ": ");
		wlaAuswahl
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.auswahlpersonal")
						+ ":");
		wcbPersonalAlle.setText(LPMain.getTextRespectUISPr("lp.alle"));
		wtfPersonal.setActivatable(false);
		wtfPersonal.setEditable(false);
		wtfPersonal.setSaveReportInformation(false);
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(2, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(3, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(4, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(5, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuGestern, new GridBagConstraints(6, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), -15, 0));
		
		jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, 4, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE)) {
			jpaWorkingOn.add(wcbPersonalAlle,
					new GridBagConstraints(2, 3, 5, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(2, 2, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAuswahl, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuPersonal, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungReportFac.REPORT_ZEITDATEN;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wbuGestern)) {
			wdfVon.setTimestamp(Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis() - 24 * 3600000)));
			wdfBis.setTimestamp(Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis() - 24 * 3600000)));
		}
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
