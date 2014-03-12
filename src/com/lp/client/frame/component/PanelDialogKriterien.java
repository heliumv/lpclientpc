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

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.lp.client.frame.LockStateValue;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;


/**
 * <p><I>Dieses vorgeschaltene Dialogfenster baut die Kriterien fuer ein Paneltabelle zusammen.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>21.01.05</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public abstract class PanelDialogKriterien
    extends PanelDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/** Das sind die Kriterien, die in diesem Fenster sitzen. */
  protected FilterKriterium[] aAlleKriterien = null;

  /** Knopf zum Uebernehmen der Kriterien. */
  static final public String ACTION_SPECIAL_OK =
      "action_" + ALWAYSENABLED + "ok";


  public PanelDialogKriterien(InternalFrame oInternalFrameI, String add2Title)
      throws Throwable {
    super(oInternalFrameI, add2Title);
    jbInit();
    initComponents();

    LockStateValue lockstateValue = getLockedstateDetailMainKey();
    lockstateValue.setIState(LOCK_NO_LOCKING);
    updateButtons(lockstateValue);
  }


  private void jbInit() throws Throwable {


    //accel: ALT1
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('1', java.awt.event.InputEvent.ALT_MASK), ALT1);
    getActionMap().put(ALT1, new ButtonAbstractAction(this, ALT1));
    //accel: ALT2
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('2', java.awt.event.InputEvent.ALT_MASK), ALT2);
    getActionMap().put(ALT2, new ButtonAbstractAction(this, ALT2));
    //accel: ALT3
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('3', java.awt.event.InputEvent.ALT_MASK), ALT3);
    getActionMap().put(ALT3, new ButtonAbstractAction(this, ALT3));
    //accel: ALT4
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('4', java.awt.event.InputEvent.ALT_MASK), ALT4);
    getActionMap().put(ALT4, new ButtonAbstractAction(this, ALT4));
    //accel: ALT5
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('5', java.awt.event.InputEvent.ALT_MASK), ALT5);
    getActionMap().put(ALT5, new ButtonAbstractAction(this, ALT5));
    //accel: ALT6
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('6', java.awt.event.InputEvent.ALT_MASK), ALT6);
    getActionMap().put(ALT6, new ButtonAbstractAction(this, ALT6));
    //accel: ALT7
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('7', java.awt.event.InputEvent.ALT_MASK), ALT7);
    getActionMap().put(ALT7, new ButtonAbstractAction(this, ALT7));
    //accel: ALT8
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('8', java.awt.event.InputEvent.ALT_MASK), ALT8);
    getActionMap().put(ALT8, new ButtonAbstractAction(this, ALT8));
    //accel: ALT9
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('9', java.awt.event.InputEvent.ALT_MASK), ALT9);
    getActionMap().put(ALT9, new ButtonAbstractAction(this, ALT9));
    //accel: ALTF
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('F', java.awt.event.InputEvent.ALT_MASK), ALTF);
    getActionMap().put(ALTF, new ButtonAbstractAction(this, ALTF));
    //accel: ALTR
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('R', java.awt.event.InputEvent.ALT_MASK), ALTR);
    getActionMap().put(ALTR, new ButtonAbstractAction(this, ALTR));
    //accel: ALTB
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      KeyStroke.getKeyStroke('B', java.awt.event.InputEvent.ALT_MASK), ALTB);
    getActionMap().put(ALTB, new ButtonAbstractAction(this, ALTB));


    createAndSaveAndShowButton(
        "/com/lp/client/res/check2.png",
        LPMain.getTextRespectUISPr("lp.tooltip.kriterienuebernehmen"),
        ACTION_SPECIAL_OK,
        KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK),
        null);

    String[] aWhichButtonIUse = {
        ACTION_SPECIAL_OK
    };

    enableButtonAction(aWhichButtonIUse);
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(PanelBasis.ESC) ||
        e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      getInternalFrame().closePanelDialog();
      getInternalFrame().fireItemChanged(this,
                                         ItemChangedEvent.ACTION_DISCARD);

    }
    else
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      // von der Auswahl informieren, damit es auf die Auswahl reagieren kann
      getInternalFrame().closePanelDialog();
      getInternalFrame().fireItemChanged(this,
                                         ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED);
    }
  }

  /**
   * Ueber diese Methode kann von aussden auf die Kriterien, die in diesem
   * Dialog sitzen, zugegriffen werden.
   * @throws Throwable
   * @return FilterKriterium[]
   */
  public FilterKriterium[] getAlleFilterKriterien() throws Throwable {
    return buildFilterKriterien();
  }

  /**
   * In dieser Methode werden die Kriterien, die in diesem Dialog sitzen
   * zusammengebaut.
   * @throws Throwable
   * @return FilterKriterium[]
   */
  public /*abstract*/ FilterKriterium[] buildFilterKriterien() throws Throwable {
    return null;
  }
}
