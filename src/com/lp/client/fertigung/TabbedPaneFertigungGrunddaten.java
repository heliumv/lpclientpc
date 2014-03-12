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
package com.lp.client.fertigung;


import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich Fertigungs-Grundaten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 21.07.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version $Revision: 1.4 $ Date $Date: 2012/07/26 07:51:56 $
 */
public class TabbedPaneFertigungGrunddaten
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final static int IDX_LOSKLASSE = 0;
  private final static int IDX_LOSSTATUS = 1;
  private final static int IDX_ZUSATZSTATUS = 2;
  private final static int IDX_LOSBEREICH = 3;

  private PanelQuery panelLosklasseQP1 = null;
  private PanelStammdatenCRUD panelLosklasseBottomD1 = null;
  private PanelSplit panelLosklasseSP1 = null;

  private PanelQuery panelLosstatusQP1 = null;
  private PanelStammdatenCRUD panelLosstatusBottomD1 = null;
  private PanelSplit panelLosstatusSP1 = null;

  private PanelQuery panelZusatzstatusQP1 = null;
  private PanelZusatzstatus panelZusatzstatusBottomD1 = null;
  private PanelSplit panelZusatzstatusSP1 = null;

  private PanelQuery panelLosbereichQP1 = null;
  private PanelStammdatenCRUD panelLosbereichBottomD1 = null;
  private PanelSplit panelLosbereichSP1 = null;


  public TabbedPaneFertigungGrunddaten(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    // Tab 1: Kontoart
    insertTab(LPMain.getInstance().getTextRespectUISPr("fert.tab.oben.losklasse.title"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("fert.tab.oben.losklasse.tooltip"),
              IDX_LOSKLASSE);
    // Tab 2: Uvaart
    insertTab(LPMain.getInstance().getTextRespectUISPr("fert.tab.oben.losstatus.title"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("fert.tab.oben.losstatus.tooltip"),
              IDX_LOSSTATUS);

    insertTab(LPMain.getInstance().getTextRespectUISPr("fert.zusatzstatus"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("fert.zusatzstatus"),
              IDX_ZUSATZSTATUS);
    
    insertTab(LPMain.getInstance().getTextRespectUISPr("fert.bereich"),
            null,
            null,
            LPMain.getInstance().getTextRespectUISPr("fert.bereich"),
            IDX_LOSBEREICH);


    //Default
    refreshLosklasse();

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }
  protected void lPActionEvent(ActionEvent e)
      throws Throwable {
    //nothing here
  }


  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelLosklasseQP1) {
        Integer iId = (Integer) panelLosklasseQP1.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId + "");
        panelLosklasseBottomD1.setKeyWhenDetailPanel(iId);
        panelLosklasseBottomD1.eventYouAreSelected(false);
        panelLosklasseQP1.updateButtons();
      }
      else if (e.getSource() == panelLosstatusQP1) {
        String cNr = (String) panelLosstatusQP1.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelLosstatusBottomD1.setKeyWhenDetailPanel(cNr);
        panelLosstatusBottomD1.eventYouAreSelected(false);
        panelLosstatusQP1.updateButtons();
      }
      else if (e.getSource() == panelZusatzstatusQP1) {
        Integer cNr = (Integer) panelZusatzstatusQP1.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr+"");
        panelZusatzstatusBottomD1.setKeyWhenDetailPanel(cNr);
        panelZusatzstatusBottomD1.eventYouAreSelected(false);
        panelZusatzstatusQP1.updateButtons();
      }
      else if (e.getSource() == panelLosbereichQP1) {
          Integer cNr = (Integer) panelLosbereichQP1.getSelectedId();
          getInternalFrame().setKeyWasForLockMe(cNr+"");
          panelLosbereichBottomD1.setKeyWhenDetailPanel(cNr);
          panelLosbereichBottomD1.eventYouAreSelected(false);
          panelLosbereichQP1.updateButtons();
        }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelLosklasseBottomD1) {
        panelLosklasseBottomD1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelLosstatusBottomD1) {
        panelLosstatusBottomD1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelZusatzstatusBottomD1) {
        panelZusatzstatusBottomD1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelLosbereichBottomD1) {
          panelLosbereichBottomD1.eventYouAreSelected(false);
        }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelLosklasseBottomD1) {
        Object oKey = panelLosklasseBottomD1.getKeyWhenDetailPanel();
        panelLosklasseQP1.eventYouAreSelected(false);
        panelLosklasseQP1.setSelectedId(oKey);
        panelLosklasseSP1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelLosstatusBottomD1) {
        Object oKey = panelLosstatusBottomD1.getKeyWhenDetailPanel();
        panelLosstatusQP1.eventYouAreSelected(false);
        panelLosstatusQP1.setSelectedId(oKey);
        panelLosstatusSP1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelZusatzstatusBottomD1) {
        Object oKey = panelZusatzstatusBottomD1.getKeyWhenDetailPanel();
        panelZusatzstatusQP1.eventYouAreSelected(false);
        panelZusatzstatusQP1.setSelectedId(oKey);
        panelZusatzstatusSP1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelLosbereichBottomD1) {
          Object oKey = panelLosbereichBottomD1.getKeyWhenDetailPanel();
          panelLosbereichQP1.eventYouAreSelected(false);
          panelLosbereichQP1.setSelectedId(oKey);
          panelLosbereichSP1.eventYouAreSelected(false);
        }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelLosklasseBottomD1) {
        if (panelLosklasseBottomD1.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelLosklasseQP1.getId2SelectAfterDelete();
          panelLosklasseQP1.setSelectedId(oNaechster);
        }
        panelLosklasseSP1.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (e.getSource() == panelLosstatusBottomD1) {
        panelLosstatusSP1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelZusatzstatusBottomD1) {
        panelZusatzstatusSP1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelLosbereichBottomD1) {
          panelLosbereichSP1.eventYouAreSelected(false);
        }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      if (eI.getSource() == panelLosklasseQP1) {
        if (panelLosklasseQP1.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelLosklasseBottomD1.eventActionNew(eI, true, false);
        panelLosklasseBottomD1.eventYouAreSelected(false);
        setSelectedComponent(panelLosklasseSP1);
      }
      if (eI.getSource() == panelLosstatusQP1) {
        // Stati duerfen nicht angelegt werden
      }
      else if (e.getSource() == panelZusatzstatusQP1) {
             panelZusatzstatusBottomD1.eventActionNew(e, true, false);
             panelZusatzstatusBottomD1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelLosbereichQP1) {
          panelLosbereichBottomD1.eventActionNew(e, true, false);
          panelLosbereichBottomD1.eventYouAreSelected(false);
   }
    }
  }

  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();
    switch (selectedIndex) {
      case IDX_LOSKLASSE:
        refreshLosklasse();
        panelLosklasseSP1.eventYouAreSelected(false);
        break;
      case IDX_LOSSTATUS:
        refreshLosstatus();
        panelLosstatusSP1.eventYouAreSelected(false);
        break;
      case IDX_ZUSATZSTATUS:
        refreshZusatzstatus();
        panelZusatzstatusSP1.eventYouAreSelected(false);
        break;
      case IDX_LOSBEREICH:
          refreshLosbereich();
          panelLosbereichSP1.eventYouAreSelected(false);
          break;
    }
  }


  private void refreshLosklasse()
      throws Throwable {

    if (panelLosklasseSP1 == null) {

      // Buttons.
      String[] aButton = {
          PanelBasis.ACTION_NEW
      };
      panelLosklasseQP1 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_LOSKLASSE,
          aButton,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("fert.losklasse"),
          true); // liste refresh wenn lasche geklickt wurde

      panelLosklasseBottomD1 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.detail"),
          null,
          HelperClient.SCRUD_LOSKLASSE_FILE,
          getInternalFrame(),
          HelperClient.LOCKME_LOSKLASSE);

      panelLosklasseSP1 = new PanelSplit(
          getInternalFrame(),
          panelLosklasseBottomD1,
          panelLosklasseQP1,
          200);
      setComponentAt(IDX_LOSKLASSE, panelLosklasseSP1);

      // liste soll sofort angezeigt werden
      panelLosklasseQP1.eventYouAreSelected(true);
    }
  }

  private void refreshLosstatus()
      throws Throwable {

    if (panelLosstatusSP1 == null) {

      panelLosstatusQP1 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_LOSSTATUS,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("fert.tab.oben.losstatus.title"),
          true); // liste refresh wenn lasche geklickt wurde

      panelLosstatusBottomD1 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.detail"),
          null,
          HelperClient.SCRUD_LOSSTATUS_FILE,
          getInternalFrame(),
          HelperClient.LOCKME_LOSSTATUS);

      panelLosstatusSP1 = new PanelSplit(
          getInternalFrame(),
          panelLosstatusBottomD1,
          panelLosstatusQP1,
          200);
      setComponentAt(IDX_LOSSTATUS, panelLosstatusSP1);

      // liste soll sofort angezeigt werden
      panelLosstatusQP1.eventYouAreSelected(true);
    }
  }

  private void refreshZusatzstatus()
      throws Throwable {

    if (panelZusatzstatusSP1 == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};
      panelZusatzstatusQP1 = new PanelQuery(
          null,
          SystemFilterFactory.getInstance().createFKMandantCNr(),
          QueryParameters.UC_ID_ZUSATZSTATUS,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("fert.zusatzstatus"),
          true); // liste refresh wenn lasche geklickt wurde

      panelZusatzstatusBottomD1 = new PanelZusatzstatus(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.detail"),
          null);

      panelZusatzstatusSP1 = new PanelSplit(
          getInternalFrame(),
          panelZusatzstatusBottomD1,
          panelZusatzstatusQP1,
          200);
      setComponentAt(IDX_ZUSATZSTATUS, panelZusatzstatusSP1);

      // liste soll sofort angezeigt werden
      panelZusatzstatusQP1.eventYouAreSelected(true);
    }
  }

  private void refreshLosbereich()
      throws Throwable {

    if (panelLosbereichSP1 == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};
      panelLosbereichQP1 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_LOSBEREICH,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("fert.bereich"),
          true); // liste refresh wenn lasche geklickt wurde

      panelLosbereichBottomD1 =  new PanelStammdatenCRUD(
              getInternalFrame(),
              LPMain.getInstance().getTextRespectUISPr("lp.detail"),
              null,
              HelperClient.SCRUD_LOSBEREICH_FILE,
              getInternalFrame(),
              HelperClient.LOCKME_LOSBEREICH);


      panelLosbereichSP1 = new PanelSplit(
          getInternalFrame(),
          panelLosbereichBottomD1,
          panelLosbereichQP1,
          200);
      setComponentAt(IDX_LOSBEREICH, panelLosbereichSP1);

      // liste soll sofort angezeigt werden
      panelLosbereichQP1.eventYouAreSelected(true);
    }
  }

  protected JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }

}
