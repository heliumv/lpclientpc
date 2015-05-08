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


import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.util.ClientConfiguration;
import com.lp.server.system.service.InstallerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientFac;
import com.lp.util.EJBExceptionLP;


public class TheClientDelegate
    extends Delegate
{
  // zum performancetestzwecken
  private int iWasteInMs = 0;
  private Context context;
  private TheClientFac theClientFac;

  public TheClientDelegate()
      throws ExceptionLP {
    try {
        context = new InitialContext();
     theClientFac = (TheClientFac) context.lookup("lpserver/TheClientFacBean/remote");
    }
    catch (Throwable t) {
      throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
    }
  }


  public void createTheClient(TheClientDto theClientDto)
      throws Exception {
    theClientFac.createTheClient(theClientDto);
  }


  public void updateTheClient(TheClientDto theClientDto)
      throws Exception {
    theClientFac.updateTheClient(theClientDto);
  }


//  public void removeTheClient(TheClientDto theClientDto)
//      throws Exception {
//    theClientFac.removeTheClient(theClientDto);
//  }
//

  public void removeTheClientTVonTBis(Timestamp tVon, Timestamp tBis)
      throws Exception {
    try {
      theClientFac.removeTheClientTVonTBis(tVon, tBis);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }
  public InstallerDto getInstaller() throws ExceptionLP{
    try {
      return theClientFac.getInstaller();
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public InstallerDto getInstallerWithoutClientFile() throws ExceptionLP{
    try {
      return theClientFac.getInstallerWithoutClientFile();
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

  public InstallerDto getInstallerPart(Integer iPart) throws ExceptionLP{
	  try {
		  return theClientFac.getInstallerPart(iPart);
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }



  public boolean istNeuerClientVerfuegbar() throws ExceptionLP{
    try {
      return theClientFac.istNeuerClientVerfuegbar(ClientConfiguration.getBuildNumber());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return false;
    }
  }
  public TheClientDto theClientFindByPrimaryKey(String cNr)
      throws ExceptionLP {

    TheClientDto theClientDto = null;
    long lBegin = System.currentTimeMillis();
    try {
      theClientDto = theClientFac.theClientFindByPrimaryKey(cNr);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
    iWasteInMs += (System.currentTimeMillis() - lBegin);
    return theClientDto;
  }

}
