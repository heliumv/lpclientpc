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
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

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
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportLagerstandliste extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperLabel wlaStichtag = new WrapperLabel();
	private WrapperDateField wtdArtikel = new WrapperDateField();
	private Integer artikelIId_Von = null;
	private Integer artikelIId_Bis = null;
	private PanelQueryFLR panelQueryFLRArtikel_Von = null;
	private PanelQueryFLR panelQueryFLRArtikel_Bis = null;

	private WrapperCheckBox wcbVersteckte = null;

	private WrapperSelectField wsfLager = null;
	private WrapperSelectField wsfLagerplatz = null;
	private WrapperSelectField wsfArtikelgruppe = null;
	private WrapperSelectField wsfArtikelklasse = null;
	private WrapperSelectField wsfShopgruppe = null;

	private WrapperSelectField wsfVkpreisliste = null;

	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";
	static final public String ACTION_SPECIAL_ARTIKELVON_FROM_LISTE = "ACTION_SPECIAL_ARTIKELVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE = "ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE";

	private WrapperButton wbuArtikelnrVon = new WrapperButton();
	private WrapperButton wbuArtikelnrBis = new WrapperButton();
	private WrapperTextField wtfArtikelnrVon = new WrapperTextField();
	private WrapperTextField wtfArtikelnrBis = new WrapperTextField();
	private WrapperCheckBox wcbOhneAZArtikel = new WrapperCheckBox();
	private WrapperCheckBox wcbMitArtikelOhneLagerstand = new WrapperCheckBox();
	private WrapperCheckBox wcbMitNichtLagerbewirtschaftetenArtikeln = new WrapperCheckBox();
	private WrapperCheckBox wcbNurLagerbewerteteArtikel = new WrapperCheckBox();
	private WrapperComboBox wcbArtikelarten = new WrapperComboBox();
	private WrapperCheckBox wcbMitAbgewertetemGestpreis = new WrapperCheckBox();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbSortArtikelnr = new WrapperRadioButton();
	private WrapperRadioButton wrbSortArtikelklasse = new WrapperRadioButton();
	private WrapperRadioButton wrbSortArtikelgruppe = new WrapperRadioButton();
	private WrapperRadioButton wrbSortLagerwert = new WrapperRadioButton();
	private WrapperRadioButton wrbSortGestehungspreis = new WrapperRadioButton();
	private WrapperRadioButton wrbSortLagerort = new WrapperRadioButton();
	private WrapperRadioButton wrbSortBezeichnung = new WrapperRadioButton();
	private WrapperRadioButton wrbSortKurzBezeichnung = new WrapperRadioButton();
	private WrapperRadioButton wrbSortShopgruppe = new WrapperRadioButton();
	private WrapperRadioButton wrbSortReferenznummer = new WrapperRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperRadioButton wrbPreisVKPreisliste = new WrapperRadioButton();
	private WrapperRadioButton wrbPreisGest = new WrapperRadioButton();
	private ButtonGroup buttonGroupPreis = new ButtonGroup();

	public ReportLagerstandliste(InternalFrameArtikel internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("artikel.report.lagerstandsliste");

		jbInit();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfLager;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaStichtag.setText(LPMain.getTextRespectUISPr("lp.stichtag"));

		wsfLager = new WrapperSelectField(WrapperSelectField.LAGER,
				getInternalFrame(), true);
		wsfArtikelgruppe = new WrapperSelectField(
				WrapperSelectField.ARTIKELGRUPPE, getInternalFrame(), true);
		wsfArtikelklasse = new WrapperSelectField(
				WrapperSelectField.ARTIKELKLASSE, getInternalFrame(), true);
		wsfShopgruppe = new WrapperSelectField(WrapperSelectField.SHOPGRUPPE,
				getInternalFrame(), true);
		wsfLagerplatz = new WrapperSelectField(WrapperSelectField.LAGERPLATZ,
				getInternalFrame(), true);
		wsfVkpreisliste = new WrapperSelectField(
				WrapperSelectField.VKPREISLISTE, getInternalFrame(), true);

		wcbOhneAZArtikel
				.setText(LPMain
						.getTextRespectUISPr("artikel.report.lagerstandsliste.ohneazartikel"));
		wcbNurLagerbewerteteArtikel
				.setText(LPMain
						.getTextRespectUISPr("artikel.report.lagerstandsliste.nurlagerbewerteteartikel"));

		wbuArtikelnrVon.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.von"));
		wbuArtikelnrBis.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.bis"));
		wcbOhneAZArtikel.setSelected(true);
		wcbNurLagerbewerteteArtikel.setSelected(true);

		wcbVersteckte = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.versteckte"));

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wcbMitAbgewertetemGestpreis
				.setText(LPMain
						.getTextRespectUISPr("artikel.report.lagerstandsliste.mitabgewertetemgestpreis"));

		wcbMitNichtLagerbewirtschaftetenArtikeln
		.setText(LPMain
				.getTextRespectUISPr("artikel.lagerstandsliste.mitnichtlagerbewirtschafteten"));

		wcbMitArtikelOhneLagerstand
				.setText(LPMain
						.getTextRespectUISPr("artikel.report.lagerstandsliste.mitartikelohnelagerstand"));

		wrbSortArtikelnr.setSelected(true);
		wrbSortArtikelnr.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer"));
		wrbSortArtikelgruppe.setText(LPMain
				.getTextRespectUISPr("lp.artikelgruppe"));
		wrbSortArtikelklasse.setText(LPMain
				.getTextRespectUISPr("lp.artikelklasse"));
		wrbSortLagerwert.setText(LPMain.getTextRespectUISPr("lp.lagerwert"));
		wrbSortGestehungspreis.setText(LPMain
				.getTextRespectUISPr("lp.gestehungspreis"));
		wrbSortShopgruppe.setText(LPMain.getTextRespectUISPr("lp.shopgruppe"));
		wrbSortReferenznummer.setText(LPMain.getTextRespectUISPr("lp.referenznummer"));
		wrbSortLagerort.setText(LPMain.getTextRespectUISPr("artikel.lagerort"));
		wrbSortBezeichnung
				.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wrbSortKurzBezeichnung.setText(LPMain
				.getTextRespectUISPr("artikel.kurzbez"));

		buttonGroupSortierung.add(wrbSortArtikelnr);
		buttonGroupSortierung.add(wrbSortArtikelgruppe);
		buttonGroupSortierung.add(wrbSortArtikelklasse);
		buttonGroupSortierung.add(wrbSortLagerwert);
		buttonGroupSortierung.add(wrbSortGestehungspreis);
		buttonGroupSortierung.add(wrbSortLagerort);
		buttonGroupSortierung.add(wrbSortBezeichnung);
		buttonGroupSortierung.add(wrbSortKurzBezeichnung);
		buttonGroupSortierung.add(wrbSortShopgruppe);
		buttonGroupSortierung.add(wrbSortReferenznummer);

		buttonGroupPreis.add(wrbPreisGest);
		buttonGroupPreis.add(wrbPreisVKPreisliste);
		wrbPreisGest.setText(LPMain.getTextRespectUISPr("lp.gestehungspreis"));
		wrbPreisVKPreisliste
				.setText(LPMain
						.getTextRespectUISPr("artikel.handlagerbewegung.verkaufspreis"));

		wtdArtikel.setEditable(false);

		wrbPreisGest.addActionListener(this);
		wrbPreisVKPreisliste.addActionListener(this);

		wcbArtikelarten.setMandatoryField(true);

		Map<Integer, String> m = new TreeMap<Integer, String>();
		m.put(LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_ALLE,
				LPMain.getTextRespectUISPr("artikel.lagerstandsliste.alle"));
		m.put(LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_OHNESTKL,
				LPMain.getTextRespectUISPr("artikel.lagerstandsliste.ohnestkl"));
		m.put(LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_NURSTKL,
				LPMain.getTextRespectUISPr("artikel.lagerstandsliste.nurstkl"));
		wcbArtikelarten.setMap(m);

		wrbPreisGest.setSelected(true);
		// wbuLager.setEnabled(false);
		wbuArtikelnrVon.setActionCommand(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE);
		wbuArtikelnrVon.addActionListener(this);

		wbuArtikelnrBis.setActionCommand(ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE);
		wbuArtikelnrBis.addActionListener(this);

		wsfVkpreisliste.setEnabled(false);
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		int iZeile = 0;
		jpaWorkingOn.add(wsfLager.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfLager, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortArtikelnr, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortBezeichnung, new GridBagConstraints(4, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbVersteckte, new GridBagConstraints(2, iZeile,
					1, 1, 0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 90,
					0));
		}
		iZeile++;
		jpaWorkingOn.add(wtdArtikel, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), -20, 0));
		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbNurLagerbewerteteArtikel, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbSortArtikelgruppe, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortLagerwert, new GridBagConstraints(4, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wsfArtikelgruppe, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfArtikelgruppe.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wcbMitNichtLagerbewirtschaftetenArtikeln, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbSortArtikelklasse, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortGestehungspreis, new GridBagConstraints(4,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wsfArtikelklasse.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfArtikelklasse, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbOhneAZArtikel, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortKurzBezeichnung, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortLagerort, new GridBagConstraints(4, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfShopgruppe.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfShopgruppe, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortShopgruppe, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortReferenznummer, new GridBagConstraints(4, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitArtikelOhneLagerstand, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuArtikelnrVon, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(wtfArtikelnrVon, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(wbuArtikelnrBis, new GridBagConstraints(2, iZeile, 1,
				1, 0.10, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 140, 0));
		jpaWorkingOn.add(wtfArtikelnrBis, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;

		jpaWorkingOn.add(wsfLagerplatz, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfLagerplatz.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wrbPreisGest, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitAbgewertetemGestpreis, new GridBagConstraints(3,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbPreisVKPreisliste, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfVkpreisliste.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfVkpreisliste, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbArtikelarten, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;


	}

	public String getModul() {
		return ArtikelFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LagerReportFac.REPORT_LAGERSTANDLISTE;
	}

	void dialogQueryArtikelFromListe_Von(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Von = ArtikelFilterFactory
				.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), artikelIId_Von, true);

		new DialogQuery(panelQueryFLRArtikel_Von);
	}

	void dialogQueryArtikelFromListe_Bis(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Bis = ArtikelFilterFactory
				.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), artikelIId_Bis, true);

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
				artikelIId_Von = artikelDto.getIId();
				wtfArtikelnrVon.setText(artikelDto.getCNr());
			} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				artikelIId_Bis = artikelDto.getIId();
				wtfArtikelnrBis.setText(artikelDto.getCNr());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikel_Von) {
				wtfArtikelnrVon.setText(null);
				artikelIId_Von = null;
			} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
				wtfArtikelnrBis.setText(null);
				artikelIId_Bis = null;
			}
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Von(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Bis(e);
		} else {
			if (e.getSource().equals(wrbPreisGest)) {
				wsfVkpreisliste.setKey(null);
				wsfVkpreisliste.setEnabled(false);
				wsfVkpreisliste.setMandatoryField(false);
			} else if (e.getSource().equals(wrbPreisVKPreisliste)) {
				wsfVkpreisliste.setEnabled(true);
				wsfVkpreisliste.setMandatoryField(true);
			}

		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		int iOptionSortierung = -1;

		int iArtikelarten = -1;

		if (((Integer) wcbArtikelarten.getKeyOfSelectedItem()).intValue() == LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_ALLE) {
			iArtikelarten = LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_ALLE;
		} else if (((Integer) wcbArtikelarten.getKeyOfSelectedItem())
				.intValue() == LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_NURSTKL) {
			iArtikelarten = LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_NURSTKL;
		} else if (((Integer) wcbArtikelarten.getKeyOfSelectedItem())
				.intValue() == LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_OHNESTKL) {
			iArtikelarten = LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_OHNESTKL;
		}

		if (wrbSortArtikelnr.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELNUMMER;
		} else if (wrbSortArtikelgruppe.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELGRUPPE;
		} else if (wrbSortArtikelklasse.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELKLASSE;
		} else if (wrbSortLagerwert.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_LAGERWERT;
		} else if (wrbSortGestehungspreis.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_GESTEHUNGSPREIS;
		} else if (wrbSortLagerort.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_LAGERORT;
		} else if (wrbSortBezeichnung.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELBEZEICHNUNG;
		} else if (wrbSortKurzBezeichnung.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELKURZBEZEICHNUNG;
		} else if (wrbSortShopgruppe.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_SHOPGRUPPE;
		} else if (wrbSortReferenznummer.isSelected()) {
			iOptionSortierung = LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_REFERENZNUMMER;
		}
		
		return DelegateFactory
				.getInstance()
				.getLagerReportDelegate()
				.printLagerstandliste(wsfLager.getIKey(),
						wtdArtikel.getTimestamp(),
						!wcbOhneAZArtikel.isSelected(),
						wtfArtikelnrVon.getText(), wtfArtikelnrBis.getText(),
						wsfArtikelgruppe.getIKey(), wsfArtikelklasse.getIKey(),
						wsfVkpreisliste.getIKey(), iOptionSortierung,
						iArtikelarten,
						wcbNurLagerbewerteteArtikel.isSelected(),
						wcbMitAbgewertetemGestpreis.isSelected(),
						wcbMitArtikelOhneLagerstand.isSelected(),
						wsfLagerplatz.getIKey(), wcbVersteckte.isSelected(),
						wsfShopgruppe.getIKey(),wcbMitNichtLagerbewirtschaftetenArtikeln.isSelected());
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
