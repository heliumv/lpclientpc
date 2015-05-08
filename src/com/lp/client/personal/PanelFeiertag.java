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
package com.lp.client.personal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
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
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.FeiertagDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ReligionDto;

@SuppressWarnings("static-access")
public class PanelFeiertag extends PanelBasis {

	private WrapperRadioButton wrbDatum = new WrapperRadioButton();
	private WrapperRadioButton wrbOstern = new WrapperRadioButton();
	private ButtonGroup buttonGroup1 = new ButtonGroup();

	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperComboBox wcoMonat = new WrapperComboBox();

	private WrapperNumberField wnfOffsetOstern = new WrapperNumberField();
	private WrapperNumberField wnfTag = new WrapperNumberField();
	private FeiertagDto feiertagDto = null;
	private WrapperLabel wlaTagesart = new WrapperLabel();
	private WrapperButton wbuReligion = new WrapperButton();
	private WrapperComboBox wcbTagesart = new WrapperComboBox();
	private WrapperTextField wtfReligion = new WrapperTextField();

	static final public String ACTION_SPECIAL_RELIGION_FROM_LISTE = "action_religion_from_liste";

	private PanelQueryFLR panelQueryFLRReligion = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	public PanelFeiertag(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	protected void setDefaults() throws Throwable {
		wcbTagesart.setMap(DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.getAllSprTagesartenOhneMontagBisSonntag());
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		feiertagDto = new FeiertagDto();
		
		leereAlleFelder(this);
		wrbDatum.setSelected(true);
		eventActionSpecial(new ActionEvent(wrbDatum, 0, ""));
	
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_RELIGION_FROM_LISTE)) {
			dialogQueryRelgionFromListe(e);
		}

