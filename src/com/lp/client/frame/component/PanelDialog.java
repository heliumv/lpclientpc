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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;


/**
 * <p><I>Panel fuer einen modulmodalen dialog</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>30.11.2004</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.8 $
 */
abstract public class PanelDialog
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
  protected JPanel jpaWorkingOn = null;
  private GridBagLayout gridBagLayoutWorkingOn = null;
  private Border innerBorder = null;
  private JButton wbuExit = null;
  private ImageIcon imageIconExit = null;
  private boolean bShowExitButton = true;
  private String sOldTitle=null;

  // Knopf zum Verlassen des Dialogs
  public final static String ACTION_SPECIAL_CLOSE_PANELDIALOG =
      "action_" + ALWAYSENABLED + "close_paneldialog";

  public PanelDialog(InternalFrame internalFrame, String add2Title)
      throws Throwable {
    this(internalFrame, add2Title, true);
  }

  public PanelDialog(InternalFrame internalFrame, String add2Title, Object key)
      throws Throwable {
    super(internalFrame, add2Title, key);
  }

  public PanelDialog(InternalFrame internalFrame,
                     String add2Title,
                     boolean bShowExitButton)
      throws Throwable {

    super(internalFrame, add2Title);
    this.sOldTitle=getInternalFrame().getTitle();
    //getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, add2Title);

    this.bShowExitButton = bShowExitButton;
    jbInit();
    initComponents();

    // TODO: eventYouAreSelected gehoert nicht in den Constructor von PanelDialog. Dies muessen abgeleitete Klassen (leider) aufrufen
    // knoepfe vielleicht einschalten
    //this.eventYouAreSelected(false);    
  }


  private void jbInit()
      throws Throwable {
    // das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setBorder(innerBorder);

    // Actionpanel setzen und anhaengen
    JPanel panelToolbar = getDialogToolBar();
    this.add(panelToolbar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
                                                  , GridBagConstraints.NORTHWEST,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    String[] aWhichButtonIUse = {
//        ACTION_SPECIAL_CLOSE_PANELDIALOG
    };

    enableButtonAction(aWhichButtonIUse);

    // Workingpanel
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingOn = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingOn);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(PanelBasis.ESC)||
        e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      getInternalFrame().closePanelDialog();
    }
  }


  protected void eventActionEscape(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(PanelBasis.ESC)||
        e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
    	eventActionSpecial(e);
//      getInternalFrame().closePanelDialog();
    }
  }


  protected String getLockMeWer() {
    return null;
  }


  public JPanel getDialogToolBar()
      throws Throwable {
    GridBagLayout layoutToolbar = new GridBagLayout();
    JPanel jpaToolbar = new JPanel();
    jpaToolbar.setLayout(layoutToolbar);
    jpaToolbar.add(getToolsPanel(),
                   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                                          GridBagConstraints.HORIZONTAL,
                                          new Insets(0, 0, 0, 0), 0, 0));
    if (bShowExitButton) {
      wbuExit = new JButton(getImageIconExit());
      wbuExit.setActionCommand(ACTION_SPECIAL_CLOSE_PANELDIALOG);
      // component name f. abbot/qftest
      if(Defaults.getInstance().isComponentNamingEnabled() ||
         Defaults.getInstance().isOldComponentNamingEnabled()) {
        wbuExit.setName(ACTION_SPECIAL_CLOSE_PANELDIALOG);
      }
      wbuExit.addActionListener(this);
      wbuExit.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
      wbuExit.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
      KeyStroke accelKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
      wbuExit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(accelKey,
          ACTION_SPECIAL_CLOSE_PANELDIALOG);
      wbuExit.getActionMap().put(ACTION_SPECIAL_CLOSE_PANELDIALOG,
                                 new
                                 ButtonAbstractAction(this, ACTION_SPECIAL_CLOSE_PANELDIALOG));
      jpaToolbar.add(wbuExit,
                     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(0, 0, 0, 0), 0,
                                            0));
    }
    return jpaToolbar;
  }


  private ImageIcon getImageIconExit() {
    if (imageIconExit == null) {
      imageIconExit = new ImageIcon(getClass().getResource("/com/lp/client/res/exit.png"));
    }
    return imageIconExit;
  }

  public String getOldTitle() {
    return sOldTitle;
  }
  
}
