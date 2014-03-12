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
import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;


/**
 * <p><I>Dialog zur Auswahl des MahnsperreDatums fuer eine Bestellung</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>17. 10. 2005</I></p>
 * @todo Gueltigen Datumsbereich ueberpruefen  PJ 4900
 * <p> </p>
 * @author  Josef Erlinger
 * @version $Revision: 1.3 $
 */
public class PanelDialogBSMahnsperreBis
    extends PanelDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperDateField wdfDatum = new WrapperDateField();
  private WrapperLabel wlaNeuDatum = new WrapperLabel();
  private final BestellungDto bestellungDto;
  private WrapperButton wbuOk = new WrapperButton();

  private final static String ACTION_SPECIAL_MAHNSPERRE_DATUM_OK = "action_special_" +
      ALWAYSENABLED + "mahnsperre_datum_ok";

  public PanelDialogBSMahnsperreBis(InternalFrame p0, String title, BestellungDto bestellungDto)
      throws Throwable {
    super(p0, title);
    this.bestellungDto = bestellungDto;
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
    wlaNeuDatum.setText(LPMain.getTextRespectUISPr("lp.mahnsperrebis"));
    wbuOk.setText(LPMain.getTextRespectUISPr("button.ok"));
    wbuOk.addActionListener(this);
    wbuOk.setActionCommand(ACTION_SPECIAL_MAHNSPERRE_DATUM_OK);
    jpaWorkingOn.setLayout(new GridBagLayout());
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0,
        0));
    jpaWorkingOn.add(wlaNeuDatum, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0,
        0));
    jpaWorkingOn.add(wbuOk, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0,
        0));
    // Default auf heute setzen
    if (bestellungDto.getIId() != null) {
      wdfDatum.setDate(new java.sql.Date(System.currentTimeMillis()));
    }
  }


  /**
   * Datum auslesen.
   * @return Date
   */
  public java.sql.Date getDate() {
    return wdfDatum.getDate();
  }


  /**
   *
   * @param e ActionEvent
   * @throws Throwable
   * @todo vom internalframe das dautm abholen  PJ 4908
   */
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    super.eventActionSpecial(e);
    if (e.getActionCommand().equals(ACTION_SPECIAL_MAHNSPERRE_DATUM_OK)) {
      if (!bestellungDto.getBestellungartCNr().equals(BestellungFac.
          BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
        DelegateFactory.getInstance().getBestellungDelegate().setzeBSMahnsperre(
            bestellungDto.getIId(), getDate());

        //schliessen des panels
        getInternalFrame().closePanelDialog();
      }
      else {
        DialogFactory.showModalDialog(
            LPMain.getTextRespectUISPr("lp.error"),
            LPMain.getTextRespectUISPr("bes.mahnsperrenichtmoeglichweilrahmenbestellung"));
        return;
      }
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfDatum;
  }
}
