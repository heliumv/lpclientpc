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
package com.lp.client.reklamation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportEtikett;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.reklamation.service.ReklamationReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportReklamationUnterartKundeLieferant extends PanelBasis
		implements PanelReportIfJRDS {
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private ButtonGroup buttonGroup = new ButtonGroup();
	WrapperRadioButton wrbKunde = new WrapperRadioButton();
	WrapperRadioButton wrbLieferant = new WrapperRadioButton();

	private static final long serialVersionUID = 1L;
	private Integer reklamationIId = null;

	public ReportReklamationUnterartKundeLieferant(
			InternalFrameReklamation internalFrame, String add2Title,
			Integer reklamationIId) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		initComponents();
		this.reklamationIId = reklamationIId;
	}

	private void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wrbLieferant.setText(LPMain.getTextRespectUISPr("label.lieferant"));
		wrbKunde.setText(LPMain.getTextRespectUISPr("label.kunde"));

		buttonGroup.add(wrbKunde);
		buttonGroup.add(wrbLieferant);
		wrbKunde.setSelected(true);

		

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("rekla.unterart")), new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbKunde, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbLieferant, new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

	}

	public String getModul() {
		return ReklamationReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		if (wrbKunde.isSelected()) {
			return ReklamationReportFac.REPORT_REKLAMATION;
		} else {
			return ReklamationReportFac.REPORT_REKLAMATION_LIEFERANT;
		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory.getInstance().getReklamationReportDelegate()
				.printReklamation(reklamationIId, wrbLieferant.isSelected());
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		if (reklamationIId != null) {

			ReklamationDto reklamationDto = DelegateFactory.getInstance()
					.getReklamationDelegate()
					.reklamationFindByPrimaryKey(reklamationIId);

			PartnerDto partnerDto = null;
			Integer ansprechprtnerIId = null;
			if (reklamationDto.getReklamationartCNr().equals(
					ReklamationFac.REKLAMATIONART_KUNDE)) {

				partnerDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(reklamationDto.getKundeIId())
						.getPartnerDto();

				ansprechprtnerIId = reklamationDto.getAnsprechpartnerIId();

			}

			if (partnerDto != null) {

				Locale locKunde = Helper.string2Locale(partnerDto
						.getLocaleCNrKommunikation());
				mailtextDto.setMailPartnerIId(partnerDto.getIId());
				mailtextDto.setMailAnprechpartnerIId(ansprechprtnerIId);

				
				mailtextDto.setRekla_kndlsnr(reklamationDto.getCKdlsnr());
				mailtextDto.setRekla_kndreklanr(reklamationDto.getCKdreklanr());
				if (reklamationDto.getLieferscheinIId() != null) {
					mailtextDto.setRekla_lieferschein(DelegateFactory
							.getInstance()
							.getLsDelegate()
							.lieferscheinFindByPrimaryKey(
									reklamationDto.getLieferscheinIId())
							.getCNr());
				}
				if (reklamationDto.getRechnungIId() != null) {
					mailtextDto.setRekla_rechnung(DelegateFactory
							.getInstance()
							.getRechnungDelegate()
							.rechnungFindByPrimaryKey(
									reklamationDto.getRechnungIId()).getCNr());
				}

				
					if (reklamationDto.getWareneingangIId() != null) {

						WareneingangDto weDto = DelegateFactory
								.getInstance()
								.getWareneingangDelegate()
								.wareneingangFindByPrimaryKey(
										reklamationDto.getWareneingangIId());

						mailtextDto
								.setRekla_we_lsnr(weDto.getCLieferscheinnr());

						mailtextDto.setRekla_we_lsdatum(Helper.formatDatum(
								weDto.getTLieferscheindatum(), LPMain
										.getTheClient().getLocUi()));
						mailtextDto.setRekla_we_datum(Helper.formatDatum(weDto
								.getTWareneingangsdatum(), LPMain
								.getTheClient().getLocUi()));

					}
				

				
				
				
				PersonalDto personalDtoBearbeiter = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								reklamationDto.getPersonalIIdAufnehmer());
				mailtextDto.setMailVertreter(personalDtoBearbeiter);
				mailtextDto.setMailBelegdatum(Helper.extractDate(reklamationDto
						.getTBelegdatum()));
				mailtextDto.setMailBelegnummer(reklamationDto.getCNr());
				mailtextDto.setMailBezeichnung(LPMain
						.getTextRespectSpezifischesLocale(
								"rekla.mailbezeichnung", locKunde));

				mailtextDto.setMailProjekt(reklamationDto.getCProjekt());

				mailtextDto.setMailText(null);
				mailtextDto.setParamLocale(locKunde);
			}
		}
		return mailtextDto;
	}
}
