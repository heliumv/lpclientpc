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

import com.lp.client.frame.HelperClient;
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
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
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
 * @version $Revision: 1.5 $
 */
public class TabbedPanePanel
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private PanelQuery panelQueryAuswahl
      = null;

  private PanelSplit panelSplitBeschreibung = null;
  private PanelQuery panelQueryBeschreibung = null;
  private PanelPanelbeschreibung panelBottomBeschreibung = null;

  private PanelBasis panelDetailVorschau = null;


  //Reihenfolge der Panels
  private static final int IDX_PANEL_AUSWAHL = 0;
  private static final int IDX_PANEL_BESCHREIBUNG = 1;
  private static final int IDX_PANEL_VORSCHAU = 2;

  public TabbedPanePanel(InternalFrame internalFrameI)
      throws Throwable {

    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("lp.eigenschaftsdefinition"));

    jbInit();
    initComponents();
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
        LPMain.getInstance().getTextRespectUISPr("lp.beschreibung"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.beschreibung"),
        IDX_PANEL_BESCHREIBUNG);
      insertTab(
          LPMain.getInstance().getTextRespectUISPr("lp.vorschau"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("lp.vorschau"),
          IDX_PANEL_VORSCHAU);

    createAuswahl();

    panelQueryAuswahl.eventYouAreSelected(false);
    if ( (String) panelQueryAuswahl.getSelectedId() != null) {
      getInternalFrameSystem().setPanelDto(DelegateFactory.getInstance().
                                           getPanelDelegate().
                                           panelFindByPrimaryKey( (
                                               String)
          panelQueryAuswahl.getSelectedId()));
    }
    //damit D2 einen aktuellen hat.
    ItemChangedEvent it = new ItemChangedEvent(panelQueryAuswahl,
                                               ItemChangedEvent.ITEM_CHANGED);
    lPEventItemChanged(it);

    this.addChangeListener(this);
    this.getInternalFrame().addItemChangedListener(this);

  }


  private void createAuswahl()
      throws Throwable {
    if (panelQueryAuswahl == null) {
      //Artikelauswahlliste
      panelQueryAuswahl = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_PANEL,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "lp.auswahl"), true);

      panelQueryAuswahl.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
          getInstance().createFKDKennung(), null);

      setComponentAt(IDX_PANEL_AUSWAHL, panelQueryAuswahl);

    }
  }

  private void createVorschau(String key)
      throws Throwable {

    String[] aWhichButtonIUse = new String[0];

      panelDetailVorschau = new PanelDynamisch(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.vorschau"),
          panelQueryAuswahl,key, HelperClient.LOCKME_PANELBESCHREIBUNG,aWhichButtonIUse);
      setComponentAt(IDX_PANEL_VORSCHAU, panelDetailVorschau);
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

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryAuswahl) {
        String cNr = (String) panelQueryAuswahl.getSelectedId();
        if (cNr != null) {
          setSelectedComponent(panelSplitBeschreibung);
        }
      }
    }
    else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (e.getSource() == panelQueryBeschreibung) {
        Integer iId = (Integer) panelQueryBeschreibung.getSelectedId();
        panelBottomBeschreibung.setKeyWhenDetailPanel(iId);
        panelBottomBeschreibung.eventYouAreSelected(false);
        panelQueryBeschreibung.updateButtons();
      }
      else if (e.getSource() == panelQueryAuswahl) {
        if (panelQueryAuswahl.getSelectedId() != null) {
          Object key = ( (ISourceEvent) e.getSource()).getIdSelected();

          getInternalFrameSystem().setKeyWasForLockMe(
              key + "");

          getInternalFrameSystem().setPanelDto(DelegateFactory.getInstance().
              getPanelDelegate().panelFindByPrimaryKey( (String)
              key));
          getInternalFrame().setLpTitle(
              InternalFrame.TITLE_IDX_AS_I_LIKE,
              LPMain.getInstance().getTextRespectUISPr("label.kennung") + ": " +
              getInternalFrameSystem().getPanelDto().getCNr());

          if (panelQueryAuswahl.getSelectedId() == null) {
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
    else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
      // hier kommt man nach upd im D bei einem 1:n hin.
      if (eI.getSource() == this.panelBottomBeschreibung) {
        //im QP die Buttons in den Zustand neu setzen.
        this.panelQueryBeschreibung.updateButtons(new LockStateValue(PanelBasis.
            LOCK_FOR_NEW));
        ;
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == panelBottomBeschreibung) {
        panelSplitBeschreibung.eventYouAreSelected(false);
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == panelBottomBeschreibung) {
        Object oKey = panelBottomBeschreibung.getKeyWhenDetailPanel();
        panelQueryBeschreibung.eventYouAreSelected(false);
        panelQueryBeschreibung.setSelectedId(oKey);
        panelSplitBeschreibung.eventYouAreSelected(false);
      }
    }
	else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
		if (eI.getSource() == panelQueryBeschreibung) {
			panelBottomBeschreibung.eventActionNew(eI, true, false);
			panelBottomBeschreibung.eventYouAreSelected(false); // Buttons
			// schalten
		}
	}
    // wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelBottomBeschreibung) {
        panelSplitBeschreibung.eventYouAreSelected(false);
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
      if (eI.getSource() == panelQueryBeschreibung) {
        if (panelQueryBeschreibung.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomBeschreibung.eventActionNew(eI, true, false);
        panelBottomBeschreibung.eventYouAreSelected(false);
        setSelectedComponent(panelSplitBeschreibung);
      }
    }
  }


  private InternalFrameSystem getInternalFrameSystem() {
    return (InternalFrameSystem) getInternalFrame();
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);

    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANEL_AUSWAHL) {
      createAuswahl();
      panelQueryAuswahl.eventYouAreSelected(false);
      if (panelQueryAuswahl.getSelectedId() == null) {
        getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
      }

      panelQueryAuswahl.updateButtons();
    }

    else if (selectedIndex == IDX_PANEL_BESCHREIBUNG) {
      createPanelbeschreibung(getInternalFrameSystem().getPanelDto().getCNr());
      panelSplitBeschreibung.eventYouAreSelected(false);
      panelQueryBeschreibung.updateButtons();
    }
    else if (selectedIndex == IDX_PANEL_VORSCHAU) {
      createVorschau((String)panelQueryAuswahl.getSelectedId());
      panelDetailVorschau.eventYouAreSelected(false);
    }
  }



  private void createPanelbeschreibung(
      String key)
      throws Throwable {

    if (panelQueryBeschreibung == null) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW,PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,};

      FilterKriterium[] filters = SystemFilterFactory.getInstance().
          createFKPanelbeschreibung(key);

      panelQueryBeschreibung = new PanelQuery(
          null,
          filters,
          QueryParameters.UC_ID_PANELBESCHREIBUNG,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "lp.beschreibung"), true);

      panelBottomBeschreibung = new PanelPanelbeschreibung(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "lp.beschreibung"), null);

      panelSplitBeschreibung = new PanelSplit(
          getInternalFrame(),
          panelBottomBeschreibung,
          panelQueryBeschreibung,
          180);

      setComponentAt(IDX_PANEL_BESCHREIBUNG, panelSplitBeschreibung);
    }
    else {
      //filter refreshen.
      panelQueryBeschreibung.setDefaultFilter(
          SystemFilterFactory.getInstance().createFKPanelbeschreibung(key));
    }
  }


  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }

}
