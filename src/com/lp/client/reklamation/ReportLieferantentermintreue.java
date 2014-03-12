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
package com.lp.client.reklamation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.reklamation.service.ReklamationReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportLieferantentermintreue extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private Integer lieferantIId = null;
	private WrapperButton wbuLieferant = null;
	private WrapperTextField wtfLieferant = null;
	private PanelQueryFLR panelQueryFLRLieferant = null;

	public final static String ACTION_SPECIAL_LIEFERANT = "action_special_lieferant";

	private WrapperLabel wlaDatumVon = new WrapperLabel();
	private WrapperDateField wdfDatumVon = new WrapperDateField();

	private WrapperLabel wlaDatumBis = new WrapperLabel();
	private WrapperDateField wdfDatumBis = new WrapperDateField();

	private WrapperDateRangeController wdrBereich = null;

	public ReportLieferantentermintreue(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr("rekla.reklamationsjournal");
		jbInit();
		initComponents();
		wdrBereich.doClickUp();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfDatumVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaDatumVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaDatumBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
		wdfDatumVon.setTimestamp(new java.sql.Timestamp(System
				.currentTimeMillis()));
		wdfDatumVon.setMandatoryField(true);
		wdfDatumBis.setMandatoryField(true);

		wdrBereich = new WrapperDateRangeController(wdfDatumVon, wdfDatumBis);

		wbuLieferant = new WrapperButton(
				LPMain.getTextRespectUISPr("button.lieferant"));
		wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT);
		wbuLieferant.addActionListener(this);
		wtfLieferant = new WrapperTextField();
		wtfLieferant.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wlaDatumVon, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatumVon, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaDatumBis, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatumBis, new GridBagConstraints(3, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLieferant, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLieferant, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return ReklamationReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ReklamationReportFac.REPORT_REKLAMATIONSJOURNAL;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getSource() == panelQueryFLRLieferant) {

			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				Integer pkLieferant = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (pkLieferant != null) {
					LieferantDto lieferantDto = DelegateFactory.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(pkLieferant);
					wtfLieferant.setText(lieferantDto.getPartnerDto()
							.formatFixTitelName1Name2());
					lieferantIId = lieferantDto.getIId();
				}
			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				lieferantIId = null;
				wtfLieferant.setText("");
			}

		}

	}

	protected void eventActionSpecial(ActionEvent eI) throws Throwable {

		if (eI.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT)) {

			dialogQueryLieferant();
		}
	}

	private void dialogQueryLieferant() throws Throwable {
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
				.createPanelFLRLieferantGoto(getInternalFrame(), lieferantIId,
						true, true);
		new DialogQuery(panelQueryFLRLieferant);
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory
				.getInstance()
				.getReklamationReportDelegate()
				.printLieferantentermintreue(wdfDatumVon.getTimestamp(),
						wdfDatumBis.getTimestamp(), lieferantIId);
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
