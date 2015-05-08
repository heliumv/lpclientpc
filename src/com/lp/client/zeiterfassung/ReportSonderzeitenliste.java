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
package com.lp.client.zeiterfassung;

import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;

public class ReportSonderzeitenliste extends ReportZeiterfassung implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer taetigkeitIId = null;
	private WrapperButton wbuTaetigkeit = new WrapperButton();
	private WrapperTextField wtfTaetigkeit = new WrapperTextField();
	static final public String ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE = "action_taetigkeit_from_liste";
	private PanelQueryFLR panelQueryFLRTaetigkeit = null;

	public ReportSonderzeitenliste(InternalFrameZeiterfassung internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, internalFrame.getPersonalDto().getIId(),
				add2Title);

		jbInit();
		initComponents();
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}
	
	@Override
	protected boolean showSorting() {
		return false;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE)) {
			dialogQueryTaetigkeitFromListe(e);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRTaetigkeit) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				taetigkeitIId = key;

				TaetigkeitDto taetigkeitDto = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.taetigkeitFindByPrimaryKey(key);
				wtfTaetigkeit.setText(taetigkeitDto.getCNr());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRTaetigkeit) {
				wtfTaetigkeit.setText(null);
				taetigkeitIId = null;
			}
		}
	}

	void dialogQueryTaetigkeitFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };

		panelQueryFLRTaetigkeit = new PanelQueryFLR(ZeiterfassungFilterFactory
				.getInstance().createQTTaetigkeit(), ZeiterfassungFilterFactory
				.getInstance()
				.createFKAlleSondertaetigkeitenOhneKommtGehtEndeStop(),
				QueryParameters.UC_ID_SONDERTAETIGKEIT, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("title.taetigkeitauswahlliste"));
		panelQueryFLRTaetigkeit.setSelectedId(taetigkeitIId);
		new DialogQuery(panelQueryFLRTaetigkeit);

	}

	private void jbInit() throws Throwable {

		wbuTaetigkeit
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.zeitdaten.sondertaetigkeit")
						+ "...");
		wbuTaetigkeit.setActionCommand(ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE);
		wbuTaetigkeit.addActionListener(this);
		wtfTaetigkeit.setEditable(false);

		getInternalFrame().addItemChangedListener(this);

		jpaWorkingOn.add(wbuTaetigkeit);
		jpaWorkingOn.add(wtfTaetigkeit, "span 2");

		addZeitraumAuswahl();
	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungFac.REPORT_SONDERZEITENLISTE;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		jasperPrint = DelegateFactory
				.getInstance()
				.getZeiterfassungDelegate()
				.printSondertaetigkeitsliste(getPersonalIId(), taetigkeitIId,
						wdfVon.getTimestamp(), wdfBis.getTimestamp(),
						mitVersteckten(),nurAnwesende(), getPersonAuswahl());

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
