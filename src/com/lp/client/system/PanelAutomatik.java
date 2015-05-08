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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.ejb.FinderException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.AutomatikDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutomatiktimerDto;

@SuppressWarnings("static-access")
public class PanelAutomatik
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private GridBagLayout gridBagLayoutAll = null;
  private JPanel jPanelWorkingOn = new JPanel();
  private JPanel panelButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private TabbedPaneAutomatik tpAutomatik= null;
  private AutomatiktimerDto automatiktimerDto = null;

  private WrapperTimeField wtfJobTime= null;
  private WrapperCheckBox wcbEnabled = null;
  private WrapperLabel wlaUhrzeit = null;


  public PanelAutomatik(InternalFrame internalFrameI, String addTitleI)
      throws Throwable {
    super(internalFrameI, addTitleI);
  }


  public PanelAutomatik(InternalFrame internalFrameI, String addTitleI,
                        Object keyWhenDetailPanelI, TabbedPaneAutomatik tabbedPaneAutomatik)
      throws Throwable {
    super(internalFrameI, addTitleI, keyWhenDetailPanelI);
    this.tpAutomatik = tabbedPaneAutomatik;
    jbInit();
    initComponents();
  }


  public PanelAutomatik() {
    super();
  }


  private void jbInit()
      throws Throwable {
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);
    panelButtonAction = getToolsPanel();
    this.setActionMap(null);

    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 6, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    wtfJobTime = new WrapperTimeField();
    wtfJobTime.setMinimumSize(new Dimension(200,
        Defaults.getInstance().getControlHeight()));
    wtfJobTime.setPreferredSize(new Dimension(200,
        Defaults.getInstance().getControlHeight()));
    wlaUhrzeit = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.system.automatik.startzeit"));
    wcbEnabled = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr("lp.system.automatik.aktiv"));
    jPanelWorkingOn.add(wlaUhrzeit,
                        new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfJobTime,
                        new GridBagConstraints(4, 0, 1, 1, 0.5, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wcbEnabled,
                    new GridBagConstraints(5, 0, 5, 1, 1.0, 0.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));


    String[] aWhichButtonIUse = new String[] {
        PanelBasis.ACTION_UPDATE,
        PanelBasis.ACTION_SAVE,
        PanelBasis.ACTION_DISCARD
    };
    enableToolsPanelButtons(aWhichButtonIUse);
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null || key.equals(LPMain.getLockMeForNew())) {
      dto2Components();
      if (automatiktimerDto != null) {
        automatiktimerDto.setIId(null);
      }
      wtfJobTime.setTime(null);
    }
    else {
      automatiktimerDto= DelegateFactory.getInstance().getAutomatikDelegate().automatiktimerFindByPrimaryKey(0);
      dto2Components();
    }
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_AUTOMATIKTIMER;
  }



  protected void dto2Components()
      throws ExceptionLP, RemoteException, FinderException {
    if (automatiktimerDto == null){
      automatiktimerDto= DelegateFactory.getInstance().getAutomatikDelegate().automatiktimerFindByPrimaryKey(0);
    }
    wtfJobTime.setTime(automatiktimerDto.getTTimetoperform());
    int iEnabled = automatiktimerDto.getBEnabled();
    if (iEnabled == 1) {
      wcbEnabled.setSelected(true);
    }
    else {
      wcbEnabled.setSelected(false);
    }
  }


  protected void components2Dto()
      throws ExceptionLP {
    automatiktimerDto.setTTimetoperform(wtfJobTime.getTime());
    boolean iEnabled = wcbEnabled.isSelected();
    if (iEnabled) {
      automatiktimerDto.setBEnabled(1);

    }
    else {
      automatiktimerDto.setBEnabled(0);
    }
  }

  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
    throws
    Throwable {
    components2Dto();
    AutomatikDelegate automatikdelegate = DelegateFactory.getInstance().getAutomatikDelegate();
    automatikdelegate.updateAutomatiktimer(automatiktimerDto);
    Calendar cal = Calendar.getInstance();
    long actTimeInMillis = cal.getTimeInMillis();
    cal.set(cal.get(cal.YEAR),cal.get(cal.MONTH),cal.get(cal.DATE),wtfJobTime.getStunden(),wtfJobTime.getMinuten());
    long performTime = cal.getTimeInMillis() - actTimeInMillis;
    if (performTime > 0){
      automatikdelegate.setTimer(performTime);
    } else if (performTime < 0){
      long lOneDayInMillis = 1000*60*60*24;
      automatikdelegate.setTimer(lOneDayInMillis + performTime);
    } else {
      //wenn gleich performed werden soll erst in 100ms da sonst der client steht
      automatikdelegate.setTimer(100);
    }
    super.eventActionSave(e, true);
    eventYouAreSelected(false);
  }
}
