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
package com.lp.client.partner;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 23.08.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 09:44:45 $
 */
public class PanelDialogKriterienMonatsstatistik
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaGeschaeftsjahr = null;
  private WrapperComboBox wcoGeschaeftsjahr = null;
  private FilterKriterium fkKDoderLF = null;

  public PanelDialogKriterienMonatsstatistik(InternalFrame oInternalFrameI,
                                             String title)
      throws Throwable {

    super(oInternalFrameI, title);

    jbInit();
    setDefaults();
    initComponents();
  }


  /**
   * Dialog initialisieren
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {

    wcoGeschaeftsjahr = new WrapperComboBox();
    wcoGeschaeftsjahr.setMandatoryField(true);
    wlaGeschaeftsjahr = new WrapperLabel(LPMain.getInstance().
                                         getTextRespectUISPr("label.geschaeftsjahr"));

    jpaWorkingOn.add(wlaGeschaeftsjahr,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcoGeschaeftsjahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  private void setDefaults()
      throws Throwable {
    wcoGeschaeftsjahr.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllGeschaeftsjahr());
    wcoGeschaeftsjahr.setKeyOfSelectedItem(DelegateFactory.getInstance().getParameterDelegate().
                                           getGeschaeftsjahr());
  }


  /**
   * ptkrit: 2 die gewaehlten Kriterien zusammenbauen.
   * <br>Es gilt fuer Auftrag Uebersicht:
   * <br>Krit1 : Auswertung (Belegdatum oder Liefertermin oder Finaltermin) =
   * Auswahl Geschaeftsjahr
   *
   * @throws Throwable
   * @return FilterKriterium[]
   */
  public FilterKriterium[] buildFilterKriterien()
      throws Throwable {

    aAlleKriterien = new FilterKriterium[LieferantFac.ANZAHL_KRITERIEN];

    FilterKriterium fkJahr = new FilterKriterium(
        KundeFac.KRIT_JAHR_GESCHAEFTSJAHR,
        true,
        wcoGeschaeftsjahr.getKeyOfSelectedItem().toString(),
        FilterKriterium.OPERATOR_EQUAL,
        false);
    aAlleKriterien[LieferantFac.IDX_KRIT_GESCHAEFTSJAHR] = fkJahr;

    aAlleKriterien[LieferantFac.IDX_KRIT_LIEFERANT_ODER_KUNDE_I_ID] = fkKDoderLF;

    return aAlleKriterien;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {

    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      buildFilterKriterien();
    }

    // den Dialog verlassen
    super.eventActionSpecial(e);
  }


  public FilterKriterium[] getAlleFilterKriterien()
      throws Throwable {
    return buildFilterKriterien();
  }


  public FilterKriterium getFkKDoderLF() {
    return fkKDoderLF;
  }


  public void setFkKDoderLF(FilterKriterium fkKDoderLF) {
    this.fkKDoderLF = fkKDoderLF;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wcoGeschaeftsjahr;
  }
}
