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
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;


/**
 * <p><I>Diese Klasse kuemmert sich um die Angebotstklposition.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>12.07.05</I></p>
 * @author $Author: heidi $
 * @version $Revision: 1.6 $
 */
public class AngebotstklpositionDelegate
    extends Delegate
{
  private Context context;
  private AngebotstklpositionFac angebotstklpositionFac;

  public AngebotstklpositionDelegate()
      throws ExceptionLP {
    try {
    	context = new InitialContext();
    	angebotstklpositionFac = (AngebotstklpositionFac) context.lookup("lpserver/AngebotstklpositionFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }

  public void preiseGemaessKalkulationsartUpdaten(Integer agstklIId) throws ExceptionLP {
		try {
			angebotstklpositionFac
					.preiseGemaessKalkulationsartUpdaten(agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			
		}

	}

  public void kopiereAgstklPositionen(Integer stuecklisteIId_Quelle,
                                            Integer stuecklisteIId_Ziel)
      throws
      ExceptionLP {
    try {
      angebotstklpositionFac.kopiereAgstklPositionen(stuecklisteIId_Quelle,
          stuecklisteIId_Ziel, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }
  public String getPositionAsStringDocumentWS(Integer aIIdAngebotStklPosI[])
      throws ExceptionLP {

    String sRet = null;
    try {
      sRet = angebotstklpositionFac.getPositionAsStringDocumentWS(
          aIIdAngebotStklPosI, LPMain.getTheClient().getIDUser());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

    return sRet;
  }


  public void vertauscheAgstklpositionen(Integer iIdPosition1I,
                                         Integer iIdPosition2I)
      throws ExceptionLP {
    try {
      angebotstklpositionFac.vertauscheAgstklpositionen(
          iIdPosition1I,
          iIdPosition2I);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer agstklIId,
      int iSortierungNeuePositionI)
      throws ExceptionLP {
    try {
      angebotstklpositionFac.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
          agstklIId,
          iSortierungNeuePositionI);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  /**
   * Anlegen einer neuen Angebotstklposition.
   *
   * @param angebotstklpositionDtoI die neue Angebotstklposition
   * @return Integer PK der neuen Angebotstklposition
   * @throws ExceptionLP
   */
  public Integer createAgstklposition(AgstklpositionDto angebotstklpositionDtoI)
      throws ExceptionLP {
    Integer iIdAngebotstklposition = null;

    try {
      iIdAngebotstklposition = angebotstklpositionFac.createAgstklposition(
          angebotstklpositionDtoI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return iIdAngebotstklposition;
  }


  /**
   * Eine Angebotstklposition loeschen.
   * @param angebotstklpositionDtoI die Angebotstklposition
   * @throws ExceptionLP Ausnahme
   */
  public void removeAgstklposition(AgstklpositionDto angebotstklpositionDtoI)
      throws ExceptionLP {
    try {
      angebotstklpositionFac.removeAgstklposition(
          angebotstklpositionDtoI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  /**
   * Eine bestehende Angebotstklposition aktualisieren.
   * @param angebotstklpositionDtoI die Angebotstklposition
   * @throws ExceptionLP Ausnahme
   */
  public void updateAgstklposition(AgstklpositionDto angebotstklpositionDtoI)
      throws ExceptionLP {
    try {
      angebotstklpositionFac.updateAgstklposition(
          angebotstklpositionDtoI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public AgstklpositionDto agstklpositionFindByPrimaryKey(Integer iIdAngebotstklpositionI)
      throws ExceptionLP {
    AgstklpositionDto angebotstklpositionDto = null;

    try {
      angebotstklpositionDto = angebotstklpositionFac.agstklpositionFindByPrimaryKey(
          iIdAngebotstklpositionI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return angebotstklpositionDto;
  }


  public AgstklpositionDto[] agstklpositionFindByAgstklIIdBDruckenOhneExc(Integer
      iIdAngebotstkl, Short bDrucken)
      throws ExceptionLP {
    AgstklpositionDto[] angebotstklpositionDto = null;

    try {
      angebotstklpositionDto = angebotstklpositionFac.
          agstklpositionFindByAgstklIIdBDruckenOhneExc(
              iIdAngebotstkl, bDrucken,
              LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return angebotstklpositionDto;
  }


  /**
   * Das maximale iSort bei den Angebotstklpositionen fuer eine bestimmte
   * Angebotstkl bestimmen.
   * @param iIdAngebotstklI die aktuelle Angebotstkl
   * @return Integer das maximale iSort
   * @throws ExceptionLP Ausnahme
   */
  public Integer getMaxISort(Integer iIdAngebotstklI)
      throws ExceptionLP {
    Integer iiAnzahl = null;

    try {
      iiAnzahl = angebotstklpositionFac.getMaxISort(iIdAngebotstklI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return iiAnzahl;
  }


  /**
   * Zwei bestehende Angebotstklpositionen in Bezug auf ihr iSort umreihen.
   * @param iIdPosition1I PK der ersten Position
   * @param iIdPosition2I PK der zweiten Position
   * @throws ExceptionLP Ausnahme
   */
  /*public void vertauscheAngebotstklpositionen(Integer iIdPosition1I,
                                         Integer iIdPosition2I) throws ExceptionLP {
   try {
     angebotstklpositionFac.vertauscheAngebotstklpositionen(iIdPosition1I, iIdPosition2I, LPMain.getTheClient());
   }
   catch (Throwable t) {
     handleThrowable(t);
    }
     }*/

  /**
   * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
   * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
   * Datensatz.
   * <br>Diese Methode wird am Client aufgerufen, bevor die neue Position
   * abgespeichert wird.
   * @param iIdAngebotstklI PK der Angebotstkl
   * @param iSortierungNeuePositionI die Stelle, an der eingefuegt werden soll
   * @throws ExceptionLP Ausnahme
   */
  /*public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdAngebotstklI,
      int iSortierungNeuePositionI) throws ExceptionLP {
    try {
      angebotstklpositionFac.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(iIdAngebotstklI, iSortierungNeuePositionI, LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
     }*/

  /**
   * Berechnet die Anzahl der Positionen zu einer bestimmten Angebotstkl.
   * @param iIdAngebotstklI PK der Angebotstkl
   * @return int die Anzahl der Positonen
   * @throws ExceptionLP Ausnahme
   */
  /*public int getAnzahlMengenbehafteteAngebotstklpositionen(Integer iIdAngebotstklI) throws ExceptionLP {
    int iAnzahl = 0;

    try {
      iAnzahl = angebotstklpositionFac.getAnzahlMengenbehafteteAngebotstklpositionen(iIdAngebotstklI, LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return iAnzahl;
     }*/
}
