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
package com.lp.client.system;


import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.fastlanereader.generated.service.FLRParameteranwenderPK;
import com.lp.server.system.fastlanereader.generated.service.FLRParametermandantPK;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um Parameter.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>01. 06. 05</I></p>
 *
 * @author  Uli Walch
 * @version $Revision: 1.3 $
 */
public class TabbedPaneParameter
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelSplit panelSplitParametermandant = null;
  private PanelQuery panelQueryParametermandant = null;
  private PanelParameterMandant panelBottomParametermandant = null;

  private PanelSplit panelSplitParameteranwender = null;
  private PanelQuery panelQueryParameteranwender = null;
  private PanelParameterAnwender panelBottomParameteranwender = null;

  //Reihenfolge der Panels
  private static final int IDX_PANEL_PARAMETERMANDANT = 0;
  private static final int IDX_PANEL_PARAMETERANWENDER = 1;

  public TabbedPaneParameter(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("lp.parameter"));
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.parametermandant"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.parametermandant"),
        IDX_PANEL_PARAMETERMANDANT);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.parameteranwender"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.parameteranwender"),
        IDX_PANEL_PARAMETERANWENDER);

    refreshPanelParametermandant();
    setSelectedComponent(panelSplitParametermandant);
    panelQueryParametermandant.eventYouAreSelected(false);

    // wenn es keine Parametermandant gibt, die fix verdrahteten Werte fuer den aktuellen Mandanten anlegen
    if (panelQueryParametermandant.getSelectedId() == null) {
      DelegateFactory.getInstance().getParameterDelegate().createFixverdrahteteParametermandant();

      panelQueryParametermandant.eventYouAreSelected(false);
    }

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }

  protected void lPActionEvent(ActionEvent e) {
    // nothing here.
  }


  /**
   * changed: hier wird alles durchlaufen und abgefragt zb. save event, discard
   * event, wenn ein panel refresht werden soll...
   *
   * @param eI ItemChangedEvent
   * @throws Throwable
   */
  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryParametermandant) {
        FLRParametermandantPK pk = (FLRParametermandantPK) panelQueryParametermandant.
            getSelectedId();

        if (pk != null) {
          getInternalFrame().setKeyWasForLockMe(pk.getC_nr() + pk.getC_kategorie());
        }

        panelBottomParametermandant.setKeyWhenDetailPanel(pk);
        panelBottomParametermandant.eventYouAreSelected(false);

        //im QP die Buttons in den Zustand nolocking/save setzen.
        this.panelQueryParametermandant.updateButtons(
            panelBottomParametermandant.getLockedstateDetailMainKey());

      }
      else if (e.getSource() == panelQueryParameteranwender) {
        FLRParameteranwenderPK pk = (FLRParameteranwenderPK) panelQueryParameteranwender.
            getSelectedId();

        if (pk != null) {
          getInternalFrame().setKeyWasForLockMe(pk.getC_nr() + pk.getC_kategorie());
        }

        panelBottomParameteranwender.setKeyWhenDetailPanel(pk);
        panelBottomParameteranwender.eventYouAreSelected(false);

        //im QP die Buttons in den Zustand nolocking/save setzen.
        this.panelQueryParameteranwender.updateButtons(
            panelBottomParameteranwender.getLockedstateDetailMainKey());

      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
      // hier kommt man nach upd im D bei einem 1:n hin.
      if (eI.getSource() == this.panelBottomParametermandant) {
        //im QP die Buttons in den Zustand neu setzen.
        this.panelQueryParametermandant.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
      }
      // hier kommt man nach upd im D bei einem 1:n hin.
      else if (eI.getSource() == this.panelBottomParameteranwender) {
        //im QP die Buttons in den Zustand neu setzen.
        this.panelQueryParameteranwender.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      // wir landen hier bei der Abfolge Button Aendern -> xx -> Button Discard
      if (e.getSource() == panelBottomParametermandant) {
        panelSplitParametermandant.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomParameteranwender) {
        panelSplitParameteranwender.eventYouAreSelected(false);
      }
    }

    //selektiert nach save: 0 Wir landen hier nach Button Save
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomParametermandant) {
        Object oKey = panelBottomParametermandant.getKeyWhenDetailPanel();
        panelQueryParametermandant.eventYouAreSelected(false);
        panelQueryParametermandant.setSelectedId(oKey);
        panelSplitParametermandant.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomParameteranwender) {
        Object oKey = panelBottomParameteranwender.getKeyWhenDetailPanel();
        panelQueryParameteranwender.eventYouAreSelected(false);
        panelQueryParameteranwender.setSelectedId(oKey);
        panelSplitParameteranwender.eventYouAreSelected(false);
      }
    }

    // wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomParametermandant) {
        panelSplitParametermandant.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomParameteranwender) {
        panelSplitParameteranwender.eventYouAreSelected(false);
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      if (eI.getSource() == panelQueryParametermandant) {
        if (panelQueryParametermandant.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomParametermandant.eventActionNew(eI, true, false);
        panelBottomParametermandant.eventYouAreSelected(false);
        setSelectedComponent(panelSplitParametermandant);
      }
      else if (eI.getSource() == panelQueryParameteranwender) {
        if (panelQueryParameteranwender.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomParameteranwender.eventActionNew(eI, true, false);
        panelBottomParameteranwender.eventYouAreSelected(false);
        setSelectedComponent(panelSplitParameteranwender);
      }
    }
  }

  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    switch (selectedIndex) {
      case IDX_PANEL_PARAMETERMANDANT:
        refreshPanelParametermandant();
        panelQueryParametermandant.eventYouAreSelected(false);

        //im QP die Buttons setzen.
        panelQueryParametermandant.updateButtons();

        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
        if (panelQueryParametermandant.getSelectedId() == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this,
              IDX_PANEL_PARAMETERMANDANT, false);
        }
        break;

      case IDX_PANEL_PARAMETERANWENDER:
        refreshPanelParameteranwender();
        panelQueryParameteranwender.eventYouAreSelected(false);

        //im QP die Buttons setzen.
        panelQueryParameteranwender.updateButtons();

        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
              panelBottomParameteranwender.validate();
        panelBottomParameteranwender.repaint();
        break;
    }
  }


  private void refreshPanelParametermandant()
      throws Throwable {

    if (panelSplitParametermandant == null) {
      String[] aWhichStandardButtonIUse = null;

      panelQueryParametermandant = new PanelQuery(
          null,
          SystemFilterFactory.getInstance().createFKIdCompMandantcnr(),
          QueryParameters.UC_ID_PARAMETERMANDANT,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.parametermandant"),
          true);

      panelQueryParametermandant.befuellePanelFilterkriterienDirekt(
          SystemFilterFactory.getInstance().createFKDIdCompCNr(),
          SystemFilterFactory.getInstance().createFKDIdCompCKategorie());

      panelBottomParametermandant = new PanelParameterMandant(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.parametermandant"),
          null);

      panelSplitParametermandant = new PanelSplit(
          getInternalFrame(),
          panelBottomParametermandant,
          panelQueryParametermandant,
          260);

      setComponentAt(IDX_PANEL_PARAMETERMANDANT, panelSplitParametermandant);
    }
  }


  private void refreshPanelParameteranwender()
      throws Throwable {

    if (panelSplitParameteranwender == null) {
      String[] aWhichStandardButtonIUse = null;

      panelQueryParameteranwender = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_PARAMETERANWENDER,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.parameteranwender"),
          true);

      panelBottomParameteranwender = new PanelParameterAnwender(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.parameteranwender"),
          null);

      panelSplitParameteranwender = new PanelSplit(
          getInternalFrame(),
          panelBottomParameteranwender,
          panelQueryParameteranwender,
          260);

      setComponentAt(IDX_PANEL_PARAMETERANWENDER, panelSplitParameteranwender);
    }
  }


  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }

}
