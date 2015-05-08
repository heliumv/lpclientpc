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
package com.lp.client.partner;


import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
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
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 09:44:45 $
 */
public class TabbedPaneGrunddatenLieferant
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private final static int IDX_LIEFERGRUPPEN = 0;

  private PanelQuery panelLiefergruppeQP1 = null;
  private PanelStammdatenCRUD panelLiefergruppeBottomD1 = null;
  private PanelSplit panelLiefergruppeSP1 = null;

private WrapperMenuBar wrapperMenuBar = null;

  public TabbedPaneGrunddatenLieferant(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    // Tab 1: Liefergruppen
    insertTab(LPMain.getInstance().getTextRespectUISPr("part.liefergruppe"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("part.liefergruppe"),
              IDX_LIEFERGRUPPEN);

    //Default
    refreshLiefergruppen();

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);

    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
                                  LPMain.getInstance().getTextRespectUISPr(
                                      "bes.title.panel.bestellunggrunddaten"));
  }


  protected JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {
      wrapperMenuBar = new WrapperMenuBar(this);
    }
    return wrapperMenuBar;
  }


  protected void lPActionEvent(ActionEvent e)
      throws Throwable {

  }


  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {
    if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (eI.getSource() == panelLiefergruppeQP1) {
        Integer iId = (Integer) panelLiefergruppeQP1.
            getSelectedId();
        panelLiefergruppeBottomD1.setKeyWhenDetailPanel(iId);
        panelLiefergruppeBottomD1.eventYouAreSelected(false);
        panelLiefergruppeQP1.updateButtons();
      }
    }
    else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
      // hier kommt man nach upd im D bei einem 1:n hin.
      if (eI.getSource() == panelLiefergruppeBottomD1) {
        // btnstate: 2 im QP die Buttons in den Zustand neu setzen.
        panelLiefergruppeQP1.updateButtons(
            new LockStateValue(PanelBasis.LOCK_FOR_NEW));
      }
    }
    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      if (eI.getSource() == panelLiefergruppeQP1) {
        panelLiefergruppeBottomD1.eventActionNew(eI, true, false);
        panelLiefergruppeBottomD1.eventYouAreSelected(false);
        setSelectedComponent(panelLiefergruppeSP1);
      }
    }
    else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (eI.getSource() == panelLiefergruppeBottomD1) {
        if (panelLiefergruppeBottomD1.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelLiefergruppeQP1.getId2SelectAfterDelete();
          panelLiefergruppeQP1.setSelectedId(oNaechster);
        }
        panelLiefergruppeSP1.eventYouAreSelected(false);
      }
    }
    else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (eI.getSource() == panelLiefergruppeBottomD1) {
        panelLiefergruppeSP1.eventYouAreSelected(false);
      }
    }
    else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (eI.getSource() == panelLiefergruppeBottomD1) {
        Object oKey = panelLiefergruppeBottomD1.getKeyWhenDetailPanel();
        panelLiefergruppeQP1.eventYouAreSelected(false);
        panelLiefergruppeQP1.setSelectedId(oKey);
        panelLiefergruppeSP1.eventYouAreSelected(false);
      }
    }
  }


  private void refreshLiefergruppen()
      throws Throwable {

    if (panelLiefergruppeSP1 == null) {

      // Buttons.
      String[] aButton = {
          PanelBasis.ACTION_NEW
      };
      FilterKriterium[] f = SystemFilterFactory.getInstance().createFKMandantCNr();
      panelLiefergruppeQP1 = new PanelQuery(
          null,
          f,
          QueryParameters.UC_ID_LIEFERGRUPPEN,
          aButton,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("part.liefergruppe"),
          true); // liste refresh wenn lasche geklickt wurde

      panelLiefergruppeBottomD1 = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("part.liefergruppe"),
          null,
          HelperClient.SCRUD_LIEFERGRUPPEN_FILE,
          (InternalFrameLieferant) getInternalFrame(),
          HelperClient.LOCKME_ANSPRECHPARTNERFKT);

      panelLiefergruppeSP1 = new PanelSplit(
          getInternalFrame(),
          panelLiefergruppeBottomD1,
          panelLiefergruppeQP1,
          200);
      setComponentAt(IDX_LIEFERGRUPPEN, panelLiefergruppeSP1);

      // liste soll sofort angezeigt werden
      panelLiefergruppeQP1.eventYouAreSelected(true);
    }
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

    switch (selectedIndex) {

      case IDX_LIEFERGRUPPEN: {
        refreshLiefergruppen();
        panelLiefergruppeSP1.eventYouAreSelected(false);
        panelLiefergruppeQP1.updateButtons();
        getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");
        break;
      }
    }
  }

}
