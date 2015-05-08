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
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class TabbedPaneExtraliste
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryExtraliste = null;
  private PanelBasis panelSplitExtraliste = null;
  private PanelBasis panelBottomExtraliste = null;

  private final static String EXTRA_VORSCHAU = "EXTRA_VORSCHAU";


  private static int IDX_PANEL_EXTRALISTE = 0;




  private WrapperMenuBar wrapperMenuBar = null;

  public TabbedPaneExtraliste(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("system.extraliste"));

    jbInit();
    initComponents();
  }


  public InternalFrameSystem getInternalFrameStueckliste() {
    return (InternalFrameSystem) getInternalFrame();
  }


  private void createExtraliste()
      throws Throwable {
    if (panelSplitExtraliste == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW};
      panelQueryExtraliste = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_EXTRALISTE,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "system.extraliste"), true);


      // Hier den zusaetzlichen Button aufs Panel bringen
      panelQueryExtraliste.createAndSaveAndShowButton(
          "/com/lp/client/res/goto.png",
          LPMain.getInstance().getTextRespectUISPr(
              "lp.vorschau"),
          PanelBasis.ACTION_MY_OWN_NEW + EXTRA_VORSCHAU,null);


      panelBottomExtraliste = new PanelExtraliste(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "system.extraliste"), null);
      panelSplitExtraliste = new PanelSplit(
          getInternalFrame(),
          panelBottomExtraliste,
          panelQueryExtraliste,
          200);

      setComponentAt(IDX_PANEL_EXTRALISTE, panelSplitExtraliste);
    }
  }



  private void jbInit()
      throws Throwable {
    //Kollektiv

    //1 tab oben: QP1 PartnerFLR; lazy loading
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("system.extraliste"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("system.extraliste"),
        IDX_PANEL_EXTRALISTE);


    createExtraliste();

    // Itemevents an MEIN Detailpanel senden kann.
    refreshTitle();
    this.addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);

  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryExtraliste) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        panelBottomExtraliste.setKeyWhenDetailPanel(key);
        panelBottomExtraliste.eventYouAreSelected(false);
        panelQueryExtraliste.updateButtons();
      }


    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
      panelSplitExtraliste.eventYouAreSelected(false);
    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelBottomExtraliste) {
        panelQueryExtraliste.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
      if (e.getSource() == panelQueryExtraliste) {
        int iPos = panelQueryExtraliste.getTable().getSelectedRow();

        // wenn die Position nicht die erste ist
        if (iPos > 0) {
          Integer iIdPosition = (Integer) panelQueryExtraliste.getSelectedId();

          Integer iIdPositionMinus1 = (Integer) panelQueryExtraliste.getTable().
              getValueAt(iPos - 1, 0);

          DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheMontageart(
              iIdPosition, iIdPositionMinus1);

          // die Liste neu anzeigen und den richtigen Datensatz markieren
          panelQueryExtraliste.setSelectedId(iIdPosition);
        }
      }

    }
    else
    if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
      if (e.getSource() == panelQueryExtraliste) {
        int iPos = panelQueryExtraliste.getTable().getSelectedRow();

        // wenn die Position nicht die letzte ist
        if (iPos < panelQueryExtraliste.getTable().getRowCount() - 1) {
          Integer iIdPosition = (Integer) panelQueryExtraliste.getSelectedId();

          Integer iIdPositionPlus1 = (Integer) panelQueryExtraliste.getTable().getValueAt(
              iPos + 1, 0);

          DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheMontageart(
              iIdPosition, iIdPositionPlus1);

          // die Liste neu anzeigen und den richtigen Datensatz markieren
          panelQueryExtraliste.setSelectedId(iIdPosition);
        }
      }

    }
    else
    if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
      if (e.getSource() == panelQueryExtraliste) {
        panelBottomExtraliste.eventActionNew(e, true, false);
        panelBottomExtraliste.eventYouAreSelected(false); // Buttons schalten
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryExtraliste) {
        panelBottomExtraliste.eventActionNew(e, true, false);
        panelBottomExtraliste.eventYouAreSelected(false);
      }


    }
    else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
      DialogExtraliste dialog=new DialogExtraliste((Integer)panelQueryExtraliste.getSelectedId());

    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomExtraliste) {
        panelSplitExtraliste.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomExtraliste) {
        Object oKey = panelBottomExtraliste.getKeyWhenDetailPanel();
        panelQueryExtraliste.eventYouAreSelected(false);
        panelQueryExtraliste.setSelectedId(oKey);
        panelSplitExtraliste.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomExtraliste) {
        Object oKey = panelQueryExtraliste.getSelectedId();
        if (oKey != null) {
          getInternalFrame().setKeyWasForLockMe(oKey.toString());
        }
        else {
          getInternalFrame().setKeyWasForLockMe(null);
        }
        if (panelBottomExtraliste.getKeyWhenDetailPanel() == null) {
          Object oNaechster = panelQueryExtraliste.getId2SelectAfterDelete();
          panelQueryExtraliste.setSelectedId(oNaechster);
        }
        panelSplitExtraliste.eventYouAreSelected(false);
      }


    }

  }


  private void refreshTitle() {

    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
        LPMain.getInstance().getTextRespectUISPr(
            "system.extraliste"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());
    getInternalFrame().setLpTitle(
        3, "");
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_AS_I_LIKE, "");
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANEL_EXTRALISTE) {
      createExtraliste();
      panelSplitExtraliste.eventYouAreSelected(false);
      panelQueryExtraliste.updateButtons();
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
