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
package com.lp.client.partner;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperLabel;

/**
 *
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: ? ?; dd.mm.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2009/04/08 08:40:01 $
 */
public class PanelTabelleUmsatz
    extends PanelTabelle
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private WrapperLabel wlaKritAuswertung = null;

  // das soll in der optionalen zweiten Zeile stehen
  private WrapperLabel wlaUmsatz = null;
  private String sAnrede = null;

  /**
   * PanelTabelle.
   *
   * @param iUsecaseIdI die eindeutige UseCase ID
   * @param sTitelTabbedPaneI der Titel des aktuellen TabbedPane
   * @param oInternalFrameI der uebergeordente InternalFrame
   * @param sAnredeI String
   * @throws Throwable
   */
  public PanelTabelleUmsatz(int iUsecaseIdI,
                            String sTitelTabbedPaneI,
                            InternalFrame oInternalFrameI,
                            String sAnredeI)
      throws Throwable {

    super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);

    this.sAnrede = sAnredeI;

    jbInit();
    initComponents();
  }
  
  public void setAnrede(String sAnrede){
	  this.sAnrede = sAnrede;
  }


  /**
   * Initialisiere alle Komponenten; braucht der JBX-Designer; hier bitte keine
   * wilden Dinge wie zum Server gehen, etc. machen.
   *
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    wlaKritAuswertung = new WrapperLabel();
    wlaUmsatz = new WrapperLabel();
    wlaUmsatz.setHorizontalAlignment(SwingConstants.CENTER);
    wlaUmsatz.setMaximumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
                                           Defaults.getInstance().getControlHeight()));
    getPanelOptionaleZweiteZeile().add(wlaUmsatz,
                                       new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    setFirstColumnVisible(false);
  }


  /**
   * eventActionRefresh
   *
   * @param e ActionEvent
   * @param bNeedNoRefreshI boolean
   * @throws Throwable
   */
  protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
      throws Throwable {

    super.eventActionRefresh(e, bNeedNoRefreshI);

    wlaKritAuswertung.setText("");
    wlaKritAuswertung.setMaximumSize(new Dimension(350,
        Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setMinimumSize(new Dimension(350,
        Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setPreferredSize(new Dimension(350,
        Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setHorizontalAlignment(SwingConstants.LEADING);
    getPanelFilterKriterien().add(wlaKritAuswertung);

    wlaUmsatz.setText(sAnrede);
  }


  protected String getLockMeWer()
      throws Exception {
    return null;
  }
}
