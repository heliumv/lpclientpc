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
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um textmodule, bilder, etc</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>21.04.05</I></p>
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.3 $
 */
public class TabbedPaneMediastandard
    extends TabbedPane
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelSplit panelSplitMedia = null;
  private PanelMediastandard panelMedia = null;
  private PanelQuery panelQueryMedia = null;

  private PanelSplit panelSplitMediaart = null;
  private PanelStammdatenCRUD panelBottomMediaart = null;
  private PanelQuery panelQueryMediaart = null;


  //Reihenfolge der Panels
  private static final int IDX_PANEL_MEDIA = 0;
  private static final int IDX_PANEL_MEDIAART = 1;


  public TabbedPaneMediastandard(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("lp.medien"));
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    /**
     * Ortpanel lazy loading*/
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.medien"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.medien.tooltip"),
        IDX_PANEL_MEDIA);

    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.mediaart"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.mediaart"),
        IDX_PANEL_MEDIAART);

    setSelectedComponent(getPanelSplitMedia(true));
    getPanelQueryMedia(true).eventYouAreSelected(false);
    //damit D1 einen aktuellen hat. (kuenstliches abfeuern)
    ItemChangedEvent it = new ItemChangedEvent(panelQueryMedia,
                                               ItemChangedEvent.ITEM_CHANGED);
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
      if (e.getSource() == panelQueryMedia) {
        Integer iId = (Integer) panelQueryMedia.getSelectedId();
        //key 1; IF
        getInternalFrame().setKeyWasForLockMe(iId + "");
        //key2; PBottom
        panelMedia.setKeyWhenDetailPanel(iId);
        panelMedia.eventYouAreSelected(false);
        panelQueryMedia.updateButtons();
      }
      else if (e.getSource() == panelQueryMediaart) {
        String cNr = (String) panelQueryMediaart.getSelectedId();
        getInternalFrame().setKeyWasForLockMe(cNr + "");
        panelBottomMediaart.setKeyWhenDetailPanel(cNr);
        panelBottomMediaart.eventYouAreSelected(false);
        panelQueryMediaart.updateButtons();
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (e.getSource() == panelMedia) {
        panelQueryMedia.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
      else if (e.getSource() == panelBottomMediaart) {
        panelQueryMediaart.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW)); ;
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
      // wir landen hier bei der Abfolge Button Aendern -> xx -> Button Discard
      if (e.getSource() == panelMedia) {
        panelSplitMedia.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomMediaart) {
        panelSplitMediaart.eventYouAreSelected(false);
      }

    }

    //selektiert nach save: 0 Wir landen hier nach Button Save
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

      if (e.getSource() == panelMedia) {
        Object oKey = panelMedia.getKeyWhenDetailPanel();
        panelQueryMedia.eventYouAreSelected(false);
        panelQueryMedia.setSelectedId(oKey);
        panelSplitMedia.eventYouAreSelected(false);
      }
      else if (e.getSource() == panelBottomMediaart) {
        Object oKey = panelBottomMediaart.getKeyWhenDetailPanel();
        panelQueryMediaart.eventYouAreSelected(false);
        panelQueryMediaart.setSelectedId(oKey);
        panelSplitMediaart.eventYouAreSelected(false);
      }
    }

    // wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == panelMedia) {
        Object oKey = panelQueryMedia.getSelectedId();
        //holt sich alten key wieder
        getInternalFrame().setKeyWasForLockMe(oKey == null ? null : oKey + "");
        panelSplitMedia.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
      else if (e.getSource() == panelBottomMediaart) {
        panelSplitMediaart.eventYouAreSelected(false);
      }
    }

    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
      if (e.getSource() == panelQueryMedia) {
        if (panelQueryMedia.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        //im panelOrtQP2 auf new gedrueckt.
        //jetzt in's richtige panel mit new.
        panelMedia.eventActionNew(e, true, false);
        //jetzt eventYouAreSelected ausloesen wegen naechster zeile.
        panelMedia.eventYouAreSelected(false);
        //auch ui maessig, fuehrt NICHT zu eventYouAreSelected
        setSelectedComponent(panelSplitMedia);
      }
      else if (e.getSource() == panelQueryMediaart) {
        if (panelQueryMediaart.getSelectedId() == null) {
          getInternalFrame().enableAllPanelsExcept(true);
        }
        panelBottomMediaart.eventActionNew(e, true, false);
        panelBottomMediaart.eventYouAreSelected(false);
        setSelectedComponent(panelSplitMediaart);
      }
    }
  }


  private InternalFrameSystem getInternalFrameSystem() {
    return (InternalFrameSystem) getInternalFrame();
  }


  /**
   * Behandle ChangeEvent; zB Tabwechsel oben.
   *
   * @param e ChangeEvent
   * @todo Implement this com.lp.client.frame.component.TabbedPane method
   * @throws Throwable
   */
  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    switch (selectedIndex) {
      case IDX_PANEL_MEDIA:
   //     getPanelSplitMedia(true).eventYouAreSelected(false);
        refreshPanelMediastandard();
        panelSplitMedia.eventYouAreSelected(false);
        break;

      case IDX_PANEL_MEDIAART:
        refreshPanelMediaart();
        panelQueryMediaart.eventYouAreSelected(false);

        // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
        //   die restlichen oberen Laschen deaktivieren, ausser ...
        if (panelQueryMediaart.getSelectedId() == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_MEDIAART, false);
        }
        break;
    }
  }


  private PanelSplit getPanelSplitMedia(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelSplitMedia == null && bNeedInstantiationIfNull) {

      panelSplitMedia = new PanelSplit(getInternalFrame(),
                                       getPanelMedia(true),
                                       getPanelQueryMedia(true), 200);
      this.setComponentAt(IDX_PANEL_MEDIA, panelSplitMedia);
    }
    return panelSplitMedia;
  }


  private PanelMediastandard getPanelMedia(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelMedia == null && bNeedInstantiationIfNull) {
      panelMedia = new PanelMediastandard(
          getInternalFrame(),
          "",
          null,
          this);
    }
    return panelMedia;
  }


  private PanelQuery getPanelQueryMedia(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelQueryMedia == null && bNeedInstantiationIfNull) {
      String[] aWhichButtonIUseAuftrag = {
          PanelBasis.ACTION_NEW};
      QueryType[] qtMediastandard = null;
      FilterKriterium[] filtersMediastandard = SystemFilterFactory.getInstance().
          createFKMandantCNrLocaleCNr();

      panelQueryMedia = new PanelQuery(
          qtMediastandard,
          filtersMediastandard,
          QueryParameters.UC_ID_MEDIASTANDARD,
          aWhichButtonIUseAuftrag,
          getInternalFrame(),
          "", true);
      panelQueryMedia.befuellePanelFilterkriterienDirektUndVersteckte(
          SystemFilterFactory.getInstance().createFKDKennung(),
          null,SystemFilterFactory.getInstance().createFKVMediastandard());
    }
    return panelQueryMedia;
  }


  private void refreshPanelMediaart()
      throws Throwable {

    if (panelSplitMediaart == null) {
      String[] aWhichStandardButtonIUse = null;
      panelQueryMediaart = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_MEDIAART,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.mediaart"),
          true);

      panelBottomMediaart = new PanelStammdatenCRUD(
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("lp.mediaart"),
          null,
          HelperClient.SCRUD_MEDIAART_FILE,
          getInternalFrameSystem(),
          HelperClient.LOCKME_MEDIAART);

      panelSplitMediaart = new PanelSplit(
          getInternalFrame(),
          panelBottomMediaart,
          panelQueryMediaart,
          400 - ( (PanelStammdatenCRUD) panelBottomMediaart).
          getAnzahlControls() * 30);

      setComponentAt(IDX_PANEL_MEDIAART, panelSplitMediaart);
    }
  }


  private void refreshPanelMediastandard()
      throws Throwable {
    if (panelSplitMedia == null) {
      // der Eigentumsvorbehalt muss hinterlegt sein oder werden
      try {
        DelegateFactory.getInstance().getMediaDelegate().
            mediastandardFindByCNrDatenformatCNr(
                MediaFac.MEDIAART_EIGENTUMSVORBEHALT,
                MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
      }
      catch (Throwable t) {
        DelegateFactory.getInstance().getMediaDelegate().createDefaultMediastandard(
            MediaFac.MEDIAART_EIGENTUMSVORBEHALT, LPMain.getTheClient().getLocUiAsString());
      }

      // die Lieferbedingungen muss hinterlegt sein oder werden
      try {
        DelegateFactory.getInstance().getMediaDelegate().
            mediastandardFindByCNrDatenformatCNr(
                MediaFac.MEDIAART_LIEFERBEDINGUNGEN,
                MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
      }
      catch (Throwable t) {
        DelegateFactory.getInstance().getMediaDelegate().createDefaultMediastandard(
            MediaFac.MEDIAART_LIEFERBEDINGUNGEN, LPMain.getTheClient().getLocUiAsString());
      }

      String[] aWhichButtonIUseAuftrag = {
          PanelBasis.ACTION_NEW};
      QueryType[] qtMediastandard = null;
      FilterKriterium[] filtersMediastandard = SystemFilterFactory.getInstance().
          createFKMandantCNrLocaleCNr();

      panelQueryMedia = new PanelQuery(
          qtMediastandard,
          filtersMediastandard,
          QueryParameters.UC_ID_MEDIASTANDARD,
          aWhichButtonIUseAuftrag,
          getInternalFrame(),
          "", true);

      panelQueryMedia.befuellePanelFilterkriterienDirekt(
          SystemFilterFactory.getInstance().createFKDKennung(),
          null);

      panelMedia = new PanelMediastandard(
          getInternalFrame(),
          "",
          null,
          this);

      panelSplitMedia = new PanelSplit(getInternalFrame(),
                                       panelMedia,
                                       panelQueryMedia, 200);
      this.setComponentAt(IDX_PANEL_MEDIA, panelSplitMedia);
    }

  }

  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }

}
