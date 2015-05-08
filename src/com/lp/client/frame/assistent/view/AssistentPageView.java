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
 package com.lp.client.frame.assistent.view;

import javax.swing.JPanel;

import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.component.InternalFrame;

public abstract class AssistentPageView extends JPanel implements IDataUpdateListener{
	
	private static final long serialVersionUID = 5319976547323908557L;
	
	private boolean viewInitialized;
	private InternalFrame iFrame;
	
	public abstract AssistentPageController getController();
	
	/**
	 * Der Titel, der auf der Seite ganz oben steht.
	 * @return einen String
	 */
	public abstract String getTitle();
	
	public AssistentPageView(AssistentPageController controller, InternalFrame iFrame) {
		controller.addDataUpdateListener(this);
		this.iFrame = iFrame;
	}
	
	/**
	 * &Uuml;berschreiben und true zur&uuml;ckgeben, damit {@link #initViewImpl()}
	 * bei jedem Aufruf der Seite geladen wird, statt nur beim ersten.
	 * @return true wenn die Seite neu initialisiert werden soll
	 */
	protected boolean mustInitView() {
		return !viewInitialized;
	}
	
	/**
	 * Instanziert die Components und f&uuml;gt sie dem Panel hinzu.
	 */
	public void initView() {
		if(!mustInitView()) return;
		removeAll();
		initViewImpl();
		viewInitialized = true;
	}
	/**
	 * Components hinzuf&uuml;gen. Wird beim ersten mal,
	 * wenn die Seite angezeigt wird aufgerufen.
	 * @see #mustInitView() um Aufruf bei jedem mal,
	 * wenn zu dieser Seite gewechselt wird, zu erzwingen.
	 */
	protected abstract void initViewImpl();
	
	protected InternalFrame getInternalFrame() {
		return iFrame;
	}
}
