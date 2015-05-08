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
package com.lp.client.zeiterfassung.f630;


import java.util.ResourceBundle;
import java.util.TimerTask;

import com.lp.client.util.logger.LpLogger;


/**
 * <p><I>[Hier die Beschreibung der Klasse eingfuegen]</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>[Hier das Erstellungsdatum einfuegen]</I></p>
 * <p> </p>
 * @author unbekannt
 * @version $Revision: 1.2 $
 */

public class ZETimer
{
  /**
   * ZETimer
   */
  public ZETimer(ResourceBundle ressourceBundle) {
    dialogZestifte = new DialogZestifte(ressourceBundle,this);
    dialogZestifte.setSize(500, 500);
    dialogZestifte.setVisible(true);

  }

  public boolean bProzessInArbeit=false;
  private DialogZestifte dialogZestifte = null;
  private java.util.Timer timer = null;
  protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());




  public void start() {
    timer = new java.util.Timer();
 timer.schedule(new RemindTask(),
                5000, //initial delay
                1 * 10000); //subsequent rate


  }


  public void pause() {
  }


  public void stop() {
    if(timer!=null){
      timer.cancel();
    }
  }


  class RemindTask
      extends TimerTask
  {

    public void run() {
     if(bProzessInArbeit==false){
       dialogZestifte.jButtonStart.setEnabled(false);
       dialogZestifte.jButtonClear.setEnabled(false);
       dialogZestifte.jButtonStop.setEnabled(true);
       dialogZestifte.starteDurchgang();
       bProzessInArbeit=false;
       dialogZestifte.jButtonStart.setEnabled(true);
       dialogZestifte.jButtonClear.setEnabled(true);

       dialogZestifte.jButtonStop.setEnabled(false);
       dialogZestifte.jButtonStop.setText("STOP");

     }
    }
  }
}
