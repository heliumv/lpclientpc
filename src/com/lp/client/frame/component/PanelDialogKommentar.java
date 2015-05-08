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
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditor;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.ParameterFac;

/**
 * <p>
 * <I>Dialog zur Eingabe eines Kommentars in Form eines freien Textes.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>30.09.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
abstract public class PanelDialogKommentar extends PanelDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ACTION_SPECIAL_SAVE = "action_" + ALWAYSENABLED
			+ "save";

	private LpEditor lpEditor = null;
	private String cBestehenderKommentar = null;
	protected boolean bHandleEventInSuperklasse = true;

	public PanelDialogKommentar(InternalFrame internalFrame, String add2TitleI)
			throws Throwable {
		super(internalFrame, add2TitleI);

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		
		lpEditor = new LpEditor(null, LPMain.getInstance().getUISprLocale());
		lpEditor.setPageWidth(ParameterCache.getPageWidth(ParameterFac.PARAMETER_EDITOR_BREITE_KOMMENTAR)) ;

		jpaWorkingOn.add(lpEditor, new GridBagConstraints(1, iZeile, 2, 1, 0.1,
				0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		if (getInternalFrame().getRechtModulweit().equals(
				RechteFac.RECHT_MODULWEIT_UPDATE)) {
			createAndSaveButton("/com/lp/client/res/disk_blue.png",
					LPMain.getTextRespectUISPr("lp.save"), ACTION_SPECIAL_SAVE,
					KeyStroke.getKeyStroke('S',
							java.awt.event.InputEvent.CTRL_MASK), null);

			String[] aWhichButtonIUse = { ACTION_SPECIAL_SAVE };

			enableButtonAction(aWhichButtonIUse);
		}
		LockStateValue lockstateValue = new LockStateValue(null, null,
				PanelBasis.LOCK_NO_LOCKING);
		updateButtons(lockstateValue);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(PanelBasis.ESC)
				|| e.getActionCommand()
						.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {

			if (getInternalFrame().getRechtModulweit().equals(
					RechteFac.RECHT_MODULWEIT_UPDATE)) {
				bHandleEventInSuperklasse = true;
				if (cBestehenderKommentar == null || // MB 04.05.06 auch wenns
														// vorher noch keinen
														// kommentar gegeben
														// hat,
														// muss er fragen
						!cBestehenderKommentar.equals(lpEditor.getText())) {
					int iAnswer = DialogFactory
							.showMeldung(
									LPMain.getTextRespectUISPr("lp.kommentarspeichern"),
									LPMain.getTextRespectUISPr("lp.frage"),
									JOptionPane.YES_NO_CANCEL_OPTION);
					switch (iAnswer) {
					case JOptionPane.YES_OPTION: {
						saveKommentar();
					}
						break;
					case JOptionPane.NO_OPTION: {
						// nothing here
					}
						break;
					case JOptionPane.CANCEL_OPTION: {
						bHandleEventInSuperklasse = false;
						// nothing here
					}
						break;
					default: {
						return;
					}
					}
				}
				if (bHandleEventInSuperklasse) {
					super.eventActionSpecial(e); // close Dialog
				}
			} else {
				super.eventActionSpecial(e);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			saveKommentar();
			getInternalFrame().closePanelDialog();
		}
	}

	/**
	 * Den Kommentar im Beleg abspeichern.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	abstract public void saveKommentar() throws Throwable;

	/**
	 * Den bestehenden Kommentar zum Vergleich mit der Benutzereingabe
	 * uebergeben.
	 * 
	 * @param cBestehenderKommentarI
	 *            der bestehende Kommentar
	 */
	public void setCBestehenderKommentar(String cBestehenderKommentarI) {
		cBestehenderKommentar = cBestehenderKommentarI;
	}

	public LpEditor getLpEditor() {
		return lpEditor;
	}
}
