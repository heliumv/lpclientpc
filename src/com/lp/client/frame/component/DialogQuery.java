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
package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;

/**
 * <p>
 * Dialog mit FLR.
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author unbekannt
 * @version $Revision: 1.5 $
 */

public class DialogQuery extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQueryFLR panelQuery = null;
	private int iBreiteInPixel = 0;
	private int iHoeheInPixel = 0;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	public DialogQuery(PanelQueryFLR panelQueryI) throws HeadlessException {
		super(LPMain.getInstance().getDesktop(), true);
		try {
			panelQuery = panelQueryI;

			// this.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

			addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					panelQuery.getInternalFrame().removeItemChangedListener(
							panelQuery);

				}

				public void windowClosed(java.awt.event.WindowEvent e) {
					if (panelQuery != null) {
						panelQuery.cleanup();
					}
				}
			});

			panelQuery.setDialog(this);
			if (panelQuery.getColumnWidth() > 0) {
				iBreiteInPixel = panelQuery.getColumnWidth();
				iHoeheInPixel = new Double(Math.sqrt(new Double(iBreiteInPixel)
						.doubleValue())).intValue();
			}
			jbInit();
		} catch (Throwable e) {
			dispose();
		}
	}

	private void jbInit() throws Exception {

		setTitle(panelQuery.getAdd2Title());

		// alle buttons aktivieren
		panelQuery.updateButtons(PanelBasis.LOCK_NO_LOCKING, null);
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(
				panelQuery,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), iBreiteInPixel, iHoeheInPixel));
		pack();
//		panelQuery.setFirstFocusableComponent();
		// plazieren
		ClientPerspectiveManager.getInstance().loadAndApplyQueryFLRPosition(panelQuery);
		LPMain.getInstance().getDesktop()
		.platziereDialogInDerMitteDesFensters(this);

		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				try {
					panelQuery.setFirstFocusableComponent();				
				} catch(Exception e) {
					myLogger.error("Ignored Exception in setFirstFocusable...", e) ;
				}
			}
		});
		
		setVisible(true);		
	}
}
