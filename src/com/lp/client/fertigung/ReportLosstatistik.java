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
public class ReportLosstatistik extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperRadioButton wrbErledigt = new WrapperRadioButton();
	private WrapperRadioButton wrbStichtag = new WrapperRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperDateField wdfStichtag = new WrapperDateField();

	private WrapperCheckBox wcbArbeitsplanSortiertNachAG = new WrapperCheckBox();
	private WrapperCheckBox wcbVerdichtetNachArtikel = new WrapperCheckBox();

	private WrapperButton wbuStueckliste = new WrapperButton();
	private WrapperTextField wtfStueckliste = new WrapperTextField();

	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftrag = new WrapperTextField();

	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();

	protected JPanel jpaWorkingOn = new JPanel();

	private WrapperDateRangeController wdrBereich = null;

	private Integer stuecklisteIId = null;
	private Integer auftragIId = null;
	private Integer losIId = null;
	static final public String ACTION_SPECIAL_STUECKLISTE_FROM_LISTE = "action_stueckliste_from_liste";
	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_from_liste";
	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_auftrag_los_liste";
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRStueckliste = null;
	private PanelQueryFLR panelQueryFLRLos = null;
	private InternalFrameFertigung internalFrameFertigung = null;

	public ReportLosstatistik(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		internalFrameFertigung = (InternalFrameFertigung) internalFrame;
		jbInit();
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}

	void dialogQueryStuecklisteFromListe(ActionEvent e) throws Throwable {

		Integer selId = null;
		if (stuecklisteIId == null
				&& internalFrameFertigung.getTabbedPaneLos()
						.getSelectedIIdLos() != null) {
			LosDto losDto = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.losFindByPrimaryKey(
							internalFrameFertigung.getTabbedPaneLos()
									.getSelectedIIdLos());
			selId = losDto.getStuecklisteIId();
		} else {
			selId = stuecklisteIId;
		}

		panelQueryFLRStueckliste = StuecklisteFilterFactory.getInstance()
				.createPanelFLRStueckliste(getInternalFrame(), selId, true);
		new DialogQuery(panelQueryFLRStueckliste);
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance()
				.createPanelFLRLose(getInternalFrame(), null, true);
		if (losIId == null) {
			panelQueryFLRLos.setSelectedId(internalFrameFertigung
					.getTabbedPaneLos().getSelectedIIdLos());
		}

		new DialogQuery(panelQueryFLRLos);

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn.setLayout(new GridBagLayout());
		wrbErledigt.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.losstatistik.erledigtvon"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wdfStichtag.setMandatoryField(true);
		wdfStichtag.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		wrbStichtag.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.stichtag"));

		buttonGroup.add(wrbErledigt);
		buttonGroup.add(wrbStichtag);
		wrbErledigt.setSelected(true);

		wbuStueckliste.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.stueckliste"));
		wbuAuftrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.auftrag"));
		wbuLos.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.tab.unten.los.title")
				+ "...");

		wcbArbeitsplanSortiertNachAG
				.setText(LPMain
						.getInstance()
						.getTextRespectUISPr(
								"fertigung.report.losstatistik.arbeitsplansortiertnachag"));
		wcbVerdichtetNachArtikel.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.verdichtetartikelnummer"));

		wcbVerdichtetNachArtikel.setSelected(true);

		wbuStueckliste.setActionCommand(ACTION_SPECIAL_STUECKLISTE_FROM_LISTE);
		wbuStueckliste.addActionListener(this);

		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);

		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);

		wtfStueckliste.setActivatable(false);
		wtfLos.setActivatable(false);
		wtfAuftrag.setActivatable(false);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wrbErledigt, new GridBagConstraints(0, iZeile, 1, 1,
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
		jpaWorkingOn.add(wrbStichtag, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbArbeitsplanSortiertNachAG, new GridBagConstraints(
				2, iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbVerdichtetNachArtikel, new GridBagConstraints(2,
				iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuStueckliste, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfStueckliste, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_STUECKLISTE_FROM_LISTE)) {
			dialogQueryStuecklisteFromListe(e);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			dialogQueryAuftrag(e);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRStueckliste) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				StuecklisteDto stuecklisteDto = DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey((Integer) key);
				wtfStueckliste.setText(stuecklisteDto.getArtikelDto()
						.formatArtikelbezeichnung());
				stuecklisteIId = stuecklisteDto.getIId();
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AuftragDto auftragDto = DelegateFactory.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey((Integer) key);

				wtfAuftrag.setText(auftragDto.getCNr());
				auftragIId = auftragDto.getIId();
			} else if (e.getSource() == panelQueryFLRLos) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losFindByPrimaryKey((Integer) key);

				wtfLos.setText(losDto.getCNr());
				losIId = losDto.getIId();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRStueckliste) {
				stuecklisteIId = null;

				wtfStueckliste.setText(null);
			} else if (e.getSource() == panelQueryFLRLos) {
				losIId = null;
				wtfLos.setText(null);
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				auftragIId = null;
				wtfAuftrag.setText(null);
			}

		}
		aktualisiereDatumsfelder();
	}

	private void aktualisiereDatumsfelder() {
		if (auftragIId == null && losIId == null) {
			wdfVon.setEditable(true);
			wdfVon.setMandatoryField(true);
			wdfBis.setEnabled(true);
			wdfBis.setMandatoryField(true);
			wdfStichtag.setEnabled(true);
			wdfStichtag.setMandatoryField(true);

		} else {

			wdfVon.setEditable(false);
			wdfVon.setMandatoryField(false);
			wdfBis.setEnabled(false);
			wdfBis.setMandatoryField(false);
			wdfStichtag.setEnabled(false);
			wdfStichtag.setMandatoryField(false);
		}

	}

	private void dialogQueryAuftrag(ActionEvent e) throws Throwable {

		Integer selId = null;
		if (auftragIId == null
				&& internalFrameFertigung.getTabbedPaneLos()
						.getSelectedIIdLos() != null) {
			LosDto losDto = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.losFindByPrimaryKey(
							internalFrameFertigung.getTabbedPaneLos()
									.getSelectedIIdLos());
			selId = losDto.getAuftragIId();
		} else {
			selId = auftragIId;
		}

		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), false, true, null,
						selId);
		new DialogQuery(panelQueryFLRAuftrag);
	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_LOSSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		if (wrbErledigt.isSelected()) {

			if (auftragIId == null && losIId == null) {
				java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(wdfBis
						.getTimestamp().getTime() + 24 * 3600000);

				jasperPrint = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.printLosstatistik(
								Helper.cutTimestamp(wdfVon.getTimestamp()),
								Helper.cutTimestamp(wdfBisTemp), losIId,
								stuecklisteIId, auftragIId,
								wcbArbeitsplanSortiertNachAG.isSelected(),
								wcbVerdichtetNachArtikel.isSelected(),null);
			} else {
				jasperPrint = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.printLosstatistik(null, null, losIId, stuecklisteIId,
								auftragIId,
								wcbArbeitsplanSortiertNachAG.isSelected(),
								wcbVerdichtetNachArtikel.isSelected(),null);

			}
		} else {
			if (auftragIId == null && losIId == null) {
				java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(wdfBis
						.getTimestamp().getTime() + 24 * 3600000);

				jasperPrint = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.printLosstatistik(
								null,
								null, losIId,
								stuecklisteIId, auftragIId,
								wcbArbeitsplanSortiertNachAG.isSelected(),
								wcbVerdichtetNachArtikel.isSelected(),wdfStichtag.getTimestamp());
			} else {
				jasperPrint = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.printLosstatistik(null, null, losIId, stuecklisteIId,
								auftragIId,
								wcbArbeitsplanSortiertNachAG.isSelected(),
								wcbVerdichtetNachArtikel.isSelected(),wdfStichtag.getTimestamp());

			}
		}
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
