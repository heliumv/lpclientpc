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
package com.lp.client.frame.component.jtapi;


import javax.telephony.Address;
import javax.telephony.Call;
import javax.telephony.CallObserver;
import javax.telephony.Connection;
import javax.telephony.InvalidArgumentException;
import javax.telephony.JtapiPeer;
import javax.telephony.JtapiPeerFactory;
import javax.telephony.Provider;
import javax.telephony.Terminal;
import javax.telephony.events.CallEv;
import javax.telephony.events.ConnAlertingEv;
import javax.telephony.events.ConnConnectedEv;
import javax.telephony.events.ConnDisconnectedEv;
import javax.telephony.events.ConnEv;
import javax.telephony.events.ConnInProgressEv;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
public class Outcall
{
  static private Outcall outcall = null;
  private Address origaddr = null;
  private Terminal origterm = null;
  private Provider myprovider = null;

  static public Outcall getInstance()
      throws Throwable {
    if (outcall == null) {
      String lineNumber = "16";
      ArbeitsplatzparameterDto parameter = DelegateFactory.getInstance().
          getParameterDelegate().
          holeArbeitsplatzparameter(ParameterFac.ARBEITSPLATZPARAMETER_TAPI_LINE);

      if (parameter.getCWert() != null) {
        lineNumber = parameter.getCWert();
      }
      try {
        outcall = new Outcall(lineNumber);
      }
      catch (InvalidArgumentException ex) {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
                                      "Line '" + lineNumber +
                                      "' stellt keine TAPI-Services zur Verf\u00FCgung. (" +
                                      ex.toString() +
                                      ")");
        outcall = null;
      }
      catch (Exception excp) {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
                                      "Es ist kein TAPI installiert. (" + excp.getMessage() +
                                      ")");
        outcall = null;
      }

      catch (UnsatisfiedLinkError ex2) {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
                                      "Bitte XTapi.dll ins JAVA_HOME/bin Verzeichnis kopieren. (" +
                                      ex2.getMessage() +
                                      ")");
        outcall = null;
      }

    }
    return outcall;
  }


  private Outcall(String lineNumber)
      throws Throwable {

    JtapiPeer peer = JtapiPeerFactory.getJtapiPeer(
        "net.xtapi.XJtapiPeer");
    myprovider = peer.getProvider("MSTAPI");

    origaddr = myprovider.getAddress(lineNumber);
    //Just get some Terminal on this Address
    Terminal[] terminals = origaddr.getTerminals();
    if (terminals == null) {
      DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
          "lp.error"),
                                    "Keine Terminals an Adresse " + lineNumber);
      return;
    }
    origterm = terminals[0];

  }


  public void call(String telefonnummer)
      throws Throwable {
    // Create the telephone call object and add an observer.
    Call mycall = null;
    try {
      mycall = myprovider.createCall();
      mycall.addObserver(new OutCallObserver());
    }
    catch (Exception excp) {
      System.err.println(excp);
    }

    String amtsholung = "";

    ParametermandantDto parameter = (ParametermandantDto)
        DelegateFactory.getInstance().getParameterDelegate().
        getParametermandant(ParameterFac.PARAMETER_AMTSLEITUNGSVORWAHL_TELEFON,
                            ParameterFac.KATEGORIE_ALLGEMEIN,
                            LPMain.getInstance().getTheClient().getMandant());

    if (parameter.getCWert() != null && !parameter.getCWert().equals(" ")) {
      amtsholung = parameter.getCWert();
    }


   telefonnummer=Helper.befreieFaxnummerVonSonderzeichen(telefonnummer);

    try {
      Connection c[] = mycall.connect(origterm, origaddr, amtsholung + telefonnummer);
    }
    catch (Exception excp) {
      System.out.println(excp.toString());
    }
  }

}


class OutCallObserver
    implements CallObserver
{

  public void callChangedEvent(CallEv[] evlist) {

    System.out.println("OutCallObserver.callChangedEvent");

    for (int i = 0; i < evlist.length; i++) {

      if (evlist[i] instanceof ConnEv) {

        String name = null;
        try {
          Connection connection = ( (ConnEv) evlist[i]).getConnection();
          Address addr = connection.getAddress();
          name = addr.getName();
        }
        catch (Exception excp) {
          // Handle Exceptions
          System.err.println(excp);
        }
        String msg = "Connection to Address: " + name + " is ";

        if (evlist[i].getID() == ConnAlertingEv.ID) {
          System.out.println(msg + "ALERTING");
        }
        else if (evlist[i].getID() == ConnInProgressEv.ID) {
          System.out.println(msg + "INPROGRESS");
        }
        else if (evlist[i].getID() == ConnConnectedEv.ID) {
          System.out.println(msg + "CONNECTED");
        }
        else if (evlist[i].getID() == ConnDisconnectedEv.ID) {
          System.out.println(msg + "DISCONNECTED");
        }
      }
    }
  }
}
