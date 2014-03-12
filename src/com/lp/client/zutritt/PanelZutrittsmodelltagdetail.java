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
package com.lp.client.zutritt;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZutrittsmodelltagDto;
import com.lp.server.personal.service.ZutrittsmodelltagdetailDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelZutrittsmodelltagdetail
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

  private ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto = null;
  public ZutrittsmodelltagDto zutrittsmodelltagDto = null;

  private WrapperLabel wlaTagesart = new WrapperLabel();
  private WrapperTextField wtfTagesart = new WrapperTextField();
  private WrapperLabel wlaBeginn = new WrapperLabel();
  private WrapperTimeField wtfBeginn = new WrapperTimeField();
  private WrapperLabel wlaKommt = new WrapperLabel();
  private WrapperTimeField wtfEnde = new WrapperTimeField();
  private WrapperLabel wlaOeffnungsart = new WrapperLabel();
  private WrapperComboBox wcoOeffnungsart = new WrapperComboBox();

  private WrapperCheckBox wcbRestdestages = new WrapperCheckBox();

  public PanelZutrittsmodelltagdetail(InternalFrame internalFrame, String add2TitleI,
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
    Map<?, ?> m = DelegateFactory.getInstance().getZutrittDelegate().
        getAllZutrittsoeffnungsarten();
    wcoOeffnungsart.setMap(m);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfBeginn;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    if (zutrittsmodelltagDto != null) {
      super.eventActionNew(eventObject, true, false);
      zutrittsmodelltagdetailDto = new ZutrittsmodelltagdetailDto();
      leereAlleFelder(this);
    }
    else {
      DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
          "lp.error"),
                                           LPMain.getInstance().getTextRespectUISPr(
                                               "pers.error.keinzutrittsmodelltagdefiniert"));
    }

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removeZutrittsmodelltagdetail(
        zutrittsmodelltagdetailDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws ExceptionLP {

    zutrittsmodelltagdetailDto.setZutrittsmodelltagIId(zutrittsmodelltagDto.getIId());
    zutrittsmodelltagdetailDto.setZutrittsoeffnungsartCNr( (String) wcoOeffnungsart.
        getKeyOfSelectedItem());
    zutrittsmodelltagdetailDto.setUOffenvon(wtfBeginn.getTime());

    zutrittsmodelltagdetailDto.setBRestdestages(wcbRestdestages.getShort());
    if (Helper.short2Boolean(wcbRestdestages.getShort()) == false) {
      zutrittsmodelltagdetailDto.setUOffenbis(wtfEnde.getTime());
    }
    else {
      zutrittsmodelltagdetailDto.setUOffenbis(null);
    }
  }


  protected void dto2Components()
      throws ExceptionLP, Throwable {

    wtfBeginn.setTime(zutrittsmodelltagdetailDto.getUOffenvon());
    wcoOeffnungsart.setKeyOfSelectedItem(zutrittsmodelltagdetailDto.
                                         getZutrittsoeffnungsartCNr());

    wcbRestdestages.setShort(zutrittsmodelltagdetailDto.getBRestdestages());
    if (Helper.short2Boolean(zutrittsmodelltagdetailDto.getBRestdestages()) == false) {
      wtfEnde.setTime(zutrittsmodelltagdetailDto.getUOffenbis());
      wtfEnde.setActivatable(true);
    }
    else {
      wtfEnde.setTime(null);
      wtfEnde.setActivatable(false);
    }

  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (wcbRestdestages.isSelected() || wtfBeginn.getTime().before(wtfEnde.getTime())) {
        if (zutrittsmodelltagdetailDto.getIId() == null) {
          zutrittsmodelltagdetailDto.setIId(DelegateFactory.getInstance().
                                            getZutrittDelegate().
                                            createZutrittsmodelltagdetail(
                                                zutrittsmodelltagdetailDto));
          setKeyWhenDetailPanel(zutrittsmodelltagdetailDto.getIId());
        }
        else {
          DelegateFactory.getInstance().getZutrittDelegate().
              updateZutrittsmodelltagdetail(
                  zutrittsmodelltagdetailDto);
        }

        super.eventActionSave(e, true);

        if (getInternalFrame().getKeyWasForLockMe() == null) {
          getInternalFrame().setKeyWasForLockMe(internalFrameZutritt.
                                                getZutrittsmodellDto().
                                                getIId() + "");
        }
        eventYouAreSelected(false);
      }
      else {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
                                             LPMain.getInstance().getTextRespectUISPr(
            "lp.error.beginnvorende"));
      }

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
    wtfTagesart.setMandatoryField(true);
    wtfTagesart.setActivatable(false);
    wlaBeginn.setText(LPMain.getInstance().getTextRespectUISPr("lp.beginn"));
    wlaKommt.setText(LPMain.getInstance().getTextRespectUISPr("lp.ende"));
    wtfEnde.setMandatoryField(true);
    wtfBeginn.setMandatoryField(true);
    wcoOeffnungsart.setMandatoryField(true);
    wlaOeffnungsart.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsmodelltagdetail.oeffnungsart"));
    wcbRestdestages.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.restdestages"));
    wcbRestdestages.setMnemonic('R');

    wcbRestdestages.addActionListener(new PanelZeitstifte_wcbRestdestages_actionAdapter(this));

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
        0, 0));
    jpaWorkingOn.add(wtfTagesart, new GridBagConstraints(1, 0, 5, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 150,
        0));
    jpaWorkingOn.add(wlaBeginn, new GridBagConstraints(0, 1, 1, 1, 0.05, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wtfBeginn, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        50, 0));
    jpaWorkingOn.add(wlaKommt, new GridBagConstraints(2, 1, 1, 1, 0.05, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wtfEnde, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        50, 0));
    jpaWorkingOn.add(wcbRestdestages, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        120, 0));

    jpaWorkingOn.add(wlaOeffnungsart, new GridBagConstraints(5, 1, 1, 1, 0.05, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wcoOeffnungsart, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        150, 0));
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


  public void wcbRestdestages_actionPerformed(ActionEvent e) {
    if (wcbRestdestages.isSelected()) {
      wtfEnde.setEnabled(false);
    }
    else {
      wtfEnde.setEnabled(true);

    }

  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    if (zutrittsmodelltagDto != null) {
      String s = DelegateFactory.getInstance().getZeiterfassungDelegate().
          tagesartFindByPrimaryKey(
              zutrittsmodelltagDto.getTagesartIId()).getBezeichnung();

      super.eventYouAreSelected(false);
      Object key = getKeyWhenDetailPanel();
      if (key == null
          || (key.equals(LPMain.getLockMeForNew()))) {
        leereAlleFelder(this);
        clearStatusbar();
        wtfTagesart.setText(s);
        if (wcbRestdestages.isSelected()) {
          wtfEnde.setEnabled(false);
        }
        else {
          if(key!=null){
            wtfEnde.setEnabled(true);
          }
        }
      }
      else {
        zutrittsmodelltagdetailDto = DelegateFactory.getInstance().getZutrittDelegate().
            zutrittsmodelltagdetailFindByPrimaryKey( (Integer) key);
        dto2Components();
        wtfTagesart.setText(s);

      }
    }
    else {
      leereAlleFelder(this);
    }
  }
}


class PanelZeitstifte_wcbRestdestages_actionAdapter
    implements ActionListener
{
  private PanelZutrittsmodelltagdetail adaptee;
  PanelZeitstifte_wcbRestdestages_actionAdapter(PanelZutrittsmodelltagdetail adaptee) {
    this.adaptee = adaptee;
  }


  public void actionPerformed(ActionEvent e) {
    adaptee.wcbRestdestages_actionPerformed(e);
  }
}
