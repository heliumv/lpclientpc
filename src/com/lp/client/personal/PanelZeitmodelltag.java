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
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
public class PanelZeitmodelltag extends PanelBasis {

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

	private ZeitmodelltagDto zeitmodelltagDto = null;

	private WrapperLabel wlaTagesart = new WrapperLabel();
	private WrapperComboBox wcoTagesart = new WrapperComboBox();
	private WrapperLabel wlaSollzeit = new WrapperLabel();
	private WrapperTimeField wtfSollzeit = new WrapperTimeField();
	private WrapperLabel wlaKommt = new WrapperLabel();
	private WrapperLabel wlaErlaubteAnwesenheit = new WrapperLabel();
	private WrapperTimeField wtfErlaubteAnwesenheit = new WrapperTimeField();
	private WrapperLabel wlaGeht = new WrapperLabel();
	private WrapperTimeField wtfGeht = new WrapperTimeField();
	private WrapperTimeField wtfKommt = new WrapperTimeField();
	private WrapperLabel wlaMindestpausenzeit = new WrapperLabel();
	private WrapperTimeField wtfMindestpausenzeit = new WrapperTimeField();
	private WrapperLabel wlaAutopauseab = new WrapperLabel();
	private WrapperTimeField wtfAutopauseab = new WrapperTimeField();

	private WrapperLabel wlaMindestpausenzeit2 = new WrapperLabel();
	private WrapperTimeField wtfMindestpausenzeit2 = new WrapperTimeField();
	private WrapperLabel wlaAutopauseab2 = new WrapperLabel();
	private WrapperTimeField wtfAutopauseab2 = new WrapperTimeField();

	private WrapperLabel wlaMindestpausenzeit3 = new WrapperLabel();
	private WrapperTimeField wtfMindestpausenzeit3 = new WrapperTimeField();
	private WrapperLabel wlaAutopauseab3 = new WrapperLabel();
	private WrapperTimeField wtfAutopauseab3 = new WrapperTimeField();

	private WrapperLabel wlaWochensumme = new WrapperLabel();
	private WrapperLabel wlaWochensummeSoFtg = new WrapperLabel();
	private WrapperNumberField wnfWochensumme = new WrapperNumberField();
	private WrapperNumberField wnfWochensummeSoFtg = new WrapperNumberField();

	private WrapperLabel wlaRundungBeginn = new WrapperLabel();
	private WrapperLabel wlaRundungEnde = new WrapperLabel();

	private WrapperCheckBox wcbRundesondertatigkeiten = new WrapperCheckBox();

	private WrapperSpinner wspRundungEnde = new WrapperSpinner(0, 0, 60, 5);
	private WrapperSpinner wspRundungBeginn = new WrapperSpinner(0, 0, 60, 5);

