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

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ArtikelzulageDto;
import com.lp.server.personal.service.BereitschaftartDto;
import com.lp.server.personal.service.BereitschafttagDto;
import com.lp.server.personal.service.BerufDto;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.EintrittaustrittDto;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.personal.service.FahrzeugkostenDto;
import com.lp.server.personal.service.FeiertagDto;
import com.lp.server.personal.service.GleitzeitsaldoDto;
import com.lp.server.personal.service.KollektivDto;
import com.lp.server.personal.service.Kollektivuestd50Dto;
import com.lp.server.personal.service.KollektivuestdDto;
import com.lp.server.personal.service.LohnartDto;
import com.lp.server.personal.service.LohnartstundenfaktorDto;
import com.lp.server.personal.service.LohngruppeDto;
import com.lp.server.personal.service.PendlerpauschaleDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.PersonalangehoerigeDto;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.PersonalgruppeDto;
import com.lp.server.personal.service.PersonalgruppekostenDto;
import com.lp.server.personal.service.PersonalverfuegbarkeitDto;
import com.lp.server.personal.service.PersonalzeitenDto;
import com.lp.server.personal.service.PersonalzeitmodellDto;
import com.lp.server.personal.service.ReligionDto;
import com.lp.server.personal.service.SchichtzeitmodellDto;
import com.lp.server.personal.service.StundenabrechnungDto;
import com.lp.server.personal.service.UrlaubsanspruchDto;
import com.lp.server.personal.service.ZulageDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.report.PersonRpt;

@SuppressWarnings("static-access")
public class PersonalDelegate extends Delegate {
	private Context context;
	private PersonalFac personalFac;

