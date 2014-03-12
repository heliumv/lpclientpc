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
package com.lp.client.zeiterfassung;


import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */

public class TabbedPaneZeitstifte
    extends TabbedPane
{
  private PanelQuery panelQueryZeitstifte = null;
  private PanelBasis panelSplitZeitstifte = null;
  private PanelBasis panelBottomZeitstifte = null;

  private WrapperMenuBar wrapperMenuBar = null;

  private final String MENUE_ACTION_ZESTIFTE =
      "MENUE_ACTION_ZESTIFTE";

  public TabbedPaneZeitstifte(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitstifte"));

    jbInit();
    initComponents();
  }


  public InternalFrameZeiterfassung getInternalFramePersonal() {
    return (InternalFrameZeiterfassung) getInternalFrame();
  }


  private void jbInit()
      throws Throwable {

    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_NEW};

    panelQueryZeitstifte = new PanelQuery(null,
                                            SystemFilterFactory.getInstance().createFKMandantCNr(),
                                            QueryParameters.UC_ID_ZEITSTIFT,
                                            aWhichButtonIUse,
                                            getInternalFrame(),
                                            LPMain.getTextRespectUISPr(
                                                "zeiterfassung.title.tab.zeitstifte"), true);
    panelQueryZeitstifte.eventYouAreSelected(false);



    panelBottomZeitstifte = new PanelZeitstifte(
        getInternalFrame(),
        LPMain.getTextRespectUISPr(
            "zeiterfassung.title.tab.zeitstifte"), null);
    panelSplitZeitstifte = new PanelSplit(
        getInternalFrame(),
        panelBottomZeitstifte,
        panelQueryZeitstifte,
        290);
    addTab(LPMain.getTextRespectUISPr(
        "zeiterfassung.title.tab.zeitstifte"),
           panelSplitZeitstifte);

    // Itemevents an MEIN Detailpanel senden kann.
    getInternalFrame().addItemChangedListener(this);
    this.addChangeListener(this);

  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryZeitstifte) {
        Integer iId = (Integer) panelQueryZeitstifte.getSelectedId();
        panelBottomZeitstifte.setKeyWhenDetailPanel(iId);
        panelBottomZeitstifte.eventYouAreSelected(false);
        panelQueryZeitstifte.updateButtons();
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
      panelSplitZeitstifte.eventYouAreSelected(false);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryZeitstifte) {
        panelBottomZeitstifte.eventActionNew(e, true, false);
        panelBottomZeitstifte.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomZeitstifte) {
        panelSplitZeitstifte.eventYouAreSelected(false);
      }

    }

    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomZeitstifte) {
        panelQueryZeitstifte.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomZeitstifte) {
        Object oKey = panelBottomZeitstifte.getKeyWhenDetailPanel();
        panelQueryZeitstifte.eventYouAreSelected(false);
        panelQueryZeitstifte.setSelectedId(oKey);
        panelSplitZeitstifte.eventYouAreSelected(false);

      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomZeitstifte) {
        setKeyWasForLockMe();
        if (panelBottomZeitstifte.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryZeitstifte.getId2SelectAfterDelete();
          panelQueryZeitstifte.setSelectedId(oNaechster);
        }
        panelSplitZeitstifte.eventYouAreSelected(false);
      }

    }

  }


  public void setKeyWasForLockMe() {
    Object oKey = panelQueryZeitstifte.getSelectedId();

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
        LPMain.getTextRespectUISPr(
            "zeiterfassung.title.tab.zeitstifte"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());
    getInternalFrame().setLpTitle(
        3, "");

  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    panelSplitZeitstifte.eventYouAreSelected(false);
    panelQueryZeitstifte.updateButtons();
  }


  protected void lPActionEvent(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(MENUE_ACTION_ZESTIFTE)) {
      String add2Title = LPMain.getTextRespectUISPr(
          "zeiterfassung.title.tab.zeitstifte");
      getInternalFrame().showReportKriterien(new ReportZestifte(
          getInternalFramePersonal(), add2Title));

    }
  }


  protected JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {
      wrapperMenuBar = new WrapperMenuBar(this);

      JMenu menuInfo = new WrapperMenu("lp.info", this);
      JMenuItem menuItemZestifte = new JMenuItem(LPMain.
          getTextRespectUISPr(
              "zeiterfassung.title.tab.zeitstifte"));

      menuItemZestifte.addActionListener(this);

      menuItemZestifte.setActionCommand(MENUE_ACTION_ZESTIFTE);
      menuInfo.add(menuItemZestifte);

      wrapperMenuBar.addJMenuItem(menuInfo);

    }
    return wrapperMenuBar;

  }

}
