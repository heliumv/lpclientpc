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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lp.client.pc.LPMain;

public class DurationFormatIndustrial extends DurationFormatBase {

	private static final int INDUSTRIALMINUTES_PER_HOUR = 100;

	private static final String HOURS_ONLY = "[\\d]{1,2}";
	private static final String HOURS_WHEN_MINS = "[\\d]{0,2}";
	private static final String MINS = "[\\d]{0,2}";
	private static final String COMA = "[\\.,]";

	private static final String REGEX = HOURS_WHEN_MINS + COMA + MINS + "|" + HOURS_ONLY;
	
	private static final String REGEX_INDUSTRIAL_MINUTES = 
			"(?<=^" + HOURS_WHEN_MINS + COMA + ")"+ MINS + "$";
	private static final String REGEX_INDUSTRIAL_HOURS =
			"^" + HOURS_WHEN_MINS + "(?=" + COMA + MINS + "$)|^" + HOURS_ONLY + "$";
	
	private static String seperator;
	
	protected static String getDecimalSeperator() {
		if(seperator != null)
			return seperator;
		
		Locale l = null ;
		try {
			l = LPMain.getTheClient().getLocUi() ;
		} catch(Throwable t) {
		}
		
		NumberFormat format = l == null ? DecimalFormat.getInstance() : DecimalFormat.getInstance(l);
		if (format instanceof DecimalFormat) {
			// Siehe javadoc von DecimalFormat, das
			// kann man scheinbar nicht schoener machen
			
			DecimalFormat df = (DecimalFormat) format;
			seperator = df.getDecimalFormatSymbols().getDecimalSeparator() + "";
		}
		return seperator;
	}
	
	@Override
	public String getRegex() {
		return REGEX;
	}
	
	@Override
	protected long parseImpl(String text) {
		String sMinutes = getMatch(REGEX_INDUSTRIAL_MINUTES, text);
		String sHours = getMatch(REGEX_INDUSTRIAL_HOURS, text);
		long mins = 0;
		try{
			if(sMinutes != null && !sMinutes.isEmpty()) {
				sMinutes = sMinutes.length() == 1 ? sMinutes + "0" : sMinutes;
				mins = Math.round(Integer.parseInt(sMinutes)*MINUTES_PER_HOUR/(double)INDUSTRIALMINUTES_PER_HOUR);
			}
			if(sHours != null && !sHours.isEmpty()) {
				mins += Integer.parseInt(sHours)*MINUTES_PER_HOUR;
			}
		} catch (NumberFormatException e) {
			return -1;
		}
		return mins * MILLIS_PER_MINUTE;
	}

	@Override
	public String format(long duration) {
		if(duration < 0) return null;
		
		long mins = duration/MILLIS_PER_MINUTE;
		long hours = mins/MINUTES_PER_HOUR;
		long industrialMins = Math.round(mins % MINUTES_PER_HOUR * INDUSTRIALMINUTES_PER_HOUR / (double)MINUTES_PER_HOUR);
		
		return String.format("%d%s%02d", hours, getDecimalSeperator(), industrialMins);
	}
}
