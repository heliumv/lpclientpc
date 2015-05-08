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

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.finanz.service.ExportlaufDto;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.IntrastatDto;

/**
 * <p> Diese Klasse kuemmert sich den FibuExport</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 26.01.05</p>
 *
 * <p>@author $Author: adi $</p>
 *
 * @version not attributable Date $Date: 2011/10/03 17:50:56 $
 */
public class FibuExportDelegate extends Delegate {
	private Context context;
	private FibuExportFac fibuExportFac;

	public FibuExportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			fibuExportFac = (FibuExportFac) context
					.lookup("lpserver/FibuExportFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public List<String> exportiereBuchungsjournal(String format, Date von, Date bis,
			boolean mitAutoEB, boolean mitManEB, boolean mitAutoB, boolean mitStornierte,
			String bezeichnung) throws ExceptionLP {
		try {
			return fibuExportFac.exportiereBuchungsjournal(format, von, bis, mitAutoEB,
					mitManEB, mitAutoB, mitStornierte, bezeichnung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public String exportiereBelege(FibuExportKriterienDto exportKriterienDto)
			throws ExceptionLP {
		try {
			return fibuExportFac.exportiereBelege(exportKriterienDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ExportlaufDto exportlaufFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fibuExportFac.exportlaufFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ExportdatenDto exportdatenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fibuExportFac.exportdatenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ExportdatenDto exportdatenFindByBelegartCNrBelegiid(
			String belegartCNr, Integer belegIId) throws ExceptionLP {
		try {
			return fibuExportFac.exportdatenFindByBelegartCNrBelegiid(
					belegartCNr, belegIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void nehmeExportlaufZurueckUndLoescheIhn(Integer exportlaufIId)
			throws ExceptionLP {
		try {
			fibuExportFac.nehmeExportlaufZurueckUndLoescheIhn(exportlaufIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String exportierePersonenkonten(String kontotypCNr)
			throws ExceptionLP {
		try {
			return fibuExportFac.exportierePersonenkonten(kontotypCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer exportlaufFindLetztenExportlauf() throws ExceptionLP {
		try {
			return fibuExportFac.exportlaufFindLetztenExportlauf(LPMain.getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeExportdaten(Integer exportdatenIId) throws ExceptionLP {
		try {
			fibuExportFac.removeExportdaten(exportdatenIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Intrastat-Meldung exportieren.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param sVerfahren
	 *            ReportJournalKriterienDto
	 * @param dVon
	 *            Integer
	 * @param dBis
	 *            Date
	 * @param bdTransportkosten
	 *            BigDecimal
	 */
	public ArrayList<IntrastatDto> exportiereIntrastatmeldung(
			String sVerfahren, Date dVon, Date dBis,
			BigDecimal bdTransportkosten) throws ExceptionLP {
		ArrayList<IntrastatDto> daten = null;
		try {
			daten = fibuExportFac.exportiereIntrastatmeldung(sVerfahren, dVon,
					dBis, bdTransportkosten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return daten;
	}

	public String importiereOffenePosten(ArrayList<String[]> daten)
			throws ExceptionLP {
		String result = null;
		try {
			result = fibuExportFac.importiereOffenePosten(daten, LPMain.getTheClient());
		} catch (Throwable e) {
			handleThrowable(e);
		}
		return result;
	}

	public ExportdatenDto[] exportdatenFindByExportlaufIIdBelegartCNr(
			Integer exportlaufIId, String belegartCNr) throws ExceptionLP {
		try {
			return fibuExportFac.exportdatenFindByExportlaufIIdBelegartCNr(
					exportlaufIId, belegartCNr);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

}
