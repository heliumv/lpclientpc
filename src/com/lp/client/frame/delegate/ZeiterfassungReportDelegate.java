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

import java.sql.Timestamp;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.MaschinenerfolgReportDto;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public class ZeiterfassungReportDelegate extends Delegate {
	private ZeiterfassungReportFac zeiterfassungReportFac;
	private Context context;

	public ZeiterfassungReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			zeiterfassungReportFac = (ZeiterfassungReportFac) context
					.lookup("lpserver/ZeiterfassungReportFacBean/remote");
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}

	}

	public JasperPrintLP printZestiftliste() throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printZestiftliste(LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printMaschinenzeitdaten(Integer maschineIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis)
			throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printMaschinenzeitdaten(maschineIId,
					tVon, tBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printMitarbeitereinteilung(Integer personalIId,
			Integer personalgruppeIId, java.sql.Timestamp tStichtag,
			Integer iOptionSortierung, String sortierung) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printMitarbeitereinteilung(
					personalIId, personalgruppeIId, tStichtag,
					iOptionSortierung, sortierung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MaschinenerfolgReportDto printMaschinenerfolg(Integer personalIId,
			Integer personalgruppeIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer iOptionSortierung,
			String sortierung, boolean bMonatsbetrachtung) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printMaschinenerfolg(personalIId,
					personalgruppeIId, tVon, tBis, iOptionSortierung,
					sortierung, bMonatsbetrachtung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printMaschinenliste(Timestamp tStichtag,
			boolean bMitVersteckten) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printMaschinenliste(tStichtag,
					bMitVersteckten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printMaschinenproduktivitaet(Integer maschineIId,
			Timestamp tVon, Timestamp tBis) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printMaschinenproduktivitaet(
					maschineIId, tVon, tBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printProduktivitaetstagesstatistik(
			Integer personalIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer iOption, boolean bMitVersteckten,
			boolean bMonatsbetrachtung, Integer personalgruppeIId)
			throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printProduktivitaetstagesstatistik(
					personalIId, tVon, tBis, iOption, bMitVersteckten,
					bMonatsbetrachtung, personalgruppeIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printMitarbeiteruebersicht(Integer personalIId,
			Integer iJahrVon, Integer iMonatVon, Integer iJahrBis,
			Integer iMonatBis, Integer iOption, Integer iOptionSortierung,
			boolean bPlusversteckte) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printMitarbeiteruebersicht(
					personalIId, iJahrVon, iMonatVon, iJahrBis, iMonatBis,
					iOption, iOptionSortierung, bPlusversteckte,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printArbeitszeitstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, int iOptionSortierung, String belegartCNr,
			Integer belegartIId, Integer personalIId, Integer artikelIId,
			Integer partnerIId, Integer artikelgruppeIId,
			Integer artikelklasseIId, boolean bVerdichtet) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printArbeitszeitstatistik(tVon, tBis,
					iOptionSortierung, belegartCNr, belegartIId, personalIId,
					artikelIId, partnerIId, artikelgruppeIId, artikelklasseIId,
					bVerdichtet, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printReisezeiten(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			boolean bPlusVersteckte, Integer iOption) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printReisezeiten(personalIId, tVon,
					tBis, iOption, bPlusVersteckte, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printFahrzeuge(Integer fahrzeugIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printFahrzeuge(fahrzeugIId, tVon,
					tBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printMaschinenbelegung(Integer maschineIId,
			java.sql.Timestamp tStichtag, boolean bMitErstemUagDesNaechstenAg)
			throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printMaschinenbelegung(maschineIId,
					tStichtag, bMitErstemUagDesNaechstenAg,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printFahrtenbuch(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			boolean bPlusVersteckte, Integer iOption) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printFahrtenbuch(personalIId, tVon,
					tBis, iOption, bPlusVersteckte, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printZeitdatenjournal(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis)
			throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printZeitdatenjournal(personalIId,
					tVon, tBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printTelefonzeiten(Integer personalIId,
			Integer partnerIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bSortiertNachPersonal)
			throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printTelefonzeiten(personalIId,
					partnerIId, tVon, tBis, bSortiertNachPersonal,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printAuftragszeitstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis) throws ExceptionLP {
		try {
			return zeiterfassungReportFac.printAuftragszeitstatistik(tVon,
					tBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
}
