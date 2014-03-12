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
package com.lp.client.inserat;

import java.awt.Color;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
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
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.inserat.service.InseratrechnungDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

public class PanelInseratKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameInserat internalFrameInserat = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "ACTION_SPECIAL_KUNDE_FROM_LISTE";
	static final public String ACTION_SPECIAL_LIEFERANT_FROM_LISTE = "ACTION_SPECIAL_LIEFERANT_FROM_LISTE";
	static final public String ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE = "ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE";
	static final public String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT_FROM_LISTE = "ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT_FROM_LISTE";
	static final public String ACTION_SPECIAL_PERSONAL_FROM_LISTE = "action_personal_from_liste";
	static final public String ACTION_SPECIAL_INSERAT_KOPIEREN = "action_special_kopieren";

	static final public String ACTION_SPECIAL_TOGGLE_VERRECHENBAR = "action_special_toggle_verrechenbar";
	static final public String ACTION_SPECIAL_BESTELLUNG_AUSLOESEN = "action_special_bestellung_ausloesen";

	private PanelQueryFLR panelQueryFLRLieferant = null;
	private PanelQueryFLR panelQueryFLRPersonal = null;
	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Lieferant = null;

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperLabel wlaArtikelvorhanden = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaRabatt = new WrapperLabel();
	private WrapperNumberField wnfRabattKD = new WrapperNumberField();
	private WrapperLabel wlaZusatzRabatt = new WrapperLabel();
	private WrapperNumberField wnfZusatzRabattKD = new WrapperNumberField();
	private WrapperLabel wlaNachlass = new WrapperLabel();
	private WrapperNumberField wnfNachlassKD = new WrapperNumberField();

	private WrapperNumberField wnfRabattLF = new WrapperNumberField();
	private WrapperNumberField wnfZusatzRabattLF = new WrapperNumberField();
	private WrapperNumberField wnfNachlassLF = new WrapperNumberField();

	private WrapperNumberField wnfPreisEK = new WrapperNumberField();
	private WrapperNumberField wnfErrechneterPreisEK = new WrapperNumberField();
	private WrapperNumberField wnfErrechneterWertEK = new WrapperNumberField();

	private WrapperNumberField wnfPreisVK = new WrapperNumberField();
	private WrapperNumberField wnfErrechneterPreisVK = new WrapperNumberField();
	private WrapperNumberField wnfErrechneterWertVK = new WrapperNumberField();
	private WrapperLabel wlaVerrechenbar = new WrapperLabel();
	private WrapperLabel wlaGestoppt = new WrapperLabel();
	private WrapperLabel wlaManuellerledigt = new WrapperLabel();

	private WrapperLabel wlaMitdrucken = new WrapperLabel();
	private WrapperCheckBox wcbDruckBestellungLF = new WrapperCheckBox();
	private WrapperCheckBox wcbDruckBestellungKD = new WrapperCheckBox();
	private WrapperCheckBox wcbDruckRechnungKD = new WrapperCheckBox();
	private WrapperCheckBox wcbWertaufteilen = new WrapperCheckBox();

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();

	private WrapperLabel wlaTermin = new WrapperLabel();
	private WrapperDateField wdfTermin = new WrapperDateField();

	private WrapperLabel wlaTerminBis = new WrapperLabel();
	private WrapperDateField wdfTerminBis = new WrapperDateField();

	private WrapperIdentField wifArtikel = null;

	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperLabel wlaRubrik = new WrapperLabel();
	private WrapperTextField wtfRubrik = new WrapperTextField();
	private WrapperLabel wlaRubrik2 = new WrapperLabel();
	private WrapperTextField wtfRubrik2 = new WrapperTextField();
	private WrapperLabel wlaMedium = new WrapperLabel();
	private WrapperTextField wtfMedium = new WrapperTextField();
	private WrapperLabel wlaStichwort = new WrapperLabel();
	private WrapperTextField wtfSichwort = new WrapperTextField();
	private WrapperLabel wlaStichwort2 = new WrapperLabel();
	private WrapperTextField wtfSichwort2 = new WrapperTextField();

	private WrapperLabel wlaAnhangKD = new WrapperLabel(
			LPMain.getTextRespectUISPr("label.anhang"));
	private WrapperLabel wlaAnhangLF = new WrapperLabel(
			LPMain.getTextRespectUISPr("label.anhang"));

	private WrapperEditorField wefAnhangKD = new WrapperEditorField(
			getInternalFrame(), LPMain.getTextRespectUISPr("label.anhang"));
	private WrapperEditorField wefAnhangLF = new WrapperEditorField(
			getInternalFrame(), LPMain.getTextRespectUISPr("label.anhang"));

	private WrapperGotoButton wbuKunde = new WrapperGotoButton(
			WrapperGotoButton.GOTO_KUNDE_AUSWAHL);
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperGotoButton wbuLieferant = new WrapperGotoButton(
			WrapperGotoButton.GOTO_LIEFERANT_AUSWAHL);
	private WrapperTextField wtfLieferant = new WrapperTextField();
	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();
	private WrapperButton wbuAnsprechpartnerLieferant = new WrapperButton();
	private WrapperTextField wtfAnsprechpartnerLieferant = new WrapperTextField();
	private WrapperButton wbuPersonal = new WrapperButton();
	private WrapperTextField wtfPersonal = new WrapperTextField();

	public InternalFrameInserat getInternalFrameInserat() {
		return internalFrameInserat;
	}

	private void dialogQueryLieferant() throws Throwable {
		panelQueryFLRLieferant = PartnerFilterFactory
				.getInstance()
				.createPanelFLRLieferantGoto(
						getInternalFrame(),
						internalFrameInserat.getTabbedPaneInserat()
								.getInseratDto().getLieferantIId(), true, false);
		new DialogQuery(panelQueryFLRLieferant);
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(
						getInternalFrame(),
						true,
						true,
						internalFrameInserat.getTabbedPaneInserat()
								.getInseratDto().getInseratrechnungDto()
								.getKundeIId());

		new DialogQuery(panelQueryFLRKunde);
	}

	private void dialogQueryAnsprechpartner(ActionEvent e) throws Throwable {
		// String key = (String) wcoReklamationart.getKeyOfSelectedItem();

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getInseratrechnungDto().getKundeIId() == null) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {
			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getInseratrechnungDto()
									.getKundeIId());
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(
							getInternalFrame(),
							kundeDto.getPartnerIId(),
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getInseratrechnungDto()
									.getAnsprechpartnerIId(), true, true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
		}

	}

	private void dialogQueryAnsprechpartnerLieferant(ActionEvent e)
			throws Throwable {
		// String key = (String) wcoReklamationart.getKeyOfSelectedItem();

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getLieferantIId() == null) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain
					.getTextRespectUISPr("lp.error.lieferantnichtgewaehlt"));
		} else {
			LieferantDto lfDto = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getLieferantIId());
			panelQueryFLRAnsprechpartner_Lieferant = PartnerFilterFactory
					.getInstance().createPanelFLRAnsprechpartner(
							getInternalFrame(),
							lfDto.getPartnerIId(),
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto()
									.getAnsprechpartnerIIdLieferant(), true,
							true);
			new DialogQuery(panelQueryFLRAnsprechpartner_Lieferant);
		}

	}

	void dialogQueryPersonalFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRPersonal = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(
						getInternalFrame(),
						true,
						false,
						internalFrameInserat.getTabbedPaneInserat()
								.getInseratDto().getPersonalIIdVertreter());
		new DialogQuery(panelQueryFLRPersonal);
	}

	public PanelInseratKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameInserat = (InternalFrameInserat) internalFrame;

		jbInit();
		setDefaults();
		initComponents();
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getIId() != null) {
			if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
					.getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getInseratDto().getStatusCNr()
							.equals(LocaleFac.STATUS_TEILBEZAHLT)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getInseratDto().getStatusCNr()
							.equals(LocaleFac.STATUS_BEZAHLT)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getInseratDto().getStatusCNr()
							.equals(LocaleFac.STATUS_ERLEDIGT)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfMenge;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null || key.equals(LPMain.getLockMeForNew())) {

			clearStatusbar();

			wnfNachlassKD.setDouble(0D);
			wnfNachlassLF.setDouble(0D);
			wnfZusatzRabattKD.setDouble(0D);
			wnfZusatzRabattLF.setDouble(0D);
			wnfRabattKD.setDouble(0D);
			wnfRabattLF.setDouble(0D);
			wdfDatum.setDatumHeute();
			// SP1338
			wdfTermin.setTimestamp(getInternalFrameInserat()
					.getTabbedPaneInserat().tZuletztVerwendeterTermin);

			// PJ17996
			wnfMenge.setBigDecimal(new BigDecimal(1));
			wnfPreisEK.setDouble(0D);
			wnfPreisVK.setDouble(0D);

			wcbDruckBestellungLF.setSelected(true);
			wcbDruckBestellungKD.setSelected(true);
			wcbDruckRechnungKD.setSelected(false);
			wcbWertaufteilen.setSelected(false);
			wlaArtikelvorhanden.setVisible(false);

		} else {
			internalFrameInserat.getTabbedPaneInserat().setInseratDto(
					DelegateFactory.getInstance().getInseratDelegate()
							.inseratFindByPrimaryKey((Integer) key));

			dto2Components();

		}

	}

	protected void dto2Components() throws Throwable {
		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getLieferantIId() != null) {
			LieferantDto lieferantDtoNew = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getLieferantIId());

			String sAdresse = lieferantDtoNew.getPartnerDto()
					.formatFixTitelName1Name2();
			if (lieferantDtoNew.getPartnerDto().getCKbez() != null) {
				sAdresse = sAdresse + "  /  "
						+ lieferantDtoNew.getPartnerDto().getCKbez();
			}

			wtfLieferant.setText(sAdresse);
			wbuLieferant.setOKey(internalFrameInserat.getTabbedPaneInserat()
					.getInseratDto().getLieferantIId());
		} else {
			wtfLieferant.setText(null);
			wbuLieferant.setOKey(null);
		}

		int iAnzahlArtikel = DelegateFactory
				.getInstance()
				.getInseratDelegate()
				.inseratartikelFindByInseratIId(
						internalFrameInserat.getTabbedPaneInserat()
								.getInseratDto().getIId()).length;
		if (iAnzahlArtikel == 0) {
			wlaArtikelvorhanden.setVisible(false);
		} else {
			wlaArtikelvorhanden.setVisible(true);
		}

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getInseratrechnungDto().getKundeIId() != null) {
			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getInseratrechnungDto()
									.getKundeIId());

			String sAdresse = kundeDto.getPartnerDto()
					.formatFixTitelName1Name2();
			if (kundeDto.getPartnerDto().getCKbez() != null) {
				sAdresse = sAdresse + "  /  "
						+ kundeDto.getPartnerDto().getCKbez();
			}

			wtfKunde.setText(sAdresse);
			wbuKunde.setOKey(internalFrameInserat.getTabbedPaneInserat()
					.getInseratDto().getInseratrechnungDto().getKundeIId());
		} else {
			wtfKunde.setText(null);
			wbuKunde.setOKey(null);
		}

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getInseratrechnungDto().getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getInseratrechnungDto()
									.getAnsprechpartnerIId());

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getAnsprechpartnerIIdLieferant() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto()
									.getAnsprechpartnerIIdLieferant());

			wtfAnsprechpartnerLieferant.setText(ansprechpartnerDto
					.getPartnerDto().formatTitelAnrede());
		} else {
			wtfAnsprechpartnerLieferant.setText(null);
		}
		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getPersonalIIdVertreter() != null) {

			PersonalDto personalDtoVerursacher = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getPersonalIIdVertreter());
			wtfPersonal.setText(personalDtoVerursacher.formatAnrede());
		} else {
			wtfPersonal.setText("");
		}

		ArtikelDto artikelDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(
						internalFrameInserat.getTabbedPaneInserat()
								.getInseratDto().getArtikelIIdInseratart());
		wifArtikel.setArtikelDto(artikelDto);

		wdfDatum.setTimestamp(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getTBelegdatum());
		wdfTermin.setTimestamp(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getTTermin());
		wdfTerminBis.setTimestamp(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getTTerminBis());

		wnfNachlassKD.setDouble(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getFKdNachlass());
		wnfNachlassLF.setDouble(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getFLfNachlass());
		wnfZusatzRabattKD.setDouble(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getFKdZusatzrabatt());
		wnfZusatzRabattLF.setDouble(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getFLfZusatzrabatt());
		wnfRabattKD.setDouble(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getFKdRabatt());
		wnfRabattLF.setDouble(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getFLFRabatt());

		wnfMenge.setBigDecimal(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getNMenge());
		wnfPreisEK.setBigDecimal(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getNNettoeinzelpreisEk());
		wnfPreisVK.setBigDecimal(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getNNettoeinzelpreisVk());

		// Preise berechnen
		InseratartikelDto[] inseratartikelDtos = DelegateFactory
				.getInstance()
				.getInseratDelegate()
				.inseratartikelFindByInseratIId(
						internalFrameInserat.getTabbedPaneInserat()
								.getInseratDto().getIId());

		BigDecimal preisZusatzEK = new BigDecimal(0);
		BigDecimal preisZusatzVK = new BigDecimal(0);
		for (int i = 0; i < inseratartikelDtos.length; i++) {
			preisZusatzEK = preisZusatzEK.add(inseratartikelDtos[i].getNMenge()
					.multiply(inseratartikelDtos[i].getNNettoeinzelpreisEk()));
			preisZusatzVK = preisZusatzVK.add(inseratartikelDtos[i].getNMenge()
					.multiply(inseratartikelDtos[i].getNNettoeinzelpreisVk()));
		}

		// EK
		Double dRabattGesamt = Helper.berechneRabattsatzMehrererRabatte(
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getFLFRabatt(), internalFrameInserat
						.getTabbedPaneInserat().getInseratDto()
						.getFLfZusatzrabatt(), internalFrameInserat
						.getTabbedPaneInserat().getInseratDto()
						.getFLfNachlass());

		BigDecimal bdErrechneterPreisEK = internalFrameInserat
				.getTabbedPaneInserat()
				.getInseratDto()
				.getNNettoeinzelpreisEk()
				.subtract(
						Helper.getProzentWert(internalFrameInserat
								.getTabbedPaneInserat().getInseratDto()
								.getNNettoeinzelpreisEk(), new BigDecimal(
								dRabattGesamt), 4));

		wnfErrechneterPreisEK.setBigDecimal(bdErrechneterPreisEK);
		wnfErrechneterWertEK.setBigDecimal(bdErrechneterPreisEK.multiply(
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getNMenge()).add(preisZusatzEK));

		// VK
		dRabattGesamt = Helper.berechneRabattsatzMehrererRabatte(
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getFKdRabatt(), internalFrameInserat
						.getTabbedPaneInserat().getInseratDto()
						.getFKdZusatzrabatt(), internalFrameInserat
						.getTabbedPaneInserat().getInseratDto()
						.getFKdNachlass());

		BigDecimal bdErrechneterPreisVK = internalFrameInserat
				.getTabbedPaneInserat()
				.getInseratDto()
				.getNNettoeinzelpreisVk()
				.subtract(
						Helper.getProzentWert(internalFrameInserat
								.getTabbedPaneInserat().getInseratDto()
								.getNNettoeinzelpreisVk(), new BigDecimal(
								dRabattGesamt), 4));

		wnfErrechneterPreisVK.setBigDecimal(bdErrechneterPreisVK);
		wnfErrechneterWertVK.setBigDecimal(bdErrechneterPreisVK.multiply(
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getNMenge()).add(preisZusatzVK));

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getPersonalIIdManuellverrechnen() != null) {
			PersonalDto personalDtoVerrechnen = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto()
									.getPersonalIIdManuellverrechnen());

			wlaVerrechenbar.setText(LPMain
					.getTextRespectUISPr("iv.verrechenbargesetzt")
					+ " "
					+ Helper.formatDatum(internalFrameInserat
							.getTabbedPaneInserat().getInseratDto()
							.getTManuellverrechnen(), LPMain.getTheClient()
							.getLocUi())
					+ ", "
					+ personalDtoVerrechnen.formatAnrede());

		} else if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getPersonalIIdVerrechnen() != null) {
			PersonalDto personalDtoVerrechnen = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getPersonalIIdVerrechnen());

			wlaVerrechenbar
					.setText(LPMain
							.getTextRespectUISPr("iv.verrechenbargesetzt")
							+ " "
							+ Helper.formatDatum(internalFrameInserat
									.getTabbedPaneInserat().getInseratDto()
									.getTVerrechnen(), LPMain.getTheClient()
									.getLocUi())
							+ ", "
							+ personalDtoVerrechnen.formatAnrede());

		} else {
			wlaVerrechenbar.setText("");
		}

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getPersonalIIdGestoppt() != null) {
			PersonalDto personalDtoGestoppt = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getPersonalIIdGestoppt());

			String cGestoppt = "";
			if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
					.getCGestoppt() != null) {
				cGestoppt = internalFrameInserat.getTabbedPaneInserat()
						.getInseratDto().getCGestoppt();
			}

			wlaGestoppt.setText(LPMain
					.getTextRespectUISPr("iv.gestopptgesetzt")
					+ " "
					+ Helper.formatDatum(internalFrameInserat
							.getTabbedPaneInserat().getInseratDto()
							.getTGestoppt(), LPMain.getTheClient().getLocUi())
					+ ", "
					+ personalDtoGestoppt.formatAnrede()
					+ ", "
					+ cGestoppt);

		} else {
			if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
					.getCGestoppt() != null) {
				wlaGestoppt.setText(LPMain
						.getTextRespectUISPr("iv.gestopptgesetzt")
						+ " "
						+ internalFrameInserat.getTabbedPaneInserat()
								.getInseratDto().getCGestoppt());
			} else {
				wlaGestoppt.setText("");
			}
		}

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getPersonalIIdManuellerledigt() != null) {
			PersonalDto personalDtoVerrechnen = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto()
									.getPersonalIIdManuellerledigt());

			wlaManuellerledigt.setText(LPMain
					.getTextRespectUISPr("iv.manuellerledigtam")
					+ " "
					+ Helper.formatDatum(internalFrameInserat
							.getTabbedPaneInserat().getInseratDto()
							.getTManuellerledigt(), LPMain.getTheClient()
							.getLocUi())
					+ ", "
					+ personalDtoVerrechnen.formatAnrede());

		} else {
			wlaManuellerledigt.setText("");
		}

		wcbDruckBestellungKD
				.setShort(internalFrameInserat.getTabbedPaneInserat()
						.getInseratDto().getBDruckBestellungKd());
		wcbDruckBestellungLF
				.setShort(internalFrameInserat.getTabbedPaneInserat()
						.getInseratDto().getBDruckBestellungLf());
		wcbDruckRechnungKD.setShort(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getBDruckRechnungKd());
		wcbWertaufteilen.setShort(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getBWertaufteilen());

		wtfBezeichnung.setText(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getCBez());
		wtfRubrik.setText(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getCRubrik());
		wtfRubrik2.setText(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getCRubrik2());
		wtfSichwort.setText(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getCStichwort());
		wtfSichwort2.setText(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getCStichwort2());
		wtfMedium.setText(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getCMedium());

		wefAnhangKD.setText(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getXAnhang());
		wefAnhangLF.setText(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getXAnhangLf());

		this.setStatusbarPersonalIIdAendern(internalFrameInserat
				.getTabbedPaneInserat().getInseratDto().getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(internalFrameInserat
				.getTabbedPaneInserat().getInseratDto().getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getTAnlegen());
		this.setStatusbarTAendern(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getTAendern());
		this.setStatusbarSpalte5(internalFrameInserat.getTabbedPaneInserat()
				.getInseratDto().getStatusCNr());
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);
		wifArtikel.setBMitLeerenButton(true);
		wlaDatum.setText(LPMain.getTextRespectUISPr("label.belegdatum"));
		wdfDatum.setMandatoryField(true);
		wlaTermin.setText(LPMain.getTextRespectUISPr("label.termin"));
		wlaMenge.setText(LPMain.getTextRespectUISPr("lp.menge"));

		wlaArtikelvorhanden.setText("A");
		wlaArtikelvorhanden.setForeground(Color.RED);
		Font defaultFont = wlaArtikelvorhanden.getFont();
		wlaArtikelvorhanden.setFont(defaultFont.deriveFont(Font.BOLD, defaultFont.getSize2D()+10));

		wdfTermin.setMandatoryField(true);
		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);
		wtfKunde.setActivatable(false);
		wtfKunde.setMandatoryField(true);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wbuLieferant.setText(LPMain.getTextRespectUISPr("button.lieferant"));
		wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT_FROM_LISTE);
		wbuLieferant.addActionListener(this);
		wtfLieferant.setMandatoryField(true);
		wtfLieferant.setActivatable(false);
		wtfLieferant.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner
				.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE);
		wbuAnsprechpartner.addActionListener(this);

		wbuAnsprechpartnerLieferant.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerLieferant
				.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT_FROM_LISTE);
		wbuAnsprechpartnerLieferant.addActionListener(this);
		wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartnerLieferant.setActivatable(false);

		wbuPersonal.setActionCommand(ACTION_SPECIAL_PERSONAL_FROM_LISTE);
		wbuPersonal.addActionListener(this);

		wbuPersonal.setText(LPMain.getTextRespectUISPr("button.vertreter"));
		wtfPersonal.setActivatable(false);
		wtfPersonal.setMandatoryField(true);

		wlaRabatt.setText(LPMain.getTextRespectUISPr("label.rabatt"));
		wlaZusatzRabatt.setText(LPMain.getTextRespectUISPr("iv.zusatzrabatt"));
		wlaNachlass.setText(LPMain.getTextRespectUISPr("iv.nachlass"));

		wlaTerminBis.setText(LPMain.getTextRespectUISPr("lp.bis"));

		wlaStichwort.setText(LPMain.getTextRespectUISPr("iv.stichwort"));
		wlaRubrik.setText(LPMain.getTextRespectUISPr("iv.rubrik"));
		wlaStichwort2.setText(LPMain.getTextRespectUISPr("iv.stichwort2"));
		wlaRubrik2.setText(LPMain.getTextRespectUISPr("iv.rubrik2"));
		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("iv.bezeichnung"));
		wlaMedium.setText(LPMain.getTextRespectUISPr("iv.medium"));
		wtfMedium.setColumnsMax(80);
		wtfBezeichnung.setColumnsMax(300);

		wnfMenge.setMandatoryField(true);
		wnfRabattKD.setMandatoryField(true);
		wnfZusatzRabattKD.setMandatoryField(true);
		wnfNachlassKD.setMandatoryField(true);
		wnfRabattLF.setMandatoryField(true);
		wnfZusatzRabattLF.setMandatoryField(true);
		wnfNachlassLF.setMandatoryField(true);
		wnfErrechneterPreisEK.setActivatable(false);
		wnfErrechneterPreisVK.setActivatable(false);
		wnfErrechneterWertEK.setActivatable(false);
		wnfErrechneterWertVK.setActivatable(false);
		
		
		wnfRabattKD.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfZusatzRabattKD.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfNachlassKD.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfRabattLF.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfZusatzRabattLF.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfNachlassLF.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfPreisEK.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfPreisVK.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfErrechneterPreisEK.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfErrechneterPreisVK.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfErrechneterWertEK.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfErrechneterWertVK.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		

		wcbDruckBestellungLF.setText(LPMain
				.getTextRespectUISPr("iv.kopfdaten.mitdrucken.bestellung"));
		wcbDruckBestellungKD.setText(LPMain
				.getTextRespectUISPr("iv.kopfdaten.mitdrucken.bestellung"));
		wcbDruckRechnungKD.setText(LPMain
				.getTextRespectUISPr("iv.kopfdaten.mitdrucken.rechnung"));
		wcbWertaufteilen.setText(LPMain
				.getTextRespectUISPr("iv.kopfdaten.wertaufteilen"));

		wnfPreisEK.setMandatoryField(true);
		wnfPreisVK.setMandatoryField(true);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel(new MigLayout("wrap 8", "[fill,20%][fill,15%][fill,5%][fill,20%][fill,15%][fill,10%][fill,10%][fill,10%]"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(-9, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaDatum);
		jpaWorkingOn.add(wdfDatum);
		jpaWorkingOn.add(wlaArtikelvorhanden, "span 3");
		jpaWorkingOn.add(wlaMenge);
		jpaWorkingOn.add(wnfMenge);
		jpaWorkingOn.add(new WrapperLabel(SystemFac.EINHEIT_STUECK.trim()), "left, wrap");

		jpaWorkingOn.add(wbuKunde);
		jpaWorkingOn.add(wtfKunde, "span 4");
		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("label.lieferant")), "skip");
		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("label.kunde")), "wrap");

		jpaWorkingOn.add(wbuAnsprechpartner);
		jpaWorkingOn.add(wtfAnsprechpartner, "span 3");
		wlaMitdrucken.setText(LPMain
				.getTextRespectUISPr("iv.kopfdaten.mitdrucken"));
		wlaMitdrucken.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaMitdrucken);
		wnfPreisVK.setToolTipText(LPMain
				.getTextRespectUISPr("iv.kopfdaten.preis")
				+ " "
				+ LPMain.getTheClient().getSMandantenwaehrung());
		jpaWorkingOn.add(
				new WrapperLabel(LPMain
						.getTextRespectUISPr("iv.kopfdaten.preis")));
		jpaWorkingOn.add(wnfPreisEK);
		jpaWorkingOn.add(wnfPreisVK, "wrap");

		jpaWorkingOn.add(wlaAnhangKD);
		jpaWorkingOn.add(wefAnhangKD, "w 30%:50%:100%, gpx 110, span 3 2");
		jpaWorkingOn.add(wcbDruckBestellungKD);
		jpaWorkingOn.add(wlaRabatt);
		jpaWorkingOn.add(wnfRabattLF);
		jpaWorkingOn.add(wnfRabattKD, "wrap");
		jpaWorkingOn.add(wcbDruckRechnungKD, "skip");
		jpaWorkingOn.add(wlaZusatzRabatt);
		jpaWorkingOn.add(wnfZusatzRabattLF);
		jpaWorkingOn.add(wnfZusatzRabattKD, "wrap");

		jpaWorkingOn.add(wbuLieferant);
		jpaWorkingOn.add(wtfLieferant, "span 4");
		jpaWorkingOn.add(wlaNachlass);

		jpaWorkingOn.add(wnfNachlassLF);
		jpaWorkingOn.add(wnfNachlassKD, "wrap");
		
		jpaWorkingOn.add(wbuAnsprechpartnerLieferant);
		jpaWorkingOn.add(wtfAnsprechpartnerLieferant, "span 3");
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("iv.err.preis")), "skip");
		jpaWorkingOn.add(wnfErrechneterPreisEK);
		jpaWorkingOn.add(wnfErrechneterPreisVK, "wrap");

		jpaWorkingOn.add(wlaAnhangLF);
		jpaWorkingOn.add(wefAnhangLF, "w 30%:50%:100%, gpx 110, span 3 2");
		jpaWorkingOn.add(wcbDruckBestellungLF);
		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("iv.err.wert")));
		jpaWorkingOn.add(wnfErrechneterWertEK);
		jpaWorkingOn.add(wnfErrechneterWertVK, "wrap");

		jpaWorkingOn.add(wcbWertaufteilen, "skip 3, span");

		jpaWorkingOn.add(wlaMedium);
		jpaWorkingOn.add(wtfMedium, "span");
		
		jpaWorkingOn.add(wlaRubrik);
		jpaWorkingOn.add(wtfRubrik, "span");
		
		jpaWorkingOn.add(wlaRubrik2);
		jpaWorkingOn.add(wtfRubrik2, "span");
		
		jpaWorkingOn.add(wifArtikel.getWbuArtikel());
		jpaWorkingOn.add(wifArtikel.getWtfIdent());
		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(), "span");
		
		jpaWorkingOn.add(wlaBezeichnung);
		jpaWorkingOn.add(wtfBezeichnung, "span");
		
		jpaWorkingOn.add(wlaStichwort);
		jpaWorkingOn.add(wtfSichwort, "span");

		jpaWorkingOn.add(wlaStichwort2);
		jpaWorkingOn.add(wtfSichwort2, "span");

		jpaWorkingOn.add(wbuPersonal);
		jpaWorkingOn.add(wtfPersonal, "span");

		jpaWorkingOn.add(wlaTermin);
		jpaWorkingOn.add(wdfTermin);

		jpaWorkingOn.add(wlaTerminBis);
		jpaWorkingOn.add(wdfTerminBis, "wrap");

		wlaVerrechenbar.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaVerrechenbar, "skip, span 3");
		wlaGestoppt.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaManuellerledigt, "span");
		wlaManuellerledigt.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaGestoppt, "skip, span");

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };

		enableToolsPanelButtons(aWhichButtonIUse);

		createAndSaveAndShowButton("/com/lp/client/res/news16x16.png",
				LPMain.getTextRespectUISPr("iv.inserat.kopieren"),
				ACTION_SPECIAL_INSERAT_KOPIEREN, KeyStroke.getKeyStroke('K',
						java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_IV_INSERAT_CUD);
		
		createAndSaveAndShowButton(
				"/com/lp/client/res/shoppingcart_full16x16.png",
				LPMain.getTextRespectUISPr("iv.inserat.bestellung.fuer.selektiertenkunden"),
				ACTION_SPECIAL_BESTELLUNG_AUSLOESEN, KeyStroke.getKeyStroke(
						'B', java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_IV_INSERAT_CUD);

		createAndSaveAndShowButton("/com/lp/client/res/calculator16x16.png",
				LPMain.getTextRespectUISPr("iv.inserat.toggle.verrechenbar"),
				ACTION_SPECIAL_TOGGLE_VERRECHENBAR,
				RechteFac.RECHT_IV_INSERAT_CUD);

		

		// Focus aendern:

		FocusTraversalPolicy policy = getFocusTraversal(new JComponent[] {
				wdfDatum, wnfMenge, wbuKunde.getWrapperButton(),
				wbuAnsprechpartner, wnfPreisEK, wnfPreisVK, wnfRabattLF,
				wnfRabattKD, wnfZusatzRabattLF, wnfZusatzRabattKD,
				wnfNachlassLF, wnfNachlassKD, wcbDruckBestellungKD,
				wcbDruckRechnungKD, wbuLieferant.getWrapperButton(),
				wbuAnsprechpartnerLieferant, wcbDruckBestellungLF, wtfMedium,
				wtfRubrik, wtfRubrik2, wtfBezeichnung, wtfSichwort,
				wtfSichwort2, wbuPersonal,
				wifArtikel.getWbuArtikel().getWrapperButton(),
				wifArtikel.getWtfIdent(), wdfTermin, wdfTerminBis }); // hier
		// alles
		// eintragen
		// was
		// man
		// focussieren
		// will

		setFocusTraversalPolicy(policy); // setzen
		setFocusCycleRoot(true);// enablen

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);

		InseratDto inseratDto = new InseratDto();
		inseratDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
		inseratDto.setInseratrechnungDto(new InseratrechnungDto());

		internalFrameInserat.getTabbedPaneInserat().setInseratDto(inseratDto);

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)
				|| internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getStatusCNr().equals(LocaleFac.STATUS_TEILBEZAHLT)
				|| internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getStatusCNr().equals(LocaleFac.STATUS_BEZAHLT)) {

			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("iv.inseratkunde.updatenichtmoeglich"));
			return;
		}

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {

			boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("iv.stornoaufheben"));

			if (b == true) {
				DelegateFactory
						.getInstance()
						.getInseratDelegate()
						.storniertAufheben(
								internalFrameInserat.getTabbedPaneInserat()
										.getInseratDto().getIId());
			} else {
				return;
			}
		}

		super.eventActionUpdate(aE, bNeedNoUpdateI);

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getStatusCNr().equals(LocaleFac.STATUS_BESTELLT)
				|| internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getStatusCNr().equals(LocaleFac.STATUS_ERSCHIENEN)
				|| internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getStatusCNr().equals(LocaleFac.STATUS_VERRECHENBAR)) {

			wbuLieferant.setEnabled(false);
			wbuAnsprechpartnerLieferant.setEnabled(false);
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_PERSONAL_FROM_LISTE)) {
			dialogQueryPersonalFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE)) {
			dialogQueryAnsprechpartner(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT_FROM_LISTE)) {
			dialogQueryAnsprechpartnerLieferant(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LIEFERANT_FROM_LISTE)) {
			dialogQueryLieferant();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_TOGGLE_VERRECHENBAR)) {

			if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
					.getStatusCNr().equals(LocaleFac.STATUS_ERSCHIENEN)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getInseratDto().getStatusCNr()
							.equals(LocaleFac.STATUS_VERRECHENBAR)) {

				DelegateFactory
						.getInstance()
						.getInseratDelegate()
						.toggleVerrechenbar(
								internalFrameInserat.getTabbedPaneInserat()
										.getInseratDto().getIId());
				eventYouAreSelected(false);
			} else {

				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.hinweis"), LPMain
						.getTextRespectUISPr("iv.verrechnbar.nichtmoeglich"));
				return;
			}

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_BESTELLUNG_AUSLOESEN)) {

			Integer kundeIId = internalFrameInserat.getTabbedPaneInserat()
					.getInseratDto().getInseratrechnungDto().getKundeIId();
			internalFrameInserat.getTabbedPaneInserat()
					.bestellungenAusloesenKunde(kundeIId);
			eventYouAreSelected(false);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_INSERAT_KOPIEREN)) {
			internalFrameInserat.getTabbedPaneInserat().inseratKopieren();
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INSERAT;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {

		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setMandantCNr(LPMain.getTheClient().getMandant());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setTBelegdatum(wdfDatum.getTimestamp());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setFKdNachlass(wnfNachlassKD.getDouble());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setFLfNachlass(wnfNachlassLF.getDouble());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setFKdRabatt(wnfRabattKD.getDouble());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setFLFRabatt(wnfRabattLF.getDouble());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setFKdZusatzrabatt(wnfZusatzRabattKD.getDouble());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setFLfZusatzrabatt(wnfZusatzRabattLF.getDouble());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setTTermin(wdfTermin.getTimestamp());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setNMenge(wnfMenge.getBigDecimal());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setNNettoeinzelpreisEk(wnfPreisEK.getBigDecimal());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setNNettoeinzelpreisVk(wnfPreisVK.getBigDecimal());

		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setCBez(wtfBezeichnung.getText());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setCRubrik(wtfRubrik.getText());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setCRubrik2(wtfRubrik2.getText());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setCStichwort(wtfSichwort.getText());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setCStichwort2(wtfSichwort2.getText());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setCMedium(wtfMedium.getText());

		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setXAnhang(wefAnhangKD.getText());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setXAnhangLf(wefAnhangLF.getText());

		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setBDruckBestellungKd(wcbDruckBestellungKD.getShort());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setBDruckBestellungLf(wcbDruckBestellungLF.getShort());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setBDruckRechnungKd(wcbDruckRechnungKD.getShort());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setTTerminBis(wdfTerminBis.getTimestamp());
		internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.setBWertaufteilen(wcbWertaufteilen.getShort());
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
				|| internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getStatusCNr().equals(LocaleFac.STATUS_BESTELLT)) {
			DelegateFactory
					.getInstance()
					.getInseratDelegate()
					.storniereInserat(
							internalFrameInserat.getTabbedPaneInserat()
									.getInseratDto().getIId());
			super.eventActionDelete(e, true, true);
		} else {

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hinweis"),
					LPMain.getTextRespectUISPr("iv.storno.nichmoeglich"));
			return;
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if (wdfTerminBis.getTimestamp() != null
					&& wdfTerminBis.getTimestamp().before(
							wdfTermin.getTimestamp())) {

				DialogFactory.showModalDialog(

				LPMain.getTextRespectUISPr("lp.warning"),
						LPMain.getTextRespectUISPr("iv.kopdaten.bisvorvon"));
				return;
			}

			components2Dto();

			if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
					.getIId() == null) {
				internalFrameInserat
						.getTabbedPaneInserat()
						.getInseratDto()
						.setIId(DelegateFactory
								.getInstance()
								.getInseratDelegate()
								.createInserat(
										internalFrameInserat
												.getTabbedPaneInserat()
												.getInseratDto()));
				setKeyWhenDetailPanel(internalFrameInserat
						.getTabbedPaneInserat().getInseratDto().getIId());

			} else {

				DelegateFactory
						.getInstance()
						.getInseratDelegate()
						.updateInserat(
								internalFrameInserat.getTabbedPaneInserat()
										.getInseratDto());

			}
			super.eventActionSave(e, true);

			getInternalFrameInserat().getTabbedPaneInserat().tZuletztVerwendeterTermin = internalFrameInserat
					.getTabbedPaneInserat().getInseratDto().getTTermin();

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameInserat.getTabbedPaneInserat()
								.getInseratDto().getIId().toString());
			}
			eventYouAreSelected(false);

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPersonal) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfPersonal.setText(personalDto.formatAnrede());
					internalFrameInserat.getTabbedPaneInserat().getInseratDto()
							.setPersonalIIdVertreter((Integer) key);
				}

			} else if (e.getSource() == wifArtikel.getPanelQueryFLRArtikel()) {
				internalFrameInserat
						.getTabbedPaneInserat()
						.getInseratDto()
						.setArtikelIIdInseratart(
								(Integer) wifArtikel.getPanelQueryFLRArtikel()
										.getSelectedId());

				ArtikelDto aDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								(Integer) wifArtikel.getPanelQueryFLRArtikel()
										.getSelectedId());
				if (aDto.getArtikelsprDto() != null) {
					wtfBezeichnung.setText(aDto.getArtikelsprDto().getCBez());
				}
			} else if (e.getSource() == panelQueryFLRLieferant) {
				Integer keyLieferant = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LieferantDto lieferantDtoNew = DelegateFactory.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(keyLieferant);
				
				DelegateFactory.getInstance().getLieferantDelegate()
				.pruefeLieferant(lieferantDtoNew.getIId());
				

				String sAdresse = lieferantDtoNew.getPartnerDto()
						.formatFixTitelName1Name2();
				if (lieferantDtoNew.getPartnerDto().getCKbez() != null) {
					sAdresse = sAdresse + "  /  "
							+ lieferantDtoNew.getPartnerDto().getCKbez();
				}

				wtfLieferant.setText(sAdresse);
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.setLieferantIId(keyLieferant);

				wtfAnsprechpartnerLieferant.setText(null);
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.setAnsprechpartnerIIdLieferant(null);

				if (lieferantDtoNew.getNRabatt() != null) {
					wnfRabattLF.setDouble(lieferantDtoNew.getNRabatt());
				}
			} else if (e.getSource() == panelQueryFLRKunde) {
				Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(iIdKunde);

				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(iIdKunde);

				String sAdresse = kundeDto.getPartnerDto()
						.formatFixTitelName1Name2();
				if (kundeDto.getPartnerDto().getCKbez() != null) {
					sAdresse = sAdresse + "  /  "
							+ kundeDto.getPartnerDto().getCKbez();
				}

				wtfKunde.setText(sAdresse);
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getInseratrechnungDto().setKundeIId(iIdKunde);

				wtfAnsprechpartner.setText(null);
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getInseratrechnungDto().setAnsprechpartnerIId(null);

				// Vertreter ist Provisionsempaenger aus Kunde

				internalFrameInserat
						.getTabbedPaneInserat()
						.getInseratDto()
						.setPersonalIIdVertreter(
								kundeDto.getPersonaliIdProvisionsempfaenger());

				if (kundeDto.getPersonaliIdProvisionsempfaenger() != null) {
					PersonalDto personalDtoVerursacher = DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(
									kundeDto.getPersonaliIdProvisionsempfaenger());
					wtfPersonal.setText(personalDtoVerursacher.formatAnrede());
				}
				if (kundeDto.getFRabattsatz() != null) {
					wnfRabattKD.setDouble(kundeDto.getFRabattsatz());
				}

			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
						.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
						.formatTitelAnrede());
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getInseratrechnungDto()
						.setAnsprechpartnerIId(iIdAnsprechpartner);
			} else if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferant) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
						.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartnerLieferant.setText(ansprechpartnerDto
						.getPartnerDto().formatTitelAnrede());
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.setAnsprechpartnerIIdLieferant(iIdAnsprechpartner);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				wtfAnsprechpartner.setText(null);
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.getInseratrechnungDto().setAnsprechpartnerIId(null);
			} else if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferant) {
				wtfAnsprechpartnerLieferant.setText(null);
				internalFrameInserat.getTabbedPaneInserat().getInseratDto()
						.setAnsprechpartnerIIdLieferant(null);
			}
		}
	}

	public static FocusTraversalPolicy getFocusTraversal(
			final JComponent order[]) {
		FocusTraversalPolicy policy = new FocusTraversalPolicy() {
			java.util.List<JComponent> list = java.util.Arrays.asList(order);

			public java.awt.Component getFirstComponent(
					java.awt.Container focusCycleRoot) {
				return order[0];
			}

			public java.awt.Component getLastComponent(
					java.awt.Container focusCycleRoot) {
				return order[order.length - 1];
			}

			public java.awt.Component getComponentAfter(
					java.awt.Container focusCycleRoot,
					java.awt.Component aComponent) {
				int index = 0, x = -1;
				index = list.indexOf(aComponent);
				index++; // automatisch erhoeht, sodasz er unten nichts wegzeiehn
							// musz
				// er geht rein entweder wenn es disabled ist oder wenn es nicht
				// angezeigt wird
				if (!order[index % order.length].isEnabled()
						|| !order[index % order.length].isVisible()) {
					x = index;
					index = -1;
					// zuerst die Schleife nach hinten
					for (; x != order.length; x++) {
						if (order[x].isEnabled() && order[x].isVisible()) {
							index = x;
							break;
						}
					}
					if (index == -1) {
						x = list.indexOf(aComponent);
						for (int y = 0; y <= x; y++) {
							if (order[y].isEnabled() && order[x].isVisible()) {
								index = y;
								break;
							}
						}
					}
				}
				return order[index % order.length];
			}

			public java.awt.Component getComponentBefore(
					java.awt.Container focusCycleRoot,
					java.awt.Component aComponent) {
				int index = list.indexOf(aComponent), x = -1;
				index--;
				if (!order[(index + order.length) % order.length].isEnabled()
						|| !order[(index + order.length) % order.length]
								.isVisible()) {
					x = index;
					index = -1;
					for (; x >= 0; x--) {
						if (order[x].isEnabled() && order[x].isVisible()) {
							index = x;
							break;
						}
					}
					// wenn sich nix getan hat
					if (index == -1) {
						x = list.indexOf(aComponent);
						for (int y = order.length - 1; y >= x; y--) {
							if (order[y].isEnabled() && order[x].isVisible()) {
								index = y;
								break;
							}
						}
					}

				}
				return order[(index + order.length) % order.length];
			}

			public java.awt.Component getDefaultComponent(
					java.awt.Container focusCycleRoot) {
				return order[0];
			}

			public java.awt.Component getInitialComponent(java.awt.Window window) {
				return order[0];
			}
		};
		return policy;
	}
}
