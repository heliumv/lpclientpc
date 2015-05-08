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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.MandantFac;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um die Bankdaten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum 22.03.05</p>
 *
 * @author $Author: christian $
 * @version $Revision: 1.6 $
 */
public class PanelBank extends PanelPartnerDetail {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneBank tpBank = null;

	private WrapperLabel wlaBLZ = null;
	private WrapperTextField wtfBLZ = null;
	private WrapperLabel wlaBIC = null; // Bank Identifier Code
	private WrapperTextField wtfBIC = null;
	private LockMeDto lockMePartner = null;

	public PanelBank(InternalFrame internalFrame, String add2TitleI,
			Object keyI, TabbedPaneBank tpBank) throws Throwable {

		super(internalFrame, add2TitleI, keyI);
		this.tpBank = tpBank;
		jbInit();
		initComponents();
	}

	void jbInit() throws Exception {

		wlaBLZ = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.blz"));
		wtfBLZ = new WrapperTextField(BankFac.MAX_BLZ);

		wlaBIC = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.bic"));
		wtfBIC = new WrapperTextField(BankFac.MAX_BIC);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZAHLUNGSVORSCHLAG)) {
			wtfLandPLZOrt.setMandatoryField(true);
		}

		// Zeile.
		iZeile++;
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaBLZ, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBLZ, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBIC, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBIC, new GridBagConstraints(4, iZeile, 4, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BANK;
	}

	protected void components2Dto() throws Throwable {

		super.components2Dto();

		getPartnerDto().setPartnerartCNr(PartnerFac.PARTNERART_SONSTIGES);
		getBankDto().setCBlz(wtfBLZ.getText());
		getBankDto().setCBic(wtfBIC.getText());
	}

	private BankDto getBankDto() {
		return getTabbedPaneBank().getBankDto();
	}

	protected void dto2Components() throws Throwable {

		super.dto2Components();

		wtfBLZ.setText(getBankDto().getCBlz());
		wtfBIC.setText(getBankDto().getCBic());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		lockMePartner = null;

		Integer iId = getTabbedPaneBank().getBankDto().getPartnerIId();

		if (iId == null) {
			// Neu
			super.eventYouAreSelected(true);
			leereAlleFelder(this);
			setPartnerDto(null);
			setDefaults();

			if (getTabbedPaneBank().getBankDto().getPartnerIIdNeuAus() != null) {
				// Neu aus Partner;
				setBNeuAusPartner(true);

				getTabbedPaneBank().getBankDto().setPartnerDto(
						DelegateFactory
								.getInstance()
								.getPartnerDelegate()
								.partnerFindByPrimaryKey(
										(Integer) getTabbedPaneBank()
												.getBankDto()
												.getPartnerIIdNeuAus()));
				dto2Components();

				lockMePartner = new LockMeDto(HelperClient.LOCKME_PARTNER,
						getTabbedPaneBank().getBankDto().getPartnerIIdNeuAus()
								+ "", LPMain.getInstance().getCNrUser());
				try {
					// versuche Partner und Bank zu locken.
					eventActionLock(null);
				} catch (ExceptionLP elp) {
					// lock fehgeschlagen: Code korr. fuer Meldung
					elp.setICode(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED);
					handleException(elp, false);
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING, null);
				}
			}
			clearStatusbar();
		} else {
			// Update
			getTabbedPaneBank().setBankDto(
					DelegateFactory.getInstance().getPartnerbankDelegate()
							.bankFindByPrimaryKey(iId));

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getTabbedPaneBank().getBankDto().getPartnerDto()
							.getCName1nachnamefirmazeile1());

			super.eventYouAreSelected(true);

			setStatusbar();
			dto2Components();
			updateComponents();
		}
	}

	/**
	 * updateComponents
	 */
	private void updateComponents() {
		wbuPartnerklasse.setVisible(false);
		wtfPartnerklasse.setVisible(false);
		wbuBranche.setVisible(false);
		wtfBranche.setVisible(false);
	}

	protected void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getPartnerDto().getPersonalAnlegenIId());
		setStatusbarPersonalIIdAendern(getPartnerDto().getPersonalAendernIId());
		setStatusbarTAendern(getPartnerDto().getTAendern());
		setStatusbarTAnlegen(getPartnerDto().getTAnlegen());
	}

	/**
	 * eventActionNew
	 * 
	 * @param eventObject
	 *            der event
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		lockMePartner = null;
		super.eventActionNew(eventObject, true, true);
		getTabbedPaneBank().getBankDto().setPartnerIId(null);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!bNeedNoDeleteI) {
			if (!isLockedDlg()) {
				DelegateFactory
						.getInstance()
						.getPartnerbankDelegate()
						.removeBank(
								getTabbedPaneBank().getBankDto()
										.getPartnerIId());

				getTabbedPaneBank().setPartnerBankDto(new PartnerbankDto());

				super.eventActionDelete(e, true, true);
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
			if (wtfBLZ.getText() == null && wtfBIC.getText() == null) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("part.blzbic.error"));
				return;
			}

			checkLockedDlg();
			if (lockMePartner != null) {
				checkLockedDlg(lockMePartner);
			}

			components2Dto();

			BankDto bankDto = getTabbedPaneBank().getBankDto();

			if (bankDto.getPartnerIId() == null) {
				// create
				Integer iId = DelegateFactory.getInstance()
						.getPartnerbankDelegate().createBank(bankDto);

				setKeyWhenDetailPanel(iId);

				bankDto.setPartnerIId(iId);
			} else {
				// update
				DelegateFactory.getInstance().getPartnerbankDelegate()
						.updateBank(bankDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				// der erste eintrag wurde angelegt
				getInternalFrame().setKeyWasForLockMe(
						getKeyWhenDetailPanel() + "");
			}

			eventYouAreSelected(false);

			// dem internalframe den zu lockenden setzen.
			getInternalFrame().setKeyWasForLockMe(getKeyWhenDetailPanel() + "");

			setStatusbar();

			dto2Components();
		}
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		getTabbedPaneBank().getBankDto().setPartnerDto(partnerDto);
	}

	protected PartnerDto getPartnerDto() {
		return getTabbedPaneBank().getBankDto().getPartnerDto();
	}

	private TabbedPaneBank getTabbedPaneBank() {
		return tpBank;
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED
				&& lockMePartner != null) {
			int iLockstate = getLockedByWerWas(lockMePartner);

			if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
				iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat
															// gelock zB Partner
			}
			lockstateValue.setIState(iLockstate);
		}

		return lockstateValue;
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

		// Bank locken.
		super.eventActionLock(e);

		if (lockMePartner != null) {
			// Zugehoerigen Partner locken.
			super.lock(lockMePartner);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

		// Bank unlocken.
		super.eventActionUnlock(e);

		if (lockMePartner != null) {
			// Zugehoerigen Partner unlocken.
			super.unlock(lockMePartner);
		}
	}
}
