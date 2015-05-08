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

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinFac;

@SuppressWarnings("static-access") 
/**
 * @author Uli Walch
 */
public class PanelTabelleLieferscheinUebersicht
    extends PanelTabelle {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaKritAuswertung = null;

  /**
   * PanelTabelle.
   *
   * @param iUsecaseIdI die eindeutige UseCase ID
   * @param sTitelTabbedPaneI der Titel des aktuellen TabbedPane
   * @param oInternalFrameI der uebergeordente InternalFrame
   */
  public PanelTabelleLieferscheinUebersicht(
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

  private void jbInit() throws Exception {
    wlaKritAuswertung = new WrapperLabel();

    SPALTENBREITE_ZEILENHEADER = 90;
    setFirstColumnVisible(true);
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

    // der Table Header ist von den Kriterien abhaengig
    int iJahreszahl = Integer.parseInt(getDefaultFilter()[LieferscheinFac.IDX_KRIT_JAHRESZAHL].value);

    for (int i = 1; i < getTable().getColumnCount(); i++) {
      getTable().getColumnModel().getColumn(i).setHeaderValue(new Integer(iJahreszahl));
      iJahreszahl++;
    }

    // die gewaehlte Jahreszahl anzeigen
    wlaKritAuswertung.setText(
        getDefaultFilter()[LieferscheinFac.IDX_KRIT_JAHRESZAHL]
        .formatFilterKriterium(LPMain.getInstance().getTextRespectUISPr("lp.auswertung")));
    wlaKritAuswertung.setMaximumSize(new Dimension(350, Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setMinimumSize(new Dimension(350, Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setPreferredSize(new Dimension(350, Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setHorizontalAlignment(SwingConstants.LEADING);

    getPanelFilterKriterien().add(wlaKritAuswertung);
  }
}
