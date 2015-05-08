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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.SystemDelegate;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.system.service.PingPacket;
import com.lp.server.system.service.ServerLocaleInfo;

public class SystemInfoController implements ISystemInfoController {

	private SystemDelegate systemDelegate ;
	
	private final static int PING_COUNT_TO_SEND = 5 ;
	
	public SystemInfoController() throws Throwable {
		systemDelegate = DelegateFactory.getInstance().getSystemDelegate() ;
	}
	
	
	public SystemInfoController(SystemDelegate systemDelegate) {
		setSystemDelegate(systemDelegate) ;
	}

	
	@Override
	public void setSystemDelegate(SystemDelegate systemDelegate) {
		if(null == systemDelegate) throw new IllegalArgumentException("systemDelegate == null") ;
		
		this.systemDelegate = systemDelegate ;
	}

	@Override
	public PingSystemInfo getPingInfo() {
		PingSystemInfo pingInfo = new PingSystemInfo() ;
		List<PingPacket> packets = collectPings() ;
		statPings(packets, pingInfo);
		
		return pingInfo ;
	}
	
	public TimeZoneValidationInfo getValidationInfo() throws ExceptionLP, Throwable {
		long lClient = System.currentTimeMillis();
		long lDiff = (Math.abs(lClient
				- DelegateFactory.getInstance().getSystemDelegate()
						.getServerTimestamp().getTime()) % LogonFac.EINE_STUNDE_IN_MS);


		SystemInfoController infoController = new SystemInfoController();
		ServerLocaleInfo localeInfo = infoController.getServerLocaleInfo() ;
		ServerLocaleInfo clientInfo = infoController.getClientLocaleInfo();

		int serverOffset = TimeZone.getTimeZone(localeInfo.getTimezoneID()).getRawOffset();
		int clientOffset = TimeZone.getTimeZone(clientInfo.getTimezoneID()).getRawOffset();
		
		boolean timezoneAccepted = serverOffset == clientOffset;
		boolean daySavingTimeAccepted = localeInfo.getDSTSavings().equals(clientInfo.getDSTSavings());
		boolean timeDeviationAccepted = lDiff < LogonFac.EINE_MINUTE_IN_MS;
		
		TimeZoneValidationInfo validationInfo =  new TimeZoneValidationInfo();
		validationInfo.setTimezoneAccepted(timezoneAccepted);
		validationInfo.setDaySavingTimeAccepted(daySavingTimeAccepted);
		validationInfo.setTimeDeviationAccepted(timeDeviationAccepted);
		
		validationInfo.setClientTimezone(clientInfo.getTimezoneID());
		validationInfo.setServerTimezone(localeInfo.getTimezoneID());
		
		validationInfo.setServerHasDST(localeInfo.getDSTSavings() != 0);
		validationInfo.setServerTime(DelegateFactory.getInstance().getSystemDelegate()
						.getServerTimestamp().getTime());
		
		return validationInfo;
	}

	
	public ServerLocaleInfo getServerLocaleInfo() {
		ServerLocaleInfo info = new ServerLocaleInfo() ;
		
		try {
			ServerLocaleInfo serverInfo = getSystemDelegate().getLocaleInfo() ;
			if(serverInfo != null) {
				info = serverInfo ;
			}
		} catch(ExceptionLP e) {			
		}
		
		return info ;
	}

	public ServerLocaleInfo getClientLocaleInfo() {
		ServerLocaleInfo info = new ServerLocaleInfo() ; 

		Locale l = Locale.getDefault();
		Calendar c = Calendar.getInstance();
		info.setTimezone(c.getTimeZone().getDisplayName());
		info.setCountry(l.getCountry());
		info.setLanguage(l.getLanguage());
		info.setTimezoneID(c.getTimeZone().getID());
		info.setDSTSavings(c.getTimeZone().getDSTSavings());

		return info ;
	}
	
	private void statPings(List<PingPacket> packets, PingSystemInfo pingInfo) {
		pingInfo.setPingsSent(PING_COUNT_TO_SEND) ;
		pingInfo.setPingsReceived(packets.size()) ;

		long sumMedianDiff = 0 ;
		
		for (PingPacket pingPacket : packets) {
			long diff = Math.abs(pingPacket.getPingTimeSenderStop() - pingPacket.getPingTimeSender()) ;
			if(diff > pingInfo.getMaxTime()) {
				pingInfo.setMaxTime(diff) ;
			}
			if(diff < pingInfo.getMinTime()) {
				pingInfo.setMinTime(diff) ;
			}
			
			sumMedianDiff += diff ;
		}
		
		if(packets.size() > 0) {
			pingInfo.setMedianTime(sumMedianDiff / packets.size()) ;
		}
	}

	
	private List<PingPacket> collectPings() {

		List<PingPacket> packets = new ArrayList<PingPacket>() ;
		
		for (int i = 0; i < PING_COUNT_TO_SEND; i++) {
			try {
				PingPacket received = getSystemDelegate().ping(i + 1) ;
				if(null != received) {
					packets.add(received) ;
				}
			} catch (ExceptionLP e) {
			} catch (Throwable e) {
			}
		}
		
		return packets ;
	}

	
	private SystemDelegate getSystemDelegate() {
		return systemDelegate ;
	}	
}
