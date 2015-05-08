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
package com.lp.client.rechnung;

import java.sql.Timestamp;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportRechnungZahlschein extends ReportBeleg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public ReportRechnungZahlschein(InternalFrame internalFrame,
			RechnungDto rechnungDto, String sAdd2Title) throws Throwable {
		super(internalFrame, null, sAdd2Title, LocaleFac.BELEGART_EINGANGSRECHNUNG,
				rechnungDto.getIId(), rechnungDto.getKostenstelleIId());
	}
	
	
	public String getModul() {
		return RechnungReportFac.REPORT_MODUL;
	}
	  public String getReportname() {
		    return RechnungReportFac.REPORT_RECHNUNGEN_ZAHLSCHEIN;
		  }

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;
		JasperPrintLP[] aJasperPrint =DelegateFactory.getInstance().getRechnungDelegate().printRechnungZahlschein(getIIdBeleg(),
				getReportname(), wnfKopien.getInteger());
		jasperPrint = aJasperPrint[0];

		for (int i = 1; i < aJasperPrint.length; i++) {
			jasperPrint = Helper.addReport2Report(jasperPrint, aJasperPrint[i].getPrint());
		}
		return jasperPrint;
	}
	
	public boolean getBErstelleReportSofort() {
		return true;
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		//Lock ist hier egal
		return null;
	}
	
	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		// Druck aendert Status nicht
	}
	
	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		// Druck aendert Status nicht
		return null;
	}
}
