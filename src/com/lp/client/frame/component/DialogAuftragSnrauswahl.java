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
package com.lp.client.frame.component;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.auftrag.TabbedPaneAuftrag;
import com.lp.client.pc.LPMain;


public class DialogAuftragSnrauswahl
    extends JDialog implements ActionListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TabbedPaneAuftrag tpAuftrag = null;
  private Integer iIdPosition = null;

  public DialogAuftragSnrauswahl(InternalFrameAuftrag intFrame,TabbedPaneAuftrag tpAuftrag,Integer iIdPosition) {
    this(true);
    try {
      this.tpAuftrag = tpAuftrag;
      this.iIdPosition = iIdPosition;
      this.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
      jbInit();
    }
    catch (Throwable ex) {
    }
  }



  public DialogAuftragSnrauswahl(boolean bMultiselection) {
    super(LPMain.getInstance().getDesktop(),"",true);

  }


  private void jbInit()
      throws Throwable {
   getContentPane().add(tpAuftrag.refreshPanelAuftragsrnnrn(iIdPosition));
   pack();
   this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

  }

  public void actionPerformed(ActionEvent ev) {

   }


   protected void processWindowEvent(WindowEvent e) {
     if (e.getID() == WindowEvent.WINDOW_CLOSING) {
       this.dispose();
       try {
          tpAuftrag.getAuftragPositionenBottom().setKeyWhenDetailPanel(iIdPosition);
          tpAuftrag.getAuftragPositionenBottom().eventYouAreSelected(false);
          }
       catch (Throwable ex) {
       }
     }
   }
}

