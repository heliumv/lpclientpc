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
package com.lp.client.bestellung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.EventObject;
import java.util.LinkedHashMap;

import javax.swing.JComponent;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalEinkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportBestellungWareneingang extends PanelReportJournalEinkauf
		implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String ACTION_SPECIAL_ARTIKELKLASSE = "action_special_artikelklasse";
	private final static String ACTION_SPECIAL_ARTIKELGRUPPE = "action_special_artikelgruppe";
	private final static String ACTION_SPECIAL_AUFTRAG = "action_special_auftrag";
	private final static String ACTION_SPECIAL_ARTIKEL_VON = "action_special_artikel_von";
	private final static String ACTION_SPECIAL_ARTIKEL_BIS = "action_special_artikel_bis";

	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private PanelQueryFLR panelQueryFLRArtikelklasse = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRArtikelVon = null;
	private PanelQueryFLR panelQueryFLRArtikelBis = null;

	private WrapperRadioButton wrbSortiereNachProjekt = null;
	private WrapperRadioButton wrbSortiereNachIdent = null;
	private WrapperRadioButton wrbSortiereNachDatum = null;
	private WrapperButton wbuArtikelklasse = null;
	private WrapperButton wbuArtikelgruppe = null;
	private WrapperTextField wtfArtikelklasse = null;
	private WrapperTextField wtfArtikelgruppe = null;

	private WrapperButton wbuAuftrag = null;
	private WrapperTextField wtfAuftrag = null;
	private WrapperButton wbuArtikelVon = null;
	private WrapperButton wbuArtikelBis = null;
	private WrapperTextField wtfArtikelVon = null;
	private WrapperTextField wtfArtikelBis = null;
	private WrapperLabel wlaProjekt = null;
	private WrapperTextField wtfProjekt = null;

	private Integer artikelklasseIId = null;
	private Integer artikelgruppeIId = null;
	private Integer auftragIId = null;
	private Integer artikelIIdVon = null;
	private Integer artikelIIdBis = null;

	private WrapperCheckBox wcbMitWarenverbrauch = null;
	
	public ReportBestellungWareneingang(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
	}

	private void jbInit() throws Exception {

		wrbSortiereNachProjekt = new WrapperRadioButton(LPMain
				.getTextRespectUISPr("label.projekt"));
		buttonGroupSortierung.add(wrbSortiereNachProjekt);
		
		wrbSortiereNachIdent=new WrapperRadioButton(LPMain
				.getTextRespectUISPr("lp.artikel"));
		buttonGroupSortierung.add(wrbSortiereNachIdent);

		wrbSortiereNachDatum = new WrapperRadioButton(LPMain
				.getTextRespectUISPr("lp.datum"));
		buttonGroupSortierung.add(wrbSortiereNachDatum);

		wbuArtikelklasse = new WrapperButton(LPMain
				.getTextRespectUISPr("button.artikelklasse"));
		wbuArtikelklasse.setToolTipText(LPMain
				.getTextRespectUISPr("button.artikelklasse.tooltip"));
		wbuArtikelklasse.setMinimumSize(new Dimension(BREITE_BUTTONS, Defaults
				.getInstance().getControlHeight()));
		wbuArtikelklasse.setPreferredSize(new Dimension(BREITE_BUTTONS,
				Defaults.getInstance().getControlHeight()));
		wbuArtikelgruppe = new WrapperButton(LPMain
				.getTextRespectUISPr("button.artikelgruppe"));
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

		wcbMitWarenverbrauch=new WrapperCheckBox(LPMain
				.getTextRespectUISPr("bes.wareneingangsjournal.mitwarenverbrauch"));
		
		wbuAuftrag = new WrapperButton(LPMain
				.getTextRespectUISPr("button.auftrag"));
		wbuAuftrag.setToolTipText(LPMain
				.getTextRespectUISPr("button.auftrag.tooltip"));
		wbuAuftrag.addActionListener(this);
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG);
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
		wtfAuftrag = new WrapperTextField();
		wtfArtikelVon.setActivatable(false);
		wtfArtikelBis.setActivatable(false);
		wtfAuftrag.setActivatable(false);
		wlaProjekt = new WrapperLabel(LPMain
				.getTextRespectUISPr("label.projekt"));
		wtfProjekt = new WrapperTextField();

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
		jpaWorkingOn.add(wrbSortiereNachDatum, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitWarenverbrauch, new GridBagConstraints(3, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortiereNachIdent, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(3, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuArtikelklasse, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelklasse, new GridBagConstraints(3, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuArtikelgruppe, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelgruppe, new GridBagConstraints(3, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
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
	}

	public String getModul() {
		return BestellungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_WARENEINGANG;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
		befuelleKriterien(krit);
		if (wrbSortiereNachProjekt.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT;
		}
		if (wrbSortiereNachDatum.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_DATUM;
		}
		if (wrbSortiereNachIdent.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT;
		}
		return DelegateFactory.getInstance().getBestellungDelegate()
				.printBestellungWareneingangsJournal(krit, artikelklasseIId,
						artikelgruppeIId, wtfArtikelVon.getText(),
						wtfArtikelBis.getText(), wtfProjekt.getText(),
						auftragIId,wcbMitWarenverbrauch.isSelected());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELGRUPPE)) {
			dialogQueryArtikelgruppe();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELKLASSE)) {
			dialogQueryArtikelklasse();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG)) {
			dialogQueryAuftrag();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_BIS)) {
			dialogQueryArtikelBis();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_VON)) {
			dialogQueryArtikelVon();
		}
	}

	private void dialogQueryArtikelgruppe() throws Throwable {
		panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikelgruppe(getInternalFrame(),
						artikelgruppeIId);
		new DialogQuery(panelQueryFLRArtikelgruppe);
	}

	private void dialogQueryAuftrag() throws Throwable {
		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), true, true, null,
						auftragIId);
		new DialogQuery(panelQueryFLRAuftrag);
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

	private void dialogQueryArtikelklasse() throws Throwable {
		panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikelklasse(getInternalFrame(),
						artikelklasseIId);
		new DialogQuery(panelQueryFLRArtikelklasse);
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
							.getArtikelDelegate().artgruFindByPrimaryKey(
									artikelgruppeIId);
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
							.getArtikelDelegate().artklaFindByPrimaryKey(
									artikelklasseIId);
					if (artikelklasseDto.getArtklasprDto() != null) {
						wtfArtikelklasse.setText(artikelklasseDto
								.getArtklasprDto().getCBez());
					} else {
						wtfArtikelklasse.setText(artikelklasseDto.getCNr());
					}
				}
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					auftragIId = (Integer) key;
					AuftragDto auftragDto = DelegateFactory.getInstance()
							.getAuftragDelegate().auftragFindByPrimaryKey(
									auftragIId);
					wtfAuftrag.setText(auftragDto.getCNr());
				}
			} else if (e.getSource() == panelQueryFLRArtikelBis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					artikelIIdBis = (Integer) key;
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate().artikelFindByPrimaryKey(
									artikelIIdBis);
					wtfArtikelBis.setText(artikelDto.getCNr());
				}
			} else if (e.getSource() == panelQueryFLRArtikelVon) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					artikelIIdVon = (Integer) key;
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate().artikelFindByPrimaryKey(
									artikelIIdVon);
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
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				auftragIId = null;
				wtfAuftrag.setText(null);
			} else if (e.getSource() == panelQueryFLRArtikelBis) {
				artikelIIdBis = null;
				wtfArtikelBis.setText(null);
			} else if (e.getSource() == panelQueryFLRArtikelVon) {
				artikelIIdVon = null;
				wtfArtikelVon.setText(null);
			}
		}

	}
}
