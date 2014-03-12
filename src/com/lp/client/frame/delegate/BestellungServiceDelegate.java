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
import com.lp.server.bestellung.service.BestellpositionartDto;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellungsartDto;
import com.lp.server.bestellung.service.BestellungstatusDto;
import com.lp.server.bestellung.service.BestellungtextDto;
import com.lp.server.bestellung.service.MahngruppeDto;
import com.lp.server.system.service.MediaFac;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>Delegate fuer Bestellung Services.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Uli Walch, 27. 04. 2005</p>
 *
 * <p>@author Uli Walch</p>
 *
 * @version 1.0
 *
 * @version $Revision: 1.8 $ Date $Date: 2011/05/26 09:16:55 $
 */
public class BestellungServiceDelegate extends Delegate {
	private Context context = null;
	private BestellungServiceFac bestellungServiceFac = null;

	public BestellungServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			bestellungServiceFac = (BestellungServiceFac) context
					.lookup("lpserver/BestellungServiceFacBean/remote");
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t.toString(), null);
		}
	}

	public BestellungtextDto bestellungtextFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		BestellungtextDto oBestellungtextDto = null;

		try {
			oBestellungtextDto = bestellungServiceFac
					.bestellungtextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oBestellungtextDto;
	}

	/**
	 * Den default Kopftext einer Bestellung holen.
	 * 
	 * @param pLocKunde
	 *            die Sprache des Kunden
	 * @return BestellungtextDto der Kopftext
	 * @throws ExceptionLP
	 */
	public BestellungtextDto getBestellungkopfDefault(String pLocKunde)
			throws ExceptionLP {
		BestellungtextDto oKoftextDto = null;

		try {
			oKoftextDto = bestellungServiceFac
					.bestellungtextFindByMandantLocaleCNr(LPMain.getInstance()
							.getTheClient().getMandant(), pLocKunde,
							MediaFac.MEDIAART_KOPFTEXT, LPMain.getInstance()
									.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oKoftextDto;
	}

	/**
	 * Den default Fusstext einer Bestellung holen.
	 * 
	 * @param pLocKunde
	 *            die Sprache des Kunden
	 * @return BestellungtextDto der Fusstext
	 * @throws ExceptionLP
	 */
	public BestellungtextDto getBestellungfussDefault(String pLocKunde)
			throws ExceptionLP {
		BestellungtextDto oFusstextDto = null;

		try {
			oFusstextDto = bestellungServiceFac
					.bestellungtextFindByMandantLocaleCNr(LPMain.getInstance()
							.getTheClient().getMandant(), pLocKunde,
							MediaFac.MEDIAART_FUSSTEXT, LPMain.getInstance()
									.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oFusstextDto;
	}

	public Integer createBestellungtext(BestellungtextDto bestellungtextDto)
			throws ExceptionLP {
		Integer iId = null;

		try {
			// fuer StammdatenCRUD : alle Felder, die in der UI nicht vorhanden
			// sind selbst befuellen
			bestellungtextDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			bestellungtextDto.setLocaleCNr(LPMain.getInstance().getTheClient()
					.getLocUiAsString());

			iId = bestellungServiceFac.createBestellungtext(bestellungtextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public void removeBestellungtext(BestellungtextDto bestellungtextDto)
			throws ExceptionLP {
		try {
			bestellungServiceFac.removeBestellungtext(bestellungtextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBestellungtext(BestellungtextDto bestellungtextDto)
			throws ExceptionLP {
		try {
			bestellungServiceFac.updateBestellungtext(bestellungtextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BestellungtextDto bestellungtextFindByMandantLocaleCNr(
			String sLocaleI, String sCNrI) throws ExceptionLP {
		BestellungtextDto bestellungtextDto = null;

		try {
			bestellungtextDto = bestellungServiceFac
					.bestellungtextFindByMandantLocaleCNr(LPMain.getInstance()
							.getTheClient().getMandant(), sLocaleI, sCNrI,
							LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return bestellungtextDto;
	}

	public BestellungtextDto createDefaultBestellungtext(String sMediaartI,
			String sTextinhaltI) throws ExceptionLP {
		BestellungtextDto bestellungtextDto = null;

		try {
			bestellungtextDto = bestellungServiceFac
					.createDefaultBestellungtext(sMediaartI, sTextinhaltI,
							LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return bestellungtextDto;
	}

	public String createBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws ExceptionLP {
		String cNr = null;
		try {
			cNr = bestellungServiceFac
					.createBestellungstatus(bestellungstatusDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return cNr;
	}

	public void createMahngruppe(MahngruppeDto mahngruppeDto)
			throws ExceptionLP {
		try {
			bestellungServiceFac.createMahngruppe(mahngruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeBestellungsart(BestellungsartDto bestellungartDto)
			throws ExceptionLP {
		try {
			bestellungServiceFac.removeBestellungart(bestellungartDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeMahngruppe(MahngruppeDto mahngruppeDto)
			throws ExceptionLP {
		try {
			bestellungServiceFac.removeMahngruppe(mahngruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws ExceptionLP {
		try {
			bestellungServiceFac.removeBestellungstatus(bestellungstatusDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws ExceptionLP {
		try {
			bestellungServiceFac.updateBestellungstatus(bestellungstatusDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateBestellungsart(BestellungsartDto bestellungartDto)
			throws ExceptionLP {
		try {
			bestellungServiceFac.updateBestellungart(bestellungartDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public String createBestellungsart(BestellungsartDto bestellungartDto)
			throws ExceptionLP {
		String sBestellungart = null;
		try {
			sBestellungart = bestellungServiceFac.createBestellungart(
					bestellungartDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return sBestellungart;
	}

	public BestellungsartDto bestellungsartFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		BestellungsartDto dto = null;
		try {
			dto = bestellungServiceFac.bestellungartFindByPrimaryKey(cNr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}


	public MahngruppeDto mahngruppeFindByPrimaryKey(Integer artgruIId)
			throws ExceptionLP {
		MahngruppeDto dto = null;
		try {
			dto = bestellungServiceFac.mahngruppeFindByPrimaryKey(artgruIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}

	public BestellungstatusDto bestellungstatusFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		BestellungstatusDto dto = null;
		try {
			dto = bestellungServiceFac.bestellungstatusFindByPrimaryKey(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}

	/**
	 * Alle Bestellungarten in bestmoeglicher Uebersetzung von der DB holen.
	 * 
	 * @param pLocLieferant
	 *            das Locale des Lieferanten
	 * @return Map die Bestellungarten mit ihrer Uebersetzung
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public Map<?, ?> getBestellungsart(Locale pLocLieferant) throws ExceptionLP {
		Map<?, ?> arten = null;
		try {
			arten = bestellungServiceFac.getBestellungart(pLocLieferant, LPMain
					.getInstance().getUISprLocale());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return arten;
	}

	/**
	 * Alle verfuegbaren Bestellpositionsarten in bestmoeglicher Uebersetzung
	 * aus der DB holen.
	 * 
	 * @param pLocKunde
	 *            Locale
	 * @return Map
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public Map<String, String> getBestellpositionart(Locale pLocKunde)
			throws ExceptionLP {
		Map<String, String> posarten = null;
		try {
			posarten = bestellungServiceFac.getBestellpositionart(pLocKunde,
					LPMain.getInstance().getUISprLocale());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return posarten;
	}

	public String createBestellpositionart(
			BestellpositionartDto bestellpositionartDtoI) throws ExceptionLP {
		String bestellpositionartCNr = null;

		try {
			bestellpositionartCNr = bestellungServiceFac
					.createBestellpositionart(bestellpositionartDtoI, LPMain
							.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return bestellpositionartCNr;
	}

	public void updateBestellpositionart(
			BestellpositionartDto bestellpositionartDtoI) throws ExceptionLP {
		try {
			bestellungServiceFac
					.updateBestellpositionart(bestellpositionartDtoI, LPMain
							.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public BestellpositionartDto bestellpositionartFindByPrimaryKey(
			String cNrBestellpositionartI) throws ExceptionLP {
		BestellpositionartDto bestellpositionartDto = null;

		try {
			bestellpositionartDto = bestellungServiceFac
					.bestellpositionartFindByPrimaryKey(cNrBestellpositionartI,
							LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return bestellpositionartDto;
	}

	public BestellungsartDto[] getAllBestellungsArt() throws ExceptionLP {
		BestellungsartDto[] toReturn = null;
		try {
			toReturn = bestellungServiceFac.getAllBestellungsArt();
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return toReturn;
	}
}
