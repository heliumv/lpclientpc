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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich um den Druck der Ausgabe Liste</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 19.10.05</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2012/10/23 07:47:40 $
 */
public class ReportAusgabeliste extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Integer> losIId = new ArrayList<Integer>();
	private WrapperLabel wlaSortierung = null;
	private WrapperRadioButton wrbMontageartSchale = null;
	private WrapperRadioButton wrbArtikelklasse = null;
	private WrapperRadioButton wrbLagerLagerort = null;
	private WrapperRadioButton wrbLagerortLager = null;
	private WrapperRadioButton wrbIdent = null;
	private WrapperRadioButton wrbArtikelbezeichnung = null;
	private JPanel jpaWorkingOn = null;
	private ButtonGroup bgSortierung = null;
	private WrapperCheckBox wcoVerdichtetNachIdent = null;
	private WrapperCheckBox wcoVorrangigNachFarbcodeSortiert = null;
	private WrapperCheckBox wcoMaterial = null;
	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();
	boolean bAlternativerReport = false;
	private Integer formularnummer = null;

	private static final String ACTION_SPECIAL_LEEREN = "action_special_leeren";
	private ImageIcon imageIconLeeren = null;

	private String ACTION_SORTIERUNG_ARTIKELKASSE = "ACTION_SORTIERUNG_ARTIKELKASSE";
	private String ACTION_SORTIERUNG_LAGERORT = "ACTION_SORTIERUNG_LAGERORT";
	private String ACTION_SORTIERUNG_LAGERORT_LAGER = "ACTION_SORTIERUNG_LAGERORT_LAGER";
	private String ACTION_SORTIERUNG_MONTAGEART = "ACTION_SORTIERUNG_MONTAGEART";
	private String ACTION_SORTIERUNG_IDENT = "ACTION_SORTIERUNG_MONTAGEART";
	private String ACTION_SORTIERUNG_ARTIKELBEZEICHNUNG = "ACTION_SORTIERUNG_ARTIKELBEZEICHNUNG";
	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_auftrag_los_liste";
	private PanelQueryFLR panelQueryFLRLos = null;

	public ReportAusgabeliste(InternalFrame internalFrame, Integer losIId,
			String sAdd2Title, boolean bAlternativerReport) throws Throwable {
		// reporttitel: das PanelReport kriegt einen Titel, der wird vom
		// Framework hergenommen
		super(internalFrame, sAdd2Title);
		this.bAlternativerReport = bAlternativerReport;
		this.losIId.add(losIId);
		jbInit();
		if (losIId != null) {
			LosDto losDto = DelegateFactory.getInstance()
					.getFertigungDelegate().losFindByPrimaryKey(losIId);
			wtfLos.setText(losDto.getCNr());

			formularnummer = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.fertigungsgruppeFindByPrimaryKey(
							losDto.getFertigungsgruppeIId())
					.getIFormularnummer();

		}

		setDefaults();
		initComponents();
	}

	/**
	 * setDefaults
	 */
	private void setDefaults() {
		wrbIdent.setSelected(true);
	}

	private ImageIcon getImageIconLeeren() {
		if (imageIconLeeren == null) {
			imageIconLeeren = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/leeren.png"));
		}
		return imageIconLeeren;
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		Integer selectedId = null;
		if (losIId != null && losIId.size() > 0) {
			selectedId = (Integer) losIId.get(0);
		}

		panelQueryFLRLos = FertigungFilterFactory.getInstance()
				.createPanelFLRBebuchbareLose(getInternalFrame(), false, true,
						false, selectedId, false);
		panelQueryFLRLos.setMultipleRowSelectionEnabled(true);
		new DialogQuery(panelQueryFLRLos);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}

		else if (e.getActionCommand().equals(ACTION_SORTIERUNG_ARTIKELKASSE)) {
			wcoMaterial.setEnabled(true);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LEEREN)) {
			wtfLos.setText(null);
			losIId = new ArrayList<Integer>();
		} else {
			wcoMaterial.setEnabled(false);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Object[] o = panelQueryFLRLos.getSelectedIds();

				String lose = "";

				if (wtfLos.getText() != null) {
					lose = wtfLos.getText();
				}
				for (int i = 0; i < o.length; i++) {
					LosDto losDto = DelegateFactory.getInstance()
							.getFertigungDelegate()
							.losFindByPrimaryKey((Integer) o[i]);
					lose += losDto.getCNr() + ", ";

					losIId.add(losDto.getIId());

				}
				wtfLos.setText(lose);
			}
		}
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn = new JPanel(new GridBagLayout());
		wlaSortierung = new WrapperLabel();
		wrbMontageartSchale = new WrapperRadioButton();
		wrbArtikelklasse = new WrapperRadioButton();
		wrbLagerLagerort = new WrapperRadioButton();
		wrbLagerortLager = new WrapperRadioButton();
		wrbIdent = new WrapperRadioButton();
		wrbArtikelbezeichnung = new WrapperRadioButton();

		JButton jbuSetNull = new JButton();
		jbuSetNull.setActionCommand(ACTION_SPECIAL_LEEREN);
		jbuSetNull.addActionListener(this);
		jbuSetNull.setIcon(getImageIconLeeren());
		jbuSetNull
				.setMinimumSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));
		jbuSetNull
				.setPreferredSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));

		wrbArtikelklasse.setActionCommand(ACTION_SORTIERUNG_ARTIKELKASSE);
		wrbIdent.setActionCommand(ACTION_SORTIERUNG_IDENT);
		wrbArtikelbezeichnung
				.setActionCommand(ACTION_SORTIERUNG_ARTIKELBEZEICHNUNG);
		wrbLagerLagerort.setActionCommand(ACTION_SORTIERUNG_LAGERORT);
		wrbMontageartSchale.setActionCommand(ACTION_SORTIERUNG_MONTAGEART);
		wrbLagerortLager.setActionCommand(ACTION_SORTIERUNG_LAGERORT_LAGER);

		wrbArtikelklasse.addActionListener(this);
		wrbIdent.addActionListener(this);
		wrbArtikelbezeichnung.addActionListener(this);
		wrbLagerLagerort.addActionListener(this);
		wrbMontageartSchale.addActionListener(this);

		bgSortierung = new ButtonGroup();
		wcoVerdichtetNachIdent = new WrapperCheckBox();
		wcoVorrangigNachFarbcodeSortiert = new WrapperCheckBox();

		bgSortierung.add(wrbMontageartSchale);
		bgSortierung.add(wrbArtikelklasse);
		bgSortierung.add(wrbLagerLagerort);
		bgSortierung.add(wrbLagerortLager);
		bgSortierung.add(wrbIdent);
		bgSortierung.add(wrbArtikelbezeichnung);

		wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.sortierung"));
		wrbMontageartSchale.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.montageartschale"));
		wrbArtikelklasse.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelklasse"));
		wrbLagerLagerort.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.lagerlagerort"));
		wrbLagerortLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.lagerortlager"));
		wrbIdent.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.ident"));
		wrbArtikelbezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.ausgabeliste.sortierung.artikelbezeichnung"));

		wcoVorrangigNachFarbcodeSortiert.setText(LPMain.getInstance()
				.getTextRespectUISPr("los.ausgabeliste.sortiertnachfarbcode"));

		wcoVerdichtetNachIdent.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.verdichtetartikelnummer"));
		wcoVerdichtetNachIdent.setSelected(true);
		wcoMaterial = new WrapperCheckBox();
		wcoMaterial.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.tab.oben.material.title"));
		wcoMaterial.setSelected(true);
		wcoMaterial.setEnabled(false);

		wbuLos.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.lose")
				+ "...");

		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		wtfLos.setMandatoryField(true);
		wtfLos.setActivatable(false);
		wtfLos.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(jbuSetNull, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0.4, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbMontageartSchale, new GridBagConstraints(1, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoVorrangigNachFarbcodeSortiert,
				new GridBagConstraints(2, iZeile, 2, 1, 0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbArtikelklasse, new GridBagConstraints(1, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoMaterial, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbLagerLagerort, new GridBagConstraints(1, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbLagerortLager, new GridBagConstraints(1, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbIdent, new GridBagConstraints(1, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoVerdichtetNachIdent, new GridBagConstraints(2,
				iZeile, 1, 1, 1.5, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbArtikelbezeichnung, new GridBagConstraints(1,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		if (bAlternativerReport == true) {
			return FertigungReportFac.REPORT_AUSGABELISTE2;
		} else {

			String report = FertigungReportFac.REPORT_AUSGABELISTE;
			if (formularnummer != null) {
				report = report.replace(".", formularnummer + ".");
			}
			return report;

		}
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Integer iSortierung = null;
		if (wrbArtikelklasse.isSelected()) {
			if (wcoMaterial.isSelected()) {
				iSortierung = new Integer(
						Helper.SORTIERUNG_NACH_ARTIKELKLASSE_UND_MATERIAL);
			} else {
				iSortierung = new Integer(Helper.SORTIERUNG_NACH_ARTIKELKLASSE);
			}
		} else if (wrbIdent.isSelected()) {
			iSortierung = new Integer(Helper.SORTIERUNG_NACH_IDENT);
		} else if (wrbArtikelbezeichnung.isSelected()) {
			iSortierung = new Integer(Helper.SORTIERUNG_NACH_ARTIKELBEZEICHNUNG);
		} else if (wrbLagerLagerort.isSelected()) {
			iSortierung = new Integer(Helper.SORTIERUNG_NACH_LAGER_UND_LAGERORT);
		} else if (wrbLagerortLager.isSelected()) {
			iSortierung = new Integer(Helper.SORTIERUNG_NACH_LAGERORT_UND_LAGER);
		} else if (wrbMontageartSchale.isSelected()) {
			iSortierung = new Integer(
					Helper.SORTIERUNG_NACH_MONTAGEART_UND_SCHALE);
		}

		String alternativerReport = null;
		if (bAlternativerReport == true) {
			alternativerReport = FertigungReportFac.REPORT_AUSGABELISTE2;
		}

		return DelegateFactory
				.getInstance()
				.getFertigungDelegate()
				.printAusgabeListe(
						(Integer[]) losIId.toArray(new Integer[losIId.size()]),
						iSortierung,
						Helper.short2Boolean(wcoVerdichtetNachIdent.getShort()),
						wcoVorrangigNachFarbcodeSortiert.isSelected(), null,
						alternativerReport);
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wrbLagerLagerort;
	}
}
