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
package com.lp.client.bestellung;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComponent;

import com.lp.client.anfrage.InternalFrameAnfrage;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalEinkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportBestellVorschlag extends PanelReportJournalEinkauf implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperCheckBox wcbSortiereNachLiefertermin = new WrapperCheckBox();

	public ReportBestellVorschlag(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
	}

	private void jbInit() throws Exception {
		wcbSortiereNachLiefertermin.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.sortierungnachliefertermin"));
		wrbSortierungIdentNr.setVisible(true);
		iZeile++;
		jpaWorkingOn.add(wcbSortiereNachLiefertermin, new GridBagConstraints(0,
				iZeile, 6, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		this.setEinschraenkungDatumBelegnummerSichtbar(false);
		// this.setEinschraenkungKostenstelleSichtbar(false);
		wrbBereichNummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.belegarten"));

	}

	public String getModul() {
		return BestellungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_BESTELLVORSCHLAG;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected ReportJournalKriterienDto getKriterien() {
		ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
		befuelleKriterien(krit);
		if (wrbSortierungIdentNr.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT;
		}
		return krit;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		boolean bAnfragevorschlag = false;
		if (getInternalFrame() instanceof InternalFrameAnfrage) {
			bAnfragevorschlag = true;
		}

		return DelegateFactory
				.getInstance()
				.getBestellungDelegate()
				.printBestellVorschlag(getKriterien(),
						wcbSortiereNachLiefertermin.isSelected(),
						bAnfragevorschlag);

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}
}
