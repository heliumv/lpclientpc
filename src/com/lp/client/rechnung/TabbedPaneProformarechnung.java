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
package com.lp.client.rechnung;

import javax.swing.JMenu;
import javax.swing.JSeparator;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.rechnung.service.RechnungFac;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um Panels des Rechnungsmoduls;
 * um Rechnungen des Typs Proformarechnung</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 20.11.2004</p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class TabbedPaneProformarechnung extends TabbedPaneRechnungAll {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String MENUE_ACTION_ALLE_PROFORMARECHNUNGEN = "MENUE_ACTION_ALLE_PROFORMARECHNUNGEN";

	public TabbedPaneProformarechnung(InternalFrame internalFrame)
			throws Throwable {
		super(internalFrame, RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG, LPMain
				.getInstance().getTextRespectUISPr(
						"rechnung.tab.unten.proformarechnung.title"));
	}

	protected void print() throws Throwable {
		if (getRechnungDto() != null) {
			if (pruefeKonditionen(getRechnungDto())) {
				if (DelegateFactory.getInstance().getRechnungDelegate()
						.hatRechnungPositionen(getRechnungDto().getIId())
						.booleanValue()) {
					reloadRechnungDto();
					getInternalFrame().showReportKriterien(
							new ReportProformarechnung(getInternalFrame(), getAktuellesPanel(),
									getRechnungDto(), getKundeDto(),
									super.getTitle()),
							super.getKundeDto().getPartnerDto(),
							getRechnungDto().getAnsprechpartnerIId(), false);
				} else {
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.hint"),
							"Die Proformarechnung hat noch keine Positionen");
				}
			}
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);
		JMenu modul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);
		modul.add(new JSeparator(), 0);
		JMenu journal = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_JOURNAL);
		JMenu extras = new WrapperMenu("rechnung.menu.extras", this);

		WrapperMenuItem menuItemNeuDatum = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rechnung.menu.extras.neudatum"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menuItemNeuDatum.addActionListener(this);
		menuItemNeuDatum.setActionCommand(MENUE_ACTION_NEU_DATUM);
		extras.add(menuItemNeuDatum);

		WrapperMenuItem menuItemDateiDrucken = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.drucken"), null);
		menuItemDateiDrucken.addActionListener(this);
		menuItemDateiDrucken.setActionCommand(MENUE_ACTION_DATEI_DRUCKEN);
		modul.add(menuItemDateiDrucken, 0);

		WrapperMenuItem menuItemAllePR = new WrapperMenuItem(
				"Alle Proformarechnungen", null);
		menuItemAllePR.addActionListener(this);
		menuItemAllePR.setActionCommand(MENUE_ACTION_ALLE_PROFORMARECHNUNGEN);
		journal.add(menuItemAllePR);

		return wmb;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		super.lPActionEvent(e);
		if (e.getActionCommand().equals(MENUE_ACTION_ALLE_PROFORMARECHNUNGEN)) {
			AnsprechpartnerDto a = null;
			if (getRechnungDto().getAnsprechpartnerIId() != null) {
				a = DelegateFactory
						.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(
								this.getRechnungDto().getAnsprechpartnerIId());
			}
			if (a != null) {
				getInternalFrame().showReportKriterien(
						new ReportRechnungAlleProformarechnungen(
								getInternalFrame(), "Alle Proformarechnungen"));
			} else {
				getInternalFrame().showReportKriterien(
						new ReportRechnungAlleProformarechnungen(
								getInternalFrame(), "Alle Proformarechnungen"));
			}
		}
	}

	@Override
	protected void printZahlschein() throws Throwable {
	}
}
