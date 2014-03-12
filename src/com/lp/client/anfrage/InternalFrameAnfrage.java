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
package com.lp.client.anfrage;


import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageartDto;
import com.lp.server.anfrage.service.AnfragepositionartDto;
import com.lp.server.anfrage.service.AnfragetextDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich um die Anfrage.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>07.06.05</I></p>
 * @author $Author: valentin $
 * @version $Revision: 1.4 $
 */
public class InternalFrameAnfrage
    extends InternalFrame
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TabbedPaneAnfrage tpAnfrage = null;
  private TabbedPaneAnfragevorschlag tpAnfragevorschlag = null;
  private TabbedPaneAnfrageGrunddaten tpAnfragegrunddaten = null;

  public static final int IDX_TABBED_PANE_ANFRAGE = 0;
  private static final int IDX_TABBED_PANE_ANFRAGEVORSCHLAG = 1;
  private static final int IDX_TABBED_PANE_ANFRAGEGRUNDDATEN = 2;

  private AnfragetextDto anfragetextDto = new AnfragetextDto();
  private AnfragepositionartDto anfragepositionartDto = new AnfragepositionartDto();
  private AnfrageartDto anfrageartDto = new AnfrageartDto();

  public InternalFrameAnfrage(String title,
                              String belegartCNr, String sRechtModulweitI)
      throws Throwable {

    super(title, belegartCNr, sRechtModulweitI);
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("anf.anfrage"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("anf.modulname.tooltip"),
        IDX_TABBED_PANE_ANFRAGE);

    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("anf.anfragevorschlag"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("anf.anfragevorschlag"),
        IDX_TABBED_PANE_ANFRAGEVORSCHLAG);
    // nur anzeigen wenn Benutzer Recht dazu hat
    if(DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)){
      tabbedPaneRoot.insertTab(
          LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("lp.tooltip.grunddaten"),
          IDX_TABBED_PANE_ANFRAGEGRUNDDATEN);
    }
    // Default TabbedPane setzen
    refreshTabbedPaneAnfrage();
    tpAnfrage.lPEventObjectChanged(null);
    tabbedPaneRoot.setSelectedComponent(tpAnfrage);

    // dem frame das icon setzen
    ImageIcon iicon = new javax.swing.ImageIcon(
        getClass().getResource("/com/lp/client/res/note_find16x16.png"));
    setFrameIcon(iicon);

    // ich selbst moechte informiert werden.
    addItemChangedListener(this);
    // listener bin auch ich
    registerChangeListeners();
  }


  public void lPStateChanged(EventObject e)
      throws Throwable {
    int selectedCur = 0;
    try {
      selectedCur = ( (JTabbedPane) e.getSource()).getSelectedIndex();
    }
    catch (Exception ex) {
      selectedCur = ( (com.lp.client.pc.Desktop) e.getSource()).getSelectedIndex();
    }
    DelegateFactory.getInstance().getBestellvorschlagDelegate().
              removeLockDesBestellvorschlagesWennIchIhnSperre();

    if (selectedCur == IDX_TABBED_PANE_ANFRAGE) {
      refreshTabbedPaneAnfrage();

      //Info an Tabbedpane, bist selektiert worden.
      tpAnfrage.lPEventObjectChanged(null);
    }
    else if (selectedCur == IDX_TABBED_PANE_ANFRAGEVORSCHLAG) {
      try {
        try {
          DelegateFactory.getInstance().getBestellvorschlagDelegate().
              pruefeBearbeitenDesBestellvorschlagsErlaubt();
        }
        catch (ExceptionLP ex) {
          if (ex.getICode() ==
              EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT) {

            String cWer = "";
            if (ex.getAlInfoForTheClient() != null &&
                ex.getAlInfoForTheClient().size() > 0) {
              cWer = (String) ex.getAlInfoForTheClient().get(0);
            }

            DialogFactory.showModalDialog(LPMain.getInstance().
                                                 getTextRespectUISPr("lp.warning"),
                                                 LPMain.getInstance().getTextRespectUISPr(
                "anf.anfragevorschlagerstellengesperrt") + "\n" + cWer);
            return;
          }
          else {
            handleException(ex, false);
          }

        }
        refreshTabbedPaneAnfragevorschlag();

        //Info an Tabbedpane, bist selektiert worden.
        tpAnfragevorschlag.lPEventObjectChanged(null);
      }
      catch (ExceptionLP efc) {
        if (efc != null &&
            efc.getICode() == EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT) {
          tabbedPaneRoot.setSelectedComponent(tpAnfrage);
          DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
              "lp.warning"),
                                               LPMain.getInstance().getMsg(efc));
        }
        else {
          throw efc;
        }
      }
    }
    else if (selectedCur == IDX_TABBED_PANE_ANFRAGEGRUNDDATEN) {
      refreshTabbedPaneAnfragegrunddaten();

      //Info an Tabbedpane, bist selektiert worden.
      tpAnfragegrunddaten.lPEventObjectChanged(null);
    }
  }


  private void refreshTabbedPaneAnfrage()
      throws Throwable {
    if (tpAnfrage == null) {
      tpAnfrage = new TabbedPaneAnfrage(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ANFRAGE,
                                    tpAnfrage);
      initComponents();
    }
  }


  private void refreshTabbedPaneAnfragevorschlag()
      throws Throwable {
    if (tpAnfragevorschlag == null) {
      tpAnfragevorschlag = new TabbedPaneAnfragevorschlag(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ANFRAGEVORSCHLAG, tpAnfragevorschlag);
      initComponents();
    }
  }


  private void refreshTabbedPaneAnfragegrunddaten()
      throws Throwable {
    if (tpAnfragegrunddaten == null) {
      tpAnfragegrunddaten = new TabbedPaneAnfrageGrunddaten(this);
      tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ANFRAGEGRUNDDATEN,
                                    tpAnfragegrunddaten);
      initComponents();
    }
  }


  protected void lPEventItemChanged(ItemChangedEvent eI) {
    //nothing here.
  }


  public TabbedPaneAnfrage getTabbedPaneAnfrage()
      throws Throwable {
    return tpAnfrage;
  }


  public TabbedPaneAnfragevorschlag getTabbedPaneAnfragevorschlag()
      throws Throwable {
    return tpAnfragevorschlag;
  }


  public TabbedPaneAnfrageGrunddaten getTabbedPaneAnfragegrunddaten()
      throws Throwable {
    return tpAnfragegrunddaten;
  }


  public AnfragetextDto getAnfragetextDto() {
    return this.anfragetextDto;
  }


  public void setAnfragetextDto(AnfragetextDto anfragetextDto) {
    this.anfragetextDto = anfragetextDto;
  }


  public AnfragepositionartDto getAnfragepositionartDto() {
    return this.anfragepositionartDto;
  }


  public void setAnfragepositionartDto(AnfragepositionartDto anfragepositionartDto) {
    this.anfragepositionartDto = anfragepositionartDto;
  }


  public AnfrageartDto getAnfrageartDto() {
    return this.anfrageartDto;
  }


  public void setAnfrageartDto(AnfrageartDto anfrageartDto) {
    this.anfrageartDto = anfrageartDto;
  }
}
