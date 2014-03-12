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
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um den Druck der Rechnungs-Umsatzuebersicht</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 19.10.05</p>
 *
 * <p>@author $Author: sebastian $</p>
 *
 * @version not attributable Date $Date: 2009/08/25 13:19:49 $
 */
public class ReportRechnungUmsatz extends PanelBasis implements PanelReportIfJRDS {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperCheckBox wcbGutschriftenberuecksichtigen = null;
  private JPanel jpaWorkingOn = null;
  private WrapperComboBox wcoJahr = null;
  private WrapperLabel wlaPeriode = null;


  public ReportRechnungUmsatz(InternalFrame internalFrame, String sAdd2Title)
      throws Throwable {
    super(internalFrame, sAdd2Title);
    jbInit();
    setDefaults();
    initComponents();
  }


  private void setDefaults()
      throws Throwable {
    wcoJahr.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllGeschaeftsjahr());
    // Default ist das aktuelle GJ
    wcoJahr.setKeyOfSelectedItem(DelegateFactory.getInstance().getParameterDelegate().
                                           getGeschaeftsjahr());
    // default inkl. gutschriften
    wcbGutschriftenberuecksichtigen.setSelected(true);
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(new GridBagLayout());
    jpaWorkingOn = new JPanel(new GridBagLayout());
    wcbGutschriftenberuecksichtigen = new WrapperCheckBox();
    wcbGutschriftenberuecksichtigen.setText(LPMain.getInstance().getTextRespectUISPr(
        "rechnung.gutschriftenberuecksichtigen"));
    wlaPeriode = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("label.periode"));
    wlaPeriode.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaPeriode.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wcoJahr = new WrapperComboBox();
    wcoJahr.setMandatoryField(true);
    wcoJahr.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wcoJahr.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaPeriode,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wcoJahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.NONE,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wcbGutschriftenberuecksichtigen,
                     new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
  }


  public String getModul() {
    return RechnungReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return RechnungReportFac.REPORT_RECHNUNGEN_UMSATZ;
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    return DelegateFactory.getInstance().getRechnungDelegate().printRechnungenUmsatz( (
        Integer) wcoJahr.getKeyOfSelectedItem(),
        new Boolean(wcbGutschriftenberuecksichtigen.isSelected()));
  }


  public MailtextDto getMailtextDto() throws Throwable  {
    MailtextDto mailtextDto=PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wcbGutschriftenberuecksichtigen;
  }
}
