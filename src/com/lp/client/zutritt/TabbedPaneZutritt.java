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


import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
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
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class TabbedPaneZutritt
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryZutrittsklasse = null;
  private PanelBasis panelDetailZutrittsklasse = null;

  private PanelQuery panelQueryZutrittsklasseobjekt = null;
  private PanelBasis panelBottomZutrittsklasseobjekt = null;
  private PanelSplit panelSplitZutrittsklasseobjekt = null;


  private final static int IDX_PANEL_AUSWAHL = 0;
  private final static int IDX_PANEL_DETAIL = 1;
  private final static int IDX_PANEL_ZUTRITTSKLASSEOBJEKT = 2;


  private final String MENUE_JOURNAL_ACTION_ZUTRITTSDEFINITION =
      "MENUE_JOURNAL_ACTION_ZUTRITTSDEFINITION";
  private WrapperMenuBar wrapperMenuBar = null;

  public TabbedPaneZutritt(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("pers.zutritt.modulname"));

    jbInit();
    initComponents();
  }


  public PanelQuery getPanelQueryZutrittsklasse() {
    return panelQueryZutrittsklasse;
  }


  private void createAuswahl()
      throws Throwable {
    if (panelQueryZutrittsklasse == null) {
      //Artikelauswahlliste
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};

      FilterKriterium[] fk = ZutrittFilterFactory.getInstance().createFKZutrittsklasse(
          getInternalFrameZutritt().isBIstHauptmandant());

      panelQueryZutrittsklasse = new PanelQuery(
          null,
          fk,
          QueryParameters.UC_ID_ZUTRITTSKLASSE,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "lp.auswahl"), true);

      panelQueryZutrittsklasse.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
          getInstance().createFKDKennung(),SystemFilterFactory.
             getInstance().createFKDBezeichnung());

      setComponentAt(IDX_PANEL_AUSWAHL, panelQueryZutrittsklasse);
    }
  }


  private void createDetail(Integer key)
      throws Throwable {
    if (panelDetailZutrittsklasse == null) {
      panelDetailZutrittsklasse = new PanelZutrittsklasse(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.detail"),
          key);
      setComponentAt(IDX_PANEL_DETAIL, panelDetailZutrittsklasse);
    }
  }



  private void createZutrittsklasseobjekt(
      Integer key)
      throws Throwable {

    if (panelQueryZutrittsklasseobjekt == null) {
      String[] aWhichStandardButtonIUse = {
          PanelBasis.ACTION_NEW
      };
      panelQueryZutrittsklasseobjekt = new PanelQuery(
          null,
          ZutrittFilterFactory.getInstance().createFKZutrittsklasseobjekt(key),
          QueryParameters.UC_ID_ZUTRITTSKLASSEOBJEKT,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zutritt.zutrittsklasseobjekt"), true);
      panelBottomZutrittsklasseobjekt = new PanelZutrittklasseobjekt(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zutritt.zutrittsklasseobjekt"),
          null);

      panelQueryZutrittsklasseobjekt.befuellePanelFilterkriterienDirekt(ZutrittFilterFactory.
             getInstance().createFKDZutrittsklasseobjektModell(),
             ZutrittFilterFactory.
             getInstance().createFKDZutrittsklasseobjektObjekt());

      panelSplitZutrittsklasseobjekt = new PanelSplit(
          getInternalFrame(),
          panelBottomZutrittsklasseobjekt,
          panelQueryZutrittsklasseobjekt,
          350);

      setComponentAt(IDX_PANEL_ZUTRITTSKLASSEOBJEKT, panelSplitZutrittsklasseobjekt);
    }
    else {
      //filter refreshen.
      panelQueryZutrittsklasseobjekt.setDefaultFilter(
          ZutrittFilterFactory.getInstance().createFKZutrittsklasseobjekt(key));
    }
  }


  /**
   * jbInit
   *
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
        IDX_PANEL_AUSWAHL);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.detail"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.detail"),
        IDX_PANEL_DETAIL);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsklasseobjekt"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsklasseobjekt"),
        IDX_PANEL_ZUTRITTSKLASSEOBJEKT);


    createAuswahl();

    if ( (Integer) panelQueryZutrittsklasse.getSelectedId() != null) {
      getInternalFrameZutritt().setZutrittsklasseDto(DelegateFactory.getInstance().
          getZutrittDelegate().
          zutrittsklasseFindByPrimaryKey( (
              Integer)
                                         panelQueryZutrittsklasse.getSelectedId()));
    }

    // wenn es fuer das tabbed pane noch keinen eintrag gibt, die
    // restlichen panel deaktivieren
    if (panelQueryZutrittsklasse.getSelectedId() == null) {
      getInternalFrame().enableAllPanelsExcept(false);
    }

    //damit D2 einen aktuellen hat.
    ItemChangedEvent it = new ItemChangedEvent(panelQueryZutrittsklasse,
                                               ItemChangedEvent.ITEM_CHANGED);
    lPEventItemChanged(it);

    this.addChangeListener(this);
    this.getInternalFrame().addItemChangedListener(this);

  }


  public void setKeyWasForLockMe() {
    Object oKey = panelQueryZutrittsklasse.getSelectedId();

    if (oKey != null) {
      getInternalFrame().setKeyWasForLockMe(oKey.toString());
    }
    else {
      getInternalFrame().setKeyWasForLockMe(null);
    }
  }


  public void refreshTitle() {

    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.modulname"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());

    if (getInternalFrameZutritt().getZutrittsklasseDto() != null) {
      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          getInternalFrameZutritt().getZutrittsklasseDto().getBezeichnung());
    } else {
      getInternalFrame().setLpTitle(
         InternalFrame.TITLE_IDX_AS_I_LIKE,
         "");

    }
  }


  public InternalFrameZutritt getInternalFrameZutritt() {
    return (InternalFrameZutritt) getInternalFrame();
  }


  protected void lPActionEvent(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_ZUTRITTSDEFINITION)) {

      String add2Title = LPMain.getInstance().getTextRespectUISPr(
          "pers.zutritt.definition");
       getInternalFrame().showReportKriterien(new ReportZutrittsdefinition(
           getInternalFrameZutritt(), add2Title));
    }
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryZutrittsklasse) {
        Integer iId = (Integer) panelQueryZutrittsklasse.getSelectedId();
        if (iId != null) {
          setSelectedComponent(panelDetailZutrittsklasse);
          panelDetailZutrittsklasse.eventYouAreSelected(false);
        }
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelDetailZutrittsklasse) {
        panelQueryZutrittsklasse.clearDirektFilter();
        Object oKey = panelDetailZutrittsklasse.getKeyWhenDetailPanel();

        panelQueryZutrittsklasse.setSelectedId(oKey);
      }
      else if (e.getSource() == panelBottomZutrittsklasseobjekt) {
        Object oKey = panelBottomZutrittsklasseobjekt.getKeyWhenDetailPanel();
        panelQueryZutrittsklasseobjekt.eventYouAreSelected(false);
        panelQueryZutrittsklasseobjekt.setSelectedId(oKey);
        panelSplitZutrittsklasseobjekt.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryZutrittsklasse) {
        if (panelQueryZutrittsklasse.getSelectedId() != null) {
          getInternalFrameZutritt().setKeyWasForLockMe(panelQueryZutrittsklasse.
              getSelectedId() + "");
          createDetail( (Integer) panelQueryZutrittsklasse.
                       getSelectedId());
          panelDetailZutrittsklasse.setKeyWhenDetailPanel(panelQueryZutrittsklasse.
              getSelectedId());
          getInternalFrameZutritt().setZutrittsklasseDto(
              DelegateFactory.getInstance().getZutrittDelegate().
              zutrittsklasseFindByPrimaryKey( (Integer) panelQueryZutrittsklasse.
                                             getSelectedId()));

          if (getInternalFrameZutritt().getZutrittsklasseDto() != null) {
            getInternalFrame().setLpTitle(
                InternalFrame.TITLE_IDX_AS_I_LIKE,
                getInternalFrameZutritt().getZutrittsklasseDto().getCNr());
          }

          getInternalFrame().enableAllOberePanelsExceptMe(
              this, IDX_PANEL_AUSWAHL, ( (ISourceEvent) e.getSource()).getIdSelected() != null);
        }
        else {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
        }

      }
      else if (e.getSource() == panelQueryZutrittsklasseobjekt) {
        Integer iId = (Integer) panelQueryZutrittsklasseobjekt.getSelectedId();
        panelBottomZutrittsklasseobjekt.setKeyWhenDetailPanel(iId);
        panelBottomZutrittsklasseobjekt.eventYouAreSelected(false);
        panelQueryZutrittsklasseobjekt.updateButtons();
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomZutrittsklasseobjekt) {
        panelQueryZutrittsklasseobjekt.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      //aktiviere ein QP ...
      if (e.getSource() == panelDetailZutrittsklasse) {
        this.setSelectedComponent(panelQueryZutrittsklasse);
        setKeyWasForLockMe();
        panelQueryZutrittsklasse.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZutrittsklasseobjekt) {
        setKeyWasForLockMe();
        if (panelBottomZutrittsklasseobjekt.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryZutrittsklasseobjekt.getId2SelectAfterDelete();
          panelQueryZutrittsklasseobjekt.setSelectedId(oNaechster);
        }

        panelSplitZutrittsklasseobjekt.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
      super.lPEventItemChanged(e);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomZutrittsklasseobjekt) {
        panelSplitZutrittsklasseobjekt.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryZutrittsklasse) {

        createDetail(null);
        panelDetailZutrittsklasse.eventActionNew(e, true, false);
        this.setSelectedComponent(panelDetailZutrittsklasse);
        getInternalFrame().setLpTitle(
                InternalFrame.TITLE_IDX_AS_I_LIKE,
         "");
      }
      else if (e.getSource() == panelQueryZutrittsklasseobjekt) {

        panelBottomZutrittsklasseobjekt.eventActionNew(e, true, false);
        panelBottomZutrittsklasseobjekt.eventYouAreSelected(false);
        this.setSelectedComponent(panelSplitZutrittsklasseobjekt);

      }

    }

  }


  /**
   * Behandle ChangeEvent; zB Tabwechsel oben.
   *
   * @param e ChangeEvent
   * @throws Throwable
   */
  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {

    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANEL_AUSWAHL) {
      createAuswahl();
      panelQueryZutrittsklasse.eventYouAreSelected(false);
      panelQueryZutrittsklasse.updateButtons();
    }
    else if (selectedIndex == IDX_PANEL_DETAIL) {
      Integer key = null;
      if (getInternalFrameZutritt().getZutrittsklasseDto() != null) {
        key = getInternalFrameZutritt().getZutrittsklasseDto().getIId();
      }
      createDetail(key);
      panelDetailZutrittsklasse.eventYouAreSelected(false);
    }
    else if (selectedIndex == IDX_PANEL_ZUTRITTSKLASSEOBJEKT) {
      createZutrittsklasseobjekt(getInternalFrameZutritt().getZutrittsklasseDto().getIId());
      panelSplitZutrittsklasseobjekt.eventYouAreSelected(false);
      panelQueryZutrittsklasseobjekt.updateButtons();
    }
  }


  public javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {

      wrapperMenuBar = new WrapperMenuBar(this);

      JMenuItem menuItemZutrittsdefinition = new JMenuItem(LPMain.getInstance().
          getTextRespectUISPr("pers.zutritt.definition"));

      menuItemZutrittsdefinition.addActionListener(this);
      menuItemZutrittsdefinition.setActionCommand(MENUE_JOURNAL_ACTION_ZUTRITTSDEFINITION);

      JMenu journal = (JMenu) wrapperMenuBar.getComponent(
          WrapperMenuBar.MENU_JOURNAL);
      journal.add(menuItemZutrittsdefinition);

    }
    return wrapperMenuBar;
  }

}
