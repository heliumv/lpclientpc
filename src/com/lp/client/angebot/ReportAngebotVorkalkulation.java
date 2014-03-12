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
package com.lp.client.angebot;


import java.util.Locale;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Report Angebot.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 20.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.5 $
 */
public class ReportAngebotVorkalkulation
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private AngebotDto angebotDto = null;
  private KundeDto kundeDto = null;

  public ReportAngebotVorkalkulation(
      InternalFrame internalFrame,
      Integer iIdAngebotI,
      String add2Title)
      throws Throwable {
    super(
        internalFrame,
        add2Title);

    angebotDto = DelegateFactory.getInstance().getAngebotDelegate().
        angebotFindByPrimaryKey(
            iIdAngebotI);

    kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(
        angebotDto.getKundeIIdAngebotsadresse());
  }


  public String getModul() {
    return AngebotReportFac.REPORT_MODUL;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    JasperPrintLP jasperPrint = DelegateFactory.getInstance().getAngebotReportDelegate().
        printAngebotVorkalkulation(angebotDto.getIId());
    return jasperPrint;
  }


  public boolean getBErstelleReportSofort() {
    return true;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);

    if (angebotDto != null) {
      Locale locKunde =
          Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());

      mailtextDto.setMailPartnerIId(kundeDto.getPartnerIId());
      mailtextDto.setMailAnprechpartnerIId(angebotDto.getAnsprechpartnerIIdKunde());
      // MB 13.07.06 IMS 2104
      PersonalDto personalDtoBearbeiter = DelegateFactory.getInstance().
          getPersonalDelegate().
          personalFindByPrimaryKey(angebotDto.getPersonalIIdVertreter());
      mailtextDto.setMailVertreter(personalDtoBearbeiter);
      mailtextDto.setMailBelegdatum(Helper.extractDate(angebotDto.getTBelegdatum()));
      mailtextDto.setMailBelegnummer(angebotDto.getCNr());
      mailtextDto.setMailBezeichnung(LPMain.getInstance().getTextRespectUISPr(
          "angb.mailbezeichnung"));
      // MB 13.07.06 IMS 2104
      mailtextDto.setMailProjekt(angebotDto.getCBez());
      mailtextDto.setMailFusstext(angebotDto.getXFusstextuebersteuert());
      mailtextDto.setMailText(null); // UW: kommt noch
      mailtextDto.setParamLocale(locKunde);
    }

    return mailtextDto;
  }


  public String getReportname() {
    return AngebotReportFac.REPORT_VORKALKULATION;
  }
}
