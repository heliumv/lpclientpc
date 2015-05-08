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

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.MaschinenzmDto;

@SuppressWarnings("static-access")
public class PanelMaschinenzeitmodell extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MaschinenzmDto zeitmodellDto = null;
	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	static final public String ACTION_SPECIAL_ORT_FROM_LISTE = "action_ort_from_liste";
	static final public String ACTION_SPECIAL_KOSTENSTELLE_ABTEILUNG_FROM_LISTE = "action_kostenstelle_from_liste";
	static final public String ACTION_SPECIAL_HEIMATKOSTENSTELLE_FROM_LISTE = "action_heimatkostenstelle_from_liste";
	static final public String ACTION_SPECIAL_PARTNER_FROM_LISTE = "action_partner_from_liste";
	private WrapperLabel wlaKennung = new WrapperLabel();

	private WrapperTextField wtfKennung = new WrapperTextField();

	public InternalFrameZeiterfassung getInternalFrameZeiterfassung() {
		return internalFrameZeiterfassung;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	public PanelMaschinenzeitmodell(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		// leereAlleFelder(this);
		if (!getKeyWhenDetailPanel().equals(LPMain.getLockMeForNew())) {

			zeitmodellDto = DelegateFactory
					.getInstance()
					.getMaschineDelegate()
					.maschinezmFindByPrimaryKey(
							getInternalFrameZeiterfassung().getMaschinenzmDto()
									.getIId());

			dto2Components();

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameZeiterfassung().getMaschinenzmDto()
							.getCBez());
		}
	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(zeitmodellDto.getCBez());

	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfKennung.setMandatoryField(true);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, 0, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, 0, 1, 1, 0.2,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 200, 0));

		this.add(getPanelStatusbar(), new GridBagConstraints(0, 3, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		zeitmodellDto = new MaschinenzmDto();

		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MASCHINENZEITMODELL;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {
		zeitmodellDto.setCBez(wtfKennung.getText());
		zeitmodellDto.setMandantCNr(LPMain.getTheClient().getMandant());
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getMaschineDelegate()
				.removeMaschinezm(zeitmodellDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (zeitmodellDto.getIId() == null) {
				zeitmodellDto
						.setIId(DelegateFactory.getInstance()
								.getMaschineDelegate()
								.createMaschinenzm(zeitmodellDto));
				setKeyWhenDetailPanel(zeitmodellDto.getIId());
				internalFrameZeiterfassung.setMaschinenzmDto(zeitmodellDto);
			} else {
				DelegateFactory.getInstance().getMaschineDelegate()
						.updateMaschinenzm(zeitmodellDto);
				setKeyWhenDetailPanel(zeitmodellDto.getIId());
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						zeitmodellDto.getIId().toString());
			}
			eventYouAreSelected(false);
			zeitmodellDto = DelegateFactory.getInstance().getMaschineDelegate()
					.maschinezmFindByPrimaryKey(zeitmodellDto.getIId());
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		eI = (ItemChangedEvent) eI;
	}
}
