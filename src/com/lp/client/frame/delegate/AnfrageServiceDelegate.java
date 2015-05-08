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
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfrageartDto;
import com.lp.server.anfrage.service.AnfrageerledigungsgrundDto;
import com.lp.server.anfrage.service.AnfragepositionartDto;
import com.lp.server.anfrage.service.AnfragetextDto;
import com.lp.server.anfrage.service.ZertifikatartDto;
import com.lp.server.system.service.TheClientDto;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um die Anfrage Services.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>08.06.05</I></p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.7 $
 */
public class AnfrageServiceDelegate extends Delegate {
	private Context context;
	private AnfrageServiceFac anfrageServiceFac;

	public AnfrageServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			anfrageServiceFac = (AnfrageServiceFac) context
					.lookup("lpserver/AnfrageServiceFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public AnfragetextDto anfragetextFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		AnfragetextDto oAnfragetextDto = null;

		try {
			oAnfragetextDto = anfrageServiceFac
					.anfragetextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oAnfragetextDto;
	}

	public AnfrageerledigungsgrundDto anfrageerledigungsgrundFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		AnfrageerledigungsgrundDto anfrageerledigungsgrundDto = null;

		try {
			anfrageerledigungsgrundDto = anfrageServiceFac
					.anfrageerledigungsgrundFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return anfrageerledigungsgrundDto;
	}

	public ZertifikatartDto zertifikatartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		ZertifikatartDto zertifikatartDto = null;

		try {
			zertifikatartDto = anfrageServiceFac
					.zertifikatartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return zertifikatartDto;
	}

	public Integer createZertifikatart(ZertifikatartDto zertifikatartDto)
			throws ExceptionLP {
		Integer iId = null;

		try {
			zertifikatartDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			iId = anfrageServiceFac.createZertifikatart(zertifikatartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public Integer createAnfragetext(AnfragetextDto anfragetextDto)
			throws ExceptionLP {
		Integer iId = null;

		try {
			// fuer StammdatenCRUD : alle Felder, die in der UI nicht vorhanden
			// sind selbst befuellen
			anfragetextDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			anfragetextDto.setLocaleCNr(LPMain.getInstance().getTheClient()
					.getLocUiAsString());

			iId = anfrageServiceFac.createAnfragetext(anfragetextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public void removeZertifikatart(ZertifikatartDto zertifikatartDto)
			throws ExceptionLP {
		try {
			anfrageServiceFac.removeZertifikatart(zertifikatartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAnfragetext(AnfragetextDto anfragetextDto)
			throws ExceptionLP {
		try {
			anfrageServiceFac.removeAnfragetext(anfragetextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAnfragetext(AnfragetextDto anfragetextDto)
			throws ExceptionLP {
		try {
			anfrageServiceFac.updateAnfragetext(anfragetextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAnfrageerledigungsgrund(
			AnfrageerledigungsgrundDto anfrageerledigungsgrundDto)
			throws ExceptionLP {
		try {
			anfrageServiceFac
					.updateAnfrageerledigungsgrund(anfrageerledigungsgrundDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZertifikatart(ZertifikatartDto zertifikatartDto)
			throws ExceptionLP {
		try {
			anfrageServiceFac.updateZertifikatart(zertifikatartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Einen anfragespezifischen Text auslesen. Wenn der Text weder in der
	 * gewuenschten Sprache noch in der UI-Sprache des Benutzers noch in der
	 * Konzerndatensprache hinterlegt ist, wird er in der Konzerndatensprache
	 * neu angelegt.
	 * 
	 * @param cNrLocaleI
	 *            das gewuenschte Locale
	 * @param cNrMediaartI
	 *            die gewuenschte Mediaart, z.B. MediaFac.MEDIAART_KOPFTEXT
	 * @return AnfragetextDto der Anfragetext
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public AnfragetextDto anfragetextFindByMandantLocaleCNr(String cNrLocaleI,
			String cNrMediaartI) throws ExceptionLP {
		AnfragetextDto anfragetextDto = null;

		try {
			anfragetextDto = anfrageServiceFac
					.anfragetextFindByMandantLocaleCNr(cNrLocaleI,
							cNrMediaartI, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return anfragetextDto;
	}

	public Map<String, String> getAnfragepositionart(Locale locKundeI)
			throws ExceptionLP {
		Map<String, String> map = null;
		try {
			map = anfrageServiceFac.getAnfragepositionart(locKundeI, LPMain
					.getInstance().getUISprLocale(), LPMain.getInstance()
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return map;
	}

	public String createAnfragepositionart(
			AnfragepositionartDto anfragepositionartDtoI) throws ExceptionLP {
		String anfragepositionartCNr = null;

		try {
			anfragepositionartCNr = anfrageServiceFac
					.createAnfragepositionart(anfragepositionartDtoI, LPMain
							.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return anfragepositionartCNr;
	}

	public void updateAnfragepositionart(
			AnfragepositionartDto anfragepositionartDtoI) throws ExceptionLP {
		try {
			anfrageServiceFac.updateAnfragepositionart(anfragepositionartDtoI,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AnfragepositionartDto anfragepositionartFindByPrimaryKey(
			String cNrAnfragepositionartI) throws ExceptionLP {
		AnfragepositionartDto anfragepositionartDto = null;

		try {
			anfragepositionartDto = anfrageServiceFac
					.anfragepositionartFindByPrimaryKey(cNrAnfragepositionartI,
							LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return anfragepositionartDto;
	}

	/**
	 * Eine neue Anfrageart anlegen.
	 * 
	 * @param anfrageartDtoI
	 *            die neue Anfrageart
	 * @return String PK der neuen Anfrageart
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public String createAnfrageart(AnfrageartDto anfrageartDtoI)
			throws ExceptionLP {
		String anfrageartCNr = null;

		try {
			anfrageartCNr = anfrageServiceFac.createAnfrageart(anfrageartDtoI,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return anfrageartCNr;
	}

	public Integer createAnfrageerledigungsgrund(
			AnfrageerledigungsgrundDto anfrageerledigungsgrundDto)
			throws ExceptionLP {
		try {
			return anfrageServiceFac.createAnfrageerledigungsgrund(
					anfrageerledigungsgrundDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeAnfrageerledigungsgrund(Integer iId) throws ExceptionLP {
		try {
			anfrageServiceFac.removeAnfrageerledigungsgrund(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine bestehende Anfrageart loeschen.
	 * 
	 * @param cNrAnfrageartI
	 *            die bestehende Anfrageart
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void removeAnfrageart(String cNrAnfrageartI) throws ExceptionLP {
		try {
			anfrageServiceFac.removeAnfrageart(cNrAnfrageartI, LPMain
					.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine bestehende Anfrageart aktualisieren.
	 * 
	 * @param anfrageartDtoI
	 *            die bestehende Anfrageart
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfrageart(AnfrageartDto anfrageartDtoI)
			throws ExceptionLP {
		try {
			anfrageServiceFac.updateAnfrageart(anfrageartDtoI, LPMain
					.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public boolean sindErledigugnsgruendeVorhanden()
			throws ExceptionLP {
		try {
			return anfrageServiceFac.sindErledigugnsgruendeVorhanden(LPMain
					.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	public AnfrageartDto anfrageartFindByPrimaryKey(String cNrI)
			throws ExceptionLP {
		AnfrageartDto angeboartDto = null;

		try {
			angeboartDto = anfrageServiceFac.anfrageartFindByPrimaryKey(cNrI,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angeboartDto;
	}

	/**
	 * Alle Anfragearten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locale1
	 *            bevorzugtes Locale
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return Map die Liste der Anfragearten
	 */
	public Map<?, ?> getAnfragearten(Locale locale1) throws ExceptionLP {
		Map<?, ?> map = null;

		try {
			map = anfrageServiceFac.getAnfragearten(locale1, LPMain
					.getInstance().getTheClient().getLocUi(), LPMain
					.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return map;
	}
}
