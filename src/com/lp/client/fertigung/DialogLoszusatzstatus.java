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
package com.lp.client.fertigung;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access") 
public class DialogLoszusatzstatus
    extends JDialog implements KeyListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JPanel panelUrlaubsanspruch = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  WrapperLabel wlaLosnummer = new WrapperLabel();
  WrapperLabel wlaStatus = new WrapperLabel();

  WrapperLabel wlaZusatzstatus = new WrapperLabel();

  ZusatzstatusDto zusatzstatusDto = null;

  WrapperTextField wtfLosnummer = null;

  GridBagLayout gridBagLayout2 = new GridBagLayout();

  public DialogLoszusatzstatus(ZusatzstatusDto zusatzstatusDto)
      throws Exception {
    super(LPMain.getInstance().getDesktop(),
          LPMain.getInstance().getTextRespectUISPr("fert.zusatzstatuserfassen"), true);
    this.zusatzstatusDto = zusatzstatusDto;

    wlaZusatzstatus.setText("Auf diesem Arbeitsplatz definierter Zusatzstatus: "+zusatzstatusDto.getCBez());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    jbInit();
    pack();

  }


  private void jbInit()
      throws Exception {

    wtfLosnummer = new WrapperTextField();

    wtfLosnummer.addKeyListener(this);
    panelUrlaubsanspruch.setLayout(gridBagLayout1);
    wlaLosnummer.setText(LPMain.getInstance().getTextRespectUISPr(
        "label.losnummer") + ":");

    this.getContentPane().setLayout(gridBagLayout2);
    this.getContentPane().add(panelUrlaubsanspruch,
                              new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 350,
        50));


    panelUrlaubsanspruch.add(wlaZusatzstatus, new GridBagConstraints(0, 0, 2, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));




    panelUrlaubsanspruch.add(wlaLosnummer, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    panelUrlaubsanspruch.add(wtfLosnummer,
                             new GridBagConstraints(1, 1, 1, 1, 0.2, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0,
        0));
    panelUrlaubsanspruch.add(wlaStatus,
                            new GridBagConstraints(0, 2, 2, 1, 0, 0.0
       , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
       0,
       0));





  }


  public void keyTyped(KeyEvent e) {
  }


  public void keyPressed(KeyEvent e) {
    wlaStatus.setText(null);
    if (e.getSource() == wtfLosnummer && e.getKeyCode() == KeyEvent.VK_ENTER) {
      String losnummer = wtfLosnummer.getText();
      if (losnummer != null && losnummer.length() > 1 && losnummer.startsWith("$L")) {
        losnummer = losnummer.substring(2);

        LosDto losDto = null;
        try {
          losDto = DelegateFactory.getInstance().getFertigungDelegate().
              losFindByCNrMandantCNr(
                  losnummer, LPMain.getInstance().getTheClient().getMandant());
        }
        catch (Throwable ex) {
          if (ex instanceof ExceptionLP) {

            if ( ( (ExceptionLP) ex).getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
              DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
                  "lp.warning"),"Los '"+losnummer+"' konnte nicht gefunden werden.");
              wtfLosnummer.setText(null);
              return;
            }
            else {
              LPMain.getInstance().getDesktop().exitClientNowErrorDlg(ex);
            }

          }
          else {
            LPMain.getInstance().getDesktop().exitClientNowErrorDlg(ex);
          }

        }
        try {
          LoszusatzstatusDto loszusatzstatusDto = new LoszusatzstatusDto();
          loszusatzstatusDto.setZusatzstatusIId(zusatzstatusDto.getIId());
          loszusatzstatusDto.setLosIId(losDto.getIId());

          DelegateFactory.getInstance().getFertigungDelegate().createLoszusatzstatus(
              loszusatzstatusDto);
          wlaStatus.setText("Status '"+zusatzstatusDto.getCBez()+"' f\u00FCr Los "+losDto.getCNr()+" eingetragen.");

        }
        catch (Throwable ex) {
          if (ex instanceof ExceptionLP) {

            if ( ( (ExceptionLP) ex).getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
              DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
                  "lp.warning"),
                                           "Status '"+zusatzstatusDto.getCBez()+"' f\u00FCr Los "+losnummer+" ist bereits definiert");

            }
            else {
              LPMain.getInstance().getDesktop().exitClientNowErrorDlg(ex);
            }

          }
          else {
            LPMain.getInstance().getDesktop().exitClientNowErrorDlg(ex);
          }

        }
      }
      wtfLosnummer.setText(null);
    } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
      this.dispose();
    }

  }


  public void keyReleased(KeyEvent e) {
  }
}