	public PanelZeitmodelltag(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		wcoTagesart.setMap(DelegateFactory.getInstance()
				.getZeiterfassungDelegate().getAllSprTagesarten());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoTagesart;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		zeitmodelltagDto = new ZeitmodelltagDto();

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
		DelegateFactory.getInstance().getZeiterfassungDelegate()
				.removeZeitmodelltag(zeitmodelltagDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		zeitmodelltagDto.setZeitmodellIId(internalFramePersonal
				.getZeitmodellDto().getIId());
		zeitmodelltagDto.setZeitmodellIId(internalFramePersonal
				.getZeitmodellDto().getIId());
		zeitmodelltagDto.setTagesartIId((Integer) wcoTagesart
				.getKeyOfSelectedItem());
		zeitmodelltagDto.setUBeginn(wtfKommt.getTime());
		zeitmodelltagDto.setUEnde(wtfGeht.getTime());
		zeitmodelltagDto.setUMindestpause(wtfMindestpausenzeit.getTime());
		zeitmodelltagDto.setUAutopauseab(wtfAutopauseab.getTime());
		zeitmodelltagDto.setUMindestpause2(wtfMindestpausenzeit2.getTime());
		zeitmodelltagDto.setUAutopauseab2(wtfAutopauseab2.getTime());
		zeitmodelltagDto.setUMindestpause3(wtfMindestpausenzeit3.getTime());
		zeitmodelltagDto.setUAutopauseab3(wtfAutopauseab3.getTime());
		zeitmodelltagDto.setUSollzeit(wtfSollzeit.getTime());
		zeitmodelltagDto.setUErlaubteanwesenheitszeit(wtfErlaubteAnwesenheit
				.getTime());
		zeitmodelltagDto.setIRundungbeginn(wspRundungBeginn.getInteger());
		zeitmodelltagDto.setIRundungende(wspRundungEnde.getInteger());

		zeitmodelltagDto.setBRundesondertaetigkeiten(wcbRundesondertatigkeiten
				.getShort());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wtfAutopauseab.setTime(zeitmodelltagDto.getUAutopauseab());
		wtfAutopauseab2.setTime(zeitmodelltagDto.getUAutopauseab2());
		wtfAutopauseab3.setTime(zeitmodelltagDto.getUAutopauseab3());
		wtfGeht.setTime(zeitmodelltagDto.getUEnde());
		wtfKommt.setTime(zeitmodelltagDto.getUBeginn());
		wtfMindestpausenzeit.setTime(zeitmodelltagDto.getUMindestpause());
		wtfMindestpausenzeit2.setTime(zeitmodelltagDto.getUMindestpause2());
		wtfMindestpausenzeit3.setTime(zeitmodelltagDto.getUMindestpause3());
		wtfSollzeit.setTime(zeitmodelltagDto.getUSollzeit());
		wtfErlaubteAnwesenheit.setTime(zeitmodelltagDto
				.getUErlaubteanwesenheitszeit());

		wcoTagesart.setKeyOfSelectedItem(zeitmodelltagDto.getTagesartIId());

		wspRundungBeginn.setInteger(zeitmodelltagDto.getIRundungbeginn());
		wspRundungEnde.setInteger(zeitmodelltagDto.getIRundungende());
		wcbRundesondertatigkeiten.setShort(zeitmodelltagDto
				.getBRundesondertaetigkeiten());

		this.setStatusbarPersonalIIdAendern(zeitmodelltagDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(zeitmodelltagDto.getTAendern());
		wnfWochensumme
				.setDouble(DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.getSummeSollzeitWochentags(
								zeitmodelltagDto.getZeitmodellIId()));
		wnfWochensummeSoFtg.setDouble(DelegateFactory
				.getInstance()
				.getZeiterfassungDelegate()
				.getSummeSollzeitSonnUndFeiertags(
						zeitmodelltagDto.getZeitmodellIId()));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			// PJ15577
			if (zeitmodelltagDto.getUMindestpause().getTime() > -3600000
					&& zeitmodelltagDto.getUAutopauseab().getTime() == -3600000) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.info"), LPMain.getInstance()
						.getTextRespectUISPr("pers.error.mindestpausendauer"));
				return;

			}

			if (zeitmodelltagDto.getIId() == null) {

				zeitmodelltagDto.setIId(DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.createZeitmodelltag(zeitmodelltagDto));
				setKeyWhenDetailPanel(zeitmodelltagDto.getIId());
			} else {
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.updateZeitmodelltag(zeitmodelltagDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFramePersonal.getZeitmodellDto().getIId() + "");
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

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_NUR_WARNUNG,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		boolean bPausenNurWarnung = (Boolean) parameter.getCWertAsObject();

		getInternalFrame().addItemChangedListener(this);
		wlaTagesart.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.tagesart"));
		wcoTagesart.setMandatoryField(true);
		wlaSollzeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.sollzeit"));
		wlaKommt.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.kommt"));
		wlaErlaubteAnwesenheit.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"pers.zeitmodelltag.erlaubteanwesenheitszeit"));
		wlaGeht.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.geht"));
		wlaMindestpausenzeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.mindestpausenzeit"));

		if (bPausenNurWarnung) {
			wlaAutopauseab.setText(LPMain.getInstance().getTextRespectUISPr(
					"pers.zeitmodelltag.autopauseabjeweils"));
		} else {
			wlaAutopauseab.setText(LPMain.getInstance().getTextRespectUISPr(
					"pers.zeitmodelltag.autopauseab"));
		}

		wlaMindestpausenzeit2.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.mindestpausenzeit2"));
		wlaAutopauseab2.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.autopauseab2"));
		wlaMindestpausenzeit3.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.mindestpausenzeit3"));
		wlaAutopauseab3.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.autopauseab3"));

		wlaWochensumme.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.wochensumme"));
		wlaWochensummeSoFtg.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodell.wochensummesoftg"));
		wnfWochensumme.setActivatable(false);
		wnfWochensummeSoFtg.setActivatable(false);

		wlaRundungBeginn.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.rundungbeginn"));
		wlaRundungEnde.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zeitmodelltag.rundungende"));

