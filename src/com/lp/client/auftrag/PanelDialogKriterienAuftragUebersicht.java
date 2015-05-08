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
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;


/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Auftrag Uebersicht.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>21. 01. 2005</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.5 $
 */
public class PanelDialogKriterienAuftragUebersicht
    extends PanelDialogKriterien {
	private static final long serialVersionUID = 1L;
	private InternalFrameAuftrag intFrame = null;
	private TabbedPaneAuftrag tpAuftrag = null;
	
	private WrapperLabel wlaNachdatum = null;
	private ButtonGroup jbgNachdatum = null;
	private WrapperLabel wlaEmptyLabel1 = null;
	private WrapperRadioButton wrbDatumBelegdatum = null;
	private WrapperRadioButton wrbDatumLiefertermin = null;
	private WrapperRadioButton wrbDatumFinaltermin = null;
	
	private WrapperLabel wlaEmptyLabel2 = null;
	
	private WrapperLabel wlaGeschaeftsjahr = null;
	private WrapperLabel wlaEmptyLabel3 = null;
	private WrapperComboBox wcoGeschaeftsjahr = null;
	
	private WrapperLabel wlaGeschaeftsjahresbeginn = null;
	private WrapperLabel wlaEmptyLabel4 = null;
	private WrapperLabel wlaDatum = null;
  
	private ButtonGroup jbgJahr = null;
	private WrapperRadioButton wrbKalenderjahr = null;
	private WrapperRadioButton wrbGeschaeftsjahr = null;
	private WrapperLabel wlaPeriode = null;


  public PanelDialogKriterienAuftragUebersicht(InternalFrame oInternalFrameI,
                                               String title) throws
      Throwable {
    super(oInternalFrameI, title);

    intFrame = (InternalFrameAuftrag) getInternalFrame();
    tpAuftrag = intFrame.getTabbedPaneAuftrag();

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
    wlaNachdatum = new WrapperLabel(LPMain.getTextRespectUISPr("label.auswertung"));
    wlaNachdatum.setMaximumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
    wlaNachdatum.setMinimumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
    wlaNachdatum.setPreferredSize(new Dimension(120,
                                                Defaults.getInstance().getControlHeight()));
    wlaNachdatum.setHorizontalAlignment(SwingConstants.LEADING);

    wlaEmptyLabel1 = new WrapperLabel();
    wlaEmptyLabel1.setMaximumSize(new Dimension(10, Defaults.getInstance().getControlHeight()));
    wlaEmptyLabel1.setMinimumSize(new Dimension(10, Defaults.getInstance().getControlHeight()));
    wlaEmptyLabel1.setPreferredSize(new Dimension(10,
                                                  Defaults.getInstance().getControlHeight()));

    jbgNachdatum = new ButtonGroup();

    wrbDatumBelegdatum = new WrapperRadioButton();
    wrbDatumBelegdatum.setText(
        LPMain.getTextRespectUISPr("label.belegdatum"));

    wrbDatumLiefertermin = new WrapperRadioButton();
    wrbDatumLiefertermin.setText(
        LPMain.getTextRespectUISPr("label.liefertermin"));

    wrbDatumFinaltermin = new WrapperRadioButton();
    wrbDatumFinaltermin.setText(
        LPMain.getTextRespectUISPr("label.finaltermin"));

    jbgNachdatum.add(wrbDatumBelegdatum);
    jbgNachdatum.add(wrbDatumLiefertermin);
    jbgNachdatum.add(wrbDatumFinaltermin);

    wlaEmptyLabel2 = new WrapperLabel();

    wlaGeschaeftsjahr = new WrapperLabel(
        LPMain.getTextRespectUISPr("label.geschaeftsjahr"));
    wlaGeschaeftsjahr.setHorizontalAlignment(SwingConstants.LEADING);
    wlaEmptyLabel3 = new WrapperLabel();
    wcoGeschaeftsjahr = new WrapperComboBox();
    wcoGeschaeftsjahr.setMandatoryFieldDB(true);

    wlaGeschaeftsjahresbeginn = new WrapperLabel(LPMain.getTextRespectUISPr("lp.geschaeftsjahresbeginn"));
    wlaGeschaeftsjahresbeginn.setHorizontalAlignment(SwingConstants.LEADING);
    wlaEmptyLabel4 = new WrapperLabel();
    wlaDatum = new WrapperLabel();
    wlaDatum.setHorizontalAlignment(SwingConstants.LEADING);
    
    wlaPeriode = new WrapperLabel(
            LPMain.getTextRespectUISPr("label.periode"));
    wlaPeriode.setHorizontalAlignment(SwingConstants.LEADING);

    wrbKalenderjahr = new WrapperRadioButton();
    wrbKalenderjahr.setText(
        LPMain.getTextRespectUISPr("label.kalenderjahr"));

    wrbGeschaeftsjahr = new WrapperRadioButton();
    wrbGeschaeftsjahr.setText(
        LPMain.getTextRespectUISPr("label.geschaeftsjahr"));
      jbgJahr=new ButtonGroup();
    jbgJahr.add(wrbKalenderjahr);
    jbgJahr.add(wrbGeschaeftsjahr);


    // Zeile
    jpaWorkingOn.add(wlaNachdatum,
                        new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));

    // Zeile
    iZeile++;
    jpaWorkingOn.add(wlaEmptyLabel1,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wrbDatumBelegdatum,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

      // Zeile
    iZeile++;
    jpaWorkingOn.add(wrbDatumLiefertermin,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;
    jpaWorkingOn.add(wrbDatumFinaltermin,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;
    jpaWorkingOn.add(wlaEmptyLabel2,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));

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

    // Zeile
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

     // Zeile
     iZeile++;
     jpaWorkingOn.add(wlaGeschaeftsjahresbeginn,
                         new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.BOTH,
                                                new Insets(2, 2, 2, 2), 0, 0));

      // Zeile
      iZeile++;
      jpaWorkingOn.add(wlaEmptyLabel4,
                          new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(2, 2, 2, 2), 0, 0));
      jpaWorkingOn.add(wlaDatum,
                          new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.BOTH,
                                             new Insets(2, 2, 2, 2), 0, 0));
  }

  private void setDefaults() throws Throwable {
    GregorianCalendar gcReferenz = new GregorianCalendar();

    wrbDatumBelegdatum.setSelected(true);

    wcoGeschaeftsjahr.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllGeschaeftsjahr());
    // Default ist das aktuelle GJ
    wcoGeschaeftsjahr.setKeyOfSelectedItem(DelegateFactory.getInstance().getParameterDelegate().
                                           getGeschaeftsjahr());

    ParametermandantDto pmBeginnMonat = DelegateFactory.getInstance().getParameterDelegate().
        getMandantparameter(
          LPMain.getTheClient().getMandant(),
          ParameterFac.KATEGORIE_ALLGEMEIN,
          ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);

    // die Monate im GregorianCalendar beginnen mit 0
    int iIndexBeginnMonat = ((Integer) pmBeginnMonat.getCWertAsObject()).intValue() - 1;

    String[] defaultMonths;
    DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getTheClient().getLocUi());
    defaultMonths = symbols.getMonths();

    String sNameMonat = defaultMonths[iIndexBeginnMonat];

    wlaDatum.setText("1. " + sNameMonat + " " + gcReferenz.get(Calendar.YEAR));
    wrbGeschaeftsjahr.setSelected(true);
  }

  /**
   * ptkrit: 2 die gewaehlten Kriterien zusammenbauen.
   * <br>Es gilt fuer Auftrag Uebersicht:
   * <br>Krit1 : Auswertung (Belegdatum oder Liefertermin oder Finaltermin) = Auswahl Geschaeftsjahr
   * @return FilterKriterium[]
   * @throws java.lang.Throwable Ausnahme
   */
  public FilterKriterium[] buildFilterKriterien() throws Throwable {
    aAlleKriterien = new FilterKriterium[AuftragFac.ANZAHL_KRITERIEN_UMSATZUEBERSICHT];
    
    FilterKriterium fkjahr = null;
    
    if(wrbKalenderjahr.isSelected()) {
        // Kalenderjahr
         fkjahr = new FilterKriterium(
        		 AuftragFac.KRIT_UEBERSICHT_KALENDERJAHR,
              true,
              wcoGeschaeftsjahr.getKeyOfSelectedItem().toString(),
              FilterKriterium.OPERATOR_EQUAL,
              false);
      }
      if(wrbGeschaeftsjahr.isSelected()) {
    	    // Geschaeftsjahr
    	 fkjahr = new FilterKriterium(
    			 AuftragFac.KRIT_UEBERSICHT_GESCHAEFTSJAHR,
    	          true,
    	          wcoGeschaeftsjahr.getKeyOfSelectedItem().toString(),
    	          FilterKriterium.OPERATOR_EQUAL,
    	          false);

      }
    aAlleKriterien[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_JAHR] = fkjahr;

    FilterKriterium fkAuswertung = null;

    if (wrbDatumBelegdatum.isSelected()) {
      // Auswertung nach Belegdatum
      fkAuswertung = new FilterKriterium(
          AuftragFac.KRIT_BELEGDATUM,
          wrbDatumBelegdatum.isSelected(),
          wcoGeschaeftsjahr.getKeyOfSelectedItem().toString(),
          FilterKriterium.OPERATOR_EQUAL, false);
    }
    else
    if (wrbDatumLiefertermin.isSelected()) {
      // Auswertung nach Liefertermin
      fkAuswertung = new FilterKriterium(
          AuftragFac.KRIT_LIEFERTERMIN,
          wrbDatumLiefertermin.isSelected(),
          wcoGeschaeftsjahr.getKeyOfSelectedItem().toString(),
          FilterKriterium.OPERATOR_EQUAL, false);
    }
    else
    if (wrbDatumFinaltermin.isSelected()) {
      // Auswertung nach Finaltermin
      fkAuswertung = new FilterKriterium(
          AuftragFac.KRIT_FINALTERMIN,
          wrbDatumFinaltermin.isSelected(),
          wcoGeschaeftsjahr.getKeyOfSelectedItem().toString(),
          FilterKriterium.OPERATOR_EQUAL, false);
    }

    aAlleKriterien[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_AUSWERTUNG] = fkAuswertung;

    return aAlleKriterien;
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      buildFilterKriterien();
    }

    // den Dialog verlassen
    super.eventActionSpecial(e);

    if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      tpAuftrag.setBKriterienAuftragUebersichtUeberMenueAufgerufen(false);
      tpAuftrag.gotoAuswahl();
    }
  }

  public FilterKriterium[] getAlleFilterKriterien() throws Throwable {
    return buildFilterKriterien();
  }
}
