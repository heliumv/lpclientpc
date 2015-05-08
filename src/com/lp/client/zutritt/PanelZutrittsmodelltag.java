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
package com.lp.client.zutritt;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZutrittsmodelltagDto;

@SuppressWarnings("static-access")
public class PanelZutrittsmodelltag
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//von hier ...
  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jpaWorkingOn = new JPanel();
  private JPanel jpaButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private InternalFrameZutritt internalFrameZutritt = null;

  private ZutrittsmodelltagDto zutrittsmodelltagDto = null;

  private WrapperLabel wlaTagesart = new WrapperLabel();
  private WrapperComboBox wcoTagesart = new WrapperComboBox();

  public PanelZutrittsmodelltag(InternalFrame internalFrame, String add2TitleI,
                            Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFrameZutritt = (InternalFrameZutritt) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults()
      throws Throwable {
    wcoTagesart.setMap(DelegateFactory.getInstance().getZeiterfassungDelegate().
                       getAllSprTagesarten());
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wcoTagesart;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    zutrittsmodelltagDto = new ZutrittsmodelltagDto();

    leereAlleFelder(this);
  }



  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removeZutrittsmodelltag(
        zutrittsmodelltagDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws ExceptionLP {
    zutrittsmodelltagDto.setZutrittsmodellIId(internalFrameZutritt.getZutrittsmodellDto().
                                      getIId());
    zutrittsmodelltagDto.setTagesartIId( (Integer) wcoTagesart.getKeyOfSelectedItem());
  }


  protected void dto2Components()
      throws ExceptionLP, Throwable {


    wcoTagesart.setKeyOfSelectedItem(zutrittsmodelltagDto.getTagesartIId());




  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (zutrittsmodelltagDto.getIId() == null) {

        zutrittsmodelltagDto.setIId(DelegateFactory.getInstance().getZutrittDelegate().
                                createZutrittsmodelltag(zutrittsmodelltagDto));
        setKeyWhenDetailPanel(zutrittsmodelltagDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZutrittDelegate().updateZutrittsmodelltag(
            zutrittsmodelltagDto);
      }

      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameZutritt.getZutrittsmodellDto().
                                              getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  private void jbInit()
      throws Throwable {
    //von hier ...
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    //das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    //Actionpanel von Oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();
    this.setActionMap(null);

    getInternalFrame().addItemChangedListener(this);
    wlaTagesart.setText(LPMain.getInstance().getTextRespectUISPr("lp.tagesart"));
    wcoTagesart.setMandatoryField(true);

    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.SOUTHEAST,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));


    jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        200, 0));
    jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 150,
        0));
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD,
    };

    enableToolsPanelButtons(aWhichButtonIUse);

  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_ZUTRITTSMODELL;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {
      leereAlleFelder(this);
      clearStatusbar();
    }
    else {
      zutrittsmodelltagDto = DelegateFactory.getInstance().getZutrittDelegate().
          zutrittsmodelltagFindByPrimaryKey( (Integer) key);

      dto2Components();
    }
  }
}
