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
package com.lp.client.bestellung;

import java.sql.Timestamp;
import java.util.Locale;

import javax.swing.JComponent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/*
 * <p> Diese Klasse kuemmert sich um den Bestellungsdruck</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 15.06.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/05/16 09:09:42 $
 */
public class ReportBestellung extends ReportBeleg {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private BestellungDto bsDto = null;
	private LieferantDto lfDto = null;

	public ReportBestellung(InternalFrame internalFrameI,
			PanelBasis panelToRefresh, BestellungDto bsDtoI, LieferantDto lfDtoI)
			throws Throwable {

		super(internalFrameI, panelToRefresh, "",
				LocaleFac.BELEGART_BESTELLUNG, bsDtoI.getIId(), bsDtoI
						.getKostenstelleIId());

		bsDto = bsDtoI;
		lfDto = lfDtoI;
	}

	public String getModul() {
		return BestellungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return BestellungReportFac.REPORT_BESTELLUNG;
	}

	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}
	// public String getReportname() {
	// return "";
	// }

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		JasperPrintLP[] prints = DelegateFactory
				.getInstance()
				.getBestellungDelegate()
				.printBestellung(bsDto.getIId(), wnfKopien.getInteger(),
						new Boolean(this.isBPrintLogo()));

		// alle zusammenhaengen
		JasperPrintLP print = prints[0];
		for (int i = 1; i < prints.length; i++) {
			print = Helper.addReport2Report(print, prints[i].getPrint());
		}
		return print;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);

		if (bsDto != null) {
			Locale locKunde = Helper.string2Locale(lfDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			mailtextDto.setMailPartnerIId(lfDto.getPartnerIId());
			mailtextDto.setMailAnprechpartnerIId(bsDto.getAnsprechpartnerIId());
			mailtextDto.setMailVertreter(null);
			mailtextDto.setMailBelegdatum(new java.sql.Date(bsDto
					.getDBelegdatum().getTime()));
			mailtextDto.setMailBelegnummer(bsDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain
					.getTextRespectSpezifischesLocale("bes.mailbezeichnung",
							locKunde));
			mailtextDto.setMailProjekt(bsDto.getCBez());
			/**
			 * @todo die restlichen Felder befuellen
			 */
			mailtextDto.setMailFusstext(null); // UW: kommt noch
			mailtextDto.setMailText(null); // UW: kommt noch

			mailtextDto.setParamLocale(locKunde);
		}

		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		return DelegateFactory.getInstance().getBestellungDelegate().berechneBelegControlled(bsDto.getIId());
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {

		DelegateFactory.getInstance().getBestellungDelegate().aktiviereBelegControlled(bsDto.getIId(), t);

		bsDto = DelegateFactory.getInstance().getBestellungDelegate().bestellungFindByPrimaryKey(bsDto.getIId());

		((InternalFrameBestellung) getInternalFrame()).getTabbedPaneBestellung().setBestellungDto(bsDto);

	}

}
