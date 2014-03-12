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
package com.lp.client.lieferschein;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
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
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>In diesem Detailfenster des Lieferscheins werden Kopfdaten erfasst bzw.
 * geaendert.</p> <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-10-13</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.32 $
 */
public class PanelLieferscheinKopfdaten extends PanelBasis implements
		PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameLieferschein intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneLieferschein tpLieferschein = null;

	// dtos in diesem panel
	private AnsprechpartnerDto ansprechpartnerDto = null;
	private AnsprechpartnerDto ansprechpartnerDtoRechnungsadresse = null;
	private PersonalDto vertreterDto = null;
	private KundeDto kundeRechnungsadresseDto = null;
	private AuftragDto auftragDto = null;
	private WaehrungDto waehrungDto = null;
	private LagerDto lagerDto = null;
	private LagerDto zielLagerDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private java.sql.Date tmpDatum = null;

	// die urspruengliche Belegwaehrung hinterlegen
	private WaehrungDto waehrungOriDto = null;

	private JPanel jPanelWorkingOn = null;

	public final static String ACTION_SPECIAL_LIEFERSCHEINART_CHANGED = "action_special_lieferscheinart_changed";
	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_from_liste";
	static final public String ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE = "action_ansprechpartner_from_liste";
	static final public String ACTION_SPECIAL_VERTRETER_FROM_LISTE = "action_vertreter_from_liste";
	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "action_kundelieferadresse_from_liste";
	static final public String ACTION_SPECIAL_LIEFERANT_FROM_LISTE = "action_lieferantlieferadresse_from_liste";
	static final public String ACTION_SPECIAL_RECHNUNGSADRESSE_FROM_LISTE = "action_rechnungsadresse_from_liste";
	static final public String ACTION_SPECIAL_WAEHRUNG = "action_special_waehrung";
	static final public String ACTION_SPECIAL_LAGER = "action_special_lager";
	static final public String ACTION_SPECIAL_ZIELLAGER = "action_special_ziellager";
	static final public String ACTION_SPECIAL_KOSTENSTELLE = "action_special_kostenstelle";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE = "action_special_ansprechpartner_rechungsadresse";
	public final static String MY_OWN_NEW_TOGGLE_ZOLLEXPORTPAPIER_ERHALTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_ZOLLIMPORTPAPIER_ERHALTEN";
	private static final String ACTION_SPECIAL_LIEFERAVISO = "action_special_lieferaviso" ;

	private WrapperButton wbuAnsprechpartnerRechungsadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Rechungsadresse = null;
	private WrapperTextField wtfAnsprechpartnerRechungsadresse = null;

	private WrapperLabel labelLieferscheinart = null;
	private WrapperComboBox wcoLieferscheinart = null;
	private WrapperLabel labelDatum = null;
	private WrapperDateField wdfBelegdatum = null;

	private WrapperButton wbuAuftragauswahl = null;
	private PanelQueryFLR panelQueryFLRAuftragauswahl = null;

	private WrapperButton wbuAnsprechpartnerauswahl = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerauswahl = null;

	private WrapperButton wbuVertreterauswahl = null;
	private PanelQueryFLR panelQueryFLRVertreterauswahl = null;

	private WrapperGotoButton wbuKunde = null;
	private PanelQueryFLR panelQueryFLRKundenauswahl = null;

	private WrapperButton wbuLieferantenauswahl = null;
	private PanelQueryFLR panelQueryFLRLieferantenauswahl = null;

	private WrapperGotoButton wbuKundeRechnungsadresse = null;
	private PanelQueryFLR panelQueryFLRRechnungsadresseauswahl = null;

	private WrapperButton wbuLager = null;
	private PanelQueryFLR panelQueryFLRLager = null;

	private WrapperButton wbuZielLager = null;
	private PanelQueryFLR panelQueryFLRZielLager = null;

	private WrapperButton wbuKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;

	private WrapperTextField wtfAuftragnummer = null;
	private WrapperTextField wtfAuftragbezeichnung = null;
	private WrapperTextField wtfKundeLieferadresse = null;

	private WrapperLabel wlaAdresse = null;
	private WrapperTextField wtfKundeLieferadresseAdresse = null;
	private WrapperLabel wlaKundeLieferadresseAbteilung = null;
	private WrapperTextField wtfKundeLieferadresseAbteilung = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private WrapperTextField wtfVertreter = null;
	private WrapperTextField wtfKundeRechnungsadresse = null;

	private WrapperButton wbuWaehrung = null;
	private PanelQueryFLR panelQueryFLRWaehrung = null;
	private WrapperTextField wtfWaehrung = null;

	private WrapperLabel wlaKurs = null;
	private WrapperNumberField wnfKurs = null;

	private WrapperLabel labelProjekt = null;
	private WrapperLabel labelBestellnummer = null;
	private WrapperTextField wtfProjekt = null;
	private WrapperTextField wtfBestellnummer = null;

	private WrapperLabel labelKommission = null;
	private WrapperTextField wtfKommission = null;

	private WrapperTextField wtfLager = null;
	private WrapperTextField wtfZielLager = null;
	private WrapperTextField wtfKostenstelle = null;
	private WrapperTextField wtfKostenstelleBezeichnung = null;

	private WrapperGotoButton wbuRechnung = null;
	private WrapperTextField wtfRechnungcnr = null;

	private WrapperSelectField wsfProjekt = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), true);
	
	private boolean bZusatzfunktionVersandweg = false ;

	private static final ImageIcon AVISO_ICON = HelperClient
			.createImageIcon("data_out.png");
	private static final ImageIcon AVISO_ICON_DONE = HelperClient
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
	public PanelLieferscheinKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameLieferschein) getInternalFrame();
		tpLieferschein = ((InternalFrameLieferschein) getInternalFrame())
				.getTabbedPaneLieferschein();

		bZusatzfunktionVersandweg = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_VERSANDWEG) ;
		
		jbInit();
		initComponents();
		initPanel();
	}

	private String getIconNameLieferaviso() {
		String iconName = "/com/lp/client/res/data_out.png" ;
		if(tpLieferschein.getLieferscheinDto() != null && 
				tpLieferschein.getLieferscheinDto().getTLieferaviso() != null) {
			iconName = "/com/lp/client/res/data_out_green.png" ;
		}
		return iconName ;
	}
	
	void jbInit() throws Throwable {
		setLayout(new GridBagLayout());

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD, PanelBasis.ACTION_PRINT };
		enableToolsPanelButtons(aWhichButtonIUse);

		if(bZusatzfunktionVersandweg) {
			createAndSaveAndShowButton(getIconNameLieferaviso(),
					LPMain.getInstance().getTextRespectUISPr(
							"ls.lieferaviso"),
					ACTION_SPECIAL_LIEFERAVISO, KeyStroke.getKeyStroke('L',
							java.awt.event.InputEvent.CTRL_MASK),
					RechteFac.RECHT_LS_LIEFERSCHEIN_VERSAND);
		}
		
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		labelLieferscheinart = new WrapperLabel();
		labelLieferscheinart.setText(LPMain
				.getTextRespectUISPr("label.lieferscheinart"));

		labelDatum = new WrapperLabel();
		labelDatum.setText(LPMain.getTextRespectUISPr("label.belegdatum"));

		wcoLieferscheinart = new WrapperComboBox();
		wcoLieferscheinart.setMandatoryFieldDB(true);
		wcoLieferscheinart
				.setActionCommand(ACTION_SPECIAL_LIEFERSCHEINART_CHANGED);
		wcoLieferscheinart.addActionListener(this);

		wdfBelegdatum = new WrapperDateField();
		wdfBelegdatum.setMandatoryField(true);
		boolean bDarf = false;
		try {
			bDarf = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);
		} catch (Throwable ex) {
			handleException(ex, true);
		}
		if (!bDarf) {
			ParametermandantDto parametermandantDto = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
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
		}

		wbuAuftragauswahl = new WrapperButton();
		wbuAuftragauswahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.auftrag"));
		wbuAuftragauswahl
				.setActionCommand(this.ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftragauswahl.addActionListener(this);

		wbuKunde = new WrapperGotoButton(WrapperGotoButton.GOTO_KUNDE_AUSWAHL);
		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wbuKunde.setActionCommand(this.ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);

		wbuLieferantenauswahl = new WrapperButton();
		wbuLieferantenauswahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferant"));
		wbuLieferantenauswahl
				.setActionCommand(this.ACTION_SPECIAL_LIEFERANT_FROM_LISTE);
		wbuLieferantenauswahl.addActionListener(this);

		wbuAnsprechpartnerauswahl = new WrapperButton();
		wbuAnsprechpartnerauswahl.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerauswahl
				.setActionCommand(this.ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE);
		wbuAnsprechpartnerauswahl.addActionListener(this);

		wbuVertreterauswahl = new WrapperButton();
		wbuVertreterauswahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.vertreter"));
		wbuVertreterauswahl
				.setActionCommand(this.ACTION_SPECIAL_VERTRETER_FROM_LISTE);
		wbuVertreterauswahl.addActionListener(this);

		wbuKundeRechnungsadresse = new WrapperGotoButton(
				WrapperGotoButton.GOTO_KUNDE_AUSWAHL);
		wbuKundeRechnungsadresse.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.rechnungsadresse"));
		wbuKundeRechnungsadresse
				.setActionCommand(this.ACTION_SPECIAL_RECHNUNGSADRESSE_FROM_LISTE);
		wbuKundeRechnungsadresse.addActionListener(this);

		wtfAuftragnummer = new WrapperTextField();
		wtfAuftragnummer.setActivatable(false);

		wtfAuftragbezeichnung = new WrapperTextField();
		wtfAuftragbezeichnung.setActivatable(false);
		wtfAuftragbezeichnung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wlaAdresse = new WrapperLabel();
		wlaAdresse.setText(LPMain.getTextRespectUISPr("lp.adresse.kbez"));
		wtfKundeLieferadresse = new WrapperTextField();
		wtfKundeLieferadresse.setActivatable(false);
		wtfKundeLieferadresse.setMandatoryField(true);
		wtfKundeLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfKundeLieferadresseAdresse = new WrapperTextField();
		wtfKundeLieferadresseAdresse.setActivatable(false);
		wtfKundeLieferadresseAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaKundeLieferadresseAbteilung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.abteilung"));

		wtfKundeLieferadresseAbteilung = new WrapperTextField();
		wtfKundeLieferadresseAbteilung.setActivatable(false);
		wtfKundeLieferadresseAbteilung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

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

		wtfVertreter = new WrapperTextField();
		wtfVertreter.setActivatable(false);
		wtfVertreter.setMandatoryField(true);
		wtfVertreter.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfKundeRechnungsadresse = new WrapperTextField();
		wtfKundeRechnungsadresse.setActivatable(false);
		wtfKundeRechnungsadresse.setMandatoryField(true);
		wtfKundeRechnungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		labelProjekt = new WrapperLabel();
		labelProjekt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));

		labelKommission = new WrapperLabel();
		labelKommission.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommission"));

		labelBestellnummer = new WrapperLabel();
		labelBestellnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.bestellnummer"));

		wtfProjekt = new WrapperTextField(80);

		wtfBestellnummer = new WrapperTextField();
		wtfKommission = new WrapperTextField();

		wbuLager = new WrapperButton(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wbuLager.setMandatoryField(true);
		wbuLager.addActionListener(this);
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER);

		wtfLager = new WrapperTextField();
		wtfLager.setMandatoryField(true);
		wtfLager.setActivatable(false);

		wbuZielLager = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.ziellager"));

		wbuZielLager.addActionListener(this);
		wbuZielLager.setActionCommand(ACTION_SPECIAL_ZIELLAGER);

		wtfZielLager = new WrapperTextField();
		wtfZielLager.setActivatable(false);

		wbuKostenstelle = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.addActionListener(this);
		wbuKostenstelle.setActionCommand(this.ACTION_SPECIAL_KOSTENSTELLE);

		wtfKostenstelle = new WrapperTextField();
		wtfKostenstelle.setMandatoryField(true);
		wtfKostenstelle.setActivatable(false);
		wtfKostenstelleBezeichnung = new WrapperTextField();
		wtfKostenstelleBezeichnung.setActivatable(false);

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

		wbuRechnung = new WrapperGotoButton(LPMain.getInstance()
				.getTextRespectUISPr("rechnung.modulname"),
				WrapperGotoButton.GOTO_RECHNUNG_AUSWAHL);
		wbuRechnung.setActivatable(false);
		wtfRechnungcnr = new WrapperTextField();
		wtfRechnungcnr.setActivatable(false);
		
		// Workingpanel
		jPanelWorkingOn = new JPanel(new MigLayout("wrap 4, hidemode 3", "[fill, 20%][fill,30%][fill,20%][fill,30%]"));

		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		jPanelWorkingOn.add(labelLieferscheinart);
		jPanelWorkingOn.add(wcoLieferscheinart);
		jPanelWorkingOn.add(labelDatum);
		jPanelWorkingOn.add(wdfBelegdatum);

		// Zeile
		jPanelWorkingOn.add(wbuAuftragauswahl);
		jPanelWorkingOn.add(wtfAuftragnummer);
		jPanelWorkingOn.add(wtfAuftragbezeichnung, "span 2");

		jPanelWorkingOn.add(wbuKunde);
		jPanelWorkingOn.add(wbuLieferantenauswahl);
		jPanelWorkingOn.add(wtfKundeLieferadresse, "span");
		// Lieferschein an Lieferant

		jPanelWorkingOn.add(wlaAdresse);
		jPanelWorkingOn.add(wtfKundeLieferadresseAdresse, "span");

		jPanelWorkingOn.add(wlaKundeLieferadresseAbteilung);
		jPanelWorkingOn.add(wtfKundeLieferadresseAbteilung, "span");

		jPanelWorkingOn.add(wbuAnsprechpartnerauswahl);
		jPanelWorkingOn.add(wtfAnsprechpartner, "span");

		// Zeile
		jPanelWorkingOn.add(wbuVertreterauswahl);
		jPanelWorkingOn.add(wtfVertreter, "span");

		// Zeile
		jPanelWorkingOn.add(wbuKundeRechnungsadresse);
		jPanelWorkingOn.add(wtfKundeRechnungsadresse, "span");
		
		jPanelWorkingOn.add(wbuAnsprechpartnerRechungsadresse);
		jPanelWorkingOn.add(wtfAnsprechpartnerRechungsadresse, "span");
		
		// Zeile
		jPanelWorkingOn.add(labelProjekt);

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

			jPanelWorkingOn.add(wtfProjekt);
			jPanelWorkingOn.add(wsfProjekt.getWrapperGotoButton());
			jPanelWorkingOn.add(wsfProjekt.getWrapperTextField());

		} else {
			jPanelWorkingOn.add(wtfProjekt, "span");
		}

		// Zeile
		jPanelWorkingOn.add(labelBestellnummer);
		jPanelWorkingOn.add(wtfBestellnummer, "span");
		
		// Zeile
		jPanelWorkingOn.add(labelKommission);
		jPanelWorkingOn.add(wtfKommission, "span");

		// Zeile
		jPanelWorkingOn.add(wbuWaehrung);
		jPanelWorkingOn.add(wtfWaehrung);
		jPanelWorkingOn.add(wlaKurs);
		jPanelWorkingOn.add(wnfKurs);

		// Zeile
		jPanelWorkingOn.add(wbuLager);
		jPanelWorkingOn.add(wtfLager);
		jPanelWorkingOn.add(wbuZielLager);
		jPanelWorkingOn.add(wtfZielLager);

		// Zeile
		jPanelWorkingOn.add(wbuKostenstelle);
		jPanelWorkingOn.add(wtfKostenstelle);
		jPanelWorkingOn.add(wtfKostenstelleBezeichnung, "span");
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wbuRechnung);
		jPanelWorkingOn.add(wtfRechnungcnr);

		createAndSaveAndShowButton(
				"/com/lp/client/res/document_preferences.png",
				LPMain.getTextRespectUISPr("ls.zollexportpapiere.erhalten"),
				MY_OWN_NEW_TOGGLE_ZOLLEXPORTPAPIER_ERHALTEN, null);

	}

	private void initPanel() throws Throwable {
		wcoLieferscheinart.setMap(DelegateFactory.getInstance().getLsDelegate()
				.getLieferscheinart(LPMain.getInstance().getUISprLocale()));
	}

	/**
	 * Defaultwerte auf dem Panel setzen.
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		auftragDto = new AuftragDto();
		ansprechpartnerDto = new AnsprechpartnerDto();
		ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();
		vertreterDto = new PersonalDto();
		kundeRechnungsadresseDto = new KundeDto();
		waehrungDto = new WaehrungDto();
		lagerDto = new LagerDto();
		zielLagerDto = new LagerDto();
		kostenstelleDto = new KostenstelleDto();
		waehrungOriDto = new WaehrungDto();

		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		setRechnungVisible(false);

		// alle vorbelegten werte setzen
		Date currentDate = new Date(System.currentTimeMillis());

		auftragDto = new AuftragDto();

		wcoLieferscheinart.setKeyOfSelectedItem(LieferscheinFac.LSART_FREI);
		enableComponentsAuftragauswahl();
		// enableComponentsInAbhaengigkeitZuLieferscheinart();

		wdfBelegdatum.setDate(currentDate);

		// ad Lager im Lieferschein: Default Lager ist das Lager des aktuellen
		// Benutzers; ist das Lager des aktuellen Benutzers null, dann gilt das
		// Hauptlager des Mandanten
		lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.getHauptlagerDesMandanten();
		wtfLager.setText(lagerDto.getCNr());

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
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// source ist auftrag auswahldialog
			if (e.getSource() == panelQueryFLRAuftragauswahl) {
				Integer iIdAuftrag = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftrag);
				auftragDto2Components();
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
			} else
			// Lieferantauswahldialog;
			if (e.getSource() == panelQueryFLRLieferantenauswahl) {
				Integer iIdLieferant = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				Integer iIdKunde = DelegateFactory.getInstance()
						.getKundeDelegate()
						.createVerstecktenKundenAusLieferant(iIdLieferant);
				befuelleFelderKundenAuswahl(iIdKunde);
			} else
			// Source ist mein Kundeauswahldialog; wenn ein neuer Lieferkunde
			// gewaehlt wurde, werden die abhaengigen Felder angepasst
			if (e.getSource() == panelQueryFLRKundenauswahl) {
				Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(iIdKunde);
				// PJ 15739
				if (kundeDto.getPartnerDto().getLandplzortIId() == null) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lieferschein.error.kundeohnelkz"));
					return;
				}

				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(iIdKunde);

				befuelleFelderKundenAuswahl(iIdKunde);
			} else

			// Source Auswahldialog fuer den Ansprechpartner
			if (e.getSource() == panelQueryFLRAnsprechpartnerauswahl) {
				Integer iIdAnsprechparnter = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				bestimmeUndZeigeAnsprechpartner(iIdAnsprechparnter);
			} else

			// Source ist der Ansprechpartnerauswahldialog

			// Source ist der Ansprechpartnerauswahldialog
			if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();

				bestimmeUndZeigeAnsprechpartnerRechnungsadresse(iIdAnsprechpartner);
			} else

			// source ist vertreter auswahldialog
			if (e.getSource() == panelQueryFLRVertreterauswahl) {
				Integer iIdPersonal = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				vertreterDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(iIdPersonal);
				vertreterDto2Components();
			} else

			// Source ist der Kundenauswahldialog fuer die Rechnungsadresse
			if (e.getSource() == panelQueryFLRRechnungsadresseauswahl) {
				Integer pkKunde = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(pkKunde);

				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(pkKunde);

				// PJ 15739
				if (kundeDto.getPartnerDto().getLandplzortIId() == null) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lieferschein.error.kundeohnelkz"));
					return;
				}

				wtfAnsprechpartnerRechungsadresse.setText("");
				ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();

				kundeRechnungsadresseDto = kundeDto;
				kundeRechnungsadresseDto2Components();
			} else if (e.getSource() == panelQueryFLRLager) {
				Integer iIdLager = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				lagerDto = DelegateFactory.getInstance().getLagerDelegate()
						.lagerFindByPrimaryKey(iIdLager);
				wtfLager.setText(lagerDto.getCNr());
			} else if (e.getSource() == panelQueryFLRZielLager) {
				Integer iIdLager = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (!iIdLager.equals(lagerDto.getIId())) {
					zielLagerDto = DelegateFactory.getInstance()
							.getLagerDelegate().lagerFindByPrimaryKey(iIdLager);

					wtfZielLager.setText(zielLagerDto.getCNr());
				} else {
					DialogFactory.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr(
									"lp.warning"),
							LPMain.getInstance().getTextRespectUISPr(
									"ls.warning.lagergleichziellager"));
				}
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Integer iIdKostenstelle = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				kostenstelleDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.kostenstelleFindByPrimaryKey(iIdKostenstelle);
				wtfKostenstelle.setText(kostenstelleDto.getCBez());
				wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftragauswahl) {
				auftragDto = new AuftragDto();
				wtfAuftragnummer.setText("");
				wtfAuftragbezeichnung.setText("");
			} else if (e.getSource() == panelQueryFLRAnsprechpartnerauswahl) {
				ansprechpartnerDto = new AnsprechpartnerDto();
				wtfAnsprechpartner.setText("");
			} else if (e.getSource() == panelQueryFLRZielLager) {
				zielLagerDto = new LagerDto();
				wtfZielLager.setText("");
			} else if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
				ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();
				wtfAnsprechpartnerRechungsadresse.setText("");
			}
		}
	}

	/**
	 * Alle durch den Auftrag bestimmten Felder setzen.
	 * 
	 * @throws Throwable
	 */
	private void auftragDto2Components() throws Throwable {
		// der auftrag bei einem auftragtragbezogenen lieferschein kann nicht
		// geaendert werden
		// this.jButtonAuftragauswahl.setEnabled(false);
		// this.jTextFieldAuftragnummer.setEnabled(false);

		// Belegdatum nicht setzen, hat Default und koennte manuell veraendert
		// worden sein

		wtfAuftragnummer.setText(auftragDto.getCNr());
		wtfAuftragbezeichnung.setText(auftragDto.getCBezProjektbezeichnung());

		// eigenschaften des auftragskunden uebernehmen
		Integer iIdKundeLieferadresse = auftragDto.getKundeIIdLieferadresse();
		if (iIdKundeLieferadresse != null) {
			tpLieferschein.setKundeLieferadresseDto(DelegateFactory
					.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(iIdKundeLieferadresse));
			kundeLieferadresseDto2Components();
		}

		bestimmeUndZeigeAnsprechpartner(auftragDto.getAnsprechparnterIId());

		Integer iIdPersonal = auftragDto.getPersonalIIdVertreter();
		if (iIdPersonal != null) {
			vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(iIdPersonal);
			vertreterDto2Components();
		}

		Integer iIdKundeRechnungsadresse = auftragDto
				.getKundeIIdRechnungsadresse();
		if (iIdKundeRechnungsadresse != null) {
			kundeRechnungsadresseDto = DelegateFactory.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(iIdKundeRechnungsadresse);
			kundeRechnungsadresseDto2Components();
		}

		wtfProjekt.setText(auftragDto.getCBezProjektbezeichnung());
		wtfBestellnummer.setText(auftragDto.getCBestellnummer());

		wtfWaehrung.setText(auftragDto.getCAuftragswaehrung());
		wnfKurs.setBigDecimal(DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.getWechselkurs2(
						LPMain.getInstance().getTheClient()
								.getSMandantenwaehrung(),
						auftragDto.getCAuftragswaehrung()));

		kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
				.kostenstelleFindByPrimaryKey(auftragDto.getKostIId());
		wtfKostenstelle.setText(kostenstelleDto.getCBez());
		wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
	}

	/**
	 * Die Eigenschaften des Vertreters beim Kunden zur Anzeige bringen.
	 * 
	 * @throws Throwable
	 */
	private void vertreterDto2Components() throws Throwable {
		if (vertreterDto != null) {
			wtfVertreter.setText(vertreterDto.getPartnerDto().formatAnrede());
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		// dtos aus dem tabbed pane zuruecksetzen
		tpLieferschein.resetDtos();

		// dieses panel zuruecksetzen
		setDefaults();

		tpLieferschein.setTitleLieferschein("ls.title.panel.kopfdaten");
		clearStatusbar();
	}

	/**
	 * Einen Lieferschein abspeichern.
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

			DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.pruefeKreditlimit(
							tpLieferschein.getLieferscheinDto()
									.getKundeIIdRechnungsadresse());

			if (tpLieferschein.getLieferscheinDto().getIId() == null) {
				Integer iIdLieferschein = null;

				if (auftragDto.getIId() != null) {
					iIdLieferschein = DelegateFactory
							.getInstance()
							.getAuftragDelegate()
							.erzeugeLieferscheinAusAuftrag(auftragDto.getIId(),
									tpLieferschein.getLieferscheinDto());
				} else {
					tpLieferschein.getLieferscheinDto().setAuftragIId(null);
					iIdLieferschein = DelegateFactory
							.getInstance()
							.getLsDelegate()
							.createLieferschein(
									tpLieferschein.getLieferscheinDto());
				}

				tpLieferschein.setLieferscheinDto(DelegateFactory.getInstance()
						.getLsDelegate()
						.lieferscheinFindByPrimaryKey(iIdLieferschein));

			} else {
				boolean bUpdate = true;

				if (tpLieferschein.getLieferscheinDto()
						.getStatusCNr()
						.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
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
					LieferscheinDto lDtoVorhanden = DelegateFactory
							.getInstance()
							.getLsDelegate()
							.lieferscheinFindByPrimaryKey(
									tpLieferschein.getLieferscheinDto()
											.getIId());
					if (!lDtoVorhanden.getKundeIIdLieferadresse().equals(
							tpLieferschein.getLieferscheinDto()
									.getKundeIIdLieferadresse())) {

						DialogGeaenderteKonditionenVK dialog = new DialogGeaenderteKonditionenVK(
								tpLieferschein.getLieferscheinDto(),
								tpLieferschein.getLieferscheinDto()
										.getKundeIIdLieferadresse(),
								getInternalFrame());
						LPMain.getInstance().getDesktop()
								.platziereDialogInDerMitteDesFensters(dialog);

						if (dialog.bKonditionenUnterschiedlich == true) {
							dialog.setVisible(true);

							if (dialog.bAbgebrochen == false) {
								tpLieferschein
										.setLieferscheinDto((LieferscheinDto) dialog
												.getBelegVerkaufDto());
							} else {
								bUpdate = false;
							}
						}

					}

					if (bUpdate) {

						// die Auftragszuordnung koennte geaendert worden sein
						Integer[] aAuftragIId = new Integer[1];
						aAuftragIId[0] = auftragDto.getIId();

						boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = DelegateFactory
								.getInstance()
								.getLsDelegate()
								.updateLieferschein(
										tpLieferschein.getLieferscheinDto(),
										waehrungOriDto == null ? null
												: waehrungOriDto.getCNr(),
										aAuftragIId);
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
						// das Datum koennte geaendert worden sein
						if (!tmpDatum.equals(tpLieferschein
								.getLieferscheinDto().getTBelegdatum())) {
							// DelegateFactory.getInstance().getMandantDelegate().
							// mwstsatzFindByPrimaryKey()

						}
					}
				}
			}

			setKeyWhenDetailPanel(tpLieferschein.getLieferscheinDto().getIId());
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	/**
	 * Den Lieferschein drucken.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpLieferschein.printLieferschein();
		eventYouAreSelected(false);
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERSCHEINART_CHANGED)) {
			String selectedKey = (String) wcoLieferscheinart
					.getKeyOfSelectedItem();

			if (selectedKey.equalsIgnoreCase(LieferscheinFac.LSART_FREI)) {
				auftragDto = new AuftragDto();

				wtfAuftragnummer.setText("");
				wtfAuftragbezeichnung.setText("");
			}

			//
			if (selectedKey.equalsIgnoreCase(LieferscheinFac.LSART_LIEFERANT)) {
				wbuKunde.setVisible(false);
				wbuLieferantenauswahl.setVisible(true);
			} else {
				wbuKunde.setVisible(true);
				wbuLieferantenauswahl.setVisible(false);
			}

			enableComponentsAuftragauswahl();
			// enableComponentsInAbhaengigkeitZuLieferscheinart();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			dialogQueryAuftragFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE)) {
			dialogQueryAnsprechpartnerFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE)) {
			dialogQueryAnsprechpartnerRechnungsadresse(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_VERTRETER_FROM_LISTE)) {
			dialogQueryVertreterFromListe();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKundeFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LIEFERANT_FROM_LISTE)) {
			dialogQueryLieferantFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_RECHNUNGSADRESSE_FROM_LISTE)) {
			dialogQueryRechnungsadresseFromListe();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER)) {
			dialogQueryLager();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ZIELLAGER)) {
			dialogQueryZielLager();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_WAEHRUNG)) {
			dialogQueryWaehrung();
		} else if (e.getActionCommand().equals(
				MY_OWN_NEW_TOGGLE_ZOLLEXPORTPAPIER_ERHALTEN)) {
			// PJ 17696
			if (tpLieferschein.getLieferscheinDto() != null
					&& tpLieferschein.getLieferscheinDto().getIId() != null) {

				// SP1811

				boolean b = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("ls.exportbeleg.aendern.stufe1"));
				if (b == false) {
					return;
				}

				if (tpLieferschein.getLieferscheinDto().getStatusCNr()
						.equals(LieferscheinFac.LSSTATUS_VERRECHNET)
						|| tpLieferschein.getLieferscheinDto().getStatusCNr()
								.equals(LieferscheinFac.LSSTATUS_ERLEDIGT)) {
					b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("ls.exportbeleg.aendern.stufe2"));
					if (b == false) {
						return;
					}
				}

				if (tpLieferschein.getLieferscheinDto().getTZollexportpapier() == null) {

					DialogZollexportbeleg d = new DialogZollexportbeleg(
							tpLieferschein);
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				} else {
					DelegateFactory
							.getInstance()
							.getLsDelegate()
							.toggleZollexportpapiereErhalten(
									tpLieferschein.getLieferscheinDto()
											.getIId(), null, null);
				}

				eventYouAreSelected(false);
			}
		} else if(e.getActionCommand().equals(ACTION_SPECIAL_LIEFERAVISO)) {
			if(tpLieferschein.getLieferscheinDto().getTLieferaviso() == null) {
				dialogLieferavisoErzeugen() ;
			} else {
				boolean yes = DialogFactory.showModalJaNeinDialog(
						tpLieferschein.getInternalFrame(), 
						LPMain.getTextRespectUISPr("ls.bereitsavisiert")) ;
				if(yes) {
					DelegateFactory.getInstance().getLsDelegate()
						.resetLieferscheinAviso(tpLieferschein.getLieferscheinDto().getIId());
					dialogLieferavisoErzeugen() ;
				}
			}
		}
	}

	private void dialogLieferavisoErzeugen() {
		JTextArea msgLabel;
		JProgressBar progressBar;
		final int MAXIMUM = 100;
		JPanel panel;

		progressBar = new JProgressBar(0, MAXIMUM);
		progressBar.setIndeterminate(true);
		msgLabel = new JTextArea(LPMain.getTextRespectUISPr("lp.versandweg.durchfuehren"));
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
				String result = "" ;
				try {
					result = DelegateFactory
							.getInstance()
							.getLsDelegate()
							.createLieferscheinAvisoPost(
									tpLieferschein.getLieferscheinDto().getIId());
					publish(result);

					tpLieferschein.initializeDtos(tpLieferschein.getLieferscheinDto().getIId()) ;
					updateLieferAvisoButton() ;
				} catch (Throwable t) {
					handleException(t, false);
				}

				return result ;
			}
		};

		worker.execute();
	}
	
	private void dialogQueryWaehrung() throws Throwable {
		panelQueryFLRWaehrung = FinanzFilterFactory.getInstance()
				.createPanelFLRWaehrung(getInternalFrame(),
						waehrungDto.getCNr());
		new DialogQuery(panelQueryFLRWaehrung);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpLieferschein.pruefeAktuellenLieferschein()) {
			if (!tpLieferschein.getLieferscheinDto().getStatusCNr()
					.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
				tpLieferschein.showStatusMessage("lp.warning",
						"ls.warning.kannnichtstorniertwerden");
			} else {
				if (DialogFactory.showMeldung(
						LPMain.getTextRespectUISPr("ls.stornieren"),
						LPMain.getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
					DelegateFactory
							.getInstance()
							.getLsDelegate()
							.stornieren(
									tpLieferschein.getLieferscheinDto()
											.getIId());
					super.eventActionDelete(e, false, false); // der
					// lieferschein
					// existiert
					// weiterhin!
					eventYouAreSelected(false);
				}
			}
		}
	}

	private void dialogQueryLager() throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						(lagerDto != null) ? lagerDto.getIId() : null);
		new DialogQuery(panelQueryFLRLager);
	}

	private void dialogQueryZielLager() throws Throwable {
		panelQueryFLRZielLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						(zielLagerDto != null) ? zielLagerDto.getIId() : null,
						true, false);
		new DialogQuery(panelQueryFLRZielLager);
	}

	private void dialogQueryKostenstelle() throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	private void dialogQueryAuftragFromListe() throws Throwable {
		FilterKriterium[] fk = LieferscheinFilterFactory.getInstance()
				.createFKPanelQueryFLRAuftragAuswahl();

		panelQueryFLRAuftragauswahl = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(intFrame, true, true, fk,
						auftragDto.getIId());

		new DialogQuery(panelQueryFLRAuftragauswahl);
	}

	private void dialogQueryKundeFromListe() throws Throwable {
		panelQueryFLRKundenauswahl = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(intFrame, true, false,
						tpLieferschein.getKundeLieferadresseDto().getIId());

		new DialogQuery(panelQueryFLRKundenauswahl);
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

	private void dialogQueryLieferantFromListe() throws Throwable {
		Integer lieferantIId = null;
		// wenn schon ein Kunde gewaehlt wurde, dann den lieferanten dazu suchen
		if (tpLieferschein.getKundeLieferadresseDto().getIId() != null) {
			LieferantDto lieferantDto = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByiIdPartnercNrMandantOhneExc(
							tpLieferschein.getKundeLieferadresseDto()
									.getPartnerDto().getIId(),
							LPMain.getTheClient().getMandant());
			if (lieferantDto != null) {
				lieferantIId = lieferantDto.getIId();
			}
		}
		panelQueryFLRLieferantenauswahl = PartnerFilterFactory.getInstance()
				.createPanelFLRLieferantGoto(intFrame, lieferantIId, true,
						false);
		new DialogQuery(panelQueryFLRLieferantenauswahl);
	}

	private void dialogQueryRechnungsadresseFromListe() throws Throwable {

		if (kundeRechnungsadresseDto != null) {
			panelQueryFLRRechnungsadresseauswahl = PartnerFilterFactory
					.getInstance().createPanelFLRKunde(intFrame, true, false,
							kundeRechnungsadresseDto.getIId());
			new DialogQuery(panelQueryFLRRechnungsadresseauswahl);
		} else {
			panelQueryFLRRechnungsadresseauswahl = PartnerFilterFactory
					.getInstance().createPanelFLRKunde(intFrame, true, false,
							null);
			new DialogQuery(panelQueryFLRRechnungsadresseauswahl);
		}

	}

	private void dialogQueryAnsprechpartnerFromListe() throws Throwable {
		if (tpLieferschein.getKundeLieferadresseDto().getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {
			panelQueryFLRAnsprechpartnerauswahl = PartnerFilterFactory
					.getInstance().createPanelFLRAnsprechpartner(
							intFrame,
							tpLieferschein.getKundeLieferadresseDto()
									.getPartnerDto().getIId(),
							ansprechpartnerDto.getIId(), true, true);
			new DialogQuery(panelQueryFLRAnsprechpartnerauswahl);
		}
	}

	private void dialogQueryVertreterFromListe() throws Throwable {
		panelQueryFLRVertreterauswahl = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(intFrame, true, false,
						vertreterDto.getIId());
		new DialogQuery(panelQueryFLRVertreterauswahl);
	}

	private void dto2Components() throws Throwable {
		// nur bei auftragbezogenem Lieferschein
		if (tpLieferschein.getLieferscheinDto().getLieferscheinartCNr()
				.equalsIgnoreCase(LieferscheinFac.LSART_AUFTRAG)) {
			Integer pkAuftrag = tpLieferschein.getLieferscheinDto()
					.getAuftragIId();
			auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(pkAuftrag);
			auftragDto2Components();
		}

		Integer kundeLieferadresseIId = tpLieferschein.getLieferscheinDto()
				.getKundeIIdLieferadresse();
		KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(kundeLieferadresseIId);
		tpLieferschein.setKundeLieferadresseDto(kundeDto);

		if (tpLieferschein.getLieferscheinDto().getZiellagerIId() != null) {
			zielLagerDto = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(
							tpLieferschein.getLieferscheinDto()
									.getZiellagerIId());
			wtfZielLager.setText(zielLagerDto.getCNr());
		}

		kundeLieferadresseDto2Components();

		Integer iIdAnsprechpartner = tpLieferschein.getLieferscheinDto()
				.getAnsprechpartnerIId();
		bestimmeUndZeigeAnsprechpartner(iIdAnsprechpartner);

		Integer iIdPersonal = tpLieferschein.getLieferscheinDto()
				.getPersonalIIdVertreter();
		if (iIdPersonal != null) {
			vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(iIdPersonal);
			vertreterDto2Components();
		}

		Integer pkKundeRechnung = tpLieferschein.getLieferscheinDto()
				.getKundeIIdRechnungsadresse();
		kundeRechnungsadresseDto = DelegateFactory.getInstance()
				.getKundeDelegate().kundeFindByPrimaryKey(pkKundeRechnung);
		kundeRechnungsadresseDto2Components();

		wsfProjekt.setKey(tpLieferschein.getLieferscheinDto().getProjektIId());

		Integer iIdAnsprechpartnerRechnungsadresse = tpLieferschein
				.getLieferscheinDto().getAnsprechpartnerIIdRechnungsadresse();
		bestimmeUndZeigeAnsprechpartnerRechnungsadresse(iIdAnsprechpartnerRechnungsadresse);

		// bestehende Werte im Lieferschein ueberschreiben die Defaults aus dem
		// Kunden
		lieferscheinDto2Components();
	}

	/**
	 * Die Daten eines bestehenden Lieferscheins zur Anzeige bringen.
	 * 
	 * @throws Throwable
	 */
	private void lieferscheinDto2Components() throws Throwable {
		wcoLieferscheinart.setKeyOfSelectedItem(tpLieferschein
				.getLieferscheinDto().getLieferscheinartCNr());
		wdfBelegdatum.setDate(tpLieferschein.getLieferscheinDto()
				.getTBelegdatum());
		// wird die belegatum veraendert schaue nach ob die mwst der positionen
		// veraendert werden muessen
		tmpDatum = new Date(tpLieferschein.getLieferscheinDto()
				.getTBelegdatum().getTime());
		wtfProjekt.setText(tpLieferschein.getLieferscheinDto()
				.getCBezProjektbezeichnung());
		wtfKommission.setText(tpLieferschein.getLieferscheinDto()
				.getCKommission());
		wtfBestellnummer.setText(tpLieferschein.getLieferscheinDto()
				.getCBestellnummer());

		// Waehrung und Wechselkurs setzen
		wtfWaehrung.setText(tpLieferschein.getLieferscheinDto()
				.getWaehrungCNr());
		wnfKurs.setDouble(tpLieferschein.getLieferscheinDto()
				.getFWechselkursmandantwaehrungzubelegwaehrung());

		lagerDto = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByPrimaryKey(
						tpLieferschein.getLieferscheinDto().getLagerIId());
		wtfLager.setText(lagerDto.getCNr());

		kostenstelleDto = DelegateFactory
				.getInstance()
				.getSystemDelegate()
				.kostenstelleFindByPrimaryKey(
						tpLieferschein.getLieferscheinDto()
								.getKostenstelleIId());
		wtfKostenstelle.setText(kostenstelleDto.getCBez());
		wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());

		// wenn der Lieferschein verrechnet wurde Rechnungsinfos anzeigen
		if (tpLieferschein.getLieferscheinDto().getRechnungIId() != null) {
			setRechnungVisible(true);
			RechnungDto oRechnungDto = DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.rechnungFindByPrimaryKey(
							tpLieferschein.getLieferscheinDto()
									.getRechnungIId());
			wtfRechnungcnr.setText(oRechnungDto.getCNr());
			wbuRechnung.setOKey(oRechnungDto.getIId());
		} else {
			wbuRechnung.setOKey(null);
		}
	
		updateLieferAvisoButton() ;
	}

	private void updateLieferAvisoButton() throws ExceptionLP {
		if(bZusatzfunktionVersandweg) {
			LieferscheinDto lsDto = tpLieferschein.getLieferscheinDto() ;
			boolean hasLieferaviso = lsDto != null && lsDto.getTLieferaviso() != null ;
			JButton button = getHmOfButtons().get(ACTION_SPECIAL_LIEFERAVISO).getButton();
			button.setIcon(hasLieferaviso ? AVISO_ICON_DONE : AVISO_ICON);
			
			boolean enable = 
					lsDto != null  && lsDto.getIId() != null &&
					lsDto.getTLieferaviso() == null &&
					LocaleFac.STATUS_GELIEFERT.equals(lsDto.getStatusCNr()) &&
					DelegateFactory.getInstance().getLsDelegate().hatLieferscheinVersandweg(lsDto) ;
			if(hasLieferaviso) {
				enable = true ; 
			}
			enableToolsPanelButtons(enable, ACTION_SPECIAL_LIEFERAVISO);
		}		
	}
	
	/**
	 * Die Eigenschaften des Kunden der Lieferadresse zur Anzeige bringen.
	 * 
	 * @throws Throwable
	 */

	/**
	 * Die Eigenschaften des Kunden der Lieferadresse zur Anzeige bringen.
	 * 
	 * @throws Throwable
	 */
	private void kundeLieferadresseDto2Components() throws Throwable {
		wbuKunde.setOKey(tpLieferschein.getKundeLieferadresseDto().getIId());
		wtfKundeLieferadresse.setText(tpLieferschein.getKundeLieferadresseDto()
				.getPartnerDto().formatTitelAnrede());
		String sAdresse = tpLieferschein.getKundeLieferadresseDto()
				.getPartnerDto().formatAdresse();
		if (tpLieferschein.getKundeLieferadresseDto().getPartnerDto()
				.getCKbez() != null)
			;
		sAdresse = sAdresse
				+ "  /  "
				+ tpLieferschein.getKundeLieferadresseDto().getPartnerDto()
						.getCKbez();
		wtfKundeLieferadresseAdresse.setText(sAdresse);
		wtfKundeLieferadresseAbteilung.setText(tpLieferschein
				.getKundeLieferadresseDto().getPartnerDto()
				.getCName3vorname2abteilung());

		// default waehrung des auftrags kommt aus dem kunden
		waehrungDto = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.waehrungFindByPrimaryKey(
						tpLieferschein.getKundeLieferadresseDto()
								.getCWaehrung());
		wtfWaehrung.setText(waehrungDto.getCNr());
		setzeWechselkurs();

		//
		if (tpLieferschein.getLieferscheinDto().getIId() == null
				|| (tpLieferschein.getLieferscheinDto().getIId() != null && DelegateFactory
						.getInstance()
						.getLieferscheinpositionDelegate()
						.berechneAnzahlMengenbehaftetePositionen(
								tpLieferschein.getLieferscheinDto().getIId()) == 0)) {

			if (tpLieferschein.getKundeLieferadresseDto()
					.getLagerIIdAbbuchungslager() != null) {
				lagerDto = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey(
								tpLieferschein.getKundeLieferadresseDto()
										.getLagerIIdAbbuchungslager());
				wtfLager.setText(lagerDto.getCNr());
			}
		}

		Integer iIdKostenstelle = tpLieferschein.getKundeLieferadresseDto()
				.getKostenstelleIId();

		if (iIdKostenstelle != null) {
			kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
					.kostenstelleFindByPrimaryKey(iIdKostenstelle);
			wtfKostenstelle.setText(kostenstelleDto.getCBez());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		}

		// @wh in diesem Moment muessen sich alle sprachabh Felder des
		// Lieferscheins aendern?
	}

	/**
	 * Die Eigenschaften des Kunden der Rechnungsadresse zur Anzeige bringen.
	 * 
	 * @throws Throwable
	 */
	private void kundeRechnungsadresseDto2Components() throws Throwable {
		if (kundeRechnungsadresseDto != null) {
			String sText = kundeRechnungsadresseDto.getPartnerDto()
					.formatTitelAnrede();
			if (kundeRechnungsadresseDto.getPartnerDto().formatAdresse() != null
					&& !kundeRechnungsadresseDto.getPartnerDto()
							.formatAdresse().equals("")) {
				sText = sText
						+ ", "
						+ kundeRechnungsadresseDto.getPartnerDto()
								.formatAdresse();
			}
			if (kundeRechnungsadresseDto.getPartnerDto().getCKbez() != null)
				sText = sText + "  /  "
						+ kundeRechnungsadresseDto.getPartnerDto().getCKbez();
			wtfKundeRechnungsadresse.setText(sText);
			wbuKundeRechnungsadresse.setOKey(kundeRechnungsadresseDto.getIId());
		}
	}

	/**
	 * Befuelle Eigenschaften des Kunden
	 * 
	 * @throws Throwable
	 * @param iIdKunde
	 *            Integer
	 */
	private void befuelleFelderKundenAuswahl(Integer iIdKunde) throws Throwable {

		if (iIdKunde != null) {
			// die urspruengliche Waehrung hinterlegen, wenn es eine gibt
			if (tpLieferschein.getLieferscheinDto().getWaehrungCNr() != null) {
				waehrungOriDto = DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.waehrungFindByPrimaryKey(
								tpLieferschein.getLieferscheinDto()
										.getWaehrungCNr());
			}

			tpLieferschein.setKundeLieferadresseDto(DelegateFactory
					.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(iIdKunde));

			if (tpLieferschein.getKundeLieferadresseDto().getPartnerDto()
					.getLagerIIdZiellager() != null) {
				if (tpLieferschein.getKundeLieferadresseDto().getPartnerDto()
						.getLagerIIdZiellager().equals(lagerDto.getIId())) {
					DialogFactory.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr(
									"lp.warning"),
							LPMain.getInstance().getTextRespectUISPr(
									"ls.warning.lagergleichziellager"));
				} else {
					zielLagerDto = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.lagerFindByPrimaryKey(
									tpLieferschein.getKundeLieferadresseDto()
											.getPartnerDto()
											.getLagerIIdZiellager());
					wtfZielLager.setText(zielLagerDto.getCNr());
				}
			}

			kundeLieferadresseDto2Components();

			// den Benutzer fragen, ob er die urspruengliche Waehrung
			// beibehalten moechte
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
								"lp.waehrungunterschiedlich"), LPMain
								.getInstance().getTextRespectUISPr("lp.frage"),
						aOptionen, aOptionen[0]);

				if (iAuswahl == indexWaehrungOriCNr) {
					// die Belegwaehrung wird beibehalten -> Waehrung und
					// Wechselkurs zuruecksetzen
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
							ParameterFac.PARAMETER_LIEFERSCHEIN_ANSP_VORBESETZEN,
							ParameterFac.KATEGORIE_LIEFERSCHEIN,
							LPMain.getInstance().getTheClient().getMandant());
			if ((Boolean) parameter.getCWertAsObject()) {
				anspDtoTemp = DelegateFactory
						.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(
								tpLieferschein.getKundeLieferadresseDto()
										.getPartnerIId());
			}
			if (anspDtoTemp != null) {
				ansprechpartnerDto = anspDtoTemp;

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
						.formatTitelAnrede());
			} else {
				wtfAnsprechpartner.setText("");
			}

			// es kann einen default vertreter beim kunden geben
			Integer iIdPersonal = tpLieferschein.getKundeLieferadresseDto()
					.getPersonaliIdProvisionsempfaenger();
			if (iIdPersonal != null) {
				vertreterDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(iIdPersonal);
				vertreterDto2Components();
			} else {
				vertreterDto = new PersonalDto();
				wtfVertreter.setText("");
			}

			boolean bRechnungAdresseAendern = true;
			LockStateValue lv = this.getLockedstateDetailMainKey();
			if (lv.getIState() == LOCK_IS_LOCKED_BY_ME) {
				bRechnungAdresseAendern = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr(
								"part.tooltip.rechnungsadr_from_partner"),
						LPMain.getInstance().getTextRespectUISPr("lp.frage"));
			}
			if (bRechnungAdresseAendern) {
				if (tpLieferschein.getKundeLieferadresseDto()
						.getPartnerRechnungsadresseDto() != null
						&& tpLieferschein.getKundeLieferadresseDto()
								.getPartnerRechnungsadresseDto().getIId() != null) {
					String sText = tpLieferschein.getKundeLieferadresseDto()
							.getPartnerRechnungsadresseDto()
							.formatTitelAnrede();
					if (tpLieferschein.getKundeLieferadresseDto()
							.getPartnerRechnungsadresseDto().formatAdresse() != null
							&& !tpLieferschein.getKundeLieferadresseDto()
									.getPartnerRechnungsadresseDto()
									.formatAdresse().equals("")) {
						sText = sText
								+ ", "
								+ tpLieferschein.getKundeLieferadresseDto()
										.getPartnerRechnungsadresseDto()
										.formatAdresse();

					}

					kundeRechnungsadresseDto = DelegateFactory
							.getInstance()
							.getKundeDelegate()
							.kundeFindByiIdPartnercNrMandantOhneExc(
									tpLieferschein.getKundeLieferadresseDto()
											.getPartnerRechnungsadresseDto()
											.getIId(),
									LPMain.getInstance().getTheClient()
											.getMandant());

					if (kundeRechnungsadresseDto == null) {
						wtfKundeRechnungsadresse.setText(null);

						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr("lp.error"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"ls.warning.rechnungsadressekeinkunde"));
						return;

					} else {
						wtfKundeRechnungsadresse.setText(sText);
						DelegateFactory.getInstance().getKundeDelegate()
								.pruefeKunde(kundeRechnungsadresseDto.getIId());
					}

				} else {
					kundeRechnungsadresseDto = tpLieferschein
							.getKundeLieferadresseDto();
					kundeRechnungsadresseDto2Components();
				}

			} else {

			}

		}
	}

	/**
	 * Alle Lieferscheindaten aus dem Panel sammeln.
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {

		if (tpLieferschein.getLieferscheinDto().getIId() == null) {
			tpLieferschein.getLieferscheinDto().setMandantCNr(
					LPMain.getTheClient().getMandant());
			tpLieferschein.getLieferscheinDto().setStatusCNr(
					LieferscheinFac.LSSTATUS_ANGELEGT);

			// PJ 14751

			if (kundeRechnungsadresseDto != null) {
				// Konditionen mit den Eigenschaften des Lieferscheinkunden
				// vorbelegen
				if (kundeRechnungsadresseDto.getFRabattsatz() != null) {
					tpLieferschein.getLieferscheinDto()
							.setFAllgemeinerRabattsatz(
									new Double(kundeRechnungsadresseDto
											.getFRabattsatz().doubleValue()));
				}
				tpLieferschein.getLieferscheinDto().setZahlungszielIId(
						kundeRechnungsadresseDto.getZahlungszielIId());
			} else {
				// Konditionen mit den Eigenschaften des Lieferscheinkunden
				// vorbelegen
				if (tpLieferschein.getKundeLieferadresseDto().getFRabattsatz() != null) {
					tpLieferschein.getLieferscheinDto()
							.setFAllgemeinerRabattsatz(
									new Double(tpLieferschein
											.getKundeLieferadresseDto()
											.getFRabattsatz().doubleValue()));
				}
				tpLieferschein.getLieferscheinDto().setZahlungszielIId(
						tpLieferschein.getKundeLieferadresseDto()
								.getZahlungszielIId());
			}

			tpLieferschein.getLieferscheinDto()
					.setLieferartIId(
							tpLieferschein.getKundeLieferadresseDto()
									.getLieferartIId());
			tpLieferschein.getLieferscheinDto()
					.setSpediteurIId(
							tpLieferschein.getKundeLieferadresseDto()
									.getSpediteurIId());

			// sonstige Konditionen vorbelegen
			tpLieferschein.getLieferscheinDto().setBVerrechenbar(
					Helper.boolean2Short(true));

			// Kopf- und Fusstext werden nicht vorbelegt
		}
		// MB: Der Auftrag kann auch nachtraeglich geaendert werden.

		tpLieferschein.getLieferscheinDto().setProjektIId(wsfProjekt.getIKey());

		tpLieferschein.getLieferscheinDto().setAuftragIId(auftragDto.getIId());

		tpLieferschein.getLieferscheinDto().setLieferscheinartCNr(
				(String) wcoLieferscheinart.getKeyOfSelectedItem());
		tpLieferschein.getLieferscheinDto().setTBelegdatum(
				wdfBelegdatum.getTimestamp());
		tpLieferschein.getLieferscheinDto().setKundeIIdLieferadresse(
				tpLieferschein.getKundeLieferadresseDto().getIId());
		tpLieferschein.getLieferscheinDto().setAnsprechpartnerIId(
				ansprechpartnerDto.getIId());

		tpLieferschein.getLieferscheinDto()
				.setAnsprechpartnerIIdRechnungsadresse(
						ansprechpartnerDtoRechnungsadresse.getIId());

		tpLieferschein.getLieferscheinDto().setPersonalIIdVertreter(
				vertreterDto.getIId());
		if (kundeRechnungsadresseDto != null) {
			tpLieferschein.getLieferscheinDto().setKundeIIdRechnungsadresse(
					kundeRechnungsadresseDto.getIId());
		}
		tpLieferschein.getLieferscheinDto().setCBezProjektbezeichnung(
				this.wtfProjekt.getText());
		tpLieferschein.getLieferscheinDto().setCBestellnummer(
				this.wtfBestellnummer.getText());

		tpLieferschein.getLieferscheinDto().setWaehrungCNr(
				wtfWaehrung.getText());
		tpLieferschein.getLieferscheinDto()
				.setFWechselkursmandantwaehrungzubelegwaehrung(
						this.wnfKurs.getDouble());

		tpLieferschein.getLieferscheinDto().setLagerIId(lagerDto.getIId());
		if (zielLagerDto != null) {
			tpLieferschein.getLieferscheinDto().setZiellagerIId(
					zielLagerDto.getIId());
		}
		tpLieferschein.getLieferscheinDto().setKostenstelleIId(
				kostenstelleDto.getIId());

		tpLieferschein.getLieferscheinDto().setCKommission(
				wtfKommission.getText());
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpLieferschein.aktualisiereLieferscheinstatusDurchButtonUpdate()) {

			super.eventActionUpdate(aE, false); // Buttons schalten

			enableComponentsAuftragauswahl();
			// enableComponentsInAbhaengigkeitZuLieferscheinart();
			enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen();
		}
	}

	/**
	 * Eine andere Waehrung wurde ausgewaehlt.
	 * 
	 * @param evt
	 *            Ereignis
	 * @throws java.lang.Throwable
	 *             Ausnhame
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == wtfWaehrung
				&& evt.getPropertyName().equals("value")) {
			try {
				setzeWechselkurs();
			} catch (Throwable t) {
				// @todo PJ 4999
			}
		}
	}

	/**
	 * Wenn dieses Panel ueber die Laschen gewaehlt wurde ...
	 * 
	 * @param bNeedNoYouAreSelectedI
	 *            fuer ableitende Panels
	 * @throws Throwable
	 */
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false); // Status der Buttons aktualisieren

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpLieferschein.getLieferscheinDto().getIId();

		// Anzeige komplett zuruecksetzen
		setDefaults();

		// PJ18167
		LPButtonAction lpbaZoll = getHmOfButtons().get(
				MY_OWN_NEW_TOGGLE_ZOLLEXPORTPAPIER_ERHALTEN);
		lpbaZoll.getButton().setVisible(false);
		lpbaZoll.getButton().setToolTipText(
				LPMain.getInstance().getTextRespectUISPr(
						"ls.zollexportpapiere.erhalten"));

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			tpLieferschein.setLieferscheinDto(DelegateFactory.getInstance()
					.getLsDelegate()
					.lieferscheinFindByPrimaryKey((Integer) oKey));
			tpLieferschein.setKundeLieferadresseDto(DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							tpLieferschein.getLieferscheinDto()
									.getKundeIIdLieferadresse()));

			dto2Components();

			tpLieferschein.setTitleLieferschein(LPMain.getInstance()
					.getTextRespectUISPr("ls.title.panel.kopfdaten"));

			aktualisiereStatusbar();

			boolean bButtonAnzeigen = false;

			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							tpLieferschein.getLieferscheinDto()
									.getKundeIIdLieferadresse());
			if (tpLieferschein.getLieferscheinDto().getLieferscheinartCNr()
					.equals(LieferscheinFac.LSART_LIEFERANT)) {

				LieferantDto lfDto = DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByiIdPartnercNrMandantOhneExc(
								kundeDto.getPartnerIId(),
								tpLieferschein.getLieferscheinDto()
										.getMandantCNr());
				if (lfDto != null
						&& Helper.short2boolean(lfDto.getBZollimportpapier())) {
					bButtonAnzeigen = true;
				}
			} else {
				if (Helper.short2boolean(kundeDto.getBZollpapier())) {
					bButtonAnzeigen = true;
				}
			}
			if (bButtonAnzeigen == true) {
				lpbaZoll.getButton().setVisible(true);

				String text2 = "";

				if (tpLieferschein.getLieferscheinDto().getTZollexportpapier() != null) {
					text2 = LPMain
							.getTextRespectUISPr("ls.zollexportpapiere.erhalten.persondatum")
							+ " "
							+ Helper.formatDatumZeit(tpLieferschein
									.getLieferscheinDto()
									.getTZollexportpapier(), LPMain
									.getTheClient().getLocUi());
				}
				if (tpLieferschein.getLieferscheinDto()
						.getPersonalIIdZollexportpapier() != null) {
					text2 += "("
							+ DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.personalFindByPrimaryKey(
											tpLieferschein
													.getLieferscheinDto()
													.getPersonalIIdZollexportpapier())
									.getCKurzzeichen() + ") ";
				}

				if (tpLieferschein.getLieferscheinDto().getCZollexportpapier() != null) {
					text2 += LPMain.getTextRespectUISPr("lp.zollbelegnummer")
							+ " "
							+ tpLieferschein.getLieferscheinDto()
									.getCZollexportpapier();
				}

				if (tpLieferschein.getLieferscheinDto()
						.getEingangsrechnungIdZollexport() != null) {
					EingangsrechnungDto erDtoZollImport = DelegateFactory
							.getInstance()
							.getEingangsrechnungDelegate()
							.eingangsrechnungFindByPrimaryKey(
									tpLieferschein.getLieferscheinDto()
											.getEingangsrechnungIdZollexport());
					text2 += " | "
							+ LPMain.getTextRespectUISPr("er.modulname.kurz")
							+ " " + erDtoZollImport.getCNr();
				}

				String text = "<html>"
						+ LPMain.getInstance().getTextRespectUISPr(
								"ls.zollexportpapiere.erhalten") + "<br>"
						+ text2 + "</html>";

				lpbaZoll.getButton().setToolTipText(text);

			}

		}

		enableComponentsAuftragauswahl();
		// enableComponentsInAbhaengigkeitZuLieferscheinart();
		enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen();
		tpLieferschein.enablePanels(tpLieferschein.getLieferscheinDto(), true);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}

	/**
	 * Die Anrede fuer einen Ansprechpartner bauen. <br>
	 * Es muss keinen Ansprechpartner geben.
	 * 
	 * @param iIdAnsprechpartnerI
	 *            pk des Ansprechpartners
	 * @throws Throwable
	 */
	private void bestimmeUndZeigeAnsprechpartnerRechnungsadresse(
			Integer iIdAnsprechpartnerI) throws Throwable {
		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerDtoRechnungsadresse = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);
			wtfAnsprechpartnerRechungsadresse
					.setText(ansprechpartnerDtoRechnungsadresse.getPartnerDto()
							.formatAnrede());
		} else {
			wtfAnsprechpartnerRechungsadresse.setText("");
		}
	}

	private void bestimmeUndZeigeAnsprechpartner(Integer iIdAnsprechpartnerI)
			throws Throwable {
		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerDto = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatAnrede());
		} else {
			wtfAnsprechpartner.setText("");
		}
	}

	/**
	 * Eine Auftragzuordnung kann nur unter bestimmten Bedingungen gemacht
	 * werden.
	 * 
	 * @throws Throwable
	 */
	private void enableComponentsAuftragauswahl() throws Throwable {
		boolean bEnableComponents = false;

		// Schritt 1: Der Lieferschein muss durch den aktuellen Benutzer gelockt
		// oder neu sein
		if (tpLieferschein.getLieferscheinDto().getIId() != null && isLocked()) {
			// Schritt 1a: Es handelt sich um einen auftragbezogenen
			// Lieferschein
			if (((String) wcoLieferscheinart.getKeyOfSelectedItem())
					.equals(LieferscheinFac.LSART_AUFTRAG)) {
				// Schritt 1b: Der Lieferschein befindet sich im Status Angelegt
				// oder Geliefert
				if (tpLieferschein.getLieferscheinDto()
						.getStatusCNr()
						.equals(LieferscheinFac.LSSTATUS_ANGELEGT)
						|| tpLieferschein.getLieferscheinDto()
								.getStatusCNr()
								.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
					// Schritt 1c: Der Lieferschein hat keine mengenbehafteten
					// Positionen
					if (DelegateFactory
							.getInstance()
							.getLieferscheinpositionDelegate()
							.berechneAnzahlMengenbehaftetePositionen(
									tpLieferschein.getLieferscheinDto()
											.getIId()) == 0) {
						bEnableComponents = true;
					}
				}
			}
		} else {
			// Schritt 2: Es handelt sich um einen neuen Lieferschein
			if (tpLieferschein.getLieferscheinDto().getIId() == null
					&& ((String) wcoLieferscheinart.getKeyOfSelectedItem())
							.equals(LieferscheinFac.LSART_AUFTRAG)) {
				bEnableComponents = true;
			}
		}

		wbuAuftragauswahl.setEnabled(bEnableComponents);
		wtfAuftragnummer.setMandatoryField(bEnableComponents);
	}

	/**
	 * Einige Eingaben koennen nur bei einer bestimmten Lieferscheinart gemacht
	 * werden. <br>
	 * Betrifft:
	 * <ul>
	 * <li>Kundenauswahl
	 * <li>Ansprechpartnerauswahl
	 * <li>Vertreterauswahl
	 * <li>Rechnungsadresseauswahl
	 * <li>Projekt- und Bestellnummerneingabe
	 * <li>Waehrung
	 * <li>Kostenstelle
	 * </ul>
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */

	// SK: Wird nicht benoetigt da laut wh diese Felder alle immer aktiviert
	// sein sollen
	// private void enableComponentsInAbhaengigkeitZuLieferscheinart()
	// throws Throwable {
	// boolean bEnableComponents = false;
	//
	// // Schritt 1: Der Lieferschein muss durch den aktuellen Benutzer gelockt
	// // oder neu sein
	// if (isLocked() || tpLieferschein.getLieferscheinDto().getIId() == null) {
	// // Schritt 2: Es muss sich um einen freien Lieferschein handeln
	// if (((String) wcoLieferscheinart.getKeyOfSelectedItem())
	// .equals(LieferscheinFac.LSART_FREI)) {
	// bEnableComponents = true;
	// } else if (((String) wcoLieferscheinart.getKeyOfSelectedItem())
	// .equals(LieferscheinFac.LSART_LIEFERANT)) {
	// bEnableComponents = true;
	// }
	//
	// }
	//
	// // wbuKundenauswahl.setEnabled(bEnableComponents);
	// // wbuAnsprechpartnerauswahl.setEnabled(bEnableComponents);
	// // wbuVertreterauswahl.setEnabled(bEnableComponents);
	// // wbuKundeRechnungsadresse.setEnabled(bEnableComponents);
	// //SK(14002): Projekt und Bestellnummer sollen ebenfalls immer editierbar
	// sein
	// // wtfProjekt.setEditable(bEnableComponents);
	// // wtfBestellnummer.setEditable(bEnableComponents);
	// // wbuWaehrung.setEnabled(bEnableComponents);
	// wbuKostenstelle.setEnabled(bEnableComponents);
	// }
	/**
	 * Einige Eingaben koennen nur unter bestimmten Voraussetzungen gemacht
	 * werden. <br>
	 * Betrifft:
	 * <ul>
	 * <li>Lieferscheinart
	 * <li>Waehrung
	 * <li>Lager
	 * </ul>
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private void enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen()
			throws Throwable {
		boolean bEnableComponents = false;

		// Schritt 1: Ein bestehender Lieferschein muss durch den aktuellen
		// Benutzer gelockt sein
		if (tpLieferschein.getLieferscheinDto().getIId() != null && isLocked()) {
			// Schritt 1a: Der Lieferschein befindet sich im Status Angelegt
			// oder Geliefert
			if (tpLieferschein.getLieferscheinDto().getStatusCNr()
					.equals(LieferscheinFac.LSSTATUS_ANGELEGT)
					|| tpLieferschein.getLieferscheinDto()
							.getStatusCNr()
							.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
				// Schritt 1b: Der Lieferschein hat keine mengenbehafteten
				// Positionen
				if (DelegateFactory
						.getInstance()
						.getLieferscheinpositionDelegate()
						.berechneAnzahlMengenbehaftetePositionen(
								tpLieferschein.getLieferscheinDto().getIId()) == 0) {
					bEnableComponents = true;
				}
			}
		}
		// Schritt 2: Es handelt sich um einen neuen Lieferschein
		else if (tpLieferschein.getLieferscheinDto().getIId() == null) {
			bEnableComponents = true;
		}

		wcoLieferscheinart.setEnabled(bEnableComponents);
		// wbuKundenauswahl.setEnabled(bEnableComponents);
		wbuWaehrung.setEnabled(bEnableComponents);
		wbuLager.setEnabled(bEnableComponents);
		wbuZielLager.setEnabled(bEnableComponents);

		// PJ18129
		if (tpLieferschein.getLieferscheinDto().getAuftragIId() != null) {
			AuftragDto auftragDto = DelegateFactory
					.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey(
							tpLieferschein.getLieferscheinDto().getAuftragIId());
			if (auftragDto.getProjektIId() != null) {
				wsfProjekt.setEnabled(false);
				wsfProjekt.getWrapperGotoButton().setEnabled(false);
			}

		}

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
				tpLieferschein.gotoAuswahl();
			}
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpLieferschein.getLieferscheinDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpLieferschein.getLieferscheinDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpLieferschein.getLieferscheinDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpLieferschein.getLieferscheinDto().getTAendern());
		setStatusbarStatusCNr(tpLieferschein.getLieferscheinStatus());
		if(tpLieferschein.getLieferscheinDto().getTLieferaviso() != null) {
			getPanelStatusbar().addToSpalteStatus(", Aviso: " + tpLieferschein.getLieferscheinDto().getTLieferaviso().toString());

		}
		String status = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_LIEFERSCHEIN,
						tpLieferschein.getLieferscheinDto().getIId());
		if (status != null) {
			status = LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status;
		}
		setStatusbarSpalte5(status);
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

	private void setRechnungVisible(boolean bVisibleI) {
		wbuRechnung.setVisible(bVisibleI);
		wtfRechnungcnr.setVisible(bVisibleI);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuKunde;
	}

}
