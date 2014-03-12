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
import java.sql.Date;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.LiquititaetsvorschauImportDto;
import com.lp.server.finanz.service.PrintKontoblaetterModel;
import com.lp.server.finanz.service.ReportErfolgsrechnungKriterienDto;
import com.lp.server.finanz.service.ReportSaldenlisteKriterienDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * <I>Business-Delegate fuer das Finanzmodul</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>13. 05. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.25 $
 */
public class FinanzReportDelegate extends Delegate {
	private Context context;
	private FinanzReportFac finanzReportFac;

	public FinanzReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			finanzReportFac = (FinanzReportFac) context
					.lookup("lpserver/FinanzReportFacBean/remote");
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Alle Konten drucken.
	 * 
	 * @param kontotypCNr
	 *            kontotyp
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printAlleKonten(String kontotypCNr,
			boolean bMitVersteckten) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printAlleKonten(LPMain.getTheClient(),
					kontotypCNr, bMitVersteckten);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Buchungen auf einem Konto drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param kontoIId
	 *            Integer
	 */
	public JasperPrintLP printBuchungenAufKonto(Integer kontoIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printBuchungenAufKonto(
					LPMain.getTheClient(), kontoIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Buchungen int einem Buchungsjournal drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param buchungsjournalIId
	 *            Integer
	 */
	public JasperPrintLP printBuchungenInBuchungsjournal(
			Integer buchungsjournalIId) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printBuchungenInBuchungsjournal(
					LPMain.getTheClient(), buchungsjournalIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Buchungsjournal drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param buchungsjournalIId
	 *            Integer
	 */
	public JasperPrintLP printBuchungsjournal(Integer buchungsjournalIId,
			Date dVon, Date dBis, boolean storniert,
			boolean bDatumsfilterIstBuchungsdatum, String text,
			String belegnummer, BigDecimal betrag) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printBuchungsjournal(
					LPMain.getTheClient(), buchungsjournalIId, dVon, dBis,
					storniert, bDatumsfilterIstBuchungsdatum, text,
					belegnummer, betrag);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * RA-Schreiben drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param mahnungIId
	 *            Integer
	 */
	public JasperPrintLP printRASchreiben(Integer mahnungIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printRASchreiben(
					LPMain.getTheClient(), mahnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * RA-Schreiben drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param rechnungIId
	 *            Integer
	 */
	public JasperPrintLP printRASchreibenFuerRechnung(Integer rechnungIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printRASchreibenFuerRechnung(
					LPMain.getTheClient(), rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Mahnung drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param mahnungIId
	 *            Integer
	 */
	public JasperPrintLP printMahnungAusMahnlauf(Integer mahnungIId,
			boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printMahnungAusMahnlauf(
					LPMain.getTheClient(), mahnungIId, bMitLogo);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * 
	 * @param kontotypCNr
	 * @param kontoIId
	 * @param ktoVon
	 * @param ktoBis
	 * @param bSortiertNachDatum
	 * @param tVon
	 * @param tBis
	 * @param bSortiertNachBeleg
	 * @param geschaeftsjahr
	 * @return
	 * @throws ExceptionLP
	 * 
	 * @deprecated bitte {@link printKontoblaetter(PrintKontoblaetterModel
	 *             kbModel, TheClientDto theClientDto)} benutzen
	 */
	public JasperPrintLP printKontoblaetter(String kontotypCNr,
			Integer kontoIId, String ktoVon, String ktoBis,
			boolean bSortiertNachDatum, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bSortiertNachBeleg,
			String geschaeftsjahr) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printKontoblaetter(kontotypCNr,
					kontoIId, ktoVon, ktoBis, bSortiertNachDatum, tVon, tBis,
					LPMain.getTheClient(), bSortiertNachBeleg, geschaeftsjahr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Kontobl&auml;tter ausdrucken
	 * 
	 * @param kbModel
	 * @param theClientDto
	 * @return
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printKontoblaetter(PrintKontoblaetterModel kbModel)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printKontoblaetter(kbModel,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return print;
	}
	
	/**
	 * Buchungsbeleg ausdrucken
	 */
	public JasperPrintLP printBuchungsbeleg(Integer buchungIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac.printBuchungsbeleg(buchungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return print;
	}

	public JasperPrintLP printKassabuch(PrintKontoblaetterModel kbModel)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printKassabuch(kbModel,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return print;
	}

	public JasperPrintLP printOffenPosten(String kontotypCNr,
			Integer geschaeftsjahr, Integer kontoIId,
			java.sql.Timestamp tStichtag, boolean sortAlphabethisch) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printOffenePosten(kontotypCNr,
					geschaeftsjahr, kontoIId, tStichtag, LPMain.getTheClient(), sortAlphabethisch);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printLiquiditaetsvorschau(BigDecimal kontostand,
			BigDecimal kreditlimit, Integer kalenderwochen,
			boolean bTerminNachZahlungsmoral, boolean bMitPlankosten,
			ArrayList<LiquititaetsvorschauImportDto> alPlankosten,
			boolean bMitOffenenAngeboten, boolean bMitOffenenBestellungen,
			boolean bMitOffenenAuftraegen) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printLiquiditaetsvorschau(kontostand,
					kreditlimit, kalenderwochen, bTerminNachZahlungsmoral,
					bMitPlankosten, alPlankosten, bMitOffenenAngeboten,
					bMitOffenenBestellungen, bMitOffenenAuftraegen,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Mahnung drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param rechnungIId
	 *            Integer
	 * @param mahnstufeIId
	 *            Integer
	 */
	public JasperPrintLP printMahnungFuerRechnung(Integer rechnungIId,
			Integer mahnstufeIId, boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac.printMahnungFuerRechnung(
					LPMain.getTheClient(), rechnungIId, mahnstufeIId, bMitLogo);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Alle Mahnungen eines Mahnlaufes drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param mahnlaufIId
	 *            Integer
	 */
	public JasperPrintLP[] printSammelMahnungen(Integer mahnlaufIId,
			Boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP[] print = null;
		try {
			print = this.finanzReportFac.printSammelmahnung(mahnlaufIId,
					bMitLogo, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Saldenliste drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printSaldenliste(ReportSaldenlisteKriterienDto krit)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printSaldenliste(LPMain.getTheClient()
					.getMandant(), krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * UVA drucken
	 * 
	 */
	public JasperPrintLP printUva(ReportUvaKriterienDto krit)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printUva(LPMain.getTheClient()
					.getMandant(), krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Mahnlauf drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param reportJournalKriterienDtoI
	 *            ReportJournalKriterienDto
	 * @param mahnlaufIId
	 *            Integer
	 */
	public JasperPrintLP printMahnlauf(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Integer mahnlaufIId) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac.printMahnlauf(reportJournalKriterienDtoI,
					mahnlaufIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printSteuerkategorien() throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac
					.printSteuerkategorien(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Intrastat-Vorschau drucken.
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
	public JasperPrintLP printIntrastatVorschau(String sVerfahren, Date dVon,
			Date dBis, BigDecimal bdTransportkosten) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac.printIntrastatVorschau(sVerfahren, dVon,
					dBis, bdTransportkosten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printErfolgsrechnung(
			ReportErfolgsrechnungKriterienDto kriterien, boolean bBilanz, boolean bDetails)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printErfolgsrechnung(LPMain
					.getTheClient().getMandant(), kriterien, bBilanz, bDetails, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}
}
