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

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.ejb.Gutschriftsgrund;
import com.lp.server.rechnung.service.GutschriftpositionsartDto;
import com.lp.server.rechnung.service.GutschriftsgrundDto;
import com.lp.server.rechnung.service.GutschrifttextDto;
import com.lp.server.rechnung.service.ProformarechnungpositionsartDto;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungpositionsartDto;
import com.lp.server.rechnung.service.RechnungstatusDto;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.rechnung.service.RechnungtypDto;
import com.lp.server.rechnung.service.ZahlungsartDto;

@SuppressWarnings("static-access")
public class RechnungServiceDelegate extends Delegate {
	private Context context;
	private RechnungServiceFac rechnungServiceFac;

	public RechnungServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			rechnungServiceFac = (RechnungServiceFac) context
					.lookup("lpserver/RechnungServiceFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Integer createRechnungtext(RechnungtextDto rechnungtextDto)
			throws ExceptionLP {
		try {
			rechnungtextDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			rechnungtextDto.setLocaleCNr(LPMain.getInstance().getTheClient()
					.getLocUiAsString());
			return rechnungServiceFac.createRechnungtext(rechnungtextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createGutschriftsgrund(
			GutschriftsgrundDto gutschriftsgrundDto) throws ExceptionLP {
		try {
			return rechnungServiceFac.createGutschriftsgrund(
					gutschriftsgrundDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeRechnungtext(RechnungtextDto rechnungtextDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.removeRechnungtext(rechnungtextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeGutschrifttext(GutschrifttextDto gutschrifttextDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.removeGutschrifttext(gutschrifttextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeGutschriftsgrund(GutschriftsgrundDto gutschriftsgrundDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.removeGutschriftsgrund(gutschriftsgrundDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateRechnungtext(RechnungtextDto rechnungtextDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.updateRechnungtext(rechnungtextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateGutschrifttext(GutschrifttextDto gutschrifttextDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.updateGutschrifttext(gutschrifttextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateRechnungart(RechnungartDto rechnungartDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.updateRechnungart(rechnungartDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZahlungsart(ZahlungsartDto zahlungsartDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.updateZahlungsart(zahlungsartDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public RechnungtextDto rechnungtextFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return rechnungServiceFac.rechnungtextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public GutschrifttextDto gutschrifttextFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return rechnungServiceFac.gutschrifttextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungtextDto rechnungtextFindByMandantLocaleCNr(String pSprache,
			String pText) throws ExceptionLP {
		try {
			return rechnungServiceFac
					.rechnungtextFindByMandantLocaleCNr(LPMain.getInstance()
							.getTheClient().getMandant(), pSprache, pText);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public GutschrifttextDto gutschrifttextFindByMandantLocaleCNr(
			String pSprache, String pText) throws ExceptionLP {
		try {
			return rechnungServiceFac.gutschrifttextFindByMandantLocaleCNr(
					LPMain.getInstance().getTheClient().getMandant(), pSprache,
					pText);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public GutschriftsgrundDto gutschriftsgrundFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return rechnungServiceFac.gutschriftsgrundFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungtextDto createDefaultRechnungtext(String sMediaartI,
			String sTextinhaltI, String localeCNr) throws ExceptionLP {
		RechnungtextDto oRechnungtextDto = null;

		try {
			oRechnungtextDto = rechnungServiceFac.createDefaultRechnungtext(
					sMediaartI, sTextinhaltI, localeCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oRechnungtextDto;
	}

	public GutschrifttextDto createDefaultGutschrifttext(String sMediaartI,
			String sTextinhaltI, String localeCNr) throws ExceptionLP {
		GutschrifttextDto gutschrifttextDto = null;

		try {
			gutschrifttextDto = rechnungServiceFac.createDefaultGutschrifttext(
					sMediaartI, sTextinhaltI, localeCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return gutschrifttextDto;
	}

	public RechnungpositionsartDto rechnungpositionsartFindByPrimaryKey(
			String positionsartCNr) throws ExceptionLP {
		try {
			return rechnungServiceFac
					.rechnungpositionsartFindByPrimaryKey(positionsartCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public GutschriftpositionsartDto gutschriftpositionsartFindByPrimaryKey(
			String positionsartCNr) throws ExceptionLP {
		try {
			return rechnungServiceFac
					.gutschriftpositionsartFindByPrimaryKey(positionsartCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ProformarechnungpositionsartDto proformarechnungpositionsartFindByPrimaryKey(
			String positionsartCNr) throws ExceptionLP {
		try {
			return rechnungServiceFac
					.proformarechnungpositionsartFindByPrimaryKey(positionsartCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateRechnungpositionsart(
			RechnungpositionsartDto rechnungpositionsartDto) throws ExceptionLP {
		try {
			rechnungServiceFac.updateRechnungpositionsart(
					rechnungpositionsartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateGutschriftpositionsart(
			GutschriftpositionsartDto gutschriftpositionsartDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.updateGutschriftpositionsart(
					gutschriftpositionsartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateGutschriftsgrund(GutschriftsgrundDto gutschriftsgrundDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.updateGutschriftsgrund(gutschriftsgrundDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateProformarechnungpositionsart(
			ProformarechnungpositionsartDto proformarechnungpositionsartDto)
			throws ExceptionLP {
		try {
			rechnungServiceFac.updateProformarechnungpositionsart(
					proformarechnungpositionsartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Alle verfuegbaren RechnungsPositionsarten aus der DB holen.
	 * 
	 * @return Map
	 * @throws ExceptionLP
	 */
	public Map<String, String> getAllRechnungpositionsart() throws ExceptionLP {
		myLogger.entry();
		Map<String, String> arten = null;
		try {
			Locale locale1 = LPMain.getInstance().getTheClient().getLocUi();
			Locale locale2 = LPMain.getInstance().getTheClient()
					.getLocMandant();
			arten = rechnungServiceFac.getAllRechnungpositionsart(locale1,
					locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		myLogger.exit();
		return arten;
	}

	/**
	 * Alle verfuegbaren Gutschriftpositionarten aus der DB holen.
	 * 
	 * @return Map
	 * @throws ExceptionLP
	 */
	public Map<String, String> getAllGutschriftpositionsart()
			throws ExceptionLP {
		myLogger.entry();
		Map<String, String> arten = null;
		try {
			Locale locale1 = LPMain.getInstance().getTheClient().getLocUi();
			Locale locale2 = LPMain.getInstance().getTheClient()
					.getLocMandant();
			arten = rechnungServiceFac.getAllGutschriftpositionsart(locale1,
					locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return arten;
	}

	/**
	 * Alle verfuegbaren RechnungsPositionsarten aus der DB holen.
	 * 
	 * @return Map
	 * @throws ExceptionLP
	 */
	public Map<String, String> getAllProformarechnungpositionsart()
			throws ExceptionLP {
		myLogger.entry();
		Map<String, String> arten = null;
		try {
			Locale locale1 = LPMain.getInstance().getTheClient().getLocUi();
			Locale locale2 = LPMain.getInstance().getTheClient()
					.getLocMandant();
			arten = rechnungServiceFac.getAllProformarechnungpositionsart(
					locale1, locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		myLogger.exit();
		return arten;
	}

	public RechnungtypDto rechnungtypFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		try {
			return rechnungServiceFac.rechnungtypFindByPrimaryKey(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungtypDto[] rechnungtypFindAll() throws ExceptionLP {
		try {
			return rechnungServiceFac.rechnungtypFindAll();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Alle verfuegbaren Rechnungsarten aus der DB holen.
	 * 
	 * @return Map
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getAllRechnungArtenRechnung() throws ExceptionLP {
		myLogger.entry();
		Map<?, ?> arten = null;
		try {
			Locale locale1 = LPMain.getInstance().getTheClient().getLocUi();
			Locale locale2 = LPMain.getInstance().getTheClient()
					.getLocMandant();
			arten = rechnungServiceFac.getAllRechnungartRechnung(locale1,
					locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return arten;
	}

	/**
	 * Alle verfuegbaren Gutschriftarten aus der DB holen.
	 * 
	 * @return Map
	 * @throws ExceptionLP
	 */
	public Map<String, String> getAllRechnungArtenGutschrift()
			throws ExceptionLP {
		myLogger.entry();
		Map<String, String> arten = null;
		try {
			Locale locale1 = LPMain.getInstance().getTheClient().getLocUi();
			Locale locale2 = LPMain.getInstance().getTheClient()
					.getLocMandant();
			arten = rechnungServiceFac.getAllRechnungartGutschrift(locale1,
					locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return arten;
	}

	/**
	 * Alle verfuegbaren Rechnungsarten aus der DB holen.
	 * 
	 * @return Map
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getAllRechnungArtenProformarechnung() throws ExceptionLP {
		myLogger.entry();
		Map<?, ?> arten = null;
		try {
			Locale locale1 = LPMain.getInstance().getTheClient().getLocUi();
			Locale locale2 = LPMain.getInstance().getTheClient()
					.getLocMandant();
			arten = rechnungServiceFac.getAllRechnungartProformarechnung(
					locale1, locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return arten;
	}

	/**
	 * Alle verfuegbaren Zahlungsarten aus der DB holen.
	 * 
	 * @return Map
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getAllZahlungsarten() throws ExceptionLP {
		myLogger.entry();
		Map<?, ?> arten = null;
		try {
			Locale locale1 = LPMain.getInstance().getTheClient().getLocUi();
			Locale locale2 = LPMain.getInstance().getTheClient()
					.getLocMandant();
			arten = rechnungServiceFac.getAllZahlungsarten(locale1, locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return arten;
	}

	// ****************************************************************************
	public RechnungartDto rechnungartFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		try {
			return rechnungServiceFac.rechnungartFindByPrimaryKey(cNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	// ****************************************************************************
	public RechnungstatusDto rechnungstatusFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		try {
			return rechnungServiceFac.rechnungstatusFindByPrimaryKey(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	// ****************************************************************************
	public ZahlungsartDto zahlungsartFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		try {
			return rechnungServiceFac.zahlungsartFindByPrimaryKey(cNr,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

}
