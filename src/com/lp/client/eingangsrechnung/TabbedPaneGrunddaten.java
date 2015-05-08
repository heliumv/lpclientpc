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
package com.lp.client.eingangsrechnung;


import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich um die ER-Grunddaten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 03.05.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 07:50:58 $
 */
public class TabbedPaneGrunddaten
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final static int IDX_EINGANGSRECHNUNGART = 0;
  private final static int IDX_EINGANGSRECHNUNGSSTATUS = 1;

  private PanelQuery panelEingangsrechnungsartQP1 = null;
  private PanelStammdatenCRUD panelEingangsrechnungsartBottomD1 = null;
  private PanelSplit panelEingangsrechnungsartSP1 = null;
  private PanelQuery panelEingangsrechnungsstatusQP2 = null;
  private PanelStammdatenCRUD panelEingangsrechnungsstatusBottomD2 = null;
  private PanelSplit panelEingangsrechnungsstatusSP2 = null;

  public TabbedPaneGrunddaten(InternalFrame internalFrameI)
      throws Throwable {

    super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
        "pers.title.tab.grunddaten"));

    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    // Tab 1: Eingangsrechnungsart
    insertTab(LPMain.getInstance().getTextRespectUISPr("er.tab.oben.eingangsrechnungsart"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("er.tab.oben.eingangsrechnungsart"),
              IDX_EINGANGSRECHNUNGART);

    // Tab 2: Eingangsrechnungsstatus
    insertTab(LPMain.getInstance().getTextRespectUISPr(
        "er.tab.oben.eingangsrechnungsstatus"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr(
                  "er.tab.oben.eingangsrechnungsstatus"),
              IDX_EINGANGSRECHNUNGSSTATUS);

    //Default
    refreshEingangsrechnungsart();

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }


  /**
   * Behandle ActionEvent; zB Menue-Klick oben.
   *
   * @param e ActionEvent
   * @throws Throwable
   * @todo Implement this com.lp.client.frame.component.TabbedPane method  PJ 5238
   */
  protected void lPActionEvent(ActionEvent e)
      throws Throwable {
    //nothing here
  }


  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelEingangsrechnungsartQP1) {
        String cNr = (String) panelEingangsrechnungsartQP1.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelEingangsrechnungsartBottomD1.setKeyWhenDetailPanel(cNr);
        panelEingangsrechnungsartBottomD1.eventYouAreSelected(false);
        panelEingangsrechnungsartQP1.updateButtons();
      }
      else if (e.getSource() == panelEingangsrechnungsstatusQP2) {
        String cNr = (String) panelEingangsrechnungsstatusQP2.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr);
        panelEingangsrechnungsstatusBottomD2.setKeyWhenDetailPanel(cNr);
        panelEingangsrechnungsstatusBottomD2.eventYouAreSelected(false);
        panelEingangsrechnungsstatusQP2.updateButtons();
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelEingangsrechnungsartBottomD1) {
        panelEingangsrechnungsartBottomD1.eventYouAreSelected(false);
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelEingangsrechnungsartBottomD1) {
        Object oKey = panelEingangsrechnungsartBottomD1.getKeyWhenDetailPanel();
        panelEingangsrechnungsartQP1.eventYouAreSelected(false);
        panelEingangsrechnungsartQP1.setSelectedId(oKey);
        panelEingangsrechnungsartSP1.eventYouAreSelected(false);
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelEingangsrechnungsartBottomD1) {
        panelEingangsrechnungsartSP1.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      if (eI.getSource() == panelEingangsrechnungsartQP1) {
        panelEingangsrechnungsartBottomD1.eventActionNew(e, true, false);
        panelEingangsrechnungsartBottomD1.eventYouAreSelected(false);
        setSelectedComponent(panelEingangsrechnungsartSP1);
      }
    }
  }


  /**
   * Behandle ChangeEvent; zB Tabwechsel oben.
   *
   * @param e ChangeEvent
   * @throws Throwable
   * @todo Implement this com.lp.client.frame.component.TabbedPane method  PJ 5238
   */
  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    switch (selectedIndex) {
      case IDX_EINGANGSRECHNUNGART:
        refreshEingangsrechnungsart();
        panelEingangsrechnungsartSP1.eventYouAreSelected(false);
        break;
      case IDX_EINGANGSRECHNUNGSSTATUS:
        refreshEingangsrechnungsstatus();
        panelEingangsrechnungsstatusSP2.eventYouAreSelected(false);
        break;
    }
  }


  private void refreshEingangsrechnungsart()
      throws Throwable {

    if (panelEingangsrechnungsartSP1 == null) {

      panelEingangsrechnungsartQP1 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_EINGANGSRECHNUNGART,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("er.tab.oben.eingangsrechnungsart"),
          true); // liste refresh wenn lasche geklickt wurde

      panelEingangsrechnungsartBottomD1 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("er.tab.oben.eingangsrechnungsart"),
          null,
          HelperClient.SCRUD_EINGANGSRECHNUNGSART_FILE,
          (InternalFrameEingangsrechnung) getInternalFrame(),
          HelperClient.LOCKME_EINGANGSRECHNUNGSART);

      panelEingangsrechnungsartSP1 = new PanelSplit(
          getInternalFrame(),
          panelEingangsrechnungsartBottomD1,
          panelEingangsrechnungsartQP1,
          200);
      setComponentAt(IDX_EINGANGSRECHNUNGART, panelEingangsrechnungsartSP1);

      // liste soll sofort angezeigt werden
      panelEingangsrechnungsartQP1.eventYouAreSelected(true);
    }
  }


  private void refreshEingangsrechnungsstatus()
      throws Throwable {

    if (panelEingangsrechnungsstatusSP2 == null) {

      panelEingangsrechnungsstatusQP2 = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_EINGANGSRECHNUNGSSTATUS,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("er.tab.oben.eingangsrechnungsstatus"),
          true); // liste refresh wenn lasche geklickt wurde

      panelEingangsrechnungsstatusBottomD2 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("er.tab.oben.eingangsrechnungsstatus"),
          null,
          HelperClient.SCRUD_EINGANGSRECHNUNGSSTATUS_FILE,
          (InternalFrameEingangsrechnung) getInternalFrame(),
          HelperClient.LOCKME_EINGANGSRECHNUNGSSTATUS);

      panelEingangsrechnungsstatusSP2 = new PanelSplit(
          getInternalFrame(),
          panelEingangsrechnungsstatusBottomD2,
          panelEingangsrechnungsstatusQP2,
          200);
      setComponentAt(IDX_EINGANGSRECHNUNGSSTATUS, panelEingangsrechnungsstatusSP2);

      // liste soll sofort angezeigt werden
      panelEingangsrechnungsstatusQP2.eventYouAreSelected(true);
    }
  }
  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable  {
    return new WrapperMenuBar(this);
  }

}
