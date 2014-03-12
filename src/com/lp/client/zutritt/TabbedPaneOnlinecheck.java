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
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version 1.0
 */
public class TabbedPaneOnlinecheck
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryOnlinecheck = null;
  private PanelBasis panelSplitOnlinecheck = null;
  private PanelBasis panelBottomOnlinecheck = null;
  private WrapperMenuBar wrapperMenuBar = null;

  public TabbedPaneOnlinecheck(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("pers.zutritt.onlinecheck"));

    jbInit();
    initComponents();

  }


  public InternalFrameZutritt getInternalFrameZutritt() {
    return (InternalFrameZutritt) getInternalFrame();
  }


  private void jbInit()
      throws Throwable {
    //Betriebskalender
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_NEW};

    FilterKriterium[] fk = ZutrittFilterFactory.getInstance().createFKZutrittsklasse(
        getInternalFrameZutritt().isBIstHauptmandant());

    panelQueryOnlinecheck = new PanelQuery(
        null,
        fk,
        QueryParameters.UC_ID_ZUTRITTONLINECHECK,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.onlinecheck"), true);
    panelQueryOnlinecheck.eventYouAreSelected(false);

    //  panelQueryBetriebskalender.befuellePanelFilterkriterienDirekt(PersonalFilterFactory.
    //     getInstance().createFKDBetriebskalenderBezeichnung(), null);

    panelBottomOnlinecheck = new PanelOnlinecheck(
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.onlinecheck"), null);
    panelSplitOnlinecheck = new PanelSplit(
        getInternalFrame(),
        panelBottomOnlinecheck,
        panelQueryOnlinecheck,
        330);
    addTab(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.onlinecheck"),
           panelSplitOnlinecheck);

    // Itemevents an MEIN Detailpanel senden kann.
    getInternalFrame().addItemChangedListener(this);
    this.addChangeListener(this);
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryOnlinecheck) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        panelBottomOnlinecheck.setKeyWhenDetailPanel(key);
        panelBottomOnlinecheck.eventYouAreSelected(false);
        panelQueryOnlinecheck.updateButtons();
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
      panelSplitOnlinecheck.eventYouAreSelected(false);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryOnlinecheck) {

        panelBottomOnlinecheck.eventActionNew(e, true, false);
        panelBottomOnlinecheck.eventYouAreSelected(false);

      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomOnlinecheck) {
        panelSplitOnlinecheck.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomOnlinecheck) {
        panelQueryOnlinecheck.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW)); ;
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomOnlinecheck) {
        Object oKey = panelBottomOnlinecheck.getKeyWhenDetailPanel();
        panelQueryOnlinecheck.eventYouAreSelected(false);
        panelQueryOnlinecheck.setSelectedId(oKey);
        panelSplitOnlinecheck.eventYouAreSelected(false);

      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomOnlinecheck) {
        setKeyWasForLockMe();
        if (panelBottomOnlinecheck.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryOnlinecheck.getId2SelectAfterDelete();
          panelQueryOnlinecheck.setSelectedId(oNaechster);
        }
        panelSplitOnlinecheck.eventYouAreSelected(false);
      }

    }

  }


  public void setKeyWasForLockMe() {
    Object oKey = panelQueryOnlinecheck.getSelectedId();

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
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.daueroffen"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());
    getInternalFrame().setLpTitle(
        3, "");
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    panelSplitOnlinecheck.eventYouAreSelected(false);
    panelQueryOnlinecheck.updateButtons();
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
