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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.LocaleFac;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Anfrage.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>07.06.05</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.9 $
 */
public class AnfrageDelegate extends Delegate {

	private AnfrageFac anfrageFac;
	private Context context;

	public AnfrageDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			anfrageFac = (AnfrageFac) context
					.lookup("lpserver/AnfrageFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Anlegen einer neuen Anfrage.
	 * 
	 * @param anfrageDtoI
	 *            die neue Anfrage
	 * @return Integer PK der neuen Anfrage
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer createAnfrage(AnfrageDto anfrageDtoI) throws ExceptionLP {
		Integer iIdAnfrage = null;

		try {
			iIdAnfrage = anfrageFac.createAnfrage(anfrageDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdAnfrage;
	}

	/**
	 * Eine Anfrage stornieren.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void storniereAnfrage(Integer iIdAnfrageI) throws ExceptionLP {
		try {
			anfrageFac.storniereAnfrage(iIdAnfrageI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine Anfrage mit Status 'Storniert' auf 'Offen' setzen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void stornoAufheben(Integer iIdAnfrageI) throws ExceptionLP {
		try {
			anfrageFac.stornoAufheben(iIdAnfrageI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine Anfrage manuell auf erledigt setzen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void manuellErledigen(Integer iIdAnfrageI,
			Integer erledigungsgrundIId) throws ExceptionLP {
		try {
			anfrageFac.manuellErledigen(iIdAnfrageI, erledigungsgrundIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void aktiviereBelegControlled(Integer iid, Timestamp t)
			throws ExceptionLP {
		try {
			anfrageFac.aktiviereBelegControlled(iid, t, LPMain.getTheClient());
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
	}

	public Timestamp berechneBelegControlled(Integer iid) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = anfrageFac.berechneBelegControlled(
					iid, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto);
			return pruefungDto.getBerechnungsZeitpunkt();
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public Timestamp berechneAktiviereBelegControlled(Integer iid)
			throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = anfrageFac
					.berechneAktiviereBelegControlled(iid,
							LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto);
			return pruefungDto.getBerechnungsZeitpunkt();
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public ArrayList<Integer> getAngelegteAnfragenNachUmwandlungDerLiefergruppenanfragen(
			Integer liefergruppeIId) throws ExceptionLP {
		try {
			return anfrageFac
					.getAngelegteAnfragenNachUmwandlungDerLiefergruppenanfragen(
							liefergruppeIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine bestehende Anfrage aktualisieren.
	 * 
	 * @param anfrageDtoI
	 *            die aktuelle Anfrage
	 * @param waehrungOriCNrI
	 *            die urspruengliche Belegwaehrung
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfrage(AnfrageDto anfrageDtoI,
			boolean bAufAngelegtZuruecksetzen, String waehrungOriCNrI)
			throws ExceptionLP {
		try {
			anfrageFac.updateAnfrage(anfrageDtoI, waehrungOriCNrI,
					bAufAngelegtZuruecksetzen, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AnfrageDto anfrageFindByPrimaryKey(Integer iIdAnfrageI)
			throws ExceptionLP {
		AnfrageDto anfrageDto = null;

		try {
			anfrageDto = anfrageFac.anfrageFindByPrimaryKey(iIdAnfrageI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return anfrageDto;
	}

	/**
	 * Berechne den Gesamtwert einer bestimmten Anfrage in einer bestimmten
	 * Waehrung. <br>
	 * Der Gesamtwert berechnet sich aus
	 * <p>
	 * Summe der Nettogesamtpreise der Positionen <br>
	 * - Allgemeiner Rabatt <br>
	 * Beruecksichtigt werden alle mengenbehafteten Positionen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gesamtwert der Anfrage
	 */
	public BigDecimal berechneNettowertGesamt(Integer iIdAnfrageI)
			throws ExceptionLP {
		BigDecimal bdNettowertgesamt = null;

		try {
			bdNettowertgesamt = anfrageFac.berechneNettowertGesamt(iIdAnfrageI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return bdNettowertgesamt;
	}

	/**
	 * Wenn der Abschlag in den Konditionen geaendert wurde, dann werden im
	 * Anschluss die davon abhaengigen Werte neu berechnet.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfrageKonditionen(AnfrageDto anfrageDto)
			throws ExceptionLP {
		try {
			anfrageFac.updateAnfrageKonditionen(anfrageDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateAnfrageLieferKonditionen(Integer iIdAnfrageI)
			throws ExceptionLP {
		try {
			anfrageFac.updateAnfrageLieferKonditionen(iIdAnfrageI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Den Status einer Anfrage von 'Erledigt' auf 'Erfasst' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @throws ExceptionLP
	 */
	public void erledigungAufheben(Integer iIdAnfrageI) throws ExceptionLP {
		try {
			anfrageFac.erledigungAufheben(iIdAnfrageI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Alle Anfragen zu einer Liefergruppenanfrage erzeugen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Liefergruppenanfrage
	 * @return Integer PK der letzten erzeugten Anfrage
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public ArrayList<Integer> erzeugeAnfragenAusLiefergruppenanfrage(
			Integer iIdAnfrageI) throws ExceptionLP {
		ArrayList<Integer> iIdLetzteErzeugteAnfrage = null;

		try {
			iIdLetzteErzeugteAnfrage = anfrageFac
					.erzeugeAnfragenAusLiefergruppenanfrage(iIdAnfrageI, null,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdLetzteErzeugteAnfrage;
	}

	public Integer erzeugeAnfrageAusAnfrage(Integer iIdAnfrageI,InternalFrame internalFrame)
			throws ExceptionLP {
		try {
			Integer anfrageIId= anfrageFac.erzeugeAnfrageAusAnfrage(iIdAnfrageI,
					LPMain.getTheClient());
			
			if (anfrageIId != null) {
				AnfrageDto afDto = anfrageFindByPrimaryKey(anfrageIId);
				if(afDto.getLieferantIIdAnfrageadresse()!=null){
					DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.pruefeLieferant(
							afDto.getLieferantIIdAnfrageadresse(),
							LocaleFac.BELEGART_ANFRAGE, internalFrame);
				}
				
			}
			return anfrageIId;
			
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public AnfrageDto[] anfrageFindByAnfrageIIdLiefergruppenanfrage(
			Integer iIdAnfrageLiefergruppenanfrageI) throws ExceptionLP {
		AnfrageDto[] aAnfrageDto = null;

		try {
			aAnfrageDto = anfrageFac
					.anfrageFindByAnfrageIIdLiefergruppenanfrage(
							iIdAnfrageLiefergruppenanfrageI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aAnfrageDto;
	}

	/**
	 * Nach Kriterien, die der Benutzer bestimmt hat, Anfragen aus einem
	 * existierenden Bestellvorschlag erzeugen.
	 * 
	 * @param bestellvorschlagUeberleitungKriterienDtoI
	 *            die Kriterien des Benutzers
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void erzeugeAnfragenAusBestellvorschlag(
			BestellvorschlagUeberleitungKriterienDto bestellvorschlagUeberleitungKriterienDtoI)
			throws ExceptionLP {
		try {
			anfrageFac.erzeugeAnfragenAusBestellvorschlag(
					bestellvorschlagUeberleitungKriterienDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void setzeVersandzeitpunktAufJetzt(Integer iAnfrageIId,
			String sDruckart) throws ExceptionLP {
		try {
			anfrageFac.setzeVersandzeitpunktAufJetzt(iAnfrageIId, sDruckart);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

}
