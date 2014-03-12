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

import java.sql.Date;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Auftrag Reports.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>27.06.05</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.28 $
 */
public class AuftragReportDelegate extends Delegate {
	private Context context;
	private AuftragReportFac auftragReportFac;

	public AuftragReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			auftragReportFac = (AuftragReportFac) context
					.lookup("lpserver/AuftragReportFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Alle offenen Auftraege fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bInternenKommentarDrucken
	 *            Boolean
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	public JasperPrintLP printAuftragOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Boolean bInternenKommentarDrucken, Integer iArt)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragOffene(
					reportJournalKriterienDtoI, dStichtag, new Boolean(false),
					bInternenKommentarDrucken, iArt, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	/**
	 * Alle offenen Auftraege fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bInternenKommentarDrucken
	 *            Boolean
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	public JasperPrintLP printAuftragOffeneOhneDetail(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer iArt) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragOffeneOhneDetail(
					reportJournalKriterienDtoI, dStichtag,
					bSortierungNachLiefertermin, bInternenKommentarDrucken,
					iArt, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printAuftragspositionsetikett(
			Integer auftragpositionIId) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragspositionsetikett(
					auftragpositionIId, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printErfuellungsjournal(
			Integer auftragIIdRahmenauftrag, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis) throws ExceptionLP {
		try {
			return auftragReportFac.printErfuellungsjournal(
					auftragIIdRahmenauftrag, tVon, tBis, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	/**
	 * Alle offenen Auftraege fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bInternenKommentarDrucken
	 *            Boolean
	 * @param artikelklasseIId
	 *            Integer
	 * @param artikelgruppeIId
	 *            Integer
	 * @param artikelCNrVon
	 *            String
	 * @param artikelCNrBis
	 *            String
	 * @param projektCBezeichnung
	 *            String
	 * @param iArt
	 *            Integer
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	public JasperPrintLP printAuftragOffeneDetail(
			ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung, Integer iArt,
			boolean bLagerstandsdetail, boolean bMitAngelegten)

	throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragOffeneDetails(krit,
					dStichtag, bSortierungNachLiefertermin,
					bInternenKommentarDrucken, artikelklasseIId,
					artikelgruppeIId, artikelCNrVon, artikelCNrBis,
					projektCBezeichnung, iArt, bLagerstandsdetail,
					bMitAngelegten, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printRollierendeplanung(Integer auftragIId,
			boolean bSortiertNachLieferant) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printRollierendeplanung(auftragIId,
					bSortiertNachLieferant, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	/**
	 * Alle offenen Positionen(Varianten) fuer einen bestimmten Mandanten
	 * drucken.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bOhnePositionen
	 *            Boolean
	 * @param fertigungsgruppeIId
	 *            Integer
	 * @param iArt
	 *            Integer
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	public JasperPrintLP printAuftragOffenePositionen(
			ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin, Boolean bOhnePositionen,
			Boolean bSortierungNachAbliefertermin,
			Integer[] fertigungsgruppeIId, Integer iArt,
			Boolean bSortierungNurLiefertermin, String sReportname)

	throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragOffenePositionen(krit,
					dStichtag, bSortierungNachLiefertermin, bOhnePositionen,
					bSortierungNachAbliefertermin, fertigungsgruppeIId, iArt,
					bSortierungNurLiefertermin, sReportname,
					LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printErfuellungsgrad(Timestamp dStichtag,
			Integer personalIId_Vertreter, Integer kostenstelleIId,
			boolean bMitWiederholenden)

	throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printErfuellungsgrad(dStichtag,
					personalIId_Vertreter, kostenstelleIId, bMitWiederholenden,
					LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printAuftragszeiten(Integer iIdAuftrag,
			boolean bSortiertNachPerson)

	throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragszeiten(iIdAuftrag,
					bSortiertNachPerson, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	/**
	 * Die Auftragbestaetigung zu einem bestimmten Auftrag drucken.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param iAnzahlKopienI
	 *            wieviele Kopien zusaetzlich zum Original?
	 * @param bMitLogo
	 *            Boolean
	 * @return JasperPrint[] der Druck und seine Kopien
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP[] printAuftragbestaetigung(Integer iIdAuftragI,
			Integer iAnzahlKopienI, Boolean bMitLogo, String reportname)
			throws ExceptionLP {
		JasperPrintLP[] aJasperPrint = null;

		try {
			aJasperPrint = auftragReportFac.printAuftragbestaetigung(
					iIdAuftragI, iAnzahlKopienI, bMitLogo, reportname,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aJasperPrint;
	}

	/**
	 * Die Packliste zu einem bestimmten Auftrag drucken.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAuftragPackliste(Integer iIdAuftragI,
			String sReportName) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragPackliste(iIdAuftragI,
					sReportName, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	/**
	 * Die Seriennummer zu einem bestimmten Auftragposition drucken.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param iIdAuftragpositionI
	 *            Integer
	 * @param cAktuellerReport
	 *            String
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAuftragSrnnrnEtikett(Integer iIdAuftragI,
			Integer iIdAuftragpositionI, String cAktuellerReport)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragSrnnrnEtikett(iIdAuftragI,
					iIdAuftragpositionI, cAktuellerReport,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printAuftragstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bAuftragsdatum,
			Integer kundeIId_Auftragsadresse,
			Integer kundeIId_Rechnungsadresse, Integer auftragIId,
			String cProjekt, String cBestellnummer, int iOptionSortierung,
			boolean bArbeitszeitVerdichtet, java.sql.Timestamp tStichtag,
			Integer projektIId) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printAuftragstatistik(tVon, tBis,
					bAuftragsdatum, kundeIId_Auftragsadresse,
					kundeIId_Rechnungsadresse, auftragIId, cProjekt,
					cBestellnummer, iOptionSortierung, bArbeitszeitVerdichtet,
					tStichtag, projektIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printTaetigkeitsstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, PartnerklasseDto partnerklasse)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printTaetigkeitsstatistik(tVon, tBis,
					partnerklasse, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printRahmenuebersicht(Integer auftragIId)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = auftragReportFac.printRahmenuebersicht(auftragIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	/**
	 * Die Vorkalkulation eines Auftrags drucken. <br>
	 * Beruecksichtigt werden nur preisbehaftete Positionen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAuftragVorkalkulation(Integer iIdAuftragI)
			throws ExceptionLP {
		JasperPrintLP jasperPrint = null;

		try {
			jasperPrint = auftragReportFac.printAuftragVorkalkulation(
					iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return jasperPrint;
	}

	/**
	 * Die Verfuegbarkeit zu einem bestimmten Auftrag drucken.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printVerfuegbarkeitspruefung(Integer iIdAuftragI,
			boolean bSortiertNachLieferant) throws ExceptionLP {
		JasperPrintLP oPrint = null;
		try {
			oPrint = auftragReportFac.printVerfuegbarkeitspruefung(iIdAuftragI,
					bSortiertNachLieferant, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printAuftragAlle(ReportJournalKriterienDto kritDtoI)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;
		try {
			oPrint = auftragReportFac.printAuftragAlle(kritDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printAuftraegeErledigt(
			ReportJournalKriterienDto kritDtoI) throws ExceptionLP {
		JasperPrintLP oPrint = null;
		try {
			oPrint = auftragReportFac.printAuftraegeErledigt(kritDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}
}
