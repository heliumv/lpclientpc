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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.personal.service.BereitschaftDto;
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.DiaetentagessatzDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinengruppeDto;
import com.lp.server.personal.service.MaschinenkostenDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.SollverfuegbarkeitDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.SonderzeitenImportDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.TagesartDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.UrlaubsabrechnungDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitmodellDto;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.server.personal.service.ZeitmodelltagpauseDto;
import com.lp.server.personal.service.ZeitstiftDto;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

public class ZeiterfassungDelegate extends Delegate {

	private Context context;

	private ZeiterfassungFac zeiterfassungFac;

	public ZeiterfassungDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			zeiterfassungFac = (ZeiterfassungFac) context
					.lookup("lpserver/ZeiterfassungFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public boolean sindZuvieleZeitdatenEinesBelegesVorhanden(
			String belegartCNr, Integer belegartIId) throws ExceptionLP {
		try {
			return zeiterfassungFac.sindZuvieleZeitdatenEinesBelegesVorhanden(
					belegartCNr, belegartIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}
	}

	public Integer createZeitmodell(ZeitmodellDto zeitmodellDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createZeitmodell(zeitmodellDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getBebuchbareBelegarten() throws ExceptionLP {
		try {
			return zeiterfassungFac.getBebuchbareBelegarten(LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createTagesart(TagesartDto tagesartDto) throws ExceptionLP {
		try {
			return zeiterfassungFac.createTagesart(tagesartDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createDiaeten(DiaetenDto diaetenDto) throws ExceptionLP {
		try {
			return zeiterfassungFac.createDiaeten(diaetenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.sql.Timestamp pruefeObAmLetztenBuchungstagKommtUndGehtGebuchtWurde(
			Integer personalIId) throws ExceptionLP {
		try {
			return zeiterfassungFac
					.pruefeObAmLetztenBuchungstagKommtUndGehtGebuchtWurde(
							personalIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean sindReisezeitenZueinemTagVorhanden(Integer personalIId,
			java.sql.Timestamp tDatum) throws ExceptionLP {
		try {
			return zeiterfassungFac.sindReisezeitenZueinemTagVorhanden(
					personalIId, tDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}
	}

	public Timestamp getErstesKommtEinesTages(Integer personalIId,
			java.sql.Timestamp tDatum) throws ExceptionLP {
		try {
			return zeiterfassungFac.getErstesKommtEinesTages(personalIId,
					tDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Timestamp getLetztesGehtEinesTages(Integer personalIId,
			java.sql.Timestamp tDatum) throws ExceptionLP {
		try {
			return zeiterfassungFac.getLetztesGehtEinesTages(personalIId,
					tDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printWochenabrechnung(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer iOption,
			boolean bPlusVersteckte) throws ExceptionLP {

		try {
			return zeiterfassungFac.printWochenabrechnung(personalIId, tVon,
					tBis, LPMain.getTheClient(), iOption, bPlusVersteckte);

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public JasperPrintLP printWochenjournal(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer iOption,
			boolean bPlusVersteckte) throws ExceptionLP {

		try {
			return zeiterfassungFac.printWochenjournal(personalIId, tVon,
					tBis, LPMain.getTheClient(), iOption, bPlusVersteckte);

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	

	public Integer createDiaetentagessatz(
			DiaetentagessatzDto diaetentagessatzDto) throws ExceptionLP {
		try {
			return zeiterfassungFac.createDiaetentagessatz(diaetentagessatzDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createReise(ReiseDto reiseDto) throws ExceptionLP {
		try {
			return zeiterfassungFac
					.createReise(reiseDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public UrlaubsabrechnungDto berechneUrlaubsAnspruch(Integer personalIId,
			java.sql.Date dAbrechnungszeitpunkt) throws ExceptionLP {
		try {
			return zeiterfassungFac.berechneUrlaubsAnspruch(personalIId,
					dAbrechnungszeitpunkt, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Double berechneTagesArbeitszeit(Integer personalIId,
			java.sql.Date dDatum) throws ExceptionLP {
		Double tagesarbeitszeit = null;
		try {
			tagesarbeitszeit = zeiterfassungFac.berechneTagesArbeitszeit(
					personalIId, dDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return tagesarbeitszeit;
	}

	public void automatikbuchungenAufrollen(Integer personalIId,
			java.sql.Date tVon, java.sql.Date tBis, boolean bLoeschen)
			throws ExceptionLP {
		try {
			zeiterfassungFac.automatikbuchungenAufrollen(tVon, tBis,
					personalIId, LPMain.getTheClient(), bLoeschen);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createSonderzeiten(SonderzeitenDto sonderzeitenDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createSonderzeiten(sonderzeitenDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.sql.Timestamp[] sindIstZeitenVorhandenWennUrlaubGebuchtWird(
			SonderzeitenDto sonderzeitenDto, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis) throws ExceptionLP {
		try {
			return zeiterfassungFac
					.sindIstZeitenVorhandenWennUrlaubGebuchtWird(
							sonderzeitenDto, tVon, tBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSonderzeitenVonBis(SonderzeitenDto sonderzeitenDto,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			java.sql.Timestamp[] auslassen) throws ExceptionLP {
		try {
			return zeiterfassungFac.createSonderzeitenVonBis(sonderzeitenDto,
					tVon, tBis, auslassen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createTaetigkeit(TaetigkeitDto taetigkeitDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createTaetigkeit(taetigkeitDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void uebersteuereZeitmodellFuerEinenTag(Integer personalIId,
			Integer zeitmodellIId, java.sql.Date dDatum) throws ExceptionLP {
		try {
			zeiterfassungFac.uebersteuereZeitmodellFuerEinenTag(personalIId,
					zeitmodellIId, dDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createMaschine(MaschineDto maschineDto) throws ExceptionLP {
		try {
			return zeiterfassungFac.createMaschine(maschineDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createBereitschaft(BereitschaftDto dto) throws ExceptionLP {
		try {
			return zeiterfassungFac.createBereitschaft(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMaschinengruppe(MaschinengruppeDto maschinengruppeDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createMaschinengruppe(maschinengruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMaschinenkosten(MaschinenkostenDto maschinenkostenDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createMaschinenkosten(maschinenkostenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMaschinenzeitdaten(
			MaschinenzeitdatenDto maschinenzeitdatenDto) throws ExceptionLP {
		try {
			return zeiterfassungFac.createMaschinenzeitdaten(
					maschinenzeitdatenDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZeitstift(ZeitstiftDto zeitstiftDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createZeitstift(zeitstiftDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createTelefonzeiten(TelefonzeitenDto telefonzeitenDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createTelefonzeiten(telefonzeitenDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public AuftragzeitenDto[] getAllZeitenEinesBeleges(String belegartCNr,
			boolean bOrderByArtikelCNr, boolean bOrberByPersonal,
			Integer belegartIId, Integer belegartpositionIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			Integer personalIId) throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getAllZeitenEinesBeleges(belegartCNr, belegartIId,
							belegartpositionIId, personalIId, tVon, tBis,
							bOrderByArtikelCNr, bOrberByPersonal,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public AuftragzeitenDto[] getAllMaschinenzeitenEinesBeleges(Integer losIId,
			Integer lossollarbeitsplanIId, java.sql.Timestamp tZeitenVon,
			java.sql.Timestamp tZeitenBis) throws ExceptionLP {
		try {
			return zeiterfassungFac.getAllMaschinenzeitenEinesBeleges(losIId,
					lossollarbeitsplanIId, tZeitenVon, tZeitenBis,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto[] getPersonenDieZeitmodellVerwenden(Integer zeitmodellIId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.getPersonenDieZeitmodellVerwenden(
					zeitmodellIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SollverfuegbarkeitDto[] getVerfuegbareSollzeit(
			java.sql.Timestamp tVon, java.sql.Timestamp tBis)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.getVerfuegbareSollzeit(tVon, tBis,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Double getSummeZeitenEinesBeleges(String belegartCNr,
			Integer belegartIId, Integer belegartpositionIId,
			Integer personalIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis) throws ExceptionLP {
		try {
			return zeiterfassungFac.getSummeZeitenEinesBeleges(belegartCNr,
					belegartIId, belegartpositionIId, personalIId, tVon, tBis,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.math.BigDecimal getMengeGutSchlechtEinesLosSollarbeitsplanes(
			Integer lossollarbeitsplanIId, boolean bGut) throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getMengeGutSchlechtEinesLosSollarbeitsplanes(
							lossollarbeitsplanIId, LPMain.getTheClient(),
							new Boolean(bGut));
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.math.BigDecimal getMaschinenKostenZumZeitpunkt(
			Integer maschineIId) throws ExceptionLP {
		try {
			return zeiterfassungFac.getMaschinenKostenZumZeitpunkt(maschineIId,
					new Timestamp(System.currentTimeMillis()));
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZeitdaten(ZeitdatenDto zeitdatenDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createZeitdaten(zeitdatenDto, true, true,
					true, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZeitverteilung(ZeitverteilungDto zeitverteilungDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createZeitverteilung(zeitverteilungDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void speichereZeidatenVonZEStift(ZeitdatenDto[] zeitdatenDtos)
			throws ExceptionLP {
		try {
			zeiterfassungFac.speichereZeidatenVonZEStift(zeitdatenDtos,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer bucheZeitRelativ(ZeitdatenDto zeitdatenDto,
			boolean bAuchWennZuWenigZeit) throws ExceptionLP {
		try {
			return zeiterfassungFac.bucheZeitRelativ(zeitdatenDto, null,
					new Boolean(bAuchWennZuWenigZeit), false,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void aendereZeitRelativ(ZeitdatenDto zeitdatenDto,
			java.sql.Time bZeitRelativ) throws ExceptionLP {
		try {
			zeiterfassungFac.aendereZeitRelativ(zeitdatenDto, bZeitRelativ,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createZeitmodelltag(ZeitmodelltagDto zeitmodelltagDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.createZeitmodelltag(zeitmodelltagDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean sindBelegzeitenVorhanden(String cBelegartnr,
			Integer belegartIId) throws ExceptionLP {
		try {
			return zeiterfassungFac.sindBelegzeitenVorhanden(cBelegartnr,
					belegartIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public Integer createZeitmodelltagpause(
			ZeitmodelltagpauseDto zeitmodelltagpauseDto) throws ExceptionLP {
		try {
			return zeiterfassungFac.createZeitmodelltagpause(
					zeitmodelltagpauseDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeZeitmodell(ZeitmodellDto zeitmodellDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.removeZeitmodell(zeitmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public JasperPrintLP printMonatsabrechnung(Integer personalIId,
			Integer iJahr, Integer iMonat, Integer iOption,
			Integer iOptionSortierung, boolean bPlusVersteckte,
			boolean bBisMonatsende, java.sql.Date d_datum_bis)
			throws ExceptionLP {

		try {
			return zeiterfassungFac.printMonatsAbrechnung(personalIId, iJahr,
					iMonat, bBisMonatsende, d_datum_bis, LPMain.getTheClient(),
					iOption, iOptionSortierung, new Boolean(bPlusVersteckte));

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printZeitsaldo(Integer personalIId, Integer iJahrVon,
			Integer iMonatVon, Integer iJahrBis, Integer iMonatBis,
			Integer iOption, Integer iOptionSortierung,
			boolean bPlusVersteckte, boolean bBisMonatsende,
			java.sql.Date d_datum_bis) throws ExceptionLP {

		try {
			return zeiterfassungFac.printZeitsaldo(personalIId, iJahrVon,
					iMonatVon, iJahrBis, iMonatBis, bBisMonatsende,
					d_datum_bis, LPMain.getTheClient(), iOption,
					iOptionSortierung, new Boolean(bPlusVersteckte));

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printAnwesenheitsliste() throws ExceptionLP {
		try {
			return zeiterfassungFac.printAnwesenheitsliste(LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printSondertaetigkeitsliste(Integer personalIId,
			Integer taetigkeitIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bPlusVersteckte, Integer iOption)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.printSondertaetigkeitsliste(personalIId,
					taetigkeitIId, tVon, tBis, iOption, new Boolean(
							bPlusVersteckte), LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLohndatenexport(Integer personalIId,
			Integer iJahr, Integer iMonat, boolean bisMonatsende,
			java.sql.Date d_datum_bis, Integer iOption,
			Integer iOptionSortierung, Boolean bPlusVersteckte)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.printLohndatenexport(personalIId, iJahr,
					iMonat, bisMonatsende, d_datum_bis, LPMain.getTheClient(),
					iOption, iOptionSortierung, bPlusVersteckte);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printSondertaetigkeiten() throws ExceptionLP {
		try {
			return zeiterfassungFac.printSondertaetigkeiten(LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printProduktivitaetsstatistik(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			boolean bPlusVersteckte, boolean bVerdichtet, Integer iOption)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.printProduktivitaetsstatistik(personalIId,
					tVon, tBis, iOption, new Boolean(bPlusVersteckte),
					bVerdichtet, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeZeitmodelltagpause(
			ZeitmodelltagpauseDto zeitmodelltagpauseDto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeZeitmodelltagpause(zeitmodelltagpauseDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZeitdaten(ZeitdatenDto zeitdatenDto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeZeitdaten(zeitdatenDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeReise(ReiseDto reiseDto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeReise(reiseDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeTaetigkeit(TaetigkeitDto taetigkeitDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.removeTaetigkeit(taetigkeitDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMaschine(MaschineDto maschineDto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeMaschine(maschineDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void konvertiereAngebotszeitenNachAuftragzeiten(Integer angebotIId,
			Integer auftragIIdo) throws ExceptionLP {
		try {
			zeiterfassungFac.konvertiereAngebotszeitenNachAuftragzeiten(
					angebotIId, auftragIIdo, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void loszeitenVerschieben(Integer losIId_Quelle, Integer losIId_Ziel)
			throws ExceptionLP {
		try {
			zeiterfassungFac.loszeitenVerschieben(losIId_Quelle, losIId_Ziel,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMaschinengruppe(MaschinengruppeDto maschinengruppeDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.removeMaschinengruppe(maschinengruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMaschinenkosten(MaschinenkostenDto maschinenkostenDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.removeMaschinenkosten(maschinenkostenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZeitstift(ZeitstiftDto zeitstiftDto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeZeitstift(zeitstiftDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBereitschaft(BereitschaftDto dto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeBereitschaft(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMaschinenzeitdaten(
			MaschinenzeitdatenDto maschinenzeitdatenDto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeMaschinenzeitdaten(maschinenzeitdatenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeTagesart(TagesartDto tagesartDto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeTagesart(tagesartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeDiaeten(DiaetenDto diaetenDto) throws ExceptionLP {
		try {
			zeiterfassungFac.removeDiaeten(diaetenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeDiaetentagessatz(DiaetentagessatzDto diaetentagessatzDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.removeDiaetentagessatz(diaetentagessatzDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeTelefonzeiten(TelefonzeitenDto telefonzeitenDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.removeTelefonzeiten(telefonzeitenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSonderzeiten(SonderzeitenDto sonderzeitenDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.removeSonderzeiten(sonderzeitenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZeitdaten(ZeitdatenDto zeitdatenDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateZeitdaten(zeitdatenDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateReise(ReiseDto reiseDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateReise(reiseDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSonderzeiten(SonderzeitenDto sonderzeitenDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.updateSonderzeiten(sonderzeitenDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZeitmodelltag(ZeitmodelltagDto zeitmodellDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.removeZeitmodelltag(zeitmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Map<?, ?> getAllSprTagesarten() throws ExceptionLP {
		try {
			return zeiterfassungFac.getAllSprTagesarten(LPMain.getTheClient()
					.getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprTaetigkeitarten() throws ExceptionLP {
		try {
			return zeiterfassungFac.getAllSprTaetigkeitarten(LPMain
					.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.sql.Time getRelativeZeitFuerRelativesAendernAmClient(
			Integer personalIId, java.sql.Timestamp tBelegbuchung)
			throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getRelativeZeitFuerRelativesAendernAmClient(personalIId,
							tBelegbuchung);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprTagesartenOhneMontagBisSonntag()
			throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getAllSprTagesartenOhneMontagBisSonntag(LPMain
							.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<Integer, String> getAllSprSondertaetigkeiten()
			throws ExceptionLP {
		try {
			return zeiterfassungFac.getAllSprSondertaetigkeiten(LPMain
					.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<Integer, String> getAllSprSondertaetigkeitenOhneVersteckt()
			throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getAllSprSondertaetigkeitenOhneVersteckt(LPMain
							.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<Integer, String> getAllSprSondertaetigkeitenNurBDEBuchbar()
			throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getAllSprSondertaetigkeitenNurBDEBuchbar(LPMain
							.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<Integer, String> getAllSprSondertaetigkeitenNurBDEBuchbarOhneVersteckt()
			throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getAllSprSondertaetigkeitenNurBDEBuchbarOhneVersteckt(LPMain
							.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprTagesartenEinesZeitmodells(Integer zeitmodellIId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.getAllSprTagesartenEinesZeitmodells(
					zeitmodellIId, LPMain.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Double getSummeSollzeitWochentags(Integer zeitmodellIId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.getSummeSollzeitWochentags(zeitmodellIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Double getSummeSollzeitMontagBisSonntag(Integer zeitmodellIId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getSummeSollzeitMontagBisSonntag(zeitmodellIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Double getSummeSollzeitSonnUndFeiertags(Integer zeitmodellIId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac
					.getSummeSollzeitSonnUndFeiertags(zeitmodellIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateZeitmodell(ZeitmodellDto zeitmodellDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.updateZeitmodell(zeitmodellDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void maschineStop(Integer maschineIId,
			Integer lossollarbeitsplanIId, java.sql.Timestamp tStop)
			throws ExceptionLP {
		try {
			zeiterfassungFac.maschineStop(maschineIId, lossollarbeitsplanIId,
					tStop, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateTaetigkeit(TaetigkeitDto taetigkeitDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.updateTaetigkeit(taetigkeitDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMaschinenzeitdaten(
			MaschinenzeitdatenDto maschinenzeitdatenDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateMaschinenzeitdaten(maschinenzeitdatenDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMaschine(MaschineDto maschineDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateMaschine(maschineDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void importiereSonderzeiten(java.sql.Date tLoescheVorhandenevon,
			java.sql.Date tLoescheVorhandenebis,
			HashMap<Integer, ArrayList<SonderzeitenImportDto>> daten)
			throws ExceptionLP {
		try {
			zeiterfassungFac.importiereSonderzeiten(tLoescheVorhandenevon,
					tLoescheVorhandenebis, daten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMaschinengruppe(MaschinengruppeDto maschinengruppeDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.updateMaschinengruppe(maschinengruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMaschinenkosten(MaschinenkostenDto maschinenkostenDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.updateMaschinenkosten(maschinenkostenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZeitstift(ZeitstiftDto zeitstiftDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateZeitstift(zeitstiftDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateTagesart(TagesartDto tagesartDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateTagesart(tagesartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateTagesart(DiaetenDto diaetenDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateDiaeten(diaetenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateDiaetentagessatz(DiaetentagessatzDto diaetentagessatzDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.updateDiaetentagessatz(diaetentagessatzDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateDiaetenz(DiaetenDto diaetenDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateDiaeten(diaetenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateTelefonzeiten(TelefonzeitenDto telefonzeitenDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.updateTelefonzeiten(telefonzeitenDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZeitmodelltag(ZeitmodelltagDto zeitmodelltagDto)
			throws ExceptionLP {
		try {
			zeiterfassungFac.updateZeitmodelltag(zeitmodelltagDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBereitschaft(BereitschaftDto dto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateBereitschaft(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZeitmodelltagpause(
			ZeitmodelltagpauseDto zeitmodelltagpauseDto) throws ExceptionLP {
		try {
			zeiterfassungFac.updateZeitmodelltagpause(zeitmodelltagpauseDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ZeitmodellDto zeitmodellFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.zeitmodellFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZeitdatenDto zeitdatenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.zeitdatenFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZeitdatenDto zeitdatenFindByPersonalIIdTZeit(Integer personalIId,
			java.sql.Timestamp tZeit) throws ExceptionLP {
		try {
			return zeiterfassungFac.zeitdatenFindByPersonalIIdTZeit(
					personalIId, tZeit);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public TaetigkeitDto taetigkeitFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.taetigkeitFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public TelefonzeitenDto telefonzeitenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.telefonzeitenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public TaetigkeitDto taetigkeitFindByCNr(String cNr) throws ExceptionLP {
		try {
			return zeiterfassungFac.taetigkeitFindByCNr(cNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HashMap<?, ?> taetigkeitenMitImportkennzeichen() throws ExceptionLP {
		try {
			return zeiterfassungFac.taetigkeitenMitImportkennzeichen();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public TagesartDto tagesartFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return zeiterfassungFac.tagesartFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public DiaetenDto diaetenFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return zeiterfassungFac.diaetenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BereitschaftDto bereitschaftFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.bereitschaftFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public DiaetenDto[] diaetenFindByLandIId(Integer landIId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.diaetenFindByLandIId(landIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public DiaetentagessatzDto diaetentagessatzFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.diaetentagessatzFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MaschineDto maschineFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return zeiterfassungFac.maschineFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MaschinenzeitdatenDto maschinenzeitdatenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.maschinenzeitdatenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MaschinengruppeDto maschinengruppeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.maschinengruppeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ReiseDto reiseFindByPrimaryKey(Integer reiseIId) throws ExceptionLP {
		try {
			return zeiterfassungFac.reiseFindByPrimaryKey(reiseIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String istBelegGeradeInBearbeitung(String belegartCNr,
			Integer belegartIId) throws ExceptionLP {
		try {
			return zeiterfassungFac.istBelegGeradeInBearbeitung(belegartCNr,
					belegartIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MaschinenkostenDto maschinenkostenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.maschinenkostenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MaschineDto maschineFindByMandantCNrCInventarnummer(
			String cInventarnummer) throws ExceptionLP {
		try {
			return zeiterfassungFac.maschineFindByMandantCNrCInventarnummer(
					cInventarnummer, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MaschineDto maschineFindByCIdentifikationsnr(
			String cIdentifikationsnummer) throws ExceptionLP {
		try {
			return zeiterfassungFac
					.maschineFindByCIdentifikationsnr(cIdentifikationsnummer);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZeitstiftDto zeitstiftFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.zeitstiftFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZeitverteilungDto[] zeitverteilungFindByPersonalIId(
			Integer personalIId) throws ExceptionLP {
		try {
			return zeiterfassungFac
					.zeitverteilungFindByPersonalIId(personalIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public void zeitAufLoseVerteilen(Integer personalIId, Timestamp tZeitBis)
			throws ExceptionLP {
		try {
			zeiterfassungFac.zeitAufLoseVerteilen(personalIId, tZeitBis,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ZeitstiftDto[] zeitstiftFindByMandantCNr(TheClientDto theClientDto)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.zeitstiftFindByMandantCNr(theClientDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SonderzeitenDto sonderzeitenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.sonderzeitenFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SonderzeitenDto[] sonderzeitenFindByPersonalIIdDDatum(
			Integer personalIId, java.sql.Timestamp dDatum) throws ExceptionLP {
		try {
			return zeiterfassungFac.sonderzeitenFindByPersonalIIdDDatum(
					personalIId, dDatum);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean istUrlaubstagZuDatumNoetig(Integer personalIId,
			java.sql.Timestamp dDatum) throws ExceptionLP {
		try {
			return zeiterfassungFac.istUrlaubstagZuDatumNoetig(personalIId,
					dDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public ZeitmodelltagDto zeitmodelltagFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.zeitmodelltagFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZeitmodelltagpauseDto zeitmodelltagpauseFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zeiterfassungFac.zeitmodelltagpauseFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
}
