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
 *  JLocaleChooser.java  - A bean for choosing locales
 *  Copyright (C) 2004 Kai Toedter
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

package com.lp.client.frame.component.calendar.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * JLocaleChooser is a bean for choosing locales.
 *
 * @author Kai Toedter
 * @version $LastChangedRevision: 85 $
 * @version $LastChangedDate: 2006-04-28 13:50:52 +0200 (Fr, 28 Apr 2006) $
 */
public class JLocaleChooser extends JComboBox implements ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JComponent component;

	/**
	 * Default JLocaleChooser constructor.
	 */
	public JLocaleChooser() {
	    this(null);
	}

    /**
     * Returns "JLocaleChoose".
     *
     * @return the name value
     */
    public String getName() {
        return "JLocaleChoose";
    }

	/**
	 * Default JLocaleChooser constructor.
	 */
	public JLocaleChooser(JComponent component) {
		super();
		this.component = component;
		addItemListener(this);
		locales = Calendar.getAvailableLocales();
		localeCount = locales.length;

		for (int i = 0; i < localeCount; i++) {
			if (locales[i].getCountry().length() > 0) {
				addItem(locales[i].getDisplayName());
			}
		}

		setLocale(Locale.getDefault());
	}

	/**
	 * The ItemListener for the locales.
	 */
	public void itemStateChanged(ItemEvent iEvt) {
		String item = (String) iEvt.getItem();
		int i;

		for (i = 0; i < localeCount; i++) {
			if (locales[i].getDisplayName().equals(item))
				break;
		}
		setLocale(locales[i], false);
	}

	/**
	 * Sets the locale.
	 *
	 * @see #getLocale
	 */
	private void setLocale(Locale l, boolean select) {
		Locale oldLocale = locale;
		locale = l;
		int n = 0;

		if (select) {
			for (int i = 0; i < localeCount; i++) {
				if (locales[i].getCountry().length() > 0) {
					if (locales[i].equals(locale))
						setSelectedIndex(n);
					n += 1;
				}
			}
		}

		firePropertyChange("locale", oldLocale, locale);
		if(component != null) {
		    component.setLocale(l);
		}
	}

	/**
	 * Sets the locale. This is a bound property.
	 *
	 * @see #getLocale
	 */
	public void setLocale(Locale l) {
		setLocale(l, true);
	}

	/**
	 * Returns the locale.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Creates a JFrame with a JLocaleChooser inside and can be used for
	 * testing.
	 */
	static public void main(String[] s) {
		JFrame frame = new JFrame("LocaleChooser");
		frame.getContentPane().add(new JLocaleChooser());
		frame.pack();
		frame.setVisible(true);
	}

	private Locale[] locales;
	private Locale locale;
	private int localeCount;
}

