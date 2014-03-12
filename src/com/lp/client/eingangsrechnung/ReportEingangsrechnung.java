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
package com.lp.client.eingangsrechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportEingangsrechnung extends ReportBeleg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EingangsrechnungDto erDto;

	private WrapperLabel wlaBetrag;
	private WrapperNumberField wnfBetrag;
	protected int iSchecknummer = -1;
	private WrapperNumberField wnfBetragSkonto1 = new WrapperNumberField();
	private WrapperNumberField wnfBetragSkonto2 = new WrapperNumberField();
	private WrapperButton wbuSkonto1uebernehmen = new WrapperButton();
	private WrapperButton wbuSkonto2uebernehmen = new WrapperButton();
	private WrapperLabel wlaZahlungsziel = new WrapperLabel();
	private WrapperTextField wtfZahlungsziel = new WrapperTextField();

	private WrapperLabel wlaSkonto1 = new WrapperLabel();
	private WrapperNumberField wnfSkontoProzent1 = new WrapperNumberField();
	private WrapperLabel wlaProzent1 = new WrapperLabel();
	private WrapperLabel wlaBis1 = new WrapperLabel();
	private WrapperDateField wdfSkonto1 = new WrapperDateField();

	private WrapperLabel wlaSkonto2 = new WrapperLabel();
	private WrapperNumberField wnfSkontoProzent2 = new WrapperNumberField();
	private WrapperLabel wlaProzent2 = new WrapperLabel();
	private WrapperLabel wlaBis2 = new WrapperLabel();
	private WrapperDateField wdfSkonto2 = new WrapperDateField();
	private WrapperDateField wdfZieldatum = new WrapperDateField();

	private WrapperLabel wlaZusatzText = new WrapperLabel();
	private WrapperTextField wtfZusatztext = new WrapperTextField();

	private WrapperRadioButton wrbInland = new WrapperRadioButton();
	private WrapperRadioButton wrbAusland = new WrapperRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperLabel wlaSchecknummer = new WrapperLabel();
	private WrapperNumberField wnfSchecknummer = new WrapperNumberField();

	private static final String ACTION_SPECIAL_SKONTO1 = "action_special_skonto1";
	private static final String ACTION_SPECIAL_SKONTO2 = "action_special_skonto2";

	public ReportEingangsrechnung(InternalFrame internalFrame,
			EingangsrechnungDto erDto, String sAdd2Title) throws Throwable {
		super(internalFrame, null, sAdd2Title, LocaleFac.BELEGART_EINGANGSRECHNUNG,
				erDto.getIId(), erDto.getKostenstelleIId());
		this.erDto = erDto;
		MandantDto mandantDto = DelegateFactory.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());
		PartnerDto partnerMandantDto = DelegateFactory.getInstance()
				.getPartnerDelegate()
				.partnerFindByPrimaryKey(mandantDto.getPartnerIId());
		String sLKZMandant = partnerMandantDto.getLandplzortDto().getLandDto()
				.getCLkz();
		LieferantDto lieferantDto = DelegateFactory.getInstance()
				.getLieferantDelegate()
				.lieferantFindByPrimaryKey(erDto.getLieferantIId());
		PartnerDto partnerDto = DelegateFactory.getInstance()
				.getPartnerDelegate()
				.partnerFindByPrimaryKey(lieferantDto.getPartnerIId());
		String sLKZLieferant = "";
		if (partnerDto.getLandplzortDto() != null) {
			if (partnerDto.getLandplzortDto().getLandDto() != null) {
				sLKZLieferant = partnerDto.getLandplzortDto().getLandDto()
						.getCLkz();
			}
		}

		buttonGroup.add(wrbInland);
		buttonGroup.add(wrbAusland);

		if (sLKZMandant.equals(sLKZLieferant)) {
			wrbInland.setSelected(true);
		} else {
			wrbAusland.setSelected(true);
		}
		jbInit();
		setDefaults();
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	private void jbInit() throws Throwable {

		wnfBetragSkonto1.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfBetragSkonto1.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfBetragSkonto2.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfBetragSkonto2.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wbuSkonto1uebernehmen.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wbuSkonto1uebernehmen.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wbuSkonto2uebernehmen.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wbuSkonto2uebernehmen.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));

		wrbInland.setText(LPMain.getTextRespectUISPr("lp.inland"));
		wrbAusland.setText(LPMain.getTextRespectUISPr("lp.ausland"));

		wlaBis1.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaBis2.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaSkonto1.setText(LPMain.getTextRespectUISPr("lp.skonto"));
		wlaSkonto2.setText(LPMain.getTextRespectUISPr("lp.skonto"));
		wbuSkonto1uebernehmen.setText(LPMain
				.getTextRespectUISPr("lp.uebernehmen"));
		wbuSkonto2uebernehmen.setText(LPMain
				.getTextRespectUISPr("lp.uebernehmen"));
		wlaProzent1.setText("%");
		wlaProzent2.setText("%");
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaZahlungsziel.setText(LPMain
				.getTextRespectUISPr("label.zahlungsziel"));
		wlaZusatzText.setText(LPMain.getTextRespectUISPr("lp.zusatztext"));

		wlaSchecknummer.setText(LPMain.getTextRespectUISPr("er.schecknummer"));
		wnfSchecknummer.setFractionDigits(0);
		wnfBetragSkonto1.setEditable(false);
		wnfBetragSkonto2.setEditable(false);
		wnfSkontoProzent1.setEditable(false);
		wnfSkontoProzent2.setEditable(false);
		wtfZahlungsziel.setEditable(false);
		wdfSkonto1.setEditable(false);
		wdfSkonto2.setEditable(false);
		wdfZieldatum.setEditable(false);

		wbuSkonto1uebernehmen.setActionCommand(ACTION_SPECIAL_SKONTO1);
		wbuSkonto1uebernehmen.addActionListener(this);
		wbuSkonto2uebernehmen.setActionCommand(ACTION_SPECIAL_SKONTO2);
		wbuSkonto2uebernehmen.addActionListener(this);

		wlaBetrag = new WrapperLabel(LPMain.getTextRespectUISPr("label.betrag"));
		wnfBetrag = new WrapperNumberField();
		wnfBetrag.setMandatoryField(true);
		jpaWorkingOn.add(wlaZahlungsziel, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZahlungsziel, new GridBagConstraints(1, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfZieldatum, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSkonto1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSkontoProzent1, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaProzent1, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis1, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfSkonto1, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragSkonto1, new GridBagConstraints(5, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuSkonto1uebernehmen, new GridBagConstraints(6,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSkonto2, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSkontoProzent2, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaProzent2, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis2, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfSkonto2, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragSkonto2, new GridBagConstraints(5, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuSkonto2uebernehmen, new GridBagConstraints(6,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaZusatzText, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZusatztext, new GridBagConstraints(1, iZeile, 6, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
						ParameterFac.PARAMETER_SCHECKNUMMER);
		iSchecknummer = ((Integer) parameter.getCWertAsObject()).intValue();

		if (iSchecknummer >= 0) {
			iSchecknummer++;
			wnfSchecknummer.setInteger(iSchecknummer);
			iZeile++;
			jpaWorkingOn.add(wlaSchecknummer, new GridBagConstraints(0, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wnfSchecknummer, new GridBagConstraints(1, iZeile,
					2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;
		jpaWorkingOn.add(wrbInland, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbAusland, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		// lock ist hier egal
		return null;
	}

	public void schecknummerZurueckschreiben() throws Throwable {
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
						ParameterFac.PARAMETER_SCHECKNUMMER);

		parameter.setCWert(iSchecknummer + "");
		DelegateFactory.getInstance().getParameterDelegate()
				.updateParametermandant(parameter);
	}

	private void setDefaults() throws Throwable {
		wnfKopien.setInteger(new Integer(0));

		if (erDto.getZahlungszielIId() != null) {
			ZahlungszielDto zahlungszielDto = DelegateFactory.getInstance()
					.getMandantDelegate()
					.zahlungszielFindByPrimaryKey(erDto.getZahlungszielIId());
			// Bezeichnung
			if (zahlungszielDto != null) {
				if (zahlungszielDto.getZahlungszielsprDto() != null) {
					wtfZahlungsziel.setText(zahlungszielDto
							.getZahlungszielsprDto().getCBezeichnung());
				} else {
					wtfZahlungsziel.setText(zahlungszielDto.getCBez());
				}
			} else {
				wtfZahlungsziel.setText(null);
			}
			// Skonotziel 1
			boolean bSkonto1 = false;
			wnfSkontoProzent1.setBigDecimal(zahlungszielDto
					.getSkontoProzentsatz1());
			if (zahlungszielDto.getSkontoAnzahlTage1() != null
					&& zahlungszielDto.getSkontoAnzahlTage1().intValue() != 0) {
				wdfSkonto1.setDate(Helper.addiereTageZuDatum(erDto
						.getDBelegdatum(), zahlungszielDto
						.getSkontoAnzahlTage1().intValue()));
				bSkonto1 = true;
			} else {
				wdfSkonto1.setDate(null);
			}
			wlaSkonto1.setVisible(bSkonto1);
			wnfSkontoProzent1.setVisible(bSkonto1);
			wlaProzent1.setVisible(bSkonto1);
			wlaBis1.setVisible(bSkonto1);
			wdfSkonto1.setVisible(bSkonto1);
			wnfBetragSkonto1.setVisible(bSkonto1);
			wbuSkonto1uebernehmen.setVisible(bSkonto1);
			// Skontoziel 2
			boolean bSkonto2 = false;
			wnfSkontoProzent2.setBigDecimal(zahlungszielDto
					.getSkontoProzentsatz2());
			if (zahlungszielDto.getSkontoAnzahlTage2() != null
					&& zahlungszielDto.getSkontoAnzahlTage2().intValue() != 0) {
				wdfSkonto2.setDate(Helper.addiereTageZuDatum(erDto
						.getDBelegdatum(), zahlungszielDto
						.getSkontoAnzahlTage2().intValue()));
				bSkonto2 = true;
			} else {
				wdfSkonto2.setDate(null);
			}
			wlaSkonto2.setVisible(bSkonto2);
			wnfSkontoProzent2.setVisible(bSkonto2);
			wlaProzent2.setVisible(bSkonto2);
			wlaBis2.setVisible(bSkonto2);
			wdfSkonto2.setVisible(bSkonto2);
			wnfBetragSkonto2.setVisible(bSkonto2);
			wbuSkonto2uebernehmen.setVisible(bSkonto2);
			// Nettoziel
			if (zahlungszielDto.getAnzahlZieltageFuerNetto() != null) {
				wdfZieldatum.setDate(Helper.addiereTageZuDatum(erDto
						.getDBelegdatum(), zahlungszielDto
						.getAnzahlZieltageFuerNetto().intValue()));
			}
			berechneSkontovorschlaege();
		}
		wnfBetrag.setBigDecimal(getOffenenWert());
	}

	private BigDecimal getOffenenWert() throws Throwable {
		BigDecimal bdBereitsBezahlt = getWertBereitsBezahlt();
		if (bdBereitsBezahlt == null) {
			bdBereitsBezahlt = new BigDecimal(0);
		}
		return erDto.getNBetrag().subtract(bdBereitsBezahlt);
	}

	public String getModul() {
		return EingangsrechnungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		if (wrbInland.isSelected()) {
			return EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_INLAND;
		} else {
			return EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_AUSLAND;
		}

	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;
		JasperPrintLP[] aJasperPrint = DelegateFactory
				.getInstance()
				.getEingangsrechnungDelegate()
				.printEingangsrechnung(erDto.getIId(), getReportname(),
						wnfKopien.getInteger(), wnfBetrag.getBigDecimal(),
						wtfZusatztext.getText(),
						wnfSchecknummer.getInteger());
		jasperPrint = aJasperPrint[0];

		for (int i = 1; i < aJasperPrint.length; i++) {
			jasperPrint = Helper.addReport2Report(jasperPrint,
					aJasperPrint[i].getPrint());
		}
		return jasperPrint;
	}

	protected BigDecimal getWertBereitsBezahlt() throws Throwable {
		return DelegateFactory.getInstance().getEingangsrechnungDelegate()
				.getBezahltBetrag(erDto.getIId(), null);
	}

	private void berechneSkontovorschlaege() throws ExceptionLP, Throwable {
		// Skontovorschlaege erstellen
		BigDecimal dSkonto1 = new BigDecimal(0);
		BigDecimal dSkonto2 = new BigDecimal(0);
		if (wnfSkontoProzent1.isVisible()) {
			dSkonto1 = wnfSkontoProzent1.getBigDecimal();
		}
		if (wnfSkontoProzent2.isVisible()) {
			dSkonto2 = wnfSkontoProzent2.getBigDecimal();
		}
		if (dSkonto1 != null) {
			BigDecimal bdSatz1 = new BigDecimal
			        (1.0).subtract((dSkonto1.divide(new BigDecimal(100.0),
			        4, BigDecimal.ROUND_HALF_EVEN)));
			BigDecimal bdSkontobetr1 = erDto.getNBetrag().multiply(bdSatz1);
			wnfBetragSkonto1.setBigDecimal(bdSkontobetr1
					.subtract(getWertBereitsBezahlt()));
		}
		if (dSkonto2 != null) {
			BigDecimal bdSatz2 = new BigDecimal
					(1.0).subtract((dSkonto2.divide(new BigDecimal(100.0),
					4, BigDecimal.ROUND_HALF_EVEN)));
			BigDecimal bdSkontobetr2 = erDto.getNBetrag().multiply(bdSatz2);
			wnfBetragSkonto2.setBigDecimal(bdSkontobetr2
					.subtract(getWertBereitsBezahlt()));
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_SKONTO1)) {
			wnfBetrag.setBigDecimal(wnfBetragSkonto1.getBigDecimal());
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SKONTO2)) {
			wnfBetrag.setBigDecimal(wnfBetragSkonto2.getBigDecimal());
		}
	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		// nichts zu rechnen
		return null;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		//Drucken aendert den Status nicht
	}

	
}
