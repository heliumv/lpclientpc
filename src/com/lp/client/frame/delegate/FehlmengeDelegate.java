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

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.FehlmengeFac;


/**
 * <p>BusinessDelegate fuer die Fehlmengenverwaltung</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 08.11.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2010/09/16 07:49:49 $
 */
public class FehlmengeDelegate
    extends Delegate
{
  private Context context;
  private FehlmengeFac fehlmengeFac;
  public FehlmengeDelegate()
      throws ExceptionLP {
    try {
      context = new InitialContext();
      fehlmengeFac = (FehlmengeFac) context.lookup("lpserver/FehlmengeFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public BigDecimal getAnzahlFehlmengeEinesArtikels(Integer artikelIId)
      throws ExceptionLP {
    try {
      return fehlmengeFac.getAnzahlFehlmengeEinesArtikels(artikelIId,
          LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

  public BigDecimal getAnzahlderPositivenFehlmengenEinesArtikels(Integer artikelIId)
      throws ExceptionLP {
    try {
      return fehlmengeFac.getAnzahlderPositivenFehlmengenEinesArtikels(artikelIId,
          LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public ArtikelfehlmengeDto artikelfehlmengeFindByBelegartCNrBelegartPositionIId(String
      belegartCNr, Integer belegpositionIId)
      throws ExceptionLP {
    try {
      return fehlmengeFac.artikelfehlmengeFindByBelegartCNrBelegartPositionIId(
          belegartCNr, belegpositionIId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public ArtikelfehlmengeDto artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
      String belegartCNr, Integer belegpositionIId)
      throws ExceptionLP {
    try {
      return fehlmengeFac.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
          belegartCNr, belegpositionIId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public ArtikelfehlmengeDto artikelfehlmengeFindByPrimaryKey(Integer iId)
      throws ExceptionLP {
    try {
      return fehlmengeFac.artikelfehlmengeFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public void pruefeFehlmengen()
      throws ExceptionLP {
    try {
      fehlmengeFac.pruefeFehlmengen(LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }

}
