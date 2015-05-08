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
import com.lp.client.frame.assistent.view.AssistentPageView;

public abstract class AssistentController implements IPageNavigationUpdateListener{
	
	private List<AssistentPageView> pages = new ArrayList<AssistentPageView>();
	
	private List<IPageNavigationUpdateListener> navigationUpdateListeners = new ArrayList<IPageNavigationUpdateListener>();
	private List<IAssistentListener> assistentListeners = new ArrayList<IAssistentListener>();
	
	int pageIndex = 0;
	
	/**
	 * F&uuml;gt eine AssistentPageView hinzu und gibt ihren Index zur&uuml;ck.<br>
	 * Die erste hinzugef&uuml;gte View hat Index 0.
	 * @param page
	 * @return den Index des Controllers
	 * @throws Throwable 
	 */
	protected int registerPage(AssistentPageView page) throws Throwable {
		pages.add(page);
		page.getController().addNavigationUpdateListener(this);
		//erste Seite gleich aktivieren
		if(pages.size() == 1) {
			getActivePageView().initView();
			getActivePageCtrl().activateByNext();
		}
		return pages.size()-1;
	}
	
	protected AssistentPageView getPageView(int i) {
		return pages.get(i);
	}
	
	protected AssistentPageController getPageController(int index) {
		return pages.get(index).getController();
	}

	public AssistentPageController getActivePageCtrl() {
		return getActivePageView().getController();
	}
	
	public AssistentPageView getActivePageView() {
		return pages.get(pageIndex);
	}
	
	public void gotoNextPage() throws ExceptionLP, Throwable {
		if(pageIndex == pages.size() - 1) return;
		if(!getActivePageCtrl().nextPageIfPossible())
			return;
		pageIndex++;
		firePageSwitchedEvent();
		getActivePageView().initView();
		getActivePageCtrl().activateByNext();
	}
	
	public void gotoPrevPage() throws ExceptionLP, Throwable {
		if(pageIndex == 0) return;
		if(!getActivePageCtrl().prevPageIfPossible())
			return;
		pageIndex--;
		firePageSwitchedEvent();
		getActivePageView().initView();
		getActivePageCtrl().activateByPrev();
	}
	
	public void cancel() throws ExceptionLP, Throwable {
		if(!getActivePageCtrl().cancelIfPossible())
			return;
		fireCanceledEvent();
		cleanUp();
	}
	
	public void finish() throws ExceptionLP, Throwable {
		getActivePageCtrl().finishedAssistent();
		fireFinishedEvent();
		cleanUp();
	}
	
	public void cleanUp() {
		assistentListeners.clear();
		navigationUpdateListeners.clear();
		pages.clear();
	}
	
	public boolean isFirstPage() {
		return pageIndex == 0;
	}
	
	public boolean isLastPage() {
		return pageIndex == pages.size()-1;
	}

	/**
	 * Gib an ob "Weiter" (bzw. "Fertig stellen" auf der letzten Seite) erlaubt ist.
	 * @return true, wenn erlaubt
	 */
	public boolean isNextAllowed() {
		return getActivePageCtrl().isNextAllowed();
	}

	public boolean isPrevAllowed() {
		return getActivePageCtrl().isPrevAllowed();
	}
	
	public boolean isCancelAllowed() {
		return getActivePageCtrl().isCancelAllowed();
	}

	@Override
	public void navigationUpdateEvent(EventObject e) { //an meine Listener weiterleiten
		for(IPageNavigationUpdateListener listener : navigationUpdateListeners) {
			listener.navigationUpdateEvent(e);
		}
	}
	
	public void addNavigationUpdateListener(IPageNavigationUpdateListener listener) {
		if(navigationUpdateListeners.contains(listener)) return;
		navigationUpdateListeners.add(listener);
	}
	
	public void removeNavigationUpdateListener(IPageNavigationUpdateListener listener) {
		navigationUpdateListeners.remove(listener);
	}
	
	public void addAssistentListener(IAssistentListener listener) {
		if(assistentListeners.contains(listener)) return;
		assistentListeners.add(listener);
	}
	
	public void removeAssistentListener(IAssistentListener listener) {
		assistentListeners.remove(listener);
	}
	
	public void fireFinishedEvent() {
		for(IAssistentListener l : assistentListeners) {
			l.finished();
		}
	}
	
	public void firePageSwitchedEvent() {
		for(IAssistentListener l : assistentListeners) {
			l.pageSwitched();
		}
	}
	
	public void fireCanceledEvent() {
		for(IAssistentListener l : assistentListeners) {
			l.canceled();
		}
	}
}
