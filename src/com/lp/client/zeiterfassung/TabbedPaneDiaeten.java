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

import javax.swing.JMenuBar;
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


/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.2 $
 */

public class TabbedPaneDiaeten
    extends TabbedPane
{
  private PanelQuery panelQueryDiaeten = null;
  private PanelBasis panelSplitDiaeten = null;
  private PanelBasis panelBottomDiaeten = null;

  private PanelQuery panelQueryDiaetentagessatz = null;
  private PanelBasis panelSplitDiaetentagessatz = null;
  private PanelBasis panelBottomDiaetentagessatz = null;

  private WrapperMenuBar wrapperMenuBar = null;

  private final static int IDX_PANEL_AUSWAHL = 0;
  private final static int IDX_PANEL_TAGESSATZ = 1;

  public TabbedPaneDiaeten(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("pers.zeiterfassung.diaeten"));

    jbInit();
    initComponents();
  }

public PanelQuery getPanelQueryTagessatz(){
  return panelQueryDiaetentagessatz;
}

  public InternalFrameZeiterfassung getInternalFramePersonal() {
    return (InternalFrameZeiterfassung) getInternalFrame();
  }


  private void jbInit()
      throws Throwable {

    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_NEW};

    panelQueryDiaeten = new PanelQuery(null,
                                         null,
                                         QueryParameters.UC_ID_DIAETEN,
                                         aWhichButtonIUse,
                                         getInternalFrame(),
                                         LPMain.getInstance().getTextRespectUISPr(
                                             "pers.zeiterfassung.diaeten"), true);
    panelQueryDiaeten.eventYouAreSelected(false);

    panelQueryDiaeten.befuellePanelFilterkriterienDirekt(
       SystemFilterFactory.getInstance().createFKDBezeichnung(),null);

    panelBottomDiaeten = new PanelDiaeten(
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zeiterfassung.diaeten"), null);
    panelSplitDiaeten = new PanelSplit(
        getInternalFrame(),
        panelBottomDiaeten,
        panelQueryDiaeten,
        270);
    addTab(LPMain.getInstance().getTextRespectUISPr(
        "pers.zeiterfassung.diaeten"),
           panelSplitDiaeten);



    insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.zeiterfassung.diaeten.tagessaetze"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.zeiterfassung.diaeten.tagessaetze"),
        IDX_PANEL_TAGESSATZ);

    // Itemevents an MEIN Detailpanel senden kann.
    getInternalFrame().addItemChangedListener(this);
    this.addChangeListener(this);

  }


  private void createTagessatz(
      Integer diaetenIId)
      throws Throwable {

    if (panelQueryDiaetentagessatz == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};

      FilterKriterium[] filters = ZeiterfassungFilterFactory.getInstance().
          createFKDiaetentagessatz(diaetenIId);

      panelQueryDiaetentagessatz = new PanelQuery(
          null,
          filters,
          QueryParameters.UC_ID_DIAETENTAGESSATZ,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zeiterfassung.diaeten.tagessaetze"), true);

      panelBottomDiaetentagessatz = new PanelDiaetentagessatz(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.zeiterfassung.diaeten.tagessaetze"), null);

      panelSplitDiaetentagessatz = new PanelSplit(
          getInternalFrame(),
          panelBottomDiaetentagessatz,
          panelQueryDiaetentagessatz,
          320);

      setComponentAt(IDX_PANEL_TAGESSATZ, panelSplitDiaetentagessatz);
    }
    else {
      //filter refreshen.
      panelQueryDiaetentagessatz.setDefaultFilter(
          ZeiterfassungFilterFactory.getInstance().createFKDiaetentagessatz(diaetenIId));
    }
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryDiaetentagessatz) {
        Integer iId = (Integer) panelQueryDiaetentagessatz.getSelectedId();
        panelBottomDiaetentagessatz.setKeyWhenDetailPanel(iId);
        panelBottomDiaetentagessatz.eventYouAreSelected(false);
        panelQueryDiaetentagessatz.updateButtons();
      }
      else if (e.getSource() == panelQueryDiaeten) {
        if (panelQueryDiaeten.getSelectedId() != null) {
          Object key = ( (ISourceEvent) e.getSource()).getIdSelected();

          Integer iId = (Integer) panelQueryDiaeten.getSelectedId();
        panelBottomDiaeten.setKeyWhenDetailPanel(iId);
        panelBottomDiaeten.eventYouAreSelected(false);
          getInternalFramePersonal().setKeyWasForLockMe(
              key + "");

          getInternalFramePersonal().setDiaetenDto(DelegateFactory.getInstance().
              getZeiterfassungDelegate().diaetenFindByPrimaryKey( (Integer)
              key));
          String sBezeichnung = "";
          if(getInternalFramePersonal().getDiaetenDto().getCBez()!=null){
            sBezeichnung+=" "+getInternalFramePersonal().getDiaetenDto().getCBez();
          }
          getInternalFrame().setLpTitle(
              InternalFrame.TITLE_IDX_AS_I_LIKE, sBezeichnung);

          if (panelQueryDiaeten.getSelectedId() == null) {
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
    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryDiaeten) {
        panelBottomDiaeten.eventActionNew(e, true, false);
        panelBottomDiaeten.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelQueryDiaetentagessatz) {
        panelBottomDiaetentagessatz.eventActionNew(e, true, false);
        panelBottomDiaetentagessatz.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomDiaeten) {
        panelSplitDiaeten.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomDiaetentagessatz) {
        panelSplitDiaetentagessatz.eventYouAreSelected(false);
      }

    }

    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomDiaeten) {
        panelQueryDiaeten.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
      else if (e.getSource() == panelBottomDiaetentagessatz) {
        panelQueryDiaetentagessatz.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomDiaeten) {
        Object oKey = panelBottomDiaeten.getKeyWhenDetailPanel();
        panelQueryDiaeten.eventYouAreSelected(false);
        panelQueryDiaeten.setSelectedId(oKey);
        panelSplitDiaeten.eventYouAreSelected(false);
        panelQueryDiaeten.updateButtons();

      }
      else if (e.getSource() == panelBottomDiaetentagessatz) {
        Object oKey = panelBottomDiaetentagessatz.getKeyWhenDetailPanel();
        panelQueryDiaetentagessatz.eventYouAreSelected(false);
        panelQueryDiaetentagessatz.setSelectedId(oKey);
        panelSplitDiaetentagessatz.eventYouAreSelected(false);

      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomDiaeten) {
        getInternalFrame().enableAllPanelsExcept(true);

        setKeyWasForLockMe();
        panelQueryDiaeten.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomDiaetentagessatz) {
        setKeyWasForLockMe();
        if (panelBottomDiaetentagessatz.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryDiaetentagessatz.getId2SelectAfterDelete();
          panelQueryDiaetentagessatz.setSelectedId(oNaechster);
        }
        panelSplitDiaetentagessatz.eventYouAreSelected(false);
      }

    }

  }


  public void setKeyWasForLockMe() {
    Object oKey = panelQueryDiaeten.getSelectedId();

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
            "pers.zeiterfassung.diaeten"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());
     if (getInternalFramePersonal().getDiaetenDto() != null) {
       getInternalFrame().setLpTitle(
           3, getInternalFramePersonal().getDiaetenDto().getCBez());
     }
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);

    int selectedIndex = this.getSelectedIndex();
    if (selectedIndex == IDX_PANEL_AUSWAHL) {
      panelQueryDiaeten.eventYouAreSelected(false);
      if (panelQueryDiaeten.getSelectedId() == null) {
        getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
      }

      panelQueryDiaeten.updateButtons();
      panelSplitDiaeten.eventYouAreSelected(false);
    }

    else if (selectedIndex == IDX_PANEL_TAGESSATZ) {
      createTagessatz(getInternalFramePersonal().getDiaetenDto().getIId());
      panelSplitDiaetentagessatz.eventYouAreSelected(false);
      panelQueryDiaetentagessatz.updateButtons();
    }
    refreshTitle();
  }

  protected void lPActionEvent(ActionEvent e)
      throws Throwable {

  }


  protected JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {
      wrapperMenuBar = new WrapperMenuBar(this);
    }
    return wrapperMenuBar;


  }

}
