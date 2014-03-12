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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportEtikett;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportEinkaufsangebot extends ReportEtikett implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// protected JPanel jpaWorkingOn = new JPanel();
	// private GridBagLayout gridBagLayout1 = new GridBagLayout();
	// private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private Integer einkaufsangebotIId = null;

	private WrapperComboBox wcoSortierung1 = new WrapperComboBox();

	private static int SORT_ARTIKELNUMMER = 0;
	private static int SORT_POSITION = 1;
	private static int SORT_BEMERKUNG = 2;
	private static int SORT_SORT = 3;

	public ReportEinkaufsangebot(InternalFrameAngebotstkl internalFrame,
			String add2Title, Integer einkaufsangebotIId) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		initComponents();
		this.einkaufsangebotIId = einkaufsangebotIId;
	}

	private void jbInit() throws Exception {

		
		wcoSortierung1.setMandatoryField(true);
		
		Map<Integer, String> mSortierung = new LinkedHashMap<Integer, String>();
		mSortierung.put(new Integer(SORT_ARTIKELNUMMER), "Artikelnummer");
		mSortierung.put(new Integer(SORT_POSITION), "Position");
		mSortierung.put(new Integer(SORT_BEMERKUNG), "Bemerkung");
		mSortierung.put(new Integer(SORT_SORT), "Sortierung");
		wcoSortierung1.setMap(mSortierung);
		

		// /this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
		// GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
		// 0, 0, 0), 0, 0));

		iZeile++;
		WrapperLabel wlaSortierung = new WrapperLabel(LPMain
				.getTextRespectUISPr("lp.sortierung")
				+ ":");
		wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoSortierung1, new GridBagConstraints(1, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

	}

	public String getModul() {
		return AngebotstklFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AngebotstklFac.REPORT_EINKAUFSANGEBOT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iSortierung = -1;

		if ((Integer) wcoSortierung1.getKeyOfSelectedItem() == SORT_ARTIKELNUMMER) {
			iSortierung = AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_ARTIKELNR;
		} else if ((Integer) wcoSortierung1.getKeyOfSelectedItem() == SORT_BEMERKUNG) {
			iSortierung = AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_BEMERKUNG;
		} else if ((Integer) wcoSortierung1.getKeyOfSelectedItem() == SORT_POSITION) {
			iSortierung = AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_POSITION;
		} else if ((Integer) wcoSortierung1.getKeyOfSelectedItem() == SORT_SORT) {
			iSortierung = AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_SORT;
		}

		return DelegateFactory.getInstance().getAngebotstklDelegate()
				.printEinkaufsangebot(einkaufsangebotIId, iSortierung);
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
