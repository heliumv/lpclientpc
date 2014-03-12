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
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access") 
public class ReportEingangsrechnungKontierung
    extends PanelBasis implements PanelReportIfJRDS, PropertyChangeListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final static String ACTION_SPECIAL_KOSTENSTELLE_ALLE =
      "action_special_kostenstelle_alle";
  private final static String ACTION_SPECIAL_KOSTENSTELLE_EINE =
      "action_special_kostenstelle_eine";
  private final static String ACTION_SPECIAL_KOSTENSTELLE_AUSWAHL =
      "action_special_kostenstelle_auswahl";

  private final static int BREITE_SPALTE2 = 80;
  private final static int BREITE_BUTTONS = 105;

  private PanelQueryFLR panelQueryFLRKostenstelle = null;

  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private WrapperLabel wlaKostenstellen = new WrapperLabel();
  private Border border1;
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private WrapperButton wbuKostenstelle = new WrapperButton();
  private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
  private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
  private ButtonGroup buttonGroupKostenstelle = new ButtonGroup();
  private ButtonGroup buttonGroupEingangsrechnung = new ButtonGroup();
  private ButtonGroup buttonGroupDatum = new ButtonGroup();
  private WrapperLabel wlaVon = new WrapperLabel();
  private WrapperLabel wlaBis = new WrapperLabel();
  private KostenstelleDto kostenstelleDto = null;
  private WrapperDateField wdfVon = new WrapperDateField();
  private WrapperDateField wdfBis = new WrapperDateField();
  private WrapperRadioButton wrbKostenstelleAlle = new WrapperRadioButton();
  private WrapperRadioButton wrbKostenstelleEine = new WrapperRadioButton();
  private JPanel jpaWorkingOn = new JPanel();
  private WrapperRadioButton wrbErOffene = new WrapperRadioButton();
  private WrapperRadioButton wrbErBezahlte = new WrapperRadioButton();
  private WrapperRadioButton wrbErAlle = new WrapperRadioButton();
  private WrapperRadioButton wrbFreigabedatum = new WrapperRadioButton();
  private WrapperRadioButton wrbRechnungsdatum = new WrapperRadioButton();
  private WrapperLabel wlaEr = new WrapperLabel();
  private WrapperLabel wlaDatum = new WrapperLabel();
  private WrapperDateRangeController wdrBereich = null;

	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
  
  public ReportEingangsrechnungKontierung(TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung, String add2Title)
      throws Throwable {
    super(tabbedPaneEingangsrechnung.getInternalFrame(), add2Title);
    this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
    jbInit();
    initPanel();
    setDefaults();
    initComponents();
  }


  private void initPanel()
      throws Throwable {
  }


  /**
   * setDefaults
   */
  private void setDefaults() {
    setVisibilityKostenstelle(true);
    wrbErOffene.setSelected(true);
    wrbKostenstelleAlle.setSelected(true);
    wrbRechnungsdatum.setSelected(true);
    // mit Vormonat vorbesetzen
    wdrBereich.doClickUp();
    wdrBereich.doClickDown();
  }


  private void jbInit()
      throws Throwable {
    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setLayout(gridBagLayout1);
    wlaEr.setText("Eingangsrechnungen");
    wlaEr.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaEr.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wrbErAlle.setText("Alle");
    wrbErBezahlte.setText("Bezahlte");
    wrbErOffene.setText("Offene");
    wlaDatum.setText("Auswertung nach");
    wlaDatum.setHorizontalAlignment(SwingConstants.LEFT);
    wrbFreigabedatum.setText("Freigabedatum");
    wrbRechnungsdatum.setText("Rechnungsdatum");
    wlaEr.setHorizontalAlignment(SwingConstants.LEFT);
    wlaKostenstellen.setHorizontalAlignment(SwingConstants.LEFT);
    wlaKostenstellen.setText(LPMain.getInstance().getTextRespectUISPr(
        "label.kostenstelle"));
    wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle"));
    wbuKostenstelle.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle.tooltip"));
    wrbKostenstelleAlle.setMinimumSize(new Dimension(BREITE_SPALTE2,
        Defaults.getInstance().getControlHeight()));
    wrbKostenstelleAlle.setPreferredSize(new Dimension(BREITE_SPALTE2,
        Defaults.getInstance().getControlHeight()));

    wbuKostenstelle.setMinimumSize(new Dimension(BREITE_BUTTONS,
                                                 Defaults.getInstance().getControlHeight()));
    wbuKostenstelle.setPreferredSize(new Dimension(BREITE_BUTTONS,
        Defaults.getInstance().getControlHeight()));
    wrbErOffene.setMinimumSize(new Dimension(100,
                                             Defaults.getInstance().getControlHeight()));
    wrbErOffene.setPreferredSize(new Dimension(100,
                                               Defaults.getInstance().getControlHeight()));
    wtfKostenstelleNummer.setMinimumSize(new Dimension(50,
        Defaults.getInstance().getControlHeight()));
    wtfKostenstelleNummer.setPreferredSize(new Dimension(50,
        Defaults.getInstance().getControlHeight()));
    jpaWorkingOn.setLayout(gridBagLayout2);
    wrbKostenstelleAlle.setSelected(true);
    wrbKostenstelleAlle.setText("Alle");
    wrbKostenstelleEine.setText("Eine");
    wlaVon.setText("Von");
    wlaBis.setText("Bis");
    wrbKostenstelleAlle.setMinimumSize(new Dimension(50,
        Defaults.getInstance().getControlHeight()));
    wrbKostenstelleAlle.setPreferredSize(new Dimension(50,
        Defaults.getInstance().getControlHeight()));
    wlaVon.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaVon.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    jpaWorkingOn.setBorder(border1);
    wtfKostenstelleBezeichnung.setActivatable(false);
    wtfKostenstelleNummer.setActivatable(false);
    wtfKostenstelleBezeichnung.setEditable(false);
    wtfKostenstelleNummer.setEditable(false);

    wrbKostenstelleAlle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE_ALLE);
    wrbKostenstelleEine.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE_EINE);
    wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE_AUSWAHL);

    wrbKostenstelleAlle.addActionListener(this);
    wrbKostenstelleEine.addActionListener(this);
    wbuKostenstelle.addActionListener(this);

    wdfVon.setMandatoryField(true);
    wdfBis.setMandatoryField(true);

    wdfVon.getDisplay().addPropertyChangeListener(this);
    wdfBis.getDisplay().addPropertyChangeListener(this);
    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

    //wegen dialogFLR
    getInternalFrame().addItemChangedListener(this);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaEr,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wrbErOffene,
                     new GridBagConstraints(1, iZeile, 4, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbErBezahlte,
                     new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbErAlle,
                     new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaKostenstellen,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wrbKostenstelleAlle,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbKostenstelleEine,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wbuKostenstelle,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfKostenstelleNummer,
                     new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfKostenstelleBezeichnung,
                     new GridBagConstraints(4, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaDatum,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wrbRechnungsdatum,
                     new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbFreigabedatum,
                     new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    JPanel jpaDatum = new JPanel(new GridBagLayout());

    jpaDatum.add(wlaVon,
                 new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                        0, 0));
    jpaDatum.add(wdfVon,
                 new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                        0, 0));
    jpaDatum.add(wlaBis,
                 new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                        0, 0));
    jpaDatum.add(wdfBis,
                 new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                        0, 0));
    jpaDatum.add(wdrBereich,
                 new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.CENTER,
                                        GridBagConstraints.VERTICAL,
                                        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(jpaDatum,
                     new GridBagConstraints(0, iZeile, 4, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.NONE,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    buttonGroupKostenstelle.add(wrbKostenstelleAlle);
    buttonGroupKostenstelle.add(wrbKostenstelleEine);
    buttonGroupEingangsrechnung.add(wrbErAlle);
    buttonGroupEingangsrechnung.add(wrbErBezahlte);
    buttonGroupEingangsrechnung.add(wrbErOffene);
    buttonGroupDatum.add(wrbFreigabedatum);
    buttonGroupDatum.add(wrbRechnungsdatum);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(this.ACTION_SPECIAL_KOSTENSTELLE_ALLE)) {
      setVisibilityKostenstelle(true);
    }
    else if (e.getActionCommand().equals(this.ACTION_SPECIAL_KOSTENSTELLE_EINE)) {
      setVisibilityKostenstelle(false);
      // wenn noch keine gewaehlt, dann geht der dialog auf
      if (kostenstelleDto == null) {
        wbuKostenstelle.doClick();
      }
    }
    else if (e.getActionCommand().equals(this.ACTION_SPECIAL_KOSTENSTELLE_AUSWAHL)) {
      dialogQueryKostenstelle();
    }
  }


  private void setVisibilityKostenstelle(boolean alle) {
    wbuKostenstelle.setVisible(!alle);
    wtfKostenstelleNummer.setVisible(!alle);
    wtfKostenstelleNummer.setMandatoryField(!alle);
    wtfKostenstelleBezeichnung.setVisible(!alle);
  }


  private void dialogQueryKostenstelle()
      throws Throwable {
    panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().
        createPanelFLRKostenstelle(getInternalFrame(), false, false);
    if (kostenstelleDto != null) {
      panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
    }
    new DialogQuery(panelQueryFLRKostenstelle);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRKostenstelle) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        if (key != null) {
          try {
            kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
                kostenstelleFindByPrimaryKey( (Integer) key);
            dto2ComponentsKostenstelle();
          }
          catch (Throwable ex) {
            /**
             * @todo MB  PJ 5148
             */
          }
        }
      }
    }
  }


  /**
   * Traegt die Daten fuer die Kostenstelle ein.
   */
  private void dto2ComponentsKostenstelle() {
    if (kostenstelleDto != null) {
      wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
      wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
    }
    else {
      wtfKostenstelleNummer.setText(null);
      wtfKostenstelleBezeichnung.setText(null);
    }
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
    return EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_KONTIERUNG;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    int iFilter;
    if (wrbErOffene.isSelected()) {
      iFilter = EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_OFFENE;
    }
    else if (wrbErBezahlte.isSelected()) {
      iFilter = EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_BEZAHLT;
    }
    else {
      iFilter = EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_ALLE;
    }
    Integer kostenstelleIId = null;
    if (wrbKostenstelleEine.isSelected() && kostenstelleDto != null) {
      kostenstelleIId = kostenstelleDto.getIId();
    }
    int iKritDatum;
    if (wrbRechnungsdatum.isSelected()) {
      iKritDatum = EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_BELEG;
    }
    else {
      iKritDatum = EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE;
    }
    return DelegateFactory.getInstance().getEingangsrechnungDelegate().printKontierungsjournal(
        iFilter, kostenstelleIId, iKritDatum, wdfVon.getDate(), wdfBis.getDate(),tabbedPaneEingangsrechnung.isBZusatzkosten());
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wrbErOffene;
  }

}
