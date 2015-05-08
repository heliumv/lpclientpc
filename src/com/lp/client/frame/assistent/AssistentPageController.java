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
 package com.lp.client.frame.assistent;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.view.IDataUpdateListener;

public abstract class AssistentPageController {

	private List<IPageNavigationUpdateListener> navigationUpdateListeners = new ArrayList<IPageNavigationUpdateListener>();
	private List<IDataUpdateListener> dataUpdateListeners = new ArrayList<IDataUpdateListener>();

	/**
	 * Gib an ob "Weiter" (bzw. "Fertig stellen" auf der letzten Seite) erlaubt ist.
	 * @return true, wenn erlaubt
	 */
	public abstract boolean isNextAllowed();

	public abstract boolean isPrevAllowed();
	
	public abstract boolean isCancelAllowed();

	/**
	 * Die Seite ist jetzt aktiv, der Assistent kommt von der davor liegenden Seite
	 * @throws Throwable 
	 */
	public abstract void activateByNext() throws Throwable;
	
	/**
	 * Die Seite ist jetzt aktiv, der Assistent kommt von der danach liegenden Seite zur&uuml;ck
	 * @throws Throwable 
	 */
	public abstract void activateByPrev() throws Throwable;
	

	/**
	 * Teilt der Seite mit, dass die n&auml;chste Seite
	 * ge&ouml;ffnet werden sollte. Will man das verhindern,
	 * muss false zur&uuml;ckgegeben werden.<br>
	 * Wird false zur&uuml;ck gegeben, sollte man 
	 * den Benutzer selbst dar&uuml;ber informieren,
	 * warum die Aktion gescheitert ist.
	 * @return Seitenwechsel zulassen?
	 * @throws ExceptionLP 
	 * @throws Throwable 
	 */
	public abstract boolean nextPageIfPossible() throws ExceptionLP, Throwable;

	/**
	 * Teilt der Seite mit, dass die vorherige Seite
	 * ge&ouml;ffnet werden sollte. Will man das verhindern,
	 * muss false zur&uuml;ckgegeben werden.<br>
	 * Wird false zur&uuml;ck gegeben, sollte man den Benutzer selbst dar&uuml;ber informieren,
	 * warum die Aktion gescheitert ist.
	 * @return Seitenwechsel zulassen?
	 * @throws ExceptionLP 
	 * @throws Throwable 
	 */
	public abstract boolean prevPageIfPossible() throws ExceptionLP, Throwable;
	
	/**
	 * Teilt der Seite mit, dass der Assistent abgebrochen
	 * werden soll. Will man das verhindern, muss false
	 * zur&uuml;ckgegeben werden.<br>
	 * Wird true zur&uuml;ck gegeben, wird der Benutzer gefragt ob es sicher ist.<br>
	 * Wird false zur&uuml;ck gegeben, sollte man den Benutzer selbst dar&uuml;ber informieren,
	 * warum die Aktion gescheitert ist.
	 * @return Abrechen zulassen?
	 * @throws ExceptionLP 
	 * @throws Throwable 
	 */
	public abstract boolean cancelIfPossible() throws ExceptionLP, Throwable;
	
	/**
	 * Wird auf der letzten Seite das Beenden best&auml;tigt, wird diese Methode aufgerufen
	 * @throws Throwable 
	 * @throws ExceptionLP 
	 */
	public void finishedAssistent() throws ExceptionLP, Throwable {};
	
	public void addNavigationUpdateListener(IPageNavigationUpdateListener listener) {
		if(navigationUpdateListeners.contains(listener)) return;
		navigationUpdateListeners.add(listener);
	}
	
	public void removeNavigationUpdateListener(IPageNavigationUpdateListener listener) {
		navigationUpdateListeners.remove(listener);
	}
	
	/**
	 * teile allen Listenern mit, dass die M&ouml;glichkeiten der Navigation
	 * (zur&uuml;ck, weiter, abbruch...) sich ge&auml;ndert haben.
	 */
	protected void fireNavigationUpdateEvent() {
		for(IPageNavigationUpdateListener listener : navigationUpdateListeners)
			listener.navigationUpdateEvent(new EventObject(this));
	}
	
	public void addDataUpdateListener(IDataUpdateListener listener) {
		dataUpdateListeners.add(listener);
	}
	
	public void removeDataUpdateListener(IDataUpdateListener listener) {
		dataUpdateListeners.remove(listener);
	}
	
	/**
	 * Teilt der View mit, dass die Daten der Seite aktuallisiert wurden.
	 */
	protected void fireDataUpdateEvent() {
		for(IDataUpdateListener listener : dataUpdateListeners) {
			listener.dataUpdated();
		}
	}
}
