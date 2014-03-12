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
package com.lp.client.pc;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperPasswordField;

@SuppressWarnings("static-access")
/**
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Organisation: </p>
 *
 * Author   $Author: valentin $
 * Revision $Revision: 1.3 $
 * Letzte Aenderung: $Date: 2008/08/11 09:47:42 $
 */
public class DialogNeueskennwort
    extends JDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperPasswordField wpwdKennwort = new WrapperPasswordField();
  private WrapperLabel wlaKennwort = new WrapperLabel();
  private WrapperButton wbtOk = new WrapperButton();
  private WrapperLabel wlaKennwortWiederholen = new WrapperLabel();
  private WrapperPasswordField wpwdKennwortWiederholen = new WrapperPasswordField();

  private JPanel jpaOben = new JPanel();
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private GridBagLayout gridBagLayoutAussen = new GridBagLayout();
  private Border border = null;

  public DialogNeueskennwort(Frame frame, String title) {

    super(frame, title, true);

    jbInit();
    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().
        getScreenSize();
    setLocation(screenSize.width / 4, screenSize.height / 4);
    pack();
    this.setVisible(true);
    wpwdKennwort.requestFocus();
  }


  public char[] getNeuesKennwort() {
    return wpwdKennwort.getPassword();
  }


  private void jbInit() {

    wlaKennwort.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.benutzer.neueskennwort"));

    wlaKennwortWiederholen.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.benutzer.kennwortbestaetigen"));

    wbtOk.setHorizontalAlignment(SwingConstants.CENTER);
    wbtOk.setHorizontalTextPosition(SwingConstants.CENTER);
    wbtOk.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
    wbtOk.addActionListener(new DialogNeueskennwort_brnAnmelden_actionAdapter(this));

    getRootPane().setDefaultButton(wbtOk);

    setModal(true);
    setResizable(false);
    setTitle(LPMain.getInstance().getTextRespectUISPr("pers.benutzer.neueskennwort"));
    getContentPane().setLayout(gridBagLayoutAussen);
    addWindowListener(new DialogNeueskennwort_this_windowAdapter(this));

    jpaOben.setLayout(gridBagLayout);
    jpaOben.setAlignmentX( (float) 0.0);
    jpaOben.setAlignmentY( (float) 0.0);
    border = BorderFactory.createEmptyBorder(0, 20, 0, 20);
    jpaOben.setBorder(border);

    int w = 340;
    int h = 120;
    jpaOben.setMaximumSize(new Dimension(w, h));
    jpaOben.setMinimumSize(new Dimension(w, h));
    jpaOben.setPreferredSize(new Dimension(w, h));

    getContentPane().add(jpaOben,
                         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.EAST,
                                                GridBagConstraints.BOTH,
                                                new Insets(0, 0, 0, 0), 0, 0));
    int iZeile = 0;
    jpaOben.add(wlaKennwort,
                new GridBagConstraints(0, iZeile, 1, 1, 0.5, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(2, 2, 2, 2), 0, 0));
    jpaOben.add(wpwdKennwort,
                new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaOben.add(wlaKennwortWiederholen,
                new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(2, 2, 2, 2), 0, 0));
    jpaOben.add(wpwdKennwortWiederholen,
                new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaOben.add(wbtOk,
                new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(2, 2, 2, 2), 0, 0));

  }


  void brnAnmelden_actionPerformed(ActionEvent e) {
    if (wpwdKennwort.getPassword().length > 3) {
      if (new String(wpwdKennwort.getPassword()).equals(new String(
          wpwdKennwortWiederholen.getPassword()))) {

        dispose();
      }
      else {

        JOptionPane pane = InternalFrame.getNarrowOptionPane(
            Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
        pane.setMessage(LPMain.getInstance().getTextRespectUISPr(
            "pers.error.kennwoerterstimmennichtueberein"));
        pane.setMessageType(JOptionPane.WARNING_MESSAGE);
        JDialog dialog = pane.createDialog(this, LPMain.getInstance().getTextRespectUISPr(
            "lp.error"));
        dialog.setVisible(true);

      }
    }
    else {

      JOptionPane pane = InternalFrame.getNarrowOptionPane(
          Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
      pane.setMessage("Kennwort muss mindestens 4-stellig sein.");
      pane.setMessageType(JOptionPane.WARNING_MESSAGE);
      JDialog dialog = pane.createDialog(this, LPMain.getInstance().getTextRespectUISPr(
          "lp.warning"));
      dialog.setVisible(true);

    }
  }


  void wpwdKennwortFocusLost(FocusEvent e) {
    int i = 0;
  }


  void this_windowClosing(WindowEvent e) {
    ( (Desktop) getOwner()).setBAbbruch(true);
  }


  public void btnInfo_actionPerformed(ActionEvent e) {
    try {
      LPMain.getInstance().getDesktop().showAboutDialog();
    }
    catch (Throwable ex) {
      LPMain.getInstance().getDesktop().exitClientDlg();
    }

  }

}


class DialogNeueskennwort_brnAnmelden_actionAdapter
    implements java.awt.event.ActionListener
{
  DialogNeueskennwort adaptee;

  DialogNeueskennwort_brnAnmelden_actionAdapter(DialogNeueskennwort adaptee) {
    this.adaptee = adaptee;
  }


  public void actionPerformed(ActionEvent e) {
    adaptee.brnAnmelden_actionPerformed(e);
  }
}


class DialogNeueskennwort_this_windowAdapter
    extends java.awt.event.WindowAdapter
{
  DialogNeueskennwort adaptee;

  DialogNeueskennwort_this_windowAdapter(DialogNeueskennwort adaptee) {
    this.adaptee = adaptee;
  }


  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
