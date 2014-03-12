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
/*
 *  Copyright (C) 2011 Kai Toedter 
 *  kai@toedter.com
 *  www.toedter.com
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.lp.client.frame.component.calendar;

import java.awt.Color;
import java.util.Date;

/**
 * Implementations of this interface can be added to various JCalendar
 * components to check if certain dates are valid for selection.
 * 
 * @author Kai Toedter
 * @version $LastChangedRevision: 142 $
 * @version $LastChangedDate: 2011-06-05 07:06:03 +0200 (So, 05 Jun 2011) $
 * 
 */
public interface IDateEvaluator {
	/**
	 * Checks if a date is a special date (might have different colors and tooltips)
	 * 
	 * @param date
	 *            the date to check
	 * @return true, if the date can be selected
	 */
	boolean isSpecial(Date date);

	/**
	 * @return the foreground color (used by JDayChooser)
	 */
	Color getSpecialForegroundColor();

	/**
	 * @return the background color (used by JDayChooser)
	 */
	Color getSpecialBackroundColor();
	
	/**
	 * @return the tooltip (used by JDayChooser)
	 */
	String getSpecialTooltip(Date day);
	
	/**
	 * Checks if a date is invalid for selection
	 * 
	 * @param date
	 *            the date to check
	 * @return true, if the date is invalid and cannot be selected
	 */
	boolean isInvalid(Date date);

	/**
	 * @return the foreground color (used by JDayChooser)
	 */
	Color getInvalidForegroundColor();

	/**
	 * @return the background color (used by JDayChooser)
	 */
	Color getInvalidBackroundColor();
	
	/**
	 * @return the tooltip (used by JDayChooser)
	 */
	String getInvalidTooltip();

}
