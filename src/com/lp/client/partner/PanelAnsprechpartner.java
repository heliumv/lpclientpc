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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

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
import com.lp.client.frame.component.WrapperEmailField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/*
 * <p>Diese Klasse kuemmert sich um den Ansprechpartner</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum xx.12.04</p>
 * 
 * @author $Author: robert $
 * 
 * @version $Revision: 1.17 $ Date $Date: 2012/09/24 15:01:13 $
 */
abstract public class PanelAnsprechpartner extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = null;
	// private Border border = null;
	private JPanel panelButtonAction = null;
	private WrapperLabel wlaGueltigAb = null;
	private WrapperDateField wdfGueltigAb = null;
	private WrapperButton wbuAnsprechpartnerfunktion = null;
	private WrapperTextField wtfAnsprechpartnerfunktion = null;
	private WrapperGotoButton wbuAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerfunktion = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerAuswahl = null;
	private GridBagLayout gridBagLayoutAll = null;
	private WrapperLabel wlaBemerkung = null;
	private WrapperEditorField wefBemerkung = null;
	private WrapperLabel wlaDurchwahl = null;
	private WrapperTelefonField wtfDurchwahl = null;
	private WrapperLabel wlaEmail = null;
	private WrapperEmailField wtfEmail = null;
	private WrapperLabel wlaFaxdurchwahl = null;
	private WrapperTextField wtfFaxdurchwahl = null;
	private WrapperLabel wlaHandy = null;
	private WrapperTelefonField wtfHandy = null;
	private WrapperLabel wlaDirektfax = null;
	private WrapperTextField wtfDirektfax = null;
	private WrapperLabel wlaSort = null;
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
	private WrapperCheckBox wcbNewsletterEmpfaenger = new WrapperCheckBox();
	private WrapperLabel wlaFremdsystem = null;
	private WrapperTextField wtfFremdsystem = null;
	private WrapperButton wbuPasswort = null;

	private WrapperLabel wlaAbteilung = null;
	private WrapperTextField wtfAbteilung = null;

	protected WrapperTextNumberField wtfSort = null;

	protected WrapperComboBox wcoAnrede = null;
	protected WrapperLabel wlaTitel = null;
	protected WrapperTextField wtfTitel = null;
	protected WrapperLabel wlaNtitel = null;
	protected WrapperTextField wtfNtitel = null;
	protected WrapperLabel wlaVorname = null;
	protected WrapperTextField wtfVorname = null;
	protected WrapperLabel wlaGebDatum = null;
	protected WrapperDateField wdfGebDatum = null;

	static final public String ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION = "action_special_flr_ansprechpartner_funktion";
	static final public String ACTION_SPECIAL_FLR_ANSPRECHPARTNER = "action_special_flr_ansprechpartner";

	private static final String ACTION_SPECIAL_VCARD_EXPORT = "action_special_vcard_export";
	private static final String ACTION_SPECIAL_PASSWORD = "action_special_password";

	public PanelAnsprechpartner(InternalFrame internalFrame, String add2TitleI,
			Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// Buttons.
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		// setBorder(border);

		// Das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Ab hier Ansprechpartnerfelder.
		wlaGueltigAb = new WrapperLabel();
		LPMain.getInstance();
		wlaGueltigAb.setText(LPMain.getTextRespectUISPr("lp.gueltigab"));
		wdfGueltigAb = new WrapperDateField();
		wdfGueltigAb.setMandatoryFieldDB(true);

		wlaTitel = new WrapperLabel();
		wlaNtitel = new WrapperLabel();
		wlaVorname = new WrapperLabel();
		wlaGebDatum = new WrapperLabel();
		LPMain.getInstance();
		wlaTitel.setText(LPMain.getTextRespectUISPr("lp.titel"));
		LPMain.getInstance();
		wlaNtitel.setText(LPMain.getTextRespectUISPr("lp.ntitel"));
		LPMain.getInstance();
		wlaVorname = new WrapperLabel(LPMain.getTextRespectUISPr("lp.vorname"));
		wlaGebDatum.setText(LPMain
				.getTextRespectUISPr("pers.personalangehoerige.geburtsdatum"));

		wcbVersteckt.setText(LPMain.getTextRespectUISPr("lp.versteckt"));
		wcbNewsletterEmpfaenger
				.setText(LPMain
						.getTextRespectUISPr("part.ansprechpartner.newsletterempfaenger"));

		wlaAbteilung = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.abteilung"));
		wtfAbteilung = new WrapperTextField();

		wtfVorname = new WrapperTextField();
		wdfGebDatum = new WrapperDateField();
		wcoAnrede = new WrapperComboBox();
		wtfTitel = new WrapperTextField();
		wtfNtitel = new WrapperTextField();

		wtfAnsprechpartnerfunktion = new WrapperTextField();
		wtfAnsprechpartnerfunktion.setActivatable(false);
		wtfAnsprechpartnerfunktion.setMandatoryFieldDB(true);

		wbuAnsprechpartnerfunktion = new WrapperButton();
		wbuAnsprechpartnerfunktion.setText(LPMain
				.getTextRespectUISPr("part.ansprechpartner_funktion"));
		wbuAnsprechpartnerfunktion
				.setActionCommand(ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION);
		wbuAnsprechpartnerfunktion.addActionListener(this);

		// gotobutton: 3 Anstatt des WrapperButtons den WrapperGotoButton
		// verwenden
		wbuAnsprechpartner = new WrapperGotoButton(
				WrapperGotoButton.GOTO_PARTNER_AUSWAHL);
		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner.long"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_FLR_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setMandatoryFieldDB(true);

		wlaBemerkung = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.bemerkung"));
		wlaBemerkung.setVerticalAlignment(SwingConstants.NORTH);
		wefBemerkung = new WrapperEditorFieldTexteingabe(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.bemerkung"));

		wlaDurchwahl = new WrapperLabel();
		wlaDurchwahl.setText(LPMain.getTextRespectUISPr("lp.durchwahl"));
		wlaDurchwahl.setMinimumSize(new Dimension(170, Defaults.getInstance()
				.getControlHeight()));
		wlaDurchwahl.setPreferredSize(new Dimension(170, Defaults.getInstance()
				.getControlHeight()));

		wtfDurchwahl = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfDurchwahl.setIstAnsprechpartner(true);
		wtfDurchwahl.setMinimumSize(new Dimension(170, Defaults.getInstance()
				.getControlHeight()));
		wtfDurchwahl.setPreferredSize(new Dimension(170, Defaults.getInstance()
				.getControlHeight()));

		wlaEmail = new WrapperLabel();
		LPMain.getInstance();
		wlaEmail.setText(LPMain.getTextRespectUISPr("lp.email"));
		wlaEmail.setMinimumSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wlaEmail.setPreferredSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wtfEmail = new WrapperEmailField();

		LPMain.getInstance();
		wlaFremdsystem = new WrapperLabel(
				LPMain.getTextRespectUISPr("part.ansprechpartner.fremdsystem"));
		wtfFremdsystem = new WrapperTextField(30);

		wlaFaxdurchwahl = new WrapperLabel();
		LPMain.getInstance();
		wlaFaxdurchwahl = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.faxdurchwahl"));
		wtfFaxdurchwahl = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaHandy = new WrapperLabel();
		LPMain.getInstance();
		wlaHandy = new WrapperLabel(LPMain.getTextRespectUISPr("lp.handy"));
		wtfHandy = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);

		wlaDirektfax = new WrapperLabel();
		LPMain.getInstance();
		wlaDirektfax = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.direktfax"));
		wtfDirektfax = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaSort = new WrapperLabel();
		LPMain.getInstance();
		wlaSort = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.sortierung"));
		wtfSort = new WrapperTextNumberField();
		wtfSort.setMandatoryFieldDB(true);
		wtfSort.setMinimumValue(new Integer(0));
		wtfSort.setMaximumDigits(4);
		wtfSort.setMaximumValue(new Integer(9999));

		wbuPasswort = new WrapperButton(
				LPMain.getTextRespectUISPr("part.ansprechpartner.passwort"));
		wbuPasswort.addActionListener(this);
		wbuPasswort.setActionCommand(ACTION_SPECIAL_PASSWORD);

		jpaWorkingOn = new JPanel(new MigLayout("wrap 8",
				"[25%][10%][20%][10%][10%][10%][20%]"));

		// Actionpanel von Oberklasse holen und einhaengen.
		panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Ab hier einhaengen.
		// Zeile

		jpaWorkingOn.add(wbuAnsprechpartner, "growx");
		jpaWorkingOn.add(wcoAnrede, "growx, width 70:70:70");
		jpaWorkingOn.add(wtfAnsprechpartner, "growx");
		jpaWorkingOn.add(wlaGebDatum, "growx, width 70:70:70");
		jpaWorkingOn.add(wdfGebDatum, "growx");
		jpaWorkingOn.add(wlaGueltigAb, "growx");
		jpaWorkingOn.add(wdfGueltigAb, "growx, span2");

		jpaWorkingOn.add(wcbNewsletterEmpfaenger, "growx");
		jpaWorkingOn.add(wlaVorname, "growx");
		jpaWorkingOn.add(wtfVorname, "growx");
		jpaWorkingOn.add(wlaTitel, "growx");
		jpaWorkingOn.add(wtfTitel, "growx");
		jpaWorkingOn.add(wlaNtitel, "growx, width 50:50:50");
		jpaWorkingOn.add(wtfNtitel, "growx, span2");

		jpaWorkingOn.add(wbuAnsprechpartnerfunktion, "growx");
		jpaWorkingOn.add(wtfAnsprechpartnerfunktion, "growx, span 2");
		jpaWorkingOn.add(wlaAbteilung, "growx");
		jpaWorkingOn.add(wtfAbteilung, "growx, span 3, wrap");

		jpaWorkingOn.add(wlaBemerkung, "top, growx");
		jpaWorkingOn.add(wefBemerkung, "grow, span");
		iZeile++;
		jpaWorkingOn.add(wlaDurchwahl, "growx");
		jpaWorkingOn.add(wtfDurchwahl, "growx, span 2");
		jpaWorkingOn.add(wlaEmail, "growx, span 2");
		jpaWorkingOn.add(wtfEmail, "growx, span");
		iZeile++;
		jpaWorkingOn.add(wlaFaxdurchwahl, "growx");
		jpaWorkingOn.add(wtfFaxdurchwahl, "growx, span 2");
		jpaWorkingOn.add(wlaHandy, "growx, span 2");
		jpaWorkingOn.add(wtfHandy, "growx, span");
		iZeile++;
		jpaWorkingOn.add(wlaDirektfax, "growx");
		jpaWorkingOn.add(wtfDirektfax, "growx, span 2");
		jpaWorkingOn.add(wlaFremdsystem, "growx, span 2");
		jpaWorkingOn.add(wtfFremdsystem, "growx, span");
		iZeile++;
		jpaWorkingOn.add(wlaSort, "top, growx");
		jpaWorkingOn.add(wtfSort, "top, growx, span 2");

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbVersteckt, "skip 2, growx, width 80:80:80");
		} else {
			jpaWorkingOn.add(new WrapperLabel(""), "skip 2, growx");
		}

		jpaWorkingOn.add(wbuPasswort, "growx");

		// Zeile

		createAndSaveAndShowButton("/com/lp/client/res/book_open2.png",
				LPMain.getTextRespectUISPr("part.partner.export.vcard"),
				ACTION_SPECIAL_VCARD_EXPORT, null);

	}

	protected void speicherePartner() throws Throwable {

		// Wenn Partner manuell eingegeben wurde, dann vorher anlegen
		if (getAnsprechpartnerDto().getPartnerIIdAnsprechpartner() == null) {
			PartnerDto partnerDto = new PartnerDto();
			partnerDto.setCName1nachnamefirmazeile1(wtfAnsprechpartner
					.getText());
			partnerDto.setCName2vornamefirmazeile2(wtfVorname.getText());
			partnerDto.setCTitel(wtfTitel.getText());
			partnerDto.setCNtitel(wtfNtitel.getText());
			partnerDto.setAnredeCNr((String) wcoAnrede.getKeyOfSelectedItem());
			partnerDto.setDGeburtsdatumansprechpartner(wdfGebDatum.getDate());
			partnerDto.setPartnerartCNr(PartnerFac.PARTNERART_ANSPRECHPARTNER);
			partnerDto.setBVersteckt(com.lp.util.Helper.boolean2Short(false));
			LPMain.getInstance();
			partnerDto.setLocaleCNrKommunikation(LPMain.getTheClient()
					.getLocUiAsString());
			String kbez = wtfAnsprechpartner.getText();
			if (kbez.length() > 14) {
				kbez = kbez.substring(0, 13);
			}
			partnerDto.setCKbez(kbez);
			getAnsprechpartnerDto().setPartnerIIdAnsprechpartner(
					DelegateFactory.getInstance().getPartnerDelegate()
							.createPartner(partnerDto));
		}

	}

	protected abstract String getLockMeWer() throws Exception;

	/**
	 * Behandle Ereignis Neu.
	 * 
	 * @param eventObject
	 *            Ereignis.
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	final public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		if (!bNeedNoNewI) {
			setAnsprechpartnerDto(new AnsprechpartnerDto());

			setDefaults();
		}
	}

	final public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);
		
		wbuPasswort.setBackground(UIManager
				.getColor("Button.background"));

		// Die normale Telefon/Faxnummer vor der Durchwajl anzeigen
		PartnerDto dto = null;
		if (getInternalFrame() instanceof InternalFrameLieferant) {
			dto = ((InternalFrameLieferant) getInternalFrame())
					.getLieferantDto().getPartnerDto();
		} else if (getInternalFrame() instanceof InternalFrameKunde) {
			dto = ((InternalFrameKunde) getInternalFrame()).getKundeDto()
					.getPartnerDto();
		} else if (getInternalFrame() instanceof InternalFramePartner) {
			dto = ((InternalFramePartner) getInternalFrame()).getTpPartner()
					.getPartnerDto();
		}

		String cTelefon = DelegateFactory
				.getInstance()
				.getPartnerDelegate()
				.enrichNumber(dto.getIId(),
						PartnerFac.KOMMUNIKATIONSART_TELEFON, null, true);

		if (cTelefon != null) {

			LPMain.getInstance();
			wlaDurchwahl.setText(LPMain.getTextRespectUISPr("lp.durchwahl")
					+ " (" + cTelefon + ")");
		} else {
			LPMain.getInstance();
			wlaDurchwahl.setText(LPMain.getTextRespectUISPr("lp.durchwahl"));
		}

		cTelefon = DelegateFactory
				.getInstance()
				.getPartnerDelegate()
				.enrichNumber(dto.getIId(), PartnerFac.KOMMUNIKATIONSART_FAX,
						null, true);

		if (cTelefon != null) {

			LPMain.getInstance();
			wlaFaxdurchwahl.setText(LPMain
					.getTextRespectUISPr("lp.faxdurchwahl")
					+ " ("
					+ cTelefon
					+ ")");
		} else {
			LPMain.getInstance();
			wlaFaxdurchwahl.setText(LPMain
					.getTextRespectUISPr("lp.faxdurchwahl"));
		}

		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();

			setAnsprechpartnerDto(new AnsprechpartnerDto());

			if (key == null
					|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

				leereAlleFelder(this);
				setDefaults();
				clearStatusbar();
				if (key != null && key.equals(LPMain.getLockMeForNew())) {
					wtfAnsprechpartner.setActivatable(true);
					wtfAnsprechpartner.setEditable(true);
					wtfVorname.setActivatable(true);
					wtfVorname.setEditable(true);
					wtfTitel.setActivatable(true);
					wtfTitel.setEditable(true);
					wtfNtitel.setActivatable(true);
					wtfNtitel.setEditable(true);
					wdfGebDatum.setActivatable(true);
					wdfGebDatum.setEnabled(true);
					wcoAnrede.setActivatable(true);
					wcoAnrede.setEnabled(true);
				}
			} else {
				wtfAnsprechpartner.setActivatable(false);
				wtfAnsprechpartner.setEditable(false);
				wtfVorname.setActivatable(false);
				wtfVorname.setEditable(false);
				wtfTitel.setActivatable(false);
				wtfTitel.setEditable(false);
				wtfNtitel.setActivatable(false);
				wtfNtitel.setEditable(false);
				wdfGebDatum.setActivatable(false);
				wdfGebDatum.setEditable(false);
				wcoAnrede.setActivatable(false);
				wcoAnrede.setEditable(false);

				setAnsprechpartnerDto(DelegateFactory.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) key));

				setStatusbar();

				dto2Components(dto);
			}

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getSelectedPartnerTitelAnrede());
		}
	}

	protected abstract String getSelectedPartnerTitelAnrede();

	final protected void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getPartnerDto().getPersonalIIdAnlegen());
		setStatusbarPersonalIIdAendern(getPartnerDto().getPersonalIIdAnlegen());
		setStatusbarTAendern(getPartnerDto().getTAendern());
		setStatusbarTAnlegen(getPartnerDto().getTAnlegen());
	}

	protected void setDefaults() throws Throwable {

		getPartnerDto().setPartnerartCNr(PartnerFac.PARTNERART_ANSPRECHPARTNER);
		wdfGueltigAb.setDate(new java.sql.Date(System.currentTimeMillis()));

		LPMain.getInstance();
		Map<?, ?> tmAnreden = (SortedMap<?, ?>) DelegateFactory.getInstance()
				.getPartnerDelegate()
				.getAllAnreden(LPMain.getTheClient().getLocUi());
		wcoAnrede.setMap(tmAnreden);
		wcoAnrede.setKeyOfSelectedItem(PartnerFac.PARTNER_ANREDE_HERR);

	}

	abstract protected PartnerDto getPartnerDto();

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ANSPRECHPARTNER)) {

			String[] aWhichButtonIUse = null;

			QueryType[] querytypes = null;
			LPMain.getInstance();
			panelQueryFLRAnsprechpartnerAuswahl = new PanelQueryFLR(querytypes,
					null, QueryParameters.UC_ID_PARTNER, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("button.ansprechpartner.long"));

			panelQueryFLRAnsprechpartnerAuswahl
					.befuellePanelFilterkriterienDirekt(PartnerFilterFactory
							.getInstance().createFKDPartnerName(),
							PartnerFilterFactory.getInstance()
									.createFKDPartnerLandPLZOrt());

			new DialogQuery(panelQueryFLRAnsprechpartnerAuswahl);
		}

		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION)) {
			String[] aWhichButtonIUse = null;
			QueryType[] querytypes = null;
			FilterKriterium[] filters = null;
			LPMain.getInstance();
			panelQueryFLRAnsprechpartnerfunktion = new PanelQueryFLR(
					querytypes, filters,
					QueryParameters.UC_ID_ANSPRECHPARTNERFUNKTION,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("part.ansprechpartner_funktion"));
			new DialogQuery(panelQueryFLRAnsprechpartnerfunktion);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VCARD_EXPORT)) {
			HelperClient.vCardAlsDateiExportieren(
					DelegateFactory
							.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey(
									getAnsprechpartnerDto().getPartnerIId()),
					getAnsprechpartnerDto());
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PASSWORD)) {
			// Passwort Dialog erstellen
			DialogAnsprechpartnerPasswort d = new DialogAnsprechpartnerPasswort(
					getAnsprechpartnerDto());
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

			if (getAnsprechpartnerDto().getCKennwort() != null) {
				wbuPasswort.setBackground(new Color(0, 200, 0));

			} else {
				wbuPasswort.setBackground(UIManager
						.getColor("Button.background"));
			}

		}

	}

	@Override
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (wcbNewsletterEmpfaenger.isSelected()
				&& !Helper.validateEmailadresse(wtfEmail.getText())) {
			showDialogEmailAusfuellen();
			return;
		}
		if (allMandatoryFieldsSetDlg()) {
			eventActionSaveImpl(e, bNeedNoSaveI);
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	public abstract void eventActionSaveImpl(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable;

	private void showDialogEmailAusfuellen() {
		DialogFactory
				.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("part.ansprechpartner.emailfuernewsletternoetig"));
	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRAnsprechpartnerfunktion) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				getAnsprechpartnerDto().setAnsprechpartnerfunktionIId(iId);

				AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = null;
				if (iId != null) {
					ansprechpartnerfunktionDto = DelegateFactory.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerfunktionFindByPrimaryKey(iId);
					wtfAnsprechpartnerfunktion
							.setText(ansprechpartnerfunktionDto
									.getBezeichnung());
				}
			} else if (e.getSource() == panelQueryFLRAnsprechpartnerAuswahl) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					getAnsprechpartnerDto().setPartnerIIdAnsprechpartner(key);
					PartnerDto partnerDto = null;
					partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate().partnerFindByPrimaryKey(key);
					wtfAnsprechpartner.setText(partnerDto
							.getCName1nachnamefirmazeile1());
					wtfVorname
							.setText(partnerDto.getCName2vornamefirmazeile2());
					wtfTitel.setText(partnerDto.getCTitel());
					wtfNtitel.setText(partnerDto.getCNtitel());
					wcoAnrede.setKeyOfSelectedItem(partnerDto.getAnredeCNr());
					wdfGebDatum.setDate(partnerDto
							.getDGeburtsdatumansprechpartner());

				}
			}
		}
	}

	protected void dto2Components(PartnerDto dto) throws Throwable {

		wefBemerkung.setText(getAnsprechpartnerDto().getXBemerkung());

		if (getAnsprechpartnerDto().getAnsprechpartnerfunktionIId() != null) {
			AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerfunktionFindByPrimaryKey(
							getAnsprechpartnerDto()
									.getAnsprechpartnerfunktionIId());
			wtfAnsprechpartnerfunktion.setText(ansprechpartnerfunktionDto
					.getBezeichnung());
		} else {
			wtfAnsprechpartnerfunktion.setText(null);
		}

		PartnerDto partnerDto = DelegateFactory
				.getInstance()
				.getPartnerDelegate()
				.partnerFindByPrimaryKey(
						getAnsprechpartnerDto().getPartnerIIdAnsprechpartner());
		wtfAnsprechpartner.setText(partnerDto.getCName1nachnamefirmazeile1());

		// gotobutton: 4 Den Key des Datensatzes jedesmal zuordnen
		wbuAnsprechpartner.setOKey(partnerDto.getIId());

		wtfVorname.setText(partnerDto.getCName2vornamefirmazeile2());
		wtfTitel.setText(partnerDto.getCTitel());
		wtfNtitel.setText(partnerDto.getCNtitel());
		wcoAnrede.setKeyOfSelectedItem(partnerDto.getAnredeCNr());
		wdfGebDatum.setDate(partnerDto.getDGeburtsdatumansprechpartner());

		AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerDto();

		wcbVersteckt.setShort(ansprechpartnerDto.getBVersteckt());
		wtfAbteilung.setText(ansprechpartnerDto.getCAbteilung());
		wdfGueltigAb.setDate(getAnsprechpartnerDto().getDGueltigab());

		// Partnerkommunikation

		wtfDirektfax.setText(getAnsprechpartnerDto().getCDirektfax());

		if (getAnsprechpartnerDto().getCEmail() != null) {
			wtfEmail.setEmail(getAnsprechpartnerDto().getCEmail(),
					getAnsprechpartnerDto());
		} else {
			wtfEmail.setEmail(null, null);
		}

		wtfFaxdurchwahl.setText(getAnsprechpartnerDto().getCFax());

		wtfDurchwahl.setPartnerKommunikationDto(dto, getAnsprechpartnerDto()
				.getCTelefon());

		if (getAnsprechpartnerDto().getCHandy() != null) {
			wtfHandy.setPartnerKommunikationDto(getAnsprechpartnerDto()
					.getPartnerDto(), getAnsprechpartnerDto().getCHandy());
		} else {
			wtfHandy.setPartnerKommunikationDto(null, null);
		}

		wtfSort.setInteger(ansprechpartnerDto.getISort());
		wtfFremdsystem.setText(ansprechpartnerDto.getCFremdsystemnr());

		wcbNewsletterEmpfaenger.setSelected(ansprechpartnerDto
				.isNewsletterEmpfaenger());
		
		
		if (getAnsprechpartnerDto().getCKennwort() != null) {
			wbuPasswort.setBackground(new Color(0, 200, 0));

		} else {
			wbuPasswort.setBackground(UIManager
					.getColor("Button.background"));
		}
		
	}

	abstract public AnsprechpartnerDto getAnsprechpartnerDto();

	abstract protected void setAnsprechpartnerDto(
			AnsprechpartnerDto ansprechpartnerDtoI);

	protected void components2Dto() throws Throwable {

		getAnsprechpartnerDto().setDGueltigab(wdfGueltigAb.getDate());
		getAnsprechpartnerDto().setXBemerkung(wefBemerkung.getText());
		getAnsprechpartnerDto().setISort(wtfSort.getInteger());
		getAnsprechpartnerDto().setBVersteckt(wcbVersteckt.getShort());
		getAnsprechpartnerDto().setCFremdsystemnr(wtfFremdsystem.getText());
		// Partnerkommunikation.

		getAnsprechpartnerDto().setCDirektfax(wtfDirektfax.getText());

		getAnsprechpartnerDto().setCTelefon(wtfDurchwahl.getText());

		getAnsprechpartnerDto().setCEmail(wtfEmail.getText());

		getAnsprechpartnerDto().setCFax(wtfFaxdurchwahl.getText());

		getAnsprechpartnerDto().setCHandy(wtfHandy.getText());
		getAnsprechpartnerDto().setNewsletterEmpfaenger(
				wcbNewsletterEmpfaenger.isSelected());
		getAnsprechpartnerDto().setCAbteilung(wtfAbteilung.getText());

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuAnsprechpartner;
	}

}
