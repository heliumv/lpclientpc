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
import com.lp.server.auftrag.service.AuftragRahmenAbrufFac;
import com.lp.server.auftrag.service.AuftragpositionDto;


/**
 * <p>Delegate fuer die Rahmen- und Abruffunktionalitaet im Auftrag.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Uli Walch, 11.11.05</p>
 *
 * <p>@author Uli Walch</p>
 *
 * @version 1.0
 *
 * @version $Revision: 1.5 $ Date $Date: 2009/01/13 12:20:16 $
 */
public class AuftragRahmenAbrufDelegate
    extends Delegate
{
  private Context context;
  private AuftragRahmenAbrufFac auftragRahmenAbrufFac;

  public AuftragRahmenAbrufDelegate() throws ExceptionLP{
    try {
    	context = new InitialContext();
    	auftragRahmenAbrufFac = (AuftragRahmenAbrufFac) context.lookup("lpserver/AuftragRahmenAbrufFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  /**
   * Wenn eine neue Abrufposition angelegt wird, muss die entsprechende
   * Rahmenposition angepasst werden.
   * @param abrufpositionDtoI die aktuelle Abrufposition
   * @return Integer PK der neuen Abrufposition
   * @throws ExceptionLP Ausnahme
   */
  public Integer createAbrufpositionZuRahmenposition(AuftragpositionDto abrufpositionDtoI)
      throws ExceptionLP {
    Integer abrufpositionIId = null;

    try {
      abrufpositionIId = auftragRahmenAbrufFac.createAbrufpositionZuRahmenposition(
          abrufpositionDtoI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return abrufpositionIId;
  }

  /**
   * Wenn eine Abrufposition geloescht wird, muss die entsprechende Rahmenposition
   * angepasst werden.
   * @param abrufpositionDtoI die aktuelle Abrufbestellposition
   * @throws ExceptionLP Ausnahme
   */
  public void removeAbrufpositionZuRahmenposition(AuftragpositionDto abrufpositionDtoI)
      throws ExceptionLP {
    try {
      auftragRahmenAbrufFac.removeAbrufpositionZuRahmenposition(
          abrufpositionDtoI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }

  /**
   * Eine Abrufbestellposition wird korrigiert. Damit mussen auch die Mengen in
   * des Rahmenauftrags angepasst werden.
   * @param abrufpositionDtoI die Abrufposition mit den aktuellen Werten
   * @throws ExceptionLP Ausnahme
   */
  public void updateAbrufpositionZuRahmenpositionSichtRahmen(AuftragpositionDto abrufpositionDtoI)
      throws ExceptionLP {
    try {
      auftragRahmenAbrufFac.updateAbrufpositionZuRahmenpositionSichtRahmen(
          abrufpositionDtoI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }

  /**
   * Eine Abrufbestellposition wird korrigiert. Damit mussen auch die Mengen in
   * des Rahmenauftrags angepasst werden.
   * @param abrufpositionDtoI die Abrufposition mit den aktuellen Werten
   * @throws ExceptionLP Ausnahme
   */
  public void updateAbrufpositionZuRahmenposition(AuftragpositionDto abrufpositionDtoI)
      throws ExceptionLP {
    try {
      auftragRahmenAbrufFac.updateAbrufpositionZuRahmenposition(
          abrufpositionDtoI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }

  /**
   * Jedes Mal, wenn eine Abrufposition zu einer Rahmenposition erfasst wird,
   * mu&szlig; danach der Status des Rahmenauftrags geprueft werden und ev. geaendert werden.
   * @param iIdRahmenauftragI PK des Rahmenauftrags
   * @throws ExceptionLP Ausnahme
   */
  public void pruefeUndSetzeRahmenstatus(Integer iIdRahmenauftragI) throws ExceptionLP {
    try {
      auftragRahmenAbrufFac.pruefeUndSetzeRahmenstatus(
          iIdRahmenauftragI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }

  /**
   * Aufgrund des Divisors die Abrufpositionen fuer einen Abrufauftrag erzeugen.
   * @param iIdAuftragI PK des Abrufauftrags
   * @param iDivisorI der Divisor
   * @throws ExceptionLP Ausnahme
   */
  public void erzeugeAbrufpositionen(Integer iIdAuftragI, int iDivisorI)
      throws ExceptionLP {
    try {
      auftragRahmenAbrufFac.erzeugeAbrufpositionen(
          iIdAuftragI,
          iDivisorI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }
}
