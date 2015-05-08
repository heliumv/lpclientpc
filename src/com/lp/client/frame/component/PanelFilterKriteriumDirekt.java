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

import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/12/03 13:08:24 $
 */
public class PanelFilterKriteriumDirekt {
	public FilterKriteriumDirekt fkd = null;

	protected WrapperLabel wlaFkdirektName1 = null;
	protected WrapperLabel wlaFkdirektOperator1 = null;
	public WrapperTextField wtfFkdirektValue1 = null;
	public WrapperNumberField wnfFkdirektValue1 = null;
	protected WrapperLabel wlaEmpty1 = null;
	

	/**
	 * Setzt die Komponenten-Namen fuer die Testumgebung Aufzurufen, wenn die
	 * Komponenten nicht mehr null sind.
	 */
	protected final void initComponents() {
		try {
			HelperClient.setComponentNames(this,
					HelperClient.replaceDotsByUnderline(fkd.kritName) + "_");
		} catch (Throwable ex) {
			// nothing
		}
	}

	public PanelFilterKriteriumDirekt(FilterKriteriumDirekt fkd)
			throws ExceptionLP {
		this.fkd = fkd;
		jbInit();
		initComponents();
	}

	private void jbInit() throws ExceptionLP {

		if (fkd.iTyp == FilterKriteriumDirekt.TYP_STRING) {
			wlaFkdirektName1 = new WrapperLabel(fkd.uiName + " ("
					+ buildFilterPattern(fkd).toString() + ")");
		} else if (fkd.iTyp == FilterKriteriumDirekt.TYP_DECIMAL) {
			wlaFkdirektName1 = new WrapperLabel(fkd.uiName);
		}

		wlaFkdirektOperator1 = new WrapperLabel(FilterKriterium.OPERATOR_EQUAL);
		wlaFkdirektOperator1.setHorizontalAlignment(SwingConstants.CENTER);
		HelperClient.setDefaultsToComponent(wlaFkdirektOperator1, 15);

		if (fkd.iTyp == FilterKriteriumDirekt.TYP_STRING) {
			wtfFkdirektValue1 = new WrapperTextField();
			wtfFkdirektValue1.setColumnsMax(fkd.iEingabebreite);
			wtfFkdirektValue1.setText(fkd.value);
		} else if (fkd.iTyp == FilterKriteriumDirekt.TYP_DECIMAL) {
			wnfFkdirektValue1 = new WrapperNumberField();
		}

		wlaEmpty1 = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaEmpty1, 15);
	}

	private StringBuffer buildFilterPattern(FilterKriteriumDirekt oKrit1) {
		StringBuffer sb = new StringBuffer();

		if (oKrit1.iWrapWithProzent == FilterKriteriumDirekt.EXTENDED_SEARCH) {
			sb.append("Aa|A+B-C");

		} else {

			if (oKrit1.isBIgnoreCase()) {
				sb.append("Aa|");
			}
			switch (oKrit1.iWrapWithProzent) {
			case FilterKriteriumDirekt.PROZENT_BOTH:
				sb.append("*A*");
				break;
			case FilterKriteriumDirekt.PROZENT_LEADING:
				sb.append("*A");
				break;
			case FilterKriteriumDirekt.PROZENT_TRAILING:
				sb.append("A*");
				break;
			}

		}
		return sb;
	}
	
	/**
	 * Das Eingabefeld l&ouml;schen
	 */
	public void clear() {
		if(wtfFkdirektValue1 != null) {
			wtfFkdirektValue1.setText(null); 
		}
		if(wnfFkdirektValue1 != null) {
			try {
				wnfFkdirektValue1.setBigDecimal(null);
			} catch(ExceptionLP e) {}
		}
	}
}
