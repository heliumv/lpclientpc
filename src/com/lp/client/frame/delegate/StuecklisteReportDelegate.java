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
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.util.report.JasperPrintLP;

public class StuecklisteReportDelegate extends Delegate {
	private Context context;
	private StuecklisteReportFac stuecklisteReportFac;

	public StuecklisteReportDelegate() throws Exception {
		context = new InitialContext();
		stuecklisteReportFac = (StuecklisteReportFac) context
				.lookup("lpserver/StuecklisteReportFacBean/remote");
	}

	public JasperPrintLP printStuecklisteAllgemein(Integer stuecklisteIId,
			boolean bMtiPositionskommentar, boolean bMitStuecklistenkommentar,
			boolean bUnterstuecklistenEinbinden,
			boolean bGleichePositionenZusammenfassen,
			int iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen,
			Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2,
			Integer iOptionSortierungStuecklisteGesamt3) throws Throwable {
		try {
			return stuecklisteReportFac.printStuecklisteAllgemein(
					stuecklisteIId, new Boolean(bMtiPositionskommentar),
					new Boolean(bMitStuecklistenkommentar), new Boolean(
							bUnterstuecklistenEinbinden), new Boolean(
							bGleichePositionenZusammenfassen), new Integer(
							iOptionSortierungUnterstuecklisten),
					bUnterstklstrukurBelassen, LPMain.getTheClient(),
					iOptionSortierungStuecklisteGesamt1,
					iOptionSortierungStuecklisteGesamt2,
					iOptionSortierungStuecklisteGesamt3);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printStuecklisteAllgemeinMitPreis(
			Integer stuecklisteIId, int iOptionPreis,
			boolean bMtiPositionskommentar, boolean bMitStuecklistenkommentar,
			boolean bUnterstuecklistenEinbinden,
			boolean bGleichePositionenZusammenfassen,
			int iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen,
			Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2,
			Integer iOptionSortierungStuecklisteGesamt3) throws Throwable {
		try {
			return stuecklisteReportFac.printStuecklisteAllgemeinMitPreis(
					stuecklisteIId, new Integer(iOptionPreis), new Boolean(
							bMtiPositionskommentar), new Boolean(
							bMitStuecklistenkommentar), new Boolean(
							bUnterstuecklistenEinbinden), new Boolean(
							bGleichePositionenZusammenfassen), new Integer(
							iOptionSortierungUnterstuecklisten),
					bUnterstklstrukurBelassen, LPMain.getTheClient(),
					iOptionSortierungStuecklisteGesamt1,
					iOptionSortierungStuecklisteGesamt2,
					iOptionSortierungStuecklisteGesamt3);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printAusgabestueckliste(Integer stuecklisteIId[],
			Integer lagerIId, boolean bMitStuecklistenkommentar,
			boolean bUnterstuecklistenEinbinden,
			boolean bGleichePositionenZusammenfassen,
			int iOptionSortierungUnterstuecklisten, BigDecimal nLossgroesse,
			boolean bUnterstklstrukurBelassen) throws Throwable {
		try {
			return stuecklisteReportFac.printAusgabestueckliste(stuecklisteIId,
					lagerIId, new Boolean(bMitStuecklistenkommentar),
					new Boolean(bUnterstuecklistenEinbinden), new Boolean(
							bGleichePositionenZusammenfassen), new Integer(
							iOptionSortierungUnterstuecklisten), nLossgroesse,
					bUnterstklstrukurBelassen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printArbeitsplan(Integer stuecklisteIId,
			BigDecimal nLossgroesse) throws Throwable {
		try {
			return stuecklisteReportFac.printArbeitsplan(stuecklisteIId,
					nLossgroesse, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLoseAktualisiert(
			TreeMap<String, Object[]> tmAufgeloesteFehlmengen) throws Throwable {
		try {
			return stuecklisteReportFac.printLoseAktualisiert(
					tmAufgeloesteFehlmengen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printGesamtkalkulation(Integer stuecklisteIId,
			BigDecimal nLosgroesse) throws Throwable {
		try {
			return stuecklisteReportFac.printGesamtkalkulation(stuecklisteIId,
					nLosgroesse, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
