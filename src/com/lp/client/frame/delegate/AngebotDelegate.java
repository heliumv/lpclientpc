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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.system.service.LocaleFac;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um das Angebot.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>04.07.05</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.9 $
 */
public class AngebotDelegate extends Delegate {
	private Context context;
	private AngebotFac angebotFac;

	public AngebotDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			angebotFac = (AngebotFac) context
					.lookup("lpserver/AngebotFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Anlegen einer neuen Angebot.
	 * 
	 * @param angebotDtoI
	 *            die neue Angebot
	 * @return Integer PK der neuen Angebot
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer createAngebot(AngebotDto angebotDtoI) throws ExceptionLP {
		Integer iIdAngebot = null;

		try {
			iIdAngebot = angebotFac.createAngebot(angebotDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdAngebot;
	}

	/**
	 * Eine Angebot stornieren.
	 * 
	 * @param angebotDtoI
	 *            das Angebot
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void storniereAngebot(AngebotDto angebotDtoI) throws ExceptionLP {
		try {
			angebotFac.storniereAngebot(angebotDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine Angebot mit Status 'Storniert' auf 'Offen' setzen.
	 * 
	 * @param iIdAngebotI
	 *            PK der Angebot
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void stornoAufheben(Integer iIdAngebotI) throws ExceptionLP {
		try {
			angebotFac.stornoAufheben(iIdAngebotI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine Angebot manuell auf erledigt setzen.
	 * 
	 * @param iIdAngebotI
	 *            PK der Angebot
	 * @param cNrAngeboterledigungsgrundI
	 *            der Erledigungsgrund
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void angebotManuellErledigen(Integer iIdAngebotI,
			String cNrAngeboterledigungsgrundI) throws ExceptionLP {
		try {
			angebotFac.angebotManuellErledigen(iIdAngebotI,
					cNrAngeboterledigungsgrundI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine Angebot manuell auf erledigt setzen.
	 * 
	 * @param iIdAngebotI
	 *            PK der Angebot
	 * @param iIdAuftragI
	 *            der Auftrag
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public boolean angebotManuellErledigendurchAuftrag(Integer iIdAngebotI,
			Integer iIdAuftragI) throws ExceptionLP {
		try {
			return angebotFac.angebotManuellErledigendurchAuftrag(iIdAngebotI,
					iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	/**
	 * Den Status eines Angebots von 'Erledigt' auf 'Offen' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void manuelleErledigungAufheben(Integer iIdAngebotI)
			throws ExceptionLP {
		try {
			angebotFac.manuelleErledigungAufheben(iIdAngebotI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine bestehende Angebot aktualisieren.
	 * 
	 * @param angebotDtoI
	 *            das aktuelle Angebot
	 * @param waehrungOriCNrI
	 *            die urspruengliche Waehrung
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public boolean updateAngebot(AngebotDto angebotDtoI, String waehrungOriCNrI)
			throws ExceptionLP {
		try {
			return angebotFac.updateAngebot(angebotDtoI, waehrungOriCNrI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	/**
	 * Angebotstatus eines bestehenden Angebot aendern.
	 * 
	 * @param angebotDto
	 *            AngebotDto
	 * @param pStatus
	 *            String
	 * @throws ExceptionLP
	 */
	public void setAngebotstatus(AngebotDto angebotDto, String pStatus)
			throws ExceptionLP {
		angebotDto.setStatusCNr(pStatus);
		updateAngebot(angebotDto, null);
	}

	/**
	 * Die Daten eines Angebots aktualisieren ohne weitere Aenderungen
	 * vorzunehmen. Pflege der Akquisedaten.
	 * 
	 * @param angebotDtoI
	 *            AngebotDto
	 * @throws ExceptionLP
	 */
	public void updateAngebotOhneWeitereAktion(AngebotDto angebotDtoI)
			throws ExceptionLP {
		try {
			angebotFac.updateAngebotOhneWeitereAktion(angebotDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AngebotDto angebotFindByPrimaryKey(Integer iIdAngebotI)
			throws ExceptionLP {
		AngebotDto angebotDto = null;

		try {
			angebotDto = angebotFac.angebotFindByPrimaryKey(iIdAngebotI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angebotDto;
	}

	public AngebotDto angebotFindByCNr(String cNr) throws ExceptionLP {
		try {
			return angebotFac.angebotFindByCNrMandantCNrOhneEx(cNr, LPMain
					.getTheClient().getMandant());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	
	public void aktiviereBelegControlled(Integer iid, Timestamp t) throws ExceptionLP {
		try {
			angebotFac.aktiviereBelegControlled(iid, t, LPMain.getTheClient());
			// SP1881
			DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
							LocaleFac.BELEGART_ANGEBOT, iid);
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
	}
	
	public Timestamp berechneBelegControlled(Integer iid) throws ExceptionLP {
		try {
			Timestamp t = angebotFac.berechneBelegControlled(iid, LPMain.getTheClient());
			// SP1881
			DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
							LocaleFac.BELEGART_ANGEBOT, iid);
			return t;
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	/**
	 * Berechne den Gesamtwert eines bestimmten Angebots in der
	 * Angebotswaehrung. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Positionen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gesamtwert des Angebots
	 */
	public BigDecimal berechneNettowertGesamt(Integer iIdAngebotI)
			throws ExceptionLP {
		BigDecimal nGesamtwert = null;

		try {
			nGesamtwert = angebotFac.berechneNettowertGesamt(iIdAngebotI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return nGesamtwert;
	}

	/**
	 * Methode zum Erzeugen eines neues Angebots als Kopie eines bestehenden
	 * Angebots. <br>
	 * Es werden auch die Positionen kopiert.
	 * 
	 * @param iIdAngebotI
	 *            PK des bestehenden Angebots
	 * @return Integer PK des neuen Angebots
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeAngebotAusAngebot(Integer iIdAngebotI)
			throws ExceptionLP {
		Integer iIdAngebot = null;

		try {
			iIdAngebot = angebotFac.erzeugeAngebotAusAngebot(iIdAngebotI,
					LPMain.getTheClient());

			if (iIdAngebot != null) {
				AngebotDto agDto = angebotFindByPrimaryKey(iIdAngebot);
				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(agDto.getKundeIIdAngebotsadresse());
			}

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdAngebot;
	}

	/**
	 * Methode zum Erzeugen eines Auftrags als Kopie eines bestehenden Angebots. <br>
	 * Es werden auch die Positionen kopiert.
	 * 
	 * @param iIdAngebotI
	 *            PK des bestehenden Angebots
	 * @return Integer PK des neuen Auftrags
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeAuftragAusAngebot(Integer iIdAngebotI,
			boolean bMitZeitDaten) throws ExceptionLP {
		Integer iIdAuftrag = null;

		try {
			iIdAuftrag = angebotFac.erzeugeAuftragAusAngebot(iIdAngebotI,
					bMitZeitDaten, LPMain.getTheClient());

			if (iIdAuftrag != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				AuftragDto abDto = DelegateFactory.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftrag);

				hmKunden.put(abDto.getKundeIIdAuftragsadresse(),
						abDto.getKundeIIdAuftragsadresse());
				if (!hmKunden.containsKey(abDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(abDto.getKundeIIdRechnungsadresse(),
							abDto.getKundeIIdRechnungsadresse());
				}

				if (!hmKunden.containsKey(abDto.getKundeIIdLieferadresse())) {
					hmKunden.put(abDto.getKundeIIdLieferadresse(),
							abDto.getKundeIIdLieferadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate()
							.pruefeKunde(it.next());
				}

			}

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdAuftrag;
	}

	/**
	 * Methode zum Erzeugen eines Lieferscheins aus einem bestehenden Angebot.
	 * 
	 * @param iIdAngebotI
	 *            PK des bestehenden Angebots
	 * @return Integer PK des neuen Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeLieferscheinAusAngebot(Integer iIdAngebotI)
			throws ExceptionLP {
		Integer lieferscheinIId = null;

		try {
			lieferscheinIId = angebotFac.erzeugeLieferscheinAusAngebot(
					iIdAngebotI, LPMain.getTheClient());

			if (lieferscheinIId != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				LieferscheinDto lsDto = DelegateFactory.getInstance()
						.getLsDelegate()
						.lieferscheinFindByPrimaryKey(lieferscheinIId);

				hmKunden.put(lsDto.getKundeIIdLieferadresse(),
						lsDto.getKundeIIdLieferadresse());

				if (!hmKunden.containsKey(lsDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(lsDto.getKundeIIdRechnungsadresse(),
							lsDto.getKundeIIdRechnungsadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate()
							.pruefeKunde(it.next());
				}

			}

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return lieferscheinIId;
	}

	/**
	 * Wenn die Zu- und Abschlaege in den Positionen geaendert wurden, dann
	 * werden im Anschluss die davon abhaengigen Werte neu berechnet.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAngebotKonditionen(Integer iIdAngebotI)
			throws ExceptionLP {
		try {
			angebotFac.updateAngebotKonditionen(iIdAngebotI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void setzeVersandzeitpunktAufJetzt(Integer iAngebotIId,
			String sDruckart) throws ExceptionLP {
		try {
			angebotFac.setzeVersandzeitpunktAufJetzt(iAngebotIId, sDruckart);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

}
