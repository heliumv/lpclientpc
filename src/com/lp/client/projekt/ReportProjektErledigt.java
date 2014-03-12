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
package com.lp.client.projekt;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalPersonal;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportProjektErledigt extends PanelReportJournalPersonal implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String ACTION_SPECIAL_PERSONAL_ERLEDIGER = "action_special_personal_erlediger";

	private PanelQueryFLR panelQueryFLRPersonalErlediger = null;

	private WrapperButton wbuPersonalErlediger = null;

	private WrapperTextField wtfPersonalErlediger = null;

	private Integer personalErledigerIId = null;

	protected WrapperRadioButton wrbKw = null;

	private WrapperNumberField wnfKw = null;
	
	private WrapperCheckBox wcbInternerledigteBeruecksichtigen =null;

	Integer bereichIId = null;

	public ReportProjektErledigt(InternalFrameProjekt internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		bereichIId = internalFrame.getTabbedPaneProjekt().getBereichIId();
		jbInit();
	}

	private void jbInit() throws Exception {
		wrbSortierungPartner.setVisible(true);
		wrbSortierungPartner.setText(LPMain.getTextRespectUISPr("part.partner")
				+ " " + LPMain.getTextRespectUISPr("lp.erledigungsdatum")
				+ " PJ Nr");
		wrbSortierungPartner.setToolTipText(LPMain
				.getTextRespectUISPr("part.partner")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.erledigungsdatum") + " PJ Nr");
		wrbPartnerAlle.setVisible(false);
		wrbPartnerEiner.setVisible(false);
		wrbPersonalAlle.setVisible(false);
		wrbPersonalEine.setVisible(false);
		wcbSortiereNachPersonal.setVisible(false);
		wlaPersonaln.setVisible(false);
		wrbBereichNummer.setVisible(false);
		wbuPersonalErlediger = new WrapperButton(
				LPMain.getTextRespectUISPr("proj.erlediger"));
		wbuPersonalErlediger.addActionListener(this);
		wbuPersonalErlediger
				.setActionCommand(ACTION_SPECIAL_PERSONAL_ERLEDIGER);
		wtfPersonalErlediger = new WrapperTextField();
		wrbBereichDatum.setText(LPMain.getTextRespectUISPr("label.erledigt"));

		
		wcbInternerledigteBeruecksichtigen =new WrapperCheckBox(LPMain.getTextRespectUISPr("proj.erledigte.internerledigteberuecksichtigen"));
		
		wrbKw = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("lp.kalenderwoche_kurz"));
		wnfKw = new WrapperNumberField();
		iZeile++;

		jpaWorkingOn.add(wrbKw, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKw, new GridBagConstraints(2, iZeile, 5, 1, 6.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuPersonalErlediger, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonalErlediger, new GridBagConstraints(1,
				iZeile, 1, 1, 6.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbInternerledigteBeruecksichtigen, new GridBagConstraints(2,
				iZeile, 4, 1, 6.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		buttonGroupBereich.add(wrbKw);
		wnfKw.setInteger(0);
	}

	public String getModul() {
		return ProjektReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPersonalErlediger) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					personalErledigerIId = (Integer) key;
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(personalErledigerIId);
					wtfPersonalErlediger.setText(personalDto.getPartnerDto()
							.formatAnrede());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPersonalErlediger) {
				personalErledigerIId = null;
				wtfPersonalErlediger.setText(null);
			}
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_PERSONAL_ERLEDIGER)) {
			dialogQueryPersonalErlediger();
		}
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Date datedStichtag = new Date(System.currentTimeMillis());
		ReportJournalKriterienDto krit = getKriterien();
		krit.personalIId = personalErledigerIId;
		if (wrbKw.isSelected()) {
			final GregorianCalendar calendar = new GregorianCalendar();
			final int CURRENT_YEAR = calendar.get(Calendar.YEAR);
			calendar.clear();
			calendar.set(Calendar.YEAR, CURRENT_YEAR);
			calendar.set(Calendar.WEEK_OF_YEAR, wnfKw.getInteger());

			krit.dVon = new java.sql.Date(calendar.getTimeInMillis());
			calendar.add(Calendar.DAY_OF_MONTH, 6);
			krit.dBis = new java.sql.Date(calendar.getTimeInMillis());
		}
		return DelegateFactory.getInstance().getProjektDelegate()
				.printProjektErledigt(krit, bereichIId, datedStichtag,wcbInternerledigteBeruecksichtigen.isSelected());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	private void dialogQueryPersonalErlediger() throws Throwable {
		panelQueryFLRPersonalErlediger = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), true, false,
						personalErledigerIId);
		new DialogQuery(panelQueryFLRPersonalErlediger);
	}

}
