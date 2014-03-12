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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.LieferantReportFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportLieferantenliste extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = new JPanel(new GridBagLayout());
	private WrapperCheckBox wcbVersteckte = new WrapperCheckBox();
	private WrapperCheckBox wcbMitMoeglichenLieferanten = new WrapperCheckBox();
	private WrapperCheckBox wcbMitAnsprechpartner = new WrapperCheckBox();
	private WrapperCheckBox wcbNurSelektierterLieferant = new WrapperCheckBox();
	private WrapperCheckBox wcbNurFreigegebeneLieferanten = new WrapperCheckBox();
	protected WrapperButton wbuPartnerklasse = null;
	protected WrapperTextField wtfPartnerklasse = null;
	protected WrapperButton wbuBranche = null;
	protected WrapperTextField wtfBranche = null;
	protected PanelQueryFLR panelQueryFLRBranche = null;
	protected PanelQueryFLR panelQueryFLRPartnerklasse = null;

	private WrapperTextField wtfPLZ = null;
	private WrapperLabel wlaPLZ = null;

	private WrapperButton wbuLKZ = null;
	private WrapperTextField wtfLKZ = null;

	Integer landIId = null;
	Integer partnerklasseIId = null;
	Integer brancheIId = null;

	private PanelQueryFLR panelQueryFLRLand = null;
	static final public String ACTION_SPECIAL_FLR_LAND = "action_special_flr_land";
	static final private String ACTION_SPECIAL_FLR_BRANCHE = "action_special_flr_branche";
	static final private String ACTION_SPECIAL_FLR_PARTNERKLASSE = "action_special_flr_partnerklasse";

	private WrapperSelectField wsfLiefergruppe = new WrapperSelectField(
			WrapperSelectField.LIEFERGRUPPE, getInternalFrame(), true);

	InternalFrameLieferant internalFrameLieferant = null;

	public ReportLieferantenliste(
			InternalFrameLieferant internalFrameLieferant, String sAdd2Title)
			throws Throwable {
		super(internalFrameLieferant, sAdd2Title);
		this.internalFrameLieferant = internalFrameLieferant;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() {
		// Default: beide Datumsfelder leer -> also alles
		// wdrBereich.doClickUp();

	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		wcbVersteckte.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.versteckte"));
		wcbMitMoeglichenLieferanten.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"part.kunde.lieferantenliste.mitmoeglichen"));
		wcbMitAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mitansprechpartner"));

		wcbNurSelektierterLieferant.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"part.kunde.lieferantenliste.nurselektierter"));
		wcbNurFreigegebeneLieferanten.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"part.kunde.lieferantenliste.nurfreigegebene"));

		wlaPLZ = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.plz.a"));
		wtfPLZ = new WrapperTextField(SystemFac.MAX_PLZ);

		wbuLKZ = new WrapperButton(LPMain.getInstance().getTextRespectUISPr(
				"lp.land.flr"));
		wtfLKZ = new WrapperTextField(SystemFac.MAX_LKZ);
		wtfLKZ.setActivatable(false);
		wbuLKZ.addActionListener(this);
		wbuLKZ.setActionCommand(ACTION_SPECIAL_FLR_LAND);

		wbuBranche = new WrapperButton();
		wbuBranche.setActionCommand(ACTION_SPECIAL_FLR_BRANCHE);

		wbuBranche.addActionListener(this);

		wbuBranche.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.branche"));

		wtfBranche = new WrapperTextField(PartnerFac.MAX_BRANCHE);
		wtfBranche.setActivatable(false);

		wbuPartnerklasse = new WrapperButton();
		wbuPartnerklasse.setActionCommand(ACTION_SPECIAL_FLR_PARTNERKLASSE);

		wbuPartnerklasse.addActionListener(this);

		wbuPartnerklasse.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.partnerklasse"));

		wtfPartnerklasse = new WrapperTextField(PartnerFac.MAX_BRANCHE);
		wtfPartnerklasse.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);
		int iZeile = 0;
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		jpaWorkingOn.add(wcbMitMoeglichenLieferanten, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbVersteckte, new GridBagConstraints(0, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		}

		jpaWorkingOn.add(wcbMitAnsprechpartner, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(wcbNurSelektierterLieferant, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 120, 0));

		iZeile++;
		jpaWorkingOn.add(wcbMitMoeglichenLieferanten, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wbuLKZ, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLKZ, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 300, 0));

		iZeile++;
		jpaWorkingOn.add(wcbNurFreigegebeneLieferanten, new GridBagConstraints(
				0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 180, 0));
		jpaWorkingOn.add(wlaPLZ, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPLZ, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuPartnerklasse, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPartnerklasse, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuBranche, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBranche, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfLiefergruppe.getWrapperButton(),
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfLiefergruppe.getWrapperTextField(),
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
	}

	public String getModul() {
		return KundeReportFac.REPORT_MODUL;
	}

	protected void eventActionSpecial(ActionEvent eI) throws Throwable {
		if (eI.getActionCommand().equals(ACTION_SPECIAL_FLR_LAND)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_LEEREN, };
			QueryType[] querytypes = null;
			FilterKriterium[] filters = null;
			panelQueryFLRLand = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_LAND, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.land"));
			new DialogQuery(panelQueryFLRLand);
		} else if (eI.getActionCommand().equals(ACTION_SPECIAL_FLR_BRANCHE)) {
			String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
					.createButtonArray(false, true);

			final QueryType[] querytypes = null;
			final FilterKriterium[] filters = null;
			panelQueryFLRBranche = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_BRANCHE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.branche"));

			if (brancheIId != null) {
				panelQueryFLRBranche.setSelectedId(brancheIId);
			}
			new DialogQuery(panelQueryFLRBranche);
		} else if (eI.getActionCommand().equals(
				ACTION_SPECIAL_FLR_PARTNERKLASSE)) {
			String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
					.createButtonArray(false, true);

			final QueryType[] querytypes = null;
			final FilterKriterium[] filters = null;
			panelQueryFLRPartnerklasse = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_PARTNERKLASSE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.partnerklasse"));
			// vorbesetzen
			if (partnerklasseIId != null) {
				panelQueryFLRPartnerklasse.setSelectedId(partnerklasseIId);
			}
			new DialogQuery(panelQueryFLRPartnerklasse);
		}
	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLand) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				landIId = iId;

				String sLand = null;
				if (iId != null) {
					LandDto l = null;
					l = DelegateFactory.getInstance().getSystemDelegate()
							.landFindByPrimaryKey(iId);
					sLand = l.getCLkz();
				}
				wtfLKZ.setText(sLand);
			} else if (e.getSource() == panelQueryFLRBranche) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					BrancheDto brancheDto = DelegateFactory.getInstance()
							.getPartnerDelegate().brancheFindByPrimaryKey(key);
					brancheIId = key;
					wtfBranche.setText(brancheDto.getCNr());
				}
			}

			else if (e.getSource() == panelQueryFLRPartnerklasse) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					PartnerklasseDto partnerklasseDto = DelegateFactory
							.getInstance().getPartnerDelegate()
							.partnerklasseFindByPrimaryKey(key);
					partnerklasseIId = key;
					wtfPartnerklasse.setText(partnerklasseDto.getCNr());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLand) {
				landIId = null;
				wtfLKZ.setText("");
			} else if (e.getSource() == panelQueryFLRBranche) {
				wtfBranche.setText(null);
				brancheIId = null;
			} else if (e.getSource() == panelQueryFLRPartnerklasse) {
				wtfPartnerklasse.setText(null);
				partnerklasseIId = null;
			}
		}
	}

	public String getReportname() {
		return LieferantReportFac.REPORT_LIEFERANTENLISTE;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		Integer lieferantIIdSelektiert = null;

		if (wcbNurSelektierterLieferant.isSelected()) {
			lieferantIIdSelektiert = internalFrameLieferant.getLieferantDto()
					.getIId();
		}

		return DelegateFactory
				.getInstance()
				.getLieferantDelegate()
				.printLieferantenliste(wcbVersteckte.isSelected(),
						wcbMitMoeglichenLieferanten.isSelected(),
						wcbMitAnsprechpartner.isSelected(),
						wcbNurFreigegebeneLieferanten.isSelected(),
						lieferantIIdSelektiert, wtfPLZ.getText(), landIId,
						brancheIId, partnerklasseIId, wsfLiefergruppe.getIKey());
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
