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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.partner.service.LieferantReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportArtikeldesLieferanten extends PanelBasis implements
		PanelReportIfJRDS {
	/**
  	 * 
  	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaStichtag = new WrapperLabel();
	private WrapperDateField wdfStichtag = new WrapperDateField();
	private WrapperSelectField wsfLieferant = null;

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private JRadioButton wrbSortArtikelnr = new JRadioButton();
	private JRadioButton wrbSortBezeichnung = new JRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private WrapperCheckBox wcbVersteckte = null;
	private WrapperCheckBox wcbSortiertNachLieferant = null;
	private WrapperCheckBox wcbNurLagerbewirtschaftete = null;

	public ReportArtikeldesLieferanten(InternalFrameLieferant internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr(
				"lieferant.report.artikeldeslieferanten");
		jbInit();
		initComponents();

		if (internalFrame.getLieferantDto() != null) {
			wsfLieferant.setKey(internalFrame.getLieferantDto().getIId());
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	private void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wsfLieferant = new WrapperSelectField(WrapperSelectField.LIEFERANT,
				getInternalFrame(), true);

		wrbSortArtikelnr.setSelected(true);
		wrbSortArtikelnr.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer"));

		wrbSortBezeichnung
				.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));

		wlaStichtag.setText(LPMain.getTextRespectUISPr("lp.stichtag"));

		wcbVersteckte = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.versteckte"));

		wcbSortiertNachLieferant = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("part.report.sortiertnachlieferant"));
		wcbNurLagerbewirtschaftete = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("part.report.artikeldeslieferante.nurlagerbewirtschaftete"));

		buttonGroupSortierung.add(wrbSortArtikelnr);

		buttonGroupSortierung.add(wrbSortBezeichnung);

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		jpaWorkingOn.add(wsfLieferant.getWrapperTextField(),
				new GridBagConstraints(2, 2, 3, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperButton(),
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(2, 3, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(0, 3, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbSortiertNachLieferant, new GridBagConstraints(3, 3,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, 4, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortArtikelnr, new GridBagConstraints(2, 4, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbVersteckte, new GridBagConstraints(3, 4, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortBezeichnung, new GridBagConstraints(2, 5, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbNurLagerbewirtschaftete, new GridBagConstraints(3,
				5, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return ArtikelReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LieferantReportFac.REPORT_ARTIKELDESLIEFERANTEN;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getLieferantDelegate()
				.printArtikeldesLieferanten(wsfLieferant.getIKey(),
						wrbSortBezeichnung.isSelected(),
						wcbVersteckte.isSelected(),
						wcbSortiertNachLieferant.isSelected(),
						wcbNurLagerbewirtschaftete.isSelected(),
						wdfStichtag.getTimestamp());
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
