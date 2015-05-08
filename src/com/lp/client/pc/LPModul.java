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

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;

import com.lp.client.frame.component.ToolbarButton;

public class LPModul {
	// public final static int STATUS_DISABLED_ALL = 0;
	// public final static int STATUS_DISABLED = 2;
	public final static int STATUS_DISABLED = 0;
	public final static int STATUS_ENABLED = 1;

	private JInternalFrame lPModule = null;
	private JCheckBoxMenuItem jMenuItem = null;
	private int iStatus = STATUS_DISABLED;
	private ToolbarButton toolbarButton = null;

	public LPModul() {
		// nothing here
	}

	public JInternalFrame getLPModule() {
		return lPModule;
	}

	public boolean hasInternalFrame() {
		return lPModule != null;
	}

	// public void setLPModule(javax.swing.JInternalFrame lPModule) {
	// this.lPModule = lPModule;
	// // wenns ein InternalFrame gibt, dann ist der Menueintrag selektiert
	// this.getJMenuItem().setSelected(lPModule != null);
	// }

	public void setOpenModules(JInternalFrame lPModule) {
		this.lPModule = lPModule;

		if (lPModule == null && getJMenuItem() != null) {
			getJMenuItem().setSelected(false);
		}
	}

	public void getOpenModules(JInternalFrame lPModule) {
		this.lPModule = lPModule;
		// wenns ein InternalFrame gibt, dann ist der Menueintrag selektiert
		this.getJMenuItem().setSelected(lPModule != null);
	}

	public String getModuleName() {
		return jMenuItem == null ? "" : ("" + jMenuItem.getText());

	}

	public JCheckBoxMenuItem getJMenuItem() {
		return jMenuItem;
	}

	public void setJMenuItem(JCheckBoxMenuItem jMenuItem) {
		this.jMenuItem = jMenuItem;
	}

	public int getStatus() {
		return iStatus;
	}

	public void setStatus(int iStatus) {
		// TODO-AGILCHANGES
		/**
		 * AGILPRO CHANGES BEGIN
		 *
		 * @author Lukas Lisowski
		 */
		if (LPMain.getInstance().isAGILPRO()) {
			return;
		}
		/**
		 * AGILPRO CHANGES END
		 */

		this.iStatus = iStatus;
		getToolbarButton().setEnabled(iStatus);
		getJMenuItem().setEnabled(iStatus == LPModul.STATUS_ENABLED);
	}

	protected ToolbarButton getToolbarButton() {
		return toolbarButton;
	}

	public void setToolbarButton(ToolbarButton toolbarButton) {
		this.toolbarButton = toolbarButton;
	}

}
