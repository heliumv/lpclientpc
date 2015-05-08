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
package com.lp.client.eingangsrechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.Helper;

 
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die ER Uebersicht.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>28. 02. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.5 $
 */
public class PanelDialogKriterienEingangsrechnungUebersicht
    extends PanelDialogKriterien {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/** Cache for convenience. */
  private TabbedPaneEingangsrechnung tabbedPaneER = null;

  private WrapperLabel wlaNachdatum = null;
  private ButtonGroup jbgNachdatum = null;
  private ButtonGroup jbgJahr = null;
  private WrapperLabel wlaEmptyLabel1 = null;
  private WrapperRadioButton wrbBelegdatum = null;
  private WrapperRadioButton wrbFreigabedatum = null;
  private WrapperRadioButton wrbKalenderjahr = null;
  private WrapperRadioButton wrbGeschaeftsjahr = null;

  private WrapperLabel wlaEmptyLabel2 = null;

  private WrapperLabel wlaPeriode = null;
  private WrapperLabel wlaEmptyLabel3 = null;
  private WrapperComboBox wcoGeschaeftsjahr = null;

  public PanelDialogKriterienEingangsrechnungUebersicht(InternalFrame oInternalFrameI,
      String title, TabbedPaneEingangsrechnung tabbedPaneER) throws
      HeadlessException, Throwable {
    super(oInternalFrameI, title);
    this.tabbedPaneER = tabbedPaneER;
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
    wlaNachdatum = new WrapperLabel(
        LPMain.getTextRespectUISPr("label.auswertung"));
    wlaNachdatum.setMaximumSize(new Dimension(120, 23));
    wlaNachdatum.setMinimumSize(new Dimension(120, 23));
    wlaNachdatum.setPreferredSize(new Dimension(120, 23));
    wlaNachdatum.setHorizontalAlignment(SwingConstants.LEADING);

    wlaEmptyLabel1 = new WrapperLabel();
    wlaEmptyLabel1.setMaximumSize(new Dimension(10, 23));
    wlaEmptyLabel1.setMinimumSize(new Dimension(10, 23));
    wlaEmptyLabel1.setPreferredSize(new Dimension(10, 23));

    jbgNachdatum = new ButtonGroup();
    jbgJahr = new ButtonGroup();

    wrbBelegdatum = new WrapperRadioButton();
    wrbBelegdatum.setText(
        LPMain.getTextRespectUISPr("label.belegdatum"));

    wrbFreigabedatum = new WrapperRadioButton();
    wrbFreigabedatum.setText(
        LPMain.getTextRespectUISPr("label.freigabedatum"));

    wrbKalenderjahr = new WrapperRadioButton();
    wrbKalenderjahr.setText(
        LPMain.getTextRespectUISPr("label.kalenderjahr"));

    wrbGeschaeftsjahr = new WrapperRadioButton();
    wrbGeschaeftsjahr.setText(
        LPMain.getTextRespectUISPr("label.geschaeftsjahr"));

    jbgNachdatum.add(wrbBelegdatum);
    jbgNachdatum.add(wrbFreigabedatum);

    jbgJahr.add(wrbGeschaeftsjahr);
    jbgJahr.add(wrbKalenderjahr);

    wlaEmptyLabel2 = new WrapperLabel();

    wlaPeriode = new WrapperLabel(
        LPMain.getTextRespectUISPr("label.periode"));
    wlaPeriode.setHorizontalAlignment(SwingConstants.LEADING);
    wlaEmptyLabel3 = new WrapperLabel();
    wcoGeschaeftsjahr = new WrapperComboBox();
    wcoGeschaeftsjahr.setMandatoryField(true);

    jpaWorkingOn.add(wlaNachdatum,
                     new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaEmptyLabel1,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wrbBelegdatum,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbFreigabedatum,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));
    iZeile++;
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
    jpaWorkingOn.add(wlaEmptyLabel2,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaEmptyLabel3,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcoGeschaeftsjahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
 
  }

  private void setDefaults() throws Throwable {
    wrbBelegdatum.setSelected(true);
    wrbKalenderjahr.setSelected(true);
    wrbGeschaeftsjahr.setVisible(true);

    wcoGeschaeftsjahr.setMap(DelegateFactory.getInstance().getSystemDelegate().
                             getAllGeschaeftsjahr());
    // Default ist das aktuelle GJ
    wcoGeschaeftsjahr.setKeyOfSelectedItem(DelegateFactory.getInstance().
                                           getParameterDelegate().getGeschaeftsjahr());
    String mandantWaehrung = DelegateFactory.getInstance().getMandantDelegate().
        mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).
        getWaehrungCNr();
   
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
  public FilterKriterium[] buildFilterKriterien() throws Throwable {
    aAlleKriterien = new FilterKriterium[EingangsrechnungFac.ANZAHL_KRITERIEN];

    FilterKriterium fkDatum = null;
    FilterKriterium fkJahr = null;
    

    if (wrbBelegdatum.isSelected()) {
      // Auswertung nach Belegdatum
      fkDatum = EingangsrechnungFilterFactory.getInstance().createFKKriteriumBelegdatum(wrbBelegdatum.isSelected(),wcoGeschaeftsjahr.getKeyOfSelectedItem().toString());
    }
    else if (wrbFreigabedatum.isSelected()) {
      // Auswertung nach Freigabedatum
      fkDatum = EingangsrechnungFilterFactory.getInstance().createFKKriteriumFreigabedatum(wrbBelegdatum.isSelected(), wcoGeschaeftsjahr.getKeyOfSelectedItem().toString());
    }
    if(wrbGeschaeftsjahr.isSelected()) {
      fkJahr = EingangsrechnungFilterFactory.getInstance().createFKKriteriumGeschaeftsjahr(wrbBelegdatum.isSelected(),wcoGeschaeftsjahr.getKeyOfSelectedItem().toString());
    }
    else if(wrbKalenderjahr.isSelected()) {
      fkJahr = EingangsrechnungFilterFactory.getInstance().createFKKriteriumKalenderjahr(wrbBelegdatum.isSelected(),wcoGeschaeftsjahr.getKeyOfSelectedItem().toString());
    }
 

    aAlleKriterien[EingangsrechnungFac.IDX_KRIT_DATUM] = fkDatum;
    aAlleKriterien[EingangsrechnungFac.IDX_KRIT_JAHR] = fkJahr;
    
    FilterKriterium fkZusatzkosten = new FilterKriterium(
			EingangsrechnungFac.KRIT_ZUSATZKOSTEN, false,
			Helper.boolean2Short(tabbedPaneER.isBZusatzkosten())+"", FilterKriterium.OPERATOR_EQUAL, false);
    
    aAlleKriterien[EingangsrechnungFac.IDX_KRIT_ZUSATZKOSTEN] = fkZusatzkosten;
    
    return aAlleKriterien;
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      buildFilterKriterien();
    }
    // den Dialog verlassen
    super.eventActionSpecial(e);
    // tabelletitel: NACH dem evtYouAreselected
    if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      tabbedPaneER.gotoAuswahl();
    }
  }

  public FilterKriterium[] getAlleFilterKriterien() throws Throwable {
    return buildFilterKriterien();
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wrbBelegdatum;
  }
}
