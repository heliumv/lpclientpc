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
package com.lp.client.rechnung;


import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahntextDto;
import com.lp.server.rechnung.service.GutschrifttextDto;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich RE-Grundaten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 04.05.05</p>
 *
 * <p>@author $Author: adi $</p>
 *
 * @version $Revision: 1.7 $ Date $Date: 2009/11/18 09:29:12 $
 */
public class TabbedPaneGrunddaten extends TabbedPane {
 
	private static final long serialVersionUID = 1L;
	private static final int IDX_PANEL_RECHNUNGTEXT = 0;
	private static final int IDX_PANEL_MAHNTEXT = 1;
	private final static int IDX_RECHNUNGART = 2;
//	private final static int IDX_RECHNUNGSSTATUS = 3;
//	private final static int IDX_RECHNUNGTYP = 4;
	private final static int IDX_RECHNUNGZAHLUNGART = 3;
	private final static int IDX_MAHNSTUFE = 4;
	private final static int IDX_RECHNUNGPOSITIONART_RE = 5;
	private final static int IDX_RECHNUNGPOSITIONART_GS = 6;
	private final static int IDX_RECHNUNGPOSITIONART_PR = 7;
	private final static int IDX_PANEL_GUTSCHRIFTTEXT = 8;
	private final static int IDX_GUTSCHRIFTGRUND = 9;

	private PanelQuery panelQueryRechnungtext;
	private PanelStammdatenCRUD panelBottomRechnungtext;
	private PanelSplit panelSplitRechnungtext;

	private PanelQuery panelQueryMahntext;
	private PanelStammdatenCRUD panelBottomMahntext;
	private PanelSplit panelSplitMahntext;

	private PanelQuery panelQueryRechnungsart = null;
	private PanelStammdatenCRUD panelBottomRechnungsart = null;
	private PanelSplit panelSplitRechnungsart = null;

// entfernt PJ 14922
/*	
	private PanelQuery panelQueryRechnungsstatus = null;
	private PanelStammdatenCRUD panelBottomRechnungsstatus = null;
	private PanelSplit panelSplitRechnungsstatus = null;

	private PanelQuery panelQueryRechnungtyp = null;
	private PanelStammdatenCRUD panelBottomRechnungtyp = null;
	private PanelSplit panelSplitRechnungtyp = null;
*/
	private PanelQuery panelQueryZahlungsart = null;
	private PanelStammdatenCRUD panelBottomZahlungsart = null;
	private PanelSplit panelSplitZahlungsart = null;

	private PanelQuery panelQueryMahnstufe = null;
	private PanelStammdatenCRUD panelBottomMahnstufe = null;
	private PanelSplit panelSplitMahnstufe = null;

	private PanelQuery panelQueryRechnungpositionartRE = null;
	private PanelStammdatenCRUD panelBottomRechnungpositionartRE = null;
	private PanelSplit panelSplitRechnungpositionartRE = null;

	private PanelQuery panelQueryRechnungpositionartGS = null;
	private PanelStammdatenCRUD panelBottomRechnungpositionartGS = null;
	private PanelSplit panelSplitRechnungpositionartGS = null;

	private PanelQuery panelQueryRechnungpositionartPR = null;
	private PanelStammdatenCRUD panelBottomRechnungpositionartPR = null;
	private PanelSplit panelSplitRechnungpositionartPR = null;

	private PanelQuery panelQueryGutschrifttext;
	private PanelStammdatenCRUD panelBottomGutschrifttext;
	private PanelSplit panelSplitGutschrifttext;
	
	private PanelQuery panelQueryGutschriftgrund = null;
	private PanelStammdatenCRUD panelBottomGutschriftgrund = null;
	private PanelSplit panelSplitGutschriftgrund = null;


  public TabbedPaneGrunddaten(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,  LPMain.getTextRespectUISPr("lp.grunddaten"));
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    // Tab 1: Rechnungstext
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.rechnungtext"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.rechnungtext"),
              IDX_PANEL_RECHNUNGTEXT);

    // Tab 2: Mahntext
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.mahntext"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.mahntext"),
              IDX_PANEL_MAHNTEXT);
    // Tab 3: Rechnungsart
    insertTab(LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungsart"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungsart"),
              IDX_RECHNUNGART);
