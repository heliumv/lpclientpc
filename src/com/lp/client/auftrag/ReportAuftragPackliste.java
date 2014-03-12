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
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Report Auftrag Packliste.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 28.09.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.11 $
 */
public class ReportAuftragPackliste extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer auftragIId = null;
	private WrapperComboBox wcbDruckformular = new WrapperComboBox();
	private WrapperLabel wlaDruckformular = new WrapperLabel();
	protected JPanel jpaWorkingOn = null;

	private WrapperComboBox wcbArtikelgruppe = new WrapperComboBox();

	public ReportAuftragPackliste(InternalFrame internalFrame,
			Integer iIdAuftragI, String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
		jbInit();
		initComponents();
		auftragIId = iIdAuftragI;
	}

	private void jbInit() throws Throwable {
		jpaWorkingOn = new JPanel(new GridBagLayout());
		this.setLayout(new GridBagLayout());
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// PJ 09/13917

		ArtgruDto[] artgruDtos = DelegateFactory.getInstance()
				.getArtikelDelegate().artgruFindAll();
		String sAnzeige = null;

		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();

		iZeile++;
		for (int i = 0; i < artgruDtos.length; i++) {
			
			tmArten.put(artgruDtos[i].getCNr().trim(), artgruDtos[i].getCNr().trim());

		}

		wcbArtikelgruppe.setMap(tmArten);
		
		jpaWorkingOn.add(wcbArtikelgruppe, new GridBagConstraints(0, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		wlaDruckformular.setText(LPMain
				.getTextRespectUISPr("auftrag.report.packliste.reportauswahl"));
		wcbDruckformular.setMandatoryField(true);
		wcbDruckformular.setVisible(true);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE4, LPMain
				.getTextRespectUISPr("auftrag.report.packliste.packliste4"));
		map.put(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE3, LPMain
				.getTextRespectUISPr("auftrag.report.packliste.packliste3"));
		map
				.put(
						AuftragReportFac.REPORT_AUFTRAG_PACKLISTE,
						LPMain
								.getTextRespectUISPr("auftrag.report.packliste.standardreport"));
		wcbDruckformular.setMap(map);
		wcbDruckformular
				.setKeyOfSelectedItem(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE);
		wcbDruckformular.setMinimumSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wcbDruckformular.setPreferredSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		jpaWorkingOn.add(wcbDruckformular, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		wlaDruckformular.setMinimumSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wlaDruckformular.setPreferredSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));

		jpaWorkingOn.add(wlaDruckformular, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	public String getReportname() {

		String sReportname = AuftragReportFac.REPORT_AUFTRAG_PACKLISTE;
		Object oKeySelected = wcbDruckformular.getKeyOfSelectedItem();
		if (oKeySelected != null
				&& oKeySelected
						.equals(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE3)) {
			sReportname = AuftragReportFac.REPORT_AUFTRAG_PACKLISTE3;
		} else if (oKeySelected != null
				&& oKeySelected
						.equals(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE4)) {
			sReportname = AuftragReportFac.REPORT_AUFTRAG_PACKLISTE4;
		}
		// PJ 09/13917

		if (wcbArtikelgruppe.getKeyOfSelectedItem() != null) {
			sReportname = sReportname
					.substring(0, sReportname.lastIndexOf("."));
			sReportname = sReportname + "_"
					+ wcbArtikelgruppe.getKeyOfSelectedItem();
			sReportname += ".jasper";
		}

		return sReportname;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory.getInstance().getAuftragReportDelegate()
				.printAuftragPackliste(auftragIId, getReportname());
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		if (auftragIId != null) {
			
			
			AuftragDto auftragDto=DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(auftragIId);
			
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
			.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());
			
			Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			mailtextDto.setMailPartnerIId(kundeDto.getPartnerIId());
			mailtextDto.setMailAnprechpartnerIId(auftragDto
					.getAnsprechparnterIId());
			 PersonalDto personalDtoBearbeiter = DelegateFactory.getInstance().getPersonalDelegate().
	          personalFindByPrimaryKey(auftragDto.getPersonalIIdVertreter());
	      mailtextDto.setMailVertreter(personalDtoBearbeiter);
	      
			mailtextDto.setMailBelegdatum(new java.sql.Date(auftragDto
					.getTBelegdatum().getTime()));
			mailtextDto.setMailBelegnummer(auftragDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain
					.getTextRespectSpezifischesLocale("auft.mailbezeichnung",locKunde));
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

}
