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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Kunden Umsatz Statistik.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>11. 05. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.3 $
 */
public class PanelDialogKriterienUmsatz
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaNachdatum = null;
  private ButtonGroup jbgJahr = null;
  private WrapperLabel wlaEmptyLabel1 = null;
  private WrapperRadioButton wrbKalenderjahr = null;
  private WrapperRadioButton wrbGeschaeftsjahr = null;
  private FilterKriterium fkKDLF = null;


  public PanelDialogKriterienUmsatz(InternalFrame oInternalFrameI,
                                    String title,
                                    FilterKriterium fkKDLFI)
      throws Throwable {

    super(oInternalFrameI, title);

    fkKDLF = fkKDLFI;
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
    // die Gruppe mit nach Datum
    wlaNachdatum = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("label.auswertung"));
    wlaNachdatum.setMaximumSize(new Dimension(120, 23));
    wlaNachdatum.setMinimumSize(new Dimension(120, 23));
    wlaNachdatum.setPreferredSize(new Dimension(120, 23));
    wlaNachdatum.setHorizontalAlignment(SwingConstants.LEADING);

    wlaEmptyLabel1 = new WrapperLabel();
    wlaEmptyLabel1.setMaximumSize(new Dimension(10, 23));
    wlaEmptyLabel1.setMinimumSize(new Dimension(10, 23));
    wlaEmptyLabel1.setPreferredSize(new Dimension(10, 23));

    jbgJahr = new ButtonGroup();

    wrbKalenderjahr = new WrapperRadioButton();
    wrbKalenderjahr.setText(
        LPMain.getInstance().getTextRespectUISPr("label.kalenderjahr"));

    wrbGeschaeftsjahr = new WrapperRadioButton();
    wrbGeschaeftsjahr.setText(
        LPMain.getInstance().getTextRespectUISPr("label.geschaeftsjahr"));

    jbgJahr.add(wrbGeschaeftsjahr);
    jbgJahr.add(wrbKalenderjahr);

    jpaWorkingOn.add(wlaNachdatum,
                     new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbKalenderjahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbGeschaeftsjahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));
  }


  private void setDefaults()
      throws Throwable {
    wrbKalenderjahr.setSelected(true);
    /**
     * @todo auswertung nach GJ  PJ 4929
     */
    wrbGeschaeftsjahr.setVisible(false);
  }


  public FilterKriterium[] buildFilterKriterien()
      throws Throwable {
    // hier sind KD und LF gleich, der KD definiert
    aAlleKriterien = new FilterKriterium[KundeFac.ANZAHL_KRITERIEN];

    FilterKriterium fkJahr = null;

    if (wrbGeschaeftsjahr.isSelected()) {
      fkJahr = PartnerFilterFactory.getInstance().createFKKriteriumGeschaeftsjahr(
          wrbGeschaeftsjahr.isSelected());
    }
    else if (wrbKalenderjahr.isSelected()) {
      fkJahr = PartnerFilterFactory.getInstance().createFKKriteriumKalenderjahr(
          wrbGeschaeftsjahr.isSelected());
    }
    aAlleKriterien[KundeFac.IDX_KRIT_KUNDE_I_ID] = fkKDLF;
    aAlleKriterien[KundeFac.IDX_KRIT_JAHR] = fkJahr;
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


  public void setFkKDLF(FilterKriterium fkKDLF) {
    this.fkKDLF = fkKDLF;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wrbKalenderjahr;
  }
}
