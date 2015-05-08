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


import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class TabbedPaneZutrittsmodell
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryZutrittsmodell = null;
  private PanelBasis panelDetailZutrittsmodell = null;

  private PanelQuery panelQueryZeitmodelltage = null;
  private PanelBasis panelBottomZeitmodelltage = null;
  private PanelSplit panelSplitZeitmodelltage = null;

  private PanelQuery panelQueryZeitmodelltagpause = null;
  private PanelZutrittsmodelltagdetail panelBottomZeitmodelltagpause = null;
  private PanelSplit panelSplitZeitmodelltagpause = null;

  private final static int IDX_PANEL_AUSWAHL = 0;
  private final static int IDX_PANEL_DETAIL = 1;
  private final static int IDX_PANEL_TAGE = 2;
  private final static int IDX_PANEL_TAGEDETAIL = 3;

  private WrapperMenuBar wrapperMenuBar = null;

  private static final String ACTION_SPECIAL_REST =
      "action_special_rest";
  private final String BUTTON_RESTLICHETAGESPEICHERN = PanelBasis.ACTION_MY_OWN_NEW +
      ACTION_SPECIAL_REST;

  public TabbedPaneZutrittsmodell(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsmodell"));

    jbInit();
    initComponents();
  }


  public PanelQuery getPanelQueryZutrittsmodell() {
    return panelQueryZutrittsmodell;
  }


  private void jbInit()
      throws Throwable {

    //Zeitmodellauswahlliste



    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_NEW};
    panelQueryZutrittsmodell = new PanelQuery(
        ZutrittFilterFactory.getInstance().createQTZutrittsmodell(),
        SystemFilterFactory.getInstance().createFKMandantCNr(),
        QueryParameters.UC_ID_ZUTRITTSMODELL,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "auft.title.panel.auswahl"), true);

    panelQueryZutrittsmodell.befuellePanelFilterkriterienDirektUndVersteckte(
        SystemFilterFactory.
        getInstance().createFKDKennung(), null, null);

    addTab(LPMain.getInstance().getTextRespectUISPr(
        "auft.title.panel.auswahl"),
           panelQueryZutrittsmodell);
    panelQueryZutrittsmodell.eventYouAreSelected(false);

    if ( (Integer) panelQueryZutrittsmodell.getSelectedId() != null) {
      getInternalFrameZutritt().setZutrittsmodellDto(DelegateFactory.getInstance().
          getZutrittDelegate().
          zutrittsmodellFindByPrimaryKey( (
              Integer)
                                         panelQueryZutrittsmodell.getSelectedId()));
    }

    //Zeitmodelldetail
    panelDetailZutrittsmodell = new PanelZutrittsmodell(
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("lp.detail"),
        panelQueryZutrittsmodell.getSelectedId());
    addTab(
        LPMain.getInstance().getTextRespectUISPr("lp.detail"),
        panelDetailZutrittsmodell);

    //Zeitmodelltag
    panelQueryZeitmodelltage = new PanelQuery(
        null,
        ZutrittFilterFactory.getInstance().createFKZutrittmodelltag( (Integer)
        panelQueryZutrittsmodell.getSelectedId()),
        QueryParameters.UC_ID_ZUTRITTSMODELLTAG,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.zutrittsmodelltag"), true);

    panelQueryZeitmodelltage.createAndSaveAndShowButton(
        "/com/lp/client/res/goto.png",
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.restlichetagespeichern"),
        BUTTON_RESTLICHETAGESPEICHERN,null);

    panelBottomZeitmodelltage = new PanelZutrittsmodelltag(
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.zutrittsmodelltag"),
        null);

    panelSplitZeitmodelltage = new PanelSplit(
        getInternalFrame(),
        panelBottomZeitmodelltage,
        panelQueryZeitmodelltage,
        280);
    addTab(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsmodelltag"),
           panelSplitZeitmodelltage);
    //Zeitmodelltagpausen
    panelQueryZeitmodelltagpause = new PanelQuery(
        null,
        ZutrittFilterFactory.getInstance().createFKZutrittsmodelltagdetail( (Integer)
        panelQueryZeitmodelltage.getSelectedId()),
        QueryParameters.UC_ID_ZUTRITTSMODELLTAGDETAIL,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.zutrittsmodelltagdetail"), true);
    panelBottomZeitmodelltagpause = new PanelZutrittsmodelltagdetail(
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.zutrittsmodelltagdetail"),
        null);

    if (panelQueryZeitmodelltage.getSelectedId() != null) {

      panelBottomZeitmodelltagpause.zutrittsmodelltagDto = DelegateFactory.getInstance().
          getZutrittDelegate().zutrittsmodelltagFindByPrimaryKey( (Integer)
          panelQueryZeitmodelltage.getSelectedId());

    }
    else {
      panelBottomZeitmodelltagpause.zutrittsmodelltagDto = null;
    }

    panelSplitZeitmodelltagpause = new PanelSplit(
        getInternalFrame(),
        panelBottomZeitmodelltagpause,
        panelQueryZeitmodelltagpause,
        350);
    addTab(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsmodelltagdetail"),
           panelSplitZeitmodelltagpause);

    panelQueryZutrittsmodell.eventYouAreSelected(false);

    //   setJMenuBarArtikel();

    // Itemevents an MEIN Detailpanel senden kann.
    getInternalFrame().addItemChangedListener(this);
    refreshTitle();
    this.addChangeListener(this);

    // Damit D2 einen Aktuellen hat.
///    ItemChangedEvent it = new ItemChangedEvent(panelQueryZeitmodell,
    //                                             ItemChangedEvent.ITEM_CHANGED);
    // /  lPEventItemChanged(it);
  }


  public void setKeyWasForLockMe() {
    Object oKey = panelQueryZutrittsmodell.getSelectedId();

    if (oKey != null) {
      getInternalFrame().setKeyWasForLockMe(oKey.toString());
    }
    else {
      getInternalFrame().setKeyWasForLockMe(null);
    }
  }


  private void refreshTitle() {

    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsmodell"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());
    if (getInternalFrameZutritt().getZutrittsmodellDto() != null) {
      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          getInternalFrameZutritt().getZutrittsmodellDto().getCNr());
    }

  }


  public InternalFrameZutritt getInternalFrameZutritt() {
    return (InternalFrameZutritt) getInternalFrame();
  }


  protected void lPActionEvent(ActionEvent e) {
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryZutrittsmodell) {
        Integer iId = (Integer) panelQueryZutrittsmodell.getSelectedId();
        if (iId != null) {
          setSelectedComponent(panelDetailZutrittsmodell);
        }
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
      String sAspectInfo = ( (ISourceEvent) e.getSource()).getAspect();
      if (sAspectInfo.endsWith(ACTION_SPECIAL_REST)) {
        if(DialogFactory.showModalJaNeinDialog(getInternalFrame(),"Wollen Sie wirklich die restlichen Tage eintragen?","Frage")){
          DelegateFactory.getInstance().getZutrittDelegate().kopiereRestlicheZutrittsmodelltage(getInternalFrameZutritt().getZutrittsmodellDto().getIId());
          panelSplitZeitmodelltage.eventYouAreSelected(false);
        }
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomZeitmodelltage) {
        panelSplitZeitmodelltage.eventYouAreSelected(false);
      }
      if (e.getSource() == panelBottomZeitmodelltagpause) {
        panelSplitZeitmodelltagpause.eventYouAreSelected(false);
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomZeitmodelltage) {
        panelQueryZeitmodelltage.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
      else if (e.getSource() == panelBottomZeitmodelltagpause) {
        panelQueryZeitmodelltagpause.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW)); ;
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

      if (e.getSource() == panelDetailZutrittsmodell) {
        panelQueryZutrittsmodell.clearDirektFilter();
        Object oKey = panelDetailZutrittsmodell.getKeyWhenDetailPanel();

        panelQueryZutrittsmodell.setSelectedId(oKey);
      }

      else if (e.getSource() == panelBottomZeitmodelltage) {

        panelQueryZeitmodelltage.setDefaultFilter(ZutrittFilterFactory.getInstance().
                                                  createFKZutrittmodelltag(
            getInternalFrameZutritt().getZutrittsmodellDto().getIId()));
        Object oKey = panelBottomZeitmodelltage.getKeyWhenDetailPanel();

        if (oKey != null) {
          panelBottomZeitmodelltagpause.zutrittsmodelltagDto = DelegateFactory.
              getInstance().
              getZutrittDelegate().zutrittsmodelltagFindByPrimaryKey( (Integer)
              oKey);
        }
        else {
          panelBottomZeitmodelltagpause.zutrittsmodelltagDto = null;
        }

        panelQueryZeitmodelltage.eventYouAreSelected(false);
        panelQueryZeitmodelltage.setSelectedId(oKey);
        panelSplitZeitmodelltage.eventYouAreSelected(false);

      }
      if (e.getSource() == panelBottomZeitmodelltagpause) {

        panelQueryZeitmodelltagpause.setDefaultFilter(ZutrittFilterFactory.getInstance().
            createFKZutrittsmodelltagdetail(
                (Integer) panelQueryZeitmodelltage.getSelectedId()));
        Object oKey = panelBottomZeitmodelltagpause.getKeyWhenDetailPanel();
        panelQueryZeitmodelltagpause.eventYouAreSelected(false);
        panelQueryZeitmodelltagpause.setSelectedId(oKey);
        panelSplitZeitmodelltagpause.eventYouAreSelected(false);
      }
    }
    else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryZutrittsmodell) {
        if (panelQueryZutrittsmodell.getSelectedId() != null) {
          getInternalFrameZutritt().setKeyWasForLockMe(panelQueryZutrittsmodell.
              getSelectedId() + "");

          //Dto-setzen

          getInternalFrameZutritt().setZutrittsmodellDto(DelegateFactory.getInstance().
              getZutrittDelegate().
              zutrittsmodellFindByPrimaryKey( (
                  Integer) panelQueryZutrittsmodell.getSelectedId()));
          getInternalFrame().setLpTitle(
              InternalFrame.TITLE_IDX_AS_I_LIKE,
              getInternalFrameZutritt().getZutrittsmodellDto().getCNr());

          //Default- Filter vorbesetzten
          panelQueryZeitmodelltage.setDefaultFilter(ZutrittFilterFactory.getInstance().
              createFKZutrittmodelltag(
                  getInternalFrameZutritt().getZutrittsmodellDto().getIId()));
          if (panelQueryZeitmodelltage.getSelectedId() == null) {
            panelBottomZeitmodelltagpause.zutrittsmodelltagDto = null;
          }
          if (panelQueryZutrittsmodell.getSelectedId() == null) {
            getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
          }
          else {
            getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
          }

        }
        else {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
        }

      }
      else if (e.getSource() == panelQueryZeitmodelltage) {

        panelQueryZeitmodelltagpause.setDefaultFilter(ZutrittFilterFactory.getInstance().
            createFKZutrittsmodelltagdetail( (
                Integer)
                                            panelQueryZeitmodelltage.getSelectedId()));
        if (panelQueryZeitmodelltage.getSelectedId() != null) {

          panelBottomZeitmodelltagpause.zutrittsmodelltagDto = DelegateFactory.
              getInstance().
              getZutrittDelegate().zutrittsmodelltagFindByPrimaryKey( (Integer)
              panelQueryZeitmodelltage.getSelectedId());

        }
        else {
          panelBottomZeitmodelltagpause.zutrittsmodelltagDto = null;
        }

        Integer iId = (Integer) panelQueryZeitmodelltage.getSelectedId();
        panelBottomZeitmodelltage.setKeyWhenDetailPanel(iId);
        panelBottomZeitmodelltage.eventYouAreSelected(false);
        panelQueryZeitmodelltage.updateButtons();
      }
      else if (e.getSource() == panelQueryZeitmodelltagpause) {

        Integer iId = (Integer) panelQueryZeitmodelltagpause.getSelectedId();
        panelBottomZeitmodelltagpause.setKeyWhenDetailPanel(iId);
        panelBottomZeitmodelltagpause.eventYouAreSelected(false);
        panelQueryZeitmodelltagpause.updateButtons();
      }

    }

    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      //aktiviere ein QP ...
      if (e.getSource() == panelDetailZutrittsmodell) {
        this.setSelectedComponent(panelQueryZutrittsmodell);
        setKeyWasForLockMe();
        panelQueryZutrittsmodell.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZeitmodelltage) {
        setKeyWasForLockMe();
        if (panelBottomZeitmodelltage.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryZeitmodelltage.getId2SelectAfterDelete();
          panelQueryZeitmodelltage.setSelectedId(oNaechster);
        }
        panelSplitZeitmodelltage.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZeitmodelltagpause) {
        setKeyWasForLockMe();
        if (panelBottomZeitmodelltagpause.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryZeitmodelltagpause.getId2SelectAfterDelete();
          panelQueryZeitmodelltagpause.setSelectedId(oNaechster);
        }
        panelSplitZeitmodelltagpause.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
      super.lPEventItemChanged(e);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryZutrittsmodell) {
        if (panelQueryZutrittsmodell.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelDetailZutrittsmodell.eventActionNew(null, true, false);
        setSelectedComponent(panelDetailZutrittsmodell);
      }
      else if (e.getSource() == panelQueryZeitmodelltage) {
        panelBottomZeitmodelltage.eventActionNew(e, true, false);
        panelBottomZeitmodelltage.eventYouAreSelected(false);
        this.setSelectedComponent(panelSplitZeitmodelltage);

      }
      else if (e.getSource() == panelQueryZeitmodelltagpause) {
        panelBottomZeitmodelltagpause.eventActionNew(e, true, false);
        panelBottomZeitmodelltagpause.eventYouAreSelected(false);
        this.setSelectedComponent(panelSplitZeitmodelltagpause);
      }
    }

  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);

    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANEL_AUSWAHL) {
      panelQueryZutrittsmodell.eventYouAreSelected(false);
      if (panelQueryZutrittsmodell.getSelectedId() == null) {
        getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
      }
      panelQueryZutrittsmodell.updateButtons();
    }
    else if (selectedIndex == IDX_PANEL_DETAIL) {
      panelDetailZutrittsmodell.eventYouAreSelected(false);
    }
    else if (selectedIndex == IDX_PANEL_TAGE) {
      if (getInternalFrameZutritt().getZutrittsmodellDto() != null) {
        panelQueryZeitmodelltage.setDefaultFilter(ZutrittFilterFactory.getInstance().
                                                  createFKZutrittmodelltag(
            getInternalFrameZutritt().getZutrittsmodellDto().getIId()));
      }

      panelSplitZeitmodelltage.eventYouAreSelected(false);
      panelQueryZeitmodelltage.updateButtons();
    }
    else if (selectedIndex == IDX_PANEL_TAGEDETAIL) {

      panelQueryZeitmodelltagpause.setDefaultFilter(ZutrittFilterFactory.getInstance().
          createFKZutrittsmodelltagdetail( (Integer)
                                          panelQueryZeitmodelltage.getSelectedId()));
      panelSplitZeitmodelltagpause.eventYouAreSelected(false);
      panelQueryZeitmodelltagpause.updateButtons();
    }
  }


  public javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {
      wrapperMenuBar = new WrapperMenuBar(this);
    }
    return wrapperMenuBar;
  }

}
