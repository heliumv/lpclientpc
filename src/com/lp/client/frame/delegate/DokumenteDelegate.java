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
import com.lp.server.system.service.BelegartdokumentDto;
import com.lp.server.system.service.DokumentDto;
import com.lp.server.system.service.DokumenteFac;

public class DokumenteDelegate
    extends Delegate
{
  private Context context;
  private DokumenteFac dokumneteFac;
  public DokumenteDelegate()
      throws Exception {
    context = new InitialContext();
    dokumneteFac = (DokumenteFac) context.lookup("lpserver/DokumenteFacBean/remote");
  }


  public DokumentDto dokumentFindByPrimaryKey(Integer iId)
      throws Throwable {
    try {
      return dokumneteFac.dokumentFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }

  }

  public BelegartdokumentDto belegartdokumentFindByPrimaryKey(Integer iId)
      throws Throwable {
    try {
      return dokumneteFac.belegartdokumentFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }

  }

  public Integer createBelegartdokument(BelegartdokumentDto belegartdokumentDto)
       throws Throwable {
     try {
       return dokumneteFac.createBelegartdokument(belegartdokumentDto,LPMain.getTheClient());
     }
     catch (Throwable ex) {
       handleThrowable(ex);
       return null;
     }

   }

   public void updateBelegartdokument(BelegartdokumentDto belegartdokumentDto)
      throws Throwable {
    try {
      dokumneteFac.updateBelegartdokument(belegartdokumentDto,LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }
  public void removeBelegartdokument(BelegartdokumentDto belegartdokumentDto)
     throws Throwable {
   try {
     dokumneteFac.removeBelegartdokument(belegartdokumentDto);
   }
   catch (Throwable ex) {
     handleThrowable(ex);
   }

 }





  public DokumentDto dokumentFindByPrimaryKeyOhneInhaltBeiPdf(Integer iId)
      throws Throwable {
    try {
      return dokumneteFac.dokumentFindByPrimaryKeyOhneInhaltBeiPdf(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }

  }

  public void vertauscheBelegartDokument(Integer iId1,
                                           Integer iId2)
      throws ExceptionLP {
    try {
      dokumneteFac.vertauscheBelegartDokument(
          iId1,
          iId2);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }

  public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(String belegartCNr,Integer iBelegartid,
      int iSortierungNeuePositionI)
      throws ExceptionLP {
    try {
      dokumneteFac.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
          belegartCNr,iBelegartid,
          iSortierungNeuePositionI);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }

}
