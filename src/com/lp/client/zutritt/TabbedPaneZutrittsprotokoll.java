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
package com.lp.client.zutritt;


import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
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
public class TabbedPaneZutrittsprotokoll
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private PanelQuery panelQueryZutrittsprotokoll = null;

  private final static int IDX_PANEL_ZUTRITTSPROTOKOLL = 0;

  private final String MENUE_JOURNAL_ACTION_ZUTRITTSPROTOKOLL =
      "MENUE_JOURNAL_ACTION_ZUTRITTSPROTOKOLL";

  private WrapperMenuBar wrapperMenuBar = null;

  public TabbedPaneZutrittsprotokoll(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("lp.protokoll"));

    jbInit();
    initComponents();
  }


  public InternalFrameZutritt getInternalFrameZutritt() {
    return (InternalFrameZutritt) getInternalFrame();
  }


  private void jbInit()
      throws Throwable {
    //Kollektiv

    //1 tab oben: QP1 PartnerFLR; lazy loading
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.protokoll"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.protokoll"),
        IDX_PANEL_ZUTRITTSPROTOKOLL);

    panelQueryZutrittsprotokoll = new PanelQuery(
        null,
        ZutrittFilterFactory.getInstance().createFKZutrittsprotokoll(
            getInternalFrameZutritt().isBIstHauptmandant()),
        QueryParameters.UC_ID_ZUTRITTSLOG,
        null,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.zutritte"), true);

    panelQueryZutrittsprotokoll.befuellePanelFilterkriterienDirekt( ZutrittFilterFactory.
        getInstance().createFKDZutrittslogPersonal(),ZutrittFilterFactory.
        getInstance().createFKDZutrittslogObjekt());

    setComponentAt(IDX_PANEL_ZUTRITTSPROTOKOLL, panelQueryZutrittsprotokoll);

    //p//anelQueryZutrittsprotokoll.befuellePanelFilterkriterienDirekt(ArtikelFilterFactory.
    //  getInstance().createFKDArtikelnummer(getInternalFrame()),
    // ArtikelFilterFactory.getInstance().createFKDVolltextsuche());
    setComponentAt(IDX_PANEL_ZUTRITTSPROTOKOLL, panelQueryZutrittsprotokoll);

    // Itemevents an MEIN Detailpanel senden kann.
    this.addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);

  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {

    }

  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANEL_ZUTRITTSPROTOKOLL) {
      panelQueryZutrittsprotokoll.eventYouAreSelected(false);
      if (panelQueryZutrittsprotokoll.getSelectedId() == null) {
        getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_ZUTRITTSPROTOKOLL, false);
      }
      panelQueryZutrittsprotokoll.updateButtons();

    }

  }
  protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_ZUTRITTSPROTOKOLL)) {

      String add2Title = LPMain.getInstance().getTextRespectUISPr(
          "pers.zutritt.zutrittsprotokoll");
       getInternalFrame().showReportKriterien(new ReportZutrittsprotokoll(
           getInternalFrameZutritt(), add2Title));
    }
  }


  public javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {

      wrapperMenuBar = new WrapperMenuBar(this);

      JMenuItem menuItemZutrittsprotokoll = new JMenuItem(LPMain.getInstance().
          getTextRespectUISPr("pers.zutritt.zutrittsprotokoll"));

      menuItemZutrittsprotokoll.addActionListener(this);
      menuItemZutrittsprotokoll.setActionCommand(MENUE_JOURNAL_ACTION_ZUTRITTSPROTOKOLL);

      JMenu journal = (JMenu) wrapperMenuBar.getComponent(
          WrapperMenuBar.MENU_JOURNAL);
      journal.add(menuItemZutrittsprotokoll);

    }
    return wrapperMenuBar;

  }

}
