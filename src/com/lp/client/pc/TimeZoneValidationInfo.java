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
 ******************************************************************************/
package com.lp.client.pc;

/**
 * Enthaelt Information darueber, ob die Zeitzone,
 * Sommerzeit und Zeitabweichung akzeptabel sind.
 * Ist eine dieser Eigenschaften false, kann es
 * zu falschen bzw. fehlerhaften Zeitbuchungen kommen
 * und auch andere Seiteneffekte sind moeglich.
 * @author robert
 *
 */
public class TimeZoneValidationInfo {
	
	private boolean timezoneAccepted;
	private boolean daySavingTimeAccepted;
	private boolean timeDeviationAccepted;
	
	private String serverTimezone;
	private String clientTimezone;
	
	private boolean serverHasDST;
	private long serverTime;
	
	public TimeZoneValidationInfo() {
		timezoneAccepted = false;
		daySavingTimeAccepted = false;
		timeDeviationAccepted = false;
		serverTimezone = "<undefined>";
		clientTimezone = "<undefined>";
		serverHasDST = false;
		setServerTime(0);
	}
	
	public boolean isTimezoneAccepted() {
		return timezoneAccepted;
	}
	public boolean isDaySavingTimeAccepted() {
		return daySavingTimeAccepted;
	}
	public boolean isTimeDeviationAccepted() {
		return timeDeviationAccepted;
	}

	public String getServerTimezone() {
		return serverTimezone;
	}

	public void setServerTimezone(String serverTimezone) {
		this.serverTimezone = serverTimezone;
	}

	public String getClientTimezone() {
		return clientTimezone;
	}

	public void setClientTimezone(String clientTimezone) {
		this.clientTimezone = clientTimezone;
	}

	public void setTimezoneAccepted(boolean timezoneAccepted) {
		this.timezoneAccepted = timezoneAccepted;
	}

	public void setDaySavingTimeAccepted(boolean daySavingTimeAccepted) {
		this.daySavingTimeAccepted = daySavingTimeAccepted;
	}

	public void setTimeDeviationAccepted(boolean timeDeviationAccepted) {
		this.timeDeviationAccepted = timeDeviationAccepted;
	}

	public boolean isServerHasDST() {
		return serverHasDST;
	}

	public void setServerHasDST(boolean serverHasDST) {
		this.serverHasDST = serverHasDST;
	}

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
	
	
}
