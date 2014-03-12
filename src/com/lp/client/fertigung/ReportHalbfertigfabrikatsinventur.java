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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich um den Druck der Ausgabe Liste</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 19.10.05</p>
 * 
 * <p>@author $Author: robert $</p>
 * 
 * @version not attributable Date $Date: 2012/10/19 13:19:03 $
 */
public class ReportHalbfertigfabrikatsinventur extends PanelBasis implements
		PanelReportIfJRDS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaStichtag = new WrapperLabel();
	private WrapperDateField wdfStichtag = new WrapperDateField();
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperCheckBox wcbVerdichtet = new WrapperCheckBox();
	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperButton wbuFertigungsort = new WrapperButton();
	private WrapperTextField wtfFertigungsort = new WrapperTextField();
	private static final String ACTION_SPECIAL_FERTIGUNGSORT = "action_special_los_fertigungsort";
	private PanelQueryFLR panelQueryFLRFertigungsort = null;
	private ButtonGroup buttonGroupOption = new ButtonGroup();
	private WrapperRadioButton wrbOptionLosnummer = new WrapperRadioButton();
	private WrapperRadioButton wrbOptionArtikelnummer = new WrapperRadioButton();
	private Integer partnerIId_Fertigungsort = null;

	public ReportHalbfertigfabrikatsinventur(InternalFrame internalFrame,
			String sAdd2Title) throws Throwable {
		// reporttitel: das PanelReport kriegt einen Titel, der wird vom
		// Framework hergenommen
		super(internalFrame, sAdd2Title);
		jbInit();
		initComponents();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FERTIGUNGSORT)) {
			dialogQueryFertigungsort(e);
		}
	}

	private void dialogQueryFertigungsort(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH , PanelBasis.ACTION_LEEREN };

		panelQueryFLRFertigungsort = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_MANDANT, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"title.mandantauswahlliste"));

		new DialogQuery(panelQueryFLRFertigungsort);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRFertigungsort) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					MandantDto mandantDto = DelegateFactory.getInstance()
							.getMandantDelegate().mandantFindByPrimaryKey(
									(String) key);

					PartnerDto partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate().partnerFindByPrimaryKey(
									mandantDto.getPartnerIId());
					wtfFertigungsort.setText(partnerDto.formatTitelAnrede());
					partnerIId_Fertigungsort=mandantDto.getPartnerIId();
				}
			}
		}else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			 if (e.getSource() == panelQueryFLRFertigungsort) {
				 wtfFertigungsort.setText(null);
				 partnerIId_Fertigungsort=null;
			 }
		}

	}

	/**
	 * jbInit
	 */
	private void jbInit() {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaStichtag.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.stichtag"));

		wbuFertigungsort.addActionListener(this);
		wbuFertigungsort.setActionCommand(ACTION_SPECIAL_FERTIGUNGSORT);
		wtfFertigungsort.setActivatable(false);

		  wbuFertigungsort.setText(LPMain.getInstance().getTextRespectUISPr(
	        "button.fertigungsort"));
		
		wcbVerdichtet.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.verdichtet"));
		wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.sortierung"));
		wrbOptionArtikelnummer.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.artikelnummer"));
		wrbOptionLosnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.losnummer"));
		buttonGroupOption.add(wrbOptionArtikelnummer);
		buttonGroupOption.add(wrbOptionLosnummer);
		wrbOptionArtikelnummer.setSelected(true);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		int iZeile = 0;
		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(1, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbOptionArtikelnummer, new GridBagConstraints(3,
				iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbVerdichtet, new GridBagConstraints(1, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbOptionLosnummer, new GridBagConstraints(3, iZeile,
				1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuFertigungsort, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFertigungsort, new GridBagConstraints(1, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_HALBFERTIGFABRIKATSINVENTUR;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iSortierung = -1;

		if (wrbOptionArtikelnummer.isSelected()) {
			iSortierung = FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR;
		} else if (wrbOptionLosnummer.isSelected()) {
			iSortierung = FertigungReportFac.HF_OPTION_SORTIERUNG_LOSNR;
		}

		return DelegateFactory.getInstance().getFertigungDelegate()
				.printHalbfertigfabrikatsinventur(wdfStichtag.getTimestamp(),
						iSortierung, wcbVerdichtet.isSelected(), partnerIId_Fertigungsort);
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}
}
