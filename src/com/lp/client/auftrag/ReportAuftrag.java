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

import java.sql.Timestamp;
import java.util.Locale;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Report Auftragbestaetigung + Vorkalkulation.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 29.06.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.13 $
 */
public class ReportAuftrag extends ReportBeleg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AuftragDto auftragDto = null;
	private KundeDto kundeDto = null;

	public ReportAuftrag(InternalFrame internalFrame,
			PanelBasis panelToRefresh, AuftragDto auftragDtoI, String add2Title)
			throws Throwable {
		super(internalFrame, panelToRefresh, add2Title,
				LocaleFac.BELEGART_AUFTRAG, auftragDtoI.getIId(), auftragDtoI
						.getKostIId());

		auftragDto = auftragDtoI;

		kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());

		// UW 19.04.06 der Kunde definiert keine Kopienanzahl fuer den Auftrag
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUFTRAG;
	}

	public String getReportname() {
		return AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG;
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		JasperPrintLP[] aJasperPrint = DelegateFactory
				.getInstance()
				.getAuftragReportDelegate()
				.printAuftragbestaetigung(auftragDto.getIId(),
						wnfKopien.getInteger(),
						new Boolean(this.isBPrintLogo()), getReportname());

		// Original und Kopien hintereinanderhaengen
		jasperPrint = aJasperPrint[0];

		for (int i = 1; i < aJasperPrint.length; i++) {
			jasperPrint = Helper.addReport2Report(jasperPrint,
					aJasperPrint[i].getPrint());
		}

		return jasperPrint;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		if (auftragDto != null) {
			Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			mailtextDto.setMailPartnerIId(kundeDto.getPartnerIId());
			mailtextDto.setMailAnprechpartnerIId(auftragDto
					.getAnsprechparnterIId());
			PersonalDto personalDtoBearbeiter = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							auftragDto.getPersonalIIdVertreter());
			mailtextDto.setMailVertreter(personalDtoBearbeiter);

			mailtextDto.setMailBelegdatum(new java.sql.Date(auftragDto
					.getTBelegdatum().getTime()));
			mailtextDto.setMailBelegnummer(auftragDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain
					.getTextRespectSpezifischesLocale("auft.mailbezeichnung",
							locKunde));
			mailtextDto.setMailProjekt(auftragDto.getCBezProjektbezeichnung());
			mailtextDto.setKundenbestellnummer(auftragDto.getCBestellnummer());
			/**
			 * @todo die restlichen Felder befuellen
			 */
			mailtextDto.setMailFusstext(null); // UW: kommt noch
			mailtextDto.setMailText(null); // UW: kommt noch
			mailtextDto.setParamLocale(locKunde);
		}
		return mailtextDto;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		DelegateFactory.getInstance().getAuftragDelegate().aktiviereBelegControlled(auftragDto.getIId(), t);
	}
	
	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		return DelegateFactory.getInstance().getAuftragDelegate().berechneBelegControlled(auftragDto.getIId());
	}
}
