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
package com.lp.client.pc;

import java.text.MessageFormat;

public class Texts {
//	private static Texts instance ;
//	
//	private Texts() {
//	}
//
//	public static Texts t() {
//		if(instance == null) {
//			instance = new Texts() ;
//		}
//		return instance ;
//	}
	
	private static String getResourceLabel(String string) {
		String resourceLabel = LPMain.getTextRespectUISPr(string);
		return resourceLabel;
	}

	private static String getResourceLabelFormatted(String token,
			Object... values) {
		String str = getResourceLabel(token);
		return MessageFormat.format(str, values);
	}
	
	
	// Text
	
	public static String txtHvUser() {
		return getResourceLabel("lp.about.hvbenutzer") ;
	}
	
	public static String txtSysUser() {
		return getResourceLabel("lp.about.systembenutzer") ;
	}
	
	public static String txtComputerName() {
		return getResourceLabel("lp.about.computername");
	}
	
	public static String txtLoggedOnServer() {
		return getResourceLabel("lp.about.angemeldetanserver");
	}
	
	public static String txtClientJavaInfo() {
		return getResourceLabel("lp.client.java.info");
	}
	
	public static String txtServerJavaInfo() {
		return getResourceLabel("lp.server.java.info");
	}
	
	public static String txtLocaleCountry() {
		return getResourceLabel("lp.locale.country") ;
	}
	
	public static String txtOs() {
		return getResourceLabel("lp.betriebssystem");
	}
	
	
	/**
	 * Formatierter Text
	 * @return den formatierten Text (Beispiel: "Text: {text})
	 */
	public static String msgTimezone(String timezone) {
		return getResourceLabelFormatted("lp.locale.timezone", timezone) ;
	}
	
	public static String msgCountry(String country) {
		return getResourceLabelFormatted("lp.locale.country", country);
	}
	
	public static String msgLanguage(String language) {
		return getResourceLabelFormatted("lp.locale.language", language);
	}
	
	public static String msgHttp(String httpAddress) {
		return getResourceLabelFormatted("internet.protocol.suite.application.layer.http", httpAddress);
	}
	
	public static String msgFreeMemory(long freeMemory) {
		return getResourceLabelFormatted("lp.free",  freeMemory);
	}
	
	public static String msgMaxMemory(long maxMemory) {
		return getResourceLabelFormatted("lp.max",  maxMemory);
	}
	
	public static String msgTotMemory(long totalMemory) {
		return getResourceLabelFormatted("lp.tot",  totalMemory);
	}
	
	public static String msgClientLogoutOpenModules(int i) {
		if (i == 1) {
			return LPMain.getTextRespectUISPr("lp.warning.open.lpmodule.offen.abmelden") ;
		}
		else if (i > 1) {
			return getResourceLabelFormatted("lp.warning.open.lpmodule.offen.mz.abmelden", i);
		}
		return "" ;
	}
	
	public static String msgClientLogoutLockedModules(int j) {
		if (j == 1) {
			return LPMain.getTextRespectUISPr("lp.warning.open.lpmodule.locked.abmelden");
		}
		else if (j > 1) {
			getResourceLabelFormatted("lp.warning.open.lpmodule.locked.mz.abmelden", j);
		}
		return "" ;
	}
}
