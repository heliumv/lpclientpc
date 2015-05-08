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

import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich Lieferantenkopfdaten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum 15.02.05</p>
 *
 * @author $Author: christian $
 *
 * @version $Revision: 1.7 $
 */
public class PanelLieferantkopfdaten extends PanelPartnerDetail {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaMwst = null;
	private WrapperComboBox wcoMwst = null;
	private WrapperLabel wlaKundennr = null;
	private WrapperTextField wtfKundennr = null;
	private WrapperCheckBox wcbMoeglicherLieferant = null;
	private WrapperLabel wlaEinheitProzentRabatt = null;
	private LockMeDto lockMePartner = null;

	public PanelLieferantkopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();
		initPanel();
	}

	void jbInit() throws Exception {
		resetToolsPanel();
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// Partnerfelder von der Oberklasse, es folgen die Lieferantenfelder.
		wlaMwst = new WrapperLabel();
		wlaMwst.setText(LPMain.getInstance().getTextRespectUISPr("part.mwst"));
		wcoMwst = new WrapperComboBox();
		// wcoMwst.setName(LPMain.getInstance().getTextRespectUISPr(
		// "part.mwst"));

		wlaKundennr = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.kundennummer"));
		wtfKundennr = new WrapperTextField();

		wcbMoeglicherLieferant = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("part.moeglicher_lieferant"));

		wlaEinheitProzentRabatt = new WrapperLabel();
		wlaEinheitProzentRabatt.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		wlaEinheitProzentRabatt.setHorizontalAlignment(SwingConstants.LEFT);

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaMwst, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoMwst, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheitProzentRabatt, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaKundennr, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKundennr, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMoeglicherLieferant, new GridBagConstraints(4, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		createAndSaveAndShowButton("/com/lp/client/res/book_open2.png",
				LPMain.getTextRespectUISPr("part.partner.export.vcard"),
				ACTION_SPECIAL_VCARD_EXPORT, null);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERANT;
	}

	protected void components2Dto() throws Throwable {

		super.components2Dto();

		getInternalFrameLieferant().getLieferantDto().setMwstsatzbezIId(
				(Integer) wcoMwst.getKeyOfSelectedItem());
		getInternalFrameLieferant().getLieferantDto().setCKundennr(
				wtfKundennr.getText());
		getInternalFrameLieferant().getLieferantDto().setBMoeglicherLieferant(
				wcbMoeglicherLieferant.getShort());
		// damit die Kreditorenkto. nicht anschlaegt.
		getInternalFrameLieferant().getLieferantDto()
				.setUpdateModeKreditorenkonto(
						LieferantDto.I_UPD_KREDITORENKONTO_KEIN_UPDATE);
		getInternalFrameLieferant().getLieferantDto().setBIgErwerb(false);
	}

	protected void dto2Components() throws Throwable {

		super.dto2Components();

		wcoMwst.setKeyOfSelectedItem(getInternalFrameLieferant()
				.getLieferantDto().getMwstsatzbezIId());
		wtfKundennr.setText(getInternalFrameLieferant().getLieferantDto()
				.getCKundennr());
		wcbMoeglicherLieferant.setShort(getInternalFrameLieferant()
				.getLieferantDto().getBMoeglicherLieferant());
	}

	private void initPanel() throws Throwable {
		wcoMwst.setMap(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.getAllMwstsatzbez(
						LPMain.getInstance().getTheClient().getMandant()));
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		lockMePartner = null;

		try {
			Integer iId = getInternalFrameLieferant().getLieferantDto()
					.getIId();

			if (iId == null) {
				// Create.
				super.eventYouAreSelected(true);
				leereAlleFelder(this);
				// MB 04.05.06 IMS 1671 MWST satz vorbelegen
				MandantDto mandantDto = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								LPMain.getInstance().getTheClient()
										.getMandant());
				// Aktuellen MWST-Satz uebersetzen.
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								mandantDto
										.getMwstsatzbezIIdStandardinlandmwstsatz());
				wcoMwst.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());

				if (getInternalFrameLieferant().getLieferantDto()
						.getPartnerIId() != null) {
					setBNeuAusPartner(true);

					// Neu aus Partner;
					getInternalFrameLieferant()
							.getLieferantDto()
							.setPartnerDto(
									DelegateFactory
											.getInstance()
											.getPartnerDelegate()
											.partnerFindByPrimaryKey(
													(Integer) getInternalFrameLieferant()
															.getLieferantDto()
															.getPartnerIId()));
					dto2Components();

					lockMePartner = new LockMeDto(HelperClient.LOCKME_PARTNER,
							getInternalFrameLieferant().getLieferantDto()
									.getPartnerIId() + "", LPMain.getInstance()
									.getCNrUser());

					eventActionLock(null);
					dto2Components();
				}
				clearStatusbar();
			} else {
				// Update.
				getInternalFrameLieferant().setLieferantDto(
						DelegateFactory.getInstance().getLieferantDelegate()
								.lieferantFindByPrimaryKey(iId));
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFrameLieferant().getLieferantDto()
								.getPartnerDto().formatFixTitelName1Name2());

				lockMePartner = new LockMeDto(HelperClient.LOCKME_PARTNER,
						getInternalFrameLieferant().getLieferantDto()
								.getPartnerIId() + "", LPMain.getInstance()
								.getCNrUser());

				super.eventYouAreSelected(true);

				updateComponents();
				dto2Components();
				setStatusbar();
			}
		} catch (ExceptionLP elp) {
			// lock fehlgeschlagen: Code korr. fuer Meldung
			elp.setICode(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED);
			// handleex: Der Aufruf.
			handleException(elp, false);
			throw new ExceptionLP(EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING,
					null);
		}
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

	/**
	 * updateComponents
	 */
	private void updateComponents() {
		// nothing here
	}

	protected void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getPartnerDto().getPersonalIIdAnlegen());
		setStatusbarPersonalIIdAendern(getPartnerDto().getPersonalIIdAendern());
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
		super.eventActionNew(eventObject, true, false);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!bNeedNoDeleteI) {
			if (!isLockedDlg()) {
				DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.removeLieferant(
								getInternalFrameLieferant().getLieferantDto());

				getInternalFrameLieferant().setLieferantDto(new LieferantDto());

				super.eventActionDelete(e, true, true);
			}
		}
	}

	protected void setDefaults() throws Throwable {

		getInternalFrameLieferant().getLieferantDto().setMandantCNr(
				LPMain.getInstance().getTheClient().getMandant());

		super.setDefaults();

		// getPartnerDto().setPartnerartCNr(PartnerFac.PARTNERART_FIRMA);

		getInternalFrameLieferant().getLieferantDto().setBBeurteilen(
				Helper.boolean2Short(true));
		getInternalFrameLieferant().getLieferantDto().setBReversecharge(
				Helper.boolean2Short(false));

		getInternalFrameLieferant().getLieferantDto().setBVersteckt(
				Helper.boolean2Short(false));

		getInternalFrameLieferant().getLieferantDto().setBMoeglicherLieferant(
				Helper.boolean2Short(false));

		MandantDto mandantDto = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant());

		getInternalFrameLieferant().getLieferantDto().setIdSpediteur(
				mandantDto.getSpediteurIIdLF());
		getInternalFrameLieferant().getLieferantDto().setLieferartIId(
				mandantDto.getLieferartIIdLF());
		getInternalFrameLieferant().getLieferantDto().setZahlungszielIId(
				mandantDto.getZahlungszielIIdLF());

		getInternalFrameLieferant().getLieferantDto().setWaehrungCNr(
				mandantDto.getWaehrungCNr());
	}

	/**
	 * Behandle Ereignis Save.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			checkLockedDlg();
			if (lockMePartner != null) {
				checkLockedDlg(lockMePartner);
			}
			components2Dto();

			LieferantDto lieferantDto = getInternalFrameLieferant()
					.getLieferantDto();

			if (lieferantDto.getIId() == null) {
				// create
				Integer key = DelegateFactory.getInstance()
						.getLieferantDelegate().createLieferant(lieferantDto);
				setKeyWhenDetailPanel(key);
				lieferantDto.setIId(key);
			} else {
				// update
				DelegateFactory.getInstance().getLieferantDelegate()
						.updateLieferant(lieferantDto);
				setKeyWhenDetailPanel(lieferantDto.getIId());
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				// der erste eintrag wurde angelegt
				getInternalFrame().setKeyWasForLockMe(
						getKeyWhenDetailPanel() + "");
			}
			eventYouAreSelected(false);
			getInternalFrame().setKeyWasForLockMe(getKeyWhenDetailPanel() + "");
			setStatusbar();
			dto2Components();
		}
	}

	public void setDefaultMWSTforLand(LandDto landDto) {
		try {
			MandantDto mandantDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mandantFindByPrimaryKey(
							LPMain.getInstance().getTheClient().getMandant());
			String sLKZMandant = mandantDto.getPartnerDto().getLandplzortDto()
					.getLandDto().getCLkz();
			// Land != Mandantenland => ausland
			if (!landDto.getCLkz().equals(sLKZMandant)) {

				if (landDto.getEUMitglied() == null
						|| landDto.getEUMitglied().after(
								new java.sql.Date(System.currentTimeMillis()))) {
					// Fuer Drittland soll MWST-Satz wie in Mandant definiert
					// vorbesetzt werden
					Integer mwstSatzIId = mandantDto
							.getMwstsatzbezIIdStandarddrittlandmwstsatz();
					if (mwstSatzIId != null) {
						wcoMwst.setKeyOfSelectedItem(mwstSatzIId);
					}
				} else {
					// Fuer EU-Ausland soll MWST-Satz wie in Mandant definiert
					// vorbesetzt werden
					Integer mwstSatzIId = mandantDto
							.getMwstsatzbezIIdStandardauslandmwstsatz();
					if (mwstSatzIId != null) {
						wcoMwst.setKeyOfSelectedItem(mwstSatzIId);
					}
				}

			} else {
				Integer mwstSatzIId = mandantDto
						.getMwstsatzbezIIdStandardinlandmwstsatz();
				if (mwstSatzIId != null) {
					wcoMwst.setKeyOfSelectedItem(mwstSatzIId);
				}
			}
		} catch (Throwable e) {
			// Sollte nicht vorkommen... und wenn doch dann gibts keinen MWST
			// vorbesetzt
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		getInternalFrameLieferant().getLieferantDto().setPartnerDto(partnerDto);
	}

	protected PartnerDto getPartnerDto() {
		return getInternalFrameLieferant().getLieferantDto().getPartnerDto();
	}

	private InternalFrameLieferant getInternalFrameLieferant() {
		return (InternalFrameLieferant) getInternalFrame();
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

		// Kunden locken.
		super.eventActionLock(e);

		if (lockMePartner != null) {
			// Zugehoerigen Partner locken.
			super.lock(lockMePartner);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		// immer
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;
		// Ort wurde ausgewaehlt
		if (e.getSource() == panelQueryFLROrt) {
			// Abfrage ob Kunde angelegt oder editiert wird (IID=null =>
			// anlegen)
			if (getInternalFrameLieferant().getLieferantDto().getIId() == null
					&& getPartnerDto().getLandplzortDto() != null
					&& getPartnerDto().getLandplzortDto().getLandDto() != null) {
				setDefaultMWSTforLand(getPartnerDto().getLandplzortDto()
						.getLandDto());
			}
		}

	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

		// Kunde unlocken.
		super.eventActionUnlock(e);

		if (lockMePartner != null) {
			// Zugehoerigen Partner unlocken.
			super.unlock(lockMePartner);
		}
	}

}
