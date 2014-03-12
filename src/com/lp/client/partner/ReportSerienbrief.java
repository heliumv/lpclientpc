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


import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportAllgemeinIfJRDS;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


/**
 * <p> Diese Klasse kuemmert sich um einen Serienbrief.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 02.12.05</p>
 *
 * <p>@author $Author: sebastian $</p>
 *
 * @version not attributable Date $Date: 2009/08/25 13:19:50 $
 */
public class ReportSerienbrief
    extends PanelBasis implements PanelReportIfJRDS,PanelReportAllgemeinIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Integer serienbriefIId = null;
  private boolean bPrintLogo = false;

  public ReportSerienbrief(InternalFrame internalFrame,Integer serienbriefIId,
                           String add2Title)
      throws Throwable {

    super(internalFrame, add2Title);

    this.serienbriefIId = serienbriefIId;
  }


  public String getModul() {
    return PartnerReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return PartnerReportFac.REPORT_PART_KURZBRIEF;
  }


  public boolean getBErstelleReportSofort() {
    return true;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    // no e-mail no fax
    return mailtextDto;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {

    return DelegateFactory.getInstance().getKundeDelegate().
    druckeSerienbrief(serienbriefIId,bPrintLogo);
  }

  public boolean isBPrintLogo() {
  return bPrintLogo;
}


public void setBPrintLogo(boolean bPrintLogo) {
  this.bPrintLogo = bPrintLogo;
}

}
