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
package com.lp.client.partner;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


public class ReportEmpfaengerliste
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = new JPanel(new GridBagLayout());
	private Integer serienbriefIId =null;

  public ReportEmpfaengerliste(InternalFrame internalFrame ,Integer serienbriefIId, String sAdd2Title)
      throws Throwable {
    super(internalFrame, sAdd2Title);
    this.serienbriefIId=serienbriefIId;
    jbInit();
    setDefaults();
  }


  private void setDefaults() {

  }


  private void jbInit()
  throws Throwable {
	  this.setLayout(new GridBagLayout());
	 
	  
	  

	  int iZeile = 0;
	  iZeile++;
	  this.add(jpaWorkingOn,
			  new GridBagConstraints(0, 0, 5, 2, 1.0, 1.0, GridBagConstraints.EAST,
					  GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  }


  public String getModul() {
    return KundeReportFac.REPORT_MODUL;
  }


  public String getReportname() {
		  return PartnerReportFac.REPORT_PART_EMPFAENGERLISTE;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {

    return DelegateFactory.getInstance().getKundeDelegate().printEmpfaengerliste(serienbriefIId);
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }
}
