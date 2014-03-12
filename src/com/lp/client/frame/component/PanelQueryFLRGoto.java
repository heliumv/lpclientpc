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
package com.lp.client.frame.component;


import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.HashMap;

import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.bestellung.TabbedPaneBestellung;
import com.lp.client.eingangsrechnung.InternalFrameEingangsrechnung;
import com.lp.client.eingangsrechnung.TabbedPaneEingangsrechnung;
import com.lp.client.frame.Command;
import com.lp.client.frame.Command2IFNebeneinander;
import com.lp.client.frame.CommandCreateIF;
import com.lp.client.frame.CommandGoto;
import com.lp.client.frame.CommandSetFocus;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.TabbedPaneLieferant;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;


public class PanelQueryFLRGoto
    extends PanelQueryFLR
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final String ACTION_SPECIAL_GOTO = "action_special_goto";
  private InternalFrame intFrameTo = null;
  private Integer iId = null;
  private String sBelegart = null;
  private InternalFrame intFrameFrom = null;

  public PanelQueryFLRGoto(QueryType[] typesI,
                           FilterKriterium[] filtersI,
                           int idUsecaseI,
                           String[] aWhichButtonIUseI,
                           InternalFrame internalFrameI,
                           String add2TitleI)
      throws Throwable {
    this(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, null, add2TitleI, null);
  }


  /**
   * Konstruktor fuer eine FLR Liste, in der ein bestimmter Datensatz selektiert ist.
   * <br>Der Key dieses Datensatzes wird als Parameter uebergeben.
   * @param typesI die UI Filterkriterien fuer den Benutzer
   * @param filtersI die default Filterkriterien, die fuer den Benutzer nicht sichtbar sind
   * @param idUsecaseI die ID des gewuenschten UseCase
   * @param aWhichButtonIUseI welche Buttons sind auf dem Panel sichtbar
   * @param internalFrameI den InternalFrame als Kontext setzen
   * @param add2TitleI der Titel dieses Panels
   * @param oSelectedIdI der Datensatz mit diesem Key soll selektiert werden
   * @throws Throwable
   */
  public PanelQueryFLRGoto(QueryType[] typesI,
                           FilterKriterium[] filtersI,
                           int idUsecaseI,
                           String[] aWhichButtonIUseI,
                           InternalFrame internalFrameI,
                           String add2TitleI,
                           Object oSelectedIdI)
      throws Throwable {
    this(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, null, add2TitleI,
         oSelectedIdI);
  }


  public PanelQueryFLRGoto(QueryType[] typesI,
                           FilterKriterium[] filtersI,
                           int idUsecaseI,
                           String[] aWhichButtonIUseI,
                           InternalFrame internalFrameI,
                           String sBelegartI,
                           String add2TitleI,
                           Object oSelectedIdI)
      throws Throwable {
    super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI,
          internalFrameI, add2TitleI,
          oSelectedIdI);
    intFrameFrom = internalFrameI;
    iId = (Integer) oSelectedIdI;
    sBelegart = sBelegartI;
    jbInitPanel();
  }


  private void jbInitPanel()
      throws Throwable {

    String[] aButton = {
        PanelBasis.ACTION_NEW,
        PanelBasis.ACTION_UPDATE,
    };
    enableToolsPanelButtons(aButton);

  }


  @SuppressWarnings("deprecation")
