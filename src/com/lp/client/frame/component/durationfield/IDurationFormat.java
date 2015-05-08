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
package com.lp.client.frame.component.durationfield;

public interface IDurationFormat {
	
	static final int MINUTES_PER_HOUR = 60;
	static final int MILLIS_PER_MINUTE = 60000;
	
	static final String GROUP_OPENER = "^(";
	static final String GROUP_CLOSER = ")$";
	
	/**
	 * Gibt das RegEx zur&uuml;ck, welches alle f&uuml;r dieses
	 * Format g&uuml;ltigen Strings matcht.
	 * Darf keine Gruppen enthalten (Aussnahme Basisklasse {@link DurationFormat})
	 * @return eine regular expression
	 */
	String getRegex();
	
	/**
	 * Parst den Text und gibt die Dauer zur&uuml;ck.
	 * @param text der zu parsende Text
	 * @return die Dauer in Millisekunden, oder -1 wenn das Parsen
	 * fehlschl&auml;gt oder <code>text</code> null ist
	 */
	long parse(String text);
	
	/**
	 * Formatiert die Dauer.
	 * @param duration die dauer in Millisekunden
	 * @return die formatierte Dauer oder null wenn <code>duration < 0</code>
	 */
	String format(long duration);

}
