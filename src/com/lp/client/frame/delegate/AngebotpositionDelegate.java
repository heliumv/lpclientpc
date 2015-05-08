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
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Angebotposition.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>12.07.05</I>
 * </p>
 * 
 * @author $Author: Gerold $
 * @version $Revision: 1.11 $
 */
public class AngebotpositionDelegate extends Delegate {
	private Context context;
	private AngebotpositionFac angebotpositionFac;

	public AngebotpositionDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			angebotpositionFac = (AngebotpositionFac) context
					.lookup("lpserver/AngebotpositionFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Anlegen einer neuen Angebotposition.
	 * 
	 * @param angebotpositionDtoI
	 *            die neue Angebotposition
	 * @return Integer PK der neuen Angebotposition
	 * @throws ExceptionLP
	 */
	public Integer createAngebotposition(AngebotpositionDto angebotpositionDtoI)
			throws ExceptionLP {
		Integer iIdAngebotposition = null;

		try {
			iIdAngebotposition = angebotpositionFac.createAngebotposition(
					angebotpositionDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdAngebotposition;
	}

	/**
	 * Eine Angebotposition loeschen.
	 * 
	 * @param angebotpositionDtoI
	 *            die Angebotposition
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void removeAngebotposition(AngebotpositionDto angebotpositionDtoI)
			throws ExceptionLP {
		try {
			angebotpositionFac.removeAngebotposition(angebotpositionDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Eine bestehende Angebotposition aktualisieren.
	 * 
	 * @param angebotpositionDtoI
	 *            die Angebotposition
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateAngebotposition(AngebotpositionDto angebotpositionDtoI)
			throws ExceptionLP {
		try {
			angebotpositionFac.updateAngebotposition(angebotpositionDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AngebotpositionDto angebotpositionFindByPrimaryKey(
			Integer iIdAngebotpositionI) throws ExceptionLP {
		AngebotpositionDto angebotpositionDto = null;

		try {
			angebotpositionDto = angebotpositionFac
					.angebotpositionFindByPrimaryKey(iIdAngebotpositionI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angebotpositionDto;
	}

	public void vertauscheAngebotpositionMinus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			// int pageCount = 0 ;

			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			while (--rowIndex >= 0) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			angebotpositionFac.vertauscheAngebotpositionenMinus(baseIId, iidList, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheAngebotpositionPlus(Integer rowIndex,
			TableModel tableModel) throws ExceptionLP {
		try {
			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			int maxRowCount = tableModel.getRowCount();
			while (++rowIndex < maxRowCount) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			angebotpositionFac.vertauscheAngebotpositionenPlus(baseIId, iidList, LPMain.getTheClient());
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
	 * @param iIdAngebotI
	 *            PK der Angebot
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAngebotI, int iSortierungNeuePositionI)
			throws ExceptionLP {
		try {
			angebotpositionFac
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							iIdAngebotI, iSortierungNeuePositionI, LPMain
									.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Berechnet die Anzahl der Positionen zu einer bestimmten Angebot.
	 * 
	 * @param iIdAngebotI
	 *            PK der Angebot
	 * @return int die Anzahl der Positonen
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public int getAnzahlMengenbehafteteAngebotpositionen(Integer iIdAngebotI)
			throws ExceptionLP {
		int iAnzahl = 0;

		try {
			iAnzahl = angebotpositionFac
					.getAnzahlMengenbehafteteAngebotpositionen(iIdAngebotI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iAnzahl;
	}

	/**
	 * Wenn eine Position Endsumme in den Angebotpositionen enthalten ist, dann
	 * muss sie am Ende der preisbehafteten Positionen stehen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenInBezugAufEndsumme(Integer iIdAngebotI)
			throws ExceptionLP {
		try {
			angebotpositionFac.sortierungAnpassenInBezugAufEndsumme(
					iIdAngebotI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AngebotpositionDto angebotpositionFindByAngebotIIdAngebotpositionsartCNr(
			Integer iIdAngebotI, String positionsartCNrI) throws ExceptionLP {
		AngebotpositionDto angebotpositionDto = null;

		try {
			angebotpositionDto = angebotpositionFac
					.angebotpositionFindByAngebotIIdAngebotpositionsartCNr(
							iIdAngebotI, positionsartCNrI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return angebotpositionDto;
	}

	public AngebotpositionDto[] angebotpositionFindByAngebotIId(
			Integer iIdAngebotI) throws ExceptionLP {
		try {
			return angebotpositionFac.angebotpositionFindByAngebotIId(
					iIdAngebotI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	public AngebotpositionDto[] angebotpositionFindByAngebotIIdOhneAlternative(
			Integer iIdAngebotI) throws ExceptionLP {
		try {
			return angebotpositionFac.angebotpositionFindByAngebotIIdOhneAlternative(iIdAngebotI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void berechnePauschalposition(BigDecimal wert, Integer positionIId,
			Integer belegIId) throws ExceptionLP {
		try {
			angebotpositionFac.berechnePauschalposition(wert, positionIId,
					belegIId, LPMain.getTheClient());
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
	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId,
			Integer position) throws ExceptionLP {
		try {
			return angebotpositionFac.getPositionIIdFromPositionNummer(rechnungIId,
					position);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}
	
	public Integer getPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return angebotpositionFac.getPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}
		
	/**
	 * Liefert die Positionsnummer der angegebenen Position-IId.
	 * Sollte die Position selbst keine Nummer haben, wird die unmittelbar vor dieser
	 * Position liegende letztg&uuml;ltige Nummer geliefert.
	 * 
	 * @param reposIId
	 * @return Die Positionsnummer (1 - n), oder null wenn die Position nicht vorkommt.
	 */
	public Integer getLastPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return angebotpositionFac.getLastPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}
	
	/**
	 * Die hoechste/letzte in einem Auftrag bestehende Positionsnummer ermitteln
	 * 
	 * @param rechnungIId die RechnungsIId fuer die die hoechste Pos.Nummer ermittelt 
	 *   werden soll.
	 *   
	 * @return 0 ... n 
	 */
	public Integer getHighestPositionNumber(Integer rechnungIId) throws ExceptionLP {
		try {
			return angebotpositionFac.getHighestPositionNumber(rechnungIId) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
		
		return 0 ;
	}
	
	/**
	 * Prueft, ob fuer alle Auftragspositionen zwischen den beiden angegebenen Positionsnummern
	 * der gleiche Mehrwertsteuersatz definiert ist.
	 * 
	 * @param rechnungIId
	 * @param vonPositionNumber
	 * @param bisPositionNumber
	 * @return true wenn alle Positionen den gleichen Mehrwertsteuersatz haben.
	 * @throws EJBExceptionLP
	 */
	public boolean pruefeAufGleichenMwstSatz(
			Integer rechnungIId, Integer vonPositionNumber, Integer bisPositionNumber) throws ExceptionLP {
		try {
			return angebotpositionFac.pruefeAufGleichenMwstSatz(rechnungIId, vonPositionNumber, bisPositionNumber) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
	
		return false ;
	}
	
	public Double berechneArbeitszeitSoll( Integer iIdAngebotI) throws ExceptionLP {
		  try {
			  return angebotpositionFac.berechneArbeitszeitSoll(iIdAngebotI, LPMain.getTheClient()) ;
		  } catch(Throwable t) {
			  handleThrowable(t) ;
		  }

		  return 0D ;
	  }
	
}
