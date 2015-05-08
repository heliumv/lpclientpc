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
package com.lp.client.fertigung;


import javax.swing.JMenu;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access") 
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.5 $
 */

public class TabbedPaneWiederholendelose
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryWiederholendelose = null;
  private PanelBasis panelDetailWiederholendelose = null;

  private final static int IDX_PANEL_AUSWAHL = 0;
  private final static int IDX_PANEL_DETAIL = 1;

  private final static String MENUE_ACTION_BEARBEITEN_AUTO_ANLEGEN =
      "MENUE_ACTION_BEARBEITEN_AUTO_ANLEGEN";

  private WrapperMenuBar wrapperManuBar = null;

  public TabbedPaneWiederholendelose(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("fert.wiederholendelose"));
    jbInit();
    initComponents();
  }


  public PanelQuery getPanelQueryWiederholende() {
    return panelQueryWiederholendelose;
  }


  private void jbInit()
      throws Throwable {

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
        IDX_PANEL_AUSWAHL);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.detail"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.detail"),
        IDX_PANEL_DETAIL);

    createAuswahl();

    panelQueryWiederholendelose.eventYouAreSelected(false);
    if ( (Integer) panelQueryWiederholendelose.getSelectedId() != null) {
      getInternalFrameFertigung().setWiederholendeloseDto(DelegateFactory.getInstance().
          getFertigungDelegate().
          wiederholendeloseFindByPrimaryKey( (
              Integer)
                                            panelQueryWiederholendelose.getSelectedId()));
    }
    //damit D2 einen aktuellen hat.
    ItemChangedEvent it = new ItemChangedEvent(panelQueryWiederholendelose,
                                               ItemChangedEvent.ITEM_CHANGED);
    lPEventItemChanged(it);

    this.addChangeListener(this);
    this.getInternalFrame().addItemChangedListener(this);

  }


  private void createAuswahl()
      throws Throwable {
    if (panelQueryWiederholendelose == null) {
      //Artikelauswahlliste
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
          PanelBasis.ACTION_POSITION_VONNNACHNPLUS1};
      panelQueryWiederholendelose = new PanelQuery(
          null,
          com.lp.client.system.SystemFilterFactory.getInstance().createFKMandantCNr(),
          QueryParameters.UC_ID_WIEDERHOLENDELOSE,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "lp.auswahl"), true);

      panelQueryWiederholendelose.befuellePanelFilterkriterienDirektUndVersteckte(
			null, null,
			FertigungFilterFactory.getInstance().createFKVWiederholende());
      
      setComponentAt(IDX_PANEL_AUSWAHL, panelQueryWiederholendelose);

    }
  }


  private void createDetail(Integer key)
      throws Throwable {
    if (panelDetailWiederholendelose == null) {
      panelDetailWiederholendelose = new PanelWiederholendelose(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.detail"),
          key);
      setComponentAt(IDX_PANEL_DETAIL, panelDetailWiederholendelose);
    }
  }


  public InternalFrameFertigung getInternalFrameFertigung() {
    return (InternalFrameFertigung) getInternalFrame();
  }


  public void setKeyWasForLockMe() {
    Object oKey = panelQueryWiederholendelose.getSelectedId();

    if (oKey != null) {
      getInternalFrame().setKeyWasForLockMe(oKey.toString());
    }
    else {
      getInternalFrame().setKeyWasForLockMe(null);
    }
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryWiederholendelose) {
        Integer iId = (Integer) panelQueryWiederholendelose.getSelectedId();
        if (iId != null) {
          setSelectedComponent(panelDetailWiederholendelose);
        }
      }
    }
    else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryWiederholendelose) {
        if (panelQueryWiederholendelose.getSelectedId() != null) {
          getInternalFrameFertigung().setKeyWasForLockMe(panelQueryWiederholendelose.
              getSelectedId() + "");

          //Dto-setzen
          getInternalFrameFertigung().setWiederholendeloseDto(DelegateFactory.getInstance().
              getFertigungDelegate().
              wiederholendeloseFindByPrimaryKey( (
                  Integer) panelQueryWiederholendelose.
                                                getSelectedId()));
          String sBezeichnung = getInternalFrameFertigung().getWiederholendeloseDto().
              getCProjekt();
          if (sBezeichnung == null) {
            sBezeichnung = "";
          }
          getInternalFrame().setLpTitle(
              InternalFrame.TITLE_IDX_AS_I_LIKE, sBezeichnung);
          if (panelQueryWiederholendelose.getSelectedId() == null) {
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
    else
    if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
      if (e.getSource() == panelQueryWiederholendelose) {
        int iPos = panelQueryWiederholendelose.getTable().getSelectedRow();

        // wenn die Position nicht die letzte ist
        if (iPos < panelQueryWiederholendelose.getTable().getRowCount() - 1) {
          Integer iIdPosition = (Integer) panelQueryWiederholendelose.getSelectedId();

          Integer iIdPositionPlus1 = (Integer) panelQueryWiederholendelose.getTable().
              getValueAt(
                  iPos + 1, 0);

          DelegateFactory.getInstance().getFertigungDelegate().
              vertauscheWiederholendelose(
                  iIdPosition, iIdPositionPlus1);

          // die Liste neu anzeigen und den richtigen Datensatz markieren
          panelQueryWiederholendelose.setSelectedId(iIdPosition);
        }
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
      if (e.getSource() == panelQueryWiederholendelose) {
        int iPos = panelQueryWiederholendelose.getTable().getSelectedRow();

        // wenn die Position nicht die erste ist
        if (iPos > 0) {
          Integer iIdPosition = (Integer) panelQueryWiederholendelose.getSelectedId();

          Integer iIdPositionMinus1 = (Integer) panelQueryWiederholendelose.getTable().
              getValueAt(iPos - 1, 0);

          DelegateFactory.getInstance().getFertigungDelegate().
              vertauscheWiederholendelose(
                  iIdPosition, iIdPositionMinus1);

          // die Liste neu anzeigen und den richtigen Datensatz markieren
          panelQueryWiederholendelose.setSelectedId(iIdPosition);
        }
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      refreshTitle();
    }

    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryWiederholendelose) {
        createDetail( (Integer) panelQueryWiederholendelose.getSelectedId());

        if (panelQueryWiederholendelose.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelDetailWiederholendelose.eventActionNew(null, true, false);
        setSelectedComponent(panelDetailWiederholendelose);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

      if (e.getSource() == panelDetailWiederholendelose) {
        panelQueryWiederholendelose.clearDirektFilter();
        Object oKey = panelDetailWiederholendelose.getKeyWhenDetailPanel();

        panelQueryWiederholendelose.setSelectedId(oKey);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelDetailWiederholendelose) {
        this.setSelectedComponent(panelQueryWiederholendelose);
        setKeyWasForLockMe();
        panelQueryWiederholendelose.eventYouAreSelected(false);
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {

    }

  }


  private void refreshTitle() {
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
        LPMain.getInstance().getTextRespectUISPr("fert.wiederholendelose"));
    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
        ( (PanelBasis)this.getSelectedComponent()).getAdd2Title());

    if (getInternalFrameFertigung().getWiederholendeloseDto() != null) {
      String sBezeichnung = getInternalFrameFertigung().getWiederholendeloseDto().
          getCProjekt();
      if (sBezeichnung == null) {
        sBezeichnung = "";
      }

      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          sBezeichnung);
    }
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANEL_AUSWAHL) {
      createAuswahl();
      panelQueryWiederholendelose.eventYouAreSelected(false);
      if (panelQueryWiederholendelose.getSelectedId() == null) {
        getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
      }
      panelQueryWiederholendelose.updateButtons();
    }
    else if (selectedIndex == IDX_PANEL_DETAIL) {
      Integer key = null;
      if (getInternalFrameFertigung().getWiederholendeloseDto() != null) {
        key = getInternalFrameFertigung().getWiederholendeloseDto().getIId();
      }
      createDetail(key);
      panelDetailWiederholendelose.eventYouAreSelected(false);
    }

    refreshTitle();
  }


  protected void lPActionEvent(java.awt.event.ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_AUTO_ANLEGEN)) {
      DelegateFactory.getInstance().getFertigungDelegate().wiederholendeLoseAnlegen();
    }
  }


  public javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperManuBar == null) {
      wrapperManuBar = new WrapperMenuBar(this);

      WrapperMenuItem menuItemBearbeitenAutoanlegen = new WrapperMenuItem(LPMain.
          getTextRespectUISPr("fert.wiederholendelose.autoanlegen"),
          RechteFac.RECHT_FERT_LOS_CUD);
      menuItemBearbeitenAutoanlegen.addActionListener(this);
      menuItemBearbeitenAutoanlegen.setActionCommand(
          MENUE_ACTION_BEARBEITEN_AUTO_ANLEGEN);

      JMenu jmBearbeiten = (JMenu) wrapperManuBar.getComponent(WrapperMenuBar.
          MENU_BEARBEITEN);
      jmBearbeiten.add(menuItemBearbeitenAutoanlegen);

    }
    return wrapperManuBar;

  }

}
