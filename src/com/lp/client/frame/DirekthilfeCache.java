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
package com.lp.client.frame;

import java.util.Map;

import com.lp.client.frame.delegate.DelegateFactory;

public class DirekthilfeCache {
	
	private static Map<String, String> anwenderTextMap;
	
	private static Map<String, String> hvTextMap;

	protected static Map<String, String> getAnwenderTextMap() throws ExceptionLP, Throwable {
		if(anwenderTextMap == null) {
			anwenderTextMap = DelegateFactory.getInstance().getSystemDelegate().getAnwenderDirekthilfeTexte();
		}
		return anwenderTextMap;
	}
	
	protected static Map<String, String> getHvTextMap() throws ExceptionLP, Throwable {
		if(hvTextMap == null) {
			hvTextMap = DelegateFactory.getInstance().getSystemDelegate().getHvDirekthilfeTexte();
		}
		return hvTextMap;
	}

	/**
	 * Holt einen Anwender-Direkthilfetext. Beim ersten Aufruf werden alle Texte
	 * vom Server geladen.
	 * @param token
	 * @return den Text falls vorhanden, sonst null
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	
	public static String getAnwenderText(String token) throws ExceptionLP, Throwable {
		Map<String, String> map = getAnwenderTextMap();
		return map == null ? null : map.get(token);
	}
	
	/**
	 * Holt einen HV-Direkthilfetext. Beim ersten Aufruf werden alle Texte
	 * vom Server geladen.
	 * @param token
	 * @return den Text falls vorhanden, sonst null
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	
	public static String getHvText(String token) throws ExceptionLP, Throwable {
		Map<String, String> map = getHvTextMap();
		return map == null ? null : map.get(token);
	}
	
	/**
	 * Holt alle Hilfetexte neu vom Server
	 * @throws Throwable 
	 * @throws ExceptionLP 
	 */
	public static void reload() throws ExceptionLP, Throwable {
		anwenderTextMap = null;
		hvTextMap = null;
		getAnwenderTextMap();
		getHvTextMap();
	}

	public static void putAnwenderText(String token, String text) throws ExceptionLP, Throwable {
		DelegateFactory.getInstance().getSystemDelegate().putAnwenderDirekthilfeText(token, text);
		Map<String, String> map = getAnwenderTextMap();
		if(map != null)
			map.put(token, text);
	}
	
	public static void putHvText(String token, String text) throws ExceptionLP, Throwable {
		DelegateFactory.getInstance().getSystemDelegate().putHvDirekthilfeText(token, text);
		Map<String, String> map = getHvTextMap();
		if(map != null)
			map.put(token, text);
	}
}
