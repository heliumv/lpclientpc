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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungkontierungDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.rechnung.service.ReportRechnungJournalKriterienDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * <I>BusinessDelegate zum Rechnungsmodul</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.55 $
 */
public class RechnungDelegate extends Delegate {
	private Context context;
	private RechnungFac rechnungFac;
	private RechnungReportFac rechnungReportFac;

	public RechnungDelegate() throws ExceptionLP {
		try {
			// @Todo: Facades nur bei gebrauch wenn null instanzieren anstatt im
			// Konstruktor
			context = new InitialContext();
			rechnungFac = (RechnungFac) context
					.lookup("lpserver/RechnungFacBean/remote");
			rechnungReportFac = (RechnungReportFac) context
					.lookup("lpserver/RechnungReportFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Speichern einer Rechnung.
	 * 
	 * @param rechnungDto
	 *            die zu speichernde Rechnung
	 * @throws ExceptionLP
	 * @return RechnungDto
	 */
	// delegateexc: 0 nur ExceptionLP werfen!!!
	public RechnungDto updateRechnung(RechnungDto rechnungDto)
			throws ExceptionLP {
		RechnungDto rechnungDto2Return = null;
		try {
			if (rechnungDto.getIId() != null) {
				rechnungDto.setTAendern(new Timestamp(System
						.currentTimeMillis()));
				rechnungDto2Return = rechnungFac.updateRechnung(rechnungDto,
						LPMain.getTheClient());
			} else {
				rechnungDto2Return = rechnungFac.createRechnung(rechnungDto,
						LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			// delegateexc: 1 so fange ich Exceptions vom server und verarbeite
			// sie
			handleThrowable(ex);
		}
		return rechnungDto2Return;
	}

	public void updateRechnungStatus(Integer rechnungIId, String statusNeu,
			java.sql.Date bezahltdatum) throws ExceptionLP {
		try {
			rechnungFac.updateRechnungStatus(rechnungIId, statusNeu,
					bezahltdatum);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateRechnungVertreter(Integer rechnungIId,
			Integer personalIIdVertreter_Neu) throws ExceptionLP {
		try {
			rechnungFac.updateRechnungVertreter(rechnungIId,
					personalIIdVertreter_Neu, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateRechnungZahlungsplan(Integer rechnungIId,
			BigDecimal bdZahlbetrag, Integer iZahltag) throws ExceptionLP {
		try {
			rechnungFac.updateRechnungZahlungsplan(rechnungIId, bdZahlbetrag,
					iZahltag);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Finden einer Rechnung anhand Primaerschluessel.
	 * 
	 * @param iId
	 *            Integer
	 * @throws ExceptionLP
	 * @return RechnungDto
	 */
	public RechnungDto rechnungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungDto findRechnungOderGutschriftByCNr(String cNr,
			String rechnungsart) throws ExceptionLP {
		try {
			RechnungDto[] array = rechnungFac
					.rechnungFindByCNrMandantCNrOhneExc(cNr, LPMain
							.getTheClient().getMandant());
			if (array == null)
				return null;
			for (RechnungDto dto : array) {
				if (dto.getRechnungartCNr().equals(rechnungsart))
					return dto;
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public RechnungDto rechnungFindByCNr(String cNr) throws ExceptionLP {
		return findRechnungOderGutschriftByCNr(cNr,
				RechnungFac.RECHNUNGTYP_RECHNUNG);
	}

	public RechnungDto gutschriftFindByCNr(String cNr) throws ExceptionLP {
		return findRechnungOderGutschriftByCNr(cNr,
				RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
	}

	public void removeRechnungPosition(RechnungPositionDto rechnungPositionDto)
			throws ExceptionLP {
		try {
			rechnungFac.removeRechnungPosition(rechnungPositionDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public RechnungPositionDto updateRechnungPosition(
			RechnungPositionDto rechnungPositionDto, Integer lagerIId)
			throws ExceptionLP {
		try {
			if (rechnungPositionDto.getIId() != null) {
				return rechnungFac.updateRechnungPosition(rechnungPositionDto,
						LPMain.getTheClient());
			} else {
				return rechnungFac.createRechnungPosition(rechnungPositionDto,
						lagerIId, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto updateRechnungPosition(
			RechnungPositionDto rechnungPositionDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities) throws ExceptionLP {
		try {
			if (rechnungPositionDto.getIId() != null) {
				return rechnungFac.updateRechnungPosition(rechnungPositionDto,
						identities, LPMain.getTheClient());
			} else {
				return rechnungFac.createRechnungPosition(rechnungPositionDto,
						lagerIId, identities, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto updateRechnungPosition(
			RechnungPositionDto rechnungPositionDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities, Artikelset artikelset)
			throws ExceptionLP {
		try {
			if (rechnungPositionDto.getIId() != null) {
				return rechnungFac.updateRechnungPosition(rechnungPositionDto,
						identities, artikelset, LPMain.getTheClient());
			} else {
				return rechnungFac.createRechnungPosition(rechnungPositionDto,
						lagerIId, identities, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto createRechnungposition(RechnungPositionDto pDto,
			Integer lagerIId) throws ExceptionLP {
		try {
			return rechnungFac.createRechnungPosition(pDto, lagerIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public RechnungPositionDto rechnungPositionFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return rechnungFac.rechnungPositionFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto rechnungPositionFindByLieferscheinIId(
			Integer lieferscheinIId) throws ExceptionLP {
		try {
			return rechnungFac
					.rechnungPositionFindByLieferscheinIId(lieferscheinIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto[] rechnungPositionFindByRechnungIId(
			Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungPositionFindByRechnungIId(rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Rechnung drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param pKey
	 *            Integer
	 * @param druckLocale
	 *            Locale
	 * @param bMitLogo
	 *            Boolean
	 * @param iAnzahlKopien
	 *            Integer
	 */
	public JasperPrintLP[] printRechnung(Integer pKey, Locale druckLocale,
			Boolean bMitLogo, Integer iAnzahlKopien) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP[] print = null;
		try {
			print = this.rechnungReportFac.printRechnung(pKey, druckLocale,
					bMitLogo, iAnzahlKopien, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	public JasperPrintLP printRechnungAlsMahnung(Integer pKey,
			Integer iMahnstufe, Locale druckLocale, Boolean bMitLogo)
			throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printRechnungAlsMahnung(pKey,
					druckLocale, bMitLogo, iMahnstufe, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Proformarechnung drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param pKey
	 *            Integer
	 * @param druckLocale
	 *            Locale
	 * @param bMitLogo
	 *            Boolean
	 * @param iAnzahlKopien
	 *            Integer
	 */
	public JasperPrintLP[] printProformarechnung(Integer pKey,
			Locale druckLocale, Boolean bMitLogo, Integer iAnzahlKopien)
			throws ExceptionLP {
		JasperPrintLP[] print = null;
		try {
			print = this.rechnungReportFac
					.printProformarechnung(pKey, druckLocale, bMitLogo,
							iAnzahlKopien, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Umsatz aller Rechnungen drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param iGeschaeftsjahr
	 *            Integer
	 * @param bMitGutschriften
	 *            Boolean
	 */
	public JasperPrintLP printRechnungenUmsatz(Integer iGeschaeftsjahr,
			Boolean bMitGutschriften) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printRechnungenUmsatz(
					LPMain.getTheClient(), iGeschaeftsjahr, bMitGutschriften);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Offene Rechnungen drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param krit
	 *            Date
	 */
	public JasperPrintLP printRechnungenOffene(
			ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printRechnungenOffene(krit,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Gutschrift drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param pKey
	 *            Integer
	 * @param druckLocale
	 *            Locale
	 * @param bMitLogo
	 *            Boolean
	 * @param iAnzahlKopien
	 *            Integer
	 */
	public JasperPrintLP[] printGutschrift(Integer pKey, Locale druckLocale,
			Boolean bMitLogo, Integer iAnzahlKopien) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP[] print = null;
		try {
			print = this.rechnungReportFac.printGutschrift(pKey, druckLocale,
					bMitLogo, iAnzahlKopien, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Alle Gutschriften drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param krit
	 *            Date
	 */
	public JasperPrintLP printGutschriftenAlle(
			ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printGutschriftenAlle(krit,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Alle Rechnungen drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param krit
	 *            Date
	 */
	public JasperPrintLP printRechnungenAlle(
			ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printRechnungenAlle(krit,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	public JasperPrintLP printZusammenfassendeMeldung(java.sql.Date dVon,
			java.sql.Date dBis, Integer partnerIIdFinanzamt) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printZusammenfassendeMeldung(dVon,
					dBis, partnerIIdFinanzamt, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	public JasperPrintLP printWarenausgangsjournal(
			ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		JasperPrintLP print = null;
		myLogger.entry();
		try {
			print = this.rechnungReportFac.printWarenausgangsjournal(krit,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Alle Proformarechnungen drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param krit
	 *            Date
	 */
	public JasperPrintLP printProformarechnungenAlle(
			ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printProformarechnungenAlle(krit,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Status einer bestehenden Rechnung aendern.
	 * 
	 * @param rechnungDto
	 *            RechnungDto
	 * @param pStatus
	 *            String
	 * @throws ExceptionLP
	 */
	public void setRechnungstatus(RechnungDto rechnungDto, String pStatus)
			throws ExceptionLP {
		myLogger.entry();
		rechnungDto.setStatusCNr(pStatus);

		try {
			this.updateRechnung(rechnungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public RechnungzahlungDto zahlungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return rechnungFac.zahlungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void createMonatsrechnungen() throws ExceptionLP {
		try {
			rechnungFac.createMonatsrechnungen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	/**
	 * Eine Rechnung aus einem Lieferschein erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param lieferscheinIId
	 *            Integer
	 * @param rechnungDto
	 *            RechnungDto
	 * @throws ExceptionLP
	 */
	public Integer createRechnungAusLieferschein(Integer lieferscheinIId,
			RechnungDto rechnungDto, String rechnungstypCNr,
			java.sql.Date neuDatum) throws ExceptionLP {
		try {
			if (lieferscheinIId != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				LieferscheinDto lsDto = DelegateFactory.getInstance()
						.getLsDelegate()
						.lieferscheinFindByPrimaryKey(lieferscheinIId);

				hmKunden.put(lsDto.getKundeIIdLieferadresse(),
						lsDto.getKundeIIdLieferadresse());

				if (!hmKunden.containsKey(lsDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(lsDto.getKundeIIdRechnungsadresse(),
							lsDto.getKundeIIdRechnungsadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate()
							.pruefeKunde(it.next());
				}

			}

			return rechnungFac.createRechnungAusLieferschein(lieferscheinIId,
					rechnungDto, rechnungstypCNr, neuDatum,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createRechnungAusRechnung(Integer rechnungIId,
			java.sql.Date neuDatum, boolean bUebernimmKonditionenDesKunden)
			throws ExceptionLP {
		try {
			if (rechnungIId != null) {
				RechnungDto reDto = rechnungFindByPrimaryKey(rechnungIId);

				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(reDto.getKundeIId());

			}

			return rechnungFac.createRechnungAusRechnung(rechnungIId, neuDatum,
					bUebernimmKonditionenDesKunden, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createRechnungAusAngebot(Integer angebotIId,
			java.sql.Date neuDatum) throws ExceptionLP {
		try {
			if (angebotIId != null) {
				AngebotDto agDto = DelegateFactory.getInstance()
						.getAngebotDelegate()
						.angebotFindByPrimaryKey(angebotIId);

				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(agDto.getKundeIIdAngebotsadresse());

			}

			return rechnungFac.createRechnungAusAngebot(angebotIId, neuDatum,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Rechnung aus mehrere Lieferschein gleiche Rechnungsadresse
	 * erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param lieferscheinIId
	 *            Integer
	 * @param rechnungDto
	 *            RechnungDto
	 * @throws ExceptionLP
	 */
	public Integer createRechnungAusMehrereLieferscheine(Object[] keys,
			RechnungDto rechnungDto, String rechnungstypCNr,
			java.sql.Date neuDatum) throws ExceptionLP {
		try {
			HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();
			for (int i = 0; i < keys.length; i++) {
				if (keys[i] != null) {

					LieferscheinDto lsDto = DelegateFactory.getInstance()
							.getLsDelegate()
							.lieferscheinFindByPrimaryKey((Integer) keys[i]);
					if (!hmKunden.containsKey(lsDto.getKundeIIdLieferadresse())) {
						hmKunden.put(lsDto.getKundeIIdLieferadresse(),
								lsDto.getKundeIIdLieferadresse());
					}

					if (!hmKunden.containsKey(lsDto
							.getKundeIIdRechnungsadresse())) {
						hmKunden.put(lsDto.getKundeIIdRechnungsadresse(),
								lsDto.getKundeIIdRechnungsadresse());
					}

				}
			}

			Iterator<Integer> it = hmKunden.keySet().iterator();

			while (it.hasNext()) {
				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(it.next());
			}

			return rechnungFac.createRechnungAusMehrereLieferscheine(keys,
					rechnungDto, rechnungstypCNr, neuDatum,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Rechnung aus einem Auftrag erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param auftragIId
	 *            Integer
	 * @throws ExceptionLP
	 */
	public Integer createRechnungAusAuftrag(Integer auftragIId,
			java.sql.Date neuDatum) throws ExceptionLP {
		try {

			if (auftragIId != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				AuftragDto abDto = DelegateFactory.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey(auftragIId);

				hmKunden.put(abDto.getKundeIIdAuftragsadresse(),
						abDto.getKundeIIdAuftragsadresse());
				if (!hmKunden.containsKey(abDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(abDto.getKundeIIdRechnungsadresse(),
							abDto.getKundeIIdRechnungsadresse());
				}

				if (!hmKunden.containsKey(abDto.getKundeIIdLieferadresse())) {
					hmKunden.put(abDto.getKundeIIdLieferadresse(),
							abDto.getKundeIIdLieferadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate()
							.pruefeKunde(it.next());
				}

			}

			return rechnungFac.createRechnungAusAuftrag(auftragIId, neuDatum,
					rabattAusRechnungsadresse(auftragIId),
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine GS aus einer RE erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param rechnungIId
	 *            Integer
	 * @throws ExceptionLP
	 */
	public Integer createGutschriftAusRechnung(Integer rechnungIId,
			java.sql.Date dBelegdatum) throws ExceptionLP {
		try {
			return rechnungFac.createGutschriftAusRechnung(rechnungIId,
					dBelegdatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Den Status einer Rechnung auf "Angelegt" setzen.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @throws ExceptionLP
	 */
	public void setRechnungStatusAufAngelegt(Integer rechnungIId)
			throws ExceptionLP {
		try {
			rechnungFac.setRechnungStatusAufAngelegt(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Rechnung holen.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param zahlungIIdAusgenommen
	 *            Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBereitsBezahltWertVonRechnungFw(Integer rechnungIId,
			Integer zahlungIIdAusgenommen) throws ExceptionLP {
		try {
			return rechnungFac.getBereitsBezahltWertVonRechnungFw(rechnungIId,
					zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungFw(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return rechnungFac.getAnzahlungenZuSchlussrechnungFw(rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungUstFw(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return rechnungFac
					.getAnzahlungenZuSchlussrechnungUstFw(rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Rechnung holen.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param zahlungIIdAusgenommen
	 *            Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(
			Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws ExceptionLP {
		try {
			return rechnungFac
					.getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(
							rechnungIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Rechnung holen.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param zahlungIIdAusgenommen
	 *            Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBereitsBezahltWertVonRechnung(Integer rechnungIId,
			Integer zahlungIIdAusgenommen) throws ExceptionLP {
		try {
			return rechnungFac.getBereitsBezahltWertVonRechnung(rechnungIId,
					zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Rechnung holen.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param zahlungIIdAusgenommen
	 *            Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBereitsBezahltWertVonRechnungUstFw(
			Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws ExceptionLP {
		try {
			return rechnungFac.getBereitsBezahltWertVonRechnungUstFw(
					rechnungIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Zahlung anlegen.
	 * 
	 * @param zahlungDto
	 *            Integer
	 * @param bErledigt
	 *            boolean
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public RechnungzahlungDto createZahlung(RechnungzahlungDto zahlungDto,
			boolean bErledigt) throws ExceptionLP {
		try {
			return rechnungFac.createZahlung(zahlungDto, bErledigt,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			if (ex instanceof EJBExceptionLP)
				if (((EJBExceptionLP) ex).getCode() == EJBExceptionLP.FEHLER_RECHNUNG_BEREITS_BEZAHLT) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("rechnung.zahlung.rechnungistbereitsbezahlt"));
					return null;
				}
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Zahlung anlegen.
	 * 
	 * @param zahlungDto
	 *            Integer
	 * @param bErledigt
	 *            boolean
	 * @throws ExceptionLP
	 */
	public void updateZahlung(RechnungzahlungDto zahlungDto, boolean bErledigt)
			throws ExceptionLP {
		try {
			rechnungFac.updateZahlung(zahlungDto, bErledigt,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Eine Zahlung loeschen.
	 * 
	 * @param zahlungDto
	 *            Integer
	 * @throws ExceptionLP
	 */
	public void removeZahlung(RechnungzahlungDto zahlungDto) throws ExceptionLP {
		try {
			rechnungFac.removeZahlung(zahlungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void mahneRechnung(Integer rechnungIId, Integer mahnstufeIId,
			java.sql.Date dMahndatum) throws ExceptionLP {
		try {
			rechnungFac.mahneRechnung(rechnungIId, mahnstufeIId, dMahndatum,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public boolean setzeMahnstufeZurueck(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return rechnungFac.setzeMahnstufeZurueck(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	public JasperPrintLP printZahlungsjournal(int iSortierung,
			java.sql.Date dVon, java.sql.Date dBis, Integer bankverbindungIId,
			boolean bSortierungNachKostenstelle) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = rechnungReportFac.printZahlungsjournal(
					LPMain.getTheClient(), iSortierung, dVon, dBis,
					bankverbindungIId, bSortierungNachKostenstelle);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public void vertauscheRechnungspositionMinus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			// int pageCount = 0 ;

			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			while (--rowIndex >= 0) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			rechnungFac.vertauscheRechnungspositionenMinus(baseIId, iidList,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheRechnungspositionPlus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			int maxRowCount = tableModel.getRowCount();
			while (++rowIndex < maxRowCount) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			rechnungFac.vertauscheRechnungspositionenPlus(baseIId, iidList,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Boolean hatRechnungPositionen(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return rechnungFac.hatRechnungPositionen(rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Warenausgangsjournal exportieren.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param kundeIId
	 *            Integer
	 * @param von
	 *            Date
	 * @param bis
	 *            Date
	 * @param iSortierung
	 *            Integer
	 */
	public String exportWAJournal(Integer kundeIId, java.sql.Date von,
			java.sql.Date bis, Integer iSortierung) throws ExceptionLP {
		myLogger.entry();
		String sText = null;
		try {
			sText = this.rechnungReportFac.exportWAJournal(
					LPMain.getTheClient(), kundeIId, von, bis,
					System.getProperty("line.separator"), iSortierung);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return sText;
	}

	public boolean gibtEsBereitsEineSchlussrechnungZuEinemAuftrag(
			Integer auftragIId) throws ExceptionLP {

		try {
			return this.rechnungFac
					.gibtEsBereitsEineSchlussrechnungZuEinemAuftrag(auftragIId,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}

	}

	public void erstelleEinzelexport(Integer rechnungIId, String pfad,
			boolean bSortiertNachArtikelnummer) throws ExceptionLP {
		try {
			this.rechnungReportFac.erstelleEinzelexport(rechnungIId, pfad,
					bSortiertNachArtikelnummer, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Eine Rechnung manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdRechnungI
	 *            PK der Rechnung
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void manuellErledigen(Integer iIdRechnungI) throws ExceptionLP {
		try {
			rechnungFac.manuellErledigen(iIdRechnungI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void setzeMahnsperre(Integer rechnungIId, java.sql.Date tMahnsperre)
			throws ExceptionLP {
		try {
			rechnungFac.setzeMahnsperre(rechnungIId, tMahnsperre,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void sortiereNachLieferscheinAnsprechpartner(Integer rechnungIId)
			throws ExceptionLP {
		try {
			rechnungFac.sortiereNachLieferscheinAnsprechpartner(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void sortiereNachLieferscheinNummer(Integer rechnungIId)
			throws ExceptionLP {
		try {
			rechnungFac.sortiereNachLieferscheinNummer(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer getISortUmLieferscheinNachAnsprechpartnerEinzusortieren(
			Integer rechnungIId, Integer lieferscheinIId) throws ExceptionLP {
		try {
			return rechnungFac
					.getISortUmLieferscheinNachAnsprechpartnerEinzusortieren(
							rechnungIId, lieferscheinIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Den Status einer AR von 'Erledigt' auf 'Erfasst' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param rechnungIId
	 *            PK der AR
	 * @throws ExceptionLP
	 */
	public void erledigungAufheben(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.erledigungAufheben(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void aktiviereBelegControlled(Integer iid, Timestamp t) throws ExceptionLP {
		try {
			rechnungFac.aktiviereBelegControlled(iid, t, LPMain.getTheClient());
			// SP1881
			DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
							LocaleFac.BELEGART_RECHNUNG, iid);
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
	}
	
	public Timestamp berechneBelegControlled(Integer iid) throws ExceptionLP {
		try {
			Timestamp t = rechnungFac.berechneBelegControlled(iid, LPMain.getTheClient());
			// SP1881
			DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
							LocaleFac.BELEGART_RECHNUNG, iid);
			return t;
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public void storniereRechnung(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.storniereRechnung(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void storniereRechnungRueckgaengig(Integer rechnungIId)
			throws ExceptionLP {
		try {
			rechnungFac.storniereRechnungRueckgaengig(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BigDecimal getUmsatzVomKundenImZeitraum(Integer kundeIId,
			java.sql.Date dVon, java.sql.Date dBis, boolean bStatistikadresse)
			throws ExceptionLP {
		BigDecimal bdUmsatz = null;
		try {
			bdUmsatz = rechnungFac.getUmsatzVomKundenImZeitraum(
					LPMain.getTheClient(), kundeIId, dVon, dBis,
					bStatistikadresse);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bdUmsatz;
	}

	public BigDecimal getUmsatzVomKundenHeuer(Integer kundeIId,
			boolean bStatistikadresse) throws ExceptionLP {
		BigDecimal bdUmsatz = null;
		try {
			bdUmsatz = rechnungFac.getUmsatzVomKundenHeuer(
					LPMain.getTheClient(), kundeIId, bStatistikadresse);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bdUmsatz;
	}

	public BigDecimal getUmsatzVomKundenVorjahr(Integer kundeIId,
			boolean bStatistikadresse) throws ExceptionLP {
		BigDecimal bdUmsatz = null;
		try {
			bdUmsatz = rechnungFac.getUmsatzVomKundenVorjahr(
					LPMain.getTheClient(), kundeIId, bStatistikadresse);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bdUmsatz;
	}

	public Integer getAnzahlDerRechnungenVomKundenImZeitraum(Integer kundeIId,
			java.sql.Date dVon, java.sql.Date dBis, boolean bStatistikadresse)
			throws ExceptionLP {
		Integer iAnzahl = null;
		try {
			iAnzahl = rechnungFac.getAnzahlDerRechnungenVomKundenImZeitraum(
					LPMain.getTheClient(), kundeIId, dVon, dBis,
					bStatistikadresse);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	public Integer getAnzahlDerRechnungenVomKundenHeuer(Integer kundeIId,
			boolean bStatistikadresse) throws ExceptionLP {
		Integer iAnzahl = null;
		try {
			iAnzahl = rechnungFac.getAnzahlDerRechnungenVomKundenHeuer(
					LPMain.getTheClient(), kundeIId, bStatistikadresse);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	public Integer getZahlungsmoraleinesKunden(Integer kundeIId,
			boolean bStatistikadresse) throws ExceptionLP {
		Integer iAnzahl = null;
		try {
			iAnzahl = rechnungFac.getZahlungsmoraleinesKunden(kundeIId,
					bStatistikadresse, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	public Integer getAnzahlDerRechnungenVomKundenVorjahr(Integer kundeIId,
			boolean bStatistikadresse) throws ExceptionLP {
		Integer iAnzahl = null;
		try {
			iAnzahl = rechnungFac.getAnzahlDerRechnungenVomKundenVorjahr(
					LPMain.getTheClient(), kundeIId, bStatistikadresse);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	public void removeRechnungkontierung(
			RechnungkontierungDto rechnungkontierungDto) throws ExceptionLP {
		try {
			rechnungFac.removeRechnungkontierung(rechnungkontierungDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public RechnungkontierungDto updateRechnungkontierung(
			RechnungkontierungDto rechnungkontierungDto) throws ExceptionLP {
		try {
			if (rechnungkontierungDto.getIId() == null) {
				return rechnungFac.createRechnungkontierung(
						rechnungkontierungDto, LPMain.getTheClient());
			} else {
				return rechnungFac.updateRechnungkontierung(
						rechnungkontierungDto, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungkontierungDto rechnungkontierungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return rechnungFac.rechnungkontierungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Berechnen, wieviele Prozent einer AR bereits kontiert sind
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @return BigDecimal
	 * @throws ExceptionLP
	 */
	public BigDecimal getProzentsatzKontiert(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return rechnungFac.getProzentsatzKontiert(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createRechnungenAusWiederholungsauftrag(Integer auftragIId,
			java.sql.Date dNeuDatum) throws ExceptionLP {
		try {
			return rechnungFac.createRechnungenAusWiederholungsauftrag(
					auftragIId, dNeuDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<Integer> getAngelegteRechnungen() throws ExceptionLP {
		try {
			return rechnungFac.getAngelegteRechnungen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Bei einer auftragbezogenen Rechnung ist es moeglich, all jene offenen
	 * oder teilerledigten Auftragpositionen innerhalb einer Transaktion zu
	 * uebernehmen, die keine Benutzerinteraktion benoetigen. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Handeingabepositionen werden uebernommen.
	 * <li>Nicht Serien- oder Chargennummertragende Artikelpositionen werden mit
	 * jener Menge uebernommen, die auf Lager liegt.
	 * <li>Artikelpositionen mit Seriennummer werden nicht uebernommen.
	 * <li>Artikelpositionen mit Chargennummer werden mit jener Menge
	 * uebernommen, die auf Lager liegt, wenn es genau eine Charge gibt.
	 * </ul>
	 * Die restlichen Positionen koennen nicht automatisch uebernommen werden.
	 * 
	 * @param iIdRechnungI
	 *            PK der Rechnung
	 * @param iIdAuftragI
	 *            Integer
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
			Integer iIdRechnungI, Integer iIdAuftragI) throws ExceptionLP {
		try {
			rechnungFac
					.uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
							iIdRechnungI, iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
			Integer iIdRechnungI, Integer iIdAuftrag,
			List<Artikelset> artikelsets) throws ExceptionLP {
		try {
			rechnungFac
					.uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
							iIdRechnungI, iIdAuftrag, artikelsets,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Aus einer Rechnung jene Position heraussuchen, die zu einer bestimmten
	 * Auftragposition gehoert.
	 * 
	 * @param iIdRechnungI
	 *            Integer
	 * @param iIdAuftragpositionI
	 *            Integer
	 * @throws ExceptionLP
	 * @return LieferscheinpositionDto
	 */
	public RechnungPositionDto getRechnungPositionByRechnungAuftragposition(
			Integer iIdRechnungI, Integer iIdAuftragpositionI)
			throws ExceptionLP {
		RechnungPositionDto oRechnungpositionDto = null;
		try {
			oRechnungpositionDto = rechnungFac
					.getRechnungPositionByRechnungAuftragposition(iIdRechnungI,
							iIdAuftragpositionI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oRechnungpositionDto;
	}

	public boolean pruefePositionenAufSortierungNachAuftrag(Integer rechnungIId)
			throws ExceptionLP {
		boolean bOk = false;
		try {
			bOk = rechnungFac.pruefePositionenAufSortierungNachAuftrag(
					rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return bOk;
	}

	public String pruefeRechnungswert() throws ExceptionLP {
		try {
			return rechnungFac.pruefeRechnungswert(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void sortierePositionenNachAuftrag(Integer rechnungIId)
			throws ExceptionLP {
		try {
			rechnungFac.sortierePositionenNachAuftrag(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public String getGutschriftenEinerRechnung(Integer rechnungIId)
			throws ExceptionLP {
		String text = null;
		try {
			text = rechnungFac.getGutschriftenEinerRechnung(rechnungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return text;
	}

	public void berechnePauschalposition(BigDecimal wert, Integer positionIId,
			Integer belegIId) throws ExceptionLP {
		try {
			rechnungFac.berechnePauschalposition(wert, positionIId, belegIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public BigDecimal berechneSummeOffenNetto(Integer kundeIId,
			boolean bStatistikadresse) throws ExceptionLP {
		GregorianCalendar gcVon = new GregorianCalendar(1900, 01, 01);

		GregorianCalendar gcBis = new GregorianCalendar(2099, 01, 01);
		try {
			return rechnungFac.berechneSummeOffenNetto(LPMain.getTheClient()
					.getMandant(), RechnungFac.KRIT_MIT_GUTSCHRIFTEN, gcVon,
					gcBis, kundeIId, bStatistikadresse, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP[] printRechnungZahlschein(Integer iRechnungIId,
			String sReportname, Integer iKopien) throws ExceptionLP {
		try {
			return rechnungReportFac.printRechnungZahlschein(iRechnungIId,
					sReportname, iKopien, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void setzeVersandzeitpunktAufJetzt(Integer iRechnungIId,
			String sDruckart) throws ExceptionLP {
		try {
			rechnungFac.setzeVersandzeitpunktAufJetzt(iRechnungIId, sDruckart);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void verbucheRechnungNeu(Integer iRechnungIId) throws ExceptionLP {
		try {
			rechnungFac
					.verbucheRechnungNeu(iRechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void verbucheGutschriftNeu(Integer iRechnungIId) throws ExceptionLP {
		try {
			rechnungFac.verbucheGutschriftNeu(iRechnungIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param rechnungIId
	 * @param position
	 *            die Positionsnummer f&uuml;r die die IId ermittelt werden soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId,
			Integer position) throws ExceptionLP {
		try {
			return rechnungFac.getPositionIIdFromPositionNummer(rechnungIId,
					position);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public Integer getPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return rechnungFac.getPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Liefert die Positionsnummer der angegebenen Position-IId. Sollte die
	 * Position selbst keine Nummer haben, wird die unmittelbar vor dieser
	 * Position liegende letztg&uuml;ltige Nummer geliefert.
	 * 
	 * @param reposIId
	 * @return Die Positionsnummer (1 - n), oder null wenn die Position nicht
	 *         vorkommt.
	 */
	public Integer getLastPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return rechnungFac.getLastPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Die hoechste/letzte in einer Rechnung bestehende Positionsnummer
	 * ermitteln
	 * 
	 * @param rechnungIId
	 *            die RechnungsIId fuer die die hoechste Pos.Nummer ermittelt
	 *            werden soll.
	 * 
	 * @return 0 ... n
	 */
	public Integer getHighestPositionNumber(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return rechnungFac.getHighestPositionNumber(rechnungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return 0;
	}

	/**
	 * Prueft, ob fuer alle Rechnungspositionen zwischen den beiden angegebenen
	 * Positionsnummern der gleiche Mehrwertsteuersatz definiert ist.
	 * 
	 * @param rechnungIId
	 * @param vonPositionNumber
	 * @param bisPositionNumber
	 * @return true wenn alle Positionen den gleichen Mehrwertsteuersatz haben.
	 * @throws EJBExceptionLP
	 */
	public boolean pruefeAufGleichenMwstSatz(Integer rechnungIId,
			Integer vonPositionNumber, Integer bisPositionNumber)
			throws ExceptionLP {
		try {
			return rechnungFac.pruefeAufGleichenMwstSatz(rechnungIId,
					vonPositionNumber, bisPositionNumber);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return false;
	}

	public void toggleZollpapiereErhalten(Integer eingangsrechnungIId,
			String cZollpapier) throws ExceptionLP {
		try {
			rechnungFac.toggleZollpapiereErhalten(eingangsrechnungIId,
					cZollpapier, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public RechnungPositionDto[] getArtikelsetForIId(Integer kopfartikelIId)
			throws ExceptionLP {
		try {
			return rechnungFac.getArtikelsetForIId(kopfartikelIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public List<SeriennrChargennrMitMengeDto> getSeriennrchargennrForArtikelsetPosition(
			Integer rechnungposIId) throws ExceptionLP {
		try {
			return rechnungFac
					.getSeriennrchargennrForArtikelsetPosition(rechnungposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return new ArrayList<SeriennrChargennrMitMengeDto>();
	}
}