		wcbRundesondertatigkeiten.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"pers.zeitmodelltag.rundesondertaetigkeiten"));

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
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		int iZeile = 0;

		jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(0, iZeile, 1, 1,
				0.30, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(1, iZeile, 5, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wlaWochensumme, new GridBagConstraints(3, iZeile, 1,
				1, 0.25, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWochensumme, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), -80, 0));
		jpaWorkingOn.add(wlaWochensummeSoFtg, new GridBagConstraints(5, iZeile,
				1, 1, 0.25, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWochensummeSoFtg, new GridBagConstraints(6, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -80, 0));
		iZeile++;

		jpaWorkingOn.add(wlaKommt, new GridBagConstraints(0, iZeile, 1, 1,
				0.25, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommt, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 50, 0));
		ImageIcon iicon = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/clock16x16.png"));
		WrapperLabel wlaUeberschrift0 = new WrapperLabel(iicon);
		wlaUeberschrift0.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift0, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));

		jpaWorkingOn.add(wlaSollzeit, new GridBagConstraints(3, iZeile, 1, 1,
				0.35, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSollzeit, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		WrapperLabel wlaUeberschrift1 = new WrapperLabel("hh:mm");
		wlaUeberschrift1.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift1, new GridBagConstraints(5, iZeile, 1,
				1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaGeht, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfGeht, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 60, 0));

		iicon = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/clock16x16.png"));
		WrapperLabel wlaUeberschrift3 = new WrapperLabel(iicon);
		wlaUeberschrift3.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift3, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));

		jpaWorkingOn.add(wlaErlaubteAnwesenheit, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfErlaubteAnwesenheit, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel wlaUeberschrift4 = new WrapperLabel("hh:mm");
		wlaUeberschrift4.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift4, new GridBagConstraints(5, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaRundungBeginn, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wspRundungBeginn, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(10, 2, 2, 2), 70, 0));

		jpaWorkingOn
				.add(wlaAutopauseab, new GridBagConstraints(3, iZeile, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2),
						50, 0));
		jpaWorkingOn.add(wtfAutopauseab, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(10, 2, 2, 2), 70, 0));
		WrapperLabel wlaUeberschrift2 = new WrapperLabel("hh:mm");
		wlaUeberschrift2.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift2, new GridBagConstraints(5, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 30, 0));
		iZeile++;
		jpaWorkingOn.add(wlaRundungEnde, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wspRundungEnde, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 70, 0));

		jpaWorkingOn.add(wlaMindestpausenzeit, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));
		jpaWorkingOn.add(wtfMindestpausenzeit, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 70, 0));

		WrapperLabel wlaUeberschrift5 = new WrapperLabel("hh:mm");
		wlaUeberschrift5.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift5, new GridBagConstraints(5, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30, 0));

		iZeile++;

		jpaWorkingOn.add(wcbRundesondertatigkeiten, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (bPausenNurWarnung == false) {
			jpaWorkingOn.add(wlaAutopauseab2, new GridBagConstraints(2, iZeile,
					2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2),
					-50, 0));
			jpaWorkingOn.add(wtfAutopauseab2, new GridBagConstraints(4, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(10, 2, 2, 2), 70, 0));

			WrapperLabel wlaUeberschrift6 = new WrapperLabel("hh:mm");
			wlaUeberschrift6.setHorizontalAlignment(SwingConstants.LEFT);
			jpaWorkingOn.add(wlaUeberschrift6, new GridBagConstraints(5,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 30,
					0));

			iZeile++;

			jpaWorkingOn.add(wlaMindestpausenzeit2, new GridBagConstraints(3,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50,
					0));
			jpaWorkingOn.add(wtfMindestpausenzeit2, new GridBagConstraints(4,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 70, 0));
			WrapperLabel wlaUeberschrift8 = new WrapperLabel("hh:mm");
			wlaUeberschrift8.setHorizontalAlignment(SwingConstants.LEFT);

			jpaWorkingOn.add(wlaUeberschrift8, new GridBagConstraints(5,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30,
					0));
			iZeile++;
			jpaWorkingOn.add(wlaAutopauseab3, new GridBagConstraints(2, iZeile,
					2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2),
					-50, 0));
			jpaWorkingOn.add(wtfAutopauseab3, new GridBagConstraints(4, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(10, 2, 2, 2), 70, 0));
			WrapperLabel wlaUeberschrift7 = new WrapperLabel("hh:mm");
			wlaUeberschrift7.setHorizontalAlignment(SwingConstants.LEFT);
			jpaWorkingOn.add(wlaUeberschrift7, new GridBagConstraints(5,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 30,
					0));

			iZeile++;
			jpaWorkingOn.add(wlaMindestpausenzeit3, new GridBagConstraints(3,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50,
					0));
			jpaWorkingOn.add(wtfMindestpausenzeit3, new GridBagConstraints(4,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 70, 0));

			WrapperLabel wlaUeberschrift9 = new WrapperLabel("hh:mm");
			wlaUeberschrift9.setHorizontalAlignment(SwingConstants.LEFT);

			jpaWorkingOn.add(wlaUeberschrift9, new GridBagConstraints(5,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30,
					0));
		}
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZEITMODELL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			zeitmodelltagDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.zeitmodelltagFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}
}