// entfernt PJ 14922
/*    
    // Tab 4: Rechnungsstatus
    insertTab(LPMain.getInstance().getTextRespectUISPr(
        "rechnung.tab.oben.rechnungsstatus"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr(
        "rechnung.tab.oben.rechnungsstatus"),
              IDX_RECHNUNGSSTATUS);
    // Tab 5: Rechnungstyp
    insertTab(LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungtyp"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungtyp"),
              IDX_RECHNUNGTYP);
*/
    
    // Tab 6: Rechnungzahlungsart
    insertTab(LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.zahlungsart"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.zahlungsart"),
              IDX_RECHNUNGZAHLUNGART);

    // Tab 7: Mahnstufen
    insertTab(LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.mahnstufe"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.mahnstufe"),
              IDX_MAHNSTUFE);

    // Tab 8: Rechnungpositionart Rechnung
    insertTab(LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.rechnung"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.rechnung"),
              IDX_RECHNUNGPOSITIONART_RE);

    // Tab 9: Rechnungpositionart Gutschrift
    insertTab(LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.gutschrift"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.gutschrift"),
              IDX_RECHNUNGPOSITIONART_GS);

    // Tab 10: Rechnungpositionart Proformarechnung
    insertTab(LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.proformarechnung"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.proformarechnung"),
              IDX_RECHNUNGPOSITIONART_PR);
    
    // Tab 11: Rechnungstext
    insertTab(LPMain.getInstance().getTextRespectUISPr("lp.gutschrifttext"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("lp.gutschrifttext"),
              IDX_PANEL_GUTSCHRIFTTEXT);
    // Tab 12: Rutschriftgrund
    insertTab(LPMain.getInstance().getTextRespectUISPr("rechnung.gutschriftsgrund"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("rechnung.gutschriftsgrund"),
              IDX_GUTSCHRIFTGRUND);
    
  

    //Default
    refreshRechnungsart();

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
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
      if (e.getSource() == panelQueryRechnungsart) {
        String cNr = (String) panelQueryRechnungsart.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelBottomRechnungsart.setKeyWhenDetailPanel(cNr);
        panelBottomRechnungsart.eventYouAreSelected(false);
        panelQueryRechnungsart.updateButtons();
      }
      else if (e.getSource() == panelQueryGutschriftgrund) {
          Integer iId = (Integer) panelQueryGutschriftgrund.getSelectedId();
          getInternalFrame().setKeyWasForLockMe(iId+"");
          panelBottomGutschriftgrund.setKeyWhenDetailPanel(iId);
          panelBottomGutschriftgrund.eventYouAreSelected(false);
          panelQueryGutschriftgrund.updateButtons();
      }
// entfernt PJ 14922
/*      
      else if (e.getSource() == panelQueryRechnungsstatus) {
        String cNr = (String) panelQueryRechnungsstatus.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelBottomRechnungsstatus.setKeyWhenDetailPanel(cNr);
        panelBottomRechnungsstatus.eventYouAreSelected(false);
        panelQueryRechnungsstatus.updateButtons();
      }
      else if (e.getSource() == panelQueryRechnungtyp) {
        String cNr = (String) panelQueryRechnungtyp.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelBottomRechnungtyp.setKeyWhenDetailPanel(cNr);
        panelBottomRechnungtyp.eventYouAreSelected(false);
        panelQueryRechnungtyp.updateButtons();
      }
*/
      else if (e.getSource() == panelQueryZahlungsart) {
        String cNr = (String) panelQueryZahlungsart.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelBottomZahlungsart.setKeyWhenDetailPanel(cNr);
        panelBottomZahlungsart.eventYouAreSelected(false);
        panelQueryZahlungsart.updateButtons();
      }
      else if (eI.getSource() == panelQueryRechnungtext) {
        Integer iId = (Integer) panelQueryRechnungtext.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId + "");
        panelBottomRechnungtext.setKeyWhenDetailPanel(iId);
        panelBottomRechnungtext.eventYouAreSelected(false);
        panelQueryRechnungtext.updateButtons();
      }
      else if (eI.getSource() == panelQueryGutschrifttext) {
    	Integer iId = (Integer) panelQueryGutschrifttext.getSelectedId();
    	getInternalFrame().setKeyWasForLockMe(iId + "");
    	panelBottomGutschrifttext.setKeyWhenDetailPanel(iId);
    	panelBottomGutschrifttext.eventYouAreSelected(false);
    	panelQueryGutschrifttext.updateButtons();
      }
      else if (eI.getSource() == panelQueryMahntext) {
        Integer iId = (Integer) panelQueryMahntext.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId + "");
        panelBottomMahntext.setKeyWhenDetailPanel(iId);
        panelBottomMahntext.eventYouAreSelected(false);
        panelQueryMahntext.updateButtons();
      }
      else if (eI.getSource() == panelQueryMahnstufe) {
        Integer iId = (Integer) panelQueryMahnstufe.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(iId + "");
        panelBottomMahnstufe.setKeyWhenDetailPanel(iId);
        panelBottomMahnstufe.eventYouAreSelected(false);
        panelQueryMahnstufe.updateButtons();
      }
      else if (e.getSource() == panelQueryRechnungpositionartRE) {
        String cNr = (String) panelQueryRechnungpositionartRE.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelBottomRechnungpositionartRE.setKeyWhenDetailPanel(cNr);
        panelBottomRechnungpositionartRE.eventYouAreSelected(false);
        panelQueryRechnungpositionartRE.updateButtons();
      }
      else if (e.getSource() == panelQueryRechnungpositionartGS) {
        String cNr = (String) panelQueryRechnungpositionartGS.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelBottomRechnungpositionartGS.setKeyWhenDetailPanel(cNr);
        panelBottomRechnungpositionartGS.eventYouAreSelected(false);
        panelQueryRechnungpositionartGS.updateButtons();
      }
      else if (e.getSource() == panelQueryRechnungpositionartPR) {
        String cNr = (String) panelQueryRechnungpositionartPR.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelBottomRechnungpositionartPR.setKeyWhenDetailPanel(cNr);
        panelBottomRechnungpositionartPR.eventYouAreSelected(false);
        panelQueryRechnungpositionartPR.updateButtons();
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomRechnungsart) {
        panelBottomRechnungsart.eventYouAreSelected(false);
      } 
      else if (e.getSource() == panelBottomGutschriftgrund) {
        panelBottomGutschriftgrund.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomRechnungtext) {
        panelSplitRechnungtext.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomGutschrifttext) {
        panelSplitGutschrifttext.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomMahntext) {
        panelSplitMahntext.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomMahnstufe) {
        panelSplitMahnstufe.eventYouAreSelected(false);
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomRechnungsart) {
        Object oKey = panelBottomRechnungsart.getKeyWhenDetailPanel();
        panelQueryRechnungsart.eventYouAreSelected(false);
        panelQueryRechnungsart.setSelectedId(oKey);
        panelSplitRechnungsart.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomGutschriftgrund) {
    	Object oKey = panelBottomGutschriftgrund.getKeyWhenDetailPanel();
    	panelQueryGutschriftgrund.eventYouAreSelected(false);
    	panelQueryGutschriftgrund.setSelectedId(oKey);
    	panelSplitGutschriftgrund.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomZahlungsart) {
        Object oKey = panelBottomZahlungsart.getKeyWhenDetailPanel();
        panelQueryZahlungsart.eventYouAreSelected(false);
        panelQueryZahlungsart.setSelectedId(oKey);
        panelSplitZahlungsart.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomRechnungtext) {
        Object oKey = panelBottomRechnungtext.getKeyWhenDetailPanel();
        panelQueryRechnungtext.eventYouAreSelected(false);
        panelQueryRechnungtext.setSelectedId(oKey);
        panelSplitRechnungtext.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomGutschrifttext) {
        Object oKey = panelBottomGutschrifttext.getKeyWhenDetailPanel();
        panelQueryGutschrifttext.eventYouAreSelected(false);
        panelQueryGutschrifttext.setSelectedId(oKey);
        panelSplitGutschrifttext.eventYouAreSelected(false);
       }
      else if (eI.getSource() == panelBottomMahntext) {
        Object oKey = panelBottomMahntext.getKeyWhenDetailPanel();
        panelQueryMahntext.eventYouAreSelected(false);
        panelQueryMahntext.setSelectedId(oKey);
        panelSplitMahntext.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomMahnstufe) {
        Object oKey = panelBottomMahnstufe.getKeyWhenDetailPanel();
        panelQueryMahnstufe.eventYouAreSelected(false);
        panelQueryMahnstufe.setSelectedId(oKey);
        panelSplitMahnstufe.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomRechnungpositionartRE) {
        Object oKey = panelBottomRechnungpositionartRE.getKeyWhenDetailPanel();
        panelQueryRechnungpositionartRE.eventYouAreSelected(false);
        panelQueryRechnungpositionartRE.setSelectedId(oKey);
        panelSplitRechnungpositionartRE.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomRechnungpositionartGS) {
        Object oKey = panelBottomRechnungpositionartGS.getKeyWhenDetailPanel();
        panelQueryRechnungpositionartGS.eventYouAreSelected(false);
        panelQueryRechnungpositionartGS.setSelectedId(oKey);
        panelSplitRechnungpositionartGS.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomRechnungpositionartPR) {
        Object oKey = panelBottomRechnungpositionartPR.getKeyWhenDetailPanel();
        panelQueryRechnungpositionartPR.eventYouAreSelected(false);
        panelQueryRechnungpositionartPR.setSelectedId(oKey);
        panelSplitRechnungpositionartPR.eventYouAreSelected(false);
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomRechnungsart) {
        panelSplitRechnungsart.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (e.getSource() == panelBottomGutschriftgrund) {
    	panelSplitGutschriftgrund.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (eI.getSource() == panelBottomRechnungtext) {
        panelSplitRechnungtext.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomMahntext) {
        if (panelBottomMahntext.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryMahntext.getId2SelectAfterDelete();
          panelQueryMahntext.setSelectedId(oNaechster);
        }
        panelSplitMahntext.eventYouAreSelected(false);
      }
      else if (eI.getSource() == panelBottomMahnstufe) {
        if (panelBottomMahnstufe.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryMahnstufe.getId2SelectAfterDelete();
          panelQueryMahnstufe.setSelectedId(oNaechster);
        }
        panelSplitMahnstufe.eventYouAreSelected(false);
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      if (eI.getSource() == panelQueryRechnungsart) {
        panelBottomRechnungsart.eventActionNew(e, true, false);
        panelBottomRechnungsart.eventYouAreSelected(false);
        setSelectedComponent(panelSplitRechnungsart);
      }
      else if (eI.getSource() == panelQueryGutschriftgrund) {
          panelBottomGutschriftgrund.eventActionNew(e, true, false);
          panelBottomGutschriftgrund.eventYouAreSelected(false);
          setSelectedComponent(panelSplitGutschriftgrund);
      }
      else if (eI.getSource() == panelQueryRechnungtext) {
        if (panelQueryRechnungtext.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomRechnungtext.eventActionNew(eI, true, false);
        panelBottomRechnungtext.eventYouAreSelected(false);
        setSelectedComponent(panelSplitRechnungtext);
      }
      else if (eI.getSource() == panelQueryMahntext) {
        if (panelQueryMahntext.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomMahntext.eventActionNew(eI, true, false);
        panelBottomMahntext.eventYouAreSelected(false);
        setSelectedComponent(panelSplitMahntext);
      }
      else if (eI.getSource() == panelQueryMahnstufe) {
        if (panelQueryMahnstufe.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomMahnstufe.eventActionNew(eI, true, false);
        panelBottomMahnstufe.eventYouAreSelected(false);
        setSelectedComponent(panelSplitMahnstufe);
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
    // diese haben keinen 4. titel
    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    switch (selectedIndex) {
      case IDX_RECHNUNGART: {
        refreshRechnungsart();
        panelSplitRechnungsart.eventYouAreSelected(false);
        break;
      }
      case IDX_GUTSCHRIFTGRUND: {
    	refreshGutschriftgrund();
    	panelSplitGutschriftgrund.eventYouAreSelected(false);
    	break;
        }
// entfernt PJ 14922
/*      
      case IDX_RECHNUNGSSTATUS: {
        refreshRechnungsstatus();
        panelSplitRechnungsstatus.eventYouAreSelected(false);
        break;
      }
      case IDX_RECHNUNGTYP: {
        refreshRechnungtyp();
        panelSplitRechnungtyp.eventYouAreSelected(false);
        break;
      }
*/
      case IDX_RECHNUNGZAHLUNGART: {
        refreshZahlungsart();
        panelSplitZahlungsart.eventYouAreSelected(false);
        break;
      }
      case IDX_PANEL_RECHNUNGTEXT: {
        refreshPanelRechnungtext();
        panelQueryRechnungtext.setDefaultFilter(RechnungFilterFactory.getInstance().createFKRechnungtext());
        panelQueryRechnungtext.eventYouAreSelected(false);
        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
        if (panelQueryRechnungtext.getSelectedId() == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this,
              IDX_PANEL_RECHNUNGTEXT, false);
        }
        break;
      }
      case IDX_PANEL_GUTSCHRIFTTEXT: {
    	  refreshPanelGutschrifttext();
          panelQueryGutschrifttext.setDefaultFilter(RechnungFilterFactory.getInstance().createFKGutschrifttext());
          panelQueryGutschrifttext.eventYouAreSelected(false);
          // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
          //   die restlichen oberen Laschen deaktivieren, ausser ...
          if (panelQueryGutschrifttext.getSelectedId() == null) {
            getInternalFrame().enableAllOberePanelsExceptMe(this,
                IDX_PANEL_GUTSCHRIFTTEXT, false);
          }
          break;
        }
      case IDX_PANEL_MAHNTEXT: {
        refreshPanelMahntext();
        panelQueryMahntext.setDefaultFilter(FinanzFilterFactory.getInstance().createFKMahntext());
        panelQueryMahntext.eventYouAreSelected(false);
        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
        if (panelQueryMahntext.getSelectedId() == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this,
              IDX_PANEL_MAHNTEXT, false);
        }
        break;
      }
      case IDX_MAHNSTUFE: {
        refreshMahnstufe();
        panelSplitMahnstufe.eventYouAreSelected(false);
        break;
      }
      case IDX_RECHNUNGPOSITIONART_RE: {
        refreshRechnungpositionartRE();
        panelSplitRechnungpositionartRE.eventYouAreSelected(false);
        break;
      }
      case IDX_RECHNUNGPOSITIONART_GS: {
        refreshRechnungpositionartGS();
        panelSplitRechnungpositionartGS.eventYouAreSelected(false);
        break;
      }
      case IDX_RECHNUNGPOSITIONART_PR: {
        refreshRechnungpositionartPR();
        panelSplitRechnungpositionartPR.eventYouAreSelected(false);
        break;
      }
    }
  }


  private void refreshRechnungsart()
      throws Throwable {

    if (panelSplitRechnungsart == null) {

      panelQueryRechnungsart = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_RECHNUNGART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungsart"),
          true); // liste refresh wenn lasche geklickt wurde

      panelBottomRechnungsart = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungsart"),
          null,
          HelperClient.SCRUD_RECHNUNGSART_FILE,
          (InternalFrameRechnung) getInternalFrame(),
          HelperClient.LOCKME_RECHNUNGSART);

      panelSplitRechnungsart = new PanelSplit(
          getInternalFrame(),
          panelBottomRechnungsart,
          panelQueryRechnungsart,
          200);
      setComponentAt(IDX_RECHNUNGART, panelSplitRechnungsart);

      // liste soll sofort angezeigt werden
      panelQueryRechnungsart.eventYouAreSelected(true);
    }
  }

  private void refreshGutschriftgrund()  throws Throwable {

	  if (panelSplitGutschriftgrund == null) {

		  String[] aWhichStandardButtonIUse = {
			        PanelBasis.ACTION_NEW
			    };
		  panelQueryGutschriftgrund = new PanelQuery(
				  null,
				  null,
				  QueryParameters.UC_ID_GUTSCHRIFTGRUND,
				  aWhichStandardButtonIUse,
				  getInternalFrame(),
				  LPMain.getInstance().getTextRespectUISPr("rechnung.gutschriftsgrund"),
				  true); // liste refresh wenn lasche geklickt wurde

		  panelBottomGutschriftgrund = new PanelStammdatenCRUD(
				  getInternalFrame(),
				  LPMain.getInstance().getTextRespectUISPr("rechnung.gutschriftsgrund"),
				  null,
				  HelperClient.SCRUD_GUTSCHRIFTGRUND_FILE,
				  (InternalFrameRechnung) getInternalFrame(),
				  HelperClient.LOCKME_GUTSCHRIFTGRUND);

		  panelSplitGutschriftgrund = new PanelSplit(
				  getInternalFrame(),
				  panelBottomGutschriftgrund,
				  panelQueryGutschriftgrund,
				  200);
		  setComponentAt(IDX_GUTSCHRIFTGRUND, panelSplitGutschriftgrund);

		  // liste soll sofort angezeigt werden
		  panelQueryGutschriftgrund.eventYouAreSelected(true);
	  }
  }
/* entfernt PJ 14922
  private void refreshRechnungtyp()
      throws Throwable {

    if (panelSplitRechnungtyp == null) {

      panelQueryRechnungtyp = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_RECHNUNGTYP,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungtyp"),
          true); // liste refresh wenn lasche geklickt wurde

      panelBottomRechnungtyp = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungtyp"),
          null,
          HelperClient.SCRUD_RECHNUNGTYP_FILE,
          (InternalFrameRechnung) getInternalFrame(),
          HelperClient.LOCKME_RECHNUNGTYP);

      panelSplitRechnungtyp = new PanelSplit(
          getInternalFrame(),
          panelBottomRechnungtyp,
          panelQueryRechnungtyp,
          200);
      setComponentAt(IDX_RECHNUNGTYP, panelSplitRechnungtyp);

      // liste soll sofort angezeigt werden
      panelQueryRechnungtyp.eventYouAreSelected(true);
    }
  }
*/

  private void refreshZahlungsart()
      throws Throwable {

    if (panelSplitZahlungsart == null) {

      panelQueryZahlungsart = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_ZAHLUNGSART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.zahlungsart"),
          true); // liste refresh wenn lasche geklickt wurde

      panelBottomZahlungsart = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.zahlungsart"),
          null,
          HelperClient.SCRUD_ZAHLUNGSART_FILE,
          (InternalFrameRechnung) getInternalFrame(),
          HelperClient.LOCKME_ZAHLUNGSART);

      panelSplitZahlungsart = new PanelSplit(
          getInternalFrame(),
          panelBottomZahlungsart,
          panelQueryZahlungsart,
          200);
      setComponentAt(IDX_RECHNUNGZAHLUNGART, panelSplitZahlungsart);

      // liste soll sofort angezeigt werden
      panelQueryZahlungsart.eventYouAreSelected(true);
    }
  }


  /**
   * @throws Throwable
   */
  private void refreshRechnungpositionartRE()
      throws Throwable {

    if (panelSplitRechnungpositionartRE == null) {
      panelQueryRechnungpositionartRE = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_RECHNUNGPOSITIONSART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.rechnung"),
          true); // liste refresh wenn lasche geklickt wurde

      panelBottomRechnungpositionartRE = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.rechnung"),
          null,
          HelperClient.SCRUD_RECHNUNGPOSITIONSART_FILE,
          (InternalFrameRechnung) getInternalFrame(),
          HelperClient.LOCKME_RECHNUNGPOSITIONSART);

      panelSplitRechnungpositionartRE = new PanelSplit(
          getInternalFrame(),
          panelBottomRechnungpositionartRE,
          panelQueryRechnungpositionartRE,
          200);
      setComponentAt(IDX_RECHNUNGPOSITIONART_RE, panelSplitRechnungpositionartRE);

      // liste soll sofort angezeigt werden
      panelQueryRechnungpositionartRE.eventYouAreSelected(true);
    }
  }


  /**
   * @throws Throwable
   */
  private void refreshRechnungpositionartGS()
      throws Throwable {

    if (panelSplitRechnungpositionartGS == null) {
      panelQueryRechnungpositionartGS = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_GUTSCHRIFTPOSITIONSART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.gutschrift"),
          true); // liste refresh wenn lasche geklickt wurde

      panelBottomRechnungpositionartGS = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.gutschrift"),
          null,
          HelperClient.SCRUD_GUTSCHRIFTPOSITIONSART_FILE,
          (InternalFrameRechnung) getInternalFrame(),
          HelperClient.LOCKME_GUTSCHRIFTPOSITIONSART);

      panelSplitRechnungpositionartGS = new PanelSplit(
          getInternalFrame(),
          panelBottomRechnungpositionartGS,
          panelQueryRechnungpositionartGS,
          200);
      setComponentAt(IDX_RECHNUNGPOSITIONART_GS, panelSplitRechnungpositionartGS);

      // liste soll sofort angezeigt werden
      panelQueryRechnungpositionartGS.eventYouAreSelected(true);
    }
  }


  /**
   * @throws Throwable
   */
  private void refreshRechnungpositionartPR()
      throws Throwable {

    if (panelSplitRechnungpositionartPR == null) {
      panelQueryRechnungpositionartPR = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_PROFORMARECHNUNGPOSITIONSART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.proformarechnung"),
          true); // liste refresh wenn lasche geklickt wurde

      panelBottomRechnungpositionartPR = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungpositionart.proformarechnung"),
          null,
          HelperClient.SCRUD_PROFORMARECHNUNGPOSITIONSART_FILE,
          (InternalFrameRechnung) getInternalFrame(),
          HelperClient.LOCKME_PROFORMARECHNUNGPOSITIONSART);

      panelSplitRechnungpositionartPR = new PanelSplit(
          getInternalFrame(),
          panelBottomRechnungpositionartPR,
          panelQueryRechnungpositionartPR,
          200);
      setComponentAt(IDX_RECHNUNGPOSITIONART_PR, panelSplitRechnungpositionartPR);

      // liste soll sofort angezeigt werden
      panelQueryRechnungpositionartPR.eventYouAreSelected(true);
    }
  }

