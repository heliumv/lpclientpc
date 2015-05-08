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
package com.lp.client.personal;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public class DialogSchichtzeit
    extends JDialog
{


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelBasis panelDetailSchichtzeit = null;
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private InternalFramePersonal internalFrame = null;
  private Integer personalIId=null;


  public DialogSchichtzeit() {
    // nothing here
  }


  public DialogSchichtzeit(InternalFramePersonal internalFrame, Integer personalIId)
      throws Throwable {
    super(LPMain.getInstance().getDesktop(),
          LPMain.getInstance().getTextRespectUISPr("pers.schichtzeit"), true);

    this.internalFrame = internalFrame;
    this.personalIId=personalIId;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    jbInit();
    pack();
    this.setSize(500, 270);
    panelDetailSchichtzeit.updateButtons(new LockStateValue(PanelBasis.
        LOCK_FOR_NEW));
  }


  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      internalFrame.removeItemChangedListener(panelDetailSchichtzeit);
      panelDetailSchichtzeit = null;
      this.dispose();
    }
  }

  public void closeDialog(){
    processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
  }



  private void jbInit()
      throws Throwable {

      panelDetailSchichtzeit =
          new PanelSchichtzeit(
              internalFrame,
              LPMain.getInstance().getTextRespectUISPr(
                  "artikel.fehlmengen.aufloesen"), personalIId,this);



      panelDetailSchichtzeit.setKeyWhenDetailPanel(personalIId);
      panelDetailSchichtzeit.eventYouAreSelected(false);

      this.getContentPane().setLayout(gridBagLayout2);

      this.getContentPane().add(panelDetailSchichtzeit,
                                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
        0));
  }

}
