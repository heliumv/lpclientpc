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
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Anfrageposition.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>14.06.05</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.8 $
 */
public class AnfragepositionDelegate extends Delegate {

	private AnfragepositionFac anfragepositionFac;
	private Context context;

	public AnfragepositionDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			anfragepositionFac = (AnfragepositionFac) context
					.lookup("lpserver/AnfragepositionFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Anlegen einer neuen Anfrageposition.
	 * 
	 * @param anfragepositionDtoI
	 *            die neue Anfrageposition
	 * @return Integer PK der neuen Anfrageposition
	 * @throws ExceptionLP
	 */
	public Integer createAnfrageposition(AnfragepositionDto anfragepositionDtoI)
			throws ExceptionLP {
		Integer iIdAnfrageposition = null;

		try {
			iIdAnfrageposition = anfragepositionFac.createAnfrageposition(
					anfragepositionDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdAnfrageposition;
	}

	public String getPositionAsStringDocumentWS(Integer aIIdAnfragePOSI[])
			throws ExceptionLP {

		String sRet = null;
		try {
			sRet = anfragepositionFac.getPositionAsStringDocumentWS(
					aIIdAnfragePOSI, LPMain.getInstance().getCNrUser());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return sRet;
	}

	/**
	 * Eine Anfrageposition loeschen.
	 * 
	 * @param anfragepositionDtoI
	 *            die Anfrageposition
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void removeAnfrageposition(AnfragepositionDto anfragepositionDtoI)
			throws ExceptionLP {
		try {
			anfragepositionFac.removeAnfrageposition(anfragepositionDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine bestehende Anfrageposition aktualisieren.
	 * 
	 * @param anfragepositionDtoI
	 *            die Anfrageposition
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfrageposition(AnfragepositionDto anfragepositionDtoI)
			throws ExceptionLP {
		try {
			anfragepositionFac.updateAnfrageposition(anfragepositionDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AnfragepositionDto anfragepositionFindByPrimaryKey(
			Integer iIdAnfragepositionI) throws ExceptionLP {
		AnfragepositionDto anfragepositionDto = null;

		try {
			anfragepositionDto = anfragepositionFac
					.anfragepositionFindByPrimaryKey(iIdAnfragepositionI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return anfragepositionDto;
	}

	public AnfragepositionDto[] anfragepositionFindByAnfrage(Integer anfrageIId)
			throws ExceptionLP {
		AnfragepositionDto[] anfragepositionDto = null;

		try {
			anfragepositionDto = anfragepositionFac
					.anfragepositionFindByAnfrage(anfrageIId,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return anfragepositionDto;
	}

	/**
	 * Anlegen einer neuen Anfragepositionlieferdaten.
	 * 
	 * @param anfragepositionlieferdatenDtoI
	 *            die neue Anfragepositionlieferdaten
	 * @return Integer PK der neuen Anfragepositionlieferdaten
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer createAnfragepositionlieferdaten(
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI)
			throws ExceptionLP {
		Integer iIdAnfragepositionlieferdaten = null;

		try {
			iIdAnfragepositionlieferdaten = anfragepositionFac
					.createAnfragepositionlieferdaten(
							anfragepositionlieferdatenDtoI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdAnfragepositionlieferdaten;
	}

	/**
	 * Eine Anfragepositionlieferdaten loeschen.
	 * 
	 * @param anfragepositionlieferdatenDtoI
	 *            die Anfragepositionlieferdaten
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void removeAnfragepositionlieferdaten(
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI)
			throws ExceptionLP {
		try {
			anfragepositionFac.removeAnfragepositionlieferdaten(
					anfragepositionlieferdatenDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine bestehende Anfragepositionlieferdaten aktualisieren.
	 * 
	 * @param anfragepositionlieferdatenDtoI
	 *            die Anfragepositionlieferdaten
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfragepositionlieferdaten(
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI)
			throws ExceptionLP {
		try {
			anfragepositionFac.updateAnfragepositionlieferdaten(
					anfragepositionlieferdatenDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AnfragepositionlieferdatenDto anfragepositionlieferdatenFindByPrimaryKey(
			Integer iIdAnfragepositionlieferdatenI) throws ExceptionLP {
		AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = null;

		try {
			anfragepositionlieferdatenDto = anfragepositionFac
					.anfragepositionlieferdatenFindByPrimaryKey(
							iIdAnfragepositionlieferdatenI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return anfragepositionlieferdatenDto;
	}

	/**
	 * Zwei bestehende Anfragepositionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPosition1I
	 *            PK der ersten Position
	 * @param iIdPosition2I
	 *            PK der zweiten Position
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void vertauscheAnfragepositionen(Integer iIdPosition1I,
			Integer iIdPosition2I) throws ExceptionLP {
		try {
			anfragepositionFac.vertauscheAnfragepositionen(iIdPosition1I,
					iIdPosition2I, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAnfrageI, int iSortierungNeuePositionI)
			throws ExceptionLP {
		try {
			anfragepositionFac
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							iIdAnfrageI, iSortierungNeuePositionI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Berechnet die Anzahl der mengenbehafteten Positionen zu einer bestimmten
	 * Anfrage.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @return int die Anzahl der Positonen
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public int getAnzahlMengenbehafteteAnfragepositionen(Integer iIdAnfrageI)
			throws ExceptionLP {
		int iAnzahl = 0;

		try {
			iAnzahl = anfragepositionFac
					.getAnzahlMengenbehafteteAnfragepositionen(iIdAnfrageI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iAnzahl;
	}

	/**
	 * Eine bestehende Anfragepositionlieferdaten loeschen.
	 * 
	 * @param anfragepositionlieferdatenDtoI
	 *            die Anfragepositionlieferdaten
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void resetAnfragepositionlieferdaten(
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI)
			throws ExceptionLP {
		try {
			anfragepositionFac.resetAnfragepositionlieferdaten(
					anfragepositionlieferdatenDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AnfragepositionlieferdatenDto[] anfragepositionlieferdatenFindByAnfragepositionIIdBErfasst(
			Integer iIdAnfragepositionI, Short bErfasstI) throws ExceptionLP {
		AnfragepositionlieferdatenDto[] aAnfragepositionlieferdatenDto = null;

		try {
			aAnfragepositionlieferdatenDto = anfragepositionFac
					.anfragepositionlieferdatenFindByAnfragepositionIIdBErfasst(
							iIdAnfragepositionI, bErfasstI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aAnfragepositionlieferdatenDto;
	}
}
