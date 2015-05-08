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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


public class ReportErfassteZollimportpapiere
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


  private GridBagLayout gridBagLayout1 = new GridBagLayout();
 private Border border1;

  private WrapperLabel wlaVon = new WrapperLabel();
  private WrapperLabel wlaBis = new WrapperLabel();
 
  private WrapperDateField wdfVon = new WrapperDateField();
  private WrapperDateField wdfBis = new WrapperDateField();
 private JPanel jpaWorkingOn = new JPanel();
 
  
  private WrapperDateRangeController wdrBereich = null;
  
  public ReportErfassteZollimportpapiere(TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung, String add2Title)
      throws Throwable {
    super(tabbedPaneEingangsrechnung.getInternalFrame(), add2Title);
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
 
    // mit Vormonat vorbesetzen
    wdrBereich.doClickUp();
    wdrBereich.doClickDown();
  }


  private void jbInit()
      throws Throwable {
    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setLayout(gridBagLayout1);
    
    wlaVon.setText("Datum: Von");
    wlaBis.setText("Bis");
  
    wlaVon.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaVon.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    jpaWorkingOn.setLayout(gridBagLayout1);
    jpaWorkingOn.setBorder(border1);
  

    wdfVon.setMandatoryField(true);
    wdfBis.setMandatoryField(true);

    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

    //wegen dialogFLR
    getInternalFrame().addItemChangedListener(this);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  
   
   

    jpaWorkingOn.add(wlaVon,
                 new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                        50, 0));
    jpaWorkingOn.add(wdfVon,
                 new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                        0, 0));
    jpaWorkingOn.add(wlaBis,
                 new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                        0, 0));
    jpaWorkingOn.add(wdfBis,
                 new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                        40, 0));
    jpaWorkingOn.add(wdrBereich,
                 new GridBagConstraints(4, iZeile, 1, 1, 1.0, 0.0,
                                        GridBagConstraints.WEST,
                                        GridBagConstraints.VERTICAL,
                                        new Insets(2, 2, 2, 2), 0, 0));
   
    iZeile++;
    
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
   
  }




 


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      
    }
  }


 


  public String getModul() {
    return EingangsrechnungReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ERFASSTE_ZOLLPAPIERE;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
   
 
    return DelegateFactory.getInstance().getEingangsrechnungDelegate().printErfassteZollpapiere(wdfVon.getDate(), wdfBis.getDate());
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
    return wdfVon;
  }

}
