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
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access") 
/**
 * <p><I>Dialog zur Eingabe des Divisors.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>04. 08. 2005</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelDialogDivisor
    extends PanelDialogKriterien {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Cache for convenience. */
  private InternalFrameBestellung intFrame = null;
  /** Cache for convenience. */
  private TabbedPaneBestellung tpBestellung = null;

  private WrapperLabel wlaDivisor = null;
  private WrapperNumberField wnfDivisor = null;

  public static final int KRIT_DIVISOR = 0;

  public PanelDialogDivisor(InternalFrame oInternalFrameI,
                                               String title) throws
      Throwable {
    super(oInternalFrameI, title);

    intFrame = (InternalFrameBestellung) getInternalFrame();
    tpBestellung = intFrame.getTabbedPaneBestellung();

    jbInit();
    setDefaults();
    initComponents();
  }

  /**
   * Dialog initialisieren
   * @throws Throwable
   */
  private void jbInit() throws Throwable {
    // die Gruppe mit nach Datum
    wlaDivisor = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("bes.divisor"));
    HelperClient.setDefaultsToComponent(wlaDivisor, 120);

    wnfDivisor = new WrapperNumberField();
    wnfDivisor.setMandatoryField(true);
    wnfDivisor.setMaximumIntegerDigits(3);
    wnfDivisor.setFractionDigits(0);
    wnfDivisor.setMinimumValue(1);
    HelperClient.setDefaultsToComponent(wnfDivisor, 30);

    // Zeile
    jpaWorkingOn.add(wlaDivisor,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfDivisor,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
  }

  private void setDefaults() throws Throwable {
    wnfDivisor.setInteger(new Integer(1));
  }

  /**
   * Die gewaehlten Kriterien zusammenbauen.
   * @return FilterKriterium[] die Kriterien
   * @throws java.lang.Throwable Ausnahme
   */
  public FilterKriterium[] buildFilterKriterien() throws Throwable {
    aAlleKriterien = new FilterKriterium[1];

    aAlleKriterien[KRIT_DIVISOR] = new FilterKriterium(
          "divisor",
          true,
          wnfDivisor.getInteger().toString(),
          FilterKriterium.OPERATOR_EQUAL,
          false);

    return aAlleKriterien;
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      buildFilterKriterien();
    }

    // den Dialog verlassen
    super.eventActionSpecial(e);

//    if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
//      // Sonderfall: Wechsel in Sicht Rahmenpositionen
//      tpBestellung.gotoSichtRahmen();
//    }
  }

  public FilterKriterium[] getAlleFilterKriterien() throws Throwable {
    return buildFilterKriterien();
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wnfDivisor;
  }
}
