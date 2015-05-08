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

import java.util.List;

import javax.swing.SwingWorker;

import com.lp.client.frame.delegate.DelegateFactory;

public class DauerPing {
	
	private DauerPingWrapperTable wrapperTable ;
	private Worker worker ;
	
	public DauerPing(DauerPingWrapperTable wrapperTable) {
//		if(wrapperTable.getRowCount() != 15) {
//			throw new IllegalArgumentException("WrapperTable von DialogAbout erwartet") ;
//		}
		
		this.wrapperTable = wrapperTable ;
	}
	
	public void run() {
		worker = new Worker();
		worker.execute();
		
	}
	
	public void stop() {
		worker.cancel(true) ;
	}
	
	private class Worker extends SwingWorker<Void, PingSystemInfo> {
		private SystemInfoController infoController ;
		private TotalPingSystemInfo totalInfo ;
		
		private SystemInfoController getController() {
			if(infoController == null) {
				try {
					infoController = new SystemInfoController(
							DelegateFactory.getInstance().getSystemDelegate());									
				} catch(Throwable t) {
				}
			}
			return infoController ; 
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			
			totalInfo = new TotalPingSystemInfo() ;			
			
			while(!worker.isCancelled()) {
			
//				PingSystemInfo pingInfo = null ;
				PingSystemInfo pingInfo = new PingSystemInfo();
				
				SystemInfoController c = getController() ;
				if(c == null) {
					pingInfo = new PingSystemInfo() ;
				} else {
					pingInfo = c.getPingInfo();				
				}
				
				publish(pingInfo);	
				
				Thread.sleep(1000);
			}
			
			return null;
		}
		
		@Override
		protected void process(List<PingSystemInfo> chunks) {
			for (PingSystemInfo pingSystemInfo : chunks) {
				
				totalInfo.add(pingSystemInfo);
				
				wrapperTable.setPingInfo(pingSystemInfo, totalInfo);
			}
		}				

	}
	
	public class TotalPingSystemInfo extends PingSystemInfo {
		
		public TotalPingSystemInfo () {
			setMaxTime(0);
			setMinTime(0);
			setMedianTime(0);
		}
		
		public void addPingsReceived(int pingsReceived) {
			setPingsReceived(getPingsReceived() + pingsReceived) ;
		}
		
		public void addPingsSent(int pingsSent) {
			setPingsSent(getPingsSent() + pingsSent) ;
		}
		
		public void calcMinTime(long minTime){
			if (minTime > getMinTime())
				setMinTime(minTime);
		}
		
		public void calcMaxTime(long maxTime){
			if (maxTime > getMaxTime())
			setMaxTime(maxTime);
		}
		
		public void calcMedTime(long medTime){
			if (medTime > getMedianTime())
			setMedianTime(medTime);
		}
				
		public void add(PingSystemInfo pingInfo) {
			addPingsReceived(pingInfo.getPingsReceived()) ;			
			addPingsSent(pingInfo.getPingsSent()) ;
			calcMinTime(pingInfo.getMinTime());
			calcMaxTime(pingInfo.getMaxTime());
			calcMedTime(pingInfo.getMedianTime());
		}
	}

}