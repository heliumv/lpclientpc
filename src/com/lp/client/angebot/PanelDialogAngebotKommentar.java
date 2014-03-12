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

import javax.swing.JComponent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKommentar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LockMeDto;

/**
 * <p>
 * <I>Dialog zur Eingabe des internen Kommentars fuer ein Angebot.</I>
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
 * @version $Revision: 1.5 $
 */
public class PanelDialogAngebotKommentar extends PanelDialogKommentar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAngebot intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAngebot tpAngebot = null;

	/** Wenn der Dialog von aussen geoeffnet wird, muss ein Lock gesetzt werden. */
	private LockMeDto lockMeAngebot = null;

	/** Der Kommentar kann intern oder extern sein. */
	private boolean bInternerKommentar = false;

	public PanelDialogAngebotKommentar(InternalFrame internalFrame,
			String add2TitleI, boolean bInternerKommentarI) throws Throwable {
		super(internalFrame, add2TitleI);

		intFrame = (InternalFrameAngebot) getInternalFrame();
		tpAngebot = intFrame.getTabbedPaneAngebot();
		bInternerKommentar = bInternerKommentarI;

		jbInit();
		initComponents();
		dto2Components();
	}

	private void jbInit() throws Throwable {
		if (tpAngebot.getAngebotDto() != null) {
			lockMeAngebot = new LockMeDto(HelperClient.LOCKME_ANGEBOT,
					tpAngebot.getAngebotDto().getIId() + "", LPMain
							.getInstance().getCNrUser());
		}

		// explizit ausloesen, weil weder new noch update aufgerufen werden
		eventActionLock(null);
	}

	private void dto2Components() throws Throwable {
		if (this.bInternerKommentar) {
			getLpEditor().setText(
					tpAngebot.getAngebotDto().getXInternerkommentar());
			setCBestehenderKommentar(tpAngebot.getAngebotDto()
					.getXInternerkommentar());
		} else {
			getLpEditor().setText(
					tpAngebot.getAngebotDto().getXExternerkommentar());
			setCBestehenderKommentar(tpAngebot.getAngebotDto()
					.getXExternerkommentar());
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (super.bHandleEventInSuperklasse) {
			// explizit aufrufen, weil weder save noch discard aufgerufen werden
			eventActionUnlock(null);
			tpAngebot.refreshAktuellesPanel(); // den Titel des aktuellen Panels
												// setzen
		}
	}

	/*
	 * protected void eventActionEscape(ActionEvent e) throws Throwable { if
	 * (e.getActionCommand().equals(PanelBasis.ESC)||
	 * e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
	 * eventActionSpecial(e); } }
	 */

	/**
	 * Den Kommentar im Angebot abspeichern.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void saveKommentar() throws Throwable {
		if (lockMeAngebot != null) {
			checkLockedDlg(lockMeAngebot);
		}

		if (this.bInternerKommentar) {
			tpAngebot.getAngebotDto().setXInternerkommentar(
					getLpEditor().getText());
		} else {
			tpAngebot.getAngebotDto().setXExternerkommentar(
					getLpEditor().getText());
		}

		DelegateFactory.getInstance().getAngebotDelegate()
				.updateAngebotOhneWeitereAktion(tpAngebot.getAngebotDto());
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

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}
}
