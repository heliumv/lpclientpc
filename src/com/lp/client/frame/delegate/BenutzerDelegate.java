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

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.benutzer.service.FertigungsgrupperolleDto;
import com.lp.server.benutzer.service.LagerrolleDto;
import com.lp.server.benutzer.service.NachrichtarchivDto;
import com.lp.server.benutzer.service.NachrichtartDto;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.benutzer.service.ThemaDto;
import com.lp.server.benutzer.service.ThemarolleDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class BenutzerDelegate extends Delegate {
	private Context context;
	private BenutzerFac benutzerFac;

	public BenutzerDelegate() throws Exception {
		context = new InitialContext();
		benutzerFac = (BenutzerFac) context
				.lookup("lpserver/BenutzerFacBean/remote");
	}

	public Integer createBenutzer(BenutzerDto benutzerDto) throws ExceptionLP {

		try {
			return benutzerFac.createBenutzer(benutzerDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createNachrichtart(NachrichtartDto nachrichtartDto)
			throws ExceptionLP {

		try {
			return benutzerFac.createNachrichtart(nachrichtartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printBenutzerstatistik(java.sql.Date dVon,
			java.sql.Date dBis) throws Throwable {
		try {
			return benutzerFac.printBenutzerstatistik(dVon, dBis,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createThemarolle(ThemarolleDto themarolleDto)
			throws ExceptionLP {

		try {
			return benutzerFac.createThemarolle(themarolleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createFertigungsgrupperolle(
			FertigungsgrupperolleDto fertigungsgrupperolleDto)
			throws ExceptionLP {

		try {
			return benutzerFac
					.createFertigungsgrupperolle(fertigungsgrupperolleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createLagerrolle(LagerrolleDto lagerrolleDto)
			throws ExceptionLP {

		try {
			return benutzerFac.createLagerrolle(lagerrolleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createBenutzermandantsystemrolle(
			BenutzermandantsystemrolleDto benutzermandantsystemrolleDto)
			throws ExceptionLP {

		try {
			return benutzerFac.createBenutzermandantsystemrolle(
					benutzermandantsystemrolleDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSystemrolle(SystemrolleDto systemrolleDto)
			throws ExceptionLP {

		try {
			return benutzerFac.createSystemrolle(systemrolleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}


	  public void kopiereLagerRechteEinerRolle(Integer systemrolleIIdQuelle,Integer systemrolleIIdZiel)
	      throws ExceptionLP {

	    try {
	    	benutzerFac.kopiereLagerRechteEinerRolle(systemrolleIIdQuelle,systemrolleIIdZiel, LPMain.getTheClient());
	    }
	    catch (Throwable ex) {
	      handleThrowable(ex);
	    }
	  }
	
	public void removeBenutzer(Integer iId) throws ExceptionLP {

		try {
			benutzerFac.removeBenutzer(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeNachrichtart(Integer iId) throws ExceptionLP {

		try {
			benutzerFac.removeNachrichtart(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBenutzermandantsystemrolle(Integer iId)
			throws ExceptionLP {

		try {
			benutzerFac.removeBenutzermandantsystemrolle(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSystemrolle(Integer iId) throws ExceptionLP {

		try {
			benutzerFac.removeSystemrolle(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeThemarolle(Integer iId) throws ExceptionLP {

		try {
			benutzerFac.removeThemarolle(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLagerrolle(Integer iId) throws ExceptionLP {

		try {
			benutzerFac.removeLagerrolle(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeFertigungsgrupperolle(Integer iId) throws ExceptionLP {

		try {
			benutzerFac.removeFertigungsgrupperolle(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBenutzer(BenutzerDto benutzerDto) throws ExceptionLP {

		try {
			benutzerFac.updateBenutzer(benutzerDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSystemrolle(SystemrolleDto systemrolleDto)
			throws ExceptionLP {

		try {
			benutzerFac.updateSystemrolle(systemrolleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBenutzermandantsystemrolle(
			BenutzermandantsystemrolleDto benutzermandantsystemrolleDto)
			throws ExceptionLP {

		try {
			benutzerFac.updateBenutzermandantsystemrolle(
					benutzermandantsystemrolleDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateThemarolle(ThemarolleDto themarolleDto)
			throws ExceptionLP {

		try {
			benutzerFac.updateThemarolle(themarolleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLagerrolle(LagerrolleDto lagerrolleDto)
			throws ExceptionLP {

		try {
			benutzerFac.updateLagerrolle(lagerrolleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateFertigungsgrupperolle(
			FertigungsgrupperolleDto fertigungsgrupperolleDto)
			throws ExceptionLP {

		try {
			benutzerFac.updateFertigungsgrupperolle(fertigungsgrupperolleDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateThema(ThemaDto themaDto) throws ExceptionLP {

		try {
			benutzerFac.updateThema(themaDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateNachrichtart(NachrichtartDto nachrichtartDto)
			throws ExceptionLP {

		try {
			benutzerFac.updateNachrichtart(nachrichtartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ThemaDto themaFindByPrimaryKey(String cNr) throws ExceptionLP {

		try {
			return benutzerFac.themaFindByPrimaryKey(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public NachrichtartDto nachrichtartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		try {
			return benutzerFac.nachrichtartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BenutzerDto benutzerFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return benutzerFac.benutzerFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BenutzerDto benutzerFindByCBenutzerkennung(String cBenutzerkennung,
			String cKennwort) throws ExceptionLP {

		try {
			return benutzerFac.benutzerFindByCBenutzerkennung(cBenutzerkennung,
					cKennwort);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByPrimaryKey(
			Integer iId) throws ExceptionLP {

		try {
			return benutzerFac.benutzermandantsystemrolleFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public int getAnzahlDerMandantenEinesBenutzers(String cBenutzerkennung,
			String cKennwort) throws ExceptionLP {

		try {
			return benutzerFac.getAnzahlDerMandantenEinesBenutzers(
					cBenutzerkennung,
					Helper.getMD5Hash(cBenutzerkennung + cKennwort).toString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return 0;
		}
	}

	public Integer[] getBerechtigteLagerIIdsEinerSystemrolle(Integer systemrolleIId) throws ExceptionLP {
		try {
			return benutzerFac
					.getBerechtigteLagerIIdsEinerSystemrolle(systemrolleIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public int getAnzahlDerNochNichtErledigtenAberNochZuBearbeitendenMeldungen()
			throws ExceptionLP {

		try {
			return benutzerFac
					.getAnzahlDerNochNichtErledigtenAberNochZuBearbeitendenMeldungen(LPMain
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return 0;
		}
	}

	public int getAnzahlDerUnbearbeitetenMeldungen() throws ExceptionLP {

		try {
			return benutzerFac.getAnzahlDerUnbearbeitetenMeldungen(LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return 0;
		}
	}

	public BenutzermandantsystemrolleDto[] benutzermandantsystemrolleFindByBenutzerIId(
			Integer benutzerIId) throws ExceptionLP {

		try {
			return benutzerFac
					.benutzermandantsystemrolleFindByBenutzerIId(benutzerIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BenutzermandantsystemrolleDto benutzermandantsystemrolleFindByBenutzerIIdSystemrolleIIdMandantCNr(
			Integer benutzerIId, String mandantCNr) throws ExceptionLP {

		try {
			return benutzerFac
					.benutzermandantsystemrolleFindByBenutzerIIdMandantCNr(
							benutzerIId, mandantCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SystemrolleDto systemrolleFindByCBez(String cBez) throws ExceptionLP {

		try {
			return benutzerFac.systemrolleFindByCBez(cBez);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SystemrolleDto systemrolleFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		try {
			return benutzerFac.systemrolleFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public NachrichtarchivDto nachrichtarchivFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		try {
			return benutzerFac.nachrichtarchivFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ThemarolleDto themarolleFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		try {
			return benutzerFac.themarolleFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FertigungsgrupperolleDto fertigungsgrupperolleFindByPrimaryKey(
			Integer iId) throws ExceptionLP {

		try {
			return benutzerFac.fertigungsgrupperolleFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerrolleDto lagerrolleFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		try {
			return benutzerFac.lagerrolleFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String[] getThemenDesAngemeldetenBenutzers() throws ExceptionLP {
		try {
			return benutzerFac.getThemenDesAngemeldetenBenutzers(LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer weiseNachrichtPersonZu(Integer nachrichtarchivIId)
			throws ExceptionLP {
		Integer personalBereitszugewiesenIId = null;
		try {
			personalBereitszugewiesenIId = benutzerFac.weiseNachrichtPersonZu(
					nachrichtarchivIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return personalBereitszugewiesenIId;
	}

	public void erledigeNachricht(Integer nachrichtarchivIId,
			String cErledigungsgrund) throws ExceptionLP {
		try {
			benutzerFac.erledigeNachricht(nachrichtarchivIId,
					cErledigungsgrund, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

}
