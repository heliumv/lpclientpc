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
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class TabbedPaneZutrittgrunddaten
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryZutrittscontroller = null;
  private PanelBasis panelSplitZutrittscontroller = null;
  private PanelBasis panelBottomZutrittscontroller = null;

  private PanelQuery panelQueryZutrittsobjekt = null;
  private PanelBasis panelSplitZutrittsobjekt = null;
  private PanelBasis panelBottomZutrittsobjekt = null;

  private PanelQuery panelQueryZutrittsobjektverwendung = null;
  private PanelBasis panelSplitZutrittsobjektverwendung = null;
  private PanelZutrittsobjektverwendung panelBottomZutrittsobjektverwendung = null;

  private final static int IDX_PANEL_ZUTRITTSCONTROLLER = 0;
  private final static int IDX_PANEL_ZUTRITTSOBJEKT = 1;
  private final static int IDX_PANEL_ZUTRITTSOBJEKTVERWENDUNG = 2;

  private WrapperMenuBar wrapperMenuBar = null;

  public TabbedPaneZutrittgrunddaten(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("pers.title.tab.grunddaten"));

    jbInit();
    initComponents();
  }


  public InternalFrameZutritt getInternalFrameZutritt() {
    return (InternalFrameZutritt) getInternalFrame();
  }


  private void createZutrittscontroller()
      throws Throwable {
    if (panelSplitZutrittscontroller == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};

      panelQueryZutrittscontroller = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_ZUTRITTSCONTROLLER,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zutritt.zutrittscontroller"), true);

      panelQueryZutrittscontroller.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
          getInstance().createFKDKennung(),
          SystemFilterFactory.
          getInstance().createFKDBezeichnung());

      panelBottomZutrittscontroller = new PanelZutrittcontroller(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zutritt.zutrittscontroller"), null);
      panelSplitZutrittscontroller = new PanelSplit(
          getInternalFrame(),
          panelBottomZutrittscontroller,
          panelQueryZutrittscontroller,
          350);

      setComponentAt(IDX_PANEL_ZUTRITTSCONTROLLER, panelSplitZutrittscontroller);
    }
  }


  private void createZutrittsobjekt()
      throws Throwable {
    if (panelSplitZutrittsobjekt == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};

      panelQueryZutrittsobjekt = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_ZUTRITTSOBJEKT,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zutritt.zutrittsobjekt"), true);
      panelQueryZutrittsobjekt.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
          getInstance().createFKDKennung(),
          SystemFilterFactory.
          getInstance().createFKDBezeichnung());

      panelBottomZutrittsobjekt = new PanelZutrittsobjekt(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zutritt.zutrittsobjekt"),
          null);

      panelSplitZutrittsobjekt = new PanelSplit(
          getInternalFrame(),
          panelBottomZutrittsobjekt,
          panelQueryZutrittsobjekt,
          260);

      setComponentAt(IDX_PANEL_ZUTRITTSOBJEKT, panelSplitZutrittsobjekt);
    }
  }


  private void createZutrittsobjektverwendung(Integer key)
      throws Throwable {
    if (panelSplitZutrittsobjektverwendung == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};

      panelQueryZutrittsobjektverwendung = new PanelQuery(
          null,
          ZutrittFilterFactory.getInstance().createFKZutrittsobjektverwendung(key),
          QueryParameters.UC_ID_ZUTRITTSOBJEKTVERWENDUNG,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zutritt.zutrittsobjektverwendung"), true);

      panelBottomZutrittsobjektverwendung = new PanelZutrittsobjektverwendung(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zutritt.zutrittsobjektverwendung"),
          null);

      if (panelQueryZutrittsobjekt.getSelectedId() != null) {
        panelBottomZutrittsobjektverwendung.zutrittsobjektDto = DelegateFactory.
            getInstance().
            getZutrittDelegate().zutrittsobjektFindByPrimaryKey( (Integer)
            panelQueryZutrittsobjekt.getSelectedId());
      }
      else {
        panelBottomZutrittsobjektverwendung.zutrittsobjektDto = null;
      }

      panelSplitZutrittsobjektverwendung = new PanelSplit(
          getInternalFrame(),
          panelBottomZutrittsobjektverwendung,
          panelQueryZutrittsobjektverwendung,
          300);

      setComponentAt(IDX_PANEL_ZUTRITTSOBJEKTVERWENDUNG,
                     panelSplitZutrittsobjektverwendung);

    }
    else {
      //filter refreshen.
      panelQueryZutrittsobjektverwendung.setDefaultFilter(
          ZutrittFilterFactory.getInstance().createFKZutrittsobjektverwendung(key));
    }

  }


  private void jbInit()
      throws Throwable {
    //Kollektiv

    //1 tab oben: QP1 PartnerFLR; lazy loading
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittscontroller"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittscontroller"),
        IDX_PANEL_ZUTRITTSCONTROLLER);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsobjekt"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsobjekt"),
        IDX_PANEL_ZUTRITTSOBJEKT);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsobjektverwendung"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsobjektverwendung"),
        IDX_PANEL_ZUTRITTSOBJEKTVERWENDUNG);

    createZutrittscontroller();

    // Itemevents an MEIN Detailpanel senden kann.
    this.addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);

  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryZutrittscontroller) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        panelBottomZutrittscontroller.setKeyWhenDetailPanel(key);
        panelBottomZutrittscontroller.eventYouAreSelected(false);
        panelQueryZutrittscontroller.updateButtons();
      }
      else if (e.getSource() == panelQueryZutrittsobjekt) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();

        createZutrittsobjektverwendung( (Integer) key);

        if (panelQueryZutrittsobjekt.getSelectedId() != null) {
          panelBottomZutrittsobjektverwendung.zutrittsobjektDto = DelegateFactory.
              getInstance().
              getZutrittDelegate().zutrittsobjektFindByPrimaryKey( (Integer)
              panelQueryZutrittsobjekt.getSelectedId());
        }
        else {
          panelBottomZutrittsobjektverwendung.zutrittsobjektDto = null;
        }

        panelBottomZutrittsobjekt.setKeyWhenDetailPanel(key);
        panelBottomZutrittsobjekt.eventYouAreSelected(false);
        panelQueryZutrittsobjekt.updateButtons();
      }
      else if (e.getSource() == panelQueryZutrittsobjektverwendung) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        panelBottomZutrittsobjektverwendung.setKeyWhenDetailPanel(key);
        panelBottomZutrittsobjektverwendung.eventYouAreSelected(false);
        panelQueryZutrittsobjektverwendung.updateButtons();
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
      panelSplitZutrittscontroller.eventYouAreSelected(false);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryZutrittscontroller) {
        panelBottomZutrittscontroller.eventActionNew(e, true, false);
        panelBottomZutrittscontroller.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelQueryZutrittsobjekt) {
        panelBottomZutrittsobjekt.eventActionNew(e, true, false);
        panelBottomZutrittsobjekt.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelQueryZutrittsobjektverwendung) {
        panelBottomZutrittsobjektverwendung.eventActionNew(e, true, false);
        panelBottomZutrittsobjektverwendung.eventYouAreSelected(false);
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomZutrittscontroller) {
        panelQueryZutrittscontroller.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
      }
      else if (e.getSource() == panelBottomZutrittsobjekt) {
        panelQueryZutrittsobjekt.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
      else if (e.getSource() == panelBottomZutrittsobjekt) {
        panelQueryZutrittsobjektverwendung.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomZutrittscontroller) {
        panelSplitZutrittscontroller.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZutrittsobjekt) {
        panelSplitZutrittsobjekt.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZutrittsobjektverwendung) {
        panelSplitZutrittsobjektverwendung.eventYouAreSelected(false);
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomZutrittscontroller) {
        Object oKey = panelBottomZutrittscontroller.getKeyWhenDetailPanel();
        panelQueryZutrittscontroller.eventYouAreSelected(false);
        panelQueryZutrittscontroller.setSelectedId(oKey);
        panelSplitZutrittscontroller.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZutrittsobjekt) {
        Object oKey = panelBottomZutrittsobjekt.getKeyWhenDetailPanel();
        panelQueryZutrittsobjekt.eventYouAreSelected(false);
        panelQueryZutrittsobjekt.setSelectedId(oKey);
        panelSplitZutrittsobjekt.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZutrittsobjektverwendung) {
        Object oKey = panelBottomZutrittsobjektverwendung.getKeyWhenDetailPanel();
        panelQueryZutrittsobjektverwendung.eventYouAreSelected(false);
        panelQueryZutrittsobjektverwendung.setSelectedId(oKey);
        panelSplitZutrittsobjektverwendung.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomZutrittscontroller) {
        Object oKey = panelQueryZutrittscontroller.getSelectedId();
        if (oKey != null) {
          getInternalFrame().setKeyWasForLockMe(oKey.toString());
        }
        else {
          getInternalFrame().setKeyWasForLockMe(null);
        }
        if (panelBottomZutrittscontroller.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryZutrittscontroller.getId2SelectAfterDelete();
          panelQueryZutrittscontroller.setSelectedId(oNaechster);
        }
        panelSplitZutrittscontroller.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZutrittsobjekt) {
        Object oKey = panelQueryZutrittsobjekt.getSelectedId();
        if (oKey != null) {
          getInternalFrame().setKeyWasForLockMe(oKey.toString());
        }
        else {
          getInternalFrame().setKeyWasForLockMe(null);
        }
        if (panelBottomZutrittsobjekt.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryZutrittsobjekt.getId2SelectAfterDelete();
          panelQueryZutrittsobjekt.setSelectedId(oNaechster);
        }
        panelSplitZutrittsobjekt.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZutrittsobjektverwendung) {
        Object oKey = panelQueryZutrittsobjektverwendung.getSelectedId();
        if (oKey != null) {
          getInternalFrame().setKeyWasForLockMe(oKey.toString());
        }
        else {
          getInternalFrame().setKeyWasForLockMe(null);
        }
        if (panelBottomZutrittsobjektverwendung.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryZutrittsobjektverwendung.getId2SelectAfterDelete();
          panelQueryZutrittsobjektverwendung.setSelectedId(oNaechster);
        }
        panelSplitZutrittsobjektverwendung.eventYouAreSelected(false);
      }

    }

  }


  private void refreshTitle() {

    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
        LPMain.getInstance().getTextRespectUISPr(
            "pers.title.tab.grunddaten"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());
    getInternalFrame().setLpTitle(
        3, "");
     }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANEL_ZUTRITTSCONTROLLER) {
      createZutrittscontroller();
      panelSplitZutrittscontroller.eventYouAreSelected(false);
      panelQueryZutrittscontroller.updateButtons();
    }
    else if (selectedIndex == IDX_PANEL_ZUTRITTSOBJEKT) {
      createZutrittsobjekt();
      panelSplitZutrittsobjekt.eventYouAreSelected(false);
      panelQueryZutrittsobjekt.updateButtons();
    }
    else if (selectedIndex == IDX_PANEL_ZUTRITTSOBJEKTVERWENDUNG) {
      if (panelQueryZutrittsobjekt == null) {
        createZutrittsobjekt();
        panelSplitZutrittsobjekt.eventYouAreSelected(false);
      }
      createZutrittsobjektverwendung( (Integer) panelQueryZutrittsobjekt.getSelectedId());
      panelSplitZutrittsobjektverwendung.eventYouAreSelected(false);
      panelQueryZutrittsobjektverwendung.updateButtons();

    }

  }


  protected void lPActionEvent(java.awt.event.ActionEvent e) {

  }


  public javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {
      wrapperMenuBar = new WrapperMenuBar(this);
    }
    return wrapperMenuBar;
  }

}
