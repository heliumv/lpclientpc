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
package com.lp.client.system;


import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich alle Systemtabellen.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>18.02.05</I></p>
 *
 * @author josef erlinger
 *
 * @version $Revision: 1.3 $
 */
public class TabbedPaneSystem
    extends TabbedPane
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelBasis panelOrtSP1 = null;
  private PanelBasis panelOrtBottomD1 = null;
  private PanelQuery panelOrtQP1 = null;

  private PanelBasis panelLandSP2 = null;
  private PanelBasis panelLandBottomD2 = null;
  private PanelQuery panelLandQP2 = null;

  private PanelSplit panelLandPlzOrtSP3 = null;
  private PanelLandPlzOrt panelLandPlzOrtBottomD3 = null;
  private PanelQuery panelLandPlzOrtQP3 = null;

  private WrapperMenuBar wrapperMenuBar = null;

  //Reihenfolge der Panels
  private static final int IDX_PANEL_ORT = 0;
  private static final int IDX_PANEL_LAND = 1;
  public static final int IDX_PANEL_LANDPLZORT = 2;

  public TabbedPaneSystem(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getTextRespectUISPr("lp.landplzort"));
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    /**
     * Ortpanel lazy loading*/
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.ort"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.ort.tooltip"),
        IDX_PANEL_ORT);

    /**
     * Landpanel lazy loading*/
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.land"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.land.tooltip"),
        IDX_PANEL_LAND);

    /**
     * LandPlzOrtpanel lazy loading*/
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.landplzort"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.landplzort.tooltip"),
        IDX_PANEL_LANDPLZORT);

    //Default fuer OrtSP1 (wirklich angelegt)
    refreshOrtSP1();

    //damit D1 einen aktuellen hat. (kuenstliches abfeuern)
    ItemChangedEvent it = new ItemChangedEvent(panelOrtQP1, ItemChangedEvent.ITEM_CHANGED);
    lPEventItemChanged(it);

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }

  protected void lPActionEvent(ActionEvent e) {
    // nothing here.
  }


  /**
   * changed: hier wird alles durchlaufen und abgefragt zb. save event, discard event,
   *  wenn ein panel refresht werden soll...
   *
   * @param eI ItemChangedEvent
   * @throws Throwable
   */
  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelOrtQP1) {
        Integer iId = (Integer) panelOrtQP1.getSelectedId();
        //key 1; IF
        getInternalFrame().setKeyWasForLockMe(iId + "");
        //key2; PBottom
        panelOrtBottomD1.setKeyWhenDetailPanel(iId);
        panelOrtBottomD1.eventYouAreSelected(false);

        //im QP die Buttons in den Zustand nolocking/save setzen.
        panelOrtQP1.updateButtons(
            panelOrtBottomD1.getLockedstateDetailMainKey());

      }
      else if (e.getSource() == panelLandPlzOrtQP3) {
        Integer iId = (Integer) panelLandPlzOrtQP3.getSelectedId();
        //key 1; IF
        getInternalFrame().setKeyWasForLockMe(iId + "");
        //key 2; PB
        panelLandPlzOrtBottomD3.setKeyWhenDetailPanel(iId);
        panelLandPlzOrtBottomD3.eventYouAreSelected(false);

        //im QP die Buttons in den Zustand nolocking/save setzen.
        panelLandPlzOrtQP3.updateButtons(
            panelLandPlzOrtBottomD3.getLockedstateDetailMainKey());

      }
      else if (e.getSource() == panelLandQP2) {
        Integer iId = (Integer) panelLandQP2.getSelectedId();
        //key 1; IF
        getInternalFrame().setKeyWasForLockMe(iId + "");
        //key2; PB
        panelLandBottomD2.setKeyWhenDetailPanel(iId);
        panelLandBottomD2.eventYouAreSelected(false);

        //im QP die Buttons in den Zustand nolocking/save setzen.
        panelLandQP2.updateButtons(
            panelLandBottomD2.getLockedstateDetailMainKey());

      }

    }
    else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
      // hier kommt man nach upd im D bei einem 1:n hin.
      if (eI.getSource() == this.panelOrtBottomD1) {
        //im QP die Buttons in den Zustand neu setzen.
        this.panelOrtQP1.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
      else if (eI.getSource() == this.panelLandBottomD2) {
        //im QP die Buttons in den Zustand neu setzen.
        this.panelLandQP2.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
        ;
      }
      else if (eI.getSource() == this.panelLandPlzOrtBottomD3) {
        //im QP die Buttons in den Zustand neu setzen.
        this.panelLandPlzOrtQP3.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
        ;
      }

    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      // wir landen hier bei der Abfolge Button Aendern -> xx -> Button Discard
      if (e.getSource() == panelLandBottomD2) {
        panelLandSP2.eventYouAreSelected(false); // das 1:n Panel neu aufbauen
      }
      else if (e.getSource() == panelOrtBottomD1) {
        panelOrtSP1.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelLandPlzOrtBottomD3) {
        panelLandPlzOrtSP3.eventYouAreSelected(false);
      }

    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      //selektiert nach save: 0 Wir landen hier nach Button Save
      if (e.getSource() == panelLandBottomD2) {

        Object oKey = panelLandBottomD2.getKeyWhenDetailPanel();
        panelLandQP2.eventYouAreSelected(false);
        panelLandQP2.setSelectedId(oKey);
        panelLandSP2.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelOrtBottomD1) {
        Object oKey = panelOrtBottomD1.getKeyWhenDetailPanel();
        panelOrtQP1.eventYouAreSelected(false);
        panelOrtQP1.setSelectedId(oKey);
        panelOrtSP1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelLandPlzOrtBottomD3) {
        //Ims VF 1676 directfilter
        panelLandPlzOrtQP3.clearDirektFilter();
        Object oKey = panelLandPlzOrtBottomD3.getKeyWhenDetailPanel();
        panelLandPlzOrtQP3.eventYouAreSelected(false);
        panelLandPlzOrtQP3.setSelectedId(oKey);
        panelLandPlzOrtSP3.eventYouAreSelected(false);
      }

    }
    // wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelOrtBottomD1) {
        Object oKey = panelOrtQP1.getSelectedId();
        //holt sich alten key wieder
        getInternalFrame().setKeyWasForLockMe(oKey == null ? null : oKey + "");
        panelOrtSP1.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (e.getSource() == panelLandBottomD2) {
        panelLandSP2.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (e.getSource() == panelLandPlzOrtBottomD3) {
        //bei einem Neu im 1:n Panel wurde der KeyForLockMe ueberschrieben
        //setKeyWasForLockMeLandPlzOrt();
        panelLandPlzOrtSP3.eventYouAreSelected(false); //refresh auf das gesamte 1:n panel
      }

    }

    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      // hier wird reingesprungen wenn new button gedrueckt wird (hier 1:n panel)
      //New ...
      if (eI.getSource() == panelLandQP2) {
        //im panelLandQP3 auf new gedrueckt.
        //jetzt in's richtige panel mit new.
        panelLandBottomD2.eventActionNew(e, true, false);
        //jetzt eventYouAreSelected ausloesen wegen naechster zeile.
        panelLandBottomD2.eventYouAreSelected(false);
        //auch ui maessig, fuehrt NICHT zu eventYouAreSelected
        setSelectedComponent(panelLandSP2);
      }

      else if (eI.getSource() == panelOrtQP1) {
        //im panelOrtQP2 auf new gedrueckt.
        //jetzt in's richtige panel mit new.
        panelOrtBottomD1.eventActionNew(e, true, false);
        //jetzt eventYouAreSelected ausloesen wegen naechster zeile.
        panelOrtBottomD1.eventYouAreSelected(false);
        //auch ui maessig, fuehrt NICHT zu eventYouAreSelected
        setSelectedComponent(panelOrtSP1);
      }

      else if (eI.getSource() == panelLandPlzOrtQP3) {
        //im panelOrtQP2 auf new gedrueckt.
        //jetzt in's richtige panel mit new.
        panelLandPlzOrtBottomD3.eventActionNew(e, true, false);
        //jetzt eventYouAreSelected ausloesen wegen naechster zeile.
        panelLandPlzOrtBottomD3.eventYouAreSelected(false);
        //auch ui maessig, fuehrt NICHT zu eventYouAreSelected
        setSelectedComponent(panelLandPlzOrtSP3);
      }
    }
  }


  public void setKeyWasForLockMeLandPlzOrt()
      throws Throwable {
    Object oKey = panelLandPlzOrtQP3.getSelectedId();

    if (oKey != null) {
      panelLandPlzOrtBottomD3.setLandplzortDto(DelegateFactory.getInstance().
                                               getSystemDelegate().
                                               landplzortFindByPrimaryKey( (
          Integer) oKey));
      getInternalFrame().setKeyWasForLockMe(oKey.toString());
    }
    else {
      getInternalFrame().setKeyWasForLockMe(null);
    }
  }

  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    switch (selectedIndex) {
      case IDX_PANEL_LANDPLZORT:
        refreshLandPlzOrtSP3();
        panelLandPlzOrtQP3.eventYouAreSelected(false);

        // im QP die Buttons setzen.
        panelLandPlzOrtQP3.updateButtons();

        break;

      case IDX_PANEL_ORT:
        refreshOrtSP1();
        panelOrtQP1.eventYouAreSelected(false);

        //im QP die Buttons setzen.
        panelOrtQP1.updateButtons(
            panelOrtBottomD1.getLockedstateDetailMainKey());

        break;

      case IDX_PANEL_LAND:
        refreshLandSP2();
        panelLandQP2.eventYouAreSelected(false);

        // im QP die Buttons setzen.
        panelLandQP2.updateButtons(
            panelLandBottomD2.getLockedstateDetailMainKey());

        break;

    }
  }


  public void refreshLandPlzOrtSP3()
      throws Throwable {
    if (panelLandPlzOrtSP3 == null) {
      QueryType[] qtOrt = null;
      FilterKriterium[] defaultFilterOrt = null;

      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};

      panelLandPlzOrtQP3 = new PanelQuery(qtOrt,
                                          defaultFilterOrt,
                                          QueryParameters.UC_ID_LANDPLZORT,
                                          aWhichButtonIUse,
                                          getInternalFrame(),
                                          LPMain.getInstance().getTextRespectUISPr(
                                              "lp.landplzort"),
                                          true); // liste refresh wenn lasche geklickt wurde

      panelLandPlzOrtBottomD3 = new PanelLandPlzOrt(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.landplzort"),
          null);

      panelLandPlzOrtSP3 = new PanelSplit(
          getInternalFrame(),
          panelLandPlzOrtBottomD3,
          panelLandPlzOrtQP3,
          200);
      setComponentAt(IDX_PANEL_LANDPLZORT, panelLandPlzOrtSP3);

      // liste soll sofort angezeigt werden

      panelLandPlzOrtQP3.befuellePanelFilterkriterienDirekt(
          SystemFilterFactory.getInstance().createFKDPlzOderOrt(),
          SystemFilterFactory.getInstance().createFKDLandPlzOrtPLZ());

      panelLandPlzOrtQP3.eventYouAreSelected(true);
    }
  }


  private void refreshOrtSP1()
      throws Throwable {
    if (panelOrtSP1 == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW,
          /* PanelBasis.ACTION_FILTER*/};

      panelOrtQP1 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_ORT,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.ort"),
          true); // liste refresh wenn lasche geklickt wurde

      panelOrtBottomD1 = new PanelOrt(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.ort"),
          null);

      panelOrtSP1 = new PanelSplit(
          getInternalFrame(),
          panelOrtBottomD1,
          panelOrtQP1,
          200);
      setComponentAt(IDX_PANEL_ORT, panelOrtSP1);
      panelOrtQP1.befuellePanelFilterkriterienDirekt(
          SystemFilterFactory.getInstance().createFKDOrt(),
          null);

      // liste soll sofort angezeigt werden
      panelOrtQP1.eventYouAreSelected(true);
    }
    else {
      //filter refreshen.
      // panelOrtQP1.setDefaultFilter(
      //      getDefaultFilterQP1( (Integer) key, null));
    }
  }


  private void refreshLandSP2()
      throws Throwable {
    if (panelLandSP2 == null) {
      QueryType[] qtOrt = null;
      FilterKriterium[] defaultFilterOrt = null;

      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW,
          /* PanelBasis.ACTION_FILTER*/};

      panelLandQP2 = new PanelQuery(
          qtOrt,
          defaultFilterOrt,
          QueryParameters.UC_ID_LAND,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.land"),
          true); // liste refresh wenn lasche geklickt wurde

      panelLandBottomD2 = new PanelLand(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.land"),
          null);

      panelLandSP2 = new PanelSplit(
          getInternalFrame(),
          panelLandBottomD2,
          panelLandQP2,
          200);
      setComponentAt(IDX_PANEL_LAND, panelLandSP2);

      panelLandQP2.befuellePanelFilterkriterienDirekt(
          SystemFilterFactory.getInstance().createFKDLandLKZ(),
          SystemFilterFactory.getInstance().createFKDLandName());
      // liste soll sofort angezeigt werden
      panelLandQP2.eventYouAreSelected(true);
    }
  }


  public LandplzortDto getLandplzortDto() {
    if (panelLandPlzOrtBottomD3 != null) {
      return panelLandPlzOrtBottomD3.getLandplzortDto();
    }
    else {
      return null;
    }
  }


  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {
      wrapperMenuBar = new WrapperMenuBar(this);
    }
    return wrapperMenuBar;
  }


  public void gotoLandPLZOrt()
      throws Throwable {
    refreshLandPlzOrtSP3();
    setSelectedComponent(panelLandPlzOrtSP3);
  }


  public PanelLandPlzOrt getPanelLandPlzOrtBottomD3() {
    return panelLandPlzOrtBottomD3;
  }

}
