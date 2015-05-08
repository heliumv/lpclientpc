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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public class PanelTabelleLosnachkalkulation extends PanelTabelle {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaKritAuswertung = null;

	// das soll in der optionalen zweiten Zeile stehen
	private WrapperLabel wlaEmpty = null;
	private WrapperLabel wlaUmsatz = null;

	private WrapperLabel wlaPersonalzeit = null;
	private WrapperLabel wlaMaschinenzeit = null;

	InternalFrameFertigung internalFrameFertigung = null;

	static final public String NACHKALKULATION_PRINT = LEAVEALONE
			+ "NACHKALKULATION_PRINT";

	/**
	 * PanelTabelle.
	 *
	 * @param iUsecaseIdI
	 *            die eindeutige UseCase ID
	 * @param sTitelTabbedPaneI
	 *            der Titel des aktuellen TabbedPane
	 * @param oInternalFrameI
	 *            der uebergeordente InternalFrame
	 * @throws Throwable
	 */
	public PanelTabelleLosnachkalkulation(int iUsecaseIdI,
			String sTitelTabbedPaneI, InternalFrameFertigung oInternalFrameI)
			throws Throwable {
		super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);
		internalFrameFertigung = oInternalFrameI;
		jbInit();
		initComponents();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(NACHKALKULATION_PRINT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"auft.title.panel.auftragzeiten");
			getInternalFrame().showReportKriterien(
					new ReportNachkalkulation(getInternalFrame(),
							internalFrameFertigung.getTabbedPaneLos()
									.getLosDto().getIId(), add2Title));
		}
	}

	/**
	 * Initialisiere alle Komponenten; braucht der JBX-Designer; hier bitte
	 * keine wilden Dinge wie zum Server gehen, etc. machen.
	 *
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		wlaKritAuswertung = new WrapperLabel();

		wlaEmpty = new WrapperLabel();
		wlaEmpty.setMaximumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));
		wlaEmpty.setMinimumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));
		wlaEmpty.setPreferredSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));

		wlaUmsatz = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.wert"));
		wlaUmsatz.setHorizontalAlignment(SwingConstants.CENTER);

		wlaPersonalzeit = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("pers.personalzeit"));
		wlaPersonalzeit.setHorizontalAlignment(SwingConstants.CENTER);
		wlaMaschinenzeit = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("pers.maschinenzeit"));
		wlaMaschinenzeit.setHorizontalAlignment(SwingConstants.CENTER);

		getPanelOptionaleZweiteZeile().add(
				wlaEmpty,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaPersonalzeit,
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaMaschinenzeit,
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaUmsatz,
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		setFirstColumnVisible(false);

	    // die erste Spalte ist immer der ZeilenHeader
	    TableColumn tcZeilenHeader = table.getColumnModel().getColumn(1);
	    tcZeilenHeader.setCellRenderer(new ZeilenHeaderRenderer());
	    tcZeilenHeader.setPreferredWidth(SPALTENBREITE_ZEILENHEADER);

		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/printer.png",
						LPMain.getTextRespectUISPr("lp.printer"),
						NACHKALKULATION_PRINT,
						KeyStroke.getKeyStroke('P',
								java.awt.event.InputEvent.CTRL_MASK), null);
		enableToolsPanelButtons(true, NACHKALKULATION_PRINT);

	}

	/**
	 * eventActionRefresh
	 *
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoRefreshI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		wlaKritAuswertung.setMaximumSize(new Dimension(350, Defaults
				.getInstance().getControlHeight()));
		wlaKritAuswertung.setMinimumSize(new Dimension(350, Defaults
				.getInstance().getControlHeight()));
		wlaKritAuswertung.setPreferredSize(new Dimension(350, Defaults
				.getInstance().getControlHeight()));
		wlaKritAuswertung.setHorizontalAlignment(SwingConstants.LEADING);

		getPanelFilterKriterien().add(wlaKritAuswertung);
	}

}
