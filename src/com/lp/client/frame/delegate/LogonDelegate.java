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


import java.sql.Timestamp;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.Helper;


/**
 * <p><I>Diese Klasse kuemmert sich um das Einloggen.</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>dd.mm.04</I></p>
 *
 * <p> </p>
 * @author $Author: adi $
 * @version $Revision: 1.10 $
 */
public class LogonDelegate
    extends Delegate
{

  Context context = null;
  LogonFac logonFac = null;

  public LogonDelegate()
      throws Exception {
    context = new InitialContext();
    logonFac = (LogonFac) context.lookup("lpserver/LogonFacBean/remote");
  }


  public TheClientDto logon(String benutzer, char[] kennwort, Locale uILocale, String sMandantI)
      throws ExceptionLP {

	TheClientDto r = null;
	String name = benutzer.substring(0,benutzer.indexOf("|"));
	
	try {
      r = logonFac.logon(
          benutzer,
          Helper.getMD5Hash((name + new String(kennwort)).toCharArray()),
          uILocale,
          sMandantI,
          LPMain.getTheClient(),
          new Integer(Integer.parseInt(
              LPMain.getInstance().getLPParameter("lp.version.client.build"))),
          new Timestamp(System.currentTimeMillis()));
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return r;
  }
/*
  public String logon(String benutzer, char[] kennwort, Locale uILocale, String sMandantI)
  throws ExceptionLP {

	  String r = null;
	  try {
		  r = logonFac.logon(
				  benutzer,
				  kennwort,
				  uILocale,
				  sMandantI,
				  LPMain.getInstance().getCNrUser(),
				  new Integer(Integer.parseInt(
						  LPMain.getInstance().getLPParameter("lp.version.client.build"))),
						  new Timestamp(System.currentTimeMillis()));
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
	  }
	  return r;
  }
*/
 


  public void logout(TheClientDto theClientDto)
      throws ExceptionLP {
    try {
      logonFac.logout(theClientDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public Integer getIBenutzerFrei(TheClientDto theClient)
  throws ExceptionLP {
	  Integer benutzerFrei = null;
	  try {
		  benutzerFrei = logonFac.getIBenutzerFrei(theClient);
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
	  }
	  return benutzerFrei;
  }

}
