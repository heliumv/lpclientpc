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
package com.lp.client.frame.component;


import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.lp.client.frame.Command;
import com.lp.client.frame.Command2IFNebeneinander;
import com.lp.client.frame.CommandCreateIF;
import com.lp.client.frame.CommandGoto;
import com.lp.client.frame.CommandSetFocus;
import com.lp.client.frame.ICommand;
import com.lp.client.partner.TabbedPaneLieferant;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


public class PanelQueryFLRGotoLieferant
    extends PanelQueryFLR
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private InternalFrame intFrame = null;
  private static final String ACTION_SPECIAL_GOTO = "action_special_goto";

  public PanelQueryFLRGotoLieferant(QueryType[] typesI,
                                    FilterKriterium[] filtersI,
                                    String[] aWhichButtonIUseI,
                                    InternalFrame internalFrameI,
                                    String add2TitleI)
      throws Throwable {
    super(typesI, filtersI, QueryParameters.UC_ID_LIEFERANTEN, aWhichButtonIUseI,
          internalFrameI, add2TitleI);
    this.createAndSaveAndShowButton(
        "/com/lp/client/res/goto.png",
        LPMain.getTextRespectUISPr("lp.goto"),
        ACTION_SPECIAL_GOTO, null);
    intFrame = internalFrameI;
  }


  /**
   * Konstruktor fuer eine FLR Liste, in der ein bestimmter Datensatz selektiert
   * ist.
   * <br>Der Key dieses Datensatzes wird als Parameter uebergeben.
   *
   * @param typesI die UI Filterkriterien fuer den Benutzer
   * @param filtersI die default Filterkriterien, die fuer den Benutzer nicht
   *   sichtbar sind
   * @param aWhichButtonIUseI welche Buttons sind auf dem Panel sichtbar
   * @param internalFrameI den InternalFrame als Kontext setzen
   * @param add2TitleI der Titel dieses Panels
   * @param oSelectedIdI der Datensatz mit diesem Key soll selektiert werden
   * @throws Throwable
   */
  public PanelQueryFLRGotoLieferant(QueryType[] typesI,
                                    FilterKriterium[] filtersI,
                                    String[] aWhichButtonIUseI,
                                    InternalFrame internalFrameI,
                                    String add2TitleI,
                                    Object oSelectedIdI)
      throws Throwable {
    super(typesI, filtersI, QueryParameters.UC_ID_LIEFERANTEN, aWhichButtonIUseI,
          internalFrameI, add2TitleI,
          oSelectedIdI);
  }


  public void eventActionNew(EventObject eventObject,
                             boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    dialog.dispose();
    CommandCreateIF createIFLieferant = new CommandCreateIF(
        Command.S_INTERNALFRAME_LIEFERANT);
    LPMain.getInstance().execute(createIFLieferant);
    Command2IFNebeneinander command2IFNebeneinander = new Command2IFNebeneinander(
        Command.S_INTERNALFRAME_LIEFERANT);
    command2IFNebeneinander.setSInternalFrame2I(intFrame.getBelegartCNr());
    LPMain.getInstance().execute(command2IFNebeneinander);
    CommandSetFocus setFocusCmd = new CommandSetFocus(
        Command.S_INTERNALFRAME_LIEFERANT);
    LPMain.getInstance().execute(setFocusCmd);

  }


  public int execute(Command command) {
    return ICommand.COMMAND_NOT_DONE;
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
      command2IFNebeneinander.setSInternalFrame2I(intFrame.getBelegartCNr());
      LPMain.getInstance().execute(command2IFNebeneinander);

      CommandSetFocus setFocusCmd = new CommandSetFocus(
          Command.S_INTERNALFRAME_LIEFERANT);
      LPMain.getInstance().execute(setFocusCmd);

      CommandGoto gotoPanelLieferantKopfdaten = new CommandGoto(
          Command.S_INTERNALFRAME_LIEFERANT,
          TabbedPaneLieferant.class,
          Command.PANEL_LIEFERANT_KOPFDATEN,
          ACTION_UPDATE);

      LPMain.getInstance().execute(gotoPanelLieferantKopfdaten);

    }
  }

}
