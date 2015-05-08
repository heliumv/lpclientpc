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

import java.util.ArrayList;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.ejb.SerienbriefselektionPK;
import com.lp.server.partner.ejb.SerienbriefselektionnegativPK;
import com.lp.server.partner.service.KommunikationsartDto;
import com.lp.server.partner.service.KontaktartDto;
import com.lp.server.partner.service.KundeSelectCriteriaDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.PartnerkommentarDto;
import com.lp.server.partner.service.PartnerkommentarartDto;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.partner.service.SelektionsprDto;
import com.lp.server.partner.service.SerienbriefDto;
import com.lp.server.partner.service.SerienbriefselektionDto;
import com.lp.server.partner.service.SerienbriefselektionnegativDto;
import com.lp.server.stueckliste.service.StuecklisteDto;

@SuppressWarnings("static-access")
public class PartnerServicesDelegate extends Delegate {
	private Context context;
	private PartnerServicesFac partnerServicesFac;

	public PartnerServicesDelegate() throws Exception {
		context = new InitialContext();
		partnerServicesFac = (PartnerServicesFac) context
				.lookup("lpserver/PartnerServicesFacBean/remote");
	}

	public String createKommunikationsart(
			KommunikationsartDto kommunikationsartDto) throws ExceptionLP {

		String k = null;
		try {
			k = partnerServicesFac.createKommunikationsart(
					kommunikationsartDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return k;
	}

	public void removeKommunikationsart(String cNr) throws ExceptionLP {

		try {
			partnerServicesFac.removeKommunikationsart(cNr, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKommunikationsart(
			KommunikationsartDto kommunikationsartDto) throws ExceptionLP {
		try {
			partnerServicesFac.removeKommunikationsart(kommunikationsartDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKommunikationsart(
			KommunikationsartDto kommunikationsartDto) throws ExceptionLP {
		try {
			partnerServicesFac.updateKommunikationsart(kommunikationsartDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KommunikationsartDto kommunikationsartFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		KommunikationsartDto k = null;
		try {
			k = partnerServicesFac.kommunikationsartFindByPrimaryKey(cNr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return k;
	}

	public KommunikationsartDto[] kommunikationsartFindAll() throws ExceptionLP {

		KommunikationsartDto[] ak = null;
		try {
			ak = partnerServicesFac.kommunikationsartFindAll();
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ak;
	}

	public KundeSelectCriteriaDto getSerienbriefSelektionsKriterien(
			Integer serienbriefIId) throws ExceptionLP {

		KundeSelectCriteriaDto m = null;
		try {
			m = partnerServicesFac.getSerienbriefSelektionsKriterien(
					serienbriefIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}

	public Map<?, ?> getAllKommunikationsArten(String spracheCNr)
			throws ExceptionLP {

		Map<?, ?> m = null;
		try {
			m = partnerServicesFac.getAllKommunikationsArten(spracheCNr, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}

	public String getBriefanredeFuerBeleg(Integer ansprechpartnerIId,
			Integer partnerIId) throws Exception {
		try {
			return partnerServicesFac.getBriefanredeFuerBeleg(
					ansprechpartnerIId, partnerIId, LPMain.getInstance()
							.getTheClient().getLocUi(), LPMain.getInstance()
							.getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	// **************************************************************************
	// ****
	public Integer createSelektion(SelektionDto selektionDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			selektionDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			iId = partnerServicesFac.createSelektion(selektionDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createKontaktart(KontaktartDto kontaktartDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createKontaktart(kontaktartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeSelektion(Integer iId) throws ExceptionLP {
		try {
			partnerServicesFac.removeSelektion(iId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKontaktart(KontaktartDto kontaktartDto)
			throws ExceptionLP {
		try {
			partnerServicesFac.removeKontaktart(kontaktartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSelektion(SelektionDto selektionDtoI) throws ExceptionLP {
		try {
			partnerServicesFac.removeSelektion(selektionDtoI.getIId(), LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKontaktart(KontaktartDto kontaktartDto)
			throws ExceptionLP {
		try {
			partnerServicesFac.updateKontaktart(kontaktartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSelektion(SelektionDto selektionDto) throws ExceptionLP {
		try {
			partnerServicesFac.updateSelektion(selektionDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSerienbriefMailtext(SerienbriefDto serienbriefDto)
			throws ExceptionLP {
		try {
			partnerServicesFac.updateSerienbriefMailtext(serienbriefDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public SelektionDto selektionFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		SelektionDto selektionDto = null;
		try {
			selektionDto = partnerServicesFac.selektionFindByPrimaryKey(iId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return selektionDto;
	}

	public KontaktartDto kontaktartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		KontaktartDto kontaktartDto = null;
		try {
			kontaktartDto = partnerServicesFac.kontaktartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return kontaktartDto;
	}

	// **************************************************************************
	// ****
	public void createSelektionspr(SelektionsprDto selektionsprDto)
			throws ExceptionLP {
		try {
			partnerServicesFac.createSelektionspr(selektionsprDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSelektionspr(Integer selektionIId, String localeCNr)
			throws ExceptionLP {
		try {
			partnerServicesFac.removeSelektionspr(selektionIId, localeCNr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSelektionspr(SelektionsprDto selektionsprDto)
			throws ExceptionLP {
		try {
			partnerServicesFac.updateSelektionspr(selektionsprDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SelektionsprDto selektionsprFindByPrimaryKey(Integer selektionIId,
			String localeCNr) throws ExceptionLP {
		SelektionsprDto selektionsprDto = null;
		try {
			selektionsprDto = partnerServicesFac.selektionsprFindByPrimaryKey(
					selektionIId, localeCNr, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return selektionsprDto;
	}

	// **************************************************************************
	// ****
	public Integer createSerienbrief(SerienbriefDto serienbriefDto)
			throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createSerienbrief(serienbriefDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createPartnerkommentarart(PartnerkommentarartDto artDto)
			throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createPartnerkommentarart(artDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createPartnerkommentar(PartnerkommentarDto dto)
			throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createPartnerkommentar(dto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeSerienbrief(Integer iId) throws Exception {
		try {
			partnerServicesFac.removeSerienbrief(iId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePartnerkommentarart(PartnerkommentarartDto artDto)
			throws Exception {
		try {
			partnerServicesFac.removePartnerkommentarart(artDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePartnerkommentar(PartnerkommentarDto dto)
			throws Exception {
		try {
			partnerServicesFac.removePartnerkommentar(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSerienbrief(SerienbriefDto serienbriefDto)
			throws Exception {
		try {
			partnerServicesFac.updateSerienbrief(serienbriefDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePartnerkommentarart(PartnerkommentarartDto artDto)
			throws Exception {
		try {
			partnerServicesFac.updatePartnerkommentarart(artDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePartnerkommentar(PartnerkommentarDto dto)
			throws Exception {
		try {
			partnerServicesFac.updatePartnerkommentar(dto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SerienbriefDto serienbriefFindByPrimaryKey(Integer iId)
			throws Exception {
		SerienbriefDto serienbriefDto = null;
		try {
			serienbriefDto = partnerServicesFac.serienbriefFindByPrimaryKey(
					iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return serienbriefDto;
	}

	public PartnerkommentarartDto partnerkommentarartFindByPrimaryKey(
			Integer iId) throws Exception {
		PartnerkommentarartDto partnerkommentarartDto = null;
		try {
			partnerkommentarartDto = partnerServicesFac
					.partnerkommentarartFindByPrimaryKey(iId, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return partnerkommentarartDto;
	}

	public PartnerkommentarDto partnerkommentarFindByPrimaryKey(Integer iId)
			throws Exception {
		PartnerkommentarDto partnerkommentarDto = null;
		try {
			partnerkommentarDto = partnerServicesFac
					.partnerkommentarFindByPrimaryKey(iId, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return partnerkommentarDto;
	}

	// **************************************************************************
	// ****
	public SerienbriefselektionPK createSerienbriefselektion(
			SerienbriefselektionDto serienbriefselektionDto) throws Exception {

		SerienbriefselektionPK serienbriefselektionPK = null;
		try {
			serienbriefselektionPK = partnerServicesFac
					.createSerienbriefselektion(serienbriefselektionDto, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return serienbriefselektionPK;
	}

	public void removeSerienbriefselektion(Integer serienbriefIId,
			Integer selektionIId) throws Exception {
		try {
			partnerServicesFac.removeSerienbriefselektion(serienbriefIId,
					selektionIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSerienbriefselektion(
			SerienbriefselektionDto serienbriefselektionDto) throws Exception {
		try {
			partnerServicesFac.updateSerienbriefselektion(
					serienbriefselektionDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SerienbriefselektionDto serienbriefselektionFindByPrimaryKey(
			Integer serienbriefIId, Integer selektionIId) throws Exception {
		SerienbriefselektionDto serienbriefselektionDto = null;
		try {
			serienbriefselektionDto = partnerServicesFac
					.serienbriefselektionFindByPrimaryKey(serienbriefIId,
							selektionIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return serienbriefselektionDto;
	}

	public SerienbriefselektionnegativPK createSerienbriefselektionnegativ(
			SerienbriefselektionnegativDto serienbriefselektionnegativDto)
			throws Exception {

		SerienbriefselektionnegativPK serienbriefselektionnegativPK = null;
		try {
			serienbriefselektionnegativPK = partnerServicesFac
					.createSerienbriefselektionnegativ(
							serienbriefselektionnegativDto, LPMain
									.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return serienbriefselektionnegativPK;
	}

	public void removeSerienbriefselektionnegativ(Integer serienbriefIId,
			Integer selektionIId) throws Exception {
		try {
			partnerServicesFac.removeSerienbriefselektionnegativ(
					serienbriefIId, selektionIId, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSerienbriefselektionnegativ(
			SerienbriefselektionnegativDto serienbriefselektionnegativDto)
			throws Exception {
		try {
			partnerServicesFac.updateSerienbriefselektionnegativ(
					serienbriefselektionnegativDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SerienbriefselektionnegativDto serienbriefselektionnegativFindByPrimaryKey(
			Integer serienbriefIId, Integer selektionIId) throws Exception {
		SerienbriefselektionnegativDto serienbriefselektionnegativDto = null;
		try {
			serienbriefselektionnegativDto = partnerServicesFac
					.serienbriefselektionnegativFindByPrimaryKey(
							serienbriefIId, selektionIId, LPMain.getInstance()
									.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return serienbriefselektionnegativDto;
	}

	public String[] getPartnerhinweise(Integer partnerIId, boolean bKunde,
			String belegartCNr) throws Exception {
		String[] s = null;
		try {
			s = partnerServicesFac.getPartnerhinweise(partnerIId, bKunde,
					belegartCNr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return s;
	}

	public ArrayList<byte[]> getPartnerkommentarBilderUndPDFAlsBilderUmgewandelt(
			Integer partnerIId, boolean bKunde, String belegartCNr, Integer iArt)
			throws Exception {
		ArrayList<byte[]> s = null;
		try {
			s = partnerServicesFac
					.getPartnerkommentarBilderUndPDFAlsBilderUmgewandelt(
							partnerIId, bKunde, belegartCNr, iArt, LPMain
									.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return s;
	}

}