/* entfernt PJ 14922
  private void refreshRechnungsstatus()
      throws Throwable {
    if (panelSplitRechnungsstatus == null) {
      panelQueryRechnungsstatus = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_RECHNUNGSTATUS,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungsstatus"),
          true); // liste refresh wenn lasche geklickt wurde
      panelBottomRechnungsstatus = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.rechnungsstatus"),
          null,
          HelperClient.SCRUD_RECHNUNGSSTATUS_FILE,
          (InternalFrameRechnung) getInternalFrame(),
          HelperClient.LOCKME_RECHNUNGSSTATUS);
      panelSplitRechnungsstatus = new PanelSplit(
          getInternalFrame(),
          panelBottomRechnungsstatus,
          panelQueryRechnungsstatus,
          200);
      setComponentAt(IDX_RECHNUNGSSTATUS, panelSplitRechnungsstatus);
      // liste soll sofort angezeigt werden
      panelQueryRechnungsstatus.eventYouAreSelected(true);
    }
  }
*/
  private void refreshMahnstufe()
      throws Throwable {
    /**
     * @todo MB->MB wirklich hier anlegen?
     */
    if (panelSplitMahnstufe == null) {
      MahnstufeDto mahnstufeDto1 = DelegateFactory.getInstance().getMahnwesenDelegate().
          mahnstufeFindByPrimaryKey(new Integer(FinanzServiceFac.MAHNSTUFE_1));
      if (mahnstufeDto1 == null) {
        MahnstufeDto mahnstufeDto = new MahnstufeDto();
        mahnstufeDto.setIId(new Integer(FinanzServiceFac.MAHNSTUFE_1));
        mahnstufeDto.setITage(new Integer(10));
        DelegateFactory.getInstance().getMahnwesenDelegate().createMahnstufe(mahnstufeDto);
      }
      MahnstufeDto mahnstufeDto2 = DelegateFactory.getInstance().getMahnwesenDelegate().
          mahnstufeFindByPrimaryKey(new Integer(FinanzServiceFac.MAHNSTUFE_2));
      if (mahnstufeDto2 == null) {
        MahnstufeDto mahnstufeDto = new MahnstufeDto();
        mahnstufeDto.setIId(new Integer(FinanzServiceFac.MAHNSTUFE_2));
        mahnstufeDto.setITage(new Integer(10));
        DelegateFactory.getInstance().getMahnwesenDelegate().createMahnstufe(mahnstufeDto);
      }
      MahnstufeDto mahnstufeDto3 = DelegateFactory.getInstance().getMahnwesenDelegate().
          mahnstufeFindByPrimaryKey(new Integer(FinanzServiceFac.MAHNSTUFE_3));
      if (mahnstufeDto3 == null) {
        MahnstufeDto mahnstufeDto = new MahnstufeDto();
        mahnstufeDto.setIId(new Integer(FinanzServiceFac.MAHNSTUFE_3));
        mahnstufeDto.setITage(new Integer(10));
        DelegateFactory.getInstance().getMahnwesenDelegate().createMahnstufe(mahnstufeDto);
      }
      MahnstufeDto mahnstufeDto99 = DelegateFactory.getInstance().getMahnwesenDelegate().
          mahnstufeFindByPrimaryKey(new Integer(FinanzServiceFac.MAHNSTUFE_99));
      if (mahnstufeDto99 == null) {
        MahnstufeDto mahnstufeDto = new MahnstufeDto();
        mahnstufeDto.setIId(new Integer(FinanzServiceFac.MAHNSTUFE_99));
        mahnstufeDto.setITage(new Integer(10));
        DelegateFactory.getInstance().getMahnwesenDelegate().createMahnstufe(mahnstufeDto);
      }
      String[] aWhichStandardButtonIUse = {
          PanelBasis.ACTION_NEW
      };
      panelQueryMahnstufe = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_MAHNSTUFE,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("rechnung.tab.oben.mahnstufe"),
          true); // liste refresh wenn lasche geklickt wurde
      panelBottomMahnstufe = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.detail"),
          null,
          HelperClient.SCRUD_MAHNSTUFE_FILE,
          (InternalFrameRechnung) getInternalFrame(),
          HelperClient.LOCKME_MAHNSTUFE);
      panelSplitMahnstufe = new PanelSplit(
          getInternalFrame(),
          panelBottomMahnstufe,
          panelQueryMahnstufe,
          200);
      setComponentAt(IDX_MAHNSTUFE, panelSplitMahnstufe);
      panelQueryMahnstufe.setDefaultFilter(SystemFilterFactory.getInstance().createFKMandantCNr());
      // liste soll sofort angezeigt werden
      panelQueryMahnstufe.eventYouAreSelected(true);
    }
  }


  private void refreshPanelRechnungtext() throws Throwable {
		if (panelSplitRechnungtext == null) {
			/**
			 * @todo MB->MB wirklich hier anlegen?
			 */
			// der Kopftext muss hinterlegt sein oder angelegt werden
			RechnungtextDto reTextDtoKopf = DelegateFactory.getInstance()
					.getRechnungServiceDelegate()
					.rechnungtextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_KOPFTEXT);
			if (reTextDtoKopf == null) {
				DelegateFactory.getInstance().getRechnungServiceDelegate()
						.createDefaultRechnungtext(
								MediaFac.MEDIAART_KOPFTEXT,
								RechnungServiceFac.RECHNUNG_DEFAULT_KOPFTEXT,
								LPMain.getInstance().getTheClient()
										.getLocUiAsString());
			}
			// der Fusstext muss hinterlegt sein oder angelegt werden
			RechnungtextDto reTextDtoFuss = DelegateFactory.getInstance()
					.getRechnungServiceDelegate()
					.rechnungtextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_FUSSTEXT);
			if (reTextDtoFuss == null) {
				DelegateFactory.getInstance().getRechnungServiceDelegate()
						.createDefaultRechnungtext(
								MediaFac.MEDIAART_FUSSTEXT,
								RechnungServiceFac.RECHNUNG_DEFAULT_FUSSTEXT,
								LPMain.getInstance().getTheClient()
										.getLocUiAsString());
			}
			String[] aWhichStandardButtonIUse = null;
			panelQueryRechnungtext = new PanelQuery(null, null,
					QueryParameters.UC_ID_RECHNUNGTEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.rechnungtext"), true);
			panelBottomRechnungtext = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.rechnungtext"), null,
					HelperClient.SCRUD_RECHNUNGTEXT_FILE, getInternalFrame(),
					HelperClient.LOCKME_RECHNUNGTEXT);
			panelSplitRechnungtext = new PanelSplit(getInternalFrame(),
					panelBottomRechnungtext, panelQueryRechnungtext,
					400 - ((PanelStammdatenCRUD) panelBottomRechnungtext)
							.getAnzahlControls() * 30);
			setComponentAt(IDX_PANEL_RECHNUNGTEXT, panelSplitRechnungtext);
		}
	}

	private void refreshPanelGutschrifttext() throws Throwable {
		if (panelSplitGutschrifttext == null) {
			// der Kopftext muss hinterlegt sein oder angelegt werden
			GutschrifttextDto gsTextDtoKopf = DelegateFactory.getInstance()
					.getRechnungServiceDelegate()
					.gutschrifttextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_KOPFTEXT);
			if (gsTextDtoKopf == null) {
				DelegateFactory.getInstance().getRechnungServiceDelegate()
						.createDefaultGutschrifttext(
								MediaFac.MEDIAART_KOPFTEXT,
								RechnungServiceFac.GUTSCHRIFT_DEFAULT_KOPFTEXT,
								LPMain.getInstance().getTheClient()
										.getLocUiAsString());
			}
			// der Fusstext muss hinterlegt sein oder angelegt werden
			GutschrifttextDto gsTextDtoFuss = DelegateFactory.getInstance()
					.getRechnungServiceDelegate()
					.gutschrifttextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_FUSSTEXT);
			if (gsTextDtoFuss == null) {
				DelegateFactory.getInstance().getRechnungServiceDelegate()
						.createDefaultGutschrifttext(
								MediaFac.MEDIAART_FUSSTEXT,
								RechnungServiceFac.GUTSCHRIFT_DEFAULT_FUSSTEXT,
								LPMain.getInstance().getTheClient()
										.getLocUiAsString());
			}
			String[] aWhichStandardButtonIUse = null;
			panelQueryGutschrifttext = new PanelQuery(null, null,
					QueryParameters.UC_ID_GUTSCHRIFTTEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.gutschrifttext"), true);
			panelBottomGutschrifttext = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.gutschrifttext"), null,
					HelperClient.SCRUD_GUTSCHRIFTTEXT_FILE, getInternalFrame(),
					HelperClient.LOCKME_RECHNUNGTEXT);
			panelSplitGutschrifttext = new PanelSplit(getInternalFrame(),
					panelBottomGutschrifttext, panelQueryGutschrifttext,
					400 - ((PanelStammdatenCRUD) panelBottomGutschrifttext)
							.getAnzahlControls() * 30);
			setComponentAt(IDX_PANEL_GUTSCHRIFTTEXT, panelSplitGutschrifttext);
		}
	}

  private void refreshPanelMahntext()
      throws Throwable {
    if (panelSplitMahntext == null) {
      MahntextDto mahntextDto1 = DelegateFactory.getInstance().getFinanzServiceDelegate().
          mahntextFindByMandantLocaleCNr(LPMain.getInstance().getTheClient().
                                         getLocUiAsString(),
                                         new Integer(FinanzServiceFac.MAHNSTUFE_1));
      if (mahntextDto1 == null) {
        DelegateFactory.getInstance().getFinanzServiceDelegate().createDefaultMahntext(new Integer(
            FinanzServiceFac.MAHNSTUFE_1), FinanzServiceFac.DEFAULT_TEXT_MAHNSTUFE_1);
      }
      MahntextDto mahntextDto2 = DelegateFactory.getInstance().getFinanzServiceDelegate().
          mahntextFindByMandantLocaleCNr(LPMain.getInstance().getTheClient().
                                         getLocUiAsString(),
                                         new Integer(FinanzServiceFac.MAHNSTUFE_2));
      if (mahntextDto2 == null) {
        DelegateFactory.getInstance().getFinanzServiceDelegate().createDefaultMahntext(new Integer(
            FinanzServiceFac.MAHNSTUFE_2), FinanzServiceFac.DEFAULT_TEXT_MAHNSTUFE_2);
      }
      MahntextDto mahntextDto3 = DelegateFactory.getInstance().getFinanzServiceDelegate().
          mahntextFindByMandantLocaleCNr(LPMain.getInstance().getTheClient().
                                         getLocUiAsString(),
                                         new Integer(FinanzServiceFac.MAHNSTUFE_3));
      if (mahntextDto3 == null) {
        DelegateFactory.getInstance().getFinanzServiceDelegate().createDefaultMahntext(new Integer(
            FinanzServiceFac.MAHNSTUFE_3), FinanzServiceFac.DEFAULT_TEXT_MAHNSTUFE_3);
      }
      MahntextDto mahntextDto99 = DelegateFactory.getInstance().getFinanzServiceDelegate().
          mahntextFindByMandantLocaleCNr(LPMain.getInstance().getTheClient().
                                         getLocUiAsString(),
                                         new Integer(FinanzServiceFac.MAHNSTUFE_99));
      if (mahntextDto99 == null) {
        DelegateFactory.getInstance().getFinanzServiceDelegate().createDefaultMahntext(new Integer(
            FinanzServiceFac.MAHNSTUFE_99), FinanzServiceFac.DEFAULT_TEXT_MAHNSTUFE_99);
      }
      String[] aWhichStandardButtonIUse = {
          PanelBasis.ACTION_NEW
      };

      panelQueryMahntext = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_MAHNTEXT,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.mahntext"),
          true);

      panelBottomMahntext = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.mahntext"),
          null,
          HelperClient.SCRUD_MAHNTEXT_FILE,
          getInternalFrame(),
          HelperClient.LOCKME_MAHNTEXT);

      panelSplitMahntext = new PanelSplit(
          getInternalFrame(),
          panelBottomMahntext,
          panelQueryMahntext,
          400 - ( (PanelStammdatenCRUD) panelBottomMahntext).
          getAnzahlControls() * 30);

      setComponentAt(IDX_PANEL_MAHNTEXT, panelSplitMahntext);
    }
  }
  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }

}
