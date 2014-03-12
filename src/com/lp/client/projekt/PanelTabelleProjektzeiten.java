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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.system.service.SystemFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * @author Martin Bluehweis
 */
public class PanelTabelleProjektzeiten extends PanelTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// das sind die aktuellen Filterkriterien
	private WrapperLabel wlaKritAuswertung = null;

	// das soll in der optionalen zweiten Zeile stehen
	private WrapperKeyValueField wkvDauer = null;
	private WrapperLabel wlaEinheit = null;

	private WrapperKeyValueField wkvKosten = null;
	private WrapperLabel wlaWaehrung = null; // in Mandantenwaehrung

	private WrapperKeyValueField wkvSollzeit = null;
	
	private  InternalFrameProjekt oInternalFrameI=null;
	
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
	public PanelTabelleProjektzeiten(int iUsecaseIdI, String sTitelTabbedPaneI,
			InternalFrameProjekt oInternalFrameI) throws Throwable {

		super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);
		this.oInternalFrameI=oInternalFrameI;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		wlaKritAuswertung = new WrapperLabel();

		setFirstColumnVisible(false);

		setColumnWidth(ProjektFac.IDX_SPALTE_KOSTEN, 0); // hide Kosten

		// die optionale zweite Zeile initialisieren
		
		int divLoc = Defaults.getInstance().bySizeFactor(60);
		wkvDauer = new WrapperKeyValueField(divLoc);
		wkvDauer.setKey(LPMain.getInstance().getTextRespectUISPr("lp.dauer"));
		wlaEinheit = new WrapperLabel(SystemFac.EINHEIT_STUNDE.trim());
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		wkvKosten = new WrapperKeyValueField(divLoc);
		wkvKosten.setKey(LPMain.getInstance().getTextRespectUISPr("lp.kosten"));
		
		wkvSollzeit = new WrapperKeyValueField(divLoc);
		wkvSollzeit.setKey(LPMain.getInstance().getTextRespectUISPr("lp.sollzeit"));
		
		wlaWaehrung = new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung());
		wlaWaehrung.setHorizontalAlignment(SwingConstants.LEFT);

		getPanelOptionaleZweiteZeile().setLayout(new MigLayout("wrap 8", "[30%,fill|30%,fill|5%,fill|30%,fill|5%,fill]"));
		getPanelOptionaleZweiteZeile().add(wkvSollzeit);
		getPanelOptionaleZweiteZeile().add(wkvDauer);
		getPanelOptionaleZweiteZeile().add(wlaEinheit);
		getPanelOptionaleZweiteZeile().add(wkvKosten);
		getPanelOptionaleZweiteZeile().add(wlaWaehrung);
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
		} catch (ExceptionLP ex) {
			if(ex.getCause()!=null && ex.getCause().getCause() instanceof EJBExceptionLP){
				EJBExceptionLP exp=(EJBExceptionLP)ex.getCause().getCause();
            ArrayList<?> al = exp.getAlInfoForTheClient();
				String s = "";
				if (al != null && al.size() > 1) {
					if (al.get(0) instanceof Integer) {
						PersonalDto personalDto = DelegateFactory.getInstance()
								.getPersonalDelegate()
								.personalFindByPrimaryKey((Integer) al.get(0));
						s += " ("
								+ DelegateFactory.getInstance()
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
								+ Helper.formatDatum((java.sql.Timestamp) al
										.get(1), LPMain.getInstance()
										.getTheClient().getLocUi()) + ")";
					}

				}
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("pers.error.fehlerinzeitdaten")
						+ s);

			} else {
				throw ex;
			}

		}
		wlaKritAuswertung
				.setText(getDefaultFilter()[ProjektFac.IDX_KRIT_AUSWERTUNG]
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
					ProjektFac.IDX_SPALTE_DAUER);

			if (oEinzeldauer != null) {
				dGesamtdauer += ((Double) oEinzeldauer).doubleValue();
			}
		}

		wkvDauer.setValue(Helper.formatZahl(new Double(dGesamtdauer), 2,
				Defaults.getInstance().getLocUI()));

		Double d=0D;
		if(oInternalFrameI.getTabbedPaneProjekt().getProjektDto().getDDauer()!=null){
			d=oInternalFrameI.getTabbedPaneProjekt().getProjektDto().getDDauer();
		}
		
		wkvSollzeit.setValue(Helper.formatZahl(d, 2,
				Defaults.getInstance().getLocUI()));

		
		// die gesamten Kosten berechnen
		BigDecimal bdGesamtkosten = new BigDecimal(0);

		for (int i = 0; i < getTable().getRowCount(); i++) {
			Object oEinzelkosten = getTable().getValueAt(i,
					ProjektFac.IDX_SPALTE_KOSTEN);

			if (oEinzelkosten != null) {
				bdGesamtkosten = bdGesamtkosten.add((BigDecimal) oEinzelkosten);
			}
		}

		wkvKosten.setValue(Helper.formatZahl(bdGesamtkosten, 4, Defaults
				.getInstance().getLocUI()));

		// die Spaltenueberschriften in den ersten drei Spalten sind von den
		// Kriterien abhaengig
		String sKrit = getDefaultFilter()[ProjektFac.IDX_KRIT_AUSWERTUNG].kritName;

		if (sKrit.equals(ProjektFac.KRIT_PERSONAL)) {
			getTable().getColumnModel().getColumn(1)
					.setHeaderValue(
							LPMain.getInstance().getTextRespectUISPr(
									"lo.tabelle.psnr"));
			getTable().getColumnModel().getColumn(2).setHeaderValue(
					LPMain.getInstance().getTextRespectUISPr(
							"lo.tabelle.nameident"));
			getTable().getColumnModel().getColumn(3).setHeaderValue(
					LPMain.getInstance().getTextRespectUISPr(
							"lo.tabelle.beztext"));
		} else if (sKrit.equals(ProjektFac.KRIT_IDENT)) {
			getTable().getColumnModel().getColumn(1).setHeaderValue(
					LPMain.getInstance()
							.getTextRespectUISPr("lo.tabelle.ident"));
			getTable().getColumnModel().getColumn(2).setHeaderValue(
					LPMain.getInstance().getTextRespectUISPr(
							"lo.tabelle.bezposnr"));
			getTable().getColumnModel().getColumn(3).setHeaderValue(
					LPMain.getInstance().getTextRespectUISPr(
							"lo.tabelle.nametext"));
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		try {
			super.eventYouAreSelected(false);

		} catch (ExceptionLP ex) {
			if (ex.getICode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {
				ArrayList<?> al = ex.getAlInfoForTheClient();
				String s = "";
				if (al != null && al.size() > 1) {
					if (al.get(0) instanceof Integer) {
						PersonalDto personalDto = DelegateFactory.getInstance()
								.getPersonalDelegate()
								.personalFindByPrimaryKey((Integer) al.get(0));
						s += " ("
								+ DelegateFactory.getInstance()
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
								+ Helper.formatDatum((java.sql.Timestamp) al
										.get(1), LPMain.getInstance()
										.getTheClient().getLocUi()) + ")";
					}

				}
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("pers.error.fehlerinzeitdaten")
						+ s);

			} else {
				throw ex;
			}

		}

	}
}
