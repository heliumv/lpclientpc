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
package com.lp.client.angebot;


import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotartDto;
import com.lp.server.angebot.service.AngeboteinheitDto;
import com.lp.server.angebot.service.AngeboterledigungsgrundDto;
import com.lp.server.angebot.service.AngebotpositionartDto;
import com.lp.server.angebot.service.AngebottextDto;
import com.lp.server.benutzer.service.RechteFac;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich um das Angebot.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>04.07.05</I></p>
 * @author $Author: christian $
 * @version $Revision: 1.4 $
 */
public class InternalFrameAngebot
    extends InternalFrame
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TabbedPaneAngebot tpAngebot = null;
  private TabbedPaneAngebotGrunddaten tpAngebotgrunddaten = null;

  public static final int IDX_TABBED_PANE_ANGEBOT = 0;
  private final int IDX_TABBED_PANE_ANGEBOTGRUNDDATEN = 1;

  private AngebotartDto angebotartDto = new AngebotartDto();
  private AngeboteinheitDto angeboteinheitDto = new AngeboteinheitDto();
  private AngeboterledigungsgrundDto angeboterledigungsgrundDto = new AngeboterledigungsgrundDto();
  private AngebottextDto angebottextDto = new AngebottextDto();
  private AngebotpositionartDto angebotpositionartDto = new AngebotpositionartDto();

  /**
   * Konstruktor
   * @param title Titel des Moduls
   * @param desktop Unser LP 5 Desktop
   * @param lPModulname Name des Moduls
   * @throws java.lang.Throwable Ausnahme
   */
  public InternalFrameAngebot(String title, String belegartCNr, String sRechtModulweitI)
      throws Throwable {

    super(title, belegartCNr, sRechtModulweitI);
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    // Komponente Angebot
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("angb.angebot"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("angb.modulname.tooltip"),
        IDX_TABBED_PANE_ANGEBOT);

    // Komponente Angebotgrunddaten
    // nur anzeigen wenn Benutzer Recht dazu hat
    if(DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)){
      tabbedPaneRoot.insertTab(
          LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("lp.tooltip.grunddaten"),
          IDX_TABBED_PANE_ANGEBOTGRUNDDATEN);
    }
    // Default TabbedPane setzen
    refreshTabbedPaneAngebot();
    tpAngebot.lPEventObjectChanged(null);
    tabbedPaneRoot.setSelectedComponent(tpAngebot);

    // dem frame das icon setzen
    ImageIcon iicon = new javax.swing.ImageIcon(
        getClass().getResource("/com/lp/client/res/presentation_chart16x16.png"));
    setFrameIcon(iicon);

    // ich selbst moechte informiert werden.
    addItemChangedListener(this);
    // listener bin auch ich
    registerChangeListeners();
  }

  //TODO-AGILCHANGES
	/**
	 * AGILPRO CHANGES
	 *
	 * Changed visiblity from protected to public
	 *
	 * @author Lukas Lisowski
	 */
  public void lPStateChanged(EventObject e)
      throws Throwable {

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
    if (selectedCur == IDX_TABBED_PANE_ANGEBOT) {
      refreshTabbedPaneAngebot();

      //Info an Tabbedpane, bist selektiert worden.
      tpAngebot.lPEventObjectChanged(null);
    }
    else

    if (selectedCur == IDX_TABBED_PANE_ANGEBOTGRUNDDATEN) {
      refreshTabbedPaneAngebotgrunddaten();

      //Info an Tabbedpane, bist selektiert worden.
      tpAngebotgrunddaten.lPEventObjectChanged(null);
    }
  }

  private void refreshTabbedPaneAngebot()
      throws Throwable {
    if (tpAngebot == null) {
      tpAngebot = new TabbedPaneAngebot(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ANGEBOT,
                                    tpAngebot);
      initComponents();
    }
  }

  private void refreshTabbedPaneAngebotgrunddaten()
      throws Throwable {
    if (tpAngebotgrunddaten == null) {
      tpAngebotgrunddaten = new TabbedPaneAngebotGrunddaten(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ANGEBOTGRUNDDATEN,
                                    tpAngebotgrunddaten);
      initComponents();
    }
  }

  protected void lPEventItemChanged(ItemChangedEvent eI) {
    //nothing here.
  }

  public TabbedPaneAngebot getTabbedPaneAngebot()
      throws Throwable {
    return tpAngebot;
  }

  public TabbedPaneAngebotGrunddaten getTabbedPaneAngebotgrunddaten()
      throws Throwable {
    return tpAngebotgrunddaten;
  }

  public AngebotartDto getAngebotartDto() {
    return angebotartDto;
  }

  public void setAngebotartDto(AngebotartDto angebotartDtoI) {
    angebotartDto = angebotartDtoI;
  }

  public AngeboteinheitDto getAngeboteinheitDto() {
    return angeboteinheitDto;
  }

  public void setAngeboteinheitDto(AngeboteinheitDto angeboteinheitDtoI) {
    angeboteinheitDto = angeboteinheitDtoI;
  }

  public AngeboterledigungsgrundDto getAngeboterledigungsgrundDto() {
    return angeboterledigungsgrundDto;
  }

  public void setAngeboterledigungsgrundDto(AngeboterledigungsgrundDto angeboterledigungsgrundDtoI) {
    angeboterledigungsgrundDto = angeboterledigungsgrundDtoI;
  }

  public AngebottextDto getAngebottextDto() {
    return angebottextDto;
  }

  public void setAngebottextDto(AngebottextDto angebottextDtoI) {
    angebottextDto = angebottextDtoI;
  }

  public AngebotpositionartDto getAngebotpositionartDto() {
    return angebotpositionartDto;
  }

  public void setAngebotpositionartDto(AngebotpositionartDto angebotpositionartDto) {
    this.angebotpositionartDto = angebotpositionartDto;
  }
}
