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
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.SystemFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * @author Uli Walch
 */
public class PanelTabelleAuftragzeiten extends PanelTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameAuftrag intFrame = null;
	private TabbedPaneAuftrag tpAuftrag = null;

	// das sind die aktuellen Filterkriterien
	private WrapperLabel wlaKritAuswertung = null;

	// das soll in der optionalen zweiten Zeile stehen
	private WrapperKeyValueField wkvDauer = null;
	private WrapperLabel wlaEinheit = null;

	private WrapperKeyValueField wkvKosten = null;
	private WrapperLabel wlaWaehrung = null; // in Mandantenwaehrung

	private WrapperKeyValueField wkvSollzeit = null;

	static final public String AUFTRAGZEITEN_PRINT = LEAVEALONE
			+ "AUFTRAGZEITEN_PRINT";

	/**
	 * PanelTabelle.
	 * 
	 * @param iUsecaseIdI
	 *            die eindeutige UseCase ID
	 * @param sTitelTabbedPaneI
	 *            der Titel des aktuellen TabbedPane
	 * @param oInternalFrameI
	 *            der uebergeordente InternalFrame
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelTabelleAuftragzeiten(int iUsecaseIdI, String sTitelTabbedPaneI,
			InternalFrame oInternalFrameI) throws Throwable {

		super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);

		try {
			intFrame = (InternalFrameAuftrag) oInternalFrameI;
			tpAuftrag = intFrame.getTabbedPaneAuftrag();

			jbInit();
			initComponents();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	private void jbInit() throws Throwable {
		wlaKritAuswertung = new WrapperLabel();

		setFirstColumnVisible(false);

		setColumnWidth(AuftragFac.IDX_SPALTE_KOSTEN, 0); // hide Kosten

		// die optionale zweite Zeile initialisieren
		wkvDauer = new WrapperKeyValueField(60);
		wkvDauer.setKey(LPMain.getInstance().getTextRespectUISPr("lp.dauer"));
		wlaEinheit = new WrapperLabel(SystemFac.EINHEIT_STUNDE.trim());
		wlaEinheit.setMaximumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		wkvKosten = new WrapperKeyValueField(60);
		wkvKosten.setKey(LPMain.getInstance().getTextRespectUISPr("lp.kosten"));

		wkvSollzeit = new WrapperKeyValueField(60);
		wkvSollzeit.setKey(LPMain.getInstance().getTextRespectUISPr(
				"lp.sollzeit"));

		wlaWaehrung = new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung());
		wlaWaehrung.setMaximumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung.setHorizontalAlignment(SwingConstants.LEFT);

		getPanelOptionaleZweiteZeile().add(
				wkvSollzeit,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 150, 0));
		getPanelOptionaleZweiteZeile().add(
				wkvDauer,
				new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 150, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaEinheit,
				new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wkvKosten,
				new GridBagConstraints(3, 0, 1, 1, 0.0,

				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 150, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaWaehrung,
				new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/printer.png",
						LPMain.getTextRespectUISPr("lp.printer"),
						AUFTRAGZEITEN_PRINT,
						KeyStroke.getKeyStroke('P',
								java.awt.event.InputEvent.CTRL_MASK), null);
		enableToolsPanelButtons(true, AUFTRAGZEITEN_PRINT);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(AUFTRAGZEITEN_PRINT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"auft.title.panel.auftragzeiten");
			getInternalFrame().showReportKriterien(
					new ReportAuftragzeiten(getInternalFrame(), tpAuftrag
							.getAuftragDto().getIId(), add2Title));

		}

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
		try {
			super.eventActionRefresh(e, bNeedNoRefreshI);
		} catch (Throwable ex) {

			if (ex.getCause() != null
					&& ex.getCause().getCause() instanceof EJBExceptionLP) {
				EJBExceptionLP exp = (EJBExceptionLP) ex.getCause().getCause();

				if (exp.getCode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {
					ArrayList<?> al = exp.getAlInfoForTheClient();
					String s = "";
					if (al != null && al.size() > 1) {
						if (al.get(0) instanceof Integer) {
							PersonalDto personalDto = DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.personalFindByPrimaryKey(
											(Integer) al.get(0));
							s += " ("
									+ DelegateFactory
											.getInstance()
											.getPartnerDelegate()
											.formatFixAnredeTitelName2Name1(
													personalDto.getPartnerDto(),
													LPMain.getInstance()
															.getTheClient()
															.getLocUi());
							// s += " (" + personalDto.getPartnerDto().
							// formatFixAnredeTitelName2Name1();
						}
						if (al.get(1) instanceof java.sql.Timestamp) {
							s += ", "
									+ Helper.formatDatum(
											(java.sql.Timestamp) al.get(1),
											LPMain.getInstance().getTheClient()
													.getLocUi()) + ")";
						}

					}
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"pers.error.fehlerinzeitdaten")
									+ s);

				} else {
					throw exp;
				}

			}// else

		}

		wlaKritAuswertung
				.setText(getDefaultFilter()[AuftragFac.IDX_KRIT_AUSWERTUNG]
						.formatFilterKriterium(LPMain.getInstance()
								.getTextRespectUISPr("lp.auswertung")));
		wlaKritAuswertung.setMaximumSize(new Dimension(350, 23));
		wlaKritAuswertung.setMinimumSize(new Dimension(350, 23));
		wlaKritAuswertung.setPreferredSize(new Dimension(350, 23));
		wlaKritAuswertung.setHorizontalAlignment(SwingConstants.LEADING);

		getPanelFilterKriterien().add(wlaKritAuswertung);

		// die gesamte Dauer berechnen
		double dGesamtdauer = 0;

		for (int i = 0; i < getTable().getRowCount(); i++) {
			Object oEinzeldauer = getTable().getValueAt(i,
					AuftragFac.IDX_SPALTE_DAUER);

			if (oEinzeldauer != null) {
				dGesamtdauer += ((Double) oEinzeldauer).doubleValue();
			}
		}

		wkvDauer.setValue(Helper.formatZahl(new Double(dGesamtdauer), 2,
				Defaults.getInstance().getLocUI()));

		wkvSollzeit.setValue(Helper.formatZahl(
				DelegateFactory
						.getInstance()
						.getAuftragpositionDelegate()
						.berechneArbeitszeitSoll(
								tpAuftrag.getAuftragDto().getIId()), 2,
				Defaults.getInstance().getLocUI()));

		// die gesamten Kosten berechnen
		BigDecimal bdGesamtkosten = new BigDecimal(0);

		for (int i = 0; i < getTable().getRowCount(); i++) {
			Object oEinzelkosten = getTable().getValueAt(i,
					AuftragFac.IDX_SPALTE_KOSTEN);

			if (oEinzelkosten != null) {
				bdGesamtkosten = bdGesamtkosten.add((BigDecimal) oEinzelkosten);
			}
		}

		wkvKosten.setValue(Helper.formatZahl(bdGesamtkosten, 4, Defaults
				.getInstance().getLocUI()));

		// die Spaltenueberschriften in den ersten drei Spalten sind von den
		// Kriterien abhaengig
		String sKrit = getDefaultFilter()[AuftragFac.IDX_KRIT_AUSWERTUNG].kritName;

		if (sKrit.equals(AuftragFac.KRIT_PERSONAL)) {
			getTable()
					.getColumnModel()
					.getColumn(1)
					.setHeaderValue(
							LPMain.getInstance().getTextRespectUISPr(
									"auftrag.posnummer"));
			getTable()
					.getColumnModel()
					.getColumn(2)
					.setHeaderValue(
							LPMain.getInstance().getTextRespectUISPr(
									"auftrag.nameident"));
			getTable()
					.getColumnModel()
					.getColumn(3)
					.setHeaderValue(
							LPMain.getInstance().getTextRespectUISPr(
									"auftrag.beztext"));
		} else if (sKrit.equals(AuftragFac.KRIT_IDENT)) {
			getTable()
					.getColumnModel()
					.getColumn(1)
					.setHeaderValue(
							LPMain.getInstance().getTextRespectUISPr(
									"auftrag.ident"));
			getTable()
					.getColumnModel()
					.getColumn(2)
					.setHeaderValue(
							LPMain.getInstance().getTextRespectUISPr(
									"auftrag.bezpsnummer"));
			getTable()
					.getColumnModel()
					.getColumn(3)
					.setHeaderValue(
							LPMain.getInstance().getTextRespectUISPr(
									"auftrag.nametext"));
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		tpAuftrag.setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.auftragzeiten"));
	}
}
