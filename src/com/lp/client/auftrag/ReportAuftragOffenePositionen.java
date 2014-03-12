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
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
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
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportAuftragOffenePositionen extends PanelReportJournalVerkauf
		implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected WrapperLabel wlaStichtag = new WrapperLabel();
	protected WrapperDateField wdfStichtag = new WrapperDateField();

	private WrapperCheckBox wcbSortiereNachLiefertermin = null;
	private WrapperCheckBox wcbOhnePositionen = null;
	private WrapperTextField wtfFertigungsgruppe = null;
	private WrapperButton wbuFertigungsgruppe = null;
	private WrapperComboBox wcoArt;
	private WrapperRadioButton wrbSortierungLiefertermin = new WrapperRadioButton();
	private WrapperCheckBox wcbSortiereNachAbliefertermin = null;

	private ArrayList<Integer> fertigungsgruppenIIds = new ArrayList<Integer>();

	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "action_special_fertigungsgruppe_from_liste";

	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;

	public ReportAuftragOffenePositionen(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		initComponents();
	}

	protected void jbInit() throws Exception, ExceptionLP, Throwable {

		wrbSortierungPartner.setSelected(true);
		wcbSortiereNachLiefertermin = new WrapperCheckBox();
		wcbSortiereNachLiefertermin.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.sortierungnachliefertermin"));
		wcbSortiereNachAbliefertermin = new WrapperCheckBox();
		wcbSortiereNachAbliefertermin.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.sortierungnachabliefertermin"));
		wcbOhnePositionen = new WrapperCheckBox();
		wcbOhnePositionen.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.journal.ohnepositionen"));

		wrbSortierungPartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunden"));
		wbuPartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wbuPartner.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde.tooltip"));
		wbuFertigungsgruppe = new WrapperButton();
		wbuFertigungsgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.fertigungsgruppe")
				+ "...");

		wbuFertigungsgruppe
				.setActionCommand(this.ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wbuFertigungsgruppe.addActionListener(this);
		wtfFertigungsgruppe = new WrapperTextField();
		wtfFertigungsgruppe.setActivatable(false);
		wrbSortierungLiefertermin.setText("Liefertermin");
		wlaStichtag = new WrapperLabel();
		wdfStichtag = new WrapperDateField();
		wlaStichtag.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.liefertermin.bis"));
		wdfStichtag.setMandatoryField(true);
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.AUFTRAG_JOURNAL_OFFENE_POSITIONEN_STICHTAG);
		Long weeks = Long.parseLong(parametermandantDto.getCWert()) * 7 * 24
				* 60 * 60 * 1000;
		wdfStichtag.setDate(new Date(System.currentTimeMillis() + weeks));
		LinkedHashMap<Integer, String> hm = new LinkedHashMap<Integer, String>();
		hm.put(0, LPMain.getTextRespectUISPr("auft.menu.ansicht.alle")); // Alle
		// Auftraege
		hm.put(1, LPMain
				.getTextRespectUISPr("auft.journal.ohnerahmenauftraege")); // ohne
		// Rahmenauftraege
		// ,
		// d
		// .
		// h
		// .
		// Abrufe
		// und
		// freie
		// Auftraege
		hm
				.put(2, LPMain
						.getTextRespectUISPr("auft.journal.nurrahmenauftraege")); // nur
		// Rahmenauftraege
		wcoArt = new WrapperComboBox();
		wcoArt.setMandatoryField(true);
		wcoArt.setKeyOfSelectedItem(0);
		wcoArt.setMap(hm);

		jpaWorkingOn.add(wrbSortierungLiefertermin, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0, iZeile,
				4, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile,
				5, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbSortiereNachLiefertermin, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbSortiereNachAbliefertermin, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbOhnePositionen, new GridBagConstraints(0, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcoArt, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuFertigungsgruppe, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(wtfFertigungsgruppe, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		this.setEinschraenkungKostenstelleSichtbar(false);
		this.wrbSortierungBelegnummer.setVisible(false);
		this.setEinschraenkungDatumBelegnummerSichtbar(false);
		buttonGroupSortierung.add(wrbSortierungLiefertermin);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Object[] o = panelQueryFLRFertigungsgruppe.getSelectedIds();

				fertigungsgruppenIIds=new ArrayList<Integer>();

				if (o != null) {
					String s = "";
					for (int i = 0; i < o.length; i++) {
						FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
								.getInstance().getStuecklisteDelegate()
								.fertigungsgruppeFindByPrimaryKey(
										(Integer) o[i]);
						s += fertigungsgruppeDto.getCBez() + ", ";
						
						fertigungsgruppenIIds.add((Integer) o[i]);
						
					}
					wtfFertigungsgruppe.setText(s);
				} else {
					wtfFertigungsgruppe.setText(null);
					fertigungsgruppenIIds=new ArrayList<Integer>();
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				fertigungsgruppenIIds=new ArrayList<Integer>();
				wtfFertigungsgruppe.setText(null);
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			dialogQueryFertigungsgruppeFromListe(e);
		}
	}

	private void dialogQueryFertigungsgruppeFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance()
				.createPanelFLRFertigungsgruppe(getInternalFrame(), null, true);
		panelQueryFLRFertigungsgruppe.setMultipleRowSelectionEnabled(true);
		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN;
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
		return krit;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getAuftragReportDelegate()
				.printAuftragOffenePositionen(getKriterien(),
						wdfStichtag.getDate(),
						new Boolean(wcbSortiereNachLiefertermin.isSelected()),
						new Boolean(wcbOhnePositionen.isSelected()), 
						new Boolean(wcbSortiereNachAbliefertermin.isSelected()),
						(Integer[]) fertigungsgruppenIIds.toArray(new Integer[fertigungsgruppenIIds.size()]),
						(Integer) wcoArt.getKeyOfSelectedItem(),
						wrbSortierungLiefertermin.isSelected(), getReportname());
	}
}
