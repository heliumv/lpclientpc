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
package com.lp.client.bestellung;


import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.Command;
import com.lp.client.frame.CommandGoto;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BSMahnstufeDto;
import com.lp.server.bestellung.service.BSMahntextDto;
import com.lp.server.bestellung.service.BestellpositionartDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungsartDto;
import com.lp.server.bestellung.service.BestellungstatusDto;
import com.lp.server.bestellung.service.BestellungtextDto;
import com.lp.util.EJBExceptionLP;


/**
 *
 * <p> Diese Klasse kuemmert sich um die Bestellungen</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Guenther Hodina; dd.mm.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/07/10 12:37:51 $
 */
public class InternalFrameBestellung
    extends InternalFrame
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//  public static String I_ID_RAHMENBESTELLUNG = "iIdRahmenbestellung";
  public final static String I_ID_ABRUFBESTELLUNG = "iIdAbrufbestellung";

  public final static int IDX_PANE_BESTELLUNG = 0;
  public final static int IDX_PANE_VORSCHLAG = 1;
  public final static int IDX_PANE_MAHNWESEN = 2;
  public final static int IDX_PANE_BESTELLUNGGRUNDDATEN = 3;

  private TabbedPaneBestellung tpBestellung = null;
  private TabbedPaneBestellungGrunddaten tpBestellungGrunddaten = null;
  private TabbedPaneBestellvorschlag tpBestellvorschlag = null;
  private TabbedPaneBESMahnwesen tpBESMahnwesen = null;

  private BestellungsartDto bestellungsartDto = new BestellungsartDto();
  private BestellungstatusDto bestellungstatusDto = null;
  private BestellungtextDto bestellungtextDto = new BestellungtextDto();
  private BestellpositionartDto bestellpositionartDto = new BestellpositionartDto();
  private BSMahnstufeDto bsmahnstufeDto = new BSMahnstufeDto();
  private BSMahntextDto bsmahntextDto = new BSMahntextDto();


  public InternalFrameBestellung(String title, String belegartCNr,
                                 String sRechtModulweitI)
      throws Throwable {
    super(title, belegartCNr, sRechtModulweitI);
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    //1 unteres tab: Partner; lazy loading; ist auch default.
    tabbedPaneRoot.insertTab(
        LPMain.getTextRespectUISPr("bes.bestellung.tooltip"),
        null,
        null,
        LPMain.getTextRespectUISPr("bes.bestellung.tooltip"),
        IDX_PANE_BESTELLUNG);

    //2 unteres tab: Partner; lazy loading; ist auch default.
    tabbedPaneRoot.insertTab(
        LPMain.getTextRespectUISPr("bes.title.panel.bestellvorschlag"),
        null,
        null,
        LPMain.getTextRespectUISPr("bes.title.panel.bestellvorschlag"),
        IDX_PANE_VORSCHLAG);

    //3 unteres tab: Partner; lazy loading; ist auch default.
    tabbedPaneRoot.insertTab(
        LPMain.getTextRespectUISPr("bes.title.panel.mahnwesen"),
        null,
        null,
        LPMain.getTextRespectUISPr("bes.title.panel.mahnwesen"),
        IDX_PANE_MAHNWESEN);

    //4 unteres tab: Partner; lazy loading; ist auch default.
    // nur anzeigen wenn Benutzer Recht dazu hat
    if(DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)){
      tabbedPaneRoot.insertTab(
          LPMain.getTextRespectUISPr("bes.title.panel.bestellunggrunddaten"),
          null,
          null,
          LPMain.getTextRespectUISPr("bes.title.panel.bestellunggrunddaten"),
          IDX_PANE_BESTELLUNGGRUNDDATEN);
    }
    // Defaulttabbedpane setzen.
    refreshAuswahlTP();
    tpBestellung.lPEventObjectChanged(null);

    //ich selbst moechte informiert werden.
    addItemChangedListener(this);
    //awt: listener bin auch ich.
    registerChangeListeners();

    // dem frame das icon setzen
    ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
        "/com/lp/client/res/shoppingcart_full16x16.png"));
    setFrameIcon(iicon);
  }


  public void lPStateChanged(EventObject e)
      throws Throwable {
    int selectedCur = 0;

    try {
      selectedCur = ( (JTabbedPane) e.getSource()).getSelectedIndex();
    }
    catch (Exception ex) {
      /** @todo JO na servas  PJ 4728 */
      selectedCur = ( (Desktop) e.getSource()).getSelectedIndex();
    }

    DelegateFactory.getInstance().getBestellvorschlagDelegate().
        removeLockDesBestellvorschlagesWennIchIhnSperre();

    if (selectedCur == IDX_PANE_BESTELLUNG) {
      refreshAuswahlTP();
      tabbedPaneRoot.setSelectedComponent(tpBestellung);
      //Info an Tabbedpane, bist selektiert worden.
      tpBestellung.changed(new ItemChangedEvent(this,
                                                ItemChangedEvent.ACTION_YOU_ARE_SELECTED));
      tpBestellung.lPEventObjectChanged(null);
    }

    else if (selectedCur == IDX_PANE_BESTELLUNGGRUNDDATEN) {
      refreshTPBestellungGrunddaten();
      tabbedPaneRoot.setSelectedComponent(tpBestellungGrunddaten);
      //Info an Tabbedpane, bist selektiert worden.
      tpBestellungGrunddaten.changed(new ItemChangedEvent(this,
          ItemChangedEvent.ACTION_YOU_ARE_SELECTED));
      tpBestellungGrunddaten.lPEventObjectChanged(null);
    }
    else if (selectedCur == IDX_PANE_VORSCHLAG) {
      try {
        DelegateFactory.getInstance().getBestellvorschlagDelegate().
            pruefeBearbeitenDesBestellvorschlagsErlaubt();
        refreshTPBestellvorschlag();
        tabbedPaneRoot.setSelectedComponent(tpBestellvorschlag);
        //Info an Tabbedpane, bist selektiert worden.
        tpBestellvorschlag.changed(new ItemChangedEvent(this,
            ItemChangedEvent.ACTION_YOU_ARE_SELECTED));
        tpBestellvorschlag.lPEventObjectChanged(null);
      }
      catch (ExceptionLP efc) {
        if (efc != null &&
            efc.getICode() == EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT) {
          tabbedPaneRoot.setSelectedComponent(tpBestellung);
          //Benutzer der gerade sperrt finden
          String[] sHelper = efc.getSMsg().split(" ");
          String sLocker = DelegateFactory.getInstance().getPersonalDelegate().
              personalFindByPrimaryKey(Integer.parseInt(sHelper[sHelper.length - 1])).getCKurzzeichen();
          DialogFactory.showModalDialog(
              LPMain.getTextRespectUISPr("lp.warning"),
              LPMain.getInstance().getMsg(efc) + sLocker);
        }
        else {
          throw efc;
        }
      }
    }
    else if (selectedCur == IDX_PANE_MAHNWESEN) {
      refreshTPBESMahnwesen();
      tabbedPaneRoot.setSelectedComponent(tpBESMahnwesen);
      //Info an Tabbedpane, bist selektiert worden.
      tpBESMahnwesen.changed(new ItemChangedEvent(this,
                                                  ItemChangedEvent.
                                                  ACTION_YOU_ARE_SELECTED));
      tpBESMahnwesen.lPEventObjectChanged(null);
    }

  }


  public void lPEventItemChanged(ItemChangedEvent eI) {
    //nothing here
  }


  public TabbedPaneBestellung getTabbedPaneBestellung() {
    return tpBestellung;
  }


  public TabbedPaneBestellvorschlag getTabbedPaneBestellvorschlag() {
    return tpBestellvorschlag;
  }


  private void refreshTPBestellungGrunddaten()
      throws Throwable {
    if (tpBestellungGrunddaten == null) {
      tpBestellungGrunddaten = new TabbedPaneBestellungGrunddaten(this);
      tabbedPaneRoot.setComponentAt(IDX_PANE_BESTELLUNGGRUNDDATEN, tpBestellungGrunddaten);
      initComponents();
    }
  }


  private void refreshTPBestellvorschlag()
      throws Throwable {
    if (tpBestellvorschlag == null) {
      tpBestellvorschlag = new TabbedPaneBestellvorschlag(this);
      tabbedPaneRoot.setComponentAt(IDX_PANE_VORSCHLAG, tpBestellvorschlag);
      initComponents();
    }
  }


  private void refreshTPBESMahnwesen()
      throws Throwable {
    if (tpBESMahnwesen == null) {
      tpBESMahnwesen = new TabbedPaneBESMahnwesen(this);
      tabbedPaneRoot.setComponentAt(IDX_PANE_MAHNWESEN, tpBESMahnwesen);
      initComponents();
    }
  }


  private void refreshAuswahlTP()
      throws Throwable {
    if (tpBestellung == null) {
      tpBestellung = new TabbedPaneBestellung(this);
      tabbedPaneRoot.setComponentAt(IDX_PANE_BESTELLUNG, tpBestellung);
      initComponents();
    }
  }


  public void setBestellungtextDto(BestellungtextDto bestellungtextDtoI) {
    this.bestellungtextDto = bestellungtextDtoI;
  }


  public BestellungtextDto getBestellungtextDto() {
    return bestellungtextDto;
  }


  public void setBestellungsartDto(BestellungsartDto bestellungartDto) {
    this.bestellungsartDto = bestellungartDto;
  }


  public BestellungsartDto getBestellungsartDto() {
    return bestellungsartDto;
  }


  public void setBestellungstatusDto(BestellungstatusDto bestellungstatusDto) {
    this.bestellungstatusDto = bestellungstatusDto;
  }


  public BestellungstatusDto getBestellungstatusDto() {
    return bestellungstatusDto;
  }


  public BestellpositionartDto getBestellpositionartDto() {
    return this.bestellpositionartDto;
  }


  public void setBestellpositionartDto(BestellpositionartDto bestellpositionartDtoI) {
    this.bestellpositionartDto = bestellpositionartDtoI;
  }


  public BSMahnstufeDto getBSMahnstufeDto() {
    return bsmahnstufeDto;
  }


  public void setBSMahnstufeDto(BSMahnstufeDto bsmahnstufeDtoI) {
    this.bsmahnstufeDto = bsmahnstufeDtoI;
  }


  public BSMahntextDto getBSMahntextDto() {
    return bsmahntextDto;
  }


  public void setBSMahntextDto(BSMahntextDto bsmahntextDtoI) {
    this.bsmahntextDto = bsmahntextDtoI;
  }


  public int execute(Command commandI)
      throws Throwable {

    int iRetCmd = ICommand.COMMAND_DONE;

    if (commandI instanceof CommandGoto) {
      CommandGoto gotoCommand = (CommandGoto) commandI;
      if (gotoCommand.getsInternalFrame().equals(Command.S_INTERNALFRAME_BESTELLUNG)) {
        if (gotoCommand.getTabbedPaneAsClass().equals(Command.CLASS_TABBED_PANE_BESTELLUNG)) {
          if (gotoCommand.getsPanel().equals(Command.PANELBESTELLUNG_POSITIONSICHT_RAHMEN)) {
            Integer iIdBes =
                (Integer) gotoCommand.getHmOfExtraData().get(I_ID_ABRUFBESTELLUNG);
            //leeres dto
            BestellungDto besDtoNew = new BestellungDto();
            //iIdBes hinterlegen
            besDtoNew.setIId(iIdBes);
            tpBestellung.setBestellungDto(besDtoNew);

            tpBestellung.getPanelBestellungAuswahlQP1().setSelectedId(iIdBes);
            tpBestellung.initializeDtos(iIdBes);

            tpBestellung.setSelectedIndex(
                TabbedPaneBestellung.IDX_PANEL_BESTELLPOSITIONSICHTRAHMEN);
          }

          else if (gotoCommand.getsPanel().equals(Command.PANEL_BESTELLUNG_POSITIONEN)) {
            Integer iIdBes =
                (Integer) gotoCommand.getHmOfExtraData().get(I_ID_ABRUFBESTELLUNG);
            //leeres dto
            BestellungDto besDtoNew = new BestellungDto();
            //iIdBes hinterlegen
            besDtoNew.setIId(iIdBes);
            tpBestellung.setBestellungDto(besDtoNew);

            tpBestellung.getPanelBestellungAuswahlQP1().setSelectedId(iIdBes);

            tpBestellung.refreshBestellungPositionen(iIdBes);
            tpBestellung.setSelectedIndex(TabbedPaneBestellung.IDX_PANEL_BESTELLPOSITION);
          }
        }
      }
     }
    else {
      iRetCmd = ICommand.COMMAND_NOT_DONE;
    }

    return iRetCmd;
  }

}
