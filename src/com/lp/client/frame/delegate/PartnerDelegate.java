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

import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.ejb.PASelektionPK;
import com.lp.server.partner.service.AnredeDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.KontaktDto;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerImportDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.PartnerartDto;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.partner.service.PartnerkommunikationDto;

@SuppressWarnings("static-access")
public class PartnerDelegate extends Delegate {
	private Context context;
	private PartnerFac partnerFac;
	private PartnerServicesFac partnerServicesFac;

	public PartnerDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			partnerFac = (PartnerFac) context
					.lookup("lpserver/PartnerFacBean/remote");
			partnerServicesFac = (PartnerServicesFac) context
					.lookup("lpserver/PartnerServicesFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public Integer createPartner(PartnerDto partnerDto) throws ExceptionLP {

		Integer iId = null;
		try {
			iId = partnerFac.createPartner(partnerDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Map getAllPersonenWiedervorlage() throws ExceptionLP {

		Map iId = null;
		try {
			iId = partnerFac.getAllPersonenWiedervorlage(LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createKontakt(KontaktDto kontaktDto) throws ExceptionLP {

		Integer iId = null;
		try {
			iId = partnerFac.createKontakt(kontaktDto,LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public String formatBriefAnrede(PartnerDto partnerDto, Locale loc)
			throws ExceptionLP {
		try {
			return partnerFac.formatBriefAnrede(partnerDto, loc, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void importierePartner(PartnerImportDto[] daten,
			boolean bErzeugeKunde) throws ExceptionLP {
		try {
			partnerFac.importierePartner(daten, LPMain.getTheClient(),
					bErzeugeKunde);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String pruefeCSVImport(PartnerImportDto[] daten) throws ExceptionLP {
		try {
			return partnerFac.pruefeCSVImport(daten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return "";
		}
	}

	public void removePartner(PartnerDto partnerDto) throws ExceptionLP {
		try {
			partnerFac.removePartner(partnerDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePartner(PartnerDto partnerDto) throws ExceptionLP {

		try {
			partnerFac.updatePartner(partnerDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKontakt(KontaktDto kontaktDto) throws ExceptionLP {

		try {
			partnerFac.updateKontakt(kontaktDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void zusammenfuehrenPartner(PartnerDto partnerZielDto,
			int partnerQuellDtoIid, boolean kundeMitverdichten,
			boolean lieferantMitverdichten, boolean bankMitverdichten)
			throws ExceptionLP {

		try {
			partnerFac.zusammenfuehrenPartner(partnerZielDto,
					partnerQuellDtoIid, kundeMitverdichten,
					lieferantMitverdichten, bankMitverdichten, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public PartnerDto partnerFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return partnerFac.partnerFindByPrimaryKey(iId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KontaktDto kontaktFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return partnerFac.kontaktFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PartnerDto[] partnerFindByPrimaryName1(String sName1)
			throws ExceptionLP {

		try {
			return partnerFac.partnerFindByName1(sName1, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPartnerkommunikation(
			PartnerkommunikationDto partnerkommunikationDto) throws ExceptionLP {

		Integer iId = null;
		try {
			iId = partnerFac.createPartnerkommunikation(
					partnerkommunikationDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removePartnerkommunikation(Integer iId) throws ExceptionLP {

		try {
			partnerFac.removePartnerkommunikation(iId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updatePartnerkommunikation(
			PartnerkommunikationDto partnerkommunikationDto) throws ExceptionLP {

		try {
			partnerFac.updatePartnerkommunikation(partnerkommunikationDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public PartnerkommunikationDto partnerkommunikationFindByPrimaryKey(
			Integer iId) throws ExceptionLP {

		PartnerkommunikationDto p = null;
		try {
			p = partnerFac.partnerkommunikationFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return p;
	}

	public String passeInlandsAuslandsVorwahlAn(
			Integer partnerIId, String cTelefonnummer)
			throws ExceptionLP {

		String p = null;
		try {
			p = partnerFac.passeInlandsAuslandsVorwahlAn(partnerIId, LPMain
					.getInstance().getTheClient().getMandant(), cTelefonnummer, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return p;
	}

	/**
	 * Hole die Komm. (E-Mail, Fax, Telefon, ...) einer cNrKommunikationsartI
	 * von einem iIdPartnerAnsprechpartnerI bei einem iIdPartnerI oder <BR/>
	 * wenn iIdAnsprechpartnerI == null die des iIdPartnerI.
	 * 
	 * @param iIdPartnerI
	 *            Integer
	 * @param iIdPartnerAnsprechpartnerI
	 *            Integer
	 * @param cNrKommArtI
	 *            String
	 * @param cNrMandantI
	 *            String
	 * @return PartnerkommunikationDto
	 * @throws ExceptionLP
	 */
	public String partnerkommFindOhneExec(Integer iIdPartnerI,
			Integer iIdPartnerAnsprechpartnerI, String cNrKommArtI,
			String cNrMandantI) throws ExceptionLP {

		String partnerkommunikationDto = null;
		try {
			partnerkommunikationDto = partnerFac.partnerkommFindOhneExec(
					iIdPartnerI, iIdPartnerAnsprechpartnerI, cNrKommArtI,
					cNrMandantI, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return partnerkommunikationDto;
	}

	// /**
	// * Hole fuer den iIdAnsprechpartnerI die Komm. (E-Mail, Fax, Telefon, ...)
	// * wenn er keine hat die des iIdPartnerI.
	// *
	// * @param iIdPartnerAnsprechpartnerI Integer
	// * @param iIdPartnerI Integer
	// * @param cNrKommArtI String
	// * @param cNrMandantI String
	// * @return PartnerkommunikationDto
	// * @throws ExceptionLP
	// */
	// public PartnerkommunikationDto partnerkommFindRespectPartnerOhneExec(
	// Integer iIdPartnerAnsprechpartnerI,
	// Integer iIdPartnerI,
	// String cNrKommArtI,
	// String cNrMandantI)
	// throws ExceptionLP {
	//
	// PartnerkommunikationDto p = null;
	// try {
	// p = partnerFac.
	// partnerkommFindRespectPartnerOhneExec(
	// iIdPartnerAnsprechpartnerI,
	// iIdPartnerI,
	// cNrKommArtI,
	// cNrMandantI,
	// LPMain.getInstance().getTheClient());
	// }
	// catch (Throwable ex) {
	// handleThrowable(ex);
	// }
	// return p;
	// }

	// *** Anrede
	// *******************************************************************
	public String createAnrede(AnredeDto anredeDto) throws ExceptionLP {

		String cNr = null;
		try {
			cNr = partnerFac.createAnrede(anredeDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return cNr;
	}

	public void removeAnrede(AnredeDto anredeDtoI) throws ExceptionLP {
		try {
			partnerFac.removeAnrede(anredeDtoI.getCNr(), LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAnrede(AnredeDto anredeDto) throws ExceptionLP {

		try {
			partnerFac.updateAnrede(anredeDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public AnredeDto anredeFindByPrimaryKey(String cNr) throws ExceptionLP {
		AnredeDto a = null;
		try {
			a = partnerFac.anredeFindByPrimaryKey(cNr, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return a;
	}

	public Integer createPartnerklasse(PartnerklasseDto partnerklasseDto)
			throws ExceptionLP {

		Integer iId = null;
		try {
			iId = partnerFac.createPartnerklasse(partnerklasseDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removePartnerklasse(Integer iIdI) throws ExceptionLP {
		try {
			partnerFac.removePartnerklasse(iIdI, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePartnerklasse(PartnerklasseDto partnerklasseDtoI)
			throws ExceptionLP {
		try {
			partnerFac.removePartnerklasse(partnerklasseDtoI.getIId(), LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePartnerklasse(PartnerklasseDto partnerklasseDto)
			throws ExceptionLP {
		try {
			partnerFac.updatePartnerklasse(partnerklasseDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public PartnerklasseDto partnerklasseFindByPrimaryKey(Integer iIdI)
			throws ExceptionLP {
		PartnerklasseDto p = null;
		try {
			p = partnerFac.partnerklasseFindByPrimaryKey(iIdI, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return p;
	}

	public Map<?, ?> getAllAnreden(Locale locAnreden) throws ExceptionLP {

		Map<?, ?> m = null;
		try {
			m = partnerFac.getAllAnreden(locAnreden);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}

	// *** Branche
	// ****************************************************************
	public Integer createBranche(BrancheDto brancheDto) throws ExceptionLP {

		Integer iId = null;
		try {
			iId = partnerServicesFac.createBranche(brancheDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeBranche(Integer iId) throws ExceptionLP {
		try {
			partnerServicesFac.removeBranche(iId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBranche(BrancheDto brancheDto) throws ExceptionLP {
		try {
			partnerServicesFac.removeBranche(brancheDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String enrichNumber(Integer iIdPartnerI,
			String cNrKommunikationsartI,
			String cTelefon,
			boolean bNurNummerAbschneiden) throws ExceptionLP {
		try {
			return partnerFac.enrichNumber(iIdPartnerI, cNrKommunikationsartI,
					LPMain.getInstance().getTheClient(), cTelefon,
					bNurNummerAbschneiden);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateBranche(BrancheDto brancheDto) throws ExceptionLP {
		try {
			partnerServicesFac.updateBranche(brancheDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BrancheDto brancheFindByPrimaryKey(Integer iId) throws ExceptionLP {
		BrancheDto b = null;
		try {
			b = partnerServicesFac.brancheFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return b;
	}

	// *** Partnerart
	// ***************************************************************
	public PartnerartDto partnerartFindByPrimaryKey(String cNrI)
			throws ExceptionLP {

		PartnerartDto b = null;
		try {
			b = partnerFac.partnerartFindByPrimaryKey(cNrI, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return b;
	}

	public Map<?, ?> getAllPartnerArten(String cNrLocaleI) throws ExceptionLP {

		Map<?, ?> m = null;
		try {
			m = partnerFac.getAllPartnerArten(cNrLocaleI, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}

	public void updatePartnerart(PartnerartDto partnerartDtoI)
			throws ExceptionLP {
		try {
			partnerFac.updatePartnerart(partnerartDtoI, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePartnerart(PartnerartDto partnerartDtoI)
			throws ExceptionLP {
		try {
			partnerFac.removePartnerart(partnerartDtoI, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKontakt(KontaktDto kontaktDto) throws ExceptionLP {
		try {
			partnerFac.removeKontakt(kontaktDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String createPartnerart(PartnerartDto partnerartDtoI)
			throws ExceptionLP {

		String cNr = null;
		try {
			cNr = partnerFac.createPartnerart(partnerartDtoI, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return cNr;
	}

	//**************************************************************************
	// ****
	public PASelektionPK createPASelektion(PASelektionDto pASelektionDto)
			throws ExceptionLP {
		PASelektionPK pASelektionPK = null;
		try {
			pASelektionPK = partnerFac.createPASelektion(pASelektionDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return pASelektionPK;
	}

	public void removePASelektion(PASelektionPK pASelektionPKI)
			throws ExceptionLP {
		try {
			partnerFac.removePASelektion(pASelektionPKI, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePASelektion(PASelektionDto pASelektionDto)
			throws ExceptionLP {
		try {
			partnerFac.updatePASelektion(pASelektionDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public PASelektionDto pASelektionFindByPrimaryKey(
			PASelektionPK pASelektionPKI) throws ExceptionLP {
		PASelektionDto pASelektionDto = null;
		try {
			pASelektionDto = partnerFac.pASelektionFindByPrimaryKey(
					pASelektionPKI, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return pASelektionDto;
	}

	//**************************************************************************
	// ****
	public Integer createKurzbrief(KurzbriefDto kurzbriefDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			iId = partnerFac.createKurzbrief(kurzbriefDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeKurzbrief(Integer iId) throws ExceptionLP {
		try {
			partnerFac
					.removeKurzbrief(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKurzbrief(KurzbriefDto kurzbriefDto) throws ExceptionLP {
		try {
			partnerFac.updateKurzbrief(kurzbriefDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KurzbriefDto kurzbriefFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		KurzbriefDto k = null;
		try {
			k = partnerFac.kurzbriefFindByPrimaryKey(iId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return k;
	}

	public PartnerkommunikationDto[] partnerkommFindByPartnerIIdKommunikationsartPAiIdKommArtMandant(
			Integer iIdPartnerI, String cNrKommunikationsartI,
			String cNrMandantI) throws ExceptionLP {
		PartnerkommunikationDto[] k = null;
		try {
			k = partnerFac
					.partnerkommFindByPartnerIIdKommunikationsartPAiIdKommArtMandant(
							iIdPartnerI, cNrKommunikationsartI, cNrMandantI,
							LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return k;
	}

	public String formatFixAnredeTitelName2Name1(PartnerDto partnerDto,
			Locale loc) throws ExceptionLP {
		try {
			return partnerFac.formatFixAnredeTitelName2Name1(partnerDto, loc,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

}
