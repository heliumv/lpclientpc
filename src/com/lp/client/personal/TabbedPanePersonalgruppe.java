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
package com.lp.client.personal;


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
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.personal.service.PersonalgruppeDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.1 $
 */

public class TabbedPanePersonalgruppe
    extends TabbedPane
{
  private PanelQuery panelQueryPersonalgruppe = null;
  private PanelBasis panelSplitPersonalgruppe = null;
  private PanelBasis panelBottomPersonalgruppe = null;

  private PanelQuery panelQueryPersonalgruppekosten = null;
  private PanelBasis panelSplitPersonalgruppekosten = null;
  private PanelBasis panelBottomPersonalgruppekosten = null;

  public PersonalgruppeDto personalgruppeDto=null;
  
  private WrapperMenuBar wrapperMenuBar = null;

  private final static int IDX_PANEL_AUSWAHL = 0;
  private final static int IDX_PANEL_KOSTEN = 1;


  public TabbedPanePersonalgruppe(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getTextRespectUISPr("zeiterfassung.title.tab.maschinen"));

    jbInit();
    initComponents();
  }

public PanelQuery getPanelQueryMaschinenkosten(){
  return panelQueryPersonalgruppekosten;
}

  public InternalFramePersonal getInternalFramePersonal() {
    return (InternalFramePersonal) getInternalFrame();
  }


  private void jbInit()
      throws Throwable {

    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_NEW};

    panelQueryPersonalgruppe = new PanelQuery(null,
                                         null,
                                         QueryParameters.UC_ID_PERSONALGRUPPE,
                                         aWhichButtonIUse,
                                         getInternalFrame(),
                                         LPMain.getTextRespectUISPr(
                                             "pers.personalgruppe"), true);
    panelQueryPersonalgruppe.eventYouAreSelected(false);

    panelQueryPersonalgruppe.befuellePanelFilterkriterienDirekt(
       SystemFilterFactory.getInstance().createFKDBezeichnung(),null);

    panelBottomPersonalgruppe = new PanelPersonalgruppe(
        getInternalFrame(),
        LPMain.getTextRespectUISPr(
            "pers.personalgruppe"), null);
    panelSplitPersonalgruppe = new PanelSplit(
        getInternalFrame(),
        panelBottomPersonalgruppe,
        panelQueryPersonalgruppe,
        270);
    addTab(LPMain.getTextRespectUISPr(
        "pers.personalgruppe"),
           panelSplitPersonalgruppe);



    insertTab(
        LPMain.getTextRespectUISPr("lp.kosten"),
        null,
        null,
        LPMain.getTextRespectUISPr("lp.kosten"),
        IDX_PANEL_KOSTEN);

    // Itemevents an MEIN Detailpanel senden kann.
    getInternalFrame().addItemChangedListener(this);
    this.addChangeListener(this);

  }


  private void createPersonalgruppekosten(
      Integer maschineIId)
      throws Throwable {

    if (panelQueryPersonalgruppekosten == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};

      FilterKriterium[] filters = PersonalFilterFactory.getInstance().
      createFKPersonalgruppekosten(maschineIId);

      panelQueryPersonalgruppekosten = new PanelQuery(
          null,
          filters,
          QueryParameters.UC_ID_PERSONALGRUPPEKOSTEN,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getTextRespectUISPr(
              "lp.kosten"), true);

      panelBottomPersonalgruppekosten = new PanelPersonalgruppekosten(
          getInternalFrame(),
          LPMain.getTextRespectUISPr(
              "lp.kosten"), null);

      panelSplitPersonalgruppekosten = new PanelSplit(
          getInternalFrame(),
          panelBottomPersonalgruppekosten,
          panelQueryPersonalgruppekosten,
          350);

      setComponentAt(IDX_PANEL_KOSTEN, panelSplitPersonalgruppekosten);
    }
    else {
      //filter refreshen.
      panelQueryPersonalgruppekosten.setDefaultFilter(
    		  PersonalFilterFactory.getInstance().createFKPersonalgruppekosten(maschineIId));
    }
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryPersonalgruppekosten) {
        Integer iId = (Integer) panelQueryPersonalgruppekosten.getSelectedId();
        panelBottomPersonalgruppekosten.setKeyWhenDetailPanel(iId);
        panelBottomPersonalgruppekosten.eventYouAreSelected(false);
        panelQueryPersonalgruppekosten.updateButtons();
      }
      else if (e.getSource() == panelQueryPersonalgruppe) {
        if (panelQueryPersonalgruppe.getSelectedId() != null) {
          Object key = ( (ISourceEvent) e.getSource()).getIdSelected();

          Integer iId = (Integer) panelQueryPersonalgruppe.getSelectedId();
        panelBottomPersonalgruppe.setKeyWhenDetailPanel(iId);
        panelBottomPersonalgruppe.eventYouAreSelected(false);
          getInternalFramePersonal().setKeyWasForLockMe(
              key + "");

          personalgruppeDto=DelegateFactory.getInstance().
              getPersonalDelegate().personalgruppeFindByPrimaryKey( (Integer)
              key);
         
          getInternalFrame().setLpTitle(
              InternalFrame.TITLE_IDX_AS_I_LIKE, "Personalgruppe: " + personalgruppeDto.getCBez());

          if (panelQueryPersonalgruppe.getSelectedId() == null) {
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
      // panelSplitMaschinen.eventYouAreSelected(false);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryPersonalgruppe) {
        panelBottomPersonalgruppe.eventActionNew(e, true, false);
        panelBottomPersonalgruppe.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelQueryPersonalgruppekosten) {
        panelBottomPersonalgruppekosten.eventActionNew(e, true, false);
        panelBottomPersonalgruppekosten.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomPersonalgruppe) {
        panelSplitPersonalgruppe.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomPersonalgruppekosten) {
        panelSplitPersonalgruppekosten.eventYouAreSelected(false);
      }

    }

    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomPersonalgruppe) {
        panelQueryPersonalgruppe.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
      else if (e.getSource() == panelBottomPersonalgruppekosten) {
        panelQueryPersonalgruppekosten.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomPersonalgruppe) {
        Object oKey = panelBottomPersonalgruppe.getKeyWhenDetailPanel();
        panelQueryPersonalgruppe.eventYouAreSelected(false);
        panelQueryPersonalgruppe.setSelectedId(oKey);
        panelSplitPersonalgruppe.eventYouAreSelected(false);
        panelQueryPersonalgruppe.updateButtons();

      }
      else if (e.getSource() == panelBottomPersonalgruppekosten) {
        Object oKey = panelBottomPersonalgruppekosten.getKeyWhenDetailPanel();
        panelQueryPersonalgruppekosten.eventYouAreSelected(false);
        panelQueryPersonalgruppekosten.setSelectedId(oKey);
        panelSplitPersonalgruppekosten.eventYouAreSelected(false);

      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomPersonalgruppe) {
        getInternalFrame().enableAllPanelsExcept(true);

        setKeyWasForLockMe();
        panelQueryPersonalgruppe.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomPersonalgruppekosten) {
        setKeyWasForLockMe();
        if (panelBottomPersonalgruppekosten.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryPersonalgruppekosten.getId2SelectAfterDelete();
          panelQueryPersonalgruppekosten.setSelectedId(oNaechster);
        }
        panelSplitPersonalgruppekosten.eventYouAreSelected(false);
      }

    }

  }


  public void setKeyWasForLockMe() {
    Object oKey = panelQueryPersonalgruppe.getSelectedId();

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
            "pers.personalgruppe"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());
     if (personalgruppeDto != null) {
       getInternalFrame().setLpTitle(
           3, personalgruppeDto.getCBez());
     }
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);

    int selectedIndex = this.getSelectedIndex();
    if (selectedIndex == IDX_PANEL_AUSWAHL) {
      panelQueryPersonalgruppe.eventYouAreSelected(false);
      if (panelQueryPersonalgruppe.getSelectedId() == null) {
        getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
      }

      panelQueryPersonalgruppe.updateButtons();
      panelSplitPersonalgruppe.eventYouAreSelected(false);
    }

    else if (selectedIndex == IDX_PANEL_KOSTEN) {
      createPersonalgruppekosten(personalgruppeDto.getIId());
      panelSplitPersonalgruppekosten.eventYouAreSelected(false);
      panelQueryPersonalgruppekosten.updateButtons();
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
      //Produktivitaetisstatistik
     

    }
    return wrapperMenuBar;


  }

}
