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
package com.lp.client.system;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.TheJudgeFac;


/**
 * <p> Diese Klasse kuemmert sich um die LockMe-Eintraege.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 26.06.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 10:28:44 $
 */
public class PanelLockMe
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private LockMeDto lockMeDto = null;

  private WrapperTextField wtfWer = null;
  private WrapperTextField wtfWas = null;

  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jPanelWorkingOn = null;
  private GridBagLayout gridBagLayoutWorkingOn = null;
  private Border innerBorder = null;

  public PanelLockMe(InternalFrame internalFrame, String add2TitleI,
                     Object key)
      throws Throwable {
    super(internalFrame, add2TitleI, key);
    jbInit();
    initComponents();
  }


  void jbInit()
      throws Throwable {

    // das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setBorder(innerBorder);

    // Actionpanel setzen und anhaengen
    JPanel panelButtonAction = getToolsPanel();
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    // zusaetzliche buttons
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_DELETE};
    enableToolsPanelButtons(aWhichButtonIUse);

    // Workingpanel
    jPanelWorkingOn = new JPanel();
    gridBagLayoutWorkingOn = new GridBagLayout();
    jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
    this.add(jPanelWorkingOn,
             new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                    , GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
                                    new Insets(0, 0, 0, 0), 0, 0));

    // Statusbar an den unteren Rand des Panels haengen
    add(getPanelStatusbar(),
        new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                               , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                               new Insets(0, 0, 0, 0), 0, 0));

    wtfWas = new WrapperTextField();
    wtfWas.setActivatable(false);
    wtfWas.setColumnsMax(TheJudgeFac.MAX_WAS);

    wtfWer = new WrapperTextField();
    wtfWer.setMandatoryField(true);
    wtfWer.setColumnsMax(TheJudgeFac.MAX_WER);

    // Zeile
    jPanelWorkingOn.add(wtfWer,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));

    jPanelWorkingOn.add(wtfWas,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));

  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {

    DelegateFactory.getInstance().getTheJudgeDelegate().removeLockedObject(lockMeDto);
    super.eventActionDelete(e, false, false);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  private void dto2Components()
      throws Throwable {
    wtfWer.setText(lockMeDto.getCWer());
    wtfWas.setText(lockMeDto.getCWas());
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    Object key = getKeyWhenDetailPanel();

    if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
      lockMeDto = new LockMeDto();
    }
    else {
      lockMeDto = DelegateFactory.getInstance().getTheJudgeDelegate().
          lockmeFindByPrimaryKey( (LockMeDto) key);

      aktualisiereStatusbar();
    }
    dto2Components();
  }


  private void aktualisiereStatusbar()
      throws Throwable {
    if (lockMeDto != null && lockMeDto.getPersonalIIdLocker() != null) {
      setStatusbarPersonalIIdAendern(lockMeDto.getPersonalIIdLocker());
      setStatusbarTAendern(lockMeDto.getTWann());
    }
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_LOCKME;
  }
}
