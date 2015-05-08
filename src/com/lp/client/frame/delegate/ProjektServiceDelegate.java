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

import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VorschlagstextDto;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.HistoryartDto;
import com.lp.server.projekt.service.KategorieDto;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.projekt.service.ProjektStatusDto;
import com.lp.server.projekt.service.ProjekterledigungsgrundDto;
import com.lp.server.projekt.service.ProjekttypDto;
import com.lp.server.system.service.TheClientDto;

@SuppressWarnings("static-access")
public class ProjektServiceDelegate extends Delegate {

	private Context context;
	private ProjektServiceFac projektServiceFac;

	public ProjektServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			projektServiceFac = (ProjektServiceFac) context
					.lookup("lpserver/ProjektServiceFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public String createKategorie(KategorieDto kategorieDto) throws ExceptionLP {

		String s = null;
		try {
			kategorieDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			return projektServiceFac.createKategorie(kategorieDto,
					LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return s;
	}

	public Integer createBereich(BereichDto bereichDto) throws ExceptionLP {

		Integer s = null;
		try {

			return projektServiceFac.createBereich(bereichDto,
					LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return s;
	}

	public boolean sindErledigugnsgruendeVorhanden() throws ExceptionLP {
		try {

			return projektServiceFac.sindErledigugnsgruendeVorhanden(LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return false;
	}

	public void removeKategorie(String cNr) throws ExceptionLP {
		try {
			projektServiceFac.removeKategorie(cNr);
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public Map<?, ?> getAllBereich() throws ExceptionLP {

		Map<?, ?> arten = null;

		try {
			arten = projektServiceFac.getAllBereich(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arten;
	}

	public Map<?, ?> getKategorie() throws ExceptionLP {

		Map<?, ?> arten = null;

		try {
			arten = projektServiceFac.getKategorie(LPMain.getInstance()
					.getUISprLocale(), LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arten;
	}

	/**
	 * Alle Auftragarten in bestmoeglicher Uebersetzung von der DB holen.
	 * 
	 * @return Map die Auftragarten mit ihrer Uebersetzung
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getTyp() throws ExceptionLP {

		Map<?, ?> typen = null;

		try {
			typen = projektServiceFac.getTyp(LPMain.getInstance()
					.getUISprLocale(), LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return typen;
	}

	/**
	 * Alle Auftragarten in bestmoeglicher Uebersetzung von der DB holen.
	 * 
	 * @param pLocKunde
	 *            das Locale des Kunden
	 * @return Map die Auftragarten mit ihrer Uebersetzung
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getProjektStatus(Locale pLocKunde) throws ExceptionLP {

		Map<?, ?> arten = null;

		try {
			arten = projektServiceFac.getProjektStatus(LPMain.getInstance()
					.getUISprLocale(), LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arten;
	}

	public void removeKategorie(KategorieDto kategorieDto) throws ExceptionLP {
		try {
			projektServiceFac.removeKategorie(kategorieDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public void removeBereich(BereichDto bereichDto) throws ExceptionLP {
		try {
			projektServiceFac.removeBereich(bereichDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public void updateKategorie(KategorieDto kategorieDto) throws ExceptionLP {
		try {
			projektServiceFac.updateKategorie(kategorieDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public void updateProjektStatus(ProjektStatusDto projektStatus)
			throws ExceptionLP {
		try {
			projektServiceFac.updateProjektStatus(projektStatus,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public KategorieDto kategorieFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		KategorieDto kategorieDto = null;

		try {
			String mandantCNr = LPMain.getInstance().getTheClient()
					.getMandant();
			return projektServiceFac.kategorieFindByPrimaryKey(cNr, mandantCNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return kategorieDto;
	}

	public String createProjekttyp(ProjekttypDto projekttypDtoI)
			throws ExceptionLP {
		String s = null;
		try {
			projekttypDtoI.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			return projektServiceFac.createProjekttyp(projekttypDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return s;
	}

	public Integer createHistoryart(HistoryartDto historyartDto)
			throws ExceptionLP {
		try {
			return projektServiceFac.createHistoryart(historyartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateHistoryart(HistoryartDto vorschlagstextDto)
			throws ExceptionLP {
		try {
			projektServiceFac.updateHistoryart(vorschlagstextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBereich(BereichDto bereichDto) throws ExceptionLP {
		try {
			projektServiceFac.updateBereich(bereichDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public HistoryartDto historyartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return projektServiceFac.historyartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BereichDto bereichFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return projektServiceFac.bereichFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeHistoryart(HistoryartDto dto) throws ExceptionLP {
		try {
			projektServiceFac.removeHistoryart(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createProjekterledigungsgrund(
			ProjekterledigungsgrundDto projekterledigungsgrundDto)
			throws ExceptionLP {
		try {
			return projektServiceFac.createProjekterledigungsgrund(
					projekterledigungsgrundDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateProjekterledigungsgrund(
			ProjekterledigungsgrundDto projekterledigungsgrundDto)
			throws ExceptionLP {
		try {
			projektServiceFac
					.updateProjekterledigungsgrund(projekterledigungsgrundDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ProjekterledigungsgrundDto projekterledigungsgrundFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return projektServiceFac
					.projekterledigungsgrundFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeProjekterledigungsgrund(Integer projekterledigungsgrundIId)
			throws ExceptionLP {
		try {
			projektServiceFac
					.removeProjekterledigungsgrund(projekterledigungsgrundIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String createProjektStatus(ProjektStatusDto projektStatusDto)
			throws ExceptionLP {
		String s = null;
		try {
			projektStatusDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			return projektServiceFac.createProjektStatus(projektStatusDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return s;
	}

	public ProjekttypDto projekttypFindByPrimaryKey(String cNrI)
			throws ExceptionLP {

		ProjekttypDto projekttypDto = null;
		try {
			String mandantCNr = LPMain.getInstance().getTheClient()
					.getMandant();
			projekttypDto = projektServiceFac.projekttypFindByPrimaryKey(cNrI,
					mandantCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return projekttypDto;
	}

	public ProjektStatusDto projektstatusFindByPrimaryKey(String cNrI)
			throws ExceptionLP {

		ProjektStatusDto projektstatusDto = null;
		try {
			projektstatusDto = projektServiceFac.projektStatusFindByPrimaryKey(
					cNrI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return projektstatusDto;
	}

	public void removeProjekttyp(String cNr) throws ExceptionLP {

		try {
			projektServiceFac.removeProjekttyp(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheBereich(Integer iId1I, Integer iId2I)
			throws ExceptionLP {

		try {
			projektServiceFac.vertauscheBereich(iId1I, iId2I);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeProjekttyp(ProjekttypDto projekttypDto)
			throws ExceptionLP {
		try {
			projektServiceFac.removeProjekttyp(projekttypDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeProjektStatus(ProjektStatusDto projektStatusDto)
			throws ExceptionLP {
		try {
			projektServiceFac.removeProjektStatus(projektStatusDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateProjekttyp(ProjekttypDto projekttypDto)
			throws ExceptionLP {
		try {
			projektServiceFac.updateProjekttyp(projekttypDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

}
