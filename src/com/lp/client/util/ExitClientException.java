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
package com.lp.client.util;


/**
 * <p>
 * clientexc: 2
 * Diese Exception wird zum Clientabbruch geworfen.
 * </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author $Author: valentin $
 * @version $Revision: 1.2 $
 */

public class ExitClientException
    extends Exception
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String msgForBenutzer;
  private Throwable throwable;

  public ExitClientException(Exception e, String msg) {
    super(e);
    this.throwable = e;
    this.msgForBenutzer = msg;
  }


  public ExitClientException(Throwable t, String msg) {
    super(t);
    this.throwable = t;
    this.msgForBenutzer = msg;
  }


  public String getMsgForBenutzer() {
    return msgForBenutzer;
  }


  public void setMsgForBenutzer(String msgForBenutzer) {
    this.msgForBenutzer = msgForBenutzer;
  }


  public Throwable getException() {
    return throwable;
  }


  public void setException(Exception exception) {
    this.throwable = exception;
  }

//  static public ExceptionForLPClients isExceptionForClient(Throwable throwableI) {
//    ExceptionForLPClients is = null;
//    if (throwableI instanceof ExitFrameException) {
//      if (throwableI.getCause() instanceof ExceptionForLPClients) {
//        is = (ExceptionForLPClients) throwableI.getCause();
//      }
//    }
//    return is;
//  }
}
