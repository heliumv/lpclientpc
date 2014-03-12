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
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Delegate fuer Lieferscheinpositionen.
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
 * @version $Revision: 1.16 $ Date $Date: 2012/09/07 12:51:47 $
 */
public class LieferscheinpositionDelegate extends Delegate {
	private Context context;
	private LieferscheinpositionFac lieferscheinpositionFac;

	public LieferscheinpositionDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			lieferscheinpositionFac = (LieferscheinpositionFac) context
					.lookup("lpserver/LieferscheinpositionFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine neue Lieferscheinposition anlegen.
	 * 
	 * @param pDto
	 *            LieferscheinpositionDto
	 * @throws ExceptionLP
	 * @return Integer
	 */
	public Integer createLieferscheinposition(LieferscheinpositionDto pDto,
			boolean bArtikelSetAufloesen) throws ExceptionLP {
		Integer pkPosition = null;

		try {
			pkPosition = lieferscheinpositionFac.createLieferscheinposition(
					pDto, bArtikelSetAufloesen, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return pkPosition;
	}

	public Integer createLieferscheinposition(LieferscheinpositionDto pDto,
			List<SeriennrChargennrMitMengeDto> snrs,
			boolean bArtikelSetAufloesen) throws ExceptionLP {
		Integer pkPosition = null;

		try {
			pkPosition = lieferscheinpositionFac.createLieferscheinposition(
					pDto, bArtikelSetAufloesen, snrs, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return pkPosition;
	}

	public Integer updateLieferscheinposition(
			LieferscheinpositionDto oLieferscheinpositionDtoI)
			throws ExceptionLP {
		Integer iIdLieferscheinposition = null;
		try {
			iIdLieferscheinposition = lieferscheinpositionFac
					.updateLieferscheinposition(oLieferscheinpositionDtoI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iIdLieferscheinposition;
	}

	public Integer updateLieferscheinposition(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities) throws ExceptionLP {
		Integer iIdLieferscheinposition = null;
		try {
			iIdLieferscheinposition = lieferscheinpositionFac
					.updateLieferscheinposition(oLieferscheinpositionDtoI,
							identities, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdLieferscheinposition;
	}

	public void updateLieferscheinpositionOhneWeitereAktion(
			LieferscheinpositionDto lieferscheinpositionDto) throws ExceptionLP {
		try {
			lieferscheinpositionFac
					.updateLieferscheinpositionOhneWeitereAktion(
							lieferscheinpositionDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLieferscheinpositionSichtAuftrag(
			LieferscheinpositionDto lieferscheinpositionDto) throws ExceptionLP {
		try {
			lieferscheinpositionFac.updateLieferscheinpositionSichtAuftrag(
					lieferscheinpositionDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void lieferscheinpositionKeinLieferrestEintragen(
			Integer lieferscheinpositionIId, boolean bKeinLieferrest)
			throws ExceptionLP {
		try {
			lieferscheinpositionFac
					.lieferscheinpositionKeinLieferrestEintragen(
							lieferscheinpositionIId, bKeinLieferrest, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine Lieferscheinposition von der db entfernen.
	 * 
	 * @param pPos
	 *            LieferscheinpositionDto
	 * @throws ExceptionLP
	 */
	public void removeLieferscheinposition(LieferscheinpositionDto pPos)
			throws ExceptionLP {
		try {
			lieferscheinpositionFac.removeLieferscheinposition(pPos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeLieferscheinpositionen(Object[] ids) throws ExceptionLP {
		try {
			lieferscheinpositionFac.removeLieferscheinpositionen(ids,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine bestimmte Lieferscheinposition ueber ihren pk holen.
	 * 
	 * @param iIdLieferscheinpositionI
	 *            PK der Position
	 * @throws ExceptionLP
	 * @return LieferscheinpositionDto die Position, null wenn es keine gibt
	 */
	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKey(
			Integer iIdLieferscheinpositionI) throws ExceptionLP {
		LieferscheinpositionDto oLieferscheinpositionDtoO = null;

		try {
			oLieferscheinpositionDtoO = lieferscheinpositionFac
					.lieferscheinpositionFindByPrimaryKey(
							iIdLieferscheinpositionI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oLieferscheinpositionDtoO;
	}

	
	public LieferscheinpositionDto[] lieferscheinpositionFindByLieferscheinIId(
			Integer iIdLieferscheinI) throws ExceptionLP {
		LieferscheinpositionDto[] oLieferscheinpositionDtoO = null;

		try {
			oLieferscheinpositionDtoO = lieferscheinpositionFac
					.lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oLieferscheinpositionDtoO;
	}

	
	/**
	 * Anzahl der Positionen eines Lieferscheins bestimmen.
	 * 
	 * @param iIdLieferscheinI
	 *            pk des Lieferscheins
	 * @throws ExceptionLP
	 * @return int Anzahl der Positionen
	 */
	public int berechneAnzahlMengenbehaftetePositionen(Integer iIdLieferscheinI)
			throws ExceptionLP {
		int iAnzahlO = -1;

		try {
			iAnzahlO = lieferscheinpositionFac
					.berechneAnzahlMengenbehaftetePositionen(iIdLieferscheinI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iAnzahlO;
	}

	/**
	 * Die Anzahl von Artikelpositionen in einem Lieferschein berechnen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return int die Anzahl der Artikelpositionen mit diesem Status
	 */
	public int berechneAnzahlArtikelpositionen(Integer iIdLieferscheinI)
			throws ExceptionLP {
		int iAnzahlO = -1;

		try {
			iAnzahlO = lieferscheinpositionFac
					.berechneAnzahlArtikelpositionen(iIdLieferscheinI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iAnzahlO;
	}

	public void vertauscheLieferscheinpositionMinus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			// int pageCount = 0 ;

			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			while (--rowIndex >= 0) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			lieferscheinpositionFac.vertauscheLieferscheinpositionenMinus(
					baseIId, iidList, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheLieferscheinpositionPlus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			int maxRowCount = tableModel.getRowCount();
			while (++rowIndex < maxRowCount) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			lieferscheinpositionFac.vertauscheLieferscheinpositionenPlus(
					baseIId, iidList, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void preiseAusAuftragspositionenUebernehmen(Integer auftragIId)
			throws ExceptionLP {
		try {
			lieferscheinpositionFac.preiseAusAuftragspositionenUebernehmen(
					auftragIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdLieferscheinI
	 *            der aktuelle Lieferschein
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdLieferscheinI, int iSortierungNeuePositionI)
			throws ExceptionLP {
		try {
			lieferscheinpositionFac
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							iIdLieferscheinI, iSortierungNeuePositionI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Der Kupferzuschlag einer Position kann von aussen gesetzt werden (z.B.
	 * durch die Rechnung). <br>
	 * Das Setzen des Kupferzuschlags zieht die folgenden Schritte nach sich: <br>
	 * - Die Werte der Lieferscheinposition werden neu berechnet und in der DB
	 * gespeichert <br>
	 * - Der Gesamtwert des Lieferscheins wird neu berechnet und in der DB
	 * gespeichert
	 * 
	 * @param iIdLieferscheinpositionI
	 *            PK der Lieferscheinposition
	 * @param ddKupferzuschlagI
	 *            der Kupferzuschlag in Prozent, z.B. 30
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void setzeKupferzuschlag(Integer iIdLieferscheinpositionI,
			Double ddKupferzuschlagI) throws ExceptionLP {
		try {
			lieferscheinpositionFac.setzeKupferzuschlag(
					iIdLieferscheinpositionI, ddKupferzuschlagI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLieferscheinpositionAusRechnung(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			Integer rechnungpositionIId) throws ExceptionLP {
		try {
			lieferscheinpositionFac.updateLieferscheinpositionAusRechnung(
					oLieferscheinpositionDtoI, rechnungpositionIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLieferscheinpositionAusRechnung(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			Integer rechnungpositionIId, List<SeriennrChargennrMitMengeDto> snrs)
			throws ExceptionLP {
		try {
			lieferscheinpositionFac.updateLieferscheinpositionAusRechnung(
					oLieferscheinpositionDtoI, rechnungpositionIId, snrs,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LieferscheinpositionDto[] getLieferscheinPositionenByLieferschein(
			Integer iIdLieferscheinI) throws ExceptionLP {
		LieferscheinpositionDto[] lsPos = null;

		try {
			lsPos = lieferscheinpositionFac
					.getLieferscheinPositionenByLieferschein(iIdLieferscheinI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return lsPos;
	}

	public void berechnePauschalposition(BigDecimal wert, Integer positionIId,
			Integer belegIId) throws ExceptionLP {
		try {
			lieferscheinpositionFac.berechnePauschalposition(wert, positionIId,
					belegIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void pruefeVKPreisAufLagerbewegung() throws ExceptionLP {
		try {
			lieferscheinpositionFac.pruefeVKPreisAufLagerbewegung(LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param rechnungIId
	 * @param position
	 *            die Positionsnummer f&uuml;r die die IId ermittelt werden soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getLSPositionIIdFromPositionNummer(Integer rechnungIId,
			Integer position) throws ExceptionLP {
		try {
			return lieferscheinpositionFac.getLSPositionIIdFromPositionNummer(
					rechnungIId, position);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public Integer getLSPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return lieferscheinpositionFac.getLSPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Liefert die Positionsnummer der angegebenen Position-IId. Sollte die
	 * Position selbst keine Nummer haben, wird die unmittelbar vor dieser
	 * Position liegende letztg&uuml;ltige Nummer geliefert.
	 * 
	 * @param reposIId
	 * @return Die Positionsnummer (1 - n), oder null wenn die Position nicht
	 *         vorkommt.
	 */
	public Integer getLSLastPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return lieferscheinpositionFac.getLSLastPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Die hoechste/letzte in einem Lieferschein bestehende Positionsnummer
	 * ermitteln
	 * 
	 * @param rechnungIId
	 *            die RechnungsIId fuer die die hoechste Pos.Nummer ermittelt
	 *            werden soll.
	 * 
	 * @return 0 ... n
	 */
	public Integer getLSHighestPositionNumber(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return lieferscheinpositionFac
					.getLSHighestPositionNumber(rechnungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return 0;
	}

	/**
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param rechnungIId
	 * @param position
	 *            die Positionsnummer f&uuml;r die die IId ermittelt werden soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId,
			Integer position) throws ExceptionLP {
		try {
			return lieferscheinpositionFac.getPositionIIdFromPositionNummer(
					rechnungIId, position);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public Integer getPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return lieferscheinpositionFac.getPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Liefert die Positionsnummer der angegebenen Position-IId. Sollte die
	 * Position selbst keine Nummer haben, wird die unmittelbar vor dieser
	 * Position liegende letztg&uuml;ltige Nummer geliefert.
	 * 
	 * @param reposIId
	 * @return Die Positionsnummer (1 - n), oder null wenn die Position nicht
	 *         vorkommt.
	 */
	public Integer getLastPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return lieferscheinpositionFac.getLastPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Die hoechste/letzte in einem Lieferschein bestehende Positionsnummer
	 * ermitteln
	 * 
	 * @param rechnungIId
	 *            die RechnungsIId fuer die die hoechste Pos.Nummer ermittelt
	 *            werden soll.
	 * 
	 * @return 0 ... n
	 */
	public Integer getHighestPositionNumber(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return lieferscheinpositionFac
					.getHighestPositionNumber(rechnungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return 0;
	}

	/**
	 * Prueft, ob fuer alle Lieferscheinpositionen zwischen den beiden
	 * angegebenen Positionsnummern der gleiche Mehrwertsteuersatz definiert
	 * ist.
	 * 
	 * @param rechnungIId
	 * @param vonPositionNumber
	 * @param bisPositionNumber
	 * @return true wenn alle Positionen den gleichen Mehrwertsteuersatz haben.
	 * @throws EJBExceptionLP
	 */
	public boolean pruefeAufGleichenMwstSatz(Integer rechnungIId,
			Integer vonPositionNumber, Integer bisPositionNumber)
			throws ExceptionLP {
		try {
			return lieferscheinpositionFac.pruefeAufGleichenMwstSatz(
					rechnungIId, vonPositionNumber, bisPositionNumber);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return false;
	}

	public List<SeriennrChargennrMitMengeDto> getSeriennrchargennrForArtikelsetPosition(
			Integer lieferscheinposIId) throws ExceptionLP {
		try {
			return lieferscheinpositionFac
					.getSeriennrchargennrForArtikelsetPosition(lieferscheinposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return new ArrayList<SeriennrChargennrMitMengeDto>();
	}
}
