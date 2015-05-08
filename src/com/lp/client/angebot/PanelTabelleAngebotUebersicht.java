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

import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.personal.service.PersonalDto;


/**
 * @author Uli Walch
 */
public class PanelTabelleAngebotUebersicht
    extends PanelTabelle {
	
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaKritAuswertung = null;


  /**
   * PanelTabelle.
   *
   * @param iUsecaseIdI die eindeutige UseCase ID
   * @param sTitelTabbedPaneI der Titel des aktuellen TabbedPane
   * @param oInternalFrameI der uebergeordente InternalFrame
   * @throws java.lang.Throwable Ausnahme
   */
  public PanelTabelleAngebotUebersicht(
      int iUsecaseIdI,
      String sTitelTabbedPaneI,
      InternalFrame oInternalFrameI)
      throws Throwable {
    super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);

    try {
 

      jbInit();
      initComponents();
    }
    catch (Throwable t) {
      LPMain.getInstance().exitFrame(getInternalFrame());
    }
  }

  /**
   * Initialisiere alle Komponenten; braucht der JBX-Designer;
   * hier bitte keine wilden Dinge wie zum Server gehen, etc. machen.
   *
   * @throws Exception
   */
  private void jbInit() throws Exception {
    wlaKritAuswertung = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaKritAuswertung, 350);
    wlaKritAuswertung.setHorizontalAlignment(SwingConstants.LEADING);

    setFirstColumnVisible(false);
  }

  /**
   * eventActionRefresh
   *
   * @param e ActionEvent
   * @param bNeedNoRefreshI boolean
   * @throws Throwable
   */
  protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws
      Throwable {
    super.eventActionRefresh(e, bNeedNoRefreshI);

    StringBuffer buff = new StringBuffer();

    buff.append(
        getDefaultFilter()[AngebotFac.IDX_KRIT_GESCHAEFTSJAHR]
        .formatFilterKriterium(LPMain.getTextRespectUISPr("lp.auswertung")));

    buff.append(", ");

    if (getDefaultFilter()[AngebotFac.IDX_KRIT_VERTRETER_I_ID].value != null) {
      Integer iIdPersonal = new Integer(getDefaultFilter()[AngebotFac.IDX_KRIT_VERTRETER_I_ID].value);

      PersonalDto vertreterDto = DelegateFactory.getInstance().getPersonalDelegate().
          personalFindByPrimaryKey(iIdPersonal);

      buff.append(LPMain.getTextRespectUISPr("label.vertreter"))
          .append(" ")
          .append(vertreterDto.getPartnerDto().formatTitelAnrede());
    }
    else {
      buff.append(LPMain.getTextRespectUISPr("lp.allevertreter"));
    }

    wlaKritAuswertung.setText(buff.toString());
    getPanelFilterKriterien().add(wlaKritAuswertung);

    String cNrMandantenwaehrung = DelegateFactory.getInstance().getMandantDelegate().mandantFindByPrimaryKey(
        LPMain.getTheClient().getMandant()).getWaehrungCNr();

    getTable().getColumnModel().getColumn(1).setHeaderValue(
        LPMain.getTextRespectUISPr("lp.anzeigein")
        + " "
        + cNrMandantenwaehrung);
  }
}
