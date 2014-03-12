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


import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragteilnehmerDto;
import com.lp.server.auftrag.service.AuftragteilnehmerFac;

@SuppressWarnings("static-access") 
/**
 * <p>Delegate fuer Auftragteilnehmer.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Uli Walch, 27. 04. 2005</p>
 *
 * <p>@author Uli Walch</p>
 *
 * @version 1.0
 *
 * @version $Revision: 1.5 $ Date $Date: 2008/08/11 09:00:01 $
 */
public class AuftragteilnehmerDelegate
    extends Delegate
{

  private Context context;
  private AuftragteilnehmerFac auftragteilnehmerFac;

  public AuftragteilnehmerDelegate() throws ExceptionLP {
    try {
    	context = new InitialContext();
    	auftragteilnehmerFac = (AuftragteilnehmerFac) context.lookup("lpserver/AuftragteilnehmerFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

  }



  /**
   * Einen neuen Auftragsteilnehmer anlegen.
   * @param auftragteilnehmerDto die Daten des Teilnehmers
   * @return Integer PK des Teilnehmers
   * @throws ExceptionLP
   */
  public Integer createAuftragteilnehmer(AuftragteilnehmerDto
                                         auftragteilnehmerDto)
      throws
      ExceptionLP {
    Integer iIdTeilnehmerO = null;

    try {
      iIdTeilnehmerO = auftragteilnehmerFac.createAuftragteilnehmer(
          auftragteilnehmerDto,
          LPMain.getInstance().getTheClient().getIDUser());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

    return iIdTeilnehmerO;
  }


  /**
   * Einen Auftragteilnehmer von der der db loeschen.
   * @param auftragteilnehmerDto AuftragteilnehmerDto
   * @throws ExceptionLP
   */
  public void removeAuftragteilnehmer(AuftragteilnehmerDto auftragteilnehmerDto)
      throws
      ExceptionLP {
    try {
      auftragteilnehmerFac.removeAuftragteilnehmer(auftragteilnehmerDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  /**
   * Einen Auftragsteilnehmer aktualisieren.
   * @param auftragteilnehmerDto AuftragteilnehmerDto
   * @throws ExceptionLP
   */
  public void updateAuftragteilnehmer(AuftragteilnehmerDto auftragteilnehmerDto)
      throws
      ExceptionLP {
    try {
      auftragteilnehmerFac.updateAuftragteilnehmer(auftragteilnehmerDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  /**
   * Einen bestimmten Auftragteilnehmer ueber seinen Schluessel holen.
   * @param iId Integer
   * @throws ExceptionLP
   * @return AuftragteilnehmerDto
   */
  public AuftragteilnehmerDto auftragteilnehmerFindByPrimaryKey(Integer iId)
      throws
      ExceptionLP {
    AuftragteilnehmerDto teilnehmerDto = null;

    try {
      teilnehmerDto = auftragteilnehmerFac.auftragteilnehmerFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

    return teilnehmerDto;
  }


  /**
   * Feststellen, ob ein bestimmter Partner bereits in der Teilnehmerliste eines
   * bestimmten Auftrags enthalten ist.
   * @param iIdPartnerI PK des Partners
   * @param iIdAuftragI PK des Auftrags
   * @return boolean true, wenn der Partner in der Liste der Teilnehmer enthalten ist
   * @throws ExceptionLP Ausnahme
   */
  public boolean istPartnerEinAuftragteilnehmer(Integer iIdPartnerI,
                                                Integer iIdAuftragI)
      throws
      ExceptionLP {
    boolean bIstTeilnehmerO = false;

    try {
      bIstTeilnehmerO = auftragteilnehmerFac.istPartnerEinAuftragteilnehmer(
          iIdPartnerI, iIdAuftragI);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

    return bIstTeilnehmerO;
  }


  /**
   * Das maximale iSort bei den Teilnehmer fuer einen bestimmten
   * Auftrag bestimmen.
   * @param iIdAuftragI der aktuelle Auftrag
   * @return Integer das maximale iSort
   * @throws ExceptionLP Ausnahme
   */
  public Integer getMaxISort(Integer iIdAuftragI)
      throws ExceptionLP {
    Integer iiMaxISortO = null;

    try {
      iiMaxISortO = auftragteilnehmerFac.getMaxISort(iIdAuftragI);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

    return iiMaxISortO;
  }


  /**
   * Zwei bestehende Teilnehmer in Bezug auf ihr iSort umreihen.
   * @param iIdPosition1I PK der ersten Position
   * @param iIdPosition2I PK der zweiten Position
   * @throws ExceptionLP Ausnahme
   */
  public void vertauscheAuftragteilnehmer(Integer iIdPosition1I,
                                          Integer iIdPosition2I)
      throws ExceptionLP {
    try {
      auftragteilnehmerFac.vertauscheAuftragteilnehmer(iIdPosition1I, iIdPosition2I);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  /**
   * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
   * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
   * Datensatz.
   * <br>Diese Methode wird am Client aufgerufen, bevor die neue Position
   * abgespeichert wird.
   * @param iIdAuftragI der aktuelle Auftrag
   * @param iSortierungNeuePositionI die Stelle, an der eingefuegt werden soll
   * @throws ExceptionLP Ausnahme
   */
  public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdAuftragI,
      int iSortierungNeuePositionI)
      throws ExceptionLP {
    try {
      auftragteilnehmerFac.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
          iIdAuftragI, iSortierungNeuePositionI);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }
}
