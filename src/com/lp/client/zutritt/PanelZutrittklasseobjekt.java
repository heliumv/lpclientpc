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

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.personal.service.ZutrittsklasseobjektDto;
import com.lp.server.personal.service.ZutrittsmodellDto;
import com.lp.server.personal.service.ZutrittsobjektDto;
import com.lp.server.personal.service.ZutrittsobjektverwendungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelZutrittklasseobjekt
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
  private WrapperButton wbuZutrittsmodell = new WrapperButton();
  private WrapperTextField wtfZutrittsmodell = new WrapperTextField();

  private WrapperButton wbuZutrittsobjekt = new WrapperButton();
  private WrapperTextField wtfZutrittsobjekt = new WrapperTextField();

  private ZutrittsklasseobjektDto zutrittsklasseobjektDto = null;

  private PanelQueryFLR panelQueryFLRZutrittsmodell = null;
  static final public String ACTION_SPECIAL_ZUTRITTSMODELL_FROM_LISTE =
      "ACTION_SPECIAL_ZUTRITTSMODELL_FROM_LISTE";
  private PanelQueryFLR panelQueryFLRZutrittsobjekt = null;
  static final public String ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE =
      "ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE";

  public PanelZutrittklasseobjekt(InternalFrame internalFrame, String add2TitleI,
                                  Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults() {
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    zutrittsklasseobjektDto = new ZutrittsklasseobjektDto();
    leereAlleFelder(this);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_ZUTRITTSMODELL_FROM_LISTE)) {
      dialogQueryZutrittsmodellFromListe(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE)) {
      dialogQueryZutrittsobjektFromListe(e);
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removeZutrittsklasseobjekt(
        zutrittsklasseobjektDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws Throwable {

    zutrittsklasseobjektDto.setZutrittsklasseIId( ( (InternalFrameZutritt)
        getInternalFrame()).getZutrittsklasseDto().getIId());
  }


  protected void dto2Components()
      throws Throwable {
    ZutrittsobjektDto objektDto = DelegateFactory.getInstance().getZutrittDelegate().
        zutrittsobjektFindByPrimaryKey(zutrittsklasseobjektDto.getZutrittsobjektIId());
    wtfZutrittsobjekt.setText(objektDto.getBezeichnung());
    ZutrittsmodellDto modellDto = DelegateFactory.getInstance().getZutrittDelegate().
        zutrittsmodellFindByPrimaryKey(zutrittsklasseobjektDto.getZutrittsmodellIId());
    wtfZutrittsmodell.setText(modellDto.getCNr());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (zutrittsklasseobjektDto.getIId() == null) {
        zutrittsklasseobjektDto.setIId(DelegateFactory.getInstance().getZutrittDelegate().
                                       createZutrittsklasseobjekt(
                                           zutrittsklasseobjektDto));
        setKeyWhenDetailPanel(zutrittsklasseobjektDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZutrittDelegate().updateZutrittsklasseobjekt(
            zutrittsklasseobjektDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(zutrittsklasseobjektDto.getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  void dialogQueryZutrittsmodellFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH};

    panelQueryFLRZutrittsmodell = new PanelQueryFLR(
        null,
        SystemFilterFactory.getInstance().createFKMandantCNr(),
        QueryParameters.UC_ID_ZUTRITTSMODELL,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsmodell"));

    //  FilterKriteriumDirekt fkDirekt1 = ArtikelFilterFactory.getInstance().
    //     createFKDArtikelnummer(getInternalFrame());
    //  FilterKriteriumDirekt fkDirekt2 = ArtikelFilterFactory.getInstance().
    //     createFKDVolltextsuche();
    //  panelQueryFLRZutrittsmodell.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

    panelQueryFLRZutrittsmodell.setSelectedId(zutrittsklasseobjektDto.
                                              getZutrittsmodellIId());

    new DialogQuery(panelQueryFLRZutrittsmodell);
  }


  void dialogQueryZutrittsobjektFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH};
    panelQueryFLRZutrittsobjekt = new PanelQueryFLR(
        null,
        null,
        QueryParameters.UC_ID_ZUTRITTSOBJEKT,
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


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRZutrittsmodell) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZutrittsmodellDto zutrittsmodellTmpDto = DelegateFactory.getInstance().
            getZutrittDelegate().zutrittsmodellFindByPrimaryKey( (Integer)
            key);
        wtfZutrittsmodell.setText(zutrittsmodellTmpDto.getCNr());
        zutrittsklasseobjektDto.setZutrittsmodellIId(zutrittsmodellTmpDto.getIId());
      }
      else if (e.getSource() == panelQueryFLRZutrittsobjekt) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZutrittsobjektDto zutrittsobjektTmpDto = DelegateFactory.getInstance().
            getZutrittDelegate().zutrittsobjektFindByPrimaryKey( (Integer)
            key);

        ZutrittsobjektverwendungDto[] dtos = DelegateFactory.getInstance().
            getZutrittDelegate().
            zutrittsobjektverwendungFindByZutrittsobjektIId(zutrittsobjektTmpDto.getIId());

        if (dtos.length > 0) {
          boolean bMandantGefunden = false;
          for (int i = 0; i < dtos.length; i++) {
            if (dtos[i].getMandantCNr().equals(LPMain.getInstance().getTheClient().
                                               getMandant())) {
              bMandantGefunden = true;
            }
          }
          if (bMandantGefunden) {
            wtfZutrittsobjekt.setText(zutrittsobjektTmpDto.getBezeichnung());
            zutrittsklasseobjektDto.setZutrittsobjektIId(zutrittsobjektTmpDto.getIId());
          }
          else {
            DialogFactory.showModalDialog("Fehler", "Bei dem gew\u00E4hlten Objekt ist bei einem anderen Mandanten eine Verwendungsbeschr\u00E4nkung angelegt. Somit darf das Objekt bei diesem Mandanten nicht ausgew\u00E4hlt werden.");
            return;
          }

        }
        else {
          wtfZutrittsobjekt.setText(zutrittsobjektTmpDto.getBezeichnung());
          zutrittsklasseobjektDto.setZutrittsobjektIId(zutrittsobjektTmpDto.getIId());
        }

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

    wbuZutrittsmodell.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsmodell") +
                              "...");
    wbuZutrittsmodell.setActionCommand(PanelZutrittklasseobjekt.
                                       ACTION_SPECIAL_ZUTRITTSMODELL_FROM_LISTE);
    wbuZutrittsmodell.addActionListener(this);
    wbuZutrittsobjekt.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsobjekt") +
                              "...");
    wbuZutrittsobjekt.setActionCommand(PanelZutrittklasseobjekt.
                                       ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE);
    wbuZutrittsobjekt.addActionListener(this);
    wtfZutrittsmodell.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wtfZutrittsmodell.setMandatoryField(true);
    wtfZutrittsmodell.setActivatable(false);

    wtfZutrittsobjekt.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wtfZutrittsobjekt.setMandatoryField(true);
    wtfZutrittsobjekt.setActivatable(false);

    getInternalFrame().addItemChangedListener(this);
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
    jpaWorkingOn.add(wbuZutrittsmodell,
                     new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfZutrittsmodell,
                     new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wbuZutrittsobjekt,
                     new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfZutrittsobjekt,
                     new GridBagConstraints(1, 1, 1, 1, 0.3, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
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
    return HelperClient.LOCKME_ZUTRITTSKLASSEOBJEKT;
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
      zutrittsklasseobjektDto = DelegateFactory.getInstance().getZutrittDelegate().
          zutrittsklasseobjektFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_AS_I_LIKE,
        ( (InternalFrameZutritt) getInternalFrame()).getZutrittsklasseDto().
        getBezeichnung());

  }
}
