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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

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
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportHitliste extends PanelBasis implements PanelReportIfJRDS {
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
	private WrapperDateRangeController wdrBereich = null;

	private WrapperCheckBox wcbVersteckte = null;

	private WrapperTextField wtfFilterVon = new WrapperTextField();
	private WrapperTextField wtfFilterBis = new WrapperTextField();

	private WrapperLabel wlaSortierung = new WrapperLabel();

	private WrapperCheckBox wcbMitHandlagerbewegungen = new WrapperCheckBox();
	private WrapperCheckBox wcbMitFertigung = new WrapperCheckBox();

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private WrapperRadioButton wrbSortierungIdent = new WrapperRadioButton();
	private WrapperRadioButton wrbSortierungArtikelklasse = new WrapperRadioButton();
	private WrapperRadioButton wrbSortierungArtikelgruppe = new WrapperRadioButton();
	private WrapperRadioButton wrbSortierungVKWert = new WrapperRadioButton();
	private WrapperRadioButton wrbSortierungDBWert = new WrapperRadioButton();
	private WrapperRadioButton wrbSortierungShopgruppe = new WrapperRadioButton();

	private PanelQueryFLR panelQueryFLRArtikel_Von = null;
	private PanelQueryFLR panelQueryFLRArtikel_Bis = null;

	private WrapperButton wbuArtikelnrVon = new WrapperButton();
	private WrapperButton wbuArtikelnrBis = new WrapperButton();

	static final public String ACTION_SPECIAL_ARTIKELVON_FROM_LISTE = "ACTION_SPECIAL_ARTIKELVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE = "ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE";

	public ReportHitliste(InternalFrameArtikel internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr("artikel.report.hitliste");
		jbInit();
		initComponents();

		Calendar c = Calendar.getInstance();
		c.set(c.DAY_OF_MONTH, c.get(c.DAY_OF_MONTH) - 1);
		c.set(c.MINUTE, 0);
		c.set(c.HOUR, 0);
		c.set(c.SECOND, 0);
		c.set(c.MILLISECOND, 0);
		wdfBis.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		c.set(c.YEAR, c.get(c.YEAR) - 1);

		wdfVon.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Von(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Bis(e);
		}

	}

	void dialogQueryArtikelFromListe_Von(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Von = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), null, true);

		new DialogQuery(panelQueryFLRArtikel_Von);
	}

	void dialogQueryArtikelFromListe_Bis(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Bis = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), null, true);

		new DialogQuery(panelQueryFLRArtikel_Bis);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRArtikel_Von) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				wtfFilterVon.setText(artikelDto.getCNr());
			} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				wtfFilterBis.setText(artikelDto.getCNr());
			}
		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaZeitraum.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zeitraum")
				+ ":");
		wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));

		wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.sortierung"));

		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		wbuArtikelnrVon.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer")
				+ " " + LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wbuArtikelnrBis.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer")
				+ " " + LPMain.getInstance().getTextRespectUISPr("lp.bis"));
		wbuArtikelnrVon
				.setActionCommand(this.ACTION_SPECIAL_ARTIKELVON_FROM_LISTE);
		wbuArtikelnrVon.addActionListener(this);

		wbuArtikelnrBis
				.setActionCommand(this.ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE);
		wbuArtikelnrBis.addActionListener(this);

		wcbVersteckte = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.versteckte"));

		wrbSortierungIdent.addActionListener(this);
		wrbSortierungArtikelgruppe.addActionListener(this);
		wrbSortierungArtikelklasse.addActionListener(this);

		wcbMitHandlagerbewegungen
				.setText(LPMain
						.getInstance()
						.getTextRespectUISPr(
								"report.ladenhueter.handlagerbewegungen.beruecksichtigen"));
		wcbMitFertigung.setText(LPMain.getInstance().getTextRespectUISPr(
				"report.ladenhueter.fertigung.beruecksichtigen"));

		wrbSortierungIdent.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"));
		wrbSortierungIdent.setSelected(true);
		wrbSortierungArtikelgruppe.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.title.tab.artikelgruppen"));
		wrbSortierungArtikelklasse.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.title.tab.artikelklassen"));
		wrbSortierungShopgruppe.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.shopgruppe"));
		wrbSortierungVKWert.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.verkaufswert"));
		wrbSortierungDBWert.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.dbwert"));

		buttonGroupSortierung.add(wrbSortierungIdent);
		buttonGroupSortierung.add(wrbSortierungArtikelgruppe);
		buttonGroupSortierung.add(wrbSortierungArtikelklasse);
		buttonGroupSortierung.add(wrbSortierungVKWert);
		buttonGroupSortierung.add(wrbSortierungDBWert);
		buttonGroupSortierung.add(wrbSortierungShopgruppe);
		getInternalFrame().addItemChangedListener(this);
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
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(5, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, 4, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, 6, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortierungIdent, new GridBagConstraints(2, 6, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortierungArtikelgruppe, new GridBagConstraints(2,
				7, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortierungArtikelklasse, new GridBagConstraints(2,
				8, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortierungVKWert, new GridBagConstraints(2, 9, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortierungDBWert, new GridBagConstraints(2, 10, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortierungShopgruppe, new GridBagConstraints(2, 11,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuArtikelnrVon, new GridBagConstraints(3, 6, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFilterVon, new GridBagConstraints(4, 6, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikelnrBis, new GridBagConstraints(3, 7, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFilterBis, new GridBagConstraints(4, 7, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbMitFertigung, new GridBagConstraints(4, 8, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitHandlagerbewegungen, new GridBagConstraints(4,
				9, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbVersteckte,
					new GridBagConstraints(4, 10, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

	}

	public String getModul() {
		return LagerReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LagerReportFac.REPORT_LADENHUETER;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		int sortierung = -1;
		if (wrbSortierungIdent.isSelected()) {
			sortierung = LagerReportFac.REPORT_HITLISTE_SORTIERUNG_ARTIKELNR;
		} else if (wrbSortierungArtikelgruppe.isSelected()) {
			sortierung = LagerReportFac.REPORT_HITLISTE_SORTIERUNG_ARTIKELGRUPPE;
		} else if (wrbSortierungArtikelklasse.isSelected()) {
			sortierung = LagerReportFac.REPORT_HITLISTE_SORTIERUNG_ARTIKELKLASSE;
		} else if (wrbSortierungVKWert.isSelected()) {
			sortierung = LagerReportFac.REPORT_HITLISTE_SORTIERUNG_VKWERT;
		} else if (wrbSortierungDBWert.isSelected()) {
			sortierung = LagerReportFac.REPORT_HITLISTE_SORTIERUNG_DBWERT;
		} else if (wrbSortierungShopgruppe.isSelected()) {
			sortierung = LagerReportFac.REPORT_HITLISTE_SORTIERUNG_SHOPGRUPPE;
		}

		return DelegateFactory
				.getInstance()
				.getLagerReportDelegate()
				.printHitliste(
						wdfVon.getTimestamp(),
						wdfBis.getTimestamp(),
						new Integer(sortierung),
						wtfFilterVon.getText(),
						wtfFilterBis.getText(),
						Helper.short2Boolean(wcbMitHandlagerbewegungen
								.getShort()),
						Helper.short2Boolean(wcbMitFertigung.getShort()),
						wcbVersteckte.isSelected());
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
