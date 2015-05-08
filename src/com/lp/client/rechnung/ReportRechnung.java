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
package com.lp.client.rechnung;

import java.sql.Timestamp;
import java.util.Locale;

import javax.swing.JOptionPane;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Rechnungsdruck
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Martin Bluehweis; 15.06.05
 * </p>
 *
 * <p>
 *
 * @author $Author: christian $
 *         </p>
 *
 * @version not attributable Date $Date: 2012/05/16 09:09:42 $
 */
public class ReportRechnung extends ReportBeleg {

	private static final long serialVersionUID = 1L;
	private RechnungDto rechnungDto = null;
	private KundeDto kundeDto = null;

	public ReportRechnung(InternalFrame internalFrame, PanelBasis panelToRefresh, RechnungDto rechnungDto,
			KundeDto kundeDto, String sAdd2Title) throws Throwable {
		// reporttitel: so gehts fuer Belege
		super(internalFrame, panelToRefresh, sAdd2Title, LocaleFac.BELEGART_RECHNUNG,
				rechnungDto.getIId(), rechnungDto.getKostenstelleIId());
		this.rechnungDto = rechnungDto;
		this.kundeDto = kundeDto;
		// vorbesetzen
		if (rechnungDto != null) {
			super.wnfKopien.setInteger(kundeDto.getIDefaultrekopiendrucken());
		}
	}

	public String getModul() {
		return RechnungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return RechnungReportFac.REPORT_RECHNUNG;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto()
				.getLocaleCNrKommunikation());
		// belegkopien: 5 den Report (Original + Kopien am Server erzeugen)
		JasperPrintLP[] prints = DelegateFactory
				.getInstance()
				.getRechnungDelegate()
				.printRechnung(rechnungDto.getIId(), locKunde,
						new Boolean(this.isBPrintLogo()),
						wnfKopien.getInteger());
		JasperPrintLP print = prints[0];
		for (int i = 1; i < prints.length; i++) {
			print = Helper.addReport2Report(print, prints[i].getPrint());
		}
		return print;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		// report_email: 2 Das Default-Mailtext-Dto holen
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		if (rechnungDto != null && kundeDto != null) {
			// report_email: 3 reportspezifische Parameter
			Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			mailtextDto.setMailAnprechpartnerIId(rechnungDto
					.getAnsprechpartnerIId());
			PersonalDto personalDtoBearbeiter = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							rechnungDto.getPersonalIIdVertreter());
			mailtextDto.setMailVertreter(personalDtoBearbeiter);
			mailtextDto.setMailBelegdatum(new java.sql.Date(rechnungDto
					.getTBelegdatum().getTime()));
			mailtextDto.setMailBelegnummer(rechnungDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain
					.getTextRespectSpezifischesLocale("rech.mailbezeichnung",
							locKunde));
			mailtextDto.setMailFusstext(rechnungDto.getCFusstextuebersteuert());
			mailtextDto.setMailPartnerIId(kundeDto.getPartnerIId());
			mailtextDto.setMailProjekt(null);
			mailtextDto.setMailText(null);
			mailtextDto.setParamLocale(locKunde);
		}
		return mailtextDto;
	}

	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_RECHNUNG;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		String statusCNrOld = rechnungDto.getStatusCNr();
		DelegateFactory.getInstance().getRechnungDelegate()
				.aktiviereBelegControlled(rechnungDto.getIId(), t);
		// SP 2013/001327
		rechnungDto = DelegateFactory.getInstance()
				.getRechnungDelegate().rechnungFindByPrimaryKey(rechnungDto.getIId());

		((InternalFrameRechnung)getInternalFrame()).getTabbedPaneRechnung().setRechnungDto(rechnungDto);

		if (!rechnungDto.getStatusCNr().equals(statusCNrOld)
				&& rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_OFFEN)
				&& rechnungDto.getNWert().signum() == 0) {
			DialogFactory.showMeldung(
					LPMain.getTextRespectUISPr("rech.belegwert0"),
					LPMain.getTextRespectUISPr("lp.achtung"),
					JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
		}
	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		return DelegateFactory.getInstance().getRechnungDelegate()
				.berechneBelegControlled(rechnungDto.getIId());
	}
}
