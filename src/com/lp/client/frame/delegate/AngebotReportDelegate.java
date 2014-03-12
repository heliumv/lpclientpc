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

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.ReportAngebotJournalKriterienDto;
import com.lp.server.angebot.service.ReportAngebotsstatistikKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Angebot Reports.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>15.06.05</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.8 $
 */
public class AngebotReportDelegate extends Delegate {
	private Context context;
	private AngebotReportFac angebotReportFac;

	public AngebotReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			angebotReportFac = (AngebotReportFac) context
					.lookup("lpserver/AngebotReportFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Ein bestehendes Angebot drucken.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param iAnzahlKopienI
	 *            wieviele Kopien zusaetzlich zum Original?
	 * @param bMitLogo
	 *            Boolean
	 * @return JasperPrint[] der Druck und seine Kopien
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP[] printAngebot(Integer iIdAngebotI,
			Integer iAnzahlKopienI, Boolean bMitLogo, String sReportname,
			String sDrucktype) throws ExceptionLP {
		JasperPrintLP[] aJasperPrint = null;

		try {
			aJasperPrint = angebotReportFac.printAngebot(iIdAngebotI,
					iAnzahlKopienI, bMitLogo, sReportname, sDrucktype, LPMain
							.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aJasperPrint;
	}

	/**
	 * Liste aller Angebot drucken.
	 * 
	 * @param kritDtoI
	 *            die Filter- und Sortierkriterien
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAngebotAlle(
			ReportAngebotJournalKriterienDto kritDtoI,
			String erledigungsgrundCNr) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = angebotReportFac.printAngebotAlle(kritDtoI,
					erledigungsgrundCNr, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	/**
	 * Liste aller offenen Angebote drucken.
	 * 
	 * @param kritDtoI
	 *            die Filter- und Sortierkriterien
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAngebotOffene(
			ReportAngebotJournalKriterienDto kritDtoI, Boolean bKommentare,
			Boolean bDetails, Boolean bKundenstammdaten) throws ExceptionLP {
		JasperPrintLP oPrint = null;
		try {
			oPrint = angebotReportFac.printAngebotOffene(kritDtoI, bKommentare,
					bDetails, bKundenstammdaten, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	/**
	 * Liste aller offenen Angebote drucken.
	 * 
	 * @param kritDtoI
	 *            die Filter- und Sortierkriterien
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAngebotAbgelehnte(
			ReportAngebotJournalKriterienDto kritDtoI) throws ExceptionLP {
		JasperPrintLP oPrint = null;
		try {
			oPrint = angebotReportFac.printAngebotAbgelehnte(kritDtoI, LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printAngebotspotential() throws ExceptionLP {
		JasperPrintLP oPrint = null;
		try {
			oPrint = angebotReportFac.printAngebotspotential(LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	/**
	 * Die Vorkalkulation eines Angebots drucken.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAngebotVorkalkulation(Integer iIdAngebotI)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;
		try {
			oPrint = angebotReportFac.printAngebotVorkalkulation(iIdAngebotI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printAdressetikett(Integer partnerIId,
			Integer ansprechparterIId) throws ExceptionLP {

		try {
			return angebotReportFac.printAdressetikett(partnerIId,
					ansprechparterIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printAngebotsstatistik(
			ReportAngebotsstatistikKriterienDto reportAngebotsstatistikKriterienDtoI)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = angebotReportFac
					.printAngebotsstatistik(
							reportAngebotsstatistikKriterienDtoI, LPMain
									.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

}
