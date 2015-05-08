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

import java.util.Map;
import java.util.TreeMap;

/**
 * Diese Implementierung von {@link IDurationFormat} kann eine Zeitdauer in
 * folgenden Formaten parsen:<br>
 * <li>2</li>
 * <li>,75</li>
 * <li>0,5</li>
 * <li>2,75</li>
 * <li>2:45</li>
 * <li>02:45</li>
 * <li>0245</li>
 * <li>45 min</li>
 * <li>120min</li>
 * <li>3 h</li>
 * <li>3h</li>
 * @author robert
 *
 */
public class DurationFormat {
	/**
	 * Nutzt das letzte g&uuml;ltige Eingabeformat zur Darstellung.
	 * Dies ist der Standardwert.
	 */
	static final int AUTO_FORMAT = 0;
	/**
	 * Stellt die Zeit in Standardformat dar.<br>
	 * zB.: 8:45
	 */
	static final int NORMAL_FORMAT = 1;
	/**
	 * Stellt die Zeit immer in Industriestunden dar.<br>
	 */
	static final int INDUSTRIAL_FORMAT = 2; 
	
	

	private static Map<Integer, IDurationFormat> FORMATS = new TreeMap<Integer, IDurationFormat>() {
		private static final long serialVersionUID = 8998344563133313568L;
		{
			put(NORMAL_FORMAT, new DurationFormatNormal());
			put(INDUSTRIAL_FORMAT, new DurationFormatIndustrial());
		}
	};
	
	private int format = AUTO_FORMAT;
	private int activeFormat = NORMAL_FORMAT;

	public DurationFormat() {
	}
	
	
	public DurationFormat(int format) {
		setFormat(format);
	}
	
	public void setFormat(int format) {
		this.format = format;
	}
	
	public int getFormat() {
		return format;
	}
	
	protected IDurationFormat getDurationFormat() {
		return FORMATS.get(getActiveFormat());
	}

	protected int getActiveFormat() {
		if(format == AUTO_FORMAT)
			return activeFormat;
		return format;
	}
	
	protected void updateActiveFormat(int format) {
		activeFormat = format;
	}

	
	public long parse(String text) {
		if(text == null) return -1;
		
		for (Map.Entry<Integer, IDurationFormat> formatEntry : FORMATS.entrySet()) {
			long duration = formatEntry.getValue().parse(text) ;
			if(duration > -1) {
				updateActiveFormat(formatEntry.getKey());
				return duration ;
			}
		}
		
		return -1;
	}

	public String format(long duration) {
		if(duration < 0) return null;
		return FORMATS.get(getActiveFormat()).format(duration);
	}
}
