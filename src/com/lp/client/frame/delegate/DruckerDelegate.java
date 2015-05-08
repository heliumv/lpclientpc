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

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeitabschlussDto;
import com.lp.server.system.service.DruckerFac;
import com.lp.server.system.service.ReportkonfDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.StandarddruckerDto;

@SuppressWarnings("static-access")
/*
 * <p>BusinessDelegate fuer die Druckereinstellungen</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 25.04.06</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2010/02/26 09:37:37 $
 */
public class DruckerDelegate extends Delegate {
	private Context context;
	private DruckerFac druckerFac;

	public DruckerDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			druckerFac = (DruckerFac) context
					.lookup("lpserver/DruckerFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Integer updateStandarddrucker(StandarddruckerDto standarddruckerDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			if (standarddruckerDto.getIId() == null) {
				iId = druckerFac.createStandarddrucker(standarddruckerDto,
						LPMain.getInstance().getTheClient());
			} else {
				iId = druckerFac.updateStandarddrucker(standarddruckerDto,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
		return iId;
	}

	public Integer updateReportvariante(ReportvarianteDto reportvarianteDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			druckerFac.updateReportvariante(reportvarianteDto, LPMain
					.getInstance().getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);

		}
		return iId;
	}

	public Integer createReportvariante(ReportvarianteDto dto)
			throws ExceptionLP {
		try {
			return druckerFac.createReportvariante(dto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeStandarddrucker(StandarddruckerDto standarddruckerDto)
			throws ExceptionLP {
		try {
			druckerFac.removeStandarddrucker(standarddruckerDto, LPMain
					.getInstance().getTheClient().getIDUser());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeReportvariante(ReportvarianteDto dto) throws ExceptionLP {
		try {
			druckerFac.removeReportvariante(dto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void saveReportKonf(Integer standarddruckerIId, ReportkonfDto[] dtos)
			throws ExceptionLP {
		try {
			druckerFac.saveReportKonf(standarddruckerIId, dtos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Map holeAlleVarianten(String reportname) throws ExceptionLP {
		try {
			return druckerFac.holeAlleVarianten(reportname,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void deleteReportKonf(Integer standarddruckerIId) throws ExceptionLP {
		try {
			druckerFac.deleteReportKonf(standarddruckerIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public StandarddruckerDto standarddruckerFindByPcReportnameOhneExc(
			StandarddruckerDto standarddruckerDto) throws ExceptionLP {
		try {
			return druckerFac.standarddruckerFindByPcReportnameOhneExc(
					standarddruckerDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public StandarddruckerDto standarddruckerFindByPcReportnameOhneVariante(
			StandarddruckerDto standarddruckerDto) throws ExceptionLP {
		try {
			return druckerFac.standarddruckerFindByPcReportnameOhneVariante(
					standarddruckerDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeReportkonf(Integer reportkonfIId) throws ExceptionLP {
		try {
			druckerFac.removeReportkonf(reportkonfIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ReportkonfDto[] reportkonfFindByStandarddruckerIId(
			Integer standarddruckerIId) throws ExceptionLP {
		try {
			return druckerFac
					.reportkonfFindByStandarddruckerIId(standarddruckerIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ReportvarianteDto reportvarianteFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return druckerFac.reportvarianteFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
