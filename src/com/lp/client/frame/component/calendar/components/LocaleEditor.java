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
/*
 * LocaleEditor.java - a locale editor for JavaBeans
 * Copyright (C) 2004 Kai Toedter
 * kai@toedter.com
 * www.toedter.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package com.lp.client.frame.component.calendar.components;

import java.util.Calendar;
import java.util.Locale;

/**
 * Property editor for locales.
 *
 * @author Kai Toedter
 * @version $LastChangedRevision: 85 $
 * @version $LastChangedDate: 2006-04-28 13:50:52 +0200 (Fr, 28 Apr 2006) $
 */
public class LocaleEditor extends java.beans.PropertyEditorSupport {
	private Locale[] locales;
	private String[] localeStrings;
	private Locale locale;
	private int length;

	/**
	 * Default LocaleEditor constructor.
	 */
	public LocaleEditor() {
		locale = Locale.getDefault();
		locales = Calendar.getAvailableLocales();
		length = locales.length;
		localeStrings = new String[length];
	}

	/**
	 * Returns the locale strings.
	 *
	 * @return the locale strings
	 */
	public String[] getTags() {
		for (int i = 0; i < length; i++)
			localeStrings[i] = locales[i].getDisplayName();

		return localeStrings;
	}

	/**
	 * Sets the locale strings as text and invokes setValue( locale ).
	 *
	 * @param text
	 *            the locale string text
	 *
	 * @throws IllegalArgumentException
	 *             not used
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		for (int i = 0; i < length; i++)
			if (text.equals(locales[i].getDisplayName())) {
				locale = locales[i];
				setValue(locale);

				break;
			}
	}

	/**
	 * Returns the locale string as text.
	 *
	 * @return the locale string
	 */
	public String getAsText() {
		return locale.getDisplayName();
	}
}
