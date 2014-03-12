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
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportZaehlliste extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfLager = new WrapperTextField();
	private Integer lagerIId = null;
	private PanelQueryFLR panelQueryFLRLager = null;
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";
	private WrapperCheckBox wcbNurLagerbewirtschaftet = new WrapperCheckBox();
	private WrapperCheckBox wcbNurArtikelMitLagerstand = new WrapperCheckBox();
	private WrapperLabel wlaLagerwertVon = new WrapperLabel();
	private WrapperLabel wlaGestpreisBis = new WrapperLabel();
	private WrapperNumberField wnfAbLagerwert = new WrapperNumberField();
	private WrapperNumberField wnfAbGestpreis = new WrapperNumberField();

	private WrapperComboBox wcbArtikelarten = new WrapperComboBox();
	
	private WrapperCheckBox wcbMitVerstecktenArtikeln = new WrapperCheckBox();

	private ButtonGroup buttonGroupOption = new ButtonGroup();
	private WrapperRadioButton wrbSortierungArtikel = new WrapperRadioButton();
	private WrapperRadioButton wrbSortierungLagerort = new WrapperRadioButton();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	
	Integer lagerplatzIId = null;

	private WrapperButton wbuLagerplatz = new WrapperButton();
	private WrapperTextField wtfLagerplatz = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRLagerplatz = null;
	static final public String ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE = "action_lagerplatz_from_liste";

	static final private String ACTION_SPECIAL_FLR_ARTIKELGRUPPE = "ACTION_SPECIAL_FLR_ARTIKELGRUPPE";
	static final private String ACTION_SPECIAL_FLR_ARTIKELKLASSE = "ACTION_SPECIAL_FLR_ARTIKELKLASSE";
	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private PanelQueryFLR panelQueryFLRArtikelklasse = null;
	Integer artikelgruppeIId = null;
	Integer artikelklasseIId = null;
	private WrapperButton wbuArtikelgruppeFLR = null;
	private WrapperTextField wtfArtikelgruppe = new WrapperTextField();
	private WrapperButton wbuArtikelklasseFLR = null;
	private WrapperTextField wtfArtikelklasse = new WrapperTextField();

	public ReportZaehlliste(InternalFrameArtikel internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("artikel.report.zaehlliste");
		jbInit();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLager;
	}

	private void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wbuLager.setText(LPMain.getTextRespectUISPr("button.lager"));
		wtfLager.setActivatable(false);
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);

		wrbSortierungLagerort.setSelected(true);

		wlaLagerwertVon.setText(LPMain
				.getTextRespectUISPr("artikel.inventur.ablagerwert"));
		wlaGestpreisBis.setText(LPMain
				.getTextRespectUISPr("artikel.inventur.abgestpreis"));

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wcbNurArtikelMitLagerstand
				.setText(LPMain
						.getTextRespectUISPr("artikel.inventur.lagerstandgroessernull"));
		wcbNurLagerbewirtschaftet
				.setText(LPMain
						.getTextRespectUISPr("artikel.inventur.nurlagerbewirtschaftete"));
		wcbNurLagerbewirtschaftet.setShort(Helper.boolean2Short(true));

		wrbSortierungArtikel.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer"));
		wrbSortierungLagerort.setText(LPMain
				.getTextRespectUISPr("artikel.lagerort"));

		wcbMitVerstecktenArtikeln.setText(LPMain
				.getTextRespectUISPr("lp.versteckte"));

		
		wcbArtikelarten.setMandatoryField(true);

		Map<Integer, String> m = new TreeMap<Integer, String>();
		m.put(LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_ALLE, LPMain
				.getTextRespectUISPr("artikel.lagerstandsliste.alle"));
		m.put(LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_OHNESTKL, LPMain
				.getTextRespectUISPr("artikel.lagerstandsliste.ohnestkl"));
		m.put(LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_NURSTKL, LPMain
				.getTextRespectUISPr("artikel.lagerstandsliste.nurstkl"));
		wcbArtikelarten.setMap(m);

		
		wbuArtikelgruppeFLR = new WrapperButton();
		wbuArtikelgruppeFLR.setText(LPMain
				.getTextRespectUISPr("button.artikelgruppe"));
		wbuArtikelgruppeFLR.setMandatoryField(true);
		wbuArtikelgruppeFLR.setActionCommand(ACTION_SPECIAL_FLR_ARTIKELGRUPPE);
		wbuArtikelgruppeFLR.addActionListener(this);

		wbuArtikelklasseFLR = new WrapperButton();
		wbuArtikelklasseFLR.setText(LPMain
				.getTextRespectUISPr("button.artikelklasse"));
		wbuArtikelklasseFLR.setMandatoryField(true);
		wbuArtikelklasseFLR.setActionCommand(ACTION_SPECIAL_FLR_ARTIKELKLASSE);
		wbuArtikelklasseFLR.addActionListener(this);

		wtfArtikelgruppe.setActivatable(false);
		wtfArtikelklasse.setActivatable(false);
		wtfArtikelgruppe.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfArtikelklasse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuLagerplatz.setText(LPMain.getTextRespectUISPr("lp.lagerplatz"));

		wbuLagerplatz.setActionCommand(ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE);
		wbuLagerplatz.addActionListener(this);
		wtfLagerplatz.setActivatable(false);
		
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, iZeile, 2, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, iZeile, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wtfArtikelgruppe, new GridBagConstraints(1, iZeile, 2,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikelgruppeFLR, new GridBagConstraints(0, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wtfArtikelklasse, new GridBagConstraints(1, iZeile, 2,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikelklasseFLR, new GridBagConstraints(0, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wtfLagerplatz, new GridBagConstraints(1, iZeile, 2,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuLagerplatz, new GridBagConstraints(0, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wcbNurLagerbewirtschaftet, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));

		jpaWorkingOn.add(wcbNurArtikelMitLagerstand, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wcbMitVerstecktenArtikeln, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbArtikelarten, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaLagerwertVon, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAbLagerwert, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	

		iZeile++;

		jpaWorkingOn.add(wlaGestpreisBis, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfAbGestpreis, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbSortierungLagerort, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortierungArtikel, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		buttonGroupOption.add(wrbSortierungArtikel);
		buttonGroupOption.add(wrbSortierungLagerort);
	}

	public String getModul() {
		return ArtikelFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LagerFac.REPORT_ZAEHLLISTE;
	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager_MitLeerenButton(getInternalFrame());

		new DialogQuery(panelQueryFLRLager);
	}
	public void dialogQueryLagerplatzFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLagerplatz = ArtikelFilterFactory.getInstance()
				.createPanelFLRLagerplatz(getInternalFrame(), lagerplatzIId, true);

		new DialogQuery(panelQueryFLRLagerplatz);
	}
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfLager.setText(lagerDto.getCNr());
				lagerIId = lagerDto.getIId();
				wtfLager.setText(lagerDto.getCNr());
			} else if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtgruDto artgruDto = DelegateFactory.getInstance()
						.getArtikelDelegate().artgruFindByPrimaryKey(
								(Integer) key);
				wtfArtikelgruppe.setText(artgruDto.getCNr());
				artikelgruppeIId = (Integer) key;
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtklaDto artklaDto = DelegateFactory.getInstance()
						.getArtikelDelegate().artklaFindByPrimaryKey(
								(Integer) key);
				wtfArtikelklasse.setText(artklaDto.getCNr());
				artikelklasseIId = (Integer) key;

			}
			else if (e.getSource() == panelQueryFLRLagerplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				LagerplatzDto lagerplatzDto = DelegateFactory.getInstance()
						.getLagerDelegate().lagerplatzFindByPrimaryKey(
								(Integer) key);
				wtfLagerplatz.setText(lagerplatzDto.getCLagerplatz());
				lagerplatzIId = lagerplatzDto.getIId();

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLager) {
				wtfLager.setText(null);
				lagerIId = null;
			} else if (e.getSource() == panelQueryFLRArtikelgruppe) {
				artikelgruppeIId = null;
				wtfArtikelgruppe.setText("");
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				artikelklasseIId = null;
				wtfArtikelklasse.setText("");
			}else if (e.getSource() == panelQueryFLRLagerplatz) {
				lagerplatzIId = null;
				wtfLagerplatz.setText("");
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FLR_ARTIKELGRUPPE)) {
			panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelgruppe(getInternalFrame(),
							artikelgruppeIId);
			new DialogQuery(panelQueryFLRArtikelgruppe);

		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FLR_ARTIKELKLASSE)) {
			panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelklasse(getInternalFrame(),
							artikelklasseIId);
			new DialogQuery(panelQueryFLRArtikelklasse);

		}
		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE)) {
			dialogQueryLagerplatzFromListe(e);
		} 
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		
		
		
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
		
		
		
		return DelegateFactory.getInstance().getLagerReportDelegate()
				.printZaehlliste(lagerIId, wnfAbLagerwert.getBigDecimal(),
						wnfAbGestpreis.getBigDecimal(),
						new Boolean(wcbNurLagerbewirtschaftet.isSelected()),
						new Boolean(wcbNurArtikelMitLagerstand.isSelected()),
						wrbSortierungLagerort.isSelected(),
						wcbMitVerstecktenArtikeln.isSelected(),
						artikelgruppeIId, artikelklasseIId, lagerplatzIId,iArtikelarten);
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
