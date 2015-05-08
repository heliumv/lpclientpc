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
package com.lp.client.projekt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.angebotstkl.InternalFrameAngebotstkl;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDokumentenablage;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperEmailField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMediaControl;
import com.lp.client.frame.component.WrapperMediaControlTexteingabe;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/31 14:24:31 $
 */
public class PanelProjektKopfdaten extends PanelBasis implements
		PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	private static final ImageIcon DOKUMENTE = HelperClient
			.createImageIcon("document_attachment_green16x16.png");
	private static final ImageIcon KEINE_DOKUMENTE = HelperClient
			.createImageIcon("document_attachment16x16.png");
	private JButton jbDokumente;

	private boolean bAufInternErledigteBuchen = true;

	private InternalFrameProjekt intFrame = null;
	private TabbedPaneProjekt tpProjekt = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;

	private WrapperLabel wlfKategorie;
	private WrapperComboBox wcoKategorie;
	private WrapperComboBox wcoBereich;

	private WrapperTextField wtfTitel;
	private WrapperLabel wlfTitel;

	private WrapperButton wbuPersonalZugewiesener = null;
	private WrapperTextField wtfPersonalZugewiesener = null;
	private PanelQueryFLR panelQueryFLRPersonalZugewiesener = null;

	private WrapperButton wbuPersonalErzeuger = null;
	private WrapperTextField wtfPersonalErzeuger = null;
	private PanelQueryFLR panelQueryFLRPersonalErzeuger = null;

	private WrapperGotoButton wbuPartner = new WrapperGotoButton(
			WrapperGotoButton.GOTO_PARTNER_AUSWAHL);
	private WrapperTextField wtfPartner = null;
	private PanelQueryFLR panelQueryFLRPartner = null;
	protected WrapperEmailField wtfEmail = null;

	private WrapperButton wbuAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private static final String ACTION_SPECIAL_PERSONAL_ZUGEWIESENER = "action_special_personal_zugewiesener";
	private static final String ACTION_SPECIAL_PERSONAL_ERZEUGER = "action_special_personal_erzeuger";
	private static final String ACTION_SPECIAL_PARTNER = "action_special_partner";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER = "action_special_ansprechpartner";

	public final static String MY_OWN_NEW_DOKUMENTENABLAGE = PanelBasis.LEAVEALONE
			+ "DOKUMENTENABLAGE";

	public final static String MY_OWN_NEW_TOGGLE_INTERN_ERLEDIGT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_INTERN_ERLEDIGT";

	public final static String MY_OWN_NEW_ANGEBOT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_ANGEBOT";
	public final static String MY_OWN_NEW_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_AUFTRAG";
	
	public final static String MY_OWN_NEW_AGSTKL = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_AGSTKL";

	private WrapperLabel wlfTyp;
	private WrapperComboBox wcoTyp;
	private WrapperLabel wlfPrio;
	private WrapperComboBox wcoPrio;
	private WrapperLabel wlfStatus;
	private WrapperComboBox wcoStatus;

	private WrapperLabel wlaLkzPlzOrt = new WrapperLabel();
	private JLabel wlaInternErledigt = new JLabel();

	private WrapperDateField wdfLiefertermin = null;
	private WrapperLabel wlaLiefertermin = null;

	private WrapperDateField wdfErledigt = null;
	private WrapperLabel wlaErledigt = null;

	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperEditorField wefText = null;

	private WrapperTelefonField wtfDurchwahl = null;
	private WrapperTelefonField wtfHandy = null;

	// private WrapperLabel wlaBild = new WrapperLabel();
	private WrapperMediaControl wmcBild = null;

	private WrapperCheckBox wcbVerrechenbar = null;
	private WrapperCheckBox wcbFreigegeben = null;

	private WrapperLabel wlaDauer = null;
	private WrapperNumberField wnfDauer = null;

	private WrapperLabel wlaWahrscheinlichkeit = null;
	private WrapperNumberField wnfWahrscheinlichkeit = null;

	private WrapperLabel wlaUmsatzGeplant = null;
	private WrapperNumberField wnfUmsatzGeplant = null;

	private WrapperTimeField wtfZeit = null;

	private AnsprechpartnerDto ansprechpartnerDtoVorbesetzen = null;
	private PersonalDto personalDtoZugewiesenerVorbesetzen = null;
	private PartnerDto partnerDtoVorbesetzen = null;

	private WrapperTextField wtfBuildNumber;
	private WrapperTextField wtfDeployNumber;

	private WrapperSelectField wsfNachfolger = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), true);

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK des Projektes
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelProjektKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);
		intFrame = (InternalFrameProjekt) internalFrame;
		tpProjekt = intFrame.getTabbedPaneProjekt();

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_PROJEKT,
						ParameterFac.PARAMETER_INTERN_ERLEDIGT_BEBUCHBAR);
		bAufInternErledigteBuchen = ((Boolean) parameter.getCWertAsObject());

		jbInit();
		initPanel();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD, PanelBasis.ACTION_PRINT };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		getInternalFrame().addItemChangedListener(this);

		wlfKategorie = new WrapperLabel(
				LPMain.getTextRespectUISPr("proj.projekt.label.kategorie"));
		wcoKategorie = new WrapperComboBox();
		wcoKategorie.setMandatoryField(true);

		wcoBereich = new WrapperComboBox();
		wcoBereich.setMandatoryField(true);

		wlfTitel = new WrapperLabel(
				LPMain.getTextRespectUISPr("proj.projekt.label.titel"));
		wtfTitel = new WrapperTextField();
		wtfTitel.setMandatoryField(true);
		wtfTitel.setColumnsMax(120);

		wtfHandy = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfHandy.setActivatable(false);
		wtfDurchwahl = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfDurchwahl.setActivatable(false);

		wbuPersonalZugewiesener = new WrapperButton();
		wbuPersonalZugewiesener.setText(LPMain
				.getTextRespectUISPr("button.mitarbeiter"));
		wbuPersonalZugewiesener.setToolTipText(LPMain
				.getTextRespectUISPr("button.mitarbeiter.tooltip"));
		wbuPersonalZugewiesener
				.setActionCommand(ACTION_SPECIAL_PERSONAL_ZUGEWIESENER);
		wbuPersonalZugewiesener.addActionListener(this);

		wtfEmail = new WrapperEmailField();

		wtfBuildNumber = new WrapperTextField(10);
		wtfDeployNumber = new WrapperTextField(10);

		wtfPersonalZugewiesener = new WrapperTextField();
		wtfPersonalZugewiesener.setMandatoryField(true);
		wtfPersonalZugewiesener.setActivatable(false);

		wbuPersonalErzeuger = new WrapperButton();
		wbuPersonalErzeuger.setText(LPMain
				.getTextRespectUISPr("button.erzeuger"));
		wbuPersonalErzeuger.setToolTipText(LPMain
				.getTextRespectUISPr("button.erzeuger.tooltip"));
		wbuPersonalErzeuger.setActionCommand(ACTION_SPECIAL_PERSONAL_ERZEUGER);
		wbuPersonalErzeuger.addActionListener(this);

		wtfPersonalErzeuger = new WrapperTextField();
		wtfPersonalErzeuger.setMandatoryField(true);
		wtfPersonalErzeuger.setActivatable(false);

		wbuPartner.setText(LPMain.getTextRespectUISPr("button.partner"));
		wbuPartner.setToolTipText(LPMain
				.getTextRespectUISPr("button.partner.tooltip"));

		wbuPartner.setActionCommand(ACTION_SPECIAL_PARTNER);
		wbuPartner.addActionListener(this);

		wtfPartner = new WrapperTextField();
		wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfPartner.setMandatoryField(true);
		wtfPartner.setActivatable(false);

		wbuAnsprechpartner = new WrapperButton();

		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setToolTipText(LPMain
				.getTextRespectUISPr("button.ansprechpartner.tooltip"));

		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);

		wlfTyp = new WrapperLabel(
				LPMain.getTextRespectUISPr("proj.projekt.label.typ"));
		wcoTyp = new WrapperComboBox();
		wcoTyp.setMandatoryField(true);
		wlfPrio = new WrapperLabel(
				LPMain.getTextRespectUISPr("proj.projekt.label.prio"));
		wcoPrio = new WrapperComboBox();
		wcoPrio.setMandatoryField(true);
		LinkedHashMap<Integer, Integer> hm = new LinkedHashMap<Integer, Integer>();
		hm.put(1, 1);
		hm.put(2, 2);
		hm.put(3, 3);
		hm.put(4, 4);
		hm.put(5, 5);
		wcoPrio.setMap(hm);

		wlfStatus = new WrapperLabel(LPMain.getTextRespectUISPr("lp.status"));
		wcoStatus = new WrapperComboBox();
		wcoStatus.setMandatoryField(true);

		wlaLkzPlzOrt.setHorizontalAlignment(SwingConstants.LEFT);

		Date datCurrentDate = new Date(System.currentTimeMillis());
		wlaLiefertermin = new WrapperLabel();
		wlaLiefertermin.setText(LPMain.getTextRespectUISPr("proj.zeiltermin"));

		wlaDauer = new WrapperLabel();
		wlaDauer.setText(LPMain.getTextRespectUISPr("proj.schaetzung"));
		wnfDauer = new WrapperNumberField();
		wnfDauer.setFractionDigits(2);

		wlaWahrscheinlichkeit = new WrapperLabel();
		wlaWahrscheinlichkeit.setText(LPMain
				.getTextRespectUISPr("lp.wahrscheinlichkeit") + " (%)");
		wnfWahrscheinlichkeit = new WrapperNumberField();
		wnfWahrscheinlichkeit.setFractionDigits(0);
		wnfWahrscheinlichkeit.setMaximumValue(100);
		wnfWahrscheinlichkeit.setMinimumValue(0);

		wlaUmsatzGeplant = new WrapperLabel();
		wlaUmsatzGeplant.setText(LPMain
				.getTextRespectUISPr("proj.umsatzgeplant"));
		wnfUmsatzGeplant = new WrapperNumberField();
		wnfUmsatzGeplant.setFractionDigits(2);

		wlaDauer = new WrapperLabel();
		wlaDauer.setText(LPMain.getTextRespectUISPr("proj.schaetzung"));
		wnfDauer = new WrapperNumberField();
		wnfDauer.setFractionDigits(2);

		wtfZeit = new WrapperTimeField();

		wdfLiefertermin = new WrapperDateField();
		wdfLiefertermin.setMandatoryField(true);

		wdfLiefertermin.setDate(datCurrentDate);
		wdfLiefertermin.setMinimumValue(datCurrentDate);
		wdfLiefertermin.getDisplay().addPropertyChangeListener(this);

		wefText = new WrapperEditorFieldTexteingabe(getInternalFrame(),
				LPMain.getTextRespectUISPr("label.text"));
		wefText.getLpEditor().getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT_WENN_NTEXT;

		wefText.setMandatoryField(true); // Projekt 3330
		wlaText.setText(LPMain.getTextRespectUISPr("label.text"));

		wtfEmail.setActivatable(false);

		wmcBild = new WrapperMediaControlTexteingabe(getInternalFrame(), "",
				true);

		wcbVerrechenbar = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("proj.label.verrechenbar"));
		wcbFreigegeben = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("proj.label.freigegeben"));

		wdfErledigt = new WrapperDateField();
		wdfErledigt.setActivatable(false);
		wdfErledigt.getCalendarButton().setVisible(false);
		wlaErledigt = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.erledigt"));

		jPanelWorkingOn = new JPanel(
				new MigLayout("wrap 7, hidemode 2",
						"[15%,fill|15%,fill|10%,fill|10%,fill|15%,fill|20%,fill|15%,fill]"));

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wlfKategorie);
		jPanelWorkingOn.add(wcoKategorie, "span 3");
		jPanelWorkingOn.add(wcoBereich);
		jPanelWorkingOn.add(wcbVerrechenbar);
		jPanelWorkingOn.add(wcbFreigegeben, "wrap");

		jPanelWorkingOn.add(wlfTitel);
		jPanelWorkingOn.add(wtfTitel, "span");

		wsfNachfolger.setText(LPMain
				.getTextRespectUISPr("proj.nachfolgeprojekt") + "...");
		jPanelWorkingOn.add(wlfTyp);
		jPanelWorkingOn.add(wcoTyp, "span 3");
		jPanelWorkingOn.add(wsfNachfolger.getWrapperGotoButton());
		jPanelWorkingOn.add(wsfNachfolger.getWrapperTextField(), "span");

		jPanelWorkingOn.add(wbuPersonalErzeuger);
		jPanelWorkingOn.add(wtfPersonalErzeuger, "span 3");
		jPanelWorkingOn.add(wbuPersonalZugewiesener);
		jPanelWorkingOn.add(wtfPersonalZugewiesener, "span");

		jPanelWorkingOn.add(wbuPartner);
		jPanelWorkingOn.add(wtfPartner, "span 3");
		jPanelWorkingOn.add(wtfDurchwahl);
		jPanelWorkingOn.add(wlaUmsatzGeplant);
		jPanelWorkingOn.add(wnfUmsatzGeplant, "wrap");

		jPanelWorkingOn.add(wbuAnsprechpartner);
		jPanelWorkingOn.add(wtfAnsprechpartner, "span 3");
		jPanelWorkingOn.add(wtfHandy);
		jPanelWorkingOn.add(wlaWahrscheinlichkeit);
		jPanelWorkingOn.add(wnfWahrscheinlichkeit, "wrap");

		jPanelWorkingOn.add(wlaLkzPlzOrt, "skip, span 3");
		jPanelWorkingOn.add(wtfEmail, "span");

		jPanelWorkingOn.add(wlfPrio);
		jPanelWorkingOn.add(wcoPrio);
		jPanelWorkingOn.add(wlfStatus, "skip");
		jPanelWorkingOn.add(wcoStatus);
		jPanelWorkingOn.add(wlaDauer);
		jPanelWorkingOn.add(wnfDauer, "wrap");

		jPanelWorkingOn.add(wlaLiefertermin);
		jPanelWorkingOn.add(wdfLiefertermin);
		jPanelWorkingOn.add(wtfZeit, "split 2, span 2, growx");
		jPanelWorkingOn.add(wlaErledigt, "growx 50");
		jPanelWorkingOn.add(wdfErledigt);

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_PROJEKT,
						ParameterFac.PARAMETER_BUILD_ANZEIGEN);
		boolean bBuildAnzeigen = ((Boolean) parameter.getCWertAsObject());

		if (bBuildAnzeigen) {
			jPanelWorkingOn.add(new WrapperLabel(LPMain
					.getTextRespectUISPr("proj.deploy")), "split, span");
			jPanelWorkingOn.add(wtfDeployNumber);
			jPanelWorkingOn.add(new WrapperLabel(LPMain
					.getTextRespectUISPr("proj.build")));
			jPanelWorkingOn.add(wtfBuildNumber);
		}

		jPanelWorkingOn.add(wefText, "newline, span, pushy");

		jPanelWorkingOn.add(wmcBild, "span, w :0:");

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)) {
				getToolBar().addButtonLeft(
						"/com/lp/client/res/presentation_chart16x16.png",
						LPMain.getTextRespectUISPr("proj.neues.angebot"),
						MY_OWN_NEW_ANGEBOT, null, null);
			}

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {
				getToolBar().addButtonLeft(
						"/com/lp/client/res/auftrag16x16.png",
						LPMain.getTextRespectUISPr("proj.neues.auftrag"),
						MY_OWN_NEW_AUFTRAG, null, null);
			}
			
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AGSTUECKLISTE)) {
				getToolBar().addButtonLeft(
						"/com/lp/client/res/note_add16x16.png",
						LPMain.getTextRespectUISPr("proj.neue.agstkl"),
						MY_OWN_NEW_AGSTKL, null, null);
			}
		}

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)
				&& LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG)) {
			boolean hatRecht = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);
			if (hatRecht) {
				getToolBar().addButtonCenter("/com/lp/client/res/check2.png",
						LPMain.getTextRespectUISPr("proj.internerledigen"),
						MY_OWN_NEW_TOGGLE_INTERN_ERLEDIGT, null, null);
				getToolBar().getToolsPanelCenter().add(wlaInternErledigt);

				ParametermandantDto parameterVB = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
				boolean bVonBisErfassung = (java.lang.Boolean) parameterVB
						.getCWertAsObject();

				// SP2352
				if (bVonBisErfassung == false) {

					getToolBar().addButtonRight(
							"/com/lp/client/res/gear_run.png",
							LPMain.getTextRespectUISPr("proj.startzeit"),
							Desktop.MY_OWN_NEW_ZEIT_START, null, null);
					getToolBar().addButtonRight(
							"/com/lp/client/res/gear_stop.png",
							LPMain.getTextRespectUISPr("proj.stopzeit"),
							Desktop.MY_OWN_NEW_ZEIT_STOP, null, null);
				}

				enableToolsPanelButtons(true, Desktop.MY_OWN_NEW_ZEIT_START,
						Desktop.MY_OWN_NEW_ZEIT_STOP);

			}
		}
		getToolBar().addButtonRight(
				"/com/lp/client/res/document_attachment16x16.png",
				LPMain.getTextRespectUISPr("lp.dokumentablage"),
				MY_OWN_NEW_DOKUMENTENABLAGE, null, null);
		jbDokumente = getHmOfButtons().get(MY_OWN_NEW_DOKUMENTENABLAGE)
				.getButton();

	}

	private PanelQueryFLR panelQueryFLRProjekterledigungsgrund = null;

	public void dialogQueryErledigungsgrundFromListe() throws Throwable {
		panelQueryFLRProjekterledigungsgrund = ProjektFilterFactory
				.getInstance().createPanelFLRProjekterledigungsgrund(
						getInternalFrame(), false, false);
		new DialogQuery(panelQueryFLRProjekterledigungsgrund);
	}

	public void propertyChange(PropertyChangeEvent evt) {
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_PROJEKT;
	}

	public void setDefaults() throws Throwable {

		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);
		// alle vorbelegten werte setzen
		// alle vorbelegten werte setzen
		wcoPrio.setKeyOfSelectedItem(3);
		wmcBild.setOMediaImage(null);
		wmcBild.getWcoArt().setKeyOfSelectedItem(
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);

		// den Vorschlagswert fuer die Verdichtungstage bestimmen
		ParametermandantDto parameterZielTermin = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_PROJEKT,
						ParameterFac.PARAMETER_PROJEKT_OFFSET_ZIELTERMIN);
		int defaultZielTermin = ((Integer) parameterZielTermin
				.getCWertAsObject()).intValue();

		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, defaultZielTermin);
		java.sql.Date currentDateZielTermin = new java.sql.Date(
				calendar.getTimeInMillis());

		wdfLiefertermin.setDate(currentDateZielTermin);
		wcbVerrechenbar.setShort(Helper.boolean2Short(false));
		wcbFreigegeben.setShort(Helper.boolean2Short(false));
		tpProjekt.refreshMenuAnsicht(false);
		// Status auf angelegt setzen
		wcoStatus
				.setKeyOfSelectedItem(ProjektServiceFac.PROJEKT_STATUS_ANGELEGT);
	}

	private void dto2ComponentsErzeuger(PersonalDto personalErzeugerDto) {
		wtfPersonalErzeuger.setText(personalErzeugerDto.getPartnerDto()
				.formatTitelAnrede());
	}

	private void initPanel() throws Throwable {
		wcoKategorie.setMap(DelegateFactory.getInstance()
				.getProjektServiceDelegate().getKategorie());
		wcoBereich.setMap(DelegateFactory.getInstance()
				.getProjektServiceDelegate().getAllBereich());
		wcoTyp.setMap(DelegateFactory.getInstance().getProjektServiceDelegate()
				.getTyp());
		wcoStatus.setMap(DelegateFactory.getInstance()
				.getProjektServiceDelegate()
				.getProjektStatus(LPMain.getInstance().getUISprLocale()));
	}

	private void dto2Components() throws Throwable {

		wcoKategorie.setKeyOfSelectedItem(tpProjekt.getProjektDto()
				.getKategorieCNr());
		wcoTyp.setKeyOfSelectedItem(tpProjekt.getProjektDto()
				.getProjekttypCNr());
		wcoBereich.setKeyOfSelectedItem(tpProjekt.getProjektDto()
				.getBereichIId());
		wcoStatus
				.setKeyOfSelectedItem(tpProjekt.getProjektDto().getStatusCNr());
		wcoPrio.setKeyOfSelectedItem(tpProjekt.getProjektDto().getIPrio());
		wtfTitel.setText(tpProjekt.getProjektDto().getCTitel());
		dto2ComponentsZugewiesener(tpProjekt.getPersonalZugewiesenerDto());
		dto2ComponentsErzeuger(tpProjekt.getPersonalErzeugerDto());
		dto2ComponentsPartner(tpProjekt.getPartnerDto());
		dto2ComponentsAnsprechpartner(tpProjekt.getAnsprechpartnerDto());
		wtfEmail.setEmail(null, null);

		AnsprechpartnerDto anspDto = null;
		if (tpProjekt.getAnsprechpartnerDto() != null
				&& tpProjekt.getAnsprechpartnerDto().getIId() != null) {
			anspDto = tpProjekt.getAnsprechpartnerDto();
		}

		if (tpProjekt.getAnsprechpartnerDto() != null
				&& tpProjekt.getAnsprechpartnerDto().getCEmail() != null) {
			wtfEmail.setEmail(tpProjekt.getAnsprechpartnerDto().getCEmail(),
					anspDto);
		} else {
			if (tpProjekt.getPartnerDto() != null
					&& tpProjekt.getPartnerDto().getCEmail() != null) {
				wtfEmail.setEmail(tpProjekt.getPartnerDto().getCEmail(),
						anspDto);
			}

		}

		String text = "";

		if (tpProjekt.getProjektDto().getTInternerledigt() != null) {
			text = LPMain.getTextRespectUISPr("proj.internerledigt")
					+ " "
					+ Helper.formatDatumZeit(tpProjekt.getProjektDto()
							.getTInternerledigt(), LPMain.getTheClient()
							.getLocUi());
		}
		if (tpProjekt.getProjektDto().getPersonalIIdInternerledigt() != null) {
			text += "("
					+ DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(
									tpProjekt.getProjektDto()
											.getPersonalIIdInternerledigt())
							.getCKurzzeichen() + ")";
		}

		wlaInternErledigt.setText(text);
//		wlaInternErledigt.setCutOffEnd(false);

		if (tpProjekt.getProjektDto().getXFreetext() != null) {
			wefText.setText(tpProjekt.getProjektDto().getXFreetext());
		}
		if (tpProjekt.getProjektDto().getOAttachments() != null) {
			wmcBild.setMimeType(tpProjekt.getProjektDto().getCAttachmentsType());
			wmcBild.setDateiname(tpProjekt.getProjektDto().getCDateiname());
			wmcBild.setOMediaImage(tpProjekt.getProjektDto().getOAttachments());
		} else {
			wmcBild.setOMediaImage(null);
		}

		wdfLiefertermin.setTimestamp(tpProjekt.getProjektDto()
				.getTZielwunschdatum());
		wdfErledigt.setTimestamp(tpProjekt.getProjektDto().getTErledigt());
		wcbVerrechenbar.setShort(tpProjekt.getProjektDto().getBVerrechenbar());
		wcbFreigegeben.setShort(tpProjekt.getProjektDto().getBFreigegeben());

		wsfNachfolger.setKey(tpProjekt.getProjektDto()
				.getProjektIIdNachfolger());

		ArrayList<String> s = DelegateFactory.getInstance()
				.getProjektDelegate()
				.getVorgaengerProjekte(tpProjekt.getProjektDto().getIId());

		StringBuffer str = new StringBuffer("Vorg\u00E4nger: ");
		for (int i = 0; i < s.size(); i++) {
			str.append(s.get(i));
			str.append(", ");
		}

		wsfNachfolger.setToolTipText(str.toString());

		wnfWahrscheinlichkeit.setInteger(tpProjekt.getProjektDto()
				.getIWahrscheinlichkeit());
		wnfUmsatzGeplant.setBigDecimal(tpProjekt.getProjektDto()
				.getNUmsatzgeplant());

		//
		if (tpProjekt.getProjektDto().getDDauer() != null) {
			wnfDauer.setDouble(tpProjekt.getProjektDto().getDDauer());
		}
		//
		if (tpProjekt.getProjektDto().getTZeit() != null) {
			java.sql.Time time = new Time(tpProjekt.getProjektDto().getTZeit()
					.getTime());
			wtfZeit.setTime(time);
		}
		wtfDeployNumber.setText(tpProjekt.getProjektDto().getDeployNumber());
		wtfBuildNumber.setText(tpProjekt.getProjektDto().getBuildNumber());
		PrintInfoDto values = DelegateFactory
				.getInstance()
				.getJCRDocDelegate()
				.getPathAndPartnerAndTable(tpProjekt.getProjektDto().getIId(),
						QueryParameters.UC_ID_PROJEKT);
		
		JCRRepoInfo repoInfo = new JCRRepoInfo() ;
//		boolean hasFiles = false;
		if (values != null && values.getDocPath() != null) {
			repoInfo = DelegateFactory.getInstance()
					.getJCRDocDelegate().checkIfNodeExists(values.getDocPath()) ;
			enableToolsPanelButtons(repoInfo.isOnline(), MY_OWN_NEW_DOKUMENTENABLAGE);
//				boolean online = DelegateFactory.getInstance()
//						.getJCRDocDelegate().isOnline();
//				enableToolsPanelButtons(online, MY_OWN_NEW_DOKUMENTENABLAGE);
//				if (online) {
//					hasFiles = DelegateFactory.getInstance()
//							.getJCRDocDelegate()
//							.checkIfNodeExists(values.getDocPath());
//				}
//			}
		}
		jbDokumente.setIcon(repoInfo.isExists() ? DOKUMENTE : KEINE_DOKUMENTE);

	}

	private void dto2ComponentsPartner(PartnerDto partnerDto) {
		if (partnerDto != null) {
			wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
			wbuPartner.setOKey(partnerDto.getIId());
			LandplzortDto landplzortDto = partnerDto.getLandplzortDto();
			String lKZ = null;
			if (landplzortDto != null) {
				lKZ = landplzortDto.formatLandPlzOrt();
			}
			wlaLkzPlzOrt.setText(lKZ);
		} else {
			wtfPartner.setText(null);
			wlaLkzPlzOrt.setText(null);
		}
	}

	private void dto2ComponentsZugewiesener(PersonalDto personalDto) {
		if (personalDto != null) {
			wtfPersonalZugewiesener.setText(personalDto.getPartnerDto()
					.formatTitelAnrede());
		} else {
			wtfPersonalZugewiesener.setText(null);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		tpProjekt.resetDtos();
		setDefaults();
		clearStatusbar();

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (tpProjekt.getProjektDto().getIId() == null) {
				// neues Projekt
				Integer projektIId = DelegateFactory.getInstance()
						.getProjektDelegate()
						.createProjekt(tpProjekt.getProjektDto());

				tpProjekt.setProjektDto(DelegateFactory.getInstance()
						.getProjektDelegate()
						.projektFindByPrimaryKey(projektIId));
				setKeyWhenDetailPanel(projektIId);
				// Filter loeschen und refreshen, damit ich nacher richtig
				// stehe.
				// String sOldKey = getInternalFrame().getKeyWasForLockMe();
				tpProjekt.getProjektAuswahl().clearDirektFilter();
				// tpProjekt.getProjektAuswahl().eventYouAreSelected(false);
				tpProjekt.getProjektAuswahl().setSelectedId(projektIId);
				// getInternalFrame().setKeyWasForLockMe(sOldKey);
				// Projekt 2839: Mitarbeiter und Partner aus dem letzten
				// erfassten Projekt vorbesetzen.
				// das merk ich mir hier.
				ansprechpartnerDtoVorbesetzen = tpProjekt
						.getAnsprechpartnerDto();
				personalDtoZugewiesenerVorbesetzen = tpProjekt
						.getPersonalZugewiesenerDto();
				partnerDtoVorbesetzen = tpProjekt.getPartnerDto();
			} else {
				// das aktuelle Projekt wird veraendert
				DelegateFactory.getInstance().getProjektDelegate()
						.updateProjekt(tpProjekt.getProjektDto());
			}
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		ProjektDto dto = tpProjekt.getProjektDto();
		DelegateFactory.getInstance().getProjektDelegate().removeProjekt(dto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, bAdministrateLockKeyI, false);
	}

	/**
	 * components2Dto
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {

		if (DelegateFactory.getInstance().getProjektServiceDelegate()
				.sindErledigugnsgruendeVorhanden()) {

			if (((String) wcoStatus.getKeyOfSelectedItem())
					.equals(ProjektServiceFac.PROJEKT_STATUS_ERLEDIGT)
					&& !ProjektServiceFac.PROJEKT_STATUS_ERLEDIGT
							.equals(tpProjekt.getProjektDto().getStatusCNr())) {

				dialogQueryErledigungsgrundFromListe();

				if (tpProjekt.getProjektDto().getProjekterledigungsgrundIId() == null) {
					return;
				}

			}
		}
		tpProjekt.getProjektDto().setMandantCNr(
				LPMain.getTheClient().getMandant());
		tpProjekt.getProjektDto().setBereichIId(
				(Integer) wcoBereich.getKeyOfSelectedItem());
		tpProjekt.getProjektDto().setKategorieCNr(
				(String) wcoKategorie.getKeyOfSelectedItem());

		String statusCNr = (String) wcoStatus.getKeyOfSelectedItem();
		tpProjekt.getProjektDto().setStatusCNr(statusCNr);
		
		tpProjekt.getProjektDto().setCTitel(wtfTitel.getText());

		tpProjekt.getProjektDto().setIWahrscheinlichkeit(
				wnfWahrscheinlichkeit.getInteger());
		tpProjekt.getProjektDto().setNUmsatzgeplant(
				wnfUmsatzGeplant.getBigDecimal());

		tpProjekt.getProjektDto().setProjektIIdNachfolger(
				wsfNachfolger.getIKey());

		tpProjekt.getProjektDto().setProjekttypCNr(
				(String) wcoTyp.getKeyOfSelectedItem());
		tpProjekt.getProjektDto().setPersonalIIdErzeuger(
				tpProjekt.getPersonalErzeugerDto().getIId());
		if (tpProjekt.getPersonalZugewiesenerDto() != null) {
			tpProjekt.getProjektDto().setPersonalIIdZugewiesener(
					tpProjekt.getPersonalZugewiesenerDto().getIId());
		}
		tpProjekt.getProjektDto().setIPrio(
				(Integer) wcoPrio.getKeyOfSelectedItem());
		tpProjekt.getProjektDto().setPartnerIId(
				tpProjekt.getPartnerDto().getIId());
		tpProjekt.getProjektDto().setTZielwunschdatum(
				wdfLiefertermin.getTimestamp());
		tpProjekt.getProjektDto().setXFreetext(wefText.getText());

		tpProjekt.getProjektDto().setDeployNumber(wtfDeployNumber.getText());
		tpProjekt.getProjektDto().setBuildNumber(wtfBuildNumber.getText());
		if (wmcBild.getOMediaImage() != null) {
			tpProjekt.getProjektDto()
					.setCAttachmentsType(wmcBild.getMimeType());
			tpProjekt.getProjektDto().setCDateiname(wmcBild.getDateiname());
			tpProjekt.getProjektDto().setOAttachments(wmcBild.getOMediaImage());
		} else {
			tpProjekt.getProjektDto().setCAttachmentsType(null);
			tpProjekt.getProjektDto().setOAttachments(null);
		}
		tpProjekt.getProjektDto().setBVerrechenbar(wcbVerrechenbar.getShort());
		tpProjekt.getProjektDto().setBFreigegeben(wcbFreigegeben.getShort());
		//
		java.sql.Timestamp tsZeit = wdfLiefertermin.getTimestamp();
		tsZeit = Helper.cutTimestamp(tsZeit);
		tsZeit.setTime(tsZeit.getTime() + wtfZeit.getTime().getTime() + 3600000);
		tpProjekt.getProjektDto().setTZeit(tsZeit);
		if (wnfDauer.getDouble() != null) {
			tpProjekt.getProjektDto().setDDauer(wnfDauer.getDouble());
		} else {
			tpProjekt.getProjektDto().setDDauer(0.0);
		}
		if (tpProjekt.getAnsprechpartnerDto() != null) {
			tpProjekt.getProjektDto().setAnsprechpartnerIId(
					tpProjekt.getAnsprechpartnerDto().getIId());
		}

	}

	/**
	 * Das Projekt drucken.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpProjekt.printProjekt();
		eventYouAreSelected(false);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object oKey = tpProjekt.getProjektDto().getIId();
		setDefaults();
		if (oKey == null) {
			// Neues Projekt anlegen.
			dto2ComponentsPartner(partnerDtoVorbesetzen);
			if (partnerDtoVorbesetzen != null) {
				tpProjekt.setPartnerDto(partnerDtoVorbesetzen);
			}
			dto2ComponentsZugewiesener(personalDtoZugewiesenerVorbesetzen);
			if (personalDtoZugewiesenerVorbesetzen != null) {
				tpProjekt
						.setPersonalZugewiesenerDto(personalDtoZugewiesenerVorbesetzen);
			}
			dto2ComponentsAnsprechpartner(ansprechpartnerDtoVorbesetzen);
			if (ansprechpartnerDtoVorbesetzen != null) {
				tpProjekt.setAnsprechpartnerDto(ansprechpartnerDtoVorbesetzen);
			}
			// Erzeuger ist per Default der Benutzer.
			PersonalDto personalErzeugerDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							LPMain.getTheClient().getIDPersonal());
			dto2ComponentsErzeuger(personalErzeugerDto);
			tpProjekt.setPersonalErzeugerDto(personalErzeugerDto);

			wcoBereich.setKeyOfSelectedItem((Integer) intFrame
					.getTabbedPaneProjekt().getProjektAuswahl()
					.getKeyOfFilterComboBox());

		} else if (!oKey.equals(LPMain.getLockMeForNew())) {
			tpProjekt.setProjektDto(DelegateFactory.getInstance()
					.getProjektDelegate()
					.projektFindByPrimaryKey((Integer) oKey));

			dto2Components();
			tpProjekt.setTitleProjekt(LPMain
					.getTextRespectUISPr("lp.kopfdaten"));
		}
		tpProjekt.getProjektKopfdaten().updateButtons(
				getLockedstateDetailMainKey());
		if (oKey == null) {
			wcoBereich.setEnabled(true);
		}

		aktualisiereStatusbar();
		zeitbuchungDeaktivierenWennNoetig();
	}

	private void zeitbuchungDeaktivierenWennNoetig() {
		if ((tpProjekt.getProjektDto() != null
				&& tpProjekt.getProjektDto().getStatusCNr() != null && tpProjekt
				.getProjektDto().getStatusCNr()
				.equals(ProjektServiceFac.PROJEKT_STATUS_STORNIERT))
				|| (tpProjekt.getProjektDto() != null
						&& tpProjekt.getProjektDto().getTInternerledigt() != null && bAufInternErledigteBuchen == false)) {
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

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		zeitbuchungDeaktivierenWennNoetig();

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// koennte mein lokaler FLR
			if (e.getSource() == panelQueryFLRPersonalZugewiesener) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					dto2ComponentsZugewiesener(personalDto);
					tpProjekt.setPersonalZugewiesenerDto(personalDto);
				}
			} else if (e.getSource() == panelQueryFLRProjekterledigungsgrund) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				tpProjekt.getProjektDto().setProjekterledigungsgrundIId(
						(Integer) key);
			} else if (e.getSource() == panelQueryFLRPersonalErzeuger) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					dto2ComponentsErzeuger(personalDto);
					tpProjekt.setPersonalErzeugerDto(personalDto);
				}
			} else if (e.getSource() == panelQueryFLRPartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PartnerDto partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey((Integer) key);

					String gesperrterMandant = DelegateFactory
							.getInstance()
							.getProjektDelegate()
							.istPartnerBeiEinemMandantenGesperrt(
									partnerDto.getIId());

					if (gesperrterMandant != null) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.info"),
										LPMain.getTextRespectUISPr("projekt.partnerauswahl.kundegesperrt")
												+ " " + gesperrterMandant);
					}

					KundeDto kdDto = DelegateFactory
							.getInstance()
							.getKundeDelegate()
							.kundeFindByiIdPartnercNrMandantOhneExc(
									partnerDto.getIId(),
									LPMain.getTheClient().getMandant());

					// PJ18400
					if (kdDto != null) {
						DelegateFactory.getInstance().getKundeDelegate()
								.pruefeKreditlimit(kdDto.getIId());
					}

					dto2ComponentsPartner(partnerDto);
					tpProjekt.setPartnerDto(partnerDto);
					// den Ansprechpartner beim Partner zuruecksetzen
					
				
					
					
					AnsprechpartnerDto anspDtoTemp =null;
					
					
					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_PROJEKT_ANSPRECHPARTNER_VORBESETZEN,
									ParameterFac.KATEGORIE_PROJEKT,
									LPMain.getTheClient().getMandant());
					if ((Boolean) parameter.getCWertAsObject()) {
						anspDtoTemp = DelegateFactory
								.getInstance()
								.getAnsprechpartnerDelegate()
								.ansprechpartnerFindErstenEinesPartnersOhneExc(
										tpProjekt.getPartnerDto().getIId());
					}
					
					if (anspDtoTemp != null) {
						
						
						
						tpProjekt.setAnsprechpartnerDto(anspDtoTemp);
					} else {
						tpProjekt
								.setAnsprechpartnerDto(new AnsprechpartnerDto());
					}
					dto2ComponentsAnsprechpartner(tpProjekt
							.getAnsprechpartnerDto());
				}
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
							.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey((Integer) key);
					wtfAnsprechpartner.setText(ansprechpartnerDto
							.getPartnerDto().formatAnrede());
					tpProjekt.setAnsprechpartnerDto(ansprechpartnerDto);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				tpProjekt.setAnsprechpartnerDto(new AnsprechpartnerDto());
				wtfAnsprechpartner.setText(null);
			}
		}
	}

	private void dto2ComponentsAnsprechpartner(
			AnsprechpartnerDto ansprechpartnerDto) throws Throwable {

		partnerDtoVorbesetzen = tpProjekt.getPartnerDto();

		PartnerDto dto = partnerDtoVorbesetzen;

		if (ansprechpartnerDto != null) {
			if (ansprechpartnerDto.getCTelefon() != null) {

				wtfDurchwahl.setPartnerKommunikationDto(
						tpProjekt.getPartnerDto(), null);

				wtfDurchwahl.setTextDurchwahl(DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.enrichNumber(dto.getIId(),
								PartnerFac.KOMMUNIKATIONSART_TELEFON,
								ansprechpartnerDto.getCTelefon(), false));

			} else {
				wtfDurchwahl.setPartnerKommunikationDto(tpProjekt
						.getPartnerDto(), tpProjekt.getPartnerDto()
						.getCTelefon());
			}
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatTitelAnrede());

			if (ansprechpartnerDto.getCHandy() != null) {
				wtfHandy.setPartnerKommunikationDto(
						ansprechpartnerDto.getPartnerDto(),
						ansprechpartnerDto.getCHandy());
			} else {
				wtfHandy.setPartnerKommunikationDto(null, null);
			}

		} else {
			wtfAnsprechpartner.setText(null);

		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_PERSONAL_ZUGEWIESENER)) {
			panelQueryFLRPersonalZugewiesener = PersonalFilterFactory
					.getInstance().createPanelFLRPersonal(getInternalFrame(),
							true, false,
							tpProjekt.getPersonalZugewiesenerDto().getIId());

			new DialogQuery(panelQueryFLRPersonalZugewiesener);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_PERSONAL_ERZEUGER)) {
			panelQueryFLRPersonalErzeuger = PersonalFilterFactory.getInstance()
					.createPanelFLRPersonal(getInternalFrame(), true, false,
							tpProjekt.getPersonalErzeugerDto().getIId());
			new DialogQuery(panelQueryFLRPersonalErzeuger);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER)) {
			panelQueryFLRPartner = PartnerFilterFactory.getInstance()
					.createPanelFLRPartner(getInternalFrame(),
							tpProjekt.getPartnerDto().getIId(), false);
			new DialogQuery(panelQueryFLRPartner);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER)) {
			if (tpProjekt.getPartnerDto().getIId() != null) {
				Integer selectedAnsprechpartner = null;
				if (tpProjekt.getAnsprechpartnerDto() != null) {
					selectedAnsprechpartner = tpProjekt.getAnsprechpartnerDto()
							.getIId();
				}
				panelQueryFLRAnsprechpartner = PartnerFilterFactory
						.getInstance().createPanelFLRAnsprechpartner(
								getInternalFrame(),
								tpProjekt.getPartnerDto().getIId(),
								selectedAnsprechpartner, true, true);
				new DialogQuery(panelQueryFLRAnsprechpartner);
			}
		} else if (e.getActionCommand().equals(
				MY_OWN_NEW_TOGGLE_INTERN_ERLEDIGT)) {
			// PJ 17558
			if (tpProjekt.getProjektDto().getIId() != null) {
				DelegateFactory
						.getInstance()
						.getProjektDelegate()
						.toggleInternErledigt(
								tpProjekt.getProjektDto().getIId());
				eventYouAreSelected(false);
			}
		} else if (e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_START)
				|| e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_STOP)) {

			LPMain.getInstance()
					.getDesktop()
					.zeitbuchungAufBeleg(e.getActionCommand(),
							LocaleFac.BELEGART_PROJEKT,
							tpProjekt.getProjektDto().getIId());

		} else if (e.getActionCommand().equals(MY_OWN_NEW_DOKUMENTENABLAGE)) {
			PrintInfoDto values = DelegateFactory
					.getInstance()
					.getJCRDocDelegate()
					.getPathAndPartnerAndTable(
							tpProjekt.getProjektDto().getIId(),
							QueryParameters.UC_ID_PROJEKT);

			DocPath docPath = values.getDocPath();
			Integer iPartnerIId = values.getiId();
			String sTable = values.getTable();
			if (docPath != null) {
				PanelDokumentenablage panelDokumentenverwaltung = new PanelDokumentenablage(
						getInternalFrame(), tpProjekt.getProjektDto().getIId()
								.toString(), docPath, sTable, tpProjekt
								.getProjektDto().getIId().toString(), true,
						iPartnerIId);
				getInternalFrame().showPanelDialog(panelDokumentenverwaltung);
				getInternalFrame().addItemChangedListener(
						panelDokumentenverwaltung);
			} else {
				// Show Dialog
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("jcr.hinweis.keinpfad"));
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_ANGEBOT)) {
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)) {
				InternalFrameAngebot ifAB = (InternalFrameAngebot) LPMain
						.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_ANGEBOT);
				ifAB.getTabbedPaneAngebot().erstelleAngebotAusProjekt(
						tpProjekt.getProjektDto().getIId());
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_AUFTRAG)) {

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {
				InternalFrameAuftrag ifAB = (InternalFrameAuftrag) LPMain
						.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_AUFTRAG);
				ifAB.getTabbedPaneAuftrag().erstelleAuftragAusProjekt(
						tpProjekt.getProjektDto().getIId());
			}

		}else if (e.getActionCommand().equals(MY_OWN_NEW_AGSTKL)) {

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AGSTUECKLISTE)) {
				InternalFrameAngebotstkl ifAS = (InternalFrameAngebotstkl) LPMain
						.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_AGSTUECKLISTE);
				ifAS.getTabbedPaneAngebotstkl().erstelleAgstklAusProjekt(
						tpProjekt.getProjektDto().getIId());
			}

		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpProjekt.getProjektDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpProjekt.getProjektDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpProjekt.getProjektDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpProjekt.getProjektDto().getTAendern());
		setStatusbarStatusCNr(tpProjekt.getProjektDto().getStatusCNr());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoKategorie;
	}

}
