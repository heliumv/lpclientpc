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
import com.lp.server.reklamation.service.ReklamationReportFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReklamationReportDelegate extends Delegate {
	private Context context;
	private ReklamationReportFac reklamationReportFac;

	public ReklamationReportDelegate() throws Exception {
		context = new InitialContext();
		reklamationReportFac = (ReklamationReportFac) context
				.lookup("lpserver/ReklamationReportFacBean/remote");
	}

	public JasperPrintLP printReklamationsjournal(Integer kostenstelleIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bKunde,
			boolean bLieferant, boolean bFertigung, boolean bNurOffene,
			int iSortierung) throws ExceptionLP {
		try {
			return reklamationReportFac.printReklamationsjournal(
					kostenstelleIId, tVon, tBis, bKunde, bLieferant,
					bFertigung, bNurOffene, iSortierung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printLieferantentermintreue(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer lieferantIId) throws ExceptionLP {
		try {
			return reklamationReportFac.printLieferantentermintreue(tVon, tBis,
					lieferantIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printOffeneReklamationenEinesArtikels(
			Integer artikelIId) throws ExceptionLP {
		try {
			return reklamationReportFac.printOffeneReklamationenEinesArtikels(
					artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printMitarbeiterreklamation(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bKunde, boolean bLieferant,
			boolean bFertigung, Integer kundeIId, boolean bNurBerechtigte)
			throws ExceptionLP {
		try {
			return reklamationReportFac.printMitarbeiterreklamation(tVon, tBis,
					bKunde, bLieferant, bFertigung, kundeIId, bNurBerechtigte,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printMaschinenreklamation(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bKunde, boolean bLieferant,
			boolean bFertigung, Integer kundeIId, boolean bNurBerechtigte)
			throws ExceptionLP {
		try {
			return reklamationReportFac.printMaschinenreklamation(tVon, tBis,
					bKunde, bLieferant, bFertigung, kundeIId, bNurBerechtigte,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printFehlerarten(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bKunde, boolean bLieferant,
			boolean bFertigung, Integer kundeIId, int iGruppierung,
			boolean bNurBerechtigte) throws ExceptionLP {
		try {
			return reklamationReportFac.printFehlerarten(tVon, tBis, bKunde,
					bLieferant, bFertigung, kundeIId, iGruppierung,
					bNurBerechtigte, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printLieferantenbeurteilung(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer brancheIId, Integer lieferantIId,
			Integer liefergruppeIId) throws ExceptionLP {
		try {
			return reklamationReportFac.printLieferantenbeurteilung(tVon, tBis,
					lieferantIId, brancheIId, liefergruppeIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printReklamation(Integer reklamationIId,
			boolean druckeUnterartLieferant) throws ExceptionLP {
		try {
			return reklamationReportFac.printReklamation(reklamationIId,
					druckeUnterartLieferant, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

}
