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

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck des Fertigungsbegleitscheins
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 19.10.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/12/17 08:43:01 $
 */
public class ReportFertigungsbegleitschein extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer formularnummer = null;

	private Integer losIId = null;

	public ReportFertigungsbegleitschein(InternalFrame internalFrame,
			Integer losIId, String sAdd2Title) throws Throwable {
		// reporttitel: das PanelReport kriegt einen Titel, der wird vom
		// Framework hergenommen
		super(internalFrame, sAdd2Title);
		this.losIId = losIId;

		LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
				.losFindByPrimaryKey(losIId);
		formularnummer = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.fertigungsgruppeFindByPrimaryKey(
						losDto.getFertigungsgruppeIId()).getIFormularnummer();

		this.setVisible(false);
	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		String report = FertigungReportFac.REPORT_FERTIGUNGSBEGLEITSCHEIN;
		if (formularnummer != null) {
			report = report.replace(".", formularnummer + ".");
		}
		return report;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory.getInstance().getFertigungDelegate()
				.printFertigungsbegleitschein(losIId,false);
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}
}