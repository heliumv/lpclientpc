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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportArtikelstatistik extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperLabel wlaZeitraum = new WrapperLabel();
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();
	private WrapperLabel wlaArtikel = new WrapperLabel();
	private WrapperTextField wtfArtikel = new WrapperTextField();
	private Integer artikelIId = null;
	private WrapperDateRangeController wdrBereich = null;
	private WrapperCheckBox wcbMonatsstatistik = new WrapperCheckBox();
	private WrapperCheckBox wcbEingeschraenkt = new WrapperCheckBox();
	private WrapperCheckBox wcbMitHistory = new WrapperCheckBox();
	private WrapperCheckBox wcbMitHandlagerbewegungen = new WrapperCheckBox();
	private WrapperCheckBox wcbMitBewegungsvorschau = new WrapperCheckBox();

	private WrapperLabel wlaBelegarten = new WrapperLabel();

	private ButtonGroup buttonGroupOption = new ButtonGroup();
	private WrapperRadioButton wrbOptionAlle = new WrapperRadioButton();
	private WrapperRadioButton wrbOptionEK = new WrapperRadioButton();
	private WrapperRadioButton wrbOptionVK = new WrapperRadioButton();
	private WrapperRadioButton wrbOptionFertigung = new WrapperRadioButton();

	public ReportArtikelstatistik(InternalFrame internalFrame,Integer artikelIId,boolean bMonatsstatistik, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr(
				"artikel.report.artikelstatistik");
		jbInit();
		initComponents();
		if (artikelIId != null) {
			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);

			wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
			wtfArtikel.setText(artikelDto
					.formatArtikelbezeichnung());
			this.artikelIId = artikelDto.getIId();
		}
		
		if(bMonatsstatistik){
			wcbMonatsstatistik.setSelected(true);
			wcbEingeschraenkt.setSelected(false);
			wcbMitBewegungsvorschau.setSelected(false);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	private void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaZeitraum.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zeitraum")
				+ ":");
		wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
		wlaArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.report.artikelbestellt.selektierterartikel")
				+ ": ");

		wcbMonatsstatistik.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.statistik.monate"));

		wcbMitHandlagerbewegungen
				.setText(LPMain
						.getInstance()
						.getTextRespectUISPr(
								"report.ladenhueter.handlagerbewegungen.beruecksichtigen"));

		wcbMonatsstatistik.addActionListener(this);

		
		
		wcbMitHistory.setText(LPMain.getInstance().getTextRespectUISPr(
		"artikel.report.artikelstatistik.mitdetails"));
		wcbEingeschraenkt.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.eingeschraenkt"));

		wcbMitBewegungsvorschau.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.statistik.mitbewegungsvorschau"));

		wcbEingeschraenkt.setSelected(true);
		wtfArtikel.setActivatable(false);
		wtfArtikel.setMandatoryField(true);
		wtfArtikel.setEditable(false);
		wtfArtikel.setSaveReportInformation(false);
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);

		wdfVon.setTimestamp(Helper.cutTimestamp(new java.sql.Timestamp(c
				.getTimeInMillis())));
		wdfBis.setTimestamp(Helper.cutTimestamp(new java.sql.Timestamp(System
				.currentTimeMillis() + 24 * 3600000)));

		buttonGroupOption.add(wrbOptionAlle);
		buttonGroupOption.add(wrbOptionEK);
		buttonGroupOption.add(wrbOptionVK);
		buttonGroupOption.add(wrbOptionFertigung);
		wrbOptionAlle.setSelected(true);

		wrbOptionAlle.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.alle"));
		wrbOptionEK.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.menu.einkauf"));
		wrbOptionVK.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.menu.verkauf"));
		wrbOptionFertigung.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.modulname"));
		wlaBelegarten.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.belegarten"));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(2, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(3, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(4, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(5, 5, 1, 1, 0.1,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, 4, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbMonatsstatistik, new GridBagConstraints(4, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbEingeschraenkt, new GridBagConstraints(5, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(2, 2, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaArtikel, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaBelegarten, new GridBagConstraints(0, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbOptionAlle, new GridBagConstraints(2, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbOptionEK, new GridBagConstraints(3, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbOptionVK, new GridBagConstraints(4, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbOptionFertigung, new GridBagConstraints(5, 6, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbMitHandlagerbewegungen, new GridBagConstraints(2,
				7, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitBewegungsvorschau, new GridBagConstraints(4, 7,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitHistory, new GridBagConstraints(5, 7, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));


	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wcbMonatsstatistik)) {
			if (wcbMonatsstatistik.isSelected()) {
				wcbEingeschraenkt.setSelected(false);
				wcbMitBewegungsvorschau.setSelected(false);
			}
		}
	}

	public String getModul() {
		return ArtikelFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ArtikelReportFac.REPORT_ARTIKELSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iOption = -1;

		if (wrbOptionAlle.isSelected()) {
			iOption = ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_ALLE;
		} else if (wrbOptionEK.isSelected()) {
			iOption = ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_EK;

		} else if (wrbOptionVK.isSelected()) {
			iOption = ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_VK;

		} else if (wrbOptionFertigung.isSelected()) {
			iOption = ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_FERTIGUNG;

		}

		return DelegateFactory
				.getInstance()
				.getArtikelReportDelegate()
				.printArtikelstatistik(artikelIId, wdfVon.getDate(),
						wdfBis.getDate(), iOption,
						wcbMonatsstatistik.isSelected(),
						wcbEingeschraenkt.isSelected(),
						wcbMitHandlagerbewegungen.isSelected(),
						wcbMitBewegungsvorschau.isSelected(),wcbMitHistory.isSelected());
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
