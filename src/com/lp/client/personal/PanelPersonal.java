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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperBildField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperEmailField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.component.WrapperURLField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.PersonalgruppeDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelPersonal extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersonalDto personalDto = null;
	private InternalFramePersonal internalFramePersonal = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private PanelQueryFLR panelQueryFLRKostenstelleAbteilung = null;
	private PanelQueryFLR panelQueryFLROrt = null;
	private PanelQueryFLR panelQueryFLRHeimatKostenstelle = null;
	private PanelQueryFLR panelQueryFLRPartner = null;
	private PanelQueryFLR panelQueryFLRPersonalgruppe = null;
	private ButtonGroup buttonGroup1 = new ButtonGroup();
	private WrapperGotoButton wbuPartner = new WrapperGotoButton(
			WrapperGotoButton.GOTO_PARTNER_AUSWAHL);
	private WrapperTextField wtfNachname = new WrapperTextField();
	private WrapperRadioButton wrbMaennlich = new WrapperRadioButton();
	private WrapperRadioButton wrbWeiblich = new WrapperRadioButton();
	private WrapperLabel wlaVorname2 = new WrapperLabel();
	private WrapperLabel wlaVorname1 = new WrapperLabel();
	private WrapperTextField wtfVorname1 = new WrapperTextField();
	private WrapperTextField wtfVorname2 = new WrapperTextField();
	private WrapperLabel wlaKurzkennung = new WrapperLabel();
	private WrapperTextField wtfKurzkennung = new WrapperTextField();
	private WrapperLabel wlaStrasse = new WrapperLabel();
	private WrapperTextField wtfStrasse = new WrapperTextField();
	private WrapperButton wbuOrt = new WrapperButton();
	private WrapperTextField wtfOrt = new WrapperTextField();
	private WrapperLabel wlaPersonalart = new WrapperLabel();
	private WrapperComboBox wcoPersonalart = new WrapperComboBox();
	private WrapperLabel wlaPersonalfunktion = new WrapperLabel();
	private WrapperComboBox wcoPersonalfunktion = new WrapperComboBox();
	private WrapperTextField wtfStammkostenstelle = new WrapperTextField();
	private WrapperTextField wtfAbteilung = new WrapperTextField();
	private WrapperButton wbuAbteilung = new WrapperButton();
	private WrapperButton wbuStammkostenstelle = new WrapperButton();
	private WrapperLabel wlaGeschlecht = new WrapperLabel();
	private WrapperLabel wlaPersonalnummer = new WrapperLabel();
	private WrapperTextField wnfPersonalnummer = new WrapperTextField();
	private WrapperLabel wlaKurzzeichen = new WrapperLabel();
	private WrapperTextField wtfKurzzeichen = new WrapperTextField();
	private WrapperLabel wlaAusweis = new WrapperLabel();
	private WrapperTextField wtfAusweis = new WrapperTextField();

	private WrapperButton wbuPersonalgruppe = new WrapperButton();
	private WrapperTextField wtfPersonalgruppe = new WrapperTextField();

	private WrapperBildField wmcBild = new WrapperBildField(getInternalFrame(),
			"");

	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();

	static final public String ACTION_SPECIAL_ORT_FROM_LISTE = "action_ort_from_liste";
	static final public String ACTION_SPECIAL_KOSTENSTELLE_ABTEILUNG_FROM_LISTE = "action_kostenstelle_from_liste";
	static final public String ACTION_SPECIAL_HEIMATKOSTENSTELLE_FROM_LISTE = "action_heimatkostenstelle_from_liste";
	static final public String ACTION_SPECIAL_PARTNER_FROM_LISTE = "action_partner_from_liste";
	static final public String ACTION_SPECIAL_PERSONALGRUPPE_FROM_LISTE = "action_personalgruppe_from_liste";
	private WrapperLabel wlaTitel = new WrapperLabel();
	private WrapperTextField wtfTitel = new WrapperTextField();
	private WrapperLabel wlaTelefon = new WrapperLabel();
	private WrapperLabel wlaEmail = new WrapperLabel();
	private WrapperLabel wlaFax = new WrapperLabel();
	private WrapperLabel wlaHomepage = new WrapperLabel();
	private WrapperLabel wlaHandy = new WrapperLabel();
	private WrapperTelefonField wtfTelefon = new WrapperTelefonField();
	private WrapperEmailField wtfEmail = new WrapperEmailField();
	private WrapperTextField wtfFax = new WrapperTextField();
	private WrapperURLField wtfHomepage = new WrapperURLField();
	private WrapperTelefonField wtfHandy = new WrapperTelefonField();
	
	protected WrapperTextField wtfNtitel = null;
	protected WrapperLabel wlaNtitel = null;
	

	public InternalFramePersonal getInternalFramePersonal() {
		return internalFramePersonal;
	}

	public PanelPersonal(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfPersonalnummer;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getInternalFramePersonal().getPersonalDto().getIId();

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			personalDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							getInternalFramePersonal().getPersonalDto()
									.getIId());

			dto2Components();
			getInternalFramePersonal().getTabbedPanePersonal().refreshTitle();
		} else {
			leereAlleFelder(this);
			wnfPersonalnummer.setText(DelegateFactory.getInstance()
					.getPersonalDelegate().getNextPersonalnummer());
			try {
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_DEFAULT_KOSTENSTELLE,
								ParameterFac.KATEGORIE_ALLGEMEIN,
								LPMain.getInstance().getTheClient()
										.getMandant());
				KostenstelleDto dto = DelegateFactory
						.getInstance()
						.getSystemDelegate()
						.kostenstelleFindByNummerMandant(
								parameter.getCWert(),
								LPMain.getInstance().getTheClient()
										.getMandant());

				personalDto.setKostenstelleIIdStamm(dto.getIId());
				wtfStammkostenstelle.setText(dto.getCNr());
			} catch (Throwable ex) {
				// Kein Mandantparameter vorhanden;
			}

		}

	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {
		wnfPersonalnummer.setText(personalDto.getCPersonalnr());
		wtfAusweis.setText(personalDto.getCAusweis());
		wtfNachname.setText(personalDto.getPartnerDto()
				.getCName1nachnamefirmazeile1());
		wtfTitel.setText(personalDto.getPartnerDto().getCTitel());
		wtfNtitel.setText(personalDto.getPartnerDto().getCNtitel());
		wtfVorname1.setText(personalDto.getPartnerDto()
				.getCName2vornamefirmazeile2());
		wtfVorname2.setText(personalDto.getPartnerDto()
				.getCName3vorname2abteilung());
		wtfStrasse.setText(personalDto.getPartnerDto().getCStrasse());
		wcbVersteckt.setShort(personalDto.getBVersteckt());

		// Goto Key setzen
		wbuPartner.setOKey(personalDto.getPartnerDto().getIId());

		// if(personalDto.getPartnerDto().getOBild()!=null){

		wmcBild.setImage(personalDto.getPartnerDto().getOBild());

		// }
		wtfKurzzeichen.setText(personalDto.getCKurzzeichen());
		wtfKurzkennung.setText(personalDto.getPartnerDto().getCKbez());
		if (Helper.short2boolean(personalDto.getBMaennlich())) {
			wrbMaennlich.setSelected(true);
		} else {
			wrbWeiblich.setSelected(true);
		}

		if (personalDto.getPartnerDto().getLandplzortDto() != null) {
			wtfOrt.setText(personalDto.getPartnerDto().getLandplzortDto()
					.formatLandPlzOrt());
		} else {
			wtfOrt.setText(null);
		}
		if (personalDto.getPersonalgruppeIId() != null) {

			PersonalgruppeDto personalgruppeDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalgruppeFindByPrimaryKey(
							personalDto.getPersonalgruppeIId());
			wtfPersonalgruppe.setText(personalgruppeDto.getCBez());
		} else {
			wtfPersonalgruppe.setText(null);
		}

		wcoPersonalart.setKeyOfSelectedItem(personalDto.getPersonalartCNr());
		wcoPersonalfunktion.setKeyOfSelectedItem(personalDto
				.getPersonalfunktionCNr());
		if (personalDto.getKostenstelleDto_Abteilung() != null) {
			wtfAbteilung.setText(personalDto.getKostenstelleDto_Abteilung()
					.formatKostenstellenbezeichnung());
		} else {
			wtfAbteilung.setText(null);
		}
		if (personalDto.getKostenstelleDto_Stamm() != null) {
			wtfStammkostenstelle.setText(personalDto.getKostenstelleDto_Stamm()
					.formatKostenstellenbezeichnung());
		} else {
			wtfStammkostenstelle.setText(null);
		}

		// Partnerkommunikation
		if (personalDto.getPartnerDto().getCHandy() != null) {
			wtfHandy.setPartnerKommunikationDto(personalDto.getPartnerDto(),
					personalDto.getPartnerDto().getCHandy());
		} else {
			wtfHandy.setPartnerKommunikationDto(null, null);
		}

		wtfEmail.setEmail(personalDto.getPartnerDto().getCEmail(), null);

		wtfFax.setText(personalDto.getPartnerDto().getCFax());

		wtfHomepage.setText(personalDto.getPartnerDto().getCHomepage());

		if (personalDto.getPartnerDto().getCTelefon() != null) {
			wtfTelefon.setPartnerKommunikationDto(personalDto.getPartnerDto(),
					personalDto.getPartnerDto().getCTelefon());
		} else {
			wtfTelefon.setPartnerKommunikationDto(null, null);
		}

		this.setStatusbarPersonalIIdAendern(personalDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(personalDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(personalDto.getTAnlegen());
		this.setStatusbarTAendern(personalDto.getTAendern());

	}

	void dialogQueryPartnerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRPartner = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame(),
						personalDto.getPartnerIId(), false);
		new DialogQuery(panelQueryFLRPartner);
	}

	void dialogQueryKostenstelleAbteilungFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRKostenstelleAbteilung = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, true,
						personalDto.getKostenstelleIIdAbteilung());
		new DialogQuery(panelQueryFLRKostenstelleAbteilung);
	}

	void dialogQueryKostenstelleHeimatkostenstelleFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRHeimatKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, true,
						personalDto.getKostenstelleIIdStamm());
		new DialogQuery(panelQueryFLRHeimatKostenstelle);

	}

	void dialogQueryPersonalgruppeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRPersonalgruppe = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonalgruppe(getInternalFrame(),
						personalDto.getPersonalgruppeIId(), true);
		new DialogQuery(panelQueryFLRPersonalgruppe);

	}

	void dialogQueryOrtFromListe(ActionEvent e) throws Throwable {

		Integer selectedId = null;
		if (personalDto.getPartnerDto() != null) {
			selectedId = personalDto.getPartnerDto().getLandplzortIId();
		}

		panelQueryFLROrt = SystemFilterFactory.getInstance()
				.createPanelFLRLandplzort(getInternalFrame(), selectedId, true);

		new DialogQuery(panelQueryFLROrt);
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
		wbuPartner.setText(LPMain.getInstance().getTextRespectUISPr("lp.name")
				+ "...");
		wrbMaennlich.setSelected(true);
		wrbMaennlich.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.maennlich"));
		wrbWeiblich.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.weiblich"));
		wlaVorname2.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.vorname")
				+ " 2");
		wlaVorname1.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.vorname")
				+ " 1");
		wlaKurzkennung.setText(LPMain.getInstance().getTextRespectUISPr(
				"personal.kurzbez"));
		wlaStrasse.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.strasse"));
		wbuOrt.setText(LPMain.getInstance().getTextRespectUISPr("lp.label.ort")
				+ "...");
		wcbVersteckt.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.versteckt"));
		
		wlaNtitel = new WrapperLabel();
		wlaNtitel
				.setText(LPMain.getInstance().getTextRespectUISPr("lp.ntitel"));
		wtfNtitel = new WrapperTextField(PartnerFac.MAX_TITEL);
		wtfNtitel.setText("");

		wtfNachname.setMandatoryField(true);
		wtfNachname.setColumnsMax(PartnerFac.MAX_NAME);
		wtfVorname1.setColumnsMax(PartnerFac.MAX_NAME);
		wtfVorname2.setColumnsMax(PartnerFac.MAX_NAME);
		wtfNachname.setMandatoryFieldDB(true);
		wtfNachname
				.addFocusListener(new PanelPersonal_wtfNachname_focusAdapter(
						this));
		wlaPersonalart.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personal.personalart"));
		wcoPersonalart.setMandatoryFieldDB(true);
		wcoPersonalart.setMandatoryField(true);
		wlaPersonalfunktion.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personal.personalfunktion"));
		wtfStammkostenstelle.setActivatable(false);
		wtfStammkostenstelle.setText("");
		wtfStammkostenstelle.setColumnsMax(80);
		wtfStammkostenstelle.setMandatoryField(true);

		wtfAbteilung.setActivatable(false);
		wtfAbteilung.setText("");
		wtfAbteilung.setColumnsMax(80);
		wbuAbteilung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abteilung")
				+ "...");
		wbuAbteilung
				.setActionCommand(PanelPersonal.ACTION_SPECIAL_KOSTENSTELLE_ABTEILUNG_FROM_LISTE);
		wbuAbteilung.addActionListener(this);
		wbuStammkostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personal.heimatkostenstelle")
				+ "...");
		wbuStammkostenstelle
				.setActionCommand(PanelPersonal.ACTION_SPECIAL_HEIMATKOSTENSTELLE_FROM_LISTE);
		wbuStammkostenstelle.addActionListener(this);
		wlaGeschlecht.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.geschlecht"));
		wlaPersonalnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personal.personalnummer"));

		wbuPersonalgruppe = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("pers.personalgruppe") + "...");

		wbuPersonalgruppe
				.setActionCommand(PanelPersonal.ACTION_SPECIAL_PERSONALGRUPPE_FROM_LISTE);
		wbuPersonalgruppe.addActionListener(this);

		wtfPersonalgruppe = new WrapperTextField();
		wtfPersonalgruppe.setActivatable(false);

		wlaKurzzeichen.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personal.kurzzeichen"));
		wlaAusweis.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personal.ausweis"));

		wbuOrt.setActionCommand(PanelPersonal.ACTION_SPECIAL_ORT_FROM_LISTE);
		wbuOrt.addActionListener(this);
		wbuPartner
				.setActionCommand(PanelPersonal.ACTION_SPECIAL_PARTNER_FROM_LISTE);
		wbuPartner.addActionListener(this);

		// wnfPersonalnummer.setFractionDigits(0);
		wnfPersonalnummer.setMandatoryField(true);
	
		wtfKurzzeichen.setColumnsMax(PersonalFac.MAX_PERSONAL_KURZZEICHEN);
		wtfOrt.setActivatable(false);
		wtfOrt.setColumnsMax(80);
		wlaTitel.setRequestFocusEnabled(true);
		wlaTitel.setText(LPMain.getInstance().getTextRespectUISPr("lp.titel"));
		wtfTitel.setText("");
		wtfTitel.setColumnsMax(PartnerFac.MAX_TITEL);
		wtfTelefon.setName("wtfTelefon");
		wlaTelefon.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.telefon"));
		wlaEmail.setText(LPMain.getInstance().getTextRespectUISPr("lp.email"));
		wlaFax.setText(LPMain.getInstance().getTextRespectUISPr("lp.fax"));
		wlaHomepage.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.homepage"));

		wlaHandy.setText(LPMain.getInstance().getTextRespectUISPr("lp.handy"));

		wtfFax.setText("");
		wtfFax.setColumnsMax(80);
		wtfKurzkennung.setColumnsMax(25);
		wtfKurzkennung.setMandatoryField(true);
		wtfAusweis.setColumnsMax(PersonalFac.MAX_PERSONAL_AUSWEIS);
		wtfAusweis.setUppercaseField(true);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		buttonGroup1.add(wrbWeiblich);
		buttonGroup1.add(wrbMaennlich);
		jpaWorkingOn.add(wlaVorname1, new GridBagConstraints(0, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaVorname2, new GridBagConstraints(0, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaStrasse, new GridBagConstraints(0, 8, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfNachname, new GridBagConstraints(1, 2, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVorname1, new GridBagConstraints(1, 5, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVorname2, new GridBagConstraints(1, 6, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfStrasse, new GridBagConstraints(1, 8, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuPartner, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfStammkostenstelle, new GridBagConstraints(1, 12, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuStammkostenstelle, new GridBagConstraints(0, 12, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuAbteilung, new GridBagConstraints(3, 12, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAbteilung, new GridBagConstraints(4, 12, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuOrt, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfOrt, new GridBagConstraints(1, 10, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaPersonalart, new GridBagConstraints(3, 7, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wcoPersonalart, new GridBagConstraints(4, 7, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuPersonalgruppe, new GridBagConstraints(3, 8, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfPersonalgruppe, new GridBagConstraints(4, 8, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaPersonalfunktion, new GridBagConstraints(3, 10, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoPersonalfunktion, new GridBagConstraints(4, 10, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaGeschlecht, new GridBagConstraints(0, 7, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), -1, 0));
		jpaWorkingOn.add(wrbMaennlich, new GridBagConstraints(1, 7, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 70, 0));
		jpaWorkingOn.add(wrbWeiblich, new GridBagConstraints(1, 7, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 70, 0));
		jpaWorkingOn.add(wnfPersonalnummer, new GridBagConstraints(1, 0, 1, 1,
				0.07, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKurzzeichen, new GridBagConstraints(3, 0, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaPersonalnummer, new GridBagConstraints(0, 0, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKurzzeichen, new GridBagConstraints(4, 0, 1, 1,
				0.07, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAusweis, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAusweis, new GridBagConstraints(1, 1, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKurzkennung, new GridBagConstraints(0, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKurzkennung, new GridBagConstraints(1, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbVersteckt,
					new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		jpaWorkingOn.add(wlaTitel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfTitel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
		
		jpaWorkingOn.add(wlaNtitel, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfNtitel, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wlaTelefon, new GridBagConstraints(0, 14, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaFax, new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaHandy, new GridBagConstraints(0, 16, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfTelefon, new GridBagConstraints(1, 14, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfFax, new GridBagConstraints(1, 15, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfHandy, new GridBagConstraints(1, 16, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaEmail, new GridBagConstraints(3, 14, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfEmail, new GridBagConstraints(4, 14, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaHomepage, new GridBagConstraints(3, 15, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfHomepage, new GridBagConstraints(4, 15, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wmcBild, new GridBagConstraints(0, 17, 5, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		personalDto = new PersonalDto();
		getInternalFramePersonal().setPersonalDto(personalDto);
		personalDto.setPartnerDto(new PartnerDto());
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_KOSTENSTELLE_ABTEILUNG_FROM_LISTE)) {
			dialogQueryKostenstelleAbteilungFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ORT_FROM_LISTE)) {
			dialogQueryOrtFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PARTNER_FROM_LISTE)) {
			dialogQueryPartnerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_HEIMATKOSTENSTELLE_FROM_LISTE)) {
			dialogQueryKostenstelleHeimatkostenstelleFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONALGRUPPE_FROM_LISTE)) {
			dialogQueryPersonalgruppeFromListe(e);
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

	protected void setDefaults() throws Throwable {
		wcoPersonalart.setMap(DelegateFactory.getInstance()
				.getPersonalDelegate().getAllSprPersonalarten());
		wcoPersonalfunktion.setMap(DelegateFactory.getInstance()
				.getPersonalDelegate().getAllSprPersonalfunktionen());
	}

	protected void components2Dto() throws Throwable {
		personalDto.setPersonalartCNr((String) wcoPersonalart
				.getKeyOfSelectedItem());
		personalDto.setPersonalfunktionCNr((String) wcoPersonalfunktion
				.getKeyOfSelectedItem());
		personalDto.setCPersonalnr(wnfPersonalnummer.getText());
		personalDto.setCKurzzeichen(wtfKurzzeichen.getText());
		personalDto.setCAusweis(wtfAusweis.getText());
		personalDto.setBVersteckt(wcbVersteckt.getShort());

		if (wrbMaennlich.isSelected()) {
			personalDto.setBMaennlich(Helper.boolean2Short(true));
		} else {
			personalDto.setBMaennlich(Helper.boolean2Short(false));
		}

		personalDto.getPartnerDto().setCName1nachnamefirmazeile1(
				wtfNachname.getText());
		personalDto.getPartnerDto().setCName2vornamefirmazeile2(
				wtfVorname1.getText());
		personalDto.getPartnerDto().setCName3vorname2abteilung(
				wtfVorname2.getText());
		personalDto.getPartnerDto().setCStrasse(wtfStrasse.getText());
		personalDto.getPartnerDto().setCKbez(wtfKurzkennung.getText());
		personalDto.getPartnerDto().setBVersteckt(new Short((short) 0));
		personalDto.getPartnerDto().setCTitel(wtfTitel.getText());
		personalDto.getPartnerDto().setCNtitel(wtfNtitel.getText());

		personalDto.getPartnerDto().setOBild(
				Helper.imageToByteArray((BufferedImage) wmcBild.getImage()));

		// Kommunikationsdaten

		personalDto.getPartnerDto().setCHandy(wtfHandy.getText());

		personalDto.getPartnerDto().setCTelefon(wtfTelefon.getText());

		personalDto.getPartnerDto().setCEmail(wtfEmail.getText());

		personalDto.getPartnerDto().setCFax(wtfFax.getText());

		personalDto.getPartnerDto().setCHomepage(wtfHomepage.getText());

	}

	public static PartnerkommunikationDto erzeugePartnerkommunikationDto(
			PartnerkommunikationDto partnerkommunikationDto,
			String sPartnerkommBez, String sPartnerkommInhalt,
			Integer iIdPartner) throws Throwable {

		if (partnerkommunikationDto == null) {
			partnerkommunikationDto = new PartnerkommunikationDto();
			partnerkommunikationDto.setCNrMandant(LPMain.getInstance()
					.getTheClient().getMandant());
		}
		partnerkommunikationDto.setKommunikationsartCNr(sPartnerkommBez);
		partnerkommunikationDto.setCInhalt(sPartnerkommInhalt);
		partnerkommunikationDto.setPartnerIId(iIdPartner);
		partnerkommunikationDto.setCBez(sPartnerkommBez);
		return partnerkommunikationDto;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		Object[] options = {
				LPMain.getInstance().getTextRespectUISPr("lp.ja"),
				LPMain.getInstance().getTextRespectUISPr(
						"pers.nurpersonalloeschen") };

		int iOption = DialogFactory.showModalDialog(
				getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr(
						"pers.frage.zugehoerigerpartnerloeschen"), "", options,
				options[1]);

		if (iOption == JOptionPane.YES_OPTION) {
			DelegateFactory.getInstance().getPersonalDelegate()
					.removePersonal(personalDto);
			try {
				DelegateFactory.getInstance().getPartnerDelegate()
						.removePartner(personalDto.getPartnerDto());
			} catch (ExceptionLP ex) {
				if (ex.getICode() == EJBExceptionLP.FEHLER_BEIM_LOESCHEN) {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.error"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"part.zugehoerigerpartnerkonntenichtgeloeschtwerden"));
				}
			}
		} else if (iOption == JOptionPane.NO_OPTION) {
			DelegateFactory.getInstance().getPersonalDelegate()
					.removePersonal(personalDto);
		}

		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (personalDto.getIId() == null) {
				personalDto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate().createPersonal(personalDto));
				setKeyWhenDetailPanel(personalDto.getIId());

				personalDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(personalDto.getIId());

				internalFramePersonal.setPersonalDto(personalDto);
			} else {
				try {
					DelegateFactory.getInstance().getPersonalDelegate()
							.updatePersonal(personalDto);

				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_PERSONAL_DUPLICATE_AUSWEIS) {
						String msg = LPMain.getInstance().getTextRespectUISPr(
								"pers.error.doppelteausweisnummern");

						PersonalDto personalDtoTemp = DelegateFactory
								.getInstance()
								.getPersonalDelegate()
								.personalFindByCAusweis(
										personalDto.getCAusweis());
						if (personalDtoTemp.getMandantCNr().equals(
								LPMain.getInstance().getTheClient()
										.getMandant())) {
							PartnerDto partnerDtoTemp = DelegateFactory
									.getInstance()
									.getPartnerDelegate()
									.partnerFindByPrimaryKey(
											personalDtoTemp.getPartnerIId());
							msg += " (Person: "
									+ partnerDtoTemp.formatFixTitelName1Name2()
									+ ")";
						} else {
							msg += " (Bei Mandant "
									+ personalDtoTemp.getMandantCNr() + ")";
						}

						DialogFactory.showModalDialog(LPMain.getInstance()
								.getTextRespectUISPr("lp.error"), msg);
					}

					else {
						handleException(ex, false);
					}
				}

			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						personalDto.getIId().toString());
			}

			eventYouAreSelected(false);

			personalDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(personalDto.getIId());
			Defaults.getInstance().reloadPersonalKurzzeichen();
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLROrt) {
				Integer keyLandPLZOrt = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LandplzortDto landPLZOrtDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.landplzortFindByPrimaryKey((Integer) keyLandPLZOrt);

				personalDto.getPartnerDto().setLandplzortDto(landPLZOrtDto);
				personalDto.getPartnerDto().setLandplzortIId(keyLandPLZOrt);

				wtfOrt.setText(landPLZOrtDto.formatLandPlzOrt());
			} else if (e.getSource() == panelQueryFLRPartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey((Integer) key);
				wtfNachname.setText(partnerDto.getCName1nachnamefirmazeile1());
				
				wtfVorname1.setText(partnerDto.getCName2vornamefirmazeile2());
				

				wtfVorname2.setText(partnerDto.getCName3vorname2abteilung());
				

				wtfTitel.setText(partnerDto.getCTitel());
				wtfNtitel.setText(partnerDto.getCNtitel());
				

				if (partnerDto.getLandplzortDto() != null) {
					wtfOrt.setText(partnerDto.getLandplzortDto()
							.formatLandPlzOrt());
				} else {
					wtfOrt.setText(null);
				}
				personalDto.setPartnerDto(partnerDto);
				personalDto.setPartnerIId(partnerDto.getIId());

				wtfStrasse.setText(partnerDto.getCStrasse());

				// Partnerkommunikation
				if (personalDto.getPartnerDto().getCHandy() != null) {
					wtfHandy.setPartnerKommunikationDto(personalDto
							.getPartnerDto(), personalDto.getPartnerDto()
							.getCHandy());
				} else {
					wtfHandy.setPartnerKommunikationDto(null, null);
				}

				wtfEmail.setEmail(personalDto.getPartnerDto().getCEmail(), null);

				wtfFax.setText(personalDto.getPartnerDto().getCFax());

				wtfHomepage.setText(personalDto.getPartnerDto().getCHomepage());

				if (personalDto.getPartnerDto().getCTelefon() != null) {
					wtfTelefon.setPartnerKommunikationDto(personalDto
							.getPartnerDto(), personalDto.getPartnerDto()
							.getCTelefon());
				} else {
					wtfTelefon.setPartnerKommunikationDto(null, null);
				}

			} else if (e.getSource() == panelQueryFLRKostenstelleAbteilung) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				personalDto.setKostenstelleIIdAbteilung(key);

				KostenstelleDto kostenstelleDto = null;
				kostenstelleDto = DelegateFactory.getInstance()
						.getSystemDelegate().kostenstelleFindByPrimaryKey(key);
				wtfAbteilung.setText(kostenstelleDto
						.formatKostenstellenbezeichnung());
			} else if (e.getSource() == panelQueryFLRHeimatKostenstelle) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				personalDto.setKostenstelleIIdStamm(key);

				KostenstelleDto kostenstelleDto = null;
				kostenstelleDto = DelegateFactory.getInstance()
						.getSystemDelegate().kostenstelleFindByPrimaryKey(key);
				wtfStammkostenstelle.setText(kostenstelleDto
						.formatKostenstellenbezeichnung());
			} else if (e.getSource() == panelQueryFLRPersonalgruppe) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				personalDto.setPersonalgruppeIId(key);

				PersonalgruppeDto personalgruppeDto = null;
				personalgruppeDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.personalgruppeFindByPrimaryKey(key);
				wtfPersonalgruppe.setText(personalgruppeDto.getCBez());
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRHeimatKostenstelle) {

				wtfStammkostenstelle.setText(null);
				personalDto.setKostenstelleIIdStamm(null);
				personalDto.setKostenstelleDto_Stamm(null);
			} else if (e.getSource() == panelQueryFLRKostenstelleAbteilung) {

				wtfAbteilung.setText(null);
				personalDto.setKostenstelleIIdAbteilung(null);
				personalDto.setKostenstelleDto_Abteilung(null);
			} else if (e.getSource() == panelQueryFLROrt) {
				wtfOrt.setText(null);
				personalDto.getPartnerDto().setLandplzortIId(null);
				personalDto.getPartnerDto().setLandplzortDto(null);
			} else if (e.getSource() == panelQueryFLRPersonalgruppe) {
				wtfPersonalgruppe.setText(null);
				personalDto.setPersonalgruppeIId(null);
			} else if (e.getSource() == panelQueryFLRPartner) {
				wtfNachname.setText(null);
				wtfVorname1.setText(null);
				wtfVorname2.setText(null);
				wtfTitel.setText(null);
				wtfNtitel.setText(null);
				wtfOrt.setText(null);
				wtfStrasse.setText(null);
				personalDto.setPartnerIId(null);
				personalDto.setPartnerDto(null);

			
			}
		}

	}

	public void wtfNachname_focusLost(FocusEvent e) {

		boolean bExit = false;
		try {
			if (personalDto.getPartnerIId() == null) {
				// Gibt's den Eintrag schon?
				if (wtfNachname != null && wtfNachname.getText() != null
						&& !wtfNachname.getText().equals("")) {
					PartnerDto[] p = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryName1(wtfNachname.getText());
					if (p.length > 0) {
						String sMsg = "";
						String sP = "";
						try {
							sP += " | ";
							for (int i = 0; i < p.length; i++) {
								sP += p[i].formatTitelAnrede()
										+ " "
										+ (p[i].getCUid() == null ? "" : p[i]
												.getCUid());
								sP += " | ";
							}
							Object pattern[] = { sP };

							String sFS[] = {
									LPMain.getInstance().getTextRespectUISPr(
											"part.existiert"),
									LPMain.getInstance().getTextRespectUISPr(
											"part.existieren") };
							double limits[] = { 1, 2 };
							ChoiceFormat cf = new ChoiceFormat(limits, sFS);
							sMsg = cf.format(p.length > 1 ? 2 : 1);

							sMsg = MessageFormat.format(sMsg, pattern);
						} catch (Throwable ex) {
							// nothing here
						}

						DialogFactory.showModalDialog(LPMain.getInstance()
								.getTextRespectUISPr("lp.doppelter_eintrag"),
								sMsg);
						bExit = true;
					}
				}
			}
		} catch (Throwable ex1) {
			// nothing here
		}
		if (!bExit) {
			int iLK = 0;
			if (wtfKurzkennung != null) {
				iLK = wtfKurzkennung.getText() == null ? 0 : wtfKurzkennung
						.getText().length();
			}

			if (iLK == 0) {
				String sN1 = " ";
				if (wtfNachname != null) {
					sN1 = wtfNachname.getText() == null ? " " : wtfNachname
							.getText() + " ";
				}

				int iE = sN1.indexOf(" ");
				if (iE > PartnerFac.MAX_KBEZ / 2) {
					iE = PartnerFac.MAX_KBEZ / 2;
				}
				wtfKurzkennung.setText(sN1.substring(0, iE));
			}
		}
	}

}

class PanelPersonal_wtfNachname_focusAdapter extends FocusAdapter {
	private PanelPersonal adaptee;

	PanelPersonal_wtfNachname_focusAdapter(PanelPersonal adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfNachname_focusLost(e);
	}
}
