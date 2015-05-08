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
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZutrittdaueroffenDto;
import com.lp.server.personal.service.ZutrittsobjektDto;
import com.lp.server.personal.service.ZutrittsobjektverwendungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
public class PanelDaueroffen
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

  private ZutrittdaueroffenDto zutrittsmodelltagdetailDto = null;

  private WrapperLabel wlaTagesart = new WrapperLabel();
  private WrapperTextField wtfTagesart = new WrapperTextField();
  private WrapperLabel wlaBeginn = new WrapperLabel();
  private WrapperTimeField wtfBeginn = new WrapperTimeField();
  private WrapperLabel wlaEnde = new WrapperLabel();
  private WrapperTimeField wtfEnde = new WrapperTimeField();
  private WrapperLabel wlaOeffnungsart = new WrapperLabel();
  private WrapperComboBox wcoTagesart = new WrapperComboBox();
  private WrapperButton wbuZutrittsobjekt = new WrapperButton();
  private WrapperTextField wtfZutrittsobjekt = new WrapperTextField();

  private PanelQueryFLR panelQueryFLRZutrittsobjekt = null;
  static final public String ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE =
      "ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE";

  public PanelDaueroffen(InternalFrame internalFrame, String add2TitleI,
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
    return wtfBeginn;
  }


  void dialogQueryZutrittsobjektFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH};

    InternalFrameZutritt ifZ = (InternalFrameZutritt) getInternalFrame();

    FilterKriterium[] fk = null;
    if (!ifZ.isBIstHauptmandant()) {
      fk = ZutrittFilterFactory.getInstance().createFKZutrittsklasse(ifZ.
          isBIstHauptmandant());
    }

    panelQueryFLRZutrittsobjekt = new PanelQueryFLR(
        null,
        fk,
        com.lp.server.util.fastlanereader.service.query.QueryParameters.
        UC_ID_ZUTRITTSOBJEKT,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.zutrittsobjekt"));
    /* panelQueryFLRZutrittsobjekt.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
         getInstance().createFKDBezeichnung(), null);
     panelQueryFLRZutrittsobjekt.setSelectedId(zutrittsklasseobjektDto.
                                               getZutrittsobjektIId());*/
    new DialogQuery(panelQueryFLRZutrittsobjekt);
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    zutrittsmodelltagdetailDto = new ZutrittdaueroffenDto();
    leereAlleFelder(this);

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE)) {
      dialogQueryZutrittsobjektFromListe(e);
    }

  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removeZutrittdaueroffen(
        zutrittsmodelltagdetailDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws ExceptionLP {

    zutrittsmodelltagdetailDto.setTagesartIId( (Integer) wcoTagesart.
                                              getKeyOfSelectedItem());
    zutrittsmodelltagdetailDto.setUOffenvon(wtfBeginn.getTime());
    zutrittsmodelltagdetailDto.setUOffenbis(wtfEnde.getTime());
  }


  protected void dto2Components()
      throws ExceptionLP, Throwable {

    ZutrittsobjektDto objektDto = DelegateFactory.getInstance().getZutrittDelegate().
        zutrittsobjektFindByPrimaryKey(zutrittsmodelltagdetailDto.getZutrittsobjektIId());
    wtfZutrittsobjekt.setText(objektDto.getBezeichnung());

    wtfBeginn.setTime(zutrittsmodelltagdetailDto.getUOffenvon());
    wcoTagesart.setKeyOfSelectedItem(zutrittsmodelltagdetailDto.
                                     getTagesartIId());

    wtfEnde.setTime(zutrittsmodelltagdetailDto.getUOffenbis());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      if (wtfBeginn.getTime().before(wtfEnde.getTime())) {
        components2Dto();
        if (zutrittsmodelltagdetailDto.getIId() == null) {
          zutrittsmodelltagdetailDto.setIId(DelegateFactory.getInstance().
                                            getZutrittDelegate().
                                            createZutrittdaueroffen(
                                                zutrittsmodelltagdetailDto));
          setKeyWhenDetailPanel(zutrittsmodelltagdetailDto.getIId());
        }
        else {
          DelegateFactory.getInstance().getZutrittDelegate().updateZutrittdaueroffen(
              zutrittsmodelltagdetailDto);
        }

        super.eventActionSave(e, true);

        if (getInternalFrame().getKeyWasForLockMe() == null) {
          getInternalFrame().setKeyWasForLockMe(internalFrameZutritt.getZutrittsmodellDto().
                                                getIId() + "");
        }
        eventYouAreSelected(false);
      }
      else {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
            LPMain.getInstance().getTextRespectUISPr("lp.error.beginnvorende"));
      }
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRZutrittsobjekt) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZutrittsobjektDto zutrittsobjektTmpDto = DelegateFactory.getInstance().
            getZutrittDelegate().zutrittsobjektFindByPrimaryKey( (Integer)
            key);

        ZutrittsobjektverwendungDto[] dtos = DelegateFactory.getInstance().
            getZutrittDelegate().
            zutrittsobjektverwendungFindByZutrittsobjektIId(zutrittsobjektTmpDto.getIId());

        wtfZutrittsobjekt.setText(zutrittsobjektTmpDto.getBezeichnung());
        zutrittsmodelltagdetailDto.setZutrittsobjektIId(zutrittsobjektTmpDto.getIId());

      }
    }

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
    wlaEnde.setText(LPMain.getInstance().getTextRespectUISPr("lp.ende"));
    wtfEnde.setMandatoryField(true);
    wtfBeginn.setMandatoryField(true);
    wcoTagesart.setMandatoryField(true);
    wlaOeffnungsart.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsmodelltagdetail.oeffnungsart"));
    wbuZutrittsobjekt.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsobjekt") +
                              "...");
    wbuZutrittsobjekt.setActionCommand(PanelZutrittklasseobjekt.
                                       ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE);
    wbuZutrittsobjekt.addActionListener(this);

    wtfZutrittsobjekt.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wtfZutrittsobjekt.setMandatoryField(true);
    wtfZutrittsobjekt.setActivatable(false);
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
    jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(1, 0, 5, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 150,
        0));
    jpaWorkingOn.add(wbuZutrittsobjekt,
                     new GridBagConstraints(0, 1, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfZutrittsobjekt,
                     new GridBagConstraints(1, 1, 3, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wlaBeginn, new GridBagConstraints(0, 2, 1, 1, 0.02, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wtfBeginn, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        50, 0));
    jpaWorkingOn.add(wlaEnde, new GridBagConstraints(2, 2, 1, 1, 0.05, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wtfEnde, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        50, 0));

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
      zutrittsmodelltagdetailDto = DelegateFactory.getInstance().getZutrittDelegate().
          zutrittdaueroffenFindByPrimaryKey( (Integer) key);
      dto2Components();

    }

  }
}
