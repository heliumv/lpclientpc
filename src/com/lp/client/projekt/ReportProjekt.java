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
package com.lp.client.projekt;

import java.sql.Timestamp;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Report Lieferschein.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 29.06.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.7 $
 */
public class ReportProjekt extends ReportBeleg

{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProjektDto projektDto = null;

	public ReportProjekt(InternalFrame internalFrame, ProjektDto projektDtoI,
			String add2Title) throws Throwable {

		super(internalFrame, null, add2Title, LocaleFac.BELEGART_PROJEKT,
				projektDtoI.getIId(), null);
		projektDto = projektDtoI;
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		// lock ist hier egal
		return null;
	}

	public String getModul() {
		return ProjektReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ProjektReportFac.REPORT_PROJEKT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP aJasperPrint = DelegateFactory.getInstance()
				.getProjektDelegate().printProjekt(projektDto.getIId());
		return aJasperPrint;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		if (projektDto != null) {

			PartnerDto partnerDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(projektDto.getPartnerIId());
			mailtextDto.setMailPartnerIId(projektDto.getPartnerIId());
			mailtextDto.setMailAnprechpartnerIId(projektDto
					.getAnsprechpartnerIId());
			mailtextDto.setParamLocale(Helper.string2Locale(partnerDto
					.getLocaleCNrKommunikation()));
		}

		return mailtextDto;
	}
	
	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		//Druck aendert Status nicht
	}
	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		//Druck aendert Status nicht
		return null;
	}
}
