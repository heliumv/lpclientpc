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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich um den Druck der Ausgabe Liste</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 19.10.05</p>
 * 
 * <p>@author $Author: robert $</p>
 * 
 * @version not attributable Date $Date: 2012/10/19 13:19:03 $
 */
public class ReportAblieferungsstatistik extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaVon = null;
	private WrapperLabel wlaBis = null;
	protected WrapperDateField wdfVon = null;
	protected WrapperDateField wdfBis = null;
	private WrapperCheckBox wcbNurEineIdent = null;
	private Border border = null;
	protected JPanel jpaWorkingOn = null;
	private WrapperDateRangeController wdrBereich = null;

	private WrapperLabel wlaSortierung = new WrapperLabel();

	private ButtonGroup buttonGroupOption = new ButtonGroup();
	private WrapperRadioButton wrbAblieferdatum = new WrapperRadioButton();
	private WrapperRadioButton wrbArtikel = new WrapperRadioButton();

	private StuecklisteDto stuecklisteDto = null;

	public ReportAblieferungsstatistik(InternalFrame internalFrame,
			StuecklisteDto stuecklisteDto, String sAdd2Title) throws Throwable {
		// reporttitel: das PanelReport kriegt einen Titel, der wird vom
		// Framework hergenommen
		super(internalFrame, sAdd2Title);
		this.stuecklisteDto = stuecklisteDto;
		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 */
	private void jbInit() {
		jpaWorkingOn = new JPanel(new GridBagLayout());
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		wlaVon = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.von"));
		wlaBis = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.bis"));
		wdfVon = new WrapperDateField();
		wdfBis = new WrapperDateField();
		wcbNurEineIdent = new WrapperCheckBox();
		if (stuecklisteDto != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(LPMain.getTextRespectUISPr("fert.nurartikel") + " ");
			sb.append(stuecklisteDto.getArtikelDto().getCNr());
			if (stuecklisteDto.getArtikelDto().getArtikelsprDto().getCBez() != null) {
				sb.append(" "
						+ stuecklisteDto.getArtikelDto().getArtikelsprDto()
								.getCBez());
			}
			wcbNurEineIdent.setText(sb.toString());
		}
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		
		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));
		wrbAblieferdatum.setText(LPMain.getTextRespectUISPr("fert.ablieferdatum"));
		wrbArtikel.setText(LPMain.getTextRespectUISPr("lp.artikel"));
		
		
		buttonGroupOption.add(wrbAblieferdatum);
		buttonGroupOption.add(wrbArtikel);
		wrbAblieferdatum.setSelected(true);

		wlaVon.setMinimumSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaVon.setPreferredSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaBis.setMinimumSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaBis.setPreferredSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));

		jpaWorkingOn.setBorder(border);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		if (stuecklisteDto != null) {
			iZeile++;
			jpaWorkingOn.add(wcbNurEineIdent, new GridBagConstraints(0, iZeile,
					4, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAblieferdatum, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wrbArtikel, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_ABLIEFERUNGSSTATISTIK;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Integer artikelIId = null;
		if (wcbNurEineIdent.isSelected()) {
			artikelIId = stuecklisteDto.getArtikelDto().getIId();
		}
		return DelegateFactory.getInstance().getFertigungDelegate()
				.printAblieferungsstatistik(wdfVon.getDate(), wdfBis.getDate(),
						artikelIId,wrbArtikel.isSelected(), false);
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}
}
