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
package com.lp.client.frame.component;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.LPModul;

@SuppressWarnings("static-access") 
/**
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>dd.mm.05</I></p>
 *
 * <p> </p>
 *
 * @author not attributable
 * @version $Revision: 1.3 $
 */
public class ToolbarButton
    extends JPanel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public final static String ACTION_RESTORE = "_restore";
  private JButton jbuStart = null;
  private JButton jbuRestore = null;
  private JButton jbuStartNewInstance = null;
  private String sActionCommand = null;
  private String sToolTipText = null;
  private Icon icon = null;
  private boolean bRestoreVisible = false;

  public ToolbarButton(String sActionCommand, String sToolTipText, Icon icon,
                       boolean bRestoreVisible)
      throws Throwable {
    this.sActionCommand = sActionCommand;
    this.sToolTipText = sToolTipText;
    this.icon = icon;
    this.bRestoreVisible = bRestoreVisible;
    jbInit();
    // der Name des Buttons (fuer die Testumgebung) wird der Modulname (Belegart-Konstante aus LocaleFac)
    if (sActionCommand != null && Defaults.getInstance().isComponentNamingEnabled()) {
      jbuStart.setName(HelperClient.COMP_PRAEFIX_TOOLBARBUTTON + sActionCommand.trim());
    }
    else if (sActionCommand != null && Defaults.getInstance().isOldComponentNamingEnabled()) {
      jbuStart.setName(sActionCommand.trim());
    }
  }


  public ToolbarButton(String sActionCommand, String sToolTipText, Icon icon)
      throws Throwable {
    this(sActionCommand, sToolTipText, icon, false);
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(new GridBagLayout());
    this.setMaximumSize(new Dimension(34, 38));
    this.setPreferredSize(new Dimension(34, 38));
    jbuStart = new JButton();
    jbuRestore = new JButton();
    jbuStartNewInstance = new JButton();
    jbuRestore.setActionCommand(sActionCommand + ACTION_RESTORE);
    jbuRestore.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.tooltip.wiederherstellen"));
    jbuStart.setActionCommand(sActionCommand);
    jbuStart.setIcon(icon);
    jbuStart.setToolTipText(sToolTipText);
    jbuRestore.setEnabled(false);
    jbuRestore.setVisible(false);
    jbuRestore.setMinimumSize(new Dimension(13, 7));
    jbuRestore.setMaximumSize(new Dimension(13, 7));
    jbuRestore.setPreferredSize(new Dimension(13, 7));
    jbuStartNewInstance.setEnabled(false);
    jbuStartNewInstance.setVisible(false);
    jbuStartNewInstance.setMinimumSize(new Dimension(13, 7));
    jbuStartNewInstance.setMaximumSize(new Dimension(13, 7));
    jbuStartNewInstance.setPreferredSize(new Dimension(13, 7));
//    jbuStart.setEnabled(false);
    if (bRestoreVisible) {
      this.add(jbuRestore,
               new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                      GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
                                      0));
      //this.add(jbuStartNewInstance, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
      //    , GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
      //    0));
    }
    this.add(jbuStart, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, 0));
  }


  public JButton getJbuRestore() {
    return jbuRestore;
  }


  public JButton getJbuStart() {
    return jbuStart;
  }


  public JButton getJbuStartNewInstance() {
    return jbuStartNewInstance;
  }


  public String getActionCommand() {
    return sActionCommand;
  }


  public void setEnabled(int iStatus) {
    /*
    boolean showRestore = false;
    boolean showStartNewInstance = false;
    switch (iStatus) {
      case LPModul.STATUS_DISABLED_ALL: {
        showRestore = false;
        showStartNewInstance = false;
      }
      break;
      case LPModul.STATUS_ENABLED: {
        showRestore = false;
        showStartNewInstance = false;
      }
      break;
      case LPModul.STATUS_DISABLED: {
        showRestore = true;
        showStartNewInstance = true;
      }
      break;
    }
    getJbuRestore().setEnabled(showRestore);
    getJbuRestore().setVisible(showRestore);
    getJbuStartNewInstance().setVisible(showStartNewInstance);
    getJbuStart().setEnabled(showStartNewInstance);
    */
   jbuStart.setEnabled(iStatus == LPModul.STATUS_ENABLED);
  }
}
