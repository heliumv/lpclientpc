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
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotartDto;
import com.lp.server.angebot.service.AngeboterledigungsgrundDto;
import com.lp.server.angebot.service.AngebotpositionartDto;
import com.lp.server.angebot.service.AngebottextDto;
import com.lp.server.system.service.MediaFac;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich um die Angebot Services.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>04.07.05</I></p>
 * @author $Author: christian $
 * @version $Revision: 1.8 $
 */
public class AngebotServiceDelegate extends Delegate {
	private Context context;
	private AngebotServiceFac angebotServiceFac;

	public AngebotServiceDelegate() throws Exception {
		context = new InitialContext();
		angebotServiceFac = (AngebotServiceFac) context
				.lookup("lpserver/AngebotServiceFacBean/remote");
	}

	public AngebottextDto angebottextFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		AngebottextDto angebottextDto = null;

		try {
			angebottextDto = angebotServiceFac.angebottextFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return angebottextDto;
	}

	/**
	 * Den default Kopftext eines Angebots holen.
	 * 
	 * @param pLocKunde
	 *            die Sprache des Kunden
	 * @return AngebottextDto der Kopftext
	 * @throws ExceptionLP
	 */
	public AngebottextDto getAngebotkopfDefault(String pLocKunde)
			throws ExceptionLP {
		AngebottextDto oKoftextDto = null;

		try {
			oKoftextDto = angebotServiceFac
					.angebottextFindByMandantCNrLocaleCNrCNr(pLocKunde,
							MediaFac.MEDIAART_KOPFTEXT, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oKoftextDto;
	}

	/**
	 * Den default Fusstext eines Angebots holen.
	 * 
	 * @param pLocKunde
	 *            die Sprache des Kunden
	 * @return AngebottextDto der Fusstext
	 * @throws ExceptionLP
	 */
	public AngebottextDto getAngebotfussDefault(String pLocKunde)
			throws ExceptionLP {
		AngebottextDto oFusstextDto = null;

		try {
			oFusstextDto = angebotServiceFac
					.angebottextFindByMandantCNrLocaleCNrCNr(pLocKunde,
							MediaFac.MEDIAART_FUSSTEXT, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oFusstextDto;
	}

	public Integer createAngebottext(AngebottextDto angebottextDto)
			throws ExceptionLP {
		Integer iId = null;

		try {
			// fuer StammdatenCRUD : alle Felder, die in der UI nicht vorhanden
			// sind selbst befuellen
			angebottextDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			angebottextDto.setLocaleCNr(LPMain.getInstance().getTheClient()
					.getLocUiAsString());

			iId = angebotServiceFac.createAngebottext(angebottextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public void removeAngebottext(AngebottextDto angebottextDto)
			throws ExceptionLP {
		try {
			angebotServiceFac.removeAngebottext(angebottextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAngebottext(AngebottextDto angebottextDto)
			throws ExceptionLP {
		try {
			angebotServiceFac.updateAngebottext(angebottextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public AngebottextDto angebottextFindByMandantCNrLocaleCNrCNr(
			String sLocaleI, String sCNrI) throws ExceptionLP {
		AngebottextDto angebottextDto = null;

		try {
			angebottextDto = angebotServiceFac
					.angebottextFindByMandantCNrLocaleCNrCNr(sLocaleI, sCNrI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return angebottextDto;
	}

	public AngebottextDto createDefaultAngebottext(String sMediaartI,
			String cNrLocaleI) throws ExceptionLP {
		AngebottextDto oAngebottextDto = null;

		try {
			oAngebottextDto = angebotServiceFac.createDefaultAngebottext(
					sMediaartI, cNrLocaleI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oAngebottextDto;
	}

	/**
	 * Eine neue Angebotart anlegen.
	 * 
	 * @param angebotartDtoI
	 *            die neue Angebotart
	 * @return String PK der neuen Angebotart
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public String createAngebotart(AngebotartDto angebotartDtoI)
			throws ExceptionLP {
		String angebotartCNr = null;

		try {
			angebotartCNr = angebotServiceFac.createAngebotart(angebotartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angebotartCNr;
	}

	/**
	 * Eine bestehende Angebotart loeschen.
	 * 
	 * @param cNrAngebotartI
	 *            die bestehende Angebotart
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void removeAngebotart(String cNrAngebotartI) throws ExceptionLP {
		try {
			angebotServiceFac.removeAngebotart(cNrAngebotartI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine bestehende Angebotart aktualisieren.
	 * 
	 * @param angebotartDtoI
	 *            die bestehende Angebotart
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAngebotart(AngebotartDto angebotartDtoI)
			throws ExceptionLP {
		try {
			angebotServiceFac.updateAngebotart(angebotartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AngebotartDto angebotartFindByPrimaryKey(String cNrI)
			throws ExceptionLP {
		AngebotartDto angeboartDto = null;

		try {
			angeboartDto = angebotServiceFac.angebotartFindByPrimaryKey(cNrI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angeboartDto;
	}

	public String createAngeboterledigungsgrund(
			AngeboterledigungsgrundDto angeboterledigungsgrundDtoI)
			throws ExceptionLP {
		String angeboterledigungsgrundCNr = null;

		try {
			angeboterledigungsgrundDtoI.setMandantCNr(LPMain.getInstance()
					.getTheClient().getMandant());
			angeboterledigungsgrundCNr = angebotServiceFac
					.createAngeboterledigungsgrund(angeboterledigungsgrundDtoI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angeboterledigungsgrundCNr;
	}

	public void removeAngeboterledigungsgrund(String cNrAngeboterledigungsgrundI)
			throws ExceptionLP {
		try {
			angebotServiceFac.removeAngeboterledigungsgrund(
					cNrAngeboterledigungsgrundI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateAngeboterledigungsgrund(
			AngeboterledigungsgrundDto angeboterledigungsgrundDtoI)
			throws ExceptionLP {
		try {
			angebotServiceFac.updateAngeboterledigungsgrund(
					angeboterledigungsgrundDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AngeboterledigungsgrundDto angeboterledigungsgrundFindByPrimaryKey(
			String cNrI) throws ExceptionLP {
		AngeboterledigungsgrundDto angeboterledigungsgrundDto = null;

		try {
			angeboterledigungsgrundDto = angebotServiceFac
					.angeboterledigungsgrundFindByPrimaryKey(cNrI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angeboterledigungsgrundDto;
	}

	/**
	 * Alle Angebotarten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locKundeI
	 *            bevorzugtes Locale
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return Map die Liste der Angebotarten
	 */
	public Map<?, ?> getAngebotarten(Locale locKundeI) throws ExceptionLP {
		Map<?, ?> map = null;

		try {
			map = angebotServiceFac.getAngebotarten(locKundeI, LPMain
					.getInstance().getUISprLocale());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return map;
	}

	/**
	 * Alle Angeboteinheiten holen. Aufbereitet fuer die Darstellung in einer
	 * ComboBox.
	 * 
	 * @return Map die Liste der Einheiten
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Map<?, ?> getAngeboteinheiten() throws ExceptionLP {
		Map<?, ?> map = null;

		try {
			map = angebotServiceFac.getAngeboteinheiten(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return map;
	}

	public Map<String, String> getAngebotpositionart(Locale locale1I)
			throws ExceptionLP {
		Map<String, String> map = null;

		try {
			map = angebotServiceFac.getAngebotpositionart(locale1I, LPMain
					.getInstance().getUISprLocale(), LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return map;
	}

	public String createAngebotpositionart(
			AngebotpositionartDto angebotpositionartDtoI) throws ExceptionLP {
		String angebotpositionartCNr = null;

		try {
			angebotpositionartCNr = angebotServiceFac.createAngebotpositionart(
					angebotpositionartDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angebotpositionartCNr;
	}

	public void updateAngebotpositionart(
			AngebotpositionartDto angebotpositionartDtoI) throws ExceptionLP {
		try {
			angebotServiceFac.updateAngebotpositionart(angebotpositionartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AngebotpositionartDto angebotpositionartFindByPrimaryKey(
			String cNrAngebotpositionartI) throws ExceptionLP {
		AngebotpositionartDto angebotpositionartDto = null;

		try {
			angebotpositionartDto = angebotServiceFac
					.angebotpositionartFindByPrimaryKey(cNrAngebotpositionartI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angebotpositionartDto;
	}

	/**
	 * Die in Stunden hinterlegte Lieferzeit in die gewuenschte Angebotseinheit
	 * umrechnen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param cNrAngeboteinheitI
	 *            die gewuenschte Einheit
	 * @return Integer die Lieferzeit in der gewuenschten Einheit
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer getLieferzeitInAngeboteinheit(Integer iIdAngebotI,
			String cNrAngeboteinheitI) throws ExceptionLP {
		Integer iiLieferzeit = null;

		try {
			iiLieferzeit = angebotServiceFac.getLieferzeitInAngeboteinheit(
					iIdAngebotI, cNrAngeboteinheitI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iiLieferzeit;
	}
}
