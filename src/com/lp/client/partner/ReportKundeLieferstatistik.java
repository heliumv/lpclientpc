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
package com.lp.client.partner;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportKundeLieferstatistik extends PanelBasis implements
		PropertyChangeListener, PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = new JPanel(new GridBagLayout());
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperDateField wdfBis = new WrapperDateField();
	private WrapperDateRangeController wdrBereich = null;
	private InternalFrameKunde internalFrameKunde = null;
	private WrapperRadioButton wrbIdent = null;
	private WrapperRadioButton wrbDatum = null;
	private ButtonGroup bg = null;
	private WrapperLabel wlaSortierung = null;
	private WrapperCheckBox wcbMitTexteingaben = new WrapperCheckBox();
	private WrapperCheckBox wcbVerdichtet = new WrapperCheckBox();
	private WrapperCheckBox wcbMonatsstatistik = new WrapperCheckBox();
	private WrapperCheckBox wcbRechnungsdatum = new WrapperCheckBox();
	private WrapperCheckBox wcbEingeschraenkt = new WrapperCheckBox();
	private WrapperSelectField wsfArtikelgruppe = new WrapperSelectField(
			WrapperSelectField.ARTIKELGRUPPE, getInternalFrame(), true);
	private static final String ACTION_SPECIAL_ARTIKEL = "action_special_artikel";
	private WrapperButton wbuArtikel = null;
	private WrapperTextField wtfArtikel = null;
	private WrapperLabel wlaAdresse = null;
	private ButtonGroup bgAdresse = null;
	private WrapperRadioButton wrbStatistikadresse = new WrapperRadioButton();
	private WrapperRadioButton wrbRechnungsadresse = new WrapperRadioButton();
	private WrapperRadioButton wrbLieferadresse = new WrapperRadioButton();

	private Integer artikelIId = null;
	private PanelQueryFLR panelQueryFLRArtikel = null;

	public ReportKundeLieferstatistik(InternalFrameKunde internalFrameKunde,
			String sAdd2Title) throws Throwable {
		super(internalFrameKunde, sAdd2Title);
		this.internalFrameKunde = internalFrameKunde;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() {
		// Default: beide Datumsfelder leer -> also alles
		// wdrBereich.doClickUp();
		wrbDatum.setSelected(true);
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
		wlaVon.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wlaVon.setPreferredSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wlaBis.setMinimumSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaBis.setPreferredSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));

		wlaSortierung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.sortierung"));
		wrbIdent = new WrapperRadioButton(LPMain.getInstance()
				.getTextRespectUISPr("label.ident"));
		wrbDatum = new WrapperRadioButton(LPMain.getInstance()
				.getTextRespectUISPr("lp.datum"));
		wcbMitTexteingaben.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kunde.lieferstatistik.mittexteingaben"));
		wcbRechnungsdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"kunde.report.lieferstatistik.rechnungsdatum"));
		wcbVerdichtet.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.verdichtetartikelnummer"));
		bg = new ButtonGroup();
		bg.add(wrbIdent);
		bg.add(wrbDatum);

		wbuArtikel = new WrapperButton(
				LPMain.getTextRespectUISPr("button.artikel"));
		wbuArtikel.setActionCommand(ACTION_SPECIAL_ARTIKEL);
		wbuArtikel.addActionListener(this);
		wtfArtikel = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfArtikel.setActivatable(false);

		bgAdresse = new ButtonGroup();
		bgAdresse.add(wrbLieferadresse);
		bgAdresse.add(wrbRechnungsadresse);
		bgAdresse.add(wrbStatistikadresse);

		wlaAdresse = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"part.kunde.lieferstatistik.adresse"));
		wrbStatistikadresse
				.setText(LPMain
						.getTextRespectUISPr("part.kunde.lieferstatistik.statistikadresse"));
		wrbLieferadresse
				.setText(LPMain
						.getTextRespectUISPr("part.kunde.lieferstatistik.lieferadresse"));
		wrbRechnungsadresse
				.setText(LPMain
						.getTextRespectUISPr("part.kunde.lieferstatistik.rechnungsadresse"));
		wrbLieferadresse.setSelected(true);

		wcbEingeschraenkt.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.eingeschraenkt"));
		wcbEingeschraenkt.setSelected(true);
		wdfVon.getDisplay().addPropertyChangeListener(this);
		wdfBis.getDisplay().addPropertyChangeListener(this);

		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);

		wdfVon.setTimestamp(Helper.cutTimestamp(new java.sql.Timestamp(c
				.getTimeInMillis())));
		wdfBis.setTimestamp(Helper.cutTimestamp(new java.sql.Timestamp(System
				.currentTimeMillis() + 24 * 3600000)));

		wcbMonatsstatistik.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.statistik.monate"));

		wcbMonatsstatistik.addActionListener(this);
		wcbVerdichtet.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);

		int iZeile = 0;
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// rechts auffuellen
		jpaWorkingOn.add(new JLabel(), new GridBagConstraints(5, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new JLabel(), new GridBagConstraints(6, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbIdent, new GridBagConstraints(1, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbVerdichtet, new GridBagConstraints(3, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wcbMonatsstatistik, new GridBagConstraints(5, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbStatistikadresse, new GridBagConstraints(6, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbDatum, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wcbRechnungsdatum, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 100, 0));
		
		jpaWorkingOn.add(wcbMitTexteingaben, new GridBagConstraints(3, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbEingeschraenkt, new GridBagConstraints(5, iZeile,
				5, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAdresse, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbLieferadresse, new GridBagConstraints(1, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbStatistikadresse, new GridBagConstraints(3, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wrbRechnungsadresse, new GridBagConstraints(5, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuArtikel, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wsfArtikelgruppe.getWrapperButton(),
				new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfArtikelgruppe.getWrapperTextField(),
				new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						180, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wcbMonatsstatistik)) {
			if (wcbMonatsstatistik.isSelected()) {
				wcbEingeschraenkt.setSelected(false);
				wcbVerdichtet.setSelected(false);
			}
		} else if (e.getSource().equals(wcbVerdichtet)) {
			wcbMonatsstatistik.setSelected(false);
		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL)) {
			dialogQueryArtikelVon();
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikel) {
				Integer iIdArtikelvon = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(iIdArtikelvon);
				artikelIId = iIdArtikelvon;
				wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikel) {
				artikelIId = null;
				wtfArtikel.setText("");
			}

		}
	}

	private void dialogQueryArtikelVon() throws Throwable {
		panelQueryFLRArtikel = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), true);

		panelQueryFLRArtikel.setSelectedId(artikelIId);

		new DialogQuery(panelQueryFLRArtikel);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == wdfVon.getDisplay()
				&& evt.getPropertyName().equals("date")) {
			wdfBis.setMinimumValue(wdfVon.getDate());
		} else if (evt.getSource() == wdfBis.getDisplay()
				&& evt.getPropertyName().equals("date")) {
			wdfVon.setMaximumValue(wdfBis.getDate());
		}
	}

	public String getModul() {
		return PartnerReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return PartnerReportFac.REPORT_KUNDE_LIEFERSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Integer iSortierung;
		if (wrbDatum.isSelected()) {
			iSortierung = new Integer(Helper.SORTIERUNG_NACH_DATUM);
		} else {
			iSortierung = new Integer(Helper.SORTIERUNG_NACH_IDENT);
		}

		int iOptionAdresse = -1;
		if (wrbLieferadresse.isSelected()) {
			iOptionAdresse = KundeReportFac.REPORT_LIEFERSTATISTIK_OPTION_LIEFERADRESSE;
		} else if (wrbRechnungsadresse.isSelected()) {
			iOptionAdresse = KundeReportFac.REPORT_LIEFERSTATISTIK_OPTION_RECHNUNGSADRESSE;
		} else if (wrbStatistikadresse.isSelected()) {
			iOptionAdresse = KundeReportFac.REPORT_LIEFERSTATISTIK_OPTION_STATISTIKADRESSE;
		}

		return DelegateFactory
				.getInstance()
				.getKundeDelegate()
				.printLieferstatistik(
						internalFrameKunde.getKundeDto().getIId(), artikelIId,
						wsfArtikelgruppe.getIKey(), wdfVon.getDate(),
						wdfBis.getDate(), iSortierung,
						wcbMitTexteingaben.isSelected(),
						wcbVerdichtet.isSelected(),
						wcbEingeschraenkt.isSelected(),
						wcbMonatsstatistik.isSelected(), iOptionAdresse,
						wcbRechnungsdatum.isSelected());
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
