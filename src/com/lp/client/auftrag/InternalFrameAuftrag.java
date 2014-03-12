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
package com.lp.client.auftrag;


import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragartDto;
import com.lp.server.auftrag.service.AuftragpositionArtDto;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.auftrag.service.AuftragtextDto;
import com.lp.server.benutzer.service.RechteFac;

@SuppressWarnings("static-access") 
/**
 * <p>Rahmenfenster fuer das Modul Auftrag.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-07-26</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class InternalFrameAuftrag
    extends InternalFrame
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TabbedPaneAuftrag tpAuftrag = null;
  private TabbedPaneAuftragGrunddaten tpAuftragGrunddaten = null;

  public static final int IDX_TABBED_PANE_AUFTRAG = 0;
  private final int IDX_TABBED_PANE_AUFTRAGGRUNDDATEN = 1;

  private AuftragtextDto auftragtextDto = new AuftragtextDto();
  private AuftragartDto auftragartDto = new AuftragartDto();
  private AuftragpositionArtDto auftragpositionartDto = new AuftragpositionArtDto();
  private AuftragseriennrnDto auftragseriennrnDto = new AuftragseriennrnDto();

  // Wenn eine freie Auftragposition bei einer Abrufbestellung zu einem Rahmen
  // erfasst wird, den Benutzer einmalig warnen
  public static boolean bWarnungAusgesprochen = false;

  public InternalFrameAuftrag(String title,
                              String belegartCNr, String sRechtModulweitI)
      throws Throwable {

    super(title, belegartCNr, sRechtModulweitI);
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    // Komponente Auftrag
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("auft.auftrag"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("auft.modulname.tooltip"),
        IDX_TABBED_PANE_AUFTRAG);

    // Komponente Auftraggrunddaten
    // nur anzeigen wenn Benutzer Recht dazu hat
    if(DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)){
      tabbedPaneRoot.insertTab(
          LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("lp.tooltip.grunddaten"),
          IDX_TABBED_PANE_AUFTRAGGRUNDDATEN);
    }
    // Default TabbedPane setzen
    refreshTabbedPaneAuftrag();
    tpAuftrag.lPEventObjectChanged(null);
    tabbedPaneRoot.setSelectedComponent(tpAuftrag);

    // dem frame das icon setzen
    ImageIcon iicon = new javax.swing.ImageIcon(
        getClass().getResource("/com/lp/client/res/auftrag16x16.png"));
    setFrameIcon(iicon);

    // ich selbst moechte informiert werden.
    addItemChangedListener(this);
    // listener bin auch ich
    registerChangeListeners();
  }

  //TODO-AGILCHANGES
  /**
   * AGILPRO CHANGES Changed visiblity from protected to public
   *
   * @author Lukas Lisowski
   * @param e EventObject
   * @throws Throwable
   */
  public void lPStateChanged(EventObject e) throws Throwable {
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
    if (selectedCur == IDX_TABBED_PANE_AUFTRAG) {
      refreshTabbedPaneAuftrag();

      //Info an Tabbedpane, bist selektiert worden.
      tpAuftrag.lPEventObjectChanged(null);
    }
    else
    if (selectedCur == IDX_TABBED_PANE_AUFTRAGGRUNDDATEN) {
      refreshTabbedPaneAuftraggrunddaten();

      //Info an Tabbedpane, bist selektiert worden.
      tpAuftragGrunddaten.lPEventObjectChanged(null);
    }
  }


  private void refreshTabbedPaneAuftrag() throws Throwable {
    if (tpAuftrag == null) {
      tpAuftrag = new TabbedPaneAuftrag(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_AUFTRAG,tpAuftrag);
      initComponents();
    }
  }

  private void refreshTabbedPaneAuftraggrunddaten()
      throws Throwable {
    if (tpAuftragGrunddaten == null) {
      tpAuftragGrunddaten = new TabbedPaneAuftragGrunddaten(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_AUFTRAGGRUNDDATEN,
                                    tpAuftragGrunddaten);
      initComponents();
    }
  }


  public TabbedPaneAuftrag getTabbedPaneAuftrag()
      throws Throwable {
    return tpAuftrag;
  }

  public TabbedPaneAuftragGrunddaten getTabbedPaneAuftraggrunddaten()
      throws Throwable {
    return tpAuftragGrunddaten;
  }

  public AuftragtextDto getAuftragtextDto() {
    return this.auftragtextDto;
  }

  public void setAuftragtextDto(AuftragtextDto auftragtextDto) {
    this.auftragtextDto = auftragtextDto;
  }

  public AuftragartDto getAuftragartDto() {
    return this.auftragartDto;
  }

  public void setAuftragartDto(AuftragartDto auftragartDtoI) {
    this.auftragartDto = auftragartDtoI;
  }

  public AuftragpositionArtDto getAuftragpositionArtDto() {
    return this.auftragpositionartDto;
  }

  public void setAuftragpositionArtDto(AuftragpositionArtDto auftragpositionArtDtoI) {
    this.auftragpositionartDto = auftragpositionArtDtoI;
  }


  public AuftragseriennrnDto getAuftragseriennrnDto() {
    return this.auftragseriennrnDto;
  }

  public void setAuftragseriennrnDto(AuftragseriennrnDto auftragseriennrnDto) {
    this.auftragseriennrnDto = auftragseriennrnDto;
  }


  public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
    if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (e.getSource() == getTabbedPaneAuftrag().getSichtAuftragAufAndereBelegartenTop()) {
        getTabbedPaneAuftrag().setSelectedComponent(getTabbedPaneAuftrag().getAuftragSichtLieferstatus());
      }
    }
  }
}
