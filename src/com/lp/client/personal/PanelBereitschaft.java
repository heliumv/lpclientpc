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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.zeiterfassung.InternalFrameZeiterfassung;
import com.lp.server.personal.service.BereitschaftDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

public class PanelBereitschaft extends PanelBasis {

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
	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;
	private BereitschaftDto bereitschaftDto = null;

	private WrapperLabel wlaBeginn = new WrapperLabel();
	private WrapperTimestampField wdfVon = new WrapperTimestampField();
	private WrapperLabel wlaEnde = new WrapperLabel();
	private WrapperTimestampField wdfBis = new WrapperTimestampField();
	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();

	private WrapperSelectField wsfBereitschaftart = new WrapperSelectField(
			WrapperSelectField.BEREITSCHAFTSART, getInternalFrame(), true);
	
	public PanelBereitschaft(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	protected void setDefaults() {

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
			wdfVon.setTimestamp(new java.sql.Timestamp(System
					.currentTimeMillis()));

		} else {
			bereitschaftDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.bereitschaftFindByPrimaryKey((Integer) key);

			dto2Components();

		}

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		bereitschaftDto = new BereitschaftDto();

	}

	protected void dto2Components() throws Throwable {
		
		wsfBereitschaftart.setKey(bereitschaftDto.getBereitschaftartIId());
		wdfVon.setTimestamp(bereitschaftDto.getTBeginn());
		wdfBis.setTimestamp(bereitschaftDto.getTEnde());
		wtfBemerkung.setText(bereitschaftDto.getCBemerkung());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		boolean bDatumgueltig = true;
		if (wdfVon.getTimestamp() != null) {
			if (wdfBis.getTimestamp() != null) {

				if (wdfBis.getTimestamp().getTime() < wdfVon.getTimestamp()
						.getTime()) {
					bDatumgueltig = false;
				}
			}
		}
		if (bDatumgueltig) {
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();
				if (bereitschaftDto.getIId() == null) {
					bereitschaftDto.setPersonalIId(internalFrameZeiterfassung
							.getPersonalDto().getIId());
					bereitschaftDto.setIId(DelegateFactory.getInstance()
							.getZeiterfassungDelegate()
							.createBereitschaft(bereitschaftDto));
					setKeyWhenDetailPanel(bereitschaftDto.getIId());
				} else {
					DelegateFactory.getInstance().getZeiterfassungDelegate()
							.updateBereitschaft(bereitschaftDto);
				}
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							internalFrameZeiterfassung.getMaschineDto()
									.getIId().toString());
				}
				eventYouAreSelected(false);
			}
		} else {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.bisvorvon"));
		}
	}

	protected void components2Dto() throws ExceptionLP {
		bereitschaftDto.setTBeginn(wdfVon.getTimestamp());
		bereitschaftDto.setCBemerkung(wtfBemerkung.getText());
		bereitschaftDto.setTEnde(wdfBis.getTimestamp());
		bereitschaftDto.setBereitschaftartIId(wsfBereitschaftart.getIKey());
		bereitschaftDto.setPersonalIId(internalFrameZeiterfassung
				.getPersonalDto().getIId());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getZeiterfassungDelegate()
				.removeBereitschaft(bereitschaftDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

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

		wlaBeginn.setText(LPMain.getTextRespectUISPr("lp.beginn"));
		wlaEnde.setText(LPMain.getTextRespectUISPr("lp.ende"));

		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));


		wtfBemerkung.setColumnsMax(80);
		
		getInternalFrame().addItemChangedListener(this);
		wsfBereitschaftart.setMandatoryField(true);
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);

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

		jpaWorkingOn.add(wsfBereitschaftart.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wsfBereitschaftart.getWrapperTextField(), new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaBeginn, new GridBagConstraints(0, iZeile, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaEnde, new GridBagConstraints(2, iZeile, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 3, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

}
