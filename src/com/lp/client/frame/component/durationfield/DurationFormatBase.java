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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DurationFormatBase implements IDurationFormat {
	private Pattern compiledPattern ;
	
	protected String getMatch(String regex, String text) {
		Matcher m = Pattern.compile(regex).matcher(text);
		return m.find() ?  m.group() : null ;
	}

	protected String getMatch(Pattern pattern, String text) {
		Matcher m = pattern.matcher(text) ;
		return m.find() ?  m.group() : null ;
	}
	
	protected Pattern getCompiledPattern() {
		if(compiledPattern == null) {
			compiledPattern = Pattern.compile(GROUP_OPENER + getRegex() + GROUP_CLOSER);
		}
		
		return compiledPattern;
	}
	
	@Override
	public long parse(String text) {
		if(text == null || text.trim().length() == 0) return -1 ;
		
		if(getMatch(getCompiledPattern(), text) == null) return -1 ;
		
		return parseImpl(text) ;
	}
	
	/**
	 * Es ist garantiert, dass text != empty ist
	 * @param text
	 * @return
	 */
	protected abstract long parseImpl(String text) ;
}
