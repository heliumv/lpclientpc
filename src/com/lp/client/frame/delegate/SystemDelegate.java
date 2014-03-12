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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import net.sf.jasperreports.engine.JasperReport;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.EinheitKonvertierungDto;
import com.lp.server.system.service.EinheitsprDto;
import com.lp.server.system.service.ExtralisteDto;
import com.lp.server.system.service.ExtralisteRueckgabeTabelleDto;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.PingPacket;
import com.lp.server.system.service.ServerJavaAndOSInfo;
import com.lp.server.system.service.ServerLocaleInfo;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemMultilanguageFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TextDto;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class SystemDelegate extends Delegate {
	private Context context;

	private SystemFac systemFac;
	private SystemMultilanguageFac systemMultilanguageFac;
	private SystemServicesFac systemServicesFac;

	private JasperReport dreispalter = null;

	public SystemDelegate() throws ExceptionLP {
		try {
			context = new InitialContext(); // getInitialContext();
			systemFac = (SystemFac) context
					.lookup("lpserver/SystemFacBean/remote");
			systemMultilanguageFac = (SystemMultilanguageFac) context
					.lookup("lpserver/SystemMultilanguageFacBean/remote");
			systemServicesFac = (SystemServicesFac) context
					.lookup("lpserver/SystemServicesFacBean/remote");
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public byte[] getHintergrundbild() throws ExceptionLP {
		byte[] bild = null;
		try {
			bild = systemFac.getHintergrundbild();
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bild;
	}

	public Integer createOrt(OrtDto ortDto) throws ExceptionLP {
		Integer iIdOrt = null;
		try {
			iIdOrt = systemFac.createOrt(ortDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iIdOrt;
	}

	public void removeOrt(OrtDto ortDto) throws ExceptionLP {

		try {
			systemFac.removeOrt(ortDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateOrt(OrtDto ortDto) throws ExceptionLP {
		try {
			systemFac.updateOrt(ortDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public OrtDto ortFindByPrimaryKey(Integer iId) throws ExceptionLP {

		OrtDto ortDto = null;
		try {
			ortDto = systemFac.ortFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ortDto;
	}

	public OrtDto ortFindByName(String cName) throws ExceptionLP {
		OrtDto ortDto = null;
		try {
			ortDto = systemFac.ortFindByName(cName);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ortDto;
	}

	public Integer createKostenstelle(KostenstelleDto kostenstelleDto)
			throws ExceptionLP {
		try {
			return systemFac.createKostenstelle(kostenstelleDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeKostenstelle(Integer iId) throws ExceptionLP {

		try {
			systemFac.removeKostenstelle(iId);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
	}

	public void removeKostenstelle(KostenstelleDto kostenstelleDto)
			throws ExceptionLP {
		try {
			systemFac.removeKostenstelle(kostenstelleDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
	}

	public void updateKostenstelle(KostenstelleDto kostenstelleDto)
			throws ExceptionLP {
		try {
			systemFac.updateKostenstelle(kostenstelleDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
	}

	public void speichereKeyValueDtos(KeyvalueDto[] keyvalueDtos)
			throws ExceptionLP {
		try {
			systemServicesFac.speichereKeyValueDtos(keyvalueDtos);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
	}

	public KostenstelleDto kostenstelleFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		KostenstelleDto kostenstelleDto = null;
		try {
			kostenstelleDto = systemFac.kostenstelleFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
		return kostenstelleDto;
	}

	public KostenstelleDto[] kostenstelleFindByMandant(String pMandant)
			throws ExceptionLP {
		KostenstelleDto[] kostenstelleDto = null;
		try {
			kostenstelleDto = systemFac.kostenstelleFindByMandant(pMandant);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
		return kostenstelleDto;
	}

	public KostenstelleDto kostenstelleFindByNummerMandant(String cNr,
			String mandantCNr) throws ExceptionLP {
		KostenstelleDto kostenstelleDto = null;
		try {
			kostenstelleDto = systemFac.kostenstelleFindByNummerMandant(cNr,
					mandantCNr);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
		return kostenstelleDto;
	}

	/**
	 * Alle verfuegbaren Mengeneinheiten aus der DB holen.
	 * 
	 * @return KeyValueDto[]
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getAllEinheiten() throws ExceptionLP {
		Map<?, ?> arten = null;
		try {
			arten = systemFac.getAllEinheit(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return arten;
	}

	/**
	 * 
	 * @return Map
	 * @throws ExceptionLP
	 */
	public EinheitKonvertierungDto[] getAllEinheitKonvertierungen()
			throws ExceptionLP {
		EinheitKonvertierungDto arten[] = null;
		try {
			arten = systemFac.getAllEinheitKonvertierungen();
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return arten;
	}

	/**
	 * Alle verfuegbaren Geschaefstjahre aus der DB holen.
	 * 
	 * @return KeyValueDto[]
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public Map<?, ?> getAllGeschaeftsjahr() throws ExceptionLP {
		Map<?, ?> arten = null;
		try {
			arten = systemFac.getAllGeschaeftsjahr(LPMain.getTheClient()
					.getMandant());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return arten;
	}

	public Integer createLand(LandDto landDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = systemFac.createLand(landDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public String getHauptmandant() throws ExceptionLP {
		try {
			return systemFac.getHauptmandant();
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public Integer createExtraliste(ExtralisteDto extralisteDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			iId = systemFac.createExtraliste(extralisteDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeLand(LandDto landDto) throws ExceptionLP {
		try {
			systemFac.removeLand(landDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
	}

	public void updateLand(LandDto landDto) throws ExceptionLP {
		try {
			systemFac.updateLand(landDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	// **************************************************************************
	// **
	public String createEinheit(EinheitDto einheitDto) throws ExceptionLP {
		String sNr = null;
		try {
			sNr = systemFac.createEinheit(einheitDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return sNr;
	}

	public void createEinheitspr(EinheitsprDto einheitssprDtoI)
			throws ExceptionLP {
		try {
			systemFac.createEinheitspr(einheitssprDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeEinheit(EinheitDto einheitDto) throws ExceptionLP {
		try {
			systemFac.removeEinheit(einheitDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void removeExtraliste(ExtralisteDto extralisteDto)
			throws ExceptionLP {
		try {
			systemFac.removeExtraliste(extralisteDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void updateEinheit(EinheitDto einheitDto) throws ExceptionLP {
		try {
			systemFac.updateEinheit(einheitDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateExtraliste(ExtralisteDto extralisteDto)
			throws ExceptionLP {
		try {
			systemFac.updateExtraliste(extralisteDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EinheitDto einheitFindByPrimaryKey(String cNr) throws ExceptionLP {
		EinheitDto einheitDto = null;
		try {
			einheitDto = systemFac.einheitFindByPrimaryKey(cNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
		return einheitDto;
	}

	public ExtralisteDto extralisteFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		ExtralisteDto extralisteDto = null;
		try {
			extralisteDto = systemFac.extralisteFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return extralisteDto;
	}

	public ExtralisteDto[] extralisteFindByBelegartCNr(String cNr)
			throws ExceptionLP {
		ExtralisteDto[] extralisteDtos = null;
		try {
			extralisteDtos = systemFac.extralisteFindByBelegartCNr(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return extralisteDtos;
	}

	public ExtralisteRueckgabeTabelleDto generiereExtraliste(
			Integer extralisteIId) throws ExceptionLP {
		ExtralisteRueckgabeTabelleDto o = null;
		try {
			o = systemFac.generiereExtraliste(extralisteIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return o;
	}

	// **************************************************************************
	// ****

	public EinheitKonvertierungDto einheitKonvertierungFindByPrimaryKey(
			Integer iIdI) throws ExceptionLP {
		EinheitKonvertierungDto einheitKonvertierungrDto = null;
		try {
			einheitKonvertierungrDto = systemFac
					.einheitKonvertierungFindByPrimaryKey(iIdI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return einheitKonvertierungrDto;
	}

	public Integer createEinheitKonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = systemFac.createEinheitKonvertierung(einheitKonvertierungDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeEinheitKonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto) throws ExceptionLP {
		try {
			systemFac.removeEinheitKonvertierung(einheitKonvertierungDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
	}

	public void updateEinheitKonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto) throws ExceptionLP {
		try {
			systemFac.updateEinheitkonvertierung(einheitKonvertierungDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	// **************************************************************************
	// ****
	public Integer createLandplzort(LandplzortDto landplzortDto)
			throws ExceptionLP {
		Integer landplzortId = null;
		try {
			landplzortId = systemFac.createLandplzort(landplzortDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
		return landplzortId;
	}

	public void removeLandplzort(LandplzortDto landplzortDto)
			throws ExceptionLP {
		try {
			systemFac.removeLandplzort(landplzortDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
	}

	public void updateLandplzort(LandplzortDto landplzortDto)
			throws ExceptionLP {
		try {
			systemFac.updateLandplzort(landplzortDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public LandplzortDto landplzortFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		LandplzortDto landplzortDto = null;
		try {
			landplzortDto = systemFac.landplzortFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
		return landplzortDto;
	}

	public KeyvalueDto[] keyvalueFindyByCGruppe(String cGruppe)
			throws ExceptionLP {
		KeyvalueDto[] keyvalueDto = null;
		try {
			keyvalueDto = systemServicesFac.keyvalueFindyByCGruppe(cGruppe);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return keyvalueDto;
	}

	public LandDto landFindByPrimaryKey(Integer iId) throws ExceptionLP {
		LandDto landDto = null;
		try {
			landDto = systemFac.landFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
		return landDto;

	}

	// **************************************************************************
	// **************
	public void createAnwender(AnwenderDto anwenderDto) throws ExceptionLP {
		try {
			systemFac.createAnwender(anwenderDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void removeAnwender() throws ExceptionLP {
		try {
			systemFac.removeAnwender();
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public String[] getErlaubteFonts() throws ExceptionLP {
		try {
			return null;
			// return new String[]{"Arial", "Verdana", "Times New Roman"};
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public JasperReport getDreispalter() throws ExceptionLP {
		try {
			if (dreispalter == null) {
				dreispalter = systemFac.getDreispalter(LPMain.getTheClient());
			}
			return dreispalter;
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeAnwender(AnwenderDto anwenderDto) throws ExceptionLP {
		try {
			systemFac.removeAnwender(anwenderDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void updateAnwender(AnwenderDto anwenderDto) throws ExceptionLP {
		try {
			systemFac.updateAnwender(anwenderDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void updateAnwenders(AnwenderDto[] anwenderDtos) throws ExceptionLP {
		try {
			systemFac.updateAnwenders(anwenderDtos);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public AnwenderDto anwenderFindByPrimaryKey(Integer iIdI)
			throws ExceptionLP {
		AnwenderDto anwenderDto = null;
		try {
			anwenderDto = systemFac.anwenderFindByPrimaryKey(iIdI);
			// expirehv: 1 hier wegfiltern.
			anwenderDto.setCVersionDB(anwenderDto.getCVersionDB().substring(0,
					SystemFac.MAX_LAENGE_HV_DB_VERSION));
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return anwenderDto;
	}

	public AnwenderDto anwenderFindByPrimaryKeyNoFilter(Integer iIdI)
			throws ExceptionLP {
		AnwenderDto anwenderDto = null;
		try {
			// expirehv: 2 hier NICHT wegfiltern.
			anwenderDto = systemFac.anwenderFindByPrimaryKey(iIdI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return anwenderDto;
	}

	// **************************************************************************
	// **************
	public String uebersetzeStatusOptimal(String statusCNr) throws ExceptionLP {

		String sUebersetzung = null;
		try {
			sUebersetzung = systemMultilanguageFac.uebersetzeStatusOptimal(
					statusCNr, LPMain.getInstance().getUISprLocale(), LPMain
							.getInstance().getTheClient().getLocMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return sUebersetzung;
	}

	/**
	 * 
	 * @param cNrVon
	 *            String
	 * @param cNrZu
	 *            String
	 * @return BigDecimal
	 * @throws ExceptionLP
	 */
	public BigDecimal pruefeEinheitKonvertierungViceVersa(String cNrVon,
			String cNrZu) throws ExceptionLP {
		BigDecimal nFaktor = null;
		if (cNrVon != null && cNrZu != null) {
			try {
				nFaktor = systemFac.pruefeEinheitKonvertierungViceVersa(cNrVon,
						cNrZu, LPMain.getTheClient());
			} catch (Throwable ex) {
				handleThrowable(ex);
			}
		}
		return nFaktor;
	}

	public void validateEmailadresse(String sEmailadresse) throws ExceptionLP {
		boolean bValid = Helper.validateEmailadresse(sEmailadresse);
		if (!bValid) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE,
					new Exception("Ung\u00FCltige Emailadresse: |"
							+ sEmailadresse + "|"));
		}
	}

	public void validateFaxnummer(String sFaxnummer) throws ExceptionLP {
		boolean bValid = Helper.validateFaxnummer(sFaxnummer);
		if (!bValid) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_UNGUELTIGE_FAXNUMMER,
					new Exception("Ung\u00FCltige Faxnummer: |" + sFaxnummer
							+ "|"));
		}
	}

	public Timestamp getServerTimestamp() throws ExceptionLP {
		Timestamp t = null;
		try {
			t = systemFac.getServerTimestamp();
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return t;
	}

	/**
	 * Alle verfuegbaren Mengeneinheiten aus der DB holen.
	 * 
	 * @return KeyValueDto[]
	 * @throws ExceptionLP
	 */
	public HashMap<?, ?> getAllStatiMitUebersetzung() throws ExceptionLP {
		HashMap<?, ?> arten = null;
		try {
			arten = systemMultilanguageFac.getAllStatiMitUebersetzung(LPMain
					.getTheClient().getLocUi(), LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return arten;
	}

	public BigDecimal rechneUmInAndereEinheit(BigDecimal bdMengeI,
			String cEinheitVonI, String cEinheitNachI,
			Integer stuecklistepositionIId) throws ExceptionLP {
		BigDecimal bdUmgerechnet = null;
		try {
			if (cEinheitVonI.equals(cEinheitNachI)) {
				// bei gleichen Einheiten ist keine Umrechnung erforderlich
				bdUmgerechnet = bdMengeI;
			} else {
				bdUmgerechnet = systemFac.rechneUmInAndereEinheit(bdMengeI,
						cEinheitVonI, cEinheitNachI, stuecklistepositionIId,
						LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return bdUmgerechnet;
	}

	public TextDto[] textFindByMandantCNrLocaleCNr(String mandantCNr,
			String localeCNr) throws ExceptionLP {
		TextDto[] texte = null;
		try {
			texte = systemMultilanguageFac.textFindByMandantCNrLocaleCNr(LPMain
					.getTheClient().getMandant(), LPMain.getTheClient()
					.getLocUiAsString());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return texte;
	}

	public String getServerPathSeperator() throws ExceptionLP {
		try {
			return systemFac.getServerPathSeperator();
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public String getPrinterNameForReport(String modulI, String filenameI,
			Locale spracheI, String cSubdirectory) throws ExceptionLP {
		try {
			return systemServicesFac.getPrinterNameForReport(modulI, filenameI,
					spracheI, cSubdirectory, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return cSubdirectory;
	}

	public GeschaeftsjahrMandantDto geschaeftsjahrFindByPrimaryKey(
			int geschaeftsjahr) throws ExceptionLP {
		try {
			return systemFac.geschaeftsjahrFindByPrimaryKey(geschaeftsjahr,
					LPMain.getTheClient().getMandant());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void sperreGeschaeftsjahr(Integer aktuellesGeschaeftsjahr)
			throws ExceptionLP {
		try {
			systemFac.sperreGeschaeftsjahr(aktuellesGeschaeftsjahr,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public PingPacket ping(int sequenceNumber) throws ExceptionLP {

		PingPacket packet = new PingPacket();
		packet.setRequestNumber(sequenceNumber);
		packet.setPingTimeSender(System.currentTimeMillis());
		systemFac.ping(packet);
		packet.setPingTimeSenderStop(System.currentTimeMillis());
		return packet;
	}

	public boolean isGeschaeftsjahrGesperrt(Integer geschaeftsjahr) {
		try {
			systemFac.pruefeGeschaeftsjahrSperre(geschaeftsjahr, LPMain
					.getTheClient().getMandant());
			return false;
		} catch (Throwable t) {
			return true;
		}
	}

	/**
	 * Ermittelt die Locale & Timezone die am Server verwendet wird.
	 * 
	 * @return die Locale&Timeinfo vom Server
	 */
	public ServerLocaleInfo getLocaleInfo() throws ExceptionLP {
		try {
			return systemFac.getLocaleInfo();
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}
	
	public ServerJavaAndOSInfo getJavaAndOSInfo() throws ExceptionLP {
		try {
			return systemFac.getJavaAndOSInfo();
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
			String belegartCNr, Integer belegIId) throws ExceptionLP {
		try {

			Integer kundeIId = null;
			BelegVerkaufDto belegVerkaufDto = null;
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos = null;

			if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)) {

				AngebotDto agDto = DelegateFactory.getInstance()
						.getAngebotDelegate().angebotFindByPrimaryKey(belegIId);
				kundeIId = agDto.getKundeIIdAngebotsadresse();
				belegVerkaufDto = agDto;

				belegpositionVerkaufDtos = DelegateFactory
						.getInstance()
						.getAngebotpositionDelegate()
						.angebotpositionFindByAngebotIIdOhneAlternative(
								belegIId);

			} else if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {

				AuftragDto abDto = DelegateFactory.getInstance()
						.getAuftragDelegate().auftragFindByPrimaryKey(belegIId);
				kundeIId = abDto.getKundeIIdRechnungsadresse();
				belegVerkaufDto = abDto;

				belegpositionVerkaufDtos = DelegateFactory.getInstance()
						.getAuftragpositionDelegate()
						.auftragpositionFindByAuftrag(belegIId);

			} else if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {

				LieferscheinDto lsDto = DelegateFactory.getInstance()
						.getLsDelegate().lieferscheinFindByPrimaryKey(belegIId);
				kundeIId = lsDto.getKundeIIdRechnungsadresse();
				belegVerkaufDto = lsDto;

				belegpositionVerkaufDtos = DelegateFactory.getInstance()
						.getLieferscheinpositionDelegate()
						.lieferscheinpositionFindByLieferscheinIId(belegIId);

			} else if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)
					|| belegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {

				RechnungDto reDto = DelegateFactory.getInstance()
						.getRechnungDelegate()
						.rechnungFindByPrimaryKey(belegIId);
				kundeIId = reDto.getKundeIId();
				belegVerkaufDto = reDto;
				belegpositionVerkaufDtos = DelegateFactory.getInstance()
						.getRechnungDelegate()
						.rechnungPositionFindByRechnungIId(belegIId);
			}

			if (kundeIId != null && belegVerkaufDto != null
					&& belegpositionVerkaufDtos != null) {
				boolean b = systemFac
						.enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
								kundeIId, belegpositionVerkaufDtos,
								belegVerkaufDto, LPMain.getTheClient());
				if (b == true) {

					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.warning"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"lp.vkbelege.error.kundesteuerfrei"));
				}
				
				
				b = systemFac
						.enthaeltEineVKPositionKeineMwstObwohlKundeSteuerpflichtig(
								kundeIId, belegpositionVerkaufDtos,
								belegVerkaufDto, LPMain.getTheClient());
				if (b == true) {

					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.warning"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"lp.vkbelege.error.kundesteuerpflichtig"));
				}
				
			}

		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

}
