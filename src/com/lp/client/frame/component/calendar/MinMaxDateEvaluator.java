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
package com.lp.client.frame.component.calendar;

import java.awt.Color;
import java.util.Date;

import com.lp.client.frame.HelperClient;

public class MinMaxDateEvaluator implements IDateEvaluator {

	private DateUtil dateUtil = new DateUtil();

	public boolean isSpecial(Date date) {
		return HelperClient.isFeiertag(date);
	}

	public Color getSpecialForegroundColor() {
		return Color.red;
	}

	public Color getSpecialBackroundColor() {
		return new Color(200,200,255);
	}

	public String getSpecialTooltip(Date day) {
		return HelperClient.getFeiertagBez(day);
	}

	public boolean isInvalid(Date date) {
		return !dateUtil.checkDate(date);
	}

	public Color getInvalidForegroundColor() {
//		return null;
		return new Color(200,200,200);
	}

	public Color getInvalidBackroundColor() {
		return null;
	}

	public String getInvalidTooltip() {
		return null;
	}

	/**
	 * Sets the maximum selectable date. If null, the date 01\01\9999 will be
	 * set instead.
	 *
	 * @param max
	 *            the maximum selectable date
	 *
	 * @return the maximum selectable date
	 */
	public Date setMaxSelectableDate(Date max) {
		return dateUtil.setMaxSelectableDate(max);
	}

	/**
	 * Sets the minimum selectable date. If null, the date 01\01\0001 will be
	 * set instead.
	 *
	 * @param min
	 *            the minimum selectable date
	 *
	 * @return the minimum selectable date
	 */
	public Date setMinSelectableDate(Date min) {
		return dateUtil.setMinSelectableDate(min);
	}

	/**
	 * Gets the maximum selectable date.
	 *
	 * @return the maximum selectable date
	 */
	public Date getMaxSelectableDate() {
		return dateUtil.getMaxSelectableDate();
	}

	/**
	 * Gets the minimum selectable date.
	 *
	 * @return the minimum selectable date
	 */
	public Date getMinSelectableDate() {
		return dateUtil.getMinSelectableDate();
	}

}
