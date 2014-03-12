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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BSMahnlaufDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.bestellung.service.ImportMonatsbestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public class BestellungDelegate extends Delegate {
	private Context context;
	private BestellungFac bestellungFac;
	private BestellpositionFac bestellpositionFac;
	private BestellungReportFac bestellungReportFac;

	public BestellungDelegate() throws Throwable {
		context = new InitialContext();
		bestellungFac = (BestellungFac) context
				.lookup("lpserver/BestellungFacBean/remote");
		bestellpositionFac = (BestellpositionFac) context
				.lookup("lpserver/BestellpositionFacBean/remote");
		bestellungReportFac = (BestellungReportFac) context
				.lookup("lpserver/BestellungReportFacBean/remote");
	}

	public Integer createBestellung(BestellungDto bestellungDto)
			throws ExceptionLP {
		Integer iIdBestellung = null;
		try {
			iIdBestellung = bestellungFac.createBestellung(bestellungDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iIdBestellung;
	}

	public boolean enthaeltBestellungMaterialzuschlaege(Integer bestellungIId)
			throws ExceptionLP {

		try {
			return bestellpositionFac
					.enthaeltBestellungMaterialzuschlaege(bestellungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}

	}

	public void aktiviereBelegControlled(Integer iid, Timestamp t) throws ExceptionLP {
		try {
			bestellungFac.aktiviereBelegControlled(iid, t, LPMain.getTheClient());
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
	}
	
	public Timestamp berechneBelegControlled(Integer iid) throws ExceptionLP {
		try {
			return bestellungFac.berechneBelegControlled(iid, LPMain.getTheClient());
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public void updateBestellung(BestellungDto bestellungDto)
			throws ExceptionLP {
		try {
			bestellungFac
					.updateBestellung(bestellungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BestellungDto bestellungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		BestellungDto bestellungDto = null;
		try {
			bestellungDto = bestellungFac.bestellungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return bestellungDto;
	}

	public Integer createBestellposition(BestellpositionDto bestellpositionDto,
			String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern)
			throws ExceptionLP {
		Integer iIdBestellposition = null;
		try {
			iIdBestellposition = bestellpositionFac.createBestellposition(
					bestellpositionDto, LPMain.getTheClient(), sPreispflegeI,
					artikellieferantstaffelIId_ZuAendern);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iIdBestellposition;
	}

	/**
	 * L&ouml;schen einer bestehendenreat Bestellposition.
	 * 
	 * @param pPos
	 *            BestellpositionDto
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public void removeBestellposition(BestellpositionDto pPos)
			throws ExceptionLP {
		try {
			bestellpositionFac.removeBestellposition(pPos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * L&ouml;schen des ABTermin von Bestellposition.
	 * 
	 * @param pPos
	 *            BestellpositionDto
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public void removeABTerminVonBestellposition(BestellpositionDto pPos)
			throws ExceptionLP {
		try {
			bestellpositionFac.removeABTerminVonBestellposition(pPos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Holen einer Auftragsposition.
	 * 
	 * @param pKey
	 *            Integer
	 * @throws ExceptionLP
	 * @throws Throwable
	 * @return AuftragpositionDto
	 */
	public BestellpositionDto bestellpositionFindByPrimaryKey(Integer pKey)
			throws ExceptionLP {
		BestellpositionDto dto = null;
		try {
			dto = bestellpositionFac.bestellpositionFindByPrimaryKey(pKey);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}

	/**
	 * 
	 * @param bestellpositionDto
	 *            BestellpositionDto
	 * @param sPreispflegeI
	 *            String
	 * @throws ExceptionLP
	 */
	public void updateBestellposition(BestellpositionDto bestellpositionDto,
			String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern)
			throws ExceptionLP {

		try {
			bestellpositionFac.updateBestellposition(bestellpositionDto,
					LPMain.getTheClient(), sPreispflegeI,
					artikellieferantstaffelIId_ZuAendern);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * 
	 * @param bestellpositionDto
	 *            BestellpositionDto
	 * @throws ExceptionLP
	 */
	public void updateBestellpositionOhneWeitereAktion(
			BestellpositionDto bestellpositionDto) throws ExceptionLP {

		try {
			bestellpositionFac.updateBestellpositionOhneWeitereAktion(
					bestellpositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * 
	 * @param bestellpositionDto
	 *            BestellpositionDto
	 * @param sPreispflegeI
	 *            String
	 * @throws ExceptionLP
	 */
	public void updateBestellpositionMitABTermin(
			BestellpositionDto bestellpositionDto, String sPreispflegeI)
			throws ExceptionLP {
		try {
			bestellpositionFac.updateBestellpositionMitABTermin(
					bestellpositionDto, LPMain.getTheClient(), sPreispflegeI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBestellpositionNurABTermin(Integer bestellpositionIId,
			java.sql.Date abTerminNeu) throws ExceptionLP {
		try {
			bestellpositionFac.updateBestellpositionNurABTermin(
					bestellpositionIId, abTerminNeu, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBestellpositionNurLieferterminBestaetigt(
			Integer bestellpositionIId) throws ExceptionLP {
		try {
			bestellpositionFac.updateBestellpositionNurLieferterminBestaetigt(
					bestellpositionIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void setForAllPositionenABTermin(Integer bestellungIId,
			java.sql.Date abDatum, String abNummer, boolean selectAllOrEmpty)
			throws ExceptionLP {
		try {
			bestellpositionFac.setForAllPositionenABTermin(bestellungIId,
					abDatum, abNummer, selectAllOrEmpty, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void sortiereNachArtikelnummer(Integer bestellungIId)
			throws ExceptionLP {
		try {
			bestellpositionFac.sortiereNachArtikelnummer(bestellungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Eine Bestellung drucken. <br>
	 * Wenn die Bestellung das erste Mal gedruckt wird, werden ... <br>
	 * - die Werte der Bestellung berechnet <br>
	 * - der Status der Bestellung von angelegt auf offen gesetzt
	 * 
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @param iAnzahlKopienI
	 *            Integer
	 * @param bMitLogo
	 *            Boolean
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP[] printBestellung(Integer iIdBestellungI,
			Integer iAnzahlKopienI, Boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP oPrintO[] = null;
		try {
			oPrintO = bestellungReportFac.printBestellung(iIdBestellungI,
					iAnzahlKopienI, bMitLogo, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP printWepEtikett(Integer iIdWepI,
			Integer iIdBestellpositionI, Integer iIdLagerI, Integer iExemplare,
			Integer iVerpackungseinheit, Double dGewicht,
			String sWarenverkehrsnummer, String sLagerort,
			String sUrsprungsland, String sKommentar, BigDecimal bdHandmenge,
			Integer wePosIId)

	throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printWepEtikett(iIdWepI,
					iIdBestellpositionI, iIdLagerI, iExemplare,
					iVerpackungseinheit, dGewicht, sWarenverkehrsnummer,
					sLagerort, sUrsprungsland, sKommentar, bdHandmenge,
					wePosIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	/**
	 * Journal "Alle Bestellungen".
	 * 
	 * @param krit
	 *            PK der Bestellung
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printBestellungenAlle(ReportJournalKriterienDto krit)
			throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printBestellungenAlle(krit,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	/**
	 * Offene. Bestellungen drucken.
	 * 
	 * @param krit
	 *            PK der Bestellung
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
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
	 * @param auftragIId
	 *            Integer
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printBestellungenOffene(
			ReportJournalKriterienDto krit, java.sql.Date dStichtag,
			Boolean bSortierungNachLiefertermin, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung,
			Integer auftragIId, Integer iArt, boolean bNurAngelegte,
			boolean bNurOffeneMengenAnfuehren, Integer[] projekte)
			throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printBestellungOffene(krit,
					dStichtag, bSortierungNachLiefertermin, artikelklasseIId,
					artikelgruppeIId, artikelCNrVon, artikelCNrBis,
					projektCBezeichnung, auftragIId, iArt, bNurAngelegte,
					bNurOffeneMengenAnfuehren, projekte, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	/**
	 * Bestellvorschlag drucken.
	 * 
	 * @param krit
	 *            PK der Bestellung
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printBestellVorschlag(ReportJournalKriterienDto krit,
			Boolean bSortierungNachLiefertermin, boolean bAnfragevorschlag)
			throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printBestellVorschlag(krit,
					bSortierungNachLiefertermin, bAnfragevorschlag,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP printBestellungWareneingangsJournal(
			ReportJournalKriterienDto krit, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung,
			Integer auftragIId, boolean bMitWarenverbrauch) throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printBestellungWareneingangsJournal(
					krit, artikelklasseIId, artikelgruppeIId, artikelCNrVon,
					artikelCNrBis, projektCBezeichnung, auftragIId,
					bMitWarenverbrauch, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP printAbholauftrag(Integer bestellungIId)
			throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printAbholauftrag(bestellungIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP printGeaenderteArtikel() throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printGeaenderteArtikel(LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public void vertauscheBestellpositionMinus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			// int pageCount = 0 ;

			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			while (--rowIndex >= 0) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			bestellpositionFac.vertauscheBestellpositionenMinus(baseIId,
					iidList, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheBestellpositionPlus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			int maxRowCount = tableModel.getRowCount();
			while (++rowIndex < maxRowCount) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			bestellpositionFac
					.vertauscheBestellpositionenPlus(baseIId, iidList, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdBestellungI, int iSortierungNeuePositionI)
			throws ExceptionLP {
		try {
			bestellpositionFac
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							iIdBestellungI, iSortierungNeuePositionI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Alle Positionen zu einer Bestellung bestimmen.
	 * 
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @return BestellpositionDto[] alle Positionen
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public BestellpositionDto[] bestellpositionFindByBestellung(
			Integer iIdBestellungI) throws ExceptionLP {
		BestellpositionDto[] aPositionDtos = null;
		try {
			aPositionDtos = bestellpositionFac.bestellpositionFindByBestellung(
					iIdBestellungI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return aPositionDtos;
	}

	public String getPositionAsStringDocumentWS(Integer aIIdBSPOSI[])
			throws ExceptionLP {
		String sRet = null;
		try {
			sRet = bestellpositionFac.getPositionAsStringDocumentWS(aIIdBSPOSI,
					LPMain.getInstance().getCNrUser());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return sRet;
	}

	/**
	 * Ein Lieferschein kann manuell freigegeben werden. <br>
	 * Der Lieferschein muss sich im Status 'Angelegt' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void manuellFreigeben(Integer iIdLieferscheinI) throws ExceptionLP {
		try {
			bestellungFac.manuellFreigeben(iIdLieferscheinI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Bestellung kann manuell erledigt werden. <br>
	 * Bestellung muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdBestellungI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void manuellErledigen(Integer iIdBestellungI) throws ExceptionLP {
		try {
			bestellungFac.manuellErledigen(iIdBestellungI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Bestellposition auf vollstaendig geliefert setzen.
	 * 
	 * @param iIdBestellpositionI
	 *            PK der Bestellposition
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void bestellpositionManuellAufVollstaendigGeliefertSetzen(
			Integer iIdBestellpositionI) throws ExceptionLP {
		try {
			bestellpositionFac.manuellAufVollstaendigGeliefertSetzen(
					iIdBestellpositionI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Bestellung kann manuell erledigt werden. <br>
	 * Bestellung muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdBestellpositionI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void bestellpositionmanuellErledigungAufheben(
			Integer iIdBestellpositionI) throws ExceptionLP {
		try {
			bestellpositionFac.manuellErledigungAufheben(iIdBestellpositionI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine Bestellung stornieren. <br>
	 * Das bedeutet: - Den Status der Bestellung anpassen und die Stornodaten
	 * vermerken. - Die Bestellpositionen loeschen.
	 * 
	 * @param iIdBestellungI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void stornieren(Integer iIdBestellungI) throws ExceptionLP {
		try {
			bestellungFac.stornieren(iIdBestellungI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public boolean bIstKopfartikelEinesArtikelSets(Integer bestellpositionIId) {
		return bestellpositionFac
				.bIstKopfartikelEinesArtikelSets(bestellpositionIId);
	}

	/**
	 * Storno einer Bestellung aufheben.
	 * 
	 * @param iIdBestellungI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void stornoAufheben(Integer iIdBestellungI) throws ExceptionLP {
		try {
			bestellungFac.stornoAufheben(iIdBestellungI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Die Anzahl der mengebehafteten Positionen zu einer Bestellung berechnen.
	 * 
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @return int die Anzahl
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public int berechneAnzahlMengenbehafteteBestellpositionen(
			Integer iIdBestellungI) throws ExceptionLP {
		int iAnzahl = -1;

		try {
			iAnzahl = bestellpositionFac
					.berechneAnzahlMengenbehafteteBestellpositionen(
							iIdBestellungI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	/**
	 * @deprecated MB
	 * 
	 *             Status einer Bestellung ohne weitere Aktionen aendern.
	 * @param bestellungIId
	 *            PK der Bestellung
	 * @param sStatusI
	 *            der neue Status
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	// public void setzeBestellungstatus(Integer bestellungIId, String sStatusI)
	// throws ExceptionLP {
	// try {
	// bestellungFac.setzeBestellungstatus(bestellungIId, sStatusI,
	// LPMain.getTheClient());
	// }
	// catch (Throwable t) {
	// handleThrowable(t);
	// }
	// }
	/**
	 * @deprecated MB
	 * 
	 *             Wenn der allgemeine Rabatt in den Konditionen geaendert
	 *             wurde, dann werden im Anschluss die davon abhaengigen Werte
	 *             neu berechnet.
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateBestellungKonditionen(Integer iIdBestellungI)
			throws ExceptionLP {
		try {
			bestellungFac.updateBestellungKonditionen(iIdBestellungI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Den Gesamtwert der Bestellung in Bestellwaehrung berechnen.
	 * 
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @return BigDecimal der Bestellwert
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneNettowertGesamt(Integer iIdBestellungI)
			throws ExceptionLP {
		BigDecimal bdBestellwert = new BigDecimal(0);
		try {
			bdBestellwert = bestellungFac.berechneNettowertGesamt(
					iIdBestellungI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return bdBestellwert;
	}

	public Integer getAnzahlBestellpositionen(Integer iIdBestellungI)
			throws ExceptionLP {
		try {
			return bestellpositionFac
					.getAnzahlBestellpositionen(iIdBestellungI);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public ArrayList<Integer> getAngelegtenBestellungen(Integer lieferantIId)
			throws ExceptionLP {
		try {
			return bestellungFac.getAngelegtenBestellungen(lieferantIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Map<?, ?> getListeDerVerknuepftenBestellungen(
			Integer lossollmaterialIId) throws ExceptionLP {
		try {
			return bestellpositionFac.getListeDerVerknuepftenBestellungen(
					lossollmaterialIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public int getAnzahlMengenbehaftetBSPOS(Integer iIdBestellungI)
			throws ExceptionLP {
		int iRet = 0;
		try {
			iRet = bestellpositionFac.getAnzahlMengenbehaftetBSPOS(
					iIdBestellungI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iRet;
	}

	// /**
	// * Den Status einer Bestellung auf 'Offen' setzen und ihren Wert
	// berechnen.
	// * @param iIdBestellungI PK der Bestellung
	// * @throws ExceptionLP Ausnahme
	// */
	// public void aktiviereBestellung(Integer iIdBestellungI)
	// throws ExceptionLP {
	// try {
	// bestellungFac.aktiviereBestellung(iIdBestellungI, LPMain.getTheClient());
	// }
	// catch (Throwable t) {
	// handleThrowable(t);
	// }
	// }

	/**
	 * Eine Anfrage kann in 0..n Bestellungen aufscheinen. Hole alle
	 * Bestellungen zu einer Anfrage.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @return BestellungDto[] die Bestellungen
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public BestellungDto[] bestellungFindByAnfrage(Integer iIdAnfrageI)
			throws ExceptionLP {
		BestellungDto[] aBestellungDto = null;
		try {
			aBestellungDto = bestellungFac.bestellungFindByAnfrage(iIdAnfrageI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return aBestellungDto;
	}

	public Integer erzeugeBestellungAusAnfrage(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws ExceptionLP {
		Integer iId = null;

		try {
			iId = bestellungFac.erzeugeBestellungAusAnfrage(iIdAnfrageI,
					theClientDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer erzeugeBestellungAusBestellung(Integer iIdBSI)
			throws ExceptionLP {
		Integer iId = null;

		try {
			iId = bestellungFac.erzeugeBestellungAusBestellung(iIdBSI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	/**
	 * Methode zum Erzeugen einer Eingangsrechnung aus einer bestehenden
	 * Bestellung
	 * 
	 * @param iIdBestellungI
	 *            PK des bestehenden Auftrags
	 * @param eingangsrechnungDtoI
	 *            der Benutzer kann vorbelegte Eigenschaften uebersteuern
	 * @return Integer PK der neuen Eingangsrechnung
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeEingangsrechnungAusBestellung(Integer iIdBestellungI,
			EingangsrechnungDto eingangsrechnungDtoI) throws ExceptionLP {
		Integer iIdEingangsrechnung = null;
		try {
			iIdEingangsrechnung = bestellungFac
					.erzeugeEingangsrechnungAusBestellung(iIdBestellungI,
							eingangsrechnungDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iIdEingangsrechnung;
	}

	/**
	 * Den Status einer Bestellung von 'geliefert' auf 'offen' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'geliefert' Status manuell
	 * gesetzt wurde.
	 * 
	 * @param iIdBestellungI
	 *            PK des Bestells
	 * @throws ExceptionLP
	 */
	public void erledigungAufheben(Integer iIdBestellungI) throws ExceptionLP {
		try {
			bestellungFac.erledigenAufheben(iIdBestellungI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void importiereMonatsbestellung(
			ArrayList<ImportMonatsbestellungDto> importMonatbestellung)
			throws ExceptionLP {
		try {
			bestellungFac.importiereMonatsbestellung(importMonatbestellung,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Aufgrund des Divisors die Abrufpositionen fuer eine Abrufbestellung
	 * erzeugen.
	 * 
	 * @param iIdBestellungI
	 *            PK der Abrufbestellung
	 * @param iDivisorI
	 *            der Divisor
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void erzeugeAbrufpositionen(Integer iIdBestellungI, int iDivisorI)
			throws ExceptionLP {
		try {
			bestellpositionFac.erzeugeAbrufpositionen(iIdBestellungI,
					iDivisorI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Berechnet die gesamte offene Menge fuer eine Bestellung ueber alle
	 * Positionen. <br>
	 * Wird verwendet, um festzustellen, ob eine Rahmenbestellung erledigt ist.
	 * 
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @return int offene Menge
	 * @throws ExceptionLP
	 */
	public double berechneOffeneMenge(Integer iIdBestellungI)
			throws ExceptionLP {
		double dOffeneMenge = 0.0;

		try {
			dOffeneMenge = bestellpositionFac.berechneOffeneMenge(
					iIdBestellungI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dOffeneMenge;
	}

	/**
	 * Eine RahmenbestellungId kann in 0..n Bestellungen aufscheinen. Hole alle
	 * Abrufbestellungen zu einer Rahmenbestellung.
	 * 
	 * @param iIdRahmenBestellungI
	 *            Integer
	 * @return BestellungDto[]
	 * @throws ExceptionLP
	 */
	public BestellungDto[] abrufBestellungenfindByRahmenbestellung(
			Integer iIdRahmenBestellungI) throws ExceptionLP {
		BestellungDto[] aBestellungDtos = null;

		try {
			aBestellungDtos = bestellungFac
					.abrufBestellungenfindByRahmenbestellung(
							iIdRahmenBestellungI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aBestellungDtos;
	}

	/**
	 * Innerhalb einer Bestellung einer Bestellposition suchen, die eine
	 * bestimmte Bestellposition referenziert.
	 * 
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @param iIdBestellpositionRahmenpositionI
	 *            PK der referenzierten Bestellposition
	 * @return BestellpositionDto die gesuchte Bestellposition oder null
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public BestellpositionDto bestellpositionFindByBestellungIIdBestellpositionIIdRahmenposition(
			Integer iIdBestellungI, Integer iIdBestellpositionRahmenpositionI)
			throws ExceptionLP {
		BestellpositionDto bestellpositionDto = null;

		try {
			bestellpositionDto = bestellpositionFac
					.bestellpositionFindByBestellungIIdBestellpositionIIdRahmenposition(
							iIdBestellungI, iIdBestellpositionRahmenpositionI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return bestellpositionDto;
	}

	/**
	 * Ein Teil einer Rahmenposition oder die gesamte wird in der
	 * Abrufbestellung als Abrufposition erfasst. <br>
	 * Die erfasste Menge muss dabei > 0 sein.
	 * 
	 * @param abrufbestellpositionDtoI
	 *            die bestehenden Abrufposition
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAbrufbestellpositionSichtRahmen(
			BestellpositionDto abrufbestellpositionDtoI) throws ExceptionLP {
		try {
			bestellpositionFac.updateAbrufbestellpositionSichtRahmen(
					abrufbestellpositionDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Wenn eine Abrufposition geloescht wird, muss die entsprechende
	 * Rahmenposition angepasst werden.
	 * 
	 * @param abrufbestellpositionDtoI
	 *            die aktuelle Abrufbestellposition
	 * @throws ExceptionLP
	 */
	public void removeAbrufbestellposition(
			BestellpositionDto abrufbestellpositionDtoI) throws ExceptionLP {
		try {
			bestellpositionFac.removeAbrufbestellposition(
					abrufbestellpositionDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void preispflege(BestellpositionDto besPosDto, String sPreispflegeI,
			Integer artikellieferantstaffelIId_ZuAendern) throws ExceptionLP {
		try {
			bestellpositionFac
					.preispflege(besPosDto, sPreispflegeI,
							artikellieferantstaffelIId_ZuAendern,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Wenn eine neue Abrufposition angelegt wird, muss die entsprechende
	 * Rahmenposition angepasst werden.
	 * 
	 * @param abrufbestellpositionDtoI
	 *            die aktuelle Abrufbestellposition
	 * @return Integer PK der neuen Abrufposition
	 * @throws ExceptionLP
	 */
	public Integer createAbrufbestellposition(
			BestellpositionDto abrufbestellpositionDtoI, String sPreispflegeI,
			Integer artikellieferantstaffelIId_ZuAendern) throws ExceptionLP {
		Integer abrufpositionIId = null;

		try {
			abrufpositionIId = bestellpositionFac
					.createAbrufbestellposition(abrufbestellpositionDtoI,
							sPreispflegeI,
							artikellieferantstaffelIId_ZuAendern,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return abrufpositionIId;
	}

	/**
	 * Eine Abrufbestellposition wird korrigiert. Damit mussen auch die Mengen
	 * in der Rahmenbestellung angepasst werden.
	 * 
	 * @param abrufbestellpositionDtoI
	 *            die Abrufbestellposition mit den aktuellen Werten
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAbrufbestellposition(
			BestellpositionDto abrufbestellpositionDtoI, String sPreispflegeI,
			Integer artikellieferantstaffelIId_ZuAendern) throws ExceptionLP {
		try {
			bestellpositionFac
					.updateAbrufbestellposition(abrufbestellpositionDtoI,
							sPreispflegeI,
							artikellieferantstaffelIId_ZuAendern,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Alle Abrufpositionen holen, die sich auf eine bestimmte Rahmenposition
	 * beziehen.
	 * 
	 * @param iIdRahmenpositionI
	 *            PK der Rahmenposition
	 * @return BestellpositionDto[] die Abrufpositionen
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public BestellpositionDto[] bestellpositionFindByBestellpositionIIdRahmenposition(
			Integer iIdRahmenpositionI) throws ExceptionLP {
		BestellpositionDto[] aBestellposition = null;

		try {
			aBestellposition = bestellpositionFac
					.bestellpositionFindByBestellpositionIIdRahmenposition(
							iIdRahmenpositionI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aBestellposition;
	}

	public void refreshStatusWennAbgerufen(Integer iBestellungId)
			throws ExceptionLP {

		try {
			bestellpositionFac.refreshStatusWennAbgerufen(iBestellungId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public void checkStatusAbrufBestellungenUndRahmenbestellung(
			BestellpositionDto bestellpositionDtoI) throws ExceptionLP {

		try {
			bestellpositionFac.checkStatusAbrufBestellungenUndRahmenbestellung(
					bestellpositionDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	// /**
	// * @deprecated MB
	// *
	// * updated eine Bestellung ohne weitere Aktionen zb. Statusaenderung
	// erfolgt
	// * hier nicht
	// *
	// * @param bestellungDto BestellungDto
	// * @throws ExceptionLP
	// */
	// public void updateBestellungOhneWeitereAktionen(BestellungDto
	// bestellungDto)
	// throws ExceptionLP {
	// try {
	// bestellungFac.updateBestellungOhneWeitereAktionen(bestellungDto,
	// LPMain.getTheClient());
	// }
	// catch (Throwable ex) {
	// handleThrowable(ex);
	// }
	//
	// }

	public void setzeBSMahnsperre(Integer bestellungIId,
			java.sql.Date tMahnsperre) throws ExceptionLP {
		try {
			bestellungFac.setzeBSMahnsperre(bestellungIId, tMahnsperre,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * <br>
	 * Wenn die Bestellung das erste Mal gedruckt wird, werden ... <br>
	 * - die Werte der Bestellung berechnet <br>
	 * - der Status der Bestellung von angelegt auf offen gesetzt
	 * 
	 * @param mahnungIId
	 *            PK der Bestellung
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printBSMahnungAusMahnlauf(Integer mahnungIId,
			boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printBSMahnungAusMahnlauf(mahnungIId,
					bMitLogo, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP printBSMahnungFuerBestellung(
			Integer bestellpositionIId, Integer bestellungIId,
			Integer mahnstufeIId, java.sql.Date dMahndatum, boolean bMitLogo)
			throws ExceptionLP {

		JasperPrintLP oPrintO = null;
		try {
			oPrintO = bestellungReportFac.printBSMahnungFuerBestellung(
					bestellpositionIId, bestellungIId, mahnstufeIId,
					dMahndatum, bMitLogo, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public JasperPrintLP[] printBSSammelMahnung(Integer bsmahnlaufIId,
			boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP oPrintO[] = null;
		try {
			oPrintO = bestellungReportFac.printBSSammelMahnung(bsmahnlaufIId,
					bMitLogo, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrintO;
	}

	public boolean isBSErledigt(Integer iIdBSPOSI) throws ExceptionLP {

		Boolean bIsErledigt = null;
		try {
			bIsErledigt = bestellungFac.isBSErledigt(iIdBSPOSI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bIsErledigt.booleanValue();
	}

	public boolean isBSGeliefert(Integer iIdBSPOSI) throws ExceptionLP {

		Boolean bIsGeliefert = null;
		try {
			bIsGeliefert = bestellungFac.isBSGeliefert(iIdBSPOSI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bIsGeliefert.booleanValue();
	}

	public BigDecimal berechneOffeneMengePosition(Integer iIdBSPOSI)
			throws ExceptionLP {

		BigDecimal bdOffeneMenge = null;
		try {
			bdOffeneMenge = bestellpositionFac.berechneOffeneMenge(iIdBSPOSI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bdOffeneMenge;
	}

	public void erledigeAlleNichtMengenpositionen(BestellungDto bestellungDto)
			throws ExceptionLP {
		try {
			bestellpositionFac.erledigeAlleNichtMengenpositionen(bestellungDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public String getRichtigenBestellStatus(Integer bestellungIId,
			boolean bRekursiv) throws ExceptionLP {
		String sReturnVal = "";
		try {
			sReturnVal = bestellungFac.getRichtigenBestellStatus(bestellungIId,
					bRekursiv, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return sReturnVal;
	}

	public String getRichtigenBestellpositionStatus(Integer bestellPosIId)
			throws ExceptionLP {
		String sReturnVal = "";
		try {
			sReturnVal = bestellpositionFac.getRichtigenBestellpositionStatus(
					bestellPosIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return sReturnVal;
	}

	public String checkBestellStati() throws ExceptionLP {
		String retVal = "";
		try {
			retVal = bestellungFac.checkBestellStati(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return retVal;
	}

	public String checkBestellpositionStati() throws ExceptionLP {
		String retVal = "";
		try {
			retVal = bestellpositionFac.checkBestellpositionStati(LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return retVal;
	}

	public void sendMahnlauf(String cKommuniaktionsart,
			BSMahnlaufDto bsMahnlaufDto) throws EJBExceptionLP, Throwable {
		try {
			TheClientDto theClientDto = LPMain.getTheClient();

			bestellungReportFac.sendMahnlauf(cKommuniaktionsart, bsMahnlaufDto,
					theClientDto.getLocUi(), theClientDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Integer getPositionNummer(Integer bestellpositionIId)
			throws ExceptionLP {
		Integer iPosNummer = null;
		try {
			iPosNummer = bestellpositionFac.getPositionNummer(
					bestellpositionIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iPosNummer;
	}

	public Integer getPositionNummerReadOnly(Integer bestellpositionIId)
			throws ExceptionLP {
		Integer iPosNummer = null;
		try {
			iPosNummer = bestellpositionFac
					.getPositionNummerReadOnly(bestellpositionIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iPosNummer;
	}

	public void setzeVersandzeitpunktAufJetzt(Integer iBestellungIId,
			String sDruckart) throws ExceptionLP {
		try {
			bestellungFac.setzeVersandzeitpunktAufJetzt(iBestellungIId,
					sDruckart);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void setAuftragIIdInBestellung(BestellungDto bestellungDto)
			throws ExceptionLP {
		try {
			bestellungFac.setAuftragIIdInBestellung(bestellungDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
}
