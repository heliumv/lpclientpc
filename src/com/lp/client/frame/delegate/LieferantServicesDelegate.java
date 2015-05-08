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


import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LfliefergruppesprDto;
import com.lp.server.partner.service.LieferantServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access") 
public class LieferantServicesDelegate
    extends Delegate
{
  private Context context;
  private LieferantServicesFac lieferantServicesFac;

  public LieferantServicesDelegate()
      throws Exception {
    context = new InitialContext();
    lieferantServicesFac = (LieferantServicesFac) context.lookup("lpserver/LieferantServicesFacBean/remote");
  }


  public Integer createLfliefergruppe(LfliefergruppeDto lfliefergruppeDto)
      throws Exception {
    Integer iId = null;
    try {
      lfliefergruppeDto.setMandantCNr(
          LPMain.getInstance().getTheClient().getMandant());
      iId = lieferantServicesFac.createLfliefergruppe(lfliefergruppeDto,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return iId;
  }


  public void removeLfliefergruppe(Integer iId)
      throws Exception {
    try {
      lieferantServicesFac.removeLfliefergruppe(
          iId,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeLfliefergruppe(LfliefergruppeDto lfliefergruppeDto)
      throws Exception {
    try {
      lieferantServicesFac.removeLfliefergruppe(lfliefergruppeDto,
                                                LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updateLfliefergruppe(LfliefergruppeDto lfliefergruppeDto)
      throws Exception {
    try {
      lieferantServicesFac.updateLfliefergruppe(
          lfliefergruppeDto,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public LfliefergruppeDto lfliefergruppeFindByPrimaryKey(Integer iId)
      throws Exception, RemoteException, EJBExceptionLP, Throwable {

    LfliefergruppeDto lfliefergruppeDto = null;
    try {
      lfliefergruppeDto = lieferantServicesFac.lfliefergruppeFindByPrimaryKey(
          iId,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return lfliefergruppeDto;
  }


//******************************************************************************
   public Integer createLfliefergruppespr(LfliefergruppesprDto lfliefergruppesprDto)
       throws Exception {
     Integer iId = null;
     try {
       iId = lieferantServicesFac.createLfliefergruppespr(lfliefergruppesprDto,
           LPMain.getInstance().getTheClient());
     }
     catch (Throwable ex) {
       handleThrowable(ex);
     }
     return iId;
   }


  public void removeLfliefergruppespr(LfliefergruppesprDto lfliefergruppesprDto)
      throws Exception {
    try {
      lieferantServicesFac.removeLfliefergruppespr(lfliefergruppesprDto,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updateLfliefergruppespr(LfliefergruppesprDto lfliefergruppesprDto)
      throws Exception {
    try {
      lieferantServicesFac.updateLfliefergruppespr(lfliefergruppesprDto,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public LfliefergruppesprDto lfliefergruppesprFindByPrimaryKey(
      Integer lfliefergruppeIId,
      String localeCNr,
      TheClientDto theClientDto)
      throws Exception {

    LfliefergruppesprDto lfliefergruppesprDto = null;
    try {
      lfliefergruppesprDto = lieferantServicesFac.lfliefergruppesprFindByPrimaryKey(
          lfliefergruppeIId,
          localeCNr, theClientDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return lfliefergruppesprDto;
  }



}
