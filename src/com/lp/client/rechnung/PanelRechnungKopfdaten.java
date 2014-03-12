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
package com.lp.client.rechnung;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
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
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.GutschriftsgrundDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/*
 * <p>Panel zum Bearbeiten der Kopfdaten einer Rechnung</p> <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum <I>20. 11.
 * 2004</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.41 $
 */
public class PanelRechnungKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneRechnungAll jtpTabbedPaneRechnungAll = null;

	private AuftragDto auftragDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private LieferscheinDto lieferscheinDto = null;
	private KundeDto kundeDtoZahlungsadresse = null;
	private KundeDto kundeDtoStatistikadresse = null;
	private AnsprechpartnerDto ansprechpartnerDto = null;
	private RechnungDto rechnungDtoZuRechnung = null;
	private LagerDto lagerDto = null;
	private PersonalDto personalDtoVertreter = null;
	private GutschriftsgrundDto gutschriftsgrundDto = null;
	private boolean bReversecharge = true;

	private static final String ACTION_SPECIAL_AUFTRAG = "action_special_rechnung_auftrag";
	private static final String ACTION_SPECIAL_LIEFERSCHEIN = "action_special_rechnung_lieferschein";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_rechnung_kostenstelle";
	private static final String ACTION_SPECIAL_KUNDE = "action_special_rechnung_kunde";
	private static final String ACTION_SPECIAL_ZAHLUNGSADRESSE = "action_special_rechnung_zahlungsadresse";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER = "action_special_rechnung_ansprechpartner";
	private static final String ACTION_SPECIAL_WAEHRUNG = "action_special_rechnung_waehrung";
	private static final String ACTION_SPECIAL_RECHNUNG = "action_special_rechnung_rechnung";
	private static final String ACTION_SPECIAL_LAGER = "action_special_rechnung_lager";
	private static final String ACTION_SPECIAL_STATISTIKADRESSE = "action_special_rechnung_statistiksadresse";
	private static final String ACTION_SPECIAL_VERTRETER = "action_special_rechnung_vertreter";
	private static final String ACTION_SPECIAL_RECHNUNGSART = "action_special_rechnung_rechnungsart";
	private static final String ACTION_SPECIAL_GUTSCHRIFTSGRUND = "action_special_gutschriftsgrund";

	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRLieferschein = null;
	private PanelQueryFLR panelQueryFLRZahlungsadresse = null;
	private PanelQueryFLR panelQueryFLRRechnung = null;
	private PanelQueryFLR panelQueryFLRLager = null;
	private PanelQueryFLR panelQueryFLRStatistikadresse = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;
	private PanelQueryFLR panelQueryFLRGutschriftsgrund = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jpaWorkingOn = new JPanel();
	private WrapperLabel wlaAbteilung = new WrapperLabel();
	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private WrapperLabel wlaFibuExportDatum = new WrapperLabel();
	private WrapperLabel wlaWert = new WrapperLabel();
	private WrapperDateField wdfLetztesMahndatum = new WrapperDateField();
	private WrapperTextField wtfMahnstufe = new WrapperTextField();
	private WrapperLabel wlaZielland = new WrapperLabel();
	private WrapperLabel wlaMahnstufe = new WrapperLabel();
	private WrapperLabel wlaLetztesMahndatum = new WrapperLabel();

	private WrapperLabel labelProjekt = null;
	private WrapperTextField wtfProjekt = null;

	private WrapperLabel wlaRechnungart = new WrapperLabel();
	private WrapperComboBox wcoRechnungart = new WrapperComboBox();
	private WrapperComboBox wcoWaehrung = new WrapperComboBox();
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperTextField wtfAbteilung = new WrapperTextField();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();
	private WrapperGotoButton wbuKunde = new WrapperGotoButton(
			WrapperGotoButton.GOTO_KUNDE_AUSWAHL);
	private WrapperLabel wlaKurs = new WrapperLabel();
	private WrapperLabel wlaAngezahlt = new WrapperLabel();
	private WrapperNumberField wnfWert = new WrapperNumberField();
	private WrapperNumberField wnfWertFW = new WrapperNumberField();
	private WrapperNumberField wnfAngezahlt = new WrapperNumberField();
	private WrapperNumberField wnfKurs = new WrapperNumberField();
	private WrapperTextField wtfZielland = new WrapperTextField();
	private WrapperDateField wdfDatum = new WrapperDateField();
	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperTextField wtfAuftragNummer = new WrapperTextField();
	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperLabel wbuLieferschein = new WrapperLabel();
	private WrapperButton wbuRechnung = new WrapperButton();
	private WrapperTextField wtfAuftragBezeichnung = new WrapperTextField();
	private WrapperDateField wdfMahnsperre = new WrapperDateField();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperTextField wtfLieferscheinNummer = new WrapperTextField();
	private WrapperTextField wtfLieferscheinBezeichnung = new WrapperTextField();
	private WrapperTextField wtfRechnungNummer = new WrapperTextField();
	private WrapperLabel wlaMahnsperreBis = new WrapperLabel();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperLabel wlaWaehrung1 = new WrapperLabel();
	private WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperLabel wlaWaehrung3 = new WrapperLabel();
	private WrapperButton wbuZahlungsadresse = new WrapperButton();
	private WrapperTextField wtfZahlungsadresse = new WrapperTextField();
	private WrapperTextField wtfLager = new WrapperTextField();
	private WrapperTextField wtfGutschriftsgrund = new WrapperTextField();
	private WrapperButton wbuLager = new WrapperButton();
	private WrapperButton wbuGutschriftsgrund = new WrapperButton();
	private WrapperLabel wlaMehrfachkontierung = new WrapperLabel();
	private WrapperLabel wlaAdresse = new WrapperLabel();
	private WrapperTextField wtfAdresse = new WrapperTextField();
	private WrapperLabel wlaBestellnummer = new WrapperLabel();
	private WrapperTextField wtfBestellnummer = new WrapperTextField();
	private WrapperCheckBox wcbReversecharge = null;
	private WrapperButton wbuStatistikadresse = new WrapperButton();
	private WrapperTextField wtfStatistikadresse = new WrapperTextField();
	private WrapperCheckBox wcbErledigt = new WrapperCheckBox();

	private WrapperButton wbuVertreter = null;
	private WrapperTextField wtfVertreter = null;

	private WrapperLabel wlaZahlungGutschriften = null;
	private WrapperTextArea wtaZahlungGutschriften = null;

	private WrapperSelectField wsfProjekt = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), true);

	boolean bDarf = false;

	/**
	 * PanelRechnungKopfdaten
	 * 
	 * @param internalFrame
	 *            InternalFrame
	 * @param add2TitleI
	 *            String
	 * @param key
	 *            Object
	 * @param tabbedPaneRechnungAll
	 *            String
	 * @throws Throwable
	 */
	public PanelRechnungKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneRechnungAll tabbedPaneRechnungAll) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.jtpTabbedPaneRechnungAll = tabbedPaneRechnungAll;
		jbInit();
		setDefaults();
		initComponents();
	}

	private TabbedPaneRechnungAll getTabbedPaneRechnungAll() {
		return jtpTabbedPaneRechnungAll;
	}

	private void prepareWaehrung() throws Throwable {
		if (!wcoWaehrung.isMapSet()) {
			wcoWaehrung.setMap(DelegateFactory.getInstance()
					.getLocaleDelegate().getAllWaehrungen());
		}

		wcoWaehrung.setKeyOfSelectedItem(LPMain.getTheClient()
				.getSMandantenwaehrung());
	}

	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = getAWhichButtonIUse();
		this.enableToolsPanelButtons(aWhichButtonIUse);

		wtfKunde.setColumnsMax(80);
		this.setLayout(gridBagLayout1);
		wlaMehrfachkontierung
				.setText("Kostenstellenzuordnung siehe Kontierung");
		wlaMehrfachkontierung.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMehrfachkontierung.setVisible(false);
		wlaAbteilung.setText(LPMain.getTextRespectUISPr("lp.abteilung"));
		wlaWaehrung.setText(LPMain.getTextRespectUISPr("lp.waehrung"));
		wlaWert.setText(LPMain.getTextRespectUISPr("lp.bruttowert"));
		wlaDatum.setText(LPMain.getTextRespectUISPr("label.belegdatum"));
		wlaZielland.setText(LPMain.getTextRespectUISPr("label.zielland"));
		wlaMahnstufe.setText(LPMain.getTextRespectUISPr("lp.mahnstufe"));
		wlaLetztesMahndatum.setText(LPMain.getTextRespectUISPr("lp.mahndatum"));
		wlaAngezahlt.setText(LPMain.getTextRespectUISPr("label.bezahlt"));
		wlaKurs.setText(LPMain.getTextRespectUISPr("label.kurs"));
		wlaRechnungart.setText(LPMain.getTextRespectUISPr("label.art"));
		wlaBestellnummer.setText(LPMain
				.getTextRespectUISPr("label.bestellnummer"));

		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);
		bReversecharge = (Boolean) parametermandantDto.getCWertAsObject();

		// geht nur bei neu
		wbuAuftrag.setActivatable(false);
		// wbuLieferschein.setActivatable(false);
		wtfRechnungNummer.setActivatable(false);
		wlaAdresse.setText(LPMain.getTextRespectUISPr("lp.adresse.kbez"));
		wtfAdresse.setActivatable(false);
		wtfAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wcbReversecharge = new WrapperCheckBox();
		wcbReversecharge
				.setText(LPMain.getTextRespectUISPr("lp.reversecharge"));

		wlaZahlungGutschriften = new WrapperLabel(LPMain.getTextRespectUISPr("fb.export.gutschriften"));
		wtaZahlungGutschriften = new WrapperTextArea();
		wtaZahlungGutschriften.setActivatable(false);
		wtaZahlungGutschriften.setRows(3);

		if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			wbuRechnung.setVisible(false);
			wtfRechnungNummer.setVisible(false);
		} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			wlaLetztesMahndatum.setVisible(false);
			wdfLetztesMahndatum.setVisible(false);
			wlaMahnstufe.setVisible(false);
			wtfMahnstufe.setVisible(false);
			wdfMahnsperre.setVisible(false);
			wlaMahnsperreBis.setVisible(false);
			wlaWaehrung2.setVisible(false);
			wlaAngezahlt.setVisible(false);
			wnfAngezahlt.setVisible(false);
			wlaZielland.setVisible(false);
			wtfZielland.setVisible(false);
			wbuLieferschein.setVisible(false);
			wbuAuftrag.setVisible(false);
			wtfAuftragBezeichnung.setVisible(false);
			wtfAuftragNummer.setVisible(false);
			wtfLieferscheinBezeichnung.setVisible(false);
			wtfLieferscheinNummer.setVisible(false);
			wtfZahlungsadresse.setVisible(false);
			wbuZahlungsadresse.setVisible(false);
			wbuRechnung.setVisible(true);
			wtfRechnungNummer.setVisible(true);
			// wtfRechnungNummer.setMandatoryField(true);
			wlaZahlungGutschriften.setVisible(false);
			wtaZahlungGutschriften.setVisible(false);
		} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
			wlaLetztesMahndatum.setVisible(false);
			wdfLetztesMahndatum.setVisible(false);
			wlaMahnstufe.setVisible(false);
			wtfMahnstufe.setVisible(false);
			wdfMahnsperre.setVisible(false);
			wlaMahnsperreBis.setVisible(false);
			wlaWaehrung2.setVisible(false);
			wlaAngezahlt.setVisible(false);
			wnfAngezahlt.setVisible(false);
			wtfZahlungsadresse.setVisible(false);
			wbuZahlungsadresse.setVisible(false);
			wbuRechnung.setVisible(false);
			wtfRechnungNummer.setVisible(false);
			wbuAuftrag.setVisible(false);
			wbuLieferschein.setVisible(false);
			wtfAuftragBezeichnung.setVisible(false);
			wtfAuftragNummer.setVisible(false);
			wtfLieferscheinBezeichnung.setVisible(false);
			wtfLieferscheinNummer.setVisible(false);
			wlaZahlungGutschriften.setVisible(false);
			wtaZahlungGutschriften.setVisible(false);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"Rechnungstyp nicht vorhanden"));
		}
		wbuLieferschein.setText(LPMain
				.getTextRespectUISPr("label.lieferscheinnummer"));
		// wbuLieferschein.setToolTipText(LPMain.getInstance().getTextRespectUISPr
		// (
		// "button.lieferschein.tooltip"));
		wbuRechnung.setText(LPMain.getTextRespectUISPr("button.rechnung"));
		wbuRechnung.setToolTipText(LPMain
				.getTextRespectUISPr("button.rechnung.tooltip"));
		wbuAuftrag.setText(LPMain.getTextRespectUISPr("button.auftrag"));
		wbuAuftrag.setToolTipText(LPMain
				.getTextRespectUISPr("button.auftrag.tooltip"));
		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setToolTipText(LPMain
				.getTextRespectUISPr("button.ansprechpartner.tooltip"));
		wbuStatistikadresse.setText(LPMain
				.getTextRespectUISPr("button.statistikadresse"));
		wbuStatistikadresse.setToolTipText(LPMain
				.getTextRespectUISPr("button.statistikadresse.tooltip"));
		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuKunde.setToolTipText(LPMain
				.getTextRespectUISPr("button.kunde.tooltip"));
		wbuKostenstelle.setText(LPMain
				.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain
				.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wbuLager.setText(LPMain.getTextRespectUISPr("button.lager"));
		wbuLager.setToolTipText(LPMain
				.getTextRespectUISPr("button.lager.tooltip"));
		wbuGutschriftsgrund.setText(LPMain
				.getTextRespectUISPr("rechnung.gutschriftsgrund"));
		wbuGutschriftsgrund.setToolTipText(LPMain
				.getTextRespectUISPr("rechnung.gutschriftsgrund.tooltip"));

		wcoWaehrung.setMandatoryFieldDB(true);
		prepareWaehrung();
		wcoWaehrung.addActionListener(this);
		wcoWaehrung.setActionCommand(ACTION_SPECIAL_WAEHRUNG);

		wcoRechnungart.addActionListener(this);
		wcoRechnungart.setActionCommand(ACTION_SPECIAL_RECHNUNGSART);

		wnfWert.setActivatable(false);
		wnfWertFW.setActivatable(false);
		wnfWert.setVisible(false);
		wlaWaehrung3.setVisible(false);

		wnfAngezahlt.setActivatable(false);
		wnfKurs.setActivatable(false);
		wdfDatum.setActivatable(true);
		wdfDatum.setMandatoryFieldDB(true);

		try {
			bDarf = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);
		} catch (Throwable ex) {
			handleException(ex, true);
		}
		if (!bDarf) {
			parametermandantDto = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
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
			wdfDatum.setMinimumValue(new Date(gc.getTimeInMillis()));
		}

		wtfKunde.setActivatable(false);
		wtfKunde.setMandatoryField(true);
		wtfStatistikadresse.setMandatoryField(true);
		// Actionpanel
		JPanel panelButtonAction = getToolsPanel();
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		wtfLieferscheinNummer.setActivatable(false);
		wtfLieferscheinBezeichnung.setActivatable(false);
		wtfAuftragNummer.setActivatable(false);
		wtfAuftragBezeichnung.setActivatable(false);
		wlaMahnsperreBis
				.setText(LPMain.getTextRespectUISPr("lp.mahnsperrebis"));
		wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfStatistikadresse.setActivatable(false);
		wtfStatistikadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		// wcoWaehrung.setMandatoryFieldDB(true);
		wcoWaehrung
				.addActionListener(new PanelRechnungKopfdaten_wcoWaehrung_actionAdapter(
						this));
		wcoRechnungart
				.addActionListener(new PanelRechnungKopfdaten_wcoRechnungsart_actionAdapter(
						this));

		wtfMahnstufe.setActivatable(false);
		// wdfLetztesMahndatum.setActivatable(false);
		wtfZielland.setActivatable(false);
		wtfKostenstelleNummer.setText("");
		wtfKostenstelleNummer.setActivatable(false);
		wtfKostenstelleNummer.setMandatoryField(true);
		wtfKostenstelleBezeichnung.setText("");
		wtfKostenstelleBezeichnung.setActivatable(false);
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);
		wbuZahlungsadresse.setText(LPMain
				.getTextRespectUISPr("label.zahlungsadresse"));
		wtfZahlungsadresse.setText("");
		wtfZahlungsadresse.setActivatable(false);
		wtfZahlungsadresse.setMandatoryField(false);
		wcoRechnungart.setMandatoryFieldDB(true);

		labelProjekt = new WrapperLabel();
		labelProjekt.setText(LPMain.getTextRespectUISPr(
				"label.projekt"));
		wtfProjekt = new WrapperTextField(80);

		wnfKurs.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wtfLager.setActivatable(false);

		wbuAnsprechpartner.addActionListener(this);
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER);
		wbuAuftrag.addActionListener(this);
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG);
		wbuKostenstelle.addActionListener(this);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKunde.addActionListener(this);
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE);
		// wbuLieferschein.addActionListener(this);
		// wbuLieferschein.setActionCommand(ACTION_SPECIAL_LIEFERSCHEIN);
		wbuZahlungsadresse.addActionListener(this);
		wbuZahlungsadresse.setActionCommand(ACTION_SPECIAL_ZAHLUNGSADRESSE);
		wbuRechnung.addActionListener(this);
		wbuRechnung.setActionCommand(ACTION_SPECIAL_RECHNUNG);

		ImageIcon imageIconRE = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/calculator16x16.png"));

		wbuRechnung.setIcon(imageIconRE);
		wbuLager.addActionListener(this);
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER);
		wbuGutschriftsgrund.addActionListener(this);
		wbuGutschriftsgrund.setActionCommand(ACTION_SPECIAL_GUTSCHRIFTSGRUND);

		wbuStatistikadresse.addActionListener(this);
		wbuStatistikadresse.setActionCommand(ACTION_SPECIAL_STATISTIKADRESSE);
		wtfAbteilung.setActivatable(false);
		wdfLetztesMahndatum.setActivatable(false);
		wtfLager.setMandatoryField(true);

		wbuVertreter = new WrapperButton();
		wbuVertreter.setText(LPMain.getTextRespectUISPr("button.vertreter"));
		wbuVertreter.setToolTipText(LPMain
				.getTextRespectUISPr("button.vertreter.tooltip"));
		wbuVertreter.addActionListener(this);
		wbuVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER);
		wtfVertreter = new WrapperTextField();
		wtfVertreter.setActivatable(false);
		wtfVertreter.setMandatoryField(true);
		wcbErledigt.setText(LPMain.getTextRespectUISPr(
				"label.erledigt"));
		wcbErledigt.setActivatable(false);
		jpaWorkingOn = new JPanel(new MigLayout("wrap 5, hidemode 3", "[fill,25%][fill,35%][fill,15%][fill,25%][fill,25%]"));
		
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(-5, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		jpaWorkingOn.add(wlaRechnungart);
		jpaWorkingOn.add(wcoRechnungart, "span 2");
		jpaWorkingOn.add(wlaDatum);
		jpaWorkingOn.add(wdfDatum, "span");
		
		jpaWorkingOn.add(wbuAuftrag);
		jpaWorkingOn.add(wtfAuftragNummer, "span 2");
		jpaWorkingOn.add(wtfAuftragBezeichnung, "span");
		
		jpaWorkingOn.add(wbuRechnung);
		jpaWorkingOn.add(wtfRechnungNummer, "span 2, wrap");
		
		jpaWorkingOn.add(wbuLieferschein);
		jpaWorkingOn.add(wtfLieferscheinNummer, "span 2");
		jpaWorkingOn.add(wtfLieferscheinBezeichnung, "span");
		
		jpaWorkingOn.add(wbuKunde);
		jpaWorkingOn.add(wtfKunde, "span");
		
		jpaWorkingOn.add(wlaAdresse);
		jpaWorkingOn.add(wtfAdresse, "span");
		
		jpaWorkingOn.add(wlaAbteilung);
		jpaWorkingOn.add(wtfAbteilung, "span");
		
		jpaWorkingOn.add(wbuAnsprechpartner);
		jpaWorkingOn.add(wtfAnsprechpartner, "span");

		jpaWorkingOn.add(wbuVertreter);
		jpaWorkingOn.add(wtfVertreter, "span");
		
		jpaWorkingOn.add(wbuStatistikadresse);
		jpaWorkingOn.add(wtfStatistikadresse, "span");

		jpaWorkingOn.add(wbuKostenstelle);
		jpaWorkingOn.add(wtfKostenstelleNummer, "span 2");
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, "span");
		
		jpaWorkingOn.add(wlaMehrfachkontierung, "skip, span");

		jpaWorkingOn.add(wlaWaehrung);
		jpaWorkingOn.add(wcoWaehrung, "span 2");
		jpaWorkingOn.add(wlaKurs);
		jpaWorkingOn.add(wnfKurs, "wrap");
		
		jpaWorkingOn.add(wlaWert);
		jpaWorkingOn.add(wnfWertFW);
		jpaWorkingOn.add(wlaWaehrung1);
		jpaWorkingOn.add(wnfWert);
		jpaWorkingOn.add(wlaWaehrung3);

		jpaWorkingOn.add(wlaAngezahlt, "newline");
		jpaWorkingOn.add(wnfAngezahlt);
		jpaWorkingOn.add(wlaWaehrung2);
		jpaWorkingOn.add(wcbErledigt);
		if (bReversecharge == true) {
			jpaWorkingOn.add(wcbReversecharge);
		}

		jpaWorkingOn.add(wlaLetztesMahndatum, "newline");
		jpaWorkingOn.add(wdfLetztesMahndatum);
		jpaWorkingOn.add(wlaZielland, "span 2");
		jpaWorkingOn.add(wtfZielland, "wrap");

		jpaWorkingOn.add(wlaMahnstufe);
		jpaWorkingOn.add(wtfMahnstufe);
		jpaWorkingOn.add(wlaMahnsperreBis, "span 2");
		jpaWorkingOn.add(wdfMahnsperre, "wrap");
		
		jpaWorkingOn.add(labelProjekt);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			 parametermandantDto = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PROJEKT_IST_PFLICHTFELD);
			boolean bProjektIstPflichtfeld = ((Boolean) parametermandantDto
					.getCWertAsObject());
			if (bProjektIstPflichtfeld) {
				wsfProjekt.setMandatoryField(true);
			}

			jpaWorkingOn.add(wtfProjekt);
			jpaWorkingOn.add(wsfProjekt.getWrapperGotoButton(), "span 2");
			jpaWorkingOn.add(wsfProjekt.getWrapperTextField(), "wrap");

		} else {
			jpaWorkingOn.add(wtfProjekt, "span");
		}

		jpaWorkingOn.add(wlaBestellnummer);
		jpaWorkingOn.add(wtfBestellnummer, "span");

		jpaWorkingOn.add(wbuLager);
		jpaWorkingOn.add(wtfLager);
		jpaWorkingOn.add(wlaFibuExportDatum, "span");

		jpaWorkingOn.add(wbuGutschriftsgrund);
		jpaWorkingOn.add(wtfGutschriftsgrund, "wrap");

		jpaWorkingOn.add(wlaZahlungGutschriften);
		jpaWorkingOn.add(wtaZahlungGutschriften, "wrap");

	}

	/**
	 * Diese Methode speichert die aktuell erfasste Rechnung. Es kann sich um
	 * eine neue oder eine bestehende handeln.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			RechnungDto rechnungDto = getTabbedPaneRechnungAll()
					.getRechnungDto();

			// wenn alles passt, wird gespeichert
			if (rechnungDto != null) {

				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKreditlimit(rechnungDto.getKundeIId());

				// speichern
				RechnungDto savedDto = DelegateFactory.getInstance()
						.getRechnungDelegate().updateRechnung(rechnungDto);
				// falls neue Rechnung, titel setzen
				if (rechnungDto.getIId() == null) {
					this.setKeyWhenDetailPanel(savedDto.getIId());
				}
				getTabbedPaneRechnungAll().setRechnungDto(savedDto);

				if (savedDto
						.isBMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben() == true) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr(
											"lp.hint"),
									LPMain.getTextRespectUISPr(
													"lp.error.mwstvonnullgeaendertundhandeingaben"));

				}

				// das Panel aktualisieren
				dto2Components();
			}
			super.eventActionSave(e, false);
			getInternalFrame().setKeyWasForLockMe(
					getTabbedPaneRechnungAll().getRechnungDto().getIId()
							.toString());
			eventYouAreSelected(false);
		}
	}

	/**
	 * Befuellen der Comboboxen. Befuellung erfolgt nur beim ersten Aufruf
	 * 
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {
		prepareWaehrung();
		// if (!wcoWaehrung.isMapSet()) {
		// wcoWaehrung.setMap(DelegateFactory.getInstance()
		// .getLocaleDelegate().getAllWaehrungen());
		// }
		if (!wcoRechnungart.isMapSet()) {
			if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				wcoRechnungart.setMap(DelegateFactory.getInstance()
						.getRechnungServiceDelegate()
						.getAllRechnungArtenRechnung());
			} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				wcoRechnungart.setMap(DelegateFactory.getInstance()
						.getRechnungServiceDelegate()
						.getAllRechnungArtenGutschrift());
			} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
				wcoRechnungart.setMap(DelegateFactory.getInstance()
						.getRechnungServiceDelegate()
						.getAllRechnungArtenProformarechnung());
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"Rechnungstyp nicht vorhanden"));
			}
		}
	}

	/**
	 * Die eingegebenen Daten in ein Dto schreiben
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		// den bestehenden Dto verwenden
		RechnungDto dto = getTabbedPaneRechnungAll().getRechnungDto();
		if (dto == null) {
			dto = new RechnungDto();
			dto.setMandantCNr(LPMain.getTheClient().getMandant());
		}
		if (auftragDto != null) {
			dto.setAuftragIId(auftragDto.getIId());
		} else {
			dto.setAuftragIId(null);
		}
		if (lieferscheinDto != null) {
			dto.setLieferscheinIId(lieferscheinDto.getIId());
		} else {
			dto.setLieferscheinIId(null);
		}
		if (kostenstelleDto != null) {
			dto.setKostenstelleIId(kostenstelleDto.getIId());
		} else {
			dto.setKostenstelleIId(null);
		}
		if (ansprechpartnerDto != null) {
			dto.setAnsprechpartnerIId(ansprechpartnerDto.getIId());
		} else {
			dto.setAnsprechpartnerIId(null);
		}
		if (personalDtoVertreter != null) {
			dto.setPersonalIIdVertreter(personalDtoVertreter.getIId());
		} else {
			dto.setPersonalIIdVertreter(null);
		}
		if (getTabbedPaneRechnungAll().getKundeDto() != null) {

			// Hole gespreicherteRechnung
			if (dto.getIId() != null) {
				RechnungDto rDtoVorhanden = DelegateFactory.getInstance()
						.getRechnungDelegate()
						.rechnungFindByPrimaryKey(dto.getIId());
				if (!rDtoVorhanden.getKundeIId().equals(
						getTabbedPaneRechnungAll().getKundeDto().getIId())) {

					DialogGeaenderteKonditionenVK dialog = new DialogGeaenderteKonditionenVK(
							dto, getTabbedPaneRechnungAll().getKundeDto()
									.getIId(), getInternalFrame());
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(dialog);

					if (dialog.bKonditionenUnterschiedlich == true) {
						dialog.setVisible(true);

						if (dialog.bAbgebrochen == false) {
							dto = (RechnungDto) dialog.getBelegVerkaufDto();
						}
					}

				}
			} else {
				dto.setKundeIId(getTabbedPaneRechnungAll().getKundeDto()
						.getIId());
				// Defaultwerte aus dem kunden
				if (dto.getZahlungszielIId() == null) {
					dto.setZahlungszielIId(getTabbedPaneRechnungAll()
							.getKundeDto().getZahlungszielIId());
				}
				if (dto.getLieferartIId() == null) {
					dto.setLieferartIId(getTabbedPaneRechnungAll()
							.getKundeDto().getLieferartIId());
				}
				if (dto.getSpediteurIId() == null) {
					dto.setSpediteurIId(getTabbedPaneRechnungAll()
							.getKundeDto().getSpediteurIId());
				}
				dto.setFAllgemeinerRabattsatz(getTabbedPaneRechnungAll()
						.getKundeDto().getFRabattsatz());
			}

		} else {
			dto.setKundeIId(null);
		}
		if (kundeDtoStatistikadresse != null) {
			dto.setKundeIIdStatistikadresse(kundeDtoStatistikadresse.getIId());
		} else {
			dto.setKundeIIdStatistikadresse(null);
		}

		dto.setTBelegdatum(this.wdfDatum.getTimestamp());
		dto.setRechnungartCNr((String) this.wcoRechnungart
				.getKeyOfSelectedItem());
		dto.setWaehrungCNr((String) this.wcoWaehrung.getKeyOfSelectedItem());
		if (lagerDto != null) {
			dto.setLagerIId(lagerDto.getIId());
		}
		dto.setNKurs(this.wnfKurs.getBigDecimal());
		dto.setTMahnsperrebis(wdfMahnsperre.getDate());
		dto.setCBestellnummer(wtfBestellnummer.getText());
		dto.setCBez(wtfProjekt.getText());

		dto.setProjektIId(wsfProjekt.getIKey());

		if (bReversecharge == true) {
			dto.setBReversecharge(wcbReversecharge.getShort());
		} else {
			dto.setBReversecharge(Helper.boolean2Short(false));
		}
		getTabbedPaneRechnungAll().setRechnungDto(dto);
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		RechnungDto rechnungDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (rechnungDto != null) {
			// Kunde + Partner holen
			holeKunde(rechnungDto.getKundeIId());

			// Lieferschein holen
			holeLieferschein(rechnungDto.getLieferscheinIId());
			// Auftrag holen
			holeAuftrag(rechnungDto.getAuftragIId());
			// Rechnung zur Gutschrift holen
			if (rechnungDto.getRechnungIIdZurechnung() != null) {
				this.rechnungDtoZuRechnung = DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.rechnungFindByPrimaryKey(
								rechnungDto.getRechnungIIdZurechnung());
			} else {
				this.rechnungDtoZuRechnung = null;
			}
			dto2ComponentsRechnungZuRechnung();
			// Kostenstelle holen
			holeKostenstelle(rechnungDto.getKostenstelleIId());
			// Ansprechpartner holen
			holeAnsprechpartner(rechnungDto.getAnsprechpartnerIId());
			// Vertreter holen
			holeVertreter(rechnungDto.getPersonalIIdVertreter());
			// Zahlungsadresse holen
			// holeZahlungsadresse(rechnungDto.getPartnerIIdRechnungsadresse());
			holeStatistikadresse(rechnungDto.getKundeIIdStatistikadresse());
			this.wdfDatum.setDate(rechnungDto.getTBelegdatum());
			this.wcoRechnungart.setKeyOfSelectedItem(rechnungDto
					.getRechnungartCNr());
			passeAnGewaehlteRechnungsartAn();
			this.wcoWaehrung.setKeyOfSelectedItem(rechnungDto.getWaehrungCNr());
			String sMandantWaehrung = LPMain.getTheClient()
					.getSMandantenwaehrung();
			if (rechnungDto.getWaehrungCNr().equals(sMandantWaehrung)) {
				wnfWert.setVisible(false);
				wlaWaehrung3.setVisible(false);
			} else {
				wnfWert.setVisible(true);
				wlaWaehrung3.setVisible(true);
			}
			if (rechnungDto.getLagerIId() != null) {
				lagerDto = DelegateFactory.getInstance().getLagerDelegate()
						.lagerFindByPrimaryKey(rechnungDto.getLagerIId());
				dto2ComponentsLager();
			}

			// PJ16137

			boolean bFibu = false;
			try {
				bFibu = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.darfAnwenderAufModulZugreifen(
								LocaleFac.BELEGART_FINANZBUCHHALTUNG);
			} catch (Throwable e) {
				//
			}

			if (bFibu == false) {
				wlaFibuExportDatum.setText("");

				try {
					ExportdatenDto exportDto = DelegateFactory
							.getInstance()
							.getFibuExportDelegate()
							.exportdatenFindByBelegartCNrBelegiid(
									rechnungDto.getRechnungartCNr(),
									rechnungDto.getIId());

					wlaFibuExportDatum
							.setText(LPMain
									.getTextRespectUISPr("rech.fibuexportdatum")
									+ " "
									+ Helper.formatTimestamp(
											DelegateFactory
													.getInstance()
													.getFibuExportDelegate()
													.exportlaufFindByPrimaryKey(
															exportDto
																	.getExportlaufIId())
													.getTAendern(), LPMain.getTheClient().getLocUi()));

				} catch (Exception e) {
					// Kein Exoprt vorhanden
				}

			}

			wsfProjekt.setKey(rechnungDto.getProjektIId());

			this.wnfKurs.setBigDecimal(rechnungDto.getNKurs());

			wnfKurs.setForeground(Color.BLACK);

			BigDecimal aktuelleKurs = getKurs();
			if (aktuelleKurs != null) {
				if (wnfKurs.getBigDecimal().doubleValue() != aktuelleKurs
						.doubleValue()) {
					wnfKurs.setForeground(Color.RED);
				}
			}

			if (rechnungDto.getNWert() != null
					&& rechnungDto.getNWertust() != null) {
				this.wnfWert.setBigDecimal(rechnungDto.getNWert().add(
						rechnungDto.getNWertust()));
			} else {
				this.wnfWert.setBigDecimal(null);
			}

			if (rechnungDto.getNWertfw() != null
					&& rechnungDto.getNWertustfw() != null) {
				this.wnfWertFW.setBigDecimal(rechnungDto.getNWertfw().add(
						rechnungDto.getNWertustfw()));
			} else {
				this.wnfWertFW.setBigDecimal(null);
			}

			Integer aktuelleMahnstufeIId = DelegateFactory.getInstance()
					.getMahnwesenDelegate()
					.getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId());

			if (aktuelleMahnstufeIId != null) {
				wtfMahnstufe.setText(aktuelleMahnstufeIId.toString());
			} else {
				wtfMahnstufe.setText(null);
			}
			wdfLetztesMahndatum.setDate(DelegateFactory.getInstance()
					.getMahnwesenDelegate()
					.getAktuellesMahndatumEinerRechnung(rechnungDto.getIId()));

			wnfAngezahlt.setBigDecimal(DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(
							rechnungDto.getIId(), null));

			this.wdfMahnsperre.setDate(rechnungDto.getTMahnsperrebis());
			this.wtfBestellnummer.setText(rechnungDto.getCBestellnummer());
			this.wtfProjekt.setText(rechnungDto.getCBez());
			this.setStatusbarPersonalIIdAnlegen(rechnungDto
					.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(rechnungDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(rechnungDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(rechnungDto.getTAendern());
			this.setStatusbarStatusCNr(rechnungDto.getStatusCNr());
			String status = DelegateFactory
					.getInstance()
					.getVersandDelegate()
					.getVersandstatus(LocaleFac.BELEGART_RECHNUNG,
							rechnungDto.getIId());
			if (status != null) {
				status = LPMain.getTextRespectUISPr(
						"lp.versandstatus")
						+ ": " + status;
			}
			setStatusbarSpalte5(status);
			this.wcbReversecharge.setShort(rechnungDto.getBReversecharge());
			this.wcbErledigt.setSelected(rechnungDto.getStatusCNr().equals(
					RechnungFac.STATUS_BEZAHLT));
			String text = DelegateFactory.getInstance().getRechnungDelegate()
					.getGutschriftenEinerRechnung(rechnungDto.getIId());

			wtaZahlungGutschriften.setText(text);
		}
	}

	/**
	 * dto2ComponentsRechnung
	 */
	private void dto2ComponentsRechnungZuRechnung() {
		if (rechnungDtoZuRechnung != null) {
			wtfRechnungNummer.setText(rechnungDtoZuRechnung.getCNr());
		} else {
			wtfRechnungNummer.setText(null);
		}
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

	private void dto2ComponentsLager() {
		if (lagerDto != null) {
			wtfLager.setText(lagerDto.getCNr());
		} else {
			wtfLager.setText(null);
		}
	}

	private void dto2ComponentsGutschriftsgrund() {
		if (gutschriftsgrundDto != null) {
			wtfGutschriftsgrund.setText(gutschriftsgrundDto.getCNr());
		} else {
			wtfGutschriftsgrund.setText(null);
		}
	}

	private void dto2ComponentsAnsprechpartner() {
		if (ansprechpartnerDto != null) {
			PartnerDto aPartnerDto = ansprechpartnerDto.getPartnerDto();
			wtfAnsprechpartner.setText(aPartnerDto.formatFixTitelName1Name2());
		} else {
			wtfAnsprechpartner.setText(null);
		}
	}

	private void dto2ComponentsVertreter() {
		if (personalDtoVertreter != null) {
			PartnerDto aPartnerDto = personalDtoVertreter.getPartnerDto();
			wtfVertreter.setText(aPartnerDto.formatFixTitelName1Name2());
		} else {
			wtfVertreter.setText(null);
		}
	}

	/**
	 * Die Kundendaten in die Felder schreiben
	 * 
	 * @throws Throwable
	 */
	private void dto2ComponentsKunde() throws Throwable {
		if (getTabbedPaneRechnungAll().getKundeDto() != null) {
			// Goto Kunde Button Ziel setzen
			this.wbuKunde.setOKey(getTabbedPaneRechnungAll().getKundeDto()
					.getIId());
			// SK Rechnungsadresse wird nur als Vorschlag verwendet
			// if (getTabbedPaneRechnungAll().getKundeDto().
			// getPartnerIIdRechnungsadresse() != null) {
			// // kundeDtoZahlungsadresse
			// PartnerDto kundeDtoRechnungsadresse =
			// DelegateFactory.getInstance().
			// getPartnerDelegate().partnerFindByPrimaryKey(
			// getTabbedPaneRechnungAll().
			// getKundeDto().
			// getPartnerIIdRechnungsadresse());
			// wtfKunde.setText(kundeDtoRechnungsadresse.formatFixTitelName1Name2
			// ());
			// wtfAdresse.setText(kundeDtoRechnungsadresse.formatAdresse());
			// wtfAbteilung.setText(kundeDtoRechnungsadresse.
			// getCName3vorname2abteilung());
			// }
			// else {
			wtfKunde.setText(getTabbedPaneRechnungAll().getKundeDto()
					.getPartnerDto().formatFixTitelName1Name2());
			String sAdresse = getTabbedPaneRechnungAll().getKundeDto()
					.getPartnerDto().formatAdresse();
			if (getTabbedPaneRechnungAll().getKundeDto().getPartnerDto()
					.getCKbez() != null)
				;
			sAdresse = sAdresse
					+ "  /  "
					+ getTabbedPaneRechnungAll().getKundeDto().getPartnerDto()
							.getCKbez();
			wtfAdresse.setText(sAdresse);
			wtfAbteilung.setText(getTabbedPaneRechnungAll().getKundeDto()
					.getPartnerDto().getCName3vorname2abteilung());
			// }
		} else {
			this.wbuKunde.setOKey(null);
			this.wtfKunde.setText(null);
			this.wtfAdresse.setText(null);
			this.wtfAbteilung.setText(null);
		}
	}

	/**
	 * Die Kundendaten in die Felder schreiben
	 * 
	 * @throws Throwable
	 */
	private void dto2ComponentsKundeZahlungsadresse() throws Throwable {
		if (kundeDtoZahlungsadresse != null) {
			PartnerDto aPartnerDto = kundeDtoZahlungsadresse.getPartnerDto();
			this.wtfZahlungsadresse.setText(aPartnerDto
					.formatFixTitelName1Name2());
		} else {
			this.wtfZahlungsadresse.setText(null);
		}
	}

	/**
	 * Die Kundendaten in die Felder schreiben
	 * 
	 * @throws Throwable
	 */
	private void dto2ComponentsKundeStatistikadresse() throws Throwable {
		if (kundeDtoStatistikadresse != null) {
			PartnerDto aPartnerDto = kundeDtoStatistikadresse.getPartnerDto();
			String sAdress = aPartnerDto.formatFixTitelName1Name2();
			if (aPartnerDto.getCKbez() != null) {
				sAdress = sAdress + "  /  " + aPartnerDto.getCKbez();
			}
			this.wtfStatistikadresse.setText(sAdress);
		} else {
			this.wtfStatistikadresse.setText(null);
		}
	}

	/**
	 * Die Auftragsdaten in die Felder schreiben
	 */
	private void dto2ComponentsAuftrag() {
		if (auftragDto != null) {
			wtfAuftragNummer.setText(auftragDto.getCNr());
			wtfAuftragBezeichnung.setText(auftragDto
					.getCBezProjektbezeichnung());
		} else {
			wtfAuftragNummer.setText(null);
			wtfAuftragBezeichnung.setText(null);
		}
	}

	/**
	 * Die Lieferscheindaten in die Felder schreiben
	 */
	private void dto2ComponentsLieferschein() {
		if (lieferscheinDto != null) {
			wtfLieferscheinNummer.setText(lieferscheinDto.getCNr());
			wtfLieferscheinBezeichnung.setText(lieferscheinDto
					.getCBezProjektbezeichnung());
			wcoWaehrung.setActivatable(false);
		} else {
			wtfLieferscheinNummer.setText(null);
			wtfLieferscheinBezeichnung.setText(null);
			wcoWaehrung.setActivatable(true);
		}
	}

	/**
	 * Eine neue Rechnung erstellen.
	 * 
	 * @param eventObject
	 *            ActionEvent
	 * @param bChangeKeyLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		getTabbedPaneRechnungAll().setRechnungDto(null);
		this.auftragDto = null;
		this.lieferscheinDto = null;
		this.kostenstelleDto = null;
		this.kundeDtoZahlungsadresse = null;
		this.kundeDtoStatistikadresse = null;
		this.ansprechpartnerDto = null;
		this.lagerDto = null;
		this.leereAlleFelder(this);
		setDefaults();
		/**
		 * @todo die buttons f. anprechp., kunde, kost deaktivieren PJ 5079 weil
		 *       muss GS aus RE erstellen ;-)
		 */
		// Hauptlager vorbesetzen
		lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.getHauptlagerDesMandanten();
		dto2ComponentsLager();
		// Kontierung richtig initialisieren
		wbuKostenstelle.setVisible(true);
		wtfKostenstelleBezeichnung.setVisible(true);
		wtfKostenstelleNummer.setVisible(true);
		wlaMehrfachkontierung.setVisible(false);
	}

	/**
	 * Kontextabhaengig die Buttons erstellen.
	 * 
	 * @return String[]
	 */
	private String[] getAWhichButtonIUse() {
		String[] aWhichButtonIUse = null;
		if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			String[] buttons = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE,
					ACTION_DISCARD, ACTION_PRINT };
			aWhichButtonIUse = buttons;
		} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			String[] buttons = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE,
					ACTION_DISCARD, ACTION_PRINT };
			aWhichButtonIUse = buttons;
		} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
			String[] buttons = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE,
					ACTION_DISCARD, ACTION_PRINT };
			aWhichButtonIUse = buttons;
		}
		return aWhichButtonIUse;
	}

	/**
	 * Defaultwerte auf dem Panel setzen.
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		this.leereAlleFelder(this);
		initPanel();
		if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			wcoRechnungart
					.setKeyOfSelectedItem(RechnungFac.RECHNUNGART_RECHNUNG);
		} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			wcoRechnungart
					.setKeyOfSelectedItem(RechnungFac.RECHNUNGART_GUTSCHRIFT);
		} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
			wcoRechnungart
					.setKeyOfSelectedItem(RechnungFac.RECHNUNGART_PROFORMARECHNUNG);
		}
		// Mandantwaehrung setzen
		wcoWaehrung.setKeyOfSelectedItem(LPMain.getTheClient()
				.getSMandantenwaehrung());
		if (getInternalFrameRechnung().neuDatum != null) {
			wdfDatum.setDate(getInternalFrameRechnung().neuDatum);
		} else {
			wdfDatum.setDate(new java.sql.Date(System.currentTimeMillis()));
		}
		// kurs
		setzeKurs();
		this.clearStatusbar();
	}

	/**
	 * Stornieren einer Rechnung bzw Gutschrift
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		RechnungDto rechnungDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
			String sText;
			if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				sText = LPMain
						.getTextRespectUISPr("rechnung.bereitsstorniert.gutschrift");
			} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				sText = LPMain
						.getTextRespectUISPr("rechnung.bereitsstorniert.rechnung");
			} else {
				sText = LPMain
						.getTextRespectUISPr("rechnung.bereitsstorniert.proformarechnung");
			}
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"), sText);
			return;
		}

		String[] optionen = new String[2];
		optionen[0] = LPMain.getTextRespectUISPr("lp.ja");
		optionen[1] = LPMain.getTextRespectUISPr("lp.nein");

		String sText;
		if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			sText = LPMain
					.getTextRespectUISPr("rechnung.stornieren.gutschrift");
		} else if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			sText = LPMain.getTextRespectUISPr("rechnung.stornieren.rechnung");
		} else {
			sText = LPMain
					.getTextRespectUISPr("rechnung.stornieren.proformarechnung");
		}
		int choice = JOptionPane.showOptionDialog(this, sText,
				LPMain.getTextRespectUISPr("lp.frage"),
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				optionen, optionen[1]);
		switch (choice) {
		case JOptionPane.YES_OPTION: {
			DelegateFactory.getInstance().getRechnungDelegate()
					.storniereRechnung(rechnungDto.getIId());
			this.eventYouAreSelected(false);
		}
			break;
		case JOptionPane.NO_OPTION: {
			// nix tun
		}
			break;
		default: {
			// nix tun
		}
		}
	}

	private InternalFrameRechnung getInternalFrameRechnung() {
		return (InternalFrameRechnung) getInternalFrame();
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_RECHNUNG;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER)) {
			if (getTabbedPaneRechnungAll().getKundeDto() != null) {
				dialogQueryAnsprechpartner();
			} else {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("rechnung.kundewaehlen"));
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG)) {
			dialogQueryAuftrag();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER)) {
			dialogQueryLager();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_GUTSCHRIFTSGRUND)) {
			dialogQueryGutschriftsgrund();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE)) {
			dialogQueryKunde();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERSCHEIN)) {
			/**
			 * @todo
			 */
			// if (auftragDto == null) {
			// dialogQueryLieferschein();
			// }
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ZAHLUNGSADRESSE)) {
			if (getTabbedPaneRechnungAll().getKundeDto() != null) {
				dialogQueryZahlungsadresse();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_STATISTIKADRESSE)) {
			dialogQueryStatistiksadresse();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RECHNUNG)) {

			panelQueryFLRRechnung = RechnungFilterFactory.getInstance()
					.createPanelFLRRechnungOffen(getInternalFrame(), null);
			if (rechnungDtoZuRechnung != null) {
				panelQueryFLRRechnung.setSelectedId(rechnungDtoZuRechnung
						.getIId());
			}
			new DialogQuery(panelQueryFLRRechnung);

			// dialogQueryRechnung();
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER)) {
			panelQueryFLRVertreter = PersonalFilterFactory.getInstance()
					.createPanelFLRPersonal(getInternalFrame(), true, true);
			if (personalDtoVertreter != null) {
				panelQueryFLRVertreter.setSelectedId(personalDtoVertreter
						.getIId());
			}
			new DialogQuery(panelQueryFLRVertreter);
		}

	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeAnsprechpartner(key);
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeAuftrag((Integer) key);
				if (auftragDto != null
						&& getTabbedPaneRechnungAll().getKundeDto() == null) {
					holeKunde(auftragDto.getKundeIIdRechnungsadresse());
					besetzeKundendatenVor(auftragDto
							.getKundeIIdRechnungsadresse());
				}
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKostenstelle(key);
			} else if (e.getSource() == panelQueryFLRKunde) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(key);
				// PJ 15739
				if (kundeDto.getPartnerDto().getLandplzortIId() == null) {
					DialogFactory.showModalDialog(LPMain
							.getTextRespectUISPr("lp.error"), LPMain
							.getTextRespectUISPr("rech.error.kundeohnelkz"));
					return;
				}

				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(key);

				holeKunde(key);
				besetzeKundendatenVor(key);
			} else if (e.getSource() == panelQueryFLRLieferschein) {
				// Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
				// if (getTabbedPaneRechnungAll().getRechnungDto() == null ||
				// getTabbedPaneRechnungAll().getRechnungDto().getIId() == null)
				// {
				// erstelleRechnungAusLieferschein( (Integer) key);
				// }
				// else {
				// erstelleRechnungPositionAusLieferschein( (Integer) key);
				// }
			} else if (e.getSource() == panelQueryFLRZahlungsadresse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeZahlungsadresse(key);
			} else if (e.getSource() == panelQueryFLRStatistikadresse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeStatistikadresse(key);
			} else if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeLager(key);
			} else if (e.getSource() == panelQueryFLRGutschriftsgrund) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeGutschriftsgrund(key);
			} else if (e.getSource() == panelQueryFLRRechnung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (getTabbedPaneRechnungAll().getRechnungDto() != null
						&& getTabbedPaneRechnungAll().getRechnungDto().getIId() != null) {
					// SP853
					getTabbedPaneRechnungAll().getRechnungDto()
							.setRechnungIIdZurechnung((Integer) key);
					rechnungDtoZuRechnung = DelegateFactory.getInstance()
							.getRechnungDelegate()
							.rechnungFindByPrimaryKey((Integer) key);
					dto2ComponentsRechnungZuRechnung();

				} else {
					erstelleGutschriftAusRechnung((Integer) key);
				}

			} else if (e.getSource() == panelQueryFLRVertreter) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeVertreter(key);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				holeAnsprechpartner(null);
			} else if (e.getSource() == panelQueryFLRVertreter) {
				holeVertreter(null);
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				holeAuftrag(null);
			} else if (e.getSource() == panelQueryFLRRechnung) {
				rechnungDtoZuRechnung = null;
				getTabbedPaneRechnungAll().getRechnungDto()
						.setRechnungIIdZurechnung(null);
				dto2ComponentsRechnungZuRechnung();
			}
		}
	}

	private void besetzeKundendatenVor(Integer key) throws ExceptionLP,
			Throwable {
		// Vertreter aus kunde
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE);
		Integer iIdPersonal = null;
		if ((Boolean) parametermandantDto.getCWertAsObject()) {
			// es kann einen default vertreter beim kunden geben
			iIdPersonal = jtpTabbedPaneRechnungAll.getKundeDto()
					.getPersonaliIdProvisionsempfaenger();
			// wenn nicht, bin ichs selber
			if (iIdPersonal == null) {
				iIdPersonal = LPMain.getTheClient().getIDPersonal();
			}
		} else {
			iIdPersonal = LPMain.getTheClient().getIDPersonal();
		}
		holeVertreter(iIdPersonal);
		// Ansprechpartner vorbesetzen?
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_RECHNUNG_ANSP_VORBESETZEN,
						ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameter.getCWertAsObject()) {
			// ansp_vorbesetzen: den ersten Ansprechpartner vorbesetzen
			this.ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(
							getTabbedPaneRechnungAll().getKundeDto()
									.getPartnerIId());
			dto2ComponentsAnsprechpartner();
		}
		// Kunden auf UID-Nummer pruefen
		DelegateFactory.getInstance().getKundeDelegate()
				.pruefeKundenUIDNummer(key);

		if (getTabbedPaneRechnungAll().getRechnungDto().getIId() == null) {
			KundeDto kundeDto = DelegateFactory.getInstance()
					.getKundeDelegate().kundeFindByPrimaryKey(key);

			lagerDto = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(
							kundeDto.getLagerIIdAbbuchungslager());
			wtfLager.setText(lagerDto.getCNr());

		}

	}

