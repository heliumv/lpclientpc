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


import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.ZutrittsklasseDto;
import com.lp.server.personal.service.ZutrittsmodellDto;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */

public class InternalFrameZutritt
    extends InternalFrame
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TabbedPaneZutritt tabbedPaneZutritt = null;
  private TabbedPaneZutrittsmodell tabbedPaneZutrittsmodell = null;
  private TabbedPaneZutrittgrunddaten tabbedPaneGrunddaten = null;
  private TabbedPaneOnlinecheck tabbedPaneOnlinecheck = null;
  private TabbedPaneZutrittsprotokoll tabbedPaneZutrittsprotokoll = null;
  private TabbedPaneDaueroffen tabbedPaneDaueroffen = null;

  private int IDX_TABBED_PANE_ZUTRITT = -1;
  private int IDX_TABBED_PANE_ZUTRITTSMODELL = -1;
  private int IDX_TABBED_PANE_DAUEROFFEN = -1;
  private int IDX_TABBED_PANE_ONLINECHECK = -1;
  private int IDX_TABBED_PANE_ZUTRITTSPROTOKOLL = -1;
  private int IDX_TABBED_PANE_GRUNDDATEN = -1;
  private ZutrittsklasseDto zutrittsklasseDto;

  private ZutrittsmodellDto zutrittsmodellDto = null;
  private boolean bIstHauptmandant = false;

  public void setZutrittsmodellDto(ZutrittsmodellDto zutrittsmodellDto) {
    this.zutrittsmodellDto = zutrittsmodellDto;
  }


  public String sRechtModulweit = null;

  public void setBIstHauptmandant(boolean bIstHauptmandant) {
    this.bIstHauptmandant = bIstHauptmandant;
  }


  public void setZutrittsklasseDto(ZutrittsklasseDto zutrittsklasseDto) {
    this.zutrittsklasseDto = zutrittsklasseDto;
  }


  public TabbedPaneZutrittsmodell getTabbedPaneZutrittsmodell() {
    return tabbedPaneZutrittsmodell;
  }


  public ZutrittsmodellDto getZutrittsmodellDto() {
    return zutrittsmodellDto;
  }


  public boolean isBIstHauptmandant() {
    return bIstHauptmandant;
  }


  public ZutrittsklasseDto getZutrittsklasseDto() {
    return zutrittsklasseDto;
  }


  public InternalFrameZutritt(String title, String belegartCNr,
                              String sRechtModulweitI)
      throws Throwable {
    super(title, belegartCNr, sRechtModulweitI);
    sRechtModulweit = sRechtModulweitI;
    String hauptMandant = DelegateFactory.getInstance().getMandantDelegate().
        mandantFindByPrimaryKey(LPMain.getInstance().getTheClient().getMandant()).
        getAnwenderDto().getMandantCNrHauptmandant();
    if (hauptMandant.equals(LPMain.getInstance().getTheClient().getMandant())) {
      bIstHauptmandant = true;
    }
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    //TabbedPane Zutrittsmodell
    int tabIndex = 0;
    IDX_TABBED_PANE_ZUTRITT = tabIndex;
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.modulname"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.modulname"),
        IDX_TABBED_PANE_ZUTRITT);

    tabIndex++;
    IDX_TABBED_PANE_ZUTRITTSMODELL = tabIndex;
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsmodell"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsmodell"),
        IDX_TABBED_PANE_ZUTRITTSMODELL);

    if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.
        RECHT_PERS_ZUTRITT_DAUEROFFEN_CRUD)) {
      tabIndex++;
      IDX_TABBED_PANE_DAUEROFFEN = tabIndex;
      tabbedPaneRoot.insertTab(
          LPMain.getInstance().getTextRespectUISPr("pers.zutritt.daueroffen"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("pers.zutritt.daueroffen"),
          IDX_TABBED_PANE_DAUEROFFEN);

    }

    tabIndex++;
    IDX_TABBED_PANE_ONLINECHECK = tabIndex;
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.onlinecheck"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.onlinecheck"),
        IDX_TABBED_PANE_ONLINECHECK);


    tabIndex++;
    IDX_TABBED_PANE_ZUTRITTSPROTOKOLL = tabIndex;
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.protokoll"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.protokoll"),
        IDX_TABBED_PANE_ZUTRITTSPROTOKOLL);

    if (bIstHauptmandant == true) {
      tabIndex++;
      IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
      // nur anzeigen wenn Benutzer Recht dazu hat
    if(DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)){
      tabbedPaneRoot.insertTab(
          LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
          IDX_TABBED_PANE_GRUNDDATEN);
    }
    }
    registerChangeListeners();
    createTabbedPaneZutritt(null);
    tabbedPaneZutritt.lPEventObjectChanged(null);
    tabbedPaneRoot.setSelectedComponent(tabbedPaneZutritt);
    //iicon: hier das li/on icon gemacht
    ImageIcon iicon = new javax.swing.ImageIcon(
        getClass().getResource("/com/lp/client/res/id_card.png"));
    setFrameIcon(iicon);

  }


  private void createTabbedPaneZutritt(JTabbedPane tabbedPane)
      throws Throwable {
    if (tabbedPane == null) {
      //lazy loading
      tabbedPaneZutritt = new TabbedPaneZutritt(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ZUTRITT,
                                    tabbedPaneZutritt);
      if (tabbedPaneZutritt.getPanelQueryZutrittsklasse().getSelectedId() == null) {
        enableAllOberePanelsExceptMe(tabbedPaneZutritt, 0, false);
      }
      initComponents();
    }

  }


  private void createTabbedPaneZutrittsmodell(JTabbedPane tabbedPane)
      throws Throwable {
    if (tabbedPane == null) {
      //lazy loading
      tabbedPaneZutrittsmodell = new TabbedPaneZutrittsmodell(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ZUTRITTSMODELL,
                                    tabbedPaneZutrittsmodell);
      if (tabbedPaneZutrittsmodell.getPanelQueryZutrittsmodell().getSelectedId() == null) {
        enableAllOberePanelsExceptMe(tabbedPaneZutrittsmodell, 0, false);
      }
      initComponents();
    }

  }


  private void createTabbedPaneGrunddaten(JTabbedPane tabbedPane)
      throws Throwable {
    if (tabbedPane == null) {
      //lazy loading
      tabbedPaneGrunddaten = new TabbedPaneZutrittgrunddaten(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
                                    tabbedPaneGrunddaten);
      initComponents();
    }
  }


  private void createTabbedPaneOnlinecheck(JTabbedPane tabbedPane)
      throws Throwable {
    if (tabbedPane == null) {
      //lazy loading
      tabbedPaneOnlinecheck = new TabbedPaneOnlinecheck(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ONLINECHECK,
                                    tabbedPaneOnlinecheck);
      initComponents();
    }
  }


  private void createTabbedPaneDaueroffen(JTabbedPane tabbedPane)
      throws Throwable {
    if (tabbedPane == null) {
      //lazy loading
      tabbedPaneDaueroffen = new TabbedPaneDaueroffen(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_DAUEROFFEN,
                                    tabbedPaneDaueroffen);
      initComponents();
    }
  }


  private void createTabbedPaneZutrittsprotokoll(JTabbedPane tabbedPane)
      throws Throwable {
    if (tabbedPane == null) {
      //lazy loading
      tabbedPaneZutrittsprotokoll = new TabbedPaneZutrittsprotokoll(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ZUTRITTSPROTOKOLL,
                                    tabbedPaneZutrittsprotokoll);
      initComponents();
    }
  }


  public void lPStateChanged(EventObject e)
      throws Throwable {
    JTabbedPane tabbedPane = (JTabbedPane) ( (JTabbedPane) e.getSource()).
        getSelectedComponent();
    int selectedCur = ( (JTabbedPane) e.getSource()).getSelectedIndex();
    setRechtModulweit(sRechtModulweit);
    if (selectedCur == IDX_TABBED_PANE_ZUTRITT) {
      createTabbedPaneZutritt(tabbedPane);
      //Info an Tabbedpane, bist selektiert worden.
      tabbedPaneZutritt.lPEventObjectChanged(null);
    }
    else if (selectedCur == IDX_TABBED_PANE_ZUTRITTSMODELL) {
      createTabbedPaneZutrittsmodell(tabbedPane);
      //Info an Tabbedpane, bist selektiert worden.
      tabbedPaneZutrittsmodell.lPEventObjectChanged(null);
    }
    else if (selectedCur == IDX_TABBED_PANE_DAUEROFFEN) {
      createTabbedPaneDaueroffen(tabbedPane);
      //Info an Tabbedpane, bist selektiert worden.
      tabbedPaneDaueroffen.lPEventObjectChanged(null);
    }
    else if (selectedCur == IDX_TABBED_PANE_ONLINECHECK) {
      boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(
          RechteFac.
          RECHT_PERS_ZUTRITT_ONLINECHECK_CUD);
      if (hatRecht == true) {
        setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
      }
      createTabbedPaneOnlinecheck(tabbedPane);
      //Info an Tabbedpane, bist selektiert worden.
      tabbedPaneOnlinecheck.lPEventObjectChanged(null);
    }
    else if (selectedCur == IDX_TABBED_PANE_ZUTRITTSPROTOKOLL) {
      createTabbedPaneZutrittsprotokoll(tabbedPane);
      //Info an Tabbedpane, bist selektiert worden.
      tabbedPaneZutrittsprotokoll.lPEventObjectChanged(null);
    }
    else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
      createTabbedPaneGrunddaten(tabbedPane);
      //Info an Tabbedpane, bist selektiert worden.
      tabbedPaneGrunddaten.lPEventObjectChanged(null);
    }

  }

}
