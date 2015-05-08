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

public class ReportAllergene extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperLabel wlaStichtag = new WrapperLabel();
	private Integer artikelIId_Von = null;
	private Integer artikelIId_Bis = null;
	private PanelQueryFLR panelQueryFLRArtikel_Von = null;
	private PanelQueryFLR panelQueryFLRArtikel_Bis = null;

	static final public String ACTION_SPECIAL_ARTIKELVON_FROM_LISTE = "ACTION_SPECIAL_ARTIKELVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE = "ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE";

	private WrapperButton wbuArtikelnrVon = new WrapperButton();
	private WrapperButton wbuArtikelnrBis = new WrapperButton();
	private WrapperTextField wtfArtikelnrVon = new WrapperTextField();
	private WrapperTextField wtfArtikelnrBis = new WrapperTextField();

	public ReportAllergene(InternalFrameArtikel internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("artikel.allergen");

		jbInit();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuArtikelnrVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaStichtag.setText(LPMain.getTextRespectUISPr("lp.stichtag"));

		wbuArtikelnrVon.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.von"));
		wbuArtikelnrBis.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.bis"));

		// wbuLager.setEnabled(false);
		wbuArtikelnrVon.setActionCommand(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE);
		wbuArtikelnrVon.addActionListener(this);

		wbuArtikelnrBis.setActionCommand(ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE);
		wbuArtikelnrBis.addActionListener(this);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		int iZeile = 0;

		iZeile++;
		jpaWorkingOn.add(wbuArtikelnrVon, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfArtikelnrVon, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 240, 0));
		jpaWorkingOn.add(wbuArtikelnrBis, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfArtikelnrBis, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 240, 0));

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
		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory
				.getInstance()
				.getArtikelReportDelegate()
				.printAlergene(wtfArtikelnrVon.getText(),
						wtfArtikelnrBis.getText());
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
