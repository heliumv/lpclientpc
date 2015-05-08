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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.service.Artikelset;

/**
 * <p>
 * Delegate fuer Lieferscheine.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Uli Walch, 27. 04. 2005
 * </p>
 * 
 * <p>
 * 
 * @author Uli Walch
 *         </p>
 * 
 * @version 1.0
 * 
 * @version $Revision: 1.14 $ Date $Date: 2012/09/07 12:51:47 $
 */
public class LieferscheinDelegate extends Delegate {
	private Context context = null;
	private LieferscheinFac lsFac = null;

	public LieferscheinDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			lsFac = (LieferscheinFac) context
					.lookup("lpserver/LieferscheinFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Lieferscheinarten in Uebesetzung holen.
	 * 
	 * @param locKunde
	 *            Locale
	 * @throws ExceptionLP
	 * @return KeyValueDto[]
	 */
	public Map<?, ?> getLieferscheinart(Locale locKunde) throws ExceptionLP {
		Map<?, ?> artenMap = null;

		try {
			artenMap = this.lsFac.getLieferscheinart(locKunde, LPMain
					.getInstance().getUISprLocale());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return artenMap;
	}

	/**
	 * Lieferscheinpositionsarten in Uebersetzung holen.
	 * 
	 * @param locKunde
	 *            Locale
	 * @throws ExceptionLP
	 * @return KeyValueDto[]
	 */
	public Map<?, ?> getLieferscheinpositionart(Locale locKunde)
			throws ExceptionLP {
		Map<?, ?> artenMap = null;

		try {
			artenMap = this.lsFac.getLieferscheinpositionart(locKunde, LPMain
					.getInstance().getUISprLocale());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return artenMap;
	}

	/**
	 * Lieferscheinstatus in Uebersetzung holen.
	 * 
	 * @param pConstant
	 *            String
	 * @param locKunde
	 *            Locale
	 * @throws ExceptionLP
	 * @return String
	 */
	public String getLieferscheinstatus(String pConstant, Locale locKunde)
			throws ExceptionLP {
		String status = null;

		try {
			status = this.lsFac.getLieferscheinstatus(pConstant, locKunde,
					LPMain.getInstance().getUISprLocale());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return status;
	}

	/**
	 * Anlegen eines neuen Lieferscheinkopfs in der DB. <br>
	 * Ein Lieferschein kann zu mehreren Auftraegen liefern. <br>
	 * Der Bezug zu einem Auftrag wird in einer Kreuztabelle eingetragen.
	 * 
	 * @param lieferscheinDtoI
	 *            die Daten des Lieferscheins
	 * @throws ExceptionLP
	 * @return Integer PK des neuen Lieferscheins
	 */
	public Integer createLieferschein(LieferscheinDto lieferscheinDtoI)
			throws ExceptionLP {
		Integer pkLieferschein = null;

		try {
			pkLieferschein = this.lsFac.createLieferschein(lieferscheinDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return pkLieferschein;
	}

	/**
	 * Einen bestehenden Lieferschein aktualisieren. <br>
	 * Ein Lieferschein kann zu mehreren Auftraegen liefern. <br>
	 * Der Bezug zu einem Auftrag wird in einer Kreuztabelle eingetragen.
	 * 
	 * @param lieferscheinDtoI
	 *            die Daten des Lieferscheins
	 * @param aAuftragIIdI
	 *            der Bezug zu 0..n Auftraegen
	 * @param waehrungOriCNrI
	 *            die urspruengliche Belegwaehrung
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public boolean updateLieferschein(LieferscheinDto lieferscheinDtoI,
			String waehrungOriCNrI, Integer[] aAuftragIIdI) throws ExceptionLP {
		try {
			return lsFac.updateLieferschein(lieferscheinDtoI, aAuftragIIdI,
					waehrungOriCNrI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	/**
	 * Einen bestehenden Lieferschein ohne weitere Aktion aktualisieren. <br>
	 * Ein eventuelle Bezug zu Auftraegen bleibt dabei unberuecksichtig.
	 * 
	 * @param lieferscheinDtoI
	 *            der aktuelle Lieferschein
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateLieferscheinOhneWeitereAktion(
			LieferscheinDto lieferscheinDtoI) throws ExceptionLP {
		try {
			lsFac.updateLieferscheinOhneWeitereAktion(lieferscheinDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Einen bestehenden Lieferschein aktualisieren. <br>
	 * Ein eventuelle Bezug zu Auftraegen bleibt dabei unberuecksichtig.
	 * 
	 * @param lieferscheinDtoI
	 *            der aktuelle Lieferschein
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateLieferschein(LieferscheinDto lieferscheinDtoI)
			throws ExceptionLP {
		try {
			lsFac.updateLieferschein(lieferscheinDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLieferscheinBegruendung(Integer lieferscheinIId,
			Integer begruendungIId) throws ExceptionLP {
		try {
			lsFac.updateLieferscheinBegruendung(lieferscheinIId,
					begruendungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LieferscheinDto lieferscheinFindByCNr(String cNr) throws ExceptionLP {
		try {
			return lsFac.lieferscheinFindByCNrMandantCNr(cNr, LPMain
					.getTheClient().getMandant());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public LieferscheinDto lieferscheinFindByPrimaryKey(Integer iIdLieferscheinI)
			throws ExceptionLP {
		LieferscheinDto lsDto = null;

		try {
			lsDto = lsFac.lieferscheinFindByPrimaryKey(iIdLieferscheinI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return lsDto;
	}

	public String getLieferscheinCNr(Object[] keys) throws ExceptionLP {
		try {
			return lsFac.getLieferscheinCNr(keys);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	/**
	 * Aus einem Lieferschein jene Position heraussuchen, die zu einer
	 * bestimmten Auftragposition gehoert.
	 * 
	 * @param iIdLieferscheinI
	 *            Integer
	 * @param iIdAuftragpositionI
	 *            Integer
	 * @throws ExceptionLP
	 * @return LieferscheinpositionDto
	 */
	public LieferscheinpositionDto getLieferscheinpositionByLieferscheinAuftragposition(
			Integer iIdLieferscheinI, Integer iIdAuftragpositionI)
			throws ExceptionLP {
		LieferscheinpositionDto oLieferscheinpositionDto = null;

		try {
			oLieferscheinpositionDto = lsFac
					.getLieferscheinpositionByLieferscheinAuftragposition(
							iIdLieferscheinI, iIdAuftragpositionI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oLieferscheinpositionDto;
	}

	/**
	 * Den Status eines Lieferscheins aendern.
	 * 
	 * @param iIdLieferscheinI
	 *            pk des Lieferscheins
	 * @param sStatusI
	 *            der neue Status
	 * @param iIdRechnungI
	 *            PK der verrechnenden Rechnung
	 * @throws ExceptionLP
	 */
	public void setzeStatusLieferschein(Integer iIdLieferscheinI,
			String sStatusI, Integer iIdRechnungI) throws ExceptionLP {
		try {
			lsFac.setzeStatusLieferschein(iIdLieferscheinI, sStatusI,
					iIdRechnungI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Berechnung des Gestehungswerts eines Lieferscheins. <br>
	 * Der Gestehungswert ist die Summe ueber die Gestehungswerte der
	 * enthaltenen Artikelpositionen. <br>
	 * Der Gestehungswert einer Artikelposition errechnet sich aus Menge x
	 * positionsbezogenem Gestehungspreis des enthaltenen Artikels.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gestehungswert in der gewuenschten Waehrung
	 */
	public BigDecimal berechneGestehungswert(Integer iIdLieferscheinI)
			throws ExceptionLP {
		BigDecimal wert = null;

		try {
			wert = lsFac.berechneGestehungswert(iIdLieferscheinI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return wert;
	}

	/**
	 * Berechne den Gesamtwert eines bestimmten Lieferscheins in einer
	 * bestimmten Waherung. <br>
	 * Der Gesamtwert berechnet sich aus
	 * <p>
	 * Summe der Nettogesamtpreise der Positionen <br>
	 * + Versteckter Aufschlag <br>
	 * - Allgemeiner Rabatt
	 * 
	 * @param iIdLieferscheinI
	 *            pk des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gesamtwert des Lieferscheins
	 */
	public BigDecimal berechneGesamtwertLieferschein(Integer iIdLieferscheinI)
			throws ExceptionLP {
		BigDecimal wert = null;

		try {
			wert = lsFac.berechneGesamtwertLieferschein(iIdLieferscheinI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return wert;
	}

	public BigDecimal berechneOffenenLieferscheinwert(Integer kundeIId)
			throws ExceptionLP {
		BigDecimal wert = null;

		try {
			wert = lsFac.berechneOffenenLieferscheinwert(kundeIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return wert;
	}

	/**
	 * Die Anzahl der Lieferscheine mit Status Offen oder Geliefert.
	 * 
	 * @return int die Anzahl der offenen Lieferscheine
	 * @throws ExceptionLP
	 */
	public int berechneAnzahlDerOffenenLieferscheine() throws ExceptionLP {
		int iAnzahlO = 0;

		try {
			iAnzahlO = lsFac.berechneAnzahlDerOffenenLieferscheine();
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iAnzahlO;
	}

	/**
	 * Wenn die Konditionen im Lieferschein geaendert werden, muessen die Werte
	 * der einzelnen Positionen und die Gesamtwerte des Lieferscheins angepasst
	 * werden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferschein
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateLieferscheinKonditionen(Integer iIdLieferscheinI)
			throws ExceptionLP {
		try {
			lsFac.updateLieferscheinKonditionen(iIdLieferscheinI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Ein Lieferschein kann manuell freigegeben werden. <br>
	 * Der Lieferschein muss sich im Status 'Angelegt' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void manuellFreigeben(Integer iIdLieferscheinI) throws ExceptionLP {
		try {
			lsFac.manuellFreigeben(iIdLieferscheinI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Ein Lieferschein kann manuell erledigt werden. <br>
	 * Der Lieferschein muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void manuellErledigen(Integer iIdLieferscheinI) throws ExceptionLP {
		try {
			lsFac.manuellErledigen(iIdLieferscheinI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Die Erledigung eines Lieferscheins kann aufgehoben werden. <br>
	 * Der Lieferschein muss sich im Status 'Erledigt' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void erledigungAufheben(Integer iIdLieferscheinI) throws ExceptionLP {
		try {
			lsFac.erledigungAufheben(iIdLieferscheinI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Ein Lieferschein kann durch eine Rechnung verrechnet werden. <br>
	 * Der Lieferschein muss sich im Status 'Geliefert' befinden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param iIdRechnungI
	 *            PK der verrechnenden Rechnung
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void verrechnen(Integer iIdLieferscheinI, Integer iIdRechnungI)
			throws ExceptionLP {
		try {
			lsFac.verrechnen(iIdLieferscheinI, iIdRechnungI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Einen Lieferschein stornieren. <br>
	 * Das bedeutet: - Den Status des Lieferscheins anpassen und die Stornodaten
	 * vermerken. - Die Lieferscheinpositionen loeschen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void stornieren(Integer iIdLieferscheinI) throws ExceptionLP {
		try {
			lsFac.stornieren(iIdLieferscheinI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void toggleZollexportpapiereErhalten(Integer lieferscheinIId,
			String cZollexportpapier, Integer eingangsrechnungIId_Zollexport)
			throws ExceptionLP {
		try {
			lsFac.toggleZollexportpapiereErhalten(lieferscheinIId,
					cZollexportpapier, eingangsrechnungIId_Zollexport,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void aktiviereBelegControlled(Integer iid, Timestamp t) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = lsFac.aktiviereBelegControlled(iid, t, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto) ;
			// SP1881
//			DelegateFactory
//					.getInstance()
//					.getSystemDelegate()
//					.enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
//							LocaleFac.BELEGART_LIEFERSCHEIN, iid);
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
	}
	
	public Timestamp berechneBelegControlled(Integer iid) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = lsFac.berechneBelegControlled(iid,
					LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto) ;
			return pruefungDto.getBerechnungsZeitpunkt() ;
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public Timestamp berechneAktiviereBelegControlled(Integer iid) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = lsFac.berechneAktiviereBelegControlled(iid, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto) ;
			return pruefungDto.getBerechnungsZeitpunkt() ;
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}
	
	/**
	 * Bei einem auftragbezogenen Lieferschein ist es moeglich, all jene offenen
	 * oder teilerledigten Auftragpositionen innerhalb einer Transaktion zu
	 * uebernehmen, die keine Benutzerinteraktion benoetigen. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Handeingabepositionen werden uebernommen.
	 * <li>Nicht Serien- oder Chargennummertragende Artikelpositionen werden mit
	 * jener Menge uebernommen, die auf Lager liegt.
	 * <li>Artikelpositionen mit Seriennummer werden nicht uebernommen.
	 * <li>Artikelpositionen mit Chargennummer werden mit jener Menge
	 * uebernommen, die auf Lager liegt, wenn es genau eine Charge gibt.
	 * </ul>
	 * Die restlichen Positionen koennen nicht automatisch uebernommen werden.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param iIdAuftragI
	 *            Integer
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
//	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
//			Integer iIdLieferscheinI, Integer iIdAuftragI) throws ExceptionLP {
//		try {
//			lsFac.uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
//					iIdLieferscheinI, iIdAuftragI, LPMain.getTheClient());
//		} catch (Throwable t) {
//			handleThrowable(t);
//		}
//	}

	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
			Integer iIdLieferscheinI, Integer iIdAuftrag,
			List<Artikelset> artikelsets) throws ExceptionLP {
		try {
			lsFac.uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
					iIdLieferscheinI, iIdAuftrag, artikelsets,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void setzeVersandzeitpunktAufJetzt(Integer iLieferscheinIId,
			String sDruckart) throws ExceptionLP {
		try {
			lsFac.setzeVersandzeitpunktAufJetzt(iLieferscheinIId, sDruckart);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public boolean hatLieferscheinVersandweg(LieferscheinDto lieferscheinDto)
			throws ExceptionLP {
		try {
			return lsFac.hatLieferscheinVersandweg(lieferscheinDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return false;
	}

	public String createLieferscheinAvisoPost(Integer lieferscheinIId)
			throws ExceptionLP {
		try {
			return lsFac.createLieferscheinAvisoPost(lieferscheinIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return "";
	}

	public void resetLieferscheinAviso(Integer lieferscheinIId)
			throws ExceptionLP {
		try {
			lsFac.resetLieferscheinAviso(lieferscheinIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void repairLieferscheinZws2276(Integer lieferscheinId) throws ExceptionLP {
		try {
			lsFac.repairLieferscheinZws2276(lieferscheinId, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}	

	public List<Integer> repairLieferscheinZws2276GetList() throws ExceptionLP {
		try {
			return lsFac.repairLieferscheinZws2276GetList(LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t);
		}
		
		return new ArrayList<Integer>() ;
	}	
}