public void eventActionNew(EventObject eventObject,
                             boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {

    CommandCreateIF createIF = new CommandCreateIF(sBelegart);
    LPMain.getInstance().execute(createIF);
    intFrameTo = LPMain.getInstance().getDesktop().getLPModul(sBelegart);

    Command2IFNebeneinander command2IFNebeneinander = new Command2IFNebeneinander(
        sBelegart);
    command2IFNebeneinander.setSInternalFrame2I(intFrameFrom.getBelegartCNr());
    LPMain.getInstance().execute(command2IFNebeneinander);
    CommandSetFocus setFocusCmd = new CommandSetFocus(intFrameFrom.getBelegartCNr());
    //Abfrage ob aus Bestellung zu Eingangsrechnung gewechselt wird
    if((intFrameTo.getBelegartCNr() =="Eingangsrechng ") && (intFrameFrom.getBelegartCNr() == "Bestellung     ")){
      InternalFrameBestellung intFrameBest = (InternalFrameBestellung)this.
          getInternalFrame();
      TabbedPaneBestellung tbBestellung = intFrameBest.getTabbedPaneBestellung();
      BestellungDto bestDto = tbBestellung.getBesDto();
      InternalFrameEingangsrechnung intFrameEr = (InternalFrameEingangsrechnung)
          intFrameTo;
      TabbedPaneEingangsrechnung tbEr = intFrameEr.getTabbedPaneEingangsrechnung();
      boolean bDoIt = true;
      if(tbBestellung.getWareneingangDto().getEingangsrechnungIId()!=null){
    	  bDoIt=
    	  DialogFactory.showModalJaNeinDialog(this.getInternalFrame(), LPMain.getTextRespectUISPr("bes.wareneingang.hatbereitser"));
      }
      if(bDoIt){
    	  tbEr.erstelleEingangsrechnungausBestellung(bestDto.getIId(),tbBestellung.getWareneingangDto());
      }
    }
    this.setInternalFrame(intFrameTo);
    LPMain.getInstance().execute(setFocusCmd);
    dialog.dispose();

  }


  public int execute(Command command) {
    return ICommand.COMMAND_NOT_DONE;
  }


  @SuppressWarnings("deprecation")
protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
      throws Throwable {
    /**
     * @todo MB: das darf nur dann ausgeloest werden, wenn in dem Modul nicht gerade ein anderer Datensatz gelockt ist!!!
     */
    dialog.dispose();
    CommandCreateIF createIF = new CommandCreateIF(sBelegart);
    LPMain.getInstance().execute(createIF);
    intFrameTo = LPMain.getInstance().getDesktop().getLPModul(sBelegart);
    this.setInternalFrame(intFrameTo);
    Command2IFNebeneinander command2IFNebeneinander = new Command2IFNebeneinander(
        sBelegart);
    command2IFNebeneinander.setSInternalFrame2I(intFrameFrom.getBelegartCNr());
    LPMain.getInstance().execute(command2IFNebeneinander);
    CommandSetFocus setFocusCmd = new CommandSetFocus(intFrameFrom.getBelegartCNr());
    LPMain.getInstance().execute(setFocusCmd);
    this.setKeyWhenDetailPanel(iId);
    super.eventActionUpdate(aE, bNeedNoUpdateI);
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_FLRGOTO;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {

    if (e.getActionCommand().equals(ACTION_SPECIAL_GOTO)) {
      dialog.dispose();
      CommandCreateIF createIFLieferant = new CommandCreateIF(
          Command.S_INTERNALFRAME_LIEFERANT);
      LPMain.getInstance().execute(createIFLieferant);

      Command2IFNebeneinander command2IFNebeneinander = new Command2IFNebeneinander(
          Command.S_INTERNALFRAME_LIEFERANT);
      // command2IFNebeneinander.setSInternalFrame2I(Command.S_INTERNALFRAME_BESTELLUNG);
      command2IFNebeneinander.setSInternalFrame2I(intFrameFrom.getBelegartCNr());
      LPMain.getInstance().execute(command2IFNebeneinander);

      CommandSetFocus setFocusCmd = new CommandSetFocus(
          Command.S_INTERNALFRAME_LIEFERANT);
      LPMain.getInstance().execute(setFocusCmd);

      CommandGoto gotoPanelLieferantKopfdaten = new CommandGoto(
          Command.S_INTERNALFRAME_LIEFERANT,
          TabbedPaneLieferant.class,
          Command.PANEL_LIEFERANT_KOPFDATEN,
          ACTION_UPDATE);

      HashMap<String, Object> hm = new HashMap<String, Object> ();
      if (iId == null) {
        hm.put(InternalFrameLieferant.I_ID_LIEFERANT, this.getSelectedId());
      }
      else {
        hm.put(InternalFrameLieferant.I_ID_LIEFERANT, iId);
      }

      gotoPanelLieferantKopfdaten.setHmOfExtraData(hm);
      LPMain.getInstance().execute(gotoPanelLieferantKopfdaten);

    }
  }


  /**
   *
   * @param e MouseEvent
   * @throws ExceptionLP
   */
  protected void eventMouseClicked(MouseEvent e)
      throws ExceptionLP {
    /**
     * @todo
     */
    if (e.getSource().getClass() == WrapperTable.class) {
      if (e.getClickCount() == 1) {

      }
      if (e.getClickCount() == 2) {
        // Doppelklick in die Tabelle -> Dialog schliessen
        dialog.dispose();
      }
    }
    super.eventMouseClicked(e);
  }

}
