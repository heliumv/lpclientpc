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
package com.lp.client.frame.report;

import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.bestellung.ReportBestellVorschlag;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich ...</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>01.04.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public abstract class PanelReportJournalEinkauf extends PanelReportJournal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelQueryFLR panelQueryFLRLieferant = null;

	protected LieferantDto lieferantDto = null;

	public PanelReportJournalEinkauf(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInitPanel();
		initComponents();
	}

	private void jbInitPanel() throws Throwable {
		wrbSortierungPartner.setText(LPMain
				.getTextRespectUISPr("lp.lieferant.tooltip"));
		wbuPartner.setText(LPMain.getTextRespectUISPr("button.lieferant"));
		wbuPartner.setToolTipText(LPMain
				.getTextRespectUISPr("button.lieferant.tooltip"));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER_EINER)) {
			// wenn noch keiner gewaehlt
			if (lieferantDto == null) {
				wbuPartner.doClick();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER_AUSWAHL)) {
			dialogQueryLieferant();
		}
	}

	private void dialogQueryLieferant() throws Throwable {

		if (this instanceof ReportBestellVorschlag) {
			panelQueryFLRLieferant = BestellungFilterFactory.getInstance()
					.createPanelFLRBestellvorschlagAlleLieferanten(
							getInternalFrame());
			new DialogQuery(panelQueryFLRLieferant);
		} else {
			panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
					.createPanelFLRLieferantGoto(
							getInternalFrame(),
							(lieferantDto != null) ? lieferantDto.getIId()
									: null, true, false);
			new DialogQuery(panelQueryFLRLieferant);
		}
	
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferant) {
				try {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();
					holeLieferant((Integer) key);
				} catch (Throwable ex) {
					LPMain.getInstance().exitFrame(getInternalFrame(), ex);
				}
			}
		}
	}

	/**
	 * Die Lieferantendaten in die Felder schreiben
	 * 
	 * @throws Exception
	 */
	private void dto2ComponentsLieferant() {
		if (lieferantDto != null) {
			this.wtfPartner.setText(lieferantDto.getPartnerDto()
					.formatFixTitelName1Name2());
		} else {
			this.wtfPartner.setText(null);
		}
	}

	private void holeLieferant(Integer key) throws Throwable {
		if (key != null) {
			lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(key);
			dto2ComponentsLieferant();
		}
	}

	protected final void befuelleKriterien(ReportJournalKriterienDto krit) {
		super.befuelleKriterien(krit);
		if (wrbPartnerEiner.isSelected()) {
			krit.lieferantIId = lieferantDto.getIId();
		}
	}
}
