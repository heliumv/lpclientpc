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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
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
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;

@SuppressWarnings("static-access")
public class PanelMehrwertsteuer extends PanelBasis {

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
	private MwstsatzDto mwstsatzDto = null;
	private WrapperLabel wlaMwst = new WrapperLabel();
	private WrapperNumberField wtfMwst = new WrapperNumberField();
	private WrapperDateField wdfGueltigab = null;
	private WrapperLabel wlaGueltigab = null;
	private WrapperLabel wlaFibucode = new WrapperLabel();
	private WrapperNumberField wtfFibucode = new WrapperNumberField();

	private PanelQueryFLR panelQueryFLRMwstbez = null;
	private WrapperButton wbuMwstbez = null;

	private Date currentDate = null;
	private final String ACTION_SPECIAL_MWSTBEZ = "ACTION_SPECIAL_MWSTBEZ";

	public PanelMehrwertsteuer(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
		currentDate = new Date(System.currentTimeMillis());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		mwstsatzDto = new MwstsatzDto();
	}

	protected void dto2Components() throws Throwable {
		wtfMwst.setDouble(mwstsatzDto.getFMwstsatz());
		wtfBezeichnung.setText(mwstsatzDto.getMwstsatzbezDto()
				.getCBezeichnung());
		wdfGueltigab.setDate(mwstsatzDto.getDGueltigab());
		wtfFibucode.setInteger(mwstsatzDto.getIFibumwstcode());
	}

	protected void components2Dto() throws Throwable {
		mwstsatzDto.setIIMwstsatzbezId(mwstsatzDto.getIIMwstsatzbezId());
		mwstsatzDto.setFMwstsatz(wtfMwst.getDouble());
		mwstsatzDto.setDGueltigab(wdfGueltigab.getTimestamp());
		if (wtfFibucode.getText() != null) {
			mwstsatzDto.setIFibumwstcode(wtfFibucode.getInteger());
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRMwstbez) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					MwstsatzbezDto mwstbezDto = DelegateFactory.getInstance()
							.getMandantDelegate().mwstsatzbezFindByPrimaryKey(
									(Integer) key);
					wtfBezeichnung.setText(mwstbezDto.getCBezeichnung());
					mwstsatzDto.setIIMwstsatzbezId((Integer) key);

				}
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

		wbuMwstbez = new WrapperButton();
		wbuMwstbez.setText(LPMain.getInstance().getTextRespectUISPr("lp.mwst"));
		wbuMwstbez.setActionCommand(this.ACTION_SPECIAL_MWSTBEZ);
		wbuMwstbez.addActionListener(this);
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setColumnsMax(ArtikelFac.MAX_LAGERPLATZ_NAME);
		wtfBezeichnung.setMandatoryField(true);
		wtfBezeichnung.setActivatable(false);
		getInternalFrame().addItemChangedListener(this);

		wlaGueltigab = new WrapperLabel();
		wlaGueltigab.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gueltigab"));
		wdfGueltigab = new WrapperDateField();
		wdfGueltigab.setMandatoryField(true);

		wdfGueltigab.setDate(currentDate);
		wdfGueltigab.setMinimumValue(currentDate);

		wlaFibucode.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.fibucode"));

		wlaMwst.setText(LPMain.getInstance().getTextRespectUISPr("lp.mwst"));
		wtfMwst.setMandatoryField(true);

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
		jpaWorkingOn.add(wbuMwstbez, new GridBagConstraints(0, 0, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMwst, new GridBagConstraints(0, 1, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMwst, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaGueltigab, new GridBagConstraints(0, 2, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfGueltigab, new GridBagConstraints(1, 2, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFibucode, new GridBagConstraints(0, 3, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFibucode, new GridBagConstraints(1, 3, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getMandantDelegate().removeMwstsatz(
				mwstsatzDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MWSTBEZ)) {
			Integer iId = mwstsatzDto.getIIMwstsatzbezId();
			panelQueryFLRMwstbez = SystemFilterFactory
					.getInstance()
					.createPanelFLRMwstbez(getInternalFrame(), true, false, iId);
			new DialogQuery(panelQueryFLRMwstbez);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			mwstsatzDto = DelegateFactory.getInstance().getMandantDelegate()
					.mwstsatzFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, false);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (mwstsatzDto.getIId() == null) {

				mwstsatzDto.setIId(DelegateFactory.getInstance()
						.getMandantDelegate().createMwstsatz(mwstsatzDto));
				setKeyWhenDetailPanel(mwstsatzDto.getIId());
			} else {
				DelegateFactory.getInstance().getMandantDelegate()
						.updateMwstsatz(mwstsatzDto);
			}
			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}
	}
}
