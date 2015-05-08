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
package com.lp.client.frame.delegate;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ExtralisteRueckgabeTabelleDto;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;

public class SystemReportDelegate extends Delegate {
	private Context context;
	private SystemReportFac systemReportFac;

	public SystemReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			systemReportFac = (SystemReportFac) context
					.lookup("lpserver/SystemReportFacBean/remote");
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public JasperPrintLP printUseCaseHandler(String uuid, QueryParameters q,
			int iAnzahlZeilen, String ueberschrift) throws ExceptionLP {
		try {
			return systemReportFac.printUseCaseHandler(uuid, q, iAnzahlZeilen,
					ueberschrift, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printEntitylog(String filterKey, String filterId,
			String cDatensatz) throws ExceptionLP {
		try {
			return systemReportFac.printEntitylog(filterKey, filterId,
					cDatensatz, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printExtraliste(
			ExtralisteRueckgabeTabelleDto extralisteRueckgabeTabelleDto, Integer extralisteIId)
			throws ExceptionLP {
		try {
			return systemReportFac.printExtraliste(
					extralisteRueckgabeTabelleDto, extralisteIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP[] printVersandAuftrag(
			VersandauftragDto versandauftragDto, Integer iAnzahlKopien)
			throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP[] print = null;
		try {
			print = systemReportFac.printVersandAuftrag(versandauftragDto,
					iAnzahlKopien, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

}
