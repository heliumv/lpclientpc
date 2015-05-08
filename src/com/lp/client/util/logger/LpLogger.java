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
package com.lp.client.util.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.lp.client.frame.ExceptionLP;
import com.lp.util.EJBExceptionLP;


public class LpLogger
    extends Logger {
  private String dateFormatPattern = "yyyy-MM-dd HH:mm:ss:SSS";
  protected DateFormat dfServer = new SimpleDateFormat(dateFormatPattern);

  static String FQCN = LpLogger.class.getName() + ".";

  private static LpLoggerFactory myFactory = new LpLoggerFactory();

  public LpLogger(String name) {
    super(name);
  }

  public void debug(Object message) {
    log(FQCN, Level.DEBUG, message, null);
  }

  public static Category getInstance(Class clazz) {
    return Logger.getLogger(clazz.getName(), myFactory);
  }

  public static Logger getLogger(String name) {
    return Logger.getLogger(name, myFactory);
  }

  public void entry() {
    debug("> entry");
  }

  public void exit() {
    debug("< exit");
  }

  /**
   * Loggen.
   *
   * Die Serverzeit wird ab 3.12.2004 nicht mehr mitgeloggt
   *
   * @param callerFQCN String
   * @param level Priority
   * @param message Object
   * @param t Throwable
   */
  public void log(String callerFQCN, Priority level, Object message,
                  Throwable t) {
    super.log(callerFQCN, level, "[" + name + "]" + message, t);
  }



  public void error(Object message, Throwable t) {
    String sMessage=message!=null?message.toString():"";
    if(t instanceof EJBExceptionLP) {
      sMessage="Code="+((EJBExceptionLP)t).getCode()+" "+sMessage;
    }
    else if(t instanceof ExceptionLP) {
      sMessage="Code="+((ExceptionLP)t).getICode()+" "+sMessage;
    }
    super.error(sMessage, t);
  }
}
