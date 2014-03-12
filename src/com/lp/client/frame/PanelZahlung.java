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
package com.lp.client.frame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelQueryFLRGoto;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.PanelRechnungZahlung;
import com.lp.client.rechnung.TabbedPaneGutschrift;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse ist ein gemeinsames Panel zur Eingabe von Zahlungen</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008 / 2005
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>29.03.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public abstract class PanelZahlung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected BankverbindungDto bankverbindungDto = null;
	protected KassenbuchDto kassenbuchDto = null;
	protected BuchungdetailDto buchungdetailDto = null;
	private static Map<?, ?> zahlungsarten;

	private ZahlungszielDto zahlungszielDto = null;

	private PanelQueryFLR panelQueryFLRBank = null;
	protected PanelQueryFLR panelQueryFLRGutschrift = null;
	protected PanelQueryFLR panelQueryFLREingangsrechnung = null;
	private PanelQueryFLR panelQueryFLRKassenbuch = null;
	private PanelQueryFLR panelQueryFLRVorauszahlung = null;
	private static final String ACTION_SPECIAL_BANK = "action_special_bank";
	private static final String ACTION_SPECIAL_KASSENBUCH = "action_special_kassenbuch";
	protected static final String ACTION_SPECIAL_GUTSCHRIFT = "action_special_gutschrift";
	protected static final String ACTION_SPECIAL_VORAUSZAHLUNG = "action_special_anzahlung";
	protected static final String ACTION_SPECIAL_EINGANGSRECHNUNG = "action_special_er";
	private static final String ACTION_SPECIAL_ZAHLUNGSART = "action_special_zahlungsart";
	private static final String ACTION_SPECIAL_SKONTO1 = "action_special_skonto1";
	private static final String ACTION_SPECIAL_SKONTO2 = "action_special_skonto2";

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	protected JPanel jPanelWorkingOn = new JPanel();

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperButton wbuBankverbindung = new WrapperButton();
	private WrapperTextField wtfBankverbindungNummer = new WrapperTextField();
	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperLabel wlaOffen = new WrapperLabel();
	private WrapperLabel wlaGesOffen = new WrapperLabel();
	private WrapperLabel wlaUST = new WrapperLabel();
	private WrapperLabel wlaZahlungsart = new WrapperLabel();
	private WrapperLabel wlaUST1 = new WrapperLabel();
	private WrapperLabel wlaGesUST = new WrapperLabel();
	private WrapperTextField wtfBankverbindungBezeichnung = new WrapperTextField();
	private WrapperLabel wlaAuszug = new WrapperLabel();
	private WrapperButton wbuEingangsrechnung = new WrapperButton();
	protected WrapperTextField wtfEingangsrechnung = new WrapperTextField();
	private WrapperButton wbuKassenbuch = new WrapperButton();
	protected WrapperButton wbuGutschrift = new WrapperButton();
	protected WrapperButton wbuVorauszahlung = new WrapperButton();
	private WrapperTextField wtfKassenbuch = new WrapperTextField();
	protected WrapperTextField wtfGutschrift = new WrapperTextField();
	protected WrapperTextField wtfVorauszahlung = new WrapperTextField();
	private WrapperLabel wlaBereitsBezahlt = new WrapperLabel();
	private WrapperLabel wlaBereitsBezahltMwst = new WrapperLabel();
	private WrapperLabel wlaWaehrung1 = new WrapperLabel();
	private WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperLabel wlaWaehrung3 = new WrapperLabel();
	private WrapperLabel wlaWaehrung4 = new WrapperLabel();
	private WrapperLabel wlaWaehrung5 = new WrapperLabel();
	private WrapperLabel wlaWaehrung6 = new WrapperLabel();
	private WrapperLabel wlaWaehrung7 = new WrapperLabel();
	private WrapperLabel wlaWaehrung8 = new WrapperLabel();
	private WrapperLabel wlaWaehrung9 = new WrapperLabel();
	private WrapperLabel wlaWaehrung10 = new WrapperLabel();
	private WrapperLabel wlaSkonto = new WrapperLabel();
	private WrapperLabel wlaSkontoUST = new WrapperLabel();
	private WrapperLabel wlaRechnung = new WrapperLabel();
	protected WrapperTextField wtfRechnung = new WrapperTextField();

	protected WrapperLabel wlaAnzahlungen = new WrapperLabel();
	protected WrapperLabel wlaAnzahlungenUST = new WrapperLabel();
	protected WrapperNumberField wnfAnzahlungen = new WrapperNumberField();
	protected WrapperNumberField wnfAnzahlungenUST = new WrapperNumberField();
	protected WrapperLabel wlaWaehrungAnz = new WrapperLabel();
	protected WrapperLabel wlaWaehrungAnzUst = new WrapperLabel();

	protected WrapperComboBox wcoZahlungsart = new WrapperComboBox();
	protected WrapperDateField wdfDatum = new WrapperDateField();
	protected WrapperNumberField wnfBetrag = new WrapperNumberField();
	protected WrapperNumberField wnfBetragUST = new WrapperNumberField();
	protected WrapperNumberField wtnfAuszug = new WrapperNumberField();
	protected WrapperCheckBox wcbErledigt = new WrapperCheckBox();
	protected WrapperNumberField wnfBetragSkonto1 = new WrapperNumberField();
	protected WrapperNumberField wnfBetragSkonto2 = new WrapperNumberField();
	protected WrapperButton wbuSkonto1uebernehmen = new WrapperButton();
	protected WrapperButton wbuSkonto2uebernehmen = new WrapperButton();

	private WrapperLabel wlaWert = new WrapperLabel();
	private WrapperLabel wlaWertUST = new WrapperLabel();
	private WrapperNumberField wnfWert = new WrapperNumberField();
	private WrapperNumberField wnfWertUST = new WrapperNumberField();

	private WrapperLabel wlaKurs = new WrapperLabel();
	protected WrapperNumberField wnfKurs = new WrapperNumberField();

	protected WrapperNumberField wnfOffen = new WrapperNumberField();
	protected WrapperNumberField wnfGesOffen = new WrapperNumberField();
	private WrapperNumberField wnfOffenUST = new WrapperNumberField();
	private WrapperNumberField wnfGesOffenUST = new WrapperNumberField();

	private WrapperNumberField wnfBereitsBezahlt = new WrapperNumberField();
	private WrapperNumberField wnfBereitsBezahltMwst = new WrapperNumberField();

	private WrapperLabel wlaZahlungsziel = new WrapperLabel();
	private WrapperTextField wtfZahlungsziel = new WrapperTextField();

	private WrapperLabel wlaSkonto1 = new WrapperLabel();
	protected WrapperNumberField wnfSkontoProzent1 = new WrapperNumberField();
	private WrapperLabel wlaProzent1 = new WrapperLabel();
	private WrapperLabel wlaBis1 = new WrapperLabel();
	private WrapperDateField wdfSkonto1 = new WrapperDateField();

	private WrapperLabel wlaSkonto2 = new WrapperLabel();
	protected WrapperNumberField wnfSkontoProzent2 = new WrapperNumberField();
	private WrapperLabel wlaProzent2 = new WrapperLabel();
	private WrapperLabel wlaBis2 = new WrapperLabel();
	private WrapperDateField wdfSkonto2 = new WrapperDateField();

	private WrapperDateField wdfZieldatum = new WrapperDateField();

	protected boolean skonto1selected;
	protected boolean skonto2selected;
	protected TabbedPane tabbedPane = null;

	public PanelZahlung(InternalFrame internalFrame, TabbedPane tabbedPane,
			String add2TitleI) throws Throwable {
		super(internalFrame, add2TitleI);
		this.tabbedPane = tabbedPane;
		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		this.setBorder(BorderFactory.createEtchedBorder());
		JPanel panelButtonAction = getToolsPanel();
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);
		// ... bis hier ist's immer gleich
		// wegen dialogFLR
		getInternalFrame().addItemChangedListener(this);
		skonto1selected = false;
		skonto2selected = false;
		jPanelWorkingOn.setOpaque(true);
		wdfDatum.setActivatable(true);
		wdfDatum.setMandatoryField(false);
		wdfDatum.setMandatoryFieldDB(true);
		wcoZahlungsart.setMandatoryField(false);
		wcoZahlungsart.setMandatoryFieldDB(true);
		wlaDatum.setMinimumSize(new Dimension(130, Defaults.getInstance()
				.getControlHeight()));
		wlaDatum.setPreferredSize(new Dimension(130, Defaults.getInstance()
				.getControlHeight()));
		wnfBetragUST.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wnfBetragUST.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wnfBetragSkonto1.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfBetragSkonto1.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfBetragSkonto2.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfBetragSkonto2.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wbuSkonto1uebernehmen.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wbuSkonto1uebernehmen.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wbuSkonto2uebernehmen.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wbuSkonto2uebernehmen.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaWert.setText(LPMain.getTextRespectUISPr("lp.wert"));
		wlaWertUST.setText(LPMain.getTextRespectUISPr("lp.mwstshort"));
		wlaDatum.setText(LPMain.getTextRespectUISPr("label.zahldatum"));
		wlaRechnung.setText(LPMain.getTextRespectUISPr("rechnung.modulname"));
		wbuBankverbindung.setText(LPMain
				.getTextRespectUISPr("button.bankverbindung"));
		wbuBankverbindung.setText(LPMain
				.getTextRespectUISPr("button.bankverbindung.tooltip"));
		wcbErledigt.setText(LPMain.getTextRespectUISPr("label.erledigt"));
		wlaKurs.setText(LPMain.getTextRespectUISPr("label.kurs"));
		wnfKurs.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setActivatable(false);
		wtfBankverbindungNummer.setActivatable(false);
		wtfBankverbindungNummer.setMandatoryField(true);
		wnfBetrag.setMandatoryFieldDB(true);
		wnfBetrag.setDependenceField(true);
		wnfBetrag.addFocusListener(new PanelZahlung_focusAdapter(this));
		wnfBetrag.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					try {
						focusLostWnfBetrag();
					} catch (Throwable e1) {
					}
			}
		});
		wdfDatum.getDisplay().addFocusListener(
				new PanelZahlung_wdfDatumFocusAdapter(this));
		// wnfBetrag.setMinimumValue(new BigDecimal(0));
		wlaBetrag.setText(LPMain.getTextRespectUISPr("label.betrag"));
		wlaOffen.setText(LPMain.getTextRespectUISPr("label.offen"));
		wlaGesOffen.setText(LPMain.getTextRespectUISPr("label.gesamtoffen"));
		wlaAuszug.setText(LPMain.getTextRespectUISPr("label.auszug"));
		wtfRechnung.setActivatable(false);
		wlaBis1.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaBis2.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaSkonto.setText(LPMain.getTextRespectUISPr("lp.skonto"));
		wlaSkonto1.setText(LPMain.getTextRespectUISPr("lp.skonto"));
		wlaSkonto2.setText(LPMain.getTextRespectUISPr("lp.skonto"));
		wbuSkonto1uebernehmen.setText(LPMain
				.getTextRespectUISPr("lp.uebernehmen"));
		wbuSkonto2uebernehmen.setText(LPMain
				.getTextRespectUISPr("lp.uebernehmen"));
		wlaProzent1.setText("%");
		wlaProzent2.setText("%");
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaZahlungsziel.setText(LPMain
				.getTextRespectUISPr("label.zahlungsziel"));
		// wnfBetragUST.setMandatoryField(true);
		wnfBetragUST.setDependenceField(true);
		String mwstshort = LPMain.getTextRespectUISPr("lp.mwstshort");
		wlaUST.setText(mwstshort);
		wlaSkontoUST.setText(mwstshort);
		wlaUST1.setText(mwstshort);
		wlaGesUST.setText(mwstshort);
		wlaBereitsBezahlt.setText(LPMain
				.getTextRespectUISPr("label.bisherbezahlt"));
		wlaBereitsBezahltMwst.setText(mwstshort);
		wlaUST.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaUST.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaZahlungsart.setText(LPMain.getTextRespectUISPr("lp.art"));
		wtfKassenbuch.setActivatable(false);
		wnfOffen.setActivatable(false);
		wnfGesOffen.setActivatable(false);
		wnfWert.setActivatable(false);
		wnfWertUST.setActivatable(false);
		wnfOffen.setDependenceField(true);
		wnfGesOffen.setDependenceField(true);
		wnfOffenUST.setActivatable(false);
		wnfOffenUST.setDependenceField(true);
		wnfGesOffenUST.setActivatable(false);
		wnfGesOffenUST.setDependenceField(true);
		wnfBereitsBezahlt.setActivatable(false);
		wnfBereitsBezahltMwst.setActivatable(false);

		wnfBetragSkonto1.setActivatable(false);
		wnfBetragSkonto2.setActivatable(false);
		wnfSkontoProzent1.setActivatable(false);
		wnfSkontoProzent2.setActivatable(false);
		wtfZahlungsziel.setActivatable(false);
		wdfSkonto1.setActivatable(false);
		wdfSkonto2.setActivatable(false);
		wdfZieldatum.setActivatable(false);
		wtfGutschrift.setActivatable(false);
		wtfEingangsrechnung.setActivatable(false);
		wtfVorauszahlung.setActivatable(false);

		wlaWaehrung1.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung1.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung2.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung3.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung3.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung4.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung4.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung4.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung5.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung5.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung5.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung6.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung6.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung6.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung7.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung7.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung7.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung8.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung8.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung8.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung9.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung9.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung9.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung10.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung10.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung10.setHorizontalAlignment(SwingConstants.LEFT);
		wlaUST1.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaUST1.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaGesUST.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaGesUST.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wbuKassenbuch.setText(LPMain.getTextRespectUISPr("finanz.kassenbuch"));
		wbuGutschrift
				.setText(LPMain.getTextRespectUISPr("rechnung.gutschrift"));
		wbuVorauszahlung.setText(LPMain
				.getTextRespectUISPr("rech.zahlung.vorauszahlung"));

		wbuEingangsrechnung.setText(LPMain
				.getTextRespectUISPr("button.eingangsrechnung"));
		wbuEingangsrechnung.setActionCommand(ACTION_SPECIAL_EINGANGSRECHNUNG);
		wbuEingangsrechnung.addActionListener(this);

		wnfBetragUST.setActivatable(false);

		wcoZahlungsart.setActionCommand(ACTION_SPECIAL_ZAHLUNGSART);
		wcoZahlungsart.addActionListener(this);
		wbuBankverbindung.setActionCommand(ACTION_SPECIAL_BANK);
		wbuBankverbindung.addActionListener(this);
		wbuKassenbuch.setActionCommand(ACTION_SPECIAL_KASSENBUCH);
		wbuKassenbuch.addActionListener(this);
		wbuGutschrift.setActionCommand(ACTION_SPECIAL_GUTSCHRIFT);
		wbuGutschrift.addActionListener(this);
		wbuVorauszahlung.setActionCommand(ACTION_SPECIAL_VORAUSZAHLUNG);
		wbuVorauszahlung.addActionListener(this);
		wbuSkonto1uebernehmen.setActionCommand(ACTION_SPECIAL_SKONTO1);
		wbuSkonto1uebernehmen.addActionListener(this);
		wbuSkonto2uebernehmen.setActionCommand(ACTION_SPECIAL_SKONTO2);
		wbuSkonto2uebernehmen.addActionListener(this);

		wlaWaehrungAnz.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungAnzUst.setHorizontalAlignment(SwingConstants.LEFT);
		wnfAnzahlungen.setActivatable(false);
		wnfAnzahlungenUST.setActivatable(false);
		wlaAnzahlungen.setText(LPMain
				.getTextRespectUISPr("rech.zahlung.anzahlungen"));
		wlaAnzahlungenUST.setText(LPMain.getTextRespectUISPr("lp.mwstshort"));

		// MB 04.05.06 auf 10 vorkommastllen beschraenken
		wnfBetrag.setMaximumIntegerDigits(10);

		wtfBankverbindungBezeichnung.setActivatable(false);
		wtfBankverbindungBezeichnung.setColumnsMax(80);
		
		jPanelWorkingOn.setLayout(new MigLayout("wrap 7, hidemode 3", "[25%][20%][5%][10%][20%][10%][10%]"));
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wlaZahlungsziel, "growx");
		jPanelWorkingOn.add(wtfZahlungsziel, "growx, span 3");
		jPanelWorkingOn.add(wdfZieldatum, "growx, wrap");
		
		jPanelWorkingOn.add(wlaSkonto1, "growx");
		jPanelWorkingOn.add(wnfSkontoProzent1, "growx");
		jPanelWorkingOn.add(wlaProzent1, "growx");
		jPanelWorkingOn.add(wlaBis1, "growx");
		jPanelWorkingOn.add(wdfSkonto1, "growx");
		jPanelWorkingOn.add(wnfBetragSkonto1, "growx");
		jPanelWorkingOn.add(wbuSkonto1uebernehmen, "growx, wrap");
		
		jPanelWorkingOn.add(wlaSkonto2, "growx");
		jPanelWorkingOn.add(wnfSkontoProzent2, "growx");
		jPanelWorkingOn.add(wlaProzent2, "growx");
		jPanelWorkingOn.add(wlaBis2, "growx");
		jPanelWorkingOn.add(wdfSkonto2, "growx");
		jPanelWorkingOn.add(wnfBetragSkonto2, "growx");
		jPanelWorkingOn.add(wbuSkonto2uebernehmen, "growx, wrap");
		
		jPanelWorkingOn.add(wlaDatum, "growx");
		jPanelWorkingOn.add(wdfDatum, "growx");
		jPanelWorkingOn.add(wlaZahlungsart, "growx, span 2");
		jPanelWorkingOn.add(wcoZahlungsart, "growx, span");
		
		jPanelWorkingOn.add(wbuBankverbindung, "growx");
		jPanelWorkingOn.add(wtfBankverbindungNummer, "growx");
		jPanelWorkingOn.add(wtfBankverbindungBezeichnung, "growx, span");
		
		jPanelWorkingOn.add(wlaAuszug, "growx");
		jPanelWorkingOn.add(wtnfAuszug, "growx, wrap");
		
		jPanelWorkingOn.add(wbuKassenbuch, "growx");
		jPanelWorkingOn.add(wtfKassenbuch, "growx, wrap");
		
		jPanelWorkingOn.add(wbuGutschrift, "growx");
		jPanelWorkingOn.add(wtfGutschrift, "growx, wrap");
		
		jPanelWorkingOn.add(wbuEingangsrechnung, "growx");
		jPanelWorkingOn.add(wtfEingangsrechnung, "growx, wrap");
		
		jPanelWorkingOn.add(wbuVorauszahlung, "growx");
		jPanelWorkingOn.add(wtfVorauszahlung, "growx, wrap");
		
		jPanelWorkingOn.add(wlaRechnung, "growx");
		jPanelWorkingOn.add(wtfRechnung, "growx, wrap");
		
		jPanelWorkingOn.add(wlaWert, "growx");
		jPanelWorkingOn.add(wnfWert, "growx");
		jPanelWorkingOn.add(wlaWaehrung7, "growx");
		jPanelWorkingOn.add(wlaWertUST, "growx");
		jPanelWorkingOn.add(wnfWertUST, "growx");
		jPanelWorkingOn.add(wlaWaehrung8, "growx, wrap");
		
		jPanelWorkingOn.add(wlaAnzahlungen, "growx");
		jPanelWorkingOn.add(wnfAnzahlungen, "growx");
		jPanelWorkingOn.add(wlaWaehrungAnz, "growx");
		jPanelWorkingOn.add(wlaAnzahlungenUST, "growx");
		jPanelWorkingOn.add(wnfAnzahlungenUST, "growx");
		jPanelWorkingOn.add(wlaWaehrungAnzUst, "growx, wrap");
		
		jPanelWorkingOn.add(wlaBereitsBezahlt, "growx");
		jPanelWorkingOn.add(wnfBereitsBezahlt, "growx");
		jPanelWorkingOn.add(wlaWaehrung5, "growx");
		jPanelWorkingOn.add(wlaBereitsBezahltMwst, "growx");
		jPanelWorkingOn.add(wnfBereitsBezahltMwst, "growx");
		jPanelWorkingOn.add(wlaWaehrung6, "growx, wrap");
		
		jPanelWorkingOn.add(wlaBetrag, "growx");
		jPanelWorkingOn.add(wnfBetrag, "growx");
		jPanelWorkingOn.add(wlaWaehrung1, "growx");
		jPanelWorkingOn.add(wlaUST, "growx");
		jPanelWorkingOn.add(wnfBetragUST, "growx");
		jPanelWorkingOn.add(wlaWaehrung2, "growx, wrap");
		
		jPanelWorkingOn.add(wlaOffen, "growx");
		jPanelWorkingOn.add(wnfOffen, "growx");
		jPanelWorkingOn.add(wlaWaehrung3, "growx");
		jPanelWorkingOn.add(wlaUST1, "growx");
		jPanelWorkingOn.add(wnfOffenUST, "growx");
		jPanelWorkingOn.add(wlaWaehrung4, "growx, wrap");
		
		jPanelWorkingOn.add(wlaGesOffen, "growx");
		jPanelWorkingOn.add(wnfGesOffen, "growx");
		jPanelWorkingOn.add(wlaWaehrung9, "growx");
		jPanelWorkingOn.add(wlaGesUST, "growx");
		jPanelWorkingOn.add(wnfGesOffenUST, "growx");
		jPanelWorkingOn.add(wlaWaehrung10, "growx, wrap");
		
		jPanelWorkingOn.add(wcbErledigt, "skip, growx");
		jPanelWorkingOn.add(wlaKurs, "skip, growx");
		jPanelWorkingOn.add(wnfKurs, "growx, wrap");
		
	}

	/**
	 * Befuellen der Comboboxen. Befuellung erfolgt nur beim ersten Aufruf
	 * 
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {
		zahlungsarten = DelegateFactory.getInstance()
				.getRechnungServiceDelegate().getAllZahlungsarten();

		Map<Object, Object> m = new TreeMap<Object, Object>(zahlungsarten);
		
		if (tabbedPane instanceof TabbedPaneGutschrift) {
			m.remove(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG);
			m.remove(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG);
		}
		if (!DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			m.remove(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG);
		}

		this.wcoZahlungsart.setMap(m);
	}

	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(e, bChangeKeyLockMeI, bNeedNoNewI);
		this.bankverbindungDto = null;
		this.kassenbuchDto = null;
		leereAlleFelder(this);
		clearStatusbar();
	}

	private void setDefaults() throws Throwable {
		// Rechnungswert
		Map<Object, Object> m = new TreeMap<Object,Object>(zahlungsarten);
		
		if (tabbedPane instanceof TabbedPaneGutschrift) {
			m.remove(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG);
			m.remove(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG);
		}
		if (!DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			m.remove(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG);
		}
		if(!isGutschriftErlaubt()) {
			m.remove(RechnungFac.ZAHLUNGSART_GUTSCHRIFT);
		}
		this.wcoZahlungsart.setMap(m);

		wnfWert.setBigDecimal(getWert());
		wnfWertUST.setBigDecimal(getWertUst());
		// bereits bezahlt
		setBezahltWerte();
		// Werte berechnen
		berechneOffen();
		// Waehrung
		wlaWaehrung1.setText(getWaehrung());
		wlaWaehrung2.setText(getWaehrung());
		wlaWaehrung3.setText(getWaehrung());
		wlaWaehrung4.setText(getWaehrung());
		wlaWaehrung5.setText(getWaehrung());
		wlaWaehrung6.setText(getWaehrung());
		wlaWaehrung7.setText(getWaehrung());
		wlaWaehrung8.setText(getWaehrung());
		wlaWaehrung9.setText(getWaehrung());
		wlaWaehrung10.setText(getWaehrung());
		wlaWaehrungAnz.setText(getWaehrung());
		wlaWaehrungAnzUst.setText(getWaehrung());

		if (getZahlungszielIId() != null) {
			zahlungszielDto = DelegateFactory.getInstance()
					.getMandantDelegate()
					.zahlungszielFindByPrimaryKey(getZahlungszielIId());
			// Bezeichnung
			if (zahlungszielDto != null) {
				wnfSkontoProzent1.setBigDecimal(zahlungszielDto
						.getSkontoProzentsatz1());
				if (zahlungszielDto.getZahlungszielsprDto() != null) {
					wtfZahlungsziel.setText(zahlungszielDto
							.getZahlungszielsprDto().getCBezeichnung());
				} else {
					wtfZahlungsziel.setText(zahlungszielDto.getCBez());
				}
			} else {
				wtfZahlungsziel.setText(null);
			}
			// Skontoziel 1
			boolean bSkonto1 = false;
			if (zahlungszielDto != null
					&& zahlungszielDto.getSkontoAnzahlTage1() != null
					&& zahlungszielDto.getSkontoAnzahlTage1().intValue() != 0) {
				wdfSkonto1.setDate(Helper.addiereTageZuDatum(getDBelegdatum(),
						zahlungszielDto.getSkontoAnzahlTage1().intValue()));
				bSkonto1 = true;
			} else {
				wdfSkonto1.setDate(null);
			}
			wlaSkonto1.setVisible(bSkonto1);
			wnfSkontoProzent1.setVisible(bSkonto1);
			wlaProzent1.setVisible(bSkonto1);
			wlaBis1.setVisible(bSkonto1);
			wdfSkonto1.setVisible(bSkonto1);
			wnfBetragSkonto1.setVisible(bSkonto1);
			wbuSkonto1uebernehmen.setVisible(bSkonto1);
			// Skontoziel 2
			boolean bSkonto2 = false;
			wnfSkontoProzent2.setBigDecimal(zahlungszielDto
					.getSkontoProzentsatz2());
			if (zahlungszielDto.getSkontoAnzahlTage2() != null
					&& zahlungszielDto.getSkontoAnzahlTage2().intValue() != 0) {
				wdfSkonto2.setDate(Helper.addiereTageZuDatum(getDBelegdatum(),
						zahlungszielDto.getSkontoAnzahlTage2().intValue()));
				bSkonto2 = true;
			} else {
				wdfSkonto2.setDate(null);
			}
			wlaSkonto2.setVisible(bSkonto2);
			wnfSkontoProzent2.setVisible(bSkonto2);
			wlaProzent2.setVisible(bSkonto2);
			wlaBis2.setVisible(bSkonto2);
			wdfSkonto2.setVisible(bSkonto2);
			wnfBetragSkonto2.setVisible(bSkonto2);
			wbuSkonto2uebernehmen.setVisible(bSkonto2);
			// Nettoziel
			if (zahlungszielDto.getAnzahlZieltageFuerNetto() != null) {
				wdfZieldatum.setDate(Helper.addiereTageZuDatum(
						getDBelegdatum(), zahlungszielDto
								.getAnzahlZieltageFuerNetto().intValue()));
			}
			berechneSkontovorschlaege();
		}
	}

	private void berechneSkontovorschlaege() throws ExceptionLP, Throwable {
		// Skontovorschlaege erstellen
		BigDecimal dSkonto1 = new BigDecimal(0);
		BigDecimal dSkonto2 = new BigDecimal(0);
		if (wnfSkontoProzent1.isVisible()) {
			dSkonto1 = wnfSkontoProzent1.getBigDecimal();
		}
		if (wnfSkontoProzent2.isVisible()) {
			dSkonto2 = wnfSkontoProzent2.getBigDecimal();
		}
		if (dSkonto1 != null) {
			BigDecimal bdSatz1 = new BigDecimal(1.0).subtract((dSkonto1.divide(
					new BigDecimal(100.0), 4, BigDecimal.ROUND_HALF_EVEN)));
			BigDecimal bdSkontobetr1 = getWert().multiply(bdSatz1);
			wnfBetragSkonto1.setBigDecimal(bdSkontobetr1.subtract(
					getWertBereitsBezahlt()).subtract(getWertAnzahlungen()));
		}
		if (dSkonto2 != null) {
			BigDecimal bdSatz2 = new BigDecimal(1.0).subtract((dSkonto2.divide(
					new BigDecimal(100.0), 4, BigDecimal.ROUND_HALF_EVEN)));
			BigDecimal bdSkontobetr2 = getWert().multiply(bdSatz2);
			wnfBetragSkonto2.setBigDecimal(bdSkontobetr2.subtract(
					getWertBereitsBezahlt()).subtract(getWertAnzahlungen()));
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		leereAlleFelder(this);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			if (key != null) { // new
				// Werte vorbelegen
				wdfDatum.setDate(new Date(System.currentTimeMillis()));
				setDefaults();
				// heute
				wdfDatum.setDate(new Date(System.currentTimeMillis()));
				if (key != null && key.equals(LPMain.getLockMeForNew())) {
					// Bankverbindung des Mandanten vorbesetzen
					MandantDto mandantDto = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mandantFindByPrimaryKey(
									LPMain.getTheClient().getMandant());
					if (mandantDto.getIBankverbindung() != null) {
						bankverbindungDto = DelegateFactory
								.getInstance()
								.getFinanzDelegate()
								.bankverbindungFindByPrimaryKey(
										mandantDto.getIBankverbindung());
						dto2ComponentsBankverbindung();
					}
					BigDecimal wert = getWertGesamtOffenExklusiveZahlung();
					wnfBetrag.setBigDecimal(wnfAnzahlungen.isVisible() ? wert
							.subtract(wnfAnzahlungen.getBigDecimal()) : wert);
					wert = getWertGesamtOffenUstExklusiveZahlung();
					wnfBetragUST
							.setBigDecimal(wnfAnzahlungen.isVisible() ? wert
									.subtract(wnfAnzahlungenUST.getBigDecimal())
									: wert);
				} else {
					wnfBetrag.setBigDecimal(new BigDecimal(0));
					wnfBetragUST.setBigDecimal(new BigDecimal(0));
				}
				berechneOffen();
				focusLostWnfBetrag();
				clearStatusbar();
			}
		} else {
			setDefaults();
			focusLostWnfBetrag();
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ZAHLUNGSART)) {
			setVisibilityOfComponents();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BANK)) {
			dialogQueryBank(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KASSENBUCH)) {
			dialogQueryKassenbuch(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VORAUSZAHLUNG)) {
			dialogQueryVorauszahlung(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SKONTO1)) {
			wnfBetrag.setBigDecimal(wnfBetragSkonto1.getBigDecimal());
			wcbErledigt.setSelected(true);
			focusLostWnfBetrag();
			berechneOffen();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SKONTO2)) {
			wnfBetrag.setBigDecimal(wnfBetragSkonto2.getBigDecimal());
			wcbErledigt.setSelected(true);
			focusLostWnfBetrag();
			berechneOffen();
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBank) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					bankverbindungDto = DelegateFactory.getInstance()
							.getFinanzDelegate()
							.bankverbindungFindByPrimaryKey((Integer) key);
					dto2ComponentsBankverbindung();
				}
			} else if (e.getSource() == panelQueryFLRKassenbuch) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					kassenbuchDto = DelegateFactory.getInstance()
							.getFinanzDelegate()
							.kassenbuchFindByPrimaryKey((Integer) key);
					dto2ComponentsKassenbuch();
				}
			} else if (e.getSource() == panelQueryFLRVorauszahlung) {
				Object key = panelQueryFLRVorauszahlung.getSelectedId();
				if (key != null) {
					buchungdetailDto = DelegateFactory.getInstance()
							.getBuchenDelegate()
							.buchungdetailFindByPrimaryKey((Integer) key);
					dto2ComponentsVorauszahlung();
				}
			}
		}
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {

			if (e.getSource() instanceof PanelQueryFLRGoto
					|| e.getSource() instanceof PanelQueryFLR) {

			} else {
				setBezahltWerte();
				focusLostWnfBetrag();
			}

		}
	}

	private void setBezahltWerte() throws ExceptionLP, Throwable {
		wnfBereitsBezahlt.setBigDecimal(getWertBereitsBezahlt());
		wnfBereitsBezahltMwst.setBigDecimal(getWertBereitsBezahltUst());
		wnfAnzahlungen.setBigDecimal(getWertAnzahlungen());
		wnfAnzahlungenUST.setBigDecimal(getWertAnzahlungenUst());
	}

	protected void dto2ComponentsBankverbindung() throws Throwable {
		if (bankverbindungDto != null) {
			wtfBankverbindungNummer
					.setText(bankverbindungDto.getCKontonummer());
			PartnerDto partner = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(bankverbindungDto.getBankIId());
			wtfBankverbindungBezeichnung.setText(partner
					.formatFixTitelName1Name2());
		} else {
			wtfBankverbindungNummer.setText(null);
			wtfBankverbindungBezeichnung.setText(null);
		}
	}

	protected void dto2ComponentsKassenbuch() {
		if (kassenbuchDto != null) {
			wtfKassenbuch.setText(kassenbuchDto.getCBez());
		} else {
			wtfKassenbuch.setText(null);
		}
	}

	protected void dto2ComponentsVorauszahlung() throws ExceptionLP, Throwable {
		enableToolsPanelButtons(false, ACTION_UPDATE);
		wnfBetrag.setBigDecimal(null);
		berechneOffen();
		if (buchungdetailDto != null) {
			wtfVorauszahlung.setText(DelegateFactory.getInstance()
					.getBuchenDelegate()
					.buchungFindByPrimaryKey(buchungdetailDto.getBuchungIId())
					.getCText());
			BuchungDto buchungDto = DelegateFactory.getInstance()
					.getBuchenDelegate()
					.buchungFindByPrimaryKey(buchungdetailDto.getBuchungIId());
			wdfDatum.setDate(buchungDto.getDBuchungsdatum());
			if (buchungdetailDto.getNBetrag().compareTo(
					wnfGesOffen.getBigDecimal()) > 0)
				wnfBetrag.setBigDecimal(getWertOffen());
			else
				wnfBetrag.setBigDecimal(buchungdetailDto.getNBetrag());
		} else {
			wtfVorauszahlung.setText(null);
		}
		berechneOffen();
	}

	protected void focusLostWnfBetrag() throws Throwable {
		Object key = getKeyWhenDetailPanel();
		if (key == null) {
			// nothing
		} else if (key.equals(LPMain.getLockMeForNew())
				|| this.getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {
			// den Betrag vom offenen abziehen
			BigDecimal betrag = wnfBetrag.getBigDecimal();
			BigDecimal bdOffenUst = getWertOffenUst();
			BigDecimal bdOffen = getWertOffen();
			if (betrag != null) {
				// die Umsatzsteuer anteilig berechnen
				if (bdOffen.compareTo(new BigDecimal(0)) != 0) {
					// wenn es noch einen offenen wert gibt
					int iNachkommastellen = 2;
					BigDecimal bdFaktor = betrag.divide(bdOffen, 10,
							BigDecimal.ROUND_HALF_EVEN);
					BigDecimal bdAnteiligeUst = bdOffenUst.multiply(bdFaktor);
					bdAnteiligeUst = Helper.rundeKaufmaennisch(bdAnteiligeUst,
							iNachkommastellen);
					wnfBetragUST.setBigDecimal(bdAnteiligeUst);
				} else {
					// kein offenener wert mehr -> sicherheitshalber die ganze
					// restliche ust
					wnfBetragUST.setBigDecimal(bdOffenUst);
				}
			} else {
				wnfBetragUST.setBigDecimal(new BigDecimal(0));
			}
		}
		berechneSkontovorschlaege();
		berechneOffen();
	}

	protected void wdfDatumFocusLost() throws Throwable {
		setDefaults();
	}

	protected void dialogQueryBank(ActionEvent e) throws Throwable {
		// ffcreatespanel: fuer eine dialogquery
		panelQueryFLRBank = FinanzFilterFactory.getInstance()
				.createPanelFLRBankverbindung(getInternalFrame(), false, false);
		new DialogQuery(panelQueryFLRBank);
	}

	protected void dialogQueryVorauszahlung(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;

		// TODO: Nur aktuelles Geschaeftsjahr? Jetzt erst mal alle

		List<FilterKriterium> filter = new ArrayList<FilterKriterium>(
				Arrays.asList(FinanzFilterFactory.getInstance()
						.createFKBuchungDetail(getKontoIIdAnzahlung())));
		filter.addAll(Arrays.asList(FinanzFilterFactory.getInstance()
				.createFKSchnellansicht()));

		filter.add(new FilterKriterium("buchungdetailart_c_nr", true, "'"
				+ getBuchungDetailAtCNr() + "'", FilterKriterium.OPERATOR_LIKE,
				false));
		filter.add(new FilterKriterium("flrbuchung.buchungsart_c_nr", true, "('"
				+ FinanzFac.BUCHUNGSART_UMBUCHUNG + "','"
				+ FinanzFac.BUCHUNGSART_BANKBUCHUNG + "')",
				FilterKriterium.OPERATOR_IN, false));
		filter.add(FinanzFilterFactory.getInstance().createFKVBuchungStorno());

		panelQueryFLRVorauszahlung = new PanelQueryFLR(qt,
				filter.toArray(new FilterKriterium[0]),
				QueryParameters.UC_ID_BUCHUNGDETAIL, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("rech.zahlung.vorauszahlung"));
		new DialogQuery(panelQueryFLRVorauszahlung);
	}

	protected abstract String getBuchungDetailAtCNr();

	protected void dialogQueryKassenbuch(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		FilterKriterium[] filters = SystemFilterFactory.getInstance()
				.createFKMandantCNr();
		panelQueryFLRKassenbuch = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_KASSENBUCH, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("finanz.liste.kassenbuecher"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKassenbuchbezeichnung();
		panelQueryFLRKassenbuch.befuellePanelFilterkriterienDirekt(fkDirekt1,
				null);
		new DialogQuery(panelQueryFLRKassenbuch);
	}

	private void setVisibilityOfComponents() {
		String zahlungsartCNr = (String) wcoZahlungsart.getKeyOfSelectedItem();
		if(zahlungsartCNr == null) return;
		boolean bIsBank = zahlungsartCNr
				.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BANK);
		boolean bIsGegenverrechnung = zahlungsartCNr
				.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG);
		boolean bIsGutschrift = zahlungsartCNr
				.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GUTSCHRIFT);
		boolean bIsKassenbuch = zahlungsartCNr
				.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BAR);
		boolean bIsVorauszahlung = zahlungsartCNr
				.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG);
		// bank
		wbuBankverbindung.setVisible(bIsBank);
		wtfBankverbindungBezeichnung.setVisible(bIsBank);
		wtfBankverbindungNummer.setVisible(bIsBank);
		wtfBankverbindungNummer.setMandatoryField(bIsBank);
		wlaAuszug.setVisible(bIsBank);
		wtnfAuszug.setVisible(bIsBank);
		wtnfAuszug.setMandatoryField(bIsBank);
		wtnfAuszug.setFractionDigits(0);
		// bar
		wbuKassenbuch.setVisible(bIsKassenbuch);
		wtfKassenbuch.setVisible(bIsKassenbuch);
		wtfKassenbuch.setMandatoryField(bIsKassenbuch);
		// gutschrift
		wbuGutschrift.setVisible(bIsGutschrift);
		wtfGutschrift.setVisible(bIsGutschrift);
		wtfGutschrift.setMandatoryField(bIsGutschrift);

		wbuVorauszahlung.setVisible(bIsVorauszahlung);
		wtfVorauszahlung.setVisible(bIsVorauszahlung);
		wtfVorauszahlung.setMandatoryField(bIsVorauszahlung);

		// gegenverrechnung
		wbuEingangsrechnung.setVisible(false);
		wtfEingangsrechnung.setVisible(false);
		wtfEingangsrechnung.setMandatoryField(false);
		wlaRechnung.setVisible(false);
		wtfRechnung.setVisible(false);

		if (this instanceof PanelRechnungZahlung) {
			wbuEingangsrechnung.setVisible(bIsGegenverrechnung);
			wtfEingangsrechnung.setVisible(bIsGegenverrechnung);
			wtfEingangsrechnung.setMandatoryField(bIsGegenverrechnung);
		} else {
			wlaRechnung.setVisible(bIsGegenverrechnung);
			wtfRechnung.setVisible(bIsGegenverrechnung);
		}

	}

	protected void berechneOffen() throws Throwable {
		BigDecimal bdOffen = getWert();
		if (bdOffen == null) {
			bdOffen = new BigDecimal(0);
		}
		if (wnfBereitsBezahlt.getBigDecimal() == null) {
			wnfBereitsBezahlt.setBigDecimal(new BigDecimal(0));
			wnfBereitsBezahltMwst.setBigDecimal(new BigDecimal(0));
		}
		bdOffen = bdOffen.subtract(wnfBereitsBezahlt.getBigDecimal());
		if (wnfAnzahlungen.isVisible())
			bdOffen = bdOffen.subtract(wnfAnzahlungen.getBigDecimal());
		if (wnfBetrag.getBigDecimal() != null) {
			bdOffen = bdOffen.subtract(wnfBetrag.getBigDecimal());
		}
		wnfOffen.setBigDecimal(bdOffen);

		BigDecimal bdOffenUST = getWertUst();
		if (bdOffenUST == null) {
			bdOffenUST = new BigDecimal(0);
		}
		bdOffenUST = bdOffenUST.subtract(wnfBereitsBezahltMwst.getBigDecimal());
		if (wnfAnzahlungenUST.isVisible())
			bdOffenUST = bdOffenUST.subtract(wnfAnzahlungenUST.getBigDecimal());
		if (wnfBetragUST.getBigDecimal() != null) {
			bdOffenUST = bdOffenUST.subtract(wnfBetragUST.getBigDecimal());
		}
		wnfOffenUST.setBigDecimal(bdOffenUST);

		if (wnfBetrag.getBigDecimal() == null) {
//			wnfBetrag.setBigDecimal(new BigDecimal(0));
			wnfBetragUST.setBigDecimal(new BigDecimal(0));
		}

		BigDecimal gesOffen = getWertGesamtOffenExklusiveZahlung().subtract(
				wnfBetrag.getBigDecimal()==null?BigDecimal.ZERO:wnfBetrag.getBigDecimal());

		BigDecimal gesOffenUst = getWertGesamtOffenUstExklusiveZahlung()
				.subtract(wnfBetragUST.getBigDecimal());

		if (wnfAnzahlungen.isVisible()) {
			gesOffen = gesOffen.subtract(wnfAnzahlungen.getBigDecimal());
			gesOffenUst = gesOffenUst.subtract(wnfAnzahlungenUST
					.getBigDecimal());
		}
		wnfGesOffen.setBigDecimal(gesOffen);
		wnfGesOffenUST.setBigDecimal(gesOffenUst);

		// das erledigt-hakerl
		if (isLocked()) {
			if (bdOffen.signum() == 0) {
				wcbErledigt.setSelected(true);
			} else {
				wcbErledigt.setSelected(false);
			}
		}
	}

	private BigDecimal getWertOffen() throws Throwable {
		return getWert().subtract(getWertBereitsBezahlt()).subtract(
				getWertAnzahlungen());
	}

	private BigDecimal getWertOffenUst() throws Throwable {
		return getWertUst().subtract(getWertBereitsBezahltUst()).subtract(
				getWertAnzahlungenUst());
	}

	protected abstract BigDecimal getWertGesamtOffenExklusiveZahlung()
			throws Throwable;

	protected abstract BigDecimal getWertGesamtOffenUstExklusiveZahlung()
			throws Throwable;

	protected abstract BigDecimal getWertBereitsBezahlt() throws Throwable;

	protected abstract BigDecimal getWertBereitsBezahltUst() throws Throwable;

	protected abstract BigDecimal getWertAnzahlungen() throws Throwable;

	protected abstract BigDecimal getWertAnzahlungenUst() throws Throwable;

	protected abstract BigDecimal getWert() throws Throwable;

	protected abstract BigDecimal getWertUst() throws Throwable;

	protected abstract String getWaehrung() throws Throwable;

	protected abstract Integer getZahlungszielIId() throws Throwable;

	protected abstract java.sql.Date getDBelegdatum();
	
	protected abstract boolean isGutschriftErlaubt();

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfDatum;
	}

	protected abstract Integer getKontoIIdAnzahlung() throws Throwable;
}

class PanelZahlung_focusAdapter implements java.awt.event.FocusListener {
	PanelZahlung adaptee;

	PanelZahlung_focusAdapter(PanelZahlung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.focusLostWnfBetrag();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}

class PanelZahlung_wdfDatumFocusAdapter implements FocusListener {
	PanelZahlung adaptee;

	PanelZahlung_wdfDatumFocusAdapter(PanelZahlung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wdfDatumFocusLost();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}
