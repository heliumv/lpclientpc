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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.Defaults;
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
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Panel zum Bearbeiten der Kopfdaten eines Loses
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>06. 10. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.50 $
 */
public class PanelLosKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneLos tabbedPaneLos = null;

	private AuftragDto auftragDto = null;
	private KundeDto kundeDto = null;
	private AuftragpositionDto auftragpositionDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private LagerDto lagerDto = null;
	private PersonalDto personalDtoTechniker = null;
	private PartnerDto partnerDtoFertigungsort = null;
	private FertigungsgruppeDto fertigungsgruppeDto = null;

	private static final String ACTION_SPECIAL_AUFTRAG = "action_special_los_auftrag";
	private static final String ACTION_SPECIAL_KUNDE = "action_special_los_kunde";
	private static final String ACTION_SPECIAL_AUFTRAGPOSITION = "action_special_los_auftragposition";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_los_kostenstelle";
	private static final String ACTION_SPECIAL_LAGER = "action_special_los_lager";
	private static final String ACTION_SPECIAL_STUECKLISTE = "action_special_los_stueckliste";
	private static final String ACTION_SPECIAL_TECHNIKER = "action_special_los_techniker";
	private static final String ACTION_SPECIAL_TECHNIKER_RECHT = "action_special_los_techniker_recht";
	private static final String ACTION_SPECIAL_FERTIGUNGSORT = "action_special_los_fertigungsort";
	private static final String ACTION_SPECIAL_LOSART = "action_special_los_losart";
	private static final String ACTION_SPECIAL_AUSGEBEN = "action_special_los_ausgeben";
	private static final String ACTION_SPECIAL_HANDAUSGABE = "action_special_los_handausgabe";
	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE";
	private static final String ACTION_SPECIAL_LOSEFUERAUFTRAG = "ACTION_SPECIAL_LOSEFUERAUFTRAG";
	private WrapperButton wbuFertigungsgruppe = new WrapperButton();
	private WrapperTextField wtfFertigungsgruppe = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRAuftragposition = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRLager = null;
	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRStueckliste = null;
	private PanelQueryFLR panelQueryFLRTechniker = null;
	private PanelQueryFLR panelQueryFLRFertigungsort = null;
	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanelWorkingOn = new JPanel();
	private WrapperLabel wlaAbteilung = new WrapperLabel();

	private WrapperSelectField wsfLosbereich = null;

	private WrapperTextField wtfAbteilung = new WrapperTextField();
	private WrapperButton wlaKunde = new WrapperButton();
	private WrapperTextField wtfAuftragNummer = new WrapperTextField();
	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftragBezeichnung = new WrapperTextField();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperTextField wtfLager = new WrapperTextField();
	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfAdresse = new WrapperTextField();

	private WrapperButton wbuLoseFurAuftragAnlegen = new WrapperButton();

	private WrapperButton wbuAuftragposition = new WrapperButton();
	private WrapperNumberField wtfAuftragpositionNummer = new WrapperNumberField();
	private WrapperTextField wtfAuftragpositionBezeichnung = new WrapperTextField();

	private WrapperButton wbuTechniker = new WrapperButton();
	private WrapperTextField wtfTechniker = new WrapperTextField();

	private WrapperGotoButton wbuStueckliste = new WrapperGotoButton(
			WrapperGotoButton.GOTO_STUECKLISTE_AUSWAHL);
	private WrapperTextField wtfStuecklisteNummer = new WrapperTextField();
	private WrapperTextField wtfStuecklisteBezeichnung = new WrapperTextField();
	private WrapperTextField wtfStuecklisteZusatzBezeichnung = new WrapperTextField();

	private WrapperButton wbuFertigungsort = new WrapperButton();
	private WrapperTextField wtfFertigungsort = new WrapperTextField();

	private WrapperLabel wlaLosgroesse = new WrapperLabel();
	private WrapperNumberField wnfLosgroesse = null;
	private WrapperLabel wlaEinheit = new WrapperLabel();

	private WrapperLabel wlaSollmaterialGeplant = new WrapperLabel();
	private WrapperNumberField wnfSollmaterialGeplant = new WrapperNumberField();

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperTextField wtfProjekt = new WrapperTextField();
	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField();
	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperEditorField wefText = null;

	private WrapperLabel wlaErledigtam = new WrapperLabel();
	private WrapperLabel wlaAusgegebenam = new WrapperLabel();
	private WrapperLabel wlaGestopptam = new WrapperLabel();

	private WrapperLabel wlaProduktionsbeginn = new WrapperLabel();
	private WrapperDateField wdfProduktionsbeginn = new WrapperDateField();

	private WrapperLabel wlaProduktionsende = new WrapperLabel();
	private WrapperDateField wdfProduktionsende = new WrapperDateField();

	private WrapperLabel wlaDauer = new WrapperLabel();
	private WrapperNumberField wnfDauer = new WrapperNumberField();
	private WrapperLabel wlaTage = new WrapperLabel();

	private WrapperLabel wlaLosart = new WrapperLabel();
	private WrapperComboBox wcoLosart = new WrapperComboBox();
	private WrapperCheckBox wcbKeinPositionsbezug = new WrapperCheckBox();

	private WrapperRadioButton wrbTerminVorwaerts = null;
	private WrapperRadioButton wrbTerminRueckwaerts = null;
	private ButtonGroup bgTermin = null;
	private int bLosnummerIstAuftragsnummer = 0;
	private boolean bNurTermineingabe = false;
	private boolean bAusgegebenEigenerStatus = false;

	private Integer letzterBereichIId = null;

	public PanelLosKopfdaten(InternalFrame internalFrame, String add2TitleI,
			Object key, TabbedPaneLos tabbedPaneLos) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneLos = tabbedPaneLos;
		jbInit();
		initComponents();
		initPanel();
	}

	/**
	 * initPanel
	 */
	private void initPanel() throws Throwable {
		LinkedHashMap<String, String> m = new LinkedHashMap<String, String>();
		m.put(FertigungFac.LOSART_IDENT,
				LPMain.getTextRespectUISPr("label.ident"));
		m.put(FertigungFac.LOSART_MATERIALLISTE,
				LPMain.getTextRespectUISPr("label.materialliste"));
		wcoLosart.setMap(m);

		wrbTerminRueckwaerts.setSelected(true);

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_NUR_TERMINEINGABE);

		if ((Boolean) parameter.getCWertAsObject()) {
			bNurTermineingabe = true;
		}
	}

	private TabbedPaneLos getTabbedPaneLos() {
		return tabbedPaneLos;
	}

	public WrapperEditorField getWefText() {
		return wefText;
	}

	private void dialogQueryFertigungsgruppeFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory
				.getInstance()
				.createPanelFLRFertigungsgruppe(getInternalFrame(), null, false);
		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	private void jbInit() throws Throwable {
		createAndSaveAndShowButton("/com/lp/client/res/data_next.png",
				LPMain.getTextRespectUISPr("fert.tooltip.losausgeben"),
				ACTION_SPECIAL_AUSGEBEN, RechteFac.RECHT_FERT_LOS_CUD);
		createAndSaveAndShowButton("/com/lp/client/res/hand_blue_card.png",
				LPMain.getTextRespectUISPr("fert.tooltip.handausgabe"),
				ACTION_SPECIAL_HANDAUSGABE, RechteFac.RECHT_FERT_LOS_CUD);
		String[] aWhichButtonIUse = null;

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_FERT_TECHNIKER_BEARBEITEN)) {
			createAndSaveAndShowButton("/com/lp/client/res/worker16x16.png",
					LPMain.getTextRespectUISPr("fert.techniker.aendern"),
					ACTION_SPECIAL_TECHNIKER_RECHT,
					RechteFac.RECHT_FERT_TECHNIKER_BEARBEITEN);

			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD, ACTION_SPECIAL_AUSGEBEN,
					ACTION_SPECIAL_HANDAUSGABE, ACTION_SPECIAL_TECHNIKER_RECHT };

		} else {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD, ACTION_SPECIAL_AUSGEBEN,
					ACTION_SPECIAL_HANDAUSGABE };
		}

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);

		bLosnummerIstAuftragsnummer = (Integer) parameter.getCWertAsObject();

		wsfLosbereich = new WrapperSelectField(WrapperSelectField.LOSBEREICH,
				getInternalFrame(), false);

		this.enableToolsPanelButtons(aWhichButtonIUse);
		// Komponenten instantiieren
		wrbTerminVorwaerts = new WrapperRadioButton();
		wrbTerminRueckwaerts = new WrapperRadioButton();
		bgTermin = new ButtonGroup();
		bgTermin.add(wrbTerminRueckwaerts);
		bgTermin.add(wrbTerminVorwaerts);
		wrbTerminVorwaerts.setText(LPMain.getTextRespectUISPr("lp.vorwaerts"));
		wrbTerminRueckwaerts.setText(LPMain
				.getTextRespectUISPr("lp.rueckwaerts"));

		wbuFertigungsgruppe.setText(LPMain
				.getTextRespectUISPr("stkl.fertigungsgruppe") + "...");
		wbuFertigungsgruppe
				.setActionCommand(ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wbuFertigungsgruppe.addActionListener(this);
		wtfFertigungsgruppe.setActivatable(false);

		wlaSollmaterialGeplant.setText(LPMain
				.getTextRespectUISPr("fert.los.geplantessollmaterial"));

		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		this.setLayout(gridBagLayout1);
		wefText = new WrapperEditorFieldTexteingabe(getInternalFrame(),
				LPMain.getTextRespectUISPr("los.kopfdaten.text"));
		wlaText.setText(LPMain.getTextRespectUISPr("los.kopfdaten.text"));
		wlaLosart.setText(LPMain.getTextRespectUISPr("lp.art"));
		wlaLosgroesse.setText(LPMain.getTextRespectUISPr("label.losgroesse"));
		wlaProduktionsbeginn.setText(LPMain.getTextRespectUISPr("lp.beginn"));
		wlaProduktionsende.setText(LPMain.getTextRespectUISPr("lp.ende"));
		wlaProjekt.setText(LPMain.getTextRespectUISPr("fert.los.projekt"));
		wlaDauer.setText(LPMain.getTextRespectUISPr("lp.dauer"));
		wlaTage.setText(LPMain.getTextRespectUISPr("lp.tage"));
		wlaTage.setHorizontalAlignment(SwingConstants.LEFT);
		wlaKommentar.setText(LPMain
				.getTextRespectUISPr("los.kopfdaten.kommentar"));
		wlaAbteilung.setText(LPMain.getTextRespectUISPr("lp.abteilung"));
		wtfAdresse.setActivatable(false);
		wtfAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAuftragpositionNummer.setFractionDigits(0);
		wtfAuftragpositionNummer.setMaximumIntegerDigits(4);
		wtfAuftragpositionNummer.setActivatable(false);
		wbuAuftrag.setText(LPMain.getTextRespectUISPr("button.auftrag"));

		wcbKeinPositionsbezug.setText(LPMain
				.getTextRespectUISPr("fert.los.keinpositionsbezug"));

		wbuLoseFurAuftragAnlegen.setText("->");
		wbuLoseFurAuftragAnlegen.setToolTipText(LPMain
				.getTextRespectUISPr("fert.losefuerauftragpositionenanlegen"));

		wbuAuftragposition.setText(LPMain.getTextRespectUISPr("lp.position"));
		wbuAuftragposition.setToolTipText(LPMain
				.getTextRespectUISPr("lp.position"));
		wlaKunde.setText(LPMain.getTextRespectUISPr("label.kunde"));
		wbuKostenstelle.setText(LPMain
				.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain
				.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wbuLager.setText(LPMain.getTextRespectUISPr("button.ziellager"));
		wbuLager.setToolTipText(LPMain
				.getTextRespectUISPr("button.lager.tooltip"));
		wbuTechniker.setText(LPMain.getTextRespectUISPr("button.techniker"));
		wbuTechniker.setToolTipText(LPMain
				.getTextRespectUISPr("button.techniker.tooltip"));
		wbuFertigungsort.setText(LPMain
				.getTextRespectUISPr("button.fertigungsort"));
		wbuFertigungsort.setToolTipText(LPMain
				.getTextRespectUISPr("button.fertigungsort.tooltip"));
		wbuStueckliste
				.setText(LPMain.getTextRespectUISPr("button.stueckliste"));
		wbuStueckliste.setToolTipText(LPMain
				.getTextRespectUISPr("button.stueckliste.tooltip"));

		wtfKunde.setActivatable(false);

		parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_KUNDE_IST_PFLICHTFELD);

		if ((Boolean) parameter.getCWertAsObject()) {
			wtfKunde.setMandatoryField(true);
		}

		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		wtfAuftragNummer.setActivatable(false);
		wtfAuftragBezeichnung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAuftragBezeichnung.setActivatable(false);
		wtfKostenstelleNummer.setActivatable(false);
		wtfKostenstelleNummer.setMandatoryField(true);
		wtfFertigungsgruppe.setMandatoryField(true);
		wtfKostenstelleBezeichnung.setActivatable(false);
		wtfAuftragpositionBezeichnung.setActivatable(false);
		wtfLager.setActivatable(false);
		wtfStuecklisteBezeichnung.setActivatable(false);
		wtfStuecklisteZusatzBezeichnung.setActivatable(false);
		wtfStuecklisteNummer.setActivatable(false);
		wtfFertigungsgruppe.setActivatable(false);
		wtfTechniker.setActivatable(false);
		wtfFertigungsort.setActivatable(false);

		wbuAuftrag.addActionListener(this);
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG);

		wlaKunde.addActionListener(this);
		wlaKunde.setActionCommand(ACTION_SPECIAL_KUNDE);

		wbuLoseFurAuftragAnlegen.addActionListener(this);
		wbuLoseFurAuftragAnlegen
				.setActionCommand(ACTION_SPECIAL_LOSEFUERAUFTRAG);

		wtfProjekt.setColumnsMax(80);

		wbuAuftragposition.addActionListener(this);
		wbuAuftragposition.setActionCommand(ACTION_SPECIAL_AUFTRAGPOSITION);
		wbuKostenstelle.addActionListener(this);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuLager.addActionListener(this);
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER);
		wbuTechniker.addActionListener(this);
		wbuTechniker.setActionCommand(ACTION_SPECIAL_TECHNIKER);
		wbuFertigungsort.addActionListener(this);
		wbuFertigungsort.setActionCommand(ACTION_SPECIAL_FERTIGUNGSORT);
		wbuStueckliste.addActionListener(this);
		wbuStueckliste.setActionCommand(ACTION_SPECIAL_STUECKLISTE);
		wcoLosart.addActionListener(this);
		wcoLosart.setActionCommand(ACTION_SPECIAL_LOSART);

		wdfProduktionsbeginn.getDisplay().addFocusListener(
				new PanelLosKopfdaten_focusAdapter(this));
		wdfProduktionsende.getDisplay().addFocusListener(
				new PanelLosKopfdaten_focusAdapter(this));
		wnfDauer.addFocusListener(new PanelLosKopfdaten_focusAdapter(this));

		wnfLosgroesse = new WrapperNumberField();
		wnfLosgroesse.setMinimumValue(1);
		wnfLosgroesse.setFractionDigits(0);
		wnfLosgroesse.setMandatoryFieldDB(true);
		wtfAbteilung.setActivatable(false);
		wtfLager.setMandatoryField(true);

		wcoLosart.setMandatoryFieldDB(true);
		wtfStuecklisteNummer.setMandatoryField(true);
		wdfProduktionsbeginn.setMandatoryField(true);
		wdfProduktionsende.setMandatoryField(true);
		wnfDauer.setMandatoryField(true);
		wnfDauer.setMinimumValue(0);
		wnfDauer.setFractionDigits(0);
		wnfDauer.setMaximumIntegerDigits(3);

		parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_NUR_TERMINEINGABE);

		if ((Boolean) parameter.getCWertAsObject()) {
			bNurTermineingabe = true;
		}

		parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_AUSGABE_EIGENER_STATUS);

		if ((Boolean) parameter.getCWertAsObject()) {
			bAusgegebenEigenerStatus = true;
		}

		if (bNurTermineingabe == true) {
			wnfDauer.setActivatable(false);
			wnfDauer.setMandatoryField(false);
		}

		wtfFertigungsort.setMandatoryFieldDB(true);

		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		wlaLosgroesse.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wlaLosgroesse.setPreferredSize(new Dimension(120, Defaults
				.getInstance().getControlHeight()));
		wnfLosgroesse.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wnfLosgroesse.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));

		wbuLager.setMinimumSize(new Dimension(110, Defaults.getInstance()
				.getControlHeight()));
		wbuLager.setPreferredSize(new Dimension(110, Defaults.getInstance()
				.getControlHeight()));

		jPanelWorkingOn.setLayout(new MigLayout("wrap 5, hidemode 3", "[25%][30%][15%][25%][25%]"));
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanelWorkingOn.add(wlaLosart, "growx");
		jPanelWorkingOn.add(wcoLosart, "growx, span");
		
		jPanelWorkingOn.add(wbuAuftrag, "growx");
		jPanelWorkingOn.add(wtfAuftragNummer, "growx");
		jPanelWorkingOn.add(wtfAuftragBezeichnung, "growx 90, split 2, span");
		jPanelWorkingOn.add(wbuLoseFurAuftragAnlegen, "growx 10");

		if (bLosnummerIstAuftragsnummer == 2) {
			jPanelWorkingOn.add(wsfLosbereich.getWrapperButton(), "growx");
			jPanelWorkingOn.add(wsfLosbereich.getWrapperTextField(), "growx");
		} else {
			jPanelWorkingOn.add(wbuAuftragposition, "growx");
			jPanelWorkingOn.add(wtfAuftragpositionNummer, "growx");

			if (bLosnummerIstAuftragsnummer == 1) {
				jPanelWorkingOn.add(wtfAuftragpositionBezeichnung, "growx, span2");

				wcbKeinPositionsbezug.addActionListener(this);

				jPanelWorkingOn.add(wcbKeinPositionsbezug, "growx");
			} else if (bLosnummerIstAuftragsnummer == 0) {
				jPanelWorkingOn.add(wtfAuftragpositionBezeichnung, "growx, span 3");
			}
		}
		jPanelWorkingOn.add(wlaKunde, "newline, growx");
		jPanelWorkingOn.add(wtfKunde, "growx, span");
		
		jPanelWorkingOn.add(wtfAdresse, "skip, growx, span");
		
		jPanelWorkingOn.add(wlaAbteilung, "growx");
		jPanelWorkingOn.add(wtfAbteilung, "growx, span");
		
		jPanelWorkingOn.add(wbuKostenstelle, "growx");
		jPanelWorkingOn.add(wtfKostenstelleNummer, "growx");
		jPanelWorkingOn.add(wtfKostenstelleBezeichnung, "growx, span");
		
		jPanelWorkingOn.add(wbuStueckliste, "growx");
		jPanelWorkingOn.add(wtfStuecklisteNummer, "growx");
		jPanelWorkingOn.add(wtfStuecklisteBezeichnung, "growx, span");
		
		jPanelWorkingOn.add(wtfStuecklisteZusatzBezeichnung, "skip 2, growx, span");
		
		jPanelWorkingOn.add(wlaLosgroesse, "growx");
		jPanelWorkingOn.add(wnfLosgroesse, "growx");
		jPanelWorkingOn.add(wlaEinheit, "growx");
		jPanelWorkingOn.add(wbuLager, "growx");
		jPanelWorkingOn.add(wtfLager, "growx");
		
		jPanelWorkingOn.add(wlaProduktionsbeginn, "growx");
		jPanelWorkingOn.add(wdfProduktionsbeginn, "left, growx, split 2");
		jPanelWorkingOn.add(wlaDauer, "right, growx");
		jPanelWorkingOn.add(wnfDauer, "growx");
		jPanelWorkingOn.add(wlaTage, "left, growx 25, split 2, span");
		jPanelWorkingOn.add(wlaAusgegebenam, "right, growx 75");
		
		jPanelWorkingOn.add(wlaProduktionsende, "growx");
		jPanelWorkingOn.add(wdfProduktionsende, "left, growx, split 2");

		if (bNurTermineingabe == false) {

			jPanelWorkingOn.add(wrbTerminRueckwaerts, "right, growx");
			jPanelWorkingOn.add(wrbTerminVorwaerts, "growx");
			jPanelWorkingOn.add(wlaErledigtam, "right, growx, span");
		} else {
			jPanelWorkingOn.add(wlaErledigtam, "skip 2, right, growx, span");
		}

		jPanelWorkingOn.add(wbuTechniker, "growx");
		jPanelWorkingOn.add(wtfTechniker, "growx");
		jPanelWorkingOn.add(wlaGestopptam, "skip, growx, span");
		
		jPanelWorkingOn.add(wlaProjekt, "growx");
		jPanelWorkingOn.add(wtfProjekt, "growx, span 2");
		jPanelWorkingOn.add(wlaSollmaterialGeplant, "growx");
		jPanelWorkingOn.add(wnfSollmaterialGeplant, "growx, wrap");
		
		jPanelWorkingOn.add(wlaKommentar, "growx");
		jPanelWorkingOn.add(wtfKommentar, "growx, span 2, wrap");

		jPanelWorkingOn.add(wbuFertigungsgruppe, "growx");
		jPanelWorkingOn.add(wtfFertigungsgruppe, "growx");
		jPanelWorkingOn.add(wbuFertigungsort, "growx, span 2");
		jPanelWorkingOn.add(wtfFertigungsort, "growx, wrap");
		
		jPanelWorkingOn.add(wlaText, "growx");
		jPanelWorkingOn.add(wefText, "grow, span");
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			LosDto losDto = getTabbedPaneLos().getLosDto();
			// wenn alles passt, wird gespeichert
			if (losDto != null) {
				boolean bIsNewLos = getTabbedPaneLos().getLosDto().getIId() == null;
				// speichern
				LosDto savedDto = null;
				try {
					savedDto = DelegateFactory.getInstance()
							.getFertigungDelegate().updateLos(losDto);
				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.error.losnummerzuauftragspositionschonvorhanden"));
						return;
					} else {
						handleException(ex, true);
						return;
					}

				}
				// falls neues Los, key setzen
				if (losDto.getIId() == null) {
					this.setKeyWhenDetailPanel(savedDto.getIId());
					// PJ13767
					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.zeigeArtikelhinweiseAllerLossollpositionen(
									savedDto.getIId());

				}
				getTabbedPaneLos().setLosDto(savedDto);
				getTabbedPaneLos().pruefeStuecklisteAktuellerAlsLosDlg();
				// das Panel aktualisieren
				super.eventActionSave(e, true);
				eventYouAreSelected(false);
				if (bIsNewLos) {
					getInternalFrame().setKeyWasForLockMe(
							savedDto.getIId().toString());
				}
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
		LosDto dto = getTabbedPaneLos().getLosDto();
		if (dto == null) {
			dto = new LosDto();
			dto.setMandantCNr(LPMain.getTheClient().getMandant());
		}

		if (auftragDto != null || auftragpositionDto != null) {
			dto.setKundeIId(null);
		}

		if (auftragDto == null && kundeDto != null) {
			dto.setKundeIId(kundeDto.getIId());
		}
		if (auftragpositionDto != null) {
			dto.setAuftragpositionIId(auftragpositionDto.getIId());
			dto.setAuftragIId(auftragpositionDto.getBelegIId());
			if (wtfAuftragpositionNummer.getInteger() != null) {
				dto.setCZusatznummer(wtfAuftragpositionNummer.getInteger()
						.toString());
			}
		} else {
			dto.setAuftragpositionIId(null);
			dto.setCZusatznummer(null);
			// Kopplung an Auftrag, aber nicht an eine bestimmte Position
			if (auftragDto != null) {
				dto.setAuftragIId(auftragDto.getIId());
			} else {
				dto.setAuftragIId(null);
			}
		}
		dto.setCKommentar(wtfKommentar.getText());
		dto.setCProjekt(wtfProjekt.getText());
		if (kostenstelleDto != null) {
			dto.setKostenstelleIId(kostenstelleDto.getIId());
		} else {
			dto.setKostenstelleIId(null);
		}
		if (lagerDto != null) {
			dto.setLagerIIdZiel(lagerDto.getIId());
		}
		if (fertigungsgruppeDto != null) {
			dto.setFertigungsgruppeIId(fertigungsgruppeDto.getIId());
		}
		/**
		 * @todo elternlos PJ 5008
		 */
		dto.setNLosgroesse(wnfLosgroesse.getBigDecimal());
		if (partnerDtoFertigungsort != null) {
			dto.setPartnerIIdFertigungsort(partnerDtoFertigungsort.getIId());
		} else {
			dto.setPartnerIIdFertigungsort(null);
		}
		dto.setNSollmaterial(wnfSollmaterialGeplant.getBigDecimal());
		if (personalDtoTechniker != null) {
			dto.setPersonalIIdTechniker(personalDtoTechniker.getIId());
		} else {
			dto.setPersonalIIdTechniker(null);
		}
		if (getTabbedPaneLos().getStuecklisteDto() != null) {
			dto.setStuecklisteIId(getTabbedPaneLos().getStuecklisteDto()
					.getIId());
		} else {
			dto.setStuecklisteIId(null);
		}
		dto.setTProduktionsbeginn(wdfProduktionsbeginn.getDate());
		dto.setTProduktionsende(wdfProduktionsende.getDate());
		dto.setXText(wefText.getText());
		dto.setLosbereichIId(wsfLosbereich.getIKey());

		if (wsfLosbereich.getIKey() != null) {
			letzterBereichIId = wsfLosbereich.getIKey();
		}

		getTabbedPaneLos().setLosDto(dto);
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		LosDto losDto = getTabbedPaneLos().getLosDto();
		if (losDto.getStuecklisteIId() != null) {
			// Goto Stueckliste Button Ziel setzen
			wbuStueckliste.setOKey(losDto.getStuecklisteIId());
			wcoLosart.setKeyOfSelectedItem(FertigungFac.LOSART_IDENT);

		} else {
			wbuStueckliste.setOKey(null);
			wcoLosart.setKeyOfSelectedItem(FertigungFac.LOSART_MATERIALLISTE);
		}
		updateLosart();

		
		holeKunde(losDto.getKundeIId());
		
		holeAuftragposition(losDto.getAuftragpositionIId());

		
		if (auftragpositionDto != null) {
			holeAuftrag(auftragpositionDto.getBelegIId());
		} else {
			holeAuftrag(losDto.getAuftragIId());
		}
		
	
		

		if (auftragpositionDto == null) {
			wcbKeinPositionsbezug.setSelected(true);
			wtfAuftragpositionNummer.setMandatoryField(false);
		}
		holeFertigungsort(losDto.getPartnerIIdFertigungsort());
		holeKostenstelle(losDto.getKostenstelleIId());
		holeStueckliste(losDto.getStuecklisteIId());
		holeLager(losDto.getLagerIIdZiel());
		holeFertigungsgruppe(losDto.getFertigungsgruppeIId());

		holeTechniker(losDto.getPersonalIIdTechniker());
		wtfKommentar.setText(losDto.getCKommentar());
		wtfProjekt.setText(losDto.getCProjekt());
		/**
		 * @todo elternlos PJ 5008
		 */
		wnfLosgroesse.setBigDecimal(losDto.getNLosgroesse());
		wnfSollmaterialGeplant.setBigDecimal(losDto.getNSollmaterial());
		wdfProduktionsbeginn.setDate(losDto.getTProduktionsbeginn());
		wdfProduktionsende.setDate(losDto.getTProduktionsende());
		Integer iDauer = new Integer(Helper.getDifferenzInTagen(
				wdfProduktionsbeginn.getDate(), wdfProduktionsende.getDate()));
		wnfDauer.setInteger(iDauer);
		wefText.setText(losDto.getXText());
		wefText.setDefaultText(losDto.getXText());

		wsfLosbereich.setKey(losDto.getLosbereichIId());

		String s = LPMain.getTextRespectUISPr("fert.erledigtam") + " ";

		if (losDto.getTManuellerledigt() != null) {
			s += Helper.formatDatum(losDto.getTManuellerledigt(), LPMain
					.getTheClient().getLocUi());
			s += ": " + LPMain.getTextRespectUISPr("bes.abruf_bes.manuel");

			if (losDto.getPersonalIIdManuellerledigt() != null) {
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								losDto.getPersonalIIdManuellerledigt());
				s += "(" + personalDto.getCKurzzeichen() + ")";
			}

			wlaErledigtam.setText(s);
		} else if (losDto.getTErledigt() != null) {
			s += Helper.formatDatum(losDto.getTErledigt(), LPMain
					.getTheClient().getLocUi());
			s += ": " + LPMain.getTextRespectUISPr("lp.automatisch");

			if (losDto.getPersonalIIdErledigt() != null) {
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								losDto.getPersonalIIdErledigt());
				s += " (" + personalDto.getCKurzzeichen() + ")";
			}

			wlaErledigtam.setText(s);
		} else {
			wlaErledigtam.setText("");
		}

		if (losDto.getTAusgabe() != null) {

			String ausg = LPMain.getTextRespectUISPr("fert.ausgegebenam")
					+ ": ";

			ausg += Helper.formatDatum(losDto.getTAusgabe(), LPMain
					.getTheClient().getLocUi());

			if (losDto.getPersonalIIdAusgabe() != null) {
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								losDto.getPersonalIIdAusgabe());
				ausg += " (" + personalDto.getCKurzzeichen() + ")";
			}

			wlaAusgegebenam.setText(ausg);

		} else {
			wlaAusgegebenam.setText("");
		}
		
		
		if (losDto.getTProduktionsstop() != null) {

			String ausg = LPMain.getTextRespectUISPr("fert.gestopptam")
					+ ": ";

			ausg += Helper.formatDatum(losDto.getTProduktionsstop(), LPMain
					.getTheClient().getLocUi());

			if (losDto.getPersonalIIdProduktionsstop()!= null) {
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								losDto.getPersonalIIdProduktionsstop());
				ausg += " (" + personalDto.getCKurzzeichen() + ")";
			}

			wlaGestopptam.setText(ausg);

		} else {
			wlaGestopptam.setText("");
		}
		

		this.setStatusbarPersonalIIdAnlegen(losDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(losDto.getTAnlegen());
		this.setStatusbarPersonalIIdAendern(losDto.getPersonalIIdAendern());
		this.setStatusbarTAendern(losDto.getTAendern());
		this.setStatusbarStatusCNr(losDto.getStatusCNr());

		StringBuffer sbZusatzinfo = new StringBuffer();
		if (getTabbedPaneLos().getLosDto().getStuecklisteIId() != null
				&& ((getTabbedPaneLos().getLosDto()
						.getTAktualisierungarbeitszeit() != null && getTabbedPaneLos()
						.getStuecklisteDto()
						.getTAendernarbeitsplan()
						.after(getTabbedPaneLos().getLosDto()
								.getTAktualisierungarbeitszeit())) || (getTabbedPaneLos()
						.getLosDto().getTAktualisierungstueckliste() != null && getTabbedPaneLos()
						.getStuecklisteDto()
						.getTAendernposition()
						.after(getTabbedPaneLos().getLosDto()
								.getTAktualisierungstueckliste())))) {
			sbZusatzinfo.append(LPMain
					.getTextRespectUISPr("fert.hinweis.stklveraendert"));
		}
		this.setStatusbarSpalte5(sbZusatzinfo.toString());
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

	private void dto2ComponentsFertigungsgruppe() {
		if (fertigungsgruppeDto != null) {
			wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
		} else {
			wtfFertigungsgruppe.setText(null);
		}
	}

	private void dto2ComponentsAuftrag() throws Throwable {
		if (auftragDto != null) {
			wtfAuftragNummer.setText(auftragDto.getCNr());
			wtfAuftragBezeichnung.setText(auftragDto
					.getCBezProjektbezeichnung());

			if (bLosnummerIstAuftragsnummer == 1) {
				wtfAuftragpositionNummer.setMandatoryField(true);
			} else if (bLosnummerIstAuftragsnummer == 2) {
				wsfLosbereich.setMandatoryField(true);
			}

			// wenn noch kein Projekt eingegeben ist, wird das vom Auftrag
			// uebernommen
			if (wtfProjekt.getText() == null
					|| wtfProjekt.getText().trim().equals("")) {
				wtfProjekt.setText(auftragDto.getCBezProjektbezeichnung());
			}
			// die kostenstelle wird aus dem auftrag uebernommen, wenn nicht
			// schon anders definiert
			if (kostenstelleDto == null && auftragDto.getKostIId() != null) {
				holeKostenstelle(auftragDto.getKostIId());
			}
			// wenn schon eine position definiert ist und die zu einem anderen
			// auftrag gehoert, dann loeschen
			if (auftragpositionDto != null
					&& !auftragpositionDto.getBelegIId().equals(
							auftragDto.getIId())) {
				holeAuftragposition(null);
			}
		} else {
			wtfAuftragNummer.setText(null);
			wtfAuftragBezeichnung.setText(null);
			// auch die auftragsposition loeschen
			holeAuftragposition(null);
			wtfAuftragpositionNummer.setMandatoryField(false);
			wsfLosbereich.setMandatoryField(false);

		}
		dto2ComponentsKunde();
	}

	private void dto2ComponentsAuftragposition() throws Throwable {
		if (auftragpositionDto != null) {
			wtfAuftragpositionNummer.setInteger(auftragpositionDto.getISort());
			wtfAuftragpositionBezeichnung.setText(auftragpositionDto.getCBez());

			//
			if (auftragpositionDto.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								auftragpositionDto.getArtikelIId());
				if (artikelDto.getArtikelsprDto() != null) {
					wtfAuftragpositionBezeichnung.setText(artikelDto
							.getArtikelsprDto().getCBez());
				} else {
					wtfAuftragpositionBezeichnung.setText(auftragpositionDto
							.getCBez());
				}
			} else {
				wtfAuftragpositionBezeichnung.setText(auftragpositionDto
						.getCBez());
			}
		} else {

			wtfAuftragpositionNummer.setInteger(null);
			wtfAuftragpositionBezeichnung.setText(null);
		}
	}

	private void dto2ComponentsKunde() throws Throwable {

		wtfKunde.setText(null);
		wtfAdresse.setText(null);
		wtfAbteilung.setText(null);
		if (auftragDto != null) {
			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							auftragDto.getKundeIIdAuftragsadresse());
			this.wtfKunde.setText(kundeDto.getPartnerDto()
					.formatFixTitelName1Name2());
			if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
				this.wtfAdresse.setText(kundeDto.getPartnerDto()
						.getLandplzortDto().formatLandPlzOrt());
			} else {
				this.wtfAdresse.setText(null);
			}
			this.wtfAbteilung.setText(kundeDto.getPartnerDto()
					.getCName3vorname2abteilung());
		} else {

			if (getTabbedPaneLos().getLosDto() != null
					&& getTabbedPaneLos().getLosDto().getKundeIId() != null) {
				kundeDto = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								getTabbedPaneLos().getLosDto().getKundeIId());
			}
			if (kundeDto != null) {

				this.wtfKunde.setText(kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
				if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
					this.wtfAdresse.setText(kundeDto.getPartnerDto()
							.getLandplzortDto().formatLandPlzOrt());
				} else {
					this.wtfAdresse.setText(null);
				}
				this.wtfAbteilung.setText(kundeDto.getPartnerDto()
						.getCName3vorname2abteilung());
			}

		}
	}

	private void dto2ComponentsTechniker() {
		if (personalDtoTechniker != null) {

			wtfTechniker.setText(personalDtoTechniker
					.formatFixUFTitelName2Name1());
		} else {
			wtfTechniker.setText(null);

		}

	}

	private void dto2ComponentsFertigungsort() throws Throwable {
		if (partnerDtoFertigungsort != null) {
			String sFertigungsOrt = null;
			sFertigungsOrt = partnerDtoFertigungsort.formatTitelAnrede();
			wtfFertigungsort.setText(sFertigungsOrt);
		} else {
			wtfFertigungsort.setText(null);
		}
	}

	private void dto2ComponentsStueckliste() throws Throwable {
		if (getTabbedPaneLos().getStuecklisteDto() != null) {
			// Der Artikel kommt nicht immer mit
			if (getTabbedPaneLos().getStuecklisteDto().getArtikelDto() == null) {
				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								getTabbedPaneLos().getStuecklisteDto()
										.getArtikelIId());
				getTabbedPaneLos().getStuecklisteDto()
						.setArtikelDto(artikelDto);
			}
			wtfStuecklisteNummer.setText(getTabbedPaneLos().getStuecklisteDto()
					.getArtikelDto().getCNr());
			if (getTabbedPaneLos().getStuecklisteDto().getArtikelDto()
					.getArtikelsprDto() != null) {
				wtfStuecklisteBezeichnung.setText(getTabbedPaneLos()
						.getStuecklisteDto().getArtikelDto().getArtikelsprDto()
						.getCBez());
				wtfStuecklisteZusatzBezeichnung.setText(getTabbedPaneLos()
						.getStuecklisteDto().getArtikelDto().getArtikelsprDto()
						.getCZbez());
			} else {
				wtfStuecklisteBezeichnung.setText(null);
				wtfStuecklisteZusatzBezeichnung.setText(null);
			}
			if (wnfDauer.getBigDecimal() == null) {
				wnfDauer.setBigDecimal(getTabbedPaneLos().getStuecklisteDto()
						.getNDefaultdurchlaufzeit());
			}

			wlaEinheit.setText(getTabbedPaneLos().getStuecklisteDto()
					.getArtikelDto().getEinheitCNr());

			holeLager(getTabbedPaneLos().getStuecklisteDto()
					.getLagerIIdZiellager());

		} else {
			wtfStuecklisteNummer.setText(null);
			wtfStuecklisteBezeichnung.setText(null);
			wtfStuecklisteZusatzBezeichnung.setText(null);
			wlaEinheit.setText(SystemFac.EINHEIT_STUECK);
		}
	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		getTabbedPaneLos().setLosDto(null);
		// damit das pos.feld nicht mandatory wird
		holeAuftrag(null);
		this.kostenstelleDto = null;
		this.lagerDto = null;
		this.kundeDto = null;
		this.leereAlleFelder(this);
		this.clearStatusbar();
		wlaAusgegebenam.setText(null);
		wlaGestopptam.setText(null);
		lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.getHauptlagerDesMandanten();
		dto2ComponentsLager();
		MandantDto mandantDto = DelegateFactory.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());
		holeFertigungsort(mandantDto.getPartnerIId());
		// Hauptkostenstelle des Mandanten vorbesetzen
		if (mandantDto.getIIdKostenstelle() != null) {
			holeKostenstelle(mandantDto.getIIdKostenstelle());
		}
		// lt. WH: Fertigungsgrupe mit der ersten vorbesetzen
		FertigungsgruppeDto[] dtos = DelegateFactory.getInstance()
				.getStuecklisteDelegate().fertigungsgruppeFindByMandantCNr();

		if (dtos != null && dtos.length > 0) {
			holeFertigungsgruppe(dtos[0].getIId());
		}
		wlaEinheit.setText(SystemFac.EINHEIT_STUECK);
		wnfLosgroesse.setInteger(new Integer(1));
		wtfAuftragpositionNummer.setMandatoryField(false);
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
		LosDto losDto = getTabbedPaneLos().getLosDto();
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					"Los ist bereits storniert");
			return;
		} 

		String[] optionen = new String[2];
		optionen[0] = LPMain.getTextRespectUISPr("lp.ja");
		optionen[1] = LPMain.getTextRespectUISPr("lp.nein");

		
		String meldung= LPMain.getTextRespectUISPr("fert.los.stornieren");
		if(DelegateFactory.getInstance().getZeiterfassungDelegate().sindBelegzeitenVorhanden(LocaleFac.BELEGART_LOS, losDto.getIId())){
			//PJ17710
			meldung=LPMain.getTextRespectUISPr("fert.los.stornieren.zeitengebucht");
		}
		
		
		
		int choice = JOptionPane.showOptionDialog(this,meldung,
				LPMain.getTextRespectUISPr("lp.frage"),
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				optionen, optionen[1]);
		switch (choice) {
		case JOptionPane.YES_OPTION: {
			boolean bAuftragsbezugEntfernen=false;
			//SP1349
			if(losDto.getAuftragpositionIId()!=null){
				int i = DialogFactory
						.showModalJaNeinAbbrechenDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("fert.los.stornieren.aufposbezugentfernen"),
								LPMain.getTextRespectUISPr(
												"lp.frage"));
				if (i == JOptionPane.YES_OPTION) {
					bAuftragsbezugEntfernen = true;
				}  else if (i == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			
			
			
			DelegateFactory.getInstance().getFertigungDelegate()
					.storniereLos(losDto.getIId(),bAuftragsbezugEntfernen);
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

	protected String getLockMeWer() {
		return HelperClient.LOCKME_LOS;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG)) {
			dialogQueryAuftrag(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAGPOSITION)) {
			if (auftragDto == null) {
				// zuerst den auftrag
				dialogQueryAuftrag(e);
			} else {
				dialogQueryAuftragposition(e);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER)) {
			dialogQueryLager(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE)) {

			if (wtfAuftragNummer.getText() == null) {
				dialogQueryKunde(e);
			} else {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), LPMain
						.getTextRespectUISPr("fert.los.kundenurohneauftrag"));
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_TECHNIKER)) {
			dialogQueryTechniker(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_TECHNIKER_RECHT)) {
			LosDto losDto1 = getTabbedPaneLos().getLosDto();

			if (losDto1.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)
					|| losDto1.getStatusCNr().equals(
							FertigungFac.STATUS_ERLEDIGT)) {
				DialogFactory.showMeldung(
						LPMain.getTextRespectUISPr("lp.techniker.aendern"),
						LPMain.getTextRespectUISPr("lp.achtung"),
						javax.swing.JOptionPane.CLOSED_OPTION);
			} else {
				dialogQueryTechniker(e);
				components2Dto();
				LosDto savedDto = DelegateFactory.getInstance()
						.getFertigungDelegate().updateLos(losDto1);
				getTabbedPaneLos().setLosDto(savedDto);
				getTabbedPaneLos().pruefeStuecklisteAktuellerAlsLosDlg();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FERTIGUNGSORT)) {
			dialogQueryFertigungsort(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_STUECKLISTE)) {
			dialogQueryStueckliste(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_AUSGEBEN)) {
			// los aktualisieren und Stuecklistenaenderungen pruefen
			getTabbedPaneLos().reloadLosDto();
			int iAnswer = getTabbedPaneLos()
					.pruefeStuecklisteAktuellerAlsLosDlg();
			if (iAnswer != JOptionPane.CANCEL_OPTION) {
				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.gebeLosAus(
								getTabbedPaneLos().getLosDto().getIId(),
								false,
								getTabbedPaneLos()
										.getAbzubuchendeSeriennrChargen(
												getTabbedPaneLos().getLosDto()
														.getNLosgroesse()));
				// refresh aufs panel
				eventYouAreSelected(false);
				getTabbedPaneLos()
						.printAusgabelisteUndFertigungsbegleitscheinDlg();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_HANDAUSGABE)) {
			// los aktualisieren und Stuecklistenaenderungen pruefen
			getTabbedPaneLos().reloadLosDto();
			int iAnswer = getTabbedPaneLos()
					.pruefeStuecklisteAktuellerAlsLosDlg();
			if (iAnswer != JOptionPane.CANCEL_OPTION) {
				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.gebeLosAus(getTabbedPaneLos().getLosDto().getIId(),
								true, null);
				// refresh aufs panel
				eventYouAreSelected(false);
				getTabbedPaneLos()
						.printAusgabelisteUndFertigungsbegleitscheinDlg();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LOSART)) {
			updateLosart();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			dialogQueryFertigungsgruppeFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LOSEFUERAUFTRAG)) {

			if (getTabbedPaneLos().getLosDto() != null) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								"Diese Funkion kann nur verwendet werden, wenn ein neues Los angelegt wird.");

				return;

			}

			// Es muss zumindest
			// AUFTRAG/KOSTENSTELLE/BEGINN/ENDE/FERTIGUNGSGRUPPE/FERTIGUGNSORT
			// ausgewaehlt sein
			components2Dto();

			LosDto losDto = getTabbedPaneLos().getLosDto();

			if (losDto.getAuftragIId() == null
					|| losDto.getKostenstelleIId() == null
					|| losDto.getTProduktionsbeginn() == null
					|| losDto.getTProduktionsende() == null
					|| losDto.getFertigungsgruppeIId() == null
					|| losDto.getPartnerIIdFertigungsort() == null) {
				allMandatoryFieldsSetDlg();
				return;
			} else {

				LosDto losNeuDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.createLoseAusAuftrag(losDto, losDto.getAuftragIId());
				if (losNeuDto != null) {
					getTabbedPaneLos().setLosDto(losNeuDto);
					// In Auswahlliste wechseln
					this.eventYouAreSelected(false);
					getTabbedPaneLos().getPanelQueryAuswahl(false)
							.setSelectedId(losNeuDto.getIId());
				}
				getTabbedPaneLos().setSelectedIndex(0);

			}

		}

		else if (e.getSource().equals(wcbKeinPositionsbezug)) {
			if (wtfAuftragNummer.getText() != null) {
				if (wcbKeinPositionsbezug.isSelected()) {
					wtfAuftragpositionNummer.setMandatoryField(false);
					wtfAuftragpositionNummer.setInteger(null);
					wtfAuftragpositionBezeichnung.setText(null);
					if (getTabbedPaneLos().getLosDto() != null) {
						getTabbedPaneLos().getLosDto().setAuftragpositionIId(
								null);
					}
					auftragpositionDto = null;
				} else {
					wtfAuftragpositionNummer.setMandatoryField(true);
				}
			} else {
				wtfAuftragpositionNummer.setMandatoryField(false);
			}

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
			if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKostenstelle(key);
			} else if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeLager((Integer) key);
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeAuftrag(key);
				if (bLosnummerIstAuftragsnummer == 1) {
					wcbKeinPositionsbezug.setSelected(false);
					wtfAuftragpositionNummer.setMandatoryField(true);
				} else if (bLosnummerIstAuftragsnummer == 2) {
					wsfLosbereich.setMandatoryField(true);
					if(letzterBereichIId != null)
						wsfLosbereich.setKey(letzterBereichIId);
				}
			} else if (e.getSource() == panelQueryFLRKunde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (getTabbedPaneLos().getLosDto() != null) {
					getTabbedPaneLos().getLosDto().setKundeIId((Integer) key);
				}

				holeKunde(key);
				dto2ComponentsKunde();
			} else if (e.getSource() == panelQueryFLRAuftragposition) {
				Object keyPos = ((ISourceEvent) e.getSource()).getIdSelected();
				holeAuftragposition(keyPos);

				// Stueckliste vorbesetzen, wenns eine ist und wenn vorher noch
				// keine definiert wurde
				boolean bAendern = false;
				if (getTabbedPaneLos().getStuecklisteDto() != null) {
					// PJ 13784
					bAendern = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("fert.stuecklisteaendern"),
									LPMain.getTextRespectUISPr("lp.frage"));

				} else {
					bAendern = true;
				}

				if (bAendern) {
					if (auftragpositionDto.getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
						StuecklisteDto stuecklisteDto = DelegateFactory
								.getInstance()
								.getStuecklisteDelegate()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										auftragpositionDto.getArtikelIId());

						boolean bZuorden = true;
						if (stuecklisteDto == null) {
							bZuorden = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr("fert.warning.keinestueckliste"),
											LPMain.getTextRespectUISPr("lp.hint"));
							if (bZuorden == false) {
								return;
							}
						} else {

							ArtikelDto artikelDto = DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikelFindByPrimaryKey(
											stuecklisteDto.getArtikelIId());

							artikelDto = DelegateFactory
									.getInstance()
									.getArtikelkommentarDelegate()
									.pruefeArtikel(artikelDto,
											LocaleFac.BELEGART_LOS,
											getInternalFrame());

							if (artikelDto == null) {
								bZuorden = false;
							} else {
								holeFertigungsgruppe(stuecklisteDto.getFertigungsgruppeIId());
							}

						}

						if (bZuorden == true) {
							// Stueckliste setzen
							getTabbedPaneLos()
									.setStuecklisteDto(stuecklisteDto);
							dto2ComponentsStueckliste();
							// beim neu anlegen werden ein paar felder
							// vorbesetzt
							Object key = getKeyWhenDetailPanel();
							if (key == null
									|| (key.equals(LPMain.getLockMeForNew()))) {
								// Offene Menge im Auftrag

								// PJ 09/14030
								if (auftragpositionDto.getNOffeneMenge() != null
										&& auftragpositionDto.getNOffeneMenge()
												.doubleValue() <= 0) {

									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.hint"),
													LPMain.getTextRespectUISPr("lp.error.neueslosausauftragsposition"));

									wnfLosgroesse.setBigDecimal(new BigDecimal(
											1));
								} else {
									wnfLosgroesse
											.setBigDecimal(auftragpositionDto
													.getNOffeneMenge());
								}

								// Endtermin ist der Liefertermin der
								// Auftragsposition ...

								int iLieferdauer = 0;
								if (auftragDto != null
										&& auftragDto
												.getKundeIIdAuftragsadresse() != null) {
									KundeDto kundeDto = DelegateFactory
											.getInstance()
											.getKundeDelegate()
											.kundeFindByPrimaryKey(
													auftragDto
															.getKundeIIdAuftragsadresse());
									iLieferdauer = kundeDto.getILieferdauer();
								}

								if (auftragpositionDto
										.getTUebersteuerbarerLiefertermin() != null) {

									wdfProduktionsende
											.setDate(new Timestamp(
													auftragpositionDto
															.getTUebersteuerbarerLiefertermin()
															.getTime()
															- (24 * 3600000 * iLieferdauer)));
								}
								// ... oder der aus den Auftragskopfdaten
								else if (auftragDto != null) {
									wdfProduktionsende
											.setDate(new Date(
													auftragDto
															.getDLiefertermin()
															.getTime()
															- (24 * 3600000 * iLieferdauer)));
								}
								// jetzt die Rueckwaertsterminierung aktivieren
								wrbTerminRueckwaerts.setSelected(true);
								// Defaultdurchlaufzeit vorbesetzen
								Integer iDefaultdurchlaufzeit;
								// zuerst aus der Stueckliste
								if (getTabbedPaneLos().getStuecklisteDto() != null
										&& getTabbedPaneLos()
												.getStuecklisteDto()
												.getNDefaultdurchlaufzeit() != null) {
									iDefaultdurchlaufzeit = new Integer(
											getTabbedPaneLos()
													.getStuecklisteDto()
													.getNDefaultdurchlaufzeit()
													.intValue());
								} else {
									// sonst aus dem Mandantenparameter
									ParametermandantDto parameter = DelegateFactory
											.getInstance()
											.getParameterDelegate()
											.getMandantparameter(
													LPMain.getTheClient()
															.getMandant(),
													ParameterFac.KATEGORIE_FERTIGUNG,
													ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
									iDefaultdurchlaufzeit = new Integer(
											parameter.getCWert());
								}
								wnfDauer.setInteger(iDefaultdurchlaufzeit);
								focusLostDauer();
							}
						}
					}
					// kein Stuecklistenartikel
					else {

						boolean bZuorden = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("fert.warning.keinestueckliste"),
										LPMain.getTextRespectUISPr("lp.hint"));

						if (bZuorden == false) {
							auftragpositionDto = null;
							dto2ComponentsAuftragposition();
							return;
						}
					}
				}

			} else if (e.getSource() == panelQueryFLRStueckliste) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				StuecklisteDto stuecklisteDto = DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey((Integer) key);

				String[] hinweise = DelegateFactory
						.getInstance()
						.getArtikelkommentarDelegate()
						.getArtikelhinweise(stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS);
				if (hinweise != null) {
					for (int i = 0; i < hinweise.length; i++) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								Helper.strippHTML(hinweise[i]));
					}
				}

				// Wenn Fremdfertigung, dann Warnung (Projekt 13478)

				if (stuecklisteDto != null
						&& Helper.short2boolean(stuecklisteDto
								.getBFremdfertigung()) == true) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.warning"),
									LPMain.getTextRespectUISPr("stkl.auswahl.fremdgefertigt"));

				}

				if (stuecklisteDto != null
						&& !stuecklisteDto.getStuecklisteartCNr().equals(
								StuecklisteFac.STUECKLISTEART_STUECKLISTE)) {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("fert.los.warnung.hilfsstueckliste"));
					return;

				}

				holeStueckliste(key);

				if (getTabbedPaneLos().getStuecklisteDto() != null) {

					// PJ14987

					ArtikelDto artikelDto = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									getTabbedPaneLos().getStuecklisteDto()
											.getArtikelIId());

					artikelDto = DelegateFactory
							.getInstance()
							.getArtikelkommentarDelegate()
							.pruefeArtikel(artikelDto, LocaleFac.BELEGART_LOS,
									getInternalFrame());

					if (artikelDto == null) {
						holeStueckliste(null);
					} else {
						holeFertigungsgruppe(getTabbedPaneLos()
								.getStuecklisteDto().getFertigungsgruppeIId());
					}

				}
			} else if (e.getSource() == panelQueryFLRTechniker) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeTechniker(key);
			} else if (e.getSource() == panelQueryFLRFertigungsort) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					MandantDto mandantDto = DelegateFactory.getInstance()
							.getMandantDelegate()
							.mandantFindByPrimaryKey((String) key);
					holeFertigungsort(mandantDto.getPartnerIId());
				} else {
					holeFertigungsort(null);
				}
			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				holeFertigungsgruppe(key);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				holeAuftrag(null);
				holeAuftragposition(null);
				wtfAuftragpositionNummer.setMandatoryField(false);
			} else if (e.getSource() == panelQueryFLRAuftragposition) {
				holeAuftragposition(null);
			} else if (e.getSource() == panelQueryFLRKunde) {
				getTabbedPaneLos().getLosDto().setKundeIId(null);
				kundeDto=null;
				dto2ComponentsKunde();
			}
		}
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

	private void holeLager(Integer key) throws Throwable {
		if (key != null) {
			lagerDto = DelegateFactory.getInstance().getLagerDelegate()
					.lagerFindByPrimaryKey(key);
		} else {
			lagerDto = null;
		}
		dto2ComponentsLager();
	}

	private void holeAuftrag(Object key) throws Throwable {
		if (key != null) {
			auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey((Integer) key);
		} else {
			auftragDto = null;
		}
		dto2ComponentsAuftrag();
	}

	private void holeKunde(Object key) throws Throwable {
		if (key != null) {
			kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey((Integer) key);
		} else {
			kundeDto = null;
		}
		dto2ComponentsKunde();
	}

	private void holeFertigungsgruppe(Integer key) throws Throwable {
		if (key != null) {
			fertigungsgruppeDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.fertigungsgruppeFindByPrimaryKey(key);

		} else {
			fertigungsgruppeDto = null;
		}
		dto2ComponentsFertigungsgruppe();
	}

	private void holeAuftragposition(Object key) throws Throwable {
		if (key != null) {
			auftragpositionDto = DelegateFactory.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByPrimaryKey((Integer) key);
		} else {
			auftragpositionDto = null;
		}
		dto2ComponentsAuftragposition();
	}

	private void holeStueckliste(Object key) throws Throwable {
		if (key != null) {
			getTabbedPaneLos().setStuecklisteDto(
					DelegateFactory.getInstance().getStuecklisteDelegate()
							.stuecklisteFindByPrimaryKey((Integer) key));

			BigDecimal nDurchlaufzeit = getTabbedPaneLos().getStuecklisteDto()
					.getNDefaultdurchlaufzeit();
			if (nDurchlaufzeit != null) {
				int iDurchlaufzeit = nDurchlaufzeit.intValue();
				if (wrbTerminRueckwaerts.isSelected()) {
					if (wdfProduktionsende.getTimestamp() != null) {
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(wdfProduktionsende.getTimestamp()
								.getTime());
						c.set(Calendar.DATE, c.get(Calendar.DATE)
								- iDurchlaufzeit);
						wdfProduktionsbeginn.setTimestamp(new Timestamp(c
								.getTimeInMillis()));
						wnfDauer.setInteger(iDurchlaufzeit);
					}
				} else {
					if (wdfProduktionsbeginn.getTimestamp() != null) {
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(wdfProduktionsbeginn.getTimestamp()
								.getTime());
						c.set(Calendar.DATE, c.get(Calendar.DATE)
								+ iDurchlaufzeit);
						wdfProduktionsende.setTimestamp(new Timestamp(c
								.getTimeInMillis()));
						wnfDauer.setInteger(iDurchlaufzeit);
					}
				}
			}
		} else {
			getTabbedPaneLos().setStuecklisteDto(null);
		}
		dto2ComponentsStueckliste();
	}

	private void holeTechniker(Object key) throws Throwable {
		if (key != null) {
			personalDtoTechniker = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey((Integer) key);
		} else {
			personalDtoTechniker = null;
		}
		dto2ComponentsTechniker();
	}

	private void holeFertigungsort(Integer key) throws Throwable {
		if (key != null) {
			partnerDtoFertigungsort = DelegateFactory.getInstance()
					.getPartnerDelegate().partnerFindByPrimaryKey(key);
		} else {
			partnerDtoFertigungsort = null;
		}
		dto2ComponentsFertigungsort();
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
	 * Dialogfenster zur Auftragauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryAuftrag(ActionEvent e) throws Throwable {

		boolean bRahmenauftraegeVerwendbar = false;
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_RAHMENAUFTRAEGE_IN_FERTIGUNG_VERWENDBAR);

		if ((Boolean) parameter.getCWertAsObject()) {
			bRahmenauftraegeVerwendbar = true;
		}

		if (bRahmenauftraegeVerwendbar == false) {

			panelQueryFLRAuftrag = AuftragFilterFactory
					.getInstance()
					.createPanelFLRAuftrag(getInternalFrame(), true, true, null);

		} else {
			panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
					.createPanelFLRAuftragMitRahmen(getInternalFrame(), true,
							true, null);
		}
		if (auftragDto != null) {
			panelQueryFLRAuftrag.setSelectedId(auftragDto.getIId());
		}
		new DialogQuery(panelQueryFLRAuftrag);

	}

	/**
	 * Dialogfenster zur Auftragauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryAuftragposition(ActionEvent e) throws Throwable {
		FilterKriterium[] filtersPositionen = AuftragFilterFactory
				.getInstance().createFKFlrauftragiid(auftragDto.getIId());
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };
		panelQueryFLRAuftragposition = new PanelQueryFLR(
				null,
				filtersPositionen,
				QueryParameters.UC_ID_AUFTRAGPOSITION,
				aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("title.auftragspositionauswahlliste"));
		if (auftragpositionDto != null) {
			panelQueryFLRAuftragposition.setSelectedId(auftragpositionDto
					.getIId());
		}
		new DialogQuery(panelQueryFLRAuftragposition);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {
			LosDto losDto = getTabbedPaneLos().getLosDto();
			if (losDto != null) {
				losDto = DelegateFactory.getInstance().getFertigungDelegate()
						.losFindByPrimaryKey(losDto.getIId());
				getTabbedPaneLos().setLosDto(losDto);
				dto2Components();
			} else {
				// Neu
				wdfProduktionsbeginn
						.setTimestamp(Helper.cutTimestamp(new Timestamp(System
								.currentTimeMillis())));
				wdfProduktionsende
						.setTimestamp(Helper.cutTimestamp(new Timestamp(System
								.currentTimeMillis())));
				wnfDauer.setInteger(0);
			}
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		LosDto losDto = getTabbedPaneLos().getLosDto();
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			boolean bStornoAufheben = (DialogFactory.showMeldung(
					LPMain.getTextRespectUISPr("fert.frage.stornoaufheben"),
					LPMain.getTextRespectUISPr("lp.frage"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (bStornoAufheben) {
				DelegateFactory.getInstance().getFertigungDelegate()
						.storniereLosRueckgaengig(losDto.getIId());
				eventYouAreSelected(false);
				getTabbedPaneLos().pruefeStuecklisteAktuellerAlsLosDlg();
			} else {
				return;
			}
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			boolean bStornoAufheben = (DialogFactory.showMeldung(
					LPMain.getTextRespectUISPr("fert.frage.erledigtaufheben"),
					LPMain.getTextRespectUISPr("lp.frage"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (bStornoAufheben) {
				DelegateFactory.getInstance().getFertigungDelegate()
						.manuellErledigenRueckgaengig(losDto.getIId());
				eventYouAreSelected(false);
			}
			return;

		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| losDto.getStatusCNr().equals(
						FertigungFac.STATUS_IN_PRODUKTION)
				|| losDto.getStatusCNr().equals(
						FertigungFac.STATUS_TEILERLEDIGT)) {

			if (bAusgegebenEigenerStatus
					&& losDto.getStatusCNr().equals(
							FertigungFac.STATUS_IN_PRODUKTION)) {
				boolean bStornoAufheben = (DialogFactory.showMeldung(LPMain
						.getTextRespectUISPr("fert.frage.statusaufausgegeben"),
						LPMain.getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bStornoAufheben) {
					DelegateFactory.getInstance().getFertigungDelegate()
							.gebeLosAusRueckgaengig(losDto.getIId());
					eventYouAreSelected(false);
				} else {
					return;
				}
			} else {

				boolean bStornoAufheben = (DialogFactory
						.showMeldung(
								LPMain.getTextRespectUISPr("fert.frage.ausgabenzuruecknehmen"),
								LPMain.getTextRespectUISPr("lp.frage"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bStornoAufheben) {
					DelegateFactory.getInstance().getFertigungDelegate()
							.gebeLosAusRueckgaengig(losDto.getIId());
					eventYouAreSelected(false);
					getTabbedPaneLos().pruefeStuecklisteAktuellerAlsLosDlg();
				} else {
					return;
				}
			}

		}

		super.eventActionUpdate(aE, bNeedNoUpdateI);

		wsfLosbereich.setEnabled(false);

	}

	private void dialogQueryLager(ActionEvent e) throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						(lagerDto != null) ? lagerDto.getIId() : null);
		new DialogQuery(panelQueryFLRLager);
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), false, true);
		new DialogQuery(panelQueryFLRKunde);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoLosart;
	}

	private void dialogQueryTechniker(ActionEvent e) throws Throwable {
		panelQueryFLRTechniker = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), true, true);
		if (personalDtoTechniker != null) {
			panelQueryFLRTechniker.setSelectedId(personalDtoTechniker.getIId());
		}
		new DialogQuery(panelQueryFLRTechniker);
	}

	private void dialogQueryFertigungsort(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRFertigungsort = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_MANDANT, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("title.mandantauswahlliste"));
		if (partnerDtoFertigungsort != null) {
			MandantDto[] mandantDto = DelegateFactory.getInstance()
					.getMandantDelegate()
					.mandantFindByPartnerIId(partnerDtoFertigungsort.getIId());
			if (mandantDto != null && mandantDto.length > 0) {
				panelQueryFLRFertigungsort
						.setSelectedId(mandantDto[0].getCNr());
			}
		}
		new DialogQuery(panelQueryFLRFertigungsort);
	}

	private void dialogQueryStueckliste(ActionEvent e) throws Throwable {
		Integer selectedId = null;
		if (getTabbedPaneLos().getStuecklisteDto() != null) {
			selectedId = getTabbedPaneLos().getStuecklisteDto().getIId();
		}
		panelQueryFLRStueckliste = StuecklisteFilterFactory.getInstance()
				.createPanelFLRStueckliste(getInternalFrame(), selectedId,
						false);
		new DialogQuery(panelQueryFLRStueckliste);
	}

	public void focusLostDatum(FocusEvent e) throws Throwable {
		if (e.getSource() == wnfDauer) {
			focusLostDauer();
		} else if (e.getSource() == wdfProduktionsbeginn.getDisplay()) {
			focusLostProduktionsbeginn();
		} else if (e.getSource() == wdfProduktionsende.getDisplay()) {
			focusLostProduktionsende();
		}
	}

	private void focusLostProduktionsende() throws ExceptionLP {
		// Rueckwaertsterminierung
		if (bNurTermineingabe) {
			berechneDauer();
		} else {

			if (wrbTerminRueckwaerts.isSelected()) {
				Integer iDauer = wnfDauer.getInteger();
				// Ausgehend von Endtermin und Dauer wird das beginndatum
				// ermittelt
				if (wdfProduktionsende.getDate() != null && iDauer != null) {
					Date dBeginn = Helper.addiereTageZuDatum(
							wdfProduktionsende.getDate(), -iDauer.intValue());
					wdfProduktionsbeginn.setDate(dBeginn);
				}
			}
			// Vorwaertsterminierung
			else if (wrbTerminVorwaerts.isSelected()) {
				// hier passiert grundsaetzlich nichts, nur wenn genau dieses
				// feld
				// noch leer ist.
				Integer iDauer = wnfDauer.getInteger();
				if (wdfProduktionsende.getDate() == null
						&& wdfProduktionsbeginn.getDate() != null
						&& iDauer != null) {
					Date dEnde = Helper.addiereTageZuDatum(
							wdfProduktionsbeginn.getDate(), iDauer.intValue());
					wdfProduktionsende.setDate(dEnde);
				}
			}
		}
	}

	private void focusLostDauer() throws ExceptionLP {
		// Rueckwaertsterminierung
		if (wrbTerminRueckwaerts.isSelected()) {
			Integer iDauer = wnfDauer.getInteger();
			// Ausgehend von Endtermin und Dauer wird das beginndatum ermittelt
			if (wdfProduktionsende.getDate() != null && iDauer != null) {
				Date dBeginn = Helper.addiereTageZuDatum(
						wdfProduktionsende.getDate(), -iDauer.intValue());
				wdfProduktionsbeginn.setDate(dBeginn);
			}
		} else if (wrbTerminVorwaerts.isSelected()) {
			if (wdfProduktionsbeginn.getDate() != null
					&& wnfDauer.getInteger() != null) {
				Date dEnde = Helper.addiereTageZuDatum(wdfProduktionsbeginn
						.getDate(), wnfDauer.getInteger().intValue());
				wdfProduktionsende.setDate(dEnde);
			}
		}
	}

	private void berechneDauer() {
		wnfDauer.setInteger(null);

		if (wdfProduktionsbeginn.getTimestamp() != null
				&& wdfProduktionsende.getTimestamp() != null) {
			wnfDauer.setInteger(Helper.getDifferenzInTagen(
					wdfProduktionsbeginn.getDate(),
					wdfProduktionsende.getDate()));
		}
	}

	private void focusLostProduktionsbeginn() throws ExceptionLP {
		// Rueckwaertsterminierung

		if (bNurTermineingabe) {
			berechneDauer();
		} else {

			if (wrbTerminRueckwaerts.isSelected()) {
				// hier passiert grundsaetzlich nichts, nur wenn genau dieses
				// feld
				// noch leer ist.
				Integer iDauer = wnfDauer.getInteger();
				if (wdfProduktionsbeginn.getDate() == null
						&& wdfProduktionsende.getDate() != null
						&& iDauer != null) {
					Date dBeginn = Helper.addiereTageZuDatum(
							wdfProduktionsende.getDate(), -iDauer.intValue());
					wdfProduktionsbeginn.setDate(dBeginn);
				}
			}
			// Vorwaertsterminierung
			else if (wrbTerminVorwaerts.isSelected()) {
				Integer iDauer = wnfDauer.getInteger();
				// Ausgehend von Endtermin und Dauer wird das beginndatum
				// ermittelt
				if (wdfProduktionsbeginn.getDate() != null && iDauer != null) {
					Date dEnde = Helper.addiereTageZuDatum(
							wdfProduktionsbeginn.getDate(), iDauer.intValue());
					wdfProduktionsende.setDate(dEnde);
				}
			}
		}
	}

	private void updateLosart() throws Throwable {
		Object key = wcoLosart.getKeyOfSelectedItem();
		// if (this.getLockedstateDetailMainKey() == null ||
		// this.getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW ||
		// this.getLockedstateDetailMainKey().getIState() ==
		// LOCK_IS_LOCKED_BY_ME) {
		if (key.equals(FertigungFac.LOSART_IDENT)) {
			wbuStueckliste.setActionCommand(ACTION_SPECIAL_STUECKLISTE);
			wtfStuecklisteNummer.setMandatoryField(true);
		} else if (key.equals(FertigungFac.LOSART_MATERIALLISTE)) {
			holeAuftrag(null);
			holeAuftragposition(null);
			wbuStueckliste.setActionCommand("");
			wtfStuecklisteNummer.setMandatoryField(false);
			getTabbedPaneLos().setStuecklisteDto(null);
			dto2ComponentsStueckliste();

			FertigungsgruppeDto[] dtos = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.fertigungsgruppeFindByMandantCNr();
			if (dtos != null && dtos.length > 0) {
				holeFertigungsgruppe(dtos[0].getIId());
			}

		}
		// }
	}
}

class PanelLosKopfdaten_focusAdapter implements java.awt.event.FocusListener {
	private PanelLosKopfdaten adaptee;

	PanelLosKopfdaten_focusAdapter(PanelLosKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.focusLostDatum(e);
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}
