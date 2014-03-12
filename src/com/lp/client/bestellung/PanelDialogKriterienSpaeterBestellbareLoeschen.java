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
package com.lp.client.bestellung;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

/**
 * <p> Diese Klasse kuemmert sich um die Eingabe der Kriterien,
 * um spaeter bestellbare Positionen aus dem BV zu loeschen</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004 - 2007</p>
 *
 * <p>Erstellung: Martin Bluehweis; 25.08.07</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 07:47:52 $
 */
public class PanelDialogKriterienSpaeterBestellbareLoeschen
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperDateField wdfDatum = new WrapperDateField();
  private WrapperLabel wlaDatum = new WrapperLabel();

  public PanelDialogKriterienSpaeterBestellbareLoeschen(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getTextRespectUISPr("bes.spaeterbestellbareloeschen"));
    jbInitPanel();
    initComponents();
  }


  /**
   * Dialog initialisieren
   *
   * @throws Throwable
   */
  private void jbInitPanel()
      throws Throwable {
    wlaDatum.setText(LPMain.getTextRespectUISPr("bes.naechsterbestellvorschlag"));
    wdfDatum.setMandatoryField(true);
    jpaWorkingOn.setLayout(new GridBagLayout());
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaDatum,
                     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wdfDatum,
                     new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));
    // Default auf heute in 1 Woche setzen
    wdfDatum.setDate(Helper.addiereTageZuDatum(new java.sql.Date(System.currentTimeMillis()), 7));
  }


  /**
   * Datum auslesen.
   * @return Date
   */
  public java.sql.Date getDate() {
    return wdfDatum.getDate();
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfDatum;
  }
}
