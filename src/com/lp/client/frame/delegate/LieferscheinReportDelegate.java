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
package com.lp.client.frame.delegate;

import java.math.BigDecimal;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.ReportLieferscheinJournalKriterienDto;
import com.lp.server.rechnung.service.ReportRechnungJournalKriterienDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Delegate fuer Lieferscheinreports.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Uli Walch, 28.09.2005
 * </p>
 * 
 * <p>
 * 
 * @author Uli Walch
 *         </p>
 * 
 * @version 1.0
 * 
 * @version $Revision: 1.15 $ Date $Date: 2012/04/04 13:26:50 $
 */
public class LieferscheinReportDelegate extends Delegate {
	private Context context = null;
	private LieferscheinReportFac lieferscheinReportFac = null;

	public LieferscheinReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			lieferscheinReportFac = (LieferscheinReportFac) context
					.lookup("lpserver/LieferscheinReportFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Einen Lieferschein drucken.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param iAnzahlKopienI
	 *            wieviele Kopien zusaetzlich zum Original?
	 * @param bMitLogo
	 *            Boolean
	 * @return JasperPrint[] der Druck und seine Kopien
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP[] printLieferschein(Integer iIdLieferscheinI,
			Integer iAnzahlKopienI, Boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP[] aJasperPrint = null;

		try {
			aJasperPrint = lieferscheinReportFac.printLieferschein(
					iIdLieferscheinI, iAnzahlKopienI, bMitLogo,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aJasperPrint;
	}

	public int getGesamtzahlPakete(Integer iIdLieferscheinI) throws ExceptionLP {
		try {
			return lieferscheinReportFac.getGesamtzahlPakete(iIdLieferscheinI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return 0;
		}
	}

	public JasperPrintLP printLieferscheinAlle(
			ReportLieferscheinJournalKriterienDto krit) throws ExceptionLP {
		JasperPrintLP aJasperPrint = null;

		try {
			aJasperPrint = lieferscheinReportFac.printLieferscheinAlle(krit,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aJasperPrint;
	}

	/**
	 * Lieferscheinetikett drucken.
	 * 
	 * @param iIdLieferscheinI
	 *            pk des Lieferscheins
	 * @param iPaketnummer
	 *            Integer
	 * @throws ExceptionLP
	 * @return JasperPrint der Druck
	 */
	public JasperPrintLP printLieferscheinetikett(Integer iIdLieferscheinI,
			Integer iPaketnummer) throws ExceptionLP {
		JasperPrintLP print = null;

		try {
			print = lieferscheinReportFac.printLieferscheinEtikett(
					iIdLieferscheinI, iPaketnummer, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return print;
	}

	/**
	 * Lieferscheinetikett drucken.
	 * 
	 * @param iIdLieferscheinI
	 *            pk des Lieferscheins
	 * @throws ExceptionLP
	 * @return JasperPrint der Druck
	 */
	public JasperPrintLP printLieferscheinWAEtikett(Integer iIdLieferscheinI,
			Integer lieferscheinpositionIId, Integer iPaketnummer,
			BigDecimal bdHandmenge) throws ExceptionLP {
		JasperPrintLP print = null;

		try {
			print = lieferscheinReportFac.printLieferscheinWAEtikett(
					iIdLieferscheinI, iPaketnummer, lieferscheinpositionIId,
					bdHandmenge, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return print;
	}

	public JasperPrintLP printVersandetikett(Integer iIdLieferscheinI)
			throws ExceptionLP {
		JasperPrintLP print = null;

		try {
			print = lieferscheinReportFac.printVersandetikett(iIdLieferscheinI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return print;
	}

	/**
	 * Alle offenen Lieferscheine fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Filter- und Sortierkriterien
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	public JasperPrintLP printLieferscheinOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI, Integer iArt,
			boolean bMitDetails) throws ExceptionLP {
		JasperPrintLP print = null;

		try {
			print = lieferscheinReportFac.printLieferscheinOffene(
					reportJournalKriterienDtoI, iArt, bMitDetails,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return print;
	}

	/**
	 * Eine Liste aller Lieferscheine eines Mandanten drucken, die sich im
	 * Status angleget befinden.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Filter- und Sortierkriterien
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printLieferscheinAngelegte(
			ReportJournalKriterienDto reportJournalKriterienDtoI)
			throws ExceptionLP {
		JasperPrintLP print = null;

		try {
			print = lieferscheinReportFac.printLieferscheinAngelegte(
					reportJournalKriterienDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return print;
	}
}
