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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.EventObject;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Command;
import com.lp.client.frame.Command2IFNebeneinander;
import com.lp.client.frame.CommandCreateIF;
import com.lp.client.frame.CommandGoto;
import com.lp.client.frame.CommandSetFocus;
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
import com.lp.client.frame.component.WrapperEmailField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperURLField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.system.TabbedPaneSystem;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um das Panel Partner.</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>20.10.04</I></p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.22 $
 * 
 * Date $Date: 2012/11/09 08:01:49 $
 */
public class PanelPartnerDetail extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPanePartner tpPartner = null;

	protected JPanel jpaWorkingOn = null;

	static final private String ACTION_SPECIAL_FLR_ORT = "action_special_flr_landplzort";
	static final private String ACTION_SPECIAL_FLR_POSTFACH = "action_special_flr_landplzort_postfach";
	static final private String ACTION_SPECIAL_FLR_BRANCHE = "action_special_flr_branche";
	static final private String ACTION_SPECIAL_FLR_PARTNERKLASSE = "action_special_flr_partnerklasse";

	protected static final String ACTION_SPECIAL_VCARD_EXPORT = "action_special_vcard_export";

	// Auf diese Member koennen Panelableitungen zugreifen.
	protected PanelQueryFLR panelQueryFLRPostfach = null;
	protected PanelQueryFLR panelQueryFLROrt = null;
	protected PanelQueryFLR panelQueryFLRBranche = null;
	protected PanelQueryFLR panelQueryFLRPartnerklasse = null;
	protected JPanel jpaButtonAction = null;
	protected WrapperComboBox wcoAnrede = null;
	protected Border border = null;
	protected SortedMap<?, ?> tmAnreden = null;
	protected WrapperLabel wla1 = null;
	protected WrapperLabel wlaLocaleKommunikation = null;
	protected WrapperComboBox wcoLocaleKommunikation = null;
	protected WrapperLabel wlaKurzbezeichnung = null;
	protected WrapperCheckBox wcbVersteckt = null;
	protected Map<?, ?> tmLocales = null;
	protected Map<?, ?> tmPartnerarten = null;
	protected GridBagLayout gblAussen = null;
	protected GridBagLayout gblPartner = null;
	protected WrapperLabel wlaBemerkung = null;
	protected WrapperEditorField wefBemerkung = null;
	protected WrapperTextField wtfName2 = null;
	protected WrapperLabel wlaVorname = null;
	protected WrapperLabel wlaTelefon = null;
	protected WrapperTelefonField wtfTelefon = null;
	protected WrapperLabel wlaEmail = null;
	protected WrapperEmailField wtfEmail = null;
	protected WrapperLabel wlaFax = null;
	protected WrapperTextField wtfFax = null;
	protected WrapperLabel wlaHomepage = null;
	protected WrapperURLField wtfHomepage = null;
	protected WrapperLabel wlaDirektfax = null;
	protected WrapperTextField wtfDirektfax = null;

	protected WrapperTextField wtfName1 = null;
	protected WrapperTextField wtfKurzbezeichnung = null;
	protected WrapperTextField wtfTitel = null;
	protected WrapperTextField wtfNtitel = null;
	protected WrapperLabel wlaTitel = null;
	protected WrapperLabel wlaNtitel = null;
	protected WrapperLabel wlaPartnerart = null;
	protected WrapperComboBox wcoPartnerart = null;
	protected WrapperTextField wtfUID = null;
	protected WrapperTextField wtfEORI = null;
	protected WrapperLabel wlaUID = null;
	protected WrapperLabel wlaEORI = null;
	protected WrapperLabel wlaPostfachnr = null;
	protected WrapperButton wbuPostfach = null;
	protected WrapperTextField wtfOrtPostfach = null;
	protected WrapperTextField wtfPostfach = null;
	protected WrapperTextField wtfStrasse = null;
	protected WrapperLabel wlaStrasse = null;
	protected WrapperLabel wlaAbteilung = null;
	protected WrapperTextField wtfName3 = null;
	protected WrapperButton wbuOrt = null;
	protected WrapperTextField wtfLandPLZOrt = null;
	protected WrapperLabel wlaFirmenbuchnr = null;
	protected WrapperTextField wtfFirmenbuchnr = null;
	protected WrapperLabel wlaGerichtsstand = null;
	protected WrapperTextField wtfGerichtsstand = null;
	protected WrapperButton wbuBranche = null;
	protected WrapperTextField wtfBranche = null;
	protected WrapperLabel wlaGebDatumAnsprechpartner = null;
	protected WrapperDateField wdfGebDatumAnsprechpartner = null;
	protected WrapperButton wbuPartnerklasse = null;
	protected WrapperTextField wtfPartnerklasse = null;

	protected WrapperLabel wlaIln = null;
	protected WrapperTextField wtfIln = null;
	protected WrapperLabel wlaFilialnr = null;
	protected WrapperTextField wtfFilialnr = null;
	private WrapperLabel wlaGmtVersatz = new WrapperLabel();
	protected WrapperNumberField wnfGmtVersatz = new WrapperNumberField();

	private boolean bNeuAusPartner = false;

	public PanelPartnerDetail(InternalFrame internalFrame, String add2TitleI,
			Object keyI, TabbedPanePartner tpPartner) throws Throwable {
		this(internalFrame, add2TitleI, keyI);
		this.tpPartner = tpPartner;
	}

	public PanelPartnerDetail(InternalFrame internalFrame, String add2TitleI,
			Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();
		initPanel();
	}

	/**
	 * Fuelle die Helpercontrols, jene aus denen ausgewaehlt werden kann.
	 * 
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {
		Locale clientLocUi = LPMain.getInstance().getTheClient().getLocUi();
		tmAnreden = (SortedMap<?, ?>) DelegateFactory.getInstance()
				.getPartnerDelegate().getAllAnreden(clientLocUi);
		wcoAnrede.setMap(tmAnreden);

		tmLocales = DelegateFactory.getInstance().getLocaleDelegate()
				.getAllLocales(clientLocUi);
		wcoLocaleKommunikation.setMap(tmLocales);

		tmPartnerarten = DelegateFactory
				.getInstance()
				.getPartnerDelegate()
				.getAllPartnerArten(
						LPMain.getInstance().getTheClient().getLocUiAsString());
		wcoPartnerart.setMap(tmPartnerarten);
	}

	public TabbedPanePartner getTabbedPanePartner() {
		return tpPartner;
	}

	protected PartnerDto getPartnerDto() {
		return getTabbedPanePartner().getPartnerDto();
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		getTabbedPanePartner().setPartnerDto(partnerDto);
	}

	private void jbInit() throws Throwable {

		// Buttons.
		resetToolsPanel();

		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// von hier ...

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		// actionpanel von oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();

		add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		// ... bis hier ist's immer gleich

		wtfName3 = new WrapperTextField(PartnerFac.MAX_NAME);
		wtfName3.setText("");

		wtfName3.setMandatoryFieldDB(false);

		wlaStrasse = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.strasse"));
		wlaStrasse.setHorizontalAlignment(SwingConstants.RIGHT);

		wtfStrasse = new WrapperTextField(PartnerFac.MAX_STRASSE);
		wtfStrasse.setText(LPMain.getInstance().getTextRespectUISPr("lp.uid"));

		wtfStrasse.setMandatoryFieldDB(false);

		wcoAnrede = new WrapperComboBox();
		wcoLocaleKommunikation = new WrapperComboBox();
		wcoLocaleKommunikation.setMandatoryFieldDB(true);

		if (!LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_MEHRSPRACHIGKEIT)) {
			wcoLocaleKommunikation.setActivatable(false);
		}

		wbuOrt = new WrapperButton();
		wbuOrt.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.button.ort"));

		wbuOrt.setActionCommand(ACTION_SPECIAL_FLR_ORT);

		// flrlokal: fuers aufmachen
		wbuOrt.addActionListener(this);

		wlaGmtVersatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.land.gmtversatz"));

		wnfGmtVersatz.setFractionDigits(2);

		// flrlokal: damit der event via internalframe direkt hierherkommt,
		// nicht ueber tabbedpanes.
		getInternalFrame().addItemChangedListener(this);

		wtfLandPLZOrt = new WrapperTextField();
		wtfLandPLZOrt.setActivatable(false);

		wtfLandPLZOrt.setText("");

		wtfUID = new WrapperTextField(PartnerFac.MAX_UID);
		wtfUID.setUppercaseField(true);
		wtfUID.setText("");

		wtfEORI = new WrapperTextField(25);

		wtfTitel = new WrapperTextField(PartnerFac.MAX_TITEL);
		wtfTitel.setText("");
		wtfNtitel = new WrapperTextField(PartnerFac.MAX_TITEL);
		wtfNtitel.setText("");

		wlaTitel = new WrapperLabel();
		wlaTitel.setText(LPMain.getInstance().getTextRespectUISPr("lp.titel"));
		wlaNtitel = new WrapperLabel();
		wlaNtitel
				.setText(LPMain.getInstance().getTextRespectUISPr("lp.ntitel"));

		wlaKurzbezeichnung = new WrapperLabel();
		wlaKurzbezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kurzbez"));

		wtfKurzbezeichnung = new WrapperTextField();

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LAENGE_PARTNER_KURZBEZEICHNUNG,
						ParameterFac.KATEGORIE_ALLGEMEIN,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			wtfKurzbezeichnung.setColumnsMax(((Integer) parameter
					.getCWertAsObject()).intValue());
		}

		wtfKurzbezeichnung.setDependenceField(true);

		wtfKurzbezeichnung.setMandatoryFieldDB(true);

		// Workingpanel
		gblPartner = new GridBagLayout();
		jpaWorkingOn.setLayout(gblPartner);

		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wla1 = new WrapperLabel();
		wla1.setText("");

		wlaAbteilung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.abteilung"));
		wlaAbteilung.setHorizontalAlignment(SwingConstants.RIGHT);

		wtfName2 = new WrapperTextField(PartnerFac.MAX_NAME);

		wtfName1 = new WrapperTextField(PartnerFac.MAX_NAME);
		wtfName1.setMandatoryFieldDB(true);

		wtfName1.setDependenceField(true);

		wtfName1.addFocusListener(new WtfName1FocusAdapter(this));

		wlaVorname = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.vorname"));

		wlaLocaleKommunikation = new WrapperLabel();
		wlaLocaleKommunikation.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.sprache.kommunikation"));

		wlaUID = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.uid"));
		wlaEORI = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.eori"));

		wlaPostfachnr = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.postfach.nr"));

		wcbVersteckt = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("lp.versteckt"));

		wtfOrtPostfach = new WrapperTextField();
		wtfOrtPostfach.setActivatable(false);

		wbuPostfach = new WrapperButton();
		wbuPostfach.setActionCommand(ACTION_SPECIAL_FLR_POSTFACH);

		wbuPostfach.addActionListener(this);

		wbuPostfach.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.postfach"));

		wtfPostfach = new WrapperTextField(PartnerFac.MAX_POSTFACH);
		wtfPostfach.setText("");

		wlaGebDatumAnsprechpartner = new WrapperLabel();
		wlaGebDatumAnsprechpartner.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.geb_datum_ansprechpartner"));

		wdfGebDatumAnsprechpartner = new WrapperDateField();

		wlaGerichtsstand = new WrapperLabel();
		wlaGerichtsstand.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gerichtsstand"));

		wlaIln = new WrapperLabel();
		wlaIln.setText(LPMain.getInstance().getTextRespectUISPr("lp.iln"));
		wlaFilialnr = new WrapperLabel();
		wlaFilialnr.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.filialnr"));
		wlaFilialnr.setHorizontalAlignment(SwingConstants.LEFT);

		wtfGerichtsstand = new WrapperTextField(PartnerFac.MAX_GERICHTSSTAND);

		wtfIln = new WrapperTextField(15);
		wtfFilialnr = new WrapperTextField(15);

		wlaFirmenbuchnr = new WrapperLabel();
		wlaFirmenbuchnr.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.firmenbuchnr"));

		wtfFirmenbuchnr = new WrapperTextField(PartnerFac.MAX_FIRMENBUCHNR);

		wbuBranche = new WrapperButton();
		wbuBranche.setActionCommand(ACTION_SPECIAL_FLR_BRANCHE);

		wbuBranche.addActionListener(this);

		wbuBranche.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.branche"));
		
		
		wtfBranche = new WrapperTextField(PartnerFac.MAX_BRANCHE);
		wtfBranche.setActivatable(false);

		wbuPartnerklasse = new WrapperButton();
		wbuPartnerklasse.setActionCommand(ACTION_SPECIAL_FLR_PARTNERKLASSE);

		wbuPartnerklasse.addActionListener(this);

		wbuPartnerklasse.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.partnerklasse"));

		wtfPartnerklasse = new WrapperTextField(PartnerFac.MAX_BRANCHE);
		wtfPartnerklasse.setActivatable(false);

		wlaBemerkung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bemerkung"));
		wlaBemerkung.setVerticalAlignment(SwingConstants.NORTH);

		wefBemerkung = new WrapperEditorField(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.bemerkung"));

		wlaPartnerart = new WrapperLabel(
				LPMain.getTextRespectUISPr("part.partnerart"));
		wcoPartnerart = new WrapperComboBox();
		wcoPartnerart.setMandatoryFieldDB(true);

		wcoPartnerart.addItemListener(new WcoPartnerartItemAdapter(this));

		wlaTelefon = new WrapperLabel();
		wlaTelefon.setText(LPMain.getTextRespectUISPr("lp.telefon"));

		wtfTelefon = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);

		wlaEmail = new WrapperLabel();
		wlaEmail.setText(LPMain.getTextRespectUISPr("lp.email"));

		wtfEmail = new WrapperEmailField();

		wlaFax = new WrapperLabel(LPMain.getTextRespectUISPr("lp.fax"));
		wtfFax = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaHomepage = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.homepage"));
		wtfHomepage = new WrapperURLField();

		wtfHomepage.setColumnsMax(80);
		wtfEmail.setColumnsMax(80);


		wlaDirektfax = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.direktfax"));
		wtfDirektfax = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		// ab hier einhaengen!
		// 1. Zeile Abstaende definieren.
		jpaWorkingOn.add(wlaPartnerart, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoPartnerart, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Leerabstand
//		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
//
//		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(3, iZeile, 2, 1, 0.4, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wcoAnrede, new GridBagConstraints(0, iZeile, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfName1, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));


		// Leerabstand

		jpaWorkingOn.add(wlaKurzbezeichnung, new GridBagConstraints(4, iZeile, 2, 1, 0.4, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfKurzbezeichnung, new GridBagConstraints(6, iZeile, 2, 1, 0.4, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaVorname, new GridBagConstraints(0, iZeile, 1, 1, 0.8, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfName2, new GridBagConstraints(1, iZeile, 3, 1, 0.7, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaTitel, new GridBagConstraints(4, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfTitel, new GridBagConstraints(5, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		wlaNtitel.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaNtitel, new GridBagConstraints(6, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfNtitel, new GridBagConstraints(7, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaAbteilung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfName3, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaUID, new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfUID, new GridBagConstraints(6, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaStrasse, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfStrasse, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaEORI, new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfEORI, new GridBagConstraints(6, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuOrt, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLandPLZOrt, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaGebDatumAnsprechpartner, new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfGebDatumAnsprechpartner, new GridBagConstraints(6, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuPostfach, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfOrtPostfach, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaPostfachnr, new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPostfach, new GridBagConstraints(6, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaLocaleKommunikation, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoLocaleKommunikation, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 130, 0));

		jpaWorkingOn.add(wlaGmtVersatz, new GridBagConstraints(2, iZeile, 1, 1, 0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfGmtVersatz, new GridBagConstraints(3, iZeile, 1, 1, 0.15, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -40, 0));

		jpaWorkingOn.add(wlaIln, new GridBagConstraints(4, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfIln, new GridBagConstraints(5, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaFilialnr, new GridBagConstraints(6, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfFilialnr, new GridBagConstraints(7, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuPartnerklasse, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfPartnerklasse, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaGerichtsstand, new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfGerichtsstand, new GridBagConstraints(6, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuBranche, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBranche, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaFirmenbuchnr, new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfFirmenbuchnr, new GridBagConstraints(6, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
//		iZeile++;
//		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wefBemerkung, new GridBagConstraints(1, iZeile, 7, 1, 0.0, 0.02, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaTelefon, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfTelefon, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaEmail, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfEmail, new GridBagConstraints(4, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaFax, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfFax, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaHomepage, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfHomepage, new GridBagConstraints(4, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaDirektfax, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfDirektfax, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		createAndSaveAndShowButton("/com/lp/client/res/book_open2.png",
				LPMain.getTextRespectUISPr("part.partner.export.vcard"),
				ACTION_SPECIAL_VCARD_EXPORT, null);

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		// queue test
		// LPMain.getInstance().getInfoTopic().send2AllUser("test all info");

		setBNeuAusPartner(false);

		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {
			Object key = getTabbedPanePartner().getPartnerDto().getIId();
			if (key == null) {
				leereAlleFelder(this);
				clearStatusbar();
			} else {
				setPartnerDto(DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey((Integer) key));
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						getTabbedPanePartner().getPartnerDto()
								.formatFixTitelName1Name2());
				setStatusbar();

			}
			initPanel();
			updateComponents();
			dto2Components();
		}
	}

	protected void setDefaults() throws Throwable {

		if (getPartnerDto() == null) {
			setPartnerDto(new PartnerDto());
		}

		getPartnerDto().setBVersteckt(Helper.boolean2Short(false));

		getPartnerDto().setLocaleCNrKommunikation(
				LPMain.getInstance().getTheClient().getLocMandantAsString());

		// wenn neu
		wcoLocaleKommunikation.setKeyOfSelectedItem(LPMain.getInstance()
				.getTheClient().getLocMandantAsString());

		wcoPartnerart.setActivatable(true);
		wefBemerkung.setText(null);
	}

	protected void setStatusbar() throws Throwable {
		setStatusbarModification(getPartnerDto());
	}

	/**
	 * Fuelle alle Panelfelder.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void dto2Components() throws Throwable {

		PartnerDto partnerDto = getPartnerDto();

		wtfName1.setText(partnerDto.getCName1nachnamefirmazeile1());
		wtfName2.setText(partnerDto.getCName2vornamefirmazeile2());
		wtfName3.setText(partnerDto.getCName3vorname2abteilung());
		wtfStrasse.setText(partnerDto.getCStrasse());
		wtfKurzbezeichnung.setText(partnerDto.getCKbez());
		wcoAnrede.setKeyOfSelectedItem(partnerDto.getAnredeCNr());
		wcoPartnerart.setKeyOfSelectedItem(partnerDto.getPartnerartCNr());

		String locKommunikationAsString = partnerDto
				.getLocaleCNrKommunikation();
		wcoLocaleKommunikation.setKeyOfSelectedItem(locKommunikationAsString);

		LandplzortDto landplzortDto = partnerDto.getLandplzortDto();
		String lKZ = null;
		if (landplzortDto != null) {
			lKZ = landplzortDto.formatLandPlzOrt();
		}
		wtfLandPLZOrt.setText(lKZ);

		landplzortDto = partnerDto.getLandplzortDto_Postfach();
		lKZ = null;
		if (landplzortDto != null) {
			lKZ = landplzortDto.formatLandPlzOrt();
		}
		wtfOrtPostfach.setText(lKZ);
		wtfPostfach.setText(partnerDto.getCPostfach());
		wnfGmtVersatz.setDouble(partnerDto.getFGmtversatz());
		wtfUID.setText(partnerDto.getCUid());
		wtfEORI.setText(partnerDto.getCEori());
		wtfTitel.setText(partnerDto.getCTitel());
		wtfNtitel.setText(partnerDto.getCNtitel());
		wdfGebDatumAnsprechpartner.setDate(partnerDto
				.getDGeburtsdatumansprechpartner());
		wtfGerichtsstand.setText(partnerDto.getCGerichtsstand());
		wtfIln.setText(partnerDto.getCIln());
		wtfFilialnr.setText(partnerDto.getCFilialnummer());
		wtfFirmenbuchnr.setText(partnerDto.getCFirmenbuchnr());

		if (partnerDto.getBrancheIId() != null) {
			BrancheDto brancheDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.brancheFindByPrimaryKey(partnerDto.getBrancheIId());
			wtfBranche.setText(brancheDto.getCNr());
		} else {
			wtfBranche.setText(null);
		}

		if (partnerDto.getPartnerklasseIId() != null) {
			PartnerklasseDto partnerklasseDto = DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.partnerklasseFindByPrimaryKey(
							partnerDto.getPartnerklasseIId());
			wtfPartnerklasse.setText(partnerklasseDto.getCNr());
		} else {
			wtfPartnerklasse.setText(null);
		}

		wcbVersteckt.setShort(partnerDto.getBVersteckt());
		wefBemerkung.setText(partnerDto.getXBemerkung());

		// Partnerkommunikation

		wtfDirektfax.setText(partnerDto.getCDirektfax());

		wtfEmail.setEmail(partnerDto.getCEmail(), null);

		wtfFax.setText(partnerDto.getCFax());

		wtfHomepage.setText(partnerDto.getCHomepage());

		wtfTelefon.setPartnerKommunikationDto(partnerDto,
				partnerDto.getCTelefon());

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		super.eventActionUpdate(aE, bNeedNoUpdateI);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ORT)) {
			panelQueryFLROrt = SystemFilterFactory.getInstance()
					.createPanelFLRLandplzort(getInternalFrame(), null, true);
			// vorbesetzen
			if (getPartnerDto().getLandplzortIId() != null) {
				panelQueryFLROrt.setSelectedId(getPartnerDto()
						.getLandplzortIId());
			}
			new DialogQuery(panelQueryFLROrt);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_POSTFACH)) {
			panelQueryFLRPostfach = SystemFilterFactory.getInstance()
					.createPanelFLRLandplzort(getInternalFrame(),
							getPartnerDto().getLandplzortIIdPostfach(), true);

			new DialogQuery(panelQueryFLRPostfach);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_BRANCHE)) {
			String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
					.createButtonArray(false, true);

			final QueryType[] querytypes = null;
			final FilterKriterium[] filters = null;
			panelQueryFLRBranche = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_BRANCHE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.branche"));
			// vorbesetzen
			if (getPartnerDto().getBrancheIId() != null) {
				panelQueryFLRBranche.setSelectedId(getPartnerDto()
						.getBrancheIId());
			}
			new DialogQuery(panelQueryFLRBranche);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FLR_PARTNERKLASSE)) {
			String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
					.createButtonArray(false, true);

			final QueryType[] querytypes = null;
			final FilterKriterium[] filters = null;
			panelQueryFLRPartnerklasse = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_PARTNERKLASSE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.partnerklasse"));
			// vorbesetzen
			if (getPartnerDto().getPartnerklasseIId() != null) {
				panelQueryFLRPartnerklasse.setSelectedId(getPartnerDto()
						.getPartnerklasseIId());
			}
			new DialogQuery(panelQueryFLRPartnerklasse);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VCARD_EXPORT)) {
			HelperClient.vCardAlsDateiExportieren(getPartnerDto(), null);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (!bNeedNoSaveI) {

			wtfName1FocusLost(null);

			if (allMandatoryFieldsSetDlg()) {

				checkLockedDlg();

				components2Dto();

				if (getPartnerDto().getIId() == null) {
					// create
					Integer key = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.createPartner(getPartnerDto());

					setKeyWhenDetailPanel(key);

					getPartnerDto().setIId(key);
				} else {
					// update
					DelegateFactory.getInstance().getPartnerDelegate()
							.updatePartner(getPartnerDto());
				}

				super.eventActionSave(e, true);

				eventYouAreSelected(false);
			}
		} else {
			super.eventActionSave(e, true);
		}
	}

	protected void components2Dto() throws Throwable {

		PartnerDto partnerDto = getPartnerDto();

		if (isBNeuAusPartner()) {
			if (!wtfName1.getText().equals(
					partnerDto.getCName1nachnamefirmazeile1())) {
				// --hat nach neu aus LF|KD|... den Namen1 geaendert: create
				// beim Partner
				partnerDto.setIId(null);
			}
		}

		partnerDto.setPersonalIIdAendern(LPMain.getInstance().getTheClient()
				.getIDPersonal());
		partnerDto.setPersonalIIdAnlegen(LPMain.getInstance().getTheClient()
				.getIDPersonal());

		partnerDto.setCName1nachnamefirmazeile1(wtfName1.getText());

		partnerDto.setCName2vornamefirmazeile2(wtfName2.getText());

		partnerDto.setCName3vorname2abteilung(wtfName3.getText());

		partnerDto.setCUid(wtfUID.getText());
		partnerDto.setCEori(wtfEORI.getText());
		partnerDto.setFGmtversatz(wnfGmtVersatz.getDouble());
		partnerDto.setCTitel(wtfTitel.getText());
		partnerDto.setCNtitel(wtfNtitel.getText());

		partnerDto.setCStrasse(wtfStrasse.getText());

		partnerDto.setLocaleCNrKommunikation((String) wcoLocaleKommunikation
				.getKeyOfSelectedItem());

		partnerDto.setCKbez(wtfKurzbezeichnung.getText());

		partnerDto.setBVersteckt(wcbVersteckt.getShort());

		partnerDto.setAnredeCNr((String) wcoAnrede.getKeyOfSelectedItem());

		partnerDto.setPartnerartCNr((String) wcoPartnerart
				.getKeyOfSelectedItem());

		partnerDto.setCPostfach(wtfPostfach.getText());

		partnerDto.setDGeburtsdatumansprechpartner(wdfGebDatumAnsprechpartner
				.getDate());
		partnerDto.setCGerichtsstand(wtfGerichtsstand.getText());

		partnerDto.setCIln(wtfIln.getText());
		partnerDto.setCFilialnummer(wtfFilialnr.getText());

		partnerDto.setCFirmenbuchnr(wtfFirmenbuchnr.getText());

		partnerDto.setXBemerkung(wefBemerkung.getText());

		// Kommunikationsdaten

		partnerDto.setCDirektfax(wtfDirektfax.getText());

		partnerDto.setCTelefon(wtfTelefon.getText());

		partnerDto.setCEmail(wtfEmail.getText());

		partnerDto.setCFax(wtfFax.getText());
		partnerDto.setCHomepage(wtfHomepage.getText());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ACTION_NEW) {

			if (e.getSource() == panelQueryFLROrt) {
				// cmd: 5
				CommandCreateIF createIFSystem = new CommandCreateIF(
						Command.S_INTERNALFRAME_SYSTEM);
				LPMain.getInstance().execute(createIFSystem);

				Command2IFNebeneinander command2IFNebeneinander = new Command2IFNebeneinander(
						Command.S_INTERNALFRAME_SYSTEM);
				command2IFNebeneinander
						.setSInternalFrame2I(Command.S_INTERNALFRAME_PARTNER);
				LPMain.getInstance().execute(command2IFNebeneinander);

				CommandSetFocus setFocusCmd = new CommandSetFocus(
						Command.S_INTERNALFRAME_SYSTEM);
				LPMain.getInstance().execute(setFocusCmd);

				CommandGoto gotoSystemLandOrtPLZ = new CommandGoto(
						Command.S_INTERNALFRAME_SYSTEM, TabbedPaneSystem.class,
						Command.PANEL_LAND_PLZ_ORT, ACTION_UPDATE);

				LPMain.getInstance().execute(gotoSystemLandOrtPLZ);
			}
		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLROrt) {
				Integer keyLandPLZOrt = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (keyLandPLZOrt != null) {
					LandplzortDto landPLZOrtDto = DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.landplzortFindByPrimaryKey((Integer) keyLandPLZOrt);
					getPartnerDto().setLandplzortDto(landPLZOrtDto);
					getPartnerDto().setLandplzortIId(keyLandPLZOrt);
					wtfLandPLZOrt.setText(landPLZOrtDto.formatLandPlzOrt());
					wnfGmtVersatz.setDouble(landPLZOrtDto.getLandDto()
							.getFGmtversatz());
				}
			}

			else if (e.getSource() == panelQueryFLRPostfach) {
				Integer keyLandPLZOrt = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (keyLandPLZOrt != null) {
					LandplzortDto landPLZOrtDto = DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.landplzortFindByPrimaryKey((Integer) keyLandPLZOrt);
					getPartnerDto().setLandplzortDto_Postfach(landPLZOrtDto);
					getPartnerDto().setLandplzortIIdPostfach(keyLandPLZOrt);
					wtfOrtPostfach.setText(landPLZOrtDto.formatLandPlzOrt());
				}
			}

			else if (e.getSource() == panelQueryFLRBranche) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					BrancheDto brancheDto = DelegateFactory.getInstance()
							.getPartnerDelegate().brancheFindByPrimaryKey(key);
					getPartnerDto().setBrancheIId(key);
					wtfBranche.setText(brancheDto.getCNr());
				}
			}

			else if (e.getSource() == panelQueryFLRPartnerklasse) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					PartnerklasseDto partnerklasseDto = DelegateFactory
							.getInstance().getPartnerDelegate()
							.partnerklasseFindByPrimaryKey(key);
					getPartnerDto().setPartnerklasseIId(key);
					wtfPartnerklasse.setText(partnerklasseDto.getCNr());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLROrt) {
				wtfLandPLZOrt.setText(null);
				getPartnerDto().setLandplzortDto(null);
				getPartnerDto().setLandplzortIId(null);
			} else if (e.getSource() == panelQueryFLRPostfach) {
				wtfOrtPostfach.setText(null);
				getPartnerDto().setLandplzortDto_Postfach(null);
				getPartnerDto().setLandplzortIIdPostfach(null);
			} else if (e.getSource() == panelQueryFLRBranche) {
				wtfBranche.setText(null);
				getPartnerDto().setBrancheIId(null);
			} else if (e.getSource() == panelQueryFLRPartnerklasse) {
				wtfPartnerklasse.setText(null);
				getPartnerDto().setPartnerklasseIId(null);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelQueryFLROrt) {
				Integer keyLandPLZOrt = (Integer) panelQueryFLROrt
						.getKeyWhenDetailPanel();
				if (keyLandPLZOrt != null) {
					LandplzortDto landPLZOrtDto = DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.landplzortFindByPrimaryKey((Integer) keyLandPLZOrt);
					getPartnerDto().setLandplzortDto(landPLZOrtDto);
					getPartnerDto().setLandplzortIId(keyLandPLZOrt);
					wtfLandPLZOrt.setText(landPLZOrtDto.formatLandPlzOrt());
					wnfGmtVersatz.setDouble(landPLZOrtDto.getLandDto()
							.getFGmtversatz());
				}
			}
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, bLockMeI, bNeedNoNewI);

		if (!bNeedNoNewI) {
			// Partner leeren.
			setPartnerDto(new PartnerDto());

			setDefaults();
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!bNeedNoDeleteI) {
			if (!isLockedDlg()) {
				DelegateFactory.getInstance().getPartnerDelegate()
						.removePartner(getPartnerDto());

				setPartnerDto(new PartnerDto());
			}
		}
		super.eventActionDelete(e, bAdministrateLockKeyI, false);
	}

	protected void wtfName1FocusLost(FocusEvent e) {

		boolean bExit = false;
		try {

			if (getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW
					|| getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {

				if (getPartnerDto().getIId() == null // neu
						|| (wtfName1 != null && wtfName1.getText() != null && !wtfName1
								.getText()
								.equals(getPartnerDto()
										.getCName1nachnamefirmazeile1())) // name1
																			// geaendert
						|| isBNeuAusPartner()) { // neu aus

					if (wtfName1 != null && wtfName1.getText() != null
							&& !wtfName1.getText().equals("")) {
						// es steht was im Name1
						PartnerDto[] p = DelegateFactory
								.getInstance()
								.getPartnerDelegate()
								.partnerFindByPrimaryName1(
										wtfName1.getText() + "%");
						if (getPartnerDto().getIId() == null) {
							if (p.length > 0) {
								String sMsg = "";
								String sP = "";
								try {
									sP += " | ";
									for (int i = 0; i < p.length; i++) {
										sP += DelegateFactory
												.getInstance()
												.getPartnerDelegate()
												.formatFixAnredeTitelName2Name1(
														p[i],
														LPMain.getInstance()
																.getTheClient()
																.getLocUi())
												+ " "
												+ ((p[i].getCStrasse() == null) ? " "
														: p[i].getCStrasse())
												+ ((p[i].getCUid() == null) ? ""
														: p[i].getCUid());
										sP += " | ";
									}
									Object pattern[] = { sP };

									String sFS[] = {
											LPMain.getInstance()
													.getTextRespectUISPr(
															"part.existiert"),
											LPMain.getInstance()
													.getTextRespectUISPr(
															"part.existieren") };
									double limits[] = { 1, 2 };
									ChoiceFormat cf = new ChoiceFormat(limits,
											sFS);
									sMsg = cf.format((p.length > 1) ? 2 : 1);

									sMsg = MessageFormat.format(sMsg, pattern);
								} catch (Throwable exDummy) {
									// nothing here
								}

								DialogFactory
										.showModalDialog(
												LPMain.getInstance()
														.getTextRespectUISPr(
																"lp.doppelter_eintrag"),
												sMsg);
								bExit = true;
							}
						}
					}
				}
			}
		} catch (Throwable ex1) {
			// nothing here
		}

		if (!bExit) {
			// Kurzbezeichnung
			int iLK = 0;
			if (wtfKurzbezeichnung != null) {
				iLK = (wtfKurzbezeichnung.getText() == null) ? 0
						: wtfKurzbezeichnung.getText().length();
			}

			if (iLK == 0) {
				String sN1 = " ";
				if (wtfName1 != null) {
					sN1 = (wtfName1.getText() == null) ? " " : wtfName1
							.getText() + " ";
				}

				int iE = sN1.indexOf(" ");
				if (iE > PartnerFac.MAX_KBEZ / 2) {
					iE = PartnerFac.MAX_KBEZ / 2;
				}
				wtfKurzbezeichnung.setText(sN1.substring(0, iE));
			}
		}
	}

	protected void wcoItemStateChanged(ItemEvent e) {
		updateComponents();
	}

	/**
	 * Update alle Components; dh. hide/show Components; reset Components.
	 */
	private void updateComponents() {

		wlaGebDatumAnsprechpartner.setVisible(false);
		wdfGebDatumAnsprechpartner.setVisible(false);
		wlaDirektfax.setVisible(false);
		wtfDirektfax.setVisible(false);

		wtfTitel.setVisible(true);
		wtfNtitel.setVisible(true);
		wlaTitel.setVisible(true);
		wlaNtitel.setVisible(true);
		wtfUID.setVisible(true);
		wlaUID.setVisible(true);
		wlaEORI.setVisible(true);
		wtfEORI.setVisible(true);
		wbuPostfach.setVisible(true);
		wtfOrtPostfach.setVisible(true);
		wtfPostfach.setVisible(true);
		wlaPostfachnr.setVisible(true);
		wtfStrasse.setVisible(true);
		wlaStrasse.setVisible(true);
		wlaAbteilung.setVisible(true);
		wtfName3.setVisible(true);
		wbuOrt.setVisible(true);
		wtfLandPLZOrt.setVisible(true);
		wlaFirmenbuchnr.setVisible(true);
		wtfFirmenbuchnr.setVisible(true);
		wlaGerichtsstand.setVisible(true);
		wtfGerichtsstand.setVisible(true);
		wlaIln.setVisible(true);
		wtfIln.setVisible(true);
		wbuBranche.setVisible(true);
		wtfBranche.setVisible(true);
		wlaVorname.setVisible(false);

		wlaFilialnr.setVisible(true);
		wtfFilialnr.setVisible(true);

		String partnerart = (String) wcoPartnerart.getKeyOfSelectedItem();

		if (partnerart != null) {

			if (partnerart.equals(PartnerFac.PARTNERART_FIRMA)) {
				wtfTitel.setVisible(false);
				wlaTitel.setVisible(false);
				wtfTitel.setText(null);
				wtfNtitel.setVisible(false);
				wlaNtitel.setVisible(false);
				wtfNtitel.setText(null);
			}

			else if (partnerart.equals(PartnerFac.PARTNERART_PERSON)) {
				wtfUID.setVisible(false);
				wlaUID.setVisible(false);
				wtfUID.setText(null);
				wtfEORI.setVisible(false);
				wlaEORI.setVisible(false);
				wtfEORI.setText(null);
				wlaFilialnr.setVisible(false);
				wtfFilialnr.setVisible(false);
			}

			else if (partnerart.equals(PartnerFac.PARTNERART_ANSPRECHPARTNER)) {
				wlaFilialnr.setVisible(false);
				wtfFilialnr.setVisible(false);

				wtfUID.setVisible(false);
				wlaUID.setVisible(false);
				wtfUID.setText(null);
				wtfEORI.setVisible(false);
				wlaEORI.setVisible(false);
				wtfEORI.setText(null);

				wlaAbteilung.setVisible(false);
				wtfName3.setVisible(false);
				wtfName3.setText(null);

				wlaFirmenbuchnr.setVisible(false);
				wtfFirmenbuchnr.setVisible(false);
				wtfFirmenbuchnr.setText(null);

				wlaGerichtsstand.setVisible(false);
				wtfGerichtsstand.setVisible(false);
				wtfGerichtsstand.setText(null);

				wlaIln.setVisible(false);
				wtfIln.setVisible(false);
				wtfIln.setText(null);

				wlaDirektfax.setVisible(true);
				wtfDirektfax.setVisible(true);

				wlaGebDatumAnsprechpartner.setVisible(true);
				wdfGebDatumAnsprechpartner.setVisible(true);

				wlaVorname.setVisible(true);
				if (getPartnerDto() != null && getPartnerDto().getIId() == null) {
					wcoAnrede
							.setKeyOfSelectedItem(PartnerFac.PARTNER_ANREDE_HERR);
				}
			}
		}
	}

	public void setBNeuAusPartner(boolean bNeuAusPartner) {
		this.bNeuAusPartner = bNeuAusPartner;
	}

	public boolean isBNeuAusPartner() {
		return bNeuAusPartner;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfName1;
	}

	protected void setActivatable(boolean newState) {
		wcoPartnerart.setActivatable(newState);
		wcoAnrede.setActivatable(newState);
		wtfName1.setActivatable(newState);
		wtfName2.setActivatable(newState);
		wtfKurzbezeichnung.setActivatable(newState);
		wtfTitel.setActivatable(newState);
		wtfNtitel.setActivatable(newState);
		wtfUID.setActivatable(newState);
		wtfEORI.setActivatable(newState);
		wbuPostfach.setActivatable(newState);
		wbuPostfach.setEnabled(newState);
		wtfOrtPostfach.setActivatable(newState);
		wtfPostfach.setActivatable(newState);
		wtfStrasse.setActivatable(newState);
		wtfName3.setActivatable(newState);
		wbuOrt.setActivatable(newState);
		wbuOrt.setEnabled(newState);
		wtfLandPLZOrt.setActivatable(newState);
		wtfFirmenbuchnr.setActivatable(newState);
		wtfGerichtsstand.setActivatable(newState);
		wbuBranche.setActivatable(newState);
		wtfBranche.setActivatable(newState);
		wdfGebDatumAnsprechpartner.setActivatable(newState);
		wbuPartnerklasse.setActivatable(newState);
		wtfPartnerklasse.setActivatable(newState);
		wefBemerkung.setEditable(newState);
		wtfEmail.setActivatable(newState);
		wtfHomepage.setActivatable(newState);
		wtfIln.setActivatable(newState);
		wtfFilialnr.setActivatable(newState);
		wnfGmtVersatz.setActivatable(newState);
		wtfTelefon.setActivatable(newState);
		wtfFax.setActivatable(newState);
	}

	/**
	 * S&auml;mtliche Eingabefelder auf "Readonly" - also nur lesbar umstellen
	 */
	protected void beReadonly() {
		setActivatable(false);
	}

	/**
	 * S&auml;mtliche Eingabefelder sind wieder &auml;nderbar
	 */
	protected void beWritable() {
		setActivatable(true);
	}
}

// ******************************************************************************
class WtfName1FocusAdapter extends java.awt.event.FocusAdapter {
	private PanelPartnerDetail adaptee;

	protected WtfName1FocusAdapter(PanelPartnerDetail adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfName1FocusLost(e);
	}

}

// ******************************************************************************
class WcoPartnerartItemAdapter implements ItemListener {
	private PanelPartnerDetail adaptee;

	WcoPartnerartItemAdapter(PanelPartnerDetail adaptee) {
		this.adaptee = adaptee;
	}

	public void itemStateChanged(ItemEvent e) {
		adaptee.wcoItemStateChanged(e);
	}
}
