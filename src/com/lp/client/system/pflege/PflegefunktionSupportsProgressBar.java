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
package com.lp.client.system.pflege;

import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BoundedRangeModel;

public abstract class PflegefunktionSupportsProgressBar implements IPflegefunktion {
	
	private ArrayList<PflegeEventListener> listeners;
	private BoundedRangeModel progress;
	private long startTime;
	
	public PflegefunktionSupportsProgressBar() {
		listeners = new ArrayList<PflegeEventListener>();
	}
		
	public BoundedRangeModel getProgress() {
		return progress;
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * Wenn brm = null wird progressBar disabled.
	 * Wenn brm.minimum == brm.value zeigt der Progressbar an,
	 * dass gearbeitet wird, aber noch kein Fortschritt gemacht wurde.
	 * Sonst ganz normale Anzeige des progressBar.
	 * @param brm
	 */
	public void setProgress(BoundedRangeModel brm) {
		progress = brm;
		fireProgressEvent(new EventObject(this));
	}
	
	public void setProgressValue(int value) {
		progress.setValue(value);
		fireProgressEvent(new EventObject(this));
	}
	
	@Override
	public boolean supportsProgressBar() {
		return true;
	}
	
	@Override
	public void addPflegeEventListener(PflegeEventListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	@Override
	public void removeAllPflegeEventListeners() {
		listeners.clear();
	}
	
	protected void fireDoneEvent(EventObject e) {
		for(PflegeEventListener listener:listeners)
			listener.done(e);
	}
	protected void fireStartedEvent(EventObject e) {
		startTime = System.currentTimeMillis();
		for(PflegeEventListener listener:listeners)
			listener.started(e);
	}
	protected void fireProgressEvent(EventObject e) {
		for(PflegeEventListener listener:listeners)
			listener.progress(e);
	}
	protected void fireCanceledEvent(EventObject e) {
		for(PflegeEventListener listener:listeners)
			listener.canceled(e);
	}
	
	
	protected void fireEnabledStartableEvent(EventObject e) {
		for(PflegeEventListener listener:listeners)
			listener.enabledStartable(e);
	}
	protected void fireDisableStartableEvent(EventObject e) {
		for(PflegeEventListener listener:listeners)
			listener.disabledStartable(e);
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
