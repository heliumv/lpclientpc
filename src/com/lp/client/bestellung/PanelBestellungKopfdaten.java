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
package com.lp.client.bestellung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogGeaenderteKonditionenEK;
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
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * In diesem Detailfenster der Bestellung werden Kopfdaten erfasst bzw.
 * geaendert.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum 2005-02-16
 * </p>
 * 
 * @author Josef Erlinger; Josef Ornetsmueller
 * @version $Revision: 1.43 $
 */
public class PanelBestellungKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private final InternalFrameBestellung intFrame;

	private KostenstelleDto kostenstelleDto = null;
	private AnsprechpartnerDto ansprechpartnerDto;
	private AnsprechpartnerDto ansprechpartnerLieferadresseDto;
	private AnsprechpartnerDto ansprechpartnerAbholadresseDto;
	private PersonalDto anfordererDto = null;
	private PartnerDto lieferadresseDto = null;
	private PartnerDto abholadresseDto = null;
	private LieferantDto lieferantDtoRechnungsadresse = null;

	private AuftragDto auftragDto = null;

	private JPanel jPanelWorkingOn = null;
	private WrapperLabel wlaProjekt = null;
	private WrapperTextField wtfProjekt = null;

	private WrapperLabel wlaLieferantenangebot = null;
	private WrapperTextField wtfLieferantenangebot = null;

	private WrapperLabel wlaBestellungsart = null;
	private WrapperComboBox wcoBestellungsart = null;
	private WrapperLabel wlaBelegdatum = null;
	private WrapperDateField wdfBelegdatum = null;
	protected WrapperComboBox wcoWaehrungen = new WrapperComboBox();
	private WrapperLabel wlaWaehrung = null;
	private WrapperLabel wlaWechselkursMandant2BS = null;
	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;
	private WrapperButton wbuKostenstelle = null;

	private WrapperTextField wtfKostenstelleBezeichnung = null;
	private WrapperTextField wtfKostenstelleNummer = null;

	private WrapperDateField wdfMahnsperre = new WrapperDateField();
	private WrapperLabel wlaMahnsperreBis = new WrapperLabel();

	// private Map<?, ?> tmWaehrungen = null;
	private WrapperGotoButton wbuLieferant = null;
	private PanelQueryFLR panelQueryFLRLieferant = null;

	private WrapperCheckBox wcbPoenale = null;

	private WrapperTextField wtfLieferantBestellung = null;
	private WrapperButton wbuLieferantRechnungsadresse = null;
	private PanelQueryFLR panelQueryFLRRechnungsadresse = null;
	private WrapperTextField wtfLieferantRechnungsadresse = null;

	private WrapperButton wbuAnforderer = null;
	private PanelQueryFLR panelQueryFLRAnforderer = null;
	private WrapperTextField wtfAnforderer = null;

	private WrapperButton wbuLieferadresse = null;
	private PanelQueryFLR panelQueryFLRLieferadresse = null;
	private WrapperTextField wtfLieferadresse = null;

	private WrapperTextField wtfAnsprechpartner = null;
	private WrapperButton wbuAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	private WrapperTextField wtfAnsprechpartnerLieferadresse = null;
	private WrapperButton wbuAnsprechpartnerLieferadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerLieferadresse = null;

	private WrapperCheckBox wcbTeillieferung = null;
	private WrapperNumberField wnfLeihtage = null;
	private WrapperLabel wlaLeihtage = null;

	private WrapperLabel wlaAnfrage = null;
	private WrapperTextField wtfAnfrage = null;

	private WrapperLabel wlaAbrufe = null;
	private WrapperTextArea wtaAbrufe = null;

	private WrapperButton wbuRahmenauswahl = null;
	private PanelQueryFLR panelQueryFLRRahmenauswahl = null;

	private WrapperTextField wtfAbrufausrahmencnr = null;
	private WrapperTextField wtfAbrufausrahmenbez = null;

	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private WrapperButton wbuAuftrag = null;
	private WrapperTextField wtfAuftragnummer = null;
	private WrapperTextField wtfAuftragbezeichnung = null;

	private WrapperNumberField wnfDivisor = null;
	private WrapperRadioButton wcbDivisor = null;
	private WrapperRadioButton wcbRest = null;
	private WrapperRadioButton wcbManuellAbruf = null;
	private ButtonGroup jbgAbrufBes = null;

	private WrapperButton wbuAbholadresse = null;
	private WrapperTextField wtfAbholadresse = null;
	private WrapperTextField wtfAnsprechpartnerAbholadresse = null;
	private WrapperButton wbuAnsprechpartnerAbholadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerAbholadresse = null;
	private PanelQueryFLR panelQueryFLRAbholadresse = null;

	private WrapperSelectField wsfProjekt = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), true);

	public final static String ACTION_SPECIAL_BESTELLUNGART_CHANGED = "action_special_bestellungart_changed";
	public final static String ACTION_SPECIAL_KOSTENSTELLE = "action_special_be_kostenstelle";
	public final static String ACTION_SPECIAL_LIEFERANT_BESTELLUNG = "action_special_lieferant_bestellung";
	public final static String ACTION_SPECIAL_RECHNUNGSADRESSE_BESTELLUNG = "action_special_rechnungsadresse_bestellung";
	public final static String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT = "action_special_ansprechpartnerlieferant";
	public final static String ACTION_SPECIAL_ANFORDERER = "action_special_anforderer";
	public final static String ACTION_SPECIAL_LIEFERADRESSE = "action_special_lieferadresse";
	public final static String ACTION_SPECIAL_RAHMENAUSWAHL = "action_special_rahmenauswahl";
	public final static String ACTION_SPECIAL_AUFTRAG = "action_special_auftrag";
	public final static String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE = "action_special_ansprechpartnerlieferadresse";
	public final static String ACTION_SPECIAL_ABHOLADRESSE = "action_special_abholdresse";
	public final static String ACTION_SPECIAL_ANSPRECHPARTNER_ABHOLADRESSE = "action_special_ansprechpartnerabholadresse";

	// merkt sich bestellungart (fuer rahmen und abruf)
	// private String bestellungsart = null;
	private Integer iBestellungAbrufId = null;
	private BigDecimal bdWechselkursMandant2BS = null;

	// die urspruengliche Belegwaehrung hinterlegen
	private WaehrungDto waehrungDtoOld = null;
	private WrapperLabel wlaLeer = new WrapperLabel();
	private WrapperLabel wlaLeerAbruf = null;
	private boolean bAbrufbesUndNeu = false;
	private JPanel jPanelAbrufBes = null;
	private int nMengePos = 0;

	public PanelBestellungKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);
		intFrame = (InternalFrameBestellung) internalFrame;
		jbInit();
		initPanel();
		initComponents();
	}

	private void prepareWaehrung() throws Throwable {
		if (!wcoWaehrungen.isMapSet()) {
			wcoWaehrungen.setMap(DelegateFactory.getInstance()
					.getLocaleDelegate().getAllWaehrungen());
		}

		wcoWaehrungen.setKeyOfSelectedItem(LPMain.getTheClient()
				.getSMandantenwaehrung());
	}

	private void jbInit() throws Throwable {
		setLayout(new GridBagLayout());

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD, PanelBasis.ACTION_PRINT };
		enableToolsPanelButtons(aWhichButtonIUse);

		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		wlaProjekt = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.projekt"));
		wlaLieferantenangebot = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.lieferantenangebot"));

		wtfProjekt = new WrapperTextField(80);
		wtfLieferantenangebot = new WrapperTextField(40);

		wlaBestellungsart = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.bestellungart"));
		HelperClient.setDefaultsToComponent(wlaBestellungsart, 115);

		wcoBestellungsart = new WrapperComboBox();
		wcoBestellungsart.setMandatoryFieldDB(true);
		wcoBestellungsart
				.setActionCommand(ACTION_SPECIAL_BESTELLUNGART_CHANGED);
		wcoBestellungsart.addActionListener(this);

		wcbPoenale = new WrapperCheckBox();
		wcbPoenale.setText(LPMain.getTextRespectUISPr("detail.label.poenale"));

		wlaBelegdatum = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.belegdatum"));
		HelperClient.setDefaultsToComponent(wlaBelegdatum, 90);

		wdfBelegdatum = new WrapperDateField();
		wdfBelegdatum.setShowRubber(false);
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_BEWEGUNGSMODULE_ANLEGEN_BIS_ZUM);
		int iTagen = ((Integer) parametermandantDto.getCWertAsObject())
				.intValue();
		// Parameter anlegen bis zum beruecksichtigen
		GregorianCalendar gc = new GregorianCalendar();
		// Auf Vormonat setzen
		gc.set(Calendar.MONTH, gc.get(Calendar.MONTH) - 1);
		// Tag setzen
		gc.set(Calendar.DATE, iTagen);

		wdfBelegdatum.setMinimumValue(new Date(gc.getTimeInMillis()));

		wlaLiefertermin = new WrapperLabel();
		wlaLiefertermin.setText(LPMain
				.getTextRespectUISPr("label.liefertermin"));

		wdfLiefertermin = new WrapperDateField();
		wdfLiefertermin.setShowRubber(false);
		wdfLiefertermin.setMandatoryField(true);

		wlaWaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.waehrung"));

		wlaWechselkursMandant2BS = new WrapperLabel();

		// wcoWaehrungen = new WrapperComboBox();
		wcoWaehrungen.setMandatoryFieldDB(true);
		prepareWaehrung();

		wcoWaehrungen
				.addActionListener(new PanelBestellungKopfdaten_wcoWaehrungen_actionAdapter(
						this));

		wbuKostenstelle = new WrapperButton();
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKostenstelle.setText(LPMain
				.getTextRespectUISPr("label.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain
				.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wbuKostenstelle.addActionListener(this);
		wtfKostenstelleBezeichnung = new WrapperTextField();
		wtfKostenstelleNummer = new WrapperTextField();
		wtfKostenstelleBezeichnung.setActivatable(false);
		wtfKostenstelleNummer.setActivatable(false);

		wbuLieferant = new WrapperGotoButton(
				WrapperGotoButton.GOTO_LIEFERANT_AUSWAHL);
		wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT_BESTELLUNG);
		wbuLieferant.setText(LPMain.getTextRespectUISPr("button.lieferant"));
		wbuLieferant.addActionListener(this);

		wtfLieferantBestellung = new WrapperTextField();
		wtfLieferantBestellung.setMandatoryField(true);
		wtfLieferantBestellung.setActivatable(false);
		wtfLieferantBestellung.setColumnsMax(80);

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));

		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAnsprechpartner.setActivatable(false);
		wbuAnsprechpartner
				.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT);

		wbuAnsprechpartnerLieferadresse = new WrapperButton();
		wbuAnsprechpartnerLieferadresse.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));

		wbuAnsprechpartnerLieferadresse.addActionListener(this);

		wtfAnsprechpartnerLieferadresse = new WrapperTextField();
		wtfAnsprechpartnerLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAnsprechpartnerLieferadresse.setActivatable(false);
		wbuAnsprechpartnerLieferadresse
				.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE);

		wbuAnsprechpartnerAbholadresse = new WrapperButton();
		wbuAnsprechpartnerAbholadresse.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerAbholadresse
				.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_ABHOLADRESSE);
		wbuAnsprechpartnerAbholadresse.addActionListener(this);

		wtfAnsprechpartnerAbholadresse = new WrapperTextField();
		wtfAnsprechpartnerAbholadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAnsprechpartnerAbholadresse.setActivatable(false);

		wbuLieferantRechnungsadresse = new WrapperButton();
		wbuLieferantRechnungsadresse.setText(LPMain
				.getTextRespectUISPr("button.rechnungsadresse"));
		wbuLieferantRechnungsadresse
				.setActionCommand(ACTION_SPECIAL_RECHNUNGSADRESSE_BESTELLUNG);
		wbuLieferantRechnungsadresse.addActionListener(this);

		wtfLieferantRechnungsadresse = new WrapperTextField();
		wtfLieferantRechnungsadresse
				.setColumnsMax(QueryParameters.FLR_BREITE_XXL);
		wtfLieferantRechnungsadresse.setMandatoryField(true);
		wtfLieferantRechnungsadresse.setActivatable(false);

		wlaLeihtage = new WrapperLabel();
		wlaLeihtage
				.setText(LPMain.getTextRespectUISPr("detail.label.leihtage"));

		wnfLeihtage = new WrapperNumberField(new Long(
				BestellungFac.MIN_I_LEIHTAGE).intValue(), new Long(
				BestellungFac.MAX_I_LEIHTAGE).intValue());
		wnfLeihtage.setFractionDigits(BestellungFac.FRACTION_LEIHTAGE);

		wlaLeihtage = new WrapperLabel();
		wlaLeihtage
				.setText(LPMain.getTextRespectUISPr("detail.label.leihtage"));

		wbuAnforderer = new WrapperButton();
		wbuAnforderer.setText(LPMain
				.getTextRespectUISPr("bes.button.anforderer"));
		wbuAnforderer.setActionCommand(ACTION_SPECIAL_ANFORDERER);
		wbuAnforderer.addActionListener(this);

		wtfAnforderer = new WrapperTextField();
		wtfAnforderer.setActivatable(false);
		wtfAnforderer.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_MAHNUNGSABSENDER);
		int iMahnungsabsenderNachAnforderer = ((Integer) parametermandantDto
				.getCWertAsObject());

		if (iMahnungsabsenderNachAnforderer == 1) {
			wtfAnforderer.setMandatoryField(true);
		}

		wbuLieferadresse = new WrapperButton();
		wbuLieferadresse.setText(LPMain
				.getTextRespectUISPr("button.lieferadresse"));
		wbuLieferadresse.setActionCommand(ACTION_SPECIAL_LIEFERADRESSE);
		wbuLieferadresse.addActionListener(this);

		wbuAbholadresse = new WrapperButton();
		wbuAbholadresse.setText(LPMain.getTextRespectUISPr("best.abholadresse")
				+ "..");
		wbuAbholadresse.setActionCommand(ACTION_SPECIAL_ABHOLADRESSE);
		wbuAbholadresse.addActionListener(this);
		wtfAbholadresse = new WrapperTextField();
		wtfAbholadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAbholadresse.setActivatable(false);

		wtfLieferadresse = new WrapperTextField();
		wtfLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfLieferadresse.setActivatable(false);

		wcbTeillieferung = new WrapperCheckBox();
		wcbTeillieferung.setText(LPMain
				.getTextRespectUISPr("label.teillieferung"));

		wnfLeihtage = new WrapperNumberField();

		wlaAnfrage = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.anfragenummer"));
		wtfAnfrage = new WrapperTextField();
		wtfAnfrage.setActivatable(false);

		wlaAbrufe = new WrapperLabel(LPMain.getTextRespectUISPr("bes.abrufe"));
		wtaAbrufe = new WrapperTextArea();
		wtaAbrufe.setActivatable(false);
		wtaAbrufe.setRows(4);

		wbuRahmenauswahl = new WrapperButton(
				LPMain.getTextRespectUISPr("button.rahmen"));
		wbuRahmenauswahl.setActionCommand(ACTION_SPECIAL_RAHMENAUSWAHL);
		wbuRahmenauswahl.addActionListener(this);

		wtfAbrufausrahmencnr = new WrapperTextField();
		wtfAbrufausrahmencnr.setActivatable(false);
		wtfAbrufausrahmenbez = new WrapperTextField();
		wtfAbrufausrahmenbez.setActivatable(false);

		wlaMahnsperreBis
				.setText(LPMain.getTextRespectUISPr("lp.mahnsperrebis"));

		wbuAuftrag = new WrapperButton();
		wtfAuftragnummer = new WrapperTextField();
		wtfAuftragbezeichnung = new WrapperTextField();
		wbuAuftrag.addActionListener(this);
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG);
		wbuAuftrag.setText(LPMain.getTextRespectUISPr("button.auftrag"));
		wbuAuftrag.setToolTipText(LPMain
				.getTextRespectUISPr("button.auftrag.tooltip"));
		wtfAuftragbezeichnung.setActivatable(false);
		wtfAuftragnummer.setActivatable(false);

		// Workingpanel
		jPanelWorkingOn = new JPanel(new MigLayout("wrap 5, hidemode 3",
				"[35%][40%][25%][25%]"));
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		createAbrufPanel();

		// 0. Zeile Abstaende
		jPanelWorkingOn.add(wlaBestellungsart, "right, growx");
		jPanelWorkingOn.add(wcoBestellungsart, "growx");
		jPanelWorkingOn.add(wlaBelegdatum, "skip, growx");
		jPanelWorkingOn.add(wdfBelegdatum, "growx, wrap");

		jPanelWorkingOn.add(jPanelAbrufBes, "growx, span");

		jPanelWorkingOn.add(wbuRahmenauswahl, "growx");
		jPanelWorkingOn.add(wtfAbrufausrahmencnr, "growx");
		jPanelWorkingOn.add(wtfAbrufausrahmenbez, "growx, span");

		jPanelWorkingOn.add(wbuLieferant, "growx");
		jPanelWorkingOn.add(wtfLieferantBestellung, "growx, span");

		jPanelWorkingOn.add(wbuAnsprechpartner, "growx");
		jPanelWorkingOn.add(wtfAnsprechpartner, "growx, span");

		jPanelWorkingOn.add(wbuLieferantRechnungsadresse, "growx");
		jPanelWorkingOn.add(wtfLieferantRechnungsadresse, "growx, span");

		jPanelWorkingOn.add(wbuAnforderer, "growx");
		jPanelWorkingOn.add(wtfAnforderer, "growx, span");

		jPanelWorkingOn.add(wbuLieferadresse, "growx");
		jPanelWorkingOn.add(wtfLieferadresse, "growx, span");

		jPanelWorkingOn.add(wbuAnsprechpartnerLieferadresse, "growx");
		jPanelWorkingOn.add(wtfAnsprechpartnerLieferadresse, "growx, span");

		jPanelWorkingOn.add(wbuAbholadresse, "growx");
		jPanelWorkingOn.add(wtfAbholadresse, "growx, span");

		jPanelWorkingOn.add(wbuAnsprechpartnerAbholadresse, "growx");
		jPanelWorkingOn.add(wtfAnsprechpartnerAbholadresse, "growx, span");

		jPanelWorkingOn.add(wlaProjekt, "growx");

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			jPanelWorkingOn.add(wtfProjekt, "growx");
			jPanelWorkingOn.add(wsfProjekt.getWrapperGotoButton(), "growx");
			jPanelWorkingOn
					.add(wsfProjekt.getWrapperTextField(), "growx, span");

		} else {

			jPanelWorkingOn.add(wtfProjekt, "growx, span");
		}

		jPanelWorkingOn.add(wlaWechselkursMandant2BS, "growx, span 3");
		jPanelWorkingOn.add(wlaWaehrung, "growx");
		jPanelWorkingOn.add(wcoWaehrungen, "growx");

		jPanelWorkingOn.add(wcbTeillieferung, "skip, growx, split 2");
		jPanelWorkingOn.add(wcbPoenale, "growx");
		jPanelWorkingOn.add(wlaLiefertermin, "growx, span 2");
		jPanelWorkingOn.add(wdfLiefertermin, "growx");

		jPanelWorkingOn.add(wbuKostenstelle, "growx");
		jPanelWorkingOn.add(wtfKostenstelleNummer, "growx");
		jPanelWorkingOn.add(wtfKostenstelleBezeichnung, "growx, span");

		jPanelWorkingOn.add(wlaLeihtage, "growx");
		jPanelWorkingOn.add(wnfLeihtage, "growx, wrap");

		jPanelWorkingOn.add(wlaLieferantenangebot, "growx");
		jPanelWorkingOn.add(wtfLieferantenangebot, "growx");
		jPanelWorkingOn.add(wlaAnfrage, "growx, span 2");
		jPanelWorkingOn.add(wtfAnfrage, "growx");

		jPanelWorkingOn.add(wbuAuftrag, "growx");
		jPanelWorkingOn.add(wtfAuftragnummer, "growx");
		jPanelWorkingOn.add(wtfAuftragbezeichnung, "growx, span");

		jPanelWorkingOn.add(wlaAbrufe, "growx");
		jPanelWorkingOn.add(wtaAbrufe, "growx, span");

		jPanelWorkingOn.add(wlaMahnsperreBis, "top, growx");
		jPanelWorkingOn.add(wdfMahnsperre, "top, growx");

		createAndSaveAndShowButton(
				"/com/lp/client/res/shoppingcart.png",
				LPMain.getTextRespectUISPr("bes.bestellung.neuerwareneingang.auswep"),
				TabbedPaneBestellung.MY_OWN_NEW_NEUER_WARENEINGANG_AUS_WEP,
				RechteFac.RECHT_BES_BESTELLUNG_CUD);

	}

	private void createAbrufPanel() throws ExceptionLP {

		wnfDivisor = new WrapperNumberField();
		wnfDivisor.setMaximumIntegerDigits(3);
		wnfDivisor.setFractionDigits(0);
		wnfDivisor.setMaximumValue(999);
		wnfDivisor.setMinimumValue(1);
		wnfDivisor.setHorizontalAlignment(SwingConstants.LEFT);

		wcbDivisor = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("bes.abruf_bes.divisor"));
		wcbRest = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("bes.abruf_bes.rest"));
		wcbManuellAbruf = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("bes.abruf_bes.manuel"));

		jbgAbrufBes = new ButtonGroup();
		jbgAbrufBes.add(wcbDivisor);
		jbgAbrufBes.add(wcbRest);
		jbgAbrufBes.add(wcbManuellAbruf);
		ParametermandantDto mandantParam = null;
		try {
			mandantParam = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_BESTELLUNG,
							ParameterFac.PARAMETER_ABRUFE_DEFAULT_ABRUFART_BESTELLUNG);
		} catch (Throwable e) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.hinweis"), LPMain
					.getTextRespectUISPr("lp.abrufemenge.parameter.inkorrekt"));
		}
		if (mandantParam != null) {
			String sParamValue = mandantParam.getCWert().toUpperCase();
			if (sParamValue.equals("REST")) {
				wcbManuellAbruf.setSelected(false);
				wcbDivisor.setSelected(false);
				wcbRest.setSelected(true);
			} else if (sParamValue.equals("MANUELL")) {
				wcbDivisor.setSelected(false);
				wcbRest.setSelected(false);
				wcbManuellAbruf.setSelected(true);
			} else if (sParamValue.equals("DIVISOR")) {
				wcbRest.setSelected(false);
				wcbManuellAbruf.setSelected(false);
				wcbDivisor.setSelected(true);
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("lp.abrufemenge.parameter.inkorrekt"));
			}
		}

		wlaLeerAbruf = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.abruf_bes"));
		wlaLeerAbruf.setHorizontalAlignment(SwingConstants.LEFT);

		jPanelAbrufBes = new JPanel(new GridBagLayout());

		jPanelAbrufBes.add(wlaLeerAbruf, new GridBagConstraints(0, 0, 1, 1,
				0.12, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(wcbDivisor, new GridBagConstraints(1, 0, 1, 1, 0.09,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(wnfDivisor, new GridBagConstraints(2, 0, 1, 1,
				0.005, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(new WrapperLabel(), new GridBagConstraints(3, 0, 1,
				1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(wcbManuellAbruf, new GridBagConstraints(4, 0, 1, 1,
				0.07, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(wcbRest, new GridBagConstraints(5, 0, 1, 1, 0.07,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(new WrapperLabel(), new GridBagConstraints(6, 0, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	private void setVisibleAbrufe(boolean bVisible) {
		wlaAbrufe.setVisible(bVisible);
		wtaAbrufe.setVisible(bVisible);
	}

	private void setVisibleLeihtage(boolean bVisible) {
		wlaLeihtage.setVisible(bVisible);
		wnfLeihtage.setVisible(bVisible);
	}

	private void setVisisbleAbrufausrahmen(boolean bVisible) {
		wbuRahmenauswahl.setVisible(bVisible);
		wtfAbrufausrahmencnr.setVisible(bVisible);
		wtfAbrufausrahmenbez.setVisible(bVisible);
	}

	private void setVisisbleMahnfelder(boolean bVisible) {
		wdfMahnsperre.setVisible(bVisible);
		wlaMahnsperreBis.setVisible(bVisible);
	}

	/**
	 * Einmalige Vorbelegungen.
	 * 
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {

		wcoBestellungsart.setMap(DelegateFactory.getInstance()
				.getBestellungServiceDelegate()
				.getBestellungsart(LPMain.getInstance().getUISprLocale()));

		// tmWaehrungen =
		// DelegateFactory.getInstance().getLocaleDelegate().getAllWaehrungen();
		// wcoWaehrungen.setMap(tmWaehrungen);
		prepareWaehrung();
	}

	/**
	 * Das Panel fuer die Anzeige oder Eingabe eines Datensatzes sauber
	 * aufbauen.
	 * 
	 * @throws Throwable
	 */
	protected void setDefaults() throws Throwable {

		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		nMengePos = 0;

		// die eventuell hinterlegte urspruengliche Waehrung zuruecksetzen
		waehrungDtoOld = new WaehrungDto();

		wlaWechselkursMandant2BS.setText("");

		if (getTabbedPaneBestellung().getBesDto() != null) {
			wcoBestellungsart
					.setKeyOfSelectedItem(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);

			getTabbedPaneBestellung().getBesDto().setBestellungartCNr(
					BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);

			// default waehrung kommt vom mandanten
			// String cNrMandantenwaehrung =
			// DelegateFactory.getInstance().getMandantDelegate
			// ().mandantFindByPrimaryKey(
			// LPMain.getInstance().getTheClient().getMandant()).getWaehrungCNr()
			// ;
			//
			// getTabbedPaneBestellung().getBestellungDto().
			// setWaehrungCNrBestellungswaehrung(
			// cNrMandantenwaehrung);
			// wcoWaehrungen.setKeyOfSelectedItem(cNrMandantenwaehrung);

			getTabbedPaneBestellung().getBesDto().setBTeillieferungMoeglich(
					Helper.boolean2Short(false));
		}
		ansprechpartnerDto = new AnsprechpartnerDto();
		ansprechpartnerLieferadresseDto = new AnsprechpartnerDto();
		anfordererDto = new PersonalDto();
		auftragDto = null;
		kostenstelleDto = null;
		getInternalFrameBestellung().getTabbedPaneBestellung().setRahmBesDto(
				new BestellungDto());

		setLieferantDtoRechnungsadresse(new LieferantDto());
		// Vorbelegungen im wenn neu geklickt
		anfordererDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
		getTabbedPaneBestellung().getBesDto().setPersonalIIdAnforderer(
				anfordererDto.getIId());
		wtfAnforderer.setText(anfordererDto.getPartnerDto().formatAnrede());
		// Default Lieferadresse ist die Adresse des aktuellen Mandanten
		Integer iPartnerIIdLIeferadresse = DelegateFactory.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant())
				.getPartnerIIdLieferadresse();
		lieferadresseDto = DelegateFactory.getInstance().getPartnerDelegate()
				.partnerFindByPrimaryKey(iPartnerIIdLIeferadresse);
		wtfLieferadresse.setText(lieferadresseDto.formatAnrede());

		setDefaultNeuAus();

		wnfDivisor.setInteger(1);
	}

	private void setDefaultNeuAus() throws Throwable {
		java.sql.Date dHeute = Helper.cutDate(new java.sql.Date(System
				.currentTimeMillis()));
		// Default-Liefertermin ist heute + 14 Tage.

		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_DEFAULT_LIEFERZEITVERSATZ);
		int iTagen = ((Integer) parametermandantDto.getCWertAsObject())
				.intValue();

		if (iTagen != -1) {
			java.sql.Date dVorgeschlagenerLiefertermin = Helper
					.addiereTageZuDatum(dHeute, iTagen);
			wdfLiefertermin.setDate(dVorgeschlagenerLiefertermin);

		} else {
			wdfLiefertermin.setDate(null);
		}
		wdfBelegdatum.setDate(dHeute);

		// PJ 16819
		parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_DEFAULT_TEILLIEFERUNG);
		boolean bTeillieferung = ((Boolean) parametermandantDto
				.getCWertAsObject());

		wcbTeillieferung.setSelected(bTeillieferung);

	}

	public void setDefaultsAusProjekt(Integer projektIId) throws Throwable {
		ProjektDto projektDto = DelegateFactory.getInstance()
				.getProjektDelegate().projektFindByPrimaryKey(projektIId);

		wtfProjekt.setText(projektDto.getCTitel());
		wsfProjekt.setKey(projektDto.getIId());

	}

	/**
	 * Hier werden die Events empfangen und verarbeitet.
	 * 
	 * @param eI
	 *            das Event
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRRahmenauswahl) {
				Object iIdRahmenbestellung = ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iIdRahmenbestellung != null) {
					getInternalFrameBestellung()
							.getTabbedPaneBestellung()
							.setRahmBesDto(
									DelegateFactory
											.getInstance()
											.getBestellungDelegate()
											.bestellungFindByPrimaryKey(
													(Integer) iIdRahmenbestellung));

					wtfAbrufausrahmencnr
							.setText(getInternalFrameBestellung()
									.getTabbedPaneBestellung().getRahmBesDto()
									.getCNr());

					wtfAbrufausrahmenbez.setText(getInternalFrameBestellung()
							.getTabbedPaneBestellung().getRahmBesDto()
							.getCBez());

					if (getInternalFrameBestellung().getTabbedPaneBestellung()
							.getBesDto() != null) {
						setBestellungIdforAbruf(getInternalFrameBestellung()
								.getTabbedPaneBestellung().getBesDto().getIId());
					}
					getInternalFrameBestellung().getTabbedPaneBestellung()
							.setBestellungDto(
									getInternalFrameBestellung()
											.getTabbedPaneBestellung()
											.getRahmBesDto());
					erzeugeAbrufbestellung();
				}
			} else if (e.getSource() == panelQueryFLRLieferant) {
				Integer pkLieferant = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (pkLieferant != null) {
					refreshLieferant(pkLieferant);
				}
			} else if (e.getSource() == panelQueryFLRRechnungsadresse) {
				Integer pkRechnungsadr = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				bestimmeUndZeigeRechnungsadresse(pkRechnungsadr);
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				bestimmeUndZeigeAnsprechpartner(iIdAnsprechpartner);
			} else if (e.getSource() == panelQueryFLRAnsprechpartnerLieferadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				bestimmeUndZeigeAnsprechpartnerLieferadresse(iIdAnsprechpartner);
			} else if (e.getSource() == panelQueryFLRAnsprechpartnerAbholadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				bestimmeUndZeigeAnsprechpartnerAbholadresse(iIdAnsprechpartner);
			} else if (e.getSource() == panelQueryFLRAnforderer) {
				Integer pkPersonal = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				bestimmeUndZeigeAnforderer(pkPersonal);
			} else if (e.getSource() == this.panelQueryFLRLieferadresse) {
				Integer pkPartner = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				bestimmeUndZeigeLieferadresse(pkPartner);
			} else if (e.getSource() == this.panelQueryFLRAbholadresse) {
				Integer pkPartner = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				bestimmeUndZeigeAbholadresse(pkPartner);
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKostenstelle((Integer) key);
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeAuftrag((Integer) key);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRRechnungsadresse) {
				wtfLieferantRechnungsadresse.setText("");
			} else if (e.getSource() == panelQueryFLRLieferadresse) {
				lieferadresseDto = null;
				wtfLieferadresse.setText("");
				components2Dto();
			} else if (e.getSource() == panelQueryFLRAbholadresse) {
				abholadresseDto = null;
				wtfAbholadresse.setText("");
				components2Dto();
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				holeAuftrag(null);
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				ansprechpartnerDto = new AnsprechpartnerDto();
				wtfAnsprechpartner.setText("");
			} else if (e.getSource() == panelQueryFLRAnforderer) {
				anfordererDto = new PersonalDto();
				wtfAnforderer.setText("");
			} else if (e.getSource() == panelQueryFLRAnsprechpartnerLieferadresse) {
				ansprechpartnerLieferadresseDto = new AnsprechpartnerDto();
				wtfAnsprechpartnerLieferadresse.setText("");
			} else if (e.getSource() == panelQueryFLRAnsprechpartnerAbholadresse) {
				ansprechpartnerAbholadresseDto = new AnsprechpartnerDto();
				wtfAnsprechpartnerAbholadresse.setText("");
			}
		}
	}

	private void setBestellungIdforAbruf(Integer iBestellungAbrufId) {
		this.iBestellungAbrufId = iBestellungAbrufId;
	}

	private Integer getBestellungIdforAbruf() {
		return iBestellungAbrufId;
	}

	private void holeKostenstelle(Integer key) throws Throwable {
		if (key != null) {
			kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
					.kostenstelleFindByPrimaryKey(key);
		} else {
			kostenstelleDto = null;
		}
		dto2ComponentsKostenstelle();
	}

	private void holeAuftrag(Integer key) throws Throwable {
		if (key != null) {
			auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(key);
		} else {
			auftragDto = null;
		}
		dto2ComponentsAuftrag();
	}

	private void dto2ComponentsKostenstelle() {
		if (kostenstelleDto != null) {
			wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		} else {
			wtfKostenstelleNummer.setText(null);
			wtfKostenstelleBezeichnung.setText(null);
		}
	}

	private void dto2ComponentsAuftrag() {
		if (auftragDto != null) {
			wtfAuftragnummer.setText(auftragDto.getCNr());
			wtfAuftragbezeichnung.setText(auftragDto
					.getCBezProjektbezeichnung());
		} else {
			wtfAuftragnummer.setText(null);
			wtfAuftragbezeichnung.setText(null);
		}
	}

	private void refreshLieferant(Integer iIdLieferantNewI) throws Throwable {

		String sWaehrungBSOld = getBestellungDto().getWaehrungCNr();
		waehrungDtoOld = DelegateFactory.getInstance().getLocaleDelegate()
				.waehrungFindByPrimaryKey(sWaehrungBSOld);

		LieferantDto lieferantDtoNew = DelegateFactory.getInstance()
				.getLieferantDelegate()
				.lieferantFindByPrimaryKey(iIdLieferantNewI);

		AnsprechpartnerDto dto = null;
		// Ansprechpartner vorbesetzen?
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_BESTELLUNG_ANSP_VORBESETZEN,
						ParameterFac.KATEGORIE_BESTELLUNG,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameter.getCWertAsObject()) {
			dto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(
							lieferantDtoNew.getPartnerIId());
		}
		if (dto != null) {
			bestimmeUndZeigeAnsprechpartner(dto.getIId());
		} else {
			bestimmeUndZeigeAnsprechpartner(null);
		}
		if (wechselkurseOKDlg(LPMain.getTheClient().getSMandantenwaehrung(),
				(String) wcoWaehrungen.getKeyOfSelectedItem())) {
			// Alle Wechselkurse OK; LF in BS uebernehmen
			if (getInternalFrameBestellung().getTabbedPaneBestellung()
					.getBesDto().getIId() != null) {
				// upd.
				// event. fragen, ob er die urspruengliche Waehrung beibehalten
				// moechte
				if (!takeNewWhgDlg(waehrungDtoOld.getCNr(),
						lieferantDtoNew.getWaehrungCNr())) {
					// die alte Belegwaehrung wird beibehalten ->
					// Waehrung und Wechselkurs zuruecksetzen
					wcoWaehrungen.setKeyOfSelectedItem(waehrungDtoOld.getCNr());
					getTabbedPaneBestellung().getBesDto().setWaehrungCNr(
							waehrungDtoOld.getCNr());
				} else {
					// die neue Belegwaehrung wird genommen
					if (wechselkurseOKDlg(LPMain.getTheClient()
							.getSMandantenwaehrung(),
							(String) wcoWaehrungen.getSelectedItem())) {
						wcoWaehrungen.setKeyOfSelectedItem(lieferantDtoNew
								.getWaehrungCNr());
						// Alle Wechselkurse OK; LF in BS uebernehmen
						lF2BS(lieferantDtoNew);
					} else {
						// Wechselkurse NICHT OK; alter LF bleibt
						wcoWaehrungen.setKeyOfSelectedItem(waehrungDtoOld
								.getCNr());
						getTabbedPaneBestellung().getBesDto().setWaehrungCNr(
								waehrungDtoOld.getCNr());
					}
				}
			} else {
				// neu: LF uebernehmen
				lF2BS(lieferantDtoNew);
			}
		}
	}

	private void lF2BS(LieferantDto lieferantDtoNew) throws Exception,
			Throwable {

		// Lieferadresse
		getTabbedPaneBestellung().getBesDto().setLieferantIIdBestelladresse(
				lieferantDtoNew.getIId());
		getInternalFrameBestellung().getTabbedPaneBestellung().setLieferantDto(
				lieferantDtoNew);

		// Rechnungsadresse
		PartnerDto partnerDto = lieferantDtoNew.getPartnerDto();
		if (partnerDto == null) {
			throw new Exception("partnerDto == null");
		}

		wtfLieferantBestellung.setText(partnerDto.formatFixTitelName1Name2());
		// wenn LF keine Rechnungsadresse hat dann wird Hauptadresse genommen
		if (lieferantDtoNew.getPartnerRechnungsadresseDto() == null) {
			wtfLieferantRechnungsadresse.setText(partnerDto
					.formatFixTitelName1Name2());
			// SP1759
			getTabbedPaneBestellung().getBesDto()
					.setLieferantIIdRechnungsadresse(lieferantDtoNew.getIId());

			setLieferantDtoRechnungsadresse(lieferantDtoNew);
		} else {
			wtfLieferantRechnungsadresse
					.setText(lieferantDtoNew.getPartnerRechnungsadresseDto()
							.formatFixTitelName1Name2());

			LieferantDto lfDto = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByiIdPartnercNrMandantOhneExc(
							lieferantDtoNew.getPartnerRechnungsadresseDto()
									.getIId(),
							LPMain.getTheClient().getMandant());
			if (lfDto != null) {
				// SP1759
				getTabbedPaneBestellung().getBesDto()
						.setLieferantIIdRechnungsadresse(lfDto.getIId());
				setLieferantDtoRechnungsadresse(lfDto);

			}

		}

		if (kostenstelleDto == null) {
			holeKostenstelle(lieferantDtoNew.getIIdKostenstelle());
		}
		// Lieferantenwaehrung vorbesetzen
		if (lieferantDtoNew.getWaehrungCNr() != null) {
			wcoWaehrungen
					.setKeyOfSelectedItem(lieferantDtoNew.getWaehrungCNr());
		}
	}

	private boolean takeNewWhgDlg(String sWaehrungOldI, String sWaehrungNewI) {

		boolean bTakeNewWaehrung = true;
		if (sWaehrungOldI != null && !sWaehrungOldI.equals(sWaehrungNewI)) {
			final int indexWaehrungOriCNr = 0;
			final int indexWaehrungNeuCNr = 1;
			final int iAnzahlOptionen = 2;

			Object[] aOptionen = new Object[iAnzahlOptionen];
			aOptionen[indexWaehrungOriCNr] = sWaehrungOldI;
			aOptionen[indexWaehrungNeuCNr] = sWaehrungNewI;

			int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.waehrungunterschiedlich"),
					LPMain.getTextRespectUISPr("lp.frage"), aOptionen,
					aOptionen[0]);

			if (iAuswahl == indexWaehrungOriCNr) {
				// die Belegwaehrung wird beibehalten -> Waehrung und
				// Wechselkurs zuruecksetzen
				bTakeNewWaehrung = false;
			}
		}
		return bTakeNewWaehrung;
	}

	protected void refreshLabelKurs(BigDecimal bdWechselkursMandant2BS)
			throws Throwable {

		Object pattern[] = {
				LPMain.getTheClient().getSMandantenwaehrung(),
				LPMain.getTextRespectUISPr("lp.mandantwaehrung"),
				Helper.formatZahl(bdWechselkursMandant2BS, 6, LPMain
						.getInstance().getUISprLocale()),
				wcoWaehrungen.getKeyOfSelectedItem(),
				LPMain.getTextRespectUISPr("bes.waehrung") };
		String sText = LPMain.getTextRespectUISPr("bes.wechselkurs");
		sText = MessageFormat.format(sText, pattern);
		wlaWechselkursMandant2BS.setText(sText);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param eI
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent eI) throws Throwable {

		if (eI.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT_BESTELLUNG)) {
			wtfAnsprechpartner.setText(null);
			dialogQueryLieferant();
		} else if (eI.getActionCommand().equals(
				ACTION_SPECIAL_RECHNUNGSADRESSE_BESTELLUNG)) {
			dialogQueryRechnungsadresse();
		} else if (eI.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT)) {
			if (wtfLieferantBestellung.getText() != null) {
				dialogQueryAnsprechpartner();
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("er.error.zuerst_lieferant_waehlen"));
			}
		} else if (eI.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE)) {
			if (wtfLieferadresse.getText() != null) {
				dialogQueryAnsprechpartnerLieferadresse();
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("er.error.zuerst_lieferadresse_waehlen"));
			}
		} else if (eI.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_ABHOLADRESSE)) {
			if (wtfAbholadresse.getText() != null) {
				dialogQueryAnsprechpartnerAbholadresse();
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("best.error.zuerst_abholadresse_waehlen"));
			}
		} else if (eI.getActionCommand().equals(ACTION_SPECIAL_ANFORDERER)) {
			dialogQueryAnforderer();
		} else if (eI.getActionCommand().equals(ACTION_SPECIAL_LIEFERADRESSE)) {
			wtfAnsprechpartnerLieferadresse.setText(null);
			dialogQueryLieferadresse();
		} else if (eI.getActionCommand().equals(ACTION_SPECIAL_ABHOLADRESSE)) {
			wtfAnsprechpartnerAbholadresse.setText(null);
			dialogQueryAbholadresse();
		} else if (eI.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle();
		} else if (eI.getActionCommand().equals(ACTION_SPECIAL_RAHMENAUSWAHL)) {
			dialogQueryRahmenbestellungFromListe();
		} else if (eI.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG)) {
			dialogQueryAuftrag();
		} else if (eI.getActionCommand().equals(
				ACTION_SPECIAL_BESTELLUNGART_CHANGED)) {
			bestellungsartChanged();
		} else if (eI.getActionCommand().equals(
				TabbedPaneBestellung.MY_OWN_NEW_NEUER_WARENEINGANG_AUS_WEP)) {
			// Neue Wareneingang aus Dialog anlegen
			DialogNeuerWareneingang dialog = new DialogNeuerWareneingang(
					getTabbedPaneBestellung());
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(dialog);
			dialog.setVisible(true);
		}
	}

	private void bestellungsartChanged() throws Throwable {

		/** @todo JO qs PJ 4868 */
		if (wcoBestellungsart.getKeyOfSelectedItem().equals(
				BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
			wlaLiefertermin.setText(LPMain
					.getTextRespectUISPr("lp.rahmentermin"));
			setVisisbleMahnfelder(false);
		} else {
			wlaLiefertermin.setText(LPMain
					.getTextRespectUISPr("label.liefertermin"));
		}
		bAbrufbesUndNeu = false;
		if (wcoBestellungsart.getKeyOfSelectedItem().equals(
				BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
			setVisisbleAbrufausrahmen(true);

			wtfAbrufausrahmencnr.setMandatoryField(true);

			bAbrufbesUndNeu = (getTabbedPaneBestellung() != null)
					&& (getTabbedPaneBestellung().getBesDto() != null)
					&& (getTabbedPaneBestellung().getBesDto().getIId() == null);

			wlaLeer.setVisible(!bAbrufbesUndNeu);
			wnfDivisor.setMandatoryField(bAbrufbesUndNeu);
			jPanelAbrufBes.setVisible(bAbrufbesUndNeu);
			wnfDivisor.getText();
		} else {
			if (getInternalFrameBestellung().getTabbedPaneBestellung() != null) {
				getInternalFrameBestellung().getTabbedPaneBestellung()
						.setRahmBesDto(new BestellungDto());
			}

			wtfAbrufausrahmencnr.setText("");
			wtfAbrufausrahmencnr.setMandatoryField(false);
			wtfAbrufausrahmenbez.setText("");

			setVisisbleAbrufausrahmen(false);

			wnfDivisor.setMandatoryField(false);
			jPanelAbrufBes.setVisible(false);
		}

		if (wcoBestellungsart.getKeyOfSelectedItem().equals(
				BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
			setVisibleAbrufe(true);
		} else {
			setVisibleAbrufe(false);
		}

		setVisibleLeihtage(wcoBestellungsart.getKeyOfSelectedItem().equals(
				BestellungFac.BESTELLUNGART_LEIHBESTELLUNG_C_NR));
	}

	private void dialogQueryRahmenbestellungFromListe() throws Throwable {
		Integer rahmbesIId = null;
		if (getInternalFrameBestellung().getTabbedPaneBestellung()
				.getRahmBesDto() != null) {
			rahmbesIId = getInternalFrameBestellung().getTabbedPaneBestellung()
					.getRahmBesDto().getIId();
		}
		FilterKriterium[] fk = BestellungFilterFactory.getInstance()
				.createFKRahmenbestellungenEinesMandanten();
		panelQueryFLRRahmenauswahl = BestellungFilterFactory.getInstance()
				.createPanelFLRBestellung(getInternalFrame(), false, false, fk,
						rahmbesIId);
		new DialogQuery(panelQueryFLRRahmenauswahl);
	}

	/**
	 * Dialogfenster zur Anfordererauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryAnforderer() throws Throwable {
		if (anfordererDto != null) {
			panelQueryFLRAnforderer = PersonalFilterFactory.getInstance()
					.createPanelFLRPersonal(getInternalFrame(), true, false,
							anfordererDto.getIId());
			new DialogQuery(panelQueryFLRAnforderer);
		} else {
			panelQueryFLRAnforderer = PersonalFilterFactory.getInstance()
					.createPanelFLRPersonal(getInternalFrame(), true, false,
							null);
			new DialogQuery(panelQueryFLRAnforderer);
		}
	}

	/**
	 * Dialogfenster zur Lieferadressenauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryLieferadresse() throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_LEEREN };

		panelQueryFLRLieferadresse = new PanelQueryFLR(
				null,
				null,
				QueryParameters.UC_ID_PARTNER,
				aWhichButtonIUse,
				getInternalFrameBestellung(),
				LPMain.getTextRespectUISPr("bes.title.lieferadresseauswahlliste"));

		panelQueryFLRLieferadresse
				.befuellePanelFilterkriterienDirekt(PartnerFilterFactory
						.getInstance().createFKDPartnerName(),
						PartnerFilterFactory.getInstance()
								.createFKDPartnerLandPLZOrt());

		new DialogQuery(panelQueryFLRLieferadresse);
	}

	private void dialogQueryAbholadresse() throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_LEEREN };

		panelQueryFLRAbholadresse = new PanelQueryFLR(
				null,
				null,
				QueryParameters.UC_ID_PARTNER,
				aWhichButtonIUse,
				getInternalFrameBestellung(),
				LPMain.getTextRespectUISPr("bes.title.abholadresseauswahlliste"));

		panelQueryFLRAbholadresse
				.befuellePanelFilterkriterienDirekt(PartnerFilterFactory
						.getInstance().createFKDPartnerName(),
						PartnerFilterFactory.getInstance()
								.createFKDPartnerLandPLZOrt());

		new DialogQuery(panelQueryFLRAbholadresse);
	}

	/**
	 * Dialogfenster zur Lieferantenauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryLieferant() throws Throwable {
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
				.createPanelFLRLieferantGoto(
						getInternalFrame(),
						getTabbedPaneBestellung().getBesDto()
								.getLieferantIIdBestelladresse(), true, false);
		new DialogQuery(panelQueryFLRLieferant);
	}

	/**
	 * Dialogfenster zur Ansprechpartnerauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryAnsprechpartner() throws Throwable {
		Integer ansprechpartnerIId = getTabbedPaneBestellung().getBesDto()
				.getAnsprechpartnerIId();
		panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
				.createPanelFLRAnsprechpartner(
						getInternalFrame(),
						getInternalFrameBestellung().getTabbedPaneBestellung()
								.getLieferantDto().getPartnerDto().getIId(),
						ansprechpartnerIId, true, true);
		new DialogQuery(panelQueryFLRAnsprechpartner);
	}

	private void dialogQueryAnsprechpartnerLieferadresse() throws Throwable {
		Integer ansprechpartnerIId = getTabbedPaneBestellung().getBesDto()
				.getAnsprechpartnerIIdLieferadresse();
		panelQueryFLRAnsprechpartnerLieferadresse = PartnerFilterFactory
				.getInstance().createPanelFLRAnsprechpartner(
						getInternalFrame(), lieferadresseDto.getIId(),
						ansprechpartnerIId, true, true);
		new DialogQuery(panelQueryFLRAnsprechpartnerLieferadresse);
	}

	private void dialogQueryAnsprechpartnerAbholadresse() throws Throwable {
		Integer ansprechpartnerIId = getTabbedPaneBestellung().getBesDto()
				.getAnsprechpartnerIIdAbholadresse();
		panelQueryFLRAnsprechpartnerAbholadresse = PartnerFilterFactory
				.getInstance().createPanelFLRAnsprechpartner(
						getInternalFrame(), abholadresseDto.getIId(),
						ansprechpartnerIId, true, true);
		new DialogQuery(panelQueryFLRAnsprechpartnerAbholadresse);
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		if (exfc.getICode() == EJBExceptionLP.FEHLER_KEIN_PARTNER_GEWAEHLT) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain
					.getTextRespectUISPr("lp.error.lieferantnichtgewaehlt"));
		} else {
			bErrorErkannt = false;
		}
		return bErrorErkannt;
	}

	/**
	 * Dialogfenster zur Lieferantenauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryRechnungsadresse() throws Throwable {
		panelQueryFLRRechnungsadresse = PartnerFilterFactory
				.getInstance()
				.createPanelFLRLieferantGoto(
						getInternalFrame(),
						getTabbedPaneBestellung().getBesDto()
								.getLieferantIIdRechnungsadresse(),
						true,
						false,
						LPMain.getTextRespectUISPr("title.rechnungsadresseauswahlliste"));
		new DialogQuery(panelQueryFLRRechnungsadresse);
	}

	public void dialogQueryKostenstelle() throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	// private void checkBestellungsartAenderungBeiRahmenBestellung()
	// throws Throwable {
	//
	// BestellungDto abrufBestellungDto[] = null;
	// DelegateFactory.getInstance().getBestellungDelegate().
	// abrufBestellungenfindByRahmenbestellung(this.getTabbedPaneBestellung().
	// getBestellungDto().getIId());
	//
	// if (abrufBestellungDto.length > 0) {
	// DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
	// "lp.error"),
	// LPMain.getInstance().getTextRespectUISPr(
	// "bes.nichtloeschenweilreferenzaufabruf"));
	// return;
	// }
	// }

	/**
	 * eine rahmen-, abruf-, leih-, oder normale bestellung abspeichern
	 * 
	 * @param e
	 *            der Save Button schickt dieses Event
	 * @param bNeedNoSaveI
	 *            flag
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			BestellungDto besDto = getTabbedPaneBestellung().getBesDto();
			if (besDto.getBestellungartCNr() != null) {

				// PJ 15052
				if (Helper.cutTimestamp(besDto.getDLiefertermin()).before(
						Helper.cutDate(besDto.getDBelegdatum()))) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.warning"),
									LPMain.getTextRespectUISPr("bes.warning.lieferterminvorbelegdatum"));
				}

				Integer iIdBes = null;

				if (besDto.getBestellungartCNr().equals(
						BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
					// --ist AbrufBes
					if (wtfAbrufausrahmencnr.getText() == null) {
						// --keine abrufbes mehr -> rahmenbezug loeschen
						besDto.setIBestellungIIdRahmenbestellung(null);
					}
				}

				boolean bNeueBestellung;

				if (besDto.getIId() == null) {
					bNeueBestellung = true;
					// create
					besDto.setMandantCNr(LPMain.getTheClient().getMandant());
					addExtraDaten(besDto);
					iIdBes = DelegateFactory.getInstance()
							.getBestellungDelegate().createBestellung(besDto);
					besDto.setIId(iIdBes);
					setKeyWhenDetailPanel(iIdBes);
					DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.pruefeLieferant(
									besDto.getLieferantIIdBestelladresse());

					getTabbedPaneBestellung().setBestellungDto(
							DelegateFactory.getInstance()
									.getBestellungDelegate()
									.bestellungFindByPrimaryKey(iIdBes));

				} else {
					bNeueBestellung = false;

					// SP1141
					boolean bUpdate = true;
					BestellungDto aDtoVorhanden = DelegateFactory.getInstance()
							.getBestellungDelegate()
							.bestellungFindByPrimaryKey(besDto.getIId());
					if (!aDtoVorhanden.getLieferantIIdBestelladresse().equals(
							besDto.getLieferantIIdBestelladresse())) {

						DialogGeaenderteKonditionenEK dialog = new DialogGeaenderteKonditionenEK(
								besDto, besDto.getLieferantIIdBestelladresse(),
								getInternalFrame());
						LPMain.getInstance().getDesktop()
								.platziereDialogInDerMitteDesFensters(dialog);

						if (dialog.bKonditionenUnterschiedlich == true) {
							dialog.setVisible(true);

							if (dialog.bAbgebrochen == false) {
								besDto = (BestellungDto) dialog.getBelegDto();
							} else {
								bUpdate = false;
							}
						}

					}

					// update
					if (!BestellungFac.BESTELLSTATUS_STORNIERT.equals(besDto
							.getStatusCNr())) {
						// Wenn Status nicht storniert muss tStorniert null sein
						besDto.setTStorniert(null);
					}
					if (bUpdate == true) {
						DelegateFactory.getInstance().getBestellungDelegate()
								.updateBestellung(besDto);
					}
					setKeyWhenDetailPanel(besDto.getIId());
				}

				super.eventActionSave(e, true); // ok
				getInternalFrame().setKeyWasForLockMe(
						getKeyWhenDetailPanel() + "");
				eventYouAreSelected(false); // ok

				// Wenn eine neue Abrufbestellung angelegt wurde, schalten wir
				// auf die "Sicht Rahmen" um.
				if (bNeueBestellung
						&& besDto
								.getBestellungartCNr()
								.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
					// --ist AbrufBes
					workoutAbrufBes(iIdBes);
				}
			}
		}
	}

	private void workoutAbrufBes(Integer iIdAbrufBesI) throws ExceptionLP,
			Throwable {

		if (wcbDivisor.isSelected() || wcbRest.isSelected()) {
			if (wcbRest.isSelected()) {
				// rest ist wie divisor mit 1
				wnfDivisor.setInteger(1);
			}

			int iDivisor = wnfDivisor.getInteger();

			DelegateFactory.getInstance().getBestellungDelegate()
					.erzeugeAbrufpositionen(iIdAbrufBesI, iDivisor);
			getTabbedPaneBestellung().refreshBestellungPositionSichtRahmen();
			getTabbedPaneBestellung().setSelectedComponent(
					getTabbedPaneBestellung()
							.getPanelBestellungPositionSichtRahmenSP6());
			getTabbedPaneBestellung()
					.getPanelBestellungPositionSichtRahmenSP6()
					.eventYouAreSelected(false);

		}

		else if (wcbManuellAbruf.isSelected()) {
			// alle bespos mit restmenge in sicht rahmen anzeigen
			getTabbedPaneBestellung().refreshBestellungPositionSichtRahmen();
			getTabbedPaneBestellung().setSelectedComponent(
					getTabbedPaneBestellung()
							.getPanelBestellungPositionSichtRahmenSP6());
			getTabbedPaneBestellung()
					.getPanelBestellungPositionSichtRahmenSP6()
					.eventYouAreSelected(false);
		}
	}

	private void addExtraDaten(BestellungDto besDto) {

		if (besDto.getFAllgemeinerRabattsatz() == null) {
			// kein rabatt->mit 0 vorbesetzen.
			besDto.setFAllgemeinerRabattsatz(new Double(0));
		}

		besDto.setZahlungszielIId(getInternalFrameBestellung()
				.getTabbedPaneBestellung().getLieferantDto()
				.getZahlungszielIId());
		besDto.setLieferartIId(getInternalFrameBestellung()
				.getTabbedPaneBestellung().getLieferantDto().getLieferartIId());
		besDto.setSpediteurIId(getInternalFrameBestellung()
				.getTabbedPaneBestellung().getLieferantDto().getIdSpediteur());
	}

	protected void dto2Components() throws Throwable {

		wdfMahnsperre.setDate(getBestellungDto().getTMahnsperreBis());
		wsfProjekt.setKey(getBestellungDto().getProjektIId());
		if (getBestellungDto().getAnfrageIId() != null) {
			AnfrageDto anfrageDto = DelegateFactory
					.getInstance()
					.getAnfrageDelegate()
					.anfrageFindByPrimaryKey(getBestellungDto().getAnfrageIId());
			wtfAnfrage.setText(anfrageDto.getCNr());
		} else {
			wtfAnfrage.setText(null);
		}

		holeKostenstelle(getTabbedPaneBestellung().getBesDto()
				.getKostenstelleIId());

		wtfProjekt.setText(getTabbedPaneBestellung().getBesDto().getCBez());
		wtfLieferantenangebot.setText(getTabbedPaneBestellung().getBesDto()
				.getCLieferantenangebot());

		wcbPoenale
				.setShort(getTabbedPaneBestellung().getBesDto().getBPoenale());

		wcoBestellungsart.setKeyOfSelectedItem(getTabbedPaneBestellung()
				.getBesDto().getBestellungartCNr());

		/*
		 * PJ 13245 if
		 * (getTabbedPaneBestellung().getBesDto().getBestellungartCNr().equals(
		 * BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) { wdfBelegdatum
		 * .setDate(new java.sql.Date(System.currentTimeMillis())); } else {
		 */
		wdfBelegdatum.setDate(getTabbedPaneBestellung().getBesDto()
				.getDBelegdatum());

		Timestamp liefertermin = getTabbedPaneBestellung().getBesDto()
				.getDLiefertermin();
		wdfLiefertermin.setDate(new java.sql.Date(
				(liefertermin == null) ? System.currentTimeMillis()
						: liefertermin.getTime()));

		// Bestelladresse bestimmen
		Integer iIdBestelladresse = getTabbedPaneBestellung().getBesDto()
				.getLieferantIIdBestelladresse();

		if (iIdBestelladresse != null) {

			getInternalFrameBestellung().getTabbedPaneBestellung()
					.setLieferantDto(
							DelegateFactory
									.getInstance()
									.getLieferantDelegate()
									.lieferantFindByPrimaryKey(
											iIdBestelladresse));

			LieferantDto lieferantDto = getInternalFrameBestellung()
					.getTabbedPaneBestellung().getLieferantDto();
			PartnerDto partnerDto = lieferantDto.getPartnerDto();
			if (partnerDto != null) {
				wtfLieferantBestellung.setText(partnerDto
						.formatFixTitelName1Name2());
				// wenn LF keine Rechnungsadresse hat dann wird Hauptadresse
				// genommen
				if (lieferantDto.getPartnerRechnungsadresseDto() == null) {
					wtfLieferantRechnungsadresse.setText(partnerDto
							.formatFixTitelName1Name2());
				} else {
					wtfLieferantRechnungsadresse.setText(lieferantDto
							.getPartnerRechnungsadresseDto()
							.formatFixTitelName1Name2());
				}
				if (kostenstelleDto == null) {
					holeKostenstelle(lieferantDto.getIIdKostenstelle());
				}

				// GOTO Lieferant Button Ziel setzen
				wbuLieferant.setOKey(lieferantDto.getIId());
			}

			getTabbedPaneBestellung().getBesDto()
					.setLieferantIIdBestelladresse(iIdBestelladresse);

		}

		wnfLeihtage.setInteger(getTabbedPaneBestellung().getBesDto()
				.getILeihtage());
		if (getTabbedPaneBestellung().getBesDto().getBTeillieferungMoeglich() != null) {
			wcbTeillieferung.setShort(getTabbedPaneBestellung().getBesDto()
					.getBTeillieferungMoeglich());
		}

		Integer iIdLieferadresse = getTabbedPaneBestellung().getBesDto()
				.getPartnerIIdLieferadresse();
		bestimmeUndZeigeLieferadresse(iIdLieferadresse);

		Integer iIdAbholadresse = getTabbedPaneBestellung().getBesDto()
				.getPartnerIIdAbholadresse();
		bestimmeUndZeigeAbholadresse(iIdAbholadresse);

		// Ansprechpartner bestimmen
		Integer iIdAnsprechpartner = getTabbedPaneBestellung().getBesDto()
				.getAnsprechpartnerIId();
		bestimmeUndZeigeAnsprechpartner(iIdAnsprechpartner);

		Integer iIdAnforderer = getTabbedPaneBestellung().getBesDto()
				.getPersonalIIdAnforderer();
		bestimmeUndZeigeAnforderer(iIdAnforderer);

		Integer iIdAnsprechpartnerLieferadresse = getTabbedPaneBestellung()
				.getBesDto().getAnsprechpartnerIIdLieferadresse();
		bestimmeUndZeigeAnsprechpartnerLieferadresse(iIdAnsprechpartnerLieferadresse);

		Integer iIdAnsprechpartnerAbholadresse = getTabbedPaneBestellung()
				.getBesDto().getAnsprechpartnerIIdAbholadresse();
		bestimmeUndZeigeAnsprechpartnerAbholadresse(iIdAnsprechpartnerAbholadresse);

		wcoWaehrungen.setKeyOfSelectedItem(getTabbedPaneBestellung()
				.getBesDto().getWaehrungCNr());

		Double dWechselkursMandant2BS = getTabbedPaneBestellung().getBesDto()
				.getFWechselkursmandantwaehrungzubelegwaehrung();
		refreshLabelKurs(new BigDecimal(dWechselkursMandant2BS.doubleValue()));

		// Rechnungsadresse bestimmen
		Integer iIdRechnungsadresse = getTabbedPaneBestellung().getBesDto()
				.getLieferantIIdRechnungsadresse();
		bestimmeUndZeigeRechnungsadresse(iIdRechnungsadresse);

		Integer iIdBestellungRahmenbestellung = getTabbedPaneBestellung()
				.getBesDto().getIBestellungIIdRahmenbestellung();

		if (iIdBestellungRahmenbestellung != null) {
			getInternalFrameBestellung().getTabbedPaneBestellung()
					.setRahmBesDto(
							DelegateFactory
									.getInstance()
									.getBestellungDelegate()
									.bestellungFindByPrimaryKey(
											iIdBestellungRahmenbestellung));

			wtfAbrufausrahmencnr.setText(getInternalFrameBestellung()
					.getTabbedPaneBestellung().getRahmBesDto().getCNr());
			wtfAbrufausrahmenbez.setText(getInternalFrameBestellung()
					.getTabbedPaneBestellung().getRahmBesDto().getCBez());
		}

		if (getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto()
				.getBestellungartCNr()
				.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
			// alle Abrufe anzeigen
			BestellungDto[] aAbrufbestellungDto = DelegateFactory
					.getInstance()
					.getBestellungDelegate()
					.abrufBestellungenfindByRahmenbestellung(
							getInternalFrameBestellung()
									.getTabbedPaneBestellung().getBesDto()
									.getIId());

			wtaAbrufe.setText(formatBestellungen(aAbrufbestellungDto));
		}

		holeAuftrag(getInternalFrameBestellung().getTabbedPaneBestellung()
				.getBesDto().getAuftragIId());

		refreshStatusbar();
	}

	private static String formatBestellungen(BestellungDto[] aBestellungDtoI) {
		String cFormat = "";

		// nach jeweils 4 Abrufen einen Zeilenumbruch einfuegen
		int iAnzahl = 0;

		if (aBestellungDtoI != null && aBestellungDtoI.length > 0) {
			for (int i = 0; i < aBestellungDtoI.length; i++) {
				cFormat += aBestellungDtoI[i].getCNr();
				iAnzahl++;

				if (iAnzahl == 5) {
					cFormat += "\n";
					iAnzahl = 0;
				} else {
					cFormat += " | ";
				}
			}
		}

		// if (cFormat.length() > 3) {
		// cFormat = cFormat.substring(0, cFormat.length() - 3);
		// }
		return cFormat;
	}

	/**
	 * Die Anrede fuer einen Ansprechpartner bauen. <br>
	 * Es muss keinen Ansprechpartner geben.
	 * 
	 * @param iIdAnsprechpartnerI
	 *            pk des Ansprechpartners
	 * @throws Throwable
	 */
	private void bestimmeUndZeigeAnsprechpartner(Integer iIdAnsprechpartnerI)
			throws Throwable {

		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerDto = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatFixTitelName1Name2());
			getTabbedPaneBestellung().getBesDto().setAnsprechpartnerIId(
					iIdAnsprechpartnerI);
		} else {
			getTabbedPaneBestellung().getBesDto().setAnsprechpartnerIId(null);
			ansprechpartnerDto = null;
			wtfAnsprechpartner.setText("");
		}
	}

	private void bestimmeUndZeigeAnsprechpartnerLieferadresse(
			Integer iIdAnsprechpartnerI) throws Throwable {

		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerLieferadresseDto = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);
			wtfAnsprechpartnerLieferadresse
					.setText(ansprechpartnerLieferadresseDto.getPartnerDto()
							.formatFixTitelName1Name2());
			getTabbedPaneBestellung().getBesDto()
					.setAnsprechpartnerIIdLieferadresse(iIdAnsprechpartnerI);
		} else {
			wtfAnsprechpartnerLieferadresse.setText("");
		}
	}

	private void bestimmeUndZeigeAnsprechpartnerAbholadresse(
			Integer iIdAnsprechpartnerI) throws Throwable {

		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerAbholadresseDto = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);
			wtfAnsprechpartnerAbholadresse
					.setText(ansprechpartnerAbholadresseDto.getPartnerDto()
							.formatFixTitelName1Name2());
			getTabbedPaneBestellung().getBesDto()
					.setAnsprechpartnerIIdAbholadresse(iIdAnsprechpartnerI);
		} else {
			wtfAnsprechpartnerAbholadresse.setText("");
		}
	}

	/**
	 * Die Anrede fuer einen Ansprechpartner bauen. Es muss keinen
	 * Ansprechpartner geben.
	 * 
	 * @param iIdRechnungsadresseI
	 *            pk des Ansprechpartners
	 * @throws Throwable
	 */
	private void bestimmeUndZeigeRechnungsadresse(Integer iIdRechnungsadresseI)
			throws Throwable {
		if (iIdRechnungsadresseI != null) {
			try {
				setLieferantDtoRechnungsadresse(DelegateFactory.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(iIdRechnungsadresseI));
				this.wtfLieferantRechnungsadresse
						.setText(getLieferantDtoRechnungsadresse()
								.getPartnerDto().formatFixTitelName1Name2());
				getTabbedPaneBestellung().getBesDto()
						.setLieferantIIdRechnungsadresse(iIdRechnungsadresseI);
			} catch (Throwable t) {
				/**
				 * @todo so nicht
				 */
				getTabbedPaneBestellung().setRechnungPartnerDto(
						DelegateFactory.getInstance().getPartnerDelegate()
								.partnerFindByPrimaryKey(iIdRechnungsadresseI));
				this.wtfLieferantRechnungsadresse
						.setText(getTabbedPaneBestellung()
								.getRechnungPartnerDto()
								.formatFixTitelName1Name2());
			}
		}
	}

	private void bestimmeUndZeigeAnforderer(Integer iIdAnfordererI)
			throws Throwable {
		if (iIdAnfordererI != null) {
			anfordererDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(iIdAnfordererI);
			if (anfordererDto != null && anfordererDto.getIId() != null) {
				wtfAnforderer.setText(anfordererDto.getPartnerDto()
						.formatAnrede());
			}
		} else {
			wtfAnforderer.setText("");
		}
	}

	private void bestimmeUndZeigeLieferadresse(Integer iIdLieferadresse)
			throws Throwable {

		if (iIdLieferadresse != null) {
			lieferadresseDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(iIdLieferadresse);
			if (lieferadresseDto != null && lieferadresseDto.getIId() != null) {
				wtfLieferadresse.setText(lieferadresseDto.formatAnrede());
			}
		} else {
			wtfLieferadresse.setText("");
		}
	}

	private void bestimmeUndZeigeAbholadresse(Integer iIdAbholadresse)
			throws Throwable {

		if (iIdAbholadresse != null) {
			abholadresseDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(iIdAbholadresse);
			if (abholadresseDto != null && abholadresseDto.getIId() != null) {
				wtfAbholadresse.setText(abholadresseDto.formatAnrede());
			}
		} else {
			wtfAbholadresse.setText("");
		}
	}

	/**
	 * Alle Bestellungsdaten aus dem Panel sammeln.
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {

		getTabbedPaneBestellung().getBesDto().setTMahnsperreBis(
				wdfMahnsperre.getDate());

		getTabbedPaneBestellung().getBesDto().setCBez(wtfProjekt.getText());
		getTabbedPaneBestellung().getBesDto().setCLieferantenangebot(
				wtfLieferantenangebot.getText());

		getTabbedPaneBestellung().getBesDto().setDBelegdatum(
				wdfBelegdatum.getDate());
		getTabbedPaneBestellung().getBesDto()
				.setBPoenale(wcbPoenale.getShort());
		getTabbedPaneBestellung().getBesDto().setBestellungartCNr(
				(String) wcoBestellungsart.getKeyOfSelectedItem());

		if (getInternalFrameBestellung().getTabbedPaneBestellung()
				.getRahmBesDto() != null) {
			if (wcoBestellungsart.getKeyOfSelectedItem().equals(
					BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
				getTabbedPaneBestellung().getBesDto()
						.setIBestellungIIdRahmenbestellung(
								getInternalFrameBestellung()
										.getTabbedPaneBestellung()
										.getRahmBesDto().getIId());
			} else {
				getTabbedPaneBestellung().getBesDto()
						.setIBestellungIIdRahmenbestellung(null);
			}
		}

		feldBelegung();

		getTabbedPaneBestellung().getBesDto().setILeihtage(
				wnfLeihtage.getInteger());
		getTabbedPaneBestellung().getBesDto().setWaehrungCNr(
				(String) wcoWaehrungen.getKeyOfSelectedItem());

		getTabbedPaneBestellung().getBesDto()
				.setFWechselkursmandantwaehrungzubelegwaehrung(
						new Double(bdWechselkursMandant2BS.doubleValue()));

		getTabbedPaneBestellung().getBesDto().setDLiefertermin(
				this.wdfLiefertermin.getTimestamp());
		if (kostenstelleDto != null) {
			getTabbedPaneBestellung().getBesDto().setKostenstelleIId(
					kostenstelleDto.getIId());
		} else {
			getTabbedPaneBestellung().getBesDto().setKostenstelleIId(null);
		}
		getTabbedPaneBestellung().getBesDto().setILeihtage(
				wnfLeihtage.getInteger());
		getTabbedPaneBestellung().getBesDto().setBTeillieferungMoeglich(
				wcbTeillieferung.getShort());
		getTabbedPaneBestellung().getBesDto().setProjektIId(
				(Integer) wsfProjekt.getOKey());

		if (auftragDto != null) {
			getTabbedPaneBestellung().getBesDto().setAuftragIId(
					auftragDto.getIId());
		} else {
			getTabbedPaneBestellung().getBesDto().setAuftragIId(null);
		}
	}

	private void feldBelegung() {
		if (getTabbedPaneBestellung().getLieferantDto() != null) {
			getTabbedPaneBestellung().getBesDto()
					.setLieferantIIdBestelladresse(
							getTabbedPaneBestellung().getLieferantDto()
									.getIId());
		}

		if (getLieferantDtoRechnungsadresse() != null) {
			getTabbedPaneBestellung().getBesDto()
					.setLieferantIIdRechnungsadresse(
							getLieferantDtoRechnungsadresse().getIId());
		}

		if (ansprechpartnerDto != null) {
			getTabbedPaneBestellung().getBesDto().setAnsprechpartnerIId(
					ansprechpartnerDto.getIId());
		}

		if (ansprechpartnerLieferadresseDto != null) {
			getTabbedPaneBestellung().getBesDto()
					.setAnsprechpartnerIIdLieferadresse(
							ansprechpartnerLieferadresseDto.getIId());
		}

		if (anfordererDto != null) {
			getTabbedPaneBestellung().getBesDto().setPersonalIIdAnforderer(
					anfordererDto.getIId());
		}

		if (lieferadresseDto != null) {
			getTabbedPaneBestellung().getBesDto().setPartnerIIdLieferadresse(
					lieferadresseDto.getIId());
		} else {
			getTabbedPaneBestellung().getBesDto().setPartnerIIdLieferadresse(
					null);
		}
		if (abholadresseDto != null) {
			getTabbedPaneBestellung().getBesDto().setPartnerIIdAbholadresse(
					abholadresseDto.getIId());
		} else {
			getTabbedPaneBestellung().getBesDto().setPartnerIIdAbholadresse(
					null);
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {
			// Neu.
			Integer oKey = getTabbedPaneBestellung().getBesDto().getIId();

			// setDefaults();

			if (oKey == null || oKey.equals(LPMain.getLockMeForNew())) {
				// Neu.
				String cNrMandantenwaehrung = LPMain.getTheClient()
						.getSMandantenwaehrung();
				getTabbedPaneBestellung().getBesDto().setWaehrungCNr(
						cNrMandantenwaehrung);
				wcoWaehrungen.setKeyOfSelectedItem(cNrMandantenwaehrung);
			} else {
				// Bestehende Bestellung anzeigen.
				getTabbedPaneBestellung().setBestellungDto(
						DelegateFactory.getInstance().getBestellungDelegate()
								.bestellungFindByPrimaryKey(oKey));

				dto2Components();

				if (getBestellungDto().getIId() != null) {
					nMengePos = DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.berechneAnzahlMengenbehafteteBestellpositionen(
									getBestellungDto().getIId());
				}
			}
		}

		getInternalFrameBestellung().getTabbedPaneBestellung().enablePanels(
				getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto(), true);

		refreshMyComponents();
	}

	private BestellungDto getBestellungDto() {
		return getTabbedPaneBestellung().getBesDto();
	}

	/**
	 * behandle ereignis neu.
	 * 
	 * @param eventObject
	 *            Ereignis.
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			// aus einer RahmBes eine AbrufBes erzeugen
			erzeugeAbrufbestellung();
		} else {
			// dto leeren und Defaults setzen.
			getInternalFrameBestellung().getTabbedPaneBestellung().resetDtos();
			setDefaults();
		}
		super.eventActionNew(eventObject, true, false);
	}

	/**
	 * Eine neue Abrufbestellung als Clone einer Rahmenbestellung erzeugen und
	 * anzeigen.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void erzeugeAbrufbestellung() throws Throwable {

		getInternalFrameBestellung().getTabbedPaneBestellung().setRahmBesDto(
				getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto());

		BestellungDto abrufBesDto = (BestellungDto) getInternalFrameBestellung()
				.getTabbedPaneBestellung().getRahmBesDto().clone();

		abrufBesDto
				.setBestellungartCNr(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR);

		// PJ 13245
		abrufBesDto.setDBelegdatum(new Date(System.currentTimeMillis()));
		// PJ 17487
		abrufBesDto.setTAenderungsbestellung(null);

		abrufBesDto
				.setIBestellungIIdRahmenbestellung(getInternalFrameBestellung()
						.getTabbedPaneBestellung().getBesDto().getIId());

		getInternalFrameBestellung().getTabbedPaneBestellung()
				.setBestellungDto(abrufBesDto);

		if (getBestellungIdforAbruf() != null) {
			getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto()
					.setIId(iBestellungAbrufId);
		}

		// den Clone anzeigen; Vorsicht: dieses Dto ist vorerst temporaer
		dto2Components();
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);

		getInternalFrameBestellung().getTabbedPaneBestellung().enablePanels(
				getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto(), true);

		refreshMyComponents();
	}

	public boolean wechselkurseOKDlg(String sWaehrungLFI, String sWaehrungBSI) {
		String sWaehrungVon = null;
		String sWaehrungNach = null;
		bdWechselkursMandant2BS = null;
		boolean bWOK = true;

		try {
			// 1 Check Mandant2BS
			sWaehrungVon = LPMain.getTheClient().getSMandantenwaehrung();
			sWaehrungNach = sWaehrungBSI;
			bdWechselkursMandant2BS = DelegateFactory.getInstance()
					.getLocaleDelegate()
					.getWechselkurs2(sWaehrungVon, sWaehrungNach);

			try {
				// 2 Check BS2LF only; use spaeter im BSPOS
				sWaehrungVon = sWaehrungLFI;
				sWaehrungNach = sWaehrungBSI;
				// DelegateFactory.getInstance().getLocaleDelegate().
				// getWechselkurs(
				// sWaehrungMandant,
				// sWaehrungNach).doubleValue();
			} catch (Throwable t) {
				bWOK = false;
			}

		} catch (Throwable t) {
			bWOK = false;
		}

		if (!bWOK) {
			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("bes.wahrung.exit"));
			try {
				mf.setLocale(LPMain.getTheClient().getLocUi());
			} catch (Throwable tDummy) {
				// nothing here
			}

			Object pattern[] = { sWaehrungVon + " " + sWaehrungNach };
			String sMsg = mf.format(pattern);

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"), sMsg);

			bdWechselkursMandant2BS = new BigDecimal(1);
		}

		if (getInternalFrameBestellung().getTabbedPaneBestellung() != null) {
			getInternalFrameBestellung().getTabbedPaneBestellung()
					.setbWechselkurseOK(bWOK);
		}
		return bWOK;
	}

	/**
	 * Drucke Bestllung.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {

		getTabbedPaneBestellung().printBestellung();

		eventYouAreSelected(false);

		getInternalFrameBestellung().getTabbedPaneBestellung().enablePanels(
				getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto(), true);
	}

	/**
	 * Goto von Bestllung.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	public void eventActionGoto(ActionEvent e) throws Throwable {
		System.out.println();

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (getInternalFrameBestellung().getTabbedPaneBestellung()
				.istAktualisierenBestellungErlaubt()) {
			Integer nWEs = null;
			if (getBestellungDto().getIId() != null) {
				nWEs = DelegateFactory.getInstance().getWareneingangDelegate()
						.getAnzahlWE(getBestellungDto().getIId());
				if (nWEs != null && nWEs.intValue() == 0) {
					getBestellungDto().setTGedruckt(null);
					getBestellungDto().setTStorniert(null);
					getBestellungDto().setStatusCNr(
							BestellungFac.BESTELLSTATUS_ANGELEGT);
					getBestellungDto().setTVersandzeitpunkt(null);
					DelegateFactory.getInstance().getBestellungDelegate()
							.updateBestellung(getBestellungDto());
				}
			}

			String cNrBSArt = getInternalFrameBestellung()
					.getTabbedPaneBestellung().getBesDto()
					.getBestellungartCNr();
			if (cNrBSArt
					.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
				BestellpositionDto[] besposDto = DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellpositionFindByBestellung(
								getInternalFrameBestellung()
										.getTabbedPaneBestellung().getBesDto()
										.getIId());
				if (besposDto.length != 0) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("bes.abrufbestellungkopfdatenaendern"));
					return;
				}
			}
			wechselkurseOKDlg(LPMain.getTheClient().getSMandantenwaehrung(),
					(String) wcoWaehrungen.getSelectedItem());
			refreshMyComponents();

			super.eventActionUpdate(aE, false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (getTabbedPaneBestellung().isBSangelegtDlg()) {
			if (!getTabbedPaneBestellung().getBesDto().getStatusCNr()
					.equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
				getInternalFrameBestellung().getTabbedPaneBestellung()
						.showStatusMessage("lp.warning",
								"bes.warning.beskannnichtstorniertwerden");
			} else {
				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.stornieren(
								getTabbedPaneBestellung().getBesDto().getIId());
				super.eventActionDelete(e, false, false); // die bestellung
				// existiert
				// weiterhin!
				eventYouAreSelected(false);
			}
		}

		// if (getInternalFrameBestellung().getTabbedPaneBestellung().
		// getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac.
		// BESTELLSTATUS_OFFEN) ||
		// getInternalFrameBestellung().getTabbedPaneBestellung().getBestellungDto
		// ().getBestellungsstatusCNr().
		// equals(BestellungFac.BESTELLSTATUS_BESTAETIGT)) {
		// getInternalFrameBestellung().getTabbedPaneBestellung().
		// enableWareneingangPanels();
		// getInternalFrameBestellung().getTabbedPaneBestellung().enablePanels(
		// null);
		// }
		// else {
		// getInternalFrameBestellung().getTabbedPaneBestellung().enableWE(false)
		// ;
		// getInternalFrameBestellung().getTabbedPaneBestellung().enableWEP(false
		// );
		// getInternalFrameBestellung().getTabbedPaneBestellung().enablePanels(
		// null);
		// }
		// getInternalFrameBestellung().getTabbedPaneBestellung().
		// enableSichtLieferantTermine();
	}

	private void refreshStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getTabbedPaneBestellung().getBesDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(getTabbedPaneBestellung().getBesDto()
				.getTAnlegen());
		setStatusbarPersonalIIdAendern(getTabbedPaneBestellung().getBesDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(getTabbedPaneBestellung().getBesDto()
				.getTAendern());

		setStatusbarStatusCNr(getTabbedPaneBestellung().getBesDto()
				.getStatusCNr());

		String status = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_BESTELLUNG,
						getTabbedPaneBestellung().getBesDto().getIId());
		if (status != null) {
			status = LPMain.getTextRespectUISPr("lp.versandstatus") + ": "
					+ status;
		}
		setStatusbarSpalte5(status);

	}

	public InternalFrameBestellung getInternalFrameBestellung() {
		return intFrame;
	}

	public TabbedPaneBestellung getTabbedPaneBestellung() {
		return getInternalFrameBestellung().getTabbedPaneBestellung();
	}

	private void setLieferantDtoRechnungsadresse(LieferantDto dto) {
		lieferantDtoRechnungsadresse = dto;
	}

	private LieferantDto getLieferantDtoRechnungsadresse() {
		return lieferantDtoRechnungsadresse;
	}

	private void dialogQueryAuftrag() throws Throwable {
		FilterKriterium[] fk = SystemFilterFactory.getInstance()
				.createFKMandantCNr();
		Integer auftragIId = null;
		if (auftragDto != null) {
			auftragIId = auftragDto.getIId();
		}
		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), true, true, fk,
						auftragIId);
		new DialogQuery(panelQueryFLRAuftrag);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLieferant;
	}

	private void refreshMyComponents() throws Throwable {

		String cNrStatus = getBestellungDto().getStatusCNr();
		int lockstate = getLockedstateDetailMainKey().getIState();

		boolean bEnableLoesche = getBestellungDto().getIId() != null
				&& (DelegateFactory.getInstance().getWareneingangDelegate()
						.getAnzahlWE(getBestellungDto().getIId()).intValue() <= 0)
				&& cNrStatus != null
				&& !cNrStatus.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
				&& !cNrStatus.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)
				&& lockstate != LOCK_FOR_EMPTY
				&& lockstate != LOCK_FOR_NEW
				&& getInternalFrameBestellung().getTabbedPaneBestellung()
						.isbWechselkurseOK();

		// loeschebutton
		LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
				PanelBasis.ACTION_DELETE);
		item.getButton().setEnabled(bEnableLoesche);

		boolean bEnableUpd = bEnableLoesche;

		// updatebutton
		item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_UPDATE);
		item.getButton().setEnabled(bEnableUpd);

		// statusbar
		refreshStatusbar();

		boolean bEnableBesArtKombo = (getBestellungDto().getIId() == null || getBestellungDto()
				.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT))
				&& nMengePos == 0;
		wcoBestellungsart.setActivatable(bEnableBesArtKombo);
		wcoBestellungsart.setEnabled(bEnableBesArtKombo);

		// titel
		getTabbedPaneBestellung().setTitle();
	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 * @todo Implement this com.lp.client.frame.component.PanelBasis method PJ
	 *       4869
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		getInternalFrameBestellung().getTabbedPaneBestellung().enablePanels(
				getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto(), false);

		wcoBestellungsart.setEnabled(false);
	}

	public boolean isBAbrufbesUndNeu() {
		return bAbrufbesUndNeu;
	}
}

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: Gerold $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/23 14:21:12 $
 */
class PanelBestellungKopfdaten_wcoWaehrungen_actionAdapter implements
		java.awt.event.ActionListener {
	private PanelBestellungKopfdaten adaptee = null;

	PanelBestellungKopfdaten_wcoWaehrungen_actionAdapter(
			PanelBestellungKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (adaptee.getTabbedPaneBestellung() != null) {
				if (adaptee.getTabbedPaneBestellung().getLieferantDto() != null
						&& adaptee.getTabbedPaneBestellung().getLieferantDto()
								.getIId() != null) {
					// ein LF wurde ausgewaehlt
					if (adaptee.wcoWaehrungen.getSelectedItem() != null) {
						// wir haben Vorschlagswaehrungen
						if (adaptee.getInternalFrameBestellung()
								.getTabbedPaneBestellung().getLieferantDto()
								.getWaehrungCNr() != null) {
							if (adaptee.wechselkurseOKDlg(LPMain.getTheClient()
									.getSMandantenwaehrung(),
									(String) adaptee.wcoWaehrungen
											.getSelectedItem())) {
								// Kurs holen
								BigDecimal bdKurs = DelegateFactory
										.getInstance()
										.getLocaleDelegate()
										.getWechselkurs2(
												LPMain.getTheClient()
														.getSMandantenwaehrung(),
												(String) adaptee.wcoWaehrungen
														.getSelectedItem());
								adaptee.refreshLabelKurs(bdKurs);
							} else {
								adaptee.wcoWaehrungen.setSelectedItem(adaptee
										.getInternalFrameBestellung()
										.getTabbedPaneBestellung().getBesDto()
										.getWaehrungCNr());
							}
						}
					}
				}
			}
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}
