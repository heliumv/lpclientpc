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
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperEmailField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperPasswordField;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.BerufDto;
import com.lp.server.personal.service.KollektivDto;
import com.lp.server.personal.service.LohngruppeDto;
import com.lp.server.personal.service.PendlerpauschaleDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ReligionDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelPersonaldaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PersonalDto personalDto = null;
	private InternalFramePersonal internalFramePersonal = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaGeburtsdatum = new WrapperLabel();
	private WrapperDateField wdfGeburtsdatum = new WrapperDateField();
	private WrapperButton wbuGeburtsort = new WrapperButton();
	private WrapperTextField wtfGeburtsort = new WrapperTextField();
	private WrapperLabel wlaSozialversicherungsnummer = new WrapperLabel();
	private WrapperTextField wtfsozialversicherungsnummer = new WrapperTextField();
	private WrapperButton wbuSozialversicherer = new WrapperButton();
	private WrapperButton wbuKollektiv = new WrapperButton();
	private WrapperTextField wtfKollektiv = new WrapperTextField();
	private WrapperTextField wtfSozialversicherer = new WrapperTextField();
	private WrapperButton wbuBeruf = new WrapperButton();
	private WrapperTextField wtfBeruf = new WrapperTextField();
	private WrapperButton wbuLohngruppe = new WrapperButton();
	private WrapperTextField wtfLohngruppe = new WrapperTextField();
	private WrapperCheckBox wcbKeineAnzeigeInAnwesenheitsliste = new WrapperCheckBox();
	private WrapperCheckBox wcbBekommtUeberstundenausbezahlt = new WrapperCheckBox();
	private WrapperCheckBox wcbAnwesenheitslisteTermial = new WrapperCheckBox();
	private WrapperCheckBox wcbAnwesenheitslisteAlleTermial = new WrapperCheckBox();
	private WrapperEditorField wefSignatur = null;
	private WrapperLabel wlaSignatur = new WrapperLabel();

	private WrapperTextField wtfPendlerpauschale = new WrapperTextField();
	private WrapperButton wbuPendlerpauschale = new WrapperButton();
	private WrapperComboBox wcbFamilienstand = new WrapperComboBox();
	private WrapperLabel wlaFamilienstand = new WrapperLabel();
	private WrapperTextField wtfStaatsangehoerigkeit = new WrapperTextField();
	private WrapperButton wbuStaatsangehoerigkeit = new WrapperButton();
	private WrapperTextField wtfReligion = new WrapperTextField();
	private WrapperButton wbuReligion = new WrapperButton();
	private WrapperTextField wtfFirmenzugehoerigkeit = new WrapperTextField();
	private WrapperButton wbuFirmenzugehoerigkeit = new WrapperButton();

	private WrapperLabel wlaAbsenderdaten = null;
	private WrapperLabel wlaDurchwahl = null;
	private WrapperTextField wtfDurchwahl = null;
	private WrapperLabel wlaEmail = null;
	private WrapperEmailField wtfEmail = null;
	private WrapperLabel wlaFaxdurchwahl = null;
	private WrapperTextField wtfFaxdurchwahl = null;
	private WrapperLabel wlaHandy = null;
	private WrapperTelefonField wtfHandy = null;
	private WrapperLabel wlaDirektfax = null;
	private WrapperTextField wtfDirektfax = null;

	private WrapperTextField wtfUnterschriftsfunktion = new WrapperTextField();
	private WrapperTextField wtfUnterschriftstext = new WrapperTextField();
	private WrapperLabel wlaUnterschriftsfunktion = new WrapperLabel();
	private WrapperLabel wlaUnterschriftstext = new WrapperLabel();

	private PanelQueryFLR panelQueryFLRGeburtsort = null;
	private PanelQueryFLR panelQueryFLRSozialversicherer = null;
	private PanelQueryFLR panelQueryFLRStaatsangehoerigkeit = null;
	private PanelQueryFLR panelQueryFLRReligion = null;
	private PanelQueryFLR panelQueryFLRKollektiv = null;
	private PanelQueryFLR panelQueryFLRBeruf = null;
	private PanelQueryFLR panelQueryFLRPendlerpauschale = null;
	private PanelQueryFLR panelQueryFLRLohngrupe = null;
	private PanelQueryFLR panelQueryFLRfirmenzugehoerigkeit = null;

	private WrapperLabel wlaImapBenutzer = new WrapperLabel();
	private WrapperTextField wtfImapBenutzer = new WrapperTextField();

	private WrapperLabel wlaImapKennwort = new WrapperLabel();
	private WrapperPasswordField wtfImapKennwort = new WrapperPasswordField();

	static final public String ACTION_SPECIAL_GEBURTSORT_FROM_LISTE = "action_geburtsort_from_liste";
	static final public String ACTION_SPECIAL_SOZIALVERISCHERER_FROM_LISTE = "action_sozialversicherer_from_liste";
	static final public String ACTION_SPECIAL_STAATSANGEHOERIGKEIT_FROM_LISTE = "action_staatsangehoerigkeit_from_liste";
	static final public String ACTION_SPECIAL_RELIGION_FROM_LISTE = "action_religion_from_liste";
	static final public String ACTION_SPECIAL_KOLLEKTIV_FROM_LISTE = "action_kollektiv_from_liste";
	static final public String ACTION_SPECIAL_BERUF_FROM_LISTE = "action_beruf_from_liste";
	static final public String ACTION_SPECIAL_PENDLERPAUSCHALE_FROM_LISTE = "action_pendlerpauschale_from_liste";
	static final public String ACTION_SPECIAL_LOHNGRUPPE_FROM_LISTE = "action_lohngruppe_from_liste";
	static final public String ACTION_SPECIAL_FIRMENZUGEHOERIGKEIT_FROM_LISTE = "action_firmenzugehoerigkeit_from_liste";

	public PanelPersonaldaten(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfGeburtsdatum;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);
		personalDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						((InternalFramePersonal) getInternalFrame())
								.getPersonalDto().getIId());
		dto2Components();
	}

	private void jbInit() throws Throwable {
		setBorder(border);
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
		wlaGeburtsdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalangehoerige.geburtsdatum"));
		wbuGeburtsort.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.geburtsort")
				+ "...");

		wlaSignatur.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.signatur"));
		wefSignatur = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("pers.signatur"));

		wefSignatur.getLpEditor().getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT_WENN_NTEXT;

		wbuGeburtsort
				.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_GEBURTSORT_FROM_LISTE);
		wbuGeburtsort.addActionListener(this);
		wtfGeburtsort.setActivatable(false);
		wtfGeburtsort.setText("");
		wtfGeburtsort.setColumnsMax(200);
		wlaSozialversicherungsnummer.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"pers.personalangehoerige.sozialversicherungsnummer"));
		wtfsozialversicherungsnummer.setText("");
		wtfsozialversicherungsnummer
				.setColumnsMax(PersonalFac.MAX_PERSONAL_SOZIALVERSNR);
		wbuSozialversicherer.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.sozialversicherer")
				+ "...");
		wbuSozialversicherer
				.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_SOZIALVERISCHERER_FROM_LISTE);
		wbuSozialversicherer.addActionListener(this);
		wbuKollektiv.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.kollektiv")
				+ "...");
		wbuKollektiv
				.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_KOLLEKTIV_FROM_LISTE);
		wbuKollektiv.addActionListener(this);
		wtfKollektiv.setActivatable(false);
		wtfKollektiv.setText("");
		wtfKollektiv.setColumnsMax(80);
		wtfSozialversicherer.setActivatable(false);
		wtfSozialversicherer.setColumnsMax(200);
		wbuBeruf.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.beruf")
				+ "...");
		wbuBeruf.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_BERUF_FROM_LISTE);
		wbuBeruf.addActionListener(this);
		wtfBeruf.setActivatable(false);
		wtfBeruf.setText("");
		wtfBeruf.setColumnsMax(80);

		wbuLohngruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.lohngruppe")
				+ "...");
		wbuLohngruppe
				.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_LOHNGRUPPE_FROM_LISTE);
		wbuLohngruppe.addActionListener(this);
		wtfLohngruppe.setActivatable(false);
		wtfLohngruppe.setText("");
		wtfLohngruppe.setColumnsMax(80);
		wcbKeineAnzeigeInAnwesenheitsliste.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"pers.personaldaten.keineanzeigeinanwesenheitsliste"));
		wcbBekommtUeberstundenausbezahlt.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"pers.personaldaten.keineueberstundenauszahlung"));
		wcbAnwesenheitslisteTermial.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"pers.personaldaten.anwesenheitslisteterminal"));
		wcbAnwesenheitslisteAlleTermial.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"pers.personaldaten.anwesenheitslistealleterminal"));

		wtfPendlerpauschale.setSelectionStart(17);
		wtfPendlerpauschale.setActivatable(false);
		wtfPendlerpauschale.setText("");
		wtfPendlerpauschale.setColumnsMax(80);
		wbuPendlerpauschale.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.pendlerpauschale")
				+ "...");
		wbuPendlerpauschale
				.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_PENDLERPAUSCHALE_FROM_LISTE);
		wbuPendlerpauschale.addActionListener(this);
		wlaFamilienstand.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.familienstand"));
		wtfStaatsangehoerigkeit.setActivatable(false);
		wtfStaatsangehoerigkeit.setText("");
		wtfStaatsangehoerigkeit.setColumnsMax(80);
		wbuStaatsangehoerigkeit.setText(LPMain.getInstance()
				.getTextRespectUISPr("pers.personaldaten.staatsangehoerigkeit")
				+ "...");
		wbuStaatsangehoerigkeit
				.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_STAATSANGEHOERIGKEIT_FROM_LISTE);
		wbuStaatsangehoerigkeit.addActionListener(this);
		wtfReligion.setActivatable(false);
		wtfReligion.setText("");
		wtfReligion.setColumnsMax(80);
		wbuReligion.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.religion")
				+ "...");
		wbuReligion
				.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_RELIGION_FROM_LISTE);
		wbuReligion.addActionListener(this);
		wtfFirmenzugehoerigkeit.setActivatable(false);
		wtfFirmenzugehoerigkeit.setText("");
		wtfFirmenzugehoerigkeit.setColumnsMax(200);
		wcbFamilienstand.setMandatoryField(false);
		wbuFirmenzugehoerigkeit.setText(LPMain.getInstance()
				.getTextRespectUISPr("pers.personaldaten.firmenzugehoerigkeit")
				+ "...");
		wbuFirmenzugehoerigkeit
				.setActionCommand(PanelPersonaldaten.ACTION_SPECIAL_FIRMENZUGEHOERIGKEIT_FROM_LISTE);
		wbuFirmenzugehoerigkeit.addActionListener(this);

		wlaUnterschriftsfunktion.setText(LPMain.getInstance()
				.getTextRespectUISPr("benutzer.unterschriftsfunktion"));
		wlaUnterschriftstext.setText(LPMain.getInstance().getTextRespectUISPr(
				"benutzer.unterschriftstext"));

		wtfImapBenutzer.setColumnsMax(80);

		wtfUnterschriftsfunktion
				.setColumnsMax(BenutzerFac.MAX_BENUTZERMANDANTSYSTEMROLLE_C_UNTERSCHRIFTSFUNKTION);
		wtfUnterschriftstext
				.setColumnsMax(BenutzerFac.MAX_BENUTZERMANDANTSYSTEMROLLE_C_UNTERSCHRIFTSTEXT);

		wlaDurchwahl = new WrapperLabel();
		wlaDurchwahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.durchwahl"));

		wtfDurchwahl = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);
		wlaEmail = new WrapperLabel();
		wlaEmail.setText(LPMain.getInstance().getTextRespectUISPr("lp.email"));
		wtfEmail = new WrapperEmailField();

		wlaFaxdurchwahl = new WrapperLabel();
		wlaFaxdurchwahl = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.faxdurchwahl"));
		wtfFaxdurchwahl = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaHandy = new WrapperLabel();
		wlaHandy = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.handy"));
		wtfHandy = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);

		wlaDirektfax = new WrapperLabel();
		wlaDirektfax = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.direktfax"));
		wtfDirektfax = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaAbsenderdaten = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.absenderdaten") + ":");

		wlaImapBenutzer.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.imapuser"));
		wlaImapKennwort.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.imapkennwort"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile = 0;
		jpaWorkingOn.add(wlaFamilienstand, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 150, 0));
		jpaWorkingOn.add(wcbFamilienstand, new GridBagConstraints(1, iZeile, 1,
				1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaGeburtsdatum, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wdfGeburtsdatum, new GridBagConstraints(3, iZeile, 2,
				1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuGeburtsort, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfGeburtsort, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaSozialversicherungsnummer, new GridBagConstraints(
				0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfsozialversicherungsnummer, new GridBagConstraints(
				1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuSozialversicherer, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSozialversicherer, new GridBagConstraints(3,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuStaatsangehoerigkeit, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfStaatsangehoerigkeit, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuReligion, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfReligion, new GridBagConstraints(3, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKollektiv, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKollektiv, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuBeruf, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBeruf, new GridBagConstraints(3, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuPendlerpauschale, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfPendlerpauschale, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuLohngruppe, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfLohngruppe, new GridBagConstraints(3, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wtfFirmenzugehoerigkeit, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuFirmenzugehoerigkeit, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaUnterschriftsfunktion, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfUnterschriftsfunktion, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaUnterschriftstext, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfUnterschriftstext, new GridBagConstraints(3,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbKeineAnzeigeInAnwesenheitsliste,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wlaImapBenutzer, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfImapBenutzer, new GridBagConstraints(3, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbBekommtUeberstundenausbezahlt,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wlaImapKennwort, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfImapKennwort, new GridBagConstraints(3, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbAnwesenheitslisteTermial, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbAnwesenheitslisteAlleTermial,
				new GridBagConstraints(2, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaAbsenderdaten, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaDurchwahl, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfDurchwahl, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEmail, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfEmail, new GridBagConstraints(3, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaFaxdurchwahl, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFaxdurchwahl, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaHandy, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfHandy, new GridBagConstraints(3, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaDirektfax, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfDirektfax, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSignatur, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefSignatur, new GridBagConstraints(1, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 35));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void setDefaults() throws Throwable {
		wcbFamilienstand.setMap(DelegateFactory.getInstance()
				.getPersonalDelegate().getAllSprFamilienstaende());
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_SOZIALVERISCHERER_FROM_LISTE)) {
			dialogQuerySozialversichererFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_GEBURTSORT_FROM_LISTE)) {
			dialogQueryGeburtsortFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BERUF_FROM_LISTE)) {
			dialogQueryBerufFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FIRMENZUGEHOERIGKEIT_FROM_LISTE)) {
			dialogQueryFirmenzugehoerigkeitFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_KOLLEKTIV_FROM_LISTE)) {
			dialogQueryKollektivFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LOHNGRUPPE_FROM_LISTE)) {
			dialogQueryLohngruppeFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PENDLERPAUSCHALE_FROM_LISTE)) {
			dialogQueryPendlerpauschaleFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_RELIGION_FROM_LISTE)) {
			dialogQueryRelgionFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_STAATSANGEHOERIGKEIT_FROM_LISTE)) {
			dialogQueryStaatsangehoerigkeitFromListe(e);
		}
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		if (personalDto.getBerufDto() != null) {
			wtfBeruf.setText(personalDto.getBerufDto().getCBez());
		}
		if (personalDto.getKollektivDto() != null) {
			wtfKollektiv.setText(personalDto.getKollektivDto().getCBez());
		}
		if (personalDto.getPendlerpauschaleDto() != null) {
			wtfPendlerpauschale.setText(personalDto.getPendlerpauschaleDto()
					.getCBez());
		}
		if (personalDto.getReligionDto() != null) {
			wtfReligion.setText(personalDto.getReligionDto().getCNr());
		}
		if (personalDto.getLandplzortDto_Geburtsort() != null) {
			wtfGeburtsort.setText(personalDto.getLandplzortDto_Geburtsort()
					.formatLandPlzOrt());
		}
		if (personalDto.getLohngruppeIId() != null) {
			wtfLohngruppe.setText(personalDto.getLohngruppeDto().getCBez());
		}
		if (personalDto.getPartnerDto_Sozialversicherer() != null) {
			wtfSozialversicherer.setText(personalDto
					.getPartnerDto_Sozialversicherer().formatTitelAnrede());
		} else {
			wtfSozialversicherer.setText(null);
		}
		if (personalDto.getPartnerDto_Firma() != null) {
			wtfFirmenzugehoerigkeit.setText(personalDto.getPartnerDto_Firma()
					.formatTitelAnrede());
		} else {
			wtfFirmenzugehoerigkeit.setText(null);
		}

		wtfsozialversicherungsnummer.setText(personalDto.getCSozialversnr());
		if (personalDto.getLandDto() != null) {
			wtfStaatsangehoerigkeit.setText(personalDto.getLandDto().getCLkz());
		} else {
			wtfStaatsangehoerigkeit.setText(null);
		}
		wcbFamilienstand
				.setKeyOfSelectedItem(personalDto.getFamilienstandCNr());

		wcbBekommtUeberstundenausbezahlt.setShort(personalDto
				.getBUeberstundenausbezahlt());

		if (personalDto.getBAnwesenheitsliste().intValue() == 0) {
			wcbKeineAnzeigeInAnwesenheitsliste.setShort(new Short((short) 1));
		} else {
			wcbKeineAnzeigeInAnwesenheitsliste.setShort(new Short((short) 0));
		}

		wcbAnwesenheitslisteTermial.setShort(personalDto
				.getBAnwesenheitTerminal());
		wcbAnwesenheitslisteAlleTermial.setShort(personalDto
				.getBAnwesenheitalleterminal());

		wdfGeburtsdatum.setTimestamp(personalDto.getTGeburtsdatum());

		wtfUnterschriftsfunktion.setText(personalDto
				.getCUnterschriftsfunktion());
		wtfUnterschriftstext.setText(personalDto.getCUnterschriftstext());

		wtfImapBenutzer.setText(personalDto.getCImapbenutzer());

		wtfImapKennwort.setText("**********");

		wtfDirektfax.setText(personalDto.getCDirektfax());

		wtfEmail.setEmail(personalDto.getCEmail(), null);

		wtfFaxdurchwahl.setText(personalDto.getCFax());

		if (personalDto.getCHandy() != null) {
			wtfHandy.setPartnerKommunikationDto(personalDto.getPartnerDto(),
					personalDto.getCHandy());
		} else {
			wtfHandy.setPartnerKommunikationDto(null, null);
		}

		wtfDurchwahl.setText(personalDto.getCTelefon());

		wefSignatur.setText(DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.getSignatur(personalDto.getIId(),
						LPMain.getTheClient().getLocUiAsString()));

		this.setStatusbarPersonalIIdAendern(personalDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(personalDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(personalDto.getTAnlegen());
		this.setStatusbarTAendern(personalDto.getTAendern());
	}

	void dialogQuerySozialversichererFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRSozialversicherer = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame(),
						personalDto.getPartnerIIdSozialversicherer(), true);

		new DialogQuery(panelQueryFLRSozialversicherer);
	}

	void dialogQueryFirmenzugehoerigkeitFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRfirmenzugehoerigkeit = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame(),
						personalDto.getPartnerIIdFirma(), true);
		new DialogQuery(panelQueryFLRfirmenzugehoerigkeit);
	}

	void dialogQueryGeburtsortFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRGeburtsort = SystemFilterFactory.getInstance()
				.createPanelFLRLandplzort(getInternalFrame(),
						personalDto.getLandplzortIIdGeburt(), true);

		new DialogQuery(panelQueryFLRGeburtsort);
	}

	void dialogQueryRelgionFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRReligion = PersonalFilterFactory.getInstance()
				.createPanelFLRReligion(getInternalFrame(),
						personalDto.getReligionIId());

		new DialogQuery(panelQueryFLRReligion);
	}

	void dialogQueryBerufFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRBeruf = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_BERUF, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.berufauswahlliste"));
		panelQueryFLRBeruf.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDBezeichnung(), null);
		panelQueryFLRBeruf.setSelectedId(personalDto.getBerufIId());
		new DialogQuery(panelQueryFLRBeruf);
	}

	void dialogQueryLohngruppeFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRLohngrupe = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_LOHNGRUPPE, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.lohngruppeauswahlliste"));
		panelQueryFLRLohngrupe.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

		panelQueryFLRLohngrupe.setSelectedId(personalDto.getLohngruppeIId());

		new DialogQuery(panelQueryFLRLohngrupe);
	}

	void dialogQueryKollektivFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRKollektiv = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_KOLLEKTIV, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.kollektivauswahlliste"));
		panelQueryFLRKollektiv.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDBezeichnung(), null);
		panelQueryFLRKollektiv.setSelectedId(personalDto.getKollektivIId());

		new DialogQuery(panelQueryFLRKollektiv);
	}

	void dialogQueryPendlerpauschaleFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRPendlerpauschale = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_PENDLERPAUSCHALE, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr(
								"title.pendlerpauschaleauswahlliste"));
		panelQueryFLRPendlerpauschale.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

		panelQueryFLRPendlerpauschale.setSelectedId(personalDto
				.getKollektivIId());

		new DialogQuery(panelQueryFLRPendlerpauschale);
	}

	void dialogQueryStaatsangehoerigkeitFromListe(ActionEvent e)
			throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRStaatsangehoerigkeit = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_LAND, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.landauswahlliste"));

		panelQueryFLRStaatsangehoerigkeit.setSelectedId(personalDto
				.getLandIIdStaatsangehoerigkeit());

		new DialogQuery(panelQueryFLRStaatsangehoerigkeit);
	}

	protected void components2Dto() throws Throwable {

		personalDto.setCSozialversnr(wtfsozialversicherungsnummer.getText());
		personalDto.setFamilienstandCNr((String) wcbFamilienstand
				.getKeyOfSelectedItem());
		personalDto.setBUeberstundenausbezahlt(wcbBekommtUeberstundenausbezahlt
				.getShort());

		personalDto.setCUnterschriftsfunktion(wtfUnterschriftsfunktion
				.getText());
		personalDto.setCUnterschriftstext(wtfUnterschriftstext.getText());

		personalDto.setCImapbenutzer(wtfImapBenutzer.getText());
		if (!new String(wtfImapKennwort.getPassword()).equals("**********")) {
			personalDto.setCImapkennwort(new String(wtfImapKennwort
					.getPassword()));
		}

		if (wcbKeineAnzeigeInAnwesenheitsliste.getShort().intValue() == 0) {
			personalDto.setBAnwesenheitsliste(new Short((short) 1));
		} else {
			personalDto.setBAnwesenheitsliste(new Short((short) 0));
		}
		personalDto.setBAnwesenheitTerminal(wcbAnwesenheitslisteTermial
				.getShort());
		personalDto.setBAnwesenheitalleterminal(wcbAnwesenheitslisteAlleTermial
				.getShort());
		personalDto.setTGeburtsdatum(wdfGeburtsdatum.getTimestamp());

		// Kommunikationsdaten

		personalDto.setCDirektfax(wtfDirektfax.getText());

		personalDto.setCTelefon(wtfDurchwahl.getText());

		personalDto.setCEmail(wtfEmail.getText());

		personalDto.setCFax(wtfFaxdurchwahl.getText());

		personalDto.setCHandy(wtfHandy.getText());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRBeruf) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				BerufDto berufDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.berufFindByPrimaryKey((Integer) key);
				wtfBeruf.setText(berufDto.getCBez());
				personalDto.setBerufIId(berufDto.getIId());
			} else if (e.getSource() == panelQueryFLRfirmenzugehoerigkeit) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				PartnerDto partnerTempDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey((Integer) key);
				if (partnerTempDto.getIId().equals(
						internalFramePersonal.getPersonalDto().getPartnerDto()
								.getIId())) {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.error"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"pers.error.kannnichtselbstzugeordnetwerden"));
				} else {
					wtfFirmenzugehoerigkeit.setText(partnerTempDto
							.formatAnrede());
					personalDto.setPartnerIIdFirma(partnerTempDto.getIId());
				}
			} else if (e.getSource() == panelQueryFLRGeburtsort) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LandplzortDto landplzortDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.landplzortFindByPrimaryKey((Integer) key);
				wtfGeburtsort.setText(landplzortDto.formatLandPlzOrt());
				personalDto.setLandplzortIIdGeburt(landplzortDto.getIId());
			} else if (e.getSource() == panelQueryFLRKollektiv) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				KollektivDto kollektivDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.kollektivFindByPrimaryKey((Integer) key);
				wtfKollektiv.setText(kollektivDto.getCBez());
				personalDto.setKollektivIId(kollektivDto.getIId());
			} else if (e.getSource() == panelQueryFLRLohngrupe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LohngruppeDto lohngruppeDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.lohngruppeFindByPrimaryKey((Integer) key);
				wtfLohngruppe.setText(lohngruppeDto.getCBez());
				personalDto.setLohngruppeIId(lohngruppeDto.getIId());
			} else if (e.getSource() == panelQueryFLRPendlerpauschale) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				PendlerpauschaleDto pendlerpauschaleDto = DelegateFactory
						.getInstance().getPersonalDelegate()
						.pendlerpauschaleFindByPrimaryKey((Integer) key);
				wtfPendlerpauschale.setText(pendlerpauschaleDto.getCBez());
				personalDto
						.setPendlerpauschaleIId(pendlerpauschaleDto.getIId());
			} else if (e.getSource() == panelQueryFLRReligion) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ReligionDto religionDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.religionFindByPrimaryKey((Integer) key);
				wtfReligion.setText(religionDto.getCNr());
				personalDto.setReligionIId(religionDto.getIId());
			} else if (e.getSource() == panelQueryFLRSozialversicherer) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey((Integer) key);
				wtfSozialversicherer.setText(partnerDto.formatAnrede());
				personalDto.setPartnerIIdSozialversicherer(partnerDto.getIId());
			} else if (e.getSource() == panelQueryFLRStaatsangehoerigkeit) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LandDto landDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.landFindByPrimaryKey((Integer) key);
				wtfStaatsangehoerigkeit.setText(landDto.getCLkz());
				personalDto.setLandIIdStaatsangehoerigkeit(landDto.getIID());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRBeruf) {
				wtfBeruf.setText(null);
				personalDto.setBerufDto(null);
				personalDto.setBerufIId(null);
			} else if (e.getSource() == panelQueryFLRfirmenzugehoerigkeit) {
				wtfFirmenzugehoerigkeit.setText(null);
				personalDto.setPartnerIIdFirma(null);
				personalDto.setPartnerDto_Firma(null);
			} else if (e.getSource() == panelQueryFLRGeburtsort) {
				wtfGeburtsort.setText(null);
				personalDto.setLandplzortDto_Geburtsort(null);
				personalDto.setLandplzortIIdGeburt(null);
			} else if (e.getSource() == panelQueryFLRKollektiv) {
				wtfKollektiv.setText(null);
				personalDto.setKollektivDto(null);
				personalDto.setKollektivIId(null);
			} else if (e.getSource() == panelQueryFLRLohngrupe) {
				wtfLohngruppe.setText(null);
				personalDto.setLohngruppeDto(null);
				personalDto.setLohngruppeIId(null);
			} else if (e.getSource() == panelQueryFLRPendlerpauschale) {
				wtfPendlerpauschale.setText(null);
				personalDto.setPendlerpauschaleDto(null);
				personalDto.setPendlerpauschaleIId(null);
			} else if (e.getSource() == panelQueryFLRReligion) {
				wtfReligion.setText(null);
				personalDto.setReligionDto(null);
				personalDto.setReligionIId(null);
			} else if (e.getSource() == panelQueryFLRSozialversicherer) {
				wtfSozialversicherer.setText(null);
				personalDto.setPartnerDto_Sozialversicherer(null);
				personalDto.setPartnerIIdSozialversicherer(null);
			} else if (e.getSource() == panelQueryFLRStaatsangehoerigkeit) {
				wtfStaatsangehoerigkeit.setText(null);
				personalDto.setLandDto(null);
				personalDto.setLandIIdStaatsangehoerigkeit(null);
			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();
			DelegateFactory.getInstance().getPersonalDelegate()
					.updatePersonal(personalDto);
			DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.updateSignatur(personalDto.getIId(), wefSignatur.getText());
		}
		super.eventActionSave(e, true);
	}

}
