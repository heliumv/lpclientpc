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
package com.lp.client.zeiterfassung;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


public class ReportSondertaetigkeitenliste
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private InternalFrameZeiterfassung internalFrameZeiterfassung = null;

  public ReportSondertaetigkeitenliste(InternalFrameZeiterfassung internalFrame,
                                       String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance();
	LPMain.getTextRespectUISPr(
        "zeiterfassung.report.sondertaetigkeitenliste");

    internalFrameZeiterfassung = internalFrame;
    jbInit();
    initComponents();
  }
  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return NO_VALUE_THATS_OK_JCOMPONENT;
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

  }


  public String getModul() {
    return ZeiterfassungFac.REPORT_MODUL;
  }


  public String getReportname() {
    return ZeiterfassungFac.REPORT_SONDERTAETIKGEITEN;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {

    return DelegateFactory.getInstance().getZeiterfassungDelegate().
        printSondertaetigkeiten();

  }


  public boolean getBErstelleReportSofort() {
    return true;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }
}
