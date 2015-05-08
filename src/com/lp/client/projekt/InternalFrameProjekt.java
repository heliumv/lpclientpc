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
package com.lp.client.projekt;


import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.projekt.service.KategorieDto;
import com.lp.server.projekt.service.ProjektStatusDto;
import com.lp.server.projekt.service.ProjekttypDto;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2009/06/24 12:08:07 $
 */
public class InternalFrameProjekt
    extends InternalFrame
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  public static int IDX_TABBED_PANE_PROJEKT = 0;
  private static int IDX_TABBED_PANE_PROJEKTGRUNDDATEN = 1;

  private TabbedPaneProjekt tpProjekt = null;
  private TabbedPaneProjektGrunddaten tpProjektGrunddaten = null;

  private KategorieDto kategorieDto = new KategorieDto();
  private ProjekttypDto projekttypDto = new ProjekttypDto();
  private ProjektStatusDto projektStatusDto = new ProjektStatusDto();

  public InternalFrameProjekt(String title, String belegartCNr,
                              String sRechtModulweitI)
      throws Throwable {

    super(title, belegartCNr, sRechtModulweitI);
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    // Komponente Projekt
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("proj.projekt"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("proj.projekt"),
        IDX_TABBED_PANE_PROJEKT);
    // Komponente Projektgrunddaten
    // nur anzeigen wenn Benutzer Recht dazu hat
    if(DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)){
      tabbedPaneRoot.insertTab(
          LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("lp.tooltip.grunddaten"),
          IDX_TABBED_PANE_PROJEKTGRUNDDATEN);
    }
    refreshProjektTP();
    tpProjekt.lPEventObjectChanged(null);
    tabbedPaneRoot.setSelectedComponent(tpProjekt);
    addItemChangedListener(this);
    registerChangeListeners();
    // dem frame das icon setzen
    ImageIcon iicon = new javax.swing.ImageIcon(
        getClass().getResource("/com/lp/client/res/briefcase2_document16x16.png"));
    setFrameIcon(iicon);
  }


  private void refreshProjektTP()
      throws Throwable {
    if (tpProjekt == null) {
      tpProjekt = new TabbedPaneProjekt(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_PROJEKT, tpProjekt);
      initComponents();
    }
  }


  private void refreshProjektGrunddatenTP()
      throws Throwable {
    if (tpProjektGrunddaten == null) {
      tpProjektGrunddaten = new TabbedPaneProjektGrunddaten(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_PROJEKTGRUNDDATEN,
                                    tpProjektGrunddaten);
      initComponents();
    }
  }


  public void lPStateChanged(EventObject e)
      throws Throwable {
    JTabbedPane tabbedPane = (JTabbedPane) ( (JTabbedPane) e.getSource()).
        getSelectedComponent();
    //TODO-AGILCHANGES
    /**
     * AGILPRO CHANGES BEGIN
     * @author Lukas Lisowski
     */
    int selectedCur = 0;
    try {
      selectedCur = ( (JTabbedPane) e.getSource()).getSelectedIndex();
    }
    catch (Exception ex) {
      selectedCur = ( (com.lp.client.pc.Desktop) e.getSource()).getSelectedIndex();
    }
    /**
     * AGILPRO CHANGES END
     */
    if (selectedCur == IDX_TABBED_PANE_PROJEKT) {
      refreshProjektTP();
      //Info an Tabbedpane, bist selektiert worden.
      tpProjekt.lPEventObjectChanged(null);
    }
    else
    if (selectedCur == IDX_TABBED_PANE_PROJEKTGRUNDDATEN) {
      refreshProjektGrunddatenTP();
      //Info an Tabbedpane, bist selektiert worden.
      tpProjektGrunddaten.lPEventObjectChanged(null);
    }

  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {

  }


  public TabbedPaneProjekt getTabbedPaneProjekt()
      throws Throwable {
    return tpProjekt;
  }


  public TabbedPaneProjektGrunddaten getTabbedPaneProjektgrunddaten()
      throws Throwable {
    return tpProjektGrunddaten;
  }


  public void setKategorieDto(KategorieDto kategorieDto) {
    this.kategorieDto = kategorieDto;
  }


  public KategorieDto getKategorieDto() {
    return kategorieDto;
  }


  public ProjekttypDto getProjekttypDto() {
    return projekttypDto;
  }


  public void setProjekttypDto(ProjekttypDto projekttypDto) {
    this.projekttypDto = projekttypDto;
  }



  public void setProjektStatusDto(ProjektStatusDto projektStatusDto) {
    this.projektStatusDto = projektStatusDto;
  }


  public ProjektStatusDto getProjektStatusDto() {
    return projektStatusDto;
  }

}