		if (e.getSource().equals(wrbDatum) || e.getSource().equals(wrbOstern)) {

			LockStateValue lockStateValue = super.getLockedstateDetailMainKey();
			if (wrbDatum.isSelected()) {
				wcoMonat.setMandatoryField(true);
				wcoMonat.setActivatable(true);
				wnfTag.setActivatable(true);
				wnfTag.setMandatoryField(true);

				wnfOffsetOstern.setMandatoryField(false);
				wnfOffsetOstern.setActivatable(false);

				if (lockStateValue.getIState() == LOCK_IS_LOCKED_BY_ME
						|| lockStateValue.getIState() == LOCK_FOR_NEW) {
					wcoMonat.setEnabled(true);
					wnfTag.setEditable(true);
				}

			} else {
				wcoMonat.setMandatoryField(false);
				wcoMonat.setActivatable(false);
				wnfTag.setMandatoryField(false);
				wnfTag.setActivatable(false);

				wnfOffsetOstern.setMandatoryField(true);
				wnfOffsetOstern.setActivatable(true);

				if (lockStateValue.getIState() == LOCK_IS_LOCKED_BY_ME
						|| lockStateValue.getIState() == LOCK_FOR_NEW) {
					wnfOffsetOstern.setEditable(true);
				}

			}
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeFeiertag(feiertagDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	void dialogQueryRelgionFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRReligion = PersonalFilterFactory.getInstance()
				.createPanelFLRReligion(getInternalFrame(),
						feiertagDto.getReligionIId());
		new DialogQuery(panelQueryFLRReligion);
	}

	protected void components2Dto() throws Throwable {
		if (wrbOstern.isSelected()) {
			feiertagDto.setIOffsetOstersonntag(wnfOffsetOstern.getInteger());
			feiertagDto.setiMonat(null);
			feiertagDto.setiTag(null);
		} else {
			feiertagDto.setiMonat((Integer) wcoMonat.getKeyOfSelectedItem());
			feiertagDto.setiTag(wnfTag.getInteger());
			feiertagDto.setIOffsetOstersonntag(null);
		}

		feiertagDto
				.setTagesartIId((Integer) wcbTagesart.getKeyOfSelectedItem());
		feiertagDto.setCBez(wtfBezeichnung.getText());
	}

	protected void dto2Components() throws Throwable {

		wnfTag.setInteger(feiertagDto.getiTag());
		wcoMonat.setKeyOfSelectedItem(feiertagDto.getiMonat());
		wcbTagesart.setKeyOfSelectedItem(feiertagDto.getTagesartIId());
		wtfBezeichnung.setText(feiertagDto.getCBez());
		wnfOffsetOstern.setInteger(feiertagDto.getIOffsetOstersonntag());
		
		
		if(feiertagDto.getIOffsetOstersonntag()==null){
			wrbDatum.setSelected(true);
			eventActionSpecial(new ActionEvent(wrbDatum, 0, ""));
		} else {
			wrbOstern.setSelected(true);
			eventActionSpecial(new ActionEvent(wrbOstern, 0, ""));
		}
		if (feiertagDto.getReligionIId() != null) {
			wtfReligion.setText(DelegateFactory.getInstance()
					.getPersonalDelegate()
					.religionFindByPrimaryKey(feiertagDto.getReligionIId())
					.getCNr());
		} else {
			wtfReligion.setText("");
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (feiertagDto.getIId() == null) {
				feiertagDto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate().createFeiertag(feiertagDto));
				setKeyWhenDetailPanel(feiertagDto.getIId());

			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updateFeiertag(feiertagDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame()
						.setKeyWasForLockMe(feiertagDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRReligion) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ReligionDto religionDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.religionFindByPrimaryKey((Integer) key);
				wtfReligion.setText(religionDto.getCNr());
				feiertagDto.setReligionIId(religionDto.getIId());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRReligion) {
				wtfReligion.setText(null);
				feiertagDto.setReligionIId(null);
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

		buttonGroup1.add(wrbDatum);
		buttonGroup1.add(wrbOstern);

		wrbDatum.addActionListener(this);
		wrbOstern.addActionListener(this);
		wtfBezeichnung.setMandatoryField(true);
		wcbTagesart.setMandatoryField(true);

		getInternalFrame().addItemChangedListener(this);

		wrbDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.feiertag.tagmonat"));
		wrbOstern.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.feiertag.offset_ostern"));

		wlaTagesart.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.tagesart"));
		wbuReligion.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.religion")
				+ "...");

		wcoMonat.setMandatoryField(true);
		wnfTag.setMandatoryField(true);

		DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getInstance()
				.getUISprLocale());
		String[] defaultMonths = symbols.getMonths();
		Map<Integer, String> m = new TreeMap<Integer, String>();
		for (int i = 0; i < defaultMonths.length - 1; i++) {
			m.put(new Integer(i), defaultMonths[i]);

		}
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		cal.set(Calendar.MONTH, month - 1);
		month = cal.get(Calendar.MONTH);
		wcoMonat.setMap(m);

		wnfOffsetOstern.setFractionDigits(0);
		wnfTag.setFractionDigits(0);
		wnfTag.setMinimumValue(1);
		wnfTag.setMaximumValue(31);

		wbuReligion
				.setActionCommand(PanelBetriebskalender.ACTION_SPECIAL_RELIGION_FROM_LISTE);
		wbuReligion.addActionListener(this);

		wtfReligion.setActivatable(false);
		wtfReligion.setText("");
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung
				.setColumnsMax(PersonalFac.MAX_BETRIEBSKALENDER_BEZEICHNUNG);
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

		jpaWorkingOn.add(wrbDatum, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfTag, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), -80, 0));
		jpaWorkingOn.add(wcoMonat, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 45, 2, 2), 80, 0));

		iZeile++;

		jpaWorkingOn.add(wrbOstern, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfOffsetOstern, new GridBagConstraints(1, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbTagesart, new GridBagConstraints(1, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 150, 0));

		iZeile++;

		jpaWorkingOn.add(wbuReligion, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfReligion, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_FEIERTAG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			feiertagDto = DelegateFactory.getInstance().getPersonalDelegate()
					.feiertagFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
