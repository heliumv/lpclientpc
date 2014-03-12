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
package com.lp.client.finanz;


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
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access") 
/**
 * <p><I>Diese Klasse kuemmert sich um die Ergebnisgruppen</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>13.01.05</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.3 $
 */
public class TabbedPaneErgebnisgruppen
    extends TabbedPane
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelSplit panelSplitErgebnisgruppen = null;
  private PanelQuery panelQueryErgebnisgruppen = null;
  private PanelFinanzErgebnisgruppeKopfdaten panelFinanzErgebnisgruppeKopfdaten = null;
  private PanelQuery panelQueryKonten = null;
  private ErgebnisgruppeDto ergebnisgruppeDto = null;
  public boolean bBilanzgruppe = false;

  public final static int IDX_ERGEBNISGRUPPEN = 0;
  public final static int IDX_KONTEN = 1;

  public TabbedPaneErgebnisgruppen(InternalFrame internalFrameI,String title, boolean bBilanzgruppe)
      throws Throwable {
	    super(internalFrameI, title);
    this.bBilanzgruppe=bBilanzgruppe;
    jbInit();
    initComponents();
  }


  void setErgebnisgruppeDto(ErgebnisgruppeDto ergebnisgruppeDto)
      throws Throwable {
    this.ergebnisgruppeDto = ergebnisgruppeDto;
    if (getErgebnisgruppeDto() != null) {
      getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
                                    getErgebnisgruppeDto().getCBez());
    }
    refreshFiltersKonten();
  }


  ErgebnisgruppeDto getErgebnisgruppeDto() {
    return ergebnisgruppeDto;
  }


  /**
   * jbInit.
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    // Tab 1: Liste der Konten
	  
	  if(bBilanzgruppe){
		  insertTab(LPMain.getInstance().getTextRespectUISPr("finanz.tab.unten.bilanzgruppen.title"),
	              null,
	              null,
	              LPMain.getInstance().getTextRespectUISPr("finanz.tab.unten.bilanzgruppen.title"),
	              IDX_ERGEBNISGRUPPEN);
	  } else {
		  insertTab(LPMain.getInstance().getTextRespectUISPr("finanz.ergebnisgruppen"),
	              null,
	              null,
	              LPMain.getInstance().getTextRespectUISPr("finanz.ergebnisgruppen"),
	              IDX_ERGEBNISGRUPPEN);
	  }
	  
  
    // Tab 2: Kopfdaten
    insertTab(LPMain.getInstance().getTextRespectUISPr("finanz.konten"),
              null,
              null,
              LPMain.getInstance().getTextRespectUISPr("finanz.konten"),
              IDX_KONTEN);

    // Default
    this.setSelectedComponent(getPanelSplitErgebnisgruppen(true));
    // damit gleich eine selektiert ist
    ItemChangedEvent it = new ItemChangedEvent(getPanelQueryErgebnisgruppen(true),
                                               ItemChangedEvent.ITEM_CHANGED);
    lPEventItemChanged(it);
    // Listener
    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      // da will jemand die Buchungen ansehen
      if (e.getSource() == getPanelQueryErgebnisgruppen(false)) {
        Object key = getPanelQueryErgebnisgruppen(true).getSelectedId();
        holeErgebnisgruppeDto(key);
        // nur wechseln wenns auch einen gibt
        if (key != null) {
          getPanelDetailErgebnisgruppeKopfdaten(true).eventYouAreSelected(false);
        }
      }
    }
    else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == getPanelQueryErgebnisgruppen(false)) {
        Object key = getPanelQueryErgebnisgruppen(true).getSelectedId();
        holeErgebnisgruppeDto(key);
        if (key == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_ERGEBNISGRUPPEN, false);
        }
        else {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_ERGEBNISGRUPPEN, true);
        }
        getPanelQueryErgebnisgruppen(true).updateButtons();
        getPanelDetailErgebnisgruppeKopfdaten(true).eventYouAreSelected(false);
      }
      if (e.getSource() == getPanelQueryKonten(false)) {
        getPanelQueryKonten(true).updateButtons();
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == getPanelQueryErgebnisgruppen(false)) {
        if (getPanelQueryErgebnisgruppen(true).getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        getPanelDetailErgebnisgruppeKopfdaten(true).eventActionNew(null, true, false);
        // Lockstate
        LockStateValue lockstateValue = new LockStateValue(
            null,
            null,
            PanelBasis.LOCK_FOR_NEW);
        getPanelDetailErgebnisgruppeKopfdaten(true).updateButtons(lockstateValue);
        getPanelQueryErgebnisgruppen(true).updateButtons();
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == getPanelDetailErgebnisgruppeKopfdaten(false)) {
        Object key = getPanelDetailErgebnisgruppeKopfdaten(true).getKeyWhenDetailPanel();
        getPanelSplitErgebnisgruppen(true).eventYouAreSelected(false);
        getPanelQueryErgebnisgruppen(true).setSelectedId(key);
        getPanelSplitErgebnisgruppen(true).eventYouAreSelected(false);
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == getPanelDetailErgebnisgruppeKopfdaten(false)) {
        getPanelSplitErgebnisgruppen(true).eventYouAreSelected(false);
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == getPanelDetailErgebnisgruppeKopfdaten(false)) {
        setErgebnisgruppeDto(null);
        getPanelQueryErgebnisgruppen(true).eventYouAreSelected(false);
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
      if (e.getSource() == getPanelQueryErgebnisgruppen(false)) {
        int iPos = getPanelQueryErgebnisgruppen(true).getTable().getSelectedRow();
        // wenn die Position nicht die erste ist
        if (iPos > 0) {
          Integer iIdPosition = (Integer) getPanelQueryErgebnisgruppen(true).
              getSelectedId();
          Integer iIdPositionMinus1 = (Integer) getPanelQueryErgebnisgruppen(true).
              getTable().
              getValueAt(iPos - 1, 0);
          DelegateFactory.getInstance().getFinanzDelegate().vertauscheErgebnisgruppen(
              iIdPosition, iIdPositionMinus1);
          // die Liste neu anzeigen und den richtigen Datensatz markieren
          getPanelQueryErgebnisgruppen(true).setSelectedId(iIdPosition);
        }
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
      if (e.getSource() == getPanelQueryErgebnisgruppen(false)) {
        int iPos = getPanelQueryErgebnisgruppen(true).getTable().getSelectedRow();
        // wenn die Position nicht die letzte ist
        if (iPos < getPanelQueryErgebnisgruppen(true).getTable().getRowCount() - 1) {
          Integer iIdPosition = (Integer) getPanelQueryErgebnisgruppen(true).
              getSelectedId();
          Integer iIdPositionPlus1 = (Integer) getPanelQueryErgebnisgruppen(true).
              getTable().
              getValueAt(iPos + 1, 0);
          DelegateFactory.getInstance().getFinanzDelegate().vertauscheErgebnisgruppen(
              iIdPosition, iIdPositionPlus1); // die Liste neu anzeigen und den richtigen Datensatz markieren
          getPanelQueryErgebnisgruppen(true).setSelectedId(iIdPosition);
        }
      }
    }
  }


  /**
   * lPActionEvent.
   *
   * @param e ActionEvent
   */
  protected void lPActionEvent(ActionEvent e) {
  }


  /**
   * lPEventObjectChanged.
   *
   * @param e ChangeEvent
   * @throws Throwable
   */
  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    // lazy loading und titel setzen
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();
    if (selectedIndex == IDX_ERGEBNISGRUPPEN) {
      getPanelSplitErgebnisgruppen(true).eventYouAreSelected(false);
    }
    else if (selectedIndex == IDX_KONTEN) {
      getPanelQueryKonten(true).eventYouAreSelected(false);
      refreshFiltersKonten();
    }
  }


  /**
   * hole KassenbuchDto.
   *
   * @param key Object
   * @throws Throwable
   */
  private void holeErgebnisgruppeDto(Object key)
      throws Throwable {
    if (key != null) {
      ErgebnisgruppeDto kassenbuchDto = DelegateFactory.getInstance().getFinanzDelegate().
          ergebnisgruppeFindByPrimaryKey( (Integer) key);
      setErgebnisgruppeDto(kassenbuchDto);
      getInternalFrame().setKeyWasForLockMe(key.toString());
    }
    getPanelDetailErgebnisgruppeKopfdaten(true).setKeyWhenDetailPanel(key);
  }


  /**
   * getPanelDetailErgebnisgruppeKopfdaten mit extrem lazy loading.
   *
   * @param bNeedInstantiationIfNull boolean
   * @return PanelFinanzKassenbuchKopfdaten
   * @throws Throwable
   */
  private PanelFinanzErgebnisgruppeKopfdaten getPanelDetailErgebnisgruppeKopfdaten(boolean
      bNeedInstantiationIfNull)
      throws Throwable {
    if (panelFinanzErgebnisgruppeKopfdaten == null && bNeedInstantiationIfNull) {
      panelFinanzErgebnisgruppeKopfdaten = new PanelFinanzErgebnisgruppeKopfdaten(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
          this);
    }
    return panelFinanzErgebnisgruppeKopfdaten;
  }


  private PanelQuery getPanelQueryErgebnisgruppen(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelQueryErgebnisgruppen == null && bNeedInstantiationIfNull) {
      String[] aWhichButtonIUseErgebnisgruppen = {
          PanelBasis.ACTION_NEW,
          PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
          PanelBasis.ACTION_POSITION_VONNNACHNPLUS1};
      QueryType[] qtErgebnisgruppen = null;
     

      panelQueryErgebnisgruppen = new PanelQuery(
          qtErgebnisgruppen,
          FinanzFilterFactory.getInstance().createFKErgebnisgruppe(bBilanzgruppe),
          QueryParameters.UC_ID_ERGEBNISGRUPPE,
          aWhichButtonIUseErgebnisgruppen,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), true);
    }
    return panelQueryErgebnisgruppen;
  }


  private PanelSplit getPanelSplitErgebnisgruppen(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelSplitErgebnisgruppen == null && bNeedInstantiationIfNull) {
      panelSplitErgebnisgruppen = new PanelSplit(getInternalFrame(),
                                                 getPanelDetailErgebnisgruppeKopfdaten(true),
                                                 getPanelQueryErgebnisgruppen(true), 200);
      this.setComponentAt(IDX_ERGEBNISGRUPPEN, panelSplitErgebnisgruppen);
    }
    return panelSplitErgebnisgruppen;
  }


  /**
   * initPanelTop1QueryKonten.
   *
   * @return PanelQuery
   * @param bNeedInstantiationIfNull boolean
   * @throws Throwable
   */
  private PanelQuery getPanelQueryKonten(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelQueryKonten == null && bNeedInstantiationIfNull) {
      String[] aWhichButtonIUseKonten = {
          };
      QueryType[] qtKonten = null;
      FilterKriterium[] filtersKonten = null;

      panelQueryKonten = new PanelQuery(
          qtKonten,
          filtersKonten,
          QueryParameters.UC_ID_FINANZKONTEN,
          aWhichButtonIUseKonten,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("finanz.konten"), true);
      FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().
          createFKDKontonummer();
      FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().
          createFKDKontobezeichnung();
      panelQueryKonten.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
      this.setComponentAt(IDX_KONTEN, panelQueryKonten);
    }
    return panelQueryKonten;
  }


  private void refreshFiltersKonten()
      throws Throwable {
    FilterKriterium[] filters = new FilterKriterium[1];
    Integer key = null;
    if (getErgebnisgruppeDto() != null) {
      key = getErgebnisgruppeDto().getIId();
    }
    if (key == null) {
      key = new Integer( -1);
    }
    filters = FinanzFilterFactory.getInstance().createFKKontenEinerErgebnisgruppe(key);
    getPanelQueryKonten(true).setDefaultFilter(filters);
  }


  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }


  public Object getInseratDto() {
    return ergebnisgruppeDto;
  }

}

