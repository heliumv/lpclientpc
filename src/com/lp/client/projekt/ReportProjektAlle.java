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
package com.lp.client.projekt;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Date;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalPersonal;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportProjektAlle extends PanelReportJournalPersonal implements
		PanelReportIfJRDS {

	private WrapperCheckBox wcbBelegdatumStattZieltermin = new WrapperCheckBox();

	private static final long serialVersionUID = 1L;
	Integer bereichIId = null;

	public ReportProjektAlle(InternalFrameProjekt internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		bereichIId = internalFrame.getTabbedPaneProjekt().getBereichIId();

	}

	private void jbInit() throws Exception {
		wrbSortierungPartner.setVisible(false);
		wrbPartnerAlle.setVisible(false);
		wrbPartnerEiner.setVisible(false);

		wcbBelegdatumStattZieltermin.setText(LPMain.getTextRespectUISPr(
		        "proj.journal.alle.belegdatumstattzieltermin"));
		
		
		iZeile++;
		jpaWorkingOn.add(wcbBelegdatumStattZieltermin, new GridBagConstraints(
				1, iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return ProjektReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Date datedStichtag = new Date(System.currentTimeMillis());
		return DelegateFactory
				.getInstance()
				.getProjektDelegate()
				.printProjektAlle(getKriterien(), bereichIId, datedStichtag,
						wcbBelegdatumStattZieltermin.isSelected());

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}
}
