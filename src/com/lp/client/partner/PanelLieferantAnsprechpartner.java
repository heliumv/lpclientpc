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

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Lieferantenansprechpartner.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>27.03.05</I>
 * </p>
 * 
 * @author $Author: valentin $
 * @version $Revision: 1.3 $
 */
public class PanelLieferantAnsprechpartner extends PanelAnsprechpartner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelLieferantAnsprechpartner(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {
		super(internalFrame, add2TitleI, keyI);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERANT;
	}

	private InternalFrameLieferant getInternalFrameLieferant() {
		return ((InternalFrameLieferant) getInternalFrame());
	}

	protected PartnerDto getPartnerDto() {
		return getInternalFrameLieferant().getAnsprechpartnerDto()
				.getPartnerDto();
	}

	protected AnsprechpartnerDto getAnsprechpartnerDto() {
		return getInternalFrameLieferant().getAnsprechpartnerDto();
	}

	protected void setAnsprechpartnerDto(AnsprechpartnerDto ansprechpartnerDtoI) {
		getInternalFrameLieferant().setAnsprechpartnerDto(ansprechpartnerDtoI);
	}

	protected void setDefaults() throws Throwable {

		super.setDefaults();

		Integer iId = getInternalFrameLieferant().getLieferantDto().getIId();
		LieferantDto lieferantDto = DelegateFactory.getInstance()
				.getLieferantDelegate().lieferantFindByPrimaryKey(iId);
		getInternalFrameLieferant().getAnsprechpartnerDto().setPartnerIId(
				lieferantDto.getPartnerIId());

		// eine neue Position bekommt ein eindeutiges iSort
		Integer iSort = DelegateFactory
				.getInstance()
				.getAnsprechpartnerDelegate()
				.getMaxISort(
						getInternalFrameLieferant().getLieferantDto()
								.getPartnerIId());
		iSort = new Integer(iSort.intValue() + 1);
		wtfSort.setInteger(iSort);
	}

	public void eventActionSaveImpl(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		Integer iIdAnsprechpartner = null;

		components2Dto();
		speicherePartner();

		if (getAnsprechpartnerDto().getIId() == null) {
			// create.
			iIdAnsprechpartner = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.createAnsprechpartner(
							getInternalFrameLieferant().getAnsprechpartnerDto());

			// dem dto den key setzen.
			getAnsprechpartnerDto().setIId(iIdAnsprechpartner);

			// diesem panel den key setzen.
			setKeyWhenDetailPanel(iIdAnsprechpartner);
		} else {
			// update
			DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.updateAnsprechpartner(
							getInternalFrameLieferant().getAnsprechpartnerDto());
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.removeAnsprechpartner(
							getInternalFrameLieferant().getAnsprechpartnerDto());

			getAnsprechpartnerDto().setIId(null);
			this.setKeyWhenDetailPanel(null);

			leereAlleFelder(this);

			super.eventActionDelete(e, false, true);
		}
	}

	protected String getSelectedPartnerTitelAnrede() {

		String t1 = getInternalFrameLieferant().getLieferantDto()
				.getPartnerDto().formatFixTitelName1Name2();
		if (getAnsprechpartnerDto().getIId() != null) {
			t1 += " | "
					+ getAnsprechpartnerDto().getPartnerDto()
							.formatFixTitelName1Name2();
		}
		return t1;
	}

}
