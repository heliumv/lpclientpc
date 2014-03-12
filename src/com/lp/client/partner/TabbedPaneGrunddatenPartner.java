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
package com.lp.client.partner;


import java.awt.event.ActionEvent;

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
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.partner.service.AnredeDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich um die Partnergrunddaten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 05.05.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2009/07/28 15:23:00 $
 */
public class TabbedPaneGrunddatenPartner
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final static int IDX_ANSPRECHPARTNERFKT = 0;
  private final static int IDX_PARTNERBRANCHE = 1;
  private final static int IDX_ANREDE = 2;
  private final static int IDX_PARTNERKLASSE = 3;
  private final static int IDX_PARTNERART = 4;
  private final static int IDX_KOMMUNIKATIONSART = 5;
  private final static int IDX_SELEKTION = 6;
  private final static int IDX_KONTAKTART = 7;

  private PanelQuery panelAnsprechpartnerfktQP1 = null;
  private PanelStammdatenCRUD panelAnsprechpartnerfktBottomD1 = null;
  private PanelSplit panelAnsprechpartnerfktSP1 = null;
  private PanelQuery panelBrancheQP2 = null;
  private PanelStammdatenCRUD panelBrancheBottomD2 = null;
  private PanelSplit panelBrancheSP2 = null;
  private PanelQuery panelAnredeQP3 = null;
  private PanelStammdatenCRUD panelAnredeBottomD3 = null;
  private PanelSplit panelAnredeSP3 = null;
  private AnredeDto anredeDto = new AnredeDto();
  private PanelQuery panelKlasseQP4 = null;
  private PanelPartnerklasse panelKlasseBottomD4 = null;
  private PanelSplit panelKlasseSP4 = null;
  private PanelQuery panelPartnerartQP5 = null;
  private PanelStammdatenCRUD panelPartnerartBottomD5 = null;
  private PanelSplit panelPartnerartSP5 = null;
  private PanelQuery panelKommunikationsartQP6 = null;
  private PanelStammdatenCRUD panelKommunikationsartBottomD6 = null;
  private PanelSplit panelKommunikationsartSP6 = null;
  private PanelQuery panelSelektionQP7 = null;
  private PanelStammdatenCRUD panelSelektionBottomD7 = null;
  private PanelSplit panelSelektionSP7 = null;

  private PanelQuery panelQueryKontaktart = null;
  private PanelBasis panelDetailKontaktart = null;
  private PanelSplit panelSplitKontaktart = null;
  
  public TabbedPaneGrunddatenPartner(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getTextRespectUISPr("pers.title.tab.grunddaten"));
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    // Tab 1: Ansprechpartnerfkt
    insertTab(LPMain.getInstance().getTextRespectUISPr("part.ansprechpartnerfkt"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("part.ansprechpartnerfkt"),
              IDX_ANSPRECHPARTNERFKT);

    // Tab 2: Branche
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.branche"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.branche"),
              IDX_PARTNERBRANCHE);

    // Tab 3: Anrede
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.anrede"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.anrede"),
              IDX_ANREDE);

    // Tab 4: Klasse
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.partnerklasse"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.partnerklasse"),
              IDX_PARTNERKLASSE);

    // Tab 5: Art
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.art"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.art"),
              IDX_PARTNERART);

    // Tab 6: Kommunikationsart
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kommunikationsart"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.kommunikationsart"),
              IDX_KOMMUNIKATIONSART);

    // Tab 7: Selektion
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
              IDX_SELEKTION);
    if (LPMain.getInstance().getDesktop()
			.darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_KONTAKTMANAGMENT)) {
    // Tab 8: Selektion
    insertTab(LPMain.getInstance().getTextRespectUISPr("proj.projekt.label.kontaktart"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("proj.projekt.label.kontaktart"),
              IDX_KONTAKTART);
    }

    //Default
    refreshAnsprechpartnerfkt();

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);

    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
                              LPMain.getInstance().getTextRespectUISPr("bes.title.panel.bestellunggrunddaten"));
  }


  /**
   * Behandle ActionEvent; zB Menue-Klick oben.
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void lPActionEvent(ActionEvent e)
      throws Throwable {
    //nothing here
  }


  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelAnsprechpartnerfktQP1) {
        Integer iId = (Integer) panelAnsprechpartnerfktQP1.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId + "");
        panelAnsprechpartnerfktBottomD1.setKeyWhenDetailPanel(iId);
        panelAnsprechpartnerfktBottomD1.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelAnsprechpartnerfktQP1.updateButtons();
      }
      else if (e.getSource() == panelBrancheQP2) {
        Integer iId = (Integer) panelBrancheQP2.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId + "");
        panelBrancheBottomD2.setKeyWhenDetailPanel(iId);
        panelBrancheBottomD2.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelBrancheQP2.updateButtons();
      }
      else if (e.getSource() == panelAnredeQP3) {
        String cNr = (String) panelAnredeQP3.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr + "");
        panelAnredeBottomD3.setKeyWhenDetailPanel(cNr);
        panelAnredeBottomD3.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelAnredeQP3.updateButtons();
      }
      else if (e.getSource() == panelKlasseQP4) {
        Integer iId = (Integer) panelKlasseQP4.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId + "");
        panelKlasseBottomD4.setKeyWhenDetailPanel(iId);
        panelKlasseBottomD4.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelKlasseQP4.updateButtons();
      }
      else if (e.getSource() == panelPartnerartQP5) {
        String cNr = (String) panelPartnerartQP5.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelPartnerartBottomD5.setKeyWhenDetailPanel(cNr);
        panelPartnerartBottomD5.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelPartnerartQP5.updateButtons();
      }
      else if (e.getSource() == panelKommunikationsartQP6) {
        String cNr = (String) panelKommunikationsartQP6.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelKommunikationsartBottomD6.setKeyWhenDetailPanel(cNr);
        panelKommunikationsartBottomD6.eventYouAreSelected(false);
        // im QP die Buttons in den Zustand nolocking/save setzen.
        panelKommunikationsartQP6.updateButtons();
      }
      else if (e.getSource() == panelSelektionQP7) {
        Integer iId = (Integer) panelSelektionQP7.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId+"");
        panelSelektionBottomD7.setKeyWhenDetailPanel(iId);
        panelSelektionBottomD7.eventYouAreSelected(false);
        panelSelektionQP7.updateButtons();
      }
      else if (e.getSource() == panelQueryKontaktart) {
          Integer iId = (Integer) panelQueryKontaktart.getSelectedId();
          getInternalFrame().setKeyWasForLockMe(iId+"");
          panelDetailKontaktart.setKeyWhenDetailPanel(iId);
          panelDetailKontaktart.eventYouAreSelected(false);
          panelQueryKontaktart.updateButtons();
        }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelAnsprechpartnerfktBottomD1) {
        panelAnsprechpartnerfktSP1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBrancheBottomD2) {
        panelBrancheSP2.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelAnredeBottomD3) {
        panelAnredeSP3.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelKlasseBottomD4) {
        panelKlasseSP4.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelPartnerartBottomD5) {
        panelPartnerartSP5.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelKommunikationsartBottomD6) {
        panelKommunikationsartSP6.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelDetailKontaktart) {
          panelSplitKontaktart.eventYouAreSelected(false);
        }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
      // hier kommt man nach upd im D bei einem 1:n hin.
      if (eI.getSource() == panelAnsprechpartnerfktBottomD1) {
        // im QP die Buttons in den Zustand neu setzen.
        panelAnsprechpartnerfktQP1.updateButtons(
            new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
      else if (eI.getSource() == panelBrancheBottomD2) {
        // im QP die Buttons in den Zustand neu setzen.
        panelBrancheQP2.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
      else if (eI.getSource() == panelAnredeBottomD3) {
        // im QP die Buttons in den Zustand neu setzen.
        panelAnredeQP3.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == panelKlasseBottomD4) {
        // im QP die Buttons in den Zustand neu setzen.
        panelKlasseQP4.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == panelPartnerartBottomD5) {
        // im QP die Buttons in den Zustand neu setzen.
        panelPartnerartQP5.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == panelKommunikationsartBottomD6) {
        // im QP die Buttons in den Zustand neu setzen.
        panelKommunikationsartQP6.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == panelSelektionBottomD7) {
        // im QP die Buttons in den Zustand neu setzen.
        panelSelektionQP7.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (eI.getSource() == panelDetailKontaktart) {
          // im QP die Buttons in den Zustand neu setzen.
          panelQueryKontaktart.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
        }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelAnsprechpartnerfktBottomD1) {
        Object oKey = panelAnsprechpartnerfktBottomD1.getKeyWhenDetailPanel();
        panelAnsprechpartnerfktQP1.eventYouAreSelected(false);
        panelAnsprechpartnerfktQP1.setSelectedId(oKey);
        panelAnsprechpartnerfktSP1.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBrancheBottomD2) {
        Object oKey = panelBrancheBottomD2.getKeyWhenDetailPanel();
        panelBrancheQP2.eventYouAreSelected(false);
        panelBrancheQP2.setSelectedId(oKey);
        panelBrancheSP2.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelAnredeBottomD3) {
        Object oKey = panelAnredeBottomD3.getKeyWhenDetailPanel();
        panelAnredeQP3.eventYouAreSelected(false);
        panelAnredeQP3.setSelectedId(oKey);
        panelAnredeSP3.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelKlasseBottomD4) {
        Object oKey = panelKlasseBottomD4.getKeyWhenDetailPanel();
        panelKlasseQP4.eventYouAreSelected(false);
        panelKlasseQP4.setSelectedId(oKey);
        panelKlasseSP4.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelPartnerartBottomD5) {
        Object oKey = panelPartnerartBottomD5.getKeyWhenDetailPanel();
        panelPartnerartQP5.eventYouAreSelected(false);
        panelPartnerartQP5.setSelectedId(oKey);
        panelPartnerartSP5.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelKommunikationsartBottomD6) {
        Object oKey = panelKommunikationsartBottomD6.getKeyWhenDetailPanel();
        panelKommunikationsartQP6.eventYouAreSelected(false);
        panelKommunikationsartQP6.setSelectedId(oKey);
        panelKommunikationsartSP6.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelSelektionBottomD7) {
        Object oKey = panelSelektionBottomD7.getKeyWhenDetailPanel();
        panelSelektionQP7.eventYouAreSelected(false);
        panelSelektionQP7.setSelectedId(oKey);
        panelSelektionSP7.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelDetailKontaktart) {
          Object oKey = panelDetailKontaktart.getKeyWhenDetailPanel();
          panelQueryKontaktart.eventYouAreSelected(false);
          panelQueryKontaktart.setSelectedId(oKey);
          panelSplitKontaktart.eventYouAreSelected(false);
        }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelAnsprechpartnerfktBottomD1) {
        if (panelAnsprechpartnerfktBottomD1.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelAnsprechpartnerfktQP1.getId2SelectAfterDelete();
          panelAnsprechpartnerfktQP1.setSelectedId(oNaechster);
        }
        panelAnsprechpartnerfktSP1.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelBrancheBottomD2) {
        if (panelBrancheBottomD2.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelBrancheQP2.getId2SelectAfterDelete();
          panelBrancheQP2.setSelectedId(oNaechster);
        }
        panelBrancheSP2.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelAnredeBottomD3) {
        panelAnredeSP3.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelKlasseBottomD4) {
        if (panelKlasseBottomD4.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelKlasseQP4.getId2SelectAfterDelete();
          panelKlasseQP4.setSelectedId(oNaechster);
        }
        panelKlasseSP4.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelPartnerartBottomD5) {
        panelPartnerartSP5.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelKommunikationsartBottomD6) {
        panelKommunikationsartSP6.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelDetailKontaktart) {
          panelSplitKontaktart.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
        }
      else if (eI.getSource() == panelSelektionBottomD7) {
        if (panelSelektionBottomD7.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelSelektionQP7.getId2SelectAfterDelete();
          panelSelektionQP7.setSelectedId(oNaechster);
        }
        panelSelektionSP7.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      if (eI.getSource() == panelAnsprechpartnerfktQP1) {
        panelAnsprechpartnerfktBottomD1.eventActionNew(e, true, false);
        panelAnsprechpartnerfktBottomD1.eventYouAreSelected(false);
        setSelectedComponent(panelAnsprechpartnerfktSP1);
      }
      else if (eI.getSource() == panelBrancheQP2) {
        if (panelBrancheQP2.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBrancheBottomD2.eventActionNew(eI, true, false);
        panelBrancheBottomD2.eventYouAreSelected(false);
        setSelectedComponent(panelBrancheSP2);
      }
      else if (eI.getSource() == panelAnredeQP3) {
        if (panelAnredeQP3.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelAnredeBottomD3.eventActionNew(eI, true, false);
        panelAnredeBottomD3.eventYouAreSelected(false);
        setSelectedComponent(panelAnredeSP3);
      }
      else if (eI.getSource() == panelKlasseQP4) {
        if (panelKlasseQP4.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelKlasseBottomD4.eventActionNew(eI, true, false);
        panelKlasseBottomD4.eventYouAreSelected(false);
        setSelectedComponent(panelKlasseSP4);
      }
      else if (eI.getSource() == panelPartnerartQP5) {
        if (panelPartnerartQP5.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelPartnerartBottomD5.eventActionNew(eI, true, false);
        panelPartnerartBottomD5.eventYouAreSelected(false);
        setSelectedComponent(panelPartnerartSP5);
      }
      else if (eI.getSource() == panelKommunikationsartQP6) {
        if (panelKommunikationsartQP6.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelKommunikationsartBottomD6.eventActionNew(eI, true, false);
        panelKommunikationsartBottomD6.eventYouAreSelected(false);
        setSelectedComponent(panelKommunikationsartSP6);
      }
      else if (eI.getSource() == panelSelektionQP7) {
        if (panelSelektionQP7.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelSelektionBottomD7.eventActionNew(eI, true, false);
        panelSelektionBottomD7.eventYouAreSelected(false);
        setSelectedComponent(panelSelektionSP7);
      }
      else if (eI.getSource() == panelQueryKontaktart) {
          if (panelQueryKontaktart.getSelectedId() == null) {
            getInternalFrame().enableAllPanelsExcept(true);
          }
          panelDetailKontaktart.eventActionNew(eI, true, false);
          panelDetailKontaktart.eventYouAreSelected(false);
          setSelectedComponent(panelSplitKontaktart);
        }
    }
  }


  /**
   * Behandle ChangeEvent; zB Tabwechsel oben.
   *
   * @param e ChangeEvent
   * @throws Throwable
   */
  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);

    int selectedIndex = this.getSelectedIndex();

    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

    switch (selectedIndex) {

      case IDX_ANSPRECHPARTNERFKT: {
        refreshAnsprechpartnerfkt();
        panelAnsprechpartnerfktSP1.eventYouAreSelected(false);
        panelAnsprechpartnerfktQP1.updateButtons();
        getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");
        break;
      }

      case IDX_PARTNERBRANCHE: {
        refreshBranche();
        panelBrancheSP2.eventYouAreSelected(false);
        panelBrancheQP2.updateButtons();
        break;
      }

      case IDX_ANREDE: {
        refreshAnrede();
        panelAnredeSP3.eventYouAreSelected(false);
        panelAnredeQP3.updateButtons();
        break;
      }

      case IDX_PARTNERKLASSE: {
        refreshKlasse();
        panelKlasseSP4.eventYouAreSelected(false);
        panelKlasseQP4.updateButtons();
        break;
      }

      case IDX_PARTNERART: {
        refreshPartnerart();
        panelPartnerartSP5.eventYouAreSelected(false);
        panelPartnerartQP5.updateButtons();
        break;
      }

      case IDX_KOMMUNIKATIONSART: {
        refreshKommunikationsart();
        panelKommunikationsartSP6.eventYouAreSelected(false);
        panelKommunikationsartQP6.updateButtons();
        break;
      }

      case IDX_SELEKTION: {
        refreshSelektion();
        panelSelektionSP7.eventYouAreSelected(false);
        panelSelektionQP7.updateButtons();
        break;
      }
      case IDX_KONTAKTART: {
          refreshKontaktart();
          panelSplitKontaktart.eventYouAreSelected(false);
          panelQueryKontaktart.updateButtons();
          break;
        }
    }
  }


  private void refreshAnsprechpartnerfkt()
      throws Throwable {

    if (panelAnsprechpartnerfktSP1 == null) {

      // Buttons.
      String[] aButton = {
          PanelBasis.ACTION_NEW
      };
      panelAnsprechpartnerfktQP1 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_ANSPRECHPARTNERFUNKTION,
          aButton,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("part.ansprechpartnerfkt"),
          true); // liste refresh wenn lasche geklickt wurde

      panelAnsprechpartnerfktBottomD1 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("part.ansprechpartnerfkt"),
          null,
          HelperClient.SCRUD_ANSPRECHPARTNERFKT_FILE,
          (InternalFramePartner) getInternalFrame(),
          HelperClient.LOCKME_ANSPRECHPARTNERFKT);

      panelAnsprechpartnerfktSP1 = new PanelSplit(
          getInternalFrame(),
          panelAnsprechpartnerfktBottomD1,
          panelAnsprechpartnerfktQP1,
          200);
      setComponentAt(IDX_ANSPRECHPARTNERFKT, panelAnsprechpartnerfktSP1);

      // liste soll sofort angezeigt werden
      panelAnsprechpartnerfktQP1.eventYouAreSelected(true);
    }
  }


  private void refreshBranche()
      throws Throwable {

    if (panelBrancheSP2 == null) {

      // Buttons.
      String[] aButton = {
          PanelBasis.ACTION_NEW
      };
      panelBrancheQP2 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_BRANCHE,
          aButton,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.branche"),
          true); // liste refresh wenn lasche geklickt wurde

      panelBrancheBottomD2 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.branche"),
          null,
          HelperClient.SCRUD_PARTNERBRANCHE_FILE,
          (InternalFramePartner) getInternalFrame(),
          HelperClient.LOCKME_PARTNERBRANCHE);

      panelBrancheSP2 = new PanelSplit(
          getInternalFrame(),
          panelBrancheBottomD2,
          panelBrancheQP2,
          200);
      setComponentAt(IDX_PARTNERBRANCHE, panelBrancheSP2);

      // liste soll sofort angezeigt werden
      panelBrancheQP2.eventYouAreSelected(true);
    }
  }

  private void refreshKontaktart()
      throws Throwable {

    if (panelSplitKontaktart == null) {

      // Buttons.
      String[] aButton = {
          PanelBasis.ACTION_NEW
      };
      panelQueryKontaktart = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_KONTAKTART,
          aButton,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("proj.projekt.label.kontaktart"),
          true); // liste refresh wenn lasche geklickt wurde

      panelDetailKontaktart = new PanelKontaktart(getInternalFrame(),LPMain.getInstance().getTextRespectUISPr("proj.projekt.label.kontaktart"),
          null);

      panelSplitKontaktart = new PanelSplit(
          getInternalFrame(),
          panelDetailKontaktart,
          panelQueryKontaktart,
          300);
      setComponentAt(IDX_KONTAKTART, panelSplitKontaktart);

      // liste soll sofort angezeigt werden
      panelQueryKontaktart.eventYouAreSelected(true);
    }
  }


  private void refreshAnrede()
      throws Throwable {

    if (panelAnredeSP3 == null) {

      // Buttons.
      panelAnredeQP3 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_ANREDE,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.anrede"),
          true); // liste refresh wenn lasche geklickt wurde

      panelAnredeBottomD3 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.anrede"),
          null,
          HelperClient.SCRUD_PARTNERANREDE_FILE,
          this, //Dto
          HelperClient.LOCKME_PARTNERANREDE);

      panelAnredeSP3 = new PanelSplit(
          getInternalFrame(),
          panelAnredeBottomD3,
          panelAnredeQP3,
          200);
      setComponentAt(IDX_ANREDE, panelAnredeSP3);

      // liste soll sofort angezeigt werden
      panelAnredeQP3.eventYouAreSelected(true);
    }
  }


  private void refreshPartnerart()
      throws Throwable {

    if (panelPartnerartSP5 == null) {
      // Buttons.
    	 String[] aButton = {
    	          PanelBasis.ACTION_NEW
    	      };
      panelPartnerartQP5 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_PARTNERART,
          aButton,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.art"),
          true); // liste refresh wenn lasche geklickt wurde

      panelPartnerartBottomD5 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.art"),
          null,
          HelperClient.SCRUD_PARTNERART_FILE,
          (InternalFramePartner) getInternalFrame(),
          HelperClient.LOCKME_PARTNERART);

      panelPartnerartSP5 = new PanelSplit(
          getInternalFrame(),
          panelPartnerartBottomD5,
          panelPartnerartQP5,
          200);
      setComponentAt(IDX_PARTNERART, panelPartnerartSP5);

      // liste soll sofort angezeigt werden
      panelPartnerartQP5.eventYouAreSelected(true);
    }
  }

  private void refreshKommunikationsart()
      throws Throwable {

    if (panelKommunikationsartSP6 == null) {
      // Buttons.
      panelKommunikationsartQP6 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_KOMMUNIKATIONSART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.kommunikationsart"),
          true); // liste refresh wenn lasche geklickt wurde

      panelKommunikationsartBottomD6 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.kommunikationsart"),
          null,
          HelperClient.SCRUD_KOMMUNIKATIONSART_FILE,
          (InternalFramePartner) getInternalFrame(),
          HelperClient.LOCKME_KOMMUNIKATIONSART);

      panelKommunikationsartSP6 = new PanelSplit(
          getInternalFrame(),
          panelKommunikationsartBottomD6,
          panelKommunikationsartQP6,
          200);
      setComponentAt(IDX_KOMMUNIKATIONSART, panelKommunikationsartSP6);

      // liste soll sofort angezeigt werden
      panelKommunikationsartQP6.eventYouAreSelected(true);
    }
  }



  private void refreshSelektion()
      throws Throwable {

    if (panelSelektionSP7 == null) {
      // Buttons.
      String[] aButton = {
          PanelBasis.ACTION_NEW
      };
      FilterKriterium[] f = SystemFilterFactory.getInstance().createFKMandantCNr();
      panelSelektionQP7 = new PanelQuery(
          null,
          f,
          QueryParameters.UC_ID_SELEKTION,
          aButton,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
          true); // liste refresh wenn lasche geklickt wurde

      panelSelektionBottomD7 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
          null,
          HelperClient.SCRUD_SELEKTION_FILE,
          (InternalFramePartner) getInternalFrame(),
          HelperClient.LOCKME_SELEKTION);

      panelSelektionSP7 = new PanelSplit(
          getInternalFrame(),
          panelSelektionBottomD7,
          panelSelektionQP7,
          200);
      setComponentAt(IDX_SELEKTION, panelSelektionSP7);

      // liste soll sofort angezeigt werden
      panelSelektionQP7.eventYouAreSelected(true);
    }
  }


  private void refreshKlasse()
      throws Throwable {

    if (panelKlasseSP4 == null) {

      // Buttons.
      String[] aButton = {
          PanelBasis.ACTION_NEW
      };
      panelKlasseQP4 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_PARTNERKLASSE,
          aButton,
          (InternalFramePartner) getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.partnerklasse"),
          true); // liste refresh wenn lasche geklickt wurde

      panelKlasseBottomD4 = new PanelPartnerklasse(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.partnerklasse"),
          null);

      panelKlasseSP4 = new PanelSplit(
          getInternalFrame(),
          panelKlasseBottomD4,
          panelKlasseQP4,
          200);
      setComponentAt(IDX_PARTNERKLASSE, panelKlasseSP4);

      // liste soll sofort angezeigt werden
      panelKlasseQP4.eventYouAreSelected(true);
    }
  }


  public void setAnredeDto(AnredeDto anredeDto) {
    this.anredeDto = anredeDto;
  }


  public AnredeDto getAnredeDto() {
    return anredeDto;
  }
  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }

}
