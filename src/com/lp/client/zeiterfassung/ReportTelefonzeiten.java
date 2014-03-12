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
package com.lp.client.zeiterfassung;

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
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportTelefonzeiten extends PanelBasis implements
		PanelReportIfJRDS {
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();
	private WrapperLabel wbuPersonal = new WrapperLabel();
	private WrapperTextField wtfPersonal = new WrapperTextField();
	private WrapperTextField wtfPartner = new WrapperTextField();
	private WrapperButton wbuPartner = new WrapperButton();
	private WrapperCheckBox wcbPersonalAlle = new WrapperCheckBox();
	private Integer personalIId = null;
	private Integer partnerIId = null;
	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbPersonal = null;
	private WrapperRadioButton wrbPartner = null;
	private ButtonGroup bg = null;

	private WrapperDateRangeController wdrBereich = null;
	private static final String ACTION_SPECIAL_PARTNER = "ACTION_SPECIAL_PARTNER";
	private PanelQueryFLR panelQueryFLRPartner = null;

	public ReportTelefonzeiten(InternalFrameZeiterfassung internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("pers.telefonzeiten");

		jbInit();
		initComponents();
		if (internalFrame.getPersonalDto() != null) {
			wtfPersonal.setText(internalFrame.getPersonalDto().formatAnrede());
			personalIId = internalFrame.getPersonalDto().getIId();
		}
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}

	private void dialogQueryPartner(ActionEvent e) throws Throwable {
		panelQueryFLRPartner = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame(), partnerIId, true);
		new DialogQuery(panelQueryFLRPartner);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPartner) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey((Integer) key);
				partnerIId = partnerDto.getIId();
				wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPartner) {
				partnerIId = null;
				wtfPartner.setText(null);
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER)) {
			dialogQueryPartner(e);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Integer iPersonal = personalIId;
		if (wcbPersonalAlle.isSelected()) {
			iPersonal = null;
		}

		return DelegateFactory
				.getInstance()
				.getZeiterfassungReportDelegate()
				.printTelefonzeiten(iPersonal, partnerIId,
						wdfVon.getTimestamp(), wdfBis.getTimestamp(),
						wrbPersonal.isSelected());
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wbuPersonal
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.selektierteperson")
						+ ": ");
		wcbPersonalAlle.setText(LPMain.getTextRespectUISPr("lp.alle"));
		wtfPersonal.setActivatable(false);
		wtfPersonal.setEditable(false);
		wtfPartner.setActivatable(false);
		wtfPartner.setEditable(false);
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wbuPartner.setText(LPMain.getTextRespectUISPr("button.partner"));
		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wrbPersonal = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("button.personal.tooltip"));
		wrbPersonal.setSelected(true);

		wrbPartner = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("part.partner"));
		bg = new ButtonGroup();
		bg.add(wrbPersonal);
		bg.add(wrbPartner);

		wbuPartner.setMandatoryField(true);
		wbuPartner.setActionCommand(ACTION_SPECIAL_PARTNER);
		wbuPartner.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		int iZeile = 0;
		jpaWorkingOn.add(wbuPartner, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPartner, new GridBagConstraints(2, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuPersonal, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(2, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE)) {
			jpaWorkingOn.add(wcbPersonalAlle,
					new GridBagConstraints(2, iZeile, 5, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			iZeile++;
		}
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(2, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(4, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(5, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbPersonal, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbPartner, new GridBagConstraints(2, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungReportFac.REPORT_TELEFONZEITEN;
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
