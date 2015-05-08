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
package com.lp.client.anfrage;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogGeaenderteKonditionenEK;
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
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfrageerledigungsgrundDto;
import com.lp.server.anfrage.service.AnfragetextDto;
import com.lp.server.angebot.service.AngeboterledigungsgrundDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>Kopfdaten der Anfrage</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 08.06.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.16 $
 */
public class PanelAnfrageKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAnfrage intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAnfrage tpAnfrage = null;

	private JPanel jPanelWorkingOn = null;
	private Border innerBorder = null;

	// dtos in diesem Panel
	private AnsprechpartnerDto ansprechpartnerDto = null;
	private WaehrungDto waehrungDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private WaehrungDto waehrungOriDto = null;

	static final public String ACTION_SPECIAL_LIEFERANT = "action_special_lieferant";
	static final public String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT = "action_special_ansprechpartner_lieferant";
	static final public String ACTION_SPECIAL_LIEFERGRUPPE = "action_special_liefergruppe";
	static final public String ACTION_SPECIAL_KOSTENSTELLE = "action_special_kostenstelle";

	private WrapperLabel wlaAnfrageart = null;
	private WrapperComboBox wcoAnfrageart = null;
	private WrapperLabel wlaBelegdatum = null;
	private WrapperDateField wdfBelegdatum = null;

	private WrapperGotoButton jpaLieferant = null;
	private PanelQueryFLR panelQueryFLRLieferant = null;
	private WrapperTextField wtfLieferant = null;

	private WrapperTextField wtfLieferantAdresse = null;
	private WrapperLabel wlaLieferantAbteilung = null;
	private WrapperTextField wtfLieferantAbteilung = null;

	private WrapperButton wbuAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;

	private WrapperButton wbuLiefergruppe = null;
	private PanelQueryFLR panelQueryFLRLiefergruppe = null;
	private WrapperTextField wtfLiefergruppe = null;

	private WrapperLabel wlaProjekt = null;
	private WrapperTextField wtfProjekt = null;

	private WrapperLabel wlaWaehrung = null;
	private WrapperComboBox wcbWaehrung = null;

	private WrapperLabel wlaKurs = null;
	private WrapperNumberField wnfKurs = null;

	private WrapperLabel wlaAnliefertermin = null;
	private WrapperDateField wdfAnliefertermin = null;

	private WrapperButton wbuKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private WrapperTextField wtfKostenstelle = null;

	private WrapperLabel wlaBestellungen = null;
	private WrapperTextArea wtaBestellungen = null;

	private WrapperLabel wlaErzeugteAnfragen = null;
	private WrapperTextArea wtaErzeugteAnfragen = null;

	private WrapperLabel wlaLiefergruppenanfrage = null;
	private WrapperTextField wtfLiefergruppenanfrage = null;

	private WrapperSelectField wsfProjekt = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), true);

	private WrapperLabel wlaAnfrageerledigungsgrund = null;
	private WrapperTextField wtfAnfrageerledigungsgrund = null;

	private Map<?, ?> tmWaehrungen = null;

	public PanelAnfrageKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAnfrage) internalFrame;
		tpAnfrage = intFrame.getTabbedPaneAnfrage();

		jbInit();
		initPanel();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		innerBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche Buttons auf das Actionpanel setzen
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD, PanelBasis.ACTION_PRINT };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		jPanelWorkingOn.setLayout(new GridBagLayout());
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		wlaAnfrageart = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.anfrageart"));
		HelperClient.setDefaultsToComponent(wlaAnfrageart, 115);

		wcoAnfrageart = new WrapperComboBox();
		wcoAnfrageart.setMandatoryFieldDB(true);
		wcoAnfrageart.setActivatable(false);

		wlaBelegdatum = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaBelegdatum, 90);
		wlaBelegdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.belegdatum"));

		wdfBelegdatum = new WrapperDateField();
		wdfBelegdatum.setMandatoryField(true);
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

		jpaLieferant = new WrapperGotoButton(
				WrapperGotoButton.GOTO_LIEFERANT_AUSWAHL);
		jpaLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT);
		jpaLieferant.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferant"));
		jpaLieferant.addActionListener(this);

		wtfLieferant = new WrapperTextField();
		wtfLieferant.setActivatable(false);
		wtfLieferant.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfLieferantAdresse = new WrapperTextField();
		wtfLieferantAdresse.setActivatable(false);
		wtfLieferantAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaLieferantAbteilung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.abteilung"));

		wtfLieferantAbteilung = new WrapperTextField();
		wtfLieferantAbteilung.setActivatable(false);
		wtfLieferantAbteilung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner
				.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT);
		wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ansprechpartner"));
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuLiefergruppe = new WrapperButton();
		wbuLiefergruppe.setActionCommand(ACTION_SPECIAL_LIEFERGRUPPE);
		wbuLiefergruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.liefergruppe.flr"));
		wbuLiefergruppe.addActionListener(this);

		wtfLiefergruppe = new WrapperTextField();
		wtfLiefergruppe.setActivatable(false);
		wtfLiefergruppe.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaProjekt = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));
		wtfProjekt = new WrapperTextField();
		wtfProjekt.setColumnsMax(80);

		wlaWaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.waehrung"));

		wcbWaehrung = new WrapperComboBox();
		wcbWaehrung.setMandatoryFieldDB(true);

		wcbWaehrung
				.addActionListener(new PanelAnfrageKopfdaten_wcoWaehrungen_actionAdapter(
						this));

		wlaKurs = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.kurs"));

		wnfKurs = new WrapperNumberField();
		wnfKurs.setActivatable(false);
		wnfKurs.setMandatoryField(true);
		wnfKurs.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);

		wlaAnliefertermin = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.anliefertermin"));

		wdfAnliefertermin = new WrapperDateField();

		wbuKostenstelle = new WrapperButton();
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));
		wbuKostenstelle.addActionListener(this);

		wtfKostenstelle = new WrapperTextField();
		wtfKostenstelle.setActivatable(false);
		wtfKostenstelle.setMandatoryField(true);

		wlaBestellungen = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.bestellungen"));
		wtaBestellungen = new WrapperTextArea();
		wtaBestellungen.setRows(3);
		// wtaBestellungen.setColumns(20);
		wtaBestellungen.setActivatable(false);

		wlaErzeugteAnfragen = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.erzeugteanfragen"));
		wtaErzeugteAnfragen = new WrapperTextArea();
		wtaErzeugteAnfragen.setRows(3);
		// wtaErzeugteAnfragen.setColumns(20);
		wtaErzeugteAnfragen.setActivatable(false);

		wlaLiefergruppenanfrage = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.liefergruppenanfrage"));
		wtfLiefergruppenanfrage = new WrapperTextField();
		wtfLiefergruppenanfrage.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfLiefergruppenanfrage.setActivatable(false);

		wlaAnfrageerledigungsgrund = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.erledigungsgrund"));
		wtfAnfrageerledigungsgrund = new WrapperTextField();
		wtfAnfrageerledigungsgrund.setActivatable(false);

		// Zeile
		jPanelWorkingOn.add(wlaAnfrageart, new GridBagConstraints(0, iZeile, 1,
				1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 10, 2), 0, 0));
		jPanelWorkingOn.add(wcoAnfrageart, new GridBagConstraints(1, iZeile, 1,
				1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
		jPanelWorkingOn.add(wlaBelegdatum, new GridBagConstraints(2, iZeile, 1,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
		jPanelWorkingOn.add(wdfBelegdatum, new GridBagConstraints(3, iZeile, 1,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(jpaLieferant, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLieferant, new GridBagConstraints(1, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wtfLieferantAdresse, new GridBagConstraints(1,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaLieferantAbteilung, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLieferantAbteilung, new GridBagConstraints(1,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wbuLiefergruppe, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLiefergruppe, new GridBagConstraints(1, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaProjekt, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 2, 2), 0, 0));

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			jPanelWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

			jPanelWorkingOn.add(wsfProjekt.getWrapperGotoButton(),
					new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(10, 2, 2, 2), 0, 0));
			jPanelWorkingOn.add(wsfProjekt.getWrapperTextField(),
					new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(10, 2, 2, 2), 0, 0));

		} else {

			jPanelWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile,
					3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		}

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaWaehrung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbWaehrung, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaKurs, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfKurs, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaAnliefertermin, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfAnliefertermin, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKostenstelle, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaAnfrageerledigungsgrund, new GridBagConstraints(
				0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAnfrageerledigungsgrund, new GridBagConstraints(
				1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaBestellungen, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtaBestellungen, new GridBagConstraints(1, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaErzeugteAnfragen, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtaErzeugteAnfragen, new GridBagConstraints(1,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaLiefergruppenanfrage, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLiefergruppenanfrage, new GridBagConstraints(1,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	public void setDefaults() throws Throwable {
		ansprechpartnerDto = new AnsprechpartnerDto();
		waehrungDto = new WaehrungDto();
		kostenstelleDto = new KostenstelleDto();
		waehrungOriDto = new WaehrungDto();

		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		wdfBelegdatum.setDate(new Date(System.currentTimeMillis()));
		wdfAnliefertermin.setDate(null);

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
		wcbWaehrung.setSelectedItem(waehrungDto.getCNr());
		setzeWechselkurs();

		// default Kostenstelle kommt vom Benutzer
		kostenstelleDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getIDPersonal())
				.getKostenstelleDto_Stamm();
		wtfKostenstelle.setText(kostenstelleDto.getCBez());
	}

	public void initPanel() throws Throwable {
		wcoAnfrageart.setMap(DelegateFactory.getInstance()
				.getAnfrageServiceDelegate()
				.getAnfragearten(LPMain.getInstance().getUISprLocale()));
		tmWaehrungen = DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen();
		wcbWaehrung.setMap(tmWaehrungen);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferant) {
				Object iIdLieferant = ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (iIdLieferant != null) {
					if (tpAnfrage.getAnfrageDto().getWaehrungCNr() != null) {
						waehrungOriDto = DelegateFactory
								.getInstance()
								.getLocaleDelegate()
								.waehrungFindByPrimaryKey(
										tpAnfrage.getAnfrageDto()
												.getWaehrungCNr());
					}

					tpAnfrage.setLieferantDto(DelegateFactory.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey((Integer) iIdLieferant));

					wtfLieferant.setText(tpAnfrage.getLieferantDto()
							.getPartnerDto().formatTitelAnrede());
					wtfLieferantAdresse.setText(tpAnfrage.getLieferantDto()
							.getPartnerDto().formatAdresse());
					wtfLieferantAbteilung.setText(tpAnfrage.getLieferantDto()
							.getPartnerDto().getCName3vorname2abteilung());

					// den Ansprechpartner beim Lieferanten zuruecksetzen
					ansprechpartnerDto = new AnsprechpartnerDto();

					AnsprechpartnerDto anspDtoTemp = null;
					// Ansprechpartner vorbesetzen?
					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_ANFRAGE_ANSP_VORBESETZEN,
									ParameterFac.KATEGORIE_ANFRAGE,
									LPMain.getInstance().getTheClient()
											.getMandant());
					if ((Boolean) parameter.getCWertAsObject()) {
						anspDtoTemp = DelegateFactory
								.getInstance()
								.getAnsprechpartnerDelegate()
								.ansprechpartnerFindErstenEinesPartnersOhneExc(
										tpAnfrage.getLieferantDto()
												.getPartnerIId());
					}
					if (anspDtoTemp != null) {
						ansprechpartnerDto = anspDtoTemp;
						wtfAnsprechpartner.setText(ansprechpartnerDto
								.getPartnerDto().formatTitelAnrede());
					} else {
						wtfAnsprechpartner.setText("");
					}

					// default waehrung der Anfrage kommt aus dem Lieferanten
					waehrungDto = DelegateFactory
							.getInstance()
							.getLocaleDelegate()
							.waehrungFindByPrimaryKey(
									tpAnfrage.getLieferantDto()
											.getWaehrungCNr());
					wcbWaehrung.setSelectedItem(waehrungDto.getCNr());
					setzeWechselkurs();

					if (waehrungOriDto.getCNr() != null
							&& !waehrungOriDto.getCNr().equals(
									wcbWaehrung.getSelectedItem())) {
						int indexWaehrungOriCNr = 0;
						int indexWaehrungNeuCNr = 1;
						int iAnzahlOptionen = 2;

						Object[] aOptionen = new Object[iAnzahlOptionen];
						aOptionen[indexWaehrungOriCNr] = waehrungOriDto
								.getCNr();
						aOptionen[indexWaehrungNeuCNr] = wcbWaehrung
								.getSelectedItem();

						int iAuswahl = DialogFactory.showModalDialog(
								getInternalFrame(),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.waehrungunterschiedlich"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.frage"), aOptionen, aOptionen[0]);

						if (iAuswahl == indexWaehrungOriCNr) {
							// die Belegwaehrung wird beibehalten -> Waehrung
							// und Wechselkurs zuruecksetzen
							waehrungDto = waehrungOriDto;
							wcbWaehrung.setSelectedItem(waehrungDto.getCNr());
							setzeWechselkurs();
							waehrungOriDto = null;
						}
					}

					if (tpAnfrage.getLieferantDto().getIIdKostenstelle() != null) {
						kostenstelleDto = DelegateFactory
								.getInstance()
								.getSystemDelegate()
								.kostenstelleFindByPrimaryKey(
										tpAnfrage.getLieferantDto()
												.getIIdKostenstelle());

						wtfKostenstelle.setText(kostenstelleDto.getCBez());
					} else {
						// default Kostenstelle kommt vom Benutzer
						kostenstelleDto = DelegateFactory
								.getInstance()
								.getPersonalDelegate()
								.personalFindByPrimaryKey(
										LPMain.getInstance().getTheClient()
												.getIDPersonal())
								.getKostenstelleDto_Stamm();
						wtfKostenstelle.setText(kostenstelleDto.getCBez());
					}
				}
			} else if (e.getSource() == panelQueryFLRLiefergruppe) {
				Object liefergruppeIId = ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (liefergruppeIId != null) {
					tpAnfrage.setLiefergruppeDto(DelegateFactory
							.getInstance()
							.getLieferantServicesDelegate()
							.lfliefergruppeFindByPrimaryKey(
									(Integer) liefergruppeIId));

					String liefergruppeCBez = tpAnfrage.getLiefergruppeDto()
							.getCNr();
					if (tpAnfrage.getLiefergruppeDto()
							.getLfliefergruppesprDto() != null
							&& tpAnfrage.getLiefergruppeDto()
									.getLfliefergruppesprDto().getCBez() != null) {
						liefergruppeCBez = tpAnfrage.getLiefergruppeDto()
								.getLfliefergruppesprDto().getCBez();
					}

					wtfLiefergruppe.setText(liefergruppeCBez);
				}
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object iIdAnsprechpartner = ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (iIdAnsprechpartner != null) {
					ansprechpartnerDto = DelegateFactory
							.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(
									(Integer) iIdAnsprechpartner);

					wtfAnsprechpartner.setText(ansprechpartnerDto
							.getPartnerDto().formatTitelAnrede());
				} else {
					wtfAnsprechpartner.setText("");
				}
			}
			// else
			// if (e.getSource() == panelQueryFLRWaehrung) {
			// Object cNrWaehrung = ( (ISourceEvent) e.getSource()).
			// getIdSelected();
			//
			//
			// }
			else if (e.getSource() == panelQueryFLRKostenstelle) {
				Object iIdKostenstelle = ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (iIdKostenstelle != null) {
					kostenstelleDto = DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.kostenstelleFindByPrimaryKey(
									(Integer) iIdKostenstelle);

					wtfKostenstelle.setText(kostenstelleDto.getCBez());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				ansprechpartnerDto = new AnsprechpartnerDto();
				wtfAnsprechpartner.setText("");
			}
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		// dtos aus dem tabbed pane zuruecksetzen
		tpAnfrage.resetDtos();
		tpAnfrage.setTitleAnfrage(LPMain.getInstance().getTextRespectUISPr(
				"anf.anfrage"));

		// diese panel zuruecksetzen
		setDefaults();

		setzeAnfrageart(AnfrageServiceFac.ANFRAGEART_LIEFERANT);
		enableComponents();
		clearStatusbar();
	}

	public void eventActionNewLiefergruppenanfrage(EventObject eventObject,
			boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		eventActionNew(eventObject, bLockMeI, bNeedNoNewI);

		setzeAnfrageart(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT)) {
			dialogQueryLieferant();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT)) {
			dialogQueryAnsprechpartnerLieferant();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERGRUPPE)) {
			dialogQueryLiefergruppe();
		}
	}

	private void dialogQueryLieferant() throws Throwable {
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
				.createPanelFLRLieferantGoto(intFrame,
						tpAnfrage.getLieferantDto().getIId(), true, false);
		new DialogQuery(panelQueryFLRLieferant);
	}

	private void dialogQueryLiefergruppe() throws Throwable {
		panelQueryFLRLiefergruppe = PartnerFilterFactory.getInstance()
				.createPanelFLRLiefergruppeMindestensEinLieferant(intFrame,
						false, false, tpAnfrage.getLiefergruppeDto().getIId()); // UW->JO,
																				// //beim
																				// sort()
																				// seltsame
																				// Hibernate
																				// Exception
		new DialogQuery(panelQueryFLRLiefergruppe);
	}

	private void dialogQueryAnsprechpartnerLieferant() throws Throwable {
		if (tpAnfrage.getLieferantDto().getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.lieferantnichtgewaehlt"));
		} else {
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(
							intFrame,
							tpAnfrage.getLieferantDto().getPartnerDto()
									.getIId(),
							tpAnfrage.getAnfrageDto()
									.getAnsprechpartnerIIdLieferant(), true,
							true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
		}
	}

	private void dialogQueryKostenstelle() throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false,
						kostenstelleDto.getIId());
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANFRAGE;
	}

	private void dto2Components() throws Throwable {
		if (tpAnfrage.pruefeAktuelleAnfrage()) {
			setzeAnfrageart(tpAnfrage.getAnfrageDto().getArtCNr());
			// Goto Lieferant Key setzen
			jpaLieferant.setOKey(tpAnfrage.getAnfrageDto()
					.getLieferantIIdAnfrageadresse());
			wdfBelegdatum.setDate(tpAnfrage.getAnfrageDto().getTBelegdatum());

			wtfProjekt.setText(tpAnfrage.getAnfrageDto().getCBez());
			wcbWaehrung.setSelectedItem(tpAnfrage.getAnfrageDto()
					.getWaehrungCNr());
			wnfKurs.setDouble(tpAnfrage.getAnfrageDto()
					.getFWechselkursmandantwaehrungzubelegwaehrung());

			wdfAnliefertermin.setDate(tpAnfrage.getAnfrageDto()
					.getTAnliefertermin());

			kostenstelleDto = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.kostenstelleFindByPrimaryKey(
							tpAnfrage.getAnfrageDto().getKostenstelleIId());
			wtfKostenstelle.setText(kostenstelleDto.getCBez());

			// zu dieser Anfrage erfasste Bestellungen anzeigen
			BestellungDto[] aBestellungDto = DelegateFactory
					.getInstance()
					.getBestellungDelegate()
					.bestellungFindByAnfrage(tpAnfrage.getAnfrageDto().getIId());
			wtaBestellungen.setText(formatBestellungen(aBestellungDto));

			// erzeugte Anfragen zu einer Liefergruppenanfrage anzeigen
			if (tpAnfrage.getAnfrageDto().getArtCNr()
					.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)
					&& tpAnfrage.getAnfrageDto().getIId() != null) {
				AnfrageDto[] aAnfrageDto = DelegateFactory
						.getInstance()
						.getAnfrageDelegate()
						.anfrageFindByAnfrageIIdLiefergruppenanfrage(
								tpAnfrage.getAnfrageDto().getIId());
				wtaErzeugteAnfragen.setText(formatAnfragen(aAnfrageDto));
			}

			if (tpAnfrage.getAnfrageDto().getAnfrageIIdLiefergruppenanfrage() != null) {
				AnfrageDto liefergruppenanfrageDto = DelegateFactory
						.getInstance()
						.getAnfrageDelegate()
						.anfrageFindByPrimaryKey(
								tpAnfrage.getAnfrageDto()
										.getAnfrageIIdLiefergruppenanfrage());
				wtfLiefergruppenanfrage.setText(liefergruppenanfrageDto
						.getCNr());
			}

			wsfProjekt.setKey(tpAnfrage.getAnfrageDto().getProjektIId());

			if (tpAnfrage.getAnfrageDto().getAnfrageerledigungsgrundIId() != null) {

				AnfrageerledigungsgrundDto grundDto = DelegateFactory
						.getInstance()
						.getAnfrageServiceDelegate()
						.anfrageerledigungsgrundFindByPrimaryKey(
								tpAnfrage.getAnfrageDto()
										.getAnfrageerledigungsgrundIId());

				wtfAnfrageerledigungsgrund.setText(grundDto.getCBez());
			} else {
				wtfAnfrageerledigungsgrund.setText(null);
			}

			aktualisiereStatusbar();
		}
	}

	private String formatBestellungen(BestellungDto[] aBestellungDtoI) {
		String cFormat = "";

		if (aBestellungDtoI != null && aBestellungDtoI.length > 0) {
			for (int i = 0; i < aBestellungDtoI.length; i++) {
				cFormat += aBestellungDtoI[i].getCNr();
				cFormat += " | ";
			}
		}

		if (cFormat.length() > 3) {
			cFormat = cFormat.substring(0, cFormat.length() - 3);
		}

		return cFormat;
	}

	private String formatAnfragen(AnfrageDto[] aAnfrageDtoI) {
		String cFormat = "";

		if (aAnfrageDtoI != null && aAnfrageDtoI.length > 0) {
			for (int i = 0; i < aAnfrageDtoI.length; i++) {
				cFormat += aAnfrageDtoI[i].getCNr();
				cFormat += " | ";
			}
		}

		if (cFormat.length() > 3) {
			cFormat = cFormat.substring(0, cFormat.length() - 3);
		}

		return cFormat;
	}

	private void components2Dto() throws Throwable {
		if (tpAnfrage.getAnfrageDto() == null
				|| tpAnfrage.getAnfrageDto().getIId() == null) {
			tpAnfrage.setAnfrageDto(new AnfrageDto());

			tpAnfrage.getAnfrageDto().setMandantCNr(
					LPMain.getInstance().getTheClient().getMandant());
			tpAnfrage.getAnfrageDto().setArtCNr(
					(String) wcoAnfrageart.getKeyOfSelectedItem());
			tpAnfrage.getAnfrageDto().setStatusCNr(
					AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT);
			tpAnfrage.getAnfrageDto()
					.setBelegartCNr(LocaleFac.BELEGART_ANFRAGE);
			tpAnfrage.getAnfrageDto().setFAllgemeinerRabattsatz(new Double(0));

			// Konditionen vorbelegen
			if (tpAnfrage.getAnfrageDto().getArtCNr()
					.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
				if (tpAnfrage.getLieferantDto().getNRabatt() != null) {
					tpAnfrage.getAnfrageDto().setFAllgemeinerRabattsatz(
							tpAnfrage.getLieferantDto().getNRabatt());
				}
				if (tpAnfrage.getLieferantDto().getLieferartIId() != null) {
					tpAnfrage.getAnfrageDto().setLieferartIId(
							tpAnfrage.getLieferantDto().getLieferartIId());
				}
				if (tpAnfrage.getLieferantDto().getZahlungszielIId() != null) {
					tpAnfrage.getAnfrageDto().setZahlungszielIId(
							tpAnfrage.getLieferantDto().getZahlungszielIId());
				}

				if (tpAnfrage.getLieferantDto().getIdSpediteur() != null) {
					tpAnfrage.getAnfrageDto().setSpediteurIId(
							tpAnfrage.getLieferantDto().getIdSpediteur());
				}
			}

			tpAnfrage.getAnfrageDto().setNTransportkosteninanfragewaehrung(
					new BigDecimal(0));

			// speziell in ANF: die Anfragetexte in Kopfdaten vorbelegen
			String localeCNr = LPMain.getInstance().getTheClient()
					.getLocUiAsString();

			if (wcoAnfrageart.getKeyOfSelectedItem().equals(
					AnfrageServiceFac.ANFRAGEART_LIEFERANT)
					&& tpAnfrage.getLieferantDto().getPartnerDto()
							.getLocaleCNrKommunikation() != null) {
				localeCNr = tpAnfrage.getLieferantDto().getPartnerDto()
						.getLocaleCNrKommunikation();
			}

			AnfragetextDto anfragetextDto = DelegateFactory
					.getInstance()
					.getAnfrageServiceDelegate()
					.anfragetextFindByMandantLocaleCNr(localeCNr,
							MediaFac.MEDIAART_KOPFTEXT);

			tpAnfrage.getAnfrageDto().setBelegtextIIdKopftext(
					anfragetextDto.getIId());

			anfragetextDto = DelegateFactory
					.getInstance()
					.getAnfrageServiceDelegate()
					.anfragetextFindByMandantLocaleCNr(localeCNr,
							MediaFac.MEDIAART_FUSSTEXT);

			tpAnfrage.getAnfrageDto().setBelegtextIIdFusstext(
					anfragetextDto.getIId());

		}
		tpAnfrage.getAnfrageDto().setProjektIId((Integer) wsfProjekt.getOKey());
		tpAnfrage.getAnfrageDto().setTBelegdatum(wdfBelegdatum.getTimestamp());

		if (tpAnfrage.getAnfrageDto().getArtCNr()
				.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
			tpAnfrage.getAnfrageDto().setLieferantIIdAnfrageadresse(
					tpAnfrage.getLieferantDto().getIId());
			tpAnfrage.getAnfrageDto().setAnsprechpartnerIIdLieferant(
					ansprechpartnerDto.getIId());
		} else if (tpAnfrage.getAnfrageDto().getArtCNr()
				.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
			tpAnfrage.getAnfrageDto().setLiefergruppeIId(
					tpAnfrage.getLiefergruppeDto().getIId());
		}

		tpAnfrage.getAnfrageDto().setCBez(wtfProjekt.getText());
		tpAnfrage.getAnfrageDto().setWaehrungCNr(
				(String) wcbWaehrung.getSelectedItem());
		tpAnfrage.getAnfrageDto()
				.setFWechselkursmandantwaehrungzubelegwaehrung(
						wnfKurs.getDouble());
		tpAnfrage.getAnfrageDto().setTAnliefertermin(
				wdfAnliefertermin.getTimestamp());
		tpAnfrage.getAnfrageDto().setKostenstelleIId(kostenstelleDto.getIId());
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAnfrage.getAnfrageDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAnfrage.getAnfrageDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAnfrage.getAnfrageDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAnfrage.getAnfrageDto().getTAendern());
		setStatusbarStatusCNr(tpAnfrage.getAnfrageStatus());
		String status = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_ANFRAGE,
						tpAnfrage.getAnfrageDto().getIId());
		if (status != null) {
			setStatusbarSpalte5(LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status);
		}
	}

	public void setzeWechselkurs() throws Throwable {
		if (waehrungDto != null && waehrungDto.getCNr() != null) {
			try {
				wnfKurs.setBigDecimal(DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.getWechselkurs2(
								DelegateFactory
										.getInstance()
										.getMandantDelegate()
										.mandantFindByPrimaryKey(
												LPMain.getInstance()
														.getTheClient()
														.getMandant())
										.getWaehrungCNr(), waehrungDto.getCNr()));
			} catch (Throwable t) {
				handleException(t, true); // UW: muss bleiben
				wnfKurs.setBigDecimal(null); // wnfKurs ist mandatory!
				eventActionUnlock(null);
				getInternalFrame().enableAllPanelsExcept(true);
				tpAnfrage.gotoAuswahl();
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false); // Stati aller Components
											// aktualisieren

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpAnfrage.getAnfrageDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		// einen bestehenden Auftrag anzeigen
		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {

			tpAnfrage.setAnfrageDto(DelegateFactory.getInstance()
					.getAnfrageDelegate()
					.anfrageFindByPrimaryKey((Integer) oKey));

			if (tpAnfrage.getAnfrageDto().getLieferantIIdAnfrageadresse() != null) {
				tpAnfrage.setLieferantDto(DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								tpAnfrage.getAnfrageDto()
										.getLieferantIIdAnfrageadresse()));
			}

			dto2Components();

			tpAnfrage.setTitleAnfrage(LPMain.getInstance().getTextRespectUISPr(
					"anf.panel.kopfdaten"));
		}

		// statuskopfdaten: 1
		tpAnfrage.getAnfrageKopfdaten().updateButtons(
				getLockedstateDetailMainKey());
		enableComponents(); // unbedingt nach .updateButtons aufrufen :-)
		tpAnfrage.enableLieferdaten();
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (tpAnfrage.getAnfrageDto().getIId() == null) {
				Integer iIdAnfrage = DelegateFactory.getInstance()
						.getAnfrageDelegate()
						.createAnfrage(tpAnfrage.getAnfrageDto());

				tpAnfrage.setAnfrageDto(DelegateFactory.getInstance()
						.getAnfrageDelegate()
						.anfrageFindByPrimaryKey(iIdAnfrage));

				setKeyWhenDetailPanel(iIdAnfrage);
				if (tpAnfrage.getAnfrageDto().getLieferantIIdAnfrageadresse() != null) {
					DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.pruefeLieferant(
									tpAnfrage.getAnfrageDto()
											.getLieferantIIdAnfrageadresse(),
									LocaleFac.BELEGART_ANFRAGE,
									getInternalFrame());
				}
			} else {
				boolean bUpdate = true;

				if (tpAnfrage.getAnfrageDto().getStatusCNr()
						.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
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

				// PJ18030
				AnfrageDto aDtoVorhanden = DelegateFactory
						.getInstance()
						.getAnfrageDelegate()
						.anfrageFindByPrimaryKey(
								tpAnfrage.getAnfrageDto().getIId());
				if (aDtoVorhanden.getLieferantIIdAnfrageadresse() != null
						&& !aDtoVorhanden.getLieferantIIdAnfrageadresse()
								.equals(tpAnfrage.getAnfrageDto()
										.getLieferantIIdAnfrageadresse())) {

					DialogGeaenderteKonditionenEK dialog = new DialogGeaenderteKonditionenEK(
							tpAnfrage.getAnfrageDto(), tpAnfrage
									.getAnfrageDto()
									.getLieferantIIdAnfrageadresse(),
							getInternalFrame());
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(dialog);

					if (dialog.bKonditionenUnterschiedlich == true) {
						dialog.setVisible(true);

						if (dialog.bAbgebrochen == false) {
							tpAnfrage.setAnfrageDto((AnfrageDto) dialog
									.getBelegDto());
						} else {
							bUpdate = false;
						}
					}

				}

				if (bUpdate) {
					DelegateFactory
							.getInstance()
							.getAnfrageDelegate()
							.updateAnfrage(
									tpAnfrage.getAnfrageDto(),
									true,
									waehrungOriDto == null ? null
											: waehrungOriDto.getCNr());
				}
			}
			setKeyWhenDetailPanel(tpAnfrage.getAnfrageDto().getIId());
			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (!isLockedDlg()) {
			DelegateFactory.getInstance().getAnfrageDelegate()
					.storniereAnfrage(tpAnfrage.getAnfrageDto().getIId());
			super.eventActionDelete(e, false, false); // der auftrag existiert
														// weiterhin!
			eventYouAreSelected(false);
		}
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		switch (exfc.getICode()) {
		case EJBExceptionLP.FEHLER_KEIN_PARTNER_GEWAEHLT:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.lieferantnichtgewaehlt"));
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

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpAnfrage.printAnfrage();
		eventYouAreSelected(false);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAnfrage.aktualisiereAnfragestatusDurchButtonUpdate()) {
			super.eventActionUpdate(aE, false);

			enableComponents();
		}
	}

	private void setzeAnfrageart(String cNrAnfrageartI) throws Throwable {
		tpAnfrage.getAnfrageDto().setArtCNr(cNrAnfrageartI);
		wcoAnfrageart.setKeyOfSelectedItem(tpAnfrage.getAnfrageDto()
				.getArtCNr());

		if (cNrAnfrageartI.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
			jpaLieferant.setEnabled(true);
			wbuAnsprechpartner.setEnabled(true);
			wbuLiefergruppe.setEnabled(false);

			if (tpAnfrage.getLieferantDto() != null) {
				wtfLieferant.setText(tpAnfrage.getLieferantDto()
						.getPartnerDto().formatTitelAnrede());
				wtfLieferantAdresse.setText(tpAnfrage.getLieferantDto()
						.getPartnerDto().formatAdresse());
				wtfLieferantAbteilung.setText(tpAnfrage.getLieferantDto()
						.getPartnerDto().getCName3vorname2abteilung());
			}

			if (tpAnfrage.getAnfrageDto().getAnsprechpartnerIIdLieferant() != null) {
				ansprechpartnerDto = DelegateFactory
						.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(
								tpAnfrage.getAnfrageDto()
										.getAnsprechpartnerIIdLieferant());

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
						.formatTitelAnrede());
			} else {
				wtfAnsprechpartner.setText("");
			}

			tpAnfrage.setLiefergruppeDto(new LfliefergruppeDto());
			wtfLiefergruppe.setText("");
		} else if (cNrAnfrageartI
				.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
			jpaLieferant.setEnabled(false);
			wbuAnsprechpartner.setEnabled(false);
			wbuLiefergruppe.setEnabled(true);

			tpAnfrage.setLieferantDto(new LieferantDto());
			wtfLieferant.setText("");
			wtfLieferantAdresse.setText("");
			wtfLieferantAbteilung.setText("");

			ansprechpartnerDto = new AnsprechpartnerDto();
			wtfAnsprechpartner.setText("");

			if (tpAnfrage.getLiefergruppeDto() != null) {
				String liefergruppeCBez = tpAnfrage.getLiefergruppeDto()
						.getCNr();
				if (tpAnfrage.getLiefergruppeDto().getLfliefergruppesprDto() != null
						&& tpAnfrage.getLiefergruppeDto()
								.getLfliefergruppesprDto().getCBez() != null) {
					liefergruppeCBez = tpAnfrage.getLiefergruppeDto()
							.getLfliefergruppesprDto().getCBez();
				}

				wtfLiefergruppe.setText(liefergruppeCBez);
			}
		}
	}

	// Components enable in den Anfrage Kopfdaten.
	// Es gilt: Anfrageart Lieferant oder Liefergruppe, wird durch den New
	// Button in der Auswahl festgelegt.
	// Wann sind welche Buttons enabled?
	// - Bei einer neuen Anfrage:
	// * bei Anfrageart Lieferant:
	// Enabled Lieferant, Ansprechpartner, Waehrung, Kostenstelle
	// Disabled Liefergruppe
	// * bei Anfrageart Liefergruppe:
	// Enabled Liefergruppe, Waehrung, Kostenstelle
	// Disabled Lieferant, Ansprechpartner
	// - Bei einer bestehenden Anfrage bei Status (Angelegt || Offen) && durch
	// den Benutzer selbst gelockt:
	// * bei Anfrageart Lieferant:
	// Wenn es noch keine mengenbehafteten Positionen gibt:
	// Enabled Lieferant, Ansprechpartner, Waehrung, Kostenstelle
	// Disabled Liefergruppe
	// Wenn es mengenbehafteten Positionen gibt:
	// Enabled Ansprechpartner, Kostenstelle
	// Disabled Lieferant, Waehrung, Liefergruppe
	// * bei Anfrageart Liefergruppe:
	// Enabled Liefergruppe, Waehrung, Kostenstelle
	// Disabled Lieferant, Ansprechpartner

	/**
	 * Components enable in den Kopfdaten. Siehe obige Beschreibung.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private void enableComponents() throws Throwable {
		if (tpAnfrage.getAnfrageDto() != null) {
			if (tpAnfrage.getAnfrageDto().getIId() == null) { // bei einer neuen
																// Anfrage
				if (tpAnfrage.getAnfrageDto().getArtCNr()
						.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
					jpaLieferant.setEnabled(true);
					wtfLieferant.setMandatoryField(true);
					wbuAnsprechpartner.setEnabled(true);
					wcbWaehrung.setEnabled(true);
					wbuKostenstelle.setEnabled(true);

					wbuLiefergruppe.setEnabled(false);
					wtfLiefergruppe.setMandatoryField(false);

					setVisibleLieferantenanfrage(true);
				} else if (tpAnfrage.getAnfrageDto().getArtCNr()
						.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
					wbuLiefergruppe.setEnabled(true);
					wtfLiefergruppe.setMandatoryField(true);
					wcbWaehrung.setEnabled(true);
					wbuKostenstelle.setEnabled(true);

					jpaLieferant.setEnabled(false);
					wtfLieferant.setMandatoryField(false);
					wbuAnsprechpartner.setEnabled(false);

					setVisibleLieferantenanfrage(false);
				}
			} else { // bei einer bestehenden Anfrage
				if ((tpAnfrage.getAnfrageDto().getStatusCNr()
						.equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT) || tpAnfrage
						.getAnfrageDto().getStatusCNr()
						.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN))
						&& DelegateFactory
								.getInstance()
								.getAnfragepositionDelegate()
								.getAnzahlMengenbehafteteAnfragepositionen(
										tpAnfrage.getAnfrageDto().getIId()) <= 0
						&& super.getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {
					if (tpAnfrage.getAnfrageDto().getArtCNr()
							.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
						jpaLieferant.setEnabled(true);
						wtfLieferant.setMandatoryField(true);
						wbuAnsprechpartner.setEnabled(true);
						wcbWaehrung.setEnabled(true);
						wbuKostenstelle.setEnabled(true);

						wbuLiefergruppe.setEnabled(false);
						wtfLiefergruppe.setMandatoryField(false);

						setVisibleLieferantenanfrage(true);
					} else if (tpAnfrage.getAnfrageDto().getArtCNr()
							.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
						wbuLiefergruppe.setEnabled(true);
						wtfLiefergruppe.setMandatoryField(true);
						wcbWaehrung.setEnabled(true);
						wbuKostenstelle.setEnabled(true);

						jpaLieferant.setEnabled(false);
						wtfLieferant.setMandatoryField(false);
						wbuAnsprechpartner.setEnabled(false);

						setVisibleLieferantenanfrage(false);
					}
				} else if ((tpAnfrage.getAnfrageDto().getStatusCNr()
						.equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT) || tpAnfrage
						.getAnfrageDto().getStatusCNr()
						.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN))
						&& DelegateFactory
								.getInstance()
								.getAnfragepositionDelegate()
								.getAnzahlMengenbehafteteAnfragepositionen(
										tpAnfrage.getAnfrageDto().getIId()) > 0
						&& super.getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {
					if (tpAnfrage.getAnfrageDto().getArtCNr()
							.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
						wbuAnsprechpartner.setEnabled(true);
						wbuKostenstelle.setEnabled(true);

						if (tpAnfrage
								.getAnfrageDto()
								.getStatusCNr()
								.equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)) {
							jpaLieferant.setEnabled(true);
						} else {
							jpaLieferant.setEnabled(false);
						}

						wtfLieferant.setMandatoryField(false);
						wbuLiefergruppe.setEnabled(false);
						wtfLiefergruppe.setMandatoryField(false);
						wcbWaehrung.setEnabled(false);

						setVisibleLieferantenanfrage(true);
					} else if (tpAnfrage.getAnfrageDto().getArtCNr()
							.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
						wbuLiefergruppe.setEnabled(true);
						wtfLiefergruppe.setMandatoryField(true);
						wbuKostenstelle.setEnabled(true);

						jpaLieferant.setEnabled(false);
						wtfLieferant.setMandatoryField(false);
						wbuAnsprechpartner.setEnabled(false);
						wcbWaehrung.setEnabled(false);

						setVisibleLieferantenanfrage(false);
					}
				} else {
					// die Anfrage ist nicht gelockt und/oder
					// befindet sich auch nicht im passenden Status und/oder
					// hat mengenbehaftete Positionen
					jpaLieferant.setEnabled(false);
					wbuAnsprechpartner.setEnabled(false);
					wbuLiefergruppe.setEnabled(false);
					wcbWaehrung.setEnabled(false);
					wbuKostenstelle.setEnabled(false);

					if (tpAnfrage.getAnfrageDto().getArtCNr()
							.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
						setVisibleLieferantenanfrage(true);
					} else if (tpAnfrage.getAnfrageDto().getArtCNr()
							.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
						setVisibleLieferantenanfrage(false);
					}
				}
			}
		}
	}

	private void setVisibleErfassteBestellungen(boolean bVisible) {
		wlaBestellungen.setVisible(bVisible);
		wtaBestellungen.setVisible(bVisible);
	}

	private void setVisibleErzeugteAnfragen(boolean bVisible) {
		wlaErzeugteAnfragen.setVisible(bVisible);
		wtaErzeugteAnfragen.setVisible(bVisible);
	}

	private void setVisibleLiefergruppenanfrage(boolean bVisible) {
		wlaLiefergruppenanfrage.setVisible(bVisible);
		wtfLiefergruppenanfrage.setVisible(bVisible);
	}

	private void setVisibleLieferantenanfrage(boolean bVisible) {
		setVisibleErfassteBestellungen(bVisible); // true
		setVisibleErzeugteAnfragen(!bVisible); // false

		if (tpAnfrage.getAnfrageDto().getAnfrageIIdLiefergruppenanfrage() != null) {
			setVisibleLiefergruppenanfrage(true);
		} else {
			setVisibleLiefergruppenanfrage(false);
		}
	}

	// statuskopfdaten: 0 getLockedstateDetailMainKey ueberschreiben
	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (tpAnfrage.getAnfrageDto().getIId() != null) {
			if (tpAnfrage.getAnfrageDto().getStatusCNr()
					.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)
					&& tpAnfrage.getAnfrageDto().getArtCNr()
							.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY);
			} else if (tpAnfrage.getAnfrageDto().getStatusCNr()
					.equals(AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY);
			} else if (tpAnfrage.getAnfrageDto().getStatusCNr()
					.equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			} else if (tpAnfrage.getAnfrageDto().getStatusCNr()
					.equals(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
				if (tpAnfrage.getAnfrageDto().getArtCNr()
						.equals(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
					lockStateValue = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				} else {
					lockStateValue = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY);
				}
			} else if (tpAnfrage.getAnfrageDto().getStatusCNr()
					.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)
					&& tpAnfrage.getAnfrageDto().getCAngebotnummer() != null) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	// statuskopfdaten: X
	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		tpAnfrage.enableLieferdaten();
	}

	public void setDefaultsAusProjekt(Integer projektIId) throws Throwable {
		ProjektDto projektDto = DelegateFactory.getInstance()
				.getProjektDelegate().projektFindByPrimaryKey(projektIId);

		wtfProjekt.setText(projektDto.getCTitel());
		wsfProjekt.setKey(projektDto.getIId());

	}

	// statuskopfdaten: X
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		tpAnfrage.enableLieferdaten();
	}

	public WaehrungDto getWaehrungDto() {
		return waehrungDto;
	}

	public void setWaehrungDto(WaehrungDto waehrungDto) {
		this.waehrungDto = waehrungDto;
	}

	public WrapperComboBox getWcbWaehrung() {
		return wcbWaehrung;
	}

	public void setWcbWaehrung(WrapperComboBox wcbWaehrung) {
		this.wcbWaehrung = wcbWaehrung;
	}

}

class PanelAnfrageKopfdaten_wcoWaehrungen_actionAdapter implements
		java.awt.event.ActionListener {

	private PanelAnfrageKopfdaten adaptee = null;

	public PanelAnfrageKopfdaten_wcoWaehrungen_actionAdapter(
			PanelAnfrageKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			Object cNrWaehrung = adaptee.getWcbWaehrung().getSelectedItem();
			if (cNrWaehrung != null) {
				WaehrungDto waehrungDto = adaptee.getWaehrungDto();
				waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
						.waehrungFindByPrimaryKey((String) cNrWaehrung);
				adaptee.setWaehrungDto(waehrungDto);
				// wcbWaehrung.setSelectedItem(waehrungDto.getCNr());

				adaptee.setzeWechselkurs();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
