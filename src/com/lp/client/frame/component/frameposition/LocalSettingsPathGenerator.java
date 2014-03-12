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
package com.lp.client.frame.component.frameposition;

import com.lp.client.pc.LPMain;
import com.lp.util.Helper;



public class LocalSettingsPathGenerator {

	public static final String SEP = System.getProperty("file.separator");
	private static final String HELIUMV_DIR = ".heliumv";
	private static final String USERNAMEDEFAULT = "Default";
	
	private String mandant;
	private String username;
	
	public LocalSettingsPathGenerator() {
		try {
			String benutzername = LPMain.getTheClient().getBenutzername();
			mandant = LPMain.getTheClient().getMandant();
			username = Helper.getMD5Hash(benutzername.trim().substring(0,	benutzername.indexOf("|"))).toString();
		} catch (Throwable e) {
			username = USERNAMEDEFAULT;
		}
	}
	
	private String getMandantFolder() {
		return mandant == null? "" : mandant + SEP;
	}
	
	public String getPath() {
		return  getUserHomeDir() + SEP + HELIUMV_DIR + SEP + username + SEP + getMandantFolder();
	}
	
	private String getUserHomeDir() {
//		return SystemProperties.isWindows() ? "%APPDATA%" : System.getProperty("user.home");
		return System.getProperty("user.home");
	}
}
