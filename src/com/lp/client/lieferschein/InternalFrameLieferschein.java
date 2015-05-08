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
package com.lp.client.lieferschein;


import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinartDto;
import com.lp.server.lieferschein.service.LieferscheinpositionartDto;
import com.lp.server.lieferschein.service.LieferscheintextDto;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access") 
/**
 * <p>Rahmenfenster fuer das Modul Lieferschein.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-10-21</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */

public class InternalFrameLieferschein
    extends InternalFrame
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TabbedPaneLieferschein tpLieferschein = null;
  private TabbedPaneLieferscheinGrunddaten tpLieferscheingrunddaten = null;

  public static final int IDX_TABBED_PANE_LIEFERSCHEIN = 0;
  private final int IDX_TABBED_PANE_LIEFERSCHEINGRUNDDATEN = 1;

  private LieferscheintextDto lieferscheintextDto = new LieferscheintextDto();
  private LieferscheinartDto lieferscheinartDto = new LieferscheinartDto();
  private LieferscheinpositionartDto lieferscheinpositionartDto = new LieferscheinpositionartDto();

  // Wenn eine freie Lieferscheinposition bei einem auftragbezogenen Lieferschein
  // erfasst wird, den Benutzer einmalig warnen
  public static boolean bWarnungAusgesprochen = false;

  /**
   * Konstruktor.
   *
   * @param title der Titel des Frame
   * @param belegartCNr der Name des Moduls
   * @param sRechtModulweitI String
   * @throws Throwable Ausnahme
   */
  public InternalFrameLieferschein(String title,
                                   String belegartCNr, String sRechtModulweitI)
      throws Throwable {
    super(title, belegartCNr, sRechtModulweitI);

    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    // Komponente Lieferschein
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("ls.modulname"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("ls.modulname.tooltip"),
        IDX_TABBED_PANE_LIEFERSCHEIN);

    // Komponente Lieferscheingrunddaten
    // nur anzeigen wenn Benutzer Recht dazu hat
    if(DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)){
      tabbedPaneRoot.insertTab(
          LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("lp.tooltip.grunddaten"),
          IDX_TABBED_PANE_LIEFERSCHEINGRUNDDATEN);
    }
    refreshTabbedPaneLieferschein();
    tpLieferschein.lPEventObjectChanged(null);
    tabbedPaneRoot.setSelectedComponent(tpLieferschein);

    // dem frame das icon setzen
    ImageIcon iicon = new javax.swing.ImageIcon(
        getClass().getResource("/com/lp/client/res/truck_red16x16.png"));
    setFrameIcon(iicon);

    // ich selbst moechte informiert werden.
    addItemChangedListener(this);
    // listener bin auch ich
    registerChangeListeners();
  }

  public void lPStateChanged(EventObject e) throws Throwable {
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
    if (selectedCur == IDX_TABBED_PANE_LIEFERSCHEIN) {
      refreshTabbedPaneLieferschein();

      //Info an Tabbedpane, bist selektiert worden.
      tpLieferschein.lPEventObjectChanged(null);
    }
    else if (selectedCur == IDX_TABBED_PANE_LIEFERSCHEINGRUNDDATEN) {
      refreshTabbedPaneLieferscheingrunddaten();

      //Info an Tabbedpane, bist selektiert worden.
      tpLieferscheingrunddaten.lPEventObjectChanged(null);
    }
  }


  private void refreshTabbedPaneLieferschein() throws Throwable {
    if (tpLieferschein == null) {
      // lazy loading
      tpLieferschein = new TabbedPaneLieferschein(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_LIEFERSCHEIN,
                                    tpLieferschein);
      initComponents();
    }
  }

  private void refreshTabbedPaneLieferscheingrunddaten()
      throws Throwable {
    if (tpLieferscheingrunddaten == null) {
      tpLieferscheingrunddaten = new TabbedPaneLieferscheinGrunddaten(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_LIEFERSCHEINGRUNDDATEN,
                                    tpLieferscheingrunddaten);
      initComponents();
    }
  }


  protected void lPEventItemChanged(ItemChangedEvent eI) {
    //nothing here.
  }

  public TabbedPaneLieferschein getTabbedPaneLieferschein() {
    return (TabbedPaneLieferschein) tpLieferschein;
  }

  public TabbedPaneLieferscheinGrunddaten getTabbedPaneLieferscheingrunddaten() {
    return (TabbedPaneLieferscheinGrunddaten) tpLieferscheingrunddaten;
  }

  protected boolean handleOwnException(ExceptionLP exfc) {
    boolean bErrorErkannt = true;
    int code = exfc.getICode();

    switch (code) {
      case EJBExceptionLP.FEHLER_LIEFERSCHEIN_KEINEOFFENENLIEFERSCHEINE:
        DialogFactory.showModalDialog(
            LPMain.getInstance().getTextRespectUISPr("lp.warning"),
            LPMain.getInstance().getTextRespectUISPr(
                "ls.warning.keineoffenenlieferscheine"));
        break;

      case EJBExceptionLP.FEHLER_LIEFERSCHEIN_KEINEANGELEGTENLIEFERSCHEINE:
        DialogFactory.showModalDialog(
            LPMain.getInstance().getTextRespectUISPr("lp.warning"),
            LPMain.getInstance().getTextRespectUISPr(
                "ls.warning.keineangelegtenlieferscheine"));
        break;

      case EJBExceptionLP.FEHLER_LIEFERSCHEIN_TEXTINKONZERNDATENSPRACHENICHTHINTERLEGT:
        DialogFactory.showModalDialog(
            LPMain.getInstance().getTextRespectUISPr("lp.warning"),
            LPMain.getInstance().getTextRespectUISPr(
                "ls.warning.textkonzerndatensprache"));
        break;

      default:
        bErrorErkannt = false;
    }

    return bErrorErkannt;
  }

  public LieferscheintextDto getLieferscheintextDto() {
    return lieferscheintextDto;
  }

  public void setLieferscheintextDto(LieferscheintextDto lieferscheintextDtoI) {
    this.lieferscheintextDto = lieferscheintextDtoI;
  }

  public LieferscheinartDto getLieferscheinartDto() {
    return this.lieferscheinartDto;
  }

  public void setLieferscheinartDto(LieferscheinartDto lieferscheinartDtoI) {
    this.lieferscheinartDto = lieferscheinartDtoI;
  }

  public LieferscheinpositionartDto getLieferscheinpositionartDto() {
    return this.lieferscheinpositionartDto;
  }

  public void setLieferscheinpositionartDto(LieferscheinpositionartDto lieferscheinpositionartDtoI) {
    this.lieferscheinpositionartDto = lieferscheinpositionartDtoI;
  }
}
