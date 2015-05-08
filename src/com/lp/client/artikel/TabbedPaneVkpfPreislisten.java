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
package com.lp.client.artikel;


import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
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
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>Tabbed pane fuer Komponente Preislisten.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-10-28</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */

public class TabbedPaneVkpfPreislisten
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public PanelQuery preislistennameTop = null;
  public PanelVkpfPreisliste preislistennameBottom = null;
  public PanelSplit preislistenname = null; // FLR 1:n Liste

  private final static int IDX_PANEL_PREISLISTEN = 0;

  private VkpfartikelpreislisteDto vkpfartikelpreislisteDto = null;

  public TabbedPaneVkpfPreislisten(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("vkpf.preislisten.title.tab"));

    jbInit();
    initComponents();
  }


  public VkpfartikelpreislisteDto getVkpfartikelpreislisteDto() {
    return vkpfartikelpreislisteDto;
  }


  public void setVkpfartikelpreislisteDto(VkpfartikelpreislisteDto
                                          vkpfartikelpreislisteDtoI) {
    vkpfartikelpreislisteDto = vkpfartikelpreislisteDtoI;
  }


  private void jbInit()
      throws Throwable {
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("vkpf.preisliste.title.panel.preisliste"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr(
        "vkpf.preisliste.title.tooltip.preisliste"),
        IDX_PANEL_PREISLISTEN);

    refreshPanelPreislisten();
    setSelectedComponent(preislistenname);
    preislistennameTop.eventYouAreSelected(false);
    setKeyWasForLockMe();
    setTitlePreisliste(LPMain.getInstance().getTextRespectUISPr("lp.detail"));
    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }


  private PanelSplit refreshPanelPreislisten()
      throws Throwable {
    if (preislistenname == null) {
      preislistennameBottom = new PanelVkpfPreisliste(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
          "vkpf.preisliste.title.panel.preisliste"),
          null);

      QueryType[] qtPreislistenname = null;
      FilterKriterium[] filtersPreislistenname = SystemFilterFactory.getInstance().
          createFKMandantCNr();

      // posreihung: 3 die zusaetzlichen Buttons am PanelQuery anbringen
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_NEW,
          PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
          PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
          PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN};

      preislistennameTop = new PanelQuery(
          qtPreislistenname,
          filtersPreislistenname,
          QueryParameters.UC_ID_PREISLISTENNAME,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
          "vkpf.preisliste.title.panel.preisliste"),
          true);

      preislistenname = new PanelSplit(
          getInternalFrame(),
          preislistennameBottom,
          preislistennameTop, 200);

      setComponentAt(IDX_PANEL_PREISLISTEN, preislistenname);
    }

    return preislistenname;
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);

    int selectedIndex = getSelectedIndex();

    switch (selectedIndex) {
      case IDX_PANEL_PREISLISTEN:

        // das Panel existiert auf jeden Fall
        preislistenname.eventYouAreSelected(false);
        preislistennameTop.updateButtons(preislistennameBottom.
                                         getLockedstateDetailMainKey());
        break;
    }
  }


  private void refreshCurrentPanel()
      throws Throwable {
    Integer iIdPreisliste = (Integer) vkpfartikelpreislisteDto.getIId();

    initializeDtos(iIdPreisliste);

    int selectedIndex = getSelectedIndex();

    switch (selectedIndex) {
      case IDX_PANEL_PREISLISTEN:
        preislistenname.eventYouAreSelected(false);

        // im QP die Buttons setzen.
        preislistennameTop.updateButtons(
            preislistennameBottom.getLockedstateDetailMainKey());
        break;
    }

    setTitlePreisliste(LPMain.getInstance().getTextRespectUISPr("lp.detail"));
  }


  private void initializeDtos(Integer iIdPreislisteI)
      throws Throwable {
    if (iIdPreislisteI != null) {
      vkpfartikelpreislisteDto = DelegateFactory.getInstance().getVkPreisfindungDelegate().
          vkpfartikelpreislisteFindByPrimaryKey(iIdPreislisteI);
    }
  }


  public void resetDtos() {
    vkpfartikelpreislisteDto = new VkpfartikelpreislisteDto();
  }


  public void setTitlePreisliste(String sTitleOhrwaschloben)
      throws Throwable {
    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
                                  LPMain.
                                  getInstance().getTextRespectUISPr("vkpf.preislisten.title.tab"));
    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
                                  sTitleOhrwaschloben);

    // TITLE_IDX_AS_I_LIKE setzen
    String sPreisliste = "";

    if (vkpfartikelpreislisteDto == null || vkpfartikelpreislisteDto.getIId() == null) {
      sPreisliste = LPMain.getInstance().getTextRespectUISPr("vkpf.neuepreisliste");
    }
    else {
      sPreisliste = vkpfartikelpreislisteDto.getCNr();
    }

    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sPreisliste);
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {
    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) { // Zeile in Tabelle gewaehlt
      if (e.getSource() == preislistennameTop) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        preislistennameBottom.setKeyWhenDetailPanel(key);
        preislistennameBottom.eventYouAreSelected(false);

        // im QP die Buttons in den Zustand nolocking/save setzen.
        preislistennameTop.updateButtons(
            preislistennameBottom.getLockedstateDetailMainKey());
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      // hier kommt man nach upd im D bei einem 1:n hin.
      if (e.getSource() == preislistennameBottom) {
        // im QP die Buttons in den Zustand neu setzen.
        preislistennameTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
      setTitlePreisliste(LPMain.getInstance().getTextRespectUISPr("lp.detail"));
      preislistenname.eventYouAreSelected(false);
    }
    else
    if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == preislistennameTop) {
        preislistennameBottom.eventActionNew(e, true, false); // keyForLockMe setzen
        preislistennameBottom.eventYouAreSelected(false);
        setSelectedComponent(preislistenname); // ui
      }
    }
    else

    // wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
    if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == preislistennameBottom) {
        setKeyWasForLockMe();
        preislistenname.eventYouAreSelected(false);
      }
    }

    // wir landen hier bei der Abfolge Button Aendern -> xx -> Button Discard
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (e.getSource() == preislistennameBottom) {
        preislistenname.eventYouAreSelected(false); // das 1:n Panel neu aufbauen
      }
    }

    // markierenachsave: 0 Wir landen hier nach Button Save
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (e.getSource() == preislistennameBottom) {
        // markierenachsave: 1 den Key des Datensatzes merken
        Object oKey = preislistennameBottom.getKeyWhenDetailPanel();

        // markierenachsave: 2 die Liste neu aufbauen
        preislistennameTop.eventYouAreSelected(false);

        // markierenachsave: 3 den Datensatz in der Liste selektieren
        preislistennameTop.setSelectedId(oKey);

        // markierenachsave: 4 im Detail den selektierten anzeigen
        preislistenname.eventYouAreSelected(false);
      }
    }
    else

    // posreihung: 4 Einer der Knoepfe zur Reihung der Positionen auf einem PanelQuery wurde gedrueckt
    if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
      if (e.getSource() == preislistennameTop) {
        int iPos = preislistennameTop.getTable().getSelectedRow();

        // wenn die Position nicht die erste ist
        if (iPos > 0) {
          Integer iIdPosition = (Integer) preislistennameTop.getSelectedId();

          Integer iIdPositionMinus1 = (Integer) preislistennameTop.getTable().
              getValueAt(iPos - 1, 0);

          DelegateFactory.getInstance().getVkPreisfindungDelegate().vertauscheVkpfartikelpreisliste(
              iIdPosition, iIdPositionMinus1);

          // die Liste neu anzeigen und den richtigen Datensatz markieren
          preislistennameTop.setSelectedId(iIdPosition);
        }
      }
    }
    else
    if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
      if (e.getSource() == preislistennameTop) {
        int iPos = preislistennameTop.getTable().getSelectedRow();

        // wenn die Position nicht die letzte ist
        if (iPos < preislistennameTop.getTable().getRowCount() - 1) {
          Integer iIdPosition = (Integer) preislistennameTop.getSelectedId();

          Integer iIdPositionPlus1 = (Integer) preislistennameTop.getTable().getValueAt(
              iPos + 1, 0);

          DelegateFactory.getInstance().getVkPreisfindungDelegate().vertauscheVkpfartikelpreisliste(
              iIdPosition, iIdPositionPlus1);

          // die Liste neu anzeigen und den richtigen Datensatz markieren
          preislistennameTop.setSelectedId(iIdPosition);
        }
      }
    }
    else
    if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
      if (e.getSource() == preislistennameTop) {
        preislistennameBottom.eventActionNew(e, true, false);
        preislistennameBottom.eventYouAreSelected(false); // Buttons schalten
      }
    }
  }


  protected void lPActionEvent(java.awt.event.ActionEvent e) {
    e.getActionCommand();
  }


  public PanelQuery getPreislistennameTop() {
    return this.preislistennameTop;
  }


  /**
   * Diese Methode setzt ide aktuelle Preisliste als den zu
   * lockenden Auftrag.
   */
  public void setKeyWasForLockMe() {
    Object oKey = preislistennameTop.getSelectedId();

    if (oKey != null) {
      getInternalFrame().setKeyWasForLockMe(oKey.toString());
    }
    else {
      getInternalFrame().setKeyWasForLockMe(null);
    }
  }


  protected boolean handleOwnException(ExceptionLP exfc) {
    boolean bErrorErkannt = true;
    int code = exfc.getICode();

    switch (code) {
      case EJBExceptionLP.FEHLER_VKPF_MAXIMALZEHNPREISLISTEN:
        DialogFactory.showModalDialog(
            LPMain.getInstance().getTextRespectUISPr("lp.hint"),
            LPMain.getInstance().getTextRespectUISPr(
                "vkpf.hint.maximalzehnpreislisten"));

        try {
          preislistenname.eventYouAreSelected(false); // @JO hier sitzt ein Lock drauf???
        }
        catch (Throwable t) {
          super.handleException(t,true);
        }

        break
            ;

      default:
        bErrorErkannt = false;
    }

    return bErrorErkannt;
  }


  protected JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }

  public Object getDto() {
	return vkpfartikelpreislisteDto;
  }
}
