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
package com.lp.client.partner;

import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Diese Klasse kuemmert sich die Lieferantenrechnungsadresse.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>18.03.05</I>
 * </p>
 * 
 * @author $Author: valentin $
 * 
 * @version $Revision: 1.3 $
 */
public class PanelLieferantRechnungsadresse extends PanelPartnerDetail {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String EXTRA_NEU_AUS_PARTNER = "neu_aus_partner";
	private PanelQueryFLR panelPartner = null;
	protected LockMeDto lockMePartner = null;
	private LockMeDto lockMePartnerRE = null;
	private LockMeDto lockMePartnerZuLF = null;

	public PanelLieferantRechnungsadresse(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);
		jbInit();
		initComponents();
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		getInternalFrameLieferant().getLieferantDto()
				.setPartnerRechnungsadresseDto(partnerDto);
	}

	protected PartnerDto getPartnerDto() {
		return getInternalFrameLieferant().getLieferantDto()
				.getPartnerRechnungsadresseDto();
	}

	private InternalFrameLieferant getInternalFrameLieferant() {
		return (InternalFrameLieferant) getInternalFrame();
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

		// LF locken.
		super.eventActionLock(e);

		// Partner RE locken.
		lockMePartnerRE = new LockMeDto(HelperClient.LOCKME_PARTNER,
				getInternalFrameLieferant().getLieferantDto()
						.getPartnerIIdRechnungsadresse() + "", LPMain
						.getInstance().getCNrUser());
		if (getLockedByWerWas(lockMePartnerRE) == LOCK_IS_NOT_LOCKED) {
			super.lock(lockMePartnerRE);
		} else {
			clearRE();
			throw new ExceptionLP(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED, null);
		}

		// Zugehoerigen Partner zum LF locken.
		lockMePartnerZuLF = new LockMeDto(HelperClient.LOCKME_PARTNER,
				getInternalFrameLieferant().getLieferantDto().getPartnerDto()
						.getIId()
						+ "", LPMain.getInstance().getCNrUser());
		if (getLockedByWerWas(lockMePartnerZuLF) == LOCK_IS_NOT_LOCKED) {
			super.lock(lockMePartnerZuLF);
		} else {
			clearRE();
			super.unlock(lockMePartnerRE);
			throw new ExceptionLP(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED, null);
		}
	}

	private void clearRE() throws Throwable {

		setPartnerDto(null);
		getInternalFrameLieferant().getLieferantDto()
				.setPartnerIIdRechnungsadresse(null);

		super.eventActionUnlock(null);
		super.eventActionRefresh(null, false);

		LockStateValue lockstateValue = new LockStateValue(null, null,
				LOCK_IS_NOT_LOCKED);
		updateButtons(lockstateValue);
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

		// LF unlocken.
		super.eventActionUnlock(e);

		// Partner RE unlocken.
		if (getLockedByWerWas(lockMePartnerRE) == LOCK_IS_LOCKED_BY_ME) {
			super.unlock(lockMePartnerRE);
		} else {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_LOCK_NOTFOUND, null);
		}

		// Zugehoerigen Partner zum LF unlocken.
		if (getLockedByWerWas(lockMePartnerZuLF) == LOCK_IS_LOCKED_BY_ME) {
			super.unlock(lockMePartnerZuLF);
		} else {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_LOCK_NOTFOUND, null);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);

		if (!bNeedNoYouAreSelectedI) {
			Object iId = getInternalFrameLieferant().getLieferantDto().getIId();

			// Lieferanten lesen.
			getInternalFrameLieferant().setLieferantDto(
					(DelegateFactory.getInstance().getLieferantDelegate()
							.lieferantFindByPrimaryKey((Integer) iId)));

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameLieferant().getLieferantDto()
							.getPartnerDto().formatFixTitelName1Name2());

			if (getInternalFrameLieferant().getLieferantDto()
					.getPartnerIIdRechnungsadresse() == null) {
				leereAlleFelder(this);
				setDefaults();
				clearStatusbar();
				((LPButtonAction) getHmOfButtons().get(ACTION_DELETE))
						.getButton().setEnabled(false);
			} else {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						getPartnerDto().formatAnrede());

				String t1 = getInternalFrameLieferant().getLieferantDto()
						.getPartnerDto().formatFixTitelName1Name2();
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						t1
								+ " | "
								+ getInternalFrameLieferant().getLieferantDto()
										.getPartnerRechnungsadresseDto()
										.formatAnrede());

				setStatusbar();

				dto2Components();
			}
		}
	}

	/**
	 * Behandle Ereignis Save.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			LieferantDto lieferantDto = getInternalFrameLieferant()
					.getLieferantDto();
			// damit die Kreditorenkto. nicht anschlaegt.
			getInternalFrameLieferant().getLieferantDto()
					.setUpdateModeKreditorenkonto(
							LieferantDto.I_UPD_KREDITORENKONTO_KEIN_UPDATE);

			if (lieferantDto.getIId() == null) {
				// Create.
				throw new Exception("lieferantDto.getIId() == null");
			} else {
				// Update.
				DelegateFactory.getInstance().getLieferantDelegate()
						.updateLieferantRechnungsadresse(lieferantDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				// Der erste Eintrag wurde angelegt.
				getInternalFrame().setKeyWasForLockMe(
						getKeyWhenDetailPanel() + "");
			}

			eventYouAreSelected(false);
			setStatusbar();
			dto2Components();
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (getLockedstateDetailMainKey().getIState() != PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.removeLieferantPartnerRechnungsadresse(
							getInternalFrameLieferant().getLieferantDto());

			getInternalFrameLieferant().setLieferantDto(new LieferantDto());

			super.eventActionDelete(e, false, true);
		}
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);

		if (getInternalFrameLieferant().getLieferantDto()
				.getPartnerIIdRechnungsadresse() == null) {
			((LPButtonAction) getHmOfButtons().get(ACTION_DELETE)).getButton()
					.setEnabled(false);
		}
	}

	private void jbInit() throws Throwable {
		createAndSaveAndShowButton(
				"/com/lp/client/res/businessmen16x16.png",
				LPMain.getTextRespectUISPr("part.tooltip.rechnungsadr_from_partner"),
				PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_PARTNER, null);
	}

	// protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
	// throws Throwable {
	//
	// super.eventActionUpdate(aE, bNeedNoUpdateI);
	//
	// if
	// (getInternalFrameLieferant().getLieferantDto().getPartnerIIdRechnungsadresse()
	// != null) {
	// // --Via Neu aus .. angelegt->locken.
	// lockMePartner = new LockMeDto(
	// HelperClient.LOCKME_PARTNER,
	// getInternalFrameLieferant().getLieferantDto().getPartnerIIdRechnungsadresse()
	// +
	// "",
	// LPMain.getInstance().getCNrUser());
	//
	// if (getLockedByWerWas(lockMePartner) == LOCK_IS_NOT_LOCKED) {
	// lock(lockMePartner);
	// }
	// else {
	// throw new ExceptionLP(
	// ExceptionLP.FEHLER_IS_ALREADY_LOCKED, null);
	// }
	// }
	// }

	protected void eventItemchanged(EventObject eI) throws Throwable {

		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelPartner) {
				// Neu aus Partner;
				Integer iIdPartnerRE = (Integer) panelPartner.getSelectedId();

				// lockMePartner = new LockMeDto(
				// HelperClient.LOCKME_PARTNER,
				// iIdPartnerRE + "",
				// LPMain.getInstance().getCNrUser());
				// if (getLockedByWerWas(lockMePartner) == LOCK_IS_NOT_LOCKED) {
				// lock(lockMePartner);
				// }
				// else {
				// throw new ExceptionLP(ExceptionLP.FEHLER_IS_ALREADY_LOCKED,
				// null);
				// }

				// leereAlleFelder(this);
				// setDefaults();
				//
				// eventActionNew(eI, false, false);
				// updateButtons(LOCK_DETERMINE_LOCK_INTERNAL);
				//
				// setPartnerDto(getInternalFrame().getPartnerDelegate().partnerFindByPrimaryKey(
				// iIdPartnerRE));
				// dto2Components();
				// Neu aus Partner.

				eventYouAreSelected(false);

				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey(iIdPartnerRE);

				LockStateValue lockstateValue = getLockedstateDetailMainKey();
				lockstateValue.setIState(LOCK_IS_LOCKED_BY_ME);
				updateButtons(lockstateValue);

				eventActionUpdate(null, false);
				setPartnerDto(partnerDto);
				getInternalFrameLieferant().getLieferantDto()
						.setPartnerIIdRechnungsadresse(iIdPartnerRE);
				getInternalFrameLieferant().getLieferantDto()
						.setPartnerRechnungsadresseDto(partnerDto);

				dto2Components();
			}
			clearStatusbar();
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		super.eventActionSpecial(e);

		if (e.getActionCommand().endsWith(EXTRA_NEU_AUS_PARTNER)) {
			panelPartner = PartnerFilterFactory.getInstance()
					.createPanelFLRPartner(getInternalFrame());
			new DialogQuery(panelPartner);
		}
	}

	protected void setDefaults() throws Throwable {

		super.setDefaults();

		// Ist fix Adresse.
		wcoPartnerart.setActivatable(false);
		wcoPartnerart.setEnabled(false);
		wcoPartnerart.setKeyOfSelectedItem(PartnerFac.PARTNERART_ADRESSE);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERANT;
	}

}
