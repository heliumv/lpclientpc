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
package com.lp.client.stueckliste;

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
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftartDto;

@SuppressWarnings("static-access")
public class PanelStuecklisteeigenschaft extends PanelBasis {

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
	private InternalFrameStueckliste internalFrameStueckliste = null;
	private WrapperButton wbuEigenschaftart = new WrapperButton();
	private WrapperTextField wtfEigenschaftart = new WrapperTextField();
	private StuecklisteeigenschaftDto stuecklisteeigenschaftDto = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	static final public String ACTION_SPECIAL_EIGENSCHAFTART_FROM_LISTE = "action_eigenschaftart_from_liste";
	private PanelQueryFLR panelQueryFLREigenschaftart = null;

	public PanelStuecklisteeigenschaft(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameStueckliste = (InternalFrameStueckliste) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	private void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		stuecklisteeigenschaftDto = new StuecklisteeigenschaftDto();
		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_EIGENSCHAFTART_FROM_LISTE)) {
			dialogQueryEigenschaftartFromListe(e);
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getStuecklisteDelegate()
				.removeStuecklisteeigenschaft(stuecklisteeigenschaftDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		stuecklisteeigenschaftDto.setStuecklisteIId(internalFrameStueckliste
				.getStuecklisteDto().getIId());
		stuecklisteeigenschaftDto.setCBez(wtfBezeichnung.getText());
	}

	protected void dto2Components() throws Throwable {
		wtfBezeichnung.setText(stuecklisteeigenschaftDto.getCBez());
		StuecklisteeigenschaftartDto dto = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.stuecklisteeigenschaftartFindByPrimaryKey(
						stuecklisteeigenschaftDto
								.getStuecklisteeigenschaftartIId());
		wtfEigenschaftart.setText(dto.getCBez());

		this.setStatusbarPersonalIIdAendern(stuecklisteeigenschaftDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(stuecklisteeigenschaftDto.getTAendern());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (stuecklisteeigenschaftDto.getIId() == null) {
				components2Dto();
				stuecklisteeigenschaftDto
						.setIId(DelegateFactory
								.getInstance()
								.getStuecklisteDelegate()
								.createStuecklisteeigenschaft(
										stuecklisteeigenschaftDto));
				setKeyWhenDetailPanel(stuecklisteeigenschaftDto.getIId());
			} else {
				DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.updateStuecklisteeigenschaft(stuecklisteeigenschaftDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameStueckliste.getStuecklisteDto().getIId()
								+ "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLREigenschaftart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				StuecklisteeigenschaftartDto eigenschaftartTempDto = DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteeigenschaftartFindByPrimaryKey(
								(Integer) key);
				wtfEigenschaftart.setText(eigenschaftartTempDto.getCBez());
				stuecklisteeigenschaftDto
						.setStuecklisteeigenschaftartIId(eigenschaftartTempDto
								.getIId());
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

		wbuEigenschaftart.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.eigenschaftart")
				+ "...");
		wbuEigenschaftart
				.setActionCommand(this.ACTION_SPECIAL_EIGENSCHAFTART_FROM_LISTE);
		wbuEigenschaftart.addActionListener(this);

		wtfBezeichnung.setMandatoryField(true);

		wtfEigenschaftart.setMandatoryField(true);
		wtfEigenschaftart.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setText("");
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
		jpaWorkingOn.add(wbuEigenschaftart, new GridBagConstraints(0, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfEigenschaftart, new GridBagConstraints(1, 0, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 1, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	void dialogQueryEigenschaftartFromListe(ActionEvent e) throws Throwable {
		panelQueryFLREigenschaftart = StuecklisteFilterFactory.getInstance()
				.createPanelFLREigenschaftart(
						getInternalFrame(),
						stuecklisteeigenschaftDto
								.getStuecklisteeigenschaftartIId());
		new DialogQuery(panelQueryFLREigenschaftart);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STUECKLISTE;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			stuecklisteeigenschaftDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.stuecklisteeigenschaftFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
