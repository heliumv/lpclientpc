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

import java.util.regex.Pattern;


public class DurationFormatNormal extends DurationFormatBase {

	//Regexbausteine fuer das HH:MM Format
	private static final String RX_HH = "[\\d]{1,2}";
	private static final String RX_MM = "[\\d]{2}";
	private static final String RX_SEP = ":?";
	
	//Regexbausteine fuer das Fromat: 13h bzw 45 min
	private static final String RX_3NUMBERS = "[\\d]{1,3}";
	private static final String RX_UNIT_HOUR = "\\s?h";
	private static final String RX_UNIT_MIN = "\\s?(min|m)";

	private static final String REGEX_HH_MM = RX_HH + RX_SEP + RX_MM;
	private static final String REGEX_HOUR = RX_3NUMBERS + RX_UNIT_HOUR;
	private static final String REGEX_MIN = RX_3NUMBERS + RX_UNIT_MIN;
	
	private static final String REGEX = REGEX_HH_MM + "|" + REGEX_MIN + "|" + REGEX_HOUR;

	//Matcht nur die Stunden bzw Minuten der HH:MM Darstellung
	private static final String MATCH_HH = "^" + RX_HH + "(?=" + RX_SEP + RX_MM + "$)";
	private static final String MATCH_MM = "(?<=^" + RX_HH + RX_SEP + ")" + RX_MM + "$";

	//Matcht nur die Stunden bzw Minuten der xx min bzw xx h Darstellung
	private static final String MATCH_HOURS = "^" + RX_3NUMBERS + "(?=" + RX_UNIT_HOUR + "$)";
	private static final String MATCH_MINS = "^" + RX_3NUMBERS + "(?=" + RX_UNIT_MIN + "$)";

	//Matcht nur die Stunden bzw Minuten beider Darstellung
	private static final String MATCH_HH_AND_HOURS = MATCH_HH + "|" + MATCH_HOURS;
	private static final String MATCH_MM_AND_MINS = MATCH_MM + "|" + MATCH_MINS;

	@Override
	public String getRegex() {
		return REGEX;
	}

	private Pattern compiledHH ;
	private Pattern getCompiledHH() {
		if(compiledHH == null) {
			compiledHH = Pattern.compile(MATCH_HH_AND_HOURS);
		}
		return compiledHH ;
	}

	private Pattern compiledMM ;
	private Pattern getCompiledMM() {
		if(compiledMM == null) {
			compiledMM = Pattern.compile(MATCH_MM_AND_MINS);
		}
		return compiledMM ;
	}

	@Override
	protected long parseImpl(String text) {
		String hours = getMatch(getCompiledHH(), text);
		String mins = getMatch(getCompiledMM(), text);
		if(hours == null && mins == null)
			return -1;
		try {
			int h = hours == null ? 0 : Integer.parseInt(hours);
			int m = mins == null ? 0 : Integer.parseInt(mins);
			if(h > 0 && m >= 60) return -1;
			return h * MILLIS_PER_MINUTE * MINUTES_PER_HOUR + m * MILLIS_PER_MINUTE;
		} catch(NumberFormatException e) {
			return -1;
		}
	}
	@Override
	public String format(long duration) {
		if(duration < 0) return null;
		long minutes = duration / MILLIS_PER_MINUTE;
		long hours = minutes / MINUTES_PER_HOUR;
		minutes = minutes % MINUTES_PER_HOUR;
		return String.format("%02d:%02d", hours, minutes);
	}

}
