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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;

public class PanelEinkaufsangebotpositionenKommentare extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EinkaufsangebotpositionDto einkaufsangebotpositionDto = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaKommentar1 = new WrapperLabel();
	private WrapperLabel wlaKommentar2 = new WrapperLabel();
	private WrapperEditorField wefKommentar1 = null;
	private WrapperEditorField wefKommentar2 = null;

	public PanelEinkaufsangebotpositionenKommentare(
			InternalFrame internalFrame, String add2TitleI, Object pk)
			throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINKAUFSANGEBOT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);

		if (((InternalFrameAngebotstkl) getInternalFrame())
				.getTabbedPaneEinkaufsangebot()
				.getPanelQueryEinkaufsangebotpositionen() != null) {

			Integer positionIId = (Integer) ((InternalFrameAngebotstkl) getInternalFrame())
					.getTabbedPaneEinkaufsangebot()
					.getPanelQueryEinkaufsangebotpositionen().getSelectedId();
			if (positionIId != null) {
				einkaufsangebotpositionDto = DelegateFactory.getInstance()
						.getAngebotstklDelegate()
						.einkaufsangebotpositionFindByPrimaryKey(positionIId);
				dto2Components();
			} else {
				einkaufsangebotpositionDto = null;
				leereAlleFelder(this);

				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("agstkl.ekangebotposkommentar.error"));

				((InternalFrameAngebotstkl) getInternalFrame())
						.getTabbedPaneEinkaufsangebot().setSelectedIndex(
								TabbedPaneEinkaufsangebot.IDX_PANEL_POSITIONEN);
			}
		} else {

			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("agstkl.ekangebotposkommentar.error"));

			((InternalFrameAngebotstkl) getInternalFrame())
					.getTabbedPaneEinkaufsangebot().setSelectedIndex(
							TabbedPaneEinkaufsangebot.IDX_PANEL_POSITIONEN);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wefKommentar1;
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaKommentar1.setText(LPMain
				.getTextRespectUISPr("agstkl.ekangebotposkommentar1"));
		wlaKommentar2.setText(LPMain
				.getTextRespectUISPr("agstkl.ekangebotposkommentar2"));

		wefKommentar1 = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.kommentar"));
		wefKommentar2 = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.kommentar"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKommentar1, new GridBagConstraints(0, iZeile, 1, 1,
				0.25, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wefKommentar1, new GridBagConstraints(1, iZeile, 1, 1,
				0.8, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaKommentar2, new GridBagConstraints(0, iZeile, 1, 1,
				0.25, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentar2, new GridBagConstraints(1, iZeile, 1, 1,
				0.8, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };
		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected void setDefaults() {
	}

	protected void dto2Components() throws Throwable {
		wefKommentar1.setText(einkaufsangebotpositionDto.getCKommentar1());
		wefKommentar2.setText(einkaufsangebotpositionDto.getCKommentar2());

	}

	protected void components2Dto() throws Throwable {
		einkaufsangebotpositionDto.setCKommentar1(wefKommentar1.getText());
		einkaufsangebotpositionDto.setCKommentar2(wefKommentar2.getText());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getAngebotstklDelegate()
					.updateEinkaufsangebotposition(einkaufsangebotpositionDto);
		}
		super.eventActionSave(e, true);
	}
}
