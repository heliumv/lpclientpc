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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.MaschinenzmtagesartDto;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelMaschinenzmtagesart extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameZeiterfassung internalFramePersonal = null;

	private MaschinenzmtagesartDto zeitmodelltagDto = null;

	private WrapperLabel wlaTagesart = new WrapperLabel();
	private WrapperComboBox wcoTagesart = new WrapperComboBox();

	private WrapperLabel wlaVerfuegbarkeit = new WrapperLabel();
	private WrapperNumberField wnfVerfuegbarkeit = new WrapperNumberField();

	public PanelMaschinenzmtagesart(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFrameZeiterfassung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		wcoTagesart.setMap(DelegateFactory.getInstance()
				.getZeiterfassungDelegate().getAllSprTagesarten());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoTagesart;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		zeitmodelltagDto = new MaschinenzmtagesartDto();

		leereAlleFelder(this);
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getMaschineDelegate()
				.removeMaschinenzmtagesart(zeitmodelltagDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		zeitmodelltagDto.setMaschinenzmIId(internalFramePersonal
				.getMaschinenzmDto().getIId());
		zeitmodelltagDto.setTagesartIId((Integer) wcoTagesart
				.getKeyOfSelectedItem());
		zeitmodelltagDto.setLVerfuegbarkeit(Helper.verfuegbarkeitvonStundenEinesTagesNachLong(wnfVerfuegbarkeit.getBigDecimal()));

	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wcoTagesart.setKeyOfSelectedItem(zeitmodelltagDto.getTagesartIId());
		wnfVerfuegbarkeit.setBigDecimal(Helper
				.verfuegbarkeitvonLongNachStundenEinesTages(zeitmodelltagDto
						.getLVerfuegbarkeit()));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (zeitmodelltagDto.getIId() == null) {

				zeitmodelltagDto.setIId(DelegateFactory.getInstance()
						.getMaschineDelegate()
						.createMaschinenzmtagesart(zeitmodelltagDto));
				setKeyWhenDetailPanel(zeitmodelltagDto.getIId());
			} else {
				DelegateFactory.getInstance().getMaschineDelegate()
						.updateMaschinenzmtagesart(zeitmodelltagDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame()
						.setKeyWasForLockMe(
								internalFramePersonal.getMaschinenzmDto()
										.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);
		wlaTagesart.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.tagesart"));
		wcoTagesart.setMandatoryField(true);
		wnfVerfuegbarkeit.setMandatoryField(true);
		wlaVerfuegbarkeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.maschinenzeitmodelltag.verfugbarkeit"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		int iZeile = 0;

		jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(0, iZeile, 1, 1,
				0.30, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(1, iZeile, 5, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wlaVerfuegbarkeit, new GridBagConstraints(3, iZeile,
				1, 1, 0.25, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfVerfuegbarkeit, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -80, 0));

		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MASCHINENZEITMODELL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			zeitmodelltagDto = DelegateFactory.getInstance()
					.getMaschineDelegate()
					.maschinezmtagesartFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}
}
