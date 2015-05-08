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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
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
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportFehlerstatistik extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaErledigt = new WrapperLabel();

	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperRadioButton wrbBelegnummer = new WrapperRadioButton();
	private WrapperRadioButton wrbArtikel = new WrapperRadioButton();
	private WrapperRadioButton wrbFehler = new WrapperRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperCheckBox wcbAlle = new WrapperCheckBox();

	protected JPanel jpaWorkingOn = new JPanel();

	private WrapperDateRangeController wdrBereich = null;

	public ReportFehlerstatistik(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);

		jbInit();
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn.setLayout(new GridBagLayout());
		wlaErledigt.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.losstatistik.erledigtvon"));
		wrbBelegnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.losnummer"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));

		wcbAlle.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.fehlerstatistik.alle"));

		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);

		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		wrbArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"));
		wrbFehler.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.fehler"));

		buttonGroup.add(wrbBelegnummer);
		buttonGroup.add(wrbArtikel);
		buttonGroup.add(wrbFehler);
		wrbBelegnummer.setSelected(true);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wlaErledigt, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.sortierung")),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wcbAlle, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBelegnummer, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wrbArtikel, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbFehler, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}

	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_FEHLERSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		Integer iSortierung = FertigungReportFac.SORTIERUNG_FEHLERSTATISTIK_LOSNUMMER;
		if (wrbArtikel.isSelected()) {
			iSortierung = FertigungReportFac.SORTIERUNG_FEHLERSTATISTIK_ARTIKELNUMMER;
		} else if (wrbFehler.isSelected()) {
			iSortierung = FertigungReportFac.SORTIERUNG_FEHLERSTATISTIK_FEHLER;
		}

		java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(wdfBis
				.getTimestamp().getTime() + 24 * 3600000);

		jasperPrint = DelegateFactory
				.getInstance()
				.getFertigungDelegate()
				.printFehlerstatistik(
						Helper.cutTimestamp(wdfVon.getTimestamp()),
						Helper.cutTimestamp(wdfBisTemp), iSortierung,
						wcbAlle.isSelected());

		return jasperPrint;

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
