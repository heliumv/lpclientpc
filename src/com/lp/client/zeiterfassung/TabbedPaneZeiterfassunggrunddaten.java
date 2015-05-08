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


public class TabbedPaneZeiterfassunggrunddaten
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryMaschinengruppe = null;
  private PanelBasis panelSplitMaschinengruppe = null;
  private PanelBasis panelBottomMaschinengruppe = null;


  private final static int IDX_PANEL_MASCHINENGRUPPE = 0;

  private WrapperMenuBar wrapperMenuBar = null;

  public TabbedPaneZeiterfassunggrunddaten(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("pers.title.tab.grunddaten"));

    jbInit();
    initComponents();
  }


  public InternalFrameZeiterfassung getInternalFrameZeiterfassung() {
    return (InternalFrameZeiterfassung) getInternalFrame();
  }


  private void createKollektiv()
      throws Throwable {
    if (panelSplitMaschinengruppe == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};

      panelQueryMaschinengruppe = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_MASCHINENGRUPPE,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.maschinengruppe"), true);

      panelQueryMaschinengruppe.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
          getInstance().createFKDBezeichnung(), null);

      panelBottomMaschinengruppe = new PanelMaschinengruppe(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "pers.maschinengruppe"), null);
      panelSplitMaschinengruppe = new PanelSplit(
          getInternalFrame(),
          panelBottomMaschinengruppe,
          panelQueryMaschinengruppe,
          400);

      setComponentAt(IDX_PANEL_MASCHINENGRUPPE, panelSplitMaschinengruppe);
    }
  }


  private void jbInit()
      throws Throwable {
    //Kollektiv

    //1 tab oben: QP1 PartnerFLR; lazy loading
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.maschinengruppe"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.maschinengruppe"),
        IDX_PANEL_MASCHINENGRUPPE);



    createKollektiv();

    // Itemevents an MEIN Detailpanel senden kann.
    this.addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);

  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryMaschinengruppe) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        panelBottomMaschinengruppe.setKeyWhenDetailPanel(key);
        panelBottomMaschinengruppe.eventYouAreSelected(false);
        panelQueryMaschinengruppe.updateButtons();
      }


    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
      panelSplitMaschinengruppe.eventYouAreSelected(false);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryMaschinengruppe) {
        panelBottomMaschinengruppe.eventActionNew(e, true, false);
        panelBottomMaschinengruppe.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
       if (e.getSource() == panelBottomMaschinengruppe) {
        panelQueryMaschinengruppe.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }

    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomMaschinengruppe) {
        panelSplitMaschinengruppe.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomMaschinengruppe) {
        Object oKey = panelBottomMaschinengruppe.getKeyWhenDetailPanel();
        panelQueryMaschinengruppe.eventYouAreSelected(false);
        panelQueryMaschinengruppe.setSelectedId(oKey);
        panelSplitMaschinengruppe.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomMaschinengruppe) {
        Object oKey = panelQueryMaschinengruppe.getSelectedId();
        if (oKey != null) {
          getInternalFrame().setKeyWasForLockMe(oKey.toString());
        }
        else {
          getInternalFrame().setKeyWasForLockMe(null);
        }
        if (panelBottomMaschinengruppe.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryMaschinengruppe.getId2SelectAfterDelete();
          panelQueryMaschinengruppe.setSelectedId(oNaechster);
        }
        panelSplitMaschinengruppe.eventYouAreSelected(false);
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

    if (selectedIndex == IDX_PANEL_MASCHINENGRUPPE) {
      createKollektiv();
      panelSplitMaschinengruppe.eventYouAreSelected(false);
      panelQueryMaschinengruppe.updateButtons();
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