	public PersonalDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			personalFac = (PersonalFac) context
					.lookup("lpserver/PersonalFacBean/remote");
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}
	}

	public Integer createPersonal(PersonalDto personalDto) throws ExceptionLP {
		try {
			return personalFac.createPersonal(personalDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalgruppe(PersonalgruppeDto personalgruppeDto)
			throws ExceptionLP {
		try {
			return personalFac.createPersonalgruppe(personalgruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto) throws ExceptionLP {
		try {
			return personalFac
					.createPersonalgruppekosten(personalgruppekostenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public int getAnzahlDerZeitmodelleEinerPerson(Integer personalIId)
			throws ExceptionLP {
		try {
			return personalFac.getAnzahlDerZeitmodelleEinerPerson(personalIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return 0;
		}
	}

	public void removePersonal(PersonalDto personalDto) throws ExceptionLP {
		try {
			personalFac.removePersonal(personalDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePersonalgruppe(PersonalgruppeDto personalgruppeDto)
			throws ExceptionLP {
		try {
			personalFac.removePersonalgruppe(personalgruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto) throws ExceptionLP {
		try {
			personalFac.removePersonalgruppekosten(personalgruppekostenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public JasperPrintLP printPersonalliste(java.sql.Timestamp tsStichtag,
			boolean bMitBarcodes, boolean bMitVersteckten, int iOptionSortierung)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = personalFac.printPersonalliste(tsStichtag, bMitBarcodes,
					bMitVersteckten, iOptionSortierung, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public PersonRpt getPersonRpt(Integer personalIId) throws ExceptionLP {
		try {
			return personalFac.getPersonRpt(personalIId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removePersonalgehalt(PersonalgehaltDto personalgehaltDto)
			throws ExceptionLP {
		try {
			personalFac.removePersonalgehalt(personalgehaltDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws ExceptionLP {
		try {
			personalFac.removePersonalverfuegbarkeit(personalverfuegbarkeitDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto) throws ExceptionLP {
		try {
			personalFac.removeStundenabrechnung(stundenabrechnungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePersonalzeiten(PersonalzeitenDto personalzeitenDto)
			throws ExceptionLP {
		try {
			personalFac.removePersonalzeiten(personalzeitenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto) throws ExceptionLP {
		try {
			personalFac.removePersonalzeitmodell(personalzeitmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto) throws ExceptionLP {
		try {
			personalFac.removeSchichtzeitmodell(schichtzeitmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Map<?, ?> getAllSprPersonalfunktionen() throws ExceptionLP {
		try {
			return personalFac.getAllSprPersonalfunktionen(LPMain.getInstance()
					.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto[] getAllPersonenOhneEintragInEintrittAustritt(
			boolean bPlusVersteckte, int iOption) throws ExceptionLP {
		try {
			return personalFac.getAllPersonenOhneEintragInEintrittAustritt(
					LPMain.getInstance().getTheClient(), new Boolean(
							bPlusVersteckte), new Integer(iOption));
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprPersonalarten() throws ExceptionLP {
		try {
			return personalFac.getAllSprPersonalarten(LPMain.getInstance()
					.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HashMap<?, ?> getAllKurzzeichen() throws ExceptionLP {
		try {
			return personalFac.getAllKurzzeichen();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllLohnstundenarten() throws ExceptionLP {
		try {
			return personalFac.getAllLohnstundenarten();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprFamilienstaende() throws ExceptionLP {
		try {
			return personalFac.getAllSprFamilienstaende(LPMain.getInstance()
					.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprAngehoerigenarten() throws ExceptionLP {
		try {
			return personalFac.getAllSprangehoerigenarten(LPMain.getInstance()
					.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updatePersonal(PersonalDto personalDto) throws ExceptionLP {
		try {
			personalFac.updatePersonal(personalDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBereitschaftart(BereitschaftartDto dto)
			throws ExceptionLP {
		try {
			personalFac.updateBereitschaftart(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBereitschafttag(BereitschafttagDto dto)
			throws ExceptionLP {
		try {
			personalFac.updateBereitschafttag(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalgruppe(PersonalgruppeDto personalgruppeDto)
			throws ExceptionLP {
		try {
			personalFac.updatePersonalgruppe(personalgruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto) throws ExceptionLP {
		try {
			personalFac.updatePersonalgruppekosten(personalgruppekostenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalKommentar(PersonalDto personalDto)
			throws ExceptionLP {
		try {
			personalFac.updatePersonalKommentar(personalDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalgehalt(PersonalgehaltDto personalgehaltDto)
			throws ExceptionLP {
		try {
			personalFac.updatePersonalgehalt(personalgehaltDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws ExceptionLP {
		try {
			personalFac.updatePersonalverfuegbarkeit(personalverfuegbarkeitDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto)
			throws ExceptionLP {
		try {
			personalFac.updateGleitzeitsaldo(gleitzeitsaldoDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBetriebskalender(BetriebskalenderDto betriebskalenderDto)
			throws ExceptionLP {
		try {
			personalFac.updateBetriebskalender(betriebskalenderDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateFeiertag(FeiertagDto feiertagDto) throws ExceptionLP {
		try {
			personalFac.updateFeiertag(feiertagDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateFahrzeug(FahrzeugDto dto) throws ExceptionLP {
		try {
			personalFac
					.updateFahrzeug(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateFahrzeugkosten(FahrzeugkostenDto dto) throws ExceptionLP {
		try {
			personalFac.updateFahrzeugkosten(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public PersonalDto personalFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return personalFac.personalFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalgruppeDto personalgruppeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.personalgruppeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalgruppekostenDto personalgruppekostenFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return personalFac.personalgruppekostenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto[] personalFindByMandantCNr() throws ExceptionLP {
		try {
			return personalFac.personalFindByMandantCNr(LPMain.getTheClient()
					.getMandant(), false);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto[] personalFindAllArbeiterEinesMandanten()
			throws ExceptionLP {
		try {
			return personalFac.personalFindAllArbeiterEinesMandanten(LPMain
					.getTheClient().getMandant(), false);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto[] personalFindAllAngestellteEinesMandanten()
			throws ExceptionLP {
		try {
			return personalFac.personalFindAllAngestellteEinesMandanten(LPMain
					.getTheClient().getMandant(), false);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto personalFindByCAusweis(String cAusweis)
			throws ExceptionLP {
		try {
			return personalFac.personalFindByCAusweis(cAusweis);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalzeitenDto personalzeitenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.personalzeitenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalzeitmodellDto personalzeitmodellFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.personalzeitmodellFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SchichtzeitmodellDto schichtzeitmodellFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.schichtzeitmodellFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalzeitmodellDto personalzeitmodellFindZeitmodellZuDatum(
			Integer personalIId, java.sql.Timestamp dDatum) throws ExceptionLP {
		try {
			return personalFac.personalzeitmodellFindZeitmodellZuDatum(
					personalIId, dDatum, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto personalFindByPartnerIIdMandantCNr(Integer partnerIId,
			String mandantCNr) throws ExceptionLP {
		try {
			return personalFac.personalFindByPartnerIIdMandantCNr(partnerIId,
					mandantCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto personalFindByCPersonalnrMandantCNrOhneExc(
			String cPersonalnr) throws ExceptionLP {
		try {
			return personalFac.personalFindByCPersonalnrMandantCNrOhneExc(
					cPersonalnr, LPMain.getInstance().getTheClient()
							.getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalDto[] personalFindByMandantCNrPersonalfunktionCNr(
			String mandantCNr, String personalfunktionCNr) throws ExceptionLP {
		try {
			return personalFac.personalFindByMandantCNrPersonalfunktionCNr(
					mandantCNr, personalfunktionCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createLohngruppe(LohngruppeDto lohngruppeDto)
			throws ExceptionLP {
		try {
			return personalFac.createLohngruppe(lohngruppeDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createUrlaubsanspruch(UrlaubsanspruchDto lohngruppeDto)
			throws ExceptionLP {
		try {
			return personalFac.createUrlaubsanspruch(lohngruppeDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeLohngruppe(Integer iId) throws ExceptionLP {
		try {
			personalFac.removeLohngruppe(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLohngruppe(LohngruppeDto lohngruppeDto)
			throws ExceptionLP {
		try {
			personalFac.updateLohngruppe(lohngruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LohngruppeDto lohngruppeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.lohngruppeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BetriebskalenderDto betriebskalenderFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.betriebskalenderFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FeiertagDto feiertagFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return personalFac.feiertagFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FahrzeugDto fahrzeugFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return personalFac.fahrzeugFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FahrzeugkostenDto fahrzeugkostenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.fahrzeugkostenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BetriebskalenderDto[] betriebskalenderFindByMandantCNrTagesartCNr(
			String tagesartCNr) throws ExceptionLP {
		try {
			return personalFac.betriebskalenderFindByMandantCNrTagesartCNr(
					tagesartCNr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BetriebskalenderDto betriebskalenderFindByMandantCNrDDatum(
			java.sql.Timestamp datum) throws ExceptionLP {
		try {
			return personalFac.betriebskalenderFindByMandantCNrDDatum(datum,
					LPMain.getInstance().getTheClient().getMandant(), LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createReligion(ReligionDto religionDto) throws ExceptionLP {
		try {
			return personalFac.createReligion(religionDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeReligion(ReligionDto dto) throws ExceptionLP {
		try {
			personalFac.removeReligion(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeUrlaubsanspruch(UrlaubsanspruchDto dto)
			throws ExceptionLP {
		try {
			personalFac.removeUrlaubsanspruch(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateReligion(ReligionDto religionDto) throws ExceptionLP {
		try {
			personalFac.updateReligion(religionDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto) throws ExceptionLP {
		try {
			personalFac.updateStundenabrechnung(stundenabrechnungDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateUrlaubsanspruch(UrlaubsanspruchDto dto)
			throws ExceptionLP {
		try {
			personalFac.updateUrlaubsanspruch(dto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ReligionDto religionFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return personalFac.religionFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalgehaltDto personalgehaltFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.personalgehaltFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalverfuegbarkeitDto personalverfuegbarkeitFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return personalFac.personalverfuegbarkeitFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalgehaltDto personalgehaltFindLetztePersonalgehalt(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws ExceptionLP {
		try {
			return personalFac.personalgehaltFindLetztePersonalgehalt(
					personalIId, iJahr, iMonat);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public UrlaubsanspruchDto urlaubsanspruchFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.urlaubsanspruchFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public UrlaubsanspruchDto[] urlaubsanspruchFindByPersonalIIdIJahrKleiner(
			Integer personalIId, Integer iJahr) throws ExceptionLP {
		try {
			return personalFac.urlaubsanspruchFindByPersonalIIdIJahrKleiner(
					personalIId, iJahr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalangehoerige(
			PersonalangehoerigeDto personalangehoerigeDto) throws ExceptionLP {
		try {
			return personalFac
					.createPersonalangehoerige(personalangehoerigeDto, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalzeiten(PersonalzeitenDto personalzeitenDto)
			throws ExceptionLP {
		try {
			return personalFac.createPersonalzeiten(personalzeitenDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws ExceptionLP {
		try {
			return personalFac
					.createPersonalverfuegbarkeit(personalverfuegbarkeitDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto) throws ExceptionLP {
		try {
			return personalFac.createPersonalzeitmodell(personalzeitmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto) throws ExceptionLP {
		try {
			return personalFac.createSchichtzeitmodell(schichtzeitmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removePersonalangehoerige(Integer iId) throws ExceptionLP {
		try {
			personalFac.removePersonalangehoerige(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalangehoerige(
			PersonalangehoerigeDto personalangehoerigeDto) throws ExceptionLP {
		try {
			personalFac.updatePersonalangehoerige(personalangehoerigeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalzeiten(PersonalzeitenDto personalzeitenDto)
			throws ExceptionLP {
		try {
			personalFac.updatePersonalzeiten(personalzeitenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto) throws ExceptionLP {
		try {
			personalFac.updatePersonalzeitmodell(personalzeitmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto) throws ExceptionLP {
		try {
			personalFac.updateSchichtzeitmodell(schichtzeitmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public PersonalangehoerigeDto personalangehoerigeFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return personalFac.personalangehoerigeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createKollektiv(KollektivDto kollektivDto)
			throws ExceptionLP {
		try {
			return personalFac.createKollektiv(kollektivDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws ExceptionLP {
		try {
			return personalFac.createKollektivuestd(kollektivuestdDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createKollektivuestd50(
			Kollektivuestd50Dto kollektivuestd50Dto) throws ExceptionLP {
		try {
			return personalFac.createKollektivuestd50(kollektivuestd50Dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZulage(ZulageDto zulageDto) throws ExceptionLP {
		try {
			return personalFac.createZulage(zulageDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createLohnart(LohnartDto lohnartDto) throws ExceptionLP {
		try {
			return personalFac.createLohnart(lohnartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createLohnartstundenfaktor(
			LohnartstundenfaktorDto lohnartstundenfaktorDto) throws ExceptionLP {
		try {
			return personalFac
					.createLohnartstundenfaktor(lohnartstundenfaktorDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikelzulage(ArtikelzulageDto artikelzulageDto)
			throws ExceptionLP {
		try {
			return personalFac.createArtikelzulage(artikelzulageDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createFahrzeug(FahrzeugDto dto) throws ExceptionLP {
		try {
			return personalFac.createFahrzeug(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createFahrzeugkosten(FahrzeugkostenDto dto)
			throws ExceptionLP {
		try {
			return personalFac.createFahrzeugkosten(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createBereitschafttag(BereitschafttagDto dto)
			throws ExceptionLP {
		try {
			return personalFac.createBereitschafttag(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void feiertageAusVorlageFuerJahrEintragen(Integer iJahr)
			throws ExceptionLP {
		try {
			personalFac.feiertageAusVorlageFuerJahrEintragen(iJahr, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public Integer createBereitschaftart(BereitschaftartDto dto)
			throws ExceptionLP {
		try {
			return personalFac.createBereitschaftart(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalgehalt(PersonalgehaltDto personalgehaltDto)
			throws ExceptionLP {
		try {
			return personalFac.createPersonalgehalt(personalgehaltDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeKollektiv(Integer iId) throws ExceptionLP {
		try {
			personalFac.removeKollektiv(iId);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws ExceptionLP {
		try {
			personalFac.removeKollektivuestd(kollektivuestdDto);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKollektivuestd(Kollektivuestd50Dto kollektivuestd50Dto)
			throws ExceptionLP {
		try {
			personalFac.removeKollektivuestd50(kollektivuestd50Dto);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZulage(Integer iId) throws ExceptionLP {
		try {
			personalFac.removeZulage(iId);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBereitschaftart(BereitschaftartDto dto)
			throws ExceptionLP {
		try {
			personalFac.removeBereitschaftart(dto);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBereitschafttag(BereitschafttagDto dto)
			throws ExceptionLP {
		try {
			personalFac.removeBereitschafttag(dto);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLohnart(Integer iId) throws ExceptionLP {
		try {
			personalFac.removeLohnart(iId);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLohnartstundenfaktor(Integer iId) throws ExceptionLP {
		try {
			personalFac.removeLohnartstundenfaktor(iId);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikelzulage(Integer iId) throws ExceptionLP {
		try {
			personalFac.removeArtikelzulage(iId);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto)
			throws ExceptionLP {
		try {
			personalFac.removeGleitzeitsaldo(gleitzeitsaldoDto);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKollektiv(KollektivDto kollektivDto) throws ExceptionLP {
		try {
			personalFac.updateKollektiv(kollektivDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws ExceptionLP {
		try {
			personalFac.updateKollektivuestd(kollektivuestdDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKollektivuestd50(Kollektivuestd50Dto kollektivuestd50Dto)
			throws ExceptionLP {
		try {
			personalFac.updateKollektivuestd50(kollektivuestd50Dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZulage(ZulageDto zulageDto) throws ExceptionLP {
		try {
			personalFac.updateZulage(zulageDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLohnart(LohnartDto lohnartDto) throws ExceptionLP {
		try {
			personalFac.updateLohnart(lohnartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLohnartstundenfaktor(
			LohnartstundenfaktorDto lohnartstundenfaktorDto) throws ExceptionLP {
		try {
			personalFac.updateLohnartstundenfaktor(lohnartstundenfaktorDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikelzulage(ArtikelzulageDto artikelzulageDto)
			throws ExceptionLP {
		try {
			personalFac.updateArtikelzulage(artikelzulageDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KollektivDto kollektivFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.kollektivFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KollektivuestdDto kollektivuestdFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.kollektivuestdFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Kollektivuestd50Dto kollektivuestd50FindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.kollektivuestd50FindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZulageDto zulageFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return personalFac.zulageFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LohnartDto lohnartFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return personalFac.lohnartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LohnartstundenfaktorDto lohnartstundenfaktorFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return personalFac.lohnartstundenfaktorFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelzulageDto artikelzulageFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.artikelzulageFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BereitschaftartDto bereitschaftartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.bereitschaftartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BereitschafttagDto bereitschafttagFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.bereitschafttagFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPendlerpauschale(
			PendlerpauschaleDto pendlerpauschaleDto) throws ExceptionLP {
		try {
			return personalFac.createPendlerpauschale(pendlerpauschaleDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removePendlerpauschale(Integer iId) throws ExceptionLP {
		try {
			personalFac.removePendlerpauschale(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePendlerpauschale(PendlerpauschaleDto pendlerpauschaleDto)
			throws ExceptionLP {
		try {
			personalFac.updatePendlerpauschale(pendlerpauschaleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public PendlerpauschaleDto pendlerpauschaleFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.pendlerpauschaleFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createBeruf(BerufDto berufDto) throws ExceptionLP {
		try {
			return personalFac.createBeruf(berufDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto) throws ExceptionLP {
		try {
			return personalFac.createStundenabrechnung(stundenabrechnungDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto)
			throws ExceptionLP {
		try {
			return personalFac.createGleitzeitsaldo(gleitzeitsaldoDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createBetriebskalender(
			BetriebskalenderDto betriebskalenderDto) throws ExceptionLP {
		try {
			return personalFac.createBetriebskalender(betriebskalenderDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createFeiertag(FeiertagDto feiertagDto) throws ExceptionLP {
		try {
			return personalFac.createFeiertag(feiertagDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeBeruf(Integer iId) throws ExceptionLP {
		try {
			personalFac.removeBeruf(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void schichtzeitenVorausplanen(Integer personalIId,
			Integer zeitmodellIId1, Integer zeitmodellIId2,
			Integer zeitmodellIId3, Integer zeitmodellIId4, Integer iKwVon,
			Integer iKwBis, Integer iJahrVon, Integer iJahrBis,
			String tagCNrSchichtwechsel) throws ExceptionLP {
		try {
			personalFac.schichtzeitenVorausplanen(personalIId, zeitmodellIId1,
					zeitmodellIId2, zeitmodellIId3, zeitmodellIId4, iKwVon,
					iKwBis, iJahrVon, iJahrBis, tagCNrSchichtwechsel);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBetriebskalender(BetriebskalenderDto betriebskalenderDto)
			throws ExceptionLP {
		try {
			personalFac.removeBetriebskalender(betriebskalenderDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeFeiertag(FeiertagDto feiertagDto) throws ExceptionLP {
		try {
			personalFac.removeFeiertag(feiertagDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeFahrzeug(FahrzeugDto dto) throws ExceptionLP {
		try {
			personalFac.removeFahrzeug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeFahrzeugkosten(FahrzeugkostenDto dto) throws ExceptionLP {
		try {
			personalFac.removeFahrzeugkosten(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBeruf(BerufDto berufDto) throws ExceptionLP {
		try {
			personalFac.updateBeruf(berufDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BerufDto berufFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return personalFac.berufFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public StundenabrechnungDto stundenabrechnungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.stundenabrechnungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public GleitzeitsaldoDto gleitzeitsaldoFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.gleitzeitsaldoFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public GleitzeitsaldoDto gleitzeitsaldoFindLetztenGleitzeitsaldo(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws ExceptionLP {
		try {
			return personalFac.gleitzeitsaldoFindLetztenGleitzeitsaldo(
					personalIId, iJahr, iMonat);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createEintrittaustritt(
			EintrittaustrittDto eintrittaustrittDto) throws ExceptionLP {
		try {
			return personalFac.createEintrittaustritt(eintrittaustrittDto,
					LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeEintrittaustritt(Integer iId) throws ExceptionLP {
		try {
			personalFac.removeEintrittaustritt(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String getNextPersonalnummer() throws ExceptionLP {
		try {
			return personalFac.getNextPersonalnummer(LPMain.getInstance()
					.getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Double getAuslastungEinerPerson(Integer personalIId)
			throws ExceptionLP {
		try {
			return personalFac.getAuslastungEinerPerson(personalIId);

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
			Integer personalIId) throws ExceptionLP {
		try {
			return personalFac
					.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(personalIId);

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateEintrittaustritt(EintrittaustrittDto eintrittaustrittDto)
			throws ExceptionLP {
		try {
			personalFac.updateEintrittaustritt(eintrittaustrittDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EintrittaustrittDto eintrittaustrittFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return personalFac.eintrittaustrittFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getSignatur(Integer personalIId, String locale)
			throws ExceptionLP {
		try {
			return personalFac.getSignatur(personalIId, locale);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateSignatur(Integer personalIId, String xSignatur)
			throws ExceptionLP {
		try {
			personalFac.updateSignatur(personalIId, xSignatur, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EintrittaustrittDto eintrittaustrittFindLetztenEintrittBisDatum(
			Integer personalIId) throws ExceptionLP {
		try {
			return personalFac.eintrittaustrittFindLetztenEintrittBisDatum(
					personalIId,
					new java.sql.Timestamp(System.currentTimeMillis()));
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
