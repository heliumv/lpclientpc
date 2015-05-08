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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um die Mandantdaten.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>08.03.05</I></p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.7 $
 */
public class PanelMandantKonditionen extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperTextField wtfPreisliste = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = null;
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaMandant = null;
	private WrapperTextField wtfMandant = null;
	private WrapperLabel wlaKurzbez = null;
	private WrapperTextField wtfKurzbez = null;
	private WrapperLabel wlaIstHauptmandant = null;
	private WrapperCheckBox wcbIstHauptmandant = null;
	private WrapperButton wbuLieferartKunde = null;
	private WrapperButton wbuSpediteurKunde = null;
	private PanelQueryFLR panelQueryFLRSpediteurKunde = null;
	private PanelQueryFLR panelQueryFLRLieferartKunde = null;
	private PanelQueryFLR panelQueryFLRLieferartLF = null;
	private WrapperTextField wtfLieferartKunde = null;
	private WrapperTextField wtfLieferartLF = null;
	private WrapperTextField wtfSpediteurKunde = null;
	private WrapperButton wbuZahlungszielKunde = null;
	private WrapperTextField wtfZahlungszielKunde = null;
	private PanelQueryFLR panelQueryFLRZahlungszielKunde = null;
	private WrapperButton wbuPreislisteFLR = null;
	private WrapperTextField wtfKostenstelleKunde = null;
	private WrapperLabel wlaKundenvorbelegungen = null;
	private WrapperLabel wlaLFvorbelegungen = null;
	private WrapperLabel wlaVorbelegungen = null;

	static final private String ACTION_SPECIAL_FLR_LIEFERART_KUNDE = "action_special_flr_lieferart_kunde";
	static final private String ACTION_SPECIAL_FLR_LIEFERART_LF = "action_special_flr_lieferart_lf";
	static final private String ACTION_SPECIAL_FLR_SPEDITEUR_KUNDE = "action_special_flr_spediteur_kunde";
	static final private String ACTION_SPECIAL_FLR_SPEDITEUR_LF = "action_special_flr_spediteur_lf";
	static final private String ACTION_SPECIAL_FLR_ZAHLUNGSZIEL_KUNDE = "action_special_flr_zahlungsziel_kunde";
	static final private String ACTION_SPECIAL_FLR_ZAHLUNGSZIEL_LF = "action_special_flr_zahlungsziel_lf";
	static final private String ACTION_SPECIAL_FLR_PREISLISTEN = "action_special_flr_preislisten";
	static final private String ACTION_SPECIAL_FLR_KOSTENSTELLE = "action_special_flr_kostenstelle";
	static final private String ACTION_SPECIAL_FLR_MWSTSATZ = "action_special_flr_mwstsatz";
	static final private String ACTION_SPECIAL_FLR_MWSTSATZAUSLAND = "action_special_flr_mwstsatzausland";
	static final private String ACTION_SPECIAL_FLR_MWSTSATZDRITTLAND = "action_special_flr_mwstsatzdrittland";
	private static final String ACTION_SPECIAL_BANK = "action_special_bank";
	private static final String ACTION_SPECIAL_LIEFERADRESSE = "action_special_lieferadresse";

	private PanelQueryFLR panelQueryFLRPreislisten = null;

	private PanelQueryFLR panelQueryFLRMWSTSatz = null;
	private WrapperButton wbuMWSTSatz = null;
	private WrapperTextField wtfMWSTSatz = null;

	private PanelQueryFLR panelQueryFLRMWSTSatzAusland = null;
	private WrapperButton wbuMWSTSatzAusland = null;
	private WrapperTextField wtfMWSTSatzAusland = null;

	private PanelQueryFLR panelQueryFLRMWSTSatzDrittland = null;
	private WrapperButton wbuMWSTSatzDrittland = null;
	private WrapperTextField wtfMWSTSatzDrittland = null;

	private WrapperButton wbuKostenstelleFLR = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;

	private WrapperButton wbuBankverbindung = null;
	private WrapperTextField wtfBankverbindungNummer = null;
	private WrapperTextField wtfBankverbindungBezeichnung = null;
	private PanelQueryFLR panelQueryFLRBank = null;

	private WrapperButton wbuLieferadresse = null;
	private WrapperTextField wtfLieferadresse = null;
	private PanelQueryFLR panelQueryLieferadresse = null;

	private WrapperButton wbuLieferartLF = null;
	private WrapperButton wbuSpediteurLF = null;
	private WrapperTextField wtfSpediteurLF = null;
	private PanelQueryFLR panelQueryFLRSpediteurLF = null;
	private WrapperButton wbuZahlungszielLF = null;
	private WrapperTextField wtfZahlungszielLF = null;
	private PanelQueryFLR panelQueryFLRZahlungszielLF = null;

	public PanelMandantKonditionen(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {
		super(internalFrame, add2TitleI, keyI);
		jbInit();
		initComponents();
	}

	private void initPanel() throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRLieferartKunde) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LieferartDto lieferart2Dto = DelegateFactory.getInstance()
						.getLocaleDelegate().lieferartFindByPrimaryKey(iId);
				wtfLieferartKunde.setText(lieferart2Dto.getCNr());

				getMandantDto().setLieferartIIdKunde(iId);
			}

			else if (e.getSource() == panelQueryFLRLieferartLF) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LieferartDto lieferart2Dto = DelegateFactory.getInstance()
						.getLocaleDelegate().lieferartFindByPrimaryKey(iId);
				wtfLieferartLF.setText(lieferart2Dto.getCNr());

				getMandantDto().setLieferartIIdLF(iId);
			}

			else if (e.getSource() == panelQueryFLRSpediteurKunde) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				SpediteurDto spediteurDto = DelegateFactory.getInstance()
						.getMandantDelegate().spediteurFindByPrimaryKey(iId);
				wtfSpediteurKunde.setText(spediteurDto.getCNamedesspediteurs());

				getMandantDto().setSpediteurIIdKunde(iId);
			}

			else if (e.getSource() == panelQueryFLRSpediteurLF) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				SpediteurDto spediteurDto = DelegateFactory.getInstance()
						.getMandantDelegate().spediteurFindByPrimaryKey(iId);
				wtfSpediteurLF.setText(spediteurDto.getCNamedesspediteurs());

				getMandantDto().setSpediteurIIdLF(iId);
			}

			else if (e.getSource() == panelQueryFLRPreislisten) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				getMandantDto().setVkpfArtikelpreislisteIId(iId);

				setWtfPreisliste(iId);
			}

			else if (e.getSource() == panelQueryFLRKostenstelle) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				getMandantDto().setIIdKostenstelle(iId);

				KostenstelleDto k = DelegateFactory.getInstance()
						.getSystemDelegate().kostenstelleFindByPrimaryKey(iId);
				wtfKostenstelleKunde.setText(k.getCNr());
			}

			else if (e.getSource() == panelQueryFLRZahlungszielKunde) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				getMandantDto().setZahlungszielIIdKunde(iId);

				ZahlungszielDto zahlungszielDto = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.zahlungszielFindByPrimaryKey(
								getMandantDto().getZahlungszielIIdKunde());
				wtfZahlungszielKunde.setText(zahlungszielDto.getCBez());
			}

			else if (e.getSource() == panelQueryFLRZahlungszielLF) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				getMandantDto().setZahlungszielIIdLF(iId);

				ZahlungszielDto zahlungszielDto = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.zahlungszielFindByPrimaryKey(
								getMandantDto().getZahlungszielIIdLF());
				wtfZahlungszielLF.setText(zahlungszielDto.getCBez());
			} else if (e.getSource() == panelQueryFLRMWSTSatz) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				MwstsatzbezDto mwstbezDto = DelegateFactory.getInstance()
						.getMandantDelegate().mwstsatzbezFindByPrimaryKey(iId);
				wtfMWSTSatz.setText(mwstbezDto.getCBezeichnung());
				getMandantDto().setMwstsatzbezIIdStandardinlandmwstsatz(iId);
			} else if (e.getSource() == panelQueryFLRMWSTSatzAusland) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				MwstsatzbezDto mwstbezDto = DelegateFactory.getInstance()
						.getMandantDelegate().mwstsatzbezFindByPrimaryKey(iId);
				wtfMWSTSatzAusland.setText(mwstbezDto.getCBezeichnung());
				getMandantDto().setMwstsatzbezIIdStandardauslandmwstsatz(iId);
			} else if (e.getSource() == panelQueryFLRMWSTSatzDrittland) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				MwstsatzbezDto mwstbezDto = DelegateFactory.getInstance()
						.getMandantDelegate().mwstsatzbezFindByPrimaryKey(iId);
				wtfMWSTSatzDrittland.setText(mwstbezDto.getCBezeichnung());
				getMandantDto().setMwstsatzbezIIdStandarddrittlandmwstsatz(iId);
			} else if (e.getSource() == panelQueryFLRBank) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					BankverbindungDto bankverbindungDto = DelegateFactory
							.getInstance().getFinanzDelegate()
							.bankverbindungFindByPrimaryKey((Integer) key);
					wtfBankverbindungNummer.setText(bankverbindungDto
							.getCKontonummer());
					PartnerDto partner = DelegateFactory
							.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey(
									bankverbindungDto.getBankIId());
					wtfBankverbindungBezeichnung.setText(partner
							.formatFixTitelName1Name2());
				}
				getMandantDto().setIBankverbindung((Integer) key);
			} else if (e.getSource() == panelQueryLieferadresse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PartnerDto partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey((Integer) key);
					wtfLieferadresse.setText(partnerDto.formatAnrede());
				}
				getMandantDto().setPartnerIIdLieferadresse((Integer) key);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRBank) {
				wtfBankverbindungNummer.setText(null);
				wtfBankverbindungBezeichnung.setText(null);
				getMandantDto().setIBankverbindung(null);
			} else if (e.getSource() == panelQueryFLRMWSTSatzAusland) {
				getMandantDto().setMwstsatzbezIIdStandardauslandmwstsatz(null);
				wtfMWSTSatzAusland.setText(null);
			} else if (e.getSource() == panelQueryFLRMWSTSatzDrittland) {
				getMandantDto()
						.setMwstsatzbezIIdStandarddrittlandmwstsatz(null);
				wtfMWSTSatzDrittland.setText(null);
			} else if (e.getSource() == panelQueryFLRMWSTSatz) {
				getMandantDto().setMwstsatzbezIIdStandardinlandmwstsatz(null);
				wtfMWSTSatz.setText(null);
			}
		}
	}

	private void dialogQueryBank(ActionEvent e) throws Throwable {
		// fuer eine dialogquery
		panelQueryFLRBank = FinanzFilterFactory.getInstance()
				.createPanelFLRBankverbindung(getInternalFrame(), false, true);
		if (getMandantDto().getIBankverbindung() != null) {
			panelQueryFLRBank.setSelectedId(getMandantDto()
					.getIBankverbindung());
		}
		new DialogQuery(panelQueryFLRBank);
	}

	private void jbInit() throws Throwable {
		// von hier ...
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();

		add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// jetzt meine Felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		wlaMandant = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant"));
		wtfMandant = new WrapperTextField();
		wtfMandant.setMandatoryFieldDB(true);
		wtfMandant.setActivatable(false);

		wtfKurzbez = new WrapperTextField(MandantFac.MAX_KBEZ);
		wlaKurzbez = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.mandantbezeichnung"));
		wtfKurzbez.setMandatoryFieldDB(true);

		wlaIstHauptmandant = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.ist_hautpmandant"));
		wcbIstHauptmandant = new WrapperCheckBox();

		getInternalFrame().addItemChangedListener(this);

		wbuLieferartKunde = new WrapperButton();
		wbuLieferartKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferart"));
		wbuLieferartKunde.setActionCommand(ACTION_SPECIAL_FLR_LIEFERART_KUNDE);
		wbuLieferartKunde.addActionListener(this);
		wtfLieferartKunde = new WrapperTextField();
		wtfLieferartKunde.setActivatable(false);
		wtfLieferartKunde.setMandatoryField(true);

		wbuLieferartLF = new WrapperButton();
		wbuLieferartLF.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferart"));
		wbuLieferartLF.setActionCommand(ACTION_SPECIAL_FLR_LIEFERART_LF);
		wbuLieferartLF.addActionListener(this);
		wtfLieferartLF = new WrapperTextField();
		wtfLieferartLF.setActivatable(false);
		wtfLieferartLF.setMandatoryField(true);

		wbuSpediteurKunde = new WrapperButton();
		wbuSpediteurKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.spediteur"));
		wbuSpediteurKunde.setActionCommand(ACTION_SPECIAL_FLR_SPEDITEUR_KUNDE);
		wbuSpediteurKunde.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);
		wtfSpediteurKunde = new WrapperTextField();
		wtfSpediteurKunde.setActivatable(false);
		wtfSpediteurKunde.setMandatoryField(true);

		wbuSpediteurLF = new WrapperButton();
		wbuSpediteurLF.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.spediteur"));
		wbuSpediteurLF.setActionCommand(ACTION_SPECIAL_FLR_SPEDITEUR_LF);
		wbuSpediteurLF.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);
		wtfSpediteurLF = new WrapperTextField();
		wtfSpediteurLF.setActivatable(false);
		wtfSpediteurLF.setMandatoryField(true);

		wbuZahlungszielKunde = new WrapperButton();
		wbuZahlungszielKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.zahlungsziel"));
		wbuZahlungszielKunde
				.setActionCommand(ACTION_SPECIAL_FLR_ZAHLUNGSZIEL_KUNDE);
		wbuZahlungszielKunde.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);
		wtfZahlungszielKunde = new WrapperTextField();
		wtfZahlungszielKunde.setActivatable(false);
		wtfZahlungszielKunde.setMandatoryField(true);

		wbuZahlungszielLF = new WrapperButton();
		wbuZahlungszielLF.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.zahlungsziel"));
		wbuZahlungszielLF.setActionCommand(ACTION_SPECIAL_FLR_ZAHLUNGSZIEL_LF);
		wbuZahlungszielLF.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);
		wtfZahlungszielLF = new WrapperTextField();
		wtfZahlungszielLF.setActivatable(false);
		wtfZahlungszielLF.setMandatoryField(true);

		wbuPreislisteFLR = new WrapperButton();
		wbuPreislisteFLR.setText(LPMain.getInstance().getTextRespectUISPr(
				"vkpf.button.preislisten"));
		wbuPreislisteFLR.setMandatoryField(true);
		wbuPreislisteFLR.setActionCommand(ACTION_SPECIAL_FLR_PREISLISTEN);
		wbuPreislisteFLR.addActionListener(this);
		wtfPreisliste = new WrapperTextField(VkPreisfindungFac.MAX_CNR);
		wtfPreisliste.setActivatable(false);
		wtfPreisliste.setMandatoryField(true);

		wtfKostenstelleKunde = new WrapperTextField(MandantFac.MAX_KBEZ);
		wtfKostenstelleKunde.setMandatoryField(true);

		wbuKostenstelleFLR = new WrapperButton();
		wbuKostenstelleFLR.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));
		wbuKostenstelleFLR.setMandatoryField(true);
		wbuKostenstelleFLR.setActionCommand(ACTION_SPECIAL_FLR_KOSTENSTELLE);
		wbuKostenstelleFLR.addActionListener(this);
		wtfKostenstelleKunde = new WrapperTextField(
				SystemFac.MAX_KOSTENSTELLE_CNR);
		wtfKostenstelleKunde.setActivatable(false);
		wtfKostenstelleKunde.setMandatoryField(true);

		wbuMWSTSatz = new WrapperButton();
		wbuMWSTSatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.mwstsatz"));
		wbuMWSTSatz.setActionCommand(ACTION_SPECIAL_FLR_MWSTSATZ);
		wbuMWSTSatz.addActionListener(this);

		wtfMWSTSatz = new WrapperTextField();
		wtfMWSTSatz.setActivatable(false);

		wbuMWSTSatzAusland = new WrapperButton();
		wbuMWSTSatzAusland.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.mwstsatzausland"));
		wbuMWSTSatzAusland.setActionCommand(ACTION_SPECIAL_FLR_MWSTSATZAUSLAND);
		wbuMWSTSatzAusland.addActionListener(this);

		wtfMWSTSatzAusland = new WrapperTextField();
		wtfMWSTSatzAusland.setActivatable(false);

		wbuMWSTSatzDrittland = new WrapperButton();
		wbuMWSTSatzDrittland.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.mwstsatzdrittland"));
		wbuMWSTSatzDrittland
				.setActionCommand(ACTION_SPECIAL_FLR_MWSTSATZDRITTLAND);
		wbuMWSTSatzDrittland.addActionListener(this);

		wtfMWSTSatzDrittland = new WrapperTextField();
		wtfMWSTSatzDrittland.setActivatable(false);

		wbuBankverbindung = new WrapperButton();
		wbuBankverbindung.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.bankverbindung"));
		wbuBankverbindung.setActionCommand(ACTION_SPECIAL_BANK);
		wbuBankverbindung.addActionListener(this);

		wtfBankverbindungNummer = new WrapperTextField();
		wtfBankverbindungNummer.setActivatable(false);
		wtfBankverbindungBezeichnung = new WrapperTextField();
		wtfBankverbindungBezeichnung.setActivatable(false);

		wbuLieferadresse = new WrapperButton();
		wbuLieferadresse.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferadresse"));
		wbuLieferadresse.setActionCommand(ACTION_SPECIAL_LIEFERADRESSE);
		wbuLieferadresse.addActionListener(this);

		wtfLieferadresse = new WrapperTextField();
		wtfLieferadresse.setActivatable(false);
		wtfLieferadresse.setMandatoryField(true);

		wlaKundenvorbelegungen = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.kund.vorbelegungen"));
		wlaLFvorbelegungen = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.lief.vorbelegungen"));

		wlaVorbelegungen = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("syst.vorbelegungen.allgemein"));

		// ab hier einhaengen.

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaMandant, new GridBagConstraints(0, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfMandant, new GridBagConstraints(1, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(3, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(4, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wlaIstHauptmandant, new GridBagConstraints(3, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wcbIstHauptmandant, new GridBagConstraints(4, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaKurzbez, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfKurzbez, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		// iZeile++;
		// jpaWorkingOn.add(new WrapperLabel(),
		// new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
		// , GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH,
		// new Insets(1, 2, 1, 2), 0, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaVorbelegungen, new GridBagConstraints(0, iZeile, 1,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuLieferadresse, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferadresse, new GridBagConstraints(1, iZeile, 3,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuMWSTSatz, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfMWSTSatz, new GridBagConstraints(1, iZeile, 3, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuMWSTSatzAusland, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfMWSTSatzAusland, new GridBagConstraints(1, iZeile,
				3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuMWSTSatzDrittland, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfMWSTSatzDrittland, new GridBagConstraints(1,
				iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuBankverbindung, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfBankverbindungNummer, new GridBagConstraints(1,
				iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wtfBankverbindungBezeichnung, new GridBagConstraints(
				1, iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaKundenvorbelegungen, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuLieferartKunde, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferartKunde, new GridBagConstraints(1, iZeile,
				3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuSpediteurKunde, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfSpediteurKunde, new GridBagConstraints(1, iZeile,
				3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuZahlungszielKunde, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfZahlungszielKunde, new GridBagConstraints(1,
				iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuPreislisteFLR, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPreisliste, new GridBagConstraints(1, iZeile, 3, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuKostenstelleFLR, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleKunde, new GridBagConstraints(1,
				iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaLFvorbelegungen, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuLieferartLF, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferartLF, new GridBagConstraints(1, iZeile, 3,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuSpediteurLF, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfSpediteurLF, new GridBagConstraints(1, iZeile, 3,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuZahlungszielLF, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfZahlungszielLF, new GridBagConstraints(1, iZeile,
				3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

	}

	protected void dto2Components() throws Throwable {

		if (getMandantDto().getMwstsatzbezIIdStandardinlandmwstsatz() != null) {
			MwstsatzbezDto mwstbezDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzbezFindByPrimaryKey(
							getMandantDto()
									.getMwstsatzbezIIdStandardinlandmwstsatz());
			wtfMWSTSatz.setText(mwstbezDto.getCBezeichnung());
		}

		if (getMandantDto().getMwstsatzbezIIdStandardauslandmwstsatz() != null) {
			MwstsatzbezDto mwstbezDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzbezFindByPrimaryKey(
							getMandantDto()
									.getMwstsatzbezIIdStandardauslandmwstsatz());
			wtfMWSTSatzAusland.setText(mwstbezDto.getCBezeichnung());
		}
		if (getMandantDto().getMwstsatzbezIIdStandarddrittlandmwstsatz() != null) {
			MwstsatzbezDto mwstbezDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzbezFindByPrimaryKey(
							getMandantDto()
									.getMwstsatzbezIIdStandarddrittlandmwstsatz());
			wtfMWSTSatzDrittland.setText(mwstbezDto.getCBezeichnung());
		}

		if (getMandantDto().getIBankverbindung() != null) {
			BankverbindungDto bankverbindungDto = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.bankverbindungFindByPrimaryKey(
							getMandantDto().getIBankverbindung());
			wtfBankverbindungNummer
					.setText(bankverbindungDto.getCKontonummer());
			PartnerDto partner = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(bankverbindungDto.getBankIId());
			wtfBankverbindungBezeichnung.setText(partner
					.formatFixTitelName1Name2());
		}
		wtfKurzbez.setText(getMandantDto().getCKbez());

		wtfMandant.setText(getMandantDto().getCNr());

		if (getMandantDto().getPartnerIIdLieferadresse() != null) {
			PartnerDto partnerDto = DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(
							getMandantDto().getPartnerIIdLieferadresse());
			wtfLieferadresse.setText(partnerDto.formatAnrede());
		}

		boolean bIsHauptmandant = getMandantDto().getCNr().equals(
				getMandantDto().getAnwenderDto().getMandantCNrHauptmandant());
		wcbIstHauptmandant.setShort(Helper.boolean2Short(bIsHauptmandant));

		String sLieferart = null;
		if (getMandantDto().getLieferartIIdKunde() != null) {
			LieferartDto lieferart2Dto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.lieferartFindByPrimaryKey(
							getMandantDto().getLieferartIIdKunde());
			sLieferart = lieferart2Dto.getCNr();
		}
		wtfLieferartKunde.setText(sLieferart);

		sLieferart = null;
		if (getMandantDto().getLieferartIIdLF() != null) {
			LieferartDto lieferart2Dto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.lieferartFindByPrimaryKey(
							getMandantDto().getLieferartIIdLF());
			sLieferart = lieferart2Dto.getCNr();
		}
		wtfLieferartLF.setText(sLieferart);

		String sSpediteur = null;
		if (getMandantDto().getSpediteurIIdKunde() != null) {
			SpediteurDto spediteurDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.spediteurFindByPrimaryKey(
							getMandantDto().getSpediteurIIdKunde());
			sSpediteur = spediteurDto.getCNamedesspediteurs();
		}
		wtfSpediteurKunde.setText(sSpediteur);

		sSpediteur = null;
		if (getMandantDto().getSpediteurIIdLF() != null) {
			SpediteurDto spediteurDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.spediteurFindByPrimaryKey(
							getMandantDto().getSpediteurIIdLF());
			sSpediteur = spediteurDto.getCNamedesspediteurs();
		}
		wtfSpediteurLF.setText(sSpediteur);

		if (getMandantDto().getZahlungszielIIdKunde() != null) {
			ZahlungszielDto zahlungszielDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.zahlungszielFindByPrimaryKey(
							getMandantDto().getZahlungszielIIdKunde());
			if (zahlungszielDto != null) {
				if (zahlungszielDto.getZahlungszielsprDto() != null) {
					wtfZahlungszielKunde.setText(zahlungszielDto
							.getZahlungszielsprDto().getCBezeichnung());
				} else {
					wtfZahlungszielKunde.setText(zahlungszielDto.getCBez());
				}
			} else {
				wtfZahlungszielKunde.setText(null);
			}
		}

		if (getMandantDto().getZahlungszielIIdLF() != null) {
			ZahlungszielDto zahlungszielDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.zahlungszielFindByPrimaryKey(
							getMandantDto().getZahlungszielIIdLF());
			if (zahlungszielDto != null) {
				if (zahlungszielDto.getZahlungszielsprDto() != null) {
					wtfZahlungszielLF.setText(zahlungszielDto
							.getZahlungszielsprDto().getCBezeichnung());
				} else {
					wtfZahlungszielLF.setText(zahlungszielDto.getCBez());
				}
			} else {
				wtfZahlungszielLF.setText(null);
			}

		}

		setWtfPreisliste(getMandantDto().getVkpfArtikelpreislisteIId());

		String kos = null;
		if (getMandantDto().getIIdKostenstelle() != null) {
			kos = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.kostenstelleFindByPrimaryKey(
							getMandantDto().getIIdKostenstelle()).getCNr();
		}
		wtfKostenstelleKunde.setText(kos);
	}

	protected void components2Dto() throws Throwable {

		getMandantDto().setCKbez(wtfKurzbez.getText());

		getMandantDto().setCNr(wtfMandant.getText());

		getMandantDto().getAnwenderDto().setIId(
				new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));

		if (Helper.short2boolean(wcbIstHauptmandant.getShort()) == true) {

			AnwenderDto anwenderDto = null;
			anwenderDto = getMandantDto().getAnwenderDto();
			anwenderDto.setMandantCNrHauptmandant(getMandantDto().getCNr());
			DelegateFactory.getInstance().getSystemDelegate()
					.updateAnwender(anwenderDto);
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);

		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance()
				.getTheClient().getMandant(), getMandantDto().getCNr());

		if (!bNeedNoYouAreSelectedI) {

			String cNr = getMandantDto().getCNr();

			if (cNr == null) {
				throw new Exception("key == null");
			}

			getInternalFrameSystem().setMandantDto(
					DelegateFactory.getInstance().getMandantDelegate()
							.mandantFindByPrimaryKey(cNr));

			initPanel();

			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameSystem().getMandantDto().getCNr());

			setStatusbar();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getMandantDelegate()
					.updateMandant(getMandantDto());

			super.eventActionSave(e, true);

			eventYouAreSelected(false);

			dto2Components();
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_HAUPTMANDANT_U)) {
			wcbIstHauptmandant.setActivatable(true);
		} else {
			wcbIstHauptmandant.setActivatable(false);
		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_LIEFERART_KUNDE)) {
			panelQueryFLRLieferartKunde = SystemFilterFactory.getInstance()
					.createPanelFLRLieferart(getInternalFrame(),
							getMandantDto().getLieferartIIdKunde(),
							getMandantDto().getCNr());
			new DialogQuery(panelQueryFLRLieferartKunde);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_LIEFERART_LF)) {
			panelQueryFLRLieferartLF = SystemFilterFactory.getInstance()
					.createPanelFLRLieferart(getInternalFrame(),
							getMandantDto().getLieferartIIdLF(),
							getMandantDto().getCNr());
			new DialogQuery(panelQueryFLRLieferartLF);
		}

		else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FLR_SPEDITEUR_KUNDE)) {
			panelQueryFLRSpediteurKunde = SystemFilterFactory.getInstance()
					.createPanelFLRSpediteur(getInternalFrame(),
							getMandantDto().getSpediteurIIdKunde(),
							getMandantDto().getCNr());
			new DialogQuery(panelQueryFLRSpediteurKunde);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_SPEDITEUR_LF)) {
			panelQueryFLRSpediteurLF = SystemFilterFactory.getInstance()
					.createPanelFLRSpediteur(getInternalFrame(),
							getMandantDto().getSpediteurIIdLF(),
							getMandantDto().getCNr());
			new DialogQuery(panelQueryFLRSpediteurLF);
		}

		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_ZAHLUNGSZIEL_KUNDE)) {
			panelQueryFLRZahlungszielKunde = SystemFilterFactory.getInstance()
					.createPanelFLRZahlungsziel(getInternalFrame(),
							getMandantDto().getZahlungszielIIdKunde(),
							getMandantDto().getCNr());
			new DialogQuery(panelQueryFLRZahlungszielKunde);
		}

		else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FLR_ZAHLUNGSZIEL_LF)) {
			panelQueryFLRZahlungszielLF = SystemFilterFactory.getInstance()
					.createPanelFLRZahlungsziel(getInternalFrame(),
							getMandantDto().getZahlungszielIIdLF(),
							getMandantDto().getCNr());
			new DialogQuery(panelQueryFLRZahlungszielLF);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KOSTENSTELLE)) {
			panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
					.createPanelFLRKostenstelle(getInternalFrame(),
							getMandantDto().getCNr(), false, false);
			if (getMandantDto().getIIdKostenstelle() != null) {
				panelQueryFLRKostenstelle.setSelectedId(getMandantDto()
						.getIIdKostenstelle());
			}
			new DialogQuery(panelQueryFLRKostenstelle);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_PREISLISTEN)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
			QueryType[] querytypes = null;
			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKMandantCNr();
			panelQueryFLRPreislisten = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_PREISLISTENNAME, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("vkpf.preislisten.title.tab"));
			if (getMandantDto().getVkpfArtikelpreislisteIId() != null) {
				panelQueryFLRPreislisten.setSelectedId(getMandantDto()
						.getVkpfArtikelpreislisteIId());
			}

			new DialogQuery(panelQueryFLRPreislisten);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_MWSTSATZ)) {
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
			QueryType[] querytypes = null;
			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKMandantCNr();
			panelQueryFLRMWSTSatz = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_MWSTSATZBEZ, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.mwstsatz"));
			if (getMandantDto().getMwstsatzbezIIdStandardinlandmwstsatz() != null) {
				panelQueryFLRMWSTSatz.setSelectedId(getMandantDto()
						.getMwstsatzbezIIdStandardinlandmwstsatz());
			}
			new DialogQuery(panelQueryFLRMWSTSatz);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_MWSTSATZAUSLAND)) {
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
			QueryType[] querytypes = null;
			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKMandantCNr();
			panelQueryFLRMWSTSatzAusland = new PanelQueryFLR(querytypes,
					filters, QueryParameters.UC_ID_MWSTSATZBEZ,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("button.mwstsatzausland"));
			if (getMandantDto().getMwstsatzbezIIdStandardauslandmwstsatz() != null) {
				panelQueryFLRMWSTSatzAusland.setSelectedId(getMandantDto()
						.getMwstsatzbezIIdStandardauslandmwstsatz());
			}
			new DialogQuery(panelQueryFLRMWSTSatzAusland);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_MWSTSATZDRITTLAND)) {
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
			QueryType[] querytypes = null;
			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKMandantCNr();
			panelQueryFLRMWSTSatzDrittland = new PanelQueryFLR(querytypes,
					filters, QueryParameters.UC_ID_MWSTSATZBEZ,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("button.mwstsatzdrittland"));
			if (getMandantDto().getMwstsatzbezIIdStandarddrittlandmwstsatz() != null) {
				panelQueryFLRMWSTSatzDrittland.setSelectedId(getMandantDto()
						.getMwstsatzbezIIdStandarddrittlandmwstsatz());
			}
			new DialogQuery(panelQueryFLRMWSTSatzDrittland);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BANK)) {
			dialogQueryBank(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERADRESSE)) {
			dialogQueryLieferadresse();
			if (getMandantDto().getPartnerIIdLieferadresse() != null) {
				panelQueryLieferadresse.setSelectedId(getMandantDto()
						.getPartnerIIdLieferadresse());
			}
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MANDANT;
	}

	protected void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getMandantDto().getIAnlegen());
		setStatusbarPersonalIIdAendern(getMandantDto().getIAendern());
		setStatusbarTAendern(getMandantDto().getTAendern());
		setStatusbarTAnlegen(getMandantDto().getTAnlegen());
	}

	protected MandantDto getMandantDto() {
		return getInternalFrameSystem().getMandantDto();
	}

	private InternalFrameSystem getInternalFrameSystem() {
		return (InternalFrameSystem) getInternalFrame();
	}

	private void setWtfPreisliste(Integer keyPreisliste) throws Throwable {

		String sVKP = null;
		if (keyPreisliste != null) {
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto = DelegateFactory
					.getInstance().getVkPreisfindungDelegate()
					.vkpfartikelpreislisteFindByPrimaryKey(keyPreisliste);
			sVKP = vkpfartikelpreislisteDto.getCNr();
		}

		wtfPreisliste.setText(sVKP);
	}

	protected void eventActionRefresh(ActionEvent aE, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(aE, bNeedNoRefreshI);
		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance()
				.getTheClient().getMandant(), getMandantDto().getCNr());
	}

	/**
	 * 
	 * @param loggedinMandant
	 *            String
	 * @param selectedMandant
	 *            String
	 * @throws Throwable
	 */
	private void checkMandantLoggedInEqualsMandantSelected(
			String loggedinMandant, String selectedMandant) throws Throwable {

		if (!loggedinMandant.equals(selectedMandant)) {

			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
					PanelBasis.ACTION_UPDATE);

			item.getButton().setEnabled(false);
			getPanelStatusbar().setLockField(
					LPMain.getInstance().getTextRespectUISPr(
							"system.nurleserecht"));
		}
	}

	private void dialogQueryLieferadresse() throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_LEEREN };

		panelQueryLieferadresse = new PanelQueryFLR(
				null,
				null,
				QueryParameters.UC_ID_PARTNER,
				aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.title.lieferadresseauswahlliste"));

		panelQueryLieferadresse
				.befuellePanelFilterkriterienDirekt(PartnerFilterFactory
						.getInstance().createFKDPartnerName(),
						PartnerFilterFactory.getInstance()
								.createFKDPartnerLandPLZOrt());

		new DialogQuery(panelQueryLieferadresse);
	}

}
