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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitstiftDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

public class PanelZeitstifte extends PanelBasis {

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
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private ZeitstiftDto zeitstifteDto = null;

	private ButtonGroup buttonGroup = new ButtonGroup();
	private WrapperRadioButton wrbPersonenzuordnung = new WrapperRadioButton();
	private WrapperRadioButton wrbMehrpersonenstift = new WrapperRadioButton();
	private WrapperRadioButton wrbEinpersonenstift = new WrapperRadioButton();

	private WrapperComboBox wcoTyp = new WrapperComboBox();

	private WrapperButton wbuPersonal = new WrapperButton();
	private WrapperTextField wtfPersonal = new WrapperTextField();

	static final public String ACTION_SPECIAL_PERSONAL_FROM_LISTE = "action_personal_from_liste";
	private PanelQueryFLR panelQueryFLRPersonal = null;
	private int iLaengeZestiftkennung = 0;

	public PanelZeitstifte(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	protected void setDefaults() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LAENGE_ZESTIFTKENNUNG,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			iLaengeZestiftkennung = ((Integer) parameter.getCWertAsObject())
					.intValue();

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		zeitstifteDto = new ZeitstiftDto();
		leereAlleFelder(this);

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		if (wcoTyp.getKeyOfSelectedItem() != null
				&& wcoTyp.getKeyOfSelectedItem().equals(
						ZeiterfassungFac.ZEITSTIFT_TYP_KDC100)) {
			wrbMehrpersonenstift.setEnabled(false);
			wrbPersonenzuordnung.setEnabled(false);
			wrbEinpersonenstift.setSelected(true);
			wtfKennung.setEditable(false);
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_PERSONAL_FROM_LISTE)) {

			if ((wrbPersonenzuordnung.isSelected() || wrbMehrpersonenstift
					.isSelected())) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("pers.error.zestiftperson"));
				return;
			}

			dialogQueryPersonalFromListe(e);
		} else {

			if (wrbMehrpersonenstift.isSelected()
					|| wrbPersonenzuordnung.isSelected()) {
				wtfPersonal.setMandatoryField(false);

				zeitstifteDto.setPersonalIId(null);
				wtfPersonal.setText("");
			} else {
				wtfPersonal.setMandatoryField(true);

			}

		}

		if (e.getSource().equals(wcoTyp)) {

			LockStateValue v = getLockedstateDetailMainKey();
			if (v != null
					&& (v.getIState() == LOCK_FOR_NEW || v.getIState() == LOCK_IS_LOCKED_BY_ME)) {

				if (wcoTyp.getKeyOfSelectedItem() != null
						&& wcoTyp.getKeyOfSelectedItem().equals(
								ZeiterfassungFac.ZEITSTIFT_TYP_F630)) {

					wrbMehrpersonenstift.setEnabled(true);
					wrbPersonenzuordnung.setEnabled(true);
					wtfKennung.setEditable(true);

				} else {
					wrbMehrpersonenstift.setEnabled(false);
					wrbPersonenzuordnung.setEnabled(false);
					wrbEinpersonenstift.setSelected(true);
					wtfKennung.setEditable(false);

					if (v.getIState() == LOCK_IS_LOCKED_BY_ME) {
						wtfKennung.setText(zeitstifteDto.getCNr());
					}
				}
			}

		}

	}

	void dialogQueryPersonalFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRPersonal = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), false, true,
						zeitstifteDto.getPersonalIId());

		new DialogQuery(panelQueryFLRPersonal);

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (wcoTyp.getKeyOfSelectedItem() != null
				&& wcoTyp.getKeyOfSelectedItem().equals(
						ZeiterfassungFac.ZEITSTIFT_TYP_KDC100)) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.hinweis"), LPMain
					.getTextRespectUISPr("pers.zeitstift.kdc100.delete.error"));

			return;

		} else {
			DelegateFactory.getInstance().getZeiterfassungDelegate()
					.removeZeitstift(zeitstifteDto);
		}
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		zeitstifteDto.setCNr(wtfKennung.getText());
		zeitstifteDto.setCTyp((String) wcoTyp.getKeyOfSelectedItem());

		zeitstifteDto.setBMehrfachstift(Helper.boolean2Short(false));
		zeitstifteDto.setBPersonenzuordnung(Helper.boolean2Short(false));

		if (wrbPersonenzuordnung.isSelected()) {
			zeitstifteDto.setBPersonenzuordnung(Helper.boolean2Short(true));
		} else if (wrbEinpersonenstift.isSelected()) {

		} else if (wrbMehrpersonenstift.isSelected()) {
			zeitstifteDto.setBMehrfachstift(Helper.boolean2Short(true));
		}
	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(zeitstifteDto.getCNr().trim());

		wcoTyp.setKeyOfSelectedItem(zeitstifteDto.getCTyp());

		if (Helper.short2Boolean(zeitstifteDto.getBMehrfachstift()) == true) {
			wrbMehrpersonenstift.setSelected(true);
		} else if (Helper.short2Boolean(zeitstifteDto.getBPersonenzuordnung()) == true) {
			wrbPersonenzuordnung.setSelected(true);
		} else {
			wrbEinpersonenstift.setSelected(true);
		}

		if (zeitstifteDto.getPersonalIId() != null) {
			PersonalDto personalDto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(zeitstifteDto.getPersonalIId());
			wtfPersonal.setText(personalDto.formatAnrede());
		} else {
			wtfPersonal.setText(null);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			if (wcoTyp.getKeyOfSelectedItem().equals(
					ZeiterfassungFac.ZEITSTIFT_TYP_KDC100)
					|| wtfKennung.getText().length() == iLaengeZestiftkennung) {
				components2Dto();

				if (zeitstifteDto.getIId() == null) {

					if (zeitstifteDto.getCTyp().equals(
							ZeiterfassungFac.ZEITSTIFT_TYP_KDC100)) {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("pers.zeitstift.kdc100.error"));

						return;
					}

					zeitstifteDto.setIId(DelegateFactory.getInstance()
							.getZeiterfassungDelegate()
							.createZeitstift(zeitstifteDto));
					setKeyWhenDetailPanel(zeitstifteDto.getIId());
				} else {
					DelegateFactory.getInstance().getZeiterfassungDelegate()
							.updateZeitstift(zeitstifteDto);
				}
				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							zeitstifteDto.getIId() + "");
				}
				eventYouAreSelected(false);
			} else {
				Object[] arguments = { iLaengeZestiftkennung + "" };

				String result = MessageFormat.format(
						LPMain.getTextRespectUISPr("pers.error.zestiftlaenge"),
						arguments);

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"), result);
			}

		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPersonal) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);

					if (personalDto.getCAusweis() == null
							|| (personalDto.getCAusweis().length() != 3 && (wcoTyp
									.getKeyOfSelectedItem()
									.equals(ZeiterfassungFac.ZEITSTIFT_TYP_F630)))) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("pers.error.ausweiszestift"));

					} else {
						wtfPersonal.setText(personalDto.formatAnrede());
						zeitstifteDto.setPersonalIId((Integer) key);
					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPersonal) {
				wtfPersonal.setText(null);
				zeitstifteDto.setPersonalIId(null);
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

		wlaKennung.setText(LPMain.getTextRespectUISPr("label.kennung"));
		wtfKennung.setColumnsMax(ZeiterfassungFac.MAX_TAETIGKEIT_KENNUNG);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);
		wtfPersonal.setMandatoryField(true);
		wtfPersonal.setActivatable(false);

		wcoTyp.addActionListener(this);

		wbuPersonal.setText(LPMain.getTextRespectUISPr("lp.personal"));
		wbuPersonal.setActionCommand(ACTION_SPECIAL_PERSONAL_FROM_LISTE);
		wbuPersonal.addActionListener(this);
		wtfKennung.setColumnsMax(15);
		wtfPersonal.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wcoTyp.setMandatoryField(true);

		getInternalFrame().addItemChangedListener(this);

		Map<String, String> m = new TreeMap<String, String>();
		m.put(ZeiterfassungFac.ZEITSTIFT_TYP_F630,
				ZeiterfassungFac.ZEITSTIFT_TYP_F630);
		m.put(ZeiterfassungFac.ZEITSTIFT_TYP_KDC100,
				ZeiterfassungFac.ZEITSTIFT_TYP_KDC100);

		wcoTyp.setMap(m);

		buttonGroup.add(wrbEinpersonenstift);
		buttonGroup.add(wrbMehrpersonenstift);
		buttonGroup.add(wrbPersonenzuordnung);
		wrbMehrpersonenstift.setSelected(true);

		wrbEinpersonenstift.addActionListener(this);
		wrbMehrpersonenstift.addActionListener(this);
		wrbPersonenzuordnung.addActionListener(this);

		wrbEinpersonenstift.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.einfachstift"));
		wrbMehrpersonenstift.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.mehrfachstift"));
		wrbPersonenzuordnung.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.personenzuordnung"));

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

		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbPersonenzuordnung, new GridBagConstraints(1,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wrbMehrpersonenstift, new GridBagConstraints(1,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbEinpersonenstift, new GridBagConstraints(1, iZeile,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(1, iZeile, 2, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuPersonal, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("lp.typ")),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wcoTyp, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZEITSTIFT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
			if (key != null) {
				eventActionSpecial(new ActionEvent(this, 0, ""));
			}
		} else {
			zeitstifteDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.zeitstiftFindByPrimaryKey((Integer) key);
			dto2Components();
			eventActionSpecial(new ActionEvent(this, 0, ""));

		}
	}

}
