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
package com.lp.client.personal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.LohnartDto;
import com.lp.server.personal.service.LohnartstundenfaktorDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZulageDto;

@SuppressWarnings("static-access")
public class PanelLohnartstundenfaktor extends PanelBasis {

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
	private InternalFramePersonal internalFramePersonal = null;
	private WrapperLabel wlaLohnstundenart = new WrapperLabel();
	private WrapperComboBox wcoLohnstundenart = new WrapperComboBox();
	private WrapperLabel wlaFaktor = new WrapperLabel();
	private WrapperNumberField wnfFaktor = new WrapperNumberField();
	private WrapperSelectField wsfLohnart = null;
	private LohnartstundenfaktorDto lohnartstundenfaktorDto = null;
	private Integer letzteLohnart = null;
	private WrapperLabel wlaTagesart = new WrapperLabel();
	  private WrapperComboBox wcoTagesart = new WrapperComboBox();
	  

	public PanelLohnartstundenfaktor(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		wcoLohnstundenart.setMap(DelegateFactory.getInstance()
				.getPersonalDelegate().getAllLohnstundenarten());
		
		
		Map m=DelegateFactory.getInstance().getZeiterfassungDelegate().
        getAllSprTagesarten();
		
		wcoTagesart.emptyEntry=LPMain.getInstance().getTextRespectUISPr(
		"pers.lohnart.alle");
		wcoTagesart.setMap(m);
		
		
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfFaktor;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		lohnartstundenfaktorDto = new LohnartstundenfaktorDto();

		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeLohnartstundenfaktor(lohnartstundenfaktorDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		lohnartstundenfaktorDto.setLohnstundenartCNr((String) wcoLohnstundenart
				.getKeyOfSelectedItem());
		lohnartstundenfaktorDto.setFFaktor(wnfFaktor.getDouble());
		lohnartstundenfaktorDto.setLohnartIId(wsfLohnart.getIKey());
		lohnartstundenfaktorDto.setTagesartIId((Integer)wcoTagesart.getKeyOfSelectedItem());
		letzteLohnart = wsfLohnart.getIKey();
	}

	protected void dto2Components() throws Throwable {

		wcoLohnstundenart.setKeyOfSelectedItem(lohnartstundenfaktorDto
				.getLohnstundenartCNr());
		wcoTagesart.setKeyOfSelectedItem(lohnartstundenfaktorDto.getTagesartIId());
		wnfFaktor.setDouble(lohnartstundenfaktorDto.getFFaktor());
		wsfLohnart.setKey(lohnartstundenfaktorDto.getLohnartIId());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (lohnartstundenfaktorDto.getIId() == null) {
				lohnartstundenfaktorDto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate()
						.createLohnartstundenfaktor(lohnartstundenfaktorDto));
				setKeyWhenDetailPanel(lohnartstundenfaktorDto.getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updateLohnartstundenfaktor(lohnartstundenfaktorDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						lohnartstundenfaktorDto.getIId() + "");
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

		wsfLohnart = new WrapperSelectField(WrapperSelectField.LOHNART,
				getInternalFrame(), true);

		wlaFaktor.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.faktor"));

		wlaLohnstundenart.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.lohnstundenart"));

		wcoLohnstundenart.setMandatoryField(true);
		wnfFaktor.setMandatoryField(true);

		wlaTagesart.setText(LPMain.getInstance().getTextRespectUISPr("lp.tagesart"));
	   
		
		getInternalFrame().addItemChangedListener(this);
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

		iZeile++;
		jpaWorkingOn.add(wsfLohnart.getWrapperButton(), new GridBagConstraints(
				0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfLohnart.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaFaktor, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFaktor, new GridBagConstraints(1, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 50, 0));
		iZeile++;
		jpaWorkingOn.add(wlaLohnstundenart, new GridBagConstraints(0, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoLohnstundenart, new GridBagConstraints(1, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));
		iZeile++;
		jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(0, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(1, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LOHNARTSTUNDENFAKTOR;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			if (letzteLohnart != null) {
				wsfLohnart.setKey(letzteLohnart);
			}

			
			clearStatusbar();
		} else {
			lohnartstundenfaktorDto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.lohnartstundenfaktorFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}
}
