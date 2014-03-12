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
package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditor;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;

/**
 * <p>
 * Basisfenster fuer LP5 Positionen.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-11
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelPositionenTexteingabe extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LpEditor lpEditor = null;

	public PanelPositionenTexteingabe(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {

		super(internalFrame, add2TitleI, key);

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		setLayout(new GridBagLayout());

		lpEditor = new LpEditor(null, LPMain.getInstance().getUISprLocale());
		lpEditor.setPageWidth(ParameterCache.getPageWidth(ParameterFac.PARAMETER_EDITOR_BREITE_TEXTEINGABE));
		// UW 15.03.06 JO fragen, das gibt eine NullPointerException
		// lpEditor.setJasperReport(getInternalFrame().getSystemDelegate().getDreispalter(),
		// false);
		lpEditor.getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT;

		add(lpEditor, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
	}

	protected void setDefaults() throws Throwable {
		lpEditor.setText("");
	}

	public void requestFocus() {
		lpEditor.requestFocusInWindow();
	}
	
	public LpEditor getLpEditor() {
		return lpEditor;
	}
}
