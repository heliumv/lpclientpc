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
package com.lp.client.system;


import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um das CRUD aller Locale(Sprache)tabellen.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum 10.03.05</p>
 *
 * @version $Revision: 1.4 $ Date $Date: 2009/06/29 09:45:06 $
 */
public class TabbedPaneLocale
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryBelegart = null;
  private PanelBasis panelSplitBelegart = null;
  private PanelBasis panelBottomBelegart = null;

  private PanelQuery panelQueryStatus;
  private PanelBasis panelBottomStatus;
  private PanelSplit panelSplitStatus;

  private PanelQuery panelQueryFunktion = null;
  private PanelSplit panelSplitFunktion = null;
  private PanelStammdatenCRUD panelBottomFunktion = null;

  private PanelQuery panelQueryPositionsart = null;
  private PanelSplit panelSplitPositionsart = null;
  private PanelStammdatenCRUD panelBottomPositionsart = null;

  private PanelSplit panelEinheitSP4 = null;
  private PanelBasis panelEinheitBottomD4 = null;
  private PanelQuery panelEinheitQP4 = null;

  private PanelBasis panelEinheitKonvertierungSP5 = null;
  private PanelBasis panelEinheitKovertierungBottomD5 = null;
  private PanelQuery panelEinheitKonvertierungTopQP5 = null;

  private static final int IDX_PANEL_BELEGART = 0;
  private static final int IDX_PANEL_STATUS = 1;
  private static final int IDX_PANEL_FUNKTION = 2;
  private static final int IDX_PANEL_POSTIONSART = 3;
  private static final int IDX_PANEL_EINHEIT = 4;
  private static final int IDX_PANEL_EINHEITKONVERIERUNG = 5;

  public TabbedPaneLocale(InternalFrame internalFrameI)
      throws Throwable {

    super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("lp.locale"));

    jbInit();
    initComponents();
  }


  public InternalFrameSystem getInternalFrameSystem() {
    return (InternalFrameSystem) getInternalFrame();
  }


  private void jbInit()
      throws Throwable {
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.belegart"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.belegart.tooltip"),
        IDX_PANEL_BELEGART);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.status"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.status"),
        IDX_PANEL_STATUS);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("part.sachbearbeiterfkt"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("part.sachbearbeiterfkt"),
        IDX_PANEL_FUNKTION);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.positionsart"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.positionsart"),
        IDX_PANEL_POSTIONSART);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.einheit"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.einheit.tooltip"),
        IDX_PANEL_EINHEIT);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.einheitkonvertierung"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.einheitkonvertierung.tooltip"),
        IDX_PANEL_EINHEITKONVERIERUNG);

    //default
    refreshPanelBelegart();

    setSelectedComponent(panelSplitBelegart);

    panelQueryBelegart.eventYouAreSelected(false);

    getInternalFrameSystem().getBelegartDto().setCNr(
        panelQueryBelegart.getSelectedId() + "");

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }


  /**
   * changed
   *
   * @param eI ItemChangedEvent
   * @throws Throwable
   */
  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {
    if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (eI.getSource() == panelQueryBelegart) {
        String cNr = (String) panelQueryBelegart.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr + "");
        panelBottomBelegart.setKeyWhenDetailPanel(cNr);
        panelBottomBelegart.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelQueryBelegart.updateButtons();
      }
      else if (eI.getSource() == panelQueryStatus) {
        String cNr = (String) panelQueryStatus.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr + "");
        panelBottomStatus.setKeyWhenDetailPanel(cNr);
        panelBottomStatus.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelQueryStatus.updateButtons();
      }
      else if (eI.getSource() == panelQueryFunktion) {
        Integer iId = (Integer) panelQueryFunktion.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId + "");
        panelBottomFunktion.setKeyWhenDetailPanel(iId);
        panelBottomFunktion.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelQueryFunktion.updateButtons();
      }
      else if (eI.getSource() == panelQueryPositionsart) {
        String cNr = (String) panelQueryPositionsart.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelBottomPositionsart.setKeyWhenDetailPanel(cNr);
        panelBottomPositionsart.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelQueryPositionsart.updateButtons();
      }
      else if (eI.getSource() == panelEinheitQP4) {
        String c_Nr = (String) panelEinheitQP4.getSelectedId();
        //key 1; IF
        getInternalFrame().setKeyWasForLockMe(c_Nr + "");
        //key2; PB
        panelEinheitBottomD4.setKeyWhenDetailPanel(c_Nr);
        panelEinheitBottomD4.eventYouAreSelected(false);

        //im QP die Buttons in den Zustand nolocking/save setzen.
        panelEinheitQP4.updateButtons(
            panelEinheitBottomD4.getLockedstateDetailMainKey());

      }
      else if (eI.getSource() == panelEinheitKonvertierungTopQP5) {
        Integer i_Id = (Integer) panelEinheitKonvertierungTopQP5.getSelectedId();
        //key 1; IF
        getInternalFrame().setKeyWasForLockMe(i_Id + "");
        //key2; PB
        panelEinheitKovertierungBottomD5.setKeyWhenDetailPanel(i_Id);
        panelEinheitKovertierungBottomD5.eventYouAreSelected(false);

        //im QP die Buttons in den Zustand nolocking/save setzen.
        panelEinheitKonvertierungTopQP5.updateButtons(
            panelEinheitKovertierungBottomD5.getLockedstateDetailMainKey());
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
      // wir landen hier bei der Abfolge Button Aendern -> xx -> Button Discard
      if (eI.getSource() == panelBottomBelegart) {
        panelSplitBelegart.eventYouAreSelected(false); // das 1:n Panel neu aufbauen
      }
      else if (eI.getSource() == panelBottomStatus) {
        panelSplitStatus.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomFunktion) {
        panelSplitFunktion.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomPositionsart) {
        panelSplitPositionsart.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelEinheitBottomD4) {
        panelEinheitSP4.eventYouAreSelected(false); // das 1:n Panel neu aufbauen
      }
      else if (eI.getSource() == panelEinheitKovertierungBottomD5) {
        panelEinheitKonvertierungSP5.eventYouAreSelected(false); // das 1:n Panel neu aufbauen
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (eI.getSource() == panelBottomBelegart) {
        Object oKey = panelBottomBelegart.getKeyWhenDetailPanel();
        panelQueryBelegart.eventYouAreSelected(false);
        panelQueryBelegart.setSelectedId(oKey);
        panelSplitBelegart.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomStatus) {
        Object oKey = panelBottomStatus.getKeyWhenDetailPanel();
        panelQueryStatus.eventYouAreSelected(false);
        panelQueryStatus.setSelectedId(oKey);
        panelSplitStatus.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomFunktion) {
        Object oKey = panelBottomFunktion.getKeyWhenDetailPanel();
        panelQueryFunktion.eventYouAreSelected(false);
        panelQueryFunktion.setSelectedId(oKey);
        panelSplitFunktion.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomPositionsart) {
        Object oKey = panelBottomPositionsart.getKeyWhenDetailPanel();
        panelQueryPositionsart.eventYouAreSelected(false);
        panelQueryPositionsart.setSelectedId(oKey);
        panelSplitPositionsart.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelEinheitBottomD4) {
        Object oKey = panelEinheitBottomD4.getKeyWhenDetailPanel();
        panelEinheitQP4.eventYouAreSelected(false);
        panelEinheitQP4.setSelectedId(oKey);
        panelEinheitSP4.eventYouAreSelected(false);
        getInternalFrame().enableAllOberePanelsExceptMe(this, -1, true);
      }
      else if (eI.getSource() == panelEinheitKovertierungBottomD5) {
        Object oKey = panelEinheitKovertierungBottomD5.getKeyWhenDetailPanel();
        panelEinheitKonvertierungTopQP5.eventYouAreSelected(false);
        panelEinheitKonvertierungTopQP5.setSelectedId(oKey);
        panelEinheitKonvertierungTopQP5.eventYouAreSelected(false);
        getInternalFrame().enableAllOberePanelsExceptMe(this, -1, true);
      }

    }

    // wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
    else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (eI.getSource() == panelBottomBelegart) {
        panelSplitBelegart.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelBottomStatus) {
        panelSplitStatus.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelBottomFunktion) {
        panelSplitFunktion.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomPositionsart) {
        panelSplitPositionsart.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelEinheitBottomD4) {
        panelEinheitSP4.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelEinheitKovertierungBottomD5) {
        panelEinheitKonvertierungSP5.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      // hier wird reingesprungen wenn new button gedrueckt wird (hier 1:n panel)
      //New ...
      if (eI.getSource() == panelQueryBelegart) {
        if (panelQueryBelegart.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomBelegart.eventActionNew(eI, true, false);
        panelBottomBelegart.eventYouAreSelected(false);
        setSelectedComponent(panelSplitBelegart);
      }
      else if (eI.getSource() == panelQueryStatus) {
        if (panelQueryStatus.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomStatus.eventActionNew(eI, true, false);
        panelBottomStatus.eventYouAreSelected(false);
        setSelectedComponent(panelSplitStatus);
      }
      else if (eI.getSource() == panelQueryFunktion) {
        if (panelQueryFunktion.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomFunktion.eventActionNew(eI, true, false);
        panelBottomFunktion.eventYouAreSelected(false);
        setSelectedComponent(panelSplitFunktion);
      }
      else if (eI.getSource() == panelQueryPositionsart) {
        if (panelQueryPositionsart.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomPositionsart.eventActionNew(eI, true, false);
        panelBottomPositionsart.eventYouAreSelected(false);
        setSelectedComponent(panelSplitPositionsart);
      }
      else if (eI.getSource() == panelEinheitQP4) {
        if (panelEinheitQP4.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelEinheitBottomD4.eventActionNew(eI, true, false);
        panelEinheitBottomD4.eventYouAreSelected(false);
        setSelectedComponent(panelEinheitSP4);
      }
      else if (eI.getSource() == panelEinheitKonvertierungTopQP5) {
        if (panelEinheitKonvertierungTopQP5.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelEinheitKovertierungBottomD5.eventActionNew(eI, true, false);
        panelEinheitKovertierungBottomD5.eventYouAreSelected(false);
        setSelectedComponent(panelEinheitKonvertierungSP5);
      }

    }
    else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
      // hier kommt man nach upd im D bei einem 1:n hin.
      if (eI.getSource() == panelBottomBelegart) {
        // im QP die Buttons in den Zustand neu setzen.
        panelQueryBelegart.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == panelBottomFunktion) {
        // im QP die Buttons in den Zustand neu setzen.
        panelQueryFunktion.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == panelBottomPositionsart) {
        // im QP die Buttons in den Zustand neu setzen.
        panelQueryPositionsart.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == panelBottomStatus) {
        // im QP die Buttons in den Zustand neu setzen.
        panelQueryStatus.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == this.panelEinheitBottomD4) {
        //im QP die Buttons in den Zustand neu setzen.
        this.panelEinheitQP4.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }

      else if (eI.getSource() == this.panelEinheitKovertierungBottomD5) {
        //im QP die Buttons in den Zustand neu setzen.
        this.panelEinheitKonvertierungTopQP5.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
      }
    }

  }


  /**
   * eventStateChanged
   *
   * @param e ChangeEvent
   * @throws Throwable
   */
  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    switch (selectedIndex) {
      case IDX_PANEL_BELEGART: {
        refreshPanelBelegart();
        panelQueryBelegart.eventYouAreSelected(false);

        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
        if (panelQueryBelegart.getSelectedId() == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_BELEGART, false);
        }

        // im QP die Buttons setzen.
        panelQueryBelegart.updateButtons();
        break;
      }

      case IDX_PANEL_STATUS: {
        refreshPanelStatus();
        panelQueryStatus.eventYouAreSelected(false);

        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
        if (panelQueryStatus.getSelectedId() == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_STATUS, false);
        }

        // im QP die Buttons setzen.
        panelQueryStatus.updateButtons();
        break;
      }

      case IDX_PANEL_FUNKTION: {
        refreshPanelFunktion();
        panelQueryFunktion.eventYouAreSelected(false);

        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
//        if (panelQueryFunktion.getSelectedId() == null) {
//          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_FUNKTION, false);
//        }

        // im QP die Buttons setzen.
        panelQueryFunktion.updateButtons();
        break;
      }

      case IDX_PANEL_POSTIONSART: {
        refreshPanelPositionsart();
        panelQueryPositionsart.eventYouAreSelected(false);

        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
        if (panelQueryPositionsart.getSelectedId() == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this,
              IDX_PANEL_POSTIONSART, false);
        }
        // im QP die Buttons setzen.
        panelQueryPositionsart.updateButtons();

        break;
      }

      case IDX_PANEL_EINHEIT:
        refreshEinheitSP4();
        panelEinheitQP4.eventYouAreSelected(false);

        //im QP die Buttons setzen.
        panelEinheitQP4.updateButtons(
            panelEinheitBottomD4.getLockedstateDetailMainKey());

        break;


      case IDX_PANEL_EINHEITKONVERIERUNG:
        refreshEinheitKonvertierungSP5();
        panelEinheitKonvertierungTopQP5.eventYouAreSelected(false);

        //im QP die Buttons setzen.
        panelEinheitKonvertierungTopQP5.updateButtons(
            panelEinheitKovertierungBottomD5.getLockedstateDetailMainKey());
        break;
    }
  }

  private void refreshEinheitKonvertierungSP5()
      throws Throwable {
    if (panelEinheitKonvertierungSP5 == null) {
      QueryType[] qtEinheitKonvertierung = null;
      FilterKriterium[] defaultFilterEinheitKonvertierung = null;

      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW,
          /* PanelBasis.ACTION_FILTER*/};

      panelEinheitKonvertierungTopQP5 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_EINHEITENKONVERTIERUNG,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.einheitkonvertierung"),
          true); // liste refresh wenn lasche geklickt wurde

      panelEinheitKovertierungBottomD5 = new PanelEinheitKonvertierung(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.einheitkonvertierung"),
          null);

      panelEinheitKonvertierungSP5 = new PanelSplit(
          getInternalFrame(),
          panelEinheitKovertierungBottomD5,
          panelEinheitKonvertierungTopQP5,
          200);
      setComponentAt(IDX_PANEL_EINHEITKONVERIERUNG, panelEinheitKonvertierungSP5);

      // liste soll sofort angezeigt werden
      panelEinheitKonvertierungTopQP5.eventYouAreSelected(true);
    }
  }


  private void refreshEinheitSP4()
      throws Throwable {
    if (panelEinheitSP4 == null) {
      QueryType[] qtEinheit = null;
      FilterKriterium[] defaultFilterEinheit = null;

      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW,
          /* PanelBasis.ACTION_FILTER*/};

      panelEinheitQP4 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_EINHEIT,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.einheit"),
          true); // liste refresh wenn lasche geklickt wurde

      panelEinheitBottomD4 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.einheit"),
          null,
          HelperClient.SCRUD_EINHEIT_FILE,
          getInternalFrameSystem(),
          HelperClient.LOCKME_EINHEIT);

      panelEinheitSP4 = new PanelSplit(
          getInternalFrame(),
          panelEinheitBottomD4,
          panelEinheitQP4,
          200);
      setComponentAt(IDX_PANEL_EINHEIT, panelEinheitSP4);

      // liste soll sofort angezeigt werden
      panelEinheitQP4.eventYouAreSelected(true);
    }
  }


  protected void lPActionEvent(java.awt.event.ActionEvent e) {
    // nothing here.
  }


  private void refreshPanelBelegart()
      throws Throwable {
    if (panelSplitBelegart == null) {
      panelQueryBelegart = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_BELEGART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.belegart"),
          true);

      // stdcrud: 10 Initialisierung
      panelBottomBelegart = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.belegart"),
          null,
          HelperClient.SCRUD_BELEGART_FILE,
          getInternalFrameSystem(), HelperClient.LOCKME_BELEGART);

      // stdcrud: 11 dynamische Position des Dividers im SplitPanel
      // letzter Parameter: dividerLocation, abhaengig von der anzahl der
      // geladenen Controls;
      // 450: Abstand des dividers von oben (passend mit ButtonPanel)
      // 30: zusaetzlicher Abstand pro Control
      panelSplitBelegart = new PanelSplit(
          getInternalFrame(),
          panelBottomBelegart,
          panelQueryBelegart,
          450 - ( (PanelStammdatenCRUD) panelBottomBelegart).
          getAnzahlControls() * 30);

      setComponentAt(IDX_PANEL_BELEGART, panelSplitBelegart);
    }
  }


  private void refreshPanelStatus()
      throws Throwable {

    if (panelSplitStatus == null) {
      String[] aWhichStandardButtonIUse = null;
      panelQueryStatus = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_STATUS,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.status"),
          true);

      panelBottomStatus = new PanelStatus(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.status"),
          null);

      panelSplitStatus = new PanelSplit(
          getInternalFrame(),
          panelBottomStatus,
          panelQueryStatus,
          300);

      setComponentAt(IDX_PANEL_STATUS, panelSplitStatus);
    }
  }


  private void refreshPanelFunktion()
      throws Throwable {

    if (panelSplitFunktion == null) {
      String[] aWhichStandardButtonIUse = {
          PanelBasis.ACTION_NEW
      };
      panelQueryFunktion = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_FUNKTION,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("part.sachbearbeiterfkt"),
          true);

      panelBottomFunktion = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("part.sachbearbeiterfkt"),
          null,
          HelperClient.SCRUD_FUNKTION_FILE,
          getInternalFrameSystem(),
          HelperClient.LOCKME_FUNKTION);

      panelSplitFunktion = new PanelSplit(
          getInternalFrame(),
          panelBottomFunktion,
          panelQueryFunktion,
          420 - ( (PanelStammdatenCRUD) panelBottomFunktion).
          getAnzahlControls() * 30);

      setComponentAt(IDX_PANEL_FUNKTION, panelSplitFunktion);
    }
  }


  private void refreshPanelPositionsart()
      throws Throwable {

    if (panelSplitPositionsart == null) {
      panelQueryPositionsart = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_POSITIONSART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.positionsart"),
          true);

      panelBottomPositionsart = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.positionsart"),
          null,
          HelperClient.SCRUD_POSITIONSART_FILE,
          getInternalFrameSystem(),
          HelperClient.LOCKME_POSITIONSART);

      panelSplitPositionsart = new PanelSplit(
          getInternalFrame(),
          panelBottomPositionsart,
          panelQueryPositionsart,
          430 - ( (PanelStammdatenCRUD) panelBottomPositionsart).
          getAnzahlControls() * 30);

      setComponentAt(IDX_PANEL_POSTIONSART, panelSplitPositionsart);
    }
  }


  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }

}
