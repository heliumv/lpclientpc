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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>Delegate fuer Auftragpositionen.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Uli Walch, 27. 04. 2005</p>
 *
 * <p>@author Uli Walch</p>
 *
 * @version 1.0
 *
 * @version $Revision: 1.15 $ Date $Date: 2012/09/07 12:51:47 $
 */
public class AuftragpositionDelegate extends Delegate {
	private Context context;
	private AuftragpositionFac auftragpositionFac;

	public AuftragpositionDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			auftragpositionFac = (AuftragpositionFac) context
					.lookup("lpserver/AuftragpositionFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public void vertauscheAuftragpositionMinus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			// int pageCount = 0 ;

			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			while (--rowIndex >= 0) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			auftragpositionFac.vertauscheAuftragpositionenMinus(baseIId,
					iidList, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheAuftragpositionPlus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			int maxRowCount = tableModel.getRowCount();
			while (++rowIndex < maxRowCount) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			auftragpositionFac
					.vertauscheAuftragpositionenPlus(baseIId, iidList, LPMain.getTheClient());
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
	 * @param iIdAuftragI
	 *            der aktuelle Auftrag
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAuftragI, int iSortierungNeuePositionI)
			throws ExceptionLP {
		try {
			auftragpositionFac
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							iIdAuftragI, iSortierungNeuePositionI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Anzahl der Positionen zu einem Auftrag bestimmen.
	 * 
	 * @param iIdAuftragI
	 *            pk des Auftrags
	 * @return int Anzahl der Positionen
	 * @throws ExceptionLP
	 */
	public int getAnzahlMengenbehafteteAuftragpositionen(Integer iIdAuftragI)
			throws ExceptionLP {
		int anzahl = -1;

		try {
			anzahl = auftragpositionFac
					.getAnzahlMengenbehafteteAuftragpositionen(iIdAuftragI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return anzahl;
	}

	/**
	 * Anlegen einer neuen Auftragsposition.
	 * 
	 * @param pPos
	 *            die Daten der neuen Position
	 * @return Integer PK der neuen Position
	 * @throws ExceptionLP
	 */
	public Integer createAuftragposition(AuftragpositionDto pPos)
			throws ExceptionLP {
		Integer pkPosition = null;

		try {
			pkPosition = auftragpositionFac.createAuftragposition(pPos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return pkPosition;
	}

	public Integer createAuftragseriennrn(AuftragseriennrnDto pPos)
			throws ExceptionLP {
		Integer pkPosition = null;

		try {
			pkPosition = auftragpositionFac.createAuftragseriennrn(pPos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return pkPosition;
	}

	/**
	 * Holen einer Auftragsposition.
	 * 
	 * @param pKey
	 *            Integer
	 * @throws ExceptionLP
	 * @return AuftragpositionDto
	 */
	public AuftragpositionDto auftragpositionFindByPrimaryKey(Integer pKey)
			throws ExceptionLP {
		AuftragpositionDto dto = null;

		try {
			dto = this.auftragpositionFac.auftragpositionFindByPrimaryKey(pKey);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	public AuftragseriennrnDto auftragseriennrnFindByPrimaryKey(Integer pKey)
			throws ExceptionLP {
		AuftragseriennrnDto dto = null;

		try {
			dto = this.auftragpositionFac.auftragseriennrnFindByPrimaryKey(
					pKey, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	public AuftragseriennrnDto auftragseriennrnFindByAuftragpositionIId(
			Integer pKey) throws ExceptionLP {
		AuftragseriennrnDto dto = null;

		try {
			dto = auftragpositionFac.auftragseriennrnFindByAuftragpsotionIId(
					pKey, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	/**
	 * Aktualisieren einer bestehenden Auftragsposition.
	 * 
	 * @param pPos
	 *            AuftragpositionDto
	 * @throws ExceptionLP
	 */
	public void updateAuftragposition(AuftragpositionDto pPos)
			throws ExceptionLP {
		try {
			auftragpositionFac.updateAuftragposition(pPos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateAuftragseriennrn(AuftragseriennrnDto auftragseriennrnDto)
			throws ExceptionLP {
		try {
			auftragpositionFac.updateAuftragseriennrn(auftragseriennrnDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeAuftragseriennrn(AuftragseriennrnDto auftragseriennrnDto)
			throws ExceptionLP {
		try {
			auftragpositionFac.removeAuftragseriennrn(auftragseriennrnDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Aktualisieren einer bestehenden Auftragsposition.
	 * 
	 * @param pPos
	 *            AuftragpositionDto
	 * @throws ExceptionLP
	 */
	public void updateAuftragpositionOhneWeitereAktion(AuftragpositionDto pPos)
			throws ExceptionLP {
		try {
			auftragpositionFac.updateAuftragpositionOhneWeitereAktion(pPos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * L&ouml;schen einer bestehendenreat Auftragsposition.
	 * 
	 * @param pPos
	 *            AuftragpositionDto
	 * @throws ExceptionLP
	 */
	public void removeAuftragposition(AuftragpositionDto pPos)
			throws ExceptionLP {
		try {
			auftragpositionFac.removeAuftragposition(pPos,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Die Anzahl von Artikelpositionen in einem Auftrag berechnen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param sStatusI
	 *            der Status der gesuchten Positionen, wenn NULL alle
	 *            Artikelpositionen
	 * @throws ExceptionLP
	 *             Ausnahme
	 * @return int die Anzahl der Artikelpositionen mit diesem Status
	 */
	public int berechneAnzahlArtikelpositionenMitStatus(Integer iIdAuftragI,
			String sStatusI) throws ExceptionLP {
		int iAnzahlO = 0;

		try {
			iAnzahlO = auftragpositionFac
					.berechneAnzahlArtikelpositionenMitStatus(iIdAuftragI,
							sStatusI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iAnzahlO;
	}

	public void berechnePauschalposition(BigDecimal wert, Integer positionIId,
			Integer IId) throws ExceptionLP {
		try {
			auftragpositionFac.berechnePauschalposition(wert, positionIId, IId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Die Auftragposition an einer bestimmten Position im Auftrag bestimmen.
	 * 
	 * @param iIdAuftrag
	 *            PK des Auftrags
	 * @param iSort
	 *            die Position der Position
	 * @return AuftragpositionDto die Position
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public AuftragpositionDto auftragpositionFindByAuftragISort(
			Integer iIdAuftrag, Integer iSort) throws ExceptionLP {
		AuftragpositionDto oAuftragpositionDtoO = null;

		try {
			oAuftragpositionDtoO = auftragpositionFac
					.auftragpositionFindByAuftragISort(iIdAuftrag, iSort);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oAuftragpositionDtoO;
	}

	public void manuellErledigungAufgeben(Integer iIdPositionI)
			throws ExceptionLP {
		try {
			auftragpositionFac.manuellErledigungAufgeben(iIdPositionI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public String getNextSeriennr(Integer iIdArtikelI) throws ExceptionLP {
		String snr = null;
		try {
			snr = auftragpositionFac.getNextSeriennr(iIdArtikelI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return snr;
	}

	public void manuellErledigen(Integer iIdPositionI) throws ExceptionLP {
		try {
			auftragpositionFac.manuellErledigen(iIdPositionI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public String getSeriennummmern(Integer iIdAuftragpositionI)
			throws ExceptionLP {
		String snr = null;
		try {
			snr = auftragpositionFac.getSeriennummmern(iIdAuftragpositionI,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return snr;
	}

	/**
	 * Alle Auftragpositionen eines bestimmten Auftrags holen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return AuftragpositionDto[] die Positionen
	 * @throws ExceptionLP
	 *             Ausanahme
	 */
	public AuftragpositionDto[] auftragpositionFindByAuftrag(Integer iIdAuftragI)
			throws ExceptionLP {
		AuftragpositionDto[] aAuftragpositionDto = null;

		try {
			aAuftragpositionDto = auftragpositionFac
					.auftragpositionFindByAuftrag(iIdAuftragI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aAuftragpositionDto;
	}

	public Object[][] isAuftragseriennrnVorhanden(String[] cSerienNr,
			Integer artikelIId) throws ExceptionLP {
		Object[][] data = null;
		try {
			data = auftragpositionFac.isAuftragseriennrnVorhanden(cSerienNr,
					artikelIId, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}
		return data;
	}

	/**
	 * Zu einem bestimmten Auftrag alle Positionen holen, bei denen die offene
	 * Menge nicht NULL und nicht 0 ist.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return AuftragpositionDto[]
	 * @throws ExceptionLP
	 */
	public AuftragpositionDto[] auftragpositionFindByAuftragOffeneMenge(
			Integer iIdAuftragI) throws ExceptionLP {
		AuftragpositionDto[] aAuftragpositionDto = null;

		try {
			aAuftragpositionDto = auftragpositionFac
					.auftragpositionFindByAuftragOffeneMenge(iIdAuftragI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aAuftragpositionDto;
	}

	/**
	 * Alle Auftragpositionen suchen, die zu einem bestimmten Auftrag gehoeren
	 * und eine bestimmte Rahmenposition referenzieren.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param iIdRahmenpositionI
	 *            PK der Rahmenposition
	 * @return AuftragpositionDto die Liste der Positionen
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public AuftragpositionDto auftragpositionFindByAuftragIIdAuftragpositionIIdRahmenposition(
			Integer iIdAuftragI, Integer iIdRahmenpositionI) throws ExceptionLP {
		AuftragpositionDto auftragpositionDto = null;

		try {
			auftragpositionDto = auftragpositionFac
					.auftragpositionFindByAuftragIIdAuftragpositionIIdRahmenpositionOhneExc(
							iIdAuftragI, iIdRahmenpositionI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragpositionDto;
	}

	/**
	 * Alle Auftragpositionen suchen, die eine bestimmte Rahmenposition
	 * referenzieren.
	 * 
	 * @param iIdRahmenpositionII
	 *            PK der Rahmenposition
	 * @return AuftragpositionDto die Auftragposition, null moeglich
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public AuftragpositionDto[] auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
			Integer iIdRahmenpositionII) throws ExceptionLP {
		AuftragpositionDto[] aAuftragpositionDto = null;

		try {
			aAuftragpositionDto = auftragpositionFac
					.auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
							iIdRahmenpositionII, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aAuftragpositionDto;
	}

	public void pruefeAuftragseriennumern() throws ExceptionLP {
		try {
			auftragpositionFac.pruefeAuftragseriennumern(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer getMinISort(Integer iIdAuftragI) throws ExceptionLP {
		try {
			return auftragpositionFac.getMinISort(iIdAuftragI);
		} catch (Throwable e) {
			handleThrowable(e);
		}
		return 0;
	}

	/**
	 * Ermittelt die erf&uuml;llbare Menge/Anzahl eines Artikelsets. Es wird davon
	 * ausgegangen, dass die erste Position (positions[0]) den Kopfartikel
	 * enth&auml;lt und somit auch die Sollmenge (Satzgr&ouml;&szlig;e). Die noch offene Menge
	 * wird ber&uuml;cksichtigt.
	 * 
	 * @param positions
	 *            enth&auml;lt alle jene Auftragspositionen die f&uuml;r das ArtikelSet
	 *            relevant sind
	 * @param lagerIId
	 *            die Lager-IId von der die Ware entnommen werden soll.
	 * @param theClientDto
	 * @return die erfuellbare Menge fuer dieses Artikelset
	 * 
	 * @throws RemoteException
	 */
	public BigDecimal getErfuellbareMengeArtikelset(
			AuftragpositionDto[] positions, Integer lagerIId)
			throws ExceptionLP {
		try {
			return auftragpositionFac.getErfuellbareMengeArtikelset(positions,
					lagerIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return BigDecimal.ZERO;
	}

	public Double berechneArbeitszeitSoll(Integer iIdAuftragI)
			throws ExceptionLP {
		try {
			return auftragpositionFac.berechneArbeitszeitSoll(iIdAuftragI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return 0D;
	}

	/**
	 * Alle Artikelsets mit noch offenen Mengen liefern
	 * 
	 * @param positions
	 *            sind alle Auftragspositionen
	 * @param theClientDto
	 * @return eine Liste jene Artikelsets f&uuml;r die es noch offene Mengen im
	 *         Kopfartikel gibt. Es wird das komplette Artikelset des Auftrags
	 *         geliefert.
	 */
	public List<Artikelset> getOffeneAuftragpositionDtoMitArtikelset(
			AuftragpositionDto[] positions) throws ExceptionLP {
		try {
			return auftragpositionFac.getOffeneAuftragpositionDtoMitArtikelset(
					positions, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return new ArrayList<Artikelset>();
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
			return auftragpositionFac.getPositionIIdFromPositionNummer(
					rechnungIId, position);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public Integer getPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return auftragpositionFac.getPositionNummer(reposIId);
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
			return auftragpositionFac.getLastPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Die hoechste/letzte in einem Auftrag bestehende Positionsnummer ermitteln
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
			return auftragpositionFac.getHighestPositionNumber(rechnungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return 0;
	}

	/**
	 * Prueft, ob fuer alle Auftragspositionen zwischen den beiden angegebenen
	 * Positionsnummern der gleiche Mehrwertsteuersatz definiert ist.
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
			return auftragpositionFac.pruefeAufGleichenMwstSatz(rechnungIId,
					vonPositionNumber, bisPositionNumber);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return false;
	}

}