//	/**
//	 * erstelleRechnungPositionAusLieferschein
//	 * 
//	 * @param key
//	 *            Integer
//	 * @throws Throwable
//	 */
//	private void erstelleRechnungPositionAusLieferschein(Integer key)
//			throws Throwable {
//		LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
//				.lieferscheinFindByPrimaryKey(key);
//		RechnungDto reDto = getTabbedPaneRechnungAll().getRechnungDto();
//		if (!reDto.getKundeIId().equals(lsDto.getKundeIIdRechnungsadresse())) {
//			boolean bAnswer = (DialogFactory
//					.showMeldung(
//							"Die Rechnungsadresse des Lieferscheins "
//									+ lsDto.getCNr()
//									+ " stimmt nicht\nmit dem Kunden dieser Rechnung ueberein. Trotzdem hinzufuegen?",
//							LPMain.getTextRespectUISPr("lp.frage"),
//							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
//			if (!bAnswer) {
//				return;
//			}
//		} else {
//			boolean bAnswer = (DialogFactory.showMeldung("Lieferschein "
//					+ lsDto.getCNr() + " hinzufuegen?",
//					LPMain.getTextRespectUISPr("lp.frage"),
//					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
//			if (!bAnswer) {
//				return;
//			}
//		}
//		RechnungPositionDto rePos = new RechnungPositionDto();
//		rePos.setRechnungpositionartCNr(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN);
//		rePos.setRechnungIId(reDto.getIId());
//		rePos.setLieferscheinIId(lsDto.getIId());
//		rePos.setCBez(lsDto.getCBezProjektbezeichnung());
//		rePos.setNEinzelpreis(lsDto.getNGesamtwertInLieferscheinwaehrung());
//		rePos.setNNettoeinzelpreis(lsDto.getNGesamtwertInLieferscheinwaehrung());
//		rePos.setNBruttoeinzelpreis(lsDto
//				.getNGesamtwertInLieferscheinwaehrung());
//		rePos.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
//		rePos.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
//		rePos.setBDrucken(Helper.boolean2Short(true));
//		DelegateFactory.getInstance().getRechnungDelegate()
//				.updateRechnungPosition(rePos, reDto.getLagerIId());
//		// eventuell wuede die LS-Zuordnung veraendert - diese anzeigen
//		getTabbedPaneRechnungAll().reloadRechnungDto();
//		holeLieferschein(getTabbedPaneRechnungAll().getRechnungDto()
//				.getLieferscheinIId());
//	}

	/**
	 * erstelle Gutschrift aus Rechnung.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void erstelleGutschriftAusRechnung(Integer key) throws Throwable {
		if (key != null) {
			RechnungDto rechnung = DelegateFactory.getInstance()
					.getRechnungDelegate().rechnungFindByPrimaryKey(key);

			DelegateFactory.getInstance().getKundeDelegate()
					.pruefeKunde(rechnung.getKundeIId());

			boolean bAnswer = (DialogFactory.showMeldung(
					LPMain.getTextRespectUISPr("rech.gutschriftausrechnung")
							+ " " + rechnung.getCNr() + " "
							+ LPMain.getTextRespectUISPr("rech.erstellen"),
					LPMain.getTextRespectUISPr("lp.frage"),

					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!bAnswer) {
				return;
			}
			Integer iIdRechnung = DelegateFactory.getInstance()
					.getRechnungDelegate()
					.createGutschriftAusRechnung(key, wdfDatum.getDate());
			this.setKeyWhenDetailPanel(iIdRechnung);
			rechnung = DelegateFactory.getInstance().getRechnungDelegate()
					.rechnungFindByPrimaryKey(iIdRechnung);
			getTabbedPaneRechnungAll().setRechnungDto(rechnung);
			this.eventYouAreSelected(false);
		}
	}

	private void holeAnsprechpartner(Object key) throws Throwable {
		if (key != null) {
			ansprechpartnerDto = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey((Integer) key);
		} else {
			ansprechpartnerDto = null;
		}
		dto2ComponentsAnsprechpartner();
	}

	private void holeVertreter(Object key) throws Throwable {
		if (key != null) {
			personalDtoVertreter = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey((Integer) key);
		} else {
			personalDtoVertreter = null;
		}
		dto2ComponentsVertreter();
	}

	private void holeKostenstelle(Object key) throws Throwable {
		if (key != null) {
			kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
					.kostenstelleFindByPrimaryKey((Integer) key);
		} else {
			kostenstelleDto = null;
		}
		dto2ComponentsKostenstelle();
	}

	private void holeLager(Object key) throws Throwable {
		if (key != null) {
			lagerDto = DelegateFactory.getInstance().getLagerDelegate()
					.lagerFindByPrimaryKey((Integer) key);
		} else {
			lagerDto = null;
		}
		dto2ComponentsLager();
	}

	private void holeGutschriftsgrund(Object key) throws Throwable {
		if (key != null) {
			gutschriftsgrundDto = DelegateFactory.getInstance()
					.getRechnungServiceDelegate()
					.gutschriftsgrundFindByPrimaryKey((Integer) key);
		} else {
			gutschriftsgrundDto = null;
		}
		dto2ComponentsGutschriftsgrund();
	}

	private void holeLieferschein(Object key) throws Throwable {
		if (key != null) {
			lieferscheinDto = DelegateFactory.getInstance().getLsDelegate()
					.lieferscheinFindByPrimaryKey((Integer) key);
		} else {
			lieferscheinDto = null;
		}
		dto2ComponentsLieferschein();
	}

	private void holeAuftrag(Object key) throws ExceptionLP, Throwable {
		if (key != null) {
			auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey((Integer) key);
		} else {
			auftragDto = null;
		}
		dto2ComponentsAuftrag();
	}

	private void holeKunde(Integer key) throws Throwable {
		if (key != null) {
			// wenn neue Rechnung, dann Eigenschaften vom Kunden uebernehmen
			if (getTabbedPaneRechnungAll().getRechnungDto() == null) {
				KundeDto kundeDto = getTabbedPaneRechnungAll().getKundeDto();
				// fuer eine neue muss ich den kunden erst holen
				if (kundeDto == null) {
					kundeDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey((Integer) key);
				}
				// default kostenstelle de kunden
				if (kostenstelleDto == null) {
					holeKostenstelle(kundeDto.getKostenstelleIId());
				}
				// in kundenwaehrung
				if (kundeDto.getCWaehrung() != null) {
					wcoWaehrung.setKeyOfSelectedItem(kundeDto.getCWaehrung());
				}
				// und nun die konditionen
				// dazu muss ich jetz schon das dto anlegen
				RechnungDto reDto = new RechnungDto();
				reDto.setZahlungszielIId(kundeDto.getZahlungszielIId());
				reDto.setLieferartIId(kundeDto.getLieferartIId());
				reDto.setSpediteurIId(kundeDto.getSpediteurIId());
				if (kundeDto.getMwstsatzbezIId() != null) {
					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG,
									ParameterFac.KATEGORIE_KUNDEN,
									LPMain.getTheClient().getMandant());
					boolean isPositionskontierung = ((Boolean) parameter
							.getCWertAsObject()).booleanValue();
					if (!isPositionskontierung) {
						// Aktuellen MWST-Satz uebersetzen.
						MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
								.getInstance()
								.getMandantDelegate()
								.mwstsatzFindByMwstsatzbezIIdAktuellster(
										kundeDto.getMwstsatzbezIId());
						reDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
					}
				}
				reDto.setBMindermengenzuschlag(kundeDto
						.getBMindermengenzuschlag());
				// dto nach oben
				reDto.setKundeIId(kundeDto.getIId());
				reDto.setMandantCNr(LPMain.getTheClient().getMandant());
				getTabbedPaneRechnungAll().setRechnungDto(reDto);
				// reverse charge

				if (bReversecharge) {
					reDto.setBReversecharge(kundeDto.getBReversecharge());
					wcbReversecharge.setShort(kundeDto.getBReversecharge());
				} else {
					reDto.setBReversecharge(Helper.boolean2Short(false));
					wcbReversecharge.setShort(Helper.boolean2Short(false));
				}
				// Statistikadresse vorbesetzen
				if (kundeDtoStatistikadresse == null) {
					holeStatistikadresse(key);
				}
			} else {
				// den Kunden nachtraeglich aendern
				getTabbedPaneRechnungAll().getRechnungDto().setKundeIId(key);
				// SK Konditionen auch bei Kundenaenderung uebernehmen
				//
				KundeDto kundeDto = getTabbedPaneRechnungAll().getKundeDto();
				if (kundeDto == null) {
					kundeDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey((Integer) key);
				}
				getTabbedPaneRechnungAll().getRechnungDto().setZahlungszielIId(
						kundeDto.getZahlungszielIId());
				getTabbedPaneRechnungAll().getRechnungDto().setLieferartIId(
						kundeDto.getLieferartIId());
				getTabbedPaneRechnungAll().getRechnungDto().setSpediteurIId(
						kundeDto.getSpediteurIId());
				if (kundeDto.getMwstsatzbezIId() != null) {
					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG,
									ParameterFac.KATEGORIE_KUNDEN,
									LPMain.getTheClient().getMandant());
					boolean isPositionskontierung = ((Boolean) parameter
							.getCWertAsObject()).booleanValue();
					if (!isPositionskontierung) {
						// Aktuellen MWST-Satz uebersetzen.
						MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
								.getInstance()
								.getMandantDelegate()
								.mwstsatzFindByMwstsatzbezIIdAktuellster(
										kundeDto.getMwstsatzbezIId());
						getTabbedPaneRechnungAll().getRechnungDto()
								.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
					}
				}
				getTabbedPaneRechnungAll().setRechnungDto(
						getTabbedPaneRechnungAll().getRechnungDto());
			}
			// erst jetzt, da sich die TabbedPane den Kunden selber holt
			dto2ComponentsKunde();
		}
	}

	private void holeZahlungsadresse(Object key) throws Throwable {
		if (key != null) {
			kundeDtoZahlungsadresse = DelegateFactory.getInstance()
					.getKundeDelegate().kundeFindByPrimaryKey((Integer) key);
		} else {
			kundeDtoZahlungsadresse = null;
		}
		dto2ComponentsKundeZahlungsadresse();
	}

	private void holeStatistikadresse(Object key) throws Throwable {
		if (key != null) {
			// wenn der Kunde eh der selbe ist, dann kann ich den verwenden.
			if (getTabbedPaneRechnungAll().getKundeDto() != null
					&& getTabbedPaneRechnungAll().getKundeDto().getIId()
							.equals(key)) {
				kundeDtoStatistikadresse = getTabbedPaneRechnungAll()
						.getKundeDto();
			} else {
				kundeDtoStatistikadresse = DelegateFactory.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey((Integer) key);
			}
		} else {
			kundeDtoStatistikadresse = null;
		}
		dto2ComponentsKundeStatistikadresse();
	}

	private void dialogQueryKostenstelle() throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	/**
	 * Dialogfenster zur Auftragauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryAuftrag() throws Throwable {
		FilterKriterium[] fk;
		if (getTabbedPaneRechnungAll().getKundeDto() == null) {
			fk = SystemFilterFactory.getInstance().createFKMandantCNr();
		} else {
			fk = AuftragFilterFactory.getInstance()
					.createFKAuftraegeEinesKunden(
							getTabbedPaneRechnungAll().getKundeDto().getIId());
		}
		panelQueryFLRAuftrag = AuftragFilterFactory
				.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), true, true, fk, null);
		if (auftragDto != null) {
			panelQueryFLRAuftrag.setSelectedId(auftragDto.getIId());
		}
		new DialogQuery(panelQueryFLRAuftrag);
	}

	/**
	 * Dialogfenster zur Ansprechpartnerauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryAnsprechpartner() throws Throwable {
		Integer ansprechpartnerIId = null;
		if (ansprechpartnerDto != null) {
			ansprechpartnerIId = ansprechpartnerDto.getIId();
		}

		panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
				.createPanelFLRAnsprechpartner(
						getInternalFrame(),
						getTabbedPaneRechnungAll().getKundeDto()
								.getPartnerIId(), ansprechpartnerIId, true,
						true);

		new DialogQuery(panelQueryFLRAnsprechpartner);
	}

	/**
	 * Dialogfenster zur Kundenauswahl fuer die Zahlungsadresse.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryZahlungsadresse() throws Throwable {
		panelQueryFLRZahlungsadresse = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, true);
		new DialogQuery(panelQueryFLRZahlungsadresse);
	}

	/**
	 * Dialogfenster zur Kundenauswahl fuer die Statistiksadresse.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryStatistiksadresse() throws Throwable {
		panelQueryFLRStatistikadresse = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, false);
		if (kundeDtoStatistikadresse != null) {
			panelQueryFLRStatistikadresse
					.setSelectedId(kundeDtoStatistikadresse.getIId());
		}
		new DialogQuery(panelQueryFLRStatistikadresse);
	}

//	/**
//	 * Dialogfenster zur Lieferscheinauswahl.
//	 * 
//	 * @throws Throwable
//	 */
//	private void dialogQueryLieferschein() throws Throwable {
//		FilterKriterium[] fk = LieferscheinFilterFactory.getInstance()
//				.createFKGelieferteLieferscheine();
//		String sTitle = LPMain
//				.getTextRespectUISPr("ls.print.listenichtverrechnet");
//		panelQueryFLRLieferschein = LieferscheinFilterFactory
//				.getInstance()
//				.createPanelQueryFLRLieferschein(getInternalFrame(), fk, sTitle);
//		new DialogQuery(panelQueryFLRLieferschein);
//	}

