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
package com.lp.client.angebot;

import java.awt.event.ActionEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LockMeDto;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Eingabe der Akquisedaten fuer ein Angebot.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>14.07.05</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelDialogAngebotAkquisedaten extends PanelAngebotAkquisedaten {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAngebot intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAngebot tpAngebot = null;

	static final public String ACTION_SPECIAL_SAVE = "action_" + ALWAYSENABLED
			+ "save";

	/** Wenn der Dialog von aussen geoeffnet wird, muss ein Lock gesetzt werden. */
	private LockMeDto lockMeAngebot = null;

	public PanelDialogAngebotAkquisedaten(InternalFrame internalFrame,
			String add2TitleI) throws Throwable {
		super(internalFrame, add2TitleI);

		intFrame = (InternalFrameAngebot) getInternalFrame();
		tpAngebot = intFrame.getTabbedPaneAngebot();

		jbInit();
		setDefaults();
		initComponents();
		dto2Components();
	}

	private void jbInit() throws Throwable {
		createAndSaveAndShowButton(
				"/com/lp/client/res/disk_blue.png",
				LPMain.getInstance().getTextRespectUISPr(
						"lp.tooltip.kriterienuebernehmen"), ACTION_SPECIAL_SAVE, null);

		String[] aWhichButtonIUse = { ACTION_SPECIAL_SAVE };

		enableButtonAction(aWhichButtonIUse);
		LockStateValue lockstateValue = new LockStateValue(null, null,
				PanelBasis.LOCK_NO_LOCKING);
		updateButtons(lockstateValue);

		if (tpAngebot.getAngebotDto() != null) {
			lockMeAngebot = new LockMeDto(HelperClient.LOCKME_ANGEBOT,
					tpAngebot.getAngebotDto().getIId() + "", LPMain
							.getInstance().getCNrUser());
		}

		// explizit ausloesen, weil weder new noch update aufgerufen werden
		eventActionLock(null);
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();

		super.addAkquisedaten(jpaWorkingOn, iZeile);
	}

	private void dto2Components() throws Throwable {
		wdfNachfasstermin.setTimestamp(tpAngebot.getAngebotDto()
				.getTNachfasstermin());
		wdfRealisierungstermin.setTimestamp(tpAngebot.getAngebotDto()
				.getTRealisierungstermin());
		wnfAuftragswahrscheinlichkeit.setDouble(tpAngebot.getAngebotDto()
				.getFAuftragswahrscheinlichkeit());
		wtfAblageort.setText(tpAngebot.getAngebotDto().getXAblageort());
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)
				|| e.getActionCommand().equals(ESC)) {
			// explizit aufrufen, weil weder save noch discard aufgerufen werden
			eventActionUnlock(null);
			getInternalFrame().closePanelDialog();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			if (lockMeAngebot != null) {
				checkLockedDlg(lockMeAngebot);
			}

			// die Akquisedaten ohne Statuswechsel abspeichern
			tpAngebot.getAngebotDto().setTNachfasstermin(
					wdfNachfasstermin.getTimestamp());
			tpAngebot.getAngebotDto().setTRealisierungstermin(
					wdfRealisierungstermin.getTimestamp());
			tpAngebot.getAngebotDto().setFAuftragswahrscheinlichkeit(
					wnfAuftragswahrscheinlichkeit.getDouble());
			tpAngebot.getAngebotDto().setXAblageort(wtfAblageort.getText());

			DelegateFactory.getInstance().getAngebotDelegate()
					.updateAngebotOhneWeitereAktion(tpAngebot.getAngebotDto());

			// explizit aufrufen, weil weder save noch discard aufgerufen werden
			eventActionUnlock(null);
			getInternalFrame().closePanelDialog();

			// sonst sind die eben gespeicherten Daten in den Konditionen noch
			// nicht sichtbar
			if (tpAngebot.getSelectedIndex() == TabbedPaneAngebot.IDX_PANEL_KONDITIONEN) {
				tpAngebot.refreshAktuellesPanel();
			}
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_ANGEBOT;
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {
		if (lockMeAngebot != null) {
			// Zugehoeriges Angebot locken.
			super.lock(lockMeAngebot);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		if (lockMeAngebot != null) {
			// Zugehoeriges Angebot locken.
			super.unlock(lockMeAngebot);
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED
				&& lockMeAngebot != null) {
			int iLockstate = getLockedByWerWas(lockMeAngebot);

			if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
				iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat
															// gelock zB Partner
			}

			lockstateValue.setIState(iLockstate);
		}

		return lockstateValue;
	}
}
