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
package com.lp.client.rechnung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportZusammenfassendeMeldung extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected WrapperDateField wdfVon = new WrapperDateField();
	protected WrapperDateField wdfBis = new WrapperDateField();
	protected WrapperLabel wlaVon = new WrapperLabel();
	protected WrapperLabel wlaBis = new WrapperLabel();
	protected WrapperDateRangeController wdrBereich = null;
	private WrapperButton wbuFinanzamt = new WrapperButton();
	private WrapperTextField wtfFinanzamt = new WrapperTextField();
	private static final String ACTION_SPECIAL_FINANZAMT = "action_special_konto_finanzamt";
	private PanelQueryFLR panelQueryFLRFinanzamt = null;
	protected JPanel jpaWorkingOn = new JPanel();

	private Integer partnerIIdFinanzamt = null;

	public ReportZusammenfassendeMeldung(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();

	}

	protected void jbInit() throws Throwable {

		wbuFinanzamt.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.finanzamt"));
		wbuFinanzamt.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.finanzamt.tooltip"));
		wbuFinanzamt.addActionListener(this);
		wbuFinanzamt.setActionCommand(ACTION_SPECIAL_FINANZAMT);

		wtfFinanzamt.setActivatable(false);
		wtfFinanzamt.setMandatoryField(true);

		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
		wdrBereich.doClickUp();
		jpaWorkingOn.setLayout(new GridBagLayout());
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));

		// Hauptfinanzamt vorschlagen
		MandantDto mandantDto = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant());
		if (mandantDto.getPartnerIIdFinanzamt() != null) {
			partnerIIdFinanzamt = mandantDto.getPartnerIIdFinanzamt();
			PartnerDto partnerDto = DelegateFactory.getInstance()
					.getPartnerDelegate().partnerFindByPrimaryKey(partnerIIdFinanzamt);
			wtfFinanzamt.setText(partnerDto.formatFixTitelName1Name2());
		}

		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		this.add(jpaWorkingOn, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuFinanzamt, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wtfFinanzamt, new GridBagConstraints(1, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FINANZAMT)) {
			panelQueryFLRFinanzamt = FinanzFilterFactory.getInstance()
					.createPanelFLRFinanzamt(getInternalFrame(),
							partnerIIdFinanzamt, false);
			new DialogQuery(panelQueryFLRFinanzamt);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRFinanzamt) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate().partnerFindByPrimaryKey(iId);
				partnerIIdFinanzamt = iId;
				wtfFinanzamt.setText(partnerDto.formatFixTitelName1Name2());

			}
		}
	}

	public String getModul() {
		return RechnungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return RechnungReportFac.REPORT_RECHNUNGEN_ZM;
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
		return DelegateFactory
				.getInstance()
				.getRechnungDelegate()
				.printZusammenfassendeMeldung(wdfVon.getDate(),
						wdfBis.getDate(), partnerIIdFinanzamt);
	}
}
