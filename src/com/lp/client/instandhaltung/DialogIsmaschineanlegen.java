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
package com.lp.client.instandhaltung;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public class DialogIsmaschineanlegen extends JDialog {

	Integer anlageIId = null;
	private static final long serialVersionUID = 1L;
	private PanelBasis panelDetailFehlmengen = null;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private InternalFrame internalFrame = null;

	public DialogIsmaschineanlegen() {
		// nothing here
	}

	public DialogIsmaschineanlegen(InternalFrame internalFrame, Integer anlageIId) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("is.maschine"), true);

		this.internalFrame = internalFrame;
		this.anlageIId = anlageIId;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		jbInit();
		pack();

		panelDetailFehlmengen.updateButtons(new LockStateValue(
				PanelBasis.LOCK_FOR_NEW));
		;

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			internalFrame.removeItemChangedListener(panelDetailFehlmengen);
			panelDetailFehlmengen = null;
			this.dispose();
		}
	}

	public Integer getNeueLandPLzOrtIId() {
		if (panelDetailFehlmengen != null) {
			return (Integer) panelDetailFehlmengen.getKeyWhenDetailPanel();
		} else {
			return null;
		}
	}

	private void jbInit() throws Throwable {

		panelDetailFehlmengen = new PanelIsmaschineFuerPanelQueryFLR(internalFrame,
				LPMain.getInstance().getTextRespectUISPr("is.anlage"), this,anlageIId);
		panelDetailFehlmengen.eventYouAreSelected(false);

		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panelDetailFehlmengen,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

	}

}