//	/**
//	 * Dialogfenster zur Rechnungsauswahl.
//	 * 
//	 * @throws Throwable
//	 */
//	private void dialogQueryRechnung() throws Throwable {
//		panelQueryFLRRechnung = RechnungFilterFactory.getInstance()
//				.createPanelFLRRechnung(getInternalFrame(), null);
//		if (rechnungDtoZuRechnung != null) {
//			panelQueryFLRRechnung.setSelectedId(rechnungDtoZuRechnung.getIId());
//		}
//		new DialogQuery(panelQueryFLRRechnung);
//	}

	/**
	 * Dialogfenster zur Kundenauswahl fuer die Lieferadresse.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryKunde() throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, false);
		if (getTabbedPaneRechnungAll().getKundeDto() != null) {
			panelQueryFLRKunde.setSelectedId(getTabbedPaneRechnungAll()
					.getKundeDto().getIId());
		}
		new DialogQuery(panelQueryFLRKunde);
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getTabbedPaneRechnungAll().print();
		eventYouAreSelected(false);
	}

	/**
	 * Ich bin selektiert worden. evtyas: 1 in der Panelklasse ueberschreiben
	 * 
	 * @param bNeedNoYouAreSelectedI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		// evtyas: 3 methode der superklasse aufrufen
		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {
			// evtyas: 4 das Dto ins Panel schreiben
			RechnungDto rechnungDto = getTabbedPaneRechnungAll()
					.getRechnungDto();

			if (rechnungDto != null) {
				rechnungDto = DelegateFactory.getInstance()
						.getRechnungDelegate()
						.rechnungFindByPrimaryKey(rechnungDto.getIId());
				getTabbedPaneRechnungAll().setRechnungDto(rechnungDto);

				initPanel();
				dto2Components();
				if (DelegateFactory.getInstance().getRechnungDelegate()
						.getProzentsatzKontiert(rechnungDto.getIId())
						.compareTo(new BigDecimal(0)) == 0) {
					wbuKostenstelle.setVisible(true);
					wtfKostenstelleBezeichnung.setVisible(true);
					wtfKostenstelleNummer.setVisible(true);
					wlaMehrfachkontierung.setVisible(false);
				} else {
					wbuKostenstelle.setVisible(false);
					wtfKostenstelleBezeichnung.setVisible(false);
					wtfKostenstelleNummer.setVisible(false);
					wlaMehrfachkontierung.setVisible(true);
				}
			}
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		RechnungDto rechnungDto = getTabbedPaneRechnungAll().getRechnungDto();
		boolean allowed = getInternalFrameRechnung()
				.isUpdateAllowedForRechnungDto(rechnungDto);
		super.eventActionUpdate(aE, !allowed);
		// updaten

		// PJ18129
		if (rechnungDto.getLieferscheinIId() != null) {
			LieferscheinDto lsDto = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.lieferscheinFindByPrimaryKey(
							rechnungDto.getLieferscheinIId());
			if (lsDto.getProjektIId() != null) {
				wsfProjekt.setEnabled(false);
				wsfProjekt.getWrapperGotoButton().setEnabled(false);
			}

		}

		dto2Components();
	}

	protected void setzeKurs() throws Throwable {
		wnfKurs.setForeground(Color.BLACK);
		String sMandantWaehrung = LPMain.getTheClient().getSMandantenwaehrung();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("rech.keinestandardwaehrung"));
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		String waehrung = (String) wcoWaehrung.getKeyOfSelectedItem();
		if (waehrung != null) {
			if (sMandantWaehrung.equals(waehrung)) {
				wnfKurs.setBigDecimal(new BigDecimal(1));
			} else {

				BigDecimal bdKurs = DelegateFactory.getInstance()
						.getLocaleDelegate()
						.getWechselkurs2(sMandantWaehrung, waehrung);

				if (wdfDatum.getDate() != null) {
					WechselkursDto wDto = DelegateFactory
							.getInstance()
							.getLocaleDelegate()
							.getKursZuDatum(sMandantWaehrung, waehrung,
									wdfDatum.getDate());

					if (wDto != null) {
						bdKurs = wDto.getNKurs().setScale(6,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}

				if (bdKurs != null) {
					wnfKurs.setBigDecimal(bdKurs);
				} else {
					wnfKurs.setBigDecimal(null);
					String savedTitle = LPMain.getInstance().getDesktop()
							.getTitle();
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									"Zwischen "
											+ sMandantWaehrung
											+ " und "
											+ waehrung
											+ " ist kein Kurs hinterlegt\nBitte tragen Sie diesen nach");
					LPMain.getInstance().getDesktop().setTitle(savedTitle);
					LPMain.getInstance().exitFrameSilent(getInternalFrame());
				}
			}
		}
		wlaWaehrung1.setText(waehrung);
		wlaWaehrung2.setText(waehrung);
		wlaWaehrung3.setText(sMandantWaehrung);
	}

	protected BigDecimal getKurs() throws Throwable {
		String sMandantWaehrung = LPMain.getTheClient().getSMandantenwaehrung();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("rech.keinestandardwaehrung"));
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		String waehrung = (String) wcoWaehrung.getKeyOfSelectedItem();
		if (waehrung != null) {
			if (sMandantWaehrung.equals(waehrung)) {
				return new BigDecimal(1);
			} else {
				BigDecimal bdKurs = DelegateFactory.getInstance()
						.getLocaleDelegate()
						.getWechselkurs2(sMandantWaehrung, waehrung);

				WechselkursDto wDto = DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.getKursZuDatum(sMandantWaehrung, waehrung,
								wdfDatum.getDate());

				if (wDto != null) {
					bdKurs = wDto.getNKurs().setScale(6,
							BigDecimal.ROUND_HALF_EVEN);
					;
				}
				if (bdKurs != null) {
					return bdKurs;
				} else {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									"Zwischen "
											+ sMandantWaehrung
											+ " und "
											+ waehrung
											+ " ist kein Kurs hinterlegt\nBitte tragen Sie diesen nach");
					LPMain.getInstance().exitFrameSilent(getInternalFrame());
					return null;
				}
			}
		}
		return null;

	}

	protected void allowAuftragLieferschein() {
		wbuAuftrag.setEnabled(true);
		wbuLieferschein.setEnabled(true);
	}

	/**
	 * Eigene ExceptionLP's verarbeiten. myexception: 2 methode ueberschreiben
	 * 
	 * @return boolean
	 * @param exfc
	 *            ExceptionLP
	 * @throws Throwable
	 */
	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		if (exfc.getICode() == EJBExceptionLP.FEHLER_RECHNUNG_DARF_LAGER_NICHT_AENDERN) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("rechnung.lagernichtaendern"));
			// myexception: 3 konnte verarbeitet werden
			return true;
		} else if (exfc.getICode() == EJBExceptionLP.FEHLER_RECHNUNG_HAT_LIEFERSCHEINE_EINES_ANDEREN_KUNDEN) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("rechnung.lseinesanderenkunden"));
			return true;
		} else if (exfc.getICode() == EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			return true;
		} else if (exfc.getICode() == EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT) {
			String waehrung = (String) wcoWaehrung.getKeyOfSelectedItem();
			String sMandantWaehrung = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant())
					.getWaehrungCNr();
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							"Zwischen "
									+ sMandantWaehrung
									+ " und "
									+ waehrung
									+ " ist kein Kurs hinterlegt\nBitte tragen Sie diesen nach");
			return true;
		}
		if (exfc.getICode() == EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getMsg(exfc));
			return true;
		} else {
			// myexception: 4 konnte nicht verarbeitet werden
			return false;
		}
	}

	private void dialogQueryLager() throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						(lagerDto != null) ? lagerDto.getIId() : null);
		new DialogQuery(panelQueryFLRLager);
	}

	private void dialogQueryGutschriftsgrund() throws Throwable {
		panelQueryFLRGutschriftsgrund = RechnungFilterFactory.getInstance()
				.createPanelFLRGutschriftsgrund(
						getInternalFrame(),
						(gutschriftsgrundDto != null) ? gutschriftsgrundDto
								.getIId() : null);
		new DialogQuery(panelQueryFLRGutschriftsgrund);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		if (wbuAuftrag.isActivatable() && wbuAuftrag.isEnabled()) {
			return wbuAuftrag;
		} else {
			return wbuKunde;
		}
	}

	protected void passeAnGewaehlteRechnungsartAn() {
		String sArt = (String) wcoRechnungart.getKeyOfSelectedItem();
		if (sArt.equals(RechnungFac.RECHNUNGART_ANZAHLUNG)
				|| sArt.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
			wtfAuftragNummer.setMandatoryField(true);
		} else {
			wtfAuftragNummer.setMandatoryField(false);
		}
		if (sArt.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
				|| sArt.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
			wtfGutschriftsgrund.setVisible(true);
			wbuGutschriftsgrund.setVisible(true);
		} else {
			wtfGutschriftsgrund.setVisible(false);
			wbuGutschriftsgrund.setVisible(false);
		}
	}
}

class PanelRechnungKopfdaten_wcoWaehrung_actionAdapter implements
		java.awt.event.ActionListener {
	private PanelRechnungKopfdaten adaptee;

	PanelRechnungKopfdaten_wcoWaehrung_actionAdapter(
			PanelRechnungKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			adaptee.setzeKurs();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class PanelRechnungKopfdaten_wcoRechnungsart_actionAdapter implements
		java.awt.event.ActionListener {
	private PanelRechnungKopfdaten adaptee;

	PanelRechnungKopfdaten_wcoRechnungsart_actionAdapter(
			PanelRechnungKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			adaptee.passeAnGewaehlteRechnungsartAn();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}
