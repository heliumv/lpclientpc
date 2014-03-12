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
package com.lp.client.rechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportMahnung extends ReportBeleg implements PanelReportIfJRDS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RechnungDto rechnungDto = null;
	private PanelQuery panelQueryRechnungen = null;

	protected JPanel jpaWorkingOn = new JPanel();
	private WrapperButton wbuMahnstufeRuecksetzen = new WrapperButton();
	private WrapperRadioButton[] wrbMahnstufen = null;
	private WrapperDateField[] wdfMahnstufen = null;
	private WrapperButton wbuRuecknehmenDerLetzten = new WrapperButton();
	private WrapperRadioButton wrbDruckenAllerMahnungen = new WrapperRadioButton();
	private ButtonGroup bg = new ButtonGroup();
	private MahnstufeDto[] mahnstufeDtos = null;
	private WrapperLabel wlaWert = new WrapperLabel();
	private WrapperLabel wlaWertBezahlt = new WrapperLabel();
	private WrapperLabel wlaWertOffen = new WrapperLabel();
	private WrapperLabel wlaWertUst = new WrapperLabel();
	private WrapperLabel wlaWertBezahltUst = new WrapperLabel();
	private WrapperLabel wlaWertOffenUst = new WrapperLabel();
	private WrapperNumberField wnfWert = new WrapperNumberField();
	private WrapperNumberField wnfWertBezahlt = new WrapperNumberField();
	private WrapperNumberField wnfWertOffen = new WrapperNumberField();
	private WrapperNumberField wnfWertUst = new WrapperNumberField();
	private WrapperNumberField wnfWertBezahltUst = new WrapperNumberField();
	private WrapperNumberField wnfWertOffenUst = new WrapperNumberField();
	private WrapperLabel wlaWaehrung1 = new WrapperLabel();
	private WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperLabel wlaWaehrung3 = new WrapperLabel();
	private WrapperLabel wlaBelegdatum = new WrapperLabel();
	private WrapperLabel wlaZieldatum = new WrapperLabel();
	private WrapperDateField wdfBelegdatum = new WrapperDateField();
	private WrapperDateField wdfZieldatum = new WrapperDateField();
	private WrapperLabel wlaLetztesMahndatum = new WrapperLabel();
	private WrapperDateField wdfLetztesMahndatum = new WrapperDateField();
	private WrapperLabel wlaLetzteMahnstufe = new WrapperLabel();
	private WrapperTextNumberField wtnfLetzteMahnstufe = new WrapperTextNumberField();

	private String ACTION_MAHNSTUFEZURUECKSETZEN = "ACTION_MAHNSTUFEZURUECKSETZEN";
	private String ACTION_LETZTEZURUECKNEHMEN = "ACTION_LETZTEZURUECKNEHMEN";

	private Boolean bAusfuerlich = null;

	public ReportMahnung(InternalFrame internalFrame, PanelBasis panelToRefresh, String add2Title,
			RechnungDto rechnungDto, PanelQuery panelQueryRechnungen)
			throws Throwable {
		super(internalFrame, panelToRefresh, add2Title, LocaleFac.BELEGART_RECHNUNG,
				rechnungDto.getIId(), rechnungDto.getKostenstelleIId());
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_RECHNUNG,
						ParameterFac.PARAMETER_AUSFUEHRLICHER_MAHNUNGSDRUCK_AR);
		bAusfuerlich = (Boolean) parameter.getCWertAsObject();

		this.rechnungDto = rechnungDto;
		this.panelQueryRechnungen = panelQueryRechnungen;
		jbInit();
		setDefaults();
		initComponents();
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		// lock ist hier egal
		return null;
	}

	public String getReportname() {
		if (bAusfuerlich) {
			return FinanzReportFac.REPORT_MAHNUNG_AUSFUEHRLICH;
		} else {
			return FinanzReportFac.REPORT_MAHNUNG;
		}
	}

	private void setDefaults() throws Throwable {
		/**
		 * @todo Default selektierung PJ 5150
		 */
		wrbDruckenAllerMahnungen.setVisible(false);
		wdfBelegdatum.setDate(rechnungDto.getTBelegdatum());
		java.sql.Date dZieldatum = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.berechneZielDatumFuerBelegdatum(
						new java.sql.Date(rechnungDto.getTBelegdatum()
								.getTime()), rechnungDto.getZahlungszielIId());
		wdfZieldatum.setDate(dZieldatum);
		wnfWert.setBigDecimal(rechnungDto.getNWertfw());
		wnfWertUst.setBigDecimal(rechnungDto.getNWertustfw());
		BigDecimal bdBezahlt = DelegateFactory.getInstance()
				.getRechnungDelegate()
				.getBereitsBezahltWertVonRechnungFw(rechnungDto.getIId(), null);
		BigDecimal bdBezahltUst = DelegateFactory
				.getInstance()
				.getRechnungDelegate()
				.getBereitsBezahltWertVonRechnungUstFw(rechnungDto.getIId(),
						null);
		wnfWertBezahlt.setBigDecimal(bdBezahlt);
		wnfWertBezahltUst.setBigDecimal(bdBezahltUst);
		wnfWertOffen
				.setBigDecimal(rechnungDto.getNWertfw().subtract(bdBezahlt));
		wnfWertOffenUst.setBigDecimal(rechnungDto.getNWertustfw().subtract(
				bdBezahltUst));
		wlaWaehrung1.setText(rechnungDto.getWaehrungCNr());
		wlaWaehrung2.setText(rechnungDto.getWaehrungCNr());
		wlaWaehrung3.setText(rechnungDto.getWaehrungCNr());

		
		Integer aktuelleMahnstufeIId=DelegateFactory.getInstance()
		.getMahnwesenDelegate()
		.getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId());
		
		wdfLetztesMahndatum.setDate(DelegateFactory.getInstance()
				.getMahnwesenDelegate()
				.getAktuellesMahndatumEinerRechnung(rechnungDto.getIId()));
		if (aktuelleMahnstufeIId != null) {
			wtnfLetzteMahnstufe.setText(aktuelleMahnstufeIId
					.toString());
		}
		Integer mahnstufeIId = DelegateFactory.getInstance()
				.getMahnwesenDelegate()
				.berechneMahnstufeFuerRechnung(rechnungDto);

		if (mahnstufeIId != null) {
			if (mahnstufeIId.intValue() < FinanzServiceFac.MAHNSTUFE_99) {
				wrbMahnstufen[mahnstufeIId.intValue() - 1].setSelected(true);

			} else {
				wrbMahnstufen[wrbMahnstufen.length - 1].setSelected(true);
			}
		} else if (aktuelleMahnstufeIId != null) {
			if (aktuelleMahnstufeIId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
				wrbMahnstufen[wrbMahnstufen.length - 1].setSelected(true);
			} else {
				wrbMahnstufen[aktuelleMahnstufeIId.intValue() - 1]
						.setSelected(true);
			}
		}
	}

	private void jbInit() throws Throwable {
		jpaWorkingOn.setLayout(new GridBagLayout());
		wrbDruckenAllerMahnungen.setText(LPMain.getInstance()
				.getTextRespectUISPr("fb.allemahnungendrucken"));
		mahnstufeDtos = DelegateFactory.getInstance().getMahnwesenDelegate()
				.mahnstufeFindAll();
		wrbMahnstufen = new WrapperRadioButton[mahnstufeDtos.length];
		wdfMahnstufen = new WrapperDateField[mahnstufeDtos.length];
		for (int i = 0; i < wrbMahnstufen.length; i++) {
			wrbMahnstufen[i] = new WrapperRadioButton();
			String sText;
			if (mahnstufeDtos[i].getIId().intValue() == FinanzServiceFac.MAHNSTUFE_99) {
				sText = LPMain.getInstance().getTextRespectUISPr(
						"fb.raschreiben");
			} else {
				sText = mahnstufeDtos[i].getIId()
						+ ". "
						+ LPMain.getInstance()
								.getTextRespectUISPr("lp.mahnung");
			}
			wrbMahnstufen[i].setText(sText);
			wdfMahnstufen[i] = new WrapperDateField();
			wdfMahnstufen[i].getCalendarButton().setVisible(false);
			wdfMahnstufen[i].setEnabled(false);
			// set Dates to the dateFields
			MahnungDto[] vorhandeneMahnungen = DelegateFactory.getInstance()
					.getMahnwesenDelegate()
					.mahnungFindByRechnungIId(rechnungDto.getIId());
			for (int y = 0; y < vorhandeneMahnungen.length; y++) {
				if (mahnstufeDtos[i].getIId().equals(
						vorhandeneMahnungen[y].getMahnstufeIId())) {
					wdfMahnstufen[i].setDate(vorhandeneMahnungen[y]
							.getTMahndatum());
				}
			}
			bg.add(wrbMahnstufen[i]);
		}
		wlaWaehrung1.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung1.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaBelegdatum.setMinimumSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wlaBelegdatum.setPreferredSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wlaLetztesMahndatum.setMinimumSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wlaLetztesMahndatum.setPreferredSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wlaLetzteMahnstufe.setMinimumSize(new Dimension(60, Defaults
				.getInstance().getControlHeight()));
		wlaLetzteMahnstufe.setPreferredSize(new Dimension(60, Defaults
				.getInstance().getControlHeight()));
		wlaZieldatum.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaZieldatum.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaBelegdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.belegdatum"));
		wlaZieldatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.zahlungsziel"));
		wlaWert.setText(LPMain.getInstance().getTextRespectUISPr("lp.wert"));
		wlaWertBezahlt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.bezahlt"));
		wlaWertBezahltUst.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mwstshort"));
		wlaWertOffen.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.offen"));
		wlaWertOffenUst.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mwstshort"));
		wlaWertUst.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mwstshort"));
		wlaLetzteMahnstufe.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mahnstufe"));
		wlaLetztesMahndatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"fb.letztemahnung"));

		wdfBelegdatum.setEnabled(false);
		wdfZieldatum.setEnabled(false);
		wnfWert.setEditable(false);
		wnfWertUst.setEditable(false);
		wnfWertBezahlt.setEditable(false);
		wnfWertBezahltUst.setEditable(false);
		wnfWertOffen.setEditable(false);
		wnfWertOffenUst.setEditable(false);
		wnfWert.setMaximumIntegerDigits(7);
		wnfWertUst.setMaximumIntegerDigits(7);
		wnfWertBezahlt.setMaximumIntegerDigits(7);
		wnfWertBezahltUst.setMaximumIntegerDigits(7);
		wnfWertOffen.setMaximumIntegerDigits(7);
		wnfWertOffenUst.setMaximumIntegerDigits(7);
		wdfLetztesMahndatum.setEnabled(false);
		wtnfLetzteMahnstufe.setEditable(false);
		wdfBelegdatum.setShowRubber(false);
		wdfZieldatum.setShowRubber(false);
		wbuMahnstufeRuecksetzen.setText(LPMain.getInstance()
				.getTextRespectUISPr("fb.mahnstuferuecksetzen"));
		wbuRuecknehmenDerLetzten.setText(LPMain.getInstance()
				.getTextRespectUISPr("fb.ruecknehmenderletztenmahnung"));
		bg.add(wrbDruckenAllerMahnungen);
		bg.add(wbuMahnstufeRuecksetzen);
		bg.add(wbuRuecknehmenDerLetzten);
		wbuMahnstufeRuecksetzen.addActionListener(this);
		wbuMahnstufeRuecksetzen.setActionCommand(ACTION_MAHNSTUFEZURUECKSETZEN);
		wbuRuecknehmenDerLetzten.addActionListener(this);
		wbuRuecknehmenDerLetzten.setActionCommand(ACTION_LETZTEZURUECKNEHMEN);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuMahnstufeRuecksetzen, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		// die felder mit den rechnungsdaten
		jpaWorkingOn.add(wlaBelegdatum, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBelegdatum, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaZieldatum, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfZieldatum, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaWert, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWert, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWertUst, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWertUst, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung1, new GridBagConstraints(5, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaWertBezahlt, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWertBezahlt, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWertBezahltUst, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWertBezahltUst, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung2, new GridBagConstraints(5, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaWertOffen, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWertOffen, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWertOffenUst, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWertOffenUst, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung3, new GridBagConstraints(5, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaLetztesMahndatum, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfLetztesMahndatum, new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLetzteMahnstufe, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtnfLetzteMahnstufe, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile = iZeile - 3;
		for (int i = 0; i < wrbMahnstufen.length; i++) {

			jpaWorkingOn.add(wrbMahnstufen[i],
					new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wdfMahnstufen[i],
					new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			iZeile++;
		}
		iZeile++;
		jpaWorkingOn.add(wbuRuecknehmenDerLetzten, new GridBagConstraints(0,
				iZeile, 2, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbDruckenAllerMahnungen, new GridBagConstraints(0,
				iZeile, 6, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP print = null;

		for (int i = 0; i < wrbMahnstufen.length; i++) {
			if (wrbMahnstufen[i].isSelected()) {
				DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.mahneRechnung(
								rechnungDto.getIId(),
								mahnstufeDtos[i].getIId(),
								Helper.cutDate(new java.sql.Date(System
										.currentTimeMillis())));

				if (bAusfuerlich) {
					KundeDto kundeDto = DelegateFactory.getInstance()
							.getKundeDelegate()
							.kundeFindByPrimaryKey(rechnungDto.getKundeIId());

					Locale locKunde = Helper.string2Locale(kundeDto
							.getPartnerDto().getLocaleCNrKommunikation());
					print = DelegateFactory
							.getInstance()
							.getRechnungDelegate()
							.printRechnungAlsMahnung(rechnungDto.getIId(),
									mahnstufeDtos[i].getIId(), locKunde,
									isBPrintLogo());
				} else {
					print = DelegateFactory
							.getInstance()
							.getFinanzReportDelegate()
							.printMahnungFuerRechnung(rechnungDto.getIId(),
									mahnstufeDtos[i].getIId(), isBPrintLogo());
				}

			}
		}
		// }
		// refresh auf die Liste
		panelQueryRechnungen.eventYouAreSelected(false);
		return print;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
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
		return wbuMahnstufeRuecksetzen;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_MAHNSTUFEZURUECKSETZEN)) {
			DelegateFactory.getInstance().getRechnungDelegate()
					.mahneRechnung(rechnungDto.getIId(), null, null);
			getInternalFrame().closePanelDialog();
		}
		if (e.getActionCommand().equals(ACTION_LETZTEZURUECKNEHMEN)) {
			DelegateFactory.getInstance().getRechnungDelegate()
					.setzeMahnstufeZurueck(rechnungDto.getIId());
			getInternalFrame().closePanelDialog();

		}
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		// wird schon in getReport() gemacht
	}
	
	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		// wird schon in getReport() gemacht
		return null;
	}
	
}
