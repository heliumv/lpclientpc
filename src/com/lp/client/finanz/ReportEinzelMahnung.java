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
package com.lp.client.finanz;

import java.sql.Timestamp;
import java.util.Locale;

import javax.swing.JComponent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um den Druck des Buchungsjournals</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 07.09.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/05/16 09:09:42 $
 */
public class ReportEinzelMahnung extends ReportBeleg implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer mahnungIId = null;
	private Boolean bAusfuerlich = null;
	private MahnungDto mahnungDto = null;
	private RechnungDto rechnungDto = null;
	private KundeDto kundeDto = null;

	public ReportEinzelMahnung(InternalFrame internalFrame, PanelBasis panelToRefresh, Integer mahnungIId,
			String sAdd2Title) throws Throwable {
		super(internalFrame, panelToRefresh, sAdd2Title, null, null, null);
		this.mahnungIId = mahnungIId;
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_RECHNUNG,
						ParameterFac.PARAMETER_AUSFUEHRLICHER_MAHNUNGSDRUCK_AR);
		bAusfuerlich = (Boolean) parameter.getCWertAsObject();
		mahnungDto = DelegateFactory.getInstance().getMahnwesenDelegate()
				.mahnungFindByPrimaryKey(mahnungIId);
		rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
				.rechnungFindByPrimaryKey(mahnungDto.getRechnungIId());
		kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(rechnungDto.getKundeIId());
		this.setVisible(false);
	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		if (bAusfuerlich) {
			return FinanzReportFac.REPORT_MAHNUNG_AUSFUEHRLICH;
		} else {
			return FinanzReportFac.REPORT_MAHNUNG;
		}
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		// lock ist hier egal
		return null;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		if (bAusfuerlich) {
			Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			JasperPrintLP print = DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.printRechnungAlsMahnung(rechnungDto.getIId(),
							mahnungDto.getMahnstufeIId(), locKunde,
							isBPrintLogo());
			return print;
		} else {
			return DelegateFactory.getInstance().getFinanzReportDelegate()
					.printMahnungAusMahnlauf(mahnungIId, isBPrintLogo());
		}
	}

	// public MailtextDto getMailtextDto() throws Throwable {
	// MailtextDto mailtextDto=PanelReportKriterien.getDefaultMailtextDto(this);
	// return mailtextDto;
	// }

	public MailtextDto getMailtextDto() throws Throwable {
		// report_email: 2 Das Default-Mailtext-Dto holen
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		if (rechnungDto != null && kundeDto != null) {
			// report_email: 3 reportspezifische Parameter
			Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			mailtextDto.setMailBetreff(mahnungDto.getMahnstufeIId() + "."
					+ LPMain.getTextRespectUISPr("rech.mailbetreff.mahnung")
					+ " " + rechnungDto.getCNr());
			mailtextDto.setMailAnprechpartnerIId(rechnungDto
					.getAnsprechpartnerIId());
			mailtextDto.setMailVertreter(null);
			mailtextDto.setMailBelegdatum(new java.sql.Date(mahnungDto
					.getTMahndatum().getTime()));
			mailtextDto.setMailBelegnummer(rechnungDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain
					.getTextRespectUISPr("rech.mailbezeichnung.mahnung"));
			mailtextDto.setMailFusstext(rechnungDto.getCFusstextuebersteuert());
			mailtextDto.setMailPartnerIId(kundeDto.getPartnerIId());
			mailtextDto.setMailProjekt(null);
			mailtextDto.setMailText(null);
			mailtextDto.setParamLocale(locKunde);
		}
		return mailtextDto;
	}

	public boolean handleOwnException(ExceptionLP ex) throws Throwable {
		if (ex.getICode() == EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN) {
			String sFehler = LPMain.getInstance().getMsg(ex);
			if (ex.getAlInfoForTheClient() != null
					&& !ex.getAlInfoForTheClient().isEmpty()) {
				sFehler += ": " + ex.getAlInfoForTheClient().get(0);
			}
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), sFehler);
			return true;
		} else {
			return false;
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		DelegateFactory.getInstance().getMahnwesenDelegate()
				.mahneMahnung(mahnungIId);
		return null;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		// wurde schon aktiviert
	}
	
}
