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

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalzeitmodellDto;
import com.lp.server.personal.service.SchichtzeitmodellDto;
import com.lp.server.personal.service.ZeitmodellDto;

@SuppressWarnings("static-access")
public class PanelSchichtzeitmodell extends PanelBasis {

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
	private SchichtzeitmodellDto schichtzeitmodellDto = null;
	private WrapperButton wbuZeitmodell = new WrapperButton();
	private WrapperTextField wtfZeitmodell = new WrapperTextField();
	private WrapperTextField wtfZeitmodellBezeichnung = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRZeitmodell = null;

	static final public String ACTION_SPECIAL_ZEITMODELL_FROM_LISTE = "action_zeitmodell_from_liste";

	public PanelSchichtzeitmodell(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuZeitmodell;
	}

	void dialogQueryZeitmodellFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRZeitmodell = PersonalFilterFactory.getInstance()
				.createPanelFLRZeitmodell(getInternalFrame(),
						schichtzeitmodellDto.getZeitmodellIId(), false);
		new DialogQuery(panelQueryFLRZeitmodell);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		schichtzeitmodellDto = new SchichtzeitmodellDto();
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
		if (e.getActionCommand().equals(ACTION_SPECIAL_ZEITMODELL_FROM_LISTE)) {
			dialogQueryZeitmodellFromListe(e);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeSchichtzeitmodell(schichtzeitmodellDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		schichtzeitmodellDto.setPersonalIId(internalFramePersonal
				.getPersonalDto().getIId());
	}

	protected void dto2Components() throws Throwable{

		ZeitmodellDto zmDto = DelegateFactory
				.getInstance()
				.getZeiterfassungDelegate()
				.zeitmodellFindByPrimaryKey(
						schichtzeitmodellDto.getZeitmodellIId());

		wtfZeitmodell.setText(zmDto.getCNr());
		if (zmDto.getZeitmodellsprDto() != null) {
			wtfZeitmodellBezeichnung.setText(zmDto.getZeitmodellsprDto()
					.getCBez());
		} else {
			wtfZeitmodellBezeichnung.setText("");
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (schichtzeitmodellDto.getIId() == null) {
				schichtzeitmodellDto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate()
						.createSchichtzeitmodell(schichtzeitmodellDto));
				setKeyWhenDetailPanel(schichtzeitmodellDto.getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updateSchichtzeitmodell(schichtzeitmodellDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFramePersonal.getPersonalDto().getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRZeitmodell) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ZeitmodellDto zeitmodellDto = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.zeitmodellFindByPrimaryKey((Integer) key);
				wtfZeitmodell.setText(zeitmodellDto.getCNr());
				if (zeitmodellDto.getZeitmodellsprDto() != null) {
					wtfZeitmodellBezeichnung.setText(zeitmodellDto
							.getZeitmodellsprDto().getCBez());
				} else {
					wtfZeitmodellBezeichnung.setText("");
				}
				schichtzeitmodellDto.setZeitmodellIId(zeitmodellDto.getIId());
			}
		}
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
		wbuZeitmodell.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zeitmodell")
				+ "...");

		wbuZeitmodell
				.setActionCommand(PanelPersonalzeitmodell.ACTION_SPECIAL_ZEITMODELL_FROM_LISTE);
		wbuZeitmodell.addActionListener(this);

		wtfZeitmodell.setActivatable(false);
		wtfZeitmodell.setMandatoryField(true);
		wtfZeitmodell.setText("");
		wtfZeitmodellBezeichnung.setActivatable(false);
		wtfZeitmodellBezeichnung.setText("");

		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuZeitmodell, new GridBagConstraints(0, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZeitmodell, new GridBagConstraints(1, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZeitmodellBezeichnung, new GridBagConstraints(2, 0,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			schichtzeitmodellDto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.schichtzeitmodellFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}
}
