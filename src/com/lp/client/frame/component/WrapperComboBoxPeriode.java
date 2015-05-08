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
package com.lp.client.frame.component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.util.Helper;

public class WrapperComboBoxPeriode extends WrapperComboBox {
	private static final long serialVersionUID = 1519276859327368362L;

	private static final String[] monate = new String[] {
			LPMain.getTextRespectUISPr("lp.januar"),
			LPMain.getTextRespectUISPr("lp.februar"),
			LPMain.getTextRespectUISPr("lp.maerz"),
			LPMain.getTextRespectUISPr("lp.april"),
			LPMain.getTextRespectUISPr("lp.mai"),
			LPMain.getTextRespectUISPr("lp.juni"),
			LPMain.getTextRespectUISPr("lp.juli"),
			LPMain.getTextRespectUISPr("lp.august"),
			LPMain.getTextRespectUISPr("lp.september"),
			LPMain.getTextRespectUISPr("lp.oktober"),
			LPMain.getTextRespectUISPr("lp.november"),
			LPMain.getTextRespectUISPr("lp.dezember") };

	private static final String[] quartale = new String[] {
			LPMain.getTextRespectUISPr("lp.quartal1"),
			LPMain.getTextRespectUISPr("lp.quartal2"),
			LPMain.getTextRespectUISPr("lp.quartal3"),
			LPMain.getTextRespectUISPr("lp.quartal4") };

	private String geschaeftsjahr;
	private String abrechnungszeitraum;
	private boolean startMitGeschaeftsjahr;

	public WrapperComboBoxPeriode(String geschaeftsjahr) throws ExceptionLP,
			Throwable {
		this(geschaeftsjahr, null, true);

	}

	/**
	 * 
	 * @param geschaeftsjahr
	 * @param abrechnungszeitraum
	 *            akzeptiert die Werte: FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT;
	 *            FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL;
	 *            FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR
	 * @param startMitGeschaeftsjahr
	 *            true wenn die Auswahlliste mit dem ersten Monat im
	 *            Geschaeftsjahr beginnen soll, false wenn sie mit dem
	 *            Kalenderjahr beginnen soll. Hat nur Einfluss, wenn der
	 *            Abrechnungszeitraum Monat ist.
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public WrapperComboBoxPeriode(String geschaeftsjahr,
			String abrechnungszeitraum, boolean startMitGeschaeftsjahr)
			throws ExceptionLP, Throwable {

		setGeschaeftsjahr(geschaeftsjahr, abrechnungszeitraum,
				startMitGeschaeftsjahr);
		setDefaultPeriode();
	}

	public String getGeschaeftsjahr() {
		return geschaeftsjahr;
	}

	public void setGeschaeftsjahr(String geschaeftsjahr,
			String abrechnungszeitraum, boolean startMitGeschaeftsjahr)
			throws ExceptionLP, Throwable {

		if (abrechnungszeitraum == null) {
			abrechnungszeitraum = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_UVA_ABRECHNUNGSZEITRAUM)
					.getCWert();
		}

		this.startMitGeschaeftsjahr = startMitGeschaeftsjahr;
		this.geschaeftsjahr = geschaeftsjahr;
		this.abrechnungszeitraum = abrechnungszeitraum;

		removeAllItems();
		if (abrechnungszeitraum.equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT)) {
			if (!startMitGeschaeftsjahr) {
				this.addAll(monate);
			} else {
				try {
					Timestamp[] tVonBis = DelegateFactory
							.getInstance()
							.getBuchenDelegate()
							.getDatumVonBisGeschaeftsjahr(
									Integer.parseInt(geschaeftsjahr));
					this.addAll(Helper.monatsnamenBereich(monate, tVonBis));

				} catch (Throwable t) {
					this.addAll(monate);
					startMitGeschaeftsjahr = true;
				}
			}
		} else if (abrechnungszeitraum
				.equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
			addAll(quartale);
		} else {
			setEnabled(false);
		}
	}

	public void setDefaultPeriode() throws Throwable {
		if (!startMitGeschaeftsjahr
				&& abrechnungszeitraum
						.equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT)) {
			setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
		} else {
			Timestamp[] tVonBis = DelegateFactory
					.getInstance()
					.getBuchenDelegate()
					.getDatumVonBisGeschaeftsjahr(
							Integer.parseInt(geschaeftsjahr));
			if (System.currentTimeMillis() > tVonBis[0].getTime()
					&& System.currentTimeMillis() < tVonBis[1].getTime()) {
				if (abrechnungszeitraum
						.equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
					setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH) / 3);
				} else {
					try {
						setPeriode(DelegateFactory
								.getInstance()
								.getBuchenDelegate()
								.getPeriodeImGJFuerDatum(
										new Date(System.currentTimeMillis())));
						return;
					} catch (Throwable e) {
						setPeriode(1);
					}
				}
			} else {
				setPeriode(1);
			}
		}
	}

	public boolean isStartMitGeschaeftsjahr() {
		return startMitGeschaeftsjahr;
	}

	/**
	 * Setzt die Periode
	 * 
	 * @param periode
	 *            die Periode. 1 = erste Periode im Gesch&auml;ftsjahr, bzw wenn
	 *            isCustomZeitraum() 1 = 1. Monat/Quartal im Jahr
	 */
	public void setPeriode(int periode) {
		setSelectedIndex(periode - 1);
	}

	public int getPeriode() {
		return getSelectedIndex() + 1;
	}
	
	public void setSelectedPeriodeFromMonth(int month) {
		if(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT.equals(abrechnungszeitraum)) {
			setSelectedIndex(month) ;
			return ;
		}

		if(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL.equals(abrechnungszeitraum)) {
			setSelectedIndex((month) / 3) ;
			return ;
		}
		
		setSelectedIndex(0);
	}
	
}
