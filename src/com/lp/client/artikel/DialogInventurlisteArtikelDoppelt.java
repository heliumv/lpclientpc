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
package com.lp.client.artikel;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access") 
public class DialogInventurlisteArtikelDoppelt
    extends JDialog implements ActionListener
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private WrapperLabel lblFrage = new WrapperLabel();
  private JButton btnKorrekturbuchung = new JButton();
  private JButton btnErgaenzung = new JButton();
  public Boolean bKorrekturbuchung = null;


  public DialogInventurlisteArtikelDoppelt()
      throws Throwable {
    super(LPMain.getInstance().getDesktop(),
          LPMain.getInstance().
                                           getTextRespectUISPr("lp.frage"), true);

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   // setBounds(100,100,500,500);
    jbInit();
    this.setSize(300,150);
    lblFrage.setFocusable(true);
    this.requestFocus();

  }
  public void actionPerformed(ActionEvent e) {
    if(e.getSource().equals(btnErgaenzung)){
      bKorrekturbuchung=false;
    } else if(e.getSource().equals(btnKorrekturbuchung)){
      bKorrekturbuchung=true;
    }

    this.setVisible(false);
  }


  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      this.dispose();
    }
  }


  private void jbInit()
      throws Throwable {

    lblFrage.setText(LPMain.getInstance().getTextRespectUISPr("inventur.scannereintragvorhanden"));
    btnKorrekturbuchung.setText(LPMain.getInstance().getTextRespectUISPr("inventur.korrekturbuchung"));
    btnKorrekturbuchung.setMnemonic('K');
    btnErgaenzung.setText(LPMain.getInstance().getTextRespectUISPr("inventur.ergaenzungsbuchung"));
    btnErgaenzung.setMnemonic('E');
    btnKorrekturbuchung.addActionListener(this);
    btnErgaenzung.addActionListener(this);

    this.getContentPane().setLayout(gridBagLayout2);

    this.getContentPane().add(lblFrage,
                              new GridBagConstraints(0, 0, 2, 1, 1, 1
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0,
        80));
    this.getContentPane().add(btnKorrekturbuchung,
                              new GridBagConstraints(0, 1, 1, 1, 0,0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0,
        0));
    this.getContentPane().add(btnErgaenzung,
                              new GridBagConstraints(1, 1, 1, 1, 0,0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0,
        0));

  }

}
