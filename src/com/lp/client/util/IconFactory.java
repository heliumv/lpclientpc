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
 *******************************************************************************/
package com.lp.client.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class IconFactory {

	private static Map<String, ImageIcon> cachedIcons = new HashMap<String, ImageIcon>() ;

	public static void clear() {
		cachedIcons.clear();
	}

	public static ImageIcon getInternalNamedIcon(String resourcename) {
		ImageIcon icon = cachedIcons.get(resourcename) ;
		if(icon == null) {
			icon = new ImageIcon(IconFactory.class.getResource("/com/lp/client/res/" + resourcename));
			cachedIcons.put(resourcename, icon) ;
		}

		return icon ;
	}


	public static ImageIcon getMailForward() {
		return getInternalNamedIcon("mail_forward.png");
	}

	public static ImageIcon getEdit() {
		return getInternalNamedIcon("notebook.png") ;
	}

	public static ImageIcon getReset() {
		return getInternalNamedIcon("undo.png") ;
	}

	public static ImageIcon getDateChooser() {
		return getInternalNamedIcon("JDateChooserIcon.png") ;
	}

	/**
	 * Pfeil nach oben im Kalender (vorige Woche)
	 * @return Image f&uuml;r Pfeil nach oben
	 */
	public static ImageIcon getUpArrow() {
		return getInternalNamedIcon("navigate_close.png") ;
	}

	/**
	 * Pfeil nach unten im Kalender (n&auml;chste Woche)
	 * @return Pfeil nach unten im Kalender (n&auml;chste Woche)
	 */
	public static ImageIcon getDownArrow() {
		return getInternalNamedIcon("navigate_open.png") ;
	}

	/*
	 * 	private ImageIcon getImageIconEdit() {
		if (imageIconEdit == null) {
			imageIconEdit = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/notebook.png"));
		}
		return imageIconEdit;
	}

	 */
}
