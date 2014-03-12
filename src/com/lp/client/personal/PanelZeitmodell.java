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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitmodellDto;
import com.lp.server.personal.service.ZeitmodellsprDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelZeitmodell extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ZeitmodellDto zeitmodellDto = null;
	private InternalFramePersonal internalFramePersonal = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	static final public String ACTION_SPECIAL_ORT_FROM_LISTE = "action_ort_from_liste";
	static final public String ACTION_SPECIAL_KOSTENSTELLE_ABTEILUNG_FROM_LISTE = "action_kostenstelle_from_liste";
	static final public String ACTION_SPECIAL_HEIMATKOSTENSTELLE_FROM_LISTE = "action_heimatkostenstelle_from_liste";
	static final public String ACTION_SPECIAL_PARTNER_FROM_LISTE = "action_partner_from_liste";
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperCheckBox wcbTeilzeitmodell = new WrapperCheckBox();
	private WrapperCheckBox wcbDynamisch = new WrapperCheckBox();
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
	private WrapperLabel wrapperLabel1 = new WrapperLabel();
	private WrapperLabel wlaWochensumme = new WrapperLabel();
	private WrapperLabel wlaUrlaubstageProWoche = new WrapperLabel();
	private WrapperNumberField wnfUrlaubstageProWoche = new WrapperNumberField();
	private WrapperLabel wlaWochensummeSoFtg = new WrapperLabel();
	private WrapperNumberField wnfWochensumme = new WrapperNumberField();
	private WrapperNumberField wnfWochensummeSoFtg = new WrapperNumberField();

	private WrapperLabel wlaMinutenabzug = new WrapperLabel();
	private WrapperNumberField wnfMinutenabzug = new WrapperNumberField();

	private WrapperLabel wlaWochensummeMoBisSo = new WrapperLabel();
	private WrapperNumberField wnfWochensummeMoBisSo = new WrapperNumberField();

	private WrapperLabel wlaSollstundenFix = new WrapperLabel();
	private WrapperNumberField wnfSollstundenFix = new WrapperNumberField();
	private WrapperLabel wlaSollstundenFixHinweis = new WrapperLabel();

	public InternalFramePersonal getInternalFramePersonal() {
		return internalFramePersonal;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	public PanelZeitmodell(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
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
					.getZeiterfassungDelegate()
					.zeitmodellFindByPrimaryKey(
							getInternalFramePersonal().getZeitmodellDto()
									.getIId());

			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFramePersonal().getZeitmodellDto().getCNr());
		}
	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(zeitmodellDto.getCNr());
		if (zeitmodellDto.getZeitmodellsprDto() != null) {
			wtfBezeichnung.setText(zeitmodellDto.getZeitmodellsprDto()
					.getCBez());
		} else {
			wtfBezeichnung.setText("");
		}
		wcbTeilzeitmodell.setShort(zeitmodellDto.getBTeilzeit());
		wcbVersteckt.setShort(zeitmodellDto.getBVersteckt());
		wnfSollstundenFix.setBigDecimal(zeitmodellDto.getNSollstundenfix());

		wcbDynamisch.setShort(zeitmodellDto.getBDynamisch());

		wnfUrlaubstageProWoche.setDouble(zeitmodellDto
				.getFUrlaubstageprowoche());
		wnfMinutenabzug.setInteger(zeitmodellDto.getIMinutenabzug());

		this.setStatusbarPersonalIIdAendern(zeitmodellDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(zeitmodellDto.getTAendern());
		wnfWochensumme.setDouble(DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.getSummeSollzeitWochentags(zeitmodellDto.getIId()));
		wnfWochensummeSoFtg.setDouble(DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.getSummeSollzeitSonnUndFeiertags(zeitmodellDto.getIId()));
		wnfWochensummeMoBisSo.setDouble(DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.getSummeSollzeitMontagBisSonntag(zeitmodellDto.getIId()));
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
				"label.kennung"));
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfKennung.setMandatoryField(true);

		wlaUrlaubstageProWoche.setText(LPMain.getInstance()
				.getTextRespectUISPr("pers.zeitmodell.urlaubstageprowoche"));

		wnfUrlaubstageProWoche.setMandatoryField(true);
		wnfUrlaubstageProWoche.setFractionDigits(2);

		wlaMinutenabzug.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.minutenabzug"));

		wnfMinutenabzug.setMandatoryField(true);
		wnfMinutenabzug.setFractionDigits(0);

		wtfKennung.setText("");
		wtfKennung.setColumnsMax(ZeiterfassungFac.MAX_ZEITMODELL_KENNUNG);
		wtfBezeichnung.setText("");
		wtfBezeichnung
				.setColumnsMax(ZeiterfassungFac.MAX_ZEITMODELL_BEZEICHNUNG);
		wcbTeilzeitmodell.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.teilzeitmodell"));
		wcbDynamisch.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.dynamischepause"));

		wcbVersteckt.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.versteckt"));

		wlaWochensumme.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.wochensumme"));
		wrapperLabel1.setText("");
		wlaWochensummeSoFtg.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.wochensummesoftg"));
		wlaWochensummeMoBisSo.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.wochensummemoso"));
		wnfWochensumme.setActivatable(false);
		wnfWochensummeSoFtg.setActivatable(false);
		wnfWochensummeMoBisSo.setActivatable(false);

		wlaSollstundenFix.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.sollstundenfix"));
		wlaSollstundenFixHinweis.setText(LPMain.getInstance()
				.getTextRespectUISPr("pers.zeitmodell.sollstundenfixhinweis"));
		wlaSollstundenFixHinweis.setForeground(Color.RED);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, 0, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, 0, 1, 1, 0.2,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 1, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbTeilzeitmodell, new GridBagConstraints(1, 2, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbDynamisch, new GridBagConstraints(2, 2, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 200, 0));

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {

			jpaWorkingOn.add(wcbVersteckt,
					new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		jpaWorkingOn.add(wrapperLabel1, new GridBagConstraints(0, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaWochensumme, new GridBagConstraints(0, 7, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWochensummeSoFtg, new GridBagConstraints(0, 8, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWochensummeMoBisSo, new GridBagConstraints(0, 9, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaUrlaubstageProWoche, new GridBagConstraints(0, 10,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMinutenabzug, new GridBagConstraints(0, 11, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfWochensumme, new GridBagConstraints(1, 7, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWochensummeSoFtg, new GridBagConstraints(1, 8, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWochensummeMoBisSo, new GridBagConstraints(1, 9, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfUrlaubstageProWoche, new GridBagConstraints(1, 10,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMinutenabzug, new GridBagConstraints(1, 11, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		wlaSollstundenFixHinweis.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaSollstundenFixHinweis, new GridBagConstraints(0,
				12, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(30, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSollstundenFix, new GridBagConstraints(0, 13, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSollstundenFix, new GridBagConstraints(1, 13, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

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
		zeitmodellDto = new ZeitmodellDto();
		zeitmodellDto.setZeitmodellsprDto(new ZeitmodellsprDto());
		leereAlleFelder(this);
		wnfMinutenabzug.setInteger(0);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZEITMODELL;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {
		zeitmodellDto.setCNr(wtfKennung.getText());
		zeitmodellDto.setBTeilzeit(wcbTeilzeitmodell.getShort());
		zeitmodellDto.setBDynamisch(wcbDynamisch.getShort());
		zeitmodellDto.setFUrlaubstageprowoche(wnfUrlaubstageProWoche
				.getDouble());
		zeitmodellDto.setNSollstundenfix(wnfSollstundenFix.getBigDecimal());
		zeitmodellDto.setIMinutenabzug(wnfMinutenabzug.getInteger());
		if (zeitmodellDto.getIId() != null
				&& zeitmodellDto.getBVersteckt() != null
				&& !Helper.short2boolean(zeitmodellDto.getBVersteckt())) {

			if (Helper.short2boolean(wcbVersteckt.getShort())) {
				PersonalDto[] dtos = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.getPersonenDieZeitmodellVerwenden(
								zeitmodellDto.getIId());

				if (dtos != null && dtos.length > 0) {
					String s = LPMain.getInstance().getTextRespectUISPr(
							"zeiterfassung.personenverwendenzeitmodell");

					for (int i = 0; i < dtos.length; i++) {
						s += dtos[i].formatAnrede() + "\n";

					}
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.warning"), s);

				}

			}
		}

		zeitmodellDto.setBVersteckt(wcbVersteckt.getShort());
		if (wtfBezeichnung.getText() != null) {
			ZeitmodellsprDto sprDto = new ZeitmodellsprDto();
			sprDto.setCBez(wtfBezeichnung.getText());
			zeitmodellDto.setZeitmodellsprDto(sprDto);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getZeiterfassungDelegate()
				.removeZeitmodell(zeitmodellDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (zeitmodellDto.getIId() == null) {
				zeitmodellDto.setIId(DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.createZeitmodell(zeitmodellDto));
				setKeyWhenDetailPanel(zeitmodellDto.getIId());
				internalFramePersonal.setZeitmodellDto(zeitmodellDto);
			} else {
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.updateZeitmodell(zeitmodellDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						zeitmodellDto.getIId().toString());
			}
			eventYouAreSelected(false);
			zeitmodellDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.zeitmodellFindByPrimaryKey(zeitmodellDto.getIId());
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		eI = (ItemChangedEvent) eI;
	}
}
