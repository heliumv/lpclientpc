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
package com.lp.client.instandhaltung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.instandhaltung.service.InstandhaltungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/**
 * <p>Report Auftragnachkalkulation.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 12.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class ReportWartungsplan extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperSelectField wsfPersonalgruppe = new WrapperSelectField(
			WrapperSelectField.PERSONALGRUPPE, getInternalFrame(), true);

	private WrapperSelectField wsfKostenstelle = new WrapperSelectField(
			WrapperSelectField.KOSTENSTELLE, getInternalFrame(), true);

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private WrapperRadioButton wrbSortPersonalgruppe = new WrapperRadioButton();
	private WrapperRadioButton wrbSortKostenstelle = new WrapperRadioButton();
	InternalFrameInstandhaltung internalFrame = null;
	private WrapperCheckBox wcbNurSelektierterStandort = new WrapperCheckBox();

	public ReportWartungsplan(InternalFrameInstandhaltung internalFrame,
			String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
		this.internalFrame = internalFrame;
		jbInit();
	}

	public String getModul() {
		return InstandhaltungReportFac.REPORT_MODUL;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.stichtag"));
		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wrbSortKostenstelle.setText(LPMain
				.getTextRespectUISPr("label.kostenstelle"));
		wrbSortPersonalgruppe.setText(LPMain
				.getTextRespectUISPr("pers.personalgruppe"));

		wcbNurSelektierterStandort
				.setText(LPMain
						.getTextRespectUISPr("is.wartungsplan.nurselektierterstandort"));

		wdfBis.setMandatoryField(true);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

		wdfBis.setTimestamp(new Timestamp(c.getTimeInMillis()));
		buttonGroupSortierung.add(wrbSortPersonalgruppe);
		buttonGroupSortierung.add(wrbSortKostenstelle);
		wrbSortPersonalgruppe.setSelected(true);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wsfPersonalgruppe.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						150, 0));
		jpaWorkingOn.add(wsfPersonalgruppe.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wrbSortPersonalgruppe, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorkingOn.add(wsfKostenstelle.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfKostenstelle.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wrbSortKostenstelle, new GridBagConstraints(3, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wcbNurSelektierterStandort, new GridBagConstraints(3, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	}

	public String getReportname() {
		return InstandhaltungReportFac.REPORT_WARTUNGSPLAN;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		Integer iOptionSortierung = InstandhaltungReportFac.REPORT_WARTUNGSPLAN_OPTION_SORTIERUNG_PERSONALGRUPPE;

		if (wrbSortKostenstelle.isSelected()) {
			iOptionSortierung = InstandhaltungReportFac.REPORT_WARTUNGSPLAN_OPTION_SORTIERUNG_KOSTENSTELLE;
		}

		Integer standortIIdSelektiert = null;

		if (wcbNurSelektierterStandort.isSelected()
				&& internalFrame.getTabbedPaneInstandhaltung().getStandortDto() != null) {
			standortIIdSelektiert = internalFrame.getTabbedPaneInstandhaltung()
					.getStandortDto().getIId();
		}

		return DelegateFactory
				.getInstance()
				.getInstandhaltungReportDelegate()
				.printWartungsplan(wdfBis.getTimestamp(),
						wsfPersonalgruppe.getIKey(), wsfKostenstelle.getIKey(),
						iOptionSortierung, standortIIdSelektiert);
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
