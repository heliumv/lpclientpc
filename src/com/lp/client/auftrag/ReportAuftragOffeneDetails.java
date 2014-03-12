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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.EventObject;
import java.util.LinkedHashMap;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalVerkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportAuftragOffeneDetails extends PanelReportJournalVerkauf
		implements PanelReportIfJRDS {
	private static final long serialVersionUID = 1L;
	private WrapperCheckBox wcbSortiereNachLiefertermin = null;
	private WrapperCheckBox wcbMitAngelegten = null;
	private WrapperCheckBox wcbLagerstandsdetail = null;
	private WrapperRadioButton wrbSortiereNachProjekt = null;
	protected WrapperLabel wlaStichtag = new WrapperLabel();
	protected WrapperDateField wdfStichtag = new WrapperDateField();
	private WrapperComboBox wcoArt;

	private final static String ACTION_SPECIAL_ARTIKELKLASSE = "action_special_artikelklasse";
	private final static String ACTION_SPECIAL_ARTIKELGRUPPE = "action_special_artikelgruppe";
	private final static String ACTION_SPECIAL_AUFTRAG = "action_special_auftrag";
	private final static String ACTION_SPECIAL_ARTIKEL_VON = "action_special_artikel_von";
	private final static String ACTION_SPECIAL_ARTIKEL_BIS = "action_special_artikel_bis";

	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private PanelQueryFLR panelQueryFLRArtikelklasse = null;
	private PanelQueryFLR panelQueryFLRArtikelVon = null;
	private PanelQueryFLR panelQueryFLRArtikelBis = null;

	private WrapperButton wbuArtikelklasse = null;
	private WrapperButton wbuArtikelgruppe = null;
	private WrapperTextField wtfArtikelklasse = null;
	private WrapperTextField wtfArtikelgruppe = null;

	private WrapperButton wbuArtikelVon = null;
	private WrapperButton wbuArtikelBis = null;
	private WrapperTextField wtfArtikelVon = null;
	private WrapperTextField wtfArtikelBis = null;
	private WrapperLabel wlaProjekt = null;
	private WrapperTextField wtfProjekt = null;

	private Integer artikelklasseIId = null;
	private Integer artikelgruppeIId = null;
	private Integer artikelIIdVon = null;
	private Integer artikelIIdBis = null;

	public ReportAuftragOffeneDetails(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		initComponents();
	}

	protected void jbInit() throws Exception {

		wcbSortiereNachLiefertermin = new WrapperCheckBox();
		wcbSortiereNachLiefertermin.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.sortierungnachliefertermin"));

		wcbMitAngelegten = new WrapperCheckBox();
		wcbMitAngelegten.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.offenedetails.mitangelegten"));

		wcbLagerstandsdetail = new WrapperCheckBox();
		wcbLagerstandsdetail.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.offenedetails.lagerstandsdetail"));

		wrbSortierungPartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunden"));
		wbuPartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wbuPartner.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde.tooltip"));

		wrbSortiereNachProjekt = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("label.projekt"));
		buttonGroupSortierung.add(wrbSortiereNachProjekt);
		wbuArtikelklasse = new WrapperButton(
				LPMain.getTextRespectUISPr("button.artikelklasse"));
		wbuArtikelklasse.setToolTipText(LPMain
				.getTextRespectUISPr("button.artikelklasse.tooltip"));
		wbuArtikelklasse.setMinimumSize(new Dimension(BREITE_BUTTONS, Defaults
				.getInstance().getControlHeight()));
		wbuArtikelklasse.setPreferredSize(new Dimension(BREITE_BUTTONS,
				Defaults.getInstance().getControlHeight()));
		wbuArtikelgruppe = new WrapperButton(
				LPMain.getTextRespectUISPr("button.artikelgruppe"));
		wbuArtikelgruppe.setToolTipText(LPMain
				.getTextRespectUISPr("button.artikelgruppe.tooltip"));
		wtfArtikelklasse = new WrapperTextField();
		wtfArtikelgruppe = new WrapperTextField();
		wtfArtikelklasse.setActivatable(false);
		wtfArtikelgruppe.setActivatable(false);
		wbuArtikelklasse.addActionListener(this);
		wbuArtikelgruppe.addActionListener(this);
		wbuArtikelklasse.setActionCommand(ACTION_SPECIAL_ARTIKELKLASSE);
		wbuArtikelgruppe.setActionCommand(ACTION_SPECIAL_ARTIKELGRUPPE);
		wbuArtikelVon = new WrapperButton(LPMain.getTextRespectUISPr("lp.von")
				+ " " + LPMain.getTextRespectUISPr("button.artikel"));
		wbuArtikelVon.setToolTipText(LPMain
				.getTextRespectUISPr("button.artikel.tooltip"));
		wbuArtikelVon.addActionListener(this);
		wbuArtikelVon.setActionCommand(ACTION_SPECIAL_ARTIKEL_VON);
		wbuArtikelBis = new WrapperButton(LPMain.getTextRespectUISPr("lp.bis")
				+ " " + LPMain.getTextRespectUISPr("button.artikel"));
		wbuArtikelBis.setToolTipText(LPMain
				.getTextRespectUISPr("button.artikel.tooltip"));
		wbuArtikelBis.addActionListener(this);
		wbuArtikelBis.setActionCommand(ACTION_SPECIAL_ARTIKEL_BIS);
		wtfArtikelVon = new WrapperTextField();
		wtfArtikelBis = new WrapperTextField();
		wtfArtikelVon.setActivatable(false);
		wtfArtikelBis.setActivatable(false);
		wlaProjekt = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.projekt"));
		wtfProjekt = new WrapperTextField();
		wlaStichtag = new WrapperLabel();
		wdfStichtag = new WrapperDateField();
		wlaStichtag.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.stichtag"));
		wdfStichtag.setMandatoryField(true);
		wdfStichtag.setDate(new Date(System.currentTimeMillis()));
		LinkedHashMap<Integer, String> hm = new LinkedHashMap<Integer, String>();
		hm.put(0, LPMain.getTextRespectUISPr("auft.menu.ansicht.alle")); // Alle
																			// Auftraege
		hm.put(1,
				LPMain.getTextRespectUISPr("auft.journal.ohnerahmenauftraege")); // ohne
																					// Rahmenauftraege,
																					// d.h.
																					// Abrufe
																					// und
																					// freie
																					// Auftraege
		hm.put(2, LPMain.getTextRespectUISPr("auft.journal.nurrahmenauftraege")); // nur
																					// Rahmenauftraege
		wcoArt = new WrapperComboBox();
		wcoArt.setMandatoryField(true);
		wcoArt.setKeyOfSelectedItem(0);
		wcoArt.setMap(hm);
		wrbSortierungIdentNr.setVisible(true);

		iZeile++;
		jpaWorkingOn.add(wrbSortiereNachProjekt, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaProjekt, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(3, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikelklasse, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelklasse, new GridBagConstraints(3, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbSortiereNachLiefertermin, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikelgruppe, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelgruppe, new GridBagConstraints(3, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbMitAngelegten, new GridBagConstraints(0, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikelVon, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelVon, new GridBagConstraints(3, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuArtikelBis, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelBis, new GridBagConstraints(3, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcoArt, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbLagerstandsdetail, new GridBagConstraints(2,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		this.setEinschraenkungDatumBelegnummerSichtbar(false);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					artikelgruppeIId = (Integer) key;
					ArtgruDto artikelgruppeDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artgruFindByPrimaryKey(artikelgruppeIId);
					if (artikelgruppeDto.getArtgrusprDto() != null) {
						wtfArtikelgruppe.setText(artikelgruppeDto
								.getArtgrusprDto().getCBez());
					} else {
						wtfArtikelgruppe.setText(artikelgruppeDto.getCNr());
					}
				}
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					artikelklasseIId = (Integer) key;
					ArtklaDto artikelklasseDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artklaFindByPrimaryKey(artikelklasseIId);
					if (artikelklasseDto.getArtklasprDto() != null) {
						wtfArtikelklasse.setText(artikelklasseDto
								.getArtklasprDto().getCBez());
					} else {
						wtfArtikelklasse.setText(artikelklasseDto.getCNr());
					}
				}
			}

			else if (e.getSource() == panelQueryFLRArtikelBis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					artikelIIdBis = (Integer) key;
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(artikelIIdBis);
					wtfArtikelBis.setText(artikelDto.getCNr());
				}
			} else if (e.getSource() == panelQueryFLRArtikelVon) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					artikelIIdVon = (Integer) key;
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(artikelIIdVon);
					wtfArtikelVon.setText(artikelDto.getCNr());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {
				artikelgruppeIId = null;
				wtfArtikelgruppe.setText(null);
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				artikelklasseIId = null;
				wtfArtikelklasse.setText(null);
			} else if (e.getSource() == panelQueryFLRArtikelBis) {
				artikelIIdBis = null;
				wtfArtikelBis.setText(null);
			} else if (e.getSource() == panelQueryFLRArtikelVon) {
				artikelIIdVon = null;
				wtfArtikelVon.setText(null);
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELGRUPPE)) {
			dialogQueryArtikelgruppe();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELKLASSE)) {
			dialogQueryArtikelklasse();
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_BIS)) {
			dialogQueryArtikelBis();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_VON)) {
			dialogQueryArtikelVon();
		}
	}

	private void dialogQueryArtikelklasse() throws Throwable {
		panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikelklasse(getInternalFrame(),
						artikelklasseIId);
		new DialogQuery(panelQueryFLRArtikelklasse);
	}

	private void dialogQueryArtikelgruppe() throws Throwable {
		panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikelgruppe(getInternalFrame(),
						artikelgruppeIId);
		new DialogQuery(panelQueryFLRArtikelgruppe);
	}

	private void dialogQueryArtikelVon() throws Throwable {
		panelQueryFLRArtikelVon = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), artikelIIdVon, true);
		new DialogQuery(panelQueryFLRArtikelVon);
	}

	private void dialogQueryArtikelBis() throws Throwable {
		panelQueryFLRArtikelBis = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), artikelIIdBis, true);
		new DialogQuery(panelQueryFLRArtikelBis);
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected ReportJournalKriterienDto getKriterien() {
		ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
		befuelleKriterien(krit);
		if (wrbSortierungIdentNr.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT;
		} else if (wrbSortiereNachProjekt.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT;
		}
		return krit;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getAuftragReportDelegate()
				.printAuftragOffeneDetail(getKriterien(),
						wdfStichtag.getDate(),
						new Boolean(wcbSortiereNachLiefertermin.isSelected()),
						new Boolean(false), artikelklasseIId, artikelgruppeIId,
						wtfArtikelVon.getText(), wtfArtikelBis.getText(),
						wtfProjekt.getText(),
						(Integer) wcoArt.getKeyOfSelectedItem(),
						wcbLagerstandsdetail.isSelected(),
						wcbMitAngelegten.isSelected());

	}
}
