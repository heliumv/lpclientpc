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
package com.lp.client.auftrag;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogGeaenderteKonditionenVK;
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
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragdokumentDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
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
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>In diesem Detailfenster des Auftrags werden Kopfdaten erfasst bzw.
 * geaendert.</p> <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-08-03</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.51 $
 */
public class PanelAuftragKopfdaten extends PanelBasis implements
		PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;

	// dtos in diesem panel
	private AuftragDto rahmenauftragDto = null;
	private AnsprechpartnerDto ansprechpartnerDto = null;
	private AnsprechpartnerDto ansprechpartnerDtoLieferadresse = null;
	private AnsprechpartnerDto ansprechpartnerDtoRechnungsadresse = null;
	private PersonalDto vertreterDto = null;
	private KundeDto kundeLieferadresseDto = null;
	private KundeDto kundeRechnungsadresseDto = null;
	private WaehrungDto waehrungDto = null;
	private KostenstelleDto kostenstelleDto = null;

	private PanelQueryFLR panelQueryFLRAbbuchungsLager = null;

	// aenderewaehrung: 0 die urspruengliche Belegwaehrung hinterlegen
	private WaehrungDto waehrungOriDto = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;

	private final static String ACTION_SPECIAL_AUFTRAGART_CHANGED = "action_special_auftragart_changed";
	private final static String ACTION_SPECIAL_RAHMENAUSWAHL = "action_special_rahmenauswahl";
	private static final String ACTION_SPECIAL_KUNDE_AUFTRAG = "action_special_kunde_auftrag";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE = "action_special_ansprechpartner_kunde";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE = "action_special_ansprechpartner_lieferadresse";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE = "action_special_ansprechpartner_rechungsadresse";
	private static final String ACTION_SPECIAL_VERTRETER_KUNDE = "action_special_vertreter_kunde";
	private static final String ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG = "action_special_lieferadresse_auftrag";
	private static final String ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG = "action_special_rechnungsadresse_auftrag";
	private static final String ACTION_SPECIAL_WAEHRUNG = "action_special_waehrung";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_kostenstelle";
	// private static final String ACTION_SPECIAL_LIEFERTERMIN =
	// "action_special_liefertermin";
	static final public String ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE = "action_abbuchungslager_from_liste";

	private static final String ACTION_SPECIAL_VERSTECKEN = "action_special_verstecken";
	private static final String ACTION_SPECIAL_ORDERRESPONSE = "action_special_orderresponse";

	private WrapperButton wbuAbbuchungslager = null;
	private WrapperTextField wtfAbbuchungslager = null;

	private WrapperLabel wlaAuftragsart = null;
	private WrapperComboBox wcoAuftragsart = null;
	private WrapperLabel jLabelBelegdatum = null;
	private WrapperDateField wdfBelegdatum = null;

	private WrapperGotoButton jButtonKunde = null;
	private PanelQueryFLR panelQueryFLRKunde = null;

	private WrapperButton wbuAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	private WrapperButton wbuAnsprechpartnerRechungsadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Rechungsadresse = null;
	private WrapperTextField wtfAnsprechpartnerRechungsadresse = null;

	private WrapperButton wbuAnsprechpartnerLieferadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Lieferadresse = null;

	private WrapperButton jButtonVertreter = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;

	private WrapperButton jButtonLieferadresse = null;
	private PanelQueryFLR panelQueryFLRLieferadresse = null;

	private WrapperButton jButtonRechnungsadresse = null;
	private PanelQueryFLR panelQueryFLRRechnungsadresse = null;

	private WrapperButton wbuKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;

	private WrapperLabel wlaAdresse = null;
	private WrapperTextField wtfKundeAuftrag = null;
	private WrapperTextField wtfKundeAuftragAdresse = null;
	private WrapperLabel wlaKundeAuftragAbteilung = null;
	private WrapperTextField wtfKundeAuftragAbteilung = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartnerLieferadresse = null;
	private WrapperTextField wtfVertreter = null;
	private WrapperTextField wtfKundeLieferadresse = null;
	private WrapperTextField wtfKundeRechnungsadresse = null;

	private WrapperLabel wlaVersteckt = new WrapperLabel();

	private WrapperButton wbuWaehrung = null;
	private PanelQueryFLR panelQueryFLRWaehrung = null;
	private WrapperTextField wtfWaehrung = null;

	private WrapperLabel wlaKurs = null;
	private WrapperNumberField wnfKurs = null;

	private WrapperLabel jLabelProjekt = null;
	private WrapperLabel jLabelBestellnummer = null;
	private WrapperTextField wtfBestellnummer = null;
	private WrapperTextField wtfProjekt = null;
	private WrapperLabel jLabelSonderrabatt = null;
	private WrapperTextField jTextFieldRabattsatz = null;
	// private WrapperComboBox jComboBoxSonderrabatt = null;
	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;
	private WrapperLabel jLabelFinaltermin = null;
	private WrapperDateField wdfFinaltermin = null;
	private WrapperCheckBox wcbTeillieferung = null;
	private WrapperCheckBox wcbUnverbindlich = null;
	private WrapperCheckBox wcbPoenale = null;
	private WrapperCheckBox wcbRoHs = null;
	private WrapperLabel wlaVerrechenbar = new WrapperLabel();
	private WrapperTextField wtfKostenstelle = null;
	private WrapperTextField wtfKostenstelleBezeichnung = null;

	private WrapperLabel wlaBestelldatum = null;
	private WrapperDateField wdfBestelldatum = null;
	private WrapperLabel jLabelLeihtage = null;
	private WrapperNumberField wnfLeihtage = null;

	private WrapperLabel wlaAngebot = null;
	private WrapperTextField wtfAngebot = null;

	private WrapperButton wbuRahmenauswahl = null;
	private PanelQueryFLR panelQueryFLRRahmenauswahl = null;

	private WrapperTextField wtfRahmencnr = null;
	private WrapperTextField wtfRahmenbez = null;

	private WrapperLabel wlaAbrufe = null;
	private WrapperTextArea wtaAbrufe = null;

	private WrapperLabel wlaWiederholungsintervall = null;
	private WrapperComboBox wcoWiederholungsintervall = null;

	private WrapperLabel wlaLauf = null;
	private WrapperDateField wdfLauf = null;

	private GregorianCalendar calendar = null;

	private WrapperLabel wlaErfuellungsgrad = null;
	private WrapperNumberField wnfErfuellungsgrad = null;

	private WrapperNumberField wnfDivisor = null;
	private WrapperRadioButton wcbDivisor = null;
	private WrapperRadioButton wcbRest = null;
	private WrapperRadioButton wcbManuellAbruf = null;
	private ButtonGroup jbgAbrufBes = null;
	private WrapperLabel wlaLeer = new WrapperLabel();
	private WrapperLabel wlaLeerAbruf = null;
	private boolean bAbrufbesUndNeu = false;
	private JPanel jPanelAbrufBes = null;
	private WrapperTimeField wtfZeitLiefertermin = null;
	private WrapperTimeField wtfZeitFinaltermin = null;

	private WrapperSelectField wsfProjekt = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), true);

	private boolean bZusatzfunktionVersandweg = false;
	private static final ImageIcon RESPONSE_ICON = HelperClient
			.createImageIcon("data_out.png");
	private static final ImageIcon RESPONSE_ICON_DONE = HelperClient
			.createImageIcon("data_out_green.png");

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK des Auftrags
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelAuftragKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();

		bZusatzfunktionVersandweg = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_VERSANDWEG);

		jbInit();
		initPanel();
		initComponents();
	}

	private String getIconNameLieferaviso() {
		String iconName = "/com/lp/client/res/data_out.png";
		if (tpAuftrag.getAuftragDto() != null
				&& tpAuftrag.getAuftragDto().getTResponse() != null) {
			iconName = "/com/lp/client/res/data_out_green.png";
		}
		return iconName;
	}

	void jbInit() throws Throwable {

		createAndSaveAndShowButton(
				"/com/lp/client/res/element_preferences.png",
				LPMain.getTextRespectUISPr("auftrag.verstecken"),
				ACTION_SPECIAL_VERSTECKEN, RechteFac.RECHT_AUFT_AUFTRAG_CUD);

		if (bZusatzfunktionVersandweg) {
			createAndSaveAndShowButton(getIconNameLieferaviso(), LPMain
					.getInstance().getTextRespectUISPr("auftrag.bestaetigung"),
					ACTION_SPECIAL_ORDERRESPONSE, KeyStroke.getKeyStroke('L',
							java.awt.event.InputEvent.CTRL_MASK),
					RechteFac.RECHT_AUFT_AKTIVIEREN);
		}

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD, // btndiscard: 0 den Button am Panel
				// anbringen
				PanelBasis.ACTION_PRINT, ACTION_SPECIAL_VERSTECKEN,
				ACTION_SPECIAL_ORDERRESPONSE };
		this.enableToolsPanelButtons(aWhichButtonIUse);

		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);
		Date datCurrentDate = new Date(System.currentTimeMillis());

		wlaAuftragsart = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("detail.label.auftragart"));
		HelperClient.setDefaultsToComponent(wlaAuftragsart, 115);

		wcoAuftragsart = new WrapperComboBox();
		wcoAuftragsart.setMandatoryFieldDB(true);
		wcoAuftragsart.setActionCommand(ACTION_SPECIAL_AUFTRAGART_CHANGED);
		wcoAuftragsart.addActionListener(this);

		jLabelBelegdatum = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.belegdatum"));
		HelperClient.setDefaultsToComponent(jLabelBelegdatum, 90);

		wdfBelegdatum = new WrapperDateField();
		wdfBelegdatum.setDate(datCurrentDate);
		wdfBelegdatum.setMandatoryField(true);
		wdfBelegdatum.addPropertyChangeListener("date",
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						wdfLauf.setMinimumValue(wdfBelegdatum.getDate());
					}
				});

		wbuAbbuchungslager = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.abbuchungslager"));
		wbuAbbuchungslager.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button..hint.abbuchungslager"));
		wbuAbbuchungslager
				.setActionCommand(ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE);
		wbuAbbuchungslager.addActionListener(this);
		wtfAbbuchungslager = new WrapperTextField();
		wtfAbbuchungslager.setActivatable(false);
		wtfAbbuchungslager.setMandatoryField(true);

		ParametermandantDto parametermandantDtoTagen = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_BEWEGUNGSMODULE_ANLEGEN_BIS_ZUM);
		int iTagen = ((Integer) parametermandantDtoTagen.getCWertAsObject())
				.intValue();
		// Parameter anlegen bis zum beruecksichtigen
		GregorianCalendar gc = new GregorianCalendar();
		// Auf Vormonat setzen
		gc.set(Calendar.MONTH, gc.get(Calendar.MONTH) - 1);
		// Tag setzen
		gc.set(Calendar.DATE, iTagen);
		wdfBelegdatum.setMinimumValue(new Date(gc.getTimeInMillis()));

		wbuRahmenauswahl = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.rahmen"));
		wbuRahmenauswahl.setActionCommand(ACTION_SPECIAL_RAHMENAUSWAHL);
		wbuRahmenauswahl.addActionListener(this);

		wtfRahmencnr = new WrapperTextField();
		wtfRahmencnr.setActivatable(false);
		wtfRahmenbez = new WrapperTextField();
		wtfRahmenbez.setActivatable(false);

		jButtonKunde = new WrapperGotoButton(
				WrapperGotoButton.GOTO_KUNDE_AUSWAHL);
		jButtonKunde.setActionCommand(ACTION_SPECIAL_KUNDE_AUFTRAG);
		jButtonKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		jButtonKunde.addActionListener(this);

		wlaAdresse = new WrapperLabel();
		wlaAdresse.setText(LPMain.getTextRespectUISPr("lp.adresse.kbez"));
		wtfKundeAuftrag = new WrapperTextField();
		wtfKundeAuftrag.setMandatoryField(true);
		wtfKundeAuftrag.setActivatable(false);
		wtfKundeAuftrag.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfKundeAuftragAdresse = new WrapperTextField();
		wtfKundeAuftragAdresse.setActivatable(false);
		wtfKundeAuftragAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaKundeAuftragAbteilung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.abteilung"));

		wtfKundeAuftragAbteilung = new WrapperTextField();
		wtfKundeAuftragAbteilung.setActivatable(false);
		wtfKundeAuftragAbteilung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ansprechpartner"));
		wbuAnsprechpartner
				.setActionCommand(this.ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE);
		wbuAnsprechpartner.addActionListener(this);

		wbuAnsprechpartnerLieferadresse = new WrapperButton();
		wbuAnsprechpartnerLieferadresse.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerLieferadresse
				.setActionCommand(this.ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE);
		wbuAnsprechpartnerLieferadresse.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfAnsprechpartnerLieferadresse = new WrapperTextField();
		wtfAnsprechpartnerLieferadresse.setActivatable(false);
		wtfAnsprechpartnerLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAnsprechpartnerRechungsadresse = new WrapperButton();
		wbuAnsprechpartnerRechungsadresse.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerRechungsadresse
				.setActionCommand(this.ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE);
		wbuAnsprechpartnerRechungsadresse.addActionListener(this);

		wtfAnsprechpartnerRechungsadresse = new WrapperTextField();
		wtfAnsprechpartnerRechungsadresse.setActivatable(false);
		wtfAnsprechpartnerRechungsadresse
				.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonVertreter = new WrapperButton();
		jButtonVertreter.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.vertreter"));
		jButtonVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_KUNDE);
		jButtonVertreter.addActionListener(this);

		wtfVertreter = new WrapperTextField();
		wtfVertreter.setMandatoryField(true);
		wtfVertreter.setActivatable(false);
		wtfVertreter.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonLieferadresse = new WrapperButton();
		jButtonLieferadresse.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferadresse"));
		jButtonLieferadresse
				.setActionCommand(ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG);
		jButtonLieferadresse.addActionListener(this);

		wtfKundeLieferadresse = new WrapperTextField();
		wtfKundeLieferadresse.setMandatoryField(true);
		wtfKundeLieferadresse.setActivatable(false);
		wtfKundeLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonRechnungsadresse = new WrapperButton();
		jButtonRechnungsadresse.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.rechnungsadresse"));
		jButtonRechnungsadresse
				.setActionCommand(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG);
		jButtonRechnungsadresse.addActionListener(this);

		wtfKundeRechnungsadresse = new WrapperTextField();
		wtfKundeRechnungsadresse.setMandatoryField(true);
		wtfKundeRechnungsadresse.setActivatable(false);
		wtfKundeRechnungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuWaehrung = new WrapperButton();
		wbuWaehrung.setActionCommand(ACTION_SPECIAL_WAEHRUNG);
		wbuWaehrung.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.waehrung"));
		wbuWaehrung.addActionListener(this);

		wtfWaehrung = new WrapperTextField();
		wtfWaehrung.setActivatable(false);
		wtfWaehrung.setMandatoryField(true);
		wtfWaehrung.addPropertyChangeListener(this);

		wlaKurs = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.kurs"));

		wnfKurs = new WrapperNumberField();
		wnfKurs.setActivatable(false);
		wnfKurs.setMandatoryField(true);
		wnfKurs.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);

		jLabelProjekt = new WrapperLabel();
		jLabelProjekt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));

		jLabelBestellnummer = new WrapperLabel();
		jLabelBestellnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.bestellnummer"));

		wtfBestellnummer = new WrapperTextField(
		/* AuftragFac.MAX_AUFT_AUFTRAG_BESTELLNUMMER */);

		wtfProjekt = new WrapperTextField(
				AuftragFac.MAX_AUFT_AUFTRAG_PROJEKTBEZEICHNUNG);

		jLabelSonderrabatt = new WrapperLabel();
		jLabelSonderrabatt.setText(LPMain.getInstance().getTextRespectUISPr(
				"detail.label.sonderrabatt"));

		jTextFieldRabattsatz = new WrapperTextField();
		jTextFieldRabattsatz.setHorizontalAlignment(SwingConstants.RIGHT);

		// jComboBoxSonderrabatt = new WrapperComboBox();

		wlaLiefertermin = new WrapperLabel();
		wlaLiefertermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.liefertermin"));

		wdfLiefertermin = new WrapperDateField();
		wdfLiefertermin.setMandatoryField(true);

		wdfLiefertermin.setDate(datCurrentDate);
		wdfLiefertermin.getDateEditor().addPropertyChangeListener("date", this);

		jLabelFinaltermin = new WrapperLabel();
		jLabelFinaltermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.finaltermin"));

		wdfFinaltermin = new WrapperDateField();
		wdfFinaltermin.setMandatoryField(true);
		wdfFinaltermin.setDate(datCurrentDate); // muss initialisiert sein!

		wtfZeitLiefertermin = new WrapperTimeField();
		wtfZeitFinaltermin = new WrapperTimeField();

		wcbTeillieferung = new WrapperCheckBox();
		wcbTeillieferung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.teillieferung"));

		wcbUnverbindlich = new WrapperCheckBox();
		wcbUnverbindlich.setText(LPMain.getInstance().getTextRespectUISPr(
				"detail.label.unverbindlich"));

		wcbPoenale = new WrapperCheckBox();
		wcbPoenale.setText(LPMain.getInstance().getTextRespectUISPr(
				"detail.label.poenale"));
		wcbRoHs = new WrapperCheckBox();
		wcbRoHs.setText(LPMain.getInstance().getTextRespectUISPr(
				"detail.label.rohs"));

		wnfLeihtage = new WrapperNumberField(
				new Long(AuftragFac.MIN_I_LEIHTAGE).intValue(), new Long(
						AuftragFac.MAX_I_LEIHTAGE).intValue());
		wnfLeihtage.setFractionDigits(0);

		wbuKostenstelle = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setMandatoryField(true);
		wbuKostenstelle.addActionListener(this);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);

		wtfKostenstelle = new WrapperTextField();
		wtfKostenstelle.setActivatable(false);
		wtfKostenstelle.setMandatoryField(true);
		wtfKostenstelleBezeichnung = new WrapperTextField();
		wtfKostenstelleBezeichnung.setActivatable(false);

		wlaErfuellungsgrad = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("auft.menu.bearbeiten.erfuellungsgrad"));
		wnfErfuellungsgrad = new WrapperNumberField();
		wnfErfuellungsgrad.setActivatable(false);
		wnfErfuellungsgrad.setMandatoryField(false);
		wnfErfuellungsgrad.setFractionDigits(0);

		wlaBestelldatum = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bestelldatum"));
		wdfBestelldatum = new WrapperDateField();
		wdfBestelldatum.setDate(datCurrentDate);

		jLabelLeihtage = new WrapperLabel();
		jLabelLeihtage.setText(LPMain.getInstance().getTextRespectUISPr(
				"detail.label.leihtage"));

		wlaAngebot = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"anf.angebotnummer"));
		wtfAngebot = new WrapperTextField();
		wtfAngebot.setActivatable(false);

		wlaAbrufe = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"bes.abrufe"));
		wtaAbrufe = new WrapperTextArea();
		wtaAbrufe.setActivatable(false);
		wtaAbrufe.setRows(4);

		wlaWiederholungsintervall = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.wiederholungsintervall"));
		wlaWiederholungsintervall.setVisible(false);
		wcoWiederholungsintervall = new WrapperComboBox();
		wcoWiederholungsintervall.setVisible(false);
		wcoWiederholungsintervall.setMandatoryFieldDB(true);

		wlaLauf = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.ab"));
		wlaLauf.setVisible(false);
		wdfLauf = new WrapperDateField();
		wdfLauf.setVisible(false);

		createAbrufPanel();

		// Workingpanel
		jPanelWorkingOn = new JPanel(new MigLayout("wrap 6, hidemode 3",
				"[fill,20%][fill,15%][fill,15%][fill,15%][fill,20%][fill,15%]"));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		jPanelWorkingOn.add(wlaAuftragsart, "right");
		jPanelWorkingOn.add(wcoAuftragsart, "span 3");
		jPanelWorkingOn.add(jLabelBelegdatum, "right");
		jPanelWorkingOn.add(wdfBelegdatum, "wrap");

		jPanelWorkingOn.add(jPanelAbrufBes, "span");

		jPanelWorkingOn.add(wbuRahmenauswahl);
		jPanelWorkingOn.add(wtfRahmencnr, "span 3");
		jPanelWorkingOn.add(wtfRahmenbez, "span");

		jPanelWorkingOn.add(jButtonKunde);
		jPanelWorkingOn.add(wtfKundeAuftrag, "span");

		jPanelWorkingOn.add(wlaAdresse);
		jPanelWorkingOn.add(wtfKundeAuftragAdresse, "span");

		jPanelWorkingOn.add(wlaKundeAuftragAbteilung);
		jPanelWorkingOn.add(wtfKundeAuftragAbteilung, "span");

		jPanelWorkingOn.add(wbuAnsprechpartner);
		jPanelWorkingOn.add(wtfAnsprechpartner, "span");

		// Zeile
		jPanelWorkingOn.add(jButtonVertreter);
		jPanelWorkingOn.add(wtfVertreter, "span");

		// Zeile
		jPanelWorkingOn.add(jButtonLieferadresse);
		jPanelWorkingOn.add(wtfKundeLieferadresse, "span 3");
		jPanelWorkingOn.add(wbuAnsprechpartnerLieferadresse);
		jPanelWorkingOn.add(wtfAnsprechpartnerLieferadresse);

		// Zeile
		jPanelWorkingOn.add(jButtonRechnungsadresse);
		jPanelWorkingOn.add(wtfKundeRechnungsadresse, "span 3");
		jPanelWorkingOn.add(wbuAnsprechpartnerRechungsadresse);
		jPanelWorkingOn.add(wtfAnsprechpartnerRechungsadresse);

		// Zeile
		jPanelWorkingOn.add(jLabelProjekt);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			
			ParametermandantDto parametermandantDto = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PROJEKT_IST_PFLICHTFELD);
			boolean bProjektIstPflichtfeld = ((Boolean) parametermandantDto
					.getCWertAsObject());
			if (bProjektIstPflichtfeld) {
				wsfProjekt.setMandatoryField(true);
			}

			
			jPanelWorkingOn.add(wtfProjekt, "span 3");
			jPanelWorkingOn.add(wsfProjekt.getWrapperGotoButton());
			jPanelWorkingOn.add(wsfProjekt.getWrapperTextField(), "span");
		} else {
			jPanelWorkingOn.add(wtfProjekt, "span");
		}
		// Zeile
		jPanelWorkingOn.add(jLabelBestellnummer);
		jPanelWorkingOn.add(wtfBestellnummer, "span 3");
		jPanelWorkingOn.add(wlaAngebot);
		jPanelWorkingOn.add(wtfAngebot);

		// Zeile
		jPanelWorkingOn.add(wbuWaehrung);
		jPanelWorkingOn.add(wtfWaehrung, "span 3");
		jPanelWorkingOn.add(wlaKurs);
		jPanelWorkingOn.add(wnfKurs);

		// Zeile
		jPanelWorkingOn.add(wlaBestelldatum);
		jPanelWorkingOn.add(wdfBestelldatum);
		jPanelWorkingOn.add(wbuAbbuchungslager);
		jPanelWorkingOn.add(wtfAbbuchungslager);
		jPanelWorkingOn.add(jLabelLeihtage);
		jPanelWorkingOn.add(wnfLeihtage);

		// Zeile
		jPanelWorkingOn.add(wlaLiefertermin);
		jPanelWorkingOn.add(wdfLiefertermin);
		if (tpAuftrag.getBAuftragterminstudenminuten()) {
			jPanelWorkingOn.add(wtfZeitLiefertermin);
		} else {
			jPanelWorkingOn.add(new WrapperLabel());
		}
		jPanelWorkingOn.add(wlaErfuellungsgrad, "skip");
		jPanelWorkingOn.add(wnfErfuellungsgrad);

		// Zeile
		jPanelWorkingOn.add(jLabelFinaltermin);
		jPanelWorkingOn.add(wdfFinaltermin);
		if (tpAuftrag.getBAuftragterminstudenminuten()) {
			jPanelWorkingOn.add(wtfZeitFinaltermin);
		} else {
			jPanelWorkingOn.add(new WrapperLabel());
		}

		jPanelWorkingOn.add(wcbUnverbindlich, "skip");
		jPanelWorkingOn.add(wcbTeillieferung);

		// Zeile
		jPanelWorkingOn.add(wbuKostenstelle);
		jPanelWorkingOn.add(wtfKostenstelle, "span 2");
		jPanelWorkingOn.add(wlaVersteckt);

		jPanelWorkingOn.add(wcbPoenale);

		jPanelWorkingOn.add(wcbRoHs);

		// Zeile
		/*
		 * iZeile++; jPanelWorkingOn.add(wtfKostenstelleBezeichnung, new
		 * GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0 ,
		 * GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2,
		 * 2, 2), 0, 0));
		 */

		/*
		 * jPanelWorkingOn.add(jLabelSonderrabatt, new GridBagConstraints(0, 9,
		 * 1, 1, 0.0, 0.0 , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		 * new Insets(0, 2, 2, 2), 0, 0));
		 */
		/*
		 * jPanelWorkingOn.add(jComboBoxSonderrabatt, new GridBagConstraints(1,
		 * 9, 1, 1, 0.0, 0.0 , GridBagConstraints.CENTER,
		 * GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		 */

		/*
		 * jPanelWorkingOn.add(jTextFieldRabattsatz, new GridBagConstraints(3,
		 * 9, 1, 1, 0.0, 0.0 , GridBagConstraints.WEST, GridBagConstraints.BOTH,
		 * new Insets(2, 2, 2, 2), 0, 0));
		 */

		jPanelWorkingOn.add(wlaAbrufe);
		jPanelWorkingOn.add(wtaAbrufe, "span");

		jPanelWorkingOn.add(wlaWiederholungsintervall);
		jPanelWorkingOn.add(wcoWiederholungsintervall, "span 3");
		jPanelWorkingOn.add(wlaLauf);
		jPanelWorkingOn.add(wdfLauf);

		ParametermandantDto parametermandantDtoVerrechenbar = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR);
		boolean bVerrechenbar = ((Boolean) parametermandantDtoVerrechenbar
				.getCWertAsObject());
		if (bVerrechenbar) {
			jPanelWorkingOn.add(wlaVerrechenbar, "skip, span");
		}

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)) {
			boolean hatRecht = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);
			if (hatRecht) {

				getToolBar().addButtonRight("/com/lp/client/res/gear_run.png",
						LPMain.getTextRespectUISPr("proj.startzeit"),
						Desktop.MY_OWN_NEW_ZEIT_START, null, null);
				getToolBar().addButtonRight("/com/lp/client/res/gear_stop.png",
						LPMain.getTextRespectUISPr("proj.stopzeit"),
						Desktop.MY_OWN_NEW_ZEIT_STOP, null, null);

				enableToolsPanelButtons(true, Desktop.MY_OWN_NEW_ZEIT_START,
						Desktop.MY_OWN_NEW_ZEIT_STOP);

			}
		}

	}

	private void initPanel() throws Throwable {
		wcoAuftragsart.setMap(DelegateFactory.getInstance()
				.getAuftragServiceDelegate()
				.getAuftragart(LPMain.getInstance().getUISprLocale()));
		wcoWiederholungsintervall.setMap(DelegateFactory
				.getInstance()
				.getAuftragServiceDelegate()
				.getAuftragwiederholungsintervall(
						LPMain.getInstance().getUISprLocale()));
	}

	public void setDefaults() throws Throwable {
		rahmenauftragDto = new AuftragDto();
		ansprechpartnerDto = new AnsprechpartnerDto();
		ansprechpartnerDtoLieferadresse = new AnsprechpartnerDto();
		ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();
		vertreterDto = new PersonalDto();
		kundeLieferadresseDto = new KundeDto();
		kundeRechnungsadresseDto = new KundeDto();
		waehrungDto = new WaehrungDto();
		kostenstelleDto = new KostenstelleDto();
		// aenderewaehrung: 1 die eventuell hinterlegte urspruengliche Waehrung
		// zuruecksetzen
		waehrungOriDto = new WaehrungDto();

		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		wlaVersteckt.setText("");
		// alle vorbelegten werte setzen
		wcoAuftragsart.setKeyOfSelectedItem(AuftragServiceFac.AUFTRAGART_FREI);
		Date currentDate = new Date(System.currentTimeMillis());
		// Locale locDefault = LPMain.getInstance().getUISprLocale();

		wdfBelegdatum.setDate(currentDate);
		wdfBestelldatum.setDate(currentDate);
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG);
		int defaultLieferzeitAuftrag = ((Integer) parametermandantDto
				.getCWertAsObject()).intValue();

		calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, defaultLieferzeitAuftrag);
		java.sql.Date currentDateMandantParameter = new java.sql.Date(
				calendar.getTimeInMillis());
		wdfLiefertermin.setDate(currentDateMandantParameter);
		wdfFinaltermin.setDate(currentDateMandantParameter);
		calendar.add(Calendar.MONTH, 1);
		java.sql.Date laufDate = new java.sql.Date(calendar.getTimeInMillis());
		wdfLauf.setDate(laufDate);
		wdfLauf.setMinimumValue(new java.sql.Date(System.currentTimeMillis()));
		wcoWiederholungsintervall
				.setKeyOfSelectedItem(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH);

		// default waehrung kommt vom Mandanten
		waehrungDto = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.waehrungFindByPrimaryKey(
						DelegateFactory
								.getInstance()
								.getMandantDelegate()
								.mandantFindByPrimaryKey(
										LPMain.getInstance().getTheClient()
												.getMandant()).getWaehrungCNr());
		wtfWaehrung.setText(waehrungDto.getCNr());
		setzeWechselkurs();
		wnfLeihtage.setInteger(new Integer(0));
		wcbTeillieferung.setSelected(true);
		// Vertreter aus kunde
		parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE);
		if (parametermandantDto.getCWert().equals("0")) {
			if (tpAuftrag.getAuftragDto().getIId() == null) {
				vertreterDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								LPMain.getInstance().getTheClient()
										.getIDPersonal());
				if (vertreterDto != null && vertreterDto.getIId() != null) {
					wtfVertreter.setText(vertreterDto.getPartnerDto()
							.formatTitelAnrede());
				}
			}
		}
		wnfDivisor.setInteger(1);

		parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_DEFAULT_UNVERBINDLICH);
		if (((Boolean) parametermandantDto.getCWertAsObject()) == true) {
			wcbUnverbindlich.setSelected(true);
		}

	}

	public void setDefaultsAusProjekt(Integer projektIId) throws Throwable {
		ProjektDto projektDto = DelegateFactory.getInstance()
				.getProjektDelegate().projektFindByPrimaryKey(projektIId);

		wtfProjekt.setText(projektDto.getCTitel());
		wsfProjekt.setKey(projektDto.getIId());

		vertreterDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						projektDto.getPersonalIIdZugewiesener());
		if (vertreterDto != null && vertreterDto.getIId() != null) {
			wtfVertreter.setText(vertreterDto.getPartnerDto()
					.formatTitelAnrede());
		}

		KundeDto kundeDto = DelegateFactory
				.getInstance()
				.getKundeDelegate()
				.kundeFindByiIdPartnercNrMandantOhneExc(
						projektDto.getPartnerIId(),
						LPMain.getInstance().getTheClient().getMandant());
		if (kundeDto == null) {
			// Dann Kunde zuerst anlegen

			PartnerDto pDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(projektDto.getPartnerIId());

			boolean b = DialogFactory.showModalJaNeinDialog(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.kunde.auspartner.anlegen.teil1")
							+ pDto.formatFixTitelName1Name2()
							+ LPMain.getInstance().getTextRespectUISPr(
									"lp.kunde.auspartner.anlegen.teil2"));

			if (b == true) {
				Integer kundeIId = DelegateFactory.getInstance()
						.getKundeDelegate()
						.createKundeAusPartner(projektDto.getPartnerIId());
				kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(kundeIId);

			} else {
				return;
			}

		} else {
			kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(kundeDto.getIId());
		}

		tpAuftrag.setKundeAuftragDto(kundeDto);

		kundenDatenVorbesetzen(kundeDto.getIId());
		if (projektDto.getAnsprechpartnerIId() != null) {
			ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							projektDto.getAnsprechpartnerIId());

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatTitelAnrede());
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// Source ist mein Kundeauswahldialog; wenn ein neuer Auftragskunde
			// gewaehlt wurde, werden die abhaengigen Felder angepasst
			if (e.getSource() == panelQueryFLRKunde) {
				Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (iIdKunde != null) {

					kundenDatenVorbesetzen(iIdKunde);

				}
			} else if (e.getSource() == panelQueryFLRAbbuchungsLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfAbbuchungslager.setText(lagerDto.getCNr());
				tpAuftrag.getAuftragDto().setLagerIIdAbbuchungslager(
						lagerDto.getIId());
			} else

			// Source ist der Ansprechpartnerauswahldialog

			// Source ist der Ansprechpartnerauswahldialog
			if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();

				bestimmeUndZeigeAnsprechpartnerLieferadresse(iIdAnsprechpartner);
			} else

			// Source ist der Ansprechpartnerauswahldialog

			// Source ist der Ansprechpartnerauswahldialog
			if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();

				bestimmeUndZeigeAnsprechpartnerRechnungsadresse(iIdAnsprechpartner);
			}

			else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();

				ansprechpartnerDto = DelegateFactory.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
						.formatTitelAnrede());

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_AUFTRAG_LIEFERADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_AUFTRAG,
								LPMain.getInstance().getTheClient()
										.getMandant());

				int iOption = (Integer) parameter.getCWertAsObject();
				if (iOption == 2) {
					if (kundeLieferadresseDto != null
							&& kundeLieferadresseDto.getIId() != null
							&& tpAuftrag.getKundeAuftragDto() != null) {
						if (kundeLieferadresseDto.getIId().equals(
								tpAuftrag.getKundeAuftragDto().getIId())) {
							bestimmeUndZeigeAnsprechpartnerLieferadresse(iIdAnsprechpartner);

						}
					}

				}

				parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_AUFTRAG_RECHNUNGSADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_AUFTRAG,
								LPMain.getInstance().getTheClient()
										.getMandant());

				iOption = (Integer) parameter.getCWertAsObject();
				if (iOption == 2) {
					if (kundeRechnungsadresseDto != null
							&& kundeRechnungsadresseDto.getIId() != null
							&& tpAuftrag.getKundeAuftragDto() != null) {
						if (kundeRechnungsadresseDto.getIId().equals(
								tpAuftrag.getKundeAuftragDto().getIId())) {
							bestimmeUndZeigeAnsprechpartnerRechnungsadresse(iIdAnsprechpartner);

						}
					}

				}

			} else

			// source ist MEIN Vertreterauswahldialog
			if (e.getSource() == panelQueryFLRVertreter) {
				Integer pkPersonal = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				vertreterDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(pkPersonal);
				vertreterDto2components();
			} else

			// source ist MEINE auswahl der lieferadresse
			if (e.getSource() == panelQueryFLRLieferadresse) {
				Integer pkKunde = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				kundeLieferadresseDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(pkKunde);

				kundeLieferadresseDto2components();

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_AUFTRAG_LIEFERADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_AUFTRAG,
								LPMain.getInstance().getTheClient()
										.getMandant());

				int iOption = (Integer) parameter.getCWertAsObject();

				AnsprechpartnerDto anspDtoTemp = null;

				if (iOption == 1 || iOption == 2) {
					anspDtoTemp = DelegateFactory
							.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(
									kundeLieferadresseDto.getPartnerDto()
											.getIId());

				}

				if (anspDtoTemp != null) {
					ansprechpartnerDtoLieferadresse = anspDtoTemp;

					wtfAnsprechpartnerLieferadresse
							.setText(ansprechpartnerDtoLieferadresse
									.getPartnerDto().formatTitelAnrede());
				} else {
					wtfAnsprechpartnerLieferadresse.setText("");
					ansprechpartnerDtoLieferadresse = new AnsprechpartnerDto();
				}

			} else

			// source ist MEINE auswahl der rechnungsadresse
			if (e.getSource() == panelQueryFLRRechnungsadresse) {
				Integer pkKunde = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				kundeRechnungsadresseDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(pkKunde);

				kundeRechnungsadresseDto2components();

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_AUFTRAG_RECHNUNGSADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_AUFTRAG,
								LPMain.getInstance().getTheClient()
										.getMandant());

				int iOption = (Integer) parameter.getCWertAsObject();

				AnsprechpartnerDto anspDtoTemp = null;

				if (iOption == 1 || iOption == 2) {
					if (iOption == 2 && ansprechpartnerDto != null
							&& ansprechpartnerDto.getIId() != null) {
						anspDtoTemp = ansprechpartnerDto;
					} else {
						anspDtoTemp = DelegateFactory
								.getInstance()
								.getAnsprechpartnerDelegate()
								.ansprechpartnerFindErstenEinesPartnersOhneExc(
										kundeRechnungsadresseDto
												.getPartnerDto().getIId());
					}
				}

				if (anspDtoTemp != null) {
					ansprechpartnerDtoRechnungsadresse = anspDtoTemp;

					wtfAnsprechpartnerRechungsadresse
							.setText(ansprechpartnerDtoRechnungsadresse
									.getPartnerDto().formatTitelAnrede());
				} else {
					wtfAnsprechpartnerRechungsadresse.setText("");
					ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();
				}

			} else if (e.getSource() == panelQueryFLRWaehrung) {
				Object cNrWaehrung = ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (cNrWaehrung != null) {
					waehrungDto = DelegateFactory.getInstance()
							.getLocaleDelegate()
							.waehrungFindByPrimaryKey((String) cNrWaehrung);

					wtfWaehrung.setText(waehrungDto.getCNr());

					setzeWechselkurs();
				}
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Integer iIdKostenstelle = (Integer) (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				kostenstelleDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.kostenstelleFindByPrimaryKey(iIdKostenstelle);
				wtfKostenstelle.setText(kostenstelleDto.getCBez());
				wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
			} else if (e.getSource() == panelQueryFLRRahmenauswahl) {
				Integer auftragIIdRahmenauftrag = (Integer) (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();

				if (auftragIIdRahmenauftrag != null) {
					rahmenauftragDto = DelegateFactory.getInstance()
							.getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragIIdRahmenauftrag);
					Integer IId = null;
					if (tpAuftrag.getAuftragDto() != null)
						IId = tpAuftrag.getAuftragDto().getIId();
					tpAuftrag.setAuftragDto(rahmenauftragDto);

					wtfRahmencnr.setText(rahmenauftragDto.getCNr());
					wtfRahmenbez.setText(rahmenauftragDto
							.getCBezProjektbezeichnung());

					erzeugeAbrufauftrag(IId);
				}
			}
		} else

		// dqeingabeleeren: 1 das Event vom leeren Button behandeln
		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			// source ist MEIN Ansprechpartnerdialog
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				ansprechpartnerDto = new AnsprechpartnerDto();
				wtfAnsprechpartner.setText("");
			}
			if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferadresse) {
				ansprechpartnerDtoLieferadresse = new AnsprechpartnerDto();
				wtfAnsprechpartnerLieferadresse.setText("");
			}
			if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
				ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();
				wtfAnsprechpartnerRechungsadresse.setText("");
			}
		}
	}

	private void kundenDatenVorbesetzen(Integer iIdKunde) throws ExceptionLP,
			Throwable {
		DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(iIdKunde);

		// aenderewaehrung: 2 die urspruengliche Waehrung
		// hinterlegen, wenn es eine gibt
		if (tpAuftrag.getAuftragDto().getCAuftragswaehrung() != null) {
			waehrungOriDto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.waehrungFindByPrimaryKey(
							tpAuftrag.getAuftragDto().getCAuftragswaehrung());
		}

		// aenderewaehrung: 3 den gewaehlten Kunden uebernehmen
		tpAuftrag.setKundeAuftragDto(DelegateFactory.getInstance()
				.getKundeDelegate().kundeFindByPrimaryKey(iIdKunde));

		LagerDto lagerDto = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByPrimaryKey(
						tpAuftrag.getKundeAuftragDto()
								.getLagerIIdAbbuchungslager());
		wtfAbbuchungslager.setText(lagerDto.getCNr());
		tpAuftrag.getAuftragDto().setLagerIIdAbbuchungslager(lagerDto.getIId());

		kundeAuftragDto2Components();

		// aenderewaehrung: 4 den Benutzer fragen, ob er die
		// urspruengliche Waehrung beibehalten moechte
		if (waehrungOriDto.getCNr() != null
				&& !waehrungOriDto.getCNr().equals(wtfWaehrung.getText())) {
			int indexWaehrungOriCNr = 0;
			int indexWaehrungNeuCNr = 1;
			int iAnzahlOptionen = 2;

			Object[] aOptionen = new Object[iAnzahlOptionen];
			aOptionen[indexWaehrungOriCNr] = waehrungOriDto.getCNr();
			aOptionen[indexWaehrungNeuCNr] = wtfWaehrung.getText();

			int iAuswahl = DialogFactory.showModalDialog(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.waehrungunterschiedlich"), LPMain.getInstance()
							.getTextRespectUISPr("lp.frage"), aOptionen,
					aOptionen[0]);

			if (iAuswahl == indexWaehrungOriCNr) {
				// die Belegwaehrung wird beibehalten -> Waehrung
				// und Wechselkurs zuruecksetzen
				waehrungDto = waehrungOriDto;
				wtfWaehrung.setText(waehrungDto.getCNr());
				setzeWechselkurs();
				waehrungOriDto = null;
			}
		}

		// den Ansprechpartner beim Kunden zuruecksetzen
		ansprechpartnerDto = new AnsprechpartnerDto();

		AnsprechpartnerDto anspDtoTemp = null;
		// Ansprechpartner vorbesetzen?
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUFTRAG_ANSP_VORBESETZEN,
						ParameterFac.KATEGORIE_AUFTRAG,
						LPMain.getInstance().getTheClient().getMandant());
		if ((Boolean) parameter.getCWertAsObject()) {
			anspDtoTemp = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(
							tpAuftrag.getKundeAuftragDto().getPartnerIId());
		}
		if (anspDtoTemp != null) {
			ansprechpartnerDto = anspDtoTemp;

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText("");
		}

		// Vertreter aus kunde
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE);
		if (parametermandantDto.getCWert().equals("1")) {
			// es kann einen default vertreter beim kunden geben
			Integer iIdPersonal = tpAuftrag.getKundeAuftragDto()
					.getPersonaliIdProvisionsempfaenger();
			if (iIdPersonal != null) {
				vertreterDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(iIdPersonal);
				vertreterDto2components();
			} else {
				vertreterDto = new PersonalDto();
				wtfVertreter.setText("");
			}
		}

		Integer partnerIid = null;

		ParametermandantDto parametermandantAdressvorbelegungDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_ADRESSVORBELEGUNG);

		int iAdressvorbelegung = (Integer) parametermandantAdressvorbelegungDto
				.getCWertAsObject();

		if (iAdressvorbelegung == 0) {

			// PJ 17644 Rechnungsadresse aus Haeufigkeit holen
			ArrayList<KundeDto> listKundenRechnungsadresse = DelegateFactory
					.getInstance()
					.getAuftragDelegate()
					.getRechnungsadressenEinerAuftragsadresseSortiertNachHaeufigkeit(
							tpAuftrag.getKundeAuftragDto().getIId());
			if (listKundenRechnungsadresse.size() == 0) {
				partnerIid = tpAuftrag.getKundeAuftragDto()
						.getPartnerIIdRechnungsadresse();
			} else if (listKundenRechnungsadresse.size() == 1) {
				partnerIid = listKundenRechnungsadresse.get(0).getPartnerIId();
			} else if (listKundenRechnungsadresse.size() > 1) {
				// Dialog anzeigen
				DialogRechnungsLieferadresse d = new DialogRechnungsLieferadresse(
						listKundenRechnungsadresse,
						LPMain.getInstance().getTextRespectUISPr(
								"auft.rechnungsadresse.haeufigkeit.auswaehlen"));
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);
				if (d.kundeDto != null) {
					partnerIid = d.kundeDto.getPartnerIId();
				}
			}

			if (partnerIid == null) {
				partnerIid = tpAuftrag.getKundeAuftragDto()
						.getPartnerIIdRechnungsadresse();
			}
		} else {
			partnerIid = tpAuftrag.getKundeAuftragDto()
					.getPartnerIIdRechnungsadresse();
		}

		// Der Kunde hat eine andere Rechnungadresse

		if (partnerIid != null) {
			kundeRechnungsadresseDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByiIdPartnercNrMandantOhneExc(partnerIid,
							LPMain.getInstance().getTheClient().getMandant());
			if (kundeRechnungsadresseDto != null) {
				kundeRechnungsadresseDto.setPartnerDto(DelegateFactory
						.getInstance().getPartnerDelegate()
						.partnerFindByPrimaryKey(partnerIid));
			} else {
				kundeRechnungsadresseDto = tpAuftrag.getKundeAuftragDto();
			}

		} else {
			kundeRechnungsadresseDto = tpAuftrag.getKundeAuftragDto();
		}

		kundeRechnungsadresseDto2components();

		// PJ17869
		ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();

		anspDtoTemp = null;
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUFTRAG_RECHNUNGSADRESSE_ANSP_VORBESETZEN,
						ParameterFac.KATEGORIE_AUFTRAG,
						LPMain.getInstance().getTheClient().getMandant());

		int iOption = (Integer) parameter.getCWertAsObject();
		if (iOption == 1) {
			anspDtoTemp = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(
							kundeRechnungsadresseDto.getPartnerDto().getIId());
		} else if (iOption == 2) {

			// Wenn Auftrags/Rechnungsadresse ungleich, dann ersten
			// vorbesetzen
			if (kundeRechnungsadresseDto.getIId().equals(
					tpAuftrag.getKundeAuftragDto().getIId())) {
				anspDtoTemp = DelegateFactory
						.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(
								tpAuftrag.getKundeAuftragDto().getPartnerIId());
			} else {
				anspDtoTemp = DelegateFactory
						.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(
								kundeRechnungsadresseDto.getPartnerDto()
										.getIId());
			}

		}
		if (anspDtoTemp != null) {
			ansprechpartnerDtoRechnungsadresse = anspDtoTemp;

			wtfAnsprechpartnerRechungsadresse
					.setText(ansprechpartnerDtoRechnungsadresse.getPartnerDto()
							.formatTitelAnrede());
		} else {
			wtfAnsprechpartnerRechungsadresse.setText("");
		}

		// Lieferadresse

		if (iAdressvorbelegung == 0) {
			// PJ 17644 Rechnungsadresse aus Haeufigkeit holen
			ArrayList<KundeDto> listKundenLieferadresse = DelegateFactory
					.getInstance()
					.getAuftragDelegate()
					.getLieferadressenEinerRechnungsadresseSortiertNachHaeufigkeit(
							kundeRechnungsadresseDto.getIId());

			kundeLieferadresseDto = tpAuftrag.getKundeAuftragDto();
			if (listKundenLieferadresse.size() == 1) {
				kundeLieferadresseDto = listKundenLieferadresse.get(0);
			} else if (listKundenLieferadresse.size() > 1) {
				// Dialog anzeigen
				DialogRechnungsLieferadresse d = new DialogRechnungsLieferadresse(
						listKundenLieferadresse,
						LPMain.getInstance().getTextRespectUISPr(
								"auft.lieferadresse.haeufigkeit.auswaehlen"));
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);
				if (d.kundeDto != null) {
					kundeLieferadresseDto = d.kundeDto;
				}

			}
		} else {
			kundeLieferadresseDto = tpAuftrag.getKundeAuftragDto();
		}
		if (kundeLieferadresseDto == null) {
			kundeLieferadresseDto = tpAuftrag.getKundeAuftragDto();
		}
		kundeLieferadresseDto2components();

		// den Ansprechpartner beim Kunden zuruecksetzen
		ansprechpartnerDtoLieferadresse = new AnsprechpartnerDto();

		anspDtoTemp = null;

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUFTRAG_LIEFERADRESSE_ANSP_VORBESETZEN,
						ParameterFac.KATEGORIE_AUFTRAG,
						LPMain.getInstance().getTheClient().getMandant());

		iOption = (Integer) parameter.getCWertAsObject();
		if (iOption == 1) {
			anspDtoTemp = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(
							kundeLieferadresseDto.getPartnerDto().getIId());
		} else if (iOption == 2) {

			// Wenn Auftrags/Lieferadresse ungleich, dann ersten
			// vorbesetzen
			if (kundeLieferadresseDto.getIId().equals(
					tpAuftrag.getKundeAuftragDto().getIId())) {
				anspDtoTemp = DelegateFactory
						.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(
								tpAuftrag.getKundeAuftragDto().getPartnerIId());
			} else {
				anspDtoTemp = DelegateFactory
						.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(
								kundeLieferadresseDto.getPartnerDto().getIId());
			}

		}
		if (anspDtoTemp != null) {
			ansprechpartnerDtoLieferadresse = anspDtoTemp;

			wtfAnsprechpartnerLieferadresse
					.setText(ansprechpartnerDtoLieferadresse.getPartnerDto()
							.formatTitelAnrede());
		} else {
			wtfAnsprechpartnerLieferadresse.setText("");
		}
	}

	/**
	 * Einen Abrufauftrag als Clone eines Rahmenauftrags erzeugen und anzeigen.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void erzeugeAbrufauftrag(Integer iId) throws Throwable {
		rahmenauftragDto = tpAuftrag.getAuftragDto();

		AuftragDto abrufauftragDto = (AuftragDto) rahmenauftragDto.clone();
		abrufauftragDto.setIId(iId);
		abrufauftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_ABRUF);
		abrufauftragDto.setAuftragIIdRahmenauftrag(rahmenauftragDto.getIId());
		abrufauftragDto.setPersonalIIdAnlegen(LPMain.getInstance()
				.getTheClient().getIDPersonal());
		abrufauftragDto.setPersonalIIdAendern(LPMain.getInstance()
				.getTheClient().getIDPersonal());
		abrufauftragDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		abrufauftragDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));

		tpAuftrag.setAuftragDto(abrufauftragDto);
		tpAuftrag.setKundeAuftragDto(DelegateFactory
				.getInstance()
				.getKundeDelegate()
				.kundeFindByPrimaryKey(
						abrufauftragDto.getKundeIIdAuftragsadresse()));

		// den Clone anzeigen; Vorsicht: dieses Dto ist vorerst temporaer
		dto2Components();

		// es muss der aktuelle Wechselkurs von heute verwendet werden
		setzeWechselkurs();
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		// hier landen wir, wenn aus einem Rahmenauftrag ein Abrufauftrag
		// erzeugt werden soll
		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			erzeugeAbrufauftrag(null);

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance().getTextRespectUISPr(
							"auft.title.neuerauftrag"));
		} else {
			tpAuftrag.resetDtos();
			tpAuftrag.setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
					"auft.title.panel.kopfdaten"));
			setDefaults();
		}

		super.eventActionNew(eventObject, true, false);

		clearStatusbar();

		enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen();

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAuftrag.aktualisiereAuftragstatus()) {
			super.eventActionUpdate(aE, false);

			enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen();
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUFTRAG;
	}

	/**
	 * Einen Auftrag auf storniert setzen. <br>
	 * Auftraege koennen nicht physikalisch geloescht werden.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (!isLockedDlg()) {
			if (!tpAuftrag.getAuftragDto().getAuftragstatusCNr()
					.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
						.getTextRespectUISPr("auft.storno.statusnichtoffen"));
				return;
			}

			DelegateFactory.getInstance().getAuftragDelegate()
					.storniereAuftrag(tpAuftrag.getAuftragDto());
			super.eventActionDelete(e, false, false); // der auftrag existiert
			// weiterhin!
			eventYouAreSelected(false);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		boolean bUpdate = false;
		if (allMandatoryFieldsSetDlg()) {

			components2auftragDto();

			if (Helper
					.cutTimestamp(tpAuftrag.getAuftragDto().getDFinaltermin())
					.before(Helper.cutTimestamp(tpAuftrag.getAuftragDto()
							.getTBelegdatum()))) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("auft.hint.finalterminvorbelegtermin"));
			}
			if (Helper.cutTimestamp(
					tpAuftrag.getAuftragDto().getDLiefertermin()).before(
					Helper.cutTimestamp(tpAuftrag.getAuftragDto()
							.getTBelegdatum()))) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("auft.hint.lieferterminvorbelegtermin"));
			}

			// pruefen, obs schon AB's mit dieser Bestellnummer gibt
			if (tpAuftrag.getAuftragDto().getCBestellnummer() != null
					&& tpAuftrag.getAuftragDto().getCBestellnummer().trim()
							.length() > 0) {
				AuftragDto[] dtos = DelegateFactory
						.getInstance()
						.getAuftragDelegate()
						.auftragFindByMandantCNrKundeIIdBestellnummerOhneExc(
								tpAuftrag.getAuftragDto()
										.getKundeIIdAuftragsadresse(),
								tpAuftrag.getAuftragDto().getMandantCNr(),
								tpAuftrag.getAuftragDto().getCBestellnummer());
				if (dtos != null && dtos.length > 0) {
					// wenn es die AB ist, die ich grad bearbeite
					if (tpAuftrag.getAuftragDto().getIId() != null
							&& dtos.length == 1
							&& dtos[0].getIId().equals(
									tpAuftrag.getAuftragDto().getIId())) {
						// nothing here
					} else {
						StringBuffer sb = new StringBuffer();
						sb.append(LPMain.getInstance().getTextRespectUISPr(
								"auft.error.bestellnummer_doppelt"));
						for (int i = 0; i < dtos.length; i++) {
							// auch hier die aktuelle nicht anzeigen
							if (tpAuftrag.getAuftragDto().getIId() != null
									&& dtos[i].getIId().equals(
											tpAuftrag.getAuftragDto().getIId())) {
								// nothing here
							} else {
								sb.append("\n");
								sb.append(dtos[i].getCNr() + "   "
										+ dtos[i].getCBestellnummer());
							}
						}
						sb.append("\n");
						sb.append(LPMain.getInstance().getTextRespectUISPr(
								"lp.frage.trotzdemspeichern"));
						boolean answer = (DialogFactory.showMeldung(sb
								.toString(), LPMain.getInstance()
								.getTextRespectUISPr("lp.frage"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
						if (!answer) {
							return;
						}
					}
				}
			}

			DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.pruefeKreditlimit(
							tpAuftrag.getAuftragDto()
									.getKundeIIdRechnungsadresse());

			AuftragdokumentDto[] alleAuftragdokumentDtos = DelegateFactory
					.getInstance().getAuftragServiceDelegate()
					.auftragdokumentFindByBVersteckt();

			ArrayList<AuftragdokumentDto> alAuftragsdokumenteZuSpeichern = new ArrayList<AuftragdokumentDto>();

			if (alleAuftragdokumentDtos != null
					&& alleAuftragdokumentDtos.length > 0) {

				Integer auftragIIdDokumente = tpAuftrag.getAuftragDto()
						.getIId();

				if (wcoAuftragsart.getKeyOfSelectedItem().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)
						&& tpAuftrag.getAuftragDto().getIId() == null) {
					auftragIIdDokumente = tpAuftrag.getAuftragDto()
							.getAuftragIIdRahmenauftrag();
				}

				DialogAuftragdokumente d = new DialogAuftragdokumente(
						auftragIIdDokumente);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

				if (d.getAuftragdokumentDto() == null) {
					return;
				}

				alAuftragsdokumenteZuSpeichern = d.getAuftragdokumentDto();

			}

			if (tpAuftrag.getAuftragDto().getIId() == null) {

				// vorschlagswerte aus dem kunden fuer konditionen vorbelegen,
				// wenn es sich um keinen Abrufauftrag handelt
				if (!((String) wcoAuftragsart.getKeyOfSelectedItem())
						.equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
					initKonditionen();
				}
				// belegartkonditionen: 0 Kopf- und Fusstext werden nicht
				// vorbelegt, damit
				// man spaeter erkennen kann, ob die Konditionen abgespeichert
				// wurden
				Integer iIdAuftrag = DelegateFactory.getInstance()
						.getAuftragDelegate()
						.createAuftrag(tpAuftrag.getAuftragDto());
				tpAuftrag.setAuftragDto(DelegateFactory.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftrag));
				setKeyWhenDetailPanel(iIdAuftrag);

			} else {
				bUpdate = true;
				if (tpAuftrag.getAuftragDto().getAuftragstatusCNr()
						.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
					if (DialogFactory
							.showMeldung(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hint.offennachangelegt"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hint"),
									javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {
						bUpdate = false;
					}
				}

				if (bUpdate) {

					// SP1141
					AuftragDto aDtoVorhanden = DelegateFactory
							.getInstance()
							.getAuftragDelegate()
							.auftragFindByPrimaryKey(
									tpAuftrag.getAuftragDto().getIId());
					if (!aDtoVorhanden.getKundeIIdAuftragsadresse().equals(
							tpAuftrag.getAuftragDto()
									.getKundeIIdAuftragsadresse())) {

						DialogGeaenderteKonditionenVK dialog = new DialogGeaenderteKonditionenVK(
								tpAuftrag.getAuftragDto(), tpAuftrag
										.getAuftragDto()
										.getKundeIIdAuftragsadresse(),
								tpAuftrag.getAuftragDto()
										.getKundeIIdLieferadresse(),
								getInternalFrame());
						LPMain.getInstance().getDesktop()
								.platziereDialogInDerMitteDesFensters(dialog);

						if (dialog.bKonditionenUnterschiedlich == true) {
							dialog.setVisible(true);

							if (dialog.bAbgebrochen == false) {
								tpAuftrag.setAuftragDto((AuftragDto) dialog
										.getBelegVerkaufDto());
							} else {
								bUpdate = false;
							}
						}

					}

					if (bUpdate == true) {
						// vorschlagswerte aus dem kunden fuer konditionen
						// vorbelegen, wenn es sich um keinen Abrufauftrag
						// handelt

						// Wegen PJ 856 auskommentiert
						// if (!((String) wcoAuftragsart.getKeyOfSelectedItem())
						// .equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
						// initKonditionen();
						// }
						// aenderewaehrung: 5 Wenn die Waehrung geaendert wurde,
						// muessen die Belegwerte neu berechnet werden
						boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = DelegateFactory
								.getInstance()
								.getAuftragDelegate()
								.updateAuftrag(
										tpAuftrag.getAuftragDto(),
										waehrungOriDto == null ? null
												: waehrungOriDto.getCNr());

						if (bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben == true) {
							DialogFactory
									.showModalDialog(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.hint"),
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.error.mwstvonnullgeaendertundhandeingaben"));

						}

						setKeyWhenDetailPanel(tpAuftrag.getAuftragDto()
								.getIId());
					}
				}
			}

			DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.updateAuftragdokumente(tpAuftrag.getAuftragDto().getIId(),
							alAuftragsdokumenteZuSpeichern);

			super.eventActionSave(e, true);
			// Wenn eine neue Abrufauftrag angelegt wurde, schalten wir auf die
			// "Sicht Rahmen" um.
			if (!bUpdate) {
				if (wcoAuftragsart.getKeyOfSelectedItem().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)) {
					if (wcbDivisor.isSelected() || wcbRest.isSelected()) {
						if (wcbRest.isSelected()) {
							// rest ist wie divisor mit 1
							wnfDivisor.setInteger(1);
						}
						int iDivisor = this.wnfDivisor.getInteger();
						DelegateFactory
								.getInstance()
								.getAuftragRahmenAbrufDelegate()
								.erzeugeAbrufpositionen(
										tpAuftrag.getAuftragDto().getIId(),
										iDivisor);
						tpAuftrag.gotoSichtRahmen();
					} else if (wcbManuellAbruf.isSelected()) {
						// alle bespos mit restmenge in sicht rahmen anzeigen
						tpAuftrag.gotoSichtRahmen();
					}
				}
			}
			eventYouAreSelected(false);
		}
	}

	private void initKonditionen() throws Throwable {
		if (tpAuftrag.getKundeAuftragDto().getFRabattsatz() != null) {
			tpAuftrag.getAuftragDto().setFAllgemeinerRabattsatz(
					new Double(tpAuftrag.getKundeAuftragDto().getFRabattsatz()
							.doubleValue()));
		}
		if (tpAuftrag.getKundeAuftragDto() != null) {
			tpAuftrag.getAuftragDto().setIGarantie(
					tpAuftrag.getKundeAuftragDto().getIGarantieinmonaten());
		}
		if (tpAuftrag.getKundeAuftragDto().getLieferartIId() != null) {
			tpAuftrag.getAuftragDto().setLieferartIId(
					tpAuftrag.getKundeAuftragDto().getLieferartIId());
		}
		if (tpAuftrag.getKundeAuftragDto().getZahlungszielIId() != null) {
			tpAuftrag.getAuftragDto().setZahlungszielIId(
					tpAuftrag.getKundeAuftragDto().getZahlungszielIId());
		}
		if (tpAuftrag.getKundeAuftragDto().getSpediteurIId() != null) {
			tpAuftrag.getAuftragDto().setSpediteurIId(
					tpAuftrag.getKundeAuftragDto().getSpediteurIId());
		}

		if (tpAuftrag.getAuftragDto().getBMitzusammenfassung() == null) {
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_DEFAULT_MIT_ZUSAMMENFASSUNG,
							ParameterFac.KATEGORIE_ANGEBOT,
							LPMain.getTheClient().getMandant());

			boolean bMitZusammenfassung = (Boolean) parameter
					.getCWertAsObject();

			tpAuftrag.getAuftragDto().setBMitzusammenfassung(
					Helper.boolean2Short(bMitZusammenfassung));
		}

	}

	/**
	 * Drucke Auftragbestaetigung.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpAuftrag.printAuftragbestaetigung();
		// eventYouAreSelected(false);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAGART_CHANGED)) {
			if (wcoAuftragsart.getKeyOfSelectedItem().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				wlaLiefertermin.setText(LPMain.getInstance()
						.getTextRespectUISPr("lp.rahmentermin"));
				setVisibleAbrufe(true);
			} else {
				wlaLiefertermin.setText(LPMain.getInstance()
						.getTextRespectUISPr("label.liefertermin"));
				setVisibleAbrufe(false);
			}
			bAbrufbesUndNeu = false;
			if (wcoAuftragsart.getKeyOfSelectedItem().equals(
					AuftragServiceFac.AUFTRAGART_ABRUF)) {
				setVisisbleAbrufausrahmen(true);
				wtfRahmencnr.setMandatoryField(true);

				bAbrufbesUndNeu = (tpAuftrag != null)
						&& (tpAuftrag.getAuftragDto() != null);
				// && (tpAuftrag.getAuftragDto().getIId() == null);

				wlaLeer.setVisible(!bAbrufbesUndNeu);
				wnfDivisor.setMandatoryField(bAbrufbesUndNeu);
				jPanelAbrufBes.setVisible(bAbrufbesUndNeu);
				wnfDivisor.getText();
			} else {
				rahmenauftragDto = new AuftragDto();

				wtfRahmencnr.setText("");
				wtfRahmencnr.setMandatoryField(false);
				wtfRahmenbez.setText("");

				setVisisbleAbrufausrahmen(false);
				wnfDivisor.setMandatoryField(false);
				jPanelAbrufBes.setVisible(false);

			}
			if (wcoAuftragsart.getKeyOfSelectedItem().equals(
					AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
				wlaWiederholungsintervall.setVisible(true);
				wcoWiederholungsintervall.setVisible(true);
				wlaLauf.setVisible(true);
				wdfLauf.setVisible(true);
				wdfLauf.setMandatoryField(true);
			} else {
				wlaWiederholungsintervall.setVisible(false);
				wcoWiederholungsintervall.setVisible(false);
				wlaLauf.setVisible(false);
				wdfLauf.setVisible(false);
				wdfLauf.setMandatoryField(false);
			}
		} else if (e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_START)
				|| e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_STOP)) {

			LPMain.getInstance()
					.getDesktop()
					.zeitbuchungAufBeleg(e.getActionCommand(),
							LocaleFac.BELEGART_AUFTRAG,
							tpAuftrag.getAuftragDto().getIId());

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RAHMENAUSWAHL)) {
			dialogQueryRahmenauftragFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_AUFTRAG)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERSTECKEN)) {
			DelegateFactory.getInstance().getAuftragDelegate()
					.updateAuftragVersteckt(tpAuftrag.getAuftragDto().getIId());

			eventYouAreSelected(false);

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE)) {
			dialogQueryAnsprechpartner(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE)) {
			dialogQueryAnsprechpartnerLieferadresse(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE)) {
			dialogQueryAnsprechpartnerRechnungsadresse(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE)) {
			dialogQueryAbbuchungsLagerFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_KUNDE)) {
			dialogQueryVertreter(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG)) {
			dialogQueryLieferadresse(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG)) {
			dialogQueryRechnungsadresse(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_WAEHRUNG)) {
			dialogQueryWaehrung(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ORDERRESPONSE)) {
			if (tpAuftrag.getAuftragDto().getTResponse() == null) {
				dialogOrderResponseErzeugen();
			} else {
				boolean yes = DialogFactory
						.showModalJaNeinDialog(
								tpAuftrag.getInternalFrame(),
								LPMain.getTextRespectUISPr("auftrag.bereitsbestaetigt"));
				if (yes) {
					dialogOrderResponseErzeugen();
				}
			}
		}
	}

	private void dialogOrderResponseErzeugen() {
		JTextArea msgLabel;
		JProgressBar progressBar;
		final int MAXIMUM = 100;
		JPanel panel;

		progressBar = new JProgressBar(0, MAXIMUM);
		progressBar.setIndeterminate(true);
		msgLabel = new JTextArea(
				LPMain.getTextRespectUISPr("lp.versandweg.durchfuehren"));
		msgLabel.setEditable(false);

		panel = new JPanel(new BorderLayout(5, 5));
		panel.add(msgLabel, BorderLayout.PAGE_START);
		panel.add(progressBar, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

		final JDialog dialog = new JDialog();
		dialog.getContentPane().add(panel);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setSize(500, dialog.getHeight());
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(false);
		dialog.setVisible(true);
		msgLabel.setBackground(panel.getBackground());

		SwingWorker<String, Object> worker = new SwingWorker<String, Object>() {
			@Override
			protected void done() {
				dialog.dispose();
			}

			@Override
			protected String doInBackground() throws Exception {
				String result = "";
				try {
					result = DelegateFactory.getInstance().getAuftragDelegate()
							.createOrderResponsePost(tpAuftrag.getAuftragDto());
					publish(result);

					tpAuftrag
							.initializeDtos(tpAuftrag.getAuftragDto().getIId());
					updateOrderResponseButton();
				} catch (Throwable t) {
					handleException(t, false);
				}

				return result;
			}
		};

		worker.execute();
	}

	private void dialogQueryRahmenauftragFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRRahmenauswahl = AuftragFilterFactory.getInstance()
				.createPanelFLRRahmenauftrag(intFrame, true, false,
						rahmenauftragDto.getIId());

		new DialogQuery(panelQueryFLRRahmenauswahl);
	}

	private void dialogQueryWaehrung(ActionEvent e) throws Throwable {
		panelQueryFLRWaehrung = FinanzFilterFactory.getInstance()
				.createPanelFLRWaehrung(getInternalFrame(),
						waehrungDto.getCNr());
		new DialogQuery(panelQueryFLRWaehrung);
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(intFrame, true, false,
						tpAuftrag.getKundeAuftragDto().getIId());

		new DialogQuery(panelQueryFLRKunde);
	}

	private void dialogQueryAnsprechpartner(ActionEvent e) throws Throwable {
		if (tpAuftrag.getKundeAuftragDto().getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(
							intFrame,
							tpAuftrag.getKundeAuftragDto().getPartnerDto()
									.getIId(), ansprechpartnerDto.getIId(),
							true, true);

			new DialogQuery(panelQueryFLRAnsprechpartner);
		}
	}

	private void dialogQueryAnsprechpartnerLieferadresse(ActionEvent e)
			throws Throwable {
		if (kundeLieferadresseDto == null
				|| kundeLieferadresseDto.getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {

			panelQueryFLRAnsprechpartner_Lieferadresse = PartnerFilterFactory
					.getInstance().createPanelFLRAnsprechpartner(intFrame,
							kundeLieferadresseDto.getPartnerIId(),
							ansprechpartnerDtoLieferadresse.getIId(), true,
							true);

			new DialogQuery(panelQueryFLRAnsprechpartner_Lieferadresse);
		}
	}

	private void dialogQueryAnsprechpartnerRechnungsadresse(ActionEvent e)
			throws Throwable {
		if (kundeRechnungsadresseDto == null
				|| kundeRechnungsadresseDto.getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {

			panelQueryFLRAnsprechpartner_Rechungsadresse = PartnerFilterFactory
					.getInstance().createPanelFLRAnsprechpartner(intFrame,
							kundeRechnungsadresseDto.getPartnerIId(),
							ansprechpartnerDtoRechnungsadresse.getIId(), true,
							true);

			new DialogQuery(panelQueryFLRAnsprechpartner_Rechungsadresse);
		}
	}

	private void dialogQueryVertreter(ActionEvent e) throws Throwable {
		panelQueryFLRVertreter = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(intFrame, true, false,
						vertreterDto.getIId());

		new DialogQuery(panelQueryFLRVertreter);
	}

	void dialogQueryLieferadresse(ActionEvent e) throws Throwable {
		panelQueryFLRLieferadresse = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(intFrame, true, false,
						kundeLieferadresseDto.getIId());

		new DialogQuery(panelQueryFLRLieferadresse);
	}

	private void dialogQueryRechnungsadresse(ActionEvent e) throws Throwable {
		panelQueryFLRRechnungsadresse = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(intFrame, true, false,
						kundeRechnungsadresseDto.getIId());

		new DialogQuery(panelQueryFLRRechnungsadresse);
	}

	private void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false,
						kostenstelleDto.getIId());

		new DialogQuery(panelQueryFLRKostenstelle);
	}

	private void dto2Components() throws Throwable {
		if (tpAuftrag.getAuftragDto().getAngebotIId() != null) {
			AngebotDto angebotDto = DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.angebotFindByPrimaryKey(
							tpAuftrag.getAuftragDto().getAngebotIId());
			wtfAngebot.setText(angebotDto.getCNr());
		}

		if (Helper.short2boolean(tpAuftrag.getAuftragDto().getBVersteckt())) {
			wlaVersteckt.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.versteckt"));
		} else {
			wlaVersteckt.setText("");
		}

		wcoAuftragsart.setKeyOfSelectedItem(tpAuftrag.getAuftragDto()
				.getAuftragartCNr());

		wsfProjekt.setKey(tpAuftrag.getAuftragDto().getProjektIId());
		Integer auftragIIdRahmenauftrag = tpAuftrag.getAuftragDto()
				.getAuftragIIdRahmenauftrag();

		if (tpAuftrag.getAuftragDto().getAuftragartCNr()
				.equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {

			if (tpAuftrag.getAuftragDto().getWiederholungsintervallCNr() != null) {
				wcoWiederholungsintervall.setKeyOfSelectedItem(tpAuftrag
						.getAuftragDto().getWiederholungsintervallCNr());
			}

			wdfLauf.setTimestamp(tpAuftrag.getAuftragDto().getTLauftermin());
		}

		if (auftragIIdRahmenauftrag != null) {
			rahmenauftragDto = DelegateFactory.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey(auftragIIdRahmenauftrag);

			wtfRahmencnr.setText(rahmenauftragDto.getCNr());
			wtfRahmenbez.setText(rahmenauftragDto.getCBezProjektbezeichnung());
		}

		wdfBelegdatum.setDate(tpAuftrag.getAuftragDto().getTBelegdatum());
		wdfBestelldatum.setDate(tpAuftrag.getAuftragDto().getDBestelldatum());
		kundeAuftragDto2Components();

		// Ansprechpartner bestimmen
		Integer iIdAnsprechpartner = tpAuftrag.getAuftragDto()
				.getAnsprechparnterIId();
		if (iIdAnsprechpartner != null) {
			ansprechpartnerDto = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText("");
		}

		LagerDto lagerDto = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByPrimaryKey(
						tpAuftrag.getAuftragDto().getLagerIIdAbbuchungslager());
		wtfAbbuchungslager.setText(lagerDto.getCNr());

		// Ansprechpartner bestimmen
		Integer iIdAnsprechpartnerLieferadresse = tpAuftrag.getAuftragDto()
				.getAnsprechpartnerIIdLieferadresse();
		bestimmeUndZeigeAnsprechpartnerLieferadresse(iIdAnsprechpartnerLieferadresse);

		Integer iIdAnsprechpartnerRechnungsadresse = tpAuftrag.getAuftragDto()
				.getAnsprechpartnerIIdRechnungsadresse();
		bestimmeUndZeigeAnsprechpartnerRechnungsadresse(iIdAnsprechpartnerRechnungsadresse);

		// vertreter bestimmen, kann null sein
		Integer pkVertreter = tpAuftrag.getAuftragDto()
				.getPersonalIIdVertreter();
		if (pkVertreter != null) {
			vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(pkVertreter);
			vertreterDto2components();
		}

		// Lieferadresse bestimmen
		Integer pkKundeLiefer = tpAuftrag.getAuftragDto()
				.getKundeIIdLieferadresse();
		kundeLieferadresseDto = DelegateFactory.getInstance()
				.getKundeDelegate().kundeFindByPrimaryKey(pkKundeLiefer);
		kundeLieferadresseDto2components();

		// Rechnungsadresse bestimmen
		Integer pkKundeRechnung = tpAuftrag.getAuftragDto()
				.getKundeIIdRechnungsadresse();
		kundeRechnungsadresseDto = DelegateFactory.getInstance()
				.getKundeDelegate().kundeFindByPrimaryKey(pkKundeRechnung);
		kundeRechnungsadresseDto2components();

		kostenstelleDto = DelegateFactory
				.getInstance()
				.getSystemDelegate()
				.kostenstelleFindByPrimaryKey(
						tpAuftrag.getAuftragDto().getKostIId());
		wtfKostenstelle.setText(kostenstelleDto.getCBez());
		wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());

		this.wtfProjekt.setText(tpAuftrag.getAuftragDto()
				.getCBezProjektbezeichnung());
		this.wtfBestellnummer.setText(tpAuftrag.getAuftragDto()
				.getCBestellnummer());

		// Waehrung und Wechselkurs setzen
		wtfWaehrung.setText(tpAuftrag.getAuftragDto().getCAuftragswaehrung());
		wnfKurs.setDouble(tpAuftrag.getAuftragDto()
				.getFWechselkursmandantwaehrungzubelegwaehrung());
		wnfErfuellungsgrad.setDouble(tpAuftrag.getAuftragDto()
				.getFErfuellungsgrad());
		// @todo Sonderrabatt PJ 4799
		java.sql.Timestamp liefertermin = tpAuftrag.getAuftragDto()
				.getDLiefertermin();
		if (liefertermin != null) {
			wdfLiefertermin.setDate(new java.sql.Date(liefertermin.getTime()));
			if (tpAuftrag.getBAuftragterminstudenminuten())
				wtfZeitLiefertermin.setTime(new java.sql.Time(liefertermin
						.getTime()));
		}
		java.sql.Timestamp finaltermin = tpAuftrag.getAuftragDto()
				.getDFinaltermin();

		if (finaltermin != null) {
			wdfFinaltermin.setDate(new java.sql.Date(finaltermin.getTime()));
			if (tpAuftrag.getBAuftragterminstudenminuten())
				wtfZeitFinaltermin.setTime(new java.sql.Time(finaltermin
						.getTime()));
		}
		wcbUnverbindlich.setSelected(Helper.short2boolean(tpAuftrag
				.getAuftragDto().getBLieferterminUnverbindlich()));
		wcbTeillieferung.setSelected(Helper.short2boolean(tpAuftrag
				.getAuftragDto().getBTeillieferungMoeglich()));
		wcbPoenale.setSelected(Helper.short2boolean(tpAuftrag.getAuftragDto()
				.getBPoenale()));
		wcbRoHs.setSelected(Helper.short2boolean(tpAuftrag.getAuftragDto()
				.getBRoHs()));

		if (tpAuftrag.getAuftragDto().getPersonalIIdVerrechenbar() != null) {
			PersonalDto personalDtoVerrechnen = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							tpAuftrag.getAuftragDto()
									.getPersonalIIdVerrechenbar());

			wlaVerrechenbar.setText(LPMain
					.getTextRespectUISPr("iv.verrechenbargesetzt")
					+ " "
					+ Helper.formatDatum(tpAuftrag.getAuftragDto()
							.getTVerrechenbar(), LPMain.getTheClient()
							.getLocUi())
					+ ", "
					+ personalDtoVerrechnen.formatAnrede());

		} else {
			wlaVerrechenbar.setText("");
		}

		wnfLeihtage.setInteger(tpAuftrag.getAuftragDto().getILeihtage());

		if (tpAuftrag.getAuftragDto().getAuftragartCNr()
				.equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
			// alle Abrufe anzeigen
			AuftragDto[] aAbrufauftragDto = DelegateFactory
					.getInstance()
					.getAuftragDelegate()
					.abrufauftragFindByAuftragIIdRahmenauftrag(
							tpAuftrag.getAuftragDto().getIId());

			wtaAbrufe.setText(formatAuftraege(aAbrufauftragDto));
		}

		updateOrderResponseButton();
		aktualisiereStatusbar();
	}

	private void updateOrderResponseButton() throws Throwable, ExceptionLP {
		if (bZusatzfunktionVersandweg) {
			AuftragDto auftragDto = tpAuftrag.getAuftragDto();
			boolean hasOrderResponse = auftragDto != null
					&& auftragDto.getTResponse() != null;
			JButton button = getHmOfButtons().get(ACTION_SPECIAL_ORDERRESPONSE)
					.getButton();
			button.setIcon(hasOrderResponse ? RESPONSE_ICON_DONE
					: RESPONSE_ICON);

			boolean enable = auftragDto != null
					&& auftragDto.getIId() != null
					&& auftragDto.getTResponse() == null
					&& LocaleFac.STATUS_OFFEN.equals(auftragDto.getStatusCNr())
					&& DelegateFactory.getInstance().getAuftragDelegate()
							.hatAuftragVersandweg(auftragDto);
			if (hasOrderResponse) {
				enable = true;
			}
			enableToolsPanelButtons(enable, ACTION_SPECIAL_ORDERRESPONSE);
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAuftrag.getAuftragDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAuftrag.getAuftragDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAuftrag.getAuftragDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAuftrag.getAuftragDto().getTAendern());

		String add2Status = "";
		Timestamp tErledigt = null;
		String kurzzeichen = null;

		if (tpAuftrag.getAuftragDto().getTErledigt() != null) {
			tErledigt = tpAuftrag.getAuftragDto().getTErledigt();
		} else {
			if (tpAuftrag.getAuftragDto().getTManuellerledigt() != null) {
				tErledigt = tpAuftrag.getAuftragDto().getTManuellerledigt();
			}
		}
		if (tpAuftrag.getAuftragDto().getPersonalIIdErledigt() != null) {

			kurzzeichen = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							tpAuftrag.getAuftragDto().getPersonalIIdErledigt())
					.getCKurzzeichen();
		} else {
			if (tpAuftrag.getAuftragDto().getPersonalIIdManuellerledigt() != null) {
				kurzzeichen = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								tpAuftrag.getAuftragDto()
										.getPersonalIIdManuellerledigt())
						.getCKurzzeichen();
			}
		}

		if (tErledigt != null) {
			add2Status += "/"
					+ Helper.formatDatum(tErledigt, LPMain.getTheClient()
							.getLocUi());
		}
		if (kurzzeichen != null) {
			add2Status += "/" + kurzzeichen;
		}

		setStatusbarStatusCNr(tpAuftrag.getAuftragStatus());
		getPanelStatusbar().addToSpalteStatus(add2Status);
		String status = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_AUFTRAG,
						tpAuftrag.getAuftragDto().getIId());
		if (status != null) {
			status = LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status;
		}
		setStatusbarSpalte5(status);
	}

	private void dialogQueryAbbuchungsLagerFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRAbbuchungsLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						tpAuftrag.getAuftragDto().getLagerIIdAbbuchungslager(),
						true, false);

		new DialogQuery(panelQueryFLRAbbuchungsLager);
	}

	/**
	 * Die Eigenschaften des Vertreters beim Kunden zur Anzeige bringen.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private void vertreterDto2components() throws Throwable {
		if (vertreterDto != null && vertreterDto.getIId() != null) {
			wtfVertreter.setText(vertreterDto.getPartnerDto()
					.formatTitelAnrede());
		}
	}

	/**
	 * Die Eigenschaften des Auftragskunden zur Anzeige bringen.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private void kundeAuftragDto2Components() throws Throwable {
		jButtonKunde.setOKey(tpAuftrag.getKundeAuftragDto().getIId());
		wtfKundeAuftrag.setText(tpAuftrag.getKundeAuftragDto().getPartnerDto()
				.formatTitelAnrede());
		String sAdresse = tpAuftrag.getKundeAuftragDto().getPartnerDto()
				.formatAdresse();
		if (tpAuftrag.getKundeAuftragDto().getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  "
				+ tpAuftrag.getKundeAuftragDto().getPartnerDto().getCKbez();
		wtfKundeAuftragAdresse.setText(sAdresse);
		wtfKundeAuftragAbteilung.setText(tpAuftrag.getKundeAuftragDto()
				.getPartnerDto().getCName3vorname2abteilung());

		// default waehrung des auftrags kommt aus dem kunden
		waehrungDto = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.waehrungFindByPrimaryKey(
						tpAuftrag.getKundeAuftragDto().getCWaehrung());
		wtfWaehrung.setText(waehrungDto.getCNr());
		setzeWechselkurs();

		// default kostenstelle des auftrags kommt aus dem kunden
		if (tpAuftrag.getKundeAuftragDto().getKostenstelleIId() != null) {
			kostenstelleDto = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.kostenstelleFindByPrimaryKey(
							tpAuftrag.getKundeAuftragDto().getKostenstelleIId());
			wtfKostenstelle.setText(kostenstelleDto.getCBez());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		}

		// default fuer teillieferung erlaubt im auftrag kommt aus dem kunden
		wcbTeillieferung.setSelected(Helper.short2boolean(tpAuftrag
				.getKundeAuftragDto().getBAkzeptiertteillieferung()));

		// @todo in diesem Moment muessen sich alle sprachabh Felder des
		// Auftrags aendern? PJ 4800
	}

	/**
	 * Die Eigenschaften des Kunden der Lieferadresse zur Ansicht bringen.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private void kundeLieferadresseDto2components() throws Throwable {
		String sAdresse = kundeLieferadresseDto.getPartnerDto()
				.formatTitelAnrede();
		if (kundeLieferadresseDto.getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  "
				+ kundeLieferadresseDto.getPartnerDto().getCKbez();
		wtfKundeLieferadresse.setText(sAdresse);
	}

	/**
	 * Die Eigenschaften des Kunden der Rechnungsadresse zur Anzeige bringen.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private void kundeRechnungsadresseDto2components() throws Throwable {
		String sAdresse = kundeRechnungsadresseDto.getPartnerDto()
				.formatTitelAnrede();
		if (kundeRechnungsadresseDto.getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  "
				+ kundeRechnungsadresseDto.getPartnerDto().getCKbez();
		wtfKundeRechnungsadresse.setText(sAdresse);
	}

	/**
	 * Alle Auftragdaten aus dem Panel sammeln.
	 * 
	 * @throws Throwable
	 */
	private void components2auftragDto() throws Throwable {
		if (tpAuftrag.getAuftragDto() == null
				|| tpAuftrag.getAuftragDto().getIId() == null) {
			// tpAuftrag.setAuftragDto(new AuftragDto());
			tpAuftrag.getAuftragDto().setMandantCNr(
					LPMain.getInstance().getTheClient().getMandant());
			tpAuftrag.getAuftragDto().setAuftragstatusCNr(
					AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
		}

		tpAuftrag.getAuftragDto().setAuftragartCNr(
				(String) wcoAuftragsart.getKeyOfSelectedItem());
		tpAuftrag.getAuftragDto().setProjektIId(wsfProjekt.getIKey());
		if (rahmenauftragDto != null
				&& wcoAuftragsart.getKeyOfSelectedItem().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)) {
			tpAuftrag.getAuftragDto().setAuftragIIdRahmenauftrag(
					rahmenauftragDto.getIId());

		}

		if (wcoAuftragsart.getKeyOfSelectedItem().equals(
				AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {

			if (tpAuftrag.getAuftragDto().getIId() != null
					&& tpAuftrag.getAuftragDto().getWiederholungsintervallCNr() != null
					&& !tpAuftrag
							.getAuftragDto()
							.getWiederholungsintervallCNr()
							.equals((String) wcoWiederholungsintervall
									.getKeyOfSelectedItem())) {
				// Art wurde geaendert, Datum berechnen
				java.sql.Date d = DelegateFactory
						.getInstance()
						.getAuftragDelegate()
						.getAuftragWiederholungsstart(
								tpAuftrag.getAuftragDto().getIId());
				if (d != null)
					wdfLauf.setTimestamp(new Timestamp(d.getTime()));
			}
			tpAuftrag.getAuftragDto().setWiederholungsintervallCNr(
					(String) wcoWiederholungsintervall.getKeyOfSelectedItem());
			tpAuftrag.getAuftragDto().setTLauftermin(wdfLauf.getTimestamp());
		}

		tpAuftrag.getAuftragDto().setTBelegdatum(wdfBelegdatum.getTimestamp());
		tpAuftrag.getAuftragDto().setDBestelldatum(
				wdfBestelldatum.getTimestamp());
		tpAuftrag.getAuftragDto().setKundeIIdAuftragsadresse(
				tpAuftrag.getKundeAuftragDto().getIId());
		tpAuftrag.getAuftragDto().setAnsprechpartnerIId(
				ansprechpartnerDto.getIId());
		tpAuftrag.getAuftragDto().setAnsprechpartnerIIdLieferadresse(
				ansprechpartnerDtoLieferadresse.getIId());
		tpAuftrag.getAuftragDto().setAnsprechpartnerIIdRechnungsadresse(
				ansprechpartnerDtoRechnungsadresse.getIId());
		tpAuftrag.getAuftragDto()
				.setPersonalIIdVertreter(vertreterDto.getIId());
		tpAuftrag.getAuftragDto().setKundeIIdLieferadresse(
				kundeLieferadresseDto.getIId());
		tpAuftrag.getAuftragDto().setKundeIIdRechnungsadresse(
				kundeRechnungsadresseDto.getIId());
		tpAuftrag.getAuftragDto().setCBezProjektbezeichnung(
				wtfProjekt.getText());
		tpAuftrag.getAuftragDto().setCBestellnummer(wtfBestellnummer.getText());
		tpAuftrag.getAuftragDto().setCAuftragswaehrung(wtfWaehrung.getText());
		tpAuftrag.getAuftragDto()
				.setFWechselkursmandantwaehrungzubelegwaehrung(
						this.wnfKurs.getDouble());
		tpAuftrag.getAuftragDto().setBLieferterminUnverbindlich(
				Helper.boolean2Short(wcbUnverbindlich.isSelected()));

		if (tpAuftrag.getBAuftragterminstudenminuten()) {
			java.sql.Timestamp tsLiefertermin = wdfLiefertermin.getTimestamp();
			tsLiefertermin = Helper.cutTimestamp(tsLiefertermin);

			Calendar cLTDatum = Calendar.getInstance();
			cLTDatum.setTimeInMillis(tsLiefertermin.getTime());

			Calendar cLTZeit = Calendar.getInstance();
			cLTZeit.setTimeInMillis(wtfZeitLiefertermin.getTime().getTime());

			cLTDatum.set(Calendar.HOUR_OF_DAY,
					cLTZeit.get(Calendar.HOUR_OF_DAY));
			cLTDatum.set(Calendar.MINUTE, cLTZeit.get(Calendar.MINUTE));
			cLTDatum.set(Calendar.SECOND, cLTZeit.get(Calendar.SECOND));
			cLTDatum.set(Calendar.MILLISECOND,
					cLTZeit.get(Calendar.MILLISECOND));
			tsLiefertermin.setTime(cLTDatum.getTimeInMillis());
			tpAuftrag.getAuftragDto().setDLiefertermin(tsLiefertermin);

			java.sql.Timestamp tsFinaltermin = wdfFinaltermin.getTimestamp();
			tsFinaltermin = Helper.cutTimestamp(tsFinaltermin);

			Calendar cFTDatum = Calendar.getInstance();
			cFTDatum.setTimeInMillis(tsFinaltermin.getTime());

			Calendar cFTZeit = Calendar.getInstance();
			cFTZeit.setTimeInMillis(wtfZeitFinaltermin.getTime().getTime());

			cFTDatum.set(Calendar.HOUR_OF_DAY,
					cFTZeit.get(Calendar.HOUR_OF_DAY));
			cFTDatum.set(Calendar.MINUTE, cFTZeit.get(Calendar.MINUTE));
			cFTDatum.set(Calendar.SECOND, cFTZeit.get(Calendar.SECOND));
			cFTDatum.set(Calendar.MILLISECOND,
					cFTZeit.get(Calendar.MILLISECOND));
			tsFinaltermin.setTime(cFTDatum.getTimeInMillis());
			tpAuftrag.getAuftragDto().setDFinaltermin(tsFinaltermin);

		} else {
			tpAuftrag.getAuftragDto().setDLiefertermin(
					Helper.cutTimestamp(new Timestamp(wdfLiefertermin.getDate()
							.getTime())));
			tpAuftrag.getAuftragDto().setDFinaltermin(
					Helper.cutTimestamp(new Timestamp(wdfFinaltermin.getDate()
							.getTime())));
		}
		tpAuftrag.getAuftragDto().setBRoHs(
				Helper.boolean2Short(wcbRoHs.isSelected()));
		tpAuftrag.getAuftragDto().setKostIId(kostenstelleDto.getIId());
		tpAuftrag.getAuftragDto().setBTeillieferungMoeglich(
				Helper.boolean2Short(wcbTeillieferung.isSelected()));
		tpAuftrag.getAuftragDto().setBPoenale(
				Helper.boolean2Short(wcbPoenale.isSelected()));
		tpAuftrag.getAuftragDto().setBRoHs(
				Helper.boolean2Short(wcbRoHs.isSelected()));
		tpAuftrag.getAuftragDto().setILeihtage(wnfLeihtage.getInteger());
	}

	void jComboBoxAuftragsart_actionPerformed(ActionEvent e) {

	}

	/**
	 * Die Waehrung ist aenderbar, wenn ... ... ein neuer Auftrag angelegt wird
	 * ... der Auftrag den Status ('Angelegt' oder 'Offen') hat und keine
	 * mengenbehafteten Positionen hat und der Datensatz gelockt ist. <br>
	 * Wenn die Waehrung nicht geaendert werden darf, darf auch der Kunde nicht
	 * geaendert werden, da dieser seine Waehrung mitbringt.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	/*
	 * private void setzeWaehrungAenderbar() throws Throwable { if
	 * (tpAuftrag.getAuftragDto() != null) { if
	 * (tpAuftrag.getAuftragDto().getIId() == null ||
	 * ((tpAuftrag.getAuftragDto()
	 * .getAuftragstatusCNr().equals(AuftragServiceFac. AUFTRAGSTATUS_ANGELEGT)
	 * ||
	 * tpAuftrag.getAuftragDto().getAuftragstatusCNr().equals(AuftragServiceFac.
	 * AUFTRAGSTATUS_OFFEN)) &&
	 * DelegateFactory.getInstance().getAuftragpositionDelegate
	 * ().getAnzahlMengenbehafteteAuftragpositionen(
	 * tpAuftrag.getAuftragDto().getIId()) == 0) && isLocked()) {
	 * wbuWaehrung.setEnabled(true); jButtonKunde.setEnabled(true); } else {
	 * wbuWaehrung.setEnabled(false); jButtonKunde.setEnabled(false); } } }
	 */

	/**
	 * Einige Eingaben koennen nur unter bestimmten Voraussetzungen gemacht
	 * werden. <br>
	 * Betrifft:
	 * <ul>
	 * <li>Auftragart
	 * <li>Waehrung
	 * </ul>
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private void enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen()
			throws Throwable {
		boolean bEnableComponents = false;

		// Schritt 1: Ein bestehender Auftrag muss durch den aktuellen Benutzer
		// gelockt sein
		if (tpAuftrag.getAuftragDto().getIId() != null && isLocked()) {
			// Schritt 1a: Der Auftrag befindet sich im Status Angelegt oder
			// Offen
			if (tpAuftrag.getAuftragDto().getAuftragstatusCNr()
					.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
					|| tpAuftrag.getAuftragDto().getAuftragstatusCNr()
							.equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
				// Schritt 1b: Der Auftrag hat keine mengenbehafteten Positionen
				if (DelegateFactory
						.getInstance()
						.getAuftragpositionDelegate()
						.getAnzahlMengenbehafteteAuftragpositionen(
								tpAuftrag.getAuftragDto().getIId()) == 0) {
					bEnableComponents = true;
				}
				if (tpAuftrag.getAuftragDto().getAuftragartCNr()
						.equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
					boolean bEnable = DelegateFactory
							.getInstance()
							.getAuftragDelegate()
							.darfWiederholungsTerminAendern(
									tpAuftrag.getAuftragDto().getIId());
					wdfLauf.setEnabled(bEnable);
				}
			}
		}
		// Schritt 2: Es handelt sich um einen neuen Auftrag
		else if (tpAuftrag.getAuftragDto().getIId() == null) {
			bEnableComponents = true;
		}

		wcoAuftragsart.setEnabled(bEnableComponents);
		// jButtonKunde.setEnabled(bEnableComponents); UW 24.03.06 Der Kunde
		// darf geaendert werden
		wbuWaehrung.setEnabled(bEnableComponents);

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_AUFT_AUFTRAG_CUD)) {

			if (tpAuftrag.getAuftragDto() != null
					&& tpAuftrag.getAuftragDto().getIId() != null) {

				((LPButtonAction) getHmOfButtons().get(
						ACTION_SPECIAL_VERSTECKEN)).getButton().setEnabled(
						!isLocked());
			}
		}

		// PJ18129
		if (tpAuftrag.getAuftragDto().getAngebotIId() != null) {
			AngebotDto angebotDto = DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.angebotFindByPrimaryKey(
							tpAuftrag.getAuftragDto().getAngebotIId());
			if (angebotDto.getProjektIId() != null) {
				wsfProjekt.setEnabled(false);
				wsfProjekt.getWrapperGotoButton().setEnabled(false);
			}

		}

	}
	
	@Override
	public void eventActionLock(ActionEvent e) throws Throwable {
		// ist public wegen Termin verschieben aus TabbedPaneAuftrag
		super.eventActionLock(e);
	}
	
	@Override
	public void eventActionUnlock(ActionEvent e) throws Throwable {
		// ist public wegen Termin verschieben aus TabbedPaneAuftrag
		super.eventActionUnlock(e);
	}

	public void setzeWechselkurs() throws Throwable {
		if (waehrungDto != null && waehrungDto.getCNr() != null) {
			try {
				wnfKurs.setBigDecimal(DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.getWechselkurs2(
								LPMain.getInstance().getTheClient()
										.getSMandantenwaehrung(),
								waehrungDto.getCNr()));
			} catch (Throwable t) {
				handleException(t, true); // UW: muss bleiben
				wnfKurs.setBigDecimal(null); // wnfKurs ist mandatory!
				eventActionUnlock(null);
				getInternalFrame().enableAllPanelsExcept(true);
				tpAuftrag.gotoAuswahl();
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false); // Stati aller Components
		// aktualisieren

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpAuftrag.getAuftragDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		// einen bestehenden Auftrag anzeigen
		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			tpAuftrag.setAuftragDto(DelegateFactory.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey((Integer) oKey));
			tpAuftrag.setKundeAuftragDto(DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							tpAuftrag.getAuftragDto()
									.getKundeIIdAuftragsadresse()));

			dto2Components();

			tpAuftrag.setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
					"auft.title.panel.kopfdaten"));
		}

		tpAuftrag.getAuftragKopfdaten().updateButtons(
				getLockedstateDetailMainKey());
		enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen(); // unbedingt
		// nach
		// updateButtons
		// (
		// )
		// aufrufen
		tpAuftrag.enablePanelsNachBitmuster(getLockedstateDetailMainKey());

		if (tpAuftrag.getAuftragDto() != null
				&& tpAuftrag.getAuftragDto().getIId() != null) {
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START)
						.getButton().setEnabled(true);
			}
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP)
						.getButton().setEnabled(true);
			}
		}
		if (tpAuftrag.getAuftragDto() != null
				&& tpAuftrag.getAuftragDto().getAuftragstatusCNr() != null
				&& tpAuftrag.getAuftragDto().getAuftragstatusCNr()
						.equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START)
						.getButton().setEnabled(false);
			}
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP)
						.getButton().setEnabled(false);
			}
		}

	}

	private void bestimmeUndZeigeAnsprechpartnerLieferadresse(
			Integer iIdAnsprechpartnerI) throws Throwable {
		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerDtoLieferadresse = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);

			wtfAnsprechpartnerLieferadresse
					.setText(ansprechpartnerDtoLieferadresse.getPartnerDto()
							.formatTitelAnrede());
		} else {
			wtfAnsprechpartnerLieferadresse.setText("");
		}
	}

	private void bestimmeUndZeigeAnsprechpartnerRechnungsadresse(
			Integer iIdAnsprechpartnerI) throws Throwable {
		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerDtoRechnungsadresse = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);

			wtfAnsprechpartnerRechungsadresse
					.setText(ansprechpartnerDtoRechnungsadresse.getPartnerDto()
							.formatTitelAnrede());
		} else {
			wtfAnsprechpartnerRechungsadresse.setText("");
		}
	}

	/**
	 * Wenn in Liefertermin oder Finaltermin ein neues Datum gewaehlt wurde,
	 * dann landet man hier.
	 * 
	 * @param evt
	 *            Ereignis
	 */
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getSource() == wdfLiefertermin.getDisplay()
				&& evt.getPropertyName().equals("date")) {
			wdfFinaltermin.setMinimumValue(wdfLiefertermin.getDate());

			if (wdfFinaltermin.getDate() != null
					&& wdfLiefertermin.getDate() != null) {
				if (wdfFinaltermin.getDate().before(wdfLiefertermin.getDate())) {
					wdfFinaltermin.setDate(wdfLiefertermin.getDate());
				}
			}
		} else if (evt.getSource() == wtfWaehrung
				&& evt.getPropertyName().equals("value")) {
			try {
				setzeWechselkurs();
			} catch (Throwable t) {
				myLogger.error(t.getLocalizedMessage(), t); // @todo sanieren
				// wie bei den
				// anderen Panels...
				// PJ 4809
			}
		}
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		switch (exfc.getICode()) {
		case EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			break;

		case EJBExceptionLP.FEHLER_KEIN_PARTNER_GEWAEHLT:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
			break;

		case EJBExceptionLP.FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.manuellerledigen"));
			break;

		case EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT:
			MessageFormat mf = new MessageFormat(LPMain.getInstance()
					.getTextRespectUISPr("lp.error.keinwechselkurshinterlegt"));

			String cWaehrungMandant = null;

			try {
				cWaehrungMandant = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								LPMain.getInstance().getTheClient()
										.getMandant()).getWaehrungCNr();
				mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());
			} catch (Throwable t) {
				LPMain.getInstance().exitFrame(getInternalFrame(), t);
			}

			Object pattern[] = { cWaehrungMandant, waehrungDto.getCNr() };

			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), mf.format(pattern));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}

	private String formatAuftraege(AuftragDto[] aAuftragDtoI) {
		String cFormat = "";

		// nach jeweils 4 Abrufen einen Zeilenumbruch einfuegen
		int iAnzahl = 0;

		if (aAuftragDtoI != null && aAuftragDtoI.length > 0) {
			for (int i = 0; i < aAuftragDtoI.length; i++) {
				cFormat += aAuftragDtoI[i].getCNr();
				iAnzahl++;

				if (iAnzahl == 5) {
					cFormat += "\n";
					iAnzahl = 0;
				} else {
					cFormat += " | ";
				}
			}
		}

		if (cFormat.length() > 3 && iAnzahl != 0) {
			cFormat = cFormat.substring(0, cFormat.length() - 3);
		}

		return cFormat;
	}

	private void setVisisbleAbrufausrahmen(boolean bVisible) {
		wbuRahmenauswahl.setVisible(bVisible);
		wtfRahmencnr.setVisible(bVisible);
		wtfRahmenbez.setVisible(bVisible);
	}

	private void setVisibleAbrufe(boolean bVisible) {
		wlaAbrufe.setVisible(bVisible);
		wtaAbrufe.setVisible(bVisible);
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
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_ABRUFE_DEFAULT_ABRUFART_AUFTRAG);
		} catch (Throwable e) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hinweis"), LPMain.getInstance()
					.getTextRespectUISPr("lp.abrufemenge.parameter.inkorrekt"));
		}
		if (mandantParam != null) {
			String sParamValue = mandantParam.getCWert().toUpperCase();
			if (sParamValue.equals("REST")) {
				wcbRest.setSelected(true);
			} else if (sParamValue.equals("MANUELL")) {
				wcbManuellAbruf.setSelected(true);
			} else if (sParamValue.equals("DIVISOR")) {
				wcbDivisor.setSelected(true);
			} else {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.hinweis"),
						LPMain.getInstance().getTextRespectUISPr(
								"lp.abrufemenge.parameter.inkorrekt"));
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

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (tpAuftrag.getAuftragDto().getIId() != null
				&& lockStateValue.getIState() != PanelBasis.LOCK_FOR_NEW
				&& lockStateValue.getIState() != PanelBasis.LOCK_IS_LOCKED_BY_ME
				&& lockStateValue.getIState() != PanelBasis.LOCK_IS_LOCKED_BY_OTHER_USER) {
			if (tpAuftrag.getAuftragDto().getAuftragstatusCNr()
					.equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				// Drucken kann man nur, wenn die Konditionen und
				// mengenbehaftete Positionen erfasst wurden
				if (tpAuftrag.getAuftragDto().getAuftragIIdKopftext() != null
						&& tpAuftrag.getAuftragDto().getAuftragIIdFusstext() != null
						&& DelegateFactory
								.getInstance()
								.getAuftragpositionDelegate()
								.getAnzahlMengenbehafteteAuftragpositionen(
										tpAuftrag.getAuftragDto().getIId()) > 0) {
					lockStateValue = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY);
				} else {
					lockStateValue = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDUPDATE_ONLY);
				}
			} else if (tpAuftrag.getAuftragDto().getAuftragstatusCNr()
					.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
					|| tpAuftrag.getAuftragDto().getAuftragstatusCNr()
							.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)
					|| tpAuftrag.getAuftragDto().getAuftragstatusCNr()
							.equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
				// Button Stornieren ist nicht verfuegbar, in diesen Faellen
				// wird ein echtes Update ausgeloest
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		tpAuftrag.enablePanelsNachBitmuster();

		if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
			this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START)
					.getButton().setEnabled(true);
		}
		if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
			this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP).getButton()
					.setEnabled(true);
		}
		if (tpAuftrag.getAuftragDto() != null
				&& tpAuftrag.getAuftragDto().getAuftragstatusCNr() != null
				&& tpAuftrag.getAuftragDto().getAuftragstatusCNr()
						.equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START)
						.getButton().setEnabled(false);
			}
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP)
						.getButton().setEnabled(false);
			}
		}

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		tpAuftrag.enablePanelsNachBitmuster();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		if (tpAuftrag.getAuftragDto().getIId() == null) {
			return jButtonKunde;
		} else {
			return wtfProjekt;
		}
	}
}
