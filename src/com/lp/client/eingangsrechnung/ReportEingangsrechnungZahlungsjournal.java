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
package com.lp.client.eingangsrechnung;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


public class ReportEingangsrechnungZahlungsjournal
    extends PanelBasis implements PanelReportIfJRDS, PropertyChangeListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private WrapperLabel wlaSortierung = new WrapperLabel();
  private WrapperRadioButton wrbZahlungsausgang = new WrapperRadioButton();
  private WrapperRadioButton wrbRechnungsnummer = new WrapperRadioButton();
  private WrapperRadioButton wrbBankAuszug = new WrapperRadioButton();
  private WrapperLabel wlaZahlungsdatum = new WrapperLabel();
  private WrapperLabel wlaVon = new WrapperLabel();
  private WrapperLabel wlaBis = new WrapperLabel();
  private WrapperDateField wdfVon = new WrapperDateField();
  private WrapperDateField wdfBis = new WrapperDateField();
  private ButtonGroup bgSortierung = new ButtonGroup();
  private WrapperDateRangeController wdrBereich = null;

	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
  
  private final static String PROPERTY_DATUM_VON = "action_special_datum_von";
  private final static String PROPERTY_DATUM_BIS = "action_special_datum_bis";

  public ReportEingangsrechnungZahlungsjournal(TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung,
                                               String add2Title)
      throws Throwable {
    super(tabbedPaneEingangsrechnung.getInternalFrame(), add2Title);
    this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
    jbInit();
    setDefaults();
    initComponents();
  }


  /**
   * setDefaults
   */
  private void setDefaults() {
    wrbZahlungsausgang.setSelected(true);
    wdrBereich.doClickDown();
    wdrBereich.doClickUp();
  }


  /**
   * jbInit
   */
  private void jbInit() {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    wlaSortierung.setText("Sortierung");
    wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);
    wlaVon.setText("Von");
    wlaBis.setText("Bis");
    wrbBankAuszug.setText("Bank, Auszug");
    wrbRechnungsnummer.setText("Rechnungsnummer");
    wrbZahlungsausgang.setText("Zahlungsausgang");
    bgSortierung.add(wrbBankAuszug);
    bgSortierung.add(wrbRechnungsnummer);
    bgSortierung.add(wrbZahlungsausgang);
    wlaVon.setMinimumSize(new Dimension(40, Defaults.getInstance().getControlHeight()));
    wlaVon.setPreferredSize(new Dimension(40, Defaults.getInstance().getControlHeight()));
    wlaBis.setMinimumSize(new Dimension(40, Defaults.getInstance().getControlHeight()));
    wlaBis.setPreferredSize(new Dimension(40, Defaults.getInstance().getControlHeight()));
    wdfVon.setMandatoryField(true);
    wdfBis.setMandatoryField(true);
    wdfVon.getDisplay().addPropertyChangeListener(this);
    wdfBis.getDisplay().addPropertyChangeListener(this);
    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaSortierung,
                     new GridBagConstraints(0, iZeile, 5, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbZahlungsausgang,
                     new GridBagConstraints(0, iZeile, 5, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbRechnungsnummer,
                     new GridBagConstraints(0, iZeile, 5, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbBankAuszug,
                     new GridBagConstraints(0, iZeile, 5, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaVon,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfVon,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaBis,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfBis,
                     new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdrBereich,
                     new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.VERTICAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    // ein dummy damit alles links ist
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(5, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() == wdfVon.getDisplay() && evt.getPropertyName().equals("date")) {
      wdfBis.setMinimumValue(wdfVon.getDate());
    }
    else if (evt.getSource() == wdfBis.getDisplay() &&
             evt.getPropertyName().equals("date")) {
      wdfVon.setMaximumValue(wdfBis.getDate());
    }
  }


  public String getModul() {
    return EingangsrechnungReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ZAHLUNGSJOURNAL;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    int iSortierung;
    if (wrbBankAuszug.isSelected()) {
      iSortierung = EingangsrechnungReportFac.REPORT_ZAHLUNGEN_SORT_BANK_AUSZUG;
    }
    else if (wrbRechnungsnummer.isSelected()) {
      iSortierung = EingangsrechnungReportFac.REPORT_ZAHLUNGEN_SORT_RECHNUNGSNUMMER;
    }
    else {
      iSortierung = EingangsrechnungReportFac.REPORT_ZAHLUNGEN_SORT_ZAHLUNGSAUSGANG;
    }
    return DelegateFactory.getInstance().getEingangsrechnungDelegate().printZahlungsjournal(
        iSortierung, wdfVon.getDate(), wdfBis.getDate(),tabbedPaneEingangsrechnung.isBZusatzkosten());
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }



  public MailtextDto getMailtextDto() throws Throwable  {
    MailtextDto mailtextDto=PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wrbZahlungsausgang;
  }
}