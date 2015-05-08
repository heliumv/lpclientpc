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
import com.lp.server.lieferschein.service.BegruendungDto;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinartDto;
import com.lp.server.lieferschein.service.LieferscheinpositionartDto;
import com.lp.server.lieferschein.service.LieferscheintextDto;
import com.lp.server.lieferschein.service.VerkettetDto;

@SuppressWarnings("static-access")
/**
 * <p>Delegate fuer Lieferschein Services..</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Uli Walch, 21. 04. 2005</p>
 *
 * <p>@author Uli Walch</p>
 *
 * @version 1.0
 *
 * @version $Revision: 1.8 $ Date $Date: 2010/09/21 09:30:03 $
 */
public class LieferscheinServiceDelegate extends Delegate {
	private Context context = null;
	private LieferscheinServiceFac lsServiceFac = null;

	public LieferscheinServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			lsServiceFac = (LieferscheinServiceFac) context
					.lookup("lpserver/LieferscheinServiceFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public Integer createLieferscheintext(
			LieferscheintextDto lieferscheintextDto) throws ExceptionLP {
		Integer iId = null;

		try {
			// fuer StammdatenCRUD : alle Felder, die in der UI nicht vorhanden
			// sind selbst befuellen
			lieferscheintextDto.setMandantCNr(LPMain.getInstance()
					.getTheClient().getMandant());
			lieferscheintextDto.setLocaleCNr(LPMain.getInstance()
					.getTheClient().getLocUiAsString());

			iId = lsServiceFac.createLieferscheintext(lieferscheintextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public void removeLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws ExceptionLP {
		try {
			lsServiceFac.removeLieferscheintext(lieferscheintextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws ExceptionLP {
		try {
			lsServiceFac.updateLieferscheintext(lieferscheintextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBegruendung(BegruendungDto begruendungDto)
			throws ExceptionLP {
		try {
			lsServiceFac.updateBegruendung(begruendungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LieferscheintextDto lieferscheintextFindByPrimaryKey(
			Integer iIdLieferscheintextI) throws ExceptionLP {
		LieferscheintextDto oLieferscheintextDto = null;

		try {
			oLieferscheintextDto = lsServiceFac
					.lieferscheintextFindByPrimaryKey(iIdLieferscheintextI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oLieferscheintextDto;
	}

	public BegruendungDto begruendungFindByPrimaryKey(Integer begruendungIId)
			throws ExceptionLP {
		BegruendungDto oLieferscheintextDto = null;

		try {
			oLieferscheintextDto = lsServiceFac
					.begruendungFindByPrimaryKey(begruendungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oLieferscheintextDto;
	}

	public VerkettetDto verkettetFindByPrimaryKey(Integer begruendungIId)
			throws ExceptionLP {
		VerkettetDto dto = null;

		try {
			dto = lsServiceFac.verkettetFindByPrimaryKey(begruendungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return dto;
	}

	public LieferscheintextDto lieferscheintextFindByMandantLocaleCNr(
			String sLocaleI, String sCNrI) throws ExceptionLP {
		LieferscheintextDto oLieferscheintextDto = null;

		try {
			oLieferscheintextDto = lsServiceFac
					.lieferscheintextFindByMandantLocaleCNr(sLocaleI, sCNrI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oLieferscheintextDto;
	}

	public LieferscheintextDto createDefaultLieferscheintext(String sMediaartI,
			String localeCNrI) throws ExceptionLP {
		LieferscheintextDto oLieferscheintextDto = null;

		try {
			oLieferscheintextDto = lsServiceFac.createDefaultLieferscheintext(
					sMediaartI, localeCNrI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oLieferscheintextDto;
	}

	public Integer createBegruendung(BegruendungDto begruendungDto)
			throws ExceptionLP {
		try {
			return lsServiceFac.createBegruendung(begruendungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createVerkettet(VerkettetDto dto) throws ExceptionLP {
		try {
			return lsServiceFac.createVerkettet(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public String createLieferscheinart(LieferscheinartDto lieferscheinartDtoI)
			throws ExceptionLP {
		String cNr = null;

		try {
			cNr = lsServiceFac.createLieferscheinart(lieferscheinartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return cNr;
	}

	public void removeLieferscheinart(LieferscheinartDto lieferscheinartDtoI,
			String cNrUserI) throws ExceptionLP {
		try {
			lsServiceFac.removeLieferscheinart(lieferscheinartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeBegruendung(Integer iId) throws ExceptionLP {
		try {
			lsServiceFac.removeBegruendung(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeVerkettet(Integer iId) throws ExceptionLP {
		try {
			lsServiceFac.removeVerkettet(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLieferscheinart(LieferscheinartDto lieferscheinartDtoI)
			throws ExceptionLP {
		try {
			lsServiceFac.updateLieferscheinart(lieferscheinartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateVerkettet(VerkettetDto dto) throws ExceptionLP {
		try {
			lsServiceFac.updateVerkettet(dto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LieferscheinartDto lieferscheinartFindByPrimaryKey(String cNrI)
			throws ExceptionLP {
		LieferscheinartDto lieferscheinartDto = null;

		try {
			lieferscheinartDto = lsServiceFac.lieferscheinartFindByPrimaryKey(
					cNrI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return lieferscheinartDto;
	}

	public Map<String, String> getLieferscheinpositionart(Locale locKundeI)
			throws ExceptionLP {
		Map<String, String> map = null;

		try {
			map = lsServiceFac.lieferscheinpositionartFindAll(locKundeI, LPMain
					.getInstance().getUISprLocale(), LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return map;
	}

	public String createLieferscheinpositionart(
			LieferscheinpositionartDto lieferscheinpositionartDtoI)
			throws ExceptionLP {
		String lieferscheinpositionartCNr = null;

		try {
			lieferscheinpositionartCNr = lsServiceFac
					.createLieferscheinpositionart(lieferscheinpositionartDtoI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return lieferscheinpositionartCNr;
	}

	public void updateLieferscheinpositionart(
			LieferscheinpositionartDto lieferscheinpositionartDtoI)
			throws ExceptionLP {
		try {
			lsServiceFac.updateLieferscheinpositionart(
					lieferscheinpositionartDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LieferscheinpositionartDto lieferscheinpositionartFindByPrimaryKey(
			String cNrLieferscheinpositionartI) throws ExceptionLP {
		LieferscheinpositionartDto lieferscheinpositionartDto = null;

		try {
			lieferscheinpositionartDto = lsServiceFac
					.lieferscheinpositionartFindByPrimaryKey(
							cNrLieferscheinpositionartI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return lieferscheinpositionartDto;
	}

	public VerkettetDto verkettetfindByLieferscheinIIdVerkettetOhneExc(
			Integer lieferscheinIIdVerkettet) throws ExceptionLP {
		VerkettetDto verkettetDto = null;

		try {
			verkettetDto = lsServiceFac
					.verkettetfindByLieferscheinIIdVerkettetOhneExc(lieferscheinIIdVerkettet);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return verkettetDto;
	}

}
