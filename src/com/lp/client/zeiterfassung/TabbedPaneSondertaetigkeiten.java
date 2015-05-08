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
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.4 $
 */

public class TabbedPaneSondertaetigkeiten
    extends TabbedPane
{
  private PanelQuery panelQueryTaetigkeiten = null;
  private PanelBasis panelSplitTaetigkeiten = null;
  private PanelBasis panelBottomTaetigkeiten = null;

  private WrapperMenuBar wrapperMenuBar = null;

  private final String MENUE_ACTION_SONDERTAETIGKETENLISTE =
      "MENUE_ACTION_SONDERTAETIGKETENLISTE";

  public TabbedPaneSondertaetigkeiten(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getTextRespectUISPr("zeiterfassung.title.tab.sondertaetigkeiten"));
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

    panelQueryTaetigkeiten = new PanelQuery(null,
                                            ZeiterfassungFilterFactory.getInstance().
                                            createFKZusaetzlicheSondertaetigkeiten(),
                                            QueryParameters.UC_ID_SONDERTAETIGKEIT,
                                            aWhichButtonIUse,
                                            getInternalFrame(),
                                            LPMain.getInstance().getTextRespectUISPr(
                                                "zeiterfassung.title.tab.taetigkeiten"), true);
    panelQueryTaetigkeiten.eventYouAreSelected(false);

    panelQueryTaetigkeiten.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
        getInstance().createFKDKennung(),
        SystemFilterFactory.getInstance().createFKDSprTabelleBezeichnung(ZeiterfassungFac.
        FLR_TAETIGKEIT_TAETIGKEITSPRSET));

    panelBottomTaetigkeiten = new PanelSondertaetigkeiten(
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "zeiterfassung.title.tab.taetigkeiten"), null);
    panelSplitTaetigkeiten = new PanelSplit(
        getInternalFrame(),
        panelBottomTaetigkeiten,
        panelQueryTaetigkeiten,
        240);
    addTab(LPMain.getInstance().getTextRespectUISPr(
        "zeiterfassung.title.tab.taetigkeiten"),
           panelSplitTaetigkeiten);

    // Itemevents an MEIN Detailpanel senden kann.
    getInternalFrame().addItemChangedListener(this);
    this.addChangeListener(this);

  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryTaetigkeiten) {
        Integer iId = (Integer) panelQueryTaetigkeiten.getSelectedId();
        panelBottomTaetigkeiten.setKeyWhenDetailPanel(iId);
        panelBottomTaetigkeiten.eventYouAreSelected(false);
        panelQueryTaetigkeiten.updateButtons();
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
      panelSplitTaetigkeiten.eventYouAreSelected(false);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryTaetigkeiten) {
        panelBottomTaetigkeiten.eventActionNew(e, true, false);
        panelBottomTaetigkeiten.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomTaetigkeiten) {
        panelSplitTaetigkeiten.eventYouAreSelected(false);
      }

    }

    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomTaetigkeiten) {
        panelQueryTaetigkeiten.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomTaetigkeiten) {
        Object oKey = panelBottomTaetigkeiten.getKeyWhenDetailPanel();
        panelQueryTaetigkeiten.eventYouAreSelected(false);
        panelQueryTaetigkeiten.setSelectedId(oKey);
        panelSplitTaetigkeiten.eventYouAreSelected(false);

      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomTaetigkeiten) {
        setKeyWasForLockMe();
        if (panelBottomTaetigkeiten.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryTaetigkeiten.getId2SelectAfterDelete();
          panelQueryTaetigkeiten.setSelectedId(oNaechster);
        }
        panelSplitTaetigkeiten.eventYouAreSelected(false);
      }

    }

  }


  public void setKeyWasForLockMe() {
    Object oKey = panelQueryTaetigkeiten.getSelectedId();

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
            "zeiterfassung.title.tab.sondertaetigkeiten"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());
    getInternalFrame().setLpTitle(
        3, "");

  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    panelSplitTaetigkeiten.eventYouAreSelected(false);
    panelQueryTaetigkeiten.updateButtons();
  }


  protected void lPActionEvent(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(MENUE_ACTION_SONDERTAETIGKETENLISTE)) {
      String add2Title = LPMain.getInstance().getTextRespectUISPr(
          "zeiterfassung.report.sondertaetigkeitenliste");
      getInternalFrame().showReportKriterien(new ReportSondertaetigkeitenliste(
          getInternalFramePersonal(), add2Title));

    }

  }


  protected JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {
      wrapperMenuBar = new WrapperMenuBar(this);

      JMenu menuInfo = new WrapperMenu("lp.info", this);
      JMenuItem menuItemSondertaetigkeiten = new JMenuItem(LPMain.getInstance().
          getTextRespectUISPr(
              "zeiterfassung.report.sondertaetigkeitenliste"));

      menuItemSondertaetigkeiten.addActionListener(this);

      menuItemSondertaetigkeiten.setActionCommand(MENUE_ACTION_SONDERTAETIGKETENLISTE);
      menuInfo.add(menuItemSondertaetigkeiten);

      wrapperMenuBar.addJMenuItem(menuInfo);

    }

    return wrapperMenuBar;

  }

}
