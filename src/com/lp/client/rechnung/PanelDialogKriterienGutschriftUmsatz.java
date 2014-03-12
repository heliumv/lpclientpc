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
package com.lp.client.rechnung;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Gutschrift Uebersicht.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>21. 01. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelDialogKriterienGutschriftUmsatz
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Cache for convenience. */
  private InternalFrameRechnung intFrame = null;
  /** Cache for convenience. */
  private TabbedPaneGutschrift tabbedPaneGutschrift = null;

  private WrapperLabel wlaEmptyLabel3 = null;
  private WrapperComboBox wcoJahr = null;
  private ButtonGroup jbgJahr = null;
  private WrapperRadioButton wrbKalenderjahr = null;
  private WrapperRadioButton wrbGeschaeftsjahr = null;
  private WrapperLabel wlaPeriode = null;

  public PanelDialogKriterienGutschriftUmsatz(InternalFrame oInternalFrameI,
                                            String title,
                                            TabbedPaneGutschrift tabbedPaneRechnung)
      throws Throwable {
    super(oInternalFrameI, title);
    intFrame = (InternalFrameRechnung) getInternalFrame();
    this.tabbedPaneGutschrift = tabbedPaneRechnung;
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
    wlaPeriode = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("label.periode"));
    wlaPeriode.setHorizontalAlignment(SwingConstants.LEADING);
    wlaPeriode.setMinimumSize(new Dimension(200, Defaults.getInstance().getControlHeight()));
    wlaPeriode.setPreferredSize(new Dimension(200, Defaults.getInstance().getControlHeight()));
    wlaPeriode.setHorizontalAlignment(SwingConstants.LEADING);

    wlaEmptyLabel3 = new WrapperLabel();
    wcoJahr = new WrapperComboBox();
    wcoJahr.setMandatoryField(true);
    wrbKalenderjahr = new WrapperRadioButton();
    wrbKalenderjahr.setText(
        LPMain.getInstance().getTextRespectUISPr("label.kalenderjahr"));

    wrbGeschaeftsjahr = new WrapperRadioButton();
    wrbGeschaeftsjahr.setText(
        LPMain.getInstance().getTextRespectUISPr("label.geschaeftsjahr"));
      jbgJahr=new ButtonGroup();
    jbgJahr.add(wrbKalenderjahr);
    jbgJahr.add(wrbGeschaeftsjahr);

    jpaWorkingOn.add(wlaPeriode,
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
    iZeile++;
    jpaWorkingOn.add(wlaEmptyLabel3,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcoJahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
  }


  private void setDefaults()
      throws Throwable {
    wrbKalenderjahr.setSelected(true);
    wrbGeschaeftsjahr.setVisible(true);
    wcoJahr.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllGeschaeftsjahr());
    // Default ist das aktuelle Jahr
    wcoJahr.setKeyOfSelectedItem(DelegateFactory.getInstance().getParameterDelegate().
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
    aAlleKriterien = new FilterKriterium[1];

    FilterKriterium fkJahr = null;
    if(wrbKalenderjahr.isSelected()) {
      fkJahr = RechnungFilterFactory.getInstance().createFKKriteriumKalenderjahr(true,
          wcoJahr.getKeyOfSelectedItem().toString());
    }
    if (wrbGeschaeftsjahr.isSelected()) {
      fkJahr = RechnungFilterFactory.getInstance().createFKKriteriumGeschaeftsjahr(true,
          wcoJahr.getKeyOfSelectedItem().toString());
    }

    aAlleKriterien[RechnungFac.IDX_KRIT_JAHR] = fkJahr;

    return aAlleKriterien;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      buildFilterKriterien();
    }
    // den Dialog verlassen
    super.eventActionSpecial(e);
    // falls schliessen, dann zur Auswahl
    if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      tabbedPaneGutschrift.gotoAuswahl();
    }
  }


  public FilterKriterium[] getAlleFilterKriterien()
      throws Throwable {
    return buildFilterKriterien();
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wcoJahr;
  }
}
