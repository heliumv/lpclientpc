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

import java.sql.Date;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokoFac;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Delegate fuer Kundensonderkonditionen.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Uli Walch, 16.06.2006
 * </p>
 * 
 * <p>
 * @author Uli Walch
 * </p>
 * 
 * @version 1.0
 * 
 * @version $Revision: 1.7 $ Date $Date: 2012/03/05 14:12:21 $
 */
public class KundensokoDelegate extends Delegate {
	private Context context;
	private KundesokoFac kundesokoFac;

	public KundensokoDelegate() throws ExceptionLP {

		try {
			context = new InitialContext();
			kundesokoFac = (KundesokoFac) context
					.lookup("lpserver/KundesokoFacBean/remote");
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}
	}

	/**
	 * Einen neue Kundensonderkondition anlegen.
	 * 
	 * @param kundesokoDtoI
	 *            die neue SOKO
	 * @param defaultMengenstaffelDtoI
	 * @return Integer PK der neuen SOKO
	 * @throws EJBException
	 *             Ausnahme
	 */
	public Integer createKundesoko(KundesokoDto kundesokoDtoI,
			KundesokomengenstaffelDto defaultMengenstaffelDtoI)
			throws ExceptionLP {
		Integer iId = null;

		try {
			iId = kundesokoFac.createKundesoko(kundesokoDtoI,
					defaultMengenstaffelDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	/**
	 * Einen Kundensonderkondition loeschen.
	 * 
	 * @param kundesokoDtoI
	 *            die Kundensoko
	 * @throws EJBException
	 *             Ausnahme
	 */
	public void removeKundesoko(KundesokoDto kundesokoDtoI) throws ExceptionLP {
		try {
			kundesokoFac.removeKundesoko(kundesokoDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Eine bestehende Kundensonderkondition aktualisieren.
	 * 
	 * @param kundesokoDtoI
	 *            die zu aktualisierende Kundensoko
	 * @param defaultMengenstaffelDtoI
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateKundesoko(KundesokoDto kundesokoDtoI,
			KundesokomengenstaffelDto defaultMengenstaffelDtoI)
			throws ExceptionLP {
		try {
			kundesokoFac.updateKundesoko(kundesokoDtoI,
					defaultMengenstaffelDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KundesokoDto kundesokoFindByPrimaryKey(Integer iIdI)
			throws ExceptionLP {
		KundesokoDto kundesokoDto = null;

		try {
			kundesokoDto = kundesokoFac.kundesokoFindByPrimaryKey(iIdI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return kundesokoDto;
	}

	public KundesokoDto[] kundesokoFindByKundeIId(Integer iIdKundeI)
			throws ExceptionLP {
		KundesokoDto[] aKundesokoDtos = null;

		try {
			aKundesokoDtos = kundesokoFac.kundesokoFindByKundeIId(iIdKundeI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return aKundesokoDtos;
	}

	public KundesokoDto[] kundesokoFindByKundeIIdOhneExc(Integer iIdKundeI)
			throws ExceptionLP {
		KundesokoDto[] aKundesokoDtos = null;

		try {
			aKundesokoDtos = kundesokoFac
					.kundesokoFindByKundeIIdOhneExc(iIdKundeI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return aKundesokoDtos;
	}

	/**
	 * Einen neue Mengenstaffel zu einer Kundensonderkondition anlegen.
	 * 
	 * @param kundesokomengenstaffelDtoI
	 *            die neue Mengenstaffel
	 * @return Integer PK der neuen Mengenstaffel
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer createKundesokomengenstaffel(
			KundesokomengenstaffelDto kundesokomengenstaffelDtoI)
			throws ExceptionLP {
		Integer iId = null;

		try {
			iId = kundesokoFac.createKundesokomengenstaffel(
					kundesokomengenstaffelDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	/**
	 * Eine Mengenstaffel zu einer Kundensonderkondition loeschen.
	 * 
	 * @param kundesokomengenstaffelDtoI
	 *            die zu loeschende Mengenstaffel
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void removeKundesokomengenstaffel(
			KundesokomengenstaffelDto kundesokomengenstaffelDtoI)
			throws ExceptionLP {
		try {
			kundesokoFac.removeKundesokomengenstaffel(
					kundesokomengenstaffelDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Eine bestehende SOKO Mengenstaffel aktualisieren.
	 * 
	 * @param kundesokomengenstaffelDtoI
	 *            die Mengenstaffel
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateKundesokomengenstaffel(
			KundesokomengenstaffelDto kundesokomengenstaffelDtoI)
			throws ExceptionLP {
		try {
			kundesokoFac.updateKundesokomengenstaffel(
					kundesokomengenstaffelDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KundesokomengenstaffelDto kundesokomengenstaffelFindByPrimaryKey(
			Integer iIdI) throws ExceptionLP {
		KundesokomengenstaffelDto kundesokomengenstaffelDto = null;

		try {
			kundesokomengenstaffelDto = kundesokoFac
					.kundesokomengenstaffelFindByPrimaryKey(iIdI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return kundesokomengenstaffelDto;
	}

	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIId(
			Integer iIdKundesokoIId) throws ExceptionLP {
		KundesokomengenstaffelDto[] aKundesokomengenstaffelDtos = null;

		try {
			aKundesokomengenstaffelDtos = kundesokoFac
					.kundesokomengenstaffelFindByKundesokoIId(iIdKundesokoIId,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return aKundesokomengenstaffelDtos;
	}

	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
			Integer iIdKundesokoIId, Date tGueltigkeitsdatumI, String waehrungCNrZielwaehrung)
			throws ExceptionLP {
		KundesokomengenstaffelDto[] aKundesokomengenstaffelDtos = null;

		try {
			aKundesokomengenstaffelDtos = kundesokoFac
					.kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
							iIdKundesokoIId, tGueltigkeitsdatumI,
							waehrungCNrZielwaehrung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return aKundesokomengenstaffelDtos;
	}

	public KundesokoDto kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
			Integer kundeIIdI, Integer artikelIIdI, Date tGueltigkeitsdatumI)
			throws ExceptionLP {
		KundesokoDto kundesokoDto = null;

		try {
			kundesokoDto = kundesokoFac
					.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
							kundeIIdI, artikelIIdI, tGueltigkeitsdatumI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return kundesokoDto;
	}

	public KundesokoDto kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(
			Integer kundeIIdI, Integer artgruIIdI, Date tGueltigkeitsdatumI)
			throws ExceptionLP {
		KundesokoDto kundesokoDto = null;

		try {
			kundesokoDto = kundesokoFac
					.kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(
							kundeIIdI, artgruIIdI, tGueltigkeitsdatumI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return kundesokoDto;
	}
}
