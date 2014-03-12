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
package com.lp.client.frame.component;

import java.awt.Point;

/**
 * <p>
 * Erzwingt eine Methode welche fuer die Direkthilfe benoetigt wird.
 * </p>
 * 
 * <p>
 * Copyright Helium V IT-Solutions (c) 2004 - 2012
 * </p>
 * 
 * <p>
 * Erstellung: Robert Kreiseder; 02.10.2012
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:14 $
 */

public interface IDirektHilfe {
	public final Point DEFAULT_OFFSET = new Point(1, 1);

	/**
	 * Override mit <code>cornerInfoButton.setToolTipToken(token);</code>
	 */
	public void setToken(String token);
	
	/**
	 * Override mit <code>cornerInfoButton.getToolTipToken(token);</code>
	 */
	public String getToken();

	/**
	 * Rueckgabe des Positions-offset des <code>CornerInfoButton</code>
	 * abhaengig vom Typ der Componente. Richtige Implementierung:
	 * <code>return InfoButtonRelocator.getInstance().getRelocation(this);</code>
	 * s
	 */
	public Point getLocationOffset();
	
	/**
	 * setzt <code>cornerInfoButton = null</code>
	 */
	public void removeCib();
}
