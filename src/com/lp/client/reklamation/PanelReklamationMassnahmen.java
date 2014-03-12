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
package com.lp.client.reklamation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.reklamation.service.MassnahmeDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.WirksamkeitDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelReklamationMassnahmen extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ReklamationDto reklamationDto = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperButton wbuPersonalMassnahmeKurz = new WrapperButton();
	private WrapperButton wbuPersonalMassnahmeMittel = new WrapperButton();
	private WrapperButton wbuPersonalMassnahmeLang = new WrapperButton();
	private WrapperButton wbuMassnahmeKurz = new WrapperButton();
	private WrapperButton wbuMassnahmeMittel = new WrapperButton();
	private WrapperButton wbuMassnahmeLang = new WrapperButton();

	private WrapperSelectField wsfPersonalwirksamkeit = new WrapperSelectField(
			WrapperSelectField.PERSONAL, getInternalFrame(), true);

	private WrapperTextField wtfPersonalMassnahmeKurz = new WrapperTextField();
	private WrapperTextField wtfPersonalMassnahmeMittel = new WrapperTextField();
	private WrapperTextField wtfPersonalMassnahmeLang = new WrapperTextField();
	private WrapperTextField wtfMassnahmeKurz = new WrapperTextField();
	private WrapperTextField wtfMassnahmeMittel = new WrapperTextField();
	private WrapperTextField wtfMassnahmeLang = new WrapperTextField();

	private WrapperLabel wlaMassnahmeKurzEingefuehrt = new WrapperLabel();
	private WrapperLabel wlaMassnahmeMittelEingefuehrt = new WrapperLabel();
	private WrapperLabel wlaMassnahmeLangEingefuehrt = new WrapperLabel();

	private WrapperDateField wdfMassnahmeKurzEingefuehrt = new WrapperDateField();
	private WrapperDateField wdfMassnahmeMittelEingefuehrt = new WrapperDateField();
	private WrapperDateField wdfMassnahmeLangEingefuehrt = new WrapperDateField();

	private WrapperDateField wdfWirksamkeitEingefuehrt = new WrapperDateField();
	private WrapperDateField wdfWirksamkeitBis = new WrapperDateField();

	private WrapperLabel wlaMassnahmeBisKurz = new WrapperLabel();
	private WrapperLabel wlaMassnahmeBisMittel = new WrapperLabel();
	private WrapperLabel wlaMassnahmeBisLang = new WrapperLabel();

	private WrapperEditorField wefWirksamkeit = new WrapperEditorFieldTexteingabe(
			getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
					"lp.bemerkung"));
	private WrapperEditorField wefMassnameKurz = new WrapperEditorFieldTexteingabe(
			getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
					"lp.bemerkung"));
	private WrapperEditorField wefMassnameMittel = new WrapperEditorFieldTexteingabe(
			getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
					"lp.bemerkung"));
	private WrapperEditorField wefMassnameLang = new WrapperEditorFieldTexteingabe(
			getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
					"lp.bemerkung"));

	private WrapperDateField wdfMassnahmeBisKurz = new WrapperDateField();

	private WrapperDateField wdfMassnahmeBisMittelt = new WrapperDateField();
	private WrapperDateField wdfMassnahmeBisLang = new WrapperDateField();

	private WrapperButton wbuWirksamkeit = new WrapperButton();

	private WrapperTextField wtfWirksamkeit = new WrapperTextField();

	private WrapperNumberField wnfAnzahlLagernde = new WrapperNumberField();
	private WrapperNumberField wnfAnzahlGelieferte = new WrapperNumberField();

	private WrapperCheckBox wcbBetrifftLagerstand = new WrapperCheckBox();
	private WrapperCheckBox wcbBetrifftGelieferte = new WrapperCheckBox();

	private PanelQueryFLR panelQueryFLRMassnahmeKurz = null;
	private PanelQueryFLR panelQueryFLRPersonalMassnahmeKurzEingefuehrt = null;
	private PanelQueryFLR panelQueryFLRMassnahmeMittel = null;
	private PanelQueryFLR panelQueryFLRPersonalMassnahmeMittelEingefuehrt = null;
	private PanelQueryFLR panelQueryFLRMassnahmeLang = null;
	private PanelQueryFLR panelQueryFLRPersonalMassnahmeLangEingefuehrt = null;
	private PanelQueryFLR panelQueryFLRWirksamkeit = null;

	private InternalFrameReklamation internalFrameReklamation = null;

	static final public String ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_KURZ_FROM_LISTE = "ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_KURZ_FROM_LISTE";
	static final public String ACTION_SPECIAL_MASSNAHME_KURZ_FROM_LISTE = "ACTION_SPECIAL_MASSNAHME_KURZ_FROM_LISTE";
	static final public String ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_MITTEL_FROM_LISTE = "ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_MITTEL_FROM_LISTE";
	static final public String ACTION_SPECIAL_MASSNAHME_MITTEL_LISTE = "ACTION_SPECIAL_MASSNAHME_MITTEL_LISTE";
	static final public String ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_LANG_FROM_LISTE = "ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_LANG_FROM_LISTE";
	static final public String ACTION_SPECIAL_MASSNAHME_LANG_LISTE = "ACTION_SPECIAL_MASSNAHME_LANG_LISTE";

	static final public String ACTION_SPECIAL_WIRKSAMKEIT_FROM_LISTE = "ACTION_SPECIAL_WIRKSAMKEIT_FROM_LISTE";

	public PanelReklamationMassnahmen(InternalFrameReklamation internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameReklamation = internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_REKLAMATION;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuWirksamkeit;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);
		reklamationDto = DelegateFactory
				.getInstance()
				.getReklamationDelegate()
				.reklamationFindByPrimaryKey(
						((InternalFrameReklamation) getInternalFrame())
								.getReklamationDto().getIId());
		dto2Components();
	}

	void dialogQueryMassnahmeKurzFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRMassnahmeKurz = ReklamationFilterFactory.getInstance()
				.createPanelFLRMassnahme(getInternalFrame(),
						reklamationDto.getMassnahmeIIdKurz());

		new DialogQuery(panelQueryFLRMassnahmeKurz);
	}

	void dialogQueryMassnahmeMittelFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRMassnahmeMittel = ReklamationFilterFactory.getInstance()
				.createPanelFLRMassnahme(getInternalFrame(),
						reklamationDto.getMassnahmeIIdMittel());

		new DialogQuery(panelQueryFLRMassnahmeMittel);
	}

	void dialogQueryMassnahmeLangFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRMassnahmeLang = ReklamationFilterFactory.getInstance()
				.createPanelFLRMassnahme(getInternalFrame(),
						reklamationDto.getMassnahmeIIdLang());

		new DialogQuery(panelQueryFLRMassnahmeLang);
	}

	void dialogQueryPersonalMassnahmeKurzEingefuehrtFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRPersonalMassnahmeKurzEingefuehrt = PersonalFilterFactory
				.getInstance().createPanelFLRPersonal(getInternalFrame(), true,
						true, reklamationDto.getPersonalIIdEingefuehrtkurz());
		new DialogQuery(panelQueryFLRPersonalMassnahmeKurzEingefuehrt);
	}

	void dialogQueryPersonalMassnahmeMittelEingefuehrtFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRPersonalMassnahmeMittelEingefuehrt = PersonalFilterFactory
				.getInstance().createPanelFLRPersonal(getInternalFrame(), true,
						true, reklamationDto.getPersonalIIdEingefuehrtmittel());
		new DialogQuery(panelQueryFLRPersonalMassnahmeMittelEingefuehrt);
	}

	void dialogQueryPersonalMassnahmeLangEingefuehrtFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRPersonalMassnahmeLangEingefuehrt = PersonalFilterFactory
				.getInstance().createPanelFLRPersonal(getInternalFrame(), true,
						true, reklamationDto.getPersonalIIdEingefuehrtlang());
		new DialogQuery(panelQueryFLRPersonalMassnahmeLangEingefuehrt);
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

		wbuMassnahmeKurz.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.massnahmekurz")
				+ "...");
		wbuMassnahmeKurz
				.setActionCommand(ACTION_SPECIAL_MASSNAHME_KURZ_FROM_LISTE);
		wbuMassnahmeKurz.addActionListener(this);
		wtfMassnahmeKurz.setActivatable(false);

		wbuMassnahmeMittel.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.massnahmemittel")
				+ "...");
		wbuMassnahmeMittel
				.setActionCommand(ACTION_SPECIAL_MASSNAHME_MITTEL_LISTE);
		wbuMassnahmeMittel.addActionListener(this);
		wtfMassnahmeMittel.setActivatable(false);

		wbuMassnahmeLang.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.massnahmelang")
				+ "...");
		wbuMassnahmeLang.setActionCommand(ACTION_SPECIAL_MASSNAHME_LANG_LISTE);
		wbuMassnahmeLang.addActionListener(this);
		wtfMassnahmeLang.setActivatable(false);

		wbuWirksamkeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.wirksamkeit")
				+ "...");

		wsfPersonalwirksamkeit.setText(LPMain.getInstance()
				.getTextRespectUISPr("rekla.personal") + "...");

		wcbBetrifftGelieferte.addActionListener(this);
		wcbBetrifftLagerstand.addActionListener(this);

		wcbBetrifftLagerstand.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.betrifftlagerstand"));
		wcbBetrifftGelieferte.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.betrifftgelieferte"));

		wbuWirksamkeit.setActionCommand(ACTION_SPECIAL_WIRKSAMKEIT_FROM_LISTE);
		wbuWirksamkeit.addActionListener(this);
		wtfWirksamkeit.setActivatable(false);

		wlaMassnahmeBisKurz.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.schwere"));
		wlaMassnahmeBisKurz.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.massnahmegeplant"));
		wlaMassnahmeBisLang.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.massnahmegeplant"));
		wlaMassnahmeBisMittel.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.massnahmegeplant"));

		wnfAnzahlGelieferte.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfAnzahlLagernde.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());

		wbuPersonalMassnahmeKurz.setText(LPMain.getInstance()
				.getTextRespectUISPr("rekla.personal") + "...");
		wbuPersonalMassnahmeKurz
				.setActionCommand(ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_KURZ_FROM_LISTE);
		wbuPersonalMassnahmeKurz.addActionListener(this);
		wtfPersonalMassnahmeKurz.setActivatable(false);

		wbuPersonalMassnahmeMittel.setText(LPMain.getInstance()
				.getTextRespectUISPr("rekla.personal") + "...");
		wbuPersonalMassnahmeMittel
				.setActionCommand(ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_MITTEL_FROM_LISTE);
		wbuPersonalMassnahmeMittel.addActionListener(this);
		wtfPersonalMassnahmeMittel.setActivatable(false);

		wbuPersonalMassnahmeLang.setText(LPMain.getInstance()
				.getTextRespectUISPr("rekla.personal") + "...");
		wbuPersonalMassnahmeLang
				.setActionCommand(ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_LANG_FROM_LISTE);
		wbuPersonalMassnahmeLang.addActionListener(this);
		wtfPersonalMassnahmeLang.setActivatable(false);
		wlaMassnahmeKurzEingefuehrt.setText(LPMain.getInstance()
				.getTextRespectUISPr("rekla.eingefuehrt"));
		wlaMassnahmeMittelEingefuehrt.setText(LPMain.getInstance()
				.getTextRespectUISPr("rekla.eingefuehrt"));
		wlaMassnahmeLangEingefuehrt.setText(LPMain.getInstance()
				.getTextRespectUISPr("rekla.eingefuehrt"));

		wtfMassnahmeKurz.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfMassnahmeMittel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfMassnahmeLang.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

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
		jpaWorkingOn.add(wbuMassnahmeKurz, new GridBagConstraints(0, iZeile, 1,
				1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMassnahmeKurz, new GridBagConstraints(1, iZeile, 1,
				1, 0.7, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMassnahmeBisKurz, new GridBagConstraints(2, iZeile,
				1, 1, 0.4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfMassnahmeBisKurz, new GridBagConstraints(3, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuPersonalMassnahmeKurz, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonalMassnahmeKurz, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMassnahmeKurzEingefuehrt, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfMassnahmeKurzEingefuehrt, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wefMassnameKurz, new GridBagConstraints(0, iZeile, 4,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 10));

		iZeile++;
		jpaWorkingOn.add(wbuMassnahmeMittel, new GridBagConstraints(0, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMassnahmeMittel, new GridBagConstraints(1, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMassnahmeBisMittel, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfMassnahmeBisMittelt, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuPersonalMassnahmeMittel, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonalMassnahmeMittel, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMassnahmeMittelEingefuehrt, new GridBagConstraints(
				2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfMassnahmeMittelEingefuehrt, new GridBagConstraints(
				3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wefMassnameMittel, new GridBagConstraints(0, iZeile,
				4, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 10));
		iZeile++;
		jpaWorkingOn.add(wbuMassnahmeLang, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMassnahmeLang, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMassnahmeBisLang, new GridBagConstraints(2, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfMassnahmeBisLang, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuPersonalMassnahmeLang, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonalMassnahmeLang, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMassnahmeLangEingefuehrt, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfMassnahmeLangEingefuehrt, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wefMassnameLang, new GridBagConstraints(0, iZeile, 4,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 10));
		iZeile++;

		jpaWorkingOn.add(wbuWirksamkeit, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWirksamkeit, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("rekla.massnahmegeplant")),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wdfWirksamkeitBis, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfPersonalwirksamkeit, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfPersonalwirksamkeit.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("rekla.durchgefuehrt")),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wdfWirksamkeitEingefuehrt, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wefWirksamkeit, new GridBagConstraints(0, iZeile, 4,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 10));
		iZeile++;
		jpaWorkingOn.add(wcbBetrifftGelieferte, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAnzahlGelieferte, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wcbBetrifftLagerstand, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAnzahlLagernde, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 50, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void setDefaults() throws Throwable {
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_MASSNAHME_KURZ_FROM_LISTE)) {
			dialogQueryMassnahmeKurzFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_MASSNAHME_MITTEL_LISTE)) {
			dialogQueryMassnahmeMittelFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_MASSNAHME_LANG_LISTE)) {
			dialogQueryMassnahmeLangFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_KURZ_FROM_LISTE)) {
			dialogQueryPersonalMassnahmeKurzEingefuehrtFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_MITTEL_FROM_LISTE)) {
			dialogQueryPersonalMassnahmeMittelEingefuehrtFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONAL_EINGEFUEHRT_LANG_FROM_LISTE)) {
			dialogQueryPersonalMassnahmeLangEingefuehrtFromListe(e);
		}

		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_WIRKSAMKEIT_FROM_LISTE)) {
			dialogQueryWirksamkeitFromListe(e);
		}

		else if (e.getSource().equals(wcbBetrifftGelieferte)) {
			if (wcbBetrifftGelieferte.isSelected()) {
				wnfAnzahlGelieferte.setEditable(true);
			} else {
				wnfAnzahlGelieferte.setBigDecimal(null);
				wnfAnzahlGelieferte.setEditable(false);
			}
		} else if (e.getSource().equals(wcbBetrifftLagerstand)) {
			if (wcbBetrifftLagerstand.isSelected()) {
				wnfAnzahlLagernde.setEditable(true);
			} else {
				wnfAnzahlLagernde.setBigDecimal(null);
				wnfAnzahlLagernde.setEditable(false);
			}
		}

	}

	void dialogQueryWirksamkeitFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRWirksamkeit = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_WIRKSAMKEIT, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"rekla.wirksamkeit"));
		panelQueryFLRWirksamkeit.befuellePanelFilterkriterienDirekt(
				ReklamationFilterFactory.getInstance()
						.createFKDBezeichnungMitAlias("wirksamkeit"), null);
		panelQueryFLRWirksamkeit.setSelectedId(reklamationDto
				.getWirksamkeitIId());

		new DialogQuery(panelQueryFLRWirksamkeit);
	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wdfMassnahmeBisKurz.setTimestamp(reklamationDto.getTMassnahmebiskurz());
		wdfMassnahmeBisLang.setTimestamp(reklamationDto.getTMassnahmebislang());
		wdfMassnahmeBisMittelt.setTimestamp(reklamationDto
				.getTMassnahmebismittel());

		wdfWirksamkeitBis.setTimestamp(reklamationDto.getTWirksamkeitbis());
		wdfWirksamkeitEingefuehrt.setTimestamp(reklamationDto
				.getTWirksamkeiteingefuehrt());
		wsfPersonalwirksamkeit.setKey(reklamationDto
				.getPersonalIIdWirksamkeit());

		wdfMassnahmeKurzEingefuehrt.setTimestamp(reklamationDto
				.getTEingefuehrtkurz());
		wdfMassnahmeLangEingefuehrt.setTimestamp(reklamationDto
				.getTEingefuehrtlang());
		wdfMassnahmeMittelEingefuehrt.setTimestamp(reklamationDto
				.getTEingefuehrtmittel());

		wnfAnzahlGelieferte
				.setBigDecimal(reklamationDto.getNStueckgelieferte());
		wnfAnzahlLagernde.setBigDecimal(reklamationDto.getNStuecklagerstand());

		wcbBetrifftGelieferte.setShort(reklamationDto.getBBetrifftgelieferte());
		wcbBetrifftLagerstand.setShort(reklamationDto.getBBetrifftlagerstand());

		if (reklamationDto.getPersonalIIdEingefuehrtkurz() != null) {
			PersonalDto personalDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							reklamationDto.getPersonalIIdEingefuehrtkurz());
			wtfPersonalMassnahmeKurz.setText(personalDto.formatAnrede());
		} else {
			wtfPersonalMassnahmeKurz.setText(null);
		}

		if (reklamationDto.getPersonalIIdEingefuehrtlang() != null) {
			PersonalDto personalDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							reklamationDto.getPersonalIIdEingefuehrtlang());
			wtfPersonalMassnahmeLang.setText(personalDto.formatAnrede());
		} else {
			wtfPersonalMassnahmeLang.setText(null);
		}

		if (reklamationDto.getPersonalIIdEingefuehrtmittel() != null) {
			PersonalDto personalDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							reklamationDto.getPersonalIIdEingefuehrtmittel());
			wtfPersonalMassnahmeMittel.setText(personalDto.formatAnrede());
		} else {
			wtfPersonalMassnahmeMittel.setText(null);
		}

		if (reklamationDto.getWirksamkeitIId() != null) {
			WirksamkeitDto wirksamkeitDto = DelegateFactory
					.getInstance()
					.getReklamationDelegate()
					.wirksamkeitFindByPrimaryKey(
							reklamationDto.getWirksamkeitIId());
			wtfWirksamkeit.setText(wirksamkeitDto.getCBez());
		} else {
			wtfWirksamkeit.setText(null);
		}

		if (reklamationDto.getMassnahmeIIdKurz() != null) {
			MassnahmeDto massnahmeDto = DelegateFactory
					.getInstance()
					.getReklamationDelegate()
					.massnahmeFindByPrimaryKey(
							reklamationDto.getMassnahmeIIdKurz());
			wtfMassnahmeKurz.setText(massnahmeDto.getCBez());
		} else {
			wtfMassnahmeKurz.setText(null);
		}
		if (reklamationDto.getMassnahmeIIdLang() != null) {
			MassnahmeDto massnahmeDto = DelegateFactory
					.getInstance()
					.getReklamationDelegate()
					.massnahmeFindByPrimaryKey(
							reklamationDto.getMassnahmeIIdLang());
			wtfMassnahmeLang.setText(massnahmeDto.getCBez());
		} else {
			wtfMassnahmeLang.setText(null);
		}
		if (reklamationDto.getMassnahmeIIdMittel() != null) {
			MassnahmeDto massnahmeDto = DelegateFactory
					.getInstance()
					.getReklamationDelegate()
					.massnahmeFindByPrimaryKey(
							reklamationDto.getMassnahmeIIdMittel());
			wtfMassnahmeMittel.setText(massnahmeDto.getCBez());
		} else {
			wtfMassnahmeMittel.setText(null);
		}

		wefMassnameKurz.setText(reklamationDto.getXMassnahmeKurz());
		wefMassnameMittel.setText(reklamationDto.getXMassnahmeMittel());
		wefMassnameLang.setText(reklamationDto.getXMassnahmeLang());
		wefWirksamkeit.setText(reklamationDto.getXWirksamkeit());

		this.setStatusbarPersonalIIdAendern(reklamationDto
				.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(reklamationDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(reklamationDto.getTAnlegen());
		this.setStatusbarTAendern(reklamationDto.getTAendern());
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		boolean b = internalFrameReklamation.getTabbedPaneReklamation()
				.pruefeObReklamationAenderbar();
		if (b == true) {
			super.eventActionUpdate(aE, false); // Buttons schalten
		} else {
			return;
		}
	}

	protected void components2Dto() throws Throwable {
		reklamationDto.setTEingefuehrtkurz(wdfMassnahmeKurzEingefuehrt
				.getTimestamp());
		reklamationDto.setTEingefuehrtlang(wdfMassnahmeLangEingefuehrt
				.getTimestamp());
		reklamationDto.setTEingefuehrtmittel(wdfMassnahmeMittelEingefuehrt
				.getTimestamp());

		reklamationDto.setTMassnahmebiskurz(wdfMassnahmeBisKurz.getTimestamp());
		reklamationDto.setTMassnahmebislang(wdfMassnahmeBisLang.getTimestamp());
		reklamationDto.setTMassnahmebismittel(wdfMassnahmeBisMittelt
				.getTimestamp());

		reklamationDto
				.setNStueckgelieferte(wnfAnzahlGelieferte.getBigDecimal());
		reklamationDto.setNStuecklagerstand(wnfAnzahlLagernde.getBigDecimal());

		reklamationDto.setBBetrifftgelieferte(wcbBetrifftGelieferte.getShort());
		reklamationDto.setBBetrifftlagerstand(wcbBetrifftLagerstand.getShort());

		reklamationDto.setXMassnahmeKurz(wefMassnameKurz.getText());
		reklamationDto.setXMassnahmeMittel(wefMassnameMittel.getText());
		reklamationDto.setXMassnahmeLang(wefMassnameLang.getText());

		reklamationDto.setXWirksamkeit(wefWirksamkeit.getText());

		reklamationDto.setTWirksamkeiteingefuehrt(wdfWirksamkeitEingefuehrt
				.getTimestamp());
		reklamationDto.setTWirksamkeitbis(wdfWirksamkeitBis.getTimestamp());

		reklamationDto.setPersonalIIdWirksamkeit(wsfPersonalwirksamkeit
				.getIKey());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRMassnahmeKurz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				MassnahmeDto massnahmeDto = DelegateFactory.getInstance()
						.getReklamationDelegate()
						.massnahmeFindByPrimaryKey((Integer) key);
				wtfMassnahmeKurz.setText(massnahmeDto.getCBez());
				reklamationDto.setMassnahmeIIdKurz(massnahmeDto.getIId());
			} else if (e.getSource() == panelQueryFLRMassnahmeMittel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				MassnahmeDto massnahmeDto = DelegateFactory.getInstance()
						.getReklamationDelegate()
						.massnahmeFindByPrimaryKey((Integer) key);
				wtfMassnahmeMittel.setText(massnahmeDto.getCBez());
				reklamationDto.setMassnahmeIIdMittel(massnahmeDto.getIId());
			} else if (e.getSource() == panelQueryFLRMassnahmeLang) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				MassnahmeDto massnahmeDto = DelegateFactory.getInstance()
						.getReklamationDelegate()
						.massnahmeFindByPrimaryKey((Integer) key);
				wtfMassnahmeLang.setText(massnahmeDto.getCBez());
				reklamationDto.setMassnahmeIIdLang(massnahmeDto.getIId());
			}

			else if (e.getSource() == panelQueryFLRWirksamkeit) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				WirksamkeitDto wirksamkeitDto = DelegateFactory.getInstance()
						.getReklamationDelegate()
						.wirksamkeitFindByPrimaryKey((Integer) key);
				wtfWirksamkeit.setText(wirksamkeitDto.getCBez());
				reklamationDto.setWirksamkeitIId((Integer) key);

			} else if (e.getSource() == panelQueryFLRPersonalMassnahmeKurzEingefuehrt) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfPersonalMassnahmeKurz
							.setText(personalDto.formatAnrede());
					reklamationDto.setPersonalIIdEingefuehrtkurz((Integer) key);
				}
			} else if (e.getSource() == panelQueryFLRPersonalMassnahmeMittelEingefuehrt) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfPersonalMassnahmeMittel.setText(personalDto
							.formatAnrede());
					reklamationDto
							.setPersonalIIdEingefuehrtmittel((Integer) key);
				}
			} else if (e.getSource() == panelQueryFLRPersonalMassnahmeLangEingefuehrt) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfPersonalMassnahmeLang
							.setText(personalDto.formatAnrede());
					reklamationDto.setPersonalIIdEingefuehrtlang((Integer) key);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRWirksamkeit) {
				wtfWirksamkeit.setText(null);
				reklamationDto.setWirksamkeitIId(null);
			} else if (e.getSource() == panelQueryFLRMassnahmeKurz) {
				wtfMassnahmeKurz.setText(null);
				reklamationDto.setMassnahmeIIdKurz(null);
			} else if (e.getSource() == panelQueryFLRMassnahmeLang) {
				wtfMassnahmeLang.setText(null);
				reklamationDto.setMassnahmeIIdLang(null);
			} else if (e.getSource() == panelQueryFLRMassnahmeMittel) {
				wtfMassnahmeMittel.setText(null);
				reklamationDto.setMassnahmeIIdMittel(null);
			} else if (e.getSource() == panelQueryFLRPersonalMassnahmeKurzEingefuehrt) {
				wtfPersonalMassnahmeKurz.setText(null);
				reklamationDto.setPersonalIIdEingefuehrtkurz(null);
			} else if (e.getSource() == panelQueryFLRPersonalMassnahmeLangEingefuehrt) {
				wtfPersonalMassnahmeLang.setText(null);
				reklamationDto.setPersonalIIdEingefuehrtlang(null);
			} else if (e.getSource() == panelQueryFLRPersonalMassnahmeMittelEingefuehrt) {
				wtfPersonalMassnahmeMittel.setText(null);
				reklamationDto.setPersonalIIdEingefuehrtmittel(null);
			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();
			DelegateFactory.getInstance().getReklamationDelegate()
					.updateReklamation(reklamationDto);
		}
		super.eventActionSave(e, true);
	}

}
