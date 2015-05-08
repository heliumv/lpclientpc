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
package com.lp.client.anfrage;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageerledigungsgrundDto;
import com.lp.server.personal.service.PersonalFac;

@SuppressWarnings("static-access")
public class PanelAnfrageerledigungsgrund extends PanelBasis {

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
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private AnfrageerledigungsgrundDto anfrageerledigungsgrundDto = null;

	public PanelAnfrageerledigungsgrund(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		anfrageerledigungsgrundDto = new AnfrageerledigungsgrundDto();

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
		DelegateFactory
				.getInstance()
				.getAnfrageServiceDelegate()
				.removeAnfrageerledigungsgrund(
						anfrageerledigungsgrundDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable{
		anfrageerledigungsgrundDto.setCBez(wtfBezeichnung.getText());
		anfrageerledigungsgrundDto.setMandantCNr(LPMain.getTheClient().getMandant());
	}

	protected void dto2Components() {
		wtfBezeichnung.setText(anfrageerledigungsgrundDto.getCBez());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (anfrageerledigungsgrundDto.getIId() == null) {
				anfrageerledigungsgrundDto.setIId(DelegateFactory
						.getInstance()
						.getAnfrageServiceDelegate()
						.createAnfrageerledigungsgrund(
								anfrageerledigungsgrundDto));
				setKeyWhenDetailPanel(anfrageerledigungsgrundDto.getIId());
			} else {
				DelegateFactory
						.getInstance()
						.getAnfrageServiceDelegate()
						.updateAnfrageerledigungsgrund(
								anfrageerledigungsgrundDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						anfrageerledigungsgrundDto.getIId() + "");
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

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setColumnsMax(PersonalFac.MAX_LOHNGRUPPE_BEZEICHNUNG);
		wtfBezeichnung.setText("");
		wtfBezeichnung.setMandatoryField(true);
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
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 0, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANFRAGEERLEDIGUNGSGRUND;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			anfrageerledigungsgrundDto = DelegateFactory.getInstance()
					.getAnfrageServiceDelegate()
					.anfrageerledigungsgrundFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
