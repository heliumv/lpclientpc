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

import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

public class ProjektDelegate extends Delegate {
	private Context context;
	private ProjektFac projektFac;
	private ProjektReportFac projektReportFac;

	public ProjektDelegate() throws Exception {
		context = new InitialContext();
		projektFac = (ProjektFac) context
				.lookup("lpserver/ProjektFacBean/remote");
		projektReportFac = (ProjektReportFac) context
				.lookup("lpserver/ProjektReportFacBean/remote");
	}

	public Integer createProjekt(ProjektDto projektDto) throws ExceptionLP {
		Integer key = null;

		try {
			key = projektFac.createProjekt(projektDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return key;
	}

	public Integer createHistory(HistoryDto historyDto) throws ExceptionLP {
		Integer key = null;

		try {
			key = projektFac.createHistory(historyDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return key;
	}

	public void removeProjekt(Integer iId) throws ExceptionLP {
		try {
			projektFac.removeProjekt(iId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public void removeProjekt(ProjektDto projektDto) throws ExceptionLP {
		try {
			projektFac.removeProjekt(projektDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * L&ouml;schen einer bestehendenreat History.
	 * 
	 * @param historyDto
	 *            historyDto
	 * @throws ExceptionLP
	 */
	public void removeHistory(HistoryDto historyDto) throws ExceptionLP {
		try {
			projektFac.removeHistory(historyDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void toggleInternErledigt(Integer projektIId) throws ExceptionLP {
		try {
			projektFac.toggleInternErledigt(projektIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateProjekt(ProjektDto projektDto) throws ExceptionLP {
		try {
			projektFac.updateProjekt(projektDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateHistory(HistoryDto historyDto) throws ExceptionLP {
		try {
			projektFac.updateHistory(historyDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Einen Projekt ueber seinen Schluessel von der db holen.
	 * 
	 * @param iId
	 *            Integer
	 * @throws ExceptionLP
	 * @return ProjektDto
	 */
	public ProjektDto projektFindByPrimaryKey(Integer iId) throws ExceptionLP {
		ProjektDto projektDto = null;

		try {
			projektDto = projektFac.projektFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return projektDto;
	}

	public ArrayList<String> getVorgaengerProjekte(Integer projektIId)
			throws ExceptionLP {
		ArrayList<String> s = null;

		try {
			s = projektFac.getVorgaengerProjekte(projektIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return s;
	}

	/**
	 * Einen History ueber seinen Schluessel von der db holen.
	 * 
	 * @param iId
	 *            Integer
	 * @throws ExceptionLP
	 * @return HistoryDto
	 */
	public HistoryDto historyFindByPrimaryKey(Integer iId) throws ExceptionLP {
		HistoryDto historyDto = null;

		try {
			historyDto = projektFac.historyFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return historyDto;
	}

	/**
	 * Offene. Projekte drucken.
	 * 
	 * @param krit
	 *            PK der Projekt
	 * @param dStichtag
	 *            Date
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printProjektOffene(ReportJournalKriterienDto krit,
			Integer bereichIId, java.sql.Date dStichtag) throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = projektReportFac.printProjektOffene(krit, dStichtag,
					bereichIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public String istPartnerBeiEinemMandantenGesperrt(Integer partnerIId)
			throws ExceptionLP {

		try {
			return projektFac.istPartnerBeiEinemMandantenGesperrt(partnerIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public JasperPrintLP printProjektOffeneAuswahlListe() throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = projektReportFac.printProjektOffeneAuswahlListe(LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	/**
	 * Alle. Projekte drucken.
	 * 
	 * @param krit
	 *            PK der Projekt
	 * @param dStichtag
	 *            Date
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printProjektAlle(ReportJournalKriterienDto krit,
			Integer bereichIId, java.sql.Date dStichtag,
			boolean belegdatumStattZieltermin) throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = projektReportFac.printProjektAlle(krit, dStichtag,
					bereichIId, belegdatumStattZieltermin,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	/**
	 * Alle. Projekte drucken.
	 * 
	 * @param krit
	 *            PK der Projekt
	 * @param dStichtag
	 *            Date
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printProjektErledigt(ReportJournalKriterienDto krit,
			Integer bereichIId, java.sql.Date dStichtag,
			boolean interneErledigungBeruecksichtigen) throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = projektReportFac.printProjektErledigt(krit, dStichtag,
					bereichIId, interneErledigungBeruecksichtigen,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP printAktivitaetsuebersicht(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bGesamtinfo) throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = projektReportFac.printAktivitaetsuebersicht(tVon, tBis,
					bGesamtinfo, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP printProjektverlauf(Integer projektIId)
			throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = projektReportFac.printProjektverlauf(projektIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP printProjekt(Integer iidProjekt) throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = projektReportFac.printProjekt(iidProjekt, null, null,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public void vertauscheProjekte(Integer iIdPosition1I,
			Integer iIdPosition2I, int min) throws ExceptionLP {
		try {
			projektFac.vertauscheProjekte(iIdPosition1I, iIdPosition2I, min);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void inQueueAufnehmen(Integer iIdPosition1I) throws ExceptionLP {
		try {
			projektFac.inQueueAufnehmen(iIdPosition1I, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void ausQueueEntfernen(Integer iIdPosition1I) throws ExceptionLP {
		try {
			projektFac.ausQueueEntfernen(iIdPosition1I, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Double berechneGesamtSchaetzung(Integer personal_i_id_zugewiesener)
			throws ExceptionLP {
		try {
			return projektFac.berechneGesamtSchaetzung(
					personal_i_id_zugewiesener, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

}
