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
package com.lp.client.eingangsrechnung;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
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
import com.lp.client.frame.component.WrapperLabelOverlay;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um die Kopfdaten der Eingangsrechnung</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.42 $
 */
public class PanelEingangsrechnungKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MEHRFACH = "mehrfach";
	private BestellungDto bestellungDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private ZahlungszielDto zahlungszielDto = null;
	private KontoDto kontoDto = null;
	private PanelQueryFLR panelQueryFLRWaehrung = null;
	private boolean bFibuInstalliert = false;
	private int iStellenLieferantenrechnungsnummer = 0;
	private boolean bReversechargeVerwenden = true;
	private boolean bIstModulKostenstelleInstalliert = false;

	public final static String ACTION_SPECIAL_ART = "action_special_er_art";
	public final static String ACTION_SPECIAL_MWST = "action_special_er_mwst";
	public final static String ACTION_SPECIAL_WAEHRUNG = "action_special_er_waehrung";
	public final static String ACTION_SPECIAL_BESTELLUNG = "action_special_er_bestellung";
	public final static String ACTION_SPECIAL_LIEFERANT = "action_special_er_lieferant";
	public final static String ACTION_SPECIAL_KONTO = "action_special_er_konto";
	public final static String ACTION_SPECIAL_KOSTENSTELLE = "action_special_er_kostenstelle";
	public final static String ACTION_SPECIAL_ZAHLUNGSZIEL = "action_special_er_zahlungsziel";
	public final static String ACTION_SPECIAL_MEHRFACH = "action_special_er_mehrfach";
	public final static String ACTION_SPECIAL_IGERWERB = "action_special_igerwerb";
	public final static String ACTION_SPECIAL_REVERSECHARGE = "action_special_reversecharge";

	public final static String MY_OWN_NEW_TOGGLE_WIEDERHOLEND_ERLEDIGT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_WIEDERHOLEND_ERLEDIGT";

	public final static String MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_ZOLLIMPORTPAPIER_ERHALTEN";

	private PanelQueryFLR panelQueryFLRLieferant = null;
	private PanelQueryFLR panelQueryFLRBestellung = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRZahlungsziel = null;
	private PanelQueryFLR panelQueryFLRKonto = null;

	private WrapperCheckBox wcbReversecharge = null;
	private WrapperCheckBox wcbIGErwerb = null;
	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperGotoButton wbuLieferant = new WrapperGotoButton(
			WrapperGotoButton.GOTO_LIEFERANT_AUSWAHL);
	private WrapperTextField wtfLieferant = new WrapperTextField();
	private WrapperButton wbuKonto = new WrapperButton();
	private WrapperTextField wtfKontoNummer = new WrapperTextField();
	private WrapperLabel wlaArt = new WrapperLabel();
	private WrapperComboBox wcoArt = new WrapperComboBox();
	private WrapperLabel wlaBelegdatum = new WrapperLabel();
	private WrapperLabel wlaFreigabedatum = new WrapperLabel();
	private WrapperDateField wdfBelegdatum = new WrapperDateField();
	private WrapperDateField wdfFreigabedatum = new WrapperDateField();
	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperLabel wlaWEArtikel = new WrapperLabel();

	private WrapperLabel wlaFibuExportDatum = new WrapperLabel();

	private WrapperLabel wlaKreditorennummer = new WrapperLabel();

	private JTextField wtnfLieferantenrechnungsnummer = null;
	private WrapperTextField wtfText = new WrapperTextField();
	private WrapperTextField wtfWEArtikel = new WrapperTextField();
	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperNumberField wnfBetrag = new WrapperNumberField();
	private WrapperButton wbuWaehrung = new WrapperButton();
	public WrapperLabel wlaWaehrung1 = new WrapperLabel();
	public WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperTextField wtfWaehrung = new WrapperTextField();
	private WrapperLabel wlaKurs = new WrapperLabel();
	private WrapperNumberField wnfKurs = new WrapperNumberField();
	private WrapperLabel wlaMwst = new WrapperLabel();
	private WrapperComboBox wcoMwst = new WrapperComboBox();
	private WrapperNumberField wnfMwst = new WrapperNumberField();
	private WrapperButton wbuBestellung = new WrapperButton();
	private WrapperTextField wtfBestellung = new WrapperTextField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperButton wbuZahlungsziel = new WrapperButton();
	private WrapperTextField wtfZahlungsziel = new WrapperTextField();
	private JLabel wlaZollimportpapiere = new JLabel();
	private WrapperLabel wlaWiederholendErledigt = new WrapperLabel();

	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperCheckBox wcbMehrfachkontierung = new WrapperCheckBox();
	private WrapperLabel wlaLieferantenrechnungsnummer = null;
	private WrapperTextField wtfAdresse = new WrapperTextField();
	private WrapperTextField wtfAbteilung = new WrapperTextField();
	private WrapperLabel wlaAbteilung = new WrapperLabel();
	private WrapperTextField wtfKundendaten = new WrapperTextField();
	private WrapperLabel wlaKundendaten = new WrapperLabelOverlay();

	private WrapperLabel wlaWiederholungsintervall = null;
	private WrapperComboBox wcoWiederholungsintervall = null;
	private WrapperLabel wlaNachfolger = new WrapperLabel();
	private WrapperTextField wtfNachfolger = new WrapperTextField();

	private WrapperLabel wlaAnzahlungen;
	private WrapperTextField wtfAnzahlungen;

	private WrapperLabel wlaNochNichtKontiert = new WrapperLabel();
	private BigDecimal nettoBetrag;

	private boolean bMapSetAktiv = false;

	public PanelEingangsrechnungKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung)
			throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
		jbInit();
		initPanel();
		initComponents();
	}

	private TabbedPaneEingangsrechnung getTabbedPane() {
		return tabbedPaneEingangsrechnung;
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaLieferantenrechnungsnummer = new WrapperLabel();

		// Texte
		wbuLieferant.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferant"));
		wbuLieferant.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferant.tooltip"));
		wbuBestellung.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.bestellung"));
		wbuBestellung.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.bestellung.tooltip"));
		wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wbuZahlungsziel.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.zahlungsziel"));
		wbuZahlungsziel.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.zahlungsziel.tooltip"));
		wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto"));
		wbuKonto.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto.tooltip"));
		wlaAbteilung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abteilung"));
		wlaArt.setText(LPMain.getInstance().getTextRespectUISPr("label.art"));

		wlaKreditorennummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.kreditorennr")
				+ ":");

		wlaBelegdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.belegdatum"));
		wlaFreigabedatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.freigabedatum"));
		wlaText.setText(LPMain.getInstance().getTextRespectUISPr("label.text"));
		wtfWEArtikel.setColumnsMax(80);
		wlaWEArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.weartikel"));
		wbuWaehrung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.waehrung"));
		wlaKurs.setText(LPMain.getInstance().getTextRespectUISPr("label.kurs"));
		wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.bruttobetrag"));
		wlaMwst.setText(LPMain.getInstance().getTextRespectUISPr("label.mwst"));
		wlaNochNichtKontiert.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.nochnichtvollstaendigkontiert"));
		wlaNochNichtKontiert.setForeground(Color.RED);
		wlaLieferantenrechnungsnummer.setText(LPMain
				.getTextRespectUISPr("label.lieferantenrechnungsnummer"));

		wcbMehrfachkontierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.checkbox.mehrfach"));
		wcbMehrfachkontierung.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("er.checkbox.mehrfach.tooltip"));
		// sizes
		wcbReversecharge = new WrapperCheckBox();
		wcbReversecharge
				.setText(LPMain.getTextRespectUISPr("lp.reversecharge"));
		wcbReversecharge.setActionCommand(ACTION_SPECIAL_REVERSECHARGE);
		wcbReversecharge.addActionListener(this);

		ParametermandantDto parametermandantRCDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);
		bReversechargeVerwenden = (Boolean) parametermandantRCDto
				.getCWertAsObject();

		wcbIGErwerb = new WrapperCheckBox();
		wcbIGErwerb.setText(LPMain.getTextRespectUISPr("lp.igerwerb"));
		wcbIGErwerb.setActionCommand(ACTION_SPECIAL_IGERWERB);
		wcbIGErwerb.addActionListener(this);

		wlaWiederholungsintervall = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.wiederholungsintervall"));
		wcoWiederholungsintervall = new WrapperComboBox();

		wlaKundendaten.setText(LPMain.getTextRespectUISPr("er.kundendaten"));
		/**
		 * @todo 12 nicht hart codiert
		 */
		wtfKundendaten.setColumnsMax(12);
		// beschraenkungen
		/**
		 * @todo begrenzung PJ 4958
		 */
		// wnfBetrag.setMinimumValue(0);
		wnfKurs.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		// activatable
		wtfLieferant.setActivatable(false);
		wtfBestellung.setActivatable(false);
		wnfKurs.setActivatable(false);
		wnfMwst.setActivatable(false);
		wtfAbteilung.setActivatable(false);
		wtfAdresse.setActivatable(false);
		wtfKostenstelleBezeichnung.setActivatable(false);
		wtfKostenstelleNummer.setActivatable(false);

		wtfKontoNummer.setActivatable(false);
		wtfZahlungsziel.setActivatable(false);
		wtfWaehrung.setActivatable(false);
		// dependence
		wnfBetrag.setDependenceField(true);
		wnfMwst.setDependenceField(true);
		// mandatory
		wcoArt.setMandatoryFieldDB(true);
		wcoMwst.setMandatoryFieldDB(true);
		wtfWaehrung.setMandatoryFieldDB(true);
		wtfLieferant.setMandatoryFieldDB(true);
		wdfBelegdatum.setMandatoryFieldDB(true);

		boolean bRechtChefbuchhalter = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);
		if (!bRechtChefbuchhalter) {
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
		}

		wlaNachfolger.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.nachfolger"));
		wtfNachfolger.setActivatable(false);

		wdfFreigabedatum.setMandatoryFieldDB(true);
		wtfKostenstelleNummer.setMandatoryField(true);
		wtfKontoNummer.setMandatoryField(true);
		wtfKontoNummer.setColumnsMax(15 + 40);
		wnfBetrag.setMandatoryFieldDB(true);
		// Commands
		wcoArt.setActionCommand(ACTION_SPECIAL_ART);
		wcoMwst.setActionCommand(ACTION_SPECIAL_MWST);
		wbuWaehrung.setActionCommand(ACTION_SPECIAL_WAEHRUNG);
		wbuWaehrung.addActionListener(this);
		wbuBestellung.setActionCommand(ACTION_SPECIAL_BESTELLUNG);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT);
		wbuZahlungsziel.setActionCommand(ACTION_SPECIAL_ZAHLUNGSZIEL);
		wcbMehrfachkontierung.setActionCommand(ACTION_SPECIAL_MEHRFACH);
		// max
		wtfLieferant.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		// Listener
		wcoArt.addActionListener(this);
		wcoMwst.addActionListener(this);
		wtfWaehrung.addActionListener(this);
		wbuBestellung.addActionListener(this);
		wbuKostenstelle.addActionListener(this);
		wbuKonto.addActionListener(this);
		wbuLieferant.addActionListener(this);
		wbuZahlungsziel.addActionListener(this);
		wcbMehrfachkontierung.addActionListener(this);
		wnfBetrag
				.addFocusListener(new PanelEingangsrechnungKopfdaten_wnfBetrag_focusAdapter(
						this));

		bFibuInstalliert = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG);

		wcbIGErwerb.setActivatable(!bFibuInstalliert); // bei Fibu nicht
														// aenderbar, da aus
														// Kreditorenkonto
		// PJ18303

		wlaAnzahlungen = new WrapperLabel(
				LPMain.getTextRespectUISPr("rech.zahlung.anzahlungen"));
		wtfAnzahlungen = new WrapperTextField(9999);
		wtfAnzahlungen.setActivatable(false);

		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
						ParameterFac.PARAMETER_EINGANGSRECHNUNG_LIEFERANTENRECHNUNGSNUMMER_LAENGE);
		iStellenLieferantenrechnungsnummer = Integer
				.parseInt(parametermandantDto.getCWert());

		if (iStellenLieferantenrechnungsnummer == -1) {
			wtnfLieferantenrechnungsnummer = new WrapperTextField(20);
		} else {
			wtnfLieferantenrechnungsnummer = new WrapperTextNumberField();
		}

		if (wtnfLieferantenrechnungsnummer instanceof WrapperTextNumberField) {
			((WrapperTextNumberField) wtnfLieferantenrechnungsnummer)
					.setMandatoryField(true);
		} else if (wtnfLieferantenrechnungsnummer instanceof WrapperTextField) {
			((WrapperTextField) wtnfLieferantenrechnungsnummer)
					.setMandatoryField(true);
		}

		if (bFibuInstalliert) {

			if (wtnfLieferantenrechnungsnummer instanceof WrapperTextNumberField) {
				((WrapperTextNumberField) wtnfLieferantenrechnungsnummer)
						.setMaximumDigits(20);
			} else if (wtnfLieferantenrechnungsnummer instanceof WrapperTextField) {
				((WrapperTextField) wtnfLieferantenrechnungsnummer)
						.setColumnsMax(20);
			}

		} else {
			if (iStellenLieferantenrechnungsnummer != -1) {
				if (wtnfLieferantenrechnungsnummer instanceof WrapperTextNumberField) {
					((WrapperTextNumberField) wtnfLieferantenrechnungsnummer)
							.setMaximumDigits(iStellenLieferantenrechnungsnummer);
				} else if (wtnfLieferantenrechnungsnummer instanceof WrapperTextField) {
					((WrapperTextField) wtnfLieferantenrechnungsnummer)
							.setColumnsMax(iStellenLieferantenrechnungsnummer);

				}
			}

		}
		// Actionpanel
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT };
		enableToolsPanelButtons(aWhichButtonIUse);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 1.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		getToolBar().getToolsPanelRight().add(wlaZollimportpapiere);
		// jpaWorkingOn.add(wlaZollimportpapiere, new GridBagConstraints(2,
		// iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaArt, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoArt, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaWiederholendErledigt, new GridBagConstraints(2,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (!tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			iZeile++;
			jpaWorkingOn.add(wbuBestellung, new GridBagConstraints(0, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfBestellung, new GridBagConstraints(1, iZeile,
					3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wbuLieferant, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferant, new GridBagConstraints(1, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wtfAdresse, new GridBagConstraints(1, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAbteilung, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAbteilung, new GridBagConstraints(1, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, new GridBagConstraints(2,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMehrfachkontierung, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaNochNichtKontiert, new GridBagConstraints(2,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKonto, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoNummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKreditorennummer, new GridBagConstraints(2, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuZahlungsziel, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZahlungsziel, new GridBagConstraints(1, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBelegdatum, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBelegdatum, new GridBagConstraints(1, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFreigabedatum, new GridBagConstraints(2, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfFreigabedatum, new GridBagConstraints(3, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaLieferantenrechnungsnummer, new GridBagConstraints(
				0, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtnfLieferantenrechnungsnummer,
				new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKundendaten, new GridBagConstraints(2, iZeile, 1,
				1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKundendaten, new GridBagConstraints(3, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfText, new GridBagConstraints(1, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaWEArtikel, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWEArtikel, new GridBagConstraints(1, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung1, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaFibuExportDatum, new GridBagConstraints(2, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuWaehrung, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWaehrung, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKurs, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKurs, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMwst, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoMwst, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		if (bReversechargeVerwenden == true) {
			jpaWorkingOn.add(wcbReversecharge, new GridBagConstraints(3,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wcbIGErwerb, new GridBagConstraints(3, iZeile + 1,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;
		jpaWorkingOn.add(wnfMwst, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung2, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			iZeile++;
			jpaWorkingOn.add(wlaWiederholungsintervall, new GridBagConstraints(
					2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wcoWiederholungsintervall, new GridBagConstraints(
					3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wlaNachfolger, new GridBagConstraints(2, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfNachfolger, new GridBagConstraints(3, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wlaAnzahlungen, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnzahlungen, new GridBagConstraints(1, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			getToolBar()
					.addButtonCenter(
							"/com/lp/client/res/check2.png",
							LPMain.getTextRespectUISPr("er.zusatzkosten.wiederholungerledigen"),
							MY_OWN_NEW_TOGGLE_WIEDERHOLEND_ERLEDIGT, null, null);
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
		} else {
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			getToolBar()
					.addButtonCenter(
							"/com/lp/client/res/document_preferences.png",
							LPMain.getTextRespectUISPr("er.zollimportpapiere.erhalten"),
							MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN, null,
							null);
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
			// getToolsPanel().add(new WrapperLabel(""));
		}

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	/**
	 * Neue ER.
	 * 
	 * @param eventObject
	 *            EventObject
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, bLockMeI, bNeedNoNewI);
		getTabbedPane().setEingangsrechnungDto(null);
		getTabbedPane().setLieferantDto(null);
		this.bestellungDto = null;
		this.kostenstelleDto = null;
		this.zahlungszielDto = null;
		this.kontoDto = null;
		this.leereAlleFelder(this);
		setDefaults();
		updateMehrfach();
		// noch nicht vollstaendig kontiert ausblenden
		wlaNochNichtKontiert.setVisible(false);
		this.clearStatusbar();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// PJ18167
		LPButtonAction lpbaZoll = getHmOfButtons().get(
				MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN);
		if (lpbaZoll != null) {
			lpbaZoll.getButton().setVisible(false);
		}

		if (!bNeedNoYouAreSelectedI) {
			EingangsrechnungDto erDto = getTabbedPane()
					.getEingangsrechnungDto();
			if (erDto != null) {
				getTabbedPane().setEingangsrechnungDto(
						DelegateFactory
								.getInstance()
								.getEingangsrechnungDelegate()
								.eingangsrechnungFindByPrimaryKey(
										erDto.getIId()));
				dto2Components();
				getTabbedPane().enablePanels();

				LieferantDto lfDto = DelegateFactory.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(erDto.getLieferantIId());
				if (lfDto != null
						&& Helper.short2boolean(lfDto.getBZollimportpapier())) {
					if (lpbaZoll != null) {
						lpbaZoll.getButton().setVisible(true);
					}
				}

			}
		}

	}

	private void dialogQueryWaehrung(ActionEvent e) throws Throwable {
		String selectetWaehrung = null;
		if (getTabbedPane().getEingangsrechnungDto() != null) {
			selectetWaehrung = getTabbedPane().getEingangsrechnungDto()
					.getWaehrungCNr();
		}

		panelQueryFLRWaehrung = FinanzFilterFactory.getInstance()
				.createPanelFLRWaehrung(getInternalFrame(), selectetWaehrung);
		new DialogQuery(panelQueryFLRWaehrung);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_BESTELLUNG)) {
			dialogQueryBestellung();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT)) {
			dialogQueryLieferant(e);
			setIGErwerbReverseCharge();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ZAHLUNGSZIEL)) {
			dialogQueryZahlungsziel(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_WAEHRUNG)) {
			dialogQueryWaehrung(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MWST)) {
			wnfBetrag_focusLost();
			if (wcoMwst.isEnabled() && !bMapSetAktiv) {
				if (kontoDto != null
						&& Helper.short2boolean(kontoDto.getBOhneUst())) {
					if (wcoMwst.getSelectedItem() != null) {
						if (!wcoMwst.getKeyOfSelectedItem().equals(MEHRFACH)) {
							MwstsatzDto mwst = getMwstsatzForSelected();
							if (mwst != null && mwst.getFMwstsatz() != 0.0) {
								DialogFactory
										.showModalDialog(
												LPMain.getInstance()
														.getTextRespectUISPr(
																"lp.hint"),
												LPMain.getInstance()
														.getTextRespectUISPr(
																"er.hint.keinevst"));
							}
						}
					}
				}
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MEHRFACH)) {
			updateMehrfach();
			// falls NICHT mehrfach, dann kst des lieferanten vorbesetzen
			if (wcbMehrfachkontierung.isSelected() == false) {
				// falls er eine hat
				if (getTabbedPane().getLieferantDto().getIIdKostenstelle() != null) {
					holeKostenstelle(getTabbedPane().getLieferantDto()
							.getIIdKostenstelle());
				}
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ART)) {
			String sArt = wcoArt.getKeyOfSelectedItem().toString();
			if (sArt.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG)
					|| sArt.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
				wbuBestellung.setVisible(false);
				wtfBestellung.setVisible(false);
				wtfBestellung.setMandatoryField(false);
			} else {
				wbuBestellung.setVisible(true);
				wtfBestellung.setVisible(true);
				wtfBestellung.setMandatoryField(true);
			}
			boolean schlusszahlung = sArt
					.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG);
			wlaAnzahlungen.setVisible(schlusszahlung);
			wtfAnzahlungen.setVisible(schlusszahlung);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
			dialogQueryKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_IGERWERB)) {
			if (wcbIGErwerb.isSelected() && wcbReversecharge.isSelected())
				wcbReversecharge.setSelected(false);
			setIGErwerbReverseCharge();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_REVERSECHARGE)) {
			if (wcbReversecharge.isSelected() && wcbIGErwerb.isSelected())
				wcbIGErwerb.setSelected(false);
			else {
				if (bFibuInstalliert) {
					if (!wcbReversecharge.isSelected()) {
						LieferantDto lieferantDto = getTabbedPane()
								.getLieferantDto();
						if (lieferantDto != null) {
							boolean istIgErwerb = DelegateFactory
									.getInstance()
									.getFinanzServiceDelegate()
									.istIgErwerb(
											getTabbedPane()
													.getLieferantDto()
													.getKontoIIdKreditorenkonto());
							wcbIGErwerb.setSelected(istIgErwerb);
						}
					}
				}
			}
			setIGErwerbReverseCharge();
		} else if (e.getActionCommand().equals(
				MY_OWN_NEW_TOGGLE_WIEDERHOLEND_ERLEDIGT)) {
			// PJ 17584
			if (getTabbedPane().getEingangsrechnungDto() != null
					&& getTabbedPane().getEingangsrechnungDto().getIId() != null) {
				DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.toggleWiederholdendErledigt(
								getTabbedPane().getEingangsrechnungDto()
										.getIId());
				eventYouAreSelected(false);
			}
		} else if (e.getActionCommand().equals(
				MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN)) {
			// PJ 17696
			if (getTabbedPane().getEingangsrechnungDto() != null
					&& getTabbedPane().getEingangsrechnungDto().getIId() != null) {

				// SP1811

				boolean b = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("er.exportbeleg.aendern.stufe1"));
				if (b == false) {
					return;
				}

				if (getTabbedPane().getEingangsrechnungDto()
						.getTFibuuebernahme() != null) {

					boolean bChefbuchhalter = DelegateFactory.getInstance()
							.getTheJudgeDelegate()
							.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);

					if (bChefbuchhalter) {
						b = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("er.exportbeleg.aendern.stufe2"));
						if (b == false) {
							return;
						}
					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("er.exportbeleg.aendern.keineberechtigung"));
						return;
					}

				}

				if (getTabbedPane().getEingangsrechnungDto()
						.getTZollimportpapier() == null) {

					DialogZollbeleg d = new DialogZollbeleg(getTabbedPane()
							.getEingangsrechnungDto().getIId(), getTabbedPane());
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				} else {
					DelegateFactory
							.getInstance()
							.getEingangsrechnungDelegate()
							.toggleZollimportpapiereErhalten(
									getTabbedPane().getEingangsrechnungDto()
											.getIId(), null, null);
				}

				eventYouAreSelected(false);
			}
		}
	}

	private void setIGErwerbReverseCharge() throws ExceptionLP, Throwable {
		boolean selected = wcbIGErwerb.isSelected()
				|| wcbReversecharge.isSelected();
		if (selected) {
			wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr(
					"label.bruttobetrag.ig"));
			wlaMwst.setText(LPMain.getInstance().getTextRespectUISPr(
					"label.mwst.ig"));
		} else {
			wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr(
					"label.bruttobetrag"));
			wlaMwst.setText(LPMain.getInstance().getTextRespectUISPr(
					"label.mwst"));
		}
		updateMwst();
	}

	/**
	 * updateMehrfach.
	 * 
	 * @throws Throwable
	 */
	private void updateMehrfach() throws Throwable {
		if (wcbMehrfachkontierung.isSelected()) {
			wbuKostenstelle.setEnabled(false);
			wtfKostenstelleNummer.setMandatoryField(false);
			wbuKonto.setEnabled(false);
			wtfKontoNummer.setMandatoryField(false);
			kostenstelleDto = null;
			kontoDto = null;
			dto2ComponentsKostenstelle();
			dto2ComponentsKonto();
			wcoMwst.setMap(getMapSieheKontierung());
			wcoMwst.setEnabled(false);
		} else {
			// buttons nur aktiveren wenn die anderen auch aktiviert sind
			if (wbuLieferant.isEnabled()) {
				LockStateValue lockstateValue = super
						.getLockedstateDetailMainKey();
				if (!(lockstateValue.getIState() == LOCK_IS_NOT_LOCKED)) {

					wbuKostenstelle.setEnabled(true);
					wbuKonto.setEnabled(true);
				}
			}
			wtfKostenstelleNummer
					.setMandatoryField(true && bIstModulKostenstelleInstalliert);
			wtfKontoNummer.setMandatoryField(true);
			bMapSetAktiv = true; // Pruefung auf keineUst uebergehen
			if (getTabbedPane().getEingangsrechnungDto() != null) {
				wcoMwst.setMap(DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.getAllMwstsatz(
								LPMain.getInstance().getTheClient()
										.getMandant(),
								new Timestamp(getTabbedPane()
										.getEingangsrechnungDto()
										.getDBelegdatum().getTime())));
			} else {
				wcoMwst.setMap(DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.getAllMwstsatz(
								LPMain.getInstance().getTheClient()
										.getMandant(),
								new Timestamp(System.currentTimeMillis())));
			}
			bMapSetAktiv = false;
			if (getTabbedPane().getEingangsrechnungDto() != null) {
				wcoMwst.setKeyOfSelectedItem(getTabbedPane()
						.getEingangsrechnungDto().getMwstsatzIId());
			}
			LockStateValue lsv = this.getLockedstateDetailMainKey();
			if (lsv.getIState() != PanelBasis.LOCK_IS_NOT_LOCKED) {
				wcoMwst.setEnabled(true);
			}
		}
	}

	/**
	 * Speichere ER.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// noetigenfalls den Betrag merken
		BigDecimal bdAlterWert = null;
		if (getTabbedPane().getEingangsrechnungDto() != null) {
			bdAlterWert = getTabbedPane().getEingangsrechnungDto()
					.getNBetragfw();
		}

		
		//SP3255
		if (wnfBetrag.hasFocus()) {
			updateMwst();
		} 
		
		try {
			if (allMandatoryFieldsSetDlg()) {
				if (getTabbedPane().getEingangsrechnungDto() == null) {
					DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.pruefeLieferant(
									getTabbedPane().getLieferantDto().getIId(),LocaleFac.BELEGART_EINGANGSRECHNUNG,
									getInternalFrame());
				}
				components2Dto();
				// Betrag prufen
				if (wnfBetrag.getBigDecimal().doubleValue() == 0) {
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"er.betragnullnichtmoeglich"));

					return;
				}
				// keine Lieferantenrechnungsnummer eingegeben
				if (wtnfLieferantenrechnungsnummer.getText() == null) {
					boolean answer = (DialogFactory.showMeldung(
							LPMain.getInstance().getTextRespectUISPr(
									"er.ohnelieferantenrechnungsnummer"),
							LPMain.getInstance()
									.getTextRespectUISPr("lp.frage"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
					if (!answer) {
						return;
					}
				}
				// pruefen, obs schon ER's mit dieser Lieferantenrechnungsnummer
				// gibt
				else {
					EingangsrechnungDto[] dtos = DelegateFactory
							.getInstance()
							.getEingangsrechnungDelegate()
							.eingangsrechnungFindByLieferantIIdLieferantenrechnungsnummerOhneExc(
									getTabbedPane().getEingangsrechnungDto()
											.getLieferantIId(),
									getTabbedPane().getEingangsrechnungDto()
											.getCLieferantenrechnungsnummer());
					if (dtos != null && dtos.length > 0) {
						// wenn es die ER ist, die ich grad bearbeite
						if (getTabbedPane().getEingangsrechnungDto().getIId() != null
								&& dtos.length == 1
								&& dtos[0].getIId().equals(
										getTabbedPane()
												.getEingangsrechnungDto()
												.getIId())) {
							// nothing here
						} else {
							StringBuffer sb = new StringBuffer();
							sb.append(LPMain
									.getInstance()
									.getTextRespectUISPr(
											"er.error.lieferantenrechnungsnummer_doppelt"));
							for (int i = 0; i < dtos.length; i++) {
								// auch hier die aktuelle nicht anzeigen
								if (getTabbedPane().getEingangsrechnungDto()
										.getIId() != null
										&& dtos[i]
												.getIId()
												.equals(getTabbedPane()
														.getEingangsrechnungDto()
														.getIId())) {
									// nothing here
								} else {
									sb.append("\n");
									sb.append(dtos[i].getCNr());
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
				/**
				 * @todo Lieferantenrechnungsnummer auf fibutauglichkeit pruefen
				 *       (10 stellen, numerisch) PJ 4959
				 */
				boolean bIsNewER = getTabbedPane().getEingangsrechnungDto()
						.getIId() == null;
				EingangsrechnungDto erDto = DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.updateEingangsrechnung(
								getTabbedPane().getEingangsrechnungDto());
				this.setKeyWhenDetailPanel(erDto.getIId());
				getTabbedPane().setEingangsrechnungDto(erDto);
				if (getTabbedPane().getWareneingangDto() != null) {
					getTabbedPane().getWareneingangDto()
							.setEingangsrechnungIId(erDto.getIId());
					DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.updateWareneingang(
									getTabbedPane().getWareneingangDto());
					getTabbedPane().setWareneingangDto(null);
				}

				if (getTabbedPane().getInseratIIds() != null
						&& getTabbedPane().getInseratIIds().length > 0) {
					for (int i = 0; i < getTabbedPane().getInseratIIds().length; i++) {
						DelegateFactory
								.getInstance()
								.getInseratDelegate()
								.eingangsrechnungZuordnen(
										(Integer) getTabbedPane()
												.getInseratIIds()[i],
										erDto.getIId(),
										erDto.getNBetrag().subtract(
												erDto.getNUstBetrag()));
					}
					getTabbedPane().setInseratIIds(null);
				}

				super.eventActionSave(e, true);
				eventYouAreSelected(false);
				if (bIsNewER) {
					getTabbedPane().setSelectedEingangsrechnungIId();
				}
				nettoBetrag = null;
			}
		} catch (ExceptionLP t) {
			switch (t.getICode()) {
			case EJBExceptionLP.FEHLER_LIEFERANTENRECHNUNGSNUMMER_DOPPELT: {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"er.error.lieferantenrechnungsnummer_doppelt"));
			}
				break;
			case EJBExceptionLP.FEHLER_WERT_UNTER_AUFTRAGSZUORDNUNG: {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"er.error.wert_unter_auftragszuordnung"));
				wnfBetrag.setBigDecimal(bdAlterWert);
				wnfBetrag_focusLost();
			}
				break;
			default: {
				/**
				 * @todo PJ 4960
				 */
				throw t;
			}
			}
		}
	}

	/**
	 * Stornieren einer ER.
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
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_STORNIERT)) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"),
					"Die Eingangsrechnung ist bereits storniert");
			return;
		}
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_ERLEDIGT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr("lp.hint"),
							"Die Eingangsrechnung ist bereits erledigt\nSie kann daher nicht storniert werden");
			return;
		}
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_TEILBEZAHLT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr("lp.hint"),
							"Die Eingangsrechnung ist bereits teilweise bezahlt\nSie kann daher nicht storniert werden");
			return;
		}
		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ANGELEGT)) {
			boolean answer = (DialogFactory.showMeldung("Eingangsrechnung "
					+ erDto.getCNr() + " stornieren?", LPMain.getInstance()
					.getTextRespectUISPr("lp.frage"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			} else {
				DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.storniereEingangsrechnung(
								getTabbedPane().getEingangsrechnungDto()
										.getIId());
			}
		}
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBestellung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeBestellung((Integer) key);
				if (getTabbedPane().getLieferantDto() == null) {
					holeLieferant(bestellungDto.getLieferantIIdBestelladresse());
					// Feld Kundendaten ist vorbesetzt mit der Kundennummer d.
					// Lieferanten
					if (getTabbedPane().getLieferantDto() != null
							&& getTabbedPane().getLieferantDto().getCKundennr() != null) {
						/**
						 * @todo 12 nicht hart codiert
						 */
						String cKnr = Helper.cutString(getTabbedPane()
								.getLieferantDto().getCKundennr(), 12);
						wtfKundendaten.setText(cKnr);
					} else {
						wtfKundendaten.setText(null);
					}
				}
			} else if (e.getSource() == panelQueryFLRWaehrung) {
				String cNrWaehrung = (String) ((ISourceEvent) e.getSource())
						.getIdSelected();
				wtfWaehrung.setText(cNrWaehrung);
				wlaWaehrung1.setText(cNrWaehrung);
				wlaWaehrung2.setText(cNrWaehrung);
				setzeWechselkurs(cNrWaehrung);

			} else if (e.getSource() == panelQueryFLRLieferant) {

				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				holeLieferant(key);
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKostenstelle((Integer) key);
			} else if (e.getSource() == panelQueryFLRZahlungsziel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeZahlungsziel((Integer) key);
			} else if (e.getSource() == panelQueryFLRKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKonto((Integer) key);
			}
		}
	}

	private BigDecimal getWechselkurs(String cNrWaehrung) throws ExceptionLP,
			Throwable {
		String sMandantWaehrung = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"),
					"Beim Mandanten ist keine Standard-Waehrung hinterlegt");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		String sWaehrung = cNrWaehrung;
		if (sWaehrung.equals(sMandantWaehrung)) {
			return new BigDecimal(1);
		} else {
			BigDecimal bdKurs = DelegateFactory.getInstance()
					.getLocaleDelegate()
					.getWechselkurs2(sWaehrung, sMandantWaehrung);
			if (bdKurs != null) {
				return bdKurs.setScale(6, BigDecimal.ROUND_HALF_EVEN);
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								"Zwischen "
										+ sMandantWaehrung
										+ " und "
										+ sWaehrung
										+ " ist kein Kurs hinterlegt\nBitte tragen Sie diesen nach");
				return null;
			}
		}
	}

	private void setzeWechselkurs(String cNrWaehrung) throws ExceptionLP,
			Throwable {
		wnfKurs.setForeground(Color.BLACK);
		String sMandantWaehrung = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"),
					"Beim Mandanten ist keine Standard-Waehrung hinterlegt");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		String sWaehrung = cNrWaehrung;
		if (sWaehrung.equals(sMandantWaehrung)) {
			wnfKurs.setBigDecimal(new BigDecimal(1));
		} else {

			BigDecimal bdKurs = DelegateFactory.getInstance()
					.getLocaleDelegate()
					.getWechselkurs2(sWaehrung, sMandantWaehrung);

			WechselkursDto wDto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.getKursZuDatum(sWaehrung, sMandantWaehrung,
							wdfBelegdatum.getDate());

			if (wDto != null) {
				bdKurs = wDto.getNKurs();
			}

			if (bdKurs != null) {
				wnfKurs.setBigDecimal(bdKurs);
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								"Zwischen "
										+ sMandantWaehrung
										+ " und "
										+ sWaehrung
										+ " ist kein Kurs hinterlegt\nBitte tragen Sie diesen nach");
				// LPMain.getInstance().exitFrame(getInternalFrame());
			}
		}
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

	private void holeLieferant(Integer key) throws Throwable {
		if (key != null) {
			LieferantDto lieferantDto = DelegateFactory.getInstance()
					.getLieferantDelegate().lieferantFindByPrimaryKey(key);
			getTabbedPane().setLieferantDto(lieferantDto);

			if (lieferantDto.getKontoIIdKreditorenkonto() != null) {

				KontoDto kredKto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey(
								lieferantDto.getKontoIIdKreditorenkonto());

				wlaKreditorennummer.setText(LPMain.getInstance()
						.getTextRespectUISPr("er.kreditorennr")
						+ ": "
						+ kredKto.getCNr());
			} else {
				wlaKreditorennummer.setText(LPMain.getInstance()
						.getTextRespectUISPr("er.kreditorennr") + ":");
			}

			// fuer eine neue ER die Lieferantendaten uebernehmen
			if (getTabbedPane().getEingangsrechnungDto() == null) {
				if (zahlungszielDto == null) {
					holeZahlungsziel(getTabbedPane().getLieferantDto()
							.getZahlungszielIId());
				}
				if (lieferantDto.getWaehrungCNr() != null) {
					wtfWaehrung.setText(lieferantDto.getWaehrungCNr());
					wlaWaehrung1.setText(lieferantDto.getWaehrungCNr());
					wlaWaehrung2.setText(lieferantDto.getWaehrungCNr());
					setzeWechselkurs(lieferantDto.getWaehrungCNr());
				}
				if (lieferantDto.getMwstsatzbezIId() != null) {
					// Auf den aktuellen MWST-Satz uebersetzen.
					MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									lieferantDto.getMwstsatzbezIId());
					wcoMwst.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
				}
				if (!wcbMehrfachkontierung.isSelected()) {
					if (getTabbedPane().getLieferantDto()
							.getKontoIIdWarenkonto() != null) {
						holeKonto(getTabbedPane().getLieferantDto()
								.getKontoIIdWarenkonto());
					}
					if (kostenstelleDto == null) {
						holeKostenstelle(getTabbedPane().getLieferantDto()
								.getIIdKostenstelle());
					}
				}

				// Feld Kundendaten ist vorbesetzt mit der Kundennummer d.
				// Lieferanten
				if (getTabbedPane().getLieferantDto() != null
						&& getTabbedPane().getLieferantDto().getCKundennr() != null) {
					/**
					 * @todo 12 nicht hart codiert
					 */
					String cKnr = Helper.cutString(getTabbedPane()
							.getLieferantDto().getCKundennr(), 12);
					wtfKundendaten.setText(cKnr);
				} else {
					wtfKundendaten.setText(null);
				}
			}
		} else {
			getTabbedPane().setLieferantDto(null);
		}
		dto2ComponentsLieferant();
	}

	/**
	 * holeZahlungsziel
	 * 
	 * @param key
	 *            Integer
	 * @throws Throwable
	 */
	private void holeZahlungsziel(Integer key) throws Throwable {
		if (key != null) {
			zahlungszielDto = DelegateFactory.getInstance()
					.getMandantDelegate().zahlungszielFindByPrimaryKey(key);
		} else {
			zahlungszielDto = null;
		}
		dto2ComponentsZahlungsziel();
	}

	/**
	 * dto2ComponentsZahlungsziel
	 */
	private void dto2ComponentsZahlungsziel() {
		if (zahlungszielDto != null) {
			if (zahlungszielDto.getZahlungszielsprDto() != null) {
				wtfZahlungsziel.setText(zahlungszielDto.getZahlungszielsprDto()
						.getCBezeichnung());
			} else {
				wtfZahlungsziel.setText(zahlungszielDto.getCBez());
			}
		} else {
			wtfZahlungsziel.setText(null);
		}
	}

	private void holeBestellung(Integer key) throws Throwable {
		if (key != null) {
			bestellungDto = DelegateFactory.getInstance()
					.getBestellungDelegate().bestellungFindByPrimaryKey(key);
		} else {
			bestellungDto = null;
		}
		dto2ComponentsBestellung();
	}

	private void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	/**
	 * Dialogfenster zur Lieferantenauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryLieferant(ActionEvent e) throws Throwable {
		Integer lieferantIId = null;
		if (getTabbedPane().getLieferantDto() != null) {
			lieferantIId = getTabbedPane().getLieferantDto().getIId();
		}
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
				.createPanelFLRLieferantGoto(getInternalFrame(), lieferantIId,
						true, false);
		new DialogQuery(panelQueryFLRLieferant);
	}

	/**
	 * Dialogfenster zur Zahlungszieleauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryZahlungsziel(ActionEvent e) throws Throwable {
		Integer zahlungszielIId = null;
		if (zahlungszielDto != null) {
			zahlungszielIId = zahlungszielDto.getIId();
		}
		panelQueryFLRZahlungsziel = SystemFilterFactory
				.getInstance()
				.createPanelFLRZahlungsziel(getInternalFrame(), zahlungszielIId);
		new DialogQuery(panelQueryFLRZahlungsziel);
	}

	/**
	 * Dialogfenster zur Bestellungauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryBestellung() throws Throwable {
		Integer besIId = null;
		if (bestellungDto != null) {
			besIId = bestellungDto.getIId();
		}
		FilterKriterium[] fk;
		if (getTabbedPane().getLieferantDto() != null) {
			fk = BestellungFilterFactory.getInstance()
					.getFKBestellungenEinesLieferanten(
							getTabbedPane().getLieferantDto().getIId());
		} else {
			fk = SystemFilterFactory.getInstance().createFKMandantCNr();
		}
		panelQueryFLRBestellung = BestellungFilterFactory.getInstance()
				.createPanelFLRBestellung(getInternalFrame(), false, false, fk,
						besIId);
		new DialogQuery(panelQueryFLRBestellung);
	}

	/**
	 * initPanel
	 * 
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {

		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {

			wcoWiederholungsintervall.setMap(DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.getAuftragwiederholungsintervall(
							LPMain.getInstance().getUISprLocale()));
			// wcoWiederholungsintervall
			// .setKeyOfSelectedItem(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH);

			if (!wcoArt.isMapSet()) {
				wcoArt.setMap(DelegateFactory.getInstance()
						.getEingangsrechnungDelegate()
						.getSprEingangsrechnungartNurZusatzkosten());
			}
		} else {
			if (!wcoArt.isMapSet()) {
				wcoArt.setMap(DelegateFactory.getInstance()
						.getEingangsrechnungDelegate()
						.getAllSprEingangsrechnungarten());
			}
		}

		if (!wcoMwst.isMapSet()) {
			bMapSetAktiv = true; // Pruefung auf keineUst uebergehen
			if (getTabbedPane().getEingangsrechnungDto() != null) {
				wcoMwst.setMap(DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.getAllMwstsatz(
								LPMain.getInstance().getTheClient()
										.getMandant(),
								new Timestamp(getTabbedPane()
										.getEingangsrechnungDto()
										.getDBelegdatum().getTime())));
			} else {
				wcoMwst.setMap(DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.getAllMwstsatz(
								LPMain.getInstance().getTheClient()
										.getMandant(),
								new Timestamp(System.currentTimeMillis())));
			}
			bMapSetAktiv = false;
		}

		// rechte
		bIstModulKostenstelleInstalliert = true;
		wbuKostenstelle.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleBezeichnung.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer
				.setMandatoryField(bIstModulKostenstelleInstalliert);
	}

	private void setDefaults() throws Throwable {
		wcbMehrfachkontierung.setSelected(false);
		wcoArt.setKeyOfSelectedItem(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
		wdfBelegdatum.setDate(new java.sql.Date(System.currentTimeMillis()));
		wdfFreigabedatum.setDate(DelegateFactory.getInstance()
				.getEingangsrechnungDelegate().getDefaultFreigabeDatum());
		// mit der Mandantenwaehrung beginnen, damit ist auch der kurs definiert
		wtfWaehrung.setText(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr());
		wlaWaehrung1.setText(wtfWaehrung.getText());
		wlaWaehrung2.setText(wtfWaehrung.getText());
		setzeWechselkurs(wtfWaehrung.getText());
		nettoBetrag = null;
	}

	private void components2Dto() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto == null) {
			// eine neue wirds
			erDto = new EingangsrechnungDto();
			erDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
		}
		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			erDto.setWiederholungsintervallCNr((String) wcoWiederholungsintervall
					.getKeyOfSelectedItem());
		}
		if (bReversechargeVerwenden == true) {
			erDto.setBReversecharge(wcbReversecharge.getShort());
		} else {
			erDto.setBReversecharge(Helper.boolean2Short(false));
		}
		erDto.setBIgErwerb(wcbIGErwerb.getShort());

		// Zuordnung zu Bestellung nur bei An-/Schlusszahlung
		if (wcoArt.getKeyOfSelectedItem().equals(
				EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG)
				|| wcoArt.getKeyOfSelectedItem().equals(
						EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)
				|| wcoArt.getKeyOfSelectedItem().equals(
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
			erDto.setBestellungIId(null);
		} else {
			erDto.setBestellungIId(bestellungDto.getIId());
		}
		erDto.setCLieferantenrechnungsnummer(wtnfLieferantenrechnungsnummer
				.getText());
		erDto.setCText(wtfText.getText());
		erDto.setCWeartikel(wtfWEArtikel.getText());
		erDto.setDBelegdatum(wdfBelegdatum.getDate());
		erDto.setDFreigabedatum(wdfFreigabedatum.getDate());
		erDto.setEingangsrechnungartCNr((String) wcoArt.getKeyOfSelectedItem());

		if (!wcoMwst.getKeyOfSelectedItem().equals(MEHRFACH)) {
			Integer pkMwstsatz = (Integer) wcoMwst.getKeyOfSelectedItem();
			if (pkMwstsatz != null) {
				erDto.setMwstsatzIId(pkMwstsatz);
			}
		}
		if (kostenstelleDto != null) {
			erDto.setKostenstelleIId(kostenstelleDto.getIId());
		} else {
			erDto.setKostenstelleIId(null);
		}
		if (getTabbedPane().getLieferantDto() != null) {
			erDto.setLieferantIId(getTabbedPane().getLieferantDto().getIId());
		} else {
			erDto.setLieferantIId(null);
		}
		if (kontoDto != null) {
			erDto.setKontoIId(kontoDto.getIId());
		} else {
			erDto.setKontoIId(null);
		}
		String sMandantWaehrung = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"),
					"Beim Mandanten ist keine Standard-Waehrung hinterlegt");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		String sWaehrung = (String) wtfWaehrung.getText();
		WechselkursDto kursDto = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.getKursZuDatum(sWaehrung, sMandantWaehrung,
						erDto.getDBelegdatum());
		BigDecimal bdKurs = kursDto.getNKurs().setScale(
				LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
				BigDecimal.ROUND_HALF_EVEN);
		erDto.setNBetrag(Helper.rundeKaufmaennisch(wnfBetrag.getBigDecimal()
				.multiply(bdKurs), FinanzFac.NACHKOMMASTELLEN));
		erDto.setNBetragfw(wnfBetrag.getBigDecimal());
		erDto.setNKurs(bdKurs);
		if (wnfMwst.getBigDecimal() != null) {
			erDto.setNUstBetragfw(wnfMwst.getBigDecimal());
			erDto.setNUstBetrag(Helper.rundeKaufmaennisch(wnfMwst
					.getBigDecimal().multiply(bdKurs),
					FinanzFac.NACHKOMMASTELLEN));
		} else {
			erDto.setNUstBetrag(new BigDecimal(0));
			erDto.setNUstBetragfw(new BigDecimal(0));
		}
		erDto.setWaehrungCNr(sWaehrung);
		if (zahlungszielDto != null) {
			erDto.setZahlungszielIId(zahlungszielDto.getIId());
		} else {
			erDto.setZahlungszielIId(null);
		}
		erDto.setCKundendaten(wtfKundendaten.getText());
		getTabbedPane().setEingangsrechnungDto(erDto);
	}

	private void dto2Components() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		holeBestellung(erDto.getBestellungIId());
		holeKostenstelle(erDto.getKostenstelleIId());
		holeLieferant(erDto.getLieferantIId());
		holeZahlungsziel(erDto.getZahlungszielIId());
		holeKonto(erDto.getKontoIId());

		String text = "";
		String text2 = "";

		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {

			if (erDto.getTWiederholenderledigt() != null) {
				text = LPMain
						.getTextRespectUISPr("er.zusatzkosten.wiederholungerledigt")
						+ " "
						+ Helper.formatDatumZeit(erDto
								.getTWiederholenderledigt(), LPMain
								.getTheClient().getLocUi());
			}
			if (erDto.getPersonalIIdWiederholenderledigt() != null) {
				text += "("
						+ DelegateFactory
								.getInstance()
								.getPersonalDelegate()
								.personalFindByPrimaryKey(
										erDto.getPersonalIIdWiederholenderledigt())
								.getCKurzzeichen() + ")";
			}
		} else {
			if (erDto.getTZollimportpapier() != null) {
				text = LPMain
						.getTextRespectUISPr("er.zollimportpapiere.erhalten.persondatum")
						+ " "
						+ Helper.formatDatumZeit(erDto.getTZollimportpapier(),
								LPMain.getTheClient().getLocUi());
			}
			if (erDto.getPersonalIIdZollimportpapier() != null) {
				text += "("
						+ DelegateFactory
								.getInstance()
								.getPersonalDelegate()
								.personalFindByPrimaryKey(
										erDto.getPersonalIIdZollimportpapier())
								.getCKurzzeichen() + ")";
			}
			if (erDto.getCZollimportpapier() != null) {
				text2 = LPMain.getTextRespectUISPr("lp.zollbelegnummer") + " "
						+ erDto.getCZollimportpapier();
			}

			if (erDto.getEingangsrechnungIdZollimport() != null) {
				EingangsrechnungDto erDtoZollImport = DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey(
								erDto.getEingangsrechnungIdZollimport());
				text2 += " | "
						+ LPMain.getTextRespectUISPr("er.modulname.kurz") + " "
						+ erDtoZollImport.getCNr();
			}

		}
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
								LocaleFac.BELEGART_EINGANGSRECHNUNG,
								erDto.getIId());

				wlaFibuExportDatum.setText(LPMain
						.getTextRespectUISPr("rech.fibuexportdatum")
						+ " "
						+ Helper.formatTimestamp(
								DelegateFactory
										.getInstance()
										.getFibuExportDelegate()
										.exportlaufFindByPrimaryKey(
												exportDto.getExportlaufIId())
										.getTAendern(), LPMain.getInstance()
										.getTheClient().getLocUi()));

			} catch (Exception e) {
				// Kein Exoprt vorhanden
			}

		}

		wlaZollimportpapiere.setText(text);
		wlaWiederholendErledigt.setText(text2);

		boolean bMehrfach = erDto.getKontoIId() == null
				|| erDto.getKostenstelleIId() == null;
		wcbMehrfachkontierung.setSelected(bMehrfach);
		updateMehrfach();
		// vollstaendig kontiert?
		if (bMehrfach) {
			BigDecimal bdNochNichtKontiert = DelegateFactory.getInstance()
					.getEingangsrechnungDelegate()
					.getWertNochNichtKontiert(erDto.getIId());
			// falls noch was offen ist
			if (bdNochNichtKontiert.compareTo(new BigDecimal(0)) != 0) {
				wlaNochNichtKontiert.setVisible(true);
			} else {
				wlaNochNichtKontiert.setVisible(false);
			}
		} else {
			wlaNochNichtKontiert.setVisible(false);
		}
		this.wcbReversecharge.setShort(erDto.getBReversecharge());
		wcbIGErwerb.setShort(erDto.getBIgErwerb());
		if (!bFibuInstalliert)
			// Betrags/Ust Labels anpassen
			setIGErwerbReverseCharge();
		if (erDto.getEingangsrechnungIIdNachfolger() != null) {
			EingangsrechnungDto erDtoNachfolger = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungFindByPrimaryKey(
							erDto.getEingangsrechnungIIdNachfolger());
			wtfNachfolger.setText(erDtoNachfolger.getCNr());
		} else {
			wtfNachfolger.setText("");
		}

		wcoWiederholungsintervall.setKeyOfSelectedItem(erDto
				.getWiederholungsintervallCNr());

		wtnfLieferantenrechnungsnummer.setText(erDto
				.getCLieferantenrechnungsnummer());
		wtfText.setText(erDto.getCText());
		wtfWEArtikel.setText(erDto.getCWeartikel());
		wdfBelegdatum.setDate(erDto.getDBelegdatum());
		wdfFreigabedatum.setDate(erDto.getDFreigabedatum());
		wnfBetrag.setBigDecimal(erDto.getNBetragfw());

		wcoMwst.setKeyOfSelectedItem(erDto.getMwstsatzIId());
		wnfKurs.setBigDecimal(erDto.getNKurs());

		wnfMwst.setBigDecimal(erDto.getNUstBetragfw());
		wtfWaehrung.setText(erDto.getWaehrungCNr());

		wnfKurs.setForeground(Color.BLACK);

		BigDecimal aktuelleKurs = getWechselkurs(erDto.getWaehrungCNr());

		WechselkursDto wDto = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.getKursZuDatum(erDto.getWaehrungCNr(),
						LPMain.getTheClient().getSMandantenwaehrung(),
						wdfBelegdatum.getDate());

		if (wDto != null) {
			aktuelleKurs = wDto.getNKurs().setScale(6,
					BigDecimal.ROUND_HALF_EVEN);
		}

		if (aktuelleKurs != null) {
			if (wnfKurs.getBigDecimal().doubleValue() != aktuelleKurs
					.doubleValue()) {
				wnfKurs.setForeground(Color.RED);
			}
		}

		wlaWaehrung1.setText(erDto.getWaehrungCNr());
		wlaWaehrung2.setText(erDto.getWaehrungCNr());
		wcoArt.setKeyOfSelectedItem(erDto.getEingangsrechnungartCNr());
		wtfKundendaten.setText(erDto.getCKundendaten());
		// Statusbar fuellen
		this.setStatusbarPersonalIIdAnlegen(erDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(erDto.getTAnlegen());
		this.setStatusbarPersonalIIdAendern(erDto.getPersonalIIdAendern());
		this.setStatusbarTAendern(erDto.getTAendern());
		this.setStatusbarStatusCNr(erDto.getStatusCNr());
		String status = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_EINGANGSRECHNUNG,
						erDto.getIId());
		if (status != null) {
			status = LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status;
		}
		setStatusbarSpalte5(status);
	}

	private void dto2ComponentsLieferant() throws ExceptionLP, Throwable {
		if (getTabbedPane().getLieferantDto() != null) {
			LieferantDto lieferant = getTabbedPane().getLieferantDto();
			wbuLieferant.setOKey(lieferant.getIId());
			wtfLieferant.setText(lieferant.getPartnerDto()
					.formatFixTitelName1Name2());
			wtfAdresse.setText(lieferant.getPartnerDto().formatAdresse());
			wtfAbteilung.setText(lieferant.getPartnerDto()
					.getCName3vorname2abteilung());
			wcbReversecharge.setShort(getTabbedPane().getLieferantDto()
					.getBReversecharge());
			wcbIGErwerb.setShort(getTabbedPane().getLieferantDto()
					.getBIgErwerb());
			if (wcbReversecharge.isSelected()) {
				wcbIGErwerb.setSelected(false);
			} else {
				if (bFibuInstalliert) {
					boolean istIgErwerb = DelegateFactory
							.getInstance()
							.getFinanzServiceDelegate()
							.istIgErwerb(
									getTabbedPane().getLieferantDto()
											.getKontoIIdKreditorenkonto());
					wcbIGErwerb.setSelected(istIgErwerb);
				} else {
					wcbIGErwerb.setSelected(lieferant.getBIgErwerbBoolean());
				}
			}
			setIGErwerbReverseCharge();
		} else {
			wbuLieferant.setOKey(null);
			wtfLieferant.setText(null);
			wtfAdresse.setText(null);
			wtfAbteilung.setText(null);
			if (bFibuInstalliert)
				wcbIGErwerb.setSelected(false);
		}
	}

	private void dto2ComponentsBestellung() throws ExceptionLP, Throwable {
		if (bestellungDto != null) {
			wtfBestellung.setText(bestellungDto.getCNr());
			String art = getTabbedPane().getEingangsrechnungDto() == null ? wcoArt
					.getSelectedItem().toString() : getTabbedPane()
					.getEingangsrechnungDto().getEingangsrechnungartCNr();
			if (EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG.trim()
					.equals(art.trim())) {
				StringBuffer sb = new StringBuffer();
				wlaAnzahlungen.setVisible(true);
				wtfAnzahlungen.setVisible(true);
				EingangsrechnungDto[] dtos = DelegateFactory.getInstance()
						.getEingangsrechnungDelegate()
						.findByBestellungIId(bestellungDto.getIId());
				for (EingangsrechnungDto dto : dtos) {
					if (!dto.getEingangsrechnungartCNr().equals(
							EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG))
						continue;
					if (dto.getStatusCNr().equals(
							EingangsrechnungFac.STATUS_STORNIERT))
						continue;
					sb.append(dto.getCNr() + ", ");
				}
				wtfAnzahlungen.setText(sb.toString());
			} else {
				wlaAnzahlungen.setVisible(false);
				wtfAnzahlungen.setVisible(false);
			}
		} else {
			wtfBestellung.setText(null);
			wlaAnzahlungen.setVisible(false);
			wtfAnzahlungen.setVisible(false);
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

	/**
	 * wnfBetrag_focusLost
	 * 
	 * @throws Throwable
	 */
	void wnfBetrag_focusLost() throws Throwable {
		updateMwst();
	}

	private void updateMwst() throws ExceptionLP, Throwable {
		MwstsatzDto mwst = null;
		if (wcoMwst.getKeyOfSelectedItem() == null
				|| wcoMwst.getKeyOfSelectedItem().equals(MEHRFACH)) {
			wnfMwst.setBigDecimal(null);
		} else {
			if (wnfBetrag.getBigDecimal() == null) {
				wnfMwst.setBigDecimal(null);
			} else {
				mwst = getMwstsatzForSelected();

				if (wcbIGErwerb.isSelected() || wcbReversecharge.isSelected()) {
					if (nettoBetrag != null) {
						wnfBetrag.setBigDecimal(nettoBetrag);
					}
					// IG Erwerb und Reverse Charge rechnen netto
					wnfMwst.setBigDecimal(Helper.getProzentWert(
							wnfBetrag.getBigDecimal(),
							new BigDecimal(mwst.getFMwstsatz()),
							FinanzFac.NACHKOMMASTELLEN));
				} else {
					if (nettoBetrag != null) {
						// wenn nettoBetrag gesetzt ist, rechnen wir immer
						// von diesem ausgehend. Netto bleibt also immer gleich.
						wnfBetrag.setBigDecimal(nettoBetrag.add(Helper
								.getProzentWert(nettoBetrag, new BigDecimal(
										mwst.getFMwstsatz()),
										FinanzFac.NACHKOMMASTELLEN)));
					}
					wnfMwst.setBigDecimal(Helper.getMehrwertsteuerBetrag(
							wnfBetrag.getBigDecimal(), mwst.getFMwstsatz()
									.doubleValue()));
				}
			}
		}
	}

	private MwstsatzDto getMwstsatzForSelected() throws ExceptionLP, Throwable {
		MwstsatzDto mwst;
		mwst = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mwstsatzFindByPrimaryKey(
						(Integer) wcoMwst.getKeyOfSelectedItem());
		return mwst;
	}

	private void holeKonto(Integer key) throws Throwable {
		if (key != null) {
			this.kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey(key);
		} else {
			kontoDto = null;
		}
		dto2ComponentsKonto();
	}

	/**
	 * dto2Components
	 */
	private void dto2ComponentsKonto() {
		if (kontoDto != null) {
			wtfKontoNummer.setText(kontoDto.getKontonrBezeichnung());
		} else {
			wtfKontoNummer.setText(null);
		}
	}

	private void dialogQueryKonto(ActionEvent e) throws Throwable {
		wbuKonto.setEnabled(false);
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance()
				.createFKSachkonten();
		panelQueryFLRKonto = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"finanz.liste.sachkonten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
				.createFKVKonto();
		panelQueryFLRKonto.befuellePanelFilterkriterienDirektUndVersteckte(
				fkDirekt1, fkDirekt2, fkVersteckt);
		if (kontoDto != null) {
			panelQueryFLRKonto.setSelectedId(kontoDto.getIId());
		}
		new DialogQuery(panelQueryFLRKonto);
		wbuKonto.setEnabled(true);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (EingangsrechnungFac.STATUS_STORNIERT.equals(erDto.getStatusCNr())) {
			boolean answer = (DialogFactory
					.showMeldung(
							"Die Eingangsrechnung ist storniert\nSoll sie wieder verwendet werden?",
							LPMain.getInstance()
									.getTextRespectUISPr("lp.frage"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			}
			DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.storniereEingangsrechnungRueckgaengig(erDto.getIId());
			this.eventYouAreSelected(false);
		} else if (erDto.getStatusCNr().equals(
				EingangsrechnungFac.STATUS_TEILBEZAHLT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr("lp.hint"),
							"Es sind bereits Zahlungen auf diese Eingangsrechnung eingetragen\nNehmen Sie zuerst die Zahlungen zur\u00FCck");
			return;
		} else if (erDto.getStatusCNr().equals(
				EingangsrechnungFac.STATUS_ERLEDIGT)) {
			if (DialogFactory.showMeldung(
					LPMain.getInstance().getTextRespectUISPr(
							"er.eingangsrechnungistbereitserledigt"), LPMain
							.getInstance().getTextRespectUISPr("lp.hint"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
				DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.erledigungAufheben(erDto.getIId());
				getTabbedPane().reloadEingangsrechnungDto();
				eventYouAreSelected(false);
			}
			return;
		}
		super.eventActionUpdate(aE, false);
		setzeWechselkurs(wtfWaehrung.getText());
		updateMehrfach();

		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ANGELEGT)
				&& getTabbedPane().getEingangsrechnungDto().getTGedruckt() != null) {
			boolean bFrage = DialogFactory.showModalJaNeinDialog(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("er.gedruckt.zuruecksetzen"));
			if (bFrage == true) {
				getTabbedPane().getEingangsrechnungDto().setTGedruckt(null);
			}
		}

	}

	public Map<String, String> getMapSieheKontierung() {
		TreeMap<String, String> tm = new TreeMap<String, String>();
		tm.put(MEHRFACH,
				LPMain.getInstance().getTextRespectUISPr("er.siehekontierung"));
		return tm;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLieferant;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getTabbedPane().enablePanels();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		getTabbedPane().enablePanels();
		getTabbedPane().setWareneingangDto(null);
		getTabbedPane().setInseratIIds(null);

		nettoBetrag = null;
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getTabbedPane().print();
		eventYouAreSelected(false);
	}

	public void setMyComponents(EingangsrechnungDto eingangsrechnungDto)
			throws Throwable {
		holeBestellung(eingangsrechnungDto.getBestellungIId());
		holeKostenstelle(eingangsrechnungDto.getKostenstelleIId());
		holeLieferant(eingangsrechnungDto.getLieferantIId());
		holeZahlungsziel(eingangsrechnungDto.getZahlungszielIId());
		holeKonto(eingangsrechnungDto.getKontoIId());

		wtnfLieferantenrechnungsnummer.setText(eingangsrechnungDto
				.getCLieferantenrechnungsnummer());
		wtfText.setText(eingangsrechnungDto.getCText());
		wdfBelegdatum.setDate(eingangsrechnungDto.getDBelegdatum());
		wdfFreigabedatum.setDate(eingangsrechnungDto.getDFreigabedatum());
		// egal ob "normale" ER oder Gutschrift, hier steht immer der absolute
		// betrag
		wnfBetrag.setBigDecimal(eingangsrechnungDto.getNBetragfw().abs());

		wcoMwst.setKeyOfSelectedItem(eingangsrechnungDto.getMwstsatzIId());
		wnfKurs.setBigDecimal(eingangsrechnungDto.getNKurs());
		// egal ob "normale" ER oder Gutschrift, hier steht immer der absolute
		// betrag
		wnfMwst.setBigDecimal(eingangsrechnungDto.getNUstBetragfw().abs());
		wtfWaehrung.setText(eingangsrechnungDto.getWaehrungCNr());
		wcoArt.setKeyOfSelectedItem(eingangsrechnungDto
				.getEingangsrechnungartCNr());
		wtfKundendaten.setText(eingangsrechnungDto.getCKundendaten());
		// Statusbar fuellen
		this.setStatusbarPersonalIIdAnlegen(eingangsrechnungDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(eingangsrechnungDto.getTAnlegen());
		this.setStatusbarPersonalIIdAendern(eingangsrechnungDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(eingangsrechnungDto.getTAendern());
		this.setStatusbarStatusCNr(eingangsrechnungDto.getStatusCNr());
		String status = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_EINGANGSRECHNUNG,
						eingangsrechnungDto.getIId());
		if (status != null) {
			status = LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status;
		}
		setStatusbarSpalte5(status);
	}

	public void setNettoBetrag(BigDecimal nettoBetrag) throws ExceptionLP,
			Throwable {
		this.nettoBetrag = nettoBetrag;
		updateMwst();
		
		//SP2896
		this.nettoBetrag=null;
	}
}

class PanelEingangsrechnungKopfdaten_wnfBetrag_focusAdapter implements
		java.awt.event.FocusListener {
	private PanelEingangsrechnungKopfdaten adaptee;

	PanelEingangsrechnungKopfdaten_wnfBetrag_focusAdapter(
			PanelEingangsrechnungKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfBetrag_focusLost();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}
